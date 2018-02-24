package org.me.gj.controller.planillas.informes;/*C*/

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OracleCallableStatement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesCartaBancos;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCartaBancos {

    //Variables de Conexion
    Connection con = null;
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //Variables
    Personal objPersonal;
    //InformesCartaBancos objPlaCarBan;
    ListModelList<Personal> objlstPersonal;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //*****LISTA DE LA CARTA QUE SE CREARA
    public InformesCartaBancos listaCartaBancos(String idpersonal) throws SQLException {

        InformesCartaBancos objPlaCarBan = null;
        
        String sql_certra = "select\n"
                + " CASE\n"
                + "  WHEN l.emp_id = '1' THEN 'COMERCIALIZADORA Y DISTRIBUIDORA JIMENEZ S.A.C.'\n"
                + "  WHEN l.emp_id = '9' THEN 'DISTRIBUIDORA DE COSMETICOS Y BELLEZA S.A.C.'\n"
                + "  ELSE pack_tpersonal.femp_descripcion(l.emp_id)\n"
                + " END emp,\n"
                + " pack_tpersonal.femp_ruc(l.emp_id) ruc,\n"
                + " CASE\n"
                + "  WHEN l.emp_id = '1' and l.suc_id = '7' THEN 'Piura'\n"
                + "  ELSE pack_tpersonal.fsuc_descripcion(l.emp_id, l.suc_id)\n"
                + " END suc,\n"
                + "to_char(decode(to_char(l.plfecces, 'd'),7, l.plfecces+2, l.plfecces+1), 'dd \"de\" ')|| trim(to_char(decode(to_char(l.plfecces, 'd'),6, l.plfecces+2, l.plfecces+1), 'Month'))||to_char(decode(to_char(l.plfecces, 'd'),6, l.plfecces+2, l.plfecces+1), ' \"del\" yyyy') feccar,\n"
                + " pack_tpersonal.fban_descripcion(dp.plbanco) perbanco,\n"
                + " p.plapepat||' '||p.plapemat||' '||p.plnomemp empleado,\n"
                + " decode(l.pltipdoc,1,'DNI',2,'CARNET DE EXTRANJERIA',3,'RUC',4,'PASAPORTE',5,'PARTIDA DE NACIMIENTO',6,'OTROS') tipdoc,\n"
                + " l.plnrodoc ndoc,\n"
                + " to_char(l.plfecces, 'dd \"de \"')||Trim(to_char(l.plfecces, 'Month'))||to_char(l.plfecces, '\" del\" yyyy') fces,\n"
                + " dp.plnrocta nban\n"
                + " from tpersonal p ,tpldatoslab l, tplctadep dp\n"
                + " where\n"
                + " p.pltipdoc=l.pltipdoc and\n"
                + " p.plnrodoc = l.plnrodoc and\n"
                + " p.pltipdoc = dp.pltipdoc and\n"
                + " p.plnrodoc = dp.plnrodoc and\n"
                + " l.pltipdoc = dp.pltipdoc and\n"
                + " l.plnrodoc = dp.plnrodoc and\n"
                + " dp.plestado_dep = '1' and\n"
                + " dp.pltipdep = '2' and\n"
                + " p.pltipdoc||''||p.plnrodoc = '" + idpersonal + "' and\n"
                + " p.plestado = 1 and\n"
                + " l.plestado_dl = 1 and\n"
                + " l.plfecces is not null";

        try {
            
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_certra);
            while (rs.next()) {
                objPlaCarBan = new InformesCartaBancos();
                objPlaCarBan.setEmpnom(rs.getString("emp"));
                objPlaCarBan.setEmpruc(rs.getString("ruc"));
                objPlaCarBan.setEmpsuc(rs.getString("suc"));
                objPlaCarBan.setFeccar(rs.getString("feccar"));
                objPlaCarBan.setPerbanco(rs.getString("perbanco"));
                objPlaCarBan.setEmpleado(rs.getString("empleado"));
                objPlaCarBan.setTipdoc(rs.getString("tipdoc"));
                objPlaCarBan.setNrodoc(rs.getString("ndoc"));
                objPlaCarBan.setFecces(rs.getString("fces"));
                objPlaCarBan.setNrocuenta(rs.getString("nban"));
            }
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objPlaCarBan;
    }

    //*****VERIFICA CODIGO EMPLEADO
    public Personal getLovPersonal(String id) throws SQLException {

        String sql_personal = " select t.pltipdoc||t.plnrodoc id_per,"
                            + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                            + " t.pltipdoc,t.plnrodoc,d.suc_id,d.emp_id,d.plfecing"
                            + " from tpersonal t,tpldatoslab d,tplctadep dp"
                            + " where"
                            + " t.pltipdoc = d.pltipdoc and"
                            + " t.plnrodoc = d.plnrodoc and"
                            + " t.plnrodoc = dp.plnrodoc and"
                            + " t.pltipdoc = dp.pltipdoc and"
                            + " d.pltipdoc = dp.pltipdoc and"
                            + " d.plnrodoc = dp.plnrodoc and"
                            + " d.emp_id = dp.emp_id and"
                            + " t.plestado = 1 and"
                            + " d.plestado_dl = 1 and"
                            + " d.emp_id = '" + objUsuCredential.getCodemp() + "' and"
                            + " dp.pltipdep = '2' and"
                            + " d.plfecces is not null and"
                            + " dp.plnrocta is not null and"
                            + " t.pltipdoc||''||t.plnrodoc like '%" + id + "%'"
                            + " order by t.plapepat";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            objPersonal = null;
            while (rs.next()) {
                objPersonal = new Personal();

                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                objPersonal.setEmp_id(rs.getInt("emp_id"));
                objPersonal.setSuc_id(rs.getInt("suc_id"));
                objPersonal.setFechaing(rs.getDate("plfecing"));
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
        return objPersonal;
    }

//*****METODOS DE BUSQUEDA PARA EL LOV
    public ListModelList<Personal> busquedaLovPersonal(int empresa,String estado, int cese,String consulta,int cuenta) throws SQLException {
		
   String sql_personal = "{call codijisa.p_lovPersonal.p_listPersonalLov(?,?,?,?,?,?)}";
      /*  String sql_personal = " select t.pltipdoc||t.plnrodoc id_per,"
                            + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                            + " t.pltipdoc,t.plnrodoc, s.suc_des"
                            + " from tpersonal t,tpldatoslab d, tplctadep dp, tsucursales s"
                            + " where"
                            + " t.pltipdoc = d.pltipdoc and"
                            + " t.plnrodoc = d.plnrodoc and"
                            + " d.suc_id = s.suc_id and"
                            + " d.emp_id = s.emp_id and"
                            + " s.emp_id = dp.emp_id and"
                            + " t.plnrodoc = dp.plnrodoc and"
                            + " t.pltipdoc = dp.pltipdoc and"
                            + " d.pltipdoc = dp.pltipdoc and"
                            + " d.plnrodoc = dp.plnrodoc and"
                            + " d.emp_id = dp.emp_id and"
                            + " t.plestado = 1 and"
                            + " d.plestado_dl = 1 and"
                            + " d.emp_id = '" + objUsuCredential.getCodemp() + "' and"
                            + " dp.plestado_dep = '1' and"
                            + " dp.pltipdep = '2' and"
                            + " d.plfecces is not null and"
                            + " dp.plnrocta is not null"
                            + " order by t.plapepat, t.plapemat, t.plnomemp";*/

       con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_personal);
            cst.setInt(1, empresa);
            cst.setString(2, estado);
            cst.setInt(3, cese);
            cst.setString(4, consulta);
            cst.setInt(5, cuenta);
            cst.registerOutParameter(6, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(6);
            objlstPersonal = null;  
            objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                objPersonal.setSuc_id_des(rs.getString("suc_des"));
                objlstPersonal.add(objPersonal);
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

        return objlstPersonal;

    }

    public ListModelList<Personal> busquedaLovPersonal2(String consulta) throws SQLException {

        String sql_personal = " select t.pltipdoc||t.plnrodoc id_per,"
                            + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                            + " t.pltipdoc,t.plnrodoc, s.suc_des"
                            + " from tpersonal t,tpldatoslab d, tplctadep dp, tsucursales s"
                            + " where"
                            + " t.pltipdoc = d.pltipdoc and"
                            + " t.plnrodoc = d.plnrodoc and"
                            + " d.suc_id = s.suc_id and"
                            + " d.emp_id = s.emp_id and"
                            + " s.emp_id = dp.emp_id and"
                            + " t.plnrodoc = dp.plnrodoc and"
                            + " t.pltipdoc = dp.pltipdoc and"
                            + " d.pltipdoc = dp.pltipdoc and"
                            + " d.plnrodoc = dp.plnrodoc and"
                            + " d.emp_id = dp.emp_id and"
                            + " t.plestado = 1 and"
                            + " d.plestado_dl = 1 and"
                            + " d.emp_id =" + objUsuCredential.getCodemp() + " and"
                            + " d.plfecces is not null and"
                            + " dp.plnrocta is not null and"
                            + " (t.plapepat||' '||t.plapemat||' '||t.plnomemp like '%" + consulta + "%' or t.plnrodoc like '%" + consulta + "%') and"
                            + " dp.plestado_dep = '1' and"
                            + " dp.pltipdep = '2'"
                            + " order by t.plapepat, t.plapemat, t.plnomemp";

        objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                objPersonal.setSuc_id_des(rs.getString("suc_des"));
                objlstPersonal.add(objPersonal);

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

        return objlstPersonal;

    }

}
