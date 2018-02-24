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
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ConfPlanilla;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoConfPlanilla {

    ListModelList<ConfPlanilla> objlstConf;
    ConfPlanilla objConf;
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

    public String insertar(ConfPlanilla objConf) throws SQLException {
        String codigo = objConf.getCodigo();
        String constante = objConf.getConstante();
        String query = "{call pack_tplcfgplla.p_insertar(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setString(1, codigo);
            cst.setString(2, constante);
            cst.setString(3, objUsuCredential.getCuenta());
            cst.setString(4, objUsuCredential.getComputerName());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + codigo);
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

    public String actualizar(ConfPlanilla objConf) throws SQLException {
        String codigo = objConf.getCodigo();
        String constante = objConf.getConstante();
        int nro = objConf.getNroope();
        String query = "{call pack_tplcfgplla.p_modificar(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setString(1, codigo);
            cst.setString(2, constante);
            cst.setInt(3, nro);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.setString(5, objUsuCredential.getComputerName());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + codigo);
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

    public String eliminar(ConfPlanilla objconf) throws SQLException {
        String codigo = objConf.getCodigo();
        String constante = objConf.getConstante();
        int nro = objConf.getNroope();
        String query = "{call pack_tplcfgplla.p_eliminar(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setString(1, codigo);
            cst.setString(2, constante);
            cst.setInt(3, nro);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.setString(5, objUsuCredential.getComputerName());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + codigo);
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

    public ListModelList<ConfPlanilla> consultar(String codigo, int seleccion) throws SQLException {

        String query = "{call pack_tplcfgplla.p_listar(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.setString(1, codigo);
            cst.setInt(2, seleccion);
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(3);
            objlstConf = new ListModelList<ConfPlanilla>();
            while (rs.next()) {
                objConf = new ConfPlanilla();
                objConf.setCodigo(rs.getString("codigo"));
                objConf.setConstante(rs.getString("constante"));
                objConf.setDes_const(rs.getString("descripcion"));
                objConf.setNroope(rs.getInt("nro"));
                objConf.setUsu_add(rs.getString("tplcp_usuadd"));
                objConf.setFecha_add(rs.getTimestamp("tplcp_fecadd"));
                objConf.setUsu_mod(rs.getString("tplcp_usumod"));
                objConf.setFecha_mod(rs.getTimestamp("tplcp_fecmod"));
                objConf.setTipo(rs.getString("tipo"));
                objlstConf.add(objConf);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                rs.close();
                con.close();
            }
        }
        return objlstConf;
    }

    public int validaConstante(String constante,String codigo) throws SQLException {
        int dato = 0;
        String query;
        try {
            con = new ConectaBD().conectar();
            query = "{?=call codijisa.pack_tplcfgplla.f_existe(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, constante);
            cst.setString(3, codigo);
            cst.execute();

            dato = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();

            }
        }
        return dato;

    }
}
