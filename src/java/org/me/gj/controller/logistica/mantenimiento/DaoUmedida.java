package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Umedida;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoUmedida {

    //Instancias de Objetos
    ListModelList<Umedida> objlstUmedida;
    Umedida objUmedida;
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
    private static final Logger LOGGER = Logger.getLogger(DaoUmedida.class);

    //Eventos Primarios - Transaccionales
    public String insertar(Umedida ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        int i_tab_tip = ttabGen.getTab_tip();
        String SQL_INSERT_PROC_UMEDIDA = "{call pack_ttabgen.p_016_insertarUnidadMedida(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_UMEDIDA);
            cst.clearParameters();
            cst.setString(1, ttabGen.getTab_subdes());
            cst.setString(2, ttabGen.getTab_subdir());
            cst.setInt(3, i_tab_tip);
            cst.setString(4, ttabGen.getTab_nomrep());
            cst.setString(5, ttabGen.getTab_usuadd());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + ttabGen.getTab_subdes());
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

    public String modificar(Umedida ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        int i_tab_id = ttabGen.getTab_id();
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        String s_tab_subdir = ttabGen.getTab_subdir();
        s_tab_subdir = s_tab_subdir.toUpperCase();
        int i_tab_tip = ttabGen.getTab_tip();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep().toUpperCase();
        String s_tab_usumod = ttabGen.getTab_usumod();
        String SQL_UPDATE_PROC_UMEDIDA = "{call pack_ttabgen.p_016_actualizarunidadMedida(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_UMEDIDA);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.setString(2, s_tab_subdes);
            cst.setString(3, s_tab_subdir);
            cst.setInt(4, i_tab_tip);
            cst.setString(5, s_tab_nomrep);
            cst.setInt(6, i_tab_est);
            cst.setString(7, s_tab_usumod);
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tab_id);
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

    public String eliminar(Umedida ttabGen) throws SQLException {
        String SQL_INSERT_PROC_UMEDIDA = "{call pack_ttabgen.p_016_eliminarUnidadMedida(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_UMEDIDA);
            cst.clearParameters();
            cst.setInt(1, ttabGen.getTab_id());
            cst.setString(2, ttabGen.getTab_subdir());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + ttabGen.getTab_id());
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
    public ListModelList<Umedida> listaUmedida(int estado) throws SQLException {
        String SQL_UMEDIDA;
        if (estado == 0) {
            SQL_UMEDIDA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est <> '" + estado + "'  order by t.tab_id";
        } else {
            SQL_UMEDIDA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est = '" + estado + "' order by t.tab_id";
        }
        P_WHERE = SQL_UMEDIDA;
        objlstUmedida = new ListModelList<Umedida>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMEDIDA);
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt("tab_id"));
                objUmedida.setTab_subdes(rs.getString("tab_subdes"));
                objUmedida.setTab_subdir(rs.getString("tab_subdir"));
                objUmedida.setTab_tip(rs.getInt("tab_tip"));
                objUmedida.setTab_est(rs.getInt("tab_est"));
                objUmedida.setTab_nomrep(rs.getString("tab_nomrep"));
                objUmedida.setTab_usuadd(rs.getString("tab_usuadd"));
                objUmedida.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objUmedida.setTab_usumod(rs.getString("tab_usumod"));
                objUmedida.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstUmedida.add(objUmedida);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUmedida.getSize() + " registro(s)");
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
        return objlstUmedida;
    }

    public ListModelList<Umedida> lstUmedidaVen() throws SQLException {
        String SQL_UMEDIDA_VEN = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=16 and t.tab_est=1 order by t.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMEDIDA_VEN);
            objlstUmedida = new ListModelList<Umedida>();
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt(1));
                objUmedida.setTab_subdir(rs.getString(2));
                objUmedida.setTab_subdes(rs.getString(3));
                objlstUmedida.add(objUmedida);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUmedida.getSize() + " registro(s)");
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

        return objlstUmedida;
    }

    public ListModelList<Umedida> lstEmpIndivVen() throws SQLException {
        String SQL_EMPINDIV_VEN = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=16 and t.tab_est=1 order by t.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_VEN);
            objlstUmedida = new ListModelList<Umedida>();
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt(1));
                objUmedida.setTab_subdir(rs.getString(2));
                objUmedida.setTab_subdes(rs.getString(3));
                objlstUmedida.add(objUmedida);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUmedida.getSize() + " registro(s)");
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
        return objlstUmedida;
    }

    public ListModelList<Umedida> lstEmpIndivComp() throws SQLException {
        String SQL_EMPINDIV_COMP = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=16 and t.tab_est=1 order by t.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_COMP);
            objlstUmedida = new ListModelList<Umedida>();
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt(1));
                objUmedida.setTab_subdir(rs.getString(2));
                objUmedida.setTab_subdes(rs.getString(3));
                objlstUmedida.add(objUmedida);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUmedida.getSize() + " registro(s)");
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
        return objlstUmedida;
    }

    public ListModelList<Umedida> lstUmedidaComp() throws SQLException {
        String SQL_UMEDIDA_COMP = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=16 and t.tab_est=1 order by t.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMEDIDA_COMP);
            objlstUmedida = new ListModelList<Umedida>();
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt(1));
                objUmedida.setTab_subdir(rs.getString(2));
                objUmedida.setTab_subdes(rs.getString(3));
                objlstUmedida.add(objUmedida);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstUmedida.getSize() + " registro(s)");
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
        return objlstUmedida;
    }

    public ListModelList<Umedida> busqueda(String dato, int indice, int i_estado) throws SQLException {        
        String SQL_UMEDIDA_SEARCH = "";
        if (i_estado == 3) {
            if (indice == 0) {
                SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est<>0 order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est<>0 and lpad(t.tab_id,4,'0') like '" + dato + "' order by t.tab_id";
                } else if (indice == 2) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est<>0 and t.tab_subdes like '" + dato + "' order by t.tab_id";
                } else if (indice == 3) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est<>0 and t.tab_subdir like '" + dato + "' order by t.tab_id";
                } else if (indice == 4) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est<>0 and t.tab_tip like '" + dato + "' order by t.tab_id";
                }
            }
        } else {
            if (indice == 0) {
                SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est=" + i_estado + "  order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est=" + i_estado + " and lpad(t.tab_id,4,'0') like '" + dato + "' order by t.tab_id";
                } else if (indice == 2) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est=" + i_estado + " and t.tab_subdes like '" + dato + "' order by t.tab_id";
                } else if (indice == 3) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est=" + i_estado + " and t.tab_subdir like '" + dato + "' order by t.tab_id";
                } else if (indice == 4) {
                    SQL_UMEDIDA_SEARCH = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_tip,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=16 and t.tab_est=" + i_estado + " and t.tab_tip like '" + dato + "' order by t.tab_id";
                }
            }
        }
        P_WHERE = SQL_UMEDIDA_SEARCH;
        objlstUmedida = new ListModelList<Umedida>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMEDIDA_SEARCH);
            while (rs.next()) {
                objUmedida = new Umedida();
                objUmedida.setTab_id(rs.getInt(1));
                objUmedida.setTab_subdes(rs.getString(2));
                objUmedida.setTab_subdir(rs.getString(3));
                objUmedida.setTab_tip(rs.getInt(4));
                objUmedida.setTab_est(rs.getInt(5));
                objUmedida.setTab_nomrep(rs.getString(6));
                objUmedida.setTab_usuadd(rs.getString(7));
                objUmedida.setTab_fecadd(rs.getTimestamp(8));
                objUmedida.setTab_usumod(rs.getString(9));
                objUmedida.setTab_fecmod(rs.getTimestamp(10));
                objlstUmedida.add(objUmedida);
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

        return objlstUmedida;
    }

    public int ValidaAbrev(String dato, int codigo) throws SQLException {
        String SQL_UMEDIDA;
        int valor = 0;
        if (codigo == 0) {
            SQL_UMEDIDA = "select nvl(to_number(count(*)),0) from codijisa.ttabgen t where t.tab_cod='16' and  t.tab_subdir='" + dato + "'";
        } else {
            SQL_UMEDIDA = "select nvl(to_number(count(*)),0) from codijisa.ttabgen t where t.tab_cod='16' and  t.tab_subdir='" + dato + "' and not t.tab_id='" + codigo + "'";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMEDIDA);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validacion de Abreviatura  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
    
    public String validaMovimientos(Umedida ttabGen) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttaben.p_valida_movimientosumed(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, ttabGen.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la unidad de medida " + ttabGen.getTab_id());
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
