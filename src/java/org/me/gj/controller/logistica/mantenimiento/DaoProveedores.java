package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.ProvCont;
import org.me.gj.model.logistica.mantenimiento.ProvDet;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zul.Messagebox;

public class DaoProveedores {

    //Instancias de Objetos
    ListModelList<Proveedores> objlstProv;
    ListModelList<ProvCont> objlstCont;
    ListModelList<ProvDet> objlstDet;
    Proveedores objProv;
    ProvCont objProvCont;
    ProvDet objProvDet;
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
    private static final Logger LOGGER = Logger.getLogger(DaoProveedores.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarProveedor(Proveedores proveedor, Object[][] listaContactos, Object[][] listaTelefonos) throws SQLException {
        String SQL_INSERT_PROVEEDORES = "{call pack_tproveedores.p_001_insertarProveedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ARRAY arrCont, arrTel;
        ArrayDescriptor arrayDescCont, arrayDescTel;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROVEEDORES);
            arrayDescCont = ArrayDescriptor.createDescriptor("LISTACONTPROV", con.getMetaData().getConnection());
            arrayDescTel = ArrayDescriptor.createDescriptor("LISTATELFPROV", con.getMetaData().getConnection());
            arrCont = new ARRAY(arrayDescCont, con.getMetaData().getConnection(), listaContactos);
            arrTel = new ARRAY(arrayDescTel, con.getMetaData().getConnection(), listaTelefonos);
            cst.clearParameters();
            cst.setLong(1, proveedor.getProv_ruc());
            cst.setString(2, proveedor.getProv_razsoc());
            cst.setString(3, proveedor.getProv_siglas());
            cst.setString(4, proveedor.getProv_dir());
            cst.setInt(5, proveedor.getCon_key());
            cst.setInt(6, proveedor.getProv_tip());
            cst.setLong(7, proveedor.getProv_rucext());
            cst.setString(8, proveedor.getProv_web());
            cst.setString(9, proveedor.getProv_clave());
            cst.setInt(10, proveedor.getProv_normal());
            cst.setInt(11, proveedor.getProv_reten());
            cst.setInt(12, proveedor.getProv_buenc());
            cst.setInt(13, proveedor.getProv_percep());
            cst.setInt(14, proveedor.getProv_rel());
            cst.setString(15, proveedor.getProv_usuadd());
            cst.setString(16, proveedor.getProv_nomrep());
            cst.setInt(17, proveedor.getProv_ord());
            cst.setArray(18, arrCont);
            cst.setArray(19, arrTel);
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + proveedor.getProv_razsoc().toUpperCase());
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

