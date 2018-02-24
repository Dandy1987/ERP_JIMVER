package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.TipoCambio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoTipoCambio {

    //Instancia de Objetos
    ListModelList<TipoCambio> objlstTipoCambio;
    TipoCambio objTipoCambio;
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
    private static Logger LOGGER = Logger.getLogger(DaoTipoCambio.class);

    //Eventos Primarios - Transaccionales
    public String insertarTipoCambio(TipoCambio objTipoCambio) throws SQLException {
        String SQL_INSERTAR_TIPOCAMBIO = "{call pack_tipocambio.p_insertarTipoCambio(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TIPOCAMBIO);
            cst.clearParameters();
            cst.setDate(1, new java.sql.Date(objTipoCambio.getDia_fec().getTime()));
            cst.setInt(2, objTipoCambio.getTab_id());
            cst.setDouble(3, objTipoCambio.getTcam_com());
            cst.setDouble(4, objTipoCambio.getTcam_conv());
            cst.setDouble(5, objTipoCambio.getTcam_conc());
            cst.setString(6, objTipoCambio.getTcam_usuadd());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + objTipoCambio.getTab_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_msg;
    }

    public String actualizarTipoCambio(TipoCambio objTipoCambio) throws SQLException {
        String SQL_ACTUALIZAR_TIPOCAMBIO = "{call pack_tipocambio.p_actualizarTipoCambio(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TIPOCAMBIO);
            cst.clearParameters();
            cst.setInt(1, objTipoCambio.getTcam_key());
            cst.setDate(2, new java.sql.Date(objTipoCambio.getDia_fec().getTime()));
            cst.setInt(3, objTipoCambio.getTab_id());
            cst.setInt(4, objTipoCambio.getTcam_est());
            cst.setDouble(5, objTipoCambio.getTcam_com());
            cst.setDouble(6, objTipoCambio.getTcam_conv());
            cst.setDouble(7, objTipoCambio.getTcam_conc());
            cst.setString(8, objTipoCambio.getTcam_usumod());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objTipoCambio.getTcam_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    public String eliminarTipoCambio(TipoCambio objTipoCambio) throws SQLException {
        String SQL_ELIMINAR_TIPOCAMBIO = "{call pack_tipocambio.p_eliminarTipoCambio(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_TIPOCAMBIO);
            cst.clearParameters();
            cst.setInt(1, objTipoCambio.getTcam_key());
            cst.setDate(2, new java.sql.Date(objTipoCambio.getDia_fec().getTime()));
            cst.setInt(3, objTipoCambio.getTab_id());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objTipoCambio.getTcam_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<TipoCambio> listaTipoCambio(int i_caso, String periodo) throws SQLException {
        String SQL_TIPOCAMBIO;
        if (i_caso == 0) {
            SQL_TIPOCAMBIO = "select t.tcam_key,t.tcam_id,t.dia_fec,t.tab_cod,t.tab_id,t1.tab_subdes,t.tcam_est,t.tcam_com,t.tcam_conv,"
                    + "t.tcam_conc,t.tcam_usuadd,t.tcam_fecadd,t.tcam_usumod,t.tcam_fecmod from ttip_cam t,ttabgen t1 where t.tab_id=t1.tab_id "
                    + "and t.tab_cod=t1.tab_cod and t.tab_cod=24 and to_char(t.dia_fec,'yyyymm') = '" + periodo + "' and t.tcam_est <> '" + i_caso + "' order by t.dia_fec,t.tcam_key";
        } else {
            SQL_TIPOCAMBIO = "select t.tcam_key,t.tcam_id,t.dia_fec,t.tab_cod,t.tab_id,t1.tab_subdes,t.tcam_est,t.tcam_com,t.tcam_conv,"
                    + "t.tcam_conc,t.tcam_usuadd,t.tcam_fecadd,t.tcam_usumod,t.tcam_fecmod from ttip_cam t,ttabgen t1 where t.tab_id=t1.tab_id "
                    + "and t.tab_cod=t1.tab_cod and t.tab_cod=24 and to_char(t.dia_fec,'yyyymm') = '" + periodo + "' and t.tcam_est = '" + i_caso + "' order by t.dia_fec,t.tcam_key";;
        }
        P_WHERE = SQL_TIPOCAMBIO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPOCAMBIO);
            objlstTipoCambio = new ListModelList<TipoCambio>();
            while (rs.next()) {
                objTipoCambio = new TipoCambio();
//                objTipoCambio.setTcam_key(rs.getInt(1));
                objTipoCambio.setTcam_key(rs.getInt("tcam_key"));
                objTipoCambio.setTcam_id(rs.getString("tcam_id"));
                objTipoCambio.setDia_fec(rs.getDate("dia_fec"));
                objTipoCambio.setTab_cod(rs.getInt("tab_cod"));
                objTipoCambio.setTab_id(rs.getInt("tab_id"));
                objTipoCambio.setMoneda(rs.getString("tab_subdes"));
                objTipoCambio.setTcam_est(rs.getInt("tcam_est"));
                objTipoCambio.setTcam_com(rs.getDouble("tcam_com"));
                objTipoCambio.setTcam_conv(rs.getDouble("tcam_conv"));
                objTipoCambio.setTcam_conc(rs.getDouble("tcam_conc"));
                objTipoCambio.setTcam_usuadd(rs.getString("tcam_usuadd"));
                objTipoCambio.setTcam_fecadd(rs.getTimestamp("tcam_fecadd"));
                objTipoCambio.setTcam_usumod(rs.getString("tcam_usumod"));
                objTipoCambio.setTcam_fecmod(rs.getTimestamp("tcam_fecmod"));
                objlstTipoCambio.add(objTipoCambio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoCambio.getSize() + " registro(s)");
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
        return objlstTipoCambio;
    }

    public ListModelList<TipoCambio> busquedaTipoCambio(int i_estado, String periodo) throws SQLException {
        String SQL_TIPOCAMBIO;
        if (i_estado == 3) { // todos activos e inactivos
            SQL_TIPOCAMBIO = "select t.tcam_key,t.tcam_id,t.dia_fec,t.tab_cod,t.tab_id,t1.tab_subdes,t.tcam_est,t.tcam_com,t.tcam_conv,"
                    + "t.tcam_conc,t.tcam_usuadd,t.tcam_fecadd,t.tcam_usumod,t.tcam_fecmod from ttip_cam t,ttabgen t1 where t.tab_id=t1.tab_id "
                    + "and t.tab_cod=t1.tab_cod and t.tab_cod=24 and to_char(t.dia_fec,'yyyymm') like '%" + periodo + "%' and t.tcam_est <> 0 order by t.dia_fec,t.tcam_key";
        } else {
            SQL_TIPOCAMBIO = "select t.tcam_key,t.tcam_id,t.dia_fec,t.tab_cod,t.tab_id,t1.tab_subdes,t.tcam_est,t.tcam_com,t.tcam_conv,"
                    + "t.tcam_conc,t.tcam_usuadd,t.tcam_fecadd,t.tcam_usumod,t.tcam_fecmod from ttip_cam t,ttabgen t1 where t.tab_id=t1.tab_id "
                    + "and t.tab_cod=t1.tab_cod and t.tab_cod=24 and to_char(t.dia_fec,'yyyymm') like '%" + periodo + "%' and t.tcam_est = '" + i_estado + "' order by t.dia_fec,t.tcam_key";;
        }
        P_WHERE = SQL_TIPOCAMBIO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPOCAMBIO);
            objlstTipoCambio = new ListModelList<TipoCambio>();
            while (rs.next()) {
                objTipoCambio = new TipoCambio();
                objTipoCambio.setTcam_key(rs.getInt(1));
                objTipoCambio.setTcam_id(rs.getString(2));
                objTipoCambio.setDia_fec(rs.getDate(3));
                objTipoCambio.setTab_cod(rs.getInt(4));
                objTipoCambio.setTab_id(rs.getInt(5));
                objTipoCambio.setMoneda(rs.getString(6));
                objTipoCambio.setTcam_est(rs.getInt(7));
                objTipoCambio.setTcam_com(rs.getDouble(8));
                objTipoCambio.setTcam_conv(rs.getDouble(9));
                objTipoCambio.setTcam_conc(rs.getDouble(10));
                objTipoCambio.setTcam_usuadd(rs.getString(11));
                objTipoCambio.setTcam_fecadd(rs.getTimestamp(12));
                objTipoCambio.setTcam_usumod(rs.getString(13));
                objTipoCambio.setTcam_fecmod(rs.getTimestamp(14));
                objlstTipoCambio.add(objTipoCambio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoCambio.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }

        return objlstTipoCambio;
    }

    public double obtieneTipoCambioComercial(String s_fecha, int n_codmon) throws SQLException {
        String SQL_TIPOCAMBIO = "  select nvl(t.tcam_com,0) from ttip_cam t where t.tab_cod = 24  and t.tcam_est <> '0' and t.dia_fec = to_date('" + s_fecha + "','dd/mm/yyyy') and t.tab_id = " + n_codmon + "  ";
        double tipCambioComercial = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPOCAMBIO);

            while (rs.next()) {
                tipCambioComercial = rs.getDouble(1);

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoCambio.getSize() + " registro(s)");
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
        return tipCambioComercial;
    }

}
