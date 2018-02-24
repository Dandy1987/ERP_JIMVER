package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.InvFisico;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoInvFisFormato {

    ListModelList<InvFisico> objlstInvFisico = new ListModelList<InvFisico>();
    InvFisico objInvFisico;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoInvFisFormato.class);

    public ListModelList<InvFisico> listaInventario(String periodo, String almacen, String provid, String artid, String stock) throws SQLException {
        String SQL_INVENTARIOS = "select t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) proveedor ,t.pro_id artid, "
                + "t.pro_des artdes, t.pro_unimanven undman, t.pro_presminven undmed from tproductos t";
        //todos seleccionados
        if (!almacen.equals("") && !provid.equals("") && !artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' and t2.alm_key in ('" + almacen + "')"
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_id in ('" + artid + "') "
                    + " and t.pro_provid in ('" + provid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //todos vacios
        } else if (almacen.equals("") && provid.equals("") && artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' "
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //almacen y proveedor seleccionada
        } else if (!almacen.equals("") && !provid.equals("") && artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' and t2.alm_key in ('" + almacen + "')"
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_provid in ('" + provid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //almacen y articulo seleccionadas
        } else if (!almacen.equals("") && provid.equals("") && !artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' and t2.alm_key in ('" + almacen + "')"
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_id in ('" + artid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //proveedor y articulo seleccionadas
        } else if (almacen.equals("") && !provid.equals("") && !artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' "
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_id in ('" + artid + "') "
                    + " and t.pro_provid in ('" + provid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //solo proveedor seleccionada
        } else if (almacen.equals("") && !provid.equals("") && artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' "
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_provid in ('" + provid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //solo articulo seleccionada
        } else if (almacen.equals("") && provid.equals("") && !artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' "
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " and t.pro_id in ('" + artid + "') "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
            //solo almacen seleccionada
        } else if (!almacen.equals("") && provid.equals("") && artid.equals("")) {
            P_WHERE = " where ( t.pro_id in( select t2.pro_id from tstocks t2 "
                    + " where t2.emp_id='" + objUsuarioCredential.getCodemp() + "' and t2.suc_id = '" + objUsuarioCredential.getCodsuc() + "' "
                    + " and t2.per_key='" + periodo + "' and t2.alm_key in ('" + almacen + "')"
                    + " and t2.stk_actual " + stock + " 0 )) "
                    + " order by t.pro_provid||'-'||codijisa.pack_tproveedores.f_001_descProveedor(t.pro_provid) ,t.pro_id ";
        }
        SQL_INVENTARIOS += P_WHERE;
        objlstInvFisico = null;
        objlstInvFisico = new ListModelList<InvFisico>();
        try {
            objInvFisico = null;
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVENTARIOS);
            while (rs.next()) {
                objInvFisico = new InvFisico();
                objInvFisico.setProveedor(rs.getString("proveedor"));
                objInvFisico.setArtid(rs.getString("artid"));
                objInvFisico.setArtdes(rs.getString("artdes"));
                objInvFisico.setUndmed(rs.getString("undmed"));
                objInvFisico.setUndman(rs.getString("undman"));;
                objlstInvFisico.add(objInvFisico);
            }
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisico.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstInvFisico;
    }
//
//    public ListModelList<InvFisico> listaRepInventario(String periodo, String almacen, String provid, String observacion) throws SQLException {
//        //todos seleccionados
//        if (!almacen.equals("") && !provid.equals("") && !observacion.equals(" ")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia , "
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " and t.prov_id in ('" + provid + "') "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 and alm_key in ('" + almacen + "')) "
//                    + " where observacion is not null and observacion like '" + observacion + "' order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //todos vacios
//        } else if (almacen.equals("") && provid.equals("") && observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico ,"
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 ) "
//                    + " where observacion is not null order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //almacen y proveedor seleccionada
//        } else if (!almacen.equals("") && !provid.equals("") && observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO  from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "                    
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " and t.prov_id in ('" + provid + "') "
//                    + " group by t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 and alm_key in ('" + almacen + "')) "
//                    + " where observacion is not null order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //almacen y observacion seleccionadas
//        } else if (!almacen.equals("") && provid.equals("") && !observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " grupo "   
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " group by t.emp_id , t.per_key ,t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 and alm_key in ('" + almacen + "')) "
//                    + " where observacion is not null and observacion like '" + observacion + "' order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //proveedor y observacion seleccionadas
//        } else if (almacen.equals("") && !provid.equals("") && !observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO  from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " and t.prov_id in ('" + provid + "') "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 ) "
//                    + " where observacion is not null and observacion like '" + observacion + "' order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //solo proveedor seleccionada
//        } else if (almacen.equals("") && !provid.equals("") && observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " and t.prov_id in ('" + provid + "') "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0) "
//                    + " where observacion is not null order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //solo observacion seleccionada
//        } else if (almacen.equals("") && provid.equals("") && !observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO  from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 ) "
//                    + " where observacion is not null and observacion like '" + observacion + "' order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//            //solo almacen seleccionada
//        } else if (!almacen.equals("") && provid.equals("") && observacion.equals("")) {
//            P_WHERE = "select ARTID, ARTDES,PROV_ID || ' - ' || PROV_RAZSOC PROVEEDOR, UNDMED ,UNDMAN,FISICO,OBSERVACION , GRUPO  from "
//                    + "( select   ART_ID ARTID, ART_DESCRI ARTDES, PROV_ID, PROV_RAZSOC, TALM_ID, UNDMED, UNDMAN,Sistema, "
//                    + " trunc(Fisico,2) fisico, Diferencia, ValSistema, ValFisico , "
//                    + " case when Fisico = 0  then  'NO HA CONTADO!' when diferencia < 9.99 and diferencia > -4.99 then 'OK' when diferencia > 10 then 'RECONTAR' when diferencia < -5 then 'RECONTAR URGENTE!!!'  end observacion , "
//                    + " grupo "
//                    + " from ( "
//                    + " SELECT t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, t.art_undpre undmed ,t.art_undman undman, "
//                    + " sum((t.sistema)/t.art_undpre) Sistema, sum((t.fisico)/t.art_undpre) Fisico, sum((t.diferencia)/t.art_undpre) Diferencia,"
//                    + " sum( (t.sistema/t.art_undpre) * t2.cst_cstfina ) ValSistema, sum( (t.fisico/t.art_undpre) * t2.cst_cstfina ) ValFisico , "
//                    + " (select count(i.fisicab_grupo) from invfisico_det i where i.emp_id = t.emp_id and i.per_periodo = t.per_key and i.alm_key = t.alm_key and i.prov_id = t.prov_id and i.pro_id = t.pro_id and nvl(i.fisidet_stk,0) <> 0 ) grupo "                    
//                    + " FROM CODIJISA.vis_difestock t, costos t2"
//                    + " WHERE t.emp_id=t2.emp_id(+) "
//                    + " and t.pro_id=t2.pro_id(+) "
//                    + " and t.prov_id=t2.prov_id(+) "
//                    + " and t.per_key=t2.cst_periodo(+) "
//                    + " and t.alm_key=t2.alm_key(+) "
//                    + " and t.EMP_ID='" + objUsuarioCredential.getCodemp() + "' "
//                    + " and t.STK_PERIODO='" + periodo + "' "
//                    + " group by t.emp_id , t.per_key , t.ART_ID, t.ART_DESCRI, t.PROV_ID, t.PROV_RAZSOC, t.TALM_ID, T.ART_UNDPRE ,T.ART_UNDMAN "
//                    + " ORDER BY t.PROV_ID, t.ART_ID)"
//                    + " where diferencia <> 0 and alm_key in ('" + almacen + "')) "
//                    + " where observacion is not null order by PROV_ID || ' - ' || PROV_RAZSOC,ARTID";
//        }
//        //SQL_INVENTARIOS += P_WHERE;
//        InvFisico objInvFisico = null;
//        ListModelList<InvFisico> objlstInvFisico = new ListModelList<InvFisico>();
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            st = con.createStatement();
//            rs = st.executeQuery(P_WHERE);
//            while (rs.next()) {
//                objInvFisico = new InvFisico();
//                objInvFisico.setProveedor(rs.getString("proveedor"));
//                objInvFisico.setArtid(rs.getString("artid"));
//                objInvFisico.setArtdes(rs.getString("artdes"));
//                objInvFisico.setUndmed(rs.getString("undmed"));
//                objInvFisico.setUndman(rs.getString("undman"));
//                objInvFisico.setFisico(rs.getDouble("fisico"));
//                objInvFisico.setObservacion(rs.getString("observacion"));
//                objInvFisico.setGrupo(rs.getString("grupo"));
//                objlstInvFisico.add(objInvFisico);
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objInvFisico = null;
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objInvFisico = null;
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objlstInvFisico;
//    }

//
//    public Proveedores getProveedor(String emp_id, String prov_id) throws SQLException {
//        String SQL_PROVEEDORES = "select distinct t.prov_id,t.prov_razsoc from tproveedor t where t.emp_id='" + emp_id + "' and prov_id='" + prov_id + "'";
//        Proveedores objProveedores = null;
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            st = con.createStatement();
//            rs = st.executeQuery(SQL_PROVEEDORES);
//            while (rs.next()) {
//                objProveedores = new Proveedores();
//                objProveedores.setProv_id(rs.getString("prov_id"));
//                objProveedores.setProv_razsoc(rs.getString("prov_razsoc"));
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objProveedores = null;
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objProveedores = null;
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objProveedores;
//    }
//
//    public Productos getProducto(String emp_id, String prov_id, String pro_id) throws SQLException {
//        String SQL_PRODUCTO, filtro;
//        Productos objProductos = null;
//        if (!prov_id.isEmpty() && !pro_id.isEmpty()) {
//            filtro = "and t.prov_id='" + prov_id + "' and p.pro_id='" + pro_id + "'";
//        } else if (prov_id.isEmpty() && !pro_id.isEmpty()) {
//            filtro = "and p.pro_id='" + pro_id + "'";
//        } else {
//            filtro = "";
//        }
//        SQL_PRODUCTO = "select t.prov_id,t.prov_razsoc,p.pro_id,p.art_descri from proveedor t,articulos p "
//                + "where t.emp_id=p.emp_id and t.prov_id=p.prov_id "
//                + "and t.emp_id='" + emp_id + "' " + filtro + " order by t.prov_id,p.pro_id";
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            st = con.createStatement();
//            rs = st.executeQuery(SQL_PRODUCTO);
//            while (rs.next()) {
//                objProductos = new Productos();
//                objProductos.setPro_provid(rs.getString("prov_id"));
//                objProductos.setPro_provrazsoc(rs.getString("prov_razsoc"));
//                objProductos.setPro_id(rs.getString("pro_id"));
//                objProductos.setPro_des(rs.getString("art_descri"));
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objProductos = null;
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//            objProductos = null;
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objProductos;
//    }
//
//    public ListModelList<Proveedores> getProveedores(String emp_id) throws SQLException {
//        String SQL_PROVEEDORES = "select distinct t.prov_id,t.prov_razsoc from proveedor t where t.emp_id='" + emp_id + "' order by t.prov_id";
//        ListModelList<Proveedores> objlstProveedores = new ListModelList<Proveedores>();
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            if (con != null) {
//                st = con.createStatement();
//                rs = st.executeQuery(SQL_PROVEEDORES);
//                while (rs.next()) {
//                    Proveedores objProveedores = new Proveedores();
//                    objProveedores.setProv_id(rs.getString("prov_id"));
//                    objProveedores.setProv_razsoc(rs.getString("prov_razsoc"));
//                    objlstProveedores.add(objProveedores);
//                }
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objlstProveedores;
//    }
//
//    public ListModelList<Productos> getProductos(String emp_id, String prov_id) throws SQLException {
//        String SQL_PRODUCTOS, filtro;
//        filtro = prov_id.isEmpty() ? "" : "and t.prov_id='" + prov_id + "'";
//        ListModelList<Productos> objlstProductos = new ListModelList<Productos>();
//        SQL_PRODUCTOS = "select t.prov_id,t.prov_razsoc,p.pro_id,p.art_descri , "
//                + "round(nvl(pack_tstocks.f_stock(p.emp_id, to_char(sysdate,'YYYYMM'), p.pro_id,'', '101', '', 'S')/p.art_undpre,0),2) stock"
//                + " from proveedor t,articulos p "
//                + "where t.emp_id=p.emp_id and t.prov_id=p.prov_id "
//                + "and t.emp_id='" + emp_id + "' " + filtro + " order by t.prov_id,p.pro_id";
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            if (con != null) {
//                st = con.createStatement();
//                rs = st.executeQuery(SQL_PRODUCTOS);
//                while (rs.next()) {
//                    Productos objProductos = new Productos();
//                    objProductos.setPro_provid(rs.getString("prov_id"));
//                    objProductos.setPro_provrazsoc(rs.getString("prov_razsoc"));
//                    objProductos.setPro_id(rs.getString("pro_id"));
//                    objProductos.setPro_des(rs.getString("art_descri"));
//                    objProductos.setPro_stock(rs.getDouble("stock"));
//                    objlstProductos.add(objProductos);
//                }
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objlstProductos;
//    }
//
//
//    public ListModelList<Proveedores> busquedaProveedor(String emp_id, String busqueda) throws SQLException {
//        String filtro = busqueda.isEmpty() ? "" : "and (t.prov_id like '" + busqueda + "' or t.prov_razsoc like '" + busqueda + "')";
//        String SQL_PROVEEDORES = "select distinct t.prov_id,t.prov_razsoc from proveedor t where  t.emp_id='" + emp_id + "' " + filtro + " order by t.prov_id";
//        ListModelList<Proveedores> objlstProveedores = new ListModelList<Proveedores>();
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            if (con != null) {
//                st = con.createStatement();
//                rs = st.executeQuery(SQL_PROVEEDORES);
//                while (rs.next()) {
//                    Proveedores objProveedores = new Proveedores();
//                    objProveedores.setProv_id(rs.getString("prov_id"));
//                    objProveedores.setProv_razsoc(rs.getString("prov_razsoc"));
//                    objlstProveedores.add(objProveedores);
//                }
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objlstProveedores;
//    }
//
//    public ListModelList<Productos> busquedaProducto(String emp_id, String prov_id, String busqueda) throws SQLException {
//        String SQL_PRODUCTOS, filtro, filtro2;
//        filtro = prov_id.isEmpty() ? "" : "and t.prov_id='" + prov_id + "'";
//        filtro2 = busqueda.isEmpty() ? "" : "and (p.pro_id like '" + busqueda + "' or p.art_descri like '" + busqueda + "') order by t.prov_id,p.pro_id";
//        ListModelList<Productos> objlstProductos = new ListModelList<Productos>();
//        SQL_PRODUCTOS = "select t.prov_id,t.prov_razsoc,p.pro_id,p.art_descri , "
//                + "round(nvl(pack_tstocks.f_stock(p.emp_id, to_char(sysdate,'YYYYMM'), p.pro_id,'', '101', '', 'S')/p.art_undpre,0),2) stock "
//                + "from proveedor t,articulos p "
//                + " where t.emp_id=p.emp_id and t.prov_id=p.prov_id "
//                + " and t.emp_id='" + emp_id + "' " + filtro + " " + filtro2;
//        try {
//            con = new ConectaBD().conectar(objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
//            if (con != null) {
//                st = con.createStatement();
//                rs = st.executeQuery(SQL_PRODUCTOS);
//                while (rs.next()) {
//                    Productos objProductos = new Productos();
//                    objProductos.setPro_provid(rs.getString("prov_id"));
//                    objProductos.setPro_provrazsoc(rs.getString("prov_razsoc"));
//                    objProductos.setPro_id(rs.getString("pro_id"));
//                    objProductos.setPro_des(rs.getString("art_descri"));
//                    objProductos.setPro_stock(rs.getDouble("stock"));
//                    objlstProductos.add(objProductos);
//                }
//            }
//        } catch (SQLException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } catch (NullPointerException e) {
//            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
//        } finally {
//            if (con != null) {
//                st.close();
//                rs.close();
//                con.close();
//            }
//        }
//        return objlstProductos;
//    }
//
}
