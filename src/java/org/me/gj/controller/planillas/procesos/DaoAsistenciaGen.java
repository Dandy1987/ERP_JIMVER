package org.me.gj.controller.planillas.procesos;

import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.procesos.AsistenciaGen;
import org.zkoss.zul.ListModelList;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoAsistenciaGen {

    //Instancias de Objetos
    ListModelList<AsistenciaGen> objlstAsistenciaGen;
    ListModelList<AsistenciaGen> objlstAsistenciaGenDet;
    AsistenciaGen objAsistenciaGen;

    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;

    ListModelList<Sucursales> objlstSucursal;
    Sucursales objSucursal;
    ListModelList<Sucursales> objlstSucursales;

    ArrayDescriptor arrayDescriptor;
    ARRAY arr1, arr2;

    //Variables publicas
    Connection con = null;
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCliente.class);

    ///Eventos Primarios - Procedimientos insertar,modificar,eliminar
    public ParametrosSalida modificarAsistencia(Object[][] listasistencia, Object[][] listaLov) throws SQLException {

	String sql_modificar_asistencia = "{call pack_tplasiste.p_modificarAsistencia(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_modificar_asistencia);
            arrayDescriptor = ArrayDescriptor.createDescriptor("LISTASISTENCIA", con.getMetaData().getConnection());
            arr1 = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listasistencia);
            arrayDescriptor = ArrayDescriptor.createDescriptor("LISTASUBSIDIO", con.getMetaData().getConnection());
            arr2 = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listaLov);
            cst.clearParameters();
            cst.setArray(1, arr1);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.setArray(4, arr2);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas
    public ListModelList<AsistenciaGen> listaAsistencia(int n_empid, int n_sucid, String periodo) throws SQLException {

        objlstAsistenciaGen = null;

        String sql_asistencia;

        sql_asistencia = "{call codijisa.pack_tplasiste.p_listasistencia(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_asistencia);//se cambio cst x csts
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, n_sucid); //cst.setInt(2, objUsuCredential.getCodsuc());
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.setString(4, periodo);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(3);
            st = con.createStatement();

            objAsistenciaGen = null;
            objlstAsistenciaGen = new ListModelList<AsistenciaGen>();

            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                objAsistenciaGen.setId_per(rs.getString("id_per"));
                objAsistenciaGen.setDes_per(rs.getString("des_per"));
                objAsistenciaGen.setId_area(rs.getString("cod_area"));
                objAsistenciaGen.setDes_area(rs.getString("des_area"));
                objAsistenciaGen.setPer_cargo(rs.getString("car_per"));
                objAsistenciaGen.setPlcodhor(rs.getString("codhor"));
                objAsistenciaGen.setDeshor(rs.getString("deshor"));
                objAsistenciaGen.setPer_fecing(rs.getDate("fecing_per"));
                objAsistenciaGen.setPer_fecces(rs.getDate("fecces_per"));
                objAsistenciaGen.setMes(rs.getString("mes"));
                objAsistenciaGen.setPlas_fecadd(rs.getTimestamp("plas_fecadd"));
                objAsistenciaGen.setPlas_usuadd(rs.getString("plas_usuadd"));
                objAsistenciaGen.setPlas_usumod(rs.getString("plas_usumod"));
                objAsistenciaGen.setPlas_fecmod(rs.getTimestamp("plas_fecmod"));
                objAsistenciaGen.setPeriodo(rs.getString("periodo"));
                objAsistenciaGen.setPldia01(rs.getString("pldia01"));
                objAsistenciaGen.setPldia02(rs.getString("pldia02"));
                objAsistenciaGen.setPldia03(rs.getString("pldia03"));
                objAsistenciaGen.setPldia04(rs.getString("pldia04"));
                objAsistenciaGen.setPldia05(rs.getString("pldia05"));
                objAsistenciaGen.setPldia06(rs.getString("pldia06"));
                objAsistenciaGen.setPldia07(rs.getString("pldia07"));
                objAsistenciaGen.setPldia08(rs.getString("pldia08"));
                objAsistenciaGen.setPldia09(rs.getString("pldia09"));
                objAsistenciaGen.setPldia10(rs.getString("pldia10"));
                objAsistenciaGen.setPldia11(rs.getString("pldia11"));
                objAsistenciaGen.setPldia12(rs.getString("pldia12"));
                objAsistenciaGen.setPldia13(rs.getString("pldia13"));
                objAsistenciaGen.setPldia14(rs.getString("pldia14"));
                objAsistenciaGen.setPldia15(rs.getString("pldia15"));
                objAsistenciaGen.setPldia16(rs.getString("pldia16"));
                objAsistenciaGen.setPldia17(rs.getString("pldia17"));
                objAsistenciaGen.setPldia18(rs.getString("pldia18"));
                objAsistenciaGen.setPldia19(rs.getString("pldia19"));
                objAsistenciaGen.setPldia20(rs.getString("pldia20"));
                objAsistenciaGen.setPldia21(rs.getString("pldia21"));
                objAsistenciaGen.setPldia22(rs.getString("pldia22"));
                objAsistenciaGen.setPldia23(rs.getString("pldia23"));
                objAsistenciaGen.setPldia24(rs.getString("pldia24"));
                objAsistenciaGen.setPldia25(rs.getString("pldia25"));
                objAsistenciaGen.setPldia26(rs.getString("pldia26"));
                objAsistenciaGen.setPldia27(rs.getString("pldia27"));
                objAsistenciaGen.setPldia28(rs.getString("pldia28"));
                objAsistenciaGen.setPldia29(rs.getString("pldia29"));
                objAsistenciaGen.setPldia30(rs.getString("pldia30"));
                objAsistenciaGen.setPldia31(rs.getString("pldia31"));
                //agregado 3108217
                objAsistenciaGen.setPltipdoc(rs.getInt("pltipdoc"));
                objAsistenciaGen.setPlnrodoc(rs.getString("plnrodoc"));
                objAsistenciaGen.setSuc_id(rs.getInt("suc_id"));
                objlstAsistenciaGen.add(objAsistenciaGen);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objlstAsistenciaGen;

    }

    public String modificaEstadoSubsidio(int i_emp, String s_periodo, String s_nrodoc, int i_subsidio) throws SQLException {
        String s_msg = "";
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tplasiste.p_modificar_estado_subsidio(?,?,?, ?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, i_emp);
            cst.setString(2, s_periodo);
            cst.setString(3, s_nrodoc);
            cst.setInt(4, i_subsidio);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);

            cst.execute();
            s_msg = cst.getString(5);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return s_msg;
    }

