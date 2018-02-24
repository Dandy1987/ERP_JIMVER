package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoImpuesto;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.cxc.mantenimiento.DaoTipoCambio;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.ComprasCab;
import org.me.gj.model.logistica.procesos.ComprasDet;
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
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerGenFacPro extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Window w_genfacturap;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoc, tbbtn_btn_guardarc, tbbtn_btn_editarc, tbbtn_btn_eliminarc, tbbtn_btn_deshacerc;
    @Wire
    Textbox txt_serie, txt_nrodoc, txt_ocnropedkey, txt_provid, txt_provrazsoc, txt_glosa, txt_prodid, txt_proddes, txt_conid, txt_condes, txt_tcid,
            txt_providcab, txt_provdescab, txt_filtro_fac,
            txt_usuadd, txt_usumod;
    @Wire
    Doublebox txt_tcamb, txt_impb, txt_pgral, txt_pitem, txt_ttotal, txt_pdescxart, txt_vdescxart, txt_dsctgral, txt_dsctfin,
            txt_cantfac, txt_cantped, txt_vventa, txt_ocvtotal, txt_preuni,
            txt_preunifac, txt_totalped, txt_totalfac,
            //txt_lpvta, txt_ldsctogral, txt_ldsctoitem, txt_lneto, txt_lvventa, txt_ligv, txt_lsubtotal,//txt_lpvtaoc,
            txt_lvventaped, txt_lvventafac, txt_ligvped, txt_ligvfac, txt_lpventaped, txt_lpventafac;
    @Wire
    Button btn_ocnropedkey, tbbtn_btn_buscar;
    @Wire
    Datebox d_fecha, d_fecemi, d_fecven, d_fecadd, d_fecmod, d_fecemiini, d_fecemifin;
    @Wire
    Combobox cb_periodo, cb_moneda, cb_tipdoc;
    @Wire
    Listbox lst_comprascab, lst_comprasdet;
    @Wire
    Longbox txt_tckey;
    @Wire
    Checkbox chk_dsctitem, chk_estado, chk_efectivo; //chk_descuentos, chk_dsctgral, chk_dsctcom,
    @Wire
    Label lbl_tdsctos, lbl_valventa, lbl_valimp, lbl_valtotal, lbl_codori;
    //Instancias de Objetos
    ListModelList<ManPer> objListModelListManPer = new ListModelList<ManPer>();
    ListModelList<Moneda> objlstMonedas = new ListModelList<Moneda>();
    ListModelList<TipDoc> objlstTipDoc = new ListModelList<TipDoc>();
    ListModelList<OrdCompDet> objlstOrdCompDet = new ListModelList<OrdCompDet>();
    ListModelList<ComprasDet> objlstComprasDet = new ListModelList<ComprasDet>();
    ListModelList<ComprasDet> objlstEliminacion = new ListModelList<ComprasDet>();
    ListModelList<ComprasCab> objlstComprasCab = new ListModelList<ComprasCab>();
    DaoManPer objDaoManPer = new DaoManPer();
    DaoMoneda objDaoMonedas = new DaoMoneda();
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoFacPro objDaoGenFactProv = new DaoFacPro();
    DaoImpuesto objDaoImpuesto;
    DaoOrdCom objDaoOrdComp = new DaoOrdCom();
    OrdCompDet objOrdCompDet = new OrdCompDet();
    OrdCompDet objComprasOrdDet = new OrdCompDet();
    ComprasCab objComprasCab = new ComprasCab();
    ComprasDet objComprasDet = new ComprasDet();
    ManPer objManPer = new ManPer();
    Moneda objMonedas = new Moneda();
    TipDoc objTipDoc = new TipDoc();
    Accesos objAccesos = new Accesos();
    Precio objPrecioCompra;
    //Variables publicas
    String campo = "";
    String foco = "";
    String s_estado = "Q";
    String s_estadoDetalle = "Q";
    String s_mensaje = "";
    String s_facprov = "";
    String dscto = "NO";
    String modoEjecucion;
    int i_selCab, i_selDet;
    int valor;
    int monid;
    long nrooc;
    public static boolean bandera = false;
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    DecimalFormat df4 = new DecimalFormat("###,##0.0000", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenFacPro.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_genfacturap")
    public void llenaRegistros() throws SQLException {
        objDaoImpuesto = new DaoImpuesto();
        objlstComprasCab = objDaoGenFactProv.listaFacturaProveedorCab();
        lst_comprascab.setModel(objlstComprasCab);
        txt_filtro_fac.focus();
        LlenaDataCabecera();
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10207000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a la Generación Factura de Proveedor con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a la Generación Factura de Proveedor con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Factura de Proveedor");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Factura de Proveedor");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Factura de Proveedor");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Factura de Proveedor");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Factura de Proveedor");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Factura de Proveedor");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de una Factura de Proveedor");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de una Factura de Proveedor");
        }
    }
    
    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String resultado;
        Date fecha_emisioni = d_fecemiini.getValue();
        Date fecha_emisionf = d_fecemifin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "";
        } else {
            f_emisioni = sdf.format(d_fecemiini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_fecemifin.getValue());
        }
        String s_proveedor = txt_providcab.getValue();
        String s_factura = txt_filtro_fac.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (resultado.equals("F1<") || resultado.equals("=") || resultado.equals("OK")) {
            objlstComprasCab = new ListModelList<ComprasCab>();
            objlstComprasCab = objDaoGenFactProv.BusquedaFacturaProveedor(f_emisioni, f_emisionf, s_proveedor, s_factura);
            if (objlstComprasCab.getSize() > 0) {
                lst_comprascab.setModel(objlstComprasCab);
            } else {
                objlstComprasCab = null;
                lst_comprascab.setModel(objlstComprasCab);
                Messagebox.show("No existe facturas de proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        limpiaAuditoria();
        objComprasDet = null;
        lst_comprasdet.setModel(objlstOrdCompDet);
    }
    
    @Listen("onSelect=#lst_comprascab")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCampos();
        limpiarCamposDetalle();
        objComprasCab = (ComprasCab) lst_comprascab.getSelectedItem().getValue();
        if (objComprasCab == null) {
            objComprasCab = objlstComprasCab.get(i_selCab + 1);
        }
        i_selCab = lst_comprascab.getSelectedIndex();
        //cargamos las listas con los datos de detalle
        objlstComprasDet = objDaoGenFactProv.listaFacturaProveedorDet(objComprasCab.getTc_key());
        lst_comprasdet.setModel(objlstComprasDet);
        habilitaBotonesDetalle(true, true);
        //cargamos los campos
        llenarCampos(objComprasCab);
        llenarTCamposDetalle();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objComprasCab.getTc_id());
    }
    
    @Listen("onSelect=#lst_comprasdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        limpiarCamposDetalle();
        objComprasDet = (ComprasDet) lst_comprasdet.getSelectedItem().getValue();
        if (objComprasDet == null) {
            objComprasDet = objlstComprasDet.get(i_selDet + 1);
        }
        i_selDet = lst_comprasdet.getSelectedIndex();
        String codProd = objComprasDet.getPro_id();
        String fac_cab;
        if ("M".equals(s_estado)) {
            fac_cab = objComprasCab.getTc_id();
            objComprasOrdDet = objDaoOrdComp.OrdCompDetFacxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Long.parseLong(txt_ocnropedkey.getValue()), "C", codProd, s_estado, fac_cab);
            llenarCamposDetalle(objComprasDet);
        } else {
            fac_cab = "";
            objComprasOrdDet = objDaoOrdComp.OrdCompDetFacxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Long.parseLong(txt_ocnropedkey.getValue()), "C", codProd, s_estado, fac_cab);
            llenarCamposDetalle(objComprasDet);
            //LlenarCamposDetalle(objComprasOrdDet);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con descripcion " + objComprasDet.getPro_des());
    }
    
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        limpiarCampos();
        LlenaDataCabecera();
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitaBotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        //limpiamos la lista
        objlstComprasDet = null;
        objlstComprasDet = new ListModelList();
        lst_comprasdet.setModel(objlstComprasDet);
        s_estado = "N";
        //fecha de emision
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_fecven.setValue(Utilitarios.hoyAsFecha());
        d_fecemi.focus();
        d_fecemi.select();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }
    
    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_serie.setConstraint("");
                            txt_nrodoc.setConstraint("");
                            limpiarCampos();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaBotonesDetalle(false, true);
                            s_estado = "Q";
                            s_estadoDetalle = "Q";
                            limpiarCamposDetalle();
                            habilitaCamposDetalle(true);
                            lst_comprascab.setSelectedIndex(-1);
                            //limpiamos la lista
                            objlstComprasDet = null;
                            lst_comprasdet.setModel(objlstComprasDet);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }
    
    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_comprascab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una factura", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if ("ANULADO".equals(objComprasCab.getTc_estado())) {
                Messagebox.show("Esta factura ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "M";
                habilitaBotones(true, false);
                habilitaBotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                d_fecemi.setDisabled(false);
                d_fecven.setDisabled(false);
                cb_tipdoc.setDisabled(false);
                txt_nrodoc.setDisabled(false);
                txt_serie.setDisabled(false);
                txt_glosa.setDisabled(false);
                chk_efectivo.setDisabled(false);
                //chk_descuentos.setDisabled(false);
                //chk_descuentos.setChecked(!(objComprasCab.getTc_dscgral() != 0 || objComprasCab.getTc_dsccom() != 0));
                //chk_dsctgral.setDisabled(!chk_descuentos.isChecked());
                //chk_dsctcom.setDisabled(!chk_descuentos.isChecked());
                txt_dsctgral.setDisabled(!(objComprasCab.getTc_dscgral() != 0));
                txt_dsctfin.setDisabled(!(objComprasCab.getTc_dscfin() != 0));
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<ComprasDet>();
                lst_comprasdet.setSelectedIndex(-1);
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }
    
    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Fecha Emision")) {
                            d_fecemi.focus();
                        } else if (campo.equals("Fecha Vencimiento")) {
                            d_fecven.focus();
                        } else if (campo.equals("Moneda")) {
                            cb_moneda.focus();
                        } else if (campo.equals("Tipo de Documento")) {
                            cb_tipdoc.focus();
                        } else if (campo.equals("Serie")) {
                            txt_serie.focus();
                        } else if (campo.equals("Documento")) {
                            txt_nrodoc.focus();
                        } else if (campo.equals("Orden de Compra")) {
                            txt_ocnropedkey.focus();
                        } else if (campo.equals("Condicion de Pago")) {
                            txt_conid.focus();
                        } else if (campo.equals("Proveedor")) {
                            txt_provid.focus();
                        }
                    }
                }
            });
        } else if (lst_comprasdet.getItemCount() <= 0) {
            Messagebox.show("Por favor ingresar productos en la factura", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_valida = verificarFechas();
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida);
            } else {
                String fecemi = sdf.format(d_fecemi.getValue());
                if (new DaoCierreDia().ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                    Messagebox.show("El día se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if ("NUEVOS SOLES".equals(cb_moneda.getValue())) {
                        monid = 1;
                    } else {
                        monid = cb_moneda.getSelectedItem().getValue();
                    }
                    if (new DaoTipoCambio().obtieneTipoCambioComercial(fecemi, monid) < 1) {
                        Messagebox.show("No existe tipo de cambio para la fecha " + fecemi, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        txt_tcamb.setValue(0.00);
                    } else {
                        txt_tcamb.setValue(new DaoTipoCambio().obtieneTipoCambioComercial(fecemi, monid));
                        if (!s_estadoDetalle.equals("E")) {
                            s_mensaje = "Está seguro que desea guardar los cambios?";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParametrosCabecera;
                                                objComprasCab = generaRegistro();
                                                if (s_estado.equals("N")) {
                                                    objParametrosCabecera = objDaoGenFactProv.insertarCabecera(objComprasCab);
                                                } else {
                                                    objParametrosCabecera = objDaoGenFactProv.modificarCabecera(objComprasCab);
                                                }
                                                if (objParametrosCabecera.getFlagIndicador() == 0) {
                                                    ParametrosSalida objParamDetalle = new ParametrosSalida();
                                                    boolean verificarDetalle = true;
                                                    int i = 0;
                                                    if (s_estado.equals("N")) {
                                                        if (objlstComprasDet == null) {
                                                            Session sess = Sessions.getCurrent();
                                                            objlstComprasDet = (ListModelList) sess.getAttribute("objlstComprasDet");
                                                            while (i < objlstComprasDet.getSize() && verificarDetalle) {
                                                                long tc_key = Long.parseLong(objParametrosCabecera.getCodigoRegistro());
                                                                
                                                                objlstComprasDet.get(i).setTc_key(tc_key);
                                                                objlstComprasDet.get(i).setTcd_usuadd(objUsuCredential.getCuenta());
                                                                objParamDetalle = objDaoGenFactProv.insertarDetalle(objlstComprasDet.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrió un error al crear el producto " + objlstComprasDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        } else if (objlstComprasDet != null) {
                                                            while (i < objlstComprasDet.getSize() && verificarDetalle) {
                                                                long tc_key = Long.parseLong(objParametrosCabecera.getCodigoRegistro());
                                                                objlstComprasDet.get(i).setTc_key(tc_key);
                                                                objlstComprasDet.get(i).setTcd_usuadd(objUsuCredential.getCuenta());
                                                                objParamDetalle = objDaoGenFactProv.insertarDetalle(objlstComprasDet.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrió un error al crear el producto " + objlstComprasDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                    } else {
                                                        //OPERACION DE ELINACION DE PRODUCTOS DE LA LISTA FACTURA PROVEEDOR
                                                        if (!objlstEliminacion.isEmpty()) {
                                                            while (i < objlstEliminacion.getSize() && verificarDetalle) {
                                                                objlstComprasDet.get(i).setTcd_usumod(objUsuCredential.getCuenta());
                                                                objParamDetalle = objDaoGenFactProv.eliminarDetalle(objlstEliminacion.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrió un error al eliminar un producto con el Item" + objlstEliminacion.get(i).getTcd_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                        //OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                                        if (objlstComprasDet != null) {
                                                            i = 0;
                                                            verificarDetalle = true;
                                                            while (i < objlstComprasDet.getSize() && verificarDetalle) {
                                                                String indicador = objlstComprasDet.get(i).getInd_accion();
                                                                if (indicador.equals("N") || indicador.equals("NM")) {
                                                                    objlstComprasDet.get(i).setTcd_usuadd(objUsuCredential.getCuenta());
                                                                    objParamDetalle = objDaoGenFactProv.insertarDetalle(objlstComprasDet.get(i));
                                                                } else if (indicador.equals("M")) {
                                                                    objlstComprasDet.get(i).setTcd_usumod(objUsuCredential.getCuenta());
                                                                    objParamDetalle = objDaoGenFactProv.modificarDetalle(objlstComprasDet.get(i));
                                                                }
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrió un error con el producto " + objlstComprasDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                    }
                                                    txt_serie.setConstraint("");
                                                    txt_nrodoc.setConstraint("");
                                                    // Messagebox.show(s_mensaje);
                                                    tab_lista.setSelected(true);
                                                    tab_lista.setDisabled(false);
                                                    tab_mantenimiento.setSelected(false);
                                                    //validacion de guardar/nuevo
                                                    VerificarTransacciones();
                                                    tbbtn_btn_guardar.setDisabled(true);
                                                    tbbtn_btn_deshacer.setDisabled(true);
                                                    //
                                                    limpiarCampos();
                                                    limpiarCamposDetalle();
                                                    habilitaCampos(true);
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    /**/
                                                    limpiarCamposDetalle();
                                                    habilitaCamposDsctos(true);
                                                    /**/
                                                    habilitaBotonesDetalle(true, true);
                                                    lst_comprascab.setSelectedIndex(-1);
                                                    //limpiamos las listas
                                                    objlstComprasCab = new ListModelList<ComprasCab>();
                                                    objlstEliminacion = new ListModelList<ComprasDet>();
                                                    objlstComprasDet = new ListModelList<ComprasDet>();
                                                    objlstComprasCab.clear();
                                                    objlstOrdCompDet.clear();
                                                    objlstEliminacion.clear();
                                                    /*
                                                     lst_comprascab.setModel(objlstComprasCab);
                                                     lst_comprasdet.setModel(objlstComprasDet);*/
                                                    //cargamos la lista de facturas proveedor
                                                    objlstComprasCab = objDaoGenFactProv.listaFacturaProveedorCab();
                                                    lst_comprascab.setModel(objlstComprasCab);
                                                    lst_comprascab.setSelectedIndex(-1);
                                                    s_estadoDetalle = "Q";
                                                    s_estado = "Q";
                                                } else {
                                                    Messagebox.show(objParametrosCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Messagebox.show("El producto se encuentra en edición", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    }
                }
            }
        }
    }
    
    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_comprascab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una factura", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar esta factura de proveedor?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objComprasCab.setTc_usumod(objUsuCredential.getCuenta());
                                objParamSalida = objDaoGenFactProv.eliminarCabecera(objComprasCab);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstComprasCab.clear();
                                    busquedaRegistros();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }
    
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstComprasCab == null || objlstComprasCab.isEmpty()) {
            Messagebox.show("No hay factura de proveedor para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_comprascab.getSelectedIndex() >= 0) {
                objComprasCab = (ComprasCab) lst_comprascab.getSelectedItem().getValue();
                if (objComprasCab == null) {
                    objComprasCab = objlstComprasCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
                objMapObjetos.put("codigofac", objComprasCab.getTc_id());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenFacPro.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una factura de proveedor para imprimir","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
            }
        }
    }
    
    @Listen("onClick=#tbbtn_btn_nuevoc")
    public void botonNuevoDetalle() throws SQLException {
        if (!verificar().isEmpty()) {
            Messagebox.show("Completar datos de la factura de proveedor","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
        } else if (objlstComprasDet.getSize() <= 0) {
            Messagebox.show("Seleccione una orden de compra","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
        } else {
            Utilitarios.deshabilitarLista(true, lst_comprasdet);
            limpiarCamposDetalle();
            habilitaCamposDetalle(true);
            habilitaBotonesDetalle(true, false);
            txt_prodid.setDisabled(false);
            //txt_preuni.setDisabled(false);
            txt_cantfac.setDisabled(false);
            txt_pdescxart.setValue(0);
            txt_vdescxart.setValue(0);
            s_estadoDetalle = "N";
            /*if (chk_dsctitem.isChecked()) {
             txt_pdescxart.setDisabled(false);
             } else {
             txt_pdescxart.setDisabled(true);
             }*/
            txt_prodid.focus();
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro Detalle");
    }
    
    @Listen("onClick=#tbbtn_btn_editarc")
    public void botonEditarDetalle() throws SQLException {
        if (lst_comprasdet.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Utilitarios.deshabilitarLista(true, lst_comprasdet);
            habilitaBotonesDetalle(true, false);
            habilitaCamposDetalle(false);
            habilitaCampos(true);
            /*if (chk_dsctitem.isChecked()) {
             habilitaCamposDetalle(false);
             s_estadoDetalle = "E";
             } else {
             habilitaCamposDetalle(false);
             txt_prodid.setDisabled(true);
             txt_pdescxart.setDisabled(true);
             //txt_impnet.setDisabled(true);
             s_estadoDetalle = "E";
             }*/
            txt_cantfac.focus();
        }
    }
    
    @Listen("onClick=#tbbtn_btn_eliminarc")
    public void botonEliminarDetalle() throws SQLException {
        /*if (objlstComprasDet.isEmpty()) {
         Messagebox.show("No existen Elementos en la Factura Proveedor a Eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
         } else*/ if (lst_comprasdet.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            Messagebox.show("Está seguro que desea eliminar el producto seleccionado?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objComprasDet = (ComprasDet) lst_comprasdet.getSelectedItem().getValue();
                                int posicion = lst_comprasdet.getSelectedIndex();
                                if (s_estado.equals("M") && (!objComprasDet.getInd_accion().equals("N") || !objComprasDet.getInd_accion().equals("NM"))) {
                                    objComprasDet.setInd_accion("E");
                                    long tcd_item = objComprasDet.getTcd_item();
                                    long tcd_key = txt_tckey.getValue();
                                    String tcd_usumod = objUsuCredential.getCuenta();
                                    objlstEliminacion.add(new ComprasDet(tcd_key, objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), tcd_item, tcd_usumod));
                                }
                                objlstComprasDet = (ListModelList) lst_comprasdet.getModel();
                                objlstComprasDet.remove(posicion);
                                limpiarCamposDetalle();
                                llenarTCamposDetalle();
                            }
                        }
                    });
        }
    }
    
    @Listen("onClick=#tbbtn_btn_deshacerc")
    public void botonDeshacerDetalle() throws SQLException {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalle();
                            habilitaBotonesDetalle(false, true);
                            habilitaCampos(false);
                            habilitaCamposDetalle(true);
                            Utilitarios.deshabilitarLista(false, lst_comprasdet);
                            tbbtn_btn_guardarc.setDisabled(true);
                            tbbtn_btn_deshacerc.setDisabled(true);
                            s_estadoDetalle = "Q";
                            lst_comprasdet.setSelectedIndex(-1);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }
    
    @Listen("onClick=#tbbtn_btn_guardarc")
    public void botonGuardarDetalle() throws SQLException {
        //VALIDA SI ES QUE LA LISTA DEL DETALLE DE FACTURA PROVEEDOR ESTA LLENA
        if (objlstComprasDet == null) {
            objlstComprasDet = (ListModelList) lst_comprasdet.getModel();
        }
        String validaProducto = verificarDetalle();
        if (!validaProducto.isEmpty()) {
            Messagebox.show("Por favor verificar los datos en el campo " + validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
                    if (foco.equals("Codigo")) {
                        txt_prodid.focus();
                    } else if (foco.equals("Porcentaje")) {
                        txt_pdescxart.focus();
                    } else if (foco.equals("Cantidad")) {
                        txt_cantfac.focus();
                    }
                }
            });
        } else {
            String pro_id = txt_prodid.getValue();
            objlstComprasDet = (ListModelList) lst_comprasdet.getModel();
            if (s_estado.equals("N")) {
                if (s_estadoDetalle.equals("N")) {
                    if (validaIngresoProducto(pro_id)) {
                        objComprasDet = (ComprasDet) generaRegistroDetalle();
                        objlstComprasDet.add(objComprasDet);
                        lst_comprasdet.setModel(objlstComprasDet);
                    } else {
                        Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                } else {
                    objComprasDet = (ComprasDet) generaRegistroDetalle();
                    int lst_ord = lst_comprasdet.getSelectedIndex();
                    objlstComprasDet.set(lst_ord, objComprasDet);
                }
            } else {
                if (s_estadoDetalle.equals("N")) {
                    if (validaIngresoProducto(pro_id)) {
                        objComprasDet = (ComprasDet) generaRegistroDetalle();
                        objComprasDet.setInd_accion("N");
                        objlstComprasDet.add(objComprasDet);
                    } else {
                        Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                } else {
                    String indicador = objComprasDet.getInd_accion();
                    if (indicador.equals("N") || indicador.equals("NM")) {
                        objComprasDet = (ComprasDet) generaRegistroDetalle();
                        objComprasDet.setInd_accion("NM");
                    } else {
                        long item = objComprasDet.getTcd_item();
                        objComprasDet = (ComprasDet) generaRegistroDetalle();
                        objComprasDet.setInd_accion("M");
                        objComprasDet.setTcd_item(item);
                    }
                    int lst_ord = lst_comprasdet.getSelectedIndex();
                    objlstComprasDet.set(lst_ord, objComprasDet);
                }
            }
            s_estadoDetalle = "Q";
            Utilitarios.deshabilitarLista(false, lst_comprasdet);
            habilitaCampos(false);
            llenarTCamposDetalle();
            habilitaBotonesDetalle(false, true);
            limpiarCamposDetalle();
            habilitaCamposDetalle(true);
            lst_comprasdet.setSelectedIndex(-1);
        }
    }

    //Eventos Secundarios - Validacion       
//    @Listen("onCtrlKey=#tabbox_genfacturap")
//    public void GuardarInformacion(Event event) throws SQLException {
//        int keyCode;
//        keyCode = ((KeyEvent) event).getKeyCode();
//        switch (keyCode) {
//            case 121:
//                if (!tbbtn_btn_guardar.isDisabled()) {
//                    botonGuardar();
//                }
//                break;
//            case 118:
//                botonNuevoDetalle();
//                break;
//        }
//    }
    
        //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_genfacturap")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo();
                }
                break;
            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    botonEliminar();
                }
                break;

            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;

            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;

            case 78:
                if (!tbbtn_btn_nuevoc.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarc.isDisabled()) {
                    botonEditarDetalle();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarc.isDisabled()) {
                    botonEliminarDetalle();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarc.isDisabled()) {
                    botonGuardarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacerc.isDisabled()) {
                    botonDeshacerDetalle();
                }
                break;
        }
    }
    
    @Listen("onOK=#txt_filtro_fac")
    public void onOKFactura() throws SQLException {
        txt_providcab.focus();
    }
    
    @Listen("onBlur=#txt_filtro_fac")
    public void onBlurFactura() throws SQLException {
        if (!txt_filtro_fac.getValue().isEmpty()) {
            if (!txt_filtro_fac.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_filtro_fac.setValue("");
                        txt_filtro_fac.focus();
                    }
                });
            } else {
                String cod = Utilitarios.lpad(txt_filtro_fac.getValue(), 10, "0");
                txt_filtro_fac.setValue(cod);
            }
        }
    }
    
    @Listen("onClick=#btn_ocnropedkey")
    public void lov_oc() throws SQLException {
        if (bandera == false) {
            limpiarCamposDetalle();
            bandera = true;
            Map parametros = new HashMap();
            modoEjecucion = "GenFactProv";
            parametros.put("txt_ocnropedkey", txt_ocnropedkey);
            parametros.put("txt_provid", txt_provid);
            parametros.put("txt_provrazsoc", txt_provrazsoc);
            parametros.put("lst_comprasdet", lst_comprasdet);
            parametros.put("txt_conid", txt_conid);
            parametros.put("txt_condes", txt_condes);
            parametros.put("txt_glosa", txt_glosa);
            parametros.put("chk_efectivo", chk_efectivo);
            //-----descuento general y financiero
            parametros.put("txt_dsctgral", txt_dsctgral);
            parametros.put("txt_dsctfin", txt_dsctfin);
            //----totales de la lista detalle
            parametros.put("txt_lvventaped", txt_lvventaped);
            parametros.put("txt_lvventafac", txt_lvventafac);
            parametros.put("txt_ligvped", txt_ligvped);
            parametros.put("txt_ligvfac", txt_ligvfac);
            parametros.put("txt_lpventaped", txt_lpventaped);
            parametros.put("txt_lpventafac", txt_lpventafac);
            //----totales de la factura
            parametros.put("lbl_valventa", lbl_valventa);
            parametros.put("lbl_valimp", lbl_valimp);
            parametros.put("lbl_valtotal", lbl_valtotal);
            //-------
            parametros.put("validaBusqueda", modoEjecucion);
            parametros.put("controlador", "ControllerGenFacPro");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProcOrdComRec.zul", null, parametros);
            window.doModal();
        }
    }
    
    @Listen("onBlur=#btn_ocnropedkey")
    public void validaoc() throws SQLException {
        bandera = false;
    }
    
    @Listen("onOK=#txt_providcab")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_providcab.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "GenFactProv";
                String tipo = "1";
                objMapObjetos.put("txt_providcab", txt_providcab);
                objMapObjetos.put("txt_provdescab", txt_provdescab);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerGenFactProv");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                tbbtn_btn_buscar.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_providcab")
    public void valida_proveedor() throws SQLException {
        if (!txt_providcab.getValue().isEmpty()) {
            if (!txt_providcab.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_providcab.setValue("");
                                    txt_provdescab.setValue("");
                                    txt_providcab.focus();
                                }
                            }
                        });
            } else {
                long prov_id = Long.parseLong(txt_providcab.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    s_mensaje = "El código de proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_providcab.setValue("");
                                        txt_provdescab.setValue("");
                                        txt_providcab.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_providcab.setValue(objProveedor.getProv_id());
                    txt_provdescab.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }
    
    @Listen("onOK=#txt_prodid")
    public void lov_productosoc() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_prodid.getValue().isEmpty()) {
                Map parametros = new HashMap();
                modoEjecucion = "GenFactProv";
                long nropedkey = Long.parseLong(txt_ocnropedkey.getValue());
                parametros.put("txt_prodid", txt_prodid);
                parametros.put("txt_proddes", txt_proddes);
                parametros.put("txt_cantped", txt_cantped);
                parametros.put("txt_cantfac", txt_cantfac);
                parametros.put("txt_preuni", txt_preuni);
                parametros.put("txt_preunifac", txt_preunifac);
                parametros.put("txt_totalped", txt_totalped);
                parametros.put("txt_totalfac", txt_totalfac);
                parametros.put("nropedkey", nropedkey);
                parametros.put("tbbtn_btn_guardarc", tbbtn_btn_guardarc);
                parametros.put("validaBusqueda", modoEjecucion);
                parametros.put("controlador", "ControllerGenFactProv");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovOrdCompDet.zul", null, parametros);
                window.doModal();
            } else {
                txt_cantfac.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_prodid")
    public void validaProductooc() throws SQLException {
        if (!txt_prodid.getValue().isEmpty()) {
            if (!txt_prodid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalle();
                            txt_prodid.focus();
                        }
                    }
                });
            } else {
                long oc = Long.parseLong(txt_ocnropedkey.getValue());
                String pro_id = txt_prodid.getValue();
                objOrdCompDet = new DaoOrdCom().OrdCompDetFacxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), oc, "C", pro_id, s_estado, txt_tcid.getValue());
                if (objOrdCompDet == null) {
                    s_mensaje = "El código de producto no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarCamposDetalle();
                                txt_prodid.focus();
                            }
                        }
                    });
                } else {
                    txt_prodid.setDisabled(true);
                    txt_proddes.setValue(objOrdCompDet.getPro_des());
                    txt_preuni.setValue(objOrdCompDet.getOcd_precio());
                    txt_preunifac.setValue((objOrdCompDet.getOcd_precio() * 100) / (100 - (chk_efectivo.isChecked() ? 0 : txt_dsctfin.getValue())));
                    txt_pdescxart.setValue(0);
                    txt_vdescxart.setValue(0);
                    txt_totalped.setValue(objOrdCompDet.getSubtotal());
                    txt_totalfac.setValue(0);
                    lbl_codori.setValue(objOrdCompDet.getPro_codori());
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objOrdCompDet.getPro_id() + " y ha encontrado 1 registro(s)");
                }
            }
        } else {
            limpiarCamposDetalle();
        }
        bandera = false;
    }
    
    @Listen("onCheck=#chk_efectivo")
    public void onCheckefectivo() throws SQLException {
        //if (chk_efectivo.isChecked()) {
        CalculoEfectivo(chk_efectivo.isChecked());
        //}
    }
    
    @Listen("onOK=#d_fecemi")
    public void onOKFechaEmision() throws SQLException {
        if (d_fecemi.getValue() == null) {
            s_mensaje = "Debe ingresar la fecha de emisión";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                d_fecemi.focus();
                                d_fecemi.select();
                            }
                        }
                    });
        } else {
            d_fecven.focus();
            d_fecven.select();
        }
    }
    
    @Listen("onBlur=#d_fecemi")
    public void onBlurFechaEmision() throws SQLException {
        if (d_fecemi.getValue() != null) {
            obtenerTipoCambio();
        } else {
            Messagebox.show("Debe ingresar fecha de emisión");
        }
    }
    
    @Listen("onOK=#txt_serie")
    public void onOKSerie() {
        if (txt_serie.getValue().isEmpty()) {
            s_mensaje = "Por favor ingresar la serie del documento";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_serie.focus();
                            }
                        }
                    });
        } else {
            txt_nrodoc.focus();
        }
    }

    @Listen("onOK=#txt_nrodoc")
    public void onOKNumeroDocumento() {
        if (txt_nrodoc.getValue().isEmpty()) {
            s_mensaje = "Por favor ingresar el número de documento";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrodoc.setValue(null);
                                txt_nrodoc.focus();
                            }
                        }
                    });
        } else {
            if (s_estado.equals("E")) {
                txt_glosa.focus();
            } else {
                btn_ocnropedkey.setDisabled(false);
                btn_ocnropedkey.setFocus(true);
            }
        }
    }
    
    @Listen("onBlur=#txt_nrodoc")
    public void onBlurNumeroDocumento() {
        if (!txt_nrodoc.getValue().matches("[0-9]*")) {
            s_mensaje = "Por favor ingresar valores numéricos";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrodoc.setValue("");
                                txt_nrodoc.focus();
                            }
                        }
                    });
        }
    }
    
    @Listen("onOK=#d_fecven")
    public void onOKFechaVencimiento() {
        if (d_fecven.getValue() == null) {
            s_mensaje = "Debe ingresar la fecha de vencimiento";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                d_fecven.focus();
                            }
                        }
                    });
        } else {
            cb_moneda.focus();
            cb_moneda.select();
        }
    }
    
    @Listen("onOK=#txt_pdescxart")
    public void onOKDescxArt() {
        txt_cantfac.focus();
    }
    
    @Listen("onBlur=#txt_cantfac")
    public void onBlurCantidadFac() throws SQLException {
        if (txt_cantfac.getValue() != null) {
            if (txt_cantfac.getValue() <= 0) {
                s_mensaje = "Debe ingresar una cantidad mayor a 0";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_cantfac.setValue(null);
                                    txt_cantfac.focus();
                                }
                            }
                        });
            } else if (txt_cantfac.getValue() > txt_cantped.getValue()) {
                s_mensaje = "Debe ingresar una cantidad menor o igual a la cantidad pedida";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_cantfac.setValue(null);
                                    txt_cantfac.focus();
                                }
                            }
                        });
            } else {
                double valbruto = txt_cantfac.getValue() * txt_preunifac.getValue();
                txt_vdescxart.setValue(valbruto * txt_pdescxart.getValue() / 100);
                double valneto = valbruto - txt_vdescxart.getValue();
                double igv = objComprasOrdDet.getIgv() == 0 ? 0 : valneto * (objDaoImpuesto.obtieneValorImpuesto(txt_prodid.getValue()) / 100);
                txt_totalfac.setValue(valneto + igv);
            }
        }
    }
    
    @Listen("onOK=#txt_cantfac")
    public void onOKCantidadFac() throws SQLException {
        txt_totalfac.focus();
        txt_totalfac.setFocus(false);
    }
    
    @Listen("onOK=#txt_totalfac")
    public void onOKtotalFac() throws SQLException {
        botonGuardarDetalle();
    }
    
    @Listen("onOK=#cb_moneda")
    public void onOKTipoCambio() throws SQLException {
        cb_tipdoc.focus();
        cb_tipdoc.select();
    }
    
    @Listen("onBlur=#cb_moneda")
    public void onBlurTipoCambio() throws SQLException {
        obtenerTipoCambio();
    }
    
    @Listen(" onOK=#cb_tipdoc")
    public void SeleccionaTipoDocumento() throws SQLException {
        txt_serie.focus();
    }

    //Eventos Otros     
    public void obtenerTipoCambio() throws SQLException {
        String s_fecha_emision = sdf.format(d_fecemi.getValue());
        if (cb_moneda.getSelectedIndex() == -1) {
            Messagebox.show("Debe seleccionar un tipo de moneda ","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
        } else {
            if ("NUEVOS SOLES".equals(cb_moneda.getValue())) {
                monid = 1;
            } else {
                monid = cb_moneda.getSelectedItem().getValue();
            }
            double i_tc = new DaoTipoCambio().obtieneTipoCambioComercial(s_fecha_emision, monid);
            if (i_tc < 1) {
                Messagebox.show("No existe tipo de cambio para la fecha " + s_fecha_emision, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_tcamb.setValue(null);
                                    cb_moneda.setValue("NUEVOS SOLES");
                                    d_fecemi.focus();
                                    d_fecemi.select();
                                }
                            }
                        });
                
            } else {
                txt_tcamb.setValue(i_tc);
            }
        }
    }
    
    public ComprasCab generaRegistro() {
        objlstComprasDet = (ListModelList) lst_comprasdet.getListModel();
        String tc_id, tc_tipdoc, tc_serie, tc_nrodoc, tc_provid, tc_glosa,
                tc_usuadd, tc_usumod;
        int emp_id, suc_id, tc_moneda, tc_conid, tc_efec;
        long tc_key, tc_nrpedkey;
        double tc_tcambio, tc_dscgral, tc_dscfin,/* tc_tdsctos,*/ tc_ocvtotal, tc_valventa,
                tc_vimpt, tc_vtotal;
        Date tc_fecemi, tc_fecven, tc_fecadd, tc_fecmod;
        tc_id = (txt_tcid.getValue().isEmpty() ? "" : txt_tcid.getValue());
        tc_key = (txt_tckey.getValue() == null ? 0 : txt_tckey.getValue());
        tc_tipdoc = String.valueOf(cb_tipdoc.getSelectedItem().getValue());
        tc_serie = txt_serie.getValue().toUpperCase();
        tc_nrodoc = txt_nrodoc.getValue();
        tc_provid = txt_provid.getValue();
        tc_efec = chk_efectivo.isChecked() ? 1 : 0;
        tc_glosa = txt_glosa.getValue();
        tc_usuadd = objUsuCredential.getCuenta();
        tc_usumod = objUsuCredential.getCuenta();
        
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        tc_moneda = Integer.parseInt(cb_moneda.getSelectedItem().getValue().toString());
        tc_conid = Integer.parseInt(txt_conid.getValue());
        
        tc_nrpedkey = Long.parseLong(txt_ocnropedkey.getValue());
        tc_tcambio = (txt_tcamb.getValue() == null ? 0 : txt_tcamb.getValue());
        /**
         * *************************************************************************
         */
        tc_dscgral = (txt_dsctgral.getValue() == null ? 0 : txt_dsctgral.getValue());
        tc_dscfin = (txt_dsctfin.getValue() == null ? 0 : txt_dsctfin.getValue());
        //tc_tdsctos = Double.valueOf(lbl_tdsctos.getValue().replace(",", "").trim());
        tc_ocvtotal = 0;
        for (int i = 0; i < objlstComprasDet.getSize(); i++) {
            tc_ocvtotal = tc_ocvtotal + objlstComprasDet.get(i).getTcd_vventaped();
        }
        tc_valventa = Double.valueOf(lbl_valventa.getValue().replace(",", "").trim());
        tc_vimpt = Double.valueOf(lbl_valimp.getValue().replace(",", "").trim());
        tc_vtotal = Double.valueOf(lbl_valtotal.getValue().replace(",", "").trim());
        /**
         * *************************************************************************
         */
        tc_fecemi = d_fecemi.getValue();
        tc_fecven = d_fecven.getValue();
        
        return new ComprasCab(emp_id, suc_id, tc_key, tc_tipdoc, tc_serie,
                tc_nrodoc, tc_provid, tc_fecemi, tc_fecven, tc_nrpedkey,
                tc_moneda, tc_tcambio, tc_conid, tc_dscgral, tc_dscfin, tc_ocvtotal,
                /*tc_tdsctos, */ tc_valventa, tc_vimpt, tc_vtotal, tc_glosa,
                1, tc_efec, tc_usuadd,tc_usumod);
    }
    
    public Object generaRegistroDetalle() throws SQLException {
        long tc_key = txt_tckey.getValue() == null ? 0 : txt_tckey.getValue();
        String pro_des = txt_proddes.getValue();
        String pro_desdes = new DaoProductos().getDescripcionCortaProducto(txt_prodid.getValue());
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();
        String tcd_codori = lbl_codori.getValue();
        String pro_id = txt_prodid.getValue();
        double tcd_preuni = txt_preuni.getValue();
        double tcd_preunifac = txt_preunifac.getValue();
        double tcd_cantped = txt_cantped.getValue();
        double tcd_cantfac = txt_cantfac.getValue();
        double tcd_cantrec = 0;
        double tcd_descxart = txt_pdescxart.getValue();
        double tcd_vdescxart = tcd_cantfac * tcd_preunifac * tcd_descxart / 100;
        double tcd_vventaped = (tcd_cantped * tcd_preuni);
        double tcd_vventafac = (tcd_cantfac * tcd_preunifac) - tcd_vdescxart;
        double tcd_igvped = tcd_vventaped * ((objDaoImpuesto.obtieneValorImpuesto(txt_prodid.getValue())) / 100);
        double tcd_igvfac = tcd_vventafac * ((objDaoImpuesto.obtieneValorImpuesto(txt_prodid.getValue())) / 100);
        double tcd_pventaped = tcd_vventaped + tcd_igvped;
        double tcd_pventafac = tcd_vventafac + tcd_igvfac;
        String tcd_usuadd = objUsuCredential.getCuenta();
        String tcd_usumod = objUsuCredential.getCuenta();
        
        return new ComprasDet(tc_key, pro_des, pro_desdes, emp_id, suc_id, tcd_codori, pro_id, tcd_preuni, tcd_preunifac, tcd_cantped, tcd_cantfac, tcd_cantrec,
                tcd_descxart, tcd_vdescxart, tcd_vventaped, tcd_vventafac, tcd_igvped, tcd_igvfac, tcd_pventaped, tcd_pventafac, tcd_usuadd, tcd_usumod);
    }
    
    public void LlenaDataCabecera() throws SQLException {
        //carga monedas
        objlstMonedas = objDaoMonedas.listaMonedas(1);
        cb_moneda.setModel(objlstMonedas);
        cb_moneda.setValue("NUEVOS SOLES");
        //carga tipo de documento
        objlstTipDoc = objDaoTipDoc.listaTipDoc(1);
        cb_tipdoc.setModel(objlstTipDoc);
        cb_tipdoc.setValue("FACTURA");
    }
    
    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }
    
    public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoc.setDisabled(b_valida1);
        tbbtn_btn_editarc.setDisabled(b_valida1);
        tbbtn_btn_eliminarc.setDisabled(b_valida1);
        tbbtn_btn_deshacerc.setDisabled(b_valida2);
        tbbtn_btn_guardarc.setDisabled(b_valida2);
    }
    
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }
    
    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }
    
    public void habilitaCampos(boolean b_valida1) {
        //limpieza dsctos
        //chk_descuentos.setDisabled(b_valida1);
        habilitaCamposDsctos(true);
        chk_efectivo.setDisabled(b_valida1);
        d_fecemi.setDisabled(b_valida1);
        d_fecven.setDisabled(b_valida1);
        cb_moneda.setDisabled(b_valida1);
        cb_tipdoc.setDisabled(b_valida1);
        txt_serie.setDisabled(b_valida1);
        txt_nrodoc.setDisabled(b_valida1);
        btn_ocnropedkey.setDisabled(b_valida1);
        txt_glosa.setDisabled(b_valida1);
    }
    
    public void habilitaCamposDetalle(boolean b_valida1) {
        txt_prodid.setDisabled(b_valida1);
        //txt_preuni.setDisabled(b_valida1);
        txt_pdescxart.setDisabled(b_valida1);
        txt_cantfac.setDisabled(b_valida1);
        txt_preunifac.setDisabled(b_valida1);
        //txt_impnet.setDisabled(b_valida1);
    }
    
    public void llenarCampos(ComprasCab objComCab) {
        txt_tcid.setValue(objComprasCab.getTc_id());
        txt_tckey.setValue(objComprasCab.getTc_key());
        d_fecemi.setValue(objComprasCab.getTc_fecemi());
        d_fecven.setValue(objComprasCab.getTc_fecven());
        cb_moneda.setSelectedItem(Utilitarios.valorPorTexto(cb_moneda, objComprasCab.getTc_moneda()));
        txt_tcamb.setValue(objComprasCab.getTc_tcambio());
        cb_tipdoc.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipdoc, Integer.parseInt(objComprasCab.getTc_tipdoc())));
        txt_serie.setValue(objComprasCab.getTc_serie());
        txt_nrodoc.setValue(objComprasCab.getTc_nrodoc());
        txt_ocnropedkey.setValue(Utilitarios.lpad(String.valueOf(objComprasCab.getTc_nrpedkey()), 10, "0"));
        txt_conid.setValue(Utilitarios.lpad(String.valueOf(objComprasCab.getTc_conid()), 3, "0"));
        txt_condes.setValue(objComprasCab.getTc_condicionpago());
        txt_provid.setValue(objComprasCab.getTc_provid());
        txt_provrazsoc.setValue(objComprasCab.getTc_provrazsoc());
        txt_glosa.setValue(objComprasCab.getTc_glosa());
        chk_efectivo.setChecked(objComprasCab.isValorefec());
        txt_dsctgral.setValue(objComprasCab.getTc_dscgral());
        txt_dsctfin.setValue(objComprasCab.getTc_dscfin());
        lbl_valventa.setValue(String.valueOf(df2.format(objComprasCab.getTc_valventa())));
        lbl_valimp.setValue(String.valueOf(df2.format(objComprasCab.getTc_vimpt())));
        lbl_valtotal.setValue(String.valueOf(df2.format(objComprasCab.getTc_vtotal())));
        txt_usuadd.setValue(objComprasCab.getTc_usuadd());
        d_fecadd.setValue(objComprasCab.getTc_fecadd());
        txt_usumod.setValue(objComprasCab.getTc_usumod());
        d_fecmod.setValue(objComprasCab.getTc_fecmod());
    }
    
    public void llenarCamposDetalle(ComprasDet objComprasDet) {
        txt_prodid.setValue(objComprasDet.getPro_id());
        txt_proddes.setValue(objComprasDet.getPro_des());
        lbl_codori.setValue(objComprasDet.getTcd_codori());
        txt_pdescxart.setValue(objComprasDet.getTcd_dscxart());
        txt_vdescxart.setValue(objComprasDet.getTcd_vdscxart());
        txt_preuni.setValue(objComprasDet.getTcd_precioped());
        txt_preunifac.setValue(objComprasDet.getTcd_preciofac());
        txt_cantped.setValue(objComprasDet.getTcd_cantped());
        txt_cantfac.setValue(objComprasDet.getTcd_cantfac());
        txt_totalped.setValue(objComprasDet.getTcd_prevenped());
        txt_totalfac.setValue(objComprasDet.getTcd_prevenfac());
    }
    
    public void LlenarCamposDetalle(OrdCompDet objComprasOrdDet) {
        txt_prodid.setValue(objComprasOrdDet.getPro_id());
        txt_proddes.setValue(objComprasOrdDet.getPro_des());
        lbl_codori.setValue(objComprasOrdDet.getPro_codori());
        txt_preuni.setValue(objComprasOrdDet.getOcd_precio());
        txt_cantfac.setValue(objComprasOrdDet.getCant_fac());
        txt_cantped.setValue(objComprasOrdDet.getCant_ped());
    }
    
    public void limpiarCampos() {
        //limpieza dsctos
        chk_efectivo.setChecked(false);
        txt_dsctfin.setValue(null);
        txt_dsctgral.setValue(null);
        d_fecemi.setValue(null);
        d_fecven.setValue(null);
        cb_moneda.setSelectedIndex(-1);
        txt_tcamb.setValue(null);
        cb_tipdoc.setSelectedIndex(-1);
        txt_serie.setValue(null);
        txt_nrodoc.setValue("");
        txt_ocnropedkey.setValue("");
        txt_conid.setValue("");
        txt_condes.setValue("");
        txt_provid.setValue("");
        txt_provrazsoc.setValue("");
        txt_glosa.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
        //totales de la lista detalle
        txt_lvventaped.setValue(null);
        txt_lvventafac.setValue(null);
        txt_ligvped.setValue(null);
        txt_ligvfac.setValue(null);
        txt_lpventaped.setValue(null);
        txt_lpventafac.setValue(null);
        /*//campos detalle
         txt_prodid.setValue("");
         txt_proddes.setValue("");
         txt_preuni.setValue(null);
         txt_preunifac.setValue(null);
         txt_cantfac.setValue(null);
         txt_cantped.setValue(null);*/
        //totales de la factura
        lbl_tdsctos.setValue("0.00");
        lbl_valimp.setValue("0.00");
        lbl_valventa.setValue("0.00");
        lbl_valtotal.setValue("0.00");
    }
    
    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }
    
    public void limpiarCamposDetalle() {
        txt_prodid.setValue("");
        txt_proddes.setValue("");
        txt_pdescxart.setValue(null);
        txt_vdescxart.setValue(null);
        txt_preuni.setValue(null);
        txt_preunifac.setValue(null);
        txt_cantfac.setValue(null);
        txt_cantped.setValue(null);
        txt_totalped.setValue(null);
        txt_totalfac.setValue(null);
    }
    
    public void llenarTCamposDetalle() {
        double data[];
        data = calcularTotales();
        txt_lvventaped.setValue(data[0]);
        txt_lvventafac.setValue(data[1]);
        txt_ligvped.setValue(data[2]);
        txt_ligvfac.setValue(data[3]);
        txt_lpventaped.setValue(data[4]);
        txt_lpventafac.setValue(data[5]);
        lbl_tdsctos.setValue(Utilitarios.formatoDecimal(data[6], "#,##0.00"));
        lbl_valventa.setValue(Utilitarios.formatoDecimal(txt_lvventafac.getValue(), "#,##0.00"));
        lbl_valimp.setValue(Utilitarios.formatoDecimal(txt_ligvfac.getValue(), "#,##0.00"));
        lbl_valtotal.setValue(Utilitarios.formatoDecimal(txt_lpventafac.getValue(), "#,##0.00"));
    }
    
    public String verificarDetalle() {
        String valida;
        if (txt_prodid.getValue().isEmpty()) {
            valida = "'Código de producto'";
            foco = "Codigo";
        } else if (txt_cantfac.getValue() == null) {
            valida = "'Cantidad facturada'";
            foco = "Cantidad";
        } else {
            valida = "";
        }
        return valida;
    }
    
    public String verificar() {
        String valida;
        if (d_fecemi.getValue() == null) {
            valida = "El campo 'Fecha emision' es obligatorio";
            campo = "Fecha Emision";
        } else if (d_fecven.getValue() == null) {
            valida = "El campo 'Fecha Vencimiento' es obligatorio";
            campo = "Fecha Vencimiento";
        } else if (cb_moneda.getSelectedIndex() == -1) {
            valida = "El campo 'Moneda' es obligatorio";
            campo = "Moneda";
        } else if (cb_tipdoc.getSelectedIndex() == -1) {
            valida = "El campo 'Tipo de Documento' es obligatorio";
            campo = "Tipo de Documento";
        } else if (txt_serie.getValue().isEmpty()) {
            valida = "El campo 'Serie' es obligatorio";
            campo = "Serie";
        } else if (txt_nrodoc.getValue().isEmpty()) {
            valida = "El campo 'Documento' es obligatorio";
            campo = "Documento";
        } else if (txt_ocnropedkey.getValue().isEmpty()) {
            valida = "El campo 'Orden de Compra' es obligatorio";
            campo = "Orden de Compra";
        } else if (txt_conid.getValue().isEmpty()) {
            valida = "El campo 'Condicion de Pago' es obligatorio";
            campo = "Condicion de Pago";
        } else if (txt_provid.getValue().isEmpty()) {
            valida = "El campo 'Proveedor' es obligatorio";
            campo = "Proveedor";
        } else {
            valida = "";
        }
        return valida;
    }
    
    public double[] calcularTotales() {
        //double totales[] = new double[8];
        double totales[] = new double[7];
        for (int i = 0; i < objlstComprasDet.getSize(); i++) {
            totales[0] = totales[0] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_vventaped();
            totales[1] = totales[1] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_vventafac();
            totales[2] = totales[2] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_igvped();
            totales[3] = totales[3] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_igvfac();
            totales[4] = totales[4] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_prevenped();
            totales[5] = totales[5] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_prevenfac();
            totales[6] = totales[6] + ((ComprasDet) objlstComprasDet.get(i)).getTcd_vdscxart();
        }
        return totales;
    }
    
    public boolean validaIngresoProducto(String pro_id) {
        int i = 0;
        int cantDet = objlstComprasDet.getSize();
        boolean validaIngreso = true;
        while (i < cantDet && validaIngreso) {
            if (objlstComprasDet.get(i).getPro_id().equals(pro_id)) {
                validaIngreso = false;
            }
            i++;
        }
        return validaIngreso;
    }
    
    public String verificarFechas() {
        String s_valor = "";
        //se tiene que validad que la fecha de emision sea del mismo periodo
        String fecha_emision = sdf.format(d_fecemi.getValue());
        if (d_fecven.getValue().getTime() < d_fecemi.getValue().getTime()) {
            s_valor = "La fecha de recepción debe ser mayor o igual que : " + fecha_emision;
        }
        return s_valor;
    }
    
    public void CalculoEfectivo(boolean ind) throws SQLException {
        double Sumvventaped = 0, Sumvventafac = 0, Sumigvped = 0, Sumigvfac = 0, Sumpventaped = 0, Sumpventafac = 0;
        //ListModelList<ComprasDet> objlstComprasDet;
        if (ind == true) {
            objlstComprasDet = (ListModelList) lst_comprasdet.getListModel();
            for (int i = 0; i < objlstComprasDet.size(); i++) {
                objlstComprasDet.get(i).setTcd_preciofac(objlstComprasDet.get(i).getTcd_precioped());
                objlstComprasDet.get(i).setTcd_vventafac(objlstComprasDet.get(i).getTcd_preciofac() * objlstComprasDet.get(i).getTcd_cantfac());
                objlstComprasDet.get(i).setTcd_igvfac(objlstComprasDet.get(i).getTcd_igvfac() == 0 ? 0 : objlstComprasDet.get(i).getTcd_vventafac() * (objDaoImpuesto.obtieneValorImpuesto(objlstComprasDet.get(i).getPro_id()) / 100));
                objlstComprasDet.get(i).setTcd_prevenfac(objlstComprasDet.get(i).getTcd_vventafac() + objlstComprasDet.get(i).getTcd_igvfac());
                Sumvventaped = Sumvventaped + objlstComprasDet.get(i).getTcd_vventaped();
                Sumvventafac = Sumvventafac + objlstComprasDet.get(i).getTcd_vventafac();
                Sumigvped = Sumigvped + objlstComprasDet.get(i).getTcd_igvped();
                Sumigvfac = Sumigvfac + objlstComprasDet.get(i).getTcd_igvfac();
                Sumpventaped = Sumpventaped + objlstComprasDet.get(i).getTcd_prevenped();
                Sumpventafac = Sumpventafac + objlstComprasDet.get(i).getTcd_prevenfac();
                objlstComprasDet.get(i).setInd_accion("M");
            }
        } else {
            objlstComprasDet = (ListModelList) lst_comprasdet.getListModel();
            for (int i = 0; i < objlstComprasDet.size(); i++) {
                objlstComprasDet.get(i).setTcd_preciofac(objlstComprasDet.get(i).getTcd_precioped() * 100 / (100 - txt_dsctfin.getValue()));
                objlstComprasDet.get(i).setTcd_vventafac(objlstComprasDet.get(i).getTcd_preciofac() * objlstComprasDet.get(i).getTcd_cantfac());
                objlstComprasDet.get(i).setTcd_igvfac(objlstComprasDet.get(i).getTcd_igvfac() == 0 ? 0 : objlstComprasDet.get(i).getTcd_vventafac() * (objDaoImpuesto.obtieneValorImpuesto(objlstComprasDet.get(i).getPro_id()) / 100));
                objlstComprasDet.get(i).setTcd_prevenfac(objlstComprasDet.get(i).getTcd_vventafac() + objlstComprasDet.get(i).getTcd_igvfac());
                Sumvventaped = Sumvventaped + objlstComprasDet.get(i).getTcd_vventaped();
                Sumvventafac = Sumvventafac + objlstComprasDet.get(i).getTcd_vventafac();
                Sumigvped = Sumigvped + objlstComprasDet.get(i).getTcd_igvped();
                Sumigvfac = Sumigvfac + objlstComprasDet.get(i).getTcd_igvfac();
                Sumpventaped = Sumpventaped + objlstComprasDet.get(i).getTcd_prevenped();
                Sumpventafac = Sumpventafac + objlstComprasDet.get(i).getTcd_prevenfac();
                objlstComprasDet.get(i).setInd_accion("M");
            }
        }
        lst_comprasdet.setModel(objlstComprasDet);
        txt_lvventaped.setValue(Sumvventaped);
        txt_lvventafac.setValue(Sumvventafac);
        txt_ligvped.setValue(Sumigvped);
        txt_ligvfac.setValue(Sumigvfac);
        txt_lpventaped.setValue(Sumpventaped);
        txt_lpventafac.setValue(Sumpventafac);
        lbl_valventa.setValue(String.valueOf(df2.format(Sumvventafac)));
        lbl_valimp.setValue(String.valueOf(df2.format(Sumigvfac)));
        lbl_valtotal.setValue(String.valueOf(df2.format(Sumpventafac)));
    }
    
    public void limpiaDsctos() {
        txt_dsctgral.setValue(null);
        txt_dsctfin.setValue(null);
        txt_dsctgral.setDisabled(true);
        txt_dsctfin.setDisabled(true);
        //chk_dsctgral.setChecked(false);
        //chk_dsctcom.setChecked(false);
        //chk_dsctitem.setChecked(false);
    }
    
    public void habilitaCamposDsctos(boolean b_valida) {
        //chk_dsctgral.setDisabled(b_valida);
        //chk_dsctcom.setDisabled(b_valida);
        //chk_dsctitem.setDisabled(b_valida);
        txt_dsctgral.setDisabled(b_valida);
        txt_dsctfin.setDisabled(b_valida);
    }
    
    public void limpiaCamposDsctos() {
        txt_dsctgral.setValue(null);
        txt_dsctfin.setValue(null);
    }
    
    public void limpiarLista() {
        //limpio mi lista
        objlstComprasCab = null;
        objlstComprasCab = new ListModelList<ComprasCab>();
        lst_comprascab.setModel(objlstComprasCab);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
