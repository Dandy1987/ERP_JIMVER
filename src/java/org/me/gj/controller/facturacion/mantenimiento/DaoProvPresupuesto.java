package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ProvPresupuesto;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoProvPresupuesto {

    //Instancias de Objetos
    ListModelList<ProvPresupuesto> objlstProvPresupuestoes;
    ProvPresupuesto objProvPresupuesto;
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
    private static final Logger LOGGER = Logger.getLogger(DaoProvPresupuesto.class);

    //Eventos Primarios - Transaccionales
    public String insertarProvPresupuesto(ProvPresupuesto objProvPresupuesto) throws SQLException {
        String provpresu_des = objProvPresupuesto.getProvpresu_des();
        String provpresu_desabre = objProvPresupuesto.getProvpresu_desabre();
        int provpresu_provkey = objProvPresupuesto.getProvpresu_provkey();
        String provpresu_usuadd = objProvPresupuesto.getProvpresu_usuadd();
        String SQL_INSERTAR_PROVPRESU = "{call PACK_TPROVPRESU.p_insertarProvPresupuesto(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PROVPRESU);
            cst.clearParameters();
            cst.setString(1, provpresu_des);
            cst.setString(2, provpresu_desabre);
            cst.setInt(3, provpresu_provkey);
            cst.setString(4, provpresu_usuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripcion " + provpresu_des);
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
        return s_msg;
    }

    public String actualizarProvPresupuesto(ProvPresupuesto objProvPresupuesto) throws SQLException {
        int provpresu_key = objProvPresupuesto.getProvpresu_key();
        String provpresu_des = objProvPresupuesto.getProvpresu_des();
        String provpresu_desabre = objProvPresupuesto.getProvpresu_desabre();
        int i_provpresu_provkey = objProvPresupuesto.getProvpresu_provkey();
        int i_provpresu_est = objProvPresupuesto.getProvpresu_est();
        String provpresu_usumod = objProvPresupuesto.getProvpresu_usumod();
        String SQL_ACTUALIZAR_PROVPRESU = "{call pack_tprovpresu.p_actualizarProvPresupuesto(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PROVPRESU);
            cst.clearParameters();
            cst.setInt(1, provpresu_key);
            cst.setString(2, provpresu_des);
            cst.setString(3, provpresu_desabre);
            cst.setInt(4, i_provpresu_provkey);
            cst.setInt(5, i_provpresu_est);
            cst.setString(6, provpresu_usumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + provpresu_key);
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
        return s_msg;
    }

    public String eliminarProvPresupuesto(ProvPresupuesto objProvPresupuesto) throws SQLException {
        int provpresu_key = objProvPresupuesto.getProvpresu_key();
        String SQL_ELIMINAR_PROVPRESU = "{call pack_tprovpresu.p_eliminarProvPresupuesto(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PROVPRESU);
            cst.clearParameters();
            cst.setInt(1, provpresu_key);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + provpresu_key);
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
        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ProvPresupuesto> listaProvPresupuestoes(int i_caso) throws SQLException {
        String SQL_PROVPRESUES;
        SQL_PROVPRESUES = "select p.* from v_listaprovpresu p where p.PROVPRESU_EST =" + i_caso + " order by p.provpresu_id";

        P_WHERE = SQL_PROVPRESUES;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROVPRESUES);
            objlstProvPresupuestoes = new ListModelList<ProvPresupuesto>();
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));

                objlstProvPresupuestoes.add(objProvPresupuesto);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProvPresupuestoes.getSize() + " registro(s)");
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
        return objlstProvPresupuestoes;
    }

    public ListModelList<ProvPresupuesto> busquedaProvPresupuestoes(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;
        if (i_estado == 3) {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est<>0 order by p.provpresu_id";
            } else {
            	if (seleccion == 1) {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est<>0 and p.provpresu_key like '" + consulta + "' order by p.provpresu_id";
                }
            	else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est<>0 and p.provpresu_provdes like '" + consulta + "' order by p.provpresu_id";
                } else {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est<>0 and p.provpresu_desabre like '" + consulta + "' order by provpresu_id";
                }
            }
        } else {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p. provpresu_est=" + i_estado + " order by provpresu_id";
            } else {
            	if (seleccion == 1) {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est=" + i_estado + " and provpresu_key like '" + consulta + "' order by provpresu_id";
                }
            	else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est=" + i_estado + " and provpresu_provdes like '" + consulta + "' order by provpresu_id";
                } else {
                    SQL_BUSQUEDA = "select p.* from v_listaprovpresu p WHERE p.provpresu_est=" + i_estado + " and provpresu_desabre like '" + consulta + "' order by provpresu_id";
                }
            }
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstProvPresupuestoes = new ListModelList<ProvPresupuesto>();
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));
                objlstProvPresupuestoes.add(objProvPresupuesto);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstProvPresupuestoes;
    }

    public ProvPresupuesto ProvPresxProveedor(String proveedor, String busqueda) throws SQLException {
        String SQL_PROVPRESUES;
        SQL_PROVPRESUES = "select p.* from v_listaprovpresu p where p.provpresu_provid =" + proveedor + " order by p.provpresu_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROVPRESUES);
            objProvPresupuesto = null;
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProvPresupuestoes.getSize() + " registro(s)");
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
        return objProvPresupuesto;
    }

    public ProvPresupuesto ProvPresxProveedor(String proveedor, int busqueda) throws SQLException {
        String SQL_PROVPRESUES;
        SQL_PROVPRESUES = "select p.* from v_listaprovpresu p where p.provpresu_provid =" + proveedor + " and p.provpresu_key =" + busqueda + " order by p.provpresu_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROVPRESUES);
            objProvPresupuesto = null;
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProvPresupuestoes.getSize() + " registro(s)");
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
        return objProvPresupuesto;
    }

    public ListModelList<ProvPresupuesto> listaProvPresuxProv(String proveedor) throws SQLException {
        String SQL_PROVPRESUES;
        SQL_PROVPRESUES = "select p.* from v_listaprovpresu p where p.provpresu_provid =" + proveedor + " "
                + "order by p.provpresu_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROVPRESUES);
            objlstProvPresupuestoes = new ListModelList<ProvPresupuesto>();
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));

                objlstProvPresupuestoes.add(objProvPresupuesto);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProvPresupuestoes.getSize() + " registro(s)");
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
        return objlstProvPresupuestoes;
    }

    public ListModelList<ProvPresupuesto> busquedaProvPresuxProv(String proveedor, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;

        SQL_BUSQUEDA = "select p.* from v_listaprovpresu p where p.provpresu_provid =" + proveedor + " and p.provpresu_desabre like '" + consulta + "' and p.provpresu_est=" + i_estado + " "
                + "order by p.provpresu_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstProvPresupuestoes = new ListModelList<ProvPresupuesto>();
            while (rs.next()) {
                objProvPresupuesto = new ProvPresupuesto();
                objProvPresupuesto.setProvpresu_key(rs.getInt("provpresu_key"));
                objProvPresupuesto.setProvpresu_id(rs.getString("provpresu_id"));
                objProvPresupuesto.setProvpresu_provid(rs.getString("provpresu_provid"));
                objProvPresupuesto.setProvpresu_provdes(rs.getString("provpresu_provdes"));
                objProvPresupuesto.setProvpresu_des(rs.getString("provpresu_des"));
                objProvPresupuesto.setProvpresu_desabre(rs.getString("provpresu_desabre"));
                objProvPresupuesto.setProvpresu_est(rs.getInt("provpresu_est"));
                objProvPresupuesto.setProvpresu_usuadd(rs.getString("provpresu_usuadd"));
                objProvPresupuesto.setProvpresu_fecadd(rs.getTimestamp("provpresu_fecadd"));
                objProvPresupuesto.setProvpresu_usumod(rs.getString("provpresu_usuamod"));
                objProvPresupuesto.setProvpresu_fecmod(rs.getTimestamp("provpresu_fecmod"));
                objlstProvPresupuestoes.add(objProvPresupuesto);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstProvPresupuestoes;
    }

    public ParametrosSalida validamovimiento(int provpresu) throws SQLException {
        String SQL_VALIDAMOVIMIENTO = "{call pack_tprovpresu.p_verificamovimiento(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDAMOVIMIENTO);
            cst.clearParameters();
            cst.setInt(1, provpresu);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + provpresu);
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

}
