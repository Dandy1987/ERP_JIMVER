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
import java.util.Date;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.procesos.Descuentos;
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
public class DaoDescuentos {

    // ListModelList<Personal> objlstPersonal;
    ListModelList<Descuentos> objlstDescuentos;
    Descuentos objDescuentos;
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

    /**
     * ***************************************
     * para los movimiento de descuentos
     *
     *
     * @return
     * @throws java.sql.SQLException ***************************************
     */
    public int validaPeriodoProceso(String s_periodo) throws SQLException {
        int i_valida = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tperpag.f_periodo_movimiento(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3, s_periodo);
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

    public int validaPeriodoCalculando(String s_periodo) throws SQLException {
        int i_valida = 0;
        try {
            con = (new ConectaBD().conectar());
            String query = "{?=call pack_tperpag.f_validaperiodo_calculando(?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3, s_periodo);
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

    public ListModelList<Descuentos> busquedaConstanteDescuentos() throws SQLException {
        String query = "{call codijisa.pack_tpldsctos.p_lov_concepto(?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(1);
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            /*String query = " select t.tabla_id, t.tabla_descri"
             + " from tpltablas1 t"
             + " where t.tabla_cod = '00001'"
             + " and t.tabla_tipo1 in ('C','M')"
             + " and t.tabla_datbol in ('2')"
             + " order by 1";
             objlstDescuentos = new ListModelList<Descuentos>();
             try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(query);*/
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCod_constante(rs.getString("tabla_id"));
                objDescuentos.setDes_constante(rs.getString("tabla_descri"));
                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

    public ListModelList<Descuentos> buscarSeleccionDescuentos(String consulta) throws SQLException {
        String query = "{call codijisa.pack_tpldsctos.p_lov_enter_concepto(?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.setString(1, consulta);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(2);
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            /*  String query = " select t.tabla_id, t.tabla_descri "
             + " from tpltablas1 t"
             + " where t.tabla_cod = '00001'"
             + " and t.tabla_tipo1 in ('C','M')"
             + " and t.tabla_datbol in ('2')"
             + " and (t.tabla_id like '%" + consulta + "%' or t.tabla_descri like '%" + consulta + "%')"
             + " order by 1";
             //+ " and (e.tabla_id like '%" + consulta + "%' or e.tabla_descri like '%" + consulta + "%')"

             objlstDescuentos = new ListModelList<Descuentos>();
             try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(query);*/
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCod_constante(rs.getString("tabla_id"));
                objDescuentos.setDes_constante(rs.getString("tabla_descri"));
                objlstDescuentos.add(objDescuentos);

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

        return objlstDescuentos;

    }

    public Descuentos consultaContanteDescuento(String codigo) throws SQLException {
        String query = " select t.tabla_id, t.tabla_descri "
                + " from tpltablas1 t"
                + " where t.tabla_cod = '00001'"
                + " and t.tabla_valor1 in ('701','706')"
                + " and t.tabla_estado=1"
                + " and t.tabla_id<>000"
                //  + " and t.tabla_tipo1 in ('C','M')"
                + " and t.tabla_datbol in ('2','3')"
                + " and t.tabla_id ='" + codigo + "'"
                + " order by 1";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objDescuentos = null;
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCod_constante(rs.getString("tabla_id"));
                objDescuentos.setDes_constante(rs.getString("tabla_descri"));

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

    /**
     * Procedimiento oara insertar en base de datos
     *
     * @param lista
     * @return
     * @throws java.sql.SQLException
     */
    public ParametrosSalida insertar(Object[][] lista) throws SQLException {
        String query = "{call pack_tpldsctos.p_insertar_registro(?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTADESCUENTOS", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), lista);
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

    public ParametrosSalida insertarBloque(Object[][] lista) throws SQLException {
        String query = "{call pack_tpldsctos.p_insertar_bloque_registro(?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTADESCUENTOS", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), lista);
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

    /**
     * Procedimiento oara insertar en base de datos
     *
     * @param lista
     * @return
     * @throws java.sql.SQLException
     */
    public ParametrosSalida modificar(Object[][] lista) throws SQLException {
        String query = "{call pack_tpldsctos.p_modificar_registro(?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTADESCUENTOS", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), lista);
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

    /**
     * Metodo para el ingreso principal
     *
     * @param periodo
     * @return
     * @throws java.sql.SQLException
     */
    public ListModelList<Descuentos> consultar(String periodo) throws SQLException {
        String query = "{call codijisa.pack_tpldsctos.p_consulta(?,?,?)}";

        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, periodo);
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(3);

            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCodigo_vista(rs.getString("codigo"));
                objDescuentos.setPaterno(rs.getString("plapepat"));
                objDescuentos.setMaterno(rs.getString("plapemat"));
                objDescuentos.setNombre(rs.getString("plnomemp"));
                objDescuentos.setArea(rs.getString("plarea_des"));
                objDescuentos.setTipo_doc(rs.getInt("pltipdoc"));
                objDescuentos.setDocumento(rs.getString("plnrodoc"));
                objDescuentos.setSucursal(rs.getInt("suc_id"));
                objDescuentos.setFec_ing(rs.getDate("plfecing"));
                objDescuentos.setFec_ces(rs.getDate("plfecces"));
                objDescuentos.setNeto(rs.getDouble("neto"));
                objDescuentos.setPeriodo(rs.getString("plppag_id"));
                /* objMov.setUsu_add(rs.getString("pld_usuadd"));
                 objMov.setUsu_mod(rs.getString("pld_usumod"));
                 objMov.setFecha_add(rs.getTimestamp("pld_fecadd"));
                 objMov.setFecha_mod(rs.getTimestamp("pld_fecmod"));*/
                objlstDescuentos.add(objDescuentos);
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

        return objlstDescuentos;

    }

    /**
     * @param codigo -->codigo del trabajador
     * @param sucursal
     * @autor Junior Fernandez Ortiz version 10/08/2017
     * @return lista de datos a mostrar enm el detalle
     * @throws SQLException
     */
    public ListModelList<Descuentos> buscarDetalle(String codigo, int sucursal) throws SQLException {

        String query = "{call codijisa.pack_tpldsctos.p_buscaxpersonal(?,?,?,?)}";

        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, sucursal);
            cst.setString(3, codigo);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(4);

            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCodigo_vista(rs.getString("codigo"));
                objDescuentos.setCod_constante(rs.getString("pldc_idconc"));
                objDescuentos.setDes_constante(rs.getString("tabla_descri"));
                objDescuentos.setFecha_mov(rs.getDate("pldc_fecmov"));
                objDescuentos.setGlosa(rs.getString("pldc_glosa"));
                objDescuentos.setCargo(rs.getDouble("cargo"));
                objDescuentos.setAbono(rs.getDouble("abono"));
                objDescuentos.setNumero_op(rs.getInt("pldc_nroope"));
                objDescuentos.setUsu_add(rs.getString("pldc_usuadd"));
                objDescuentos.setUsu_mod(rs.getString("pldc_usumod"));
                objDescuentos.setFecha_add(rs.getTimestamp("pldc_fecadd"));
                objDescuentos.setFecha_mod(rs.getTimestamp("pldc_fecmod"));
                objDescuentos.setTipo_ingreso(rs.getInt("tipo_ing"));
                objlstDescuentos.add(objDescuentos);
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

        return objlstDescuentos;

    }

