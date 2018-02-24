package org.me.gj.controller.planillas.mantenimiento;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import org.me.gj.controller.cxc.mantenimiento.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.mantenimiento.PlantillaContrato;
import org.me.gj.model.planillas.mantenimiento.TipContrato;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerContratos extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Tab tab_listacontrato, tab_contrato;
    @Wire
    Listbox lst_contrato;
    @Wire
    Textbox txt_busqueda,
            txt_empresa, txt_ruc, txt_domicilio, txt_distrito,
            txt_perNroDoc, txt_perNom, txt_perDir, txt_perCargo, txt_conAct,
            txt_usuadd, txt_usumod, txt_idtipcont, txt_destipcont, txt_codigo;
    @Wire
    Datebox d_fecadd, d_fecmod,//Auditoría
            d_conFecIni, d_conFecFin;
    @Wire
    Intbox ib_conMeses, ib_conRem;

    @Wire
    Button btn_coordenadasDomi;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;

    @Wire
    Button btn_consultar;

    @Wire
    Combobox cb_estado, cb_busqueda, cb_area , cb_diasfiltro;

    //Instancias de Objetos
    ListModelList<PlantillaContrato> objlstPlaContrato, objlstContratoValidar;
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<TipoDocumento> objlstTipoDocumento;

    PlantillaContrato objPlaContrato;

    DaoContrato objDaoContrato;
    DaoCliente objDaoCliente;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    TipoDocumento objTipoDocumento;

    ParametrosSalida objParamSalida;

    //LOV PERSONAL
    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;
    DaoPersonal objDaoPersonal = new DaoPersonal();

    //LOV TIPO CONTRATO
    ListModelList<TipContrato> objlstTipContrato;
    TipContrato objTipContrato;

    //Variables publicas
    String s_estado = "Q";

    String modoEjecucion;
    String s_mensaje = "";
    String foco = "";
    String focoDerHab = "";
    String tab = "";

    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileContratos = rutaFile + "CONTRATOS\\";
    String rutaBatch = rutaFile + "TEMPLATES\\generaPDF.bat";

    //String motivoCese, ceseDet, ceseObs;
    Map parametros;

    Media mediaAntPol;

    File archivoAntPol;

    int i_sel, emp_id, suc_id;
    public static boolean bandera = false;
    boolean cargaAntPol = false;

    SimpleDateFormat ffecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfcont = new SimpleDateFormat("ddMMyyyy");

    SimpleDateFormat formateador = new SimpleDateFormat(" EEEEEEEEE dd 'de' MMMMM 'del' yyyy");
    SimpleDateFormat formateador2 = new SimpleDateFormat(" MMMMM 'en' yyyy");
    SimpleDateFormat formateador3 = new SimpleDateFormat(" dd 'de' MMMMM 'del' yyyy");
    Process process;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCliente.class);

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90103000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Utilidad con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Utilidad con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Utilidad");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Utilidad");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Utilidad");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Utilidad");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Utilidad");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Utilidad");
        }
    }

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_contratos")
    public void llenaRegistros() throws SQLException {
        cb_estado.setSelectedIndex(2);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
		objDaoPersonal = new DaoPersonal();

        objDaoCliente = new DaoCliente();
        objDaoContrato = new DaoContrato();
        objDaoAccesos = new DaoAccesos();
        objAccesos = new Accesos();
        objlstPlaContrato = objDaoContrato.listaContratos(objUsuCredential.getCodemp());
        lst_contrato.setModel(objlstPlaContrato);
		objlstContratoValidar = new ListModelList<PlantillaContrato>();
        objlstPersonal = objDaoContrato.busquedaLovPersonal();
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
		objlstContratoValidar = objlstPlaContrato;
		
        bandera = false;

    }

    @Listen("onSelect=#lst_contrato")
    public void seleccionaRegistroCab() throws SQLException {
        limpiarCampos();
        objPlaContrato = (PlantillaContrato) lst_contrato.getSelectedItem().getValue();
        if (objPlaContrato == null) {
            objPlaContrato = objlstPlaContrato.get(i_sel + 1);
        }
        i_sel = lst_contrato.getSelectedIndex();
        habilitaCampos(true);
        llenarCabecera(objPlaContrato);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        objPlaContrato = new PlantillaContrato();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_distrito.focus();
        d_conFecIni.setValue(Utilitarios.hoyAsFecha());
        txt_empresa.setDisabled(true);
        txt_domicilio.setDisabled(true);
        txt_ruc.setDisabled(true);
        txt_perNom.setDisabled(true);
        txt_perCargo.setDisabled(true);
        txt_perDir.setDisabled(true);
        txt_destipcont.setDisabled(true);
        d_conFecFin.setDisabled(true);
        txt_codigo.setDisabled(true);
        //carga la empresa
        objlstPlaContrato = objDaoContrato.listaEmpresa(objUsuCredential.getCodemp());
        txt_empresa.setValue(objlstPlaContrato.get(0).getPar01_empnom());
        txt_ruc.setValue(objlstPlaContrato.get(0).getPar02_empruc());
        txt_domicilio.setValue(objlstPlaContrato.get(0).getPar03_empdom());
        //estado
        s_estado = "N";

    }

@Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws IOException, XDocReportException, SQLException {

        String s_valida = verificar();
        // Si hay algun error de validacion envia el foco al campo obligatorio
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("distrito")) {
                            txt_distrito.focus();
                        } else if (foco.equals("dni")) {
                            txt_perNroDoc.focus();
                        } else if (foco.equals("dirper")) {
                            txt_perNroDoc.focus();
                        } else if (foco.equals("cargoper")) {
                            txt_perNroDoc.focus();
                        } else if (foco.equals("meses")) {
                            ib_conMeses.focus();
                        } else if (foco.equals("fecini")) {
                            d_conFecIni.focus();
                        } else if (foco.equals("fecfin")) {
                            d_conFecFin.focus();
                        } else if (foco.equals("remu")) {
                            ib_conRem.focus();
                        } else if (foco.equals("idtip")) {
                            txt_idtipcont.focus();
                        } else if (foco.equals("actvi")) {
                            txt_conAct.focus();
                        }
                    }
                }
            });
        } else if (!validaFechaVacacionesModificar(objlstContratoValidar) && s_estado.equals("N")) { //----------------------------------------------------------MODIFICADO----------------------------------------------------
            Messagebox.show("Ya existe un contrato del trabajador en esa fecha", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                objlstPlaContrato = objDaoContrato.listaContratos(objUsuCredential.getCodemp());
                                lst_contrato.setModel(objlstPlaContrato);
                                objPlaContrato = new PlantillaContrato();
                                lst_contrato.focus();
                                s_estado = "Q";
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParam;// = null;
                                objlstPlaContrato = new ListModelList<PlantillaContrato>();
                                objPlaContrato = (PlantillaContrato) generaDatosContrato();

                                if (s_estado.equals("N")) {
                                    objParam = objDaoContrato.insertarContrato(objPlaContrato);

                                } else {
                                    objParam = objDaoContrato.modificarContrato(objPlaContrato);
                                }

                                if (objParam.getFlagIndicador() == 0) {
                                    //DOCUMENTOS
                                    if (s_estado.equals("M")) {
                                        Prueba2();
                                    }
                                    if (s_estado.equals("N")) {
                                        Prueba(objParam.getCodigoRegistro());
                                    }
                                    limpiarListaRegistro();

                                }
                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                objlstPlaContrato = objDaoContrato.listaContratos(objUsuCredential.getCodemp());
                                lst_contrato.setModel(objlstPlaContrato);
                                objPlaContrato = new PlantillaContrato();
                                lst_contrato.focus();
                                s_estado = "Q";
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_contrato.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            txt_codigo.setDisabled(true);
            txt_conAct.setDisabled(false);
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            txt_conAct.focus();

        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_contrato.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccionar un registro", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                            }
                        }
                    });

        } else {
            s_mensaje = "Está seguro que desea eliminar el contrato?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", 3, "z-msgbox z-msgbox-question", new EventListener() {
                public void onEvent(Event evento) throws SQLException {
                    if (((Integer) evento.getData()).intValue() == 1) {
                        ParametrosSalida objParamSalida = objDaoContrato.eliminarContrato(objPlaContrato);
                        if (objParamSalida.getFlagIndicador() == 0) {
                            objlstPlaContrato.clear();
                            objlstPlaContrato = objDaoContrato.listaContratos(objUsuCredential.getCodemp());
                            lst_contrato.setModel(objlstPlaContrato);
                            lst_contrato.focus();
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

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() throws SQLException {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_contrato.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            lst_contrato.focus();
                            //
                            habilitaCampos(true);
                            limpiaAuditoria();
                            s_estado = "Q";

                        }
                    }
                });

    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }
	
    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarListaRegistro();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();        	
		
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstPlaContrato = new ListModelList<PlantillaContrato>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;

        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;

        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;

        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;

        }

        //objlstPlaContrato = objDaoContrato.consultaContratos(i_seleccion, s_consulta, i_estado,  ,area);
        if (cb_diasfiltro.getSelectedIndex() == 0 || cb_diasfiltro.getSelectedIndex() == -1) {
            objlstPlaContrato = objDaoContrato.consultaContratos(i_seleccion, s_consulta, i_estado, 10000 , area );

        } else {
            objlstPlaContrato = objDaoContrato.consultaContratos(i_seleccion, s_consulta, i_estado, Integer.parseInt(cb_diasfiltro.getValue()) , area);
        }

        if (objlstPlaContrato.getSize() > 0) {
            lst_contrato.setModel(objlstPlaContrato);
        } else {
            objlstPlaContrato = null;
            lst_contrato.setModel(objlstPlaContrato);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws FileNotFoundException, IOException, XDocReportException {
        if (lst_contrato.getSelectedIndex() == -1) {
            Messagebox.show("Debe seleccionar un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String nombre_archivo_pdf = rutaFileContratos + "Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + "." + "pdf";
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

         archivo = new File("D:\\Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + ".odt");
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
         Desktop.getDesktop().open(new File("D:\\Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + ".odt"));
         fr.close();
         }
         } catch (Exception e2) {
         e2.printStackTrace();
         }
         }
         */
    }

    public void Prueba(String codigoRegistro) throws IOException, XDocReportException {

        //String rutaPlantilla = "GJ_CARPETAS\\PLANILLAS\\TEMPLATES\\Contrato.odt";//false
        String rutaPlantilla = rutaFile + "TEMPLATES\\Contrato.odt";
        String extension = "odt";

        generarDocumento(rutaPlantilla, codigoRegistro, extension, false);//false
    }

    private void generarDocumento(String rutaPlantilla, String codigoRegistro, String extension, boolean convertirPdf) throws IOException, XDocReportException {

        //FORMATO DE FECHAS
        DateFormat formatoFecha = DateFormat.getDateInstance(DateFormat.FULL);

        // Mapa con las variables a reemplazar
        Map<String, String> variablesMap = new HashMap<String, String>();
        variablesMap.put("idcont", codigoRegistro.toString());
        variablesMap.put("empNombre", txt_empresa.getValue().toUpperCase());
        variablesMap.put("empRuc", txt_ruc.getValue());
        variablesMap.put("empDom", txt_domicilio.getValue().toUpperCase());
        variablesMap.put("empDis", txt_distrito.getValue().toUpperCase());
        variablesMap.put("replegal", objPersonal.getEmp_repleg());//objPersonal.getEmp_repleg());//objPersonal
        variablesMap.put("dnirep", objPersonal.getEmp_dnirep());//objPersonal.getEmp_dnirep());//objPersonal
        //
        variablesMap.put("perDocumento", StringFns.substring(txt_perNroDoc.getValue(), 1, 9));
        variablesMap.put("perNombre", txt_perNom.getValue().toUpperCase());
        variablesMap.put("perDireccion", txt_perDir.getValue().toUpperCase());
        variablesMap.put("perCargo", txt_perCargo.getValue().toUpperCase());
        //
        variablesMap.put("conMeses", String.valueOf(ib_conMeses.getValue()));
        variablesMap.put("conFecIni", formateador3.format(d_conFecIni.getValue()));
        variablesMap.put("conFecFin", formateador3.format(d_conFecFin.getValue()));
        variablesMap.put("conRem", "S/." + String.valueOf(ib_conRem.getValue()) + " " + "Nuevos Soles");
        variablesMap.put("conAct", String.valueOf(txt_conAct.getValue()).toUpperCase());

        variablesMap.put("conFecFirm", formateador3.format(Utilitarios.hoyAsFecha()));
        // 2) Create fields metadata to manage lazy loop (#forech velocity)
        // for table row.
        FieldsMetadata metadata = new FieldsMetadata();

        // Mapa con las variables de tipo imagen. Estas variables contienen el path de la imagen
        Map<String, String> imagenesMap = new HashMap<String, String>();
        imagenesMap.put("header_image_logo", "./Logo.png");

        GeneradorDocumentosService generadorDocumentosService = new GeneradorDocumentosService();
        byte[] mergedOutput = generadorDocumentosService.generarDocumento(rutaPlantilla,
                TemplateEngineKind.Freemarker, variablesMap, imagenesMap, convertirPdf, metadata
        );

        // Se comprueba que se ha generado el documento
        //assertNotNull(mergedOutput);
        String nombre_archivo_odt = "Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + ".";
        // Se escribe el documento de salida
        FileOutputStream os = new FileOutputStream(rutaFileContratos + nombre_archivo_odt + extension);
        os.write(mergedOutput);
        os.flush();
        os.close();
        //Generamos el archivo PDF
        try {
            //String ruta_defecto_bat = rutaFileContratos + "generaPDF.bat";
            Runtime runtime = Runtime.getRuntime();
            String sentencia_batch = "cmd start /c " + rutaBatch + " " + rutaFileContratos + nombre_archivo_odt + extension + " " + rutaFileContratos + nombre_archivo_odt + "pdf";
            process = runtime.exec(sentencia_batch);
            int x = process.waitFor();
        } catch (Exception e) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + e, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public void Prueba2() throws IOException, XDocReportException {

        //String rutaPlantilla = "GJ_CARPETAS\\PLANILLAS\\TEMPLATES\\Contrato.odt";//false
        String rutaPlantilla = rutaFile + "TEMPLATES\\Contrato.odt";
        String extension = "odt";

        generarDocumento2(rutaPlantilla, extension, false);
    }

    private void generarDocumento2(String rutaPlantilla, String extension, boolean convertirPdf) throws IOException, XDocReportException {

        //FORMATO DE FECHAS
        // DateFormat formatoFecha = DateFormat.getDateInstance(DateFormat.FULL);
        // Mapa con las variables a reemplazar
        Map<String, String> variablesMap = new HashMap<String, String>();
        variablesMap.put("idcont", objPlaContrato.getId_contrato());
        variablesMap.put("empNombre", txt_empresa.getValue().toUpperCase());
        variablesMap.put("empRuc", txt_ruc.getValue());
        variablesMap.put("empDom", txt_domicilio.getValue().toUpperCase());
        variablesMap.put("empDis", txt_distrito.getValue().toUpperCase());
        variablesMap.put("replegal", objlstPersonal.get(0).getEmp_repleg());///
        variablesMap.put("dnirep", objlstPersonal.get(0).getEmp_dnirep());
        //
        variablesMap.put("perDocumento", String.valueOf(objlstPersonal.get(0).getPltipdoc()));//StringFns.substring(txt_perNroDoc.getValue(), 1, 9));
        variablesMap.put("perNombre", txt_perNom.getValue().toUpperCase());
        variablesMap.put("perDireccion", txt_perDir.getValue().toUpperCase());
        variablesMap.put("perCargo", txt_perCargo.getValue().toUpperCase());
        //
        variablesMap.put("conMeses", String.valueOf(ib_conMeses.getValue()));
        variablesMap.put("conFecIni", formateador3.format(d_conFecIni.getValue()));
        variablesMap.put("conFecFin", formateador3.format(d_conFecFin.getValue()));
        variablesMap.put("conRem", "S/." + String.valueOf(ib_conRem.getValue()) + " " + "Nuevos Soles");
        variablesMap.put("conAct", String.valueOf(txt_conAct.getValue()).toUpperCase());

        variablesMap.put("conFecFirm", formateador3.format(Utilitarios.hoyAsFecha()));
        // 2) Create fields metadata to manage lazy loop (#forech velocity)
        // for table row.
        FieldsMetadata metadata = new FieldsMetadata();

        // Mapa con las variables de tipo imagen. Estas variables contienen el path de la imagen
        Map<String, String> imagenesMap = new HashMap<String, String>();
        imagenesMap.put("header_image_logo", "./Logo.png");

        GeneradorDocumentosService generadorDocumentosService = new GeneradorDocumentosService();
        byte[] mergedOutput = generadorDocumentosService.generarDocumento(rutaPlantilla,
                TemplateEngineKind.Freemarker, variablesMap, imagenesMap, convertirPdf, metadata
        );

        // Se comprueba que se ha generado el documento
        //assertNotNull(mergedOutput);
        String nombre_archivo_odt = "Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + ".";
        // Se escribe el documento de salida
        //FileOutputStream os = new FileOutputStream("D:\\Contrato_" + txt_perNroDoc.getValue() + "_" + sdfcont.format(d_conFecIni.getValue()) + "." + extension);
        FileOutputStream os = new FileOutputStream(rutaFileContratos + nombre_archivo_odt + extension);
        os.write(mergedOutput);
        os.flush();
        os.close();
        //Generamos el archivo PDF
        try {
            String ruta_defecto_bat = rutaFileContratos + "generaPDF.bat";
            Runtime runtime = Runtime.getRuntime();
            String sentencia_batch = "cmd start /c " + ruta_defecto_bat + " " + rutaFileContratos + nombre_archivo_odt + extension + " " + rutaFileContratos + nombre_archivo_odt + "pdf";
            process = runtime.exec(sentencia_batch);
            int x = process.waitFor();
        } catch (Exception e) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + e, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public PlantillaContrato generaDatosContrato() {
        int par06_pertipdoc;
        String par07_pernumdoc;

        int estado = 1;
        String id_contrato = objPlaContrato.getId_contrato();
        String ct_tipo = txt_idtipcont.getValue();
        String par01_empnom = txt_empresa.getValue();
        String par02_empruc = txt_ruc.getValue();
        String par03_empdom = txt_domicilio.getValue();
        // String par07_pernumdoc = txt_perNroDoc.getValue();//tipo y dni
        if (s_estado.equals("N")) {
            par06_pertipdoc = objPersonal.getPltipdoc();//tipo de doc
            par07_pernumdoc = objPersonal.getPlnrodoc();//tipo y dni
        } else {
            par06_pertipdoc = objPlaContrato.getPar06_pertipdoc();
            par07_pernumdoc = objPlaContrato.getPar07_pernumdoc();

        }

        String par08_pernom = txt_perNom.getValue();
        String par09_perdir = txt_perDir.getValue();
        String par10_percargo = txt_perCargo.getValue();
        int par13_conmeses = ib_conMeses.getValue();
        Date par14_confecini = d_conFecIni.getValue();
        Date par15_confecfin = d_conFecFin.getValue();
        int par16_conremu = ib_conRem.getValue();
        String par17_conact = txt_conAct.getValue();
        String par04_empxxx = txt_distrito.getValue();
        String ct_usumod = objUsuCredential.getCuenta();
        int empresa_id = objUsuCredential.getCodemp();

        return new PlantillaContrato(id_contrato, estado, ct_tipo, par01_empnom, par02_empruc, par03_empdom, par06_pertipdoc,
                par07_pernumdoc, par08_pernom, par09_perdir, par10_percargo, par13_conmeses, par14_confecini,
                par15_confecfin, par16_conremu, par17_conact, par04_empxxx, ct_usumod, empresa_id);
    }

    //metodos
    public void llenarCabecera(PlantillaContrato objPlaContrato) throws SQLException {

        txt_conAct.setValue(objPlaContrato.getPar17_conact());
        txt_perDir.setValue(objPlaContrato.getPar09_perdir());
        txt_domicilio.setValue(objPlaContrato.getPar03_empdom());
        txt_empresa.setValue(objPlaContrato.getPar01_empnom());
        txt_perCargo.setValue(objPlaContrato.getPar10_percargo());
        txt_perNom.setValue(objPlaContrato.getPar08_pernom());
        txt_perNroDoc.setValue(objPlaContrato.getPar06_pertipdoc() + objPlaContrato.getPar07_pernumdoc());
        txt_ruc.setValue(objPlaContrato.getPar02_empruc());
        d_conFecIni.setValue(objPlaContrato.getPar14_confecini());
        d_conFecFin.setValue(objPlaContrato.getPar15_confecfin());
        ib_conMeses.setValue(objPlaContrato.getPar13_conmeses());
        ib_conRem.setValue(objPlaContrato.getPar16_conremu());
        d_fecadd.setValue(objPlaContrato.getCt_fecadd());
        d_fecmod.setValue(objPlaContrato.getCt_fecmod());
        txt_usuadd.setValue(objPlaContrato.getCt_usuadd());
        txt_usumod.setValue(objPlaContrato.getCt_usumod());
        txt_distrito.setValue(objPlaContrato.getPar04_empxxx());
        txt_idtipcont.setValue(objPlaContrato.getCt_tipo());
        txt_destipcont.setValue(objPlaContrato.getDesct_tipo());
        txt_codigo.setValue(objPlaContrato.getId_contrato());
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
        tab_listacontrato.setDisabled(b_valida1);
        tab_contrato.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacontrato.setSelected(b_valida1);
        tab_contrato.setSelected(b_valida2);
    }

    public void limpiarCampos() {
        txt_conAct.setValue("");
        d_conFecIni.setValue(null);
        d_conFecFin.setValue(null);
        txt_distrito.setValue("");
        txt_domicilio.setValue("");
        txt_empresa.setValue("");
        txt_perCargo.setValue("");
        txt_perDir.setValue("");
        txt_perNom.setValue("");
        txt_perNroDoc.setValue(null);
        txt_ruc.setValue("");
        ib_conMeses.setValue(null);
        ib_conRem.setValue(null);
        txt_idtipcont.setValue("");
        txt_destipcont.setValue("");
        txt_codigo.setValue("");
    }

    public void habilitaCampos(boolean b_valida) {
        txt_codigo.setDisabled(b_valida);
        txt_conAct.setDisabled(b_valida);
        d_conFecIni.setDisabled(b_valida);
        d_conFecFin.setDisabled(b_valida);
        txt_distrito.setDisabled(b_valida);
        txt_domicilio.setDisabled(b_valida);
        txt_empresa.setDisabled(b_valida);
        txt_perCargo.setDisabled(b_valida);
        txt_perDir.setDisabled(b_valida);
        txt_perNom.setDisabled(b_valida);
        txt_perNroDoc.setDisabled(b_valida);
        txt_ruc.setDisabled(b_valida);
        ib_conMeses.setDisabled(b_valida);
        ib_conRem.setDisabled(b_valida);
        txt_destipcont.setDisabled(b_valida);
        txt_idtipcont.setDisabled(b_valida);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarListaRegistro() {
        objlstPlaContrato = null;
        objlstPlaContrato = new ListModelList<PlantillaContrato>();
        lst_contrato.setModel(objlstPlaContrato);

    }

    public String verificar() {
        String s_valor = "";

        if (txt_distrito.getValue().isEmpty()) {
            s_valor = "Por favor ingresar el distrito";
            foco = "distrito";
        } else if (txt_perNroDoc.getValue().isEmpty()) {
            s_valor = "Por favor ingresar el NRO.DOC";
            foco = "dni";
        } else if (txt_perDir.getValue().isEmpty()) {
            s_valor = "Por favor ingresar la dirección";
            foco = "dirper";
        } else if (txt_perCargo.getValue().isEmpty()) {
            s_valor = "Por favor ingresar el cargo";
            foco = "cargoper";
        } else if (ib_conMeses.getValue() == null) {
            s_valor = "Por favor ingresar los meses";
            foco = "meses";
        } else if (ib_conMeses.getValue() <= 0) {
            s_valor = "Los meses deben ser mayor a cero";
            ib_conMeses.setValue(null);
            foco = "meses";
        } else if (d_conFecIni.getValue() == null) {
            s_valor = "Por favor ingresar la fecha inicio";
            foco = "fecini";
        } else if (d_conFecFin.getValue() == null) {
            s_valor = "Por favor ingresar la fecha fin";
            foco = "fecfin";
        } else if (ib_conRem.getValue() == null) {
            s_valor = "Por favor ingresar la remuneración";
            foco = "remu";
        } else if (ib_conRem.getValue() <= 0) {
            s_valor = "La remuneración debe ser mayor a cero";
            ib_conRem.setValue(null);
            foco = "remu";
        } else if (txt_idtipcont.getValue().isEmpty()) {
            s_valor = "Por favor ingresar el TIP.CONT";
            foco = "idtip";
        } else if (txt_conAct.getValue().isEmpty()) {
            s_valor = "Por favor ingresar las actividades";
            foco = "actvi";
        }
        return s_valor;
    }

@Listen("onOK=#d_conFecIni")
    public void calculafecha() {

        if (ib_conMeses.getValue() == null) {
            Messagebox.show("Ingresar los meses", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            d_conFecFin.setValue(null);
			
        } else {
            Date date_final = new Date();
            long un_dia = (long) 1000 * 60 * 60 * 24;

            int mes = ib_conMeses.getValue();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d_conFecIni.getValue());
            calendar.add(Calendar.MONTH, mes);

            long fecha_final_cal = calendar.getTime().getTime() - (un_dia);
            date_final.setTime(fecha_final_cal);

            d_conFecFin.setValue(date_final);

        }
    }

    @Listen("onBlur=#ib_conMeses")
    public void Mes() {
        calculafecha();
    }

    @Listen("onBlur=#d_conFecIni")
    public void FecIni() {
        calculafecha();
    }

    @Listen("onOK=#ib_conMeses")
    public void validaMes() {

        if (ib_conMeses.getValue() == null) {
            Messagebox.show("Ingresar los meses", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            d_conFecFin.setValue(null);
        } else if (ib_conMeses.getValue() <= 0) {
            Messagebox.show("Los meses deben ser mayores a cero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            ib_conMeses.setValue(null);
            d_conFecFin.setValue(null);
        } else {
            d_conFecIni.focus();
        }

    }

    @Listen("onOK=#ib_conRem")
    public void validaRemuneracion() {

        if (ib_conRem.getValue() == null) {
            Messagebox.show("Ingresar remuneración", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (ib_conRem.getValue() <= 0) {
            Messagebox.show("La remuneración debe ser mayor a cero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            ib_conRem.setValue(null);
        } else {
            txt_idtipcont.focus();
        }

    }

    @Listen("onOK=#txt_distrito")
    public void nextdistrito() {
        txt_perNroDoc.focus();

    }

    @Listen("onOK=#txt_idtipcont")
    public void nextTipCon() {
        txt_conAct.focus();

    }

    @Listen("onOK=#d_conFecIni")
    public void nextFecIni() {
        ib_conRem.focus();

    }

    //LOV PERSONAL MANT
    @Listen("onOK=#txt_perNroDoc")
    public void busquedaPersonalMan() {
        if (bandera == false) {
            bandera = true;
            if (txt_perNroDoc.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_perNroDoc);
                objMapObjetos.put("des_per", txt_perNom);
                objMapObjetos.put("controlador", "ControllerContratos");
                //Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovPersonal.zul", null, objMapObjetos);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }

        }
        ib_conMeses.focus();

    }

    @Listen("onBlur=#txt_perNroDoc")
    public void valida_PersonalMan() throws SQLException {
        if (!txt_perNroDoc.getValue().isEmpty()) {
            String id = txt_perNroDoc.getValue();
            objPersonal = objDaoContrato.getLovPersonal(id);
            if (objPersonal == null) {
                Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_perNroDoc.setValue(null);
                        txt_perNroDoc.setValue(null);
                        txt_perNroDoc.focus();
                        txt_perNom.setValue("");
                        txt_perDir.setValue("");
                        txt_perCargo.setValue("");
                    }
                });

            } else {
                txt_perNroDoc.setValue(objPersonal.getPlidper());
                txt_perNom.setValue(objPersonal.getPldesper());
                txt_perDir.setValue(objPersonal.getDireccion());
                txt_perCargo.setValue(objPersonal.getCargo());

            }

        } else {
            txt_perNom.setValue("");

        }
        bandera = false;
    }

    //LOV TIPO CONTRATO
    @Listen("onOK=#txt_idtipcont")
    public void busquedaTipCont() {
        if (bandera == false) {
            bandera = true;
            if (txt_idtipcont.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_idtipcont);
                objMapObjetos.put("TABLA_DESCRI", txt_destipcont);
                objMapObjetos.put("controlador", "ControllerPersonal");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTipContrato.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_idtipcont")
    public void valida_TipCont() throws SQLException {
        if (!txt_idtipcont.getValue().isEmpty()) {
            String consulta = txt_idtipcont.getValue();
            objTipContrato = objDaoPersonal.getLovTipCont(consulta);
            if (objTipContrato == null) {
                Messagebox.show("El código del Tipo Contrato es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_idtipcont.setValue(null);
                        txt_destipcont.setValue("");
                        txt_idtipcont.focus();
                    }
                });
            } else {
                txt_idtipcont.setValue(objTipContrato.getTipcont_id());
                txt_destipcont.setValue(objTipContrato.getTipcont_des());
            }
        } else {
            txt_idtipcont.setValue("");
        }
        bandera = false;
    }

    @Listen("onCtrlKey=#tabbox_contratos")
    public void ctrl_teclado(Event event) throws SQLException, ParseException, IOException, XDocReportException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo();
                }
                break;
            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    botonEliminar();
                }
                break;
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;
        }
    }
	
	public boolean validaFechaVacacionesModificar(ListModelList<PlantillaContrato> objListVacaValidar) {
        boolean b_validar = true;
        for (int i = 0; i < objListVacaValidar.size(); i++) {
            if (txt_perNroDoc.getValue().substring(1).equals(objListVacaValidar.get(i).getPar07_pernumdoc())  && objListVacaValidar.get(i).isValor()) {
                if (validarRangoFecha(d_conFecIni.getValue(), objListVacaValidar.get(i).getPar14_confecini(), objListVacaValidar.get(i).getPar15_confecfin())) {
                    b_validar = false;
                    break;
                } else if (validarRangoFecha(d_conFecFin.getValue(), objListVacaValidar.get(i).getPar14_confecini(), objListVacaValidar.get(i).getPar15_confecfin())) {
                    b_validar = false;
                    break;
                } else {
                    b_validar = true;
                }
            }
        }

        return b_validar;
    }

    boolean validarRangoFecha(Date d_validar, Date d_fecha1, Date fecha2) {
        return !(d_validar.before(d_fecha1) || d_validar.after(fecha2));
    }
}
