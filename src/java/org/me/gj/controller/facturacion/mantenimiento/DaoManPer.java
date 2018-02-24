package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoManPer {

    //Instancias de Objetos
    ListModelList<ManPer> objLisManPer;
    ManPer objManPer;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManPer.class);

    //Eventos Primarios - Transaccionales
    public String insertar(ManPer objPeriodos) throws Exception {
        String c_fecha_inicial = objPeriodos.getFecini();
        String c_fecha_final = objPeriodos.getFecfin();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String SQL_INSERT_PROC_PERIODOS = "{call pack_tperiodos.p_insertarPeriodo(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_PERIODOS);
            cst.clearParameters();
            cst.setDate(1, new java.sql.Date(sdf.parse(c_fecha_inicial).getTime()));
            cst.setDate(2, new java.sql.Date(sdf.parse(c_fecha_final).getTime()));
            cst.setString(3, objUsuCredential.getCuenta());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro para periodos ");
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

    public String actualizar(ManPer objPeriodos) throws Exception {
        String c_per_id = objPeriodos.getPer_id();
        int i_estado = objPeriodos.getPer_estado();
        String c_fecha_inicial = objPeriodos.getFecini();
        String c_fecha_final = objPeriodos.getFecfin();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        /*String s_desLin = motivoCambio.getTab_subdes();
         int i_ordLin = motivoCambio.getTab_ord();
         int i_estLin = motivoCambio.getTab_est();
         String s_ordNomRep = motivoCambio.getTab_nomrep();*/
        String SQL_UPDATE_PROC_PERIODOS = "{call pack_tperiodos.p_actualizarPeriodo(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_PERIODOS);
            cst.clearParameters();
            cst.setString(1, c_per_id);
            cst.setInt(2, i_estado);
            cst.setDate(3, new java.sql.Date(sdf.parse(c_fecha_inicial).getTime()));
            cst.setDate(4, new java.sql.Date(sdf.parse(c_fecha_final).getTime()));
            cst.setString(5, objUsuCredential.getCuenta());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + c_per_id);
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

    public String eliminar(ManPer objPeriodos) throws SQLException {
        String c_per_id = objPeriodos.getPer_id();
        String SQL_DELETE_PROC_PERIODOS = "{call pack_tperiodos.p_eliminarPeriodo(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_PERIODOS);
            cst.clearParameters();
            cst.setInt(1, Integer.parseInt(c_per_id));
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + c_per_id);
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
    public ListModelList<ManPer> listaPeriodos(int caso) throws SQLException {
        String sql;
        if (caso == 0) {
            //  sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado <> " + caso + " order by t.per_id desc";
            sql = "select r.per_key , r.per_id , r.per_anio,r.per_mes , r.per_fecini , r.per_fecfin , r.per_estado from (select t.per_key , t.per_id , t.per_anio, t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado <> 0 union select null , null , null, null , null ,null ,null from tperiodos t where t.per_estado <> 0) r order by r.per_id desc";
        } else {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + caso + " order by t.per_id desc";
        }
        try {
            objLisManPer = new ListModelList<ManPer>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                objManPer = new ManPer();
                objManPer.setPer_id(rs.getString(1));
                objManPer.setPer_periodo(rs.getString(2));
                objManPer.setAnio(rs.getString(3));
                objManPer.setMes(rs.getString(4));
                objManPer.setFecini(sdf.format(rs.getDate(5)));
                objManPer.setFecfin(sdf.format(rs.getDate(6)));
                objManPer.setPer_estado(rs.getInt(7));
                objLisManPer.add(objManPer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisManPer.getSize() + " registro(s)");
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
        return objLisManPer;
    }

    //periodo con campo en blanco
    public ListModelList<ManPer> listaPeriodosDinamico(int caso) throws SQLException {
        String sql;
        if (caso == 0) {
            //  sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado <> " + caso + " order by t.per_id desc";
            sql = "select r.per_id  from (select  t.per_id  from tperiodos t where t.per_estado <> " + caso + " union select null  from tperiodos t where t.per_estado <> " + caso + ") r order by r.per_id desc";
        } else {
            //sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + caso + " order by t.per_id desc";
            sql = "select r.per_id  from (select  t.per_id  from tperiodos t where t.per_estado = " + caso + " union select null  from tperiodos t where t.per_estado = " + caso + ") r order by r.per_id desc";
        }
        try {
            objLisManPer = new ListModelList<ManPer>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                objManPer = new ManPer();
                objManPer.setPer_id(rs.getString("per_id"));
                objLisManPer.add(objManPer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisManPer.getSize() + " registro(s)");
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
        return objLisManPer;
    }

    public ListModelList<ManPer> listaPeriodosActual(int caso, int meses) throws SQLException {
        String sql;
        if (caso == 0) {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado <> " + caso + " and t.per_id between to_char(add_months(sysdate, " + meses + "*-1),'yyyymm') and to_char(sysdate,'yyyymm') order by t.per_id desc";
        } else {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + caso + " and t.per_id between to_char(add_months(sysdate, " + meses + "*-1),'yyyymm') and to_char(sysdate,'yyyymm') order by t.per_id desc";
        }
        try {
            objLisManPer = new ListModelList<ManPer>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                objManPer = new ManPer();
                objManPer.setPer_id(rs.getString(1));
                objManPer.setPer_periodo(rs.getString(2));
                objManPer.setAnio(rs.getString(3));
                objManPer.setMes(rs.getString(4));
                objManPer.setFecini(sdf.format(rs.getDate(5)));
                objManPer.setFecfin(sdf.format(rs.getDate(6)));
                objManPer.setPer_estado(rs.getInt(7));
                objLisManPer.add(objManPer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisManPer.getSize() + " registro(s)");
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
        return objLisManPer;
    }

    public ListModelList<ManPer> listaAnios(int caso) throws SQLException {
        String sql;
        if (caso == 0) {
            sql = "select distinct t.per_anio from tperiodos t where t.per_estado <> " + caso + " order by t.per_anio desc";
        } else {
            sql = "select distinct t.per_anio from tperiodos t where t.per_estado = " + caso + " order by t.per_anio desc";
        }
        try {
            objLisManPer = new ListModelList<ManPer>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objManPer = new ManPer();
                objManPer.setAnio(rs.getString(1));
                objLisManPer.add(objManPer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisManPer.getSize() + " registro(s)");
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
        return objLisManPer;
    }

    public ListModelList<ManPer> busquedaPeriodo(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (seleccion == 1) {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + i_estado + " and t.per_key like '" + consulta + "' order by t.per_id desc";
        } else if (seleccion == 2) {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + i_estado + " and t.per_id like '" + consulta + "' order by t.per_id desc";
        } else if (seleccion == 3) {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + i_estado + " and t.per_anio like '" + consulta + "' order by t.per_id desc";
        } else {
            sql = "select t.per_key , t.per_id , t.per_anio , t.per_mes , t.per_fecini , t.per_fecfin , t.per_estado from tperiodos t where t.per_estado = " + i_estado + " and t.per_mes like '" + consulta + "' order by t.per_id desc";
        }
        try {
            objLisManPer = new ListModelList<ManPer>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                objManPer = new ManPer();
                objManPer.setPer_id(rs.getString(1));
                objManPer.setPer_periodo(rs.getString(2));
                objManPer.setAnio(rs.getString(3));
                objManPer.setMes(rs.getString(4));
                objManPer.setFecini(sdf.format(rs.getDate(5)));
                objManPer.setFecfin(sdf.format(rs.getDate(6)));
                objManPer.setPer_estado(rs.getInt(7));
                objLisManPer.add(objManPer);
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
        return objLisManPer;
    }
}
