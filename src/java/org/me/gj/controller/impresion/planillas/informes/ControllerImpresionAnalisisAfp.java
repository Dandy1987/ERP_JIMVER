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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
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
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.InformesPrestamos;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ControllerImpresionAnalisisAfp extends SelectorComposer<Component> {

    @Wire
    Window w_lov_impresion_analisisafp;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;

    int emp_id;
    int i = 0;
    String codigo, periodo, s_tipo;
    int id_empresa, id_sucursal, dia, i_tipo;

    String ruta = "", titulo = "", newcadenaLetra, empresa;
    //ListModelList<InformesMovimiento> objlstMovimiento;
    String separador = System.getProperty("file.separator");
    //RUTAS
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileRep = rutaFile + "REPORTES\\";
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    SimpleDateFormat sdfcont = new SimpleDateFormat("dd-MM-yyyy");

    double total, totalpagado, totaldeuda;
    Date d_fechadeposito;
    JasperPrint jasperPrint = null;
    InputStream is = null;
    AMedia amedia = null;
    File file = null;
    String nom_reporte, nrodoc, estadocuota, estadotrabajador, estadoprestamo, formato, afp;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        id_empresa = (Integer) parametros.get("nempid");
        id_sucursal = (Integer) parametros.get("nsucuid");
        periodo = (String) parametros.get("cperiodo");
        codigo = (String) parametros.get("ccodemp");

        afp = (String) parametros.get("cafp");
        if (afp.isEmpty()) {
            afp = "TODOS";
        }
        int x = 100;
        int y = 0;
        String codigo_aux, afp_Aux;
        int aleatorio = (int) (Math.random() * x) + y;
        codigo_aux = codigo.replace("'", "");
        codigo_aux = codigo_aux.replace(",", "");
        afp_Aux = afp.replace("'", "");
        afp_Aux = afp_Aux.replace(",", "");
        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "analisisAFP-" + "-" + aleatorio;
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int p = 0; p < ps.length; p++) {
            cb_impresoras.getItems().add(new Comboitem(ps[p].getName()));
        }
        seleccionImpresion();

    }

    public void archivos() throws FileNotFoundException, SQLException {
        InputStream inputStream = null;
        JasperPrint jasperPrint = null;
        Map objParam = new HashMap();
        final Execution exec = Executions.getCurrent();
        Connection conexion = new ConectaBD().conectar();
        try {

            //JasperViewer.viewReport(jasperPrint, false);
            if (cb_formato.getValue().trim().equals("")) {
                Messagebox.show("Ingrese el TIPO de Formato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (cb_formato.getValue().trim().equals("PDF")) {//-----PDF

                    objParam.put("nempid", id_empresa);
                    objParam.put("nsucuid", id_sucursal);
                    objParam.put("cperiodo", periodo);
                    objParam.put("ccodemp", codigo);
                    objParam.put("cafp", afp);
                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));

                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InformeAnalisisAfp.jasper", false));
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                    reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                    jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);

                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".pdf");

                    exporter.exportReport();

                    file = new File(rutaFileRep + nom_reporte + ".pdf");
                    Filedownload.save(file, "application/pdf");
                } else {//-----EXCEL

                    objParam.put("nempid", id_empresa);
                    objParam.put("nsucuid", id_sucursal);
                    objParam.put("cperiodo", periodo);
                    objParam.put("ccodemp", codigo);
                    objParam.put("cafp", afp);
                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));

                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InformeAnalisisAfp.jasper", false));
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                    reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                    // JasperReport reporte = (JasperReport) JRLoader.loadObject(inputStream);

                    JExcelApiExporter exporterXLS = new JExcelApiExporter();
                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".xls");
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                    exporterXLS.exportReport();
                    file = new File(rutaFileRep + nom_reporte + ".xls");
                    Filedownload.save(file, "application/xlsx");
                }
            }

            // JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\cpure\\Desktop\\VersionesERP\\jimver_08012018\\ERP_JIMVER_V2\\web\\WEB-INF\\reportes\\planillas\\informes\\InfPrestamosPrueba.pdf");
        } catch (JRException e) {
            String err = "Error al leer el fichero de carga jasper report " + e.toString();
            Messagebox.show("No se pudo generar por: " + err, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() {
        try {
            if (rbg_impresion.getItems().get(0).isChecked()) {
                if (rbg_tipo.getItems().get(0).isChecked()) {
                    reportePdf();
                } else {
                    //reporteMatricial();
                }
            } else if (rbg_impresion.getItems().get(1).isChecked()) {//--------------------------------------------------IMPRESION_IMPRESORA
                // imprimirPDF();
            } else if (rbg_impresion.getItems().get(2).isChecked()) {//--------------------------------------------------IMPRESION_ARCHIVO

                // archivos();
            }

        } catch (Exception ex) {
            String error = ex.toString();
        }
    }

    public void reportePdf() throws SQLException, IOException, PrintException {
        Connection conexion = new ConectaBD().conectar();
        InputStream is = null;
        JasperPrint jasperPrint = null;
        AMedia amedia;

        File file;
        Map objParam = new HashMap();
        Map<String, Object> objHashMap2 = new HashMap<String, Object>();

        objParam.put("nempid", id_empresa);
        objParam.put("nsucuid", id_sucursal);
        objParam.put("cperiodo", periodo);
        objParam.put("ccodemp", codigo);
        objParam.put("cafp", afp);

        objParam.put("REPORT_LOCALE", new Locale("en", "US"));
        final Execution exec = Executions.getCurrent();
        try {
            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InformeAnalisisAfp.jasper", false));
            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".pdf");
            exporter.exportReport();
            file = new File(rutaFileRep + nom_reporte + ".pdf");
            FileInputStream fis = new FileInputStream(file);
            amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
            s_tipo = "pdf";

            objHashMap2.put("amedia", amedia);
            objHashMap2.put("archivo", file.getAbsolutePath().toString());
            objHashMap2.put("tipo", s_tipo);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
            window.doModal();

            // JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\cpure\\Desktop\\VersionesERP\\jimver_08012018\\ERP_JIMVER_V2\\web\\WEB-INF\\reportes\\planillas\\informes\\InfPrestamosPrueba.pdf");
        } catch (JRException e) {
            String err = "Error al leer el fichero de carga jasper report " + e.toString();
            Messagebox.show("No se pudo generar por: " + err, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void imprimirPDF() throws FileNotFoundException, SQLException, JRException, PrintException {

        PrintService designatedService = null;
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        Connection conexion = new ConectaBD().conectar();
        InputStream is = null;
        JasperPrint jasperPrint = null;
        AMedia amedia;

        File file;
        Map objParam = new HashMap();
        Map<String, Object> objHashMap2 = new HashMap<String, Object>();

        if (printServices.length > 0) {
            String impresora = cb_impresoras.getValue();
            for (PrintService printService : printServices) {
                if (printService.getName().equalsIgnoreCase(impresora)) {
                    designatedService = printService;
                }
            }
            if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) {
                if (i == 0) {

                    objParam.put("nempid", id_empresa);
                    objParam.put("nsucuid", id_sucursal);
                    objParam.put("cperiodo", periodo);
                    objParam.put("ccodemp", codigo);
                    objParam.put("cafp", afp);

                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                    final Execution exec = Executions.getCurrent();
                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InformeAnalisisAfp.jasper", false));
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                    reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                    jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".txt");
                    exporter.exportReport();
                    file = new File(rutaFileRep + nom_reporte + ".txt");
                    getPrinter(file, designatedService);
                }

            } else {
                Messagebox.show("Debe seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            Messagebox.show("No existen impresoras disponibles", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

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
        }
    }

    public void getPrinter(File file, PrintService ps) throws PrintException, FileNotFoundException {
        javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;
        javax.print.attribute.PrintRequestAttributeSet pras = new javax.print.attribute.HashPrintRequestAttributeSet();
        javax.print.DocPrintJob job = ps.createPrintJob();
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();
        javax.print.Doc doc = new javax.print.SimpleDoc(fis, flavor, das);
        job.print(doc, pras);
    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_analisisafp.detach();
    }

}
