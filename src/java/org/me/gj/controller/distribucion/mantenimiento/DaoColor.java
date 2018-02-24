package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Color;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoColor {

    //Instancia de Objetos
    ListModelList<Color> objlstColor;
    Color objColor;
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoColor.class);

    //Eventos Primarios - Transaccionales
    public String insertarColor(Color objColor) throws SQLException {
        String s_tabsubdes = objColor.getTab_subdes();
        String s_tabsubdir = objColor.getTab_subdir();
        String s_tabnomrep = objColor.getTab_nomrep();
        int i_tabord = objColor.getTab_ord();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_COLOR = "{call pack_ttabgen.p_035_insertarColorTrans(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_COLOR);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabsubdir);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setString(5, objColor.getTab_usuadd());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tabsubdes);
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

    public String actualizarColor(Color objColor) throws SQLException {
        int s_tabid = objColor.getTab_id();
        String s_tabsubdes = objColor.getTab_subdes();
        String s_tabsubdir = objColor.getTab_subdir();
        String s_tabnomrep = objColor.getTab_nomrep();
        int i_tabord = objColor.getTab_ord();
        int i_tabest = objColor.getTab_est();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_COLOR = "{call pack_ttabgen.p_035_actualizarColorTrans(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_COLOR);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabsubdir);
            cst.setString(4, s_tabnomrep);
            cst.setInt(5, i_tabord);
            cst.setInt(6, i_tabest);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + s_tabid);
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

    public String eliminarColor(Color objColor) throws SQLException {
        int s_tabid = objColor.getTab_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_COLOR = "{call pack_ttabgen.p_035_eliminarColorTrans(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_COLOR);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + s_tabid);
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
    public ListModelList<Color> listaColor(int i_caso) throws SQLException {
        String SQL_COLOR;
        if (i_caso == 0) {
            SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=35 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=35 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_COLOR;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_COLOR);
            objlstColor = new ListModelList<Color>();
            while (rs.next()) {
                objColor = new Color();
                objColor.setTab_id(rs.getInt("tab_id"));
                objColor.setTab_subdes(rs.getString("tab_subdes"));
                objColor.setTab_subdir(rs.getString("tab_subdir"));
                objColor.setTab_est(rs.getInt("tab_est"));
                objColor.setTab_ord(rs.getInt("tab_ord"));
                objColor.setTab_nomrep(rs.getString("tab_nomrep"));
                objColor.setTab_usuadd(rs.getString("tab_usuadd"));
                objColor.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objColor.setTab_usumod(rs.getString("tab_usumod"));
                objColor.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstColor.add(objColor);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstColor.getSize() + " registro(s)");
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
        return objlstColor;
    }

    public ListModelList<Color> busquedaColor(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_COLOR;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0 and p.tab_est=" + i_estado + "  order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else if (seleccion == 2) {
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_COLOR = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=35 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdir like '" + consulta + "' order by p.tab_id";
            }
        }
        P_WHERE = SQL_COLOR;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_COLOR);
            objlstColor = new ListModelList<Color>();
            while (rs.next()) {
                objColor = new Color();
                objColor.setTab_id(rs.getInt("tab_id"));
                objColor.setTab_subdes(rs.getString("tab_subdes"));
                objColor.setTab_subdir(rs.getString("tab_subdir"));
                objColor.setTab_est(rs.getInt("tab_est"));
                objColor.setTab_ord(rs.getInt("tab_ord"));
                objColor.setTab_nomrep(rs.getString("tab_nomrep"));
                objColor.setTab_usuadd(rs.getString("tab_usuadd"));
                objColor.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objColor.setTab_usumod(rs.getString("tab_usumod"));
                objColor.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstColor.add(objColor);
            }
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
        return objlstColor;
    }

}
