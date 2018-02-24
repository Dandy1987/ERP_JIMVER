package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.PerPago;
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
public class DaoPerPago {

    //Instancias de Objetos
    ListModelList<PerPago> objListPerPago;
    PerPago objPerPago;

    //Variables Públicas
    public static String P_WHERE = "";
    public static String P_WHERE_R = "";
    int i_flagErrorBD = 0;
    String s_msg = "";

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //Variables Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoPerPago.class);

    public ParametrosSalida insertarPerPago(PerPago objPerPago) throws SQLException, ParseException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_PERPAGO = "{call pack_tperpag.p_insertarPerPago(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PERPAGO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, objPerPago.getPerPag_id());
            cst.setString(3, objPerPago.getPerPag_desc());
            cst.setDate(4, new java.sql.Date(objPerPago.getPerPag_fecIni().getTime()));
            cst.setDate(5, new java.sql.Date(objPerPago.getPerPag_fecFin().getTime()));
            cst.setString(6, objUsuCredential.getCuenta());
            cst.setString(7, objUsuCredential.getComputerName());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(8);
            s_msg = cst.getString(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objPerPago.getPerPag_desc());
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

    public ParametrosSalida editarPerPago(PerPago objPerPago) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_PERPAGO = "{call pack_tperpag.p_modificarPerPago(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PERPAGO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objPerPago.getPerPag_estado());
            cst.setString(3, objPerPago.getPerPag_id());
            cst.setString(4, objPerPago.getPerPag_desc());
            cst.setDate(5, new java.sql.Date(objPerPago.getPerPag_fecIni().getTime()));
            cst.setDate(6, new java.sql.Date(objPerPago.getPerPag_fecFin().getTime()));
            cst.setString(7, objUsuCredential.getCuenta());
            cst.setString(8, objUsuCredential.getComputerName());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(9);
            s_msg = cst.getString(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objPerPago.getPerPag_desc());
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

    public ListModelList<PerPago> busquedaPeriodoPago(String s_consulta) throws SQLException {
        String S_SQL_BUSQUEDA_PER_PAGO;

        S_SQL_BUSQUEDA_PER_PAGO = "select * from v_listaperiodopago l where l.emp_id = '" + objUsuCredential.getCodemp() + "' and l.plppag_id like '%" + s_consulta + "' order by l.plppag_id desc";

        P_WHERE = S_SQL_BUSQUEDA_PER_PAGO;
        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(P_WHERE);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPerPag_id(rs.getString("plppag_id"));
                objPerPago.setPerPag_desc(rs.getString("plppag_desc"));
                objPerPago.setPerPag_fecIni(rs.getDate("plppag_feci"));
                objPerPago.setPerPag_fecFin(rs.getDate("plppag_fecf"));
                objPerPago.setPerPag_situ(rs.getString("plppag_situ"));
                objPerPago.setPerPag_usuAdd(rs.getString("plppag_usuadd"));
                //objPerPago.setPerPag_fecAdd(rs.getDate("plppag_fecadd"));
                objPerPago.setPerPag_fecAdd(rs.getTimestamp("plppag_fecadd"));
                objPerPago.setPerPag_usuMod(rs.getString("plppag_usumod"));
                objPerPago.setPerPag_fecMod(rs.getTimestamp("plppag_fecmod"));
                //objPerPago.setPerPag_fecMod(rs.getDate("plppag_fecmod"));
                objPerPago.setPerPag_Estado(rs.getInt("plppag_estado"));
                objPerPago.setTab_id(rs.getString("tab_id"));
                objPerPago.setTab_descri(rs.getString("tab_descri"));
                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
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
        return objListPerPago;
    }

