/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author wcamacho
 */
public class DaoPlanilla {

    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    private int i_flagErrorBD = 0;
    private String s_msg = "";

    /**
     * Metodo que invoca al procedimiento "pack_tcalculoplla.p_calculo", y
     * realiza el calculo de la planilla en proceso
     *
     * @param cod_empresa Codigo de la empresa
     * @param cod_sucursal Codigo de la sucursal
     * @param cod_periodo Periodo en proceso a calcular
     * @return Objeto de la clase
     * @throws SQLException ParametrosSalida con valores retornados del
     * procedimiento
     */
    public ParametrosSalida calcular(int cod_empresa, String cod_sucursal, String cod_periodo) throws SQLException {
        String query = "{call pack_tcalculoplla.p_calculo(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, cod_empresa); // Codigo de Empresa
            cst.setString(2, cod_sucursal); // Codgio de Sucursal 
            cst.setString(3, cod_periodo); // Periodo en proceso
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(4);
            s_msg = cst.getString(5);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public String robotBuscarSueldo( int empresa,String sucursal,String periodo) throws SQLException {
        String codigo = "";
        String sql;
        try {
            con = new ConectaBD().conectar();
            sql = "{?= call pack_tcalculoplla.f_robot_sbasico(?,?,?)}";
            cst = con.prepareCall(sql);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, empresa);
            cst.setString(3, periodo);
            cst.setString(4, sucursal);
            cst.execute();
            codigo = cst.getString(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return codigo;
    }
	
    /**
     * Metodo que permite eliminar el calculo de la planilla
     *
     * @param cod_empresa codigo de la empresa
     * @param cod_periodo periodo a eliminar
     * @return Objeto con variables de respuesta
     * @throws SQLException
     */
    public ParametrosSalida eliminar(int cod_empresa, String cod_periodo) throws SQLException {
        String query = "{call pack_tcalculoplla.p_eliminar_calculo(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, cod_empresa); // Codigo de Empresa            
            cst.setString(2, cod_periodo); // Periodo en proceso
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(3);
            s_msg = cst.getString(4);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
	
    /**
     * Metodo que invoca al procedimiento "pack_tcalculoplla.p_calculoPersonal",
     * y realiza el calculo de la planilla en proceso
     *
     * @param cod_empresa Codigo de la empresa
     * @param cod_sucursal Codigo de la sucursal
     * @param cod_periodo Periodo en proceso a calcular
     * @return Objeto de la clase
     * @throws SQLException ParametrosSalida con valores retornados del
     * procedimiento
     */
    public ParametrosSalida calcularPorPersonal(int cod_empresa, String cod_sucursal, String cod_periodo,String personal) throws SQLException {

        String query = "{call pack_tcalculoplla.p_calculoXpersonal(?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, cod_empresa); // Codigo de Empresa
            cst.setString(2, cod_sucursal); // Codgio de Sucursal 
            cst.setString(3, cod_periodo); // Periodo en proceso
            cst.setString(4, personal);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(5);
            s_msg = cst.getString(6);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
	
}