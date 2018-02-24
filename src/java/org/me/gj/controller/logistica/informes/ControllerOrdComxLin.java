package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoLineas;
import org.me.gj.controller.logistica.procesos.DaoOrdCom;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.OrdCompLin;
import org.me.gj.model.logistica.mantenimiento.Lineas;
import org.me.gj.model.logistica.procesos.OrdCompCab;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerOrdComxLin extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Textbox txt_linid, txt_lindes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod;
    @Wire
    Button btn_buscarordenes;
    @Wire
    Listbox lst_ordcompcabxlin, lst_ordcompcab;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Doublebox txt_totcant, txt_totdes, txt_totafec, txt_totinafec, txt_totexo, txt_totigv, txt_totventa;
    //Instancias de Objetos
    ListModelList<OrdCompLin> objlstOrdCompCabxLin;
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<ManPer> objlstPeriodos;
    DaoOrdCom objDaoOrdCom;
    DaoManPer objDaoPeriodo;
    DaoAccesos objDaoAccesos;
    ManPer objPeriodo;
    OrdCompLin objOrdCompLin;
    OrdCompCab objOrdCompCab;
    Accesos objAccesos;
    //Variables publicas
    int emp_id, suc_id;
    int i_selCab;
    String fechaActual;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerOrdComxLin.class);
    

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objDaoPeriodo = new DaoManPer();
        objDaoOrdCom = new DaoOrdCom();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objlstPeriodos = objDaoPeriodo.listaPeriodos(1);
        objlstPeriodos.add(new ManPer("", ""));
        cb_periodo.setModel(objlstPeriodos);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10301030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Orden de Compra por Sublinea con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Orden de Compra por Sublinea con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Orden de Compra por Sublinea");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Orden de Compra por Sublinea");
        }
    }

    @Listen("onClick=#btn_buscarordenes")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String resultado/* = ""*/;
        Date fecha_emisioni = d_fecini.getValue();
        Date fecha_emisionf = d_fecfin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_fecini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_fecfin.getValue());
        }
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_ordcompcab.setSelectedIndex(-1);
            limpiarCamposTotales();
        } else {
            String per = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedItem().getValue() == "" ? "%%" : cb_periodo.getSelectedItem().getValue().toString();
            String lin_id = txt_linid.getValue().isEmpty() ? "%%" : String.valueOf(Integer.parseInt(txt_linid.getValue()));
            objlstOrdCompCabxLin = null;
            lst_ordcompcabxlin.setModel(objlstOrdCompCabxLin);
            objlstOrdCompCab = null;
            objlstOrdCompCabxLin = objDaoOrdCom.listaTotalxLinea(emp_id, suc_id, per, "%45%", f_emisioni, f_emisionf, lin_id);
            lst_ordcompcab.setModel(objlstOrdCompCab);
            if (objlstOrdCompCabxLin.getSize() > 0) {
                lst_ordcompcabxlin.setModel(objlstOrdCompCabxLin);
                llenarCamposTotales();
            } else {
                objlstOrdCompCabxLin = null;
                lst_ordcompcabxlin.setModel(objlstOrdCompCabxLin);
                limpiarCamposTotales();
                Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        LimpiarCampos();
    }

    @Listen("onSelect=#lst_ordcompcabxlin")
    public void seleccionaRegistroTotal() throws SQLException {
        objOrdCompLin = (OrdCompLin) lst_ordcompcabxlin.getSelectedItem().getValue();
        String per = objOrdCompLin.getPeriodo();
        String fecini = objOrdCompLin.getFecemi();
        String fecfinal = objOrdCompLin.getFecfin();
        String lin_id = String.valueOf(Integer.parseInt(objOrdCompLin.getLin_id()));
        objlstOrdCompCab = null;
        objlstOrdCompCab = objDaoOrdCom.OrdCompxLinea(emp_id, suc_id, per, "%45%", fecini, fecfinal, lin_id);
        LimpiarCampos();
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }

    @Listen("onSelect=#lst_ordcompcab")
    public void seleccionaRegistro() {
        objOrdCompCab = (OrdCompCab) lst_ordcompcab.getSelectedItem().getValue();
        LimpiarCampos();
        txt_usuadd.setValue(objOrdCompCab.getOc_usuadd());
        d_fecadd.setValue(objOrdCompCab.getOc_fecadd());
        txt_usumod.setValue(objOrdCompCab.getOc_usumod());
        d_fecmod.setValue(objOrdCompCab.getOc_fecmod());
    }
    
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstOrdCompCabxLin == null || objlstOrdCompCabxLin.isEmpty()) {
            Messagebox.show("No hay ordenes de compra por linea para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_ordcompcabxlin.getSelectedIndex() >= 0) {
                objOrdCompLin = (OrdCompLin) lst_ordcompcabxlin.getSelectedItem().getValue();
                if (objOrdCompLin == null) {
                    objOrdCompLin = objlstOrdCompCabxLin.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("lin_id", objOrdCompLin.getLin_id());
                objMapObjetos.put("lin_des", objOrdCompLin.getLin_des());
                objMapObjetos.put("svimpto", objOrdCompLin.getSvimpto());
                objMapObjetos.put("svdesc", objOrdCompLin.getSvdesc());
                objMapObjetos.put("svafecto", objOrdCompLin.getSvafecto());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionOrdComxLin.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una linea para imprimir sus ordenes de compra");
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_linid")
    public void lovLineas() {
        if (bandera == false) {
            bandera = true;
            if (txt_linid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "OrdComLin";
                objMapObjetos.put("txt_linid", txt_linid);
                objMapObjetos.put("txt_lindes", txt_lindes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompxLin");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovLineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarordenes.focus();
            }
        }
    }

    @Listen("onBlur=#txt_linid")
    public void validaLineas() throws SQLException {
        if (!txt_linid.getValue().isEmpty()) {
            if (!txt_linid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_linid.setValue("");
                        txt_lindes.setValue("");
                        txt_linid.focus();
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_linid.getValue());
                Lineas objlinea = new DaoLineas().busquedaLineaxCodigo(ven_id);
                if (objlinea == null) {
                    Messagebox.show("El código de linea no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_linid.setValue("");
                            txt_lindes.setValue("");
                            txt_linid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea " + objlinea.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_linid.setValue(objlinea.getTab_subdir());
                    txt_lindes.setValue(objlinea.getTab_subdes());
                }
            }
        }else{
            txt_lindes.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros
    public void LimpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstOrdCompCabxLin = null;
        objlstOrdCompCabxLin = new ListModelList<OrdCompLin>();
        lst_ordcompcab.setModel(objlstOrdCompCabxLin);
    }
    
    public double[] calculosTotal() {
        int i, cont = 1;
        double data[] = new double[7];
        for (i = 0; i < objlstOrdCompCabxLin.getSize(); i++) {
            //data[0] = cont++;
            data[0] = data[0] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getCant();
            data[1] = data[1] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVdesc();
            data[2] = data[2] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVafecto();
            data[3] = data[3] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVinafecto();
            data[4] = data[4] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVexonerado();
            data[5] = data[5] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVimpto();
            data[6] = data[6] + ((OrdCompLin) objlstOrdCompCabxLin.get(i)).getVtotal();
        }
        return data;
    }
    
    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_totcant.setValue(data[0]);
        txt_totdes.setValue(data[1]);
        txt_totafec.setValue(data[2]);
        txt_totinafec.setValue(data[3]);
        txt_totexo.setValue(data[4]);
        txt_totigv.setValue(data[5]);
        txt_totventa.setValue(data[6]);
        txt_totventa.setStyle("text-align: right; background-color: #BEF781;color: #2E2E2E;");
    }
    
    public void limpiarCamposTotales() {
        txt_totcant.setValue(null);
        txt_totdes.setValue(null);
        txt_totafec.setValue(null);
        txt_totinafec.setValue(null);
        txt_totexo.setValue(null);
        txt_totigv.setValue(null);
        txt_totventa.setValue(null);
        txt_totventa.setStyle(null);
    }
}
