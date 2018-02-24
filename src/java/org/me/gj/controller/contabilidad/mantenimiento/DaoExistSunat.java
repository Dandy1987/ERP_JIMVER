
package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.ExistSunat;
import org.zkoss.zul.ListModelList;

public class DaoExistSunat {
    ExistSunat objExistSunat;
    ListModelList<ExistSunat> objlstExistSunat;
    Connection con;
    
    public ListModelList<ExistSunat> lstExistSunat(int caso) throws SQLException
    {
        String SQL_EXIST_SUNAT;
        if (caso == 0) {
            SQL_EXIST_SUNAT = "select p.tab_id,p.tab_subdes,p.tab_est from ttabgen p where p.tab_cod=17 and p.tab_est<>" + caso + " order by p.tab_ord,p.tab_id";
        } else {
            SQL_EXIST_SUNAT = "select p.tab_id,p.tab_subdes,p.tab_est from ttabgen p where p.tab_cod=17 and p.tab_est=" + caso + " order by p.tab_ord,p.tab_id";
        }
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_EXIST_SUNAT);
            objlstExistSunat=new ListModelList<ExistSunat>();
            while (rs.next()) {
                objExistSunat = new ExistSunat();
                objExistSunat.setTab_id(rs.getInt(1));
                objExistSunat.setTab_subdes(rs.getString(2));
                objExistSunat.setTab_est(rs.getInt(3));
                objlstExistSunat.add(objExistSunat);
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
        return objlstExistSunat;
    }
}
