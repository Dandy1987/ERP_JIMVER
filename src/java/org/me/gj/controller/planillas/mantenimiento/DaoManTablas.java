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
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManTablas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoManTablas {
    
    ListModelList<ManTablas> objlstTablas = new ListModelList<ManTablas>();
    ManTablas objTablas;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
  
    public static String P_WHERER = "";
    
     public static String P_WHERE = "";
  
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManTablas.class);

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ManTablas> listaPeriodos() throws SQLException {
        String sql;
        
        sql = "select distinct t.per_anio  from tperiodos t where t.per_estado='1' order by t.per_anio desc";
        
       
        
        try {
            
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                objTablas = new ManTablas();
                
                objTablas.setAnio(rs.getString("per_anio"));
                
                objlstTablas.add(objTablas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTablas.getSize() + " registro(s)");
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
        return objlstTablas;
    }
    
    public ListModelList<ManTablas> lstTablas(String periodo) throws SQLException {
        String sql;
        sql = " select *  from tpltablas2 e where  e.tabla_estado='1'"
                + " and e.tabla_per='" +  periodo +"'"
                + " and e.tabla_idmes='00'"
                + " order by e.tabla_id";
        
        P_WHERE = sql;
        objlstTablas = new ListModelList<ManTablas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTablas = new ManTablas();
                objTablas.setCodigo(rs.getString("tabla_id"));
                objTablas.setDescripcion(rs.getString("tabla_descri"));
                objTablas.setTipo(rs.getString("tabla_tipo"));
                objTablas.setPeriodo(rs.getString("tabla_per"));
                objTablas.setEstado(rs.getInt("tabla_estado"));
                objTablas.setUsu_add(rs.getString("tabla_usuadd"));
                objTablas.setUsu_mod(rs.getString("tabla_usumod"));
                objTablas.setDia_add(rs.getTimestamp("tabla_fecadd"));
                objTablas.setDia_mod(rs.getTimestamp("tabla_fecmod"));
                
                objlstTablas.add(objTablas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTablas.getSize() + " registro(s)");
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
        return objlstTablas;
    }
    
    public String eliminar(ManTablas objTablas) throws SQLException {
        String key = objTablas.getCodigo();
        String periodo = objTablas.getPeriodo();
        String sql_eliminar = "{call pack_tpltablas2.p_eliminar(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setString(1, key);
            cst.setString(2, periodo);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objTablas.getCodigo());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return s_msg;
    }
    
    public String actualizar(ManTablas objTablas, String descripcion, String mes, double valor) throws SQLException {
        String periodo = objTablas.getPeriodo();
        String codigo = objTablas.getCodigo();
        String descri = descripcion;
        String me = mes;
        String tipo = objTablas.getTipo_valor();
        double defaul = valor;
        int estado = objTablas.getEstado();
        String sql_actualizar = "{call pack_tpltablas2.p_modificar(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
            cst.setString(1, periodo);
            cst.setString(2, codigo);
            cst.setString(3, descri);
            cst.setString(4, me);
            cst.setString(5, tipo);
            cst.setDouble(6, defaul);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setInt(8, estado);
            cst.setString(9, objUsuCredential.getComputerName());
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objTablas.getCodigo());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return s_msg;
    }
    
    public String insertar(ManTablas objTablas, String descripcion, String mes, double valor) throws SQLException {
        String periodo = objTablas.getPeriodo();
        String codigo = objTablas.getCodigo();
        String descri = descripcion;
        String me = mes;
        String tipo = objTablas.getTipo_valor();
        double defaul = valor;
        /*double enero = objTablas.getEnero();
         double febrero = objTablas.getFebrero();
         double marzo = objTablas.getMarzo();
         double abril = objTablas.getAbril();
         double mayo = objTablas.getMayo();
         double junio = objTablas.getJunio();
         double julio = objTablas.getJulio();
         double agosto = objTablas.getAgosto();
         double septiembre = objTablas.getSeptiembre();
         double octubre = objTablas.getOctubre();
         double noviembre = objTablas.getNoviembre();
         double diciembre = objTablas.getDiciembre();*/
        String sql_insertar = "{call pack_tpltablas2.p_insertar(?,?,?,?,?,?,?,?,?,?)}";
        
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            cst.setString(1, periodo);
            cst.setString(2, codigo);
            cst.setString(3, descri);
            cst.setString(4, me);
            cst.setString(5, tipo);
            cst.setDouble(6, defaul);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setString(8, objUsuCredential.getComputerName());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTablas.getCodigo());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return s_msg;
    }
    
    public ListModelList<ManTablas> lista_det(String codigo, String periodo) throws SQLException {
        String query = " select * "
                + " from tpltablas2 e "
                + " where  e.tabla_estado='1'"
                + " and e.tabla_id='" + codigo + "'"
                + " and e.tabla_per='" + periodo + "'"
                + " order by e.tabla_idmes";
        
        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            objlstTablas = new ListModelList<ManTablas>();
            while (rs.next()) {                
                objTablas = new ManTablas();
                objTablas.setCodigo(rs.getString("tabla_id"));
                objTablas.setDescripcion(rs.getString("tabla_descri"));
                objTablas.setTipo(rs.getString("tabla_tipo"));
                objTablas.setMes(rs.getString("tabla_idmes"));
                objTablas.setPeriodo(rs.getString("tabla_per"));
                objTablas.setEstado(rs.getInt("tabla_estado"));
                objTablas.setDefaul(rs.getDouble("tabla_valor"));
                objTablas.setUsu_add(rs.getString("tabla_usuadd"));
                objTablas.setUsu_mod(rs.getString("tabla_usumod"));
                objTablas.setDia_add(rs.getTimestamp("tabla_fecadd"));
                objTablas.setDia_mod(rs.getTimestamp("tabla_fecmod"));
                
                objlstTablas.add(objTablas);
                
            }
        } catch (SQLException e) {
            // s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            //  s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstTablas;
    }
    
    
    
    public ListModelList<ManTablas> buscaCorrelativo() throws SQLException {
        String sql_lista_activo;

        try {
            //  con=(new ConectaBD()).conectar(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
            con = (new ConectaBD()).conectar();
            sql_lista_activo = "{?=call codijisa.pack_tpltablas2.f_correlativo(?,?)}";
            //String SQL_CONSULTA_EFI = "{?=call tpack_prueba2.f_consulta_eficomp(?,?,?)}";
            cst = con.prepareCall(sql_lista_activo);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setInt(3, objUsuCredential.getCodsuc());
       
            cst.execute();
            String consulta = cst.getString(1);

            P_WHERER = consulta;

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
        return objlstTablas;
    }
    
    public ListModelList<ManTablas> consultar(int selec, String consulta, int est,String periodo) throws SQLException {
        String sql = "";
        String es;
        if (est == 3) {
            es = "b.tabla_estado in ('1','2')";
        } else if (est == 1) {
            es = "b.tabla_estado ='1'";
        } else {
            es = "b.tabla_estado ='2'";
        }

        if (selec == 0) {
            sql = " select * from tpltablas2 b where "
                    + es
                    + " and b.tabla_per='" + periodo + "'"
                    + " and b.tabla_idmes='00'"
                    + " order by b.tabla_id";
        } else if (selec == 1) {
            sql = " select * from tpltablas2 b where "
                    + es
                    + " and b.tabla_per='" + periodo + "'"
                    + " and b.tabla_idmes='00'"
                    + " and b.tabla_id like '" + consulta + "'"
                    + " order by b.tabla_id";

        } else if (selec == 2) {
            sql = " select * from tpltablas2 b where "
                    + es
                    + " and b.tabla_per='" + periodo + "'" 
                    + " and b.tabla_idmes='00'"
                    + " and b.tabla_descri like '" + consulta + "'"
                    + " order by b.tabla_id";
        }
        
        P_WHERE = sql;

        objlstTablas = new ListModelList<ManTablas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTablas = new ManTablas();
                objTablas.setCodigo(rs.getString("tabla_id"));
                objTablas.setDescripcion(rs.getString("tabla_descri"));
                objTablas.setTipo(rs.getString("tabla_tipo"));
                objTablas.setPeriodo(rs.getString("tabla_per"));
                objTablas.setEstado(rs.getInt("tabla_estado"));
                objTablas.setUsu_add(rs.getString("tabla_usuadd"));
                objTablas.setUsu_mod(rs.getString("tabla_usumod"));
                objTablas.setDia_add(rs.getTimestamp("tabla_fecadd"));
                objTablas.setDia_mod(rs.getTimestamp("tabla_fecmod"));
                objlstTablas.add(objTablas);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstTablas;
    }
}