    public String insertarContacto(ProvCont prov, Object[][] listaContactos, Object[][] listaTelefonos) throws SQLException {
        String SQL_INSERT_CONTACTOS = "{call pack_tproveedores.p_002_insertarContacto(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_CONTACTOS);
            cst.clearParameters();
            cst.setInt("n_procon_id", prov.getProcon_id());
            cst.setLong("n_prov_key", prov.getProv_key());
            cst.setString("c_procon_nom", prov.getProcon_nom().toUpperCase());
            cst.setString("c_procon_ape", prov.getProcon_ape().toUpperCase());
            cst.setString("c_procon_ema", prov.getProcon_ema().toUpperCase());
            cst.setString("c_procon_usuadd", prov.getProcon_usuadd().toUpperCase());
            cst.setLong("n_procon_telef1", prov.getProcon_telef1());
            cst.setLong("n_procon_telef2", prov.getProcon_telef2());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo contacto con nombre: " + prov.getProcon_nom().toUpperCase() + "del Proveedor :" + prov.getProv_key());
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

    public String insertarTelefono(ProvDet prov) throws SQLException {
        String SQL_INSERT_TELEFONO = "{call pack_tproveedores.p_003_insertarTelefono(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_TELEFONO);
            cst.clearParameters();
            cst.setLong("n_prodet_id", prov.getProdet_id());
            cst.setLong("n_prov_key", prov.getProv_key());
            cst.setLong("n_prodet_tel", prov.getProdet_tel());
            cst.setLong("n_prodet_cel", prov.getProdet_cel());
            cst.setLong("n_prodet_fax", prov.getProdet_fax());
            cst.setString("c_prodet_usuadd", prov.getProdet_usuadd().toUpperCase());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo telefono del proveedor : " + prov.getProv_key());
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

    public ParametrosSalida actualizarProveedor(Proveedores proveedor, Object[][] listaContactos, Object[][] listaTelefonos) throws SQLException {
        String SQL_UPDATE_PROVEEDOR = "{call pack_tproveedores.p_001_actualizarProveedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ArrayDescriptor arrayDescCont, arrayDescTel;
        ARRAY arrCont, arrTel;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROVEEDOR);
            arrayDescCont = ArrayDescriptor.createDescriptor("LISTACONTPROV", con.getMetaData().getConnection());
            arrayDescTel = ArrayDescriptor.createDescriptor("LISTATELFPROV", con.getMetaData().getConnection());
            arrCont = new ARRAY(arrayDescCont, con.getMetaData().getConnection(), listaContactos);
            arrTel = new ARRAY(arrayDescTel, con.getMetaData().getConnection(), listaTelefonos);
            cst.clearParameters();
            cst.setLong(1, proveedor.getProv_key());
            cst.setLong(2, proveedor.getProv_ruc());
            cst.setString(3, proveedor.getProv_razsoc());
            cst.setString(4, proveedor.getProv_siglas());
            cst.setString(5, proveedor.getProv_dir().toUpperCase());
            cst.setInt(6, proveedor.getProv_tip());
            cst.setInt(7, proveedor.getCon_key());
            cst.setString(8, proveedor.getProv_web().toUpperCase());
            cst.setString(9, proveedor.getProv_clave().toUpperCase());
            cst.setLong(10, proveedor.getProv_rucext());
            cst.setInt(11, proveedor.getProv_normal());
            cst.setInt(12, proveedor.getProv_reten());
            cst.setInt(13, proveedor.getProv_buenc());
            cst.setInt(14, proveedor.getProv_percep());
            cst.setInt(15, proveedor.getProv_detrac());
            cst.setInt(16, proveedor.getProv_rel());
            cst.setString(17, proveedor.getProv_nomrep().toUpperCase());
            cst.setString(18, proveedor.getProv_usumod().toUpperCase());
            cst.setInt(19, proveedor.getProv_ord());
            cst.setInt(20, proveedor.getProv_est());
            cst.setArray(21, arrCont);
            cst.setArray(22, arrTel);
            cst.registerOutParameter(23, java.sql.Types.NUMERIC);
            cst.registerOutParameter(24, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(24);
            i_flagErrorBD = cst.getInt(23);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + proveedor.getProv_id());
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

    public ParametrosSalida eliminar(Proveedores objProveedor) throws SQLException {
        String SQL_DELETE_PROVEEDORES = "{call pack_tproveedores.p_001_eliminarProveedor(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROVEEDORES);
            cst.clearParameters();
            cst.setLong(1, objProveedor.getProv_key());
            cst.setString(2, objProveedor.getProv_usumod());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objProveedor.getProv_key());
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

    public String actualizarContacto(ProvCont contacto) throws SQLException {
        String SQL_UPDATE_CONTACTO = "{call pack_tproveedores.p_002_actualizarContacto(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_CONTACTO);
            cst.clearParameters();
            cst.setLong("n_prov_key", contacto.getProv_key());
            cst.setInt("n_procon_id", contacto.getProcon_id());
            cst.setString("c_procon_nom", contacto.getProcon_nom().toUpperCase());
            cst.setString("c_procon_ape", contacto.getProcon_ape().toUpperCase());
            cst.setString("c_procon_ema", contacto.getProcon_ema().toUpperCase());
            cst.setString("c_procon_usumod", contacto.getProcon_usumod().toUpperCase());
            cst.setLong("n_procon_telef1", contacto.getProcon_telef1());
            cst.setLong("n_procon_telef2", contacto.getProcon_telef2());
            cst.setInt("n_procon_est", contacto.getProcon_est());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + contacto.getProcon_id());
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

    public String actualizarTelefono(ProvDet telefono) throws SQLException {
        String SQL_UPDATE_TELEFONO = "{call pack_tproveedores.p_003_actualizarTelefono(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_TELEFONO);
            cst.clearParameters();
            cst.setLong("n_prov_key", telefono.getProv_key());
            cst.setInt("n_prodet_id", telefono.getProdet_id());
            cst.setLong("n_prodet_tel", telefono.getProdet_tel());
            cst.setLong("n_prodet_cel", telefono.getProdet_cel());
            cst.setLong("n_prodet_fax", telefono.getProdet_fax());
            cst.setString("c_prodet_usumod", telefono.getProdet_usumod().toUpperCase());
            cst.setInt("n_prodet_est", telefono.getProdet_est());
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + telefono.getProdet_id());
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Proveedores> listaProveedores(String estado) throws SQLException {
        String sql;
        if ("0".equals(estado)) {
            sql = "select * from v_listaproveedor t where t.prov_est<>0 order by t.prov_id,t.prov_tip";
        } else {
            sql = "select * from v_listaproveedor t where t.prov_est=1 order by t.prov_id,t.prov_tip";
        }
        P_WHERE = sql;
        objlstProv = new ListModelList<Proveedores>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objProv = new Proveedores();
                objProv.setProv_id(rs.getString("prov_id"));
                objProv.setProv_key(rs.getLong("prov_key"));
                objProv.setProv_ruc(rs.getLong("prov_ruc"));
                objProv.setCon_key(rs.getInt("con_key"));
                objProv.setCon_tipo(rs.getString("con_tipo"));
                objProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objProv.setProv_siglas(rs.getString("prov_siglas"));
                objProv.setProv_dir(rs.getString("prov_dir"));
                objProv.setProv_tip(rs.getInt("prov_tip"));
                objProv.setProv_rucext(rs.getLong("prov_rucext"));
                objProv.setProv_web(rs.getString("prov_web"));
                objProv.setProv_clave(rs.getString("prov_clave"));
                objProv.setProv_est(rs.getInt("prov_est"));
                objProv.setProv_normal(rs.getInt("prov_normal"));
                objProv.setProv_reten(rs.getInt("prov_reten"));
                objProv.setProv_buenc(rs.getInt("prov_buenc"));
                objProv.setProv_percep(rs.getInt("prov_percep"));
                objProv.setProv_detrac(rs.getInt("prov_detrac"));
                objProv.setProv_rel(rs.getInt("prov_rel"));
                objProv.setProv_usuadd(rs.getString("prov_usuadd"));
                objProv.setProv_fecadd(rs.getTimestamp("prov_fecadd"));
                objProv.setProv_usumod(rs.getString("prov_usumod"));
                objProv.setProv_fecmod(rs.getTimestamp("prov_fecmod"));
                objProv.setProv_nomrep(rs.getString("prov_nomrep"));
                objProv.setProv_ord(rs.getInt("prov_ord"));
                objProv.setCon_des(rs.getString("con_des"));
                objlstProv.add(objProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProv.getSize() + " registro(s)");
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
        return objlstProv;
    }

    public ListModelList<ProvCont> listaContactos(long codigo) throws SQLException {
        String SQL_LISTA_CONTACTOS = " select * from v_listacontactosproveedor t where t.procon_est<>0 and t.prov_key = '" + codigo + "' order by t.procon_id ";
        objlstCont = new ListModelList<ProvCont>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_CONTACTOS);
            while (rs.next()) {
                objProvCont = new ProvCont();
                objProvCont.setProcon_id(rs.getInt("procon_id"));
                //objProvCont.setProv_(rs.getLong(2));
                objProvCont.setProv_key(rs.getLong("prov_key"));
                objProvCont.setProcon_nom(rs.getString("procon_nom"));
                objProvCont.setProv_razsoc(rs.getString("prov_razsoc"));
                objProvCont.setProcon_ape(rs.getString("procon_ape"));
                objProvCont.setProcon_ema(rs.getString("procon_ema"));
                objProvCont.setProcon_usuadd(rs.getString("procon_usuadd"));
                objProvCont.setProcon_fecadd(rs.getTimestamp("procon_fecadd"));
                objProvCont.setProcon_usumod(rs.getString("procon_usumod"));
                objProvCont.setProcon_fecmod(rs.getTimestamp("procon_fecmod"));
                objProvCont.setProcon_telef1(rs.getLong("procon_telef1"));
                objProvCont.setProcon_telef2(rs.getLong("procon_telef2"));
                objProvCont.setProcon_cargo(rs.getString("procon_cargo"));
                objProvCont.setProcon_suc(rs.getString("procon_suc"));
                objProvCont.setProcon_est(rs.getInt("procon_est"));
                objlstCont.add(objProvCont);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCont.getSize() + " registro(s)");
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
        return objlstCont;
    }

    public ListModelList<ProvDet> listaTelefonos(long codigo) throws SQLException {
        String SQL_LISTA_TELEFONOS = "select * from v_listadetalleproveedor t where t.prodet_est<>0 and t.prov_key = '" + codigo + "' order by t.prodet_id";
        objlstDet = new ListModelList<ProvDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_TELEFONOS);
            while (rs.next()) {
                objProvDet = new ProvDet();
                objProvDet.setProdet_id(rs.getInt("prodet_id"));
                objProvDet.setProv_key(rs.getLong("prov_key"));
                objProvDet.setProv_razsoc(rs.getString("prov_razsoc"));
                objProvDet.setProdet_est(rs.getInt("prodet_est"));
                objProvDet.setProdet_tel(rs.getLong("prodet_tel"));
                objProvDet.setProdet_cel(rs.getLong("prodet_cel"));
                objProvDet.setProdet_fax(rs.getLong("prodet_fax"));
                objProvDet.setProdet_usuadd(rs.getString("prodet_usuadd"));
                objProvDet.setProdet_fecadd(rs.getTimestamp("prodet_fecadd"));
                objProvDet.setProdet_usumod(rs.getString("prodet_usumod"));
                objProvDet.setProdet_fecmod(rs.getTimestamp("prodet_fecmod"));
                objlstDet.add(objProvDet);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstDet.getSize() + " registro(s)");
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
        return objlstDet;
    }

