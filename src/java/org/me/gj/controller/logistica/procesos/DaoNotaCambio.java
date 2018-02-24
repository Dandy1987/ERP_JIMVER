package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.NotCambCab;
import org.me.gj.model.logistica.procesos.NotCambDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotaCambio {

    //Instancias de Objetos
    ListModelList<NotCambCab> objlstNotCambCab;
    ListModelList<NotCambDet> objlstNotCambDet;
    NotCambCab objNotCambCab;
    NotCambDet objNotCambDet;
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
    private static final Logger LOGGER = Logger.getLogger(DaoNotaCambio.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarNotaCambioCab(NotCambCab objNotCambCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        String nrid;
        cst = null;
        String SQL_INSERTAR_NOTACAMCAB = "{call pack_tnotacambio.p_insertarNotaCambioCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTACAMCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotCambCab.getEmp_id());
            cst.setInt("n_suc_id", objNotCambCab.getSuc_id());
            cst.setString("c_cli_id", objNotCambCab.getCli_id());
            cst.setLong("n_cli_key", objNotCambCab.getCli_key());
            cst.setLong("n_clidir_id", objNotCambCab.getClidir_id());
            cst.setDate("d_nc_fecemi", new java.sql.Date(objNotCambCab.getNc_fecemi().getTime()));
            cst.setDate("d_nc_fecent", new java.sql.Date(objNotCambCab.getNc_fecent().getTime()));
            cst.setString("c_nc_zona", objNotCambCab.getNc_zona());
            cst.setInt("n_nc_motcam", objNotCambCab.getNc_motcam());
            cst.setInt("n_nc_sup", objNotCambCab.getNc_sup());
            cst.setInt("n_nc_vend", objNotCambCab.getNc_vend());
            cst.setInt("n_nc_trans", objNotCambCab.getNc_trans());
            cst.setInt("n_nc_hor", objNotCambCab.getNc_hor());
            cst.setString("c_nc_usuadd", objNotCambCab.getNc_usuadd());
            cst.setString("c_nc_pcadd", objNotCambCab.getNc_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.registerOutParameter("c_nc_id", java.sql.Types.VARCHAR);
            cst.execute();
            nrid = cst.getString("c_nc_id");
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo una Nota de Cambio con Codigo " + nrid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            nrid = "";
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            nrid = "";
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, nrid);
    }

    public ParametrosSalida modificarNotaCambioCab(NotCambCab objNotCambCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTACAMBIOCAB = "{call pack_tnotacambio.p_modificarNotaCambioCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTACAMBIOCAB);
            cst.clearParameters();
            cst.setLong("n_nc_key", objNotCambCab.getNc_key());
            cst.setInt("n_emp_id", objNotCambCab.getEmp_id());
            cst.setInt("n_suc_id", objNotCambCab.getSuc_id());
            cst.setString("c_cli_id", objNotCambCab.getCli_id());
            cst.setLong("n_cli_key", objNotCambCab.getCli_key());
            cst.setLong("n_clidir_id", objNotCambCab.getClidir_id());
            cst.setDate("d_nc_fecemi", new java.sql.Date(objNotCambCab.getNc_fecemi().getTime()));
            cst.setDate("d_nc_fecent", new java.sql.Date(objNotCambCab.getNc_fecent().getTime()));
            cst.setString("c_nc_zona", objNotCambCab.getNc_zona());
            cst.setInt("n_nc_motcam", objNotCambCab.getNc_motcam());
            cst.setInt("n_nc_sup", objNotCambCab.getNc_sup());
            cst.setInt("n_nc_vend", objNotCambCab.getNc_vend());
            cst.setInt("n_nc_trans", objNotCambCab.getNc_trans());
            cst.setInt("n_nc_hor", objNotCambCab.getNc_hor());
            cst.setString("c_nc_usumod", objNotCambCab.getNc_usumod());
            cst.setString("c_nc_pcmod", objNotCambCab.getNc_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se Modifico la Nota de Cambio con codigo " + objNotCambCab.getNc_id());
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

    public ParametrosSalida eliminarNotaCambioCab(NotCambCab objNotaCambioCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTACAMBIOCAB = "{call pack_tnotacambio.p_eliminarNotaCambioCab(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTACAMBIOCAB);
            cst.clearParameters();
            cst.setLong("n_nc_key", objNotaCambioCab.getNc_key());
            cst.setInt("n_emp_id", objNotaCambioCab.getEmp_id());
            cst.setInt("n_suc_id", objNotaCambioCab.getSuc_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Nota de Cambio con Codigo " + objNotaCambioCab.getNc_id());
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

    public ParametrosSalida insertarNotaCambioDet(NotCambDet objNotCambDet) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_NOTACAMBIODET = "{call pack_tnotacambio.p_insertarNotaCambioDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NOTACAMBIODET);
            cst.clearParameters();
            cst.setLong("n_nc_key", objNotCambDet.getNc_key());
            cst.setInt("n_emp_id", objNotCambDet.getEmp_id());
            cst.setInt("n_suc_id", objNotCambDet.getSuc_id());
            cst.setInt("n_ncd_tipdoc", objNotCambDet.getNcd_tipdoc());
            cst.setString("c_ncd_serie", objNotCambDet.getNcd_serie());
            cst.setString("c_ncd_docref", objNotCambDet.getNcd_doc());
            cst.setInt("n_ncd_cantent", objNotCambDet.getNcd_cantent());
            cst.setInt("n_ncd_cantfrac", objNotCambDet.getNcd_cantfrac());
            cst.setInt("n_ncd_canttot", objNotCambDet.getNcd_canttot());
            cst.setString("c_pro_id", objNotCambDet.getPro_id());
            cst.setString("c_ncd_glosa", objNotCambDet.getNcd_glosa());
            cst.setString("c_ncd_usuadd", objNotCambDet.getNcd_usuadd());
            cst.setString("c_ncd_pcadd", objNotCambDet.getNcd_pcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto a la Nota de Cambio " + objNotCambDet.getNc_key() + " con Codigo " + objNotCambDet.getPro_id());
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

    public ParametrosSalida modificarNotaCambioDet(NotCambDet objNotCambDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_NOTACAMBIODET = "{call pack_tnotacambio.p_modificarNotaCambioDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_NOTACAMBIODET);
            cst.clearParameters();
            cst.setLong("n_nc_key", objNotCambDet.getNc_key());
            cst.setInt("n_emp_id", objNotCambDet.getEmp_id());
            cst.setInt("n_suc_id", objNotCambDet.getSuc_id());
            cst.setLong("n_ncd_item", objNotCambDet.getNcd_item());
            cst.setInt("n_ncd_tipdoc", objNotCambDet.getNcd_tipdoc());
            cst.setString("c_ncd_serie", objNotCambDet.getNcd_serie());
            cst.setString("c_ncd_docref", objNotCambDet.getNcd_doc());
            cst.setInt("n_ncd_cantent", objNotCambDet.getNcd_cantent());
            cst.setInt("n_ncd_cantfrac", objNotCambDet.getNcd_cantfrac());
            cst.setInt("n_ncd_canttot", objNotCambDet.getNcd_canttot());
            cst.setString("c_pro_id", objNotCambDet.getPro_id());
            cst.setString("c_ncd_glosa", objNotCambDet.getNcd_glosa());
            cst.setString("c_ncd_usumod", objNotCambDet.getNcd_usumod());
            cst.setString("c_ncd_pcmod", objNotCambDet.getNcd_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto a la Nota de Cambio " + objNotCambDet.getNc_key() + " con Codigo " + objNotCambDet.getPro_id());
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

    public ParametrosSalida eliminarNotaCambioDet(NotCambDet objNotCambDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_NOTACAMBIODET = "{call pack_tnotacambio.p_eliminarNotaCambioDet(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NOTACAMBIODET);
            cst.clearParameters();
            cst.setLong("n_nc_key", objNotCambDet.getNc_key());
            cst.setInt("n_emp_id", objNotCambDet.getEmp_id());
            cst.setInt("n_suc_id", objNotCambDet.getSuc_id());
            cst.setLong("n_ncd_item", objNotCambDet.getNcd_item());
            cst.setString("c_ncd_usumod", objNotCambDet.getNcd_usumod());
            cst.setString("c_ncd_pcmod", objNotCambDet.getNcd_pcmod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un producto a la Nota de Cambio " + objNotCambDet.getNc_key() + " con Codigo " + objNotCambDet.getPro_id());
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

    public ParametrosSalida autorizarNotaCambio(NotCambCab objNotCambCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_AUTORIZAR_NOTACAMBCAB = "{call pack_tnotacambio.p_autorizarNotaCambio(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_AUTORIZAR_NOTACAMBCAB);
            cst.clearParameters();
            cst.setInt("n_emp_id", objNotCambCab.getEmp_id());
            cst.setInt("n_suc_id", objNotCambCab.getSuc_id());
            cst.setLong("n_nc_key", objNotCambCab.getNc_key());
            cst.setInt("n_nc_sit", objNotCambCab.getNc_sit());
            cst.setString("c_nc_autusuadd", objNotCambCab.getNc_autusuadd());
            cst.setString("c_nc_autpcadd", objNotCambCab.getNc_autpcadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Autorizo una Nota de Cambio con Codigo " + objNotCambCab.getNc_id());
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
    public ListModelList<NotCambCab> listaNotaCambioCab(int emp_id, int suc_id, String ven_key, String sit, String fecinicial, String fecfinal, boolean ordVend) throws SQLException {
        String ordenar;
        String SQL_NOTACAMBIOCAB = "";
        String fechaActual = Utilitarios.hoyAsString();
        if (ordVend) {
            ordenar = "t.nc_vend, ";
        } else {
            ordenar = "";
        }
        //fecha inicial lleno, fecha final lleno, vendedor lleno 
        if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_vend like '" + ven_key.trim()
                    + "' and t.nc_sit like '" + sit + "' and t.nc_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                    + " and to_date('" + fecfinal + "','DD/MM/YYYY') order by  " + ordenar + "t.nc_key";

        }//fecha inicial vacio, fecha final vacio, vendedor vacio
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_vend like '" + ven_key.trim()
                    + "' and t.nc_sit like '" + sit + "' and t.nc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by " + ordenar + "t.nc_key";

        }//fecha inicial llena, fecha final llena, vendedor vacio 
        else if (!fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.nc_sit like '" + sit + "' and t.nc_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY') and to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.nc_key";

        }//fecha inicial llena, fecha final vacio, vendedor lleno  
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_vend like '" + ven_key.trim()
                    + "' and t.nc_sit like '" + sit + "' and t.nc_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.nc_key";
        } //fecha inicial vacio, fecha final llena, vendedor lleno 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_vend like '" + ven_key.trim()
                    + "' and t.nc_sit like '" + sit + "' and t.nc_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY')  order by " + ordenar + "t.nc_key";

        } //fecha inicial llena, fecha final vacio, vendedor vacio 
        else if (!fecinicial.isEmpty() && fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_sit like '" + sit + "' and t.nc_fecemi >= to_date('" + fecinicial + "','DD/MM/YYYY') order by " + ordenar + "t.nc_key";

        } //fecha inicial vacia, fecha final vacio, vendedor lleno 
        else if (fecinicial.isEmpty() && fecfinal.isEmpty() && !ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_vend like '" + ven_key.trim()
                    + "' and t.nc_sit like '" + sit + "' and t.nc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " order by " + ordenar + "t.nc_key";

        } //fecha inicial vacia, fecha final lleno, vendedor vacio 
        else if (fecinicial.isEmpty() && !fecfinal.isEmpty() && ven_key.isEmpty()) {
            SQL_NOTACAMBIOCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.nc_sit like '" + sit + "' and t.nc_fecemi <= to_date('" + fecfinal + "','DD/MM/YYYY') order by " + ordenar + "t.nc_key";

        }
        objlstNotCambCab = new ListModelList<NotCambCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTACAMBIOCAB);
            while (rs.next()) {
                objNotCambCab = new NotCambCab();
                objNotCambCab.setNc_key(rs.getLong("nc_key"));
                objNotCambCab.setNc_id(rs.getString("nc_id"));
                objNotCambCab.setEmp_id(rs.getInt("emp_id"));
                objNotCambCab.setSuc_id(rs.getInt("suc_id"));
                objNotCambCab.setCli_id(rs.getString("cli_id"));
                objNotCambCab.setCli_key(rs.getLong("cli_key"));
                //objNotCambCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotCambCab.setCli_razsoc(rs.getString("cli_razsoc").length() < 33 ? rs.getString("cli_razsoc") : rs.getString("cli_razsoc").substring(0, 33));
                objNotCambCab.setClidir_id(rs.getLong("clidir_id"));
                objNotCambCab.setClidir_direc(rs.getString("clidir_direc"));
                objNotCambCab.setNc_fecemi(rs.getDate("nc_fecemi"));
                objNotCambCab.setNc_fecent(rs.getDate("nc_fecent"));
                objNotCambCab.setNc_periodo(rs.getInt("nc_periodo"));
                objNotCambCab.setNc_sit(rs.getInt("nc_sit"));
                objNotCambCab.setNc_motrec(rs.getInt("nc_motrec"));
                objNotCambCab.setMrec_des(rs.getString("mrec_des"));
                objNotCambCab.setNc_est(rs.getInt("nc_est"));
                objNotCambCab.setNc_sitdes(rs.getString("nc_sitdes").length() < 12 ? rs.getString("nc_sitdes") : rs.getString("nc_sitdes").substring(0, 12));
                objNotCambCab.setNc_zona(rs.getString("nc_zona"));
                objNotCambCab.setZon_des(rs.getString("zon_des"));
                objNotCambCab.setNc_motcam(rs.getInt("nc_motcam"));
                objNotCambCab.setMcam_des(rs.getString("mcam_des"));
                objNotCambCab.setNc_sup(rs.getInt("nc_sup"));
                objNotCambCab.setNc_vend(rs.getInt("nc_vend"));
                objNotCambCab.setVen_id(rs.getString("ven_id"));
                //objNotCambCab.setVen_nom(rs.getString("ven_nom"));
                objNotCambCab.setVen_nom(rs.getString("ven_nom").length() < 30 ? rs.getString("ven_nom") : rs.getString("ven_nom").substring(0, 30));
                objNotCambCab.setNc_trans(rs.getInt("nc_trans"));
                objNotCambCab.setTrans_alias(rs.getString("trans_alias"));
                objNotCambCab.setNc_hor(rs.getInt("nc_hor"));
                objNotCambCab.setHor_des(rs.getString("hor_des"));
                objNotCambCab.setNc_tipnotaent(rs.getString("nc_tipnotaent"));
                objNotCambCab.setNc_notaent(rs.getString("nc_notaent"));
                objNotCambCab.setNc_tipnotasal(rs.getString("nc_tipnotasal"));
                objNotCambCab.setNc_notasal(rs.getString("nc_notasal"));
                objNotCambCab.setNc_nroreg(rs.getString("nc_nroreg"));
                objNotCambCab.setNc_nrodep(rs.getString("nc_nrodep"));
                objNotCambCab.setNc_autusuadd(rs.getString("nc_autusuadd"));
                objNotCambCab.setNc_autfecadd(rs.getDate("nc_autfecadd"));
                objNotCambCab.setNc_autpcadd(rs.getString("nc_autpcadd"));
                objNotCambCab.setNc_usuadd(rs.getString("nc_usuadd"));
                objNotCambCab.setNc_fecadd(rs.getTimestamp("nc_fecadd"));
                objNotCambCab.setNc_pcadd(rs.getString("nc_pcadd"));
                objNotCambCab.setNc_usumod(rs.getString("nc_usumod"));
                objNotCambCab.setNc_fecmod(rs.getTimestamp("nc_fecmod"));
                objNotCambCab.setNc_pcmod(rs.getString("nc_pcmod"));
                objlstNotCambCab.add(objNotCambCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotCambCab.getSize() + " registro(s)");
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
        return objlstNotCambCab;
    }

    public ListModelList<NotCambDet> listaNotaCambioDet(int emp_id, int suc_id, long nc_key) throws SQLException {
        String SQL_NOTACAMBIODET = "select * from v_listanotacambiodet t where t.ncd_est=1 and t.emp_id=" + emp_id
                + " and t.suc_id=" + suc_id + " and t.nc_key=" + nc_key + " order by nc_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTACAMBIODET);
            objlstNotCambDet = new ListModelList<NotCambDet>();
            while (rs.next()) {
                objNotCambDet = new NotCambDet();
                objNotCambDet.setNc_key(rs.getLong("nc_key"));
                objNotCambDet.setEmp_id(rs.getInt("emp_id"));
                objNotCambDet.setSuc_id(rs.getInt("suc_id"));
                objNotCambDet.setNcd_item(rs.getLong("ncd_item"));
                objNotCambDet.setNcd_tipdoc(rs.getInt("ncd_tipdoc"));
                objNotCambDet.setNcd_tipdocdes(rs.getString("ncd_tipdocdes"));
                objNotCambDet.setNcd_serie(rs.getString("ncd_serie") == null ? "" : rs.getString("ncd_serie"));
                objNotCambDet.setNcd_doc(rs.getString("ncd_doc"));
                objNotCambDet.setNcd_cantent(rs.getInt("ncd_cantent"));
                objNotCambDet.setNcd_cantfrac(rs.getInt("ncd_cantfrac"));
                objNotCambDet.setNcd_canttot(rs.getInt("ncd_canttot"));
                objNotCambDet.setNcd_cantrec(rs.getInt("ncd_cantrec"));
                objNotCambDet.setNcd_cantmov(rs.getInt("ncd_cantmov"));
                objNotCambDet.setPro_id(rs.getString("pro_id"));
                objNotCambDet.setPro_desdes(rs.getString("pro_desdes"));
                objNotCambDet.setPro_des(rs.getString("pro_des"));
                objNotCambDet.setPro_uniman(rs.getString("pro_uniman"));
                objNotCambDet.setPro_presmin(rs.getInt("pro_presmin"));
                objNotCambDet.setNcd_glosa(rs.getString("ncd_glosa"));
                objNotCambDet.setNcd_est(rs.getInt("ncd_est"));
                objNotCambDet.setNcd_usuadd(rs.getString("ncd_usuadd"));
                objNotCambDet.setNcd_fecadd(rs.getTimestamp("ncd_fecadd"));
                objNotCambDet.setNcd_pcadd(rs.getString("ncd_pcadd"));
                objNotCambDet.setNcd_usumod(rs.getString("ncd_usumod"));
                objNotCambDet.setNcd_fecmod(rs.getTimestamp("ncd_fecmod"));
                objNotCambDet.setNcd_pcmod(rs.getString("ncd_pcmod"));
                objlstNotCambDet.add(objNotCambDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotCambDet.getSize() + " registro(s)");
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
        return objlstNotCambDet;
    }

    public ListModelList<NotCambCab> listaNotCambioCabApro(int emp_id, int suc_id, String cli_key, String ven_id, String sitIng, String sitApro, String sitRec, String fecinicial, String fecfinal) throws SQLException {
        String SQL_NOTCAMBCAB = "select * from v_listanotacambiocab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.cli_key like '" + cli_key
                + "' and t.nc_vend like '" + ven_id + "' and t.nc_sit in ('" + sitIng + "','" + sitApro + "','" + sitRec + "') and t.nc_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                + " and to_date('" + fecfinal + "','DD/MM/YYYY') and t.nc_est=1 order by t.nc_key";
        objlstNotCambCab = null;
        objlstNotCambCab = new ListModelList<NotCambCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTCAMBCAB);
            while (rs.next()) {
                objNotCambCab = new NotCambCab();
                objNotCambCab.setNc_key(rs.getLong("nc_key"));
                objNotCambCab.setNc_id(rs.getString("nc_id"));
                objNotCambCab.setEmp_id(rs.getInt("emp_id"));
                objNotCambCab.setSuc_id(rs.getInt("suc_id"));
                objNotCambCab.setCli_id(rs.getString("cli_id"));
                objNotCambCab.setCli_key(rs.getLong("cli_key"));
                objNotCambCab.setCli_razsoc(rs.getString("cli_razsoc"));
                objNotCambCab.setClidir_id(rs.getLong("clidir_id"));
                objNotCambCab.setClidir_direc(rs.getString("clidir_direc"));
                objNotCambCab.setNc_fecemi(rs.getDate("nc_fecemi"));
                objNotCambCab.setNc_fecent(rs.getDate("nc_fecent"));
                objNotCambCab.setNc_periodo(rs.getInt("nc_periodo"));
                objNotCambCab.setNc_sit(rs.getInt("nc_sit"));
                objNotCambCab.setNc_motrec(rs.getInt("nc_motrec"));
                objNotCambCab.setMrec_des(rs.getString("mrec_des"));
                objNotCambCab.setNc_est(rs.getInt("nc_est"));
                objNotCambCab.setNc_sitdes(rs.getString("nc_sitdes"));
                objNotCambCab.setNc_zona(rs.getString("nc_zona"));
                objNotCambCab.setZon_des(rs.getString("zon_des"));
                objNotCambCab.setNc_motcam(rs.getInt("nc_motcam"));
                objNotCambCab.setMcam_des(rs.getString("mcam_des"));
                objNotCambCab.setNc_sup(rs.getInt("nc_sup"));
                objNotCambCab.setNc_vend(rs.getInt("nc_vend"));
                objNotCambCab.setVen_id(rs.getString("ven_id"));
                objNotCambCab.setVen_nom(rs.getString("ven_nom"));
                objNotCambCab.setNc_trans(rs.getInt("nc_trans"));
                objNotCambCab.setTrans_alias(rs.getString("trans_alias"));
                objNotCambCab.setNc_hor(rs.getInt("nc_hor"));
                objNotCambCab.setHor_des(rs.getString("hor_des"));
                objNotCambCab.setNc_tipnotaent(rs.getString("nc_tipnotaent"));
                objNotCambCab.setNc_notaent(rs.getString("nc_notaent"));
                objNotCambCab.setNc_tipnotasal(rs.getString("nc_tipnotasal"));
                objNotCambCab.setNc_notasal(rs.getString("nc_notasal"));
                objNotCambCab.setNc_nroreg(rs.getString("nc_nroreg"));
                objNotCambCab.setNc_nrodep(rs.getString("nc_nrodep"));
                objNotCambCab.setNc_autusuadd(rs.getString("nc_autusuadd"));
                objNotCambCab.setNc_autfecadd(rs.getDate("nc_autfecadd"));
                objNotCambCab.setNc_autpcadd(rs.getString("nc_autpcadd"));
                objNotCambCab.setNc_usuadd(rs.getString("nc_usuadd"));
                objNotCambCab.setNc_fecadd(rs.getTimestamp("nc_fecadd"));
                objNotCambCab.setNc_pcadd(rs.getString("nc_pcadd"));
                objNotCambCab.setNc_usumod(rs.getString("nc_usumod"));
                objNotCambCab.setNc_fecmod(rs.getTimestamp("nc_fecmod"));
                objNotCambCab.setNc_pcmod(rs.getString("nc_pcmod"));
                objlstNotCambCab.add(objNotCambCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotCambCab.getSize() + " registro(s)");
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
        return objlstNotCambCab;
    }
}
