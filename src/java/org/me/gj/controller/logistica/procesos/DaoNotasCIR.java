package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.NotaCIR;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotasCIR {

    //Instancias de Objetos
    ListModelList<NotaCIR> objlstNotasCIR;
    NotaCIR objNotaCIR;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoNotasCIR.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida procesarNotaCambio(NotaCIR objNotaCIR, String nescab_tnotaes, int nc_situacion, Date fecha) throws SQLException, ParseException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_PROCESAR_NOTACAMBCAB = "{call pack_tnotacambio.p_procesarNotCambCab(?,?,?,?,?,?,?,?,?,?)}";
        try {
			String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(fecha);
            fecha_hora = sdf1.format(fecha);
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            Time hora = new Time(fec_conc.getTime());
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCESAR_NOTACAMBCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaCIR.getEmp_id());
            cst.setInt("n_suc_id", objNotaCIR.getSuc_id());
            cst.setLong("n_nc_key", objNotaCIR.getNot_key());
            cst.setString("c_nescab_cliid", objNotaCIR.getCli_id());
            cst.setString("c_nc_tnotaes", nescab_tnotaes);
            cst.setInt("n_nc_sit", nc_situacion);
            cst.setTime("d_nescab_fecha", hora);
            cst.setString("c_nescab_usuadd", objNotaCIR.getNot_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Proceso una Nota de Cambio con Codigo " + objNotaCIR.getNot_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida procesarNotaRecojo(NotaCIR objNotaCIR, String nescab_tnotaes, int nc_situacion, Date fecha) throws SQLException, ParseException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_PROCESAR_NOTARECCAB = "{call pack_tnotarecojo.p_procesarNotRecCab(?,?,?,?,?,?,?,?,?,?)}";
        try {
			String fecha_cadena, fecha_hora;
             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
             SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
             SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
             fecha_cadena = sdf.format(fecha);
             fecha_hora = sdf1.format(fecha);
             String fecha_concat = fecha_cadena + ' ' + fecha_hora;
             Date fec_conc = sdf2.parse(fecha_concat);
             Time hora = new Time(fec_conc.getTime()); 
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCESAR_NOTARECCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaCIR.getEmp_id());
            cst.setInt("n_suc_id", objNotaCIR.getSuc_id());
            cst.setLong("n_nr_key", objNotaCIR.getNot_key());
            cst.setString("c_nescab_cliid", objNotaCIR.getCli_id());
            cst.setString("c_nr_tnotaes", nescab_tnotaes);
            cst.setInt("n_nr_sit", nc_situacion);
            cst.setTime("d_nescab_fecha", hora);
            cst.setString("c_nescab_usuadd", objNotaCIR.getNot_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Proceso una Nota de Recojo con Codigo " + objNotaCIR.getNot_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida procesarNotaIntercambio(NotaCIR objNotaCIR, String nescab_tnotaes, int nc_situacion, Date fecha) throws SQLException, ParseException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_PROCESAR_NOTAINTERCAB = "{call pack_tnotainter.p_procesarNotIntCab(?,?,?,?,?,?,?,?,?,?)}";
        try {
			String fecha_cadena, fecha_hora;
	         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	         SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
	         SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	         fecha_cadena = sdf.format(fecha);
	         fecha_hora = sdf1.format(fecha);
	         String fecha_concat = fecha_cadena + ' ' + fecha_hora;
	         Date fec_conc = sdf2.parse(fecha_concat);
	         Time hora = new Time(fec_conc.getTime());
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCESAR_NOTAINTERCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaCIR.getEmp_id());
            cst.setInt("n_suc_id", objNotaCIR.getSuc_id());
            cst.setLong("n_ni_key", objNotaCIR.getNot_key());
            cst.setString("c_nescab_cliid", objNotaCIR.getCli_id());
            cst.setString("c_ni_tnotaes", nescab_tnotaes);
            cst.setInt("n_ni_sit", nc_situacion);
            cst.setTime("d_nescab_fecha", hora);
            cst.setString("c_nescab_usuadd", objNotaCIR.getNot_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Proceso una Nota de Intercambio con Codigo " + objNotaCIR.getNot_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
    
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NotaCIR> listaNotasCIR(int emp_id, int suc_id, String not_tipkey, String ven_key, String cli_key, String not_periodo, String not_sit, String mcam_id, String not_fecemi) throws SQLException {
        String validaFecha, SQL_NOTASCIR;
        if (not_fecemi.equals("")) {
            validaFecha = "";
        } else {
            validaFecha = "and t.not_fecemi=to_date('" + not_fecemi + "','DD/MM/YYYY')";
        }
        SQL_NOTASCIR = "select * from v_listanotas t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.not_est=1 and "
                + //" t.not_sit in ("+not_sit.substring(0, 1) +","+not_sit.substring(1,2)+","+not_sit.substring(2, 3)+")"+
                " t.not_sit like '" + not_sit + "' "
                + " and t.not_tipkey like '" + not_tipkey + "' and t.ven_key like '" + ven_key + "'"
                + " and t.mcam_key like '" + mcam_id + "' and t.cli_key like '" + cli_key + "' " + validaFecha
                + " and t.not_periodo like '" + not_periodo + "'"
                + " order by t.not_tipkey, t.not_key";
        objlstNotasCIR = null;
        objlstNotasCIR = new ListModelList<NotaCIR>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTASCIR);
            while (rs.next()) {
                objNotaCIR = new NotaCIR();
                objNotaCIR.setNot_tipkey(rs.getInt("not_tipkey"));
                objNotaCIR.setNot_tipdes(rs.getString("not_tipdes"));
                objNotaCIR.setNot_key(rs.getInt("not_key"));
                objNotaCIR.setNot_id(rs.getString("not_id"));
                objNotaCIR.setEmp_id(rs.getInt("emp_id"));
                objNotaCIR.setSuc_id(rs.getInt("suc_id"));
                objNotaCIR.setCli_key(rs.getLong("cli_key"));
                objNotaCIR.setCli_id(rs.getString("cli_id"));
                objNotaCIR.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotaCIR.setNot_est(rs.getInt("not_est"));
                objNotaCIR.setNot_fecemi(rs.getDate("not_fecemi"));
                objNotaCIR.setNot_fecent(rs.getDate("not_fecent"));
                objNotaCIR.setNot_periodo(rs.getInt("not_periodo"));
                objNotaCIR.setNot_sit(rs.getInt("not_sit"));
                objNotaCIR.setNot_sitdes(rs.getString("not_sitdes").length() < 11 ? rs.getString("not_sitdes") : rs.getString("not_sitdes").substring(0, 11));
                objNotaCIR.setVen_key(rs.getInt("ven_key"));
                objNotaCIR.setVen_id(rs.getString("ven_id"));
                objNotaCIR.setVen_apenom(rs.getString("ven_apenom"));
                objNotaCIR.setMcam_key(rs.getInt("mcam_key"));
                objNotaCIR.setMcam_id(rs.getString("mcam_id"));
                objNotaCIR.setMcam_des(rs.getString("mcam_des"));
                objNotaCIR.setNot_tipnotaent(rs.getString("not_tipnotaent"));
                objNotaCIR.setNot_notaent(rs.getString("not_notaent"));
                objNotaCIR.setNot_tipnotasal(rs.getString("not_tipnotasal"));
                objNotaCIR.setNot_notasal(rs.getString("not_notasal"));
                objNotaCIR.setNot_nroreg(rs.getString("not_nroreg"));
                objNotaCIR.setNot_nrodep(rs.getString("not_nrodep"));
                objNotaCIR.setNot_usuadd(rs.getString("not_usuadd"));
                objNotaCIR.setNot_fecadd(rs.getTimestamp("not_fecadd"));
                objNotaCIR.setNot_pcadd(rs.getString("not_pcadd"));
                objNotaCIR.setNot_usumod(rs.getString("not_usumod"));
                objNotaCIR.setNot_fecmod(rs.getTimestamp("not_fecmod"));
                objNotaCIR.setNot_pcmod(rs.getString("not_pcmod"));
                objlstNotasCIR.add(objNotaCIR);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotasCIR.getSize() + " registro(s)");
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
        return objlstNotasCIR;
    }
}
