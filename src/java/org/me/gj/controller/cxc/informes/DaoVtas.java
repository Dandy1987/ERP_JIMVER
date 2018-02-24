package org.me.gj.controller.cxc.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.VtasDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoVtas {
	
    //Instancia de Objetos
    ListModelList<VtasDet> objlstVtasDet;
	VtasDet objVtasDet;
	
    //Variables Publicas
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;   
	//Variables de Sesion
	Session sesion = Sessions.getCurrent();
	UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
	private static final Logger LOGGER = Logger.getLogger(DaoCtaCob.class);

	
	 public ListModelList<VtasDet> listaVentasDetallexCodigo(String vtas_key) throws SQLException{ 	
	   	 String SQL_LISTA_CTACOBCAB = " select * from v_listavtas_det t"
	                + " where t.vtas_key=" + vtas_key 
	                + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
	                + " order by t.pro_id ";
	        try {
	            con = new ConectaBD().conectar();
	            st = con.createStatement();
	            rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
	            objlstVtasDet = new ListModelList<VtasDet>();
	            while (rs.next()) {
	                objVtasDet = new VtasDet();
	                objVtasDet.setEmp_id(rs.getInt("emp_id"));
	                objVtasDet.setSuc_id(rs.getInt("suc_id")); 
	                objVtasDet.setVtas_key(rs.getString("vtas_key"));
	                objVtasDet.setVtas_periodo(rs.getString("vtas_periodo"));
	                objVtasDet.setPro_id(rs.getString("pro_id"));	
	                objVtasDet.setPro_des(rs.getString("pro_des"));	
	                objVtasDet.setPro_clas(rs.getString("pro_clas"));
	                objVtasDet.setPro_peso(rs.getInt("pro_peso"));
	                objVtasDet.setVtas_estado(rs.getInt("vtas_estado"));
	                //objVtasDet.setVtas_estadodes(rs.getString("vtas_estadodes"));
	                objVtasDet.setVtas_sit(rs.getInt("vtas_sit"));
	                objVtasDet.setVtas_unipre(rs.getInt("vtas_unipre"));
	                objVtasDet.setVtas_canped(rs.getInt("vtas_canped"));
	                objVtasDet.setVtas_canent(rs.getInt("vtas_canent"));
	                objVtasDet.setVtas_punit(rs.getDouble("vtas_punit"));
	                objVtasDet.setVtas_vventa(rs.getDouble("vtas_vventa"));
	                objVtasDet.setVtas_sdsctot(rs.getDouble("vtas_sdsctot"));
	                objVtasDet.setVtas_dsctot(rs.getDouble("vtas_dsctot"));
	                objVtasDet.setVtas_impto(rs.getDouble("vtas_impto"));
	                objVtasDet.setVtas_vimpto(rs.getDouble("vtas_vimpto"));
	                objVtasDet.setVtas_pventa(rs.getDouble("vtas_pventa"));
	                objVtasDet.setVtas_impper(rs.getDouble("vtas_impper"));
	                objVtasDet.setVtas_usuadd(rs.getString("vtas_usuadd"));
	                objVtasDet.setVtas_fecadd(rs.getTimestamp("vtas_fecadd"));
	                objVtasDet.setVtas_usumod(rs.getString("vtas_usumod"));
	                objVtasDet.setVtas_fecmod(rs.getTimestamp("vtas_fecmod")); 
	                objVtasDet.setLp_id(rs.getString("lp_id"));
	                objVtasDet.setLp_des(rs.getString("lp_des"));
	                objlstVtasDet.add(objVtasDet);
	               
	            }
	            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha encontrado ventas con  " + objlstVtasDet.getSize()+" registro(s)");
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
	        return objlstVtasDet;  	
	   }

}
