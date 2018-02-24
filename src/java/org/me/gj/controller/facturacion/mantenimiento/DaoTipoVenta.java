package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoTipoVenta {

    //Instancias de Objetos
    ListModelList<TipoVenta> objlstTipoVentas;
    TipoVenta objTipoVenta;
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
    private static final Logger LOGGER = Logger.getLogger(DaoTipoVenta.class);

    //Eventos Primarios - Transaccionales
    public String insertarTipoVenta(TipoVenta objTipoVenta) throws SQLException {

        String SQL_INSERTAR_TIPOVENTA = "{call pack_ttabgen.p_011_insertarTipoVenta(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TIPOVENTA);
            cst.clearParameters();
            cst.setString(1, objTipoVenta.getTab_subdes());
            cst.setString(2, objTipoVenta.getTab_nomrep());
            cst.setInt(3, objTipoVenta.getTab_ord());
            cst.setInt(4, objTipoVenta.getTab_tip());
            cst.setString(5, objTipoVenta.getTab_usuadd());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objTipoVenta.getTab_subdes());
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

    public String actualizarTipoVenta(TipoVenta objTipoVenta) throws SQLException {

        String SQL_ACTUALIZAR_TIPOVENTA = "{call pack_ttabgen.p_011_actualizarTipoVenta(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TIPOVENTA);
            cst.clearParameters();
            cst.setInt(1, objTipoVenta.getTab_id());
            cst.setString(2, objTipoVenta.getTab_subdes());
            cst.setString(3, objTipoVenta.getTab_nomrep());
            cst.setInt(4, objTipoVenta.getTab_ord());
            cst.setInt(5, objTipoVenta.getTab_tip());
            cst.setInt(6, objTipoVenta.getTab_est());
            cst.setString(7, objTipoVenta.getTab_usumod());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objTipoVenta.getTab_id());
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

    public String eliminarTipoVenta(TipoVenta objTipoVenta) throws SQLException {

        String SQL_ELIMINAR_TIPOVENTA = "{call pack_ttabgen.p_011_eliminarTipoVenta(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_TIPOVENTA);
            cst.clearParameters();
            cst.setInt(1, objTipoVenta.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objTipoVenta.getTab_id());
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
    public ListModelList<TipoVenta> listaTipoVentas(int i_caso) throws SQLException {
        String SQL_TIPOVENTA;
        if (i_caso == 0) {
            SQL_TIPOVENTA = "select tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_tip,p.tab_ord,p.tab_nomrep, "
                    + "p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod "
                    + "from ttabgen p where p.tab_cod=11 and p.tab_est<>" + i_caso + " order by p.tab_ord,p.tab_id";
        } else {
            SQL_TIPOVENTA = "select tab_id ,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_tip,p.tab_ord,p.tab_nomrep, "
                    + "p.tab_usuadd, p.tab_fecadd, p.tab_usumod, p.tab_fecmod "
                    + "from ttabgen p where p.tab_cod=11 and p.tab_est=" + i_caso + " order by p.tab_ord,p.tab_id";
        }
        P_WHERE = SQL_TIPOVENTA;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_TIPOVENTA);
            objlstTipoVentas = new ListModelList<TipoVenta>();
            while (rs.next()) {
                objTipoVenta = new TipoVenta();
                objTipoVenta.setTab_id(rs.getInt("tab_id"));
                objTipoVenta.setTab_subdes(rs.getString("tab_subdes"));
                objTipoVenta.setTab_subdir(rs.getString("tab_subdir"));
                objTipoVenta.setTab_est(rs.getInt("tab_est"));
                objTipoVenta.setTab_tip(rs.getInt("tab_tip"));
                objTipoVenta.setTab_ord(rs.getInt("tab_ord"));
                objTipoVenta.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipoVenta.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipoVenta.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipoVenta.setTab_usumod(rs.getString("tab_usumod"));
                objTipoVenta.setTab_fecmod(rs.getTimestamp("tab_fecmod"));

                objlstTipoVentas.add(objTipoVenta);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTipoVentas.getSize() + " registro(s)");
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
        return objlstTipoVentas;
    }

    public ListModelList<TipoVenta> busquedaTipoVentas(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;
        if (i_estado == 3) {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=11 and p.tab_id<>0  and p.tab_est <>0 order by tab_ord,p.tab_id";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=11 and p.tab_id<>0  and p.tab_est <>0 and tab_subdir like '" + consulta + "' order by tab_ord,tab_id";
                } else {
                    SQL_BUSQUEDA = "select  * from ttabgen p where p.tab_cod=11 and p.tab_id<>0  and p.tab_est <>0 and tab_subdes like '" + consulta + "' order by tab_ord,tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=11 and p.tab_id<>0  and p.tab_est=" + i_estado + " order by tab_id";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=11 and p.tab_id<>0  and p.tab_est=" + i_estado + " and tab_subdir like '" + consulta + "' order by tab_ord,tab_id";
                } else {
                    SQL_BUSQUEDA = "select * from ttabgen p where p.tab_cod=11 and p.tab_id<>0 and p.tab_est=" + i_estado + " and tab_subdes like '" + consulta + "' order by tab_ord,tab_id";
                }
            }
        }
        P_WHERE = SQL_BUSQUEDA;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstTipoVentas = new ListModelList<TipoVenta>();
            while (rs.next()) {
                objTipoVenta = new TipoVenta();
                objTipoVenta.setTab_id(rs.getInt("tab_id"));
                objTipoVenta.setTab_subdes(rs.getString("tab_subdes"));
                objTipoVenta.setTab_subdir(rs.getString("tab_subdir"));
                objTipoVenta.setTab_est(rs.getInt("tab_est"));
                objTipoVenta.setTab_tip(rs.getInt("tab_tip"));
                objTipoVenta.setTab_ord(rs.getInt("tab_ord"));
                objTipoVenta.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipoVenta.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipoVenta.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipoVenta.setTab_usumod(rs.getString("tab_usumod"));
                objTipoVenta.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTipoVentas.add(objTipoVenta);
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
        return objlstTipoVentas;
    }

    public TipoVenta getDesTipoVenta(int tipven_id) throws SQLException {
        String SQL_BUSQUEDA = " select p.tab_id , p.tab_subdes from ttabgen p where p.tab_cod = '11' and p.tab_est <> 0 and p.tab_id =  " + tipven_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objTipoVenta = null;
            while (rs.next()) {
                objTipoVenta = new TipoVenta();
                objTipoVenta.setTab_id(rs.getInt(1));
                objTipoVenta.setTab_subdes(rs.getString(2));
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
        return objTipoVenta;
    }

    public TipoVenta getDesTipoVenta(String tipven_id) throws SQLException {
        String SQL_BUSQUEDA = " select p.tab_id , p.tab_subdes from ttabgen p where p.tab_cod = '11' and p.tab_est <> 0 and p.tab_subdir =  " + tipven_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objTipoVenta = null;
            while (rs.next()) {
                objTipoVenta = new TipoVenta();
                objTipoVenta.setTab_id(rs.getInt(1));
                objTipoVenta.setTab_subdes(rs.getString(2));
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
        return objTipoVenta;
    }

}
