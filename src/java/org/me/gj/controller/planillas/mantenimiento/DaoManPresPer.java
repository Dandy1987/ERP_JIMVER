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
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManPresPer;
import org.me.gj.model.planillas.mantenimiento.ManPresPerDet;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoManPresPer {

    ListModelList<ManPresPer> objlstManPresPer;
    ManPresPer objManPresper;

    ListModelList<ManPresPerDet> objlstManPresDet;
    ManPresPerDet objManPresPerDet;
    //LOV PERSONAL
    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;
    //LOV AREAS
    ListModelList<ManAreas> objlstAreas;
    ManAreas objAreas;

    ArrayDescriptor arrayDescriptor;
    ARRAY arr1;

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    ArrayDescriptor arrayC, arrayCM;
    ARRAY arrC, arrCM;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManPresPer.class);

    ///Eventos Primarios - Procedimientos insertar,modificar,eliminar
    public ParametrosSalida insertarPrestamo(ManPresPer objManPresPer, Object[][] listadetalle) throws SQLException {

        String SQL_INSERTAR_PRESTAMO = "{call pack_tplpresper.p_insertarPrestamo(?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_PRESTAMO);
            arrayDescriptor = ArrayDescriptor.createDescriptor("LIS_DETALLE", con.getMetaData().getConnection());
            arr1 = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listadetalle);
            cst.clearParameters();

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, objManPresPer.getTPLPRESCAB_IDPER());
            cst.setString(4, objManPresPer.getTPLPRESCAB_DESPER());
            cst.setString(5, objManPresPer.getTPLPRESCAB_USUADD());

            cst.setDouble(6, objManPresPer.getTlplprescab_monto());
            cst.setInt(7, objManPresPer.getTlplprescab_nrocuo());

            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.setDate(10, new java.sql.Date(objManPresPer.getTPLPRESCAB_FECEMI().getTime()));
            cst.setArray(11, arr1);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);

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

    public ParametrosSalida modificarPrestamo(ManPresPer objManPresPer, Object[][] listadetalle) throws SQLException {

        String SQL_MODIFICAR_PRESTAMO = "{call pack_tplpresper.p_modificarPrestamo(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_PRESTAMO);
            arrayDescriptor = ArrayDescriptor.createDescriptor("LIS_DETALLE", con.getMetaData().getConnection());
            arr1 = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listadetalle);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, objManPresPer.getTPLPRESCAB_ID());
            cst.setString(4, objManPresPer.getTPLPRESCAB_USUMOD());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.setArray(7, arr1);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }

    public ParametrosSalida eliminarPrestamo(ManPresPer objManPresPer) throws SQLException {

        String SQL_ELIMINAR_PRESTAMO = "{call pack_tplpresper.p_eliminarPrestamo(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PRESTAMO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, objManPresPer.getTPLPRESCAB_ID());
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }

        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }

    //Eventos Secundarios - Listas y Validaciones
    public ListModelList<ManPresPer> listaPrestamo(int c_idemp, int c_idsuc) throws SQLException {
        String sql_listaprestamo;

        //c_idemp = rs.getInt(objUsuCredential.getCodemp());
        //c_idsuc = rs.getInt(objUsuCredential.getCodsuc());
        sql_listaprestamo = "{call codijisa.pack_tplpresper.p_listPres(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listaprestamo);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(3);
            st = con.createStatement();

            objManPresper = null;
            objlstManPresPer = new ListModelList<ManPresPer>();

            while (rs.next()) {
                objManPresper = new ManPresPer();
                objManPresper.setTPLPRESCAB_ID(rs.getString("tplprescab_id"));
                objManPresper.setTPLPRESCAB_FECEMI(rs.getDate("tplprescab_fecemi"));
                objManPresper.setTPLPRESCAB_DESPER(rs.getString("tplprescab_desper"));
                objManPresper.setTPLPRESCAB_IDPER(rs.getString("tplprescab_idper"));
                objManPresper.setTPLPRESCAB_USUADD(rs.getString("tplprescab_usuadd"));
                objManPresper.setTPLPRESCAB_FECADD(rs.getTimestamp("tplprescab_fecadd"));
                objManPresper.setTPLPRESCAB_ESTADO(rs.getInt("tplprescab_estado"));
                objManPresper.setTPLPRESCAB_FECMOD(rs.getTimestamp("tplprescab_fecmod"));
                objManPresper.setTPLPRESCAB_USUMOD(rs.getString("tplprescab_usumod"));
                objManPresper.setTlplprescab_monto(rs.getDouble("tplprescab_monto"));
                objManPresper.setTlplprescab_nrocuo(rs.getInt("tplprescab_nrocuo"));
                objlstManPresPer.add(objManPresper);

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

        return objlstManPresPer;

    }

    public ListModelList<ManPresPerDet> listaDetalle(int emp_id, int suc_id, String pres_id) throws SQLException {

        String sql_detalle = "select d.tplpresdet_nrocuota,d.tplpresdet_montcuota,d.tplpresdet_totcuotas,d.tplpresdet_salcuota,d.tplpresdet_ind,d.tplpresdet_fecpago,d.tplpresdet_montotal,d.tplpresdet_situcuota from tplpresdet d "
                + "where d.emp_id = '" + emp_id + "'"
                + "and   d.suc_id = '" + suc_id + "'"
                + "and   d.tplprescab_id = '" + pres_id + "'"
                + "order by d.tplprescab_id,d.tplpresdet_nrocuota";

        try {
            con = (new ConectaBD()).conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_detalle);
            objlstManPresDet = new ListModelList<ManPresPerDet>();
            while (rs.next()) {
                objManPresPerDet = new ManPresPerDet();
                objManPresPerDet.setTPLPRESDET_NROCUOTA(rs.getInt("tplpresdet_nrocuota"));
                objManPresPerDet.setTPLPRESDET_MONTCUOTA(rs.getDouble("tplpresdet_montcuota"));
                objManPresPerDet.setTPLPRESDET_TOTCUOTAS(rs.getInt("tplpresdet_totcuotas"));
                objManPresPerDet.setTPLPRESDET_SALCUOTA(rs.getDouble("tplpresdet_salcuota"));
                objManPresPerDet.setTPLPRESDET_IND(rs.getInt("tplpresdet_ind"));
                objManPresPerDet.setTPLPRESDET_FECPAGO(rs.getDate("tplpresdet_fecpago"));
                objManPresPerDet.setTPLPRESDET_MONTOTAL(rs.getDouble("tplpresdet_montotal"));
                objManPresPerDet.setTPLPRESDET_SITUCUOTA(rs.getInt("tplpresdet_situcuota"));
                objlstManPresDet.add(objManPresPerDet);

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

        return objlstManPresDet;

    }

    public ListModelList<ManPresPer> consultaPrestamos(int seleccion, String consulta, int estado) throws SQLException {

        String SQL_CONSULTA = "";

        if (estado == 3) {
            if (seleccion == 0) {
                SQL_CONSULTA = " select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                        + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                        + " from tplprescab t "
                        + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                        + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                        + "  order by t.tplprescab_id";

            } else {
                if (seleccion == 1) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_id like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                } else if (seleccion == 2) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_desper like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                } else if (seleccion == 3) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_desarea like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                }
            }

        } else {
            if (seleccion == 0) {
                SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                        + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                        + " from tplprescab t "
                        + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                        + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                        + " and   t.tplprescab_estado = '" + estado + "' "
                        + " order by t.tplprescab_id";
            } else {
                if (seleccion == 1) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_estado = '" + estado + "' "
                            + " and   t.tplprescab_id like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                } else if (seleccion == 2) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_estado = '" + estado + "' "
                            + " and   t.tplprescab_desper like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                } else if (seleccion == 3) {
                    SQL_CONSULTA = "select t.tplprescab_id tplprescab_id,t.tplprescab_fecemi tplprescab_fecemi,t.tplprescab_desper , "
                            + " t.tplprescab_idper,t.tplprescab_usuadd,t.tplprescab_fecadd,t.tplprescab_estado,t.tplprescab_fecmod,t.tplprescab_usumod, t.tplprescab_monto, t.tplprescab_nrocuo "
                            + " from tplprescab t "
                            + " where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + " and   t.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                            + " and   t.tplprescab_estado = '" + estado + "' "
                            + " and   t.tplprescab_desarea like '" + consulta + "' "
                            + " order by t.tplprescab_id";

                }

            }

        }
        P_WHERE = SQL_CONSULTA;
        objlstManPresPer = new ListModelList<ManPresPer>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTA);
            while (rs.next()) {
                objManPresper = new ManPresPer();
                objManPresper.setTPLPRESCAB_ID(rs.getString("tplprescab_id"));
                objManPresper.setTPLPRESCAB_FECEMI(rs.getDate("tplprescab_fecemi"));
                objManPresper.setTPLPRESCAB_DESPER(rs.getString("tplprescab_desper"));
                objManPresper.setTPLPRESCAB_IDPER(rs.getString("tplprescab_idper"));
                objManPresper.setTPLPRESCAB_USUADD(rs.getString("tplprescab_usuadd"));
                objManPresper.setTPLPRESCAB_FECADD(rs.getTimestamp("tplprescab_fecadd"));
                objManPresper.setTPLPRESCAB_ESTADO(rs.getInt("tplprescab_estado"));
                objManPresper.setTPLPRESCAB_FECMOD(rs.getTimestamp("tplprescab_fecmod"));
                objManPresper.setTPLPRESCAB_USUMOD(rs.getString("tplprescab_usumod"));
                objManPresper.setTlplprescab_monto(rs.getDouble("tplprescab_monto"));
                objManPresper.setTlplprescab_nrocuo(rs.getInt("tplprescab_nrocuo"));
                objlstManPresPer.add(objManPresper);

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
        return objlstManPresPer;

    }

