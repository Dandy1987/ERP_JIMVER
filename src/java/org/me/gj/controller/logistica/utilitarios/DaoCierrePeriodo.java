package org.me.gj.controller.logistica.utilitarios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.utilitarios.CierrePeriodo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCierrePeriodo {

    //Instancias de Objetos
    ListModelList<CierrePeriodo> objlstCierrePeriodo;
    CierrePeriodo objCierrePeriodo;
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
    private static final Logger LOGGER = Logger.getLogger(DaoCierrePeriodo.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida CerrarPeriodoLogistica(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarPeriodoLogistica(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el Periodo " + objCierrePeriodo.getPer_id());
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

    public ParametrosSalida AbriPeriodoLogistica(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirPeriodoLogistica(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierrePeriodo.getPer_id());
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

    public ParametrosSalida CerrarPeriodoFacturacion(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarPeriodoFacturacion(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el Periodo " + objCierrePeriodo.getPer_id());
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

    public ParametrosSalida AbriPeriodoFacturacion(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirPeriodoFacturacion(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierrePeriodo.getPer_id());
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

    public ParametrosSalida CerrarPeriodoCxc(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarPeriodoCxc(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el Periodo " + objCierrePeriodo.getPer_id());
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

    public ParametrosSalida AbriPeriodoCxc(CierrePeriodo objCierrePeriodo) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirPeriodoCxc(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierrePeriodo.getEmp_id());
            cst.setInt(2, objCierrePeriodo.getSuc_id());
            cst.setString(3, objCierrePeriodo.getPer_id());
            cst.setString(4, objCierrePeriodo.getCieper_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierrePeriodo.getPer_id());
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
    public ListModelList<CierrePeriodo> listaCierrePeriodo(String anio) throws SQLException {
        String SQL_CIERRES = "select * from tcierreperiodos t where t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' and substr(t.per_id,1,4)= '" + anio + "' order by t.per_id";
        objlstCierrePeriodo = new ListModelList<CierrePeriodo>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CIERRES);
            while (rs.next()) {
                objCierrePeriodo = new CierrePeriodo();
                objCierrePeriodo.setPer_id(rs.getString("per_id"));
                objCierrePeriodo.setPer_key(rs.getInt("per_key"));
                objCierrePeriodo.setSuc_id(rs.getInt("suc_id"));
                objCierrePeriodo.setEmp_id(rs.getInt("emp_id"));
                objCierrePeriodo.setCieper_log(rs.getInt("cieper_log"));
                objCierrePeriodo.setCieper_fac(rs.getInt("cieper_fac"));
                objCierrePeriodo.setCieper_cxc(rs.getInt("cieper_cxc"));
                objCierrePeriodo.setCieper_dis(rs.getInt("cieper_dis"));
                objCierrePeriodo.setCieper_cxp(rs.getInt("cieper_cxp"));
                objCierrePeriodo.setCieper_ban(rs.getInt("cieper_ban"));
                objCierrePeriodo.setCieper_caj(rs.getInt("cieper_caj"));
                objCierrePeriodo.setCieper_con(rs.getInt("cieper_con"));
                objCierrePeriodo.setCieper_pla(rs.getInt("cieper_pla"));
                objCierrePeriodo.setCieper_seg(rs.getInt("cieper_seg"));
                objCierrePeriodo.setCieper_fin(rs.getInt("cieper_fin"));
                objCierrePeriodo.setCieper_est(rs.getInt("cieper_est"));
                objCierrePeriodo.setCieper_usuadd(rs.getString("cieper_usuadd"));
                objCierrePeriodo.setCieper_fecadd(rs.getTimestamp("cieper_fecadd"));
                objCierrePeriodo.setCieper_usumod(rs.getString("cieper_usumod"));
                objCierrePeriodo.setCieper_fecmod(rs.getTimestamp("cieper_fecmod"));
                objlstCierrePeriodo.add(objCierrePeriodo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCierrePeriodo.getSize() + " registro(s)");
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
        return objlstCierrePeriodo;
    }

}
