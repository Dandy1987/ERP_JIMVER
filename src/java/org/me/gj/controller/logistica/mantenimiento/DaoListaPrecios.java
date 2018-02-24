package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoListaPrecios {

    //Instancias de Objetos
    ListModelList<ListaPrecio> objlstPcompras;
    ListaPrecio objLPcompras;
    ListaPrecio objListaPrecio;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    int i_lp_key = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoListaPrecios.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarListaPrecio(ListaPrecio objLstPrecio) throws SQLException {
        String SQL_INSERTAR_LISTA = "{call pack_tlistaprecios.p_insertarListaPrecio(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_LISTA);
            cst.clearParameters();
            cst.setString("c_lp_des", objLstPrecio.getLp_des());
            cst.setInt("n_emp_id", objLstPrecio.getEmp_id());
            cst.setInt("n_suc_id", objLstPrecio.getSuc_id());
            cst.setLong("n_prov_key", objLstPrecio.getProv_key());
            cst.setInt("n_lp_tipo", objLstPrecio.getLp_tipo());
            cst.setDouble("n_lp_descgen", objLstPrecio.getLp_descgen());
            cst.setDouble("n_lp_descfinan", objLstPrecio.getLp_descfinan());
            cst.setInt("n_lp_ord", objLstPrecio.getLp_ord());
            cst.setString("c_lp_nomrep", objLstPrecio.getLp_nomrep());
            cst.setString("c_lp_usuadd", objLstPrecio.getLp_usuadd());
            cst.registerOutParameter("n_lp_key", java.sql.Types.NUMERIC);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            i_lp_key = cst.getInt("n_lp_key");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objLstPrecio.getLp_des());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, i_lp_key);
    }

    public ParametrosSalida modificarListaPrecio(ListaPrecio objLstPrecio) throws SQLException {
        String SQL_MODIFICAR_LISTA = "{call pack_tlistaprecios.p_modificarListaPrecio(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_LISTA);
            cst.clearParameters();
            cst.setInt("n_lp_key", objLstPrecio.getLp_key());
            cst.setString("c_lp_des", objLstPrecio.getLp_des());
            cst.setInt("n_emp_id", objLstPrecio.getEmp_id());
            cst.setInt("n_suc_id", objLstPrecio.getSuc_id());
            cst.setLong("n_prov_key", objLstPrecio.getProv_key());
            cst.setInt("n_lp_tipo", objLstPrecio.getLp_tipo());
            cst.setDouble("n_lp_descgen", objLstPrecio.getLp_descgen());
            cst.setDouble("n_lp_descfinan", objLstPrecio.getLp_descfinan());
            cst.setInt("n_lp_est", objLstPrecio.getLp_est());
            cst.setInt("n_lp_ord", objLstPrecio.getLp_ord());
            cst.setString("c_lp_nomrep", objLstPrecio.getLp_nomrep());
            cst.setString("c_lp_usumod", objLstPrecio.getLp_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un registro con descripción " + objLstPrecio.getLp_des());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la Modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la Modificacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarListaPrecio(ListaPrecio objLstPrecio) throws SQLException {
        String SQL_ELIMINAR_LISTA = "{call pack_tlistaprecios.p_eliminarListaPrecio(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_LISTA);
            cst.clearParameters();
            cst.setInt("n_lp_key", objLstPrecio.getLp_key());
            cst.setInt("n_emp_id", objLstPrecio.getEmp_id());
            cst.setInt("n_suc_id", objLstPrecio.getSuc_id());
            cst.setInt("n_lp_tipo", objLstPrecio.getLp_tipo());
            cst.setString("c_lp_usumod", objLstPrecio.getLp_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Elimino un nuevo registro con descripción " + objLstPrecio.getLp_des());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la Eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la Eliminacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ListaPrecio> listaPrecios(int emp_id, int suc_id, int estado, int tipo) throws SQLException {
        String sql;
        if (estado == 0) {
            sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est<>" + estado + " and t.lp_tipo=" + tipo + " order by t.lp_key";

        } else {
            sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=" + estado + " and t.lp_tipo=" + tipo + " order by t.lp_key";
        }
        P_WHERE = sql;
        objlstPcompras = new ListModelList<ListaPrecio>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objLPcompras = new ListaPrecio();
                objLPcompras.setLp_key(rs.getInt("lp_key"));
                objLPcompras.setEmp_id(rs.getInt("emp_id"));
                objLPcompras.setSuc_id(rs.getInt("suc_id"));
                objLPcompras.setLp_tipo(rs.getInt("lp_tipo"));
                objLPcompras.setLp_id(rs.getString("lp_id"));
                objLPcompras.setProv_key(rs.getLong("prov_key"));
                objLPcompras.setProv_id(rs.getString("prov_id"));
                objLPcompras.setProv_razsoc(rs.getString("prov_razsoc"));
                objLPcompras.setLp_des(rs.getString("lp_des"));
                objLPcompras.setLp_descgen(rs.getDouble("lp_descgen"));
                objLPcompras.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objLPcompras.setLp_est(rs.getInt("lp_est"));
                objLPcompras.setLp_ord(rs.getInt("lp_ord"));
                objLPcompras.setLp_nomrep(rs.getString("lp_nomrep"));
                objLPcompras.setLp_usuadd(rs.getString("lp_usuadd"));
                objLPcompras.setLp_fecadd(rs.getTimestamp("lp_fecadd"));
                objLPcompras.setLp_usumod(rs.getString("lp_usumod"));
                objLPcompras.setLp_fecmod(rs.getTimestamp("lp_fecmod"));
                objlstPcompras.add(objLPcompras);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPcompras.getSize() + " registro(s)");
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
        return objlstPcompras;
    }

    public ListModelList<ListaPrecio> getListaPreCompxProv(int emp_id, int suc_id, long prov_key) throws SQLException {
        String sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=1 and t.lp_tipo=1 and t.prov_key = " + prov_key + " order by t.lp_key";
        objlstPcompras = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objlstPcompras = new ListModelList<ListaPrecio>();
            while (rs.next()) {
                objLPcompras = new ListaPrecio();
                objLPcompras.setLp_key(rs.getInt("lp_key"));
                objLPcompras.setEmp_id(rs.getInt("emp_id"));
                objLPcompras.setSuc_id(rs.getInt("suc_id"));
                objLPcompras.setLp_tipo(rs.getInt("lp_tipo"));
                objLPcompras.setLp_id(rs.getString("lp_id"));
                objLPcompras.setProv_key(rs.getLong("prov_key"));
                objLPcompras.setProv_id(rs.getString("prov_id"));
                objLPcompras.setProv_razsoc(rs.getString("prov_razsoc"));
                objLPcompras.setLp_des(rs.getString("lp_des"));
                objLPcompras.setLp_descgen(rs.getDouble("lp_descgen"));
                objLPcompras.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objLPcompras.setLp_est(rs.getInt("lp_est"));
                objLPcompras.setLp_ord(rs.getInt("lp_ord"));
                objLPcompras.setLp_nomrep(rs.getString("lp_nomrep"));
                objLPcompras.setLp_usuadd(rs.getString("lp_usuadd"));
                objLPcompras.setLp_fecadd(rs.getTimestamp("lp_fecadd"));
                objLPcompras.setLp_usumod(rs.getString("lp_usumod"));
                objLPcompras.setLp_fecmod(rs.getTimestamp("lp_fecmod"));
                objlstPcompras.add(objLPcompras);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPcompras.getSize() + " registro(s)");
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
        return objlstPcompras;
    }

    public ListModelList<ListaPrecio> busquedaLpPrecio(int emp_id, int suc_id, int i_tipo, int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String sql = "";
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est<>0 and t.lp_tipo=" + i_tipo
                        + " order by t.lp_key";
            } else {

                if (i_seleccion == 1) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est<>0 and t.lp_tipo=" + i_tipo
                            + " and t.lp_id like '" + s_consulta + "' order by t.lp_key";
                } else if (i_seleccion == 2) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est<>0 and t.lp_tipo=" + i_tipo
                            + " and t.lp_des like '" + s_consulta + "' order by t.lp_key";
                } else if (i_seleccion == 3) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est<>0 and t.lp_tipo=" + i_tipo
                            + " and t.prov_razsoc like '" + s_consulta + "' order by t.lp_key";
                }
            }
        } else {
            if (i_seleccion == 0) {
                sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=" + i_estado + " and t.lp_tipo=" + i_tipo
                        + " order by t.lp_key";
            } else {
                if (i_seleccion == 1) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=" + i_estado + " and t.lp_tipo=" + i_tipo
                            + " and t.lp_id like '" + s_consulta + "' order by t.lp_key";
                } else if (i_seleccion == 2) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=" + i_estado + " and t.lp_tipo=" + i_tipo
                            + " and t.lp_des like '" + s_consulta + "' order by t.lp_key";
                } else if (i_seleccion == 3) {
                    sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=" + i_estado + " and t.lp_tipo=" + i_tipo
                            + " and t.prov_razsoc like '" + s_consulta + "' order by t.lp_key";
                }
            }
        }
        P_WHERE = sql;
        objlstPcompras = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objlstPcompras = new ListModelList<ListaPrecio>();
            while (rs.next()) {
                objLPcompras = new ListaPrecio();
                objLPcompras.setLp_key(rs.getInt("lp_key"));
                objLPcompras.setEmp_id(rs.getInt("emp_id"));
                objLPcompras.setSuc_id(rs.getInt("suc_id"));
                objLPcompras.setLp_tipo(rs.getInt("lp_tipo"));
                objLPcompras.setLp_id(rs.getString("lp_id"));
                objLPcompras.setProv_key(rs.getLong("prov_key"));
                objLPcompras.setProv_id(rs.getString("prov_id"));
                objLPcompras.setProv_razsoc(rs.getString("prov_razsoc"));
                objLPcompras.setLp_des(rs.getString("lp_des"));
                objLPcompras.setLp_descgen(rs.getDouble("lp_descgen"));
                objLPcompras.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objLPcompras.setLp_est(rs.getInt("lp_est"));
                objLPcompras.setLp_ord(rs.getInt("lp_ord"));
                objLPcompras.setLp_nomrep(rs.getString("lp_nomrep"));
                objLPcompras.setLp_usuadd(rs.getString("lp_usuadd"));
                objLPcompras.setLp_fecadd(rs.getTimestamp("lp_fecadd"));
                objLPcompras.setLp_usumod(rs.getString("lp_usumod"));
                objLPcompras.setLp_fecmod(rs.getTimestamp("lp_fecmod"));
                objlstPcompras.add(objLPcompras);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPcompras.getSize() + " registro(s)");
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
        return objlstPcompras;
    }

    public ListaPrecio getListaPrecio(int codigo, int tipo) throws SQLException {
        String sql = "select * from v_lislistaprecios t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.lp_tipo=" + tipo
                + " and t.lp_key=" + codigo + " order by t.lp_key";
        P_WHERE = sql;
        objListaPrecio = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objListaPrecio = new ListaPrecio();
                objListaPrecio.setLp_key(rs.getInt("lp_key"));
                objListaPrecio.setEmp_id(rs.getInt("emp_id"));
                objListaPrecio.setSuc_id(rs.getInt("suc_id"));
                objListaPrecio.setLp_tipo(rs.getInt("lp_tipo"));
                objListaPrecio.setLp_id(rs.getString("lp_id"));
                objListaPrecio.setProv_key(rs.getLong("prov_key"));
                objListaPrecio.setProv_id(rs.getString("prov_id"));
                objListaPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objListaPrecio.setLp_des(rs.getString("lp_des"));
                objListaPrecio.setLp_descgen(rs.getDouble("lp_descgen"));
                objListaPrecio.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objListaPrecio.setLp_est(rs.getInt("lp_est"));
                objListaPrecio.setLp_ord(rs.getInt("lp_ord"));
                objListaPrecio.setLp_nomrep(rs.getString("lp_nomrep"));
                objListaPrecio.setLp_usuadd(rs.getString("lp_usuadd"));
                objListaPrecio.setLp_fecadd(rs.getTimestamp("lp_fecadd"));
                objListaPrecio.setLp_usumod(rs.getString("lp_usumod"));
                objListaPrecio.setLp_fecmod(rs.getTimestamp("lp_fecmod"));
                objlstPcompras.add(objListaPrecio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPcompras.getSize() + " registro(s)");
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
        return objListaPrecio;
    }

    public ListaPrecio getListaPreCompxProv(int emp_id, int suc_id, long prov_key, int lp_key) throws SQLException {
        String sql = "select * from v_lislistaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_est=1 and t.lp_tipo=1 "
                + "and t.prov_key = " + prov_key + " and t.lp_key=" + lp_key + " order by t.lp_key";
        objLPcompras = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objLPcompras = new ListaPrecio();
                objLPcompras.setLp_key(rs.getInt("lp_key"));
                objLPcompras.setEmp_id(rs.getInt("emp_id"));
                objLPcompras.setSuc_id(rs.getInt("suc_id"));
                objLPcompras.setLp_tipo(rs.getInt("lp_tipo"));
                objLPcompras.setLp_id(rs.getString("lp_id"));
                objLPcompras.setProv_key(rs.getLong("prov_key"));
                objLPcompras.setProv_id(rs.getString("prov_id"));
                objLPcompras.setProv_razsoc(rs.getString("prov_razsoc"));
                objLPcompras.setLp_des(rs.getString("lp_des"));
                objLPcompras.setLp_descgen(rs.getDouble("lp_descgen"));
                objLPcompras.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objLPcompras.setLp_est(rs.getInt("lp_est"));
                objLPcompras.setLp_ord(rs.getInt("lp_ord"));
                objLPcompras.setLp_nomrep(rs.getString("lp_nomrep"));
                objLPcompras.setLp_usuadd(rs.getString("lp_usuadd"));
                objLPcompras.setLp_fecadd(rs.getTimestamp("lp_fecadd"));
                objLPcompras.setLp_usumod(rs.getString("lp_usumod"));
                objLPcompras.setLp_fecmod(rs.getTimestamp("lp_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPcompras.getSize() + " registro(s)");
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
        return objLPcompras;
    }

    public String validaMovimientos(ListaPrecio objLstPrecio) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tlistaprecios.p_valida_movimientos(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objLstPrecio.getLp_key());
            cst.setInt(2, objLstPrecio.getEmp_id());
            cst.setInt(3, objLstPrecio.getSuc_id());
            cst.setInt(4, objLstPrecio.getLp_tipo());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la lista de precios " + objLstPrecio.getLp_id());
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
}
