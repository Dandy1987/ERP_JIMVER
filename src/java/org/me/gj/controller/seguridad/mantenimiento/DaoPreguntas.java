package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Preguntas;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoPreguntas {

    //Instancias de Objetos
    ListModelList<Preguntas> objlstPreguntas;
    Preguntas objPreguntas;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";

    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoPreguntas.class);

    //Eventos Primarios - Transaccionales
    public String insertarPregunta(Preguntas objPregunta) throws SQLException {
        String s_tabsubdes = objPregunta.getPreg_des();
        String s_tabnomrep = objPregunta.getPreg_nomrep();
        int i_tabord = objPregunta.getPreg_ord();
        String SQL_INSERTAR_REL_GUIAS = "{call pack_ttabgen.p_013_insertarPregunta(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_REL_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
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

    public String actualizarPregunta(Preguntas objPregunta) throws SQLException {
        int i_tabid = objPregunta.getPreg_id();
        String s_tabsubdes = objPregunta.getPreg_des();
        String s_tabnomrep = objPregunta.getPreg_nomrep();
        int i_tabord = objPregunta.getPreg_ord();
        int i_tabest = objPregunta.getPreg_est();
        String SQL_ACTUALIZAR_REL_GUIAS = "{call pack_ttabgen.p_013_actualizarPregunta(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tabid);
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

    public String eliminarPregunta(Preguntas objPregunta) throws SQLException {
        int i_tabid = objPregunta.getPreg_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_ttabgen.p_013_eliminarPregunta(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tabid);
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
    public ListModelList<Preguntas> lstPreguntas(int i_caso) throws SQLException {
        String SQL_PREGUNTAS;
        if (i_caso == 0) {
            SQL_PREGUNTAS = "select t.tab_id rol_id,t.tab_subdes rol_des, t.tab_est, t.tab_ord, t.tab_nomrep "
                    + "from ttabgen t where t.tab_cod=13 and t.tab_id<>0 and t.tab_est<>" + i_caso + " order by t.tab_ord,t.tab_id";
        } else {
            SQL_PREGUNTAS = "select t.tab_id rol_id,t.tab_subdes rol_des, t.tab_est, t.tab_ord, t.tab_nomrep "
                    + "from ttabgen t where t.tab_cod=13 and t.tab_id<>0 and t.tab_est=" + i_caso + " order by t.tab_ord,t.tab_id";
        }
        objlstPreguntas = new ListModelList<Preguntas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PREGUNTAS);
            while (rs.next()) {
                objPreguntas = new Preguntas();
                objPreguntas.setPreg_id(rs.getInt(1));
                objPreguntas.setPreg_des(rs.getString(2));
                objPreguntas.setPreg_est(rs.getInt(3));
                objPreguntas.setPreg_ord(rs.getInt(4));
                objPreguntas.setPreg_nomrep(rs.getString(5));
                objlstPreguntas.add(objPreguntas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPreguntas.getSize() + " registro(s)");
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
        return objlstPreguntas;
    }

    public ListModelList<Preguntas> busquedaPreguntas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_PREGUNTA;
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est<>0 order by p.tab_ord,p.tab_id";
            } else if (i_seleccion == 1) {
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est<>0 and p.tab_id like '" + s_consulta + "' order by p.tab_ord,p.tab_id";
            } else {
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdes like '" + s_consulta + "' order by p.tab_ord,p.tab_id";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_ord,p.tab_id";
            } else if (i_seleccion == 1) {
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + s_consulta + "' order by p.tab_ord,p.tab_id";
            } else {
                SQL_PREGUNTA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                        + "where p.tab_cod=13 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + s_consulta + "' order by p.tab_ord,p.tab_id";
            }
        }
        try {
            objlstPreguntas = new ListModelList<Preguntas>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PREGUNTA);
            while (rs.next()) {
                objPreguntas = new Preguntas();
                objPreguntas.setPreg_id(rs.getInt("tab_id"));
                objPreguntas.setPreg_des(rs.getString("tab_subdes"));
                objPreguntas.setPreg_est(rs.getInt("tab_est"));
                objPreguntas.setPreg_nomrep(rs.getString("tab_nomrep"));
                objPreguntas.setPreg_ord(rs.getInt("tab_ord"));
                objlstPreguntas.add(objPreguntas);
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
        return objlstPreguntas;
    }
}
