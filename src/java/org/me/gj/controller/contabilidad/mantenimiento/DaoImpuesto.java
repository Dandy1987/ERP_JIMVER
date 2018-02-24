package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.*;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.procesos.DaoFacPro;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.Impuestos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoImpuesto {

    Impuestos objImpuesto;
    ListModelList<Impuestos> objlstImpuestos;

    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    private String s_msg = "";
    Session sesion = Sessions.getCurrent();
    private UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(DaoImpuesto.class);

    public ListModelList<Impuestos> lstImpuestos(int caso) throws SQLException {
        String SQL_IMPUESTOS;
        if (caso == 0) {
            SQL_IMPUESTOS = "select * from timpuestos p where p.imp_est<>" + caso + " order by p.imp_id";
        } else {
            SQL_IMPUESTOS = "select * from timpuestos p where p.imp_est=" + caso + " order by p.imp_ord,p.imp_id";
        }
        try {
            con = new ConectaBD().conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_IMPUESTOS);
            objlstImpuestos = new ListModelList<Impuestos>();
            while (rs.next()) {
                objImpuesto = new Impuestos();
                objImpuesto.setImp_id(rs.getInt(1));
                objImpuesto.setImp_des(rs.getString(2));
                objImpuesto.setImp_valor(rs.getInt(3));
                objImpuesto.setImp_est(rs.getInt(4));
                objImpuesto.setImp_usuadd(rs.getString(5));
                objImpuesto.setImp_fecadd(rs.getDate(6));
                objImpuesto.setImp_usumod(rs.getString(7));
                objImpuesto.setImp_fecmod(rs.getDate(8));
                objImpuesto.setImp_nomrep(rs.getString(9));
                objImpuesto.setImp_ord(rs.getInt(10));
                objlstImpuestos.add(objImpuesto);
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
        return objlstImpuestos;
    }

    public double obtieneValorImpuesto(String pro_id) throws SQLException {
        String SQL_VALORIMPTO = "select decode(t.pro_condimp,'A',t1.imp_valor,0) impto from codijisa.tproductos t, timpuestos t1 where t.imp_id = t1.imp_id "
                + " and t.pro_id = '" + pro_id + "' and t.pro_est=1";
        double impvalor = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VALORIMPTO);
            while (rs.next()) {
                impvalor = rs.getDouble(1);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos correctamente");
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
        return impvalor;
    }

}
