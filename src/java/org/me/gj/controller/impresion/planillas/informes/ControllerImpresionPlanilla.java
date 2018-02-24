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
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.apache.log4j.Logger;
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

public class ControllerImpresionPlanilla extends SelectorComposer<Component> {

    @Wire
    Window w_lov_impresion_boleta;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;
    int emp_id, i = 0, flag, tipo_data, norden;
    String anio, diauno, diados, mes_report, sucursal, personal, per, tabla, costo,
            tipo, fecha, filtro, periodo = "";
    Boolean chk;
    //RUTAS
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";
    String area, tab;
    Map parametros;
    URL imagen;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionInformesMovimiento.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        parametros = new HashMap(Executions.getCurrent().getArg());
        anio = (String) parametros.get("anio");
        diauno = (String) parametros.get("diauno");
        diados = (String) parametros.get("diados");
        mes_report = (String) parametros.get("mes");
        sucursal = (String) parametros.get("sucursal");
        per = (String) parametros.get("per");
        tabla = (String) parametros.get("tabla");
        flag = (Integer) parametros.get("flag");
        chk = (Boolean) parametros.get("chk");
		tab = (String) parametros.get("tab");
        tipo_data = (Integer) parametros.get("tipo");
        //norden = (Integer) parametros.get("norden");
        area = (String) parametros.get("area");
        //if(area.isEmpty()) area="TODOS";
		if (area.isEmpty()) {
            area = "TODOS";
        }
		if (tab.equals("personal")) {
            chk = (Boolean) parametros.get("chk");

            norden = (Integer) parametros.get("norden");
        }
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int p = 0; p < ps.length; p++) {
            cb_impresoras.getItems().add(new Comboitem(ps[p].getName()));
        }
        seleccionImpresion();

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException, IOException, PrintException {
        int x = 100;
        int y = 0;
        int aleatorio = (int) (Math.random() * x) + y;
        Connection conexion = new ConectaBD().conectar();
        InputStream is = null;
        JasperPrint jasperPrint = null;
        AMedia amedia;
        String nom_reporte = null;
        File file;
        Map objParam = new HashMap();
        try {

            if (rbg_impresion.getItems().get(0).isChecked()) {
                Map<String, Object> objHashMap2 = new HashMap<String, Object>();

                objParam.put("MES", mes_report);
                objParam.put("DIAUNO", diauno);
                objParam.put("DIADOS", diados);
                objParam.put("EMPRESA", objUsuCredential.getCodemp());
                objParam.put("SUCURSAL", sucursal);
                objParam.put("PERIODO", per);

                objParam.put("TIPO", tipo_data);
                if (tab.equals("personal")) {
                    objParam.put("ORDEN", norden);
                } else {
                    objParam.put("ORDEN", 0);
                }
                //objParam.put("AREA", area);
                objParam.put("c_area", area);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                if (rbg_tipo.getItems().get(0).isChecked()) {
                    if (rbg_reporte.getItems().get(0).isChecked()) {
                        final Execution exec = Executions.getCurrent();
                        //reporte de tplplames
                        if (tab.equals("personal")) {
                            if (tabla.equals("1")) {
                                if (chk == false) {
                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                } else {
                                    //reporte periodo actual con cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                }
                            } //reporte de tplplanilla
                            else if (tabla.equals("2")) {
                                if (chk == false) {
                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                } else {
                                    //periodo anterior con cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                }
                            }
                        } else { // total
                            if (tabla.equals("1")) {

                                //periodo actual sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                            } //reporte de tplplanilla
                            else if (tabla.equals("2")) {

                                //periodo anterior sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                            }
                        }

                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                        jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                        if (tab.equals("personal")) {
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                        } else {
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;
                        }
                    } else {
                        //aqui va imprimir en pantalla

                        objParam.put("MES", mes_report);
                        objParam.put("DIAUNO", diauno);
                        objParam.put("DIADOS", diados);
                        objParam.put("EMPRESA", objUsuCredential.getCodemp());
                        objParam.put("SUCURSAL", sucursal);
                        objParam.put("PERIODO", per);
                        objParam.put("TIPO", tipo_data);
                        if (tab.equals("personal")) {
                            objParam.put("ORDEN", norden);
                        } else {
                            objParam.put("ORDEN", 0);
                        }
                        //objParam.put("AREA", area);
                        objParam.put("c_area", area);
                        objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                        final Execution exec = Executions.getCurrent();
                        if (tab.equals("personal")) {
                            //reporte de tplplames
                            if (tabla.equals("1")) {
                                if (chk == false) {
                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                } else {
                                    //reporte periodo actual con cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                }
                            } //reporte de tplplanilla
                            else if (tabla.equals("2")) {
                                if (chk == false) {
                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                } else {
                                    //periodo anterior con cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                }
                            }
                        } else {
                            if (tabla.equals("1")) {

                                //periodo actual sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                            } //reporte de tplplanilla
                            else if (tabla.equals("2")) {

                                //periodo anterior sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                            }
                        }
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                        jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);

                    }
                    // jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                    if (tab.equals("personal")) {
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                    } else {
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                    }
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".pdf");
                    exporter.exportReport();
                    file = new File(rutaFileCumple + nom_reporte + ".pdf");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    tipo = "pdf";
                } else {//MATRICIAL --> me falta el report

                    objParam.put("MES", mes_report);
                    objParam.put("DIAUNO", diauno);
                    objParam.put("DIADOS", diados);
                    objParam.put("EMPRESA", objUsuCredential.getCodemp());
                    objParam.put("SUCURSAL", sucursal);
                    objParam.put("PERIODO", per);
                    objParam.put("TIPO", tipo_data);
                    if (tab.equals("personal")) {
                        objParam.put("ORDEN", norden);
                    } else {
                        objParam.put("ORDEN", 0);
                    }
                    //objParam.put("AREA", area);
                    objParam.put("c_area", area);
                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                    final Execution exec = Executions.getCurrent();

                    if (tab.equals("personal")) {

                        if (tabla.equals("1")) {
                            if (chk == false) {
                                //periodo actual sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaActualSC.jasper", false));
                            } else {
                                //reporte periodo actual con cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaActual.jasper", false));
                            }
                        } //reporte de tplplanilla
                        else if (tabla.equals("2")) {
                            if (chk == false) {
                                //periodo anterior sin cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSC.jasper", false));
                            } else {
                                //periodo anterior con cabecera
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanilla.jasper", false));
                            }
                        }
                    } else {
                        if (tabla.equals("1")) {

                            //periodo actual sin cabecera
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaActualSC_TOTAL.jasper", false));

                        } //reporte de tplplanilla
                        else if (tabla.equals("2")) {

                            //periodo anterior sin cabecera
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSC_TOTAL.jasper", false));

                        }
                    }
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                    reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                    JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                    jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                    if (tab.equals("personal")) {
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                    } else {
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                    }

//formato
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);//1060
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);//636
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".txt");
                    exporter.exportReport();
                    file = new File(rutaFileCumple + nom_reporte + ".txt");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
                    tipo = "txt";
                }
                //aqui muestra el pdf
                objHashMap2.put("amedia", amedia);
                objHashMap2.put("archivo", file.getAbsolutePath().toString());
                objHashMap2.put("tipo", tipo);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
                window.doModal();
                //  window.detach();
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
                            objParam.put("MES", mes_report);
                            objParam.put("DIAUNO", diauno);
                            objParam.put("DIADOS", diados);
                            objParam.put("EMPRESA", objUsuCredential.getCodemp());
                            objParam.put("SUCURSAL", sucursal);
                            objParam.put("PERIODO", per);
                            objParam.put("TIPO", tipo_data);
                            if (tab.equals("personal")) {
                                objParam.put("ORDEN", norden);
                            } else {
                                objParam.put("ORDEN", 0);
                            }
                            //objParam.put("AREA", area);
                            objParam.put("c_area", area);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();

                            if (tab.equals("personal")) {

                                if (tabla.equals("1")) {
                                    if (chk == false) {
                                        //periodo actual sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                    } else {
                                        //reporte periodo actual con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                    }
                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {
                                    if (chk == false) {
                                        //periodo anterior sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                    } else {
                                        //periodo anterior con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                    }
                                }
                            } else {
                                if (tabla.equals("1")) {

                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {

                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                                }
                            }
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                            if (tab.equals("personal")) {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                            } else {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                            }
                            JRTextExporter exporter = new JRTextExporter();
                            exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                            exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".txt");
                            exporter.exportReport();
                            file = new File(rutaFileCumple + nom_reporte + ".txt");
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
                        //indic¿vidual
                        if (rbg_reporte.getItems().get(0).isChecked()) {
                            objParam.put("MES", mes_report);
                            objParam.put("DIAUNO", diauno);
                            objParam.put("DIADOS", diados);
                            objParam.put("EMPRESA", objUsuCredential.getCodemp());
                            objParam.put("SUCURSAL", sucursal);
                            objParam.put("PERIODO", per);
                            objParam.put("TIPO", tipo_data);
                            if (tab.equals("personal")) {
                                objParam.put("ORDEN", norden);
                            } else {
                                objParam.put("ORDEN", 0);
                            }
                            //objParam.put("AREA", area);
                            objParam.put("c_area", area);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();

                            if (tab.equals("personal")) {

                                if (tabla.equals("1")) {
                                    if (chk == false) {
                                        //periodo actual sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                    } else {
                                        //reporte periodo actual con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                    }
                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {
                                    if (chk == false) {
                                        //periodo anterior sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                    } else {
                                        //periodo anterior con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                    }
                                }
                            } else {

                                if (tabla.equals("1")) {

                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {

                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                                }
                            }

                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                            if (tab.equals("personal")) {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                            } else {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                            }
                        } else {
                            objParam.put("MES", mes_report);
                            objParam.put("DIAUNO", diauno);
                            objParam.put("DIADOS", diados);
                            objParam.put("EMPRESA", objUsuCredential.getCodemp());
                            objParam.put("SUCURSAL", sucursal);
                            objParam.put("PERIODO", per);
                            objParam.put("TIPO", tipo_data);
                            if (tab.equals("personal")) {
                                objParam.put("ORDEN", norden);
                            } else {
                                objParam.put("ORDEN", 0);
                            }
                            //objParam.put("AREA", area);
                            objParam.put("c_area", area);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();

                            if (tab.equals("personal")) {

                                if (tabla.equals("1")) {
                                    if (chk == false) {
                                        //periodo actual sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                    } else {
                                        //reporte periodo actual con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                    }
                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {
                                    if (chk == false) {
                                        //periodo anterior sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                    } else {
                                        //periodo anterior con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                    }
                                }
                            } else {
                                if (tabla.equals("1")) {

                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {

                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                                }
                            }
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                            if (tab.equals("personal")) {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                            } else {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                            }
                        }
                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".pdf");
                        exporter.exportReport();
                        file = new File(rutaFileCumple + nom_reporte + ".pdf");
                        Filedownload.save(file, "application/pdf");
                    } else {//si elige excel
                        if (rbg_reporte.getItems().get(0).isChecked()) {
                            objParam.put("MES", mes_report);
                            objParam.put("DIAUNO", diauno);
                            objParam.put("DIADOS", diados);
                            objParam.put("EMPRESA", objUsuCredential.getCodemp());
                            objParam.put("SUCURSAL", sucursal);
                            objParam.put("PERIODO", per);
                            objParam.put("TIPO", tipo_data);
                            if (tab.equals("personal")) {
                                objParam.put("ORDEN", norden);
                            } else {
                                objParam.put("ORDEN", 0);
                            }
                            //objParam.put("AREA", area);
                            objParam.put("c_area", area);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            if (tab.equals("personal")) {
                                if (tabla.equals("1")) {
                                    if (chk == false) {
                                        //periodo actual sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                    } else {
                                        //reporte periodo actual con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                    }
                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {
                                    if (chk == false) {
                                        //periodo anterior sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                    } else {
                                        //periodo anterior con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                    }
                                }
                            } else {
                                if (tabla.equals("1")) {

                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {

                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                                }
                            }
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                            if (tab.equals("personal")) {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                            } else {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                            }
                        } else {
                            objParam.put("MES", mes_report);
                            objParam.put("DIAUNO", diauno);
                            objParam.put("DIADOS", diados);
                            objParam.put("EMPRESA", objUsuCredential.getCodemp());
                            objParam.put("SUCURSAL", sucursal);
                            objParam.put("PERIODO", per);
                            objParam.put("TIPO", tipo_data);
                            if (tab.equals("personal")) {
                                objParam.put("ORDEN", norden);
                            } else {
                                objParam.put("ORDEN", 0);
                            }
                            //objParam.put("AREA", area);
                            objParam.put("c_area", area);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            if (tab.equals("personal")) {
                                if (tabla.equals("1")) {
                                    if (chk == false) {
                                        //periodo actual sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF.jasper", false));
                                    } else {
                                        //reporte periodo actual con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualPDF.jasper", false));
                                    }
                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {
                                    if (chk == false) {
                                        //periodo anterior sin cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF.jasper", false));
                                    } else {
                                        //periodo anterior con cabecera
                                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaPDF.jasper", false));
                                    }
                                }
                            } else {
                                if (tabla.equals("1")) {

                                    //periodo actual sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportActualSCPDF_TOTAL.jasper", false));

                                } //reporte de tplplanilla
                                else if (tabla.equals("2")) {

                                    //periodo anterior sin cabecera
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSCPDF_TOTAL.jasper", false));

                                }
                            }
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                            if (tab.equals("personal")) {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla-" + aleatorio;
                            } else {
                                nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReportePlanilla_TOTAL-" + aleatorio;

                            }
                        }
                        /*JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                         reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                         JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                         JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                         jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);*/
                        JExcelApiExporter exporterXLS = new JExcelApiExporter();
                        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                        exporterXLS.exportReport();
                        file = new File(rutaFileCumple + nom_reporte + ".xls");
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

    /**
     * *****************************************************************************
     */
    /*try {
     Map<String, Object> objHashMap2 = new HashMap<String, Object>();
     Map objParam = new HashMap();
     objParam.put("MES", mes_report);
     objParam.put("DIAUNO", diauno);
     objParam.put("DIADOS", diados);
     objParam.put("EMPRESA", objUsuCredential.getCodemp());
     objParam.put("SUCURSAL", sucursal);
     objParam.put("PERIODO", per);
     objParam.put("REPORT_LOCALE", new Locale("en", "US"));
     final Execution exec = Executions.getCurrent();
     //reporte de tplplames
     if (tabla.equals("1")) {
     if (chk == false) {
     //periodo actual sin cabecera
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaActualSC.jasper", false));
     } else {
     //reporte periodo actual con cabecera
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaActual.jasper", false));
     }
     } 
     //reporte de tplplanilla
     else if (tabla.equals("2")) {
     if (chk == false) {
     //periodo anterior sin cabecera
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanillaSC.jasper", false));
     } else {
     //periodo anterior con cabecera
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportPlanilla.jasper", false));
     }
     }
     JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
     reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     int x = 100;
     int y = 0;
     int aleatorio = (int) (Math.random() * x) + y;
     jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
     nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
     JRTextExporter exporter = new JRTextExporter();
     exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);//1060
     exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);//636
     exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
     exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
     exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
     exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".txt");
     exporter.exportReport();
     file = new File(rutaFileCumple + nom_reporte + ".txt");
     FileInputStream fis = new FileInputStream(file);
     amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
     tipo = "txt";
     objHashMap2.put("amedia", amedia);
     objHashMap2.put("archivo", file.getAbsolutePath().toString());
     objHashMap2.put("tipo", tipo);
     Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
     window.doModal();
     } catch (Exception ex) {
     throw new RuntimeException(ex);
     } finally {
     if (is != null) {
     is.close();
     }
     if (conexion != null) {
     conexion.close();
     }
     }*/
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

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_boleta.detach();
    }

}
