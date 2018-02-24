package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerCosteoAuto extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_costear, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_costeo, lst_costeodetalle;
    @Wire
    Textbox txt_nescab_tipnotaes, txt_nomtipnotaes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo, cb_situacion;
    @Wire
    Button btn_consultar;
    @Wire
    Datebox d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Window w_costeonotes;
    @Wire
    Tab tab_lista, tab_detalle;
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<ManPer> objlstPeriodo;
    ListModelList<NotaESCab> objlstNotaESCab,objlstNotaESDet;
    Guias objGuias;
    NotaESCab objNotaESCab;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    DaoManPer objDaoManPer;
    DaoCosteo objDaoCosteo;
    ParametrosSalida objParametrosSalida;
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "";
    String notaes = "";
    String modoEjecucion;
    int i_selCab;
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCosteoAuto.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_notes")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objAccesos = new Accesos();
        objGuias = new Guias();
        objNotaESCab = new NotaESCab();
        objDaoAccesos = new DaoAccesos();
        objDaoManPer = new DaoManPer();
        objDaoCosteo = new DaoCosteo();
        objParametrosSalida = new ParametrosSalida();
        objlstPeriodo = new ListModelList<ManPer>();
        objlstPeriodo = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objlstPeriodo);
        objlstPeriodo.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        txt_nescab_tipnotaes.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10211010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Costeo de Nota E/S Automático con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado en Procesos Costeo de Nota E/S Automático con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_costear.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a Costear Nota E/S");
        } else {
            tbbtn_btn_costear.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a Costear Nota E/S");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_nescab_tipnotaes.getValue().toUpperCase().trim();
        String s_periodo = cb_periodo.getValue();
        String fecha = d_fecemi.getValue() == null ? "" : sdf.format(d_fecemi.getValue());
        String s_situacion;
        s_situacion = cb_situacion.getSelectedIndex() == -1 ? "" : "'" + cb_situacion.getSelectedItem().getValue().toString() + "'";
        objlstNotaESCab = new ListModelList<NotaESCab>();
        objlstNotaESCab = objDaoCosteo.BusquedaNotaES(s_periodo, fecha, s_consulta, 1, s_situacion);
        if (objlstNotaESCab.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstNotaESCab.getSize() + " registro(s)");
        }
        if (objlstNotaESCab.getSize() > 0) {
            lst_costeo.setModel(objlstNotaESCab);
        } else {
            objlstNotaESCab = null;
            lst_costeo.setModel(objlstNotaESCab);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        chk_selecAll.setChecked(false);
        limpiaAuditoria();
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotaESCab == null || objlstNotaESCab.isEmpty()) {
            Messagebox.show("No hay pedidos de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaESCab.getSize(); i++) {
                //if (objlstNotaESCab.get(i).getSituacion().equals("NORMAL")) {
                    objlstNotaESCab.get(i).setValSelec(chk_selecAll.isChecked());
                //}
            }
            lst_costeo.setModel(objlstNotaESCab);
        }
    }

    @Listen("onSeleccion=#lst_costeo")
    public void seleccionaRegistro(ForwardEvent evt) throws SQLException {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        
        
        objlstNotaESCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_costeo.setModel(objlstNotaESCab);
        /*
        if (!objlstNotaESCab.get(item.getIndex()).getSituacion().equals("NORMAL")) {
            Messagebox.show("La nota no puede ser costeada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstNotaESCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_costeo.setModel(objlstNotaESCab);
        }
            */
    }
    
    @Listen("onSelect=#lst_costeo")
    public void seleccionaRegistro2() throws SQLException {
        objNotaESCab = (NotaESCab) lst_costeo.getSelectedItem().getValue();
        if (objNotaESCab == null) {
            objNotaESCab = objlstNotaESCab.get(i_sel + 1);
        }
        i_sel = lst_costeo.getSelectedIndex();
        objlstNotaESDet = new ListModelList<NotaESCab>();
        objlstNotaESDet = objDaoCosteo.listaDetalleCosteoAuto(objNotaESCab.getNescab_id(), objNotaESCab.getNescab_nroserie() + objNotaESCab.getNescab_nrodoc());
        lst_costeodetalle.setModel(objlstNotaESDet);
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | seleccionó el registro con el código " + objNotaESCab.getNescab_id());
    }

    @Listen("onClick=#tbbtn_btn_costear")
    public void costearNota() throws SQLException {
        if (objlstNotaESCab == null || objlstNotaESCab.isEmpty()) {
            Messagebox.show("No hay registros para costear", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaESCab.getSize(); j++) {
                if (objlstNotaESCab.get(j).isValSelec()) {
                    i = i + 1;
                    break;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                actualizarNotaES();
                chk_selecAll.setChecked(false);
                /*if (lst_costeo.getSelectedIndex() >= 0) {
                 objNotaESCab = (NotaESCab) lst_costeo.getSelectedItem().getValue();
                 if (objNotaESCab == null) {
                 objNotaESCab = objlstNotaESCab.get(i_selCab + 1);
                 }
                 actualizarNotaES(objNotaESCab);
                 } else {
                 Messagebox.show("Seleccione un registro para costear");
                 }*/
            }

        }
    }

    //Eventos Secundarios - Validaciones 
    @Listen("onOK=#txt_nescab_tipnotaes")
    public void lov_notaes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nescab_tipnotaes.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "CosteoAuto";
                mapeo.put("txt_nescab_tipnotaes", txt_nescab_tipnotaes);
                mapeo.put("txt_nomtipnotaes", txt_nomtipnotaes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerCosteoAuto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNotaES.zul", null, mapeo);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }

    @Listen("onBlur=#txt_nescab_tipnotaes")
    public void valida_tipnotaes() throws SQLException {
        if (!txt_nescab_tipnotaes.getValue().isEmpty()) {
            if (!txt_nescab_tipnotaes.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nescab_tipnotaes.setValue("");
                                    txt_nomtipnotaes.setValue("");
                                    txt_nescab_tipnotaes.focus();
                                }
                            }
                        });
            } else {
                txt_nescab_tipnotaes.setValue(Utilitarios.lpad(txt_nescab_tipnotaes.getValue(), 3, "0"));
                Guias objGuia = new DaoManNotaES().BusquedaGuia(txt_nescab_tipnotaes.getValue());
                if (objGuia.getCodigo() == null) {
                    Messagebox.show("El código de la nota de E/S no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_nescab_tipnotaes.setValue("");
                                        txt_nomtipnotaes.setValue("");
                                        txt_nescab_tipnotaes.focus();
                                    }
                                }
                            });
                } else {
                    txt_nescab_tipnotaes.setValue(objGuia.getCodigo());
                    txt_nomtipnotaes.setValue(objGuia.getDesGui());
                }
            }
        } else {
            txt_nomtipnotaes.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros
    public void actualizarNotaES() throws SQLException {
        //notaes = objNotaESCab.getNescab_id();
        //if (objNotaESCab.getNescab_costeo() == 2) {
        //if (objNotaESCab.getConcepto().equals("N")) {
        s_mensaje = "¿Está Ud. seguro que desea costear la Nota E/S?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws SQLException {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            for (int i = 0; i < objlstNotaESCab.size(); i++) {
                                //Si el indicador de costeo es NO = 2 y si esta seleccionado
                                if (objlstNotaESCab.get(i).getNescab_costeo() == 2 && objlstNotaESCab.get(i).isValSelec()) {                                    
                                    //Si no existe diferencia en soles y en cantidades
                                    if( objlstNotaESCab.get(i).getSituacion().equals("NORMAL") ){
                                        objParametrosSalida = objDaoCosteo.actualizarCosteoNotaES( objlstNotaESCab.get(i).getEmp_id() , objlstNotaESCab.get(i).getSuc_id() , objlstNotaESCab.get(i).getNescab_id() , 1);
                                    }
                                    //Si hay diferencia en cantidades y soles
                                    else if(objlstNotaESCab.get(i).getSituacion().equals("FALTA PROD / FALTA SOLES")){                                        
                                        objParametrosSalida = objDaoCosteo.actualizarCosteoNotaES(objlstNotaESCab.get(i).getEmp_id() , objlstNotaESCab.get(i).getSuc_id() , objlstNotaESCab.get(i).getNescab_id() , 2);
                                    }
                                    //Si hay diferencia soles
                                    else if(objlstNotaESCab.get(i).getSituacion().equals("FALTA SOLES")){                                        
                                        objParametrosSalida = objDaoCosteo.actualizarCosteoNotaES(objlstNotaESCab.get(i).getEmp_id() , objlstNotaESCab.get(i).getSuc_id() , objlstNotaESCab.get(i).getNescab_id() , 3);
                                    }
                                }
                                
                            }
                        }
                        busquedaRegistros();
                    }
                });

        /*} else {
         Messagebox.show("Por favor verifique la situacion de los documentos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         }
         } else {
         Messagebox.show("La Nota E/S ya está costeada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         }*/
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        lst_costeo.setModel(objlstNotaESCab);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenarCampos() {
        txt_usuadd.setValue(objNotaESCab.getNescab_usuadd());
        d_fecadd.setValue(objNotaESCab.getNescab_fecadd());
        txt_usumod.setValue(objNotaESCab.getNescab_usumod());
        d_fecmod.setValue(objNotaESCab.getNescab_fecmod());
    }
}
