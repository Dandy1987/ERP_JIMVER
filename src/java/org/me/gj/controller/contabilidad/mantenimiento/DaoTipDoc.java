package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoTipDoc {

    TipDoc objTipDoc;
    ListModelList<TipDoc> objlstTipDoc;
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoTipDoc.class);

    public ListModelList<TipDoc> listaTipDoc(int i_caso) throws SQLException {
        String SQL_TIPDOC = "";
        if (i_caso == 0) {
            SQL_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est<>" + i_caso + " order by t.tipdoc_key";
        } else if (i_caso == 1) {
            SQL_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est=" + i_caso + " order by t.tipdoc_ord, t.tipdoc_key";
        } else {
            SQL_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est=1 and t.tipdoc_key in(1,2) order by t.tipdoc_key";

        }

        objlstTipDoc = new ListModelList<TipDoc>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPDOC);
            while (rs.next()) {
                objTipDoc = new TipDoc();
                objTipDoc.setTipdoc_key(rs.getInt(1));
                objTipDoc.setTipdoc_id(rs.getString(2));
                objTipDoc.setTipdoc_des(rs.getString(3));
                objTipDoc.setTipdoc_est(rs.getInt(4));
                objTipDoc.setTipdoc_sun(rs.getInt(5));
                objTipDoc.setTipdoc_com(rs.getInt(6));
                objTipDoc.setTipdoc_caj(rs.getInt(7));
                objTipDoc.setTipdoc_deb(rs.getInt(8));
                objTipDoc.setTipdoc_mov(rs.getString(9));
                objTipDoc.setTipdoc_ord(rs.getInt(10));
                objTipDoc.setTipdoc_nomrep(rs.getString(11));
                objlstTipDoc.add(objTipDoc);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipDoc.getSize() + " registro(s)");
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
        return objlstTipDoc;
    }

    public ListModelList<TipDoc> busquedaTipDoc(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_LISTA_TIPDOC;
        if (seleccion == 1) {
            SQL_LISTA_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est=" + i_estado + " and t.tipdoc_id like '" + consulta + "' order by t.tipdoc_key";
        } else {
            SQL_LISTA_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est=" + i_estado + " and t.tipdoc_des like '" + consulta + "' order by t.tipdoc_key";
        }
        objlstTipDoc = new ListModelList<TipDoc>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_TIPDOC);
            while (rs.next()) {
                objTipDoc = new TipDoc();
                objTipDoc.setTipdoc_key(rs.getInt(1));
                objTipDoc.setTipdoc_id(rs.getString(2));
                objTipDoc.setTipdoc_des(rs.getString(3));
                objTipDoc.setTipdoc_est(rs.getInt(4));
                objTipDoc.setTipdoc_sun(rs.getInt(5));
                objTipDoc.setTipdoc_com(rs.getInt(6));
                objTipDoc.setTipdoc_caj(rs.getInt(7));
                objTipDoc.setTipdoc_deb(rs.getInt(8));
                objTipDoc.setTipdoc_mov(rs.getString(9));
                objTipDoc.setTipdoc_ord(rs.getInt(10));
                objTipDoc.setTipdoc_nomrep(rs.getString(11));
                objlstTipDoc.add(objTipDoc);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipDoc.getSize() + " registro(s)");
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
        return objlstTipDoc;
    }

    public TipDoc buscarTipDocxCodigo(int i_tipdoc_key) throws SQLException {
        String SQL_TIPDOC = "select * from v_listatipdoc t where t.tipdoc_est=1 and t.tipdoc_key=" + i_tipdoc_key;
        objTipDoc = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPDOC);
            while (rs.next()) {
                objTipDoc = new TipDoc();
                objTipDoc.setTipdoc_key(rs.getInt(1));
                objTipDoc.setTipdoc_id(rs.getString(2));
                objTipDoc.setTipdoc_des(rs.getString(3));
                objTipDoc.setTipdoc_est(rs.getInt(4));
                objTipDoc.setTipdoc_sun(rs.getInt(5));
                objTipDoc.setTipdoc_com(rs.getInt(6));
                objTipDoc.setTipdoc_caj(rs.getInt(7));
                objTipDoc.setTipdoc_deb(rs.getInt(8));
                objTipDoc.setTipdoc_mov(rs.getString(9));
                objTipDoc.setTipdoc_ord(rs.getInt(10));
                objTipDoc.setTipdoc_nomrep(rs.getString(11));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha busqueda el registro con codigo " + i_tipdoc_key);
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
        return objTipDoc;
    }

    public ParametrosSalida insertar(TipDoc objTipDoc) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_TIPDOC = "{call pack_ttabgen.p_039_insertarTipDoc(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TIPDOC);
            cst.clearParameters();
            cst.setString(1, objTipDoc.getTipdoc_des());
            cst.setString(2, (objTipDoc.getTipdoc_sun() == -1) ? null : String.valueOf(objTipDoc.getTipdoc_sun()));
            cst.setInt(3, objTipDoc.getTipdoc_com());
            cst.setInt(4, objTipDoc.getTipdoc_caj());
            cst.setInt(5, objTipDoc.getTipdoc_deb());
            cst.setString(6, objTipDoc.getTipdoc_mov());
            cst.setInt(7, objTipDoc.getTipdoc_ord());
            cst.setString(8, objTipDoc.getTipdoc_nomrep());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTipDoc.getTipdoc_des());
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

    public ParametrosSalida modificar(TipDoc objTipDoc) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_TIPDOC = "{call pack_ttabgen.p_039_actualizarTipDoc(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TIPDOC);
            cst.clearParameters();
            cst.setInt(1, objTipDoc.getTipdoc_key());
            cst.setString(2, objTipDoc.getTipdoc_des());
            cst.setInt(3, objTipDoc.getTipdoc_est());
            cst.setString(4, (objTipDoc.getTipdoc_sun() == -1) ? null : String.valueOf(objTipDoc.getTipdoc_sun()));
            cst.setInt(5, objTipDoc.getTipdoc_com());
            cst.setInt(6, objTipDoc.getTipdoc_caj());
            cst.setInt(7, objTipDoc.getTipdoc_deb());
            cst.setString(8, objTipDoc.getTipdoc_mov());
            cst.setInt(9, objTipDoc.getTipdoc_ord());
            cst.setString(10, objTipDoc.getTipdoc_nomrep());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTipDoc.getTipdoc_des());
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

    public ParametrosSalida eliminar(TipDoc objTipDoc) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_TIPDOC = "{call pack_ttabgen.p_039_eliminarTipDoc(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_TIPDOC);
            cst.clearParameters();
            cst.setInt(1, objTipDoc.getTipdoc_key());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTipDoc.getTipdoc_des());
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
}
