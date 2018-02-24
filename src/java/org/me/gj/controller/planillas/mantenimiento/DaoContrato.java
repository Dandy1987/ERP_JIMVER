package org.me.gj.controller.planillas.mantenimiento;

import org.me.gj.controller.cxc.mantenimiento.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.DatosLaborales;
import org.me.gj.model.planillas.mantenimiento.DerHabiente;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.mantenimiento.PlantillaContrato;
import org.me.gj.model.planillas.mantenimiento.Tablas1;
import org.me.gj.model.planillas.mantenimiento.TipContrato;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoContrato {

    //Instancias de Objetos
    ListModelList<Sucursales> objlstSucursal;
    ListModelList<Tablas1> objlstTablas1;
    ListModelList<Personal> objlstPersonal;
    ListModelList<DerHabiente> objlstDerHabientes;
    ListModelList<DatosLaborales> objlstDatosLab;
    ListModelList<PlantillaContrato> objlstPlaContrato;

    Sucursales objSucursal;
    Tablas1 objTablas1;
    Personal objPersonal;
    DerHabiente objDerHabiente;
    DatosLaborales objDatosLab;
    PlantillaContrato objPlaContrato;

    //LOV TIPO CONTRATO
    ListModelList<TipContrato> objlstTipContrato;
    TipContrato objTipContrato;

    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_INFORMES = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCliente.class);

    ListModelList<Sucursales> objlstSucursales;

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarContrato(PlantillaContrato objPlaContrato) throws SQLException, ParseException {

        String codigoRegistro = "";
        String SQL_PROCEDURE = "{call codijisa.pack_contratos.p_insertarContrato("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," // datos contrato
                + "?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCEDURE);
            cst.clearParameters();

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, objPlaContrato.getCt_tipo());
            cst.setString(3, objPlaContrato.getPar01_empnom());
            cst.setString(4, objPlaContrato.getPar02_empruc());
            cst.setString(5, objPlaContrato.getPar03_empdom());
            //agrego jr  11/01/2018
            cst.setString(6, objPlaContrato.getPar07_pernumdoc());
            cst.setString(7, objPlaContrato.getPar08_pernom());
            cst.setString(8, objPlaContrato.getPar09_perdir());
            cst.setString(9, objPlaContrato.getPar10_percargo());
            cst.setInt(10, objPlaContrato.getPar13_conmeses());
            cst.setDate(11, new java.sql.Date(objPlaContrato.getPar14_confecini().getTime()));
            cst.setDate(12, new java.sql.Date(objPlaContrato.getPar15_confecfin().getTime()));
            cst.setInt(13, objPlaContrato.getPar16_conremu());
            cst.setString(14, objPlaContrato.getPar17_conact());
            cst.setString(15, objUsuCredential.getCuenta());
            cst.registerOutParameter(16, java.sql.Types.NUMERIC);
            cst.registerOutParameter(17, java.sql.Types.VARCHAR);
            cst.setString(18, objPlaContrato.getPar04_empxxx());
            cst.registerOutParameter(19, java.sql.Types.VARCHAR);
            cst.setInt(20, objPlaContrato.getPar06_pertipdoc());
            cst.execute();
            i_flagErrorBD = cst.getInt(16);
            s_msg = cst.getString(17);
            codigoRegistro = cst.getString(19);

        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, codigoRegistro);
    }

    public ParametrosSalida modificarContrato(PlantillaContrato objPlaContrato) throws SQLException, ParseException {

        String SQL_MODIFICAR_CONTRATO = "{call codijisa.pack_contratos.p_modificarContrato(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_CONTRATO);
            cst.clearParameters();
            cst.setString(1, objPlaContrato.getId_contrato());
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.setString(3, objPlaContrato.getPar17_conact());
            cst.setString(4, objPlaContrato.getCt_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(5);
            s_msg = cst.getString(6);

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

    public ParametrosSalida eliminarContrato(PlantillaContrato objplaContrato) throws SQLException {

        String SQL_ELIMINAR_CONTRATO = "{call codijisa.pack_contratos.p_eliminarCargo(?,?,?,?)}";

        try {

            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CONTRATO);
            cst.clearParameters();
            cst.setString(1, objplaContrato.getId_contrato());
            cst.setInt(2, objUsuCredential.getCodemp());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
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

    //Eventos Secundarios - Listas
    public ListModelList<PlantillaContrato> listaContratos(int n_empid) throws SQLException {

        String sql_listacontratos;

        DateTime d_dia_ahora = new DateTime();
        int i_dias;

        //long difference;
        //float daysBetween;
        sql_listacontratos = "{call codijisa.pack_contratos.p_listarContrato(?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objPlaContrato = null;
            objlstPlaContrato = new ListModelList<PlantillaContrato>();

            while (rs.next()) {
                objPlaContrato = new PlantillaContrato();
                objPlaContrato.setId_contrato(rs.getString("id_contrato"));
                objPlaContrato.setCt_estado(rs.getInt("ct_estado"));
                objPlaContrato.setCt_tipo(rs.getString("ct_tipo"));
                objPlaContrato.setPar01_empnom(rs.getString("par01_empnom"));
                objPlaContrato.setPar02_empruc(rs.getString("par02_empruc"));
                objPlaContrato.setPar03_empdom(rs.getString("par03_empdom"));
                objPlaContrato.setPar04_empxxx(rs.getString("par04_empxxx"));
                objPlaContrato.setPar06_pertipdoc(rs.getInt("par06_pertipdoc"));
                objPlaContrato.setPar07_pernumdoc(rs.getString("par07_pernumdoc"));
                objPlaContrato.setPar08_pernom(rs.getString("par08_pernom"));
                objPlaContrato.setPar09_perdir(rs.getString("par09_perdir"));
                objPlaContrato.setPar10_percargo(rs.getString("par10_percargo"));
                objPlaContrato.setPar13_conmeses(rs.getInt("par13_conmeses"));
                objPlaContrato.setPar14_confecini(rs.getDate("par14_confecini"));
                objPlaContrato.setPar15_confecfin(rs.getDate("par15_confecfin"));
                objPlaContrato.setPar16_conremu(rs.getInt("par16_conremu"));
                objPlaContrato.setPar17_conact(rs.getString("par17_conact"));
                objPlaContrato.setCt_usuadd(rs.getString("ct_usuadd"));
                objPlaContrato.setCt_fecadd(rs.getTimestamp("ct_fecadd"));
                objPlaContrato.setCt_usumod(rs.getString("ct_usumod"));
                objPlaContrato.setCt_fecmod(rs.getTimestamp("ct_fecmod"));
                objPlaContrato.setDesct_tipo(rs.getString("tabla_descri"));
                //difference= objPlaContrato.getPar15_confecfin().getTime() - d_tiempo_ahora.getTime();
                //daysBetween = (difference / (1000 * 60 * 60 * 24));
                i_dias = Days.daysBetween(new LocalDate(d_dia_ahora), new LocalDate(objPlaContrato.getPar15_confecfin().getTime())).getDays();



                objPlaContrato.setDias_faltantes(i_dias);
                objlstPlaContrato.add(objPlaContrato);
                //objlstPlaContrato.add(objPlaContrato);

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
        return objlstPlaContrato;

    }

    public ListModelList<PlantillaContrato> listaContratosPorDias(int n_empid, int i_diasfiltro) throws SQLException {

        String sql_listacontratos;

        DateTime d_dia_ahora = new DateTime();
        int i_dias;

        long difference;

        float daysBetween;

        sql_listacontratos = "{call codijisa.pack_contratos.p_listarContrato(?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listacontratos);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objPlaContrato = null;
            objlstPlaContrato = new ListModelList<PlantillaContrato>();

            while (rs.next()) {
                objPlaContrato = new PlantillaContrato();
                objPlaContrato.setId_contrato(rs.getString("id_contrato"));
                objPlaContrato.setCt_estado(rs.getInt("ct_estado"));
                objPlaContrato.setCt_tipo(rs.getString("ct_tipo"));
                objPlaContrato.setPar01_empnom(rs.getString("par01_empnom"));
                objPlaContrato.setPar02_empruc(rs.getString("par02_empruc"));
                objPlaContrato.setPar03_empdom(rs.getString("par03_empdom"));
                objPlaContrato.setPar04_empxxx(rs.getString("par04_empxxx"));
                objPlaContrato.setPar06_pertipdoc(rs.getInt("par06_pertipdoc"));
                objPlaContrato.setPar07_pernumdoc(rs.getString("par07_pernumdoc"));
                objPlaContrato.setPar08_pernom(rs.getString("par08_pernom"));
                objPlaContrato.setPar09_perdir(rs.getString("par09_perdir"));
                objPlaContrato.setPar10_percargo(rs.getString("par10_percargo"));
                objPlaContrato.setPar13_conmeses(rs.getInt("par13_conmeses"));
                objPlaContrato.setPar14_confecini(rs.getDate("par14_confecini"));
                objPlaContrato.setPar15_confecfin(rs.getDate("par15_confecfin"));
                objPlaContrato.setPar16_conremu(rs.getInt("par16_conremu"));
                objPlaContrato.setPar17_conact(rs.getString("par17_conact"));
                objPlaContrato.setCt_usuadd(rs.getString("ct_usuadd"));
                objPlaContrato.setCt_fecadd(rs.getTimestamp("ct_fecadd"));
                objPlaContrato.setCt_usumod(rs.getString("ct_usumod"));
                objPlaContrato.setCt_fecmod(rs.getTimestamp("ct_fecmod"));
                objPlaContrato.setDesct_tipo(rs.getString("tabla_descri"));
                //difference= objPlaContrato.getPar15_confecfin().getTime() - d_tiempo_ahora.getTime();
                //daysBetween = (difference / (1000 * 60 * 60 * 24));
                i_dias = Days.daysBetween(new LocalDate(d_dia_ahora), new LocalDate(objPlaContrato.getPar15_confecfin().getTime())).getDays();
                objPlaContrato.setDias_faltantes(i_dias);
                if (i_dias <= i_diasfiltro) {
                    objlstPlaContrato.add(objPlaContrato);
                }

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
        return objlstPlaContrato;

    }

    public ListModelList<PlantillaContrato> listaEmpresa(int n_empid) throws SQLException {

        String sql_listaempresa;

        sql_listaempresa = "{call codijisa.pack_contratos.p_listarEmpresa(?,?)}";

        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_listaempresa);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.execute();

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objPlaContrato = null;
            objlstPlaContrato = new ListModelList<PlantillaContrato>();

            while (rs.next()) {
                objPlaContrato = new PlantillaContrato();
                objPlaContrato.setPar01_empnom(rs.getString("emp_des"));
                objPlaContrato.setPar02_empruc(rs.getString("emp_ruc"));
                objPlaContrato.setPar03_empdom(rs.getString("emp_dir"));
                objPlaContrato.setReplegal(rs.getString("emp_repleg"));
                objPlaContrato.setDnireplegal(rs.getString("emp_dnirep"));
                objlstPlaContrato.add(objPlaContrato);

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
        return objlstPlaContrato;

    }

    //LISTA CONTRATOS
    public ListModelList<PlantillaContrato> consultaContratos(int seleccion, String consulta, int estado, int diasfiltro, String carea) throws SQLException {

        String ccarea = carea.isEmpty() ? "" : " and d.plarea = " + carea + "";
        String SQL_CONSULTA = "";

        DateTime d_dia_ahora = new DateTime();
        int i_dias;

        long difference;

        float daysBetween;

        if (estado == 3) {
            if (seleccion == 0) {
                SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                        + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                        + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                        + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                        + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                        + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                        + "   and   t.ct_tipo = p.tabla_id "
                        + "   and   p.tabla_cod = '00014' "
                        + "   and t.par06_pertipdoc = d.pltipdoc"
                        + "   and t.par07_pernumdoc = d.plnrodoc"
						+ "   and d.plestado_dl = 1"
                        + "   and t.emp_id = d.emp_id"
                        + "   and   p.tabla_estado = 1 " + ccarea
                        + "   order by t.id_contrato ";

            } else {
                if (seleccion == 1) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.id_contrato like '" + consulta + "' "
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and   p.tabla_cod = '00014' "
							+ "   and d.plestado_dl = 1"
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";

                } else if (seleccion == 2) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.par08_pernom like '" + consulta + "' "
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and   p.tabla_cod = '00014' "
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
							+ "   and d.plestado_dl = 1"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";

                } else if (seleccion == 3) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.par10_percargo like '" + consulta + "' "
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and   p.tabla_cod = '00014' "
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
							+ "   and d.plestado_dl = 1"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";
                }

            }

        } else {
            if (seleccion == 0) {
                SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                        + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                        + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                        + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                        + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                        + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                        + "   and   t.ct_tipo = p.tabla_id "
                        + "   and   p.tabla_cod = '00014' "
                        + "   and t.par06_pertipdoc = d.pltipdoc"
                        + "   and t.par07_pernumdoc = d.plnrodoc"
						+ "   and d.plestado_dl = 1"
                        + "   and t.emp_id = d.emp_id"
                        + "   and   t.ct_estado = '" + estado + "' "
                        + "   and   p.tabla_estado = 1 " + ccarea
                        + "   order by t.id_contrato ";

            } else {
                if (seleccion == 1) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and   p.tabla_cod = '00014' "
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
							+ "   and d.plestado_dl = 1"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   t.ct_estado = '" + estado + "' "
                            + "   and   t.id_contrato like '" + consulta + "' "
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";

                } else if (seleccion == 2) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and   p.tabla_cod = '00014' "
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
							+ "   and d.plestado_dl = 1"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   t.ct_estado = '" + estado + "' "
                            + "   and   t.par08_pernom like '" + consulta + "' "
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";

                } else if (seleccion == 3) {
                    SQL_CONSULTA = "select t.id_contrato,t.ct_estado,t.ct_tipo,t.par01_empnom,t.par02_empruc,t.par03_empdom,t.par04_empxxx,t.par05_empxxx, "
                            + "          t.par06_pertipdoc,t.par07_pernumdoc,t.par08_pernom,t.par09_perdir,t.par10_percargo,t.par11_perxxx,t.par12_perxxx, "
                            + "          t.par13_conmeses,t.par14_confecini,t.par15_confecfin,t.par16_conremu,t.par17_conact,t.par18_conxxx,t.par19_conxxx, "
                            + "          t.par20_conxxx,t.ct_usuadd,t.ct_fecadd,t.ct_pcadd,t.ct_usumod,t.ct_fecmod,t.ct_pcmod,p.tabla_descri "
                            + "   from tplcontrato t,tpltablas1 p,tpldatoslab d "
                            + "   where t.emp_id = '" + objUsuCredential.getCodemp() + "'"
                            + "   and   t.ct_tipo = p.tabla_id "
                            + "   and t.par06_pertipdoc = d.pltipdoc"
                            + "   and t.par07_pernumdoc = d.plnrodoc"
							+ "   and d.plestado_dl = 1"
                            + "   and t.emp_id = d.emp_id"
                            + "   and   p.tabla_cod = '00014' "
                            + "   and   t.ct_estado = '" + estado + "' "
                            + "   and   t.par10_percargo like '" + consulta + "' "
                            + "   and   p.tabla_estado = 1 " + ccarea
                            + "   order by t.id_contrato ";

                }

            }

        }
        P_WHERE = SQL_CONSULTA;
        objlstPlaContrato = new ListModelList<PlantillaContrato>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_CONSULTA);
            while (rs.next()) {
                objPlaContrato = new PlantillaContrato();
                objPlaContrato.setId_contrato(rs.getString("id_contrato"));
                objPlaContrato.setCt_estado(rs.getInt("ct_estado"));
                objPlaContrato.setCt_tipo(rs.getString("ct_tipo"));
                objPlaContrato.setPar01_empnom(rs.getString("par01_empnom"));
                objPlaContrato.setPar02_empruc(rs.getString("par02_empruc"));
                objPlaContrato.setPar03_empdom(rs.getString("par03_empdom"));
                objPlaContrato.setPar04_empxxx(rs.getString("par04_empxxx"));
                objPlaContrato.setPar06_pertipdoc(rs.getInt("par06_pertipdoc"));
                objPlaContrato.setPar07_pernumdoc(rs.getString("par07_pernumdoc"));
                objPlaContrato.setPar08_pernom(rs.getString("par08_pernom"));
                objPlaContrato.setPar09_perdir(rs.getString("par09_perdir"));
                objPlaContrato.setPar10_percargo(rs.getString("par10_percargo"));
                objPlaContrato.setPar13_conmeses(rs.getInt("par13_conmeses"));
                objPlaContrato.setPar14_confecini(rs.getDate("par14_confecini"));
                objPlaContrato.setPar15_confecfin(rs.getDate("par15_confecfin"));
                objPlaContrato.setPar16_conremu(rs.getInt("par16_conremu"));
                objPlaContrato.setPar17_conact(rs.getString("par17_conact"));
                objPlaContrato.setCt_usuadd(rs.getString("ct_usuadd"));
                objPlaContrato.setCt_fecadd(rs.getTimestamp("ct_fecadd"));
                objPlaContrato.setCt_usumod(rs.getString("ct_usumod"));
                objPlaContrato.setCt_fecmod(rs.getTimestamp("ct_fecmod"));
                objPlaContrato.setDesct_tipo(rs.getString("tabla_descri"));
                i_dias = Days.daysBetween(new LocalDate(d_dia_ahora), new LocalDate(objPlaContrato.getPar15_confecfin().getTime())).getDays();
                objPlaContrato.setDias_faltantes(i_dias);
                if (i_dias <= diasfiltro) {
                    objlstPlaContrato.add(objPlaContrato);
                }

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
        return objlstPlaContrato;

    }

    //LOV PERSONAL
    public ListModelList<Personal> busquedaLovPersonal() throws SQLException {

        String sql_personal = "select p.pltipdoc||''||p.plnrodoc id_per,p.plapepat||' '||p.plapemat||' '||p.plnomemp des_per, "
                + " pack_tplpresper.f_decripcionVia(p.pldir_via) || ' ' || p.pldir_nomvia || ' ' || p.pldir_num || ' ' || p.pldir_int || ' ' || pack_tplpresper.f_decripcionZona(p.pldir_zona) || ' ' || p.pldir_nomzona dir_per, "
                + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t, tempresas e "
                + "                where p.pltipdoc = l.pltipdoc "
                + "                and   p.plnrodoc = l.plnrodoc "
                + "                and   t.tabla_id = l.plidcargo "
                + "                and l.emp_id = e.emp_id "
                + "                and   t.tabla_cod = '00006' "
                + "                and   t.tabla_id <> '000' "
                + "                and   l.emp_id = '" + objUsuCredential.getCodemp() + "'"
                + "                and   l.suc_id = '" + objUsuCredential.getCodsuc() + "'"
                + "                and p.plestado <> 0"
                //+ "                and l.plestado_dl = 0"
                + "                and l.plestado_dl <> 0"
                + "                and e.emp_est = 1"
                + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "                t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "               order by p.plnrodoc ";

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
                + "       t.tabla_descri car_per,l.plfecing fecing_per, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "                from tpersonal p ,tpldatoslab l ,Tpltablas1 t,tempresas e "
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
                + "                and   p.plapepat||' '||p.plapemat||' '||p.plnomemp like '%" + consulta + "%'"
                + "                group by p.pltipdoc,p.plnrodoc,p.plapepat,p.plapemat,p.plnomemp,p.pldir_via,p.pldir_nomvia,p.pldir_num,p.pldir_int,p.pldir_zona,p.pldir_nomzona, "
                + "                t.tabla_descri,l.plfecing, e.emp_des,e.emp_ruc,e.emp_repleg,e.emp_dnirep "
                + "               order by p.plnrodoc ";

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
                + "        e.emp_ruc , e.emp_repleg , e.emp_dnirep,l.pltipdoc,l.plnrodoc "
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
                + " and p.pltipdoc||''||p.plnrodoc = '" + id + "'"
                + " order by p.pltipdoc , p.plnrodoc ";

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
                //agrego jr 11/01/2018
                objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                objPersonal.setPlnrodoc(rs.getString("plnrodoc"));

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

    //LOV TIPO CONTRATO 
    public ListModelList<TipContrato> busquedaLovTipCont() throws SQLException {

        String sql_tipcont = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00014' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " order by r.tabla_id";

        objlstTipContrato = new ListModelList<TipContrato>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tipcont);
            while (rs.next()) {
                objTipContrato = new TipContrato();
                objTipContrato.setTipcont_id(rs.getString("TABLA_ID"));
                objTipContrato.setTipcont_des(rs.getString("TABLA_DESCRI"));
                objlstTipContrato.add(objTipContrato);

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

        return objlstTipContrato;

    }

    public ListModelList<TipContrato> busquedaLovTipCont2(String consulta) throws SQLException {

        String sql_tipcont = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00014' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstTipContrato = new ListModelList<TipContrato>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tipcont);
            while (rs.next()) {
                objTipContrato = new TipContrato();
                objTipContrato.setTipcont_id(rs.getString("TABLA_ID"));
                objTipContrato.setTipcont_des(rs.getString("TABLA_DESCRI"));
                objlstTipContrato.add(objTipContrato);

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

        return objlstTipContrato;

    }

    public TipContrato getLovTipCont(String tipcont) throws SQLException {
        String sql_tipcont = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00014' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + tipcont + "%' "
                + " order by r.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tipcont);
            objTipContrato = null;
            while (rs.next()) {
                objTipContrato = new TipContrato();
                objTipContrato.setTipcont_id(rs.getString("TABLA_ID"));
                objTipContrato.setTipcont_des(rs.getString("TABLA_DESCRI"));

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

        return objTipContrato;

    }

    /**
     * Metodo para listar contratos en informes
     *
     * @param carea
     * @param ccodigo
     * @param cperiodo
     * @return
     * @throws java.sql.SQLException
     */
    public ListModelList<PlantillaContrato> consultaContratoInformes(String carea, String ccodigo, String cperiodo, int tipoPeriodo) throws SQLException {

        String ccperiodod;
        //String query = "{call codijisa.PACK_CONTRATOS.p_contratoInformes(?,?,?,?,?,?)}";
        String ccarea = carea.isEmpty() ? "" : " and d.plarea = " + carea + "";
        String s_codigo = ccodigo.isEmpty() ? "" : " and t.par06_pertipdoc||t.par07_pernumdoc in ('" + ccodigo + "')";
        //String ccperiodod = cperiodo.isEmpty() ? "" : " and to_char(t.par14_confecini,'yyyyMM')= '" + cperiodo + "'";

        if (tipoPeriodo == 0) {
            ccperiodod = cperiodo.isEmpty() ? "" : " and to_char(t.par14_confecini,'yyyyMM')= '" + cperiodo + "'";
        } else {
            ccperiodod = cperiodo.isEmpty() ? "" : " and to_char(t.par15_confecfin,'yyyyMM')= '" + cperiodo + "'";
        }

        String query = " select t.id_contrato id_c,\n"
                + "       t.ct_tipo tipo,\n"
                + "       t.par06_pertipdoc tipo_doc,t.par06_pertipdoc||t.par07_pernumdoc codigo,\n"
                + "       t.par07_pernumdoc dni,\n"
                + "       t.par08_pernom personal,\n"
                + "       t.par13_conmeses tiempo,\n"
                + "       t.par14_confecini inicio,\n"
                + "       t.par15_confecfin fin,\n"
                + "       p.tabla_descri des_contrato,e.emp_des empresa"
                + "  from tplcontrato t, tpltablas1 p,tpldatoslab d, tempresas e "
                + " where t.emp_id = " + objUsuCredential.getCodemp()
                + "   and t.ct_tipo = p.tabla_id\n"
                + "   and t.par06_pertipdoc=d.pltipdoc\n"
                + "   and t.par07_pernumdoc=d.plnrodoc"
				+ "   and d.plestado_dl = 1"
                + "   and t.emp_id = e.emp_id "
                + "   and t.emp_id = d.emp_id\n"
                + "   and p.tabla_cod = '00014'\n"
                + "   and t.ct_estado = '1'\n"
                + "   and p.tabla_estado = 1" + ccarea + s_codigo + ccperiodod + " order by t.id_contrato";

        P_INFORMES = query;
        objlstPlaContrato = null;
        objlstPlaContrato = new ListModelList<PlantillaContrato>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                objPlaContrato = new PlantillaContrato();
                objPlaContrato.setId_contrato(rs.getString("id_c"));
                objPlaContrato.setPar06_pertipdoc(rs.getInt("tipo_doc"));
                objPlaContrato.setPar07_pernumdoc(rs.getString("dni"));
                objPlaContrato.setPar14_confecini(rs.getDate("inicio"));
                objPlaContrato.setPar15_confecfin(rs.getDate("fin"));
                objPlaContrato.setDesct_tipo(rs.getString("des_contrato"));
                objPlaContrato.setPar13_conmeses(rs.getInt("tiempo"));
                objPlaContrato.setPar08_pernom(rs.getString("personal"));
                objlstPlaContrato.add(objPlaContrato);

            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                rs.close();
                con.close();
            }
        }
        return objlstPlaContrato;

    }

}
