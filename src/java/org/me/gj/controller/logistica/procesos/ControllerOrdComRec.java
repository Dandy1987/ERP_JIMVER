package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoImpuesto;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.logistica.mantenimiento.DaoLineas;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.mantenimiento.DaoSubLineas;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.logistica.mantenimiento.Lineas;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.mantenimiento.SubLineas;
import org.me.gj.model.logistica.procesos.ComprasDet;
import org.me.gj.model.logistica.procesos.NotaESDet;
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
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerOrdComRec extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_manprocordcomp;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_provid, txt_provdes, txt_prodid, txt_proddes, txt_linid, txt_lindes, txt_sublinid, txt_sublindes,
            txt_filtro_orden, txt_usuadd, txt_usumod,//Campos Busqueda
            txt_oc_nropedid, txt_oc_provid, txt_oc_provrazsoc, txt_oc_conid, txt_oc_condes, txt_oc_lpcid, txt_oc_lpcdes, txt_oc_glosa, txt_listapreid, txt_listapredes,
            txt_oc_almoriid, txt_oc_almorides, txt_oc_almdesid, txt_oc_almdesdes, txt_oc_mondes,//Campos Cabecera
            txt_ocd_prodid, txt_ocd_proddes, txt_ocd_glosapro,//Campos Detalle
            txt_oc_nropedidman, txt_providman, txt_provdesmanm, txt_oc_glosaman, txt_oc_almdesman, txt_gcab_nrodoc,//Campo enviado desde el mantenimiento genera nota de Cambio
            txt_condes, txt_conid, txt_glosa,//genera Factura Proveedor
            txt_ocd_peso_und, txt_vol_und, txt_odc_prounimanven, txt_lugent, txt_situacion;
    @Wire
    Combobox cb_situacion,//Campos Busqueda
            cb_almori, cb_almdes, cb_gcab_tipdoc,//Campo enviado desde el mantenimiento genera nota de Cambio
            cb_moneda, cb_cabecera_periodo, cb_alm_origen, cb_alm_destino, cb_monpedcom;//------>Datos Cabecera Orden de Compra
    @Wire
    Checkbox chk_efectivo;
    @Wire
    Doublebox db_oc_tcambio,//Campos Cabecera
            txt_tpeso, txt_tprecio, txt_tcantidad, txt_tafecto, txt_texonerado, txt_tdesc, txt_timp, txt_ttotal,//Campos Totales
            txt_ocd_pdesc, txt_ocd_cantped,//Campos Detalle
            txt_lvventaped, txt_lvventafac, txt_ligvped, txt_ligvfac, txt_lpventaped, txt_lpventafac, txt_dsctgral, txt_dsctfin,
            txt_lbruto, txt_ldsctogral, txt_ldsctoitem, txt_lneto, txt_lvventa, txt_ligv, txt_lsubtotal,//Campos total de Factura Proveedor
            txt_ocd_pesounit, txt_voluunit, txt_ocd_precom,
            txt_odc_valafe, txt_odc_exonerado, txt_odc_valdes, txt_porimp, txt_odc_valimp,
            txt_odc_preven, txt_tpdes, txt_tvaldes, txt_tpreven, txt_totvolumen;
    @Wire
    Intbox txt_ocd_comdet_key;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod,//Campos Busqueda
            d_oc_fecemi, d_oc_fecrec, d_oc_feccad;//Campos Cabecera
    @Wire
    Listbox lst_ordcompcab, lst_ordcompdet,
            lst_notes_det, lst_comprasdet;
    @Wire
    Label lbl_total,
            lbl_tdsctos, lbl_valventa, lbl_valimp, lbl_valtotal;
    @Wire
    Button tbbtn_btn_buscar;

    //Instancias de Objetos
    ListModelList<ComprasDet> objlstComprasDet = new ListModelList<ComprasDet>();
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<OrdCompDet> objlstOrdCompDet, objlstNotaES, objlstGenFactProv;
    ListModelList<NotaESDet> objlstNotaESDet;
    DaoOrdCom objDaoOrdComp;
    DaoAccesos objDaoAccesos;
    DaoImpuesto objDaoImpuesto;
    OrdCompCab objOrdCompCab;
    OrdCompDet objOrdCompDet;
    Accesos objAccesos;
    Productos objProducto;
    ParametrosSalida objParamActualizacionOC;
    //Variables publicas
    int emp_id, suc_id, i_selCab;
    String fechaActual;
    String controlador;
    String s_mensaje;
    Map parametros;
    public static boolean bandera = false;
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    DecimalFormat df4 = new DecimalFormat("###,##0.0000", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerOrdComRec.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoImpuesto = new DaoImpuesto();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (!parametros.isEmpty()) {
            if (parametros.get("validaBusqueda").equals("mantNotaES")) {
                txt_oc_nropedidman = (Textbox) parametros.get("txt_nescab_ocnropedkey");
                txt_providman = (Textbox) parametros.get("txt_nescab_provid");
                txt_provdesmanm = (Textbox) parametros.get("txt_nescab_provrazsoc");
                txt_oc_glosaman = (Textbox) parametros.get("txt_nescab_glosa");
                txt_listapreid = (Textbox) parametros.get("txt_listapreid");
                txt_listapredes = (Textbox) parametros.get("txt_listapredes");
                cb_almori = (Combobox) parametros.get("cb_nescab_almori");
                cb_almdes = (Combobox) parametros.get("cb_nescab_almdes");
                lst_notes_det = (Listbox) parametros.get("lst_notes_det");
            } else if (parametros.get("validaBusqueda").equals("GenFactProv")) {
                txt_oc_nropedidman = (Textbox) parametros.get("txt_ocnropedkey");
                txt_providman = (Textbox) parametros.get("txt_provid");
                txt_provdesmanm = (Textbox) parametros.get("txt_provrazsoc");
                lst_comprasdet = (Listbox) parametros.get("lst_comprasdet");
                txt_conid = (Textbox) parametros.get("txt_conid");
                txt_condes = (Textbox) parametros.get("txt_condes");
                txt_glosa = (Textbox) parametros.get("txt_glosa");
                chk_efectivo = (Checkbox) parametros.get("chk_efectivo");
                //-----descuento geenral y financiero
                txt_dsctgral = (Doublebox) parametros.get("txt_dsctgral");
                txt_dsctfin = (Doublebox) parametros.get("txt_dsctfin");
                //----totales lista detalle
                txt_lvventaped = (Doublebox) parametros.get("txt_lvventaped");
                //txt_lbruto = (Doublebox) parametros.get("txt_lbruto");
                txt_lvventafac = (Doublebox) parametros.get("txt_lvventafac");
                txt_ligvped = (Doublebox) parametros.get("txt_ligvped");
                txt_ligvfac = (Doublebox) parametros.get("txt_ligvfac");
                txt_lpventaped = (Doublebox) parametros.get("txt_lpventaped");
                txt_lpventafac = (Doublebox) parametros.get("txt_lpventafac");
                //txt_lsubtotal = (Doublebox) parametros.get("txt_lsubtotal");
                //-----totales de la factura
                //lbl_tdsctos = (Label) parametros.get("lbl_tdsctos");
                lbl_valventa = (Label) parametros.get("lbl_valventa");
                lbl_valimp = (Label) parametros.get("lbl_valimp");
                lbl_valtotal = (Label) parametros.get("lbl_valtotal");
            }
        }
    }

    @Listen("onCreate=#tabbox_ordcompRec")
    public void llenaRegistros() throws SQLException {
        objDaoOrdComp = new DaoOrdCom();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        String prov_id = "";
        fechaActual = Utilitarios.hoyAsString();
        ListModelList<Moneda> objlstMonedas;
        objlstMonedas = new DaoMoneda().listaMonedas(1);
        cb_moneda.setModel(objlstMonedas);
        objDaoOrdComp = new DaoOrdCom();
        //actualizamos la situacion de todas las ordenes de compra
        objParamActualizacionOC = new ParametrosSalida();
        objParamActualizacionOC = objDaoOrdComp.actualizaSituacionOC();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | Se actualizo la situacion de las o/c | " + objParamActualizacionOC.getMsgValidacion());
        objlstOrdCompCab = objDaoOrdComp.listaOrdCompCabRec(emp_id, suc_id, "C", prov_id, "%%", "%%", "%%", fechaActual, fechaActual, "%%", "%%");
        lst_ordcompcab.setModel(objlstOrdCompCab);
        txt_provid.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10205000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado en Procesos Orden de Compra por Recibir con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado en Procesos Orden de Compra por Recibir con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            if (!parametros.isEmpty()) {
                tbbtn_btn_imprimir.setDisabled(true);
            } else {
                tbbtn_btn_imprimir.setDisabled(false);
            }
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a Imprimir Lista de Ordenes de Compra");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a Imprimir Lista de Ordenes de Compra");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
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
        String pro_key = txt_prodid.getValue().isEmpty() ? "%%" : txt_prodid.getValue();
        String lin = txt_linid.getValue().isEmpty() ? "%%" : txt_linid.getValue();
        String slin = txt_sublinid.getValue().isEmpty() ? "%%" : txt_sublinid.getValue();
        String orden = txt_filtro_orden.getValue().isEmpty() ? "%%" : txt_filtro_orden.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstOrdCompCab = new ListModelList();
            objlstOrdCompCab = objDaoOrdComp.listaOrdCompCabRec(emp_id, suc_id, "C", prov_id, pro_key, lin, slin, f_emisioni, f_emisionf, oc_sit, orden);
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

    @Listen("onSelect=#lst_ordcompcab")
    public void seleccionaRegistro() throws ParseException, SQLException {
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
        //llenarCamposTotales();
        limpiarCamposDetalle();
        lbl_total.setValue(objOrdCompCab.getOc_svtotal());
        //llamado desde el mantenimiento de Nota de cambio
        if (!parametros.isEmpty()) {
            if (parametros.get("validaBusqueda").equals("mantNotaES")) {
                //aqui determinamos lo que tiene que pasar a la nota de e/s
                //si ya tiene nota de entrada 001, entonces solo pasaria los restos.
                txt_oc_nropedidman.setValue(objOrdCompCab.getOc_nropedid());
                txt_providman.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_provid()), 8, "0"));
                txt_provdesmanm.setValue(objOrdCompCab.getOc_provrazsoc());
                txt_oc_glosaman.setValue(objOrdCompCab.getOc_condes());
                txt_listapreid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_lpcid()), 4, "0"));
                txt_listapredes.setValue(objOrdCompCab.getOc_lpcdes());
                cb_almori.setSelectedItem(Utilitarios.valorPorTexto(cb_almori, objOrdCompCab.getOc_almori()));
                cb_almdes.setSelectedItem(Utilitarios.valorPorTexto(cb_almdes, objOrdCompCab.getOc_almdes()));
                objlstNotaES = objDaoOrdComp.listaOrdCompDetNotaES(emp_id, suc_id, objOrdCompCab.getOc_nropedkey(), "C");
                objlstNotaESDet = new ListModelList<NotaESDet>();
                objlstNotaESDet = GeneraListaNotaES(objlstNotaES);
                lst_notes_det.setModel(objlstNotaESDet);
                Session sess = Sessions.getCurrent();
                sess.setAttribute("objlstNotaESDet", objlstNotaESDet);
                w_lov_manprocordcomp.detach();
            } else if (parametros.get("validaBusqueda").equals("GenFactProv")) {
                //preguntamos si es que la o/c ya cuenta con factura
                int existe = new DaoFacPro().existeFacturaProveedor(emp_id, suc_id, objOrdCompCab.getOc_nropedkey());
                if (existe > 0) {
                    Messagebox.show("La orden de compra ya tiene " + existe + " facturas creada . Desea continuar?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_oc_nropedidman.setValue(objOrdCompCab.getOc_nropedid());
                                        txt_providman.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_provid()), 8, "0"));
                                        txt_provdesmanm.setValue(objOrdCompCab.getOc_provrazsoc());
                                        txt_dsctgral.setValue(objOrdCompCab.getLp_descgen());
                                        txt_dsctfin.setValue(objOrdCompCab.getLp_descfinan());
                                        objlstGenFactProv = objDaoOrdComp.listaOrdCompDetFact(emp_id, suc_id, objOrdCompCab.getOc_nropedkey(), "C");
                                        txt_conid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_conid()), 3, "0"));
                                        txt_condes.setValue(objOrdCompCab.getOc_condes());
                                        ListaComplemCompraDetalle(txt_condes.getValue().equals("EFECTIVO"), chk_efectivo.isChecked());
                                        lst_comprasdet.setModel(objlstComprasDet);
                                        Session sess = Sessions.getCurrent();
                                        sess.setAttribute("objlstComprasDet", objlstComprasDet);
                                        txt_glosa.setDisabled(false);
                                        txt_glosa.setFocus(true);
                                        calculosTotales();
                                        w_lov_manprocordcomp.detach();
                                    } else {
                                        lst_ordcompcab.setSelectedIndex(-1);
                                    }
                                }
                            });
                } else {
                    txt_oc_nropedidman.setValue(objOrdCompCab.getOc_nropedid());
                    txt_providman.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_provid()), 8, "0"));
                    txt_provdesmanm.setValue(objOrdCompCab.getOc_provrazsoc());
                    txt_dsctgral.setValue(objOrdCompCab.getLp_descgen());
                    txt_dsctfin.setValue(objOrdCompCab.getLp_descfinan());
                    objlstGenFactProv = objDaoOrdComp.listaOrdCompDet(emp_id, suc_id, objOrdCompCab.getOc_nropedkey(), "C");
                    txt_conid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_conid()), 3, "0"));
                    txt_condes.setValue(objOrdCompCab.getOc_condes());
                    ListaInicialCompraDetalle(txt_condes.getValue().equals("EFECTIVO"), chk_efectivo.isChecked());
                    lst_comprasdet.setModel(objlstComprasDet);
                    Session sess = Sessions.getCurrent();
                    sess.setAttribute("objlstComprasDet", objlstComprasDet);
                    txt_glosa.setDisabled(false);
                    calculosTotales();
                    w_lov_manprocordcomp.detach();
                }
            }
            if (controlador.equals("ControllerGenFacPro")) {
                ControllerGenFacPro.bandera = false;
            }
            if (controlador.equals("ControllerNotaES")) {
                ControllerGenNotaES.bandera = false;
            }
        }
        calculosTotales();
    }

    @Listen("onSelect=#lst_ordcompdet")
    public void seleccionaRegistrosDetalle() {
        objOrdCompDet = (OrdCompDet) lst_ordcompdet.getSelectedItem().getValue();
        llenarCamposProducto();
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstOrdCompCab == null || objlstOrdCompCab.isEmpty()) {
            Messagebox.show("No hay orden de compra por recibir para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionOrdComRec.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_filtro_orden")
    public void onOKOrden() throws SQLException {
        cb_situacion.focus();
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
                objMapObjetos.put("controlador", "ControllerOrdCompRec");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_linid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_provid.setValue("");
                                    txt_provdes.setValue("");
                                    txt_prodid.setValue("");
                                    txt_proddes.setValue("");
                                    txt_sublinid.setValue("");
                                    txt_sublindes.setValue("");
                                    txt_linid.setValue("");
                                    txt_lindes.setValue("");
                                    txt_provid.focus();
                                }
                            }
                        });
            } else {
                String prov_id = txt_provid.getValue();
                txt_provid.setValue(Utilitarios.lpad(prov_id, 8, "0"));
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(txt_provid.getValue(), "1");
                if (objProveedor == null) {
                    s_mensaje = "El código de proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_provid.setValue("");
                                        txt_provdes.setValue("");
                                        txt_prodid.setValue("");
                                        txt_proddes.setValue("");
                                        txt_sublinid.setValue("");
                                        txt_sublindes.setValue("");
                                        txt_linid.setValue("");
                                        txt_lindes.setValue("");
                                        txt_provid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                    txt_prodid.setValue("");
                    txt_proddes.setValue("");
                    txt_sublinid.setValue("");
                    txt_sublindes.setValue("");
                    txt_linid.setValue("");
                    txt_lindes.setValue("");

                }
            }
        } else {
            txt_provdes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_prodid")
    public void lov_productos() {
        if (bandera == false) {
            bandera = true;
            if (txt_prodid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProcOrdCompRec";
                objMapObjetos.put("txt_proidman", txt_prodid);
                objMapObjetos.put("txt_prodesman", txt_proddes);
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_linid", txt_linid);
                objMapObjetos.put("txt_sublinid", txt_sublinid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompRec");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
                cb_situacion.select();
            }
        }
    }

    @Listen("onBlur=#txt_prodid")
    public void validaProducto() throws SQLException {
        if (!txt_prodid.getValue().isEmpty()) {
            if (!txt_prodid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_prodid.setValue("");
                                    txt_proddes.setValue("");
                                    txt_prodid.focus();
                                }
                            }
                        });
            } else {
                String producto = txt_prodid.getValue().isEmpty() ? "%%" : txt_prodid.getValue();
                String prov = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
                String linea = txt_linid.getValue().isEmpty() ? "%%" : txt_linid.getValue();
                String sublinea = txt_sublinid.getValue().isEmpty() ? "%%" : txt_sublinid.getValue();
                objProducto = new DaoProductos().buscarProdxTodo(producto, prov, linea, sublinea);
                if (objProducto == null) {
                    s_mensaje = "El código de producto no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_prodid.setValue("");
                                        txt_proddes.setValue("");
                                        txt_prodid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_prodid.setValue(objProducto.getPro_id());
                    txt_proddes.setValue(objProducto.getPro_des());
                }
            }
        } else {
            txt_proddes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_linid")
    public void lov_lineas() {
        if (bandera == false) {
            bandera = true;
            if (txt_linid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProcOrdCompRec";
                objMapObjetos.put("txt_prodid", txt_prodid);
                objMapObjetos.put("txt_linid", txt_linid);
                objMapObjetos.put("txt_lindes", txt_lindes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompRec");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovLineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_sublinid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_linid")
    public void validaLinea() throws SQLException {
        if (!txt_linid.getValue().isEmpty()) {
            if (!txt_linid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_sublinid.setValue("");
                                    txt_sublindes.setValue("");
                                    txt_linid.setValue("");
                                    txt_lindes.setValue("");
                                    txt_prodid.setValue("");
                                    txt_proddes.setValue("");
                                    txt_linid.focus();
                                }
                            }
                        });
            } else {
                Lineas objLinea;
                int lin_id = Integer.parseInt(txt_linid.getValue());
                objLinea = new DaoLineas().busquedaLineaxCodigo(lin_id);
                if (objLinea == null) {
                    s_mensaje = "El código de linea no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_sublinid.setValue("");
                                        txt_sublindes.setValue("");
                                        txt_linid.setValue("");
                                        txt_lindes.setValue("");
                                        txt_prodid.setValue("");
                                        txt_proddes.setValue("");
                                        txt_linid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la linea " + objLinea.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_linid.setValue(objLinea.getTab_subdir());
                    txt_lindes.setValue(objLinea.getTab_subdes());
                    txt_prodid.setValue("");
                    txt_proddes.setValue("");
                    txt_sublinid.setValue("");
                    txt_sublindes.setValue("");
                }
            }
        } else {
            txt_lindes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_sublinid")
    public void lov_sublineas() {
        if (bandera == false) {
            bandera = true;
            if (txt_sublinid.getValue().isEmpty() && !txt_linid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProcOrdCompRec";
                objMapObjetos.put("txt_linid", txt_linid);
                objMapObjetos.put("txt_sublinid", txt_sublinid);
                objMapObjetos.put("txt_sublindes", txt_sublindes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompRec");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSublineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_prodid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_sublinid")
    public void validaSubLinea() throws SQLException {
        if (!txt_sublinid.getValue().isEmpty()) {
            if (!txt_sublinid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_sublinid.setValue("");
                                    txt_sublindes.setValue("");
                                    txt_prodid.setValue("");
                                    txt_proddes.setValue("");
                                    txt_sublinid.focus();
                                }
                            }
                        });
            } else {
                String sublin_id = Utilitarios.lpad(txt_sublinid.getValue(), 6, "0");
                String lin_id = txt_sublinid.getValue().length() == 6 ? txt_sublinid.getValue().substring(0, 3) : "%%";
                SubLineas objSubLinea = new DaoSubLineas().buscarSublineaxCodigo(lin_id, sublin_id);
                if (objSubLinea == null) {
                    s_mensaje = "El código de sublinea no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_sublinid.setValue("");
                                        txt_sublindes.setValue("");
                                        txt_prodid.setValue("");
                                        txt_proddes.setValue("");
                                        txt_sublinid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la linea " + objSubLinea.getSlin_des() + " y ha encontrado 1 registro(s)");
                    txt_sublinid.setValue(objSubLinea.getSlin_cod());
                    txt_sublindes.setValue(objSubLinea.getSlin_des());
                    txt_prodid.setValue("");
                    txt_proddes.setValue("");
                }
            }
        } else {
            txt_sublindes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#cb_situacion")
    public void onBlurOKSituacion() {
        d_fecini.focus();
    }

    @Listen("onOK=#d_fecini")
    public void onBlurOKFechInicial() {
        d_fecfin.focus();
    }

    @Listen("onOK=#d_fecfin")
    public void onBlurOKFechFin() throws SQLException {
        busquedaRegistros();
    }

    @Listen("onClose=#w_lov_manprocordcomp")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerGenFactProv")) {
            ControllerGenFacPro.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
    }

    //Eventos Otros 
    public ListModelList<NotaESDet> GeneraListaNotaES(ListModelList<OrdCompDet> objlstNES) {
        //aqui solo devolveremos las cantidades restantes de la o/c
        objlstNotaESDet = new ListModelList<NotaESDet>();
        for (int i = 0; i < objlstNES.size(); i++) {
            NotaESDet objNotaESDet = new NotaESDet();
            objNotaESDet.setEmp_id(objUsuarioCredential.getCodemp());
            objNotaESDet.setSuc_id(objUsuarioCredential.getCodsuc());
            objNotaESDet.setPro_id(objlstNotaES.get(i).getPro_id());
            objNotaESDet.setPro_key(objlstNotaES.get(i).getPro_key());
            objNotaESDet.setPro_des(objlstNotaES.get(i).getPro_des());
            objNotaESDet.setPro_desdes(objlstNotaES.get(i).getPro_desdes());
            objNotaESDet.setNesdet_undpre(Integer.parseInt(objlstNotaES.get(i).getPro_presminven()));
            objNotaESDet.setNesdet_cant(objlstNotaES.get(i).getOcd_cantped());
            objNotaESDet.setNesdet_pimpto(objlstNotaES.get(i).getOcd_pimpto());
            objNotaESDet.setNesdet_vimpto(objlstNotaES.get(i).getOcd_vimpto());
            objNotaESDet.setNesdet_valina(objlstNotaES.get(i).getOcd_exonerado());
            objNotaESDet.setNesdet_valafe(objlstNotaES.get(i).getOcd_vafecto());
            objNotaESDet.setNesdet_pvta(objlstNotaES.get(i).getOcd_vtotal());
            double cantped = objlstNotaES.get(i).getOcd_cantped() / objNotaESDet.getNesdet_undpre();
            String proconv = objlstNotaES.get(i).getPro_conv();
            double unimas = Double.parseDouble(objlstNotaES.get(i).getPro_unimas());
            long gdet_cantconv = proconv.equals("*") ? (long) ((cantped) / unimas) : (long) (unimas * (cantped));
            objNotaESDet.setNesdet_cantconv(gdet_cantconv);
            objNotaESDet.setNesdet_proconv(objlstNotaES.get(i).getPro_conv());
            objNotaESDet.setNesdet_provid(objlstNotaES.get(i).getPro_provid());
            objNotaESDet.setNesdet_provrazsoc(objlstNotaES.get(i).getPro_provrazsoc());
            objNotaESDet.setNesdet_usuadd(objUsuarioCredential.getCuenta());
            objNotaESDet.setNesdet_usumod(objUsuarioCredential.getCuenta());
            objNotaESDet.setNesdet_tipmov("E");
            objNotaESDet.setNesdet_ubides(objlstNotaES.get(i).getPro_ubi());
            //objNotaESDet.setNesdet_ubiori(objlstNotaES.get(i).getPro_ubi());
            objNotaESDet.setNesdet_ubiori(null);
            objlstNotaESDet.add(objNotaESDet);
        }
        return objlstNotaESDet;
    }

    public void llenarCamposCabecera() throws ParseException {
        txt_oc_nropedid.setValue(objOrdCompCab.getOc_nropedid());
        txt_oc_provid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_provid()), 8, "0"));
        txt_oc_provrazsoc.setValue(objOrdCompCab.getOc_provrazsoc());
        d_oc_fecemi.setValue(objOrdCompCab.getOc_fecemi());
        d_oc_fecrec.setValue(objOrdCompCab.getOc_fecrec());
        d_oc_feccad.setValue(objOrdCompCab.getOc_feccad());
        txt_oc_conid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_conid()), 3, "0"));
        txt_oc_condes.setValue(objOrdCompCab.getOc_condes());
        txt_oc_lpcid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_lpcid()), 3, "0"));
        txt_oc_lpcdes.setValue(objOrdCompCab.getOc_lpcdes());
        txt_oc_mondes.setValue(objOrdCompCab.getOc_mondes());
        db_oc_tcambio.setValue(objOrdCompCab.getOc_tcambio());
        txt_oc_glosa.setValue(objOrdCompCab.getOc_glosa());
        txt_oc_almoriid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_almori()), 4, "0"));
        txt_oc_almorides.setValue(objOrdCompCab.getOc_almorides());
        txt_oc_almdesid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_almdes()), 4, "0"));
        txt_oc_almdesdes.setValue(objOrdCompCab.getOc_almdesdes());
    }

    public void llenarCamposProducto() {
        String s_key = String.valueOf(objOrdCompDet.getOcd_item());
        txt_ocd_comdet_key.setValue(Integer.parseInt(s_key));// ITEM
        txt_ocd_prodid.setValue(objOrdCompDet.getPro_id());//COD Productos
        txt_ocd_proddes.setValue(objOrdCompDet.getPro_des());//Descripcion Producto
        txt_ocd_pesounit.setValue(objOrdCompDet.getOcd_peso() * Integer.parseInt(objOrdCompDet.getPro_presminven()));//Peso U.Inicial
        //txt_ocd_pesounit.setValue(objOrdCompDet.getOcd_peso());//Peso U.
        txt_ocd_peso_und.setValue(objOrdCompDet.getPro_unipeso());//Peso U.Inicial
        txt_voluunit.setValue(objOrdCompDet.getPro_svol() * Integer.parseInt(objOrdCompDet.getPro_presminven()));//VOL U.
        txt_vol_und.setValue(objOrdCompDet.getPro_sunivol());//VOL INICIAL
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
            Messagebox.show("Error de Formato en las Fechas: " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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

    public void calculosTotales() {
        if (!parametros.isEmpty()) {
            if (parametros.get("validaBusqueda").equals("GenFactProv")) {
                double data[];
                data = calculos();
                txt_lvventaped.setValue(data[0]);
                txt_lvventafac.setValue(data[1]);
                txt_ligvped.setValue(data[2]);
                txt_ligvfac.setValue(data[3]);
                txt_lpventaped.setValue(data[4]);
                txt_lpventafac.setValue(data[5]);
                //txt_ligv.setValue(data[6]);
                //txt_lsubtotal.setValue(data[7]);
                //lbl dsctos
                //lbl_tdsctos.setValue(df2.format(new Double(0.00)));
                lbl_valventa.setValue(df2.format(txt_lvventafac.getValue()));
                lbl_valimp.setValue(df2.format(txt_ligvfac.getValue()));
                lbl_valtotal.setValue(df2.format(txt_lvventafac.getValue() + txt_ligvfac.getValue()));
                //lbl_valventa.setValue(df2.format(data[5]));
                //lbl_valimp.setValue(df2.format(data[6]));
                //lbl_vtotal.setValue(df2.format(data[7]));
            }
        } else {
            double data[] = calculos();
            txt_tafecto.setValue(data[0]);
            txt_texonerado.setValue(data[1]);
            txt_tpdes.setValue(data[2]);
            txt_tvaldes.setValue(data[3]);
            txt_timp.setValue(data[4]);
            txt_tpreven.setValue(data[5]);
            txt_tpeso.setValue(data[6]);
            txt_totvolumen.setValue(data[7]);
            //lbl_total.setValue(objOrdCompCab.getOc_svtotal());
        }
    }

    public void ListaComplemCompraDetalle(boolean ind1, boolean ind2) throws SQLException {
        objlstComprasDet = new ListModelList<ComprasDet>();
        for (int i = 0; i < objlstGenFactProv.size(); i++) {
            ComprasDet objComprasDet = new ComprasDet();
            objComprasDet.setEmp_id(objlstGenFactProv.get(i).getEmp_id());
            objComprasDet.setSuc_id(objlstGenFactProv.get(i).getSuc_id());
            objComprasDet.setTcd_codori(objlstGenFactProv.get(i).getPro_codori());
            objComprasDet.setPro_key(objlstGenFactProv.get(i).getPro_key());
            objComprasDet.setPro_id(objlstGenFactProv.get(i).getPro_id());
            objComprasDet.setPro_desdes(objlstGenFactProv.get(i).getPro_desdes());
            objComprasDet.setPro_des(objlstGenFactProv.get(i).getPro_des());

            objComprasDet.setTcd_precioped(objlstGenFactProv.get(i).getOcd_precio());
            objComprasDet.setTcd_preciofac(ind1 == true || ind2 == true ? objlstGenFactProv.get(i).getOcd_precio() : (objlstGenFactProv.get(i).getOcd_precio() * 100) / (100 - txt_dsctfin.getValue()));

            objComprasDet.setTcd_cantped(objlstGenFactProv.get(i).getOcd_cantped());
            objComprasDet.setTcd_cantfac(objlstGenFactProv.get(i).getCant_fac());

            objComprasDet.setTcd_vventaped(objComprasDet.getTcd_cantped() * objComprasDet.getTcd_precioped());
            objComprasDet.setTcd_vventafac(objComprasDet.getTcd_cantfac() * objComprasDet.getTcd_preciofac());

            objComprasDet.setTcd_igvped((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? 0 : objComprasDet.getTcd_vventaped() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)));
            objComprasDet.setTcd_igvfac((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? 0 : objComprasDet.getTcd_vventafac() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)));

            objComprasDet.setTcd_prevenped(objComprasDet.getTcd_vventaped() + objComprasDet.getTcd_igvped());
            objComprasDet.setTcd_prevenfac(objComprasDet.getTcd_vventafac() + objComprasDet.getTcd_igvfac());

            //objComprasDet.setTcd_impnet(objlstGenFactProv.get(i).getP_vta());
            //objComprasDet.setTcd_impnetoc(objlstGenFactProv.get(i).getPv_oc());
            //objComprasDet.setTcd_imptot(objlstGenFactProv.get(i).getOcd_vafecto());
            //parte totales
            //objComprasDet.setTcd_imptot(objlstGenFactProv.get(i).getImp_neto());
            //objComprasDet.setTcd_vventaped(objlstGenFactProv.get(i).getVal_vta());
            //objComprasDet.setTcd_vventa(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() : objlstGenFactProv.get(i).getOcd_vafecto());
            //objComprasDet.setTcd_igv((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? 0 : objlstGenFactProv.get(i).getOcd_vafecto() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)));
            //objComprasDet.setTcd_igvped(objlstGenFactProv.get(i).getIgv());
            //objComprasDet.setTcd_subtotalvta(objlstGenFactProv.get(i).getSubtotal());
            //objComprasDet.setTcd_prevenped(objlstGenFactProv.get(i).getSubtotal());
            objlstComprasDet.add(objComprasDet);
        }
    }

    public void ListaInicialCompraDetalle(boolean ind1, boolean ind2) throws SQLException {
        objlstComprasDet = new ListModelList<ComprasDet>();
        for (int i = 0; i < objlstGenFactProv.size(); i++) {
            ComprasDet objComprasDet = new ComprasDet();
            objComprasDet.setEmp_id(objlstGenFactProv.get(i).getEmp_id());
            objComprasDet.setSuc_id(objlstGenFactProv.get(i).getSuc_id());
            objComprasDet.setTcd_codori(objlstGenFactProv.get(i).getPro_codori());
            objComprasDet.setPro_key(objlstGenFactProv.get(i).getPro_key());
            objComprasDet.setPro_id(objlstGenFactProv.get(i).getPro_id());
            objComprasDet.setPro_desdes(objlstGenFactProv.get(i).getPro_desdes());
            objComprasDet.setPro_des(objlstGenFactProv.get(i).getPro_des());

            objComprasDet.setTcd_precioped(objlstGenFactProv.get(i).getOcd_precio());
            objComprasDet.setTcd_preciofac(ind1 == true || ind2 == true ? objlstGenFactProv.get(i).getOcd_precio() : (objlstGenFactProv.get(i).getOcd_precio() * 100) / (100 - txt_dsctfin.getValue()));
            //objComprasDet.setTcd_preciofac(ind == true ? objlstGenFactProv.get(i).getOcd_precio() : objlstGenFactProv.get(i).getOcd_precio() * (1 + (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)) * (1 + (txt_dsctfin.getValue() / 100)));

            objComprasDet.setTcd_cantped(objlstGenFactProv.get(i).getOcd_cantped());
            objComprasDet.setTcd_cantfac(objlstGenFactProv.get(i).getOcd_cantped());
            //objComprasDet.setTcd_impnet(objlstGenFactProv.get(i).getOcd_vtotal());
            //objComprasDet.setTcd_impnetoc(objlstGenFactProv.get(i).getOcd_vtotal());
            //objComprasDet.setTcd_imptot(objlstGenFactProv.get(i).getOcd_vafecto());

            //parte totales
            //objComprasDet.setTcd_imptot(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() : objlstGenFactProv.get(i).getOcd_vafecto());
            objComprasDet.setTcd_vventaped(objComprasDet.getTcd_cantped() * objComprasDet.getTcd_precioped());
            objComprasDet.setTcd_vventafac(objComprasDet.getTcd_cantfac() * objComprasDet.getTcd_preciofac());
            //objComprasDet.setTcd_vventafac(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() * objComprasDet.getTcd_preciofac() : objlstGenFactProv.get(i).getOcd_cantped() * objComprasDet.getTcd_preciofac());
            //objComprasDet.setTcd_vventa(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() : objlstGenFactProv.get(i).getOcd_vafecto());
            objComprasDet.setTcd_igvped((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? 0 : objComprasDet.getTcd_vventaped() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)));
            objComprasDet.setTcd_igvfac((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? 0 : objComprasDet.getTcd_vventafac() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)));
            //objComprasDet.setTcd_subtotalvta((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() : (objlstGenFactProv.get(i).getOcd_vafecto() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100))) + objlstGenFactProv.get(i).getOcd_vafecto());
            //objComprasDet.setTcd_prevenped((objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() : (objlstGenFactProv.get(i).getOcd_vafecto() * (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100))) + objlstGenFactProv.get(i).getOcd_vafecto());
            //objComprasDet.setTcd_prevenfac(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() * objlstGenFactProv.get(i).getOcd_cantped() * objComprasDet.getTcd_preciofac() / (1 + (objDaoImpuesto.obtieneValorImpuesto(objlstGenFactProv.get(i).getPro_id()) / 100)) : objComprasDet.getTcd_preciofac() * objlstGenFactProv.get(i).getOcd_cantped());
            objComprasDet.setTcd_prevenped(objComprasDet.getTcd_vventaped() + objComprasDet.getTcd_igvped());
            objComprasDet.setTcd_prevenfac(objComprasDet.getTcd_vventafac() + objComprasDet.getTcd_igvfac());
            //objComprasDet.setTcd_vventafac(objlstGenFactProv.get(i).getOcd_vafecto() == 0 ? objlstGenFactProv.get(i).getOcd_exonerado() * objComprasDet.getTcd_preciofac() : objlstGenFactProv.get(i).getOcd_cantped() * objComprasDet.getTcd_preciofac());

            objlstComprasDet.add(objComprasDet);
        }
    }

    public double[] calculos() {
        if (!parametros.isEmpty()) {
            double dataprov[] = new double[6];
            if (parametros.get("validaBusqueda").equals("GenFactProv")) {
                for (int i = 0; i < objlstComprasDet.getSize(); i++) {
                    dataprov[0] = dataprov[0] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_vventaped();//valor venta de la o/c
                    dataprov[1] = dataprov[1] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_vventafac();//valor venta total factura
                    dataprov[2] = dataprov[2] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_igvped();//impuesto total o/c
                    dataprov[3] = dataprov[3] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_igvfac();//impuesto total factura
                    dataprov[4] = dataprov[4] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_prevenped();//precio venta total o/c
                    dataprov[5] = dataprov[5] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_prevenfac();//precio venta total factura
                    //dataprov[6] = dataprov[6] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_igvped();
                    //dataprov[7] = dataprov[7] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_subtotalvta();
                }
            }
            return dataprov;
        } else {
            double data[] = new double[8];
            for (int i = 0; i < objlstOrdCompDet.getSize(); i++) {
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
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }
}
