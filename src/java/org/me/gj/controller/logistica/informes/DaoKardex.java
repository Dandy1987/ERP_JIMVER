package org.me.gj.controller.logistica.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.Kardex;
import org.me.gj.model.logistica.informes.KardexCab;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoKardex {

    //Instancia de Objetos
    ListModelList<Kardex> objlstKardex;
    Kardex objKardex;
    KardexCab objKardexCab;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    public static String P_WHEREDET = "";
    public static String P_TITULO = "";
    SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoKardex.class);

    //Eventos Primarios - Transaccionales
    public KardexCab generarKardexDetallado(int emp_id, int suc_id, Date fechaIni, Date fechaFin, String alm_id, String prov_id, String pro_idi, String pro_idf, String s_ide) throws SQLException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_GENERAR_KARDEX_CAB;
        if (pro_idf.isEmpty()) {
            pro_idf = pro_idi;
        }
        SQL_GENERAR_KARDEX_CAB = "{call pack_tkardex.p_kardexdetallado(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_KARDEX_CAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_prov_id", prov_id);
            cst.setString("c_pro_idi", pro_idi);
            cst.setString("c_pro_idf", pro_idf);
            cst.setDate("c_kar_fecini", new java.sql.Date(fechaIni.getTime()));
            cst.setDate("c_kar_fecfin", new java.sql.Date(fechaFin.getTime()));
            cst.setString("c_alm_id", alm_id);
            cst.setString("c_mac", s_ide);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            if (i_flagErrorBD == 0) {
                st = con.createStatement();
                sf1 = new SimpleDateFormat("dd/MM/yyyy");
                String SQL_KARDEX = "select t.emp_id,t.suc_id,t.kar_key,t.kar_id,t.pro_id,t.pro_des,t.pro_undpre,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.stk_ini,t.cst_ini,t.cst_uni,t.stk_fin,t.cst_fin,t.cst_unf,t.cab_mac,"
                        + " round(((t.stk_ini/t.pro_undpre)-(mod((t.stk_ini/t.pro_undpre),1))),0) stkini_ent,mod(t.stk_ini , t.pro_undpre) stkini_fra,"
                        + " round(((nvl(sum(t1.cante),0)/t.pro_undpre)-(mod(nvl(sum(t1.cante),0)/t.pro_undpre,1))),0) cante_ent,mod(nvl(sum(t1.cante),0), t.pro_undpre) cante_fra,"
                        + " round(((nvl(sum(t1.cants),0)/t.pro_undpre)-(mod(nvl(sum(t1.cants),0)/t.pro_undpre,1))),0) cants_ent,mod(nvl(sum(t1.cants),0), t.pro_undpre) cants_fra,"
                        + " round(((t.stk_fin/t.pro_undpre)-(mod((t.stk_fin/t.pro_undpre),1))),0) stkfin_ent,mod(t.stk_fin , t.pro_undpre) stkfin_fra"
                        + " from tkardex_cab t ,tkardex_det t1 where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.cab_mac=t1.det_mac and t.cab_mac='" + s_ide + "' and t.kar_key=t1.kar_key"
                        + " group by t.emp_id,t.suc_id,t.kar_key,t.kar_id,t.pro_id,t.pro_des,t.pro_undpre,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.stk_ini,t.cst_ini,t.cst_uni,t.stk_fin,t.cst_fin,t.cst_unf,t.cab_mac order by t.kar_key";
                P_WHERER = SQL_KARDEX;
                P_TITULO = "ALMACEN " + alm_id + " DEL " + sf1.format(fechaIni) + " HASTA " + sf1.format(fechaFin);
                objKardexCab = null;
                rs = st.executeQuery(SQL_KARDEX);
                while (rs.next()) {
                    objKardexCab = new KardexCab();
                    objKardexCab.setEmp_id(rs.getInt("emp_id"));
                    objKardexCab.setSuc_id(rs.getInt("suc_id"));
                    objKardexCab.setKar_key(rs.getLong("kar_key"));
                    objKardexCab.setKar_id(rs.getString("kar_id"));
                    objKardexCab.setPro_id(rs.getString("pro_id"));
                    objKardexCab.setPro_des(rs.getString("pro_des"));
                    objKardexCab.setPro_undpre(rs.getDouble("pro_undpre"));
                    objKardexCab.setProv_id(rs.getString("prov_id"));
                    objKardexCab.setProv_razsoc(rs.getString("prov_razsoc"));
                    objKardexCab.setLin_id(rs.getString("lin_id"));
                    objKardexCab.setLin_des(rs.getString("lin_des"));
                    objKardexCab.setSkt_ini(rs.getDouble("stk_ini"));
                    objKardexCab.setCst_ini(rs.getDouble("cst_ini"));
                    objKardexCab.setCst_uni(rs.getDouble("cst_uni"));
                    objKardexCab.setSkt_fin(rs.getDouble("stk_fin"));
                    objKardexCab.setCst_fin(rs.getDouble("cst_fin"));
                    objKardexCab.setCst_unf(rs.getDouble("cst_unf"));
                    objKardexCab.setCab_mac(rs.getString("cab_mac"));
                }
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return objKardexCab;
    }

    public KardexCab generarKardexResumido(int emp_id, int suc_id, Date fechaIni, Date fechaFin, String alm_id, String prov_id, String pro_idi, String pro_idf, String s_ide) throws SQLException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_GENERAR_KARDEX_CAB;
        if (pro_idf.isEmpty()) {
            pro_idf = pro_idi;
        }
        SQL_GENERAR_KARDEX_CAB = "{call pack_tkardex.p_kardexresumido(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_KARDEX_CAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_prov_id", prov_id);
            cst.setString("c_pro_idi", pro_idi);
            cst.setString("c_pro_idf", pro_idf);
            cst.setDate("c_kar_fecini", new java.sql.Date(fechaIni.getTime()));
            cst.setDate("c_kar_fecfin", new java.sql.Date(fechaFin.getTime()));
            cst.setString("c_alm_id", alm_id);
            cst.setString("c_mac", s_ide);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            if (i_flagErrorBD == 0) {
                st = con.createStatement();
                sf1 = new SimpleDateFormat("dd/MM/yyyy");
                String SQL_KARDEX = "select t.emp_id,t.suc_id,t.kar_key,t.kar_id,t.pro_id,t.pro_des,t.pro_undpre,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.stk_ini,t.cst_ini,t.cst_uni,t.stk_fin,t.cst_fin,t.cst_unf,t.cab_mac,"
                        + " round(((t.stk_ini/t.pro_undpre)-(mod((t.stk_ini/t.pro_undpre),1))),0) stkini_ent,mod(t.stk_ini , t.pro_undpre) stkini_fra,"
                        + " round(((nvl(sum(t1.cante),0)/t.pro_undpre)- mod((nvl(sum(t1.cante),0)/t.pro_undpre),1)),0) cante_ent,mod(nvl(sum(t1.cante),0),t.pro_undpre) cante_fra,"
                        + " round(((nvl(sum(t1.cants),0)/t.pro_undpre)- mod((nvl(sum(t1.cants),0)/t.pro_undpre),1)),0) cants_ent,mod(nvl(sum(t1.cants),0),t.pro_undpre) cants_fra,"
                        + " round(((t.stk_fin/t.pro_undpre)-(mod((t.stk_fin/t.pro_undpre),1))),0) stkfin_ent,mod(t.stk_fin , t.pro_undpre) stkfin_fra"
                        + " from tkardex_cab t ,tkardex_det t1 where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.cab_mac=t1.det_mac and t.cab_mac='" + s_ide + "' and t.kar_key=t1.kar_key"
                        + " group by t.emp_id,t.suc_id,t.kar_key,t.kar_id,t.pro_id,t.pro_des,t.pro_undpre,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.stk_ini,t.cst_ini,t.cst_uni,t.stk_fin,t.cst_fin,t.cst_unf,t.cab_mac order by t.kar_key";
                P_WHERER = SQL_KARDEX;
                P_TITULO = "ALMACEN " + alm_id + " DEL " + sf1.format(fechaIni) + " HASTA " + sf1.format(fechaFin);
                objKardexCab = null;
                rs = st.executeQuery(SQL_KARDEX);
                while (rs.next()) {
                    objKardexCab = new KardexCab();
                    objKardexCab.setEmp_id(rs.getInt("emp_id"));
                    objKardexCab.setSuc_id(rs.getInt("suc_id"));
                    objKardexCab.setKar_key(rs.getLong("kar_key"));
                    objKardexCab.setKar_id(rs.getString("kar_id"));
                    objKardexCab.setPro_id(rs.getString("pro_id"));
                    objKardexCab.setPro_des(rs.getString("pro_des"));
                    objKardexCab.setPro_undpre(rs.getDouble("pro_undpre"));
                    objKardexCab.setProv_id(rs.getString("prov_id"));
                    objKardexCab.setProv_razsoc(rs.getString("prov_razsoc"));
                    objKardexCab.setLin_id(rs.getString("lin_id"));
                    objKardexCab.setLin_des(rs.getString("lin_des"));
                    objKardexCab.setSkt_ini(rs.getDouble("stk_ini"));
                    objKardexCab.setCst_ini(rs.getDouble("cst_ini"));
                    objKardexCab.setCst_uni(rs.getDouble("cst_uni"));
                    objKardexCab.setSkt_fin(rs.getDouble("stk_fin"));
                    objKardexCab.setCst_fin(rs.getDouble("cst_fin"));
                    objKardexCab.setCst_unf(rs.getDouble("cst_unf"));
                    objKardexCab.setCab_mac(rs.getString("cab_mac"));
                }
            }
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar Kardex debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar Kardex porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return objKardexCab;
    }

    public KardexCab generarKardexTotalizado(int emp_id, int suc_id, Date fechaIni, Date fechaFin, String alm_id, String prov_id, String pro_idi, String pro_idf, String s_mac) throws SQLException, ParseException {
        s_msg = "";
        cst = null;
        con = null;
        String SQL_GENERAR_KARDEX_CAB;
        if (pro_idf.isEmpty()) {
            pro_idf = pro_idi;
        }
        SQL_GENERAR_KARDEX_CAB = "{call PACK_TKARDEX.p_kardextotalizado(?,?,?,?,?,?,?,?,?,?,?)}";
        sf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sf2 = new SimpleDateFormat("dd/MM/yyyy");

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_KARDEX_CAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_prov_id", prov_id);
            cst.setString("c_pro_idi", pro_idi);
            cst.setString("c_pro_idf", pro_idf);
            cst.setDate("c_kar_fecini", new java.sql.Date(fechaIni.getTime()));
            cst.setDate("c_kar_fecfin", new java.sql.Date(fechaFin.getTime()));
            cst.setString("c_alm_id", alm_id);
            cst.setString("c_mac", s_mac);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.execute();
            i_flagErrorBD = cst.getInt("n_Flag");
            s_msg = cst.getString("c_msg");
            if (i_flagErrorBD == 0) {
                st = con.createStatement();
                String SQL_KARDEX = "select  kd.nes_tip codnotes, kd.nes_des notes,"
                        + "kc.emp_id,kc.suc_id,kc.kar_key,kc.kar_id, kc.pro_id,kc.pro_des,kc.pro_undpre,kc.prov_id,kc.prov_razsoc,kc.lin_id,kc.lin_des,kc.stk_ini,kc.cst_ini,kc.cst_uni,kc.stk_fin,kc.cst_fin,kc.cst_unf,kc.cab_mac, "
                        + "sum(kd.cstunis) costini , sum(kc.stk_ini) stk_ini, sum(kc.cst_uni) stfin, sum(kd.cante)totent, x.totcstinie, sum(kd.cstinie) costini,sum(kd.cants)totsal, y.totcstinis "
                        + "from tkardex_cab kc, tkardex_det kd ,"
                        + "(select sum(t.cstinie) totcstinie from tkardex_det t where t.tip_mov='E') x ,"
                        + "(select sum(t.cstinie) totcstinis from tkardex_det t where t.tip_mov='S') y "
                        + "where kc.emp_id = kd.emp_id and kc.suc_id = kd.suc_id and kc.kar_key = kd.kar_key and kc.cab_mac = kd.det_mac and "
                        + "trunc(kd.fecha) between to_date('" + sf2.format(fechaIni) + "','dd/MM/yy') and to_date('" + sf1.format(fechaFin) + "','dd/MM/yyyy') "
                        + "and kc.cab_mac = '" + s_mac + "' and kc.suc_id=" + suc_id + " and kc.emp_id=" + emp_id
                        + " group by  kd.nes_tip, kd.nes_des ,"
                        + "kc.emp_id,kc.suc_id,kc.kar_key,kc.kar_id, kc.pro_id,kc.pro_des,kc.pro_undpre,kc.prov_id,kc.prov_razsoc,kc.lin_id,kc.lin_des,kc.stk_ini,kc.cst_ini,kc.cst_uni,kc.stk_fin,kc.cst_fin,kc.cst_unf,kc.cab_mac, "
                        + "kc.stk_ini, kc.stk_fin ,x.totcstinie, y.totcstinis, kd.nes_tip ,kd.nes_des "
                        + "order by kd.nes_tip asc";
                P_WHERER = SQL_KARDEX;
                P_TITULO = "ALMACEN " + alm_id + " DEL " + sf1.format(fechaIni) + " HASTA " + sf1.format(fechaFin);
                //objKardexCab = null;
                rs = st.executeQuery(SQL_KARDEX);
                while (rs.next()) {
                    objKardexCab = new KardexCab();
                    objKardexCab.setEmp_id(rs.getInt("emp_id"));
                    objKardexCab.setSuc_id(rs.getInt("suc_id"));
                    objKardexCab.setKar_key(rs.getLong("kar_key"));
                    objKardexCab.setKar_id(rs.getString("kar_id"));
                    objKardexCab.setPro_id(rs.getString("pro_id"));
                    objKardexCab.setPro_des(rs.getString("pro_des"));
                    objKardexCab.setPro_undpre(rs.getDouble("pro_undpre"));
                    objKardexCab.setProv_id(rs.getString("prov_id"));
                    objKardexCab.setProv_razsoc(rs.getString("prov_razsoc"));
                    objKardexCab.setLin_id(rs.getString("lin_id"));
                    objKardexCab.setLin_des(rs.getString("lin_des"));
                    objKardexCab.setSkt_ini(rs.getDouble("stk_ini"));
                    objKardexCab.setCst_ini(rs.getDouble("cst_ini"));
                    objKardexCab.setCst_uni(rs.getDouble("cst_uni"));
                    objKardexCab.setSkt_fin(rs.getDouble("stk_fin"));
                    objKardexCab.setCst_fin(rs.getDouble("cst_fin"));
                    objKardexCab.setCst_unf(rs.getDouble("cst_unf"));
                    objKardexCab.setCab_mac(rs.getString("cab_mac"));
                }
            }
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar Kardex debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar Kardex porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return objKardexCab;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Kardex> mostrarKardexDetallado(int emp_id, int suc_id, Date fechaIni, Date fechaFin, long kar_key) throws SQLException {
        String SQL_KARDEX = "select t.emp_id empid,t.suc_id sucid,t.kar_key,t1.kar_item karitem,t1.fecha fecha,t1.hora hora,t1.tip_mov tip,"
                + " nvl(trunc(decode(t1.tip_mov,'E',t1.cante,'S',t1.cants)/t.pro_undpre),0) movent, nvl(mod(decode(t1.tip_mov,'E',t1.cante,'S',t1.cants),t.pro_undpre),0) movfra, "
                + " nvl(trunc(t1.canttot/t.pro_undpre),0) stkent, nvl(mod(t1.canttot,t.pro_undpre),0) stkfra, "
                + " t1.nes_tip idguia,t1.nes_des desguia,t1.nes_nro nroguia,t1.tip_doc tipdoc,t1.nro_doc nrodoc,t1.cstunitot,t1.csttot,t1.cli_id,t1.cli_razsoc,t.prov_id prov_id,t.prov_razsoc prov_razsoc ,t.pro_id pro_id,t.pro_des pro_des,"
                + " t.pro_undpre pro_undpre from tkardex_cab t, tkardex_det t1 "
                + " where t.emp_id=t1.emp_id and t.suc_id=t1.suc_id and t.kar_key=t1.kar_key "
                + " and trunc(t1.fecha) between to_date('" + sf1.format(fechaIni) + "','dd/mm/yyyy') and to_date('" + sf1.format(fechaFin) + "','dd/mm/yyyy')"
                + " and t.emp_id=" + emp_id + " "
                + " and t.suc_id=" + suc_id + " and t.kar_key = " + kar_key + " order by t.emp_id,t.suc_id,t1.kar_item ";
        //P_WHEREDET=SQL_KARDEX;
        objlstKardex = new ListModelList<Kardex>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_KARDEX);
            while (rs.next()) {
                objKardex = new Kardex();
                objKardex.setEmp_id(rs.getInt("empid"));
                objKardex.setSuc_id(rs.getInt("sucid"));
                objKardex.setKar_item(rs.getInt("karitem"));
                objKardex.setGui_fecha(rs.getTimestamp("fecha"));
                objKardex.setGui_hora(rs.getString("hora"));
                objKardex.setTip_mov(rs.getString("tip"));
                objKardex.setMovent(rs.getInt("movent"));
                objKardex.setMovfra(rs.getInt("movfra"));
                objKardex.setStkent(rs.getInt("stkent"));
                objKardex.setStkfra(rs.getInt("stkfra"));
                objKardex.setGui_tip(rs.getString("idguia"));
                objKardex.setGui_des(rs.getString("desguia").length() < 25 ? rs.getString("desguia") : rs.getString("desguia").substring(0, 24));
                objKardex.setGui_nro(rs.getString("nroguia"));
                objKardex.setTip_doc(rs.getString("tipdoc"));
                objKardex.setNro_doc(rs.getString("nrodoc"));
                objKardex.setProv_id(rs.getString("prov_id"));
                objKardex.setProv_razsoc(rs.getString("prov_razsoc"));
                objKardex.setPro_id(rs.getString("pro_id"));
                objKardex.setPro_des(rs.getString("pro_des"));
                objKardex.setPro_undpre(rs.getDouble("pro_undpre"));
                objKardex.setKar_cliid(rs.getString("cli_id"));
                objKardex.setKar_clides(rs.getString("cli_razsoc").length() < 25 ? rs.getString("cli_razsoc") : rs.getString("cli_razsoc").substring(0, 24));
                //objKardex.setKar_clides(rs.getString("pro_undpre"));
                objlstKardex.add(objKardex);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstKardex.getSize() + " registro(s)");
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
        return objlstKardex;
    }

    public ListModelList<Kardex> mostrarKardexResumido(int emp_id, int suc_id, Date fechaIni, Date fechaFin, long kar_key) throws SQLException {
        String SQL_KARDEX = "select t.emp_id empid,t.suc_id sucid,t1.kar_item karitem,t1.fecha fecha,t1.tip_mov tip,"
                + " nvl(trunc(decode(t1.tip_mov,'E',t1.cante,'S',t1.cants)/t.pro_undpre),0) movent, nvl(mod(decode(t1.tip_mov,'E',t1.cante,'S',t1.cants),t.pro_undpre),0) movfra, "
                + " nvl(trunc(t1.canttot/t.pro_undpre),0) stkent, nvl(mod(t1.canttot,t.pro_undpre),0) stkfra, "
                + " t1.nes_tip idguia,t1.nes_des desguia ,t.pro_id pro_id,t.pro_des pro_des,"
                + " t.pro_undpre pro_undpre from tkardex_cab t, tkardex_det t1 "
                + " where t.emp_id=t1.emp_id and t.suc_id=t1.suc_id and t.kar_key=t1.kar_key "
                + " and trunc(t1.fecha) between to_date('" + sf1.format(fechaIni) + "','dd/mm/yyyy') and to_date('" + sf1.format(fechaFin) + "','dd/mm/yyyy')"
                + " and t.emp_id=" + emp_id + " "
                + " and t.suc_id=" + suc_id + " and t.kar_key = " + kar_key + " order by t.emp_id,t.suc_id,t1.kar_item,t1.fecha ";
        //P_WHEREDET=SQL_KARDEX;
        objlstKardex = new ListModelList<Kardex>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_KARDEX);
            while (rs.next()) {
                objKardex = new Kardex();
                objKardex.setEmp_id(rs.getInt("empid"));
                objKardex.setSuc_id(rs.getInt("sucid"));
                objKardex.setKar_item(rs.getInt("karitem"));
                objKardex.setGui_fecha(rs.getTimestamp("fecha"));
                objKardex.setGui_hora("");
                objKardex.setTip_mov(rs.getString("tip"));
                objKardex.setMovent(rs.getInt("movent"));
                objKardex.setMovfra(rs.getInt("movfra"));
                objKardex.setStkent(rs.getInt("stkent"));
                objKardex.setStkfra(rs.getInt("stkfra"));
                objKardex.setGui_tip(rs.getString("idguia"));
                objKardex.setGui_des(rs.getString("desguia").length() < 35 ? rs.getString("desguia") : rs.getString("desguia").substring(0, 34));
                objKardex.setGui_nro("");
                objKardex.setTip_doc("");
                objKardex.setNro_doc("");
                objKardex.setProv_id("");
                objKardex.setProv_razsoc("");
                objKardex.setPro_id(rs.getString("pro_id"));
                objKardex.setPro_des(rs.getString("pro_des"));
                objKardex.setPro_undpre(rs.getDouble("pro_undpre"));
                objlstKardex.add(objKardex);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstKardex.getSize() + " registro(s)");
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
        return objlstKardex;
    }

    public KardexCab consultaCabecera(int emp_id, int suc_id, long kar_key) throws SQLException {
        String SQL_CON_COMP = "select * from tkardex_cab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.kar_key=" + kar_key + "";
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CON_COMP);
            while (rs.next()) {
                objKardexCab = new KardexCab();
                objKardexCab.setEmp_id(rs.getInt("emp_id"));
                objKardexCab.setSuc_id(rs.getInt("suc_id"));
                objKardexCab.setKar_key(rs.getLong("kar_key"));
                objKardexCab.setKar_id(rs.getString("kar_id"));
                objKardexCab.setPro_id(rs.getString("pro_id"));
                objKardexCab.setPro_des(rs.getString("pro_des"));
                objKardexCab.setPro_undpre(rs.getDouble("pro_undpre"));
                objKardexCab.setProv_id(rs.getString("prov_id"));
                objKardexCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objKardexCab.setLin_id(rs.getString("lin_id"));
                objKardexCab.setLin_des(rs.getString("lin_des"));
                objKardexCab.setSkt_ini(rs.getDouble("stk_ini"));
                objKardexCab.setCst_ini(rs.getDouble("cst_ini"));
                objKardexCab.setCst_uni(rs.getDouble("cst_uni"));
                objKardexCab.setSkt_fin(rs.getDouble("stk_fin"));
                objKardexCab.setCst_fin(rs.getDouble("cst_fin"));
                objKardexCab.setCst_unf(rs.getDouble("cst_unf"));
                objKardexCab.setCab_mac(rs.getString("cab_mac"));
            }
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
        return objKardexCab;
    }
}
