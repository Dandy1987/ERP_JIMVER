package org.me.gj.controller.seguridad.mantenimiento;

import java.net.InetAddress;
import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;
import java.net.UnknownHostException;

public class DaoSucursales {

    //Instancias de Objetos
    ListModelList<Sucursales> lista;
    Sucursales obj_sucursales;
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
    private static final Logger LOGGER = Logger.getLogger(DaoSucursales.class);

    //Eventos Primarios - Transaccionales
    public String insertarSucursal(Sucursales objSucursal) throws SQLException {
        String s_sucdir = objSucursal.getSuc_dir();
        String s_suclug = objSucursal.getSuc_des();
        int i_empid = objSucursal.getEmp_id();
        int i_sucord = objSucursal.getSuc_ord();
        long l_suctel = objSucursal.getSuc_telef();
        String s_sucfax = objSucursal.getSuc_fax();
        String s_usuadd = objSucursal.getSuc_usuadd();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERT_SUCURSALES = "{call pack_tsucursales.p_insertarSucursal(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_SUCURSALES);
            cst.clearParameters();
            cst.setString(1, s_sucdir);
            cst.setString(2, s_suclug);
            cst.setInt(3, i_empid);
            cst.setLong(4, l_suctel);
            cst.setString(5, s_sucfax);
            cst.setInt(6, i_sucord);
            cst.setString(7, s_usuadd);
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de sucursal para la empresa con descripción " + objSucursal.getEmp_sig());
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

    public String actualizarSucursal(Sucursales objSucursal) throws SQLException {
        int i_sucid = objSucursal.getSuc_id();
        int i_empid = objSucursal.getEmp_id();
        String s_sucdir = objSucursal.getSuc_dir();
        long l_suctel = objSucursal.getSuc_telef();
        String s_sucfax = objSucursal.getSuc_fax();
        String s_suclug = objSucursal.getSuc_des();
        int i_sucord = objSucursal.getSuc_ord();
        int i_sucest = objSucursal.getSuc_est();
        String s_usumod = objSucursal.getSuc_usumod();
        String SQL_UPDATE_SUCURSALES = "{call pack_tsucursales.p_actualizarSucursal(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_SUCURSALES);
            cst.clearParameters();
            cst.setInt(1, i_sucid);
            cst.setInt(2, i_empid);
            cst.setString(3, s_sucdir);
            cst.setLong(4, l_suctel);
            cst.setString(5, s_sucfax);
            cst.setString(6, s_suclug);
            cst.setInt(7, i_sucord);
            cst.setInt(8, i_sucest);
            cst.setString(9, s_usumod);
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro de sucursal para la emresa con decripción " + objSucursal.getEmp_sig());
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

    public String eliminarSucursal(Sucursales objSucursal) throws SQLException {
        int i_sucid = objSucursal.getSuc_id();
        int i_empid = objSucursal.getEmp_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_DELETE_SUCURSALES = "{call pack_tsucursales.p_eliminarSucursal(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_SUCURSALES);
            cst.clearParameters();
            cst.setInt(1, i_sucid);
            cst.setInt(2, i_empid);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + objUsuCredential.getCuenta() + " | elimino un registro de sucursal para la emresa con decripción " + objSucursal.getEmp_sig());
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
    public ListModelList<Sucursales> lstSucursales(String cod, int caso) throws SQLException {
        lista = new ListModelList<Sucursales>();
        String SQL_SUCURSALES;
        if (caso == 0) {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod, t.suc_telef, t.suc_fax from tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est<>" + caso
                    + " order by t.suc_ord,t.suc_id,p.emp_id";
        } else {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod ,t.suc_telef, t.suc_fax from tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est=" + caso + " and p.emp_est=1 and t.emp_id like '" + cod + "'"
                    + "order by t.suc_ord,t.suc_id,p.emp_id";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);
            while (rs.next()) {
                obj_sucursales = new Sucursales();
                obj_sucursales.setSuc_id(rs.getInt("suc_id"));
                obj_sucursales.setEmp_id(rs.getInt("emp_id"));
                obj_sucursales.setEmp_sig(rs.getString("emp_sig"));
                obj_sucursales.setSuc_des(rs.getString("suc_des"));
                obj_sucursales.setSuc_dir(rs.getString("suc_dir"));
                obj_sucursales.setSuc_est(rs.getInt("suc_est"));
                obj_sucursales.setSuc_ord(rs.getInt("suc_ord"));
                obj_sucursales.setSuc_telef(rs.getLong("suc_telef"));
                obj_sucursales.setSuc_fax(rs.getString("suc_fax"));
                obj_sucursales.setSuc_usuadd(rs.getString("suc_usuadd"));
                obj_sucursales.setSuc_fecadd(rs.getDate("suc_fecadd"));
                obj_sucursales.setSuc_usumod(rs.getString("suc_usumod"));
                obj_sucursales.setSuc_fecmod(rs.getDate("suc_fecmod"));
                lista.add(obj_sucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + lista.getSize() + " registro(s)");
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
        return lista;
    }

    public ListModelList<Sucursales> lstSucursalesLogin(String cod, int caso) throws SQLException, UnknownHostException {
        lista = new ListModelList<Sucursales>();
        String SQL_SUCURSALES;
        String computerName;
        computerName = InetAddress.getLocalHost().getHostName();
        if (caso == 0) {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod , t.suc_telef, t.suc_faxfrom tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est<>" + caso
                    + " order by t.suc_ord,t.suc_id ";
        } else {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod, t.suc_telef, t.suc_fax from tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est=" + caso + " and t.emp_id='" + cod
                    + "' order by t.suc_ord,t.suc_id ";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);
            while (rs.next()) {
                obj_sucursales = new Sucursales();
                obj_sucursales.setSuc_id(rs.getInt("suc_id"));
                obj_sucursales.setEmp_id(rs.getInt("emp_id"));
                obj_sucursales.setEmp_sig(rs.getString("emp_sig"));
                obj_sucursales.setSuc_des(rs.getString("suc_des"));
                obj_sucursales.setSuc_dir(rs.getString("suc_dir"));
                obj_sucursales.setSuc_est(rs.getInt("suc_est"));
                obj_sucursales.setSuc_ord(rs.getInt("suc_ord"));
                obj_sucursales.setSuc_telef(rs.getLong("suc_telef"));
                obj_sucursales.setSuc_fax(rs.getString("suc_fax"));
                obj_sucursales.setSuc_usuadd(rs.getString("suc_usuadd"));
                obj_sucursales.setSuc_fecadd(rs.getDate("suc_fecadd"));
                obj_sucursales.setSuc_usumod(rs.getString("suc_usumod"));
                obj_sucursales.setSuc_fecmod(rs.getDate("suc_fecmod"));
                lista.add(obj_sucursales);
            }
            LOGGER.info("[" + computerName + "] | 'SIN USUARIO CONECTADO' | ha cargado los datos y ha encontrado " + lista.getSize() + " registro(s)");
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
        return lista;
    }

    public ListModelList<Sucursales> busquedaSucursales(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_EMPRESA;
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est<> 0 and p.emp_est=1 order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 1) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est<> 0 and p.emp_est=1 and t.suc_id like '" + s_consulta + "' order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 2) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est<> 0 and p.emp_est=1 and t.suc_des like '" + s_consulta + "' order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 3) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est<> 0 and p.emp_est=1 and t.suc_dir like '" + s_consulta + "' order by t.suc_ord, t.suc_id,p.emp_id";
            } else {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est<> 0 and p.emp_est=1 and p.emp_sig like '" + s_consulta + "' order by t.suc_ord, t.suc_id,p.emp_id";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est=" + i_estado + " and p.emp_est=1  order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 1) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est = " + i_estado + " and p.emp_est=1 and t.suc_id like '" + s_consulta + "'  order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 2) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est = " + i_estado + " and p.emp_est=1 and t.suc_des like '" + s_consulta + "'  order by t.suc_ord, t.suc_id,p.emp_id";
            } else if (i_seleccion == 3) {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est = " + i_estado + " and p.emp_est=1 and t.suc_dir like '" + s_consulta + "'  order by t.suc_ord, t.suc_id,p.emp_id";
            } else {
                SQL_EMPRESA = "select * from tsucursales t, tempresas p where t.emp_id=p.emp_id and t.suc_est = " + i_estado + " and p.emp_est=1 and p.emp_sig like '" + s_consulta + "'  order by t.suc_ord, t.suc_id,p.emp_id";
            }
        }
        try {
            lista = new ListModelList<Sucursales>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESA);
            while (rs.next()) {
                obj_sucursales = new Sucursales();
                obj_sucursales.setSuc_id(rs.getInt("suc_id"));
                obj_sucursales.setEmp_id(rs.getInt("emp_id"));
                obj_sucursales.setEmp_sig(rs.getString("emp_sig"));
                obj_sucursales.setSuc_des(rs.getString("suc_des"));
                obj_sucursales.setSuc_dir(rs.getString("suc_dir"));
                obj_sucursales.setSuc_est(rs.getInt("suc_est"));
                obj_sucursales.setSuc_ord(rs.getInt("suc_ord"));
                obj_sucursales.setSuc_telef(rs.getLong("suc_telef"));
                obj_sucursales.setSuc_fax(rs.getString("suc_fax"));
                obj_sucursales.setSuc_usuadd(rs.getString("suc_usuadd"));
                obj_sucursales.setSuc_fecadd(rs.getDate("suc_fecadd"));
                obj_sucursales.setSuc_usumod(rs.getString("suc_usumod"));
                obj_sucursales.setSuc_fecmod(rs.getDate("suc_fecmod"));
                lista.add(obj_sucursales);
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
        return lista;
    }
}
