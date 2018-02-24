package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.FormaPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoFormaPago {

    //Instancia de Objetos
    ListModelList<FormaPago> objlstFormaPago;
    FormaPago objFormaPago;
    //Variables Publicas
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
    private static final Logger LOGGER = Logger.getLogger(DaoFormaPago.class);

    //Eventos Primarios - Transaccionales
    public String insertarFormaPago(FormaPago objFormaPago) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_FORMAPAGO = "{call pack_tforpag.p_insertarFormaPago(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_FORMAPAGO);
            cst.clearParameters();
            cst.setString(1, objFormaPago.getForpag_des());
            cst.setInt(2, objFormaPago.getForpag_caj());
            cst.setInt(3, objFormaPago.getForpag_com());
            cst.setString(4, objFormaPago.getForpag_lov());
            cst.setInt(5, objFormaPago.getForpag_ord());
            cst.setString(6, objFormaPago.getForpag_nomrep());
            cst.setString(7, objFormaPago.getForpag_usuadd());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Forma de pago con la descripcion" + objFormaPago.getForpag_des());
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

    public String actualizarFormaPago(FormaPago objFormaPago) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_FORMAPAGO = "{call pack_tforpag.p_modificarFormaPago(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_FORMAPAGO);
            cst.clearParameters();
            cst.setInt(1, objFormaPago.getForpag_key());
            cst.setString(2, objFormaPago.getForpag_id());
            cst.setString(3, objFormaPago.getForpag_des());
            cst.setInt(4, objFormaPago.getForpag_caj());
            cst.setInt(5, objFormaPago.getForpag_com());
            cst.setString(6, objFormaPago.getForpag_lov());
            cst.setInt(7, objFormaPago.getForpag_est());
            cst.setInt(8, objFormaPago.getForpag_ord());
            cst.setString(9, objFormaPago.getForpag_nomrep());
            cst.setString(10, objFormaPago.getForpag_usumod());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico el registro de Forma de pago con el codigo" + objFormaPago.getForpag_id());
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

    public String eliminarFormaPago(FormaPago objFormaPago) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_FORMAPAGO = "{call pack_tforpag.p_eliminarFormaPago(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_FORMAPAGO);
            cst.clearParameters();
            cst.setInt(1, objFormaPago.getForpag_key());
            cst.setString(2, objFormaPago.getForpag_id());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico el registro de Forma de pago con el codigo" + objFormaPago.getForpag_id());
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<FormaPago> listaFormaPago(int i_caso) throws SQLException {
        String SQL_FORMAPAGO = "";
        if (i_caso == 0) {
            SQL_FORMAPAGO = "select * from tforpag t where t.forpag_est<>" + i_caso + " order by t.forpag_key";
        } else {
            SQL_FORMAPAGO = "select * from tforpag t where t.forpag_est=" + i_caso + " order by t.forpag_key";
        }
        P_WHERE = SQL_FORMAPAGO;
        objlstFormaPago = new ListModelList<FormaPago>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FORMAPAGO);
            while (rs.next()) {
                objFormaPago = new FormaPago();
                objFormaPago.setForpag_key(rs.getInt("forpag_key"));
                objFormaPago.setForpag_id(rs.getString("forpag_id"));
                objFormaPago.setForpag_des(rs.getString("forpag_des"));
                objFormaPago.setForpag_caj(rs.getInt("forpag_caj"));
                objFormaPago.setForpag_com(rs.getInt("forpag_com"));
                objFormaPago.setForpag_lov(rs.getString("forpag_lov"));
                objFormaPago.setForpag_est(rs.getInt("forpag_est"));
                objFormaPago.setForpag_ord(rs.getInt("forpag_ord"));
                objFormaPago.setForpag_nomrep(rs.getString("forpag_nomrep"));
                objFormaPago.setForpag_usuadd(rs.getString("forpag_usuadd"));
                objFormaPago.setForpag_fecadd(rs.getTimestamp("forpag_fecadd"));
                objFormaPago.setForpag_usumod(rs.getString("forpag_usumod"));
                objFormaPago.setForpag_fecmod(rs.getTimestamp("forpag_fecmod"));
                objlstFormaPago.add(objFormaPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstFormaPago.getSize() + " registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstFormaPago;
    }

    public ListModelList<FormaPago> busquedaFormasPago(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA = "";
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est <> 0  order by t.forpag_key";
            } else if (i_seleccion == 1) {
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est <> 0 and t.forpag_key like '" + s_consulta.replace("0", "").trim() + "' order by t.forpag_key";
            } else {
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est <> 0 and t.forpag_des like '" + s_consulta + "' order by t.forpag_key";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est=" + i_estado + " order by t.forpag_key";
            } else if (i_seleccion == 1) {
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est=" + i_estado + " and t.forpag_key like '" + s_consulta.replace("0", "").trim() + "' order by t.forpag_key";
            } else {
                SQL_BUSQUEDA = "select * from tforpag t where t.forpag_est=" + i_estado + " and t.forpag_des like '" + s_consulta + "' order by t.forpag_key";
            }
        }

        P_WHERE = SQL_BUSQUEDA;
        objlstFormaPago = new ListModelList<FormaPago>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objFormaPago = new FormaPago();
                objFormaPago.setForpag_key(rs.getInt("forpag_key"));
                objFormaPago.setForpag_id(rs.getString("forpag_id"));
                objFormaPago.setForpag_des(rs.getString("forpag_des"));
                objFormaPago.setForpag_caj(rs.getInt("forpag_caj"));
                objFormaPago.setForpag_com(rs.getInt("forpag_com"));
                objFormaPago.setForpag_lov(rs.getString("forpag_lov"));
                objFormaPago.setForpag_est(rs.getInt("forpag_est"));
                objFormaPago.setForpag_ord(rs.getInt("forpag_ord"));
                objFormaPago.setForpag_nomrep(rs.getString("forpag_nomrep"));
                objFormaPago.setForpag_usuadd(rs.getString("forpag_usuadd"));
                objFormaPago.setForpag_fecadd(rs.getTimestamp("forpag_fecadd"));
                objFormaPago.setForpag_usumod(rs.getString("forpag_usumod"));
                objFormaPago.setForpag_fecmod(rs.getTimestamp("forpag_fecmod"));
                objlstFormaPago.add(objFormaPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstFormaPago.getSize() + " registro(s)");
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
        return objlstFormaPago;
    }

}
