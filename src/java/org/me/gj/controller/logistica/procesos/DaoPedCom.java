package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.PedidoComLin;
import org.me.gj.model.logistica.informes.PedidoComProd;
import org.me.gj.model.logistica.informes.PedidoComProv;
import org.me.gj.model.logistica.informes.PedidoComSublin;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.procesos.PedidoCompraCab;
import org.me.gj.model.logistica.procesos.PedidoCompraDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPedCom {

    //Instancias de Objetos
    ListModelList<PedidoCompraCab> objListaPedidoCompraCab;
    ListModelList<PedidoCompraDet> objListaPedidoCompraDet;
    ListModelList<Productos> objListaProductos;
    PedidoCompraCab objPedidoCompraCab;
    PedidoCompraDet objPedidoCompraDet;
    ParametrosSalida objParametrosSalida;
    Productos objProducto;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    public static String P_CABECERA = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoPedCom.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarPedidoCompra(PedidoCompraCab objPedidoCompraCab, Object[][] listaPedidoCompraDet) throws SQLException {
        String SQL_INSERTAR_PEDIDO_COMPRA = "{call pack_tpedcom_cab.p_insertarPedidoCompra(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ARRAY a_pedido_compra_det;
        ArrayDescriptor ad_pedido_compra_det;
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_INSERTAR_PEDIDO_COMPRA);
            ad_pedido_compra_det = ArrayDescriptor.createDescriptor("LISTA_PEDIDO_DET", con.getMetaData().getConnection());
            a_pedido_compra_det = new ARRAY(ad_pedido_compra_det, con.getMetaData().getConnection(), listaPedidoCompraDet);
            cst.clearParameters();
            cst.setInt(1, objPedidoCompraCab.getEmp_id());
            cst.setInt(2, objPedidoCompraCab.getSuc_id());
            cst.setString(3, objPedidoCompraCab.getPer_id());
            cst.setInt(4, objPedidoCompraCab.getPer_key());
            cst.setDate(5, objPedidoCompraCab.getPedcom_fecemi() != null ? new Date(objPedidoCompraCab.getPedcom_fecemi().getTime()) : null);
            cst.setDate(6, objPedidoCompraCab.getPedcom_fecrec() != null ? new Date(objPedidoCompraCab.getPedcom_fecrec().getTime()) : null);
            cst.setDate(7, objPedidoCompraCab.getPedcom_feccad() != null ? new Date(objPedidoCompraCab.getPedcom_feccad().getTime()) : null);
            cst.setInt(8, objPedidoCompraCab.getPedcom_mon());
            cst.setDouble(9, objPedidoCompraCab.getPedcom_tipcam());
            cst.setString(10, objPedidoCompraCab.getPedcom_lispre());
            cst.setString(11, objPedidoCompraCab.getPedcom_glo());
            cst.setLong(12, objPedidoCompraCab.getProv_key());
            cst.setInt(13, objPedidoCompraCab.getCon_key());
            cst.setString(14, objPedidoCompraCab.getPedcom_almori());
            cst.setString(15, objPedidoCompraCab.getPedcom_almdes());
            cst.setString(16, objPedidoCompraCab.getProv_id());
            cst.setString(17, objUsuCredential.getCuenta());
            cst.setArray(18, a_pedido_compra_det);
            cst.registerOutParameter(19, java.sql.Types.NUMERIC);
            cst.registerOutParameter(20, java.sql.Types.VARCHAR);
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(19);
            s_msg = cst.getString(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida modificarPedidoCompra(PedidoCompraCab objPedidoCompraCab, Object[][] listaPedidoCompraDet) throws SQLException {
        String SQL_MODIFICAR_PEDIDO_COMPRA = "{call pack_tpedcom_cab.p_modificarPedidoCompra(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ARRAY a_pedido_compra_det;
        ArrayDescriptor ad_pedido_compra_det;
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_MODIFICAR_PEDIDO_COMPRA);
            ad_pedido_compra_det = ArrayDescriptor.createDescriptor("LISTA_PEDIDO_DET", con.getMetaData().getConnection());
            a_pedido_compra_det = new ARRAY(ad_pedido_compra_det, con.getMetaData().getConnection(), listaPedidoCompraDet);
            cst.clearParameters();
            cst.setString(1, objPedidoCompraCab.getPedcom_id());
            cst.setLong(2, objPedidoCompraCab.getPedcom_key());
            cst.setInt(3, objPedidoCompraCab.getEmp_id());
            cst.setInt(4, objPedidoCompraCab.getSuc_id());
            cst.setString(5, objPedidoCompraCab.getPer_id());
            cst.setInt(6, objPedidoCompraCab.getPer_key());
            cst.setDate(7, objPedidoCompraCab.getPedcom_fecemi() != null ? new Date(objPedidoCompraCab.getPedcom_fecemi().getTime()) : null);
            cst.setDate(8, objPedidoCompraCab.getPedcom_fecrec() != null ? new Date(objPedidoCompraCab.getPedcom_fecrec().getTime()) : null);
            cst.setDate(9, objPedidoCompraCab.getPedcom_feccad() != null ? new Date(objPedidoCompraCab.getPedcom_feccad().getTime()) : null);
            cst.setInt(10, objPedidoCompraCab.getPedcom_mon());
            cst.setDouble(11, objPedidoCompraCab.getPedcom_tipcam());
            cst.setString(12, objPedidoCompraCab.getPedcom_lispre());
            cst.setString(13, objPedidoCompraCab.getPedcom_glo());
            cst.setLong(14, objPedidoCompraCab.getProv_key());
            cst.setInt(15, objPedidoCompraCab.getCon_key());
            cst.setString(16, objPedidoCompraCab.getPedcom_almori());
            cst.setString(17, objPedidoCompraCab.getPedcom_almdes());
            cst.setString(18, objPedidoCompraCab.getProv_id());
            cst.setString(19, objUsuCredential.getCuenta());
            cst.setArray(20, a_pedido_compra_det);
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(21);
            s_msg = cst.getString(22);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarPedidoCompra(PedidoCompraCab objPedidoCompraCab) throws SQLException {
        String SQL_ELIMINAR_PEDIDO_COMPRA = "{call pack_tpedcom_cab.p_eliminarPedidoCompra(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PEDIDO_COMPRA);
            cst.clearParameters();
            cst.setLong("n_pedcom_key", objPedidoCompraCab.getPedcom_key());
            cst.setInt("n_emp_id", objPedidoCompraCab.getEmp_id());
            cst.setInt("n_suc_id", objPedidoCompraCab.getSuc_id());
            cst.setString("c_usumod", objUsuCredential.getCuenta());
            cst.registerOutParameter("n_Flag", 2);
            cst.registerOutParameter("c_msg", 12);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida rechazarPedidoCompra(PedidoCompraCab objPedidoCompraCab) throws SQLException {
        String SQL_RECHAZAR_PEDIDO_COMPRA = "{call pack_tpedcom_cab.p_RecPedidoCompra(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_RECHAZAR_PEDIDO_COMPRA);
            cst.clearParameters();
            cst.setLong("n_pedcom_key", objPedidoCompraCab.getPedcom_key());
            cst.setInt("n_emp_id", objPedidoCompraCab.getEmp_id());
            cst.setInt("n_suc_id", objPedidoCompraCab.getSuc_id());
            cst.setString("c_usumod", objUsuCredential.getCuenta());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida generaOrdenCompra(PedidoCompraCab objPedidoCompraCab) throws SQLException {
        String SQL_GENERA_ORDEN_COMPRA = "{call pack_tpedcom_cab.p_001_generarOrdenCompra(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERA_ORDEN_COMPRA);
            cst.clearParameters();
            cst.setLong("n_pedcom_key", objPedidoCompraCab.getPedcom_key());
            cst.setInt("n_emp_id", objPedidoCompraCab.getEmp_id());
            cst.setInt("n_suc_id", objPedidoCompraCab.getSuc_id());
            cst.setString("c_usu_add", objUsuCredential.getCuenta());
            cst.registerOutParameter("n_Flag", 2);
            cst.registerOutParameter("c_msg", 12);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones    
    public ListModelList<PedidoCompraCab> buscarPedidosCompras(String fechai, String fechaf, String proveedor, String pedido) throws SQLException {
        String SQL_LISTA_PEDIDOS_COMPRAS;
        SQL_LISTA_PEDIDOS_COMPRAS = "";
        String tituloRep = "";
        String fechaActual = Utilitarios.hoyAsString();
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id,t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and  to_date('" + fechaf + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } else if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id,t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and  to_date('" + fechaf + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        }//fecha inicial vacio, fecha final vacio, proveedor vacio
        else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,  "
                    + " t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, "
                    + "t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides,"
                    + " t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , "
                    + "t.alm_direcc , t.total  from v_listapedidocompra t  "
                    + "where t.emp_id = " + objUsuCredential.getCodemp() + " "
                    + "and t.suc_id = " + objUsuCredential.getCodsuc() + " "
                    + "and t.pedcom_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA";
        } else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,  "
                    + " t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, "
                    + "t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides,"
                    + " t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , "
                    + "t.alm_direcc , t.total  from v_listapedidocompra t  "
                    + "where t.emp_id = " + objUsuCredential.getCodemp() + " "
                    + "and t.suc_id = " + objUsuCredential.getCodsuc() + " "
                    + "and t.pedcom_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA";
        }//fecha inicial llena, fecha final llena, proveedor vacio 
        else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id,t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and  to_date('" + fechaf + "','dd/mm/yyyy') order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        } else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id,t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and  to_date('" + fechaf + "','dd/mm/yyyy') and t.pedcom_id=" + pedido + " order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        }//fecha inicial llena, fecha final vacio, proveedor lleno  
        else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi >=  to_date('" + fechai + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi >=  to_date('" + fechai + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacio, fecha final llena, proveedor lleno 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi <=  to_date('" + fechaf + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi <=  to_date('" + fechaf + "','dd/mm/yyyy') and t.prov_id = " + proveedor + " and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial llena, fecha final vacio, proveedor vacio 
        else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi >=  to_date('" + fechai + "','dd/mm/yyyy') order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc ,  t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi >=  to_date('" + fechai + "','dd/mm/yyyy') and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA DESDE " + fechai;
        } //fecha inicial vacia, fecha final vacio, proveedor lleno 
        else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,  "
                    + " t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, "
                    + "t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes,"
                    + " t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  "
                    + "from v_listapedidocompra t  "
                    + "where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " "
                    + "and t.prov_id = " + proveedor + "  "
                    + "and t.pedcom_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,  "
                    + " t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, "
                    + "t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes,"
                    + " t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  "
                    + "from v_listapedidocompra t  "
                    + "where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " "
                    + "and t.prov_id = " + proveedor + "  "
                    + "and t.pedcom_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacia, fecha final lleno, proveedor vacio 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi <=  to_date('" + fechaf + "','dd/mm/yyyy') order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !pedido.isEmpty()) {
            SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key, t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and t.pedcom_fecemi <=  to_date('" + fechaf + "','dd/mm/yyyy') and t.pedcom_id=" + pedido + "order by t.pedcom_id  ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        }
        P_WHERER = SQL_LISTA_PEDIDOS_COMPRAS;
        P_TITULO = tituloRep;
        objListaPedidoCompraCab = new ListModelList();
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PEDIDOS_COMPRAS);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objPedidoCompraCab.setPedcom_lugent(rs.getString("alm_direcc"));
                objPedidoCompraCab.setPedcom_total(Double.valueOf(rs.getDouble("total")));
                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public ListModelList<PedidoCompraCab> listaPedidosCompras() throws SQLException {
        String SQL_LISTA_PEDIDOS_COMPRAS;
        SQL_LISTA_PEDIDOS_COMPRAS = " select t.emp_id, t.suc_id, t.per_id, t.pedcom_id, t.pedcom_key , t.pedcom_fecemi, t.pedcom_fecrec, t.pedcom_feccad,   t.pedcom_est, t.pedcom_estdes, t.prov_id, t.prov_key , t.prov_ruc, t.prov_razsoc, t.pedcom_mon, t.tab_subdes,   t.pedcom_tipcam, t.pedcom_lispre, t.pedcom_lispredes, t.con_key, t.con_des, t.pedcom_sit,   t.pedcom_sitdes, t.pedcom_almori, t.alm_orides, t.pedcom_almdes, t.alm_desdes, t.pedcom_glo, t.pedcom_usuadd , t.pedcom_fecadd , t.pedcom_usumod , t.pedcom_fecmod , t.alm_direcc , t.total  from v_listapedidocompra t  where t.emp_id = " + objUsuCredential.getCodemp() + " and t.suc_id = " + objUsuCredential.getCodsuc() + " and to_char(t.pedcom_fecemi,'dd/mm/yyyy') = to_char(sysdate,'dd/mm/yyyy') and t.pedcom_est <> 0 order by t.pedcom_id  ";
        P_WHERER = SQL_LISTA_PEDIDOS_COMPRAS;
        P_TITULO = "PEDIDOS DE COMPRA DESDE " + Utilitarios.hoyAsString() + " HASTA " + Utilitarios.hoyAsString();
        objListaPedidoCompraCab = new ListModelList();
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PEDIDOS_COMPRAS);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objPedidoCompraCab.setPedcom_lugent(rs.getString("alm_direcc"));
                objPedidoCompraCab.setPedcom_total(Double.valueOf(rs.getDouble("total")));
                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public ListModelList<PedidoCompraDet> listaPedidoCompraDet(Long nropedcom_key) throws SQLException {
        String SQL_PEDIDO_COMPRA_DET = " select * from v_listapedidocompradet t where  t.pedcom_key = '" + nropedcom_key + "' and t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "'   ";
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PEDIDO_COMPRA_DET);
            objListaPedidoCompraDet = new ListModelList();
            while (rs.next()) {
                objPedidoCompraDet = new PedidoCompraDet();
                objPedidoCompraDet.setPedcomdet_key(rs.getInt("pedcomdet_key"));
                objPedidoCompraDet.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraDet.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraDet.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraDet.setPer_id(rs.getString("per_id"));
                objPedidoCompraDet.setPro_id(rs.getString("pro_id"));
                objPedidoCompraDet.setPro_des(rs.getString("pro_des"));
                objPedidoCompraDet.setPro_desdes(rs.getString("pro_desdes"));
                objPedidoCompraDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objPedidoCompraDet.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraDet.setPedcom_canped(rs.getDouble("pedcom_canped"));
                objPedidoCompraDet.setPedcom_scanped(rs.getDouble("pedcom_canped"));
                objPedidoCompraDet.setPedcom_preuni(rs.getDouble("pedcom_preuni"));
                objPedidoCompraDet.setPedcom_valafe(rs.getDouble("pedcom_valafe"));
                objPedidoCompraDet.setPedcom_svalafe(rs.getDouble("pedcom_valafe"));
                objPedidoCompraDet.setPedcom_valexo(rs.getDouble("pedcom_valexo"));
                objPedidoCompraDet.setPedcom_svalexo(rs.getDouble("pedcom_valexo"));
                objPedidoCompraDet.setPedcom_pordes(rs.getDouble("pedcom_pordes"));
                objPedidoCompraDet.setPedcom_valdes(rs.getDouble("pedcom_valdes"));
                objPedidoCompraDet.setPedcom_svaldes(rs.getDouble("pedcom_valdes"));
                objPedidoCompraDet.setPedcom_porimp(rs.getDouble("pedcom_porimp"));
                objPedidoCompraDet.setPedcom_valimp(rs.getDouble("pedcom_valimp"));
                objPedidoCompraDet.setPedcom_svalimp(rs.getDouble("pedcom_valimp"));
                objPedidoCompraDet.setPedcom_valtot(rs.getDouble("pedcom_valtot"));
                objPedidoCompraDet.setPedcom_svaltot(rs.getDouble("pedcom_valtot"));
                objPedidoCompraDet.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraDet.setPedcom_genord(rs.getInt("pedcom_genord"));
                objPedidoCompraDet.setPedcom_canbon(rs.getDouble("pedcom_canbon"));
                objPedidoCompraDet.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraDet.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraDet.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraDet.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objPedidoCompraDet.setPro_pesouni(rs.getDouble("pro_peso"));
                objPedidoCompraDet.setPro_peso_unidaduni(rs.getString("pro_unipeso"));
                objPedidoCompraDet.setPro_spesouni(rs.getDouble("pro_peso"));
                objPedidoCompraDet.setPro_pesotot(rs.getDouble("peso_total"));
                //objPedidoCompraDet.setPro_spesotot((rs.getDouble("pro_peso") * rs.getDouble("pedcom_canped")));
                objPedidoCompraDet.setPro_spesotot(rs.getDouble("peso_total"));
                objPedidoCompraDet.setPro_peso_unidadtot(rs.getString("uni_total"));
                //objPedidoCompraDet.setPro_peso_unidadtot("KLG");
                objPedidoCompraDet.setPro_voluni(rs.getDouble("volumen"));
                objPedidoCompraDet.setPro_vol_unidaduni(rs.getString("uni_volumen"));
                objPedidoCompraDet.setPro_svoluni(rs.getDouble("volumen"));
                objPedidoCompraDet.setPro_voltot(rs.getDouble("volumen_total"));
                objPedidoCompraDet.setPro_svoltot(rs.getDouble("volumen_total"));
                //objPedidoCompraDet.setPro_voltot((rs.getDouble("volumen") * rs.getDouble("pedcom_canped")));
                //objPedidoCompraDet.setPro_svoltot((rs.getDouble("volumen") * rs.getDouble("pedcom_canped")));
                objPedidoCompraDet.setPro_vol_unidadtot(rs.getString("uni_volumen"));
                objPedidoCompraDet.setPedcom_ubi(rs.getString("pedcom_ubi"));
                objPedidoCompraDet.setPedcom_ubides(rs.getString("ubic_des"));
                objListaPedidoCompraDet.add(objPedidoCompraDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraDet;
    }

    public ListModelList<PedidoComProv> listaTotalxProveedor(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id) throws SQLException {
        String validaFecha, SQL_CONSULTAXPROVEEDOR;
        ListModelList<PedidoComProv> objlstPedidoComProv = new ListModelList<PedidoComProv>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
        } else {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        }
        SQL_CONSULTAXPROVEEDOR = "select t.emp_id, t.suc_id, t.prov_key, t.prov_id, t.prov_razsoc prov_razsoc,"
                + "count(distinct t.pedcom_key) prov_cant, "
                + "sum(p.pedcom_valdes) prov_valdes ,"
                + "sum(p.pedcom_valafe) prov_valafe, "
                + "sum(p.pedcom_valinafe) prov_valinafe, "
                + "sum(p.pedcom_valexo) prov_valexo, "
                + "sum(p.pedcom_valimp) prov_valimp ,"
                + "sum(p.pedcom_valtot) prov_valtot "
                + "from v_listapedidocompra t,v_listapedidocompradet p where t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.pedcom_key=p.pedcom_key "
                + "and t.pedcom_est=1 and p.pedcom_est=1 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pedcom_sit like '" + sit + "' and t.per_id like '" + periodo + "' "
                + "and t.prov_id like '" + prov_id + "' " + validaFecha
                + "group by t.emp_id, t.suc_id,t.prov_key,t.prov_id,t.prov_razsoc";
        P_CABECERA = SQL_CONSULTAXPROVEEDOR;

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXPROVEEDOR);
            while (rs.next()) {
                PedidoComProv objPedidoComProv = new PedidoComProv();
                objPedidoComProv.setEmp_id(rs.getInt("emp_id"));
                objPedidoComProv.setSuc_id(rs.getInt("suc_id"));
                objPedidoComProv.setProv_key(rs.getLong("prov_key"));
                objPedidoComProv.setProv_id(rs.getString("prov_id"));
                objPedidoComProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoComProv.setFecemi(fec_inicial);
                objPedidoComProv.setFecfin(fec_final);
                objPedidoComProv.setPeriodo(periodo);
                objPedidoComProv.setCant(rs.getLong("prov_cant"));
                objPedidoComProv.setVdesc(rs.getDouble("prov_valdes"));
                objPedidoComProv.setVafecto(rs.getDouble("prov_valafe"));
                objPedidoComProv.setVinafecto(rs.getDouble("prov_valinafe"));
                objPedidoComProv.setVexonerado(rs.getDouble("prov_valexo"));
                objPedidoComProv.setVimpto(rs.getDouble("prov_valimp"));
                objPedidoComProv.setVtotal(rs.getDouble("prov_valtot"));
                objlstPedidoComProv.add(objPedidoComProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstPedidoComProv.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPedidoComProv;
    }

    public ListModelList<PedidoComProd> listaTotalxProducto(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id, String pro_id, String pro_key) throws SQLException {
        String validaFecha, SQL_CONSULTAXPRODUCTO;
        ListModelList<PedidoComProd> objlstPedidoComProd = new ListModelList<PedidoComProd>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
        } else {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        }
        SQL_CONSULTAXPRODUCTO = "select t.emp_id, t.suc_id, p.pro_id, p.pro_des, p.pro_desdes ,"
                + "count(distinct t.pedcom_key) prod_cant, "
                + "sum(p.pedcom_valdes) prod_valdes ,"
                + "sum(p.pedcom_valafe) prod_valafe, "
                + "sum(p.pedcom_valinafe) prod_valinafe, "
                + "sum(p.pedcom_valexo) prod_valexo, "
                + "sum(p.pedcom_valimp) prod_valimp ,"
                + "sum(p.pedcom_valtot) prod_valtot "
                + "from v_listapedidocompra t,v_listapedidocompradet p where t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.pedcom_key=p.pedcom_key "
                + "and t.pedcom_est=1 and p.pedcom_est=1 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pedcom_sit like '" + sit + "' and p.pro_id like '" + pro_id + "' and t.prov_id like '" + prov_id + "' and t.per_id like '" + periodo + "' " + validaFecha
                + "group by t.emp_id, t.suc_id, p.pro_id, p.pro_des, p.pro_desdes";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXPRODUCTO);
            PedidoComProd objPedidoComProd;
            while (rs.next()) {
                objPedidoComProd = new PedidoComProd();
                objPedidoComProd.setEmp_id(rs.getInt("emp_id"));
                objPedidoComProd.setSuc_id(rs.getInt("suc_id"));
                objPedidoComProd.setPro_id(rs.getString("pro_id"));
                objPedidoComProd.setPro_des(rs.getString("pro_des"));
                objPedidoComProd.setPro_desdes(rs.getString("pro_desdes"));
                objPedidoComProd.setCant(rs.getLong("prod_cant"));
                objPedidoComProd.setFecemi(fec_inicial);
                objPedidoComProd.setFecfin(fec_final);
                objPedidoComProd.setPeriodo(periodo);
                objPedidoComProd.setVdesc(rs.getDouble("prod_valdes"));
                objPedidoComProd.setVafecto(rs.getDouble("prod_valafe"));
                objPedidoComProd.setVinafecto(rs.getDouble("prod_valinafe"));
                objPedidoComProd.setVexonerado(rs.getDouble("prod_valexo"));
                objPedidoComProd.setVimpto(rs.getDouble("prod_valimp"));
                objPedidoComProd.setVtotal(rs.getDouble("prod_valtot"));
                objlstPedidoComProd.add(objPedidoComProd);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstPedidoComProd.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPedidoComProd;
    }

    public ListModelList<PedidoComLin> listaTotalxLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_CONSULTAXLINEA;
        ListModelList<PedidoComLin> objlstPedidoComLin = new ListModelList<PedidoComLin>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') ";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') ";
        }
        SQL_CONSULTAXLINEA = "select t.emp_id,t.suc_id,p.tab_id lin_key ,p.tab_subdir lin_id, p.tab_subdes lin_des,"
                + "count(distinct t.pedcom_key) lin_cant,"
                + "sum(t.pedcom_valdes) lin_valdes ,"
                + "sum(t.pedcom_valafe) lin_valafe, "
                + "sum(t.pedcom_valinafe) lin_valinafe, "
                + "sum(t.pedcom_valexo) lin_valexo, "
                + "sum(t.pedcom_valimp) lin_valimp ,"
                + "sum(t.pedcom_valtot) lin_valtot "
                + "from v_listapedidocompradet t, ttabgen p,tpedcom_cab x where p.tab_id=to_number(codijisa.pack_tproductos.f_003_descLineaProducto(t.pro_id)) "
                + "and t.emp_id=x.emp_id and t.suc_id=x.suc_id and t.pedcom_key=x.pedcom_key and p.tab_cod=4 "
                + "and t.pedcom_est=1 and x.pedcom_est=1 and x.pedcom_sit like '" + sit + "' and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                + "and p.tab_id like '" + lin_key + "' and t.per_id like '" + periodo + "' " + validaFecha
                + " group by t.emp_id,t.suc_id,p.tab_id,p.tab_subdir, p.tab_subdes";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXLINEA);
            PedidoComLin objPedidoComLin;
            while (rs.next()) {
                objPedidoComLin = new PedidoComLin();
                objPedidoComLin.setEmp_id(rs.getInt("emp_id"));
                objPedidoComLin.setSuc_id(rs.getInt("suc_id"));
                objPedidoComLin.setLin_key(rs.getInt("lin_key"));
                objPedidoComLin.setLin_id(rs.getString("lin_id"));
                objPedidoComLin.setLin_des(rs.getString("lin_des"));
                objPedidoComLin.setCant(rs.getLong("lin_cant"));
                objPedidoComLin.setFecemi(fec_inicial);
                objPedidoComLin.setFecfin(fec_final);
                objPedidoComLin.setPeriodo(periodo);
                objPedidoComLin.setVdesc(rs.getDouble("lin_valdes"));
                objPedidoComLin.setVafecto(rs.getDouble("lin_valafe"));
                objPedidoComLin.setVinafecto(rs.getDouble("lin_valinafe"));
                objPedidoComLin.setVexonerado(rs.getDouble("lin_valexo"));
                objPedidoComLin.setVimpto(rs.getDouble("lin_valimp"));
                objPedidoComLin.setVtotal(rs.getDouble("lin_valtot"));
                objlstPedidoComLin.add(objPedidoComLin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstPedidoComLin.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPedidoComLin;
    }

    public ListModelList<PedidoComSublin> listaTotalxSubLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_CONSULTAXSUBLINEA;
        ListModelList<PedidoComSublin> objlstPedidoComSublin = new ListModelList<PedidoComSublin>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') ";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') ";
        }
        SQL_CONSULTAXSUBLINEA = "select t.emp_id,t.suc_id,p.slin_codslin slin_key ,p.slin_cod slin_id, p.slin_des slin_des,"
                + "count(distinct t.pedcom_key) slin_cant,"
                + "sum(t.pedcom_valdes) slin_valdes ,"
                + "sum(t.pedcom_valafe) slin_valafe, "
                + "sum(t.pedcom_valinafe) slin_valinafe, "
                + "sum(t.pedcom_valexo) slin_valexo, "
                + "sum(t.pedcom_valimp) slin_valimp ,"
                + "sum(t.pedcom_valtot) slin_valtot "
                + "from v_listapedidocompradet t, tsublineas p,tpedcom_cab x where p.slin_cod=to_number(codijisa.pack_tproductos.f_004_descslineaproducto(t.pro_id)) "
                + "and t.emp_id=x.emp_id and t.suc_id=x.suc_id and t.pedcom_key=x.pedcom_key "
                + "and t.pedcom_est=1 and x.pedcom_est=1 and x.pedcom_sit like '" + sit + "' and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                + "and p.slin_cod like '" + lin_key + "' and t.per_id like '" + periodo + "' " + validaFecha
                + " group by t.emp_id,t.suc_id,p.slin_codslin,p.slin_cod, p.slin_des";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXSUBLINEA);
            PedidoComSublin objPedidoComSublin;
            while (rs.next()) {
                objPedidoComSublin = new PedidoComSublin();
                objPedidoComSublin.setEmp_id(rs.getInt("emp_id"));
                objPedidoComSublin.setSuc_id(rs.getInt("suc_id"));
                objPedidoComSublin.setSlin_key(rs.getInt("slin_key"));
                objPedidoComSublin.setSlin_id(rs.getString("slin_id"));
                objPedidoComSublin.setSlin_des(rs.getString("slin_des"));
                objPedidoComSublin.setCant(rs.getLong("slin_cant"));
                objPedidoComSublin.setFecemi(fec_inicial);
                objPedidoComSublin.setFecfin(fec_final);
                objPedidoComSublin.setPeriodo(periodo);
                objPedidoComSublin.setVdesc(rs.getDouble("slin_valdes"));
                objPedidoComSublin.setVafecto(rs.getDouble("slin_valafe"));
                objPedidoComSublin.setVinafecto(rs.getDouble("slin_valinafe"));
                objPedidoComSublin.setVexonerado(rs.getDouble("slin_valexo"));
                objPedidoComSublin.setVimpto(rs.getDouble("slin_valimp"));
                objPedidoComSublin.setVtotal(rs.getDouble("slin_valtot"));
                objlstPedidoComSublin.add(objPedidoComSublin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstPedidoComSublin.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPedidoComSublin;
    }

    public ListModelList<PedidoCompraCab> PedidoCompraxProveedor(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id) throws SQLException {
        String validaFecha, SQL_LISTAPEDCOMP, titulo;
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PROVEEDOR DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "t.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PROVEEDOR DESDE 01/01/2000";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PROVEEDOR DESDE " + fec_inicial;
        } else {
            validaFecha = "t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PROVEEDOR DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        SQL_LISTAPEDCOMP = "select * from v_listapedidocompra t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pedcom_sit like '" + sit + "'  "
                + "and t.per_id like '" + periodo + "' and " + validaFecha + " t.prov_id like '" + prov_id + "' order by t.pedcom_key";

        P_WHERER = SQL_LISTAPEDCOMP;
        P_TITULO = titulo;
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPEDCOMP);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPer_key(rs.getInt("per_key"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_total(rs.getDouble("total"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));

                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public ListModelList<PedidoCompraCab> PedidoCompraxProducto(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String pro_id) throws SQLException {
        String validaFecha, SQL_LISTAPEDCOMP, titulo;
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PRODUCTO DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PRODUCTO HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PRODUCTO DESDE " + fec_inicial;
        } else {
            validaFecha = "and t.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR PRODUCTO DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        SQL_LISTAPEDCOMP = "select t.emp_id,t.suc_id,t.per_id,t.per_key,t.pedcom_id,t.pedcom_key,t.pedcom_fecemi,t.pedcom_fecrec,"
                + "t.pedcom_feccad,t.pedcom_est,t.pedcom_estdes,t.prov_id,t.prov_key,t.prov_ruc,t.prov_razsoc,"
                + "t.pedcom_mon,t.tab_subdes,t.pedcom_tipcam,t.pedcom_lispre,t.pedcom_lispredes,t.con_key,t.con_des,"
                + "t.pedcom_sit,t.pedcom_sitdes,t.pedcom_almori,t.alm_orides,t.pedcom_almdes,t.alm_desdes,"
                + "t.pedcom_glo,t.pedcom_usuadd,t.pedcom_usumod,t.pedcom_fecadd,t.pedcom_fecmod,sum(p.pedcom_valtot) total from v_listapedidocompra t, v_listapedidocompradet p where t.emp_id=p.emp_id and t.suc_id=p.suc_id "
                + "and t.pedcom_key=p.pedcom_key and t.pedcom_est=1 and p.pedcom_est=1 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pedcom_sit like '" + sit + "' "
                + "and p.pro_id like '" + pro_id + "' and t.per_id like '" + periodo + "' " + validaFecha + " "
                + "group by t.emp_id,t.suc_id,t.per_id,t.per_key,t.pedcom_id,t.pedcom_key,t.pedcom_fecemi,t.pedcom_fecrec,"
                + "t.pedcom_feccad,t.pedcom_est,t.pedcom_estdes,t.prov_id,t.prov_key,t.prov_ruc,t.prov_razsoc,"
                + "t.pedcom_mon,t.tab_subdes,t.pedcom_tipcam,t.pedcom_lispre,t.pedcom_lispredes,t.con_key,t.con_des,"
                + "t.pedcom_sit,t.pedcom_sitdes,t.pedcom_almori,t.alm_orides,t.pedcom_almdes,t.alm_desdes,"
                + "t.pedcom_glo,t.pedcom_usuadd,t.pedcom_usumod,t.pedcom_fecadd,t.pedcom_fecmod"
                + " order by t.pedcom_key";
        P_TITULO = titulo;
        P_WHERER = SQL_LISTAPEDCOMP;
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPEDCOMP);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPer_key(rs.getInt("per_key"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_total(rs.getDouble("total"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public ListModelList<PedidoCompraCab> PedidoCompraxLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_LISTAPEDCOMP, titulo;
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR LINEA DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR LINEA HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR LINEA DESDE " + fec_inicial;
        } else {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR LINEA DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        SQL_LISTAPEDCOMP = "select x.emp_id,x.suc_id,x.per_id,x.per_key,x.pedcom_id,x.pedcom_key,x.pedcom_fecemi,x.pedcom_fecrec,x.pedcom_feccad,x.pedcom_est,x.pedcom_estdes,x.prov_id,x.prov_key,x.prov_ruc,"
                + "x.prov_razsoc,x.pedcom_mon,x.tab_subdes,x.pedcom_tipcam,x.pedcom_lispre,x.pedcom_lispredes,x.con_key,x.con_des,x.pedcom_sit,x.pedcom_sitdes,x.pedcom_almori,x.alm_orides,x.pedcom_almdes,x.alm_desdes,"
                + "x.pedcom_glo,x.pedcom_usuadd,x.pedcom_usumod,x.pedcom_fecadd,x.pedcom_fecmod,sum(t.pedcom_valtot) total from v_listapedidocompra x ,v_listapedidocompradet t, ttabgen p "
                + "where x.pedcom_key=t.pedcom_key and x.emp_id=t.emp_id and x.suc_id=t.suc_id and p.tab_id like '" + lin_key + "' and p.tab_cod=4 "
                + "and to_number(codijisa.pack_tproductos.f_003_descLineaProducto(t.pro_id))=p.tab_id and t.pedcom_est=1 and x.pedcom_est=1 and x.pedcom_sit like '" + sit + "' and x.emp_id=" + emp_id + " and x.suc_id=" + suc_id + " "
                + "and t.per_id like '" + periodo + "' " + validaFecha + " group by x.emp_id,x.suc_id,x.per_id,x.per_key,x.pedcom_id,x.pedcom_key,x.pedcom_fecemi,x.pedcom_fecrec,x.pedcom_feccad,"
                + "x.pedcom_est,x.pedcom_estdes,x.prov_id,x.prov_key,x.prov_ruc,x.prov_razsoc,x.pedcom_mon,x.tab_subdes,x.pedcom_tipcam,x.pedcom_lispre,x.pedcom_lispredes,x.con_key,x.con_des,x.pedcom_sit,"
                + "x.pedcom_sitdes,x.pedcom_almori,x.alm_orides,x.pedcom_almdes,x.alm_desdes,x.pedcom_glo,x.pedcom_usuadd,x.pedcom_usumod,x.pedcom_fecadd,x.pedcom_fecmod order by x.pedcom_key";

        P_TITULO = titulo;
        P_WHERER = SQL_LISTAPEDCOMP;
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPEDCOMP);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPer_key(rs.getInt("per_key"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_total(rs.getDouble("total"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public ListModelList<PedidoCompraCab> PedidoCompraxSubLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_LISTAPEDCOMP, titulo;
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR SUBLINEA DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR SUBLINEA HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR SUBLINEA DESDE " + fec_inicial;
        } else {
            validaFecha = "and x.pedcom_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE PEDIDOS DE COMPRA POR SUBLINEA DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        SQL_LISTAPEDCOMP = "select x.emp_id,x.suc_id,x.per_id,x.per_key,x.pedcom_id,x.pedcom_key,x.pedcom_fecemi,x.pedcom_fecrec,x.pedcom_feccad,x.pedcom_est,x.pedcom_estdes,x.prov_id,x.prov_key,x.prov_ruc,"
                + "x.prov_razsoc,x.pedcom_mon,x.tab_subdes,x.pedcom_tipcam,x.pedcom_lispre,x.pedcom_lispredes,x.con_key,x.con_des,x.pedcom_sit,x.pedcom_sitdes,x.pedcom_almori,x.alm_orides,x.pedcom_almdes,x.alm_desdes,"
                + "x.pedcom_glo,x.pedcom_usuadd,x.pedcom_usumod,x.pedcom_fecadd,x.pedcom_fecmod,sum(t.pedcom_valtot) total from v_listapedidocompra x ,v_listapedidocompradet t, tsublineas p "
                + "where x.pedcom_key=t.pedcom_key and x.emp_id=t.emp_id and x.suc_id=t.suc_id and p.slin_cod like '" + lin_key + "' "
                + "and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(t.pro_id))=p.slin_cod and t.pedcom_est=1 and x.pedcom_est=1 and x.pedcom_sit like '" + sit + "' and x.emp_id=" + emp_id + " and x.suc_id=" + suc_id + " "
                + "and t.per_id like '" + periodo + "' " + validaFecha + " group by x.emp_id,x.suc_id,x.per_id,x.per_key,x.pedcom_id,x.pedcom_key,x.pedcom_fecemi,x.pedcom_fecrec,x.pedcom_feccad,"
                + "x.pedcom_est,x.pedcom_estdes,x.prov_id,x.prov_key,x.prov_ruc,x.prov_razsoc,x.pedcom_mon,x.tab_subdes,x.pedcom_tipcam,x.pedcom_lispre,x.pedcom_lispredes,x.con_key,x.con_des,x.pedcom_sit,"
                + "x.pedcom_sitdes,x.pedcom_almori,x.alm_orides,x.pedcom_almdes,x.alm_desdes,x.pedcom_glo,x.pedcom_usuadd,x.pedcom_usumod,x.pedcom_fecadd,x.pedcom_fecmod order by x.pedcom_key";

        P_TITULO = titulo;
        P_WHERER = SQL_LISTAPEDCOMP;
        objListaPedidoCompraCab = null;
        objListaPedidoCompraCab = new ListModelList<PedidoCompraCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPEDCOMP);
            while (rs.next()) {
                objPedidoCompraCab = new PedidoCompraCab();
                objPedidoCompraCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoCompraCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoCompraCab.setPer_id(rs.getString("per_id"));
                objPedidoCompraCab.setPer_key(rs.getInt("per_key"));
                objPedidoCompraCab.setPedcom_id(rs.getString("pedcom_id"));
                objPedidoCompraCab.setPedcom_key(rs.getLong("pedcom_key"));
                objPedidoCompraCab.setPedcom_fecemi(rs.getDate("pedcom_fecemi"));
                objPedidoCompraCab.setPedcom_fecrec(rs.getDate("pedcom_fecrec"));
                objPedidoCompraCab.setPedcom_feccad(rs.getDate("pedcom_feccad"));
                objPedidoCompraCab.setPedcom_est(rs.getInt("pedcom_est"));
                objPedidoCompraCab.setPedcom_estdes(rs.getString("pedcom_estdes"));
                objPedidoCompraCab.setProv_id(rs.getString("prov_id"));
                objPedidoCompraCab.setProv_key(rs.getLong("prov_key"));
                objPedidoCompraCab.setProv_ruc(rs.getLong("prov_ruc"));
                objPedidoCompraCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objPedidoCompraCab.setPedcom_mon(rs.getInt("pedcom_mon"));
                objPedidoCompraCab.setPedcom_mondes(rs.getString("tab_subdes"));
                objPedidoCompraCab.setPedcom_tipcam(rs.getDouble("pedcom_tipcam"));
                objPedidoCompraCab.setPedcom_lispre(rs.getString("pedcom_lispre"));
                objPedidoCompraCab.setPedcom_lispredes(rs.getString("pedcom_lispredes"));
                objPedidoCompraCab.setCon_key(rs.getInt("con_key"));
                objPedidoCompraCab.setCon_des(rs.getString("con_des"));
                objPedidoCompraCab.setPedcom_sit(rs.getInt("pedcom_sit"));
                objPedidoCompraCab.setPedcom_sitdes(rs.getString("pedcom_sitdes"));
                objPedidoCompraCab.setPedcom_almori(rs.getString("pedcom_almori"));
                objPedidoCompraCab.setPedcom_almorides(rs.getString("alm_orides"));
                objPedidoCompraCab.setPedcom_almdes(rs.getString("pedcom_almdes"));
                objPedidoCompraCab.setPedcom_almdesdes(rs.getString("alm_desdes"));
                objPedidoCompraCab.setPedcom_glo(rs.getString("pedcom_glo"));
                objPedidoCompraCab.setPedcom_total(rs.getDouble("total"));
                objPedidoCompraCab.setPedcom_usuadd(rs.getString("pedcom_usuadd"));
                objPedidoCompraCab.setPedcom_usumod(rs.getString("pedcom_usumod"));
                objPedidoCompraCab.setPedcom_fecadd(rs.getTimestamp("pedcom_fecadd"));
                objPedidoCompraCab.setPedcom_fecmod(rs.getTimestamp("pedcom_fecmod"));
                objListaPedidoCompraCab.add(objPedidoCompraCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoCompraCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaPedidoCompraCab;
    }

    public String obtieneProductoConDimp(String s_prod_id) throws SQLException {
        String SQL_STRING_PRODUCTOSCONDIMP, S_PRO_CONDIMP = null;
        SQL_STRING_PRODUCTOSCONDIMP = "select tp.pro_condimp from tproductos tp where tp.pro_id =" + s_prod_id;
        objProducto = new Productos();
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_STRING_PRODUCTOSCONDIMP);
            while (rs.next()) {
                S_PRO_CONDIMP = rs.getString("pro_condimp");
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaProductos.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return S_PRO_CONDIMP;
    }
}
