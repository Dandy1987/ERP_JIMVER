package org.me.gj.controller.facturacion.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.procesos.PedidoVentaCab;
import org.me.gj.model.facturacion.procesos.PedidoVentaDet;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPedVen {

    //Instancias de Objetos
    ListModelList<PedidoVentaCab> objListaPedidoVentaCab;
    ListModelList<PedidoVentaDet> objListaPedidoVentaDet;
    ListModelList<Productos> objListaProductos;
    PedidoVentaCab objPedidoVentaCab;
    PedidoVentaDet objPedidoVentaDet;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoPedVen.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarPedVenCab(PedidoVentaCab objPedVenCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        String nropedid;
        cst = null;
        String SQL_INSERTAR_PEDVENCAB = "{call pack_tpedven.p_insertarPedVenCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PEDVENCAB);
            cst.clearParameters();
            cst.setInt(1, objPedVenCab.getEmp_id());
            cst.setInt(2, objPedVenCab.getSuc_id());
            cst.setString(3, objPedVenCab.getPcab_tipven());
            cst.setString(4, objPedVenCab.getPcab_nrodni());
            cst.setString(5, objPedVenCab.getPcab_nroruc());
            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objPedVenCab.getPcab_fecemi());
            fecha_hora = sdf1.format(objPedVenCab.getPcab_fecemi());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(6, hora);
            cst.setString(7, objPedVenCab.getPcab_mon());
            cst.setInt(8, objPedVenCab.getPcab_modtipcam());
            cst.setDouble(9, objPedVenCab.getPcab_tipcam());
            cst.setString(10, objPedVenCab.getCli_id());
            cst.setDouble(11, objPedVenCab.getPcab_limcre());
            cst.setInt(12, objPedVenCab.getPcab_limdoc());
            cst.setDouble(13, objPedVenCab.getPcab_salcre());
            cst.setInt(14, objPedVenCab.getPcab_estado());
            cst.setInt(15, objPedVenCab.getClidir_id());
            cst.setString(16, objPedVenCab.getPcab_dirent());
            cst.setString(17, objPedVenCab.getZon_id());
            cst.setString(18, objPedVenCab.getPcab_giro());
            cst.setString(19, objPedVenCab.getPcab_canid());
            cst.setString(20, objPedVenCab.getVen_id());
            cst.setString(21, objPedVenCab.getSup_id());
            cst.setInt(22, objPedVenCab.getPcab_diavis());
            cst.setString(23, objPedVenCab.getLp_id());
            cst.setString(24, objPedVenCab.getCon_id());
            cst.setInt(25, objPedVenCab.getPcab_situacion());
            cst.setDouble(26, objPedVenCab.getPcab_totped());
            cst.setInt(27, objPedVenCab.getPcab_ppago());
            cst.setInt(28, objPedVenCab.getPcab_facbol());
            cst.setString(29, objPedVenCab.getPcab_horent());
            cst.setString(30, objPedVenCab.getPcab_motrec());
            cst.setString(31, objPedVenCab.getPcab_usuadd());
            cst.registerOutParameter(32, java.sql.Types.NUMERIC);
            cst.registerOutParameter(33, java.sql.Types.VARCHAR);
            cst.registerOutParameter(34, java.sql.Types.VARCHAR);
            cst.execute();
            nropedid = cst.getString(34);
            s_msg = cst.getString(33);
            i_flagErrorBD = cst.getInt(32);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un pedido de Venta con numero :" + nropedid);
        } catch (SQLException e) {
            nropedid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            nropedid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, nropedid);
    }

    public ParametrosSalida actualizarPedVenCab(PedidoVentaCab objPedVenCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_PEDVENCAB = "{call PACK_TPEDVEN.p_actualizarPedVenCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PEDVENCAB);
            cst.clearParameters();
            cst.setInt(1, objPedVenCab.getEmp_id());
            cst.setInt(2, objPedVenCab.getSuc_id());
            cst.setInt(3, objPedVenCab.getPcab_estado());
            cst.setString(4, objPedVenCab.getPcab_tipven());
            cst.setString(5, objPedVenCab.getPcab_nroped());
            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objPedVenCab.getPcab_fecemi());
            fecha_hora = sdf1.format(objPedVenCab.getPcab_fecemi());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)

            Time hora = new Time(fec_conc.getTime());
            cst.setTime(6, hora);
            //cst.setDate(6,sdf2.format(objPedVenCab.getPcab_fecemi()).);
            //cst.setDate(6, new java.sql.Date(objPedVenCab.getPcab_fecemi().getTime()));
            cst.setString(7, objPedVenCab.getPcab_mon());
            cst.setInt(8, objPedVenCab.getPcab_modtipcam());
            cst.setDouble(9, objPedVenCab.getPcab_tipcam());
            cst.setString(10, objPedVenCab.getCli_id());
            cst.setInt(11, objPedVenCab.getClidir_id());
            cst.setString(12, objPedVenCab.getPcab_dirent());
            cst.setString(13, objPedVenCab.getZon_id());
            cst.setString(14, objPedVenCab.getPcab_giro());
            cst.setString(15, objPedVenCab.getPcab_canid());
            cst.setString(16, objPedVenCab.getVen_id());
            cst.setString(17, objPedVenCab.getSup_id());
            cst.setInt(18, objPedVenCab.getPcab_diavis());
            cst.setString(19, objPedVenCab.getLp_id());
            cst.setString(20, objPedVenCab.getCon_id());
            cst.setInt(21, objPedVenCab.getPcab_ppago());
            cst.setDouble(22, objPedVenCab.getPcab_totped());
            cst.setInt(23, objPedVenCab.getPcab_facbol());
            cst.setString(24, objPedVenCab.getPcab_horent());
            cst.setString(25, objPedVenCab.getPcab_usuadd());
            cst.registerOutParameter(26, java.sql.Types.NUMERIC);
            cst.registerOutParameter(27, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(27);
            i_flagErrorBD = cst.getInt(26);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un Pedido de Venta con codigo " + objPedidoVentaCab.getPcab_tipven());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarPedidoVenta(PedidoVentaCab objPedidoVentaCab) throws SQLException {
        String SQL_ELIMINAR_PEDIDO_VENTA = "{call PACK_TPEDVEN.p_eliminarPedVenCab(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PEDIDO_VENTA);
            cst.clearParameters();
            cst.setString("c_pcab_nroped", objPedidoVentaCab.getPcab_nroped());
            cst.setInt("n_emp_id", objPedidoVentaCab.getEmp_id());
            cst.setInt("n_suc_id", objPedidoVentaCab.getSuc_id());
            cst.setString("c_pcab_usumod", objUsuCredential.getCuenta());
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

    public ParametrosSalida insertarPedidoVentaDet(PedidoVentaDet objPedidoVentaDet) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_PEDIDO_VENTADET = "{call PACK_TPEDVEN.p_insertarPedVenDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PEDIDO_VENTADET);
            cst.clearParameters();
            cst.setString(1, objPedidoVentaDet.getPcab_nroped());

            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objPedidoVentaDet.getPcab_fecemi());
            fecha_hora = sdf1.format(objPedidoVentaDet.getPcab_fecemi());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(2, hora);
            //cst.setDate(2, new java.sql.Date(objPedidoVentaDet.getPcab_fecemi().getTime()));
            cst.setInt(3, objPedidoVentaDet.getEmp_id());
            cst.setInt(4, objPedidoVentaDet.getSuc_id());
            cst.setInt(5, objPedidoVentaDet.getPdet_estado());
            cst.setInt(6, objPedidoVentaDet.getPdet_situacion());
            cst.setString(7, objPedidoVentaDet.getPro_id());
            cst.setString(8, objPedidoVentaDet.getPro_cla());
            cst.setString(9, objPedidoVentaDet.getLp_id());
            cst.setDouble(10, objPedidoVentaDet.getPdet_canped());
            cst.setDouble(11, objPedidoVentaDet.getPdet_punit());
            cst.setDouble(12, objPedidoVentaDet.getPdet_vventa());
            cst.setInt(13, objPedidoVentaDet.getPdet_desman());
            cst.setDouble(14, objPedidoVentaDet.getPdet_dscto());
            cst.setDouble(15, objPedidoVentaDet.getPdet_sdscto());
            cst.setDouble(16, objPedidoVentaDet.getPdet_impto());
            cst.setDouble(17, objPedidoVentaDet.getPdet_vimpto());
            cst.setDouble(18, objPedidoVentaDet.getPdet_pventa());
            cst.setDouble(19, objPedidoVentaDet.getPdet_unipre());
            cst.setString(20, objPedidoVentaDet.getPdet_usuadd());
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(22);
            i_flagErrorBD = cst.getInt(21);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto al Pedido de Venta " + objPedidoVentaDet.getPcab_nroped() + " con Codigo " + objPedidoVentaDet.getPro_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarPedidoVentaDet(PedidoVentaDet objPedidoVentaDet) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_PEDVENDET = "{call PACK_TPEDVEN.p_actualizarPedVenDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PEDVENDET);
            cst.clearParameters();
            cst.setInt(1, objPedidoVentaDet.getEmp_id());
            cst.setInt(2, objPedidoVentaDet.getSuc_id());
            cst.setString(3, objPedidoVentaDet.getPcab_nroped());

            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objPedidoVentaDet.getPcab_fecemi());
            fecha_hora = sdf1.format(objPedidoVentaDet.getPcab_fecemi());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(4, hora);
            //cst.setDate(4, new java.sql.Date(objPedidoVentaDet.getPcab_fecemi().getTime()));
            cst.setInt(5, objPedidoVentaDet.getPdet_item());
            cst.setString(6, objPedidoVentaDet.getPro_id());
            cst.setString(7, objPedidoVentaDet.getPro_cla());
            cst.setString(8, objPedidoVentaDet.getLp_id());
            cst.setDouble(9, objPedidoVentaDet.getPdet_canped());
            cst.setDouble(10, objPedidoVentaDet.getPdet_punit());
            //cst.setInt(5, objPedidoVentaDet.getPdet_estado());
            //cst.setInt(6, objPedidoVentaDet.getPdet_situacion());
            cst.setDouble(11, objPedidoVentaDet.getPdet_vventa());
            cst.setInt(12, objPedidoVentaDet.getPdet_desman());
            cst.setDouble(13, objPedidoVentaDet.getPdet_dscto());
            cst.setDouble(14, objPedidoVentaDet.getPdet_sdscto());
            cst.setDouble(15, objPedidoVentaDet.getPdet_impto());
            cst.setDouble(16, objPedidoVentaDet.getPdet_vimpto());
            cst.setDouble(17, objPedidoVentaDet.getPdet_pventa());
            cst.setDouble(18, objPedidoVentaDet.getPdet_unipre());
            cst.setString(19, objPedidoVentaDet.getPdet_usumod());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto del Pedido de Venta " + objPedidoVentaDet.getPcab_nroped() + " con Codigo " + objPedidoVentaDet.getPro_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarPedidoVentaDet(PedidoVentaDet objPedidoVentaDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_ORDCOMPCAB = "{call PACK_TPEDVEN.p_eliminarPedVenDet(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_ORDCOMPCAB);
            cst.clearParameters();
            cst.setInt(1, objPedidoVentaDet.getEmp_id());
            cst.setInt(2, objPedidoVentaDet.getSuc_id());
            cst.setString(3, objPedidoVentaDet.getPcab_nroped());
            cst.setInt(4, objPedidoVentaDet.getPdet_item());
            cst.setString(5, objPedidoVentaDet.getPdet_usumod());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino el Pedido de Venta con codigo " + objPedidoVentaDet.getPcab_nroped());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida activarAprobEspecial(String nropedido, String mot_rec) throws SQLException {
        String SQL_ACTIVAR_PEDIDO_VENTA = "{call PACK_TPEDVEN.p_activarAprobEspecial(?,?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ACTIVAR_PEDIDO_VENTA);
            cst.clearParameters();
            cst.setString("c_pcab_nroped", nropedido);
            cst.setString("c_motrec", mot_rec);
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_pcab_usumod", objUsuCredential.getCuenta());
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

    public ParametrosSalida activarPedidoVenta(String nropedido) throws SQLException {
        String SQL_ACTIVAR_PEDIDO_VENTA = "{call PACK_TPEDVEN.p_activarPedVenta(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ACTIVAR_PEDIDO_VENTA);
            cst.clearParameters();
            cst.setString("c_pcab_nroped", nropedido);
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_pcab_usumod", objUsuCredential.getCuenta());
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

    public ParametrosSalida actualizarSituRevisado(String nropedido) throws SQLException {
        String SQL_ACTUALIZAR_SITU_REVISADO = "{call PACK_TPEDVEN.p_actualizarSituRevisado(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_SITU_REVISADO);
            cst.clearParameters();
            cst.setString("c_pcab_nroped", nropedido);
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_pcab_usumod", objUsuCredential.getCuenta());
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

    public ParametrosSalida recuperarPedidoVenta(PedidoVentaCab objPedidoVentaCab) throws SQLException {
        String SQL_RECUPERAR_PEDIDO_VENTA = "{call PACK_TPEDVEN.p_recuperarPedVenta(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_RECUPERAR_PEDIDO_VENTA);
            cst.clearParameters();
            cst.setString("c_pcab_nroped", objPedidoVentaCab.getPcab_nroped());
            cst.setInt("n_emp_id", objPedidoVentaCab.getEmp_id());
            cst.setInt("n_suc_id", objPedidoVentaCab.getSuc_id());
            cst.setString("c_pcab_usumod", objUsuCredential.getCuenta());
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

    public ParametrosSalida actualizaIndicador(int emp_id, int suc_id, String pedido, int indicador) throws SQLException {
        String SQL_ACTUALIZAR_INDICE_PEDVENTA = "{call PACK_TPEDVEN.p_actualizarIndicador(?,?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_INDICE_PEDVENTA);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_pcab_nroped", pedido);
            cst.setInt("n_indicador", indicador);
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

    public ParametrosSalida generarNotaVenta(int tiponota, String nota, String serie, String almacen, int emp_id, int suc_id, /*String fecha,*/ Date fecha,
            Object[][] listapedvencab, String usuario) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        ARRAY arrCab;
        ArrayDescriptor arrayCab;
        String SQL_GENERAR_NOTAVENTA = "{call pack_tpedven.p_notaSalidaVentas(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_NOTAVENTA);
            arrayCab = ArrayDescriptor.createDescriptor("LISTAPEDVENCAB", con.getMetaData().getConnection());
            arrCab = new ARRAY(arrayCab, con.getMetaData().getConnection(), listapedvencab);
            cst.clearParameters();
            cst.setInt(1, tiponota);
            cst.setString(2, nota);
            cst.setString(3, serie);
            cst.setString(4, almacen);
            cst.setInt(5, emp_id);
            cst.setInt(6, suc_id);
            //cst.setString(7, fecha);
            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(fecha);
            fecha_hora = sdf1.format(fecha);
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(7, hora);
            cst.setArray(8, arrCab);
            cst.setString(9, usuario);
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero la nota de venta");
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar la nota de venta debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar la nota de venta debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<PedidoVentaCab> listaPedidoVenCab(int emp_id, int suc_id, String situacion, String fechai, String fechaf, int estado) throws SQLException {
        String SQL_LISTAR_PEDIDO_VENTACAB;
        if (estado == 1) {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.pcab_situacion like '" + situacion + "' "
                    + " and t.pcab_estado=" + estado + " order by t.emp_id,t.suc_id,t.pcab_periodo,t.pcab_fecemi,t.pcab_nroped asc";

        } else {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.pcab_situacion like '" + situacion + "' "
                    + " and t.pcab_estado=" + estado + " order by t.emp_id,t.suc_id,t.pcab_periodo,t.pcab_fecemi,t.pcab_nroped asc";
        }

        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        P_WHERE = SQL_LISTAR_PEDIDO_VENTACAB;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_PEDIDO_VENTACAB);
            while (rs.next()) {
                objPedidoVentaCab = new PedidoVentaCab();
                objPedidoVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaCab.setPcab_fecemi(rs.getDate("pcab_fecemi"));
                objPedidoVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objPedidoVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objPedidoVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objPedidoVentaCab.setZon_id(rs.getString("zon_id"));
                objPedidoVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").length() < 12 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 12));
                objPedidoVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objPedidoVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objPedidoVentaCab.setCli_id(rs.getString("cli_id"));
                objPedidoVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objPedidoVentaCab.setVen_id(rs.getString("ven_id"));
                objPedidoVentaCab.setSup_id(rs.getString("sup_id"));
                objPedidoVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objPedidoVentaCab.setCon_id(rs.getString("con_id"));
                objPedidoVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objPedidoVentaCab.setLp_id(rs.getString("lp_id"));
                objPedidoVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objPedidoVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objPedidoVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objPedidoVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objPedidoVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objPedidoVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objPedidoVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objPedidoVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objPedidoVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objPedidoVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objPedidoVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objPedidoVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objPedidoVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objPedidoVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objPedidoVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objPedidoVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objPedidoVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objPedidoVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objPedidoVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objPedidoVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objPedidoVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objPedidoVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                //objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 35 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 35));
                objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 37 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 37));
                objPedidoVentaCab.setVen_des(rs.getString("ven_des").length() < 35 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 35));
                objPedidoVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                objPedidoVentaCab.setZon_des(rs.getString("zon_des"));
                objPedidoVentaCab.setLp_des(rs.getString("lp_des"));
                objPedidoVentaCab.setCond_des(rs.getString("con_des"));
                objPedidoVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objPedidoVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objPedidoVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objPedidoVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objPedidoVentaCab.setIndicador(rs.getInt("pcab_ind"));
                objListaPedidoVentaCab.add(objPedidoVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaCab.getSize() + " registro(s)");
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
        return objListaPedidoVentaCab;
    }

    public ListModelList<PedidoVentaCab> busquedaPedidoVenta(int emp_id, int suc_id, String fechai, String supervisor, String vendedor, String situacion, int estado, String periodo) throws SQLException {
        String SQL_LISTAR_PEDIDO_VENTACAB;
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.pcab_periodo=" + periodo + " ";
        String s_validafec = fechai.isEmpty() ? "" : " and t.PCAB_FECEMI like to_date('" + fechai + "','dd/mm/yyyy') ";
        String s_validavendedor = vendedor.isEmpty() ? "" : " and t.ven_id like '" + vendedor + "'";
        String s_validasupervisor = supervisor.isEmpty() ? "" : " and t.sup_id like '" + supervisor + "' ";

        if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + " and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validaperiodo + " order by t.pcab_nroped";
        } else if (periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + " and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validasupervisor + " order by t.pcab_nroped";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + " and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + " order by t.pcab_nroped";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fechai.isEmpty()) {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + " and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validafec + " order by t.pcab_nroped";
        } else {
            SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + " and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + s_validasupervisor + s_validafec + s_validaperiodo + " order by t.pcab_nroped";
        }
        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        P_WHERE = SQL_LISTAR_PEDIDO_VENTACAB;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_PEDIDO_VENTACAB);
            while (rs.next()) {
                objPedidoVentaCab = new PedidoVentaCab();
                objPedidoVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaCab.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objPedidoVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objPedidoVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objPedidoVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objPedidoVentaCab.setZon_id(rs.getString("zon_id"));
                objPedidoVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                //objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des"));
                objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").length() < 9 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 9));
                //objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? "" : rs.getString("pcab_motrec_des").trim().replace(" ", "."));
                objPedidoVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objPedidoVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objPedidoVentaCab.setCli_id(rs.getString("cli_id"));
                objPedidoVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objPedidoVentaCab.setVen_id(rs.getString("ven_id"));
                objPedidoVentaCab.setSup_id(rs.getString("sup_id"));
                objPedidoVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objPedidoVentaCab.setCon_id(rs.getString("con_id"));
                objPedidoVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objPedidoVentaCab.setLp_id(rs.getString("lp_id"));
                objPedidoVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objPedidoVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objPedidoVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objPedidoVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objPedidoVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objPedidoVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objPedidoVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objPedidoVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objPedidoVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objPedidoVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objPedidoVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objPedidoVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objPedidoVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objPedidoVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objPedidoVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objPedidoVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objPedidoVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objPedidoVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objPedidoVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objPedidoVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objPedidoVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objPedidoVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                //objPedidoVentaCab.setCli_des(rs.getString("cli_des"));
                //objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 35 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 35));
                objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 32 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 32));
                objPedidoVentaCab.setVen_des(rs.getString("ven_des").length() < 32 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 32));
                objPedidoVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                //objPedidoVentaCab.setpcab_estado(rs.getString("pcab_estado_des"));
                objPedidoVentaCab.setZon_des(rs.getString("zon_des"));
                objPedidoVentaCab.setLp_des(rs.getString("lp_des"));
                objPedidoVentaCab.setCond_des(rs.getString("con_des"));
                objPedidoVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objPedidoVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objPedidoVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objPedidoVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objPedidoVentaCab.setIndicador(rs.getInt("pcab_ind"));

                objListaPedidoVentaCab.add(objPedidoVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaCab.getSize() + " registro(s)");
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
        return objListaPedidoVentaCab;
    }

    public ListModelList<PedidoVentaCab> busquedaPedidoVentaxTipo(int emp_id, int suc_id, String fechai, String supervisor, String vendedor, int situacion, int estado, String tipo) throws SQLException {
        String SQL_LISTAR_PEDIDO_VENTACAB = "select * from v_listapedidoventcab t where  t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ven_id like '" + vendedor + "'"
                + " and t.sup_id like '" + supervisor + "' and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado + " and t.pcab_tipven like '" + tipo + "'"
                + " order by t.emp_id,t.suc_id,t.pcab_periodo,t.pcab_fecemi,t.pcab_nroped asc";

        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_PEDIDO_VENTACAB);
            while (rs.next()) {
                objPedidoVentaCab = new PedidoVentaCab();
                objPedidoVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaCab.setPcab_fecemi(rs.getDate("pcab_fecemi"));
                objPedidoVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objPedidoVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objPedidoVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objPedidoVentaCab.setZon_id(rs.getString("zon_id"));
                objPedidoVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                objPedidoVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objPedidoVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objPedidoVentaCab.setCli_id(rs.getString("cli_id"));
                objPedidoVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objPedidoVentaCab.setVen_id(rs.getString("ven_id"));
                objPedidoVentaCab.setSup_id(rs.getString("sup_id"));
                objPedidoVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objPedidoVentaCab.setCon_id(rs.getString("con_id"));
                objPedidoVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objPedidoVentaCab.setLp_id(rs.getString("lp_id"));
                objPedidoVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objPedidoVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objPedidoVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objPedidoVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objPedidoVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objPedidoVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objPedidoVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objPedidoVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objPedidoVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objPedidoVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objPedidoVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objPedidoVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objPedidoVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objPedidoVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objPedidoVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objPedidoVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objPedidoVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objPedidoVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objPedidoVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objPedidoVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objPedidoVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objPedidoVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objPedidoVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                objPedidoVentaCab.setCli_des(rs.getString("cli_des"));
                objPedidoVentaCab.setVen_des(rs.getString("ven_des").length() < 25 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 25));
                objPedidoVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                //objPedidoVentaCab.setpcab_estado(rs.getString("pcab_estado_des"));
                objPedidoVentaCab.setZon_des(rs.getString("zon_des"));
                objPedidoVentaCab.setLp_des(rs.getString("lp_des"));
                objPedidoVentaCab.setCond_des(rs.getString("con_des"));
                objPedidoVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objPedidoVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objPedidoVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objListaPedidoVentaCab.add(objPedidoVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaCab.getSize() + " registro(s)");
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
        return objListaPedidoVentaCab;
    }

    public ListModelList<PedidoVentaDet> listaPedidoVentaDet(int emp_id, int suc_id, String nropedven) throws SQLException {
        String SQL_PEDIDO_VENTA_DET = " select * from v_listapedidoventdet t"
                + " where t.pcab_nroped=" + nropedven + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pdet_estado<>0"
                + " order by t.pdet_item";
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PEDIDO_VENTA_DET);
            objListaPedidoVentaDet = new ListModelList();
            while (rs.next()) {
                objPedidoVentaDet = new PedidoVentaDet();
                objPedidoVentaDet.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaDet.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objPedidoVentaDet.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaDet.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaDet.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaDet.setPdet_estado(rs.getInt("pdet_estado"));
                objPedidoVentaDet.setPdet_situacion(rs.getInt("pdet_situacion"));
                objPedidoVentaDet.setPro_id(rs.getString("pro_id"));
                objPedidoVentaDet.setPro_cla(rs.getString("pro_cla"));
                objPedidoVentaDet.setLp_id(rs.getString("lp_id"));
                objPedidoVentaDet.setLp_des(rs.getString("lp_des"));
                objPedidoVentaDet.setPdet_canped(rs.getInt("pdet_canped"));
                objPedidoVentaDet.setPdet_canent(rs.getInt("pdet_canent"));
                objPedidoVentaDet.setPdet_punit(rs.getDouble("pdet_punit"));
                objPedidoVentaDet.setPdet_vventa(rs.getDouble("pdet_vventa"));
                objPedidoVentaDet.setPdet_dscto(rs.getDouble("pdet_dscto"));
                objPedidoVentaDet.setPdet_sdscto(rs.getDouble("pdet_sdscto"));
                objPedidoVentaDet.setPdet_impto(rs.getDouble("pdet_impto"));
                objPedidoVentaDet.setPdet_vimpto(rs.getDouble("pdet_vimpto"));
                objPedidoVentaDet.setPdet_pventa(rs.getDouble("pdet_pventa"));
                objPedidoVentaDet.setPdet_unipre(rs.getInt("pdet_unipre"));
                objPedidoVentaDet.setPdet_desart(rs.getString("pdet_desart"));
                objPedidoVentaDet.setPdet_desman(rs.getInt("pdet_desman"));
                objPedidoVentaDet.setPdet_proper(rs.getDouble("pdet_proper"));
                objPedidoVentaDet.setPdet_item(rs.getInt("pdet_item"));
                //objPedidoVentaDet.setPro_presminven(rs.getDouble("pro_presminven"));
                objPedidoVentaDet.setPdet_usuadd(rs.getString("pdet_usuadd"));
                //objPedidoVentaDet.setPdet_fecadd(rs.getDate("pdet_fecadd"));
                objPedidoVentaDet.setPdet_usumod(rs.getString("pdet_usumod"));
                //objPedidoVentaDet.setPdet_fecmod(rs.getDouble("pdet_fecmod"));
                objPedidoVentaDet.setPdet_prodes(rs.getString("pro_des"));
                objListaPedidoVentaDet.add(objPedidoVentaDet);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaDet.getSize() + " registro(s)");
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
        return objListaPedidoVentaDet;
    }

    //@SuppressWarnings("deprecation")
    public ListModelList<PedidoVentaCab> busquedaPedidoPendiente(String situacion, String periodo, String supervisor, String motivo, String fecha, String vendedor, int estado, int orden) throws SQLException {
        String SQLLISTAPEDIDOPENDIENTE;
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.pcab_periodo =" + periodo + " ";
        String s_validafec = fecha.isEmpty() ? "" : " and t.pcab_fecemi like to_date('" + fecha + "','dd/mm/yyyy') ";
        String s_validavendedor = vendedor.isEmpty() ? "" : " and t.ven_id like '" + vendedor + "'";
        String s_validasupervisor = supervisor.isEmpty() ? "" : " and t.sup_id like '" + supervisor + "' ";
        String s_motivo = motivo.isEmpty() ? "" : " and t.pcab_motrec like '" + motivo + "' ";
        String s_orden = "";
        if (orden == 1) {
            s_orden = "t.pcab_fecemi";
        } else if (orden == 2) {
            s_orden = "t.pcab_totped asc";
        } else if (orden == 3) {
            s_orden = "t.pcab_totped desc";
        }
        //TODOS LOS CAMPOS LLENOS
        if (!periodo.isEmpty() && !supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } //PERIODO LLENO - SUPERVISOR VACIO - VENDEDOR VACIO - FECHA VACIO - MOTIVO VACIO 
        else if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                //PERIODO LLENO - SUPERVISOR LLENO - VENDEDOR VACIO - FECHA VACIO - MOTIVO VACIO
                || periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                //PERIODO LLENO - SUPERVISOR VACIO - VENDEDOR LLENO - FECHA VACIO - MOTIVO VACIO
                || periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                //PERIODO LLENO - SUPERVISOR LLENO - VENDEDOR VACIO - FECHA VACIO - MOTIVO LLENO  
                || periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()
                //PERIODO LLENO - SUPERVISOR VACIO - VENDEDOR VACIO - FECHA LLENO - MOTIVO VACIO
                || periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO VACIO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO VACIO
        else if (periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO VACIO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO VACIO
                || periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO VACIO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO LLENO
                || periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO VACIO
        else if (!periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO VACIO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO LLENO
        else if (!periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA VACIO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA VACIO - MOTIVO LLENO
                || !periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR LLENO - FECHA VACIO - MOTIVO VACIO
                || !periodo.isEmpty() && !supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA VACIO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO VACIO
        else if (!periodo.isEmpty() && !supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR LLENO - FECHA VACIO - MOTIVO LLENO
                || !periodo.isEmpty() && !supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO VACIO
        else if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA VACIO - MOTIVO VACIO   
                || !periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO VACIO
                || !periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fecha.isEmpty() && motivo.isEmpty()
                // PERIODO LLENO  - SUPERVISOR VACIO  - VENDEDOR VACIO - FECHA VACIO - MOTIVO LLENO
                || !periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fecha.isEmpty() && !motivo.isEmpty()) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + "and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by " + s_orden;
        } // PERIODO VACIO  - SUPERVISOR VACIO  - VENDEDOR VACIO - FECHA LLENO - MOTIVO VACIO
        else if (periodo.isEmpty() && !supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty()
                // PERIODO VACIO  - SUPERVISOR LLENO  - VENDEDOR VACIO - FECHA VACIO - MOTIVO VACIO   
                || periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() && !motivo.isEmpty() /*// PERIODO VACIO  - SUPERVISOR VACIO  - VENDEDOR LLENO - FECHA LLENO - MOTIVO VACIO
                 ||periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && !fecha.isEmpty() &&  motivo.isEmpty()
                 // PERIODO VACIO  - SUPERVISOR VACIO  - VENDEDOR VACIO - FECHA VACIO - MOTIVO LLENO
                 ||periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fecha.isEmpty() &&  !motivo.isEmpty()*/) {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  t.pcab_estado=" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + " order by  " + s_orden;
        } else {
            SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab t where t.suc_id=" + objUsuCredential.getCodsuc() + " and t.emp_id=" + objUsuCredential.getCodemp()
                    + " and t.pcab_situacion in (" + situacion + ") and  (t.pcab_estado =" + estado + " or t.pcab_estado =" + estado + s_validaperiodo
                    + s_validavendedor + s_validasupervisor + s_validafec + s_motivo + ") order by " + s_orden;
        }
        /*SQLLISTAPEDIDOPENDIENTE = "select * from v_listapedidoventcab c "
         + "where c.sup_id like '" + s_supervisor + "' and c.pcab_fecemi like to_date('" + s_fecha + "', 'dd/MM/yy') "
         + "and c.pcab_periodo like '" + s_periodo + "' and c.ven_id like '" + s_vendedor + "' and c.pcab_motrec like '" + s_motivo + "' and pcab_situacion=" + i_situacion
         + " order by c.pcab_nroped ";*/
        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQLLISTAPEDIDOPENDIENTE);
            while (rs.next()) {
                objPedidoVentaCab = new PedidoVentaCab();
                objPedidoVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaCab.setPcab_fecemi(rs.getDate("pcab_fecemi"));
                objPedidoVentaCab.setHora(rs.getTime("pcab_fecemi"));
                objPedidoVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objPedidoVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objPedidoVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objPedidoVentaCab.setZon_id(rs.getString("zon_id"));
                objPedidoVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                //objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des").length() < 16 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 16));
                objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? "" : rs.getString("pcab_motrec_des").trim().replace(" ", "."));
                //objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? "" : rs.getString("pcab_motrec_des").length() < 17 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 17));
                objPedidoVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objPedidoVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objPedidoVentaCab.setCli_id(rs.getString("cli_id"));
                objPedidoVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objPedidoVentaCab.setVen_id(rs.getString("ven_id"));
                objPedidoVentaCab.setSup_id(rs.getString("sup_id"));
                objPedidoVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objPedidoVentaCab.setCon_id(rs.getString("con_id"));
                objPedidoVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objPedidoVentaCab.setLp_id(rs.getString("lp_id"));
                objPedidoVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objPedidoVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objPedidoVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objPedidoVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objPedidoVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objPedidoVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objPedidoVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objPedidoVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objPedidoVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objPedidoVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objPedidoVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objPedidoVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objPedidoVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objPedidoVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objPedidoVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objPedidoVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objPedidoVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objPedidoVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objPedidoVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objPedidoVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objPedidoVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objPedidoVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objPedidoVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                //objPedidoVentaCab.setCli_des(rs.getString("cli_des"));
                //objPedidoVentaCab.setVen_des(rs.getString("ven_des"));
                objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 24 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 24));
                objPedidoVentaCab.setVen_des(rs.getString("ven_des").length() < 24 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 24));
                objPedidoVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                //objPedidoVentaCab.setpcab_estado(rs.getString("pcab_estado_des"));
                objPedidoVentaCab.setZon_des(rs.getString("zon_des"));
                objPedidoVentaCab.setLp_des(rs.getString("lp_des"));
                objPedidoVentaCab.setCond_des(rs.getString("con_des"));
                objPedidoVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objPedidoVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objPedidoVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objListaPedidoVentaCab.add(objPedidoVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaCab.getSize() + " registro(s)");
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
        return objListaPedidoVentaCab;
    }

    public ListModelList<PedidoVentaCab> busquedaPedidoVentaNotaPendiente(String fechai, String supervisor, String vendedor, String pedido, String situacion, int estado, String periodo, String orden) throws SQLException {
        String SQL_LISTAR_NOTAPEN_VENTACAB;
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.pcab_periodo=" + periodo + " ";
        String s_validafec = fechai.isEmpty() ? "" : " and t.PCAB_FECEMI like to_date('" + fechai + "','dd/mm/yyyy') ";
        String s_validavendedor = vendedor.isEmpty() ? "" : " and t.ven_id like '" + vendedor + "'";
        String s_validasupervisor = supervisor.isEmpty() ? "" : " and t.sup_id like '" + supervisor + "' ";
        String s_validapedido = pedido.isEmpty() ? "" : " and t.pcab_nroped like '" + pedido + "' ";

        if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotapendientes t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validaperiodo + s_validapedido + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotapendientes t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validasupervisor + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotapendientes t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotapendientes t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validafec + " order by t." + orden + " ASC ";
        } else {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotapendientes t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.pcab_situacion like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + s_validasupervisor + s_validapedido + s_validafec + s_validaperiodo + " order by t." + orden + " ASC ";
        }
        objListaPedidoVentaCab = new ListModelList<PedidoVentaCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_NOTAPEN_VENTACAB);
            while (rs.next()) {
                objPedidoVentaCab = new PedidoVentaCab();
                objPedidoVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objPedidoVentaCab.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objPedidoVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objPedidoVentaCab.setSuc_id(rs.getInt("suc_id"));
                objPedidoVentaCab.setEmp_id(rs.getInt("emp_id"));
                objPedidoVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objPedidoVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objPedidoVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objPedidoVentaCab.setZon_id(rs.getString("zon_id"));
                objPedidoVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                objPedidoVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").length() < 12 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 12));
                objPedidoVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objPedidoVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objPedidoVentaCab.setCli_id(rs.getString("cli_id"));
                objPedidoVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objPedidoVentaCab.setVen_id(rs.getString("ven_id"));
                objPedidoVentaCab.setSup_id(rs.getString("sup_id"));
                objPedidoVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objPedidoVentaCab.setCon_id(rs.getString("con_id"));
                objPedidoVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objPedidoVentaCab.setLp_id(rs.getString("lp_id"));
                objPedidoVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objPedidoVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objPedidoVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objPedidoVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objPedidoVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objPedidoVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objPedidoVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objPedidoVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objPedidoVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objPedidoVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objPedidoVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objPedidoVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objPedidoVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objPedidoVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objPedidoVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objPedidoVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objPedidoVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objPedidoVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objPedidoVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objPedidoVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objPedidoVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objPedidoVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                objPedidoVentaCab.setCli_des(rs.getString("cli_des").length() < 35 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 35));
                objPedidoVentaCab.setVen_des(rs.getString("ven_des").length() < 35 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 35));
                objPedidoVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                objPedidoVentaCab.setZon_des(rs.getString("zon_des"));
                objPedidoVentaCab.setLp_des(rs.getString("lp_des"));
                objPedidoVentaCab.setCond_des(rs.getString("con_des"));
                objPedidoVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objPedidoVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objPedidoVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objPedidoVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objPedidoVentaCab.setIndicador(rs.getInt("pcab_ind"));
                objPedidoVentaCab.setNotestip(rs.getString("notestip") == null ? "" : rs.getString("notestip"));
                objPedidoVentaCab.setNotesdes(rs.getString("notesdes") == null ? "" : rs.getString("notesdes"));
                objPedidoVentaCab.setNotesnro(rs.getString("notesnro") == null ? "" : rs.getString("notesnro"));
                objPedidoVentaCab.setSup_des(rs.getString("sup_des") == null ? "" : rs.getString("sup_des"));
                objListaPedidoVentaCab.add(objPedidoVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaPedidoVentaCab.getSize() + " registro(s)");
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
        return objListaPedidoVentaCab;
    }

    public String obtieneImpuesto(int n_tipoimp) throws SQLException {
        String SQL_STRING_PRODUCTOSCONDIMP, S_PRO_CONDIMP = null;
        SQL_STRING_PRODUCTOSCONDIMP = "select imp_valor from timpuestos where imp_id=" + n_tipoimp;
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_STRING_PRODUCTOSCONDIMP);
            while (rs.next()) {
                S_PRO_CONDIMP = rs.getString("imp_valor");
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
