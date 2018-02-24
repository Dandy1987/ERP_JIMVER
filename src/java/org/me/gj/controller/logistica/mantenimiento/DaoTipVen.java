package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.TipVen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoTipVen {

    //Instancias de Objetos
    TipVen objTipVen;
    ListModelList<TipVen> objlstTipVen;
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
    private static final Logger LOGGER = Logger.getLogger(DaoTipVen.class);

    //Eventos Primarios - Transaccionales
    public String insertarTipoVenta(TipVen objTipVen) throws SQLException {
        String s_tabsubdes = objTipVen.getDesVen();
        String s_tabnomrep = objTipVen.getNomRepVen();
        int i_tabord = objTipVen.getOrdVen();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_PROC_GUIAS = "{call pack_ttabgen.p_011_insertarTipoVenta(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PROC_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
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

    public String actualizarTipoFact(TipVen objTipVen) throws SQLException {
        int s_tabid = objTipVen.getIdVen();
        String s_tabsubdes = objTipVen.getDesVen();
        String s_tabnomrep = objTipVen.getNomRepVen();
        int i_tabord = objTipVen.getOrdVen();
        int i_tabest = objTipVen.getEstVen();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_PROC_GUIAS = "{call pack_ttabgen.p_011_actualizarTipoVenta(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PROC_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
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

    public String eliminarTipoFact(TipVen objTipVen) throws SQLException {
        int s_tabid = objTipVen.getIdVen();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_PROC_GUIAS = "{call pack_ttabgen.p_011_eliminarTipoVenta(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PROC_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
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
    public ListModelList<TipVen> listaTipoVenta(int val) throws SQLException {
        String sql;
        if (val == 0) {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep from ttabgen p"
                    + " where p.tab_cod=11 and p.tab_id<>0 and p.tab_est<>" + val
                    + " order by p.tab_ord,p.tab_id";
        } else {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep from ttabgen p "
                    + "where p.tab_cod=11 and p.tab_id<>0 and p.tab_est=" + val + " order by p.tab_ord,p.tab_id";
        }
        try {
            objlstTipVen = new ListModelList<TipVen>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTipVen = new TipVen();
                objTipVen.setIdVen(rs.getInt(1));
                objTipVen.setDesVen(rs.getString(2));
                objTipVen.setEstVen(rs.getInt(3));
                objTipVen.setOrdVen(rs.getInt(4));
                objTipVen.setNomRepVen(rs.getString(5));
                objlstTipVen.add(objTipVen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipVen.getSize() + " registro(s)");
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
        return objlstTipVen;
    }

    public ListModelList<TipVen> busquedaTipVen(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (seleccion == 1) {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep from ttabgen p"
                    + " where p.tab_cod=11 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta
                    + "' order by p.tab_ord,p.tab_id";
        } else {
            sql = "select p.tab_id,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep from ttabgen p"
                    + " where p.tab_cod=11 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta
                    + "' order by p.tab_ord,p.tab_id";
        }
        try {
            objlstTipVen = new ListModelList<TipVen>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTipVen = new TipVen();
                objTipVen.setIdVen(rs.getInt(1));
                objTipVen.setDesVen(rs.getString(2));
                objTipVen.setEstVen(rs.getInt(3));
                objTipVen.setOrdVen(rs.getInt(4));
                objTipVen.setNomRepVen(rs.getString(5));
                objlstTipVen.add(objTipVen);
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
        return objlstTipVen;
    }
}
