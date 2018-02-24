package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.Repartidor;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoRepartidor {

    //Instancia de Objetos
    ListModelList<Repartidor> objlstRepartidor;
    Repartidor objRepartidor;
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
    public ParametrosSalida insertarRepartidor(Repartidor objRepartidor) throws SQLException {
        String rep_apepat = objRepartidor.getRep_apepat();
        String rep_apemat = objRepartidor.getRep_apemat();
        String rep_nom = objRepartidor.getRep_nom();
        String rep_dni = objRepartidor.getRep_dni();
        Date rep_fecnac = objRepartidor.getRep_fecnac();
        int rep_telef = objRepartidor.getRep_telef();
        String rep_direcc = objRepartidor.getRep_direcc();
        int rep_est = objRepartidor.getRep_est();
        String rep_usuadd = objRepartidor.getRep_usuadd();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_CHOFER = "{call pack_trepartidor.p_insertarRepartidor(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setString(3, rep_apepat);
            cst.setString(4, rep_apemat);
            cst.setString(5, rep_nom);
            cst.setString(6, rep_dni);
            cst.setDate(7, new java.sql.Date(rep_fecnac.getTime()));
            cst.setInt(8, rep_telef);
            cst.setString(9, rep_direcc);
            cst.setInt(10, rep_est);
            cst.setString(11, rep_usuadd);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + objRepartidor.getRep_id());
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

    public ParametrosSalida actualizarRepartidor(Repartidor objRepartidor) throws SQLException {
        int rep_key = objRepartidor.getRep_key();
        String rep_apepat = objRepartidor.getRep_apepat();
        String rep_apemat = objRepartidor.getRep_apemat();
        String rep_nom = objRepartidor.getRep_nom();
        String rep_dni = objRepartidor.getRep_dni();
        Date rep_fecnac = objRepartidor.getRep_fecnac();
        int rep_telef = objRepartidor.getRep_telef();
        String rep_direcc = objRepartidor.getRep_direcc();
        int rep_est = objRepartidor.getRep_est();
        String rep_usuadd = objRepartidor.getRep_usuadd();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_CHOFER = "{call pack_trepartidor.p_actualizarRepartidor(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, rep_key);
            cst.setString(4, rep_apepat);
            cst.setString(5, rep_apemat);
            cst.setString(6, rep_nom);
            cst.setString(7, rep_dni);
            cst.setDate(8, new java.sql.Date(rep_fecnac.getTime()));
            cst.setInt(9, rep_telef);
            cst.setString(10, rep_direcc);
            cst.setInt(11, rep_est);
            cst.setString(12, rep_usuadd);
            cst.registerOutParameter(13, java.sql.Types.NUMERIC);
            cst.registerOutParameter(14, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(14);
            i_flagErrorBD = cst.getInt(13);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + objRepartidor.getRep_id());
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

    public ParametrosSalida eliminarRepartidor(Repartidor objRepartidor) throws SQLException {
        int rep_key = objRepartidor.getRep_key();
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_CHOFER = "{call pack_trepartidor.p_eliminarRepartidor(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CHOFER);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, rep_key);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + objRepartidor.getRep_id());
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
    public ListModelList<Repartidor> listaRepartidor(int estado) throws SQLException {
        String SQL_LISTA_CHOFER;
        if (estado == 0) {
            SQL_LISTA_CHOFER = "select * from v_listarepartidor t where t.rep_est<>" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.rep_key";
        } else {
            SQL_LISTA_CHOFER = "select * from v_listarepartidor t where t.rep_est=" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.rep_key";
        }
        objlstRepartidor = new ListModelList<Repartidor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CHOFER);
            while (rs.next()) {
                objRepartidor = new Repartidor();
                objRepartidor.setRep_key(rs.getInt("rep_key"));
                objRepartidor.setRep_id(rs.getString("rep_id"));
                objRepartidor.setRep_apepat(rs.getString("rep_apepat"));
                objRepartidor.setRep_apemat(rs.getString("rep_apemat"));
                objRepartidor.setRep_nom(rs.getString("rep_nom"));
                objRepartidor.setS_nomcompleto(rs.getString("rep_nomcompleto"));
                objRepartidor.setRep_dni(rs.getString("rep_dni"));
                objRepartidor.setRep_fecnac(rs.getDate("rep_fecnac"));
                objRepartidor.setRep_telef(rs.getInt("rep_telef"));
                objRepartidor.setRep_direcc(rs.getString("rep_direcc"));
                objRepartidor.setRep_est(rs.getInt("rep_est"));
                objRepartidor.setRep_usuadd(rs.getString("rep_usuadd"));
                objRepartidor.setRep_fecadd(rs.getTimestamp("rep_fecadd"));
                objRepartidor.setRep_usumod(rs.getString("rep_usumod"));
                objRepartidor.setRep_fecmod(rs.getTimestamp("rep_fecmod"));
                objRepartidor.setSuc_id(rs.getInt("suc_id"));
                objRepartidor.setEmp_id(rs.getInt("emp_id"));
                objlstRepartidor.add(objRepartidor);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRepartidor.getSize() + " registro(s)");
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
        return objlstRepartidor;
    }

    public ListModelList<Repartidor> busquedaRepartidores(int seleccion, String busqueda, int estado) throws SQLException {
        String SQL_LISTA_CHOFER = "";
        int em_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();

        if (estado == 3) { // TODOS 
            if (seleccion == 0) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where  t.emp_id='" + em_id + "' and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 1) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_id like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 2) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_nomcompleto like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 3) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_dni like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            }
        } else {
            if (seleccion == 0) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_est=" + estado + " "
                        + " and t.emp_id='" + em_id + "' and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 1) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_est=" + estado + " "
                        + " and t.rep_id like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 2) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_est=" + estado + " "
                        + " and t.rep_nomcompleto like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            } else if (seleccion == 3) {
                SQL_LISTA_CHOFER = " select * from v_listarepartidor t"
                        + " where t.rep_est=" + estado + " "
                        + " and t.rep_dni like '" + busqueda + "' and t.emp_id='" + em_id + "' "
                        + " and t.suc_id='" + suc_id + "'"
                        + " order by t.rep_key";
            }
        }

        P_WHERE = SQL_LISTA_CHOFER;
        objlstRepartidor = new ListModelList<Repartidor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CHOFER);
            while (rs.next()) {
                objRepartidor = new Repartidor();
                objRepartidor.setRep_key(rs.getInt("rep_key"));
                objRepartidor.setRep_id(rs.getString("rep_id"));
                objRepartidor.setRep_apepat(rs.getString("rep_apepat"));
                objRepartidor.setRep_apemat(rs.getString("rep_apemat"));
                objRepartidor.setRep_nom(rs.getString("rep_nom"));
                objRepartidor.setS_nomcompleto(rs.getString("rep_nomcompleto"));
                objRepartidor.setRep_dni(rs.getString("rep_dni"));
                objRepartidor.setRep_fecnac(rs.getDate("rep_fecnac"));
                objRepartidor.setRep_telef(rs.getInt("rep_telef"));
                objRepartidor.setRep_direcc(rs.getString("rep_direcc"));
                objRepartidor.setRep_est(rs.getInt("rep_est"));
                objRepartidor.setRep_usuadd(rs.getString("rep_usuadd"));
                objRepartidor.setRep_fecadd(rs.getTimestamp("rep_fecadd"));
                objRepartidor.setRep_usumod(rs.getString("rep_usumod"));
                objRepartidor.setRep_fecmod(rs.getTimestamp("rep_fecmod"));
                objRepartidor.setSuc_id(rs.getInt("suc_id"));
                objRepartidor.setEmp_id(rs.getInt("emp_id"));
                objlstRepartidor.add(objRepartidor);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRepartidor.getSize() + " registro(s)");
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
        return objlstRepartidor;
    }

    public Repartidor busquedaRepartidorxCodigo(int rep_key) throws SQLException {
        String SQL_BUSQUEDA_REPARTIDOR = "";

        SQL_BUSQUEDA_REPARTIDOR = " select * from v_listarepartidor t where t.rep_est= 1"
                + " and t.rep_key = " + rep_key + " "
                + " and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + " and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                + " order by t.rep_key";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_REPARTIDOR);
            while (rs.next()) {
                objRepartidor = new Repartidor();
                objRepartidor.setRep_key(rs.getInt("rep_key"));
                objRepartidor.setRep_id(rs.getString("rep_id"));
                objRepartidor.setRep_apepat(rs.getString("rep_apepat"));
                objRepartidor.setRep_apemat(rs.getString("rep_apemat"));
                objRepartidor.setRep_nom(rs.getString("rep_nom"));
                objRepartidor.setS_nomcompleto(rs.getString("rep_nomcompleto"));
                objRepartidor.setRep_dni(rs.getString("rep_dni"));
                objRepartidor.setRep_fecnac(rs.getDate("rep_fecnac"));
                objRepartidor.setRep_telef(rs.getInt("rep_telef"));
                objRepartidor.setRep_direcc(rs.getString("rep_direcc"));
                objRepartidor.setRep_est(rs.getInt("rep_est"));
                objRepartidor.setRep_usuadd(rs.getString("rep_usuadd"));
                objRepartidor.setRep_fecadd(rs.getTimestamp("rep_fecadd"));
                objRepartidor.setRep_usumod(rs.getString("rep_usumod"));
                objRepartidor.setRep_fecmod(rs.getTimestamp("rep_fecmod"));
                objRepartidor.setSuc_id(rs.getInt("suc_id"));
                objRepartidor.setEmp_id(rs.getInt("emp_id"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRepartidor.getSize() + " registro(s)");
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
        return objRepartidor;
    }

    public Repartidor existeDNI(String dni) throws SQLException {
        String SQL_REPDNI;

        SQL_REPDNI = "select t.rep_key,t.rep_id, rep_nomcompleto ,t.rep_dni from v_listarepartidor t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.rep_dni=" + dni + " order by t.rep_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_REPDNI);
            objRepartidor = null;
            while (rs.next()) {
                objRepartidor = new Repartidor();
                objRepartidor.setRep_key(rs.getInt("rep_key"));
                objRepartidor.setRep_id(rs.getString("rep_id"));
                objRepartidor.setS_nomcompleto(rs.getString("rep_nomcompleto"));
                objRepartidor.setRep_dni(rs.getString("rep_dni"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el dni cuyo numero es" + objRepartidor.getRep_dni());
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
        return objRepartidor;
    }

}