    public ListModelList<Proveedores> busquedaProveedores(int seleccion, String consulta, int i_estado, String tipo) throws SQLException {
        String sql = "";
        //X CODIGO
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select * from v_listaproveedor t where  t.prov_est<>0 and t.prov_tip like '" + tipo + "' order by t.prov_id";
            } else {
                if (seleccion == 1) {
                    sql = "select * from v_listaproveedor t where t.prov_id like '" + consulta + "' and t.prov_est<>0 and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X RAZON SOCIAL 
                else if (seleccion == 2) {
                    sql = "select * from v_listaproveedor t where t.prov_razsoc LIKE '" + consulta + "' and t.prov_est<>0 and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X DIRECCION
                else if (seleccion == 3) {
                    sql = "select * from v_listaproveedor t where t.Prov_Dir LIKE '" + consulta + "' and t.prov_est <>0 and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X RUC
                else if (seleccion == 4) {
                    sql = "select * from v_listaproveedor t where t.Prov_Ruc LIKE '" + consulta + "' and t.prov_est <>0 and t.prov_tip like '" + tipo + "' order by t.prov_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select * from v_listaproveedor t where  t.prov_est = '" + i_estado + "' and t.prov_tip like '" + tipo + "' order by t.prov_id";
            } else {
                if (seleccion == 1) {
                    sql = "select * from v_listaproveedor t where t.prov_id like '" + consulta + "' and t.prov_est = '" + i_estado + "' and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X RAZON SOCIAL
                else if (seleccion == 2) {
                    sql = "select * from v_listaproveedor t where t.prov_razsoc LIKE '" + consulta + "' and t.prov_est = '" + i_estado + "' and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X DIRECCION
                else if (seleccion == 3) {
                    sql = "select * from v_listaproveedor t where t.Prov_Dir LIKE '" + consulta + "' and t.prov_est = '" + i_estado + "' and t.prov_tip like '" + tipo + "' order by t.prov_id";
                } //X RUC
                else if (seleccion == 4) {
                    sql = "select * from v_listaproveedor t where t.Prov_Ruc LIKE '" + consulta + "' and t.prov_est = '" + i_estado + "' and t.prov_tip like '" + tipo + "' order by t.prov_id";
                }
            }
        }
        P_WHERE = sql;
        objlstProv = new ListModelList<Proveedores>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objProv = new Proveedores();
                objProv.setProv_id(rs.getString("prov_id"));
                objProv.setProv_key(rs.getLong("prov_key"));
                objProv.setProv_ruc(rs.getLong("prov_ruc"));
                objProv.setCon_key(rs.getInt("con_key"));
                objProv.setCon_tipo(rs.getString("con_tipo"));
                objProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objProv.setProv_siglas(rs.getString("prov_siglas"));
                objProv.setProv_dir(rs.getString("prov_dir"));
                objProv.setProv_tip(rs.getInt("prov_tip"));
                objProv.setProv_rucext(rs.getLong("prov_rucext"));
                objProv.setProv_web(rs.getString("prov_web"));
                objProv.setProv_clave(rs.getString("prov_clave"));
                objProv.setProv_est(rs.getInt("prov_est"));
                objProv.setProv_normal(rs.getInt("prov_normal"));
                objProv.setProv_reten(rs.getInt("prov_reten"));
                objProv.setProv_buenc(rs.getInt("prov_buenc"));
                objProv.setProv_percep(rs.getInt("prov_percep"));
                objProv.setProv_detrac(rs.getInt("prov_detrac"));
                objProv.setProv_rel(rs.getInt("prov_rel"));
                objProv.setProv_usuadd(rs.getString("prov_usuadd"));
                objProv.setProv_fecadd(rs.getTimestamp("prov_fecadd"));
                objProv.setProv_usumod(rs.getString("prov_usumod"));
                objProv.setProv_fecmod(rs.getTimestamp("prov_fecmod"));
                objProv.setProv_nomrep(rs.getString("prov_nomrep"));
                objProv.setProv_ord(rs.getInt("prov_ord"));
                objProv.setCon_des(rs.getString("con_des"));
                objlstProv.add(objProv);
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
        return objlstProv;
    }

    public Proveedores BusquedaProveedor(Long Codigo) throws SQLException {
        String SQL_PROV = "select t.prov_key,t.prov_id,t.prov_razsoc,t.con_key,t.con_des from v_listaproveedor t where t.prov_key=" + Codigo;
        objProv = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROV);
            objProv = null;
            while (rs.next()) {
                objProv = new Proveedores();
                objProv.setProv_key(rs.getLong("prov_key"));
                objProv.setProv_id(rs.getString("prov_id"));
                objProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objProv.setCon_key(rs.getInt("con_key"));
                objProv.setCon_des(rs.getString("con_des"));
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
        return objProv;
    }

    public Proveedores BusquedaProveedor(String codigo, String tipo) throws SQLException {
        //String SQL_PROV = "select t.prov_key,t.prov_id,t.prov_razsoc,t.con_key,t.con_des from v_listaproveedor t where t.prov_id='" + codigo + "'";
        String SQL_PROV;
        if ("1".equals(tipo)) {
            SQL_PROV = "select t.prov_key,t.prov_id , t.prov_razsoc, t.con_key, t.con_des from v_listaproveedor t where t.prov_id='" + codigo + "' and t.prov_est <> 0 and t.prov_tip = " + tipo + " order by t.prov_id,t.prov_tip";
        } else {
            SQL_PROV = "select t.prov_key,t.prov_id , t.prov_razsoc, t.con_key, t.con_des from v_listaproveedor t where t.prov_id='" + codigo + "' and t.prov_est <> 0 and t.prov_tip = " + tipo + " order by t.prov_id,t.prov_tip";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROV);
            objProv = null;
            while (rs.next()) {
                objProv = new Proveedores();
                objProv.setProv_key(rs.getLong("prov_key"));
                objProv.setProv_id(rs.getString("prov_id"));
                objProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objProv.setCon_key(rs.getInt("con_key"));
                objProv.setCon_des(rs.getString("con_des"));
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
        return objProv;
    }

    public ListModelList<Proveedores> getListaProveedores(String tipo) throws SQLException {
        String sql;
        if ("1".equals(tipo)) {
            sql = "select t.prov_id , t.prov_razsoc, t.con_key, t.con_des from v_listaproveedor t where t.prov_est <> 0 and t.prov_tip = " + tipo + " order by t.prov_id,t.prov_tip";
        } else {
            sql = "select t.prov_id , t.prov_razsoc, t.con_key, t.con_des from v_listaproveedor t where t.prov_est <> 0 and t.prov_tip = " + tipo + " order by t.prov_id,t.prov_tip";
        }
        objlstProv = new ListModelList<Proveedores>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objProv = new Proveedores();
                objProv.setProv_id(rs.getString("prov_id"));
                objProv.setProv_razsoc(rs.getString("prov_razsoc"));
                objProv.setCon_key(rs.getInt("con_key"));
                objProv.setCon_des(rs.getString("con_des"));
                objlstProv.add(objProv);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProv.getSize() + " registro(s)");
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
        return objlstProv;
    }

    public Long extraerCodigoProveedor() throws SQLException {
        String SQL_CODIGO = "select to_number(max(t.prov_id)) from tproveedores t";
        Long codigo = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CODIGO);
            rs.next();
            codigo = rs.getLong(1);
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
        return codigo;
    }

    public int extraerCodigoContacto() throws SQLException {
        String SQL_CODIGO_CONTACTO = "select to_number(nvl(max(t.procon_id),0)) from tproveedores_cont t";
        int codigo = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CODIGO_CONTACTO);
            rs.next();
            codigo = rs.getInt(1);
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
        return codigo;
    }

    public int extraerCodigoTelefono() throws SQLException {
        String SQL_CODIGO_TELEFONO = "select to_number(nvl(max(t.prodet_id),0)) from tproveedores_det t";
        int codigo = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CODIGO_TELEFONO);
            rs.next();
            codigo = rs.getInt(1);
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
        return codigo;
    }

    public int ExistenciaRuc(Long ruc) throws SQLException {
        String SQL_RUC = "select count(*) from tproveedores t where t.prov_ruc='" + ruc + "'";
        int codigo = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_RUC);
            rs.next();
            codigo = rs.getInt(1);
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
        return codigo;
    }

    public String validaMovimientos(Proveedores proveedor) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tproveedores.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, proveedor.getProv_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del proveedor " + proveedor.getProv_id());
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
}
