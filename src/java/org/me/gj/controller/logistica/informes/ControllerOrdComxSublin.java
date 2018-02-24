package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoSubLineas;
import org.me.gj.controller.logistica.procesos.DaoOrdCom;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.OrdCompSublin;
import org.me.gj.model.logistica.mantenimiento.SubLineas;
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

public class ControllerOrdComxSublin extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Textbox txt_slinid, txt_slindes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod;
    @Wire
    Button btn_buscarordenes;
    @Wire
    Listbox lst_ordcompcabxslin, lst_ordcompcab;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Doublebox txt_totcant, txt_totdes, txt_totafec, txt_totinafec, txt_totexo, txt_totigv, txt_totventa;
    //Instancias de Objetos
    ListModelList<OrdCompSublin> objlstOrdCompCabxSubLin;
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<ManPer> objlstPeriodos;
    DaoOrdCom objDaoOrdCom;
    DaoAccesos objDaoAccesos;
    DaoManPer objDaoPeriodo;
    ManPer objPeriodo;
    OrdCompSublin objOrdCompSubLin;
    OrdCompCab objOrdCompCab;
    Accesos objAccesos;
    //Variables publicas
    int emp_id, suc_id;
    int i_selCab;
    String fechaActual;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerOrdComxSublin.class);

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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10301040, usuario, empresa, sucursal);
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
            String slin_id = txt_slinid.getValue().isEmpty() ? "%%" : String.valueOf(Integer.parseInt(txt_slinid.getValue()));
            objlstOrdCompCabxSubLin = null;
            lst_ordcompcabxslin.setModel(objlstOrdCompCabxSubLin);
            objlstOrdCompCab = null;
            objlstOrdCompCabxSubLin = objDaoOrdCom.listaTotalxSubLinea(emp_id, suc_id, per, "%45%", f_emisioni, f_emisionf, slin_id);
            lst_ordcompcab.setModel(objlstOrdCompCab);
            if (objlstOrdCompCabxSubLin.getSize() > 0) {
                lst_ordcompcabxslin.setModel(objlstOrdCompCabxSubLin);
                llenarCamposTotales();
            } else {
                objlstOrdCompCabxSubLin = null;
                lst_ordcompcabxslin.setModel(objlstOrdCompCabxSubLin);
                limpiarCamposTotales();
                Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        LimpiarCampos();
    }

    @Listen("onSelect=#lst_ordcompcabxslin")
    public void seleccionaRegistroTotal() throws SQLException {
        objOrdCompSubLin = (OrdCompSublin) lst_ordcompcabxslin.getSelectedItem().getValue();
        String per = objOrdCompSubLin.getPeriodo();
        String fecini = objOrdCompSubLin.getFecemi();
        String fecfinal = objOrdCompSubLin.getFecfin();
        String lin_id = String.valueOf(objOrdCompSubLin.getSlin_id());
        objlstOrdCompCab = null;
        objlstOrdCompCab = objDaoOrdCom.OrdCompxSubLinea(emp_id, suc_id, per, "%45%", fecini, fecfinal, lin_id);
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
        if (objlstOrdCompCabxSubLin == null || objlstOrdCompCabxSubLin.isEmpty()) {
            Messagebox.show("No hay orden de compra por sublinea para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_ordcompcabxslin.getSelectedIndex() >= 0) {
                objOrdCompSubLin = (OrdCompSublin) lst_ordcompcabxslin.getSelectedItem().getValue();
                if (objOrdCompSubLin == null) {
                    objOrdCompSubLin = objlstOrdCompCabxSubLin.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("slin_id", objOrdCompSubLin.getSlin_id());
                objMapObjetos.put("slin_des", objOrdCompSubLin.getSlin_des());
                objMapObjetos.put("svimpto", objOrdCompSubLin.getSvimpto());
                objMapObjetos.put("svdesc", objOrdCompSubLin.getSvdesc());
                objMapObjetos.put("svafecto", objOrdCompSubLin.getSvafecto());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionOrdComxSubLin.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una sublinea para imprimir sus ordenes de compra");
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_slinid")
    public void lovSubLineas() {
        if (bandera == false) {
            bandera = true;
            if (txt_slinid.getValue().isEmpty()) {
                Textbox txt_auxiliar = new Textbox();
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "OrdComSlin";
                objMapObjetos.put("txt_sublinid", txt_slinid);
                objMapObjetos.put("txt_sublindes", txt_slindes);
                objMapObjetos.put("txt_linid", txt_auxiliar);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompxSublin");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSublineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarordenes.focus();
            }
        }
    }

    @Listen("onBlur=#txt_slinid")
    public void validaSubLineas() throws SQLException {
        if (!txt_slinid.getValue().isEmpty()) {
            if (!txt_slinid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_slinid.setValue("");
                        txt_slindes.setValue("");
                        txt_slinid.focus();
                    }
                });
            } else {
                String sublin_id = txt_slinid.getValue();
                SubLineas objSublinea = new DaoSubLineas().buscarSublineaxCodigo("%%", sublin_id);
                if (objSublinea == null) {
                    Messagebox.show("El código de sublinea no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_slinid.setValue("");
                            txt_slindes.setValue("");
                            txt_slinid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la SubLinea " + objSublinea.getSlin_des() + " y ha encontrado 1 registro(s)");
                    txt_slinid.setValue(objSublinea.getSlin_cod());
                    txt_slindes.setValue(objSublinea.getSlin_des());
                }
            }
        }else{
            txt_slindes.setValue("");
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
        objlstOrdCompCabxSubLin = null;
        objlstOrdCompCabxSubLin = new ListModelList<OrdCompSublin>();
        lst_ordcompcabxslin.setModel(objlstOrdCompCabxSubLin);
    }
    
    public double[] calculosTotal() {
        int i, cont = 1;
        double data[] = new double[7];
        for (i = 0; i < objlstOrdCompCabxSubLin.getSize(); i++) {
            //data[0] = cont++;
            data[0] = data[0] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getCant();
            data[1] = data[1] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVdesc();
            data[2] = data[2] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVafecto();
            data[3] = data[3] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVinafecto();
            data[4] = data[4] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVexonerado();
            data[5] = data[5] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVimpto();
            data[6] = data[6] + ((OrdCompSublin) objlstOrdCompCabxSubLin.get(i)).getVtotal();
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
