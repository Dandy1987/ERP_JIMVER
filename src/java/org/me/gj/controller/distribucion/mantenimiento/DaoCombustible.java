package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Combustible;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCombustible {

    //Instancia de Objetos
    ListModelList<Combustible> objlstCombustible;
    Combustible objCombustible;
    //Variables Publicas
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
    private static final Logger LOGGER = Logger.getLogger(DaoCombustible.class);

    //Eventos Primarios - Transaccionales
    public String insertarCombustible(Combustible objCombustible) throws SQLException {
        String s_tabsubdes = objCombustible.getTab_subdes();
        String s_tabsubdir = objCombustible.getTab_subdir();
        String s_tabnomrep = objCombustible.getTab_nomrep();
        int i_tabord = objCombustible.getTab_ord();
        String SQL_INSERTAR_COMBUSTIBLE = "{call pack_ttabgen.p_034_insertarCombusTrans(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_COMBUSTIBLE);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabsubdir);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setString(5, objCombustible.getTab_usuadd());
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

    public String actualizarCombustible(Combustible objCombustible) throws SQLException {
        int s_tabid = objCombustible.getTab_id();
        String s_tabsubdes = objCombustible.getTab_subdes();
        String s_tabsubdir = objCombustible.getTab_subdir();
        String s_tabnomrep = objCombustible.getTab_nomrep();
        int i_tabord = objCombustible.getTab_ord();
        int i_tabest = objCombustible.getTab_est();
        String SQL_ACTUALIZAR_COMBUSTIBLE = "{call pack_ttabgen.p_034_actualizarCombusTrans(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_COMBUSTIBLE);
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

    public String eliminarCombustible(Combustible objCombustible) throws SQLException {
        int s_tabid = objCombustible.getTab_id();
        String SQL_ELIMINAR_COMBUSTIBLE = "{call pack_ttabgen.p_034_eliminarCombusTrans(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_COMBUSTIBLE);
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
    public ListModelList<Combustible> listaCombustible(int i_caso) throws SQLException {
        String SQL_COMBUSTIBLE;
        if (i_caso == 0) {
            SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=34 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=34 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_COMBUSTIBLE;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_COMBUSTIBLE);
            objlstCombustible = new ListModelList<Combustible>();
            while (rs.next()) {
                objCombustible = new Combustible();
                objCombustible.setTab_id(rs.getInt("tab_id"));
                objCombustible.setTab_subdes(rs.getString("tab_subdes"));
                objCombustible.setTab_subdir(rs.getString("tab_subdir"));
                objCombustible.setTab_est(rs.getInt("tab_est"));
                objCombustible.setTab_ord(rs.getInt("tab_ord"));
                objCombustible.setTab_nomrep(rs.getString("tab_nomrep"));
                objCombustible.setTab_usuadd(rs.getString("tab_usuadd"));
                objCombustible.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objCombustible.setTab_usumod(rs.getString("tab_usumod"));
                objCombustible.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstCombustible.add(objCombustible);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCombustible.getSize() + " registro(s)");
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
        return objlstCombustible;
    }

    public ListModelList<Combustible> busquedaCombustible(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_COMBUSTIBLE;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else if (seleccion == 2) {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdir like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0 and p.tab_est=" + i_estado + "  order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta + "' order by p.tab_ord,p.tab_id";
            } else if (seleccion == 2) {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_ord,p.tab_id";
            } else {
                SQL_COMBUSTIBLE = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=34 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdir like '" + consulta + "' order by p.tab_ord,p.tab_id";
            }
        }
        P_WHERE = SQL_COMBUSTIBLE;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_COMBUSTIBLE);
            objlstCombustible = new ListModelList<Combustible>();
            while (rs.next()) {
                objCombustible = new Combustible();
                objCombustible.setTab_id(rs.getInt("tab_id"));
                objCombustible.setTab_subdes(rs.getString("tab_subdes"));
                objCombustible.setTab_subdir(rs.getString("tab_subdir"));
                objCombustible.setTab_est(rs.getInt("tab_est"));
                objCombustible.setTab_ord(rs.getInt("tab_ord"));
                objCombustible.setTab_nomrep(rs.getString("tab_nomrep"));
                objCombustible.setTab_usuadd(rs.getString("tab_usuadd"));
                objCombustible.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objCombustible.setTab_usumod(rs.getString("tab_usumod"));
                objCombustible.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstCombustible.add(objCombustible);
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
        return objlstCombustible;
    }

}
