package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.NotaESLin;
import org.me.gj.model.logistica.informes.NotaESProd;
import org.me.gj.model.logistica.informes.NotaESProv;
import org.me.gj.model.logistica.informes.NotaESSublin;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.logistica.procesos.NotaESDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotaES {

    //Instancias de Objetos
    ListModelList<NotaESDet> objlstNotaESDet;
    ListModelList<NotaESCab> objlstNotaESCab;
    NotaESDet objNotaESDet;
    NotaESCab objNotaESCab;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoNotaES.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarCabecera(NotaESCab objNotaESCab) throws SQLException, ParseException {
        objParametrosSalida = new ParametrosSalida();
        String SQL_INSERTAR_CABECERA = "{call pack_tnotaes.p_insertarNotaESCab(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CABECERA);
            cst.clearParameters();
            cst.setString(1, objNotaESCab.getNescab_tipnotaes());
            cst.setString(2, objNotaESCab.getNescab_sernotaes());
            cst.setInt(3, objNotaESCab.getEmp_id());
            cst.setInt(4, objNotaESCab.getSuc_id());
            cst.setString(5, objNotaESCab.getNescab_nroped());
            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objNotaESCab.getNescab_fecha());
            fecha_hora = sdf1.format(objNotaESCab.getNescab_fecha());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(6, hora);
            //cst.setString(6, fecha_concat);
            cst.setString(7, objNotaESCab.getNescab_glosa());
            cst.setString(8, objNotaESCab.getNescab_ocind());
            cst.setLong(9, objNotaESCab.getNescab_ocnropedkey());
            cst.setString(10, objNotaESCab.getNescab_tipdoc());
            cst.setString(11, objNotaESCab.getNescab_nroserie());
            cst.setString(12, objNotaESCab.getNescab_nrodoc());
            cst.setString(13, objNotaESCab.getNescab_provid());
            cst.setString(14, objNotaESCab.getNescab_cliid());
            cst.setInt(15, objNotaESCab.getNescab_moneda());
            cst.setDouble(16, objNotaESCab.getNescab_tcamb());
            cst.setString(17, objNotaESCab.getNescab_almori());
            cst.setString(18, objNotaESCab.getNescab_almdes());
            cst.setInt(19, objNotaESCab.getNescab_despacho());
            cst.setString(20, objNotaESCab.getNescab_nrodep());
            cst.setString(21, objNotaESCab.getNescab_usuadd());
            cst.registerOutParameter(22, java.sql.Types.NUMERIC);
            cst.registerOutParameter(23, java.sql.Types.VARCHAR);
            cst.registerOutParameter(24, java.sql.Types.VARCHAR);
            cst.execute();
            objParametrosSalida.setFlagIndicador(cst.getInt(22));
            objParametrosSalida.setMsgValidacion(cst.getString(23));
            objParametrosSalida.setCodigoRegistro(cst.getString(24));
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + cst.getString(24));
        } catch (SQLException e) {
            objParametrosSalida.setMsgValidacion("Ocurrio una Excepcion1 debido al Error " + e.toString());
            objParametrosSalida.setFlagIndicador(1);
            objParametrosSalida.setCodigoRegistro("0");
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            objParametrosSalida.setMsgValidacion("Ocurrio una Excepcion2 debido al Error " + e.toString());
            objParametrosSalida.setFlagIndicador(1);
            objParametrosSalida.setCodigoRegistro("0");
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return objParametrosSalida;
    }

    public ParametrosSalida modificarCabecera(NotaESCab objNotaESCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_FACTURAPROVEEDOR = "{call pack_tnotaes.p_actualizarNotaESCab(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_FACTURAPROVEEDOR);
            cst.clearParameters();
            cst.setString(1, objNotaESCab.getNescab_id());
            cst.setString(2, objNotaESCab.getNescab_key());
            cst.setString(3, objNotaESCab.getNescab_tipnotaes());
            cst.setInt(4, objNotaESCab.getEmp_id());
            cst.setInt(5, objNotaESCab.getSuc_id());
            cst.setString(6, objNotaESCab.getNescab_nroped());
            String fecha_cadena, fecha_hora;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            fecha_cadena = sdf.format(objNotaESCab.getNescab_fecha());
            fecha_hora = sdf1.format(objNotaESCab.getNescab_fecha());
            String fecha_concat = fecha_cadena + ' ' + fecha_hora;
            Date fec_conc = sdf2.parse(fecha_concat);
            //cst.setString(6, new java.sql.Date(fec_conc.getTime()));
            //new java.util.Date(fecha_cadena)
            Time hora = new Time(fec_conc.getTime());
            cst.setTime(7, hora);
            //st.setDate(7, new java.sql.Date(objNotaESCab.getNescab_fecha().getTime()));
            cst.setString(8, objNotaESCab.getNescab_glosa());
            cst.setInt(9, objNotaESCab.getNescab_situacion());
            cst.setInt(10, objNotaESCab.getNescab_est());
            cst.setString(11, objNotaESCab.getNescab_ocind());
            cst.setLong(12, objNotaESCab.getNescab_ocnropedkey());
            cst.setString(13, objNotaESCab.getNescab_tipdoc());
            cst.setString(14, objNotaESCab.getNescab_nroserie());
            cst.setString(15, objNotaESCab.getNescab_nrodoc());
            cst.setString(16, objNotaESCab.getNescab_provid());
            cst.setString(17, objNotaESCab.getNescab_cliid());
            cst.setInt(18, objNotaESCab.getNescab_moneda());
            cst.setDouble(19, objNotaESCab.getNescab_tcamb());
            cst.setString(20, objNotaESCab.getNescab_almori());
            cst.setString(21, objNotaESCab.getNescab_almdes());
            cst.setInt(22, objNotaESCab.getNescab_costeo());
            cst.setInt(23, objNotaESCab.getNescab_despacho());
            cst.setString(24, objNotaESCab.getNescab_nrodep());
            cst.setString(25, objNotaESCab.getNescab_usumod());
            cst.registerOutParameter(26, java.sql.Types.NUMERIC);
            cst.registerOutParameter(27, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(27);
            i_flagErrorBD = cst.getInt(26);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico una Nota de E/S con codigo " + objNotaESCab.getNescab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion3 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion4 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarCabecera(NotaESCab objNotaESCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_CABECERA = "{call pack_tnotaes.p_eliminarNotaESCab(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CABECERA);
            cst.clearParameters();
            cst.setInt(1, objNotaESCab.getEmp_id());
            cst.setInt(2, objNotaESCab.getSuc_id());
            cst.setString(3, objNotaESCab.getNescab_id());
            cst.setString(4, objNotaESCab.getNescab_key());
            cst.setString(5, objNotaESCab.getNescab_usumod());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Nota E/S con Codigo " + objNotaESCab.getNescab_cliid());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion5 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion6 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarDetalle(NotaESDet objNotaESDet) throws SQLException, ParseException {
        String SQL_INSERTAR_DETALLE = "{call pack_tnotaes.p_insertarNotaESDet(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_DETALLE);
            cst.clearParameters();
            cst.setString(1, objNotaESDet.getNescab_id());
            cst.setString(2, objNotaESDet.getNescab_key());
            cst.setString(3, objNotaESDet.getNescab_tipnotaes());
            cst.setInt(4, objNotaESDet.getEmp_id());
            cst.setInt(5, objNotaESDet.getSuc_id());
            cst.setString(6, objNotaESDet.getNesdet_tipmov());
            cst.setString(7, objNotaESDet.getNesdet_provid());
            cst.setString(8, objNotaESDet.getPro_id());
            cst.setString(9, objNotaESDet.getNesdet_glosa());
            cst.setDouble(10, objNotaESDet.getNesdet_cant());
            cst.setInt(11, objNotaESDet.getNesdet_undpre());
            cst.setString(12, objNotaESDet.getNesdet_proconv());
            cst.setLong(13, objNotaESDet.getNesdet_cantconv());
            cst.setDouble(14, objNotaESDet.getNesdet_pimpto());
            cst.setDouble(15, objNotaESDet.getNesdet_vimpto());
            cst.setDouble(16, objNotaESDet.getNesdet_valafe());
            cst.setDouble(17, objNotaESDet.getNesdet_valina());
            cst.setDouble(18, objNotaESDet.getNesdet_pvta());
            cst.setString(19, objNotaESDet.getNesdet_almori());
            cst.setString(20, objNotaESDet.getNesdet_almdes());
            cst.setString(21, objNotaESDet.getNesdet_ubiori());
            cst.setString(22, objNotaESDet.getNesdet_ubides());
            cst.setDouble(23, objNotaESDet.getNesdet_cantfac());
            cst.setDouble(24, objNotaESDet.getNesdet_peso());
            cst.setDouble(25, objNotaESDet.getNesdet_cositem());
            cst.setString(26, objNotaESDet.getNesdet_usuadd());
            cst.registerOutParameter(27, java.sql.Types.NUMERIC);
            cst.registerOutParameter(28, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(28);
            i_flagErrorBD = cst.getInt(27);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + cst.getString(28));
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion7 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
            i_flagErrorBD = 1;
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion8 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
            i_flagErrorBD = 1;
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida modificarDetalle(NotaESDet objNotaESDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_DETALLE = "{call pack_tnotaes.p_actualizarNotaESDet(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_DETALLE);
            cst.clearParameters();
            cst.setString(1, objNotaESDet.getNescab_id());
            cst.setString(2, objNotaESDet.getNescab_key());
            cst.setString(3, objNotaESDet.getNescab_tipnotaes());
            cst.setInt(4, objNotaESDet.getEmp_id());
            cst.setInt(5, objNotaESDet.getSuc_id());
            cst.setLong(6, objNotaESDet.getNesdet_item());
            cst.setString(7, objNotaESDet.getNesdet_tipmov());
            cst.setInt(8, objNotaESDet.getNesdet_est());
            cst.setString(9, objNotaESDet.getNesdet_provid());
            cst.setString(10, objNotaESDet.getPro_id());
            cst.setString(11, objNotaESDet.getNesdet_glosa());
            cst.setDouble(12, objNotaESDet.getNesdet_cant());
            cst.setInt(13, objNotaESDet.getNesdet_undpre());
            cst.setString(14, objNotaESDet.getNesdet_proconv());
            cst.setLong(15, objNotaESDet.getNesdet_cantconv());
            cst.setDouble(16, objNotaESDet.getNesdet_pimpto() == 0 ? 0.0 : objNotaESDet.getNesdet_pimpto());
            cst.setDouble(17, objNotaESDet.getNesdet_vimpto() == 0 ? 0.0 : objNotaESDet.getNesdet_vimpto());
            cst.setDouble(18, objNotaESDet.getNesdet_valafe() == 0 ? 0.0 : objNotaESDet.getNesdet_valafe());
            cst.setDouble(19, objNotaESDet.getNesdet_valina() == 0 ? 0.0 : objNotaESDet.getNesdet_valina());
            cst.setDouble(20, objNotaESDet.getNesdet_pvta() == 0 ? 0.0 : objNotaESDet.getNesdet_pvta());
            cst.setString(21, objNotaESDet.getNesdet_almori());
            cst.setString(22, objNotaESDet.getNesdet_almdes());
            cst.setString(23, objNotaESDet.getNesdet_ubiori());
            cst.setString(24, objNotaESDet.getNesdet_ubides());
            cst.setDouble(25, objNotaESDet.getNesdet_cantfac());
            cst.setDouble(26, objNotaESDet.getNesdet_peso());
            cst.setDouble(27, objNotaESDet.getNesdet_cositem());
            cst.setString(28, objNotaESDet.getNesdet_usumod());
            cst.registerOutParameter(29, java.sql.Types.NUMERIC);
            cst.registerOutParameter(30, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(29);
            s_msg = cst.getString(30);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto de la Nota E/S " + objNotaESDet.getNescab_id() + " con Codigo de Producto " + objNotaESDet.getPro_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion9 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion10 debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarDetalle(NotaESDet objNotaESDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_DETALLE = "{call pack_tnotaes.p_eliminarNotaESDet(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_DETALLE);
            cst.clearParameters();
            cst.setInt(1, objNotaESDet.getEmp_id());
            cst.setInt(2, objNotaESDet.getSuc_id());
            cst.setString(3, objNotaESDet.getNescab_id());
            cst.setString(4, objNotaESDet.getNescab_key());
            cst.setLong(5, objNotaESDet.getNesdet_item());
            cst.setString(6, objNotaESDet.getNesdet_usumod());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino Nota E/S Detalle con Codigo " + objNotaESDet.getNesdet_item());
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

    public ParametrosSalida actualizaSituacionOC(long oc, String nescab_id) throws SQLException, ParseException {
        String SQL_ACTUALIZARSITUACIONOC = "{call pack_tnotaes.p_actualizaSituacionOC(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZARSITUACIONOC);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setLong(3, oc);
            cst.setString(4, nescab_id);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se actualizo la situacion de la o/c " + Utilitarios.lpad(String.valueOf(oc), 10, "0"));
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
            i_flagErrorBD = 1;
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
            i_flagErrorBD = 1;
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NotaESCab> BusquedaNotaES(String s_periodo, String f_fecha, String s_nescab_tipnotaes, int i_estado, int i_despacho, String nota) throws SQLException {
        String SQL_LISTA_FACTURAPROVEEDOR = "";
        //el periodo es un dato obligatorio
        //si eligio fecha + proveedor
        if (s_periodo.isEmpty()) {
            s_periodo = "%%";
        }
        if (!f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty() && nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "'";
        } else if (!f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty() && !nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "'";
        }//si eligio proveedor
        else if (f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty() && nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and t.nescab_tipnotaes='" + s_nescab_tipnotaes + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "'";
        } else if (f_fecha.isEmpty() && !s_nescab_tipnotaes.isEmpty() && !nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and t.nescab_tipnotaes='" + s_nescab_tipnotaes + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "'";
        }//si eligio fecha
        else if (!f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty() && nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "'";
        } else if (!f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty() && !nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and to_char(t.nescab_fecha,'dd/mm/yyyy')='" + f_fecha + "'";
        } //si no eligio nada
        else if (f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty() && nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "'"
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "'";
        } else if (f_fecha.isEmpty() && s_nescab_tipnotaes.isEmpty() && !nota.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                    + " and t.nescab_est ='" + i_estado + "' and t.nescab_despacho='" + i_despacho + "' and t.nescab_id=" + nota
                    + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "'";
        }
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_FACTURAPROVEEDOR);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                //objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setNotaes(rs.getString("notaes").length() < 35 ? rs.getString("notaes") : rs.getString("notaes").substring(0, 35));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_serie(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc") == null ? "" : rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie") == null ? "" : rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                objNotaESCab.setListaprecio(rs.getString("oc_lpcid") == null ? "" : rs.getString("oc_lpcid"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setHora(rs.getTime("nescab_fecha"));
                //objNotaESCab.setNescab_fecadd(new Date().getDate());
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objlstNotaESCab.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESCab.getSize() + " registro(s)");
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> listaNotaESCab() throws SQLException {
        String SQL_NOTAESCAB = "select t.*,c.oc_lpcid from codijisa.v_listanotaescab t,tordcompra_cab c where t.emp_id=c.emp_id and t.suc_id=c.suc_id "
                + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.suc_id=c.suc_id and c.oc_nropedkey(+)=t.nescab_ocnropedkey"
                + " and to_char(t.nescab_fecha,'dd/mm/yyyy')=to_char(sysdate,'dd/mm/yyyy') and t.nescab_est = 1"
                + " union select t.*,null oc_lpcid from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
                + " and t.nescab_ocnropedkey=0 and to_char(t.nescab_fecha,'dd/mm/yyyy')=to_char(sysdate,'dd/mm/yyyy') and t.nescab_est = 1";
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESCAB);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes").length() < 35 ? rs.getString("notaes") : rs.getString("notaes").substring(0, 35));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_serie(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getTimestamp("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc") == null ? "" : rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie") == null ? "" : rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                objNotaESCab.setListaprecio(rs.getString("oc_lpcid") == null ? "" : rs.getString("oc_lpcid"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setHora(rs.getTime("nescab_fecha"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objlstNotaESCab.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESCab.getSize() + " registro(s)");
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> BusquedaContaFactura(String fechai) throws SQLException {

        String s_validafec = fechai.isEmpty() ? "" : " and trunc(d.nescab_fecha) like to_date('" + fechai + "','dd/mm/yyyy') ";
        String SQL_NOTAESCAB = "select d.*,sum(t.nesdet_valafe) v_afecto,sum(t.nesdet_valina) v_inafecto,sum(t.nesdet_vimpto) v_impto,"
                + " sum(t.nesdet_pvta) p_venta,c.oc_lpcid from v_listanotaesdet t,v_listanotaescab d,tordcompra_cab c where  d.emp_id=c.emp_id and d.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + " and t.suc_id='" + objUsuCredential.getCodsuc() + "' and d.nescab_id=t.nescab_id and t.nesdet_est = 1 and c.oc_nropedkey(+)=d.nescab_ocnropedkey" + s_validafec
                + " group by d.nescab_id,d.nescab_key,d.nescab_tipnotaes,d.notaes,d.emp_id,d.suc_id,d.serie,d.nescab_nroped,d.nescab_fecha,d.hora,d.nescab_glosa,d.nescab_situacion,d.nescab_est,"
                + " d.nescab_ocind,d.nescab_ocnropedkey,d.oc,d.nescab_tipdoc,d.nescab_tipdoc_des,d.nescab_nroserie,d.nescab_nrodoc,d.nescab_provid,d.proveedor,d.nescab_cliid,d.cliente,d.nescab_moneda,"
                + " d.nescab_moneda_des,d.nescab_tcamb,d.nescab_almori,d.nescab_almori_des,d.nescab_almdes,d.nescab_almdes_des,d.nescab_costeo,d.nescab_despacho,d.sitdes,d.nescab_nrodep,d.nescab_usuadd,"
                + " d.nescab_fecadd,d.nescab_usumod,d.nescab_fecmod,c.oc_lpcid";
        /*+ " union"
         + " select d.*,sum(t.nesdet_valafe) v_afecto,sum(t.nesdet_valina) v_inafecto,sum(t.nesdet_vimpto) v_impto,sum(t.nesdet_pvta) p_venta,null oc_lpcid"
         + " from v_listanotaesdet t,v_listanotaescab d,tordcompra_cab c where d.emp_id=c.emp_id and d.suc_id=c.suc_id and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'"
         + " and d.nescab_id=t.nescab_id and t.nesdet_est = 1 and d.nescab_ocnropedkey=0" and d.nescab_tipnotaes='001'"
         + " group by d.nescab_id,d.nescab_key,d.nescab_tipnotaes,d.notaes,d.emp_id,d.suc_id,d.serie,d.nescab_nroped,d.nescab_fecha,d.hora,d.nescab_glosa,d.nescab_situacion,d.nescab_est,"
         + " d.nescab_ocind,d.nescab_ocnropedkey,d.oc,d.nescab_tipdoc,d.nescab_tipdoc_des,d.nescab_nroserie,d.nescab_nrodoc,d.nescab_provid,d.proveedor,d.nescab_cliid,d.cliente,d.nescab_moneda,"
         + " d.nescab_moneda_des,d.nescab_tcamb,d.nescab_almori,d.nescab_almori_des,d.nescab_almdes,d.nescab_almdes_des,d.nescab_costeo,d.nescab_despacho,d.sitdes,d.nescab_nrodep,d.nescab_usuadd,"
         + " d.nescab_fecadd,d.nescab_usumod,d.nescab_fecmod,c.oc_lpcid";*/

        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESCAB);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                //objNotaESCab.setNescab_fecha(rs.getTimestamp("nescab_fecha"));
                //objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                //objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setV_afecto(rs.getDouble("v_afecto"));
                objNotaESCab.setV_inafecto(rs.getDouble("v_inafecto"));
                objNotaESCab.setV_impto(rs.getDouble("v_impto"));
                objNotaESCab.setP_venta(rs.getDouble("p_venta"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes").length() < 35 ? rs.getString("notaes") : rs.getString("notaes").substring(0, 35));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_serie(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getTimestamp("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc") == null ? "" : rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie") == null ? "" : rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                objNotaESCab.setListaprecio(rs.getString("oc_lpcid") == null ? "" : rs.getString("oc_lpcid"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setHora(rs.getTime("nescab_fecha"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objlstNotaESCab.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESCab.getSize() + " registro(s)");
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESDet> listaNotaESDet(String nescab_id, int estado) throws SQLException {
        String SQL_NOTAESDET/* = ""*/;
        if (estado == 1) {
            SQL_NOTAESDET = "select * from codijisa.v_listanotaesdet t "
                    + "where t.nesdet_est = 1 and t.nescab_id = '" + nescab_id + "' "
                    + "and t.emp_id = '" + objUsuCredential.getCodemp() + "' "
                    + "and t.suc_id = '" + objUsuCredential.getCodsuc() + "'";
        } else {
            SQL_NOTAESDET = "select * from codijisa.v_listanotaesdet t "
                    + "where t.nesdet_est = 3 and t.nescab_id = '" + nescab_id + "' "
                    + "and t.emp_id = '" + objUsuCredential.getCodemp() + "' "
                    + "and t.suc_id = '" + objUsuCredential.getCodsuc() + "'";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESDET);
            objlstNotaESDet = new ListModelList<NotaESDet>();
            while (rs.next()) {
                objNotaESDet = new NotaESDet();
                objNotaESDet.setNescab_id(rs.getString("nescab_id"));
                objNotaESDet.setNescab_key(rs.getString("nescab_key"));
                objNotaESDet.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESDet.setEmp_id(rs.getInt("emp_id"));
                objNotaESDet.setSuc_id(rs.getInt("suc_id"));
                objNotaESDet.setNesdet_item(rs.getLong("nesdet_item"));
                objNotaESDet.setNesdet_tipmov(rs.getString("nesdet_tipmov"));
                objNotaESDet.setNesdet_est(rs.getInt("nesdet_est"));
                objNotaESDet.setNesdet_provid(rs.getString("nesdet_provid"));
                objNotaESDet.setNesdet_provrazsoc(rs.getString("prov_razsoc"));
                objNotaESDet.setPro_id(rs.getString("pro_id"));
                objNotaESDet.setPro_des(rs.getString("pro_des"));
                objNotaESDet.setPro_desdes(rs.getString("pro_desdes"));
                objNotaESDet.setNesdet_glosa(rs.getString("nesdet_glosa"));
                objNotaESDet.setNesdet_cant(rs.getDouble("nesdet_cant"));
                objNotaESDet.setNesdet_undpre(rs.getInt("nesdet_undpre"));
                objNotaESDet.setNesdet_proconv(rs.getString("nesdet_proconv"));
                objNotaESDet.setNesdet_cantconv(rs.getLong("nesdet_cantconv"));
                objNotaESDet.setNesdet_pimpto(rs.getDouble("nesdet_pimpto"));
                objNotaESDet.setNesdet_vimpto(rs.getDouble("nesdet_vimpto"));
                objNotaESDet.setNesdet_valafe(rs.getDouble("nesdet_valafe"));
                objNotaESDet.setNesdet_valina(rs.getDouble("nesdet_valina"));
                objNotaESDet.setNesdet_pvta(rs.getDouble("nesdet_pvta"));
                objNotaESDet.setNesdet_almori(rs.getString("nesdet_almori"));
                objNotaESDet.setNesdet_almdes(rs.getString("nesdet_almdes"));
                objNotaESDet.setNesdet_ubiori(rs.getString("nesdet_ubiori"));
                objNotaESDet.setNesdet_ubides(rs.getString("nesdet_ubides"));
                objNotaESDet.setNesdet_cantfac(rs.getDouble("nesdet_cantfac"));
                objNotaESDet.setNesdet_peso(rs.getDouble("nesdet_peso"));
                objNotaESDet.setNesdet_cositem(rs.getDouble("nesdet_cositem"));
                objNotaESDet.setNesdet_usuadd(rs.getString("nesdet_usuadd"));
                objNotaESDet.setNesdet_fecadd(rs.getTimestamp("nesdet_fecadd"));
                objNotaESDet.setNesdet_usumod(rs.getString("nesdet_usumod"));
                objNotaESDet.setNesdet_fecmod(rs.getTimestamp("nesdet_fecmod"));
                objlstNotaESDet.add(objNotaESDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESDet.getSize() + " registro(s)");
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
        return objlstNotaESDet;
    }

    public ListModelList<NotaESLin> listaTotalxLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_TOTALLINEA;
        ListModelList<NotaESLin> objlstNotaESLin = new ListModelList<NotaESLin>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALLINEA = "select p.emp_id, p.suc_id, t.tab_id lin_key, t.tab_subdir lin_id, t.tab_subdes lin_des, "
                + "count(distinct x.nescab_id) cant, "
                + "sum(x.nesdet_valafe) valafe, "
                + "sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + "sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + "sum(x.nesdet_vimpto) vimpto, "
                + "sum(x.nesdet_pvta) vtotal "
                + "from ttabgen t,v_listanotaescab p, v_listanotaesdet x "
                + "where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id and p.nescab_tipnotaes='001' "
                + "and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' and t.tab_cod=4 and p.nescab_est=1 and x.nesdet_est=1 "
                + "and to_number(substr(x.pro_id,1,3))= t.tab_id and t.tab_id like '" + lin_key + "' " + validaFecha
                + "group by p.emp_id, p.suc_id, t.tab_id, t.tab_subdir, t.tab_subdes order by t.tab_subdir";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALLINEA);
            NotaESLin objNotaESLin;
            while (rs.next()) {
                objNotaESLin = new NotaESLin();
                objNotaESLin.setEmp_id(rs.getInt("emp_id"));
                objNotaESLin.setSuc_id(rs.getInt("suc_id"));
                objNotaESLin.setLin_key(rs.getInt("lin_key"));
                objNotaESLin.setLin_id(rs.getString("lin_id"));
                objNotaESLin.setLin_des(rs.getString("lin_des"));
                objNotaESLin.setCant(rs.getLong("cant"));
                objNotaESLin.setFecemi(fecini);
                objNotaESLin.setFecfin(fecfin);
                objNotaESLin.setPeriodo(periodo);
                objNotaESLin.setVafecto(rs.getDouble("valafe"));
                objNotaESLin.setVinafecto(rs.getDouble("valina"));
                objNotaESLin.setVexonerado(rs.getDouble("valexo"));
                objNotaESLin.setVimpto(rs.getDouble("vimpto"));
                objNotaESLin.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESLin.add(objNotaESLin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESLin.size() + " Registro(s)");
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
        return objlstNotaESLin;
    }

    public ListModelList<NotaESProv> listaTotalxProveedor(String fecini, String fecfin, String periodo, String provid) throws SQLException {
        ListModelList<NotaESProv> objlstNotaESProv = new ListModelList<NotaESProv>();
        String validaFecha, SQL_TOTALPROVEEDOR;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALPROVEEDOR = "select p.emp_id, p.suc_id, p.nescab_provid provid, p.proveedor proveedor, "
                + "count(distinct x.nescab_key) cant, "
                + "sum(x.nesdet_valafe) valafe, "
                + "sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + "sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + "sum(x.nesdet_vimpto) vimpto, "
                + "sum(x.nesdet_pvta) vtotal "
                + "from v_listanotaescab p, v_listanotaesdet x where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id "
                + "and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' and p.nescab_est=1 and x.nesdet_est=1 "
                + "and p.nescab_provid = x.nesdet_provid and p.nescab_provid like '" + provid + "' " + validaFecha
                + "group by p.emp_id, p.suc_id, p.nescab_provid ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPROVEEDOR);
            NotaESProv objNotaESProv;
            while (rs.next()) {
                objNotaESProv = new NotaESProv();
                objNotaESProv.setEmp_id(rs.getInt("emp_id"));
                objNotaESProv.setSuc_id(rs.getInt("suc_id"));
                objNotaESProv.setProvid(rs.getString("provid"));
                objNotaESProv.setProvrazsoc(rs.getString("proveedor"));
                objNotaESProv.setCant(rs.getLong("cant"));
                objNotaESProv.setFecemi(fecini);
                objNotaESProv.setFecfin(fecfin);
                objNotaESProv.setPeriodo(periodo);
                objNotaESProv.setVafecto(rs.getDouble("valafe"));
                objNotaESProv.setVinafecto(rs.getDouble("valina"));
                objNotaESProv.setVexonerado(rs.getDouble("valexo"));
                objNotaESProv.setVimpto(rs.getDouble("vimpto"));
                objNotaESProv.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESProv.add(objNotaESProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESProv.size() + " Registro(s)");
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
        return objlstNotaESProv;
    }

    public ListModelList<NotaESProd> listaTotalxProducto(String prov_id, String pro_id, String periodo, String fecini, String fecfin) throws SQLException {
        String validaFecha, SQL_TOTALPRODUCTO;
        ListModelList<NotaESProd> objlstNotaESProd = new ListModelList<NotaESProd>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(t.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(t.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and trunc(t.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALPRODUCTO = "select p.emp_id,p.suc_id,p.pro_id,p.pro_des, "
                + "count(p.pro_id) cant, "
                + "sum(p.nesdet_valafe) valafe, "
                + "sum(decode(p.pro_condimp,'I',p.nesdet_valina,0)) valina, "
                + "sum(decode(p.pro_condimp,'E',p.nesdet_valina,0)) valexo, "
                + "sum(p.nesdet_vimpto) vimpto, "
                + "sum(p.nesdet_pvta) vtotal "
                + "from v_listanotaescab t,v_listanotaesdet p where t.emp_id=p.emp_id "
                + "and t.suc_id=p.suc_id and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and t.nescab_id=p.nescab_id and t.nescab_est=1 and p.nesdet_est=1 and t.nescab_tipnotaes='001' "
                + "and p.pro_id like '" + pro_id + "' "
                + "and p.nesdet_provid like '" + prov_id + "' "
                + "and to_char(t.nescab_fecha,'yyyymm') like '" + periodo + "' " + validaFecha
                + "group by p.emp_id,p.suc_id,p.pro_id,p.pro_des order by p.pro_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPRODUCTO);
            NotaESProd objNotaESProd;
            while (rs.next()) {
                objNotaESProd = new NotaESProd();
                objNotaESProd.setEmp_id(rs.getInt("emp_id"));
                objNotaESProd.setSuc_id(rs.getInt("suc_id"));
                /*objNotaESProd.setPro_key(rs.getLong("pro_key"));*/
                objNotaESProd.setPro_id(rs.getString("pro_id"));
                objNotaESProd.setPro_des(rs.getString("pro_des"));
                objNotaESProd.setCant(rs.getLong("cant"));
                objNotaESProd.setFecemi(fecini);
                objNotaESProd.setFecfin(fecfin);
                objNotaESProd.setPeriodo(periodo);
                objNotaESProd.setVafecto(rs.getDouble("valafe"));
                objNotaESProd.setVinafecto(rs.getDouble("valina"));
                objNotaESProd.setVexonerado(rs.getDouble("valexo"));
                objNotaESProd.setVimpto(rs.getDouble("vimpto"));
                objNotaESProd.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESProd.add(objNotaESProd);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESProd.size() + " Registro(s)");
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
        return objlstNotaESProd;
    }

    public ListModelList<NotaESSublin> listaTotalxSubLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_TOTALSUBLINEA;
        ListModelList<NotaESSublin> objlstNotaESSublin = new ListModelList<NotaESSublin>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALSUBLINEA = "select p.emp_id, p.suc_id, t.slin_codslin slin_key, t.slin_cod slin_id, t.slin_des slin_des, "
                + "count(distinct x.nescab_id) cant, "
                + "sum(x.nesdet_valafe) valafe, "
                + "sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + "sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + "sum(x.nesdet_vimpto) vimpto, "
                + "sum(x.nesdet_pvta) vtotal "
                + "from tsublineas t,v_listanotaescab p, v_listanotaesdet x "
                + "where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id and p.nescab_tipnotaes='001'"
                + "and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' and p.nescab_est=1 and x.nesdet_est=1 "
                + "and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod and t.slin_cod like '" + lin_key + "' " + validaFecha
                + "group by p.emp_id, p.suc_id, t.slin_codslin,t.slin_cod, t.slin_des order by t.slin_cod";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALSUBLINEA);
            NotaESSublin objNotaESSublin;
            while (rs.next()) {
                objNotaESSublin = new NotaESSublin();
                objNotaESSublin.setEmp_id(rs.getInt("emp_id"));
                objNotaESSublin.setSuc_id(rs.getInt("suc_id"));
                objNotaESSublin.setSlin_key(rs.getInt("slin_key"));
                objNotaESSublin.setSlin_id(rs.getString("slin_id"));
                objNotaESSublin.setSlin_des(rs.getString("slin_des"));
                objNotaESSublin.setCant(rs.getLong("cant"));
                objNotaESSublin.setFecemi(fecini);
                objNotaESSublin.setFecfin(fecfin);
                objNotaESSublin.setPeriodo(periodo);
                objNotaESSublin.setVafecto(rs.getDouble("valafe"));
                objNotaESSublin.setVinafecto(rs.getDouble("valina"));
                objNotaESSublin.setVexonerado(rs.getDouble("valexo"));
                objNotaESSublin.setVimpto(rs.getDouble("vimpto"));
                objNotaESSublin.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESSublin.add(objNotaESSublin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESSublin.size() + " Registro(s)");
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
        return objlstNotaESSublin;
    }

    public ListModelList<NotaESCab> ListaNotaESxProveedor(String fecini, String fecfin, String periodo, String provid) throws SQLException {
        String validaFecha, SQL_NOTAESXPROVEEDOR, titulo;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE NOTAS E/S POR PROVEEDOR DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PROVEEDOR DESDE 01/01/2000";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PROVEEDOR DESDE " + fecini;
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PROVEEDOR DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_NOTAESXPROVEEDOR = "select distinct p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha,"
                + "p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,"
                + "p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal "
                + "from v_listanotaescab p, tnotaes_det x where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id "
                + "and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' "
                + "and p.nescab_est=1 and x.nesdet_est=1 and p.nescab_provid = x.nesdet_provid and p.nescab_provid like '" + provid + "' " + validaFecha
                + "group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,"
                + "p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + "p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,"
                + "p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,"
                + "p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,"
                + "p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";

        P_WHERER = SQL_NOTAESXPROVEEDOR;
        P_TITULO = titulo;
        objNotaESCab = null;
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXPROVEEDOR);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> ListaNotaESxProducto(String pro_key, String periodo, String fecini, String fecfin) throws SQLException {

        String validaFecha, SQL_NOTAESXPRODUCTO, titulo;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE NOTAS E/S POR PRODUCTO DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PRODUCTO DESDE 01/01/2000";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PRODUCTO DESDE " + fecini;
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR PRODUCTO DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_NOTAESXPRODUCTO = "select distinct p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha, "
                + "p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,"
                + "p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(t.nesdet_pvta) vtotal "
                + "from v_listanotaescab p,v_listanotaesdet t where p.emp_id=t.emp_id and p.suc_id=t.suc_id and p.nescab_key=t.nescab_key and p.nescab_id=t.nescab_id "
                + "and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.nescab_tipnotaes='001' and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' and t.pro_id=" + pro_key + " and p.nescab_est=1 and t.nesdet_est=1 " + validaFecha
                + "group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped, p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + "p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,"
                + "p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";
        P_WHERER = SQL_NOTAESXPRODUCTO;
        P_TITULO = titulo;
        objNotaESCab = null;
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXPRODUCTO);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> ListaNotaESxLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_NOTAESXLINEA, titulo;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE NOTAS E/S POR LINEA DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR LINEA DESDE 01/01/2000";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR LINEA DESDE " + fecini;
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR LINEA DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_NOTAESXLINEA = "select p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + "p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal from v_listanotaescab p, tnotaes_det x, ttabgen t "
                + "where p.nescab_id=x.nescab_id and p.nescab_key=x.nescab_key and p.nescab_tipnotaes=x.nescab_tipnotaes and p.emp_id=x.emp_id and p.suc_id=x.suc_id and t.tab_id like '" + lin_key + "' "
                + "and t.tab_cod=4 and to_number(substr(x.pro_id,1,3))= t.tab_id and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.nescab_est=1 and p.nescab_tipnotaes='001' "
                + "and x.nesdet_est=1 and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' " + validaFecha + " group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,"
                + "p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,"
                + "p.nescab_almori,p.nescab_almdes,p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";

        P_WHERER = SQL_NOTAESXLINEA;
        P_TITULO = titulo;
        objNotaESCab = null;
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXLINEA);

            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESCab> ListaNotaESxSublinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_NOTAESXSUBLINEA, titulo;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE NOTAS E/S POR SUBLINEA DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR SUBLINEA DESDE 01/01/2000";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR SUBLINEA DESDE " + fecini;
        } else {
            validaFecha = "and trunc(p.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE NOTAS E/S POR SUBLINEA DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_NOTAESXSUBLINEA = "select p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + "p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal from v_listanotaescab p, tnotaes_det x, tsublineas t "
                + "where p.nescab_id=x.nescab_id and p.nescab_key=x.nescab_key and p.nescab_tipnotaes=x.nescab_tipnotaes and p.nescab_tipnotaes='001' and p.emp_id=x.emp_id and p.suc_id=x.suc_id and t.slin_cod like '" + lin_key + "' "
                + "and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.nescab_est=1 "
                + "and x.nesdet_est=1 and to_char(p.nescab_fecha,'yyyymm') like '" + periodo + "' " + validaFecha + " group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,"
                + "p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,"
                + "p.nescab_almori,p.nescab_almdes,p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";

        P_WHERER = SQL_NOTAESXSUBLINEA;
        P_TITULO = titulo;
        objNotaESCab = null;
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXSUBLINEA);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    /* NotaES vs Factura***************************************************************************************************************/
    public ListModelList<NotaESProv> listaTotalNotaESvsFacxProveedor(String fecini, String fecfin, String periodo, String provid) throws SQLException {
        String s_validafecha = "";

        if (!fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('01/01/2000','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('01/01/2500','dd/MM/yyyy') ";
        }

        String s_periodo = periodo.isEmpty() ? " and to_char(f.tc_fecemi,'yyyymm') like '%%' " : "and to_char(f.tc_fecemi,'yyyymm') like '" + periodo + "' ";

        ListModelList<NotaESProv> objlstNotaESProv = new ListModelList<NotaESProv>();
        String SQL_TOTALPROVEEDOR;
        SQL_TOTALPROVEEDOR = "select p.emp_id, p.suc_id,f.tc_fecemi, p.nescab_provid provid, p.proveedor proveedor, "
                + "count(distinct x.nescab_key) cant, "
                + "sum(x.nesdet_valafe) valafe, "
                + "sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + "sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + "sum(x.nesdet_vimpto) vimpto, "
                + "sum(x.nesdet_pvta) vtotal "
                + "from v_listanotaescab p, v_listanotaesdet x, v_listafacturaproveedorcab f , v_listafacturaproveedordet d "
                + "where p.nescab_id = x.nescab_id and f.tc_key = d.tc_key "
                + "and p.emp_id=x.emp_id and p.suc_id=x.suc_id and f.emp_id = d.emp_id and f.suc_id=d.suc_id "
                + "and p.oc = f.tc_oc and d.pro_id = x.pro_id and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and p.nescab_est=1 and x.nesdet_est=1 and f.tc_est =1 "
                + "and p.nescab_provid = x.nesdet_provid and p.nescab_provid like '" + provid + "' " + s_periodo + s_validafecha
                + "group by  p.nescab_provid , f.tc_fecemi,p.emp_id, p.suc_id ,p.proveedor "
                + "order by p.nescab_provid ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPROVEEDOR);
            NotaESProv objNotaESProv;
            while (rs.next()) {
                objNotaESProv = new NotaESProv();
                objNotaESProv.setEmp_id(rs.getInt("emp_id"));
                objNotaESProv.setSuc_id(rs.getInt("suc_id"));
                objNotaESProv.setProvid(rs.getString("provid"));
                objNotaESProv.setProvrazsoc(rs.getString("proveedor"));
                objNotaESProv.setCant(rs.getLong("cant"));
                objNotaESProv.setFecemi(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("tc_fecemi")));
                objNotaESProv.setPeriodo(periodo);
                objNotaESProv.setVafecto(rs.getDouble("valafe"));
                objNotaESProv.setVinafecto(rs.getDouble("valina"));
                objNotaESProv.setVexonerado(rs.getDouble("valexo"));
                objNotaESProv.setVimpto(rs.getDouble("vimpto"));
                objNotaESProv.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESProv.add(objNotaESProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESProv.size() + " Registro(s)");
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
        return objlstNotaESProv;
    }

    public ListModelList<NotaESCab> ListaNotaESvsFacxProveedor(String fecemi, String provid) throws SQLException {
        String SQL_NOTAESXPROVEEDOR, titulo;

        titulo = "REPORTE DE NOTAS E/S VS. FACTURA POR PROVEEDOR DESDE " + fecemi;

        SQL_NOTAESXPROVEEDOR = "select distinct p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped, p.nescab_fecha, "
                + "p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,"
                + "p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal "
                + "from v_listanotaescab p, tnotaes_det x ,  v_listafacturaproveedorcab f,v_listafacturaproveedordet d "
                + "where p.nescab_key  = x.nescab_key and p.nescab_id = x. nescab_id and p.emp_id  = x.emp_id and p.suc_id  = x.suc_id "
                + "and p.oc = f.tc_oc "
                + "and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id "
                + "and x.pro_id = d.pro_id and x.emp_id = d.emp_id and x.suc_id = d.suc_id "
                + "and f.tc_est =1 and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and trunc(f.tc_fecemi) = to_date('" + fecemi + "','dd/MM/yyyy') and p.nescab_provid like '" + provid + "' and x.nesdet_est=1 "
                + "group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped, "
                + "f.tc_fecemi,p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey, "
                + "p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid, proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda, "
                + "p.nescab_tcamb,p.nescab_almori,p.nescab_almdes, p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd, "
                + "p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod "
                + "order by p.nescab_id ";

        P_WHERER = SQL_NOTAESXPROVEEDOR;
        P_TITULO = titulo;

        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXPROVEEDOR);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESProd> listaTotalNotaESvsFacxProducto(String prov_id, String pro_id, String periodo, String fecini, String fecfin) throws SQLException {
        String SQL_TOTALPRODUCTO, s_validafecha = "";
        ListModelList<NotaESProd> objlstNotaESProd = new ListModelList<NotaESProd>();

        if (!fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = "and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = "and trunc(f.tc_fecemi) between to_date('01/01/2000','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            s_validafecha = "and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('01/01/2500','dd/MM/yyyy') ";
        }

        String s_periodo = periodo.isEmpty() ? " and to_char(f.tc_fecemi,'yyyymm') like '%%' " : "and to_char(f.tc_fecemi,'yyyymm') like '" + periodo + "' ";

        SQL_TOTALPRODUCTO = "select p.emp_id,p.suc_id,f.tc_fecemi,p.pro_id,p.pro_des, "
                + " count(p.pro_id) cant, "
                + " sum(p.nesdet_vimpto) vimpto, "
                + " sum(p.nesdet_valafe) valafe,"
                + " sum(decode(p.pro_condimp,'I',p.nesdet_valina,0)) valina, "
                + " sum(decode(p.pro_condimp,'E',p.nesdet_valina,0)) valexo, "
                + " sum(p.nesdet_pvta) vtotal "
                + " from v_listanotaescab t,v_listanotaesdet p , v_listafacturaproveedorcab f , v_listafacturaproveedordet d "
                + " where t.emp_id = p.suc_id and t.suc_id=p.suc_id  and t.nescab_key = p.nescab_key and t.nescab_id = p.nescab_id "
                + " and t.oc = f.tc_oc and t.emp_id = f.emp_id and t.suc_id = f.suc_id"
                + " and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id"
                + " and d.pro_id = p.pro_id  and f.tc_key = d.tc_key "
                + " and p.emp_id =" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + " and p.pro_id like '" + pro_id + "' and p.nesdet_provid like '" + prov_id + "' and p.nesdet_est=1 "
                + s_periodo + s_validafecha
                + " group by p.pro_id,p.pro_des,p.emp_id, f.tc_fecemi,p.suc_id "
                + " order by p.pro_id ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPRODUCTO);
            NotaESProd objNotaESProd;
            while (rs.next()) {
                objNotaESProd = new NotaESProd();
                objNotaESProd.setEmp_id(rs.getInt("emp_id"));
                objNotaESProd.setSuc_id(rs.getInt("suc_id"));
                objNotaESProd.setPro_id(rs.getString("pro_id"));
                objNotaESProd.setPro_des(rs.getString("pro_des"));
                objNotaESProd.setCant(rs.getLong("cant"));
                objNotaESProd.setFecemi(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("tc_fecemi")));
                objNotaESProd.setVafecto(rs.getDouble("valafe"));
                objNotaESProd.setVinafecto(rs.getDouble("valina"));
                objNotaESProd.setVexonerado(rs.getDouble("valexo"));
                objNotaESProd.setVimpto(rs.getDouble("vimpto"));
                objNotaESProd.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESProd.add(objNotaESProd);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESProd.size() + " Registro(s)");
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
        return objlstNotaESProd;
    }

    public ListModelList<NotaESCab> ListaNotaESvsFacxProducto(String pro_id, String fecemi) throws SQLException {
        String SQL_NOTAESXPRODUCTO, titulo;
        titulo = "REPORTE DE NOTAS E/S VS. FACTURA  POR PRODUCTO DESDE " + fecemi;
        SQL_NOTAESXPRODUCTO = "select distinct p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha, "
                + "p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,"
                + "p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + "p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(t.nesdet_pvta) vtotal "
                + "from v_listanotaescab p,v_listanotaesdet t, v_listafacturaproveedorcab f ,  v_listafacturaproveedordet d "
                + "where p.emp_id=t.emp_id and p.suc_id=t.suc_id and p.nescab_key=t.nescab_key and p.nescab_id=t.nescab_id and f.tc_key = d.tc_key and t.pro_id = d.pro_id "
                + "and p.emp_id=" + objUsuCredential.getCodemp() + "and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.oc = f.tc_oc  and f.tc_fecemi = to_date('" + fecemi + "','dd/MM/yyyy') "
                + "and p.nescab_est=1 and t.nesdet_est=1  and t.pro_id ='" + pro_id + "' "
                + "group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped, p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + "p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,"
                + "p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";

        P_WHERER = SQL_NOTAESXPRODUCTO;
        P_TITULO = titulo;

        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXPRODUCTO);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESLin> listaTotalNotaESvsFacxLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String SQL_TOTALLINEA, s_validafecha = "";
        ListModelList<NotaESLin> objlstNotaESLin = new ListModelList<NotaESLin>();

        if (!fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('01/01/2000','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('01/01/2500','dd/MM/yyyy') ";
        }

        String s_periodo = periodo.isEmpty() ? " and to_char(f.tc_fecemi,'yyyymm') like '%%' " : "and to_char(f.tc_fecemi,'yyyymm') like '" + periodo + "' ";

        SQL_TOTALLINEA = "select p.emp_id, p.suc_id, f.tc_fecemi, t.tab_id lin_key, t.tab_subdir lin_id, t.tab_subdes lin_des, "
                + " count(distinct x.nescab_id) cant, "
                + " sum(x.nesdet_valafe) valafe, "
                + " sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + " sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + " sum(x.nesdet_vimpto) vimpto, "
                + " sum(x.nesdet_pvta) vtotal "
                + " from ttabgen t,v_listanotaescab p, v_listanotaesdet x, v_listafacturaproveedorcab f , v_listafacturaproveedordet d  "
                + " where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id and p.nescab_key = x.nescab_key "
                + " and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id "
                + " and f.tc_oc = p.oc and f.emp_id = p.emp_id and f.suc_id = p.suc_id "
                + " and x.pro_id= d.pro_id and x.emp_id = d.emp_id and x.suc_id = d.suc_id "
                + " and f.tc_oc = p.oc and f.emp_id = p.emp_id and f.suc_id= p.suc_id "
                + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and t.tab_cod=4 and p.nescab_est=1 and x.nesdet_est=1 and to_number(substr(x.pro_id,1,3))= t.tab_id and t.tab_id like '" + lin_key + "' "
                + s_validafecha + s_periodo
                + "group by p.emp_id, p.suc_id, f.tc_fecemi, t.tab_id, t.tab_subdir, t.tab_subdes "
                + "order by t.tab_id ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALLINEA);
            NotaESLin objNotaESLin;

            while (rs.next()) {
                objNotaESLin = new NotaESLin();
                objNotaESLin.setEmp_id(rs.getInt("emp_id"));
                objNotaESLin.setSuc_id(rs.getInt("suc_id"));
                objNotaESLin.setLin_key(rs.getInt("lin_key"));
                objNotaESLin.setLin_id(rs.getString("lin_id"));
                objNotaESLin.setLin_des(rs.getString("lin_des"));
                objNotaESLin.setCant(rs.getLong("cant"));
                objNotaESLin.setFecemi(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("tc_fecemi")));
                objNotaESLin.setVimpto(rs.getDouble("vimpto"));
                objNotaESLin.setVafecto(rs.getDouble("valafe"));
                objNotaESLin.setVinafecto(rs.getDouble("valina"));
                objNotaESLin.setVexonerado(rs.getDouble("valexo"));
                objNotaESLin.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESLin.add(objNotaESLin);

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESLin.size() + " Registro(s)");
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
        return objlstNotaESLin;

    }

    public ListModelList<NotaESCab> ListaNotaESvsFacxLinea(String fecemi, String lin_key) throws SQLException {
        String SQL_NOTAESXLINEA, titulo;
        titulo = "REPORTE DE NOTAS E/S VS. FACTURA POR LINEA DESDE " + fecemi;
        SQL_NOTAESXLINEA = "select p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + " p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + " p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal "
                + " from v_listanotaescab p, tnotaes_det x, ttabgen t ,v_listafacturaproveedorcab f,v_listafacturaproveedordet d "
                + " where p.nescab_id=x.nescab_id and p.nescab_key=x.nescab_key and p.nescab_id=x.nescab_id and p.emp_id=x.emp_id and p.suc_id=x.suc_id "
                + " and x.pro_id = d.pro_id and x.emp_id = d.emp_id and x.suc_id = d.suc_id "
                + " and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id "
                + " and p.oc = f.tc_oc and p.emp_id = f.emp_id and p.suc_id = f.suc_id "
                + " and t.tab_cod=4 and to_number(substr(x.pro_id,1,3))= t.tab_id and p.nescab_est=1 and x.nesdet_est=1 "
                + " and t.tab_id like '" + lin_key + "' and p.emp_id=" + objUsuCredential.getCodemp() + "and p.suc_id=" + objUsuCredential.getCodsuc() + " and f.tc_fecemi = to_date('" + fecemi + "','dd/MM/yyyy')"
                + " group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,"
                + " p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,"
                + " p.nescab_almori,p.nescab_almdes,p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id ";

        P_WHERER = SQL_NOTAESXLINEA;
        P_TITULO = titulo;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXLINEA);

            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }
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
        return objlstNotaESCab;
    }

    public ListModelList<NotaESSublin> listaTotalESvsFacxSubLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String SQL_TOTALSUBLINEA, s_validafecha = "";
        ListModelList<NotaESSublin> objlstNotaESSublin = new ListModelList<NotaESSublin>();
        if (!fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('01/01/2000','dd/MM/yyyy') and to_date('" + fecfin + "','dd/MM/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            s_validafecha = " and trunc(f.tc_fecemi) between to_date('" + fecini + "','dd/MM/yyyy') and to_date('01/01/2500','dd/MM/yyyy') ";
        }
        String s_periodo = periodo.isEmpty() ? " and to_char(f.tc_fecemi,'yyyymm') like '%%' " : "and to_char(f.tc_fecemi,'yyyymm') like '" + periodo + "' ";

        SQL_TOTALSUBLINEA = "select p.emp_id, p.suc_id, f.tc_fecemi, t.slin_codslin slin_key, t.slin_cod slin_id, t.slin_des slin_des, "
                + " count(distinct x.nescab_id) cant, "
                + " sum(x.nesdet_valafe) valafe, "
                + " sum(decode(x.pro_condimp,'I',x.nesdet_valina,0)) valina, "
                + " sum(decode(x.pro_condimp,'E',x.nesdet_valina,0)) valexo, "
                + " sum(x.nesdet_vimpto) vimpto, "
                + " sum(x.nesdet_pvta) vtotal "
                + " from tsublineas t,v_listanotaescab p, v_listanotaesdet x,  v_listafacturaproveedorcab f , v_listafacturaproveedordet d "
                + " where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.nescab_id=x.nescab_id "
                + " and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id "
                + " and f.tc_oc = p.oc and f.emp_id = p.emp_id and f.suc_id = p.suc_id "
                + " and  x.pro_id = d.pro_id and x.emp_id = d.emp_id and x.suc_id = d.suc_id "
                + " and f.emp_id = p.emp_id and f.suc_id= p.suc_id "
                + " and p.nescab_est=1 and x.nesdet_est=1 and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod "
                + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " "
                + " and t.slin_cod like '" + lin_key + "' " + s_periodo + s_validafecha
                + " group by p.emp_id, p.suc_id, f.tc_fecemi, t.slin_codslin,t.slin_cod, t.slin_des "
                + " order by t.slin_cod, t.slin_codslin ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALSUBLINEA);
            NotaESSublin objNotaESSublin;
            while (rs.next()) {
                objNotaESSublin = new NotaESSublin();
                objNotaESSublin.setEmp_id(rs.getInt("emp_id"));
                objNotaESSublin.setSuc_id(rs.getInt("suc_id"));
                objNotaESSublin.setSlin_key(rs.getInt("slin_key"));
                objNotaESSublin.setSlin_id(rs.getString("slin_id"));
                objNotaESSublin.setSlin_des(rs.getString("slin_des"));
                objNotaESSublin.setCant(rs.getLong("cant"));
                objNotaESSublin.setFecemi(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("tc_fecemi")));
                objNotaESSublin.setVafecto(rs.getDouble("valafe"));
                objNotaESSublin.setVinafecto(rs.getDouble("valina"));
                objNotaESSublin.setVexonerado(rs.getDouble("valexo"));
                objNotaESSublin.setVimpto(rs.getDouble("vimpto"));
                objNotaESSublin.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESSublin.add(objNotaESSublin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstNotaESSublin.size() + " Registro(s)");
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
        return objlstNotaESSublin;
    }

    public ListModelList<NotaESCab> ListaNotaESvsFacxSublinea(String fecemi, String lin_key) throws SQLException {
        String SQL_NOTAESXSUBLINEA, titulo;
        titulo = "REPORTE DE NOTAS E/S VS. FACTURA POR SUBLINEA DESDE " + fecemi;
        SQL_NOTAESXSUBLINEA = " select p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,"
                + " p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,p.nescab_almori,p.nescab_almdes,p.nescab_costeo,"
                + " p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod, sum(x.nesdet_pvta) vtotal "
                + " from v_listanotaescab p, tnotaes_det x, tsublineas t, v_listafacturaproveedorcab f,v_listafacturaproveedordet d "
                + " where p.nescab_id=x.nescab_id and p.nescab_key=x.nescab_key and p.emp_id=x.emp_id and p.suc_id=x.suc_id "
                + " and x.pro_id = d.pro_id and x.emp_id = d.emp_id and x.suc_id = d.suc_id "
                + " and f.tc_key = d.tc_key and f.emp_id = d.emp_id and f.suc_id = d.suc_id "
                + " and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod "
                + " and p.oc = f.tc_oc and p.emp_id = f.emp_id and p.suc_id = f.suc_id "
                + " and t.slin_cod like '" + lin_key + "' and p.emp_id=" + objUsuCredential.getCodemp() + "and p.suc_id=" + objUsuCredential.getCodsuc() + " and f.tc_fecemi = to_date('" + fecemi + "','dd/MM/yyyy')"
                + " group by p.nescab_id,p.nescab_key,p.nescab_tipnotaes,p.notaes,p.emp_id,p.suc_id,p.serie,p.nescab_nroped,"
                + "p.nescab_fecha,p.nescab_glosa, p.nescab_situacion, p.nescab_est,p.nescab_ocind,p.nescab_ocnropedkey,p.oc,p.nescab_tipdoc,p.nescab_nroserie,p.nescab_nrodoc,p.nescab_provid,p.proveedor,p.nescab_cliid,p.cliente,p.nescab_moneda,p.nescab_tcamb,"
                + "p.nescab_almori,p.nescab_almdes,p.nescab_costeo,p.nescab_despacho,p.sitdes,p.nescab_nrodep,p.nescab_usuadd,p.nescab_fecadd,p.nescab_usumod,p.nescab_fecmod order by p.nescab_id";

        P_WHERER = SQL_NOTAESXSUBLINEA;
        P_TITULO = titulo;
        objlstNotaESCab = new ListModelList<NotaESCab>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NOTAESXSUBLINEA);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes"));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_sernotaes(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor"));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getTimestamp("nescab_fecadd"));
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getTimestamp("nescab_fecmod"));
                objNotaESCab.setVtotal(rs.getDouble("vtotal"));
                objlstNotaESCab.add(objNotaESCab);
            }

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
        return objlstNotaESCab;
    }
}
