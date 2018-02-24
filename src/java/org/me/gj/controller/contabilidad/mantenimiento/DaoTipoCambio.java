package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.TipoCambio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoTipoCambio {

    private Connection con;
    ListModelList<TipoCambio> objlstTipoCambio;
    TipoCambio objTipoCambio;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(DaoTipoCambio.class);

    public ListModelList<TipoCambio> lstTipoCambio(int i_caso) throws SQLException {
        String SQL_DATA;
        if (i_caso == 0) {
            SQL_DATA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_anc,p.tab_ord,p.tab_nomrep "
                    + "from codijisa.ttabgen p where p.tab_cod=20 and p.tab_id<>0 and p.tab_est<>" + i_caso;
        } else {
            SQL_DATA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_anc,p.tab_ord,p.tab_nomrep "
                    + "from codijisa.ttabgen p where p.tab_cod=20 and p.tab_id<>0 and p.tab_est=" + i_caso;
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_DATA);
            objlstTipoCambio = new ListModelList<TipoCambio>();
            while (rs.next()) {
                objTipoCambio = new TipoCambio();
                objTipoCambio.setIdTipCam(rs.getInt(1));
                objTipoCambio.setDesTipCam(rs.getString(2));
                objTipoCambio.setSigTipCam(rs.getString(3));
                objTipoCambio.setEstTipCam(rs.getInt(4));
                objTipoCambio.setValorTipCam(rs.getDouble(5));
                objTipoCambio.setOrdTipCam(rs.getInt(6));
                objTipoCambio.setNomRepTipCam(rs.getString(7));
                objlstTipoCambio.add(objTipoCambio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoCambio.getSize() + " registro(s)");
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
        return objlstTipoCambio;
    }

    public double lstValorCambio(int idTipCam) throws SQLException {
        String SQL_DATA = "select t.tab_anc from ttabgen t where t.tab_cod=20 and t.tab_id=" + idTipCam;
        double valTipCam = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_DATA);
            while (rs.next()) {
                valTipCam = rs.getDouble(1);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado 1 registro(s)");
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
        return valTipCam;
    }
}
