package org.me.gj.controller.seguridad.mantenimiento;

import java.net.InetAddress;
import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;
import java.net.UnknownHostException;

public class DaoEmpresas {

    //Instancias de Objetos
    Empresas obj_empresas;
    ListModelList<Empresas> objlstEmpresas;
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
    private static Logger LOGGER = Logger.getLogger(DaoEmpresas.class);

    //Eventos Primarios - Transaccionales
    public String insertarEmpresa(Empresas objempresa) throws SQLException {
        String s_empdes = objempresa.getEmp_des();
        String s_empdir = objempresa.getEmp_dir();
        Long s_empruc = objempresa.getEmp_ruc();
        String s_empsig = objempresa.getEmp_sig();
        String s_empusuadd = objempresa.getEmp_usuadd();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERT_EMPRESA = "{call pack_tempresas.p_insertarEmpresa(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_EMPRESA);
            cst.clearParameters();
            cst.setString(1, s_empdes);
            cst.setString(2, s_empdir);
            cst.setLong(3, s_empruc);
            cst.setString(4, s_empsig);
            cst.setString(5, s_empusuadd);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_empdes);
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

    public String actualizarEmpresa(Empresas objempresa) throws SQLException {
        int i_empid = objempresa.getEmp_id();
        String s_empdes = objempresa.getEmp_des();
        String s_empdir = objempresa.getEmp_dir();
        Long s_empruc = objempresa.getEmp_ruc();
        String s_empsig = objempresa.getEmp_sig();
        int i_empest = objempresa.getEmp_est();
        String s_empusumod = objempresa.getEmp_usumod();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_UPDATE_EMPRESA = "{call pack_tempresas.p_actualizarEmpresa(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_EMPRESA);
            cst.clearParameters();
            cst.setInt(1, i_empid);
            cst.setString(2, s_empdes);
            cst.setString(3, s_empdir);
            cst.setLong(4, s_empruc);
            cst.setString(5, s_empsig);
            cst.setInt(6, i_empest);
            cst.setString(7, s_empusumod);
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_empid);
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

    public String eliminarEmpresa(Empresas objempresa) throws SQLException {
        int i_idEmp = objempresa.getEmp_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_DELETE_EMPRESA = "{call pack_tempresas.p_eliminarEmpresa(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_EMPRESA);
            cst.clearParameters();
            cst.setInt(1, i_idEmp);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_idEmp);
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
    public ListModelList<Empresas> lstEmpresas(int caso) throws SQLException {
        String SQL_EMPRESAS;
        if (caso == 0) {
            SQL_EMPRESAS = "select t.emp_id,t.emp_sig,t.emp_des,t.emp_dir,t.emp_ruc,t.emp_est, t.emp_usuadd,"
                    + "t.emp_fecadd,t.emp_usumod,t.emp_fecmod from tempresas t "
                    + "where t.emp_est<>" + caso
                    + "order by t.emp_id";
        } else {
            SQL_EMPRESAS = "select t.emp_id,t.emp_sig,t.emp_des,t.emp_dir,t.emp_ruc,t.emp_est, t.emp_usuadd,"
                    + "t.emp_fecadd,t.emp_usumod,t.emp_fecmod from tempresas t "
                    + "where t.emp_est=" + caso
                    + "order by t.emp_id";
        }
        objlstEmpresas = new ListModelList<Empresas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESAS);
            while (rs.next()) {
                obj_empresas = new Empresas();
                obj_empresas.setEmp_id(rs.getInt(1));
                obj_empresas.setEmp_sig(rs.getString(2));
                obj_empresas.setEmp_des(rs.getString(3));
                obj_empresas.setEmp_dir(rs.getString(4));
                obj_empresas.setEmp_ruc(rs.getLong(5));
                obj_empresas.setEmp_est(rs.getInt(6));
                obj_empresas.setEmp_usuadd(rs.getString(7));
                obj_empresas.setEmp_feccadd(rs.getDate(8));
                obj_empresas.setEmp_usumod(rs.getString(9));
                obj_empresas.setEmp_fecmod(rs.getDate(10));
                objlstEmpresas.add(obj_empresas);
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

    public ListModelList<Empresas> lstEmpresasLogin(int caso) throws SQLException, UnknownHostException {
        String SQL_EMPRESAS;
        String computerName;
        computerName = InetAddress.getLocalHost().getHostName();
        if (caso == 0) {
            SQL_EMPRESAS = "select t.emp_id,t.emp_sig,t.emp_des,t.emp_dir,t.emp_ruc,t.emp_est, t.emp_usuadd,"
                    + "t.emp_fecadd,t.emp_usumod,t.emp_fecmod from tempresas t "
                    + "where t.emp_est<>" + caso
                    + "order by t.emp_id";
        } else {
            SQL_EMPRESAS = "select t.emp_id,t.emp_sig,t.emp_des,t.emp_dir,t.emp_ruc,t.emp_est, t.emp_usuadd,"
                    + "t.emp_fecadd,t.emp_usumod,t.emp_fecmod from tempresas t "
                    + "where t.emp_est=" + caso
                    + "order by t.emp_id";
        }
        objlstEmpresas = new ListModelList<Empresas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESAS);
            while (rs.next()) {
                obj_empresas = new Empresas();
                obj_empresas.setEmp_id(rs.getInt(1));
                obj_empresas.setEmp_sig(rs.getString(2));
                obj_empresas.setEmp_des(rs.getString(3));
                obj_empresas.setEmp_dir(rs.getString(4));
                obj_empresas.setEmp_ruc(rs.getLong(5));
                obj_empresas.setEmp_est(rs.getInt(6));
                obj_empresas.setEmp_usuadd(rs.getString(7));
                obj_empresas.setEmp_feccadd(rs.getDate(8));
                obj_empresas.setEmp_usumod(rs.getString(9));
                obj_empresas.setEmp_fecmod(rs.getDate(10));
                objlstEmpresas.add(obj_empresas);
            }
            LOGGER.info("[" + computerName + "] | 'SIN USUARIO CONECTADO' | se cargo los datos y ha encontrado " + objlstEmpresas.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + computerName + "] | 'SIN USUARIO CONECTADO' | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + computerName + "] | 'SIN USUARIO CONECTADO' | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstEmpresas;
    }

