package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.RelGui;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zul.Messagebox;

public class DaoRelNotaES {

    //Instancias de Objetos
    ListModelList<RelGui> objlstRelGui;
    RelGui objRelGui;
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
    private static final Logger LOGGER = Logger.getLogger(DaoRelNotaES.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarRelGui(RelGui objRelGui) throws SQLException {
        String s_tabsubdes = objRelGui.getTab_subdes();
        String s_tabnomrep = objRelGui.getTab_nomrep();
        int i_tabord = objRelGui.getTab_ord();
        String s_tabusuadd = objRelGui.getTab_usuadd();
        String SQL_INSERTAR_REL_GUIAS = "{call pack_ttabgen.p_012_insertarRelacionGuia(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_REL_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.setString(4, s_tabusuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tabsubdes);
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

    public ParametrosSalida actualizarRelGui(RelGui objRelGui) throws SQLException {
        int i_tabid = objRelGui.getTab_id();
        String s_tabsubdes = objRelGui.getTab_subdes();
        String s_tabnomrep = objRelGui.getTab_nomrep();
        int i_tabord = objRelGui.getTab_ord();
        int i_tabest = objRelGui.getTab_est();
        String s_tabusumod = objRelGui.getTab_usumod();
        String SQL_ACTUALIZAR_REL_GUIAS = "{call pack_ttabgen.p_012_actualizarRelacionGuia(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.setString(6, s_tabusumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarRelGui(RelGui objRelGui) throws SQLException {
        int i_tabid = objRelGui.getTab_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_ttabgen.p_012_eliminarRelacionGuia(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
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
    public ListModelList<RelGui> listaRelacionGuia(int val) throws SQLException {
        String sql;
        if (val == 0) {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                    + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est<>" + val
                    + " order by p.tab_id";
        } else {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                    + "where p.tab_cod=12 and p.tab_id<>0 and p.tab_est=" + val + " order by p.tab_id";
        }
        P_WHERE = sql;
        try {
            objlstRelGui = new ListModelList<RelGui>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objRelGui = new RelGui();
                objRelGui.setTab_id(rs.getInt("tab_id"));
                objRelGui.setTab_subdes(rs.getString("tab_subdes"));
                objRelGui.setTab_est(rs.getInt("tab_est"));
                objRelGui.setTab_ord(rs.getInt("tab_ord"));
                objRelGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objRelGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objRelGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objRelGui.setTab_usumod(rs.getString("tab_usumod"));
                objRelGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstRelGui.add(objRelGui);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRelGui.getSize() + " registro(s)");
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
        return objlstRelGui;
    }

    public ListModelList<RelGui> busquedaRelGui(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                        + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est<>0 order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                            + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est<>0 and p.tab_id like '" + consulta + "'"
                            + " order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                            + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est<>0  and p.tab_subdes like '" + consulta + "'"
                            + " order by p.tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                        + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                            + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta + "'"
                            + " order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p"
                            + " where p.tab_cod=12 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "'"
                            + " order by p.tab_id";
                }
            }
        }
        P_WHERE = sql;
        try {
            objlstRelGui = new ListModelList<RelGui>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objRelGui = new RelGui();
                objRelGui.setTab_id(rs.getInt("tab_id"));
                objRelGui.setTab_subdes(rs.getString("tab_subdes"));
                objRelGui.setTab_est(rs.getInt("tab_est"));
                objRelGui.setTab_ord(rs.getInt("tab_ord"));
                objRelGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objRelGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objRelGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objRelGui.setTab_usumod(rs.getString("tab_usumod"));
                objRelGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstRelGui.add(objRelGui);
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
        return objlstRelGui;
    }
    
    public String validaMovimientos(RelGui objRelGui) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientosrel(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objRelGui.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la Relación de Guia " + objRelGui.getTab_id());
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
