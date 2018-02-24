package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoMotivoRechazo {

    //Instancias de Objetos
    MotivoRechazo objMotivoRechazo;
    ListModelList<MotivoRechazo> objlstMotivoRechazo;
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
    private static final Logger LOGGER = Logger.getLogger(DaoMotivoRechazo.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(MotivoRechazo objMotivoRechazo) throws SQLException {
        cst = null;
        s_msg = "";
        i_flagErrorBD = 0;
        String SQL_INSERTAR_MOTIVORECHAZO = "{call pack_tmotrechazo.p_insertarMotivoRechazo(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MOTIVORECHAZO);
            cst.clearParameters();
            cst.setString(1, objMotivoRechazo.getMrec_des());
            cst.setString(2, objMotivoRechazo.getMrec_tipdoc());
            cst.setString(3, objMotivoRechazo.getMrec_tipo());
            cst.setInt(4, objMotivoRechazo.getMrec_ord());
            cst.setString(5, objMotivoRechazo.getMrec_nomrep());
            cst.setString(6, objMotivoRechazo.getMrec_usuadd());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Motivo de Rechazo con Descripcion " + objMotivoRechazo.getMrec_des());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida modificar(MotivoRechazo objMotivoRechazo) throws SQLException {
        cst = null;
        s_msg = "";
        i_flagErrorBD = 0;
        String SQL_MODIFICAR_MOTIVORECHAZO = "{call pack_tmotrechazo.p_actualizarMotivoRechazo(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_MOTIVORECHAZO);
            cst.clearParameters();
            cst.setInt(1, objMotivoRechazo.getMrec_key());
            cst.setString(2, objMotivoRechazo.getMrec_id());
            cst.setString(3, objMotivoRechazo.getMrec_des());
            cst.setString(4, objMotivoRechazo.getMrec_tipdoc());
            cst.setString(5, objMotivoRechazo.getMrec_tipo());
            cst.setInt(6, objMotivoRechazo.getMrec_est());
            cst.setInt(7, objMotivoRechazo.getMrec_ord());
            cst.setString(8, objMotivoRechazo.getMrec_nomrep());
            cst.setString(9, objMotivoRechazo.getMrec_usuadd());
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro de Motivo de Rechazo con Codigo " + objMotivoRechazo.getMrec_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminar(MotivoRechazo objMotivoRechazo) throws SQLException {
        cst = null;
        s_msg = "";
        i_flagErrorBD = 0;
        String SQL_ELIMINAR_MOTIVORECHAZO = "{call pack_tmotrechazo.p_eliminarMotivoRechazo(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_MOTIVORECHAZO);
            cst.clearParameters();
            cst.setInt(1, objMotivoRechazo.getMrec_key());
            cst.setString(2, objMotivoRechazo.getMrec_id());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro de Motivo de Rechazo con Codigo " + objMotivoRechazo.getMrec_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<MotivoRechazo> listaMotivoRechazo(int i_caso) throws SQLException {
        String SQLLISTAMOTIVORECHAZO;
        if (i_caso == 0) {
            SQLLISTAMOTIVORECHAZO = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM',CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                    + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                    + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>" + i_caso + " order by t.mrec_key";
        } else {
            SQLLISTAMOTIVORECHAZO = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                    + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                    + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_caso + " order by t.mrec_key";
        }
        P_WHERE = SQLLISTAMOTIVORECHAZO;
        objlstMotivoRechazo = new ListModelList<MotivoRechazo>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQLLISTAMOTIVORECHAZO);
            while (rs.next()) {
                objMotivoRechazo = new MotivoRechazo();
                objMotivoRechazo.setMrec_key(rs.getInt("mrec_key"));
                objMotivoRechazo.setMrec_id(rs.getString("mrec_id"));
                objMotivoRechazo.setMrec_des(rs.getString("mrec_des"));
                objMotivoRechazo.setMrec_tipdoc(rs.getString("mrec_tipdoc"));
                objMotivoRechazo.setMrec_tipo(rs.getString("mrec_tipo"));
                objMotivoRechazo.setMrec_est(rs.getInt("mrec_est"));
                objMotivoRechazo.setMrec_ord(rs.getInt("mrec_ord"));
                objMotivoRechazo.setMrec_nomrep(rs.getString("mrec_nomrep"));
                objMotivoRechazo.setMrec_usuadd(rs.getString("mrec_usuadd"));
                objMotivoRechazo.setMrec_fecadd(rs.getTimestamp("mrec_fecadd"));
                objMotivoRechazo.setMrec_usumod(rs.getString("mrec_usumod"));
                objMotivoRechazo.setMrec_fecmod(rs.getTimestamp("mrec_fecmod"));
                objlstMotivoRechazo.add(objMotivoRechazo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMotivoRechazo.getSize() + " registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMotivoRechazo;
    }

    public ListModelList<MotivoRechazo> listaMotivoRecxTipo(String s_tipdoc) throws SQLException {
        String SQLLISTAMOTIVORECHAZO = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=1 and t.mrec_tipdoc like '" + s_tipdoc + "' order by t.mrec_key";
        P_WHERE = SQLLISTAMOTIVORECHAZO;
        objlstMotivoRechazo = new ListModelList<MotivoRechazo>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQLLISTAMOTIVORECHAZO);
            while (rs.next()) {
                objMotivoRechazo = new MotivoRechazo();
                objMotivoRechazo.setMrec_key(rs.getInt("mrec_key"));
                objMotivoRechazo.setMrec_id(rs.getString("mrec_id"));
                objMotivoRechazo.setMrec_des(rs.getString("mrec_des"));
                objMotivoRechazo.setMrec_tipdoc(rs.getString("mrec_tipdoc"));
                objMotivoRechazo.setMrec_tipo(rs.getString("mrec_tipo"));
                objMotivoRechazo.setMrec_est(rs.getInt("mrec_est"));
                objMotivoRechazo.setMrec_ord(rs.getInt("mrec_ord"));
                objMotivoRechazo.setMrec_nomrep(rs.getString("mrec_nomrep"));
                objMotivoRechazo.setMrec_usuadd(rs.getString("mrec_usuadd"));
                objMotivoRechazo.setMrec_fecadd(rs.getTimestamp("mrec_fecadd"));
                objMotivoRechazo.setMrec_usumod(rs.getString("mrec_usumod"));
                objMotivoRechazo.setMrec_fecmod(rs.getTimestamp("mrec_fecmod"));
                objlstMotivoRechazo.add(objMotivoRechazo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMotivoRechazo.getSize() + " registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMotivoRechazo;
    }

    public ListModelList<MotivoRechazo> busquedaMotivoRechazo(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String BUSQUEDAMOTIVORECHAZOS = "";
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                        + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                        + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>0 order by t.mrec_key";
            } else {
                if (i_seleccion == 1) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>0 and mrec_id like '" + s_consulta + "' order by t.mrec_key";
                } else if (i_seleccion == 2) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>0 and mrec_des like '" + s_consulta + "'  order by t.mrec_key";
                } else if (i_seleccion == 3) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>0 and mrec_tipdoc like '" + s_consulta + "'  order by t.mrec_key";
                } else if (i_seleccion == 4) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est<>0 and mrec_tipo like '" + s_consulta + "'  order by t.mrec_key";
                }
            }
        } else {
            if (i_seleccion == 0) {
                BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                        + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                        + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_estado + " order by t.mrec_key";
            } else {
                if (i_seleccion == 1) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_estado + " and mrec_id like '" + s_consulta + "' order by t.mrec_key";
                } else if (i_seleccion == 2) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_estado + " and mrec_des like '" + s_consulta + "' order by t.mrec_key";
                } else if (i_seleccion == 3) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_estado + " and mrec_tipdoc like '" + s_consulta + "' order by t.mrec_key";
                } else if (i_seleccion == 4) {
                    BUSQUEDAMOTIVORECHAZOS = "select t.mrec_key,t.mrec_id,t.mrec_des,t.mrec_tipdoc,decode(t.mrec_tipdoc,'DV','DOCUMENTO DE VENTA','PV','PEDIDO DE VENTA','CM','CAMBIO') MREC_TIPDOCDES,t.mrec_tipo,"
                            + "decode(t.mrec_tipo,'RT','RECHAZO TOTAL','RP','RECHAZO PARCIAL','VT','VENTA TOTAL','VP','VENTA PARCIAL')MREC_TIPODES ,"
                            + "t.mrec_est,t.mrec_ord,t.mrec_nomrep,t.mrec_usuadd,t.mrec_fecadd,t.mrec_usumod,t.mrec_fecmod from tmotivo_rechazo t where t.mrec_est=" + i_estado + " and mrec_tipo like '" + s_consulta + "' order by t.mrec_key";
                }
            }
        }
        P_WHERE = BUSQUEDAMOTIVORECHAZOS;
        objlstMotivoRechazo = new ListModelList<MotivoRechazo>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(BUSQUEDAMOTIVORECHAZOS);
            while (rs.next()) {
                objMotivoRechazo = new MotivoRechazo();
                objMotivoRechazo.setMrec_key(rs.getInt("mrec_key"));
                objMotivoRechazo.setMrec_id(rs.getString("mrec_id"));
                objMotivoRechazo.setMrec_des(rs.getString("mrec_des"));
                objMotivoRechazo.setMrec_tipdoc(rs.getString("mrec_tipdoc"));
                objMotivoRechazo.setMrec_tipo(rs.getString("mrec_tipo"));
                objMotivoRechazo.setMrec_est(rs.getInt("mrec_est"));
                objMotivoRechazo.setMrec_ord(rs.getInt("mrec_ord"));
                objMotivoRechazo.setMrec_nomrep(rs.getString("mrec_nomrep"));
                objMotivoRechazo.setMrec_usuadd(rs.getString("mrec_usuadd"));
                objMotivoRechazo.setMrec_fecadd(rs.getTimestamp("mrec_fecadd"));
                objMotivoRechazo.setMrec_usumod(rs.getString("mrec_usumod"));
                objMotivoRechazo.setMrec_fecmod(rs.getTimestamp("mrec_fecmod"));
                objlstMotivoRechazo.add(objMotivoRechazo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMotivoRechazo.getSize() + " registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstMotivoRechazo;
    }

    public MotivoRechazo buscaMotRecxCodigo(String codigo) throws SQLException {
        String SQLMOTIVORECHAZO = "select * from tmotivo_rechazo t where t.mrec_est=1 and t.mrec_id='" + codigo + "'";
        objMotivoRechazo = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQLMOTIVORECHAZO);
            while (rs.next()) {
                objMotivoRechazo = new MotivoRechazo();
                objMotivoRechazo.setMrec_key(rs.getInt("mrec_key"));
                objMotivoRechazo.setMrec_id(rs.getString("mrec_id"));
                objMotivoRechazo.setMrec_des(rs.getString("mrec_des"));
                objMotivoRechazo.setMrec_tipdoc(rs.getString("mrec_tipdoc"));
                objMotivoRechazo.setMrec_tipo(rs.getString("mrec_tipo"));
                objMotivoRechazo.setMrec_est(rs.getInt("mrec_est"));
                objMotivoRechazo.setMrec_ord(rs.getInt("mrec_ord"));
                objMotivoRechazo.setMrec_nomrep(rs.getString("mrec_nomrep"));
                objMotivoRechazo.setMrec_usuadd(rs.getString("mrec_usuadd"));
                objMotivoRechazo.setMrec_fecadd(rs.getTimestamp("mrec_fecadd"));
                objMotivoRechazo.setMrec_usumod(rs.getString("mrec_usumod"));
                objMotivoRechazo.setMrec_fecmod(rs.getTimestamp("mrec_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha consultado el registro con el codigo " + objMotivoRechazo.getMrec_id());
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objMotivoRechazo;
    }

}