    public ListModelList<Empresas> busquedaEmpresas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_EMPRESA;
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_EMPRESA = "select * from tempresas t where t.emp_est<> 0 order by t.emp_id";
            } else if (i_seleccion == 1) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est<> 0 and t.emp_id like '" + s_consulta + "' order by t.emp_id";
            } else if (i_seleccion == 2) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est<> 0 and t.emp_des like '" + s_consulta + "' order by t.emp_id";
            } else if (i_seleccion == 3) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est<> 0 and t.emp_sig like '" + s_consulta + "' order by t.emp_id";
            } else {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est<> 0 and t.emp_ruc like '" + s_consulta + "' order by t.emp_id";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_EMPRESA = "select * from tempresas t where t.emp_est=" + i_estado + "  order by t.emp_id";
            } else if (i_seleccion == 1) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est = " + i_estado + " and t.emp_id like '" + s_consulta + "'  order by t.emp_id";
            } else if (i_seleccion == 2) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est = " + i_estado + " and t.emp_des like '" + s_consulta + "'  order by t.emp_id";
            } else if (i_seleccion == 3) {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est = " + i_estado + " and t.emp_sig like '" + s_consulta + "'  order by t.emp_id";
            } else {
                SQL_EMPRESA = "select * from tempresas t where t.emp_est = " + i_estado + " and t.emp_ruc like '" + s_consulta + "'  order by t.emp_id";
            }
        }
        try {
            objlstEmpresas = new ListModelList<Empresas>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESA);
            while (rs.next()) {
                obj_empresas = new Empresas();
                obj_empresas.setEmp_id(rs.getInt("emp_id"));
                obj_empresas.setEmp_sig(rs.getString("emp_sig"));
                obj_empresas.setEmp_des(rs.getString("emp_des"));
                obj_empresas.setEmp_dir(rs.getString("emp_dir"));
                obj_empresas.setEmp_ruc(rs.getLong("emp_ruc"));
                obj_empresas.setEmp_est(rs.getInt("emp_est"));
                obj_empresas.setEmp_usuadd(rs.getString("emp_usuadd"));
                obj_empresas.setEmp_feccadd(rs.getDate("emp_fecadd"));
                obj_empresas.setEmp_usumod(rs.getString("emp_usumod"));
                obj_empresas.setEmp_fecmod(rs.getDate("emp_fecmod"));
                objlstEmpresas.add(obj_empresas);
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
