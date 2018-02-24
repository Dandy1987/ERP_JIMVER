package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.TtabGen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zul.Messagebox;

public class DaoUmanejo {

    //Instancias de Objetos
    ListModelList<TtabGen> objlstTtabgen;
    TtabGen objTtabgen;
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
    private static final Logger LOGGER = Logger.getLogger(DaoUmanejo.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(TtabGen ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        String s_tab_subdir = ttabGen.getTab_subdir();
        s_tab_subdir = s_tab_subdir.toUpperCase();
        int i_tab_ord = ttabGen.getTab_ord();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep();
        s_tab_nomrep = s_tab_nomrep.toUpperCase();
        String s_tabusuadd = ttabGen.getTab_usuadd();
        String SQL_INSERT_PROC_UMANEJO = "{call pack_ttabgen.p_007_insertarUnidadManejo(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_UMANEJO);
            cst.clearParameters();
            cst.setString(1, s_tab_subdes);
            cst.setString(2, s_tab_subdir);
            cst.setInt(3, i_tab_ord);
            cst.setInt(4, i_tab_est);
            cst.setString(5, s_tab_nomrep);
            cst.setString(6, s_tabusuadd);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_tab_subdes);
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

    public ParametrosSalida eliminar(TtabGen ttabGen) throws SQLException {
        String SQL_INSERT_PROC_UMANEJO = "{call pack_ttabgen.p_007_eliminarUnidadManejo(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_UMANEJO);
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida modificar(TtabGen ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        int i_tab_id = ttabGen.getTab_id();
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        String s_tab_subdir = ttabGen.getTab_subdir();
        s_tab_subdir = s_tab_subdir.toUpperCase();
        int i_tab_ord = ttabGen.getTab_ord();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep().toUpperCase();
        String s_tabusuadd = ttabGen.getTab_usuadd();
        String SQL_UPDATE_PROC_UMANEJO = "{call pack_ttabgen.p_007_actualizarUnidadManejo(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_UMANEJO);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.setString(2, s_tab_subdes);
            cst.setString(3, s_tab_subdir);
            cst.setInt(4, i_tab_ord);
            cst.setInt(5, i_tab_est);
            cst.setString(6, s_tab_nomrep);
            cst.setString(7, s_tabusuadd);
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
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<TtabGen> lstUmanejo(String estado) throws SQLException {
        String SQL_UMANEJO;
        if ("0".equals(estado)) {
            SQL_UMANEJO = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est <> 0 order by t.tab_id";
        } else {
            SQL_UMANEJO = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est = 1 order by t.tab_id";
        }
        P_WHERE = SQL_UMANEJO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMANEJO);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_cod(rs.getInt("tab_cod"));
                objTtabgen.setTab_id(rs.getInt("tab_id"));
                objTtabgen.setTab_subdes(rs.getString("tab_subdes"));
                objTtabgen.setTab_subdir(rs.getString("tab_subdir"));
                objTtabgen.setTab_ord(rs.getInt("tab_ord"));
                objTtabgen.setTab_est(rs.getInt("tab_est"));
                objTtabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                objTtabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                objTtabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTtabgen.setTab_usumod(rs.getString("tab_usumod"));
                objTtabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTtabgen.add(objTtabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtabgen.getSize() + " registro(s)");
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
        return objlstTtabgen;
    }

    public ListModelList<TtabGen> lstUmanejoVen() throws SQLException {
        String SQL_UMANEJO_VEN = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=7 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMANEJO_VEN);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_id(rs.getInt(1));
                objTtabgen.setTab_subdir(rs.getString(2));
                objTtabgen.setTab_subdes(rs.getString(3));
                objlstTtabgen.add(objTtabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtabgen.getSize() + " registro(s)");
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
        return objlstTtabgen;
    }

    public ListModelList<TtabGen> lstEmpIndivVen() throws SQLException {
        String SQL_EMPINDIV_VEN = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=7 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_VEN);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_id(rs.getInt(1));
                objTtabgen.setTab_subdir(rs.getString(2));
                objTtabgen.setTab_subdes(rs.getString(3));
                objlstTtabgen.add(objTtabgen);
            }

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtabgen.getSize() + " registro(s)");
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
        return objlstTtabgen;
    }

    public ListModelList<TtabGen> lstEmpIndivComp() throws SQLException {
        String SQL_EMPINDIV_COMP = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=7 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_EMPINDIV_COMP);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_id(rs.getInt(1));
                objTtabgen.setTab_subdir(rs.getString(2));
                objTtabgen.setTab_subdes(rs.getString(3));
                objlstTtabgen.add(objTtabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtabgen.getSize() + " registro(s)");
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
        return objlstTtabgen;
    }

    public ListModelList<TtabGen> lstUmanejoComp() throws SQLException {
        String SQL_UMANEJO_COMP = "select t.tab_id,t.tab_subdir,t.tab_subdes from ttabgen t "
                + "where t.tab_cod=7 and t.tab_est=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMANEJO_COMP);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_id(rs.getInt(1));
                objTtabgen.setTab_subdir(rs.getString(2));
                objTtabgen.setTab_subdes(rs.getString(3));
                objlstTtabgen.add(objTtabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtabgen.getSize() + " registro(s)");
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
        return objlstTtabgen;
    }

    public ListModelList<TtabGen> Search(String dato, int indice, int i_estado) throws SQLException {
        String SQL_UMANEJO_SEARCH;
        if (i_estado == 3) {
            if (indice == 0) {
                SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est<>0 order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est<>0 and t.tab_id like '" + dato + "' order by t.tab_id";
                } else if ((indice == 2)) {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est<>0 and t.tab_subdir like '" + dato + "'order by t.tab_id";
                } else {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est<>0 and t.tab_subdes like '" + dato + "'order by t.tab_id";
                }
            }
        } else {
            if (indice == 0) {
                SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est=" + i_estado + "order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est=" + i_estado + " and t.tab_id like '" + dato + "' order by t.tab_id ";
                } else if (indice == 2) {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est=" + i_estado + " and t.tab_subdir like '" + dato + "' order by t.tab_id";
                } else {
                    SQL_UMANEJO_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_ord,t.tab_est,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod=7 and t.tab_est=" + i_estado + " and t.tab_subdes like '" + dato + "' order by t.tab_id";
                }
            }
        }
        P_WHERE = SQL_UMANEJO_SEARCH;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMANEJO_SEARCH);
            objlstTtabgen = new ListModelList<TtabGen>();
            while (rs.next()) {
                objTtabgen = new TtabGen();
                objTtabgen.setTab_cod(rs.getInt("tab_cod"));
                objTtabgen.setTab_id(rs.getInt("tab_id"));
                objTtabgen.setTab_subdes(rs.getString("tab_subdes"));
                objTtabgen.setTab_subdir(rs.getString("tab_subdir"));
                objTtabgen.setTab_ord(rs.getInt("tab_ord"));
                objTtabgen.setTab_est(rs.getInt("tab_est"));
                objTtabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                objTtabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                objTtabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTtabgen.setTab_usumod(rs.getString("tab_usumod"));
                objTtabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTtabgen.add(objTtabgen);
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
        return objlstTtabgen;
    }

    public int ValidaAbrev(String dato, int codigo) throws SQLException {
        String SQL_UMANEJO;
        int valor = 0;
        if (codigo == 0) {
            SQL_UMANEJO = "select nvl(to_number(count(*)),0) from codijisa.ttabgen t where t.tab_cod='7' and  t.tab_subdir='" + dato + "'";
        } else {
            SQL_UMANEJO = "select nvl(to_number(count(*)),0) from codijisa.ttabgen t where t.tab_cod='7' and  t.tab_subdir='" + dato + "' and not t.tab_id='" + codigo + "'";
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_UMANEJO);
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

    public String validaMovimientos(TtabGen ttabGen) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientosuman(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, ttabGen.getTab_subdir());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la unidad de manejo " + ttabGen.getTab_id());
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
