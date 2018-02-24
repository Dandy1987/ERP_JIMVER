package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Menus;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoMenu {

    //Instancias de Objetos
    ListModelList<Menus> objlstMenus;
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
    private static final Logger LOGGER = Logger.getLogger(DaoMenu.class);

    //Eventos Primarios - Transaccionales
    public String insertarMenus(Menus objMenus, int tabla, String usuario) throws SQLException {
        String s_mendes = objMenus.getMen_des().toUpperCase();
        String s_men_usuadd = usuario.toUpperCase();
        int i_men_idmod = objMenus.getMen_idmod();
        int i_men_idmen = objMenus.getMen_idmen();
        int i_men_idsubmen1 = objMenus.getMen_idsubmen1();
        int i_men_idsubmen2 = objMenus.getMen_idsubmen2();
        String SQL_INSERT_MENU;
        try {
            con = new ConectaBD().conectar();
            if (tabla == 1) {//MODULO
                SQL_INSERT_MENU = "{call pack_tmenu.p_001_insertarModulo(?,?,?,?)}";
                cst = con.prepareCall(SQL_INSERT_MENU);
                cst.clearParameters();
                cst.setString(1, s_mendes);
                cst.setString(2, s_men_usuadd);
                cst.registerOutParameter(3, java.sql.Types.NUMERIC);
                cst.registerOutParameter(4, java.sql.Types.VARCHAR);
                cst.execute();
                s_msg = cst.getString(4);
                i_flagErrorBD = cst.getInt(3);
            } else if (tabla == 2) {//MENU
                SQL_INSERT_MENU = "{call pack_tmenu.p_002_insertarMenu(?,?,?,?,?)}";
                cst = con.prepareCall(SQL_INSERT_MENU);
                cst.clearParameters();
                cst.setString(1, s_mendes);
                cst.setString(2, s_men_usuadd);
                cst.setInt(3, i_men_idmod);
                cst.registerOutParameter(4, java.sql.Types.NUMERIC);
                cst.registerOutParameter(5, java.sql.Types.VARCHAR);
                cst.execute();
                s_msg = cst.getString(5);
                i_flagErrorBD = cst.getInt(4);
            } else if (tabla == 3) {//SUBMENU1
                SQL_INSERT_MENU = "{call pack_tmenu.p_003_insertarSubMenu1(?,?,?,?,?,?)}";
                cst = con.prepareCall(SQL_INSERT_MENU);
                cst.clearParameters();
                cst.setString(1, s_mendes);
                cst.setString(2, s_men_usuadd);
                cst.setInt(3, i_men_idmod);
                cst.setInt(4, i_men_idmen);
                cst.registerOutParameter(5, java.sql.Types.NUMERIC);
                cst.registerOutParameter(6, java.sql.Types.VARCHAR);
                cst.execute();
                s_msg = cst.getString(6);
                i_flagErrorBD = cst.getInt(5);
            } else if (tabla == 4) {//SUBMENU2
                SQL_INSERT_MENU = "{call pack_tmenu.p_004_insertarSubMenu2(?,?,?,?,?,?,?)}";
                cst = con.prepareCall(SQL_INSERT_MENU);
                cst.clearParameters();
                cst.setString(1, s_mendes);
                cst.setString(2, s_men_usuadd);
                cst.setInt(3, i_men_idmod);
                cst.setInt(4, i_men_idmen);
                cst.setInt(5, i_men_idsubmen1);
                cst.registerOutParameter(6, java.sql.Types.NUMERIC);
                cst.registerOutParameter(7, java.sql.Types.VARCHAR);
                cst.execute();
                s_msg = cst.getString(7);
                i_flagErrorBD = cst.getInt(6);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_mendes);
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Menus> lstMenus() throws SQLException {
        String SQL_MENUS = "select * from v_menucascada t";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MENUS);
            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_tip(rs.getInt(1));
                objMenus.setMen_des(rs.getString(2));
                objMenus.setCod_menu(rs.getInt(3));
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

    public ListModelList<Menus> listaModulos() throws SQLException {
        String sql = "select t.men_id,t.men_des from tmenu t where t.men_tip='1'";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
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

    public ListModelList<Menus> listaMenus(int modulo) throws SQLException {
        String sql = "select t.men_id,t.men_des from tmenu t where t.men_tip='2' and t.men_idmod='" + modulo + "'";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
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

    public ListModelList<Menus> listaSubMenus(String modulo, String menu) throws SQLException {
        String sql = "select distinct t.codigo,t.nombre from v_menu t where "
                + "t.MODULO='" + modulo + "' and t.MENU='" + menu + "' "
                + "and t.SUBMENU='-' order by t.CODIGO";
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
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

    public ListModelList<Menus> busquedaMenus(int i_seleccion, String s_consulta) throws SQLException {
        String SQL_MENU;
        if (i_seleccion == 0) {
            SQL_MENU = "select * from v_menucascada t";
        } else if (i_seleccion == 1) {
            SQL_MENU = "select * from v_menucascada t where t.men_tip='1' and men_des like '" + s_consulta + "'";
        } else if (i_seleccion == 2) {
            SQL_MENU = "select * from v_menucascada t where t.men_tip='2' and men_des like '" + s_consulta + "'";
        } else if (i_seleccion == 3) {
            SQL_MENU = "select * from v_menucascada t where t.men_tip='3' and men_des like '" + s_consulta + "'";
        } else {
            SQL_MENU = "select * from v_menucascada t where t.men_tip='4' and men_des like '" + s_consulta + "'";
        }
        objlstMenus = new ListModelList<Menus>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MENU);
            while (rs.next()) {
                objMenus = new Menus();
                objMenus.setMen_tip(rs.getInt("men_tip"));
                objMenus.setMen_des(rs.getString("men_des"));
                objMenus.setCod_menu(rs.getInt("node_data"));
                objlstMenus.add(objMenus);
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
        return objlstMenus;
    }

}
