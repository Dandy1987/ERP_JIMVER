/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoBancos {

    ListModelList<Bancos> objlstBancos;
    Bancos objBancos;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    public static String P_WHERE = "";

    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoAlmacenes.class);

    public ListModelList<Bancos> lstBancos(int estado) throws SQLException {
        String sql_query;
        sql_query = "select * from codijisa.tbancos t where t.ban_estado=" + estado + " order by t.ban_key";
        P_WHERE = sql_query;
        
        objlstBancos = new ListModelList<Bancos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setKey(rs.getInt("ban_key"));
                objBancos.setId(rs.getString("ban_id"));
                objBancos.setDescripcion(rs.getString("ban_des"));
                objBancos.setSiglas(rs.getString("ban_sigla"));
                objBancos.setEstado(rs.getInt("ban_estado"));
                objBancos.setNumeracion(rs.getInt("ban_numeracion"));
                objBancos.setUsu_add(rs.getString("ban_usuadd"));
                objBancos.setUsu_mod(rs.getString("ban_usumod"));
                objBancos.setDia_add(rs.getTimestamp("ban_fecadd"));
                objBancos.setDia_mod(rs.getTimestamp("ban_fecmod"));

                objlstBancos.add(objBancos);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstBancos.getSize() + " registro(s)");
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
        return objlstBancos;
    }

    public String insertar(Bancos objBancos) throws SQLException {
        String descripcion = objBancos.getDescripcion();
        String siglas = objBancos.getSiglas();
        int numeracion = objBancos.getNumeracion();
        String sql_insertar = "{call pack_tbancos.p_insertar(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, descripcion);
            cst.setString(4, siglas);
            cst.setInt(5, numeracion);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.setString(7, objUsuCredential.getComputerName());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + descripcion);
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

    public String actualizar(Bancos objBancos) throws SQLException {
        String key = objBancos.getId();
        int estado = objBancos.getEstado();
        String descripcion = objBancos.getDescripcion();
        String siglas = objBancos.getSiglas();
        int numeracion = objBancos.getNumeracion();
        String sql_actualizar = "{call pack_tbancos.p_modificar(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
            cst.setString(1, key);
            cst.setInt(2, estado);
            cst.setString(3, descripcion);
            cst.setString(4, siglas);
            cst.setInt(5, numeracion);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.setString(7, objUsuCredential.getComputerName()); 
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objBancos.getId());
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

    public String eliminar(Bancos objBancos) throws SQLException {
        String key = objBancos.getId();
        String sql_eliminar = "{call pack_tbancos.p_eliminar(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setString(1, key);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objBancos.getId());
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

    public ListModelList<Bancos> consultar(int selec, String consulta, int est) throws SQLException {
        String sql = "";
        String es;
        if (est == 3) {
            es = "b.ban_estado in ('1','2')";
        } else if (est == 1) {
            es = "b.ban_estado ='1'";
        } else {
            es = "b.ban_estado ='2'";
        }

        if (selec == 0) {
            sql = " select * from tbancos b where "
                    + es
                    + " order by b.ban_id";
        } else if (selec == 1) {
            sql = " select * from tbancos b where "
                    + es
                    + " and b.ban_id like '" + consulta + "'"
                    + " order by b.ban_id";

        } else if (selec == 2) {
            sql = " select * from tbancos b where "
                    + es
                    + " and b.ban_des like '" + consulta + "'"
                    + " order by b.ban_id";
        }

        objlstBancos = new ListModelList<Bancos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setDescripcion(rs.getString("ban_des"));
                objBancos.setEstado(rs.getInt("ban_estado"));
                objBancos.setId(rs.getString("ban_id"));
                objBancos.setNumeracion(rs.getInt("ban_numeracion"));
                objBancos.setSiglas(rs.getString("ban_sigla"));
                objBancos.setUsu_add(rs.getString("ban_usuadd"));
                objBancos.setUsu_mod(rs.getString("ban_usumod"));
                objBancos.setDia_add(rs.getTimestamp("ban_fecadd"));
                objBancos.setDia_mod(rs.getTimestamp("ban_fecmod"));
                objlstBancos.add(objBancos);
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
        return objlstBancos;
    }
}
