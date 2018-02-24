package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.FacturaLin;
import org.me.gj.model.logistica.informes.FacturaProd;
import org.me.gj.model.logistica.informes.FacturaProv;
import org.me.gj.model.logistica.informes.FacturaSubLin;
import org.me.gj.model.logistica.procesos.ComprasCab;
import org.me.gj.model.logistica.procesos.ComprasDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoFacPro {

    //Instancias de Objetos
    ListModelList<ComprasDet> objlstComprasDet = new ListModelList<ComprasDet>();
    ListModelList<ComprasCab> objlstComprasCab = new ListModelList<ComprasCab>();
    ComprasDet objComprasDet;
    ComprasCab objComprasCab;
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
    private static final Logger LOGGER = Logger.getLogger(DaoFacPro.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarCabecera(ComprasCab objComprasCab) throws SQLException, ParseException {
        objParametrosSalida = new ParametrosSalida();
        String SQL_INSERTAR_CABECERA = "{call pack_tordcomp.p_insertarCabeceraFProveedor(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CABECERA);
            cst.clearParameters();
            cst.setInt(1, objComprasCab.getEmp_id());
            cst.setInt(2, objComprasCab.getSuc_id());
            //cst.setString(3, objComprasCab.getTc_id());
            cst.setString(3, objComprasCab.getTc_tipdoc());
            cst.setString(4, objComprasCab.getTc_serie());
            cst.setString(5, objComprasCab.getTc_nrodoc());
            cst.setString(6, objComprasCab.getTc_provid());
            if (objComprasCab.getTc_fecemi() == null) {
                cst.setDate(7, null);
            } else {
                cst.setDate(7, new java.sql.Date(objComprasCab.getTc_fecemi().getTime()));
            }
            if (objComprasCab.getTc_fecven() == null) {
                cst.setDate(8, null);
            } else {
                cst.setDate(8, new java.sql.Date(objComprasCab.getTc_fecven().getTime()));
            }
            cst.setLong(9, objComprasCab.getTc_nrpedkey());
            cst.setInt(10, objComprasCab.getTc_moneda());
            cst.setDouble(11, objComprasCab.getTc_tcambio());
            cst.setInt(12, objComprasCab.getTc_conid());
            //cst.setDouble(13, objComprasCab.getTc_dscgral());
            //cst.setDouble(14, objComprasCab.getTc_dscfin());
            //cst.setDouble(15, objComprasCab.getTc_tdsctos());
            cst.setDouble(13, objComprasCab.getTc_ocvtotal());
            cst.setDouble(14, objComprasCab.getTc_valventa());
            cst.setDouble(15, objComprasCab.getTc_vimpt());
            cst.setDouble(16, objComprasCab.getTc_vtotal());
            cst.setString(17, objComprasCab.getTc_glosa());
            cst.setInt(18, objComprasCab.getTc_efectivo());
            cst.setString(19, objComprasCab.getTc_usuadd());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.registerOutParameter(22, java.sql.Types.NUMERIC);
            cst.registerOutParameter(23, java.sql.Types.VARCHAR);
            cst.execute();
            objParametrosSalida.setFlagIndicador(cst.getInt(20));
            objParametrosSalida.setMsgValidacion(cst.getString(21));
            objParametrosSalida.setCodigoRegistro(cst.getString(23));
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + cst.getString(23));
        } catch (SQLException e) {
            objParametrosSalida.setMsgValidacion("Ocurrio una Excepcion debido al Error " + e.toString());
            objParametrosSalida.setFlagIndicador(1);
            objParametrosSalida.setCodigoRegistro("0");
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            objParametrosSalida.setMsgValidacion("Ocurrio una Excepcion debido al Error " + e.toString());
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

    public ParametrosSalida modificarCabecera(ComprasCab objComprasCab) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_FACTURAPROVEEDOR = "{call pack_tordcomp.p_actualizarCabeceraFProveedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_FACTURAPROVEEDOR);
            cst.clearParameters();
            cst.setInt(1, objComprasCab.getEmp_id());
            cst.setInt(2, objComprasCab.getSuc_id());
            cst.setLong(3, objComprasCab.getTc_key());
            cst.setString(4, objComprasCab.getTc_tipdoc());
            cst.setString(5, objComprasCab.getTc_serie());
            cst.setString(6, objComprasCab.getTc_nrodoc());
            cst.setDate(7, new java.sql.Date(objComprasCab.getTc_fecemi().getTime()));
            cst.setDate(8, new java.sql.Date(objComprasCab.getTc_fecven().getTime()));
            //cst.setDouble(9, objComprasCab.getTc_dscgral());
            //cst.setDouble(10, objComprasCab.getTc_dscfin());
            //cst.setDouble(11, objComprasCab.getTc_tdsctos());
            cst.setDouble(9, objComprasCab.getTc_valventa());
            cst.setDouble(10, objComprasCab.getTc_vimpt());
            cst.setDouble(11, objComprasCab.getTc_vtotal());
            cst.setString(12, objComprasCab.getTc_glosa());
            cst.setInt(13, objComprasCab.getTc_efectivo());
            cst.setString(14, objComprasCab.getTc_usumod());
            cst.registerOutParameter(15, java.sql.Types.NUMERIC);
            cst.registerOutParameter(16, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(16);
            i_flagErrorBD = cst.getInt(15);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico una Orden de Compra con codigo " + objComprasCab.getTc_id());
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

    public ParametrosSalida eliminarCabecera(ComprasCab objComprasCab) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_FACTURAPROVEEDOR = "{call pack_tordcomp.p_eliminarCabeceraFProveedor(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_FACTURAPROVEEDOR);
            cst.clearParameters();
            cst.setInt(1, objComprasCab.getEmp_id());
            cst.setInt(2, objComprasCab.getSuc_id());
            cst.setLong(3, objComprasCab.getTc_key());
            cst.setString(4, objComprasCab.getTc_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino la Orden de Compra con Codigo " + objComprasCab.getTc_id());
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

    public ParametrosSalida insertarDetalle(ComprasDet objComprasDet) throws SQLException, ParseException {
        String SQL_INSERTAR_DETALLE = "{call pack_tordcomp.p_insertarDetalleFProveedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_DETALLE);
            cst.clearParameters();
            cst.setLong(1, objComprasDet.getTc_key());
            cst.setInt(2, objComprasDet.getEmp_id());
            cst.setInt(3, objComprasDet.getSuc_id());
            cst.setString(4, objComprasDet.getTcd_codori());
            cst.setString(5, objComprasDet.getPro_id());
            cst.setDouble(6, objComprasDet.getTcd_cantped());
            cst.setDouble(7, objComprasDet.getTcd_cantfac());
            cst.setDouble(8, objComprasDet.getTcd_cantrec());
            //cst.setDouble(10, objComprasDet.getTcd_impnetoc());
            //cst.setDouble(11, objComprasDet.getTcd_impnet());
            //cst.setDouble(12, objComprasDet.getTcd_mdscgral());
            cst.setDouble(9, objComprasDet.getTcd_dscxart());
            cst.setDouble(10, objComprasDet.getTcd_vdscxart());
            cst.setDouble(11, objComprasDet.getTcd_precioped());
            cst.setDouble(12, objComprasDet.getTcd_preciofac());
            //cst.setDouble(15, objComprasDet.getTcd_imptot());
            cst.setDouble(13, objComprasDet.getTcd_vventaped());
            cst.setDouble(14, objComprasDet.getTcd_vventafac());
            cst.setDouble(15, objComprasDet.getTcd_igvped());
            cst.setDouble(16, objComprasDet.getTcd_igvfac());
            cst.setDouble(17, objComprasDet.getTcd_prevenped());
            cst.setDouble(18, objComprasDet.getTcd_prevenfac());
            cst.setString(19, objComprasDet.getTcd_usuadd());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + cst.getString(21));
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

    public ParametrosSalida modificarDetalle(ComprasDet objComprasDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_DETALLE = "{call pack_tordcomp.p_actualizarDetalleFProveedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_DETALLE);
            cst.clearParameters();
            cst.setLong(1, objComprasDet.getTc_key());
            cst.setInt(2, objComprasDet.getEmp_id());
            cst.setInt(3, objComprasDet.getSuc_id());
            cst.setLong(4, objComprasDet.getTcd_item());
            cst.setString(5, objComprasDet.getTcd_codori());
            cst.setString(6, objComprasDet.getPro_id());
            cst.setDouble(7, objComprasDet.getTcd_cantped());
            cst.setDouble(8, objComprasDet.getTcd_cantfac());
            cst.setDouble(9, objComprasDet.getTcd_cantrec());
            //cst.setDouble(11, objComprasDet.getTcd_impnet());
            //cst.setDouble(12, objComprasDet.getTcd_mdscgral());
            cst.setDouble(10, objComprasDet.getTcd_dscxart());
            cst.setDouble(11, objComprasDet.getTcd_vdscxart());
            cst.setDouble(12, objComprasDet.getTcd_precioped());
            cst.setDouble(13, objComprasDet.getTcd_preciofac());
            //cst.setDouble(15, objComprasDet.getTcd_imptot());
            cst.setDouble(14, objComprasDet.getTcd_vventaped());
            cst.setDouble(15, objComprasDet.getTcd_vventafac());
            cst.setDouble(16, objComprasDet.getTcd_igvped());
            cst.setDouble(17, objComprasDet.getTcd_igvfac());
            cst.setDouble(18, objComprasDet.getTcd_prevenped());
            cst.setDouble(19, objComprasDet.getTcd_prevenfac());
            cst.setString(20, objComprasDet.getTcd_usumod());
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(22);
            i_flagErrorBD = cst.getInt(21);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Modifico un producto de la Factura Proveedor " + objComprasDet.getTc_key() + " con Codigo " + objComprasDet.getPro_id());
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

    public ParametrosSalida eliminarDetalle(ComprasDet objComprasDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_DETALLE = "{call pack_tordcomp.p_eliminarDetalleFProveedor(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_DETALLE);
            cst.clearParameters();
            cst.setInt(1, objComprasDet.getEmp_id());
            cst.setInt(2, objComprasDet.getSuc_id());
            cst.setLong(3, objComprasDet.getTc_key());
            cst.setLong(4, objComprasDet.getTcd_item());
            cst.setString(5, objComprasDet.getTcd_usumod());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino Factura Proveedor Detalle con Codigo " + objComprasDet.getTcd_item());
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
    public ListModelList<ComprasCab> BusquedaFacturaProveedor(String fechai, String fechaf, String proveedor, String factura) throws SQLException {
        String SQL_LISTA_FACTURAPROVEEDOR = "";
        //fecha inicial + fecha final + proveedor
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        }
        if (!fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//fecha inicial + fecha final 
        else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (!fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi between to_date('" + fechai + "','dd/mm/yyyy') and to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//fecha inicial + proveedor 
        else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//fecha final + proveedor 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && !proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_provid = '" + proveedor + "' and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//proveedor  
        else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_provid = '" + proveedor + "' and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (fechai.isEmpty() && fechaf.isEmpty() && !proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_provid = '" + proveedor + "' and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//ninguno 
        else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//fecha inicial 
        else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (!fechai.isEmpty() && fechaf.isEmpty() && proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi >= to_date('" + fechai + "','dd/mm/yyyy') and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }//fecha final 
        else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi <= to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_est ='1'  order by t.tc_key,tc_fecemi";
        } else if (fechai.isEmpty() && !fechaf.isEmpty() && proveedor.isEmpty() && !factura.isEmpty()) {
            SQL_LISTA_FACTURAPROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tc_fecemi <= to_date('" + fechaf + "','dd/mm/yyyy') and t.tc_est ='1' and t.tc_id='" + factura + "' order by t.tc_key,tc_fecemi";
        }
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_FACTURAPROVEEDOR);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_efectivo(rs.getInt("tc_efectivo"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));
                objlstComprasCab.add(objComprasCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstComprasCab.getSize() + " registro(s)");
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
        return objlstComprasCab;
    }

    public ListModelList<ComprasCab> listaFacturaProveedorCab() throws SQLException {
        String SQL_FACTURA_PROVEEDOR = "select * from codijisa.v_listafacturaproveedorcab t where t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                + "and to_char(t.tc_fecemi,'dd/mm/yyyy')=to_char(sysdate,'dd/mm/yyyy') and t.tc_est <> 0 order by t.tc_key";
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACTURA_PROVEEDOR);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_efectivo(rs.getInt("tc_efectivo"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));
                objlstComprasCab.add(objComprasCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstComprasCab.getSize() + " registro(s)");
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
        return objlstComprasCab;
    }

    public ListModelList<ComprasDet> listaFacturaProveedorDet(long tc_key) throws SQLException {
        String SQL_FACTURAPROVEEDORDET = "select * from codijisa.v_listafacturaproveedordet t "
                + "where t.tcd_est <> 0 and t.tc_key = '" + tc_key + "' "
                + "and t.emp_id = '" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id = '" + objUsuCredential.getCodsuc() + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACTURAPROVEEDORDET);
            objlstComprasDet = new ListModelList<ComprasDet>();
            while (rs.next()) {
                objComprasDet = new ComprasDet();
                objComprasDet.setTc_key(rs.getInt("tc_key"));
                objComprasDet.setEmp_id(rs.getInt("emp_id"));
                objComprasDet.setSuc_id(rs.getInt("suc_id"));
                objComprasDet.setTcd_item(rs.getLong("tcd_item"));
                objComprasDet.setTcd_est(rs.getInt("tcd_est"));
                objComprasDet.setTcd_codori(rs.getString("tcd_codori"));
                objComprasDet.setPro_id(rs.getString("pro_id"));
                objComprasDet.setPro_des(rs.getString("pro_des"));
                objComprasDet.setPro_desdes(rs.getString("pro_desdes"));
                objComprasDet.setTcd_precioped(rs.getDouble("tcd_preuniped"));
                objComprasDet.setTcd_preciofac(rs.getDouble("tcd_preunifac"));
                objComprasDet.setTcd_cantped(rs.getDouble("tcd_cantped"));
                objComprasDet.setTcd_cantfac(rs.getInt("tcd_cantfac"));
                objComprasDet.setTcd_cantrec(rs.getInt("tcd_cantrec"));
                //objComprasDet.setTcd_impnetoc(rs.getDouble("tcd_ocprodtot"));
                //objComprasDet.setTcd_impnet(rs.getDouble("tcd_impnet"));
                //objComprasDet.setTcd_mdscgral(rs.getDouble("tcd_dscgral"));
                objComprasDet.setTcd_dscxart(rs.getDouble("tcd_dscxart"));
                objComprasDet.setTcd_vdscxart(rs.getDouble("tcd_vdscxart"));
                //objComprasDet.setTcd_imptot(rs.getDouble("tcd_imptot"));
                objComprasDet.setTcd_vventaped(rs.getDouble("tcd_vventaped"));
                objComprasDet.setTcd_vventafac(rs.getDouble("tcd_vventafac"));
                objComprasDet.setTcd_igvped(rs.getDouble("tcd_igvped"));
                objComprasDet.setTcd_igvfac(rs.getDouble("tcd_igvfac"));
                objComprasDet.setTcd_prevenped(rs.getDouble("tcd_subtotalped"));
                objComprasDet.setTcd_prevenfac(rs.getDouble("tcd_subtotalfac"));
                objComprasDet.setTcd_usuadd(rs.getString("tcd_usuadd"));
                objComprasDet.setTcd_fecadd(rs.getTimestamp("tcd_fecadd"));
                objComprasDet.setTcd_usumod(rs.getString("tcd_usumod"));
                objComprasDet.setTcd_fecmod(rs.getTimestamp("tcd_fecmod"));
                objlstComprasDet.add(objComprasDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstComprasDet.getSize() + " registro(s)");
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
        return objlstComprasDet;
    }

    public ListModelList<FacturaProv> listaTotalxProveedor(String tc_fecini, String tc_fecfinal, String tc_periodo, String tc_provid) throws SQLException {
        ListModelList<FacturaProv> objlstFacturaProv = new ListModelList<FacturaProv>();
        String validaFecha, SQL_TOTALPROVEEDOR;
        if (tc_fecini.isEmpty() && tc_fecfinal.isEmpty()) {
            validaFecha = " ";
        } else if (tc_fecini.isEmpty() && !tc_fecfinal.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + tc_fecfinal + "','dd/mm/yyyy') ";
        } else if (!tc_fecini.isEmpty() && tc_fecfinal.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('" + tc_fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and t.tc_fecemi between to_date('" + tc_fecini + "','dd/mm/yyyy') and to_date('" + tc_fecfinal + "','dd/mm/yyyy') ";
        }
        SQL_TOTALPROVEEDOR = "select t.emp_id,t.suc_id,t.tc_provid,t.prov_razsoc, "
                + "count(distinct t.tc_key) cant, "
                // + "sum(t.tc_tdsctos) vdscgen,  "
                + "sum(t.tc_valventa) vneto, "
                + "sum(t.tc_vimpt) vimpt,  "
                + "sum(t.tc_vtotal) vtotal "
                + "from v_listafacturaproveedorcab t  "
                + "where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.tc_est=1 "
                + "and t.tc_provid like '" + tc_provid + "' and to_char(t.tc_fecemi,'yyyymm') like '" + tc_periodo + "' " + validaFecha
                + "group by t.emp_id,t.suc_id,t.tc_provid,t.prov_razsoc";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPROVEEDOR);
            FacturaProv objFacturaProv;
            while (rs.next()) {
                objFacturaProv = new FacturaProv();
                objFacturaProv.setEmp_id(rs.getInt("emp_id"));
                objFacturaProv.setSuc_id(rs.getInt("suc_id"));
                objFacturaProv.setTc_provid(rs.getString("tc_provid"));
                objFacturaProv.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objFacturaProv.setCant(rs.getLong("cant"));
                objFacturaProv.setFecemi(tc_fecini);
                objFacturaProv.setFecfin(tc_fecfinal);
                objFacturaProv.setPeriodo(tc_periodo);
                //objFacturaProv.setVbruto(rs.getDouble("vbruto"));
                //  objFacturaProv.setVdscgen(rs.getDouble("vdscgen"));
                objFacturaProv.setVafecto(rs.getDouble("vneto"));
                objFacturaProv.setVimpto(rs.getDouble("vimpt"));
                //objFacturaProv.setVdesc(rs.getDouble("vdescacor"));
                objFacturaProv.setVtotal(rs.getDouble("vtotal"));
                objlstFacturaProv.add(objFacturaProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstFacturaProv.size() + " Registro(s)");
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
        return objlstFacturaProv;
    }

    public ListModelList<FacturaProd> listaTotalxProducto(String prov_id, String pro_id, String periodo, String fecini, String fecfin) throws SQLException {
        String validaFecha, SQL_TOTALPRODUCTO;
        ListModelList<FacturaProd> objlstFacturaProd = new ListModelList<FacturaProd>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and t.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALPRODUCTO = "select p.emp_id,p.suc_id,p.pro_id,p.pro_des, "
                + "count(distinct p.tc_key) cant, "
                + "sum(p.tcd_vdscxart) vdesc, "
                + "sum(p.tcd_vventafac) vneto,  "
                + "sum(p.tcd_igvfac) vimpt, "
                + "sum(p.tcd_subtotalfac) vtotal "
                + "from v_listafacturaproveedorcab t,v_listafacturaproveedordet p "
                + "where t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                + "and t.tc_key=p.tc_key and t.tc_est=1 and p.tcd_est=1 and t.tc_provid like '" + prov_id + "' "
                + "and p.pro_id like '" + pro_id + "' "
                + "and to_char(t.tc_fecemi,'yyyymm') like '" + periodo + "' " + validaFecha
                + "group by p.emp_id,p.suc_id,p.pro_id,p.pro_des";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALPRODUCTO);
            FacturaProd objFacturaProd;
            while (rs.next()) {
                objFacturaProd = new FacturaProd();
                objFacturaProd.setEmp_id(rs.getInt("emp_id"));
                objFacturaProd.setSuc_id(rs.getInt("suc_id"));
                //objFacturaProd.setPro_key(rs.getLong("pro_key"));
                objFacturaProd.setPro_id(rs.getString("pro_id"));
                objFacturaProd.setPro_des(rs.getString("pro_des"));
                objFacturaProd.setCant(rs.getLong("cant"));
                objFacturaProd.setFecemi(fecini);
                objFacturaProd.setFecfin(fecfin);
                objFacturaProd.setPeriodo(periodo);
                //objFacturaProd.setVbruto(rs.getDouble("vbruto"));
                objFacturaProd.setVdscgen(rs.getDouble("vdesc"));
                objFacturaProd.setVafecto(rs.getDouble("vneto"));
                objFacturaProd.setVimpto(rs.getDouble("vimpt"));
                //objFacturaProd.setVdesc(rs.getDouble("vdescacor"));
                objFacturaProd.setVtotal(rs.getDouble("vtotal"));
                objlstFacturaProd.add(objFacturaProd);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstFacturaProd.size() + " Registro(s)");
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
        return objlstFacturaProd;
    }

    public ListModelList<FacturaLin> listaTotalxLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_TOTALLINEA;
        ListModelList<FacturaLin> objlstFacturaLin = new ListModelList<FacturaLin>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALLINEA = "select p.emp_id, p.suc_id, t.tab_id lin_key, t.tab_subdir lin_id, t.tab_subdes lin_des, "
                + " count(distinct x.tc_key) cant, "
                + " sum(x.tcd_vdscxart) vdesc, "
                + " sum(x.tcd_vventafac) vneto, "
                + " sum(x.tcd_igvfac) vimpt, "
                + " sum(x.tcd_subtotalfac) vtotal "
                + " from ttabgen t,v_listafacturaproveedorcab p, tcompras_det x "
                + " where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.tc_key=x.tc_key and "
                + " p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and "
                + " to_char(p.tc_fecemi,'yyyymm') like '" + periodo + "' and t.tab_cod=4 and p.tc_est=1 and "
                + " x.tcd_est=1 and to_number(codijisa.pack_tproductos.f_003_descLineaProducto(x.pro_id))= t.tab_id and t.tab_id like '" + lin_key + "' " + validaFecha
                + " group by p.emp_id, p.suc_id, t.tab_id, t.tab_subdir, t.tab_subdes";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALLINEA);
            FacturaLin objFacturaLinea;
            while (rs.next()) {
                objFacturaLinea = new FacturaLin();
                objFacturaLinea.setEmp_id(rs.getInt("emp_id"));
                objFacturaLinea.setSuc_id(rs.getInt("suc_id"));
                objFacturaLinea.setLin_key(rs.getInt("lin_key"));
                objFacturaLinea.setLin_id(rs.getString("lin_id"));
                objFacturaLinea.setLin_des(rs.getString("lin_des"));
                objFacturaLinea.setCant(rs.getLong("cant"));
                objFacturaLinea.setFecemi(fecini);
                objFacturaLinea.setFecfin(fecfin);
                objFacturaLinea.setPeriodo(periodo);
                //objFacturaLinea.setVbruto(rs.getDouble("vbruto"));
                objFacturaLinea.setVdscgen(rs.getDouble("vdesc"));
                objFacturaLinea.setVafecto(rs.getDouble("vneto"));
                objFacturaLinea.setVimpto(rs.getDouble("vimpt"));
                //objFacturaLinea.setVdesc(rs.getDouble("vdescacor"));
                objFacturaLinea.setVtotal(rs.getDouble("vtotal"));
                objlstFacturaLin.add(objFacturaLinea);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstFacturaLin.size() + " Registro(s)");
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
        return objlstFacturaLin;
    }

    public ListModelList<FacturaSubLin> listaTotalxSubLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_TOTALSUBLINEA;
        ListModelList<FacturaSubLin> objlstFacturaSubLin = new ListModelList<FacturaSubLin>();
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
        } else {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
        }
        SQL_TOTALSUBLINEA = "select p.emp_id, p.suc_id, t.slin_codslin slin_key, t.slin_cod slin_id, t.slin_des slin_des, "
                + "count(distinct x.tc_key) cant, "
                + " sum(x.tcd_vdscxart) vdesc, "
                + "sum(x.tcd_vventafac) vneto, "
                + "sum(x.tcd_igvfac) vimpt, "
                + "sum(x.tcd_subtotalfac) vtotal "
                + "from tsublineas t,v_listafacturaproveedorcab p, tcompras_det x "
                + "where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.tc_key=x.tc_key and "
                + "p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and "
                + "to_char(p.tc_fecemi,'yyyymm') like '" + periodo + "' and p.tc_est=1 and "
                + "x.tcd_est=1 and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod and t.slin_cod like '" + lin_key + "' " + validaFecha
                + "group by p.emp_id, p.suc_id, t.slin_codslin, t.slin_cod, t.slin_des "
                + "order by t.slin_codslin";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TOTALSUBLINEA);
            FacturaSubLin objFacturaSubLinea;
            while (rs.next()) {
                objFacturaSubLinea = new FacturaSubLin();
                objFacturaSubLinea.setEmp_id(rs.getInt("emp_id"));
                objFacturaSubLinea.setSuc_id(rs.getInt("suc_id"));
                objFacturaSubLinea.setSlin_key(rs.getInt("slin_key"));
                objFacturaSubLinea.setSlin_id(rs.getString("slin_id"));
                objFacturaSubLinea.setSlin_des(rs.getString("slin_des"));
                objFacturaSubLinea.setCant(rs.getLong("cant"));
                objFacturaSubLinea.setFecemi(fecini);
                objFacturaSubLinea.setFecfin(fecfin);
                objFacturaSubLinea.setPeriodo(periodo);
                //objFacturaSubLinea.setVbruto(rs.getDouble("vbruto"));
                objFacturaSubLinea.setVdscgen(rs.getDouble("vdesc"));
                objFacturaSubLinea.setVafecto(rs.getDouble("vneto"));
                objFacturaSubLinea.setVimpto(rs.getDouble("vimpt"));
                //objFacturaSubLinea.setVdesc(rs.getDouble("vdescacor"));
                objFacturaSubLinea.setVtotal(rs.getDouble("vtotal"));
                objlstFacturaSubLin.add(objFacturaSubLinea);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha buscado " + objlstFacturaSubLin.size() + " Registro(s)");
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
        return objlstFacturaSubLin;
    }

    public ListModelList<ComprasCab> FacProvxProveedor(String tc_fecini, String tc_fecfinal, String tc_periodo, String tc_provid) throws SQLException {
        String validaFecha, SQL_FACPROVXPROVEEDOR, titulo/* = ""*/;
        if (tc_fecini.isEmpty() && tc_fecfinal.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE FACTURA POR PROVEEDOR DESDE 01/01/2000";
        } else if (tc_fecini.isEmpty() && !tc_fecfinal.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + tc_fecfinal + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PROVEEDOR HASTA " + tc_fecfinal;
        } else if (!tc_fecini.isEmpty() && tc_fecfinal.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('" + tc_fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PROVEEDOR DESDE " + tc_fecini;
        } else {
            validaFecha = "and t.tc_fecemi between to_date('" + tc_fecini + "','dd/mm/yyyy') and to_date('" + tc_fecfinal + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PROVEEDOR DESDE " + tc_fecini + " HASTA " + tc_fecfinal;
        }
        SQL_FACPROVXPROVEEDOR = "select * from v_listafacturaproveedorcab t "
                + "where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.tc_est=1 "
                + "and t.tc_provid like '" + tc_provid + "' and to_char(t.tc_fecemi,'yyyymm') like '" + tc_periodo + "' " + validaFecha + " order by t.tc_key";
        P_WHERER = SQL_FACPROVXPROVEEDOR;
        P_TITULO = titulo;
        objComprasCab = null;
        objlstComprasCab = null;
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACPROVXPROVEEDOR);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));
                objlstComprasCab.add(objComprasCab);
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
        return objlstComprasCab;
    }

    public ListModelList<ComprasCab> FacProvxProducto(String pro_id, String periodo, String fecini, String fecfin) throws SQLException {
        String validaFecha, SQL_FACPROVXPRODUCTO, titulo/* = ""*/;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE FACTURA POR PRODUCTO DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PRODUCTO HASTA " + fecfin;
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and t.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PRODUCTO DESDE " + fecini;
        } else {
            validaFecha = "and t.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR PRODUCTO DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_FACPROVXPRODUCTO = "select t.tc_id, t.tc_key, t.tc_fecemi , "
                + " t.tc_fecven, t.emp_id, t.suc_id, t.tc_tipdoc, t.tipo, t.tc_serie,t.tc_nrodoc,t.tc_provid,t.prov_razsoc,t.tc_nrpedkey, "
                + " t.tc_moneda,t.tc_tcambio,t.tc_conid,t.tc_condes,t.tc_dscgral,t.tc_dscfin,t.tc_ocvtotal,t.tc_valventa,t.tc_vimpt,t.tc_glosa,t.tc_est,t.tc_oc,t.tc_est_des, "
                + " t.tc_usuadd,t.tc_fecadd,t.tc_usumod,tc_fecmod, sum(p.tcd_vventafac) tc_valventa, sum(p.tcd_igvfac) tc_vimpt,sum(p.tcd_subtotalfac) tc_vtotal "
                + " from v_listafacturaproveedorcab t, v_listafacturaproveedordet p "
                + " where t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id= " + objUsuCredential.getCodsuc() + " and t.tc_key=p.tc_key and "
                + " t.tc_est=1 and p.tcd_est=1 and p.pro_id like '" + pro_id + "' and to_char(t.tc_fecemi,'yyyymm') like '" + periodo + "' " + validaFecha
                + " group by t.tc_id, t.tc_key, t.tc_fecemi , t.tc_fecven, t.emp_id, t.suc_id, t.tc_tipdoc, t.tipo, t.tc_serie,t.tc_oc,t.tc_est_des, "
                + " t.tc_nrodoc,t.tc_provid,t.prov_razsoc,t.tc_nrpedkey, tc_moneda,tc_tcambio,tc_conid,tc_condes,tc_dscgral,t.tc_dscfin, "
                + " t.tc_ocvtotal,t.tc_valventa, t.tc_vimpt, t.tc_glosa,t.tc_est,t.tc_usuadd,t.tc_fecadd,t.tc_usumod,t.tc_fecmod "
                + " order by t.tc_key ";
        P_WHERER = SQL_FACPROVXPRODUCTO;
        P_TITULO = titulo;
        objComprasCab = null;
        objlstComprasCab = null;
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACPROVXPRODUCTO);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));               
                objlstComprasCab.add(objComprasCab);
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
        return objlstComprasCab;
    }

    public ListModelList<ComprasCab> FacProvxLinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_FACPROVXLINEA, titulo/* = ""*/;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE FACTURA POR LINEA DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR LINEA HASTA " + fecfin;
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR LINEA DESDE " + fecini;
        } else {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR LINEA DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_FACPROVXLINEA = "select distinct p.tc_id, p.tc_key, p.tc_fecemi , "
                + " p.tc_fecven, p.emp_id, p.suc_id, p.tc_tipdoc, p.tipo, p.tc_serie,p.tc_nrodoc,p.tc_provid,p.prov_razsoc,p.tc_nrpedkey, "
                + " p.tc_moneda,p.tc_tcambio,p.tc_conid,p.tc_condes,p.tc_dscgral,p.tc_dscfin,p.tc_ocvtotal,p.tc_valventa ,p.tc_glosa,p.tc_est,p.tc_oc,p.tc_est_des, "
                + " p.tc_usuadd,p.tc_fecadd,p.tc_usumod,p.tc_fecmod, sum(x.tcd_vventafac) tc_valventa, sum(x.tcd_igvfac) tc_vimpt,sum(x.tcd_subtotalfac) tc_vtotal "
                + " from ttabgen t,v_listafacturaproveedorcab p, tcompras_det x "
                + " where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.tc_key=x.tc_key and "
                + " p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and "
                + " to_char(p.tc_fecemi,'yyyymm') like '" + periodo + "' and t.tab_cod=4 and p.tc_est=1 and "
                + " x.tcd_est=1 and to_number(codijisa.pack_tproductos.f_003_descLineaProducto(x.pro_id))= t.tab_id and t.tab_id like '" + lin_key + "' " + validaFecha + " "
                + " group by p.tc_id, p.tc_key, p.tc_fecemi , "
                + " p.tc_fecven, p.emp_id, p.suc_id, p.tc_tipdoc, p.tipo, p.tc_serie,p.tc_nrodoc,p.tc_provid,p.prov_razsoc,p.tc_nrpedkey, "
                + " p.tc_moneda,p.tc_tcambio,p.tc_conid,p.tc_condes,p.tc_dscgral,p.tc_dscfin,p.tc_ocvtotal,p.tc_valventa ,p.tc_glosa,p.tc_est,p.tc_oc,p.tc_est_des, "
                + " p.tc_usuadd,p.tc_fecadd,p.tc_usumod,p.tc_fecmod "
                + " order by p.tc_key";
        P_WHERER = SQL_FACPROVXLINEA;
        P_TITULO = titulo;
        objComprasCab = null;
        objlstComprasCab = null;
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACPROVXLINEA);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));
                objlstComprasCab.add(objComprasCab);
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
        return objlstComprasCab;
    }

    public ListModelList<ComprasCab> FacProvxSublinea(String fecini, String fecfin, String periodo, String lin_key) throws SQLException {
        String validaFecha, SQL_FACPROVXSUBLINEA, titulo/* = ""*/;
        if (fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = " ";
            titulo = "REPORTE DE FACTURA POR SUBLINEA DESDE 01/01/2000";
        } else if (fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('01/01/1999','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR SUBLINEA HASTA " + fecfin;
        } else if (!fecini.isEmpty() && fecfin.isEmpty()) {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + Utilitarios.hoyAsString() + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR SUBLINEA DESDE " + fecini;
        } else {
            validaFecha = "and p.tc_fecemi between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            titulo = "REPORTE DE FACTURA POR SUBLINEA DESDE " + fecini + " HASTA " + fecfin;
        }
        SQL_FACPROVXSUBLINEA = "select distinct p.tc_id, p.tc_key, p.tc_fecemi , "
                + " p.tc_fecven, p.emp_id, p.suc_id, p.tc_tipdoc, p.tipo, p.tc_serie,p.tc_nrodoc,p.tc_provid,p.prov_razsoc,p.tc_nrpedkey, "
                + " p.tc_moneda,p.tc_tcambio,p.tc_conid,p.tc_condes,p.tc_dscgral,p.tc_dscfin,p.tc_ocvtotal,p.tc_valventa ,p.tc_glosa,p.tc_est,p.tc_oc,p.tc_est_des, "
                + " p.tc_usuadd,p.tc_fecadd,p.tc_usumod,p.tc_fecmod, sum(x.tcd_vventafac) tc_valventa, sum(x.tcd_igvfac) tc_vimpt,sum(x.tcd_subtotalfac) tc_vtotal "
                + " from tsublineas t,v_listafacturaproveedorcab p, tcompras_det x "
                + " where p.emp_id=x.emp_id and p.suc_id=x.suc_id and p.tc_key=x.tc_key and "
                + " p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and "
                + " to_char(p.tc_fecemi,'yyyymm') like '" + periodo + "' and p.tc_est=1 and "
                + " x.tcd_est=1 and to_number(codijisa.pack_tproductos.f_004_descslineaproducto(x.pro_id))= t.slin_cod and t.slin_cod like '" + lin_key + "' " + validaFecha
                + " group by p.tc_id, p.tc_key, p.tc_fecemi , "
                + " p.tc_fecven, p.emp_id, p.suc_id, p.tc_tipdoc, p.tipo, p.tc_serie,p.tc_nrodoc,p.tc_provid,p.prov_razsoc,p.tc_nrpedkey, "
                + " p.tc_moneda,p.tc_tcambio,p.tc_conid,p.tc_condes,p.tc_dscgral,p.tc_dscfin,p.tc_ocvtotal,p.tc_valventa ,p.tc_glosa,p.tc_est,p.tc_oc,p.tc_est_des,"
                + " p.tc_usuadd,p.tc_fecadd,p.tc_usumod,p.tc_fecmod"
                + " order by p.tc_key";
        P_WHERER = SQL_FACPROVXSUBLINEA;
        P_TITULO = titulo;
        objComprasCab = null;
        objlstComprasCab = null;
        objlstComprasCab = new ListModelList<ComprasCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACPROVXSUBLINEA);
            while (rs.next()) {
                objComprasCab = new ComprasCab();
                objComprasCab.setTc_key(rs.getLong("tc_key"));
                objComprasCab.setEmp_id(rs.getInt("emp_id"));
                objComprasCab.setSuc_id(rs.getInt("suc_id"));
                objComprasCab.setTc_id(rs.getString("tc_id"));
                objComprasCab.setTc_tipdoc(rs.getString("tc_tipdoc"));
                objComprasCab.setTc_tipodocumento(rs.getString("tipo"));
                objComprasCab.setTc_serie(rs.getString("tc_serie"));
                objComprasCab.setTc_nrodoc(rs.getString("tc_nrodoc"));
                objComprasCab.setTc_provid(rs.getString("tc_provid"));
                objComprasCab.setTc_provrazsoc(rs.getString("prov_razsoc"));
                objComprasCab.setTc_fecemi(rs.getDate("tc_fecemi"));
                objComprasCab.setTc_fecven(rs.getDate("tc_fecven"));
                objComprasCab.setTc_nrpedkey(rs.getLong("tc_nrpedkey"));
                objComprasCab.setTc_moneda(rs.getInt("tc_moneda"));
                objComprasCab.setTc_tcambio(rs.getInt("tc_tcambio"));
                objComprasCab.setTc_conid(rs.getInt("tc_conid"));
                objComprasCab.setTc_condicionpago(rs.getString("tc_condes"));
                objComprasCab.setTc_dscgral(rs.getDouble("tc_dscgral"));
                objComprasCab.setTc_dscfin(rs.getDouble("tc_dscfin"));
                //objComprasCab.setTc_tdsctos(rs.getDouble("tc_tdsctos"));
                objComprasCab.setTc_ocvtotal(rs.getDouble("tc_ocvtotal"));
                objComprasCab.setTc_valventa(rs.getDouble("tc_valventa"));
                objComprasCab.setTc_vimpt(rs.getDouble("tc_vimpt"));
                objComprasCab.setTc_vtotal(rs.getDouble("tc_vtotal"));
                objComprasCab.setTc_glosa(rs.getString("tc_glosa"));
                objComprasCab.setTc_est(rs.getInt("tc_est"));
                objComprasCab.setTc_usuadd(rs.getString("tc_usuadd"));
                objComprasCab.setTc_fecadd(rs.getTimestamp("tc_fecadd"));
                objComprasCab.setTc_usumod(rs.getString("tc_usumod"));
                objComprasCab.setTc_fecmod(rs.getTimestamp("tc_fecmod"));
                objlstComprasCab.add(objComprasCab);
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
        return objlstComprasCab;
    }

    public int getPrecioxLpCompra(int emp_id, int suc_id, String oc_id) throws SQLException {
        String SQL_PRECIOS = "select t.oc_lpcid from tordcompra_cab t where t.oc_nropedid='" + oc_id + "' and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + "";
        int lista = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRECIOS);
            while (rs.next()) {
                lista = rs.getInt("oc_lpcid");
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
        return lista;
    }

    public int existeFacturaProveedor(int emp_id, int suc_id, long oc_nropedkey) throws SQLException {
        String SQL_FACTURA = "select to_number(count(*)) cant from tcompras_cab t where t.tc_nrpedkey=" + oc_nropedkey + " "
                + "and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + " and t.tc_est=1";
        int lista = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_FACTURA);
            while (rs.next()) {
                lista = rs.getInt("cant");
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
        return lista;
    }

}
