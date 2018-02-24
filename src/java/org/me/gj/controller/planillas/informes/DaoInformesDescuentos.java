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
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesDescuentos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoInformesDescuentos {

    ListModelList<InformesDescuentos> objlstDescuentos;
    InformesDescuentos objDescuentos;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoAccesos.class);

    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";

    public ListModelList<InformesDescuentos> listaSucursal() throws SQLException {
        String SQL_SUCURSALES = "select t.suc_id,t.suc_des from v_listasucursales t where t.emp_id=" + objUsuCredential.getCodemp();
        //+ " select t.emp_id,'',null,'' from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setId_sucursal(rs.getInt(1));
                objDescuentos.setDes_sucursal(rs.getString(2));
                objlstDescuentos.add(objDescuentos);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstDescuentos;
    }

    public ListModelList<InformesDescuentos> buscarSeleccion(String consulta) throws SQLException {
        String query = "select t.suc_id,t.suc_des from v_listasucursales t where t.emp_id=" + objUsuCredential.getCodemp()
                + "and (t.suc_id like '" + consulta + "' or t.suc_des like '" + consulta + "')";

        objlstDescuentos = new ListModelList<InformesDescuentos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setId_sucursal(rs.getInt("suc_id"));
                objDescuentos.setDes_sucursal(rs.getString("suc_des"));
                objlstDescuentos.add(objDescuentos);

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
        return objlstDescuentos;

    }

    public InformesDescuentos getSucursal(String sucursal) throws SQLException {
        String query = "select t.suc_id,t.suc_des from v_listasucursales t where t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.suc_id =" + sucursal;

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objDescuentos = null;
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setId_sucursal(rs.getInt("suc_id"));
                objDescuentos.setDes_sucursal(rs.getString("suc_des"));
            }
        } catch (NullPointerException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objDescuentos;
    }

    //consuta legal en informes
    public ListModelList<InformesDescuentos> consultaLegal(String sucursal, String persona, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id in ('" + sucursal + "')";
        String s_personal = persona.isEmpty() ? "" : " and d.pltipdoc||d.plnrodoc in ('" + persona + "')";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id = " + periodo + "";
        String query = " select "
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat||' '||t.plapemat||' '||t.plnomemp nombres,"
                + " d.pldc_idconc,"
                + " pack_planilla_informes.f_descripcion_constante(pldc_idconc)tabla_descri,"
                + " d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " d.pldc_fecmov,"
                + " sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999')) abono,"
                + " sum(to_number(lib.decrypt8(d.pldc_valcar),'99999990.999')) cargo,"
                + " d.emp_id,d.suc_id,t.plapepat,e.emp_des,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') area"
                + " from "
                + " tpersonal t,tpldsctos d,tpldatoslab dl,tempresas e"
                + " where"
                + " t.pltipdoc=d.pltipdoc and"
                + " d.emp_id=dl.emp_id and"
                + " d.suc_id=dl.suc_id and"
                + " e.emp_id=d.emp_id and"
                + " e.emp_id=dl.emp_id and"
                + " d.pltipdoc=dl.pltipdoc and"
                + " d.plnrodoc = dl.plnrodoc and"
                + " t.plnrodoc=d.plnrodoc and"
                + " e.emp_est=1 and"
                + " t.plestado = 1 and"
                + " d.pldc_estado = 1 and"
                + " dl.plestado_dl = 1"
                + " and d.emp_id =" + objUsuCredential.getCodemp()
                + s_sucursal
                + s_personal
                + s_periodo
                + " group by t.pltipdoc,t.plnrodoc, d.pldc_idconc,d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " t.plapepat,t.plapemat,t.plnomemp, d.emp_id,d.suc_id,d.pldc_fecmov,dl.plarea,e.emp_des"
                + " order by t.plnrodoc";

        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setCodigo(rs.getString("codigo"));
                objDescuentos.setNombres(rs.getString("nombres"));
                objDescuentos.setId_concepto(rs.getString("pldc_idconc"));
                objDescuentos.setDes_concepto(rs.getString("tabla_descri"));
                objDescuentos.setGlosa(rs.getString("pldc_glosa"));
                objDescuentos.setRegreso(rs.getString("nro_rec_ie"));
                objDescuentos.setReferencia(rs.getString("nro_rec_ref"));
                objDescuentos.setFecha_mov(rs.getDate("pldc_fecmov"));
                objDescuentos.setAbono(rs.getDouble("abono"));
                objDescuentos.setCargo(rs.getDouble("cargo"));
                objDescuentos.setAp_paterno(rs.getString("plapepat"));
                objDescuentos.setArea(rs.getString("area"));
                objDescuentos.setEmpresa(rs.getString("emp_des"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

    //consuta fecha en informes
    public ListModelList<InformesDescuentos> consultaFecha(String sucursal, String finicio, String ffinal, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id in ('" + sucursal + "')";
        String s_inicio = finicio.isEmpty() ? "" : " and d.pldc_fecmov >= to_date('" + finicio + "','dd/mm/yyyy')";
        String s_final = ffinal.isEmpty() ? "" : " and d.pldc_fecmov <= to_date('" + ffinal + "','dd/mm/yyyy')";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id = " + periodo + "";
        String query = " select "
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat||' '||t.plapemat||' '||t.plnomemp nombres,"
                + " d.pldc_idconc,"
                + " pack_planilla_informes.f_descripcion_constante(pldc_idconc)tabla_descri,"
                + " d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " d.pldc_fecmov,e.emp_des,"
                + " sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999')) abono,"
                + " sum(to_number(lib.decrypt8(d.pldc_valcar),'99999990.999')) cargo,"
                // + " sum(to_number(lib.decrypt8(d.pldc_valabo))) abono,"
                // + " sum(to_number(lib.decrypt8(d.pldc_valcar))) cargo,"
                + " d.emp_id,d.suc_id,t.plapepat,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') area"
                + " from "
                + " tpersonal t,tpldsctos d,tpldatoslab dl,tempresas e"
                + " where"
                + " t.pltipdoc=d.pltipdoc and"
                + " d.emp_id=dl.emp_id and"
                + " d.suc_id=dl.suc_id and"
                + " e.emp_id=d.emp_id and"
                + " e.emp_id=dl.emp_id and"
                + " d.pltipdoc=dl.pltipdoc and"
                + " d.plnrodoc = dl.plnrodoc and"
                + " t.plnrodoc=d.plnrodoc and"
                + " t.plestado = 1 and"
                + " e.emp_est=1 and"
                + " d.pldc_estado = 1 and"
                + " dl.plestado_dl = 1"
                + " and d.emp_id =" + objUsuCredential.getCodemp()
                + s_sucursal
                + s_inicio
                + s_final
                + s_periodo
                + " group by t.pltipdoc,t.plnrodoc, d.pldc_idconc,d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " t.plapepat,t.plapemat,t.plnomemp, d.emp_id,d.suc_id,d.pldc_fecmov,dl.plarea,e.emp_des"
                + " order by d.pldc_fecmov";

        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setCodigo(rs.getString("codigo"));
                objDescuentos.setNombres(rs.getString("nombres"));
                objDescuentos.setId_concepto(rs.getString("pldc_idconc"));
                objDescuentos.setDes_concepto(rs.getString("tabla_descri"));
                objDescuentos.setGlosa(rs.getString("pldc_glosa"));
                objDescuentos.setRegreso(rs.getString("nro_rec_ie"));
                objDescuentos.setReferencia(rs.getString("nro_rec_ref"));
                objDescuentos.setFecha_mov(rs.getDate("pldc_fecmov"));
                objDescuentos.setAbono(rs.getDouble("abono"));
                objDescuentos.setCargo(rs.getDouble("cargo"));
                objDescuentos.setAp_paterno(rs.getString("plapepat"));
                objDescuentos.setArea(rs.getString("area"));
                objDescuentos.setEmpresa(rs.getString("emp_des"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

    //consuta persona en informes
    public ListModelList<InformesDescuentos> consultaPersona(String sucursal, String persona, String periodo, String area) throws SQLException {
        String s_area= area.isEmpty() ? "" : " and '"+ area +"' like '%'||dl.plarea||'%' " ;
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id in ('" + sucursal + "')";
        String s_personal = persona.isEmpty() ? "" : " and d.pltipdoc||d.plnrodoc in ('" + persona + "')";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id = " + periodo + "";
        String query = " select "
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat||' '||t.plapemat||' '||t.plnomemp nombres,"
                + " d.pldc_idconc,"
                + " pack_planilla_informes.f_descripcion_constante(pldc_idconc)tabla_descri,"
                + " d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " d.pldc_fecmov,e.emp_des,"
                + " sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999')) abono,"
                + " sum(to_number(lib.decrypt8(d.pldc_valcar),'99999990.999')) cargo,"
                //+ " sum(to_number(lib.decrypt8(d.pldc_valabo))) abono,"
                //+ " sum(to_number(lib.decrypt8(d.pldc_valcar))) cargo,"
                + " d.emp_id,d.suc_id,t.plapepat,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') area"
                + " from "
                + " tpersonal t,tpldsctos d,tpldatoslab dl,tempresas e"
                + " where"
                + " t.pltipdoc=d.pltipdoc and"
                + " d.emp_id=dl.emp_id and"
                + " d.suc_id=dl.suc_id and"
                + " d.pltipdoc=dl.pltipdoc and"
                + " d.plnrodoc = dl.plnrodoc and"
                + " e.emp_id=d.emp_id and"
                + " e.emp_id=dl.emp_id and"
                + " t.plnrodoc=d.plnrodoc and"
                + " t.plestado = 1 and"
                + " e.emp_est=1 and"
                + " d.pldc_estado = 1 and"
                + " dl.plestado_dl = 1"
                + " and d.emp_id =" + objUsuCredential.getCodemp()
                +s_area
                + s_sucursal
                + s_personal
                + s_periodo
                + " group by t.pltipdoc,t.plnrodoc, d.pldc_idconc,d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " t.plapepat,t.plapemat,t.plnomemp, d.emp_id,d.suc_id,d.pldc_fecmov,dl.plarea,e.emp_des"
                + " order by t.plapepat";

        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setCodigo(rs.getString("codigo"));
                objDescuentos.setNombres(rs.getString("nombres"));
                objDescuentos.setId_concepto(rs.getString("pldc_idconc"));
                objDescuentos.setDes_concepto(rs.getString("tabla_descri"));
                objDescuentos.setGlosa(rs.getString("pldc_glosa"));
                objDescuentos.setRegreso(rs.getString("nro_rec_ie"));
                objDescuentos.setReferencia(rs.getString("nro_rec_ref"));
                objDescuentos.setFecha_mov(rs.getDate("pldc_fecmov"));
                objDescuentos.setAbono(rs.getDouble("abono"));
                objDescuentos.setCargo(rs.getDouble("cargo"));
                objDescuentos.setAp_paterno(rs.getString("plapepat"));
                objDescuentos.setArea(rs.getString("area"));
                objDescuentos.setEmpresa(rs.getString("emp_des"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }
	
    //consuta costo en informes
    public ListModelList<InformesDescuentos> consultaCosto(String sucursal, String costo, String periodo, String finicio, String ffinal) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id in ('" + sucursal + "')";
        String s_costo = costo.isEmpty() ? "" : "  and dl.plccosto ='" + costo + "'";
        String s_inicio = finicio.isEmpty() ? "" : " and d.pldc_fecmov >= to_date('" + finicio + "','dd/mm/yyyy')";
        String s_final = ffinal.isEmpty() ? "" : " and d.pldc_fecmov <= to_date('" + ffinal + "','dd/mm/yyyy')";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id = " + periodo + "";
        String query = " select "
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat||' '||t.plapemat||' '||t.plnomemp nombres,"
                + " d.pldc_idconc,"
                + " pack_planilla_informes.f_descripcion_constante(pldc_idconc)tabla_descri,"
                + " d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " d.pldc_fecmov,"
                + " sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999')) abono,"
                + " sum(to_number(lib.decrypt8(d.pldc_valcar),'99999990.999')) cargo,"
                //+ " sum(to_number(lib.decrypt8(d.pldc_valabo))) abono,"
                //  + " sum(to_number(lib.decrypt8(d.pldc_valcar))) cargo,"
                + " d.emp_id,d.suc_id,t.plapepat,e.emp_des,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') area"
                + " from "
                + " tpersonal t,tpldsctos d,tpldatoslab dl,tempresas e"
                + " where"
                + " t.pltipdoc=d.pltipdoc and"
                + " d.emp_id=dl.emp_id and"
                + " d.suc_id=dl.suc_id and"
                + " e.emp_id=d.emp_id and"
                + " e.emp_id=dl.emp_id and"
                + " d.pltipdoc=dl.pltipdoc and"
                + " d.plnrodoc = dl.plnrodoc and"
                + " t.plnrodoc=d.plnrodoc and"
                + " t.plestado = 1 and"
                + " e.emp_est=1 and"
                + " d.pldc_estado = 1 and"
                + " dl.plestado_dl = 1"
                + " and d.emp_id =" + objUsuCredential.getCodemp()
                + s_sucursal
                + s_inicio
                + s_final
                + s_periodo
                + s_costo
                + " group by t.pltipdoc,t.plnrodoc, d.pldc_idconc,d.pldc_glosa,d.nro_rec_ie,d.nro_rec_ref,"
                + " t.plapepat,t.plapemat,t.plnomemp, d.emp_id,d.suc_id,d.pldc_fecmov,dl.plarea,e.emp_des"
                + " order by nombres";

        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        P_WHERE = query;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objDescuentos = new InformesDescuentos();
                objDescuentos.setCodigo(rs.getString("codigo"));
                objDescuentos.setNombres(rs.getString("nombres"));
                objDescuentos.setId_concepto(rs.getString("pldc_idconc"));
                objDescuentos.setDes_concepto(rs.getString("tabla_descri"));
                objDescuentos.setGlosa(rs.getString("pldc_glosa"));
                objDescuentos.setRegreso(rs.getString("nro_rec_ie"));
                objDescuentos.setReferencia(rs.getString("nro_rec_ref"));
                objDescuentos.setFecha_mov(rs.getDate("pldc_fecmov"));
                objDescuentos.setAbono(rs.getDouble("abono"));
                objDescuentos.setCargo(rs.getDouble("cargo"));
                objDescuentos.setAp_paterno(rs.getString("plapepat"));
                objDescuentos.setArea(rs.getString("area"));
                objDescuentos.setEmpresa(rs.getString("emp_des"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

}