//LOV AREAS
    public ListModelList<ManAreas> busquedaLovAreas() throws SQLException {

        String sql_areas = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00003' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " order by r.tabla_id";

        objlstAreas = new ListModelList<ManAreas>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_areas);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));
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

    public ListModelList<ManAreas> busquedaLovAreas2(String consulta) throws SQLException {

        String sql_areas = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00003' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstAreas = new ListModelList<ManAreas>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_areas);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));
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

    public ManAreas getLovAreas(String areas) throws SQLException {

        String sql_areas = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00003' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + areas + "%' "
                + " order by r.tabla_id";

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_areas);
            objAreas = null;
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));

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

        return objAreas;

    }

    //LOV PERSONAL
    public ListModelList<Personal> busquedaLovPersonal() throws SQLException {

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + " pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per, "
                + " t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + " from tpersonal p ,tpldatoslab l ,Tpltablas1 t, tempresas e "
                + " where p.pltipdoc = l.pltipdoc "
                + " and   p.plnrodoc = l.plnrodoc "
                + " and   t.tabla_id = l.plidcargo "
                + " and l.emp_id = e.emp_id "
                + " and   t.tabla_cod = '00006' "
                + " and   t.tabla_id <> '000' "
                + " and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + " and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + " and p.plestado = 1" // <> 0
                + " and l.plestado_dl = 1" //= 0
                + " and e.emp_est = 1"
                + " group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + " t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + " order by p.plapepat,p.plnrodoc "; /*"select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per "
         + " from tpersonal p "
         + " order by p.plnrodoc";*/

        objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));
                objlstPersonal.add(objPersonal);

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

        return objlstPersonal;

    }

    public ListModelList<Personal> busquedaLovPersonal2(String consulta) throws SQLException {

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + " pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per,"
                + "  t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "  from tpersonal p ,tpldatoslab l ,Tpltablas1 t,tempresas e "
                + "  where p.pltipdoc = l.pltipdoc "
                + "  and   p.plnrodoc = l.plnrodoc "
                + "  and   t.tabla_id = l.plidcargo "
                + "  and l.emp_id = e.emp_id "
                + "  and   t.tabla_cod = '00006' "
                + "  and   t.tabla_id <> '000' "
                + "  and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + "  and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + "  and p.plestado <> 0"
                + "  and p.plestado = 1" // <> 0
                + "  and l.plestado_dl = 1" //= 0
                + "  and   p.plapepat||' '||p.plapemat||' '||p.plnomemp like '%" + consulta + "%'"
                + "  group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "  t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "  order by p.plapepat,p.plnrodoc "; /*"select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per "
         + " from tpersonal p "
         + " where p.plapepat||' '||p.plapemat||' '||p.plnomemp like '%" + consulta + "%'"
         + " order by p.plnrodoc";*/

        objlstPersonal = new ListModelList<Personal>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));
                objlstPersonal.add(objPersonal);

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

        return objlstPersonal;

    }

    public Personal getLovPersonal(String id) throws SQLException {
        /*
         String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
         + "       pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per, "
         + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
         + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t, tempresas e"
         + "                where p.pltipdoc = l.pltipdoc "
         + "                and   p.plnrodoc = l.plnrodoc "
         + "                and   t.tabla_id = l.plidcargo "
         + "                and l.emp_id = e.emp_id "
         + "                and   t.tabla_cod = '00006' "
         + "                and   t.tabla_id <> '000' "
         + "                and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
         + "                and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
         + "                and p.plestado <> 0"
         + "                and l.plestado_dl = 0"
         + "                and e.emp_est = 1"
         + "                and   p.pltipdoc||''||p.plnrodoc like '%" + id + "%'"
         + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
         + "                t.tabla_descri,l.plfecing , e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
         + "               order by p.plnrodoc ";
         */
        String sql_personal = " select p.pltipdoc||''||p.plnrodoc id_per , p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per , "
                + "        p.pldiremp dir_per , t.tabla_descri car_per , l.plfecing fecing_per , e.emp_des , "
                + "        e.emp_ruc , e.emp_repleg , e.emp_dnirep "
                + " from tpersonal p ,tpldatoslab l ,tpltablas1 t , tempresas e "
                + " where p.pltipdoc = l.pltipdoc "
                + " and p.plnrodoc = l.plnrodoc "
                + " and t.tabla_id = l.plidcargo "
                + " and l.emp_id = e.emp_id "
                + " and t.tabla_cod = '00006' "
                + " and t.tabla_id <> '000' "
                + " and p.plestado = 1 "
                + " and l.plestado_dl = 1 "
                + " and e.emp_est = 1 "
                + " and l.plfecces is null "
                + " and l.emp_id = " + objUsuCredential.getCodemp() + " "
                + " and p.pltipdoc||''||p.plnrodoc = " + id + " "
                + " order by p.plapepat, p.pltipdoc , p.plnrodoc ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_personal);
            objPersonal = null;
            while (rs.next()) {
                objPersonal = new Personal();

                objPersonal.setPlidper(rs.getString("id_per"));
                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setDireccion(rs.getString("dir_per"));
                objPersonal.setCargo(rs.getString("car_per"));
                objPersonal.setFechaing(rs.getDate("fecing_per"));
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setEmp_ruc(rs.getString("emp_ruc"));
                objPersonal.setEmp_repleg(rs.getString("emp_repleg"));
                objPersonal.setEmp_dnirep(rs.getString("emp_dnirep"));

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

        return objPersonal;

    }

    /**
     * Metodo para mostrar data en lov de prestamos automatico.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public ListModelList<ManPresPerDet> consultaBloquePrestamos(String sucursal, String area, String codigo, String day1,
            String day2, String periodo) throws SQLException {

        String query = "{call codijisa.pack_tpldsctos.p_lista_per(?,?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, sucursal);
            cst.setString(3, area);
            cst.setString(4, codigo);
            cst.setString(5, day1);
            cst.setString(6, day2);
            cst.setString(7, periodo);
            cst.registerOutParameter(8, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(8);
            objlstManPresDet = null;
            objlstManPresDet = new ListModelList<ManPresPerDet>();
            while (rs.next()) {
                objManPresPerDet = new ManPresPerDet();
                objManPresPerDet.setTPLPRESCAB_ID(rs.getString("codigo"));
                objManPresPerDet.setTPLID_PERSONAL(rs.getString("personal"));
                objManPresPerDet.setTPLPRESDET_MONTCUOTA(rs.getDouble("monto"));
                objManPresPerDet.setF_pago(rs.getString("fecha"));
                objManPresPerDet.setSituacion(rs.getString("situacion"));
                objManPresPerDet.setTPLPRESDET_NROCUOTA(rs.getInt("num_cuota"));
				objManPresPerDet.setTPLPRESDET_TOTCUOTAS(rs.getInt("tot_cuota"));
                objManPresPerDet.setSUC_ID(rs.getString("sucursal"));
                objManPresPerDet.setTipo(rs.getInt("tipo"));
                objManPresPerDet.setDoc(rs.getString("doc"));
                objManPresPerDet.setSucursal(rs.getInt("sucursal"));
                objlstManPresDet.add(objManPresPerDet);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }

        return objlstManPresDet;

    }

    /**
     * Metodo que cancela en bloque prestamos
     *
     * @param listaPrestamo
     * @return
     * @throws java.sql.SQLException
     */
    public ParametrosSalida modificarBloquePrestamos(Object[][] listaPrestamo) throws SQLException {

        String query = "{call pack_tpldsctos.p_insertar_bloque(?,?,?)}";
        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);

            arrayC = ArrayDescriptor.createDescriptor("LIST_PRESTAMO", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), listaPrestamo);
            cst.clearParameters();
            cst.setArray(1, arrC);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(2);
            i_flagErrorBD = cst.getInt(3);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }
}
