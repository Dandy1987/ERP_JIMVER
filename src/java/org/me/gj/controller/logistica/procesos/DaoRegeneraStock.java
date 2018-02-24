package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class DaoRegeneraStock {
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
    private static final Logger LOGGER = Logger.getLogger(DaoRegeneraStock.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida regenerarStock(int emp_id, int suc_id, String per_id, String stk_usuadd) throws SQLException {
        String SQL_VALIDAPROCESO;
        SQL_VALIDAPROCESO = "{call pack_tempresas.p_validaproceso(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDAPROCESO);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            if (s_msg.equals("N")) {
                i_flagErrorBD = 0;
                s_msg = "";
                cst = null;
                String SQL_REGENERAR_STOCK = "{call pack_tstocks.p_regenera_stock(?,?,?,?,?,?)}";
                try {
                    con = new ConectaBD().conectar();
                    cst = con.prepareCall(SQL_REGENERAR_STOCK);
                    cst.clearParameters();
                    cst.setInt("n_emp_id", emp_id);
                    cst.setInt("n_suc_id", suc_id);
                    cst.setString("c_per_id", per_id);
                    cst.setString("c_stk_usuadd", stk_usuadd);
                    cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
                    cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
                    cst.execute();
                    s_msg = cst.getString("c_msg");
                    i_flagErrorBD = cst.getInt("n_Flag");
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Regenero el Stock del Periodo " + per_id);
                } catch (SQLException e) {
                    i_flagErrorBD = 1;
                    s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
                    LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo Regenerar el Stock debido al Error " + e.toString());
                } catch (NullPointerException e) {
                    i_flagErrorBD = 1;
                    s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
                    LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo Regenerar el Stock debido al Error " + e.toString());
                } finally {
                    if (con != null) {
                        cst.close();
                        con.close();
                    }
                }
                return new ParametrosSalida(i_flagErrorBD, s_msg);
            }
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo Regenerar los Costos al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo Regenerar los Costos debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
}
