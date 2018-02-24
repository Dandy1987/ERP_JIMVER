package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.ProgramacionReparto;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoProgramacionReparto {

    //Instancia de Objetos
    ListModelList<ProgramacionReparto> objlstProgReparto;
    ProgramacionReparto objProgReparto;
    //Variables Publicas
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    CallableStatement cst = null;
    int i_flagErrorBD;
    String s_msg;
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoProgramacionReparto.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarProgramacionReparto(ProgramacionReparto objProgReparto) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_ASIGREPARTO = "{call pack_tprogramacion_reparto.p_insertarProgReparto(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_ASIGREPARTO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, objProgReparto.getProgrep_transid());
            cst.setString(4, objProgReparto.getProgrep_chofid());
            cst.setString(5, objProgReparto.getProgrep_repartid());
            cst.setString(6, objProgReparto.getProgrep_horaid());
            cst.setDate(7, new java.sql.Date(objProgReparto.getProgrep_fecha().getTime()));
            cst.setInt(8, objProgReparto.getProgrep_estado());
            cst.setString(9, objProgReparto.getProgrep_usuadd());
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con codigo " + objProgReparto.getProgrep_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarProgramacionReparto(ProgramacionReparto objProgReparto) throws SQLException {        
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_ASIGREPARTO = "{call pack_tprogramacion_reparto.p_actualizarProgReparto(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_ASIGREPARTO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(3, objProgReparto.getProgrep_key());
            cst.setString(4, objProgReparto.getProgrep_transid());
            cst.setString(5, objProgReparto.getProgrep_chofid());
            cst.setString(6, objProgReparto.getProgrep_repartid());
            cst.setString(7, objProgReparto.getProgrep_horaid());
            cst.setDate(8, new java.sql.Date(objProgReparto.getProgrep_fecha().getTime()));
            cst.setInt(9, objProgReparto.getProgrep_estado());
            cst.setString(10, objProgReparto.getProgrep_usumod());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + objProgReparto.getProgrep_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarProgramacionReparto(ProgramacionReparto objAsignacionReparto) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_ASIGREPARTO = "{call pack_tprogramacion_reparto.p_eliminarProgReparto(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_ASIGREPARTO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(3, objAsignacionReparto.getProgrep_key());
            cst.setString(4, objUsuCredential.getCuenta());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con codigo " + objAsignacionReparto.getProgrep_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida duplicarDia(Date fecduplicar, Date fecnueva) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_DUPLICAR_PROGREPARTO = "{call pack_tprogramacion_reparto.p_duplicarDia(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DUPLICAR_PROGREPARTO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setDate(3, new java.sql.Date(fecduplicar.getTime()));
            cst.setDate(4, new java.sql.Date(fecnueva.getTime()));
            cst.setString(5, objUsuCredential.getCuenta());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con la fecha " + new SimpleDateFormat("dd/MM/yyyy").format(fecduplicar));
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<ProgramacionReparto> listaProgramacionReparto(int estado) throws SQLException {
        
        String SQL_LISTA_ASIGREPARTO;
        if (estado == 0) {
            SQL_LISTA_ASIGREPARTO = "select * from v_listaprogramacionreparto t where t.progrep_estado<>" + estado + " and "
                    + " t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and t.progrep_fecasig = trunc(sysdate) "
                    + " order by t.progrep_transid";
        } else {
            SQL_LISTA_ASIGREPARTO = "select * from v_listaprogramacionreparto t where t.progrep_estado=" + estado + " and "
                    + " t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and t.progrep_fecasig = trunc(sysdate) "
                    + " order by t.progrep_transid";
        }

        P_WHERE = SQL_LISTA_ASIGREPARTO;
        objlstProgReparto = new ListModelList<ProgramacionReparto>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_ASIGREPARTO);
            while (rs.next()) {
                objProgReparto = new ProgramacionReparto();
                objProgReparto.setEmp_id(rs.getInt("emp_id"));
                objProgReparto.setSuc_id(rs.getInt("suc_id"));
                objProgReparto.setProgrep_key(rs.getInt("progrep_key"));
                objProgReparto.setProgrep_id(rs.getString("progrep_id"));
                objProgReparto.setProgrep_transid(rs.getString("progrep_transid"));
                objProgReparto.setProgrep_transdes(rs.getString("progrep_transdes"));
                objProgReparto.setProgrep_chofid(rs.getString("progrep_chofid"));
                objProgReparto.setProgrep_chofdes(rs.getString("progrep_chofdes"));
                objProgReparto.setProgrep_repartid(rs.getString("progrep_repartid"));
                objProgReparto.setProgrep_repartdes(rs.getString("progrep_repartdes"));
                objProgReparto.setProgrep_horaid(rs.getString("progrep_horaid"));
                objProgReparto.setProgrep_horades(rs.getString("progrep_horades"));
                objProgReparto.setProgrep_fecha(rs.getDate("progrep_fecasig"));
                objProgReparto.setProgrep_estado(rs.getInt("progrep_estado"));
                objProgReparto.setProgrep_usuadd(rs.getString("progrep_usuadd"));
                objProgReparto.setProgrep_fecadd(rs.getTimestamp("progrep_fecadd"));
                objProgReparto.setProgrep_usumod(rs.getString("progrep_usumod"));
                objProgReparto.setProgrep_fecmod(rs.getTimestamp("progrep_fecmod"));
                objlstProgReparto.add(objProgReparto);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgReparto.getSize() + " registro(s)");
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
        return objlstProgReparto;
    }

    public ListModelList<ProgramacionReparto> busquedaProgramacionReparto(int emp_id, int suc_id, String periodo, String fecha) throws SQLException {
        String SQL_LISTA_ASIGREPARTO/*SQL_BUSQUEDA_PROGREP*/, s_validafecha;
        s_validafecha = fecha.isEmpty() ? "" : " and t.progrep_fecasig = to_date('" + fecha + "', 'dd/mm/yyyy') ";

        SQL_LISTA_ASIGREPARTO = " select * from v_listaprogramacionreparto t where t.progrep_estado<>0"
                + " and t.progrep_periodo = '" + periodo + "' "
                + " and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + " and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                + s_validafecha
                + " order by t.progrep_transid";
        P_WHERE = SQL_LISTA_ASIGREPARTO;
        objlstProgReparto = new ListModelList<ProgramacionReparto>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_ASIGREPARTO);
            while (rs.next()) {
                objProgReparto = new ProgramacionReparto();
                objProgReparto.setEmp_id(rs.getInt("emp_id"));
                objProgReparto.setSuc_id(rs.getInt("suc_id"));
                objProgReparto.setProgrep_key(rs.getInt("progrep_key"));
                objProgReparto.setProgrep_id(rs.getString("progrep_id"));
                objProgReparto.setProgrep_transid(rs.getString("progrep_transid"));
                objProgReparto.setProgrep_transdes(rs.getString("progrep_transdes"));
                objProgReparto.setProgrep_chofid(rs.getString("progrep_chofid"));
                objProgReparto.setProgrep_chofdes(rs.getString("progrep_chofdes"));
                objProgReparto.setProgrep_repartid(rs.getString("progrep_repartid"));
                objProgReparto.setProgrep_repartdes(rs.getString("progrep_repartdes"));
                objProgReparto.setProgrep_horaid(rs.getString("progrep_horaid"));
                objProgReparto.setProgrep_horades(rs.getString("progrep_horades"));
                objProgReparto.setProgrep_fecha(rs.getDate("progrep_fecasig"));
                objProgReparto.setProgrep_estado(rs.getInt("progrep_estado"));
                objProgReparto.setProgrep_usuadd(rs.getString("progrep_usuadd"));
                objProgReparto.setProgrep_fecadd(rs.getTimestamp("progrep_fecadd"));
                objProgReparto.setProgrep_usumod(rs.getString("progrep_usumod"));
                objProgReparto.setProgrep_fecmod(rs.getTimestamp("progrep_fecmod"));
                objlstProgReparto.add(objProgReparto);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProgReparto.getSize() + " registro(s)");
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
        return objlstProgReparto;
    }

}
