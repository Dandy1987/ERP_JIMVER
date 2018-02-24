/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.SimpleDateFormat;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesPrestamos;
import org.me.gj.model.planillas.mantenimiento.Personal;

import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author cpure
 */
public class DaoInformesPrestamos {

    ListModelList<InformesPrestamos> objlstPrestamo;
    InformesPrestamos objPrestamo;
    Personal objPersonal;
    ListModelList<Personal> objlstPersonal;
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    SimpleDateFormat sdfcont = new SimpleDateFormat("dd-MM-yyyy");
    int i_flagErrorBD = 0;
    String s_msg = "";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    public static String P_WHERE = "";

    public ListModelList<InformesPrestamos> consultaPrestamosSinFecha(int i_empid, int i_sucursal, String s_estadotrabajador, String s_estadoprestamo) throws SQLException {

        String sql_listacontratos;

        sql_listacontratos = "{call codijisa.pack_tplpresper.p_listPresFiltroSinFecha(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, i_empid);
            cst.setInt(2, i_sucursal);
            cst.setString(3, s_estadotrabajador);
            cst.setString(4, s_estadoprestamo);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(5);
            st = con.createStatement();

            objPrestamo = null;
            objlstPrestamo = new ListModelList<InformesPrestamos>();

            while (rs.next()) {
                objPrestamo = new InformesPrestamos();
                objPrestamo.setCodigo(rs.getString("tplprescab_id"));
                objPrestamo.setMonto(rs.getDouble("tplprescab_monto"));
                objPrestamo.setNombres(rs.getString("tplprescab_desper"));
                objPrestamo.setFecemi(sdfcont.format(rs.getDate("tplprescab_fecemi")));
                objPrestamo.setNrorestamos(rs.getInt("NumeroPrestamo"));
                objPrestamo.setPorpagar(rs.getDouble("FALTANTE"));
                objPrestamo.setEmpresa(rs.getString("emp_des"));
                objPrestamo.setSucursal(rs.getString("suc_des"));
				objPrestamo.setIdper(rs.getString("tplprescab_idper"));
                if (rs.getInt("ESTADO") == 0) {
                    objPrestamo.setEstado("PAGADO");
                } else {
                    objPrestamo.setEstado("PENDIENTE");
                }

                objlstPrestamo.add(objPrestamo);

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
        return objlstPrestamo;

    }

    public ListModelList<InformesPrestamos> consultaPrestamosConFecha(int i_empid, int i_sucursal, String s_estadotrabajador, String s_estadoprestamo, Date d_fechaini, Date d_fechafin) throws SQLException {

        String sql_listacontratos;

        sql_listacontratos = "{call codijisa.pack_tplpresper.p_listPresFiltroConFecha(?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, i_empid);
            cst.setInt(2, i_sucursal);
            cst.setString(3, s_estadotrabajador);
            cst.setString(4, s_estadoprestamo);
            cst.setDate(5, d_fechaini);
            cst.setDate(6, d_fechafin);
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(7);
            st = con.createStatement();

            objPrestamo = null;
            objlstPrestamo = new ListModelList<InformesPrestamos>();

            while (rs.next()) {
                objPrestamo = new InformesPrestamos();
                objPrestamo.setCodigo(rs.getString("tplprescab_id"));
                objPrestamo.setMonto(rs.getDouble("tplprescab_monto"));
                objPrestamo.setNombres(rs.getString("tplprescab_desper"));
                objPrestamo.setFecemi(sdfcont.format(rs.getDate("tplprescab_fecemi")));
                objPrestamo.setNrorestamos(rs.getInt("NumeroPrestamo"));
                objPrestamo.setPorpagar(rs.getDouble("FALTANTE"));
                objPrestamo.setEmpresa(rs.getString("emp_des"));
				objPrestamo.setIdper(rs.getString("tplprescab_idper"));
                objPrestamo.setSucursal(rs.getString("suc_des"));
                if (rs.getInt("ESTADO") == 0) {
                    objPrestamo.setEstado("PAGADO");
                } else {
                    objPrestamo.setEstado("PENDIENTE");
                }

                objlstPrestamo.add(objPrestamo);

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
        return objlstPrestamo;

    }

    public Personal consultaPersonalPorId(String s_nrodoc) throws SQLException {

        String sql_listacontratos;

        sql_listacontratos = "{call codijisa.pack_tpersonal.p_listaPersonalPorId(?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setString(1, s_nrodoc);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objPersonal = null;
            objlstPersonal = new ListModelList<Personal>();

            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setPldesper(rs.getString("plapepat") + " " + rs.getString("plapemat") + " " + rs.getString("plnomemp"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlidper(rs.getInt("pltipdoc") + rs.getString("plnrodoc"));

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

    public ListModelList<InformesPrestamos> consultaPrestamosDetalleSinFecha(int i_empid, int i_sucursal, String s_idtrabajador, String s_estadocuota) throws SQLException {

        String sql_listacontratos;

        sql_listacontratos = "{call codijisa.pack_tplpresper.p_listPresConDetalle(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, i_empid);
            cst.setInt(2, i_sucursal);
            cst.setString(3, s_idtrabajador);
            cst.setString(4, s_estadocuota);
            cst.registerOutParameter(5, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(5);
            st = con.createStatement();

            objPrestamo = null;
            objlstPrestamo = new ListModelList<InformesPrestamos>();

            while (rs.next()) {
                objPrestamo = new InformesPrestamos();
                objPrestamo.setCodigo(rs.getString("tplprescab_id"));
                objPrestamo.setMontocuota(rs.getDouble("tplpresdet_montcuota"));
                objPrestamo.setNrocuota(rs.getInt("tplpresdet_nrocuota"));
                objPrestamo.setFechapago(sdfcont.format(rs.getDate("tplpresdet_fecpago")));
                objPrestamo.setNombres(rs.getString("tplprescab_desper"));
                objPrestamo.setEmpresa(rs.getString("emp_des"));
                objPrestamo.setSucursal(rs.getString("suc_des"));
                objPrestamo.setNrodocper(rs.getString("tplprescab_idper"));
                objPrestamo.setTotalpagado(rs.getDouble("TOTALPAGADO"));
                objPrestamo.setTotaldeuda(rs.getDouble("TOTALDEUDA"));
                objPrestamo.setTotalprestamos(rs.getDouble("tplprescab_monto"));
                if (rs.getInt("tplpresdet_situcuota") == 0) {
                    objPrestamo.setEstadocuota("PAGADO");
                } else {
                    objPrestamo.setEstadocuota("PENDIENTE");
                }
                if (objPrestamo.getEstadocuota().equals("PAGADO")) {
                    objPrestamo.setFaltante(0);

                } else {
                    objPrestamo.setFaltante(rs.getDouble("tplpresdet_montcuota"));
                }

                objlstPrestamo.add(objPrestamo);

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
        return objlstPrestamo;

    }

    public ListModelList<InformesPrestamos> consultaPrestamosDetalleConFecha(int i_empid, int i_sucursal, String s_idtrabajador, String s_estadocuota, Date d_fechaini, Date d_fechafin) throws SQLException {

        String sql_listacontratos;

        sql_listacontratos = "{call codijisa.pack_tplpresper.p_listPresConDetalleFecha(?,?,?,?,?,?,?)}";
        double totalprestamo = 0;
        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, i_empid);
            cst.setInt(2, i_sucursal);
            cst.setString(3, s_idtrabajador);
            cst.setString(4, s_estadocuota);
            cst.setDate(5, d_fechaini);
            cst.setDate(6, d_fechafin);
            cst.registerOutParameter(7, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(7);
            st = con.createStatement();

            objPrestamo = null;
            objlstPrestamo = new ListModelList<InformesPrestamos>();

            while (rs.next()) {
                objPrestamo = new InformesPrestamos();
                objPrestamo.setCodigo(rs.getString("tplprescab_id"));
                objPrestamo.setMontocuota(rs.getDouble("tplpresdet_montcuota"));
                objPrestamo.setNrocuota(rs.getInt("tplpresdet_nrocuota"));
                objPrestamo.setFechapago(sdfcont.format(rs.getDate("tplpresdet_fecpago")));
                objPrestamo.setNombres(rs.getString("tplprescab_desper"));
                objPrestamo.setEmpresa(rs.getString("emp_des"));
                objPrestamo.setSucursal(("suc_des"));
                objPrestamo.setNrodocper(rs.getString("tplprescab_idper"));
                objPrestamo.setTotalpagado(rs.getDouble("TOTALPAGADO"));
                objPrestamo.setTotaldeuda(rs.getDouble("TOTALDEUDA"));
                objPrestamo.setTotalprestamos(rs.getDouble("tplprescab_monto"));
                if (rs.getInt("tplpresdet_situcuota") == 0) {
                    objPrestamo.setEstadocuota("PAGADO");
                } else {
                    objPrestamo.setEstadocuota("PENDIENTE");
                }
                if (objPrestamo.getEstadocuota().equals("PAGADO")) {
                    objPrestamo.setFaltante(0);

                } else {
                    objPrestamo.setFaltante(rs.getDouble("tplpresdet_montcuota"));
                }

                objlstPrestamo.add(objPrestamo);

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
        return objlstPrestamo;

    }

}
