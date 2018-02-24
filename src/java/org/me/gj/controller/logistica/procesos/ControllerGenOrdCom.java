package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.cxc.mantenimiento.DaoTipoCambio;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoCondicion;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoCfgInicial;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Condicion;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.logistica.procesos.OrdCompCab;
import org.me.gj.model.logistica.procesos.OrdCompDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.CfgInicial;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerGenOrdCom extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaordcomp, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,//------->Botonoes Orden de Compra General
            tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro, tbbtn_btn_deshacerpro, tbbtn_btn_guardarpro;//------->Botonoes Orden de Compra Detalle
    @Wire
    Listbox lst_ordcompcab, lst_ordcompdet;
    @Wire
    Intbox txt_ocd_comdet_key;
    @Wire
    Textbox txt_provid, txt_provdes, txt_provid_busq, txt_provdes_busq, txt_filtro_orden,//----->Opciones de Busqueda
            txt_oc_nropedid, txt_oc_provid, txt_oc_provrazsoc, txt_oc_lpcid, txt_oc_lpcdes, txt_oc_conid, txt_oc_condes,
            txt_oc_glosa, txt_oc_almoriid, txt_oc_almorides, txt_oc_almdesid,
            txt_oc_almdesdes,//------>Datos Cabecera Orden de Compra
            txt_ocd_prodid, txt_ocd_proddes, txt_ocd_glosapro, txt_ocd_peso_und,
            txt_vol_und, txt_odc_prounimanven, //------>Datos Detalle Orden de Compra
            txt_usuadd, txt_usumod, txt_lugent, txt_situacion, txt_est, txt_ocd_idubi, txt_ocd_desubi;//------>Datos Detalle Orden de Compra
    @Wire
    Combobox cb_situacion,//----->Opciones de Busqueda
            cb_moneda, cb_cabecera_periodo, cb_oc_almdesdes, cb_oc_almorides;//------>Datos Cabecera Orden de Compra
    @Wire
    Datebox d_fecini, d_fecfin,//----->Opciones de Busqueda
            d_oc_fecemi, d_oc_fecrec, d_oc_feccad,//------>Datos Cabecera Orden de Compra
            d_fecadd, d_fecmod;//------>Datos Detalle Orden de Compra
    @Wire
    Button btn_buscarordenes;//------>Datos Detalle Orden de Compra
    @Wire
    Doublebox db_oc_tcambio,//------>Datos Cabecera Orden de Compra
            txt_ocd_pdesc, txt_ocd_cantped, txt_tpeso, txt_tprecio, txt_tcantidad, txt_totvolumen,
            txt_tafecto, txt_texonerado, txt_odc_exonerado, txt_tdesc, txt_timp, txt_tpreven,
            txt_ocd_pesounit, txt_voluunit, txt_ocd_precom, txt_odc_valafe,
            txt_odc_valdes, txt_porimp, txt_odc_valimp, txt_odc_preven, txt_tpdes, txt_tvaldes;//------>Datos Detalle Orden de Compra
    //Instancias de Objetos
    ListModelList<OrdCompDet> objlstEliminacion;
    ListModelList lst_ManPeriodos;
    ListModelList lst_Almacenes_Origen;
    ListModelList lst_Almacenes_Destino;
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<OrdCompDet> objlstOrdCompDet;
    Ubicaciones objUbicaciones = new Ubicaciones();
    DaoUbicaciones objdaoubicaciones = new DaoUbicaciones();
    DaoOrdCom objDaoOrdComp;
    DaoAccesos objDaoAccesos;
    DaoCierreDia objDaoCierreDia;
    DaoCfgInicial objDaoCfgInicial;
    CfgInicial objCfgInicial;
    OrdCompCab objOrdCompCab;
    OrdCompDet objOrdCompDet;
    Precio objPrecioCompra;
    Productos objProductos;
    Accesos objAccesos;
    ParametrosSalida objParamActualizacionOC;
    //Variables publicas
    String campo = "";
    String foco = "";
    int emp_id, suc_id;
    int monid;
    int i_selCab, i_selDet;
    String s_estado = "Q", fechaActual, s_mensaje, s_estado_detalle = "Q";
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenOrdCom.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_ordcomp")
    public void llenaRegistros() throws SQLException {
        String prov_id = "%%";
        String situacion = "'1','2','3','4','5','6','7'";
        String orden = "%%";
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        objDaoOrdComp = new DaoOrdCom();
        objDaoCierreDia = new DaoCierreDia();
        objDaoCfgInicial = new DaoCfgInicial();
        objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(emp_id, suc_id);
        Date fecha = new Date();
        d_oc_fecemi.setValue(fecha);
        Utilitarios.sumaFecha(d_oc_fecrec, fecha, 2);//Fec Recepcion        
        Utilitarios.sumaFecha(d_oc_feccad, d_oc_fecrec.getValue(), objCfgInicial.getTcfg_diacad());//Fecha Caducida
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        //actualizamos la situacion de todas las ordenes de compra
        objParamActualizacionOC = new ParametrosSalida();
        objParamActualizacionOC = objDaoOrdComp.actualizaSituacionOC();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | Se actualizo la situacion de las o/c | " + objParamActualizacionOC.getMsgValidacion());
        objlstOrdCompCab = objDaoOrdComp.listaOrdCompCab(emp_id, suc_id, "C", prov_id, situacion, fechaActual, fechaActual, orden);
        ListModelList<Moneda> objlstMonedas;
        objlstMonedas = new DaoMoneda().listaMonedas(1);
        cb_moneda.setModel(objlstMonedas);
        lst_ordcompcab.setModel(objlstOrdCompCab);
        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        String periodo = sdfm.format(fecha);
        cb_cabecera_periodo.setModel(lst_ManPeriodos);
        cb_cabecera_periodo.setValue(periodo);
        lst_Almacenes_Origen = new ListModelList();
        lst_Almacenes_Origen = (new DaoAlmacenes()).almacenDefectoCompra(emp_id, suc_id);
        cb_oc_almorides.setModel(lst_Almacenes_Origen);
        lst_Almacenes_Destino = new ListModelList();
        lst_Almacenes_Destino = (new DaoAlmacenes()).almacenDefecto(emp_id, suc_id);
        cb_oc_almdesdes.setModel(lst_Almacenes_Destino);
        txt_provid_busq.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10201010, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado en Procesos Generación de Orden de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado en Procesos Generación de Orden de Compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creación de Orden de Compra");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creación de Orden de Compra");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edición de Orden de Compra");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edición de Orden de Compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminación de Orden de Compra");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminación de Orden de Compra");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Ordenes de Compra");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Ordenes de Compra");
        }
    }

    @Listen("onClick=#btn_buscarordenes")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
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
        String prov_id = txt_provid_busq.getValue().isEmpty() ? "%%" : txt_provid_busq.getValue();
        String oc_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        String s_orden = txt_filtro_orden.getValue().isEmpty() ? "%%" : txt_filtro_orden.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if ("%%".equals(oc_sit)) {
                oc_sit = "'1','2','3','4','5','6','7'";
            }
            objlstOrdCompCab = new ListModelList();
            objlstOrdCompCab = objDaoOrdComp.listaOrdCompCab(emp_id, suc_id, "C", prov_id, oc_sit, f_emisioni, f_emisionf, s_orden);
            //Validar la tabla sin registro
            if (objlstOrdCompCab.getSize() > 0) {
                lst_ordcompcab.setModel(objlstOrdCompCab);
            } else {
                objlstOrdCompCab = null;
                lst_ordcompcab.setModel(objlstOrdCompCab);
                Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        objlstOrdCompDet = null;
        lst_ordcompdet.setModel(objlstOrdCompDet);
        limpiarCamposDetalle();
        txt_ocd_idubi.setValue("");
        txt_ocd_desubi.setValue("");
        limpiarCamposTotales();
    }

    @Listen("onSelect=#lst_ordcompcab")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos la data
        limpiarCampos();
        limpiarCamposDetalle();
        txt_ocd_idubi.setValue("");
        txt_ocd_desubi.setValue("");
        limpiarCamposTotales();
        objOrdCompCab = (OrdCompCab) lst_ordcompcab.getSelectedItem().getValue();
        if (objOrdCompCab == null) {
            objOrdCompCab = objlstOrdCompCab.get(i_selCab + 1);
        }
        i_selCab = lst_ordcompcab.getSelectedIndex();
        llenarCampos();
        llenarCamposDetalle();
        llenarCamposTotales();
    }

    @Listen("onSelect=#lst_ordcompdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objOrdCompDet = lst_ordcompdet.getSelectedItem().getValue();
        llenarCamposProducto();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objOrdCompDet.getPro_id()).toString());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objOrdCompCab = null;
        objOrdCompCab = new OrdCompCab();
        objOrdCompDet = null;
        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        lst_ordcompdet.setModel(objlstOrdCompDet);
        limpiarCampos();
        limpiarCamposDetalle();
        txt_ocd_idubi.setValue("");
        txt_ocd_desubi.setValue("");
        cb_oc_almorides.setValue("ALMACEN PROVEEDOR");
        cb_oc_almdesdes.setValue("ALMACEN PRINCIPAL");
        limpiarCamposTotales();
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitabotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        s_estado = "N";
        cb_cabecera_periodo.focus();
        cb_cabecera_periodo.select();
        //cargo por defecto soles y verifica tipo de cambio
        cb_moneda.setValue("NUEVOS SOLES");
        Date fecha = new Date();
        d_oc_fecemi.setValue(fecha);
        Utilitarios.sumaFecha(d_oc_fecrec, fecha, 2);//Fec Recepcion        
        Utilitarios.sumaFecha(d_oc_feccad, d_oc_fecrec.getValue(), objCfgInicial.getTcfg_diacad());//Fecha Caducida
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_ordcompcab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objOrdCompCab.getOc_situacion() != 1) {
                Messagebox.show("Esta orden de compra ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "M";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                cb_cabecera_periodo.setDisabled(false);
                d_oc_fecemi.setDisabled(false);
                d_oc_fecrec.setDisabled(false);
                d_oc_feccad.setDisabled(false);
                txt_oc_glosa.setDisabled(false);
                validaCabecera();
                txt_oc_lpcid.setDisabled(true);
                txt_oc_conid.setDisabled(false);
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<OrdCompDet>();
                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida;
        s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Código de Proveedor")) {
                            txt_oc_provid.focus();
                        } else if (campo.equals("Condición de Compra")) {
                            txt_oc_conid.focus();
                        } else if (campo.equals("Fecha de Emisión")) {
                            d_oc_fecemi.focus();
                        } else if (campo.equals("Fecha de Recepción")) {
                            d_oc_fecrec.focus();
                        } else if (campo.equals("Fecha de Caducidad")) {
                            d_oc_feccad.focus();
                        } else if (campo.equals("Lista Precio de Compra")) {
                            txt_oc_lpcid.focus();
                        } else if (campo.equals("Tipo de Moneda")) {
                            cb_moneda.focus();
                        } else if (campo.equals("Almacen Origen")) {
                            cb_oc_almorides.focus();
                        } else if (campo.equals("Almacen Destino")) {
                            cb_oc_almdesdes.focus();
                        } else if (campo.equals("Tipo de Cambio")) {
                            db_oc_tcambio.focus();
                        }
                    }
                }
            });
        } else if (objlstOrdCompDet.isEmpty()) {
            Messagebox.show("Por favor ingresar un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_valida = verificarFechas();
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida);
            } else {
                String fecemi = sdf.format(d_oc_fecemi.getValue());
                if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                    Messagebox.show("El día se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if ("NUEVOS SOLES".equals(cb_moneda.getValue())) {
                        monid = 1;
                    } else {
                        monid = cb_moneda.getSelectedItem().getValue();
                    }
                    if (new DaoTipoCambio().obtieneTipoCambioComercial(fecemi, monid) < 1) {
                        Messagebox.show("No existe tipo de cambio para la fecha " + fecemi, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        db_oc_tcambio.setValue(0.00);
                    } else {
                        db_oc_tcambio.setValue(new DaoTipoCambio().obtieneTipoCambioComercial(fecemi, monid));
                        if (!s_estado_detalle.equals("M")) {
                            s_mensaje = "Está seguro que desea guardar los cambios?";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParamCabecera, objParamDetalle;
                                                objOrdCompCab = (OrdCompCab) generaRegistroCabecera();
                                                if (s_estado.equals("N")) {
                                                    objParamCabecera = objDaoOrdComp.insertarOrdCompCab(objOrdCompCab);
                                                } else {
                                                    objParamCabecera = objDaoOrdComp.modificarOrdCompCab(objOrdCompCab);
                                                }
                                                if (objParamCabecera.getFlagIndicador() == 0) {
                                                    boolean verificarDetalle = true;
                                                    int i = 0;
                                                    if (s_estado.equals("N")) {
                                                        while (i < objlstOrdCompDet.getSize() && verificarDetalle) {
                                                            long oc_nropedkey = Long.parseLong(objParamCabecera.getCodigoRegistro());
                                                            objlstOrdCompDet.get(i).setOc_nropedkey(oc_nropedkey);
                                                            objParamDetalle = objDaoOrdComp.insertarOrdCompDet(objlstOrdCompDet.get(i));
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un error con el producto " + objlstOrdCompDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    } else {
                                                        //OPERACION DE ELINACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                                        if (!objlstEliminacion.isEmpty()) {
                                                            while (i < objlstEliminacion.getSize() && verificarDetalle) {
                                                                objParamDetalle = objDaoOrdComp.eliminarOrdCompDet(objlstEliminacion.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrio un error con el item" + objlstEliminacion.get(i).getOcd_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                        //OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                                        i = 0;
                                                        verificarDetalle = true;
                                                        while (i < objlstOrdCompDet.getSize() && verificarDetalle) {
                                                            String indicador = objlstOrdCompDet.get(i).getInd_accion();
                                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                                objParamDetalle = objDaoOrdComp.insertarOrdCompDet(objlstOrdCompDet.get(i));

                                                            } else {
                                                                objParamDetalle = objDaoOrdComp.modificarOrdCompDet(objlstOrdCompDet.get(i));
                                                            }
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un error con el producto " + objlstOrdCompDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    }
                                                    //validacion de guardar/nuevo
                                                    VerificarTransacciones();
                                                    tbbtn_btn_guardar.setDisabled(true);
                                                    tbbtn_btn_deshacer.setDisabled(true);
                                                    //
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    habilitaCampos(true);
                                                    habilitaCamposDetalle(true);
                                                    habilitabotonesDetalle(true, true);
                                                    limpiarCamposDetalle();
                                                    txt_ocd_idubi.setValue("");
                                                    txt_ocd_desubi.setValue("");
                                                    objlstOrdCompCab = new ListModelList<OrdCompCab>();
                                                    objlstEliminacion = new ListModelList<OrdCompDet>();
                                                    objlstOrdCompCab.clear();
                                                    objlstOrdCompDet.clear();
                                                    objlstEliminacion.clear();
                                                    busquedaRegistros();
                                                }
                                                Messagebox.show(objParamCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                        }
                                    });
                        } else {
                            Messagebox.show("El producto ingresado se encuentra en edición", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    }
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_ordcompcab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (objOrdCompCab.getOc_situacion() != 1) {
            Messagebox.show("Esta orden de compra ya no puede ser eliminada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar la orden de compra?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objParamSalida = objDaoOrdComp.eliminarOrdCompCab(objOrdCompCab);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstOrdCompCab.clear();
                                    busquedaRegistros();
                                    s_estado = "Q";
                                    s_estado_detalle = "Q";
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //validacion de guardar/nuevo
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaCampos(true);
                            limpiarCampos();
                            //Campos del detalle de la Orden de Compra
                            habilitabotonesDetalle(true, true);
                            habilitaCamposDetalle(true);
                            limpiarCamposDetalle();
                            txt_ocd_idubi.setValue("");
                            txt_ocd_desubi.setValue("");
                            limpiarCamposTotales();
                            s_estado = "Q";
                            s_estado_detalle = "Q";
                            objlstOrdCompCab = null;
                            busquedaRegistros();
                            txt_oc_provid.setReadonly(false);
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstOrdCompCab == null || objlstOrdCompCab.isEmpty()) {
            Messagebox.show("No existe orden de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_ordcompcab.getSelectedIndex() >= 0) {
                objOrdCompCab = (OrdCompCab) lst_ordcompcab.getSelectedItem().getValue();
                if (objOrdCompCab == null) {
                    objOrdCompCab = objlstOrdCompCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
                objMapObjetos.put("codigoOrd", objOrdCompCab.getOc_nropedkey());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenOrdCom.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una orden de compra para imprimir");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevopro")
    public void botonNuevoDetalle() throws SQLException {
        if (txt_oc_lpcid.getValue().equals("")) {
            s_mensaje = "Debe ingresar una Lista de Precio";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_oc_lpcdes.setValue("");
                                txt_oc_lpcid.focus();
                            }
                        }
                    });
        } else {
            Ubicaciones objubUbicaciones = objdaoubicaciones.ubicacionDefault(1);
            if (objubUbicaciones != null) {
                limpiarCamposDetalle();
                txt_ocd_idubi.setValue("");
                txt_ocd_desubi.setValue("");
                lst_ordcompdet.setSelectedIndex(-1);
                habilitaCamposDetalle(false);
                habilitabotonesDetalle(true, false);
                txt_ocd_proddes.setDisabled(true);
                tbbtn_btn_guardarpro.setDisabled(false);
                Utilitarios.deshabilitarLista(true, lst_ordcompdet);
                txt_ocd_prodid.focus();
                txt_ocd_idubi.setValue(objubUbicaciones.getUbic_id());
                txt_ocd_desubi.setValue(objubUbicaciones.getUbic_des());
                s_estado_detalle = "N";
            } else {
                Messagebox.show("No hay ubicacion por defecto", "ERP-JIMVER", 1, Messagebox.INFORMATION);

            }
        }
    }

    @Listen("onClick=#tbbtn_btn_editarpro")
    public void botonEditarDetalle() {
        if (lst_ordcompdet.getSelectedIndex() == -1) {
            Messagebox.show("Por favor Seleccione un Registro de Detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitaCamposDetalle(false);
            txt_ocd_prodid.setDisabled(true);
            habilitabotonesDetalle(true, false);
            txt_ocd_proddes.setDisabled(true);
            tbbtn_btn_guardarpro.setDisabled(false);
            Utilitarios.deshabilitarLista(true, lst_ordcompdet);
            txt_ocd_prodid.focus();
            s_estado_detalle = "M";
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarpro")
    public void botonEliminarDetalle() {
        if (objlstOrdCompDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Orden de Compra a Eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (lst_ordcompdet.getSelectedIndex() == -1) {
            Messagebox.show("Por Favor Seleccione un Registro a Eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            objOrdCompDet = (OrdCompDet) lst_ordcompdet.getSelectedItem().getValue();
            if (s_estado.equals("M") && !objOrdCompDet.getInd_accion().equals("N")) {
                objOrdCompDet.setInd_accion("E");
                objlstEliminacion.add(objOrdCompDet);
            }
            objlstOrdCompDet.remove(lst_ordcompdet.getSelectedIndex());
            limpiarCamposDetalle();
            txt_ocd_idubi.setValue("");
            txt_ocd_desubi.setValue("");
            lst_ordcompdet.setModel(objlstOrdCompDet);
        }
        double data[];
        data = calculosTotal();
        txt_tafecto.setValue(data[0]);
        txt_texonerado.setValue(data[1]);
        txt_tpdes.setValue(data[2]);
        txt_tvaldes.setValue(data[3]);
        txt_timp.setValue(data[4]);
        txt_tpreven.setValue(data[5]);
        txt_tpeso.setValue(data[6]);
        txt_totvolumen.setValue(data[7]);
    }

    @Listen("onClick=#tbbtn_btn_deshacerpro")
    public void botonDeshacerDetalle() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    limpiarCamposDetalle();
                    txt_ocd_idubi.setValue("");
                    txt_ocd_desubi.setValue("");
                    habilitaCamposDetalle(true);
                    habilitabotonesDetalle(false, true);
                    tbbtn_btn_guardarpro.setDisabled(true);
                    txt_oc_glosa.focus();
                    llenarCamposTotales();
                    Utilitarios.deshabilitarLista(false, lst_ordcompdet);
                    lst_ordcompdet.clearSelection();
                    s_estado_detalle = "Q";
                }
                lst_ordcompdet.setFocus(true);
                lst_ordcompdet.focus();
            }
        }
        );
    }

    @Listen("onClick=#tbbtn_btn_guardarpro")
    public void botonGuardarDetalle() throws SQLException {
        String validaProducto = verificarDetalle();
        if (!validaProducto.isEmpty()) {
            Messagebox.show("Por favor ingrese valores en el campo " + validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("codigo")) {
                            txt_ocd_prodid.focus();
                        } else if (foco.equals("cantidad1") || foco.equals("cantidad2")) {
                            txt_ocd_cantped.focus();
                        } else if (foco.equals("descuento1") || foco.equals("descuento2") || foco.equals("descuento3")) {
                            txt_ocd_pdesc.focus();
                        } else if (foco.equals("ubicacion")) {
                            txt_ocd_idubi.focus();
                        } else if (foco.equals("ubicaciondes")) {
                            txt_ocd_idubi.focus();
                        }
                    }
                }
            });
        } else {
            if (s_estado_detalle.equals("N")) {
                if (validaIngresoProducto(txt_ocd_prodid.getValue())) {
                    objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                    objOrdCompDet.setInd_accion("N");
                    objlstOrdCompDet.add(objOrdCompDet);
                } else {
                    Messagebox.show("Producto ya ha sido ingresado", "ERP-JIMVER", 1, Messagebox.INFORMATION);
                }
            } //Si se modifica o elimina en el detalle
            else {
                //Si se ingreso en el detalle un nuevo registro
                if (objOrdCompDet.getInd_accion().equals("N")) {
                    objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                    objOrdCompDet.setInd_accion("N");
                } else {
                    objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                    objOrdCompDet.setInd_accion("M");
                }
                //Reemplazar el registro en la ubicacion seleccionada
                objlstOrdCompDet.set(lst_ordcompdet.getSelectedIndex(), objOrdCompDet);
                s_estado_detalle = "Q";
            }
            objlstOrdCompDet.clearSelection();
            lst_ordcompdet.setModel(objlstOrdCompDet);
            txt_oc_provid.setReadonly(true);
            txt_oc_lpcid.setReadonly(true);
            Utilitarios.deshabilitarLista(false, lst_ordcompdet);
            limpiarCamposDetalle();
            txt_ocd_idubi.setValue("");
            txt_ocd_desubi.setValue("");
            habilitaCamposDetalle(true);
            habilitabotonesDetalle(false, true);
            tbbtn_btn_guardarpro.setDisabled(true);
            double data[]/* = new double[8]*/;
            data = calculosTotal();
            txt_tafecto.setValue(data[0]);
            txt_texonerado.setValue(data[1]);
            txt_tpdes.setValue(data[2]);
            txt_tvaldes.setValue(data[3]);
            txt_timp.setValue(data[4]);
            txt_tpreven.setValue(data[5]);
            txt_tpeso.setValue(data[6]);
            txt_totvolumen.setValue(data[7]);
            lst_ordcompdet.setFocus(true);
            lst_ordcompdet.focus();
        }
    }

    //Eventos Secundarios - Validacion
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
                if (Long.parseLong(txt_filtro_orden.getValue()) < 1000000000) {
                    String cod = Utilitarios.lpad(txt_filtro_orden.getValue(), 10, "0");
                    txt_filtro_orden.setValue(cod);
                } else {
                    Messagebox.show("El código ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_filtro_orden.setValue("");
                            txt_filtro_orden.focus();
                        }
                    });
                }
            }
        }
    }

    @Listen("onOK=#d_fecini")
    public void onOKd_fecini() throws SQLException {
        d_fecfin.focus();
    }

    @Listen("onOK=#d_fecfin")
    public void onOKd_fecfin() throws SQLException {
        btn_buscarordenes.focus();
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
                objMapObjetos.put("controlador", "ControllerOrdComp");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onOK=#txt_oc_provid")
    public void lov_ocproveedor() {
        if (bandera == false) {
            bandera = true;
            if (txt_oc_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantOrdCompMant";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_oc_provid);
                objMapObjetos.put("txt_provdes", txt_oc_provrazsoc);
                objMapObjetos.put("txt_oc_conid", txt_oc_conid);
                objMapObjetos.put("txt_oc_condes", txt_oc_condes);
                objMapObjetos.put("d_oc_fecemi", d_oc_fecemi);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerOrdComp");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_moneda.focus();
            }
        }
    }

    @Listen("onOK=#txt_oc_lpcid")
    public void lov_lpCompraoc() {
        if (bandera == false) {
            bandera = true;
            if (txt_oc_provid.getValue().isEmpty() || txt_oc_provrazsoc.getValue().isEmpty()) {
                s_mensaje = "Por favor ingrese un proveedor";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_oc_provid.setValue("");
                                    txt_oc_provrazsoc.setValue("");
                                    txt_oc_lpcid.setValue("");
                                    txt_oc_lpcdes.setValue("");
                                    txt_oc_conid.setValue("");
                                    txt_oc_condes.setValue("");
                                    txt_oc_provid.focus();
                                }
                            }
                        });
            } else {
                if (txt_oc_lpcid.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    String modoEjecucion = "mantOrdCompMant";
                    objMapObjetos.put("txt_lisprecom", txt_oc_lpcid);
                    objMapObjetos.put("txt_lisprecomdes", txt_oc_lpcdes);
                    objMapObjetos.put("txt_proidman", txt_oc_provid);
                    objMapObjetos.put("cb_moneda", cb_moneda);
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerOrdComp");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioCompra.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_oc_conid.focus();
                }
            }
        }
    }

    @Listen("onOK=#txt_oc_conid")
    public void lov_condicionoc() {
        if (txt_oc_provid.getValue().isEmpty() || txt_oc_provrazsoc.getValue().isEmpty()) {
            s_mensaje = "Por favor Verifique los Datos en Proveedor";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_oc_provid.setValue("");
                                txt_oc_provrazsoc.setValue("");
                                txt_oc_lpcid.setValue("");
                                txt_oc_lpcdes.setValue("");
                                txt_oc_condes.setValue("");
                                txt_oc_conid.setValue("");
                                txt_oc_provid.focus();
                            }
                        }
                    });
        } else {
            if (txt_oc_conid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantOrdCompMant";
                objMapObjetos.put("txt_forid", txt_oc_conid);
                objMapObjetos.put("txt_fordes", txt_oc_condes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCondicionesCompra.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_oc_almorides.focus();
                cb_oc_almorides.select();
            }
        }
    }

    @Listen("onOK=#txt_provid_busq")
    public void lov_proveedoresbusq() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid_busq.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantOrdComp";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid_busq);
                objMapObjetos.put("txt_provdes", txt_provdes_busq);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerOrdComp");
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

    @Listen("onOK=#txt_ocd_prodid")
    public void lov_ocd_prodid() {
        if (bandera == false) {
            bandera = true;
            if (txt_ocd_prodid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                String modoEjecucion = "mantOrdComp";
                mapeo.put("proveedor", txt_oc_provid.getValue());
                mapeo.put("txt_proidman", txt_ocd_prodid);
                mapeo.put("txt_prodesman", txt_ocd_proddes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerOrdComp");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                window.doModal();
            } else {
                txt_ocd_cantped.focus();
            }
        }
    }

    @Listen("onBlur=#txt_oc_provid")
    public void validaProveedor() throws SQLException {
        if (objlstOrdCompDet.getSize() > 0) {
            s_mensaje = "Esta Seguro que desea cambiar el proveedor? ";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_oc_provrazsoc.setValue("");
                                txt_oc_lpcid.setValue("");
                                txt_oc_lpcdes.setValue("");
                                txt_oc_conid.setValue("");
                                txt_oc_condes.setValue("");
                                objlstOrdCompDet = null;
                                objlstOrdCompDet = new ListModelList<OrdCompDet>();
                                lst_ordcompdet.setModel(objlstOrdCompDet);
                                limpiarCamposTotales();
                                limpiarCamposDetalle();
                                txt_oc_provid.setReadonly(false);
                                txt_oc_provid.focus();
                            }
                        }
                    });
        } else {
            if (!txt_oc_lpcid.getValue().isEmpty()) {
                txt_oc_provrazsoc.setValue("");
                txt_oc_lpcid.setValue("");
                txt_oc_lpcdes.setValue("");
                txt_oc_conid.setValue("");
                txt_oc_condes.setValue("");
                txt_oc_provid.focus();
            } else if (!txt_oc_provid.getValue().isEmpty()) {
                if (!txt_oc_provid.getValue().matches("[0-9]*")) {
                    s_mensaje = "Por favor ingrese valores numéricos";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_oc_provid.setValue("");
                                        txt_oc_provrazsoc.setValue("");
                                        txt_oc_lpcid.setValue("");
                                        txt_oc_lpcdes.setValue("");
                                        txt_oc_conid.setValue("");
                                        txt_oc_condes.setValue("");
                                        txt_oc_provid.focus();
                                    }
                                }
                            });
                } else {
                    long prov_id = Long.parseLong(txt_oc_provid.getValue());
                    Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                    if (objProveedor == null) {
                        s_mensaje = "El código de proveedor no existe o está inactivo";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                                Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            txt_oc_provid.setValue("");
                                            txt_oc_provrazsoc.setValue("");
                                            txt_oc_lpcid.setValue("");
                                            txt_oc_lpcdes.setValue("");
                                            txt_oc_conid.setValue("");
                                            txt_oc_condes.setValue("");
                                            txt_oc_provid.focus();
                                        }
                                    }
                                });
                    } else {
                        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                        txt_oc_provid.setValue(objProveedor.getProv_id());
                        txt_oc_provrazsoc.setValue(objProveedor.getProv_razsoc());
                        txt_oc_conid.setValue(Utilitarios.lpad(String.valueOf(objProveedor.getCon_key()), 3, "0"));
                        txt_oc_condes.setValue(objProveedor.getCon_des());
                        validaTcambiooc();
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_oc_conid")
    public void validaCondicionoc() throws SQLException {
        if (!txt_oc_conid.getValue().isEmpty()) {
            if (!txt_oc_conid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_oc_conid.setValue("");
                                    txt_oc_condes.setValue("");
                                    txt_oc_conid.focus();
                                }
                            }
                        });
            } else {
                int con_key = Integer.parseInt(txt_oc_conid.getValue());
                Condicion objCondicionCompra = new DaoCondicion().buscarCondicion(con_key, "C");
                if (objCondicionCompra == null) {
                    s_mensaje = "El código de la condición de compra no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_oc_conid.setValue("");
                                        txt_oc_condes.setValue("");
                                        txt_oc_conid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la Condicion de Compra " + objCondicionCompra.getConId() + " y ha encontrado 1 registro(s)");
                    txt_oc_conid.setValue(objCondicionCompra.getConId());
                    txt_oc_condes.setValue(objCondicionCompra.getConDes());
                }
            }
        }
    }

    @Listen("onBlur=#txt_oc_lpcid")
    public void validaLpCompraoc() throws SQLException {
        if (objlstOrdCompDet.getSize() > 0) {
            s_mensaje = "Está seguro que desea cambiar la lista de precio? ";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_oc_lpcid.setValue("");
                                txt_oc_lpcdes.setValue("");
                                objlstOrdCompDet = null;
                                objlstOrdCompDet = new ListModelList<OrdCompDet>();
                                lst_ordcompdet.setModel(objlstOrdCompDet);
                                limpiarCamposTotales();
                                limpiarCamposDetalle();
                                txt_oc_lpcid.setReadonly(false);
                                txt_oc_lpcid.focus();
                            }
                        }
                    });
        } else {
            if (!txt_oc_provid.getValue().isEmpty()) {
                if (!txt_oc_lpcid.getValue().isEmpty()) {
                    if (!txt_oc_lpcid.getValue().matches("[0-9]*")) {
                        s_mensaje = "Por favor ingrese valores numéricos";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                                Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            txt_oc_lpcid.setValue("");
                                            txt_oc_lpcdes.setValue("");
                                            txt_oc_lpcid.focus();
                                        }
                                    }
                                });
                    } else {
                        long prov_key = Long.parseLong(txt_oc_provid.getValue());
                        int lpc_key = Integer.parseInt(txt_oc_lpcid.getValue());
                        ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_key, lpc_key);
                        if (objLpCompra == null) {
                            s_mensaje = "El código de la lista de precio no existe para el proveedor";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                                    Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                txt_oc_lpcid.setValue("");
                                                txt_oc_lpcdes.setValue("");
                                                txt_oc_lpcid.focus();
                                            }
                                        }
                                    });
                        } else {
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la Lista Precio de Compra " + objLpCompra.getLp_id() + " y ha encontrado 1 registro(s)");
                            txt_oc_lpcid.setValue(objLpCompra.getLp_id());
                            txt_oc_lpcdes.setValue(objLpCompra.getLp_des());
                            if ("E".equals(s_estado) && objOrdCompCab.getOc_lpcid() != lpc_key) {
                                validaProductosxLpc();
                            }
                        }
                    }
                } else {
                    txt_oc_lpcdes.setValue("");
                }
            } else {
                Messagebox.show("Ingrese un proveedor", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_oc_lpcid.setValue("");
                                    txt_oc_lpcdes.setValue("");
                                    txt_oc_provid.focus();
                                }
                            }
                        });
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_provid_busq")
    public void validaProveedorBusq() throws SQLException {
        if (!txt_provid_busq.getValue().isEmpty()) {
            if (!txt_provid_busq.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_provid_busq.setValue("");
                                    txt_provdes_busq.setValue("");
                                    txt_provid_busq.focus();
                                }
                            }
                        });
            } else {
                String prov_id = txt_provid_busq.getValue();
                txt_provid_busq.setValue(Utilitarios.lpad(prov_id, 8, "0"));
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(txt_provid_busq.getValue(), "1");
                if (objProveedor == null) {
                    s_mensaje = "El código de proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_provid_busq.setValue("");
                                        txt_provdes_busq.setValue("");
                                        txt_provid_busq.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid_busq.setValue(objProveedor.getProv_id());
                    txt_provdes_busq.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onSelect=#cb_moneda;onOK=#cb_moneda")
    public void validaTcambiooc() throws SQLException {
        if (d_oc_fecemi.getValue() == null) {
            Messagebox.show("Ingresar la fecha de Emisión de la Orden de Compra ");
        } else {
            String s_fecha_emision = sdf.format(d_oc_fecemi.getValue());
            if ("NUEVOS SOLES".equals(cb_moneda.getValue())) {
                monid = 1;
            } else {
                monid = cb_moneda.getSelectedItem().getValue();
            }
            double i_tc = new DaoTipoCambio().obtieneTipoCambioComercial(s_fecha_emision, monid);
            if (i_tc < 1) {
                Messagebox.show("No existe tipo de Cambio para la fecha " + s_fecha_emision);
                db_oc_tcambio.setValue(0.00);
                cb_moneda.focus();
            } else {
                db_oc_tcambio.setValue(i_tc);
            }
        }
    }

    @Listen("onOK=#cb_moneda")
    public void validaMoneda() {
        if (cb_moneda.getSelectedIndex() == -1) {
            Messagebox.show("Por Favor seleccione un tipo de moneda", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            txt_oc_lpcid.focus();
        }
    }

    @Listen("onOK=#d_oc_fecemi")
    public void validaFecEmi() throws SQLException {
        if (d_oc_fecemi.getValue() == null) {
            Messagebox.show("Por favor ingrese la fecha de emisión", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String periodo = cb_cabecera_periodo.getValue();
            String fecha_emisionm = sdfm.format(d_oc_fecemi.getValue());
            if (!fecha_emisionm.equals(periodo)) {
                Messagebox.show("La fecha de emisión debe encontrarse en el periodo : " + periodo, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String fecemi = sdf.format(d_oc_fecemi.getValue());
                if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                    Messagebox.show("El día se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
                    Utilitarios.sumaFecha(d_oc_fecrec, d_oc_fecemi.getValue(), 2);
                    Utilitarios.sumaFecha(d_oc_feccad, d_oc_fecrec.getValue(), objCfgInicial.getTcfg_diacad());
                    validaTcambiooc();
                    d_oc_fecrec.focus();
                }
            }
        }
    }

    @Listen("onBlur=#d_oc_fecemi")
    public void onBlurFecEmi() throws SQLException {
        if (d_oc_fecemi.getValue() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(d_oc_fecemi.getValue());
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                Messagebox.show("No se puede haber emision los domingos, cambie 'FEC.EMISION'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        d_oc_fecemi.focus();
                    }
                });
            } else {
                objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
                Utilitarios.sumaFecha(d_oc_fecrec, d_oc_fecemi.getValue(), 2);
                Utilitarios.sumaFecha(d_oc_feccad, d_oc_fecrec.getValue(), objCfgInicial.getTcfg_diacad());
            }
        }
    }

    @Listen("onOK=#d_oc_fecrec")
    public void validaFecRec() {
        if (d_oc_fecrec.getValue() == null) {
            Messagebox.show("Por favor ingrese la fecha de recepción", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            d_oc_feccad.focus();
        }
    }

    @Listen("onOK=#d_oc_feccad")
    public void validaFecCad() {
        if (d_oc_feccad.getValue() == null) {
            Messagebox.show("Por favor ingrese la fecha de caducidad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            txt_oc_provid.focus();
        }
    }

    @Listen("onBlur=#txt_ocd_prodid")
    public void validaProducto() throws SQLException {
        if (!txt_ocd_prodid.getValue().isEmpty()) {
            if (!txt_ocd_prodid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalle();
                                    txt_ocd_prodid.focus();
                                }
                            }
                        });
            } else {
                Precio objPreciosCompra;
                String pro_id = txt_ocd_prodid.getValue();
                long prov_key = Long.parseLong(txt_oc_provid.getValue());
                int lpc_key = Integer.parseInt(txt_oc_lpcid.getValue());
                objPreciosCompra = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, pro_id, prov_key, lpc_key);
                objProductos = (new DaoProductos()).listaPro(txt_ocd_prodid.getValue());
                if (objProductos == null) {
                    limpiarCamposDetalle();
                    s_mensaje = "El producto no existe o esta inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_ocd_prodid.focus();
                                    }
                                }
                            });
                } else {
                    if (objPreciosCompra != null) {
                        limpiarCamposDetalle();
                        txt_ocd_prodid.setValue(objProductos.getPro_id());
                        txt_ocd_proddes.setValue(objProductos.getPro_des());
                        txt_ocd_pesounit.setValue(objProductos.getPro_peso() * objProductos.getPro_presminven());
                        txt_ocd_peso_und.setValue(objProductos.getPro_unipeso());
                        txt_voluunit.setValue(objProductos.getPro_vol() * objProductos.getPro_presminven());
                        txt_odc_prounimanven.setValue(objProductos.getPro_unimanven());
                        txt_vol_und.setValue("MT3");
                        txt_porimp.setValue(objPreciosCompra.getImp_valor());
                        txt_ocd_precom.setValue(objPreciosCompra.getPre_valvent());
                        txt_ocd_prodid.setDisabled(true);
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto " + objPreciosCompra.getPro_id() + " y ha encontrado 1 registro(s)");
                    } else {
                        limpiarCamposDetalle();
                        txt_ocd_prodid.focus();
                    }
                }
            }
        } else {
            limpiarCamposDetalle();
        }
        bandera = false;
    }

    @Listen("onOK=#txt_ocd_cantped")
    public void valida_txt_cantped() throws SQLException {
        if (txt_ocd_cantped.getValue() == null) {
            s_mensaje = "Por favor ingrese una cantidad de productos";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ocd_cantped.focus();
                            }
                        }
                    });
        } else {
            txt_ocd_pdesc.focus();
        }
    }

    @Listen("onOK=#txt_ocd_pdesc")
    public void onOKdescuento() throws SQLException, ParseException {
        if (txt_ocd_pdesc.getValue() == null) {
            s_mensaje = "Por favor ingrese un porcentaje";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ocd_pdesc.setValue(null);
                                txt_ocd_pdesc.focus();
                            }
                        }
                    });
        } else {
            txt_ocd_idubi.focus();
        }
    }

    @Listen("onBlur=#txt_ocd_pdesc")
    public void validaDescuento() throws SQLException, ParseException {
        if (txt_ocd_pdesc.getValue() != null) {
            if (!txt_ocd_prodid.getValue().isEmpty()) {
                if (txt_ocd_cantped.getValue() != null) {
                    if (txt_ocd_pdesc.getValue().doubleValue() < 0.0) {
                        s_mensaje = "Debe ingresar un valor mayor o igual a cero";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_ocd_pdesc.setValue(null);
                                    txt_odc_valdes.setValue(null);
                                    txt_odc_valimp.setValue(null);
                                    txt_odc_preven.setValue(null);
                                    txt_ocd_pdesc.focus();
                                }
                            }
                        });
                    } else if (txt_ocd_pdesc.getValue() >= 100) {
                        s_mensaje = "El descuento tiene que ser menor que 100";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_ocd_pdesc.setValue(null);
                                    txt_ocd_pdesc.focus();
                                }
                            }
                        });
                    } else {
                        CalculosAEI();
                        txt_ocd_idubi.focus();
                    }
                } else {
                    Messagebox.show("Debe ingresar una cantidad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            txt_ocd_pdesc.setValue(null);
                            txt_ocd_cantped.focus();
                        }
                    });
                }
            }
        } else {
            txt_odc_valdes.setValue(null);
            txt_odc_valimp.setValue(null);
            txt_odc_preven.setValue(null);
        }
    }

    @Listen("onOK=#cb_cabecera_periodo")
    public void onOKCabPeriodo() {
        d_oc_fecemi.focus();
    }

    @Listen("onOK=#txt_oc_glosa")
    public void onOKGlosaCAB() throws SQLException {
        botonNuevoDetalle();
    }

    @Listen("onOK=#txt_ocd_glosapro")
    public void onOKGlosaDET() throws SQLException {
        botonGuardarDetalle();
    }

    @Listen("onOK=#cb_oc_almorides")
    public void onOKAlmOrigen() {
        if (cb_oc_almorides.getSelectedIndex() == -1) {
            cb_oc_almorides.focus();
        } else {
            cb_oc_almdesdes.focus();
            cb_oc_almdesdes.select();
        }
    }

    @Listen("onOK=#cb_oc_almdesdes")
    public void onOKAlmDestino() {
        if (cb_oc_almdesdes.getSelectedIndex() == -1) {
            cb_oc_almdesdes.focus();
        } else {
            txt_oc_glosa.focus();
        }
    }

    @Listen("onSelect=#cb_oc_almdesdes;onBlur=#cb_oc_almdesdes")
    public void onSelectAlmDestino() {
        txt_lugent.setValue(lst_Almacenes_Destino.size() <= 0 ? "" : ((Almacenes) lst_Almacenes_Destino.get(0)).getAlm_direcc());
        //txt_lugent.setValue(((Almacenes) lst_Almacenes_Destino.get(0)).getAlm_direcc());
    }

    @Listen("onBlur=#txt_ocd_cantped")
    public void formatoCantPed() throws SQLException {
        if (txt_ocd_cantped.getValue() != null) {
            if (!txt_ocd_prodid.getValue().isEmpty()) {
                if (txt_ocd_cantped.getValue().doubleValue() <= 0.0) {
                    s_mensaje = "Debe ingresar una cantidad mayor a cero";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            txt_ocd_cantped.setValue(null);
                            txt_odc_valafe.setValue(null);
                            txt_odc_exonerado.setValue(null);
                            txt_odc_valimp.setValue(null);
                            txt_ocd_pdesc.setValue(null);
                            txt_odc_valdes.setValue(null);
                            txt_odc_preven.setValue(null);
                            txt_ocd_cantped.focus();
                        }
                    });
                } else {
                    objProductos = (new DaoProductos()).listaPro(txt_ocd_prodid.getValue());
                    if (objProductos == null) {
                        Messagebox.show("Producto incorrecto");
                    } else {
                        String s_prodinafecto = new DaoPedCom().obtieneProductoConDimp(txt_ocd_prodid.getValue());
                        if (s_prodinafecto.equals("I")) {
                            txt_odc_valafe.setValue(txt_ocd_cantped.getValue() * txt_ocd_precom.getValue());
                            txt_odc_exonerado.setValue(0.0);
                        } else if (s_prodinafecto.equals("E")) {
                            txt_odc_valafe.setValue(0.0);
                            txt_odc_exonerado.setValue(txt_ocd_cantped.getValue() * txt_ocd_precom.getValue());
                        } else {
                            txt_odc_valafe.setValue(txt_ocd_cantped.getValue() * txt_ocd_precom.getValue());
                            txt_odc_exonerado.setValue(0.0);
                        }
                        if (txt_ocd_pdesc.getValue() != null) {
                            CalculosAEI();
                        }
                    }
                }
            } else {
                Messagebox.show("Por favor ingrese un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        limpiarCamposDetalle();
                        txt_ocd_prodid.focus();
                    }
                });
            }
        } else {
            txt_odc_valafe.setValue(null);
            txt_odc_exonerado.setValue(null);
            txt_odc_valdes.setValue(null);
            txt_odc_valimp.setValue(null);
            txt_odc_preven.setValue(null);
        }
    }

    @Listen("onOK=#txt_ocd_idubi")
    public void lov_ubicaciones() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_ocd_idubi.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantOrdComp";
                objMapObjetos.put("txt_ocd_idubi", txt_ocd_idubi);
                objMapObjetos.put("txt_ocd_desubi", txt_ocd_desubi);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenOrdCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbicacion.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_ocd_glosapro.focus();
            }
        }
    }

    @Listen("onBlur=#txt_ocd_idubi")
    public void valida_ubicaciones() throws SQLException {
        if (!txt_ocd_idubi.getValue().isEmpty()) {
            if (!txt_ocd_idubi.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_ocd_idubi.setValue("");
                                    txt_ocd_desubi.setValue("");
                                    txt_ocd_idubi.focus();
                                }
                            }
                        });
            } else {
                String codigo = Utilitarios.lpad(txt_ocd_idubi.getValue(), 4, "0");
                objUbicaciones = (new DaoUbicaciones()).listauUbi(codigo);
                if (objUbicaciones == null) {
                    s_mensaje = "La ubicación no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_ocd_idubi.setValue("");
                                        txt_ocd_desubi.setValue("");
                                        txt_ocd_idubi.focus();
                                    }
                                }
                            });
                } else {
                    txt_ocd_idubi.setValue(codigo);
                    txt_ocd_desubi.setValue(objdaoubicaciones.descripcionUbicacion(txt_ocd_idubi.getValue()));
                }
            }
        } else {
            txt_ocd_desubi.setValue("");
        }
        bandera = false;
    }

    @Listen("onCtrlKey=#tabbox_ordcomp")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
            case 118:
                if (!tbbtn_btn_nuevopro.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
        }
    }

    @Listen("onBlur=#txt_provid_busq")
    public void onBlur_txt_provid_busq() {
        if (txt_provid_busq.getValue().isEmpty()) {
            txt_provdes_busq.setValue("");
        }
    }

    @Listen("onBlur=#txt_oc_provid")
    public void onBlur_txt_oc_provid() {
        if (txt_oc_provid.getValue().isEmpty()) {
            txt_oc_provrazsoc.setValue("");
        }
    }

    @Listen("onBlur=#txt_oc_lpcid")
    public void onBlur_txt_oc_lpcid() {
        if (txt_oc_lpcid.getValue().isEmpty()) {
            txt_oc_lpcdes.setValue("");
        }
    }

    @Listen("onBlur=#txt_oc_conid")
    public void onBlur_txt_oc_conid() {
        if (txt_oc_conid.getValue().isEmpty()) {
            txt_oc_condes.setValue("");
        }
    }
    
            @Listen("onCtrlKey=#w_manprocordcomp")
    public void ctrl_teclado(Event event) throws SQLException, ParseException {
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
        }
    }

    //Eventos Otros 
    public void validaProductosxLpc() {
        s_mensaje = "Usted ha cambiado la lista de precio de compra para esta orden, esto puede afectar a algunos productos, desea realizar los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            for (int i = objlstOrdCompDet.size() - 1; i >= 0; i--) {
                                String pro_id = objlstOrdCompDet.get(i).getPro_id();
                                long prov_key = Long.parseLong(txt_oc_provid.getValue());
                                int lpc_key = Integer.parseInt(txt_oc_lpcid.getValue());
                                objPrecioCompra = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, pro_id, prov_key, lpc_key);
                                if (objPrecioCompra == null) {
                                    if ((!objlstOrdCompDet.get(i).getInd_accion().equals("N") || !objlstOrdCompDet.get(i).getInd_accion().equals("NM"))) {
                                        int ocd_item = objlstOrdCompDet.get(i).getOcd_item();
                                        String oc_ind = "C";
                                        long oc_nropedkey = Long.parseLong(txt_oc_nropedid.getValue());
                                        String ocd_usumod = objUsuarioCredential.getCuenta();
                                        String ocd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
                                        objlstEliminacion.add(new OrdCompDet(oc_nropedkey, emp_id, suc_id, oc_ind, ocd_item, ocd_usumod, ocd_pcmod));
                                    }
                                    objlstOrdCompDet.set(i, null);
                                } else {
                                    txt_ocd_prodid.setValue(objlstOrdCompDet.get(i).getPro_id());
                                    txt_ocd_cantped.setValue(objlstOrdCompDet.get(i).getOcd_cantped());
                                    txt_ocd_pdesc.setValue(objlstOrdCompDet.get(i).getOcd_pdesc());
                                    txt_ocd_glosapro.setValue(objlstOrdCompDet.get(i).getOcd_glosa());
                                    String indicador = objlstOrdCompDet.get(i).getInd_accion();
                                    String ocd_conImp = objPrecioCompra.getPro_condimp();
                                    if (indicador.equals("N") || indicador.equals("NM")) {
                                        if (ocd_conImp.equals("A")) {
                                            objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                                        } else {
                                            objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                                        }
                                        objOrdCompDet.setInd_accion("NM");
                                    } else {

                                        int item = objlstOrdCompDet.get(i).getOcd_item();
                                        if (ocd_conImp.equals("A")) {
                                            objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                                        } else {
                                            objOrdCompDet = (OrdCompDet) generaRegistroDetalle();
                                        }
                                        objOrdCompDet.setInd_accion("M");
                                        objOrdCompDet.setOcd_item(item);
                                    }
                                    objlstOrdCompDet.set(i, objOrdCompDet);
                                }
                            }
                            for (int i = objlstOrdCompDet.size() - 1; i >= 0; i--) {
                                if (objlstOrdCompDet.get(i) == null) {
                                    objlstOrdCompDet.remove(i);
                                }
                            }
                            txt_ocd_prodid.setValue("");
                            txt_ocd_cantped.setValue(null);
                            txt_ocd_pdesc.setValue(null);
                            txt_ocd_glosapro.setValue("");
                            llenarCamposTotales();
                            objOrdCompCab.setOc_lpcid(Integer.parseInt(txt_oc_lpcid.getValue()));
                            Messagebox.show("Se ha Realizado el Cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Lista de Precio " + objOrdCompCab.getOc_lpcid() + " y ha encontrado 1 registro(s)");
                        } else {
                            txt_oc_lpcid.setValue(Utilitarios.lpad(String.valueOf(objOrdCompCab.getOc_lpcid()), 3, "0"));
                            txt_oc_lpcdes.setValue(objOrdCompCab.getOc_lpcdes());
                        }
                    }
                });
    }

    public void validaCabecera() {
        String verificarCabecera = verificar();
        if (!verificarCabecera.isEmpty()) {
            Messagebox.show("Verificar los Datos en el Campo " + verificarCabecera, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitabotonesDetalle(false, true);
            txt_oc_provid.setDisabled(true);
            txt_oc_conid.setDisabled(true);
            txt_oc_lpcid.setDisabled(true);
            cb_moneda.setDisabled(true);
            txt_oc_glosa.focus();
        }
    }

    public double muestraTotal() {
        double sumaTotal = 0;
        for (int i = 0; i < objlstOrdCompDet.size(); i++) {
            sumaTotal = sumaTotal + objlstOrdCompDet.get(i).getOcd_vtotal();
        }
        return sumaTotal;
    }

    public boolean validaIngresoProducto(String pro_id) {
        int i = 0;
        int cantDet = objlstOrdCompDet.getSize();
        boolean validaIngreso = true;
        while (i < cantDet && validaIngreso) {
            if (objlstOrdCompDet.get(i).getPro_id().equals(pro_id)) {
                validaIngreso = false;
            }
            i++;
        }
        return validaIngreso;
    }

    public Object generaRegistroCabecera() {
        double data[];
        String oc_ind = "C";
        long oc_nropedkey = txt_oc_nropedid.getValue().isEmpty() ? 0 : Long.parseLong(txt_oc_nropedid.getValue());
        long oc_provid = Long.parseLong(txt_oc_provid.getValue());
        int oc_moneda = cb_moneda.getSelectedItem().getValue();
        double oc_tcambio = db_oc_tcambio.getValue();
        data = calculosTotal();
        double oc_vafecto = Utilitarios.formatoDecimal(data[0], 4);
        double oc_exonerado = Utilitarios.formatoDecimal(data[1], 4);
        double oc_vimpt = Utilitarios.formatoDecimal(data[2], 4);
        double oc_vtotal = Utilitarios.formatoDecimal(data[5], 4);
        Date oc_fecemi = d_oc_fecemi.getValue();
        Date oc_fecrec = d_oc_fecrec.getValue();
        Date oc_feccad = d_oc_feccad.getValue();
        String oc_glosa = txt_oc_glosa.getValue().toUpperCase();
        int oc_conid = Integer.parseInt(txt_oc_conid.getValue());
        int oc_lpcid = Integer.parseInt(txt_oc_lpcid.getValue());
        double oc_vdesc = Utilitarios.formatoDecimal(data[3], 4);
        String oc_usuadd = objUsuarioCredential.getCuenta();
        String oc_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String oc_usumod = objUsuarioCredential.getCuenta();
        String oc_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new OrdCompCab(emp_id, suc_id, oc_ind, oc_nropedkey, oc_provid, oc_moneda, oc_tcambio, oc_vafecto, oc_exonerado, oc_vimpt,
                oc_vtotal, oc_fecemi, oc_fecrec, oc_feccad, oc_glosa, oc_conid, oc_lpcid, oc_vdesc, oc_usuadd, oc_pcadd, oc_usumod, oc_pcmod);
    }

    public Object generaRegistroDetalle() throws SQLException {
        String oc_ind = "C";
        int l_item = txt_ocd_comdet_key.getValue() == null ? 0 : txt_ocd_comdet_key.getValue();
        long l_nropedid = txt_oc_nropedid.getValue().isEmpty() ? 0 : Long.parseLong(txt_oc_nropedid.getValue());
        String s_proid = txt_ocd_prodid.getValue();
        String s_prodes = objProductos == null ? txt_ocd_proddes.getValue() : objProductos.getPro_des();
        String s_prodesde = objProductos == null ? txt_ocd_proddes.getValue() : objProductos.getPro_desdes();
        double d_cantped = txt_ocd_cantped.getValue().doubleValue();
        String s_prounimaven = txt_odc_prounimanven.getValue();
        double d_peso = txt_ocd_pesounit.getValue();
        String s_pesound;
        double d_pesototal;
        d_pesototal = txt_ocd_peso_und.getValue().toString().equals("GRM") || txt_ocd_peso_und.getValue().toString().equals("MLL") ? (txt_ocd_pesounit.getValue() * txt_ocd_cantped.getValue()) / 1000 : (txt_ocd_pesounit.getValue() * txt_ocd_cantped.getValue());
        if (txt_ocd_peso_und.getValue().toString().equals("GRM")) {
            s_pesound = "KLG";
        } else if (txt_ocd_peso_und.getValue().toString().equals("MLL")) {
            s_pesound = "LTS";
        } else {
            s_pesound = txt_ocd_peso_und.getValue();
        }
        double d_vol = txt_voluunit.getValue();
        String s_volund = txt_vol_und.getValue();
        double d_voltotal = (txt_voluunit.getValue() * txt_ocd_cantped.getValue());
        String s_volundtotal = "MT3";

        double d_precio = txt_ocd_precom.getValue().doubleValue();
        double d_vafecto = txt_odc_valafe.getValue() == null ? 0 : txt_odc_valafe.getValue();
        double d_vexo = txt_odc_exonerado.getValue() == null ? 0 : txt_odc_exonerado.getValue();
        double d_pdesc = (txt_ocd_pdesc.getValue().doubleValue());
        double d_vdesc = (txt_odc_valdes.getValue().doubleValue());
        double d_pimpto = (txt_porimp.getValue().doubleValue());
        double d_vimpto = txt_odc_valimp.getValue() == null ? 0 : txt_odc_valimp.getValue();
        double d_vtotal = (txt_odc_preven.getValue().doubleValue());

        String s_glosa = txt_ocd_glosapro.getValue();
        String ocd_idubi = Utilitarios.lpad(txt_ocd_idubi.getValue(), 4, "0");
        String ocd_usuadd = objUsuarioCredential.getCuenta();
        String ocd_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String ocd_usumod = objUsuarioCredential.getCuenta();
        String ocd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();

        return new OrdCompDet(l_nropedid, emp_id, suc_id, oc_ind, l_item, s_proid, s_prodes, s_prodesde, d_precio,
                d_cantped, d_vafecto, d_vexo, d_pimpto, d_vimpto, s_glosa, d_peso,
                d_vdesc, d_pdesc, d_vtotal, ocd_usuadd, ocd_pcadd, ocd_usumod, ocd_pcmod,
                d_vol, s_volund, s_pesound, s_prounimaven, d_voltotal, s_volundtotal, d_pesototal, s_pesound, ocd_idubi);

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

    public void llenarCampos() {
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
        db_oc_tcambio.setValue(objOrdCompCab.getOc_tcambio());
        txt_oc_glosa.setValue(objOrdCompCab.getOc_glosa());
        cb_oc_almorides.setSelectedItem(Utilitarios.valorPorTexto(cb_oc_almorides, objOrdCompCab.getOc_almori()));
        cb_oc_almdesdes.setSelectedItem(Utilitarios.valorPorTexto(cb_oc_almdesdes, objOrdCompCab.getOc_almdes()));
        txt_usuadd.setValue(objOrdCompCab.getOc_usuadd());
        d_fecadd.setValue(objOrdCompCab.getOc_fecadd());
        txt_usumod.setValue(objOrdCompCab.getOc_usumod());
        d_fecmod.setValue(objOrdCompCab.getOc_fecmod());
        cb_cabecera_periodo.setValue(String.valueOf(objOrdCompCab.getOc_periodo()));
        txt_situacion.setValue(objOrdCompCab.getOc_sitdes());
        txt_lugent.setValue(cb_oc_almdesdes.getValue().isEmpty() ? "" : "MZ. A LT. 32 RETABLO II ETAPA - COMAS");
    }

    public void llenarCamposDetalle() throws SQLException {
        long oc_nropedkey = objOrdCompCab.getOc_nropedkey();
        String oc_ind = "C";
        objlstOrdCompDet = null;
        objlstOrdCompDet = objDaoOrdComp.listaOrdCompDet(emp_id, suc_id, oc_nropedkey, oc_ind);
        lst_ordcompdet.setModel(objlstOrdCompDet);
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
        txt_ocd_idubi.setValue(objOrdCompDet.getPro_ubi());// UBICACION
        txt_ocd_desubi.setValue(objdaoubicaciones.descripcionUbicacion(objOrdCompDet.getPro_ubi()));
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaordcomp.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaordcomp.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitabotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevopro.setDisabled(b_valida1);
        tbbtn_btn_editarpro.setDisabled(b_valida1);
        tbbtn_btn_eliminarpro.setDisabled(b_valida1);
        tbbtn_btn_deshacerpro.setDisabled(b_valida2);
        tbbtn_btn_guardarpro.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_oc_nropedid.setValue("");
        txt_oc_provid.setValue("");
        txt_oc_provrazsoc.setValue("");
        d_oc_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_oc_fecrec.setValue(Utilitarios.hoyAsFecha());
        d_oc_feccad.setValue(Utilitarios.hoyAsFecha());
        txt_est.setValue("ACTIVO");
        txt_oc_lpcid.setValue("");
        txt_oc_lpcdes.setValue("");
        txt_oc_conid.setValue("");
        txt_oc_condes.setValue("");
        cb_moneda.setSelectedIndex(-1);
        db_oc_tcambio.setValue(null);
        txt_oc_glosa.setValue("");
        cb_oc_almorides.setSelectedIndex(-1);
        cb_oc_almdesdes.setSelectedIndex(-1);
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
        cb_cabecera_periodo.setSelectedIndex(-1);
        txt_situacion.setValue("POR APROBAR");
        txt_lugent.setValue("");
    }

    public void limpiarCamposTotales() {
        txt_tafecto.setValue(null);
        txt_texonerado.setValue(null);
        txt_tpdes.setValue(null);
        txt_tvaldes.setValue(null);
        txt_timp.setValue(null);
        txt_tpreven.setValue(null);
        txt_tpeso.setValue(null);
        txt_totvolumen.setValue(null);
        txt_ocd_pesounit.setValue(null);
        txt_ocd_peso_und.setValue(null);
        txt_voluunit.setValue(null);
        txt_ocd_comdet_key.setValue(null);
        txt_ocd_peso_und.setValue("");
        txt_ocd_pesounit.setValue(null);
        txt_vol_und.setValue("");
        txt_voluunit.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_ocd_comdet_key.setValue(null);
        txt_ocd_prodid.setValue("");
        txt_ocd_proddes.setValue("");
        txt_ocd_pdesc.setValue(null);
        txt_ocd_cantped.setValue(null);
        txt_ocd_glosapro.setValue(null);
        txt_ocd_cantped.setValue(null);
        txt_ocd_precom.setValue(null);
        txt_odc_prounimanven.setText("");
        txt_odc_valafe.setValue(null);
        txt_odc_exonerado.setValue(null);
        txt_ocd_pdesc.setValue(null);
        txt_odc_valdes.setValue(null);
        txt_porimp.setValue(null);
        txt_odc_valimp.setValue(null);
        txt_odc_preven.setValue(null);
        txt_ocd_glosapro.setText("");
        txt_ocd_peso_und.setValue("");
        txt_ocd_pesounit.setValue(null);
        txt_vol_und.setValue("");
        txt_voluunit.setValue(null);
    }

    public void habilitaCampos(boolean b_valida1) {
        cb_cabecera_periodo.setDisabled(b_valida1);
        txt_oc_provid.setDisabled(b_valida1);
        d_oc_fecemi.setDisabled(b_valida1);
        d_oc_fecrec.setDisabled(b_valida1);
        d_oc_feccad.setDisabled(b_valida1);
        txt_oc_lpcid.setDisabled(b_valida1);
        txt_oc_conid.setDisabled(b_valida1);
        cb_moneda.setDisabled(b_valida1);
        cb_oc_almorides.setDisabled(b_valida1);
        cb_oc_almdesdes.setDisabled(b_valida1);
        txt_oc_glosa.setDisabled(b_valida1);
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        txt_ocd_prodid.setDisabled(b_valida1);
        txt_ocd_proddes.setDisabled(b_valida1);
        txt_ocd_pdesc.setDisabled(b_valida1);
        txt_ocd_cantped.setDisabled(b_valida1);
        txt_ocd_glosapro.setDisabled(b_valida1);
        txt_ocd_idubi.setDisabled(b_valida1);
    }

    public String verificar() {
        String s_valor;
        if (txt_oc_provid.getValue().isEmpty() || txt_oc_provrazsoc.getValue().isEmpty()) {
            s_valor = "El campo Codigo del Proveedor es obligatorio";
            campo = "Código de Proveedor";
        } else if (txt_oc_conid.getValue().isEmpty() || txt_oc_condes.getValue().isEmpty()) {
            s_valor = "El campo Codigo de la Condición de Compra es obligatorio";
            campo = "Condición de Compra";
        } else if (d_oc_fecemi.getValue() == null || d_oc_fecemi.getValue().toString().length() < 8) {
            s_valor = "El campo Fecha de Emisión es obligatorio";
            campo = "Fecha de Emisión";
        } else if (d_oc_fecrec.getValue() == null || d_oc_fecrec.getValue().toString().length() < 8) {
            s_valor = "El campo Fecha de Recepción es obligatorio";
            campo = "Fecha de Recepción";
        } else if (d_oc_feccad.getValue() == null || d_oc_feccad.getValue().toString().length() < 8) {
            s_valor = "El campo Fecha de Caducidad es obligatorio";
            campo = "Fecha de Caducidad";
        } else if (txt_oc_lpcid.getValue().isEmpty() || txt_oc_lpcdes.getValue().isEmpty()) {
            s_valor = "El campo Código de la Lista Precio de Compra es obligatorio";
            campo = "Lista Precio de Compra";
        } else if (cb_moneda.getSelectedIndex() == -1) {
            s_valor = "El campo Tipo de Moneda es obligatorio";
            campo = "Tipo de Moneda";
        } else if (cb_oc_almorides.getSelectedIndex() == -1) {
            s_valor = "El campo Almacen de Origen";
            campo = "Almacen Origen";
        } else if (cb_oc_almdesdes.getSelectedIndex() == -1) {
            s_valor = "El campo Almacen de Destino";
            campo = "Almacen Destino";
        } else if (db_oc_tcambio.getValue() == null || db_oc_tcambio.getValue() == 0.00) {
            s_valor = "El campo Tipo de Cambio es obligatorio";
            campo = "Tipo de Cambio";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public String verificarFechas() {
        String s_valor = "";
        //se tiene que validad que la fecha de emision sea del mismo periodo
        String periodo = cb_cabecera_periodo.getValue();
        String fecha_emisionm = sdfm.format(d_oc_fecemi.getValue());
        String fecha_emision = sdf.format(d_oc_fecemi.getValue());
        String fecha_recepcion = sdf.format(d_oc_fecrec.getValue());
        if (!fecha_emisionm.equals(periodo)) {
            s_valor = "La fecha de Emisión debe encontrarse en el Periodo : " + periodo;
        } else {
            if (d_oc_fecrec.getValue().getTime() < d_oc_fecemi.getValue().getTime()) {
                s_valor = "La fecha de Recepción debe ser Mayor o igual que : " + fecha_emision;
            } else if (d_oc_feccad.getValue().getTime() < d_oc_fecemi.getValue().getTime()) {
                s_valor = "La fecha de Caducidad debe ser Mayor o igual que : " + fecha_emision;
            } else if (d_oc_fecemi.getValue().getTime() > d_oc_fecrec.getValue().getTime()) {
                s_valor = "La fecha de Recepción debe ser Mayor o igual que : " + fecha_emision;
            } else if (d_oc_fecemi.getValue().getTime() > d_oc_feccad.getValue().getTime()) {
                s_valor = "La fecha de Caducidad debe ser Mayor o igual que : " + fecha_emision;
            } else if (d_oc_fecrec.getValue().getTime() > d_oc_feccad.getValue().getTime()) {
                s_valor = "La fecha de Caducidad debe ser Mayor o igual que : " + fecha_recepcion;
            }
        }
        return s_valor;
    }

    public String verificarDetalle() {
        String valida = "";
        if (txt_ocd_prodid.getValue().isEmpty()) {
            valida = "'Código de producto'";
            foco = "codigo";
        } else if (txt_ocd_cantped.getValue() == null) {
            valida = "'Cantidad'";
            foco = "cantidad";
        } else if (txt_ocd_cantped.getValue() <= 0) {
            valida = "'Cantidad' - Debe ser mayor a 0";
            foco = "cantidad";
        } else if (txt_ocd_pdesc.getValue() == null) {
            valida = "'Descuento'";
            foco = "descuento1";
        } else if (txt_ocd_pdesc.getValue() < 0) {
            valida = "'Descuento' - Debe ser mayor o igual a 0";
            foco = "descuento2";
        } else if (txt_ocd_pdesc.getValue() >= 100) {
            valida = "'Descuento' - Debe ser menor a 100";
            foco = "descuento3";
        } else if (txt_ocd_idubi.getValue().isEmpty()) {
            valida = "'Ubicacion' ";
            foco = "ubicacion";
        } else if (txt_ocd_desubi.getValue() == null) {
            valida = "'Ubicacion' ";
            foco = "ubicaciondes";
        }
        return valida;
    }

    public void CalculosAEI() throws SQLException {
        String s_prodinafecto = new DaoPedCom().obtieneProductoConDimp(txt_ocd_prodid.getValue());
        if (s_prodinafecto.equals("I")) {
            txt_odc_valdes.setValue(txt_odc_valafe.getValue() * (txt_ocd_pdesc.getValue() / 100D));
            txt_odc_valimp.setValue(0.0);
            txt_odc_preven.setValue((txt_odc_valafe.getValue() - txt_odc_valdes.getValue()) + txt_odc_valimp.getValue());
        } else if (s_prodinafecto.equals("E")) {
            txt_odc_valdes.setValue(txt_odc_exonerado.getValue() * (txt_ocd_pdesc.getValue() / 100D));
            txt_odc_valimp.setValue(0.0);
            txt_odc_preven.setValue((txt_odc_exonerado.getValue() - txt_odc_valdes.getValue()) + txt_odc_valimp.getValue());
        } else {
            txt_odc_valdes.setValue(txt_odc_valafe.getValue() * (txt_ocd_pdesc.getValue() / 100D));
            txt_odc_valimp.setValue((txt_odc_valafe.getValue() - txt_odc_valdes.getValue()) * (txt_porimp.getValue() / 100D));
            txt_odc_preven.setValue((txt_odc_valafe.getValue() - txt_odc_valdes.getValue()) + txt_odc_valimp.getValue());
        }
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }

    //metodos sin utilizar
    public void OnChange() {
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
    }
}
