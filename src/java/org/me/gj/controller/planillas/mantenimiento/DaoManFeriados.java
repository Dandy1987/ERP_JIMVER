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
import java.text.SimpleDateFormat;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.Feriados;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManPresPer;
import org.me.gj.model.planillas.mantenimiento.ManPresPerDet;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoManFeriados {

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    ArrayDescriptor arrayC, arrayCM;
    ARRAY arrC, arrCM;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManPresPer.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ListModelList<Feriados> objListFeriados;
    Feriados objFeriados;

    public String insertarFeriado(Feriados objFeriados) throws SQLException {

        String SQL = "{call pack_tferiados.p_insertarFeriados(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL);

            cst.clearParameters();

            cst.setString(1, objFeriados.getS_anho());
            cst.setDate(2, convertJavaDateToSqlDate(objFeriados.getD_dia()));
            cst.setString(3, objFeriados.getS_dianum());
            cst.setString(4, objFeriados.getS_diasemana());
            cst.setString(5, objFeriados.getS_mes());
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);

            cst.execute();
            s_msg = cst.getString(7);

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

    public ListModelList<Feriados> listarFeriados(String s_anho, String s_mes) throws SQLException {

        String sql;

        objListFeriados = new ListModelList<Feriados>();
        sql = "{call codijisa.pack_tferiados.p_lisFeriados(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql);
            cst.setString(1, s_anho);
            cst.setString(2, s_mes);
            cst.registerOutParameter(3, OracleTypes.CURSOR);

            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(3);
            st = con.createStatement();
            while (rs.next()) {
                objFeriados = new Feriados();
                objFeriados.setS_anho(rs.getString("FER_ANHO"));
                objFeriados.setS_mes(rs.getString("FER_MES"));
                objFeriados.setS_dianum(rs.getString("FER_DIA"));
                objFeriados.setD_dia(rs.getDate("FER_FECHA"));
                objFeriados.setS_diasemana(rs.getString("FER_DIASEMANA"));
                objFeriados.setS_dia(sdf.format(objFeriados.getD_dia()));
                objListFeriados.add(objFeriados);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListFeriados.getSize() + " registros(s)");
        } catch (SQLException e) {
            Messagebox.show(" Error de Búsqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objListFeriados;
    }
    
       public String eliminarFeriado(Feriados objFeriados) throws SQLException {

        String SQL = "{call pack_tferiados.p_eliminarFeriado(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL);

            cst.clearParameters();

            cst.setString(1, objFeriados.getS_anho());
            cst.setDate(2, convertJavaDateToSqlDate(objFeriados.getD_dia()));
            cst.setString(3, objUsuCredential.getCuenta());

            cst.registerOutParameter(4, java.sql.Types.VARCHAR);

            cst.execute();
            s_msg = cst.getString(4);

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


    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
