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
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoManAreas {

    //INSTANCIAS DE OBJETOS
    ListModelList<ManAreas> objlstAreas;
    ManAreas objAreas;
    //LOV CENTRO COSTOS
    ListModelList<ManCCostos> objlstCCostos;
    ManCCostos objCCostos;

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

    //EVENTOS PRIMARIOS - TRANSACCIONALES
    public String insertar(ManAreas objAreas) throws SQLException {
        String s_area_des = objAreas.getArea_des();
        String s_area_usuadd = objAreas.getArea_usuadd();
        String s_area_1 = objAreas.getArea_cod_costo();

        String SQL_INSERTAR_AREAS = "{call codijisa.pack_tpltablas1.p_insertarAreas(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_AREAS);
            cst.clearParameters();
            cst.setString(1, s_area_des);
            cst.setString(2, s_area_usuadd);
            cst.setString(3, s_area_1);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_area_des);
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

    public String modificar(ManAreas objAreas) throws SQLException {

        String s_area_id = objAreas.getArea_id();
        String s_area_des = objAreas.getArea_des();
        int s_area_est = objAreas.getArea_est();
        String s_area_1 = objAreas.getArea_cod_costo();
        String s_area_usumod = objAreas.getArea_usumod();
        String SQL_MODIFICAR_AREAS = "{call codijisa.pack_tpltablas1.p_modificarAreas(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_AREAS);
            cst.clearParameters();
            cst.setString(1, s_area_id);
            cst.setString(2, s_area_des);
            cst.setInt(3, s_area_est);
            cst.setString(4, s_area_1);
            cst.setString(5, s_area_usumod);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objAreas.getArea_id());

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

    public String eliminar(ManAreas objAreas) throws SQLException {

        String s_area_id = objAreas.getArea_id();

        String SQL_ELIMINAR_CARGO = "{call codijisa.pack_tpltablas1.p_eliminarAreas(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CARGO);
            cst.clearParameters();
            cst.setString(1, s_area_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objAreas.getArea_id());
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

    //EVENTOS SECUNDARIOS - LISTAS Y VALIDACIONES
    public ListModelList<ManAreas> listaAreas(int n_area_est) throws SQLException {

        String sql_lista_areas;

        sql_lista_areas = "{call codijisa.pack_tpltablas1.p_listareas(?,?,?)}";

        //P_WHERE = sql_lista_areas;
        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_lista_areas);
            cst.setInt(1, n_area_est);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();

            P_WHERE = cst.getString(3);

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objAreas = null;
            objlstAreas = new ListModelList<ManAreas>();

            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("TABLA_ID"));
                objAreas.setArea_des(rs.getString("TABLA_DESCRI"));
                objAreas.setArea_des_costo(rs.getString("cc_descri"));
                objAreas.setArea_cod_costo(rs.getString("TABLA_VALOR1"));
                objAreas.setArea_est(rs.getInt("TABLA_ESTADO"));
                objAreas.setArea_usuadd(rs.getString("TABLA_USUADD"));
                objAreas.setArea_usumod(rs.getString("TABLA_USUMOD"));
                objAreas.setArea_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objAreas.setArea_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstAreas.add(objAreas);

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

        return objlstAreas;

    }

    public ListModelList<ManAreas> busquedaAreas(int seleccion, String consulta, int s_area_est) throws SQLException {

        String sql = "";
        if (s_area_est == 3) {
            if (seleccion == 0) {
                sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                        + "where t.tabla_valor1 = c.plccosto "
                        + "and   t.tabla_cod = '00003' "
                        + "and   t.tabla_id <> '000' "
                        + "order by t.tabla_id";
            } else {
                if (seleccion == 1) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   t.tabla_id like '" + consulta + "' "
                            + "order by t.tabla_id";
                } else if (seleccion == 2) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   t.tabla_descri like '" + consulta + "' "
                            + "order by t.tabla_id";

                } else if (seleccion == 3) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   c.cc_descri like '" + consulta + "' "
                            + "order by t.tabla_id";

                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                        + "where t.tabla_valor1 = c.plccosto "
                        + "and   t.tabla_cod = '00003' "
                        + "and   t.tabla_id <> '000' "
                        + "and   t.tabla_estado = '" + s_area_est + "' "
                        + "order by t.tabla_id";

            } else {
                if (seleccion == 1) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   t.tabla_estado = '" + s_area_est + "' "
                            + "and   t.tabla_id like '" + consulta + "' "
                            + "order by t.tabla_id";

                } else if (seleccion == 2) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   t.tabla_estado = '" + s_area_est + "' "
                            + "and   t.tabla_descri like '" + consulta + "' "
                            + "order by t.tabla_id";
                } else if (seleccion == 3) {
                    sql = "select t.*,c.cc_descri cc_descri from tpltablas1 t, tccostos c "
                            + "where t.tabla_valor1 = c.plccosto "
                            + "and   t.tabla_cod = '00003' "
                            + "and   t.tabla_id <> '000' "
                            + "and   t.tabla_estado = '" + s_area_est + "' "
                            + "and   c.cc_descri like '" + consulta + "' "
                            + "order by t.tabla_id";

                }

            }
        }
        P_WHERE = sql;
        objlstAreas = new ListModelList<ManAreas>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("TABLA_ID"));
                objAreas.setArea_des(rs.getString("TABLA_DESCRI"));
                objAreas.setArea_des_costo(rs.getString("cc_descri"));
                objAreas.setArea_cod_costo(rs.getString("TABLA_VALOR1"));
                objAreas.setArea_est(rs.getInt("TABLA_ESTADO"));
                objAreas.setArea_usuadd(rs.getString("TABLA_USUADD"));
                objAreas.setArea_usumod(rs.getString("TABLA_USUMOD"));
                objAreas.setArea_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objAreas.setArea_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstAreas.add(objAreas);
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
        return objlstAreas;

    }

    // LOV CENTRO COSTOS
    public ListModelList<ManCCostos> busquedaLovCCostos() throws SQLException {

        String sql_costos = " select c.plccosto, c.cc_descri from codijisa.tccostos c"
                        + " where "
                        + " c.cc_estado = '1'"
                        + " order by c.plccosto";

        objlstCCostos = new ListModelList<ManCCostos>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_costos);
            while (rs.next()) {
                objCCostos = new ManCCostos();
                objCCostos.setCosto_cod(rs.getString("plccosto"));
                objCCostos.setCosto_des(rs.getString("cc_descri"));
                objlstCCostos.add(objCCostos);

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

        return objlstCCostos;

    }

    public ListModelList<ManCCostos> busquedaLovCCostos2(String consulta) throws SQLException {

        String sql_costos = "select c.plccosto,c.cc_descri  from tccostos c"
                + " where c.cc_descri like '%" + consulta + "%' ";

        objlstCCostos = new ListModelList<ManCCostos>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_costos);
            while (rs.next()) {
                objCCostos = new ManCCostos();
                objCCostos.setCosto_cod(rs.getString("plccosto"));
                objCCostos.setCosto_des(rs.getString("cc_descri"));
                objlstCCostos.add(objCCostos);

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

        return objlstCCostos;

    }

    public ManCCostos getLovCCostos(String costo) throws SQLException {
        String sql_costos = "select c.plccosto ,c.cc_descri  from tccostos c"
                + " where c.plccosto like '%" + costo + "%'";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_costos);
            objCCostos = null;
            while (rs.next()) {
                objCCostos = new ManCCostos();
                objCCostos.setCosto_cod(rs.getString("plccosto"));
                objCCostos.setCosto_des(rs.getString("cc_descri"));

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

        return objCCostos;

    }
}
