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
import org.me.gj.model.planillas.mantenimiento.ManDatForm;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.A;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoManDatForm {

    ListModelList<ManDatForm> objlstdatDatForm;
    ManDatForm objDatForm;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    public static String P_WHERE = "";

    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManDatForm.class);

    public ListModelList<ManDatForm> lstDatForm() throws SQLException {
        String sql_query;
        sql_query = " SELECT * "
                + " FROM tpldatfor t "
                + " where t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                + " and t.pldatfor_estado =1"
                + " and t.pldatfor_idt = '100'"
                + " order by t.pldatfor_id";

        P_WHERE = sql_query;

        //"select * from codijisa.tbancos t where t.ban_estado=" + estado + " order by t.ban_key";
        objlstdatDatForm = new ListModelList<ManDatForm>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            while (rs.next()) {
                objDatForm = new ManDatForm();
                objDatForm.setCodigo(rs.getString("pldatfor_id"));
                objDatForm.setView(rs.getString("pldatfor_dview"));
                objDatForm.setRecord(rs.getString("pldatfor_dform"));
                objDatForm.setEstado(rs.getInt("pldatfor_estado"));
                objDatForm.setDia_add(rs.getTimestamp("pldatfor_fecadd"));
                objDatForm.setDia_mod(rs.getTimestamp("pldatfor_fecmod"));
                objDatForm.setUsu_add(rs.getString("pldatfor_usuadd"));
                objDatForm.setUsu_mod(rs.getString("pldatfor_usumod"));

                objlstdatDatForm.add(objDatForm);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstdatDatForm.getSize() + " registro(s)");
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
        return objlstdatDatForm;
    }

    public String insertar002(ManDatForm objDatForm) throws SQLException {
        String view = objDatForm.getView();
        String grabar = objDatForm.getRecord();
        String query = "{call pack_tpldatfor.p_insertar002(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, view);
            cst.setString(4, grabar);
            cst.setString(5, objUsuCredential.getCuenta());
            cst.setString(6, objUsuCredential.getComputerName());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String insertar001(ManDatForm objDatForm) throws SQLException {
        String view = objDatForm.getView();
        String grabar = objDatForm.getRecord();
        String sql_insertar = "{call pack_tpldatfor.p_insertar001(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, view);
            cst.setString(4, grabar);
            cst.setString(5, objUsuCredential.getCuenta());
            cst.setString(6, objUsuCredential.getComputerName());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String insertar(ManDatForm objDatForm) throws SQLException {
        String view = objDatForm.getView();
        String grabar = objDatForm.getRecord();

        String sql_insertar = "{call pack_tpldatfor.p_insertar(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, view);
            cst.setString(4, grabar);
            cst.setString(5, objUsuCredential.getCuenta());
            cst.setString(6, objUsuCredential.getComputerName());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String actualizar(ManDatForm objDatform) throws SQLException {
        String key = objDatform.getCodigo();
        int estado = objDatform.getEstado();
        String view = objDatform.getView();
        String grabar = objDatform.getRecord();
        String sql_actualizar = "{call pack_tpldatfor.p_modificar(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, key);
            cst.setString(4, view);
            cst.setString(5, grabar);
            cst.setInt(6, estado);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setString(8, objUsuCredential.getComputerName());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String actualizar001(ManDatForm objDatform) throws SQLException {
        String key = objDatform.getCodigo();
        int estado = objDatform.getEstado();
        String view = objDatform.getView();
        String grabar = objDatform.getRecord();
        String sql_actualizar = "{call pack_tpldatfor.p_modificar001(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, key);
            cst.setString(4, view);
            cst.setString(5, grabar);
            cst.setInt(6, estado);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setString(8, objUsuCredential.getComputerName());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String actualizar002(ManDatForm objDatform) throws SQLException {
        String key = objDatform.getCodigo();
        int estado = objDatform.getEstado();
        String view = objDatform.getView();
        String grabar = objDatform.getRecord();
        String sql_actualizar = "{call pack_tpldatfor.p_modificar002(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_actualizar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, key);
            cst.setString(4, view);
            cst.setString(5, grabar);
            cst.setInt(6, estado);
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setString(8, objUsuCredential.getComputerName());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + view);
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

    public String eliminar(ManDatForm objDatForm) throws SQLException {
        String codigo = objDatForm.getCodigo();
        String sql_eliminar = "{call pack_tpldatfor.p_eliminar(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, codigo);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objDatForm.getCodigo());
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

    public ListModelList<ManDatForm> consultar(String consulta, int estado) throws SQLException {
        String key = consulta.isEmpty() ? "" : " and t.pldatfor_id ='" + consulta + "'";
        String sql = "";
        String es;
        if (estado == 3) {
            es = " and t.pldatfor_estado in ('1','2')";
        } else if (estado == 1) {
            es = " and t.pldatfor_estado ='1'";
        } else {
            es = " and t.pldatfor_estado ='2'";
        }

        sql = "select * from tpldatfor t"
                + " where  t.pldatfor_idt ='100'"
                + " and t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                + key
                //+ " and t.pldatfor_id ='" + consulta + "'"
                + es;

        P_WHERE = sql;

        objlstdatDatForm = new ListModelList<ManDatForm>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objDatForm = new ManDatForm();
                objDatForm.setCodigo(rs.getString("pldatfor_id"));
                objDatForm.setRecord(rs.getString("pldatfor_dform"));
                objDatForm.setView(rs.getString("pldatfor_dview"));
                objDatForm.setEstado(rs.getInt("pldatfor_estado"));
                objDatForm.setDia_add(rs.getTimestamp("pldatfor_fecadd"));
                objDatForm.setDia_mod(rs.getTimestamp("pldatfor_fecmod"));
                objDatForm.setUsu_add(rs.getString("pldatfor_usuadd"));
                objDatForm.setUsu_mod(rs.getString("pldatfor_usumod"));
                objlstdatDatForm.add(objDatForm);
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
        return objlstdatDatForm;
    }

    public ListModelList<ManDatForm> consultarVariable(String consulta, int estado, int tipo) throws SQLException {

        String stipo = tipo == 0 ? " and t.pldatfor_idt ='001'" : " and t.pldatfor_idt ='002'";

        String key = consulta.isEmpty() ? "" : " and t.pldatfor_id ='" + consulta + "'";
        String sql = "";
        String es;
        if (estado == 3) {
            es = " and t.pldatfor_estado in ('1','2')";
        } else if (estado == 1) {
            es = " and t.pldatfor_estado ='1'";
        } else {
            es = " and t.pldatfor_estado ='2'";
        }

        sql = "select * from tpldatfor t"
                + " where "
                + " t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'" + stipo
                + key
                //+ " and t.pldatfor_id ='" + consulta + "'"
                + es + " order by t.pldatfor_id";

        P_WHERE = sql;

        objlstdatDatForm = new ListModelList<ManDatForm>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objDatForm = new ManDatForm();
                objDatForm.setCodigo(rs.getString("pldatfor_id"));
                objDatForm.setRecord(rs.getString("pldatfor_dform"));
                objDatForm.setView(rs.getString("pldatfor_dview"));
                objDatForm.setEstado(rs.getInt("pldatfor_estado"));
                objDatForm.setDia_add(rs.getTimestamp("pldatfor_fecadd"));
                objDatForm.setDia_mod(rs.getTimestamp("pldatfor_fecmod"));
                objDatForm.setUsu_add(rs.getString("pldatfor_usuadd"));
                objDatForm.setUsu_mod(rs.getString("pldatfor_usumod"));
                objDatForm.setIdentificador(rs.getString("pldatfor_idt"));
                objlstdatDatForm.add(objDatForm);
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
        return objlstdatDatForm;
    }

    public ListModelList<ManDatForm> lstDatForm001() throws SQLException {
        String sql_query;
        sql_query = " SELECT * "
                + " FROM tpldatfor t "
                + " where t.emp_id ='" + objUsuCredential.getCodemp() + "'"
                + " and t.suc_id ='" + objUsuCredential.getCodsuc() + "'"
                + " and t.pldatfor_estado =1"
                + " and t.pldatfor_idt in ('001','002')"
                + " order by t.pldatfor_id";

        P_WHERE = sql_query;

        //"select * from codijisa.tbancos t where t.ban_estado=" + estado + " order by t.ban_key";
        objlstdatDatForm = new ListModelList<ManDatForm>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            while (rs.next()) {
                objDatForm = new ManDatForm();
                objDatForm.setCodigo(rs.getString("pldatfor_id"));
                objDatForm.setView(rs.getString("pldatfor_dview"));
                objDatForm.setRecord(rs.getString("pldatfor_dform"));
                objDatForm.setEstado(rs.getInt("pldatfor_estado"));
                objDatForm.setDia_add(rs.getTimestamp("pldatfor_fecadd"));
                objDatForm.setDia_mod(rs.getTimestamp("pldatfor_fecmod"));
                objDatForm.setUsu_add(rs.getString("pldatfor_usuadd"));
                objDatForm.setUsu_mod(rs.getString("pldatfor_usumod"));
                objDatForm.setIdentificador(rs.getString("pldatfor_idt"));
                objlstdatDatForm.add(objDatForm);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstdatDatForm.getSize() + " registro(s)");
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
        return objlstdatDatForm;
    }
    
    public String eliminar001(ManDatForm objDatForm) throws SQLException {
        String codigo = objDatForm.getCodigo();
        String sql_eliminar = "{call pack_tpldatfor.p_eliminar001(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, codigo);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objDatForm.getCodigo());
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
    
    public String eliminar002(ManDatForm objDatForm) throws SQLException {
        String codigo = objDatForm.getCodigo();
        String sql_eliminar = "{call pack_tpldatfor.p_eliminar002(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, codigo);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objDatForm.getCodigo());
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

}
