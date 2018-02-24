package org.me.gj.controller.facturacion.informes;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.procesos.InterfaceControllers;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerRepGuiasRemision extends SelectorComposer implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaGuiaRemision, tab_mantenimientoGuiaRemision;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro,
            tbbtn_btn_guardarpro, tbbtn_btn_deshacerpro, tbbtn_filtro_buscar;
    @Wire
    Combobox cb_periodo, cb_simbmon, cb_tipoventa, cb_pronpag;
    @Wire
    Datebox d_filtro_fecha, d_fecemi, d_fecadd, d_fecmod;
    @Wire
    Doublebox db_por_lispre, db_por_convta, db_tipcam, txt_total,
            db_por_dsccli, db_por_dscesp, db_por_desman,
            db_det_preven, db_det_valvta, db_det_valdes, db_det_pordes, db_det_porigv,
            db_det_valigv, db_det_total, db_det_stock, txt_cant,
            db_sumped, db_sumok, db_sumrec, db_valped, db_valok, db_valrec;
    @Wire
    Textbox txt_filtro_codsuper, txt_filtro_dessuper, txt_filtro_codven, txt_filtro_nomven,
            txt_nropedven, txt_situacion, txt_dni, txt_ruc,
            txt_proid, txt_cli_id, txt_cli_razsoc, txt_lincred, txt_limdoc, txt_saldo, txt_estado, txt_clidir_direcc,
            txt_zon_id, txt_zon_des, txt_giro, txt_desgiro, txt_idvendedor, txt_nomvendedor, txt_diavis,
            txt_id_listapre, txt_des_listapre, txt_id_condven, txt_des_condven, txt_diaplazo,
            txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_usuadd, txt_usumod;
    @Wire
    Listbox lst_guiasremision, lst_guiasremision_detalle;
    @Wire
    Button btn_procesar;
    @Wire
    Intbox txt_pedvendet_key, txt_frac;
    @Wire
    Longbox txt_clidir_id;
    //Instancias de Objetos
    ListModelList lst_ManPeriodos;
    ListModelList lst_tipoventa;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
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
    private static final Logger LOGGER = Logger.getLogger(ControllerRepGuiasRemision.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        habilitaBotones(false, true);
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
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40403000, usuario, empresa, sucursal);
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

    //Eventos Secundarios - Validacion    
    @Listen("onOK=#cb_periodo")
    public void next_Supervisor() {
        txt_filtro_codsuper.focus();
    }

    //Eventos Otros 
    public void llenarCampos() {
        /*txt_nropedven.setValue(objPedidoVentaCab.getPcab_nroped());
         cb_tipoventa.setValue(objPedidoVentaCab.getTip_ventades());
         if (objPedidoVentaCab.getPcab_motrec_des() != null) {
         //txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des() + "-(" + objPedidoVentaCab.getPcab_motrec_des() + ")");
         txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des() + "-(" + objMotRec.getMrec_des() + ")");
         } else {
         txt_situacion.setValue(objPedidoVentaCab.getPcab_situacion_des());
         }
         d_fecemi.setValue(objPedidoVentaCab.getPcab_fecemi());
         //cb_simbmon.setValue(objPedidoVentaCab.getPcab_mon().equals("001") ? "S/." : "$");
         cb_simbmon.setSelectedIndex(objPedidoVentaCab.getPcab_mon().equals("001") ? 0 : 1);
         db_tipcam.setValue(objPedidoVentaCab.getPcab_tipcam());
         txt_cli_id.setValue(objPedidoVentaCab.getCli_id());
         txt_cli_razsoc.setValue(objPedidoVentaCab.getCli_des());
         txt_dni.setValue(objPedidoVentaCab.getPcab_nrodni());
         txt_ruc.setValue(objPedidoVentaCab.getPcab_nroruc());
         txt_lincred.setValue(String.valueOf(objPedidoVentaCab.getPcab_limcre()));
         txt_limdoc.setValue(String.valueOf(objPedidoVentaCab.getPcab_limdoc()));
         txt_saldo.setValue(String.valueOf(objPedidoVentaCab.getPcab_salcre()));
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
         d_fecmod.setValue(objPedidoVentaCab.getPcab_fecmod());*/
    }

    public void llenarCamposDetalle() throws SQLException {
        /*String nropedkey = objPedidoVentaCab.getPcab_nroped();
         //String pv_ind = "C";
         objListaPedidoVentaDet = null;
         objListaPedidoVentaDet = objDaoGenPedVen.listaPedidoVentaDet(emp_id, suc_id, nropedkey, pv_ind);
         lst_pedven_detalle.setModel(objListaPedidoVentaDet);*/
    }

    public void llenarCamposProducto() throws SQLException {
        //String s_key = String.valueOf(objPedidoVentaDet.getPdet_item());
        /*txt_pedvendet_key.setValue(objPedidoVentaDet.getPdet_item());// ITEM
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
         if (objParametrosSalida.getCantStocks() > objProductos.getPro_scknofact()) {
         db_det_stock.setValue((objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
         } else {
         db_det_stock.setValue(0.00);
         }*/
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaGuiaRemision.setSelected(b_valida1);
        tab_mantenimientoGuiaRemision.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaGuiaRemision.setDisabled(b_valida1);
        tab_mantenimientoGuiaRemision.setDisabled(b_valida2);
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
        /*txt_nropedven.setValue("");
         txt_dni.setValue("");
         txt_ruc.setValue("");
         db_tipcam.setValue(1.000);
         txt_cli_id.setValue(null);
         txt_cli_razsoc.setValue("");
         txt_lincred.setValue("");
         txt_limdoc.setValue("");
         txt_saldo.setValue("");
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
         db_por_dscesp.setValue(null);*/
    }

    public void limpiarCamposDetalle() {
        /*txt_pedvendet_key.setValue(null);
         txt_proid.setValue("");
         txt_prodes.setValue("");
         txt_det_idlispre.setValue("");
         txt_det_deslispre.setValue("");
         txt_cant.setValue(null);
         txt_frac.setValue(null);
         db_det_preven.setValue(null);
         db_det_valvta.setText(null);
         db_det_pordes.setValue(null);
         db_det_valdes.setValue(null);
         db_det_porigv.setValue(null);
         db_det_valigv.setValue(null);
         db_det_total.setValue(null);
         db_det_stock.setValue(null);*/
    }

    public void limpiarDireccionNulo() {
        /*txt_zon_id.setValue("");
         txt_zon_des.setValue("");
         txt_idvendedor.setValue("");
         txt_nomvendedor.setValue("");
         txt_giro.setValue("");
         txt_clidir_id.setValue(null);
         txt_clidir_direcc.setValue("");*/
    }

    public void habilitaCampos(boolean b_valida1) {
        /*cb_tipoventa.setDisabled(b_valida1);
         d_fecemi.setDisabled(b_valida1);
         cb_simbmon.setDisabled(b_valida1);
         txt_cli_id.setDisabled(b_valida1);
         txt_clidir_id.setDisabled(b_valida1);
         txt_idvendedor.setDisabled(b_valida1);
         txt_id_listapre.setDisabled(b_valida1);
         txt_id_condven.setDisabled(b_valida1);
         cb_pronpag.setDisabled(b_valida1);
         db_por_dscesp.setDisabled(b_valida1);*/
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        txt_proid.setDisabled(b_valida1);
        txt_cant.setDisabled(b_valida1);
        txt_frac.setDisabled(b_valida1);
        db_det_stock.setDisabled(b_valida1);
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    //metodos sin utilizar
    public void llenaRegistros() throws SQLException {
    }

    public void botonNuevo() {
    }

    public void botonEditar() throws SQLException {
    }

    public void botonEliminar() throws SQLException {
    }

    public void botonDeshacer() {
    }

    public void botonGuardar() {
    }

    public void botonNuevoDetalle() {
    }

    public void botonEditarDetalle() {
    }

    public void botonEliminarDetalle() {
    }

    public void botonGuardarDetalle() {
    }

    public void botonDeshacerDetalle() {
    }

    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
