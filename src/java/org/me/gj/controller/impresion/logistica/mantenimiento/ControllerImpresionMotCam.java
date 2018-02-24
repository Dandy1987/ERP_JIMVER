package org.me.gj.controller.impresion.logistica.mantenimiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.script.ScriptException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoMotivoCambio;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ControllerImpresionMotCam extends SelectorComposer<Component> implements Serializable {

    //Componentes Web
    @Wire
    Window w_lov_impresion_motcam;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras;
    //Instancias de Objetos
    //Variables publicas
    String tipo, emp_id, fecha, filtro, periodo = "";
    String titulo = "";
    String empresa, usuario;
    String separador = System.getProperty("file.separator");
    String ruta = "";
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionMotCam.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
    }

    @Listen("onCheck=#rbg_impresion")
    public void seleccionImpresion() {
        if (rbg_impresion.getItems().get(0).isChecked()) {
            rbg_tipo.setVisible(true);
            cb_impresoras.setDisabled(true);
            cb_impresoras.setSelectedIndex(-1);
        } else if (rbg_impresion.getItems().get(1).isChecked()) {
            rbg_tipo.setVisible(false);
            cb_impresoras.setDisabled(false);
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {
        //SI ELIGIO PANTALLA
        if (rbg_impresion.getItems().get(0).isChecked()) {
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            JasperPrint jasperPrint;
            try {
                Map objParam = new HashMap();
                Map objMapObjetos = new HashMap();
                AMedia amedia;
                objParam.put("empresa", empresa);
                objParam.put("usuario", usuario);
                objParam.put("P_WHERE", DaoMotivoCambio.P_WHERE);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                //si eligio grafico (pdf)
                if (rbg_tipo.getItems().get(0).isChecked()) {
                    final Execution exec = Executions.getCurrent();
                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/mantenimiento/ManMotCam.jasper", false));
                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManMotCam.jasper");
                    int x = 100;
                    int y = 0;
                    int aleatorio = (int) (Math.random() * x) + y;
                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "MOTCAM" + aleatorio;
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        ruta = "D:\\" + nom_reporte + ".pdf";
                    } else {
                        ruta = separador + "home" + separador + nom_reporte + ".pdf";
                    }
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
                    exporter.exportReport();
                    File file;
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                        file = new File("D:\\" + nom_reporte + ".pdf");
                    } else {
                        // everything else
                        file = new File(separador + "home" + separador + nom_reporte + ".pdf");
                    }
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    tipo = "pdf";
                    objMapObjetos.put("amedia", amedia);
                    objMapObjetos.put("archivo", file.getAbsolutePath().toString());
                    objMapObjetos.put("tipo", tipo);
                    //si eligio matricial (txt)
                } else {
                    final Execution exec = Executions.getCurrent();
                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/mantenimiento/ManMotCam_M.jasper", false));
                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManMotCam_M.jasper");
                    int x = 100;
                    int y = 0;
                    int aleatorio = (int) (Math.random() * x) + y;
                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "MOTCAM" + aleatorio;
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        ruta = "D:\\" + nom_reporte + ".txt";
                    } else {
                        ruta = separador + "home" + separador + nom_reporte + ".txt";
                    }
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
                    exporter.exportReport();
                    File file;
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                        file = new File("D:\\" + nom_reporte + ".txt");
                    } else {
                        // everything else
                        file = new File(separador + "home" + separador + nom_reporte + ".txt");
                    }
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
                    tipo = "txt";
                    objMapObjetos.put("amedia", amedia);
                    objMapObjetos.put("archivo", file.getAbsolutePath().toString());
                    objMapObjetos.put("tipo", tipo);
                }
                Window window = (Window) Executions.createComponents("/org/me/gj/view/logistica/mantenimiento/Reporte.zul", null, objMapObjetos);
                window.doModal();
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
        } //SI ELIGIO IMPRESORA
        else if (rbg_impresion.getItems().get(1).isChecked()) {
            PrintService designatedService = null;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length > 0)//si existen impresoras
            {
                String impresora = cb_impresoras.getValue();
                for (int i = 0; i < printServices.length; i++) {
                    if (printServices[i].getName().equalsIgnoreCase(impresora)) {
                        designatedService = printServices[i];
                    }
                }
                if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) //Si se selecciono una impresora
                {
                    JasperPrint jasperPrint;
                    Connection conexion = new ConectaBD().conectar();
                    InputStream is = null;
                    try {
                        int x = 100;
                        int y = 0;
                        int aleatorio = (int) (Math.random() * x) + y;
                        final Execution exec = Executions.getCurrent();
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/mantenimiento/ManMotCam_M.jasper", false));
                        //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManMotCam_M.jasper");
                        Map objParam = new HashMap();
                        objParam.put("empresa", empresa);
                        objParam.put("usuario", usuario);
                        objParam.put("P_WHERE", DaoMotivoCambio.P_WHERE);
                        objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                        jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                        String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "MOTCAM" + aleatorio;
                        if (System.getProperty("os.name").startsWith("Windows")) {
                            ruta = "D:\\" + nom_reporte + ".txt";
                        } else {
                            ruta = separador + "home" + separador + nom_reporte + ".txt";
                        }
                        JRTextExporter exporter = new JRTextExporter();
                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);
                        exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                        exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
                        exporter.exportReport();
                        File file;
                        if (System.getProperty("os.name").startsWith("Windows")) {
                            // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                            file = new File("D:\\" + nom_reporte + ".txt");
                        } else {
                            // everything else
                            file = new File(separador + "home" + separador + nom_reporte + ".txt");
                        }
                        getPrinter(file, designatedService);
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
            } else {
                Messagebox.show("Debe Seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_motcam.detach();
    }

    //Eventos Secundarios - Validacion
    //Eventos Otros 
    public void getPrinter(File file, PrintService sp) throws PrintException, FileNotFoundException {
        javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;
        javax.print.attribute.PrintRequestAttributeSet pras = new javax.print.attribute.HashPrintRequestAttributeSet();
        javax.print.DocPrintJob job = sp.createPrintJob();
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();
        javax.print.Doc doc = new javax.print.SimpleDoc(fis, flavor, das);
        job.print(doc, pras);
    }
}
