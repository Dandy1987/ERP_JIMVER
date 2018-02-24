package org.me.gj.controller.facturacion.informes;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.DaoHorario;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerRepConsolidadoMercaderia extends SelectorComposer {

    //Componentes Web
    @Wire
    Tab tab_listaConsoMerca, tab_mantenimientoConsoMerca;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_filtro_fecha, d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Doublebox db_sumped;
    @Wire
    Textbox txt_filtro_idhorent, txt_filtro_deshorent, //----->tab_lista
            txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_usuadd, txt_usumod;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro,
            tbbtn_btn_guardarpro, tbbtn_btn_deshacerpro, tbbtn_filtro_buscar;
    @Wire
    Listbox lst_consomercadesp, lst_consomercanodesp;
    @Wire
    Button btn_consultar, btn_marcardesp, btn_consxtransporte, btn_nodespacho;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_selecAll, chk_listadoc, chk_impquiebre;//------>mantenimiento
    //Instancias de Objetos
    ListModelList<Horario> objlstHorario;
    ListModelList lst_ManPeriodos;
    Mesa objMesa = new Mesa();
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    DaoHorario objDaoHorario;
    Horario objHorario;
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
    private static final Logger LOGGER = Logger.getLogger(ControllerRepConsolidadoMercaderia.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        habilitaBotones(false, true);
        /*objdaoCliente = new DaoCliente();
         objCliente = new Cliente();
         objDaoMesa = new DaoMesa();
         objDaoMotRec = new DaoMotivoRechazo();
         objDaoGenPedVen = new DaoPedVen();
         objPedidoVentaCab = new PedidoVentaCab();
         objPedidoVentaDet = new PedidoVentaDet();
         objParametrosSalida = new ParametrosSalida();
         objDaoCierreDia = new DaoCierreDia();*/
        s_estado = "Q";
        s_estado_detalle = "Q";
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();

        objlstHorario = new ListModelList<Horario>();
        objDaoHorario = new DaoHorario();
        objlstHorario = objDaoHorario.listaHorario(1);

        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(lst_ManPeriodos);
        cb_periodo.setValue(periodo);
        //txt_filtro_codven.focus();
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
    @Listen("onClick=#btn_nodespacho")
    public void botonPreview() throws SQLException {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
        objMapObjetos.put("usuario", objUsuCredential.getCuenta());
        objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConsoMercaNoDesp.zul", null, objMapObjetos);
        window.doModal();
    }

    @Listen("onOK=#cb_periodo")
    public void next_Supervisor() {
        d_filtro_fecha.focus();
    }

    //Eventos Otros 
    public void llenarCampos() {
        /*d_fecemi.setValue(objPedidoVentaCab.getPcab_fecemi());
         txt_usuadd.setValue(objPedidoVentaCab.getPcab_usuadd());
         d_fecadd.setValue(objPedidoVentaCab.getPcab_fecadd());
         txt_usumod.setValue(objPedidoVentaCab.getPcab_usumod());
         d_fecmod.setValue(objPedidoVentaCab.getPcab_fecmod());*/
    }

    public void llenarCamposDetalle() throws SQLException {
        /*String nropedkey = objPedidoVentaCab.getPcab_nroped();
         //String pv_ind = "C";
         objListaPedidoVentaDet = null;
         objListaPedidoVentaDet = objDaoGenPedVen.listaPedidoVentaDet(emp_id, suc_id, nropedkey, pv_ind);
         lst_pedven_detalle.setModel(objListaPedidoVentaDet);*/
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaConsoMerca.setSelected(b_valida1);
        tab_mantenimientoConsoMerca.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaConsoMerca.setDisabled(b_valida1);
        tab_mantenimientoConsoMerca.setDisabled(b_valida2);
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
    }

    public void limpiarCamposTotalesLista() {
        db_sumped.setValue(null);
    }

    public void limpiarCamposTotales() {
        db_sumped.setValue(null);
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

    //metodos sin utilizar
    public void habilitaCampos(boolean b_valida1) {
    }
}
