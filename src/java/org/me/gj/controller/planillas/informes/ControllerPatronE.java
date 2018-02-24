/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import static org.me.gj.controller.planillas.mantenimiento.ControllerPersonal.bandera;
import org.me.gj.controller.planillas.mantenimiento.DaoAfpsCre;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
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
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerPatronE extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Tab tab_listapersonal, tab_datosper, tab_datoslab, tab_pagos, tab_adjuntos, tab_derhabientes;
    @Wire
    Listbox lst_personal, lst_derhab, lst_empresas, lst_sucursales;
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
    ListModelList<Sucursales> objlstSucursal, objlstEmpresas, objlstEmpresaTmp, objlstAux,objlstSucursalTmp;

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

        //objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        // objlstSucursal = objDaoPersonal.lstSucursales(emp_id); //se comento
      //  cb_sucursal.setModel(objlstSucursal);
        //  cb_fsucursal.setModel(objlstSucursal);
        //se comento por periodo dinamico que sale un campo en blanco
       /* cb_fpering.setModel(new DaoManPer().listaPeriodos(0));
         cb_fperces.setModel(new DaoManPer().listaPeriodos(0));*/
        cb_fpering.setModel(new DaoManPer().listaPeriodosDinamico(0));
        cb_fperces.setModel(new DaoManPer().listaPeriodosDinamico(0));
        //carga clientes
        objlstPersonal = new ListModelList<Personal>();

        objlstPersonal = objDaoPersonal.consultaPatronPersonal("","", "", "", 0, "", "no", 1, "", "");
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

        //carga empresas
        objlstEmpresas = objDaoAccesos.lstEmpresasPatron();
        lst_empresas.setModel(objlstEmpresas);
        //carga sucursales
        // objlstVtasSucursales = new ListModelList<VtasSucursales>();
        objlstSucursal = objDaoAccesos.lstSucursalesPatron("");
        //  objlstVtasSucursales = objDaoVtasxGrupo.listaSucursales("");
        lst_sucursales.setModel(objlstSucursal);

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
                objMapObjetos.put("controlador", "ControllerPatronE");
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
            objMap.put("controlador", "ControllerPatronE");
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
       ///////// String sucursal = cb_fsucursal.getSelectedIndex() == -1 || cb_fsucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
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
        objlstPersonal = objDaoPersonal.consultaPatronPersonal(RetornaCadenaEmpid(),RetornaCadenaSucid(), per_ing, per_ces, i_seleccion, s_consulta, cesados, est, s_aux, costo);
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

    public void limpiarLista() {
        //limpio mi lista
        objlstPersonal = null;
        objlstPersonal = new ListModelList<Personal>();
        lst_personal.setModel(objlstPersonal);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
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

    public void llenarListaDerHabiente() throws SQLException {
        objlstDerHabientes = null;
        objlstDerHabientes = objDaoPersonal.listaDerHabiente(objPersonal.getPltipdoc(), objPersonal.getPlnrodoc());
        lst_derhab.setModel(objlstDerHabientes);
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

    public void limpiarCamposPagosCts() {
        ib_correlpc.setValue(null);
        txt_banDepCtsId.setValue("");
        txt_banDepCtsDes.setValue("");
        cb_tipCuentaDepCts.setSelectedIndex(-1);
        cb_tipMonedaDepCts.setSelectedIndex(-1);
        txt_nroCtaDepCts.setValue("");
        txt_glosaDepCts.setValue("");

    }

    @Listen("onSeleccione=#lst_empresas")
    public void seleccionaregistroEmpresa(ForwardEvent evt) throws SQLException {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstEmpresas.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_empresas.setModel(objlstEmpresas);

        objlstSucursal = objDaoAccesos.lstSucursalesPatron(RetornaCadenaEmpid());
        lst_sucursales.setModel(objlstSucursal);
        //agregado para canal

    }
     @Listen("onSeleccions=#lst_sucursales")
    public void seleccionaRegistroSucursal(ForwardEvent evt) throws SQLException {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstSucursal.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_sucursales.setModel(objlstSucursal);
  
     
    }

    public String RetornaCadenaEmpid() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstEmpresas.getSize(); j++) {
            if (objlstEmpresas.get(j).isValSelec()) {
                i = i + 1;
                break;

            }

        }
        if (i <= 0) {
            cadena = "";

        } else {
            objlstEmpresaTmp = new ListModelList<Sucursales>();
            objlstEmpresaTmp = getlistaEmpid(objlstEmpresas);
            for (int j = 0; j < objlstEmpresaTmp.getSize(); j++) {
                cadena = cadena + objlstEmpresaTmp.get(j).getEmp_id() + "','";
            }

        }
        return cadena;
    }

    public ListModelList<Sucursales> getlistaEmpid(ListModelList<Sucursales> objlstEmp) {
        objlstAux = new ListModelList<Sucursales>();
        for (int i = 0; i < objlstEmp.getSize(); i++) {
            if (objlstEmp.get(i).isValSelec()) {
                objlstAux.add(objlstEmp.get(i));
            }
        }
        return objlstAux;
    }
    
    public String RetornaCadenaSucid() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstSucursal.getSize(); j++) {
            if (objlstSucursal.get(j).isValSelec()) {
                i = i + 1;
                break;

            }

        }
        if (i <= 0) {
            cadena = "";

        } else {
            objlstSucursalTmp = new ListModelList<Sucursales>();
            objlstSucursalTmp = getlistaSucid(objlstSucursal);
            for (int j = 0; j < objlstSucursalTmp.getSize(); j++) {
                cadena = cadena + objlstSucursalTmp.get(j).getSuc_id()+ "','";
            }

        }
        return cadena;
    }
    
        public ListModelList<Sucursales> getlistaSucid(ListModelList<Sucursales> objlstEmp) {
        objlstAux = new ListModelList<Sucursales>();
        for (int i = 0; i < objlstEmp.getSize(); i++) {
            if (objlstEmp.get(i).isValSelec()) {
                objlstAux.add(objlstEmp.get(i));
            }
        }
        return objlstAux;
    }
    
}
