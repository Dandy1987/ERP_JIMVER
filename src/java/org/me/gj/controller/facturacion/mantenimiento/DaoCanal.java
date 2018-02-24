package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoCanal {

    //Instancias de Objetos
    ListModelList<Canal> objlstCanales;
    Canal objCanal;
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
    private static final Logger LOGGER = Logger.getLogger(DaoCanal.class);

    //Eventos Primarios - Transaccionales
    public String insertarCanal(Canal objCanal) throws SQLException {
        String tab_subdes = objCanal.getTab_subdes();
        String tab_nomrep = objCanal.getTab_nomrep();
        int tab_ord = objCanal.getTab_ord();
        String tab_usuadd = objCanal.getTab_usuadd();
        String SQL_INSERTAR_CANAL = "{call pack_ttabgen.p_028_insertarCanal(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CANAL);
            cst.clearParameters();
            cst.setString(1, tab_subdes);
            cst.setString(2, tab_nomrep);
            cst.setInt(3, tab_ord);
            cst.setString(4, tab_usuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + tab_subdes);
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

    public String actualizarCanal(Canal objCanal) throws SQLException {
        int i_tabid = objCanal.getTab_id();
        String s_tabsubdes = objCanal.getTab_subdes();
        String s_tabnomrep = objCanal.getTab_nomrep();
        int i_tabord = objCanal.getTab_ord();
        int i_tabest = objCanal.getTab_est();
        String tabusumod = objCanal.getTab_usumod();
        String SQL_ACTUALIZAR_CANAL = "{call pack_ttabgen.p_028_actualizarCanal(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_CANAL);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setString(5, tabusumod);
            cst.setInt(6, i_tabest);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + i_tabid);
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

    public String eliminarCanal(Canal objCanal) throws SQLException {
        int i_tabid = objCanal.getTab_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_ttabgen.p_028_eliminarCanal(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_tabid);
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
    public ListModelList<Canal> listaCanales(int i_caso) throws SQLException {
        String SQL_CANALES;
        if (i_caso == 0) {
            SQL_CANALES = "select lpad(p.tab_id,2,0) tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep, "
                    + "p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod "
                    + "from ttabgen p where p.tab_cod=28 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_CANALES = "select lpad(p.tab_id,2,0) tab_id ,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep, "
                    + "p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod "
                    + "from ttabgen p where p.tab_cod=28 and p.tab_est=" + i_caso + " order by p.tab_id";
        }
        P_WHERE = SQL_CANALES;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CANALES);
            objlstCanales = new ListModelList<Canal>();
            while (rs.next()) {
                objCanal = new Canal();
                objCanal.setTab_id(rs.getInt("tab_id"));
                objCanal.setTab_subdes(rs.getString("tab_subdes"));
                objCanal.setTab_subdir(rs.getString("tab_subdir"));
                objCanal.setTab_est(rs.getInt("tab_est"));
                objCanal.setTab_ord(rs.getInt("tab_ord"));
                objCanal.setTab_usuadd(rs.getString("tab_usuadd"));
                objCanal.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objCanal.setTab_usumod(rs.getString("tab_usumod"));
                objCanal.setTab_fecmod(rs.getTimestamp("tab_fecmod"));

                objlstCanales.add(objCanal);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstCanales.getSize() + " registro(s)");
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
        return objlstCanales;
    }

    public ListModelList<Canal> busquedaCanales(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA="";
        if (i_estado == 3) {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=28 and p.tab_id<>0  and p.tab_est <>0 order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=28 and p.tab_id<>0  and p.tab_est <>0 and tab_subdir like '" + consulta + "' order by tab_id";
                } else if((seleccion == 2)) {
                    SQL_BUSQUEDA = "select  * from ttabgen p where p.tab_cod=28 and p.tab_id<>0  and p.tab_est <>0 and tab_subdes like '" + consulta + "' order by tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=28 and p.tab_id<>0  and p.tab_est=" + i_estado + " order by tab_id";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=28 and p.tab_id<>0  and p.tab_est=" + i_estado + " and tab_subdir like '" + consulta + "' order by tab_id";
                } else if((seleccion == 2)) {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=28 and p.tab_id<>0 and p.tab_est=" + i_estado + " and tab_subdes like '" + consulta + "' order by tab_id";
                }
            }
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstCanales = new ListModelList<Canal>();
            while (rs.next()) {
                objCanal = new Canal();
                objCanal.setTab_id(rs.getInt("tab_id"));
                objCanal.setTab_subdes(rs.getString("tab_subdes"));
                objCanal.setTab_subdir(rs.getString("tab_subdir"));
                objCanal.setTab_est(rs.getInt("tab_est"));
                objCanal.setTab_ord(rs.getInt("tab_ord"));
                objCanal.setTab_nomrep(rs.getString("tab_nomrep"));
                objCanal.setTab_usuadd(rs.getString("tab_usuadd"));
                objCanal.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objCanal.setTab_usumod(rs.getString("tab_usumod"));
                objCanal.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstCanales.add(objCanal);
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
        return objlstCanales;
    }

    public Canal getDesCanal(int can_id) throws SQLException {
        String SQL_BUSQUEDA = " select p.tab_id , p.tab_subdes from ttabgen p where p.tab_cod = '28' and p.tab_est <> 0 and p.tab_id =  " + can_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objCanal = null;
            while (rs.next()) {
                objCanal = new Canal();
                objCanal.setTab_id(rs.getInt(1));
                objCanal.setTab_subdes(rs.getString(2));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCanal;
    }

    public Canal getDesCanal(String can_id) throws SQLException {
        String SQL_BUSQUEDA = " select p.tab_id , p.tab_subdes from ttabgen p where p.tab_cod = '28' and p.tab_est <> 0 and p.tab_subdir =  " + can_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objCanal = null;
            while (rs.next()) {
                objCanal = new Canal();
                objCanal.setTab_id(rs.getInt(1));
                objCanal.setTab_subdes(rs.getString(2));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objCanal;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC = "";
        int valor = 0;
        if (tipo == 1) {//
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tvendedores t, ttabgen p where t.tab_id = p.tab_id and t.ven_est<>0 and p.tab_est<>0"
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and p.tab_id=" + dato;
        } else if (tipo == 2) {//
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tmesas t, ttabgen p where t.mes_canalid = p.tab_id and t.mes_est<>0 and p.tab_est<>0"
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and p.tab_id=" + dato;
        } else if (tipo == 3) {//
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from trutas t, ttabgen p where t.rut_canalid = p.tab_id and t.rut_est<>0 and p.tab_est<>0"
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and p.tab_id=" + dato;
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Canal debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
}
