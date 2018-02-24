package org.me.gj.controller.logistica.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.InvFisicoCab;
import org.me.gj.model.logistica.procesos.InvFisicoDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoInvFisico {

    //Instancias de Objetos
    ListModelList<InvFisicoCab> objlstInvFisicoCab = new ListModelList<InvFisicoCab>();
    ListModelList<InvFisicoDet> objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
    InvFisicoCab objInvFisicoCab;
    InvFisicoDet objInvFisicoDet;
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
    private static final Logger LOGGER = Logger.getLogger(DaoInvFisico.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarInventario(InvFisicoCab objInvFisicoCab, Object[][] listaDetalles) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        ARRAY arrDet;
        ArrayDescriptor arrayDet;
        String SQL_INSERTAR_INVENTARIO = "{call pack_tstckfisico.p_insertarStockFisico(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_INVENTARIO);
            arrayDet = ArrayDescriptor.createDescriptor("LISTAFISICODET", con.getMetaData().getConnection());
            arrDet = new ARRAY(arrayDet, con.getMetaData().getConnection(), listaDetalles);
            cst.clearParameters();
            cst.setString(1, objInvFisicoCab.getFisicab_id().toUpperCase());
            cst.setInt(2, objInvFisicoCab.getEmp_id());
            cst.setInt(3, objInvFisicoCab.getSuc_id());
            cst.setInt(4, objInvFisicoCab.getAlm_key());
            cst.setString(5, objInvFisicoCab.getPer_id());
            cst.setInt(6, objInvFisicoCab.getPer_key());
            cst.setString(7, objInvFisicoCab.getFisicab_respon());
            cst.setString(8, objInvFisicoCab.getFisicab_usuadd());
            cst.setString(9, objInvFisicoCab.getFisicab_pcadd());
            cst.setArray(10, arrDet);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida modificarInventario(InvFisicoCab objInvFisicoCab, Object[][] listaDetalles) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        ARRAY arrDet;
        ArrayDescriptor arrayDet;
        String SQL_MODIFICAR_INVENTARIO = "{call pack_tstckfisico.p_modificarStockFisico(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_INVENTARIO);
            arrayDet = ArrayDescriptor.createDescriptor("LISTAFISICODET", con.getMetaData().getConnection());
            arrDet = new ARRAY(arrayDet, con.getMetaData().getConnection(), listaDetalles);
            cst.clearParameters();
            cst.setString(1, objInvFisicoCab.getFisicab_id().toUpperCase());
            cst.setInt(2, objInvFisicoCab.getEmp_id());
            cst.setInt(3, objInvFisicoCab.getSuc_id());
            cst.setInt(4, objInvFisicoCab.getAlm_key());
            cst.setString(5, objInvFisicoCab.getPer_id());
            cst.setInt(6, objInvFisicoCab.getPer_key());
            cst.setString(7, objInvFisicoCab.getFisicab_respon());
            cst.setString(8, objInvFisicoCab.getFisicab_usumod());
            cst.setString(9, objInvFisicoCab.getFisicab_pcmod());
            cst.setArray(10, arrDet);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarInventario(InvFisicoCab objInvFisico) throws SQLException {
        String SQL_DELETE_INVFISICO = "{call pack_tstckfisico.p_eliminarStockFisico(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_INVFISICO);
            cst.clearParameters();
            cst.setString(1, objInvFisico.getFisicab_id());
            cst.setInt(2, objInvFisico.getEmp_id());
            cst.setInt(3, objInvFisico.getSuc_id());
            cst.setInt(4, objInvFisico.getAlm_key());
            cst.setString(5, objInvFisico.getPer_id());
            cst.setInt(6, objInvFisico.getPer_key());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objInvFisico.getFisicab_id());
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

    public ParametrosSalida insertarInventarioDet(InvFisicoDet objInvFisicoDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_INVENTARIO = "{call pack_tstckfisico.p_insertarStockFisicoDet(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_INVENTARIO);
            cst.clearParameters();
            cst.setString(1, objInvFisicoDet.getFisicab_id().toUpperCase());
            cst.setInt(2, objInvFisicoDet.getEmp_id());
            cst.setInt(3, objInvFisicoDet.getSuc_id());
            cst.setInt(4, objInvFisicoDet.getAlm_key());
            cst.setString(5, objInvFisicoDet.getPer_id());
            cst.setInt(6, objInvFisicoDet.getPer_key());
            cst.setInt(7, objInvFisicoDet.getUbic_key());
            cst.setString(8, objInvFisicoDet.getPro_id());
            cst.setLong(9, objInvFisicoDet.getPro_key());
            cst.setLong(10, objInvFisicoDet.getProv_key());
            cst.setLong(11, objInvFisicoDet.getFisidet_cant());
            //cst.setString(12, objInvFisicoCab.getFisicab_usuadd());
            //cst.setString(13, objInvFisicoCab.getFisicab_pcadd());
            cst.setString(12, objInvFisicoDet.getFisidet_usuadd());
            cst.setString(13, objInvFisicoDet.getFisidet_pcadd());
            
            cst.registerOutParameter(14, java.sql.Types.NUMERIC);
            cst.registerOutParameter(15, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(15);
            i_flagErrorBD = cst.getInt(14);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
    
    
//    public ParametrosSalida insertarInventarioDet(InvFisicoCab objInvFisicoCab, Object[][] listaDetalles) throws SQLException {
//        i_flagErrorBD = 0;
//        s_msg = "";
//        cst = null;
//        ARRAY arrDet;
//        ArrayDescriptor arrayDet;
//        String SQL_INSERTAR_INVENTARIO = "{call pack_tstckfisico.p_insertarStockFisicoDet(?,?,?,?,?,?,?,?,?,?,?,?)}";
//        try {
//            con = new ConectaBD().conectar();
//            cst = con.prepareCall(SQL_INSERTAR_INVENTARIO);
//            arrayDet = ArrayDescriptor.createDescriptor("LISTAFISICODET", con.getMetaData().getConnection());
//            arrDet = new ARRAY(arrayDet, con.getMetaData().getConnection(), listaDetalles);
//            cst.clearParameters();
//            cst.setString(1, objInvFisicoCab.getFisicab_id().toUpperCase());
//            cst.setInt(2, objInvFisicoCab.getEmp_id());
//            cst.setInt(3, objInvFisicoCab.getSuc_id());
//            cst.setInt(4, objInvFisicoCab.getAlm_key());
//            cst.setString(5, objInvFisicoCab.getPer_id());
//            cst.setInt(6, objInvFisicoCab.getPer_key());
//            cst.setString(7, objInvFisicoCab.getFisicab_respon());
//            cst.setString(8, objInvFisicoCab.getFisicab_usuadd());
//            cst.setString(9, objInvFisicoCab.getFisicab_pcadd());
//            cst.setArray(10, arrDet);
//            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
//            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
//            cst.execute();
//            s_msg = cst.getString(12);
//            i_flagErrorBD = cst.getInt(11);
//            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
//        } catch (SQLException e) {
//            i_flagErrorBD = 1;
//            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
//            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
//        } catch (NullPointerException e) {
//            i_flagErrorBD = 1;
//            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
//            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
//        } finally {
//            if (con != null) {
//                cst.close();
//                con.close();
//            }
//        }
//        return new ParametrosSalida(i_flagErrorBD, s_msg);
//    }

    public ParametrosSalida modificarInventarioDet(InvFisicoCab objInvFisicoCab, String pro_id, Object[][] listaDetalles) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        ARRAY arrDet;
        ArrayDescriptor arrayDet;
        String SQL_MODIFICAR_INVENTARIO = "{call pack_tstckfisico.p_modificarStockFisicoDet(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_INVENTARIO);
            arrayDet = ArrayDescriptor.createDescriptor("LISTAFISICODET", con.getMetaData().getConnection());
            arrDet = new ARRAY(arrayDet, con.getMetaData().getConnection(), listaDetalles);
            cst.clearParameters();
            cst.setString(1, objInvFisicoCab.getFisicab_id().toUpperCase());
            cst.setInt(2, objInvFisicoCab.getEmp_id());
            cst.setInt(3, objInvFisicoCab.getSuc_id());
            cst.setInt(4, objInvFisicoCab.getAlm_key());
            cst.setString(5, objInvFisicoCab.getPer_id());
            cst.setInt(6, objInvFisicoCab.getPer_key());
            cst.setString(7, pro_id);
            cst.setString(8, objInvFisicoCab.getFisicab_usumod());
            cst.setString(9, objInvFisicoCab.getFisicab_pcmod());
            cst.setArray(10, arrDet);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarInventarioDet(InvFisicoDet objInvFisicoDet) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_INVENTARIO = "{call pack_tstckfisico.p_eliminarStockFisicoDet(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_INVENTARIO);
            cst.clearParameters();
            cst.setString(1, objInvFisicoDet.getFisicab_id().toUpperCase());
            cst.setInt(2, objInvFisicoDet.getEmp_id());
            cst.setInt(3, objInvFisicoDet.getSuc_id());
            cst.setInt(4, objInvFisicoDet.getAlm_key());
            cst.setString(5, objInvFisicoDet.getPer_id());
            cst.setInt(6, objInvFisicoDet.getPer_key());
            cst.setString(7, objInvFisicoDet.getPro_id());
            cst.setInt(8, objInvFisicoDet.getFisidet_key());
            cst.setString(9, objUsuCredential.getCuenta());
            cst.setString(10, objUsuCredential.getComputerName());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero el Stock fisico con el codigo " + objInvFisicoCab.getFisicab_id());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el Stock Fisico debido al Error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }
    
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<InvFisicoCab> listaInvFisico(String grup_id, int emp_id, int suc_id, String per_key, int alm_key) throws SQLException {
        String valida_grupid = grup_id.isEmpty() ? "" : " and t.fisicab_id='" + grup_id + "' ";
        String valida_perid = per_key.isEmpty() ? "" : " and t.per_key=" + per_key;
        String valida_almid = alm_key == 0 ? "" : " and t.alm_key=" + alm_key;
        String SQL_INVFISICOCAB = "select * from v_listainvfiscab t where t.fisicab_est<>0 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id + valida_perid + valida_almid + valida_grupid + " order by t.fisicab_id";
        objlstInvFisicoCab = null;
        objlstInvFisicoCab = new ListModelList<InvFisicoCab>();
        try {
            objInvFisicoCab = null;
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICOCAB);
            while (rs.next()) {
                objInvFisicoCab = new InvFisicoCab();
                objInvFisicoCab.setFisicab_id(rs.getString("fisicab_id"));
                objInvFisicoCab.setEmp_id(rs.getInt("emp_id"));
                objInvFisicoCab.setSuc_id(rs.getInt("suc_id"));
                objInvFisicoCab.setAlm_key(rs.getInt("alm_key"));
                objInvFisicoCab.setAlm_id(rs.getString("alm_id"));
                objInvFisicoCab.setAlm_des(rs.getString("alm_des"));
                objInvFisicoCab.setPer_id(rs.getString("per_id"));
                objInvFisicoCab.setPer_key(rs.getInt("per_key"));
                objInvFisicoCab.setFisicab_respon(rs.getString("fisicab_respon"));
                objInvFisicoCab.setFisicab_est(rs.getInt("fisicab_est"));
                objInvFisicoCab.setFisicab_usuadd(rs.getString("fisicab_usuadd"));
                objInvFisicoCab.setFisicab_fecadd(rs.getDate("fisicab_fecadd"));
                objInvFisicoCab.setFisicab_pcadd(rs.getString("fisicab_pcadd"));
                objInvFisicoCab.setFisicab_usumod(rs.getString("fisicab_usuadd"));
                objInvFisicoCab.setFisicab_fecmod(rs.getDate("fisicab_fecmod"));
                objInvFisicoCab.setFisicab_pcmod(rs.getString("fisicab_pcmod"));
                objlstInvFisicoCab.add(objInvFisicoCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objlstInvFisicoCab;
    }

    public ListModelList<InvFisicoCab> listaInvFisicoCab(int emp_id, int suc_id, int per_id, int alm_key) throws SQLException {
        String SQL_INVFISICOCAB = "select t.emp_id, t.suc_id, t.per_id, t.alm_key, t.pro_id, pack_tproductos.f_002_deslargaProducto(t.pro_id) pro_des, "
                + " pack_tproductos.f_005_presminvenProducto(t.pro_id) pro_presminven,lpad(t.prov_key,8,'0') prov_id, pack_tproveedores.f_001_descProveedor(lpad(t.prov_key,8,'0')) prov_razsoc from tstocks t "
                + " where "
                + " t.emp_id=" + emp_id + " "
                + " and t.suc_id=" + suc_id + " "
                + " and t.per_key=" + per_id + " "
                + " and t.alm_key=" + alm_key + " "
                + " group by t.emp_id, t.suc_id, t.per_id, t.alm_key, t.pro_id, t.prov_key "
                + " order by t.pro_id";
        ListModelList<InvFisicoCab> objlstInvFisico = new ListModelList<InvFisicoCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICOCAB);
            while (rs.next()) {
                objInvFisicoCab = new InvFisicoCab();
                objInvFisicoCab.setEmp_id(rs.getInt("emp_id"));
                objInvFisicoCab.setSuc_id(rs.getInt("suc_id"));
                objInvFisicoCab.setPer_id(rs.getString("per_id"));
                objInvFisicoCab.setAlm_key(rs.getInt("alm_key"));
                objInvFisicoCab.setPro_id(rs.getString("pro_id"));
                objInvFisicoCab.setPro_des(rs.getString("pro_des"));
                objInvFisicoCab.setPro_presminven(rs.getInt("pro_presminven"));
                objInvFisicoCab.setProv_id(rs.getString("prov_id"));
                objInvFisicoCab.setProv_razsoc(rs.getString("prov_razsoc"));
                objlstInvFisico.add(objInvFisicoCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objlstInvFisico;
    }

    public ListModelList<InvFisicoDet> listaStkFisicoDet(int emp_id, int suc_id, String grup_id, int per_key, int alm_key) throws SQLException {
        String valida_perid = per_key == 0 ? "" : " and t.per_key=" + per_key;
        String valida_almid = alm_key == 0 ? "" : " and t.alm_key=" + alm_key;
        String SQL_INVFISICODET = "select '" + grup_id + "' fisicab_id,t.emp_id, t.suc_id, t.alm_key, lpad(t.alm_key,4,'0') alm_id, pack_talmacenes.f_descripcion(t.emp_id,t.suc_id,lpad(t.alm_key,4,'0')) alm_des, t.per_id, "
                + " t.per_key,rownum-1 fisidet_key,t.ubic_key, lpad(t.ubic_key,4,'0') ubic_id, "
                + " pack_tubicaciones.f_descripcion(t.emp_id,t.suc_id,lpad(t.ubic_key,4,'0')) ubic_des, t.pro_id,to_number(substr(t.pro_id,7,9)) pro_key, pack_tproductos.f_002_deslargaProducto(t.pro_id) pro_des, "
                + " pack_tproductos.f_005_presminvenProducto(t.pro_id) pro_presminven,lpad(t.prov_key,8,'0') prov_id,t.prov_key, pack_tproveedores.f_001_descProveedor(lpad(t.prov_key,8,'0')) prov_razsoc, "
                + " t.stk_actual ,0 fisidet_cant, t.stk_estado fisidet_est from tstocks t  where t.stk_estado <> 0 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + valida_perid + valida_almid + " order by rownum-1, t.pro_id";
        objlstInvFisicoDet = null;
        objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICODET);
            while (rs.next()) {
                objInvFisicoDet = new InvFisicoDet();
                objInvFisicoDet.setFisicab_id(rs.getString("fisicab_id"));
                objInvFisicoDet.setEmp_id(rs.getInt("emp_id"));
                objInvFisicoDet.setSuc_id(rs.getInt("suc_id"));
                objInvFisicoDet.setAlm_key(rs.getInt("alm_key"));
                objInvFisicoDet.setAlm_id(rs.getString("alm_id"));
                objInvFisicoDet.setAlm_des(rs.getString("alm_des"));
                objInvFisicoDet.setPer_id(rs.getString("per_id"));
                objInvFisicoDet.setPer_key(rs.getInt("per_key"));
                objInvFisicoDet.setFisidet_key(rs.getInt("fisidet_key"));
                objInvFisicoDet.setUbic_key(rs.getInt("ubic_key"));
                objInvFisicoDet.setUbic_id(rs.getString("ubic_id"));
                objInvFisicoDet.setUbic_des(rs.getString("ubic_des"));
                objInvFisicoDet.setPro_id(rs.getString("pro_id"));
                objInvFisicoDet.setPro_key(rs.getLong("pro_key"));
                objInvFisicoDet.setPro_des(rs.getString("pro_des"));
                objInvFisicoDet.setPro_presminven(rs.getInt("pro_presminven"));
                objInvFisicoDet.setProv_id(rs.getString("prov_id"));
                objInvFisicoDet.setProv_key(rs.getLong("prov_key"));
                objInvFisicoDet.setProv_razsoc(rs.getString("prov_razsoc"));
                objInvFisicoDet.setFisidet_cant(rs.getInt("fisidet_cant"));
                objInvFisicoDet.setFisidet_est(rs.getInt("fisidet_est"));
                objInvFisicoDet.setStk_actual(rs.getInt("stk_actual"));
                objlstInvFisicoDet.add(objInvFisicoDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objlstInvFisicoDet;
    }

    public ListModelList<InvFisicoDet> listaInvFisicoDet(int emp_id, int suc_id, String grup_id, int per_key, int alm_key) throws SQLException {
        String valida_grupid = grup_id.isEmpty() ? "" : " and t.fisicab_id='" + grup_id + "' ";
        String valida_perid = per_key == 0 ? "" : " and t.per_key=" + per_key;
        String valida_almid = alm_key == 0 ? "" : " and t.alm_key=" + alm_key;
        String SQL_INVFISICODET = "select rownum-1 num, t.fisicab_id,t.emp_id,t.suc_id,t.per_id,t.per_key,t.alm_key,t.alm_id,t.alm_des,"
                + " t.ubic_key,t.ubic_id,t.ubic_des,t.pro_id,t.pro_key,t.pro_des,t.pro_presminven,t.prov_id,t.prov_key,t.prov_razsoc,"
                + " t.fisidet_cant, t.fisidet_est, t.stk_actual from v_listainvfisicodet t where t.fisidet_est<>0 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + valida_grupid + valida_perid + valida_almid + " order by fisidet_key-1,t.pro_id";
        objlstInvFisicoDet = null;
        objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICODET);
            while (rs.next()) {
                objInvFisicoDet = new InvFisicoDet();
                objInvFisicoDet.setFisidet_key(rs.getInt("num"));
                objInvFisicoDet.setFisicab_id(rs.getString("fisicab_id"));
                objInvFisicoDet.setEmp_id(rs.getInt("emp_id"));
                objInvFisicoDet.setSuc_id(rs.getInt("suc_id"));
                objInvFisicoDet.setPer_id(rs.getString("per_id"));
                objInvFisicoDet.setPer_key(rs.getInt("per_key"));
                objInvFisicoDet.setAlm_key(rs.getInt("alm_key"));
                objInvFisicoDet.setAlm_id(rs.getString("alm_id"));
                objInvFisicoDet.setAlm_des(rs.getString("alm_des"));
                objInvFisicoDet.setUbic_key(rs.getInt("ubic_key"));
                objInvFisicoDet.setUbic_id(rs.getString("ubic_id"));
                objInvFisicoDet.setUbic_des(rs.getString("ubic_des"));
                objInvFisicoDet.setPro_id(rs.getString("pro_id"));
                objInvFisicoDet.setPro_key(rs.getLong("pro_key"));
                objInvFisicoDet.setPro_des(rs.getString("pro_des"));
                objInvFisicoDet.setPro_presminven(rs.getInt("pro_presminven"));
                objInvFisicoDet.setProv_id(rs.getString("prov_id"));
                objInvFisicoDet.setProv_key(rs.getLong("prov_key"));
                objInvFisicoDet.setProv_razsoc(rs.getString("prov_razsoc"));
                objInvFisicoDet.setFisidet_cant(rs.getInt("fisidet_cant"));
                objInvFisicoDet.setFisidet_est(rs.getInt("fisidet_est"));
                objInvFisicoDet.setStk_actual(rs.getInt("stk_actual"));
                objlstInvFisicoDet.add(objInvFisicoDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objlstInvFisicoDet;
    }

    public ListModelList<InvFisicoDet> listaInvFisicoDetxProd(int emp_id, int suc_id, String grup_id, int per_key, int alm_key, String pro_id) throws SQLException {
        String valida_grupid = grup_id.isEmpty() ? "" : " and t.fisicab_id='" + grup_id + "' ";
        String valida_perid = per_key == 0 ? "" : " and t.per_key=" + per_key;
        String valida_almid = alm_key == 0 ? "" : " and t.alm_key=" + alm_key;
        String SQL_INVFISICODET = "select t.fisidet_key, t.fisicab_id,t.emp_id,t.suc_id,t.per_id,t.per_key,t.alm_key,t.alm_id,t.alm_des,"
                + " t.ubic_key,t.ubic_id,t.ubic_des,t.pro_id,t.pro_key,t.pro_des,t.pro_presminven,t.prov_id,t.prov_key,t.prov_razsoc,"
                + " t.fisidet_cant, t.fisidet_est, t.stk_actual from v_listainvfisicodet t where t.fisidet_est<>0 and t.emp_id=" + emp_id + " and t.suc_id=" + suc_id
                + valida_grupid + valida_perid + valida_almid + " and t.pro_id = '" + pro_id + "' order by t.fisidet_key,t.pro_id";
        objlstInvFisicoDet = null;
        objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICODET);
            while (rs.next()) {
                objInvFisicoDet = new InvFisicoDet();
                objInvFisicoDet.setFisidet_key(rs.getInt("fisidet_key"));
                objInvFisicoDet.setFisicab_id(rs.getString("fisicab_id"));
                objInvFisicoDet.setEmp_id(rs.getInt("emp_id"));
                objInvFisicoDet.setSuc_id(rs.getInt("suc_id"));
                objInvFisicoDet.setPer_id(rs.getString("per_id"));
                objInvFisicoDet.setPer_key(rs.getInt("per_key"));
                objInvFisicoDet.setAlm_key(rs.getInt("alm_key"));
                objInvFisicoDet.setAlm_id(rs.getString("alm_id"));
                objInvFisicoDet.setAlm_des(rs.getString("alm_des"));
                objInvFisicoDet.setUbic_key(rs.getInt("ubic_key"));
                objInvFisicoDet.setUbic_id(rs.getString("ubic_id"));
                objInvFisicoDet.setUbic_des(rs.getString("ubic_des"));
                objInvFisicoDet.setPro_id(rs.getString("pro_id"));
                objInvFisicoDet.setPro_key(rs.getLong("pro_key"));
                objInvFisicoDet.setPro_des(rs.getString("pro_des"));
                objInvFisicoDet.setPro_presminven(rs.getInt("pro_presminven"));
                objInvFisicoDet.setProv_id(rs.getString("prov_id"));
                objInvFisicoDet.setProv_key(rs.getLong("prov_key"));
                objInvFisicoDet.setProv_razsoc(rs.getString("prov_razsoc"));
                objInvFisicoDet.setFisidet_cant(rs.getInt("fisidet_cant"));
                objInvFisicoDet.setFisidet_est(rs.getInt("fisidet_est"));
                objInvFisicoDet.setStk_actual(rs.getInt("stk_actual"));
                objlstInvFisicoDet.add(objInvFisicoDet);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objlstInvFisicoDet;
    }

    public String generaGrupoInvFisico(int emp_id, int suc_id) throws SQLException {
        String codigo = "";
        String SQL_GENERA_CODIGO = "select pack_tstckfisico.f_generoGrupoStockFisico(" + emp_id + "," + suc_id + ") num from dual";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GENERA_CODIGO);
            while (rs.next()) {
                codigo = rs.getString("num");
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | el codigo generado es " + codigo + "");
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
        return codigo;

    }

    public InvFisicoDet agregaDetalleStkFisicoDet(String pro_id) throws SQLException {
        String SQL_INVFISICODET = "select t.pro_key,t.pro_id,t.pro_des,t.pro_provid prov_id, to_number(t.pro_provid) prov_key,"
                + " pack_tproveedores.f_001_descProveedor(t.pro_provid) prov_razsoc, t.pro_presminven from tproductos t  where t.pro_id='" + pro_id + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_INVFISICODET);
            while (rs.next()) {
                objInvFisicoDet = new InvFisicoDet();
                objInvFisicoDet.setPro_key(rs.getLong("pro_key"));
                objInvFisicoDet.setPro_id(rs.getString("pro_id"));
                objInvFisicoDet.setPro_des(rs.getString("pro_des"));
                objInvFisicoDet.setPro_presminven(rs.getInt("pro_presminven"));
                objInvFisicoDet.setProv_key(rs.getLong("prov_key"));
                objInvFisicoDet.setProv_id(rs.getString("prov_id"));
                objInvFisicoDet.setProv_razsoc(rs.getString("prov_razsoc"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstInvFisicoCab.getSize() + " registro(s)");
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
        return objInvFisicoDet;
    }

}
