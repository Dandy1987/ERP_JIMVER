/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManConceptos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author achocano
 */
public class DaoManConceptos {

    ListModelList<ManConceptos> objlstConceptos = new ListModelList<ManConceptos>();
    ManConceptos objConceptos;
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
    private static final Logger LOGGER = Logger.getLogger(DaoManConceptos.class);

    public ListModelList<ManConceptos> lstTablas() throws SQLException {
        String sql;
        sql = " select E.*,decode(e.tabla_tipo1,'F','FUNCION','C','CONSTANTE','M','CONSTANTE MENSUAL') tipo from TPLTABLAS1 E "
                + " WHERE E.TABLA_COD='00001'"
                + " and e.tabla_estado=1"
                + " and e.tabla_id<>000 "
                + " order by e.tabla_id";
        P_WHERE = sql;

        objlstConceptos = new ListModelList<ManConceptos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConceptos = new ManConceptos();
                objConceptos.setCodigo(rs.getString("tabla_id"));
                objConceptos.setDescripcion(rs.getString("tabla_descri"));
                objConceptos.setTipo(rs.getString("tabla_tipo1") == null ? " " : rs.getString("tabla_tipo1"));
                objConceptos.setS_boleta(rs.getString("tabla_datbol") == null ? " " : rs.getString("tabla_datbol"));
                objConceptos.setPrioridad(rs.getInt("tabla_prior"));
                objConceptos.setCod_sunat(rs.getString("tabla_valor1"));
                objConceptos.setEstado(rs.getInt("tabla_estado"));
                objConceptos.setDebe(rs.getString("tabla_valor3"));
                objConceptos.setHaber(rs.getString("tabla_valor4"));
                objConceptos.setEstado(rs.getInt("tabla_estado"));
                objConceptos.setUsu_add(rs.getString("tabla_usuadd"));
                objConceptos.setUsu_mod(rs.getString("tabla_usumod"));
                objConceptos.setDia_add(rs.getTimestamp("tabla_fecadd"));
                objConceptos.setDia_mod(rs.getTimestamp("tabla_fecmod"));

                objlstConceptos.add(objConceptos);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstConceptos.getSize() + " registro(s)");
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
        return objlstConceptos;
    }

    public String insertar(ManConceptos objConceptos) throws SQLException {
        String descripcion = objConceptos.getDescripcion();
        String sunat = objConceptos.getCod_sunat();
        String tipo = objConceptos.getG_tipo();
        int prioridad = objConceptos.getPrioridad();
        String debe = objConceptos.getDebe();
        String haber = objConceptos.getHaber();
        int boleta = objConceptos.getBoleta();

        String sql_insertar = "{call pack_tconceptos.p_insertar(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();

            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, descripcion);
            cst.setString(4, sunat);//valor 1
            cst.setString(5, tipo);//valor 2
            cst.setString(6, debe);
            cst.setString(7, haber);
            cst.setInt(8, prioridad);
            cst.setInt(9, boleta);
            cst.setString(10, objUsuCredential.getCuenta());
            cst.setString(11, objUsuCredential.getComputerName());
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + descripcion);
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

    public String modificar(ManConceptos objConceptos) throws SQLException {
        String key = objConceptos.getCodigo();
        int estado = objConceptos.getEstado();
        String descripcion = objConceptos.getDescripcion();
        String sunat = objConceptos.getCod_sunat();
        String tipo = objConceptos.getG_tipo();
        int prioridad = objConceptos.getPrioridad();
        String debe = objConceptos.getDebe();
        String haber = objConceptos.getHaber();
        int boleta = objConceptos.getBoleta();

        String sql_insertar = "{call pack_tconceptos.p_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();

            cst = con.prepareCall(sql_insertar);
            cst.clearParameters();
            //cst.setInt(1, objUsuCredential.getCodemp());
            // cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(1, key);
            cst.setInt(2, estado);
            cst.setString(3, descripcion);
            cst.setString(4, sunat);//valor 1
            cst.setString(5, tipo);//valor 2
            cst.setString(6, debe);
            cst.setString(7, haber);
            cst.setInt(8, prioridad);
            cst.setInt(9, boleta);
            cst.setString(10, objUsuCredential.getCuenta());
            cst.setString(11, objUsuCredential.getComputerName());
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.setInt(14, objUsuCredential.getCodemp());
            cst.setInt(15, objUsuCredential.getCodsuc());
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + descripcion);
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

    public String eliminar(ManConceptos objConceptos) throws SQLException {
        String key = objConceptos.getCodigo();
        String sql_eliminar = "{call pack_tconceptos.p_eliminar(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(sql_eliminar);
            cst.clearParameters();
            cst.setString(1, key);
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setInt(3, objUsuCredential.getCodsuc());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(4);
            s_msg = cst.getString(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objConceptos.getCodigo());
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

    public ListModelList<ManConceptos> consultar(int selec, String consulta, int estado, int tipo) throws SQLException {

        String sql = "", es, tip;

        //para saber el estado a consultar
        if (estado == 3) {
            es = " and t.tabla_estado in ('1','2')";
        } else if (estado == 1) {
            es = " and t.tabla_estado ='1'";
        } else {
            es = " and t.tabla_estado ='2'";
        }

        //para filtrar por tipo 
        if (tipo == 1) {
            tip = " and t.tabla_tipo1 = 'F'";
        } else if (tipo == 2) {
            tip = " and t.tabla_tipo1 = 'C'";
        } else if (tipo == 3) {
            tip = " and t.tabla_tipo1 = 'M'";
        } else {
            tip = " ";
        }
        //tip = "";
        //para buscar por id o descripcion
        if (selec == 0) {
            sql = " select t.*,decode(t.tabla_tipo1,'F','FUNCION','C','CONSTANTE','M','CONSTANTE MENSUAL') tipo from tpltablas1 t  where "
                    + " t.tabla_cod = '00001'"
                    + es
                    + tip
                    + " order by t.tabla_id";
        } else if (selec == 1) {
            sql = " select t.*,decode(t.tabla_tipo1,'F','FUNCION','C','CONSTANTE','M','CONSTANTE MENSUAL') tipo from tpltablas1 t where "
                    + " t.tabla_cod = '00001'"
                    + es
                    + tip
                    + " and t.tabla_id like '" + consulta + "'"
                    + " order by t.tabla_id";

        } else if (selec == 2) {
            sql = " select t.*,decode(t.tabla_tipo1,'F','FUNCION','C','CONSTANTE','M','CONSTANTE MENSUAL') tipo from tpltablas1 t where "
                    + " t.tabla_cod = '00001'"
                    + es
                    + tip
                    + " and t.tabla_descri like '" + consulta + "'"
                    + " order by t.tabla_id";
        }
        
        P_WHERE = sql;

        objlstConceptos = new ListModelList<ManConceptos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConceptos = new ManConceptos();
                objConceptos.setCodigo(rs.getString("tabla_id"));
                objConceptos.setDescripcion(rs.getString("tabla_descri"));
                objConceptos.setTipo(rs.getString("tabla_tipo1") == null ? " " : rs.getString("tabla_tipo1"));
                objConceptos.setS_boleta(rs.getString("tabla_datbol") == null ? " " : rs.getString("tabla_datbol"));
                objConceptos.setPrioridad(rs.getInt("tabla_prior"));
                objConceptos.setCod_sunat(rs.getString("tabla_valor1"));
                objConceptos.setEstado(rs.getInt("tabla_estado"));
                objConceptos.setDebe(rs.getString("tabla_valor3"));
                objConceptos.setHaber(rs.getString("tabla_valor4"));
                objConceptos.setEstado(rs.getInt("tabla_estado"));
                objConceptos.setUsu_add(rs.getString("tabla_usuadd"));
                objConceptos.setUsu_mod(rs.getString("tabla_usumod"));
                objConceptos.setDia_add(rs.getTimestamp("tabla_fecadd"));
                objConceptos.setDia_mod(rs.getTimestamp("tabla_fecmod"));
                objlstConceptos.add(objConceptos);
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
        return objlstConceptos;
    }

}
