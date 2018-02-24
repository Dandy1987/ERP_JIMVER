package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoUbicaciones {

    //Instancias de Objetos
    ListModelList<Ubicaciones> objlstUbi;
    Ubicaciones objUbicaciones;
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
    private static final Logger LOGGER = Logger.getLogger(DaoUbicaciones.class);

    //Eventos Primarios - Transaccionales
    public String insertar(Ubicaciones objUbicacion) throws SQLException {
        String s_ubicdes = objUbicacion.getUbic_des();
        double db_ubicalt = objUbicacion.getUbic_alt();
        double db_ubicanc = objUbicacion.getUbic_anc();
        double db_ubiclar = objUbicacion.getUbic_lar();
        String s_ubicnomrep = objUbicacion.getUbic_nomrep();
        int i_ubicord = objUbicacion.getUbic_ord();
        String s_ubicusuadd = objUbicacion.getUbic_usuadd();
        int i_emp_id = objUbicacion.getEmp_id();
        int i_suc_id = objUbicacion.getSuc_id();
        int i_ubi_def = objUbicacion.getUbic_default();
        String SQL_INSERTAR_UBICACION = "{call pack_tubicaciones.p_insertarUbicacion(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_UBICACION);
            cst.clearParameters();
            cst.setString(1, s_ubicdes);
            cst.setDouble(2, db_ubicalt);
            cst.setDouble(3, db_ubicanc);
            cst.setDouble(4, db_ubiclar);
            cst.setString(5, s_ubicnomrep);
            cst.setInt(6, i_ubicord);
            cst.setString(7, s_ubicusuadd);
            cst.setInt(8, i_emp_id);
            cst.setInt(9, i_suc_id);
            cst.setInt(10, i_ubi_def);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_ubicdes);
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

    public String actualizar(Ubicaciones objUbicacion) throws SQLException {
        int i_ubickey = objUbicacion.getUbic_key();
        int i_ubicest = objUbicacion.getUbic_est();
        String s_ubicdes = objUbicacion.getUbic_des();
        double db_ubicalt = objUbicacion.getUbic_alt();
        double db_ubicanc = objUbicacion.getUbic_anc();
        double db_ubiclar = objUbicacion.getUbic_lar();
        String s_ubicnomrep = objUbicacion.getUbic_nomrep();
        int i_ubicord = objUbicacion.getUbic_ord();
        String s_ubicusumod = objUbicacion.getUbic_usumod();
        int i_emp_id = objUbicacion.getEmp_id();
        int i_suc_id = objUbicacion.getSuc_id();
        int i_ubi_def = objUbicacion.getUbic_default();
        String SQL_ATUALZAR_UBICACION = "{call pack_tubicaciones.p_actualizarUbicacion(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ATUALZAR_UBICACION);
            cst.clearParameters();
            cst.setInt("n_ubic_key", i_ubickey);
            cst.setInt("n_ubic_est", i_ubicest);
            cst.setString("c_ubic_des", s_ubicdes);
            cst.setDouble("n_ubic_alt", db_ubicalt);
            cst.setDouble("n_ubic_anc", db_ubicanc);
            cst.setDouble("n_ubic_lar", db_ubiclar);
            cst.setString("c_ubic_nomrep", s_ubicnomrep);
            cst.setInt("n_ubic_ord", i_ubicord);
            cst.setString("c_ubic_usumod", s_ubicusumod);
            cst.setInt("n_emp_id", i_emp_id);
            cst.setInt("n_suc_id", i_suc_id);
            cst.setInt("n_ubi_def", i_ubi_def);
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objUbicacion.getUbic_id());
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

    public String eliminar(Ubicaciones objUbicacion) throws SQLException {
        int i_ubickey = objUbicacion.getUbic_key();
        int i_empid = objUbicacion.getEmp_id();
        int i_sucid = objUbicacion.getSuc_id();
        String SQL_ELIMINAR_UBICACION = "{call pack_tubicaciones.p_eliminarUbicacion(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_UBICACION);
            cst.clearParameters();
            cst.setInt(1, i_ubickey);
            cst.setInt(2, i_empid);
            cst.setInt(3, i_sucid);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objUbicacion.getUbic_id());
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
        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Ubicaciones> listaUbicaciones(int estado) throws SQLException {
        String sql;
        if (estado == 0) {
            sql = "select * from tubicaciones p where p.ubic_est<>" + estado + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " order by p.ubic_key";
        } else {
            sql = "select * from tubicaciones p where p.ubic_est=" + estado + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " order by p.ubic_key";
        }
        P_WHERE = sql;
        objlstUbi = new ListModelList<Ubicaciones>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objUbicaciones = new Ubicaciones();
                objUbicaciones.setSuc_id(rs.getInt("suc_id"));
                objUbicaciones.setEmp_id(rs.getInt("emp_id"));
                objUbicaciones.setUbic_key(rs.getInt("ubic_key"));
                objUbicaciones.setUbic_id(rs.getString("ubic_id"));
                objUbicaciones.setUbic_des(rs.getString("ubic_des"));
                objUbicaciones.setUbic_est(rs.getInt("ubic_est"));
                objUbicaciones.setUbic_alt(rs.getDouble("ubic_alt"));
                objUbicaciones.setUbic_lar(rs.getDouble("ubic_lar"));
                objUbicaciones.setUbic_anc(rs.getDouble("ubic_anc"));
                objUbicaciones.setUbic_nomrep(rs.getString("ubic_nomrep"));
                objUbicaciones.setUbic_ord(rs.getInt("ubic_ord"));
                objUbicaciones.setUbic_default(rs.getInt("ubic_default"));
                objUbicaciones.setUbic_usuadd(rs.getString("ubic_usuadd"));
                objUbicaciones.setUbic_fecadd(rs.getTimestamp("ubic_fecadd"));
                objUbicaciones.setUbic_usumod(rs.getString("ubic_usumod"));
                objUbicaciones.setUbic_fecmod(rs.getTimestamp("ubic_fecmod"));
                objlstUbi.add(objUbicaciones);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUbi.getSize() + " registro(s)");
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
        return objlstUbi;
    }

    public ListModelList<Ubicaciones> busquedaUbicacion(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql = "";
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select * from tubicaciones p where p.ubic_est <>0 and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
            } else {
                if (seleccion == 1) {
                    sql = "select * from tubicaciones p where p.ubic_est <>0 and p.ubic_id like '" + consulta
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 2) {
                    sql = "select * from tubicaciones p where p.ubic_est <>0  and p.ubic_des like '" + consulta
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 3) {
                    sql = "select * from tubicaciones p where p.ubic_est <>0 and p.ubic_alt like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 4) {
                    sql = "select * from tubicaciones p where p.ubic_est <>0 and p.ubic_anc like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 5) {
                    sql = "select * from tubicaciones p where p.ubic_est <>0 and p.ubic_lar like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
            } else {
                if (seleccion == 1) {
                    sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "' and p.ubic_id like '" + consulta
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 2) {
                    sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "' and p.ubic_des like '" + consulta
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 3) {
                    sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "'and p.ubic_alt like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 4) {
                    sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "' and p.ubic_anc like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                } else if (seleccion == 5) {
                    sql = "select * from tubicaciones p where p.ubic_est='" + i_estado + "' and p.ubic_lar like '" + consulta.replace(".", ",")
                            + "'  and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
                }
            }
        }
        P_WHERE = sql;
        objlstUbi = new ListModelList<Ubicaciones>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objUbicaciones = new Ubicaciones();
                objUbicaciones.setSuc_id(rs.getInt("suc_id"));
                objUbicaciones.setEmp_id(rs.getInt("emp_id"));
                objUbicaciones.setUbic_key(rs.getInt("ubic_key"));
                objUbicaciones.setUbic_id(rs.getString("ubic_id"));
                objUbicaciones.setUbic_des(rs.getString("ubic_des"));
                objUbicaciones.setUbic_est(rs.getInt("ubic_est"));
                objUbicaciones.setUbic_alt(rs.getDouble("ubic_alt"));
                objUbicaciones.setUbic_lar(rs.getDouble("ubic_lar"));
                objUbicaciones.setUbic_anc(rs.getDouble("ubic_anc"));
                objUbicaciones.setUbic_nomrep(rs.getString("ubic_nomrep"));
                objUbicaciones.setUbic_ord(rs.getInt("ubic_ord"));
                objUbicaciones.setUbic_usuadd(rs.getString("ubic_usuadd"));
                objUbicaciones.setUbic_fecadd(rs.getTimestamp("ubic_fecadd"));
                objUbicaciones.setUbic_usumod(rs.getString("ubic_usumod"));
                objUbicaciones.setUbic_fecmod(rs.getTimestamp("ubic_fecmod"));
                objlstUbi.add(objUbicaciones);
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
        return objlstUbi;
    }

    public Ubicaciones ubicacionDefault(int estado) throws SQLException {
        String sql;
        sql = "select * from tubicaciones p where p.ubic_default=" + estado + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.ubic_est=1 order by p.ubic_key";
        P_WHERE = sql;
        objlstUbi = new ListModelList<Ubicaciones>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objUbicaciones = new Ubicaciones();
                objUbicaciones.setUbic_key(rs.getInt("ubic_key"));
                objUbicaciones.setUbic_id(rs.getString("ubic_id"));
                objUbicaciones.setUbic_des(rs.getString("ubic_des"));
                objUbicaciones.setSuc_id(rs.getInt("suc_id"));
                objUbicaciones.setEmp_id(rs.getInt("emp_id"));
                objUbicaciones.setUbic_usuadd(rs.getString("ubic_usuadd"));
                objUbicaciones.setUbic_fecadd(rs.getTimestamp("ubic_fecadd"));
                objUbicaciones.setUbic_usumod(rs.getString("ubic_usumod"));
                objUbicaciones.setUbic_fecmod(rs.getTimestamp("ubic_fecmod"));
                objUbicaciones.setUbic_default(rs.getInt("ubic_default"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUbi.getSize() + " registro(s)");
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
        return objUbicaciones;
    }

    public Ubicaciones BusquedaUbicacion(String Codigo) throws SQLException {
        String SQL_UBI = "select t.ubic_key,t.ubic_id,t.ubic_des,t.ubic_default from tubicaciones t where t.ubic_key=" + Codigo;
        objUbicaciones = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UBI);
            objUbicaciones = null;
            while (rs.next()) {
                objUbicaciones = new Ubicaciones();
                objUbicaciones.setUbic_key(rs.getInt("ubic_key"));
                objUbicaciones.setUbic_id(rs.getString("ubic_id"));
                objUbicaciones.setUbic_des(rs.getString("ubic_des"));
                objUbicaciones.setUbic_default(rs.getInt("ubic_default"));
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
        return objUbicaciones;
    }

    public Ubicaciones listauUbi(String codigo) throws SQLException {
        String SQL_LISTAUBICACION = " select t.ubic_key , t.ubic_id , t.ubic_des , t.ubic_est From codijisa.tubicaciones t "
                + " where t.ubic_id = '" + codigo + "' and t.ubic_est = 1 and t.emp_id=" + objUsuCredential.getCodemp() + "and t.suc_id=" + objUsuCredential.getCodsuc();
        objUbicaciones = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAUBICACION);

            while (rs.next()) {
                objUbicaciones = new Ubicaciones();
                objUbicaciones.setUbic_key(rs.getInt("ubic_key"));
                objUbicaciones.setUbic_id(rs.getString("ubic_id"));
                objUbicaciones.setUbic_des(rs.getString("ubic_des"));
                objUbicaciones.setUbic_est(rs.getInt("ubic_est"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos ");
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
        return objUbicaciones;
    }

    public String descripcionUbicacion(String codigo) throws SQLException {
        String nombre = "";
        String sql;
        sql = "select p.ubic_des from tubicaciones p where p.ubic_id=" + codigo + " and p.emp_id='" + objUsuCredential.getCodemp() + "' and p.suc_id='" + objUsuCredential.getCodsuc() + "' order by p.ubic_key";
        P_WHERE = sql;
        objlstUbi = new ListModelList<Ubicaciones>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                nombre = rs.getString("ubic_des");
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
        return nombre;
    }

    public int busquedaExistenciaDefault() throws SQLException {
        String SQL_VERIFICA_DEF;
        int valor = 0;

        SQL_VERIFICA_DEF = "select nvl(to_number(count(*)),0) from tubicaciones p "
                //+ " where p.ubic_key not in(" + ubi_def + ") "
                + " where p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc()
                + " and p.ubic_default = 1 "
                + " and p.ubic_est=1 order by p.ubic_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DEF);

            while (rs.next()) {

                valor = rs.getInt(1);
            }
        } catch (SQLException e) {

            Messagebox.show(" Error de Validación de Ubicacion debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
        return valor;
    }
    
    public String validaMovimientos(Ubicaciones objUbicacion) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tubicaciones.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, objUbicacion.getUbic_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la ubicacion " + objUbicacion.getUbic_id());
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
