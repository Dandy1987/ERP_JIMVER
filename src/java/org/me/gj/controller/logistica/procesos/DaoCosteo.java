package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.Costos;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.logistica.procesos.NotaESDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCosteo {

    //Instancias de Objetos
    ListModelList<NotaESDet> objlstNotaESDet;
    ListModelList<NotaESCab> objlstNotaESCab;
    ListModelList<Costos> objlstCostos;
    NotaESDet objNotaESDet;
    NotaESCab objNotaESCab;
    Costos objCostos;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCosteo.class);

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NotaESCab> BusquedaNotaES(String s_periodo, String f_fecha, String s_nescab_tipnotaes, int i_estado, String s_situacion) throws SQLException {
        String SQL_LISTA_NOTAES = "";
        //el periodo es un dato obligatorio
        //si eligio fecha + proveedor
        if (s_periodo.isEmpty()) {
            s_periodo = "%%";
        }
        if (s_situacion.isEmpty() || s_situacion.equals("'%%'")) {
            s_situacion = "'N','FP','FS','FP / FS','FF'";
        }
        if (s_situacion.equals("'FP'")) {
            s_situacion = "'FP','FP / FS'";
        }
        if (s_situacion.equals("'FS'")) {
            s_situacion = "'FS','FP / FS'";
        }
        if (s_situacion.equals("FF")) {
            s_situacion = "'FF'";
        }
        if (s_situacion.equals("FP")) {
            s_situacion = "'FP'";
        }
        if (s_situacion.equals("N")) {
            s_situacion = "'N'";
        }
         if (s_situacion.equals("%%")) {
            s_situacion = "''";
        }
        if (!f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTA_NOTAES = "select * from codijisa.v_listaresumencosteo t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' "
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.concepto in (" + s_situacion + ")  order by t.nescab_id,t.nescab_fecha";
        }//si eligio proveedor
        else if (f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTA_NOTAES = "select * from codijisa.v_listaresumencosteo t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and t.nescab_tipnotaes='" + s_nescab_tipnotaes + "' "
                    + " and t.nescab_est ='" + i_estado + "' and t.concepto in (" + s_situacion + ")  order by t.nescab_id,t.nescab_fecha";
        }//si eligio fecha
        else if (!f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTA_NOTAES = "select * from codijisa.v_listaresumencosteo t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' "
                    + " and t.nescab_est ='" + i_estado + "' and t.concepto in (" + s_situacion + ")  order by t.nescab_id,t.nescab_fecha";
        } //si no eligio nada
        else if (f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTA_NOTAES = "select * from codijisa.v_listaresumencosteo t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' "
                    + " and t.nescab_est ='" + i_estado + "' and t.concepto in (" + s_situacion + ") order by t.nescab_id,t.nescab_fecha";
        }
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_NOTAES);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes").length() < 35 ? rs.getString("notaes") : rs.getString("notaes").substring(0, 35));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_serie(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc") == null ? "" : rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie") == null ? "" : rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setHora(rs.getTime("nescab_fecha"));
                //objNotaESCab.setNescab_fecadd(new Date().getDate());
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setConcepto(rs.getString("concepto"));
                objlstNotaESCab.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESCab.getSize() + " registro(s)");
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> listaDetalleCosteoAuto(String notaes, String documento) throws SQLException {
        String SQL_DETALLECOSTEOAUTO;
        SQL_DETALLECOSTEOAUTO
                = " select r.*,decode(r.situacioncant||r.situaciontotal,'','NORMAL',r.situacioncant||','||r.situaciontotal) situacion from( "
                + " select k.*, "
                + " decode(k.difcant,0,'',"
                + " CASE "
                + " WHEN (k.difcant > 0) THEN 'CANT FACTURA(+)'"
                + " WHEN (k.difcant < 0) THEN 'CANT NOTA(+)'"
                + " ELSE 'N' END "
                + " ) situacioncant,"
                + " decode(k.diftotal,0,'',"
                + " CASE "
                + " WHEN (k.diftotal > 0) THEN 'SOLES FACTURA(+)'"
                + " WHEN (k.diftotal < 0) THEN 'SOLES NOTA(+)'"
                + " ELSE 'N' END "
                + " ) situaciontotal"
                + " from ("
                + " select z.*,t.pro_des,(z.prefac-z.prenota) difpre,(z.cantfac-z.cantnota) difcant,(z.vventafac-z.vventanota) difvventa,(z.totalfac-z.totalnota) diftotal from "
                + " (select x.*,nvl(y.prenota,0) prenota,nvl(y.cantnota,0) cantnota,nvl(y.vventanota,0) vventanota,nvl(y.totalnota,0) totalnota from  "
                + " ("
                + " select c2.tc_key factura, c1.tc_nrpedkey orden,/*c1.tc_serie||c1.tc_nrodoc documento,*/c2.pro_id,c2.tcd_preunifac prefac,nvl(c2.tcd_cantfac,0) cantfac,c2.tcd_vventafac vventafac,nvl(c2.tcd_subtotalfac,0) totalfac from "
                + " tcompras_cab c1,tcompras_det c2 where  c1.tc_key=c2.tc_key and c1.emp_id=c2.emp_id and c1.suc_id=c2.suc_id and c1.tc_est=1 and c2.tcd_est=1 and c1.tc_serie||c1.tc_nrodoc= '" + documento + "'"
                + " and c1.emp_id= " + objUsuCredential.getCodemp() + " and c1.suc_id= " + objUsuCredential.getCodsuc()
                + " ) x, "
                + " ( "
                + " select g2.nescab_id notaes, g1.nescab_ocnropedkey orden,/*g1.nescab_nrodoc documento,*/g2.pro_id,decode(g2.nesdet_valafe,0,g2.nesdet_valina,g2.nesdet_valafe)/(g2.nesdet_cant/g2.nesdet_undpre) prenota,nvl(g2.nesdet_cant/g2.nesdet_undpre,0) cantnota,decode(g2.nesdet_valafe,0,g2.nesdet_valina,g2.nesdet_valafe) vventanota,nvl(g2.nesdet_pvta,0) totalnota from tnotaes_cab g1,tnotaes_det g2"
                + " where g1.nescab_id='" + notaes + "' and g1.nescab_id=g2.nescab_id and g1.emp_id=g2.emp_id and g1.suc_id=g2.suc_id and g1.nescab_est=1 and g2.nesdet_est=1 and g1.nescab_nroserie||g1.nescab_nrodoc= '" + documento + "'"
                + " and g1.emp_id= " + objUsuCredential.getCodemp() + " and g1.suc_id= " + objUsuCredential.getCodsuc()
                + " ) y "
                + " where y.pro_id=x.pro_id) z,tproductos t "
                + " where z.pro_id=t.pro_id "
                + " ) k "
                + " ) r ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_DETALLECOSTEOAUTO);
            objlstNotaESCab = new ListModelList<NotaESCab>();
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setCstauto_idproducto(rs.getString("pro_id"));
                objNotaESCab.setCstauto_desproducto(rs.getString("pro_des"));
                objNotaESCab.setCstauto_prefac(rs.getDouble("prefac"));
                objNotaESCab.setCstauto_prenota(rs.getDouble("prenota"));
                objNotaESCab.setCstauto_cantfac(rs.getDouble("cantfac"));
                objNotaESCab.setCstauto_cantnota(rs.getDouble("cantnota"));
                objNotaESCab.setCstauto_vventafac(rs.getDouble("vventafac"));
                objNotaESCab.setCstauto_vventanota(rs.getDouble("vventanota"));
                objNotaESCab.setCstauto_totalfac(rs.getDouble("totalfac"));
                objNotaESCab.setCstauto_totalnota(rs.getDouble("totalnota"));
                objNotaESCab.setCstauto_difpre(rs.getDouble("difpre"));
                objNotaESCab.setCstauto_difcant(rs.getDouble("difcant"));
                objNotaESCab.setCstauto_difvventa(rs.getDouble("difvventa"));
                objNotaESCab.setCstauto_diftotal(rs.getDouble("diftotal"));
                objNotaESCab.setCstauto_situacion(rs.getString("situacion"));
                objlstNotaESCab.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESCab.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
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
        return objlstNotaESCab;
    }

    public ParametrosSalida actualizarCosteoNotaES(int emp_id, int suc_id, String notaes, int tip_costeo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_DETALLE = "{call pack_tnotaes.p_actualizarCosteoNotaES(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_DETALLE);
            cst.clearParameters();
            cst.setString(1, notaes);
            cst.setInt(2, emp_id);
            cst.setInt(3, suc_id);
            cst.setInt(4, tip_costeo);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Actualizó el costeo de la NotaES con código " + notaes);
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

    public ListModelList<Costos> busquedaCostos(int emp_id, int suc_id, String periodo, String almacen, String proveedor, String producto) throws SQLException {
        String SQL_LISTACOSTOS;
        String s_validaperiodo = periodo.isEmpty() ? "" : " and t.cst_periodo like '" + periodo + "'";
        //String s_validafec = fechai.isEmpty() ? "" : " and t.PCAB_FECEMI like to_date('" + fechai + "','dd/mm/yyyy') ";
        String s_validaalmacen = almacen.isEmpty() ? "" : " and t.alm_id like '" + almacen + "'";
        String s_validaproveedor = proveedor.isEmpty() ? "" : " and t.prov_id like '" + proveedor + "'";
        String s_validaproducto = producto.isEmpty() ? "" : " and t.pro_id like '" + producto + "' ";

        if (!periodo.isEmpty() && almacen.isEmpty() && proveedor.isEmpty() && producto.isEmpty()) {
            SQL_LISTACOSTOS = "select t.emp_id,t.suc_id,t.cst_periodo,t.alm_id,t.prov_id,t.pro_id,t.cst_estado,nvl(t.cst_cstini,0) cst_cstini,nvl(t.cst_cstfin,0) cst_cstfin,nvl(t.cst_inilifo,0) cst_inilifo,nvl(t.cst_finlifo,0) cst_finlifo,nvl(t.cst_repo,0) cst_repo,t.cst_usuadd,t.cst_fecadd,t.cst_usumod,t.cst_fecmod,"
                    + " pack_tproveedores.f_001_descProveedor(t.prov_id) prov_des,pack_tproductos.f_002_deslargaProducto(t.pro_id) prod_des from tcostos t "
                    + " where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id + " and t.cst_estado= 1 "
                    + s_validaperiodo + " order by t.cst_periodo,t.alm_id,t.prov_id,t.pro_id";
        } else if (periodo.isEmpty() && !almacen.isEmpty() && proveedor.isEmpty() && producto.isEmpty()) {
            SQL_LISTACOSTOS = "select t.emp_id,t.suc_id,t.cst_periodo,t.alm_id,t.prov_id,t.pro_id,t.cst_estado,nvl(t.cst_cstini,0) cst_cstini,nvl(t.cst_cstfin,0) cst_cstfin,nvl(t.cst_inilifo,0) cst_inilifo,nvl(t.cst_finlifo,0) cst_finlifo,nvl(t.cst_repo,0) cst_repo,t.cst_usuadd,t.cst_fecadd,t.cst_usumod,t.cst_fecmod,"
                    + " pack_tproveedores.f_001_descProveedor(t.prov_id) prov_des,pack_tproductos.f_002_deslargaProducto(t.pro_id) prod_des from tcostos t "
                    + " where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id + " and t.cst_estado= 1 "
                    + s_validaalmacen + " order by t.cst_periodo,t.alm_id,t.prov_id,t.pro_id";
        } else if (periodo.isEmpty() && almacen.isEmpty() && !proveedor.isEmpty() && producto.isEmpty()) {
            SQL_LISTACOSTOS = "select t.emp_id,t.suc_id,t.cst_periodo,t.alm_id,t.prov_id,t.pro_id,t.cst_estado,nvl(t.cst_cstini,0) cst_cstini,nvl(t.cst_cstfin,0) cst_cstfin,nvl(t.cst_inilifo,0) cst_inilifo,nvl(t.cst_finlifo,0) cst_finlifo,nvl(t.cst_repo,0) cst_repo,t.cst_usuadd,t.cst_fecadd,t.cst_usumod,t.cst_fecmod,"
                    + " pack_tproveedores.f_001_descProveedor(t.prov_id) prov_des,pack_tproductos.f_002_deslargaProducto(t.pro_id) prod_des from tcostos t "
                    + " where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id + " and t.cst_estado= 1 "
                    + s_validaproveedor + " order by t.cst_periodo,t.alm_id,t.prov_id,t.pro_id";
        } else if (periodo.isEmpty() && almacen.isEmpty() && proveedor.isEmpty() && !producto.isEmpty()) {
            SQL_LISTACOSTOS = "select t.emp_id,t.suc_id,t.cst_periodo,t.alm_id,t.prov_id,t.pro_id,t.cst_estado,nvl(t.cst_cstini,0) cst_cstini,nvl(t.cst_cstfin,0) cst_cstfin,nvl(t.cst_inilifo,0) cst_inilifo,nvl(t.cst_finlifo,0) cst_finlifo,nvl(t.cst_repo,0) cst_repo,t.cst_usuadd,t.cst_fecadd,t.cst_usumod,t.cst_fecmod,"
                    + " pack_tproveedores.f_001_descProveedor(t.prov_id) prov_des,pack_tproductos.f_002_deslargaProducto(t.pro_id) prod_des from tcostos t "
                    + " where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id + " and t.cst_estado= 1 "
                    + s_validaproducto + " order by t.cst_periodo,t.alm_id,t.prov_id,t.pro_id";
        } else {
            SQL_LISTACOSTOS = "select t.emp_id,t.suc_id,t.cst_periodo,t.alm_id,t.prov_id,t.pro_id,t.cst_estado,nvl(t.cst_cstini,0) cst_cstini,nvl(t.cst_cstfin,0) cst_cstfin,nvl(t.cst_inilifo,0) cst_inilifo,nvl(t.cst_finlifo,0) cst_finlifo,nvl(t.cst_repo,0) cst_repo,t.cst_usuadd,t.cst_fecadd,t.cst_usumod,t.cst_fecmod,"
                    + " pack_tproveedores.f_001_descProveedor(t.prov_id) prov_des,pack_tproductos.f_002_deslargaProducto(t.pro_id) prod_des from tcostos t "
                    + " where t.suc_id=" + suc_id + " and t.emp_id=" + emp_id + " and t.cst_estado= 1 "
                    + s_validaperiodo + s_validaalmacen + s_validaproveedor + s_validaproducto + " order by t.cst_periodo,t.alm_id,t.prov_id,t.pro_id";
        }

        P_WHERE = SQL_LISTACOSTOS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTACOSTOS);
            objlstCostos = new ListModelList<Costos>();
            while (rs.next()) {
                objCostos = new Costos();
                objCostos.setEmp_id(rs.getInt("emp_id"));
                objCostos.setSuc_id(rs.getInt("suc_id"));
                objCostos.setCst_periodo(rs.getString("cst_periodo"));
                objCostos.setAlm_id(rs.getString("alm_id"));
                objCostos.setProv_id(rs.getString("prov_id"));
                objCostos.setProd_id(rs.getString("pro_id"));
                objCostos.setCst_estado(rs.getInt("cst_estado"));
                objCostos.setCst_cstini(rs.getDouble("cst_cstini"));
                objCostos.setCst_cstfin(rs.getDouble("cst_cstfin"));
                objCostos.setCst_inilifo(rs.getDouble("cst_inilifo"));
                objCostos.setCst_finlifo(rs.getDouble("cst_finlifo"));
                objCostos.setCst_repo(rs.getDouble("cst_repo"));
                objCostos.setCst_usuadd(rs.getString("cst_usuadd"));
                objCostos.setCst_fecadd(rs.getDate("cst_fecadd"));
                objCostos.setCst_usumod(rs.getString("cst_usumod"));
                objCostos.setCst_fecmod(rs.getDate("cst_fecmod"));
                //objCostos.setProv_des(rs.getString("prov_des"));
                objCostos.setProv_des(rs.getString("prov_des").length() < 28 ? rs.getString("prov_des") : rs.getString("prov_des").substring(0, 28));
                objCostos.setProd_des(rs.getString("prod_des").length() < 35 ? rs.getString("prod_des") : rs.getString("prod_des").substring(0, 35));
                //objCostos.setProd_des(rs.getString("prod_des"));
                objlstCostos.add(objCostos);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstCostos.size() + " Registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
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
        return objlstCostos;
    }
}
