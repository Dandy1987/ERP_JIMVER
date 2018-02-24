package org.me.gj.controller.contabilidad.mantenimiento;

import org.me.gj.controller.logistica.mantenimiento.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.TipoExistencia;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoTipExistencia {

    TipoExistencia obj_tipexi;
    ListModelList<TipoExistencia> listado;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(DaoUmanejo.class);

    public ListModelList<TipoExistencia> lstTexistencia(int estado) throws SQLException {
        listado = new ListModelList<TipoExistencia>();
        String SQL_TEXISTENCIA = "";
       //SQL_TEXISTENCIA = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_ord,t.tab_est,t.tab_nomrep from ttabgen t where t.tab_cod=17 and t.tab_est <> 0 order by t.tab_id";
           if (estado == 0) {
               SQL_TEXISTENCIA = "select t.tab_id,t.tab_subdes,t.tab_tip,t.tab_est,t.tab_nomrep from ttabgen t where t.tab_cod=17 and t.tab_est <> '"+estado+"'  order by t.tab_id";
           } else {
               SQL_TEXISTENCIA = "select t.tab_id,t.tab_subdes,t.tab_tip,t.tab_est,t.tab_nomrep from ttabgen t where t.tab_cod=17 and t.tab_est = '"+estado+"' order by t.tab_id";
           }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA);
            Boolean b_valor = false;
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                obj_tipexi.setTab_tip(rs.getInt(3));
                obj_tipexi.setTab_est(rs.getInt(4));
                obj_tipexi.setTab_nomrep(rs.getString(5));

                if (rs.getInt(4) == 1) {
                    b_valor = true;
                } else {
                    b_valor = false;
                }
                obj_tipexi.setVal(b_valor);
                listado.add(obj_tipexi);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + listado.getSize() + " registro(s)");
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

        return listado;
    }

    public ListModelList<TipoExistencia> lstTexistenciaVen() throws SQLException {
        String SQL_TEXISTENCIA_VEN = "select t.tab_id,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=17 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA_VEN);
            listado = new ListModelList<TipoExistencia>();
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                listado.add(obj_tipexi);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + listado.getSize() + " registro(s)");
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

        return listado;
    }

    public ListModelList<TipoExistencia> lstEmpIndivVen() throws SQLException {
        String SQL_EMPINDIV_VEN = "select t.tab_id,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=17 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_VEN);
            listado = new ListModelList<TipoExistencia>();
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                listado.add(obj_tipexi);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + listado.getSize() + " registro(s)");
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

        return listado;
    }

    public ListModelList<TipoExistencia> lstEmpIndivComp() throws SQLException {
        String SQL_EMPINDIV_COMP = "select t.tab_id,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=17 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_COMP);
            listado = new ListModelList<TipoExistencia>();
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                listado.add(obj_tipexi);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + listado.getSize() + " registro(s)");
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

        return listado;
    }

    public ListModelList<TipoExistencia> lstTexistenciaComp() throws SQLException {
        String SQL_TEXISTENCIA_COMP = "select t.tab_id,t.tab_subdes, t.tab_tip, t.tab_nomrep,t.tab_est from ttabgen t"
                + "where t.tab_cod=17 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA_COMP);
            listado = new ListModelList<TipoExistencia>();
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                listado.add(obj_tipexi);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + listado.getSize() + " registro(s)");
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

        return listado;
    }

    public String Insert(TipoExistencia ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        int i_tab_tip = ttabGen.getTab_tip();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep();
        s_tab_nomrep = s_tab_nomrep.toUpperCase();
        String SQL_INSERT_PROC_TEXISTENCIA = "{call pack_ttabgen.p_038_insertarTipExSunat(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_TEXISTENCIA);
            cst.clearParameters();
            cst.setString(1, s_tab_subdes);
            cst.setInt(2, i_tab_tip);
            cst.setString(3, s_tab_nomrep);
            cst.setInt(4, i_tab_est);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tab_subdes);
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

    public String Delete(int i_tab_id) throws SQLException {
        String SQL_INSERT_PROC_TEXISTENCIA = "{call pack_ttabgen.p_038_eliminarTipExSunat(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_TEXISTENCIA);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tab_id);
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

    public String Update(TipoExistencia ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        int i_tab_id = ttabGen.getTab_id();
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        int i_tab_tip = ttabGen.getTab_tip();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep().toUpperCase();
        String SQL_UPDATE_PROC_TEXISTENCIA = "{call pack_ttabgen.p_038_actualizarTipExSunat(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_TEXISTENCIA);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.setString(2, s_tab_subdes);
            cst.setInt(3, i_tab_tip);
            cst.setString(4, s_tab_nomrep);
            cst.setInt(5, i_tab_est);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tab_id);
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

    public ListModelList<TipoExistencia> Search(String dato, int indice, int i_estado) throws SQLException {
        ListModelList<TipoExistencia> lista = new ListModelList<TipoExistencia>();
        String SQL_TEXISTENCIA_SEARCH = "";

        if (indice == 1) {
            SQL_TEXISTENCIA_SEARCH = "select t.tab_id,t.tab_subdes, t.tab_tip, t.tab_nomrep,t.tab_est from ttabgen t where t.tab_cod=17 and t.tab_est=" + i_estado + " and t.tab_id like '" + dato + "' ";
        } else if (indice ==2) {
          
            SQL_TEXISTENCIA_SEARCH = "select t.tab_id,t.tab_subdes, t.tab_tip, t.tab_nomrep,t.tab_est from ttabgen t where t.tab_cod=17 and t.tab_est=" + i_estado + " and t.tab_subdes like '" + dato + "'";
          }
        else if (indice ==3) {
            SQL_TEXISTENCIA_SEARCH = "select t.tab_id,t.tab_subdes, t.tab_tip, t.tab_nomrep,t.tab_est from ttabgen t where t.tab_cod=17 and t.tab_est=" + i_estado + " and t.tab_tip like '" + dato + "'";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA_SEARCH);
            Boolean b_valor = false;
            while (rs.next()) {
                obj_tipexi = new TipoExistencia();
                obj_tipexi.setTab_id(rs.getInt(1));
                obj_tipexi.setTab_subdes(rs.getString(2));
                obj_tipexi.setTab_tip(rs.getInt(3));
                obj_tipexi.setTab_nomrep(rs.getString(4));
                obj_tipexi.setTab_est(rs.getInt(5));
                if (rs.getInt(5) == 1) {
                    b_valor = true;
                } else {
                    b_valor = false;
                }
                obj_tipexi.setVal(b_valor);
                lista.add(obj_tipexi);
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
        return lista;
    }

    public int ValidaAbrev(String dato) throws SQLException {
        String SQL_TEXISTENCIA = "";
        int valor = 0;
        SQL_TEXISTENCIA = "select nvl(to_number(count(*)),0) from codijisa.ttabgen t where t.tab_cod='17' and  t.tab_tip='" + dato + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validacion de Abreviatura  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
        return valor;
    }
    
    public int ValidaRelacion(String dato) throws SQLException {
        String SQL_TEXISTENCIA = "";
        int valor = 0;
        SQL_TEXISTENCIA = "select nvl(to_number(count(*)),0) from codijisa.tproductos t where t.pro_unimancom = '"+dato+"'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TEXISTENCIA);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validacion de Abreviatura  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
        return valor;
    }
}
