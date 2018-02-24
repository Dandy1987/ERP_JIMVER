package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.MotCam;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoMotivoCambio {

    //Instancias de Objetos
    ListModelList<MotCam> objLisMotCam;
    MotCam objMotivoCambio;
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
    private static final Logger LOGGER = Logger.getLogger(DaoMotivoCambio.class);

    //Eventos Primarios - Transaccionales
    public String insertar(MotCam motivoCambio) throws SQLException {
        String s_desLin = motivoCambio.getTab_subdes();
        int i_ordLin = motivoCambio.getTab_ord();
        String s_ordNomRep = motivoCambio.getTab_nomrep();
        String s_usuadd = motivoCambio.getTab_usuadd();
        String SQL_INSERT_PROC_LINEAS = "{call pack_ttabgen.p_021_insertarMotivoCambio(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_LINEAS);
            cst.clearParameters();
            cst.setString(1, s_desLin);
            cst.setInt(2, i_ordLin);
            cst.setString(3, s_ordNomRep);
            cst.setString(4, s_usuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_desLin);
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

    public String actualizar(MotCam motivoCambio) throws SQLException {
        int i_idLin = motivoCambio.getTab_id();
        String s_desLin = motivoCambio.getTab_subdes();
        int i_ordLin = motivoCambio.getTab_ord();
        int i_estLin = motivoCambio.getTab_est();
        String s_ordNomRep = motivoCambio.getTab_nomrep();
        String s_usumod = motivoCambio.getTab_usumod();
        String SQL_UPDATE_PROC_LINEAS = "{call pack_ttabgen.p_021_actualizarMotivoCambio(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_LINEAS);
            cst.clearParameters();
            cst.setInt(1, i_idLin);
            cst.setString(2, s_desLin);
            cst.setInt(3, i_ordLin);
            cst.setInt(4, i_estLin);
            cst.setString(5, s_ordNomRep);
            cst.setString(6, s_usumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_idLin);
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

    public String eliminar(MotCam motivoCambio) throws SQLException {
        int i_idLin = motivoCambio.getTab_id();
        String SQL_DELETE_PROC_LINEAS = "{call pack_ttabgen.p_021_eliminarMotivoCambio(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_LINEAS);
            cst.clearParameters();
            cst.setInt(1, i_idLin);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_idLin);
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
    public ListModelList<MotCam> listaMotivosCambio(int caso) throws SQLException {
        String sql;
        if (caso == 0) {
            sql = "select t.tab_id,t.tab_subdes, t.tab_est, tab_nomrep, t.tab_ord,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod "
                    + "from ttabgen t where t.tab_cod=21 and t.tab_id<>0 and tab_est<>" + caso + " order by t.tab_id";
        } else {
            sql = "select t.tab_id,t.tab_subdes, t.tab_est, tab_nomrep, t.tab_ord,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod "
                    + "from ttabgen t where t.tab_cod=21 and t.tab_id<>0 and tab_est=" + caso + " order by t.tab_id";
        }
        P_WHERE = sql;
        try {
            objLisMotCam = new ListModelList<MotCam>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objMotivoCambio = new MotCam();
                objMotivoCambio.setTab_id(rs.getInt(1));
                objMotivoCambio.setTab_subdes(rs.getString(2));
                objMotivoCambio.setTab_est(rs.getInt(3));
                objMotivoCambio.setTab_nomrep(rs.getString(4));
                objMotivoCambio.setTab_ord(rs.getInt(5));
                objMotivoCambio.setTab_subdir(rs.getString(6));
                objMotivoCambio.setTab_usuadd(rs.getString(7));
                objMotivoCambio.setTab_fecadd(rs.getTimestamp(8));
                objMotivoCambio.setTab_usumod(rs.getString(9));
                objMotivoCambio.setTab_fecmod(rs.getTimestamp(10));
                objLisMotCam.add(objMotivoCambio);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisMotCam.getSize() + " registro(s)");
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
        return objLisMotCam;
    }

    public ListModelList<MotCam> busquedaMotivo(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est<>0  order by p.tab_id,p.tab_ord";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdir like '" + consulta + "' order by p.tab_id,p.tab_ord";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdes like '" + consulta + "' order by p.tab_id,p.tab_ord";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id,p.tab_ord";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdir like '" + consulta + "' order by p.tab_id,p.tab_ord";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_subdir,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=21 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id,p.tab_ord";
                }
            }
        }
        P_WHERE = sql;
        try {
            objLisMotCam = new ListModelList<MotCam>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objMotivoCambio = new MotCam();
                objMotivoCambio.setTab_id(rs.getInt(1));
                objMotivoCambio.setTab_subdes(rs.getString(2));
                objMotivoCambio.setTab_est(rs.getInt(3));
                objMotivoCambio.setTab_nomrep(rs.getString(4));
                objMotivoCambio.setTab_ord(rs.getInt(5));
                objMotivoCambio.setTab_subdir(rs.getString(6));
                objMotivoCambio.setTab_usuadd(rs.getString(7));
                objMotivoCambio.setTab_fecadd(rs.getTimestamp(8));
                objMotivoCambio.setTab_usumod(rs.getString(9));
                objMotivoCambio.setTab_fecmod(rs.getTimestamp(10));
                objLisMotCam.add(objMotivoCambio);
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
        return objLisMotCam;
    }

    public MotCam getNomMotivoCambio(String mot_id) throws SQLException {
        String SQL_BUSQUEDA = "select t.tab_id,t.tab_subdir,t.tab_subdes  from ttabgen t where t.tab_cod='21' and t.tab_est<>0 and t.tab_id = " + mot_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objMotivoCambio = null;
            while (rs.next()) {
                objMotivoCambio = new MotCam();
                objMotivoCambio.setTab_id(rs.getInt(1));
                objMotivoCambio.setTab_subdir(rs.getString(2));
                objMotivoCambio.setTab_subdes(rs.getString(3));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objMotivoCambio;
    }

    public String validaMovimientos(MotCam objMotCam) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientosmotcam(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objMotCam.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del Motivo de Cambio " + objMotCam.getTab_subdir());
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
}
