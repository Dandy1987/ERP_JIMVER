package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.ProgramacionRutas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoProgramacionRutas {

    //Instancia de Objetos
    ListModelList<ProgramacionRutas> objlstProgramacionRutas;
    ProgramacionRutas objProgramacionRutas;
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
    private static final Logger LOGGER = Logger.getLogger(DaoProgramacionRutas.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarProgramacionRutas(ProgramacionRutas objProgramacionRutas) throws SQLException {
        String progruta_dvisid = objProgramacionRutas.getProgruta_dvisid();
        String progruta_hentid = objProgramacionRutas.getProgruta_hentid();
        String progruta_transid = objProgramacionRutas.getProgruta_transid();
        String progruta_zonid = objProgramacionRutas.getProgruta_zonid();
        String progruta_glosa = objProgramacionRutas.getProgruta_glosa();
        int progruta_estado = objProgramacionRutas.getProgruta_estado();
        String progruta_usuadd = objProgramacionRutas.getProgruta_usuadd();
        int em_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_PROGRUTAS = "{call pack_tprogramacion_rutas.p_insertarProgramacionRutas(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PROGRUTAS);
            cst.clearParameters();
            cst.setInt(1, em_id);
            cst.setInt(2, suc_id);
            cst.setString(3, progruta_dvisid);
            cst.setString(4, progruta_hentid);
            cst.setString(5, progruta_transid);
            cst.setString(6, progruta_zonid);
            cst.setString(7, progruta_glosa);
            cst.setInt(8, progruta_estado);
            cst.setString(9, progruta_usuadd);
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con los codigos :  dia de visita (" + progruta_dvisid + ") , transporte (" + progruta_transid + ") ,  zona (" + progruta_zonid + ") y  horario (" + progruta_hentid + ") ");
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarProgramacionRutas(ProgramacionRutas objProgramacionRutas) throws SQLException {
        int progruta_key = objProgramacionRutas.getProgruta_key();
        String progruta_dvisid = objProgramacionRutas.getProgruta_dvisid();
        String progruta_hentid = objProgramacionRutas.getProgruta_hentid();
        String progruta_transid = objProgramacionRutas.getProgruta_transid();
        String progruta_zonid = objProgramacionRutas.getProgruta_zonid();
        String progruta_glosa = objProgramacionRutas.getProgruta_glosa();
        int progruta_estado = objProgramacionRutas.getProgruta_estado();
        String pcambios = objProgramacionRutas.getPcambios();
        String progruta_usumod = objProgramacionRutas.getProgruta_usumod();
        int em_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_PROGRUTAS = "{call pack_tprogramacion_rutas.p_actualizarProgramacionRutas(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PROGRUTAS);
            cst.clearParameters();
            cst.setInt(1, em_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, progruta_key);
            cst.setString(4, progruta_dvisid);
            cst.setString(5, progruta_hentid);
            cst.setString(6, progruta_transid);
            cst.setString(7, progruta_zonid);
            cst.setString(8, progruta_glosa);
            cst.setInt(9, progruta_estado);
            cst.setString(10, pcambios);
            cst.setString(11, progruta_usumod);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + progruta_key);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarProgramacionRutas(ProgramacionRutas objProgramacionRutas) throws SQLException {
        int progruta_key = objProgramacionRutas.getProgruta_key();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_PROGRUTAS = "{call pack_tprogramacion_rutas.p_eliminarProgramacionRutas(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PROGRUTAS);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, progruta_key);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + progruta_key);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ProgramacionRutas> listaProgramacionRutas(int i_caso) throws SQLException {
        String SQL_PROGRUTAS;
        if (i_caso == 0) {
            SQL_PROGRUTAS = "select * from v_listaprogramacionrutas t where t.EMP_ID="+ objUsuCredential.getCodemp()
                    + " and t.SUC_ID=" + objUsuCredential.getCodsuc()
                    + " and t.progruta_estado<>" + i_caso + " "
                    + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
        } else {
            SQL_PROGRUTAS = "select * from v_listaprogramacionrutas t where t.EMP_ID="+ objUsuCredential.getCodemp()
                    + " and t.SUC_ID=" + objUsuCredential.getCodsuc()
                    + " and t.progruta_estado=" + i_caso + " "
                    + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
        }
        P_WHERE = SQL_PROGRUTAS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROGRUTAS);
            objlstProgramacionRutas = new ListModelList<ProgramacionRutas>();
            while (rs.next()) {
                objProgramacionRutas = new ProgramacionRutas();
                objProgramacionRutas.setProgruta_key(rs.getInt("progruta_key"));
                objProgramacionRutas.setProgruta_id(rs.getString("progruta_id"));
                objProgramacionRutas.setProgruta_hentid(rs.getString("progruta_hentid"));
                objProgramacionRutas.setProgruta_hentdes(rs.getString("progruta_hentdes"));
                objProgramacionRutas.setProgruta_dvisid(rs.getString("progruta_dvisid"));
                objProgramacionRutas.setProgruta_dvisdes(rs.getString("progruta_dvisdes"));
                objProgramacionRutas.setProgruta_transid(rs.getString("progruta_transid"));
                objProgramacionRutas.setProgruta_transdes(rs.getString("progruta_transdes"));
                objProgramacionRutas.setProgruta_estado(rs.getInt("progruta_estado"));
                objProgramacionRutas.setProgruta_glosa(rs.getString("progruta_glosa"));
                objProgramacionRutas.setProgruta_zonid(rs.getString("progruta_zonid"));
                objProgramacionRutas.setProgruta_zondes(rs.getString("progruta_zondes"));
                objProgramacionRutas.setProgruta_usuadd(rs.getString("progruta_usuadd"));
                objProgramacionRutas.setProgruta_fecadd(rs.getTimestamp("progruta_fecadd"));
                objProgramacionRutas.setProgruta_usumod(rs.getString("progruta_usumod"));
                objProgramacionRutas.setProgruta_fecmod(rs.getTimestamp("progruta_fecmod"));
                objlstProgramacionRutas.add(objProgramacionRutas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgramacionRutas.getSize() + " registro(s)");
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
        return objlstProgramacionRutas;
    }

    public ListModelList<ProgramacionRutas> busquedaProgramacionRutas(int emp_id, int suc_id, int seleccion, String busqueda, int estado) throws SQLException {
        String SQL_LISTA_PROGRUTA = "";
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();

        if (estado == 3) { // TODOS 
            if (seleccion == 0) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado<>0 and t.emp_id=" + emp_id + " and t.suc_id= " + suc_id + ""
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 1) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado<>0 and t.PROGRUTA_DVISID||'-'||t.progruta_dvisdes like '" + busqueda + "' and t.emp_id=" + emp_id + " "
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 2) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado<>0 and t.progruta_glosa like '" + busqueda + "' and t.emp_id=" + emp_id + " "
                        + " and t.suc_id= " + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 3) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado<>0 "
                        + " and T.PROGRUTA_TRANSID||'-'||t.progruta_transdes like '" + busqueda + "' and t.emp_id=" + emp_id + ""
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 4) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado<>0 and t.PROGRUTA_ZONID||'-'||t.progruta_zondes like '" + busqueda + "' and t.emp_id=" + emp_id + " "
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            }
        } else {
            if (seleccion == 0) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado=" + estado + " "
                        + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + ""
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 1) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado=" + estado + " "
                        + " and t.PROGRUTA_DVISID||'-'||t.progruta_dvisdes like '" + busqueda + "' and t.emp_id=" + emp_id + ""
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 2) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado=" + estado + " "
                        + " and t.progruta_glosa like '" + busqueda + "' and t.emp_id=" + emp_id + ""
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 3) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado=" + estado + " "
                        + " and T.PROGRUTA_TRANSID||'-'||t.progruta_transdes like '" + busqueda + "' and t.emp_id=" + emp_id + ""
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            } else if (seleccion == 4) {
                SQL_LISTA_PROGRUTA = " select * from v_listaprogramacionrutas t"
                        + " where t.progruta_estado=" + estado + " "
                        + " and t.PROGRUTA_ZONID||'-'||t.progruta_zondes like '" + busqueda + "' and t.emp_id=" + emp_id + ""
                        + " and t.suc_id=" + suc_id + " "
                        + " order by t.progruta_dvisid,progruta_transid,t.progruta_glosa, t.progruta_zonid";
            }
        }
        P_WHERE = SQL_LISTA_PROGRUTA;
        objlstProgramacionRutas = new ListModelList<ProgramacionRutas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PROGRUTA);
            while (rs.next()) {
                objProgramacionRutas = new ProgramacionRutas();
                objProgramacionRutas.setProgruta_key(rs.getInt("progruta_key"));
                objProgramacionRutas.setProgruta_id(rs.getString("progruta_id"));
                objProgramacionRutas.setProgruta_hentid(rs.getString("progruta_hentid"));
                objProgramacionRutas.setProgruta_hentdes(rs.getString("progruta_hentdes"));
                objProgramacionRutas.setProgruta_dvisid(rs.getString("progruta_dvisid"));
                objProgramacionRutas.setProgruta_dvisdes(rs.getString("progruta_dvisdes"));
                objProgramacionRutas.setProgruta_transid(rs.getString("progruta_transid"));
                objProgramacionRutas.setProgruta_transdes(rs.getString("progruta_transdes"));
                objProgramacionRutas.setProgruta_estado(rs.getInt("progruta_estado"));
                objProgramacionRutas.setProgruta_glosa(rs.getString("progruta_glosa"));
                objProgramacionRutas.setProgruta_zonid(rs.getString("progruta_zonid"));
                objProgramacionRutas.setProgruta_zondes(rs.getString("progruta_zondes"));
                objProgramacionRutas.setProgruta_usuadd(rs.getString("progruta_usuadd"));
                objProgramacionRutas.setProgruta_fecadd(rs.getTimestamp("progruta_fecadd"));
                objProgramacionRutas.setProgruta_usumod(rs.getString("progruta_usumod"));
                objProgramacionRutas.setProgruta_fecmod(rs.getTimestamp("progruta_fecmod"));
                objlstProgramacionRutas.add(objProgramacionRutas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgramacionRutas.getSize() + " registro(s)");
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
        return objlstProgramacionRutas;
    }

}
