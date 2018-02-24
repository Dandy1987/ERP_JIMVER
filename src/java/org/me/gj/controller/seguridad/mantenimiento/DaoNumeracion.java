package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zul.Messagebox;

public class DaoNumeracion {

    //Instancias de Objetos
    ListModelList<Numeracion> objlstNumeracion;
    Numeracion objNumeracion;
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
    private static final Logger LOGGER = Logger.getLogger(DaoNumeracion.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarNumeracionTabla(Numeracion objNumeracion) throws SQLException {
        s_msg = "";
        i_flagErrorBD = 0;
        cst = null;
        String SQL_INSERTAR_NUMERACION = "{call pack_tnumeracion.p_insertarNumeracionTabla(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NUMERACION);
            cst.clearParameters();
            cst.setInt(1, objNumeracion.getNum_id());
            cst.setString(2, objNumeracion.getNum_des());
            cst.setInt(3, objNumeracion.getNum_cor());
            cst.setInt(4, objNumeracion.getEmp_id());
            cst.setInt(5, objNumeracion.getSuc_id());
            cst.setString(6, objNumeracion.getNum_tip());
            cst.setString(7, objNumeracion.getNum_usuadd());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objNumeracion.getNum_des());
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

    public ParametrosSalida actualizarNumeracionTabla(Numeracion objNumeracion) throws SQLException {
        s_msg = "";
        i_flagErrorBD = 0;
        cst = null;
        String SQL_ACTUALIZAR_NUMERACION = "{call pack_tnumeracion.p_actualizarNumeracionTabla(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_NUMERACION);
            cst.clearParameters();
            cst.setInt(1, objNumeracion.getNum_id());
            cst.setString(2, objNumeracion.getNum_des());
            cst.setInt(3, objNumeracion.getNum_cor());
            cst.setInt(4, objNumeracion.getNum_est());
            cst.setInt(5, objNumeracion.getEmp_id());
            cst.setInt(6, objNumeracion.getSuc_id());
            cst.setString(7, objNumeracion.getNum_tip());
            cst.setString(8, objNumeracion.getNum_usumod());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objNumeracion.getNum_id());
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

    public ParametrosSalida insertarNumeracionSerie(Numeracion objNumeracion) throws SQLException {
        s_msg = "";
        i_flagErrorBD = 0;
        cst = null;
        String SQL_INSERTAR_NUMERACION = "{call pack_tnumeracion.p_insertarNumeracionSerie(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NUMERACION);
            cst.clearParameters();
            cst.setInt(1, objNumeracion.getEmp_id());
            cst.setInt(2, objNumeracion.getSuc_id());
            cst.setString(3, objNumeracion.getNum_tip());
            cst.setString(4, objNumeracion.getNum_des());
            cst.setInt(5, objNumeracion.getNum_cor());
            cst.setString(6, objNumeracion.getNum_ser());
            cst.setInt(7, objNumeracion.getNum_gui());
            cst.setString(8, objNumeracion.getNum_ven());
            cst.setString(9, objNumeracion.getNum_cos());
            cst.setInt(10, objNumeracion.getNum_fac());
            cst.setInt(11, objNumeracion.getNum_almori());
            cst.setInt(12, objNumeracion.getNum_almdes());
            cst.setInt(13, objNumeracion.getNum_clipro());
            cst.setString(14, objNumeracion.getNum_salalm());
            cst.setString(15, objNumeracion.getNum_com());
            cst.setString(16, objNumeracion.getNum_nomrep());
            cst.setString(17, objNumeracion.getNum_usuadd());
            cst.registerOutParameter(18, java.sql.Types.NUMERIC);
            cst.registerOutParameter(19, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(19);
            i_flagErrorBD = cst.getInt(18);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objNumeracion.getNum_des());
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

    public ParametrosSalida actualizarNumeracionSerie(Numeracion objNumeracion) throws SQLException {
        s_msg = "";
        i_flagErrorBD = 0;
        cst = null;
        String SQL_ACTUALIZAR_NUMERACION = "{call pack_tnumeracion.p_actualizarNumeracionSerie(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_NUMERACION);
            cst.clearParameters();
            cst.setInt(1, objNumeracion.getNum_id());
            cst.setInt(2, objNumeracion.getEmp_id());
            cst.setInt(3, objNumeracion.getSuc_id());
            cst.setString(4, objNumeracion.getNum_tip());
            cst.setString(5, objNumeracion.getNum_des());
            cst.setInt(6, objNumeracion.getNum_est());
            cst.setInt(7, objNumeracion.getNum_cor());
            cst.setString(8, objNumeracion.getNum_ser());
            cst.setInt(9, objNumeracion.getNum_gui());
            cst.setString(10, objNumeracion.getNum_ven());
            cst.setString(11, objNumeracion.getNum_cos());
            cst.setInt(12, objNumeracion.getNum_fac());
            cst.setInt(13, objNumeracion.getNum_almori());
            cst.setInt(14, objNumeracion.getNum_almdes());
            cst.setInt(15, objNumeracion.getNum_clipro());
            cst.setString(16, objNumeracion.getNum_salalm());
            cst.setString(17, objNumeracion.getNum_com());
            cst.setString(18, objNumeracion.getNum_nomrep());
            cst.setString(19, objNumeracion.getNum_usumod());
            cst.registerOutParameter(20, java.sql.Types.NUMERIC);
            cst.registerOutParameter(21, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(21);
            i_flagErrorBD = cst.getInt(20);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objNumeracion.getNum_id());
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

    public ParametrosSalida eliminarNumeracion(Numeracion objNumeracion) throws SQLException {
        s_msg = "";
        i_flagErrorBD = 0;
        cst = null;
        String SQL_ELIMINAR_NUMERACION = "{call pack_tnumeracion.p_eliminarNumeracion(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_NUMERACION);
            cst.clearParameters();
            cst.setInt(1, objNumeracion.getNum_id());
            cst.setInt(2, objNumeracion.getEmp_id());
            cst.setInt(3, objNumeracion.getSuc_id());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objNumeracion.getNum_id());
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Numeracion> listaSeries(int num_guia) throws SQLException {
        String sql = "select distinct t.num_ser from tnumeracion t where t.num_gui='" + num_guia + "' and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.num_tip='R' and t.num_ser is not null ";
        try {
            objlstNumeracion = new ListModelList<Numeracion>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objNumeracion = new Numeracion();
                objNumeracion.setNum_ser(rs.getString("num_ser"));
                objlstNumeracion.add(objNumeracion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeracion.getSize() + " registro(s)");
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
        return objlstNumeracion;
    }

    public ListModelList<Numeracion> listaSeriesSinFiltro() throws SQLException {
        String sql = "select distinct t.num_ser from tnumeracion t where t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.num_tip='R' and t.num_ser is not null order by t.num_ser";
        try {
            objlstNumeracion = new ListModelList<Numeracion>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objNumeracion = new Numeracion();
                objNumeracion.setNum_ser(rs.getString("num_ser"));
                objlstNumeracion.add(objNumeracion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeracion.getSize() + " registro(s)");
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
        return objlstNumeracion;
    }

    public ListModelList<Numeracion> listaNumeracion() throws SQLException {
        String sql = "select * from v_listanumeracion t where t.num_est<>0 order by t.num_id";
        try {
            objlstNumeracion = new ListModelList<Numeracion>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objNumeracion = new Numeracion();
                objNumeracion.setEmp_id(rs.getInt("emp_id"));
                objNumeracion.setSuc_id(rs.getInt("suc_id"));
                objNumeracion.setNum_id(rs.getInt("num_id"));
                objNumeracion.setNum_des(rs.getString("num_des"));
                objNumeracion.setNum_tip(rs.getString("num_tip"));
                objNumeracion.setNum_gui(rs.getInt("num_gui"));
                objNumeracion.setNum_guides(rs.getString("num_guides"));
                objNumeracion.setNum_est(rs.getInt("num_est"));
                objNumeracion.setNum_ser(rs.getString("num_ser"));
                objNumeracion.setNum_cor(rs.getInt("num_cor"));
                objNumeracion.setNum_ven(rs.getString("num_ven"));
                objNumeracion.setNum_clipro(rs.getInt("num_clipro"));
                objNumeracion.setNum_cliprodes(rs.getString("num_cliprodes"));
                objNumeracion.setNum_salalm(rs.getString("num_salalm"));
                objNumeracion.setNum_com(rs.getString("num_com"));
                objNumeracion.setNum_cos(rs.getString("num_cos"));
                objNumeracion.setNum_almori(rs.getInt("num_almori"));
                objNumeracion.setNum_almorides(rs.getString("num_almorides"));
                objNumeracion.setNum_almdes(rs.getInt("num_almdes"));
                objNumeracion.setNum_almdesdes(rs.getString("num_almdesdes"));
                objNumeracion.setNum_fac(rs.getInt("num_fac"));
                objNumeracion.setNum_facdes(rs.getString("num_facdes"));
                objNumeracion.setNum_tipdoc(rs.getString("num_tipdoc"));
                objNumeracion.setNum_nomrep(rs.getString("num_nomrep"));
                objNumeracion.setNum_usuadd(rs.getString("num_usuadd"));
                objNumeracion.setNum_fecadd(rs.getDate("num_fecadd"));
                objNumeracion.setNum_usumod(rs.getString("num_usumod"));
                objNumeracion.setNum_fecmod(rs.getDate("num_fecmod"));
                objlstNumeracion.add(objNumeracion);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeracion.getSize() + " registro(s)");
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
        return objlstNumeracion;
    }

    public ListModelList<Numeracion> busquedaNumeracion(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_NUMERACION;
        if (i_estado == 3) { // todos activos e inactivos
            if (i_seleccion == 0) {//todos
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0";
            } else if (i_seleccion == 1) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0 and t.num_id like '" + s_consulta + "'";
            } else if (i_seleccion == 2) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0 and t.num_des like '" + s_consulta + "'";
            } else if (i_seleccion == 3) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0 and t.num_gui like '" + s_consulta + "'";
            } else if (i_seleccion == 4) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0 and t.num_guides like '" + s_consulta + "'";
            } else {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est<>0 and t.num_ser like '" + s_consulta + "'";
            }
        } else {
            if (i_seleccion == 0) {//todos
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado;
            } else if (i_seleccion == 1) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado + " and t.num_id like '" + s_consulta + "'";
            } else if (i_seleccion == 2) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado + " and t.num_des like '" + s_consulta + "'";
            } else if (i_seleccion == 3) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado + " and t.num_gui like '" + s_consulta + "'";
            } else if (i_seleccion == 4) {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado + " and t.num_guides like '" + s_consulta + "'";
            } else {
                SQL_NUMERACION = "select * from v_listanumeracion t where t.num_est=" + i_estado + " and t.num_ser like '" + s_consulta + "'";
            }
        }

        try {
            objlstNumeracion = new ListModelList<Numeracion>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NUMERACION);
            while (rs.next()) {
                objNumeracion = new Numeracion();
                objNumeracion.setEmp_id(rs.getInt("emp_id"));
                objNumeracion.setSuc_id(rs.getInt("suc_id"));
                objNumeracion.setNum_id(rs.getInt("num_id"));
                objNumeracion.setNum_des(rs.getString("num_des"));
                objNumeracion.setNum_tip(rs.getString("num_tip"));
                objNumeracion.setNum_gui(rs.getInt("num_gui"));
                objNumeracion.setNum_guides(rs.getString("num_guides"));
                objNumeracion.setNum_est(rs.getInt("num_est"));
                objNumeracion.setNum_ser(rs.getString("num_ser"));
                objNumeracion.setNum_cor(rs.getInt("num_cor"));
                objNumeracion.setNum_ven(rs.getString("num_ven"));
                objNumeracion.setNum_clipro(rs.getInt("num_clipro"));
                objNumeracion.setNum_cliprodes(rs.getString("num_cliprodes"));
                objNumeracion.setNum_salalm(rs.getString("num_salalm"));
                objNumeracion.setNum_com(rs.getString("num_com"));
                objNumeracion.setNum_cos(rs.getString("num_cos"));
                objNumeracion.setNum_almori(rs.getInt("num_almori"));
                objNumeracion.setNum_almorides(rs.getString("num_almorides"));
                objNumeracion.setNum_almdes(rs.getInt("num_almdes"));
                objNumeracion.setNum_almdesdes(rs.getString("num_almdesdes"));
                objNumeracion.setNum_fac(rs.getInt("num_fac"));
                objNumeracion.setNum_facdes(rs.getString("num_facdes"));
                objNumeracion.setNum_tipdoc(rs.getString("num_tipdoc"));
                objNumeracion.setNum_nomrep(rs.getString("num_nomrep"));
                objNumeracion.setNum_usuadd(rs.getString("num_usuadd"));
                objNumeracion.setNum_fecadd(rs.getDate("num_fecadd"));
                objNumeracion.setNum_usumod(rs.getString("num_usumod"));
                objNumeracion.setNum_fecmod(rs.getDate("num_fecmod"));
                objlstNumeracion.add(objNumeracion);
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
        return objlstNumeracion;
    }

    public String listaAlmacenxGuia(int num_guia, String num_ser) throws SQLException {
        String alm_ori = "";
        String sql = "select t.num_almori from tnumeracion t where t.num_gui='" + num_guia + "' and t.num_ser='" + num_ser + "' and t.emp_id='" + objUsuCredential.getCodemp() + "' "
                + "and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.num_tip='R'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                alm_ori = "0" + rs.getInt("num_almori");
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeracion.getSize() + " registro(s)");
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
        return alm_ori;
    }

}
