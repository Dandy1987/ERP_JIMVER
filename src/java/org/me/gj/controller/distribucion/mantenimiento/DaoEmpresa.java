package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.EmpSeguro;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoEmpresa {

    //Instancia de Objetos
    ListModelList<EmpSeguro> objlstEmpresas;
    EmpSeguro objEmpresa;
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
    private static final Logger LOGGER = Logger.getLogger(DaoEmpresa.class);

    //Eventos Primarios - Transaccionales
    public String insertarEmpresa(EmpSeguro objEmpresa) throws SQLException {
        String s_tabsubdes = objEmpresa.getTab_subdes();
        String s_tabnomrep = objEmpresa.getTab_nomrep();
        int i_tabord = objEmpresa.getTab_ord();
        String SQL_INSERTAR_EMPRESAS = "{call pack_ttabgen.p_037_insertarEmpAseguradora(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_EMPRESAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.setString(4, objEmpresa.getTab_usuadd());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tabsubdes);
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

    public String actualizarEmpresa(EmpSeguro objEmpresa) throws SQLException {
        int s_tabid = objEmpresa.getTab_id();
        String s_tabsubdes = objEmpresa.getTab_subdes();
        String s_tabnomrep = objEmpresa.getTab_nomrep();
        int i_tabord = objEmpresa.getTab_ord();
        int i_tabest = objEmpresa.getTab_est();
        String SQL_ACTUALIZAR_EMPRESAS = "{call pack_ttabgen.p_037_actualizarEmpAseguradora(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_EMPRESAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + s_tabid);
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

    public String eliminarEmpresa(EmpSeguro objEmpresa) throws SQLException {
        int s_tabid = objEmpresa.getTab_id();
        String SQL_ELIMINAR_EMPRESAS = "{call pack_ttabgen.p_037_eliminarEmpAseguradora(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_EMPRESAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + s_tabid);
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
    public ListModelList<EmpSeguro> listaEmpresa(int i_caso) throws SQLException {
        String SQL_EMPRESAS;
        if (i_caso == 0) {
            SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=37 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=37 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_EMPRESAS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESAS);
            objlstEmpresas = new ListModelList<EmpSeguro>();
            while (rs.next()) {
                objEmpresa = new EmpSeguro();
                objEmpresa.setTab_id(rs.getInt("tab_id"));
                objEmpresa.setTab_subdes(rs.getString("tab_subdes"));
                objEmpresa.setTab_est(rs.getInt("tab_est"));
                objEmpresa.setTab_ord(rs.getInt("tab_ord"));
                objEmpresa.setTab_nomrep(rs.getString("tab_nomrep"));
                objEmpresa.setTab_usuadd(rs.getString("tab_usuadd"));
                objEmpresa.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objEmpresa.setTab_usumod(rs.getString("tab_usumod"));
                objEmpresa.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstEmpresas.add(objEmpresa);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstEmpresas.getSize() + " registro(s)");
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
        return objlstEmpresas;
    }

    public ListModelList<EmpSeguro> busquedaEmpresa(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_EMPRESAS;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0 and p.tab_est=" + i_estado + "  order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_EMPRESAS = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=37 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        }
        P_WHERE = SQL_EMPRESAS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESAS);
            objlstEmpresas = new ListModelList<EmpSeguro>();
            while (rs.next()) {
                objEmpresa = new EmpSeguro();
                objEmpresa.setTab_id(rs.getInt("tab_id"));
                objEmpresa.setTab_subdes(rs.getString("tab_subdes"));
                objEmpresa.setTab_est(rs.getInt("tab_est"));
                objEmpresa.setTab_ord(rs.getInt("tab_ord"));
                objEmpresa.setTab_nomrep(rs.getString("tab_nomrep"));
                objEmpresa.setTab_usuadd(rs.getString("tab_usuadd"));
                objEmpresa.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objEmpresa.setTab_usumod(rs.getString("tab_usumod"));
                objEmpresa.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstEmpresas.add(objEmpresa);
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
        return objlstEmpresas;
    }

}
