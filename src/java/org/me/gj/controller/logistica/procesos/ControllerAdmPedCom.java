package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.PedidoCompraCab;
import org.me.gj.model.logistica.procesos.PedidoCompraDet;
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

public class ControllerAdmPedCom extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_aprobar, tbbtn_btn_rechazar;
    @Wire
    Listbox lst_admpedcom;
    @Wire
    Textbox txt_filtro_ped, txt_provid, txt_provdes, txt_usuadd, txt_usumod;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod, d_fecemi;
    @Wire
    Button btn_buscarordenes;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Combobox cb_cabecera_periodo, cb_alm_origen, cb_alm_destino, cb_monpedcom;
    @Wire
    Doublebox db_tipcam, txt_canent, txt_precom, txt_pordes, txt_valafe,
            txt_valdes, txt_porimp, txt_valimp, txt_valexo, txt_preven,
            txt_totvalafe, txt_totvalexo, txt_totvaldes, txt_totvalimp,
            txt_totpreven, txt_pesounit, txt_voluunit, txt_totpeso,
            txt_totvolumen, txt_totpordes;
    @Wire
    Datebox d_feccad, d_fecrec;
    @Wire
    Listbox lst_admpedidocompra_detalle;
    @Wire
    Intbox txt_prodkey, txt_pedcomdet_key;
    @Wire
    Textbox txt_nropedcom, txt_est, txt_proidman, txt_prodesman, txt_lugent,
            txt_glosa, txt_lisprecom, txt_lisprecomdes, txt_forid, txt_fordes,
            txt_situacion, txt_peso_und, txt_pedcomglo, txt_prounimanven,
            txt_proid, txt_prodes, txt_vol_und, txt_pedubi, txt_peddes;
    //Instancias de Objetos
    ListModelList<PedidoCompraCab> objListaPedidoCompraCab;
    ListModelList<PedidoCompraDet> objListaPedidoCompraDet, objListaEliminacionPedidoCompraDet;
    DaoPedCom objDaoGenPedCom;
    PedidoCompraCab objPedidoCompraCab;
    PedidoCompraDet objPedidoCompraDet;
    Productos objProductos;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    //Variables publicas
    String fechaActual, s_mensaje;
    int i_selCab;
    int emp_id, suc_id;
    int i_sel;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAdmPedCom.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#div_admpedcom")
    public void llenaRegistros() throws SQLException {
        objDaoGenPedCom = new DaoPedCom();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        //String prov_id = "%%";
        fechaActual = Utilitarios.hoyAsString();
        txt_filtro_ped.focus();
        objListaPedidoCompraCab = objDaoGenPedCom.listaPedidosCompras();
        lst_admpedcom.setModel(objListaPedidoCompraCab);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10210000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado en Procesos Administración de Pedido de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado en Procesos Administración de Pedido de Compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_aprobar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a aprobar un Pedido de Compra");
        } else {
            tbbtn_btn_aprobar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a aprobar un Pedido de Compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_rechazar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a rechazar un Pedido de Compra");
        } else {
            tbbtn_btn_rechazar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a rechazar un Pedido de Compra");
        }
    }

    @Listen("onSelect=#lst_admpedcom")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos la data
        limpiarCampos();
        limpiarCamposDetalle();
        objPedidoCompraCab = (PedidoCompraCab) lst_admpedcom.getSelectedItem().getValue();
        if (objPedidoCompraCab == null) {
            objPedidoCompraCab = objListaPedidoCompraCab.get(i_sel + 1);
        }
        i_selCab = lst_admpedcom.getSelectedIndex();
        llenarCampos(objPedidoCompraCab);
        llenarCamposDetalle();
        txt_peddes.setValue("");
        txt_pedubi.setValue("");
        txt_usuadd.setValue(objPedidoCompraCab.getPedcom_usuadd());
        d_fecadd.setValue(objPedidoCompraCab.getPedcom_fecadd());
        txt_usumod.setValue(objPedidoCompraCab.getPedcom_usumod());
        d_fecmod.setValue(objPedidoCompraCab.getPedcom_fecmod());
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | selecciono el registro con el codigo ").append(objPedidoCompraCab.getPedcom_id()).toString());

    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objListaPedidoCompraCab == null || objListaPedidoCompraCab.isEmpty()) {
            Messagebox.show("No hay pedidos de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objListaPedidoCompraCab.getSize(); i++) {
                if (objListaPedidoCompraCab.get(i).getPedcom_sit() == 1) {
                    objListaPedidoCompraCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_admpedcom.setModel(objListaPedidoCompraCab);
        }
    }

    @Listen("onSeleccion=#lst_admpedcom")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objListaPedidoCompraCab.get(item.getIndex()).getPedcom_sit() == 2 || objListaPedidoCompraCab.get(item.getIndex()).getPedcom_sit() == 3) {
            Messagebox.show("El pedido de compra ya no puede ser procesado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objListaPedidoCompraCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_admpedcom.setModel(objListaPedidoCompraCab);
        }
    }

    @Listen("onSelect=#lst_admpedidocompra_detalle")
    public void seleccionaRegistroDetalle() throws SQLException {
        objPedidoCompraDet = lst_admpedidocompra_detalle.getSelectedItem().getValue();
        llenarCamposProducto();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objPedidoCompraDet.getPro_id()).toString());
    }

    @Listen("onClick=#tbbtn_btn_aprobar")
    public void AprobarPedidoCompra() throws SQLException {
        if (objListaPedidoCompraCab == null || objListaPedidoCompraCab.isEmpty()) {
            Messagebox.show("No hay pedidos de compra por aprobar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objListaPedidoCompraCab.getSize(); j++) {
                if (objListaPedidoCompraCab.get(j).isValSelec()) {
                    i = i + 1;
                    break;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un pedido de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                accionesPedidoCompra("Aprobar", 2);
            }
        }
        chk_selecAll.setChecked(false);
    }

    @Listen("onClick=#tbbtn_btn_rechazar")
    public void RechazarPedidoCompra() throws SQLException {
        if (objListaPedidoCompraCab == null || objListaPedidoCompraCab.isEmpty()) {
            Messagebox.show("No hay pedidos de compra por rechazar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objListaPedidoCompraCab.getSize(); j++) {
                if (objListaPedidoCompraCab.get(j).isValSelec()) {
                    i = i + 1;
                    break;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un pedido de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                rechazoPedidoCompra("Rechazar", 3);
            }
        }
        chk_selecAll.setChecked(false);
    }

    @Listen("onClick=#btn_buscarordenes")
    public void buscarPedidosCompra() throws SQLException {
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
        String s_proveedor = txt_provid.getValue();
        String s_pedido = txt_filtro_ped.getValue();
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objListaPedidoCompraCab = new ListModelList();
            objListaPedidoCompraCab = objDaoGenPedCom.buscarPedidosCompras(f_emisioni, f_emisionf, s_proveedor, s_pedido);
            //Validar la tabla sin registro
            if (objListaPedidoCompraCab.getSize() > 0) {
                lst_admpedcom.setModel(objListaPedidoCompraCab);
            } else {
                objListaPedidoCompraCab = null;
                lst_admpedcom.setModel(objListaPedidoCompraCab);
                Messagebox.show("No existen pedidos de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        objListaPedidoCompraDet = null;
        lst_admpedidocompra_detalle.setModel(objListaPedidoCompraDet);
        limpiarCamposDetalle();
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objListaPedidoCompraCab == null || objListaPedidoCompraCab.isEmpty()) {
            Messagebox.show("No hay pedidos de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionAdmPedCom.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion    
    @Listen("onOK=#txt_filtro_ped")
    public void onOKPedido() throws SQLException {
        txt_provid.focus();
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
                String cod = Utilitarios.lpad(txt_filtro_ped.getValue(), 10, "0");
                txt_filtro_ped.setValue(cod);
            }
        }
    }

    @Listen("onOK=#txt_provid")
    public void lov_proveedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "AdmPedCom";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerAdmPedCom");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                d_fecini.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void onBlurProveedor() throws SQLException {
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
                Proveedores objPedidoCompraCabedor = new DaoProveedores().BusquedaProveedor(txt_provid.getValue(), "1");
                if (objPedidoCompraCabedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objPedidoCompraCabedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objPedidoCompraCabedor.getProv_id());
                    txt_provdes.setValue(objPedidoCompraCabedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#d_fecini")
    public void onOK_d_fecini() {
        d_fecfin.focus();
    }

    @Listen("onOK=#d_fecfin")
    public void onOK_d_fecfin() {
        btn_buscarordenes.focus();
    }
    
    @Listen("onBlur=#txt_provid")
    public void onBlur_txt_provid() {
        if (txt_provid.getValue().isEmpty()) {
            txt_provdes.setValue("");
        }
    }

    //Eventos Otros 
    public void accionesPedidoCompra(String proceso, final int sit) throws SQLException {
        s_mensaje = "¿Está Ud. seguro que desea generar la orden de compra para los pedidos seleccionados?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            String s_mensaje = "Pedidos de compra \n\n";
                            for (int i = 0; i < objListaPedidoCompraCab.size(); i++) {
                                if (objListaPedidoCompraCab.get(i).getPedcom_sit() == 1 && objListaPedidoCompraCab.get(i).isValSelec()) { //                                             
                                    ParametrosSalida objParamSalida = objDaoGenPedCom.generaOrdenCompra(objListaPedidoCompraCab.get(i));
                                    s_mensaje += "Nro.Pedido - " + objListaPedidoCompraCab.get(i).getPedcom_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                }
                            }
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                        buscarPedidosCompra();
                    }
                });
    }

    public void rechazoPedidoCompra(String proceso, final int sit) throws SQLException {
        s_mensaje = "¿Está Ud. seguro que desea rechazar los pedidos seleccionados?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    String s_mensaje = "Pedidos de Compra \n\n";
                    for (int i = 0; i < objListaPedidoCompraCab.size(); i++) {
                        if (objListaPedidoCompraCab.get(i).getPedcom_sit() == 1 && objListaPedidoCompraCab.get(i).isValSelec()) { //                                             
                            ParametrosSalida objParamSalida = objDaoGenPedCom.rechazarPedidoCompra(objListaPedidoCompraCab.get(i));
                            s_mensaje += "Nro.Pedido - " + objListaPedidoCompraCab.get(i).getPedcom_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                        }
                    }
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
                buscarPedidosCompra();
            }
        });
    }

    public void llenarCampos(PedidoCompraCab objPedidoCompraCab) throws SQLException {
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
        lst_admpedidocompra_detalle.setModel(objListaPedidoCompraDet);
        double data[]/* = new double[8]*/;
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
    }

    public void limpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        lst_admpedcom.setModel(objListaPedidoCompraCab);
    }
}
