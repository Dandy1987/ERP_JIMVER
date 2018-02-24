package org.me.gj.controller.planillas.utilitarios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author chuallpa
 */
public class DaoCierres {

    //Variables de conexion
    String s_msg = "";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //Indicadores
    int i_flagErrorBD = 0;
    public static String P_WHERE = "";

    //Variables de Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCierres.class);

    public int cierre(String periodo, int empr) throws SQLException {
        String empresa = String.valueOf(empr);
        int consulta = 0;
        try {
            con = new ConectaBD().conectar();
            String query = "{?=call pack_tperpag.f_percie(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setString(2, empresa);
            cst.setString(3, periodo);
            cst.execute();
            consulta = cst.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Intentó cierrar la planilla | " + objUsuCredential.getEmpresa() + " | En el periodo: " + periodo);
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return consulta;
    }

    public int revierteCierre(int emp, String periodo) {
        String empresa = String.valueOf(emp);
        int consulta = 0;
        String query = "";
        query = "{?=call pack_tperpag.f_perrev(?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, empresa);
            cst.setString(3, periodo);
            cst.execute();
            consulta = cst.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Intentó revertir el cierre de planilla | " + objUsuCredential.getEmpresa() + " | En el periodo: " + periodo + e.toString());
            Messagebox.show("Error de Carga de Datos debido al Error: " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            consulta = 0;
        } finally {
            if (con != null) {
                try {
                    con.close();
                    cst.close();
                } catch (SQLException ex) {
                    Messagebox.show("Error de conexión debido al Error: " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
        return consulta;
    }

    /**
     * Ecuentra numero de trabajadores activos
     *
     * @return # de trabajadores
     * @throws java.sql.SQLException
     */
    public int trabActivos(String s_periodo) throws SQLException {
        int trabajador = 0;
        String query;
        try {
            con = new ConectaBD().conectar();
            query = "{?= call pack_tcalculoplla.f_t_tpldatlab(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
			cst.setString(3, s_periodo);
            cst.execute();
            trabajador = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return trabajador;
    }

    /**
     * Ecuentra numero de trabajadores calculados
     *
     * @param periodo
     * @return # trabajadores para comparar
     * @throws java.sql.SQLException
     */
    public int trabPlames(String periodo) throws SQLException {
        int trabajador = 0;
        String query;
        try {
            con = new ConectaBD().conectar();
            query = "{?= call pack_tcalculoplla.f_t_ptlplames(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3, periodo);
            cst.execute();
            trabajador = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return trabajador;
    }

    /**
     * Metodo para inactivar automaticamente
     * @param periodo
     * @return 
     * @throws java.sql.SQLException
     */
    public ParametrosSalida inactivaAutomatico(String periodo) throws SQLException {
        String query = "{call pack_tcalculoplla.p_robot_Inactiva(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, periodo);
            cst.setString(3, objUsuCredential.getCuenta());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(4);
            s_msg = cst.getString(5);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
    
       /**
     * Metodo para inactivar automaticamente
     * @param periodo
     * @return 
     * @throws java.sql.SQLException
     */
    public ParametrosSalida activaAutomatico(String periodo) throws SQLException {
        String query = "{call pack_tcalculoplla.p_robot_Revierte(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, periodo);
            cst.setString(3, objUsuCredential.getCuenta());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(4);
            s_msg = cst.getString(5);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

}
