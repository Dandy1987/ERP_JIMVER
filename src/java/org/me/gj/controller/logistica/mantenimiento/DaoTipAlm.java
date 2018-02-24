package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.TipAlm;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoTipAlm {

    //Instancias de Objetos
    ListModelList<TipAlm> objlstTipAlm;
    TipAlm objTipAlm;
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
    private static final Logger LOGGER = Logger.getLogger(DaoTipAlm.class);

    //Eventos Primarios - Transaccionales
    public String insertarTipAlm(TipAlm objTipAlm) throws SQLException {
        String SQL_INSERTAR_TIP_ALMACEN = "{call pack_ttabgen.p_009_insertarTiposAlmacen(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TIP_ALMACEN);
            cst.clearParameters();
            cst.setString(1, objTipAlm.getTab_subdes());
            cst.setString(2, objTipAlm.getTab_nomrep());
            cst.setInt(3, objTipAlm.getTab_ord());
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTipAlm.getTab_subdes());
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

    public String actualizarTipAlm(TipAlm objTipAlm) throws SQLException {
        String SQL_ACTUALIZAR_TIP_ALMACEN = "{call pack_ttabgen.p_009_actualizarTiposAlmacen(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TIP_ALMACEN);
            cst.clearParameters();
            cst.setInt(1, objTipAlm.getTab_id());
            cst.setString(2, objTipAlm.getTab_subdes());
            cst.setString(3, objTipAlm.getTab_nomrep());
            cst.setInt(4, objTipAlm.getTab_ord());
            cst.setInt(5, objTipAlm.getTab_est());
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objTipAlm.getTab_id());
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

    public String eliminarTipAlm(TipAlm objTipAlm) throws SQLException {
        int i_tabid = objTipAlm.getTab_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_ttabgen.p_009_eliminarTiposAlmacen(?,?,?)}";
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
    public ListModelList<TipAlm> listaTipoAlmacen(int val) throws SQLException {
        String SQL;
        if (val == 0) {
            SQL = "select * from ttabgen p where p.tab_cod = 9 and p.tab_id <> 0 and p.tab_est <> " + val + " order by p.tab_id";
        } else {
            SQL = "select * from ttabgen p where p.tab_cod = 9 and p.tab_id <> 0 and p.tab_est = " + val + " order by p.tab_id";
        }
        P_WHERE = SQL;
        try {
            objlstTipAlm = new ListModelList<TipAlm>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                objTipAlm = new TipAlm();
                objTipAlm.setTab_id(rs.getInt("tab_id"));
                objTipAlm.setTab_subdes(rs.getString("tab_subdes"));
                objTipAlm.setTab_est(rs.getInt("tab_est"));
                objTipAlm.setTab_ord(rs.getInt("tab_ord"));
                objTipAlm.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipAlm.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipAlm.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipAlm.setTab_usumod(rs.getString("tab_usumod"));
                objTipAlm.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTipAlm.add(objTipAlm);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipAlm.getSize() + " registro(s)");
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
        return objlstTipAlm;
    }

    public ListModelList<TipAlm> busquedaTipAlm(int seleccion, /*String consulta,*/ int i_estado) throws SQLException {
        String sql;
        //String orden;
        String orden;
        if (seleccion == 0){
            orden="tab_id";
        }else if (seleccion == 1){
            orden="tab_subdes";
        }else{
            orden="tab_ord";
        } 
        /*if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 order by " + orden;
            } else if (seleccion == 1) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 and tab_id like '" + consulta
                        + "' order by" + orden;
            } else {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 and tab_subdes like '" + consulta
                        + "'order by" + orden;
            }
        } else {
            if (seleccion == 0) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " order by" + orden;
            } else if (seleccion == 1) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " and tab_id like '" + consulta
                        + "' order by " + orden;
            } else {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " and tab_subdes like '" + consulta
                        + "' order by " + orden;
            }
        }*/
        
        if (i_estado == 0) {
            /*if (seleccion == 0) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 order by " + orden;
            } else if (seleccion == 1) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 order by " + orden;
            } else {*/
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est <> 0 order by " + orden;
            //}
        } else {
            /*if (seleccion == 0) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " order by " + orden;
            } else if (seleccion == 1) {
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " order by " + orden;
            } else {*/
                sql = "select * from ttabgen where tab_cod = 9 and tab_id <> 0 and tab_est=" + i_estado + " order by " + orden;
            //}
        }
        
        P_WHERE = sql;
        try {
            objlstTipAlm = new ListModelList<TipAlm>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTipAlm = new TipAlm();
                objTipAlm.setTab_id(rs.getInt("tab_id"));
                objTipAlm.setTab_subdes(rs.getString("tab_subdes"));
                objTipAlm.setTab_est(rs.getInt("tab_est"));
                objTipAlm.setTab_ord(rs.getInt("tab_ord"));
                objTipAlm.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipAlm.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipAlm.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipAlm.setTab_usumod(rs.getString("tab_usumod"));
                objTipAlm.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTipAlm.add(objTipAlm);
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
        return objlstTipAlm;
    }

    public String validaMovimientos(TipAlm objTipAlm) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objTipAlm.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del tipo de almacen " + objTipAlm.getTab_id());
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
