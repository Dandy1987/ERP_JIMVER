package org.me.gj.controller.planillas.informes;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.controller.planillas.mantenimiento.GeneradorDocumentosService;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesCertificadoTrab;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerCertificadoTrab extends SelectorComposer<Component> {

    @Wire
    Textbox txt_codper, txt_desper;

    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
	@Wire
    Radiogroup rg_descripcion;

    int i_sel, emp_id, suc_id;

    //Variables Publicas
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileContratos = rutaFile + "CERTIFICADOTRAB\\";
    String rutaBatch = rutaFile + "TEMPLATES\\generaPDF.bat";

    Personal objPersonal;
    //DaoMovLinea objDaoMovLinea;

    InformesCertificadoTrab objPlaCerTra;
    DaoCertificadoTrab objDaoCerTra = new DaoCertificadoTrab();
    ListModelList<InformesCertificadoTrab> objlstPlaCerTra;

    Process process;
	
	Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables de Fecha
    DateFormat fhora = new SimpleDateFormat("HHmmss");
    DateFormat ffecha = new SimpleDateFormat("ddMMyyyy");
    String impresion = "";

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCliente.class);

    public static boolean bandera = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoCerTra = new DaoCertificadoTrab();
		rg_descripcion.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90308000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Certificado de trabajo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Certificado de trabajo con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Certificado de trabajo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Certificado de trabajo");
        }

    }
	
    @Listen("onCreate=#tabbox_infregasi")
    public void llenaCredenciales() throws SQLException {
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
    }

    //LOV PARA PERSONAL
    @Listen("onOK=#txt_codper") //ENTER DONDE CARGA EL LOV
    public void buscarPersonalPrincipal() {
        if (bandera == false) {
            bandera = true;
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                objMapObjetos.put("controlador", "ControllerCertificadoTrab");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws FileNotFoundException, IOException, XDocReportException, SQLException, ParseException {
        if (txt_codper.getValue().equals("")) {
            Messagebox.show("Por favor ingresar código de Personal con fecha de cese.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (!txt_codper.getValue().isEmpty()) {
            String id = txt_codper.getValue();
            objPersonal = objDaoCerTra.getLovPersonal(id);
            if (objPersonal == null) {
                Messagebox.show("El código debe ser de Personal con fecha de cese.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codper.setValue(null);
                        txt_codper.setValue("");
                        txt_desper.setValue(null);
                        txt_desper.setValue("");
                        txt_codper.focus();
                    }
                });
            } else {                
				if (rg_descripcion.getSelectedIndex() == 0) {
                    documentGeneratorNormal();
                }else{
                    documentGeneratorBueno();
                }
                String nombre_archivo_pdf = rutaFileContratos + "CertificadoTrab_" + impresion;
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
                    Messagebox.show("La ruta del archivo no se encontró.", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Listen("onBlur=#txt_codper")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codper.getValue().isEmpty()) {
            String id = txt_codper.getValue();
            objPersonal = objDaoCerTra.getLovPersonal(id);
            if (objPersonal == null) {
                txt_codper.setValue(null);
                txt_codper.setValue("");
                txt_desper.setValue(null);
                txt_desper.setValue("");
                txt_codper.focus();
            } else {
                txt_codper.setValue(objPersonal.getPlidper());
                txt_desper.setValue(objPersonal.getPldesper());
            }
        } else {
            txt_desper.setValue("");
        }
        bandera = false;
    }

    /* *************************************************************************************************************************** */
    public void documentGeneratorNormal() throws IOException, XDocReportException, SQLException, ParseException {

        String rutaPlantilla = rutaFile + "TEMPLATES\\Certificadotrab.odt";
        String extension = "odt";

        generator(rutaPlantilla, extension, false);
    }
	
        public void documentGeneratorBueno() throws IOException, XDocReportException, SQLException, ParseException {

        String rutaPlantilla = rutaFile + "TEMPLATES\\CertificadotrabMejor.odt";
        String extension = "odt";

        generator(rutaPlantilla, extension, false);
    }

    private void generator(String rutaPlantilla, String extension, boolean convertirPdf) throws IOException, XDocReportException, SQLException, ParseException {

        //Objeto PLantilla CErtificado Trab
        objPlaCerTra = new InformesCertificadoTrab();
        objPlaCerTra = objDaoCerTra.listaCertificadoTrab(txt_codper.getValue());

        // Mapa con las variables a reemplazar
        Map<String, String> variablesMap = new HashMap<String, String>();
        variablesMap.put("empNombre", objPlaCerTra.getEmpnom().toString());
        variablesMap.put("empRuc", objPlaCerTra.getEmpruc().toString());
        variablesMap.put("empSuc", toMinuscula(objPlaCerTra.getEmpsuc()));
        variablesMap.put("fecAct", objPlaCerTra.getFeccert().trim());
        variablesMap.put("perNombre", objPlaCerTra.getEmpleado().toString());
        variablesMap.put("tipDoc", objPlaCerTra.getTipdoc().toString());
        variablesMap.put("perDoc", objPlaCerTra.getNrodoc().toString());
        variablesMap.put("conFecIni", objPlaCerTra.getFecini());
        variablesMap.put("conFecFin", objPlaCerTra.getFecfin());
        variablesMap.put("perCargo", objPlaCerTra.getPercargo().toString());

        FieldsMetadata metadata = new FieldsMetadata();

        // Mapa con las variables de tipo imagen. Estas variables contienen el path de la imagen
        Map<String, String> imagenesMap = new HashMap<String, String>();
        imagenesMap.put("header_image_logo", "./Logo.png");

        GeneradorDocumentosService generadorDocumentosService = new GeneradorDocumentosService();
        byte[] mergedOutput = generadorDocumentosService.generarDocumento(rutaPlantilla, TemplateEngineKind.Freemarker, variablesMap, imagenesMap, convertirPdf, metadata);

        // Se comprueba que se ha generado el documento
        String fec = ffecha.format(new Date()).toString();
        String hor = fhora.format(new Date()).toString();
        impresion = fec + "_" + hor + "_" + txt_codper.getValue().toString() + ".pdf";

        String nombre_archivo_odt = "CertificadoTrab_" + fec + "_" + hor + "_" + txt_codper.getValue() + ".";

        // Se escribe el documento de salida
        FileOutputStream os = new FileOutputStream(rutaFileContratos + nombre_archivo_odt + extension);
        os.write(mergedOutput);
        os.flush();
        os.close();

        //Generamos el archivo PDF
        try {
            //String ruta_defecto_bat = rutaB
            Runtime runtime = Runtime.getRuntime();
            String sentencia_batch = "cmd start /c " + rutaBatch + " " + rutaFileContratos + nombre_archivo_odt + extension + " " + rutaFileContratos + nombre_archivo_odt + "pdf";
            process = runtime.exec(sentencia_batch);
            int x = process.waitFor();
        } catch (IOException ex) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + ex, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (InterruptedException ex) {
            Messagebox.show("Ocurrio un error cuando se generaba el archivo PDF " + ex, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
        objPlaCerTra = null;
    }

    public String toMinuscula(String toMinus) {
        String cadena = toMinus.toLowerCase();
        char[] caracter = cadena.toCharArray();
        caracter[0] = Character.toUpperCase(caracter[0]);
        return new String(caracter);
    }

}
