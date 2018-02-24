/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManHorarios;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoManHorarios {

    ListModelList<ManHorarios> objlstHorarios;
    ManHorarios objHorarios;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManHorarios.class);
    public static String sql_query;
    public static String P_WHERE = "";
    public static String P_WHERE2 = "";

    public DaoManHorarios() {
        objlstHorarios = new ListModelList<ManHorarios>();
    }

    public ListModelList<ManHorarios> listHorario(int empresa) throws SQLException {
        sql_query = "{call codijisa.pack_tashorario.p_consultar(?,?,?)}";
        objlstHorarios.clear();
        //P_WHERE2 = sql_query;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_query);
            cst.setInt(1, empresa);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            
            cst.execute();
            
            P_WHERE = cst.getString(3);
            
            rs = ((OracleCallableStatement) cst).getCursor(2);
            
            objlstHorarios = null;
            objlstHorarios = new ListModelList<ManHorarios>();
            while (rs.next()) {
                objHorarios = new ManHorarios();
                objHorarios.setSucursal(rs.getString("sucursal"));
                objHorarios.setHorario(rs.getString("horario"));
                objHorarios.setTipo(rs.getString("tipo"));
                objHorarios.setTipo_dia(rs.getString("tipo_dia"));
                objHorarios.setIngreso(rs.getString("hor_ingreso"));
                objHorarios.setSal_ref(rs.getString("hor_salrefri"));
                objHorarios.setIng_ref(rs.getString("hor_ingrefri"));
                objHorarios.setSalida(rs.getString("hor_salida"));
                //****************************
                objHorarios.setSucursal(rs.getString("suc_des"));
                objHorarios.setMan_codigo(rs.getString("horario_id"));
                objHorarios.setMan_descripcion(rs.getString("hor_descrip"));
                // objHorarios.setTipo(rs.getString(""));
                objHorarios.setMan_amanecida(rs.getString("hor_amanecida"));
                objHorarios.setMan_rango(rs.getInt("hor_ranaut"));
                objHorarios.setMan_hr_trabajadas(rs.getInt("hor_horatrab"));
                objHorarios.setMan_hr_refrigerio(rs.getInt("hor_refrigerio"));
                objHorarios.setMan_hr_tolerancia(rs.getInt("hor_minutole"));
                objHorarios.setMan_hr_semanal(rs.getString("hsm_id"));
                // objHorarios.setMan_de(rs.getString("hor_diaini"));
                objHorarios.setMan_de(rs.getString("ddiaini"));
                objHorarios.setMan_a(rs.getString("ddiafin"));
                objHorarios.setMan_descanso(rs.getString("ddiades"));
                objHorarios.setS_sucursal(rs.getString("suc_des"));
               // objHorarios.setMan_descanso(rs.getString("hor_diades"));
                objHorarios.setMan_iempresa(rs.getString("hor_marie"));
                objHorarios.setMan_srefrigerio(rs.getString("hor_marsr"));
                objHorarios.setMan_irefrigerio(rs.getString("hor_marir"));
                objHorarios.setMan_sempresa(rs.getString("hor_marse"));
                //dias
                objHorarios.setMan_lun1(rs.getString("hor_dia1ing"));
                objHorarios.setMan_lun2(rs.getString("hor_dia1salref"));
                objHorarios.setMan_lun3(rs.getString("hor_dia1ingref"));
                objHorarios.setMan_lun4(rs.getString("hor_dia1sal"));
                objHorarios.setMan_mar1(rs.getString("hor_dia2ing"));
                objHorarios.setMan_mar2(rs.getString("hor_dia2salref"));
                objHorarios.setMan_mar3(rs.getString("hor_dia3ingref"));
                objHorarios.setMan_mar4(rs.getString("hor_dia2sal"));
                objHorarios.setMan_mier1(rs.getString("hor_dia3ing"));
                objHorarios.setMan_mier2(rs.getString("hor_dia3salref"));
                objHorarios.setMan_mier3(rs.getString("hor_dia3ingref"));
                objHorarios.setMan_mier4(rs.getString("hor_dia3sal"));
                objHorarios.setMan_juev1(rs.getString("hor_dia4ing"));
                objHorarios.setMan_juev2(rs.getString("hor_dia4salref"));
                objHorarios.setMan_juev3(rs.getString("hor_dia4ingref"));
                objHorarios.setMan_juev4(rs.getString("hor_dia4sal"));
                objHorarios.setMan_vie1(rs.getString("hor_dia5ing"));
                objHorarios.setMan_vie2(rs.getString("hor_dia5salref"));
                objHorarios.setMan_vie3(rs.getString("hor_dia5ingref"));
                objHorarios.setMan_vie4(rs.getString("hor_dia5sal"));
                objHorarios.setMan_sab1(rs.getString("hor_dia6ing"));
                objHorarios.setMan_sab2(rs.getString("hor_dia6salref"));
                objHorarios.setMan_sab3(rs.getString("hor_dia6ingref"));
                objHorarios.setMan_sab4(rs.getString("hor_dia6sal"));
                objHorarios.setMan_dom1(rs.getString("hor_dia7ing"));
                objHorarios.setMan_dom2(rs.getString("hor_dia7salref"));
                objHorarios.setMan_dom3(rs.getString("hor_dia7ingref"));
                objHorarios.setMan_dom4(rs.getString("hor_dia7sal"));
                objHorarios.setMan_ant1(rs.getInt("hor_tantes1"));
                objHorarios.setMan_ant2(rs.getInt("hor_tantes2"));
                objHorarios.setMan_ant3(rs.getInt("hor_tantes3"));
                objHorarios.setMan_ant4(rs.getInt("hor_tantes4"));
                objHorarios.setMan_des1(rs.getInt("hor_tdespu1"));
                objHorarios.setMan_des2(rs.getInt("hor_tdespu2"));
                objHorarios.setMan_des3(rs.getInt("hor_tdespu3"));
                objHorarios.setMan_des4(rs.getInt("hor_tdespu4"));
                objHorarios.setUsu_add(rs.getString("hor_usuadd"));
                objHorarios.setUsu_mod(rs.getString("hor_usumod"));
                objHorarios.setDia_add(rs.getTimestamp("hor_fecadd"));
                objHorarios.setDia_mod(rs.getTimestamp("hor_fecmod"));
                objHorarios.setMan_sucursal(rs.getInt("suc_id"));
                objHorarios.setDia_inicio(rs.getString("hor_diaini"));
                objHorarios.setDia_descanso(rs.getString("hor_diades"));
                objHorarios.setHor_tiphora(rs.getString("hor_tiphora"));
                objlstHorarios.add(objHorarios);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstHorarios.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {

                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstHorarios;
    }

    //lov horarios
    public ListModelList<ManHorarios> listaLovHorario(int empresa) throws SQLException {
        String query_horarion = " select hsm_id,upper(rtrim(ltrim(codijisa.pack_tashorario.cdia(diaini)))) ddiaini,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diafin)))) ddiafin,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diades)))) ddiades,"
                + " diaini, diafin, diades"
                + " from("
                + " select s.hsm_id, max(decode(s.hsm_orden, 1, s.hsm_dia, null)) diaini,"
                + " max(decode(s.hsm_orden, 6, s.hsm_dia, null)) diafin,"
                + " max(decode(s.hsm_orden, 7, s.hsm_dia, null)) diades"
                + " from tplhorsem s"
                + " where s.emp_id = '" + empresa + "'"
                + " and s.hsm_orden in(1, 6, 7)"
                + " group by s.hsm_id"
                + " order by s.hsm_id)";

        objlstHorarios = new ListModelList<ManHorarios>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query_horarion);
            while (rs.next()) {
                objHorarios = new ManHorarios();
                objHorarios.setMan_codigo_semanal(rs.getString("hsm_id"));
                objHorarios.setMan_de(rs.getString("ddiaini"));
                objHorarios.setMan_a(rs.getString("ddiafin"));
                objHorarios.setMan_descanso(rs.getString("ddiades"));
                objlstHorarios.add(objHorarios);

            }
        } catch (Exception e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstHorarios;
    }

    public ListModelList<ManHorarios> buscaInicio(int empresa, String consulta) throws SQLException {
        String query = " select hsm_id,upper(rtrim(ltrim(codijisa.pack_tashorario.cdia(diaini)))) ddiaini,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diafin)))) ddiafin,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diades)))) ddiades,"
                + " diaini, diafin, diades"
                + " from("
                + " select s.hsm_id, max(decode(s.hsm_orden, 1, s.hsm_dia, null)) diaini,"
                + " max(decode(s.hsm_orden, 6, s.hsm_dia, null)) diafin,"
                + " max(decode(s.hsm_orden, 7, s.hsm_dia, null)) diades"
                + " from tplhorsem s"
                + " where s.emp_id =" + empresa + ""
                + "  and s.hsm_id ='" + consulta + "'"
                + " and s.hsm_orden in(1, 6, 7)"
                + " group by s.hsm_id"
                + " order by s.hsm_id)";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objlstHorarios = new ListModelList<ManHorarios>();
            while (rs.next()) {
                objHorarios = new ManHorarios();
                objHorarios.setMan_codigo_semanal(rs.getString("hsm_id"));
                objHorarios.setMan_de(rs.getString("ddiaini"));
                objHorarios.setMan_a(rs.getString("ddiafin"));
                objHorarios.setMan_descanso(rs.getString("ddiades"));
                objlstHorarios.add(objHorarios);
            }
        } catch (Exception e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstHorarios;

    }

    public ManHorarios getHora(int empresa, String consulta) throws SQLException {
        String query = " select hsm_id,upper(rtrim(ltrim(codijisa.pack_tashorario.cdia(diaini)))) ddiaini,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diafin)))) ddiafin,"
                + " upper(rtrim(ltrim(pack_tashorario.cdia(diades)))) ddiades,"
                + " diaini, diafin, diades"
                + " from("
                + " select s.hsm_id, max(decode(s.hsm_orden, 1, s.hsm_dia, null)) diaini,"
                + " max(decode(s.hsm_orden, 6, s.hsm_dia, null)) diafin,"
                + " max(decode(s.hsm_orden, 7, s.hsm_dia, null)) diades"
                + " from tplhorsem s"
                + " where s.emp_id =" + empresa + ""
                + "  and s.hsm_id ='" + consulta + "'"
                + " and s.hsm_orden in(1, 6, 7)"
                + " group by s.hsm_id"
                + " order by s.hsm_id)";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objHorarios = null;
            while (rs.next()) {
                objHorarios = new ManHorarios();
                objHorarios.setMan_codigo_semanal(rs.getString("hsm_id"));
                objHorarios.setMan_de(rs.getString("ddiaini"));
                objHorarios.setMan_a(rs.getString("ddiafin"));
                objHorarios.setMan_descanso(rs.getString("ddiades"));
                objHorarios.setDia_inicio(rs.getString("diaini"));
                objHorarios.setDia_descanso(rs.getString("diades"));
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
        return objHorarios;
    }

    public String insertar(ManHorarios objHorarios) throws SQLException {

        //String codigo = objHorarios.getMan_codigo();
        int sucursal = objHorarios.getMan_sucursal();
        String descripcion = objHorarios.getMan_descripcion();
        String tipo = objHorarios.getTipo(); //AQUI TOMA EL TIPO
        String amanecida = objHorarios.getMan_amanecida();
        int rango = objHorarios.getMan_rango();
        int hr_trabajo = objHorarios.getMan_hr_trabajadas();
        int hr_refrigerio = objHorarios.getMan_hr_refrigerio();
        int hr_tolerancia = objHorarios.getMan_hr_tolerancia();
        String hr_semanal = objHorarios.getMan_hr_semanal();
        String desde = objHorarios.getMan_de();
        String descanso = objHorarios.getMan_descanso();
        String chk_iempresa = objHorarios.getMan_iempresa();
        String chk_srefrigerio = objHorarios.getMan_srefrigerio();
        String chk_irefrigerio = objHorarios.getMan_irefrigerio();
        String chk_sempresa = objHorarios.getMan_sempresa();
        String lunes1 = objHorarios.getMan_lun1();
        String lunes2 = objHorarios.getMan_lun2();
        String lunes3 = objHorarios.getMan_lun3();
        String lunes4 = objHorarios.getMan_lun4();
        String martes1 = objHorarios.getMan_mar1();
        String martes2 = objHorarios.getMan_mar2();
        String martes3 = objHorarios.getMan_mar3();
        String martes4 = objHorarios.getMan_mar4();
        String miercoles1 = objHorarios.getMan_mier1();
        String miercoles2 = objHorarios.getMan_mier2();
        String miercoles3 = objHorarios.getMan_mier3();
        String miercoles4 = objHorarios.getMan_mier4();
        String jueves1 = objHorarios.getMan_juev1();
        String jueves2 = objHorarios.getMan_juev2();
        String jueves3 = objHorarios.getMan_juev3();
        String jueves4 = objHorarios.getMan_juev4();
        String viernes1 = objHorarios.getMan_vie1();
        String viernes2 = objHorarios.getMan_vie2();
        String viernes3 = objHorarios.getMan_vie3();
        String viernes4 = objHorarios.getMan_vie4();
        String sabado1 = objHorarios.getMan_sab1();
        String sabado2 = objHorarios.getMan_sab2();
        String sabado3 = objHorarios.getMan_sab3();
        String sabado4 = objHorarios.getMan_sab4();
        String domingo1 = objHorarios.getMan_dom1();
        String domingo2 = objHorarios.getMan_dom2();
        String domingo3 = objHorarios.getMan_dom3();
        String domingo4 = objHorarios.getMan_dom4();
        int antes1 = objHorarios.getMan_ant1();
        int antes2 = objHorarios.getMan_ant2();
        int antes3 = objHorarios.getMan_ant2();
        int antes4 = objHorarios.getMan_ant4();
        int despues1 = objHorarios.getMan_des1();
        int despues2 = objHorarios.getMan_des2();
        int despues3 = objHorarios.getMan_des3();
        int despues4 = objHorarios.getMan_des4();
        int marcado = objHorarios.getMarcado();
        String sql_insertar = "{call codijisa.pack_tashorario.p_insertar(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();

            cst.setInt(1, objUsuCredential.getCodemp()); 
            cst.setInt(2, sucursal);
            cst.setString(3, descripcion);
            cst.setString(4, tipo);
            cst.setString(5, amanecida);
            cst.setInt(6, rango);
            cst.setInt(7, hr_trabajo);
            cst.setInt(8, hr_refrigerio);
            cst.setInt(9, hr_tolerancia);
            cst.setString(10, hr_semanal);
            cst.setString(11, desde);
            cst.setString(12, descanso);
            cst.setString(13, chk_iempresa);
            cst.setString(14, chk_srefrigerio);
            cst.setString(15, chk_irefrigerio);
            cst.setString(16, chk_sempresa);
            cst.setString(17, lunes1);
            cst.setString(18, lunes2);
            cst.setString(19, lunes3);
            cst.setString(20, lunes4);
            cst.setString(21, martes1);
            cst.setString(22, martes2);
            cst.setString(23, martes3);
            cst.setString(24, martes4);
            cst.setString(25, miercoles1);
            cst.setString(26, miercoles2);
            cst.setString(27, miercoles3);
            cst.setString(28, miercoles4);
            cst.setString(29, jueves1);
            cst.setString(30, jueves2);
            cst.setString(31, jueves3);
            cst.setString(32, jueves4);
            cst.setString(33, viernes1);
            cst.setString(34, viernes2);
            cst.setString(35, viernes3);
            cst.setString(36, viernes4);
            cst.setString(37, sabado1);
            cst.setString(38, sabado2);
            cst.setString(39, sabado3);
            cst.setString(40, sabado4);
            cst.setString(41, domingo1);
            cst.setString(42, domingo2);
            cst.setString(43, domingo3);
            cst.setString(44, domingo4);
            cst.setInt(45, antes1);
            cst.setInt(46, antes2);
            cst.setInt(47, antes3);
            cst.setInt(48, antes4);
            cst.setInt(49, despues1);
            cst.setInt(50, despues2);
            cst.setInt(51, despues3);
            cst.setInt(52, despues4);
            cst.setString(53, objUsuCredential.getCuenta());
            cst.registerOutParameter(54, java.sql.Types.NUMERIC);
            cst.registerOutParameter(55, java.sql.Types.VARCHAR);
            cst.setInt(56, marcado);
            cst.execute();
            s_msg = cst.getString(55);
            i_flagErrorBD = cst.getInt(54);
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + descripcion);
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
        return s_msg;
    }

    public String actualizar(ManHorarios objHorarios) throws SQLException {

        /*String codigo = objHorarios.getMan_codigo();
        int sucursal = objHorarios.getMan_sucursal();
        String descripcion = objHorarios.getMan_descripcion();
        String tipo = objHorarios.getTipo();
        String amanecida = objHorarios.getMan_amanecida();
        int rango = objHorarios.getMan_rango();
        int hr_trabajo = objHorarios.getMan_hr_trabajadas();
        int hr_refrigerio = objHorarios.getMan_hr_refrigerio();
        int hr_tolerancia = objHorarios.getMan_hr_tolerancia();
        String hr_semanal = objHorarios.getMan_hr_semanal();
        String desde = objHorarios.getMan_de();
        String descanso = objHorarios.getMan_descanso();
        String chk_iempresa = objHorarios.getMan_iempresa();
        String chk_srefrigerio = objHorarios.getMan_srefrigerio();
        String chk_irefrigerio = objHorarios.getMan_irefrigerio();
        String chk_sempresa = objHorarios.getMan_sempresa();
        String lunes1 = objHorarios.getMan_lun1();
        String lunes2 = objHorarios.getMan_lun2();
        String lunes3 = objHorarios.getMan_lun3();
        String lunes4 = objHorarios.getMan_lun4();
        String martes1 = objHorarios.getMan_mar1();
        String martes2 = objHorarios.getMan_mar2();
        String martes3 = objHorarios.getMan_mar3();
        String martes4 = objHorarios.getMan_mar4();
        String miercoles1 = objHorarios.getMan_mier1();
        String miercoles2 = objHorarios.getMan_mier2();
        String miercoles3 = objHorarios.getMan_mier3();
        String miercoles4 = objHorarios.getMan_mier4();
        String jueves1 = objHorarios.getMan_juev1();
        String jueves2 = objHorarios.getMan_juev2();
        String jueves3 = objHorarios.getMan_juev3();
        String jueves4 = objHorarios.getMan_juev4();
        String viernes1 = objHorarios.getMan_vie1();
        String viernes2 = objHorarios.getMan_vie2();
        String viernes3 = objHorarios.getMan_vie3();
        String viernes4 = objHorarios.getMan_vie4();
        String sabado1 = objHorarios.getMan_sab1();
        String sabado2 = objHorarios.getMan_sab2();
        String sabado3 = objHorarios.getMan_sab3();
        String sabado4 = objHorarios.getMan_sab4();
        String domingo1 = objHorarios.getMan_dom1();
        String domingo2 = objHorarios.getMan_dom2();
        String domingo3 = objHorarios.getMan_dom3();
        String domingo4 = objHorarios.getMan_dom4();
        int antes1 = objHorarios.getMan_ant1();
        int antes2 = objHorarios.getMan_ant2();
        int antes3 = objHorarios.getMan_ant2();
        int antes4 = objHorarios.getMan_ant4();
        int despues1 = objHorarios.getMan_des1();
        int despues2 = objHorarios.getMan_des2();
        int despues3 = objHorarios.getMan_des3();
        int despues4 = objHorarios.getMan_des4();*/
        String codigo = objHorarios.getMan_codigo();
         int sucursal = objHorarios.getMan_sucursal();
        String descripcion = objHorarios.getMan_descripcion();
        String tipo = objHorarios.getTipo();
        String amanecida = objHorarios.getMan_amanecida();
        int rango = objHorarios.getMan_rango();
        int hr_trabajo = objHorarios.getMan_hr_trabajadas();
        int hr_refrigerio = objHorarios.getMan_hr_refrigerio();
        int hr_tolerancia = objHorarios.getMan_hr_tolerancia();
        String hr_semanal = objHorarios.getMan_hr_semanal();
        String desde = objHorarios.getMan_de();
        String descanso = objHorarios.getMan_descanso();
        String chk_iempresa = objHorarios.getMan_iempresa();
        String chk_srefrigerio = objHorarios.getMan_srefrigerio();
        String chk_irefrigerio = objHorarios.getMan_irefrigerio();
        String chk_sempresa = objHorarios.getMan_sempresa();
        String lunes1 = objHorarios.getMan_lun1();
        String lunes2 = objHorarios.getMan_lun2();
        String lunes3 = objHorarios.getMan_lun3();
        String lunes4 = objHorarios.getMan_lun4();
        String martes1 = objHorarios.getMan_mar1();
        String martes2 = objHorarios.getMan_mar2();
        String martes3 = objHorarios.getMan_mar3();
        String martes4 = objHorarios.getMan_mar4();
        String miercoles1 = objHorarios.getMan_mier1();
        String miercoles2 = objHorarios.getMan_mier2();
        String miercoles3 = objHorarios.getMan_mier3();
        String miercoles4 = objHorarios.getMan_mier4();
        String jueves1 = objHorarios.getMan_juev1();
        String jueves2 = objHorarios.getMan_juev2();
        String jueves3 = objHorarios.getMan_juev3();
        String jueves4 = objHorarios.getMan_juev4();
        String viernes1 = objHorarios.getMan_vie1();
        String viernes2 = objHorarios.getMan_vie2();
        String viernes3 = objHorarios.getMan_vie3();
        String viernes4 = objHorarios.getMan_vie4();
        String sabado1 = objHorarios.getMan_sab1();
        String sabado2 = objHorarios.getMan_sab2();
        String sabado3 = objHorarios.getMan_sab3();
        String sabado4 = objHorarios.getMan_sab4();
        String domingo1 = objHorarios.getMan_dom1();
        String domingo2 = objHorarios.getMan_dom2();
        String domingo3 = objHorarios.getMan_dom3();
        String domingo4 = objHorarios.getMan_dom4();
        int antes1 = objHorarios.getMan_ant1();
        int antes2 = objHorarios.getMan_ant2();
        int antes3 = objHorarios.getMan_ant2();
        int antes4 = objHorarios.getMan_ant4();
        int despues1 = objHorarios.getMan_des1();
        int despues2 = objHorarios.getMan_des2();
        int despues3 = objHorarios.getMan_des3();
        int despues4 = objHorarios.getMan_des4();
        int marcado = objHorarios.getMarcado();

        String sql_actualizar = "{call pack_tashorario.p_modificar(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
           /* cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, codigo);
            cst.setInt(4, sucursal);
            cst.setString(5, descripcion);
            cst.setString(6, tipo);
            cst.setString(7, amanecida);
            cst.setInt(8, rango);
            cst.setInt(9, hr_trabajo);
            cst.setInt(10, hr_refrigerio);
            cst.setInt(11, hr_tolerancia);
            cst.setString(12, hr_semanal);
            cst.setString(13, desde);
            cst.setString(14, descanso);
            cst.setString(15, chk_iempresa);
            cst.setString(16, chk_srefrigerio);
            cst.setString(17, chk_irefrigerio);
            cst.setString(18, chk_sempresa);
            cst.setString(19, lunes1);
            cst.setString(20, lunes2);
            cst.setString(21, lunes3);
            cst.setString(22, lunes4);
            cst.setString(23, martes1);
            cst.setString(24, martes2);
            cst.setString(25, martes3);
            cst.setString(26, martes4);
            cst.setString(27, miercoles1);
            cst.setString(28, miercoles2);
            cst.setString(29, miercoles3);
            cst.setString(30, miercoles4);
            cst.setString(31, jueves1);
            cst.setString(32, jueves2);
            cst.setString(33, jueves3);
            cst.setString(34, jueves4);
            cst.setString(35, viernes1);
            cst.setString(36, viernes2);
            cst.setString(37, viernes3);
            cst.setString(38, viernes4);
            cst.setString(39, sabado1);
            cst.setString(40, sabado2);
            cst.setString(41, sabado3);
            cst.setString(42, sabado4);
            cst.setString(43, domingo1);
            cst.setString(44, domingo2);
            cst.setString(45, domingo3);
            cst.setString(46, domingo4);
            cst.setInt(47, antes1);
            cst.setInt(48, antes2);
            cst.setInt(49, antes3);
            cst.setInt(50, antes4);
            cst.setInt(51, despues1);
            cst.setInt(52, despues2);
            cst.setInt(53, despues3);
            cst.setInt(54, despues4);
            cst.registerOutParameter(55, java.sql.Types.NUMERIC);
            cst.registerOutParameter(56, java.sql.Types.VARCHAR);
            cst.setString(57, objUsuCredential.getCuenta());
            cst.execute();
            s_msg = cst.getString(56);
            i_flagErrorBD = cst.getInt(55);*/
             cst.setInt(1, objUsuCredential.getCodemp()); 
            cst.setInt(2, sucursal);
            cst.setString(3, descripcion);
            cst.setString(4, tipo);
            cst.setString(5, amanecida);
            cst.setInt(6, rango);
            cst.setInt(7, hr_trabajo);
            cst.setInt(8, hr_refrigerio);
            cst.setInt(9, hr_tolerancia);
            cst.setString(10, hr_semanal);
            cst.setString(11, desde);
            cst.setString(12, descanso);
            cst.setString(13, chk_iempresa);
            cst.setString(14, chk_srefrigerio);
            cst.setString(15, chk_irefrigerio);
            cst.setString(16, chk_sempresa);
            cst.setString(17, lunes1);
            cst.setString(18, lunes2);
            cst.setString(19, lunes3);
            cst.setString(20, lunes4);
            cst.setString(21, martes1);
            cst.setString(22, martes2);
            cst.setString(23, martes3);
            cst.setString(24, martes4);
            cst.setString(25, miercoles1);
            cst.setString(26, miercoles2);
            cst.setString(27, miercoles3);
            cst.setString(28, miercoles4);
            cst.setString(29, jueves1);
            cst.setString(30, jueves2);
            cst.setString(31, jueves3);
            cst.setString(32, jueves4);
            cst.setString(33, viernes1);
            cst.setString(34, viernes2);
            cst.setString(35, viernes3);
            cst.setString(36, viernes4);
            cst.setString(37, sabado1);
            cst.setString(38, sabado2);
            cst.setString(39, sabado3);
            cst.setString(40, sabado4);
            cst.setString(41, domingo1);
            cst.setString(42, domingo2);
            cst.setString(43, domingo3);
            cst.setString(44, domingo4);
            cst.setInt(45, antes1);
            cst.setInt(46, antes2);
            cst.setInt(47, antes3);
            cst.setInt(48, antes4);
            cst.setInt(49, despues1);
            cst.setInt(50, despues2);
            cst.setInt(51, despues3);
            cst.setInt(52, despues4);
            cst.setString(53, objUsuCredential.getCuenta());
            cst.registerOutParameter(54, java.sql.Types.NUMERIC);
            cst.registerOutParameter(55, java.sql.Types.VARCHAR);
            cst.setInt(56, marcado);
            cst.setString(57, codigo);
            
            cst.execute();
            s_msg = cst.getString(55);
            i_flagErrorBD = cst.getInt(54);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + descripcion);
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
        return s_msg;
    }

    public String eliminar(ManHorarios objHorario) throws SQLException {
        String key = objHorario.getMan_codigo();

        String sql_eliminar = "{call pack_tashorario.p_eliminar(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, key);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objHorario.getMan_codigo());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    public ListModelList<ManHorarios> listHorarioBuscar(int empresa,int sucursal) throws SQLException {
        sql_query = "{call codijisa.pack_tashorario.p_consultar_buscar(?,?,?,?)}";
        objlstHorarios.clear();
        //P_WHERE = sql_query;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_query);
            cst.setInt(1, empresa);
            cst.setInt(2, sucursal);
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            P_WHERE = cst.getString(4);
            rs = ((OracleCallableStatement) cst).getCursor(3);
            objlstHorarios = null;
            objlstHorarios = new ListModelList<ManHorarios>();
            while (rs.next()) {
                objHorarios = new ManHorarios();
                objHorarios.setSucursal(rs.getString("sucursal"));
                objHorarios.setHorario(rs.getString("horario"));
                objHorarios.setTipo(rs.getString("tipo"));
                objHorarios.setTipo_dia(rs.getString("tipo_dia"));
                objHorarios.setIngreso(rs.getString("hor_ingreso"));
                objHorarios.setSal_ref(rs.getString("hor_salrefri"));
                objHorarios.setIng_ref(rs.getString("hor_ingrefri"));
                objHorarios.setSalida(rs.getString("hor_salida"));
                //****************************
                objHorarios.setSucursal(rs.getString("suc_des"));
                objHorarios.setMan_codigo(rs.getString("horario_id"));
                objHorarios.setMan_descripcion(rs.getString("hor_descrip"));
                // objHorarios.setTipo(rs.getString(""));
                objHorarios.setMan_amanecida(rs.getString("hor_amanecida"));
                objHorarios.setMan_rango(rs.getInt("hor_ranaut"));
                objHorarios.setMan_hr_trabajadas(rs.getInt("hor_horatrab"));
                objHorarios.setMan_hr_refrigerio(rs.getInt("hor_refrigerio"));
                objHorarios.setMan_hr_tolerancia(rs.getInt("hor_minutole"));
                objHorarios.setMan_hr_semanal(rs.getString("hsm_id"));
                // objHorarios.setMan_de(rs.getString("hor_diaini"));
                objHorarios.setMan_de(rs.getString("ddiaini"));
                objHorarios.setMan_a(rs.getString("ddiafin"));
                objHorarios.setMan_descanso(rs.getString("ddiades"));
                objHorarios.setS_sucursal(rs.getString("suc_des"));
               // objHorarios.setMan_descanso(rs.getString("hor_diades"));
                objHorarios.setMan_iempresa(rs.getString("hor_marie"));
                objHorarios.setMan_srefrigerio(rs.getString("hor_marsr"));
                objHorarios.setMan_irefrigerio(rs.getString("hor_marir"));
                objHorarios.setMan_sempresa(rs.getString("hor_marse"));
                //dias
                objHorarios.setMan_lun1(rs.getString("hor_dia1ing"));
                objHorarios.setMan_lun2(rs.getString("hor_dia1salref"));
                objHorarios.setMan_lun3(rs.getString("hor_dia1ingref"));
                objHorarios.setMan_lun4(rs.getString("hor_dia1sal"));
                objHorarios.setMan_mar1(rs.getString("hor_dia2ing"));
                objHorarios.setMan_mar2(rs.getString("hor_dia2salref"));
                objHorarios.setMan_mar3(rs.getString("hor_dia3ingref"));
                objHorarios.setMan_mar4(rs.getString("hor_dia2sal"));
                objHorarios.setMan_mier1(rs.getString("hor_dia3ing"));
                objHorarios.setMan_mier2(rs.getString("hor_dia3salref"));
                objHorarios.setMan_mier3(rs.getString("hor_dia3ingref"));
                objHorarios.setMan_mier4(rs.getString("hor_dia3sal"));
                objHorarios.setMan_juev1(rs.getString("hor_dia4ing"));
                objHorarios.setMan_juev2(rs.getString("hor_dia4salref"));
                objHorarios.setMan_juev3(rs.getString("hor_dia4ingref"));
                objHorarios.setMan_juev4(rs.getString("hor_dia4sal"));
                objHorarios.setMan_vie1(rs.getString("hor_dia5ing"));
                objHorarios.setMan_vie2(rs.getString("hor_dia5salref"));
                objHorarios.setMan_vie3(rs.getString("hor_dia5ingref"));
                objHorarios.setMan_vie4(rs.getString("hor_dia5sal"));
                objHorarios.setMan_sab1(rs.getString("hor_dia6ing"));
                objHorarios.setMan_sab2(rs.getString("hor_dia6salref"));
                objHorarios.setMan_sab3(rs.getString("hor_dia6ingref"));
                objHorarios.setMan_sab4(rs.getString("hor_dia6sal"));
                objHorarios.setMan_dom1(rs.getString("hor_dia7ing"));
                objHorarios.setMan_dom2(rs.getString("hor_dia7salref"));
                objHorarios.setMan_dom3(rs.getString("hor_dia7ingref"));
                objHorarios.setMan_dom4(rs.getString("hor_dia7sal"));
                objHorarios.setMan_ant1(rs.getInt("hor_tantes1"));
                objHorarios.setMan_ant2(rs.getInt("hor_tantes2"));
                objHorarios.setMan_ant3(rs.getInt("hor_tantes3"));
                objHorarios.setMan_ant4(rs.getInt("hor_tantes4"));
                objHorarios.setMan_des1(rs.getInt("hor_tdespu1"));
                objHorarios.setMan_des2(rs.getInt("hor_tdespu2"));
                objHorarios.setMan_des3(rs.getInt("hor_tdespu3"));
                objHorarios.setMan_des4(rs.getInt("hor_tdespu4"));
                objHorarios.setUsu_add(rs.getString("hor_usuadd"));
                objHorarios.setUsu_mod(rs.getString("hor_usumod"));
                objHorarios.setDia_add(rs.getTimestamp("hor_fecadd"));
                objHorarios.setDia_mod(rs.getTimestamp("hor_fecmod"));
                objHorarios.setMan_sucursal(rs.getInt("suc_id"));
                objHorarios.setDia_inicio(rs.getString("hor_diaini"));
                objHorarios.setDia_descanso(rs.getString("hor_diades"));
                objlstHorarios.add(objHorarios);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstHorarios.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {

                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstHorarios;
    }

    public String retornaTipHora(){
        String query = "";
        return "";
    }

}


