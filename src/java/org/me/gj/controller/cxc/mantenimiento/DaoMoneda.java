package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoMoneda {

    //Instancia de Objetos
    ListModelList<Moneda> objlstMoneda;
    Moneda objMoneda;
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
    private static Logger LOGGER = Logger.getLogger(DaoMoneda.class);

    //Eventos Primarios - Transaccionales
    public String insertarMoneda(Moneda monedas) throws SQLException {
        String s_desMon = monedas.getTab_subdes();
        int i_ordMon = monedas.getTab_ord();
        String s_ordNomRep = monedas.getTab_nomrep();
        String s_codSunat = monedas.getTab_cod_sunat();
        String SQL_INSERTAR_MONEDA = "{call pack_ttabgen.p_024_insertarMonedas(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MONEDA);
            cst.clearParameters();
            cst.setString(1, s_desMon);
            cst.setInt(2, i_ordMon);
            cst.setString(3, s_ordNomRep);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.setString(7,s_codSunat);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_desMon);
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

    public String actualizarMoneda(Moneda monedas) throws SQLException {
        int i_idMon = monedas.getTab_id();
        String s_desMon = monedas.getTab_subdes();
        int i_ordMon = monedas.getTab_ord();
        int i_estMon = monedas.getTab_est();
        String s_ordNomRep = monedas.getTab_nomrep();
        String s_codSunat = monedas.getTab_cod_sunat();
        String SQL_ACTUALIZAR_MONEDA = "{call pack_ttabgen.p_024_actualizarMonedas(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_MONEDA);
            cst.clearParameters();
            cst.setInt(1, i_idMon);
            cst.setString(2, s_desMon);
            cst.setInt(3, i_ordMon);
            cst.setInt(4, i_estMon);
            cst.setString(5, s_ordNomRep);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.setString(9, s_codSunat);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_idMon);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }

        return s_msg;
    }

    public String eliminarMoneda(Moneda monedas) throws SQLException {
        int i_idMon = monedas.getTab_id();
        String SQL_ELIMINAR_MONEDA = "{call pack_ttabgen.p_024_eliminarMonedas(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_MONEDA);
            cst.clearParameters();
            cst.setInt(1, i_idMon);
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_idMon);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }

        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Moneda> listaMonedas(int i_caso) throws SQLException {
        String SQL_MONEDAS = "";
        if (i_caso == 0) {
            SQL_MONEDAS = "select t.tab_id,t.tab_subdes,t.tab_subdir, t.tab_ord,t.tab_nomrep,t.tab_est, t.tab_usuadd, t.tab_fecadd, t.tab_usumod, t.tab_fecmod,t.tab_cod_sunat  from codijisa.ttabgen t where t.tab_cod='24' and t.tab_est <> " + i_caso + " order by t.tab_id";
        } else {
            SQL_MONEDAS = "select t.tab_id,t.tab_subdes,t.tab_subdir, t.tab_ord,t.tab_nomrep,t.tab_est, t.tab_usuadd, t.tab_fecadd, t.tab_usumod, t.tab_fecmod,t.tab_cod_sunat  from codijisa.ttabgen t where t.tab_cod='24' and t.tab_est=" + i_caso + " order by t.tab_id";
        }
        P_WHERE = SQL_MONEDAS;
        objlstMoneda = new ListModelList<Moneda>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MONEDAS);
            while (rs.next()) {
                objMoneda = new Moneda();
                objMoneda.setTab_id(rs.getInt("tab_id"));
                objMoneda.setTab_subdes(rs.getString("tab_subdes"));
                objMoneda.setTab_subdir(rs.getString("tab_subdir"));
                objMoneda.setTab_est(rs.getInt("tab_est"));
                objMoneda.setTab_ord(rs.getInt("tab_ord"));
                objMoneda.setTab_nomrep(rs.getString("tab_nomrep"));
                objMoneda.setTab_usuadd(rs.getString("tab_usuadd"));
                objMoneda.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objMoneda.setTab_usumod(rs.getString("tab_usumod"));
                objMoneda.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objMoneda.setTab_cod_sunat(rs.getString("tab_cod_sunat"));
                objlstMoneda.add(objMoneda);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMoneda.getSize() + " registro(s)");
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
        return objlstMoneda;
    }

    public ListModelList<Moneda> busquedaMonedas(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_MONEDAS = "";
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_est <> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_subdes like '" + consulta + "'  order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_id like '" + consulta + "' and p.tab_est=" + i_estado + " order by p.tab_id";
            } else {
                SQL_MONEDAS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from codijisa.ttabgen p "
                        + " where p.tab_cod=24 and p.tab_id<>0 and p.tab_subdes like '" + consulta + "' and p.tab_est=" + i_estado + " order by p.tab_id";
            }
        }
        P_WHERE = SQL_MONEDAS;
        objlstMoneda = new ListModelList<Moneda>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MONEDAS);
            while (rs.next()) {
                objMoneda = new Moneda();
                objMoneda.setTab_id(rs.getInt("tab_id"));
                objMoneda.setTab_subdes(rs.getString("tab_subdes"));
                objMoneda.setTab_subdir(rs.getString("tab_subdir"));
                objMoneda.setTab_est(rs.getInt("tab_est"));
                objMoneda.setTab_ord(rs.getInt("tab_ord"));
                objMoneda.setTab_nomrep(rs.getString("tab_nomrep"));
                objMoneda.setTab_usuadd(rs.getString("tab_usuadd"));
                objMoneda.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objMoneda.setTab_usumod(rs.getString("tab_usumod"));
                objMoneda.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstMoneda.add(objMoneda);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMoneda.getSize() + " registro(s)");
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

        return objlstMoneda;
    }

}
