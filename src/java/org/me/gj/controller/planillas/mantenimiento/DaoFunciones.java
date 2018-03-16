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
import org.me.gj.model.planillas.mantenimiento.EditorFormulas;
import org.me.gj.model.planillas.mantenimiento.Formulas;
import org.me.gj.model.planillas.mantenimiento.Tablas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author wcamacho
 */
public class DaoFunciones {
    
    ListModelList<Tablas> objListModelTablas;
    ListModelList<Formulas> objListModelFormulas;
    Tablas objTablas;
    Formulas objFormulas;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    
    public static String P_WHERE = "";
    
    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoFunciones.class);
    
    public ListModelList<Formulas> listaFormulas(int s_cargo_est) throws SQLException {
        
        String SQL_LISTA_FORMULAS = "{call codijisa.pack_tformulas.p_lista_formulas(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(SQL_LISTA_FORMULAS);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(3, s_cargo_est);
            cst.registerOutParameter(4, OracleTypes.CURSOR); //REF CURSOR
            cst.registerOutParameter(5, OracleTypes.VARCHAR);
            cst.execute();
            
            P_WHERE = cst.getString(5);
            
            rs = ((OracleCallableStatement) cst).getCursor(4);
            st = con.createStatement();
            //objFormulas = null;
            objListModelFormulas = new ListModelList<Formulas>();
            while (rs.next()) {
                objFormulas = new Formulas();
                objFormulas.setEmp_id(rs.getInt("EMP_ID"));
                objFormulas.setSuc_id(rs.getInt("SUC_ID"));
                objFormulas.setForm_id(rs.getString("FORM_ID"));
                objFormulas.setForm_descri(rs.getString("FORM_DESCRI"));
                objFormulas.setForm_estado(rs.getInt("FORM_ESTADO"));
                objFormulas.setForm_contenido(rs.getString("FORM_CONTENIDO") == null ? "" : rs.getString("FORM_CONTENIDO"));
                objFormulas.setForm_calculo(rs.getString("FORM_CALCULO") == null ? "" : rs.getString("FORM_CALCULO"));
                objFormulas.setForm_sep_contenido(rs.getString("FORM_SEP_CONTENIDO") == null ? "" : rs.getString("FORM_SEP_CONTENIDO"));
                objFormulas.setForm_sep_calculo(rs.getString("FORM_SEP_CALCULO") == null ? "" : rs.getString("FORM_SEP_CALCULO"));                
                objFormulas.setForm_usuadd(rs.getString("FORM_USUADD") == null ? "" : rs.getString("FORM_USUADD"));
                objFormulas.setForm_fecadd(rs.getTimestamp("FORM_FECADD"));
                objFormulas.setForm_usumod(rs.getString("FORM_USUMOD") == null ? "" : rs.getString("FORM_USUMOD"));
                objFormulas.setForm_fecmod(rs.getTimestamp("FORM_FECMOD"));                
                objListModelFormulas.add(objFormulas);
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
        return objListModelFormulas;
    }
    
    public ListModelList<Tablas> getListBoxTablas(String SQL_QUERY) throws SQLException {
        objListModelTablas = new ListModelList<Tablas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_QUERY);
            while (rs.next()) {
                objTablas = new Tablas();
                objTablas.setConcepto_puro(rs.getString("CONCEPTO_PURO"));
                objTablas.setPldatfor_dview(rs.getString("CONCEPTO"));
                objTablas.setPldatfor_dform(rs.getString("FORMULA"));
                objListModelTablas.add(objTablas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListModelTablas.getSize() + " registro(s)");
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
        return objListModelTablas;
    }
    
    public String actualizar(ListModelList<EditorFormulas> objListModelEditorFormulas, int i_estado) throws SQLException {
        //
        String s_form_id = objListModelEditorFormulas.get(0).getS_cod_concepto();        
        String s_contenido = "", s_contenido_sep = "", s_formula = "", s_sep_formula = "";
        // Barrer Lista de objetos para armar la cadena a guardar
        for (int i = 0; i < objListModelEditorFormulas.getSize(); i++) {
            s_contenido += objListModelEditorFormulas.get(i).getConcepto_puro().trim() + " ";
            s_contenido_sep += objListModelEditorFormulas.get(i).getConcepto_puro() + "@";
            s_formula += objListModelEditorFormulas.get(i).getFormula().trim() + " ";
            s_sep_formula += objListModelEditorFormulas.get(i).getFormula() + "@";
        }
        // Declaracion de sentencia SQL
        String SQL_UPDATE = "{call pack_tformulas.p_modificar_formula(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(3, i_estado);
            cst.setString(4, s_form_id);
            cst.setString(5, s_contenido);
            cst.setString(6, s_formula);
            cst.setString(7, s_contenido_sep);
            cst.setString(8, s_sep_formula);
            cst.setString(9, objUsuCredential.getCuenta());            
            cst.setString(10, objUsuCredential.getComputerName());            
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + s_form_id);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }
    
    public String eliminar(Formulas objFormula) throws SQLException {
        // Declaracion de sentencia SQL
        String SQL_UPDATE = "{call pack_tformulas.p_eliminar_formula(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, objFormula.getForm_id());            
            cst.setString(4, objUsuCredential.getCuenta());            
            cst.setString(5, objUsuCredential.getComputerName());            
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objFormula.getForm_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }
    
    public ListModelList<Formulas> listaFormulas2(int selec, String consulta, int est) throws SQLException {
        String sql = "", estado;
        //para ver si estado es activo, inactivo o ambos
        if (est == 3) {
            estado = " t.form_estado in ('1','2')";
        } else if (est == 1) {
            estado = " t.form_estado = 1";
        } else {
            estado = " t.form_estado = 2";
        }
        //para evr seleccion si es por id o descripcion
        if (selec == 0) {
            
            sql = "select t.emp_id , t.suc_id ,t.form_id , pack_tconceptos.f_descripcion('00001',t.form_id) form_descri ,"
                    + " t.form_estado , t.form_contenido , t.form_calculo , t.form_sep_contenido , t.form_sep_calculo ,"
                    + " t.form_usuadd , t.form_fecadd , t.form_usumod , t.form_fecmod"
                    + " from codijisa.tplformulas t"
                    + " where"
                    + estado
                    + " and t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                    + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                    + " order by t.form_id";
            
        } else if (selec == 1) {
            sql = "select t.emp_id , t.suc_id ,t.form_id , pack_tconceptos.f_descripcion('00001',t.form_id) form_descri ,"
                    + " t.form_estado , t.form_contenido , t.form_calculo , t.form_sep_contenido , t.form_sep_calculo ,"
                    + " t.form_usuadd , t.form_fecadd , t.form_usumod , t.form_fecmod"
                    + " from codijisa.tplformulas t"
                    + " where"
                    + estado
                    + " and t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                    + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.form_id like '" + consulta + "'"
                    + " order by t.form_id";
            
        } 
        else if (selec == 2) {
            sql = "select t.emp_id , t.suc_id ,t.form_id , pack_tconceptos.f_descripcion('00001',t.form_id) form_descri ,"
                    + " t.form_estado , t.form_contenido , t.form_calculo , t.form_sep_contenido , t.form_sep_calculo ,"
                    + " t.form_usuadd , t.form_fecadd , t.form_usumod , t.form_fecmod"
                    + " from codijisa.tplformulas t"
                    + " where"
                    + estado
                    + " and t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                    + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                    + " and pack_tconceptos.f_descripcion('00001',t.form_id) like '" + consulta + "'"
                    + " order by t.form_id";            
        }
        
        else if (selec == 3) {
            sql = "select t.emp_id , t.suc_id ,t.form_id , pack_tconceptos.f_descripcion('00001',t.form_id) form_descri ,"
                    + " t.form_estado , t.form_contenido , t.form_calculo , t.form_sep_contenido , t.form_sep_calculo ,"
                    + " t.form_usuadd , t.form_fecadd , t.form_usumod , t.form_fecmod"
                    + " from codijisa.tplformulas t"
                    + " where"
                    + estado
                    + " and t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                    + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.form_contenido like '" + consulta + "'"
                    + " order by t.form_id";            
        }
        
        objListModelFormulas = new ListModelList<Formulas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                objFormulas = new Formulas();
                objFormulas.setEmp_id(rs.getInt("EMP_ID"));
                objFormulas.setSuc_id(rs.getInt("SUC_ID"));
                objFormulas.setForm_id(rs.getString("FORM_ID"));
                objFormulas.setForm_descri(rs.getString("FORM_DESCRI"));
                objFormulas.setForm_estado(rs.getInt("FORM_ESTADO"));
                objFormulas.setForm_contenido(rs.getString("FORM_CONTENIDO") == null ? "" : rs.getString("FORM_CONTENIDO"));
                objFormulas.setForm_calculo(rs.getString("FORM_CALCULO") == null ? "" : rs.getString("FORM_CALCULO"));
                objFormulas.setForm_sep_contenido(rs.getString("FORM_SEP_CONTENIDO") == null ? "" : rs.getString("FORM_SEP_CONTENIDO"));
                objFormulas.setForm_sep_calculo(rs.getString("FORM_SEP_CALCULO") == null ? "" : rs.getString("FORM_SEP_CALCULO"));                
                objFormulas.setForm_usuadd(rs.getString("FORM_USUADD") == null ? "" : rs.getString("FORM_USUADD"));
                objFormulas.setForm_fecadd(rs.getTimestamp("FORM_FECADD"));
                objFormulas.setForm_usumod(rs.getString("FORM_USUMOD") == null ? "" : rs.getString("FORM_USUMOD"));
                objFormulas.setForm_fecmod(rs.getTimestamp("FORM_FECMOD"));                
                objListModelFormulas.add(objFormulas);
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
        return objListModelFormulas;
    }

    /**
     * metodo para clonacion
     * @return 
     * @throws java.sql.SQLException
     */
    public ParametrosSalida clonar() throws SQLException {
        String query = "{call codijisa.pack_tformulas.p_clonar_funciones(?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(1);
        } catch (Exception e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        
        return new ParametrosSalida(i_flagErrorBD, s_msg);
        
    }
}
