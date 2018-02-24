/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.impresion.planillas.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.log4j.Logger;
import org.me.gj.controller.impresion.planillas.mantenimiento.ControllerImpresionBancos;
import org.me.gj.controller.planillas.mantenimiento.DaoBancos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.util.media.AMedia;
import org.zkoss.xel.fn.StringFns;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerImpresionDMedico extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_impresion_dmedico;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;
    String txt_periodo, txt_codigo1;
    //Instancias de Objetos
    //Variables publicas
    String tipo, filtro, periodo = "";
    int emp_id;
    int i = 0;
    String newcadenaLetra;
    String separador = System.getProperty("file.separator");
	
	String s_tipodescanso="";
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionBancos.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_codigo1 = (String) parametros.get("ccodemp");
        txt_periodo = (String) parametros.get("periodo");
		s_tipodescanso = (String) parametros.get("tipodescanso");
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
        String cadenaLetra = "";
        rbg_impresion.getItems().get(1).setChecked(true);
        if (i >= 1) {
            newcadenaLetra = StringFns.substring(cadenaLetra, 0, cadenaLetra.length() - 1);
        }
        seleccionImpresion();
    }

    @Listen("onCheck=#rbg_impresion")
    public void seleccionImpresion() {
        if (i == 0) {// TODOS
            if (rbg_impresion.getItems().get(0).isChecked()) {
                rbg_tipo.setVisible(true);
                rbg_tipo.setSelectedIndex(0);
                group_tipo.setVisible(true);
                cb_impresoras.setDisabled(true);
                cb_impresoras.setSelectedIndex(-1);
                cb_formato.setVisible(false);
                group_impresora.setVisible(false);
                cb_formato.setValue("");
                group_reporte.setVisible(true);
                rbg_reporte.setSelectedIndex(1);
                rbg_reporte.getItems().get(0).setVisible(false);
            } else if (rbg_impresion.getItems().get(1).isChecked()) {
                group_tipo.setVisible(false);
                cb_impresoras.setDisabled(false);
                group_impresora.setVisible(true);
                cb_formato.setValue("");
                group_reporte.setVisible(false);
            } else if (rbg_impresion.getItems().get(2).isChecked()) {
                rbg_tipo.setVisible(false);
                group_tipo.setVisible(true);
                cb_formato.setDisabled(false);
                cb_formato.setVisible(true);
                cb_impresoras.setDisabled(true);
                cb_impresoras.setSelectedIndex(-1);
                group_impresora.setVisible(false);
                group_reporte.setVisible(true);
                rbg_tipo.setSelectedIndex(0);
                rbg_reporte.setSelectedIndex(1);
                rbg_reporte.getItems().get(0).setVisible(false);
            }
        } else { // Algunos
            if (rbg_impresion.getItems().get(0).isChecked()) {
                rbg_tipo.setVisible(true);
                rbg_tipo.setSelectedIndex(0);
                rbg_tipo.getItems().get(1).setVisible(false);
                group_tipo.setVisible(true);
                cb_formato.setVisible(false);
                cb_formato.setValue("");
                group_impresora.setVisible(false);
                group_reporte.setVisible(true);
                rbg_reporte.setSelectedIndex(0);
                rbg_reporte.getItems().get(1).setVisible(false);
            } else if (rbg_impresion.getItems().get(1).isChecked()) {
                group_tipo.setVisible(false);
                cb_impresoras.setDisabled(false);
                group_impresora.setVisible(true);
                cb_formato.setValue("");
                group_reporte.setVisible(false);
            } else if (rbg_impresion.getItems().get(2).isChecked()) {
                rbg_tipo.setVisible(false);
                group_tipo.setVisible(true);
                cb_formato.setDisabled(false);
                cb_formato.setVisible(true);
                cb_impresoras.setDisabled(true);
                cb_impresoras.setSelectedIndex(-1);
                group_impresora.setVisible(false);
                group_reporte.setVisible(true);
                rbg_tipo.setSelectedIndex(0);
                rbg_reporte.getItems().get(1).setVisible(false);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {
        int x = 100;
        int y = 0;
        int aleatorio = (int) (Math.random() * x) + y;
        String nom_reporte;

        Map<String, Object> objHashMap = new HashMap<String, Object>();
        Connection conexion = new ConectaBD().conectar();
        JasperPrint jasperPrint;
        InputStream is = null;
        AMedia amedia;
        File file;

        try {
            if (rbg_impresion.getItems().get(0).isChecked()) {// => SI ELIGIO PANTALLA <=
                Map<String, Object> objHashMap2 = new HashMap<String, Object>();
                objHashMap.put("empresa", objUsuCredential.getCodemp());
                objHashMap.put("periodo", txt_periodo);
                objHashMap.put("ccodemp", txt_codigo1);
				objHashMap.put("ctipodescanso",s_tipodescanso);

                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                if (rbg_tipo.getItems().get(0).isChecked()) {
                    if (rbg_reporte.getItems().get(0).isChecked()) {
                        final Execution exec = Executions.getCurrent();
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMPDF.jasper", false));
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;

                    } else {
                        objHashMap.put("empresa", objUsuCredential.getCodemp());
                        objHashMap.put("periodo", txt_periodo);
                        objHashMap.put("ccodemp", txt_codigo1);
						objHashMap.put("ctipodescanso",s_tipodescanso);
                        objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                        final Execution exec = Executions.getCurrent();
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMPDF.jasper", false));
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                    }
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
                    exporter.exportReport();
                    file = new File("D:\\" + nom_reporte + ".pdf");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    tipo = "pdf";

                } else {//matricial
                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                    objHashMap.put("periodo", txt_periodo);
                    objHashMap.put("ccodemp", txt_codigo1);
					objHashMap.put("ctipodescanso",s_tipodescanso);
                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                    final Execution exec = Executions.getCurrent();
                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMatricial.jasper", false));
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                    reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                    jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                    nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".txt");
                    exporter.exportReport();
                    file = new File("D:\\" + nom_reporte + ".txt");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
                    tipo = "txt";

                }
                objHashMap2.put("amedia", amedia);
                objHashMap2.put("archivo", file.getAbsolutePath().toString());
                objHashMap2.put("tipo", tipo);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
                window.doModal();
            } else if (rbg_impresion.getItems().get(1).isChecked()) {
                PrintService designatedService = null;
                PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                if (printServices.length > 0) {
                    String impresora = cb_impresoras.getValue();
                    for (PrintService printService : printServices) {
                        if (printService.getName().equalsIgnoreCase(impresora)) {
                            designatedService = printService;
                        }

                    }
                    if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) {
                        if (i == 0) {
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("periodo", txt_periodo);
                            objHashMap.put("ccodemp", txt_codigo1);
							objHashMap.put("ctipodescanso",s_tipodescanso);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMPDF.jasper", false));
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                            JRTextExporter exporter = new JRTextExporter();
                            exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                            exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".txt");
                            exporter.exportReport();
                            file = new File("D:\\" + nom_reporte + ".txt");
                            getPrinter(file, designatedService);
                        }
                    } else {
                        Messagebox.show("Debe seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }

                } else {
                    Messagebox.show("No existen impresoras disponibles", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }

            } else if (rbg_impresion.getItems().get(2).isChecked()) {
                if (cb_formato.getValue().trim().equals("")) {
                    Messagebox.show("Ingrese el TIPO de Formato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if (cb_formato.getValue().trim().equals("PDF")) {
                        //individual
                        if (rbg_reporte.getItems().get(0).isChecked()) {
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("periodo", txt_periodo);
                            objHashMap.put("ccodemp", txt_codigo1);
							objHashMap.put("ctipodescanso",s_tipodescanso);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMPDF.jasper", false));
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                        } else {
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("periodo", txt_periodo);
                            objHashMap.put("ccodemp", txt_codigo1);
							objHashMap.put("ctipodescanso",s_tipodescanso);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMPDF.jasper", false));
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                        }
                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
                        exporter.exportReport();
                        file = new File("D:\\" + nom_reporte + ".pdf");
                        Filedownload.save(file, "application/pdf");
                    } else {//si elige formato excel
                        if (rbg_reporte.getItems().get(0).isChecked()) {
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("periodo", txt_periodo);
                            objHashMap.put("ccodemp", txt_codigo1);
							objHashMap.put("ctipodescanso",s_tipodescanso);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMedico.jasper", false));
                            //jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                        } else {
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("periodo", txt_periodo);
                            objHashMap.put("ccodemp", txt_codigo1);
							objHashMap.put("ctipodescanso",s_tipodescanso);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/Report_DescansoMedico.jasper", false));
                            // jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "Report_DescansoMedico-" + aleatorio;
                        }
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                        JExcelApiExporter exporterXLS = new JExcelApiExporter();
                        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".xls");
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                        exporterXLS.exportReport();
                        file = new File("D:\\" + nom_reporte + ".xls");
                        Filedownload.save(file, "application/xlsx");

                    }
                }
            }

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

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_dmedico.detach();
    }

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
////