    /**
     * @param check
     * @autor Junior Fernandez Ortiz version 11/08/2017
     * @return lista de datos a mostrar en la lista Principal
     * @throws SQLException
     * @version 17/08/2017
     * @param sucursal
     * @param idcodigo
     * @param idconstante
     * @param area
     * @param periodo
     */
    public ListModelList<Descuentos> buscarRegistro(String sucursal, String idcodigo, String idconstante, String area, String periodo, Boolean check) throws SQLException {

        String check_negativo = check == true ? " where r.neto < 0 " : " where r.neto > 0 ";
        String s_sucursal = sucursal.isEmpty() ? "" : " and d.suc_id = " + sucursal + "";
        String s_personal = idcodigo.isEmpty() ? "" : " and d.pltipdoc||d.plnrodoc in ('" + idcodigo + "')";
        String cidconstante = idconstante.isEmpty() ? "" : " and d.pldc_idconc =" + idconstante + "";
        String s_area = area.isEmpty() ? "" : " and dl.plarea =" + area + "";
        String s_periodo = periodo.isEmpty() ? "" : " and d.plppag_id ='" + periodo + "'";

        /* if (check == true) {
         check_negativo = " where r.neto < 0  ";
         }else{
         check_negativo = " where r.neto > 0";
         }*/
        String query = " select * from (select"
                + " t.pltipdoc||t.plnrodoc codigo,"
                + " t.plapepat,t.plapemat,t.plnomemp,t.pltipdoc,t.plnrodoc,dl.plfecing,dl.plfecces,d.plppag_id,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003') plarea_des, dl.suc_id,"
                + " (  sum(to_number(lib.decrypt8(d.pldc_valcar),'99999990.999')) - sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999'))    )neto"
                //+ " sum(to_number(lib.decrypt8(d.pldc_valabo),'99999990.999')) abono,"
                + " from"
                + " tpersonal t,tpldsctos d,tpldatoslab dl"
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
                + " d.pldc_estado=1 and"
                + " dl.plestado_dl=1 and"
                + " d.emp_id =" + objUsuCredential.getCodemp()
                + s_sucursal + s_personal + cidconstante + s_area + s_periodo
                + " group by t.pltipdoc||t.plnrodoc ,t.plapepat,t.plapemat,t.plnomemp,t.pltipdoc,t.plnrodoc,dl.plfecing,dl.plfecces,d.plppag_id,"
                + " pack_tpersonal.ftb1_descripcion(dl.plarea,'00003'), dl.suc_id"
                + " order by t.plapepat) r "
                + check_negativo;

        /*String query = "{call codijisa.pack_tpldsctos.p_consultaFiltro(?,?,?,?,?,?,?)}";
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
         rs = ((OracleCallableStatement) cst).getCursor(7);*/
        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<Descuentos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCodigo_vista(rs.getString("codigo"));
                objDescuentos.setPaterno(rs.getString("plapepat"));
                objDescuentos.setMaterno(rs.getString("plapemat"));
                objDescuentos.setNombre(rs.getString("plnomemp"));
                objDescuentos.setArea(rs.getString("plarea_des"));
                objDescuentos.setTipo_doc(rs.getInt("pltipdoc"));
                objDescuentos.setDocumento(rs.getString("plnrodoc"));
                objDescuentos.setSucursal(rs.getInt("suc_id"));
                objDescuentos.setNeto(rs.getDouble("neto"));
                objDescuentos.setFec_ing(rs.getDate("plfecing"));
                objDescuentos.setFec_ces(rs.getDate("plfecces"));
                objDescuentos.setPeriodo(rs.getString("plppag_id"));
                objlstDescuentos.add(objDescuentos);
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

        return objlstDescuentos;

    }

