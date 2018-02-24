package org.me.gj.controller.logistica.mantenimiento;

import java.sql.*;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Condicion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCondicion {

    //Instancias de Objetos
    ListModelList<Condicion> objlstConComp;
    Condicion objConComp;
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
    private static final Logger LOGGER = Logger.getLogger(DaoCondicion.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(Condicion objConComp) throws SQLException {
        String conDes = objConComp.getConDes();
        String conTipo = objConComp.getConTipo();
        String conUsuadd = objUsuCredential.getCuenta();
        int conDias = objConComp.getConDias();
        int conOrd = objConComp.getConOrd();
        String conNomRep = objConComp.getConNomRep();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_CONDICION = "{call pack_tcondicion.p_insertarCondicion(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CONDICION);
            cst.clearParameters();
            cst.setString(1, conDes);
            cst.setString(2, conTipo);
            cst.setInt(3, conDias);
            cst.setInt(4, conOrd);
            cst.setString(5, conNomRep);
            cst.setString(6, conUsuadd);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + conDes);
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

    public ParametrosSalida actualizar(Condicion objConComp) throws SQLException {
        int conkey = objConComp.getConKey();
        String conTipo = objConComp.getConTipo();
        int conEst = objConComp.getConEst();
        String conDes = objConComp.getConDes();
        int conDias = objConComp.getConDias();
        int conOrd = objConComp.getConOrd();
        String conNomRep = objConComp.getConNomRep();
        String conUsumod = objUsuCredential.getCuenta();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_CONDICION = "{call pack_tcondicion.p_actualizarCondicion(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CONDICION);
            cst.clearParameters();
            cst.setInt(1, conkey);
            cst.setString(2, conTipo);
            cst.setInt(3, conEst);
            cst.setString(4, conDes);
            cst.setInt(5, conDias);
            cst.setInt(6, conOrd);
            cst.setString(7, conNomRep);
            cst.setString(8, conUsumod);
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objConComp.getConId());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminar(Condicion objConComp) throws SQLException {
        int conId = objConComp.getConKey();
        String conTipo = objConComp.getConTipo();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_CONDICION = "{call pack_tcondicion.p_eliminarCondicion(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CONDICION);
            cst.clearParameters();
            cst.setInt(1, conId);
            cst.setString(2, conTipo);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + conId);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Condicion> lstCondicion(String s_tipo, int i_caso) throws SQLException {
        String SQL_CON_COMP;
        if (i_caso == 0) {
            SQL_CON_COMP = "select t.con_key,t.con_tipo,t.con_id, t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep, "
                    + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>"
                    + i_caso + " and t.con_tipo like '" + s_tipo + "' order by t.con_id";
        } else {
            SQL_CON_COMP = "select t.con_key,t.con_tipo,t.con_id, t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep, "
                    + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est="
                    + i_caso + " and t.con_tipo like '" + s_tipo + "' order by t.con_id";
        }
        P_WHERE = SQL_CON_COMP;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CON_COMP);
            objlstConComp = new ListModelList<Condicion>();
            while (rs.next()) {
                objConComp = new Condicion();
                objConComp.setConKey(rs.getInt("con_key"));
                objConComp.setConTipo(rs.getString("con_tipo"));
                objConComp.setConId(rs.getString("con_id"));
                objConComp.setConDes(rs.getString("con_des"));
                objConComp.setConEst(rs.getInt("con_est"));
                objConComp.setConDias(rs.getInt("con_dpla"));
                objConComp.setConOrd(rs.getInt("con_ord"));
                objConComp.setConNomRep(rs.getString("con_nomrep"));
                objConComp.setConUsuadd(rs.getString("con_usuadd"));
                objConComp.setConFecadd(rs.getTimestamp("con_fecadd"));
                objConComp.setConUsumod(rs.getString("con_usumod"));
                objConComp.setConFecmod(rs.getTimestamp("con_fecmod"));
                objlstConComp.add(objConComp);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstConComp.getSize() + " registro(s)");
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
        return objlstConComp;
    }

    public ListModelList<Condicion> lstCondicionProv() throws SQLException {
        String SQL_CON_PROV = "select t.con_key, t.con_id,t.con_des from tcondicion t "
                + "where t.con_est='1' and t.con_tipo='C'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CON_PROV);
            objlstConComp = new ListModelList<Condicion>();
            while (rs.next()) {
                objConComp = new Condicion();
                objConComp.setConKey(rs.getInt("con_key"));
                objConComp.setConId(rs.getString("con_id"));
                objConComp.setConDes(rs.getString("con_des"));
                objlstConComp.add(objConComp);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstConComp.getSize() + " registro(s)");
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
        return objlstConComp;
    }

    public ListModelList<Condicion> busqueda(int i_seleccion, String s_consulta, int i_estado, String s_tipo) throws SQLException {
        String SQL_BUSQUEDA = "";
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                        + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_tipo like '" + s_tipo + "' order by t.con_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_id like '" + s_consulta + "' and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_des like '" + s_consulta + "' and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_dpla like '" + s_consulta + "' and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                        + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_est=" + i_estado
                        + "and t.con_tipo like '" + s_tipo + "' order by t.con_id";

            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_id like '" + s_consulta + "' and t.con_est=" + i_estado
                            + "and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_des like '" + s_consulta + "' and t.con_est=" + i_estado
                            + "and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select t.con_key,t.con_tipo,t.con_id,t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep , "
                            + "t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est<>0 and t.con_dpla like '" + s_consulta + "' and t.con_est=" + i_estado
                            + "and t.con_tipo like '" + s_tipo + "' order by t.con_id";
                }
            }
        }
        P_WHERE = SQL_BUSQUEDA;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstConComp = new ListModelList<Condicion>();
            while (rs.next()) {
                objConComp = new Condicion();
                objConComp.setConKey(rs.getInt("con_key"));
                objConComp.setConTipo(rs.getString("con_tipo"));
                objConComp.setConId(rs.getString("con_id"));
                objConComp.setConDes(rs.getString("con_des"));
                objConComp.setConEst(rs.getInt("con_est"));
                objConComp.setConDias(rs.getInt("con_dpla"));
                objConComp.setConOrd(rs.getInt("con_ord"));
                objConComp.setConNomRep(rs.getString("con_nomrep"));
                objConComp.setConUsuadd(rs.getString("con_usuadd"));
                objConComp.setConFecadd(rs.getTimestamp("con_fecadd"));
                objConComp.setConUsumod(rs.getString("con_usumod"));
                objConComp.setConFecmod(rs.getTimestamp("con_fecmod"));
                objlstConComp.add(objConComp);
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
        return objlstConComp;
    }

    public Condicion buscarCondicion(int con_key, String tipo) throws SQLException {
        String SQL_CONDICION = "select * from tcondicion p where p.con_tipo='" + tipo + "' and p.con_key=" + con_key
                + " and p.con_est=1 order by p.con_id";
        objConComp = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONDICION);
            while (rs.next()) {
                objConComp = new Condicion();
                objConComp.setConKey(rs.getInt("con_key"));
                objConComp.setConTipo(rs.getString("con_tipo"));
                objConComp.setConId(rs.getString("con_id"));
                objConComp.setConDes(rs.getString("con_des"));
                objConComp.setConEst(rs.getInt("con_est"));
                objConComp.setConDias(rs.getInt("con_dpla"));
                objConComp.setConOrd(rs.getInt("con_ord"));
                objConComp.setConNomRep(rs.getString("con_nomrep"));
                objConComp.setConUsuadd(rs.getString("con_usuadd"));
                objConComp.setConFecadd(rs.getTimestamp("con_fecadd"));
                objConComp.setConUsumod(rs.getString("con_usumod"));
                objConComp.setConFecmod(rs.getTimestamp("con_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la condicion con Codigo" + con_key);
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
        return objConComp;
    }

    public Condicion busquedaCondicionProv(long prov_id) throws SQLException {
        String SQL_CON_PROV = "select t.con_key,t.con_des from tcondicion t, tproveedores p "
                + "where t.con_id=p.con_id and t.con_tipo='C' and p.prov_id=" + prov_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CON_PROV);
            while (rs.next()) {
                objConComp = new Condicion(rs.getInt("con_key"), rs.getString("con_des"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado la Condicion para el Proveedor con codigo " + prov_id);
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
        return objConComp;
    }

    public Condicion condicionCompra(String s_codcon) throws SQLException {
        String SQL_CON_COMP = " select t.con_key,t.con_tipo,t.con_id, t.con_des,t.con_est,t.con_dpla,t.con_ord,t.con_nomrep, t.con_usuadd, t.con_fecadd, t.con_usumod, t.con_fecmod from tcondicion t where t.con_est='1' and t.con_tipo = 'C' and t.con_id ='" + s_codcon + "' order by t.con_ord,t.con_id ";
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CON_COMP);
            while (rs.next()) {
                objConComp = new Condicion();
                objConComp.setConKey(rs.getInt("con_key"));
                objConComp.setConTipo(rs.getString("con_tipo"));
                objConComp.setConId(rs.getString("con_id"));
                objConComp.setConDes(rs.getString("con_des"));
                objConComp.setConEst(rs.getInt("con_est"));
                objConComp.setConDias(rs.getInt("con_dpla"));
                objConComp.setConOrd(rs.getInt("con_ord"));
                objConComp.setConNomRep(rs.getString("con_nomrep"));
                objConComp.setConUsuadd(rs.getString("con_usuadd"));
                objConComp.setConFecadd(rs.getTimestamp("con_fecadd"));
                objConComp.setConUsumod(rs.getString("con_usumod"));
                objConComp.setConFecmod(rs.getTimestamp("con_fecmod"));
            }
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
        return objConComp;
    }
    
    public String validaMovimientos(Condicion objConComp) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tcondicion.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objConComp.getConKey());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la condicion " + objConComp.getConKey());
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
