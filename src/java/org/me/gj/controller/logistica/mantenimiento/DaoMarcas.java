package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.TtabGen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;

public class DaoMarcas {

    //Instancias de Objetos
    ListModelList<TtabGen> objlsttabGen;
    TtabGen obj_tabgen;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoMarcas.class);

    //Eventos Primarios - Transaccionales
    public String insertar(TtabGen ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        int i_tab_ord = ttabGen.getTab_ord();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep();
        s_tab_nomrep = s_tab_nomrep.toUpperCase();
        String s_tabusuadd = ttabGen.getTab_usuadd();
        String SQL_INSERT_PROC_MARCAS = "{call pack_ttabgen.p_006_insertarMarcas(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_MARCAS);
            cst.clearParameters();
            cst.setString(1, s_tab_subdes);
            cst.setInt(2, i_tab_ord);
            cst.setInt(3, i_tab_est);
            cst.setString(4, s_tab_nomrep);
            cst.setString(5, s_tabusuadd);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
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

        return s_msg;
    }

    public String eliminar(int i_tab_id) throws SQLException {
        String SQL_DELETE_PROC_MARCAS = "{call pack_ttabgen.p_006_eliminarMarcas(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_MARCAS);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tab_id);
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

    public String modificar(TtabGen ttabGen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        int i_tab_id = ttabGen.getTab_id();
        String s_tab_subdes = ttabGen.getTab_subdes();
        s_tab_subdes = s_tab_subdes.toUpperCase();
        String s_tab_des = ttabGen.getTab_des().toUpperCase();
        int i_tab_ord = ttabGen.getTab_ord();
        int i_tab_est = ttabGen.getTab_est();
        String s_tab_nomrep = ttabGen.getTab_nomrep().toUpperCase();
        String s_tabusumod = ttabGen.getTab_usumod();
        String SQL_UPDATE_PROC_MARCAS = "{call pack_ttabgen.p_006_actualizarMarcas(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_MARCAS);
            cst.clearParameters();
            cst.setInt(1, i_tab_id);
            cst.setString(2, s_tab_subdes);
            cst.setInt(3, i_tab_ord);
            cst.setInt(4, i_tab_est);
            cst.setString(5, s_tab_nomrep);
            cst.setString(6, s_tabusumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<TtabGen> lstMarcas(String estado) throws SQLException {
        String SQL_MARCAS;
        if ("0".equals(estado)) {
            SQL_MARCAS = " select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est <> " + estado + "  order by t.tab_id ";
        } else if ("1".equals(estado)) {
            SQL_MARCAS = " select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est = " + estado + "  order by t.tab_id ";
        } else {
            SQL_MARCAS = " select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est = 1  order by t.tab_subdes ";
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MARCAS);
            objlsttabGen = new ListModelList<TtabGen>();
            while (rs.next()) {
                obj_tabgen = new TtabGen();
                obj_tabgen.setTab_cod(rs.getInt("tab_cod"));
                obj_tabgen.setTab_id(rs.getInt("tab_id"));
                obj_tabgen.setTab_subdes(rs.getString("tab_subdes"));
                obj_tabgen.setTab_subdir(rs.getString("tab_subdir"));
                obj_tabgen.setTab_est(rs.getInt("tab_est"));
                obj_tabgen.setTab_ord(rs.getInt("tab_ord"));
                obj_tabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                obj_tabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                obj_tabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                obj_tabgen.setTab_usumod(rs.getString("tab_usumod"));
                obj_tabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlsttabGen.add(obj_tabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlsttabGen.getSize() + " registro(s)");
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
        return objlsttabGen;
    }

    public ListModelList<TtabGen> Search(String dato, int indice, int i_estado) throws SQLException {
        String SQL_MARCAS_SEARCH;
        if (i_estado == 3) {
            if (indice == 0) {
                SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est <>0 order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est <>0 and t.tab_id like '" + dato + "'  order by t.tab_id";
                } else {
                    SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est <>0 and t.tab_subdes like '" + dato + "' order by t.tab_id";
                }
            }
        } else {
            if (indice == 0) {
                SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est=" + i_estado + "order by t.tab_id";
            } else {
                if (indice == 1) {
                    SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est=" + i_estado + " and t.tab_subdir like '" + dato + "'  order by t.tab_id";
                } else {
                    SQL_MARCAS_SEARCH = "select t.tab_cod,t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_ord,t.tab_nomrep,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='6' and t.tab_est=" + i_estado + " and t.tab_subdes like '" + dato + "' order by t.tab_id";
                }
            }
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MARCAS_SEARCH);
            objlsttabGen = new ListModelList<TtabGen>();
            while (rs.next()) {
                obj_tabgen = new TtabGen();
                obj_tabgen.setTab_cod(rs.getInt("tab_cod"));
                obj_tabgen.setTab_id(rs.getInt("tab_id"));
                obj_tabgen.setTab_subdes(rs.getString("tab_subdes"));
                obj_tabgen.setTab_subdir(rs.getString("tab_subdir"));
                obj_tabgen.setTab_est(rs.getInt("tab_est"));
                obj_tabgen.setTab_ord(rs.getInt("tab_ord"));
                obj_tabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                obj_tabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                obj_tabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                obj_tabgen.setTab_usumod(rs.getString("tab_usumod"));
                obj_tabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlsttabGen.add(obj_tabgen);
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
        return objlsttabGen;
    }

    public ListModelList<TtabGen> busquedaLovMarcas(String buscar) throws SQLException {
        String SQL_LOVMARCAS = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                + "where t.tab_cod= 6 and t.tab_est=1 and (t.tab_id like '" + buscar + "' or t.tab_subdes like '" + buscar + "') order by t.tab_id";
        
        objlsttabGen = null;
        objlsttabGen = new ListModelList<TtabGen>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LOVMARCAS);
            while (rs.next()) {
                obj_tabgen = new TtabGen();
                obj_tabgen.setTab_id(rs.getInt("tab_id"));
                obj_tabgen.setTab_subdes(rs.getString("tab_subdes"));
                obj_tabgen.setTab_subdir(rs.getString("tab_subdir"));
                obj_tabgen.setTab_est(rs.getInt("tab_est"));
                obj_tabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                obj_tabgen.setTab_ord(rs.getInt("tab_ord"));
                obj_tabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                obj_tabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                obj_tabgen.setTab_usumod(rs.getString("tab_usumod"));
                obj_tabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlsttabGen.add(obj_tabgen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlsttabGen.getSize() + " registro(s)");
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
        return objlsttabGen;
    }

    public TtabGen busquedaMarcaxCodigo(int marca) throws SQLException {
        String SQL_LOVMARCAS = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                + "where t.tab_cod= 6 and t.tab_est=1 and t.tab_id=" + marca + " order by t.tab_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LOVMARCAS);
            while (rs.next()) {
                obj_tabgen = new TtabGen();
                obj_tabgen.setTab_id(rs.getInt("tab_id"));
                obj_tabgen.setTab_subdes(rs.getString("tab_subdes"));
                obj_tabgen.setTab_subdir(rs.getString("tab_subdir"));
                obj_tabgen.setTab_est(rs.getInt("tab_est"));
                obj_tabgen.setTab_nomrep(rs.getString("tab_nomrep"));
                obj_tabgen.setTab_ord(rs.getInt("tab_ord"));
                obj_tabgen.setTab_usuadd(rs.getString("tab_usuadd"));
                obj_tabgen.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                obj_tabgen.setTab_usumod(rs.getString("tab_usumod"));
                obj_tabgen.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlsttabGen.getSize() + " registro(s)");
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
        return obj_tabgen;
    }
    
    public String validaMovimientos(TtabGen objTtabGen) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientosmar(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, objTtabGen.getTab_subdir());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la marca " + objTtabGen.getTab_subdir());
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
