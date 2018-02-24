package org.me.gj.controller.facturacion.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import org.apache.commons.collections.map.HashedMap;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.cxc.mantenimiento.DaoDirecciones;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.cxc.mantenimiento.DaoTipoCambio;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoMesa;
import org.me.gj.controller.facturacion.mantenimiento.DaoMotivoRechazo;
import org.me.gj.controller.facturacion.mantenimiento.DaoSupervisores;
import org.me.gj.controller.facturacion.mantenimiento.DaoTipoVenta;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.facturacion.mantenimiento.DaoZonas;
import org.me.gj.controller.logistica.mantenimiento.DaoCondicion;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.facturacion.mantenimiento.Zona;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.facturacion.procesos.PedidoVentaCab;
import org.me.gj.model.facturacion.procesos.PedidoVentaDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerGenPedVen extends SelectorComposer implements InterfaceControllers {

    //Componentes Web
    @Wire
    Combobox cb_periodo, cb_simbmon, cb_tipoventa, cb_pronpag;
    @Wire
    Datebox d_filtro_fecha, d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Doublebox db_por_lispre, db_por_convta, db_tipcam, txt_total,
            db_por_dsccli, db_por_dscesp, db_por_desman, //----->descuentos
            db_det_preven, db_det_valvta, db_det_valdes, db_det_pordes, db_det_porigv,
            db_det_valigv, db_det_total, db_det_stock, txt_cant, //----->datos de detalle       
            db_sumped, db_sumok, db_sumrec, db_valped, db_valok, db_valrec, db_limcred, db_saldo;
    @Wire
    Textbox txt_filtro_codsuper, txt_filtro_dessuper, txt_filtro_codven, txt_filtro_nomven, //----->tab_lista
            txt_nropedven, txt_situacion, txt_dni, txt_ruc,
            txt_proid, txt_cli_id, txt_cli_razsoc/*, txt_lincred, txt_limdoc, txt_saldo*/, txt_estado, txt_clidir_direcc,
            txt_zon_id, txt_zon_des, txt_giro, txt_desgiro, txt_idvendedor, txt_nomvendedor, txt_diavis,
            txt_id_listapre, txt_des_listapre, txt_id_condven, txt_des_condven, txt_diaplazo, txt_horent/*(oculto)*/,
            txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_usuadd, txt_usumod;

    @Wire
    Tab tab_listaPedVen, tab_mantenimientoPedVen;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro,
            tbbtn_btn_guardarpro, tbbtn_btn_deshacerpro, tbbtn_filtro_buscar, tbbtn_recuperar;
    @Wire
    Listbox lst_pedidoventa, lst_pedven_detalle;
    @Wire
    Button btn_consultarpedven, btn_recped;
    @Wire
    Intbox txt_pedvendet_key, txt_frac, txt_limdoc, txt_facbol/*(oculto)*/;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_ingresado, chk_estado,//----->busqueda
            chk_modtip, chk_desman;//------>mantenimiento
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPeriodos;
    ListModelList<Almacenes> objlstAlmacenesOrigen;
    ListModelList<Almacenes> objlstAlmacenesDestino;
    ListModelList<Moneda> objlstMonedas;
    ListModelList<TipoVenta> objlsTipoventa;
    ListModelList<PedidoVentaCab> objListaPedidoVentaCab, objlstAux;
    ListModelList<PedidoVentaDet> objListaPedidoVentaDet, objListaEliminacion;
    PedidoVentaCab objPedidoVentaCab;
    PedidoVentaDet objPedidoVentaDet;
    ParametrosSalida objParametrosSalida;
    DaoPedVen objDaoGenPedVen;
    DaoAccesos objDaoAccesos;
    DaoCliente objDaoCliente;
    DaoMesa objDaoMesa;
    DaoTipoVenta objDaoTipoVenta;
    DaoMoneda objDaoMoneda;
    DaoManPer objDaoManPer;
    DaoMotivoRechazo objDaoMotivoRechazo;
    DaoCierreDia objDaoCierreDia;
    DaoPrecios objDaoPrecioVen;
    DaoProductos objDaoProducto;
    Accesos objAccesos;
    Productos objProductos;
    Precio objPreciosVenta;
    Cliente objCliente;
    CliFinanciero objCliFin;
    ListaPrecio objLpVenta;
    Condicion objCondicion;

    MotivoRechazo objMotivoRechazo;
    Mesa objMesa = new Mesa();
    Utilitarios objUtil = new Utilitarios();
    //Variables publicas
    int i_sel;
    int i_selCab, i_selDet;
    int emp_id;
    int suc_id;
    int monid;
    String s_estado = "Q";
    String s_estado_detalle = "Q";
    String s_mensaje;
    String s_nroPedidoVenta;
    String modoEjecucion;
    String fechaActual;
    String foco = "";
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdffe = new SimpleDateFormat("EEE");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenPedVen.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoCliente = new DaoCliente();
        objCliente = new Cliente();
        objDaoMesa = new DaoMesa();
        objDaoMotivoRechazo = new DaoMotivoRechazo();
        objDaoTipoVenta = new DaoTipoVenta();
        objDaoMoneda = new DaoMoneda();
        objDaoManPer = new DaoManPer();
        objListaEliminacion = new ListModelList<PedidoVentaDet>();
        objDaoGenPedVen = new DaoPedVen();
        objPedidoVentaCab = new PedidoVentaCab();
        objPedidoVentaDet = new PedidoVentaDet();
        objParametrosSalida = new ParametrosSalida();
        objDaoCierreDia = new DaoCierreDia();
        s_estado = "Q";
        s_estado_detalle = "Q";
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objlstManPeriodos = new ListModelList();
        objlstManPeriodos = objDaoManPer.listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(objlstManPeriodos);
        cb_periodo.setValue(periodo);
        db_tipcam.setValue(1.00);
        objlsTipoventa = new ListModelList();
        objlsTipoventa = objDaoTipoVenta.listaTipoVentas(1);
        cb_tipoventa.setModel(objlsTipoventa);
        objlstMonedas = new ListModelList();
        objlstMonedas = objDaoMoneda.listaMonedas(1);
        cb_simbmon.setModel(objlstMonedas);
        cb_periodo.focus();
        cb_periodo.select();
    }

    @Listen("onCreate=#tabbox_pedventa")
    public void llenaRegistros() throws SQLException {
        /*objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
         objListaPedidoVentaCab = objDaoGenPedVen.listaPedidoVenCab(emp_id, suc_id, "%%", fechaActual, fechaActual, 1);
         lst_pedidoventa.setModel(objListaPedidoVentaCab);*/
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40201000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Generación de Pedido de Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado Procesos Generación de Pedido de Venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un Pedido de Venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un Pedido de Venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Pedido de Venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Pedido de Venta");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Pedido de Venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Pedido de Venta");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Pedidos de Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Pedidos de Venta");
        }
    }

    @Listen("onClick=#btn_consultarpedven")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        Date fecha_emisioni = d_filtro_fecha.getValue();
        String f_emisioni, f_periodo;
        if (fecha_emisioni == null) {
            f_emisioni = "";
            f_periodo = "";
        } else {
            f_emisioni = sdf.format(d_filtro_fecha.getValue());
            f_periodo = sdfm.format(d_filtro_fecha.getValue());
        }
        if (cb_periodo.getValue().equals(f_periodo) || f_emisioni.equals("")) {
            String sup_id = txt_filtro_codsuper.getValue().isEmpty() ? "%%" : txt_filtro_codsuper.getValue();
            String ven_id = txt_filtro_codven.getValue().isEmpty() ? "%%" : txt_filtro_codven.getValue();
            String situacion = chk_ingresado.isChecked() ? "1" : "%%";
            int estado = chk_estado.isChecked() ? 1 : 2;
            String s_periodo = cb_periodo.getValue();

            objListaPedidoVentaCab = new ListModelList();
            objListaPedidoVentaCab = objDaoGenPedVen.busquedaPedidoVenta(emp_id, suc_id, f_emisioni, sup_id, ven_id, situacion, estado, s_periodo);

            //Validar la tabla sin registro
            if (objListaPedidoVentaCab.getSize() > 0) {
                lst_pedidoventa.setModel(objListaPedidoVentaCab);
                lst_pedidoventa.setFocus(true);
                llenarCamposTotalesLista();
            } else {
                objListaPedidoVentaCab = null;
                lst_pedidoventa.setModel(objListaPedidoVentaCab);
                Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                limpiarCamposTotalesLista();
            }
        } else {
            Messagebox.show("La fecha no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            limpiarCamposTotalesLista();
        }
        limpiarCampos();
        objListaPedidoVentaDet = null;
        lst_pedven_detalle.setModel(objListaPedidoVentaDet);
        limpiarCamposDetalle();
        limpiarCamposTotales();
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_pedidoventa")
    public void seleccionaRegistro() throws SQLException {
        limpiarCampos();
        limpiarCamposDetalle();
        limpiarCamposTotales();
        objPedidoVentaCab = (PedidoVentaCab) lst_pedidoventa.getSelectedItem().getValue();
        if (objPedidoVentaCab == null) {
            objPedidoVentaCab = objListaPedidoVentaCab.get(i_selCab + 1);
        }
        i_selCab = lst_pedidoventa.getSelectedIndex();
        db_tipcam.setDisabled(true);
        llenarCampos();
        llenarCamposDetalle();
        llenarCamposTotales();
    }

    @Listen("onSelect=#lst_pedven_detalle")
    public void seleccionaRegistroDetalle() throws SQLException {
        objPedidoVentaDet = lst_pedven_detalle.getSelectedItem().getValue();
        llenarCamposProducto();
        db_det_pordes.setDisabled(true);
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objPedidoVentaDet.getPro_id()).toString());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        objPedidoVentaCab = null;
        objPedidoVentaCab = new PedidoVentaCab();
        objPedidoVentaDet = null;
        objListaPedidoVentaDet = null;
        objListaPedidoVentaDet = new ListModelList();
        lst_pedven_detalle.setModel(objListaPedidoVentaDet);
        limpiarCampos();
        limpiarCamposDetalle();
        limpiarCamposTotales();
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitaBotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        lst_pedven_detalle.setSelectedIndex(-1);
        cb_tipoventa.setValue("NORMAL");
        cb_tipoventa.focus();
        cb_tipoventa.select();
        txt_situacion.setValue("INGRESADO");
        //db_tipcam.setDisabled(true);

        //cb_simbmon.setValue("S/.");
        cb_simbmon.setSelectedIndex(0);
        //cb_pronpag.setValue("NO");
        cb_pronpag.setSelectedIndex(1);
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion nuevo para crear un registro").toString());
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_pedidoventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un pedido de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_pedidoventa.focus();
        } else {
            if (objPedidoVentaCab.getPcab_situacion() == 2) {
                Messagebox.show("El pedido de venta ya no puede ser modificado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } /*else if (objPedidoVentaCab.getIndicador() == 1) {
             Messagebox.show("El pedido está en modo cierre para su generacion de nota de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             }*/ else {
                String pedido = objPedidoVentaCab.getPcab_nroped();
                ParametrosSalida objParamCabecera;
                objParamCabecera = objDaoGenPedVen.actualizaIndicador(emp_id, suc_id, pedido, 1);
                if (objParamCabecera.getFlagIndicador() == 1) {
                    Messagebox.show("Ocurrio un error con el pedido " + objPedidoVentaCab.getPcab_nroped(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
                s_estado = "M";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                habilitaBotonesDetalle(false, true);

                d_fecemi.setValue(Utilitarios.hoyAsFecha());
                //txt_cli_id.setDisabled(true);
                objListaEliminacion = null;
                objListaEliminacion = new ListModelList<PedidoVentaDet>();
                txt_cli_id.focus();
                txt_cli_id.select();
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_pedidoventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un pedido de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (objPedidoVentaCab.getPcab_situacion() != 1) {
            Messagebox.show("El pedido de venta ya no puede ser anulado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea anular el pedido de venta?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objParamSalida = objDaoGenPedVen.eliminarPedidoVenta(objPedidoVentaCab);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objListaPedidoVentaCab.clear();
                                    busquedaRegistros();
                                    s_estado = "Q";
                                    s_estado_detalle = "Q";
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
        lst_pedidoventa.focus();
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (s_estado.equals("M")) {
                                String pedido = objPedidoVentaCab.getPcab_nroped();
                                ParametrosSalida objParamCabecera;
                                objParamCabecera = objDaoGenPedVen.actualizaIndicador(emp_id, suc_id, pedido, 0);
                                if (objParamCabecera.getFlagIndicador() == 1) {
                                    Messagebox.show("Ocurrio un error con el pedido " + objPedidoVentaCab.getPcab_nroped(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                            VerificarTransacciones();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            limpiarCampos();
                            habilitaBotonesDetalle(true, true);
                            habilitaCamposDetalle(true);
                            limpiarCamposDetalle();
                            limpiarCamposTotales();
                            limpiaAuditoria();
                            txt_filtro_codsuper.setValue("");
                            txt_filtro_dessuper.setValue("");
                            txt_filtro_codven.setValue("");
                            txt_filtro_nomven.setValue("");
                            //lst_pedidoventa.focus();
                            s_estado = "Q";
                            s_estado_detalle = "Q";
                            lst_pedidoventa.focus();
                            //busquedaRegistros();
                        }
                    }
                });
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
                        if (foco.equals("Moneda")) {
                            cb_simbmon.select();
                            cb_simbmon.focus();
                        } else if (foco.equals("Cliente")) {
                            txt_cli_id.focus();
                        } else if (foco.equals("Direccion")) {
                            txt_clidir_id.focus();
                        } else if (foco.equals("Zona")) {
                            txt_zon_id.focus();
                        } else if (foco.equals("Vendedor")) {
                            txt_idvendedor.focus();
                        } else if (foco.equals("ListaPrecio")) {
                            txt_id_listapre.focus();
                        } else if (foco.equals("Condicion")) {
                            txt_id_condven.focus();
                        }
                    }
                }
            });
        } else if (objListaPedidoVentaDet.isEmpty()) {
            Messagebox.show("Por favor ingresar un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String fecemi = sdf.format(d_fecemi.getValue());
            if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                Messagebox.show("El día se encuentra cerrado - Módulo Logistica", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (objDaoCierreDia.ValidaDia(fecemi, "F").getCiedia_fac() == 0) {
                    Messagebox.show("El día se encuentra cerrado - Módulo Facturación", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if (objDaoCierreDia.ValidaDia(fecemi, "X").getCiedia_cxc() == 0) {
                        Messagebox.show("El día se encuentra cerrado - Módulo Cxc", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        if (!s_estado_detalle.equals("M")) {
                            s_mensaje = "Está seguro que desea guardar los cambios?";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParamCabecera, objParamDetalle;
                                                objPedidoVentaCab = (PedidoVentaCab) generaRegistroCabecera();
                                                if (s_estado.equals("N")) {
                                                    objParamCabecera = objDaoGenPedVen.insertarPedVenCab(objPedidoVentaCab);
                                                } else {
                                                    objParamCabecera = objDaoGenPedVen.actualizarPedVenCab(objPedidoVentaCab);
                                                }
                                                if (objParamCabecera.getFlagIndicador() == 0) {
                                                    boolean verificarDetalle = true;
                                                    int i = 0;
                                                    if (s_estado.equals("N")) {
                                                        while (i < objListaPedidoVentaDet.getSize() && verificarDetalle) {
                                                            String oc_nropedkey = objParamCabecera.getCodigoRegistro();
                                                            objListaPedidoVentaDet.get(i).setPcab_nroped(oc_nropedkey);
                                                            objParamDetalle = objDaoGenPedVen.insertarPedidoVentaDet(objListaPedidoVentaDet.get(i));
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un error con el producto " + objListaPedidoVentaDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    } else {
                                                        //OPERACION DE ELINACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                                        if (!objListaEliminacion.isEmpty()) {
                                                            while (i < objListaEliminacion.getSize() && verificarDetalle) {
                                                                objParamDetalle = objDaoGenPedVen.eliminarPedidoVentaDet(objListaEliminacion.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrio un error con el item" + objListaEliminacion.get(i).getPdet_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                        //OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                                        i = 0;
                                                        verificarDetalle = true;
                                                        while (i < objListaPedidoVentaDet.getSize() && verificarDetalle) {
                                                            String indicador = objListaPedidoVentaDet.get(i).getInd_accion();
                                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                                objParamDetalle = objDaoGenPedVen.insertarPedidoVentaDet(objListaPedidoVentaDet.get(i));
                                                            } else {
                                                                objParamDetalle = objDaoGenPedVen.actualizarPedidoVentaDet(objListaPedidoVentaDet.get(i));
                                                            }
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un error con el producto " + objListaPedidoVentaDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    }
                                                    //validacion de guardar/nuevo
                                                    VerificarTransacciones();
                                                    habilitaBotones(false, true);
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    habilitaCampos(true);
                                                    habilitaCamposDetalle(true);
                                                    habilitaBotonesDetalle(true, true);
                                                    limpiarCamposDetalle();
                                                    objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
                                                    objListaEliminacion = new ListModelList<PedidoVentaDet>();
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

    @Listen("onClick=#tbbtn_btn_nuevopro")
    public void botonNuevoDetalle() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("Cliente")) {
                            txt_cli_id.focus();
                        } else if (foco.equals("Direccion")) {
                            txt_clidir_id.focus();
                        } else if (foco.equals("Zona")) {
                            txt_zon_id.focus();
                        } else if (foco.equals("Vendedor")) {
                            txt_idvendedor.focus();
                        } else if (foco.equals("ListaPrecio")) {
                            txt_id_listapre.focus();
                        } else if (foco.equals("Condicion")) {
                            txt_id_condven.focus();
                        }
                    }
                }
            });
        } else {
            limpiarCamposDetalle();
            habilitaCamposDetalle(false);
            habilitaBotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_pedven_detalle);
            habilitaCampos(true);
            db_tipcam.setDisabled(true);
            txt_proid.focus();
            s_estado_detalle = "N";
        }
    }

    @Listen("onClick=#tbbtn_btn_editarpro")
    public void botonEditarDetalle() throws SQLException {
        if (lst_pedven_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitaCamposDetalle(false);
            txt_proid.setDisabled(true);
            //txt_det_idlispre.setDisabled(true);
            habilitaCampos(true);
            db_tipcam.setDisabled(true);
            habilitaBotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_pedven_detalle);
            objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
            txt_proid.focus();
            s_estado_detalle = "M";
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarpro")
    public void botonEliminarDetalle() {
        if (objListaPedidoVentaDet.isEmpty()) {
            Messagebox.show("No existen productos en el pedido de venta", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (lst_pedven_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            Messagebox.show("Está seguro que desea eliminar el producto", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objPedidoVentaDet = (PedidoVentaDet) lst_pedven_detalle.getSelectedItem().getValue();
                        if (s_estado.equals("M") && !objPedidoVentaDet.getInd_accion().equals("N")) {
                            objPedidoVentaDet.setInd_accion("E");
                            objListaEliminacion.add(objPedidoVentaDet);
                        }
                        objListaPedidoVentaDet.remove(lst_pedven_detalle.getSelectedIndex());
                        limpiarCamposDetalle();
                        double data[];
                        data = calculosTotal();
                        txt_total.setValue(data[0]);
                        lst_pedven_detalle.setModel(objListaPedidoVentaDet);
                    }
                }
            });

        }
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
                            txt_proid.focus();
                        } /*else if (foco.equals("lista")) {
                         txt_det_idlispre.focus();
                         }*/ else if (foco.equals("cantidad")) {
                            txt_cant.focus();
                        } else if (foco.equals("fraccion")) {
                            txt_frac.focus();
                        } else if (foco.equals("descuento")) {
                            db_det_pordes.focus();
                        }
                    }
                }
            });
        } else {
            String validaFraccion = validaFraccionVenta();
            if (!validaFraccion.isEmpty()) {
                Messagebox.show(validaFraccion, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_frac.focus();
                        }
                    }
                });
            } else {
                if (s_estado_detalle.equals("N")) {
                    if (validaIngresoProducto(txt_proid.getValue())) {
                        objPedidoVentaDet = (PedidoVentaDet) generaRegistroDetalle();
                        objPedidoVentaDet.setInd_accion("N");
                        objPedidoVentaDet.setPdet_prodes(txt_prodes.getValue());
                        objPedidoVentaDet.setLp_des(txt_des_listapre.getValue());
                        objListaPedidoVentaDet.add(objPedidoVentaDet);
                    } else {
                        Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", 1, Messagebox.INFORMATION);
                    }
                } //Si se modifica o elimina en el detalle
                else {
                    //productoDetalle();
                    //Si se ingreso en el detalle un nuevo registro
                    if (objPedidoVentaDet.getInd_accion().equals("N")) {
                        objPedidoVentaDet = (PedidoVentaDet) generaRegistroDetalle();
                        objPedidoVentaDet.setInd_accion("N");
                    } else {
                        objPedidoVentaDet = (PedidoVentaDet) generaRegistroDetalle();
                        objPedidoVentaDet.setInd_accion("M");
                    }
                    //Reemplazar el registro en la ubicacion seleccionada
                    objPedidoVentaDet.setPdet_prodes(txt_prodes.getValue());
                    objPedidoVentaDet.setLp_des(txt_des_listapre.getValue());
                    objListaPedidoVentaDet.set(lst_pedven_detalle.getSelectedIndex(), objPedidoVentaDet);
                    s_estado_detalle = "Q";
                }
                objListaPedidoVentaDet.clearSelection();
                lst_pedven_detalle.setModel(objListaPedidoVentaDet);
                Utilitarios.deshabilitarLista(false, lst_pedven_detalle);
                limpiarCamposDetalle();
                habilitaCamposDetalle(true);
                habilitaBotonesDetalle(false, true);
                double data[];
                data = calculosTotal();
                //db_det_pordes.setDisabled(true);
                txt_total.setValue(data[0]);
                lst_pedven_detalle.focus();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacerpro")
    public void botonDeshacerDetalle() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    habilitaBotonesDetalle(false, true);
                    //habilitaCampos(false);
                    llenarCamposTotales();
                    db_tipcam.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_pedven_detalle);
                    lst_pedven_detalle.clearSelection();
                    lst_pedven_detalle.focus();
                    s_estado_detalle = "Q";
                }
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objListaPedidoVentaCab == null || objListaPedidoVentaCab.isEmpty()) {
            Messagebox.show("No existen pedidos de venta para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_pedidoventa.getSelectedIndex() >= 0) {
                objPedidoVentaCab = (PedidoVentaCab) lst_pedidoventa.getSelectedItem().getValue();
                if (objPedidoVentaCab == null) {
                    objPedidoVentaCab = objListaPedidoVentaCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("codigoped", Integer.parseInt(objPedidoVentaCab.getPcab_nroped()));
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/procesos/LovImpresionGenPedVen.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione un pedido de venta para imprimir");
            }
        }
    }

    @Listen("onClickRecuperar=#lst_pedidoventa")
    public void recuperarPedidoAnulado(ForwardEvent evt) throws SQLException {
        tbbtn_recuperar = (Toolbarbutton) evt.getOrigin().getTarget();
        objlstAux = (ListModelList) lst_pedidoventa.getModel();
        final Listitem item = (Listitem) tbbtn_recuperar.getParent().getParent();
        if (objlstAux.get(item.getIndex()).getPcab_estado() == 2) {
            s_mensaje = "Está seguro que desea recuperar el pedido de venta?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objParametrosSalida = objDaoGenPedVen.recuperarPedidoVenta(objPedidoVentaCab);
                                if (objParametrosSalida.getFlagIndicador() == 0) {
                                    objListaPedidoVentaCab.clear();
                                    busquedaRegistros();
                                    s_estado = "Q";
                                    s_estado_detalle = "Q";
                                    lst_pedven_detalle.focus();
                                }
                                Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        } else {
            Messagebox.show("El pedido de venta no está anulado");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_genpedven")
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
                if (!tbbtn_btn_nuevopro.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarpro.isDisabled()) {
                    botonEditarDetalle();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarpro.isDisabled()) {
                    botonEliminarDetalle();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarpro.isDisabled()) {
                    botonGuardarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacerpro.isDisabled()) {
                    botonDeshacerDetalle();
                }
                break;
        }
    }

    /*@Listen("onClose=#w_genpedven")
     public void cerrarVentana() throws SQLException {
     if (s_estado.equals("M")) {
     String pedido = objPedidoVentaCab.getPcab_nroped();
     ParametrosSalida objParamCabecera;
     objParamCabecera = objDaoGenPedVen.actualizaIndicador(emp_id, suc_id, pedido, 0);
     if (objParamCabecera.getFlagIndicador() == 1) {
     Messagebox.show("Ocurrio un error con el pedido " + objPedidoVentaCab.getPcab_nroped(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     }
     }
     }*/
    @Listen("onOK=#cb_periodo")
    public void next_Supervisor() {
        txt_filtro_codsuper.focus();
    }

    @Listen("onOK=#txt_filtro_codsuper")
    public void lovSupervisor() {
        if (bandera == false) {
            bandera = true;
            if (txt_filtro_codsuper.getValue().isEmpty()) {
                modoEjecucion = "GenPedVen";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_supid", txt_filtro_codsuper);
                objMapObjetos.put("txt_apenom", txt_filtro_dessuper);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSupervisores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_filtro_codven.focus();
            }
        }
    }

    @Listen("onBlur=#txt_filtro_codsuper")
    public void validaSupervisor() throws SQLException {
        if (!txt_filtro_codsuper.getValue().isEmpty()) {
            if (!txt_filtro_codsuper.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_filtro_codsuper.setValue("");
                            txt_filtro_dessuper.setValue("");
                            txt_filtro_codsuper.focus();
                        }
                    }
                });
            } else {
                Supervisor objSup;
                int supid = Integer.parseInt(txt_filtro_codsuper.getValue());
                objSup = new DaoSupervisores().getNomSupervisor(supid);
                if (objSup == null) {
                    Messagebox.show("El código de supervisor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_filtro_codsuper.setValue("");
                                txt_filtro_dessuper.setValue("");
                                txt_filtro_codsuper.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Supervisor con codigo " + supid + " y ha encontrado 1 registro(s)");
                    txt_filtro_codsuper.setValue(Utilitarios.lpad(String.valueOf(objSup.getSup_key()), 4, "0"));
                    txt_filtro_dessuper.setValue(objSup.getSup_apenom());
                }
            }
        } else {
            txt_filtro_dessuper.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_filtro_codven")
    public void lovVendedorbusqueda() {
        if (bandera == false) {
            bandera = true;
            if (txt_filtro_codven.getValue().isEmpty()) {
                modoEjecucion = "GenPedVen";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_venid", txt_filtro_codven);
                objMapObjetos.put("txt_vendes", txt_filtro_nomven);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                d_filtro_fecha.focus();
            }
        }
    }

    @Listen("onBlur=#txt_filtro_codven")
    public void validaVendedor() throws SQLException {
        if (!txt_filtro_codven.getValue().isEmpty()) {
            if (!txt_filtro_codven.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_filtro_codven.setValue("");
                            txt_filtro_nomven.setValue("");
                            txt_filtro_codven.focus();
                        }
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_filtro_codven.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El código de vendedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_filtro_codven.setValue("");
                                txt_filtro_nomven.setValue("");
                                txt_filtro_codven.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedorCon.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_filtro_codven.setValue(objVendedorCon.getVen_id());
                    txt_filtro_nomven.setValue(objVendedorCon.getVen_ape() + " " + objVendedorCon.getVen_nom());
                }
            }
        } else {
            txt_filtro_nomven.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#d_filtro_fecha")
    public void next_Consultar() {
        btn_consultarpedven.focus();
    }

    @Listen("onOK=#txt_cli_id")
    public void lovCliente() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "GenPedVen";
                objMapObjetos.put("txt_cli_id", txt_cli_id);
                objMapObjetos.put("txt_cli_razsoc", txt_cli_razsoc);
                objMapObjetos.put("txt_dni", txt_dni);
                objMapObjetos.put("txt_ruc", txt_ruc);
                objMapObjetos.put("db_limcred", db_limcred);
                objMapObjetos.put("txt_limdoc", txt_limdoc);
                objMapObjetos.put("db_saldo", db_saldo);
                objMapObjetos.put("txt_estado", txt_estado);
                objMapObjetos.put("txt_clidir_id", txt_clidir_id);
                objMapObjetos.put("txt_clidir_direcc", txt_clidir_direcc);
                objMapObjetos.put("txt_zon_id", txt_zon_id);
                objMapObjetos.put("txt_zon_des", txt_zon_des);
                objMapObjetos.put("txt_giro", txt_giro);
                objMapObjetos.put("txt_desgiro", txt_desgiro);
                objMapObjetos.put("txt_idvendedor", txt_idvendedor);
                objMapObjetos.put("txt_nomvendedor", txt_nomvendedor);
                objMapObjetos.put("txt_diavis", txt_diavis);
                objMapObjetos.put("txt_id_listapre", txt_id_listapre);
                objMapObjetos.put("txt_des_listapre", txt_des_listapre);
                objMapObjetos.put("txt_id_condven", txt_id_condven);
                objMapObjetos.put("txt_des_condven", txt_des_condven);
                objMapObjetos.put("txt_diaplazo", txt_diaplazo);
                objMapObjetos.put("cb_pronpag", cb_pronpag);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_clidir_id.focus();
                txt_clidir_id.select();
            }
        }
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaCliente() throws SQLException {
        if (!txt_cli_id.getValue().isEmpty()) {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_dni.setValue("");
                                    txt_ruc.setValue("");
                                    txt_cli_id.focus();
                                    txt_cli_id.select();
                                }
                            }
                        });
            } else {
                objCliente = objDaoCliente.ExistenciaCliente(txt_cli_id.getValue());
                //Cliente objClientes = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                //Cliente objClientes = new DaoCliente().ExistenciaCliente(txt_cli_id.getValue());
                if (objCliente == null/* || objClientes == null*/) {
                    Messagebox.show("El código de cliente no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_dni.setValue("");
                                txt_ruc.setValue("");
                                txt_cli_id.focus();
                                txt_cli_id.select();
                            }
                        }
                    });
                } else {
                    objCliFin = objDaoCliente.getClienteFin(objCliente.getCli_id().toString(), emp_id, suc_id);
                    txt_cli_id.setValue(Utilitarios.lpad(txt_cli_id.getValue(), 10, "0"));
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                    txt_dni.setValue(objCliente.getCli_dni());
                    txt_ruc.setValue(String.valueOf(objCliente.getCli_ruc()));
                    //txt_lincred.setValue(String.valueOf(objCliente.getCli_limcredemp()));
                    //txt_limdoc.setValue(String.valueOf(objCliente.getCli_limdocemp()));
                    /*Object obj[] = objDaoCliente.ValidaLimiteCredito(txt_cli_id.getValue(), Long.parseLong(txt_cli_id.getValue()));
                     int saldo = Integer.parseInt(obj[0].toString());
                     int credito = Integer.parseInt(String.valueOf(objCliente.getCli_credcor()));                    
                     txt_saldo.setValue(String.valueOf(credito - saldo));*/
                    db_limcred.setValue(objCliFin == null ? 0.0 : objCliFin.getClifin_limcred());
                    txt_limdoc.setValue(objCliFin == null ? 0 : objCliFin.getClifin_limdoc());
                    db_saldo.setValue(0.0);
                    txt_estado.setValue(objCliente.getCli_est() == 1 ? "ACTIVO" : "INACTIVO");
                    txt_clidir_id.setValue(Long.parseLong(String.valueOf(objCliente.getClidir_id())));
                    txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                    txt_zon_id.setValue(objCliente.getZon_id());
                    txt_zon_des.setValue(objCliente.getZon_des());
                    txt_giro.setValue(objCliente.getCli_giro());
                    txt_idvendedor.setValue(objCliente.getVen_id());
                    txt_nomvendedor.setValue(objCliente.getVen_apenom());
                    txt_diavis.setValue(objCliente.getZon_dvis_des());
                    txt_id_listapre.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_lista()), 4, "0"));
                    objLpVenta = new DaoListaPrecios().getListaPrecio(objCliente.getCli_lista(), 2);
                    if (objLpVenta != null) {
                        txt_des_listapre.setValue(objLpVenta.getLp_des());
                    } else {
                        txt_des_listapre.setValue("");
                    }
                    txt_id_condven.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_con()), 3, "0"));
                    txt_des_condven.setValue(objCliente.getCli_descond());
                    txt_diaplazo.setValue(String.valueOf(objCliente.getDiasplazo()));
                    txt_facbol.setValue(objCliente.getCli_factura());
                    txt_horent.setValue(objCliente.getHor_id());

                    //if (s_estado.equals("M") && !txt_id_condven.getValue().isEmpty() && !txt_cli_id.getValue().isEmpty() && !txt_id_listapre.getValue().isEmpty()) {
                    if (s_estado.equals("M") && objLpVenta != null && objCondicion != null) {
                        recalcularLista();
                        llenarCamposTotales();
                        limpiarCamposDetalle();
                    }

                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + "y ha encontrado 1 registro(s)");
                }
            }
        }
        bandera = false;

    }

    @Listen("onOK=#txt_clidir_id")
    public void lovDireccion() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (!txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() == null) {
                Map mapeo = new HashMap();
                modoEjecucion = "GenPedVen";
                mapeo.put("txt_cliid", txt_cli_id);
                mapeo.put("txt_dirid", txt_clidir_id);
                mapeo.put("txt_dirdes", txt_clidir_direcc);
                mapeo.put("txt_zonid", txt_zon_id);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDirecciones.zul", null, mapeo);
                window.doModal();
            } else {
                txt_idvendedor.focus();
                txt_idvendedor.select();
            }
        }
    }

    @Listen("onBlur=#txt_clidir_id")
    public void validaDireccion() throws SQLException {
        if (txt_clidir_id.getValue() != null) {
            if (txt_cli_id.getValue().isEmpty()) {
                Messagebox.show("Por favor verifique el código del cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_cli_id.focus();
                        }
                    }
                });
            } else {
                if (txt_cli_id.getValue().matches("[0-9]*")) {
                    objCliente = objDaoCliente.ExistenciaCliente(txt_cli_id.getValue());
                    if (objCliente != null) {
                        CliDireccion objCliDireccion = new DaoDirecciones().getNomDireccion(txt_cli_id.getValue(), txt_clidir_id.getValue(), objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                        if (objCliDireccion == null) {
                            Messagebox.show("La direccion no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                                    Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                limpiarDireccionNulo();
                                                txt_clidir_id.focus();
                                            }
                                        }
                                    });
                        } else {
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la dirección del cliente con código " + objCliDireccion.getCli_id() + " y ha encontrado 1 registro(s)");
                            txt_clidir_id.setValue(Long.parseLong(String.valueOf(objCliDireccion.getClidir_id())));
                            txt_clidir_direcc.setValue(objCliDireccion.getClidir_direc());
                            txt_zon_id.setValue(objCliDireccion.getZon_id());
                            txt_zon_des.setValue(objCliDireccion.getZon_des());
                            txt_diavis.setValue(objCliDireccion.getZon_diavis_des());
                            txt_idvendedor.setValue(objCliDireccion.getVen_id());
                            txt_nomvendedor.setValue(objCliDireccion.getVen_apenom());
                            txt_giro.setValue(objCliDireccion.getGiro_id().concat(" - ").concat(objCliDireccion.getGiro_des()));
                            txt_facbol.setValue(objCliDireccion.getCli_factura());
                            txt_horent.setValue(objCliDireccion.getHor_id());
                        }
                    }
                }
            }
        } else {
            limpiarDireccionNulo();
        }
        bandera = false;
    }

    @Listen("onOK=#txt_zon_id")
    public void lovZona() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if ((!txt_cli_id.getValue().isEmpty() || !txt_cli_razsoc.getValue().isEmpty()) && txt_zon_id.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                modoEjecucion = "GenPedVen";
                objLovZonas.put("txt_zonid", txt_zon_id);
                objLovZonas.put("txt_zondes", txt_zon_des);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovZonas.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_idvendedor.focus();
            }
        }
    }

    @Listen("onBlur=#txt_zon_id")
    public void validaZona() throws SQLException {
        if (!txt_zon_id.getValue().isEmpty()) {
            if (!txt_zon_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor solo ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_zon_id.setValue("");
                        txt_zon_des.setValue("");
                        txt_zon_id.focus();
                    }
                });
            } else {
                Zona objZona = new DaoZonas().buscaZonaxCodigo(txt_zon_id.getValue());
                if (objZona == null) {
                    Messagebox.show("El código ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_zon_id.setValue("");
                            txt_zon_des.setValue("");
                            txt_zon_id.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Zona con codigo " + objZona.getZon_id() + " y ha encontrado 1 registro(s)");
                    txt_zon_id.setValue(objZona.getZon_id());
                    txt_zon_des.setValue(objZona.getZon_des());
                }
            }
        } else {
            txt_zon_des.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_idvendedor")
    public void lovVendedor() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_idvendedor.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "GenPedVen";
                objMapObjetos.put("txt_venid", txt_idvendedor);
                objMapObjetos.put("txt_vendes", txt_nomvendedor);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_id_listapre.focus();
                txt_id_listapre.select();
            }
        }
    }

    @Listen("onBlur=#txt_idvendedor")
    public void validaProveedor() throws SQLException {
        if (!txt_idvendedor.getValue().isEmpty()) {
            if (!txt_idvendedor.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_idvendedor.setValue("");
                            txt_nomvendedor.setValue("");
                            txt_idvendedor.focus();
                        }
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_idvendedor.getValue());
                Vendedor objVendedor = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedor == null) {
                    Messagebox.show("El código de vendedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_idvendedor.setValue("");
                                txt_nomvendedor.setValue("");
                                txt_idvendedor.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedor.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_idvendedor.setValue(objVendedor.getVen_id());
                    txt_nomvendedor.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
                }
            }
        } else {
            txt_nomvendedor.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_id_listapre")
    public void lovListaPrecioVenta() {
        if (bandera == false) {
            bandera = true;
            if (txt_id_listapre.getValue().isEmpty() && !txt_cli_id.getValue().isEmpty()) {
                Map mapeo = new HashedMap();
                modoEjecucion = "GenPedVen";
                mapeo.put("txt_id_listapre", txt_id_listapre);
                mapeo.put("txt_des_listapre", txt_des_listapre);
                mapeo.put("txt_id_condven", txt_id_condven);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioVenta.zul", null, mapeo);
                window.doModal();
            } else {
                txt_id_condven.focus();
                txt_id_condven.select();
            }
        }
    }

    @Listen("onBlur=#txt_id_listapre")
    public void validaListaPrecioVenta() throws SQLException {
        if (!txt_id_listapre.getValue().isEmpty()) {
            if (!txt_id_listapre.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_id_listapre.setValue("");
                                    txt_des_listapre.setValue("");
                                    txt_id_listapre.focus();
                                }
                            }
                        });
            } else {
                int lpc_key = Integer.parseInt(txt_id_listapre.getValue());
                objLpVenta = new DaoListaPrecios().getListaPrecio(lpc_key, 2);
                if (objLpVenta == null) {
                    Messagebox.show("El código de la lista de precio no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_id_listapre.setValue("");
                                        txt_des_listapre.setValue("");
                                        txt_id_listapre.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objLpVenta.getLp_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_id_listapre.setValue(objLpVenta.getLp_id());
                    txt_des_listapre.setValue(objLpVenta.getLp_des());

                    //if (s_estado.equals("M") && !txt_id_condven.getValue().isEmpty() && !txt_cli_id.getValue().isEmpty() && !txt_id_listapre.getValue().isEmpty()) {
                    //if (s_estado.equals("M") && !txt_id_condven.getValue().equals("") && !txt_cli_id.getValue().equals("") && !txt_id_listapre.getValue().equals("")) {
                    if (s_estado.equals("M") && objCliente != null && objCondicion != null) {
                        recalcularLista();
                        llenarCamposTotales();
                        limpiarCamposDetalle();
                    }
                }
            }
        } else {
            txt_des_listapre.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_id_condven")
    public void lovCondicionVenta() throws SQLException {
        if (bandera == false) {
            //bandera = true;
            if ((!txt_cli_id.getValue().isEmpty() || !txt_cli_razsoc.getValue().isEmpty()) && txt_id_condven.getValue().isEmpty()) {
                bandera = true;
                Map objMapObjetos = new HashMap();
                modoEjecucion = "GenPedVen";
                objMapObjetos.put("txt_id_condven", txt_id_condven);
                objMapObjetos.put("txt_des_condven", txt_des_condven);
                objMapObjetos.put("txt_diaplazo", txt_diaplazo);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCondicionesVenta.zul", null, objMapObjetos);
                window.doModal();
            } else {
                /*cb_pronpag.focus();
                 cb_pronpag.select();*/
                txt_diaplazo.focus();
            }
        }
    }

    @Listen("onBlur=#txt_id_condven")
    public void validaCondicionVenta() throws SQLException {
        if (!txt_id_condven.getValue().isEmpty()) {
            if (!txt_id_condven.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_id_condven.setValue("");
                                    txt_des_condven.setValue("");
                                    txt_id_condven.focus();
                                }
                            }
                        });
            } else {
                int con_key = Integer.parseInt(txt_id_condven.getValue());
                objCondicion = new DaoCondicion().buscarCondicion(con_key, "V");
                if (objCondicion == null) {
                    Messagebox.show("El código de la condición no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_id_condven.setValue("");
                                        txt_des_condven.setValue("");
                                        txt_id_condven.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objCondicion.getConId()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_id_condven.setValue(objCondicion.getConId());
                    txt_des_condven.setValue(objCondicion.getConDes());
                    txt_diaplazo.setValue(String.valueOf(objCondicion.getConDias()));

                    //if (s_estado.equals("M") && !txt_id_condven.getValue().isEmpty() && !txt_cli_id.getValue().isEmpty() && !txt_id_listapre.getValue().isEmpty()) {
                    if (s_estado.equals("M") && objCliente != null && objLpVenta != null) {
                        /*if (s_estado.equals("M")) {
                         if (objCliente != null) {
                         if (objLpVenta != null) {*/
                        recalcularLista();
                        llenarCamposTotales();
                        limpiarCamposDetalle();
                        //}
                    }
                }
            }
        } else {
            txt_des_condven.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_diaplazo")
    public void next_nuevodetalle() throws SQLException {
        botonNuevoDetalle();
    }

    @Listen("onOK=#cb_tipoventa")
    public void next_txt_dni() {
        //d_fecemi.focus();
        txt_cli_id.focus();
    }

    @Listen("onOK=#d_fecemi")
    public void next_cb_simbmon() {
        cb_simbmon.focus();
        cb_simbmon.select();
    }

    @Listen("onOK=#cb_simbmon")
    public void next_txt_cli_id() {
        txt_cli_id.focus();
    }

    @Listen("onBlur=#cb_simbmon")
    public void validaTipoCambio() throws SQLException {
        if (d_fecemi.getValue() == null) {
            Messagebox.show("Ingresar la fecha de emisión del pedido de venta ");
        } else {
            String s_fecha_emision = sdf.format(d_fecemi.getValue());
            if ("S/.".equals(cb_simbmon.getValue())) {
                monid = 1;
            } else {
                monid = cb_simbmon.getSelectedItem().getValue();
            }
            double i_tc = new DaoTipoCambio().obtieneTipoCambioComercial(s_fecha_emision, monid);
            if (i_tc < 1) {
                Messagebox.show("No existe tipo de Cambio para la fecha " + s_fecha_emision);
                db_tipcam.setValue(0.00);
                db_tipcam.focus();
            } else {
                db_tipcam.setValue(i_tc);
            }
        }
    }

    @Listen("onCheck=#chk_modtip")
    public void modificaTipoCambio() throws SQLException {
        if (chk_modtip.isChecked()) {
            db_tipcam.setDisabled(false);
        } else {
            validaTipoCambio();
            db_tipcam.setDisabled(true);
        }
    }

    @Listen("onBlur=#db_tipcam")
    public void onBlurTipoCambio() throws SQLException {
        if (db_tipcam.getValue() == null) {
            Messagebox.show("Por favor ingresar tipo de cambio", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                db_tipcam.focus();
                            }
                        }
                    });
        }
    }

    /*@Listen("onOK=#cb_pronpag")
     public void next_nuevoDetalle() throws SQLException {
     botonNuevoDetalle();
     }*/
    @Listen("onOK=#txt_proid")
    public void lovProducto() {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "GenPedVen";
                mapeo.put("txt_proid", txt_proid);
                mapeo.put("txt_prodes", txt_prodes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerGenPedVen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                window.doModal();
            } else {
                txt_cant.focus();
            }
        }
    }

    @Listen("onBlur=#txt_proid")
    public void validaProducto() throws SQLException {
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
                objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
                if (objProductos == null) {
                    limpiarCamposDetalle();
                    s_mensaje = "El producto no existe o esta inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_proid.focus();
                                    }
                                }
                            });
                } else {
                    txt_prodes.setValue(objProductos.getPro_des());
                    String pro_id = txt_proid.getValue();
                    objPreciosVenta = new DaoPrecios().getPrecioxLpVenta(emp_id, suc_id, pro_id, txt_id_listapre.getValue());
                    if (objPreciosVenta != null) {
                        //String imp = new DaoPedVen().obtieneImpuesto(1);
                        Double imp;
                        String tipimp = objProductos.getPro_condimp();
                        if (tipimp.equals("A")) {
                            imp = Double.parseDouble(new DaoPedVen().obtieneImpuesto(1));
                        } else {
                            imp = 0.0;
                        }
                        txt_det_idlispre.setValue(txt_id_listapre.getValue());
                        txt_det_deslispre.setValue(txt_des_listapre.getValue());
                        db_det_preven.setValue(objPreciosVenta.getPre_venta() / (1 + (imp / 100)));
                        if (txt_id_condven.getValue().equals("001")) {
                            db_det_pordes.setValue(objPreciosVenta.getPre_valvent());
                        } else {
                            db_det_pordes.setValue(objPreciosVenta.getPre_igv());
                        }

                        db_det_porigv.setValue(imp);
                        //objParametrosSalida = new ParametrosSalida();
                        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_fecemi.getValue()), 101, objProductos.getPro_id(), "%%");
                        if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > objProductos.getPro_scknofact()) {
                            db_det_stock.setValue((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - objProductos.getPro_scknofact());
                        } else {
                            db_det_stock.setValue(0.00);
                        }
                        txt_proid.setDisabled(true);
                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objPreciosVenta.getPro_id() + " y ha encontrado 1 registro(s)");
                    } else {
                        Messagebox.show("El producto no tiene una lista asignada", "ERP-JIMVER", Messagebox.OK,
                                Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            txt_proid.focus();
                                        }
                                    }
                                });
                    }
                }
            }
        } else {
            limpiarCamposDetalle();
        }
        bandera = false;
    }

    @Listen("onOK=#txt_cant")
    public void next_txt_frac() {
        if (txt_cant.getValue() == null) {
            txt_cant.setValue(0);
        }
        txt_frac.focus();
    }

    @Listen("onBlur=#txt_cant")
    public void validacalculoEnt() throws SQLException {
        if (txt_cant.getValue() != null) {
            if (db_det_preven.getValue() != null) {
                if (txt_frac.getValue() == null) {
                    txt_frac.setValue(0);
                }
                if (txt_cant.getValue() < 0) {
                    txt_cant.setValue(txt_cant.getValue() * -1);
                }
                if (txt_cant.getValue() != 0) {
                    int unipresven = objProductos.getPro_presminven();
                    double decimal = Double.parseDouble(df2.format(txt_cant.getValue() % 1));
                    if (decimal != 0.0) {
                        int sindec = (int) Math.round(txt_cant.getValue() - decimal);
                        int frac = (int) (decimal * unipresven);
                        txt_frac.setValue(frac);
                        txt_cant.setValue(sindec);
                    }
                }
                validacalculo();
            }
        } else {
            txt_cant.setValue(0);
        }
    }

    @Listen("onOK=#txt_frac")
    public void next_txt_lista() throws SQLException {
        if (txt_frac.getValue() == null) {
            txt_frac.setValue(0);
        }
        db_det_stock.focus();
    }

    @Listen("onBlur=#txt_frac")
    public void validacalculoFrac() throws SQLException {
        if (txt_frac.getValue() != null) {
            if (db_det_preven.getValue() != null) {
                if (txt_cant.getValue() == null) {
                    txt_cant.setValue(0);
                }
                if (txt_frac.getValue() != 0) {
                    int unipresven = objProductos.getPro_presminven();
                    int canfrac = txt_frac.getValue();
                    int canent = (int) Math.round(txt_cant.getValue());
                    txt_frac.setValue(canfrac >= unipresven ? canfrac % unipresven : canfrac);
                    txt_cant.setValue(canent + Math.round(canfrac / unipresven));
                }
                validacalculo();
            }
        } else {
            txt_frac.setValue(0);
        }
    }

    @Listen("onCheck=#chk_desman")
    public void habilitaDescuento() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (txt_cant.getValue() == null) {
                txt_cant.setValue(0);
            }
            if (txt_frac.getValue() == null) {
                txt_frac.setValue(0);
            }
            if (chk_desman.isChecked()) {
                db_det_pordes.setDisabled(false);
            } else {
                objPreciosVenta = new DaoPrecios().getPrecioxLpVenta(emp_id, suc_id, txt_proid.getValue(), txt_id_listapre.getValue());
                db_det_preven.setValue(objPreciosVenta.getPre_venta());
                if (txt_id_condven.getValue().equals("001")) {
                    db_det_pordes.setValue(objPreciosVenta.getPre_valvent());
                } else {
                    db_det_pordes.setValue(objPreciosVenta.getPre_igv());
                }
                db_det_pordes.setDisabled(true);
            }
            validacalculo();
        } else {
            Messagebox.show("Por favor ingrese un código de producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        chk_desman.setChecked(false);
                        txt_proid.focus();
                    }
                }
            });
        }
    }

    @Listen("onOK=#db_det_pordes")
    public void onOK_db_det_pordes() throws SQLException {
        db_det_stock.focus();
    }

    @Listen("onBlur=#db_det_pordes")
    public void onBlur_db_det_pordes() throws SQLException {
        if (db_det_pordes.getValue() == null) {
            db_det_pordes.setValue(0.0);
        }
        validacalculo();
    }

    @Listen("onOK=#db_det_stock")
    public void onOK_db_det_stock() throws SQLException {
        botonGuardarDetalle();
    }

    //Eventos Otros 
    public Object generaRegistroCabecera() throws SQLException {
        double data[];
        //String oc_ind = "C";
        data = calculosTotal();

        int canal = Integer.parseInt(StringFns.substring(txt_zon_id.getValue(), 0, 2));
        int mesa = Integer.parseInt(StringFns.substring(txt_zon_id.getValue(), 2, 4));
        objMesa = objDaoMesa.busMesaxCanal(canal, mesa);

        String pcab_nroped = txt_nropedven.getValue().equals("") ? "0" : txt_nropedven.getValue();
        Date pcab_fecemi = d_fecemi.getValue();
        int pcab_estado = txt_estado.getValue().equals("ACTIVO") ? 1 : 2;
        String pcab_tipven = Utilitarios.lpad(cb_tipoventa.getSelectedItem().getValue().toString(), 3, "0");
        int pcab_situacion = 1;
        int pcab_gpslat = 1;
        int pcab_gpslon = 1;
        Date pcab_fecent = d_fecemi.getValue();
        String diaent = sdffe.format(pcab_fecent);
        if (diaent.equals("sáb")) {
            pcab_fecent = Utilitarios.sumaDias(pcab_fecemi, 2);
        } else {
            pcab_fecent = Utilitarios.sumaDias(pcab_fecemi, 1);
        }
        String zon_id = txt_zon_id.getValue();
        String pcab_motrec = "000";
        int clidir_id = Integer.parseInt(txt_clidir_id.getValue().toString());
        String pcab_dirent = txt_clidir_direcc.getValue();
        String cli_id = txt_cli_id.getValue();
        String pcab_canid = StringFns.substring(txt_zon_id.getValue(), 0, 2);
        String ven_id = txt_idvendedor.getValue();
        String sup_id = objMesa.getSup_id();
        int pcab_facbol = txt_facbol.getValue();
        String con_id = txt_id_condven.getValue();
        String pcab_mon = Utilitarios.lpad(cb_simbmon.getSelectedItem().getValue().toString(), 3, "0");
        String lp_id = txt_id_listapre.getValue();
        double pcab_tipcam = db_tipcam.getValue();
        double pcab_limcre = db_limcred.getValue();
        int pcab_limdoc = txt_limdoc.getValue();
        //double pcab_salcre = Double.parseDouble(txt_saldo.getValue());
        double pcab_salcre = 0.0;
        String pcab_nrodni = txt_dni.getValue();
        String pcab_nroruc = txt_ruc.getValue();
        double pcab_totped = Utilitarios.formatoDecimal(data[0], 4);
        //int pcab_diavis = Integer.parseInt(txt_diavis.getValue());
        int pcab_diavis = Integer.parseInt(StringFns.substring(txt_zon_id.getValue(), 7, 8));
        String pcab_horent = txt_horent.getValue();
        double pcab_totper = 1;
        int pcab_aprcon = 1;
        String pcab_aprglo = "01";
        String pcab_docref = "01";
        String pcab_giro = txt_giro.getValue();
        int pcab_ppago = cb_pronpag.getValue().toString().equals("SI") ? 1 : 2;
        int pcab_modtipcam = chk_modtip.isChecked() ? 1 : 2;
        String pcab_usuadd = objUsuCredential.getCuenta();
        String pcab_pcadd = objUsuCredential.getComputerName().toUpperCase();
        String pcab_usumod = objUsuCredential.getCuenta();
        String pcab_pcmod = objUsuCredential.getComputerName().toUpperCase();
        return new PedidoVentaCab(pcab_nroped, pcab_fecemi, emp_id, suc_id, pcab_estado, pcab_situacion,
                pcab_fecent, zon_id, pcab_motrec, clidir_id, pcab_dirent, cli_id, pcab_canid,
                ven_id, sup_id, pcab_facbol, con_id, pcab_mon, lp_id, pcab_tipcam, pcab_limcre,
                pcab_limdoc, pcab_salcre, pcab_nrodni, pcab_nroruc, pcab_totped, pcab_diavis, pcab_horent,
                pcab_gpslat, pcab_gpslon, pcab_totper, pcab_aprcon, pcab_aprglo, pcab_docref,
                pcab_giro, pcab_ppago, pcab_tipven, pcab_modtipcam, pcab_usuadd, pcab_pcadd, pcab_usumod, pcab_pcmod);
    }

    public Object generaRegistroDetalle() throws SQLException {
        //String pv_ind = "C";
        int l_item = txt_pedvendet_key.getValue() == null ? 0 : txt_pedvendet_key.getValue();
        String l_nropedid = txt_nropedven.getValue().isEmpty() ? "" : txt_nropedven.getValue();
        Date fecha = d_fecemi.getValue();
        //String periodo = sdfm.format(fecha);
        int estado = 1;
        int situacion = 1;
        String s_proid = Utilitarios.lpad(txt_proid.getValue(), 9, "0");
        String pro_cla = objProductos.getPro_clas();
        String lp_id = txt_det_idlispre.getValue();
        int det_unipre = objProductos.getPro_presminven();
        double d_cant = txt_cant.getValue();
        int d_frac = txt_frac.getValue();
        int det_cantped = (int) (d_cant * det_unipre + d_frac);
        int det_canent = det_cantped;
        double d_precio = db_det_preven.getValue();
        //double d_precio = Utilitarios.formatoDecimal(db_det_preven.getValue().doubleValue(), 4);
        double d_val_vta = db_det_valvta.getValue();
        //double d_val_vta = Utilitarios.formatoDecimal(db_det_valvta.getValue().doubleValue(), 4);
        double d_pdesc = db_det_pordes.getValue();
        //double d_pdesc = Utilitarios.formatoDecimal(db_det_pordes.getValue().doubleValue(), 2);
        double d_vdesc = db_det_valdes.getValue();
        //double d_vdesc = Utilitarios.formatoDecimal(db_det_valdes.getValue().doubleValue(), 4);
        double d_pimpto = db_det_porigv.getValue();
        //double d_pimpto = Utilitarios.formatoDecimal(db_det_porigv.getValue().doubleValue(), 2);
        double d_vimpto = db_det_valigv.getValue();
        //double d_vimpto = Utilitarios.formatoDecimal(db_det_valigv.getValue().doubleValue(), 4);
        double d_vtotal = db_det_total.getValue();
        //double d_vtotal = Utilitarios.formatoDecimal(db_det_total.getValue().doubleValue(), 4);
        int det_desman = chk_desman.isChecked() ? 1 : 0;

        String usu_add = objUsuCredential.getCuenta().toUpperCase();
        String usu_mod = objUsuCredential.getCuenta().toUpperCase();
        //String det_desart = objProductos.getPro_tip().equals("SERVICIO") ? objProductos.getPro_desdes() : "";

        //int det_proper = 0;
        return new PedidoVentaDet(l_item, l_nropedid, fecha, suc_id, emp_id, estado, situacion, s_proid, pro_cla, lp_id, det_cantped,
                det_canent, d_precio, d_val_vta, d_pdesc, d_vdesc, d_pimpto, d_vimpto, d_vtotal, det_unipre, /*det_desart,*/ det_desman,/*det_proper*/
                usu_add, usu_mod);
    }

    public void llenarCampos() {

        try {
            objMotivoRechazo = objDaoMotivoRechazo.buscaMotRecxCodigo(objPedidoVentaCab.getPcab_motrec());
            objCliente = objDaoCliente.getNomCliente(objPedidoVentaCab.getCli_id());
            objCliFin = objDaoCliente.getClienteFin(objCliente.getCli_id().toString(), emp_id, suc_id);

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ControllerGenPedVen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        txt_nropedven.setValue(objPedidoVentaCab.getPcab_nroped());
        cb_tipoventa.setValue(objPedidoVentaCab.getTip_ventades());
        if (objPedidoVentaCab.getPcab_motrec_des() != null) {
            //txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des() + "-(" + objPedidoVentaCab.getPcab_motrec_des() + ")");
            txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des() + "-(" + objMotivoRechazo.getMrec_des() + ")");
        } else {
            txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des());
        }
        d_fecemi.setValue(objPedidoVentaCab.getPcab_fecemi());
        //cb_simbmon.setValue(objPedidoVentaCab.getPcab_mon().equals("001") ? "S/." : "$");
        cb_simbmon.setSelectedIndex(objPedidoVentaCab.getPcab_mon().equals("001") ? 0 : 1);
        db_tipcam.setValue(objPedidoVentaCab.getPcab_tipcam());
        txt_cli_id.setValue(objPedidoVentaCab.getCli_id());
        //txt_cli_razsoc.setValue(objPedidoVentaCab.getCli_des());
        txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
        txt_dni.setValue(objPedidoVentaCab.getPcab_nrodni());
        txt_ruc.setValue(objPedidoVentaCab.getPcab_nroruc());
        /*if (objCliFin == null) {
         txt_lincred.setValue("");
         txt_limdoc.setValue("");
         } else {
         txt_lincred.setValue(String.valueOf(objCliFin.getClifin_limcred()));
         txt_limdoc.setValue(String.valueOf(objCliFin.getClifin_limdoc()));
         }*/
        db_limcred.setValue(objCliFin == null ? 0.0 : objCliFin.getClifin_limcred());
        txt_limdoc.setValue(objCliFin == null ? 0 : objCliFin.getClifin_limdoc());
        //txt_saldo.setValue(String.valueOf(objPedidoVentaCab.getPcab_salcre()));
        db_saldo.setValue(0.0);
        txt_estado.setValue(objPedidoVentaCab.getPcab_estado() == 1 ? "ACTIVO" : "INACTIVO");
        txt_clidir_id.setValue(Long.parseLong(String.valueOf(objPedidoVentaCab.getClidir_id())));
        txt_clidir_direcc.setValue(objPedidoVentaCab.getPcab_dirent());
        txt_zon_id.setValue(objPedidoVentaCab.getZon_id());
        txt_zon_des.setValue(objPedidoVentaCab.getZon_des());
        txt_giro.setValue(objPedidoVentaCab.getPcab_giro());
        txt_idvendedor.setValue(objPedidoVentaCab.getVen_id());
        txt_nomvendedor.setValue(objPedidoVentaCab.getVen_des());
        txt_diavis.setValue(objPedidoVentaCab.getPcab_diavisdes());
        txt_id_listapre.setValue(Utilitarios.lpad(objPedidoVentaCab.getLp_id(), 4, "0"));
        txt_des_listapre.setValue(objPedidoVentaCab.getLp_des());
        txt_id_condven.setValue(Utilitarios.lpad(String.valueOf(objPedidoVentaCab.getCon_id()), 3, "0"));
        txt_des_condven.setValue(objPedidoVentaCab.getCond_des());
        txt_diaplazo.setValue(String.valueOf(objPedidoVentaCab.getCon_dpla()));
        cb_pronpag.setValue(objPedidoVentaCab.getPcab_ppago() == 1 ? "SI" : "NO");
        chk_modtip.setChecked(objPedidoVentaCab.isValortipcam());
        txt_facbol.setValue(objPedidoVentaCab.getPcab_facbol());
        txt_horent.setValue(objPedidoVentaCab.getPcab_horent());
        txt_usuadd.setValue(objPedidoVentaCab.getPcab_usuadd());
        d_fecadd.setValue(objPedidoVentaCab.getPcab_fecadd());
        txt_usumod.setValue(objPedidoVentaCab.getPcab_usumod());
        d_fecmod.setValue(objPedidoVentaCab.getPcab_fecmod());
    }

    public void llenarCamposDetalle() throws SQLException {
        String nropedkey = objPedidoVentaCab.getPcab_nroped();
        //String pv_ind = "C";
        objListaPedidoVentaDet = null;
        objListaPedidoVentaDet = objDaoGenPedVen.listaPedidoVentaDet(emp_id, suc_id, nropedkey/*, pv_ind*/);
        lst_pedven_detalle.setModel(objListaPedidoVentaDet);
    }

    public void llenarCamposProducto() throws SQLException {
        //String s_key = String.valueOf(objPedidoVentaDet.getPdet_item());
        txt_pedvendet_key.setValue(objPedidoVentaDet.getPdet_item());// ITEM
        txt_proid.setValue(objPedidoVentaDet.getPro_id());//COD Productos
        txt_prodes.setValue(objPedidoVentaDet.getPdet_prodes());//Descripcion Producto
        txt_det_idlispre.setValue(objPedidoVentaDet.getLp_id());//Código de lista
        txt_det_deslispre.setValue(objPedidoVentaDet.getLp_des());//Descripción de lista
        txt_cant.setValue(objPedidoVentaDet.getPdet_ent());//Cant.Entera
        txt_frac.setValue(objPedidoVentaDet.getPdet_frac());//Cant.Fraccion
        db_det_preven.setValue(objPedidoVentaDet.getPdet_punit());//P.Unit
        db_det_valvta.setValue(objPedidoVentaDet.getPdet_vventa());///Valor Venta
        //txt_odc_prounimanven.setValue(objPedidoVentaDet.getPro_unimanven());//UND.VTA
        chk_desman.setChecked(objPedidoVentaDet.isVal_descman());/// % Descuento
        db_det_pordes.setValue(objPedidoVentaDet.getPdet_dscto());/// % Descuento
        db_det_valdes.setValue(objPedidoVentaDet.getPdet_sdscto());// Descuento
        db_det_porigv.setValue(objPedidoVentaDet.getPdet_impto());//% Impuesto
        db_det_valigv.setValue(objPedidoVentaDet.getPdet_vimpto());//Impuesto
        db_det_total.setValue(objPedidoVentaDet.getPdet_pventa());// Total
        objProductos = new DaoProductos().getNomProductos(objPedidoVentaDet.getPro_id());
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_fecemi.getValue()), 101, objPedidoVentaDet.getPro_id(), "%%");
        if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > objProductos.getPro_scknofact()) {
            db_det_stock.setValue((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - objProductos.getPro_scknofact());
        } else {
            db_det_stock.setValue(0.00);
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaPedVen.setSelected(b_valida1);
        tab_mantenimientoPedVen.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaPedVen.setDisabled(b_valida1);
        tab_mantenimientoPedVen.setDisabled(b_valida2);
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
        tbbtn_btn_nuevopro.setDisabled(b_valida1);
        tbbtn_btn_editarpro.setDisabled(b_valida1);
        tbbtn_btn_eliminarpro.setDisabled(b_valida1);
        tbbtn_btn_deshacerpro.setDisabled(b_valida2);
        tbbtn_btn_guardarpro.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_nropedven.setValue("");
        d_fecemi.setValue(null);
        txt_dni.setValue("");
        txt_ruc.setValue("");
        chk_modtip.setChecked(false);
        db_tipcam.setValue(1.000);
        txt_cli_id.setValue(null);
        txt_cli_razsoc.setValue("");
        db_limcred.setValue(null);
        txt_limdoc.setValue(null);
        db_saldo.setValue(null);
        txt_estado.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_giro.setValue("");
        txt_idvendedor.setValue("");
        txt_nomvendedor.setValue("");
        txt_diavis.setValue("");
        txt_id_listapre.setValue("");
        txt_des_listapre.setValue("");
        txt_id_condven.setValue("");
        txt_des_condven.setValue("");
        txt_diaplazo.setValue("");
        txt_facbol.setValue(null);
        txt_horent.setValue("");
        db_por_dscesp.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_pedvendet_key.setValue(null);
        txt_proid.setValue("");
        txt_prodes.setValue("");
        txt_det_idlispre.setValue("");
        txt_det_deslispre.setValue("");
        txt_cant.setValue(null);
        txt_frac.setValue(null);
        db_det_preven.setValue(null);
        db_det_valvta.setValue(null);
        db_det_pordes.setValue(null);
        chk_desman.setChecked(false);
        db_det_valdes.setValue(null);
        db_det_porigv.setValue(null);
        db_det_valigv.setValue(null);
        db_det_total.setValue(null);
        db_det_stock.setValue(null);
    }

    public void limpiarDireccionNulo() {
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_idvendedor.setValue("");
        txt_nomvendedor.setValue("");
        txt_giro.setValue("");
        txt_diavis.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");

    }

    public void habilitaCampos(boolean b_valida1) {
        cb_tipoventa.setDisabled(b_valida1);
        chk_modtip.setDisabled(b_valida1);
        if (chk_modtip.isChecked()) {
            db_tipcam.setDisabled(false);
        } else {
            db_tipcam.setDisabled(true);
        }
        //db_tipcam.setDisabled(b_valida1);
        d_fecemi.setDisabled(b_valida1);
        cb_simbmon.setDisabled(b_valida1);
        txt_cli_id.setDisabled(b_valida1);
        txt_clidir_id.setDisabled(b_valida1);
        //txt_zon_id.setDisabled(b_valida1);
        txt_idvendedor.setDisabled(b_valida1);
        txt_id_listapre.setDisabled(b_valida1);
        txt_id_condven.setDisabled(b_valida1);
        cb_pronpag.setDisabled(b_valida1);
        db_por_dscesp.setDisabled(b_valida1);
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        txt_proid.setDisabled(b_valida1);
        //txt_det_idlispre.setDisabled(b_valida1);
        txt_cant.setDisabled(b_valida1);
        txt_frac.setDisabled(b_valida1);
        chk_desman.setDisabled(b_valida1);
        if (chk_desman.isChecked()) {
            db_det_pordes.setDisabled(false);
        } else {
            db_det_pordes.setDisabled(true);
        }
        db_det_stock.setDisabled(b_valida1);

    }

    public String verificar() {
        String s_resultado;
        if (txt_cli_id.getValue().isEmpty() || txt_cli_razsoc.getValue().isEmpty() || objCliente == null) {
            s_resultado = "El campo Cliente es obligatorio";
            foco = "Cliente";
        } else if (txt_clidir_id.getValue() == null || txt_clidir_id.getValue() == 0 || txt_clidir_direcc.getValue().isEmpty()) {
            s_resultado = "El campo Dirección es obligatorio";
            foco = "Direccion";
        } else if (txt_zon_id.getValue().isEmpty() || txt_zon_des.getValue().isEmpty()) {
            s_resultado = "El campo Zona es obligatorio";
            foco = "Zona";
        } else if (txt_idvendedor.getValue().isEmpty() || txt_nomvendedor.getValue().isEmpty()) {
            s_resultado = "El campo Vendedor es obligatorio";
            foco = "Vendedor";
        } else if (txt_id_listapre.getValue().isEmpty() || txt_des_listapre.getValue().isEmpty()) {
            s_resultado = "El campo Lista Precio es obligatorio";
            foco = "ListaPrecio";
        } else if (txt_id_condven.getValue().isEmpty() || txt_des_condven.getValue().isEmpty()) {
            s_resultado = "El campo Condición de Venta es obligatorio";
            foco = "Condicion";
        } else if (db_tipcam.getValue().doubleValue() <= 0.0) {
            s_resultado = "El campo Tipo de cambio es obligatorio";
            foco = "Moneda";
        } else {
            s_resultado = "";
        }
        return s_resultado;
    }

    public String verificarDetalle() {
        String valida = "";
        if (txt_proid.getValue().isEmpty()) {
            valida = "'Código de producto'";
            foco = "codigo";
        } /*else if (txt_det_idlispre.getValue() == null) {
         valida = "'Lista de Precio'";
         foco = "lista";
         }*/ else if (txt_cant.doubleValue() == 0.0 && txt_frac.intValue() == 0) {
            valida = "'Cantidad'";
            foco = "cantidad";
        } else if (txt_cant.getValue() == null) {
            valida = "'Cantidad'";
            foco = "cantidad";
        } else if (txt_frac.getValue() == null) {
            valida = "'Fracción'";
            foco = "fraccion";
        } else if (db_det_pordes.getValue() < 0) {
            valida = "'Descuento' - Debe ser mayor o igual a 0";
            foco = "descuento";
        }
        return valida;
    }

    public void validacalculo() throws SQLException {
        objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
        double precioent, preciofrac, val_vta, val_desc, val_igv;
        int unipresven = objProductos.getPro_presminven();
        precioent = db_det_preven.getValue();
        preciofrac = db_det_preven.getValue() / unipresven;
        val_vta = (txt_cant.getValue() * precioent) + (txt_frac.getValue() * preciofrac);
        db_det_valvta.setValue(val_vta);
        val_desc = val_vta * db_det_pordes.getValue() / 100;
        db_det_valdes.setValue(val_desc);
        /*String tipimp = objProductos.getPro_condimp();
         if (tipimp.equals("A")) {
         val_igv = (val_vta - val_desc) * db_det_porigv.getValue() / 100;
         } else {
         val_igv = 0;
         }*/
        val_igv = (val_vta - val_desc) * db_det_porigv.getValue() / 100;
        db_det_valigv.setValue(val_igv);
        db_det_total.setValue(val_vta - val_desc + val_igv);
    }

    public void recalcularLista() throws SQLException {
        double precioent, preciofrac, val_vta, por_desc, val_desc, val_igv, val_total, impuesto;
        String pro_id;
        int unipresven, cantidad, fraccion;
        for (int i = 0; i < objListaPedidoVentaDet.getSize(); i++) {
            pro_id = objListaPedidoVentaDet.get(i).getPro_id();
            unipresven = objListaPedidoVentaDet.get(i).getPdet_unipre();
            val_igv = objListaPedidoVentaDet.get(i).getPdet_impto();
            cantidad = objListaPedidoVentaDet.get(i).getPdet_ent();
            fraccion = objListaPedidoVentaDet.get(i).getPdet_frac();

            objPreciosVenta = new DaoPrecios().getPrecioxLpVenta(emp_id, suc_id, pro_id, txt_id_listapre.getValue());
            if (val_igv != 0) {
                impuesto = Double.parseDouble(new DaoPedVen().obtieneImpuesto(1));
            } else {
                impuesto = 0;
            }
            if (txt_id_condven.getValue().equals("001")) {
                por_desc = objPreciosVenta.getPre_valvent();
            } else {
                por_desc = objPreciosVenta.getPre_igv();
            }
            precioent = objPreciosVenta.getPre_venta() / (1 + (impuesto / 100));
            preciofrac = precioent / unipresven;
            val_vta = (cantidad * precioent) + (fraccion * preciofrac);
            val_desc = val_vta * por_desc / 100;
            val_igv = (val_vta - val_desc) * impuesto / 100;
            val_total = val_vta - val_desc + val_igv;

            objListaPedidoVentaDet.get(i).setLp_id(txt_id_listapre.getValue());
            objListaPedidoVentaDet.get(i).setLp_des(txt_des_listapre.getValue());
            objListaPedidoVentaDet.get(i).setPdet_punit(precioent);
            objListaPedidoVentaDet.get(i).setPdet_vventa(val_vta);
            objListaPedidoVentaDet.get(i).setPdet_dscto(por_desc);
            objListaPedidoVentaDet.get(i).setPdet_sdscto(val_desc);
            objListaPedidoVentaDet.get(i).setPdet_impto(impuesto);
            objListaPedidoVentaDet.get(i).setPdet_vimpto(val_igv);
            objListaPedidoVentaDet.get(i).setPdet_pventa(val_total);
        }

        lst_pedven_detalle.setModel(objListaPedidoVentaDet);
    }

    public boolean validaIngresoProducto(String pro_id) {
        int i = 0;
        int cantDet = objListaPedidoVentaDet.getSize();
        boolean validaIngreso = true;
        while (i < cantDet && validaIngreso) {
            if (objListaPedidoVentaDet.get(i).getPro_id().equals(pro_id)) {
                validaIngreso = false;
            }
            i++;
        }
        return validaIngreso;
    }

    public double[] calculosTotalLista() {
        int i;
        int sumRec = 0;
        int sumOk = 0;
        int sumPed;
        double valRec = 0;
        double valOk = 0;
        double valPed;
        double data[] = new double[6];
        for (i = 0; i < objListaPedidoVentaCab.getSize(); i++) {
            if (objListaPedidoVentaCab.get(i).getPcab_situacion() == 3 || objListaPedidoVentaCab.get(i).getPcab_situacion() == 4) {
                sumRec++;
                valRec += ((PedidoVentaCab) objListaPedidoVentaCab.get(i)).getPcab_totped();
            } else {
                sumOk++;
                valOk += ((PedidoVentaCab) objListaPedidoVentaCab.get(i)).getPcab_totped();
            }
        }
        sumPed = sumOk + sumRec;
        valPed = valOk + valRec;
        data[0] = sumPed;
        data[1] = sumOk;
        data[2] = sumRec;
        data[3] = valPed;
        data[4] = valOk;
        data[5] = valRec;
        return data;
    }

    public double[] calculosTotal() {
        int i;
        double data[] = new double[1];
        for (i = 0; i < objListaPedidoVentaDet.getSize(); i++) {
            data[0] = data[0] + ((PedidoVentaDet) objListaPedidoVentaDet.get(i)).getPdet_pventa();
        }
        return data;
    }

    public String validaFraccionVenta() throws SQLException {
        String fraccionProd = objProductos.getPro_frac();
        int unidadpres = objProductos.getPro_presminven();
        int frac1 = 0, frac2 = 0;
        String msg, nomfrac = "";
        if (fraccionProd.equals("E")) {
            frac1 = 0;
            nomfrac = "ENTEROS";
        } else if (fraccionProd.equals("M")) {
            frac1 = unidadpres / 2;
            nomfrac = "MEDIOS";
        } else if (fraccionProd.equals("C")) {
            frac1 = unidadpres / 4;
            nomfrac = "CUARTOS";
        } /*else if (fraccionProd.equals("T")) {
         frac1 = unidadpres / 2;
         frac2 = unidadpres / 4;
         }*/

        if (txt_frac.getValue() == frac1 || txt_frac.getValue() == frac2 || txt_frac.getValue() == 0 || fraccionProd.equals("T")) {
            msg = "";
        } else {
            /*if (fraccionProd.equals("T")) {
             msg = "El producto solo se puede fraccionar en MEDIOS Y CUARTOS";
             } else {*/
            msg = "El producto solo se puede fraccionar en " + nomfrac;
            //}
        }
        return msg;
    }

    public void limpiarCamposTotalesLista() {
        db_sumped.setValue(null);
        db_sumok.setValue(null);
        db_sumrec.setValue(null);
        db_valped.setValue(null);
        db_valok.setValue(null);
        db_valrec.setValue(null);
    }

    public void limpiarCamposTotales() {
        txt_total.setValue(null);
    }

    public void llenarCamposTotalesLista() {
        double data[] = calculosTotalLista();
        db_sumped.setValue(data[0]);
        db_sumok.setValue(data[1]);
        db_sumrec.setValue(data[2]);
        db_valped.setValue(data[3]);
        db_valok.setValue(data[4]);
        db_valrec.setValue(data[5]);
    }

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_total.setValue(data[0]);
    }

    public void limpiarLista() {
        objListaPedidoVentaCab = null;
        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        lst_pedidoventa.setModel(objListaPedidoVentaCab);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
