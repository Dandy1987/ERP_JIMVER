/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Descuentos;
import org.me.gj.model.planillas.procesos.Vacaciones;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author cpure
 */
public class DaoVacaciones {

    ListModelList<Vacaciones> objlstVacaciones;
    ListModelList<Personal> objlstPersonal;
    Vacaciones objVacaciones;
    Personal objPersonal;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";

    ArrayDescriptor arrayC, arrayCM;
    ARRAY arrC, arrCM;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * ***************************************
     * para los movimiento de descuentos
     *
     *
     * @return
     * @throws java.sql.SQLException ***************************************
     */
    public ListModelList<Vacaciones> listarVacaciones(int i_emp, int i_suc) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_listVac(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.registerOutParameter(3, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(3);
            objlstVacaciones = null;
            objlstVacaciones = new ListModelList<Vacaciones>();
            Date d_tiempoahora = new Date();
            while (rs.next()) {
                objVacaciones = new Vacaciones();
                objVacaciones.setArea(rs.getString("tabla_descri"));
                objVacaciones.setNombres(rs.getString("NOMBRES"));
                // objVacaciones.setCodigo(rs.getString("plvg_correlativo"));
                objVacaciones.setFechaingreso(rs.getDate("plfecing"));
                objVacaciones.setFechacese(rs.getDate("plfecces"));
                objVacaciones.setSucursal(rs.getString("suc_des"));
                objVacaciones.setS_fechaingreso(sdf.format(objVacaciones.getFechaingreso()));
                if (objVacaciones.getFechacese() != null) {
                    objVacaciones.setS_fechacese(sdf.format(objVacaciones.getFechacese()));
                } else {
                    objVacaciones.setS_fechacese("----");
                }
                /*  objVacaciones.setFechainicio(rs.getDate("Plvg_Fecini"));
                 objVacaciones.setFechafin(rs.getDate("Plvg_Fecfin"));

                 if (objVacaciones.getFechainicio() != null) {
                 objVacaciones.setS_fechainicio(sdf.format(objVacaciones.getFechainicio()));
                 objVacaciones.setS_fechafin(sdf.format(objVacaciones.getFechafin()));
                 }else{
                 objVacaciones.setS_fechainicio("----");
                 objVacaciones.setS_fechafin("----");
                 }*/

                // objVacaciones.setGozado(Days.daysBetween(new LocalDate(objVacaciones.getFechainicio()), new LocalDate(objVacaciones.getFechafin().getTime())).getDays()+1);
                objVacaciones.setTotal((Years.yearsBetween(new LocalDate(objVacaciones.getFechaingreso()), new LocalDate(d_tiempoahora)).getYears()) * 30);
                objVacaciones.setGozado(rs.getInt("GOZADO"));
                objVacaciones.setPendiente(objVacaciones.getTotal() - objVacaciones.getGozado());
                //objVacaciones.setUsu_add(rs.getString("plvg_usuadd"));
                // objVacaciones.setPc_add(rs.getString("plvg_pcadd"));
                // objVacaciones.setUsu_mod(rs.getString("plvg_usumod"));
                // objVacaciones.setPc_mod(rs.getString("plvg_pcmod"));
                // objVacaciones.setFecha_add(rs.getDate("plvg_fecadd"));
                // objVacaciones.setFecha_mod(rs.getDate("plvg_fecmod"));
                // objVacaciones.setGlosa(rs.getString("plvg_glosa"));
                // objVacaciones.setPeriodo(rs.getString("plvg_periodo"));
                // objVacaciones.setPeriodogozado(rs.getString("plppag_id"));
                objVacaciones.setIdarea(rs.getString("tabla_id"));
                objVacaciones.setIdsucursal(rs.getInt("suc_id"));
                objVacaciones.setNrodocpersona(rs.getString("plnrodoc"));
                objVacaciones.setTipodoc(rs.getInt("pltipdoc"));
                objlstVacaciones.add(objVacaciones);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return objlstVacaciones;

    }

    public ListModelList<Vacaciones> listarVacacionesFiltro(int i_emp, int i_suc, String s_area, String s_estado) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_listVacFiltro(?,?,?,?,?)}";
        if (s_area.isEmpty()) {
            s_area = "TODOS";
        }
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, i_suc);
            cst.setString(3, s_area);
            cst.setString(4, s_estado);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(5);
            objlstVacaciones = null;
            objlstVacaciones = new ListModelList<Vacaciones>();
            Date d_tiempoahora = new Date();
            while (rs.next()) {
                objVacaciones = new Vacaciones();
                objVacaciones.setArea(rs.getString("tabla_descri"));
                objVacaciones.setNombres(rs.getString("NOMBRES"));
                // objVacaciones.setCodigo(rs.getString("plvg_correlativo"));
                objVacaciones.setFechaingreso(rs.getDate("plfecing"));
                objVacaciones.setFechacese(rs.getDate("plfecces"));
                objVacaciones.setSucursal(rs.getString("suc_des"));
                objVacaciones.setS_fechaingreso(sdf.format(objVacaciones.getFechaingreso()));
                if (objVacaciones.getFechacese() != null) {
                    objVacaciones.setS_fechacese(sdf.format(objVacaciones.getFechacese()));
                } else {
                    objVacaciones.setS_fechacese("----");
                }
                objVacaciones.setTotal((Years.yearsBetween(new LocalDate(objVacaciones.getFechaingreso()), new LocalDate(d_tiempoahora)).getYears()) * 30);
                objVacaciones.setGozado(rs.getInt("GOZADO"));
                objVacaciones.setPendiente(objVacaciones.getTotal() - objVacaciones.getGozado());

                objVacaciones.setIdarea(rs.getString("tabla_id"));
                objVacaciones.setIdsucursal(rs.getInt("suc_id"));
                objVacaciones.setNrodocpersona(rs.getString("plnrodoc"));
                objVacaciones.setTipodoc(rs.getInt("pltipdoc"));
                objlstVacaciones.add(objVacaciones);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return objlstVacaciones;

    }

