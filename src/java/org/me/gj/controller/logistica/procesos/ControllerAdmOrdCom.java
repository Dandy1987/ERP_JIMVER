package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.OrdCompCab;
import org.me.gj.model.logistica.procesos.OrdCompDet;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerAdmOrdCom extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_aprobar, tbbtn_btn_rechazar;
    @Wire
    Listbox lst_ordcompcab, lst_ordcompdet;
    @Wire
    Button btn_buscar;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Combobox cb_moneda, cb_cabecera_periodo, cb_alm_origen, cb_alm_destino, cb_monpedcom, cb_situacion;
    @Wire
    Doublebox txt_ocd_pdesc, txt_ocd_cantped, txt_tafecto, txt_texonerado,
            txt_timp, txt_tdesc, txt_ttotal, txt_tpeso, txt_tprecio, txt_tcantidad, db_oc_tcambio,
            txt_ocd_pesounit, txt_voluunit, txt_ocd_precom,
            txt_odc_valafe, txt_odc_exonerado, txt_odc_valdes, txt_porimp, txt_odc_valimp,
            txt_odc_preven, txt_tpdes, txt_tvaldes, txt_tpreven, txt_totvolumen;
    @Wire
    Textbox txt_filtro_orden, txt_provid, txt_provdes, txt_usuadd, txt_usumod,
            txt_oc_nropedid, txt_oc_provid, txt_oc_provrazsoc,
            txt_ocd_prodid, txt_ocd_proddes, txt_ocd_glosapro,
            txt_oc_lpcid, txt_oc_lpcdes, txt_oc_conid, txt_oc_condes, txt_oc_glosa,
            txt_ocd_peso_und, txt_vol_und, txt_odc_prounimanven, txt_lugent, txt_situacion, txt_ocd_idubi, txt_ocd_desubi;
    @Wire
    Intbox txt_ocd_comdet_key;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod, d_oc_fecemi, d_oc_fecrec, d_oc_feccad;
    //Instancias de Objetos
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<OrdCompDet> objlstOrdCompDet;
    DaoOrdCom objDaoOrdComp;
    DaoAccesos objDaoAccesos;
    OrdCompCab objOrdCompCab;
    OrdCompDet objOrdCompDet;
    Productos objProductos;
    Accesos objAccesos;
    ParametrosSalida objParamActualizacionOC;
    DaoUbicaciones objdaoubicaciones = new DaoUbicaciones();
    //Variables publicas
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    int emp_id, suc_id;
    int i_selCab, i_selDet;
    String fechaActual, s_mensaje;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAdmOrdCom.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_ordcom")
    public void llenaRegistros() throws SQLException {
        objDaoOrdComp = new DaoOrdCom();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        String prov_id = "";
        String orden_id = "";
        String oc_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        fechaActual = Utilitarios.hoyAsString();
        ListModelList<Moneda> objlstMonedas;
        objlstMonedas = new DaoMoneda().listaMonedas(1);
        cb_moneda.setModel(objlstMonedas);
        //actualizamos la situacion de todas las ordenes de compra
        objParamActualizacionOC = new ParametrosSalida();
        objParamActualizacionOC = objDaoOrdComp.actualizaSituacionOC();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | Se actualizo la situacion de las o/c | " + objParamActualizacionOC.getMsgValidacion());
        if ("%%".equals(oc_sit)) {
            oc_sit = "'1','2','7'";
        }
        objlstOrdCompCab = objDaoOrdComp.listaOrdCompCab(emp_id, suc_id, "C", prov_id, oc_sit, fechaActual, fechaActual, orden_id);
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10204000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado en Procesos Administración de Orden de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado en Procesos Administración de Orden de Compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_aprobar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a aprobar un Orden de Compra");
        } else {
            tbbtn_btn_aprobar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a aprobar un Orden de Compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_rechazar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a rechazar un Orden de Compra");
        } else {
            tbbtn_btn_rechazar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a rechazar un Orden de Compra");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void buscarOrdenesCompra() throws SQLException {
        LimpiarLista();
        String resultado;
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
        String prov_id = txt_provid.getValue();
        String oc_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        String s_orden = txt_filtro_orden.getValue().isEmpty() ? "%%" : txt_filtro_orden.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if ("%%".equals(oc_sit)) {
                oc_sit = "'1','2','7'";
            }
            objlstOrdCompCab = new ListModelList();
            objlstOrdCompCab = objDaoOrdComp.listaOrdCompCab(emp_id, suc_id, "C", prov_id, oc_sit, f_emisioni, f_emisionf, s_orden);
            //Validar la tabla sin registro
            if (objlstOrdCompCab.getSize() > 0) {
                lst_ordcompcab.setModel(objlstOrdCompCab);
            } else {
                objlstOrdCompCab = null;
                lst_ordcompcab.setModel(objlstOrdCompCab);
                Messagebox.show("No existe orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        objlstOrdCompDet = null;
        lst_ordcompdet.setModel(objlstOrdCompDet);
        limpiarCamposDetalle();
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstOrdCompCab == null || objlstOrdCompCab.isEmpty()) {
            Messagebox.show("No hay orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstOrdCompCab.getSize(); i++) {
                if (objlstOrdCompCab.get(i).getOc_situacion() == 1) {
                    objlstOrdCompCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_ordcompcab.setModel(objlstOrdCompCab);
        }
    }

    @Listen("onSeleccion=#lst_ordcompcab")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstOrdCompCab.get(item.getIndex()).getOc_situacion() == 2 || objlstOrdCompCab.get(item.getIndex()).getOc_situacion() == 7) {
            Messagebox.show("La orden de compra ya no puede ser procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstOrdCompCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_ordcompcab.setModel(objlstOrdCompCab);
        }
    }

    @Listen("onSelect=#lst_ordcompcab")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos la data
        limpiarCampos();
        limpiarCamposDetalle();
        objOrdCompCab = (OrdCompCab) lst_ordcompcab.getSelectedItem().getValue();
        if (objOrdCompCab == null) {
            objOrdCompCab = objlstOrdCompCab.get(i_selCab + 1);
        }
        i_selCab = lst_ordcompcab.getSelectedIndex();
        llenarCampos(objOrdCompCab);
        llenarCamposDetalle();
        llenarCamposTotales();
        limpiarCamposDetalle();
    }

    @Listen("onSelect=#lst_ordcompdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objOrdCompDet = lst_ordcompdet.getSelectedItem().getValue();
        objOrdCompDet = (OrdCompDet) lst_ordcompdet.getSelectedItem().getValue();
        if (objOrdCompDet == null) {
            objOrdCompDet = objlstOrdCompDet.get(i_selDet + 1);
        }
        i_selDet = lst_ordcompcab.getSelectedIndex();
        llenarCamposProducto();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objOrdCompDet.getPro_id()).toString());
    }

    @Listen("onClick=#tbbtn_btn_aprobar")
    public void AprobarOrdenCompra() throws SQLException {
        int i = 0;
        for (int j = 0; j < objlstOrdCompCab.getSize(); j++) {
            if (objlstOrdCompCab.get(j).isValSelec()) {
                i = i + 1;
                break;
            }
        }
        if (i <= 0) {
            Messagebox.show("Debe seleccionar una orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            accionesOrdenCompra("Aprobar", 2);
        }
        chk_selecAll.setChecked(false);
    }

    @Listen("onClick=#tbbtn_btn_rechazar")
    public void RechazarOrdenCompra() throws SQLException {
        int i = 0;
        for (int j = 0; j < objlstOrdCompCab.getSize(); j++) {
            if (objlstOrdCompCab.get(j).isValSelec()) {
                i = i + 1;
                break;
            }
        }
        if (i <= 0) {
            Messagebox.show("Debe seleccionar una orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            accionesOrdenCompra("Rechazar", 7);
        }
        chk_selecAll.setChecked(false);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstOrdCompCab == null || objlstOrdCompCab.isEmpty()) {
            Messagebox.show("No hay ordenes de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionAdmOrdCom.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_provid.setValue("");
                        txt_provdes.setValue("");
                        txt_provid.focus();
                    }
                });
            } else {
                String prov_id = txt_provid.getValue();
                txt_provid.setValue(Utilitarios.lpad(prov_id, 8, "0"));
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(txt_provid.getValue(), "1");
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_provid")
    public void lov_proveedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantOrdComp";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerAproOrdComp");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
                cb_situacion.select();
            }
        }
    }

    @Listen("onOK=#cb_situacion")
    public void onOK_cb_situacion() {
        txt_filtro_orden.focus();
    }

    @Listen("onOK=#txt_filtro_orden")
    public void onOKOrden() throws SQLException {
        d_fecini.focus();
    }

    @Listen("onBlur=#txt_filtro_orden")
    public void onBlurOrden() throws SQLException {
        if (!txt_filtro_orden.getValue().isEmpty()) {
            if (!txt_filtro_orden.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_filtro_orden.setValue("");
                        txt_filtro_orden.focus();
                    }
                });
            } else {
                String cod = Utilitarios.lpad(txt_filtro_orden.getValue(), 10, "0");
                txt_filtro_orden.setValue(cod);
            }
        }
    }

    @Listen("onOK=#d_fecini")
    public void onOKd_fecini() throws SQLException {
        d_fecfin.focus();
    }

    @Listen("onOK=#d_fecfin")
    public void onOKd_fecfin() throws SQLException {
        btn_buscar.focus();
    }

    @Listen("onBlur=#txt_provid")
    public void onBlur_txt_oc_lpcid() {
        if (txt_provid.getValue().isEmpty()) {
            txt_provdes.setValue("");
        }
    }

    //Eventos Otros
    public void accionesOrdenCompra(String proceso, final int sit) throws SQLException {
        s_mensaje = "¿Está seguro que desea " + proceso + " las ordenes de compra seleccionadas?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            String s_mensaje = "Ordenes de compra\n\n";
                            for (int i = 0; i < objlstOrdCompCab.size(); i++) {
                                if (objlstOrdCompCab.get(i).getOc_situacion() == 1 && objlstOrdCompCab.get(i).isValSelec()) {
                                    objlstOrdCompCab.get(i).setOc_situacion(sit);
                                    objlstOrdCompCab.get(i).setOc_usuapro(objUsuarioCredential.getCuenta());
                                    objlstOrdCompCab.get(i).setOc_pcapro(objUsuarioCredential.getComputerName().toUpperCase());
                                    ParametrosSalida objParamSalida = objDaoOrdComp.aproRecOrdenCompra(objlstOrdCompCab.get(i));
                                    s_mensaje += "O.C. N° " + objlstOrdCompCab.get(i).getOc_nropedid() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                }
                            }
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                        buscarOrdenesCompra();
                    }
                });
    }

    public void llenarCampos(OrdCompCab objOrdCompCab) {
        txt_oc_nropedid.setValue(objOrdCompCab.getOc_nropedid());
        txt_oc_provid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_provid()), 8, "0"));
        txt_oc_provrazsoc.setValue(objOrdCompCab.getOc_provrazsoc());
        try {
            d_oc_fecemi.setValue(objOrdCompCab.getOc_fecemi());
            d_oc_fecrec.setValue(objOrdCompCab.getOc_fecrec());
            d_oc_feccad.setValue(objOrdCompCab.getOc_feccad());
        } catch (ParseException ex) {
            Messagebox.show("Error de formato en las fechas: " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
        txt_oc_lpcid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_lpcid()), 4, "0"));
        txt_oc_lpcdes.setValue(objOrdCompCab.getOc_lpcdes());
        txt_oc_conid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_conid()), 3, "0"));
        txt_oc_condes.setValue(objOrdCompCab.getOc_condes());
        cb_moneda.setSelectedItem(Utilitarios.valorPorTexto(cb_moneda, objOrdCompCab.getOc_moneda()));
        cb_cabecera_periodo.setValue(String.valueOf(objOrdCompCab.getOc_periodo()));
        cb_alm_origen.setValue(objOrdCompCab.getOc_almorides());
        cb_alm_destino.setValue(objOrdCompCab.getOc_almdesdes());
        txt_oc_glosa.setValue(objOrdCompCab.getOc_glosa());
        txt_lugent.setValue(cb_alm_destino.getValue().isEmpty() ? "" : "MZ. A LT. 32 RETABLO II ETAPA - COMAS");
        db_oc_tcambio.setValue(objOrdCompCab.getOc_tcambio());
        txt_oc_glosa.setValue(objOrdCompCab.getOc_glosa());
        txt_situacion.setValue(objOrdCompCab.getOc_sitdes());
        txt_usuadd.setValue(objOrdCompCab.getOc_usuadd());
        d_fecadd.setValue(objOrdCompCab.getOc_fecadd());
        txt_usumod.setValue(objOrdCompCab.getOc_usumod());
        d_fecmod.setValue(objOrdCompCab.getOc_fecmod());
    }

    public void llenarCamposDetalle() throws SQLException {
        long oc_nropedkey = objOrdCompCab.getOc_nropedkey();
        String oc_ind = "C";
        objlstOrdCompDet = null;
        objlstOrdCompDet = objDaoOrdComp.listaOrdCompDet(emp_id, suc_id, oc_nropedkey, oc_ind);
        lst_ordcompdet.setModel(objlstOrdCompDet);
    }

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_tafecto.setValue(data[0]);
        txt_texonerado.setValue(data[1]);
        txt_tpdes.setValue(data[2]);
        txt_tvaldes.setValue(data[3]);
        txt_timp.setValue(data[4]);
        txt_tpreven.setValue(data[5]);
        txt_tpeso.setValue(data[6]);
        txt_totvolumen.setValue(data[7]);
    }

    public double[] calculosTotal() {
        int i;
        double data[] = new double[8];
        for (i = 0; i < objlstOrdCompDet.getSize(); i++) {
            data[0] = data[0] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_vafecto();
            data[1] = data[1] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_exonerado();
            data[2] = data[2] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_pdesc();
            data[3] = data[3] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_vdesc();
            data[4] = data[4] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_vimpto();
            data[5] = data[5] + ((OrdCompDet) objlstOrdCompDet.get(i)).getOcd_vtotal();
            data[6] = data[6] + ((OrdCompDet) objlstOrdCompDet.get(i)).getPro_pesototal();
            data[7] = data[7] + ((OrdCompDet) objlstOrdCompDet.get(i)).getPro_voltotal();
        }
        return data;
    }

    public void limpiarCampos() {
        txt_oc_nropedid.setValue("");
        txt_oc_provid.setValue("");
        txt_oc_provrazsoc.setValue("");
        d_oc_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_oc_fecrec.setValue(Utilitarios.hoyAsFecha());
        d_oc_feccad.setValue(Utilitarios.hoyAsFecha());
        txt_oc_lpcid.setValue("");
        txt_oc_lpcdes.setValue("");
        txt_oc_conid.setValue("");
        txt_oc_condes.setValue("");
        cb_moneda.setSelectedIndex(-1);
        cb_alm_origen.setSelectedIndex(-1);
        cb_alm_destino.setSelectedIndex(-1);
        cb_cabecera_periodo.setSelectedIndex(-1);
        db_oc_tcambio.setValue(null);
        txt_oc_glosa.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_ocd_prodid.setValue("");
        txt_ocd_proddes.setValue("");
        txt_ocd_pdesc.setValue(null);
        txt_ocd_cantped.setValue(null);
        txt_ocd_glosapro.setValue(null);
        txt_ocd_comdet_key.setValue(null);
        txt_ocd_pesounit.setValue(null);
        txt_ocd_peso_und.setValue("");
        txt_voluunit.setValue(null);
        txt_vol_und.setValue("");
        txt_ocd_precom.setValue(null);
        txt_odc_prounimanven.setValue("");
        txt_odc_valafe.setValue(null);
        txt_odc_exonerado.setValue(null);
        txt_ocd_pdesc.setValue(null);
        txt_odc_valdes.setValue(null);
        txt_porimp.setValue(null);
        txt_odc_valimp.setValue(null);
        txt_odc_preven.setValue(null);
        txt_ocd_idubi.setValue("");
        txt_ocd_desubi.setValue("");
    }

    public void llenarCamposProducto() throws SQLException {
        objProductos = (new DaoProductos()).listaPro(objOrdCompDet.getPro_id());
        String s_key = String.valueOf(objOrdCompDet.getOcd_item());
        txt_ocd_comdet_key.setValue(Integer.parseInt(s_key));// ITEM
        txt_ocd_prodid.setValue(objOrdCompDet.getPro_id());//COD Productos
        txt_ocd_proddes.setValue(objOrdCompDet.getPro_des());//Descripcion Producto
        txt_ocd_pesounit.setValue(objProductos.getPro_presminven() * objProductos.getPro_peso());//Peso U.
        txt_ocd_peso_und.setValue(objProductos.getPro_unipeso());//Peso U.Inicial
        txt_voluunit.setValue(objProductos.getPro_presminven() * objProductos.getPro_vol());//VOL U.
        txt_vol_und.setValue("MT3");//VOL INICIAL
        txt_ocd_cantped.setValue(objOrdCompDet.getOcd_cantped());//Cantidad
        txt_ocd_precom.setValue(Double.parseDouble(objOrdCompDet.getOcd_sprecio()));///P.Unit
        txt_odc_prounimanven.setValue(objOrdCompDet.getPro_unimanven());//UND.VTA
        txt_odc_valafe.setValue(objOrdCompDet.getOcd_vafecto());/// V AFECTO
        txt_odc_exonerado.setValue(objOrdCompDet.getOcd_exonerado());// V EXO
        txt_ocd_pdesc.setValue(objOrdCompDet.getOcd_pdesc());//% DSCTO
        txt_odc_valdes.setValue(objOrdCompDet.getOcd_vdesc());//V DSCTO
        txt_porimp.setValue(objOrdCompDet.getOcd_pimpto());// % IGV
        txt_odc_valimp.setValue(objOrdCompDet.getOcd_vimpto());//V IGV
        txt_ocd_glosapro.setValue(objOrdCompDet.getOcd_glosa());// Glosa
        txt_odc_preven.setValue(objOrdCompDet.getOcd_vtotal());// P.VENTA
        txt_ocd_idubi.setValue(objOrdCompDet.getPro_ubi());
        txt_ocd_desubi.setValue(objdaoubicaciones.descripcionUbicacion(objOrdCompDet.getPro_ubi()));
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }
}
