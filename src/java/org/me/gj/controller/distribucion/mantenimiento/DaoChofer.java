package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Chofer;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoChofer {

    //Instancia de Objetos
    ListModelList<Chofer> objlstChofer;
    Chofer objChofer;
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
    private static final Logger LOGGER = Logger.getLogger(DaoProgramacionReparto.class);

    //Eventos Primarios -Transaccional
    public ParametrosSalida insertarChofer(Chofer objChofer) throws SQLException {
        String chof_apepat = objChofer.getChof_apepat();
        String chof_apemat = objChofer.getChof_apemat();
        String chof_nom = objChofer.getChof_nom();
        String chof_razsoc = objChofer.getChof_razsoc();
        String chof_ruc = objChofer.getChof_ruc();
        int chof_ididen = objChofer.getChof_ididen();
        String chof_nroiden = objChofer.getChof_nroiden();
        Date chof_fecnac = objChofer.getChof_fecnac();
        String chof_brevete = objChofer.getChof_brevete();
        int chof_telef = objChofer.getChof_telef();
        String chof_direcc = objChofer.getChof_direcc();
        int chof_est = objChofer.getChof_est();
        String chof_usuadd = objChofer.getChof_usuadd();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_CHOFER = "{call pack_tchofer.p_insertarChofer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setString(3, chof_apepat);
            cst.setString(4, chof_apemat);
            cst.setString(5, chof_nom);
            cst.setString(6, chof_razsoc);
            cst.setString(7, chof_ruc);
            cst.setInt(8, chof_ididen);
            cst.setString(9, chof_nroiden);
            cst.setDate(10, new java.sql.Date(chof_fecnac.getTime()));
            cst.setString(11, chof_brevete);
            cst.setInt(12, chof_telef);
            cst.setString(13, chof_direcc);
            cst.setInt(14, chof_est);
            cst.setString(15, chof_usuadd);
            cst.registerOutParameter(16, java.sql.Types.NUMERIC);
            cst.registerOutParameter(17, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(17);
            i_flagErrorBD = cst.getInt(16);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + objChofer.getChof_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarChofer(Chofer objChofer) throws SQLException {
        int chof_key = objChofer.getChof_key();
        String chof_apepat = objChofer.getChof_apepat();
        String chof_apemat = objChofer.getChof_apemat();
        String chof_nom = objChofer.getChof_nom();
        String chof_razsoc = objChofer.getChof_razsoc();
        String chof_ruc = objChofer.getChof_ruc();
        int chof_ididen = objChofer.getChof_ididen();
        String chof_nroiden = objChofer.getChof_nroiden();
        Date chof_fecnac = objChofer.getChof_fecnac();
        String chof_brevete = objChofer.getChof_brevete();
        int chof_telef = objChofer.getChof_telef();
        String chof_direcc = objChofer.getChof_direcc();
        int chof_est = objChofer.getChof_est();
        String chof_usuadd = objChofer.getChof_usuadd();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_CHOFER = "{call pack_tchofer.p_actualizarChofer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, chof_key);
            cst.setString(4, chof_apepat);
            cst.setString(5, chof_apemat);
            cst.setString(6, chof_nom);
            cst.setString(7, chof_razsoc);
            cst.setString(8, chof_ruc);
            cst.setInt(9, chof_ididen);
            cst.setString(10, chof_nroiden);
            cst.setDate(11, new java.sql.Date(chof_fecnac.getTime()));
            cst.setString(12, chof_brevete);
            cst.setInt(13, chof_telef);
            cst.setString(14, chof_direcc);
            cst.setInt(15, chof_est);
            cst.setString(16, chof_usuadd);
            cst.registerOutParameter(17, java.sql.Types.NUMERIC);
            cst.registerOutParameter(18, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(18);
            i_flagErrorBD = cst.getInt(17);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + objChofer.getChof_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarChofer(Chofer objChofer) throws SQLException {
        int chof_key = objChofer.getChof_key();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_CHOFER = "{call pack_tchofer.p_eliminarChofer(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, chof_key);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + objChofer.getChof_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Chofer> listaChofer(int estado) throws SQLException {
        String SQL_LISTA_CHOFER;
        if (estado == 0) {
            SQL_LISTA_CHOFER = "select * from v_listachofer t where t.chof_est<>" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.chof_key";
        } else {
            SQL_LISTA_CHOFER = "select * from v_listachofer t where t.chof_est=" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.chof_key";
        }
        P_WHERE = SQL_LISTA_CHOFER;
        objlstChofer = new ListModelList<Chofer>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CHOFER);
            while (rs.next()) {
                objChofer = new Chofer();
                objChofer.setChof_key(rs.getInt("chof_key"));
                objChofer.setChof_id(rs.getString("chof_id"));
                objChofer.setChof_apepat(rs.getString("chof_apepat"));
                objChofer.setChof_apemat(rs.getString("chof_apemat"));
                objChofer.setChof_nom(rs.getString("chof_nom"));
                objChofer.setChof_razsoc(rs.getString("chof_razsoc"));
                objChofer.setChof_ruc(rs.getString("chof_ruc"));
                objChofer.setChof_ididen(rs.getInt("chof_ididen"));
                objChofer.setChof_nroiden(rs.getString("chof_nroiden"));
                objChofer.setChof_fecnac(rs.getDate("chof_fecnac"));
                objChofer.setChof_brevete(rs.getString("chof_brevete"));
                objChofer.setChof_telef(rs.getInt("chof_telef"));
                objChofer.setChof_direcc(rs.getString("chof_direcc"));
                objChofer.setChof_est(rs.getInt("chof_est"));
                objChofer.setChof_usuadd(rs.getString("chof_usuadd"));
                objChofer.setChof_fecadd(rs.getTimestamp("chof_fecadd"));
                objChofer.setChof_usumod(rs.getString("chof_usumod"));
                objChofer.setChof_fecmod(rs.getTimestamp("chof_fecmod"));
                objChofer.setSuc_id(rs.getInt("suc_id"));
                objChofer.setEmp_id(rs.getInt("emp_id"));
                objlstChofer.add(objChofer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstChofer.getSize() + " registro(s)");
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
        return objlstChofer;
    }

    public ListModelList<Chofer> busquedaChoferes(int seleccion, String busqueda, int estado) throws SQLException {
        String SQL_LISTA_CHOFER = "";
        int em_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        if (estado == 3) { // TODOS 
            if (seleccion == 0) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where  t.emp_id='" + em_id + "' and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 1) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_key like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 2) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_razsoc like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 3) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_nroiden like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            }
        } else {
            if (seleccion == 0) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_est=" + estado + " "
                        + " and t.emp_id='" + em_id + "' and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 1) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_est=" + estado + " "
                        + " and t.chof_id like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 2) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_est=" + estado + " "
                        + " and t.chof_razsoc like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            } else if (seleccion == 3) {
                SQL_LISTA_CHOFER = " select * from v_listachofer t"
                        + " where t.chof_est=" + estado + " "
                        + " and t.chof_nroiden like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.chof_key";
            }
        }

        P_WHERE = SQL_LISTA_CHOFER;
        objlstChofer = new ListModelList<Chofer>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CHOFER);
            while (rs.next()) {
                objChofer = new Chofer();
                objChofer.setChof_key(rs.getInt("chof_key"));
                objChofer.setChof_id(rs.getString("chof_id"));
                objChofer.setChof_apepat(rs.getString("chof_apepat"));
                objChofer.setChof_apemat(rs.getString("chof_apemat"));
                objChofer.setChof_nom(rs.getString("chof_nom"));
                objChofer.setChof_razsoc(rs.getString("chof_razsoc"));
                objChofer.setChof_ruc(rs.getString("chof_ruc"));
                objChofer.setChof_ididen(rs.getInt("chof_ididen"));
                objChofer.setChof_nroiden(rs.getString("chof_nroiden"));
                objChofer.setChof_fecnac(rs.getDate("chof_fecnac"));
                objChofer.setChof_brevete(rs.getString("chof_brevete"));
                objChofer.setChof_telef(rs.getInt("chof_telef"));
                objChofer.setChof_direcc(rs.getString("chof_direcc"));
                objChofer.setChof_est(rs.getInt("chof_est"));
                objChofer.setChof_usuadd(rs.getString("chof_usuadd"));
                objChofer.setChof_fecadd(rs.getTimestamp("chof_fecadd"));
                objChofer.setChof_usumod(rs.getString("chof_usumod"));
                objChofer.setChof_fecmod(rs.getTimestamp("chof_fecmod"));
                objChofer.setSuc_id(rs.getInt("suc_id"));
                objChofer.setEmp_id(rs.getInt("emp_id"));
                objlstChofer.add(objChofer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstChofer.getSize() + " registro(s)");
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
        return objlstChofer;
    }

    public Chofer busquedaChoferxCodigo(int chof_key) throws SQLException {
        String SQL_BUSQUEDA_CHOFER;

        SQL_BUSQUEDA_CHOFER = " select * from v_listachofer t where t.chof_est= 1"
                + " and t.chof_key = " + chof_key + " "
                + " and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + " and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                + " order by t.chof_key";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_CHOFER);
            while (rs.next()) {
                objChofer = new Chofer();
                objChofer.setChof_key(rs.getInt("chof_key"));
                objChofer.setChof_id(rs.getString("chof_id"));
                objChofer.setChof_apepat(rs.getString("chof_apepat"));
                objChofer.setChof_apemat(rs.getString("chof_apemat"));
                objChofer.setChof_nom(rs.getString("chof_nom"));
                objChofer.setChof_razsoc(rs.getString("chof_razsoc"));
                objChofer.setChof_ruc(rs.getString("chof_ruc"));
                objChofer.setChof_ididen(rs.getInt("chof_ididen"));
                objChofer.setChof_nroiden(rs.getString("chof_nroiden"));
                objChofer.setChof_fecnac(rs.getDate("chof_fecnac"));
                objChofer.setChof_brevete(rs.getString("chof_brevete"));
                objChofer.setChof_telef(rs.getInt("chof_telef"));
                objChofer.setChof_direcc(rs.getString("chof_direcc"));
                objChofer.setChof_est(rs.getInt("chof_est"));
                objChofer.setChof_usuadd(rs.getString("chof_usuadd"));
                objChofer.setChof_fecadd(rs.getTimestamp("chof_fecadd"));
                objChofer.setChof_usumod(rs.getString("chof_usumod"));
                objChofer.setChof_fecmod(rs.getTimestamp("chof_fecmod"));
                objChofer.setSuc_id(rs.getInt("suc_id"));
                objChofer.setEmp_id(rs.getInt("emp_id"));
                objlstChofer.add(objChofer);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstChofer.getSize() + " registro(s)");
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
        return objChofer;
    }

    public Chofer existeDNI(String dni) throws SQLException {
        String SQL_REPDNI;
        String s_validadni = dni.isEmpty() ? "" : " and t.chof_nroiden=" + dni + " ";

        SQL_REPDNI = "select t.chof_key,t.chof_id, chof_razsoc ,t.chof_nroiden from v_listachofer t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "t.suc_id=" + objUsuCredential.getCodsuc() + s_validadni + " order by t.chof_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_REPDNI);
            objChofer = null;
            while (rs.next()) {
                objChofer = new Chofer();
                objChofer.setChof_key(rs.getInt("chof_key"));
                objChofer.setChof_id(rs.getString("chof_id"));
                objChofer.setChof_razsoc(rs.getString("chof_razsoc"));
                objChofer.setChof_nroiden(rs.getString("chof_nroiden") == null ? "" : rs.getString("chof_nroiden"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el dni es" + objChofer.getChof_nroiden());
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
        return objChofer;
    }

    public Chofer existeRuc(String ruc) throws SQLException {
        String SQL_REPRUC;
        String s_validaruc = ruc.isEmpty() ? "" : " and t.chof_ruc=" + ruc + " ";

        SQL_REPRUC = "select t.chof_key,t.chof_id, chof_razsoc ,t.chof_ruc from v_listachofer t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "t.suc_id=" + objUsuCredential.getCodsuc() + s_validaruc + " order by t.chof_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_REPRUC);
            objChofer = null;
            while (rs.next()) {
                objChofer = new Chofer();
                objChofer.setChof_key(rs.getInt("chof_key"));
                objChofer.setChof_id(rs.getString("chof_id"));
                objChofer.setChof_razsoc(rs.getString("chof_razsoc"));
                objChofer.setChof_ruc(rs.getString("chof_ruc") == null ? "" : rs.getString("chof_ruc"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el ruc  es" + objChofer.getChof_ruc());
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
        return objChofer;
    }

}
