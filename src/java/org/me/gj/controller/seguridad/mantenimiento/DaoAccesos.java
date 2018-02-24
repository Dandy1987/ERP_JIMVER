package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Menus;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.mantenimiento.Usuarios;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoAccesos {

    //Instancias de Objetos    
    ListModelList<Accesos> objlstAccesos;
    ListModelList<Usuarios> objlstUsuarios;
    ListModelList<Empresas> objlstEmpresas;
    ListModelList<Sucursales> objlstSucursales;
    ListModelList<Menus> objlstMenus;
    Accesos objAccesos;
    Usuarios objUsuarios;
    Empresas objEmpresas;
    Sucursales objSucursales;
    Menus objMenus;
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
    private static final Logger LOGGER = Logger.getLogger(DaoAccesos.class);

    //Eventos Primarios - Transaccionales
    public String insertarAcceso(int i_seleccion, Accesos objAccesos) throws SQLException {
        int usu_id = objAccesos.getUsu_id();
        int men_id = objAccesos.getMen_id();
        int emp_id = objAccesos.getEmp_id();
        String acc_usuadd = objAccesos.getAcc_usuadd();
        int suc_id = objAccesos.getSuc_id();
        int men_idmod = objAccesos.getMen_idmod();
        int men_idmen = objAccesos.getMen_idmen();
        int men_idsubmen1 = objAccesos.getMen_idsubmen1();
        int men_idsubmen2 = objAccesos.getMen_idsubmen2();
        int acc_ins = objAccesos.getAcc_ins();
        int acc_mod = objAccesos.getAcc_mod();
        int acc_eli = objAccesos.getAcc_eli();
        int acc_imp = objAccesos.getAcc_imp();

        String SQL_INSERT_ACCESOS;
        SQL_INSERT_ACCESOS = "{call pack_taccesos.p_insertarAcceso(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_ACCESOS);
            cst.clearParameters();
            cst.setInt(1, i_seleccion);
            cst.setInt(2, usu_id);
            cst.setInt(3, men_id);
            cst.setInt(4, emp_id);
            cst.setString(5, acc_usuadd);
            cst.setInt(6, suc_id);
            cst.setInt(7, men_idmod);
            cst.setInt(8, men_idmen);
            cst.setInt(9, men_idsubmen1);
            cst.setInt(10, men_idsubmen2);
            cst.setInt(11, acc_ins);
            cst.setInt(12, acc_mod);
            cst.setInt(13, acc_eli);
            cst.setInt(14, acc_imp);
            cst.registerOutParameter(15, java.sql.Types.NUMERIC);
            cst.registerOutParameter(16, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(16);
            i_flagErrorBD = cst.getInt(15);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de acceso para el usuario :  " + usu_id);
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

    public String eliminarAcceso(int codigo, int empresa, int sucursal, int usuario) throws SQLException {
        String SQL_DELETE_ACCESO = "{call pack_taccesos.p_eliminarAcceso(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_ACCESO);
            cst.clearParameters();
            cst.setInt(1, codigo);
            cst.setInt(2, empresa);
            cst.setInt(3, sucursal);
            cst.setInt(4, usuario);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + codigo + "  del usuario : " + usuario);
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

    public ListModelList<Usuarios> lstUsuarios() throws SQLException {
        String SQL_USUARIOS = "select * from v_listausuarios t";
        objlstUsuarios = new ListModelList<Usuarios>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_USUARIOS);
            while (rs.next()) {
                objUsuarios = new Usuarios();
                objUsuarios.setUsu_id(rs.getInt(1));
                objUsuarios.setUsu_nick(rs.getString(2));
                objUsuarios.setUsu_area(rs.getString(3));
                objlstUsuarios.add(objUsuarios);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUsuarios.getSize() + " registro(s)");
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
        return objlstUsuarios;
    }

    public ListModelList<Empresas> lstEmpresas() throws SQLException {
        String SQL_EMPRESAS = "select * from v_listaempresas t";
        objlstEmpresas = new ListModelList<Empresas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPRESAS);
            while (rs.next()) {
                objEmpresas = new Empresas();
                objEmpresas.setEmp_id(rs.getInt(1));
                objEmpresas.setEmp_sig(rs.getString(2));
                objlstEmpresas.add(objEmpresas);
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

    public ListModelList<Sucursales> lstSucursales(int cod_emp) throws SQLException {
        String SQL_SUCURSALES = "select * from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstSucursales = new ListModelList<Sucursales>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            while (rs.next()) {
                objSucursales = new Sucursales();
                objSucursales.setEmp_id(rs.getInt(1));
                objSucursales.setEmp_sig(rs.getString(2));
                objSucursales.setSuc_id(rs.getInt(3));
                objSucursales.setSuc_des(rs.getString(4));
                objlstSucursales.add(objSucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }

    public ListModelList<Menus> lstModulos() throws SQLException {
        String SQL_MODULOS = "select t.men_id, t.men_des from tmenu t where t.men_tip='1'";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MODULOS);

            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_id(rs.getInt(1));
                objMenus.setMen_des(rs.getString(2));
                objlstMenus.add(objMenus);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMenus.getSize() + " registro(s)");
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
        return objlstMenus;
    }

    public ListModelList<Menus> lstMenus(int modulo) throws SQLException {
        String SQL_MENUS = "select t.men_id, t.men_des from tmenu t where t.men_tip='2' and t.men_idmod='" + modulo + "'";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MENUS);
            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_id(rs.getInt(1));
                objMenus.setMen_des(rs.getString(2));
                objlstMenus.add(objMenus);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMenus.getSize() + " registro(s)");
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
        return objlstMenus;
    }

    public ListModelList<Menus> lstSubmenu1(int mod, int men) throws SQLException {
        String SQL_SUBMENU1 = "select t.men_id, t.men_des from tmenu t where t.men_tip='3' and t.men_idmod=" + mod + " and t.men_idmen=" + men + "";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUBMENU1);

            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_id(rs.getInt(1));
                objMenus.setMen_des(rs.getString(2));
                objlstMenus.add(objMenus);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMenus.getSize() + " registro(s)");
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
        return objlstMenus;
    }

    public ListModelList<Menus> lstSubmenu2(int mod, int men, int submenu) throws SQLException {
        String SQL_SUBMENU2 = "select t.men_id, t.men_des from tmenu t where t.men_tip='4' and t.men_idmod='" + mod + "' "
                + "and t.men_idmen='" + men + "' and t.men_idsubmen1='" + submenu + "'";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUBMENU2);

            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_id(rs.getInt(1));
                objMenus.setMen_des(rs.getString(2));
                objlstMenus.add(objMenus);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMenus.getSize() + " registro(s)");
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
        return objlstMenus;
    }

    public ListModelList<Accesos> lstAccesos(int i_usuario, int i_empresa, int i_sucursal) throws SQLException {
        String SQL_ACCESOS = "SELECT T1.MEN_ID,DECODE(T1.MEN_TIP,1,'+ ' || T1.MEN_DES,2,'|___+ ' || T1.MEN_DES,3,'|_________+ ' || T1.MEN_DES,4,'|_____________+ ' || T1.MEN_DES) MEN_DES,T1.MEN_TIP,T.USU_ID,T.EMP_ID,T.SUC_ID FROM TACCESOS T ,TMENU T1 "
                + "WHERE T.USU_ID='" + i_usuario + "' AND T.EMP_ID='" + i_empresa + "' AND T.SUC_ID='" + i_sucursal + "' AND T.MEN_ID=T1.MEN_ID ORDER BY T1.MEN_ID";
        objlstAccesos = new ListModelList<Accesos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ACCESOS);
            while (rs.next()) {
                objAccesos = new Accesos();
                objAccesos.setMen_id(rs.getInt(1));
                objAccesos.setAcc_des(rs.getString(2));
                objlstAccesos.add(objAccesos);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAccesos.getSize() + " registro(s)");
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
        return objlstAccesos;
    }

    //Eventos Secundarios - Listas y validaciones
    public Accesos Verifica_Permisos_Transacciones(int codigo, int usuario, int empresa, int sucursal) throws SQLException {
        String SQL_PERMISOS = "select t.usu_id,t.emp_id,t.suc_id,t.acc_ins,t.acc_mod,t.acc_eli,t.acc_imp from taccesos t "
                + "where t.usu_id='" + usuario + "' and t.emp_id='" + empresa + "' and t.suc_id='" + sucursal + "' and t.men_id='" + codigo + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PERMISOS);

            while (rs.next()) {
                objAccesos = new Accesos();
                objAccesos.setUsu_id(rs.getInt(1));
                objAccesos.setEmp_id(rs.getInt(2));
                objAccesos.setSuc_id(rs.getInt(3));
                objAccesos.setAcc_ins(rs.getInt(4));
                objAccesos.setAcc_mod(rs.getInt(5));
                objAccesos.setAcc_eli(rs.getInt(6));
                objAccesos.setAcc_imp(rs.getInt(7));
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
        return objAccesos;
    }

    public boolean Verifica_Acceso(int codigo, int usuario, int empresa, int sucursal) throws SQLException {
        String SQL_VERIFICA = "SELECT count(*) FROM TACCESOS T ,TMENU T1 "
                + "WHERE T.USU_ID='" + usuario + "' AND T.EMP_ID='" + empresa + "' AND T.SUC_ID='" + sucursal + "' "
                + "and t.men_id = '" + codigo + "' "
                + "AND T.MEN_ID=T1.MEN_ID ORDER BY T1.MEN_ID";
        Boolean b_val = true;
        int i_val = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA);
            while (rs.next()) {
                i_val = rs.getInt(1);
            }
            if (i_val == 1) {
                b_val = false;
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
        return b_val;
    }
    
    public ListModelList<Sucursales> lstSucursales_union(int cod_emp) throws SQLException {
        String SQL_SUCURSALES = "select t.emp_id,t.emp_sig,t.suc_id,t.suc_des from v_listasucursales t where t.emp_id='" + cod_emp + "'"
                + " union"
                + " select t.emp_id,'',0,'' from v_listasucursales t where t.emp_id='" + cod_emp + "'";
                //+ " select t.emp_id,'',null,'' from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstSucursales = new ListModelList<Sucursales>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            while (rs.next()) {
                objSucursales = new Sucursales();
                objSucursales.setEmp_id(rs.getInt(1));
                objSucursales.setEmp_sig(rs.getString(2));
                objSucursales.setSuc_id(rs.getInt(3));
                objSucursales.setSuc_des(rs.getString(4));
                objlstSucursales.add(objSucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }

    public ListModelList<Sucursales> lstSucursalesPatron(String empresa) throws SQLException {
        String emp = empresa.isEmpty() ? "" :  " and t.emp_id in ('" + empresa + "')";
         String SQL_SUCURSALES = "select distinct t.suc_id,t.suc_des from tsucursales t where t.suc_est = 1 " + emp ;
              //  + " union"
              //  + " select t.emp_id,'',0,'' from v_listasucursales t where " + emp;
                //+ " select t.emp_id,'',null,'' from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstSucursales = new ListModelList<Sucursales>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            while (rs.next()) {
                objSucursales = new Sucursales();
                //objSucursales.setEmp_id(rs.getInt(1));
                //objSucursales.setEmp_sig(rs.getString(2));
                objSucursales.setSuc_id(rs.getInt(1));
                objSucursales.setSuc_des(rs.getString(2));
                objlstSucursales.add(objSucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }
 public ListModelList<Sucursales> lstEmpresasPatron() throws SQLException {
       // String emp = empresa.isEmpty() ? "" : " t.emp_id in ('" + empresa + "')";
         String SQL_SUCURSALES = "select t.emp_id,t.emp_sig from tempresas t where t.emp_est = 1" ;
              //  + " union"
              //  + " select t.emp_id,'',0,'' from v_listasucursales t where " + emp;
                //+ " select t.emp_id,'',null,'' from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstSucursales = new ListModelList<Sucursales>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            while (rs.next()) {
                objSucursales = new Sucursales();
                objSucursales.setEmp_id(rs.getInt(1));
                objSucursales.setEmp_sig(rs.getString(2));
               
                objlstSucursales.add(objSucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }
}