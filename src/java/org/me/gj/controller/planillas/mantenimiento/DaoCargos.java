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
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManCargos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoCargos {

    //Instancias de Objetos
    ListModelList<ManCargos> objlstCargos;
    ManCargos objCargos;
    //Variables Publicas
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
    public String insertar(ManCargos objCargos) throws SQLException {
        String s_cargo_des = objCargos.getCargo_des();
        String s_cargo_usuadd = objCargos.getCargo_usuadd();
        String SQL_INSERTAR_CARGO = "{call codijisa.pack_tpltablas1.p_insertarCargo(?,?,?,?)}";

        try {

            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CARGO);
            cst.clearParameters();
            cst.setString(1, s_cargo_des);
            cst.setString(2, s_cargo_usuadd);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_cargo_des);
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

    public String modificar(ManCargos objCargos) throws SQLException {
        String s_cargo_id = objCargos.getCargo_id();
        String s_cargo_des = objCargos.getCargo_des();
        int s_cargo_est = objCargos.getCargo_est();
        String s_cargo_usumod = objCargos.getCargo_usumod();

        String SQL_MODIFICAR_CARGO = "{call codijisa.pack_tpltablas1.p_modificarCargo(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_CARGO);
            cst.clearParameters();
            cst.setString(1, s_cargo_id);
            cst.setString(2, s_cargo_des);
            cst.setInt(3, s_cargo_est);
            cst.setString(4, s_cargo_usumod);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objCargos.getCargo_id());
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

    public String eliminar(ManCargos objCargos) throws SQLException {

        String s_cargo_id = objCargos.getCargo_id();

        String SQL_ELIMINAR_CARGO = "{call codijisa.pack_tpltablas1.p_eliminarCargo(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CARGO);
            cst.clearParameters();
            cst.setString(1, s_cargo_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objCargos.getCargo_id());
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

    //Eventos Secundarios - Listas y Validaciones
    public ListModelList<ManCargos> listaCargos(int s_cargo_est) throws SQLException {
        String sql_listacargos;

        sql_listacargos = "{call codijisa.pack_tpltablas1.p_listcargos(?,?,?)}";

        //P_WHERE = sql_listacargos; conrep1

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacargos);
            cst.setInt(1, s_cargo_est);
            cst.registerOutParameter(2, OracleTypes.CURSOR);//REF CURSOR
            cst.registerOutParameter(3, OracleTypes.VARCHAR);//CADENA
            cst.execute();
            
            P_WHERE = cst.getString(3);
            
            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objCargos = null;
            objlstCargos = new ListModelList<ManCargos>();

            while (rs.next()) {
                objCargos = new ManCargos();
                objCargos.setCargo_id(rs.getString("TABLA_ID"));
                objCargos.setCargo_des(rs.getString("TABLA_DESCRI"));
                objCargos.setCargo_est(rs.getInt("TABLA_ESTADO"));
                objCargos.setCargo_usuadd(rs.getString("TABLA_USUADD"));
                objCargos.setCargo_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objCargos.setCargo_usumod(rs.getString("TABLA_USUMOD"));
                objCargos.setCargo_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstCargos.add(objCargos);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }

        return objlstCargos;

    }

    public ListModelList<ManCargos> busquedaCargos(int seleccion, String consulta, int s_cargo_est) throws SQLException {
        String sql = "";

        if (s_cargo_est == 3) {
            if (seleccion == 0) {
                sql = "select t.* from CODIJISA.Tpltablas1 t "
                        + "   where t.tabla_cod = '00006' "
                        + "   and  t.tabla_id <> '000' "
                        + "   order by t.tabla_id";
            } else {
                if (seleccion == 1) {
                    sql = "select t.* from CODIJISA.Tpltablas1 t "
                            + "   where t.tabla_cod = '00006' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_id like '" + consulta + "' "
                            + "   order by t.tabla_id";
                } else if (seleccion == 2) {
                    sql = "select t.* from CODIJISA.Tpltablas1 t "
                            + "   where t.tabla_cod = '00006' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_descri like '" + consulta + "' "
                            + "   order by t.tabla_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select t.* from CODIJISA.Tpltablas1 t "
                        + "   where t.tabla_cod = '00006' "
                        + "   and  t.tabla_id <> '000' "
                        + "   and  t.tabla_estado ='" + s_cargo_est + "' "
                        + "   order by t.tabla_id ";
            } else {
                if (seleccion == 1) {
                    sql = "select t.* from CODIJISA.Tpltablas1 t "
                            + "   where t.tabla_cod = '00006' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_estado ='" + s_cargo_est + "' "
                            + "   and   t.tabla_id like '" + consulta + "' "
                            + "   order by t.tabla_id";
                } else if (seleccion == 2) {
                    sql = "select t.* from CODIJISA.Tpltablas1 t "
                            + "   where t.tabla_cod = '00006' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_estado ='" + s_cargo_est + "' "
                            + "   and   t.tabla_descri like '" + consulta + "' "
                            + "   order by t.tabla_id";
                }
            }
        }
        P_WHERE = sql;
        objlstCargos = new ListModelList<ManCargos>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objCargos = new ManCargos();
                objCargos.setCargo_id(rs.getString("TABLA_ID"));
                objCargos.setCargo_des(rs.getString("TABLA_DESCRI"));
                objCargos.setCargo_est(rs.getInt("TABLA_ESTADO"));
                objCargos.setCargo_usuadd(rs.getString("TABLA_USUADD"));
                objCargos.setCargo_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objCargos.setCargo_usumod(rs.getString("TABLA_USUMOD"));
                objCargos.setCargo_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstCargos.add(objCargos);
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
        return objlstCargos;

    }

}
