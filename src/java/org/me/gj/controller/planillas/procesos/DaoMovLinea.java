/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Movlinea;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoMovLinea {

    ListModelList<Movlinea> objlstMov;
    Movlinea objMov;
    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;
    ManAreas objArea;

    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";

    ArrayDescriptor arrayC, arrayCM;
    ARRAY arrC, arrCM;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    public ListModelList<Movlinea> busquedaConstante(String tipo) throws SQLException {

        String query = " select e.tabla_id,e.tabla_descri from TPLTABLAS1 E "
                + " WHERE E.TABLA_COD='00001'"
                + " and e.tabla_estado=1"
                + " and e.tabla_id<>000 "
                + " and e.tabla_tipo1='" + tipo + "'"
                + " order by e.tabla_id";
        objlstMov = new ListModelList<Movlinea>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setId_concepto(rs.getString("tabla_id"));
                objMov.setDescripcion(rs.getString("tabla_descri"));
                objlstMov.add(objMov);
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
        return objlstMov;

    }

    public Movlinea consultaContante(String codigo, String tipo) throws SQLException {
        String query = " select e.tabla_id,e.tabla_descri from TPLTABLAS1 E"
                + " WHERE E.TABLA_COD='00001'"
                + " and e.tabla_estado=1"
                + " and e.tabla_id<>000 "
                + " and e.tabla_tipo1='" + tipo + "'"
                + " and e.tabla_id =" + codigo + ""
                + " order by e.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objMov = null;
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setId_concepto(rs.getString("tabla_id"));
                objMov.setDescripcion(rs.getString("tabla_descri"));

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
        return objMov;
    }

    public ListModelList<Movlinea> buscarSeleccion(String consulta, String tipo) throws SQLException {

        String query = " select e.tabla_id,e.tabla_descri from TPLTABLAS1 E "
                + " WHERE E.TABLA_COD='00001'"
                + " and e.tabla_estado=1"
                + " and e.tabla_id<>000 "
                + " AND E.TABLA_TIPO1='" + tipo + "'"
                + " and (e.tabla_id like '%" + consulta + "%' or e.tabla_descri like '%" + consulta + "%')"
                + " order by e.tabla_id";
        objlstMov = new ListModelList<Movlinea>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setId_concepto(rs.getString("tabla_id"));
                objMov.setDescripcion(rs.getString("tabla_descri"));
                objlstMov.add(objMov);

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

        return objlstMov;

    }

    public ParametrosSalida insertarBloque(Object[][] listaConstante) throws SQLException {

        String query = "{call pack_movimiento_linea.p_insertar_bloque(?,?,?)}";

        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTACONSTANTE", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), listaConstante);

            cst.clearParameters();
            cst.setArray(1, arrC);

            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(2);
            i_flagErrorBD = cst.getInt(3);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }

    public ParametrosSalida insertarConstante(Object[][] listaConstante, Object[][] listaConstanteMensual) throws SQLException {

        String query = "{call pack_movimiento_linea.p_insertar_constante(?,?,?,?)}";

        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTACONSTANTE", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), listaConstante);
            arrayCM = ArrayDescriptor.createDescriptor("LISTACONSTANTEMENSUAL", con.getMetaData().getConnection());
            arrCM = new ARRAY(arrayCM, con.getMetaData().getConnection(), listaConstanteMensual);
            cst.clearParameters();
            cst.setArray(1, arrC);
            cst.setArray(2, arrCM);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(4);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }

    public ParametrosSalida modificaConstante(Object[][] listaConstante, Object[][] listaConstanteMensual) throws SQLException {
        String query = "{call codijisa.pack_movimiento_linea.p_modificar(?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTACONSTANTE", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), listaConstante);
            arrayCM = ArrayDescriptor.createDescriptor("LISTACONSTANTEMENSUAL", con.getMetaData().getConnection());
            arrCM = new ARRAY(arrayCM, con.getMetaData().getConnection(), listaConstanteMensual);
            cst.clearParameters();
            cst.setArray(1, arrC);
            cst.setArray(2, arrCM);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(4);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }

    /*
     * metodo para hallar el periodo con estado 2 que es proceso
     */
    public String setearPeriodo() throws SQLException {
        String valor = "";
        String sql = "select t.plppag_id as periodo"
                + " from tplperpag t"
                + " where t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.plppag_estado=1"
                + " and t.plppag_situ=2";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getString("periodo");
            }
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //  LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return valor;

    }

	public ListModelList<Personal> busquedaLovPersonal(int empresa,int sucursal,String area,String estado,String cese,String consulta) throws SQLException {
		
String sql_personal = "{call codijisa.pack_tpersonal.p_listPersonalLov(?,?,?,?,?,?,?)}";
      /*  String sql_personal = "select t.pltipdoc||t.plnrodoc id_per,"
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
                + " d.plfecces is null"
                + " order by t.plapepat";
        objlstPersonal = new ListModelList<Personal>();*/

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
            rs = ((oracle.jdbc.internal.OracleCallableStatement) cst).getCursor(7);
            objlstPersonal = null;  
            objlstPersonal = new ListModelList<Personal>();
            /*con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);*/
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

    public ListModelList<Personal> busquedaLovPersonalBoleta(int sucursal, String periodo, int tipo, String area, String consulta) throws SQLException {
        String query = "{call codijisa.pack_planilla_informes.p_lov_salto_boleta(?,?,?,?,?,?,?)}";
        if (area.isEmpty()) {
            area = "TODOS";
        }
        try {
            con = new ConectaBD().conectar();

            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, sucursal);
            cst.setString(3, periodo);
            cst.setInt(4, tipo);
            cst.setString(5, area);
            cst.setString(6, consulta);
            cst.registerOutParameter(7, OracleTypes.CURSOR); //REF CURSOR
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
                objPersonal.setPlarea_des(rs.getString("area_des"));
                objPersonal.setPlarea(rs.getString("area"));
                objlstPersonal.add(objPersonal);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                // st.close();
                rs.close();
                con.close();
            }

        }
        return objlstPersonal;
    }

    public ListModelList<Personal> busquedaLovPersonal2(String consulta) throws SQLException {

        String sql_personal = "select t.pltipdoc||t.plnrodoc id_per,"
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
                + " (t.plapepat||' '||t.plapemat||' '||t.plnomemp like '" + consulta + "' or t.plnrodoc like '" + consulta + "')"
                + " order by t.plapepat";

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

    /**
     * @param periodo
     * @autor Junior Fernandez Ortiz version 10/08/2017
     * @return lista de datos a mostrar
     * @throws SQLException
     */
    public ListModelList<Movlinea> ingresoMovimiento(String periodo) throws SQLException {

        String query = "{call codijisa.pack_movimiento_linea.p_consulta(?,?,?)}";

        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, periodo);
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(3);

            objlstMov = null;
            objlstMov = new ListModelList<Movlinea>();
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setCodigo_vista(rs.getString("codigo"));
                objMov.setPaterno(rs.getString("plapepat"));
                objMov.setMaterno(rs.getString("plapemat"));
                objMov.setNombre(rs.getString("plnomemp"));
                objMov.setArea(rs.getString("plarea_des"));
                objMov.setTipo_doc(rs.getInt("pltipdoc"));
                objMov.setNumero_doc(rs.getString("plnrodoc"));
                objMov.setSucursal(rs.getInt("suc_id"));

                objMov.setValor_constante(0);
                objMov.setValor_constante_mesual(0);
                /* objMov.setUsu_add(rs.getString("pld_usuadd"));
                 objMov.setUsu_mod(rs.getString("pld_usumod"));
                 objMov.setFecha_add(rs.getTimestamp("pld_fecadd"));
                 objMov.setFecha_mod(rs.getTimestamp("pld_fecmod"));*/
                objlstMov.add(objMov);
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

        return objlstMov;

    }

    /**
     * @param codigo -->codigo del trabajador
     * @param tipo -- tipo de constante
     * @param sucursal -- codigo de sucursal
     * @autor Junior Fernandez Ortiz version 10/08/2017
     * @return lista de datos a mostrar enm el detalle
     * @throws SQLException
     */
    public ListModelList<Movlinea> buscarDetalle(String codigo, String tipo, int sucursal) throws SQLException {

        String query = "{call codijisa.pack_movimiento_linea.p_buscaxpersonal(?,?,?,?,?)}";

        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, sucursal); //objUsuCredential.getCodsuc());
            cst.setString(3, codigo);
            cst.setString(4, tipo);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(5);

            objlstMov = null;
            objlstMov = new ListModelList<Movlinea>();
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setCodigo_vista(rs.getString("codigo"));
                objMov.setId_concepto(rs.getString("pld_idconc"));
                objMov.setDescripcion(rs.getString("tabla_descri"));
                objMov.setTipo(rs.getString("tipo"));
                objMov.setValor_concepto(rs.getDouble("valor"));
                objMov.setUsu_add(rs.getString("pld_usuadd"));
                objMov.setUsu_mod(rs.getString("pld_usumod"));
                objMov.setFecha_add(rs.getTimestamp("pld_fecadd"));
                objMov.setFecha_mod(rs.getTimestamp("pld_fecmod"));
                objMov.setSucursal(rs.getInt("suc_id"));
				//SE modifico jr 19/01/2017
                objMov.setNro_ope(rs.getInt("pld_nroope"));
                if (tipo.equals("C")) {
                    objMov.setValor_constante(rs.getDouble("valor"));
                } else if (tipo.equals("M")) {
                    objMov.setValor_constante_mesual(rs.getDouble("valor"));
                }
                objlstMov.add(objMov);
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

        return objlstMov;

    }

    /**
     * @autor Junior Fernandez Ortiz version 11/08/2017
     * @return lista de datos a mostrar en la lista Principal
     * @throws SQLException
     * @param sucursal
     * @param idcodigo
     * @param idconstante
     * @param area
     * @param periodo
     */
    public ListModelList<Movlinea> buscarRegistro(String sucursal, String idcodigo, String idconstante, String area, String periodo) throws SQLException {
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";
        String s_personal = idcodigo.isEmpty() ? "" : " and d.pltipdoc||d.plnrodoc in ('" + idcodigo + "')";
        String cidconstante = idconstante.isEmpty() ? "" : " and d.pld_idconc =" + idconstante + "";
        String s_area = area.isEmpty() ? "" : " and dl.plarea in ('" + area + "')";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id ='" + periodo + "'";

        String query = " select"
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat,t.plapemat,t.plnomemp,t.pltipdoc,t.plnrodoc,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') plarea_des, dl.suc_id ,"
                + " case when '" + idconstante + "' is not null then pack_movimiento_linea.f_buscatipoC('" + idconstante + "') else 'X' end tipo,"
                + " max(case when '" + idconstante + "' is not null then to_number(lib.decrypt8(d.pld_valor)) else 0 end) valor"
                + " from"
                + " tpersonal t,tpldatosfv d,tpldatoslab dl"
                + " where"
                + " t.pltipdoc=d.pltipdoc and"
                + " t.plnrodoc=d.plnrodoc and"
                + " t.pltipdoc=dl.pltipdoc and"
                + " t.plnrodoc=dl.plnrodoc and"
                + " d.emp_id=dl.emp_id and"
                + " d.suc_id=dl.suc_id and"
                + " d.pltipdoc=dl.pltipdoc and"
                + " d.plnrodoc = dl.plnrodoc and"
                + " t.plestado=1 and"
                + " d.pld_estado=1 and"
                + " dl.plestado_dl=1 and"
                + " d.emp_id = " + objUsuCredential.getCodemp()
                + s_sucursal + s_personal + cidconstante + s_area + s_periodo
                + " group by t.pltipdoc||t.plnrodoc ,t.plapepat,t.plapemat,t.plnomemp,t.pltipdoc,t.plnrodoc,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003'), dl.suc_id"
                + " order by t.plapepat";
        objlstMov = null;
        objlstMov = new ListModelList<Movlinea>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setCodigo_vista(rs.getString("codigo"));
                objMov.setPaterno(rs.getString("plapepat"));
                objMov.setMaterno(rs.getString("plapemat"));
                objMov.setNombre(rs.getString("plnomemp"));
                objMov.setArea(rs.getString("plarea_des"));
                objMov.setTipo_doc(rs.getInt("pltipdoc"));
                objMov.setNumero_doc(rs.getString("plnrodoc"));
                objMov.setSucursal(rs.getInt("suc_id"));
                if (rs.getString("tipo").equals("C")) {
                    objMov.setValor_constante(rs.getDouble("valor"));
                } else if (rs.getString("tipo").equals("M")) {
                    objMov.setValor_constante_mesual(rs.getDouble("valor"));
                }
                objlstMov.add(objMov);
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
        return objlstMov;

    }

