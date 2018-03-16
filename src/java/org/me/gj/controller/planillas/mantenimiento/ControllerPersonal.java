package org.me.gj.controller.planillas.mantenimiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import org.me.gj.controller.cxc.mantenimiento.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.planillas.mantenimiento.DatosLaborales;
import org.me.gj.model.planillas.mantenimiento.DerHabiente;
import org.me.gj.model.planillas.mantenimiento.EntiSalud;
import org.me.gj.model.planillas.mantenimiento.HorariosPla;
import org.me.gj.model.planillas.mantenimiento.ManAfpsCre;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
import org.me.gj.model.planillas.mantenimiento.ManCargos;
import org.me.gj.model.planillas.mantenimiento.Nacion;
import org.me.gj.model.planillas.mantenimiento.NivEducativo;
import org.me.gj.model.planillas.mantenimiento.Ocupacion;
import org.me.gj.model.planillas.mantenimiento.PersAportacion;
import org.me.gj.model.planillas.mantenimiento.PersPagoDep;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.mantenimiento.RegPensionario;
import org.me.gj.model.planillas.mantenimiento.Situacion;
import org.me.gj.model.planillas.mantenimiento.Tablas1;
import org.me.gj.model.planillas.mantenimiento.TipContrato;
import org.me.gj.model.planillas.mantenimiento.TipTrabajador;
import org.me.gj.model.planillas.mantenimiento.UbigeoPla;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerPersonal extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listapersonal, tab_datosper, tab_datoslab, tab_pagos, tab_adjuntos, tab_derhabientes;
    @Wire
    Listbox lst_personal, lst_derhab;
    @Wire
    Textbox txt_busqueda, txt_apePat, txt_apeMat, txt_nombres, txt_nacionId, txt_nacionDes, txt_nroDoc,
            txt_grupSang, txt_telefono, txt_movil, txt_correo, txt_nivEduId, txt_nivEduDes,
            txt_ocupaId, txt_ocupaDes, txt_ubigeoId, txt_ubigeoDes, txt_via, txt_nro, txt_int,
            txt_zona, txt_direccion, txt_referencia,//Datos personales
            txt_codInterno, txt_areaId, txt_areaDes, txt_ccostoId, txt_ccostoDes, txt_tipTrabId, txt_tipTrabDes,
            txt_horarioId, txt_horarioDes, txt_cargoId, txt_cargoDes, txt_tipContId, txt_tipContDes,
            txt_carnetSsp, txt_ceseDet, txt_ceseObs,//Datos laborales
            txt_regPenId, txt_regPenDes, txt_afpId, txt_afpDes, txt_carnetAfp, txt_presSaludId, txt_presSaludDes,
            txt_sitEpsId, txt_sitEpsDes,//Datos aportes
            txt_banDepHabId, txt_banDepHabDes, txt_nroCtaDepHab, txt_glosaDepHab,
            txt_banDepCtsId, txt_banDepCtsDes, txt_nroCtaDepCts, txt_glosaDepCts,//Datos Pagos
            txt_adj_croquis, txt_adj_dni, txt_adj_firma, txt_adj_foto, txt_adj_recibo, txt_adj_otros,
            txt_adj_antpol, txt_adj_cerdom, txt_adj_curvit,//adjuntos
            txt_dhApePat, txt_dhApeMat, txt_dhNombres, txt_dhNroDoc, txt_dhNroDocVf, txt_dhNrd,
            txt_dhIdMuni, txt_dhDesMuni, txt_dhNacion, txt_dhDesNacion, txt_dhZona, txt_dhNro,
            txt_dhInt, txt_dhRefer, txt_dhCodUbi, txt_dhDesUbi, txt_dhVia,//derhabientes
            txt_usuadd, txt_usumod, txt_areaId_Lov, txt_areaDes_Lov, txt_areaId1_Lov, txt_costo, txt_descripcioncosto, txt_costo1;
    @Wire
    Combobox cb_fsucursal, cb_fpering, cb_fperces, cb_busqueda,//busqueda
            cb_tipDoc, cb_sexo, cb_estCivil, cb_condicion, cb_via, cb_zona,//datos personales
            cb_sucursal, cb_categoria, cb_sitEsp, cb_tipSueldo, cb_perRem, cb_motivoCese,//datos laborales
            cb_tipPen, cb_sctrSalud, cb_sctrPension,//aportacion
            cb_tipCuentaDepHab, cb_tipMonedaDepHab, cb_tipPagoDepHab,
            cb_tipCuentaDepCts, cb_tipMonedaDepCts,//pagos
            cb_dhTipDoc, cb_dhSexo, cb_dhParentesto, cb_dhDocVf, cb_dhSituacion,
            cb_dhTipBaja, cb_dhZona, cb_dhVia, cb_estado;//derhabientes

    @Wire
    Datebox d_fecadd, d_fecmod,//Auditoría
            d_fecNac,
            d_fecIng, d_fecCese,
            d_fecIngRp, d_fecCesRp,
            d_fecBajaEs,
            d_dhFecNac, d_dhFecAlta, d_dhFecBaja;
    @Wire
    Intbox /*ib_hExtras,*/ ib_dhId,
            ib_correlph, ib_correlpc;
    @Wire
    Label lbl_nombre1, lbl_nombre2, lbl_nombre3, lbl_nombre4;
    @Wire
    Doublebox db_dirlatx, db_dirlony;

    URL imagen;
    @Wire
    Div img_foto;
    @Wire
    Radiogroup rbg_indicador;
    @Wire
    Checkbox chk_cesados, chk_activos,
            chk_estado, chk_discap, chk_sujCtrlInm, chk_utilidad, chk_excQuinta, chk_otrosQuinta, chk_essalud,
            chk_sindicalizado, chk_pensionista, chk_comMix,
            chk_dhViveDom,
            chk_dhEstado, chk_dhEst, chk_dhDiscap, chk_dmedicos;
    @Wire
    Button btn_coordenadasDomi, btn_movimiento, btn_descuento, btn_asistencia/*, btn_capture, btn_guardar*/;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevodl, tbbtn_btn_editardl, tbbtn_btn_deshacerdl, tbbtn_btn_historialdl, tbbtn_btn_cesedl,
            tbbtn_btn_nuevoap, tbbtn_btn_deshacerap, tbbtn_btn_historialap,
            tbbtn_btn_nuevoph, tbbtn_btn_editarph, tbbtn_btn_deshacerph, tbbtn_btn_historialph,
            tbbtn_btn_nuevopc, tbbtn_btn_editarpc, tbbtn_btn_deshacerpc, tbbtn_btn_historialpc,
            //
            tbbtn_adjcar_antpol, tbbtn_adjpdf_antpol, tbbtn_adjcar_cerdom, tbbtn_adjpdf_cerdom, tbbtn_adjcar_curvit, tbbtn_adjpdf_curvit,
            tbbtn_adjcar_croquis, tbbtn_adjpdf_croquis, tbbtn_adjcar_dni, tbbtn_adjpdf_dni, tbbtn_adjcar_firma, tbbtn_adjpdf_firma,
            tbbtn_adjcar_foto, /*tbbtn_adjpdf_foto,*/ tbbtn_adjcar_recibo, tbbtn_adjpdf_recibo, tbbtn_adjcar_otros, tbbtn_adjpdf_otros, tbbtn_adjcar_fotofile,
            //
            tbbtn_btn_nuevodh, tbbtn_btn_editardh, tbbtn_btn_deshacerdh, tbbtn_btn_eliminardh, tbbtn_btn_guardardh;

    @Wire
    Button btn_consultar;
    //Instancias de Objetos
    ListModelList<TipoDocumento> objlstTipoDocumento;
    ListModelList<Sucursales> objlstSucursal;
    ListModelList<Personal> objlstPersonal, objlstPersonalIgual;
    ListModelList<Tablas1> objlstEstadoCivil;
    ListModelList<Tablas1> objlstVia;
    ListModelList<Tablas1> objlstZona;
    ListModelList<Tablas1> objlstCategoria;
    ListModelList<Tablas1> objlstTipoPension;
    ListModelList<Tablas1> objlstMotivos;
    ListModelList<Tablas1> objlstParentesco;
    ListModelList<Tablas1> objlstTipoBajaDH;

    public static int val;
    DaoPersonal objDaoPersonal;
    DaoCliente objDaoCliente;
    DaoAccesos objDaoAccesos;
    DaoMovLinea objDaoMovLinea;

    Accesos objAccesos;
    Personal objPersonal;
    Personal objPersonalDni = new Personal();
    DatosLaborales objDatosLabBase, objDatosLabNuevo;
    PersAportacion objPersAportacionBase, objPersAportacionNuevo;
    PersPagoDep objPersPagoHabBase, objPersPagoHabNuevo;
    PersPagoDep objPersPagoCtsBase, objPersPagoCtsNuevo;

    //Utilitarios objUtil = new Utilitarios();
    ParametrosSalida objParamSalida;

    //Variables publicas
    String s_estado = "Q";
    String s_estado_dl = "Q";
    String s_estado_ap = "Q";
    String s_estado_ph = "Q";
    String s_estado_pc = "Q";
    String s_estado_dh = "Q";

    String modoEjecucion;
    String s_mensaje = "";
    String foco = "";
    String focoDerHab = "";
    String tab = "";
    String orden;

    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileAntPol = rutaFile + "ANTPOL\\";
    String rutaFileCerDom = rutaFile + "CERDOM\\";
    String rutaFileCurVit = rutaFile + "CURVIT\\";
    //
    String rutaFileCroquis = rutaFile + "CROQUIS\\";
    String rutaFileDni = rutaFile + "DNI\\";
    String rutaFileFirma = rutaFile + "FIRMA\\";
    String rutaFileFoto = rutaFile + "FOTO\\";
    String rutaFileRecibo = rutaFile + "RECIBO\\";
    String rutaFileOtros = rutaFile + "OTROS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";

    //String motivoCese, ceseDet, ceseObs;
    Map parametros;

    Media mediaAntPol, mediaCerDom, mediaCurVit, mediaCroquis,
            mediaDni, mediaFirma, mediaFoto, mediaRecibo, mediaOtros, mediaFotoFromFile;

    File archivoAntPol, archivoCerDom, archivoCurVit, archivoCroquis,
            archivoDni, archivoFirma, archivoFoto, archivoRecibo, archivoOtros, archivoFotoFromFile;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formato2 = new SimpleDateFormat("yyyyMM");
    int i_sel, emp_id, suc_id;
    //int flag = 3;
    public static boolean bandera = false;
    boolean cargaAntPol = false;
    boolean cargaCerDom = false;
    boolean cargaCurVit = false;
    boolean cargaCroquis = false;
    boolean cargaDni = false;
    boolean cargaFirma = false;
    boolean cargaFoto = false;
    boolean cargaFotoFromFile = false;
    boolean cargaRecibo = false;
    boolean cargaOtros = false;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCliente.class);

    //LOV AREAS
    ListModelList<ManAreas> objlstAreas;
    ManAreas objAreas;
    DaoManAreas objDaoAreas = new DaoManAreas();
    // LOV CENTRO COSTOS
    ListModelList<ManCCostos> objlstCCostos;
    ManCCostos objCCostos;

    //LOV REGIMEN PENSIONARIO
    ListModelList<ManAfpsCre> objlstAfp = new ListModelList<ManAfpsCre>();
    ManAfpsCre objAfp = new ManAfpsCre();
    DaoAfpsCre objDaoAfp = new DaoAfpsCre();
    ListModelList<RegPensionario> objlstRegPensionario;
    RegPensionario objRegPensionario;

    // DERECHO HABIENTES
    ListModelList<DerHabiente> objlstDerHabientes, objlstDerHabientesEli;
    DerHabiente objDerHabiente;

    // LOV NACION
    ListModelList<Nacion> objlstNacion;
    Nacion objNacion;

    // LOV NIVEL EDUCATIVO
    ListModelList<NivEducativo> objlstNivEdu;
    NivEducativo objNivEdu;

    // LOV OCUPACION
    ListModelList<Ocupacion> objlstOcupacion;
    Ocupacion objOcupacion;

    // LOV UBIGEO
    ListModelList<UbigeoPla> objlstUbigeoPla;
    UbigeoPla objUbigeoPla;

    // LOV CARGOS
    ListModelList<ManCargos> objlstCargos;
    ManCargos objCargos;

    //LOV TIPO TRABAJADOR
    ListModelList<TipTrabajador> objlstTipTrabajador;
    TipTrabajador objTipTrabajador;

    //LOV TIPO CONTRATO
    ListModelList<TipContrato> objlstTipContrato;
    TipContrato objTipContrato;

    //LOV SITUACION
    ListModelList<Situacion> objlstSituacion;
    Situacion objSituacion;

    //LOV ENTIDAD PRESTADORA SALUD
    ListModelList<EntiSalud> objlstEntiSalud;
    EntiSalud objEntiSalud;

    //LOV BANCOS
    ListModelList<Bancos> objlstBancos;
    Bancos objBancos;

    //LOV HORARIO
    ListModelList<HorariosPla> objlstHorariosPla;
    HorariosPla objHorarioPla;

    //  SimpleDateFormat sdfmes = new SimpleDateFormat("MM");
    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_personal")
    public void llenaRegistros() throws SQLException {
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        //carga listas
        objDaoPersonal = new DaoPersonal();
        objDaoAccesos = new DaoAccesos();
        objAccesos = new Accesos();
        objDaoCliente = new DaoCliente();
        objDerHabiente = new DerHabiente();
        objDaoMovLinea = new DaoMovLinea();
        objlstDerHabientesEli = new ListModelList<DerHabiente>();

        objDatosLabBase = new DatosLaborales();
        objPersAportacionBase = new PersAportacion();
        objPersPagoHabBase = new PersPagoDep();
        objPersPagoCtsBase = new PersPagoDep();

        objlstTipoDocumento = objDaoCliente.listaTipoDocumento();
        cb_tipDoc.setModel(objlstTipoDocumento);
        cb_dhTipDoc.setModel(objlstTipoDocumento);

        objlstEstadoCivil = objDaoPersonal.listaTablas1("00010");
        cb_estCivil.setModel(objlstEstadoCivil);

        objlstVia = objDaoPersonal.listaTablas1("00017");
        cb_via.setModel(objlstVia);
        cb_dhVia.setModel(objlstVia);
        objlstZona = objDaoPersonal.listaTablas1("00018");
        cb_zona.setModel(objlstZona);
        cb_dhZona.setModel(objlstZona);
        objlstCategoria = objDaoPersonal.listaTablas1("00007");
        cb_categoria.setModel(objlstCategoria);
        objlstTipoPension = objDaoPersonal.listaTablas1("00008");
        cb_tipPen.setModel(objlstTipoPension);
        objlstMotivos = objDaoPersonal.listaTablas1("00021");
        cb_motivoCese.setModel(objlstMotivos);

        objlstParentesco = objDaoPersonal.listaTablas1("00020");
        cb_dhParentesto.setModel(objlstParentesco);
        objlstTipoBajaDH = objDaoPersonal.listaTablas1("00022");
        cb_dhTipBaja.setModel(objlstTipoBajaDH);

        objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        // objlstSucursal = objDaoPersonal.lstSucursales(emp_id); //se comento

        cb_sucursal.setModel(objlstSucursal);
        cb_fsucursal.setModel(objlstSucursal);
        //se comento por periodo dinamico que sale un campo en blanco
       /* cb_fpering.setModel(new DaoManPer().listaPeriodos(0));
         cb_fperces.setModel(new DaoManPer().listaPeriodos(0));*/
        cb_fpering.setModel(new DaoManPer().listaPeriodosDinamico(0));
        cb_fperces.setModel(new DaoManPer().listaPeriodosDinamico(0));
        //carga clientes
        objlstPersonal = new ListModelList<Personal>();

        objlstPersonal = objDaoPersonal.consultaPersonal("", "", "", 0, "", "no", 1, "", "");
        lst_personal.setModel(objlstPersonal);

        objlstDerHabientes = null;
        lst_derhab.setModel(objlstDerHabientes);
        /*txt_clifecnac.setDisabled(true);
         cb_estado.setSelectedIndex(0);
         cb_busqueda.setSelectedIndex(0);
         txt_busqueda.setValue("%%");
         txt_busqueda.setDisabled(true);*/
        limpiaCargaAdjunto();
        habilitaBotonesPdf(false);
        bandera = false;
        cb_estado.setSelectedIndex(0);

        //valida dni 8 digitos
        // validaDocumento();
    }

    @Listen("onClick=#btn_movimiento")
    public void btn_movimiento() throws Exception {

        Executions.sendRedirect("/org/me/gj/view/planillas/procesos/MovLinea.zul");
        val = 1;
    }

    @Listen("onClick=#tab_datoslab")
    public void label1() {
        lbl_nombre1.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre2.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre3.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre4.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
    }

    @Listen("onClick=#tab_pagos")
    public void label2() {
        lbl_nombre1.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre2.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre3.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre4.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());

    }

    @Listen("onClick=#tab_adjuntos")
    public void label3() {
        lbl_nombre1.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre2.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre3.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre4.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());

    }

    @Listen("onClick=#tab_derhabientes")
    public void label4() {
        lbl_nombre1.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre2.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre3.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());
        lbl_nombre4.setValue(txt_apePat.getValue().toString() + " " + txt_apeMat.getValue().toString() + " " + txt_nombres.getValue().toString());

    }

    @Listen("onClick=#btn_descuento")
    public void btn_descuento() {
        Executions.sendRedirect("/org/me/gj/view/planillas/procesos/Descuentos.zul");
        val = 1;

    }

    /* @Listen("onClick=#btn_asistencia")
     public void btn_asistencia() {
     Executions.sendRedirect("/org/me/gj/view/planillas/procesos/AsisGeneral.zul");
     }*/
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90102000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Personal con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Personal con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Personal");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Personal");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Personal");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Personal");
        }
        /*  if (objAccesos.getAcc_eli() == 1) {
         tbbtn_btn_eliminar.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Cliente");
         } else {
         tbbtn_btn_eliminar.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Cliente");
         }*/
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Personal");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Personal");
        }

    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {

        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int est;
        objlstPersonal = new ListModelList<Personal>();

        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }

        //objlstPersonal = objDaoCanales.busquedaCanales(i_seleccion, s_consulta, i_estado);
        //
        //|| cb_fsucursal.getSelectedIndex() == 0
        String sucursal = cb_fsucursal.getSelectedIndex() == -1 || cb_fsucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
        //Messagebox.show(sucursal);

        //String junior = cb_fpering.getValue();
        String per_ing = cb_fpering.getSelectedIndex() == -1 || cb_fpering.getSelectedIndex() == 0 ? "" : cb_fpering.getSelectedItem().getValue().toString();
        String per_ces = cb_fperces.getSelectedIndex() == -1 || cb_fperces.getSelectedIndex() == 0 ? "" : cb_fperces.getSelectedItem().getValue().toString();
        String cesados = chk_cesados.isChecked() ? "si" : "no";
        // int estado = chk_activos.isChecked() ? 1 : 2;
        String area = txt_areaId1_Lov.getValue();

        String s_aux = area.replace("'", "");
        s_aux = area.replace(")", "");
        String costo = txt_costo1.getValue();
       // String s_auxCosto = costo.replace("'", "");
        // s_auxCosto = costo.replace(")", "");
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }
        objlstPersonal = objDaoPersonal.consultaPersonal(sucursal, per_ing, per_ces, i_seleccion, s_consulta, cesados, est, s_aux, costo);
        //public ListModelList<Personal> consultaPersonal(String per_ingreso, String per_cesado, int seleccion, String busqueda) throws SQLException {
        if (rbg_indicador.getSelectedIndex() == 0) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlapepat().compareTo(o2.getPlapepat());

                }
            });

        } else if (rbg_indicador.getSelectedIndex() == 1) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlccosto().compareTo(o2.getPlccosto());

                }
            });
        } else if (rbg_indicador.getSelectedIndex() == 2) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlcodafp() == null ? 000 : o1.getPlcodafp().compareTo(o2.getPlcodafp() == null ? "000" : o2.getPlcodafp());

                }
            });
        } else if (rbg_indicador.getSelectedIndex() == 3) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlfecing().compareTo(o2.getPlfecing());

                }
            });
        } else if (rbg_indicador.getSelectedIndex() == 4) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlfecnac().compareTo(o2.getPlfecnac());

                }
            });
        } else if (rbg_indicador.getSelectedIndex() == 5) {
            Collections.sort(objlstPersonal, new Comparator<Personal>() {
                public int compare(Personal o1, Personal o2) {
                    //return o1.getPlfecces().compareTo(o2.getPlfecces());                  
                    return o1.getPlfecces() == null ? 14 / 05 / 1091 : o1.getPlfecces().compareTo((Date) (o2.getPlfecces() == null ? 14 / 05 / 1091 : o2.getPlfecces()));
                }
            });
        } else {

            Collections.sort(objlstPersonal, new Comparator<Personal>() {

                public int compare(Personal o1, Personal o2) {
                    return o1.getPlnrodoc().compareTo(o2.getPlnrodoc());

                }
            });
        }

        lst_personal.setModel(objlstPersonal);
        rbg_indicador.setSelectedIndex(-1);
        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#lst_personal")
    public void seleccionaRegistro() throws SQLException {
        limpiarCampos();
        limpiaAuditoria();
        limpiarCamposDatosLab();
        limpiarCamposAportacion();
        limpiarCamposPagosHab();
        limpiarCamposPagosCts();
        lst_derhab.getItems().clear();
        limpiarCamposDerHabiente();
        objPersonal = new Personal();
        objPersonal = (Personal) lst_personal.getSelectedItem().getValue();
        if (objPersonal == null) {
            objPersonal = objlstPersonal.get(i_sel + 1);
        }

        i_sel = lst_personal.getSelectedIndex();
        try {
            llenarCampos(objPersonal);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ControllerPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
        llenarListaDerHabiente();
    }

    @Listen("onSelect=#lst_derhab")
    public void seleccionaRegistroDerHabiente() throws SQLException {
        objDerHabiente = lst_derhab.getSelectedItem().getValue();
        limpiarCamposDerHabiente();
        llenarCamposDerHabiente();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        s_estado_dl = "N";
        s_estado_ap = "N";
        objPersonal = new Personal();
        objDatosLabBase = new DatosLaborales();
        objPersAportacionBase = new PersAportacion();
        objPersPagoHabBase = new PersPagoDep();
        objPersPagoCtsBase = new PersPagoDep();

        habilitaTab(true, false);
        seleccionaTab(false, true);

        limpiarCampos();
        limpiarCamposDatosLab();
        limpiarCamposAportacion();
        limpiarCamposPagosHab();
        limpiarCamposPagosCts();
        limpiarCamposDerHabiente();

        habilitaBotones(true, false);
        habilitaBotonesDatosLab(true, false);
        tbbtn_btn_deshacerdl.setDisabled(true);
        habilitaBotonesAportacion(true, false);
        //tbbtn_btn_deshacerap.setDisabled(true);
        habilitaBotonesPagosHab(false, true);
        habilitaBotonesPagosCts(false, true);
        habilitaBotonesDerHabiente(false, true);

        habilitaBotonesAdjunto(false);
        habilitaBotonesPdf(true);

        habilitaCampos(false);
        habilitaCamposDatosLab(false);
        d_fecCese.setDisabled(true);
        habilitaCamposAportacion(false);

        objDerHabiente = null;
        objlstDerHabientes = null;
        objlstDerHabientes = new ListModelList<DerHabiente>();
        lst_derhab.setModel(objlstDerHabientes);

        //DATOS POR DEFECTO
        chk_estado.setChecked(true);
        chk_estado.setDisabled(true);
        d_fecIng.setValue(new Date());
        d_fecIngRp.setValue(new Date());

        txt_nacionId.setValue("589");
        cb_tipDoc.setSelectedIndex(0);
        cb_categoria.setSelectedIndex(0);
        txt_tipTrabId.setValue("21");
        cb_sitEsp.setSelectedIndex(0);
        cb_perRem.setSelectedIndex(0);
        cb_sctrSalud.setSelectedIndex(0);
        cb_sctrPension.setSelectedIndex(0);
        txt_sitEpsId.setValue("11");

        txt_apePat.focus();

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_personal.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            habilitaBotonesDatosLab(false, true);
            habilitaBotonesAportacion(false, true);
            habilitaBotonesPagosHab(false, true);
            habilitaBotonesPagosCts(false, true);
            habilitaBotonesDerHabiente(false, true);
            habilitaBotonesAdjunto(false);
            habilitaBotonesPdf(false);
            cb_tipDoc.setDisabled(true);
            txt_nroDoc.setDisabled(true);
            objlstDerHabientesEli = null;
            objlstDerHabientesEli = new ListModelList<DerHabiente>();

        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_personal.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Está seguro que desea eliminar el personal", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParam = objDaoPersonal.eliminarPersonal(objPersonal);
                                if (objParam.getFlagIndicador() == 0) {
                                    limpiarCampos();
                                    limpiaAuditoria();

                                    objlstPersonal = objDaoPersonal.consultaPersonal("", "", "", 0, "", "no", 1, "", "");
                                    lst_personal.setModel(objlstPersonal);
                                    VerificarTransacciones();
                                }
                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }

                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        validaFoco();
                    }
                }
            });
        } else {
            if (!s_estado.equals("M")) {
                String s_valida2 = validaFechaIngreso();
                if (!s_valida2.isEmpty()) {
                    Messagebox.show(s_valida2, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                d_fecIng.focus();
                            }
                        }
                    });
                }
            } else {
                String s_valida3 = validaFechaCese();
                if (!s_valida3.isEmpty()) {
                    Messagebox.show(s_valida3, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                d_fecCese.focus();
                            }
                        }
                    });
                } else {
                    String s_valida4 = validaNumCtaBan();
                    if (!s_valida4.isEmpty()) {
                        Messagebox.show(s_valida4, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    if (foco.equals("nroctadh")) {
                                        txt_nroCtaDepHab.focus();
                                    } else if (foco.equals("nroctadc")) {
                                        txt_nroCtaDepCts.focus();
                                    }
                                }
                            }
                        });
                    } else {
                        if (s_estado_dh.equals("M")) {
                            Messagebox.show("La lista derechohabiente se encuentra en edición", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        } else {
                            s_mensaje = "Está seguro que desea guardar los cambios?";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                                objPersonal = generaDatosPer();
                                                objDatosLabNuevo = generaDatosLab();
                                                objPersAportacionNuevo = generaAportacion();
                                                objPersPagoHabNuevo = generaPagoHaberes();
                                                objPersPagoCtsNuevo = generaPagoCts();

                                                objlstDerHabientes.addAll(objlstDerHabientesEli);

                                                ParametrosSalida objParam;
                                                if (s_estado.equals("N")) {
                                                    objParam = objDaoPersonal.insertarPersonal(objPersonal, objDatosLabNuevo, objPersAportacionNuevo,
                                                            objPersPagoHabNuevo, objPersPagoCtsNuevo, getDerHabiente(objlstDerHabientes));

                                                } else {
                                                    objParam = objDaoPersonal.actualizarPersonal(objPersonal, objDatosLabNuevo, objPersAportacionNuevo,
                                                            objPersPagoHabNuevo, objPersPagoCtsNuevo, getDerHabiente(objlstDerHabientes));

                                                }
                                                String s_ms1 = "";
                                                if (objParam.getFlagIndicador() == 0) {
                                                    //validacion de guardar/nuevo
                                                    if (cargaFoto == true) {
                                                        //Files.copy(archivoFoto, mediaFoto.getStreamData());
                                                        guardar_foto();
                                                    }
                                                    if (cargaFotoFromFile == true) {
                                                        Files.copy(archivoFotoFromFile, mediaFotoFromFile.getStreamData());
                                                        //guardar_foto();
                                                    }
                                                    //DOCUMENTOS
                                                    if (cargaAntPol == true) {
                                                        Files.copy(archivoAntPol, mediaAntPol.getStreamData());
                                                    }
                                                    if (cargaCerDom == true) {
                                                        Files.copy(archivoCerDom, mediaCerDom.getStreamData());
                                                    }
                                                    if (cargaCurVit == true) {
                                                        Files.copy(archivoCurVit, mediaCurVit.getStreamData());
                                                    }

                                                    if (cargaCroquis == true) {
                                                        Files.copy(archivoCroquis, mediaCroquis.getStreamData());
                                                    }
                                                    if (cargaDni == true) {
                                                        Files.copy(archivoDni, mediaDni.getStreamData());
                                                    }
                                                    if (cargaFirma == true) {
                                                        Files.copy(archivoFirma, mediaFirma.getStreamData());
                                                    }

                                                    if (cargaRecibo == true) {
                                                        Files.copy(archivoRecibo, mediaRecibo.getStreamData());
                                                    }
                                                    if (cargaOtros == true) {
                                                        Files.copy(archivoOtros, mediaOtros.getStreamData());
                                                    }
                                                    // AQUI!!!!!
                                                    try {
                                                        if (objDatosLabNuevo.getPlfecces() != null) {
                                                            s_ms1 = objDaoPersonal.copiaMovimiento(objPersonal.getPltipdoc() + objPersonal.getPlnrodoc(), formato2.format(objDatosLabNuevo.getPlfecces()));

                                                        } else {
                                                            s_ms1 = objDaoPersonal.eliminaMovimiento(objPersonal.getPltipdoc() + objPersonal.getPlnrodoc(), Utilitarios.periodoActual());

                                                        }
                                                    } catch (Exception ex) {
                                                        String err = ex.toString() + "*--" + ex.getMessage();
                                                    }

                                                    habilitaBotones(false, true);
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    habilitaCampos(true);
                                                    limpiarCampos();
                                                    habilitaCamposDatosLab(true);
                                                    habilitaCamposAportacion(true);
                                                    habilitaCamposPagosHab(true);
                                                    habilitaCamposPagosCts(true);
                                                    habilitaBotonesPersonal(true);
                                                    habilitaBotonesAdjunto(true);
                                                    habilitaBotonesPdf(false);
                                                    habilitaBotonesPagosHab(false, true);
                                                    tbbtn_btn_nuevoph.setDisabled(true);
                                                    // tbbtn_btn_editarph.setDisabled(true);
                                                    habilitaBotonesPagosCts(false, true);
                                                    tbbtn_btn_nuevopc.setDisabled(true);
                                                    tbbtn_btn_editarpc.setDisabled(true);
                                                    habilitaBotonesDerHabiente(false, true);
                                                    tbbtn_btn_nuevodh.setDisabled(true);
                                                    tbbtn_btn_editardh.setDisabled(true);
                                                    tbbtn_btn_eliminardh.setDisabled(true);
                                                    limpiarCamposDatosLab();
                                                    limpiarCamposAportacion();
                                                    limpiarCamposPagosHab();
                                                    limpiarCamposPagosCts();
                                                    limpiarCamposDerHabiente();
                                                    limpiaAuditoria();
                                                    limpiaCargaAdjunto();
                                                    limpiarListaDerhabiente();
                                                    objlstPersonal = new ListModelList<Personal>();
                                                    objlstPersonal = objDaoPersonal.consultaPersonal("", "", "", 0, "", "no", 1, "", "");
                                                    lst_personal.setModel(objlstPersonal);
                                                    //objlstPersonal.clear();
                                                    objlstDerHabientes.clear();
                                                    objlstDerHabientesEli.clear();
                                                    tbbtn_btn_guardardh.setDisabled(true);
                                                    tbbtn_btn_deshacerdh.setDisabled(true);
                                                    s_estado = "Q";
                                                    s_estado_dl = "Q";
                                                    s_estado_ap = "Q";
                                                    s_estado_pc = "Q";
                                                    s_estado_ph = "Q";
                                                    s_estado_dh = "Q";
                                                    VerificarTransacciones();
                                                }
                                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }

    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        Messagebox.show("Está seguro que desea deshacer los cambios", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //habilitaTab(false, true);
                            tab_listapersonal.setDisabled(false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            habilitaCamposDatosLab(true);
                            habilitaCamposAportacion(true);
                            habilitaCamposPagosHab(true);
                            habilitaCamposPagosCts(true);
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarCamposDerHabiente();
                            habilitaBotonesPersonal(true);
                            habilitaBotonesAdjunto(true);
                            habilitaBotonesPagosHab(false, true);
                            tbbtn_btn_nuevoph.setDisabled(true);
                            //tbbtn_btn_editarph.setDisabled(true);
                            habilitaBotonesPagosCts(false, true);
                            tbbtn_btn_nuevopc.setDisabled(true);
                            tbbtn_btn_editarpc.setDisabled(true);
                            habilitaBotonesDerHabiente(false, true);
                            tbbtn_btn_nuevodh.setDisabled(true);
                            tbbtn_btn_editardh.setDisabled(true);
                            tbbtn_btn_eliminardh.setDisabled(true);
                            habilitaBotonesPdf(false);
                            /*tbbtn_btn_deshacerdh.setDisabled(true);
                             tbbtn_btn_guardardh.setDisabled(true);*/
                            limpiaCargaAdjunto();
                            //lst_personal.clearSelection();
                            lst_personal.setSelectedIndex(-1);
                            s_estado = "Q";
                            VerificarTransacciones();
                            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //DATOS LABORALES
    @Listen("onClick=#tbbtn_btn_nuevodl")
    public void botonNuevoDatosLab() {
        s_estado_dl = "N";
        objDatosLabNuevo = new DatosLaborales();

        //limpiarCamposDatosLab();
        habilitaBotonesDatosLab(true, false);
        //habilitaTab(true, false);
        //seleccionaTab(false, true);
        habilitaCamposDatosLab(false);

        d_fecCese.setDisabled(true);
        d_fecCese.setValue(null);
        cb_motivoCese.setSelectedIndex(-1);
        txt_ceseDet.setValue("");
        txt_ceseObs.setValue("");

    }

    @Listen("onClick=#tbbtn_btn_editardl")
    public void botonEditarDatosLab() throws SQLException {
        s_estado_dl = "M";
        habilitaBotonesDatosLab(true, false);
        habilitaCamposDatosLabEditar(false);

    }

    @Listen("onClick=#tbbtn_btn_deshacerdl")
    public void botonDeshacerDatosLab() {
        Messagebox.show("Está seguro que desea deshacer los cambios", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //habilitaTab(false, true);
                            //tab_listapersonal.setDisabled(false);
                            //seleccionaTab(true, false);
                            habilitaBotonesDatosLab(false, true);
                            habilitaCamposDatosLab(true);

                            limpiarCamposDatosLab();
                            llenarCamposDatosLab(objDatosLabBase);
                            s_estado_dl = "Q";
                            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //historico de datos de trabajador
    @Listen("onClick=#tbbtn_btn_historialdl")
    public void botonHistorialDatosLab() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("tipdoc", cb_tipDoc.getSelectedItem().getValue().toString());
            objMapObjetos.put("numdoc", txt_nroDoc.getValue());
            objMapObjetos.put("personal", txt_apePat.getValue() + " " + txt_apeMat.getValue() + " " + txt_nombres.getValue());
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHistorialDatosLab.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    //historico de aportaciones
    @Listen("onClick=#tbbtn_btn_historialap")
    public void botonHistorialDatosAp() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("tipdoc", cb_tipDoc.getSelectedItem().getValue().toString());
            objMapObjetos.put("numdoc", txt_nroDoc.getValue());
            objMapObjetos.put("personal", txt_apePat.getValue() + " " + txt_apeMat.getValue() + " " + txt_nombres.getValue());
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHistorialDatosAportaciones.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    //pagos de haberes
    @Listen("onClick=#tbbtn_btn_historialph")
    public void botonHistorialDatosPagosHaberes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("tipdoc", cb_tipDoc.getSelectedItem().getValue().toString());
            objMapObjetos.put("numdoc", txt_nroDoc.getValue());
            objMapObjetos.put("personal", txt_apePat.getValue() + " " + txt_apeMat.getValue() + " " + txt_nombres.getValue());
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHistorialDatosPHaberes.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    //pagos de cts
    @Listen("onClick=#tbbtn_btn_historialpc")
    public void botonHistorialDatosPagosCts() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("tipdoc", cb_tipDoc.getSelectedItem().getValue().toString());
            objMapObjetos.put("numdoc", txt_nroDoc.getValue());
            objMapObjetos.put("personal", txt_apePat.getValue() + " " + txt_apeMat.getValue() + " " + txt_nombres.getValue());
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHistorialDatosCts.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    @Listen("onClick=#tbbtn_btn_cesedl")
    public void botonEditarDatosLabCese() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("s_estado_dl", s_estado_dl);
            objMapObjetos.put("cb_motivoCese", cb_motivoCese);
            objMapObjetos.put("txt_ceseDet", txt_ceseDet);
            objMapObjetos.put("txt_ceseObs", txt_ceseObs);
            objMapObjetos.put("controlador", "ControllerPersonal");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoCese.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    /**
     * Metodo de filtro de personal por areas
     */
    @Listen("onOK=#txt_areaId_Lov")
    public void lovAreas() {
        if (bandera == false) {
            bandera = true;
            Map objMap = new HashMap();
            objMap.put("id_area", txt_areaId_Lov);
            objMap.put("des_area", txt_areaDes_Lov);
            objMap.put("codarea1", txt_areaId1_Lov);
            objMap.put("controlador", "ControllerPersonal");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAreas.zul", null, objMap);
            window.doModal();
        }
    }

    //Salir de lov para el filtro
    @Listen("onBlur=#txt_areaId_Lov")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_areaId_Lov.getValue().isEmpty()) {
            if (!txt_areaId_Lov.getValue().equals("ALL")) {
                String id = txt_areaId_Lov.getValue();
                objAreas = objDaoMovLinea.consultaArea(id);
                if (objAreas == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_areaId_Lov.setValue(null);
                            txt_areaId_Lov.focus();
                            txt_areaDes_Lov.setValue("");

                        }
                    });

                } else {
                    txt_areaId_Lov.setValue(objAreas.getArea_id());
                    txt_areaDes_Lov.setValue(objAreas.getArea_des());
                    //  habilitaBotonesDetalle(false);
                    txt_areaId1_Lov.setValue(objAreas.getArea_id() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_areaDes_Lov.setValue("");
            txt_areaId1_Lov.setValue("");
        }

        bandera = false;
    }

    //DATOS APORTACION
    @Listen("onClick=#tbbtn_btn_nuevoap")
    public void botonNuevoAportacion() {
        s_estado_ap = "N";
        objPersAportacionNuevo = new PersAportacion();

        //limpiarCamposDatosLab();
        habilitaBotonesAportacion(true, false);
        //habilitaTab(true, false);
        //seleccionaTab(false, true);
        habilitaCamposAportacion(false);
        txt_banDepHabId.focus();

    }
    //evento enter en bancos

    @Listen("onOK=#txt_busqueda")
    public void dhNextBtnConsultar() {
        btn_consultar.focus();
    }

    @Listen("onOK=#txt_banDepHabId")
    public void dhNext1() {
        cb_tipCuentaDepHab.focus();
    }

    @Listen("onOK=#cb_tipCuentaDepHab")
    public void dhNext2() {
        cb_tipMonedaDepHab.focus();
    }

    @Listen("onOK=#cb_tipMonedaDepHab")
    public void dhNext3() {
        txt_nroCtaDepHab.focus();
    }

    @Listen("onOK=#txt_nroCtaDepHab")
    public void dhNext4() {
        cb_tipPagoDepHab.focus();
    }

    @Listen("onOK=#cb_tipPagoDepHab")
    public void dhNext5() {
        txt_glosaDepHab.focus();
    }

    @Listen("onClick=#tbbtn_btn_deshacerap")
    public void botonDeshacerAportacion() {
        Messagebox.show("Está seguro que desea deshacer los cambios", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //habilitaTab(false, true);
                            //tab_listapersonal.setDisabled(false);
                            //seleccionaTab(true, false);
                            habilitaBotonesAportacion(false, true);
                            habilitaCamposAportacion(true);

                            limpiarCamposAportacion();
                            if (objPersAportacionBase.getPlregpen() != null) {
                                llenarCamposAportacion(objPersAportacionBase);
                            }
                            s_estado_ap = "Q";
                            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //DATOS PAGOS HABERES
    @Listen("onClick=#tbbtn_btn_nuevoph")
    public void botonNuevoPagosHaberes() {
        s_estado_ph = "N";
        objPersPagoHabNuevo = new PersPagoDep();

        //limpiarCamposDatosLab();
        habilitaBotonesPagosHab(true, false);
        //habilitaTab(true, false);
        //seleccionaTab(false, true);
        habilitaCamposPagosHab(false);
        txt_banDepHabId.focus();

    }

    //evento enter en deposito cts
    @Listen("onOK=#txt_banDepCtsId")
    public void dcNext1() {
        cb_tipCuentaDepCts.focus();
    }

    @Listen("onOK=#cb_tipCuentaDepCts")
    public void dcNext2() {
        cb_tipMonedaDepCts.focus();
    }

    @Listen("onOK=#cb_tipMonedaDepCts")
    public void dcNext3() {
        txt_nroCtaDepCts.focus();
    }

    @Listen("onOK=#txt_nroCtaDepCts")
    public void dcNext4() {
        txt_glosaDepCts.focus();
    }

    @Listen("onClick=#tbbtn_btn_editarph")
    public void botonEditarPagosHaberes() throws SQLException {
        s_estado_ph = "M";
        habilitaBotonesPagosHab(true, false);
        habilitaCamposPagosHab(false);

    }

    @Listen("onClick=#tbbtn_btn_deshacerph")
    public void botonDeshacerPagosHaberes() {
        Messagebox.show("Está seguro que desea deshacer los cambios", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //habilitaTab(false, true);
                            //tab_listapersonal.setDisabled(false);
                            //seleccionaTab(true, false);
                            habilitaBotonesPagosHab(false, true);
                            habilitaCamposPagosHab(true);

                            limpiarCamposPagosHab();

                            if (objPersPagoHabBase.getPlnrocta() != null) {
                                llenarCamposPagosHab(objPersPagoHabBase);
                            }
                            s_estado_ph = "Q";
                            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //DATOS PAGOS CTS
    @Listen("onClick=#tbbtn_btn_nuevopc")
    public void botonNuevoPagosCts() {
        s_estado_pc = "N";
        objPersPagoCtsNuevo = new PersPagoDep();

        //limpiarCamposDatosLab();
        habilitaBotonesPagosCts(true, false);
        //habilitaTab(true, false);
        //seleccionaTab(false, true);
        habilitaCamposPagosCts(false);
        txt_banDepCtsId.focus();

    }

    @Listen("onClick=#tbbtn_btn_deshacerpc")
    public void botonDeshacerPagosCts() {
        Messagebox.show("Está seguro que desea deshacer los cambios", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //habilitaTab(false, true);
                            //tab_listapersonal.setDisabled(false);
                            //seleccionaTab(true, false);
                            habilitaBotonesPagosCts(false, true);
                            habilitaCamposPagosCts(true);

                            limpiarCamposPagosCts();
                            if (objPersPagoCtsBase.getPlnrocta() != null) {
                                llenarCamposPagosCts(objPersPagoCtsBase);
                            }
                            s_estado_pc = "Q";
                            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    // DERECHO HABIENTES
    @Listen("onSelect=#lst_derhab")
    public void seleccionaRegistroHabiente() throws SQLException {
        objDerHabiente = new DerHabiente();
        objDerHabiente = (DerHabiente) lst_derhab.getSelectedItem().getValue();
        if (objDerHabiente == null) {
            objDerHabiente = objlstDerHabientes.get(i_sel + 1);
        }
        i_sel = lst_derhab.getSelectedIndex();

        //llenaCamposHabiente();
    }

    @Listen("onClick=#tbbtn_btn_nuevodh")
    public void botonNuevoDerHabiente() {
        s_estado_dh = "N";
        habilitaCamposDerHabiente(false);
        habilitaBotonesDerHabiente(true, false);
        Utilitarios.deshabilitarLista(true, lst_derhab);
        //objlstDerHabientes.clearSelection();
        txt_dhApePat.focus();
        limpiarCamposDerHabiente();

        //DATOS POR DEFECTO
        chk_dhEstado.setChecked(true);
        chk_dhEstado.setDisabled(true);
        txt_dhNacion.setValue("589");
        cb_dhTipDoc.setSelectedIndex(0);
        //cb_dhSituacion.setSelectedItem(Utilitarios.textoPorTexto(cb_dhSituacion, "10"));
        cb_dhSituacion.setSelectedIndex(0);

    }

    @Listen("onClick=#tbbtn_btn_editardh")
    public void botonEditarDerHabiente() throws SQLException {
        if (lst_derhab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado_dh = "M";
            habilitaCamposDerHabiente(false);
            habilitaBotonesDerHabiente(true, false);
            Utilitarios.deshabilitarLista(true, lst_derhab);
        }
    }

    @Listen("onClick=#tbbtn_btn_guardardh")
    public void botonGuardarDerHabiente() throws SQLException {
        String validaDetalle = verificarDerHabiente();
        if (!validaDetalle.isEmpty()) {
            Messagebox.show(validaDetalle, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        validaFocoDerHabiente();
                    }
                }
            });
        } else if (validaNumDoc(Integer.parseInt(cb_dhTipDoc.getSelectedItem().getValue().toString()), txt_dhNroDoc.getValue())) {
            Messagebox.show("No se pudo ingresar. Verificar la cantidad de dígitos del documento", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    txt_dhNroDoc.focus();
                }
            });
        } else if (existeDocumento(Integer.parseInt(cb_dhTipDoc.getSelectedItem().getValue().toString()), txt_dhNroDoc.getValue())) {
            Messagebox.show("No se pudo ingresar. El número de documento ya existe", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    //lst_derhab.focus();
                }
            });
        } else {
            //Si se agrega un item al detalle
            if (s_estado_dh.equals("N")) {
                //Validar si el documento ya fue ingresado

                objDerHabiente = (DerHabiente) generaDerHabiente();
                objDerHabiente.setInd_accion("N");
                objlstDerHabientes.add(objDerHabiente);

            } //Si se modifica o elimina en el detalle
            else {
                //Si se ingreso en el detalle un nuevo registro
                if (objDerHabiente.getInd_accion().equals("N")) {
                    objDerHabiente = (DerHabiente) generaDerHabiente();
                    objDerHabiente.setInd_accion("N");
                } else {
                    objDerHabiente = (DerHabiente) generaDerHabiente();
                    objDerHabiente.setInd_accion("M");
                }
                //Reemplazar el registro en la ubicacion seleccionada
                objlstDerHabientes.set(lst_derhab.getSelectedIndex(), objDerHabiente);
                s_estado_dh = "Q";
            }
            objlstDerHabientes.clearSelection();
            lst_derhab.setModel(objlstDerHabientes);
            Utilitarios.deshabilitarLista(false, lst_derhab);
            limpiarCamposDerHabiente();
            habilitaCamposDerHabiente(true);
            habilitaBotonesDerHabiente(false, true);
            tbbtn_btn_guardardh.setDisabled(true);
            lst_derhab.setFocus(true);
            lst_derhab.focus();
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacerdh")
    public void botonDeshacerDerHabiente() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    habilitaBotonesDerHabiente(false, true);
                    limpiarCamposDerHabiente();
                    habilitaCamposDerHabiente(true);
                    Utilitarios.deshabilitarLista(false, lst_derhab);
                    objlstDerHabientes.clearSelection();
                    s_estado_dh = "Q";
                }
                lst_derhab.setFocus(true);
                lst_derhab.focus();
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_eliminardh")
    public void botonEliminarDerHabiente() throws SQLException {
        if (objlstDerHabientes.isEmpty()) {
            Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (lst_derhab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar el documento?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objDerHabiente = (DerHabiente) lst_derhab.getSelectedItem().getValue();
                        int posicion = lst_derhab.getSelectedIndex();
                        if (s_estado.equals("M") && !objDerHabiente.getInd_accion().equals("N")) {
                            objDerHabiente.setInd_accion("E");
                            objlstDerHabientesEli.add(objDerHabiente);
                        }
                        objlstDerHabientes.remove(posicion);
                        limpiarCamposDerHabiente();
                        lst_derhab.setModel(objlstDerHabientes);
                    }
                }
            });
        }
    }

    public void llenarCamposDni(Personal objPersonal) throws IOException {
        chk_estado.setChecked(objPersonal.getPlestado() == 1);
        txt_apePat.setValue(objPersonal.getPlapepat());
        txt_apeMat.setValue(objPersonal.getPlapemat());
        txt_nombres.setValue(objPersonal.getPlnomemp());
        txt_nacionId.setValue(objPersonal.getPlnacion());
        txt_nacionDes.setValue(objPersonal.getPlnacion_des());
        cb_tipDoc.setSelectedItem(Utilitarios.valorPorTexto(cb_tipDoc, objPersonal.getPltipdoc()));
        txt_nroDoc.setValue(objPersonal.getPlnrodoc());
        d_fecNac.setValue(objPersonal.getPlfecnac());
        cb_sexo.setSelectedItem(Utilitarios.valorPorTexto(cb_sexo, objPersonal.getPlsexo()));
        cb_estCivil.setSelectedItem(Utilitarios.textoPorTexto(cb_estCivil, objPersonal.getPlestcivil()));
        txt_grupSang.setValue(objPersonal.getPlgruposangui());
        txt_telefono.setValue(objPersonal.getPltelemp());
        txt_movil.setValue(objPersonal.getPltelemovil());
        txt_correo.setValue(objPersonal.getPlemail());
        chk_discap.setChecked(objPersonal.getPldiscap() == 1);
        txt_nivEduId.setValue(objPersonal.getPlnivedu());
        txt_nivEduDes.setValue(objPersonal.getPlnivedu_des());
        txt_ocupaId.setValue(objPersonal.getPlidocup());
        txt_ocupaDes.setValue(objPersonal.getPlidocup_des());
        //DOMICILIO
        cb_condicion.setSelectedItem(Utilitarios.valorPorTexto(cb_condicion, objPersonal.getPlconddom()));
        txt_ubigeoId.setValue(objPersonal.getPldir_ubigeo());
        txt_ubigeoDes.setValue(objPersonal.getPldir_ubigeo_des());
        cb_via.setSelectedItem(Utilitarios.textoPorTexto(cb_via, objPersonal.getPldir_via()));
        txt_via.setValue(objPersonal.getPldir_nomvia());
        txt_nro.setValue(objPersonal.getPldir_num());
        txt_int.setValue(objPersonal.getPldir_int());
        cb_zona.setSelectedItem(Utilitarios.textoPorTexto(cb_zona, objPersonal.getPldir_zona()));
        txt_zona.setValue(objPersonal.getPldir_nomzona());
        txt_direccion.setValue(objPersonal.getPldiremp());
        txt_referencia.setValue(objPersonal.getPldir_refer());
        db_dirlatx.setValue(objPersonal.getPldir_corx());
        db_dirlony.setValue(objPersonal.getPldir_cory());
        //completa la direccion
        txt_direccion.setValue(txt_via.getValue() + " " + txt_nro.getValue() + " " + txt_int.getValue() + " " + txt_zona.getValue());

        if (objPersonal.getPlregpen() != null) {
            txt_regPenId.setValue(objPersonal.getPlregpen());
            txt_regPenDes.setValue(objPersonal.getPlregpen_des());
            d_fecIngRp.setValue(objPersonal.getPlfiregpen());
            if (objPersonal.getPltippen() != null) {
                cb_tipPen.setSelectedItem(Utilitarios.textoPorTexto(cb_tipPen, objPersonal.getPltippen()));
            }
            txt_afpId.setValue(objPersonal.getPlcodafp());
            txt_afpDes.setValue(objPersonal.getPlcodafp_des());
            txt_carnetAfp.setValue(objPersonal.getPlcarafp());
            chk_comMix.setChecked(objPersonal.getPlcommix() == 1);
            txt_presSaludId.setValue(objPersonal.getPlpressal());
            txt_presSaludDes.setValue(objPersonal.getPlpressal_des());
            txt_sitEpsId.setValue(objPersonal.getPlsiteps());
            txt_sitEpsDes.setValue(objPersonal.getPlsiteps_des());
            d_fecBajaEs.setValue(objPersonal.getPlfbeps());
            cb_sctrSalud.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrSalud, objPersonal.getPlsct_as()));
            cb_sctrPension.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrPension, objPersonal.getPlsct_as()));

            //objPersAportacionBase = new PersAportacion();
           /* objPersAportacionBase.setPlregpen(objPersonal.getPlregpen());
             objPersAportacionBase.setPlregpen_des(objPersonal.getPlregpen_des());
             objPersAportacionBase.setPlfiregpen(objPersonal.getPlfiregpen());
             objPersAportacionBase.setPltippen(objPersonal.getPltippen());
             objPersAportacionBase.setPlcodafp(objPersonal.getPlcodafp());
             objPersAportacionBase.setPlcodafp_des(objPersonal.getPlcodafp_des());
             objPersAportacionBase.setPlcarafp(objPersonal.getPlcarafp());
             objPersAportacionBase.setPlcommix(objPersonal.getPlcommix());
             objPersAportacionBase.setPlpressal(objPersonal.getPlpressal());
             objPersAportacionBase.setPlpressal_des(objPersonal.getPlpressal_des());
             objPersAportacionBase.setPlsiteps(objPersonal.getPlsiteps());
             objPersAportacionBase.setPlsiteps_des(objPersonal.getPlsiteps_des());
             objPersAportacionBase.setPlfbeps(objPersonal.getPlfbeps());
             objPersAportacionBase.setPlsct_as(objPersonal.getPlsct_as());
             objPersAportacionBase.setPlsct_pp(objPersonal.getPlsct_pp());*/
        }
        //DATOS PAGOS HABERES
        if (objPersonal.getPlnrocta_h() != null) {
            ib_correlph.setValue(objPersonal.getPlcorrel_dep_h());
            txt_banDepHabId.setValue(String.valueOf(objPersonal.getPlbanco_h()));
            txt_banDepHabDes.setValue(String.valueOf(objPersonal.getPlbanco_h_des()));
            cb_tipCuentaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepHab, objPersonal.getPltipcta_h()));
            cb_tipMonedaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepHab, objPersonal.getPlmoneda_h()));
            txt_nroCtaDepHab.setValue(objPersonal.getPlnrocta_h());
            cb_tipPagoDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipPagoDepHab, objPersonal.getPltippago_h()));
            txt_glosaDepHab.setValue(objPersonal.getPlglosa_h());

            //objPersPagoHabBase = new PersPagoDep();
           /* objPersPagoHabBase.setPlcorrel_dep(objPersonal.getPlcorrel_dep_h());
             objPersPagoHabBase.setPlbanco(objPersonal.getPlbanco_h());
             objPersPagoHabBase.setPlbanco_des(objPersonal.getPlbanco_h_des());
             objPersPagoHabBase.setPltipcta(objPersonal.getPltipcta_h());
             objPersPagoHabBase.setPlmoneda(objPersonal.getPlmoneda_h());
             objPersPagoHabBase.setPlnrocta(objPersonal.getPlnrocta_h());
             objPersPagoHabBase.setPltippago(objPersonal.getPltippago_h());
             objPersPagoHabBase.setPlglosa(objPersonal.getPlglosa_h());*/
        }

        //DATOS PAGOS CTS
        if (objPersonal.getPlnrocta_c() != null) {
            ib_correlpc.setValue(objPersonal.getPlcorrel_dep_c());
            txt_banDepCtsId.setValue(String.valueOf(objPersonal.getPlbanco_c()));
            txt_banDepCtsDes.setValue(String.valueOf(objPersonal.getPlbanco_c_des()));
            cb_tipCuentaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepCts, objPersonal.getPltipcta_c()));
            cb_tipMonedaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepCts, objPersonal.getPlmoneda_c()));
            txt_nroCtaDepCts.setValue(objPersonal.getPlnrocta_c());
            txt_glosaDepCts.setValue(objPersonal.getPlglosa_c());

            //objPersPagoCtsBase = new PersPagoDep();
           /* objPersPagoCtsBase.setPlcorrel_dep(objPersonal.getPlcorrel_dep_c());
             objPersPagoCtsBase.setPlbanco(objPersonal.getPlbanco_c());
             objPersPagoCtsBase.setPlbanco_des(objPersonal.getPlbanco_c_des());
             objPersPagoCtsBase.setPltipcta(objPersonal.getPltipcta_c());
             objPersPagoCtsBase.setPlmoneda(objPersonal.getPlmoneda_c());
             objPersPagoCtsBase.setPlnrocta(objPersonal.getPlnrocta_c());
             objPersPagoCtsBase.setPltippago(objPersonal.getPltippago_c());
             objPersPagoCtsBase.setPlglosa(objPersonal.getPlglosa_c());*/
        }
        //DATOS ADJUNTO
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        txt_adj_croquis.setValue(rutaFileCroquis + documento + ".PDF");
        txt_adj_dni.setValue(rutaFileDni + documento + ".PDF");
        txt_adj_firma.setValue(rutaFileFirma + documento + ".PDF");
        txt_adj_foto.setValue(rutaFileFoto + documento + ".JPEG");
        txt_adj_recibo.setValue(rutaFileRecibo + documento + ".PDF");
        txt_adj_otros.setValue(rutaFileOtros + documento + ".PDF");
        txt_adj_antpol.setValue(rutaFileAntPol + documento + ".PDF");
        txt_adj_cerdom.setValue(rutaFileCerDom + documento + ".PDF");
        txt_adj_curvit.setValue(rutaFileCurVit + documento + ".PDF");
        cargarImagen(rutaFileFoto + documento + ".JPEG", img_foto);

        if (objPersonal.getPlestado() == 2) {
            Messagebox.show("Trabajador inactivo desea volver activarlo", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                // d_fecNac.setValue(null);
                                chk_estado.setDisabled(false);
                                chk_estado.focus();
                            }
                        }
                    });
        }

    }

    public void llenarCampos(Personal objPersonal) throws IOException {

        chk_estado.setChecked(objPersonal.getPlestado() == 1);
        txt_apePat.setValue(objPersonal.getPlapepat());
        txt_apeMat.setValue(objPersonal.getPlapemat());
        txt_nombres.setValue(objPersonal.getPlnomemp());
        txt_nacionId.setValue(objPersonal.getPlnacion());
        txt_nacionDes.setValue(objPersonal.getPlnacion_des());
        cb_tipDoc.setSelectedItem(Utilitarios.valorPorTexto(cb_tipDoc, objPersonal.getPltipdoc()));
        txt_nroDoc.setValue(objPersonal.getPlnrodoc());
        d_fecNac.setValue(objPersonal.getPlfecnac());
        cb_sexo.setSelectedItem(Utilitarios.valorPorTexto(cb_sexo, objPersonal.getPlsexo()));
        cb_estCivil.setSelectedItem(Utilitarios.textoPorTexto(cb_estCivil, objPersonal.getPlestcivil()));
        txt_grupSang.setValue(objPersonal.getPlgruposangui());
        txt_telefono.setValue(objPersonal.getPltelemp());
        txt_movil.setValue(objPersonal.getPltelemovil());
        txt_correo.setValue(objPersonal.getPlemail());
        chk_discap.setChecked(objPersonal.getPldiscap() == 1);
        txt_nivEduId.setValue(objPersonal.getPlnivedu());
        txt_nivEduDes.setValue(objPersonal.getPlnivedu_des());
        txt_ocupaId.setValue(objPersonal.getPlidocup());
        txt_ocupaDes.setValue(objPersonal.getPlidocup_des());

        //DOMICILIO
        cb_condicion.setSelectedItem(Utilitarios.valorPorTexto(cb_condicion, objPersonal.getPlconddom()));
        txt_ubigeoId.setValue(objPersonal.getPldir_ubigeo());
        txt_ubigeoDes.setValue(objPersonal.getPldir_ubigeo_des());
        cb_via.setSelectedItem(Utilitarios.textoPorTexto(cb_via, objPersonal.getPldir_via()));
        txt_via.setValue(objPersonal.getPldir_nomvia());
        txt_nro.setValue(objPersonal.getPldir_num());
        txt_int.setValue(objPersonal.getPldir_int());
        cb_zona.setSelectedItem(Utilitarios.textoPorTexto(cb_zona, objPersonal.getPldir_zona()));
        txt_zona.setValue(objPersonal.getPldir_nomzona());
        txt_direccion.setValue(objPersonal.getPldiremp());
        txt_referencia.setValue(objPersonal.getPldir_refer());
        db_dirlatx.setValue(objPersonal.getPldir_corx());
        db_dirlony.setValue(objPersonal.getPldir_cory());

        //DATOS LABORALES
        if (objPersonal.getPlarea() != null) {
            d_fecIng.setValue(objPersonal.getPlfecing());

            if (objPersonal.getPlfecces() != null) {
                d_fecCese.setValue(objPersonal.getPlfecces());
                cb_motivoCese.setSelectedItem(Utilitarios.textoPorTexto(cb_motivoCese, objPersonal.getPlcesemot()));
                txt_ceseDet.setValue(objPersonal.getPlcesedet());
                txt_ceseObs.setValue(objPersonal.getPlceseobs());
            }
            txt_codInterno.setValue(objPersonal.getPlcodemp());
            cb_sucursal.setSelectedItem(Utilitarios.valorPorTexto(cb_sucursal, objPersonal.getSuc_id()));
            txt_areaId.setValue(objPersonal.getPlarea());
            txt_areaDes.setValue(objPersonal.getPlarea_des());
            txt_ccostoId.setValue(objPersonal.getPlccosto());
            txt_ccostoDes.setValue(objPersonal.getPlccosto_des());
            cb_categoria.setSelectedItem(Utilitarios.textoPorTexto(cb_categoria, objPersonal.getPlcatper()));
            txt_tipTrabId.setValue(objPersonal.getPltiptra());
            txt_tipTrabDes.setValue(objPersonal.getPltiptra_des());
            txt_horarioId.setValue(objPersonal.getPlhorari());
            txt_horarioDes.setValue(objPersonal.getPlhorari_des());
            txt_cargoId.setValue(objPersonal.getPlidcargo());
            txt_cargoDes.setValue(objPersonal.getPlidcargo_des());
            cb_sitEsp.setSelectedItem(Utilitarios.valorPorTexto(cb_sitEsp, objPersonal.getPl_cc()));
            txt_tipContId.setValue(objPersonal.getPltipcont());
            txt_tipContDes.setValue(objPersonal.getPltipcont_des());
            chk_sujCtrlInm.setChecked(objPersonal.getPlsujconinm() == 1);
            cb_tipSueldo.setSelectedItem(Utilitarios.valorPorTexto(cb_tipSueldo, objPersonal.getPltipsue()));
            cb_perRem.setSelectedItem(Utilitarios.valorPorTexto(cb_perRem, objPersonal.getPlperrem()));
            //ib_hExtras.setValue(objPersonal.getPlhextra());
            chk_utilidad.setChecked(objPersonal.getPlputil() == 1);
            chk_excQuinta.setChecked(objPersonal.getPlquinta() == 1);
            chk_otrosQuinta.setChecked(objPersonal.getPlotr5ta() == 1);
            chk_essalud.setChecked(objPersonal.getPlippsvi() == 1);
            txt_carnetSsp.setValue(objPersonal.getPlcarssp());
            chk_sindicalizado.setChecked(objPersonal.getPlsindic() == 1);
            chk_pensionista.setChecked(objPersonal.getPlespens() == 1);

            //objDatosLabBase = new DatosLaborales();
            objDatosLabBase.setPlfecing(objPersonal.getPlfecing());
            objDatosLabBase.setPlfecces(objPersonal.getPlfecces());
            objDatosLabBase.setPlcesemot(objPersonal.getPlcesemot());
            objDatosLabBase.setPlcesedet(objPersonal.getPlcesedet());
            objDatosLabBase.setPlceseobs(objPersonal.getPlceseobs());
            objDatosLabBase.setPlcodemp(objPersonal.getPlcodemp());
            objDatosLabBase.setSuc_id(objPersonal.getSuc_id());
            objDatosLabBase.setPlarea(objPersonal.getPlarea());
            objDatosLabBase.setPlarea_des(objPersonal.getPlarea_des());
            objDatosLabBase.setPlccosto(objPersonal.getPlccosto());
            objDatosLabBase.setPlccosto_des(objPersonal.getPlccosto_des());
            objDatosLabBase.setPlcatper(objPersonal.getPlcatper());
            objDatosLabBase.setPltiptra(objPersonal.getPltiptra());
            objDatosLabBase.setPltiptra_des(objPersonal.getPltiptra_des());
            objDatosLabBase.setPlhorari(objPersonal.getPlhorari());
            objDatosLabBase.setPlhorari_des(objPersonal.getPlhorari_des());
            objDatosLabBase.setPlidcargo(objPersonal.getPlidcargo());
            objDatosLabBase.setPlidcargo_des(objPersonal.getPlidcargo_des());
            objDatosLabBase.setPl_cc(objPersonal.getPl_cc());
            objDatosLabBase.setPltipcont(objPersonal.getPltipcont());
            objDatosLabBase.setPltipcont_des(objPersonal.getPltipcont_des());
            objDatosLabBase.setPlsujconinm(objPersonal.getPlsujconinm());
            objDatosLabBase.setPltipsue(objPersonal.getPltipsue());
            objDatosLabBase.setPlperrem(objPersonal.getPlperrem());
            // objDatosLabBase.setPlhextra(objPersonal.getPlhextra());
            objDatosLabBase.setPlputil(objPersonal.getPlputil());
            objDatosLabBase.setPlquinta(objPersonal.getPlquinta());
            objDatosLabBase.setPlotr5ta(objPersonal.getPlotr5ta());
            objDatosLabBase.setPlippsvi(objPersonal.getPlippsvi());
            objDatosLabBase.setPlcarssp(objPersonal.getPlcarssp());
            objDatosLabBase.setPlsindic(objPersonal.getPlsindic());
            objDatosLabBase.setPlespens(objPersonal.getPlespens());

        }

        //DATOS APORTACIONES
        if (objPersonal.getPlregpen() != null) {
            txt_regPenId.setValue(objPersonal.getPlregpen());
            txt_regPenDes.setValue(objPersonal.getPlregpen_des());
            d_fecIngRp.setValue(objPersonal.getPlfiregpen());
            if (objPersonal.getPltippen() != null) {
                cb_tipPen.setSelectedItem(Utilitarios.textoPorTexto(cb_tipPen, objPersonal.getPltippen()));
            }
            txt_afpId.setValue(objPersonal.getPlcodafp());
            txt_afpDes.setValue(objPersonal.getPlcodafp_des());
            txt_carnetAfp.setValue(objPersonal.getPlcarafp());
            chk_comMix.setChecked(objPersonal.getPlcommix() == 1);
            txt_presSaludId.setValue(objPersonal.getPlpressal());
            txt_presSaludDes.setValue(objPersonal.getPlpressal_des());
            txt_sitEpsId.setValue(objPersonal.getPlsiteps());
            txt_sitEpsDes.setValue(objPersonal.getPlsiteps_des());
            d_fecBajaEs.setValue(objPersonal.getPlfbeps());
            cb_sctrSalud.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrSalud, objPersonal.getPlsct_as()));
            cb_sctrPension.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrPension, objPersonal.getPlsct_as()));

            //objPersAportacionBase = new PersAportacion();
            objPersAportacionBase.setPlregpen(objPersonal.getPlregpen());
            objPersAportacionBase.setPlregpen_des(objPersonal.getPlregpen_des());
            objPersAportacionBase.setPlfiregpen(objPersonal.getPlfiregpen());
            objPersAportacionBase.setPltippen(objPersonal.getPltippen());
            objPersAportacionBase.setPlcodafp(objPersonal.getPlcodafp());
            objPersAportacionBase.setPlcodafp_des(objPersonal.getPlcodafp_des());
            objPersAportacionBase.setPlcarafp(objPersonal.getPlcarafp());
            objPersAportacionBase.setPlcommix(objPersonal.getPlcommix());
            objPersAportacionBase.setPlpressal(objPersonal.getPlpressal());
            objPersAportacionBase.setPlpressal_des(objPersonal.getPlpressal_des());
            objPersAportacionBase.setPlsiteps(objPersonal.getPlsiteps());
            objPersAportacionBase.setPlsiteps_des(objPersonal.getPlsiteps_des());
            objPersAportacionBase.setPlfbeps(objPersonal.getPlfbeps());
            objPersAportacionBase.setPlsct_as(objPersonal.getPlsct_as());
            objPersAportacionBase.setPlsct_pp(objPersonal.getPlsct_pp());

        }

        //DATOS PAGOS HABERES
        if (objPersonal.getPlnrocta_h() != null) {
            ib_correlph.setValue(objPersonal.getPlcorrel_dep_h());
            txt_banDepHabId.setValue(String.valueOf(objPersonal.getPlbanco_h()));
            txt_banDepHabDes.setValue(String.valueOf(objPersonal.getPlbanco_h_des()));
            cb_tipCuentaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepHab, objPersonal.getPltipcta_h()));
            cb_tipMonedaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepHab, objPersonal.getPlmoneda_h()));
            txt_nroCtaDepHab.setValue(objPersonal.getPlnrocta_h());
            cb_tipPagoDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipPagoDepHab, objPersonal.getPltippago_h()));
            txt_glosaDepHab.setValue(objPersonal.getPlglosa_h());

            //objPersPagoHabBase = new PersPagoDep();
            objPersPagoHabBase.setPlcorrel_dep(objPersonal.getPlcorrel_dep_h());
            objPersPagoHabBase.setPlbanco(objPersonal.getPlbanco_h());
            objPersPagoHabBase.setPlbanco_des(objPersonal.getPlbanco_h_des());
            objPersPagoHabBase.setPltipcta(objPersonal.getPltipcta_h());
            objPersPagoHabBase.setPlmoneda(objPersonal.getPlmoneda_h());
            objPersPagoHabBase.setPlnrocta(objPersonal.getPlnrocta_h());
            objPersPagoHabBase.setPltippago(objPersonal.getPltippago_h());
            objPersPagoHabBase.setPlglosa(objPersonal.getPlglosa_h());

        }

        //DATOS PAGOS CTS
        if (objPersonal.getPlnrocta_c() != null) {
            ib_correlpc.setValue(objPersonal.getPlcorrel_dep_c());
            txt_banDepCtsId.setValue(String.valueOf(objPersonal.getPlbanco_c()));
            txt_banDepCtsDes.setValue(String.valueOf(objPersonal.getPlbanco_c_des()));
            cb_tipCuentaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepCts, objPersonal.getPltipcta_c()));
            cb_tipMonedaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepCts, objPersonal.getPlmoneda_c()));
            txt_nroCtaDepCts.setValue(objPersonal.getPlnrocta_c());
            txt_glosaDepCts.setValue(objPersonal.getPlglosa_c());

            //objPersPagoCtsBase = new PersPagoDep();
            objPersPagoCtsBase.setPlcorrel_dep(objPersonal.getPlcorrel_dep_c());
            objPersPagoCtsBase.setPlbanco(objPersonal.getPlbanco_c());
            objPersPagoCtsBase.setPlbanco_des(objPersonal.getPlbanco_c_des());
            objPersPagoCtsBase.setPltipcta(objPersonal.getPltipcta_c());
            objPersPagoCtsBase.setPlmoneda(objPersonal.getPlmoneda_c());
            objPersPagoCtsBase.setPlnrocta(objPersonal.getPlnrocta_c());
            objPersPagoCtsBase.setPltippago(objPersonal.getPltippago_c());
            objPersPagoCtsBase.setPlglosa(objPersonal.getPlglosa_c());
        }

        //DATOS ADJUNTO
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        txt_adj_croquis.setValue(rutaFileCroquis + documento + ".PDF");
        txt_adj_dni.setValue(rutaFileDni + documento + ".PDF");
        txt_adj_firma.setValue(rutaFileFirma + documento + ".PDF");
        txt_adj_foto.setValue(rutaFileFoto + documento + ".JPEG");
        txt_adj_recibo.setValue(rutaFileRecibo + documento + ".PDF");
        txt_adj_otros.setValue(rutaFileOtros + documento + ".PDF");
        txt_adj_antpol.setValue(rutaFileAntPol + documento + ".PDF");
        txt_adj_cerdom.setValue(rutaFileCerDom + documento + ".PDF");
        txt_adj_curvit.setValue(rutaFileCurVit + documento + ".PDF");

        //Auditoría
        txt_usuadd.setValue(objPersonal.getPlusuadd());
        d_fecadd.setValue(objPersonal.getPlfecadd());
        txt_usumod.setValue(objPersonal.getPlusumod());
        d_fecmod.setValue(objPersonal.getPlfecmod());

        cargarImagen(rutaFileFoto + documento + ".JPEG", img_foto);

    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapersonal.setSelected(b_valida1);
        tab_datosper.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapersonal.setDisabled(b_valida1);
        tab_datosper.setDisabled(b_valida2);
        tab_datoslab.setDisabled(b_valida2);
        tab_pagos.setDisabled(b_valida2);
        tab_derhabientes.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        // tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
        //
        btn_coordenadasDomi.setDisabled(b_valida2);
        //btn_capture.setDisabled(b_valida2);
        //btn_guardar.setDisabled(b_valida2);
        //tbbtn_btn_cesedl.setDisabled(b_valida2);
    }

    public void habilitaBotonesPersonal(boolean b_valida1) {
        tbbtn_btn_nuevodl.setDisabled(b_valida1);
        tbbtn_btn_editardl.setDisabled(b_valida1);
        tbbtn_btn_deshacerdl.setDisabled(b_valida1);
        //
        tbbtn_btn_nuevoap.setDisabled(b_valida1);
        tbbtn_btn_deshacerap.setDisabled(b_valida1);
        //
        tbbtn_btn_nuevoph.setDisabled(b_valida1);
        tbbtn_btn_deshacerph.setDisabled(b_valida1);
        //
        tbbtn_btn_nuevopc.setDisabled(b_valida1);
        tbbtn_btn_deshacerpc.setDisabled(b_valida1);
    }

    public void habilitaBotonesAdjunto(boolean b_valida1) {

        tbbtn_adjcar_antpol.setDisabled(b_valida1);
        tbbtn_adjcar_cerdom.setDisabled(b_valida1);
        tbbtn_adjcar_curvit.setDisabled(b_valida1);
        tbbtn_adjcar_croquis.setDisabled(b_valida1);
        tbbtn_adjcar_dni.setDisabled(b_valida1);
        tbbtn_adjcar_firma.setDisabled(b_valida1);
        tbbtn_adjcar_foto.setDisabled(b_valida1);
        tbbtn_adjcar_recibo.setDisabled(b_valida1);
        tbbtn_adjcar_otros.setDisabled(b_valida1);
        tbbtn_adjcar_fotofile.setDisabled(b_valida1);
    }

    public void habilitaBotonesPdf(boolean b_valida1) {

        tbbtn_adjpdf_antpol.setDisabled(b_valida1);
        tbbtn_adjpdf_cerdom.setDisabled(b_valida1);
        tbbtn_adjpdf_curvit.setDisabled(b_valida1);
        tbbtn_adjpdf_croquis.setDisabled(b_valida1);
        tbbtn_adjpdf_dni.setDisabled(b_valida1);
        tbbtn_adjpdf_firma.setDisabled(b_valida1);
        //tbbtn_adjpdf_foto.setDisabled(b_valida1);
        tbbtn_adjpdf_recibo.setDisabled(b_valida1);
        tbbtn_adjpdf_otros.setDisabled(b_valida1);
    }

    public void limpiarCampos() {
        //DATOS PERSONALES
        chk_estado.setChecked(false);
        txt_apePat.setValue("");
        txt_apeMat.setValue("");
        txt_nombres.setValue("");
        txt_nacionId.setValue("");
        txt_nacionDes.setValue("");
        cb_tipDoc.setSelectedIndex(-1);
        txt_nroDoc.setValue("");
        d_fecNac.setValue(null);
        cb_sexo.setSelectedIndex(-1);
        cb_estCivil.setSelectedIndex(-1);
        txt_grupSang.setValue("");
        txt_telefono.setValue("");
        txt_movil.setValue("");
        txt_correo.setValue("");
        chk_discap.setChecked(false);
        txt_nivEduId.setValue("");
        txt_nivEduDes.setValue("");
        txt_ocupaId.setValue("");
        txt_ocupaDes.setValue("");
        img_foto.getChildren().clear();

        //DOMICILIO
        cb_condicion.setSelectedIndex(-1);
        txt_ubigeoId.setValue("");
        txt_ubigeoDes.setValue("");
        cb_via.setSelectedIndex(-1);
        txt_via.setValue("");
        txt_nro.setValue("");
        txt_int.setValue("");
        cb_zona.setSelectedIndex(-1);
        txt_zona.setValue("");
        txt_direccion.setValue("");
        txt_referencia.setValue("");
        db_dirlatx.setValue(null);
        db_dirlony.setValue(null);

        //ADJUNTO
        txt_adj_croquis.setValue("");
        txt_adj_dni.setValue("");
        txt_adj_firma.setValue("");
        txt_adj_foto.setValue("");
        txt_adj_recibo.setValue("");
        txt_adj_otros.setValue("");
        txt_adj_antpol.setValue("");
        txt_adj_cerdom.setValue("");
        txt_adj_curvit.setValue("");

    }

    public void habilitaCampos(boolean b_valida1) {
        //Datos personales
        chk_estado.setDisabled(b_valida1);
        txt_apePat.setDisabled(b_valida1);
        txt_apeMat.setDisabled(b_valida1);
        txt_nombres.setDisabled(b_valida1);
        txt_nacionId.setDisabled(b_valida1);
        cb_tipDoc.setDisabled(b_valida1);
        txt_nroDoc.setDisabled(b_valida1);
        d_fecNac.setDisabled(b_valida1);
        cb_sexo.setDisabled(b_valida1);
        cb_estCivil.setDisabled(b_valida1);
        txt_grupSang.setDisabled(b_valida1);
        txt_telefono.setDisabled(b_valida1);
        txt_movil.setDisabled(b_valida1);
        txt_correo.setDisabled(b_valida1);
        chk_discap.setDisabled(b_valida1);
        txt_nivEduId.setDisabled(b_valida1);
        txt_ocupaId.setDisabled(b_valida1);
        //Domicilio
        cb_condicion.setDisabled(b_valida1);
        txt_ubigeoId.setDisabled(b_valida1);
        cb_via.setDisabled(b_valida1);
        txt_via.setDisabled(b_valida1);
        txt_nro.setDisabled(b_valida1);
        txt_int.setDisabled(b_valida1);
        cb_zona.setDisabled(b_valida1);
        txt_zona.setDisabled(b_valida1);
        // txt_direccion.setDisabled(b_valida1);
        txt_referencia.setDisabled(b_valida1);
    }

    //DATOS LABORALES
    public void habilitaBotonesDatosLab(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevodl.setDisabled(b_valida1);
        tbbtn_btn_editardl.setDisabled(b_valida1);
        tbbtn_btn_deshacerdl.setDisabled(b_valida2);
    }

    public void limpiarCamposDatosLab() {
        d_fecIng.setValue(null);
        d_fecCese.setValue(null);
        txt_codInterno.setValue("");
        cb_sucursal.setSelectedIndex(-1);
        txt_areaId.setValue("");
        txt_areaDes.setValue("");
        txt_ccostoId.setValue("");
        txt_ccostoDes.setValue("");
        cb_categoria.setSelectedIndex(-1);
        txt_tipTrabId.setValue("");
        txt_tipTrabDes.setValue("");
        txt_horarioId.setValue("");
        txt_horarioDes.setValue("");
        txt_cargoId.setValue("");
        txt_cargoDes.setValue("");
        cb_sitEsp.setSelectedIndex(-1);
        txt_tipContId.setValue("");
        txt_tipContDes.setValue("");
        chk_sujCtrlInm.setChecked(false);
        cb_tipSueldo.setSelectedIndex(-1);
        cb_perRem.setSelectedIndex(-1);
        //ib_hExtras.setValue(null);
        chk_utilidad.setChecked(false);
        chk_excQuinta.setChecked(false);
        chk_otrosQuinta.setChecked(false);
        chk_essalud.setChecked(false);
        txt_carnetSsp.setValue("");
        chk_sindicalizado.setChecked(false);
        chk_pensionista.setChecked(false);
    }

    public void llenarCamposDatosLab(DatosLaborales objDatLab) {
        d_fecIng.setValue(objDatLab.getPlfecing());
        d_fecCese.setValue(objDatLab.getPlfecces());

        if (objDatLab.getPlcesemot() != null) {
            cb_motivoCese.setSelectedItem(Utilitarios.textoPorTexto(cb_motivoCese, objDatLab.getPlcesemot()));
        }

        txt_ceseDet.setValue(objDatLab.getPlcesedet());
        txt_ceseObs.setValue(objDatLab.getPlceseobs());
        txt_codInterno.setValue(objDatLab.getPlcodemp());
        cb_sucursal.setSelectedItem(Utilitarios.valorPorTexto(cb_sucursal, objDatLab.getSuc_id()));
        txt_areaId.setValue(objDatLab.getPlarea());
        txt_areaDes.setValue(objDatLab.getPlarea_des());
        txt_ccostoId.setValue(objDatLab.getPlccosto());
        txt_ccostoDes.setValue(objDatLab.getPlccosto_des());
        cb_categoria.setSelectedItem(Utilitarios.textoPorTexto(cb_categoria, objDatLab.getPlcatper()));
        txt_tipTrabId.setValue(objDatLab.getPltiptra());
        txt_tipTrabDes.setValue(objDatLab.getPltiptra_des());
        txt_horarioId.setValue(objDatLab.getPlhorari());
        txt_cargoId.setValue(objDatLab.getPlidcargo());
        txt_cargoDes.setValue(objDatLab.getPlidcargo_des());
        cb_sitEsp.setSelectedItem(Utilitarios.valorPorTexto(cb_sitEsp, objDatLab.getPl_cc()));
        txt_tipContId.setValue(objDatLab.getPltipcont());
        txt_tipContDes.setValue(objDatLab.getPltipcont_des());
        chk_sujCtrlInm.setChecked(objDatLab.getPlsujconinm() == 1);
        cb_tipSueldo.setSelectedItem(Utilitarios.valorPorTexto(cb_tipSueldo, objDatLab.getPltipsue()));
        cb_perRem.setSelectedItem(Utilitarios.valorPorTexto(cb_perRem, objDatLab.getPlperrem()));
        //ib_hExtras.setValue(objDatLab.getPlhextra());
        chk_utilidad.setChecked(objDatLab.getPlputil() == 1);
        chk_excQuinta.setChecked(objDatLab.getPlquinta() == 1);
        chk_otrosQuinta.setChecked(objDatLab.getPlotr5ta() == 1);
        chk_essalud.setChecked(objDatLab.getPlippsvi() == 1);
        txt_carnetSsp.setValue(objDatLab.getPlcarssp());
        chk_sindicalizado.setChecked(objDatLab.getPlsindic() == 1);
        chk_pensionista.setChecked(objDatLab.getPlespens() == 1);
    }

    public void habilitaCamposDatosLab(boolean b_valida1) {
        //Datos personales
        d_fecIng.setDisabled(b_valida1);
        d_fecCese.setDisabled(b_valida1);
        txt_codInterno.setDisabled(b_valida1);
        cb_sucursal.setDisabled(b_valida1);
        txt_areaId.setDisabled(b_valida1);
        txt_ccostoId.setDisabled(b_valida1);
        cb_categoria.setDisabled(b_valida1);
        txt_tipTrabId.setDisabled(b_valida1);
        txt_horarioId.setDisabled(b_valida1);
        txt_cargoId.setDisabled(b_valida1);
        cb_sitEsp.setDisabled(b_valida1);
        txt_tipContId.setDisabled(b_valida1);
        chk_sujCtrlInm.setDisabled(b_valida1);
        cb_tipSueldo.setDisabled(b_valida1);
        cb_perRem.setDisabled(b_valida1);
        //ib_hExtras.setDisabled(b_valida1);
        chk_utilidad.setDisabled(b_valida1);
        chk_excQuinta.setDisabled(b_valida1);
        chk_otrosQuinta.setDisabled(b_valida1);
        chk_essalud.setDisabled(b_valida1);
        txt_carnetSsp.setDisabled(b_valida1);
        chk_sindicalizado.setDisabled(b_valida1);
        chk_pensionista.setDisabled(b_valida1);
    }

    public void habilitaCamposDatosLabEditar(boolean b_valida1) {
        //Datos personales
        //d_fecIng.setDisabled(b_valida1);
        d_fecCese.setDisabled(b_valida1);
        /*txt_codInterno.setDisabled(b_valida1);
         cb_sucursal.setDisabled(b_valida1);
         txt_areaId.setDisabled(b_valida1);
         cb_categoria.setDisabled(b_valida1);
         txt_tipTrabId.setDisabled(b_valida1);
         txt_horarioId.setDisabled(b_valida1);
         txt_cargoId.setDisabled(b_valida1);
         cb_sitEsp.setDisabled(b_valida1);
         txt_tipContId.setDisabled(b_valida1);
         chk_sujCtrlInm.setDisabled(b_valida1);
         cb_tipSueldo.setDisabled(b_valida1);
         cb_perRem.setDisabled(b_valida1);
         ib_hExtras.setDisabled(b_valida1);
         chk_utilidad.setDisabled(b_valida1);
         chk_excQuinta.setDisabled(b_valida1);
         chk_otrosQuinta.setDisabled(b_valida1);
         chk_essalud.setDisabled(b_valida1);
         txt_carnetSsp.setDisabled(b_valida1);
         chk_sindicalizado.setDisabled(b_valida1);
         chk_pensionista.setDisabled(b_valida1);*/
    }

    //DATOS APORTACIONES
    public void habilitaBotonesAportacion(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoap.setDisabled(b_valida1);
        tbbtn_btn_deshacerap.setDisabled(b_valida2);
    }

    public void limpiarCamposAportacion() {
        txt_regPenId.setValue("");
        txt_regPenDes.setValue("");
        d_fecIngRp.setValue(null);
        cb_tipPen.setSelectedIndex(-1);
        txt_afpId.setValue("");
        txt_afpDes.setValue("");
        txt_carnetAfp.setValue("");
        chk_comMix.setChecked(false);
        txt_presSaludId.setValue("");
        txt_presSaludDes.setValue("");
        txt_sitEpsId.setValue("");
        txt_sitEpsDes.setValue("");
        d_fecBajaEs.setValue(null);
        cb_sctrSalud.setSelectedIndex(-1);
        cb_sctrPension.setSelectedIndex(-1);

    }

    public void habilitaCamposAportacion(boolean b_valida1) {
        txt_regPenId.setDisabled(b_valida1);
        d_fecIngRp.setDisabled(b_valida1);
        d_fecCesRp.setDisabled(b_valida1);
        cb_tipPen.setDisabled(b_valida1);
        //txt_afpId.setDisabled(b_valida1);
        txt_carnetAfp.setDisabled(b_valida1);
        chk_comMix.setDisabled(b_valida1);
        txt_presSaludId.setDisabled(b_valida1);
        txt_sitEpsId.setDisabled(b_valida1);
        d_fecBajaEs.setDisabled(b_valida1);
        cb_sctrSalud.setDisabled(b_valida1);
        cb_sctrPension.setDisabled(b_valida1);
    }

    public void llenarCamposAportacion(PersAportacion objPerAport) {
        txt_regPenId.setValue(objPerAport.getPlregpen());
        txt_regPenDes.setValue(objPerAport.getPlregpen_des());
        d_fecIngRp.setValue(objPerAport.getPlfiregpen());
        //cb_tipPen.setSelectedIndex(objPerAport.getPltippen());
        if (objPerAport.getPltippen() != null) {
            cb_tipPen.setSelectedItem(Utilitarios.textoPorTexto(cb_tipPen, objPerAport.getPltippen()));
        }

        txt_afpId.setValue(objPerAport.getPlcodafp());
        txt_afpDes.setValue(objPerAport.getPlcodafp_des());
        txt_carnetAfp.setValue(objPerAport.getPlcarafp());
        chk_comMix.setChecked(objPerAport.getPlcommix() == 1);
        txt_presSaludId.setValue(objPerAport.getPlpressal());
        txt_presSaludDes.setValue(objPerAport.getPlpressal_des());
        txt_sitEpsId.setValue(objPerAport.getPlsiteps());
        txt_sitEpsDes.setValue(objPerAport.getPlsiteps_des());
        d_fecBajaEs.setValue(objPerAport.getPlfbeps());
        cb_sctrSalud.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrSalud, objPerAport.getPlsct_as()));
        cb_sctrPension.setSelectedItem(Utilitarios.valorPorTexto(cb_sctrPension, objPerAport.getPlsct_pp()));

    }

    //DATOS PAGOS HABERES
    public void habilitaBotonesPagosHab(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoph.setDisabled(b_valida1);
        //tbbtn_btn_editarph.setDisabled(b_valida1);
        tbbtn_btn_deshacerph.setDisabled(b_valida2);
    }

    public void limpiarCamposPagosHab() {
        ib_correlph.setValue(null);
        txt_banDepHabId.setValue("");
        txt_banDepHabDes.setValue("");
        cb_tipCuentaDepHab.setSelectedIndex(-1);
        cb_tipMonedaDepHab.setSelectedIndex(-1);
        txt_nroCtaDepHab.setValue("");
        cb_tipPagoDepHab.setSelectedIndex(-1);
        txt_glosaDepHab.setValue("");

    }

    public void habilitaCamposPagosHab(boolean b_valida1) {
        txt_banDepHabId.setDisabled(b_valida1);
        cb_tipCuentaDepHab.setDisabled(b_valida1);
        cb_tipMonedaDepHab.setDisabled(b_valida1);
        txt_nroCtaDepHab.setDisabled(b_valida1);
        cb_tipPagoDepHab.setDisabled(b_valida1);
        txt_glosaDepHab.setDisabled(b_valida1);
    }

    public void llenarCamposPagosHab(PersPagoDep objPerPagHab) {
        ib_correlph.setValue(objPerPagHab.getPlcorrel_dep());
        txt_banDepHabId.setValue(String.valueOf(objPerPagHab.getPlbanco()));
        txt_banDepHabDes.setValue(objPerPagHab.getPlbanco_des());
        cb_tipCuentaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepHab, objPerPagHab.getPltipcta()));
        cb_tipMonedaDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepHab, objPerPagHab.getPlmoneda()));
        txt_nroCtaDepHab.setValue(objPerPagHab.getPlnrocta());
        cb_tipPagoDepHab.setSelectedItem(Utilitarios.valorPorTexto(cb_tipPagoDepHab, objPerPagHab.getPltippago()));
        txt_glosaDepHab.setValue(objPerPagHab.getPlglosa());

    }

    //DATOS PAGOS CTS
    public void habilitaBotonesPagosCts(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevopc.setDisabled(b_valida1);
        tbbtn_btn_editarpc.setDisabled(b_valida1);
        tbbtn_btn_deshacerpc.setDisabled(b_valida2);
    }

    public void limpiarCamposPagosCts() {
        ib_correlpc.setValue(null);
        txt_banDepCtsId.setValue("");
        txt_banDepCtsDes.setValue("");
        cb_tipCuentaDepCts.setSelectedIndex(-1);
        cb_tipMonedaDepCts.setSelectedIndex(-1);
        txt_nroCtaDepCts.setValue("");
        txt_glosaDepCts.setValue("");

    }

    public void habilitaCamposPagosCts(boolean b_valida1) {
        txt_banDepCtsId.setDisabled(b_valida1);
        cb_tipCuentaDepCts.setDisabled(b_valida1);
        cb_tipMonedaDepCts.setDisabled(b_valida1);
        txt_nroCtaDepCts.setDisabled(b_valida1);
        txt_glosaDepCts.setDisabled(b_valida1);
    }

    public void llenarCamposPagosCts(PersPagoDep objPerPagHab) {
        ib_correlpc.setValue(objPerPagHab.getPlcorrel_dep());
        txt_banDepCtsId.setValue(String.valueOf(objPerPagHab.getPlbanco()));
        txt_banDepCtsDes.setValue(objPerPagHab.getPlbanco_des());
        cb_tipCuentaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipCuentaDepCts, objPerPagHab.getPltipcta()));
        cb_tipMonedaDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipMonedaDepCts, objPerPagHab.getPlmoneda()));
        txt_nroCtaDepCts.setValue(objPerPagHab.getPlnrocta());
        //cb_tipPagoDepCts.setSelectedItem(Utilitarios.valorPorTexto(cb_tipPagoDepCts, objPerPagHab.getPltippago()));
        txt_glosaDepCts.setValue(objPerPagHab.getPlglosa());

    }

    //DERECHO HABIENTES
    public void habilitaBotonesDerHabiente(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevodh.setDisabled(b_valida1);
        tbbtn_btn_editardh.setDisabled(b_valida1);
        tbbtn_btn_eliminardh.setDisabled(b_valida1);
        tbbtn_btn_deshacerdh.setDisabled(b_valida2);
        tbbtn_btn_guardardh.setDisabled(b_valida2);

    }

    public void habilitaCamposDerHabiente(boolean b_valida1) {
        chk_dhEstado.setDisabled(b_valida1);

        chk_dhEst.setDisabled(b_valida1);
        chk_dhDiscap.setDisabled(b_valida1);

        txt_dhApeMat.setDisabled(b_valida1);
        txt_dhApePat.setDisabled(b_valida1);
        txt_dhNombres.setDisabled(b_valida1);
        cb_dhTipDoc.setDisabled(b_valida1);
        txt_dhNroDoc.setDisabled(b_valida1);
        d_dhFecNac.setDisabled(b_valida1);
        cb_dhSexo.setDisabled(b_valida1);
        cb_dhParentesto.setDisabled(b_valida1);
        d_dhFecAlta.setDisabled(b_valida1);
        cb_dhDocVf.setDisabled(b_valida1);
        txt_dhNroDocVf.setDisabled(b_valida1);
        txt_dhNrd.setDisabled(b_valida1);
        txt_dhIdMuni.setDisabled(b_valida1);
        txt_dhNacion.setDisabled(b_valida1);
        cb_dhSituacion.setDisabled(b_valida1);
        cb_dhTipBaja.setDisabled(b_valida1);
        d_dhFecBaja.setDisabled(b_valida1);
        cb_dhVia.setDisabled(b_valida1);
        txt_dhVia.setDisabled(b_valida1);
        chk_dhViveDom.setDisabled(b_valida1);
        cb_dhZona.setDisabled(b_valida1);
        txt_dhZona.setDisabled(b_valida1);
        txt_dhNro.setDisabled(b_valida1);
        txt_dhInt.setDisabled(b_valida1);
        txt_dhRefer.setDisabled(b_valida1);
        txt_dhCodUbi.setDisabled(b_valida1);

    }

    public void limpiarCamposDerHabiente() {
        txt_dhApeMat.setValue("");
        txt_dhApePat.setValue("");
        txt_dhNombres.setValue("");
        cb_dhTipDoc.setSelectedIndex(-1);
        txt_dhNroDoc.setValue("");
        d_dhFecNac.setValue(null);
        cb_dhSexo.setSelectedIndex(-1);
        cb_dhParentesto.setSelectedIndex(-1);
        d_dhFecAlta.setValue(null);
        cb_dhDocVf.setSelectedIndex(-1);
        txt_dhNroDocVf.setValue("");
        txt_dhNrd.setValue("");
        txt_dhIdMuni.setValue("");
        txt_dhDesMuni.setValue("");
        txt_dhNacion.setValue("");
        txt_dhDesNacion.setValue("");
        cb_dhSituacion.setSelectedIndex(-1);
        cb_dhTipBaja.setSelectedIndex(-1);
        d_dhFecBaja.setValue(null);
        cb_dhVia.setSelectedIndex(-1);
        txt_dhVia.setValue("");
        chk_dhViveDom.setChecked(false);
        chk_dhEst.setChecked(false);
        chk_dhDiscap.setChecked(false);
        cb_dhZona.setSelectedIndex(-1);
        txt_dhZona.setValue("");
        txt_dhNro.setValue("");
        txt_dhInt.setValue("");
        txt_dhRefer.setValue("");
        txt_dhCodUbi.setValue("");
        txt_dhDesUbi.setValue("");

    }

    public Object[][] getDerHabiente(ListModelList<DerHabiente> objListaAuxiliar) {
        ListModelList<DerHabiente> objListaDepurada;
        //Si el estado de la cabecera esta en modo Editar = E
        if (s_estado.equals("M")) {
            objListaDepurada = new ListModelList<DerHabiente>();
            //Limpiar registros eliminados del detalle
            for (int x = 0; x < objListaAuxiliar.size(); x++) {
                //Si el detalle es diferente del estado Q = QUERY - consulta
                if (!objListaAuxiliar.get(x).getInd_accion().equals("Q")) {
                    objListaDepurada.add(objListaAuxiliar.get(x));
                }
            }
        } else {
            objListaDepurada = objListaAuxiliar;
        }
        //Creamos el arreglo con los detalles
        // Object[][] listaCompRetDet = new Object[objListaDepurada.size()][29];
        Object[][] listaCompRetDet = new Object[objListaDepurada.size()][31];
        //Barreros la lista que contiene los datos a grabar
        for (int i = 0; i < objListaDepurada.size(); i++) {
            //Llenamos la lista con los datos
            listaCompRetDet[i][0] = objListaDepurada.get(i).getPldh_id();
            listaCompRetDet[i][1] = objListaDepurada.get(i).getPldh_estado();
            listaCompRetDet[i][2] = objListaDepurada.get(i).getPldh_tipdoc();
            listaCompRetDet[i][3] = objListaDepurada.get(i).getPldh_nrodoc();
            listaCompRetDet[i][4] = objListaDepurada.get(i).getPldh_apepat();
            listaCompRetDet[i][5] = objListaDepurada.get(i).getPldh_apemat();
            listaCompRetDet[i][6] = objListaDepurada.get(i).getPldh_nombres();
            listaCompRetDet[i][7] = new java.sql.Date(objListaDepurada.get(i).getPldh_fecnac().getTime());
            listaCompRetDet[i][8] = objListaDepurada.get(i).getPldh_sexo();
            listaCompRetDet[i][9] = objListaDepurada.get(i).getPldh_vinfam();
            listaCompRetDet[i][10] = objListaDepurada.get(i).getPldh_fecalt() == null ? null : new java.sql.Date(objListaDepurada.get(i).getPldh_fecalt().getTime());
            listaCompRetDet[i][11] = objListaDepurada.get(i).getPldh_tipdocvf();
            listaCompRetDet[i][12] = objListaDepurada.get(i).getPldh_nrocam();

            listaCompRetDet[i][13] = objListaDepurada.get(i).getPldh_nrd();
            listaCompRetDet[i][14] = objListaDepurada.get(i).getPldh_munpn();
            listaCompRetDet[i][15] = objListaDepurada.get(i).getPldh_nacion();
            listaCompRetDet[i][16] = objListaDepurada.get(i).getPldh_situa();
            listaCompRetDet[i][17] = objListaDepurada.get(i).getPldh_tipbaj();
            listaCompRetDet[i][18] = objListaDepurada.get(i).getPldh_fecbaj() == null ? null : new java.sql.Date(objListaDepurada.get(i).getPldh_fecbaj().getTime());

            listaCompRetDet[i][19] = objListaDepurada.get(i).getPldh_dirtipvia();
            listaCompRetDet[i][20] = objListaDepurada.get(i).getPldh_dirnomvia();
            listaCompRetDet[i][21] = objListaDepurada.get(i).getPldh_indom();
            listaCompRetDet[i][22] = objListaDepurada.get(i).getPldh_dirtipzon();
            listaCompRetDet[i][23] = objListaDepurada.get(i).getPldh_dirnomzon();
            listaCompRetDet[i][24] = objListaDepurada.get(i).getPldh_dirnumvia();
            listaCompRetDet[i][25] = objListaDepurada.get(i).getPldh_dirint();
            listaCompRetDet[i][26] = objListaDepurada.get(i).getPldh_dirref();
            listaCompRetDet[i][27] = objListaDepurada.get(i).getPldh_dirubigeo();
            listaCompRetDet[i][28] = objListaDepurada.get(i).getPldh_estudios();
            listaCompRetDet[i][29] = objListaDepurada.get(i).getPldh_discapacidad();
            listaCompRetDet[i][30] = objListaDepurada.get(i).getInd_accion();
        }
        return listaCompRetDet;
    }

    public void llenarListaDerHabiente() throws SQLException {
        objlstDerHabientes = null;
        objlstDerHabientes = objDaoPersonal.listaDerHabiente(objPersonal.getPltipdoc(), objPersonal.getPlnrodoc());
        lst_derhab.setModel(objlstDerHabientes);
    }

    public void llenarCamposDerHabiente() throws SQLException {
        ib_dhId.setValue(objDerHabiente.getPldh_id());
        chk_dhEstado.setChecked(objDerHabiente.getPldh_estado() == 1);
        txt_dhApePat.setValue(objDerHabiente.getPldh_apepat());
        txt_dhApeMat.setValue(objDerHabiente.getPldh_apemat());
        txt_dhNombres.setValue(objDerHabiente.getPldh_nombres());
        cb_dhTipDoc.setSelectedItem(Utilitarios.valorPorTexto(cb_dhTipDoc, objDerHabiente.getPldh_tipdoc()));
        txt_dhNroDoc.setValue(objDerHabiente.getPldh_nrodoc());
        d_dhFecNac.setValue(objDerHabiente.getPldh_fecnac());
        cb_dhSexo.setSelectedItem(Utilitarios.valorPorTexto(cb_dhSexo, objDerHabiente.getPldh_sexo()));

        cb_dhParentesto.setSelectedItem(Utilitarios.textoPorTexto(cb_dhParentesto, objDerHabiente.getPldh_vinfam()));
        d_dhFecAlta.setValue(objDerHabiente.getPldh_fecalt());
        if (objDerHabiente.getPldh_tipdocvf() != null) {
            cb_dhDocVf.setSelectedItem(Utilitarios.textoPorTexto(cb_dhDocVf, objDerHabiente.getPldh_tipdocvf()));
        }
        txt_dhNroDocVf.setValue(objDerHabiente.getPldh_nrocam());

        txt_dhNrd.setValue(objDerHabiente.getPldh_nrd());
        txt_dhIdMuni.setValue(objDerHabiente.getPldh_munpn());
        txt_dhDesMuni.setValue(objDerHabiente.getPldh_munpn_des());
        txt_dhNacion.setValue(objDerHabiente.getPldh_nacion());
        txt_dhDesNacion.setValue(objDerHabiente.getPldh_nacion_des());
        if (objDerHabiente.getPldh_situa() != null) {
            cb_dhSituacion.setSelectedItem(Utilitarios.textoPorTexto(cb_dhSituacion, objDerHabiente.getPldh_situa()));
        }
        if (objDerHabiente.getPldh_tipbaj() != null) {
            cb_dhTipBaja.setSelectedItem(Utilitarios.textoPorTexto(cb_dhTipBaja, objDerHabiente.getPldh_tipbaj()));
        }
        d_dhFecBaja.setValue(objDerHabiente.getPldh_fecbaj());

        if (objDerHabiente.getPldh_dirtipvia() != null) {
            cb_dhVia.setSelectedItem(Utilitarios.textoPorTexto(cb_dhVia, objDerHabiente.getPldh_dirtipvia()));
        }
        txt_dhVia.setValue(objDerHabiente.getPldh_dirnomvia());
        chk_dhViveDom.setChecked(objDerHabiente.getPldh_indom() == 1);

        if (objDerHabiente.getPldh_dirtipzon() != null) {
            cb_dhZona.setSelectedItem(Utilitarios.textoPorTexto(cb_dhZona, objDerHabiente.getPldh_dirtipzon()));
        }
        txt_dhZona.setValue(objDerHabiente.getPldh_dirnomzon());
        txt_dhNro.setValue(objDerHabiente.getPldh_dirnumvia());
        txt_dhInt.setValue(objDerHabiente.getPldh_dirint());
        txt_dhRefer.setValue(objDerHabiente.getPldh_dirref());
        txt_dhCodUbi.setValue(objDerHabiente.getPldh_dirubigeo());
        txt_dhDesUbi.setValue(objDerHabiente.getPldh_dirubigeo_des());
        chk_dhEst.setChecked(objDerHabiente.getPldh_estudios() == 1);
        chk_dhDiscap.setChecked(objDerHabiente.getPldh_discapacidad() == 1);
    }

    public void limpiarListaDerhabiente() {
        objlstDerHabientes = null;
        objlstDerHabientes = new ListModelList<DerHabiente>();
        lst_derhab.setModel(objlstDerHabientes);
    }

    //----------------
    public String verificar() {
        String s_valor = "";
        if (txt_apePat.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el apellido paterno";
            foco = "apepat";
        } else if (txt_apeMat.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el apellido materno";
            foco = "apemat";
        } else if (txt_nombres.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el nombre";
            foco = "nombres";
        } else if (txt_nacionId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese la nación";
            foco = "nacion";
        } else if (cb_tipDoc.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el tipo de documento";
            foco = "tipdoc";
        } else if (txt_nroDoc.getValue().isEmpty()) {
            s_valor = "Por favor seleccione el número de documento";
            foco = "nrodoc";
        } else if (d_fecNac.getValue() == null) {
            s_valor = "Por favor ingrese la fecha de nacimiento";
            foco = "fecnac";
        } else if (cb_sexo.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el sexo";
            foco = "sexo";
        } else if (cb_estCivil.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el estado civil";
            foco = "estcivil";
        } /*else if (txt_grupSang.getValue().isEmpty()) {
         s_valor = "Por favor ingrese el grupo sanguíneo";
         foco = "grupsang";
         }*/ /*else if (txt_telefono.getValue().isEmpty()) {
         s_valor = "Por favor ingrese el teléfono de casa";
         foco = "telcasa";
         } */ else if (txt_movil.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el teléfono móvil";
            foco = "telmovil";
        } /*else if (txt_correo.getValue().isEmpty()) {
         s_valor = "Por favor ingrese el correo";
         foco = "correo";
         } */ else if (txt_nivEduId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el nivel educativo";
            foco = "nivedu";
        } else if (txt_ocupaId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese la ocupación";
            foco = "ocupacion";
            //DOMICILIO
        } else if (cb_condicion.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la condición";
            foco = "condicion";
        } else if (txt_ubigeoId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el ubigeo";
            foco = "ubigeo";
        } else if (cb_via.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la vía";
            foco = "cbvia";
        } else if (txt_via.getValue().isEmpty()) {
            s_valor = "Por favor ingrese la vía";
            foco = "dirvia";
        } /*else if (txt_nro.getValue().isEmpty()) {
         s_valor = "Por favor ingrese el número de dirección";
         foco = "dirnro";
         } else if (txt_int.getValue().isEmpty()) {
         s_valor = "Por favor ingrese el int. de dirección";
         foco = "dirint";
         } */ else if (cb_zona.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la zona";
            foco = "cbzona";
        } else if (txt_zona.getValue().isEmpty()) {
            s_valor = "Por favor ingrese la zona";
            foco = "dirzona";
        } else if (txt_direccion.getValue().isEmpty()) {
            s_valor = "Por favor ingrese la dirección";
        } else if (db_dirlatx.getValue() == null || db_dirlony.getValue() == null) {
            s_valor = "Por favor ingrese las coordenadas";
            //DATOS LABORALES
        } else if (d_fecIng.getValue() == null) {
            s_valor = "Por favor ingrese la fecha de ingreso";
            foco = "fecing";
            /*|| cb_sucursal.getSelectedIndex() == 0*/
        } else if (cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().equals("0")) {
            s_valor = "Por favor seleccione la sucursal";
            foco = "sucursal";
        } else if (txt_areaId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el área";
            foco = "area";
        } else if (txt_ccostoId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el centro de costo";
            foco = "ccosto";
        } else if (cb_categoria.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la categoría";
            foco = "categoria";
        } else if (txt_tipTrabId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el tipo de trabajador";
            foco = "tiptrab";
        } else if (txt_horarioId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el horario";
            foco = "horario";
        } else if (txt_cargoId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el cargo";
            foco = "cargo";
        } else if (cb_sitEsp.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la situación especial";
            foco = "sitesp";
        } else if (txt_tipContId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el tipo de contrato";
            foco = "tipcont";
        } else if (cb_tipSueldo.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el tipo de sueldo";
            foco = "tipsueldo";
        } else if (cb_perRem.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione la periodicidad remunerativa";
            foco = "perrem";
        } /*else if (ib_hExtras.getValue() == null) {
         s_valor = "Por favor ingrese las horas extras";
         foco = "hextra";
         } else if (ib_hExtras.getValue() > 40) {
         s_valor = "El máximo de horas extras es 40";
         foco = "hextra";
         //DATOS APORTACION
         }*/ else if (txt_regPenId.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el régimen pensionario";
            foco = "regpen";
        } else if (d_fecIngRp.getValue() == null) {
            s_valor = "Por favor ingrese la fecha de ingreso del régimen";
            foco = "fecingrp";
        } /*else if (d_fecCesRp.getValue() != null && !txt_presSaludId.getValue().isEmpty()) {
         s_valor = "Por favor verificar la situación y fecha de cese";
         foco = "pressalud";
         }*//*else if (cb_tipPen.getSelectedIndex() == -1) {
         s_valor = "Por favor seleccione el tipo de pensión";
         foco = "tippen";
         } else if (txt_presSaludId.getValue().isEmpty()) {
         s_valor = "Por favor ingrese la entidad pres. salud";
         foco = "pressalud";
         }*/ else if (cb_sctrSalud.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el sctr salud";
            foco = "sctrsal";
        } else if (cb_sctrPension.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el sctr pensión";
            foco = "sctrpen";
            //DATOS ADJUNTOS
        } else if (txt_adj_antpol.getValue().isEmpty() || txt_adj_cerdom.getValue().isEmpty() || txt_adj_curvit.getValue().isEmpty()
                || txt_adj_croquis.getValue().isEmpty() || txt_adj_dni.getValue().isEmpty() || txt_adj_firma.getValue().isEmpty()
                || txt_adj_foto.getValue().isEmpty() || txt_adj_recibo.getValue().isEmpty()) {
            s_valor = "Por favor ingresar los adjunto obligatorios";
            foco = "adjunto";
        }

        if (!s_estado_ph.equals("Q")) {
            if (txt_banDepHabId.getValue().isEmpty()) {
                s_valor = "Por favor ingrese el banco de dep. haberes";
                foco = "bandh";
            } else if (cb_tipCuentaDepHab.getSelectedIndex() == -1) {
                s_valor = "Por favor seleccione el tipo de cuenta de dep. haberes";
                foco = "tipctadh";
            } else if (cb_tipMonedaDepHab.getSelectedIndex() == -1) {
                s_valor = "Por favor seleccione el tipo de moneda de dep. haberes";
                foco = "tipmondh";
            } else if (txt_nroCtaDepHab.getValue().isEmpty()) {
                s_valor = "El número de dígitos de la cuenta de dep. haberes ";
                foco = "nroctadh";
            } else if (cb_tipPagoDepHab.getSelectedIndex() == -1) {
                s_valor = "Por favor seleccione el tipo de pago de dep. haberes";
                foco = "tippagdh";
            }
        }

        if (!s_estado_pc.equals("Q")) {
            if (txt_banDepCtsId.getValue().isEmpty()) {
                s_valor = "Por favor ingrese el banco de dep. cts";
                foco = "bandc";
            } else if (cb_tipCuentaDepCts.getSelectedIndex() == -1) {
                s_valor = "Por favor seleccione el tipo de cuenta de dep. cts";
                foco = "tipctadc";
            } else if (cb_tipMonedaDepCts.getSelectedIndex() == -1) {
                s_valor = "Por favor seleccione el tipo de moneda de dep. cts";
                foco = "tipmondc";
            } else if (txt_nroCtaDepCts.getValue().isEmpty()) {
                s_valor = "Por favor ingrese el número de cuenta de dep. cts";
                foco = "nroctadc";
            }
        }

        return s_valor;
    }

    public void validaFoco() {
        if (foco.equals("apepat")) {
            tab_datosper.setSelected(true);
            txt_apePat.focus();
        } else if (foco.equals("apemat")) {
            tab_datosper.setSelected(true);
            txt_apeMat.focus();
        } else if (foco.equals("nombres")) {
            tab_datosper.setSelected(true);
            txt_nombres.focus();
        } else if (foco.equals("nacion")) {
            tab_datosper.setSelected(true);
            txt_nacionId.focus();
        } else if (foco.equals("tipdoc")) {
            tab_datosper.setSelected(true);
            cb_tipDoc.focus();
        } else if (foco.equals("nrodoc")) {
            tab_datosper.setSelected(true);
            txt_nroDoc.focus();
        } else if (foco.equals("fecnac")) {
            tab_datosper.setSelected(true);
            d_fecNac.focus();
        } else if (foco.equals("sexo")) {
            tab_datosper.setSelected(true);
            cb_sexo.focus();
        } else if (foco.equals("estcivil")) {
            tab_datosper.setSelected(true);
            cb_estCivil.focus();
        } /*else if (foco.equals("grupsang")) {
         tab_datosper.setSelected(true);
         txt_grupSang.focus();
         }
         *//* else if (foco.equals("telcasa")) {
         tab_datosper.setSelected(true);
         txt_telefono.focus();
         }*/ else if (foco.equals("telmovil")) {
            tab_datosper.setSelected(true);
            txt_movil.focus();
        } /*else if (foco.equals("correo")) {
         tab_datosper.setSelected(true);
         txt_correo.focus();
         } */ else if (foco.equals("nivedu")) {
            tab_datosper.setSelected(true);
            txt_nivEduId.focus();
        } else if (foco.equals("ocupacion")) {
            tab_datosper.setSelected(true);
            txt_ocupaId.focus();
        } else if (foco.equals("condicion")) {
            tab_datosper.setSelected(true);
            cb_condicion.focus();
        } else if (foco.equals("ubigeo")) {
            tab_datosper.setSelected(true);
            txt_ubigeoId.focus();
        } else if (foco.equals("cbvia")) {
            tab_datosper.setSelected(true);
            cb_via.focus();
        } else if (foco.equals("dirvia")) {
            tab_datosper.setSelected(true);
            txt_via.focus();
        } else if (foco.equals("dirnro")) {
            tab_datosper.setSelected(true);
            txt_nro.focus();
        } else if (foco.equals("dirint")) {
            tab_datosper.setSelected(true);
            txt_int.focus();
        } else if (foco.equals("cbzona")) {
            tab_datosper.setSelected(true);
            cb_zona.focus();
        } else if (foco.equals("dirzona")) {
            tab_datosper.setSelected(true);
            txt_zona.focus();
        } else if (foco.equals("direccion")) {
            tab_datosper.setSelected(true);
            txt_direccion.focus();
            //DATOS LABORALES
        } else if (foco.equals("fecing")) {
            tab_datoslab.setSelected(true);
            d_fecIng.focus();
        } else if (foco.equals("sucursal")) {
            tab_datoslab.setSelected(true);
            cb_sucursal.focus();
        } else if (foco.equals("area")) {
            tab_datoslab.setSelected(true);
            txt_areaId.focus();
        } else if (foco.equals("ccosto")) {
            tab_datoslab.setSelected(true);
            txt_ccostoId.focus();
        } else if (foco.equals("categoria")) {
            tab_datoslab.setSelected(true);
            cb_categoria.focus();
        } else if (foco.equals("tiptrab")) {
            tab_datoslab.setSelected(true);
            txt_tipTrabId.focus();
        } else if (foco.equals("horario")) {
            tab_datoslab.setSelected(true);
            txt_horarioId.focus();
        } else if (foco.equals("cargo")) {
            tab_datoslab.setSelected(true);
            txt_cargoId.focus();
        } else if (foco.equals("sitesp")) {
            tab_datoslab.setSelected(true);
            cb_sitEsp.focus();
        } else if (foco.equals("tipcont")) {
            tab_datoslab.setSelected(true);
            txt_tipContId.focus();
        } else if (foco.equals("tipsueldo")) {
            tab_datoslab.setSelected(true);
            cb_tipSueldo.focus();
        } else if (foco.equals("perrem")) {
            tab_datoslab.setSelected(true);
            cb_perRem.focus();
        }/* else if (foco.equals("hextra")) {
         tab_datoslab.setSelected(true);
         ib_hExtras.focus();
         //DATOS APORTACION
         }*/ else if (foco.equals("regpen")) {
            tab_datoslab.setSelected(true);
            txt_regPenId.focus();
        } else if (foco.equals("fecingrp")) {
            tab_datoslab.setSelected(true);
            d_fecIngRp.focus();
        } else if (foco.equals("tippen")) {
            tab_datoslab.setSelected(true);
            cb_tipPen.focus();
        } else if (foco.equals("pressalud")) {
            tab_datoslab.setSelected(true);
            txt_presSaludId.focus();
        } else if (foco.equals("sctrsal")) {
            tab_datoslab.setSelected(true);
            cb_sctrSalud.focus();
        } else if (foco.equals("sctrpen")) {
            tab_datoslab.setSelected(true);
            cb_sctrPension.focus();
        } else if (foco.equals("adjunto")) {
            tab_adjuntos.setSelected(true);
            //PAGOS HABERES
        } else if (foco.equals("bandh")) {
            tab_pagos.setSelected(true);
            txt_banDepHabId.focus();
        } else if (foco.equals("tipctadh")) {
            tab_pagos.setSelected(true);
            cb_tipCuentaDepHab.focus();
        } else if (foco.equals("tipmondh")) {
            tab_pagos.setSelected(true);
            cb_tipMonedaDepHab.focus();
        } else if (foco.equals("nroctadh")) {
            tab_pagos.setSelected(true);
            txt_nroCtaDepHab.focus();
        } else if (foco.equals("tippagdh")) {
            tab_pagos.setSelected(true);
            cb_tipPagoDepHab.focus();
            //PAGOS CTS
        } else if (foco.equals("bandc")) {
            tab_pagos.setSelected(true);
            txt_banDepCtsId.focus();
        } else if (foco.equals("tipctadc")) {
            tab_pagos.setSelected(true);
            cb_tipCuentaDepCts.focus();
        } else if (foco.equals("tipmondc")) {
            tab_pagos.setSelected(true);
            cb_tipMonedaDepCts.focus();
        } else if (foco.equals("nroctadc")) {
            tab_pagos.setSelected(true);
            txt_nroCtaDepCts.focus();
        }

    }

    public String verificarDerHabiente() {
        String s_valor = "";
        if (txt_dhApePat.getValue().trim().isEmpty()) {
            s_valor = "Por favor ingrese el apellido paterno";
            focoDerHab = "apepat";
        } else if (txt_dhApeMat.getValue().trim().isEmpty()) {
            s_valor = "Por favor ingrese el apellido materno";
            focoDerHab = "apemat";
        } else if (txt_dhNombres.getValue().trim().isEmpty()) {
            s_valor = "Por favor ingrese el nombre";
            focoDerHab = "nombres";
        } else if (cb_dhTipDoc.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el tipo de documento";
            focoDerHab = "tipdoc";
        } else if (txt_dhNroDoc.getValue().isEmpty()) {
            s_valor = "Por favor seleccione el número de documento";
            focoDerHab = "nrodoc";
        } else if (d_dhFecNac.getValue() == null) {
            s_valor = "Por favor ingrese la fecha de nacimiento";
            focoDerHab = "fecnac";
        } else if (cb_dhSexo.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el sexo";
            focoDerHab = "sexo";
        } else if (cb_dhParentesto.getSelectedIndex() == -1) {
            s_valor = "Por favor seleccione el parentesco";
            focoDerHab = "parentesco";
        }
        return s_valor;
    }

    public void validaFocoDerHabiente() {
        if (focoDerHab.equals("apepat")) {
            txt_dhApePat.focus();
        } else if (focoDerHab.equals("apemat")) {
            txt_dhApeMat.focus();
        } else if (focoDerHab.equals("nombres")) {
            txt_dhNombres.focus();
        } else if (focoDerHab.equals("tipdoc")) {
            cb_dhTipDoc.focus();
        } else if (focoDerHab.equals("nrodoc")) {
            txt_dhNroDoc.focus();
        } else if (focoDerHab.equals("fecnac")) {
            d_dhFecNac.focus();
        } else if (focoDerHab.equals("sexo")) {
            cb_dhSexo.focus();
        } else if (focoDerHab.equals("parentesco")) {
            cb_dhParentesto.focus();
        }
    }

    public String validaNumCtaBan() throws SQLException {
        String mensaje = "";
        int valhab;
        int valcts;
        if (!s_estado_ph.equals("Q")) {
            valhab = objDaoPersonal.validaDigitosCtaBan(Integer.parseInt(txt_banDepHabId.getValue()), txt_nroCtaDepHab.getValue());
            if (valhab == 1) {
                mensaje = "El número de dígitos de la cuenta de haberes es incorrecta";
                foco = "nroctadh";
            }
        }

        if (!s_estado_pc.equals("Q")) {
            valcts = objDaoPersonal.validaDigitosCtaBan(Integer.parseInt(txt_banDepCtsId.getValue()), txt_nroCtaDepCts.getValue());
            if (valcts == 1) {
                mensaje = "El número de dígitos de la cuenta cts es incorrecta";
                foco = "nroctadc";
            }
        }

        return mensaje;
    }

    public Personal generaDatosPer() {

        String apepat = txt_apePat.getValue().toUpperCase();
        String apemat = txt_apeMat.getValue().toUpperCase();
        int estado = chk_estado.isChecked() ? 1 : 2;
        String nombres = txt_nombres.getValue().toUpperCase();
        String nacion = txt_nacionId.getValue();
        int tipdoc = cb_tipDoc.getSelectedItem().getValue();
        String nrodoc = txt_nroDoc.getValue();
        Date fecnac = d_fecNac.getValue();
        int sexo = Integer.parseInt(cb_sexo.getSelectedItem().getValue().toString());
        String estcivil = cb_estCivil.getSelectedItem().getValue();
        String grupsang = txt_grupSang.getValue().toUpperCase();
        String telefono = txt_telefono.getValue();
        String telmovil = txt_movil.getValue();
        String correo = txt_correo.getValue().toUpperCase();
        int discapacidad = chk_discap.isChecked() ? 1 : 0;
        String nivedu = txt_nivEduId.getValue();
        String ocupacion = txt_ocupaId.getValue();
        //Domicilio
        int condicion = Integer.parseInt(cb_condicion.getSelectedItem().getValue().toString());
        String ubigeo = txt_ubigeoId.getValue();
        String cbvia = cb_via.getSelectedItem().getValue();
        String dirvia = txt_via.getValue().toUpperCase();
        String dirnro = txt_nro.getValue().toUpperCase();
        String dirint = txt_int.getValue().toUpperCase();
        String cbzona = cb_zona.getSelectedItem().getValue();
        String dirzona = txt_zona.getValue().toUpperCase();
        String direccion = txt_direccion.getValue().toUpperCase();
        String referencia = txt_referencia.getValue().toUpperCase();
        double dirlatitud = db_dirlatx.getValue();
        double dirlongitud = db_dirlony.getValue();

        return new Personal(tipdoc, nrodoc, estado, apepat, apemat, nombres, direccion, telefono, fecnac, telmovil, grupsang, sexo, nacion,
                estcivil, nivedu, discapacidad, condicion, correo, ocupacion, cbvia, dirvia, dirnro, dirint, cbzona, dirzona, referencia, ubigeo,
                dirlatitud, dirlongitud);
    }

    public DatosLaborales generaDatosLab() {

        String indsql = s_estado_dl;
        Date fecing = d_fecIng.getValue();
        Date fecces = d_fecCese.getValue();

        String motcese = d_fecCese.getValue() == null ? "" : cb_motivoCese.getSelectedItem().getValue().toString();
        String detcese = d_fecCese.getValue() == null ? "" : txt_ceseDet.getValue().toUpperCase();
        String obscese = d_fecCese.getValue() == null ? "" : txt_ceseObs.getValue().toUpperCase();

        String codemp = txt_codInterno.getValue().toUpperCase();
        int sucursal = cb_sucursal.getSelectedItem().getValue();
        //int sucursal = Integer.parseInt(cb_sucursal.getSelectedItem().getValue().toString());
        String area = txt_areaId.getValue().toUpperCase();
        String ccosto = txt_ccostoId.getValue().toUpperCase();
        String categoria = cb_categoria.getSelectedItem().getValue();
        String tiptrab = txt_tipTrabId.getValue().toUpperCase();
        String horario = txt_horarioId.getValue().toUpperCase();
        String cargo = txt_cargoId.getValue().toUpperCase();
        int sitesp = Integer.parseInt(cb_sitEsp.getSelectedItem().getValue().toString());
        String tipcont = txt_tipContId.getValue().toUpperCase();
        int sujctrlinm = chk_sujCtrlInm.isChecked() ? 1 : 0;
        int tipsueldo = Integer.parseInt(cb_tipSueldo.getSelectedItem().getValue().toString());
        int perrem = Integer.parseInt(cb_perRem.getSelectedItem().getValue().toString());
        // int hextra = ib_hExtras.getValue();
        int perutil = chk_utilidad.isChecked() ? 1 : 0;
        int excquinta = chk_excQuinta.isChecked() ? 1 : 0;
        int otrosquinta = chk_otrosQuinta.isChecked() ? 1 : 0;
        int essalud = chk_essalud.isChecked() ? 1 : 0;
        String carnetssp = txt_carnetSsp.getValue().toUpperCase();
        int sindicalizado = chk_sindicalizado.isChecked() ? 1 : 0;
        int pensionista = chk_pensionista.isChecked() ? 1 : 0;

        return new DatosLaborales(indsql, sucursal, codemp, area, ccosto, categoria, tiptrab, horario, cargo,
                sitesp, tipcont, sujctrlinm, tipsueldo, perrem, perutil, excquinta, otrosquinta, essalud,
                carnetssp, sindicalizado, pensionista, fecing, fecces, motcese, detcese, obscese);
    }

    public PersAportacion generaAportacion() {

        String indsql = s_estado_ap;
        String regpen = txt_regPenId.getValue().toUpperCase();
        Date fecingrp = d_fecIngRp.getValue();
        String tippen = cb_tipPen.getSelectedIndex() == -1 ? "" : cb_tipPen.getSelectedItem().getValue().toString();
        String afpid = txt_afpId.getValue().toUpperCase();
        String afpcarnet = txt_carnetAfp.getValue().trim().toUpperCase();
        int commix = chk_comMix.isChecked() ? 1 : 0;
        String entpressal = txt_presSaludId.getValue().toUpperCase();
        String situacion = txt_sitEpsId.getValue().toUpperCase();
        Date fecbaja = d_fecBajaEs.getValue();
        int sctrsal = Integer.parseInt(cb_sctrSalud.getSelectedItem().getValue().toString());
        int sctrpen = Integer.parseInt(cb_sctrPension.getSelectedItem().getValue().toString());

        return new PersAportacion(indsql, regpen, fecingrp, tippen, afpid, afpcarnet, commix,
                entpressal, situacion, fecbaja, sctrsal, sctrpen);
    }

    public PersPagoDep generaPagoHaberes() {

        String indsql = s_estado_ph;
        int correl = ib_correlph.getValue() == null ? 0 : ib_correlph.getValue();
        int tipdep = 1;
        int banco = txt_banDepHabId.getValue().isEmpty() ? 0 : Integer.parseInt(txt_banDepHabId.getValue());
        int tipcta = cb_tipCuentaDepHab.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_tipCuentaDepHab.getSelectedItem().getValue().toString());
        int moneda = cb_tipMonedaDepHab.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_tipMonedaDepHab.getSelectedItem().getValue().toString());
        String nrocta = txt_nroCtaDepHab.getValue().isEmpty() ? "" : txt_nroCtaDepHab.getValue().toUpperCase();
        int tippago = cb_tipPagoDepHab.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_tipPagoDepHab.getSelectedItem().getValue().toString());
        String glosa = txt_glosaDepHab.getValue();

        return new PersPagoDep(tipdep, banco, tipcta, correl, moneda, nrocta, tippago, glosa, indsql);
    }

    public PersPagoDep generaPagoCts() {

        String indsql = s_estado_pc;
        int correl = ib_correlpc.getValue() == null ? 0 : ib_correlpc.getValue();
        int tipdep = 2;
        int banco = txt_banDepCtsId.getValue().isEmpty() ? 0 : Integer.parseInt(txt_banDepCtsId.getValue());
        int tipcta = cb_tipCuentaDepCts.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_tipCuentaDepCts.getSelectedItem().getValue().toString());
        int moneda = cb_tipMonedaDepCts.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_tipMonedaDepCts.getSelectedItem().getValue().toString());
        String nrocta = txt_nroCtaDepCts.getValue().isEmpty() ? "" : txt_nroCtaDepCts.getValue().toUpperCase();
        int tippago = 0;
        String glosa = txt_glosaDepCts.getValue();

        return new PersPagoDep(tipdep, banco, tipcta, correl, moneda, nrocta, tippago, glosa, indsql);
    }

    public DerHabiente generaDerHabiente() {
        DerHabiente objDerHab = new DerHabiente();
        objDerHab.setPldh_id(ib_dhId.getValue() == null ? 0 : ib_dhId.getValue());
        objDerHab.setPldh_estado(chk_dhEstado.isChecked() ? 1 : 2);
        objDerHab.setPldh_tipdoc(Integer.parseInt(cb_dhTipDoc.getSelectedItem().getValue().toString()));
        objDerHab.setPldh_tipdoc_des(cb_dhTipDoc.getValue().toString());
        objDerHab.setPldh_nrodoc(txt_dhNroDoc.getValue().toUpperCase());
        objDerHab.setPldh_apepat(txt_dhApePat.getValue().toUpperCase());
        objDerHab.setPldh_apemat(txt_dhApeMat.getValue().toUpperCase());
        objDerHab.setPldh_nombres(txt_dhNombres.getValue().toUpperCase());
        objDerHab.setPldh_fecnac(d_dhFecNac.getValue());
        objDerHab.setPldh_sexo(Integer.parseInt(cb_dhSexo.getSelectedItem().getValue().toString()));
        objDerHab.setPldh_sexo_des(cb_dhSexo.getValue().toString().substring(0, 1));
        objDerHab.setPldh_vinfam(cb_dhParentesto.getSelectedItem().getValue().toString());
        objDerHab.setPldh_vinfam_des(cb_dhParentesto.getValue().toString());
        objDerHab.setPldh_fecalt(d_dhFecAlta.getValue());
        objDerHab.setPldh_tipdocvf(cb_dhDocVf.getSelectedIndex() == -1 ? null : cb_dhDocVf.getSelectedItem().getValue().toString());
        objDerHab.setPldh_nrocam(txt_dhNroDocVf.getValue().toUpperCase());

        objDerHab.setPldh_nrd(txt_dhNrd.getValue().toUpperCase());
        objDerHab.setPldh_munpn(txt_dhIdMuni.getValue().toUpperCase());
        objDerHab.setPldh_nacion(txt_dhNacion.getValue().toUpperCase());
        objDerHab.setPldh_situa(cb_dhSituacion.getSelectedItem().getValue().toString());
        objDerHab.setPldh_situa_des(cb_dhSituacion.getValue().toString());
        objDerHab.setPldh_tipbaj(cb_dhTipBaja.getSelectedIndex() == -1 ? null : cb_dhTipBaja.getSelectedItem().getValue().toString());
        objDerHab.setPldh_fecbaj(d_dhFecBaja.getValue());

        //jr 16012018
        objDerHab.setPldh_estudios(chk_dhEst.isChecked() ? 1 : 0);
        objDerHab.setPldh_discapacidad(chk_dhDiscap.isChecked() ? 1 : 0);

        objDerHab.setPldh_dirtipvia(cb_dhVia.getSelectedIndex() == -1 ? null : cb_dhVia.getSelectedItem().getValue().toString());
        objDerHab.setPldh_dirnomvia(txt_dhVia.getValue().toUpperCase());
        objDerHab.setPldh_indom(chk_dhViveDom.isChecked() ? 1 : 2);
        objDerHab.setPldh_dirtipzon(cb_dhZona.getSelectedIndex() == -1 ? null : cb_dhZona.getSelectedItem().getValue().toString());
        objDerHab.setPldh_dirnomzon(txt_dhZona.getValue().toUpperCase());
        objDerHab.setPldh_dirnumvia(txt_dhNro.getValue());
        objDerHab.setPldh_dirint(txt_dhInt.getValue());
        objDerHab.setPldh_dirref(txt_dhRefer.getValue().toUpperCase());
        objDerHab.setPldh_dirubigeo(txt_dhCodUbi.getValue());
        objDerHab.setPldh_dirubigeo_des(txt_dhDesUbi.getValue());

        return objDerHab;
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void cargarImagen(String ruta, Div img) throws IOException {
        File existe = new File(ruta);
        if (existe.exists()) {
            //if (ruta != null) {
            Image imagen = new Image();
            imagen.setContent(new AImage(ruta));
            imagen.setParent(img);
            imagen.setStyle("border: 2px solid #2E9AFE; width : 194px; height : 250px; ");
            //FileInputStream fis = new FileInputStream(ruta);
            //}
        }
    }

    public void limpiaCargaAdjunto() {
        cargaAntPol = false;
        cargaCerDom = false;
        cargaCurVit = false;
        cargaCroquis = false;
        cargaDni = false;
        cargaFirma = false;
        cargaFoto = false;
        cargaRecibo = false;
        cargaOtros = false;
    }

    public String validaFechaIngreso() {

        String mensaje;

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(d_fecIng.getValue());
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int dia_sem = calendar.get(Calendar.DAY_OF_WEEK);

        //SI DIA ES MAYOR A 27 O ES SÁBADO O DOMINGO
        if (dia > 27) {
            mensaje = "La fecha de ingreso debe ser menor al día 28";
        } else if (dia_sem == 7 || dia_sem == 1) {
            mensaje = "El día de ingreso debe ser de Lunes a Viernes";
        } else {
            mensaje = "";
        }

        return mensaje;
    }

    public String validaFechaCese() {
        String mensaje;

        if (d_fecCese.getValue() != null) {
            if (d_fecCese.getValue().before(d_fecIng.getValue())) {
                mensaje = "La fecha de cese debe ser posterior a la fecha de ingreso";
            } else if (cb_motivoCese.getSelectedIndex() == -1) {
                mensaje = "Por favor seleccionar el motivo de cese";
            } else if (txt_ceseDet.getValue().isEmpty()) {
                mensaje = "Por favor ingresar el detalle de cese";
            } else if (txt_ceseObs.getValue().isEmpty()) {
                mensaje = "Por favor ingresar la observacion de cese";
            } else {
                mensaje = "";
            }

        } else {
            mensaje = "";
        }

        return mensaje;

    }

    public boolean existeDocumento(int tipo, String nrodoc) throws SQLException {
        boolean ind = false;

        if (s_estado_dh.equals("N")) {
            for (int i = 0; i < objlstDerHabientes.size(); i++) {
                if (objlstDerHabientes.get(i).getPldh_tipdoc() == tipo
                        && objlstDerHabientes.get(i).getPldh_nrodoc().equals(nrodoc)) {
                    ind = true;
                }
            }
        } else {

            if (objDerHabiente.getPldh_tipdoc() == tipo && objDerHabiente.getPldh_nrodoc().equals(nrodoc)) {
                ind = false;
            } else {

                for (int i = 0; i < objlstDerHabientes.size(); i++) {
                    if (objlstDerHabientes.get(i).getPldh_tipdoc() == tipo
                            && objlstDerHabientes.get(i).getPldh_nrodoc().equals(nrodoc)) {
                        ind = true;
                    }
                }

            }

        }
        return ind;
    }

    public boolean validaNumDoc(int tipo, String nrodoc) throws SQLException {
        boolean ind = false;
        ind = objDaoPersonal.validaNumDoc(tipo, nrodoc) == 1;
        return ind;
    }

    @Listen("onUpload=#tbbtn_adjcar_antpol")
    public void onUploadAntPol(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaAntPol = event.getMedia();
            if (mediaAntPol instanceof Media) {
                //img_vehiculo.getChildren().clear();
                //Image foto_vehiculo = new Image();

                if (mediaAntPol.getName().contains(".pdf") || mediaAntPol.getName().contains(".PDF")) {

                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_antpol.setValue(rutaFileAntPol + documento + ".pdf");
                    archivoAntPol = new File(txt_adj_antpol.getValue());
                    cargaAntPol = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_cerdom")
    public void onUploadCerDom(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaCerDom = event.getMedia();
            if (mediaCerDom instanceof Media) {

                if (mediaCerDom.getName().contains(".pdf") || mediaCerDom.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_cerdom.setValue(rutaFileCerDom + documento + ".pdf");
                    archivoCerDom = new File(txt_adj_cerdom.getValue());
                    cargaCerDom = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_curvit")
    public void onUploadCurVit(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaCurVit = event.getMedia();
            if (mediaCurVit instanceof Media) {
                if (mediaCurVit.getName().contains(".pdf") || mediaCurVit.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_curvit.setValue(rutaFileCurVit + documento + ".pdf");
                    archivoCurVit = new File(txt_adj_curvit.getValue());
                    cargaCurVit = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_croquis")
    public void onUploadCroquis(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaCroquis = event.getMedia();
            if (mediaCroquis instanceof Media) {
                if (mediaCroquis.getName().contains(".pdf") || mediaCroquis.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_croquis.setValue(rutaFileCroquis + documento + ".pdf");
                    archivoCroquis = new File(txt_adj_croquis.getValue());
                    cargaCroquis = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_dni")
    public void onUploadDni(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaDni = event.getMedia();
            if (mediaDni instanceof Media) {
                if (mediaDni.getName().contains(".pdf") || mediaDni.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_dni.setValue(rutaFileDni + documento + ".pdf");
                    archivoDni = new File(txt_adj_dni.getValue());
                    cargaDni = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_firma")
    public void onUploadFirma(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaFirma = event.getMedia();
            if (mediaFirma instanceof Media) {
                if (mediaFirma.getName().contains(".pdf") || mediaFirma.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_firma.setValue(rutaFileFirma + documento + ".pdf");
                    archivoFirma = new File(txt_adj_firma.getValue());
                    cargaFirma = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_adjcar_foto")
    public void capturarFoto() throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Clients.evalJavaScript("tomar_foto()");
            String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
            txt_adj_foto.setValue(rutaFileFoto + documento + ".JPEG");
            cargaFoto = true;

            //resultado
            /*mediaFoto = event.getMedia();
             if (mediaFoto instanceof Media) {
             if (mediaFoto.getName().contains(".jpeg")) {
             String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
             txt_adj_foto.setValue(rutaFileFoto + documento + ".jpeg");
             archivoFoto = new File(txt_adj_foto.getValue());
             cargaFoto = true;
             } else {
             Messagebox.show("NO SE CARGÓ. Seleccionar documento en jpeg", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
             }
             }*/
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_recibo")
    public void onUploadRecibo(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaRecibo = event.getMedia();
            if (mediaRecibo instanceof Media) {
                if (mediaRecibo.getName().contains(".pdf") || mediaRecibo.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_recibo.setValue(rutaFileRecibo + documento + ".pdf");
                    archivoRecibo = new File(txt_adj_recibo.getValue());
                    cargaRecibo = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_otros")
    public void onUploadOtros(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaOtros = event.getMedia();
            if (mediaOtros instanceof Media) {
                if (mediaOtros.getName().contains(".pdf") || mediaOtros.getName().contains(".PDF")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_otros.setValue(rutaFileOtros + documento + ".pdf");
                    archivoOtros = new File(txt_adj_otros.getValue());
                    cargaOtros = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onUpload=#tbbtn_adjcar_fotofile")
    public void onUploadFotoFromFile(UploadEvent event) throws Exception {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            mediaFotoFromFile = event.getMedia();
            if (mediaFotoFromFile instanceof Media) {
                if (mediaFotoFromFile.getName().contains(".JPEG") || mediaFotoFromFile.getName().contains(".jpeg")
                        || mediaFotoFromFile.getName().contains(".JPG") || mediaFotoFromFile.getName().contains(".jpg")) {
                    String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
                    txt_adj_foto.setValue(rutaFileFoto + documento + ".JPEG");
                    archivoFotoFromFile = new File(txt_adj_foto.getValue());
                    cargaFotoFromFile = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en formato JPEG", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_adjpdf_antpol")
    public void mostrarPdfAntPol() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileAntPol + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_cerdom")
    public void mostrarPdfCerDom() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileCerDom + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_curvit")
    public void mostrarPdfCurVit() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileCurVit + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_croquis")
    public void mostrarPdfCroquis() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileCroquis + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_dni")
    public void mostrarPdfDni() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileDni + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_firma")
    public void mostrarPdfFirma() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileFirma + documento + ".pdf");
    }

    /*@Listen("onClick=#tbbtn_adjpdf_foto")
     public void mostrarPdfFoto() throws FileNotFoundException {
     String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
     generarPdf(rutaFileFoto + documento + ".jpeg");
     }*/
    @Listen("onClick=#tbbtn_adjpdf_recibo")
    public void mostrarPdfRecibo() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileRecibo + documento + ".pdf");
    }

    @Listen("onClick=#tbbtn_adjpdf_otros")
    public void mostrarPdfOtros() throws FileNotFoundException {
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();
        generarPdf(rutaFileOtros + documento + ".pdf");
    }

    public void generarPdf(String ruta) throws FileNotFoundException {
        if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            File salida = new File(ruta);
            if (salida.exists()) {

                FileInputStream fis = new FileInputStream(salida);
                Map objMapObjetos = new HashMap();
                AMedia amedia = new AMedia(salida.getAbsolutePath(), "pdf", "application/pdf", fis);
                objMapObjetos.put("amedia", amedia);
                objMapObjetos.put("archivo", salida.getAbsolutePath().toString());
                objMapObjetos.put("tipo", "pdf");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objMapObjetos);
                window.doModal();

            } else {
                Messagebox.show("La ruta del archivo no se encontró", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            }
        }

    }

    //LOV NACION
    @Listen("onOK=#txt_nacionId")
    public void busquedaNacion() {
        if (bandera == false) {
            bandera = true;
            if (txt_nacionId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_nacionId);
                objMapObjetos.put("TABLA_DESCRI", txt_nacionDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNacion.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_nacionId")
    public void valida_Nacion() throws SQLException {
        if (!txt_nacionId.getValue().isEmpty()) {
            String nacion = txt_nacionId.getValue();
            objNacion = objDaoPersonal.getLovNacion(nacion);
            if (objNacion == null) {
                Messagebox.show("El código de la Nación es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_nacionId.setValue(null);
                        txt_nacionDes.setValue("");
                        txt_nacionId.focus();

                    }
                });

            } else {
                txt_nacionId.setValue(objNacion.getNacion_id());
                txt_nacionDes.setValue(objNacion.getNacion_des());
            }

        } else {
            txt_nacionDes.setValue("");
        }
        bandera = false;
    }

    //LOV NIVEL EDUCATIVO
    @Listen("onOK=#txt_nivEduId")
    public void busquedaNivel() {
        if (bandera == false) {
            bandera = true;
            if (txt_nivEduId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_nivEduId);
                objMapObjetos.put("TABLA_DESCRI", txt_nivEduDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNivEducativo.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_nivEduId")
    public void valida_Nivel() throws SQLException {
        if (!txt_nivEduId.getValue().isEmpty()) {
            String nivel = txt_nivEduId.getValue();
            objNivEdu = objDaoPersonal.getLovNivEdu(nivel);
            if (objNivEdu == null) {
                Messagebox.show("El código del Nivel Educativo es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_nivEduId.setValue(null);
                        txt_nivEduDes.setValue("");
                        txt_nivEduId.focus();
                    }
                });

            } else {
                txt_nivEduId.setValue(objNivEdu.getNivedu_id());
                txt_nivEduDes.setValue(objNivEdu.getNivedu_des());
            }
        } else {
            txt_nivEduDes.setValue("");
        }
        bandera = false;
    }

    //LOV HORARIO
    @Listen("onOK=#txt_horarioId")
    public void busquedaHorario() {
        if (bandera == false) {
            bandera = true;
            if (txt_horarioId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("horario_id", txt_horarioId);
                objMapObjetos.put("hor_descrip", txt_horarioDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorariosPla.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_horarioId")
    public void valida_Horario() throws SQLException {
        if (!txt_horarioId.getValue().isEmpty()) {
            String horario = txt_horarioId.getValue();
            objHorarioPla = objDaoPersonal.getLovHorarios(horario);
            if (objHorarioPla == null) {
                Messagebox.show("El código del Horario es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_horarioId.setValue(null);
                        txt_horarioDes.setValue("");
                        txt_horarioId.focus();
                    }
                });
            } else {
                txt_horarioId.setValue(objHorarioPla.getHorario_id());
                txt_horarioDes.setValue(objHorarioPla.getHor_descrip());
            }
        } else {
            txt_dhDesNacion.setValue("");
        }
        bandera = false;
    }

    //LOV OCUPACION
    @Listen("onOK=#txt_ocupaId")
    public void busquedaOcupacion() {
        if (bandera == false) {
            bandera = true;
            if (txt_ocupaId.getValue().equals("")) {

                Map objMapObjetos = new HashMap();
                objMapObjetos.put("PLOCU_ID", txt_ocupaId);
                objMapObjetos.put("PLOCU_DESCRI", txt_ocupaDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovOcupacion.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    @Listen("onBlur=#txt_ocupaId")
    public void valida_Ocupacion() throws SQLException {
        if (!txt_ocupaId.getValue().isEmpty()) {
            String ocupacion = txt_ocupaId.getValue();
            objOcupacion = objDaoPersonal.getLovOcupacion(ocupacion);
            if (objOcupacion == null) {
                Messagebox.show("El código de la Ocupación es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_ocupaId.setValue(null);
                        txt_ocupaDes.setValue("");
                        txt_ocupaId.focus();
                    }
                });
            } else {
                txt_ocupaId.setValue(objOcupacion.getOcup_id());
                txt_ocupaDes.setValue(objOcupacion.getOcup_des());
            }
        } else {
            txt_ocupaDes.setValue("");
        }
        bandera = false;
    }

    //LOV REGIMEN PENSIONARIO
    @Listen("onOK=#txt_regPenId")
    public void busquedaRegimen() {
        if (bandera == false) {
            bandera = true;
            if (txt_regPenId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_regPenId);
                objMapObjetos.put("TABLA_DESCRI", txt_regPenDes);

                objMapObjetos.put("id", txt_afpId);
                objMapObjetos.put("des", txt_afpDes);

                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAfps.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_regPenId")
    public void valida_Regimen() throws SQLException {
        if (!txt_regPenId.getValue().isEmpty()) {
            String regimen = txt_regPenId.getValue();
            objRegPensionario = objDaoAfp.getLovRegPensionario(regimen);
            if (objRegPensionario == null) {
                Messagebox.show("El código del Régimen Pensionario es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_regPenId.setValue(null);
                        txt_regPenDes.setValue("");
                        txt_regPenId.focus();
                    }
                });
            } else {
                txt_regPenId.setValue(objRegPensionario.getReg_id());
                txt_regPenDes.setValue(objRegPensionario.getReg_des());
            }
        } else {
            txt_regPenDes.setValue("");
        }
        bandera = false;
    }

    //LOV UBIGEO
    @Listen("onOK=#txt_ubigeoId")
    public void busquedaUbigeo() {
        if (bandera == false) {
            bandera = true;
            if (txt_ubigeoId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ubi_id", txt_ubigeoId);
                objMapObjetos.put("ubi_des", txt_ubigeoDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbigeoPla.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_ubigeoId")
    public void valida_Ubigeo() throws SQLException {
        if (!txt_ubigeoId.getValue().isEmpty()) {
            String ubigeo = txt_ubigeoId.getValue();
            objUbigeoPla = objDaoPersonal.getLovUbigeoPla(ubigeo);
            if (objUbigeoPla == null) {
                Messagebox.show("El código del Ubigeo es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_ubigeoId.setValue(null);
                        txt_ubigeoDes.setValue("");
                        txt_ubigeoId.focus();
                    }
                });
            } else {
                txt_ubigeoId.setValue(objUbigeoPla.getUbi_id());
                txt_ubigeoDes.setValue(objUbigeoPla.getUbi_des());
            }
        } else {
            txt_ubigeoDes.setValue("");
        }
        bandera = false;
    }

    //LOV AREAS
    @Listen("onOK=#txt_areaId")
    public void busquedaAreas() {
        if (bandera == false) {
            bandera = true;
            if (txt_areaId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("tabla_id", txt_areaId);
                objMapObjetos.put("tabla_descri", txt_areaDes);
                objMapObjetos.put("ccostoId", txt_ccostoId);
                objMapObjetos.put("ccostoDes", txt_ccostoDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                objMapObjetos.put("validaBusqueda", "AreaCenCos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAreas.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_areaId")
    public void valida_Areas() throws SQLException {
        if (!txt_areaId.getValue().isEmpty()) {
            String areas = txt_areaId.getValue();
            objAreas = objDaoPersonal.getLovAreas(areas);
            if (objAreas == null) {
                Messagebox.show("El código del ÁREA es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_areaId.setValue(null);
                        txt_areaDes.setValue("");
                        txt_areaId.focus();
                    }
                });
            } else {
                txt_areaId.setValue(objAreas.getArea_id());
                txt_areaDes.setValue(objAreas.getArea_des());
            }
        } else {
            txt_areaDes.setValue("");
        }
        bandera = false;
    }

    //LOV COSTOS
    @Listen("onOK=#txt_ccostoId")
    public void busquedaCCostos() {
        if (bandera == false) {
            bandera = true;
            if (txt_ccostoId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id", txt_ccostoId);
                objMapObjetos.put("cc_descri", txt_ccostoDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCCostos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_ccostoId")
    public void valida_CCostos() throws SQLException {
        if (!txt_ccostoId.getValue().isEmpty()) {
            String costo = txt_ccostoId.getValue();
            objCCostos = objDaoAreas.getLovCCostos(costo);
            if (objCCostos == null) {
                Messagebox.show("El código del Centro Costos es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_ccostoId.setValue(null);
                        txt_ccostoDes.setValue("");
                        txt_ccostoId.focus();
                    }
                });
            } else {
                txt_ccostoId.setValue(objCCostos.getCosto_cod());
                txt_ccostoDes.setValue(objCCostos.getCosto_des());
            }
        } else {
            txt_ccostoDes.setValue("");
        }
        bandera = false;
    }

    //LOV CARGOS
    @Listen("onOK=#txt_cargoId")
    public void busquedaCargos() {
        if (bandera == false) {
            bandera = true;
            if (txt_cargoId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_cargoId);
                objMapObjetos.put("TABLA_DESCRI", txt_cargoDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCargos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_cargoId")
    public void valida_Cargos() throws SQLException {
        if (!txt_cargoId.getValue().isEmpty()) {
            String cargos = txt_cargoId.getValue();
            objCargos = objDaoPersonal.getLovCargos(cargos);
            if (objCargos == null) {
                Messagebox.show("El código del Cargo es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_cargoId.setValue(null);
                        txt_cargoDes.setValue("");
                        txt_cargoId.focus();
                    }
                });
            } else {
                txt_cargoId.setValue(objCargos.getCargo_id());
                txt_cargoDes.setValue(objCargos.getCargo_des());
            }
        } else {
            txt_cargoDes.setValue("");
        }
        bandera = false;
    }

    //LOV AFP
    @Listen("onOK=#txt_afpId")
    public void busquedaAfp() {
        if (bandera == false) {
            bandera = true;
            if (txt_afpId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_afpId);
                objMapObjetos.put("TABLA_DESCRI", txt_afpDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAfpPla.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_afpId")
    public void valida_Afp() throws SQLException {
        if (!txt_afpId.getValue().isEmpty()) {
            String afp = txt_afpId.getValue();
            objAfp = objDaoPersonal.getLovAfp(afp);
            if (objAfp == null) {
                Messagebox.show("El código de la AFP es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_afpId.setValue(null);
                        txt_afpDes.setValue("");
                        txt_afpId.focus();
                    }
                });
            } else {
                txt_afpId.setValue(objAfp.getAfp_id());
                txt_afpDes.setValue(objAfp.getAfp_des());
            }
        } else {
            txt_afpDes.setValue("");
        }
        bandera = false;
    }

    //LOV TIPO TRABAJADOR
    @Listen("onOK=#txt_tipTrabId")
    public void busquedaTipTrab() {
        if (bandera == false) {
            bandera = true;
            if (txt_tipTrabId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_tipTrabId);
                objMapObjetos.put("TABLA_DESCRI", txt_tipTrabDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTipTrabajador.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_tipTrabId")
    public void valida_TipTrab() throws SQLException {
        if (!txt_tipTrabId.getValue().isEmpty()) {
            String consulta = txt_tipTrabId.getValue();
            objTipTrabajador = objDaoPersonal.getLovTipTrab(consulta);
            if (objTipTrabajador == null) {
                Messagebox.show("El código del Tipo Trabajador es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_tipTrabId.setValue(null);
                        txt_tipTrabDes.setValue("");
                        txt_tipTrabId.focus();
                    }
                });
            } else {
                txt_tipTrabId.setValue(objTipTrabajador.getTiptrab_id());
                txt_tipTrabDes.setValue(objTipTrabajador.getTiptrab_des());
            }
        } else {
            txt_tipTrabDes.setValue("");
        }
        bandera = false;
    }

    //LOV TIPO CONTRATO
    @Listen("onOK=#txt_tipContId")
    public void busquedaTipCont() {
        if (bandera == false) {
            bandera = true;
            if (txt_tipContId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_tipContId);
                objMapObjetos.put("TABLA_DESCRI", txt_tipContDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTipContrato.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_tipContId")
    public void valida_TipCont() throws SQLException {
        if (!txt_tipContId.getValue().isEmpty()) {
            String consulta = txt_tipContId.getValue();
            objTipContrato = objDaoPersonal.getLovTipCont(consulta);
            if (objTipContrato == null) {
                Messagebox.show("El código del Tipo Contrato es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_tipContId.setValue(null);
                        txt_tipContDes.setValue("");
                        txt_tipContId.focus();
                    }
                });
            } else {
                txt_tipContId.setValue(objTipContrato.getTipcont_id());
                txt_tipContDes.setValue(objTipContrato.getTipcont_des());
            }
        } else {
            txt_tipContDes.setValue("");
        }
        bandera = false;
    }

    //LOV SITUACION
    @Listen("onOK=#txt_sitEpsId")
    public void busquedaSituacion() {
        if (bandera == false) {
            bandera = true;
            if (txt_sitEpsId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_sitEpsId);
                objMapObjetos.put("TABLA_DESCRI", txt_sitEpsDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSituacion.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_sitEpsId")
    public void valida_Situacion() throws SQLException {
        if (!txt_sitEpsId.getValue().isEmpty()) {
            String consulta = txt_sitEpsId.getValue();
            objSituacion = objDaoPersonal.getLovSituacion(consulta);
            if (objSituacion == null) {
                Messagebox.show("El código de la Situación es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_sitEpsId.setValue(null);
                        txt_sitEpsDes.setValue("");
                        txt_sitEpsId.focus();
                    }
                });
            } else {
                txt_sitEpsId.setValue(objSituacion.getSitu_id());
                txt_sitEpsDes.setValue(objSituacion.getSitu_des());
            }
        } else {
            txt_sitEpsDes.setValue("");
        }
        bandera = false;
    }

    //LOV BANCOS txt_banDepHabId
    @Listen("onOK=#txt_banDepHabId")
    public void busquedaBancos() {
        if (bandera == false) {
            bandera = true;
            if (txt_banDepHabId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ban_id", txt_banDepHabId);
                objMapObjetos.put("ban_des", txt_banDepHabDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBancos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_banDepHabId")
    public void valida_Bancos() throws SQLException {
        if (!txt_banDepHabId.getValue().isEmpty()) {
            String bancos = txt_banDepHabId.getValue();
            objBancos = objDaoPersonal.getLovBancos(bancos);
            if (objBancos == null) {
                Messagebox.show("El código del Banco es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_banDepHabId.setValue(null);
                        txt_banDepHabDes.setValue("");
                        txt_banDepHabId.focus();
                    }
                });
            } else {
                txt_banDepHabId.setValue(objBancos.getId());
                txt_banDepHabDes.setValue(objBancos.getDescripcion());

                //capturamos cantidad de digitos de cuneta  
                int numero = objDaoPersonal.consultaDigitos(txt_banDepHabId.getValue());
                //mostramos numero de digitos a ingresar
                Messagebox.show("Número de cuenta debe tener" + " " + numero + " " + "dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                //damos el maximo valor a ingrear
                txt_nroCtaDepHab.setMaxlength(numero);

            }
        } else {
            txt_banDepHabDes.setValue("");
        }

        bandera = false;
    }

    //LOV BANCOS txt_banDepCtsId
    @Listen("onOK=#txt_banDepCtsId")
    public void busquedaBancos2() {
        if (bandera == false) {
            bandera = true;
            if (txt_banDepCtsId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ban_id", txt_banDepCtsId);
                objMapObjetos.put("ban_des", txt_banDepCtsDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBancos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_banDepCtsId")
    public void valida_Bancos2() throws SQLException {
        if (!txt_banDepCtsId.getValue().isEmpty()) {
            String bancos2 = txt_banDepCtsId.getValue();
            objBancos = objDaoPersonal.getLovBancos(bancos2);
            if (objBancos == null) {
                Messagebox.show("El código del Banco es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_banDepCtsId.setValue(null);
                        txt_banDepCtsDes.setValue("");
                        txt_banDepCtsId.focus();
                    }
                });
            } else {
                txt_banDepCtsId.setValue(objBancos.getId());
                txt_banDepCtsDes.setValue(objBancos.getDescripcion());
                //capturamos cantidad de digitos de cuneta  
                int numero = objDaoPersonal.consultaDigitos(txt_banDepCtsId.getValue());
                //mostramos numero de digitos a ingresar
                Messagebox.show("Número de cuenta debe tener" + " " + numero + " " + "dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                //damos el maximo valor a ingrear
                txt_nroCtaDepCts.setMaxlength(numero);
            }
        } else {
            txt_banDepCtsDes.setValue("");
        }
        bandera = false;
    }

    //LOV ENTIDAD PRESTADORA DE SALUD
    @Listen("onOK=#txt_presSaludId")
    public void busquedaEntiSalud() {
        if (bandera == false) {
            bandera = true;
            if (txt_presSaludId.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("plps_id", txt_presSaludId);
                objMapObjetos.put("plps_descri", txt_presSaludDes);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovEntiSalud.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_presSaludId")
    public void valida_EntiSalud() throws SQLException {
        if (!txt_presSaludId.getValue().isEmpty()) {
            String consulta = txt_presSaludId.getValue();
            objEntiSalud = objDaoPersonal.getLovEntiSalud(consulta);
            if (objEntiSalud == null) {
                Messagebox.show("El código de la Entidad es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_presSaludId.setValue(null);
                        txt_presSaludDes.setValue("");
                        txt_presSaludId.focus();
                    }
                });
            } else {
                txt_presSaludId.setValue(objEntiSalud.getEntisalud_id());
                txt_presSaludDes.setValue(objEntiSalud.getEntisalud_des());
            }
        } else {
            txt_presSaludDes.setValue("");
        }
        bandera = false;
    }

    //LOV UBIGEO HABIENTES
    @Listen("onOK=#txt_dhCodUbi")
    public void busquedaUbigeoHabiente() {
        if (bandera == false) {
            bandera = true;
            if (txt_dhCodUbi.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ubi_id", txt_dhCodUbi);
                objMapObjetos.put("ubi_des", txt_dhDesUbi);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbigeoPla.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_dhCodUbi")
    public void valida_UbigeoHabiente() throws SQLException {
        if (!txt_dhCodUbi.getValue().isEmpty()) {
            String ubigeo = txt_dhCodUbi.getValue();
            objUbigeoPla = objDaoPersonal.getLovUbigeoPla(ubigeo);
            if (objUbigeoPla == null) {
                Messagebox.show("El código del Ubigeo es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_dhCodUbi.setValue(null);
                        txt_dhDesUbi.setValue("");
                        txt_dhCodUbi.focus();
                    }
                });
            } else {
                txt_dhCodUbi.setValue(objUbigeoPla.getUbi_id());
                txt_dhDesUbi.setValue(objUbigeoPla.getUbi_des());
            }
        } else {
            txt_dhDesUbi.setValue("");
        }
        bandera = false;
    }

    //LOV MUNICIPALIDAD
    @Listen("onOK=#txt_dhIdMuni")
    public void busquedaUbigeoHabienteMuni() {
        if (bandera == false) {
            bandera = true;
            if (txt_dhIdMuni.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ubi_id", txt_dhIdMuni);
                objMapObjetos.put("ubi_des", txt_dhDesMuni);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbigeoPla.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_dhIdMuni")
    public void valida_UbigeoHabienteMuni() throws SQLException {
        if (!txt_dhIdMuni.getValue().isEmpty()) {
            String ubigeo = txt_dhIdMuni.getValue();
            objUbigeoPla = objDaoPersonal.getLovUbigeoPla(ubigeo);
            if (objUbigeoPla == null) {
                Messagebox.show("El código de la Municipalidad es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_dhIdMuni.setValue(null);
                        txt_dhDesMuni.setValue("");
                        txt_dhIdMuni.focus();
                    }
                });
            } else {
                txt_dhIdMuni.setValue(objUbigeoPla.getUbi_id());
                txt_dhDesMuni.setValue(objUbigeoPla.getUbi_des());
            }
        } else {
            txt_dhDesMuni.setValue("");
        }
        bandera = false;
    }

    //LOV NACION HABIENTE
    @Listen("onOK=#txt_dhNacion")
    public void busquedaNacionHabiente() {
        if (bandera == false) {
            bandera = true;
            if (txt_dhNacion.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_dhNacion);
                objMapObjetos.put("TABLA_DESCRI", txt_dhDesNacion);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNacion.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_dhNacion")
    public void valida_NacionHabiente() throws SQLException {
        if (!txt_dhNacion.getValue().isEmpty()) {
            String nacion = txt_dhNacion.getValue();
            objNacion = objDaoPersonal.getLovNacion(nacion);
            if (objNacion == null) {
                Messagebox.show("El código de la Nación es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_dhNacion.setValue(null);
                        txt_dhDesNacion.setValue("");
                        txt_dhNacion.focus();
                    }
                });
            } else {
                txt_dhNacion.setValue(objNacion.getNacion_id());
                txt_dhDesNacion.setValue(objNacion.getNacion_des());
            }
        } else {
            txt_dhDesNacion.setValue("");
        }
        bandera = false;
    }

    @Listen("onClick=#btn_coordenadasDomi")
    public void botonVerDomicilio() {
        /*if (bandera == false) {
         bandera = true;*/
        //if (txt_cargoId.getValue().equals("")) {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("latitud", db_dirlatx);
        objMapObjetos.put("longitud", db_dirlony);
        objMapObjetos.put("s_estado", s_estado);
        objMapObjetos.put("controlador", "ControllerPersonal");
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVerDomicilio.zul", null, objMapObjetos);
        window.doModal();
        //}
    /*    }*/
    }

    /*@Listen("onClick=#btn_capture")
     public void boton_capture() {
     Clients.evalJavaScript("tomar_foto()");
        
     }*/
    //@Listen("onClick=#btn_guardar")
    public void guardar_foto() throws IOException {
        /*if (txt_nroDoc.getValue().isEmpty() || cb_tipDoc.getSelectedIndex() == -1) {
         Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else {*/
        String documento = cb_tipDoc.getSelectedItem().getValue().toString() + txt_nroDoc.getValue();

        Clients.evalJavaScript("SaveSnap('" + documento + "')");

        //Messagebox.show("GUARDANDO>>>>>>>>>>>>>>>>>>>>");
        //}
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstPersonal = null;
        objlstPersonal = new ListModelList<Personal>();
        lst_personal.setModel(objlstPersonal);
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Listen("onClick=#chk_dmedicos")
    public void imprimirDMedicos() throws SQLException, FileNotFoundException, IOException {
        if (chk_dmedicos.isChecked()) {
            String per_ing = cb_fpering.getSelectedIndex() == -1 || cb_fpering.getSelectedIndex() == 0 ? "" : cb_fpering.getSelectedItem().getValue().toString();
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;

            JasperPrint jasperPrint;
            try {
                Map objParam = new HashMap();
                objParam.put("empresa", objUsuCredential.getCodemp());
                objParam.put("periodo", per_ing);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                final Execution exec = Executions.getCurrent();
                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/Report_DescansoMedico.jasper", false));
                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);//fileUrl
                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "DESCANSO_MEDICO" + aleatorio;
                JExcelApiExporter exporterXLS = new JExcelApiExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporterXLS.exportReport();
                File file = new File(rutaFileCumple + nom_reporte + ".xls");
                Filedownload.save(file, "application/xlsx");

            } catch (JRException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (is != null) {
                    is.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            }
            chk_dmedicos.setChecked(false);
        }

    }

    @Listen("onClick=#tbbtn_excel")
    public void consultaExcel() throws SQLException, FileNotFoundException, IOException {
        busquedaRegistros();

        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();

        int i_seleccion = 0;
        int est;
        objlstPersonal = new ListModelList<Personal>();

        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }
        //cb_fsucursal.getSelectedIndex() == 0 ||
        String sucursal = cb_fsucursal.getSelectedIndex() == -1 || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
        //String junior = cb_fpering.getValue();
        String per_ing = cb_fpering.getSelectedIndex() == -1 || cb_fpering.getSelectedIndex() == 0 ? "" : cb_fpering.getSelectedItem().getValue().toString();
        String per_ces = cb_fperces.getSelectedIndex() == -1 || cb_fperces.getSelectedIndex() == 0 ? "" : cb_fperces.getSelectedItem().getValue().toString();
        String cesados = chk_cesados.isChecked() ? "si" : "no";

        String carea = txt_areaId1_Lov.getValue();
        String costo = txt_costo1.getValue();
        String documento = "", paterno = "", materno = "", nombres = "", cargo = "";
        switch (i_seleccion) {
            case 1:
                documento = s_consulta;
                break;
            case 2:
                paterno = s_consulta;
                break;
            case 3:
                materno = s_consulta;
                break;
            case 4:
                nombres = s_consulta;
                break;
            case 5:
                cargo = s_consulta;
                break;
        }
        //int num_hijo = objDaoPersonal.numHijos(documento);
        Connection conexion = new ConectaBD().conectar();
        InputStream is = null;
        //URL fileUrl = null;
        JasperPrint jasperPrint;
        try {
            Map objParam = new HashMap();
            objParam.put("empresa", objUsuCredential.getCodemp());
            objParam.put("sucursal", sucursal);
            objParam.put("cpering", per_ing);
            objParam.put("cperces", per_ces);
            objParam.put("cdocumento", documento);
            objParam.put("cpaterno", paterno);
            objParam.put("cmaterno", materno);
            objParam.put("cnombres", nombres);
            objParam.put("ccargo", cargo);
            objParam.put("ccesados", cesados);
            objParam.put("estado", est);
            objParam.put("carea", carea);
            objParam.put("ccosto", costo);
            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
            final Execution exec = Executions.getCurrent();
            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ReportPersonal.jasper", false));
            //fileUrl = getClass().getClassLoader().getResource("../../reportes/ReportPersonal.jasper");
            /// fileUrl = getClass().getClassLoader().getResource("../../../../reportes/planillas/mantenimiento/ReportPersonal.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);//fileUrl
            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            int x = 100;
            int y = 0;
            int aleatorio = (int) (Math.random() * x) + y;
            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
            String nom_reporte = Utilitarios.hoyAsString1() + "PERSONAL" + aleatorio;
            JExcelApiExporter exporterXLS = new JExcelApiExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
            exporterXLS.exportReport();
            File file = new File(rutaFileCumple + nom_reporte + ".xls");
            Filedownload.save(file, "application/xlsx");
            busquedaRegistros();
        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (is != null) {
                is.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    public String verificaCumple() {
        String valor;
        if (cb_fsucursal.getValue().trim().isEmpty()) {
            valor = "Debe seleccionar sucursal";
            foco = "sucursalC";
        } else if (cb_fpering.getValue().trim().isEmpty()) {
            valor = "Debe seleccionar un periodo";
            foco = "periodoC";
        } else {
            //valor = cb_fpering.getValue();
            valor = "";
        }
        return valor;
    }

    public String verificaTrabajor() {
        String valor;
        /*if (cb_fsucursal.getValue().trim().isEmpty()) {
         valor = "Debe seleccionar sucursal";
         foco = "sucursalC";
         }  else {*/
        //valor = cb_fpering.getValue();
        valor = "";
        //}
        return valor;
    }

    @Listen("onClick=#tbbtn_torta")
    public void cumpleFeliz() throws SQLException, FileNotFoundException, IOException {
        String s_valida = verificaCumple();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("sucursalC")) {
                            cb_fsucursal.focus();
                        } else if (foco.equals("periodoC")) {
                            cb_fpering.focus();
                        }
                    }

                }
            });
        } else {

            String sucursal = cb_fsucursal.getSelectedIndex() == -1 ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            String per_ing = cb_fpering.getSelectedIndex() == -1 ? "" : cb_fpering.getSelectedItem().getValue().toString();

            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            //  URL fileUrl = null;
            JasperPrint jasperPrint;
            try {
                imagen = getClass().getClassLoader().getResource("../../images/cumple/cumplean.JPEG");
                Map objParam = new HashMap();
                objParam.put("periodo", per_ing);
                objParam.put("empresa", objUsuCredential.getCodemp());
                objParam.put("sucursal", sucursal);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                objParam.put("sello", imagen);
                final Execution exec = Executions.getCurrent();
                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ReportCumpleanios.jasper", false));
                //fileUrl = getClass().getClassLoader().getResource("../../reportes/ReportPersonal.jasper");
                /// fileUrl = getClass().getClassLoader().getResource("../../../../reportes/planillas/mantenimiento/ReportPersonal.jasper");
                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);//fileUrl
                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "CUMPLEAÑOS" + aleatorio;
                JExcelApiExporter exporterXLS = new JExcelApiExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporterXLS.exportReport();
                File file = new File(rutaFileCumple + nom_reporte + ".xls");
                Filedownload.save(file, "application/xlsx");
                /* jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                 String nom_reporte = Utilitarios.hoyAsString1() + "PERSONAL" + aleatorio;
                 JRXlsExporter exporter = new JRXlsExporter();
                 exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                 exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".xls");
                 exporter.exportReport();
                 File file = new File("D:\\" + nom_reporte + ".xls");
                 Filedownload.save(file, "application/xlsx");*/
                //   window.doModal();
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (is != null) {
                    is.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_anios")
    public void trabajadoresCuatro() throws SQLException, FileNotFoundException, IOException {
        String s_valida = verificaTrabajor();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("sucursalC")) {
                            cb_fsucursal.focus();
                        }
                    }

                }
            });
        } else {

            //String sucursal = cb_fsucursal.getSelectedIndex() == -1 ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            //  URL fileUrl = null;
            JasperPrint jasperPrint;
            try {
                Map objParam = new HashMap();

                objParam.put("empresa", objUsuCredential.getCodemp());
                //objParam.put("sucursal", sucursal);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                final Execution exec = Executions.getCurrent();
                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ReportForYear.jasper", false));
                //fileUrl = getClass().getClassLoader().getResource("../../reportes/ReportPersonal.jasper");
                /// fileUrl = getClass().getClassLoader().getResource("../../../../reportes/planillas/mantenimiento/ReportPersonal.jasper");
                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);//fileUrl
                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "FORYEAR" + aleatorio;
                JExcelApiExporter exporterXLS = new JExcelApiExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporterXLS.exportReport();
                File file = new File(rutaFileCumple + nom_reporte + ".xls");
                Filedownload.save(file, "application/xlsx");
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (is != null) {
                    is.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            }
        }
    }

    @Listen("onSeleccion=#lst_personal")
    public void seleccionaEquipo(ForwardEvent evt) {
        //para elegir varios
        /*objlstPersonal = (ListModelList) lst_personal.getModel();

         if (!objlstPersonal.isEmpty() || objlstPersonal != null) {
         Checkbox chk_Equi = (Checkbox) evt.getOrigin().getTarget();
         Listitem item = (Listitem) chk_Equi.getParent().getParent();
         objlstPersonal.get(item.getIndex()).setValSelec(chk_Equi.isChecked());
         lst_personal.setModel(objlstPersonal);

         }*/
        //para solo elegir un dato
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            objlstPersonal.get(i).setValSelec(false);
        }
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstPersonal.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_personal.setModel(objlstPersonal);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        int i = 0;
        for (int j = 0; j < objlstPersonal.getSize(); j++) {
            if (objlstPersonal.get(j).isValSelec()) {
                i = i + 1;
                break;
            }

        }
        if (i <= 0 || lst_personal.getModel().getSize() < 1) {
            Messagebox.show("Debe seleccionar (√) un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("personal", objlstPersonal);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionPersonal.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onClick=#btn_asistencia")
    public void generarAsistencia() throws SQLException {
        int i = 0;
        for (int j = 0; j < objlstPersonal.getSize(); j++) {
            if (objlstPersonal.get(j).isValSelec()) {
                i = i + 1;
                break;
            }

        }
        if (i <= 0 || lst_personal.getModel().getSize() < 1) {
            Messagebox.show("Debe seleccionar (√) un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (cb_fpering.getValue().isEmpty()) {
            Messagebox.show("Debe elegir el periodo a generar asistencia", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            /* ParametrosSalida objPara = objDaoPersonal.generarAsistenciaAut(11,
             1,
             cb_fpering.getValue(),
             "142472830");   */
            ParametrosSalida vista = null;
            for (int j = 0; j < objlstPersonal.getSize(); j++) {
                if (objlstPersonal.get(j).isValSelec()) {
                    vista = objDaoPersonal.generarAsistenciaAut(objlstPersonal.get(j).getEmp_id(),
                            objlstPersonal.get(j).getSuc_id(),
                            cb_fpering.getValue(),
                            objlstPersonal.get(j).getPltipdoc() + objlstPersonal.get(j).getPlnrodoc());
                }

            }
            if (vista.getFlagIndicador() == 0) {
                Messagebox.show(vista.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show(vista.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }

        }

    }

    @Listen("onBlur=#txt_via;onBlur=#txt_nro;onBlur=#txt_int;onBlur=#txt_zona;onBlur=#cb_via;onBlur=#cb_zona")
    public void next() {
        String zona = "", via = "";
        if (!cb_via.getValue().isEmpty()) {
            via = cb_via.getValue().substring(0, cb_via.getValue().indexOf(" "));
        }
        if (!cb_zona.getValue().isEmpty()) {
            zona = cb_zona.getValue().substring(0, cb_zona.getValue().indexOf(" "));
        }

        txt_direccion.setValue(via + " " + txt_via.getValue() + " " + txt_nro.getValue() + " " + txt_int.getValue() + " " + zona + " " + txt_zona.getValue());
    }

    //bevento enter en domicilio
    @Listen("onOK=#cb_condicion")
    public void domiNext1() {
        txt_ubigeoId.focus();
    }

    @Listen("onOK=#txt_ubigeoId")
    public void domiNext2() {
        cb_via.focus();
    }

    @Listen("onOK=#cb_via")
    public void domiNext3() {
        txt_via.focus();
    }

    @Listen("onOK=#txt_via")
    public void domiNext4() {
        txt_nro.focus();
    }

    @Listen("onOK=#txt_nro")
    public void domiNext5() {
        txt_int.focus();
    }

    @Listen("onOK=#txt_int")
    public void domiNext6() {
        cb_zona.focus();
    }

    @Listen("onOK=#cb_zona")
    public void domiNext7() {
        txt_zona.focus();
    }

    @Listen("onOK=#txt_zona")
    public void domiNext8() {
        txt_referencia.focus();
    }

    @Listen("onOK=#txt_referencia")
    public void domiNext9() {
        btn_coordenadasDomi.focus();
    }

    //evento enter en datos personales
    @Listen("onOK=#txt_apePat")
    public void dpNext1() {
        txt_apeMat.focus();
    }

    @Listen("onOK=#txt_apeMat")
    public void dpNext2() {
        txt_nombres.focus();
    }

    @Listen("onOK=#txt_nombres")
    public void dpNext3() {
        txt_nacionId.focus();
    }

    @Listen("onOK=#txt_nacionId")
    public void dpNext4() {
        cb_tipDoc.focus();
    }

    @Listen("onOK=#cb_tipDoc")
    public void dpNext17() {
        txt_nroDoc.focus();
    }

    //para validar numero de documento
    @Listen("onBlur=#cb_tipDoc")
    public void validaDocumento() throws SQLException {
        int tipdoc = cb_tipDoc.getSelectedItem().getValue();
        int numero = objDaoPersonal.consultaDocumento(tipdoc);
        //txt_nroDoc.focus();
        txt_nroDoc.setMaxlength(numero);

    }

    @Listen("onOK=#txt_nroDoc")
    public void dpNext5() {
        d_fecNac.focus();
    }

    @Listen("onClick=#txt_nroDoc")
    public void seleccion() {
        if (cb_tipDoc.getSelectedIndex() == 0) {
            Messagebox.show("Primero debe ingresar tipo de documento", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {

                                cb_tipDoc.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onBlur=#txt_nroDoc")
    public void dpNext6() throws SQLException, IOException {
        /*  if (txt_nroDoc.getValue().isEmpty()) {
         Messagebox.show("Debe ingresar documento de identidad", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
         Messagebox.INFORMATION, new EventListener() {
         public void onEvent(Event t) throws Exception {
         if (((Integer) t.getData()).intValue() == Messagebox.OK) {
         // d_fecNac.setValue(null);
         txt_nroDoc.focus();
         }
         }
         });
         } else {*/
        if (!txt_nroDoc.getValue().isEmpty()) {
            int tipdoc = cb_tipDoc.getSelectedItem().getValue();
            int numero = objDaoPersonal.consultaDocumento(tipdoc);
            if (txt_nroDoc.getValue().length() != numero) {
                Messagebox.show("Debe ingresar n° de documento correcto", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.INFORMATION, new EventListener() {
                            public void onEvent(Event t) throws Exception {
                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                    txt_nroDoc.setValue("");
                                    txt_nroDoc.focus();
                                }
                            }
                        });

            } else {

                int dni = objDaoPersonal.buscarIgual(txt_nroDoc.getValue());
                if (dni == 0) {
                    objlstPersonalIgual = new ListModelList<Personal>();

                    objlstPersonalIgual = objDaoPersonal.consultaDni(txt_nroDoc.getValue());
                    if (!objlstPersonalIgual.isEmpty()) {
                        objPersonalDni = objlstPersonalIgual.get(i_sel);
                        //i_sel = lst_personal.getSelectedIndex();
                        llenarCamposDni(objPersonalDni);
                        objlstDerHabientes = null;
                        objlstDerHabientes = objDaoPersonal.listaDerHabiente(objPersonalDni.getPltipdoc(), objPersonalDni.getPlnrodoc());
                        lst_derhab.setModel(objlstDerHabientes);
                        //mensaje que muestra la o las empresas relacionadas con este trabajador
                        Messagebox.show("Trabajador ya existe en el Grupo, Desea ver empresa donde labora", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.INFORMATION, new EventListener() {
                                    public void onEvent(Event t) throws Exception {
                                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                            // if (bandera == false) {
                                            //   bandera = true;
                                            Map objMapObjetos = new HashMap();
                                            objMapObjetos.put("documento", txt_nroDoc.getValue());
                                            // objMapObjetos.put("sucursal",objPersonalDni.getSuc_id());
                                            objMapObjetos.put("controlador", "ControllerPersonal");
                                            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVerEmpresa.zul", null, objMapObjetos);

                                            window.doModal();
                                            //}

                                        }
                                    }
                                }
                        );
                    } else {
                        d_fecNac.focus();
                    }
                } else {
                    Messagebox.show("Trabajador ya existe en la empresa o esta inactivo", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.INFORMATION, new EventListener() {
                                public void onEvent(Event t) throws Exception {
                                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                        txt_nroDoc.setValue(null);
                                        txt_nroDoc.focus();
                                    }

                                }
                            });
                }
            }
        }
    }

    @Listen("onOK=#d_fecNac")
    public void dpNext7() {
        if (d_fecNac.getValue() == null) {
            Messagebox.show("Debe ingresar fecha de nacimiento", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.INFORMATION, new EventListener() {
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                // d_fecNac.setValue(null);
                                d_fecNac.focus();
                            }
                        }
                    });
        } else {
            String fecha = formato.format(d_fecNac.getValue());
            int eda = edad(fecha);//enviamos la fecha de nacimiento para hallar la edad del trabajador
            if (eda < 18) {
                Messagebox.show("Es menor de edad, no puede ingresar a trabajar", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.INFORMATION, new EventListener() {

                            public void onEvent(Event t) throws Exception {
                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                    d_fecNac.setValue(null);
                                    d_fecNac.focus();
                                }
                            }
                        });
            } else {
                cb_sexo.focus();
            }
        }
    }

    //metodo para hallar edad
    public int edad(String fecha_nac) {     //fecha_nac debe tener el formato dd/MM/yyyy

        Date fechaActual = new Date();
        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String hoy = formato.format(fechaActual);
        String[] dat1 = fecha_nac.split("/");
        String[] dat2 = hoy.split("/");
        int anos = Integer.parseInt(dat2[2]) - Integer.parseInt(dat1[2]);
        /* int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
         if (mes < 0) {
         anos = anos - 1;
         } else if (mes == 0) {
         int dia = Integer.parseInt(dat2[0]) - Integer.parseInt(dat1[0]);
         if (dia > 0) {
         anos = anos - 1;
         }
         }*/
        return anos;
    }

    @Listen("onOK=#cb_sexo")
    public void dpNext8() {
        cb_estCivil.focus();
    }

    @Listen("onOK=#cb_estCivil")
    public void dpNext9() {
        txt_grupSang.focus();
    }

    @Listen("onOK=#txt_grupSang")
    public void dpNext10() {
        txt_telefono.focus();
    }

    @Listen("onOK=#txt_telefono")
    public void dpnext11() {
        txt_movil.focus();
    }

    @Listen("onOK=#txt_movil")
    public void dpNext12() {
        txt_correo.focus();
    }

    //valida correo electronico
    public boolean esEmailCorrecto(String email) {

        boolean valido = false;

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);
        if (mather.find()) {
            valido = true;
        }
        return valido;
    }

    @Listen("onOK=#txt_correo")
    public void dpNext13() {
        // Patrón para validar el email
        /*Pattern pattern = Pattern
         .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
         + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

         // El email a validar
         //String email = "JUNIO670@HOTMAIL.COM";

         Matcher mather = pattern.matcher(txt_correo.getValue());

         if (mather.find() == true) {
         System.out.println("El email ingresado es válido.");
         } else {
         System.out.println("El email ingresado es inválido.");
         }*/

        boolean a = esEmailCorrecto(txt_correo.getValue());
        if (a == false) {
            Messagebox.show("Ingrese un correo electrónico correcto", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.INFORMATION, new EventListener() {
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                txt_correo.setValue(null);
                                txt_correo.focus();
                            }
                        }
                    });
        }
        chk_discap.focus();
    }

    @Listen("onOK=#chk_discap")
    public void dpNext14() {
        txt_nivEduId.focus();
    }

    @Listen("onOK=#txt_nivEduId")
    public void dpNext15() {
        txt_ocupaId.focus();
    }

    @Listen("onOK=#txt_ocupaId")
    public void dpNext16() {
        cb_condicion.focus();
    }
    //evento enter en datos laborales

    @Listen("onOK=#d_fecIng")
    public void dlNext1() {
        txt_codInterno.focus();// d_fecCese.focus();
    }
    /*@Listen("onOK=#d_fecCese")
     public void dlNext2(){
     txt_codInterno.focus();
     }-*/

    @Listen("onOK=#txt_codInterno")
    public void dlNext3() {
        cb_sucursal.focus();
    }

    @Listen("onOK=#cb_sucursal")
    public void dlNext4() {
        txt_areaId.focus();
    }

    @Listen("onOK=#txt_areaId")
    public void dlNext5() {
        txt_ccostoId.focus();
    }

    @Listen("onOK=#txt_ccostoId")
    public void dlNext6() {
        cb_categoria.focus();
    }

    @Listen("onOK=#cb_categoria")
    public void dlNext7() {
        txt_tipTrabId.focus();
    }

    @Listen("onOK=#txt_tipTrabId")
    public void dlNext8() {
        txt_horarioId.focus();
    }

    @Listen("onOK=#txt_horarioId")
    public void dlNext9() {
        txt_cargoId.focus();
    }

    @Listen("onOK=#txt_cargoId")
    public void dlNext10() {
        cb_sitEsp.focus();
    }

    @Listen("onOK=#cb_sitEsp")
    public void dlNext11() {
        txt_tipContId.focus();
    }

    @Listen("onOK=#txt_tipContId")
    public void dlNext12() {
        chk_sujCtrlInm.focus();
    }

    @Listen("onOK=#chk_sujCtrlInm")
    public void dlNext13() {
        cb_tipSueldo.focus();
    }

    @Listen("onOK=#cb_tipSueldo")
    public void dlNext14() {
        cb_perRem.focus();
    }

    @Listen("onOK=#cb_perRem")
    public void dlNext15() {
        chk_utilidad.focus();
    }

    /* @Listen("onOK=#ib_hExtras")
     public void dlNext16() {
     chk_utilidad.focus();
     }*/
    @Listen("onOK=#chk_utilidad")
    public void dlNext17() {
        chk_excQuinta.focus();
    }

    @Listen("onOK=#chk_excQuinta")
    public void dlNext18() {
        chk_otrosQuinta.focus();
    }

    @Listen("onOK=#chk_otrosQuinta")
    public void dlNext19() {
        chk_essalud.focus();
    }

    @Listen("onOK=#chk_essalud")
    public void dlNext20() {
        txt_carnetSsp.focus();
    }

    @Listen("onOK=#txt_carnetSsp")
    public void dlNext21() {
        chk_sindicalizado.focus();
    }

    @Listen("onOK=#chk_sindicalizado")
    public void dlNext22() {
        chk_pensionista.focus();
    }

    //evento enter en aportacion
    @Listen("onOK=#chk_pensionista")
    public void apNext1() {
        txt_regPenId.focus();
    }

    @Listen("onOK=#txt_regPenId")
    public void apNext2() {
        d_fecIngRp.focus();
    }

    @Listen("onOK=#d_fecIngRp")
    public void apNext3() {
        d_fecCesRp.focus();
    }

    @Listen("onOK=#d_fecCesRp")
    public void apNext4() {
        cb_tipPen.focus();
    }

    @Listen("onOK=#cb_tipPen")
    public void apNext5() {
        txt_afpId.focus();
    }

    @Listen("onOK=#txt_afpId")
    public void apNext6() {
        txt_carnetAfp.focus();
    }

    @Listen("onOK=#txt_carnetAfp")
    public void apNext7() {
        chk_comMix.focus();
    }

    @Listen("onOK=#chk_comMix")
    public void apNext8() {
        txt_presSaludId.focus();
    }

    @Listen("onOK=#txt_presSaludId")
    public void apNext9() {
        txt_sitEpsId.focus();
    }

    @Listen("onOK=#txt_sitEpsId")
    public void apNext10() {
        d_fecBajaEs.focus();
    }

    @Listen("onOK=#d_fecBajaEs")
    public void apNext11() {
        cb_sctrSalud.focus();
    }

    @Listen("onOK=#cb_sctrSalud")
    public void apNext12() {
        cb_sctrPension.focus();
    }

    //evento de derecho habiente
    @Listen("onOK=#txt_dhApePat")
    public void derehabNext1() {
        txt_dhApeMat.focus();
    }

    @Listen("onOK=#txt_dhApeMat")
    public void derehabNext2() {
        txt_dhNombres.focus();
    }

    @Listen("onOK=#txt_dhNombres")
    public void derehabNext3() {
        cb_dhTipDoc.focus();
    }

    @Listen("onOK=#cb_dhTipDoc")
    public void derehabNext4() {
        txt_dhNroDoc.focus();
    }

    @Listen("onOK=#txt_dhNroDoc")
    public void derehabNext5() throws SQLException {

        d_dhFecNac.focus();
    }

    @Listen("onOK=#d_dhFecNac")
    public void derehabNext6() {
        cb_dhSexo.focus();
    }

    @Listen("onOK=#cb_dhSexo")
    public void derehabNext7() {
        cb_dhParentesto.focus();
    }

    @Listen("onOK=#cb_dhParentesto")
    public void derehabNext8() {
        d_dhFecAlta.focus();
    }

    @Listen("onOK=#d_dhFecAlta")
    public void derehabNext9() {
        cb_dhDocVf.focus();
    }

    @Listen("onOK=#cb_dhDocVf")
    public void derehabNext10() {
        txt_dhNroDocVf.focus();
    }

    @Listen("onOK=#txt_dhNroDocVf")
    public void derehabNext11() {
        txt_dhNrd.focus();
    }

    @Listen("onOK=#txt_dhNrd")
    public void derehabNext12() {
        txt_dhIdMuni.focus();
    }

    @Listen("onOK=#txt_dhIdMuni")
    public void derehabNext13() {
        txt_dhNacion.focus();
    }

    @Listen("onOK=#txt_dhNacion")
    public void derehabNext14() {
        cb_dhSituacion.focus();
    }

    @Listen("onOK=#cb_dhSituacion")
    public void derehabNext15() {
        cb_dhTipBaja.focus();
    }

    @Listen("onOK=#cb_dhTipBaja")
    public void derehabNext16() {
        d_dhFecBaja.focus();
    }

    @Listen("onOK=#d_dhFecBaja")
    public void derehabNext17() {
        cb_dhVia.focus();
    }

    @Listen("onOK=#cb_dhVia")
    public void derehabNext18() {
        txt_dhVia.focus();
    }

    @Listen("onOK=#txt_dhVia")
    public void derehabNext19() {
        chk_dhViveDom.focus();
    }

    @Listen("onOK=#chk_dhViveDom")
    public void derehabNext20() {
        cb_dhZona.focus();
    }

    @Listen("onOK=#cb_dhZona")
    public void derehabNext21() {
        txt_dhZona.focus();
    }

    @Listen("onOK=#txt_dhZona")
    public void derehabNext22() {
        txt_dhNro.focus();
    }

    @Listen("onOK=#txt_dhNro")
    public void derehabNext23() {
        txt_dhInt.focus();
    }

    @Listen("onOK=#txt_dhInt")
    public void derehabNext24() {
        txt_dhRefer.focus();
    }

    @Listen("onOK=#txt_dhRefer")
    public void derehabNext25() {
        txt_dhCodUbi.focus();
    }

    //codigo para utilizar filtro
    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    //metodo lov para centro de costos
    @Listen("onOK=#txt_costo")
    public void lovCosto() {
        if (bandera == false) {
            bandera = true;
            if (txt_costo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("costo", txt_costo);
                objMapObjetos.put("descripcion", txt_descripcioncosto);
                objMapObjetos.put("costo1", txt_costo1);//campo invisible que guarda informacion de personal
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCostoMultiple.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    //blur para costos
    //Salir de lov para el filtro
    @Listen("onBlur=#txt_costo")
    public void valida_CostoPrincipal() throws SQLException {
        if (!txt_costo.getValue().isEmpty()) {
            if (!txt_costo.getValue().toUpperCase().equals("JIM")) {

                if (!txt_costo.getValue().equals("ALL")) {
                    String id = txt_costo.getValue();
                    objCCostos = objDaoAreas.getLovCCostos(id);
                    //  objPersonal = objDaoMovLinea.getLovPersonal(id);
                    if (objCCostos == null) {
                        Messagebox.show("El codigo de centro de costo no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                            public void onEvent(Event t) throws Exception {
                                txt_costo.setValue(null);
                                txt_costo.focus();
                                txt_descripcioncosto.setValue("");

                            }
                        });

                    } else {
                        txt_costo.setValue(objCCostos.getCosto_cod());
                        txt_descripcioncosto.setValue(objCCostos.getCosto_des());
                        txt_costo1.setValue(objCCostos.getCosto_cod() + "','");
                    }
                }
            }
        } else {
            txt_descripcioncosto.setValue("");
            txt_costo1.setValue("");
        }

        bandera = false;
    }
}
