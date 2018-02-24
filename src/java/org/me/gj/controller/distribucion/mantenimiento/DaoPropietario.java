package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Propietario;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPropietario {

    //Instancia de Objetos
    ListModelList<Propietario> objLisPropietario;
    Propietario objPropietario;
    //Variables Publicas
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
    private static final Logger LOGGER = Logger.getLogger(DaoPropietario.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarPropietario(Propietario objPropietario) throws SQLException, ParseException {
        String SQL_INSERTAR_PROPIETARIO = "{call PACK_TPROPIETARIO.p_insertarPropietario(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PROPIETARIO);
            cst.clearParameters();
            cst.setString(1, objPropietario.getProp_apepat());
            cst.setString(2, objPropietario.getProp_apemat());
            cst.setString(3, objPropietario.getProp_nom());
            cst.setString(4, objPropietario.getProp_razsoc());
            cst.setLong(5, objPropietario.getProp_ruc());
            cst.setString(6, objPropietario.getProp_dni());
            cst.setDate(7, objPropietario.getProp_fecnac() == null ? null : new java.sql.Date(objPropietario.getProp_fecnac().getTime()));
            cst.setLong(8, objPropietario.getProp_telef());
            cst.setString(9, objPropietario.getProp_direcc());
            cst.setString(10, objPropietario.getProp_usuadd());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objPropietario.getProp_razsoc());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarPropietario(Propietario objPropietario) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_PROPIETARIO = "{call PACK_TPROPIETARIO.p_actualizarPropietario(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PROPIETARIO);
            cst.clearParameters();
            cst.setInt(1, objPropietario.getProp_id());
            cst.setString(2, objPropietario.getProp_apepat());
            cst.setString(3, objPropietario.getProp_apemat());
            cst.setString(4, objPropietario.getProp_nom());
            cst.setString(5, objPropietario.getProp_razsoc());
            cst.setLong(6, objPropietario.getProp_ruc());
            cst.setString(7, objPropietario.getProp_dni());
            cst.setDate(8, objPropietario.getProp_fecnac() == null ? null : new java.sql.Date(objPropietario.getProp_fecnac().getTime()));
            cst.setLong(9, objPropietario.getProp_telef());
            cst.setString(10, objPropietario.getProp_direcc());
            cst.setInt(11, objPropietario.getProp_est());
            cst.setString(12, objUsuCredential.getCuenta());
            cst.registerOutParameter(13, java.sql.Types.NUMERIC);
            cst.registerOutParameter(14, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(14);
            i_flagErrorBD = cst.getInt(13);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objPropietario.getProp_id());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarPropietario(Propietario objPropietario) throws SQLException {
        String SQL_ELIMINAR_PROPIETARIO = "{call PACK_TPROPIETARIO.p_eliminarPropietario(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PROPIETARIO);
            cst.clearParameters();
            cst.setInt(1, objPropietario.getProp_id());
            cst.setString(2, objUsuCredential.getCuenta());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objPropietario.getProp_id());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Propietario> listaPropietario(int caso) throws SQLException {
        String SQL_PROPIETARIO;
        if (caso == 0) {
            SQL_PROPIETARIO = "select * from codijisa.tpropietario t where t.prop_est <>" + caso + " order by t.prop_id";
        } else {
            SQL_PROPIETARIO = "select * from codijisa.tpropietario t where t.prop_est =" + caso + " order by t.prop_id";
        }
        P_WHERE = SQL_PROPIETARIO;
        try {
            objLisPropietario = new ListModelList<Propietario>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROPIETARIO);
            while (rs.next()) {
                objPropietario = new Propietario();
                objPropietario.setProp_id(rs.getInt("prop_id"));
                objPropietario.setProp_apepat(rs.getString("prop_apepat"));
                objPropietario.setProp_apemat(rs.getString("prop_apemat"));
                objPropietario.setProp_nom(rs.getString("prop_nom"));
                objPropietario.setProp_razsoc(rs.getString("prop_razsoc"));
                objPropietario.setProp_ruc(rs.getLong("prop_ruc"));
                objPropietario.setProp_dni(rs.getString("prop_dni"));
                objPropietario.setProp_fecnac(rs.getDate("prop_fecnac"));
                objPropietario.setProp_telef(rs.getLong("prop_telef"));
                objPropietario.setProp_direcc(rs.getString("prop_direcc"));
                objPropietario.setProp_est(rs.getInt("prop_est"));
                objPropietario.setProp_usuadd(rs.getString("prop_usuadd"));
                objPropietario.setProp_fecadd(rs.getTimestamp("prop_fecadd"));
                objPropietario.setProp_usumod(rs.getString("prop_usumod"));
                objPropietario.setProp_fecmod(rs.getTimestamp("prop_fecmod"));
                objLisPropietario.add(objPropietario);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisPropietario.getSize() + " registro(s)");
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
        return objLisPropietario;
    }

    public ListModelList<Propietario> busquedaPropietario(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_PROPIETARIO;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 order by t.prop_id";
            } else if (seleccion == 1) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 and t.prop_id like '" + consulta + "' order by t.prop_id";
            } else if (seleccion == 2) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 and t.prop_razsoc like '" + consulta + "' order by t.prop_id";
            } else if (seleccion == 3) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 and t.prop_ruc like '" + consulta + "' order by t.prop_id";
            } else if (seleccion == 4) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 and t.prop_dni like '" + consulta + "' order by t.prop_id";
            } else {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est<> 0 and t.prop_direcc like '" + consulta + "' order by t.prop_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est=" + i_estado + "  order by t.prop_id";
            } else if (seleccion == 1) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est = " + i_estado + " and t.prop_id like '" + consulta + "'  order by t.prop_id";
            } else if (seleccion == 2) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est = " + i_estado + " and t.prop_razsoc like '" + consulta + "'  order by t.prop_id";
            } else if (seleccion == 3) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est = " + i_estado + " and t.prop_ruc like '" + consulta + "'  order by t.prop_id";
            } else if (seleccion == 4) {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est = " + i_estado + " and t.prop_dni like '" + consulta + "'  order by t.prop_id";
            } else {
                SQL_PROPIETARIO = "select * from tpropietario t where t.prop_est = " + i_estado + " and t.prop_direcc like '" + consulta + "'  order by t.prop_id";
            }
        }
        P_WHERE = SQL_PROPIETARIO;
        try {
            objLisPropietario = new ListModelList<Propietario>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROPIETARIO);
            while (rs.next()) {
                objPropietario = new Propietario();
                objPropietario.setProp_id(rs.getInt("prop_id"));
                objPropietario.setProp_apepat(rs.getString("prop_apepat"));
                objPropietario.setProp_apemat(rs.getString("prop_apemat"));
                objPropietario.setProp_nom(rs.getString("prop_nom"));
                objPropietario.setProp_razsoc(rs.getString("prop_razsoc"));
                objPropietario.setProp_ruc(rs.getLong("prop_ruc"));
                objPropietario.setProp_dni(rs.getString("prop_dni"));
                objPropietario.setProp_fecnac(rs.getDate("prop_fecnac"));
                objPropietario.setProp_telef(rs.getLong("prop_telef"));
                objPropietario.setProp_direcc(rs.getString("prop_direcc"));
                objPropietario.setProp_est(rs.getInt("prop_est"));
                objPropietario.setProp_usuadd(rs.getString("prop_usuadd"));
                objPropietario.setProp_fecadd(rs.getTimestamp("prop_fecadd"));
                objPropietario.setProp_usumod(rs.getString("prop_usumod"));
                objPropietario.setProp_fecmod(rs.getTimestamp("prop_fecmod"));
                objLisPropietario.add(objPropietario);
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
        return objLisPropietario;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC = "";
        int valor = 0;
        if (tipo == 1) {//DNI
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tpropietario t where t.prop_dni = " + dato + " and t.prop_est<>0 ";
        } else if (tipo == 2) {//RUC
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tpropietario t where t.prop_ruc = " + dato + " and t.prop_est<>0";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Almacen default  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