    //codigo para realiar lov 
    public ListModelList<Descuentos> buscaBloque(String sucursal, String idcodigo, String idconstante, String area, String periodo, double valor) throws SQLException {

        String query = "{call codijisa.pack_movimiento_linea.p_consultaParaBloque(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
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
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCodigo_vista(rs.getString("codigo"));
                objDescuentos.setPaterno(rs.getString("plapepat"));
                objDescuentos.setMaterno(rs.getString("plapemat"));
                objDescuentos.setNombre(rs.getString("plnomemp"));
                objDescuentos.setArea(rs.getString("plarea_des"));
                objDescuentos.setTipo_doc(rs.getInt("pltipdoc"));
                objDescuentos.setSucursal(rs.getInt("suc_id"));
                //   objDescuentos.setValor_concepto(valor);

            }
        } catch (Exception e) {
        }

        return objlstDescuentos;

    }

    public ParametrosSalida insertarFaltante(int i_sucu, Descuentos objDesc, String periodo, Date d_fecha) throws SQLException {
        String query = "{call pack_tpldsctos.p_faltanteProcesar(?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);

            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, i_sucu);
            cst.setString(3, objDesc.getCod_personal());
            cst.setString(4, periodo);
            cst.setString(5, objDesc.getCod_constante());
            cst.setDate(6, convertJavaDateToSqlDate(d_fecha));
            cst.setString(7, objDesc.getGlosa());
            cst.setDouble(8, objDesc.getNeto());
            cst.setString(9, objDesc.getRecibo_egreso());
            cst.setString(10, objUsuCredential.getCuenta());

            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(12);
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

    public ListModelList<Descuentos> listaDescuentosFaltante(int nsucu, String s_periodo, String s_tipopersonal, Date d_fechaini, Date d_fechafin) throws SQLException {
        String query = "{call codijisa.pack_tpldsctos.p_consultafaltantes(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, nsucu);
            cst.setString(3, s_periodo);
            cst.setString(4, s_tipopersonal);
            if (d_fechaini != null) {
                cst.setDate(5, convertJavaDateToSqlDate(d_fechaini));
                cst.setDate(6, convertJavaDateToSqlDate(d_fechafin));
            } else {
                cst.setDate(5, null);
                cst.setDate(6, null);
            }
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(7);
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            /*String query = " select t.tabla_id, t.tabla_descri"
             + " from tpltablas1 t"
             + " where t.tabla_cod = '00001'"
             + " and t.tabla_tipo1 in ('C','M')"
             + " and t.tabla_datbol in ('2')"
             + " order by 1";
             objlstDescuentos = new ListModelList<Descuentos>();
             try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(query);*/
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCod_constante(rs.getString("cod"));
                objDescuentos.setCod_personal(rs.getString("codemp"));
                objDescuentos.setNombre_completo(rs.getString("trabajador"));
                objDescuentos.setNeto(rs.getDouble("monto"));
                objDescuentos.setGlosa(rs.getString("glosa"));
                objDescuentos.setRecibo_egreso(rs.getString("codrecibo"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

    public ListModelList<Descuentos> listaDescuentosReintegros(int nsucu, String s_periodo, String s_tipopersonal, Date d_fechaini, Date d_fechafin) throws SQLException {
        String query = "{call codijisa.pack_tpldsctos.p_consultareintegros(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, nsucu);
            cst.setString(3, s_periodo);
            cst.setString(4, s_tipopersonal);
            if (d_fechaini != null) {
                cst.setDate(5, convertJavaDateToSqlDate(d_fechaini));
                cst.setDate(6, convertJavaDateToSqlDate(d_fechafin));
            } else {
                cst.setDate(5, null);
                cst.setDate(6, null);
            }
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(7);
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();
            /*String query = " select t.tabla_id, t.tabla_descri"
             + " from tpltablas1 t"
             + " where t.tabla_cod = '00001'"
             + " and t.tabla_tipo1 in ('C','M')"
             + " and t.tabla_datbol in ('2')"
             + " order by 1";
             objlstDescuentos = new ListModelList<Descuentos>();
             try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(query);*/
            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCod_constante(rs.getString("cod"));
                objDescuentos.setCod_personal(rs.getString("codemp"));
                objDescuentos.setNombre_completo(rs.getString("trabajador"));
                objDescuentos.setNeto(rs.getDouble("monto"));
                objDescuentos.setGlosa(rs.getString("glosa"));
                objDescuentos.setRecibo_egreso(rs.getString("codrecibo"));
                objDescuentos.setRecibo_egreso_referencia(rs.getString("reciboref"));

                objlstDescuentos.add(objDescuentos);
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
        return objlstDescuentos;

    }

    public ListModelList<Descuentos> buscaBloquedescuentos(String sucursal, String idconstante, String area, String periodo, double valor, String glosa, String cargo, Date fecha) throws SQLException {

        String query = "{call codijisa.pack_tpldsctos.p_buscarBloque(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);

            cst.setInt(1, objUsuCredential.getCodemp());

            cst.setString(2, sucursal);
            cst.setString(3, idconstante);
            cst.setString(4, area);

            cst.setString(5, periodo);
            cst.registerOutParameter(6, OracleTypes.CURSOR);

            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(6);
            objlstDescuentos = null;
            objlstDescuentos = new ListModelList<Descuentos>();

            while (rs.next()) {
                objDescuentos = new Descuentos();
                objDescuentos.setCodigo_vista(rs.getString("codigo"));
                objDescuentos.setPaterno(rs.getString("plapepat"));
                objDescuentos.setMaterno(rs.getString("plapemat"));
                objDescuentos.setNombre(rs.getString("plnomemp"));
                objDescuentos.setDocumento(rs.getString("dni"));
                objDescuentos.setTipo_doc(rs.getInt("pltipdoc"));
                objDescuentos.setGlosa(glosa);
                if (cargo.equals("2")) {
                    objDescuentos.setCargo(valor);
                } else if (cargo.equals("3")) {
                    objDescuentos.setAbono(valor);
                }
                objDescuentos.setPeriodo(periodo);
                objDescuentos.setCod_constante(idconstante);
                objDescuentos.setFecha_mov(fecha);
                objDescuentos.setSucursal(rs.getInt("suc_id"));

                objlstDescuentos.add(objDescuentos);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {

                cst.close();
                rs.close();
                con.close();
            }
        }

        return objlstDescuentos;

    }

    /**
     * Metodo para insertar bloque de descuentos
     */
    public ParametrosSalida insertarBloqueDes(Object[][] lista) throws SQLException {
        String query = "{call pack_tpldsctos.p_insertar_bloque_registro(?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);

            arrayC = ArrayDescriptor.createDescriptor("LISTADESCUENTOS", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), lista);
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

    public ParametrosSalida insertarReintegro(int i_sucu, Descuentos objDesc, String periodo, Date d_fecha) throws SQLException {
        String query = "{call pack_tpldsctos.p_reintegroProcesar(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);

            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, i_sucu);
            cst.setString(3, objDesc.getCod_personal());
            cst.setString(4, periodo);
            cst.setString(5, objDesc.getCod_constante());
            cst.setDate(6, convertJavaDateToSqlDate(d_fecha));
            cst.setString(7, objDesc.getGlosa());
            cst.setDouble(8, objDesc.getNeto());
            cst.setString(9, objDesc.getRecibo_egreso());
            cst.setString(10, objDesc.getRecibo_egreso_referencia());
            cst.setString(11, objUsuCredential.getCuenta());

            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.registerOutParameter(13, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(13);
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
    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

}
