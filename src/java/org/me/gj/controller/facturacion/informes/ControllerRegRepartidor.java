package org.me.gj.controller.facturacion.informes;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoMesa;
import org.me.gj.controller.facturacion.mantenimiento.DaoMotivoRechazo;
import org.me.gj.controller.facturacion.mantenimiento.DaoNumDoc;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.facturacion.procesos.DaoPedVen;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.facturacion.mantenimiento.NumDoc;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.facturacion.procesos.PedidoVentaCab;
import org.me.gj.model.facturacion.procesos.PedidoVentaDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerRegRepartidor extends SelectorComposer {

    //Componentes Web
    @Wire
    Tab tab_listaRepDocVen, tab_mantenimientoRepDocVen;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro,
            tbbtn_btn_guardarpro, tbbtn_btn_deshacerpro, tbbtn_filtro_buscar;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_filtro_fecha, d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Doublebox db_sumped;
    @Wire
    Textbox txt_filtro_codven, txt_filtro_nomven, txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_usuadd, txt_usumod;
    @Wire
    Listbox lst_repdocven, lst_pedven_detalle;
    @Wire
    Button btn_consultarpedven, btn_clonreg, btn_guiarem, btn_docxcob, btn_reasignar, btn_preview;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_modtip, chk_desman;
    //Instancias de Objetos
    ListModelList<NumDoc> objListaNumDoc;
    ListModelList lst_ManPeriodos;
    ListModelList lst_Almacenes_Origen;
    ListModelList lst_Almacenes_Destino;
    ListModelList lst_Monedas;
    ListModelList lst_tipoventa;
    ListModelList<PedidoVentaCab> objListaPedidoVentaCab;
    ListModelList<PedidoVentaDet> objListaPedidoVentaDet, objListaEliminacion;
    Mesa objMesa;
    PedidoVentaCab objPedidoVentaCab;
    PedidoVentaDet objPedidoVentaDet;
    ParametrosSalida objParametrosSalida;
    DaoPedVen objDaoGenPedVen;
    DaoAccesos objDaoAccesos;
    DaoCliente objdaoCliente;
    DaoMesa objDaoMesa;
    DaoMotivoRechazo objDaoMotRec;
    DaoCierreDia objDaoCierreDia;
    DaoPrecios objDaoPrecioVen;
    DaoProductos objDaoProducto;
    DaoNumDoc objDaoNumDoc;
    Accesos objAccesos;
    Productos objProductos;
    Cliente objCliente;
    MotivoRechazo objMotRec;
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
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdffe = new SimpleDateFormat("EEE");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRegRepartidor.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        habilitaBotones(false, true);
        objListaNumDoc = new ListModelList<NumDoc>();
        objMesa = new Mesa();
        objdaoCliente = new DaoCliente();
        objCliente = new Cliente();
        objDaoMesa = new DaoMesa();
        objDaoMotRec = new DaoMotivoRechazo();
        objDaoGenPedVen = new DaoPedVen();
        objPedidoVentaCab = new PedidoVentaCab();
        objPedidoVentaDet = new PedidoVentaDet();
        objParametrosSalida = new ParametrosSalida();
        objDaoCierreDia = new DaoCierreDia();
        s_estado = "Q";
        s_estado_detalle = "Q";
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(lst_ManPeriodos);
        cb_periodo.setValue(periodo);
        txt_filtro_codven.focus();
    }

    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40402000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Generación de Pedido de Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado Procesos Generación de Pedido de Venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Pedidos de Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Pedidos de Venta");
        }
    }

    @Listen("onCreate=#tabbox_pedventa")
    public void llenaRegistros() throws SQLException {
        /*objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
         objListaPedidoVentaCab = objDaoGenPedVen.listaPedidoVenCab(emp_id, suc_id, "%%", fechaActual, fechaActual, 1);
         lst_anudocven.setModel(objListaPedidoVentaCab);*/
    }

    @Listen("onClick=#btn_consultarpedven")
    public void busquedaRegistros() throws SQLException {
        /*limpiarLista();
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
         String ven_id = txt_filtro_codven.getValue().isEmpty() ? "%%" : txt_filtro_codven.getValue();
         String s_periodo = cb_periodo.getValue();

         //objListaPedidoVentaCab = new ListModelList();
         //objListaPedidoVentaCab = objDaoGenPedVen.busquedaPedidoVenta(emp_id, suc_id, f_emisioni, sup_id, ven_id, situacion, estado, s_periodo);
         //Validar la tabla sin registro
         if (objListaPedidoVentaCab.getSize() > 0) {
         lst_repdocven.setModel(objListaPedidoVentaCab);
         } else {
         objListaPedidoVentaCab = null;
         lst_repdocven.setModel(objListaPedidoVentaCab);
         Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         }
         } else {
         Messagebox.show("La fecha no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         limpiarCamposTotalesLista();
         }
         limpiarCampos();
         objListaPedidoVentaDet = null;
         lst_pedven_detalle.setModel(objListaPedidoVentaDet);
         limpiarCamposTotales();
         limpiaAuditoria();*/
    }

    @Listen("onSelect=#lst_repdocven")
    public void seleccionaRegistro() throws SQLException {
        limpiarCampos();
        limpiarCamposTotales();
        objPedidoVentaCab = (PedidoVentaCab) lst_repdocven.getSelectedItem().getValue();
        if (objPedidoVentaCab == null) {
            objPedidoVentaCab = objListaPedidoVentaCab.get(i_selCab + 1);
        }
        i_selCab = lst_repdocven.getSelectedIndex();
        llenarCampos();
        llenarCamposDetalle();
        llenarCamposTotales();
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        /*if (lst_anudocven.getSelectedIndex() == -1) {
         Messagebox.show("Por favor seleccione un documento de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else {
         Map objMapObjetos = new HashMap();
         objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
         objMapObjetos.put("usuario", objUsuCredential.getCuenta());
         objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
         Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAnulacionDocVenta.zul", null, objMapObjetos);
         window.doModal();*/
        //}
    }

    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_guiarem")
    public void botonGuiaRemision() throws SQLException {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
        objMapObjetos.put("usuario", objUsuCredential.getCuenta());
        objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovFacInfGuiaRem.zul", null, objMapObjetos);
        window.doModal();
    }

    @Listen("onClick=#btn_docxcob")
    public void botonDocxCobrar() throws SQLException {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
        objMapObjetos.put("usuario", objUsuCredential.getCuenta());
        objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovFacInfDocxCob.zul", null, objMapObjetos);
        window.doModal();
    }

    @Listen("onClick=#btn_preview")
    public void botonPreview() throws SQLException {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
        objMapObjetos.put("usuario", objUsuCredential.getCuenta());
        objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovFacInfPreview.zul", null, objMapObjetos);
        window.doModal();
    }

    @Listen("onOK=#cb_periodo")
    public void next_Supervisor() {
        d_filtro_fecha.focus();
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

    //Eventos Otros 
    public void llenarCampos() {
        d_fecemi.setValue(objPedidoVentaCab.getPcab_fecemi());
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

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaRepDocVen.setSelected(b_valida1);
        tab_mantenimientoRepDocVen.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaRepDocVen.setDisabled(b_valida1);
        tab_mantenimientoRepDocVen.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_modtip.setChecked(false);
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_modtip.setDisabled(b_valida1);
        d_fecemi.setDisabled(b_valida1);
    }

    public double[] calculosTotal() {
        int i;
        double data[] = new double[1];
        for (i = 0; i < objListaPedidoVentaDet.getSize(); i++) {
            data[0] = data[0] + ((PedidoVentaDet) objListaPedidoVentaDet.get(i)).getPdet_pventa();
        }
        return data;
    }

    public void limpiarCamposTotalesLista() {
        db_sumped.setValue(null);
    }

    public void limpiarCamposTotales() {
        db_sumped.setValue(null);
    }

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        db_sumped.setValue(data[0]);
    }

    public void limpiarLista() {
        /*objListaPedidoVentaCab = null;
         objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
         lst_repdocven.setModel(objListaPedidoVentaCab);*/
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }
}
