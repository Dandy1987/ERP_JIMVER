package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Roles;
import org.me.gj.model.seguridad.mantenimiento.Usuarios;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

public class DaoUsuarios {

    //Instancias de Objetos
    ListModelList<Usuarios> objlstUsuarios;
    Usuarios objUsuario;
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
    private static Logger LOGGER = Logger.getLogger(DaoUsuarios.class);

    //Eventos Primarios - Transaccionales
    public String insertarUsuario(Usuarios objUsuario) throws SQLException {
        String usu_nom = objUsuario.getUsu_nom();
        String usu_ape = objUsuario.getUsu_ape();
        String usu_nick = objUsuario.getUsu_nick();
        String usu_pass = objUsuario.getUsu_pass();
        String usu_imag = objUsuario.getUsu_imag();
        String usu_rpta = objUsuario.getUsu_rpta();
        String usu_email = objUsuario.getUsu_email();
        String usu_dni = objUsuario.getUsu_dni();
        int usu_idRol = objUsuario.getUsu_idRol();
        int usu_idPreg = objUsuario.getUsu_idPreg();
        String usu_area = objUsuario.getUsu_area();
        String usu_usuadd = objUsuario.getUsu_usuadd();
        int i_flagErrorBD = 0;
        String s_msg = "";
        CallableStatement cst;
        String SQL_INSERT_PROC_USUARIOS = "{call pack_tusuarios.p_insertarUsuario(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_USUARIOS);
            cst.clearParameters();
            cst.setString(1, usu_nom);
            cst.setString(2, usu_ape);
            cst.setString(3, usu_nick);
            cst.setString(4, usu_pass);
            cst.setString(5, usu_imag);
            cst.setString(6, usu_rpta);
            cst.setString(7, usu_email);
            cst.setString(8, usu_dni);
            cst.setInt(9, usu_idRol);
            cst.setInt(10, usu_idPreg);
            cst.setString(11, usu_area);
            cst.setString(12, usu_usuadd);
            cst.registerOutParameter(13, java.sql.Types.NUMERIC);
            cst.registerOutParameter(14, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(14);
            i_flagErrorBD = cst.getInt(13);
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return s_msg;
    }

    public String actualizarUsuario(Usuarios objUsuario) throws SQLException {
        int usu_id = objUsuario.getUsu_id();
        String usu_nom = objUsuario.getUsu_nom();
        String usu_ape = objUsuario.getUsu_ape();
        String usu_nick = objUsuario.getUsu_nick();
        String usu_pass = objUsuario.getUsu_pass();
        String usu_imag = objUsuario.getUsu_imag();
        String usu_rpta = objUsuario.getUsu_rpta();
        String usu_email = objUsuario.getUsu_email();
        String usu_dni = objUsuario.getUsu_dni();
        int usu_est = objUsuario.getUsu_est();
        int usu_sit = objUsuario.getUsu_sit();
        int usu_idRol = objUsuario.getUsu_idRol();
        int usu_idPreg = objUsuario.getUsu_idPreg();
        String usu_area = objUsuario.getUsu_area();
        String usu_usumod = objUsuario.getUsu_usumod();
        int i_flagErrorBD = 0;
        String s_msg = "";
        CallableStatement cst;
        String SQL_INSERT_PROC_USUARIOS = "{call pack_tusuarios.p_actualizarUsuario(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_USUARIOS);
            cst.clearParameters();
            cst.setInt(1, usu_id);
            cst.setString(2, usu_nom);
            cst.setString(3, usu_ape);
            cst.setString(4, usu_nick);
            cst.setString(5, usu_pass);
            cst.setString(6, usu_imag);
            cst.setString(7, usu_rpta);
            cst.setString(8, usu_email);
            cst.setString(9, usu_dni);
            cst.setInt(10, usu_est);
            cst.setInt(11, usu_sit);
            cst.setInt(12, usu_idRol);
            cst.setInt(13, usu_idPreg);
            cst.setString(14, usu_area);
            cst.setString(15, usu_usumod);
            cst.registerOutParameter(16, java.sql.Types.NUMERIC);
            cst.registerOutParameter(17, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(17);
            i_flagErrorBD = cst.getInt(16);
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return s_msg;
    }

    public String eliminarUsuario(Usuarios objUsuario) throws SQLException {
        int usu_id = objUsuario.getUsu_id();
        int i_flagErrorBD = 0;
        String s_msg = "";
        CallableStatement cst;
        String SQL_DELETE_PROC_USUARIOS = "{call pack_tusuarios.p_eliminarUsuario(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_USUARIOS);
            cst.clearParameters();
            cst.setInt(1, usu_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Usuarios> lstUsuarios() throws SQLException {
        String SQL_USUARIOS = "select t.usu_id,t.usu_nom,t.usu_ape,t.usu_nick,t.usu_pass,t.usu_est,"
                + "t.usu_sit,t.usu_email,t.usu_dni,x.tab_subdes usu_rol,t.usu_imag,t.usu_area,"
                + "p.tab_subdes usu_preg,t.usu_rpta,t.usu_int,t.usu_usuadd,t.usu_fecadd,t.usu_usumod,"
                + "t.usu_fecmod "
                + "from tusuarios t, ttabgen p, ttabgen x "
                + "where p.tab_cod=13 and x.tab_cod=14 "
                + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0 order by t.usu_id";
        objlstUsuarios = new ListModelList<Usuarios>();
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_USUARIOS);
            while (rs.next()) {
                objUsuario = new Usuarios();
                objUsuario.setUsu_id(rs.getInt(1));
                objUsuario.setUsu_nom(rs.getString(2));
                objUsuario.setUsu_ape(rs.getString(3));
                objUsuario.setUsu_nick(rs.getString(4));
                objUsuario.setUsu_pass(rs.getString(5));
                objUsuario.setUsu_est(rs.getInt(6));
                objUsuario.setUsu_sit(rs.getInt(7));
                objUsuario.setUsu_email(rs.getString(8));
                objUsuario.setUsu_dni(rs.getString(9));
                objUsuario.setUsu_rol(rs.getString(10));
                objUsuario.setUsu_imag(rs.getString(11));
                objUsuario.setUsu_area(rs.getString(12));
                objUsuario.setUsu_preg(rs.getString(13));
                objUsuario.setUsu_rpta(rs.getString(14));
                objUsuario.setUsu_int(rs.getInt(15));
                objUsuario.setUsu_usuadd(rs.getString(16));
                objUsuario.setUsu_fecadd(rs.getDate(17));
                objUsuario.setUsu_usumod(rs.getString(18));
                objUsuario.setUsu_fecmod(rs.getDate(19));
                objlstUsuarios.add(objUsuario);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstUsuarios;
    }

    public ListModelList<Usuarios> busquedaUsuarios(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_USUARIOS;
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0 order by t.usu_id";
            } else if (i_seleccion == 1) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0"
                        + "and t.usu_id like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 2) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0"
                        + "and t.usu_nom like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 3) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0"
                        + "and t.usu_ape like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 4) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0"
                        + "and t.usu_nick like '" + s_consulta + "' order by t.usu_id";
            } else {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est<>0 and t.usu_id<>0"
                        + "and t.usu_area like '" + s_consulta + "' order by t.usu_id";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0 order by t.usu_id";
            } else if (i_seleccion == 1) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0"
                        + "and t.usu_id like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 2) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0"
                        + "and t.usu_nom like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 3) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0"
                        + "and t.usu_ape like '" + s_consulta + "' order by t.usu_id";
            } else if (i_seleccion == 4) {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0"
                        + "and t.usu_nick like '" + s_consulta + "' order by t.usu_id";
            } else {
                SQL_USUARIOS = "select * from tusuarios t, ttabgen p, ttabgen x "
                        + "where p.tab_cod=13 and x.tab_cod=14 "
                        + "and t.tab_cod=p.tab_cod and t.usu_rol=x.tab_id "
                        + "and t.tab_id=p.tab_id and t.usu_est=" + i_estado + " and t.usu_id<>0"
                        + "and t.usu_area like '" + s_consulta + "' order by t.usu_id";
            }
        }

        try {
            objlstUsuarios = new ListModelList<Usuarios>();
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_USUARIOS);
            while (rs.next()) {
                objUsuario = new Usuarios();
                objUsuario.setUsu_id(rs.getInt("usu_id"));
                objUsuario.setUsu_nom(rs.getString("usu_nom"));
                objUsuario.setUsu_ape(rs.getString("usu_ape"));
                objUsuario.setUsu_nick(rs.getString("usu_nick"));
                objUsuario.setUsu_pass(rs.getString("usu_pass"));
                objUsuario.setUsu_est(rs.getInt("usu_est"));
                objUsuario.setUsu_sit(rs.getInt("usu_sit"));
                objUsuario.setUsu_email(rs.getString("usu_email"));
                objUsuario.setUsu_dni(rs.getString("usu_dni"));
                objUsuario.setUsu_rol(rs.getString("usu_rol"));
                objUsuario.setUsu_imag(rs.getString("usu_imag"));
                objUsuario.setUsu_area(rs.getString("usu_area"));
                objUsuario.setUsu_preg(rs.getString("tab_subdes"));
                objUsuario.setUsu_rpta(rs.getString("usu_rpta"));
                objUsuario.setUsu_int(rs.getInt("usu_int"));
                objUsuario.setUsu_usuadd(rs.getString("usu_usuadd"));
                objUsuario.setUsu_fecadd(rs.getDate("usu_fecadd"));
                objUsuario.setUsu_usumod(rs.getString("usu_usumod"));
                objUsuario.setUsu_fecmod(rs.getDate("usu_fecmod"));
                objlstUsuarios.add(objUsuario);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstUsuarios;
    }

    public ListModelList<Roles> lstRoles() throws SQLException {
        ListModelList<Roles> objlstRoles = new ListModelList<Roles>();
        String SQL_ROLES = "select t.tab_id rol_id,t.tab_subdes rol_des from ttabgen t where t.tab_cod=14 and t.tab_id<>0";
        Roles rol;
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_ROLES);
            while (rs.next()) {
                rol = new Roles();
                rol.setRol_id(rs.getInt(1));
                rol.setRol_des(rs.getString(2));
                objlstRoles.add(rol);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstRoles;
    }

    public int validaNick(String nick) throws SQLException {
        int i_flag = 0;
        String s_respuesta;
        CallableStatement cst;
        String SQL_RESPUESTA = "{call pack_tusuarios.p_validaNick(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_RESPUESTA);
            cst.clearParameters();
            cst.setString(1, nick);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            i_flag = cst.getInt(2);
            s_respuesta = cst.getString(3);
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return i_flag;
    }

}
