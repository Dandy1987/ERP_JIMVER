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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
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
import org.me.gj.controller.planillas.informes.DaoBoletaPago;
import org.me.gj.controller.seguridad.mantenimiento.DaoCfgInicial;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.informes.Boleta;
import org.me.gj.model.seguridad.mantenimiento.CfgInicial;
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

/**
 *
 * @author achocano
 */
public class ControllerImpresionBoleta extends SelectorComposer<Component> {

    @Wire
    Window w_lov_impresion_boleta;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;
    int emp_id, i = 0, flag;
    String anio, diauno, diados, mes_report, sucursal, personal, per, tabla, costo,
            tipo, fecha, filtro, periodo = "";
    ListModelList<Boleta> objlstBoleta, objlstBoleta1;
    DaoBoletaPago objDaoBoleta = new DaoBoletaPago();
	DaoCfgInicial objDaoCfgInicial;
    CfgInicial objCfgInicial;
    //RUTAS
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";
    Map parametros;
    String imagen;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionInformesMovimiento.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        //objlstBoleta1 = new ListModelList<Boleta>();
        anio = (String) parametros.get("anio");
        diauno = (String) parametros.get("diauno");
        diados = (String) parametros.get("diados");
        mes_report = (String) parametros.get("mes");
        objlstBoleta = (ListModelList<Boleta>) parametros.get("lista");
        sucursal = (String) parametros.get("sucursal");
        personal = (String) parametros.get("personal");
        per = (String) parametros.get("per");
        tabla = (String) parametros.get("tabla");
        costo = (String) parametros.get("costo");
        flag = (Integer) parametros.get("flag");
		objDaoCfgInicial = new DaoCfgInicial();
        objCfgInicial = new CfgInicial();
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int p = 0; p < ps.length; p++) {
            cb_impresoras.getItems().add(new Comboitem(ps[p].getName()));
        }

        seleccionImpresion();

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException, IOException, PrintException, JRException {
        int x = 100;
        int y = 0;
        int aleatorio = (int) (Math.random() * x) + y;
        String nom_reporte = null;
        List<JasperPrint> printList = null;
        Map<String, Object> objHashMap = new HashMap<String, Object>();
        Connection conexion = new ConectaBD().conectar();
        JasperPrint jasperPrint = null;
        JasperPrint printFinal = null;
        InputStream is = null;
        AMedia amedia;
        File file;

        try {
			String s_url = "";
			imagen = objDaoCfgInicial.getFirma(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
            if (rbg_impresion.getItems().get(0).isChecked()) {
                Map<String, Object> objHashMap2 = new HashMap<String, Object>();
                objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                objHashMap.put("anio", anio);
                objHashMap.put("diauno", diauno);
                objHashMap.put("diados", diados);
                objHashMap.put("mes", mes_report);
                objHashMap.put("CodigoIngresos", mes_report);
                objHashMap.put("periodo", mes_report);
                //objHashMap.put("usuario", objUsuCredential.getCuenta());
                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                if (rbg_tipo.getItems().get(0).isChecked()) {
                    if (rbg_reporte.getItems().get(0).isChecked()) {
                        final Execution exec = Executions.getCurrent();
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago.jasper", false));
                        //agregado para formato plsql
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                        reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                        jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                        // jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                    } else {
                        //aqui va imprimir en pantalla

                        for (int j = 0; j < objlstBoleta.getSize(); j++) {
                            if (flag == 1) {
                                objlstBoleta1 = objDaoBoleta.consultaBoleta(sucursal, objlstBoleta.get(j).getCodigo(), tabla, per, costo);
                                objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                                objHashMap.put("CodigoIngresos", objlstBoleta.get(j).getCodigo());
                                objHashMap.put("anio", anio);
                                objHashMap.put("diauno", diauno);
                                objHashMap.put("diados", diados);
                                objHashMap.put("mes", mes_report);
                                objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                objHashMap.put("sello", imagen);
                                objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                objHashMap.put("empresa", objUsuCredential.getCodemp());
                                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago.jasper", false));

                            } else if (flag == 2) {
                                objlstBoleta1 = objDaoBoleta.consultaCostos(sucursal, tabla, per, objlstBoleta.get(j).getCosto());
                                objHashMap.put("P_WHE", DaoBoletaPago.P_COSTOS);
                                objHashMap.put("anio", anio);
                                objHashMap.put("diauno", diauno);
                                objHashMap.put("diados", diados);
                                objHashMap.put("mes", mes_report);
                                objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                objHashMap.put("sello", imagen);
                                objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                objHashMap.put("empresa", objUsuCredential.getCodemp());
                                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCosto.jasper", false));

                            } else {
                                objHashMap.put("P_WHE", DaoBoletaPago.P_ALL);
                                objHashMap.put("anio", anio);
                                objHashMap.put("diauno", diauno);
                                objHashMap.put("diados", diados);
                                objHashMap.put("mes", mes_report);
                                objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                objHashMap.put("sello", imagen);
                                objHashMap.put("empresa", objUsuCredential.getCodemp());
                                objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCostoTo.jasper", false));

                            }

                            JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            //jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            printList = new ArrayList<JasperPrint>();
                            for (int q = 0; q <= 0; q++) {
                                printList.add(JasperFillManager.fillReport(reporte, objHashMap, conexion));
                            }
                            for (JasperPrint jp : printList) {
                                if (printFinal == null) {
                                    printFinal = jp;
                                } else {
                                    List<JRPrintPage> pages = jp.getPages();
                                    for (JRPrintPage page : pages) {
                                        printFinal.addPage(page);
                                    }
                                }
                            }
                        }
                    }
                    //jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                    nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFinal);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".pdf");
                    exporter.exportReport();
                    file = new File(rutaFileCumple + nom_reporte + ".pdf");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    tipo = "pdf";
                } else {//MATRICIAL --> me falta el report
                    for (int j = 0; j < objlstBoleta.getSize(); j++) {
                        //si se elegi por personal
                        if (flag == 1) {
                            objlstBoleta1 = objDaoBoleta.consultaBoleta(sucursal, objlstBoleta.get(j).getCodigo(), tabla, per, costo);
                            objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                            objHashMap.put("CodigoIngresos", objlstBoleta.get(j).getCodigo());
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                            objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            // objHashMap.put("sello", imagen);
                            //objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoM.jasper", false));

                        } // si se elige por centro de costos
                        else if (flag == 2) {
                            objlstBoleta1 = objDaoBoleta.consultaCostos(sucursal, tabla, per, objlstBoleta.get(j).getCosto());
                            objHashMap.put("P_WHE", DaoBoletaPago.P_COSTOS);
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                            objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                            objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoMCosto.jasper", false));

                        } else {
                            objHashMap.put("P_WHE", DaoBoletaPago.P_ALL);
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                            objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                            objHashMap.put("sello", imagen);
                            objHashMap.put("empresa", objUsuCredential.getCodemp());
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoMCostoTo.jasper", false));

                        }

                        JasperReport reporte = (JasperReport) JRLoader.loadObject(is);

                        printList = new ArrayList<JasperPrint>();
                        for (int q = 0; q <= 0; q++) {
                            printList.add(JasperFillManager.fillReport(reporte, objHashMap, conexion));

                        }
                        for (JasperPrint jp : printList) {
                            if (printFinal == null) {
                                printFinal = jp;
                            } else {
                                List<JRPrintPage> pages = jp.getPages();
                                for (JRPrintPage page : pages) {
                                    printFinal.addPage(page);
                                }
                            }
                        }
                    }
                    nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                    //formato
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);//1060
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);//636
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFinal);
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
                            objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago.jasper", false));
                            jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
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
                            objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago.jasper", false));
                            jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                        } else {
                            for (int j = 0; j < objlstBoleta.getSize(); j++) {
                                if (flag == 1) {
                                    objlstBoleta1 = objDaoBoleta.consultaBoleta(sucursal, objlstBoleta.get(j).getCodigo(), tabla, per, costo);
                                    objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                                    objHashMap.put("CodigoIngresos", objlstBoleta.get(j).getCodigo());
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago.jasper", false));

                                } else if (flag == 2) {
                                    objlstBoleta1 = objDaoBoleta.consultaCostos(sucursal, tabla, per, objlstBoleta.get(j).getCosto());
                                    objHashMap.put("P_WHE", DaoBoletaPago.P_COSTOS);
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCosto.jasper", false));

                                } else {
                                    objHashMap.put("P_WHE", DaoBoletaPago.P_ALL);
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCostoTo.jasper", false));

                                }
                                //  jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);

                                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);

                                printList = new ArrayList<JasperPrint>();
                                for (int q = 0; q <= 0; q++) {
                                    printList.add(JasperFillManager.fillReport(reporte, objHashMap, conexion));

                                }
                                for (JasperPrint jp : printList) {
                                    if (printFinal == null) {
                                        printFinal = jp;
                                    } else {
                                        List<JRPrintPage> pages = jp.getPages();
                                        for (JRPrintPage page : pages) {
                                            printFinal.addPage(page);
                                        }
                                    }
                                }

                            }
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                            JRPdfExporter exporter = new JRPdfExporter();
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFinal);//jasperPrint
                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".pdf");
                            exporter.exportReport();
                            file = new File(rutaFileCumple + nom_reporte + ".pdf");
                            Filedownload.save(file, "application/pdf");
                        }
                    } else {//si elige excel
                        if (rbg_reporte.getItems().get(0).isChecked()) {
                            objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                            objHashMap.put("anio", anio);
                            objHashMap.put("diauno", diauno);
                            objHashMap.put("diados", diados);
                            objHashMap.put("mes", mes_report);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            final Execution exec = Executions.getCurrent();
                            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago_Excel.jasper", false));
                            //jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                        } else {
                            for (int j = 0; j < objlstBoleta.getSize(); j++) {
                                if (flag == 1) {
                                    objlstBoleta1 = objDaoBoleta.consultaBoleta(sucursal, objlstBoleta.get(j).getCodigo(), tabla, per, costo);
                                    objHashMap.put("P_WHERE", DaoBoletaPago.P_WHERE);
                                    objHashMap.put("CodigoIngresos", objlstBoleta.get(j).getCodigo());
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPago_Excel.jasper", false));

                                } else if (flag == 2) {
                                    objlstBoleta1 = objDaoBoleta.consultaCostos(sucursal, tabla, per, objlstBoleta.get(j).getCosto());
                                    objHashMap.put("P_WHE", DaoBoletaPago.P_COSTOS);
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("costoPrincipal", objlstBoleta.get(j).getCosto());
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCosto_Excel.jasper", false));

                                } else {
                                    objHashMap.put("P_WHE", DaoBoletaPago.P_ALL);
                                    objHashMap.put("anio", anio);
                                    objHashMap.put("diauno", diauno);
                                    objHashMap.put("diados", diados);
                                    objHashMap.put("mes", mes_report);
                                    objHashMap.put("periodoPrincipal", per);//periodo que ingresa a los dataset
                                    objHashMap.put("tablaPrincipal", tabla);//tabla que ingresa a los dataset 
                                    objHashMap.put("sello", imagen);
                                    objHashMap.put("empresa", objUsuCredential.getCodemp());
                                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportBoletaPagoCostoTo_Excel.jasper", false));

                                }
                                //  jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);

                                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                                printList = new ArrayList<JasperPrint>();
                                for (int q = 0; q <= 0; q++) {
                                    printList.add(JasperFillManager.fillReport(reporte, objHashMap, conexion));

                                }
                                for (JasperPrint jp : printList) {
                                    if (printFinal == null) {
                                        printFinal = jp;
                                    } else {
                                        List<JRPrintPage> pages = jp.getPages();
                                        for (JRPrintPage page : pages) {
                                            printFinal.addPage(page);
                                        }
                                    }
                                }

                            }

                        }
                        nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "ReporteBoleta-" + aleatorio;
                      //  JasperReport reporte = (JasperReport) JRLoader.loadObject(is);

                        //jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                        JExcelApiExporter exporterXLS = new JExcelApiExporter();
                        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, printFinal);
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
