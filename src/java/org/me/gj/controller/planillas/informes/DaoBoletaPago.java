/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.Boleta;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoBoletaPago {

    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    int i_flagErrorBD = 0;
    String s_msg = "";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    ListModelList<Boleta> objlstBoleta;
    Boleta objBoleta;
    public static String P_WHERE = "";
    public static String P_COSTOS = "";
     public static String P_ALL = "";
    public static String PRIMERO = "";
    public static String SEGUNDO = "";
    public static String TERCERO = "";
    

      //para boleta
   /* public ListModelList<Boleta> consultaBoleta(String sucursal, String codigo, String tabla, String periodo) throws SQLException {

     String sql_query = "{call codijisa.pack_planilla_informes.p_informes_boleta(?,?,?,?,?,?)}";
     try {
     con = new ConectaBD().conectar();
     if (con != null) {
     cst = con.prepareCall(sql_query);
     cst.setInt(1, objUsuCredential.getCodemp());
     cst.setString(2, sucursal);
     cst.setString(3, codigo);
     cst.setString(4, tabla);
     cst.setString(5, periodo);
     cst.registerOutParameter(6, OracleTypes.CURSOR); //REF CURSOR
     cst.execute();
     rs = ((OracleCallableStatement) cst).getCursor(6);
     objlstBoleta = null;
     objlstBoleta = new ListModelList<Boleta>();

     while (rs.next()) {
     objBoleta = new Boleta();
     objBoleta.setAfp(rs.getString("afp"));
     objBoleta.setApenom(rs.getString("apenom"));
     objBoleta.setCargo(rs.getString("cargo"));
     objBoleta.setCodigo(rs.getString("codigo"));
     objBoleta.setDiastrab(rs.getString("diastrab"));
     objBoleta.setDire(rs.getString("dire"));
     objBoleta.setEmpresa(rs.getString("empresa"));
     objBoleta.setFaltas(rs.getString("faltas"));
     objBoleta.setHorasext(rs.getString("horasext"));
     objBoleta.setHorastrab(rs.getString("horastrab"));
     objBoleta.setNeto(rs.getDouble("neto"));
     objBoleta.setNrodoc(rs.getString("nrodoc"));
     objBoleta.setPlcarafp(rs.getString("plcarafp"));
     objBoleta.setPlcarssp(rs.getString("plcarssp"));
     objBoleta.setPlfecces(rs.getString("plfecces"));
     objBoleta.setPlfecing(rs.getString("plfecing"));
     objBoleta.setPlvacfin(rs.getString("plvacfin"));
     objBoleta.setPlvacini(rs.getString("plvacini"));
     objBoleta.setRuc(rs.getString("ruc"));
     objBoleta.setSituacion(rs.getString("situacion"));
     objBoleta.setTipdoc(rs.getString("tipdoc"));
     objBoleta.setTotdscto(rs.getDouble("totdscto"));
     objBoleta.setToting(rs.getDouble("toting"));
     objlstBoleta.add(objBoleta);
     }
     }
     } catch (SQLException e) {
     Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
     } catch (NullPointerException e) {
     Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
     } finally {
     if (con != null) {
     cst.close();
     rs.close();
     con.close();
     }
     }

     return objlstBoleta;
     }*/
    //para boleta en string 16/10/2017 jr
    public ListModelList<Boleta> consultaBoleta(String sucursal, String codigo, String tabla, String periodo, String costo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";
        String s_codigo = codigo.isEmpty() ? "" : " and p.pltipdoc||p.plnrodoc in ('" + codigo + "')";
        String p_codigo = codigo.isEmpty() ? "" : " and t.pltipdoc||t.plnrodoc in ('" + codigo + "')";
        String scosto = costo.isEmpty() ? "" : "  and d.plccosto in ('" + costo + "')";
        String query = "    select distinct p.pltipdoc||p.plnrodoc codigo, p.plapepat || ' ' || p.plapemat || ' ' || p.plnomemp apenom,e.emp_sig empresa,e.emp_ruc ruc, e.emp_dir dire,"
                + " pack_tpersonal.ftb1_descripcion(d.plidcargo,'00006') cargo,"
                + " to_char(d.plfecing,'dd/mm/yyyy')plfecing,"
                + " p.pltipdoc || '-' || pack_tpltablas1.f_descrip('00006', p.pltipdoc) tipdoc,"
                + " p.plnrodoc nrodoc,"
                + " UPPER(decode(d.pl_cc , '1' , 'Trabajador de Dirección','2','Trabajador de Confianza','Ninguna')) situacion,"
                + " (select min(t.fecha) plvacini from codijisa.tplasisten t where t.emp_id = " + objUsuCredential.getCodemp() + " and t.periodo = substr(" + periodo + "" + ",1,6) " + p_codigo + " and t.fecha between to_date('01/' || substr(" + periodo + "" + ", 5, 2) || '/' || substr(" + periodo + "" + ", 1, 4), 'dd/mm/yyyy') and to_date(''||pack_tcalculoplla.f_dt(substr(" + periodo + "" + ",1,4), '020' , substr(" + periodo + "" + ",5,2))||'/' || substr(" + periodo + "" + ", 5, 2) || '/' || substr(" + periodo + "" + ", 1, 4), 'dd/mm/yyyy') and t.tip_asis = '2') plvacini,"
                + " (select max(t.fecha) plvacini from codijisa.tplasisten t where t.emp_id = " + objUsuCredential.getCodemp() + " and t.periodo = substr(" + periodo + "" + ",1,6) " + p_codigo + " and t.fecha between to_date('01/' || substr(" + periodo + "" + ", 5, 2) || '/' || substr(" + periodo + "" + ", 1, 4), 'dd/mm/yyyy') and to_date(''||pack_tcalculoplla.f_dt(substr(" + periodo + "" + ",1,4), '020' , substr(" + periodo + "" + ",5,2))||'/' || substr(" + periodo + "" + ", 5, 2) || '/' || substr(" + periodo + "" + ", 1, 4), 'dd/mm/yyyy') and t.tip_asis = '2') plvacfin,"
                + " d.plcarssp,"
                + " a.plcarafp,"
                + " case when substr(" + periodo + "" + ",7,2) in ('00','01','04','07','08') then 0 else pack_tcalculoplla.f_as_boleta(d.emp_id, substr(" + periodo + "" + ",1,6), d.suc_id,p.pltipdoc,p.plnrodoc, '0') end diastrab,"
                + " case when substr(" + periodo + "" + ",7,2) in ('00','01','04','07','08') then 0 else pack_tcalculoplla.f_as_boleta(d.emp_id, substr(" + periodo + "" + ",1,6), d.suc_id,p.pltipdoc,p.plnrodoc , '0') * 8 end horastrab,"
                + " case when substr(" + periodo + "" + ",7,2) in ('00','01','04','07','08') then 0 else pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '066') end faltas,"
                + " case when substr(" + periodo + "" + ",7,2) in ('00','01','04','07','08') then 0 else pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ",  p.pltipdoc||p.plnrodoc, '114') end horasext,"
                + " to_char(d.plfecces,'dd/mm/yyyy') plfecces,"
                + " pack_tpltablas1.f_descrip('00005', a.plcodafp) afp,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '088') toting,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '103') totdscto,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '124') neto,"
                + " case when substr(" + periodo + "" + ", 7, 2) in ('00', '01', '04', '07', '08') then 0 else pack_tcalculoplla.f_as_boleta(d.emp_id,substr(" + periodo + "" + ", 1, 6),d.suc_id,p.pltipdoc,p.plnrodoc,'9') end diassubsidio,"//agregado 14/12/2017
                + " case when substr(" + periodo + "" + ", 7, 2) in ('00', '01', '04', '07', '08') then 0 else ( pack_tcalculoplla.f_as_boleta(d.emp_id,substr(" + periodo + "" + ", 1, 6),d.suc_id,p.pltipdoc,p.plnrodoc,'1') +   pack_tcalculoplla.f_as_boleta(d.emp_id,substr(" + periodo + "" + ", 1, 6),d.suc_id,p.pltipdoc,p.plnrodoc,'8')) end diasnosubsidio," //agregado
                + " d.plccosto,"
                + " pack_tpersonal.fcco_descripcion(d.emp_id,d.plccosto) plccosto_des,"
                + " pack_tpersonal.ftb1_descripcion(d.plarea,'00003') plarea_des"
                + " from tpersonal p,tpldatoslab d,tplperaport a,tempresas e," + tabla + " px"
                + " where p.pltipdoc = d.pltipdoc"
                + " and p.plnrodoc = d.plnrodoc"
                + " and p.pltipdoc = a.pltipdoc"
                + " and p.plnrodoc = a.plnrodoc"
                + " and px.pltipdoc = d.pltipdoc"
                + " and px.plnrodoc = d.plnrodoc"
                + " and px.emp_id = d.emp_id"
                + " and px.suc_id = d.suc_id"
                + " and d.emp_id = e.emp_id"
                + " and d.emp_id = " + objUsuCredential.getCodemp() + ""
                + " and p.plestado = 1"
                + " and d.plestado_dl = 1"
                + " and a.plestado_ap = 1"
                + " and px.plmonto > 0"
                + s_codigo + s_sucursal + scosto;
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setAfp(rs.getString("afp"));
                objBoleta.setApenom(rs.getString("apenom"));
                objBoleta.setCargo(rs.getString("cargo"));
                objBoleta.setCodigo(rs.getString("codigo"));
                objBoleta.setDiastrab(rs.getString("diastrab"));
                objBoleta.setDire(rs.getString("dire"));
                objBoleta.setEmpresa(rs.getString("empresa"));
                objBoleta.setFaltas(rs.getString("faltas"));
                objBoleta.setHorasext(rs.getString("horasext"));
                objBoleta.setHorastrab(rs.getString("horastrab"));
                objBoleta.setNeto(rs.getDouble("neto"));
                objBoleta.setNrodoc(rs.getString("nrodoc"));
                objBoleta.setPlcarafp(rs.getString("plcarafp"));
                objBoleta.setPlcarssp(rs.getString("plcarssp"));
                objBoleta.setPlfecces(rs.getString("plfecces"));
                objBoleta.setPlfecing(rs.getString("plfecing"));
                objBoleta.setPlvacfin(rs.getString("plvacfin"));
                objBoleta.setPlvacini(rs.getString("plvacini"));
                objBoleta.setRuc(rs.getString("ruc"));
                objBoleta.setSituacion(rs.getString("situacion"));
                objBoleta.setTipdoc(rs.getString("tipdoc"));
                objBoleta.setTotdscto(rs.getDouble("totdscto"));
                objBoleta.setToting(rs.getDouble("toting"));
                objBoleta.setCosto(rs.getString("plccosto"));
                objBoleta.setDiasubsidio(rs.getString("diassubsidio"));
                objBoleta.setDiasnosubsidio(rs.getString("diasnosubsidio"));
                objlstBoleta.add(objBoleta);
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
        return objlstBoleta;

    }

    //para dataset primero 
    public ListModelList<Boleta> muestraDataSetPrimero(String sucursal, String codigo, String tabla, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and p.suc_id = " + sucursal + "";
        String s_codigo = codigo.isEmpty() ? "" : "  and p.pltipdoc||p.plnrodoc in ('" + codigo + "')";

        String query = " select distinct p.plcodcon idcon1, pack_tpltablas1.f_descrip('00001', p.plcodcon) cpto1,"
                + " p.plmonto mto1"
                + " from  " + tabla + " p"
                + " where p.emp_id = " + objUsuCredential.getCodemp() + ""
                + " and p.plppag_id ='" + periodo + "'"
                + s_sucursal
                + s_codigo
                + " and p.plcodcon in (select distinct t.tabla_id"
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                + " and t.tabla_id <> '000'"
                + " and t.tabla_datbol in('1', '7'))"
                + " and p.plmonto > 0"
                + " order by p.plcodcon";
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        PRIMERO = query;

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setMto1(rs.getString("mto1"));
                objlstBoleta.add(objBoleta);

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
        return objlstBoleta;
    }

    //para dataset segundo 
    public ListModelList<Boleta> muestraDataSetSegundo(String sucursal, String codigo, String tabla, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and p.suc_id = " + sucursal + "";
        String s_codigo = codigo.isEmpty() ? "" : "  and p.pltipdoc||p.plnrodoc in ('" + codigo + "')";

        String query = " select distinct p.plcodcon idcon2, pack_Tpltablas1.f_descrip('00001', p.plcodcon) cpto2,"
                + " p.plmonto mto2"
                + " from " + tabla + " p"
                + " where p.emp_id =" + objUsuCredential.getCodemp() + ""
                + " and p.plppag_id ='" + periodo + "'"
                + s_sucursal + s_codigo
                + " and p.plcodcon in (select distinct t.tabla_id"
                + " from Tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                + " and t.tabla_id <> '000'"
                + " and t.tabla_datbol in('2', '7'))"
                + " and p.plmonto > 0"
                + " order by p.plcodcon";
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        SEGUNDO = query;

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setMto2(rs.getString("mto2"));
                objlstBoleta.add(objBoleta);

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
        return objlstBoleta;
    }

    //para dataset tercero 
    public ListModelList<Boleta> muestraDataSetTercero(String sucursal, String codigo, String tabla, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and p.suc_id = " + sucursal + "";
        String s_codigo = codigo.isEmpty() ? "" : "  and p.pltipdoc||p.plnrodoc in ('" + codigo + "')";

        String query = " select distinct p.plcodcon idcon3, pack_tpltablas1.f_descrip('00001', p.plcodcon) cpto3,"
                + " p.plmonto mto3"
                + " from " + tabla + " p"
                + " where p.emp_id =" + objUsuCredential.getCodemp() + ""
                + " and p.plppag_id ='" + periodo + "'"
                + s_sucursal + s_codigo
                + " and p.plcodcon in (select distinct t.tabla_id"
                + " from Tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                + " and t.tabla_id <> '000'"
                + " and t.tabla_datbol = '3')"
                + " and p.plmonto > 0"
                + " order by p.plcodcon";
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        TERCERO = query;

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setMto3(rs.getString("mto3"));
                objlstBoleta.add(objBoleta);

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
        return objlstBoleta;
    }

    /**
     * funcionm para traer funcion
     *
     * @param sucursal
     * @param codigo
     * @param tabla
     * @param periodo
     * @return 
     * @throws java.sql.SQLException 
     */
    public ListModelList<Boleta> consultaBoleta1(String sucursal, String codigo, String tabla, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";
        String s_codigo = codigo.isEmpty() ? "" : " and p.pltipdoc||p.plnrodoc in ('" + codigo + "')";
        String p_codigo = codigo.isEmpty() ? "" : " and t.pltipdoc||t.plnrodoc in ('" + codigo + "')";
        String query;

        try {
            con = new ConectaBD().conectar();
            query = "{?=call codijisa.pack_venta_zona.f_consulta_detalle(?,?,?,?,?,?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3, s_sucursal);
            cst.setString(4, s_codigo);
            cst.setString(5, p_codigo);
            cst.setString(6, tabla);
            cst.setString(7, periodo);
            cst.execute();
            String consulta = cst.getString(1);
            P_WHERE = consulta;
            objlstBoleta = null;
            objlstBoleta = new ListModelList<Boleta>();
            st = con.createStatement();
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setAfp(rs.getString("afp"));
                objBoleta.setApenom(rs.getString("apenom"));
                objBoleta.setCargo(rs.getString("cargo"));
                objBoleta.setCodigo(rs.getString("codigo"));
                objBoleta.setDiastrab(rs.getString("diastrab"));
                objBoleta.setDire(rs.getString("dire"));
                objBoleta.setEmpresa(rs.getString("empresa"));
                objBoleta.setFaltas(rs.getString("faltas"));
                objBoleta.setHorasext(rs.getString("horasext"));
                objBoleta.setHorastrab(rs.getString("horastrab"));
                objBoleta.setNeto(rs.getDouble("neto"));
                objBoleta.setNrodoc(rs.getString("nrodoc"));
                objBoleta.setPlcarafp(rs.getString("plcarafp"));
                objBoleta.setPlcarssp(rs.getString("plcarssp"));
                objBoleta.setPlfecces(rs.getString("plfecces"));
                objBoleta.setPlfecing(rs.getString("plfecing"));
                objBoleta.setPlvacfin(rs.getString("plvacfin"));
                objBoleta.setPlvacini(rs.getString("plvacini"));
                objBoleta.setRuc(rs.getString("ruc"));
                objBoleta.setSituacion(rs.getString("situacion"));
                objBoleta.setTipdoc(rs.getString("tipdoc"));
                objBoleta.setTotdscto(rs.getDouble("totdscto"));
                objBoleta.setToting(rs.getDouble("toting"));
                //  objBoleta.setSucursal(rs.getString("suc_id"));
                objlstBoleta.add(objBoleta);
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
        return objlstBoleta;

    }

    public ListModelList<Boleta> consultaCostos(String sucursal, String tabla, String periodo, String costo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";

        String scosto = costo.isEmpty() ? "" : "  and d.plccosto in ('" + costo + "')";
        String query = " select empresa,ruc,dire,plccosto,plccosto_des, sum(toting) as toting,sum(totdscto) as totdscto,sum(neto) as neto from ("
                + " select distinct e.emp_sig empresa,e.emp_ruc ruc, e.emp_dir dire,"
                + " d.plccosto,"
                + " pack_tpersonal.fcco_descripcion(d.emp_id,d.plccosto) plccosto_des,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '088') toting,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '103') totdscto,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '124') neto"
                + " from tpersonal p,tpldatoslab d,tplperaport a,tempresas e," + tabla + " px"
                + " where p.pltipdoc = d.pltipdoc"
                + " and p.plnrodoc = d.plnrodoc"
                + " and p.pltipdoc = a.pltipdoc"
                + " and p.plnrodoc = a.plnrodoc"
                + " and px.pltipdoc = d.pltipdoc"
                + " and px.plnrodoc = d.plnrodoc"
                + " and px.emp_id = d.emp_id"
                + " and px.suc_id = d.suc_id"
                + " and d.emp_id = e.emp_id"
                + " and d.emp_id = " + objUsuCredential.getCodemp() + ""
                + " and p.plestado = 1"
                + " and d.plestado_dl = 1"
                + " and a.plestado_ap = 1"
                + " and px.plmonto > 0"
                + s_sucursal + scosto + " ) group by empresa,ruc,dire,plccosto,plccosto_des";
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        P_COSTOS = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();

                objBoleta.setDire(rs.getString("dire"));
                objBoleta.setEmpresa(rs.getString("empresa"));
                ;
                objBoleta.setRuc(rs.getString("ruc"));
                //
                objBoleta.setCosto(rs.getString("plccosto"));
                //  objBoleta.setSucursal(rs.getString("suc_id"));
                objlstBoleta.add(objBoleta);
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
        return objlstBoleta;

    }

    //para todo en uno
    public ListModelList<Boleta> consultaJim(String sucursal, String tabla, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";

       // String scosto = costo.isEmpty() ? "" : "  and d.plccosto in ('" + costo + "')";
        String query = " select empresa,ruc, dire,sum(toting) as toting,sum(totdscto) as totdscto,sum(neto) as neto from ("
                + " select distinct e.emp_sig empresa,e.emp_ruc ruc, e.emp_dir dire,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '088') toting,"//se agrego estas tres filas
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '103') totdscto,"
                + " pack_tcalculoplla.f_valcon('" + tabla + "'" + ", d.emp_id, d.suc_id, " + periodo + "" + ", p.pltipdoc||p.plnrodoc, '124') neto"
                + " from tpersonal p,tpldatoslab d,tplperaport a,tempresas e," + tabla + " px"
                + " where p.pltipdoc = d.pltipdoc"
                + " and p.plnrodoc = d.plnrodoc"
                + " and p.pltipdoc = a.pltipdoc"
                + " and p.plnrodoc = a.plnrodoc"
                + " and px.pltipdoc = d.pltipdoc"
                + " and px.plnrodoc = d.plnrodoc"
                + " and px.emp_id = d.emp_id"
                + " and px.suc_id = d.suc_id"
                + " and d.emp_id = e.emp_id"
                + " and d.emp_id = " + objUsuCredential.getCodemp() + ""
                + " and p.plestado = 1"
                + " and d.plestado_dl = 1"
                + " and a.plestado_ap = 1"
                + " and px.plmonto > 0"
                + s_sucursal + " ) group by empresa, ruc, dire";
        objlstBoleta = null;
        objlstBoleta = new ListModelList<Boleta>();
        P_ALL = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objBoleta = new Boleta();
                objBoleta.setDire(rs.getString("dire"));
                objBoleta.setEmpresa(rs.getString("empresa"));
                objBoleta.setRuc(rs.getString("ruc"));
                //
              //  objBoleta.setCosto(rs.getString("plccosto"));
                //  objBoleta.setSucursal(rs.getString("suc_id"));
                objlstBoleta.add(objBoleta);
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
        return objlstBoleta;

    }
}
