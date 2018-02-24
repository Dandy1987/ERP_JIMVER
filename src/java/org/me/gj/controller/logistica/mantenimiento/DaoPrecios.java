package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPrecios {

    //Instancias de Objetos
    ListModelList<Precio> objlstPrecios;
    Precio objPrecio;
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
    private static final Logger LOGGER = Logger.getLogger(DaoPrecios.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarPrecio(Precio objPrecio) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_PRECIO = "{call pack_tprecios.p_insertarPrecio(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PRECIO);
            cst.clearParameters();
            cst.setInt("n_emp_id", objPrecio.getEmp_id());
            cst.setInt("n_suc_id", objPrecio.getSuc_id());
            cst.setString("c_pro_id", objPrecio.getPro_id());
            cst.setInt("n_lp_key", objPrecio.getLp_key());
            cst.setInt("n_lp_tipo", objPrecio.getLp_tipo());
            cst.setDouble("n_pre_valvent", objPrecio.getPre_valvent());
            cst.setDouble("n_pre_igv", objPrecio.getPre_igv());
            cst.setDouble("n_pre_venta", objPrecio.getPre_venta());
            cst.setString("c_pre_nomrep", objPrecio.getPre_nomrep());
            cst.setInt("n_pre_ord", objPrecio.getPre_ord());
            cst.setString("c_pre_usuadd", objPrecio.getPre_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | asignó un precio al producto con código " + objPrecio.getPro_key());
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

    public ParametrosSalida modificarPrecio(Precio objPrecio) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_PRECIO = "{call pack_tprecios.p_modificarPrecio(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_PRECIO);
            cst.clearParameters();
            cst.setLong("n_pre_key", objPrecio.getPre_key());
            cst.setInt("n_emp_id", objPrecio.getEmp_id());
            cst.setInt("n_suc_id", objPrecio.getSuc_id());
            cst.setString("c_pro_id", objPrecio.getPro_id());
            cst.setInt("n_lp_key", objPrecio.getLp_key());
            cst.setInt("n_lp_tipo", objPrecio.getLp_tipo());
            cst.setDouble("n_pre_valvent", objPrecio.getPre_valvent());
            cst.setDouble("n_pre_igv", objPrecio.getPre_igv());
            cst.setDouble("n_pre_venta", objPrecio.getPre_venta());
            cst.setInt("n_pre_est", objPrecio.getPre_est());
            cst.setString("c_pre_nomrep", objPrecio.getPre_nomrep());
            cst.setInt("n_pre_ord", objPrecio.getPre_ord());
            cst.setString("c_pre_usumod", objPrecio.getPre_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | asignó un precio al producto con código " + objPrecio.getPro_key());
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

    public ParametrosSalida eliminarPrecio(Precio objPrecio) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_PRECIO = "{call pack_tprecios.p_eliminarPrecio(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PRECIO);
            cst.clearParameters();
            cst.setLong("n_pre_key", objPrecio.getPre_key());
            cst.setInt("n_emp_id", objPrecio.getEmp_id());
            cst.setInt("n_suc_id", objPrecio.getSuc_id());
            cst.setString("c_pre_usumod", objPrecio.getPre_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | asignó un precio al producto con código " + objPrecio.getPro_key());
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
    public ListModelList<Precio> lstPrecios(int emp_id, int suc_id, String prov_id, String lp_key, String lp_tipo, String pro_id) throws SQLException {
        String SQL_PRECIOS;
        if (prov_id.isEmpty() && lp_key.isEmpty()) {
            SQL_PRECIOS = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.lp_tipo ='" + lp_tipo + "' and t.pre_est=1  order by t.pro_id,t.lp_key";
        } else {
            SQL_PRECIOS = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + prov_id
                    + "' and t.lp_id like '" + lp_key + "' and t.lp_tipo like '" + lp_tipo + "' and t.pre_est=1 and t.pro_id like '" + pro_id + "' order by t.pro_id,t.lp_key";
        }
        P_WHERE = SQL_PRECIOS;
        objlstPrecios = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            objlstPrecios = new ListModelList<Precio>();
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_key(rs.getLong("pre_key"));
                objPrecio.setEmp_id(rs.getInt("emp_id"));
                objPrecio.setSuc_id(rs.getInt("suc_id"));
                objPrecio.setPro_id(rs.getString("pro_id"));
                objPrecio.setPro_des(rs.getString("pro_des"));
                objPrecio.setPro_desdes(rs.getString("pro_desdes"));
                objPrecio.setImp_valor(rs.getDouble("imp_valor"));
                objPrecio.setPro_peso(rs.getDouble("pro_peso"));
                objPrecio.setPro_condimp(rs.getString("pro_condimp"));
                objPrecio.setProv_key(rs.getLong("prov_key"));
                objPrecio.setProv_id(rs.getString("prov_id"));
                objPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objPrecio.setLp_key(rs.getInt("lp_key"));
                objPrecio.setLp_id(rs.getString("lp_id"));
                objPrecio.setLp_tipo(rs.getInt("lp_tipo"));
                objPrecio.setLp_des(rs.getString("lp_des"));
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));
                objPrecio.setPre_igv(rs.getDouble("pre_igv"));
                objPrecio.setPre_venta(rs.getDouble("pre_venta"));
                objPrecio.setPre_est(rs.getInt("pre_est"));
                objPrecio.setPre_nomrep(rs.getString("pre_nomrep"));
                objPrecio.setPre_ord(rs.getInt("pre_ord"));
                objPrecio.setPre_usuadd(rs.getString("pre_usuadd"));
                objPrecio.setPre_fecadd(rs.getTimestamp("pre_fecadd"));
                objPrecio.setPre_usumod(rs.getString("pre_usumod"));
                objPrecio.setPre_fecmod(rs.getTimestamp("pre_fecmod"));
                objlstPrecios.add(objPrecio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objlstPrecios;
    }

    public ListModelList<Precio> getPreCompxProveedor(int emp_id, int suc_id, long prov_key, int lp_key) throws SQLException {
        String SQL_PRECIOS = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_key=" + prov_key + " and "
                + "t.lp_key=" + lp_key + " and t.lp_tipo=1 and t.pre_est=1 order by t.pro_id,t.lp_key";
        objlstPrecios = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            objlstPrecios = new ListModelList<Precio>();
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_key(rs.getLong("pre_key"));
                objPrecio.setEmp_id(rs.getInt("emp_id"));
                objPrecio.setSuc_id(rs.getInt("suc_id"));
                objPrecio.setPro_id(rs.getString("pro_id"));
                objPrecio.setPro_des(rs.getString("pro_des"));
                objPrecio.setPro_desdes(rs.getString("pro_desdes"));
                objPrecio.setImp_valor(rs.getDouble("imp_valor"));
                objPrecio.setPro_peso(rs.getDouble("pro_peso"));
                objPrecio.setPro_condimp(rs.getString("pro_condimp"));
                objPrecio.setProv_key(rs.getLong("prov_key"));
                objPrecio.setProv_id(rs.getString("prov_id"));
                objPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objPrecio.setLp_key(rs.getInt("lp_key"));
                objPrecio.setLp_id(rs.getString("lp_id"));
                objPrecio.setLp_tipo(rs.getInt("lp_tipo"));
                objPrecio.setLp_des(rs.getString("lp_des"));
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));
                objPrecio.setPre_igv(rs.getDouble("pre_igv"));
                objPrecio.setPre_venta(rs.getDouble("pre_venta"));
                objPrecio.setPre_est(rs.getInt("pre_est"));
                objPrecio.setPre_nomrep(rs.getString("pre_nomrep"));
                objPrecio.setPre_ord(rs.getInt("pre_ord"));
                objPrecio.setPre_usuadd(rs.getString("pre_usuadd"));
                objPrecio.setPre_fecadd(rs.getTimestamp("pre_fecadd"));
                objPrecio.setPre_usumod(rs.getString("pre_usumod"));
                objPrecio.setPre_fecmod(rs.getTimestamp("pre_fecmod"));
                objlstPrecios.add(objPrecio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objlstPrecios;
    }

    public ListModelList<Precio> busquedaPreciosVenta(int emp_id, int suc_id, int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String sql = "";
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                        + "t.lp_tipo=2  and t.pre_est<>0 order by t.lp_key";
            } else {
                if (i_seleccion == 1) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.lp_id like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est<>0 order by t.lp_key";
                } else if (i_seleccion == 2) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.lp_des like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est<>0 order by t.lp_key";
                } else if (i_seleccion == 3) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.pro_id like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est<>0 order by t.lp_key";
                } else if (i_seleccion == 4) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.pro_des like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est<>0 order by t.lp_key";
                }
            }
        } else {
            if (i_seleccion == 0) {
                sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                        + "t.lp_tipo=2 and t.pre_est=" + i_estado + " order by t.lp_key";
            } else {
                if (i_seleccion == 1) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.lp_id like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est=" + i_estado + " order by t.lp_key";
                } else if (i_seleccion == 2) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.lp_des like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est=" + i_estado + " order by t.lp_key";
                } else if (i_seleccion == 3) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.pro_id like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est=" + i_estado + " order by t.lp_key";
                } else if (i_seleccion == 4) {
                    sql = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                            + "t.pro_des like '" + s_consulta + "' and t.lp_tipo=2 and t.pre_est=" + i_estado + " order by t.lp_key";
                }
            }
        }

        objlstPrecios = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objlstPrecios = new ListModelList<Precio>();
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_key(rs.getLong("pre_key"));
                objPrecio.setEmp_id(rs.getInt("emp_id"));
                objPrecio.setSuc_id(rs.getInt("suc_id"));
                objPrecio.setPro_id(rs.getString("pro_id"));
                objPrecio.setPro_des(rs.getString("pro_des"));
                objPrecio.setPro_desdes(rs.getString("pro_desdes"));
                objPrecio.setImp_valor(rs.getDouble("imp_valor"));
                objPrecio.setPro_peso(rs.getDouble("pro_peso"));
                objPrecio.setPro_condimp(rs.getString("pro_condimp"));
                objPrecio.setProv_key(rs.getLong("prov_key"));
                objPrecio.setProv_id(rs.getString("prov_id"));
                objPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objPrecio.setLp_key(rs.getInt("lp_key"));
                objPrecio.setLp_id(rs.getString("lp_id"));
                objPrecio.setLp_tipo(rs.getInt("lp_tipo"));
                objPrecio.setLp_des(rs.getString("lp_des"));
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));
                objPrecio.setPre_igv(rs.getDouble("pre_igv"));
                objPrecio.setPre_venta(rs.getDouble("pre_venta"));
                objPrecio.setPre_est(rs.getInt("pre_est"));
                objPrecio.setPre_nomrep(rs.getString("pre_nomrep"));
                objPrecio.setPre_ord(rs.getInt("pre_ord"));
                objPrecio.setPre_usuadd(rs.getString("pre_usuadd"));
                objPrecio.setPre_fecadd(rs.getTimestamp("pre_fecadd"));
                objPrecio.setPre_usumod(rs.getString("pre_usumod"));
                objPrecio.setPre_fecmod(rs.getTimestamp("pre_fecmod"));
                objlstPrecios.add(objPrecio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objlstPrecios;
    }

    public Precio getPrecioxLpCompra(int emp_id, int suc_id, String pro_id, long prov_key, int lpc_key) throws SQLException {
        String SQL_PRECIOS = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pro_id=" + pro_id
                + " and t.prov_key=" + prov_key + " and t.lp_key=" + lpc_key + " and t.lp_tipo=1 order by t.pro_id,t.lp_key";
        objPrecio = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_key(rs.getLong("pre_key"));
                objPrecio.setEmp_id(rs.getInt("emp_id"));
                objPrecio.setSuc_id(rs.getInt("suc_id"));
                objPrecio.setPro_id(rs.getString("pro_id"));
                objPrecio.setPro_des(rs.getString("pro_des"));
                objPrecio.setPro_desdes(rs.getString("pro_desdes"));
                objPrecio.setImp_valor(rs.getDouble("imp_valor"));
                objPrecio.setPro_peso(rs.getDouble("pro_peso"));
                objPrecio.setPro_condimp(rs.getString("pro_condimp"));
                objPrecio.setProv_key(rs.getLong("prov_key"));
                objPrecio.setProv_id(rs.getString("prov_id"));
                objPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objPrecio.setLp_key(rs.getInt("lp_key"));
                objPrecio.setLp_id(rs.getString("lp_id"));
                objPrecio.setLp_tipo(rs.getInt("lp_tipo"));
                objPrecio.setLp_des(rs.getString("lp_des"));
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));
                objPrecio.setPre_igv(rs.getDouble("pre_igv"));
                objPrecio.setPre_venta(rs.getDouble("pre_venta"));
                objPrecio.setPre_est(rs.getInt("pre_est"));
                objPrecio.setPre_nomrep(rs.getString("pre_nomrep"));
                objPrecio.setPre_ord(rs.getInt("pre_ord"));
                objPrecio.setPre_usuadd(rs.getString("pre_usuadd"));
                objPrecio.setPre_fecadd(rs.getTimestamp("pre_fecadd"));
                objPrecio.setPre_usumod(rs.getString("pre_usumod"));
                objPrecio.setPre_fecmod(rs.getTimestamp("pre_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objPrecio;
    }

    public Precio getPrecioxLpVenta(int emp_id, int suc_id, String pro_id, String listaprecio) throws SQLException {
        String SQL_PRECIOS = "select * from v_listaprecios t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + " and t.lp_tipo=2 and t.pre_est=1 and t.pro_id=" + pro_id + " and t.lp_id = " + listaprecio;
       // objPrecio = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            objPrecio = null;
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_key(rs.getLong("pre_key"));
                objPrecio.setImp_valor(rs.getDouble("imp_valor"));
                objPrecio.setPro_peso(rs.getDouble("pro_peso"));
                objPrecio.setPro_condimp(rs.getString("pro_condimp"));
                objPrecio.setProv_key(rs.getLong("prov_key"));
                objPrecio.setProv_id(rs.getString("prov_id"));
                objPrecio.setProv_razsoc(rs.getString("prov_razsoc"));
                objPrecio.setLp_key(rs.getInt("lp_key"));
                objPrecio.setLp_id(rs.getString("lp_id"));
                objPrecio.setLp_des(rs.getString("lp_des"));
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));
                objPrecio.setPre_igv(rs.getDouble("pre_igv"));
                objPrecio.setPre_venta(rs.getDouble("pre_venta"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objPrecio;
    }

    public Precio listaProvPrecioNIC(String pro_id) throws SQLException {
        String SQL_PRECIOS;
        SQL_PRECIOS = "select t.pre_valvent from v_listaprecios t where t.pro_id= " + pro_id;
        objPrecio = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            while (rs.next()) {
                objPrecio = new Precio();
                objPrecio.setPre_valvent(rs.getDouble("pre_valvent"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPrecios.getSize() + " registro(s)");
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
        return objPrecio;
    }

    public ParametrosSalida verificarPrecio(int emp_id, int suc_id, String c_pro_id, String prov_key, int lp_key, int lp_tipo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String pro_id;
        String pro_des;
        String SQL_VERIFICAR_PRECIO = "{call pack_tprecios.p_verificaPrecioProducto(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VERIFICAR_PRECIO);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_prod_id", c_pro_id);
            cst.setString("c_prov_key", prov_key);
            cst.setInt("n_lp_key", lp_key);
            cst.setInt("n_lp_tipo", lp_tipo);
            cst.registerOutParameter("c_pro_id", java.sql.Types.VARCHAR);
            cst.registerOutParameter("c_pro_des", java.sql.Types.VARCHAR);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            pro_id = cst.getString("c_pro_id");
            pro_des = cst.getString("c_pro_des");
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | verifico el Precio para el Producto " + pro_id);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            pro_id = "";
            pro_des = "";
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la verificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            pro_id = "";
            pro_des = "";
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la verificacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, pro_id, pro_des);
    }

    public String validaMovimientos(Precio objPrecio) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tprecios.p_valida_movimientos(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setLong(1, objPrecio.getPre_key());
            cst.setInt(2, objPrecio.getLp_key());
            cst.setInt(3, objPrecio.getLp_tipo());
            cst.setString(4, objPrecio.getPro_id());
            cst.setInt(5, objPrecio.getEmp_id());
            cst.setInt(6, objPrecio.getSuc_id());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del precio " + objPrecio.getPre_key());
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
