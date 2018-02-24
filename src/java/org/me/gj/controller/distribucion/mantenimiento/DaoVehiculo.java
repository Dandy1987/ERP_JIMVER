package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.distribucion.mantenimiento.HistoPropietario;
import org.me.gj.model.distribucion.mantenimiento.Propietario;
import org.me.gj.model.distribucion.mantenimiento.Soat;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoVehiculo {

    //Instancia de Objetos
    ListModelList<Vehiculo> objlstVehiculo;
    ListModelList<Soat> objlstSoat;
    ListModelList<HistoPropietario> objlstHistoPropietario;
    Vehiculo objVehiculo;
    Propietario objPropietario;
    Soat objSoat;
    HistoPropietario objHistoPropietario;
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
    private static final Logger LOGGER = Logger.getLogger(DaoVehiculo.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarVehiculo(Vehiculo objVehiculo, Object[][] lishispropietario, Object[][] lissoat) throws SQLException, ParseException {
        String SQL_INSERTAR_VEHICULO = "{call PACK_TTRANSPORTE.p_insertarVehiculo(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ARRAY a_hisprop, a_soat;
        ArrayDescriptor ad_hisprop, ad_soat;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_VEHICULO);
            ad_hisprop = ArrayDescriptor.createDescriptor("TYLIS_THIST_PROP", con.getMetaData().getConnection());
            a_hisprop = new ARRAY(ad_hisprop, con.getMetaData().getConnection(), lishispropietario);
            ad_soat = ArrayDescriptor.createDescriptor("TYLIS_TSOAT", con.getMetaData().getConnection());
            a_soat = new ARRAY(ad_soat, con.getMetaData().getConnection(), lissoat);
            cst.clearParameters();
            cst.setInt(1, objVehiculo.getTrans_sucid());
            cst.setInt(2, objVehiculo.getTrans_empid());
            cst.setString(3, objVehiculo.getTrans_alias());
            cst.setString(4, objVehiculo.getTrans_placa());
            cst.setInt(5, objVehiculo.getTrans_marca());
            cst.setInt(6, objVehiculo.getTrans_combustible());
            cst.setInt(7, objVehiculo.getTrans_categoria());
            cst.setInt(8, objVehiculo.getTrans_modelo());
            cst.setInt(9, objVehiculo.getTrans_color());
            cst.setInt(10, objVehiculo.getTrans_transmision());
            cst.setInt(11, objVehiculo.getTrans_carroceria());
            cst.setString(12, objVehiculo.getTrans_vin());
            cst.setString(13, objVehiculo.getTrans_chasis());
            cst.setString(14, objVehiculo.getTrans_motor());
            cst.setInt(15, objVehiculo.getTrans_aniofab());
            cst.setInt(16, objVehiculo.getTrans_aniomodelo());
            cst.setString(17, objVehiculo.getTrans_version());
            cst.setInt(18, objVehiculo.getTrans_ejes());
            cst.setInt(19, objVehiculo.getTrans_asientos());
            cst.setInt(20, objVehiculo.getTrans_pasajeros());
            cst.setInt(21, objVehiculo.getTrans_ruedas());
            cst.setString(22, objVehiculo.getTrans_potencia());
            cst.setInt(23, objVehiculo.getTrans_cilindros());
            cst.setDouble(24, objVehiculo.getTrans_cilindrada());
            cst.setString(25, objVehiculo.getTrans_traccion());
            cst.setDouble(26, objVehiculo.getTrans_pesobruto());
            cst.setDouble(27, objVehiculo.getTrans_pesoneto());
            cst.setDouble(28, objVehiculo.getTrans_cargautil());
            cst.setDouble(29, objVehiculo.getTrans_largo());
            cst.setDouble(30, objVehiculo.getTrans_altura());
            cst.setDouble(31, objVehiculo.getTrans_ancho());
            cst.setString(32, objVehiculo.getTrans_img());
            cst.setDate(33, new java.sql.Date(objVehiculo.getTrans_fecing().getTime()));
            cst.setDate(34, new java.sql.Date(objVehiculo.getTrans_fecsal().getTime()));
            cst.setInt(35, objVehiculo.getTrans_ord());
            cst.setString(36, objVehiculo.getTrans_nomrep());
            cst.setString(37, objVehiculo.getTrans_usuadd());
            cst.setArray(38, a_hisprop);
            cst.setArray(39, a_soat);
            cst.registerOutParameter(40, java.sql.Types.NUMERIC);
            cst.registerOutParameter(41, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(41);
            i_flagErrorBD = cst.getInt(40);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objVehiculo.getTrans_alias());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarVehiculo(Vehiculo objVehiculo, Object[][] lishispropietario, Object[][] lissoat) throws SQLException, ParseException {
        String SQL_ACTUALIZAR_VEHICULO = "{call PACK_TTRANSPORTE.p_actualizarVehiculo(?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        ARRAY a_hisprop, a_soat;
        ArrayDescriptor ad_hisprop, ad_soat;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_VEHICULO);
            ad_hisprop = ArrayDescriptor.createDescriptor("TYLIS_THIST_PROP", con.getMetaData().getConnection());
            a_hisprop = new ARRAY(ad_hisprop, con.getMetaData().getConnection(), lishispropietario);
            ad_soat = ArrayDescriptor.createDescriptor("TYLIS_TSOAT", con.getMetaData().getConnection());
            a_soat = new ARRAY(ad_soat, con.getMetaData().getConnection(), lissoat);
            cst.clearParameters();
            cst.setInt(1, objVehiculo.getTrans_key());
            cst.setInt(2, objVehiculo.getTrans_empid());
            cst.setInt(3, objVehiculo.getTrans_sucid());
            cst.setString(4, objVehiculo.getTrans_placa());
            cst.setString(5, objVehiculo.getTrans_alias());
            cst.setInt(6, objVehiculo.getTrans_marca());
            cst.setInt(7, objVehiculo.getTrans_combustible());
            cst.setInt(8, objVehiculo.getTrans_categoria());
            cst.setInt(9, objVehiculo.getTrans_modelo());
            cst.setInt(10, objVehiculo.getTrans_color());
            cst.setInt(11, objVehiculo.getTrans_transmision());
            cst.setInt(12, objVehiculo.getTrans_carroceria());
            cst.setString(13, objVehiculo.getTrans_vin());
            cst.setString(14, objVehiculo.getTrans_chasis());
            cst.setString(15, objVehiculo.getTrans_motor());
            cst.setInt(16, objVehiculo.getTrans_aniofab());
            cst.setInt(17, objVehiculo.getTrans_aniomodelo());
            cst.setString(18, objVehiculo.getTrans_version());
            cst.setInt(19, objVehiculo.getTrans_ejes());
            cst.setInt(20, objVehiculo.getTrans_asientos());
            cst.setInt(21, objVehiculo.getTrans_pasajeros());
            cst.setInt(22, objVehiculo.getTrans_ruedas());
            cst.setString(23, objVehiculo.getTrans_potencia());
            cst.setInt(24, objVehiculo.getTrans_cilindros());
            cst.setDouble(25, objVehiculo.getTrans_cilindrada());
            cst.setString(26, objVehiculo.getTrans_traccion());
            cst.setDouble(27, objVehiculo.getTrans_pesobruto());
            cst.setDouble(28, objVehiculo.getTrans_pesoneto());
            cst.setDouble(29, objVehiculo.getTrans_cargautil());
            cst.setDouble(30, objVehiculo.getTrans_largo());
            cst.setDouble(31, objVehiculo.getTrans_altura());
            cst.setDouble(32, objVehiculo.getTrans_ancho());
            cst.setString(33, objVehiculo.getTrans_img());
            cst.setDate(34, new java.sql.Date(objVehiculo.getTrans_fecing().getTime()));
            cst.setDate(35, new java.sql.Date(objVehiculo.getTrans_fecsal().getTime()));
            cst.setInt(36, objVehiculo.getTrans_ord());
            cst.setString(37, objVehiculo.getTrans_nomrep());
            cst.setInt(38, objVehiculo.getTrans_estado());
            cst.setString(39, objUsuCredential.getCuenta());
            cst.setArray(40, a_hisprop);
            cst.setArray(41, a_soat);
            cst.registerOutParameter(42, java.sql.Types.NUMERIC);
            cst.registerOutParameter(43, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(43);
            i_flagErrorBD = cst.getInt(42);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objVehiculo.getTrans_id());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarVehiculo(int codigo) throws SQLException {
        String SQL_ELIMINAR_VEHICULO = "{call PACK_TTRANSPORTE.p_eliminarVehiculo(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_VEHICULO);
            cst.clearParameters();
            cst.setInt(1, codigo);
            cst.setString(2, objUsuCredential.getCuenta());
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Vehiculo> listaVehiculo(int i_caso) throws SQLException {
        String SQL_VEHICULO;
        if (i_caso == 0) {
            SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado <>" + i_caso + " and t.emp_id=" + objUsuCredential.getCodemp() + " and "
                    + "t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
        } else {
            SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado =" + i_caso + " and t.emp_id=" + objUsuCredential.getCodemp() + " and "
                    + "t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
        }
        P_WHERE = SQL_VEHICULO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VEHICULO);
            objlstVehiculo = new ListModelList<Vehiculo>();
            while (rs.next()) {
                objVehiculo = new Vehiculo();
                objVehiculo.setTrans_empid(rs.getInt("emp_id"));
                objVehiculo.setTrans_sucid(rs.getInt("suc_id"));
                objVehiculo.setTrans_key(rs.getInt("trans_key"));
                objVehiculo.setTrans_id(rs.getString("trans_id"));
                objVehiculo.setTrans_estado(rs.getInt("trans_estado"));
                objVehiculo.setTrans_marca(rs.getInt("trans_marca"));
                objVehiculo.setTrans_combustible(rs.getInt("trans_combustible"));
                objVehiculo.setTrans_modelo(rs.getInt("trans_modelo"));
                objVehiculo.setTrans_traccion(rs.getString("trans_traccion"));
                objVehiculo.setTrans_vin(rs.getString("trans_vin"));
                objVehiculo.setTrans_chasis(rs.getString("trans_chasis"));
                objVehiculo.setTrans_aniofab(rs.getInt("trans_aniofab"));
                objVehiculo.setTrans_color(rs.getInt("trans_color"));
                objVehiculo.setTrans_motor(rs.getString("trans_motor"));
                objVehiculo.setTrans_aniomodelo(rs.getInt("trans_aniomodelo"));
                objVehiculo.setTrans_version(rs.getString("trans_version"));
                objVehiculo.setTrans_ejes(rs.getInt("trans_ejes"));
                objVehiculo.setTrans_asientos(rs.getInt("trans_asientos"));
                objVehiculo.setTrans_pasajeros(rs.getInt("trans_pasajeros"));
                objVehiculo.setTrans_ruedas(rs.getInt("trans_ruedas"));
                objVehiculo.setTrans_carroceria(rs.getInt("trans_carroceria"));
                objVehiculo.setTrans_potencia(rs.getString("trans_potencia"));
                objVehiculo.setTrans_cilindros(rs.getInt("trans_cilindros"));
                objVehiculo.setTrans_cilindrada(rs.getDouble("trans_cilindrada"));
                objVehiculo.setTrans_pesobruto(rs.getDouble("trans_pesobruto"));
                objVehiculo.setTrans_pesoneto(rs.getDouble("trans_pesoneto"));
                objVehiculo.setTrans_cargautil(rs.getDouble("trans_cargautil"));
                objVehiculo.setTrans_largo(rs.getDouble("trans_largo"));
                objVehiculo.setTrans_altura(rs.getDouble("trans_altura"));
                objVehiculo.setTrans_ancho(rs.getDouble("trans_ancho"));
                objVehiculo.setTrans_alias(rs.getString("trans_alias"));
                objVehiculo.setTrans_categoria(rs.getInt("trans_categoria"));
                objVehiculo.setTrans_fecing(rs.getDate("trans_fecing"));
                objVehiculo.setTrans_fecsal(rs.getDate("trans_fecsal"));
                objVehiculo.setTrans_img(rs.getString("trans_img"));
                objVehiculo.setTrans_transmision(rs.getInt("trans_transmision"));
                objVehiculo.setTrans_placa(rs.getString("trans_placa"));
                objVehiculo.setTrans_nomrep(rs.getString("trans_nomrep"));
                objVehiculo.setTrans_ord(rs.getInt("trans_ord"));
                objVehiculo.setTrans_usuadd(rs.getString("trans_usuadd"));
                objVehiculo.setTrans_fecadd(rs.getTimestamp("trans_fecadd"));
                objVehiculo.setTrans_usumod(rs.getString("trans_usumod"));
                objVehiculo.setTrans_fecmod(rs.getTimestamp("trans_fecmod"));
                objVehiculo.setDesmarca(rs.getString("desmarca"));
                objVehiculo.setDescombustible(rs.getString("descombustible"));
                objVehiculo.setDescolor(rs.getString("descolor"));
                objlstVehiculo.add(objVehiculo);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstVehiculo.getSize() + " registro(s)");
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
        return objlstVehiculo;
    }

    public ListModelList<HistoPropietario> listaPropietario(int codigo) throws SQLException {
        String SQL_LISTA_PROPIETARIOS = "select * from codijisa.v_listahistopropietarios t "
                + "where t.trans_key =  '" + codigo + "' and t.hprop_est<>0 order by t.hprop_id";
        objlstHistoPropietario = new ListModelList<HistoPropietario>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_PROPIETARIOS);
            while (rs.next()) {
                objHistoPropietario = new HistoPropietario();
                objHistoPropietario.setHprop_id(rs.getInt(1));
                objHistoPropietario.setProp_id(rs.getInt(2));
                objHistoPropietario.setProp_razsoc(rs.getString(3));
                objHistoPropietario.setTrans_key(rs.getInt(4));
                objHistoPropietario.setTrans_alias(rs.getString(5));
                objHistoPropietario.setSuc_id(rs.getInt(6));
                objHistoPropietario.setEmp_id(rs.getInt(7));
                objHistoPropietario.setHprop_usuadd(rs.getString(8));
                objHistoPropietario.setHprop_fecadd(rs.getDate(9));
                objHistoPropietario.setHprop_usumod(rs.getString(10));
                objHistoPropietario.setHprop_fecmod(rs.getDate(11));
                objHistoPropietario.setHprop_est(rs.getInt(12));
                objlstHistoPropietario.add(objHistoPropietario);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstHistoPropietario.getSize() + " registro(s)");
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
        return objlstHistoPropietario;
    }

    public ListModelList<Soat> listaSoat(int codigo) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fecha_inicial, fecha_final;

        String SQL_LISTA_SOAT = "select * from codijisa.v_listasoat t "
                + "where t.trans_key =  '" + codigo + "' and t.soat_est<>0 order by t.soat_id";
        objlstSoat = new ListModelList<Soat>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTA_SOAT);
            while (rs.next()) {
                objSoat = new Soat();
                objSoat.setSoat_id(rs.getInt(1));
                objSoat.setTab_id(rs.getInt(2));
                objSoat.setTab_subdes(rs.getString(3));
                objSoat.setTab_cod(rs.getInt(4));
                objSoat.setTrans_key(rs.getInt(5));
                objSoat.setTrans_alias(rs.getString(6));
                objSoat.setSuc_id(rs.getInt(7));
                objSoat.setEmp_id(rs.getInt(8));
                objSoat.setSoat_nro(rs.getString(9));
                fecha_inicial = sdf.format(rs.getDate(10));
                objSoat.setSoat_fechainicial(fecha_inicial);
                fecha_final = sdf.format(rs.getDate(11));
                objSoat.setSoat_fechafinal(fecha_final);
                objSoat.setSoat_fecini(rs.getDate(10));
                objSoat.setSoat_fecfin(rs.getDate(11));
                objSoat.setSoat_est(rs.getInt(12));
                objSoat.setSoat_usuadd(rs.getString(13));
                objSoat.setSoat_fecadd(rs.getDate(14));
                objSoat.setSoat_usumod(rs.getString(15));
                objSoat.setSoat_fecmod(rs.getDate(16));
                objlstSoat.add(objSoat);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSoat.getSize() + " registro(s)");
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
        return objlstSoat;
    }

    public ListModelList<Vehiculo> busquedaVehiculo(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_VEHICULO;

        if (i_estado == 3) { // todos activos e inactivos
            if (seleccion == 0) {//todos
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0"
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + "order by t.trans_id";
            } else if (seleccion == 1) {
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0 and t.trans_id like '" + consulta + "' "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 2) {
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0 and t.trans_placa like '" + consulta + "' "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 3) {
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0 and t.trans_alias like '" + consulta + "' "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 4) {
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0 and t.trans_aniofab like '" + consulta + "' "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else {
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado<> 0 and t.trans_ruedas like '" + consulta + "' "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            }
        } else {
            if (seleccion == 0) {//todos
                SQL_VEHICULO = "select * from v_listatransporte t where t.trans_estado=" + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 1) {
                SQL_VEHICULO = "select *  from v_listatransporte t where t.trans_id like '" + consulta + "' and t.trans_estado = " + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 2) {
                SQL_VEHICULO = "select *  from v_listatransporte t where t.trans_placa like '" + consulta + "' and t.trans_estado = " + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 3) {
                SQL_VEHICULO = "select *  from v_listatransporte t where t.trans_alias like '" + consulta + "' and t.trans_estado = " + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else if (seleccion == 4) {
                SQL_VEHICULO = "select *  from v_listatransporte t where t.trans_aniofab like '" + consulta + "' and t.trans_estado = " + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            } else {
                SQL_VEHICULO = "select *  from v_listatransporte t where t.trans_ruedas like '" + consulta + "' and t.trans_estado = " + i_estado + " "
                        + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
            }
        }
        P_WHERE = SQL_VEHICULO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VEHICULO);
            objlstVehiculo = new ListModelList<Vehiculo>();
            while (rs.next()) {
                objVehiculo = new Vehiculo();
                objVehiculo.setTrans_empid(rs.getInt("emp_id"));
                objVehiculo.setTrans_sucid(rs.getInt("suc_id"));
                objVehiculo.setTrans_key(rs.getInt("trans_key"));
                objVehiculo.setTrans_id(rs.getString("trans_id"));
                objVehiculo.setTrans_estado(rs.getInt("trans_estado"));
                objVehiculo.setTrans_marca(rs.getInt("trans_marca"));
                objVehiculo.setTrans_combustible(rs.getInt("trans_combustible"));
                objVehiculo.setTrans_modelo(rs.getInt("trans_modelo"));
                objVehiculo.setTrans_traccion(rs.getString("trans_traccion"));
                objVehiculo.setTrans_vin(rs.getString("trans_vin"));
                objVehiculo.setTrans_chasis(rs.getString("trans_chasis"));
                objVehiculo.setTrans_aniofab(rs.getInt("trans_aniofab"));
                objVehiculo.setTrans_color(rs.getInt("trans_color"));
                objVehiculo.setTrans_motor(rs.getString("trans_motor"));
                objVehiculo.setTrans_aniomodelo(rs.getInt("trans_aniomodelo"));
                objVehiculo.setTrans_version(rs.getString("trans_version"));
                objVehiculo.setTrans_ejes(rs.getInt("trans_ejes"));
                objVehiculo.setTrans_asientos(rs.getInt("trans_asientos"));
                objVehiculo.setTrans_pasajeros(rs.getInt("trans_pasajeros"));
                objVehiculo.setTrans_ruedas(rs.getInt("trans_ruedas"));
                objVehiculo.setTrans_carroceria(rs.getInt("trans_carroceria"));
                objVehiculo.setTrans_potencia(rs.getString("trans_potencia"));
                objVehiculo.setTrans_cilindros(rs.getInt("trans_cilindros"));
                objVehiculo.setTrans_cilindrada(rs.getDouble("trans_cilindrada"));
                objVehiculo.setTrans_pesobruto(rs.getDouble("trans_pesobruto"));
                objVehiculo.setTrans_pesoneto(rs.getDouble("trans_pesoneto"));
                objVehiculo.setTrans_cargautil(rs.getDouble("trans_cargautil"));
                objVehiculo.setTrans_largo(rs.getDouble("trans_largo"));
                objVehiculo.setTrans_altura(rs.getDouble("trans_altura"));
                objVehiculo.setTrans_ancho(rs.getDouble("trans_ancho"));
                objVehiculo.setTrans_alias(rs.getString("trans_alias"));
                objVehiculo.setTrans_categoria(rs.getInt("trans_categoria"));
                objVehiculo.setTrans_fecing(rs.getDate("trans_fecing"));
                objVehiculo.setTrans_fecsal(rs.getDate("trans_fecsal"));
                objVehiculo.setTrans_img(rs.getString("trans_img"));
                objVehiculo.setTrans_transmision(rs.getInt("trans_transmision"));
                objVehiculo.setTrans_placa(rs.getString("trans_placa"));
                objVehiculo.setTrans_nomrep(rs.getString("trans_nomrep"));
                objVehiculo.setTrans_ord(rs.getInt("trans_ord"));
                objVehiculo.setTrans_usuadd(rs.getString("trans_usuadd"));
                objVehiculo.setTrans_fecadd(rs.getTimestamp("trans_fecadd"));
                objVehiculo.setTrans_usumod(rs.getString("trans_usumod"));
                objVehiculo.setTrans_fecmod(rs.getTimestamp("trans_fecmod"));
                objVehiculo.setDesmarca(rs.getString("desmarca"));
                objVehiculo.setDescombustible(rs.getString("descombustible"));
                objVehiculo.setDescolor(rs.getString("descolor"));
                objlstVehiculo.add(objVehiculo);
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
        return objlstVehiculo;
    }

    public Vehiculo buscarVehiculoxCodigo(String s_codigo) throws SQLException {
        String SQL_VEHICULO = "select * from v_listatransporte t where t.trans_id = " + s_codigo + " and t.trans_estado = 1 "
                + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.trans_id";
        objVehiculo = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VEHICULO);
            objlstVehiculo = new ListModelList<Vehiculo>();
            while (rs.next()) {
                objVehiculo = new Vehiculo();
                objVehiculo.setTrans_empid(rs.getInt("emp_id"));
                objVehiculo.setTrans_sucid(rs.getInt("suc_id"));
                objVehiculo.setTrans_key(rs.getInt("trans_key"));
                objVehiculo.setTrans_id(rs.getString("trans_id"));
                objVehiculo.setTrans_estado(rs.getInt("trans_estado"));
                objVehiculo.setTrans_marca(rs.getInt("trans_marca"));
                objVehiculo.setTrans_combustible(rs.getInt("trans_combustible"));
                objVehiculo.setTrans_modelo(rs.getInt("trans_modelo"));
                objVehiculo.setTrans_traccion(rs.getString("trans_traccion"));
                objVehiculo.setTrans_vin(rs.getString("trans_vin"));
                objVehiculo.setTrans_chasis(rs.getString("trans_chasis"));
                objVehiculo.setTrans_aniofab(rs.getInt("trans_aniofab"));
                objVehiculo.setTrans_color(rs.getInt("trans_color"));
                objVehiculo.setTrans_motor(rs.getString("trans_motor"));
                objVehiculo.setTrans_aniomodelo(rs.getInt("trans_aniomodelo"));
                objVehiculo.setTrans_version(rs.getString("trans_version"));
                objVehiculo.setTrans_ejes(rs.getInt("trans_ejes"));
                objVehiculo.setTrans_asientos(rs.getInt("trans_asientos"));
                objVehiculo.setTrans_pasajeros(rs.getInt("trans_pasajeros"));
                objVehiculo.setTrans_ruedas(rs.getInt("trans_ruedas"));
                objVehiculo.setTrans_carroceria(rs.getInt("trans_carroceria"));
                objVehiculo.setTrans_potencia(rs.getString("trans_potencia"));
                objVehiculo.setTrans_cilindros(rs.getInt("trans_cilindros"));
                objVehiculo.setTrans_cilindrada(rs.getDouble("trans_cilindrada"));
                objVehiculo.setTrans_pesobruto(rs.getDouble("trans_pesobruto"));
                objVehiculo.setTrans_pesoneto(rs.getDouble("trans_pesoneto"));
                objVehiculo.setTrans_cargautil(rs.getDouble("trans_cargautil"));
                objVehiculo.setTrans_largo(rs.getDouble("trans_largo"));
                objVehiculo.setTrans_altura(rs.getDouble("trans_altura"));
                objVehiculo.setTrans_ancho(rs.getDouble("trans_ancho"));
                objVehiculo.setTrans_alias(rs.getString("trans_alias"));
                objVehiculo.setTrans_categoria(rs.getInt("trans_categoria"));
                objVehiculo.setTrans_fecing(rs.getDate("trans_fecing"));
                objVehiculo.setTrans_fecsal(rs.getDate("trans_fecsal"));
                objVehiculo.setTrans_img(rs.getString("trans_img"));
                objVehiculo.setTrans_transmision(rs.getInt("trans_transmision"));
                objVehiculo.setTrans_placa(rs.getString("trans_placa"));
                objVehiculo.setTrans_nomrep(rs.getString("trans_nomrep"));
                objVehiculo.setTrans_ord(rs.getInt("trans_ord"));
                objVehiculo.setTrans_usuadd(rs.getString("trans_usuadd"));
                objVehiculo.setTrans_fecadd(rs.getTimestamp("trans_fecadd"));
                objVehiculo.setTrans_usumod(rs.getString("trans_usumod"));
                objVehiculo.setTrans_fecmod(rs.getTimestamp("trans_fecmod"));
                objVehiculo.setDesmarca(rs.getString("desmarca"));
                objVehiculo.setDescombustible(rs.getString("descombustible"));
                objVehiculo.setDescolor(rs.getString("descolor"));
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
        return objVehiculo;
    }

}
