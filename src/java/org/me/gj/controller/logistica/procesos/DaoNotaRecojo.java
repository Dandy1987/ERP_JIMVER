package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.NotaRecojoCab;
import org.me.gj.model.logistica.procesos.NotaRecojoDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotaRecojo {

    //Instancias de Objetos
    ListModelList<NotaRecojoCab> objlstNotaRecojoCab;
    ListModelList<NotaRecojoDet> objlstNotaRecojoDet;
    NotaRecojoCab objNotaRecojoCab;
    NotaRecojoDet objNotaRecojoDet;
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
    private static final Logger LOGGER = Logger.getLogger(DaoNotaRecojo.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarNotaRecojoCab(NotaRecojoCab objNotaRecojoCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        String nrid;
        cst = null;
        String SQL_INSERTAR_NOTARECOJOCAB = "{call pack_tnotarecojo.p_insertarNotaRecojoCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTARECOJOCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaRecojoCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoCab.getSuc_id());
            cst.setString("c_cli_id", objNotaRecojoCab.getCli_id());
            cst.setLong("n_cli_key", objNotaRecojoCab.getCli_key());
            cst.setLong("n_clidir_id", objNotaRecojoCab.getClidir_id());
            cst.setDate("d_nr_fecemi", new java.sql.Date(objNotaRecojoCab.getNr_fecemi().getTime()));
            cst.setDate("d_nr_fecent", new java.sql.Date(objNotaRecojoCab.getNr_fecent().getTime()));
            cst.setString("c_nr_zona", objNotaRecojoCab.getNr_zona());
            cst.setInt("n_nr_motcam", objNotaRecojoCab.getNr_motcam());
            cst.setInt("n_nr_sup", objNotaRecojoCab.getNr_sup());
            cst.setInt("n_nr_vend", objNotaRecojoCab.getNr_vend());
            cst.setInt("n_nr_trans", objNotaRecojoCab.getNr_trans());
            cst.setInt("n_nr_hor", objNotaRecojoCab.getNr_hor());
            cst.setString("c_nr_usuadd", objNotaRecojoCab.getNr_usuadd());
            cst.setString("c_nr_pcadd", objNotaRecojoCab.getNr_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.registerOutParameter("c_nr_id", java.sql.Types.VARCHAR);
            cst.execute();
            nrid = cst.getString("c_nr_id");
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo una Nota de Recojo con Codigo " + nrid);
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

    public ParametrosSalida modificarNotaRecojoCab(NotaRecojoCab objNotaRecojoCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTARECOJOCAB = "{call pack_tnotarecojo.p_modificarNotaRecojoCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTARECOJOCAB);
            cst.clearParameters();
            cst.setLong("n_nr_key", objNotaRecojoCab.getNr_key());
            cst.setInt("n_emp_id", objNotaRecojoCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoCab.getSuc_id());
            cst.setString("c_cli_id", objNotaRecojoCab.getCli_id());
            cst.setLong("n_cli_key", objNotaRecojoCab.getCli_key());
            cst.setLong("n_clidir_id", objNotaRecojoCab.getClidir_id());
            cst.setDate("d_nr_fecemi", new java.sql.Date(objNotaRecojoCab.getNr_fecemi().getTime()));
            cst.setDate("d_nr_fecent", new java.sql.Date(objNotaRecojoCab.getNr_fecent().getTime()));
            cst.setString("c_nr_zona", objNotaRecojoCab.getNr_zona());
            cst.setInt("n_nr_motcam", objNotaRecojoCab.getNr_motcam());
            cst.setInt("n_nr_sup", objNotaRecojoCab.getNr_sup());
            cst.setInt("n_nr_vend", objNotaRecojoCab.getNr_vend());
            cst.setInt("n_nr_trans", objNotaRecojoCab.getNr_trans());
            cst.setInt("n_nr_hor", objNotaRecojoCab.getNr_hor());
            cst.setString("c_nr_usumod", objNotaRecojoCab.getNr_usumod());
            cst.setString("c_nr_pcmod", objNotaRecojoCab.getNr_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se Modifico la Nota de Recojo con codigo " + objNotaRecojoCab.getNr_id());
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

    public ParametrosSalida eliminarNotaRecojoCab(NotaRecojoCab objNotaRecojoCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTARECOJOCAB = "{call pack_tnotarecojo.p_eliminarNotaRecojoCab(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTARECOJOCAB);
            cst.clearParameters();
            cst.setLong("n_nr_key", objNotaRecojoCab.getNr_key());
            cst.setInt("n_emp_id", objNotaRecojoCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoCab.getSuc_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Nota de Recojo con Codigo " + objNotaRecojoCab.getNr_id());
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

    public ParametrosSalida insertarNotaRecojoDet(NotaRecojoDet objNotaRecojoDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_NOTARECOJODET = "{call pack_tnotarecojo.p_insertarNotaRecojoDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTARECOJODET);
            cst.clearParameters();
            cst.setLong("n_nr_key", objNotaRecojoDet.getNr_key());
            cst.setInt("n_emp_id", objNotaRecojoDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoDet.getSuc_id());
            cst.setInt("n_nrd_tipdoc", objNotaRecojoDet.getNrd_tipdoc());
            cst.setString("c_nrd_serie", objNotaRecojoDet.getNrd_serie());
            cst.setString("c_nrd_docref", objNotaRecojoDet.getNrd_doc());
            cst.setInt("n_nrd_cantent", objNotaRecojoDet.getNrd_cantent());
            cst.setInt("n_nrd_cantfrac", objNotaRecojoDet.getNrd_cantfrac());
            cst.setInt("n_nrd_canttot", objNotaRecojoDet.getNrd_canttot());
            cst.setString("c_pro_id", objNotaRecojoDet.getPro_id());
            cst.setString("c_nrd_glosa", objNotaRecojoDet.getNrd_glosa());
            cst.setString("c_nrd_usuadd", objNotaRecojoDet.getNrd_usuadd());
            cst.setString("c_nrd_pcadd", objNotaRecojoDet.getNrd_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto a la Nota de Recojo " + objNotaRecojoDet.getNr_key() + " con Codigo " + objNotaRecojoDet.getPro_id());
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

    public ParametrosSalida modificarNotaRecojoDet(NotaRecojoDet objNotaRecojoDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTARECOJODET = "{call pack_tnotarecojo.p_modificarNotaRecojoDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTARECOJODET);
            cst.clearParameters();
            cst.setLong("n_nr_key", objNotaRecojoDet.getNr_key());
            cst.setInt("n_emp_id", objNotaRecojoDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoDet.getSuc_id());
            cst.setLong("n_nrd_item", objNotaRecojoDet.getNrd_item());
            cst.setInt("n_nrd_tipdoc", objNotaRecojoDet.getNrd_tipdoc());
            cst.setString("c_nrd_serie", objNotaRecojoDet.getNrd_serie());
            cst.setString("c_nrd_docref", objNotaRecojoDet.getNrd_doc());
            cst.setInt("n_nrd_cantent", objNotaRecojoDet.getNrd_cantent());
            cst.setInt("n_nrd_cantfrac", objNotaRecojoDet.getNrd_cantfrac());
            cst.setInt("n_nrd_canttot", objNotaRecojoDet.getNrd_canttot());
            cst.setString("c_pro_id", objNotaRecojoDet.getPro_id());
            cst.setString("c_nrd_glosa", objNotaRecojoDet.getNrd_glosa());
            cst.setString("c_nrd_usumod", objNotaRecojoDet.getNrd_usumod());
            cst.setString("c_nrd_pcmod", objNotaRecojoDet.getNrd_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto a la Nota de Recojo " + objNotaRecojoDet.getNr_key() + " con Codigo " + objNotaRecojoDet.getPro_id());
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

    public ParametrosSalida eliminarNotaRecojoDet(NotaRecojoDet objNotaRecojoDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTARECOJODET = "{call pack_tnotarecojo.p_eliminarNotaRecojoDet(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTARECOJODET);
            cst.clearParameters();
            cst.setLong("n_nr_key", objNotaRecojoDet.getNr_key());
            cst.setInt("n_emp_id", objNotaRecojoDet.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoDet.getSuc_id());
            cst.setLong("n_nrd_item", objNotaRecojoDet.getNrd_item());
            cst.setString("c_nrd_usumod", objNotaRecojoDet.getNrd_usumod());
            cst.setString("c_nrd_pcmod", objNotaRecojoDet.getNrd_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un producto a la Nota de Recojo " + objNotaRecojoDet.getNr_key() + " con Codigo " + objNotaRecojoDet.getPro_id());
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

    public ParametrosSalida autorizarNotaRecojo(NotaRecojoCab objNotaRecojoCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_AUTORIZAR_NOTARECOJOCAB = "{call pack_tnotarecojo.p_autorizarNotaRecojo(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_AUTORIZAR_NOTARECOJOCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotaRecojoCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaRecojoCab.getSuc_id());
            cst.setLong("n_nr_key", objNotaRecojoCab.getNr_key());
            cst.setInt("n_nr_sit", objNotaRecojoCab.getNr_sit());
            cst.setString("c_nr_autusuadd", objNotaRecojoCab.getNr_autusuadd());
            cst.setString("c_nr_autpcadd", objNotaRecojoCab.getNr_autpcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Autorizo una Nota de Recojo con Codigo " + objNotaRecojoCab.getNr_id());
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
    public ListModelList<NotaRecojoCab> listaNotaRecojoCab(int emp_id, int suc_id, String ven_key, String sit, String fecinicial, String fecfinal, boolean ordVend) throws SQLException {
        String SQL_NOTARECOJOCAB = "";
        String fechaActual = Utilitarios.hoyAsString();
        String ordenar;
        if (ordVend) {
            ordenar = "t.nr_vend, ";
        } else {
            ordenar = "";
        }
        //fecha inicial lleno, fecha final lleno, vendedor lleno 
        if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_vend like '" + ven_key.trim()
                    + "' and t.nr_sit like '" + sit + "' and t.nr_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                    + " and to_date('" + fecfinal + "','DD/MM/YYYY') order by  " + ordenar + "t.nr_key";

        }//fecha inicial vacio, fecha final vacio, vendedor vacio
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_vend like '" + ven_key.trim()
                    + "' and t.nr_sit like '" + sit + "' and t.nr_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by " + ordenar + "t.nr_key";

        }//fecha inicial llena, fecha final llena, vendedor vacio 
        else if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.nr_sit like '" + sit + "' and t.nr_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY') and to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.nr_key";

        }//fecha inicial llena, fecha final vacio, vendedor lleno  
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_vend like '" + ven_key.trim()
                    + "' and t.nr_sit like '" + sit + "' and t.nr_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.nr_key";
        } //fecha inicial vacio, fecha final llena, vendedor lleno 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_vend like '" + ven_key.trim()
                    + "' and t.nr_sit like '" + sit + "' and t.nr_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.nr_key";

        } //fecha inicial llena, fecha final vacio, vendedor vacio 
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_sit like '" + sit + "' and t.nc_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.nr_key";

        } //fecha inicial vacia, fecha final vacio, vendedor lleno 
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_vend like '" + ven_key.trim()
                    + "' and t.nr_sit like '" + sit + "' and t.nr_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " order by " + ordenar + "t.nr_key";

        } //fecha inicial vacia, fecha final lleno, vendedor vacio 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTARECOJOCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nr_sit like '" + sit + "' and t.nr_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY') order by " + ordenar + "t.nr_key";
        }
        objlstNotaRecojoCab = null;
        objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTARECOJOCAB);
            while (rs.next()) {
                objNotaRecojoCab = new NotaRecojoCab();
                objNotaRecojoCab.setNr_key(rs.getLong("nr_key"));
                objNotaRecojoCab.setNr_id(rs.getString("nr_id"));
                objNotaRecojoCab.setEmp_id(rs.getInt("emp_id"));
                objNotaRecojoCab.setSuc_id(rs.getInt("suc_id"));
                objNotaRecojoCab.setCli_id(rs.getString("cli_id"));
                objNotaRecojoCab.setCli_key(rs.getLong("cli_key"));
                objNotaRecojoCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotaRecojoCab.setClidir_id(rs.getLong("clidir_id"));
                objNotaRecojoCab.setClidir_direcc(rs.getString("clidir_direc"));
                objNotaRecojoCab.setNr_fecemi(rs.getDate("nr_fecemi"));
                objNotaRecojoCab.setNr_fecent(rs.getDate("nr_fecent"));
                objNotaRecojoCab.setNr_periodo(rs.getInt("nr_periodo"));
                objNotaRecojoCab.setNr_sit(rs.getInt("nr_sit"));
                objNotaRecojoCab.setDessituacion(rs.getString("nr_sitdes").length() < 10 ? rs.getString("nr_sitdes") : rs.getString("nr_sitdes").substring(0, 10));
                objNotaRecojoCab.setNr_motrec(rs.getInt("nr_motrec"));
                objNotaRecojoCab.setMrec_des(rs.getString("mrec_des"));
                objNotaRecojoCab.setNr_est(rs.getInt("nr_est"));
                objNotaRecojoCab.setNr_zona(rs.getString("nr_zona"));
                objNotaRecojoCab.setZon_des(rs.getString("zon_des"));
                objNotaRecojoCab.setNr_motcam(rs.getInt("nr_motcam"));
                objNotaRecojoCab.setMcam_des(rs.getString("mcam_des"));
                objNotaRecojoCab.setNr_sup(rs.getInt("nr_sup"));
                objNotaRecojoCab.setNr_vend(rs.getInt("nr_vend"));
                objNotaRecojoCab.setVen_nom(rs.getString("ven_nom"));
                objNotaRecojoCab.setNr_trans(rs.getInt("nr_trans"));
                objNotaRecojoCab.setTrans_id(rs.getString("trans_id"));
                objNotaRecojoCab.setTrans_alias(rs.getString("trans_alias"));
                objNotaRecojoCab.setNr_hor(rs.getInt("nr_hor"));
                objNotaRecojoCab.setHor_des(rs.getString("hor_des"));
                objNotaRecojoCab.setNr_tipnotaent(rs.getString("nr_tipnotaent"));
                objNotaRecojoCab.setNr_notaent(rs.getString("nr_notaent"));
                objNotaRecojoCab.setNr_nroreg(rs.getString("nr_nroreg"));
                objNotaRecojoCab.setNr_autusuadd(rs.getString("nr_autusuadd"));
                objNotaRecojoCab.setNr_autfecadd(rs.getDate("nr_autfecadd"));
                objNotaRecojoCab.setNr_autpcadd(rs.getString("nr_autpcadd"));
                objNotaRecojoCab.setNr_usuadd(rs.getString("nr_usuadd"));
                objNotaRecojoCab.setNr_fecadd(rs.getTimestamp("nr_fecadd"));
                objNotaRecojoCab.setNr_pcadd(rs.getString("nr_pcadd"));
                objNotaRecojoCab.setNr_usumod(rs.getString("nr_usumod"));
                objNotaRecojoCab.setNr_fecmod(rs.getTimestamp("nr_fecmod"));
                objNotaRecojoCab.setNr_pcmod(rs.getString("nr_pcmod"));
                objlstNotaRecojoCab.add(objNotaRecojoCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaRecojoCab.getSize() + " registro(s)");
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
        return objlstNotaRecojoCab;
    }

    public ListModelList<NotaRecojoDet> listaNotaRecojoDet(int emp_id, int suc_id, long nr_key) throws SQLException {
        String SQL_NOTARECOJODET = "select * from v_listanotarecojodet t where t.nrd_est=1 and t.emp_id=" + emp_id
                + " and t.suc_id=" + suc_id + " and t.nr_key=" + nr_key + " order by nr_key";
        objlstNotaRecojoDet = null;
        objlstNotaRecojoDet = new ListModelList<NotaRecojoDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTARECOJODET);
            while (rs.next()) {
                objNotaRecojoDet = new NotaRecojoDet();
                objNotaRecojoDet.setNr_key(rs.getLong("nr_key"));
                objNotaRecojoDet.setEmp_id(rs.getInt("emp_id"));
                objNotaRecojoDet.setSuc_id(rs.getInt("suc_id"));
                objNotaRecojoDet.setNrd_item(rs.getLong("nrd_item"));
                objNotaRecojoDet.setNrd_tipdoc(rs.getInt("nrd_tipdoc"));
                objNotaRecojoDet.setNrd_tipdocdes(rs.getString("nrd_tipdocdes"));
                objNotaRecojoDet.setNrd_serie(rs.getString("nrd_serie") == null ? "" : rs.getString("nrd_serie"));
                objNotaRecojoDet.setNrd_doc(rs.getString("nrd_doc"));
                objNotaRecojoDet.setNrd_cantent(rs.getInt("nrd_cantent"));
                objNotaRecojoDet.setNrd_cantfrac(rs.getInt("nrd_cantfrac"));
                objNotaRecojoDet.setNrd_canttot(rs.getInt("nrd_canttot"));
                objNotaRecojoDet.setNrd_cantrec(rs.getInt("nrd_cantrec"));
                objNotaRecojoDet.setPro_id(rs.getString("pro_id"));
                objNotaRecojoDet.setPro_desdes(rs.getString("pro_desdes"));
                objNotaRecojoDet.setPro_des(rs.getString("pro_des"));
                objNotaRecojoDet.setPro_unimancom(rs.getString("pro_unimanven"));
                objNotaRecojoDet.setPro_presmincom(rs.getInt("pro_presminven"));
                objNotaRecojoDet.setNrd_glosa(rs.getString("nrd_glosa"));
                objNotaRecojoDet.setNrd_est(rs.getInt("nrd_est"));
                objNotaRecojoDet.setNrd_usuadd(rs.getString("nrd_usuadd"));
                objNotaRecojoDet.setNrd_fecadd(rs.getDate("nrd_fecadd"));
                objNotaRecojoDet.setNrd_pcadd(rs.getString("nrd_pcadd"));
                objNotaRecojoDet.setNrd_usumod(rs.getString("nrd_usumod"));
                objNotaRecojoDet.setNrd_fecmod(rs.getDate("nrd_fecmod"));
                objNotaRecojoDet.setNrd_pcmod(rs.getString("nrd_pcmod"));
                objlstNotaRecojoDet.add(objNotaRecojoDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaRecojoDet.getSize() + " registro(s)");
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
        return objlstNotaRecojoDet;
    }

    public ListModelList<NotaRecojoCab> listaNotRecCabApro(int emp_id, int suc_id, String cli_key, String ven_id, String sitIng, String sitApro, String sitRec, String fecinicial, String fecfinal) throws SQLException {
        String SQL_NOTRECCAB = "select * from v_listanotarecojocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.cli_key like '" + cli_key
                + "' and t.nr_vend like '" + ven_id + "' and t.nr_sit in ('" + sitIng + "','" + sitApro + "','" + sitRec + "') and t.nr_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                + " and to_date('" + fecfinal + "','DD/MM/YYYY') and t.nr_est=1 order by t.nr_key";
        objlstNotaRecojoCab = null;
        objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTRECCAB);
            while (rs.next()) {
                objNotaRecojoCab = new NotaRecojoCab();
                objNotaRecojoCab.setNr_key(rs.getLong("nr_key"));
                objNotaRecojoCab.setNr_id(rs.getString("nr_id"));
                objNotaRecojoCab.setEmp_id(rs.getInt("emp_id"));
                objNotaRecojoCab.setSuc_id(rs.getInt("suc_id"));
                objNotaRecojoCab.setCli_id(rs.getString("cli_id"));
                objNotaRecojoCab.setCli_key(rs.getLong("cli_key"));
                objNotaRecojoCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotaRecojoCab.setClidir_id(rs.getLong("clidir_id"));
                objNotaRecojoCab.setClidir_direcc(rs.getString("clidir_direc"));
                objNotaRecojoCab.setNr_fecemi(rs.getDate("nr_fecemi"));
                objNotaRecojoCab.setNr_fecent(rs.getDate("nr_fecent"));
                objNotaRecojoCab.setNr_periodo(rs.getInt("nr_periodo"));
                objNotaRecojoCab.setNr_sit(rs.getInt("nr_sit"));
                objNotaRecojoCab.setNr_motrec(rs.getInt("nr_motrec"));
                objNotaRecojoCab.setMrec_des(rs.getString("mrec_des"));
                objNotaRecojoCab.setNr_est(rs.getInt("nr_est"));
                objNotaRecojoCab.setNr_zona(rs.getString("nr_zona"));
                objNotaRecojoCab.setZon_des(rs.getString("zon_des"));
                objNotaRecojoCab.setNr_motcam(rs.getInt("nr_motcam"));
                objNotaRecojoCab.setMcam_des(rs.getString("mcam_des"));
                objNotaRecojoCab.setNr_sup(rs.getInt("nr_sup"));
                objNotaRecojoCab.setNr_vend(rs.getInt("nr_vend"));
                objNotaRecojoCab.setVen_nom(rs.getString("ven_nom"));
                objNotaRecojoCab.setNr_trans(rs.getInt("nr_trans"));
                objNotaRecojoCab.setTrans_id(rs.getString("trans_id"));
                objNotaRecojoCab.setTrans_alias(rs.getString("trans_alias"));
                objNotaRecojoCab.setNr_hor(rs.getInt("nr_hor"));
                objNotaRecojoCab.setHor_des(rs.getString("hor_des"));
                objNotaRecojoCab.setNr_tipnotaent(rs.getString("nr_tipnotaent"));
                objNotaRecojoCab.setNr_notaent(rs.getString("nr_notaent"));
                objNotaRecojoCab.setNr_nroreg(rs.getString("nr_nroreg"));
                objNotaRecojoCab.setNr_autusuadd(rs.getString("nr_autusuadd"));
                objNotaRecojoCab.setNr_autfecadd(rs.getDate("nr_autfecadd"));
                objNotaRecojoCab.setNr_autpcadd(rs.getString("nr_autpcadd"));
                objNotaRecojoCab.setNr_usuadd(rs.getString("nr_usuadd"));
                objNotaRecojoCab.setNr_fecadd(rs.getTimestamp("nr_fecadd"));
                objNotaRecojoCab.setNr_pcadd(rs.getString("nr_pcadd"));
                objNotaRecojoCab.setNr_usumod(rs.getString("nr_usumod"));
                objNotaRecojoCab.setNr_fecmod(rs.getTimestamp("nr_fecmod"));
                objNotaRecojoCab.setNr_pcmod(rs.getString("nr_pcmod"));
                objlstNotaRecojoCab.add(objNotaRecojoCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaRecojoCab.getSize() + " registro(s)");
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
        return objlstNotaRecojoCab;
    }
}
