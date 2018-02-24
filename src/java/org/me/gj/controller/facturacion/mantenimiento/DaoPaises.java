package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Pais;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPaises {

    ListModelList<Pais> objlstPaises;
    Pais objPais;
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
    private static final Logger LOGGER = Logger.getLogger(DaoPaises.class);
    
    //Eventos Primarios - Transaccionales
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Pais> listaPaises(int i_caso) throws SQLException {
        String SQL_PAIS;
        if (i_caso == 0) {
            SQL_PAIS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep "
                    + "from ttabgen p where p.tab_cod=38 and p.tab_ord=1 and p.tab_est<>" + i_caso + " order by p.tab_id";
        } else {
            SQL_PAIS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep "
                    + "from ttabgen p where p.tab_cod=38 and p.tab_ord=1 and p.tab_est=" + i_caso + " order by p.tab_ord,p.tab_id";
        }
        objlstPaises = new ListModelList<Pais>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PAIS);
            while (rs.next()) {
                objPais = new Pais();
                objPais.setTab_id(rs.getInt(1));
                objPais.setTab_subdes(rs.getString(2));
                objPais.setTab_subdir(rs.getString(3));
                objPais.setTab_est(rs.getInt(4));
                objPais.setTab_ord(rs.getInt(5));
                objPais.setTab_nomrep(rs.getString(6));
                objlstPaises.add(objPais);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPaises.getSize() + " registro(s)");
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
        return objlstPaises;
    }

    public ListModelList<Pais> busquedaPaises(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;
        if (seleccion == 1) {
            SQL_BUSQUEDA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep "
                    + "from ttabgen p where p.tab_cod=38 and p.tab_ord=1 and p.tab_est=" + i_estado + " and tab_subdir like '" + consulta + "' order by tab_id";
        } else {
            SQL_BUSQUEDA = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep "
                    + "from ttabgen p where p.tab_cod=38 and p.tab_ord=1 and p.tab_est=" + i_estado + " and tab_subdes like '" + consulta + "' order by tab_id";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstPaises = new ListModelList<Pais>();
            while (rs.next()) {
                objPais = new Pais();
                objPais.setTab_id(rs.getInt(1));
                objPais.setTab_subdes(rs.getString(2));
                objPais.setTab_subdir(rs.getString(3));
                objPais.setTab_est(rs.getInt(4));
                objPais.setTab_ord(rs.getInt(5));
                objPais.setTab_nomrep(rs.getString(6));
                objlstPaises.add(objPais);
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
        return objlstPaises;
    }

    public Pais busquedaPais(int pais_id) throws SQLException {
        String SQL_PAIS = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep "
                + "from ttabgen p where p.tab_cod=38 and p.tab_est=1 and p.tab_id=" + pais_id + " order by p.tab_id";
        objPais = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PAIS);
            while (rs.next()) {
                objPais = new Pais();
                objPais.setTab_id(rs.getInt(1));
                objPais.setTab_subdes(rs.getString(2));
                objPais.setTab_subdir(rs.getString(3));
                objPais.setTab_est(rs.getInt(4));
                objPais.setTab_ord(rs.getInt(5));
                objPais.setTab_nomrep(rs.getString(6));
                objlstPaises.add(objPais);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstPaises.getSize() + " registro(s)");
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
        return objPais;
    }
}
