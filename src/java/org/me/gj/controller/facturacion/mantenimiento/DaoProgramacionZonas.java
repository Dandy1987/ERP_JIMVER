package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ProgramacionZona;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoProgramacionZonas {

    ListModelList<ProgramacionZona> objlstProgramacionZona;
    ProgramacionZona objProgramacionZona;
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
    private static final Logger LOGGER = Logger.getLogger(DaoProgramacionZonas.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarProgramacionZona(ProgramacionZona objProgramacionZona) throws SQLException {
        ParametrosSalida objParam = new ParametrosSalida();
        cst = null;
        String SQL_INSERTAR_PROGRAMACIONZONA = "{call pack_tprogramacion_zonas.p_insertarProgramacionZona(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PROGRAMACIONZONA);
            cst.clearParameters();
            cst.setString(1, objProgramacionZona.getProg_zonid());
            cst.setInt(2, objProgramacionZona.getSuc_id());
            cst.setInt(3, objProgramacionZona.getEmp_id());
            cst.setInt(4, Integer.parseInt(objProgramacionZona.getProg_transid()));
            cst.setInt(5, Integer.parseInt(objProgramacionZona.getProg_horentid()));
            cst.setString(6, objProgramacionZona.getProg_glosa());
            cst.setInt(7, objProgramacionZona.getProg_est());
            cst.setString(8, objProgramacionZona.getProg_usuadd());
            cst.setInt(9, objProgramacionZona.getProg_ord());
            cst.setString(10, objProgramacionZona.getProg_nomrep());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            objParam.setMsgValidacion(cst.getString(12));
            objParam.setFlagIndicador(cst.getInt(11));
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Programacion para la Zona con codigo " + objProgramacionZona.getProg_zonid());
        } catch (SQLException e) {
            objParam.setMsgValidacion("Ocurrio una Excepcion debido al Error " + e.toString());
            objParam.setFlagIndicador(1);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            objParam.setMsgValidacion("Ocurrio una Excepcion debido al Error " + e.toString());
            objParam.setFlagIndicador(1);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return objParam;
    }

    public ParametrosSalida eliminarProgramacionZona(ProgramacionZona objProgramacionZona) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_PROGRAMACIONZONA = "{call pack_tprogramacion_zonas.p_eliminarProgramacionZona(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PROGRAMACIONZONA);
            cst.clearParameters();
            cst.setInt(1, objProgramacionZona.getProg_key());
            cst.setString(2, objProgramacionZona.getProg_zonid());
            cst.setInt(3, objProgramacionZona.getSuc_id());
            cst.setInt(4, objProgramacionZona.getEmp_id());
            cst.setString(5, objProgramacionZona.getProg_usumod());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro de programacion para la zona " + objProgramacionZona.getProg_zondes());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ProgramacionZona> listaProgramacionZona(int i_caso) throws SQLException {
        String SQL_PROGRAMACIONZONAS;
        if (i_caso == 0) {
            SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>" + i_caso
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " order by t.prog_zonid";
        } else {
            SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_caso
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " order by t.prog_zonid";
        }
        P_WHERE = SQL_PROGRAMACIONZONAS;
        objlstProgramacionZona = new ListModelList<ProgramacionZona>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROGRAMACIONZONAS);
            while (rs.next()) {
                objProgramacionZona = new ProgramacionZona();
                objProgramacionZona.setProg_key(rs.getInt("prog_key"));
                objProgramacionZona.setProg_zonid(rs.getString("prog_zonid"));
                objProgramacionZona.setProg_zondes(rs.getString("prog_zondes"));
                objProgramacionZona.setSuc_id(rs.getInt("suc_id"));
                objProgramacionZona.setEmp_id(rs.getInt("emp_id"));
                objProgramacionZona.setProg_trans(rs.getInt("prog_transid"));
                objProgramacionZona.setProg_transid(rs.getString("transporte"));
                objProgramacionZona.setProg_transalias(rs.getString("prog_transalias"));
                objProgramacionZona.setProg_horentid(rs.getString("prog_horentid"));
                objProgramacionZona.setProg_horentdes(rs.getString("prog_horentdes"));
                objProgramacionZona.setProg_glosa(rs.getString("prog_glosa"));
                objProgramacionZona.setProg_est(rs.getInt("prog_est"));
                objProgramacionZona.setProg_usuadd(rs.getString("prog_usuadd"));
                objProgramacionZona.setProg_fecadd(rs.getTimestamp("prog_fecadd"));
                objProgramacionZona.setProg_usumod(rs.getString("prog_usumod"));
                objProgramacionZona.setProg_fecmod(rs.getTimestamp("prog_fecmod"));
                objProgramacionZona.setProg_ord(rs.getInt("prog_ord"));
                objProgramacionZona.setProg_nomrep(rs.getString("prog_nomrep"));
                objlstProgramacionZona.add(objProgramacionZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgramacionZona.getSize() + " registro(s)");
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
        return objlstProgramacionZona;
    }

    public ListModelList<ProgramacionZona> busquedaProgramacionZonas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_PROGRAMACIONZONAS = "", s_validaglosa ="";
        /*if (i_seleccion == 1) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_zonid like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 2) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_zondes like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 3) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_transid like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 4) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_transalias like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 5) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_horentid like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 6) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_horentdes like '" + s_consulta + "' order by t.prog_zonid";
         } else if (i_seleccion == 7) {
         SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
         + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.prog_glosa like '" + s_consulta + "' order by t.prog_zonid";
         }*/
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                        + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                        + " order by t.prog_zonid";
            } else {
                if (i_seleccion == 1) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_zonid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 2) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_zondes like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 3) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_transid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 4) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_transalias like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 5) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_horentid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 6) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_horentdes like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 7) {
                	s_validaglosa = s_consulta.equals("%%")?"":" and t.prog_glosa like '" + s_validaglosa + "' ";
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est<>0"
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_glosa like '" + s_validaglosa + "' order by t.prog_zonid";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                        + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                        + " order by t.prog_zonid";
            } else {
                if (i_seleccion == 1) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_zonid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 2) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_zondes like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 3) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_transid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 4) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_transalias like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 5) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_horentid like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 6) {
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.prog_horentdes like '" + s_consulta + "' order by t.prog_zonid";
                } else if (i_seleccion == 7) {
                	s_validaglosa = s_consulta.equals("%%")?"":" and t.prog_glosa like '" + s_validaglosa + "' ";
                    SQL_PROGRAMACIONZONAS = "select * from v_listaprogramacionzonas t where t.prog_est=" + i_estado
                            + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + s_validaglosa
                            + " order by t.prog_zonid";
                }
            }
        }
        P_WHERE = SQL_PROGRAMACIONZONAS;
        objlstProgramacionZona = new ListModelList<ProgramacionZona>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROGRAMACIONZONAS);
            while (rs.next()) {
                objProgramacionZona = new ProgramacionZona();
                objProgramacionZona.setProg_key(rs.getInt("prog_key"));
                objProgramacionZona.setProg_zonid(rs.getString("prog_zonid"));
                objProgramacionZona.setProg_zondes(rs.getString("prog_zondes"));
                objProgramacionZona.setSuc_id(rs.getInt("suc_id"));
                objProgramacionZona.setEmp_id(rs.getInt("emp_id"));
                objProgramacionZona.setProg_trans(rs.getInt("prog_transid"));
                objProgramacionZona.setProg_transid(rs.getString("transporte"));
                objProgramacionZona.setProg_transalias(rs.getString("prog_transalias"));
                objProgramacionZona.setProg_horentid(rs.getString("prog_horentid"));
                objProgramacionZona.setProg_horentdes(rs.getString("prog_horentdes"));
                objProgramacionZona.setProg_glosa(rs.getString("prog_glosa"));
                objProgramacionZona.setProg_est(rs.getInt("prog_est"));
                objProgramacionZona.setProg_usuadd(rs.getString("prog_usuadd"));
                objProgramacionZona.setProg_fecadd(rs.getTimestamp("prog_fecadd"));
                objProgramacionZona.setProg_usumod(rs.getString("prog_usumod"));
                objProgramacionZona.setProg_fecmod(rs.getTimestamp("prog_fecmod"));
                objProgramacionZona.setProg_ord(rs.getInt("prog_ord"));
                objProgramacionZona.setProg_nomrep(rs.getString("prog_nomrep"));
                objlstProgramacionZona.add(objProgramacionZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgramacionZona.getSize() + " registro(s)");
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
        return objlstProgramacionZona;
    }

}
