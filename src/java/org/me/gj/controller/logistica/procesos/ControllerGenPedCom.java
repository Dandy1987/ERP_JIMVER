package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.cxc.mantenimiento.DaoTipoCambio;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.*;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoCfgInicial;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.logistica.procesos.PedidoCompraCab;
import org.me.gj.model.logistica.procesos.PedidoCompraDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.CfgInicial;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerGenPedCom extends SelectorComposer implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaPedCom, tab_mantenimientoPedCom;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopc, tbbtn_btn_editarpc, tbbtn_btn_eliminarpc,
            tbbtn_btn_guardarpc, tbbtn_btn_deshacerpc, tbbtn_filtro_buscar;
    @Wire
    Combobox cb_cabecera_periodo, cb_alm_origen, cb_alm_destino, cb_monpedcom;
    @Wire
    Datebox d_filtro_fecemiini, d_filtro_fecemifin, d_fecemi, d_fecrec, d_feccad, d_fecadd, d_fecmod;

    @Wire
    Doublebox db_tipcam, txt_canent, txt_precom, txt_pordes, txt_valafe, txt_valdes, txt_porimp, txt_valimp,
            txt_valexo, txt_preven, txt_totvalafe, txt_totvalexo, txt_totvaldes, txt_totvalimp, txt_totpreven,
            txt_pesounit, txt_voluunit, txt_totpeso, txt_totvolumen, txt_totpordes;
    @Wire
    Textbox txt_forid, txt_filtro_prov, txt_filtro_provdes, txt_nropedcom, txt_est, txt_prodesman, txt_fordes,
            txt_proidman, txt_lisprecom, txt_lisprecomdes, txt_situacion, txt_glosa, txt_lugent, txt_proid,
            txt_prodes, txt_pedcomglo, txt_usuadd, txt_usumod, txt_peso_und, txt_vol_und, txt_prounimanven, txt_filtro_ped,
            txt_pedubi, txt_peddes;
    @Wire
    Listbox lst_pedidocompra, lst_pedidocompra_detalle;
    @Wire
    Button boton, btn_consultarpedcom;
    @Wire
    Intbox txt_prodkey, txt_pedcomdet_key;
    @Wire
    Doublebox l_total;
    //Instancias de Objetos
    ListModelList lst_ManPeriodos;
    ListModelList lst_Almacenes_Origen;
    ListModelList lst_Almacenes_Destino;
    ListModelList lst_Monedas;
    ListModelList<PedidoCompraCab> objListaPedidoCompraCab;
    ListModelList<PedidoCompraDet> objListaPedidoCompraDet, objListaEliminacionPedidoCompraDet;
    PedidoCompraCab objPedidoCompraCab;
    PedidoCompraDet objPedidoCompraDet;
    Precio objPrecioCompra;
    ParametrosSalida objParametrosSalida;
    DaoPedCom objDaoGenPedCom;
    DaoProductos objDaoProductos;
    DaoAccesos objDaoAccesos;
    DaoCierreDia objDaoCierreDia;
    Accesos objAccesos;
    Productos objProductos;
    DaoCfgInicial objDaoCfgInicial;
    CfgInicial objCfgInicial;
    Date fr, fc;
    //Variables publicas
    String s_estado = "Q";
    String s_estado_detalle = "Q";
    String s_mensaje;
    String s_nroPedidoCompra;
    String modoEjecucion;
    String fechaActual;
    String campo = "";
    String foco = "";
    int i_sel;
    int i_selCab, i_selDet;
    int emp_id;
    int suc_id;
    int monid;
    int voltot_prod;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenPedCom.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        objListaEliminacionPedidoCompraDet = new ListModelList<PedidoCompraDet>();
        objDaoGenPedCom = new DaoPedCom();
        objPedidoCompraCab = new PedidoCompraCab();
        objPedidoCompraDet = new PedidoCompraDet();
        objParametrosSalida = new ParametrosSalida();
        objDaoCierreDia = new DaoCierreDia();
        objDaoCfgInicial = new DaoCfgInicial();
        objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(emp_id, suc_id);
        s_estado = "Q";
        s_estado_detalle = "Q";
        s_mensaje = "";
        s_nroPedidoCompra = "";
        txt_prodkey = new Intbox();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        Date fecha = new Date();
        fr = new Date();
        fc = new Date();
        String periodo = sdfm.format(fecha);
        cb_cabecera_periodo.setModel(lst_ManPeriodos);
        cb_cabecera_periodo.setValue(periodo);
        //Fech Remision
        sumaFecha(d_fecrec, fecha, 2);
        fr = sdf.parse(sdf.format(d_fecrec.getValue()));
        //Fech Caducida
        sumaFecha(d_feccad, d_fecrec.getValue(), objCfgInicial.getTcfg_diacad());
        fc = sdf.parse(sdf.format(d_feccad.getValue()));
        db_tipcam.setValue(1.00);
        lst_Almacenes_Origen = new ListModelList();
        lst_Almacenes_Origen = (new DaoAlmacenes()).almacenDefectoCompra(emp_id, suc_id);
        cb_alm_origen.setModel(lst_Almacenes_Origen);
        lst_Almacenes_Destino = new ListModelList();
        lst_Almacenes_Destino = (new DaoAlmacenes()).almacenDefecto(emp_id, suc_id);
        cb_alm_destino.setModel(lst_Almacenes_Destino);
        lst_Monedas = new ListModelList();
        lst_Monedas = (new DaoMoneda()).listaMonedas(1);
        cb_monpedcom.setModel(lst_Monedas);
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        objListaPedidoCompraCab = objDaoGenPedCom.listaPedidosCompras();
        lst_pedidocompra.setModel(objListaPedidoCompraCab);
        objDaoProductos = new DaoProductos();
        txt_filtro_ped.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10202000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Generación de Pedido de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado Procesos Generación de Pedido de Compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un Pedido de Compra");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un Pedido de Compra");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Pedido de Compra");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Pedido de Compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Pedido de Compra");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Pedido de Compra");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Pedidos de Compra");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Pedidos de Compra");
        }
    }

    @Listen("onClick=#btn_consultarpedcom")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String resultado;
        Date fecha_emisioni = d_filtro_fecemiini.getValue();
        Date fecha_emisionf = d_filtro_fecemifin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_filtro_fecemiini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_filtro_fecemifin.getValue());
        }
        String s_proveedor = txt_filtro_prov.getValue();
        String s_pedido = txt_filtro_ped.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objListaPedidoCompraCab = new ListModelList();
            objListaPedidoCompraCab = objDaoGenPedCom.buscarPedidosCompras(f_emisioni, f_emisionf, s_proveedor, s_pedido);
            //Validar la tabla sin registro
            if (objListaPedidoCompraCab.getSize() > 0) {
                lst_pedidocompra.setModel(objListaPedidoCompraCab);
            } else {
                objListaPedidoCompraCab = null;
                lst_pedidocompra.setModel(objListaPedidoCompraCab);
                Messagebox.show("No existen pedidos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        objListaPedidoCompraDet = null;
        lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
        limpiarCamposDetalle();
        limpiarCamposTotales();
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_pedidocompra")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos la data
        limpiaAuditoria();
        limpiarCampos();
        limpiarCamposDetalle();
        limpiarCamposTotales();
        objPedidoCompraCab = (PedidoCompraCab) lst_pedidocompra.getSelectedItem().getValue();
        if (objPedidoCompraCab == null) {
            objPedidoCompraCab = objListaPedidoCompraCab.get(i_selCab + 1);
        }
        i_selCab = lst_pedidocompra.getSelectedIndex();
        llenarCampos(objPedidoCompraCab);
        llenarCamposDetalle();
        txt_peddes.setValue("");
        txt_pedubi.setValue("");
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el codigo ").append(objPedidoCompraCab.getPedcom_id()).toString());
    }

    @Listen("onSelect=#lst_pedidocompra_detalle")
    public void seleccionaRegistroDetalle() throws SQLException {
        objPedidoCompraDet = lst_pedidocompra_detalle.getSelectedItem().getValue();
        //llenarCamposDetalle();
        llenarCamposProducto();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objPedidoCompraDet.getPro_id()).toString());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        objPedidoCompraCab = null;
        objPedidoCompraCab = new PedidoCompraCab();
        objPedidoCompraDet = null;
        objListaPedidoCompraDet = null;
        objListaPedidoCompraDet = new ListModelList();
        lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
        limpiarCampos();
        limpiarCamposDetalle();
        txt_pedubi.setValue("");
        txt_peddes.setValue("");
        limpiarCamposTotales();
        habilitaCampos(false, false);
        habilitaBotones(true, false);
        habilitaBotonesDetalle(false, true);
        Date fecha = new Date();
        d_fecemi.setValue(fecha);
        sumaFecha(d_fecrec, fecha, 2);//Fec Recepcion        
        sumaFecha(d_feccad, d_fecrec.getValue(), objCfgInicial.getTcfg_diacad());//Fecha Caducida
        cb_alm_origen.setValue("ALMACEN PROVEEDOR");
        cb_alm_destino.setValue("ALMACEN PRINCIPAL");

        habilitaTab(true, false);
        seleccionaTab(false, true);
        lst_pedidocompra_detalle.setSelectedIndex(-1);
        cb_cabecera_periodo.focus();
        cb_cabecera_periodo.select();
        //cargo por defecto soles y verifica tipo de cambio
        cb_monpedcom.setValue("NUEVOS SOLES");
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion nuevo para crear un registro").toString());
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_pedidocompra.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un pedido de compra", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (objPedidoCompraCab.getPedcom_sit() != 1) {
            Messagebox.show("Este pedido de compra ya no puede ser modificado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaBotonesDetalle(false, true);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false, true);
            txt_lisprecom.setDisabled(true);
            objListaEliminacionPedidoCompraDet = null;
            objListaEliminacionPedidoCompraDet = new ListModelList();
            cb_cabecera_periodo.focus();
            cb_cabecera_periodo.select();
            LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion modificar para actualizar un registro").toString());
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (!objListaPedidoCompraDet.isEmpty()) {
            String s_valida;
            //Validar los campos de ingreso
            s_valida = verificar();
            //Si hay algun dato vacio 
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("Fecha de Emisión")) {
                                d_fecemi.focus();
                            } else if (campo.equals("Fecha de Recepción")) {
                                d_fecrec.focus();
                            } else if (campo.equals("Fecha de Caducidad")) {
                                d_feccad.focus();
                            } else if (campo.equals("Proveedor")) {
                                txt_proidman.focus();
                            } else if (campo.equals("Tipo de Cambio")) {
                                db_tipcam.focus();
                            } else if (campo.equals("Lista de Precios")) {
                                txt_lisprecom.focus();
                            } else if (campo.equals("Condicion de Compra")) {
                                txt_forid.focus();
                            } else if (campo.equals("Almacen de Origen")) {
                                cb_alm_origen.focus();
                            } else if (campo.equals("Almacen de Destino")) {
                                cb_alm_destino.focus();
                            }
                        }
                    }
                });
            } else {//verifica logica de fechas
                s_valida = verificarFechas();
                if (!s_valida.equals("")) {
                    Messagebox.show(s_valida);
                } else {
                    String fecemi = sdf.format(d_fecemi.getValue());
                    if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                        Messagebox.show("El día se encuentra cerrado o no está aperturado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        if (!s_estado_detalle.equals("M")) {
                            s_mensaje = "Está seguro que desea guardar los cambios? ";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParamCabecera;
                                                objListaPedidoCompraDet.addAll(objListaEliminacionPedidoCompraDet);
                                                objPedidoCompraCab = (PedidoCompraCab) generaRegistro();
                                                if (s_estado.equals("N")) {
                                                    objParamCabecera = objDaoGenPedCom.insertarPedidoCompra(objPedidoCompraCab, getPedidoCompraDet(objListaPedidoCompraDet));
                                                } else {
                                                    objParamCabecera = objDaoGenPedCom.modificarPedidoCompra(objPedidoCompraCab, getPedidoCompraDet(objListaPedidoCompraDet));
                                                }
                                                //Si no hubo errores al grabar en la Base de Datos
                                                if (objParamCabecera.getFlagIndicador() == 0) {
                                                    VerificarTransacciones();
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    habilitaCampos(true, true);
                                                    // habilitaCamposDetalle(true);
                                                    habilitaBotones(false, true);
                                                    habilitaBotonesDetalle(true, false);
                                                    limpiarCamposDetalle();
                                                    objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
                                                    objListaPedidoCompraDet = new ListModelList<PedidoCompraDet>();
                                                    objListaPedidoCompraCab.clear();
                                                    objListaPedidoCompraDet.clear();
                                                    objListaEliminacionPedidoCompraDet.clear();
                                                    //busquedaRegistros();
                                                    tbbtn_btn_guardarpc.setDisabled(true);
                                                    tbbtn_btn_deshacerpc.setDisabled(true);
                                                    txt_proidman.setReadonly(false);
                                                    s_estado = "Q";
                                                    s_estado_detalle = "Q";
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
        } else {
            Messagebox.show("Debe ingresar un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == 1) {
                    VerificarTransacciones();
                    habilitaTab(false, false);
                    seleccionaTab(true, false);
                    habilitaCampos(true, true);
                    limpiarCampos();
                    habilitaBotones(false, true);
                    habilitaBotonesDetalle(true, false);
                    habilitaCamposDetalle(true);
                    limpiarCamposDetalle();
                    objListaPedidoCompraDet = null;
                    lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
                    s_estado = "Q";
                    s_estado_detalle = "Q";
                    //busquedaRegistros();
                    tbbtn_btn_guardarpc.setDisabled(true);
                    tbbtn_btn_deshacerpc.setDisabled(true);
                    txt_proidman.setReadonly(false);
           
                }
            }
        }
        );
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_pedidocompra.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un pedido de compra", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (objPedidoCompraCab.getPedcom_sit() != 1) {
            Messagebox.show("Este pedido de compra ya no puede ser eliminado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            s_mensaje = "Está seguro que desea eliminar este pedido de compra?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", 3, "z-msgbox z-msgbox-question", new EventListener() {
                public void onEvent(Event event)
                        throws Exception {
                    if (((Integer) event.getData()).intValue() == 1) {
                        ParametrosSalida objParamSalida = objDaoGenPedCom.eliminarPedidoCompra(objPedidoCompraCab);
                        if (objParamSalida.getFlagIndicador() == 0) {
                            objListaPedidoCompraCab.clear();
                            s_estado = "Q";
                            s_estado_detalle = "Q";
                        }
                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                    }
                }
            }
            );
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevopc")
    public void botonNuevoDetalle() throws SQLException, ParseException {
        onSelectTipoCambio();
        String valida = verificar();
        if (!valida.equals("")) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Fecha de Emisión")) {
                            d_fecemi.focus();
                        } else if (campo.equals("Fecha de Recepción")) {
                            d_fecrec.focus();
                        } else if (campo.equals("Fecha de Caducidad")) {
                            d_feccad.focus();
                        } else if (campo.equals("Proveedor")) {
                            txt_proidman.focus();
                        } else if (campo.equals("Tipo de Cambio")) {
                            db_tipcam.focus();
                        } else if (campo.equals("Lista de Precios")) {
                            txt_lisprecom.focus();
                        } else if (campo.equals("Condicion de Compra")) {
                            txt_forid.focus();
                        } else if (campo.equals("Almacen de Origen")) {
                            cb_alm_origen.focus();
                        } else if (campo.equals("Almacen de Destino")) {
                            cb_alm_destino.focus();
                        }
                    }
                }
            });
            LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion nuevo para crear un registro en el Detalle").toString());
        } else {
            String s_valida = verificarFechas();
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida);
            } else {
                Ubicaciones ubidef = new DaoUbicaciones().ubicacionDefault(1);
                if (ubidef != null) {
                    s_estado_detalle = "N";
                    txt_proid.focus();
                    txt_pedubi.setValue("");
                    txt_peddes.setValue("");
                    habilitaCampos(true, true);
                    habilitaCamposDetalle(false);
                    habilitaBotonesDetalle(true, false);
                    Utilitarios.deshabilitarLista(true, lst_pedidocompra_detalle);
                    limpiarCamposDetalle();
                    //txt_pedubi.setDisabled(true);
                    txt_pedubi.setValue(ubidef.getUbic_id());
                    txt_peddes.setValue(ubidef.getUbic_des());
                } else {
                    Messagebox.show("No hay ubicacion por defecto", "ERP-JIMVER", 1, Messagebox.INFORMATION);
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_editarpc")
    public void botonEditarDetalle() throws SQLException {
        if (lst_pedidocompra_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto del pedido de compra", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            s_estado_detalle = "M";
            habilitaCamposDetalle(false);
            habilitaBotonesDetalle(true, false);
            habilitaCampos(true, true);
            Utilitarios.deshabilitarLista(true, lst_pedidocompra_detalle);
            txt_canent.focus();
            txt_canent.select();
        }
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion editar para modificar un registro en el detalle del pedido").toString());
    }

    @Listen("onClick=#tbbtn_btn_guardarpc")
    public void botonGuardarDetalle() throws SQLException {
        //Verificar ingreso del detalle del pedido
        String validaProducto = verificarDetalle();
        //Si algun campo del detalle no se ha ingresado
        if (!validaProducto.isEmpty()) {
            Messagebox.show("Por favor ingrese valores en el campo " + validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("codigo")) {
                            txt_proid.focus();
                        } else if (foco.equals("cantidad1") || foco.equals("cantidad2")) {
                            txt_canent.focus();
                        } else if (foco.equals("descuento1") || foco.equals("descuento2") || foco.equals("descuento3")) {
                            txt_pordes.focus();
                        } else if (foco.equals("ubicacion")) {
                            txt_pedubi.setDisabled(false);
                            txt_pedubi.focus();
                        }
                    }
                }
            });
        } else {
            //Si se agrega un item al detalle
            if (s_estado_detalle.equals("N")) {
                //Validar si el producto ya esta ingresado
                if (existeProducto(txt_proid.getValue())) {
                    objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                    objPedidoCompraDet.setInd_accion("N");
                    infoUbicacion();
                    objListaPedidoCompraDet.add(objPedidoCompraDet);
                } else {
                    Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            lst_pedidocompra_detalle.focus();
                        }
                    });

                }
            } //Si se modifica o elimina en el detalle
            else {
                Ubicaciones objUbicaciones = new DaoUbicaciones().BusquedaUbicacion(txt_pedubi.getValue());
                if (objUbicaciones == null) {
                    lst_pedidocompra_detalle.focus();
                } else {
                    //Si se ingreso en el detalle un nuevo registro
                    if (objPedidoCompraDet.getInd_accion().equals("N")) {
                        objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                        infoUbicacion();
                        objPedidoCompraDet.setInd_accion("N");
                    } else {
                        objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                        objPedidoCompraDet.setInd_accion("M");
                    }
                    //Reemplazar el registro en la ubicacion seleccionada
                    objListaPedidoCompraDet.set(lst_pedidocompra_detalle.getSelectedIndex(), objPedidoCompraDet);
                    s_estado_detalle = "Q";
                }
            }
            objListaPedidoCompraDet.clearSelection();
            lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
            Utilitarios.deshabilitarLista(false, lst_pedidocompra_detalle);
            limpiarCamposDetalle();
            txt_peddes.setValue("");
            txt_pedubi.setValue("");
            habilitaCamposDetalle(true);
            habilitaBotonesDetalle(false, true);
            tbbtn_btn_guardarpc.setDisabled(true);
            double data[]/* = new double[7]*/;
            data = calcularTotales();
            txt_totvalafe.setValue(data[0]);
            txt_totvalexo.setValue(data[1]);
            txt_totpordes.setValue(data[2]);
            txt_totvaldes.setValue(data[3]);
            txt_totvalimp.setValue(data[4]);
            txt_totpreven.setValue(data[5]);
            txt_totpeso.setValue(data[6]);
            txt_totvolumen.setValue(data[7]);
            lst_pedidocompra_detalle.setFocus(true);
            lst_pedidocompra_detalle.focus();
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacerpc")
    public void botonDeshacerDetalle() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    //VerificarTransacciones();                    
                    habilitaBotonesDetalle(false, true);
                    limpiarCamposDetalle();
                    txt_peddes.setValue("");
                    txt_pedubi.setValue("");
                    habilitaCampos(false, false);
                    habilitaCamposDetalle(true);
                    Utilitarios.deshabilitarLista(false, lst_pedidocompra_detalle);
                    objListaPedidoCompraDet.clearSelection();
                    s_estado_detalle = "Q";
                }
                lst_pedidocompra_detalle.setFocus(true);
                lst_pedidocompra_detalle.focus();
            }
        }
        );
    }

    @Listen("onClick=#tbbtn_btn_eliminarpc")
    public void botonEliminarDetalle() throws SQLException {
        if (objListaPedidoCompraDet.isEmpty()) {
            Messagebox.show("No hay productos ingresados para el pedido de compra", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (lst_pedidocompra_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            objPedidoCompraDet = (PedidoCompraDet) lst_pedidocompra_detalle.getSelectedItem().getValue();
            int posicion = lst_pedidocompra_detalle.getSelectedIndex();
            if (s_estado.equals("M") && !objPedidoCompraDet.getInd_accion().equals("N")) {
                objPedidoCompraDet.setInd_accion("E");
                objListaEliminacionPedidoCompraDet.add(objPedidoCompraDet);
            }
            objListaPedidoCompraDet.remove(posicion);
            limpiarCamposDetalle();
            txt_peddes.setValue("");
            txt_pedubi.setValue("");

            lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
        }
        double data[];
        data = calcularTotales();
        txt_totvalafe.setValue(data[0]);
        txt_totvalexo.setValue(data[1]);
        txt_totpordes.setValue(data[2]);
        txt_totvaldes.setValue(data[3]);
        txt_totvalimp.setValue(data[4]);
        txt_totpreven.setValue(data[5]);
        txt_totpeso.setValue(data[6]);
        txt_totvolumen.setValue(data[7]);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objListaPedidoCompraCab == null || objListaPedidoCompraCab.isEmpty()) {
            Messagebox.show("No existen pedidos de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_pedidocompra.getSelectedIndex() >= 0) {
                objPedidoCompraCab = (PedidoCompraCab) lst_pedidocompra.getSelectedItem().getValue();
                if (objPedidoCompraCab == null) {
                    objPedidoCompraCab = objListaPedidoCompraCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("codigoped", Integer.parseInt(objPedidoCompraCab.getPedcom_id()));
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenPedCom.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione un pedido de compra para imprimir");
            }
        }
    }
    
    @Listen("onBlur=#txt_proidman")
    public void onBlur_txt_proidman() {
        if (txt_proidman.getValue().isEmpty()) {
            txt_prodesman.setValue("");
        }
    }

    @Listen("onBlur=#txt_forid")
    public void onBlur_txt_forid() {
        if (txt_forid.getValue().isEmpty()) {
            txt_fordes.setValue("");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_filtro_ped")
    public void onOKPedido() throws SQLException {
        txt_filtro_prov.focus();
    }

    @Listen("onBlur=#txt_filtro_ped")
    public void onBlurPedido() throws SQLException {
        if (!txt_filtro_ped.getValue().isEmpty()) {
            if (!txt_filtro_ped.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_filtro_ped.setValue("");
                        txt_filtro_ped.focus();
                    }
                });
            } else {
                if (Long.parseLong(txt_filtro_ped.getValue()) < 1000000000) {
                    String cod = Utilitarios.lpad(txt_filtro_ped.getValue(), 10, "0");
                    txt_filtro_ped.setValue(cod);
                } else {
                    Messagebox.show("El código ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_filtro_ped.setValue("");
                            txt_filtro_ped.focus();
                        }
                    });
                }
            }
        }
    }

    @Listen("onOK=#txt_filtro_prov")
    public void lov_proveedor() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_filtro_prov.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenPedCom";
                mapeo.put("txt_proidman", txt_filtro_prov);
                mapeo.put("txt_prodesman", txt_filtro_provdes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerGenPedCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            } else {

                d_filtro_fecemiini.focus();
            }
        }
    }

    @Listen("onBlur=#txt_filtro_prov")
    public void valida_proveedor() throws SQLException {

        if (!txt_filtro_prov.getValue().isEmpty()) {
            if (!txt_filtro_prov.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_filtro_prov.setValue("");
                        txt_filtro_prov.focus();
                    }
                });
            } else {
                long prov_id = Long.parseLong(txt_filtro_prov.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_filtro_prov.setValue("");
                            txt_filtro_prov.focus();
                        }
                    });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_filtro_prov.setValue(objProveedor.getProv_id());
                    txt_filtro_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#d_filtro_fecemiini")
    public void onOK_d_filtro_fecemiini() throws SQLException {
        d_filtro_fecemifin.focus();
    }

    @Listen("onOK=#d_filtro_fecemifin")
    public void onOK_d_filtro_fecemifin() throws SQLException {
        btn_consultarpedcom.focus();
    }

    @Listen("onOK=#txt_proidman")
    public void lov_proveedorpc() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_proidman.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenPedCom";
                mapeo.put("txt_proidman", txt_proidman);
                mapeo.put("txt_prodesman", txt_prodesman);
                mapeo.put("txt_forid", txt_forid);
                mapeo.put("txt_fordes", txt_fordes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerGenPedCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            } else {
                cb_monpedcom.focus();
                //cb_monpedcom.select();
            }
        }
    }

    @Listen("onBlur=#txt_proidman")
    public void valida_proveedorpc() throws SQLException {

        if (objListaPedidoCompraDet.getSize() > 0) {
            s_mensaje = "Esta Seguro que desea cambiar el proveedor?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_prodesman.setValue("");
                                txt_lisprecom.setValue("");
                                txt_lisprecomdes.setValue("");
                                txt_forid.setValue("");
                                txt_fordes.setValue("");
                                objListaPedidoCompraDet = null;
                                objListaPedidoCompraDet = new ListModelList<PedidoCompraDet>();
                                lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
                                limpiarCamposTotales();
                                limpiarCamposDetalle();
                                //txt_lisprecom.setReadonly(false);
                                txt_proidman.setReadonly(false);
                                txt_proidman.focus();
                                /*} else {
                                 txt_proidman.focus();*/
                            }
                        }
                    });
        } else {
            if (!txt_lisprecom.getValue().isEmpty()) {
                txt_prodesman.setValue("");
                txt_lisprecom.setValue("");
                txt_lisprecomdes.setValue("");
                txt_forid.setValue("");
                txt_fordes.setValue("");
                txt_proidman.focus();
            } else if (!txt_proidman.getValue().isEmpty()) {
                if (!txt_proidman.getValue().matches("[0-9]*")) {
                    s_mensaje = "Por favor ingrese valores numéricos";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_proidman.setValue("");
                                txt_prodesman.setValue("");
                                txt_lisprecom.setValue("");
                                txt_lisprecomdes.setValue("");
                                txt_forid.setValue("");
                                txt_fordes.setValue("");
                                txt_proidman.focus();
                            }
                        }
                    });
                } else {
                    long prov_id = Long.parseLong(txt_proidman.getValue());
                    Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                    if (objProveedor == null) {
                        Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                            public void onEvent(Event t) throws Exception {
                                txt_proidman.setValue("");
                                txt_prodesman.setValue("");
                                txt_lisprecom.setValue("");
                                txt_lisprecomdes.setValue("");
                                txt_forid.setValue("");
                                txt_fordes.setValue("");
                                txt_proidman.focus();
                            }
                        });
                    } else {
                        //txt_lisprecom.setValue("");
                        //txt_lisprecomdes.setValue("");
                        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                        txt_proidman.setValue(objProveedor.getProv_id());
                        txt_prodesman.setValue(objProveedor.getProv_razsoc());
                        txt_forid.setValue(Utilitarios.lpad(String.valueOf(objProveedor.getCon_key()), 3, "0"));
                        txt_fordes.setValue(objProveedor.getCon_des());
                        onSelectTipoCambio();
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_proid")
    public void Lovproducto() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenPedCom";
                mapeo.put("proveedor", txt_proidman.getValue());
                mapeo.put("txt_proid", txt_proid);
                mapeo.put("txt_prodes", txt_prodes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerGenPedCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                window.doModal();
            } else {
                txt_canent.focus();
            }
        }
    }

    @Listen("onBlur=#txt_proid")
    public void valida_Producto() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (!txt_proid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalle();
                                    txt_proid.focus();
                                }
                            }
                        });
            } else {
                Precio objPreciosCompra;
                String pro_id = txt_proid.getValue();
                long prov_key = Long.parseLong(txt_proidman.getValue());
                int lpc_key = Integer.parseInt(txt_lisprecom.getValue());
                objPreciosCompra = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, pro_id, prov_key, lpc_key);//.getPrecioCompraProducto(txt_proid.getValue(), txt_proidman.getValue(), txt_lisprecom.getValue());                    
                objProductos = (new DaoProductos()).listaPro(txt_proid.getValue());
                if (objProductos == null) {
                    limpiarCamposDetalle();
                    s_mensaje = "El producto no existe o esta inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        limpiarCamposDetalle();
                                        txt_proid.focus();
                                    }
                                }
                            });
                } else {
                    if (objPreciosCompra == null) {
                        Messagebox.show("No existe una lista de precios para este producto", "ERP-JIMVER", Messagebox.OK,
                                Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            limpiarCamposDetalle();
                                            txt_proid.focus();
                                        }
                                    }
                                });
                    } else {
                        limpiarCamposDetalle();
                        txt_proid.setValue(objProductos.getPro_id());
                        txt_prodes.setValue(objProductos.getPro_des());
                        txt_pesounit.setValue(objProductos.getPro_peso() * objProductos.getPro_presminven());
                        txt_peso_und.setValue(objProductos.getPro_unipeso());
                        txt_voluunit.setValue(objProductos.getPro_vol() * objProductos.getPro_presminven());
                        txt_prounimanven.setValue(objProductos.getPro_unimanven());
                        txt_vol_und.setValue("MT3");
                        txt_porimp.setValue(objPreciosCompra.getImp_valor());
                        txt_precom.setValue(objPreciosCompra.getPre_valvent());
                    }
                }
            }
        } else {
            limpiarCamposDetalle();
        }

        bandera = false;
    }

    @Listen("onOK=#txt_pedubi")
    public void LovUbicacion() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_pedubi.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenPedCom";
                mapeo.put("txt_pedubi", txt_pedubi);
                mapeo.put("txt_peddes", txt_peddes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerGenPedCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbicacion.zul", null, mapeo);
                window.doModal();
            } else {
                txt_pedcomglo.focus();
            }
        }
    }

    @Listen("onBlur=#txt_pedubi")
    public void valida_Ubicacion() throws SQLException {
        if (!txt_pedubi.getValue().isEmpty()) {
            if (!txt_pedubi.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_pedubi.setValue("");
                                    txt_peddes.setValue("");
                                    txt_pedubi.focus();
                                }
                            }
                        });
            } else {
                Ubicaciones objUbicaciones;
                String ubi_id = txt_pedubi.getValue().isEmpty() ? "" : txt_pedubi.getValue();
                objUbicaciones = (new DaoUbicaciones()).listauUbi(ubi_id);
                //objUbicaciones = new DaoUbicaciones().BusquedaUbicacion(ubi_id);
                if (objUbicaciones == null) {
                    Messagebox.show("No existe una ubicacion", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_pedubi.setValue("");
                                        txt_peddes.setValue("");
                                        txt_pedubi.focus();
                                    }
                                }
                            });
                } else {
                    txt_pedubi.setValue(objUbicaciones.getUbic_id());
                    txt_peddes.setValue(objUbicaciones.getUbic_des());
                }
            }
        } else {
            txt_peddes.setValue("");
        }

        bandera = false;
    }

    @Listen("onOK=#cb_cabecera_periodo")
    public void onOKPeriodo() {
        d_fecemi.focus();
    }

    @Listen("onOK=#d_fecemi")
    public void onOKFemision() throws SQLException {
        if (d_fecemi.getValue() == null) {
            /*Messagebox.show("Por favor ingrese la fecha de emisión", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
             public void onEvent(Event t) throws Exception {
             d_fecemi.focus();
             }
             });*/
            Messagebox.show("Por favor ingrese la fecha de emisión", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            //String fecemi = sdf.format(d_fecemi.getValue());
            String periodo = cb_cabecera_periodo.getValue();
            String fecha_emisionm = sdfm.format(d_fecemi.getValue());
            if (!fecha_emisionm.equals(periodo)) {
                /*Messagebox.show("La fecha de emisión debe encontrarse en el periodo : " + periodo, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                 public void onEvent(Event t) throws Exception {
                 d_fecemi.focus();
                 }
                 });*/
                Messagebox.show("La fecha de emisión debe encontrarse en el periodo : " + periodo, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String fecemi = sdf.format(d_fecemi.getValue());
                if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                    /*Messagebox.show("El día se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                     public void onEvent(Event t) throws Exception {
                     d_fecemi.focus();
                     }
                     });*/
                    Messagebox.show("El día se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                    sumaFecha(d_fecrec, d_fecemi.getValue(), 2);
                    sumaFecha(d_feccad, d_fecrec.getValue(), objCfgInicial.getTcfg_diacad());
                    onSelectTipoCambio();
                    d_fecrec.focus();
                }
            }
        }
    }

    @Listen("onOK=#d_fecrec")
    public void onOKFrecepcion() {
        if (d_fecrec.getValue() == null) {
            Messagebox.show("Por favor ingrese la fecha de recepción", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
                    d_fecrec.focus();
                }
            });
        } else {
            d_feccad.focus();
        }
    }

    @Listen("onOK=#d_feccad")
    public void onOKFcaducidad() {
        if (d_feccad.getValue() == null) {
            Messagebox.show("Por favor ingrese la fecha de caducidad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
                    d_feccad.focus();
                }
            });
        } else {
            txt_proidman.focus();
        }
    }

    @Listen("onSelect=#cb_monpedcom;onOK=#cb_monpedcom")
    public void onSelectTipoCambio() throws SQLException {
        if (d_fecemi.getValue() == null) {
            Messagebox.show("Ingresar la fecha de emisión del pedido de compra ");
        } else {
            String s_fecha_emision = sdf.format(d_fecemi.getValue());
            if ("NUEVOS SOLES".equals(cb_monpedcom.getValue())) {
                monid = 1;
            } else {
                monid = cb_monpedcom.getSelectedItem().getValue();
            }
            double i_tc = new DaoTipoCambio().obtieneTipoCambioComercial(s_fecha_emision, monid);
            if (i_tc < 1) {
                /*Messagebox.show("No existe tipo de cambio para la fecha " + s_fecha_emision, "ERP-JIMVER", Messagebox.OK,
                 Messagebox.INFORMATION, new EventListener() {
                 @Override
                 public void onEvent(Event event) throws Exception {
                 if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                 db_tipcam.setValue(0.00);
                 //d_fecemi.focus();
                 cb_monpedcom.focus();
                 }
                 }
                 });*/
                Messagebox.show("No existe tipo de Cambio para la fecha " + s_fecha_emision);
                db_tipcam.setValue(0.00);
                //d_fecemi.focus();
                cb_monpedcom.focus();
            } else {
                db_tipcam.setValue(i_tc);
                //txt_lisprecom.focus();
            }
        }
    }
    
    @Listen("onOK=#cb_monpedcom")
    public void validaMoneda() {
        if (cb_monpedcom.getSelectedIndex() == -1) {
            Messagebox.show("Por Favor seleccione un tipo de moneda", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            txt_lisprecom.focus();
        }
    }

    @Listen("onOK=#txt_lisprecom")
    public void onOKLpcompra() throws SQLException {
        /*if (objListaPedidoCompraDet.getSize() > 0) {
         s_mensaje = "Se borrarán los productos ingresados";
         Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
         Messagebox.QUESTION, new EventListener() {
         @Override
         public void onEvent(Event event) throws Exception {
         if (((Integer) event.getData()).intValue() == Messagebox.OK) {
         txt_lisprecom.setValue("");
         txt_lisprecomdes.setValue("");
         objListaPedidoCompraDet = null;
         objListaPedidoCompraDet = new ListModelList<PedidoCompraDet>();
         lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
         limpiarCamposTotales();
         limpiarCamposDetalle();
         txt_lisprecom.setReadonly(false);
         }
         }
         });
         } else {*/
        if (bandera == false) {
            bandera = true;
            if (txt_proidman.getValue().isEmpty() || txt_prodesman.getValue().isEmpty()) {
                s_mensaje = "Por favor ingrese un proveedor";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_proidman.setValue("");
                                    txt_prodesman.setValue("");
                                    txt_lisprecom.setValue("");
                                    txt_lisprecomdes.setValue("");
                                    txt_forid.setValue("");
                                    txt_fordes.setValue("");
                                    txt_proidman.focus();
                                }
                            }
                        });
            } else {
                if (txt_lisprecom.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "mantGenPedCom";
                    mapeo.put("txt_lisprecom", txt_lisprecom);
                    mapeo.put("txt_lisprecomdes", txt_lisprecomdes);
                    mapeo.put("txt_proidman", txt_proidman);
                    mapeo.put("cb_moneda", cb_monpedcom);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    mapeo.put("controlador", "ControllerGenPedCom");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioCompra.zul", null, mapeo);
                    window.doModal();
                } else {
                    txt_forid.focus();
                }
            }
        }
        //}
    }

    @Listen("onBlur=#txt_lisprecom")
    public void onBlurLpcompra() throws SQLException {
        if (objListaPedidoCompraDet.getSize() > 0) {
            s_mensaje = "Está seguro que desea cambiar la lista de precio?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_lisprecom.setValue("");
                                txt_lisprecomdes.setValue("");
                                objListaPedidoCompraDet = null;
                                objListaPedidoCompraDet = new ListModelList<PedidoCompraDet>();
                                lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
                                limpiarCamposTotales();
                                limpiarCamposDetalle();
                                txt_lisprecom.setReadonly(false);
                                txt_lisprecom.focus();
                            }
                        }
                    });
        } else {
            if (!txt_proidman.getValue().isEmpty()) {
                if (!txt_lisprecom.getValue().isEmpty()) {
                    if (!txt_lisprecom.getValue().matches("[0-9]*")) {
                        s_mensaje = "Por favor ingrese valores numéricos";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                                Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            txt_lisprecom.setValue("");
                                            txt_lisprecomdes.setValue("");
                                            //limpiarCamposDetalle();
                                            txt_lisprecom.focus();
                                        }
                                    }
                                });
                    } else {
                        long prov_key = Long.parseLong(txt_proidman.getValue());
                        int lpc_key = Integer.parseInt(txt_lisprecom.getValue());
                        ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_key, lpc_key);
                        if (objLpCompra == null) {
                            s_mensaje = "El código de la lista de precios no existe para el proveedor";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                                    Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                txt_lisprecom.setValue("");
                                                txt_lisprecomdes.setValue("");
                                                //limpiarCamposDetalle();
                                                txt_lisprecom.focus();
                                            }
                                        }
                                    });
                        } else {
                            LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objLpCompra.getLp_id()).append(" y ha encontrado 1 registro(s)").toString());
                            txt_lisprecom.setValue(objLpCompra.getLp_id());
                            txt_lisprecomdes.setValue(objLpCompra.getLp_des());
                            if ("M".equals(s_estado) && !objPedidoCompraCab.getPedcom_lispre().equals(Utilitarios.lpad("" + lpc_key, 4, "0"))) {
                                validaProductosxLpc();
                            }
                        }
                    }
                } else {
                    txt_lisprecomdes.setValue("");
                }
            } else {
                Messagebox.show("Ingrese un proveedor", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_lisprecom.setValue("");
                                    txt_lisprecomdes.setValue("");
                                    txt_proid.focus();
                                }
                            }
                        });
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_forid")
    public void onOKFpago() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (!txt_proidman.getValue().isEmpty()) {
                if (txt_forid.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "mantGenPedCom";
                    mapeo.put("txt_forid", txt_forid);
                    mapeo.put("txt_fordes", txt_fordes);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCondicionesCompra.zul", null, mapeo);
                    window.doModal();
                } else {
                    cb_alm_origen.focus();
                    cb_alm_origen.select();
                }
            } else {
                s_mensaje = "Seleccione proveedor";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_prodesman.setValue("");
                                    txt_proidman.setValue("");
                                    txt_proidman.focus();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onBlur=#txt_forid")
    public void onBlurFpago() throws SQLException {
        if (!txt_forid.getValue().isEmpty()) {
            if (!txt_forid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_forid.setValue("");
                                    txt_fordes.setValue("");
                                    txt_forid.focus();
                                }
                            }
                        });
            } else if (txt_proidman.getValue().equals("")) {
                s_mensaje = "Seleccione Proveedor";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_forid.setValue("");
                                    txt_fordes.setValue("");
                                    txt_proidman.focus();
                                }
                            }
                        });
            } else {
                Condicion objCondicion;
                objCondicion = (new DaoCondicion()).condicionCompra(Utilitarios.lpad(txt_forid.getValue(), 3, "0"));
                if (objCondicion == null) {
                    s_mensaje = "El código de condicion no existe o esta inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_forid.setValue("");
                                        txt_fordes.setValue("");
                                        txt_forid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Condición de Compra ").append(objCondicion.getConDes()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_forid.setValue(objCondicion.getConId());
                    txt_fordes.setValue(objCondicion.getConDes());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_glosa")
    public void onOKGlosa() throws SQLException, ParseException {
        if (s_estado.equals("N")) {
            botonNuevoDetalle();
        }
    }

    @Listen("onOK=#cb_alm_origen")
    public void onOKAlmOrigen() {
        if (cb_alm_origen.getSelectedIndex() == -1) {
            cb_alm_origen.focus();
        } else {
            cb_alm_destino.focus();
            cb_alm_destino.select();
        }
    }

    @Listen("onOK=#cb_alm_destino")
    public void onOKAlmDestino() {
        if (cb_alm_destino.getSelectedIndex() == -1) {
            cb_alm_destino.focus();
        } else {
            txt_glosa.focus();
        }
    }

    @Listen("onSelect=#cb_alm_destino;onBlur=#cb_alm_destino")
    public void onSelectAlmDestino() {
        txt_lugent.setValue(lst_Almacenes_Destino.size() <= 0 ? "" : ((Almacenes) lst_Almacenes_Destino.get(0)).getAlm_direcc());
    }

    @Listen("onOK=#txt_canent")
    public void onOKCantidad() throws SQLException {
        if (txt_canent.getValue() == null) {
            Messagebox.show("Por favor ingresar una cantidad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    txt_canent.setValue(null);
                    txt_canent.focus();
                }
            });
        } else {
            txt_pordes.setValue(0);
            txt_pordes.focus();
        }
    }

    @Listen("onBlur=#txt_canent")
    public void onBlurCantidad() throws SQLException {
        if (txt_canent.getValue() != null) {
            if (!txt_proid.getValue().isEmpty()) {
                if (txt_canent.getValue().doubleValue() <= 0.0) {
                    Messagebox.show("Debe ingresar una cantidad mayor a cero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            txt_canent.setValue(null);
                            txt_valafe.setValue(null);
                            txt_valexo.setValue(null);
                            txt_valimp.setValue(null);
                            txt_valdes.setValue(null);
                            txt_pordes.setValue(null);
                            txt_preven.setValue(null);
                            txt_canent.focus();
                        }
                    });
                } else {
                    objProductos = (new DaoProductos()).listaPro(txt_proid.getValue());
                    if (objProductos == null) {
                        Messagebox.show("Producto incorrecto");
                    } else {
                        String s_prodinafecto = new DaoPedCom().obtieneProductoConDimp(txt_proid.getValue());
                        if (s_prodinafecto.equals("I")) {
                            txt_valafe.setValue(txt_canent.getValue() * txt_precom.getValue());
                            txt_valexo.setValue(0.0);
                        } else if (s_prodinafecto.equals("E")) {
                            txt_valafe.setValue(0.0);
                            txt_valexo.setValue(txt_canent.getValue() * txt_precom.getValue());
                        } else {
                            txt_valafe.setValue(txt_canent.getValue() * txt_precom.getValue());
                            txt_valexo.setValue(0.0);
                        }
                        if (txt_pordes.getValue() != null) {
                            CalculosAEI();
                        }
                    }
                }
            } else {
                Messagebox.show("Por favor ingrese un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalle();
                            txt_proid.focus();
                        }
                    }
                });
            }
        } else {
            txt_valafe.setValue(null);
            txt_valexo.setValue(null);
            txt_valimp.setValue(null);
            txt_valdes.setValue(null);
            txt_pordes.setValue(null);
            txt_preven.setValue(null);
        }
    }

    @Listen("onBlur=#d_fecemi")
    public void onBlurFecEmi() throws SQLException {
        if (d_fecemi.getValue() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(d_fecemi.getValue());
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                Messagebox.show("No se puede haber emision los domingos, cambie 'F.EMISION'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        d_fecemi.focus();
                    }
                });
            } else {
                objCfgInicial = objDaoCfgInicial.fecCaduEmpSuc(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                sumaFecha(d_fecrec, d_fecemi.getValue(), 2);
                sumaFecha(d_feccad, d_fecrec.getValue(), objCfgInicial.getTcfg_diacad());
            }
        }
    }

    @Listen("onOK=#txt_pordes")
    public void onOKdescuento() throws SQLException, ParseException {
        if (txt_pordes.getValue() == null) {
            Messagebox.show("Por favor ingresar un porcentaje", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    txt_pordes.setValue(null);
                    txt_pordes.focus();
                }
            });
        } else {
            txt_pedubi.focus();

        }
    }

    @Listen("onBlur=#txt_pordes")
    public void onBlurdescuento() throws SQLException, ParseException {
        if (txt_pordes.getValue() != null) {
            if (!txt_proid.getValue().isEmpty()) {
                if (txt_canent.getValue() != null) {
                    if (txt_pordes.getValue().doubleValue() < 0.0) {
                        s_mensaje = "Debe ingresar un valor mayor o igual a cero";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_pordes.setValue(null);
                                    txt_valdes.setValue(null);
                                    txt_valimp.setValue(null);
                                    txt_preven.setValue(null);
                                    txt_pordes.focus();
                                }
                            }
                        });
                    } else if (txt_pordes.getValue() >= 100) {
                        s_mensaje = "El descuento tiene que ser menor que 100";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_pordes.setValue(null);
                                    txt_pordes.focus();
                                }
                            }
                        });
                    } else {
                        CalculosAEI();
                        txt_pedubi.focus();
                    }
                } else {
                    Messagebox.show("Debe ingresar una cantidad", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_pordes.setValue(null);
                                        txt_canent.focus();
                                    }
                                }
                            });
                }
            }
        } else {
            txt_valdes.setValue(null);
            txt_valimp.setValue(null);
            txt_preven.setValue(null);
        }
    }

    @Listen("onOK=#txt_pedcomglo")
    public void onBlurOKGlosaDet() throws SQLException {
        botonGuardarDetalle();
    }

    @Listen("onCtrlKey=#w_gennotcam")
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
                    botonDeshacer();
                }
                break;

            case 78:
                if (!tbbtn_btn_nuevopc.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarpc.isDisabled()) {
                    botonEditarDetalle();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarpc.isDisabled()) {
                    botonEliminarDetalle();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarpc.isDisabled()) {
                    botonGuardarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacerpc.isDisabled()) {
                    botonDeshacerDetalle();
                }
                break;
        }
    }

    //Eventos Otros 
    public String verificar() {
        String s_valor = "";
        if (d_fecemi.getValue() == null) {
            s_valor = "El campo 'Fecha de emisión' es obligatorio";
            campo = "Fecha de Emisión";
        } else if (d_fecrec.getValue() == null) {
            s_valor = "El campo 'Fecha de recepción' es obligatorio";
            campo = "Fecha de Recepción";
        } else if (d_feccad.getValue() == null) {
            s_valor = "El campo 'Fecha de caducidad' es obligatorio";
            campo = "Fecha de Caducidad";
        } else if (txt_proidman.getValue().isEmpty()) {
            s_valor = "El campo 'Proveedor' es obligatorio";
            campo = "Proveedor";
        } else if (cb_monpedcom.getSelectedIndex() == -1) {
            s_valor = "El campo 'Moneda' es obligatorio";
            campo = "Moneda";
        } else if (db_tipcam.getValue() == null || db_tipcam.getValue() <= 0) {
            s_valor = "El campo 'Tipo de Cambio' es obligatorio";
            campo = "Tipo de Cambio";
        } else if (txt_lisprecom.getValue().isEmpty()) {
            s_valor = "El campo 'Lista de Precios' es obligatorio";
            campo = "Lista de Precios";
        } else if (txt_forid.getValue().isEmpty()) {
            s_valor = "El campo 'Condicion de compra' es obligatorio";
            campo = "Condicion de Compra";
        } else if (cb_alm_origen.getValue().isEmpty()) {
            s_valor = "El campo 'Almacen de origen' es obligatorio";
            campo = "Almacen de Origen";
        } else if (cb_alm_destino.getValue().isEmpty()) {
            s_valor = "El campo 'Almacen de destino' es obligatorio";
            campo = "Almacen de Destino";
        }
        return s_valor;
    }

    public String verificarFechas() {
        String s_valor = "";
        //se tiene que validar que la fecha de emision sea del mismo periodo
        String periodo = cb_cabecera_periodo.getValue();
        String fecha_emisionm = sdfm.format(d_fecemi.getValue());
        String fecha_emision = sdf.format(d_fecemi.getValue());
        String fecha_recepcion = sdf.format(d_fecrec.getValue());
        if (!fecha_emisionm.equals(periodo)) {
            s_valor = "La fecha de emisión debe encontrarse en el periodo : " + periodo;
        } else {
            if (d_fecrec.getValue().getTime() <= d_fecemi.getValue().getTime()) {
                s_valor = "La fecha de recepción debe ser mayor que : " + fecha_emision;
            } else if (d_feccad.getValue().getTime() <= d_fecemi.getValue().getTime()) {
                s_valor = "La fecha de caducidad debe ser mayor que : " + fecha_emision;
            } else if (d_fecemi.getValue().getTime() >= d_fecrec.getValue().getTime()) {
                s_valor = "La fecha de recepción debe ser mayor que : " + fecha_emision;
            } else if (d_fecemi.getValue().getTime() >= d_feccad.getValue().getTime()) {
                s_valor = "La fecha de caducidad debe ser mayor que : " + fecha_emision;
            } else if (d_fecrec.getValue().getTime() >= d_feccad.getValue().getTime()) {
                s_valor = "La fecha de caducidad debe ser mayor que : " + fecha_recepcion;
            }
        }
        return s_valor;
    }

    public Object generaRegistro() {
        objPedidoCompraCab = new PedidoCompraCab();
        if (s_estado.equals("N")) {
            objPedidoCompraCab.setEmp_id(objUsuCredential.getCodemp());
            objPedidoCompraCab.setSuc_id(objUsuCredential.getCodsuc());
            objPedidoCompraCab.setPer_id(cb_cabecera_periodo.getValue());
            objPedidoCompraCab.setPer_key(Integer.parseInt((String) cb_cabecera_periodo.getSelectedItem().getValue()));
            objPedidoCompraCab.setPedcom_fecemi(d_fecemi.getValue());
            objPedidoCompraCab.setPedcom_fecrec(d_fecrec.getValue());
            objPedidoCompraCab.setPedcom_feccad(d_feccad.getValue());
            objPedidoCompraCab.setPedcom_est(1);
            objPedidoCompraCab.setProv_id(txt_proidman.getValue());
            objPedidoCompraCab.setProv_key(Integer.parseInt(txt_proidman.getValue()));
            int x = ((Integer) cb_monpedcom.getSelectedItem().getValue()).intValue();
            objPedidoCompraCab.setPedcom_mon(x);
            objPedidoCompraCab.setPedcom_tipcam(db_tipcam.getValue().doubleValue());
            objPedidoCompraCab.setPedcom_lispre(txt_lisprecom.getValue());
            objPedidoCompraCab.setCon_key(Integer.parseInt(txt_forid.getValue()));
            objPedidoCompraCab.setCon_tipo("C");
            objPedidoCompraCab.setPedcom_sit(1);
            objPedidoCompraCab.setPedcom_almori((String) cb_alm_origen.getSelectedItem().getValue());
            objPedidoCompraCab.setPedcom_almdes((String) cb_alm_destino.getSelectedItem().getValue());
            objPedidoCompraCab.setPedcom_glo(txt_glosa.getValue());
        } else {
            objPedidoCompraCab.setPedcom_id(txt_nropedcom.getValue());
            objPedidoCompraCab.setPedcom_key(Integer.parseInt(txt_nropedcom.getValue()));
            objPedidoCompraCab.setEmp_id(objUsuCredential.getCodemp());
            objPedidoCompraCab.setSuc_id(objUsuCredential.getCodsuc());
            objPedidoCompraCab.setPer_id(cb_cabecera_periodo.getValue());
            objPedidoCompraCab.setPer_key(Integer.parseInt((String) cb_cabecera_periodo.getSelectedItem().getValue()));
            objPedidoCompraCab.setPedcom_fecemi(d_fecemi.getValue());
            objPedidoCompraCab.setPedcom_fecrec(d_fecrec.getValue());
            objPedidoCompraCab.setPedcom_feccad(d_feccad.getValue());
            objPedidoCompraCab.setPedcom_est(1);
            objPedidoCompraCab.setProv_id(txt_proidman.getValue());
            objPedidoCompraCab.setProv_key(Integer.parseInt(txt_proidman.getValue()));
            int x = ((Integer) cb_monpedcom.getSelectedItem().getValue()).intValue();
            objPedidoCompraCab.setPedcom_mon(x);
            objPedidoCompraCab.setPedcom_tipcam(db_tipcam.getValue().doubleValue());
            objPedidoCompraCab.setPedcom_lispre(txt_lisprecom.getValue());
            objPedidoCompraCab.setCon_key(Integer.parseInt(txt_forid.getValue()));
            objPedidoCompraCab.setCon_tipo("C");
            objPedidoCompraCab.setPedcom_sit(1);
            objPedidoCompraCab.setPedcom_almori((String) cb_alm_origen.getSelectedItem().getValue());
            objPedidoCompraCab.setPedcom_almdes((String) cb_alm_destino.getSelectedItem().getValue());
            objPedidoCompraCab.setPedcom_glo(txt_glosa.getValue());
        }
        return objPedidoCompraCab;
    }

    public Object generaDetalle() {
        PedidoCompraDet objPedidoCompraDetalle = new PedidoCompraDet();
        objPedidoCompraDetalle.setPedcomdet_key(txt_pedcomdet_key.getValue() == null ? 0 : txt_pedcomdet_key.getValue());
        objPedidoCompraDetalle.setPedcom_key(txt_nropedcom.getValue().isEmpty() ? 0 : Long.parseLong(txt_nropedcom.getValue()));
        objPedidoCompraDetalle.setPedcom_id(txt_nropedcom.getValue());
        objPedidoCompraDetalle.setPer_id(cb_cabecera_periodo.getValue());
        objPedidoCompraDetalle.setPro_id(txt_proid.getValue());
        objPedidoCompraDetalle.setPro_des(objProductos == null ? txt_prodes.getValue() : objProductos.getPro_des());
        objPedidoCompraDetalle.setPro_desdes(objProductos == null ? txt_prodes.getValue() : objProductos.getPro_desdes());
        objPedidoCompraDetalle.setPedcom_canped(txt_canent.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_scanped(txt_canent.getValue().doubleValue());
        objPedidoCompraDetalle.setPro_unimanven(txt_prounimanven.getValue());
        objPedidoCompraDetalle.setPro_pesouni(txt_pesounit.getValue());
        objPedidoCompraDetalle.setPro_peso_unidaduni(txt_peso_und.getValue());
        objPedidoCompraDetalle.setPro_spesouni(txt_pesounit.getValue());
        objPedidoCompraDetalle.setPro_pesouni(txt_pesounit.getValue());
        objPedidoCompraDetalle.setPro_pesotot(txt_peso_und.getValue().equals("GRM") || txt_peso_und.getValue().equals("MLL") ? (txt_pesounit.getValue() * txt_canent.getValue()) / 1000 : (txt_pesounit.getValue() * txt_canent.getValue()));
        if (txt_peso_und.getValue().equals("GRM")) {
            objPedidoCompraDetalle.setPro_peso_unidadtot("KLG");
        } else if (txt_peso_und.getValue().equals("MLL")) {
            objPedidoCompraDetalle.setPro_peso_unidadtot("LTS");
        } else {
            objPedidoCompraDetalle.setPro_peso_unidadtot(txt_peso_und.getValue());
        }
        objPedidoCompraDetalle.setPro_spesotot(txt_peso_und.getValue().equals("GRM") || txt_peso_und.getValue().equals("MLL") ? (txt_pesounit.getValue() * txt_canent.getValue()) / 1000 : (txt_pesounit.getValue() * txt_canent.getValue()));
        objPedidoCompraDetalle.setPro_voluni(txt_voluunit.getValue());
        objPedidoCompraDetalle.setPro_vol_unidaduni(txt_vol_und.getValue());
        objPedidoCompraDetalle.setPro_svoluni(txt_voluunit.getValue());
        objPedidoCompraDetalle.setPro_voltot((txt_voluunit.getValue() * txt_canent.getValue()));
        objPedidoCompraDetalle.setPro_vol_unidadtot("MT3");
        objPedidoCompraDetalle.setPro_svoltot((txt_voluunit.getValue() * txt_canent.getValue()));
        objPedidoCompraDetalle.setPedcom_preuni(txt_precom.getValue());
        objPedidoCompraDetalle.setPedcom_valafe(txt_valafe.getValue());
        objPedidoCompraDetalle.setPedcom_svalafe(txt_valafe.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_valexo(txt_valexo.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_svalexo(txt_valexo.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_pordes(txt_pordes.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_valdes(txt_valdes.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_svaldes(txt_valdes.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_porimp(txt_porimp.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_valimp(txt_valimp.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_svalimp(txt_valimp.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_valtot(txt_preven.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_svaltot(txt_preven.getValue().doubleValue());
        objPedidoCompraDetalle.setPedcom_glo(txt_pedcomglo.getValue());
        objPedidoCompraDetalle.setPedcom_usuadd(objUsuCredential.getCuenta());
        objPedidoCompraDetalle.setPedcom_usumod(objUsuCredential.getCuenta());
        objPedidoCompraDetalle.setEmp_id(emp_id);
        objPedidoCompraDetalle.setSuc_id(suc_id);
        objPedidoCompraDetalle.setPedcom_ubi(Utilitarios.lpad(txt_pedubi.getValue(), 4, "0"));
        objPedidoCompraDetalle.setPedcom_ubides(txt_peddes.getValue());
        return objPedidoCompraDetalle;
    }

    public Object[][] getPedidoCompraDet(ListModelList<PedidoCompraDet> objListaAuxiliar) {
        ListModelList<PedidoCompraDet> objListaDepurada;
        //Si el estado de la cabecera esta en modo Editar = E
        if (s_estado.equals("M")) {
            objListaDepurada = new ListModelList<PedidoCompraDet>();
            //Limpiar registros eliminados del detalle
            for (int x = 0; x < objListaAuxiliar.size(); x++) {
                //Si el detalle es diferente del estado Q = QUERY - consulta
                if (!objListaAuxiliar.get(x).getInd_accion().equals("Q")) {
                    objListaDepurada.add(objListaAuxiliar.get(x));
                }
            }
        } else {
            objListaDepurada = objListaAuxiliar;
        }
        //Creamos el arreglo con los detalles
        Object[][] listaPedidoCompraDet = new Object[objListaDepurada.size()][26];
        //Barreros la lista que contiene los datos a grabar
        for (int i = 0; i < objListaDepurada.size(); i++) {
            //Llenamos la lista con los datos
            listaPedidoCompraDet[i][0] = objListaDepurada.get(i).getPedcom_key();
            listaPedidoCompraDet[i][1] = objListaDepurada.get(i).getEmp_id();
            listaPedidoCompraDet[i][2] = objListaDepurada.get(i).getSuc_id();
            listaPedidoCompraDet[i][3] = objListaDepurada.get(i).getPro_id();
            //listaPedidoCompraDet[i][4] = objListaDepurada.get(i).getPro_key();
            listaPedidoCompraDet[i][4] = objListaDepurada.get(i).getPedcomdet_key();
            listaPedidoCompraDet[i][5] = objListaDepurada.get(i).getPer_id();
            listaPedidoCompraDet[i][6] = objListaDepurada.get(i).getPedcom_est();
            listaPedidoCompraDet[i][7] = objListaDepurada.get(i).getPedcom_canped();
            //listaPedidoCompraDet[i][9] = objListaDepurada.get(i).getPedcom_canrec();//peso
            listaPedidoCompraDet[i][8] = objListaDepurada.get(i).getPro_pesotot();//peso
            listaPedidoCompraDet[i][9] = objListaDepurada.get(i).getPedcom_preuni();
            listaPedidoCompraDet[i][10] = objListaDepurada.get(i).getPedcom_valafe();
            listaPedidoCompraDet[i][11] = objListaDepurada.get(i).getPedcom_valexo();
            listaPedidoCompraDet[i][12] = objListaDepurada.get(i).getPedcom_pordes();
            listaPedidoCompraDet[i][13] = objListaDepurada.get(i).getPedcom_valdes();
            listaPedidoCompraDet[i][14] = objListaDepurada.get(i).getPedcom_valimp();
            listaPedidoCompraDet[i][15] = objListaDepurada.get(i).getPedcom_valtot();
            listaPedidoCompraDet[i][16] = objListaDepurada.get(i).getPedcom_glo();
            listaPedidoCompraDet[i][17] = objListaDepurada.get(i).getPedcom_genord();
            listaPedidoCompraDet[i][18] = objListaDepurada.get(i).getPedcom_canbon();
            listaPedidoCompraDet[i][19] = objListaDepurada.get(i).getPedcom_usuadd();
            listaPedidoCompraDet[i][20] = objListaDepurada.get(i).getPedcom_fecadd();
            listaPedidoCompraDet[i][21] = objListaDepurada.get(i).getPedcom_usumod();
            listaPedidoCompraDet[i][22] = objListaDepurada.get(i).getPedcom_fecmod();
            listaPedidoCompraDet[i][23] = objListaDepurada.get(i).getPedcom_porimp();
            listaPedidoCompraDet[i][24] = objListaDepurada.get(i).getInd_accion();
            listaPedidoCompraDet[i][25] = objListaDepurada.get(i).getPedcom_ubi();

        }
        return listaPedidoCompraDet;
    }

    public String verificarDetalle() {
        String s_valor = "";
        if (txt_proid.getValue().isEmpty()) {
            s_valor = "'Código de producto'";
            foco = "codigo";
        } else if (txt_canent.getValue() == null) {
            s_valor = "'Cantidad'";
            foco = "cantidad1";
        } else if (txt_canent.getValue() <= 0) {
            s_valor = "'Cantidad' - Debe ser mayor a 0";
            foco = "cantidad2";
        } else if (txt_pordes.getValue() == null) {
            s_valor = "'Descuento'";
            foco = "descuento1";
        } else if (txt_pordes.getValue() < 0) {
            s_valor = "'Descuento' - Debe ser mayor o igual a 0";
            foco = "descuento2";
        } else if (txt_pordes.getValue() >= 100) {
            s_valor = "'Descuento' - Debe ser menor a 100";
            foco = "descuento3";
        } else if (txt_pedubi.getValue().isEmpty()) {
            s_valor = "'Ubicacion' ";
            foco = "ubicacion";
        }
        return s_valor;
    }

    public boolean existeProducto(String cod_producto) {
        boolean ban = true;
        for (int i = 0; i < objListaPedidoCompraDet.size(); i++) {
            if (objListaPedidoCompraDet.get(i).getPro_id().equals(cod_producto)) {
                ban = false;
            }
        }
        return ban;
    }

    public double[] calcularTotales() {
        double totales[] = new double[8];
        for (int i = 0; i < objListaPedidoCompraDet.getSize(); i++) {
            totales[0] = totales[0] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_valafe();
            totales[1] = totales[1] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_valexo();
            totales[2] = totales[2] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_pordes();
            totales[3] = totales[3] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_valdes();
            totales[4] = totales[4] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_valimp();
            totales[5] = totales[5] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_valtot();
            totales[6] = totales[6] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPro_pesotot();
            totales[7] = totales[7] + ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPro_voltot();
        }
        return totales;
    }

    public void CalculosAEI() throws SQLException {
        String s_prodinafecto = new DaoPedCom().obtieneProductoConDimp(txt_proid.getValue());
        if (s_prodinafecto.equals("I")) {
            txt_valdes.setValue(txt_valafe.getValue() * (txt_pordes.getValue() / 100));
            txt_valimp.setValue(0.0);
            txt_preven.setValue((txt_valafe.getValue() - txt_valdes.getValue()) + txt_valimp.getValue());
        } else if (s_prodinafecto.equals("E")) {
            txt_valdes.setValue(txt_valexo.getValue() * (txt_pordes.getValue() / 100));
            txt_valimp.setValue(0.0);
            txt_preven.setValue((txt_valexo.getValue() - txt_valdes.getValue()) + txt_valimp.getValue());
        } else {
            txt_valdes.setValue(txt_valafe.getValue() * (txt_pordes.getValue() / 100));
            txt_valimp.setValue((txt_valafe.getValue() - txt_valdes.getValue()) * (txt_porimp.getValue() / 100));
            txt_preven.setValue((txt_valafe.getValue() - txt_valdes.getValue()) + txt_valimp.getValue());
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaPedCom.setSelected(b_valida1);
        tab_mantenimientoPedCom.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaPedCom.setDisabled(b_valida1);
        tab_mantenimientoPedCom.setDisabled(b_valida2);
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
        tbbtn_btn_nuevopc.setDisabled(b_valida1);
        tbbtn_btn_editarpc.setDisabled(b_valida1);
        tbbtn_btn_eliminarpc.setDisabled(b_valida1);
        tbbtn_btn_deshacerpc.setDisabled(b_valida2);
        tbbtn_btn_guardarpc.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_nropedcom.setValue("");
        txt_est.setValue("ACTIVO");
        txt_proidman.setValue("");
        txt_prodesman.setValue("");
        cb_monpedcom.setValue(null);
        db_tipcam.setValue(null);
        txt_lisprecom.setValue("");
        txt_lisprecomdes.setValue("");
        txt_forid.setValue("");
        txt_fordes.setValue("");
        txt_situacion.setValue("INGRESADO");
        cb_alm_origen.setValue(null);
        cb_alm_destino.setValue(null);
        txt_glosa.setValue("");
        txt_lugent.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_pedcomdet_key.setValue(null);
        txt_proid.setValue("");
        txt_prodes.setValue("");
        txt_canent.setValue(null);
        txt_pesounit.setValue(null);
        txt_peso_und.setValue("");
        txt_voluunit.setValue(null);
        txt_vol_und.setValue("");
        txt_precom.setValue(null);
        txt_valafe.setValue(null);
        txt_valexo.setValue(null);
        txt_pordes.setValue(null);
        txt_valdes.setValue(null);
        txt_porimp.setValue(null);
        txt_valimp.setValue(null);
        txt_preven.setValue(null);
        txt_prounimanven.setValue("");
        txt_pedcomglo.setValue("");
        txt_vol_und.setValue("");
        txt_voluunit.setValue(null);
    }

    public void limpiarCamposTotales() {
        txt_totvalafe.setValue(null);
        txt_totvalexo.setValue(null);
        txt_totpordes.setValue(null);
        txt_totvaldes.setValue(null);
        txt_totvalimp.setValue(null);
        txt_totpreven.setValue(null);
        txt_totpeso.setValue(null);
        txt_totvolumen.setValue(null);
    }

    public void validaProductosxLpc() {
        s_mensaje = "Usted ha cambiado la lista precio de compra para este pedido, esto puede afectar a algunos productos, desea realizar los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", 3, Messagebox.QUESTION, new EventListener() {
            public void onEvent(Event event)
                    throws Exception {
                if (((Integer) event.getData()).intValue() == 1) {
                    for (int i = objListaPedidoCompraDet.size() - 1; i >= 0; i--) {
                        String pro_id = ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPro_id();
                        long prov_key = Long.parseLong(txt_proidman.getValue());
                        int lpc_key = Integer.parseInt(txt_lisprecom.getValue());
                        objPrecioCompra = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, pro_id, prov_key, lpc_key);
                        if (objPrecioCompra == null) {
                            if (!((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getInd_accion().equals("N") || !((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getInd_accion().equals("NM")) {
                                int ocd_item = ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcomdet_key();
                                String oc_ind = "C";
                                long oc_nropedkey = Long.parseLong(txt_nropedcom.getValue());
                                String ocd_usumod = objUsuCredential.getCuenta();
                                objListaEliminacionPedidoCompraDet.add(new PedidoCompraDet(oc_nropedkey, emp_id, suc_id, oc_ind, ocd_item, ocd_usumod));
                            }
                            objListaPedidoCompraDet.set(i, null);
                            continue;
                        } else {
                            Messagebox.show("El producto no tiene de precio de compra");
                        }
                        txt_proidman.setValue(((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPro_id());
                        txt_canent.setValue(((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_canped());
                        txt_pordes.setValue(((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_pordes());
                        txt_glosa.setValue(((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcom_glo());
                        String indicador = ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getInd_accion();
                        String ocd_conImp = objPrecioCompra.getPro_condimp();
                        if (indicador.equals("N") || indicador.equals("NM")) {
                            if (ocd_conImp.equals("A")) {
                                objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                            } else {
                                objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                            }
                            objPedidoCompraDet.setInd_accion("NM");
                        } else {
                            int item = ((PedidoCompraDet) objListaPedidoCompraDet.get(i)).getPedcomdet_key();
                            if (ocd_conImp.equals("A")) {
                                objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                            } else {
                                objPedidoCompraDet = (PedidoCompraDet) generaDetalle();
                            }
                            objPedidoCompraDet.setInd_accion("M");
                            objPedidoCompraDet.setPedcomdet_key(item);
                        }
                        objListaPedidoCompraDet.set(i, objPedidoCompraDet);
                    }
                    for (int i = objListaPedidoCompraDet.size() - 1; i >= 0; i--) {
                        if (objListaPedidoCompraDet.get(i) == null) {
                            objListaPedidoCompraDet.remove(i);
                        }
                    }
                    txt_proidman.setValue("");
                    txt_canent.setValue(null);
                    txt_pordes.setValue(null);
                    txt_glosa.setValue("");
                    calcularTotales();
                    objPedidoCompraCab.setPedcom_lispre(txt_lisprecom.getValue());
                    Messagebox.show("Se ha realizado el cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    txt_lisprecom.setValue(Utilitarios.lpad(String.valueOf(objPedidoCompraCab.getPedcom_lispre()), 3, "0"));
                    txt_lisprecomdes.setValue(objPedidoCompraCab.getPedcom_lispredes());
                }
            }
        }
        );
    }

    public void llenarCampos(PedidoCompraCab objPedidoCompraCab) {
        txt_nropedcom.setValue(objPedidoCompraCab.getPedcom_id());
        cb_cabecera_periodo.setValue(objPedidoCompraCab.getPer_id());
        d_fecemi.setValue(objPedidoCompraCab.getPedcom_fecemi());
        d_fecrec.setValue(objPedidoCompraCab.getPedcom_fecrec());
        d_feccad.setValue(objPedidoCompraCab.getPedcom_feccad());
        txt_est.setValue(objPedidoCompraCab.getPedcom_estdes());
        txt_proidman.setValue(objPedidoCompraCab.getProv_id());
        txt_prodesman.setValue(objPedidoCompraCab.getProv_razsoc());
        cb_monpedcom.setValue(objPedidoCompraCab.getPedcom_mondes());
        db_tipcam.setValue(objPedidoCompraCab.getPedcom_tipcam());
        txt_lisprecom.setValue(objPedidoCompraCab.getPedcom_lispre());
        txt_lisprecomdes.setValue(objPedidoCompraCab.getPedcom_lispredes());
        txt_forid.setValue(Utilitarios.lpad("" + objPedidoCompraCab.getCon_key(), 3, "0"));
        txt_fordes.setValue(objPedidoCompraCab.getCon_des());
        txt_situacion.setValue(objPedidoCompraCab.getPedcom_sitdes());
        cb_alm_origen.setValue(objPedidoCompraCab.getPedcom_almorides());
        cb_alm_destino.setValue(objPedidoCompraCab.getPedcom_almdesdes());
        txt_glosa.setValue(objPedidoCompraCab.getPedcom_glo());
        txt_usuadd.setValue(objPedidoCompraCab.getPedcom_usuadd());
        d_fecadd.setValue(objPedidoCompraCab.getPedcom_fecadd());
        txt_usumod.setValue(objPedidoCompraCab.getPedcom_usumod());
        d_fecmod.setValue(objPedidoCompraCab.getPedcom_fecmod());
        txt_lugent.setValue(objPedidoCompraCab.getPedcom_lugent());
    }

    public void llenarCamposDetalle() throws SQLException {
        objListaPedidoCompraDet = null;
        Long nro_pedcom = objPedidoCompraCab.getPedcom_key();
        objListaPedidoCompraDet = objDaoGenPedCom.listaPedidoCompraDet(nro_pedcom);
        lst_pedidocompra_detalle.setModel(objListaPedidoCompraDet);
        double data[];
        data = calcularTotales();
        txt_totvalafe.setValue(data[0]);
        txt_totvalexo.setValue(data[1]);
        txt_totpordes.setValue(data[2]);
        txt_totvaldes.setValue(data[3]);
        txt_totvalimp.setValue(data[4]);
        txt_totpreven.setValue(data[5]);
        txt_totpeso.setValue(data[6]);
        txt_totvolumen.setValue(data[7]);
    }

    public void llenarCamposProducto() throws SQLException {
        objProductos = (new DaoProductos()).listaPro(objPedidoCompraDet.getPro_id());
        txt_pedcomdet_key.setValue(objPedidoCompraDet.getPedcomdet_key());
        txt_proid.setValue(objPedidoCompraDet.getPro_id());
        txt_prodes.setValue(objPedidoCompraDet.getPro_des());
        txt_canent.setValue(objPedidoCompraDet.getPedcom_canped());
        txt_pesounit.setValue(objProductos.getPro_presminven() * objProductos.getPro_peso());
        txt_peso_und.setValue(objProductos.getPro_unipeso());
        txt_voluunit.setValue(objProductos.getPro_presminven() * objProductos.getPro_vol());
        txt_vol_und.setValue("MT3");
        txt_prounimanven.setValue(objPedidoCompraDet.getPro_unimanven());
        txt_precom.setValue(objPedidoCompraDet.getPedcom_preuni());
        txt_valafe.setValue(objPedidoCompraDet.getPedcom_valafe());
        txt_valexo.setValue(objPedidoCompraDet.getPedcom_valexo());
        txt_pordes.setValue(objPedidoCompraDet.getPedcom_pordes());
        txt_valdes.setValue(objPedidoCompraDet.getPedcom_valdes());
        txt_porimp.setValue(objPedidoCompraDet.getPedcom_porimp());
        txt_valimp.setValue(objPedidoCompraDet.getPedcom_valimp());
        txt_preven.setValue(objPedidoCompraDet.getPedcom_valtot());
        txt_pedcomglo.setValue(objPedidoCompraDet.getPedcom_glo());
        txt_pedubi.setValue(objPedidoCompraDet.getPedcom_ubi());
        txt_peddes.setValue(objPedidoCompraDet.getPedcom_ubides());
    }

    public void habilitaCampos(boolean b_valida1, boolean b_valida2) {
        cb_cabecera_periodo.setDisabled(b_valida1);
        d_fecemi.setDisabled(b_valida1);
        d_fecrec.setDisabled(b_valida1);
        d_feccad.setDisabled(b_valida1);
        txt_proidman.setDisabled(b_valida2);
        cb_monpedcom.setDisabled(b_valida1);
        txt_lisprecom.setDisabled(b_valida1);
        txt_forid.setDisabled(b_valida1);
        cb_alm_origen.setDisabled(b_valida1);
        cb_alm_destino.setDisabled(b_valida1);
        txt_glosa.setDisabled(b_valida1);
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        if (s_estado_detalle.equals("N")) {
            txt_proid.setDisabled(b_valida1);
        } else {
            txt_proid.setDisabled(true);
        }
        txt_canent.setDisabled(b_valida1);
        txt_pordes.setDisabled(b_valida1);
        txt_pedcomglo.setDisabled(b_valida1);
        txt_pedubi.setDisabled(b_valida1);
    }

    public void limpiarLista() {
        //limpio mi lista
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        lst_pedidocompra.setModel(objListaPedidoCompraCab);
    }

    public Calendar DateCalendar(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void sumaFecha(Datebox datebox, Date date, int numdias) {
        Calendar c = DateCalendar(Utilitarios.sumaDias(date, numdias));
        int nrodia = c.get(Calendar.DAY_OF_WEEK);
        if (nrodia == 1) {
            datebox.setValue(Utilitarios.sumaDias(date, numdias + 1));
        } else {
            datebox.setValue(Utilitarios.sumaDias(date, numdias));
        }
    }

    public void infoUbicacion() throws SQLException {
        String codUbi = txt_pedubi.getValue(), msj;
        double cantvol = txt_voluunit.getValue() * txt_canent.getValue();
        double voltot = txt_totvolumen.getValue() == null ? cantvol : txt_totvolumen.getValue();
        double voldisp = 0, volubi;
        int almacen = Integer.parseInt(cb_alm_destino.getSelectedItem().getValue().toString());
        objParametrosSalida = objDaoProductos.stockProducto(Utilitarios.periodoActualxFecha(d_fecemi.getValue()), almacen, objProductos.getPro_id(), String.valueOf(Integer.parseInt(codUbi)));
        objProductos = objDaoProductos.infoVolTotProUbi(codUbi, almacen);
        if (objProductos == null) {
        } else {
            volubi = objProductos.getVolubi();
            if (s_estado_detalle.equals("N")) {
                voldisp = objListaPedidoCompraDet.isEmpty() ? volubi - voltot : volubi - (voltot + cantvol);
            } else {
                boolean salida = true;
                double data[] = calcularTotales(), nv;
                int x = 0;
                while (x < objListaPedidoCompraDet.size() && salida) {
                    if (objListaPedidoCompraDet.get(x).getPro_id().equals(txt_proid.getValue())) {
                        nv = data[7] - objListaPedidoCompraDet.get(x).getPro_voltot();
                        voldisp = volubi - (nv + cantvol);
                        salida = false;
                    }
                    x++;
                }
            }
            Productos objProducto = (new DaoProductos()).listaPro(txt_proid.getValue());
            int stocks = objParametrosSalida.getCantStocks() / objProducto.getPro_presminven();

            msj = voldisp < 0 ? "LLeno \n\n Ha sobrepasado el limite de la capacidad de la ubicacion del producto (" + objProductos.getVolubi() + " MT3)" : String.valueOf(voldisp) + " MT3";
            Messagebox.show("Cantidad de stock : " + stocks + "\n Volumen Disponible: " + msj, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

        }
    }

    //metodos sin utilizar
    public void llenaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
