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
import java.sql.Date;
import java.text.SimpleDateFormat;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesPrestamos;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
import org.me.gj.model.planillas.mantenimiento.Personal;

import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoAnalisisAfp {

    ListModelList<ConfAfp1> objlstAfp;
    ConfAfp1 objAfp;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    public ListModelList<ConfAfp1> lovAfp() throws SQLException {

        String sql_personal = "{call codijisa.pack_planilla_informes.p_afplistar(?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_personal);

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(1);

            objlstAfp = null;
            objlstAfp = new ListModelList<ConfAfp1>();

            while (rs.next()) {
                objAfp = new ConfAfp1();
                objAfp.setAfp_id(rs.getString("tabla_id"));
                objAfp.setAfp_des(rs.getString("tabla_descri"));
                             
                objlstAfp.add(objAfp);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }

        }

        return objlstAfp;

    }
    
    public ListModelList<ConfAfp1> lovAfpConsulta(String consulta) throws SQLException {

        String sql_personal = "{call codijisa.pack_planilla_informes.p_afplistarConsulta(?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_personal);
            cst.setString(1, consulta);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(2);

            objlstAfp = null;
            objlstAfp = new ListModelList<ConfAfp1>();

            while (rs.next()) {
                objAfp = new ConfAfp1();
                objAfp.setAfp_id(rs.getString("tabla_id"));
                objAfp.setAfp_des(rs.getString("tabla_descri"));
                             
                objlstAfp.add(objAfp);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }

        }

        return objlstAfp;

    }

}
