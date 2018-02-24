package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.OrdCompLin;
import org.me.gj.model.logistica.informes.OrdCompProd;
import org.me.gj.model.logistica.informes.OrdCompProv;
import org.me.gj.model.logistica.informes.OrdCompSublin;
import org.me.gj.model.logistica.procesos.OrdCompCab;
import org.me.gj.model.logistica.procesos.OrdCompDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoOrdCom {

    //Instancias de Objetos    
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<OrdCompDet> objlstOrdCompDet;
    OrdCompCab objOrdCompCab;
    OrdCompDet objOrdCompDet;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    String s_msg = "";
    String fechaActual;
    int i_flagErrorBD = 0;
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    public static String P_WHERE;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoOrdCom.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida aproRecOrdenCompra(OrdCompCab objOrdCompCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_APROREC_ORDCOMP = "{call pack_tordcomp.p_aproRecOrdenCompra(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_APROREC_ORDCOMP);
            cst.clearParameters();
            cst.setLong(1, objOrdCompCab.getOc_nropedkey());
            cst.setInt(2, objOrdCompCab.getEmp_id());
            cst.setInt(3, objOrdCompCab.getSuc_id());
            cst.setString(4, objOrdCompCab.getOc_ind());
            cst.setInt(5, objOrdCompCab.getOc_situacion());
            cst.setString(6, objOrdCompCab.getOc_usuapro());
            cst.setString(7, objOrdCompCab.getOc_pcapro());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo una Orden de Compra del Tipo " + objOrdCompCab.getOc_ind());
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

    public ParametrosSalida insertarOrdCompCab(OrdCompCab objOrdCompCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        String nropedid;
        cst = null;
        String SQL_INSERTAR_ORDCOMPCAB = "{call pack_tordcomp.p_insertarOrdCompCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_ORDCOMPCAB);
            cst.clearParameters();
            cst.setInt(1, objOrdCompCab.getEmp_id());
            cst.setInt(2, objOrdCompCab.getSuc_id());
            cst.setString(3, objOrdCompCab.getOc_ind());
            cst.setLong(4, objOrdCompCab.getOc_provid());
            cst.setInt(5, objOrdCompCab.getOc_moneda());
            cst.setDouble(6, objOrdCompCab.getOc_tcambio());
            cst.setDouble(7, objOrdCompCab.getOc_vafecto());
            cst.setDouble(8, objOrdCompCab.getOc_exonerado());
            cst.setDouble(9, objOrdCompCab.getOc_vimpt());
            cst.setDouble(10, objOrdCompCab.getOc_vtotal());
            cst.setDate(11, new java.sql.Date(objOrdCompCab.getOc_fecemi().getTime()));
            cst.setDate(12, new java.sql.Date(objOrdCompCab.getOc_fecrec().getTime()));
            cst.setDate(13, new java.sql.Date(objOrdCompCab.getOc_feccad().getTime()));
            cst.setString(14, objOrdCompCab.getOc_glosa());
            cst.setInt(15, objOrdCompCab.getOc_conid());
            cst.setInt(16, objOrdCompCab.getOc_lpcid());
            cst.setDouble(17, objOrdCompCab.getOc_vdesc());
            cst.setString(18, objOrdCompCab.getOc_usuadd());
            cst.setString(19, objOrdCompCab.getOc_pcadd());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            nropedid = cst.getString(22);
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo una Orden de Compra del Tipo " + objOrdCompCab.getOc_ind());
        } catch (SQLException e) {
            nropedid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            nropedid = "";
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, nropedid);
    }

    public ParametrosSalida modificarOrdCompCab(OrdCompCab objOrdCompCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_ORDCOMPCAB = "{call pack_tordcomp.p_actualizarOrdCompCab(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_ORDCOMPCAB);
            cst.clearParameters();
            cst.setInt(1, objOrdCompCab.getEmp_id());
            cst.setInt(2, objOrdCompCab.getSuc_id());
            cst.setString(3, objOrdCompCab.getOc_ind());
            cst.setLong(4, objOrdCompCab.getOc_nropedkey());
            cst.setInt(5, objOrdCompCab.getOc_moneda());
            cst.setDouble(6, objOrdCompCab.getOc_tcambio());
            cst.setDouble(7, objOrdCompCab.getOc_vafecto());
            cst.setDouble(8, objOrdCompCab.getOc_exonerado());
            cst.setDouble(9, objOrdCompCab.getOc_vimpt());
            cst.setDouble(10, objOrdCompCab.getOc_vtotal());
            cst.setDate(11, new java.sql.Date(objOrdCompCab.getOc_fecemi().getTime()));
            cst.setDate(12, new java.sql.Date(objOrdCompCab.getOc_fecrec().getTime()));
            cst.setDate(13, new java.sql.Date(objOrdCompCab.getOc_feccad().getTime()));
            cst.setString(14, objOrdCompCab.getOc_glosa());
            cst.setInt(15, objOrdCompCab.getOc_conid());
            cst.setInt(16, objOrdCompCab.getOc_lpcid());
            cst.setDouble(17, objOrdCompCab.getOc_vdesc());
            cst.setString(18, objOrdCompCab.getOc_usumod());
            cst.setString(19, objOrdCompCab.getOc_pcmod());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico una Orden de Compra con codigo " + objOrdCompCab.getOc_nropedid());
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

    public ParametrosSalida eliminarOrdCompCab(OrdCompCab objOrdCompCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_ORDCOMPCAB = "{call pack_tordcomp.p_eliminarOrdCompCab(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_ORDCOMPCAB);
            cst.clearParameters();
            cst.setInt(1, objOrdCompCab.getEmp_id());
            cst.setInt(2, objOrdCompCab.getSuc_id());
            cst.setString(3, objOrdCompCab.getOc_ind());
            cst.setLong(4, objOrdCompCab.getOc_nropedkey());
            cst.setString(5, objOrdCompCab.getOc_usumod());
            cst.setString(6, objOrdCompCab.getOc_pcmod());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Orden de Compra con Codigo " + objOrdCompCab.getOc_nropedid());
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

    public ParametrosSalida insertarOrdCompDet(OrdCompDet objOrdCompDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_ORDCOMPDET = "{call pack_tordcomp.p_insertarOrdCompDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_ORDCOMPDET);
            cst.clearParameters();
            cst.setInt(1, objOrdCompDet.getEmp_id());
            cst.setInt(2, objOrdCompDet.getSuc_id());
            cst.setString(3, objOrdCompDet.getOc_ind());
            cst.setLong(4, objOrdCompDet.getOc_nropedkey());
            cst.setString(5, objOrdCompDet.getPro_id());
            cst.setDouble(6, objOrdCompDet.getOcd_precio());
            cst.setDouble(7, objOrdCompDet.getOcd_cantped());
            cst.setDouble(8, objOrdCompDet.getOcd_vafecto());
            cst.setDouble(9, objOrdCompDet.getOcd_exonerado());
            cst.setDouble(10, objOrdCompDet.getOcd_vimpto());
            cst.setDouble(11, objOrdCompDet.getOcd_pimpto());
            cst.setDouble(12, objOrdCompDet.getOcd_vtotal());
            cst.setString(13, objOrdCompDet.getOcd_glosa());
            cst.setDouble(14, objOrdCompDet.getPro_pesototal());
            cst.setDouble(15, objOrdCompDet.getOcd_vdesc());
            cst.setDouble(16, objOrdCompDet.getOcd_pdesc());
            cst.setString(17, objOrdCompDet.getPro_ubi());
            cst.setString(18, objOrdCompDet.getOcd_usuadd());
            cst.setString(19, objOrdCompDet.getOcd_pcadd());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Inserto un producto a la Orden de Compra " + objOrdCompDet.getOc_nropedkey() + " con Codigo " + objOrdCompDet.getPro_id());
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

    public ParametrosSalida modificarOrdCompDet(OrdCompDet objOrdCompDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_ORDCOMPDET = "{call pack_tordcomp.p_actualizarOrdCompDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_ORDCOMPDET);
            cst.clearParameters();
            cst.setLong(1, objOrdCompDet.getOc_nropedkey());
            cst.setInt(2, objOrdCompDet.getEmp_id());
            cst.setInt(3, objOrdCompDet.getSuc_id());
            cst.setString(4, objOrdCompDet.getOc_ind());
            cst.setLong(5, objOrdCompDet.getOcd_item());
            cst.setString(6, objOrdCompDet.getPro_id());
            cst.setDouble(7, objOrdCompDet.getOcd_precio());
            cst.setDouble(8, objOrdCompDet.getOcd_cantped());
            cst.setDouble(9, objOrdCompDet.getOcd_vafecto());
            cst.setDouble(10, objOrdCompDet.getOcd_exonerado());
            cst.setDouble(11, objOrdCompDet.getOcd_vimpto());
            cst.setDouble(12, objOrdCompDet.getOcd_pimpto());
            cst.setDouble(13, objOrdCompDet.getOcd_vtotal());
            cst.setString(14, objOrdCompDet.getOcd_glosa());
            cst.setDouble(15, objOrdCompDet.getOcd_peso());
            cst.setDouble(16, objOrdCompDet.getOcd_vdesc());
            cst.setDouble(17, objOrdCompDet.getOcd_pdesc());
            cst.setString(18, objOrdCompDet.getPro_ubi());
            cst.setString(19, objOrdCompDet.getOcd_usumod());
            cst.setString(20, objOrdCompDet.getOcd_pcmod());
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(22);
            i_flagErrorBD = cst.getInt(21);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto de la Orden de Compra " + objOrdCompDet.getOc_nropedkey() + " con Codigo " + objOrdCompDet.getPro_id());
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

    public ParametrosSalida eliminarOrdCompDet(OrdCompDet objOrdCompDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_ORDCOMPCAB = "{call pack_tordcomp.p_eliminarOrdCompDet(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_ORDCOMPCAB);
            cst.clearParameters();
            cst.setLong(1, objOrdCompDet.getOc_nropedkey());
            cst.setInt(2, objOrdCompDet.getEmp_id());
            cst.setInt(3, objOrdCompDet.getSuc_id());
            cst.setString(4, objOrdCompDet.getOc_ind());
            cst.setLong(5, objOrdCompDet.getOcd_item());
            cst.setString(6, objOrdCompDet.getOcd_usumod());
            cst.setString(7, objOrdCompDet.getOcd_pcmod());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Orden de Compra con Codigo " + objOrdCompCab.getOc_nropedid());
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

    public ParametrosSalida actualizaSituacionOC() throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAOC = "{call pack_tordcomp.p_actualizaSituacionOC(?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAOC);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(2);
            i_flagErrorBD = cst.getInt(1);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se actualizaron las situaciones de las o/c ");
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la actualización de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la actualización porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones    
    public ListModelList<OrdCompCab> listaOrdCompCab(int emp_id, int suc_id, String oc_ind, String proveedor, String situacion, String fechai, String fechaf, String orden) throws SQLException {
        String SQL_LISTA_ORDEN_COMPRA;
        SQL_LISTA_ORDEN_COMPRA = "";
        String tituloRep = "";
        fechaActual = Utilitarios.hoyAsString();
        //fecha inicial llena, fecha final llena, proveedor llena
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fechai + "','DD/MM/YYYY')"
                    + " and to_date('" + fechaf + "','DD/MM/YYYY')  order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        }
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fechai + "','DD/MM/YYYY')"
                    + " and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        }//fecha inicial vacio, fecha final vacio, proveedor vacio
        else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' "
                    + " and t.oc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "order by t.oc_nropedkey";
            tituloRep = "ORDENES DE COMPRA";
        } else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' "
                    + " and t.oc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " and t.oc_nropedid like '" + orden + "'order by t.oc_nropedkey";
            tituloRep = "ORDENES DE COMPRA";
        }//fecha inicial llena, fecha final llena, proveedor vacio 
        else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fechai + "','DD/MM/YYYY')"
                    + " and to_date('" + fechaf + "','DD/MM/YYYY')  order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        } else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fechai + "','DD/MM/YYYY')"
                    + " and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        }//fecha inicial llena, fecha final vacio, proveedor lleno  
        else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi >= to_date('" + fechai + "','DD/MM/YYYY') order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi >= to_date('" + fechai + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacio, fecha final llena, proveedor lleno 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi <= to_date('" + fechaf + "','DD/MM/YYYY')  order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi <= to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial llena, fecha final vacio, proveedor vacio 
        else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi >= to_date('" + fechai + "','DD/MM/YYYY')  order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi >= to_date('" + fechai + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai;
        } //fecha inicial vacia, fecha final vacio, proveedor lleno 
        else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "'"
                    + " and t.oc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + proveedor.trim()
                    + "' and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "'"
                    + " and t.oc_fecemi between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "ORDEN DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacia, fecha final lleno, proveedor vacio 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi <= to_date('" + fechaf + "','DD/MM/YYYY')  order by t.oc_nropedkey";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion in (" + situacion + ") and t.oc_ind='" + oc_ind + "' and t.oc_fecemi <= to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        }
        P_WHERER = SQL_LISTA_ORDEN_COMPRA;
        P_TITULO = tituloRep;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_ORDEN_COMPRA);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setEmp_id(rs.getInt("emp_id"));
                objOrdCompCab.setSuc_id(rs.getInt("suc_id"));
                objOrdCompCab.setOc_ind(rs.getString("oc_ind"));
                objOrdCompCab.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_provid(rs.getLong("oc_provid"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_moneda(rs.getInt("oc_moneda"));
                objOrdCompCab.setOc_mondes(rs.getString("oc_mondes"));
                objOrdCompCab.setOc_tcambio(rs.getDouble("oc_tcambio"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_usuapro(rs.getString("oc_usuapro"));
                objOrdCompCab.setOc_fecapro(rs.getDate("oc_fecapro"));
                objOrdCompCab.setOc_pcapro(rs.getString("oc_pcapro"));
                objOrdCompCab.setOc_almori(rs.getInt("oc_almori"));
                objOrdCompCab.setOc_almorides(rs.getString("oc_almorides"));
                objOrdCompCab.setOc_almdes(rs.getInt("oc_almdes"));
                objOrdCompCab.setOc_almdesdes(rs.getString("oc_almdesdes"));
                objOrdCompCab.setOc_vafecto(rs.getDouble("oc_vafecto"));
                objOrdCompCab.setOc_exonerado(rs.getDouble("oc_exonerado"));
                objOrdCompCab.setOc_vimpt(rs.getDouble("oc_vimpt"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("oc_vtotal"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setOc_periodo(rs.getInt("oc_periodo"));
                objOrdCompCab.setOc_glosa(rs.getString("oc_glosa"));
                objOrdCompCab.setOc_conid(rs.getInt("oc_conid"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_lpcid(rs.getInt("oc_lpcid"));
                objOrdCompCab.setOc_lpcdes(rs.getString("oc_lpcdes"));
                objOrdCompCab.setOc_vdesc(rs.getDouble("oc_vdesc"));
                objOrdCompCab.setPedcom_key(rs.getLong("pedcom_key"));
                objOrdCompCab.setPedcom_id(rs.getString("pedcom_id"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompCab> listaOrdCompCabApro(int emp_id, int suc_id, String prov_id, String oc_ind, String situacion, String fecinicial, String fecfinal) throws SQLException {
        String SQL_ORDCOMPCAB;
        if ("%%".equals(situacion)) {
            SQL_ORDCOMPCAB = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + prov_id.trim()
                    + "' and t.oc_situacion in ('1','2','7') and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                    + " and to_date('" + fecfinal + "','DD/MM/YYYY') and t.oc_est=1 order by t.oc_nropedkey";
        } else {
            SQL_ORDCOMPCAB = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.prov_id like '" + prov_id.trim()
                    + "' and t.oc_situacion in ('" + situacion + "') and t.oc_ind='" + oc_ind + "' and t.oc_fecemi between to_date('" + fecinicial + "','DD/MM/YYYY')"
                    + " and to_date('" + fecfinal + "','DD/MM/YYYY') and t.oc_est=1 order by t.oc_nropedkey";
        }
        P_WHERER = SQL_ORDCOMPCAB;
        P_TITULO = "ORDEN DE COMPRA DESDE " + fecinicial + " HASTA " + fecfinal;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ORDCOMPCAB);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setEmp_id(rs.getInt("emp_id"));
                objOrdCompCab.setSuc_id(rs.getInt("suc_id"));
                objOrdCompCab.setOc_ind(rs.getString("oc_ind"));
                objOrdCompCab.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_provid(rs.getLong("oc_provid"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_moneda(rs.getInt("oc_moneda"));
                objOrdCompCab.setOc_mondes(rs.getString("oc_mondes"));
                objOrdCompCab.setOc_tcambio(rs.getDouble("oc_tcambio"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_usuapro(rs.getString("oc_usuapro"));
                objOrdCompCab.setOc_fecapro(rs.getDate("oc_fecapro"));
                objOrdCompCab.setOc_pcapro(rs.getString("oc_pcapro"));
                objOrdCompCab.setOc_almori(rs.getInt("oc_almori"));
                objOrdCompCab.setOc_almorides(rs.getString("oc_almorides"));
                objOrdCompCab.setOc_almdes(rs.getInt("oc_almdes"));
                objOrdCompCab.setOc_almdesdes(rs.getString("oc_almdesdes"));
                objOrdCompCab.setOc_vafecto(rs.getDouble("oc_vafecto"));
                objOrdCompCab.setOc_exonerado(rs.getDouble("oc_exonerado"));
                objOrdCompCab.setOc_vimpt(rs.getDouble("oc_vimpt"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("oc_vtotal"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setOc_periodo(rs.getInt("oc_periodo"));
                objOrdCompCab.setOc_glosa(rs.getString("oc_glosa"));
                objOrdCompCab.setOc_conid(rs.getInt("oc_conid"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_lpcid(rs.getInt("oc_lpcid"));
                objOrdCompCab.setOc_lpcdes(rs.getString("oc_lpcdes"));
                objOrdCompCab.setOc_vdesc(rs.getDouble("oc_vdesc"));
                objOrdCompCab.setPedcom_key(rs.getLong("pedcom_key"));
                objOrdCompCab.setPedcom_id(rs.getString("pedcom_id"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompCab> listaOrdCompCabRec(int emp_id, int suc_id, String oc_ind, String proveedor, String producto, String linea, String sublinea, String fechai, String fechaf, String situacion, String orden) throws SQLException {
        String SQL_LISTA_ORDEN_COMPRA;
        SQL_LISTA_ORDEN_COMPRA = "";
        String tituloRep = "";
        fechaActual = Utilitarios.hoyAsString();
        String sit;
        if ("%%".equals(situacion)) {
            //todos 
            sit = "in ('2','3','5')";
        } else {
            sit = "like '" + situacion + "' ";
        }
        //fecha inicial llena, fecha final llena, proveedor llena
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec between to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        }
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec between to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        }//fecha inicial vacio, fecha final vacio, proveedor vacio
        else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "'"
                    + "and t.oc_fecrec between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDENES DE COMPRA";
        } else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "'"
                    + "and t.oc_fecrec between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + " and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDENES DE COMPRA";
        }//fecha inicial llena, fecha final llena, proveedor vacio 
        else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec between to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        } else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec between to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and to_date('" + fechaf + "','DD/MM/YYYY') and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " HASTA " + fechaf;
        }//fecha inicial llena, fecha final vacio, proveedor lleno  
        else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec >= to_date('" + fechai + "','DD/MM/YYYY')  "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec >= to_date('" + fechai + "','DD/MM/YYYY')  "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacio, fecha final llena, proveedor lleno 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec <= to_date('" + fechaf + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' and t.oc_fecrec <= to_date('" + fechaf + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA HASTA " + fechaf + " SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial llena, fecha final vacio, proveedor vacio 
        else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec >= to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai;
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec >= to_date('" + fechai + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA DESDE " + fechai;
        } //fecha inicial vacia, fecha final vacio, proveedor lleno 
        else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' "
                    + "and t.oc_fecrec between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.prov_id like '" + proveedor.trim() + "' "
                    + "and t.oc_fecrec between to_date('01/01/2000','dd/mm/yyyy') and  to_date('" + fechaActual + "','dd/mm/yyyy') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like '" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "ORDEN DE COMPRA SEGUN PROVEEDOR " + proveedor;
        } //fecha inicial vacia, fecha final lleno, proveedor vacio 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec <= to_date('" + fechaf + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' order by t.oc_nropedkey ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !orden.isEmpty()) {
            SQL_LISTA_ORDEN_COMPRA = "select * from v_listaordcompcab t "
                    + "where t.oc_nropedkey in "
                    + "(select p.oc_nropedkey from tordcompra_det p "
                    + "where p.pro_id like '" + producto + "' and substr(pro_id,1,3) like '" + linea + "' "
                    + "and substr(pro_id,1,6) like '" + sublinea + "' and p.ocd_est=1) "
                    + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_ind='" + oc_ind + "' "
                    + "and t.oc_est=1 and t.oc_fecrec <= to_date('" + fechaf + "','DD/MM/YYYY') "
                    + "and t.oc_situacion " + sit + " and t.oc_ind='" + oc_ind + "' and t.oc_nropedid like'" + orden + "' order by t.oc_nropedkey ";
            tituloRep = "PEDIDOS DE COMPRA HASTA " + fechaf;
        }
        P_WHERER = SQL_LISTA_ORDEN_COMPRA;
        P_TITULO = tituloRep;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_ORDEN_COMPRA);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setEmp_id(rs.getInt("emp_id"));
                objOrdCompCab.setSuc_id(rs.getInt("suc_id"));
                objOrdCompCab.setOc_ind(rs.getString("oc_ind"));
                objOrdCompCab.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_provid(rs.getLong("oc_provid"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_moneda(rs.getInt("oc_moneda"));
                objOrdCompCab.setOc_mondes(rs.getString("oc_mondes"));
                objOrdCompCab.setOc_tcambio(rs.getDouble("oc_tcambio"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_usuapro(rs.getString("oc_usuapro"));
                objOrdCompCab.setOc_fecapro(rs.getDate("oc_fecapro"));
                objOrdCompCab.setOc_pcapro(rs.getString("oc_pcapro"));
                objOrdCompCab.setOc_almori(rs.getInt("oc_almori"));
                objOrdCompCab.setOc_almorides(rs.getString("oc_almorides"));
                objOrdCompCab.setOc_almdes(rs.getInt("oc_almdes"));
                objOrdCompCab.setOc_almdesdes(rs.getString("oc_almdesdes"));
                objOrdCompCab.setOc_vafecto(rs.getDouble("oc_vafecto"));
                objOrdCompCab.setOc_exonerado(rs.getDouble("oc_exonerado"));
                objOrdCompCab.setOc_vimpt(rs.getDouble("oc_vimpt"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("oc_vtotal"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setOc_periodo(rs.getInt("oc_periodo"));
                objOrdCompCab.setOc_glosa(rs.getString("oc_glosa"));
                objOrdCompCab.setOc_conid(rs.getInt("oc_conid"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_lpcid(rs.getInt("oc_lpcid"));
                objOrdCompCab.setOc_lpcdes(rs.getString("oc_lpcdes"));
                objOrdCompCab.setOc_vdesc(rs.getDouble("oc_vdesc"));
                objOrdCompCab.setPedcom_key(rs.getLong("pedcom_key"));
                objOrdCompCab.setPedcom_id(rs.getString("pedcom_id"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                //objOrdCompCab.setOc_situacion(rs.getInt("oc_sitdes"));
                objOrdCompCab.setLp_descgen(rs.getDouble("lp_descgen"));
                objOrdCompCab.setLp_descfinan(rs.getDouble("lp_descfinan"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompDet> listaOrdCompDetNotaES(int emp_id, int suc_id, long oc_nropedkey, String oc_ind) throws SQLException {
        String SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                + " t.pro_desdes,t.pro_des,t.ocd_idubi,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                + " t.ocd_precio,z.ocd_cantped,t.ocd_cantate*t.pro_presminven ocd_cantate,z.ocd_vafecto,z.ocd_exonerado,"
                + " z.ocd_vimpto,t.ocd_pimpto,z.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                + " from ( "
                + " select c.pro_id,sum(c.cant) ocd_cantped,sum(c.valafe) ocd_vafecto,sum(c.valina) ocd_exonerado,"
                + " sum(c.vimpto) ocd_vimpto, sum(c.pvta) ocd_vtotal, sum(c.peso) pro_peso from ("
                + " select t.pro_id,t.ocd_cantped*t.pro_presminven cant,t.ocd_vafecto valafe, t.ocd_exonerado valina,"
                + " t.ocd_vimpto vimpto, t.ocd_vtotal pvta, t.pro_peso peso from codijisa.v_listaordcompdet t"
                + " where t.emp_id=" + emp_id + " "
                + " and t.suc_id=" + suc_id + " "
                + " and t.oc_nropedkey=" + oc_nropedkey + " "
                + " union all "
                + " select y.pro_id,nvl(y.nesdet_cant*-1,0) cant, nvl(y.nesdet_valafe*-1,0) valafe, nvl(y.nesdet_valina*-1,0) valina, "
                + " nvl(y.nesdet_vimpto*-1,0) vimpto,nvl(y.nesdet_pvta*-1,0) pvta, nvl(y.nesdet_peso,0) peso  from codijisa.v_listanotaescab x,codijisa.v_listanotaesdet y "
                + " where x.emp_id=y.emp_id "
                + " and x.suc_id=y.suc_id "
                + " and x.nescab_id=y.nescab_id "
                + " and y.nesdet_est=1 "
                + " and x.nescab_est=1 "
                + " and x.emp_id=" + emp_id + " "
                + " and x.suc_id=" + suc_id + " "
                + " and x.nescab_ocnropedkey=" + oc_nropedkey + ""
                + " ) c group by c.pro_id "
                + " ) z, v_listaordcompdet t,tproductos pr "
                + " where  "
                + " t.emp_id=" + emp_id + " and z.pro_id = pr.pro_id and t.pro_id=z.pro_id"
                + " and t.suc_id=" + suc_id + " "
                + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "' and t.ocd_est=1"
                //filtro de nota
                + " and z.ocd_cantped > 0";

        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objOrdCompDet.setPro_presminven(rs.getString("pro_presminven"));
                objOrdCompDet.setPro_unipeso(rs.getString("pro_unipeso"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                objOrdCompDet.setPro_unimas(rs.getString("pro_unimas"));
                objOrdCompDet.setOcd_canprov(rs.getLong("can_prov"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setPro_svol(rs.getDouble("pro_vol"));
                objOrdCompDet.setPro_sunivol(rs.getString("uni_volumen"));
                objOrdCompDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));

                objOrdCompDet.setPro_unipesototal(rs.getString("unipesototal"));
                objOrdCompDet.setPro_pesototal(rs.getDouble("pesototal"));

                objOrdCompDet.setPro_svoltotal(rs.getDouble("svoltotal"));
                objOrdCompDet.setPro_sunivoltotal(rs.getString("uni_volumen"));

                //objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
                objlstOrdCompDet.add(objOrdCompDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompDet.getSize() + " registro(s)");
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
        return objlstOrdCompDet;
    }

    public ListModelList<OrdCompDet> listaOrdCompDet(int emp_id, int suc_id, long oc_nropedkey, String oc_ind) throws SQLException {
        String SQL_LISTAORDCOMPDET = "select * from v_listaordcompdet t where t.ocd_est=1 and t.emp_id=" + emp_id
                + " and t.suc_id=" + suc_id + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "'";
        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objOrdCompDet.setPro_presmincom(rs.getString("pro_presmincom"));
                objOrdCompDet.setPro_presminven(rs.getString("pro_presminven"));
                objOrdCompDet.setPro_unipeso(rs.getString("pro_unipeso"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setPro_svol(rs.getDouble("pro_vol"));
                objOrdCompDet.setPro_sunivol(rs.getString("uni_volumen"));
                objOrdCompDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setPro_unipesototal(rs.getString("unipesototal"));
                objOrdCompDet.setPro_pesototal(rs.getDouble("pesototal"));
                objOrdCompDet.setPro_svoltotal(rs.getDouble("svoltotal"));
                objOrdCompDet.setPro_sunivoltotal(rs.getString("uni_volumen"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));

                //objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objlstOrdCompDet.add(objOrdCompDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompDet.getSize() + " registro(s)");
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
        return objlstOrdCompDet;
    }

    public ListModelList<OrdCompDet> listaOrdCompDetxProd(int emp_id, int suc_id, long oc_nropedkey, String oc_ind, String pro_id) throws SQLException {
        String SQL_LISTAORDCOMPDET = "select * from v_listaordcompdet t where t.ocd_est=1 and t.emp_id=" + emp_id
                + " and t.suc_id=" + suc_id + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "' and t.pro_des like '" + pro_id + "'";
        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objOrdCompDet.setPro_presmincom(rs.getString("pro_presmincom"));
                objOrdCompDet.setPro_unipeso(rs.getString("pro_unipeso"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_peso(rs.getDouble("ocd_peso"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
                objlstOrdCompDet.add(objOrdCompDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompDet.getSize() + " registro(s)");
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
        return objlstOrdCompDet;
    }

    public ListModelList<OrdCompDet> listaOrdCompDetFact(int emp_id, int suc_id, long oc_nropedkey, String oc_ind) throws SQLException {
        String SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                + " t.pro_desdes,t.pro_des,t.ocd_idubi,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                + " t.ocd_precio,z.cant_ped,t.ocd_cantate,t.ocd_vafecto,z.cant_fac,"
                //+ " z.pv_oc,z.p_vta,z.imp_neto,"
                + " z.val_vta,z.igv,z.subtotal,"
                + " t.ocd_pimpto,t.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                + " from ( "
                + " select c.pro_id pro_id,sum(c.cant_ped) cant_ped,sum(c.cant_fac) cant_fac,"
                //+ " sum(c.pv_oc) pv_oc,sum(c.p_vta) p_vta,sum(c.imp_neto) imp_neto,"
                + " sum(c.val_vta) val_vta,sum(c.igv) igv,sum(c.subtotal) subtotal from ("
                + " select t.pro_id pro_id,t.ocd_cantped cant_ped,t.ocd_cantped cant_fac,"
                //+ " round(t.ocd_vtotal,2) pv_oc,"
                //+ " round(t.ocd_vtotal,3) p_vta,"
                //+ " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) imp_neto,"
                + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) val_vta,"
                + " round(decode(t.ocd_vafecto,0,0,t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100),3) igv,"
                + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,(t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100)+t.ocd_vafecto),3) subtotal"
                + " from codijisa.v_listaordcompdet t"
                + " where t.ocd_est=1"
                + " and t.emp_id=" + emp_id + " "
                + " and t.suc_id=" + suc_id + " "
                + " and t.oc_nropedkey=" + oc_nropedkey + " "
                + " union "
                + " select fd.pro_id,(fd.tcd_cantfac)*-1 cant_ped,(fd.tcd_cantfac)*-1 cant_fac,"
                //+ " round((fd.tcd_impnet)*-1,3) pv_oc, "
                //+ " round((fd.tcd_impnet)*-1,3) p_vta,"
                //+ " round((fd.tcd_imptot)*-1,3) imp_neto,"
                + " round((fd.tcd_vventafac)*-1,3) val_vta, "
                + " round((fd.tcd_igvfac)*-1,3) igv,"
                + " round((fd.tcd_subtotalfac)*-1,3) subtotal"
                + " from codijisa.v_listaordcompdet t,v_listafacturaproveedordet fd,v_listafacturaproveedorcab fc  "
                + " where fc.tc_oc=t.oc_nropedkey "
                + " and fd.tc_key=fc.tc_key "
                + " and t.pro_id=fd.pro_id "
                + " and t.emp_id=fc.emp_id "
                + " and t.suc_id=fc.suc_id "
                + " and fc.emp_id=fd.emp_id "
                + " and fc.suc_id=fd.suc_id "
                + " and t.oc_nropedkey=" + oc_nropedkey + ""
                + " ) c group by c.pro_id "
                + " ) z, v_listaordcompdet t,tproductos pr "
                + " where  "
                + " t.emp_id=" + emp_id + " and z.pro_id = pr.pro_id and t.pro_id=z.pro_id"
                + " and t.suc_id=" + suc_id + " "
                + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "' and t.ocd_est=1"
                //filtro de nota
                + " and z.cant_ped > 0";

        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                /**
                 * ********Datos especiales para factura******************
                 */
                objOrdCompDet.setCant_fac(rs.getDouble("cant_fac"));
                //objOrdCompDet.setPv_oc(rs.getDouble("pv_oc"));
                //objOrdCompDet.setP_vta(rs.getDouble("p_vta"));
                //objOrdCompDet.setImp_neto(rs.getDouble("imp_neto"));
                objOrdCompDet.setVal_vta(rs.getDouble("val_vta"));
                objOrdCompDet.setIgv(rs.getDouble("igv"));
                objOrdCompDet.setSubtotal(rs.getDouble("subtotal"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                objOrdCompDet.setPro_unimas(rs.getString("pro_unimas"));
                objOrdCompDet.setOcd_canprov(rs.getLong("can_prov"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("cant_ped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setPro_svol(rs.getDouble("pro_vol"));
                objOrdCompDet.setPro_sunivol(rs.getString("uni_volumen"));
                objOrdCompDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setPro_unipesototal(rs.getString("unipesototal"));
                objOrdCompDet.setPro_pesototal(rs.getDouble("pesototal"));
                objOrdCompDet.setPro_svoltotal(rs.getDouble("svoltotal"));
                objOrdCompDet.setPro_sunivoltotal(rs.getString("uni_volumen"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                //objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                //objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                //objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
                objlstOrdCompDet.add(objOrdCompDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompDet.getSize() + " registro(s)");
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
        return objlstOrdCompDet;
    }

    public ListModelList<OrdCompProv> listaTotalxProveedor(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id) throws SQLException {
        String validaFecha, SQL_CONSULTAXPROVEEDOR, situacion = "";
        ListModelList<OrdCompProv> objlstOrdCompProv = new ListModelList<OrdCompProv>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
        } else {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        }

        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_CONSULTAXPROVEEDOR = "select t.emp_id, t.suc_id, t.oc_provid prov_key, t.prov_id, t.oc_provrazsoc prov_razsoc,"
                + " count(distinct t.oc_nropedkey) prov_occant,"
                + " sum(p.ocd_vdesc) prov_vdesc, "
                + " sum(p.ocd_vafecto) prov_vafecto,"
                + " sum(p.ocd_vinafecto) prov_vinafecto,"
                + " sum(p.ocd_exonerado) prov_vexonerado,"
                + " sum(p.ocd_vimpto) prov_vimpto,"
                + " sum(p.ocd_vtotal) prov_vtotal "
                + " from v_listaordcompcab t,v_listaordcompdet p"
                + " where t.emp_id=p.emp_id and t.suc_id=p.suc_id and"
                + " t.oc_nropedkey=p.oc_nropedkey and"
                + " t.oc_est=1 and p.ocd_est=1 and"
                + " t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion " + situacion + " and"
                + " t.oc_periodo like '" + periodo + "' and t.prov_id like '" + prov_id + "' " + validaFecha
                + " group by t.emp_id, t.suc_id,t.oc_provid,t.prov_id,t.oc_provrazsoc";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXPROVEEDOR);
            while (rs.next()) {
                OrdCompProv objOrdCompProv = new OrdCompProv();
                objOrdCompProv.setEmp_id(rs.getInt("emp_id"));
                objOrdCompProv.setSuc_id(rs.getInt("suc_id"));
                objOrdCompProv.setProv_key(rs.getLong("prov_key"));
                objOrdCompProv.setProv_id(rs.getString("prov_id"));
                objOrdCompProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objOrdCompProv.setFecemi(fec_inicial);
                objOrdCompProv.setFecfin(fec_final);
                objOrdCompProv.setPeriodo(periodo);
                objOrdCompProv.setCant(rs.getLong("prov_occant"));
                objOrdCompProv.setVdesc(rs.getDouble("prov_vdesc"));
                objOrdCompProv.setVafecto(rs.getDouble("prov_vafecto"));
                objOrdCompProv.setVinafecto(rs.getDouble("prov_vinafecto"));
                objOrdCompProv.setVexonerado(rs.getDouble("prov_vexonerado"));
                objOrdCompProv.setVimpto(rs.getDouble("prov_vimpto"));
                objOrdCompProv.setVtotal(rs.getDouble("prov_vtotal"));
                objlstOrdCompProv.add(objOrdCompProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstOrdCompProv.size() + " Registro(s)");
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
        return objlstOrdCompProv;
    }

    public ListModelList<OrdCompProd> listaTotalxProducto(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id, String pro_id) throws SQLException {
        String validaFecha, SQL_CONSULTAXPRODUCTO, situacion = "";
        ListModelList<OrdCompProd> objlstOrdCompProd = new ListModelList<OrdCompProd>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
        } else {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_CONSULTAXPRODUCTO = "select p.emp_id,p.suc_id,p.pro_id,p.pro_des,p.pro_desdes,"
                + " count(distinct t.oc_nropedkey) pro_cant,"
                + " sum(p.ocd_vdesc) prod_vdesc, "
                + " sum(p.ocd_vafecto) prod_vafecto,"
                + " sum(p.ocd_vinafecto) prod_vinafecto,"
                + " sum(p.ocd_exonerado) prod_vexonerado,"
                + " sum(p.ocd_vimpto) prod_vimpto,"
                + " sum(p.ocd_vtotal) prod_vtotal "
                + " from v_listaordcompcab t, v_listaordcompdet p"
                + " where t.oc_nropedkey=p.oc_nropedkey and t.emp_id=p.emp_id and"
                + " t.oc_est=1 and p.ocd_est=1 and t.suc_id=p.suc_id and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and"
                + " p.pro_id like '" + pro_id + "' and t.prov_id like '" + prov_id + "' and"
                + " t.oc_periodo like '" + periodo + "' " + validaFecha + " and t.oc_situacion " + situacion
                + " group by p.emp_id,p.suc_id,p.pro_id,p.pro_des,p.pro_desdes";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXPRODUCTO);
            OrdCompProd objOrdCompProd;
            while (rs.next()) {
                objOrdCompProd = new OrdCompProd();
                objOrdCompProd.setEmp_id(rs.getInt("emp_id"));
                objOrdCompProd.setSuc_id(rs.getInt("suc_id"));
                /*objOrdCompProd.setPro_key(rs.getLong("pro_key"));*/
                objOrdCompProd.setPro_id(rs.getString("pro_id"));
                objOrdCompProd.setPro_des(rs.getString("pro_des"));
                objOrdCompProd.setPro_desdes(rs.getString("pro_desdes"));
                objOrdCompProd.setCant(rs.getLong("pro_cant"));
                objOrdCompProd.setFecemi(fec_inicial);
                objOrdCompProd.setFecfin(fec_final);
                objOrdCompProd.setPeriodo(periodo);
                objOrdCompProd.setVdesc(rs.getDouble("prod_vdesc"));
                objOrdCompProd.setVafecto(rs.getDouble("prod_vafecto"));
                objOrdCompProd.setVinafecto(rs.getDouble("prod_vinafecto"));
                objOrdCompProd.setVexonerado(rs.getDouble("prod_vexonerado"));
                objOrdCompProd.setVimpto(rs.getDouble("prod_vimpto"));
                objOrdCompProd.setVtotal(rs.getDouble("prod_vtotal"));
                objlstOrdCompProd.add(objOrdCompProd);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstOrdCompProd.size() + " Registro(s)");
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
        return objlstOrdCompProd;
    }

    public ListModelList<OrdCompLin> listaTotalxLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_CONSULTAXLINEA, situacion = "";
        ListModelList<OrdCompLin> objlstOrdCompLin = new ListModelList<OrdCompLin>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and ";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') and ";
        } else {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and ";
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_CONSULTAXLINEA = "select t.emp_id,t.suc_id,p.tab_id lin_key ,p.tab_subdir lin_id, p.tab_subdes lin_des,"
                + "count(distinct t.oc_nropedkey) lin_cant,"
                + " sum(t.ocd_vdesc) lin_vdesc, "
                + " sum(t.ocd_vafecto) lin_vafecto,"
                + " sum(t.ocd_vinafecto) lin_vinafecto,"
                + " sum(t.ocd_exonerado) lin_vexonerado,"
                + " sum(t.ocd_vimpto) lin_vimpto,"
                + " sum(t.ocd_vtotal) lin_vtotal "
                + "from v_listaordcompdet t, ttabgen p,tordcompra_cab x "
                + "where p.tab_id=to_number(substr(t.pro_id,0,3)) and "
                + "t.emp_id=x.emp_id and t.suc_id=x.suc_id and "
                + "t.oc_nropedkey=x.oc_nropedkey and "
                + "p.tab_cod=4 and t.ocd_est=1 and x.oc_est=1 and "
                + "t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                + "p.tab_id like '" + lin_key + "' and t.ocd_periodo like '" + periodo + "' " + validaFecha
                + "x.oc_situacion " + situacion
                + "group by t.emp_id,t.suc_id,p.tab_id,p.tab_subdir, p.tab_subdes";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXLINEA);
            OrdCompLin objOrdCompLin;
            while (rs.next()) {
                objOrdCompLin = new OrdCompLin();
                objOrdCompLin.setEmp_id(rs.getInt("emp_id"));
                objOrdCompLin.setSuc_id(rs.getInt("suc_id"));
                objOrdCompLin.setLin_key(rs.getInt("lin_key"));
                objOrdCompLin.setLin_id(rs.getString("lin_id"));
                objOrdCompLin.setLin_des(rs.getString("lin_des"));
                objOrdCompLin.setCant(rs.getLong("lin_cant"));
                objOrdCompLin.setFecemi(fec_inicial);
                objOrdCompLin.setFecfin(fec_final);
                objOrdCompLin.setPeriodo(periodo);
                objOrdCompLin.setVdesc(rs.getDouble("lin_vdesc"));
                objOrdCompLin.setVafecto(rs.getDouble("lin_vafecto"));
                objOrdCompLin.setVinafecto(rs.getDouble("lin_vinafecto"));
                objOrdCompLin.setVexonerado(rs.getDouble("lin_vexonerado"));
                objOrdCompLin.setVimpto(rs.getDouble("lin_vimpto"));
                objOrdCompLin.setVtotal(rs.getDouble("lin_vtotal"));
                
                objlstOrdCompLin.add(objOrdCompLin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstOrdCompLin.size() + " Registro(s)");
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
        return objlstOrdCompLin;
    }

    public ListModelList<OrdCompSublin> listaTotalxSubLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_CONSULTAXSUBLINEA, situacion = "";
        ListModelList<OrdCompSublin> objlstOrdCompSublin = new ListModelList<OrdCompSublin>();
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and ";
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') and ";
        } else {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and ";
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_CONSULTAXSUBLINEA = "select t.emp_id,t.suc_id,p.slin_codslin slin_key ,p.slin_cod slin_id, p.slin_des slin_des,count(distinct t.oc_nropedkey) "
                + "slin_cant,sum(t.ocd_vimpto) slin_vimpto,sum(t.ocd_vdesc) slin_vdesc,sum(t.ocd_vafecto) slin_vafecto,sum(t.ocd_vtotal) slin_vtotal "
                + "from v_listaordcompdet t, tsublineas p,tordcompra_cab x "
                + "where p.slin_cod=to_number(codijisa.pack_tproductos.f_004_descslineaproducto(t.pro_id))and "
                + "t.emp_id=x.emp_id and t.suc_id=x.suc_id and "
                + "t.oc_nropedkey=x.oc_nropedkey and "
                + "t.ocd_est=1 and x.oc_est=1 and "
                + "t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                + "p.slin_cod like '" + lin_key + "' and t.ocd_periodo like '" + periodo + "' " + validaFecha
                + "x.oc_situacion " + situacion
                + "group by t.emp_id,t.suc_id,p.slin_codslin,p.slin_cod, p.slin_des";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTAXSUBLINEA);
            OrdCompSublin objOrdCompSublin;
            while (rs.next()) {
                objOrdCompSublin = new OrdCompSublin();
                objOrdCompSublin.setEmp_id(rs.getInt("emp_id"));
                objOrdCompSublin.setSuc_id(rs.getInt("suc_id"));
                objOrdCompSublin.setSlin_key(rs.getInt("slin_key"));
                objOrdCompSublin.setSlin_id(rs.getString("slin_id"));
                objOrdCompSublin.setSlin_des(rs.getString("slin_des"));
                objOrdCompSublin.setCant(rs.getLong("slin_cant"));
                objOrdCompSublin.setFecemi(fec_inicial);
                objOrdCompSublin.setFecfin(fec_final);
                objOrdCompSublin.setPeriodo(periodo);
                objOrdCompSublin.setVimpto(rs.getDouble("slin_vimpto"));
                objOrdCompSublin.setVdesc(rs.getDouble("slin_vdesc"));
                objOrdCompSublin.setVafecto(rs.getDouble("slin_vafecto"));
                objOrdCompSublin.setVtotal(rs.getDouble("slin_vtotal"));
                objlstOrdCompSublin.add(objOrdCompSublin);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstOrdCompSublin.size() + " Registro(s)");
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
        return objlstOrdCompSublin;
    }

    public ListModelList<OrdCompCab> OrdCompxProveedor(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String prov_id) throws SQLException {
        String validaFecha, SQL_LISTAORDCOMP, titulo, situacion = "";
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PROVEEDOR DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "t.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PROVEEDOR HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PROVEEDOR DESDE " + fec_inicial;
        } else {
            validaFecha = "t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy') and";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PROVEEDOR DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_LISTAORDCOMP = "select * from v_listaordcompcab t "
                + "where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + " and t.oc_situacion " + situacion
                + " and t.oc_periodo like '" + periodo + "' and " + validaFecha
                + " t.oc_provid like '" + prov_id + "' "
                + "order by t.oc_nropedkey";
        P_WHERER = SQL_LISTAORDCOMP;
        P_TITULO = titulo;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMP);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setEmp_id(rs.getInt("emp_id"));
                objOrdCompCab.setSuc_id(rs.getInt("suc_id"));
                objOrdCompCab.setOc_ind(rs.getString("oc_ind"));
                objOrdCompCab.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_provid(rs.getLong("oc_provid"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_moneda(rs.getInt("oc_moneda"));
                objOrdCompCab.setOc_mondes(rs.getString("oc_mondes"));
                objOrdCompCab.setOc_tcambio(rs.getDouble("oc_tcambio"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_usuapro(rs.getString("oc_usuapro"));
                objOrdCompCab.setOc_fecapro(rs.getDate("oc_fecapro"));
                objOrdCompCab.setOc_pcapro(rs.getString("oc_pcapro"));
                objOrdCompCab.setOc_almori(rs.getInt("oc_almori"));
                objOrdCompCab.setOc_almorides(rs.getString("oc_almorides"));
                objOrdCompCab.setOc_almdes(rs.getInt("oc_almdes"));
                objOrdCompCab.setOc_almdesdes(rs.getString("oc_almdesdes"));
                objOrdCompCab.setOc_vafecto(rs.getDouble("oc_vafecto"));
                objOrdCompCab.setOc_exonerado(rs.getDouble("oc_exonerado"));
                objOrdCompCab.setOc_vimpt(rs.getDouble("oc_vimpt"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("oc_vtotal"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setOc_periodo(rs.getInt("oc_periodo"));
                objOrdCompCab.setOc_glosa(rs.getString("oc_glosa"));
                objOrdCompCab.setOc_conid(rs.getInt("oc_conid"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_lpcid(rs.getInt("oc_lpcid"));
                objOrdCompCab.setOc_lpcdes(rs.getString("oc_lpcdes"));
                objOrdCompCab.setOc_vdesc(rs.getDouble("oc_vdesc"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompCab> OrdCompxProducto(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String pro_id) throws SQLException {
        String validaFecha, SQL_LISTAORDCOMP, titulo, situacion = "";
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PRODUCTO DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PRODUCTO HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PRODUCTO DESDE " + fec_inicial;
        } else {
            validaFecha = "and t.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR PRODUCTO DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' and ";
        }
        SQL_LISTAORDCOMP = "select t.oc_nropedid,t.oc_fecemi,t.oc_fecrec,t.oc_feccad,t.prov_id,t.oc_provrazsoc,t.oc_condes,t.oc_situacion,t.oc_est,"
                + " p.ocd_vtotal,t.oc_usuadd,t.oc_fecadd,t.oc_pcadd,t.oc_usumod,t.oc_fecmod,t.oc_pcmod,t.oc_sitdes from codijisa.v_listaordcompcab t,codijisa.v_listaordcompdet p"
                + " where t.oc_nropedkey=p.oc_nropedkey and t.emp_id=p.emp_id and"
                + " t.suc_id=p.suc_id and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_situacion " + situacion
                + " t.oc_est=1 and p.ocd_est=1 and p.pro_id like '" + pro_id + "' and"
                + " t.oc_periodo like '" + periodo + "' " + validaFecha;
        P_WHERER = SQL_LISTAORDCOMP;
        P_TITULO = titulo;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMP);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompCab> OrdCompxLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_LISTAORDCOMP, titulo, situacion = "";
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE ORDEN DE COMPRA POR LINEA DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR LINEA HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR LINEA DESDE " + fec_inicial;
        } else {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR LINEA DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_LISTAORDCOMP = "select x.oc_nropedid,x.oc_fecemi,x.oc_fecrec,x.oc_feccad,x.prov_id,x.oc_provrazsoc,x.oc_condes,x.oc_situacion,x.oc_est,"
                + " nvl(to_number(sum(t.ocd_vtotal)),0) ocd_vtotal,x.oc_usuadd,x.oc_fecadd,x.oc_pcadd,x.oc_usumod,x.oc_fecmod,x.oc_pcmod,x.oc_sitdes"
                + " from v_listaordcompdet t, ttabgen p,v_listaordcompcab x "
                + " where p.tab_id=to_number(substr(t.pro_id,0,3)) and "
                + " t.emp_id=x.emp_id and t.suc_id=x.suc_id and "
                + " t.oc_nropedkey=x.oc_nropedkey and "
                + " p.tab_cod=4 and t.ocd_est=1 and x.oc_est=1 and "
                + " x.oc_situacion " + situacion + " and "
                + " t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and "
                + " p.tab_id like '" + lin_key + "' and t.ocd_periodo like '" + periodo + "' " + validaFecha
                + " group by x.oc_nropedid,x.oc_fecemi,x.oc_fecrec,x.oc_feccad,x.prov_id,x.oc_provrazsoc,x.oc_condes,x.oc_situacion,x.oc_est,"
                + " x.oc_usuadd,x.oc_fecadd,x.oc_pcadd,x.oc_usumod,x.oc_fecmod,x.oc_pcmod";
        P_WHERER = SQL_LISTAORDCOMP;
        P_TITULO = titulo;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMP);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public ListModelList<OrdCompCab> OrdCompxSubLinea(int emp_id, int suc_id, String periodo, String sit, String fec_inicial, String fec_final, String lin_key) throws SQLException {
        String validaFecha, SQL_LISTAORDCOMP, titulo, situacion = "";
        if (fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "";
            titulo = "REPORTE DE ORDEN DE COMPRA POR SUBLINEA DESDE 01/01/2000";
        } else if (fec_inicial.isEmpty() && !fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR SUBLINEA HASTA " + fec_final;
        } else if (!fec_inicial.isEmpty() && fec_final.isEmpty()) {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR SUBLINEA DESDE " + fec_inicial;
        } else {
            validaFecha = "and x.oc_fecemi between to_date('" + fec_inicial + "','dd/mm/yyyy') and to_date('" + fec_final + "','dd/mm/yyyy')";
            titulo = "REPORTE DE ORDEN DE COMPRA POR SUBLINEA DESDE " + fec_inicial + " HASTA " + fec_final;
        }
        if (sit.equals("%45%")) {
            situacion = "between '4' and '5' ";
        }
        SQL_LISTAORDCOMP = "select x.oc_nropedid,x.oc_fecemi,x.oc_fecrec,x.oc_feccad,x.prov_id,x.oc_provrazsoc,x.oc_condes,x.oc_situacion,x.oc_est,"
                + " nvl(to_number(sum(t.ocd_vtotal)),0) ocd_vtotal,x.oc_usuadd,x.oc_fecadd,x.oc_pcadd,x.oc_usumod,x.oc_fecmod,x.oc_pcmod,x.oc_sitdes"
                + " from v_listaordcompdet t, tsublineas p,v_listaordcompcab x"
                + " where p.slin_cod=to_number(codijisa.pack_tproductos.f_004_descslineaproducto(t.pro_id)) and"
                + " t.emp_id=x.emp_id and t.suc_id=x.suc_id and"
                + " t.oc_nropedkey=x.oc_nropedkey and"
                + " t.ocd_est=1 and x.oc_est=1 and"
                + " x.oc_situacion " + situacion + " and"
                + " t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and"
                + " p.slin_cod like '" + lin_key + "' and t.ocd_periodo like '" + periodo + "' " + validaFecha
                + " group by x.oc_nropedid,x.oc_fecemi,x.oc_fecrec,x.oc_feccad,x.prov_id,x.oc_provrazsoc,x.oc_condes,x.oc_situacion,x.oc_est,"
                + " x.oc_usuadd,x.oc_fecadd,x.oc_pcadd,x.oc_usumod,x.oc_fecmod,x.oc_pcmod";
        P_WHERER = SQL_LISTAORDCOMP;
        P_TITULO = titulo;
        objlstOrdCompCab = null;
        objlstOrdCompCab = new ListModelList<OrdCompCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMP);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
                objlstOrdCompCab.add(objOrdCompCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstOrdCompCab.getSize() + " registro(s)");
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
        return objlstOrdCompCab;
    }

    public OrdCompDet ordCompDetxCodigo(int emp_id, int suc_id, long oc_nropedkey, String oc_ind, long ocd_item) throws SQLException {
        String SQL_ORDCOMPDET = "select * from v_listaordcompdet t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_nropedkey=" + oc_nropedkey
                + " and t.oc_ind=" + oc_ind + " and t.ocd_item" + ocd_item + " and t.ocd_est=1";
        objOrdCompDet = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objOrdCompDet.setPro_presmincom(rs.getString("pro_presmincom"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_peso(rs.getDouble("ocd_peso"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado el registro con codigo " + oc_nropedkey);
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
        return objOrdCompDet;
    }

    public OrdCompDet OrdCompDetFacxProd(int emp_id, int suc_id, long oc_nropedkey, String oc_ind, String pro_id, String estado, String fact_cab) throws SQLException {
        String SQL_LISTAORDCOMPDET;
        if ("M".equals(estado)) {
            SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                    + " t.pro_desdes,t.pro_des,t.ocd_idubi,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                    + " t.ocd_precio,z.cant_ped,t.ocd_cantate,z.cant_fac,"
                    //+ " z.pv_oc,z.p_vta,z.imp_neto,"
                    + " z.val_vta,z.igv,z.subtotal,"
                    + " t.ocd_pimpto,t.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                    + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                    + " from ( "
                    + " select c.pro_id pro_id,sum(c.cant_ped) cant_ped,sum(c.cant_fac) cant_fac,"
                    //+ " sum(c.pv_oc) pv_oc,sum(c.p_vta) p_vta,sum(c.imp_neto) imp_neto,"
                    + " sum(c.val_vta) val_vta,sum(c.igv) igv,sum(c.subtotal) subtotal from ("
                    + " select t.pro_id pro_id,t.ocd_cantped cant_ped,t.ocd_cantped cant_fac,"
                    //+ " round(t.ocd_vtotal,2) pv_oc,"
                    //+ " round(t.ocd_vtotal,3) p_vta,"
                    //+ " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) imp_neto,"
                    + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) val_vta,"
                    + " round(decode(t.ocd_vafecto,0,0,t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100),3) igv,"
                    + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,(t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100)+t.ocd_vafecto),3) subtotal"
                    + " from codijisa.v_listaordcompdet t"
                    + " where t.ocd_est=1"
                    + " and t.emp_id=" + emp_id + " "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " "
                    + " union "
                    + " select fd.pro_id,(fd.tcd_cantped)*-1 cant_ped,(fd.tcd_cantfac)*-1 cant_fac,"
                    //+ " round((fd.tcd_impnet)*-1,3) pv_oc, "
                    //+ " round((fd.tcd_impnet)*-1,3) p_vta,"
                    //+ " round((fd.tcd_imptot)*-1,3) imp_neto,"
                    + " round((fd.tcd_vventafac)*-1,3) val_vta, "
                    + " round((fd.tcd_igvfac)*-1,3) igv,"
                    + " round((fd.tcd_subtotalfac)*-1,3) subtotal"
                    + " from codijisa.v_listaordcompdet t,v_listafacturaproveedordet fd,v_listafacturaproveedorcab fc  "
                    + " where fc.tc_oc=t.oc_nropedkey "
                    + " and fd.tc_key=fc.tc_key "
                    + " and t.pro_id=fd.pro_id "
                    + " and t.emp_id=fc.emp_id "
                    + " and t.suc_id=fc.suc_id "
                    + " and fc.emp_id=fd.emp_id "
                    + " and fc.suc_id=fd.suc_id "
                    + " and t.oc_nropedkey=" + oc_nropedkey + "and fc.tc_key not in ('" + fact_cab + "')"
                    + " ) c group by c.pro_id "
                    + " ) z, v_listaordcompdet t,tproductos pr "
                    + " where  "
                    + " t.emp_id=" + emp_id + " and z.pro_id = pr.pro_id and t.pro_id=z.pro_id"
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "' and t.ocd_est=1"
                    + " and pr.pro_id=t.pro_id and t.pro_id=" + pro_id;
        } else {
            SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                    + " t.pro_desdes,t.pro_des,t.ocd_idubi,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                    + " t.ocd_precio,z.cant_ped,t.ocd_cantate,z.cant_fac,"
                    //+ " z.pv_oc,z.p_vta,z.imp_neto,"
                    + " z.val_vta,z.igv,z.subtotal,"
                    + " t.ocd_pimpto,t.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                    + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                    + " from ( "
                    + " select c.pro_id pro_id,sum(c.cant_ped) cant_ped,sum(c.cant_fac) cant_fac,"
                    //+ " sum(c.pv_oc) pv_oc,sum(c.p_vta) p_vta,sum(c.imp_neto) imp_neto,"
                    + " sum(c.val_vta) val_vta,sum(c.igv) igv,sum(c.subtotal) subtotal from ("
                    + " select t.pro_id pro_id,t.ocd_cantped cant_ped,t.ocd_cantped cant_fac,"
                    //+ " round(t.ocd_vtotal,2) pv_oc,"
                    //+ " round(t.ocd_vtotal,3) p_vta,"
                    //+ " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) imp_neto,"
                    + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,t.ocd_vafecto),3) val_vta,"
                    + " round(decode(t.ocd_vafecto,0,0,t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100),3) igv,"
                    + " round(decode(t.ocd_vafecto,0,t.ocd_exonerado,(t.ocd_vafecto*(pack_util.f_retornaimpuesto(1))/100)+t.ocd_vafecto),3) subtotal"
                    + " from codijisa.v_listaordcompdet t"
                    + " where t.ocd_est=1"
                    + " and t.emp_id=" + emp_id + " "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " "
                    + " union "
                    + " select fd.pro_id,(fd.tcd_cantfac)*-1 cant_ped,(fd.tcd_cantfac)*-1 cant_fac,"
                    //+ " round((fd.tcd_impnet)*-1,3) pv_oc, "
                    //+ " round((fd.tcd_impnet)*-1,3) p_vta,"
                    //+ " round((fd.tcd_imptot)*-1,3) imp_neto,"
                    + " round((fd.tcd_vventafac)*-1,3) val_vta, "
                    + " round((fd.tcd_igvfac)*-1,3) igv,"
                    + " round((fd.tcd_subtotalfac)*-1,3) subtotal"
                    + " from codijisa.v_listaordcompdet t,v_listafacturaproveedordet fd,v_listafacturaproveedorcab fc  "
                    + " where fc.tc_oc=t.oc_nropedkey "
                    + " and fd.tc_key=fc.tc_key "
                    + " and t.pro_id=fd.pro_id "
                    + " and t.emp_id=fc.emp_id "
                    + " and t.suc_id=fc.suc_id "
                    + " and fc.emp_id=fd.emp_id "
                    + " and fc.suc_id=fd.suc_id "
                    + " and t.oc_nropedkey=" + oc_nropedkey + ""
                    + " ) c group by c.pro_id "
                    + " ) z, v_listaordcompdet t,tproductos pr "
                    + " where  "
                    + " t.emp_id=" + emp_id + " and z.pro_id = pr.pro_id and t.pro_id=z.pro_id"
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " and t.oc_ind='" + oc_ind + "' and t.ocd_est=1"
                    + " and pr.pro_id=t.pro_id and t.pro_id=" + pro_id;
        }
        objOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                /**
                 * ********Datos especiales para factura******************
                 */
                objOrdCompDet.setCant_fac(rs.getDouble("cant_fac"));
                //objOrdCompDet.setPv_oc(rs.getDouble("pv_oc"));
                //objOrdCompDet.setP_vta(rs.getDouble("p_vta"));
                //objOrdCompDet.setImp_neto(rs.getDouble("imp_neto"));
                objOrdCompDet.setVal_vta(rs.getDouble("val_vta"));
                objOrdCompDet.setIgv(rs.getDouble("igv"));
                objOrdCompDet.setSubtotal(rs.getDouble("subtotal"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                objOrdCompDet.setPro_unimas(rs.getString("pro_unimas"));
                objOrdCompDet.setOcd_canprov(rs.getLong("can_prov"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setCant_ped(rs.getDouble("cant_ped"));
                objOrdCompDet.setCant_fac(rs.getDouble("cant_fac"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                //objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setPro_svol(rs.getDouble("pro_vol"));
                objOrdCompDet.setPro_sunivol(rs.getString("uni_volumen"));
                objOrdCompDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setPro_unipesototal(rs.getString("unipesototal"));
                objOrdCompDet.setPro_pesototal(rs.getDouble("pesototal"));
                objOrdCompDet.setPro_svoltotal(rs.getDouble("svoltotal"));
                objOrdCompDet.setPro_sunivoltotal(rs.getString("uni_volumen"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                //objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                //objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                //objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
                objlstOrdCompDet.add(objOrdCompDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado registro(s)");
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
        return objOrdCompDet;
    }

    public OrdCompCab ordCompCabxCodigo(int emp_id, int suc_id, long oc_nropedkey, String oc_ind) throws SQLException {
        String SQL_ORDCOMPCAB = "select * from v_listaordcompcab t where t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.oc_nropedkey=" + oc_nropedkey
                + " and t.oc_ind=" + oc_ind + " and t.oc_est=1";
        objOrdCompCab = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ORDCOMPCAB);
            while (rs.next()) {
                objOrdCompCab = new OrdCompCab();
                objOrdCompCab.setEmp_id(rs.getInt("emp_id"));
                objOrdCompCab.setSuc_id(rs.getInt("suc_id"));
                objOrdCompCab.setOc_ind(rs.getString("oc_ind"));
                objOrdCompCab.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompCab.setOc_nropedid(rs.getString("oc_nropedid"));
                objOrdCompCab.setOc_provid(rs.getLong("oc_provid"));
                objOrdCompCab.setProv_id(rs.getString("prov_id"));
                objOrdCompCab.setOc_provrazsoc(rs.getString("oc_provrazsoc"));
                objOrdCompCab.setOc_moneda(rs.getInt("oc_moneda"));
                objOrdCompCab.setOc_mondes(rs.getString("oc_mondes"));
                objOrdCompCab.setOc_tcambio(rs.getDouble("oc_tcambio"));
                objOrdCompCab.setOc_est(rs.getInt("oc_est"));
                objOrdCompCab.setOc_situacion(rs.getInt("oc_situacion"));
                objOrdCompCab.setOc_usuapro(rs.getString("oc_usuapro"));
                objOrdCompCab.setOc_fecapro(rs.getDate("oc_fecapro"));
                objOrdCompCab.setOc_pcapro(rs.getString("oc_pcapro"));
                objOrdCompCab.setOc_almori(rs.getInt("oc_almori"));
                objOrdCompCab.setOc_almorides(rs.getString("oc_almorides"));
                objOrdCompCab.setOc_almdes(rs.getInt("oc_almdes"));
                objOrdCompCab.setOc_almdesdes(rs.getString("oc_almdesdes"));
                objOrdCompCab.setOc_vafecto(rs.getDouble("oc_vafecto"));
                objOrdCompCab.setOc_exonerado(rs.getDouble("oc_exonerado"));
                objOrdCompCab.setOc_vimpt(rs.getDouble("oc_vimpt"));
                objOrdCompCab.setOc_vtotal(rs.getDouble("oc_vtotal"));
                objOrdCompCab.setOc_fecemi(rs.getDate("oc_fecemi"));
                objOrdCompCab.setOc_fecrec(rs.getDate("oc_fecrec"));
                objOrdCompCab.setOc_feccad(rs.getDate("oc_feccad"));
                objOrdCompCab.setOc_periodo(rs.getInt("oc_periodo"));
                objOrdCompCab.setOc_glosa(rs.getString("oc_glosa"));
                objOrdCompCab.setOc_conid(rs.getInt("oc_conid"));
                objOrdCompCab.setOc_condes(rs.getString("oc_condes"));
                objOrdCompCab.setOc_lpcid(rs.getInt("oc_lpcid"));
                objOrdCompCab.setOc_lpcdes(rs.getString("oc_lpcdes"));
                objOrdCompCab.setOc_vdesc(rs.getDouble("oc_vdesc"));
                objOrdCompCab.setPedcom_key(rs.getLong("pedcom_key"));
                objOrdCompCab.setPedcom_id(rs.getString("pedcom_id"));
                objOrdCompCab.setOc_usuadd(rs.getString("oc_usuadd"));
                objOrdCompCab.setOc_fecadd(rs.getTimestamp("oc_fecadd"));
                objOrdCompCab.setOc_pcadd(rs.getString("oc_pcadd"));
                objOrdCompCab.setOc_usumod(rs.getString("oc_usumod"));
                objOrdCompCab.setOc_fecmod(rs.getTimestamp("oc_fecmod"));
                objOrdCompCab.setOc_pcmod(rs.getString("oc_pcmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado el registro con codigo " + oc_nropedkey);
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
        return objOrdCompCab;
    }

    public OrdCompDet getPrecioxOC(int emp_id, int suc_id, String pro_id, long oc) throws SQLException {
        String SQL_PRECIOS = "select * from v_listaordcompdet t where t.oc_nropedkey=" + oc + " and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " "
                + " and t.pro_id=" + pro_id + "";
        objOrdCompDet = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                objOrdCompDet.setPro_presmincom(rs.getString("pro_presmincom"));
                objOrdCompDet.setPro_unipeso(rs.getString("pro_unipeso"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
            }
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
        return objOrdCompDet;
    }

    public OrdCompDet OrdCompDetNotaESxProd(int emp_id, int suc_id, long oc_nropedkey, String oc_ind, String pro_id, String estado, String notaes_cab) throws SQLException {
        String SQL_LISTAORDCOMPDET;
        if ("E".equals(estado)) {
            SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                    + " t.pro_desdes,t.pro_des,j.nesdet_ubides,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                    + " t.ocd_precio,z.ocd_cantped,t.ocd_cantate*t.pro_presminven ocd_cantate,z.ocd_vafecto,z.ocd_exonerado,"
                    + " z.ocd_vimpto,t.ocd_pimpto,t.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                    + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                    + " from ( "
                    + " select c.pro_id,sum(c.cant) ocd_cantped,sum(c.valafe) ocd_vafecto,sum(c.valina) ocd_exonerado,"
                    + " sum(c.vimpto) ocd_vimpto, sum(c.pvta) ocd_vtotal, sum(c.peso) pro_peso from ("
                    + " select t.pro_id,t.ocd_cantped*t.pro_presminven cant,t.ocd_vafecto valafe, t.ocd_exonerado valina,"
                    + " t.ocd_vimpto vimpto, t.ocd_vtotal pvta, t.pro_peso peso from codijisa.v_listaordcompdet t"
                    + " where t.emp_id=" + emp_id + " "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " "
                    + " union "
                    + " select y.pro_id,nvl(y.nesdet_cant,0)*-1 cant, nvl(y.nesdet_valafe,0)*-1 valafe, nvl(y.nesdet_valina,0) valina, "
                    + " nvl(y.nesdet_vimpto,0) vimpto,nvl(y.nesdet_pvta,0) pvta, nvl(y.nesdet_peso,0) peso  from codijisa.v_listanotaescab x,codijisa.v_listanotaesdet y "
                    + " where x.emp_id=y.emp_id "
                    + " and x.suc_id=y.suc_id "
                    + " and x.nescab_id=y.nescab_id "
                    + " and y.nesdet_est=1 "
                    + " and x.nescab_est=1 "
                    + " and x.emp_id=" + emp_id + " "
                    + " and x.suc_id=" + suc_id + " "
                    + " and x.nescab_ocnropedkey=" + oc_nropedkey + " and x.nescab_id not in ('" + notaes_cab + "')"
                    + " ) c group by c.pro_id "
                    + " ) z, v_listaordcompdet t,tproductos pr,v_listanotaesdet j,tnotaes_cab x "
                    + " where  "
                    + " t.emp_id=" + emp_id + " and z.pro_id = t.pro_id "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " and pr.pro_id=t.pro_id and t.pro_id= '" + pro_id + "' and t.oc_ind='" + oc_ind + "' and t.ocd_est=1"
                    + " and x.nescab_ocnropedkey=t.oc_nropedkey and x.nescab_id=j.nescab_id and j.pro_id=t.pro_id";
        } else {
            SQL_LISTAORDCOMPDET = "select t.oc_nropedkey,t.emp_id,t.suc_id,t.oc_ind,t.ocd_item,t.ocd_est,t.pro_id,t.pro_codori,"
                    + " t.pro_desdes,t.pro_des,t.ocd_idubi,pr.pro_presminven,t.pro_unipeso,t.pro_conv,t.pro_provid,t.prov_razsoc,t.oc_almdes,"
                    + " t.ocd_precio,z.ocd_cantped,t.ocd_cantate*t.pro_presminven ocd_cantate,z.ocd_vafecto,z.ocd_exonerado,"
                    + " z.ocd_vimpto,t.ocd_pimpto,t.ocd_vtotal,t.ocd_glosa,t.pro_peso,t.ocd_periodo,t.ocd_vdesc,t.ocd_pdesc,t.ocd_usuadd,t.ocd_fecadd,t.ocd_pcadd,"
                    + " t.ocd_usumod,t.ocd_fecmod,t.ocd_pcmod,t.pro_unimas,t.can_prov,t.pro_unimanven,t.pro_vol,t.uni_volumen,t.pesototal,t.unipesototal,t.svoltotal "
                    + " from ( "
                    + " select c.pro_id,sum(c.cant) ocd_cantped,sum(c.valafe) ocd_vafecto,sum(c.valina) ocd_exonerado,"
                    + " sum(c.vimpto) ocd_vimpto, sum(c.pvta) ocd_vtotal, sum(c.peso) pro_peso from ("
                    + " select t.pro_id,t.ocd_cantped*t.pro_presminven cant,t.ocd_vafecto valafe, t.ocd_exonerado valina,"
                    + " t.ocd_vimpto vimpto, t.ocd_vtotal pvta, t.pro_peso peso from codijisa.v_listaordcompdet t"
                    + " where t.emp_id=" + emp_id + " "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " "
                    + " union "
                    + " select y.pro_id,nvl(y.nesdet_cant,0)*-1 cant, nvl(y.nesdet_valafe,0)*-1 valafe, nvl(y.nesdet_valina,0) valina, "
                    + " nvl(y.nesdet_vimpto,0) vimpto,nvl(y.nesdet_pvta,0) pvta, nvl(y.nesdet_peso,0) peso  from codijisa.v_listanotaescab x,codijisa.v_listanotaesdet y "
                    + " where x.emp_id=y.emp_id "
                    + " and x.suc_id=y.suc_id "
                    + " and x.nescab_id=y.nescab_id "
                    + " and y.nesdet_est=1 "
                    + " and x.nescab_est=1 "
                    + " and x.emp_id=" + emp_id + " "
                    + " and x.suc_id=" + suc_id + " "
                    + " and x.nescab_ocnropedkey=" + oc_nropedkey + ""
                    + " ) c group by c.pro_id "
                    + " ) z, v_listaordcompdet t,tproductos pr "
                    + " where  "
                    + " t.emp_id=" + emp_id + " and z.pro_id = t.pro_id "
                    + " and t.suc_id=" + suc_id + " "
                    + " and t.oc_nropedkey=" + oc_nropedkey + " and pr.pro_id=t.pro_id and t.pro_id= '" + pro_id + "' and t.oc_ind='" + oc_ind + "' and t.ocd_est=1";
        }
        objOrdCompDet = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAORDCOMPDET);
            while (rs.next()) {
                objOrdCompDet = new OrdCompDet();
                objOrdCompDet.setOc_nropedkey(rs.getLong("oc_nropedkey"));
                objOrdCompDet.setEmp_id(rs.getInt("emp_id"));
                objOrdCompDet.setSuc_id(rs.getInt("suc_id"));
                objOrdCompDet.setOc_ind(rs.getString("oc_ind"));
                objOrdCompDet.setOcd_item(rs.getInt("ocd_item"));
                objOrdCompDet.setOcd_est(rs.getInt("ocd_est"));
                objOrdCompDet.setPro_id(rs.getString("pro_id"));
                objOrdCompDet.setPro_des(rs.getString("pro_des"));
                objOrdCompDet.setPro_desdes(rs.getString("pro_desdes"));
                /**
                 * ********Datos especiales para nota E/S******************
                 */
                objOrdCompDet.setPro_codori(rs.getString("pro_codori"));
                if ("E".equals(estado)) {
                    objOrdCompDet.setPro_ubi(rs.getString("nesdet_ubides"));
                } else {
                    objOrdCompDet.setPro_ubi(rs.getString("ocd_idubi"));
                }
                objOrdCompDet.setPro_presminven(rs.getString("pro_presminven"));
                objOrdCompDet.setPro_unipeso(rs.getString("pro_unipeso"));
                objOrdCompDet.setPro_conv(rs.getString("pro_conv"));
                objOrdCompDet.setPro_provid(rs.getString("pro_provid"));
                objOrdCompDet.setPro_provrazsoc(rs.getString("prov_razsoc"));
                objOrdCompDet.setPro_almdes(rs.getString("oc_almdes"));
                objOrdCompDet.setPro_unimas(rs.getString("pro_unimas"));
                objOrdCompDet.setOcd_canprov(rs.getLong("can_prov"));
                /**
                 * ****************************************************
                 */
                objOrdCompDet.setOcd_precio(rs.getDouble("ocd_precio"));
                objOrdCompDet.setOcd_cantped(rs.getDouble("ocd_cantped"));
                objOrdCompDet.setOcd_cantate(rs.getDouble("ocd_cantate"));
                objOrdCompDet.setOcd_vafecto(rs.getDouble("ocd_vafecto"));
                objOrdCompDet.setPro_svol(rs.getDouble("pro_vol"));
                objOrdCompDet.setPro_sunivol(rs.getString("uni_volumen"));
                objOrdCompDet.setPro_unimanven(rs.getString("pro_unimanven"));
                objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setPro_unipesototal(rs.getString("unipesototal"));
                objOrdCompDet.setPro_pesototal(rs.getDouble("pesototal"));
                objOrdCompDet.setPro_svoltotal(rs.getDouble("svoltotal"));
                objOrdCompDet.setPro_sunivoltotal(rs.getString("uni_volumen"));
                //objOrdCompDet.setOcd_peso(rs.getDouble("pro_peso"));
                objOrdCompDet.setOcd_exonerado(rs.getDouble("ocd_exonerado"));
                objOrdCompDet.setOcd_vimpto(rs.getDouble("ocd_vimpto"));
                objOrdCompDet.setOcd_pimpto(rs.getDouble("ocd_pimpto"));
                objOrdCompDet.setOcd_vtotal(rs.getDouble("ocd_vtotal"));
                objOrdCompDet.setOcd_glosa(rs.getString("ocd_glosa"));
                objOrdCompDet.setOcd_periodo(rs.getInt("ocd_periodo"));
                objOrdCompDet.setOcd_vdesc(rs.getDouble("ocd_vdesc"));
                objOrdCompDet.setOcd_pdesc(rs.getDouble("ocd_pdesc"));
                objOrdCompDet.setOcd_usuadd(rs.getString("ocd_usuadd"));
                objOrdCompDet.setOcd_fecadd(rs.getTimestamp("ocd_fecadd"));
                objOrdCompDet.setOcd_pcadd(rs.getString("ocd_pcadd"));
                objOrdCompDet.setOcd_usumod(rs.getString("ocd_usumod"));
                objOrdCompDet.setOcd_fecmod(rs.getTimestamp("ocd_fecmod"));
                objOrdCompDet.setOcd_pcmod(rs.getString("ocd_pcmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado registro(s)");
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
        return objOrdCompDet;
    }
}
