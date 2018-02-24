/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.UnidMedida;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author jmayuri
 */
public class DaoUnidMedida {
    UnidMedida objUnidMedida;
    ListModelList<UnidMedida> objlstUnidMedida;
    Connection con;
    
    public ListModelList<UnidMedida> lstUnidMedida(int caso) throws SQLException
    {
        String SQL_UNID_MEDIDA;
        if (caso == 0) {
            SQL_UNID_MEDIDA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est from ttabgen p where p.tab_cod=16 and p.tab_est<>" + caso + " order by p.tab_ord,p.tab_id";
        } else {
            SQL_UNID_MEDIDA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est from ttabgen p where p.tab_cod=16 and p.tab_est=" + caso + " order by p.tab_ord,p.tab_id";
        }
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_UNID_MEDIDA);
            objlstUnidMedida=new ListModelList<UnidMedida>();
            while (rs.next()) {
                objUnidMedida = new UnidMedida();
                objUnidMedida.setTab_id(rs.getInt(1));
                objUnidMedida.setTab_subdes(rs.getString(2));
                objUnidMedida.setTab_subdir(rs.getString(3));
                objUnidMedida.setTab_est(rs.getInt(4));
                objlstUnidMedida.add(objUnidMedida);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstUnidMedida;
    }
    
    public ListModelList<UnidMedida> lstUnidMedida() throws SQLException
    {
        String SQL_UNID_MEDIDA;
            SQL_UNID_MEDIDA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est from ttabgen p where p.tab_cod=16 and p.tab_subdes in ('KILOGRAMOS','LITROS','MILILITROS','GRAMOS') and p.tab_est = '1' order by p.tab_ord,p.tab_id";
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_UNID_MEDIDA);
            objlstUnidMedida=new ListModelList<UnidMedida>();
            while (rs.next()) {
                objUnidMedida = new UnidMedida();
                objUnidMedida.setTab_id(rs.getInt(1));
                objUnidMedida.setTab_subdes(rs.getString(2));
                objUnidMedida.setTab_subdir(rs.getString(3));
                objUnidMedida.setTab_est(rs.getInt(4));
                objlstUnidMedida.add(objUnidMedida);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstUnidMedida;
    }
    
}