//  String query = "{call codijisa.pack_movimiento_linea.p_consultaFiltro(?,?,?,?,?,?,?)}";

    /* try {
     con = (new ConectaBD().conectar());
     cst = con.prepareCall(query);
     cst.setInt(1, objUsuCredential.getCodemp());
     cst.setString(2, sucursal);
     cst.setString(3, idcodigo);
     cst.setString(4, idconstante);
     cst.setString(5, area);
     cst.setString(6, periodo);
     cst.registerOutParameter(7, OracleTypes.CURSOR);
     cst.executeQuery();
     rs = ((OracleCallableStatement) cst).getCursor(7);
     */
   
    
     public int verificarDniVacaciones(String dni) throws SQLException {
        int valor = 0;String sql;
        try {
            con = new ConectaBD().conectar();
            sql = "{ ?= call PACK_TPLVACGOZ.f_exi_empleado(?,?)}";
            cst = con.prepareCall(sql);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setString(2, dni);
            cst.setInt(3, objUsuCredential.getCodemp());
            cst.execute();
            valor = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return valor;
    }
     
    /*
     * metodo para hallar si ya existe el trabajador tiene registrado
     */
    public int verificarDni(String dni, String periodo) throws SQLException {
        int valor = 0;
        String sql ;/*= " select count(t.plnrodoc) as dni"
                + " from tpldatosfv t"
                + " where t.pltipdoc||t.plnrodoc='" + dni + "'"
                + " and t.plppag_id ='" + periodo + "'"
                + " and t.emp_id = " + objUsuCredential.getCodemp();*/
        try {
            con = new ConectaBD().conectar();
            sql = "{?= call pack_tpldatfor.f_exi_empleado(?,?,?)}";
            cst = con.prepareCall(sql);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setString(2, dni);
            cst.setString(3, periodo);
            cst.setInt(4, objUsuCredential.getCodemp());
            cst.execute();
            valor = cst.getInt(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return valor;

    }
	
    public ListModelList<Movlinea> buscarBloque(String sucursal, String idcodigo, String idconstante, String area, String periodo, double valor) throws SQLException {

        String query = "{call codijisa.pack_movimiento_linea.p_consultaParaBloque(?,?,?,?,?,?,?)}";

        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, sucursal);
            cst.setString(3, idcodigo);
            cst.setString(4, idconstante);
            cst.setString(5, area);
            cst.setString(6, periodo);
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(7);

            objlstMov = null;
            objlstMov = new ListModelList<Movlinea>();
            while (rs.next()) {
                objMov = new Movlinea();
                objMov.setCodigo_vista(rs.getString("codigo"));
                objMov.setPaterno(rs.getString("plapepat"));
                objMov.setMaterno(rs.getString("plapemat"));
                objMov.setNombre(rs.getString("plnomemp"));
                objMov.setArea(rs.getString("plarea_des"));
                objMov.setTipo_doc(rs.getInt("pltipdoc"));
                objMov.setNumero_doc(rs.getString("plnrodoc"));
                objMov.setSucursal(rs.getInt("suc_id"));
                objMov.setValor_concepto(valor);
				objMov.setNro_ope(rs.getInt("pld_nroope"));
                //auditoria d.,d.,d.,d.
              /*  objMov.setUsu_add(rs.getString("pld_usuadd"));
                 objMov.setUsu_mod(rs.getString("pld_usumod"));
                 objMov.setFecha_add(rs.getTimestamp("pld_fecadd"));
                 objMov.setFecha_mod(rs.getTimestamp("pld_fecmod"));*/
                objlstMov.add(objMov);
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

        return objlstMov;

    }

    /*
     * Metodo para hallar si hay dni en la tabla tpldsctos
     */
    /*
     * metodo para hallar si ya existe el trabajador tiene registrado
     */
    public int verificarDniDescuentos(String dni, String periodo) throws SQLException {
        int valor = 0;
        String sql = " select count(t.plnrodoc) as dni"
                + " from tpldsctos t"
                + " where t.pltipdoc||t.plnrodoc='" + dni + "'"
                + " and t.plppag_id ='" + periodo + "'"
                + " and t.emp_id = " + objUsuCredential.getCodemp();
        // + " and t.suc_id = " + objUsuCredential.getCodsuc();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getInt("dni");
            }
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //  LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return valor;

    }

    public ListModelList<Personal> buscarPersonaBoleta(int sucursal, String periodo, int tipo, String area) throws SQLException {
        String query = "{call codijisa.pack_planilla_informes.p_lov_boleta(?,?,?,?,?,?)}";
        if(area.isEmpty()){
            area = "TODOS";
        }
        try {
            con = new ConectaBD().conectar();

            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, sucursal);
            cst.setString(3, periodo);
            cst.setInt(4, tipo);
            cst.setString(5, area);
            cst.registerOutParameter(6, OracleTypes.CURSOR); //REF CURSOR
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(6);

            objlstPersonal = null;
            objlstPersonal = new ListModelList<Personal>();

            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                objPersonal.setSuc_id_des(rs.getString("suc_des"));
                objPersonal.setPlarea_des(rs.getString("area_des"));
                objPersonal.setPlarea(rs.getString("area"));
                objlstPersonal.add(objPersonal);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                // st.close();
                rs.close();
                con.close();
            }

        }

        return objlstPersonal;

    }

    public ManAreas consultaArea(String areaid) throws SQLException {
        String query = "{call codijisa.pack_movimiento_linea.p_validaArea(?,?)}";

        try {
            con = new ConectaBD().conectar();

            cst = con.prepareCall(query);
            cst.setString(1, areaid);

            cst.registerOutParameter(2, OracleTypes.CURSOR); //REF CURSOR
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(2);

            objArea = null;
            //objlstPersonal = new ListModelList<Personal>();

            while (rs.next()) {
                objArea = new ManAreas();
                objArea.setArea_id(rs.getString("tabla_id"));
                objArea.setArea_des(rs.getString("tabla_descri"));
                objArea.setArea_cod_costo(rs.getString("plccosto"));
                objArea.setArea_des_costo(rs.getString("cc_descri"));

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                // st.close();
                rs.close();
                con.close();
            }

        }
        return objArea;
    }

}