    public ListModelList<Vacaciones> listarVacacionesporPersonal(int i_emp, int i_suc, int i_tipodoc, String nrodoc) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_listvacpersonal(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setInt(3, i_tipodoc);
            cst.setString(4, nrodoc);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(5);
            objlstVacaciones = null;
            objlstVacaciones = new ListModelList<Vacaciones>();

            while (rs.next()) {
                objVacaciones = new Vacaciones();

                objVacaciones.setFechainicio(rs.getDate("Plvg_Fecini"));
                objVacaciones.setFechafin(rs.getDate("Plvg_Fecfin"));

                if (objVacaciones.getFechainicio() != null) {
                    objVacaciones.setS_fechainicio(sdf.format(objVacaciones.getFechainicio()));
                    objVacaciones.setS_fechafin(sdf.format(objVacaciones.getFechafin()));
                } else {
                    objVacaciones.setS_fechainicio("----");
                    objVacaciones.setS_fechafin("----");
                }
                objVacaciones.setCodigo(rs.getString("plvg_correlativo"));
                //objVacaciones.setUsu_add(rs.getString("plvg_usuadd"));
                // objVacaciones.setPc_add(rs.getString("plvg_pcadd"));
                // objVacaciones.setUsu_mod(rs.getString("plvg_usumod"));
                // objVacaciones.setPc_mod(rs.getString("plvg_pcmod"));
                // objVacaciones.setFecha_add(rs.getDate("plvg_fecadd"));
                // objVacaciones.setFecha_mod(rs.getDate("plvg_fecmod"));
                objVacaciones.setDias(rs.getInt("dias"));
                objVacaciones.setGlosa(rs.getString("plvg_glosa"));
                objVacaciones.setPeriodo(rs.getString("plvg_periodo"));
                objVacaciones.setPeriodogozado(rs.getString("plppag_id"));
                objVacaciones.setUsu_add(rs.getString("plvg_usuadd"));
                objVacaciones.setFecha_add(rs.getTimestamp("plvg_fecadd"));
                objVacaciones.setUsu_mod(rs.getString("plvg_usumod"));
                objVacaciones.setFecha_mod(rs.getTimestamp("plvg_fecmod"));
                objVacaciones.setCorrelativo(rs.getInt("plvg_correlativo"));
                objlstVacaciones.add(objVacaciones);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return objlstVacaciones;

    }

    public ListModelList<Personal> listarPersonal(String area) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_listPersonal(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, area);
            cst.registerOutParameter(4, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(4);
            objlstPersonal = null;
            objlstPersonal = new ListModelList<Personal>();

            while (rs.next()) {
                objPersonal = new Personal();

                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPlidper(rs.getString("plidper"));
                objPersonal.setPlarea_des(rs.getString("tabla_descri"));
                objlstPersonal.add(objPersonal);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPersonal;

    }
	
    public ListModelList<Personal> listarPersonalFiltro(String area, String s_consulta) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_listPersonalFiltro(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, area);
            cst.setString(4, s_consulta);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(5);
            objlstPersonal = null;
            objlstPersonal = new ListModelList<Personal>();

            while (rs.next()) {
                objPersonal = new Personal();

                objPersonal.setPldesper(rs.getString("des_per"));
                objPersonal.setPlidper(rs.getString("plidper"));
                objPersonal.setPlarea_des(rs.getString("tabla_descri"));
                objlstPersonal.add(objPersonal);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return objlstPersonal;

    }

    public String insertarVacaciones(Vacaciones objVac, ListModelList<Vacaciones> objlstVacaInsert) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_insertarVacaciones(?,?,?,?,?,?,?,?,?,?,?)}";        
		
        try {
            for (int i = 0; i < objlstVacaInsert.size(); i++) {
                    con = new ConectaBD().conectar();
                    cst = con.prepareCall(query);
                    cst.clearParameters();
                    cst.setInt(1, objUsuCredential.getCodemp());
                    cst.setInt(2, objVac.getIdsucursal());
                    cst.setString(3, objlstVacaInsert.get(i).getPeriodogozado());
                    cst.setString(4, objVac.getNrodocpersona());
                    cst.setInt(5, objVac.getTipodoc());
                    cst.setDate(6, convertJavaDateToSqlDate(objlstVacaInsert.get(i).getFechainicio()));
                    cst.setDate(7, convertJavaDateToSqlDate(objlstVacaInsert.get(i).getFechafin()));
                    cst.setString(8, objlstVacaInsert.get(i).getGlosa());
                    cst.setString(9, objlstVacaInsert.get(i).getPeriodo());
                    cst.setString(10, objUsuCredential.getCuenta());
                    cst.registerOutParameter(11, OracleTypes.VARCHAR);
                    cst.execute();
                    s_msg = cst.getString(11);

                }            
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return s_msg;

    }

public String modificarVacaciones(Vacaciones objVacaciones, ListModelList<Vacaciones> objlstVacaInsert,ListModelList<Integer> objlstCorrelativo,ListModelList<Integer> objlstSeleccionado) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_modificarVacaciones(?,?,?,?,?,?,?,?)}";
		
