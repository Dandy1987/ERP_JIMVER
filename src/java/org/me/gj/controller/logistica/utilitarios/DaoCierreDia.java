package org.me.gj.controller.logistica.utilitarios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.utilitarios.CierreDia;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCierreDia {

    //Instancias de Objetos
    ListModelList<CierreDia> objlstCierreDias;
    CierreDia objCierreDias;
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
    private static final Logger LOGGER = Logger.getLogger(DaoCierreDia.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida CerrarDiaLogistica(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarDiaLogistica(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el dia " + objCierreDias.getDias_fec());
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

    public ParametrosSalida AbriDiaLogistica(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirDiaLogistica(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierreDias.getDias_fec());
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

    public ParametrosSalida CerrarDiaFacturacion(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarDiaFacturacion(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el dia " + objCierreDias.getDias_fec());
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

    public ParametrosSalida AbriDiaFacturacion(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirDiaFacturacion(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierreDias.getDias_fec());
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

    public ParametrosSalida CerrarDiaCxc(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_CERRAR_DIA = "{call pack_tcierre.p_cerrarDiaCxc(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_CERRAR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro el dia " + objCierreDias.getDias_fec());
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

    public ParametrosSalida AbriDiaCxc(CierreDia objCierreDias) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ABRIR_DIA = "{call pack_tcierre.p_abrirDiaCxc(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ABRIR_DIA);
            cst.clearParameters();
            cst.setInt(1, objCierreDias.getEmp_id());
            cst.setInt(2, objCierreDias.getSuc_id());
            cst.setString(3, objCierreDias.getDias_fec());
            cst.setString(4, objCierreDias.getCiedia_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | revirtio cierre del dia " + objCierreDias.getDias_fec());
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
    public ListModelList<CierreDia> listaCierreDias(String periodo) throws SQLException {
        String SQL_CIERRES = "select * from tcierredias t where t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' and to_char(t.dia_fec,'yyyymm')='" + periodo + "' order by t.dia_fec";
        objlstCierreDias = new ListModelList<CierreDia>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CIERRES);
            while (rs.next()) {
                objCierreDias = new CierreDia();
                objCierreDias.setDia_fec(rs.getDate("dia_fec"));
                objCierreDias.setSuc_id(rs.getInt("suc_id"));
                objCierreDias.setEmp_id(rs.getInt("emp_id"));
                objCierreDias.setCiedia_estado(rs.getInt("ciedia_estado"));
                objCierreDias.setCiedia_log(rs.getInt("ciedia_log"));
                objCierreDias.setCiedia_cxc(rs.getInt("ciedia_cxc"));
                objCierreDias.setCiedia_dis(rs.getInt("ciedia_dis"));
                objCierreDias.setCiedia_fac(rs.getInt("ciedia_fac"));
                objCierreDias.setCiedia_cxp(rs.getInt("ciedia_cxp"));
                objCierreDias.setCiedia_ban(rs.getInt("ciedia_ban"));
                objCierreDias.setCiedia_caj(rs.getInt("ciedia_caj"));
                objCierreDias.setCiedia_con(rs.getInt("ciedia_con"));
                objCierreDias.setCiedia_pla(rs.getInt("ciedia_pla"));
                objCierreDias.setCiedia_seg(rs.getInt("ciedia_seg"));
                objCierreDias.setCiedia_fin(rs.getInt("ciedia_fin"));
                objCierreDias.setCiedia_fer(rs.getInt("ciedia_fer"));
                objCierreDias.setCiedia_ped(rs.getInt("ciedia_ped"));
                objCierreDias.setCiedia_labpresu(rs.getInt("ciedia_labpresu"));
                objCierreDias.setCiedia_labasis(rs.getInt("ciedia_labasis"));
                objCierreDias.setCiedia_est(rs.getInt("ciedia_est"));
                objCierreDias.setCiedia_usuadd(rs.getString("ciedia_usuadd"));
                objCierreDias.setCiedia_fecadd(rs.getTimestamp("ciedia_fecadd"));
                objCierreDias.setCiedia_usumod(rs.getString("ciedia_usumod"));
                objCierreDias.setCiedia_fecmod(rs.getTimestamp("ciedia_fecmod"));
                objlstCierreDias.add(objCierreDias);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCierreDias.getSize() + " registro(s)");
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
        return objlstCierreDias;
    }

    public CierreDia ValidaDia(String fecemi, String modulo) throws SQLException {
        String SQL_CIERRES;
        SQL_CIERRES = "select * from tcierredias t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " "
                + " and t.dia_fec = to_date('" + fecemi + "','dd/mm/yyyy')";
        objCierreDias = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CIERRES);
            while (rs.next()) {
                objCierreDias = new CierreDia();
                objCierreDias.setDia_fec(rs.getDate("dia_fec"));
                objCierreDias.setSuc_id(rs.getInt("suc_id"));
                objCierreDias.setEmp_id(rs.getInt("emp_id"));
                objCierreDias.setCiedia_estado(rs.getInt("ciedia_estado"));
                objCierreDias.setCiedia_log(rs.getInt("ciedia_log"));
                objCierreDias.setCiedia_cxc(rs.getInt("ciedia_cxc"));
                objCierreDias.setCiedia_dis(rs.getInt("ciedia_dis"));
                objCierreDias.setCiedia_fac(rs.getInt("ciedia_fac"));
                objCierreDias.setCiedia_cxp(rs.getInt("ciedia_cxp"));
                objCierreDias.setCiedia_ban(rs.getInt("ciedia_ban"));
                objCierreDias.setCiedia_caj(rs.getInt("ciedia_caj"));
                objCierreDias.setCiedia_con(rs.getInt("ciedia_con"));
                objCierreDias.setCiedia_pla(rs.getInt("ciedia_pla"));
                objCierreDias.setCiedia_seg(rs.getInt("ciedia_seg"));
                objCierreDias.setCiedia_fin(rs.getInt("ciedia_fin"));
                objCierreDias.setCiedia_fer(rs.getInt("ciedia_fer"));
                objCierreDias.setCiedia_ped(rs.getInt("ciedia_ped"));
                objCierreDias.setCiedia_labpresu(rs.getInt("ciedia_labpresu"));
                objCierreDias.setCiedia_labasis(rs.getInt("ciedia_labasis"));
                objCierreDias.setCiedia_est(rs.getInt("ciedia_est"));
                objCierreDias.setCiedia_usuadd(rs.getString("ciedia_usuadd"));
                objCierreDias.setCiedia_fecadd(rs.getTimestamp("ciedia_fecadd"));
                objCierreDias.setCiedia_usumod(rs.getString("ciedia_usumod"));
                objCierreDias.setCiedia_fecmod(rs.getTimestamp("ciedia_fecmod"));
                //objlstCierreDias.add(objCierreDias);
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCierreDias.getSize() + " registro(s)");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado de Cierre Dias");
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
        return objCierreDias;
    }
}
