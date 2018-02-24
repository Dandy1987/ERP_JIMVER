package org.me.gj.controller.cxc.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.Categoria;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
import org.me.gj.model.cxc.mantenimiento.CliUtil;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.FormaPago;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.logistica.mantenimiento.Condicion;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCliente {

    //Instancias de Objetos
    ListModelList<TipoDocumento> objlstTipoDocumento;
    ListModelList<Categoria> objlstCategoria;
    ListModelList<Moneda> objlstMoneda;
    ListModelList<Condicion> objlstCondicion;
    ListModelList<ListaPrecio> objlstLpventas;
    ListModelList<Canal> objlstCanal;
    ListModelList<FormaPago> objlstFormaPago;
    ListModelList<Cliente> objlstCliente;
    ListModelList<CliDireccion> objlstCliDireccion;
    ListModelList<CliTelefono> objlstCliTelefono;
    ListModelList<CliFinanciero> objlstCliFinanciero;
    ListModelList<CliGarantia> objlstCliGarantia;
    TipoDocumento objTipoDocumento;
    Categoria objCategoria;
    Moneda objMoneda;
    Condicion objCondicion;
    ListaPrecio objLpventas;
    Canal objCanal;
    FormaPago objFormaPago;
    Cliente objCliente;
    CliDireccion objCliDireccion;
    CliTelefono objCliTelefono;
    CliFinanciero objCliFinanciero;
    CliGarantia objCliGarantia;
    CliUtil objCliUtil;
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
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCliente.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarCliente(Cliente objCliente) throws SQLException, ParseException {
        String codigo = "";
        String SQL_INSERTAR_CLIENTE = "{call PACK_TCLIENTES.p_001_insertarCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CLIENTE);
            cst.clearParameters();
            cst.setString("c_cli_apepat", objCliente.getCli_apepat());
            cst.setString("c_cli_apemat", objCliente.getCli_apemat());
            cst.setString("c_cli_nombre", objCliente.getCli_nombre());
            cst.setString("c_cli_razsoc", objCliente.getCli_razsoc());
            cst.setDate("d_cli_fecnac", objCliente.getCli_fecnac() == null ? null : new java.sql.Date(objCliente.getCli_fecnac().getTime()));
            cst.setString("c_cli_dni", objCliente.getCli_dni());
            cst.setString("n_cli_ruc", String.valueOf(objCliente.getCli_ruc()));
            cst.setInt("n_cli_perju", objCliente.getCli_perju());
            cst.setString("c_cli_dirweb", objCliente.getCli_dirweb());
            cst.setString("c_cli_email1", objCliente.getCli_email1());
            cst.setString("c_cli_email2", objCliente.getCli_email2());
            cst.setDouble("n_cli_credcor", objCliente.getCli_limcredcorp());
            cst.setInt("n_cli_doccor", objCliente.getCli_limdoccorp());
            cst.setString("c_cli_usuadd", objCliente.getCli_usuadd());
            cst.setInt("n_cli_tipodoc", objCliente.getCli_tipodoc());
            cst.setDouble("n_cli_dscto", objCliente.getCli_dscto());
            //cst.setInt("n_cli_factura", objCliente.getCli_factura());
            cst.setInt("n_cli_moneda", objCliente.getCli_mon());
            //cst.setInt("n_cli_lista", objCliente.getCli_lista());
            cst.setInt("n_cli_condicion", objCliente.getCli_con());
            cst.setInt("n_cli_canal", objCliente.getCli_canal());
            cst.setInt("n_cli_fpagkey", objCliente.getForpag_key());
            cst.setString("c_cli_fpagid", objCliente.getForpag_id());
            cst.setInt("n_cli_emprel", objCliente.getCli_emprel());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.registerOutParameter("c_clistr", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            codigo = cst.getString("c_clistr");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliente.getCli_razsoc());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg, codigo);
    }

    public ParametrosSalida actualizarCliente(Cliente objCliente) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_CLIENTE = "{call PACK_TCLIENTES.p_001_actualizarCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CLIENTE);
            cst.clearParameters();
            cst.setString("c_cli_id", objCliente.getCli_id());
            cst.setLong("n_cli_key", objCliente.getCli_key());
            cst.setString("c_cli_apepat", objCliente.getCli_apepat());
            cst.setString("c_cli_apemat", objCliente.getCli_apemat());
            cst.setString("c_cli_nombre", objCliente.getCli_nombre());
            cst.setString("c_cli_razsoc", objCliente.getCli_razsoc());
            cst.setInt("n_cli_perju", objCliente.getCli_perju());
            if (objCliente.getCli_fecnac() == null) {
                cst.setDate("d_cli_fecnac", null);
            } else {
                cst.setDate("d_cli_fecnac", new java.sql.Date(objCliente.getCli_fecnac().getTime()));
            }
            cst.setString("c_cli_dni", objCliente.getCli_dni());
            cst.setLong("n_cli_ruc", objCliente.getCli_ruc());
            cst.setString("c_cli_dirweb", objCliente.getCli_dirweb());
            cst.setString("c_cli_email1", objCliente.getCli_email1());
            cst.setString("c_cli_email2", objCliente.getCli_email2());
            cst.setDouble("n_cli_credcor", objCliente.getCli_limcredcorp());
            cst.setInt("n_cli_doccor", objCliente.getCli_limdoccorp());
            cst.setInt("n_cli_est", objCliente.getCli_est());
            cst.setString("c_cli_usumod", objCliente.getCli_usumod());
            cst.setInt("n_cli_tipodoc", objCliente.getCli_tipodoc());
            cst.setDouble("n_cli_dscto", objCliente.getCli_dscto());
            //cst.setInt("n_cli_factura", objCliente.getCli_factura());
            cst.setInt("n_cli_moneda", objCliente.getCli_mon());
            //cst.setInt("n_cli_lista", objCliente.getCli_lista());
            cst.setInt("n_cli_condicion", objCliente.getCli_con());
            cst.setInt("n_cli_canal", objCliente.getCli_canal());
            cst.setInt("n_cli_fpagkey", objCliente.getForpag_key());
            cst.setString("c_cli_fpagid", objCliente.getForpag_id());
            cst.setInt("n_cli_emprel", objCliente.getCli_emprel());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objCliente.getCli_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public String eliminarCliente(long llave, String codigo) throws SQLException {
        String SQL_ELIMINAR_CLIENTE = "{call PACK_TCLIENTES.p_001_eliminarCliente(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CLIENTE);
            cst.clearParameters();
            cst.setString(1, codigo);
            cst.setLong(2, llave);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + codigo);
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

    public ParametrosSalida insertarDireccion(CliDireccion objCliDireccion) throws SQLException, ParseException {
        String SQL_INSERTAR_DIRECCION = "{call PACK_TCLIENTES.p_002_insertarDireccion(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_DIRECCION);
            cst.clearParameters();
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_cli_id", objCliDireccion.getCli_id());
            cst.setLong("n_cli_key", objCliDireccion.getCli_key());
            cst.setString("c_zon_id", objCliDireccion.getZon_id());
            cst.setInt("n_hor_key", Integer.parseInt(objCliDireccion.getHor_id()));
            cst.setInt("n_clizon_default", objCliDireccion.getClizon_default());
            cst.setString("c_clidir_direc", objCliDireccion.getClidir_direc());
            cst.setInt("n_clidir_idvia", objCliDireccion.getClidir_idvia());
            cst.setString("c_clidir_dnomvia", objCliDireccion.getClidir_nomvia());
            cst.setInt("n_clidir_numvia", objCliDireccion.getClidir_numvia());
            cst.setInt("n_clidir_idint", objCliDireccion.getClidir_idint());
            cst.setLong("n_clidir_nroint", objCliDireccion.getClidir_nroint());
            cst.setString("c_clidir_mza", objCliDireccion.getClidir_mza());
            cst.setString("c_clidir_lote", objCliDireccion.getClidir_lote());
            cst.setInt("n_clidir_idseg", objCliDireccion.getClidir_idseg());
            cst.setString("c_clidir_nomseg", objCliDireccion.getClidir_nomseg());
            cst.setString("c_clidir_ref", objCliDireccion.getClidir_ref());
            cst.setString("c_clidir_usuadd", objCliDireccion.getClidir_usuadd());
            cst.setInt("n_clidir_giro", Integer.parseInt(objCliDireccion.getGiro_id()));
            cst.setInt("n_clidir_postal", Integer.parseInt(objCliDireccion.getPos_id()));
            cst.setString("c_clidir_idubigeo", objCliDireccion.getUbi_id());
            cst.setInt("n_cli_lista", objCliDireccion.getCli_lista());
            cst.setInt("n_cli_factura", objCliDireccion.getCli_factura());
            cst.setInt("n_cli_perc", objCliDireccion.getCli_perc());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliDireccion.getClidir_direc());
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

    public ParametrosSalida actualizarDireccion(CliDireccion objCliDireccion) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_DIRECCION = "{call PACK_TCLIENTES.p_002_actualizarDireccion(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_DIRECCION);
            cst.clearParameters();
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setLong("n_suc_id", objUsuCredential.getCodsuc());
            cst.setLong("n_clidir_id", objCliDireccion.getClidir_id());
            cst.setString("c_cli_id", objCliDireccion.getCli_id());
            cst.setLong("n_cli_key", objCliDireccion.getCli_key());
            cst.setString("c_zon_id", objCliDireccion.getZon_id());
            cst.setInt("n_hor_key", Integer.parseInt(objCliDireccion.getHor_id()));
            cst.setInt("n_clizon_default", objCliDireccion.getClizon_default());
            cst.setInt("n_cli_lista", objCliDireccion.getCli_lista());
            cst.setInt("n_cli_factura", objCliDireccion.getCli_factura());
            cst.setInt("n_cli_perc", objCliDireccion.getCli_perc());
            cst.setInt("n_clizon_est", objCliDireccion.getClizon_est());
            cst.setString("c_clidir_direc", objCliDireccion.getClidir_direc());
            cst.setInt("n_clidir_idvia", objCliDireccion.getClidir_idvia());
            cst.setString("c_clidir_dnomvia", objCliDireccion.getClidir_nomvia());
            cst.setInt("n_clidir_numvia", objCliDireccion.getClidir_numvia());
            cst.setInt("n_clidir_idint", objCliDireccion.getClidir_idint());
            cst.setLong("n_clidir_nroint", objCliDireccion.getClidir_nroint());
            cst.setString("c_clidir_mza", objCliDireccion.getClidir_mza());
            cst.setString("c_clidir_lote", objCliDireccion.getClidir_lote());
            cst.setInt("n_clidir_idseg", objCliDireccion.getClidir_idseg());
            cst.setString("c_clidir_nomseg", objCliDireccion.getClidir_nomseg());
            cst.setString("c_clidir_ref", objCliDireccion.getClidir_ref());
            cst.setString("c_clidir_usumod", objCliDireccion.getClidir_usumod());
            cst.setInt("n_clidir_giro", objCliDireccion.getGiro_id() == null ? 0 : Integer.parseInt(objCliDireccion.getGiro_id()));
            cst.setInt("n_clidir_postal", objCliDireccion.getPos_id() == null ? 0 : Integer.parseInt(objCliDireccion.getPos_id()));
            cst.setString("c_clidir_idubigeo", objCliDireccion.getUbi_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliDireccion.getClidir_direc());
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

    public ParametrosSalida eliminarDireccion(CliDireccion objClidireccion) throws SQLException {
        String SQL_ELIMINAR_DIRECCION = "{call PACK_TCLIENTES.p_002_eliminarDireccion(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_DIRECCION);
            cst.clearParameters();
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.setLong("n_suc_id", objUsuCredential.getCodsuc());
            cst.setString("c_cli_id", objClidireccion.getCli_id());
            cst.setLong("n_cli_key", objClidireccion.getCli_key());
            cst.setLong("n_clidir_id", objClidireccion.getClidir_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objClidireccion.getClidir_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarTelefono(CliTelefono objCliTelefono) throws SQLException, ParseException {
        String SQL_INSERTAR_TELEFONO = "{call PACK_TCLIENTES.p_003_insertarTelefono(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TELEFONO);
            cst.clearParameters();
            cst.setString("c_cli_id", objCliTelefono.getCli_id());
            cst.setLong("n_cli_key", objCliTelefono.getCli_key());
            cst.setLong("n_clitel_telef1", objCliTelefono.getClitel_telef1());
            cst.setLong("n_clitel_telef2", objCliTelefono.getClitel_telef2());
            cst.setLong("n_clitel_movil", objCliTelefono.getClitel_movil());
            cst.setString("c_clitel_usuadd", objCliTelefono.getClitel_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliTelefono.getClitel_telef1());
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

    public ParametrosSalida actualizarTelefono(CliTelefono objCliTelefono) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_TELEFONO = "{call PACK_TCLIENTES.p_003_actualizarTelefono(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TELEFONO);
            cst.clearParameters();
            // cst.setLong("n_clitel_id", objCliTelefono.getClitel_id());
            cst.setString("c_cli_id", objCliTelefono.getCli_id());
            cst.setLong("n_cli_key", objCliTelefono.getCli_key());
            cst.setLong("n_clitel_telef1", objCliTelefono.getClitel_telef1());
            cst.setLong("n_clitel_telef2", objCliTelefono.getClitel_telef2());
            cst.setLong("n_clitel_movil", objCliTelefono.getClitel_movil());
            cst.setInt("n_clitel_est", objCliTelefono.getClitel_est());
            cst.setString("c_clitel_usumod", objCliTelefono.getClitel_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliTelefono.getClitel_telef1());
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

    public ParametrosSalida eliminarTelefono(CliTelefono objCliTelefono) throws SQLException {
        String SQL_ELIMINAR_DIRECCION = "{call PACK_TCLIENTES.p_003_eliminarTelefono(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_DIRECCION);
            cst.clearParameters();
            cst.setLong("n_clitel_id", objCliTelefono.getClitel_id());
            cst.setString("c_cli_id", objCliTelefono.getCli_id());
            cst.setLong("n_cli_key", objCliTelefono.getCli_key());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objCliTelefono.getClitel_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarFinanciero(CliFinanciero objCliFinanciero) throws SQLException, ParseException {
        String SQL_INSERTAR_FINANCIERO = "{call PACK_TCLIENTES.p_004_insertarFinanciero(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_FINANCIERO);
            cst.clearParameters();
            cst.setString("c_cli_id", objCliFinanciero.getCli_id());
            cst.setLong("n_cli_key", objCliFinanciero.getCli_key());
            cst.setInt("n_suc_id", objCliFinanciero.getSuc_id());
            cst.setInt("n_emp_id", objCliFinanciero.getEmp_id());
            cst.setDouble("n_clifin_limcred", objCliFinanciero.getClifin_limcred());
            cst.setInt("n_clifin_limdoc", objCliFinanciero.getClifin_limdoc());
            cst.setString("c_clifin_usuadd", objCliFinanciero.getClifin_usuadd());
            cst.setInt("n_clifin_categ", objCliFinanciero.getClifin_categ());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliFinanciero.getClifin_categ());
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

    public ParametrosSalida actualizarFinanciero(CliFinanciero objCliFinanciero) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_FINANCIERO = "{call PACK_TCLIENTES.p_004_actualizarFinanciero(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_FINANCIERO);
            cst.clearParameters();
            //cst.setLong("n_clifin_id", objCliFinanciero.getClifin_id());
            cst.setString("c_cli_id", objCliFinanciero.getCli_id());
            cst.setLong("n_cli_key", objCliFinanciero.getCli_key());
            cst.setInt("n_suc_id", objCliFinanciero.getSuc_id());
            cst.setInt("n_emp_id", objCliFinanciero.getEmp_id());
            cst.setDouble("n_clifin_limcred", objCliFinanciero.getClifin_limcred());
            cst.setInt("n_clifin_limdoc", objCliFinanciero.getClifin_limdoc());
            cst.setInt("n_clifin_est", objCliFinanciero.getClifin_est());
            cst.setString("c_clifin_usumod", objCliFinanciero.getClifin_usumod());
            cst.setInt("n_clifin_categ", objCliFinanciero.getClifin_categ());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliFinanciero.getClifin_id());
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

    public ParametrosSalida eliminarFinanciero(CliFinanciero objCliFinanciero) throws SQLException {
        String SQL_ELIMINAR_FINANCIERO = "{call PACK_TCLIENTES.p_004_eliminarFinanciero(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_FINANCIERO);
            cst.clearParameters();
            cst.setLong("n_clifin_id", objCliFinanciero.getClifin_id());
            cst.setString("c_cli_id", objCliFinanciero.getCli_id());
            cst.setLong("n_cli_key", objCliFinanciero.getCli_key());
            cst.setInt("n_suc_id", objCliFinanciero.getSuc_id());
            cst.setInt("n_emp_id", objCliFinanciero.getEmp_id());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objCliFinanciero.getClifin_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarGarantia(CliGarantia objCliGarantia) throws SQLException, ParseException {
        String SQL_INSERTAR_GARANTIA = "{call PACK_TCLIENTES.p_005_insertarGarantia(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_GARANTIA);
            cst.clearParameters();
            cst.setString("c_cli_id", objCliGarantia.getCli_id());
            cst.setLong("n_cli_key", objCliGarantia.getCli_key());
            cst.setInt("n_suc_id", objCliGarantia.getSuc_id());
            cst.setInt("n_emp_id", objCliGarantia.getEmp_id());
            cst.setString("c_cligar_garantia", objCliGarantia.getCligar_garantia());
            cst.setLong("n_cligar_monto", objCliGarantia.getCligar_monto());
            cst.setString("c_cligar_obs", objCliGarantia.getCligar_obs());
            cst.setString("c_cligar_usuadd", objCliGarantia.getCligar_usuadd());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliGarantia.getCligar_obs());
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

    public ParametrosSalida actualizarGarantia(CliGarantia objCliGarantia) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_GARANTIA = "{call PACK_TCLIENTES.p_005_actualizarGarantia(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_GARANTIA);
            cst.clearParameters();
            cst.setLong("n_cligar_id", objCliGarantia.getCligar_id());
            cst.setString("c_cli_id", objCliGarantia.getCli_id());
            cst.setLong("n_cli_key", objCliGarantia.getCli_key());
            cst.setInt("n_suc_id", objCliGarantia.getSuc_id());
            cst.setInt("n_emp_id", objCliGarantia.getEmp_id());
            cst.setString("c_cligar_garantia", objCliGarantia.getCligar_garantia());
            cst.setLong("n_cligar_monto", objCliGarantia.getCligar_monto());
            cst.setString("c_cligar_obs", objCliGarantia.getCligar_obs());
            cst.setInt("n_cligar_est", objCliGarantia.getCligar_est());
            cst.setString("c_cligar_usumod", objCliGarantia.getCligar_usumod());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliGarantia.getCligar_obs());
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

    public ParametrosSalida eliminarGarantia(CliGarantia objCliGarantia) throws SQLException {
        String SQL_ELIMINAR_GARANTIA = "{call PACK_TCLIENTES.p_005_eliminarGarantia(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_GARANTIA);
            cst.clearParameters();
            cst.setLong("n_cligar_id", objCliGarantia.getCligar_id());
            cst.setString("c_cli_id", objCliGarantia.getCli_id());
            cst.setLong("n_cli_key", objCliGarantia.getCli_key());
            cst.setInt("n_suc_id", objUsuCredential.getCodsuc());
            cst.setInt("n_emp_id", objUsuCredential.getCodemp());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objCliGarantia.getCligar_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida ExistenciaDocumento(String codigoCli, String nrodoc, int tipoDoc) throws SQLException {
        String SQL_VALIDADOCUMENTO = "{call PACK_TCLIENTES.p_verificarDocumento(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDADOCUMENTO);
            cst.clearParameters();
            cst.setString("c_cli_id", codigoCli);
            cst.setInt("n_tipdoc", tipoDoc);
            cst.setString("c_nrodoc", nrodoc);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | consulto el Numero de Documento " + nrodoc);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la Consulta debido al Error" + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la Consulta debido al Error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<TipoDocumento> listaTipoDocumento() throws SQLException {
        String SQL_LISTA_TIPODOCUMENTO = //"select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=27 and t.tab_est=1 order by t.tab_ord";
                " select * from (\n"
                + " select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=27 and t.tab_est=1 \n"
                + " union \n"
                + " select null,'' from ttabgen t WHERE t.tab_cod=27 and t.tab_est=1)\n"
                + " order by 1 desc";
        objlstTipoDocumento = new ListModelList<TipoDocumento>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_TIPODOCUMENTO);
            while (rs.next()) {
                objTipoDocumento = new TipoDocumento();
                objTipoDocumento.setTab_id(rs.getInt(1));
                objTipoDocumento.setTab_subdes(rs.getString(2));
                objlstTipoDocumento.add(objTipoDocumento);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoDocumento.getSize() + " registro(s)");
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
        return objlstTipoDocumento;
    }

    public ListModelList<Categoria> listaCategoria() throws SQLException {
        String SQL_LISTA_CATEGORIA = "select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=26 and t.tab_est=1 order by t.tab_ord";
        objlstCategoria = new ListModelList<Categoria>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CATEGORIA);
            while (rs.next()) {
                objCategoria = new Categoria();
                objCategoria.setTab_id(rs.getInt(1));
                objCategoria.setTab_subdes(rs.getString(2));
                objlstCategoria.add(objCategoria);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoDocumento.getSize() + " registro(s)");
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
        return objlstCategoria;
    }

    public ListModelList<Moneda> listaMoneda() throws SQLException {
        String SQL_LISTA_MONEDA = "select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=24 and t.tab_est=1 order by t.tab_ord";
        objlstMoneda = new ListModelList<Moneda>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_MONEDA);
            while (rs.next()) {
                objMoneda = new Moneda();
                objMoneda.setTab_id(rs.getInt(1));
                objMoneda.setTab_subdes(rs.getString(2));
                objlstMoneda.add(objMoneda);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMoneda.getSize() + " registro(s)");
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
        return objlstMoneda;
    }

    public ListModelList<ListaPrecio> listaPrecio() throws SQLException {
        //String SQL_LISTA_PRECIO = "select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=19 and t.tab_est=1 order by t.tab_ord";
        String SQL_LISTA_PRECIO = "select t.lp_key,t.lp_des from tlistaprecios t WHERE  t.emp_id=" + objUsuCredential.getCodemp() + " and"
                + " t.suc_id=" + objUsuCredential.getCodsuc() + " and t.lp_est=1 and t.lp_tipo=2 order by t.lp_ord";

        objlstLpventas = new ListModelList<ListaPrecio>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PRECIO);
            while (rs.next()) {
                objLpventas = new ListaPrecio();
                objLpventas.setLp_key(rs.getInt(1));
                objLpventas.setLp_des(rs.getString(2));
                objlstLpventas.add(objLpventas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstLpventas.getSize() + " registro(s)");
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
        return objlstLpventas;
    }

    public ListModelList<Canal> listaCanal() throws SQLException {
        String SQL_LISTA_PRECIO = "select t.tab_id,t.tab_subdes from ttabgen t WHERE t.tab_cod=28 and t.tab_est=1 order by t.tab_ord";
        objlstCanal = new ListModelList<Canal>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PRECIO);
            while (rs.next()) {
                objCanal = new Canal();
                objCanal.setTab_id(rs.getInt(1));
                objCanal.setTab_subdes(rs.getString(2));
                objlstCanal.add(objCanal);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCanal.getSize() + " registro(s)");
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
        return objlstCanal;
    }

    public ListModelList<FormaPago> listaFPago() throws SQLException {
        String SQL_LISTA_FPAGO = "select t.forpag_key,t.forpag_id,t.forpag_des from tforpag t where t.forpag_est=1 order by t.forpag_ord";
        objlstFormaPago = new ListModelList<FormaPago>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_FPAGO);
            while (rs.next()) {
                objFormaPago = new FormaPago();
                objFormaPago.setForpag_key(rs.getInt(1));
                objFormaPago.setForpag_id(rs.getString(2));
                objFormaPago.setForpag_des(rs.getString(3));
                objlstFormaPago.add(objFormaPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstFormaPago.getSize() + " registro(s)");
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
        return objlstFormaPago;
    }

    public ListModelList<Condicion> listaCondicion() throws SQLException {
        String SQL_LISTA_CONDICION = "select t.con_key,t.con_des from tcondicion t where t.con_est=1 and t.con_tipo='V' order by t.con_ord";
        objlstCondicion = new ListModelList<Condicion>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CONDICION);
            while (rs.next()) {
                objCondicion = new Condicion();
                objCondicion.setConKey(rs.getInt(1));
                objCondicion.setConDes(rs.getString(2));
                objlstCondicion.add(objCondicion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCondicion.getSize() + " registro(s)");
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
        return objlstCondicion;
    }

    public ListModelList<Cliente> listaCliente(int emp_id, int suc_id, int i_caso) throws SQLException {
        String SQL_CLIENTE;
        if (i_caso == 0) {
            SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                    + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                    + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                    + "from ( "
                    + "select * from v_listacliente t "
                    + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + ") x,tclientes y,tcondicion c"
                    + "where x.cli_id(+)=y.cli_id and "
                    + "x.cli_key(+)=y.cli_key and "
                    + "y.cli_est<>" + i_caso + " and "
                    //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                    //+ "d.cli_id=y.cli_id and "
                    + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                    + "order by y.cli_key";
        } else {
            SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                    + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                    + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                    + "from ( "
                    + "select * from v_listacliente t "
                    + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + ") x,tclientes y,tcondicion c  "
                    + "where x.cli_id(+)=y.cli_id and "
                    + "x.cli_key(+)=y.cli_key and "
                    + "y.cli_est=" + i_caso + " and "
                    //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                    //+ "d.cli_id=y.cli_id and "
                    + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                    + "order by y.cli_key";

        }
        P_WHERE = SQL_CLIENTE;
        objlstCliente = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);
            objlstCliente = new ListModelList<Cliente>();
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setZon_dvis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                //objCliente.setCli_deslista(rs.getString("lisdes") == null ? "" : rs.getString("lisdes"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objlstCliente.add(objCliente);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliente.getSize() + " registro(s)");
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
        return objlstCliente;
    }

    public ListModelList<CliDireccion> listaDireccion(String cli_key, int emp_id, int suc_id) throws SQLException {
        String SQL_DIRECCION;
        SQL_DIRECCION = "select y.*,x.clizon_default,x.clizon_est,x.zon_key,x.zon_id,x.zon_des,x.giro_id,"
                + "x.giro_des,x.pos_id,x.pos_des,x.hor_id,x.hor_des,x.ubi_id,x.ubi_des,x.can_id,"
                + "x.can_des,x.ven_id,x.ven_apenom,x.trans_id,x.trans_alias,x.cli_lista,x.cli_factura,x.cli_perc "
                + "from (select * from v_listadireccioncliente t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + ")x,tclidir y "
                + "where x.clidir_id(+)=y.clidir_id and "
                + "x.cli_id(+)=y.cli_id and "
                + "x.cli_key(+)=y.cli_key and "
                + "y.cli_key=" + cli_key
                + " and (x.clizon_est=1 or x.clizon_est is null) "
                + " order by x.cli_key,x.clidir_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_DIRECCION);
            objlstCliDireccion = new ListModelList<CliDireccion>();
            while (rs.next()) {
                objCliDireccion = new CliDireccion();
                objCliDireccion.setCli_id(rs.getString("cli_id"));
                objCliDireccion.setCli_key(rs.getLong("cli_key"));
                objCliDireccion.setClidir_id(rs.getLong("clidir_id"));
                objCliDireccion.setClidir_direc(rs.getString("clidir_direc"));
                objCliDireccion.setClidir_idvia(rs.getInt("clidir_idvia"));
                objCliDireccion.setClidir_nomvia(rs.getString("clidir_nomvia"));
                objCliDireccion.setClidir_numvia(rs.getInt("clidir_numvia"));
                objCliDireccion.setClidir_idint(rs.getInt("clidir_idint"));
                objCliDireccion.setClidir_nroint(rs.getLong("clidir_nroint"));
                objCliDireccion.setClidir_mza(rs.getString("clidir_mza"));
                objCliDireccion.setClidir_lote(rs.getString("clidir_lote"));
                objCliDireccion.setClidir_idseg(rs.getInt("clidir_idseg"));
                objCliDireccion.setClidir_nomseg(rs.getString("clidir_nomseg"));
                objCliDireccion.setClidir_ref(rs.getString("clidir_ref"));
                objCliDireccion.setClidir_usuadd(rs.getString("clidir_usuadd"));
                objCliDireccion.setClidir_fecadd(rs.getTimestamp("clidir_fecadd"));
                objCliDireccion.setClidir_usumod(rs.getString("clidir_usumod"));
                objCliDireccion.setClidir_fecmod(rs.getTimestamp("clidir_fecmod"));
                objCliDireccion.setClizon_default(rs.getInt("clizon_default"));
                objCliDireccion.setClizon_est(rs.getInt("clizon_est"));
                objCliDireccion.setZon_key(rs.getInt("zon_key"));
                objCliDireccion.setZon_id(rs.getString("zon_id"));
                objCliDireccion.setZon_des(rs.getString("zon_des"));
                objCliDireccion.setGiro_id(rs.getString("giro_id"));
                objCliDireccion.setGiro_des(rs.getString("giro_des"));
                objCliDireccion.setPos_id(rs.getString("pos_id"));
                objCliDireccion.setPos_des(rs.getString("pos_des"));
                objCliDireccion.setHor_id(rs.getString("hor_id"));
                objCliDireccion.setHor_des(rs.getString("hor_des"));
                objCliDireccion.setUbi_id(rs.getString("ubi_id"));
                objCliDireccion.setUbi_des(rs.getString("ubi_des"));
                objCliDireccion.setCan_id(rs.getString("can_id"));
                objCliDireccion.setCan_des(rs.getString("can_des"));
                objCliDireccion.setVen_id(rs.getString("ven_id"));
                objCliDireccion.setVen_apenom(rs.getString("ven_apenom"));
                objCliDireccion.setTrans_id(rs.getString("trans_id"));
                objCliDireccion.setTrans_des(rs.getString("trans_alias"));

                objCliDireccion.setCli_lista(rs.getInt("cli_lista"));
                objCliDireccion.setCli_factura(rs.getInt("cli_factura"));
                objCliDireccion.setCli_perc(rs.getInt("cli_perc"));
                objlstCliDireccion.add(objCliDireccion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliDireccion.getSize() + " registro(s)");
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
        return objlstCliDireccion;
    }

    public ListModelList<CliTelefono> listaTelefono(String CLI_ID) throws SQLException {
        String SQL_TELEFONO;
        SQL_TELEFONO = "select * from v_listatelefonocliente t where t.clitel_est <> 0 and t.cli_id = '" + CLI_ID + "' order by t.clitel_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TELEFONO);
            objlstCliTelefono = new ListModelList<CliTelefono>();
            while (rs.next()) {
                objCliTelefono = new CliTelefono();
                objCliTelefono.setClitel_id(rs.getLong(1));
                objCliTelefono.setCli_id(rs.getString(2));
                objCliTelefono.setCli_key(rs.getLong(3));
                objCliTelefono.setClitel_telef1(rs.getLong(4));
                objCliTelefono.setClitel_telef2(rs.getLong(5));
                objCliTelefono.setClitel_movil(rs.getLong(6));
                objCliTelefono.setClitel_est(rs.getInt(7));
                objCliTelefono.setClitel_usuadd(rs.getString(8));
                objCliTelefono.setClitel_fecadd(rs.getTimestamp(9));
                objCliTelefono.setClitel_usumod(rs.getString(10));
                objCliTelefono.setClitel_fecmod(rs.getTimestamp(11));
                objlstCliTelefono.add(objCliTelefono);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliTelefono.getSize() + " registro(s)");
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
        return objlstCliTelefono;
    }

    public ListModelList<CliFinanciero> listaFinanciero(String CLI_ID, int EMP_ID, int SUC_ID) throws SQLException {
        String SQL_FINANCIERO;
        SQL_FINANCIERO = "select * from v_listafinancierocliente t where t.clifin_est <> 0 and t.cli_id = '" + CLI_ID + "' and"
                + " t.emp_id = '" + EMP_ID + "' and t.suc_id = '" + SUC_ID + "' order by t.clifin_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FINANCIERO);
            objlstCliFinanciero = new ListModelList<CliFinanciero>();
            while (rs.next()) {
                objCliFinanciero = new CliFinanciero();
                objCliFinanciero.setClifin_id(rs.getLong(1));
                objCliFinanciero.setClifin_limcred(rs.getLong(2));
                objCliFinanciero.setClifin_limdoc(rs.getInt(3));
                objCliFinanciero.setClifin_est(rs.getInt(4));
                objCliFinanciero.setClifin_usuadd(rs.getString(5));
                objCliFinanciero.setClifin_fecadd(rs.getTimestamp(6));
                objCliFinanciero.setClifin_usumod(rs.getString(7));
                objCliFinanciero.setClifin_fecmod(rs.getTimestamp(8));
                objCliFinanciero.setCli_id(rs.getString(9));
                objCliFinanciero.setCli_key(rs.getLong(10));
                objCliFinanciero.setSuc_id(rs.getInt(11));
                objCliFinanciero.setEmp_id(rs.getInt(12));
                objCliFinanciero.setClifin_categ(rs.getInt(13));
                objlstCliFinanciero.add(objCliFinanciero);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliFinanciero.getSize() + " registro(s)");
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
        return objlstCliFinanciero;
    }

    public ListModelList<CliGarantia> listaGarantia(String CLI_ID, int EMP_ID, int SUC_ID) throws SQLException {
        String SQL_GARANTIA;
        SQL_GARANTIA = "select * from v_listagarantiacliente t where t.cligar_est <> 0 and t.cli_id = '" + CLI_ID + "' and t.emp_id = '" + EMP_ID + "' "
                + "and t.suc_id = '" + SUC_ID + "' order by t.cligar_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GARANTIA);
            objlstCliGarantia = new ListModelList<CliGarantia>();
            while (rs.next()) {
                objCliGarantia = new CliGarantia();
                objCliGarantia.setCligar_id(rs.getLong(1));
                objCliGarantia.setCli_id(rs.getString(2));
                objCliGarantia.setCli_key(rs.getLong(3));
                objCliGarantia.setSuc_id(rs.getInt(4));
                objCliGarantia.setEmp_id(rs.getInt(5));
                objCliGarantia.setCligar_garantia(rs.getString(6));
                objCliGarantia.setCligar_monto(rs.getLong(7));
                objCliGarantia.setCligar_obs(rs.getString(8));
                objCliGarantia.setCligar_est(rs.getInt(9));
                objCliGarantia.setCligar_usuadd(rs.getString(10));
                objCliGarantia.setCligar_fecadd(rs.getTimestamp(11));
                objCliGarantia.setCligar_usumod(rs.getString(12));
                objCliGarantia.setCligar_fecmod(rs.getTimestamp(13));
                objlstCliGarantia.add(objCliGarantia);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliGarantia.getSize() + " registro(s)");
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
        return objlstCliGarantia;
    }

    public ListModelList<Cliente> busquedaClientes(int emp_id, int suc_id, int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_CLIENTE;
        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos            
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 1) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and x.cli_id like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 2) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and y.cli_razsoc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 3) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and x.clidir_direc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 4) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and x.zon_id like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 5) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and y.cli_ruc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 6) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and y.cli_dni like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est<>0 and x.hor_des like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 1) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and y.cli_id like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 2) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and y.cli_razsoc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 3) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and x.clidir_direc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 4) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and x.zon_id like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 5) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and y.cli_ruc like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else if (seleccion == 6) {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and y.cli_dni like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            } else {
                SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                        + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                        + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                        + "from ( "
                        + "select * from v_listacliente t "
                        + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                        + ") x,tclientes y,tcondicion c "
                        + "where x.cli_id(+)=y.cli_id and "
                        + "x.cli_key(+)=y.cli_key and "
                        + "y.cli_est=" + i_estado + " and x.hor_des like '" + consulta + "' and "
                        //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                        + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                        + "order by y.cli_key";
            }
        }
        P_WHERE = SQL_CLIENTE;
        objlstCliente = new ListModelList<Cliente>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                //objCliente.setCli_factura(rs.getInt("cli_factura"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                //objCliente.setCli_lista(rs.getInt("cli_lista"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
                //objCliente.setClidir_direc(rs.getString("clidir_direc"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setZon_dvis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                //objCliente.setCli_deslista(rs.getString("lisdes"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objlstCliente.add(objCliente);
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

        return objlstCliente;
    }

    public Cliente getNomCliente(String cli_id) throws SQLException {
        String SQL_BUSQUEDA = "select p.cli_id,p.cli_razsoc from tclientes p where p.cli_id=" + cli_id;
        objCliente = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString(1));
                objCliente.setCli_razsoc(rs.getString(2));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCliente;
    }

    public Cliente getDireccionDefault(String cli_id, int emp_id, int suc_id) throws SQLException {
        String SQL_CLIDIRDEF = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des,"
                + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                + "from ( "
                + "select * from v_listacliente t "
                + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                + ") x,tclientes y,tcondicion c "
                + "where x.cli_id(+)=y.cli_id and "
                + "x.cli_key(+)=y.cli_key and "
                + "y.cli_est=1 and y.cli_key=" + cli_id
                + " and c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                + "order by y.cli_key";

        objCliente = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIDIRDEF);
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? null : rs.getLong("clidir_id"));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setZon_dvis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                //objCliente.setCli_deslista(rs.getString("lisdes") == null ? "" : rs.getString("lisdes"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
                objlstCliente.add(objCliente);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliDireccion.getSize() + " registro(s)");
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
        return objCliente;
    }

    public CliFinanciero getClienteFin(String cli_id, int emp_id, int suc_id) throws SQLException {
        String SQL_BUSQUEDA = "select * from tclifin p where p.clifin_est=1 and p.cli_id=" + cli_id + " and p.emp_id=" + emp_id + " and p.suc_id=" + suc_id;
        objCliFinanciero = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objCliFinanciero = new CliFinanciero();
                objCliFinanciero.setCli_id(rs.getString("cli_id"));
                objCliFinanciero.setClifin_limcred(rs.getLong("clifin_limcred"));
                objCliFinanciero.setClifin_limdoc(rs.getInt("clifin_limdoc"));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCliFinanciero;
    }

    public Cliente ExistenciaCliente(String cli_id) throws SQLException {
        String SQL_CLIENTE = "select y.*,x.clidir_id,x.clidir_direc, x.zon_key, x.zon_id, x.zon_des, x.hor_id, x.hor_des, "
                + "x.ven_id, x.ven_apenom,x.sup_id,x.sup_apenom, x.trans_id, x.trans_alias,x.giro,x.zon_dvis,x.zon_dvis_des, "
                + "x.cli_lista,x.cli_factura,x.cli_perc,c.con_des condicion,c.con_dpla diaplazo,y.cli_rel "
                + "from ( "
                + "select * from v_listacliente t "
                + "where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                + ") x,tclientes y,tcondicion c "
                + "where x.cli_id(+)=y.cli_id and y.cli_id=" + cli_id + " and "
                + "x.cli_key(+)=y.cli_key and "
                + "y.cli_est=1 and "
                //+ "p.lp_id=y.cli_lista and p.lp_est=1 and x.emp_id=p.emp_id and x.suc_id=p.suc_id and p.lp_tipo=2 and "
                //+ "d.cli_id=y.cli_id and "
                + "c.con_id=y.cli_con and c.con_est=1 and c.con_tipo='V' "
                + "order by y.cli_key";
        objCliente = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CLIENTE);
            objlstCliente = new ListModelList<Cliente>();
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cli_id"));
                objCliente.setCli_key(rs.getLong("cli_key"));
                objCliente.setCli_apepat(rs.getString("cli_apepat"));
                objCliente.setCli_apemat(rs.getString("cli_apemat"));
                objCliente.setCli_nombre(rs.getString("cli_nombre"));
                objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
                objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
                objCliente.setCli_ruc(rs.getLong("cli_ruc"));
                objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
                objCliente.setCli_email1(rs.getString("cli_email1"));
                objCliente.setCli_email2(rs.getString("cli_email2"));
                objCliente.setCli_est(rs.getInt("cli_est"));
                objCliente.setCli_dni(rs.getString("cli_dni"));
                objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
                objCliente.setCli_perju(rs.getInt("cli_perju"));
                objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
                objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
                objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
                objCliente.setCli_mon(rs.getInt("cli_mon"));
                objCliente.setCli_con(rs.getInt("cli_con"));
                objCliente.setCli_canal(rs.getInt("cli_canal"));
                objCliente.setForpag_key(rs.getInt("forpag_key"));
                objCliente.setForpag_id(rs.getString("forpag_id"));
                objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
                objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
                objCliente.setCli_usumod(rs.getString("cli_usumod"));
                objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
                objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? null : rs.getLong("clidir_id"));
                //objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
                objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc"));
                objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
                objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
                objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
                objCliente.setHor_id(rs.getString("hor_id") == null ? "" : rs.getString("hor_id"));
                objCliente.setHor_des(rs.getString("hor_des") == null ? "" : rs.getString("hor_des"));
                objCliente.setVen_id(rs.getString("ven_id") == null ? "" : rs.getString("ven_id"));
                objCliente.setVen_apenom(rs.getString("ven_apenom") == null ? "" : rs.getString("ven_apenom"));
                objCliente.setSup_id(rs.getString("sup_id") == null ? "" : rs.getString("sup_id"));
                objCliente.setSup_apenom(rs.getString("sup_apenom") == null ? "" : rs.getString("sup_apenom"));
                objCliente.setTrans_id(rs.getString("trans_id") == null ? "" : rs.getString("trans_id"));
                objCliente.setTrans_alias(rs.getString("trans_alias") == null ? "" : rs.getString("trans_alias"));
                objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
                objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
                objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));
                objCliente.setDia_vis(rs.getInt("zon_dvis") == 0 ? 0 : rs.getInt("zon_dvis"));
                objCliente.setZon_dvis_des(rs.getString("zon_dvis_des") == null ? "" : rs.getString("zon_dvis_des"));
                //objCliente.setCli_deslista(rs.getString("lisdes"));
                objCliente.setCli_giro(rs.getString("giro"));
                objCliente.setCli_descond(rs.getString("condicion"));
                objCliente.setDiasplazo(rs.getInt("diaplazo"));
                objCliente.setCli_emprel(rs.getInt("cli_rel"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCliente.getSize() + " registro(s)");
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
        return objCliente;
    }

    public Object[] ValidaLimiteCredito(String idcli, Long keycli, String emp_suc) throws SQLException {
        String SQL_VALIDA_LIMCRED = "select "
                + "(select nvl(sum(x.clifin_limcred),0) from tclifin x where x.clifin_est = 1 and x.cli_id='" + idcli + "' and lpad(x.emp_id,3,'0')||lpad(x.suc_id,3,'0')<> '" + emp_suc + "' ) cred  , "
                + "(select nvl(sum(x.clifin_limdoc),0) from tclifin x where x.clifin_est = 1 and x.cli_id='" + idcli + "' and lpad(x.emp_id,3,'0')||lpad(x.suc_id,3,'0')<> '" + emp_suc + "' ) doc , "
                + "t.cli_credcor credcor, t.cli_doccor doccor "
                + "from tclientes t "
                + "where t.cli_key = '" + keycli + "' and t.cli_id ='" + idcli + "' ";
        Object[] Datos = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VALIDA_LIMCRED);
            while (rs.next()) {
                Datos = new Object[4];
                Datos[0] = rs.getLong(1);//credito utilizado entre todas las empresas
                Datos[1] = rs.getInt(2);//documento utilizado entre todas las empresas
                Datos[2] = rs.getLong(3);//credito coorporativo
                Datos[3] = rs.getInt(4);//documento coorporativo
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
        return Datos;
    }

    //*************jart
    /*public ListModelList<Cliente> busquedaClientesM(int emp_id, int suc_id, int seleccion, String consulta, int i_estado) throws SQLException {
     String SQL_CLIENTE;
     if (i_estado == 3) { // todos activos e inactivos
     if (seleccion == 0) {//todos            
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0"
     + " order by c.cli_key";
     } else if (seleccion == 1) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.cli_id like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 2) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.cli_razsoc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 3) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and d.clidir_direc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 4) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.zon_id like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 5) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.cli_ruc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 6) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.cli_dni like '" + consulta + "'"
     + " order by c.cli_key";
     } else {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est<>0 and c.cli_dni like '" + consulta + "'"
     + " order by c.cli_key";
     }

     } else {
     if (seleccion == 0) {//todos
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado
     + " order by c.cli_key";
     } else if (seleccion == 1) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and c.cli_id like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 2) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and c.cli_razsoc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 3) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and d.clidir_direc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 4) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and z.zon_id like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 5) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and c.cli_ruc like '" + consulta + "'"
     + " order by c.cli_key";
     } else if (seleccion == 6) {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and c.cli_dni like '" + consulta + "'"
     + " order by c.cli_key";
     } else {
     SQL_CLIENTE = "select c.*,d.clidir_id,d.clidir_direc,z.cli_lista,z.cli_factura,z.cli_perc,n.zon_key,n.zon_id,n.zon_des"
     + " from"
     + " tclientes c,tclidir d,tclizon z,tclitel t,tzonas n"
     + " where"
     + " c.cli_key=d.cli_key and"
     + " c.cli_key=z.cli_key and"
     + " d.clidir_id=z.clidir_id and"
     + " d.cli_key=z.cli_key and"
     + " c.cli_key=t.cli_key(+) and"
     + " z.prog_zonid=n.zon_id and"
     + " z.emp_id=1 and z.suc_id=1 and"
     + " n.emp_id=1 and n.suc_id=1 and"
     + " z.clizon_est=1 and"
     + " z.clizon_default=1 and"
     + " c.cli_est=" + i_estado + " and c.cli_dni like '" + consulta + "'"
     + " order by c.cli_key";
     }
     }
     P_WHERE = SQL_CLIENTE;
     objlstCliente = new ListModelList<Cliente>();
     try {
     con = new ConectaBD().conectar();
     st = con.createStatement();
     rs = st.executeQuery(SQL_CLIENTE);
     while (rs.next()) {
     objCliente = new Cliente();
     objCliente.setCli_id(rs.getString("cli_id"));
     objCliente.setCli_key(rs.getLong("cli_key"));
     objCliente.setCli_apepat(rs.getString("cli_apepat"));
     objCliente.setCli_apemat(rs.getString("cli_apemat"));
     objCliente.setCli_nombre(rs.getString("cli_nombre"));
     objCliente.setCli_razsoc(rs.getString("cli_razsoc"));
     objCliente.setCli_fecnac(rs.getDate("cli_fecnac"));
     objCliente.setCli_ruc(rs.getLong("cli_ruc"));
     objCliente.setCli_dirweb(rs.getString("cli_dirweb"));
     objCliente.setCli_email1(rs.getString("cli_email1"));
     objCliente.setCli_email2(rs.getString("cli_email2"));
     objCliente.setCli_est(rs.getInt("cli_est"));
     objCliente.setCli_dni(rs.getString("cli_dni"));
     objCliente.setCli_tipodoc(rs.getInt("cli_tipodoc"));
     objCliente.setCli_perju(rs.getInt("cli_perju"));
     objCliente.setCli_limcredcorp(rs.getDouble("cli_credcor"));
     objCliente.setCli_limdoccorp(rs.getInt("cli_doccor"));
     objCliente.setCli_dscto(rs.getDouble("cli_dscto"));
     //objCliente.setCli_factura(rs.getInt("cli_factura"));
     objCliente.setCli_mon(rs.getInt("cli_mon"));
     //objCliente.setCli_lista(rs.getInt("cli_lista"));
     objCliente.setCli_con(rs.getInt("cli_con"));
     objCliente.setCli_canal(rs.getInt("cli_canal"));
     objCliente.setForpag_key(rs.getInt("forpag_key"));
     objCliente.setForpag_id(rs.getString("forpag_id"));
     objCliente.setCli_usuadd(rs.getString("cli_usuadd"));
     objCliente.setCli_fecadd(rs.getTimestamp("cli_fecadd"));
     objCliente.setCli_usumod(rs.getString("cli_usumod"));
     objCliente.setCli_fecmod(rs.getTimestamp("cli_fecmod"));
     objCliente.setCli_emprel(rs.getInt("cli_rel"));

     objCliente.setClidir_id(rs.getLong("clidir_id") == 0 ? 0 : rs.getLong("clidir_id"));
     //objCliente.setClidir_direc(rs.getString("clidir_direc"));
     objCliente.setClidir_direc(rs.getString("clidir_direc") == null ? "" : rs.getString("clidir_direc").length() < 60 ? rs.getString("clidir_direc") : rs.getString("clidir_direc").substring(0, 60));
     objCliente.setZon_key(rs.getInt("zon_key") == 0 ? 0 : rs.getInt("zon_key"));
     objCliente.setZon_id(rs.getString("zon_id") == null ? "" : rs.getString("zon_id"));
     objCliente.setZon_des(rs.getString("zon_des") == null ? "" : rs.getString("zon_des"));
     objCliente.setCli_lista(rs.getInt("cli_lista") == 0 ? 0 : rs.getInt("cli_lista"));
     objCliente.setCli_factura(rs.getInt("cli_factura") == 0 ? 0 : rs.getInt("cli_factura"));
     objCliente.setCli_perc(rs.getInt("cli_perc") == 0 ? 0 : rs.getInt("cli_perc"));

     objlstCliente.add(objCliente);
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

     return objlstCliente;
     }*/
    public CliTelefono getClienteTel(Long cli_key) throws SQLException {
        String SQL_BUSQUEDA = "select * from tclitel p where p.clitel_est=1 and p.cli_key=" + cli_key;
        objCliTelefono = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objCliTelefono = new CliTelefono();
                objCliTelefono.setCli_key(rs.getInt("cli_key"));
                objCliTelefono.setCli_id(rs.getString("cli_id"));
                objCliTelefono.setClitel_id(rs.getInt("clitel_id"));
                objCliTelefono.setClitel_telef1(rs.getLong("clitel_telef1"));
                objCliTelefono.setClitel_telef2(rs.getLong("clitel_telef2"));
                objCliTelefono.setClitel_movil(rs.getLong("clitel_movil"));
                objCliTelefono.setClitel_est(rs.getInt("clitel_est"));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCliTelefono;
    }

    public CliFinanciero getClienteFinan(Long cli_key, int emp_id, int suc_id) throws SQLException {
        String SQL_BUSQUEDA = " select * from tclifin p,ttabgen t"
                + " where p.clifin_categ=t.tab_id and"
                + " p.clifin_est=1 and p.cli_key=" + cli_key
                + " and p.emp_id=" + emp_id + " "
                + "and p.suc_id= " + suc_id + " and t.tab_cod=26";
        //"select * from tclifin p where p.clifin_est=1 and p.cli_key=" + cli_key + " and p.emp_id=" + emp_id + " and p.suc_id=" + suc_id;
        objCliFinanciero = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objCliFinanciero = new CliFinanciero();
                objCliFinanciero.setCli_key(rs.getLong("cli_key"));
                objCliFinanciero.setCli_id(rs.getString("cli_id"));
                objCliFinanciero.setClifin_id(rs.getInt("clifin_id"));
                objCliFinanciero.setClifin_limcred(rs.getLong("clifin_limcred"));
                objCliFinanciero.setClifin_limdoc(rs.getInt("clifin_limdoc"));
                objCliFinanciero.setClifin_est(rs.getInt("clifin_est"));
                //objCliFinanciero.setClifin_categ(rs.getInt("clifin_categ"));
                objCliFinanciero.setCat(rs.getString("tab_subdes"));
                objCliFinanciero.setClifin_saldo(rs.getInt("clifin_saldo"));
                objCliFinanciero.setClifin_docemi(rs.getInt("clifin_docemi"));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCliFinanciero;
    }

    public Cliente getCuentaContable(String fecha) throws SQLException {

        String sql_query = " select max(f.cli_id) cliente from tclientes f where to_char(f.cli_fecadd,'dd/mm/yyyy')='" + fecha + "'";

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            objCliente = null;
            while (rs.next()) {
                objCliente = new Cliente();
                objCliente.setCli_id(rs.getString("cliente"));
                // objCliente.setCuenta_des(rs.getString("pc_descri"));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objCliente;

    }
}
