package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.NotaInterCab;
import org.me.gj.model.logistica.procesos.NotaInterDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotaIntercambio {
    //Instancias de Objetos
    ListModelList<NotaInterCab> objlstNotaInterCab;
    ListModelList<NotaInterDet> objlstNotaInterDet;
    NotaInterCab objNotaInterCab;
    NotaInterDet objNotaInterDet;
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
    private static final Logger LOGGER = Logger.getLogger(DaoNotaIntercambio.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarNotaInterCab(NotaInterCab objNotaInterCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        String nrid;
        cst = null;
        String SQL_INSERTAR_NOTAINTERCAB = "{call pack_tnotainter.p_insertarNotaInterCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTAINTERCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaInterCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterCab.getSuc_id());
            cst.setLong("nic_provid", objNotaInterCab.getNic_provid());
            cst.setInt("nic_lpcid", objNotaInterCab.getNic_lpcid());
            cst.setLong("n_cli_key", objNotaInterCab.getCli_key());
            cst.setString("c_cli_id", objNotaInterCab.getCli_id());
            cst.setLong("n_clidir_id", objNotaInterCab.getClidir_id());
            cst.setDate("d_ni_fecemi", new java.sql.Date(objNotaInterCab.getNi_fecemi().getTime()));
            cst.setDate("d_ni_fecent", new java.sql.Date(objNotaInterCab.getNi_fecent().getTime()));
            cst.setString("c_ni_zona", objNotaInterCab.getNi_zona());
            cst.setInt("n_ni_motcam", objNotaInterCab.getNi_motcam());
            cst.setInt("n_ni_sup", objNotaInterCab.getNi_sup());
            cst.setInt("n_ni_vend", objNotaInterCab.getNi_vend());
            cst.setInt("n_ni_trans", objNotaInterCab.getNi_trans());
            cst.setInt("n_ni_hor", objNotaInterCab.getNi_hor());
            cst.setString("c_ni_usuadd", objNotaInterCab.getNi_usuadd());
            cst.setString("c_ni_pcadd", objNotaInterCab.getNi_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.registerOutParameter("c_ni_id", java.sql.Types.VARCHAR);
            cst.execute();
            nrid = cst.getString("c_ni_id");
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo una Nota de Intercambio con Codigo " + nrid);
        } catch (SQLException e) {
            nrid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            nrid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, nrid);
    }

    public ParametrosSalida modificarNotaInterCab(NotaInterCab objNotaInterCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTAINTERCAB = "{call pack_tnotainter.p_modificarNotaInterCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTAINTERCAB);
            cst.clearParameters();
            cst.setLong("n_ni_key", objNotaInterCab.getNi_key());
            cst.setInt("n_emp_id", objNotaInterCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterCab.getSuc_id());
            cst.setLong("nic_provid", objNotaInterCab.getNic_provid());
            cst.setInt("nic_lpcid", objNotaInterCab.getNic_lpcid());
            cst.setString("c_cli_id", objNotaInterCab.getCli_id());
            cst.setLong("n_cli_key", objNotaInterCab.getCli_key());
            cst.setLong("n_clidir_id", objNotaInterCab.getClidir_id());
            cst.setDate("d_ni_fecemi", new java.sql.Date(objNotaInterCab.getNi_fecemi().getTime()));
            cst.setDate("d_ni_fecent", new java.sql.Date(objNotaInterCab.getNi_fecent().getTime()));
            cst.setString("c_ni_zona", objNotaInterCab.getNi_zona());
            cst.setInt("n_ni_motcam", objNotaInterCab.getNi_motcam());
            cst.setInt("n_ni_sup", objNotaInterCab.getNi_sup());
            cst.setInt("n_ni_vend", objNotaInterCab.getNi_vend());
            cst.setInt("n_ni_trans", objNotaInterCab.getNi_trans());
            cst.setInt("n_ni_hor", objNotaInterCab.getNi_hor());
            cst.setString("c_ni_usumod", objNotaInterCab.getNi_usumod());
            cst.setString("c_ni_pcmod", objNotaInterCab.getNi_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se Modifico la Nota de Intercambio con codigo " + objNotaInterCab.getNi_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la modificacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarNotaInterCab(NotaInterCab objNotaInterCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTAINTERCAB = "{call pack_tnotainter.p_eliminarNotaInterCab(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTAINTERCAB);
            cst.clearParameters();
            cst.setLong("n_ni_key", objNotaInterCab.getNi_key());
            cst.setInt("n_emp_id", objNotaInterCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterCab.getSuc_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Nota de Intercambio con Codigo " + objNotaInterCab.getNi_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la eliminacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarNotaInterDet(NotaInterDet objNotaInterDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_NOTAINTERDET = "{call pack_tnotainter.p_insertarNotaInterDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTAINTERDET);
            cst.clearParameters();
            cst.setLong("n_ni_key", objNotaInterDet.getNi_key());
            cst.setInt("n_emp_id", objNotaInterDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterDet.getSuc_id());
            cst.setInt("n_nid_tipdoc", objNotaInterDet.getNid_tipdoc());
            cst.setString("c_nid_serie", objNotaInterDet.getNid_serie());
            cst.setString("c_nid_docref", objNotaInterDet.getNid_doc());
            cst.setString("c_nid_proidsal", objNotaInterDet.getNid_proidsal());
            cst.setInt("n_nid_cantsale", objNotaInterDet.getNid_cantsale());
            cst.setInt("n_nid_cantsalf", objNotaInterDet.getNid_cantsalf());
            cst.setInt("n_nid_cantsaltot", objNotaInterDet.getNid_cantsaltot());
            cst.setString("c_nid_proident", objNotaInterDet.getNid_proident());
            cst.setInt("n_nid_cantente", objNotaInterDet.getNid_cantente());
            cst.setInt("n_nid_cantentf", objNotaInterDet.getNid_cantentf());
            cst.setInt("n_nid_cantenttot", objNotaInterDet.getNid_cantenttot());
            cst.setString("c_nid_glosa", objNotaInterDet.getNid_glosa());
            cst.setString("c_nid_usuadd", objNotaInterDet.getNid_usuadd());
            cst.setString("c_nid_pcadd", objNotaInterDet.getNid_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto a la Nota de Intercambio " + objNotaInterDet.getNi_key());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
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

    public ParametrosSalida modificarNotaInterDet(NotaInterDet objNotaInterDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTAINTERDET = "{call pack_tnotainter.p_modificarNotaInterDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTAINTERDET);
            cst.clearParameters();
            cst.setLong("n_ni_key", objNotaInterDet.getNi_key());
            cst.setInt("n_emp_id", objNotaInterDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterDet.getSuc_id());
            cst.setLong("n_nid_item", objNotaInterDet.getNid_item());
            cst.setInt("n_nid_tipdoc", objNotaInterDet.getNid_tipdoc());
            cst.setString("c_nid_serie", objNotaInterDet.getNid_serie());
            cst.setString("c_nid_docref", objNotaInterDet.getNid_doc());
            cst.setString("c_nid_proidsal", objNotaInterDet.getNid_proidsal());
            cst.setInt("n_nid_cantsale", objNotaInterDet.getNid_cantsale());
            cst.setInt("n_nid_cantsalf", objNotaInterDet.getNid_cantsalf());
            cst.setInt("n_nid_cantsaltot", objNotaInterDet.getNid_cantsaltot());
            cst.setString("c_nid_proident", objNotaInterDet.getNid_proident());
            cst.setInt("n_nid_cantente", objNotaInterDet.getNid_cantente());
            cst.setInt("n_nid_cantentf", objNotaInterDet.getNid_cantentf());
            cst.setInt("n_nid_cantenttot", objNotaInterDet.getNid_cantenttot());
            cst.setString("c_nid_glosa", objNotaInterDet.getNid_glosa());
            cst.setString("c_nid_usumod", objNotaInterDet.getNid_usuadd());
            cst.setString("c_nid_pcmod", objNotaInterDet.getNid_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto a la Nota de Intercambio " + objNotaInterDet.getNi_key());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
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

    public ParametrosSalida eliminarNotaInterDet(NotaInterDet objNotaInterDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTAINTERDET = "{call pack_tnotainter.p_eliminarNotaInterDet(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTAINTERDET);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaInterDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterDet.getSuc_id());
            cst.setLong("n_ni_key", objNotaInterDet.getNi_key());
            cst.setLong("n_nid_item", objNotaInterDet.getNid_item());
            cst.setString("c_nid_usumod", objNotaInterDet.getNid_usumod());
            cst.setString("c_nid_pcmod", objNotaInterDet.getNid_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un producto a la Nota de Intercambio " + objNotaInterDet.getNi_key());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la eliminacion porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida autorizarNotaIntercambio(NotaInterCab objNotaInterCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_AUTORIZAR_NOTAINTERCAB = "{call pack_tnotainter.p_autorizarNotaIntercambio(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_AUTORIZAR_NOTAINTERCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaInterCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaInterCab.getSuc_id());
            cst.setLong("n_ni_key", objNotaInterCab.getNi_key());
            cst.setInt("n_ni_sit", objNotaInterCab.getNi_sit());
            cst.setString("c_ni_autusuadd", objNotaInterCab.getNi_autusuadd());
            cst.setString("c_ni_autpcadd", objNotaInterCab.getNi_autpcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Autorizo una Nota de Intercambio con Codigo " + objNotaInterCab.getNi_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NotaInterCab> listaNotaInterCab(int emp_id, int suc_id, String ven_key, String sit, String fecinicial, String fecfinal, boolean ordVend) throws SQLException {
        String ordenar;
        String SQL_LISTANOTINTCAB = "";
        String fechaActual = Utilitarios.hoyAsString();
        if (ordVend) {
            ordenar = "t.ni_vend, ";
        } else {
            ordenar = "";
        }
        //fecha inicial lleno, fecha final lleno, vendedor lleno 
        if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_vend like '" + ven_key
                    + "' and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')" + " and to_date('" + fecfinal + "','DD/MM/YYYY') order by " + ordenar + "t.ni_key";

        }//fecha inicial vacio, fecha final vacio, vendedor vacio
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_vend like '" + ven_key
                    + "' and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by " + ordenar + "t.ni_key";

        }//fecha inicial llena, fecha final llena, vendedor vacio 
        else if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY') and to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.ni_key";

        }//fecha inicial llena, fecha final vacio, vendedor lleno  
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_vend like '" + ven_key
                    + "' and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.ni_key";
        } //fecha inicial vacio, fecha final llena, vendedor lleno 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_vend like '" + ven_key
                    + "' and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.ni_key";

        } //fecha inicial llena, fecha final vacio, vendedor vacio 
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.ni_key";

        } //fecha inicial vacia, fecha final vacio, vendedor lleno 
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_vend like '" + ven_key
                    + "' and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " order by " + ordenar + "t.ni_key";

        } //fecha inicial vacia, fecha final lleno, vendedor vacio 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_LISTANOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.ni_sit like '" + sit + "' and t.ni_est<>0 and t.ni_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY') order by " + ordenar + "t.ni_key";
        }
        objlstNotaInterCab = null;
        objlstNotaInterCab = new ListModelList<NotaInterCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTANOTINTCAB);
            while (rs.next()) {
                objNotaInterCab = new NotaInterCab();
                objNotaInterCab.setNi_key(rs.getLong("ni_key"));
                objNotaInterCab.setNi_id(rs.getString("ni_id"));
                objNotaInterCab.setEmp_id(rs.getInt("emp_id"));
                objNotaInterCab.setSuc_id(rs.getInt("suc_id"));
                objNotaInterCab.setNic_provid(rs.getLong("ni_provid"));
                objNotaInterCab.setNic_provrazsoc(rs.getString("prov_razsoc"));
                objNotaInterCab.setNic_lpcid(rs.getInt("ni_lpcid"));
                objNotaInterCab.setNic_lpcdes(rs.getString("lp_des"));
                objNotaInterCab.setCli_id(rs.getString("cli_id"));
                objNotaInterCab.setCli_key(rs.getLong("cli_key"));
                objNotaInterCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotaInterCab.setClidir_id(rs.getLong("clidir_id"));
                objNotaInterCab.setClidir_direcc(rs.getString("clidir_direc"));
                objNotaInterCab.setNi_fecemi(rs.getDate("ni_fecemi"));
                objNotaInterCab.setNi_fecent(rs.getDate("ni_fecent"));
                objNotaInterCab.setNi_periodo(rs.getInt("ni_periodo"));
                objNotaInterCab.setNi_sit(rs.getInt("ni_sit"));
                objNotaInterCab.setNi_motrec(rs.getInt("ni_motrec"));
                objNotaInterCab.setMrec_des(rs.getString("mrec_des"));
                objNotaInterCab.setNi_est(rs.getInt("ni_est"));
                objNotaInterCab.setNi_zona(rs.getString("ni_zona"));
                objNotaInterCab.setZon_des(rs.getString("zon_des"));
                objNotaInterCab.setNi_motcam(rs.getInt("ni_motcam"));
                objNotaInterCab.setMcam_des(rs.getString("mcam_des"));
                objNotaInterCab.setNi_sup(rs.getInt("ni_sup"));
                objNotaInterCab.setNi_vend(rs.getInt("ni_vend"));
                objNotaInterCab.setVen_apenom(rs.getString("ven_apenom"));
                objNotaInterCab.setNi_trans(rs.getInt("ni_trans"));
                objNotaInterCab.setTrans_alias(rs.getString("trans_alias"));
                objNotaInterCab.setNi_hor(rs.getInt("ni_hor"));
                objNotaInterCab.setHor_des(rs.getString("hor_des"));
                objNotaInterCab.setNi_tipnotasal(rs.getString("ni_tipnotasal"));
                objNotaInterCab.setNi_notasal(rs.getString("ni_notasal"));
                objNotaInterCab.setNi_tipnotaent(rs.getString("ni_tipnotaent"));
                objNotaInterCab.setNi_notaent(rs.getString("ni_notaent"));
                objNotaInterCab.setNi_nroreg(rs.getString("ni_nroreg"));
                objNotaInterCab.setNi_nrodep(rs.getString("ni_nrodep"));
                objNotaInterCab.setNi_autusuadd(rs.getString("ni_autusuadd"));
                objNotaInterCab.setNi_autfecadd(rs.getDate("ni_autfecadd"));
                objNotaInterCab.setNi_autpcadd(rs.getString("ni_autpcadd"));
                objNotaInterCab.setNi_usuadd(rs.getString("ni_usuadd"));
                objNotaInterCab.setNi_fecadd(rs.getTimestamp("ni_fecadd"));
                objNotaInterCab.setNi_pcadd(rs.getString("ni_pcadd"));
                objNotaInterCab.setNi_usumod(rs.getString("ni_usumod"));
                objNotaInterCab.setNi_fecmod(rs.getTimestamp("ni_fecmod"));
                objNotaInterCab.setNi_pcmod(rs.getString("ni_pcmod"));
                objlstNotaInterCab.add(objNotaInterCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaInterCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstNotaInterCab;
    }

    public ListModelList<NotaInterDet> listaNotaInterDet(int emp_id, int suc_id, long ni_key) throws SQLException {
        String SQL_LISTANOTINTDET = "select * from v_listanotainterdet t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + " and t.nid_est<>0 and t.ni_key=" + ni_key;
        objlstNotaInterDet = null;
        objlstNotaInterDet = new ListModelList<NotaInterDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTANOTINTDET);
            while (rs.next()) {
                objNotaInterDet = new NotaInterDet();
                objNotaInterDet.setNi_key(rs.getLong("ni_key"));
                objNotaInterDet.setEmp_id(rs.getInt("emp_id"));
                objNotaInterDet.setSuc_id(rs.getInt("suc_id"));
                objNotaInterDet.setNid_item(rs.getLong("nid_item"));
                objNotaInterDet.setNid_tipdoc(rs.getInt("nid_tipdoc"));
                objNotaInterDet.setNid_tipdocdes(rs.getString("nid_tipdocdes"));
                objNotaInterDet.setNid_serie(rs.getString("nid_serie"));
                objNotaInterDet.setNid_doc(rs.getString("nid_doc"));
                objNotaInterDet.setNid_est(rs.getInt("nid_est"));
                objNotaInterDet.setNid_indicador(rs.getString("nid_indicador"));
                objNotaInterDet.setPro_id(rs.getString("pro_id"));
                objNotaInterDet.setNid_precio(rs.getDouble("precio"));
                objNotaInterDet.setNid_lista(rs.getInt("lista"));
                objNotaInterDet.setPro_desdes(rs.getString("pro_desdes"));
                objNotaInterDet.setPro_des(rs.getString("pro_des"));
                objNotaInterDet.setPro_uniman(rs.getString("pro_uniman"));
                objNotaInterDet.setPro_presmin(rs.getInt("pro_presminven"));
                objNotaInterDet.setNid_cantent(rs.getInt("nid_cantent"));
                objNotaInterDet.setNid_cantfrac(rs.getInt("nid_cantfrac"));
                objNotaInterDet.setNid_canttot(rs.getInt("nid_canttot"));
                objNotaInterDet.setNid_cantmovent(rs.getInt("nid_cantmovent"));
                objNotaInterDet.setNid_cantmovsal(rs.getInt("nid_cantmovsal"));
                objNotaInterDet.setNid_cantrec(rs.getInt("nid_cantrec"));
                objNotaInterDet.setNid_glosa(rs.getString("nid_glosa"));
                objNotaInterDet.setNid_usuadd(rs.getString("nid_usuadd"));
                objNotaInterDet.setNid_fecadd(rs.getDate("nid_fecadd"));
                objNotaInterDet.setNid_pcadd(rs.getString("nid_pcadd"));
                objNotaInterDet.setNid_usumod(rs.getString("nid_usumod"));
                objNotaInterDet.setNid_fecmod(rs.getDate("nid_fecmod"));
                objNotaInterDet.setNid_pcmod(rs.getString("nid_pcmod"));
                objlstNotaInterDet.add(objNotaInterDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaInterDet.getSize() + " registro(s)");
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstNotaInterDet;
    }

    public ListModelList<NotaInterCab> listaNotInterCabApro(int emp_id, int suc_id, String cli_key, String ven_id, String sitIng, String sitApro, String sitRec, String fecinicial, String fecfinal) throws SQLException {
        String SQL_NOTINTCAB = "select * from v_listanotaintercab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.cli_key like '" + cli_key
                + "' and t.ni_vend like '" + ven_id + "' and t.ni_sit in ('" + sitIng + "','" + sitApro + "','" + sitRec + "') and t.ni_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                + " and to_date('" + fecfinal + "','DD/MM/YYYY') and t.ni_est=1 order by t.ni_key";
        objlstNotaInterCab = null;
        objlstNotaInterCab = new ListModelList<NotaInterCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTINTCAB);
            while (rs.next()) {
                objNotaInterCab = new NotaInterCab();
                objNotaInterCab.setNi_key(rs.getLong("ni_key"));
                objNotaInterCab.setNi_id(rs.getString("ni_id"));
                objNotaInterCab.setEmp_id(rs.getInt("emp_id"));
                objNotaInterCab.setSuc_id(rs.getInt("suc_id"));
                objNotaInterCab.setCli_id(rs.getString("cli_id"));
                objNotaInterCab.setCli_key(rs.getLong("cli_key"));
                objNotaInterCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotaInterCab.setClidir_id(rs.getLong("clidir_id"));
                objNotaInterCab.setClidir_direcc(rs.getString("clidir_direc"));
                objNotaInterCab.setNic_provid(rs.getLong("ni_provid"));
                objNotaInterCab.setNic_provrazsoc(rs.getString("prov_razsoc"));
                objNotaInterCab.setNic_lpcid(rs.getInt("ni_lpcid"));
                objNotaInterCab.setNic_lpcdes(rs.getString("lp_des"));
                objNotaInterCab.setNi_fecemi(rs.getDate("ni_fecemi"));
                objNotaInterCab.setNi_fecent(rs.getDate("ni_fecent"));
                objNotaInterCab.setNi_periodo(rs.getInt("ni_periodo"));
                objNotaInterCab.setNi_sit(rs.getInt("ni_sit"));
                objNotaInterCab.setNi_motrec(rs.getInt("ni_motrec"));
                objNotaInterCab.setMrec_des(rs.getString("mrec_des"));
                objNotaInterCab.setNi_est(rs.getInt("ni_est"));
                objNotaInterCab.setNi_zona(rs.getString("ni_zona"));
                objNotaInterCab.setZon_des(rs.getString("zon_des"));
                objNotaInterCab.setNi_motcam(rs.getInt("ni_motcam"));
                objNotaInterCab.setMcam_des(rs.getString("mcam_des"));
                objNotaInterCab.setNi_sup(rs.getInt("ni_sup"));
                objNotaInterCab.setNi_vend(rs.getInt("ni_vend"));
                objNotaInterCab.setVen_apenom(rs.getString("ven_apenom"));
                objNotaInterCab.setNi_trans(rs.getInt("ni_trans"));
                objNotaInterCab.setTrans_alias(rs.getString("trans_alias"));
                objNotaInterCab.setNi_hor(rs.getInt("ni_hor"));
                objNotaInterCab.setHor_des(rs.getString("hor_des"));
                objNotaInterCab.setNi_tipnotasal(rs.getString("ni_tipnotasal"));
                objNotaInterCab.setNi_notasal(rs.getString("ni_notasal"));
                objNotaInterCab.setNi_tipnotaent(rs.getString("ni_tipnotaent"));
                objNotaInterCab.setNi_notaent(rs.getString("ni_notaent"));
                objNotaInterCab.setNi_nroreg(rs.getString("ni_nroreg"));
                objNotaInterCab.setNi_nrodep(rs.getString("ni_nrodep"));
                objNotaInterCab.setNi_usuadd(rs.getString("ni_usuadd"));
                objNotaInterCab.setNi_fecadd(rs.getTimestamp("ni_fecadd"));
                objNotaInterCab.setNi_pcadd(rs.getString("ni_pcadd"));
                objNotaInterCab.setNi_usumod(rs.getString("ni_usumod"));
                objNotaInterCab.setNi_fecmod(rs.getTimestamp("ni_fecmod"));
                objNotaInterCab.setNi_pcmod(rs.getString("ni_pcmod"));
                objlstNotaInterCab.add(objNotaInterCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaInterCab.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstNotaInterCab;
    }
}