public ListModelList<AsistenciaGen> listaRegistro(int n_empid, int n_sucid, String s_area, String s_idper, Date d_fecini, Date d_fecfin) throws SQLException {

      objlstAsistenciaGen = null;
	  
      String sql_asistencia = "{call codijisa.pack_tplasiste.p_listaMarcadoPantalla(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_asistencia);
            cst.setInt(1, objUsuCredential.getCodemp());
            //cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(2, n_sucid);
            cst.setString(3, s_area);
            cst.setString(4, s_idper);
                        cst.setDate(5, convertJavaDateToSqlDate(d_fecini));
            cst.setDate(6, convertJavaDateToSqlDate(d_fecfin));
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(7);
            st = con.createStatement();

            objAsistenciaGen = null;
            objlstAsistenciaGen = new ListModelList<AsistenciaGen>();

            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();

                objAsistenciaGen.setId_per(rs.getString("CODIGO"));

                objAsistenciaGen.setDes_per(rs.getString("NOMBRES"));

                objAsistenciaGen.setPlcodhor(rs.getString("horario_id"));
                objAsistenciaGen.setDes_area(rs.getString("area"));
                objAsistenciaGen.setDeshor(rs.getString("HORARIO")); 
                objlstAsistenciaGen.add(objAsistenciaGen);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstAsistenciaGen;

    }

    public AsistenciaGen buscaEmpleadoAsistencia(int n_empid, int n_sucid, String periodo, String txt, int rb) throws SQLException {

        objAsistenciaGen = null;

        String tsuc_id = "";

        String fn_sucid = n_sucid == 0 ? tsuc_id = " like '%%'" : (tsuc_id = " = " + n_sucid);

        String fperiodo = periodo.isEmpty() ? periodo = " like '%%'" : " = '" + periodo + "'";

        String filtro = rb == 0 ? "and p.pltipdoc || p.plnrodoc like '" + txt + "'" : "and p.plapepat || ' ' || p.plapemat like '" + txt + "%'";

        String sql_asistencia = " select  p.pltipdoc || '' || p.plnrodoc id_per,"
                + "         p.plapepat || ' ' || p.plapemat || ' ' || p.plnomemp des_per,"
                + "         t2.tabla_id cod_area,t2.tabla_descri des_area,t.tabla_descri car_per,a.plcodhor codhor,h.hor_descrip deshor,a.periodo periodo,l.plfecing fecing_per,l.plfecces fecces_per,"
                + "         decode(substr(a.periodo,5,2),'01','ENERO','02','FEBRERO','03','MARZO','04','ABRIL','05','MAYO','06','JUNIO','07','JULIO','08','AGOSTO','09','SETIEMBRE','10','OCTUBRE','11','NOVIEMBRE','12','DICIEMBRE')||' '|| substr(a.periodo,1,4) mes,"
                + "         a.plas_usuadd,a.plas_fecadd,a.plas_usumod,a.plas_fecmod,p.pltipdoc,p.plnrodoc,a.pldia01,a.pldia02,a.pldia03,a.pldia04,a.pldia05,a.pldia06,a.pldia07,a.pldia08,a.pldia09,a.pldia10,a.pldia11,a.pldia12,a.pldia13,a.pldia14,a.pldia15,"
                + "         a.pldia16,a.pldia17,a.pldia18,a.pldia19,a.pldia20,a.pldia21,a.pldia22,a.pldia23,a.pldia24 ,a.pldia25,a.pldia26,a.pldia27,a.pldia28,a.pldia29,a.pldia30,a.pldia31,a.emp_id,a.suc_id,l.plfecing"
                + " from    tpersonal p, tpldatoslab l, Tpltablas1 t,tplasiste a,tas_horario h,tpltablas1 t2"
                + " where   p.pltipdoc = l.pltipdoc"
                + "         and p.plnrodoc = l.plnrodoc"
                + "         and t.tabla_id = l.plidcargo"
                + "         and t2.tabla_id = l.plarea"
                + "         and p.pltipdoc = a.pltipdoc"
                + "         and p.plnrodoc = a.plnrodoc"
                + "         and l.emp_id = a.emp_id"
                + "         and l.suc_id = a.suc_id"
                + "         and a.emp_id = h.emp_id"
                + "         and a.suc_id = h.suc_id"
                + "         and a.plcodhor = h.horario_id"
                + "         and t.tabla_cod = '00006'"
                + "         and t2.tabla_cod = '00003'"
                + "         and t.tabla_id <> '000'"
                + "         and l.emp_id = '" + n_empid + "'" //PARAMETRO DE CODIGO DE EMPRESA
                + "         and l.suc_id " + fn_sucid //PARAMETRO DE CODIGO DE SUCURSAL
                + "         and a.periodo " + fperiodo //PARAMETRO DE PERIODO
                + filtro
                + "         and p.plestado <> 0\n"
                + "         and l.plestado_dl = 1\n"
                + " group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,t.tabla_descri,l.plfecing,l.plfecing,l.plfecces,a.plcodhor,h.hor_descrip,a.periodo,t2.tabla_id,t2.tabla_descri,a.plas_usuadd,a.plas_fecadd,a.plas_usumod,\n"
                + "         a.plas_fecmod,a.pldia01,a.pldia02,a.pldia03,a.pldia04,a.pldia05,a.pldia06,a.pldia07,a.pldia08,a.pldia09,a.pldia10,a.pldia11,a.pldia12,a.pldia13,a.pldia14,a.pldia15,a.pldia16,a.pldia17,a.pldia18,a.pldia19,\n"
                + "         a.pldia20,a.pldia21,a.pldia22,a.pldia23,a.pldia24 ,a.pldia25,a.pldia26,a.pldia27,a.pldia28,a.pldia29,a.pldia30,a.pldia31,a.emp_id,a.suc_id\n"
                + " order by p.plnrodoc";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();//se cambio cst x csts
            rs = st.executeQuery(sql_asistencia);

            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                objAsistenciaGen.setId_per(rs.getString("id_per"));
                objAsistenciaGen.setDes_per(rs.getString("des_per"));
                break;
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
        return objAsistenciaGen;
    }

    //POR IMPLEMENTAR
    public ListModelList<AsistenciaGen> consultarAsistenciasGen(String sucursal, String periodo, String area, String codigo) throws SQLException {

        String sql_consultar = "{call codijisa.pack_tplasiste.p_consultarAsistencia(?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_consultar);//se cambio cst CallableStatement
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, sucursal);
            cst.setString(3, periodo);
            cst.setString(4, area);
            cst.setString(5, codigo);
            cst.registerOutParameter(6, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(6);

            objlstAsistenciaGen = null;
            objlstAsistenciaGen = new ListModelList<AsistenciaGen>();

            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                objAsistenciaGen.setId_per(rs.getString("id_per"));
                objAsistenciaGen.setDes_per(rs.getString("des_per"));
                objAsistenciaGen.setId_area(rs.getString("cod_area"));
                objAsistenciaGen.setDes_area(rs.getString("des_area"));
                objAsistenciaGen.setPer_cargo(rs.getString("car_per"));
                objAsistenciaGen.setPlcodhor(rs.getString("codhor"));
                objAsistenciaGen.setDeshor(rs.getString("deshor"));
                objAsistenciaGen.setPer_fecing(rs.getDate("fecing_per"));
                objAsistenciaGen.setPer_fecces(rs.getDate("fecces_per"));
                objAsistenciaGen.setMes(rs.getString("mes"));
                objAsistenciaGen.setPlas_fecadd(rs.getTimestamp("plas_fecadd"));
                objAsistenciaGen.setPlas_usuadd(rs.getString("plas_usuadd"));
                objAsistenciaGen.setPlas_usumod(rs.getString("plas_usumod"));
                objAsistenciaGen.setPlas_fecmod(rs.getTimestamp("plas_fecmod"));
                objAsistenciaGen.setPeriodo(rs.getString("periodo"));
                objAsistenciaGen.setPldia01(rs.getString("pldia01"));
                objAsistenciaGen.setPldia02(rs.getString("pldia02"));
                objAsistenciaGen.setPldia03(rs.getString("pldia03"));
                objAsistenciaGen.setPldia04(rs.getString("pldia04"));
                objAsistenciaGen.setPldia05(rs.getString("pldia05"));
                objAsistenciaGen.setPldia06(rs.getString("pldia06"));
                objAsistenciaGen.setPldia07(rs.getString("pldia07"));
                objAsistenciaGen.setPldia08(rs.getString("pldia08"));
                objAsistenciaGen.setPldia09(rs.getString("pldia09"));
                objAsistenciaGen.setPldia10(rs.getString("pldia10"));
                objAsistenciaGen.setPldia11(rs.getString("pldia11"));
                objAsistenciaGen.setPldia12(rs.getString("pldia12"));
                objAsistenciaGen.setPldia13(rs.getString("pldia13"));
                objAsistenciaGen.setPldia14(rs.getString("pldia14"));
                objAsistenciaGen.setPldia15(rs.getString("pldia15"));
                objAsistenciaGen.setPldia16(rs.getString("pldia16"));
                objAsistenciaGen.setPldia17(rs.getString("pldia17"));
                objAsistenciaGen.setPldia18(rs.getString("pldia18"));
                objAsistenciaGen.setPldia19(rs.getString("pldia19"));
                objAsistenciaGen.setPldia20(rs.getString("pldia20"));
                objAsistenciaGen.setPldia21(rs.getString("pldia21"));
                objAsistenciaGen.setPldia22(rs.getString("pldia22"));
                objAsistenciaGen.setPldia23(rs.getString("pldia23"));
                objAsistenciaGen.setPldia24(rs.getString("pldia24"));
                objAsistenciaGen.setPldia25(rs.getString("pldia25"));
                objAsistenciaGen.setPldia26(rs.getString("pldia26"));
                objAsistenciaGen.setPldia27(rs.getString("pldia27"));
                objAsistenciaGen.setPldia28(rs.getString("pldia28"));
                objAsistenciaGen.setPldia29(rs.getString("pldia29"));
                objAsistenciaGen.setPldia30(rs.getString("pldia30"));
                objAsistenciaGen.setPldia31(rs.getString("pldia31"));
                //agregado 31082017
                objAsistenciaGen.setPltipdoc(rs.getInt("pltipdoc"));
                objAsistenciaGen.setPlnrodoc(rs.getString("plnrodoc"));
                objAsistenciaGen.setSuc_id(rs.getInt("suc_id"));
                objlstAsistenciaGen.add(objAsistenciaGen);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objlstAsistenciaGen;

    }

    public ListModelList<AsistenciaGen> listaDetalle(int emp_id, int suc_id, String id_per, String periodo) throws SQLException {

        String sql_detalle = "{call codijisa.pack_tplasiste.p_listasistenciaDetalle(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_detalle);
            cst.setInt(1, objUsuCredential.getCodemp());
            //cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(2, suc_id);
            cst.setString(3, id_per);
            cst.setString(4, periodo);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(5);
            st = con.createStatement();

            objAsistenciaGen = null;
            objlstAsistenciaGenDet = new ListModelList<AsistenciaGen>();

            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                objAsistenciaGen.setId_per(rs.getString("idper"));
                objAsistenciaGen.setPeriodo(rs.getString("periodo"));
                objAsistenciaGen.setAsistencia(rs.getString("asistencia"));
                objAsistenciaGen.setFec_asis(rs.getDate("fecha"));
                objAsistenciaGen.setPltipdoc(rs.getInt("pltipdoc"));
                objAsistenciaGen.setPlnrodoc(rs.getString("plnrodoc"));
                objlstAsistenciaGenDet.add(objAsistenciaGen);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objlstAsistenciaGenDet;

    }

    //LOV PERSONAL
    public ListModelList<Personal> busquedaLovPersonal() throws SQLException {

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + " pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per, "
                + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t, tempresas e "
                + "                where p.pltipdoc = l.pltipdoc "
                + "                and   p.plnrodoc = l.plnrodoc "
                + "                and   t.tabla_id = l.plidcargo "
                + "                and l.emp_id = e.emp_id "
                + "                and   t.tabla_cod = '00006' "
                + "                and   t.tabla_id <> '000' "
                + "                and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + "                and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + "                and p.plestado <> 0"
                + "                and l.plestado_dl = 0"
                + "                and e.emp_est = 1"
                + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "                t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "               order by p.plnrodoc ";

        objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));
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

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + " pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per,"
                + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t,tempresas e "
                + "                where p.pltipdoc = l.pltipdoc "
                + "                and   p.plnrodoc = l.plnrodoc "
                + "                and   t.tabla_id = l.plidcargo "
                + "                and l.emp_id = e.emp_id "
                + "                and   t.tabla_cod = '00006' "
                + "                and   t.tabla_id <> '000' "
                + "                and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + "                and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + "                and p.plestado <> 0"
                + "                and l.plestado_dl = 0"
                + "                and e.emp_est = 1"
                + "                and   p.plapepat||' '||p.plapemat||' '||p.plnomemp like '%" + consulta + "%'"
                + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "                t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "               order by p.plnrodoc ";

        objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));
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

    public Personal getLovPersonal(String id) throws SQLException {

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + "       pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per, "
                + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t, tempresas e"
                + "                where p.pltipdoc = l.pltipdoc "
                + "                and   p.plnrodoc = l.plnrodoc "
                + "                and   t.tabla_id = l.plidcargo "
                + "                and l.emp_id = e.emp_id "
                + "                and   t.tabla_cod = '00006' "
                + "                and   t.tabla_id <> '000' "
                + "                and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + "                and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + "                and p.plestado <> 0"
                + "                and l.plestado_dl = 0"
                + "                and e.emp_est = 1"
                + "                and   p.pltipdoc||''||p.plnrodoc like '%" + id + "%'"
                + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "                t.tabla_descri,l.plfecing , e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "               order by p.plnrodoc ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            objPersonal = null;
            while (rs.next()) {
                objPersonal = new Personal();

                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));

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

    //lov subsidio
    public ListModelList<AsistenciaGen> buscarLov(String tipo) throws SQLException {
        String query = "select t.tabla_id,t.tabla_descri"
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00023'"
                + " and t.tabla_id <> '000'"
                + " and t.tabla_valor3 ='" + tipo + "'"
                + " order by t.tabla_id";

        objlstAsistenciaGen = new ListModelList<AsistenciaGen>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                objAsistenciaGen.setCod_subsidio(rs.getString("tabla_id"));
                objAsistenciaGen.setDes_subsidio(rs.getString("tabla_descri"));
                objlstAsistenciaGen.add(objAsistenciaGen);
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
        return objlstAsistenciaGen;

    }

    //Metodo para hallar el sexo
    public int getSexo(int tipo_doc, String nrodoc) throws SQLException {
        int sexo = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tplasiste.f_sexo(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, tipo_doc);
            cst.setString(3, nrodoc);
            cst.execute();
            sexo = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return sexo;

    }

    //para lsita de asistencia en bloque
    public ListModelList<AsistenciaGen> listaBloque(int emp_id, String suc_id, String periodo) throws SQLException, ParseException {

        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        String s_aux = "01-01-1900";
        
        String sql_detalle = "{call codijisa.pack_tplasiste.p_bloque_asis(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_detalle);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, suc_id);
            cst.setString(3, periodo);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(4);
            st = con.createStatement();

            objAsistenciaGen = null;
            objlstAsistenciaGenDet = new ListModelList<AsistenciaGen>();
            while (rs.next()) {
                objAsistenciaGen = new AsistenciaGen();
                //objAsistenciaGen.setId_per(rs.getString("idper"));
                objAsistenciaGen.setPeriodo(rs.getString("periodo"));
                objAsistenciaGen.setAsistencia(rs.getString("asistencia"));
                objAsistenciaGen.setFec_asis(rs.getDate("fecha"));
                //objAsistenciaGen.setPltipdoc(rs.getInt("pltipdoc"));
                //objAsistenciaGen.setPlnrodoc(rs.getString("plnrodoc"));
                if (!fmt.format(objAsistenciaGen.getFec_asis()).equals(s_aux)) {
                    objlstAsistenciaGenDet.add(objAsistenciaGen);
                }
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objlstAsistenciaGenDet;

    }

    public void reporteInfRegistroAsis(String codper, String fecini, String fecfin, int ob) {

        codper = "('" + codper + "')";

        String orderby = ob == 0 ? "order by to_date(fecha,'dd/mm/yyyy'), codigo, ingreso" : "order by codigo, to_date(fecha,'dd/mm/yyyy'), ingreso";

        String sql = "select distinct z.empresa, z.area, z.sucursal, z.fecha, z.codigo, z.nombres, z.dni, z.horario, z.ingreso, CASE WHEN z.salida is null THEN '--:--:--' ELSE z.salida END salida,\n"
                + "CASE\n"
                + "    WHEN z.ingreso is not null and z.salida is not null and z.horario_id != '005' /*HORARIO DEL DIA*/\n"
                + "            \n"
                + "    THEN\n"
                + "        CASE\n"
                + "            WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||z.hingref, 'dd/mm/yyyy hh24:mi:ss')\n"
                + "            THEN CASE\n"
                + "                     WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio <= 0\n"
                + "                     THEN '--'\n"
                + "                     WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio > 0 and\n"
                + "                          ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio < 1\n"
                + "                     THEN '00'\n"
                + "                     ELSE decode(length(trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)),1,\n"
                + "                                   '0'||trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio),\n"
                + "                                        trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)\n"
                + "                                )\n"
                + "                 END\n"
                + "                 ||':'||\n"
                + "                 CASE\n"
                + "                    WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio <= 0\n"
                + "                    THEN '--'\n"
                + "                    WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio > 0\n"
                + "                    THEN decode(length(trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                          trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))),1,\n"
                + "                         '0'||trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                         trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60)),\n"
                + "                               trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                         trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))\n"
                + "                               )\n"
                + "                 END\n"
                + "            ELSE\n"
                + "                 CASE\n"
                + "                     WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                     THEN '--'\n"
                + "                     WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 > 0 and\n"
                + "                          (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24< 1\n"
                + "                     THEN '00'\n"
                + "                     ELSE decode(length(trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)),1,\n"
                + "                                   '0'||trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24),\n"
                + "                                        trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)\n"
                + "                                )\n"
                + "                 END\n"
                + "                 ||':'||\n"
                + "                 CASE\n"
                + "                    WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                    THEN '--'\n"
                + "                    WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 > 0\n"
                + "                    THEN decode(length(trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                         trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))),1,\n"
                + "                         '0'||trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                         trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60)),\n"
                + "                               trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                         trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))\n"
                + "                               )\n"
                + "                 END\n"
                + "        END\n"
                + "               \n"
                + "    WHEN z.ingreso is not null and z.salida is not null and z.horario_id = '005' /*HORARIO DE AMANECIDA*/\n"
                + "    THEN \n"
                + "         CASE\n"
                + "             WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||'12:00:00', 'dd/mm/yyyy hh24:mi:ss') and\n"
                + "                  to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') <= to_date(z.fecha||' '||'23:59:59', 'dd/mm/yyyy hh24:mi:ss')\n"
                + "             THEN \n"
                + "                  CASE\n"
                + "                      WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                      THEN '--'\n"
                + "                      WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 > 0 and\n"
                + "                           (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 < 1\n"
                + "                      THEN '00'\n"
                + "                      ELSE '0'||trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)\n"
                + "                  END\n"
                + "                  ||':'||\n"
                + "                  CASE\n"
                + "                      WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                      THEN '--'\n"
                + "                      WHEN ((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                           trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60 >= 0\n"
                + "                      THEN decode(length(trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)),1,\n"
                + "                                    '0'||trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60),\n"
                + "                                         trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)\n"
                + "                                  )\n"
                + "                 END\n"
                + "             WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||z.hingref, 'dd/mm/yyyy hh24:mi:ss')\n"
                + "             THEN\n"
                + "                 CASE\n"
                + "                     WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||'00:00:00', 'dd/mm/yyyy hh24:mi:ss') and\n"
                + "                          to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') <= to_date(z.fecha||' '||'11:59:59', 'dd/mm/yyyy hh24:mi:ss')\n"
                + "                     THEN \n"
                + "                          CASE\n"
                + "                              WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio <= 0\n"
                + "                              THEN '--'\n"
                + "                              WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio > 0 and\n"
                + "                                   ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio < 1\n"
                + "                              THEN '00'\n"
                + "                              ELSE  decode(length(trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)),1,\n"
                + "                                             '0'||trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio),\n"
                + "                                                  trunc(((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)\n"
                + "                                           )\n"
                + "                          END\n"
                + "                          ||':'||\n"
                + "                          CASE\n"
                + "                              WHEN ((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio <= 0\n"
                + "                              THEN '--'\n"
                + "                              WHEN ((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                   trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60 >= 0\n"
                + "                              THEN decode(length(trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                                  trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)),1,\n"
                + "                                            '0'||trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                                  trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60),\n"
                + "                                                 trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                                  trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)\n"
                + "                                          )\n"
                + "                         END\n"
                + "                 END\n"
                + "             ELSE\n"
                + "                 CASE \n"
                + "                 WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||'00:00:00', 'dd/mm/yyyy hh24:mi:ss') and\n"
                + "                      to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') <= to_date(z.fecha||' '||'11:59:59', 'dd/mm/yyyy hh24:mi:ss')\n"
                + "                 THEN \n"
                + "                      CASE\n"
                + "                          WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                          THEN '--'\n"
                + "                          WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 > 0 and\n"
                + "                               (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 < 1\n"
                + "                          THEN '00'\n"
                + "                          ELSE  decode(length(trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)),1,\n"
                + "                                         '0'||trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24),\n"
                + "                                              trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)\n"
                + "                                       )\n"
                + "                      END\n"
                + "                      ||':'||\n"
                + "                      CASE\n"
                + "                          WHEN (to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24 <= 0\n"
                + "                          THEN '--'\n"
                + "                          WHEN ((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                               trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60 >= 0\n"
                + "                          THEN decode(length(trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                              trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)),1,\n"
                + "                                        '0'||trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                              trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60),\n"
                + "                                             trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                              trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)\n"
                + "                                      )\n"
                + "                          END\n"
                + "                     END\n"
                + "         END\n"
                + "    ELSE '--:--'\n"
                + "END htra,\n"
                + "\n"
                + "CASE\n"
                + "    WHEN z.ingreso is not null and z.salida is not null and z.horario_id != '005' /*HORARIO DEL DIA*/\n"
                + "    THEN\n"
                + "        CASE\n"
                + "            WHEN (((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio) < z.hatrabajar\n"
                + "            THEN '--'\n"
                + "            ELSE decode(length(trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar)),1,\n"
                + "                          '0'||trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar),\n"
                + "                               trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar)\n"
                + "                       )\n"
                + "        END\n"
                + "        ||':'||\n"
                + "        CASE\n"
                + "           WHEN (((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio) < z.hatrabajar\n"
                + "           THEN '--'\n"
                + "           ELSE decode(length(trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))),1,\n"
                + "                         '0'||trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60)),\n"
                + "                              trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                trunc((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24))*60))\n"
                + "                      )\n"
                + "        END\n"
                + "               \n"
                + "    WHEN z.ingreso is not null and z.salida is not null and z.horario_id = '005' /*HORARIO DE AMANECIDA*/\n"
                + "    THEN \n"
                + "         CASE                                                   \n"
                + "             WHEN to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') >= to_date(z.fecha||' '||'00:00:00', 'dd/mm/yyyy hh24:mi:ss') and\n"
                + "                  to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss') <= to_date(z.fecha||' '||'12:00:00', 'dd/mm/yyyy hh24:mi:ss')\n"
                + "             THEN \n"
                + "                  CASE\n"
                + "                      WHEN (((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio) < z.hatrabajar\n"
                + "                      THEN '--'\n"
                + "                      ELSE  decode(length(trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar)),1,\n"
                + "                                     '0'||trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar),\n"
                + "                                          trunc((((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio)-z.hatrabajar)\n"
                + "                                   )\n"
                + "                  END\n"
                + "                  ||':'||\n"
                + "                  CASE\n"
                + "                      WHEN (((to_date(z.fecha||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso, 'dd/mm/yyyy hh24:mi:ss'))*24)-z.hrefrigerio) < z.hatrabajar\n"
                + "                      THEN '--'\n"
                + "                      ELSE decode(length(trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)),1,\n"
                + "                                    '0'||trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60),\n"
                + "                                         trunc(((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24-\n"
                + "                                          trunc((to_date(z.fecha ||' '||z.salida, 'dd/mm/yyyy hh24:mi:ss')+1 - to_date(z.fecha||' '||z.ingreso,'dd/mm/yyyy hh24:mi:ss'))*24))*60)\n"
                + "                                  )\n"
                + "                  END\n"
                + "             ELSE '--:--'\n"
                + "         END\n"
                + "    ELSE '--:--'\n"
                + "END hext,\n"
                + "'ASISTIÓ' licencia\n"
                + "from (\n"
                + "select \n"
                + "    e.emp_sig empresa, s.suc_des sucursal,\n"
                + "    to_char(t.rg_fechmov,'dd/mm/yyyy') fecha,t.reg_tipdoc||t.reg_nrodoc codigo,\n"
                + "    p.plapepat||' '||p.plapemat||' '||p.plnomemp nombres, t.reg_nrodoc dni, t.horario_id, h.hor_descrip  horario,\n"
                + "    pack_tplasiste.f_fec_dia_sig(t.reg_emp_id, t.reg_suc_id, t.reg_tipdoc||t.reg_nrodoc, t.horario_id, to_char(t.rg_fechmov,'dd/mm/yyyy'), 1) ingreso,\n"
                + "    decode(t.horario_id,'005',pack_tplasiste.f_fec_dia_sig(t.reg_emp_id, t.reg_suc_id, t.reg_tipdoc||t.reg_nrodoc, t.horario_id, to_char(t.rg_fechmov+1,'dd/mm/yyyy'), 4),\n"
                + "                              pack_tplasiste.f_fec_dia_sig(t.reg_emp_id, t.reg_suc_id, t.reg_tipdoc||t.reg_nrodoc, t.horario_id, to_char(t.rg_fechmov,  'dd/mm/yyyy'), 4)) salida,\n"
                + "          \n"
                + "    h.hor_horatrab hatrabajar, h.hor_refrigerio hrefrigerio, h.hor_ingrefri||':00' hingref,\n"
                + "    v.asistencia licencia\n"
                + "from tas_regasis t , tempresas e , tsucursales s , tpersonal p , tas_horario h, tplasiste a, v_asistencia v\n"
                + "where\n"
                + "   t.reg_emp_id = e.emp_id\n"
                + "    \n"
                + "    and\n"
                + "     t.reg_emp_id = s.emp_id\n"
                + "    and t.reg_suc_id = s.suc_id\n"
                + "  \n"
                + "    and t.reg_tipdoc = p.pltipdoc\n"
                + "    and t.reg_nrodoc = p.plnrodoc\n"
                + "  \n"
                + "    \n"
                + "    and h.emp_id=t.reg_emp_id(+)\n"
                + "    and h.suc_id=t.reg_suc_id(+)\n"
                + "    and h.horario_id=t.horario_id(+)\n"
                + "\n"
                + "    and t.reg_emp_id = a.emp_id\n"
                + "    and t.reg_suc_id = a.suc_id\n"
                + "    and t.reg_tipdoc = a.pltipdoc\n"
                + "    and t.reg_nrodoc = a.plnrodoc\n"
                + "    and t.horario_id = a.plcodhor\n"
                + "    and to_char(t.rg_fechmov, 'yyyymm') = a.periodo\n"
                + "    and a.periodo = to_char(t.rg_fechmov, 'yyyymm')\n"
                + " \n"
                + "    and t.reg_emp_id = v.emp_id\n"
                + "    and t.reg_suc_id = v.suc_id\n"
                + "    and t.reg_tipdoc = v.pltipdoc\n"
                + "    and t.reg_nrodoc = v.plnrodoc\n"
                + "    and t.horario_id=v.plcodhor\n"
                + "    and to_char(t.rg_fechmov, 'yyyymm') = v.periodo\n"
                + "    and t.rg_fechmov = v.fecha\n"
                + "\n"
                + "    and t.reg_emp_id = '" + objUsuCredential.getCodemp() + "'\n"
                + "    and t.reg_tipdoc||t.reg_nrodoc in " + codper + "\n"
                + "    and p.plestado = 1\n"
                + "    and t.rg_fechmov between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy')\n"
                + "    and t.rg_flag in (1,4)"
                //                + "    /* Relacion t -> e */\n"
                //                + "    t.reg_emp_id = e.emp_id\n"
                //                + "    /* Relacion t -> s */\n"
                //                + "    and t.reg_emp_id = s.emp_id\n"
                //                + "    and t.reg_suc_id = s.suc_id\n"
                //                + "    /* Relacion t -> p */\n"
                //                + "    and t.reg_tipdoc = p.pltipdoc\n"
                //                + "    and t.reg_nrodoc = p.plnrodoc\n"
                //                + "    /* Relacion t -> h */\n"
                //                + "    and t.horario_id = h.horario_id(+)\n"
                //                + "    /* Relacion t -> a */\n"
                //                + "    and t.reg_emp_id = a.emp_id(+)\n"
                //                + "    and t.reg_suc_id = a.suc_id(+)\n"
                //                + "    and t.reg_tipdoc = a.pltipdoc(+)\n"
                //                + "    and t.reg_nrodoc = a.plnrodoc(+)\n"
                //                + "    and t.horario_id = a.plcodhor(+)\n"
                //                + "    and a.periodo = to_char(t.rg_fechmov, 'yyyymm')\n"
                //                + "    /* Relacion t -> v */\n"
                //                + "    and t.reg_emp_id = v.emp_id\n"
                //                + "    and t.reg_suc_id = v.suc_id\n"
                //                + "    and t.reg_tipdoc = v.pltipdoc\n"
                //                + "    and t.reg_nrodoc = v.plnrodoc\n"
                //                + "    and to_char(t.rg_fechmov, 'yyyymm') = v.periodo\n"
                //                + "    and t.rg_fechmov = v.fecha\n"
                //                + "    /* Filtros */\n"
                //                + "    and t.reg_emp_id = '" + objUsuCredential.getCodemp() + "'\n"
                //                + "    and t.reg_tipdoc||t.reg_nrodoc in " + codper + "\n"
                //                + "    and p.plestado = 1\n"
                //                + "    and t.rg_fechmov between to_date('" + fecini + "') and to_date('" + fecfin + "')\n"
                //                + "    and t.rg_flag in (1,4)\n"
                //                + "    and h.hor_ingreso != '00:00'\n"
                + "group by a.periodo,t.rg_fechmov,t.reg_tipdoc,t.reg_nrodoc,p.plapepat,p.plapemat,p.plnomemp,t.reg_nrodoc,h.hor_descrip,e.emp_sig,s.suc_des,v.asistencia,\n"
                + "h.hor_ingreso,h.hor_refrigerio,h.hor_horatrab,t.horario_id, t.reg_emp_id,t.reg_suc_id,h.hor_ingrefri\n"
                + orderby
                + ") z\n"
                + "where z.ingreso is not null\n"
                + "and z.licencia = '0'";

        P_WHERE = sql;

    }
	
	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            return new java.sql.Date(date.getTime());
        }
    }

    public String agregaPeriodoVacacionPago(  String s_nrodoc, String s_periodo, String s_periodopagado) throws SQLException {
        String s_msg = "";
        try {
            con = (new ConectaBD().conectar());
            String query = "{call pack_tplasiste.p_ins_per_vac_pag(?,?,?,?,?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, s_nrodoc);
            cst.setString(4, s_periodo);
            cst.setString(5, s_periodopagado);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.INTEGER);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return s_msg;
    }

    public String eliminaPeriodoVacacionPago( String s_nrodoc, String s_periodo, String s_valor) throws SQLException {
        String s_msg = "";
        try {
            con = (new ConectaBD().conectar());
            String query = "{call pack_tplasiste.p_delete_per_vac_pag(?,?,?,?,?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, s_nrodoc);
            cst.setString(4, s_periodo);

            cst.setString(5, objUsuCredential.getCuenta());
            cst.setString(6, s_valor);
            cst.registerOutParameter(7, java.sql.Types.INTEGER);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return s_msg;
    }

    public int getVac( String s_periodo, String s_nrodoc) throws SQLException {
        int vacaciones = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tplasiste.f_vacaciones(?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setInt(3, objUsuCredential.getCodsuc());
            cst.setString(4, s_periodo);
            cst.setString(5, s_nrodoc);
            cst.execute();
            vacaciones = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return vacaciones;

    }

    public ListModelList<String> listaNroPlames(String periodo) throws SQLException {

        ListModelList<String> objListDni = new ListModelList<String>();

        String sql_asistencia;

        sql_asistencia = "{call codijisa.pack_tplasiste.p_list_nrodoc(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_asistencia);//se cambio cst x csts
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc()); //cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, periodo);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(4);
            st = con.createStatement();

            while (rs.next()) {
                objListDni.add(rs.getString("documento"));

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objListDni;

    }
	
    public ListModelList<String> listaNroPlanilla(String periodo) throws SQLException {

        ListModelList<String> objListDni = new ListModelList<String>();

        String sql_asistencia;

        sql_asistencia = "{call codijisa.pack_tplasiste.p_list_nrodoc_planilla(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_asistencia);//se cambio cst x csts
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc()); //cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, periodo);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(4);
            st = con.createStatement();

            while (rs.next()) {
                objListDni.add(rs.getString("documento"));

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                cst.close();
                con.close();
            }
        }
        return objListDni;

    }
    
     public int validaPeriodoProceso(String s_periodo) throws SQLException {
        int i_valida = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tperpag.f_periodo_asistencia(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3,s_periodo );
            cst.execute();
            i_valida = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return i_valida;

    }
	
public int validaPeriodoCalculado(String s_periodo) throws SQLException {
        int i_valida = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tperpag.f_periodo_asistenciac(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3,s_periodo );
            cst.execute();
            i_valida = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return i_valida;
    }
	
     public int getVacxMes( String s_periodo, String s_nrodoc) throws SQLException {
        int vacaciones = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tplasiste.f_vacaciones_mes(?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setInt(3, objUsuCredential.getCodsuc());
            cst.setString(4, s_periodo);
            cst.setString(5, s_nrodoc);
            cst.execute();
            vacaciones = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return vacaciones;

    }
	
public int getDescansoMedico (String s_periodo, String s_nrodoc) throws SQLException {
        int descanso_medico = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tplasiste.f_num_descanso_med(?,?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setInt(3, objUsuCredential.getCodsuc());
            cst.setString(4, s_periodo);
            cst.setString(5, s_nrodoc);
            cst.execute();
            descanso_medico = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return descanso_medico;

    }
}