package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Marca;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoMarca {

    //Instancia de Objetos
    ListModelList<Marca> objLisMarca;
    Marca objMarca;
    //Variables Publicas
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
    private static final Logger LOGGER = Logger.getLogger(DaoMarca.class);

    //Eventos Primarios - Transaccionales
    public String insertarMarca(Marca objMarca) throws SQLException {
        String s_tabsubdes = objMarca.getTab_subdes();
        String s_tabnomrep = objMarca.getTab_nomrep();
        int i_tab_ord = objMarca.getTab_ord();
        String SQL_INSERTAR_MARCA = "{call pack_ttabgen.p_031_insertarMarcaTrans(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MARCA);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tab_ord);
            cst.setString(4, objMarca.getTab_usuadd());
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

    public String actualizarMarca(Marca objMarca) throws SQLException {
        int i_tabid = objMarca.getTab_id();
        String s_tabsubdes = objMarca.getTab_subdes();
        String s_tabnomrep = objMarca.getTab_nomrep();
        int i_tabord = objMarca.getTab_ord();
        int i_tabest = objMarca.getTab_est();
        String SQL_ACTUALIZAR_MARCA = "{call pack_ttabgen.p_031_actualizarMarcaTrans(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_MARCA);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
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
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tabid);
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

    public String eliminarMarca(Marca objMarca) throws SQLException {
        int i_tabid = objMarca.getTab_id();
        String SQL_ELIMINAR_MARCA = "{call pack_ttabgen.p_031_eliminarMarcaTrans(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_MARCA);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tabid);
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
    public ListModelList<Marca> listaMarca(int caso) throws SQLException {
        String SQL_MARCA;
        if (caso == 0) {
            SQL_MARCA = "select t.tab_id,t.tab_subdes, t.tab_est, t.tab_nomrep, t.tab_ord,t.tab_usuadd, t.tab_fecadd, t.tab_usumod, t.tab_fecmod "
                    + "from ttabgen t where t.tab_cod=31 and t.tab_id<>0 and tab_est<>" + caso + " order by t.tab_id";
        } else {
            SQL_MARCA = "select t.tab_id,t.tab_subdes, t.tab_est, t.tab_nomrep, t.tab_ord, t.tab_usuadd, t.tab_fecadd, t.tab_usumod, t.tab_fecmod "
                    + "from ttabgen t where t.tab_cod=31 and t.tab_id<>0 and tab_est=" + caso + " order by t.tab_id";
        }
        P_WHERE = SQL_MARCA;
        try {
            objLisMarca = new ListModelList<Marca>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MARCA);
            while (rs.next()) {
                objMarca = new Marca();
                objMarca.setTab_id(rs.getInt("tab_id"));
                objMarca.setTab_subdes(rs.getString("tab_subdes"));
                objMarca.setTab_est(rs.getInt("tab_est"));
                objMarca.setTab_ord(rs.getInt("tab_ord"));
                objMarca.setTab_nomrep(rs.getString("tab_nomrep"));
                objMarca.setTab_usuadd(rs.getString("tab_usuadd"));
                objMarca.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objMarca.setTab_usumod(rs.getString("tab_usumod"));
                objMarca.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objLisMarca.add(objMarca);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisMarca.getSize() + " registro(s)");
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
        return objLisMarca;
    }

    public ListModelList<Marca> busquedaMarca(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_MARCA;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0 and p.tab_est=" + i_estado + "  order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_MARCA = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=31 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        }
        P_WHERE = SQL_MARCA;
        try {
            objLisMarca = new ListModelList<Marca>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MARCA);
            while (rs.next()) {
                objMarca = new Marca();
                objMarca.setTab_id(rs.getInt("tab_id"));
                objMarca.setTab_subdes(rs.getString("tab_subdes"));
                objMarca.setTab_est(rs.getInt("tab_est"));
                objMarca.setTab_ord(rs.getInt("tab_ord"));
                objMarca.setTab_nomrep(rs.getString("tab_nomrep"));
                objMarca.setTab_usuadd(rs.getString("tab_usuadd"));
                objMarca.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objMarca.setTab_usumod(rs.getString("tab_usumod"));
                objMarca.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objLisMarca.add(objMarca);
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
        return objLisMarca;
    }

}
