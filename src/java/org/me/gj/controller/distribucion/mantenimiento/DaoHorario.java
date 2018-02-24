package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoHorario {

    //Instancia de Objetos
    ListModelList<Horario> objlstHorarios;
    Horario objHorario;
    //Variables Publicas
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(DaoHorario.class);

    //Eventos Primarios - Transaccionales
    public String insertarHorario(Horario objHorario) throws SQLException {
        String s_tabsubdes = objHorario.getTab_subdes();
        String s_tabnomrep = objHorario.getTab_nomrep();
        int i_tabord = objHorario.getTab_ord();
        String SQL_INSERTAR_HORARIO = "{call pack_ttabgen.p_022_insertarHorarioEntrega(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_HORARIO);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setInt(2, i_tabord);
            cst.setString(3, s_tabnomrep);
            cst.setString(4, objHorario.getTab_usuadd());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tabsubdes);
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

    public String actualizarHorario(Horario objHorario) throws SQLException {
        int s_tabid = objHorario.getTab_id();
        String s_tabsubdes = objHorario.getTab_subdes();
        String s_tabnomrep = objHorario.getTab_nomrep();
        int i_tabord = objHorario.getTab_ord();
        int i_tabest = objHorario.getTab_est();
        String SQL_ACTUALIZAR_HORARIO = "{call pack_ttabgen.p_022_actualizarHorarioEntrega(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_HORARIO);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setInt(3, i_tabord);
            cst.setInt(4, i_tabest);
            cst.setString(5, s_tabnomrep);
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + s_tabid);
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

    public String eliminarHorario(Horario objHorario) throws SQLException {
        int s_tabid = objHorario.getTab_id();
        String SQL_ELIMINAR_HORARIO = "{call pack_ttabgen.p_022_eliminarHorarioEntrega(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_HORARIO);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + s_tabid);
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Horario> listaHorario(int i_caso) throws SQLException {
        String SQL_POSTALES;
        if (i_caso == 0) {
            SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=22 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=22 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_POSTALES;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_POSTALES);
            objlstHorarios = new ListModelList<Horario>();
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt("tab_id"));
                objHorario.setTab_subdes(rs.getString("tab_subdes"));
                objHorario.setTab_subdir(rs.getString("tab_subdir"));
                objHorario.setTab_est(rs.getInt("tab_est"));
                objHorario.setTab_ord(rs.getInt("tab_ord"));
                objHorario.setTab_nomrep(rs.getString("tab_nomrep"));
                objHorario.setTab_usuadd(rs.getString("tab_usuadd"));
                objHorario.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objHorario.setTab_usumod(rs.getString("tab_usumod"));
                objHorario.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstHorarios.add(objHorario);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstHorarios.getSize() + " registro(s)");
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
        return objlstHorarios;
    }

    public ListModelList<Horario> BusqueHorarioxZona(String zon_id) throws SQLException {
        String SQL_HORARIOSXZONA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord from ttabgen t, tprogramacion_zonas p "
                + "where t.tab_cod=22 and p.prog_horentid=t.tab_id and t.tab_est=1 and p.prog_est=1 and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.prog_zonid='" + zon_id + "'";
        objlstHorarios = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_HORARIOSXZONA);
            objHorario = null;
            objlstHorarios = new ListModelList<Horario>();
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdes(rs.getString(2));
                objHorario.setTab_subdir(rs.getString(3));
                objHorario.setTab_est(rs.getInt(4));
                objHorario.setTab_nomrep(rs.getString(5));
                objHorario.setTab_ord(rs.getInt(6));
                objlstHorarios.add(objHorario);
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

        return objlstHorarios;
    }

    public ListModelList<Horario> busquedaHorario(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_POSTALES;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdir like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0  and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0 and p.tab_est=" + i_estado + "  order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdir like '" + consulta + "' order by p.tab_id";
            } else {
                SQL_POSTALES = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord, p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=22 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        }
        P_WHERE = SQL_POSTALES;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_POSTALES);
            objlstHorarios = new ListModelList<Horario>();
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt("tab_id"));
                objHorario.setTab_subdes(rs.getString("tab_subdes"));
                objHorario.setTab_subdir(rs.getString("tab_subdir"));
                objHorario.setTab_est(rs.getInt("tab_est"));
                objHorario.setTab_ord(rs.getInt("tab_ord"));
                objHorario.setTab_nomrep(rs.getString("tab_nomrep"));
                objHorario.setTab_usuadd(rs.getString("tab_usuadd"));
                objHorario.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objHorario.setTab_usumod(rs.getString("tab_usumod"));
                objHorario.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstHorarios.add(objHorario);
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
        return objlstHorarios;
    }

    public Horario buscaHorarioxCodigo(String s_codigo) throws SQLException {
        objHorario = null;
        String SQL_BUSCAHORARIO = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_nomrep,p.tab_ord from ttabgen p "
                + "where p.tab_cod=22 and p.tab_id<>0 and p.tab_est=1 and p.tab_subdir = lpad('" + s_codigo + "',3,'0') order by p.tab_ord,p.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAHORARIO);
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdes(rs.getString(2));
                objHorario.setTab_subdir(rs.getString(3));
                objHorario.setTab_est(rs.getInt(4));
                objHorario.setTab_nomrep(rs.getString(5));
                objHorario.setTab_ord(rs.getInt(6));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objHorario.getTab_id() + "");
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
        return objHorario;
    }

    public Horario getNomHorario(String hor_id) throws SQLException {
        String SQL_BUSQUEDA = " select p.tab_id,p.tab_subdir,p.tab_subdes from ttabgen p where p.tab_est=1 and p.tab_cod='22' and p.tab_subdir =  " + hor_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objHorario = null;
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdir(rs.getString(2));
                objHorario.setTab_subdes(rs.getString(3));

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
        return objHorario;
    }

    public Horario getHorarioxZona(String zon_id, int hor_id) throws SQLException {
        String SQL_HORARIOSXZONA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord from ttabgen t, tprogramacion_zonas p "
                + "where t.tab_cod=22 and p.prog_horentid=t.tab_id and t.tab_est=1 and p.prog_est=1 and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.prog_zonid='" + zon_id + "' and t.tab_id=" + hor_id;
        objHorario = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_HORARIOSXZONA);
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdes(rs.getString(2));
                objHorario.setTab_subdir(rs.getString(3));
                objHorario.setTab_est(rs.getInt(4));
                objHorario.setTab_nomrep(rs.getString(5));
                objHorario.setTab_ord(rs.getInt(6));
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

        return objHorario;
    }
    
    public Horario getHorarioxZona(String diavisita ,String zon_id, int hor_id) throws SQLException {
        String SQL_HORARIOSXZONA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord from ttabgen t, tprogramacion_zonas p ,v_listazonas z "
                + "where t.tab_cod=22 and p.prog_horentid=t.tab_id and t.tab_est=1 and p.prog_est=1 and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.prog_zonid='" + zon_id + "' and t.tab_id=" + hor_id +"  and z.zon_dvis = "+diavisita;
        objHorario = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_HORARIOSXZONA);
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdes(rs.getString(2));
                objHorario.setTab_subdir(rs.getString(3));
                objHorario.setTab_est(rs.getInt(4));
                objHorario.setTab_nomrep(rs.getString(5));
                objHorario.setTab_ord(rs.getInt(6));
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

        return objHorario;
    }
    
    public Horario getHorarioxZona(String zon_id) throws SQLException {
        String SQL_HORARIOSXZONA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord from ttabgen t, tprogramacion_zonas p "
                + "where t.tab_cod=22 and p.prog_horentid=t.tab_id and t.tab_est=1 and p.prog_est=1 and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.prog_zonid='" + zon_id + "' ";
        objHorario = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_HORARIOSXZONA);
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt(1));
                objHorario.setTab_subdes(rs.getString(2));
                objHorario.setTab_subdir(rs.getString(3));
                objHorario.setTab_est(rs.getInt(4));
                objHorario.setTab_nomrep(rs.getString(5));
                objHorario.setTab_ord(rs.getInt(6));
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

        return objHorario;
    }
    
    public ListModelList<Horario> BusquedaHorarioxZona(String zon_id, String hor_id,int estado) throws SQLException {
        String SQL_HORARIOSXZONA = "select t.* from ttabgen t, tprogramacion_zonas p "
                + "where t.tab_cod=22 and p.prog_horentid=t.tab_id and t.tab_est="+estado+" and p.prog_est=1 and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.prog_zonid='" + zon_id + "' and t.tab_subdir like '" + hor_id+"' ";
        
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_HORARIOSXZONA);
            objlstHorarios = new ListModelList<Horario>();
            while (rs.next()) {
                objHorario = new Horario();
                objHorario.setTab_id(rs.getInt("tab_id"));
                objHorario.setTab_subdes(rs.getString("tab_subdes"));
                objHorario.setTab_subdir(rs.getString("tab_subdir"));
                objHorario.setTab_est(rs.getInt("tab_est"));
                objHorario.setTab_ord(rs.getInt("tab_ord"));
                objHorario.setTab_nomrep(rs.getString("tab_nomrep"));
                objHorario.setTab_usuadd(rs.getString("tab_usuadd"));
                objHorario.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objHorario.setTab_usumod(rs.getString("tab_usumod"));
                objHorario.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstHorarios.add(objHorario);
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

        return objlstHorarios;
    }
}
