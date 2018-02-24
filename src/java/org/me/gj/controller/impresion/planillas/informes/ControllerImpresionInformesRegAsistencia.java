package org.me.gj.controller.impresion.planillas.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.me.gj.controller.planillas.informes.ControllerRegistroAsis;
import org.me.gj.controller.planillas.procesos.DaoAsistenciaGen;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ControllerImpresionInformesRegAsistencia extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_impresion_asistencia;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;

    //Variables publicas
    String tipo, fecha, filtro, periodo = "";
    int emp_id, sucursal;
    int i = 0;
    String codigo, periodo_, controlador, area, idper;
    int id_empresa, id_sucursal, dia;
	Date d_fecini, d_fecfin;

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

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("formato");
        sucursal = (Integer) parametros.get("n_sucid");
		d_fecini = (Date) parametros.get("d_fecini");
        d_fecfin = (Date) parametros.get("d_fecfin");

        area = (String) parametros.get("c_area");
        idper = (String) parametros.get("c_idper");
		
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int p = 0; p < ps.length; p++) {
            cb_impresoras.getItems().add(new Comboitem(ps[p].getName()));
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

@Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException, IOException, PrintException {
        int x = 100;
        int y = 0;
        int aleatorio = (int) (Math.random() * x) + y;
        String nom_reporte = null;

        Map<String, Object> objHashMap = new HashMap<String, Object>();
        Connection conexion = new ConectaBD().conectar();
        JasperPrint jasperPrint = null;
        InputStream is = null;
        AMedia amedia = null;
        File file = null;

        try {
            if (rbg_impresion.getItems().get(0).isChecked()) {//--------------------------------------------------IMPRESION_PANTALLA

                Map<String, Object> objHashMap2 = new HashMap<String, Object>();
                objHashMap.put("n_empid", objUsuCredential.getCodemp());
                objHashMap.put("n_sucid", sucursal);
                objHashMap.put("c_area", area);
                objHashMap.put("c_idper", idper);
				objHashMap.put("d_fecini", d_fecini);
                objHashMap.put("d_fecfin", d_fecfin);
                objHashMap.put("usuario", objUsuCredential.getCuenta());
                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));

                if (rbg_tipo.getItems().get(0).isChecked()) { //----------------------------------------TIPO_GRAFICO
                    if (rbg_reporte.getItems().get(0).isChecked()) {//------------------------------REPORTE_DETALLADO

                    } else {//------------------------------REPORTE_RESUMEN
                        objHashMap.put("n_empid", objUsuCredential.getCodemp());
                        objHashMap.put("n_sucid", sucursal);
                        objHashMap.put("c_area", area);
                        objHashMap.put("c_idper", idper);
						objHashMap.put("d_fecini", d_fecini);
                        objHashMap.put("d_fecfin", d_fecfin);
						
                        objHashMap.put("usuario", objUsuCredential.getCuenta());
                        objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                        final Execution exec = Executions.getCurrent();
                        if (controlador.equals("antiguo")) {
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_ForAnt.jasper", false));
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistenciaForAnt-" + aleatorio;
                        } else {
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia.jasper", false));
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistencia-" + aleatorio;
                        }
                        //agregado para formato plsql
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                    }

                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".pdf");
                    exporter.exportReport();
                    file = new File(rutaFileRep + nom_reporte + ".pdf");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    tipo = "pdf";

                } else {//----------------------------------------TIPO_MATRICIAL
                    objHashMap.put("n_empid", objUsuCredential.getCodemp());
                    objHashMap.put("n_sucid", sucursal);
                    objHashMap.put("c_area", area);
                    objHashMap.put("c_idper", idper);
                    objHashMap.put("d_fecini", d_fecini);
                    objHashMap.put("d_fecfin", d_fecfin);
					
                    objHashMap.put("usuario", objUsuCredential.getCuenta());
                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                    final Execution exec = Executions.getCurrent();
                    if (controlador.equals("antiguo")) {
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_ForAnt_M.jasper", false));
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistenciaForAnt_M-" + aleatorio;
                    } else {
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_M.jasper", false));
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistencia_M-" + aleatorio;
                    }
                    jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                    //FORMATO
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".txt");
                    exporter.exportReport();
                    file = new File(rutaFileRep + nom_reporte + ".txt");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
                    tipo = "txt";
                }

                objHashMap2.put("amedia", amedia);
                objHashMap2.put("archivo", file.getAbsolutePath().toString());
                objHashMap2.put("tipo", tipo);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
                window.doModal();

            } else if (rbg_impresion.getItems().get(1).isChecked()) {//--------------------------------------------------IMPRESION_IMPRESORA
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
                            objHashMap.put("n_empid", objUsuCredential.getCodemp());
                            objHashMap.put("n_sucid", sucursal);
                            objHashMap.put("c_area", area);
                            objHashMap.put("c_idper", idper);
                            objHashMap.put("d_fecini", d_fecini);
                            objHashMap.put("d_fecfin", d_fecfin);
                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            if (controlador.equals("antiguo")) {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_ForAnt_M.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistenciaForAnt_M-" + aleatorio;
                            } else {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_M.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistencia_M-" + aleatorio;
                            }
                            jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
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
            } else if (rbg_impresion.getItems().get(2).isChecked()) {//--------------------------------------------------IMPRESION_ARCHIVO
                if (cb_formato.getValue().trim().equals("")) {
                    Messagebox.show("Ingrese el TIPO de Formato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if (cb_formato.getValue().trim().equals("PDF")) {//-----PDF

                        if (rbg_reporte.getItems().get(0).isChecked()) {//------------------------------REPORTE_DETALLADO

                        } else {//------------------------------REPORTE_RESUMEN
                            objHashMap.put("n_empid", objUsuCredential.getCodemp());
                            objHashMap.put("n_sucid", sucursal);
                            objHashMap.put("c_area", area);
                            objHashMap.put("c_idper", idper);
							objHashMap.put("d_fecini", d_fecini);
                            objHashMap.put("d_fecfin", d_fecfin);
                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            if (controlador.equals("antiguo")) {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_ForAnt.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistenciaForAnt-" + aleatorio;

                            } else {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistencia-" + aleatorio;
                            }
                            jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                        }
                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileRep + nom_reporte + ".pdf");
                        exporter.exportReport();
                        file = new File(rutaFileRep + nom_reporte + ".pdf");
                        Filedownload.save(file, "application/pdf");
                    } else {//-----EXCEL
                        if (rbg_reporte.getItems().get(0).isChecked()) {//------------------------------REPORTE_DETALLADO

                        } else {//------------------------------REPORTE_RESUMEN
                            objHashMap.put("n_empid", objUsuCredential.getCodemp());
                            objHashMap.put("n_sucid", sucursal);
                            objHashMap.put("c_area", area);
                            objHashMap.put("c_idper", idper);
							objHashMap.put("d_fecini", d_fecini);
                            objHashMap.put("d_fecfin", d_fecfin);
                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            if (controlador.equals("antiguo")) {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia_ForAnt.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistenciaForAnt-" + aleatorio;
                            } else {
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/InfRegistroAsistencia.jasper", false));
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesRegistroAsistencia-" + aleatorio;
                            }
                        }
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
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

        int a = rbg_impresion.getSelectedIndex();
        int b = rbg_tipo.getSelectedIndex();
        int c = rbg_reporte.getSelectedIndex();

        switch (a) {
            case 0:
                impPantalla(b, c, "", null);
            case 1:
                impImpresora();
                break;
            case 2:
                impArchivo();
                break;
            default:
                throw new AssertionError();
        }

    }

    public void impPantalla(int tipo, int reporte, String rutaViewZul, Map<String, Object> objHashMap) {
        Map<String, Object> objHashMapJasper = new HashMap<String, Object>();
        if (tipo == 0 && reporte == 0) {//GRAFICO - RESUMEN

        }
        if (tipo == 1 && reporte == 0) {//MATRICIAL - RESUMEN

        }
//        objHashMapJasper.put("amedia", amedia);
//        objHashMapJasper.put("archivo", file.getAbsolutePath().toString());
//        objHashMapJasper.put("tipo", tipo);
//        Window window = (Window) Executions.createComponents(rutaViewZul, null, objHashMapJasper);//rutaViewZul = "/org/me/gj/view/planillas/mantenimiento/Reporte.zul"
//        window.doModal();
    }

    public void impImpresora() {

    }

    public void impArchivo() {

    }

}
