package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.Ubigeo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoUbigeo {

    //Instancia de Objetos
    ListModelList<Ubigeo> objlstUbigeo;
    Ubigeo objUbigeo;
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
    private static Logger LOGGER = Logger.getLogger(DaoUbigeo.class);

    //Eventos Primarios - Transaccionales
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Ubigeo> listaUbigeo(int i_caso) throws SQLException {
        String SQL_UBIGEO;
        if (i_caso == 0) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est <> " + i_caso;
        } else {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est=" + i_caso;
        }
        P_WHERE = SQL_UBIGEO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UBIGEO);
            objlstUbigeo = new ListModelList<Ubigeo>();
            while (rs.next()) {
                objUbigeo = new Ubigeo();
                objUbigeo.setUbi_cod(rs.getString("ubi_cod"));
                objUbigeo.setUbi_id(rs.getString("ubi_id"));
                objUbigeo.setUbi_nomdis(rs.getString("ubi_nomdis"));
                objUbigeo.setUbi_nompro(rs.getString("ubi_nompro"));
                objUbigeo.setUbi_nomdep(rs.getString("ubi_nomdep"));
                objUbigeo.setUbi_est(rs.getInt("ubi_est"));
                objUbigeo.setUbi_usuadd(rs.getString("ubi_usuadd"));
                objUbigeo.setUbi_fecadd(rs.getTimestamp("ubi_fecadd"));
                objUbigeo.setUbi_usumod(rs.getString("ubi_usumod"));
                objUbigeo.setUbi_fecmod(rs.getTimestamp("ubi_fecmod"));
                objlstUbigeo.add(objUbigeo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUbigeo.getSize() + " registro(s)");
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
        return objlstUbigeo;
    }

    public ListModelList<Ubigeo> busquedaUbigeo(int seleccion, String consulta) throws SQLException {
        String SQL_UBIGEO = "";
        if (seleccion == 0) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est = '1' ";
        } else if (seleccion == 1) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est = '1' and t.ubi_id like '" + consulta + "'";
        } else if (seleccion == 2) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est = '1' and t.ubi_nomdis like '" + consulta + "'";
        } else if (seleccion == 3) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est = '1' and (t.ubi_nomdep || t.ubi_nompro || t.ubi_nomdis) like '" + consulta + "'";
        } else if (seleccion == 4) {
            SQL_UBIGEO = "select * from codijisa.v_listaubigeo t where t.ubi_est = '1' and t.ubi_nomdep like '" + consulta + "'";
        }
        P_WHERE = SQL_UBIGEO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UBIGEO);
            objlstUbigeo = new ListModelList<Ubigeo>();
            while (rs.next()) {
                objUbigeo = new Ubigeo();
                objUbigeo.setUbi_cod(rs.getString("ubi_cod"));
                objUbigeo.setUbi_id(rs.getString("ubi_id"));
                objUbigeo.setUbi_nomdis(rs.getString("ubi_nomdis"));
                objUbigeo.setUbi_nompro(rs.getString("ubi_nompro"));
                objUbigeo.setUbi_nomdep(rs.getString("ubi_nomdep"));
                objUbigeo.setUbi_est(rs.getInt("ubi_est"));
                objUbigeo.setUbi_usuadd(rs.getString("ubi_usuadd"));
                objUbigeo.setUbi_fecadd(rs.getTimestamp("ubi_fecadd"));
                objUbigeo.setUbi_usumod(rs.getString("ubi_usumod"));
                objUbigeo.setUbi_fecmod(rs.getTimestamp("ubi_fecmod"));
                objlstUbigeo.add(objUbigeo);
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

        return objlstUbigeo;
    }

    public Ubigeo buscarUbigeo(String ubi_cod) throws SQLException {
        String SQL_UBIGEO = "select t.ubi_iddep||t.ubi_idpro||t.ubi_iddis ubi_cod, t.ubi_nomdep,t.ubi_nompro,t.ubi_nomdis from tubigeo t where t.ubi_est=1 and (t.ubi_iddep||t.ubi_idpro||t.ubi_iddis)=" + ubi_cod;
        objUbigeo = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UBIGEO);
            while (rs.next()) {
                objUbigeo = new Ubigeo();
                objUbigeo.setUbi_cod(rs.getString(1));
                objUbigeo.setUbi_nomdep(rs.getString(2));
                objUbigeo.setUbi_nompro(rs.getString(3));
                objUbigeo.setUbi_nomdis(rs.getString(4));
                objlstUbigeo.add(objUbigeo);
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
        return objUbigeo;
    }
}
