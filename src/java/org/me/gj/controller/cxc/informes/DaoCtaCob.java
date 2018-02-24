package org.me.gj.controller.cxc.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.informes.CliObservacion;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.CtaCobCab;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCtaCob {

    //Instancia de Objetos
    ListModelList<Cliente> objlstCliente;
    ListModelList<CtaCobCab> objlstCtaCab;
    ListModelList<CliObservacion> objlstCliObserva;
    CliObservacion objCliObserva;
    Cliente objCliente;
    CliFinanciero objCliFinanciero;
    CliTelefono objCliTelefono;
    CliGarantia objCliGarantia;
    CtaCobCab objCtaCobCab;

    //Variables Publicas
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "", P_TITULO="";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCtaCob.class);

    //Eventos Primarios - Transaccionales
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Cliente> listasClientesCxC(int emp_id, int suc_id, int i_caso) throws SQLException {
        String SQL_CLIENTE;
        if (i_caso == 0) {
            SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                    + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                    + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel,tb.tab_subdes cli_tipodoc_des , d.clidir_ref "
                    + "from ( "
                    + "select * from v_listacliente t "
                    + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + ") x,tclientes y,tcondicion c ,v_listatelefonocliente v ,(select * from ttabgen t where t.tab_cod =27) tb ,tclidir d "
                    + "where x.cli_id(+)=y.cli_id and  v.cli_key(+)=y.cli_key and v.cli_id(+)=y.cli_id and d.cli_key = y.cli_key and "
                    + "x.cli_key(+)=y.cli_key and  y.cli_est <>" + i_caso + " and d.clidir_id = x.clidir_id and d.clidir_est = 1  and "
                    + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' and  y.cli_tipodoc = tb.tab_id "
                    + "order by y.cli_key";
        } else {
            SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                    + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                    + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel,tb.tab_subdes cli_tipodoc_des , d.clidir_ref "
                    + "from ( "
                    + "select * from v_listacliente t "
                    + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + ") x,tclientes y,tcondicion c ,v_listatelefonocliente v ,(select * from ttabgen t where t.tab_cod =27) tb ,tclidir d "
                    + "where x.cli_id(+)=y.cli_id and  v.cli_key(+)=y.cli_key and v.cli_id(+)=y.cli_id and d.cli_key = y.cli_key and "
                    + "x.cli_key(+)=y.cli_key and y.cli_est =" + i_caso + " and d.clidir_id = x.clidir_id and d.clidir_est = 1  and "
                    + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' and  y.cli_tipodoc = tb.tab_id "
                    + "order by y.cli_key";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);
            objlstCliente = new ListModelList<Cliente>();
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setDia_vis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objCliente.setCli_tipodoc_des(rs.getString("cli_tipodoc_des"));
                objCliente.setCli_dirref(rs.getString("clidir_ref"));
                objlstCliente.add(objCliente);

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliente.getSize() + " registro(s)");
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
        return objlstCliente;
    }

    public Cliente listaClienteCtaCob(String cli) throws SQLException {
        String cliente ="";
        if (cli.length() == 7) {
            cliente = "v.clitel_telef1=" + cli + " and ";
        } else if (cli.length() == 8) {
            cliente = "y.cli_dni=" + cli + " and ";
        } else if (cli.length() == 11) {
            cliente = "y.cli_ruc=" + cli + " and ";
        } else {           
            cliente = "y.cli_key=" + cli + " and ";
        }

        String SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel,tb.tab_subdes cli_tipodoc_des , d.clidir_ref "
                + "from ( "
                + "select * from v_listacliente t "
                + "where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                + ") x,tclientes y,tcondicion c ,v_listatelefonocliente v ,(select * from ttabgen t where t.tab_cod =27) tb ,tclidir d "
                + "where x.cli_id(+)=y.cli_id and  v.cli_key(+)=y.cli_key and v.cli_id(+)=y.cli_id and d.cli_key = y.cli_key and "
                + "x.cli_key(+)=y.cli_key and " + cliente
                + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' and  y.cli_tipodoc = tb.tab_id "
                + "order by y.cli_key";

        objCliente = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);

            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setDia_vis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objCliente.setCli_tipodoc_des(rs.getString("cli_tipodoc_des"));
                objCliente.setCli_dirref(rs.getString("clidir_ref"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el codigo del cliente " + objCliente.getCli_id() + " ");
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
        return objCliente;
    }

    public CliTelefono listaTelefonoCtaCob(long clikey) throws SQLException {

        String SQL_TELEFONO;
        SQL_TELEFONO = "select * from v_listatelefonocliente t where t.clitel_est <> 0 and t.cli_key =" + clikey + " order by t.clitel_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TELEFONO);
            objCliTelefono = null;
            while (rs.next()) {
                objCliTelefono = new CliTelefono();
                objCliTelefono.setClitel_id(rs.getLong("clitel_id"));
                objCliTelefono.setCli_id(rs.getString("cli_id"));
                objCliTelefono.setCli_key(rs.getLong("cli_key"));
                objCliTelefono.setClitel_telef1(rs.getLong("clitel_telef1"));
                objCliTelefono.setClitel_telef2(rs.getLong("clitel_telef2"));
                objCliTelefono.setClitel_movil(rs.getLong("clitel_movil"));
                objCliTelefono.setClitel_est(rs.getInt("clitel_est"));
                objCliTelefono.setClitel_usuadd(rs.getString("clitel_usuadd"));
                objCliTelefono.setClitel_fecadd(rs.getTimestamp("clitel_fecadd"));
                objCliTelefono.setClitel_usumod(rs.getString("clitel_usumod"));
                objCliTelefono.setClitel_fecmod(rs.getTimestamp("clitel_fecmod"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el codigo del telefono" + objCliTelefono.getClitel_id() + " segun codigo del cliente" + objCliTelefono.getCli_id());
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
        return objCliTelefono;
    }

    public CliFinanciero listaFinancieroCtaCob(long clikey) throws SQLException {

        String SQL_FINANCIERO;
        SQL_FINANCIERO = "select t.*, tc.tab_subdes cli_categ_des  from v_listafinancierocliente t , (select * from ttabgen c where c.tab_cod =26) tc where t.clifin_est <> 0 and t.cli_key =" + clikey + "  and "
                + "t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id =" + objUsuCredential.getCodsuc() + " and t.clifin_categ = tc.tab_id  order by t.clifin_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FINANCIERO);
            objCliFinanciero = null;
            while (rs.next()) {
                objCliFinanciero = new CliFinanciero();
                objCliFinanciero.setClifin_id(rs.getLong("clifin_id"));
                objCliFinanciero.setClifin_limcred(rs.getLong("clifin_limcred"));
                objCliFinanciero.setClifin_limdoc(rs.getInt("clifin_limdoc"));
                objCliFinanciero.setClifin_est(rs.getInt("clifin_est"));
                objCliFinanciero.setClifin_usuadd(rs.getString("clifin_usuadd"));
                objCliFinanciero.setClifin_fecadd(rs.getTimestamp("clifin_fecadd"));
                objCliFinanciero.setClifin_usumod(rs.getString("clifin_usumod"));
                objCliFinanciero.setClifin_fecmod(rs.getTimestamp("clifin_fecmod"));
                objCliFinanciero.setCli_id(rs.getString("cli_id"));
                objCliFinanciero.setCli_key(rs.getLong("cli_key"));
                objCliFinanciero.setSuc_id(rs.getInt("suc_id"));
                objCliFinanciero.setEmp_id(rs.getInt("emp_id"));
                objCliFinanciero.setClifin_categ(rs.getInt("clifin_categ"));
                objCliFinanciero.setClifin_categ_des(rs.getString("cli_categ_des"));
                objCliFinanciero.setClifin_saldo(rs.getDouble("saldo_cred"));
                objCliFinanciero.setClifin_docemi(rs.getInt("saldo_doc"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el codigo de financiero" + objCliFinanciero.getClifin_id() + " segun codigo del cliente " + objCliFinanciero.getCli_id());
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
        return objCliFinanciero;
    }

    public CliGarantia BusquedaGarantiaxCliente(long cli_id, int emp_id, int suc_id) throws SQLException {
        String SQL_GARANTIA;
        SQL_GARANTIA = "select * from v_listagarantiacliente t where t.cligar_est <> 0 and t.cli_key= " + cli_id + " and t.emp_id = " + emp_id + " "
                + "and t.suc_id = " + suc_id + " order by t.cligar_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GARANTIA);
            objCliGarantia = null;
            while (rs.next()) {
                objCliGarantia = new CliGarantia();
                objCliGarantia.setCligar_id(rs.getLong(1));
                objCliGarantia.setCli_id(rs.getString(2));
                objCliGarantia.setCli_key(rs.getLong(3));
                objCliGarantia.setSuc_id(rs.getInt(4));
                objCliGarantia.setEmp_id(rs.getInt(5));
                objCliGarantia.setCligar_garantia(rs.getString(6));
                objCliGarantia.setCligar_monto(rs.getLong(7));
                objCliGarantia.setCligar_obs(rs.getString(8));
                objCliGarantia.setCligar_est(rs.getInt(9));
                objCliGarantia.setCligar_usuadd(rs.getString(10));
                objCliGarantia.setCligar_fecadd(rs.getTimestamp(11));
                objCliGarantia.setCligar_usumod(rs.getString(12));
                objCliGarantia.setCligar_fecmod(rs.getTimestamp(13));
                
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha encontrado la cliente "+objCliGarantia.getCli_id()+ " con el codigo de garantia   " + objCliGarantia.getCligar_garantia() );
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
        return objCliGarantia;
    }
    
    public ListModelList<Cliente> busquedaClientes(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_CLIENTE = "";
        if (i_estado == 3) { // Activo e Inactivo                   
        	if (seleccion == 2) {
            SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                    + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                    + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel,tb.tab_subdes cli_tipodoc_des , d.clidir_ref "
                    + "from ( "
                    + "select * from v_listacliente t "
                    + "where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                    + ") x,tclientes y,tcondicion c ,v_listatelefonocliente v ,(select * from ttabgen t where t.tab_cod =27) tb ,tclidir d "
                    + " where x.cli_id(+)=y.cli_id and  v.cli_key(+)=y.cli_key and v.cli_id(+)=y.cli_id and d.cli_key = y.cli_key and "
                    + " x.cli_key(+)=y.cli_key and y.cli_est <>0 and d.clidir_id = x.clidir_id and d.clidir_est = 1  and " 
                    + " c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' and  y.cli_tipodoc = tb.tab_id and "
                    + " y.cli_est<> 0 and y.cli_razsoc like '" + consulta + "'  "                    
                    + "order by y.cli_key";
        	}
        }

        objlstCliente = new ListModelList<Cliente>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setDia_vis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objCliente.setCli_tipodoc_des(rs.getString("cli_tipodoc_des"));
                objCliente.setCli_dirref(rs.getString("clidir_ref"));
                objlstCliente.add(objCliente);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }

        return objlstCliente;
    }

    public String ingresarCliObservacion(CliObservacion objCliObservacion) throws SQLException {
        int emp_id = objCliObservacion.getEmp_id();
        int suc_id = objCliObservacion.getSuc_id();
        String cli_id = objCliObservacion.getCli_id();
        String cli_observa = objCliObservacion.getCli_observa();
        String cli_usuadd = objCliObservacion.getCli_usuadd();
        String cli_tipobs = objCliObservacion.getCli_tipobs();
        String SQL_INSERTAR_CANAL = "{call pack_tclientes.p_ingresarCliObservacion(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CANAL);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setString(3, cli_id);
            cst.setString(4, cli_observa);
            cst.setString(5, cli_usuadd);
            cst.setString(6, cli_tipobs);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripcion " + cli_observa);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_msg;
    }

    public ListModelList<CliObservacion> listaObservacionCliente(int emp_id, int suc_id, String nrocliente) throws SQLException {
        String SQL_OBSERVA_CLIENTE = " select * from tcli_observa t"
                + " where t.cli_id=" + nrocliente + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + " order by t.cli_nroope desc";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_OBSERVA_CLIENTE);
            objlstCliObserva = new ListModelList<CliObservacion>();
            while (rs.next()) {
                objCliObserva = new CliObservacion();
                objCliObserva.setEmp_id(rs.getInt("emp_id"));
                objCliObserva.setSuc_id(rs.getInt("suc_id"));
                objCliObserva.setCli_id(rs.getString("cli_id"));
                objCliObserva.setCli_nroope(rs.getInt("cli_nroope"));
                objCliObserva.setCli_observa(rs.getString("cli_observa"));
                objCliObserva.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliObserva.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliObserva.setCli_usumod(rs.getString("cli_usumod"));
                objCliObserva.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliObserva.setCli_tipobs(rs.getString("cli_tipobs"));
                objlstCliObserva.add(objCliObserva);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
        return objlstCliObserva;
    }
    
    
    public CtaCobCab obtenerDatosCtaCabxClientes(String nrocliente) throws SQLException{
    	String SQL_LISTA_CTACOBCAB = " select * from V_LISTACTACOB_CAB t"
                + " where t.cli_id=" + nrocliente 
                + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                + " order by t.ctacob_key ";
    	try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
            objCtaCobCab = null;
            while (rs.next()) {
                objCtaCobCab = new CtaCobCab();
                objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
                objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
                objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
                objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
                objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
                objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
                objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
                objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
                objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
                objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
                objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
                objCtaCobCab.setCli_id(rs.getString("cli_id"));
                objCtaCobCab.setCli_des(rs.getString("cli_des"));
                objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
                objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
                objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
                objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
                objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
                objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
                objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
                objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
                objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
                objCtaCobCab.setIdcanal(rs.getString("idcanal"));
                objCtaCobCab.setIdmesa(rs.getString("idmesa"));
                objCtaCobCab.setIdruta(rs.getString("idruta"));
                objCtaCobCab.setZon_id(rs.getString("zon_id"));
                objCtaCobCab.setZon_des(rs.getString("zon_des"));
                objCtaCobCab.setSup_id(rs.getString("sup_id"));
                objCtaCobCab.setVen_id(rs.getString("ven_id"));
                objCtaCobCab.setTrans_id(rs.getString("trans_id"));
                objCtaCobCab.setTrans_des(rs.getString("trans_des"));
                objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
                objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
                objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
                objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
                objCtaCobCab.setCon_id(rs.getString("con_id"));
                objCtaCobCab.setCon_des(rs.getString("con_des"));
                objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
                objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
                objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
                objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
                objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
                objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
                objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
                objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
                objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
                objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
                objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
                objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
                objCtaCobCab.setLp_id(rs.getString("lp_id"));
                objCtaCobCab.setLp_des(rs.getString("lp_des"));
                objCtaCobCab.setVen_des(rs.getString("ven_des"));
                objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
                objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
                objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
                objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
                objCtaCobCab.setNroiden(rs.getString("nroiden"));
                
                
               
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha encontrado al cliente con codigo " + objCtaCobCab.getCli_id()== null?"0":objCtaCobCab.getCli_id() + " registro(s)");
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
        return objCtaCobCab; 
    }
    
    public ListModelList<CtaCobCab> listaCtaCabxCliente(String nrocliente) throws SQLException{  	
    	 String SQL_LISTA_CTACOBCAB = " select * from V_LISTACTACOB_CAB t"
                 + " where t.cli_id=" + nrocliente 
                 + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                 + " order by t.ctacob_key ";
         try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
             objlstCtaCab = new ListModelList<CtaCobCab>();
             while (rs.next()) {
                 objCtaCobCab = new CtaCobCab();
                 objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
                 objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
                 objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
                 objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
                 objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
                 objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
                 objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
                 objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
                 objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
                 objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
                 objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
                 objCtaCobCab.setCli_id(rs.getString("cli_id"));
                 objCtaCobCab.setCli_des(rs.getString("cli_des"));
                 objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
                 objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
                 objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
                 objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
                 objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
                 objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
                 objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
                 objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
                 objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
                 objCtaCobCab.setIdcanal(rs.getString("idcanal"));
                 objCtaCobCab.setIdmesa(rs.getString("idmesa"));
                 objCtaCobCab.setIdruta(rs.getString("idruta"));
                 objCtaCobCab.setZon_id(rs.getString("zon_id"));
                 objCtaCobCab.setZon_des(rs.getString("zon_des"));
                 objCtaCobCab.setSup_id(rs.getString("sup_id"));
                 objCtaCobCab.setVen_id(rs.getString("ven_id"));
                 objCtaCobCab.setTrans_id(rs.getString("trans_id"));
                 objCtaCobCab.setTrans_des(rs.getString("trans_des"));
                 objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
                 objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
                 objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
                 objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
                 objCtaCobCab.setCon_id(rs.getString("con_id"));
                 objCtaCobCab.setCon_des(rs.getString("con_des"));
                 objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
                 objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
                 objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
                 objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
                 objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
                 objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
                 objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
                 objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
                 objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
                 objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
                 objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
                 objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
                 objCtaCobCab.setLp_id(rs.getString("lp_id"));
                 objCtaCobCab.setLp_des(rs.getString("lp_des"));
                 objCtaCobCab.setVen_des(rs.getString("ven_des"));
                 objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
                 objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
                 objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
                 objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
                 objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
                 objCtaCobCab.setNroiden(rs.getString("nroiden"));
                 objlstCtaCab.add(objCtaCobCab);
             }
             LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
         return objlstCtaCab;  	
    }
    
    public ListModelList<CtaCobCab> listaCtaCobSeisMeses(String nrocliente, String fecha) throws SQLException{  	
    	String s_validafecha = fecha.isEmpty()?"": " and t.ctacob_fecemi between to_date('"+fecha+"','dd/MM/yyyy') and to_date('"+Utilitarios.hoyAsString()+"','dd/MM/yyyy') ";
    	
    	String SQL_LISTA_CTACOBCAB = " select * from V_LISTACTACOB_CAB t"
                + " where t.cli_id=" + nrocliente  + s_validafecha
                + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                + " order by t.ctacob_key ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
            objlstCtaCab = new ListModelList<CtaCobCab>();
            while (rs.next()) {
                objCtaCobCab = new CtaCobCab();
                objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
                objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
                objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
                objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
                objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
                objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
                objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
                objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
                objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
                objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
                objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
                objCtaCobCab.setCli_id(rs.getString("cli_id"));
                objCtaCobCab.setCli_des(rs.getString("cli_des"));
                objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
                objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
                objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
                objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
                objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
                objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
                objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
                objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
                objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
                objCtaCobCab.setIdcanal(rs.getString("idcanal"));
                objCtaCobCab.setIdmesa(rs.getString("idmesa"));
                objCtaCobCab.setIdruta(rs.getString("idruta"));
                objCtaCobCab.setZon_id(rs.getString("zon_id"));
                objCtaCobCab.setZon_des(rs.getString("zon_des"));
                objCtaCobCab.setSup_id(rs.getString("sup_id"));
                objCtaCobCab.setVen_id(rs.getString("ven_id"));
                objCtaCobCab.setTrans_id(rs.getString("trans_id"));
                objCtaCobCab.setTrans_des(rs.getString("trans_des"));
                objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
                objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
                objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
                objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
                objCtaCobCab.setCon_id(rs.getString("con_id"));
                objCtaCobCab.setCon_des(rs.getString("con_des"));
                objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
                objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
                objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
                objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
                objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
                objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
                objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
                objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
                objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
                objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
                objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
                objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
                objCtaCobCab.setLp_id(rs.getString("lp_id"));
                objCtaCobCab.setLp_des(rs.getString("lp_des"));
                objCtaCobCab.setVen_des(rs.getString("ven_des"));
                objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
                objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
                objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
                objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
                objCtaCobCab.setNroiden(rs.getString("nroiden"));
                objlstCtaCab.add(objCtaCobCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
        return objlstCtaCab;  	
   }
    
    public CtaCobCab listaCtaCabxNroDocumento(String nrodoc, int tipdoc) throws SQLException{  	
   	 String SQL_LISTA_CTACOBCAB = " select * from V_LISTACTACOB_CAB t"
                + " where t.ctacob_nrodoc=" + nrodoc 
                + " and t.ctacob_tipdoc like '"+tipdoc+"' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                + " order by t.ctacob_key ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
            objCtaCobCab = null;
            while (rs.next()) {
                objCtaCobCab = new CtaCobCab();
                objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
                objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
                objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
                objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
                objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
                objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
                objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
                objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
                objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
                objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
                objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
                objCtaCobCab.setCli_id(rs.getString("cli_id"));
                objCtaCobCab.setCli_des(rs.getString("cli_des"));
                objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
                objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
                objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
                objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
                objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
                objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
                objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
                objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
                objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
                objCtaCobCab.setIdcanal(rs.getString("idcanal"));
                objCtaCobCab.setIdmesa(rs.getString("idmesa"));
                objCtaCobCab.setIdruta(rs.getString("idruta"));
                objCtaCobCab.setZon_id(rs.getString("zon_id"));
                objCtaCobCab.setZon_des(rs.getString("zon_des"));
                objCtaCobCab.setSup_id(rs.getString("sup_id"));
                objCtaCobCab.setVen_id(rs.getString("ven_id"));
                objCtaCobCab.setTrans_id(rs.getString("trans_id"));
                objCtaCobCab.setTrans_des(rs.getString("trans_des"));
                objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
                objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
                objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
                objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
                objCtaCobCab.setCon_id(rs.getString("con_id"));
                objCtaCobCab.setCon_des(rs.getString("con_des"));
                objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
                objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
                objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
                objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
                objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
                objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
                objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
                objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
                objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
                objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
                objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
                objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
                objCtaCobCab.setLp_id(rs.getString("lp_id"));
                objCtaCobCab.setLp_des(rs.getString("lp_des"));
                objCtaCobCab.setVen_des(rs.getString("ven_des"));
                objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
                objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
                objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
                objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
                objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
                objCtaCobCab.setNroiden(rs.getString("nroiden"));
               
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha encontrado al cliente con codigo " + objCtaCobCab.getCli_id()== null?"0":objCtaCobCab.getCli_id() + " registro(s)");
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
        return objCtaCobCab;  	
   }
    
    public ParametrosSalida actualizaGlosa(String nrodoc, String glosa) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_GLOSA = "{call pack_tctacob_cab.p_actualizaglosa(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_GLOSA);
            cst.clearParameters();
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_nrodoc", nrodoc);
            cst.setString("c_glosa",  glosa); 
            cst.setString("c_usumod",  objUsuCredential.getCuenta()); 
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con el numero de documento " + nrodoc);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

	public ParametrosSalida actualizaLimCredDoc(String cliid, Double limcred, Integer limdoc) throws SQLException{
		 String SQL_ACTUALIZAR_LIMCREDDOC = "{call pack_tctacob_cab.p_actualizalimcreddoc(?,?,?,?,?,?,?,?)}";
	        try {
	            con = new ConectaBD().conectar();
	            cst = con.prepareCall(SQL_ACTUALIZAR_LIMCREDDOC);
	            cst.clearParameters();
	            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
	            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
	            cst.setString("c_cliid", cliid);
	            cst.setDouble("n_limcred", limcred);
	            cst.setInt("n_limdoc",  limdoc); 
	            cst.setString("c_usumod",  objUsuCredential.getCuenta()); 
	            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
	            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
	            cst.execute();
	            s_msg = cst.getString("c_msg");
	            i_flagErrorBD = cst.getInt("n_Flag");
	            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con al cliente "+cliid + " con Lim.Cred "+limcred +" y con Lim.Doc "+limdoc);
	        } catch (SQLException e) {
	            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
	            i_flagErrorBD = 1;
	            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
	        } catch (NullPointerException e) {
	            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
	            i_flagErrorBD = 1;
	            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
	        } finally {
	            if (con != null) {
	                con.close();
	                cst.close();
	            }
	        }
	        return new ParametrosSalida(i_flagErrorBD, s_msg);
	    }
	
	
	public ListModelList<CtaCobCab> BusquedaFacturaBoletaCtaCobxTipDoc(String periodo, String fecha, String supervisor , String vendedor, int tipdoc) throws SQLException{  	
		
			String s_validatipdoc = tipdoc ==  0 ?"": " and t.ctacob_tipdoc = '"+tipdoc+"' "; 
			String s_validaperiodo = periodo.isEmpty()? " and t.ctacob_periodo like '%%' ": " and t.ctacob_periodo like '%"+periodo+"%' " ;
			String s_validafecha = fecha.isEmpty() ? "":" and t.ctacob_fecemi = to_date('"+fecha+"','dd/MM/yyyy') " ;
			
			String SQL_LISTA_CTACOBCAB = " select t.* from V_LISTACTACOB_CAB t "
					+" where t.sup_id like '"+supervisor+"' and t.ven_id like '"+vendedor+"' "									
					+" and t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" "
					+ s_validatipdoc + s_validaperiodo+ s_validafecha
					+" order by t.ctacob_fecemi ";	
		
		
		    try {
	        con = new ConectaBD().conectar();
	        st = con.createStatement();
	        rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
	        objlstCtaCab = new ListModelList<CtaCobCab>();
	        while (rs.next()) {
	            objCtaCobCab = new CtaCobCab();
	            objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
	            objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
	            objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
	            objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
	            objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
	            objCtaCobCab.setCtacob_tipdocdesdes(rs.getString("ctacob_tipdocdes"));
	            objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
	            objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
	            objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
	            objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
	            objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
	            objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
	            objCtaCobCab.setCli_id(rs.getString("cli_id"));
	            objCtaCobCab.setCli_des(rs.getString("cli_des"));
	            objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
	            objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
	            objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
	            objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
	            objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
	            objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
	            objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
	            objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
	            objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
	            objCtaCobCab.setIdcanal(rs.getString("idcanal"));
	            objCtaCobCab.setIdmesa(rs.getString("idmesa"));
	            objCtaCobCab.setIdruta(rs.getString("idruta"));
	            objCtaCobCab.setZon_id(rs.getString("zon_id"));
	            objCtaCobCab.setZon_des(rs.getString("zon_des"));
	            objCtaCobCab.setSup_id(rs.getString("sup_id"));
	            objCtaCobCab.setVen_id(rs.getString("ven_id"));
	            objCtaCobCab.setTrans_id(rs.getString("trans_id"));
	            objCtaCobCab.setTrans_des(rs.getString("trans_des"));
	            objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
	            objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
	            objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
	            objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
	            objCtaCobCab.setCon_id(rs.getString("con_id"));
	            objCtaCobCab.setCon_des(rs.getString("con_des"));
	            objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
	            objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
	            objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
	            objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
	            objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
	            objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
	            objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
	            objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
	            objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
	            objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
	            objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
	            objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
	            objCtaCobCab.setLp_id(rs.getString("lp_id"));
	            objCtaCobCab.setLp_des(rs.getString("lp_des"));
	            objCtaCobCab.setSup_des(rs.getString("sup_des"));
	            objCtaCobCab.setVen_des(rs.getString("ven_des"));
	            objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
	            objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
	            objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
	            objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
	            objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
	            objCtaCobCab.setNroiden(rs.getString("nroiden"));
	            objCtaCobCab.setTipvenid(rs.getString("tipvenid"));
	            objCtaCobCab.setDiavisdes(rs.getString("diavisdes"));
	            objCtaCobCab.setChof_id(rs.getString("chof_id"));
	            objCtaCobCab.setChof_des(rs.getString("chof_des"));
	            objCtaCobCab.setTrans_placa(rs.getString("trans_placa"));
	            objCtaCobCab.setLimcred(rs.getDouble("limcred"));
	            objCtaCobCab.setLimdoc(rs.getString("limdoc"));
	            
	            objlstCtaCab.add(objCtaCobCab);
	        }
	        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
	    return objlstCtaCab;  	
	}
	
	public ListModelList<CtaCobCab> BusquedaFacturaBoletaCtaCobxTipoDocSerie(String periodo, String fecha, String supervisor , String vendedor, int tipdoc , String serie) throws SQLException{  	
		
		String s_validatipdoc = tipdoc == 0 ?"": " and t.ctacob_tipdoc = '"+tipdoc+"' "; 
		String s_validaserie = serie.isEmpty() ?"": " and t.serie = '"+ serie+"' ";
		String s_validaperiodo = periodo.isEmpty()? " and t.ctacob_periodo like '%%' ": " and t.ctacob_periodo like '%"+periodo+"%' " ;
		String s_validafecha = fecha.isEmpty() ? "":" and t.ctacob_fecemi = to_date('"+fecha+"','dd/MM/yyyy') " ;
		
		String SQL_LISTA_CTACOBCAB = " select t.* from V_LISTACTACOB_CAB t "
				+" where t.sup_id like '"+supervisor+"' and t.ven_id like '"+vendedor+"' "									
				+" and t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" "
				+ s_validatipdoc + s_validaserie + s_validaperiodo+ s_validafecha 
				+" order by t.ctacob_fecemi ";	
	
	
	    try {
        con = new ConectaBD().conectar();
        st = con.createStatement();
        rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
        objlstCtaCab = new ListModelList<CtaCobCab>();
        while (rs.next()) {
            objCtaCobCab = new CtaCobCab();
            objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
            objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
            objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
            objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
            objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
            objCtaCobCab.setCtacob_tipdocdesdes(rs.getString("ctacob_tipdocdes"));
            objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
            objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
            objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
            objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
            objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
            objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
            objCtaCobCab.setCli_id(rs.getString("cli_id"));
            objCtaCobCab.setCli_des(rs.getString("cli_des"));
            objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
            objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
            objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
            objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
            objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
            objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
            objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
            objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
            objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
            objCtaCobCab.setIdcanal(rs.getString("idcanal"));
            objCtaCobCab.setIdmesa(rs.getString("idmesa"));
            objCtaCobCab.setIdruta(rs.getString("idruta"));
            objCtaCobCab.setZon_id(rs.getString("zon_id"));
            objCtaCobCab.setZon_des(rs.getString("zon_des"));
            objCtaCobCab.setSup_id(rs.getString("sup_id"));
            objCtaCobCab.setVen_id(rs.getString("ven_id"));
            objCtaCobCab.setTrans_id(rs.getString("trans_id"));
            objCtaCobCab.setTrans_des(rs.getString("trans_des"));
            objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
            objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
            objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
            objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
            objCtaCobCab.setCon_id(rs.getString("con_id"));
            objCtaCobCab.setCon_des(rs.getString("con_des"));
            objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
            objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
            objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
            objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
            objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
            objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
            objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
            objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
            objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
            objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
            objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
            objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
            objCtaCobCab.setLp_id(rs.getString("lp_id"));
            objCtaCobCab.setLp_des(rs.getString("lp_des"));
            objCtaCobCab.setSup_des(rs.getString("sup_des"));
            objCtaCobCab.setVen_des(rs.getString("ven_des"));
            objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
            objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
            objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
            objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
            objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
            objCtaCobCab.setNroiden(rs.getString("nroiden"));
            objCtaCobCab.setTipvenid(rs.getString("tipvenid"));
            objCtaCobCab.setDiavisdes(rs.getString("diavisdes"));
            objCtaCobCab.setChof_id(rs.getString("chof_id"));
            objCtaCobCab.setChof_des(rs.getString("chof_des"));
            objCtaCobCab.setTrans_placa(rs.getString("trans_placa"));
            objCtaCobCab.setLimcred(rs.getDouble("limcred"));
            objCtaCobCab.setLimdoc(rs.getString("limdoc"));
            
            objlstCtaCab.add(objCtaCobCab);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
    return objlstCtaCab;  	
}
	
	public ListModelList<CtaCobCab> BusquedaFacturaBoletaCtaCob(int canal , String fecha) throws SQLException{  	
			String s_validacanal = canal == 0 ?"":" and t.idcanal = '" + Utilitarios.lpad(String.valueOf(canal),3,"0")+"' ";
			String s_validafecha = fecha.isEmpty() ? "" :" and t.ctacob_fecent = to_date('"+fecha+"','dd/MM/yyyy') " ;
				
				
			String SQL_LISTA_CTACOBCAB = " select t.* from V_LISTACTACOB_CAB t "
						+" where  t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" "
						+  s_validacanal + s_validafecha
						+" order by t.ctacob_fecent ";
		
		
		    try {
	        con = new ConectaBD().conectar();
	        st = con.createStatement();
	        rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
	        objlstCtaCab = new ListModelList<CtaCobCab>();
	        while (rs.next()) {
	            objCtaCobCab = new CtaCobCab();
	            objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
	            objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
	            objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
	            objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
	            objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
	            objCtaCobCab.setCtacob_tipdocdesdes(rs.getString("ctacob_tipdocdes"));
	            objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
	            objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
	            objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
	            objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
	            objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
	            objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
	            objCtaCobCab.setCli_id(rs.getString("cli_id"));
	            objCtaCobCab.setCli_des(rs.getString("cli_des"));
	            objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
	            objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
	            objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
	            objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
	            objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
	            objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
	            objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
	            objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
	            objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
	            objCtaCobCab.setIdcanal(rs.getString("idcanal"));
	            objCtaCobCab.setIdmesa(rs.getString("idmesa"));
	            objCtaCobCab.setIdruta(rs.getString("idruta"));
	            objCtaCobCab.setZon_id(rs.getString("zon_id"));
	            objCtaCobCab.setZon_des(rs.getString("zon_des"));
	            objCtaCobCab.setSup_id(rs.getString("sup_id"));
	            objCtaCobCab.setVen_id(rs.getString("ven_id"));
	            objCtaCobCab.setTrans_id(rs.getString("trans_id"));
	            objCtaCobCab.setTrans_des(rs.getString("trans_des"));
	            objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
	            objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
	            objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
	            objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
	            objCtaCobCab.setCon_id(rs.getString("con_id"));
	            objCtaCobCab.setCon_des(rs.getString("con_des"));
	            objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
	            objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
	            objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
	            objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
	            objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
	            objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
	            objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
	            objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
	            objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
	            objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
	            objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
	            objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
	            objCtaCobCab.setLp_id(rs.getString("lp_id"));
	            objCtaCobCab.setLp_des(rs.getString("lp_des"));
	            objCtaCobCab.setSup_des(rs.getString("sup_des"));
	            objCtaCobCab.setVen_des(rs.getString("ven_des"));
	            objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
	            objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
	            objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
	            objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
	            objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
	            objCtaCobCab.setNroiden(rs.getString("nroiden"));
	            objCtaCobCab.setTipvenid(rs.getString("tipvenid"));
	            objCtaCobCab.setDiavisdes(rs.getString("diavisdes"));
	            objCtaCobCab.setChof_id(rs.getString("chof_id"));
	            objCtaCobCab.setChof_des(rs.getString("chof_des"));
	            objCtaCobCab.setTrans_placa(rs.getString("trans_placa"));
	            objCtaCobCab.setLimcred(rs.getDouble("limcred"));
	            objCtaCobCab.setLimdoc(rs.getString("limdoc"));
	            objCtaCobCab.setHora_id(rs.getString("hora_id"));
	            objCtaCobCab.setHora_des(rs.getString("hora_des"));
	            objCtaCobCab.setNroreg(rs.getString("nroreg"));
	            objCtaCobCab.setTip_dev(rs.getString("tip_dev"));
	            objCtaCobCab.setMot_devid(rs.getString("mot_devid"));
	            objCtaCobCab.setMot_devdes(rs.getString("mot_devdes"));
	            
	            
	            objlstCtaCab.add(objCtaCobCab);
	        }
	        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
	    return objlstCtaCab;  	
	}
	
	
	public ListModelList<CtaCobCab> BusquedaFacturaBoletaxFecha(String fecha) throws SQLException{  	
		
		String s_validafecha = fecha.isEmpty() ? "":" and t.ctacob_fecent = to_date('"+fecha+"','dd/MM/yyyy') " ;
		
		String SQL_LISTA_CTACOBCAB = " select t.* from V_LISTACTACOB_CAB t "
				+" where t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" "
				+ s_validafecha
				+" order by t.ctacob_fecemi ";		
	
	    try {
        con = new ConectaBD().conectar();
        st = con.createStatement();
        rs = st.executeQuery(SQL_LISTA_CTACOBCAB);
        objlstCtaCab = new ListModelList<CtaCobCab>();
        while (rs.next()) {
            objCtaCobCab = new CtaCobCab();
            objCtaCobCab.setEmp_id(rs.getInt("emp_id"));
            objCtaCobCab.setSuc_id(rs.getInt("suc_id")); 
            objCtaCobCab.setCtacob_key(rs.getString("ctacob_key"));
            objCtaCobCab.setCtacob_periodo(rs.getString("ctacob_periodo"));
            objCtaCobCab.setCtacob_tipdoc(rs.getString("ctacob_tipdoc"));
            objCtaCobCab.setCtacob_tipdocdesdes(rs.getString("ctacob_tipdocdes"));
            objCtaCobCab.setCtacob_tipdocdes(rs.getString("ctacob_tipdocdes").length()<3?rs.getString("ctacob_tipdocdes"): rs.getString("ctacob_tipdocdes").substring(0, 3));
            objCtaCobCab.setCtacob_nrodoc(rs.getString("ctacob_nrodoc"));
            objCtaCobCab.setCtacob_estado(rs.getInt("ctacob_estado"));
            objCtaCobCab.setCtacob_estadodes(rs.getString("ctacob_estadodes"));
            objCtaCobCab.setCtacob_tipmov(rs.getString("ctacob_tipmov"));
            objCtaCobCab.setCtacob_tipmovdes(rs.getString("ctacob_tipmovdes"));                 
            objCtaCobCab.setCli_id(rs.getString("cli_id"));
            objCtaCobCab.setCli_des(rs.getString("cli_des"));
            objCtaCobCab.setClidir_id(rs.getInt("clidir_id"));
            objCtaCobCab.setClidir_des(rs.getString("clidir_des"));
            objCtaCobCab.setCtacob_fecemi(rs.getDate("ctacob_fecemi"));
            objCtaCobCab.setCtacob_fecent(rs.getDate("ctacob_fecent"));
            objCtaCobCab.setCtacob_fecrec(rs.getDate("ctacob_fecrec"));
            objCtaCobCab.setCtacob_fecven(rs.getDate("ctacob_fecven"));
            objCtaCobCab.setCtacob_fecumov(rs.getDate("ctacob_fecumov"));
            objCtaCobCab.setCtacob_fecanu(rs.getDate("ctacob_fecanu"));  
            objCtaCobCab.setCtacob_diapla(rs.getInt("ctacob_diapla"));                 
            objCtaCobCab.setIdcanal(rs.getString("idcanal"));
            objCtaCobCab.setIdmesa(rs.getString("idmesa"));
            objCtaCobCab.setIdruta(rs.getString("idruta"));
            objCtaCobCab.setZon_id(rs.getString("zon_id"));
            objCtaCobCab.setZon_des(rs.getString("zon_des"));
            objCtaCobCab.setSup_id(rs.getString("sup_id"));
            objCtaCobCab.setVen_id(rs.getString("ven_id"));
            objCtaCobCab.setTrans_id(rs.getString("trans_id"));
            objCtaCobCab.setTrans_des(rs.getString("trans_des"));
            objCtaCobCab.setCtacob_simmon(rs.getString("ctacob_simmon"));
            objCtaCobCab.setCtacob_mon(rs.getString("ctacob_mon"));
            objCtaCobCab.setCtacob_mondes(rs.getString("ctacob_mondes"));
            objCtaCobCab.setCtacob_tipcam(rs.getDouble("ctacob_tipcam"));
            objCtaCobCab.setCon_id(rs.getString("con_id"));
            objCtaCobCab.setCon_des(rs.getString("con_des"));
            objCtaCobCab.setCtacob_glosa(rs.getString("ctacob_glosa"));
            objCtaCobCab.setCtacob_total(rs.getDouble("ctccob_total"));
            objCtaCobCab.setCtacob_saldo(rs.getDouble("ctccob_saldo"));
            objCtaCobCab.setCtacob_sit(rs.getInt("ctacob_sit"));
            objCtaCobCab.setCtacob_pen(rs.getString("ctacob_pen"));
            objCtaCobCab.setCtacob_pendes(rs.getString("ctacob_pendes"));
            objCtaCobCab.setCtacob_inc(rs.getInt("ctacob_inc"));
            objCtaCobCab.setCtacob_percom(rs.getString("ctacob_percom"));                 
            objCtaCobCab.setCtacob_usuadd(rs.getString("ctacob_usuadd"));
            objCtaCobCab.setCtacob_fecadd(rs.getTimestamp("ctacob_fecadd"));
            objCtaCobCab.setCtacob_usumod(rs.getString("ctacob_usumod"));
            objCtaCobCab.setCtacob_fecmod(rs.getTimestamp("ctacob_fecmod")); 
            objCtaCobCab.setLp_id(rs.getString("lp_id"));
            objCtaCobCab.setLp_des(rs.getString("lp_des"));
            objCtaCobCab.setSup_des(rs.getString("sup_des"));
            objCtaCobCab.setVen_des(rs.getString("ven_des"));
            objCtaCobCab.setCli_ruc(rs.getString("cli_ruc"));
            objCtaCobCab.setDoc_emi(rs.getString("doc_emi"));
            objCtaCobCab.setSaldo_total(rs.getDouble("saldo_total"));
            objCtaCobCab.setPcab_nroped(rs.getString("pcab_nroped"));
            objCtaCobCab.setNescab_id(rs.getString("nescab_id"));
            objCtaCobCab.setNroiden(rs.getString("nroiden"));
            objCtaCobCab.setTipvenid(rs.getString("tipvenid"));
            objCtaCobCab.setDiavisdes(rs.getString("diavisdes"));
            objCtaCobCab.setChof_id(rs.getString("chof_id"));
            objCtaCobCab.setChof_des(rs.getString("chof_des"));
            objCtaCobCab.setTrans_placa(rs.getString("trans_placa"));
            objCtaCobCab.setLimcred(rs.getDouble("limcred"));
            objCtaCobCab.setLimdoc(rs.getString("limdoc"));
            objCtaCobCab.setHora_id(rs.getString("hora_id"));
            objCtaCobCab.setHora_des(rs.getString("hora_des"));
            objCtaCobCab.setNroreg(rs.getString("nroreg"));
            objCtaCobCab.setTip_dev(rs.getString("tip_dev"));
            objCtaCobCab.setMot_devid(rs.getString("mot_devid"));
            objCtaCobCab.setMot_devdes(rs.getString("mot_devdes"));
            objlstCtaCab.add(objCtaCobCab);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliObserva.getSize() + " registro(s)");
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
    return objlstCtaCab;  	
	}
	
 public String InformeFacturaBoletaCtaCob(String nrodoc, int emp_id , int suc_id) throws SQLException{  
	 
	String SQL_LISTA_CTACOBCAB = "select t.*, dt.* from V_LISTACTACOB_CAB t, v_listavtas_det dt "
							+" where t.emp_id="+emp_id
							+" and t.suc_id= "+suc_id
							+" and t.ctacob_nrodoc in "+nrodoc
							+" and T.CTACOB_KEY = dt.VTAS_KEY "
							+" order by t.ctacob_fecemi, dt.pro_id ";	
	
	P_WHERE = SQL_LISTA_CTACOBCAB;
	P_TITULO = "REPORTE DOCUMENTO VENTA FACTURA / BOLETA ";
	   
   return "";
}

    
}    

