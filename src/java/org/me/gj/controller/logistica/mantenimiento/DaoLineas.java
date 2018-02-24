package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Lineas;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;

public class DaoLineas {

    //Instancias de Objetos
    ListModelList<Lineas> objlstLineas;
    Lineas objLineas;
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
    private static final Logger LOGGER = Logger.getLogger(DaoLineas.class);

    //Eventos Primarios - Transaccionales
    public String insertar(Lineas linea) throws SQLException {
        String s_desLin = linea.getTab_subdes();
        int i_ordLin = linea.getTab_ord();
        String s_ordNomRep = linea.getTab_nomrep();
        String s_tabusuadd = linea.getTab_usuadd();
        String SQL_INSERT_PROC_LINEAS = "{call pack_ttabgen.p_004_insertarLinea(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_LINEAS);
            cst.clearParameters();
            cst.setString(1, s_desLin);
            cst.setInt(2, i_ordLin);
            cst.setString(3, s_ordNomRep);
            cst.setString(4, s_tabusuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_desLin);
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

    public String actualizar(Lineas linea) throws SQLException {
        int i_idLin = linea.getTab_id();
        String s_desLin = linea.getTab_subdes();
        int i_ordLin = linea.getTab_ord();
        int i_estLin = linea.getTab_est();
        String s_ordNomRep = linea.getTab_nomrep();
        String s_tabusumod = linea.getTab_usumod();
        String SQL_UPDATE_PROC_LINEAS = "{call pack_ttabgen.p_004_actualizarLinea(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_LINEAS);
            cst.clearParameters();
            cst.setInt(1, i_idLin);
            cst.setString(2, s_desLin);
            cst.setInt(3, i_ordLin);
            cst.setInt(4, i_estLin);
            cst.setString(5, s_ordNomRep);
            cst.setString(6, s_tabusumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_idLin);
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

    public String eliminar(Lineas linea) throws SQLException {
        int i_idLin = linea.getTab_id();
        String SQL_DELETE_PROC_LINEAS = "{call pack_ttabgen.p_004_eliminarLinea(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_LINEAS);
            cst.clearParameters();
            cst.setInt(1, i_idLin);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_idLin);
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
    public ListModelList<Lineas> lstTipoLineas() throws SQLException {
        String SQL_TIPO_LINEAS = "select t.tab_id,t.tab_subdir,t.tab_subdes,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t where t.tab_cod='4' and t.tab_est='1' order by t.tab_id";
        objlstLineas = null;
        objlstLineas = new ListModelList<Lineas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPO_LINEAS);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstLineas.add(objLineas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstLineas.getSize() + " registro(s)");
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
        return objlstLineas;
    }

    public ListModelList<Lineas> busquedaLovLineas(String buscar) throws SQLException {
        String SQL_LOVLINEAS = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                + "where t.tab_cod= 4 and t.tab_est=1 and (t.tab_id like '" + buscar + "' or t.tab_subdes like '" + buscar + "') order by t.tab_id";
        objlstLineas = null;
        objlstLineas = new ListModelList<Lineas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LOVLINEAS);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_est(rs.getInt("tab_est"));
                objLineas.setTab_nomrep(rs.getString("tab_nomrep"));
                objLineas.setTab_ord(rs.getInt("tab_ord"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstLineas.add(objLineas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstLineas.getSize() + " registro(s)");
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
        return objlstLineas;
    }

    public ListModelList<Lineas> listaLineas(int caso) throws SQLException {
        String sql;
        if (caso == 0) {
            sql = "select * "
                    + "from ttabgen t where t.tab_cod = 4 and t.tab_id <> 0 and tab_est <> " + caso + " order by t.tab_id ";
        } else if (caso == 1) {
            sql = "select * "
                    + "from ttabgen t where t.tab_cod = 4 and t.tab_id <> 0 and tab_est = " + caso + " order by t.tab_id ";
        } else {
            sql = "select * "
                    + "from ttabgen t where t.tab_cod = 4 and t.tab_id <> 0 and tab_est = 1 order by t.tab_subdes ";
        }
        P_WHERE = sql;
        try {
            objlstLineas = new ListModelList<Lineas>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_est(rs.getInt("tab_est"));
                objLineas.setTab_nomrep(rs.getString("tab_nomrep"));
                objLineas.setTab_ord(rs.getInt("tab_ord"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstLineas.add(objLineas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstLineas.getSize() + " registro(s)");
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
        return objlstLineas;
    }

    public ListModelList<Lineas> listaLineasXProd(int caso, String prod) throws SQLException {
        String sql;
        if (caso == 0) {
            sql = "select * from ttabgen t where t.tab_cod = 4 and t.tab_id <> 0 and tab_est <> " + caso + " order by t.tab_id ";
        } else if (caso == 1) {
            sql = "select * from ttabgen t , tproductos tp where tp.pro_lin = t.tab_subdir and "
                    + "tp.pro_id= " + prod + " and  t.tab_cod = 4 and t.tab_id <> 0 and tab_est = " + caso + " order by t.tab_id ";
        } else {
            sql = "select * from ttabgen t where t.tab_cod = 4 and t.tab_id <> 0 and tab_est = 1 order by t.tab_subdes ";
        }
        P_WHERE = sql;
        try {
            objlstLineas = new ListModelList<Lineas>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_est(rs.getInt("tab_est"));
                objLineas.setTab_nomrep(rs.getString("tab_nomrep"));
                objLineas.setTab_ord(rs.getInt("tab_ord"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstLineas.add(objLineas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstLineas.getSize() + " registro(s)");
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
        return objlstLineas;
    }

    public ListModelList<Lineas> busquedaLineas(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select * from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est<>0  order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select * from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdir like '" + consulta + "' order by p.tab_id";
                } else {
                    sql = "select * from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdes like '" + consulta + "' order by p.tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select * from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select *from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdir like '" + consulta + "' order by p.tab_id";
                } else {
                    sql = "select * from ttabgen p where p.tab_cod=4 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta + "' order by p.tab_id";
                }
            }
        }
        P_WHERE = sql;
        try {
            objlstLineas = new ListModelList<Lineas>();
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_est(rs.getInt("tab_est"));
                objLineas.setTab_nomrep(rs.getString("tab_nomrep"));
                objLineas.setTab_ord(rs.getInt("tab_ord"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstLineas.add(objLineas);
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
        return objlstLineas;
    }

    public Lineas busquedaLineaxCodigo(int linea) throws SQLException {
        String SQL_BUSLINEA = "select t.tab_id,t.tab_subdes,t.tab_subdir,t.tab_est,t.tab_nomrep,t.tab_ord,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                + "where t.tab_cod=4 and t.tab_est=1 and t.tab_id=" + linea;
        objLineas = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSLINEA);
            while (rs.next()) {
                objLineas = new Lineas();
                objLineas.setTab_id(rs.getInt("tab_id"));
                objLineas.setTab_subdes(rs.getString("tab_subdes"));
                objLineas.setTab_subdir(rs.getString("tab_subdir"));
                objLineas.setTab_est(rs.getInt("tab_est"));
                objLineas.setTab_nomrep(rs.getString("tab_nomrep"));
                objLineas.setTab_ord(rs.getInt("tab_ord"));
                objLineas.setTab_usuadd(rs.getString("tab_usuadd"));
                objLineas.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objLineas.setTab_usumod(rs.getString("tab_usumod"));
                objLineas.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea con Codigo" + linea);
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
        return objLineas;
    }

    public int consultaNumSubLineas(int i_seleccion) throws SQLException {
        String sql = "select count(p.tab_id) from ttabgen p where p.tab_est<>0 and p.tab_cod=5 and p.tab_tip=" + i_seleccion;
        int numSubLineas = 0;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            numSubLineas = rs.getInt(1);
            st.close();
            rs.close();
            con.close();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + numSubLineas + " registro(s)");
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
        return numSubLineas;
    }
    
    public String validaMovimientos(Lineas objLinea) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientoslin(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objLinea.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la linea " + objLinea.getTab_subdir());
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
