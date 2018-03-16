/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.controller.planillas.utilitarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;


public class DaoUtiBancos {
     String s_msg = "";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //Indicadores
    int i_flagErrorBD = 0;
    public static String P_WHERE = "";

    //Variables de Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCierres.class);
    
    
    public String Cabecera(int nempid, int nsucid, String cperiodo) throws SQLException {

        String sql_personal = "{call codijisa.pack_tenlaces.p_listCabeceraBanco(?,?,?,?)}";
        String s_cabecera="";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_personal);
            cst.setInt(1, nempid);
            cst.setInt(2, nsucid);
            cst.setString(3, cperiodo);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(4);



            while (rs.next()) {
        
                s_cabecera =rs.getString("cabecera");
  
                           
           
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

        return s_cabecera;

    }
    
}
