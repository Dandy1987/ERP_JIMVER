package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.planillas.mantenimiento.Util;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author acaro
 */
public class DaoUtil {

    //Instancias de Objetos
    ListModelList<Util> objLstUtil;
    Util objUtil;
    ListModelList<ManPer> objLisManPer;
    ManPer objManPer;

    //Variables Públicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    public static String P_WHERE = "";
    int i_flagErrorBD = 0;
    String s_msg = "";

    //Variables Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    private static final Logger LOGGER = Logger.getLogger(DaoUtil.class);

    public ParametrosSalida insertarUtil(Util objUtil) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_UTIL = "{call pack_tutilidad.p_insertarUtil(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_UTIL);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, objUtil.getpUtil_id());
            cst.setDouble(3, objUtil.getpUtil_parLeg());
            cst.setDouble(4, objUtil.getpUtil_totRem());
            cst.setDouble(5, objUtil.getpUtil_facRem());
            cst.setDouble(6, objUtil.getpUtil_totDiasLab());
            cst.setDouble(7, objUtil.getpUtil_facTpoLab());
            cst.setString(8, objUsuCredential.getCuenta());
            cst.setString(9, objUsuCredential.getComputerName());
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(10);
            s_msg = cst.getString(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objUtil.getpUtil_id());
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

    public ParametrosSalida editarUtil(Util objUtil_1) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_EDITAR_PERPAGO = "{call pack_tutilidad.p_editarUtil(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_EDITAR_PERPAGO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUtil_1.getpUtil_estado());
            cst.setString(3, objUtil_1.getpUtil_id());
            cst.setDouble(4, objUtil_1.getpUtil_parLeg());
            cst.setDouble(5, objUtil_1.getpUtil_totRem());
            cst.setDouble(6, objUtil_1.getpUtil_totDiasLab());
            cst.setDouble(7, objUtil_1.getpUtil_facRem());
            cst.setDouble(8, objUtil_1.getpUtil_facTpoLab());
            cst.setString(9, objUsuCredential.getCuenta());
            cst.setString(10, objUsuCredential.getComputerName());
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(11);
            s_msg = cst.getString(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objUtil_1.getpUtil_id());
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Util> listaUtil() throws SQLException {
        String S_SQL_BUSQUEDA_UTIL;
        S_SQL_BUSQUEDA_UTIL = "select * from v_listautil u where u.emp_id = '" + objUsuCredential.getCodemp() + "' order by u.pluti_id desc";
        P_WHERE = S_SQL_BUSQUEDA_UTIL;
        objLstUtil = new ListModelList<Util>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(P_WHERE);
            while (rs.next()) {
                objUtil = new Util();
                objUtil.setpUtil_id(rs.getString("pluti_id"));
                objUtil.setpUtil_parLeg(rs.getDouble("pluti_parleg"));
                objUtil.setpUtil_totRem(rs.getDouble("pluti_totrem"));
                objUtil.setpUtil_totDiasLab(rs.getDouble("pluti_totdiaslab"));
                objUtil.setpUtil_facRem(rs.getDouble("pluti_facrem"));
                objUtil.setpUtil_facTpoLab(rs.getDouble("pluti_factpolab"));
                objUtil.setpUtil_estado(rs.getInt("pluti_estado"));
                objUtil.setpUtil_usuAdd(rs.getString("pluti_usuadd"));
                objUtil.setpUtil_fecAdd(rs.getTimestamp("pluti_fecadd"));
                objUtil.setpUtil_usuMod(rs.getString("pluti_usumod"));
                objUtil.setpUtil_fecMod(rs.getTimestamp("pluti_fecmod"));
                objLstUtil.add(objUtil);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLstUtil.getSize() + " registros(s)");
        } catch (SQLException e) {
            Messagebox.show(" Error de Búsqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objLstUtil;
    }

    public ListModelList<Util> listaPeriodoUtil() throws SQLException {
        String sql;
        sql = "select substr(p.plppag_id, 1, 4)-1 from tplperpag p where p.emp_id = '" + objUsuCredential.getCodemp() + "' and p.plppag_situ <> '4' and p.plppag_estado in ('1', '3') and p.plppag_id like '%____01' group by substr(p.plppag_id, 1, 4)-1 order by 1 desc";
        try {
            objLstUtil = new ListModelList<Util>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objUtil = new Util();
                objUtil.setpUtil_id(rs.getString(1));
                objLstUtil.add(objUtil);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objLisManPer.getSize() + " registro(s)");
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
        return objLstUtil;
    }

    public String validarSituacionUtil(String periodoUtil) throws SQLException {
        String sql, consulta = "";
        sql = "select p.plppag_situ from tplperpag p where p.emp_id = '" + objUsuCredential.getCodemp() + "' and p.plppag_estado = '1' and p.plppag_id like '%______01' and substr(p.plppag_id, 1, 4) = '" + periodoUtil + "' ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                consulta = rs.getString(1);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + consulta + " registro(s)");
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
        return consulta;
    }

}