    ///////////////para peridodo anterior
    public ListModelList<PerPago> busquedaPeriodoAnterior(String s_consulta) throws SQLException {
        String S_SQL_BUSQUEDA_PER_PAGO;

        S_SQL_BUSQUEDA_PER_PAGO = "select * from v_listaperiodoAnterior l where l.emp_id = '" + objUsuCredential.getCodemp() + "' and l.plppag_id like '%" + s_consulta + "' order by l.plppag_id desc";

        P_WHERE = S_SQL_BUSQUEDA_PER_PAGO;
        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(P_WHERE);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPerPag_id(rs.getString("plppag_id"));
                objPerPago.setPerPag_desc(rs.getString("plppag_desc"));
                objPerPago.setPerPag_fecIni(rs.getDate("plppag_feci"));
                objPerPago.setPerPag_fecFin(rs.getDate("plppag_fecf"));
                objPerPago.setPerPag_situ(rs.getString("plppag_situ"));
                objPerPago.setPerPag_usuAdd(rs.getString("plppag_usuadd"));
                //objPerPago.setPerPag_fecAdd(rs.getDate("plppag_fecadd"));
                objPerPago.setPerPag_fecAdd(rs.getTimestamp("plppag_fecadd"));
                objPerPago.setPerPag_usuMod(rs.getString("plppag_usumod"));
                objPerPago.setPerPag_fecMod(rs.getTimestamp("plppag_fecmod"));
                //objPerPago.setPerPag_fecMod(rs.getDate("plppag_fecmod"));
                objPerPago.setPerPag_Estado(rs.getInt("plppag_estado"));
                objPerPago.setTab_id(rs.getString("tab_id"));
                objPerPago.setTab_descri(rs.getString("tab_descri"));
                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
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
        return objListPerPago;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<PerPago> listaTipPeriodo() throws SQLException {
        String SQL;

        SQL = "select t.tabla_id, t.tabla_id || ' - ' || t.tabla_descri tipoDescri from tpltablas1 t where "
                + " t.tabla_cod = '00004' and t.tabla_id <> '000' and t.tabla_estado = 1 order by t.tabla_id";

        P_WHERE_R = SQL;
        try {
            objListPerPago = new ListModelList<PerPago>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(P_WHERE_R);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setTab_id(rs.getString("tabla_id"));
                objPerPago.setTab_descri(rs.getString("tipoDescri"));
                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " Registros(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga debido al Error" + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objListPerPago;
    }

    //para hallar periodo en proceso
    public ListModelList<PerPago> busquedaProceso() throws SQLException {
        String query;

        query = " select t.plppag_id"
                + " from tplperpag t"
                + " where t.emp_id='" + objUsuCredential.getCodemp() + "'"
                + " and t.plppag_estado=1"
                + " and t.plppag_situ=1"
                + " order by t.plppag_id desc";

        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPeriodoProceso(rs.getString("plppag_id"));

                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
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
        return objListPerPago;
    }

    public int validaPeriodo(int empresa, String periodo) throws SQLException {
        int valida = 0;
        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call codijisa.pack_tperpag.f_perpro(?,?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, empresa);
            cst.setString(3, periodo);
            cst.execute();
            valida = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return valida;
    }

    /**
     * Metodo que devuelve el periodo en proceso segun el codigo de la empresa
     * Llama a funcion call pack_tperpag.f_periodo_proceso(?)
     *
     * @param i_cod_empresa Codigo de la empresa
     * @return periodo en proceso formato yyyymmtt
     * @throws SQLException
     */
    public String getPeriodoProceso(int i_cod_empresa) throws SQLException {
        String s_periodo_proceso = null;
        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call pack_tperpag.f_periodo_proceso(?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, i_cod_empresa);
            cst.execute();
            s_periodo_proceso = cst.getString(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_periodo_proceso;
    }

    /**
     * Metodo que devuelve el periodo calculado segun el codigo de la empresa
     * Llama a funcion call pack_tperpag.f_periodo_calculado(?)
     *
     * @param i_cod_empresa Codigo de la empresa
     * @return periodo calculado
     * @throws SQLException
     */
    public String getPeriodoCalculado(int i_cod_empresa) throws SQLException {
        String s_periodo_calculado = null;
        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call pack_tperpag.f_periodo_calculado(?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, i_cod_empresa);
            cst.execute();
            s_periodo_calculado = cst.getString(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_periodo_calculado;

    }

    //para hallar periodo en cerrado con situacion 3
    public ListModelList<PerPago> busquedaPeriodoCerrado() throws SQLException {
        String query;

        query = " select r.plppag_id from ("
                + " select t.plppag_id from tplperpag t"
                + " where t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.plppag_estado=1"
                + " and t.plppag_situ in ('3','4')"
                + " union"
                + " select '' from tplperpag t"
                + " where t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.plppag_estado=1"
                + " and t.plppag_situ in ('3','4')) r"
                + " order by r.plppag_id desc";
        /*" select t.plppag_id"
         + " from tplperpag t"
         + " where t.emp_id='" + objUsuCredential.getCodemp() + "'"
         + " and t.plppag_estado=1"
         + " and t.plppag_situ=3"
         + " order by t.plppag_id desc";*/

        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPeriodoProceso(rs.getString("plppag_id"));

                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
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
        return objListPerPago;
    }

    /**
     * Método que ejecuta un procedimiento pack_tcalculoplla.p_ajuonp de la DB
     * para ajustar el S.S.P. de la planilla seleccionada. - chuallpa
     * @param plppag_id: Parámetro del periodo de planilla
     * @return Retorna un mensaje de validación
     * @throws SQLException
     */
    public String ajusteSSP(String plppag_id) throws SQLException {
        String query, mnsj = "";
        query = "{call pack_tcalculoplla.p_ajuonp(?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, plppag_id);
            cst.execute();
            mnsj = "Ajuste de S.S.P. con éxito";
        } catch (SQLException e) {
            mnsj = "Error al ajustar S.S.P. debido al Error " + e.toString();
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }

        return mnsj;
    }

    /**
     * Método para cargar datos a un componente de selección para revertir el
     * cierre de planilla. - chuallpa
     * @return Retorna una lista con los periodos de pago en situación de
     * cerrado.
     * @throws SQLException
     */
    public ListModelList<PerPago> busquedaPerRevCierre() throws SQLException {
        String query;
        query = " select r.plppag_id \n"
                + " from ( \n"
                + " select t.plppag_id \n"
                + " from tplperpag t \n"
                + " where t.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + " and t.plppag_estado = 1 \n"
                + " and t.plppag_situ in ('3') \n"
                + " union \n"
                + " select '' \n"
                + " from tplperpag t \n"
                + " where t.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + " and t.plppag_estado = 1 \n"
                + " and t.plppag_situ in ('3') \n"
                + " ) r \n"
                + " order by r.plppag_id desc";

        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPeriodoProceso(rs.getString("plppag_id"));
                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
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
        return objListPerPago;
    }

    /**
     * Método para obtener el id y descripción del periodo de pago. - chuallpa
     * @param plppag_id Parametro id del periodo de pago.
     * @return El id y descripción del periodo de pago.
     * @throws SQLException 
     */
    public PerPago descPerPago(String plppag_id) throws SQLException {
        String query = "";
        query = " select p.plppag_id, p.plppag_desc from tplperpag p\n"
                + " where p.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + " and p.plppag_estado = '1'\n"
                + " and p.plppag_situ = '3'\n"
                + " and p.plppag_id = " + plppag_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPeriodoProceso(rs.getString("plppag_id"));
                objPerPago.setPerPag_desc(rs.getString("plppag_desc"));
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Búsqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }

        return objPerPago;
    }

}
