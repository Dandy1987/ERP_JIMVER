package org.me.gj.controller.planillas.mantenimiento;

import org.me.gj.controller.cxc.mantenimiento.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.planillas.mantenimiento.DatosLaborales;
import org.me.gj.model.planillas.mantenimiento.DerHabiente;
import org.me.gj.model.planillas.mantenimiento.EntiSalud;
import org.me.gj.model.planillas.mantenimiento.HorariosPla;
import org.me.gj.model.planillas.mantenimiento.ManAfpsCre;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCargos;
import org.me.gj.model.planillas.mantenimiento.Nacion;
import org.me.gj.model.planillas.mantenimiento.NivEducativo;
import org.me.gj.model.planillas.mantenimiento.Ocupacion;
import org.me.gj.model.planillas.mantenimiento.PersAportacion;
import org.me.gj.model.planillas.mantenimiento.PersPagoDep;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.mantenimiento.Situacion;
import org.me.gj.model.planillas.mantenimiento.Tablas1;
import org.me.gj.model.planillas.mantenimiento.TipContrato;
import org.me.gj.model.planillas.mantenimiento.TipTrabajador;
import org.me.gj.model.planillas.mantenimiento.UbigeoPla;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoPersonal {

    //Instancias de Objetos
    ListModelList<Sucursales> objlstSucursal;
    ListModelList<Tablas1> objlstTablas1;
    ListModelList<Personal> objlstPersonal;
    ListModelList<DerHabiente> objlstDerHabientes;
    ListModelList<DatosLaborales> objlstDatosLab;
    //aportaciones
    ListModelList<PersAportacion> objlstAportacion;
    PersAportacion objAportacion;

    Sucursales objSucursal;
    Tablas1 objTablas1;
    Personal objPersonal;
    DerHabiente objDerHabiente;
    DatosLaborales objDatosLab;

    Bancos objBancos;

    /*Condicion objCondicion;
     ListaPrecio objLpventas;
     Canal objCanal;
     FormaPago objFormaPago;
     Cliente objCliente;
     CliDireccion objCliDireccion;
     CliTelefono objCliTelefono;
     CliFinanciero objCliFinanciero;
     CliGarantia objCliGarantia;
     CliUtil objCliUtil;*/
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
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoCliente.class);

    //LOV NACION
    ListModelList<Nacion> objlstNacion;
    Nacion objNacion;

    //LOV NIVEL EDUCATIVO
    ListModelList<NivEducativo> objlstNivEdu;
    NivEducativo objNivEdu;

    //LOV OCUAPACION
    ListModelList<Ocupacion> objlstOcupacion;
    Ocupacion objOcupacion;

    //LOV UBIGEO PLANILLAS
    ListModelList<UbigeoPla> objlstUbigeoPla;
    UbigeoPla objUbigeoPla;

    //LOV AREAS
    ListModelList<ManAreas> objlstAreas;
    ManAreas objAreas;

    //LOV CARGOS
    ListModelList<ManCargos> objlstCargos;
    ManCargos objCargos;
    //LOV AFP
    ListModelList<ManAfpsCre> objlstAfp;
    ManAfpsCre objAfp;

    //LOV TIPO TRABAJADOR
    ListModelList<TipTrabajador> objlstTipTrabajador;
    TipTrabajador objTipTrabajador;

    //LOV TIPO CONTRATO
    ListModelList<TipContrato> objlstTipContrato;
    TipContrato objTipContrato;

    //LOV SITUACION
    ListModelList<Situacion> objlstSituacion;
    Situacion objSituacion;

    //LOV ENTIDADES PRESTADORAS DE SALUD
    ListModelList<EntiSalud> objlstEntiSalud;
    EntiSalud objEntiSalud;

    //LOV BANCOS
    ListModelList<Bancos> objlstBancos;

    //LOV HORARIOS
    ListModelList<HorariosPla> objlstHorarios;
    HorariosPla objHorarios;

    ListModelList<Sucursales> objlstSucursales;
    ArrayDescriptor arrayDescriptor;
    ARRAY arr;

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarPersonal(Personal objPersonal, DatosLaborales objDatosLab, PersAportacion objAportacion,
            PersPagoDep objPagoDepHab, PersPagoDep objPagoDepCts, Object[][] listaDerHabientes) throws SQLException, ParseException {

        String SQL_PROCEDURE = "{call codijisa.pack_tpersonal.p_insertarPersonal("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," // datos personales
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," //datos laborales se quito un ? de hextras
                + "?,?,?,?,?,?,?,?,?,?,?," //datos aportación
                + "?,?,?,?,?,?,?," //datos pagos haberes
                + "?,?,?,?,?,?,?," //datos pagos cts
                + "?,"//lista derecho habientes
                + "?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCEDURE);
            arrayDescriptor = ArrayDescriptor.createDescriptor("TYLIS_DERHABIENTE", con.getMetaData().getConnection());
            arr = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listaDerHabientes);
            cst.clearParameters();

            cst.setInt(1, objPersonal.getPltipdoc());//n_pltipdoc
            cst.setString(2, objPersonal.getPlnrodoc());//c_plnrodoc
            cst.setString(3, objPersonal.getPlapepat());//c_plapepat
            cst.setString(4, objPersonal.getPlapemat());//c_plapemat
            cst.setString(5, objPersonal.getPlnomemp());//c_plnomemp
            cst.setString(6, objPersonal.getPlnacion());//c_plnacion
            cst.setDate(7, new java.sql.Date(objPersonal.getPlfecnac().getTime()));//d_plfecnac
            cst.setInt(8, objPersonal.getPlsexo());//n_plsexo
            cst.setString(9, objPersonal.getPlestcivil());//c_plestcivil
            cst.setString(10, objPersonal.getPlgruposangui());//c_plgruposangui
            cst.setString(11, objPersonal.getPltelemp());//c_pltelemp
            cst.setString(12, objPersonal.getPltelemovil());//c_pltelemovil
            cst.setString(13, objPersonal.getPlemail());//c_plemail
            cst.setInt(14, objPersonal.getPldiscap());//n_pldiscap
            cst.setString(15, objPersonal.getPlnivedu());//c_plnivedu
            cst.setString(16, objPersonal.getPlidocup());//c_plidocup
            cst.setInt(17, objPersonal.getPlconddom());//n_plconddom
            cst.setString(18, objPersonal.getPldir_ubigeo());//c_pldir_ubigeo
            cst.setString(19, objPersonal.getPldir_via());//c_pldir_via
            cst.setString(20, objPersonal.getPldir_nomvia());//c_pldir_nomvia
            cst.setString(21, objPersonal.getPldir_num());//c_pldir_num
            cst.setString(22, objPersonal.getPldir_int());//c_pldir_int
            cst.setString(23, objPersonal.getPldir_zona());//c_pldir_zona
            cst.setString(24, objPersonal.getPldir_nomzona());//c_pldir_nomzona
            cst.setString(25, objPersonal.getPldiremp());//c_pldiremp
            cst.setString(26, objPersonal.getPldir_refer());//c_pldir_refer
            cst.setDouble(27, objPersonal.getPldir_corx());//c_pldir_corx
            cst.setDouble(28, objPersonal.getPldir_cory());//c_pldir_cory
            cst.setString(29, objUsuCredential.getCuenta());//c_plusuadd

            //DATOS LABORALES (24)
            cst.setInt(30, objUsuCredential.getCodemp());//n_emp_id
            cst.setInt(31, objDatosLab.getSuc_id());//n_suc_id
            cst.setString(32, objDatosLab.getPlcodemp());//c_plcodemp
            cst.setString(33, objDatosLab.getPlarea());//c_plarea
            cst.setString(34, objDatosLab.getPlccosto());//c_plccosto
            cst.setString(35, objDatosLab.getPlcatper());//c_plcatper
            cst.setString(36, objDatosLab.getPltiptra());//c_pltiptra
            cst.setString(37, objDatosLab.getPlhorari());//c_plhorari
            cst.setString(38, objDatosLab.getPlidcargo());//c_plidcargo
            cst.setInt(39, objDatosLab.getPl_cc());//n_pl_cc
            cst.setString(40, objDatosLab.getPltipcont());//c_pltipcont
            cst.setInt(41, objDatosLab.getPlsujconinm());//n_plsujconinm
            cst.setInt(42, objDatosLab.getPltipsue());//n_pltipsue
            cst.setInt(43, objDatosLab.getPlperrem());//n_plperrem
            //cst.setInt(44, objDatosLab.getPlhextra());//n_plhextra
            cst.setInt(44, objDatosLab.getPlputil());//n_plputil
            cst.setInt(45, objDatosLab.getPlquinta());//n_plquinta
            cst.setInt(46, objDatosLab.getPlotr5ta());//n_plotr5ta
            cst.setInt(47, objDatosLab.getPlippsvi());//n_plippsvi
            cst.setString(48, objDatosLab.getPlcarssp());//c_plcarssp
            cst.setInt(49, objDatosLab.getPlsindic());//n_plsindic
            cst.setInt(50, objDatosLab.getPlespens());//n_plespens
            cst.setDate(51, new java.sql.Date(objDatosLab.getPlfecing().getTime()));//d_plfecing
            cst.setDate(52, objDatosLab.getPlfecces() == null ? null : new java.sql.Date(objDatosLab.getPlfecces().getTime()));//d_plfecces
            //cst.setString("c_indsql_dp", objDatosLab.getIndsql());//

            //DATOS APORTACION (11)
            cst.setString(53, objAportacion.getPlregpen());//c_plregpen
            cst.setDate(54, objAportacion.getPlfiregpen() == null ? null : new java.sql.Date(objAportacion.getPlfiregpen().getTime()));//d_plfiregpen
            cst.setString(55, objAportacion.getPltippen());//c_pltippen
            cst.setString(56, objAportacion.getPlcodafp());//c_plcodafp
            cst.setString(57, objAportacion.getPlcarafp());//c_plcarafp
            cst.setInt(58, objAportacion.getPlcommix());//n_plcommix
            cst.setString(59, objAportacion.getPlpressal());//c_plpressal
            cst.setString(60, objAportacion.getPlsiteps());//c_plsiteps
            cst.setDate(61, objAportacion.getPlfbeps() == null ? null : new java.sql.Date(objAportacion.getPlfbeps().getTime()));//d_plfbeps
            cst.setInt(62, objAportacion.getPlsct_as());//n_plsct_as
            cst.setInt(63, objAportacion.getPlsct_pp());//n_plsct_pp
            //cst.setString("c_indsql_ap", objDatosLab.getIndsql());//

            //DATOS PAGO DEPOSITO HABERES (6)
            //cst.setInt("n_pltipdep", objPagoDepHab.getPlbanco());//
            cst.setInt(64, objPagoDepHab.getPlbanco());//n_plbanco_h
            cst.setInt(65, objPagoDepHab.getPltipcta());//n_pltipcta_h
            cst.setInt(66, objPagoDepHab.getPlmoneda());//n_plmoneda_h
            cst.setString(67, objPagoDepHab.getPlnrocta());//c_plnrocta_h
            cst.setInt(68, objPagoDepHab.getPltippago());//n_pltippago_h
            cst.setString(69, objPagoDepHab.getPlglosa());//c_plglosa_h
            cst.setString(70, objPagoDepHab.getIndsql());//c_indsql_ph

            //DATOS PAGO DEPOSITO CTS(7)
            //cst.setInt("n_pltipdep_c", objPagoDepCts.getPlbanco());//
            cst.setInt(71, objPagoDepCts.getPlbanco());//n_plbanco_c
            cst.setInt(72, objPagoDepCts.getPltipcta());//n_pltipcta_c
            cst.setInt(73, objPagoDepCts.getPlmoneda());//n_plmoneda_c
            cst.setString(74, objPagoDepCts.getPlnrocta());//c_plnrocta_c
            cst.setInt(75, objPagoDepCts.getPltippago());//n_pltippago_c
            cst.setString(76, objPagoDepCts.getPlglosa());//c_plglosa_c
            cst.setString(77, objPagoDepCts.getIndsql());//c_indsql_pc

            cst.setArray(78, arr);//l_derhabientes

            cst.registerOutParameter(79, java.sql.Types.NUMERIC);//n_flag
            cst.registerOutParameter(80, java.sql.Types.VARCHAR);//c_msg
            cst.execute();
            i_flagErrorBD = cst.getInt(79);//n_flag
            s_msg = cst.getString(80);//c_msg
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objCliente.getCli_razsoc());
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarPersonal(Personal objPersonal, DatosLaborales objDatosLab, PersAportacion objAportacion,
            PersPagoDep objPagoDepHab, PersPagoDep objPagoDepCts, Object[][] listaDerHabientes) throws SQLException, ParseException {

        String SQL_PROCEDURE = "{call codijisa.pack_tpersonal.p_modificarPersonal("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," //datos personales
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," //datos laborales ?,
                + "?,?,?,?,?,?,?,?,?,?,?,?," //datos aportación
                + "?,?,?,?,?,?,?,?," //datos pagos haberes
                + "?,?,?,?,?,?,?,?," //datos pagos cts
                + "?," //lista derechohabientes
                + "?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCEDURE);

            arrayDescriptor = ArrayDescriptor.createDescriptor("TYLIS_DERHABIENTE", con.getMetaData().getConnection());
            arr = new ARRAY(arrayDescriptor, con.getMetaData().getConnection(), listaDerHabientes);

            cst.clearParameters();
            /*cst.setInt("n_pltipdoc", objPersonal.getPltipdoc());//1
             cst.setString("c_plnrodoc", objPersonal.getPlnrodoc());//2
             cst.setInt("n_plestado", objPersonal.getPlestado());//3
             cst.setString("c_plapepat", objPersonal.getPlapepat());//4
             cst.setString("c_plapemat", objPersonal.getPlapemat());//5
             cst.setString("c_plnomemp", objPersonal.getPlnomemp());//6
             cst.setString("c_plnacion", objPersonal.getPlnacion());//7
             cst.setDate("d_plfecnac", new java.sql.Date(objPersonal.getPlfecnac().getTime()));//8
             cst.setInt("n_plsexo", objPersonal.getPlsexo());//9
             cst.setString("c_plestcivil", objPersonal.getPlestcivil());//10
             cst.setString("c_plgruposangui", objPersonal.getPlgruposangui());//11
             cst.setString("c_pltelemp", objPersonal.getPltelemp());//12
             cst.setString("c_pltelemovil", objPersonal.getPltelemovil());//13
             cst.setString("c_plemail", objPersonal.getPlemail());//14
             cst.setInt("n_pldiscap", objPersonal.getPldiscap());//15
             cst.setString("c_plnivedu", objPersonal.getPlnivedu());//16
             cst.setString("c_plidocup", objPersonal.getPlidocup());//17
             cst.setInt("n_plconddom", objPersonal.getPlconddom());//18
             cst.setString("c_pldir_ubigeo", objPersonal.getPldir_ubigeo());//19
             cst.setString("c_pldir_via", objPersonal.getPldir_via());//20
             cst.setString("c_pldir_nomvia", objPersonal.getPldir_nomvia());//21
             cst.setString("c_pldir_num", objPersonal.getPldir_num());//22
             cst.setString("c_pldir_int", objPersonal.getPldir_int());//23
             cst.setString("c_pldir_zona", objPersonal.getPldir_zona());//24
             cst.setString("c_pldir_nomzona", objPersonal.getPldir_nomzona());//25
             cst.setString("c_pldiremp", objPersonal.getPldiremp());//26
             cst.setString("c_pldir_refer", objPersonal.getPldir_refer());//27
             cst.setString("c_plusumod", objUsuCredential.getCuenta());//28

             //DATOS LABORALES (28)
             cst.setInt("n_emp_id", objUsuCredential.getCodemp());//29
             cst.setInt("n_suc_id", objDatosLab.getSuc_id());//30
             cst.setString("c_plcodemp", objDatosLab.getPlcodemp());//31
             cst.setString("c_plarea", objDatosLab.getPlarea());//32
             cst.setString("c_plccosto", objDatosLab.getPlccosto());//33
             cst.setString("c_plcatper", objDatosLab.getPlcatper());//34
             cst.setString("c_pltiptra", objDatosLab.getPltiptra());//35
             cst.setString("c_plhorari", objDatosLab.getPlhorari());//36
             cst.setString("c_plidcargo", objDatosLab.getPlidcargo());//37
             cst.setInt("n_pl_cc", objDatosLab.getPl_cc());//38
             cst.setString("c_pltipcont", objDatosLab.getPltipcont());//39
             cst.setInt("n_plsujconinm", objDatosLab.getPlsujconinm());//40
             cst.setInt("n_pltipsue", objDatosLab.getPltipsue());//41
             cst.setInt("n_plperrem", objDatosLab.getPlperrem());//42
             cst.setInt("n_plhextra", objDatosLab.getPlhextra());//43
             cst.setInt("n_plputil", objDatosLab.getPlputil());//44
             cst.setInt("n_plquinta", objDatosLab.getPlquinta());//45
             cst.setInt("n_plotr5ta", objDatosLab.getPlotr5ta());//46
             cst.setInt("n_plippsvi", objDatosLab.getPlippsvi());//47
             cst.setString("c_plcarssp", objDatosLab.getPlcarssp());//48
             cst.setInt("n_plsindic", objDatosLab.getPlsindic());//49
             cst.setInt("n_plespens", objDatosLab.getPlespens());//50
             cst.setDate("d_plfecing", objDatosLab.getPlfecing() == null ? null : new java.sql.Date(objDatosLab.getPlfecing().getTime()));//51
             cst.setDate("d_plfecces", objDatosLab.getPlfecces() == null ? null : new java.sql.Date(objDatosLab.getPlfecces().getTime()));//52            
             cst.setString("c_plcesemot", objDatosLab.getPlcesemot());//53
             cst.setString("c_plcesedet", objDatosLab.getPlcesedet());//54
             cst.setString("c_plceseobs", objDatosLab.getPlceseobs());//55
             cst.setString("c_indsql_dl", objDatosLab.getIndsql());//56

             //DATOS APORTACION (12)
             cst.setString("c_plregpen", objAportacion.getPlregpen());//57
             cst.setDate("d_plfiregpen", objAportacion.getPlfiregpen() == null ? null : new java.sql.Date(objAportacion.getPlfiregpen().getTime()));//58
             cst.setString("c_pltippen", objAportacion.getPltippen());//59
             cst.setString("c_plcodafp", objAportacion.getPlcodafp());//60
             cst.setString("c_plcarafp", objAportacion.getPlcarafp());//61
             cst.setInt("n_plcommix", objAportacion.getPlcommix());//62
             cst.setString("c_plpressal", objAportacion.getPlpressal());//63
             cst.setString("c_plsiteps", objAportacion.getPlsiteps());//64
             cst.setDate("d_plfbeps", objAportacion.getPlfbeps() == null ? null : new java.sql.Date(objAportacion.getPlfbeps().getTime()));//65
             cst.setInt("n_plsct_as", objAportacion.getPlsct_as());//66
             cst.setInt("n_plsct_pp", objAportacion.getPlsct_pp());//67
             cst.setString("c_indsql_ap", objAportacion.getIndsql());//68

             //DATOS PAGO DEPOSITO HABERES (7)
             //cst.setInt("n_pltipdep", objPagoDepHab.getPlbanco());//
             cst.setInt("n_plbanco_h", objPagoDepHab.getPlbanco());//69
             cst.setInt("n_pltipcta_h", objPagoDepHab.getPltipcta());//70
             cst.setInt("n_plmoneda_h", objPagoDepHab.getPlmoneda());//71
             cst.setString("c_plnrocta_h", objPagoDepHab.getPlnrocta());//69
             cst.setInt("n_pltippago_h", objPagoDepHab.getPltippago());//70
             cst.setString("c_plglosa_h", objPagoDepHab.getPlglosa());//71
             cst.setString("c_indsql_ph", objPagoDepHab.getIndsql());//72

             //DATOS PAGO DEPOSITO CTS(7)
             //cst.setInt("n_pltipdep", objPagoDepCts.getPlbanco());//
             cst.setInt("n_plbanco_c", objPagoDepCts.getPlbanco());//72
             cst.setInt("n_pltipcta_c", objPagoDepCts.getPltipcta());//73
             cst.setInt("n_plmoneda_c", objPagoDepCts.getPlmoneda());//74
             cst.setString("c_plnrocta_c", objPagoDepCts.getPlnrocta());//75
             cst.setInt("n_pltippago_c", objPagoDepCts.getPltippago());//76
             cst.setString("c_plglosa_c", objPagoDepCts.getPlglosa());//77
             cst.setString("c_indsql_pc", objPagoDepCts.getIndsql());//78

             cst.setArray("l_derhabientes", arr);//79

             cst.registerOutParameter("n_flag", java.sql.Types.NUMERIC);//80
             cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);//81
             cst.execute();
             i_flagErrorBD = cst.getInt("n_flag");//80
             s_msg = cst.getString("c_msg");//81*/

            cst.setInt(1, objPersonal.getPltipdoc());//n_pltipdoc
            cst.setString(2, objPersonal.getPlnrodoc());//c_plnrodoc
            cst.setInt(3, objPersonal.getPlestado());//n_plestado
            cst.setString(4, objPersonal.getPlapepat());//c_plapepat
            cst.setString(5, objPersonal.getPlapemat());//c_plapemat
            cst.setString(6, objPersonal.getPlnomemp());//c_plnomemp
            cst.setString(7, objPersonal.getPlnacion());//c_plnacion
            cst.setDate(8, new java.sql.Date(objPersonal.getPlfecnac().getTime()));//d_plfecnac
            cst.setInt(9, objPersonal.getPlsexo());//n_plsexo
            cst.setString(10, objPersonal.getPlestcivil());//c_plestcivil
            cst.setString(11, objPersonal.getPlgruposangui());//c_plgruposangui
            cst.setString(12, objPersonal.getPltelemp());//c_pltelemp
            cst.setString(13, objPersonal.getPltelemovil());//c_pltelemovil
            cst.setString(14, objPersonal.getPlemail());//c_plemail
            cst.setInt(15, objPersonal.getPldiscap());//n_pldiscap
            cst.setString(16, objPersonal.getPlnivedu());//c_plnivedu
            cst.setString(17, objPersonal.getPlidocup());//c_plidocup
            cst.setInt(18, objPersonal.getPlconddom());//n_plconddom
            cst.setString(19, objPersonal.getPldir_ubigeo());//c_pldir_ubigeo
            cst.setString(20, objPersonal.getPldir_via());//c_pldir_via
            cst.setString(21, objPersonal.getPldir_nomvia());//c_pldir_nomvia
            cst.setString(22, objPersonal.getPldir_num());//c_pldir_num
            cst.setString(23, objPersonal.getPldir_int());//c_pldir_int
            cst.setString(24, objPersonal.getPldir_zona());//c_pldir_zona
            cst.setString(25, objPersonal.getPldir_nomzona());//c_pldir_nomzona
            cst.setString(26, objPersonal.getPldiremp());//c_pldiremp
            cst.setString(27, objPersonal.getPldir_refer());//c_pldir_refer
            cst.setDouble(28, objPersonal.getPldir_corx());//c_pldir_corx
            cst.setDouble(29, objPersonal.getPldir_cory());//c_pldir_cory
            cst.setString(30, objUsuCredential.getCuenta());//c_plusumod

            //DATOS LABORALES (28)
            cst.setInt(31, objUsuCredential.getCodemp());//n_emp_id
            cst.setInt(32, objDatosLab.getSuc_id());//n_suc_id
            cst.setString(33, objDatosLab.getPlcodemp());//c_plcodemp
            cst.setString(34, objDatosLab.getPlarea());//c_plarea
            cst.setString(35, objDatosLab.getPlccosto());//c_plccosto
            cst.setString(36, objDatosLab.getPlcatper());//c_plcatper
            cst.setString(37, objDatosLab.getPltiptra());//c_pltiptra
            cst.setString(38, objDatosLab.getPlhorari());//c_plhorari
            cst.setString(39, objDatosLab.getPlidcargo());//c_plidcargo
            cst.setInt(40, objDatosLab.getPl_cc());//n_pl_cc
            cst.setString(41, objDatosLab.getPltipcont());//c_pltipcont
            cst.setInt(42, objDatosLab.getPlsujconinm());//n_plsujconinm
            cst.setInt(43, objDatosLab.getPltipsue());//n_pltipsue
            cst.setInt(44, objDatosLab.getPlperrem());//n_plperrem
            //cst.setInt(45, objDatosLab.getPlhextra());//n_plhextra
            cst.setInt(45, objDatosLab.getPlputil());//n_plputil
            cst.setInt(46, objDatosLab.getPlquinta());//n_plquinta
            cst.setInt(47, objDatosLab.getPlotr5ta());//n_plotr5ta
            cst.setInt(48, objDatosLab.getPlippsvi());//n_plippsvi
            cst.setString(49, objDatosLab.getPlcarssp());//c_plcarssp
            cst.setInt(50, objDatosLab.getPlsindic());//n_plsindic
            cst.setInt(51, objDatosLab.getPlespens());//n_plespens
            cst.setDate(52, objDatosLab.getPlfecing() == null ? null : new java.sql.Date(objDatosLab.getPlfecing().getTime()));//d_plfecing
            cst.setDate(53, objDatosLab.getPlfecces() == null ? null : new java.sql.Date(objDatosLab.getPlfecces().getTime()));//d_plfecces
            cst.setString(54, objDatosLab.getPlcesemot());//c_plcesemot
            cst.setString(55, objDatosLab.getPlcesedet());//c_plcesedet
            cst.setString(56, objDatosLab.getPlceseobs());//c_plceseobs
            cst.setString(57, objDatosLab.getIndsql());//c_indsql_dl

            //DATOS APORTACION (12)
            cst.setString(58, objAportacion.getPlregpen());//c_plregpen
            cst.setDate(59, objAportacion.getPlfiregpen() == null ? null : new java.sql.Date(objAportacion.getPlfiregpen().getTime()));//d_plfiregpen
            cst.setString(60, objAportacion.getPltippen());//c_pltippen
            cst.setString(61, objAportacion.getPlcodafp());//c_plcodafp
            cst.setString(62, objAportacion.getPlcarafp());//c_plcarafp
            cst.setInt(63, objAportacion.getPlcommix());//n_plcommix
            cst.setString(64, objAportacion.getPlpressal());//c_plpressal
            cst.setString(65, objAportacion.getPlsiteps());//c_plsiteps
            cst.setDate(66, objAportacion.getPlfbeps() == null ? null : new java.sql.Date(objAportacion.getPlfbeps().getTime()));//d_plfbeps
            cst.setInt(67, objAportacion.getPlsct_as());//n_plsct_as
            cst.setInt(68, objAportacion.getPlsct_pp());//n_plsct_pp
            cst.setString(69, objAportacion.getIndsql());//c_indsql_ap

            //DATOS PAGO DEPOSITO HABERES (7)
            //cst.setInt("n_pltipdep", objPagoDepHab.getPlbanco());//
            cst.setInt(70, objPagoDepHab.getPlcorrel_dep());//n_plbanco_h
            cst.setInt(71, objPagoDepHab.getPlbanco());//n_plbanco_h
            cst.setInt(72, objPagoDepHab.getPltipcta());//n_pltipcta_h
            cst.setInt(73, objPagoDepHab.getPlmoneda());//n_plmoneda_h
            cst.setString(74, objPagoDepHab.getPlnrocta());//c_plnrocta_h
            cst.setInt(75, objPagoDepHab.getPltippago());//n_pltippago_h
            cst.setString(76, objPagoDepHab.getPlglosa());//c_plglosa_h
            cst.setString(77, objPagoDepHab.getIndsql());//c_indsql_ph

            //DATOS PAGO DEPOSITO CTS(7)
            //cst.setInt("n_pltipdep", objPagoDepCts.getPlbanco());//
            cst.setInt(78, objPagoDepCts.getPlcorrel_dep());//n_plbanco_h
            cst.setInt(79, objPagoDepCts.getPlbanco());//n_plbanco_c
            cst.setInt(80, objPagoDepCts.getPltipcta());//n_pltipcta_c
            cst.setInt(81, objPagoDepCts.getPlmoneda());//n_plmoneda_c
            cst.setString(82, objPagoDepCts.getPlnrocta());//c_plnrocta_c
            cst.setInt(83, objPagoDepCts.getPltippago());//n_pltippago_c
            cst.setString(84, objPagoDepCts.getPlglosa());//c_plglosa_c
            cst.setString(85, objPagoDepCts.getIndsql());//c_indsql_pc

            cst.setArray(86, arr);//l_derhabientes

            cst.registerOutParameter(87, java.sql.Types.NUMERIC);//n_flag
            cst.registerOutParameter(88, java.sql.Types.VARCHAR);//c_msg
            cst.execute();
            i_flagErrorBD = cst.getInt(87);//n_flag
            s_msg = cst.getString(88);//c_msg

            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objCliente.getCli_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarPersonal(Personal objPersonal) throws SQLException {

        String SQL_PROCEDURE = "{call codijisa.pack_tpersonal.p_eliminarPersonal(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCEDURE);
            cst.clearParameters();
            cst.setInt("n_pltipdoc", objPersonal.getPltipdoc());//1
            cst.setString("c_plnrodoc", objPersonal.getPlnrodoc());//2
            cst.setString("c_plusumod", objUsuCredential.getCuenta());//3
            cst.registerOutParameter("n_flag", java.sql.Types.NUMERIC);//4
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);//5
            cst.execute();
            i_flagErrorBD = cst.getInt("n_flag");//4
            s_msg = cst.getString("c_msg");//5
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + codigo);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ListModelList<Tablas1> listaTablas1(String tabla_cod) throws SQLException {

        String SQL_QUERY = " select * from tpltablas1 where tabla_estado = 1 and tabla_id <> '000' and tabla_cod = '" + tabla_cod + "'";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_QUERY);
            objlstTablas1 = new ListModelList<Tablas1>();
            while (rs.next()) {
                objTablas1 = new Tablas1();
                objTablas1.setTabla_cod(rs.getString("tabla_cod"));
                objTablas1.setTabla_id(rs.getString("tabla_id"));
                objTablas1.setTabla_descri(rs.getString("tabla_descri"));
                objTablas1.setTabla_valor1(rs.getInt("tabla_valor1"));
                objTablas1.setTabla_valor2(rs.getInt("tabla_valor2"));
                objTablas1.setTabla_tipo1(rs.getString("tabla_tipo1"));
                objTablas1.setTabla_datfor(rs.getString("tabla_datfor"));
                objTablas1.setTabla_valor3(rs.getString("tabla_valor3"));
                objTablas1.setTabla_valor4(rs.getString("tabla_valor4"));
                objTablas1.setTabla_prior(rs.getInt("tabla_prior"));
                objTablas1.setTabla_datbol(rs.getString("tabla_datbol"));
                objlstTablas1.add(objTablas1);
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlsttabGen.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstTablas1;
    }

    public ListModelList<Personal> consultaPersonal(String sucursal, String per_ingreso, String per_cesado, int seleccion, String busqueda, String cesados, int estado, String area, String costo) throws SQLException {

        String documento = "", paterno = "", materno = "", nombres = "", cargo = "";
        switch (seleccion) {

            case 1:
                documento = busqueda;

                break;
            case 2:
                paterno = busqueda;

                break;
            case 3:
                materno = busqueda;

                break;
            case 4:
                nombres = busqueda;

                break;
            case 5:
                cargo = busqueda;

                break;
        }

        String sql_query = "{call codijisa.pack_tpersonal.p_consultaPersonal(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        //objlstPersonal.clear();
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, objUsuCredential.getCodemp());
                cst.setString(2, sucursal);
                cst.setString(3, per_ingreso);
                cst.setString(4, per_cesado);
                cst.setString(5, documento);
                cst.setString(6, paterno);
                cst.setString(7, materno);
                cst.setString(8, nombres);
                cst.setString(9, cargo);
                cst.setString(10, cesados);
                cst.setInt(11, estado);
                //cst.setString(12, orden);
                cst.registerOutParameter(12, OracleTypes.CURSOR); //REF CURSOR
                cst.setString(13, area);
                cst.setString(14, costo);
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(12);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();

                while (rs.next()) {
                    objPersonal = new Personal();
                    objPersonal.setPlestado(rs.getInt("plestado"));
                    objPersonal.setPlapepat(rs.getString("plapepat"));
                    objPersonal.setPlapemat(rs.getString("plapemat"));
                    objPersonal.setPlnomemp(rs.getString("plnomemp"));
                    objPersonal.setPlnacion(rs.getString("plnacion"));
                    objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                    objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                    objPersonal.setPlfecnac(rs.getDate("plfecnac"));
                    objPersonal.setPlsexo(rs.getInt("plsexo"));
                    objPersonal.setPlestcivil(rs.getString("plestcivil"));
                    objPersonal.setPlgruposangui(rs.getString("plgruposangui"));
                    objPersonal.setPltelemp(rs.getString("pltelemp"));
                    objPersonal.setPltelemovil(rs.getString("pltelemovil"));
                    objPersonal.setPlemail(rs.getString("plemail"));
                    objPersonal.setPldiscap(rs.getInt("pldiscap"));
                    objPersonal.setPlnivedu(rs.getString("plnivedu"));
                    objPersonal.setPlidocup(rs.getString("plidocup"));
                    //DOMICILIO
                    objPersonal.setPlconddom(rs.getInt("plconddom"));
                    objPersonal.setPldir_ubigeo(rs.getString("pldir_ubigeo"));
                    objPersonal.setPldir_via(rs.getString("pldir_via"));
                    objPersonal.setPldir_nomvia(rs.getString("pldir_nomvia"));
                    objPersonal.setPldir_num(rs.getString("pldir_num"));
                    objPersonal.setPldir_int(rs.getString("pldir_int"));
                    objPersonal.setPldir_zona(rs.getString("pldir_zona"));
                    objPersonal.setPldir_nomzona(rs.getString("pldir_nomzona"));
                    objPersonal.setPldiremp(rs.getString("pldiremp"));
                    objPersonal.setPldir_refer(rs.getString("pldir_refer"));
                    objPersonal.setPldir_corx(rs.getDouble("pldir_corx"));
                    objPersonal.setPldir_cory(rs.getDouble("pldir_cory"));

                    //DATOS LABORALES
                    objPersonal.setSuc_id(rs.getInt("suc_id"));
                    objPersonal.setEmp_id(rs.getInt("emp_id"));
                    objPersonal.setPlcodemp(rs.getString("plcodemp"));
                    objPersonal.setPlarea(rs.getString("plarea"));
                    objPersonal.setPlccosto(rs.getString("plccosto"));
                    objPersonal.setPlcatper(rs.getString("plcatper"));
                    objPersonal.setPltiptra(rs.getString("pltiptra"));
                    objPersonal.setPlhorari(rs.getString("plhorari"));
                    objPersonal.setPlidcargo(rs.getString("plidcargo"));
                    objPersonal.setPl_cc(rs.getInt("pl_cc"));
                    objPersonal.setPltipcont(rs.getString("pltipcont"));
                    objPersonal.setPlsujconinm(rs.getInt("plsujconinm"));
                    objPersonal.setPltipsue(rs.getInt("pltipsue"));
                    objPersonal.setPlperrem(rs.getInt("plperrem"));
                    //  objPersonal.setPlhextra(rs.getInt("plhextra"));
                    objPersonal.setPlputil(rs.getInt("plputil"));
                    objPersonal.setPlquinta(rs.getInt("plquinta"));
                    objPersonal.setPlotr5ta(rs.getInt("plotr5ta"));
                    objPersonal.setPlippsvi(rs.getInt("plippsvi"));
                    objPersonal.setPlcarssp(rs.getString("plcarssp"));
                    objPersonal.setPlsindic(rs.getInt("plsindic"));
                    objPersonal.setPlespens(rs.getInt("plespens"));
                    objPersonal.setPlfecing(rs.getDate("plfecing"));
                    objPersonal.setPlfecces(rs.getDate("plfecces"));
                    objPersonal.setPlcesemot(rs.getString("plcesemot"));
                    objPersonal.setPlcesedet(rs.getString("plcesedet"));
                    objPersonal.setPlceseobs(rs.getString("plceseobs"));

                    //DATOS APORTACION
                    objPersonal.setPlregpen(rs.getString("plregpen"));
                    objPersonal.setPlfiregpen(rs.getDate("plfiregpen"));
                    objPersonal.setPltippen(rs.getString("pltippen"));
                    objPersonal.setPlcodafp(rs.getString("plcodafp"));
                    objPersonal.setPlcarafp(rs.getString("plcarafp"));
                    objPersonal.setPlcommix(rs.getInt("plcommix"));
                    objPersonal.setPlpressal(rs.getString("plpressal"));
                    objPersonal.setPlsiteps(rs.getString("plsiteps"));
                    objPersonal.setPlfbeps(rs.getDate("plfbeps"));
                    objPersonal.setPlsct_as(rs.getInt("plsct_as"));
                    objPersonal.setPlsct_pp(rs.getInt("plsct_pp"));

                    //DATOS PAGOS HABERES
                    objPersonal.setPlcorrel_dep_h(rs.getInt("plcorrel_dep_h"));
                    objPersonal.setPlbanco_h(rs.getInt("plbanco_h"));
                    objPersonal.setPltipcta_h(rs.getInt("pltipcta_h"));
                    objPersonal.setPlmoneda_h(rs.getInt("plmoneda_h"));
                    objPersonal.setPlnrocta_h(rs.getString("plnrocta_h"));
                    objPersonal.setPltippago_h(rs.getInt("pltippago_h"));
                    objPersonal.setPlglosa_h(rs.getString("plglosa_h"));

                    //DATOS PAGOS CTS
                    objPersonal.setPlcorrel_dep_c(rs.getInt("plcorrel_dep_c"));
                    objPersonal.setPlbanco_c(rs.getInt("plbanco_c"));
                    objPersonal.setPltipcta_c(rs.getInt("pltipcta_c"));
                    objPersonal.setPlmoneda_c(rs.getInt("plmoneda_c"));
                    objPersonal.setPlnrocta_c(rs.getString("plnrocta_c"));
                    objPersonal.setPltippago_c(rs.getInt("pltippago_c"));
                    objPersonal.setPlglosa_c(rs.getString("plglosa_c"));

                    //DESCRIPCIONES
                    objPersonal.setPlnacion_des(rs.getString("plnacion_des"));
                    objPersonal.setPlnivedu_des(rs.getString("plnivedu_des"));
                    objPersonal.setPlidocup_des(rs.getString("plidocup_des"));
                    objPersonal.setPldir_ubigeo_des(rs.getString("pldir_ubigeo_des"));
                    objPersonal.setPlarea_des(rs.getString("plarea_des"));
                    objPersonal.setPlccosto_des(rs.getString("plccosto_des"));
                    objPersonal.setPltiptra_des(rs.getString("pltiptra_des"));
                    objPersonal.setPlhorari_des(rs.getString("plhorari_des"));
                    objPersonal.setPlidcargo_des(rs.getString("plidcargo_des"));
                    objPersonal.setPltipcont_des(rs.getString("pltipcont_des"));
                    objPersonal.setPlregpen_des(rs.getString("plregpen_des"));
                    objPersonal.setPlcodafp_des(rs.getString("plcodafp_des"));
                    objPersonal.setPlpressal_des(rs.getString("plpressal_des"));
                    objPersonal.setPlsiteps_des(rs.getString("plsiteps_des"));
                    objPersonal.setPlbanco_h_des(rs.getString("plbanco_h_des"));
                    objPersonal.setPlbanco_c_des(rs.getString("plbanco_c_des"));
                    objPersonal.setSuc_id_des(rs.getString("suc_id_des"));

                    //AUDITORIA
                    objPersonal.setPlusuadd(rs.getString("plusuadd"));
                    objPersonal.setPlfecadd(rs.getTimestamp("plfecadd"));
                    objPersonal.setPlusumod(rs.getString("plusumod"));
                    objPersonal.setPlfecmod(rs.getTimestamp("plfecmod"));

                    objlstPersonal.add(objPersonal);

                }
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

        return objlstPersonal;
    }

    public ListModelList<DerHabiente> listaDerHabiente(int tipdoc, String nrodoc) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_listaDerHabientes(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, tipdoc);
                cst.setString(2, nrodoc);
                cst.registerOutParameter(3, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(3);

                objlstDerHabientes = new ListModelList<DerHabiente>();

                while (rs.next()) {
                    objDerHabiente = new DerHabiente();
                    objDerHabiente.setPldh_id(rs.getInt("pldh_id"));
                    objDerHabiente.setPldh_estado(rs.getInt("pldh_estado"));
                    objDerHabiente.setPldh_tipdoc(rs.getInt("pldh_tipdoc"));
                    objDerHabiente.setPldh_nrodoc(rs.getString("pldh_nrodoc"));
                    objDerHabiente.setPldh_apepat(rs.getString("pldh_apepat"));
                    objDerHabiente.setPldh_apemat(rs.getString("pldh_apemat"));
                    objDerHabiente.setPldh_nombres(rs.getString("pldh_nombres"));
                    objDerHabiente.setPldh_fecnac(rs.getDate("pldh_fecnac"));
                    objDerHabiente.setPldh_sexo(rs.getInt("pldh_sexo"));

                    objDerHabiente.setPldh_vinfam(rs.getString("pldh_vinfam"));
                    objDerHabiente.setPldh_fecalt(rs.getDate("pldh_fecalt"));
                    objDerHabiente.setPldh_tipdocvf(rs.getString("pldh_tipdocvf"));
                    objDerHabiente.setPldh_nrocam(rs.getString("pldh_nrocam"));

                    objDerHabiente.setPldh_nrd(rs.getString("pldh_nrd"));
                    objDerHabiente.setPldh_munpn(rs.getString("pldh_munpn"));
                    objDerHabiente.setPldh_nacion(rs.getString("pldh_nacion"));
                    objDerHabiente.setPldh_situa(rs.getString("pldh_situa"));
                    objDerHabiente.setPldh_tipbaj(rs.getString("pldh_tipbaj"));
                    objDerHabiente.setPldh_fecbaj(rs.getDate("pldh_fecbaj"));

                    objDerHabiente.setPldh_dirtipvia(rs.getString("pldh_dirtipvia"));
                    objDerHabiente.setPldh_dirnomvia(rs.getString("pldh_dirnomvia"));
                    objDerHabiente.setPldh_indom(rs.getInt("pldh_indom"));
                    objDerHabiente.setPldh_dirtipzon(rs.getString("pldh_dirtipzon"));
                    objDerHabiente.setPldh_dirnomzon(rs.getString("pldh_dirnomzon"));
                    objDerHabiente.setPldh_dirnumvia(rs.getString("pldh_dirnumvia"));
                    objDerHabiente.setPldh_dirint(rs.getString("pldh_dirint"));
                    objDerHabiente.setPldh_dirref(rs.getString("pldh_dirref"));
                    objDerHabiente.setPldh_dirubigeo(rs.getString("pldh_dirubigeo"));

                    objDerHabiente.setPldh_tipdoc_des(rs.getString("pldh_tipdoc_des"));
                    objDerHabiente.setPldh_sexo_des(rs.getString("pldh_sexo_des"));
                    objDerHabiente.setPldh_vinfam_des(rs.getString("pldh_vinfam_des"));
                    objDerHabiente.setPldh_munpn_des(rs.getString("pldh_munpn_des"));
                    objDerHabiente.setPldh_nacion_des(rs.getString("pldh_nacion_des"));
                    objDerHabiente.setPldh_situa_des(rs.getString("pldh_situa_des"));
                    objDerHabiente.setPldh_dirubigeo_des(rs.getString("pldh_dirubigeo_des"));
                    objDerHabiente.setPldh_estudios(rs.getInt("estudios"));
                    objDerHabiente.setPldh_discapacidad(rs.getInt("discapacidad"));
                    objlstDerHabientes.add(objDerHabiente);
                }
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaCompRetDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstDerHabientes;
    }

    public ListModelList<DatosLaborales> listaDatosLab(int tipdoc, String nrodoc) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_listaDatosLab(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, tipdoc);
                cst.setString(2, nrodoc);
                cst.registerOutParameter(3, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(3);

                objlstDatosLab = new ListModelList<DatosLaborales>();

                while (rs.next()) {
                    objDatosLab = new DatosLaborales();
                    objDatosLab.setEmp_id_des(rs.getString("emp_id_des"));
                    objDatosLab.setPlfecing(rs.getDate("plfecing"));
                    objDatosLab.setPlfecces(rs.getDate("plfecces"));
                    objDatosLab.setPlcesemot(rs.getString("plcesemot_des"));
                    objDatosLab.setPlarea_des(rs.getString("plarea_des"));
                    objDatosLab.setPlidcargo_des(rs.getString("plidcargo_des"));
                    objDatosLab.setPlusuadd(rs.getString("plusuadd"));
                    //objDatosLab.setPlceseobs(rs.getString("plceseobs"));
                    objlstDatosLab.add(objDatosLab);
                }
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaCompRetDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstDatosLab;
    }

    public int validaNumDoc(int tipdoc, String nrodoc) throws SQLException {
        int valida = 0;

        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call codijisa.pack_tpersonal.f_validaNumDoc(?,?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, tipdoc);
            cst.setString(3, nrodoc);
            cst.execute();
            valida = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            valida = 1;
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            valida = 1;
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return valida;

    }

    public int validaDigitosCtaBan(int tipdoc, String nrodoc) throws SQLException {
        int valida = 0;

        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call codijisa.pack_tpersonal.f_validaNumCtaBan(?,?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.NUMERIC);
            cst.setInt(2, tipdoc);
            cst.setString(3, nrodoc);
            cst.execute();
            valida = cst.getInt(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            valida = 1;
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            valida = 1;
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return valida;

    }

    // LOV NACION
    public ListModelList<Nacion> busquedaLovNacion() throws SQLException {

        String sql_nacion = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00009' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " order by p.tabla_id";

        objlstNacion = new ListModelList<Nacion>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nacion);

            while (rs.next()) {
                objNacion = new Nacion();
                objNacion.setNacion_id(rs.getString("tabla_id"));
                objNacion.setNacion_des(rs.getString("tabla_descri"));
                objlstNacion.add(objNacion);
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

        return objlstNacion;

    }

    public ListModelList<Nacion> busquedaLovNacion2(String consulta) throws SQLException {

        String sql_nacion = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00009' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " and p.tabla_descri like '%" + consulta + "%'"
                + "order by p.tabla_id";

        objlstNacion = new ListModelList<Nacion>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nacion);

            while (rs.next()) {
                objNacion = new Nacion();
                objNacion.setNacion_id(rs.getString("tabla_id"));
                objNacion.setNacion_des(rs.getString("tabla_descri"));
                objlstNacion.add(objNacion);
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

        return objlstNacion;

    }

    public Nacion getLovNacion(String nacion) throws SQLException {
        String sql_nacion = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00009' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " and p.tabla_id like '%" + nacion + "%'"
                + "order by p.tabla_id";
        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nacion);
            while (rs.next()) {
                objNacion = new Nacion();
                objNacion.setNacion_id(rs.getString("tabla_id"));
                objNacion.setNacion_des(rs.getString("tabla_descri"));

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

        return objNacion;

    }

    // LOV NIVEL EDUCATIVO
    public ListModelList<NivEducativo> busquedaLovNivEdu() throws SQLException {

        String sql_nivel = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00011' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " order by p.tabla_id ";
        objlstNivEdu = new ListModelList<NivEducativo>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nivel);
            while (rs.next()) {

                objNivEdu = new NivEducativo();
                objNivEdu.setNivedu_id(rs.getString("tabla_id"));
                objNivEdu.setNivedu_des(rs.getString("tabla_descri"));
                objlstNivEdu.add(objNivEdu);

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

        return objlstNivEdu;
    }

    public ListModelList<NivEducativo> busquedaLovNivEdu2(String consulta) throws SQLException {

        String sql_nivel = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00011' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " and p.tabla_descri like '%" + consulta + "%' "
                + " order by p.tabla_id ";
        objlstNivEdu = new ListModelList<NivEducativo>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nivel);
            while (rs.next()) {

                objNivEdu = new NivEducativo();
                objNivEdu.setNivedu_id(rs.getString("tabla_id"));
                objNivEdu.setNivedu_des(rs.getString("tabla_descri"));
                objlstNivEdu.add(objNivEdu);

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

        return objlstNivEdu;
    }

    public NivEducativo getLovNivEdu(String nivel) throws SQLException {

        String sql_nivel = "select p.* from tpltablas1 p "
                + " where p.tabla_cod = '00011' "
                + " and  p.tabla_id <> '000' "
                + " and p.tabla_estado = '1' "
                + " and p.tabla_id like '%" + nivel + "%' "
                + " order by p.tabla_id ";
        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_nivel);
            objNivEdu = null;
            while (rs.next()) {

                objNivEdu = new NivEducativo();
                objNivEdu.setNivedu_id(rs.getString("tabla_id"));
                objNivEdu.setNivedu_des(rs.getString("tabla_descri"));

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

        return objNivEdu;
    }

    //LOV OCUPACION
    public ListModelList<Ocupacion> busquedaLovOcupacion() throws SQLException {

        String sql_ocup = "select t.* from tplocupacion t "
                + "where t.plocu_estado = '1' "
                + "order by t.plocu_id ";

        objlstOcupacion = new ListModelList<Ocupacion>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ocup);

            while (rs.next()) {
                objOcupacion = new Ocupacion();
                objOcupacion.setOcup_id(rs.getString("plocu_id"));
                objOcupacion.setOcup_des(rs.getString("plocu_descri"));
                objlstOcupacion.add(objOcupacion);

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

        return objlstOcupacion;

    }

    public ListModelList<Ocupacion> busquedaLovOcupacion2(String consulta) throws SQLException {

        String sql_ocup = "select t.* from tplocupacion t "
                + " where t.plocu_estado = '1' "
                + " and t.plocu_descri like '%" + consulta + "%' "
                + " order by t.plocu_id ";

        objlstOcupacion = new ListModelList<Ocupacion>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ocup);

            while (rs.next()) {
                objOcupacion = new Ocupacion();
                objOcupacion.setOcup_id(rs.getString("plocu_id"));
                objOcupacion.setOcup_des(rs.getString("plocu_descri"));
                objlstOcupacion.add(objOcupacion);

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

        return objlstOcupacion;

    }

    public Ocupacion getLovOcupacion(String ocupacion) throws SQLException {

        String sql_ocup = "select t.* from tplocupacion t "
                + " where t.plocu_estado = '1' "
                + " and t.plocu_id like '%" + ocupacion + "%' "
                + " order by t.plocu_id ";

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ocup);
            objOcupacion = null;
            while (rs.next()) {
                objOcupacion = new Ocupacion();
                objOcupacion.setOcup_id(rs.getString("plocu_id"));
                objOcupacion.setOcup_des(rs.getString("plocu_descri"));

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

        return objOcupacion;

    }

    //LOV UBIGEO PLANILLAS
    public ListModelList<UbigeoPla> busquedaLovUbigeoPla() throws SQLException {

        String sql_ubigeo = "select t.ubi_id,t.ubi_nomdep||' - '||t.ubi_nompro||' - '||t.ubi_nomdis ubi_des from codijisa.tubigeo t \n"
                + " where t.ubi_est = '1' "
                + " order by t.ubi_id ";

        objlstUbigeoPla = new ListModelList<UbigeoPla>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ubigeo);
            while (rs.next()) {
                objUbigeoPla = new UbigeoPla();
                objUbigeoPla.setUbi_id(rs.getString("ubi_id"));
                objUbigeoPla.setUbi_des(rs.getString("ubi_des"));
                objlstUbigeoPla.add(objUbigeoPla);

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

        return objlstUbigeoPla;

    }

    public ListModelList<UbigeoPla> busquedaLovUbigeoPla2(String consulta) throws SQLException {

        String sql_ubigeo = "select t.ubi_id,t.ubi_nomdep||'-'||t.ubi_nompro||'-'||t.ubi_nomdis ubi_des from codijisa.tubigeo t                                                                   \n"
                + " where t.ubi_est = '1' "
                + " and t.ubi_nomdep||'-'||t.ubi_nompro||'-'||t.ubi_nomdis like '%" + consulta + "%' "
                + " order by t.ubi_id ";

        objlstUbigeoPla = new ListModelList<UbigeoPla>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ubigeo);
            while (rs.next()) {
                objUbigeoPla = new UbigeoPla();
                objUbigeoPla.setUbi_id(rs.getString("ubi_id"));
                objUbigeoPla.setUbi_des(rs.getString("ubi_des"));
                objlstUbigeoPla.add(objUbigeoPla);

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

        return objlstUbigeoPla;

    }

    public UbigeoPla getLovUbigeoPla(String ubigeo) throws SQLException {

        String sql_ubigeo = "select t.ubi_id,t.ubi_nomdep||'-'||t.ubi_nompro||'-'||t.ubi_nomdis ubi_des from codijisa.tubigeo t                                                                   \n"
                + " where t.ubi_est = '1' "
                + " and t.ubi_id like '%" + ubigeo + "%' "
                + " order by t.ubi_id ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_ubigeo);
            objUbigeoPla = null;
            while (rs.next()) {
                objUbigeoPla = new UbigeoPla();
                objUbigeoPla.setUbi_id(rs.getString("ubi_id"));
                objUbigeoPla.setUbi_des(rs.getString("ubi_des"));

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

        return objUbigeoPla;

    }

    //LOV AREAS
    public ListModelList<ManAreas> busquedaLovAreas() throws SQLException {

        String sql_areas = "select tb.tabla_id,tb.tabla_descri,cc.plccosto,cc.cc_descri from tpltablas1 tb, tccostos cc "
                + " where tb.tabla_valor1 = cc.plccosto "
                + " and tb.tabla_cod = '00003' "
                + " and tb.tabla_id <> '000' "
                + " and tb.tabla_estado = '1' "
                + " order by tb.tabla_id";

        objlstAreas = new ListModelList<ManAreas>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_areas);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));
                objAreas.setArea_cod_costo(rs.getString("plccosto"));
                objAreas.setArea_des_costo(rs.getString("cc_descri"));
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

        String sql_areas = "select tb.tabla_id,tb.tabla_descri,cc.plccosto,cc.cc_descri from tpltablas1 tb, tccostos cc "
                + " where tb.tabla_valor1 = cc.plccosto"
                + " and tb.tabla_cod = '00003' "
                + " and tb.tabla_id <> '000' "
                + " and tb.tabla_estado = '1' "
                + " and tb.tabla_descri like '%" + consulta + "%' "
                + " order by tb.tabla_id";

        /*String sql_areas = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
         + " where r.tabla_cod = '00003' "
         + " and   r.tabla_id <> '000' "
         + " and   r.tabla_estado = '1' "
         + " and   r.tabla_descri like '%" + consulta + "%' "
         + " order by r.tabla_id";*/
        objlstAreas = new ListModelList<ManAreas>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_areas);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));
                objAreas.setArea_cod_costo(rs.getString("plccosto"));
                objAreas.setArea_des_costo(rs.getString("cc_descri"));
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

    //LOV CARGOS
    public ListModelList<ManCargos> busquedaLovCargos() throws SQLException {

        String sql_cargos = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00006' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " order by r.tabla_id";

        objlstCargos = new ListModelList<ManCargos>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_cargos);
            while (rs.next()) {
                objCargos = new ManCargos();
                objCargos.setCargo_id(rs.getString("tabla_id"));
                objCargos.setCargo_des(rs.getString("tabla_descri"));
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

    public ListModelList<ManCargos> busquedaLovCargos2(String consulta) throws SQLException {

        String sql_cargos = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00006' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstCargos = new ListModelList<ManCargos>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_cargos);
            while (rs.next()) {
                objCargos = new ManCargos();
                objCargos.setCargo_id(rs.getString("tabla_id"));
                objCargos.setCargo_des(rs.getString("tabla_descri"));
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

    public ManCargos getLovCargos(String cargos) throws SQLException {
        String sql_cargos = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00006' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + cargos + "%' "
                + " order by r.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_cargos);
            objCargos = null;
            while (rs.next()) {
                objCargos = new ManCargos();
                objCargos.setCargo_id(rs.getString("tabla_id"));
                objCargos.setCargo_des(rs.getString("tabla_descri"));

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

        return objCargos;
    }

    //LOV AFP
    public ListModelList<ManAfpsCre> busquedaLovAfp() throws SQLException {

        String sql_afp = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00005' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " order by r.tabla_id";

        objlstAfp = new ListModelList<ManAfpsCre>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_afp);
            while (rs.next()) {
                objAfp = new ManAfpsCre();
                objAfp.setAfp_id(rs.getString("TABLA_ID"));
                objAfp.setAfp_des(rs.getString("TABLA_DESCRI"));
                objlstAfp.add(objAfp);

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

        return objlstAfp;

    }

    public ListModelList<ManAfpsCre> busquedaLovAfp2(String consulta) throws SQLException {

        String sql_afp = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00005' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstCargos = new ListModelList<ManCargos>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_afp);
            while (rs.next()) {

                objAfp = new ManAfpsCre();
                objAfp.setAfp_id(rs.getString("TABLA_ID"));
                objAfp.setAfp_des(rs.getString("TABLA_DESCRI"));
                objlstAfp.add(objAfp);

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

        return objlstAfp;

    }

    public ManAfpsCre getLovAfp(String afp) throws SQLException {
        String sql_afp = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00005' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + afp + "%' "
                + " order by r.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_afp);
            objAfp = null;
            while (rs.next()) {
                objAfp = new ManAfpsCre();
                objAfp.setAfp_id(rs.getString("TABLA_ID"));
                objAfp.setAfp_des(rs.getString("TABLA_DESCRI"));

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

        return objAfp;

    }

    //LOV TIPO TRABAJADOR
    public ListModelList<TipTrabajador> busquedaLovTipTrab() throws SQLException {

        String sql_tiptrab = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00013' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " order by r.tabla_id";

        objlstTipTrabajador = new ListModelList<TipTrabajador>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tiptrab);
            while (rs.next()) {
                objTipTrabajador = new TipTrabajador();
                objTipTrabajador.setTiptrab_id(rs.getString("TABLA_ID"));
                objTipTrabajador.setTiptrab_des(rs.getString("TABLA_DESCRI"));
                objlstTipTrabajador.add(objTipTrabajador);

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

        return objlstTipTrabajador;

    }

    public ListModelList<TipTrabajador> busquedaLovTipTrab2(String consulta) throws SQLException {

        String sql_tiptrab = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00013' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstTipTrabajador = new ListModelList<TipTrabajador>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tiptrab);
            while (rs.next()) {
                objTipTrabajador = new TipTrabajador();
                objTipTrabajador.setTiptrab_id(rs.getString("TABLA_ID"));
                objTipTrabajador.setTiptrab_des(rs.getString("TABLA_DESCRI"));
                objlstTipTrabajador.add(objTipTrabajador);

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

        return objlstTipTrabajador;

    }

    public TipTrabajador getLovTipTrab(String tiptrab) throws SQLException {
        String sql_tiptrab = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00013' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + tiptrab + "%' "
                + " order by r.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_tiptrab);
            objTipTrabajador = null;
            while (rs.next()) {
                objTipTrabajador = new TipTrabajador();
                objTipTrabajador.setTiptrab_id(rs.getString("TABLA_ID"));
                objTipTrabajador.setTiptrab_des(rs.getString("TABLA_DESCRI"));

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

        return objTipTrabajador;

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

    //LOV TIPO CONTRATO 
    public ListModelList<Situacion> busquedaLovSituacion() throws SQLException {

        String sql_situ = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00016' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_tipo1 = '0'"
                + " order by r.tabla_id";

        objlstSituacion = new ListModelList<Situacion>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_situ);
            while (rs.next()) {
                objSituacion = new Situacion();
                objSituacion.setSitu_id(rs.getString("TABLA_ID"));
                objSituacion.setSitu_des(rs.getString("TABLA_DESCRI"));
                objlstSituacion.add(objSituacion);

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

        return objlstSituacion;

    }

    public ListModelList<Situacion> busquedaLovSituacion2(String consulta) throws SQLException {

        String sql_situ = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00016' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_tipo1 = '0'"
                + " and   r.tabla_descri like '%" + consulta + "%' "
                + " order by r.tabla_id";

        objlstSituacion = new ListModelList<Situacion>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_situ);
            while (rs.next()) {
                objSituacion = new Situacion();
                objSituacion.setSitu_id(rs.getString("TABLA_ID"));
                objSituacion.setSitu_des(rs.getString("TABLA_DESCRI"));
                objlstSituacion.add(objSituacion);

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

        return objlstSituacion;

    }

    public Situacion getLovSituacion(String situacion) throws SQLException {
        String sql_situ = "select r.tabla_id,r.tabla_descri from tpltablas1 r "
                + " where r.tabla_cod = '00016' "
                + " and   r.tabla_id <> '000' "
                + " and   r.tabla_estado = '1' "
                + " and   r.tabla_id like '%" + situacion + "%' "
                + " order by r.tabla_id";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_situ);
            objSituacion = null;
            while (rs.next()) {
                objSituacion = new Situacion();
                objSituacion.setSitu_id(rs.getString("TABLA_ID"));
                objSituacion.setSitu_des(rs.getString("TABLA_DESCRI"));

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

        return objSituacion;

    }

    //LOV ENTIDAD PRESTADORA DE SALUD
    public ListModelList<EntiSalud> busquedaLovEntiSalud() throws SQLException {

        String sql_enti = "select r.plps_id,r.plps_descri from tplpresser r "
                + " where r.plps_estado = '1' "
                + " order by r.plps_id ";

        objlstEntiSalud = new ListModelList<EntiSalud>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_enti);
            while (rs.next()) {
                objEntiSalud = new EntiSalud();
                objEntiSalud.setEntisalud_id(rs.getString("plps_id"));
                objEntiSalud.setEntisalud_des(rs.getString("plps_descri"));
                objlstEntiSalud.add(objEntiSalud);

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

        return objlstEntiSalud;

    }

    public ListModelList<EntiSalud> busquedaLovEntiSalud2(String consulta) throws SQLException {

        String sql_enti = "select r.plps_id,r.plps_descri from tplpresser r "
                + " where r.plps_estado = '1' "
                + " and r.plps_descri like '%" + consulta + "%'"
                + " order by r.plps_id ";

        objlstEntiSalud = new ListModelList<EntiSalud>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_enti);
            while (rs.next()) {
                objEntiSalud = new EntiSalud();
                objEntiSalud.setEntisalud_id(rs.getString("plps_id"));
                objEntiSalud.setEntisalud_des(rs.getString("plps_descri"));
                objlstEntiSalud.add(objEntiSalud);

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

        return objlstEntiSalud;

    }

    public EntiSalud getLovEntiSalud(String entidad) throws SQLException {
        String sql_enti = "select r.plps_id,r.plps_descri from tplpresser r "
                + " where r.plps_estado = '1' "
                + " and r.plps_id like '%" + entidad + "%'"
                + " order by r.plps_id ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_enti);
            objEntiSalud = null;
            while (rs.next()) {
                objEntiSalud = new EntiSalud();
                objEntiSalud.setEntisalud_id(rs.getString("plps_id"));
                objEntiSalud.setEntisalud_des(rs.getString("plps_descri"));

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

        return objEntiSalud;

    }

    /*
     //LOV BANCOS
     public ListModelList<Bancos> busquedaLovBancos() throws SQLException {

     String sql_bancos = "select b.ban_key, b.ban_des from tbancos b "
     + " where b.ban_estado = '1' "
     + " order by b.ban_key ";

     objlstBancos = new ListModelList<Bancos>();

     try {

     con = new ConectaBD().conectar();
     st = con.createStatement();
     rs = st.executeQuery(sql_bancos);
     while (rs.next()) {
     objBancos = new Bancos();
     objBancos.setKey(rs.getInt("ban_key"));
     objBancos.setDescripcion(rs.getString("ban_des"));
     objlstBancos.add(objBancos);

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

     return objlstBancos;

     }

     public ListModelList<Bancos> busquedaLovBancos2(String consulta) throws SQLException {

     String sql_bancos = "select b.ban_key,b.ban_des from tbancos b "
     + " where b.ban_estado = '1' "
     + " and b.ban_des like '%" + consulta + "%' "
     + " order by b.ban_key ";

     objlstBancos = new ListModelList<Bancos>();

     try {

     con = new ConectaBD().conectar();
     st = con.createStatement();
     rs = st.executeQuery(sql_bancos);
     while (rs.next()) {
     objBancos = new Bancos();
     objBancos.setKey(rs.getInt("ban_key"));
     objBancos.setDescripcion(rs.getString("ban_des"));
     objlstBancos.add(objBancos);

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

     return objlstBancos;

     }
     */
    //LOV BANCOS
    public ListModelList<Bancos> busquedaLovBancos() throws SQLException {

        String sql_bancos = "select b.ban_id,b.ban_des from tbancos b "
                + " where b.ban_estado = '1' "
                + " order by b.ban_id ";

        objlstBancos = new ListModelList<Bancos>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_bancos);
            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setId(rs.getString("ban_id"));
                objBancos.setDescripcion(rs.getString("ban_des"));
                objlstBancos.add(objBancos);

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

        return objlstBancos;

    }

    public ListModelList<Bancos> busquedaLovBancos2(String consulta) throws SQLException {

        String sql_bancos = "select b.ban_id,b.ban_des from tbancos b "
                + " where b.ban_estado = '1' "
                + " and b.ban_des like '%" + consulta + "%' "
                + " order by b.ban_id ";

        objlstBancos = new ListModelList<Bancos>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_bancos);
            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setId(rs.getString("ban_id"));
                objBancos.setDescripcion(rs.getString("ban_des"));
                objlstBancos.add(objBancos);

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

        return objlstBancos;

    }

    public Bancos getLovBancos(String bancos) throws SQLException {
        String sql_enti = "select b.ban_id,b.ban_des from tbancos b "
                + " where b.ban_estado = '1' "
                + " and b.ban_id like '%" + bancos + "%' "
                + " order by b.ban_id ";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_enti);
            objBancos = null;
            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setId(rs.getString("ban_id"));
                objBancos.setDescripcion(rs.getString("ban_des"));

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

        return objBancos;

    }

    //LOV HORARIOS
    public ListModelList<HorariosPla> busquedaLovHorarios() throws SQLException {

        String sql_horarios = " select h.emp_id,h.suc_id,h.horario_id,h.hor_descrip from tas_horario h "
                + " where h.emp_id = " + objUsuCredential.getCodemp() + " and h.suc_id = " + objUsuCredential.getCodsuc();

        objlstHorarios = new ListModelList<HorariosPla>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_horarios);
            while (rs.next()) {
                objHorarios = new HorariosPla();
                objUsuCredential.setCodemp(rs.getInt("emp_id"));
                objUsuCredential.setCodsuc(rs.getInt("suc_id"));
                objHorarios.setHorario_id(rs.getString("horario_id"));
                objHorarios.setHor_descrip(rs.getString("hor_descrip"));
                objlstHorarios.add(objHorarios);

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

        return objlstHorarios;

    }

    public ListModelList<HorariosPla> busquedaLovHorarios2(String consulta) throws SQLException {

        String sql_horarios = " select h.emp_id,h.suc_id,h.horario_id,h.hor_descrip from tas_horario h "
                + " where h.hor_descrip like '%" + consulta + "%' "
                + " and h.emp_id = " + objUsuCredential.getCodemp() + " and h.suc_id = " + objUsuCredential.getCodsuc();

        objlstHorarios = new ListModelList<HorariosPla>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_horarios);
            while (rs.next()) {
                objHorarios = new HorariosPla();
                objHorarios.setHorario_id(rs.getString("horario_id"));
                objHorarios.setHor_descrip(rs.getString("hor_descrip"));
                objlstHorarios.add(objHorarios);

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

        return objlstHorarios;

    }

    public HorariosPla getLovHorarios(String horarios) throws SQLException {
        String sql_horarios = "select h.emp_id,h.suc_id,h.horario_id,h.hor_descrip from tas_horario h "
                + " where h.horario_id like '%" + horarios + "%' and "
                + " h.emp_id = " + objUsuCredential.getCodemp() + " and h.suc_id = " + objUsuCredential.getCodsuc();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_horarios);
            objHorarios = null;
            while (rs.next()) {
                objHorarios = new HorariosPla();
                objHorarios.setHorario_id(rs.getString("horario_id"));
                objHorarios.setHor_descrip(rs.getString("hor_descrip"));

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

        return objHorarios;

    }

    //Lista sucursales
    public ListModelList<Sucursales> lstSucursales(int cod_emp) throws SQLException {
        String SQL_SUCURSALES = "select * from v_listasucursales t where t.emp_id='" + cod_emp + "'";
        objlstSucursales = new ListModelList<Sucursales>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUCURSALES);

            objSucursal = new Sucursales(0, "");
            objlstSucursales.add(objSucursal);
            while (rs.next()) {
                objSucursal = new Sucursales();
                objSucursal.setSuc_id(rs.getInt(3));
                objSucursal.setSuc_des(rs.getString(4));
                objlstSucursales.add(objSucursal);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstSucursales;
    }

    //para validar dni si ya existe
    public int buscarIgual(String documento) throws SQLException {
        int valor = 0;
        String sql = " select count(t.plnrodoc) as a "
                + " from tpldatoslab t where "
                + " t.plnrodoc=" + documento + ""
                + " and t.plestado_dl <> 0"
                + " and t.emp_id=" + objUsuCredential.getCodemp();
// "select count(t.plnrodoc) as a  from tpersonal t where t.plnrodoc=" + documento + "";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getInt("a");
            }
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //  LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return valor;

    }
    //para encontrar los campos del dni

    public ListModelList<Personal> consultaDni(String dni) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_consultaPersonalExcel(?,?)}";
        //objlstPersonal.clear();
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                //cst.setInt(1, objUsuCredential.getCodemp());
                cst.setString(1, dni);
                //cst.setString(12, orden);
                cst.registerOutParameter(2, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(2);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();

                while (rs.next()) {
                    objPersonal = new Personal();
                    objPersonal.setPlestado(rs.getInt("plestado"));
                    objPersonal.setPlapepat(rs.getString("plapepat"));
                    objPersonal.setPlapemat(rs.getString("plapemat"));
                    objPersonal.setPlnomemp(rs.getString("plnomemp"));
                    objPersonal.setPlnacion(rs.getString("plnacion"));
                    objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                    objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                    objPersonal.setPlfecnac(rs.getDate("fnacimiento"));
                    objPersonal.setPlsexo(rs.getInt("sexo"));
                    objPersonal.setPlestcivil(rs.getString("plestcivil"));
                    objPersonal.setPlgruposangui(rs.getString("plgruposangui"));
                    objPersonal.setPltelemp(rs.getString("pltelemp"));
                    objPersonal.setPltelemovil(rs.getString("pltelemovil"));
                    objPersonal.setPlemail(rs.getString("plemail"));
                    objPersonal.setPldiscap(rs.getInt("pldiscap"));
                    objPersonal.setPlnivedu(rs.getString("plnivedu"));
                    objPersonal.setPlidocup(rs.getString("plidocup"));
                    //DOMICILIO
                    objPersonal.setPlconddom(rs.getInt("plconddom"));
                    objPersonal.setPldir_ubigeo(rs.getString("pldir_ubigeo"));
                    objPersonal.setPldir_via(rs.getString("pldir_via"));
                    objPersonal.setPldir_nomvia(rs.getString("pldir_nomvia"));
                    objPersonal.setPldir_num(rs.getString("pldir_num"));
                    objPersonal.setPldir_int(rs.getString("pldir_int"));
                    objPersonal.setPldir_zona(rs.getString("pldir_zona"));
                    objPersonal.setPldir_nomzona(rs.getString("pldir_nomzona"));
                    objPersonal.setPldiremp(rs.getString("pldiremp"));
                    objPersonal.setPldir_refer(rs.getString("pldir_refer"));
                    objPersonal.setPldir_corx(rs.getDouble("pldir_corx"));
                    objPersonal.setPldir_cory(rs.getDouble("pldir_cory"));

                    //DATOS LABORALES
                   /* objPersonal.setSuc_id(rs.getInt("suc_id"));
                     objPersonal.setPlcodemp(rs.getString("plcodemp"));
                     objPersonal.setPlarea(rs.getString("plarea"));
                     objPersonal.setPlccosto(rs.getString("plccosto"));
                     objPersonal.setPlcatper(rs.getString("plcatper"));
                     objPersonal.setPltiptra(rs.getString("pltiptra"));
                     objPersonal.setPlhorari(rs.getString("plhorari"));
                     objPersonal.setPlidcargo(rs.getString("plidcargo"));
                     objPersonal.setPl_cc(rs.getInt("pl_cc"));
                     objPersonal.setPltipcont(rs.getString("pltipcont"));
                     objPersonal.setPlsujconinm(rs.getInt("plsujconinm"));
                     objPersonal.setPltipsue(rs.getInt("pltipsue"));
                     objPersonal.setPlperrem(rs.getInt("plperrem"));
                     objPersonal.setPlhextra(rs.getInt("plhextra"));
                     objPersonal.setPlputil(rs.getInt("plputil"));
                     objPersonal.setPlquinta(rs.getInt("plquinta"));
                     objPersonal.setPlotr5ta(rs.getInt("plotr5ta"));
                     objPersonal.setPlippsvi(rs.getInt("plippsvi"));
                     objPersonal.setPlcarssp(rs.getString("plcarssp"));
                     objPersonal.setPlsindic(rs.getInt("plsindic"));
                     objPersonal.setPlespens(rs.getInt("plespens"));
                     objPersonal.setPlfecing(rs.getDate("plfecing"));
                     objPersonal.setPlfecces(rs.getDate("plfecces"));
                     objPersonal.setPlcesemot(rs.getString("plcesemot"));
                     objPersonal.setPlcesedet(rs.getString("plcesedet"));
                     objPersonal.setPlceseobs(rs.getString("plceseobs"));
                     */
                    //DATOS APORTACION
                    objPersonal.setPlregpen(rs.getString("plregpen"));
                    objPersonal.setPlfiregpen(rs.getDate("fecha"));
                    objPersonal.setPltippen(rs.getString("pltippen"));
                    objPersonal.setPlcodafp(rs.getString("plcodafp"));
                    objPersonal.setPlcarafp(rs.getString("plcarafp"));
                    objPersonal.setPlcommix(rs.getInt("plcommix"));
                    objPersonal.setPlpressal(rs.getString("plpressal"));
                    objPersonal.setPlsiteps(rs.getString("plsiteps"));
                    objPersonal.setPlfbeps(rs.getDate("plfbeps"));
                    objPersonal.setPlsct_as(rs.getInt("plsct_as"));
                    objPersonal.setPlsct_pp(rs.getInt("plsct_pp"));

                    //DATOS PAGOS HABERES
                    objPersonal.setPlcorrel_dep_h(rs.getInt("plcorrel_dep_h"));
                    objPersonal.setPlbanco_h(rs.getInt("plbanco_h"));
                    objPersonal.setPltipcta_h(rs.getInt("i_pltipcta_h"));
                    //objPersonal.setPltipcta_h(rs.getString("pltipcta_h"));
                    objPersonal.setPlmoneda_h(rs.getInt("moneda"));
                    objPersonal.setPlnrocta_h(rs.getString("plnrocta_h"));
                    objPersonal.setPltippago_h(rs.getInt("pago"));
                    objPersonal.setPlglosa_h(rs.getString("plglosa_h"));

                    //DATOS PAGOS CTS
                    objPersonal.setPlcorrel_dep_c(rs.getInt("plcorrel_dep_c"));
                    objPersonal.setPlbanco_c(rs.getInt("plbanco_c"));
                    objPersonal.setPltipcta_c(rs.getInt("i_pltipcta_c"));
                    objPersonal.setPlmoneda_c(rs.getInt("moneda"));
                    objPersonal.setPlnrocta_c(rs.getString("plnrocta_c"));
                    objPersonal.setPltippago_c(rs.getInt("pago"));
                    objPersonal.setPlglosa_c(rs.getString("plglosa_c"));

                    //DESCRIPCIONES
                    objPersonal.setPlbanco_h_des(rs.getString("plbanco_h_des"));
                    objPersonal.setPlbanco_c_des(rs.getString("plbanco_c_des"));
                    objPersonal.setPlregpen_des(rs.getString("plregpen_des"));
                    objPersonal.setPlcodafp_des(rs.getString("plcodafp_des"));
                    objPersonal.setPlpressal_des(rs.getString("plpressal_des"));
                    objPersonal.setPlsiteps_des(rs.getString("plsiteps_des"));
                    objPersonal.setPlnacion_des(rs.getString("plnacion_des"));
                    objPersonal.setPlnivedu_des(rs.getString("plnivedu_des"));
                    objPersonal.setPlidocup_des(rs.getString("plidocup_des"));
                    objPersonal.setPldir_ubigeo_des(rs.getString("pldir_ubigeo_des"));
                    /*  objPersonal.setPlnacion_des(rs.getString("plnacion_des"));
                     objPersonal.setPlnivedu_des(rs.getString("plnivedu_des"));
                     objPersonal.setPlidocup_des(rs.getString("plidocup_des"));
                     objPersonal.setPldir_ubigeo_des(rs.getString("pldir_ubigeo_des"));
                     objPersonal.setPlarea_des(rs.getString("plarea_des"));
                     objPersonal.setPlccosto_des(rs.getString("plccosto_des"));
                     objPersonal.setPltiptra_des(rs.getString("pltiptra_des"));
                     objPersonal.setPlhorari_des(rs.getString("plhorari_des"));
                     objPersonal.setPlidcargo_des(rs.getString("plidcargo_des"));
                     objPersonal.setPltipcont_des(rs.getString("pltipcont_des"));
                     objPersonal.setPlregpen_des(rs.getString("plregpen_des"));
                     objPersonal.setPlcodafp_des(rs.getString("plcodafp_des"));
                     objPersonal.setPlpressal_des(rs.getString("plpressal_des"));
                     objPersonal.setPlsiteps_des(rs.getString("plsiteps_des"));
                     objPersonal.setPlbanco_h_des(rs.getString("plbanco_h_des"));
                     objPersonal.setPlbanco_c_des(rs.getString("plbanco_c_des"));
                     objPersonal.setSuc_id_des(rs.getString("suc_id_des"));

                     //AUDITORIA
                     objPersonal.setPlusuadd(rs.getString("plusuadd"));
                     objPersonal.setPlfecadd(rs.getTimestamp("plfecadd"));
                     objPersonal.setPlusumod(rs.getString("plusumod"));
                     objPersonal.setPlfecmod(rs.getTimestamp("plfecmod"));
                     */
                    objPersonal.setEmp_id(rs.getInt("emp_id"));
                    objPersonal.setSuc_id(rs.getInt("suc_id"));
                    objlstPersonal.add(objPersonal);

                }
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

        return objlstPersonal;
    }

    public ListModelList<Personal> listaDatosAp(int tipdoc, String nrodoc) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_listaDatosAp(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, tipdoc);
                cst.setString(2, nrodoc);
                cst.registerOutParameter(3, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(3);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();
                while (rs.next()) {
                    objPersonal = new Personal();

                    objPersonal.setPlfiregpen(rs.getDate("plfiregpen"));
                    objPersonal.setPlfbeps(rs.getDate("plfbeps"));
                    objPersonal.setPlregpen_des(rs.getString("plregpen_des") == null ? "" : rs.getString("plregpen_des"));
                    objPersonal.setPlcodafp_des(rs.getString("plcodafp_des") == null ? "" : rs.getString("plcodafp_des"));
                    objPersonal.setPlpressal_des(rs.getString("plpressal_des") == null ? "" : rs.getString("plpressal_des"));
                    objPersonal.setPlsiteps_des(rs.getString("plsiteps_des") == null ? "" : rs.getString("plsiteps_des"));
                    objPersonal.setPlusuadd(rs.getString("plusuadd"));

                    objlstPersonal.add(objPersonal);
                }
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaCompRetDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstPersonal;
    }

    //lov pago de haberes
    public ListModelList<Personal> listaDatosPHaberes(int tipdoc, String nrodoc) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_listaDatosPHaberes(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, tipdoc);
                cst.setString(2, nrodoc);
                cst.registerOutParameter(3, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(3);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();
                while (rs.next()) {
                    objPersonal = new Personal();

                    objPersonal.setPlcorrel_dep_h(rs.getInt("plcorrel_dep_h"));
                    objPersonal.setPlbanco_h(rs.getInt("plbanco_h"));
                    objPersonal.setTipo_cuenta(rs.getString("pltipcta_h"));
                    objPersonal.setMoneda(rs.getString("plmoneda_h"));
                    objPersonal.setPlnrocta_h(rs.getString("plnrocta_h"));
                    objPersonal.setTipo_pago(rs.getString("pltippago_h"));
                    objPersonal.setPlglosa_h(rs.getString("plglosa_h"));
                    objPersonal.setPlbanco_h_des(rs.getString("plbanco_h_des"));
                    objPersonal.setPlusuadd(rs.getString("plusuadd"));
                    objlstPersonal.add(objPersonal);
                }
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaCompRetDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstPersonal;
    }

    //lov pago de cts
    public ListModelList<Personal> listaDatosPCts(int tipdoc, String nrodoc) throws SQLException {

        String sql_query = "{call codijisa.pack_tpersonal.p_listaDatosPCts(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setInt(1, tipdoc);
                cst.setString(2, nrodoc);
                cst.registerOutParameter(3, OracleTypes.CURSOR); //REF CURSOR
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(3);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();
                while (rs.next()) {
                    objPersonal = new Personal();

                    objPersonal.setPlcorrel_dep_c(rs.getInt("plcorrel_dep_c"));
                    objPersonal.setPlbanco_c(rs.getInt("plbanco_c"));
                    objPersonal.setTipo_cuenta(rs.getString("pltipcta_c"));
                    objPersonal.setMoneda(rs.getString("plmoneda_c"));
                    objPersonal.setPlnrocta_c(rs.getString("plnrocta_c"));
                    //objPersonal.setPltippago_c(rs.getInt("pltippago_c"));
                    objPersonal.setPlglosa_c(rs.getString("plglosa_c"));
                    objPersonal.setPlbanco_c_des(rs.getString("plbanco_c_des"));
                    objPersonal.setPlusuadd(rs.getString("plusuadd"));
                    objlstPersonal.add(objPersonal);
                }
            }
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListaCompRetDet.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }
        return objlstPersonal;
    }

    //valida numero de digitos de cuneta de banco
    public int consultaDigitos(String cod_banco) throws SQLException {
        int valor = 0;
        String sql = "select t.ban_numeracion as ctaCant from tbancos t where t.ban_id =" + cod_banco + "";
// "select count(t.plnrodoc) as a  from tpersonal t where t.plnrodoc=" + documento + "";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getInt("ctaCant");
            }
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //  LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return valor;

    }

    //valida numero de digitos por documento de identificacion
    public int consultaDocumento(int documento) throws SQLException {
        int valor = 0;
        String sql = "select  t.tab_tip as numero from ttabgen t \n"
                + "WHERE t.tab_cod=27"
                + "and t.tab_est=1 "
                + "and t.tab_id =" + documento + ""
                + "order by t.tab_ord";
// "select count(t.plnrodoc) as a  from tpersonal t where t.plnrodoc=" + documento + "";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getInt("numero");
            }
            // LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            //  LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return valor;

    }

    /**
     * @return lista de areas para combobox
     * @throws java.sql.SQLException
     * @autor Junior Fernandez Metodo de combobox en movimiento linea
     * @version 11/08/2017
     */
    public ListModelList<ManAreas> lst_areas() throws SQLException {
        String area = " select r.tabla_id,r.tabla_descri from"
                + " (select t.tabla_id,t.tabla_descri"
                + " from tpltablas1 t where t.tabla_cod='00003'"
                + " and t.tabla_estado=1 and t.tabla_id <> '000' "
                + "  union"
                + "   select '999',''"
                + " from tpltablas1 t where t.tabla_cod='00003'"
                + " and t.tabla_estado=1 and t.tabla_id <> '000'"
                + " )r"
                + " order by r.tabla_id ";

        /*" select t.tabla_id,t.tabla_descri\n" +
         " from tpltablas1 t where t.tabla_cod='00003' \n" +
         " and t.tabla_estado=1";*/
        objlstAreas = new ListModelList<ManAreas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(area);
            while (rs.next()) {
                objAreas = new ManAreas();
                objAreas.setArea_id(rs.getString("tabla_id"));
                objAreas.setArea_des(rs.getString("tabla_descri"));
                objlstAreas.add(objAreas);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSucursales.getSize() + " registro(s)");
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
        return objlstAreas;
    }

//ver emopresa y sucursal
    public ListModelList<Personal> verEmpresa(String documento) throws SQLException {

        String query = " select e.emp_des,s.suc_des,t.plfecces fecha_cese,pack_tpersonal.ftb1_descripcion(t.plcesemot,'00021') plcesemot_des "
                + " from tempresas e , tsucursales s , tpldatoslab t"
                + " where e.emp_id = s.emp_id"
                + " and e.emp_id = t.emp_id"
                + " and s.emp_id = t.emp_id"
                + " and s.suc_id = t.suc_id"
                + " and e.emp_est = 1"
                + " and s.suc_est = 1"
                + " and t.plestado_dl = 1"
                + " and t.plnrodoc =" + documento;

        objlstPersonal = new ListModelList<Personal>();

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objPersonal = new Personal();
                objPersonal.setEmp_des(rs.getString("emp_des"));
                objPersonal.setSuc_id_des(rs.getString("suc_des"));
                objPersonal.setPlfecces(rs.getDate("fecha_cese"));
                objPersonal.setPlcesemot(rs.getString("plcesemot_des"));
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

    /**
     * ***********************************************
     * para patrones empleados ***********************************************
     */
    public ListModelList<Personal> consultaPatronPersonal(String empresa, String sucursal, String per_ingreso, String per_cesado, int seleccion, String busqueda, String cesados, int estado, String area, String costo) throws SQLException {

        String documento = "", paterno = "", materno = "", nombres = "", cargo = "";
        switch (seleccion) {

            case 1:
                documento = busqueda;

                break;
            case 2:
                paterno = busqueda;

                break;
            case 3:
                materno = busqueda;

                break;
            case 4:
                nombres = busqueda;

                break;
            case 5:
                cargo = busqueda;

                break;
        }

        String sql_query = "{call codijisa.pack_tpersonal.p_consultaPatronPersonal(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        //objlstPersonal.clear();
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                cst = con.prepareCall(sql_query);
                cst.setString(1, empresa);
                cst.setString(2, sucursal);
                cst.setString(3, per_ingreso);
                cst.setString(4, per_cesado);
                cst.setString(5, documento);
                cst.setString(6, paterno);
                cst.setString(7, materno);
                cst.setString(8, nombres);
                cst.setString(9, cargo);
                cst.setString(10, cesados);
                cst.setInt(11, estado);
                //cst.setString(12, orden);
                cst.registerOutParameter(12, OracleTypes.CURSOR); //REF CURSOR
                cst.setString(13, area);
                cst.setString(14, costo);
                cst.execute();
                rs = ((OracleCallableStatement) cst).getCursor(12);

                objlstPersonal = null;
                objlstPersonal = new ListModelList<Personal>();

                while (rs.next()) {
                    objPersonal = new Personal();
                    objPersonal.setPlestado(rs.getInt("plestado"));
                    objPersonal.setPlapepat(rs.getString("plapepat"));
                    objPersonal.setPlapemat(rs.getString("plapemat"));
                    objPersonal.setPlnomemp(rs.getString("plnomemp"));
                    objPersonal.setPlnacion(rs.getString("plnacion"));
                    objPersonal.setPltipdoc(rs.getInt("pltipdoc"));
                    objPersonal.setPlnrodoc(rs.getString("plnrodoc"));
                    objPersonal.setPlfecnac(rs.getDate("plfecnac"));
                    objPersonal.setPlsexo(rs.getInt("plsexo"));
                    objPersonal.setPlestcivil(rs.getString("plestcivil"));
                    objPersonal.setPlgruposangui(rs.getString("plgruposangui"));
                    objPersonal.setPltelemp(rs.getString("pltelemp"));
                    objPersonal.setPltelemovil(rs.getString("pltelemovil"));
                    objPersonal.setPlemail(rs.getString("plemail"));
                    objPersonal.setPldiscap(rs.getInt("pldiscap"));
                    objPersonal.setPlnivedu(rs.getString("plnivedu"));
                    objPersonal.setPlidocup(rs.getString("plidocup"));
                    //DOMICILIO
                    objPersonal.setPlconddom(rs.getInt("plconddom"));
                    objPersonal.setPldir_ubigeo(rs.getString("pldir_ubigeo"));
                    objPersonal.setPldir_via(rs.getString("pldir_via"));
                    objPersonal.setPldir_nomvia(rs.getString("pldir_nomvia"));
                    objPersonal.setPldir_num(rs.getString("pldir_num"));
                    objPersonal.setPldir_int(rs.getString("pldir_int"));
                    objPersonal.setPldir_zona(rs.getString("pldir_zona"));
                    objPersonal.setPldir_nomzona(rs.getString("pldir_nomzona"));
                    objPersonal.setPldiremp(rs.getString("pldiremp"));
                    objPersonal.setPldir_refer(rs.getString("pldir_refer"));
                    objPersonal.setPldir_corx(rs.getDouble("pldir_corx"));
                    objPersonal.setPldir_cory(rs.getDouble("pldir_cory"));

                    //DATOS LABORALES
                    objPersonal.setSuc_id(rs.getInt("suc_id"));
                    objPersonal.setPlcodemp(rs.getString("plcodemp"));
                    objPersonal.setPlarea(rs.getString("plarea"));
                    objPersonal.setPlccosto(rs.getString("plccosto"));
                    objPersonal.setPlcatper(rs.getString("plcatper"));
                    objPersonal.setPltiptra(rs.getString("pltiptra"));
                    objPersonal.setPlhorari(rs.getString("plhorari"));
                    objPersonal.setPlidcargo(rs.getString("plidcargo"));
                    objPersonal.setPl_cc(rs.getInt("pl_cc"));
                    objPersonal.setPltipcont(rs.getString("pltipcont"));
                    objPersonal.setPlsujconinm(rs.getInt("plsujconinm"));
                    objPersonal.setPltipsue(rs.getInt("pltipsue"));
                    objPersonal.setPlperrem(rs.getInt("plperrem"));
                    objPersonal.setEmp_id(rs.getInt("emp_id"));
                    objPersonal.setPlputil(rs.getInt("plputil"));
                    objPersonal.setPlquinta(rs.getInt("plquinta"));
                    objPersonal.setPlotr5ta(rs.getInt("plotr5ta"));
                    objPersonal.setPlippsvi(rs.getInt("plippsvi"));
                    objPersonal.setPlcarssp(rs.getString("plcarssp"));
                    objPersonal.setPlsindic(rs.getInt("plsindic"));
                    objPersonal.setPlespens(rs.getInt("plespens"));
                    objPersonal.setPlfecing(rs.getDate("plfecing"));
                    objPersonal.setPlfecces(rs.getDate("plfecces"));
                    objPersonal.setPlcesemot(rs.getString("plcesemot"));
                    objPersonal.setPlcesedet(rs.getString("plcesedet"));
                    objPersonal.setPlceseobs(rs.getString("plceseobs"));

                    //DATOS APORTACION
                    objPersonal.setPlregpen(rs.getString("plregpen"));
                    objPersonal.setPlfiregpen(rs.getDate("plfiregpen"));
                    objPersonal.setPltippen(rs.getString("pltippen"));
                    objPersonal.setPlcodafp(rs.getString("plcodafp"));
                    objPersonal.setPlcarafp(rs.getString("plcarafp"));
                    objPersonal.setPlcommix(rs.getInt("plcommix"));
                    objPersonal.setPlpressal(rs.getString("plpressal"));
                    objPersonal.setPlsiteps(rs.getString("plsiteps"));
                    objPersonal.setPlfbeps(rs.getDate("plfbeps"));
                    objPersonal.setPlsct_as(rs.getInt("plsct_as"));
                    objPersonal.setPlsct_pp(rs.getInt("plsct_pp"));

                    //DATOS PAGOS HABERES
                    objPersonal.setPlcorrel_dep_h(rs.getInt("plcorrel_dep_h"));
                    objPersonal.setPlbanco_h(rs.getInt("plbanco_h"));
                    objPersonal.setPltipcta_h(rs.getInt("pltipcta_h"));
                    objPersonal.setPlmoneda_h(rs.getInt("plmoneda_h"));
                    objPersonal.setPlnrocta_h(rs.getString("plnrocta_h"));
                    objPersonal.setPltippago_h(rs.getInt("pltippago_h"));
                    objPersonal.setPlglosa_h(rs.getString("plglosa_h"));

                    //DATOS PAGOS CTS
                    objPersonal.setPlcorrel_dep_c(rs.getInt("plcorrel_dep_c"));
                    objPersonal.setPlbanco_c(rs.getInt("plbanco_c"));
                    objPersonal.setPltipcta_c(rs.getInt("pltipcta_c"));
                    objPersonal.setPlmoneda_c(rs.getInt("plmoneda_c"));
                    objPersonal.setPlnrocta_c(rs.getString("plnrocta_c"));
                    objPersonal.setPltippago_c(rs.getInt("pltippago_c"));
                    objPersonal.setPlglosa_c(rs.getString("plglosa_c"));

                    //DESCRIPCIONES
                    objPersonal.setPlnacion_des(rs.getString("plnacion_des"));
                    objPersonal.setPlnivedu_des(rs.getString("plnivedu_des"));
                    objPersonal.setPlidocup_des(rs.getString("plidocup_des"));
                    objPersonal.setPldir_ubigeo_des(rs.getString("pldir_ubigeo_des"));
                    objPersonal.setPlarea_des(rs.getString("plarea_des"));
                    objPersonal.setPlccosto_des(rs.getString("plccosto_des"));
                    objPersonal.setPltiptra_des(rs.getString("pltiptra_des"));
                    objPersonal.setPlhorari_des(rs.getString("plhorari_des"));
                    objPersonal.setPlidcargo_des(rs.getString("plidcargo_des"));
                    objPersonal.setPltipcont_des(rs.getString("pltipcont_des"));
                    objPersonal.setPlregpen_des(rs.getString("plregpen_des"));
                    objPersonal.setPlcodafp_des(rs.getString("plcodafp_des"));
                    objPersonal.setPlpressal_des(rs.getString("plpressal_des"));
                    objPersonal.setPlsiteps_des(rs.getString("plsiteps_des"));
                    objPersonal.setPlbanco_h_des(rs.getString("plbanco_h_des"));
                    objPersonal.setPlbanco_c_des(rs.getString("plbanco_c_des"));
                    objPersonal.setSuc_id_des(rs.getString("suc_id_des"));

                    //AUDITORIA
                    objPersonal.setPlusuadd(rs.getString("plusuadd"));
                    objPersonal.setPlfecadd(rs.getTimestamp("plfecadd"));
                    objPersonal.setPlusumod(rs.getString("plusumod"));
                    objPersonal.setPlfecmod(rs.getTimestamp("plfecmod"));

                    objlstPersonal.add(objPersonal);

                }
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

        return objlstPersonal;
    }

    public String copiaMovimiento(String s_codemp, String s_periodo) throws SQLException {

        String SQL = "{call pack_tpersonal.p_datostlanillatrunca(?,?,?,?,?,?,?,?)}";
        int i_flag;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL);

            cst.clearParameters();

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, s_codemp);
            cst.setString(4, objUsuCredential.getCuenta());
            cst.setString(5, objUsuCredential.getComputerName());
            cst.setString(6, s_periodo + "03");
            cst.registerOutParameter(7, java.sql.Types.INTEGER);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);

            cst.execute();
            s_msg = cst.getString(8);
            i_flag = cst.getInt(7);
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

    public String eliminaMovimiento(String s_codemp, String s_periodo) throws SQLException {

        String SQL = "{call pack_tpersonal.p_borrarplanillatrunca(?,?,?,?,?,?)}";
        int i_flag;
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL);

            cst.clearParameters();

            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, s_codemp);
            cst.setString(4, s_periodo + "03");
            cst.registerOutParameter(5, java.sql.Types.INTEGER);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);

            cst.execute();
            s_msg = cst.getString(6);
            i_flag = cst.getInt(5);
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

    public ParametrosSalida generarAsistenciaAut(int empresa, int sucursal, String periodo, String personal) throws SQLException {

        String SQL_PROCEDURE = "{call codijisa.pack_tpersonal.p_generaAsistenciaAut(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_PROCEDURE);
            cst.clearParameters();
            cst.setInt(1, empresa);
            cst.setInt(2, sucursal);
            cst.setString(3, periodo.trim());
            cst.setString(4, personal);//1
            cst.setString(5, objUsuCredential.getCuenta());//2
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);//4
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);//5
            cst.execute();
            i_flagErrorBD = cst.getInt(6);//4
            s_msg = cst.getString(7);//5
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + codigo);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            //LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

}
