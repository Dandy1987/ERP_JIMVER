package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.me.gj.database.ConectaBD;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.planillas.informes.InformesPrestamos;
import org.me.gj.model.seguridad.mantenimiento.CfgInicial;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zul.Messagebox;

public class DaoCfgInicial {

    //Instancias de Objetos
    CfgInicial objCfginicial;
    Sucursales objSucursales;
    ListModelList<CfgInicial> objlstCfginicial;
    ListModelList<Sucursales> objlstSucursales;
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
    private static final Logger LOGGER = Logger.getLogger(DaoCfgInicial.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarConfInicial(CfgInicial objcfg) throws SQLException {
        String SQL_INSERTAR_CONFIG_INI = "{call pack_tconfini.p_insertarCfg_empsuc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CONFIG_INI);
            cst.clearParameters();
            cst.setInt(1, objcfg.getEmp_id());
            cst.setInt(2, objcfg.getSuc_id());
            cst.setString(3, objcfg.getTcfg_mon());
            cst.setInt(4, objcfg.getTcfg_mtomin());
            cst.setInt(5, objcfg.getTcfg_ciemon());
            cst.setInt(6, objcfg.getTcfg_cieped());
            cst.setDouble(7, objcfg.getTcfg_topefe());
            cst.setString(8, objcfg.getTcfg_enlpla());
            cst.setString(9, objcfg.getTcfg_enlple());
            cst.setString(10, objcfg.getTcfg_gengui());
            cst.setString(11, objcfg.getTcfg_gendoc());
            cst.setString(12, objcfg.getTcfg_regstk());
            cst.setString(13, objcfg.getTcfg_regcst());
            cst.setInt(14, objcfg.getTcfg_diacad());
            cst.setInt(15, objcfg.getTcfg_voucher());
            cst.setString(16, objcfg.getTcfg_ctaafecto());
            cst.setString(17, objcfg.getTcfg_ctaafectofmes());
            cst.setString(18, objcfg.getTcfg_ctaexoinaf());
            cst.setString(19, objcfg.getTcfg_ctaimpuesto());
            cst.setString(20, objcfg.getTcfg_ctatotal());
            cst.setString(21, objcfg.getTcfg_reclamoafecto());
            cst.setString(22, objcfg.getTcfg_reclamoexoinaf());
            cst.setString(23, objcfg.getTcfg_reclamodsctodinero());
            cst.registerOutParameter(24, java.sql.Types.NUMERIC);
            cst.registerOutParameter(25, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(25);
            i_flagErrorBD = cst.getInt(24);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro una configuracion inicial con codigo " + Utilitarios.lpad("" + objcfg.getTcfg_key(), 3, "0"));
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
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

    public ParametrosSalida actualizarConfInicial(CfgInicial objcfg) throws SQLException {
        String SQL_ACTUALIZAR_CONFIG_INI = "{call pack_tconfini.p_actualizarCfg_empsuc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CONFIG_INI);
            cst.clearParameters();
            cst.setInt(1, objcfg.getTcfg_key());
            cst.setInt(2, objcfg.getEmp_id());
            cst.setInt(3, objcfg.getSuc_id());
            cst.setString(4, objcfg.getTcfg_mon());
            cst.setInt(5, objcfg.getTcfg_mtomin());
            cst.setInt(6, objcfg.getTcfg_ciemon());
            cst.setInt(7, objcfg.getTcfg_cieped());
            cst.setDouble(8, objcfg.getTcfg_topefe());
            cst.setString(9, objcfg.getTcfg_enlpla());
            cst.setString(10, objcfg.getTcfg_enlple());
            cst.setString(11, objcfg.getTcfg_gengui());
            cst.setString(12, objcfg.getTcfg_gendoc());
            cst.setString(13, objcfg.getTcfg_regstk());
            cst.setString(14, objcfg.getTcfg_regcst());
            cst.setInt(15, objcfg.getTcfg_diacad());
            cst.setInt(16, objcfg.getTcfg_voucher());
            cst.setString(17, objcfg.getTcfg_ctaafecto());
            cst.setString(18, objcfg.getTcfg_ctaafectofmes());
            cst.setString(19, objcfg.getTcfg_ctaexoinaf());
            cst.setString(20, objcfg.getTcfg_ctaimpuesto());
            cst.setString(21, objcfg.getTcfg_ctatotal());
            cst.setString(22, objcfg.getTcfg_reclamoafecto());
            cst.setString(23, objcfg.getTcfg_reclamoexoinaf());
            cst.setString(24, objcfg.getTcfg_reclamodsctodinero());
            cst.setString(25, objcfg.getTcfg_firma());
            cst.registerOutParameter(26, java.sql.Types.NUMERIC);
            cst.registerOutParameter(27, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(27);
            i_flagErrorBD = cst.getInt(26);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro una configuracion inicial con codigo " + Utilitarios.lpad("" + objcfg.getTcfg_key(), 3, "0"));
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
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
    public ListModelList<Sucursales> lstSucursales(String cod, int caso) throws SQLException {
        objlstSucursales = new ListModelList<Sucursales>();
        String SQL_SUCURSALES;
        if (caso == 0) {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod from tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est<>" + caso
                    + " order by t.suc_ord,t.suc_id,p.emp_id";
        } else {
            SQL_SUCURSALES = "select t.suc_id,p.emp_id,p.emp_sig,t.suc_des, t.suc_dir, t.suc_est, t.suc_ord, "
                    + "t.suc_usuadd, t.suc_fecadd, t.suc_usumod, t.suc_fecmod from tsucursales t, tempresas p "
                    + "where t.emp_id=p.emp_id and t.suc_est=" + caso + " and t.emp_id='" + cod + "' and p.emp_est=1 "
                    + "order by t.suc_ord,t.suc_id,p.emp_id";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);
            while (rs.next()) {
                objSucursales = new Sucursales();
                objSucursales.setSuc_id(rs.getInt(1));
                objSucursales.setEmp_id(rs.getInt(2));
                objSucursales.setEmp_sig(rs.getString(3));
                objSucursales.setSuc_des(rs.getString(4));
                objSucursales.setSuc_dir(rs.getString(5));
                objSucursales.setSuc_est(rs.getInt(6));
                objSucursales.setSuc_ord(rs.getInt(7));
                objSucursales.setSuc_usuadd(rs.getString(8));
                objSucursales.setSuc_fecadd(rs.getDate(9));
                objSucursales.setSuc_usumod(rs.getString(10));
                objSucursales.setSuc_fecmod(rs.getDate(11));
                objlstSucursales.add(objSucursales);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }

    public ListModelList<CfgInicial> lstCfgInicial() throws SQLException {
        String SQL_CONFIG_INICIAL = "select es.*, e.emp_des, e.emp_sig , es.tcfg_firma, s.suc_des , lpad(tcfg_key,3,'0') tcfg_id,  decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des ,"
                + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des , tcfg_diacad "
                + "from tcfg_empsuc es, tempresas e, tsucursales s "
                + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id "
                + "order by es.tcfg_key asc ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONFIG_INICIAL);
            objlstCfginicial = new ListModelList<CfgInicial>();
            while (rs.next()) {
                objCfginicial = new CfgInicial();
                objCfginicial.setTcfg_key(rs.getInt("tcfg_key"));
                objCfginicial.setTcfg_id(rs.getString("tcfg_id"));
                objCfginicial.setEmp_id(rs.getInt("emp_id"));
                objCfginicial.setEmp_des(rs.getString("emp_des"));
                objCfginicial.setEmp_sig(rs.getString("emp_sig"));
                objCfginicial.setSuc_id(rs.getInt("suc_id"));
                objCfginicial.setSuc_des(rs.getString("suc_des"));
                objCfginicial.setTcfg_mon(rs.getString("tcfg_mon"));
                objCfginicial.setTcfg_mtomin(rs.getInt("tcfg_mtomin"));
                objCfginicial.setTcfg_ciemon(rs.getInt("tcfg_ciemon"));
                objCfginicial.setTcfg_ciemon_des(rs.getString("tcfg_ciemon_des"));
                objCfginicial.setTcfg_cieped(rs.getInt("tcfg_cieped"));
                objCfginicial.setTcfg_cieped_des(rs.getString("tcfg_cieped_des"));
                objCfginicial.setTcfg_topefe(rs.getDouble("tcfg_topefe"));
                objCfginicial.setTcfg_enlpla(rs.getString("tcfg_enlpla"));
                objCfginicial.setTcfg_enlple(rs.getString("tcfg_enlple"));
                objCfginicial.setTcfg_gengui(rs.getString("tcfg_gengui"));
                objCfginicial.setTcfg_gengui_des(rs.getString("tcfg_gengui_des"));
                objCfginicial.setTcfg_gendoc(rs.getString("tcfg_gendoc"));
                objCfginicial.setTcfg_gendoc_des(rs.getString("tcfg_gendoc_des"));
                objCfginicial.setTcfg_regstk(rs.getString("tcfg_regstk"));
                objCfginicial.setTcfg_regstk_des(rs.getString("tcfg_regstk_des"));
                objCfginicial.setTcfg_regcst(rs.getString("tcfg_regcst"));
                objCfginicial.setTcfg_regcst_des(rs.getString("tcfg_regcst_des"));
                objCfginicial.setTcfg_diacad(rs.getInt("tcfg_diacad"));
                objCfginicial.setTcfg_voucher(rs.getInt("tcfg_voucher"));
                objCfginicial.setTcfg_ctaafecto(rs.getString("tcfg_ctaafecto"));
                objCfginicial.setTcfg_ctaafectofmes(rs.getString("tcfg_ctaafecfmes"));
                objCfginicial.setTcfg_ctaexoinaf(rs.getString("tcfg_ctaexoinaf"));
                objCfginicial.setTcfg_ctaimpuesto(rs.getString("tcfg_ctaimpuesto"));
                objCfginicial.setTcfg_ctatotal(rs.getString("tcfg_ctatotal"));
                objCfginicial.setTcfg_reclamoafecto(rs.getString("tcfg_recafecto"));
                objCfginicial.setTcfg_reclamoexoinaf(rs.getString("tcfg_recexoinaf"));
                objCfginicial.setTcfg_reclamodsctodinero(rs.getString("tcfg_recdescdin"));
                objCfginicial.setTcfg_firma(rs.getString("tcfg_firma"));
                objlstCfginicial.add(objCfginicial);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCfginicial.getSize() + " registro(s)");
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
        return objlstCfginicial;
    }

    public String getFirma(int empresa, int sucursal) throws SQLException {

        String sql;

        sql = "{call codijisa.pack_tconfini.p_listaConfigFirma(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql);
            cst.setInt(1, empresa);
            cst.setInt(2, sucursal);

            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(3);
            st = con.createStatement();

            objCfginicial = null;
            while (rs.next()) {
                objCfginicial = new CfgInicial();

              
                objCfginicial.setTcfg_firma(rs.getString("tcfg_firma"));

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }

        return objCfginicial.getTcfg_firma();
    }

    public ListModelList<CfgInicial> BusquedaCfgInicial(int seleccion, String consulta) throws SQLException {

        String SQL_CONFIG_INICIAL = "";
        if (seleccion == 0) {
            SQL_CONFIG_INICIAL = "select es.*, e.emp_des, e.emp_sig , s.suc_des , lpad(tcfg_key,3,'0') tcfg_id, es.tcfg_firma, decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des , "
                    + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des "
                    + "from tcfg_empsuc es, tempresas e, tsucursales s "
                    + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id "
                    + "order by es.tcfg_key asc ";
        } else if (seleccion == 1) {
            SQL_CONFIG_INICIAL = "select es.*, e.emp_des, e.emp_sig , s.suc_des , lpad(tcfg_key,3,'0') tcfg_id,es.tcfg_firma,  decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des , "
                    + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des "
                    + "from tcfg_empsuc es, tempresas e, tsucursales s "
                    + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id and es.tcfg_key like '" + consulta + "' "
                    + "order by es.tcfg_key asc ";
        } else if (seleccion == 2) {
            SQL_CONFIG_INICIAL = "select es.*,es.tcfg_firma, e.emp_des, e.emp_sig , s.suc_des , lpad(tcfg_key,3,'0') tcfg_id,  decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des , "
                    + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des "
                    + "from tcfg_empsuc es, tempresas e, tsucursales s "
                    + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id and e.emp_des like '" + consulta + "' "
                    + "order by es.tcfg_key asc ";
        } else if (seleccion == 3) {
            SQL_CONFIG_INICIAL = "select es.*, es.tcfg_firma,e.emp_des, e.emp_sig , s.suc_des , lpad(tcfg_key,3,'0') tcfg_id,  decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des , "
                    + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des "
                    + "from tcfg_empsuc es, tempresas e, tsucursales s "
                    + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id and s.suc_des like '" + consulta + "' "
                    + "order by es.tcfg_key asc ";
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONFIG_INICIAL);
            objlstCfginicial = new ListModelList<CfgInicial>();
            while (rs.next()) {
                objCfginicial = new CfgInicial();
                objCfginicial.setTcfg_key(rs.getInt("tcfg_key"));
                objCfginicial.setTcfg_id(rs.getString("tcfg_id"));
                objCfginicial.setEmp_id(rs.getInt("emp_id"));
                objCfginicial.setEmp_des(rs.getString("emp_des"));
                objCfginicial.setEmp_sig(rs.getString("emp_sig"));
                objCfginicial.setSuc_id(rs.getInt("suc_id"));
                objCfginicial.setSuc_des(rs.getString("suc_des"));
                objCfginicial.setTcfg_mon(rs.getString("tcfg_mon"));
                objCfginicial.setTcfg_mtomin(rs.getInt("tcfg_mtomin"));
                objCfginicial.setTcfg_ciemon(rs.getInt("tcfg_ciemon"));
                objCfginicial.setTcfg_ciemon_des(rs.getString("tcfg_ciemon_des"));
                objCfginicial.setTcfg_cieped(rs.getInt("tcfg_cieped"));
                objCfginicial.setTcfg_cieped_des(rs.getString("tcfg_cieped_des"));
                objCfginicial.setTcfg_topefe(rs.getDouble("tcfg_topefe"));
                objCfginicial.setTcfg_enlpla(rs.getString("tcfg_enlpla"));
                objCfginicial.setTcfg_enlple(rs.getString("tcfg_enlple"));
                objCfginicial.setTcfg_gengui(rs.getString("tcfg_gengui"));
                objCfginicial.setTcfg_gengui_des(rs.getString("tcfg_gengui_des"));
                objCfginicial.setTcfg_gendoc(rs.getString("tcfg_gendoc"));
                objCfginicial.setTcfg_gendoc_des(rs.getString("tcfg_gendoc_des"));
                objCfginicial.setTcfg_regstk(rs.getString("tcfg_regstk"));
                objCfginicial.setTcfg_regstk_des(rs.getString("tcfg_regstk_des"));
                objCfginicial.setTcfg_regcst(rs.getString("tcfg_regcst"));
                objCfginicial.setTcfg_regcst_des(rs.getString("tcfg_regcst_des"));
                objCfginicial.setTcfg_diacad(rs.getInt("tcfg_diacad"));
                objCfginicial.setTcfg_voucher(rs.getInt("tcfg_voucher"));
                objCfginicial.setTcfg_ctaafecto(rs.getString("tcfg_ctaafecto"));
                objCfginicial.setTcfg_ctaafectofmes(rs.getString("tcfg_ctaafecfmes"));
                objCfginicial.setTcfg_ctaexoinaf(rs.getString("tcfg_ctaexoinaf"));
                objCfginicial.setTcfg_ctaimpuesto(rs.getString("tcfg_ctaimpuesto"));
                objCfginicial.setTcfg_ctatotal(rs.getString("tcfg_ctatotal"));
                objCfginicial.setTcfg_reclamoafecto(rs.getString("tcfg_recafecto"));
                objCfginicial.setTcfg_reclamoexoinaf(rs.getString("tcfg_recexoinaf"));
                objCfginicial.setTcfg_reclamodsctodinero(rs.getString("tcfg_recdescdin"));
                objCfginicial.setTcfg_firma(rs.getString("tcfg_firma"));
                objlstCfginicial.add(objCfginicial);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCfginicial.getSize() + " registro(s)");
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
        return objlstCfginicial;
    }

    public CfgInicial fecCaduEmpSuc(int emp_id, int suc_id) throws SQLException {
        String SQL_CONFIG_INICIAL = "";

        SQL_CONFIG_INICIAL = "select es.*,es.tcfg_firma, e.emp_des, e.emp_sig , s.suc_des , lpad(tcfg_key,3,'0') tcfg_id,  decode(es.tcfg_ciemon,0,'ABIERTO',1,'CERRADO') tcfg_ciemon_des , decode(es.tcfg_cieped,0,'ABIERTO',1,'CERRADO') tcfg_cieped_des , "
                + "decode(es.tcfg_gengui,'S','SI','N','NO') tcfg_gengui_des, decode(es.tcfg_gendoc,'S','SI','N','NO') tcfg_gendoc_des, decode(es.tcfg_regstk,'S','SI','N','NO') tcfg_regstk_des, decode(es.tcfg_regcst,'S','SI','N','NO') tcfg_regcst_des "
                + "from tcfg_empsuc es, tempresas e, tsucursales s "
                + "where es.emp_id = e.emp_id and es.suc_id = s.suc_id and e.emp_id = s.emp_id and "
                + " es.emp_id=" + objUsuCredential.getCodemp() + " and es.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "order by es.tcfg_key asc ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONFIG_INICIAL);
            objCfginicial = null;
            while (rs.next()) {
                objCfginicial = new CfgInicial();
                objCfginicial.setTcfg_key(rs.getInt("tcfg_key"));
                objCfginicial.setTcfg_id(rs.getString("tcfg_id"));
                objCfginicial.setEmp_id(rs.getInt("emp_id"));
                objCfginicial.setEmp_des(rs.getString("emp_des"));
                objCfginicial.setEmp_sig(rs.getString("emp_sig"));
                objCfginicial.setSuc_id(rs.getInt("suc_id"));
                objCfginicial.setSuc_des(rs.getString("suc_des"));
                objCfginicial.setTcfg_mon(rs.getString("tcfg_mon"));
                objCfginicial.setTcfg_mtomin(rs.getInt("tcfg_mtomin"));
                objCfginicial.setTcfg_ciemon(rs.getInt("tcfg_ciemon"));
                objCfginicial.setTcfg_ciemon_des(rs.getString("tcfg_ciemon_des"));
                objCfginicial.setTcfg_cieped(rs.getInt("tcfg_cieped"));
                objCfginicial.setTcfg_cieped_des(rs.getString("tcfg_cieped_des"));
                objCfginicial.setTcfg_topefe(rs.getDouble("tcfg_topefe"));
                objCfginicial.setTcfg_enlpla(rs.getString("tcfg_enlpla"));
                objCfginicial.setTcfg_enlple(rs.getString("tcfg_enlple"));
                objCfginicial.setTcfg_gengui(rs.getString("tcfg_gengui"));
                objCfginicial.setTcfg_gengui_des(rs.getString("tcfg_gengui_des"));
                objCfginicial.setTcfg_gendoc(rs.getString("tcfg_gendoc"));
                objCfginicial.setTcfg_gendoc_des(rs.getString("tcfg_gendoc_des"));
                objCfginicial.setTcfg_regstk(rs.getString("tcfg_regstk"));
                objCfginicial.setTcfg_regstk_des(rs.getString("tcfg_regstk_des"));
                objCfginicial.setTcfg_regcst(rs.getString("tcfg_regcst"));
                objCfginicial.setTcfg_regcst_des(rs.getString("tcfg_regcst_des"));
                objCfginicial.setTcfg_diacad(rs.getInt("tcfg_diacad"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objCfginicial.getTcfg_id() + " registro(s)");

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

        return objCfginicial;
    }
}
