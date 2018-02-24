package org.me.gj.controller.facturacion.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoTipoVenta;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.facturacion.procesos.PedidoVentaCab;
import org.me.gj.model.facturacion.procesos.PedidoVentaDet;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.DaoUtil;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerGenNotaesVen extends SelectorComposer /*implements InterfaceControllers*/ {

    //Componentes Web
    @Wire
    Combobox cb_simbmon, cb_tipventa, cb_tipoventa, cb_pronpag, cb_notaes, cb_serie, cb_almacen;
    @Wire
    Datebox d_fecha, d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Doublebox db_por_lispre, db_por_convta, db_tipcam, txt_total,
            db_por_dsccli, db_por_dscesp, db_por_desman, //----->descuentos
            db_det_preven, db_det_valvta, db_det_valdes, db_det_pordes, db_det_porigv,
            db_det_valigv, db_det_total, db_det_stock, txt_cant,db_limcred,db_saldo; //----->datos de detalle       
    @Wire
    Textbox txt_nropedven, txt_situacion, txt_dni, txt_ruc,
            txt_proid, txt_cli_id, txt_cli_razsoc, /*txt_lincred, txt_limdoc, txt_saldo,*/ txt_estado, txt_clidir_direcc,
            txt_zon_id, txt_zon_des, txt_giro, txt_desgiro, txt_idvendedor, txt_nomvendedor, txt_diavis,
            txt_id_listapre, txt_des_listapre, txt_id_condven, txt_des_condven, txt_diaplazo,
            txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_usuadd, txt_usumod;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Listbox lst_pedidoventa, lst_pedven_detalle;
    @Wire
    Button btn_generar;
    @Wire
    Intbox txt_pedvendet_key, txt_frac,txt_limdoc;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_modtip, chk_desman, chk_selecAll;//------>mantenimiento
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPeriodos;
    ListModelList<Moneda> objlstMonedas;
    ListModelList<TipoVenta> objlstTipoventa;
    ListModelList<PedidoVentaCab> objListaPedidoVentaCab, objListaCabAux;
    ListModelList<PedidoVentaDet> objListaPedidoVentaDet, objListaEliminacionPedidoVentaDet;
    ListModelList<Guias> objlstGuias;
    ListModelList<Numeracion> objlstNumeracion;
    ListModelList<Almacenes> objlstAlmacen;
    PedidoVentaCab objPedidoVentaCab;
    PedidoVentaDet objPedidoVentaDet;
    ParametrosSalida objParametrosSalida;
    DaoPedVen objDaoGenPedVen;
    DaoAccesos objDaoAccesos;
    DaoCliente objDaoCliente;
    DaoCierreDia objDaoCierreDia;
    DaoUtil objDaoUtil;
    DaoPrecios objDaoPrecioVen;
    DaoProductos objDaoProducto;
    DaoManNotaES objDaoGuias;
    DaoNumeracion objDaoNumeracion;
    DaoTipoVenta objDaoTipoVenta;
    DaoMoneda objDaoMoneda;
    DaoManPer objDaoManPer;
    DaoAlmacenes objDaoAlmacen = new DaoAlmacenes();
    Accesos objAccesos;
    Productos objProductos;
    Cliente objCliente;
    CliFinanciero objCliFin;
    Guias objGuias;
    Numeracion objNumeracion;
    //Variables publicas
    int i_sel;
    int i_selCab, i_selDet;
    int emp_id;
    int suc_id;
    int monid;
    String s_estado = "Q";
    String s_estado_detalle = "Q";
    String s_mensaje;
    String s_nroPedidoCompra;
    String modoEjecucion;
    String fechaActual;
    String foco = "";
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenNotaesVen.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoCliente = new DaoCliente();
        objCliente = new Cliente();
        objGuias = new Guias();
        objNumeracion = new Numeracion();
        objDaoGenPedVen = new DaoPedVen();
        objPedidoVentaCab = new PedidoVentaCab();
        objPedidoVentaDet = new PedidoVentaDet();
        objParametrosSalida = new ParametrosSalida();
        objDaoCierreDia = new DaoCierreDia();
        objDaoTipoVenta = new DaoTipoVenta();
        objDaoMoneda = new DaoMoneda();
        objDaoManPer = new DaoManPer();
        objDaoUtil = new DaoUtil();
        objDaoGuias = new DaoManNotaES();
        objDaoNumeracion = new DaoNumeracion();
        s_estado = "Q";
        s_estado_detalle = "Q";
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objlstManPeriodos = new ListModelList();
        objlstManPeriodos = objDaoManPer.listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        db_tipcam.setValue(1.00);
        //carga tipo de venta
        objlstTipoventa = new ListModelList();
        objlstTipoventa = objDaoTipoVenta.listaTipoVentas(1);
        cb_tipventa.setModel(objlstTipoventa);
        objlstTipoventa.add(new TipoVenta(100, ""));
        cb_tipoventa.setModel(objlstTipoventa);
        //carga monedas
        objlstMonedas = new ListModelList();
        objlstMonedas = objDaoMoneda.listaMonedas(1);
        cb_simbmon.setModel(objlstMonedas);
        //carga guias
        objlstGuias = new ListModelList<Guias>();
        objlstGuias = objDaoGuias.listaGuias(3);
        cb_notaes.setModel(objlstGuias);
        objlstGuias.add(new Guias(100, ""));
        //carga serie
        /*objlstNumeracion = new ListModelList<Numeracion>();
         objlstNumeracion = objDaoNumeracion.listaSeriesSinFiltro();
         cb_serie.setModel(objlstNumeracion);*/

        //carga pedidos
        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        objListaPedidoVentaCab = objDaoGenPedVen.listaPedidoVenCab(emp_id, suc_id, "1", fechaActual, fechaActual, 1);
        lst_pedidoventa.setModel(objListaPedidoVentaCab);
        //carga almacen
        /*objlstAlmacen = new ListModelList<Almacenes>();
         objlstAlmacen = objDaoAlmacen.lstAlmacenes(1);
         cb_almacen.setModel(objlstAlmacen);*/
        objlstAlmacen = new ListModelList<Almacenes>();
        objlstAlmacen = objDaoAlmacen.busquedaAlmacenes("0101", 1, 1);
        cb_almacen.setModel(objlstAlmacen);
        chk_selecAll.setChecked(false);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40202000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Generación de Nota E/S - Pedido Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado Procesos Generación de Nota E/S - Pedido Venta con el rol: USUARIO NORMAL");
        }
    }

    @Listen("onSelect=#cb_tipventa")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        Date fecha_emisioni = d_fecha.getValue();
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "";
        } else {
            f_emisioni = sdf.format(d_fecha.getValue());
        }
        //objListaPedidoVentaCab = new ListModelList();
        //objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
         objListaPedidoVentaCab = objDaoGenPedVen.busquedaPedidoVentaxTipo(emp_id, suc_id, f_emisioni, "%%", "%%", 1, 1, "100".equals(cb_tipventa.getSelectedItem().getValue().toString()) ? "%%" : Utilitarios.lpad(cb_tipventa.getSelectedItem().getValue().toString(), 3, "0"));
        //Validar la tabla sin registro
        if (objListaPedidoVentaCab.getSize() > 0) {
            lst_pedidoventa.setModel(objListaPedidoVentaCab);
        } else {
            /*objListaPedidoVentaCab = null;
             lst_pedidoventa.setModel(objListaPedidoVentaCab);*/
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
        llenarCampos();
        llenarCamposDetalle();
        llenarCamposTotales();
    }

    @Listen("onSelect=#lst_pedven_detalle")
    public void seleccionaRegistroDetalle() throws SQLException {
        objPedidoVentaDet = lst_pedven_detalle.getSelectedItem().getValue();
        llenarCamposProducto();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objPedidoVentaDet.getPro_id()).toString());
    }

    @Listen("onClick=#btn_generar")
    public void generarNotaes() throws SQLException {
        String s_valida;
        s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("Nota")) {
                            cb_notaes.select();
                            cb_notaes.focus();
                        } else if (foco.equals("Serie")) {
                            cb_serie.select();
                            cb_serie.focus();
                        } else if (foco.equals("Almacen")) {
                            cb_almacen.select();
                            cb_almacen.focus();
                        } else if (foco.equals("Fecha")) {
                            d_fecha.focus();
                        }
                    }
                }
            });
        } else if (objListaPedidoVentaCab.getSize() < 1 || objListaPedidoVentaCab.isEmpty() || objListaPedidoVentaCab == null) {
            Messagebox.show("No hay pedidos para procesar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objListaPedidoVentaCab.getSize(); j++) {
                if (objListaPedidoVentaCab.get(j).isValSelec()) {
                    i = i + 1;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            ParametrosSalida objParam;
                            objParam = objDaoUtil.ValidaProcesos(emp_id, suc_id);
                            if (objParam.getFlagIndicador() == 0) {
                                if (objParam.getMsgValidacion().equals("N")) {
                                    int x = 0;
                                    for (int j = 0; j < objListaPedidoVentaCab.getSize(); j++) {
                                        if (objListaPedidoVentaCab.get(j).isValSelec()) {
                                            x = x + 1;
                                            break;
                                        }
                                    }
                                    if (x <= 0) {
                                        Messagebox.show("Debe seleccionar una nota de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    } else {
                                        s_mensaje = "¿Está seguro que desea generar las notas seleccionadas?";
                                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                                Messagebox.QUESTION, new EventListener() {
                                                    @Override
                                                    public void onEvent(Event event) throws Exception {
                                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                            objListaCabAux = new ListModelList<PedidoVentaCab>();
                                                            for (int i = 0; i < objListaPedidoVentaCab.size(); i++) {
                                                                if (objListaPedidoVentaCab.get(i).getPcab_situacion() == 1 && objListaPedidoVentaCab.get(i).isValSelec()) {
                                                                    //objParam = objDaoUtil.ValidaPlanillas(emp_id, suc_id);
                                                                    objListaCabAux.add(objListaPedidoVentaCab.get(i));
                                                                }
                                                            }
                                                            /*int tiponota = cb_tipventa.getSelectedItem().getValue();
                                                             int nota = cb_notaes.getSelectedItem().getValue();
                                                             int serie = Integer.parseInt(cb_serie.getSelectedItem().getValue().toString());
                                                             int almacen = Integer.parseInt(cb_almacen.getSelectedItem().getValue().toString());*/
                                                            int tiponota = cb_tipventa.getSelectedItem().getValue();
                                                            String nota = Utilitarios.lpad(cb_notaes.getSelectedItem().getValue().toString(), 3, "0");
                                                            String serie = cb_serie.getSelectedItem().getValue().toString();
                                                            String almacen = Utilitarios.lpad(cb_almacen.getSelectedItem().getValue().toString(), 4, "0");
                                                            Date fecha_emisioni = d_fecha.getValue();
                                                            String usuario = objUsuCredential.getCuenta();
                                                            ParametrosSalida objParam2 = objDaoGenPedVen.generarNotaVenta(tiponota, nota, serie, almacen, emp_id, suc_id, fecha_emisioni, getPedidoVentaCab(objListaCabAux), usuario);
                                                            if (objParam2.getFlagIndicador() == 0) {
                                                                //objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
                                                                objListaPedidoVentaCab.clear();
                                                                //objListaCabAux = new ListModelList<PedidoVentaCab>();
                                                                //objListaCabAux = null;
                                                                objListaCabAux.clear();
                                                                //lst_pedidoventa.setModel(objListaCabAux);
                                                                lst_pedidoventa.setModel(objListaPedidoVentaCab);
                                                                Messagebox.show(objParam2.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                            }
                                                            busquedaRegistros();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            } else {
                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    }
                }
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onSelect=#cb_notaes")
    public void valida_tipguia() throws SQLException {
        Guias objGuia = new DaoManNotaES().BusquedaGuia(Utilitarios.lpad(cb_notaes.getSelectedItem().getValue().toString(), 3, "0"));
        if (objGuia.getCodigo() == null) {
            Messagebox.show("El código de la nota de E/S no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                LimpiarListaSerie();
                            }
                        }
                    });
        } else {
            LimpiarListaSerie();
            objlstNumeracion = objDaoNumeracion.listaSeries(Integer.parseInt(cb_notaes.getSelectedItem().getValue().toString()));
            cb_serie.setModel(objlstNumeracion);
            cb_serie.setDisabled(false);
            cb_serie.focus();
        }
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objListaPedidoVentaCab.isEmpty()) {
            Messagebox.show("No hay registro de pedidos emitidos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objListaPedidoVentaCab.getSize(); i++) {
                if (objListaPedidoVentaCab.get(i).getPcab_situacion() == 1) {
                    objListaPedidoVentaCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_pedidoventa.setModel(objListaPedidoVentaCab);
        }
    }

    @Listen("onSeleccion=#lst_pedidoventa")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objListaPedidoVentaCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_pedidoventa.setModel(objListaPedidoVentaCab);
    }

    @Listen("onOk=#cb_serie")
    public void onOKSerie() throws SQLException {
        d_fecha.focus();
    }

    //Eventos Otros
    public void limpiarCampos() {
        txt_nropedven.setValue("");
        txt_dni.setValue("");
        txt_ruc.setValue("");
        chk_modtip.setChecked(false);
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
        db_det_valvta.setText(null);
        db_det_pordes.setValue(null);
        chk_desman.setChecked(false);
        db_det_valdes.setValue(null);
        db_det_porigv.setValue(null);
        db_det_valigv.setValue(null);
        db_det_total.setValue(null);
        db_det_stock.setValue(null);
    }

    public void limpiarCamposTotales() {
        txt_total.setValue(null);
    }

    public void llenarCampos() throws SQLException {
        objCliFin = objDaoCliente.getClienteFin(objPedidoVentaCab.getCli_id().toString(), emp_id, suc_id);
        txt_nropedven.setValue(objPedidoVentaCab.getPcab_nroped());
        cb_tipoventa.setValue(objPedidoVentaCab.getTip_ventades());
        txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des());
        d_fecemi.setValue(objPedidoVentaCab.getPcab_fecemi());
        cb_simbmon.setValue(objPedidoVentaCab.getPcab_mon().equals("001") ? "S/." : "$");
        db_tipcam.setValue(objPedidoVentaCab.getPcab_tipcam());
        txt_cli_id.setValue(objPedidoVentaCab.getCli_id());
        txt_cli_razsoc.setValue(objPedidoVentaCab.getCli_des());
        txt_dni.setValue(objPedidoVentaCab.getPcab_nrodni());
        txt_ruc.setValue(objPedidoVentaCab.getPcab_nroruc());
        db_limcred.setValue(objCliFin == null ? 0.0 : objCliFin.getClifin_limcred());
        txt_limdoc.setValue(objCliFin == null ? 0 : objCliFin.getClifin_limdoc());
        //txt_saldo.setValue(String.valueOf(objPedidoVentaCab.getPcab_salcre()));
        db_saldo.setValue(0.0);
        /*db_limcred.setValue(String.valueOf(objPedidoVentaCab.getPcab_limcre()));
        txt_limdoc.setValue(String.valueOf(objPedidoVentaCab.getPcab_limdoc()));
        db_saldo.setValue(String.valueOf(objPedidoVentaCab.getPcab_salcre()));*/
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

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_total.setValue(data[0]);
    }

    public void llenarCamposProducto() throws SQLException {
        txt_pedvendet_key.setValue(objPedidoVentaDet.getPdet_item());// ITEM
        txt_proid.setValue(objPedidoVentaDet.getPro_id());//COD Productos
        txt_prodes.setValue(objPedidoVentaDet.getPdet_prodes());//Descripcion Producto
        txt_det_idlispre.setValue(objPedidoVentaDet.getLp_id());//Código de lista
        txt_det_deslispre.setValue(objPedidoVentaDet.getLp_des());//Descripción de lista
        txt_cant.setValue(objPedidoVentaDet.getPdet_ent());//Cant.Entera
        txt_frac.setValue(objPedidoVentaDet.getPdet_frac());//Cant.Fraccion
        db_det_preven.setValue(objPedidoVentaDet.getPdet_punit());//P.Unit
        db_det_valvta.setValue(objPedidoVentaDet.getPdet_vventa());///Valor Venta
        chk_desman.setChecked(objPedidoVentaDet.isVal_descman());/// % Descuento
        db_det_pordes.setValue(objPedidoVentaDet.getPdet_dscto());/// % Descuento
        db_det_valdes.setValue(objPedidoVentaDet.getPdet_sdscto());// Descuento
        db_det_porigv.setValue(objPedidoVentaDet.getPdet_impto());//% Impuesto
        db_det_valigv.setValue(objPedidoVentaDet.getPdet_vimpto());//Impuesto
        db_det_total.setValue(objPedidoVentaDet.getPdet_pventa());// Total
        objProductos = new DaoProductos().getNomProductos(objPedidoVentaDet.getPro_id());
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_fecemi.getValue()), 101, objPedidoVentaDet.getPro_id(), "%%");
        if (objParametrosSalida.getCantStocks() > objProductos.getPro_scknofact()) {
            db_det_stock.setValue((objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
        } else {
            db_det_stock.setValue(0.00);
        }
    }

    public double[] calculosTotal() {
        int i;
        double data[] = new double[1];
        for (i = 0; i < objListaPedidoVentaDet.getSize(); i++) {
            data[0] = data[0] + ((PedidoVentaDet) objListaPedidoVentaDet.get(i)).getPdet_pventa();
        }
        return data;
    }

    public void LimpiarListaSerie() {
        //limpio mi lista serie
        objlstNumeracion = null;
        objlstNumeracion = new ListModelList<Numeracion>();
        cb_serie.setValue("");
        cb_serie.setModel(objlstNumeracion);
    }

    public void LimpiarLista() {
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

    public String verificar() {
        String s_resultado;
        if (cb_notaes.getValue().isEmpty()) {
            s_resultado = "El campo Nota E/S es obligatorio";
            foco = "Nota";
        } else if (cb_serie.getValue().isEmpty()) {
            s_resultado = "El campo Serie es obligatorio";
            foco = "Serie";
        } else if (cb_almacen.getValue().isEmpty()) {
            s_resultado = "El campo Almacen es obligatorio";
            foco = "Almacen";
        } else if (d_fecha.getValue() == null) {
            s_resultado = "El campo Fecha de Emisión es obligatorio";
            foco = "Fecha";
        } else {
            s_resultado = "";
        }
        return s_resultado;
    }

    public Object[][] getPedidoVentaCab(ListModelList<PedidoVentaCab> objLista) {
        //Creamos el arreglo con los detalles
        Object[][] listaPedidoVentaCab = new Object[objLista.size()][46];
        //Barreros la lista que contiene los datos a grabar
        for (int i = 0; i < objLista.size(); i++) {
            //Llenamos la lista con los datos
            listaPedidoVentaCab[i][0] = objLista.get(i).getPcab_nroped();
            listaPedidoVentaCab[i][1] = objLista.get(i).getPcab_fecemi();
            listaPedidoVentaCab[i][2] = objLista.get(i).getPcab_periodo();
            listaPedidoVentaCab[i][3] = objLista.get(i).getEmp_id();
            listaPedidoVentaCab[i][4] = objLista.get(i).getSuc_id();
            listaPedidoVentaCab[i][5] = objLista.get(i).getPcab_situacion();
            listaPedidoVentaCab[i][6] = objLista.get(i).getPcab_fecent();
            listaPedidoVentaCab[i][7] = objLista.get(i).getZon_id();
            listaPedidoVentaCab[i][8] = objLista.get(i).getPcab_motrec();
            listaPedidoVentaCab[i][9] = objLista.get(i).getClidir_id();
            listaPedidoVentaCab[i][10] = objLista.get(i).getPcab_dirent();
            listaPedidoVentaCab[i][11] = objLista.get(i).getCli_id();
            listaPedidoVentaCab[i][12] = objLista.get(i).getPcab_canid();
            listaPedidoVentaCab[i][13] = objLista.get(i).getVen_id();
            listaPedidoVentaCab[i][14] = objLista.get(i).getSup_id();
            listaPedidoVentaCab[i][15] = objLista.get(i).getPcab_facbol();
            listaPedidoVentaCab[i][16] = objLista.get(i).getCon_id();
            listaPedidoVentaCab[i][17] = objLista.get(i).getPcab_mon();
            listaPedidoVentaCab[i][18] = objLista.get(i).getLp_id();
            listaPedidoVentaCab[i][19] = objLista.get(i).getPcab_tipcam();
            listaPedidoVentaCab[i][20] = objLista.get(i).getPcab_limcre();
            listaPedidoVentaCab[i][21] = objLista.get(i).getPcab_limdoc();
            listaPedidoVentaCab[i][22] = objLista.get(i).getPcab_salcre();
            listaPedidoVentaCab[i][23] = objLista.get(i).getPcab_nrodni();
            listaPedidoVentaCab[i][24] = objLista.get(i).getPcab_nroruc();
            listaPedidoVentaCab[i][25] = objLista.get(i).getPcab_totped();
            listaPedidoVentaCab[i][26] = objLista.get(i).getPcab_diavis();
            //listaPedidoVentaCab[i][27] = objLista.get(i).getPcab_horent();
            listaPedidoVentaCab[i][27] = objLista.get(i).getPcab_gpslat();
            listaPedidoVentaCab[i][28] = objLista.get(i).getPcab_gpslon();
            listaPedidoVentaCab[i][29] = objLista.get(i).getPcab_totper();
            listaPedidoVentaCab[i][30] = objLista.get(i).getPcab_aprcon();
            //listaPedidoVentaCab[i][32] = objLista.get(i).getPcab_aprglo();
            //listaPedidoVentaCab[i][33] = objLista.get(i).getPcab_docref();
            listaPedidoVentaCab[i][31] = objLista.get(i).getPcab_usuadd();
            listaPedidoVentaCab[i][32] = objLista.get(i).getPcab_fecadd();
            //listaPedidoVentaCab[i][36] = objLista.get(i).getPcab_usumod();
            //listaPedidoVentaCab[i][37] = objLista.get(i).getPcab_fecmod();
            listaPedidoVentaCab[i][33] = objLista.get(i).getPcab_giro();
            listaPedidoVentaCab[i][34] = objLista.get(i).getPcab_ppago();
            listaPedidoVentaCab[i][35] = objLista.get(i).getPcab_tipven();
            listaPedidoVentaCab[i][36] = objLista.get(i).getPcab_modtipcam();
            listaPedidoVentaCab[i][37] = objLista.get(i).getCli_des();
            listaPedidoVentaCab[i][38] = objLista.get(i).getVen_des();
            listaPedidoVentaCab[i][39] = objLista.get(i).getPcab_situacion_des();
            listaPedidoVentaCab[i][40] = objLista.get(i).getLp_des();
            listaPedidoVentaCab[i][41] = objLista.get(i).getCond_des();
            listaPedidoVentaCab[i][42] = objLista.get(i).getCon_dpla();
            listaPedidoVentaCab[i][43] = objLista.get(i).getZon_des();
            listaPedidoVentaCab[i][44] = objLista.get(i).getTip_ventades();
            //listaPedidoVentaCab[i][50] = objLista.get(i).getPcab_mon();
            //listaPedidoVentaCab[i][51] = objLista.get(i).getPcab_diavisdes();
            //listaPedidoVentaCab[i][52] = objLista.get(i).getPcab_motrec_des();
            listaPedidoVentaCab[i][45] = objLista.get(i).getIndicador();
            /*listaPedidoVentaCab[i][46] = objLista.get(i).get;
            listaPedidoVentaCab[i][47] = objLista.get(i).getIndicador();*/
        }
        return listaPedidoVentaCab;
    }

    /*//metodos sin utilizar
     public void llenaRegistros() throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void botonEliminar() throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void botonNuevo() throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void botonDeshacer() {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void botonEditar() throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void OnChange() {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void validaBusqueda(InputEvent event) throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void habilitaTab(boolean b_valida1, boolean b_valida2) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void habilitaCampos(boolean b_valida1) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     public void botonGuardar() throws SQLException {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }*/
}
