/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.MedSunat;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author jmayuri
 */
public class DaoMedSunat {

    ListModelList<MedSunat> objlstMedSunat;
    MedSunat objMedSunat;
    Connection con;

    public ListModelList<MedSunat> lstMedSunat(int caso) throws SQLException {
        String SQL_MEDIDAS_SUNAT;
        if (caso == 0) {
            SQL_MEDIDAS_SUNAT = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est from ttabgen p where p.tab_cod=16 and p.tab_est<>" + caso + " order by p.tab_ord,p.tab_id";
        } else {
            SQL_MEDIDAS_SUNAT = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est from ttabgen p where p.tab_cod=16 and p.tab_est=" + caso + " order by p.tab_ord,p.tab_id";
        }
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_MEDIDAS_SUNAT);
            objlstMedSunat=new ListModelList<MedSunat>();
            while (rs.next()) {
                objMedSunat = new MedSunat();
                objMedSunat.setTab_id(rs.getInt(1));
                objMedSunat.setTab_subdes(rs.getString(2));
                objMedSunat.setTab_subdir(rs.getString(3));
                objMedSunat.setTab_est(rs.getInt(4));
                objlstMedSunat.add(objMedSunat);
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
        return objlstMedSunat;
    }
}