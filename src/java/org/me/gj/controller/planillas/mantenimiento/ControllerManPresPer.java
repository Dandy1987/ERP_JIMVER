/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoEmpresas;
import org.me.gj.model.planillas.mantenimiento.DatosLaborales;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManPresPer;
import org.me.gj.model.planillas.mantenimiento.ManPresPerDet;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerManPresPer extends SelectorComposer<Component> {

    @Wire
    Tab tab_listapresper, tab_presper;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_adjcar_docpres, tbbtn_adjpdf_docpres,
            tbbtn_btn_buscar, tbbtn_btn_editar3,
            tbbtn_btn_guardar3, tbbtn_btn_deshacer3;

    @Wire
    Textbox txt_dindicador, txt_dsituacion, txt_usuadd, txt_usumod, txt_nropres, txt_codper_man, txt_desper_man, txt_adj_docpres, txt_busqueda_pres;

    @Wire
    Intbox txt_nrocuotas, d_dncuota;

    @Wire
    Doublebox txt_montotal, d_dcuota, d_dacumulado;

    @Wire
    Listbox lst_detpres, lst_prestamo;

    @Wire
    Datebox d_fecadd, d_fecmod, d_fecemi, d_dpago;

    @Wire
    Combobox cb_estado, cb_busqueda;

    Media mediaAcuPres;

    File archivoAcuPres;

    boolean cargaAcuPres = false;
    String mensaje = "";

    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileAcuPres = rutaFile + "ACUPRES\\";
    String rutaBatch = rutaFile + "TEMPLATES\\generaPDF.bat";

    String rutaPlantilla = "GJ_CARPETAS\\PLANILLAS\\TEMPLATES\\Acuerdo.odt";
    String extension = "odt";

    //String rutaFile2 = "D:\\ACUERDOS\\DOCUMENTOS\\";
    //String rutaFileAcuPres2 = rutaFile2 + "ACUERDO\\";
    //Instancias de objetos
    ListModelList<ManPresPer> objlstManPresPer = new ListModelList<ManPresPer>();
    ManPresPer objManPresper = new ManPresPer();
    DaoManPresPer objDaoManPresPer = new DaoManPresPer();

    ListModelList<ManPresPerDet> objlstManPresPerDet = new ListModelList<ManPresPerDet>();
    ManPresPerDet objManPresperDet;//= new ManPresPerDet();

    //LOV AREAS
    ListModelList<ManAreas> objlstAreas;
    ManAreas objAreas;
    DaoManAreas objDaoAreas = new DaoManAreas();

    //LOV PERSONAL
    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;
    DaoPersonal objDaoPersonal = new DaoPersonal();

    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    ///////////////////
    ListModelList<Empresas> objlstEmpresas = new ListModelList<Empresas>();
    Empresas objEmpresa = new Empresas();
    DaoEmpresas objDaoEmpresas = new DaoEmpresas();
    /////////////////////////////////
    DatosLaborales objDatosLabBase, objDatosLabNuevo;
    Process process;
    //Variables publicas
    String s_estado = "Q";
    String d_estado = "";
    String s_mensaje;
    String campo = "";
    int i_sel;
    int valor;
    double total = 0;
    public static boolean bandera = false;

    //int vol_det = 0;
    DecimalFormat df = new DecimalFormat("#,###,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfper = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdfmes = new SimpleDateFormat("MM");
    SimpleDateFormat sdfamd = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdfanio = new SimpleDateFormat("yyyy");
    SimpleDateFormat sdfpres = new SimpleDateFormat("ddMMyyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManPresPer.class);

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90104000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Prestamo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Prestamo con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Prestamo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Prestamo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Prestamo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Prestamo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Prestamo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Prestamo");
        }
    }

    @Listen("onCreate=#tabbox_presper")
    public void llenaLista() throws SQLException {
        cb_estado.setSelectedIndex(2);
        objlstManPresPer = objDaoManPresPer.listaPrestamo(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
        lst_prestamo.setModel(objlstManPresPer);
        //d_pago.setValue(new Date());
    }

    @Listen("onSelect=#lst_prestamo")
    public void seleccionaRegistroCab() throws SQLException {
        limpiarCampos();
        objManPresper = (ManPresPer) lst_prestamo.getSelectedItem().getValue();
        if (objManPresper == null) {
            objManPresper = objlstManPresPer.get(i_sel + 1);
        }
        i_sel = lst_prestamo.getSelectedIndex();
        tbbtn_adjcar_docpres.setDisabled(true);
        tbbtn_adjpdf_docpres.setDisabled(false);
        llenarCabecera(objManPresper);
        llenaListaDetalle();
    }

    @Listen("onSeleccion=#lst_prestamo")
    public void seleccionaEquipo(ForwardEvent evt) {
        objlstManPresPer = (ListModelList) lst_prestamo.getModel();

        if (!objlstManPresPer.isEmpty() || objlstManPresPer != null) {
            Checkbox chk_Equi = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Equi.getParent().getParent();
            objlstManPresPer.get(item.getIndex()).setValSelec(chk_Equi.isChecked());
            lst_prestamo.setModel(objlstManPresPer);

        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objManPresper = new ManPresPer();
        LimpiarListaDetalle();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        habilitaBotonesDetalle(false, true);
        tbbtn_adjcar_docpres.setDisabled(false);
        tbbtn_adjpdf_docpres.setDisabled(true);
        txt_codper_man.focus();
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        s_estado = "N";

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_prestamo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            tbbtn_adjcar_docpres.setDisabled(false);
            tbbtn_adjpdf_docpres.setDisabled(true);
            habilitaBotonesDetalle(false, true);

        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() throws SQLException {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_prestamo.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            // habilitaBotones(false, true);
                            habilitaBotonesDetalle(true, true);
                            d_dpago.setDisabled(true);
                            lst_prestamo.focus();
                            //
                            habilitaCampos(true);
                            limpiaAuditoria();
                            LimpiarListaDetalle();
                            s_estado = "Q";

                        }
                    }
                });

    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        // Si hay algun error de validacion envia el foco al campo obligatorio
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("personal")) {
                            txt_codper_man.focus();
                        } else if (campo.equals("fecemi")) {
                            d_fecemi.focus();
                        } else if (campo.equals("montotal")) {
                            txt_montotal.focus();
                        } else if (campo.equals("nrocuotas")) {
                            txt_nrocuotas.focus();
                        }
                    }
                }
            });
        } //Si todos los campos son ingresados y pasan todas las validaciones
        else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                ParametrosSalida objParamCabPres;
                                objlstManPresPer = new ListModelList<ManPresPer>();
                                objManPresper = (ManPresPer) generaRegistroCab();//falta
                                if (s_estado.equals("N")) {
                                    objParamCabPres = objDaoManPresPer.insertarPrestamo(objManPresper, getDetalle(objlstManPresPerDet));
                                } else {
                                    objParamCabPres = objDaoManPresPer.modificarPrestamo(objManPresper, getDetalle(objlstManPresPerDet));
                                }

                                if (objParamCabPres.getFlagIndicador() == 0) {
                                    if (s_estado.equals("M")) {
                                        if (cargaAcuPres == true) {
                                            Files.copy(archivoAcuPres, mediaAcuPres.getStreamData());
                                        }
                                    }
                                    if (s_estado.equals("N")) {
                                        //Generar Documento Acuerdo de Prestamos en Formato ODT y luego a PDF
                                        generarDocumento(rutaPlantilla, extension, false);

                                    }
                                    limpiarListaRegistro();
                                    LimpiarListaDetalle();
                                }
                                Messagebox.show(objParamCabPres.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                habilitaBotonesDetalle(true, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                objlstManPresPer = objDaoManPresPer.listaPrestamo(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                                lst_prestamo.setModel(objlstManPresPer);
                                objManPresper = new ManPresPer();
                                lst_prestamo.focus();
                                s_estado = "Q";

                            }
                        }
                    });

            //}
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_prestamo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccionar un registro", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                            }
                        }
                    });

        } else {
            s_mensaje = "Está seguro que desea eliminar el préstamo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", 3, "z-msgbox z-msgbox-question", new EventListener() {
                public void onEvent(Event evento) throws SQLException {
                    if (((Integer) evento.getData()).intValue() == 1) {
                        ParametrosSalida objParamSalida = objDaoManPresPer.eliminarPrestamo(objManPresper);
                        if (objParamSalida.getFlagIndicador() == 0) {
                            objlstManPresPer.clear();
                            objlstManPresPer = objDaoManPresPer.listaPrestamo(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                            lst_prestamo.setModel(objlstManPresPer);
                            lst_prestamo.focus();
                            limpiarCampos();
                            limpiaAuditoria();
                            s_estado = "E";
                        }
                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                    }
                }

            });
        }
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda_pres.setDisabled(false);
        } else {
            txt_busqueda_pres.setDisabled(true);
            txt_busqueda_pres.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarListaCabecera();
        String s_consulta = txt_busqueda_pres.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 0;
        }
        objlstManPresPer = new ListModelList<ManPresPer>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;

        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;

        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;

        }

        objlstManPresPer = objDaoManPresPer.consultaPrestamos(i_seleccion, s_consulta, i_estado);

        if (objlstManPresPer.getSize() > 0) {
            lst_prestamo.setModel(objlstManPresPer);
        } else {
            objlstManPresPer = null;
            lst_prestamo.setModel(objlstManPresPer);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    public void llenarCabecera(ManPresPer objManPresPer) throws SQLException {
        File ruta_archivo;

        txt_nropres.setValue(objManPresPer.getTPLPRESCAB_ID());
        txt_codper_man.setValue(objManPresPer.getTPLPRESCAB_IDPER());
        txt_desper_man.setValue(objManPresPer.getTPLPRESCAB_DESPER());
        txt_montotal.setValue(objManPresper.getTlplprescab_monto());
        txt_nrocuotas.setValue(objManPresper.getTlplprescab_nrocuo());
        d_fecemi.setValue(objManPresPer.getTPLPRESCAB_FECEMI());
        d_fecadd.setValue(objManPresPer.getTPLPRESCAB_FECADD());
        d_fecmod.setValue(objManPresPer.getTPLPRESCAB_FECMOD());
        txt_usuadd.setValue(objManPresPer.getTPLPRESCAB_USUADD());
        txt_usumod.setValue(objManPresPer.getTPLPRESCAB_USUMOD());
        String documento = txt_codper_man.getValue();

        ruta_archivo = new File(rutaFileAcuPres + documento + ".PDF");
        if (ruta_archivo.exists()) {
            txt_adj_docpres.setValue(rutaFileAcuPres + documento + ".PDF");
        } else {
            txt_adj_docpres.setValue("");
        }
    }

    public void llenarDetalle(ManPresPerDet objManPresPerDet) throws SQLException {
        txt_montotal.setValue(objManPresPerDet.getTPLPRESDET_MONTOTAL());
        txt_nrocuotas.setValue(objManPresPerDet.getTPLPRESDET_TOTCUOTAS());

    }

    public void limpiarListaRegistro() {
        objlstManPresPer = null;
        objlstManPresPer = new ListModelList<ManPresPer>();
        lst_prestamo.setModel(objlstManPresPer);

    }

    public Object generaRegistroCab() {

        objManPresper = new ManPresPer();
        objManPresper.setEMP_ID(objUsuCredential.getCodemp());
        objManPresper.setSUC_ID(objUsuCredential.getCodsuc());
        objManPresper.setTPLPRESCAB_ID(objManPresper.getTPLPRESCAB_ID());
        objManPresper.setTPLPRESCAB_IDPER(txt_codper_man.getValue());
        objManPresper.setTPLPRESCAB_DESPER(txt_desper_man.getValue().toUpperCase());
        objManPresper.setTPLPRESCAB_USUADD(objUsuCredential.getCuenta());
        objManPresper.setTPLPRESCAB_FECEMI(d_fecemi.getValue());
        objManPresper.setTPLPRESCAB_USUMOD(objUsuCredential.getCuenta());
        objManPresper.setTPLPRESCAB_ID(txt_nropres.getValue());
        objManPresper.setTlplprescab_monto(txt_montotal.getValue());
        objManPresper.setTlplprescab_nrocuo(txt_nrocuotas.getValue());

        return objManPresper;

    }

    public Object[][] getDetalle(ListModelList<ManPresPerDet> x) {
        ListModelList<ManPresPerDet> objListaPerDet;

        objListaPerDet = x;

        Object[][] listaDet = new Object[objListaPerDet.size()][9];
        for (int i = 0; i < objListaPerDet.size(); i++) {
            listaDet[i][0] = objListaPerDet.get(i).getTPLPRESDET_NROCUOTA();
            listaDet[i][1] = objListaPerDet.get(i).getTPLPRESDET_MONTCUOTA();
            listaDet[i][2] = txt_nrocuotas.getValue();
            listaDet[i][3] = objListaPerDet.get(i).getTPLPRESDET_SALCUOTA();
            listaDet[i][4] = objListaPerDet.get(i).getTPLPRESDET_IND();
            listaDet[i][5] = new java.sql.Date(objListaPerDet.get(i).getTPLPRESDET_FECPAGO().getTime());
            listaDet[i][6] = txt_montotal.getValue();
            listaDet[i][7] = objListaPerDet.get(i).getTPLPRESDET_SITUCUOTA();
            listaDet[i][8] = objListaPerDet.get(i).getTPLINDACCION();
        }

        return listaDet;

    }

	public ListModelList<ManPresPerDet> calcularCuotas(Date fecemi, Double montototal, int nrocuotas) throws SQLException {
        //ListModelList<ManPresPerDet> objlstManPresPerDet = new ListModelList<ManPresPerDet>();

        double saldo;
        int nro_cuota = 0, y = 0;
        double montcuota = Redondear(montototal / nrocuotas, 2);
        total = 0;
        objlstManPresPerDet.clear();
        double cuotasumado = 0;

        for (int i = 0; i < nrocuotas; i++) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(fecemi);
            cal.add(Calendar.MONTH, y);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            nro_cuota = nro_cuota + 1;
            double cuadre = i == (nrocuotas - 1) ? montototal - (montcuota * (nrocuotas - 1)) : montcuota;
            fecemi = cal.getTime();
            total = Redondear(total + cuadre, 2);
            saldo = montototal - montcuota;
            y = 1;
            ManPresPerDet objManPrestamo = new ManPresPerDet();//objManPresperDet se sustituye
            objManPrestamo.setTPLPRESDET_FECPAGO(fecemi);

            if (i + 1 == nrocuotas) {
                Double ultimacuota = Redondear(montototal - cuotasumado,2);
             
                objManPrestamo.setTPLPRESDET_MONTCUOTA(ultimacuota);
            } else {
                objManPrestamo.setTPLPRESDET_MONTCUOTA(montcuota);
            }
            objManPrestamo.setTPLPRESDET_NROCUOTA(nro_cuota);
            objManPrestamo.setTPLPRESDET_SALCUOTA(total);
            objManPrestamo.setTPLPRESDET_SITUCUOTA(1);
            objManPrestamo.setTPLPRESDET_IND(1);
            cuotasumado = cuotasumado + montcuota;
            objlstManPresPerDet.add(objManPrestamo);

        }

        return objlstManPresPerDet;

    }
	
    /**
     * Metodo para haller dia final
     *
     * @param anio
     * @param mes
     * @autor junior fernandez
     * @version 15012018
     * @return
     */
    public Date fechaFinalMayoCTS(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 2, 1);
        cal.set(anio, mes - 2, cal.getActualMaximum(Calendar.DAY_OF_MONTH));//prueba

        return cal.getTime();
    }

    public double Redondear(double numero, int digitos) {
        int cifras = (int) Math.pow(10, digitos);
        return Math.rint(numero * cifras) / cifras;
    }

    public void llenaListaDetalle() throws SQLException {
        objlstManPresPerDet = null;
        objlstManPresPerDet = objDaoManPresPer.listaDetalle(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), objManPresper.getTPLPRESCAB_ID());
        lst_detpres.setModel(objlstManPresPerDet);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapresper.setDisabled(b_valida1);
        tab_presper.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapresper.setSelected(b_valida1);
        tab_presper.setSelected(b_valida2);
    }

    public String verificar() {
        String s_valor = "";
        if (s_estado.equals("N")) {
            if (d_fecemi.getValue() == null) {
                s_valor = "El campo FECHA DE EMISIÓN es obligatorio";
                campo = "fecemi";
            } else if (txt_codper_man.getValue().isEmpty()) {
                s_valor = "El campo PERSONAL es obligatorio";
                campo = "personal";
            } else if (txt_nrocuotas.getValue() == null) {
                s_valor = "El campo #CUOTAS es obligatorio";
                campo = "nrocuotas";
            } else if (txt_nrocuotas.getValue() <= 0) {
                s_valor = "El campo #CUOTAS debe ser mayor a cero";
                campo = "nrocuotas";
            } else if (txt_montotal.getValue() == null) {
                s_valor = "El campo MONTO TOTAL es obligatorio";
                campo = "montotal";
            } else if (txt_montotal.getValue() <= 0) {
                s_valor = "El campo MONTO TOTAL debe ser mayor a cero";
                campo = "montotal";
            }

        }
        return s_valor;

    }

    public void habilitaCampos(boolean b_valida) {
        d_fecemi.setDisabled(b_valida);
        txt_codper_man.setDisabled(b_valida);
        txt_nrocuotas.setDisabled(b_valida);
        txt_montotal.setDisabled(b_valida);

    }

    public void limpiarCampos() {
        txt_nropres.setValue("");
        d_fecemi.setValue(null);
        txt_codper_man.setValue("");
        txt_desper_man.setValue("");
        txt_nrocuotas.setValue(null);
        txt_montotal.setValue(null);
        txt_adj_docpres.setValue("");

    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarListaDetalle() {
        //limpio mi lista
        objlstManPresPerDet = null;
        objlstManPresPerDet = new ListModelList<ManPresPerDet>();
        lst_detpres.setModel(objlstManPresPerDet);
    }

    public void LimpiarListaCabecera() {
        //limpio mi lista
        objlstManPresPer = null;
        objlstManPresPer = new ListModelList<ManPresPer>();
        lst_prestamo.setModel(objlstManPresPer);
    }

    @Listen("onOK=#txt_idarea")
    public void Next_2() {
        txt_nrocuotas.focus();
    }
//se desbloqueo para paso de focus

    @Listen("onOK=#txt_montotal")
    public void onOK_txt_montotal() throws SQLException {
        //onBlur_txt_montotal();
        txt_nrocuotas.focus();
    }

    /* @Listen("onBlur=#txt_montotal")
     public void onBlur_txt_montotal() throws SQLException {
     //onOK_txt_montotal();
     String respuesta = "";
     respuesta = verificarDatos();
     if (!respuesta.equals("")) {
     Messagebox.show(respuesta, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     objlstManPresPerDet = calcularCuotas(d_fecemi.getValue(), txt_montotal.getValue(), txt_nrocuotas.getValue());
     lst_detpres.setModel(objlstManPresPerDet);
     }
     }*/

    /*
     @Listen("onOK=#txt_nrocuotas")
     public void onOK_txt_nrocuotas() throws SQLException {
     tbbtn_adjcar_docpres.focus();
     }
     */
@Listen("onBlur=#txt_nrocuotas")
    public void onBlur_txt_nrocuotas() throws SQLException {
      /*  String respuesta;
        respuesta = verificarDatos();
        if (!respuesta.equals("")) {
            Messagebox.show(respuesta, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            txt_montotal.focus();
        } else {
            objlstManPresPerDet = calcularCuotas(d_fecemi.getValue(), txt_montotal.getValue(), txt_nrocuotas.getValue());
            lst_detpres.setModel(objlstManPresPerDet);
        }*/

        String s_valida = verificarDatos();
        // Si hay algun error de validacion envia el foco al campo obligatorio
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("personal")) {
                            txt_codper_man.focus();
                        } else if (campo.equals("fecemi")) {
                            d_fecemi.focus();
                        } else if (campo.equals("montotal")) {
                            txt_montotal.focus();
                        } else if (campo.equals("nrocuotas")) {
                            txt_nrocuotas.focus();
                        }
                    }
                }
            });
        } else {
            objlstManPresPerDet = calcularCuotas(d_fecemi.getValue(), txt_montotal.getValue(), txt_nrocuotas.getValue());
            lst_detpres.setModel(objlstManPresPerDet);
        }

    }

    public String verificarDatos() {
        String s_valor = "";
        if (d_fecemi.getValue() == null) {
            s_valor = "El campo FECHA DE EMISIÓN es obligatorio";
            //d_fecemi.setFocus(true);
            campo = "fecemi";
        } else if (txt_nrocuotas.getValue() == null) {
            s_valor = "El campo #CUOTAS es obligatorio";
            // txt_nrocuotas.setFocus(true);
            campo = "nrocuotas";
        } else if (txt_nrocuotas.getValue() <= 0) {
            s_valor = "El campo #CUOTAS debe ser mayor a cero";
            // txt_nrocuotas.setFocus(true);
            campo = "nrocuotas";
        } else if (txt_montotal.getValue() == null) {
            s_valor = "El campo MONTO TOTAL es obligatorio";
            // txt_montotal.setFocus(true);
            campo = "montotal";
        } else if (txt_montotal.getValue() <= 0) {
            s_valor = "El campo MONTO TOTAL debe ser mayor a cero";
            // txt_montotal.setFocus(true);
            campo = "montotal";
        }
        return s_valor;

    }

    /*
     if (txt_nrocuotas.getValue() == null) {
     Messagebox.show("Ingresar #cuotas para calcular", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     objlstManPresPerDet = calcularCuotas(d_fecemi.getValue(), txt_montotal.getValue(), txt_nrocuotas.getValue());
     lst_detpres.setModel(objlstManPresPerDet);
     }
     */
    /*
     else if (txt_montotal.getValue() == null) {
     Messagebox.show("Ingresar monto total para calcular", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else if (txt_montotal.getValue() <= 0) {
     Messagebox.show("El monto total debe ser mayor a cero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } 
     */
    //LOV PERSONAL MANT
    @Listen("onOK=#txt_codper_man")
    public void busquedaPersonalMan() {
        if (bandera == false) {
            bandera = true;
            if (txt_codper_man.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper_man);
                objMapObjetos.put("des_per", txt_desper_man);
                objMapObjetos.put("controlador", "ControllerManPresPer");
                //Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovPersonal.zul", null, objMapObjetos);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }

        }
        txt_montotal.focus();

    }

    @Listen("onBlur=#txt_codper_man")
    public void valida_PersonalMan() throws SQLException {
        if (!txt_codper_man.getValue().isEmpty()) {
            String id = txt_codper_man.getValue();
            objPersonal = objDaoManPresPer.getLovPersonal(id);
            if (objPersonal == null) {
                Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_codper_man.setValue(null);
                        txt_codper_man.setValue(null);
                        txt_codper_man.focus();
                        txt_desper_man.setValue("");
                    }
                });

            } else {
                txt_codper_man.setValue(objPersonal.getPlidper());
                txt_desper_man.setValue(objPersonal.getPldesper());

            }

        } else {
            txt_desper_man.setValue("");

        }
        bandera = false;
    }

    //ADJUNTOS
    @Listen("onClick=#tbbtn_adjpdf_docpres")
    public void mostrarPdfAntPol() throws FileNotFoundException {
        String documento = txt_codper_man.getValue();
        generarPdf(rutaFileAcuPres + documento + ".pdf");
    }

    public void generarPdf(String ruta) throws FileNotFoundException {
        if (txt_codper_man.getValue().isEmpty()) {
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

    @Listen("onUpload=#tbbtn_adjcar_docpres")
    public void onUploadAntPol(UploadEvent event) throws Exception {
        if (txt_codper_man.getValue().isEmpty()) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (s_estado.equals("M")) {
            mediaAcuPres = event.getMedia();
            if (mediaAcuPres instanceof Media) {

                if (mediaAcuPres.getName().contains(".pdf")) {

                    String documento = txt_codper_man.getValue();
                    txt_adj_docpres.setValue(rutaFileAcuPres + documento + ".pdf");
                    archivoAcuPres = new File(txt_adj_docpres.getValue());
                    cargaAcuPres = true;
                } else {
                    Messagebox.show("NO SE CARGÓ. Seleccionar documento en formato pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    /*
     //GENERAR ACUERDO DE PRESTAMO
     public void Prueba() throws IOException, XDocReportException {

     String rutaPlantilla = "GJ_CARPETAS\\PLANILLAS\\TEMPLATES\\Acuerdo.odt";
     String extension = "odt";

     generarDocumento(rutaPlantilla, extension, false);

     }
     */
    private void generarDocumento(String rutaPlantilla, String extension, boolean convertirPdf) throws IOException, XDocReportException, XDocConverterException {
        //FORMATO FECHA//
        //DateFormat formatoFecha = DateFormat.getDateInstance(DateFormat.FULL);
        SimpleDateFormat formateador = new SimpleDateFormat(" EEEEEEEEE dd 'de' MMMMM 'del' yyyy");
        SimpleDateFormat formateador2 = new SimpleDateFormat(" MMMMM 'en' yyyy");

        // Mapa con las variables a reemplazar
        Map<String, Object> variablesMap = new HashMap<String, Object>();
        variablesMap.put("PERSONAL", txt_desper_man.getValue());
        variablesMap.put("DNI", StringFns.substring(txt_codper_man.getValue(), 1, 9).trim());
        variablesMap.put("MONTOTAL", "S/." + txt_montotal.getValue() + " " + "Nuevos Soles");
        variablesMap.put("EMPRESA", objPersonal.getEmp_des());
        variablesMap.put("RUC", objPersonal.getEmp_ruc());
        variablesMap.put("REPRESENTANTE", objPersonal.getEmp_repleg());
        variablesMap.put("DNIREP", objPersonal.getEmp_dnirep());
        variablesMap.put("FECEMI", formateador.format(d_fecemi.getValue()));
        variablesMap.put("CARGO", objPersonal.getCargo());
        variablesMap.put("DIRECCION", objPersonal.getDireccion());
        variablesMap.put("FECHAING", formateador2.format(objPersonal.getFechaing()));

        variablesMap.put("listaNumeros", this.construirListaDetalle());
        variablesMap.put("listaFechas", this.construirListaDetalle());
        variablesMap.put("listaMontos", this.construirListaDetalle());

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList("listaNumeros.TPLPRESDET_NROCUOTA");
        metadata.addFieldAsList("listaNumeros.TPLPRESDET_FECPAGO");
        metadata.addFieldAsList("listaNumeros.TPLPRESDET_MONTCUOTA");

        // 2) Create fields metadata to manage lazy loop (#forech velocity)
        // for table row. {num.fecha}
        // Mapa con las variables de tipo imagen. Estas variables contienen el path de la imagen
        Map<String, String> imagenesMap = new HashMap<String, String>();
        imagenesMap.put("header_image_logo", "./Logo.png");

        GeneradorDocumentoAcuerdoPres generadorDocumentosService = new GeneradorDocumentoAcuerdoPres();
        byte[] mergedOutput = generadorDocumentosService.generarDocumento(rutaPlantilla,
                TemplateEngineKind.Freemarker, variablesMap, imagenesMap, convertirPdf, metadata
        );
        //Nombre del archivo ODT
        String nombre_archivo_odt = "AP_" + txt_codper_man.getValue() + "_" + sdfpres.format(d_fecemi.getValue()) + ".";
        // Se escribe el documento de salida
        FileOutputStream os = new FileOutputStream(rutaFileAcuPres + nombre_archivo_odt + extension);
        os.write(mergedOutput);
        os.flush();
        os.close();
        //Generamos el archivo PDF
        try {
            //String ruta_defecto_bat = rutaFileAcuPres + "generaPDF.bat";
            Runtime runtime = Runtime.getRuntime();
            String sentencia_batch = "cmd start /c " + rutaBatch + " " + rutaFileAcuPres + nombre_archivo_odt + extension + " " + rutaFileAcuPres + nombre_archivo_odt + "pdf";
            process = runtime.exec(sentencia_batch);
            int x = process.waitFor();
        } catch (IOException j) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + j, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (InterruptedException j) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + j, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private List<ManPresPerDet> construirListaDetalle() {
        List<ManPresPerDet> numeros = new LinkedList<ManPresPerDet>();
        for (int i = 0; i < objlstManPresPerDet.getSize(); i++) {
            numeros.add(new ManPresPerDet(objlstManPresPerDet.get(i).getTPLPRESDET_NROCUOTA(), objlstManPresPerDet.get(i).getTPLPRESDET_FECPAGO(), /*objlstManPresPerDet.get(i).getSTPLPRESDET_FECPAGO(),*/ objlstManPresPerDet.get(i).getTPLPRESDET_MONTCUOTA()));
        }
        return numeros;
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws FileNotFoundException, IOException {

        if (txt_codper_man.getValue().isEmpty()) {
            Messagebox.show("Por favor verifique el tipo o número de documento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String nombre_archivo_pdf = rutaFileAcuPres + "AP_" + txt_codper_man.getValue() + "_" + sdfpres.format(d_fecemi.getValue()) + ".pdf";
            File salida = new File(nombre_archivo_pdf);
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

        /*
         File archivo = null;
         FileReader fr = null;
         BufferedReader br = null;

         int i = 0;

         try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).

         archivo = new File(rutaFileAcuPres + "AP_" + txt_codper_man.getValue() + "_" + sdfpres.format(d_fecemi.getValue()) + ".odt");
         fr = new FileReader(archivo);
         br = new BufferedReader(fr);

         } catch (Exception e) {
         e.printStackTrace();
         } finally {
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try {
         if (null != fr) {
         Desktop.getDesktop().open(new File("D:\\GJ_CARPETAS\\PLANILLAS\\ACUPRES\\AP_" + txt_codper_man.getValue() + "_" + sdfpres.format(d_fecemi.getValue()) + ".odt"));
         fr.close();
         }
         } catch (Exception e2) {
         e2.printStackTrace();
         }
         }
         */
        // }
    }

    public void main(String[] arg) throws FileNotFoundException {
        File archivo;
        FileReader fr = null;
        BufferedReader br;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("D:\\AcuerdoPrestamo_" + txt_codper_man.getValue() + ".odt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                //e2.printStackTrace();
            }
        }
    }

    public void convertirPDF() throws FileNotFoundException, XDocConverterException {
        // 1) Create options ODT 2 PDF to select well converter form the registry
        Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);

// 2) Get the converter from the registry
        IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

// 3) Convert ODT 2 PDF
        InputStream in = new FileInputStream(new File("D:\\AcuerdoPrestamo_" + txt_codper_man.getValue() + ".odt"));
        OutputStream out = new FileOutputStream(new File("D:\\AcuerdoPrestamo_" + txt_codper_man.getValue() + ".pdf"));
        converter.convert(in, out, options);

    }

    /**
     * Modificamos para cambiar fecha de pago de prestamo
     *
     * @version 19/08/2017
     * @throws java.sql.SQLException
     */
    @Listen("onSelect=#lst_detpres")
    public void seleccionaRegistroLeaDet() throws SQLException {
        objManPresperDet = (ManPresPerDet) lst_detpres.getSelectedItem().getValue();
        if (objManPresperDet == null) {
            objManPresperDet = objlstManPresPerDet.get(i_sel + 1);
        }
        i_sel = lst_detpres.getSelectedIndex();
        llenarCampoDet(objManPresperDet);
    }

    /**
     * Metodo para editar la fecha de pago del prestamo
     *
     * @version 21/08/2017
     */
    @Listen("onClick=#tbbtn_btn_editar3")
    public void editarDetalle() {
        if (lst_detpres.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objManPresperDet.getTPLPRESDET_IND() != 1) {
                Messagebox.show("Prestamo ya se encuentra enlazado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                d_estado = "M";
                d_dpago.setDisabled(false);
                d_dpago.focus();
                habilitaBotonesDetalle(true, false);
            }

        }
    }

    /**
     * Metodo para guardar luego de modificar la fecha de pago
     *
     * @version 31/08/2017
     */
    @Listen("onClick=#tbbtn_btn_guardar3")
    public void guardarDetalle() {
        mensaje = "Esta seguro que desea guardar";
        Messagebox.show(mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            Date fecha = d_dpago.getValue();

                            if (s_estado.equals("N")) {
                                objManPresperDet.setTPLPRESDET_FECPAGO(fecha);
                                /* objManPresperDet = new ManPresPerDet(fecha);
                                 objlstManPresPerDet.add(objManPresperDet);
                                 lst_detpres.setModel(objlstManPresPerDet);*/
                                objlstManPresPerDet.set(lst_detpres.getSelectedIndex(), objManPresperDet);
                            } else {
                                objManPresperDet.setTPLPRESDET_FECPAGO(fecha);
                                objManPresperDet.setTPLINDACCION("M");
                                objlstManPresPerDet.set(lst_detpres.getSelectedIndex(), objManPresperDet);

                            }
                            habilitaBotonesDetalle(false, true);
                            limpiarCamposDetalle();
                            lst_detpres.clearSelection();
                            d_dpago.setDisabled(true);
                        }
                    }
                });
    }

    /**
     * Metodo para retroceder en el detalle
     *
     * @version 21/08/2017
     */
    @Listen("onClick=#tbbtn_btn_deshacer3")
    public void deshacerDetalle() {
        d_estado = "A";
        String s_str = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesDetalle(false, true);
                            limpiarCamposDetalle();
                            Utilitarios.deshabilitarLista(false, lst_detpres);
                            lst_detpres.clearSelection();
                            d_dpago.setDisabled(true);
                        }
                    }
                ;
    }

    );
    }
    /**
     * Metodos generales para la edicion del detalle del prestamo
     * @param b_valida1
     * @param b_valida2 
     */
        public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        // tbbtn_btn_nuevo3.setDisabled(b_valida1);
        tbbtn_btn_guardar3.setDisabled(b_valida2);
        tbbtn_btn_deshacer3.setDisabled(b_valida2);
        tbbtn_btn_editar3.setDisabled(b_valida1);
        // tbbtn_btn_eliminar3.setDisabled(b_valida1);
    }

    //limpia campos detalle
    public void limpiarCamposDetalle() {
        d_dncuota.setValue(null);
        d_dcuota.setValue(null);
        d_dacumulado.setValue(null);
        d_dpago.setValue(null);
        txt_dsituacion.setValue("");
        txt_dindicador.setValue("");
    }

    //completamos los campos luego de selecionar la lista detalle
    public void llenarCampoDet(ManPresPerDet x) {
        d_dncuota.setValue(x.getTPLPRESDET_NROCUOTA());
        d_dcuota.setValue(x.getTPLPRESDET_MONTCUOTA());
        d_dacumulado.setValue(x.getTPLPRESDET_SALCUOTA());
        d_dpago.setValue(x.getTPLPRESDET_FECPAGO());
        txt_dsituacion.setValue(x.getSTPLPRESDET_SITUCUOTA());
        txt_dindicador.setValue(x.getSTPLPRESDET_IND());
    }
}
