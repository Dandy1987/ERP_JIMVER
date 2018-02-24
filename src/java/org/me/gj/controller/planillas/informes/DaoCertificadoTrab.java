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
import org.me.gj.model.planillas.informes.InformesCertificadoTrab;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCertificadoTrab {

    //Variables de Conexion
    Connection con = null;
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //
    Personal objPersonal;
    ListModelList<Personal> objlstPersonal;
    //InformesCertificadoTrab objPlaCerTra;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //*****LISTA DEL CERTIFICADO QUE SE CREARA
    public InformesCertificadoTrab listaCertificadoTrab(String idpersonal) throws SQLException {

        InformesCertificadoTrab objPlaCerTra = null;

        String sql_certra = "select\n"
                + "CASE\n"
                + "  WHEN l.emp_id = '1' THEN 'COMERCIALIZADORA Y DISTRIBUIDORA JIMENEZ S.A.C.'\n"
                + "  WHEN l.emp_id = '9' THEN 'DISTRIBUIDORA DE COSMETICOS Y BELLEZA S.A.C.'\n"
                + "  ELSE pack_tpersonal.femp_descripcion(l.emp_id)\n"
                + "END emp,"
                + "--pack_tpersonal.femp_descripcion(l.emp_id) emp,\n"
                + "pack_tpersonal.femp_ruc(l.emp_id) ruc,\n"
                + "CASE\n"
                + "  WHEN l.emp_id = '1' and l.suc_id = '6' THEN 'Piura'\n"
                + "  ELSE pack_tpersonal.fsuc_descripcion(l.emp_id, l.suc_id)\n"
                + "END suc,\n"
                + "to_char(decode(to_char(l.plfecces, 'd'),7, l.plfecces+2, l.plfecces+1), 'dd \"de\" ')||\n"
                + "trim(to_char(decode(to_char(l.plfecces, 'd'),7, l.plfecces+2, l.plfecces+1), 'Month'))||\n"
                + "to_char(decode(to_char(l.plfecces, 'd'),7, l.plfecces+2, l.plfecces+1), ' \"del\" yyyy') feccert,"
                + "p.plapepat||' '||p.plapemat||' '||p.plnomemp empleado,\n"
                + "decode(l.pltipdoc,1,'DNI',2,'CARNET DE EXTRANJERIA',3,'RUC',4,'PASAPORTE',5,'PARTIDA DE NACIMIENTO',6,'OTROS') tipdoc,\n"
                + "l.plnrodoc ndoc,\n"
                + "to_char(l.plfecing, 'dd/MM/yyyy') fing, to_char(l.plfecces, 'dd/MM/yyyy') fces,\n"
                + "pack_tpersonal.ftb1_descripcion(l.plidcargo,'00006') cargo\n"
                + "from tpersonal p ,tpldatoslab l\n"
                + "where\n"
                + "p.pltipdoc=l.pltipdoc and\n"
                + "p.plnrodoc = l.plnrodoc and\n"
                + "p.pltipdoc||''||p.plnrodoc = '" + idpersonal + "' and\n"
                + "p.plestado = 1 and\n"
                + "l.plestado_dl=1 and\n"
                + "l.plfecces is not null";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_certra);
            while (rs.next()) {
                objPlaCerTra = new InformesCertificadoTrab();
                objPlaCerTra.setEmpnom(rs.getString("emp"));
                objPlaCerTra.setEmpruc(rs.getString("ruc"));
                objPlaCerTra.setEmpsuc(rs.getString("suc"));
                objPlaCerTra.setFeccert(rs.getString("feccert"));
                objPlaCerTra.setEmpleado(rs.getString("empleado"));
                objPlaCerTra.setTipdoc(rs.getString("tipdoc"));
                objPlaCerTra.setNrodoc(rs.getString("ndoc"));
                objPlaCerTra.setFecini(rs.getString("fing"));
                objPlaCerTra.setFecfin(rs.getString("fces"));
                objPlaCerTra.setPercargo(rs.getString("cargo"));
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
        return objPlaCerTra;
    }

    //*****VERIFICA CODIGO EMPLEADO
    public Personal getLovPersonal(String id) throws SQLException {

        String sql_personal = "select t.pltipdoc||t.plnrodoc id_per,"
                + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                + " t.pltipdoc,t.plnrodoc,d.suc_id,d.emp_id,d.plfecing"
                + " from tpersonal t,tpldatoslab d"
                + " where t.pltipdoc = d.pltipdoc and"
                + " t.plnrodoc = d.plnrodoc and"
                + " t.plestado = 1 and"
                + " d.plestado_dl = 1 and"
                + " d.emp_id =" + objUsuCredential.getCodemp() + " and"
                + " d.plfecces is not null" //d.suc_id =" + objUsuCredential.getCodemp() + "
                + " and t.pltipdoc||''||t.plnrodoc like '%" + id + "%'"
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
    public ListModelList<Personal> busquedaLovPersonal(int empresa,int sucursal,String area,String estado,String cese,String consulta) throws SQLException {
		
String sql_personal = "{call codijisa.pack_tpersonal.p_listPersonalLov(?,?,?,?,?,?,?)}";
       /* String sql_personal = "select t.pltipdoc||t.plnrodoc id_per,"
                + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                + " t.pltipdoc,t.plnrodoc,s.suc_des"
                + " from tpersonal t,tpldatoslab d,tsucursales s"
                + " where t.pltipdoc = d.pltipdoc and"
                + " t.plnrodoc = d.plnrodoc and"
                + " d.suc_id=s.suc_id and"
                + " d.emp_id=s.emp_id and"
                + " t.plestado = 1 and"
                + " d.plestado_dl = 1 and"
                + " d.emp_id =" + objUsuCredential.getCodemp() + "and"
                + " d.plfecces is not null"
                + " order by s.suc_id ,t.plapepat, t.plapemat, t.plnomemp";*/
				
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_personal);
            cst.setInt(1, empresa);
            cst.setInt(2, sucursal);
            cst.setString(3, area);
            cst.setString(4, estado);
            cst.setString(5, cese);
            cst.setString(6, consulta);
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(7);
            objlstPersonal = null;  
            objlstPersonal = new ListModelList<Personal>();
            
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
                //st.close();
                rs.close();
                con.close();
            }

        }

        return objlstPersonal;

    }
	
    public ListModelList<Personal> busquedaLovPersonal2(String consulta) throws SQLException {

        String sql_personal = " select t.pltipdoc||t.plnrodoc id_per,"
                            + " t.plapepat || ' ' || t.plapemat || ' ' || t.plnomemp des_per,"
                            + " t.pltipdoc,t.plnrodoc,s.suc_des"
                            + " from tpersonal t,tpldatoslab d, tsucursales s"
                            + " where t.pltipdoc = d.pltipdoc and"
                            + " t.plnrodoc = d.plnrodoc and "
                            + " d.suc_id = s.suc_id and "
                            + " d.emp_id = s.emp_id and"
                            + " t.plestado = 1 and "
                            + " d.plestado_dl = 1 and "
                            + " d.emp_id =" + objUsuCredential.getCodemp() + " and"
                            + " d.plfecces is not null"
                            + " and (t.plapepat||' '||t.plapemat||' '||t.plnomemp like '%" + consulta + "%' or t.plnrodoc like '%" + consulta + "%')"
                            + " order by s.suc_id ,t.plapepat, t.plapemat, t.plnomemp";

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