        try {
            for (int i = 0; i < objlstCorrelativo.size(); i++) {
                con = new ConectaBD().conectar();
                cst = con.prepareCall(query);
                cst.clearParameters();

                cst.setString(1, objlstVacaInsert.get(objlstSeleccionado.get(i)).getPeriodogozado());
                cst.setDate(2, convertJavaDateToSqlDate(objlstVacaInsert.get(objlstSeleccionado.get(i)).getFechainicio()));
                cst.setDate(3, convertJavaDateToSqlDate(objlstVacaInsert.get(objlstSeleccionado.get(i)).getFechafin()));
                cst.setString(4, objlstVacaInsert.get(objlstSeleccionado.get(i)).getGlosa());
                cst.setString(5, objlstVacaInsert.get(objlstSeleccionado.get(i)).getPeriodo());
                cst.setString(6, objUsuCredential.getCuenta());
                cst.setInt(7, objlstCorrelativo.get(i));
                cst.registerOutParameter(8, OracleTypes.VARCHAR);
                cst.execute();
                s_msg = cst.getString(8);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return s_msg;

    }

    public String eliminarVacacionesDetalle(ListModelList<Integer> objlstVacaInsert) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_eliminaVacacionesDetalle(?,?,?)}";

        try {
            for (int i = 0; i < objlstVacaInsert.size(); i++) {
                con = new ConectaBD().conectar();
                cst = con.prepareCall(query);
                cst.clearParameters();

                cst.setInt(1, objlstVacaInsert.get(i));
                cst.setString(2, objUsuCredential.getCuenta());
                cst.registerOutParameter(3, OracleTypes.VARCHAR);
                cst.execute();
                s_msg = cst.getString(3);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return s_msg;

    }

    public String eliminarVacaciones(String s_nrodoc, int i_tipodoc) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_eliminaVacaciones(?,?,?)}";

        try {

            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();

            cst.setString(1, s_nrodoc);
            cst.setInt(2, i_tipodoc);
            cst.setString(3, objUsuCredential.getCuenta());
            cst.registerOutParameter(4, OracleTypes.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }
        return s_msg;

    }
	
    public boolean validaVacaciones(String s_nrodoc, int i_tipodoc, Date d_fechainicio, Date d_fechafin) throws SQLException {
        String query = "{call codijisa.pack_tplvacgoz.p_validaVacaciones(?,?,?,?,?)}";
        boolean b_valida = false;
        try {

            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();

            cst.setString(1, s_nrodoc);
            cst.setInt(2, i_tipodoc);
            cst.setDate(3, convertJavaDateToSqlDate(d_fechainicio));
            cst.setDate(4, convertJavaDateToSqlDate(d_fechafin));
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();
            rs = ((OracleCallableStatement) cst).getCursor(5);
            objlstVacaciones = null;
            objlstVacaciones = new ListModelList<Vacaciones>();
            while (rs.next()) {
                objVacaciones = new Vacaciones();

                objVacaciones.setFechainicio(rs.getDate("Plvg_Fecini"));
                objVacaciones.setFechafin(rs.getDate("Plvg_Fecfin"));

                objVacaciones.setCodigo(rs.getString("plvg_correlativo"));

                objVacaciones.setGlosa(rs.getString("plvg_glosa"));
                objVacaciones.setPeriodo(rs.getString("plvg_periodo"));
                objVacaciones.setPeriodogozado(rs.getString("plppag_id"));
                objlstVacaciones.add(objVacaciones);
            }
            if (objlstVacaciones.size() == 0) {
                b_valida = true;
            } else {
                b_valida = false;
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                //st.close();
                rs.close();
                con.close();
            }
        }

        return b_valida;
    }
	
    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
