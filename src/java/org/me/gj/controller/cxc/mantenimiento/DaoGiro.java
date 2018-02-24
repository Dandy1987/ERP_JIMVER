package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.Giro;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoGiro {

    //Instancia de Objetos
    ListModelList<Giro> objlstGiros;
    Giro objGiro;
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
    private static final Logger LOGGER = Logger.getLogger(DaoGiro.class);

    //Eventos Primarios - Transaccionales
    public String insertarGiro(Giro objGiro) throws SQLException {
        String s_tabsubdes = objGiro.getTab_subdes();
        String s_tabnomrep = objGiro.getTab_nomrep();
        int i_tabord = objGiro.getTab_ord();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_GIRO = "{call pack_ttabgen.p_023_insertarGiroNegocio(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_GIRO);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setInt(2, i_tabord);
            cst.setString(3, s_tabnomrep);
            cst.setString(4, objUsuCredential.getCuenta());
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

    public String actualizarGiro(Giro objGiro) throws SQLException {
        int s_tabid = objGiro.getTab_id();
        String s_tabsubdes = objGiro.getTab_subdes();
        String s_tabnomrep = objGiro.getTab_nomrep();
        int i_tabord = objGiro.getTab_ord();
        int i_tabest = objGiro.getTab_est();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_GIRO = "{call pack_ttabgen.p_023_actualizarGiroNegocio(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_GIRO);
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

    public String eliminarGiro(Giro objGiro) throws SQLException {
        int s_tabid = objGiro.getTab_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_GIRO = "{call pack_ttabgen.p_023_eliminarGiroNegocio(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_GIRO);
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
    public ListModelList<Giro> listaGiros(int i_caso) throws SQLException {
        String SQL_GIROS = "";
        if (i_caso == 0) {
            SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=23 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p where p.tab_cod=23 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_GIROS;
        objlstGiros = new ListModelList<Giro>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GIROS);

            while (rs.next()) {
                objGiro = new Giro();
                objGiro.setTab_id(rs.getInt("tab_id"));
                objGiro.setTab_subdir(rs.getString("tab_subdir"));
                objGiro.setTab_subdes(rs.getString("tab_subdes"));
                objGiro.setTab_est(rs.getInt("tab_est"));
                objGiro.setTab_ord(rs.getInt("tab_ord"));
                objGiro.setTab_nomrep(rs.getString("tab_nomrep"));
                objGiro.setTab_usuadd(rs.getString("tab_usuadd"));
                objGiro.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objGiro.setTab_usumod(rs.getString("tab_usumod"));
                objGiro.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstGiros.add(objGiro);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstGiros.getSize() + " registro(s)");
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
        return objlstGiros;
    }

    public ListModelList<Giro> busquedaGiros(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_GIROS = "";
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est<> 0 order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est<> 0 and p.tab_id like '" + consulta.replace("0", "").trim() + "' order by p.tab_id";
            } else {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est<> 0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        } else {
            if (seleccion == 0) {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else if (seleccion == 1) {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta.replace("0", "").trim() + "' order by p.tab_id";
            } else {
                SQL_GIROS = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
            }
        }
        P_WHERE = SQL_GIROS;
        objlstGiros = new ListModelList<Giro>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GIROS);

            while (rs.next()) {
                objGiro = new Giro();
                objGiro.setTab_id(rs.getInt("tab_id"));
                objGiro.setTab_subdir(rs.getString("tab_subdir"));
                objGiro.setTab_subdes(rs.getString("tab_subdes"));
                objGiro.setTab_est(rs.getInt("tab_est"));
                objGiro.setTab_ord(rs.getInt("tab_ord"));
                objGiro.setTab_nomrep(rs.getString("tab_nomrep"));
                objGiro.setTab_usuadd(rs.getString("tab_usuadd"));
                objGiro.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objGiro.setTab_usumod(rs.getString("tab_usumod"));
                objGiro.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstGiros.add(objGiro);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstGiros.getSize() + " registro(s)");
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

        return objlstGiros;
    }

    public Giro buscaGiroxCodigo(String s_codigo) throws SQLException {
        objGiro = null;
        String SQL_BUSCAGIRO = "select p.tab_id,p.tab_subdir,p.tab_subdes,p.tab_est,p.tab_nomrep,p.tab_ord,p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod  from ttabgen p "
                + "where p.tab_cod=23 and p.tab_id<>0 and p.tab_est=1 and p.tab_id='" + s_codigo + "' order by p.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAGIRO);
            while (rs.next()) {
                objGiro = new Giro();
                objGiro.setTab_id(rs.getInt("tab_id"));
                objGiro.setTab_subdir(rs.getString("tab_subdir"));
                objGiro.setTab_subdes(rs.getString("tab_subdes"));
                objGiro.setTab_est(rs.getInt("tab_est"));
                objGiro.setTab_ord(rs.getInt("tab_ord"));
                objGiro.setTab_nomrep(rs.getString("tab_nomrep"));
                objGiro.setTab_usuadd(rs.getString("tab_usuadd"));
                objGiro.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objGiro.setTab_usumod(rs.getString("tab_usumod"));
                objGiro.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objGiro.getTab_id() + "");
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
        return objGiro;
    }

}
