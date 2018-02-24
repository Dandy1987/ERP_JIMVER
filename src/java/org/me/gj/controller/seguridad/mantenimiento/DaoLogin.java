package org.me.gj.controller.seguridad.mantenimiento;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosLogueo;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoLogin {

    //Instancias de Objetos
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    String computerName = "";
    String s_pregunta = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoLogin.class);

    //Eventos Primarios - Transaccionales
    public UsuariosLogueo Login(String s_user, String s_password) throws SQLException {
        UsuariosLogueo usulog = new UsuariosLogueo();
        cst = null;
        int i_codigo = 0;
        String SQL_CODIGO = "select t.usu_id from tusuarios t where t.usu_nick='" + s_user + "' and t.usu_pass='" + s_password + "'";
        String SQL_LOGUEO = "{call pack_tlogin.p_login(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CODIGO);
            cst = con.prepareCall(SQL_LOGUEO);
            cst.clearParameters();
            cst.setString(1, s_user);
            cst.setString(2, s_password);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);//mensaje
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);//nombre
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);//valido
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);//ingreso
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);//existe
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);//rol
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);//estado
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);//situacion
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);//intentos
            cst.execute();
            while (rs.next()) {
                i_codigo = rs.getInt(1);
            }
            usulog = new UsuariosLogueo(i_codigo, cst.getString(3), cst.getString(4), cst.getInt(5), cst.getInt(6), cst.getInt(7), cst.getInt(8), cst.getInt(9), cst.getInt(10), cst.getInt(11));
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
        return usulog;
    }

    //Eventos Secundarios - Listas y validaciones
    public String PreguntaUser(String s_user) throws SQLException, UnknownHostException {
        computerName = InetAddress.getLocalHost().getHostName();
        String SQL_PREGUNTA = "{call pack_tlogin.p_pregunta(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PREGUNTA);
            cst.clearParameters();
            cst.setString(1, s_user);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);//mensaje
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);//pregunta
            cst.execute();
            s_pregunta = cst.getString(3);
            LOGGER.info("[" + computerName + "] | " + s_user + " | ha ingresado el usuario " + s_user + " para la busqueda de su pregunta que es: " + s_pregunta);
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + computerName + "] | " + s_user + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + computerName + "] | " + s_user + " | no pudo realizar la busqueda del registro(s) porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_pregunta;
    }

    public int RespuestaUser(String s_user, String s_rpta) throws SQLException, UnknownHostException {
        computerName = InetAddress.getLocalHost().getHostName();
        int i_resultado = 0;
        String SQL_RESPUESTA = "{call pack_tlogin.p_respuesta(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_RESPUESTA);
            cst.clearParameters();
            cst.setString(1, s_user);
            cst.setString(2, s_rpta);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);//mensaje
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);//resultado
            cst.execute();
            i_resultado = cst.getInt(4);
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + computerName + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + computerName + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return i_resultado;
    }
}
