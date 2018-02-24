package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoDirecciones {

    //Instancias de Objetos
    ListModelList<CliDireccion> objlstCliDireccion = new ListModelList<CliDireccion>();
    CliDireccion objCliDireccion;
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
    private static final Logger LOGGER = Logger.getLogger(DaoDirecciones.class);

    //Eventos Primarios - Transaccionales
    //Eventos Secundarios - Listas y validaciones
    public CliDireccion getNomDireccion(String cli_id, long dir_id, int emp_id, int suc_id) throws SQLException {
        String SQL_BUSQUEDA = "select t.clidir_id,t.clidir_direc,t.zon_id,t.zon_des,t.zon_dvis,t.zon_dvis_des,t.trans_id,t.trans_alias,"
                + " t.ven_id,t.ven_apenom,t.cli_factura,t.hor_id,t.hor_des, t.giro_id, t.giro_des"
                + " from v_listadireccioncliente t where t.cli_key=" + Long.parseLong(cli_id) + " and t.cli_id='" + cli_id + "' and "
                + " t.clizon_est=1 and t.clidir_id=" + dir_id + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + " order by t.cli_key,t.emp_id,t.suc_id,t.clidir_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objCliDireccion = null;
            while (rs.next()) {
                objCliDireccion = new CliDireccion();
                objCliDireccion.setClidir_id(rs.getLong("clidir_id"));
                //objCliDireccion.setClidir_direc(rs.getString("clidir_direc")== null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliDireccion.setClidir_direc(rs.getString("clidir_direc"));
                objCliDireccion.setZon_id(rs.getString("zon_id"));
                objCliDireccion.setZon_des(rs.getString("zon_des"));
                objCliDireccion.setZon_diavis(rs.getInt("zon_dvis"));
                objCliDireccion.setZon_diavis_des(rs.getString("zon_dvis_des"));
                objCliDireccion.setTrans_id(rs.getString("trans_id"));
                objCliDireccion.setTrans_des(rs.getString("trans_alias"));
                objCliDireccion.setVen_id(rs.getString("ven_id"));
                objCliDireccion.setVen_apenom(rs.getString("ven_apenom"));
                objCliDireccion.setCli_factura(rs.getInt("cli_factura"));
                objCliDireccion.setHor_id(rs.getString("hor_id"));
                objCliDireccion.setHor_des(rs.getString("hor_des"));
                objCliDireccion.setGiro_id(rs.getString("giro_id"));
                objCliDireccion.setGiro_des(rs.getString("giro_des"));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCliDireccion;
    }

    public ListModelList<CliDireccion> ListaDireccion(String cli_id, int emp_id, int suc_id) throws SQLException {
        String SQL_BUSQUEDA = "select t.clidir_id,t.clidir_direc from tclidir t,tclizon p "
                + " where t.cli_id=p.cli_id and t.cli_key=p.cli_key and "
                + " t.clidir_id=p.clidir_id and "
                + " t.cli_id='" + cli_id + "' and t.cli_key=" + Long.parseLong(cli_id) + " and "
                + " p.emp_id=" + emp_id + " and p.suc_id=" + suc_id + " and"
                + " p.clizon_est=1 order by t.clidir_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objCliDireccion = null;
            while (rs.next()) {
                objCliDireccion = new CliDireccion();
                objCliDireccion.setClidir_id(rs.getLong(1));
                objCliDireccion.setClidir_direc(rs.getString(2));
                objlstCliDireccion.add(objCliDireccion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliDireccion.getSize() + " registro(s)");
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
        return objlstCliDireccion;
    }

    public ListModelList<CliDireccion> busquedaDirecciones(String cli_id, String consulta) throws SQLException {
        String SQL_POSTALES;
        SQL_POSTALES = "select t.clidir_id,t.clidir_direc from tclidir t where t.clidir_est=1 and t.cli_id='" + cli_id + "' and t.clidir_direc like '" + consulta + "' order by t.clidir_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_POSTALES);
            objlstCliDireccion = new ListModelList<CliDireccion>();
            while (rs.next()) {
                objCliDireccion = new CliDireccion();
                objCliDireccion.setClidir_id(rs.getLong(1));
                objCliDireccion.setClidir_direc(rs.getString(2));
                objlstCliDireccion.add(objCliDireccion);
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

        return objlstCliDireccion;
    }
}
