package org.me.gj.controller.facturacion.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.procesos.NotesVentaCab;
import org.me.gj.model.facturacion.procesos.NotesVentaDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotesVenta {

    //Instancias de Objetos
    ListModelList<NotesVentaCab> objListaNotesVentaCab;
    ListModelList<NotesVentaDet> objListaNotesVentaDet;
    NotesVentaCab objNotesVentaCab;
    NotesVentaDet objNotesVentaDet;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoPedVen.class);

    //Transaccional Primario
    public ParametrosSalida generarDocumentoVenta(int emp_id, int suc_id, Object[][] listapedvencab, int docvta, int serie, Date fecdocvta, Date fecdespacho,String pcred, String usuario) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        ARRAY arrCab;
        ArrayDescriptor arrayCab;
        String SQL_GENERAR_DOCVENTA = "{call PACK_TVENTAS.p_generadocventa(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_DOCVENTA);
            arrayCab = ArrayDescriptor.createDescriptor("LISTANOTESVTAPED", con.getMetaData().getConnection());
            arrCab = new ARRAY(arrayCab, con.getMetaData().getConnection(), listapedvencab);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setArray(3, arrCab);
            cst.setInt(4, docvta);
            cst.setInt(5, serie);
            cst.setDate(6, new java.sql.Date(fecdocvta.getTime()));
            cst.setDate(7, new java.sql.Date(fecdespacho.getTime()));
            cst.setString(8, pcred);
            cst.setString(9, usuario);
            cst.setString(10, usuario);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el documento de venta");
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el documento de venta debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el documento de venta debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Transaccional Secundario
    public ListModelList<NotesVentaCab> busquedaNotaSalidaVenta(int emp_id, int suc_id, String fechai, String supervisor, String vendedor, String pedido, String situacion, int estado, String periodo, String orden,String tipventa, int tipdoc) throws SQLException {
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
    	String SQL_LISTAR_NOTAPEN_VENTACAB;
    	String s_validatipdoc = tipdoc == 0 ? "":" and t.clibolfac ="+tipdoc+" ";
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.pcab_periodo=" + periodo + " ";
        String s_validafec = fechai.isEmpty() ? "" : " and t.PCAB_FECEMI like to_date('" + fechai + "','dd/mm/yyyy') ";
        String s_validavendedor = vendedor.isEmpty() ? "" : " and t.ven_id like '" + vendedor + "'";
        String s_validasupervisor = supervisor.isEmpty() ? "" : " and t.sup_id like '" + supervisor + "' ";
        String s_validapedido = pedido.isEmpty() ? "" : " and t.pcab_nroped like '" + pedido + "' ";

      
	        if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
	            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
	                    + " and t.pcab_situacion = 2  and t.pcab_tipven  like '"+ tipventa +"' and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
	                    + s_validaperiodo + s_validapedido +s_validatipdoc
	                    +" order by pcab_fecemi , t." + orden + " ";
	        } else if (periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
	            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
	                    + " and t.pcab_situacion = 2  and t.pcab_tipven like '"+ tipventa +"' and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
	                    + s_validasupervisor+ s_validatipdoc + " order by pcab_fecemi , t." + orden + " ";
	        } else if (periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fechai.isEmpty()) {
	            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
	                    + " and t.pcab_situacion = 2  and t.pcab_tipven like '"+ tipventa +"' and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
	                    + s_validavendedor +s_validatipdoc+ " order by pcab_fecemi , t." + orden + " ";
	        } else if (periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fechai.isEmpty()) {
	            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
	                    + " and t.pcab_situacion = 2  and t.pcab_tipven like '"+ tipventa +"' and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
	                    + s_validafec + s_validatipdoc + " order by pcab_fecemi , t." + orden + " ";
	        } else {
	            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
	                    + " and t.pcab_situacion = 2  and t.pcab_tipven like '"+ tipventa +"' and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
	                    + s_validavendedor + s_validatipdoc+s_validasupervisor + s_validapedido + s_validafec + s_validaperiodo + " order by t." + orden + " ASC ";
	        }
        
        
        
        objListaNotesVentaCab = new ListModelList<NotesVentaCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_NOTAPEN_VENTACAB);
            while (rs.next()) {
                objNotesVentaCab = new NotesVentaCab();
                objNotesVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objNotesVentaCab.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objNotesVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objNotesVentaCab.setSuc_id(rs.getInt("suc_id"));
                objNotesVentaCab.setEmp_id(rs.getInt("emp_id"));
                objNotesVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objNotesVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objNotesVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objNotesVentaCab.setZon_id(rs.getString("zon_id"));
                objNotesVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                objNotesVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").length() < 12 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 12));
                objNotesVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objNotesVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objNotesVentaCab.setCli_id(rs.getString("cli_id"));
                objNotesVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objNotesVentaCab.setVen_id(rs.getString("ven_id"));
                objNotesVentaCab.setSup_id(rs.getString("sup_id"));
                objNotesVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objNotesVentaCab.setCon_id(rs.getString("con_id"));
                objNotesVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objNotesVentaCab.setLp_id(rs.getString("lp_id"));
                objNotesVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objNotesVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objNotesVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objNotesVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objNotesVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objNotesVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objNotesVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objNotesVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objNotesVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objNotesVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objNotesVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objNotesVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objNotesVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objNotesVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objNotesVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objNotesVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objNotesVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objNotesVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objNotesVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objNotesVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objNotesVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objNotesVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                objNotesVentaCab.setCli_des(rs.getString("cli_des").length() < 35 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 35));
                objNotesVentaCab.setVen_des(rs.getString("ven_des").length() < 35 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 35));
                objNotesVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                objNotesVentaCab.setZon_des(rs.getString("zon_des"));
                objNotesVentaCab.setLp_des(rs.getString("lp_des"));
                objNotesVentaCab.setCond_des(rs.getString("con_des"));
                objNotesVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objNotesVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objNotesVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objNotesVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objNotesVentaCab.setIndicador(rs.getInt("pcab_ind"));
                objNotesVentaCab.setNotes_tip(rs.getString("notes_tip") == null ? "" : rs.getString("notes_tip"));
                objNotesVentaCab.setNotes_des(rs.getString("notes_des") == null ? "" : rs.getString("notes_des"));
                objNotesVentaCab.setNotes_nro(rs.getString("notes_nro") == null ? "" : rs.getString("notes_nro"));
                objNotesVentaCab.setSup_des(rs.getString("sup_des") == null ? "" : rs.getString("sup_des"));
                objNotesVentaCab.setNotes_sit(rs.getInt("notes_sit") == 0 ? 1 : rs.getInt("notes_sit"));
                objNotesVentaCab.setClibolfac(rs.getInt("clibolfac"));
                objListaNotesVentaCab.add(objNotesVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaNotesVentaCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaNotesVentaCab;
    }
    
  //Transaccional Secundario    
    public ListModelList<NotesVentaCab> busquedaNotaSalidaVentaxTipoVenta(int emp_id, int suc_id, String fechai, String supervisor, String vendedor, String pedido, String situacion, int estado, String periodo, String orden) throws SQLException {
        String SQL_LISTAR_NOTAPEN_VENTACAB;
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.pcab_periodo=" + periodo + " ";
        String s_validafec = fechai.isEmpty() ? "" : " and t.PCAB_FECEMI like to_date('" + fechai + "','dd/mm/yyyy') ";
        String s_validavendedor = vendedor.isEmpty() ? "" : " and t.ven_id like '" + vendedor + "'";
        String s_validasupervisor = supervisor.isEmpty() ? "" : " and t.sup_id like '" + supervisor + "' ";
        String s_validapedido = pedido.isEmpty() ? "" : " and t.pcab_nroped like '" + pedido + "' ";

        if (!periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validaperiodo + s_validapedido + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && !supervisor.isEmpty() && vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validasupervisor + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && !vendedor.isEmpty() && fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + " order by t." + orden + " ASC ";
        } else if (periodo.isEmpty() && supervisor.isEmpty() && vendedor.isEmpty() && !fechai.isEmpty()) {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validafec + " order by t." + orden + " ASC ";
        } else {
            SQL_LISTAR_NOTAPEN_VENTACAB = "select * from v_listanotasalidacab t where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id
                    + "and t.notes_sit like '" + situacion + "' and t.pcab_estado=" + estado
                    + s_validavendedor + s_validasupervisor + s_validapedido + s_validafec + s_validaperiodo + " order by t." + orden + " ASC ";
        }
        objListaNotesVentaCab = new ListModelList<NotesVentaCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_NOTAPEN_VENTACAB);
            while (rs.next()) {
                objNotesVentaCab = new NotesVentaCab();
                objNotesVentaCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objNotesVentaCab.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objNotesVentaCab.setPcab_periodo(rs.getString("pcab_periodo"));
                objNotesVentaCab.setSuc_id(rs.getInt("suc_id"));
                objNotesVentaCab.setEmp_id(rs.getInt("emp_id"));
                objNotesVentaCab.setPcab_estado(rs.getInt("pcab_estado"));
                objNotesVentaCab.setPcab_situacion(rs.getInt("pcab_situacion"));
                objNotesVentaCab.setPcab_fecent(rs.getDate("pcab_fecent"));
                objNotesVentaCab.setZon_id(rs.getString("zon_id"));
                objNotesVentaCab.setPcab_motrec(rs.getString("pcab_motrec"));
                objNotesVentaCab.setPcab_motrec_des(rs.getString("pcab_motrec_des") == null ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").length() < 12 ? rs.getString("pcab_motrec_des") : rs.getString("pcab_motrec_des").substring(0, 12));
                objNotesVentaCab.setClidir_id(rs.getInt("clidir_id"));
                objNotesVentaCab.setPcab_dirent(rs.getString("pcab_dirent"));
                objNotesVentaCab.setCli_id(rs.getString("cli_id"));
                objNotesVentaCab.setPcab_canid(rs.getString("pcab_canid"));
                objNotesVentaCab.setVen_id(rs.getString("ven_id"));
                objNotesVentaCab.setSup_id(rs.getString("sup_id"));
                objNotesVentaCab.setPcab_facbol(rs.getInt("pcab_facbol"));
                objNotesVentaCab.setCon_id(rs.getString("con_id"));
                objNotesVentaCab.setPcab_mon(rs.getString("pcab_mon"));
                objNotesVentaCab.setLp_id(rs.getString("lp_id"));
                objNotesVentaCab.setPcab_tipcam(rs.getDouble("pcab_tipcam"));
                objNotesVentaCab.setPcab_limcre(rs.getDouble("pcab_limcre"));
                objNotesVentaCab.setPcab_limdoc(rs.getInt("pcab_limdoc"));
                objNotesVentaCab.setPcab_salcre(rs.getDouble("pcab_salcre"));
                objNotesVentaCab.setPcab_nrodni(rs.getString("pcab_nrodni"));
                objNotesVentaCab.setPcab_nroruc(rs.getString("pcab_nroruc"));
                objNotesVentaCab.setPcab_totped(rs.getDouble("pcab_totped"));
                objNotesVentaCab.setPcab_diavis(rs.getInt("pcab_diavis"));
                objNotesVentaCab.setPcab_horent(rs.getString("pcab_horent"));
                objNotesVentaCab.setPcab_gpslat(rs.getInt("pcab_gpslat"));
                objNotesVentaCab.setPcab_gpslon(rs.getInt("pcab_gpslon"));
                objNotesVentaCab.setPcab_totper(rs.getDouble("pcab_totper"));
                objNotesVentaCab.setPcab_aprcon(rs.getInt("pcab_aprcon"));
                objNotesVentaCab.setPcab_aprglo(rs.getString("pcab_aprglo"));
                objNotesVentaCab.setPcab_docref(rs.getString("pcab_docref"));
                objNotesVentaCab.setPcab_usuadd(rs.getString("pcab_usuadd"));
                objNotesVentaCab.setPcab_fecadd(rs.getTimestamp("pcab_fecadd"));
                objNotesVentaCab.setPcab_usumod(rs.getString("pcab_usumod"));
                objNotesVentaCab.setPcab_fecmod(rs.getTimestamp("pcab_fecmod"));
                objNotesVentaCab.setPcab_giro(rs.getString("pcab_giro"));
                objNotesVentaCab.setPcab_ppago(rs.getInt("pcab_ppago"));
                objNotesVentaCab.setPcab_tipven(rs.getString("pcab_tipven"));
                objNotesVentaCab.setCli_des(rs.getString("cli_des").length() < 35 ? rs.getString("cli_des") : rs.getString("cli_des").substring(0, 35));
                objNotesVentaCab.setVen_des(rs.getString("ven_des").length() < 35 ? rs.getString("ven_des") : rs.getString("ven_des").substring(0, 35));
                objNotesVentaCab.setPcab_situacion_des(rs.getString("pcab_situacion_des"));
                objNotesVentaCab.setZon_des(rs.getString("zon_des"));
                objNotesVentaCab.setLp_des(rs.getString("lp_des"));
                objNotesVentaCab.setCond_des(rs.getString("con_des"));
                objNotesVentaCab.setCon_dpla(rs.getInt("con_dpla"));
                objNotesVentaCab.setTip_ventades(rs.getString("tip_ventdes"));
                objNotesVentaCab.setPcab_modtipcam(rs.getInt("pcab_modtip"));
                objNotesVentaCab.setPcab_diavisdes(rs.getString("pcab_diavis_des"));
                objNotesVentaCab.setIndicador(rs.getInt("pcab_ind"));
                objNotesVentaCab.setNotes_tip(rs.getString("notes_tip") == null ? "" : rs.getString("notes_tip"));
                objNotesVentaCab.setNotes_des(rs.getString("notes_des") == null ? "" : rs.getString("notes_des"));
                objNotesVentaCab.setNotes_nro(rs.getString("notes_nro") == null ? "" : rs.getString("notes_nro"));
                objNotesVentaCab.setSup_des(rs.getString("sup_des") == null ? "" : rs.getString("sup_des"));
                objNotesVentaCab.setNotes_sit(rs.getInt("notes_sit") == 0 ? 1 : rs.getInt("notes_sit"));
                objListaNotesVentaCab.add(objNotesVentaCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaNotesVentaCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListaNotesVentaCab;
    }

    public ListModelList<NotesVentaDet> listaNotesVentaDet(int emp_id, int suc_id, String nropedven) throws SQLException {
        String SQL_LISTAR_NOTAPEN_VENTADET = " select * from v_listanotasalidadet t"
                + " where t.pcab_nroped=" + nropedven + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.pdet_estado<>0"
                + " order by t.pdet_item";
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAR_NOTAPEN_VENTADET);
            objListaNotesVentaDet = new ListModelList();
            while (rs.next()) {
                objNotesVentaDet = new NotesVentaDet();
                objNotesVentaDet.setPcab_nroped(rs.getString("pcab_nroped"));
                objNotesVentaDet.setPcab_fecemi(rs.getTimestamp("pcab_fecemi"));
                objNotesVentaDet.setPcab_periodo(rs.getString("pcab_periodo"));
                objNotesVentaDet.setSuc_id(rs.getInt("suc_id"));
                objNotesVentaDet.setEmp_id(rs.getInt("emp_id"));
                objNotesVentaDet.setPdet_estado(rs.getInt("pdet_estado"));
                objNotesVentaDet.setPdet_situacion(rs.getInt("pdet_situacion"));
                objNotesVentaDet.setPro_id(rs.getString("pro_id"));
                objNotesVentaDet.setPro_cla(rs.getString("pro_cla"));
                objNotesVentaDet.setLp_id(rs.getString("lp_id"));
                objNotesVentaDet.setLp_des(rs.getString("lp_des"));
                objNotesVentaDet.setPdet_canped(rs.getInt("pdet_canped"));
                objNotesVentaDet.setPdet_canent(rs.getInt("pdet_canent"));
                objNotesVentaDet.setNesdet_cant(rs.getDouble("nesdet_cant"));
                objNotesVentaDet.setNesdet_undpre(rs.getInt("nesdet_undpre"));
                objNotesVentaDet.setPdet_punit(rs.getDouble("pdet_punit"));
                objNotesVentaDet.setPdet_vventa(rs.getDouble("pdet_vventa"));
                objNotesVentaDet.setPdet_dscto(rs.getDouble("pdet_dscto"));
                objNotesVentaDet.setPdet_sdscto(rs.getDouble("pdet_sdscto"));
                objNotesVentaDet.setPdet_impto(rs.getDouble("pdet_impto"));
                objNotesVentaDet.setPdet_vimpto(rs.getDouble("pdet_vimpto"));
                objNotesVentaDet.setPdet_pventa(rs.getDouble("pdet_pventa"));
                objNotesVentaDet.setPdet_unipre(rs.getInt("pdet_unipre"));
                objNotesVentaDet.setPdet_desart(rs.getString("pdet_desart"));
                objNotesVentaDet.setPdet_desman(rs.getInt("pdet_desman"));
                objNotesVentaDet.setPdet_proper(rs.getDouble("pdet_proper"));
                objNotesVentaDet.setPdet_item(rs.getInt("pdet_item"));
                objNotesVentaDet.setPdet_usuadd(rs.getString("pdet_usuadd"));
                objNotesVentaDet.setPdet_prodes(rs.getString("pro_des"));
                objListaNotesVentaDet.add(objNotesVentaDet);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaNotesVentaDet.getSize() + " registro(s)");
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
        return objListaNotesVentaDet;
    }

}
