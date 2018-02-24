/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoMovimiento {

    ListModelList<InformesMovimiento> objlstMovimiento;
    InformesMovimiento objMovimiento;
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    int i_flagErrorBD = 0;
    String s_msg = "";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    public static String P_WHERE = "";


    /*
     * ***************************************
     * para informes Movimiento
     */
    public ListModelList<InformesMovimiento> busquedaConstante() throws SQLException {

        String query = "select t.tabla_id, t.tabla_descri"
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                // + " and t.tabla_tipo1 in ('C','M')"
                + " and t.tabla_estado=1"
                + " order by t.tabla_id";
        objlstMovimiento = new ListModelList<InformesMovimiento>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setCod_constante(rs.getString("tabla_id"));
                objMovimiento.setDes_constante(rs.getString("tabla_descri"));
                objlstMovimiento.add(objMovimiento);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMovimiento;

    }

    public ListModelList<InformesMovimiento> buscarSeleccion(String consulta) throws SQLException {

        String query = " select t.tabla_id, t.tabla_descri "
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                // + " and t.tabla_tipo1 in ('C','M')"
                + " and t.tabla_estado=1"
                + " and (t.tabla_id like '" + consulta + "' or t.tabla_descri like '" + consulta + "')"
                + " order by 1";
        //+ " and (e.tabla_id like '%" + consulta + "%' or e.tabla_descri like '%" + consulta + "%')"

        objlstMovimiento = new ListModelList<InformesMovimiento>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setCod_constante(rs.getString("tabla_id"));
                objMovimiento.setDes_constante(rs.getString("tabla_descri"));
                objlstMovimiento.add(objMovimiento);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstMovimiento;

    }

    //para hallar periodo si existe
    public InformesMovimiento getperiodo(String codigo) throws SQLException {
        String query = "select * from v_listaperiodopago l where l.emp_id = '" + objUsuCredential.getCodemp() + "' and l.plppag_id = '" + codigo + "' order by l.plppag_id desc";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objMovimiento = null;
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setPeriodo(rs.getString("plppag_id"));
            }
        } catch (NullPointerException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }

        return objMovimiento;

    }

    public InformesMovimiento consultaConstante(String codigo) throws SQLException {
        String query = " select t.tabla_id, t.tabla_descri "
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                //  + " and t.tabla_tipo1 in ('C','M')"
                + " and t.tabla_estado=1"
                + " and t.tabla_id ='" + codigo + "'"
                + " order by 1";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objMovimiento = null;
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setCod_constante(rs.getString("tabla_id"));
                objMovimiento.setDes_constante(rs.getString("tabla_descri"));

            }
        } catch (NullPointerException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objMovimiento;
    }

    /**
     * @version 24/08/2017
     * @param sucursal
     * @param periodo
     * @param persona
     * @param concepto
     * @param val_m
     * @return
     * @throws java.sql.SQLException
     * @autor Junior Fernandez Metodo para mostra lista principal
     */
    public ListModelList<InformesMovimiento> muestraListaTplplanilla(String sucursal, String periodo, String persona, String concepto, int val_m) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and t.suc_id =" + sucursal + "";
        String s_periodo = periodo.isEmpty() ? "" : " and t.plppag_id in ('" + periodo + "')";
        String s_personal = persona.isEmpty() ? "" : " and t.pltipdoc||t.plnrodoc in ('" + persona + "')";
        String s_concepto = concepto.isEmpty() ? "" : " and t.plcodcon in ('" + concepto + "')";
        String val_mm = "";
        if (val_m == 1) {
            val_mm = "and t.plmonto > 0";
        } else if (val_m == 2) {
            val_mm = "and t.plmonto < 0";
        } else {
            val_mm = "";
        }

        String query = " select t.pltipdoc||t.plnrodoc codigo,"
                + " pack_planilla_informes.f_descripcion_nombres(t.plnrodoc,t.pltipdoc) nombres,"
                + " t.plppag_id,t.plcodcon,"
                + " pack_planilla_informes.f_descripcion_constante(t.plcodcon) des_constante,"
                + " t.plmonto,e.emp_des"
                + " from tplplanilla t,tempresas e"
                + " where t.emp_id = e.emp_id"
                + " and t.emp_id = " + objUsuCredential.getCodemp() + ""
                + s_sucursal
                + s_periodo
                + s_personal
                + s_concepto
                + val_mm
                + " order by t.plnrodoc";

        objlstMovimiento = null;
        objlstMovimiento = new ListModelList<InformesMovimiento>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setCodigo(rs.getString("codigo"));
                objMovimiento.setNombres(rs.getString("nombres"));
                objMovimiento.setPeriodo(rs.getString("plppag_id"));
                objMovimiento.setId_concepto(rs.getString("plcodcon"));
                objMovimiento.setDes_concepto(rs.getString("des_constante"));
                objMovimiento.setMonto(rs.getDouble("plmonto"));
                objMovimiento.setEmpresa(rs.getString("emp_des"));
                objlstMovimiento.add(objMovimiento);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMovimiento;

    }

    /**
     * @version 24/08/2017
     * @param sucursal
     * @param periodo
     * @param persona
     * @param concepto
     * @param val_m
     * @return
     * @throws java.sql.SQLException
     * @autor Junior Fernandez Metodo para mostra lista principal en tplplames
     */
    public ListModelList<InformesMovimiento> muestraListaTplplames(String sucursal, String periodo, String persona, String concepto, int val_m) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and t.suc_id =" + sucursal + "";
        String s_periodo = periodo.isEmpty() ? "" : " and t.plppag_id in ('" + periodo + "')";
        String s_personal = persona.isEmpty() ? "" : " and t.pltipdoc||t.plnrodoc in ('" + persona + "')";
        String s_concepto = concepto.isEmpty() ? "" : " and t.plcodcon in ('" + concepto + "')";
        String val_mm = "";
        if (val_m == 1) {
            val_mm = "and t.plmonto > 0";
        } else if (val_m == 2) {
            val_mm = "and t.plmonto < 0";
        } else {
            val_mm = "";
        }

        String query = "select t.pltipdoc||t.plnrodoc codigo,"
                + " pack_planilla_informes.f_descripcion_nombres(t.plnrodoc,t.pltipdoc) nombres,"
                + " t.plppag_id,t.plcodcon,"
                + " pack_planilla_informes.f_descripcion_constante(t.plcodcon) des_constante,"
                + " t.plmonto,e.emp_des"
                + " from tplplames t, tempresas e "
                + " where  t.emp_id = e.emp_id"
                + " and t.emp_id = " + objUsuCredential.getCodemp() + ""
                + s_sucursal
                + s_periodo
                + s_personal
                + s_concepto
                + val_mm
                + " order by t.plnrodoc, t.plcodcon";

        objlstMovimiento = null;
        objlstMovimiento = new ListModelList<InformesMovimiento>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setCodigo(rs.getString("codigo"));
                objMovimiento.setNombres(rs.getString("nombres"));
                objMovimiento.setPeriodo(rs.getString("plppag_id"));
                objMovimiento.setId_concepto(rs.getString("plcodcon"));
                objMovimiento.setDes_concepto(rs.getString("des_constante"));
                objMovimiento.setMonto(rs.getDouble("plmonto"));
                objMovimiento.setEmpresa(rs.getString("emp_des"));
                objlstMovimiento.add(objMovimiento);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMovimiento;

    }

    /**
     * Metodo para verificar si hay data en tplplames y si es
     *
     * @return
     * @throws java.sql.SQLException
     */
    public ListModelList<InformesMovimiento> verDataTplplames() throws SQLException {

        String query = "select t.plppag_id from tplplames t";

        objlstMovimiento = new ListModelList<InformesMovimiento>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMovimiento = new InformesMovimiento();
                objMovimiento.setPeriodo(rs.getString("plppag_id"));
                objlstMovimiento.add(objMovimiento);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstMovimiento;

    }

}
