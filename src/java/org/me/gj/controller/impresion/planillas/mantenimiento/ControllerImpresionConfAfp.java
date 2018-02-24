/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.impresion.planillas.mantenimiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
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
import net.sf.jasperreports.engine.JRParameter;
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
import org.me.gj.controller.planillas.mantenimiento.DaoConfAfps;
//import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
//import org.me.gj.controller.planillas.mantenimiento.DaoCargos;
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

/**
 *
 * @author greyes
 */
public class ControllerImpresionConfAfp extends SelectorComposer<Component> implements Serializable {

    //Componentes Web
    @Wire
    Window w_lov_impresion_confafp;
    @Wire
    Radiogroup rbg_impresion, rbg_tipo, rbg_reporte;
    @Wire
    Combobox cb_impresoras, cb_formato;
    @Wire
    Groupbox group_tipo, group_impresora, group_reporte;
    //Instancias de Objetos
    //Variables publicas
    int i = 0;
    String tipo, fecha, filtro, periodo = "";
    int emp_id;
    String titulo = "";
    String empresa, usuario;
    String separador = System.getProperty("file.separator");
    String ruta = "";
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionConfAfp.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        usuario = (String) parametros.get("usuario");
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
        seleccionImpresion();
    }

    @Listen("onCheck=#rbg_impresion")
    public void seleccionImpresion() {
        if (i == 0) {// TODOS
            if (rbg_impresion.getItems().get(0).isChecked()) {
//            rbg_tipo.setVisible(true);
//            cb_impresoras.setDisabled(true);
//            cb_impresoras.setSelectedIndex(-1);
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
//            rbg_tipo.setVisible(false);
//            cb_impresoras.setDisabled(false);
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

//    @Listen("onClick=#tbbtn_btn_imprimir")
//    public void botonImprimir() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {
//        //SI ELIGIO PANTALLA
//        if (rbg_impresion.getItems().get(0).isChecked()) {
//            Connection conexion = new ConectaBD().conectar();
//            InputStream is = null;
//            JasperPrint jasperPrint;
//            try {
//                Map objParam = new HashMap();
//                Map objMapObjetos = new HashMap();
//                AMedia amedia;
//                objParam.put("usuario", usuario);
//                objParam.put("P_WHERE", DaoConfAfps.P_WHERE);
//                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
//                //si eligio grafico (pdf)
//                if (rbg_tipo.getItems().get(0).isChecked()) {
//                    final Execution exec = Executions.getCurrent();
//                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ManConfAfp.jasper", false));
//                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManUniMan.jasper");
//                    int x = 100;
//                    int y = 0;
//                    int aleatorio = (int) (Math.random() * x) + y;
//                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
//                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "CONF" + aleatorio;
//                    if (System.getProperty("os.name").startsWith("Windows")) {
//                        ruta = "D:\\" + nom_reporte + ".pdf";
//                    } else {
//                        ruta = separador + "home" + separador + nom_reporte + ".pdf";
//                    }
//                    JRPdfExporter exporter = new JRPdfExporter();
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
//                    exporter.exportReport();
//                    File file;
//                    if (System.getProperty("os.name").startsWith("Windows")) {
//                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
//                        file = new File("D:\\" + nom_reporte + ".pdf");
//                    } else {
//                        // everything else
//                        file = new File(separador + "home" + separador + nom_reporte + ".pdf");
//                    }
//                    FileInputStream fis = new FileInputStream(file);
//                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
//                    tipo = "pdf";
//                    objMapObjetos.put("amedia", amedia);
//                    objMapObjetos.put("archivo", file.getAbsolutePath().toString());
//                    objMapObjetos.put("tipo", tipo);
//                    //si eligio matricial (txt)
//                } else {
//                    final Execution exec = Executions.getCurrent();
//                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ManConfAfp_M.jasper", false));
//                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManUniMan_M.jasper");
//                    int x = 100;
//                    int y = 0;
//                    int aleatorio = (int) (Math.random() * x) + y;
//                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
//                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "CONF" + aleatorio;
//                    if (System.getProperty("os.name").startsWith("Windows")) {
//                        ruta = "D:\\" + nom_reporte + ".txt";
//                    } else {
//                        ruta = separador + "home" + separador + nom_reporte + ".txt";
//                    }
//                    JRTextExporter exporter = new JRTextExporter();
//                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);
//                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);
//                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
//                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
//                    exporter.exportReport();
//                    File file;
//                    if (System.getProperty("os.name").startsWith("Windows")) {
//                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
//                        file = new File("D:\\" + nom_reporte + ".txt");
//                    } else {
//                        // everything else
//                        file = new File(separador + "home" + separador + nom_reporte + ".txt");
//                    }
//                    FileInputStream fis = new FileInputStream(file);
//                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
//                    tipo = "txt";
//                    objMapObjetos.put("amedia", amedia);
//                    objMapObjetos.put("archivo", file.getAbsolutePath().toString());
//                    objMapObjetos.put("tipo", tipo);
//                }
//                Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objMapObjetos);
//                window.doModal();
//            } catch (JRException ex) {
//                throw new RuntimeException(ex);
//            } finally {
//                if (is != null) {
//                    is.close();
//                }
//                if (conexion != null) {
//                    conexion.close();
//                }
//            }
//        } //SI ELIGIO IMPRESORA
//        else if (rbg_impresion.getItems().get(1).isChecked()) {
//            PrintService designatedService = null;
//            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//            if (printServices.length > 0)//si existen impresoras
//            {
//                String impresora = cb_impresoras.getValue();
//                for (int i = 0; i < printServices.length; i++) {
//                    if (printServices[i].getName().equalsIgnoreCase(impresora)) {
//                        designatedService = printServices[i];
//                    }
//                }
//                if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) //Si se selecciono una impresora
//                {
//                    JasperPrint jasperPrint;
//                    Connection conexion = new ConectaBD().conectar();
//                    InputStream is = null;
//                    try {
//                        int x = 100;
//                        int y = 0;
//                        int aleatorio = (int) (Math.random() * x) + y;
//                        final Execution exec = Executions.getCurrent();
//                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/mantenimiento/ManConfAfp_M.jasper", false));
//                        //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/mantenimiento/ManUniMan_M.jasper");
//                        Map objParam = new HashMap();
//                        objParam.put("usuario", usuario);
//                        objParam.put("P_WHERE", DaoConfAfps.P_WHERE);
//                        objParam.put("REPORT_LOCALE", new Locale("en", "US"));
//                        jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
//                        String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "CONF" + aleatorio;
//                        if (System.getProperty("os.name").startsWith("Windows")) {
//                            ruta = "D:\\" + nom_reporte + ".txt";
//                        } else {
//                            ruta = separador + "home" + separador + nom_reporte + ".txt";
//                        }
//                        JRTextExporter exporter = new JRTextExporter();
//                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 180);
//                        exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 29);
//                        exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
//                        exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
//                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, ruta);
//                        exporter.exportReport();
//                        File file;
//                        if (System.getProperty("os.name").startsWith("Windows")) {
//                            // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
//                            file = new File("D:\\" + nom_reporte + ".txt");
//                        } else {
//                            // everything else
//                            file = new File(separador + "home" + separador + nom_reporte + ".txt");
//                        }
//                        getPrinter(file, designatedService);
//                    } catch (JRException ex) {
//                        throw new RuntimeException(ex);
//                    } finally {
//                        if (is != null) {
//                            is.close();
//                        }
//                        if (conexion != null) {
//                            conexion.close();
//                        }
//                    }
//                }
//            } else {
//                Messagebox.show("Debe Seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
//            }
//        }
//    }
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {

        if (rbg_impresion.getItems().get(0).isChecked()) { // => SI ELIGIO PANTALLA <=

            Map<String, Object> objHashMap = new HashMap<String, Object>();
            Map<String, Object> objHashMap2 = new HashMap<String, Object>();

            AMedia amedia;
            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));

            if (rbg_tipo.getItems().get(0).isChecked()) { // SI ELIGIO GRAFICO (pdf)
                if (rbg_reporte.getItems().get(1).isChecked()) { // RESUMEN
                    JasperPrint jasperPrint;
                    Connection conexion = new ConectaBD().conectar();
                    InputStream is = null;
                    try {
                        //URL cadena = getClass().getClassLoader().getResource("../../images/logos/logo_reporte.jpg");

                        objHashMap.put("usuario", objUsuCredential.getCuenta());
                        objHashMap.put("P_WHERE", DaoConfAfps.P_WHERE);
                        objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));

                        is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../reportes/planillas/mantenimiento/ManConfAfp.jasper");
                        int x = 100;
                        int y = 0;
                        int aleatorio = (int) (Math.random() * x) + y;
                        jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                        String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;
                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
                        exporter.exportReport();
                        File file = new File("D:\\" + nom_reporte + ".pdf");
                        FileInputStream fis = new FileInputStream(file);
                        amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                        tipo = "pdf";
                        objHashMap2.put("amedia", amedia);
                        objHashMap2.put("archivo", file.getAbsolutePath().toString());
                        objHashMap2.put("tipo", tipo);
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
            } else { // RESUMEN
                JasperPrint jasperPrint;
                Connection conexion = new ConectaBD().conectar();
                InputStream is = null;
                try {
                    objHashMap.put("usuario", objUsuCredential.getCuenta());
                    objHashMap.put("P_WHERE", DaoConfAfps.P_WHERE);
                    objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../reportes/planillas/mantenimiento/ManConfAfp_M.jasper");
                    int x = 100;
                    int y = 0;
                    int aleatorio = (int) (Math.random() * x) + y;
                    jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                    String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 1060);
                    exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 636);
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".txt");
                    exporter.exportReport();
                    File file = new File("D:\\" + nom_reporte + ".txt");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "txt", "text/plain", fis);
                    tipo = "txt";
                    objHashMap2.put("amedia", amedia);
                    objHashMap2.put("archivo", file.getAbsolutePath().toString());
                    objHashMap2.put("tipo", tipo);
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
            Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/mantenimiento/Reporte.zul", null, objHashMap2);
            window.doModal();

        } else if (rbg_impresion.getItems().get(1).isChecked()) { //=> SI ELIGIO IMPRESORA <=

            //Map objParam = new HashMap();
            Map<String, Object> objHashMap = new HashMap<String, Object>();
            PrintService designatedService = null;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            if (printServices.length > 0) {// Si existen impresoras
                String impresora = cb_impresoras.getValue();
                for (PrintService printService : printServices) {
                    if (printService.getName().equalsIgnoreCase(impresora)) {
                        designatedService = printService;
                    }
                }
                if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) {// Si se selecciono una impresora
                    if (i == 0) { // TODOS - RESUMEN
                        JasperPrint jasperPrint;
                        Connection conexion = new ConectaBD().conectar();
                        InputStream is = null;
                        try {
                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("P_WHERE", DaoConfAfps.P_WHERE);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../reportes/planillas/mantenimiento/ManConfAfp_M.jasper");
                            int x = 100;
                            int y = 0;
                            int aleatorio = (int) (Math.random() * x) + y;
                            jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
                            String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;
                            JRTextExporter exporter = new JRTextExporter();
                            exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 150);
                            exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 62);
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(7));
                            exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(14));
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".txt");
                            exporter.exportReport();
                            File file = new File("D:\\" + nom_reporte + ".txt");
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
                    Messagebox.show("Debe seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            } else {
                Messagebox.show("No existen impresoras disponibles", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else if (rbg_impresion.getItems().get(2).isChecked()) { // => SI ELIGIO ARCHIVO <=
            if (cb_formato.getValue().trim().equals("")) {
                Messagebox.show("Ingrese el TIPO de Formato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (cb_formato.getValue().trim().equals("PDF")) {
                    if (rbg_reporte.getItems().get(1).isChecked()) {// RESUMEN
                        JasperPrint jasperPrint;
                        Connection conexion = new ConectaBD().conectar();
                        InputStream is = null;
                        try {
                            //URL cadena = getClass().getClassLoader().getResource("../../images/logos/logo_reporte.jpg");
                            //Map objParam = new HashMap();
                            Map<String, Object> objHashMap = new HashMap<String, Object>();

                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("P_WHERE", DaoConfAfps.P_WHERE);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            URL fileUrl = getClass().getClassLoader().getResource("../reportes/planillas/mantenimiento/ManConfAfp.jasper");
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(fileUrl);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            int x = 100;
                            int y = 0;
                            int aleatorio = (int) (Math.random() * x) + y;
                            jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                            String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;
                            JRPdfExporter exporter = new JRPdfExporter();
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
                            exporter.exportReport();
                            File file = new File("D:\\" + nom_reporte + ".pdf");
                            Filedownload.save(file, "application/pdf");
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
                    if (rbg_reporte.getItems().get(1).isChecked()) { // RESUMEN
                        JasperPrint jasperPrint;
                        Connection conexion = new ConectaBD().conectar();
                        InputStream is = null;
                        try {
                            //URL cadena = getClass().getClassLoader().getResource("../../images/logos/logo_reporte.jpg");
                            Map<String, Object> objHashMap = new HashMap<String, Object>();
                            //Map objParam = new HashMap();
                            objHashMap.put("usuario", objUsuCredential.getCuenta());
                            objHashMap.put("P_WHERE", DaoConfAfps.P_WHERE);
                            objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
                            objHashMap.put(JRParameter.IS_IGNORE_PAGINATION, true);
                            URL fileUrl = getClass().getClassLoader().getResource("../reportes/planillas/mantenimiento/ManConfAfp.jasper");
                            JasperReport reporte = (JasperReport) JRLoader.loadObject(fileUrl);
                            reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                            int x = 100;
                            int y = 0;
                            int aleatorio = (int) (Math.random() * x) + y;
                            jasperPrint = JasperFillManager.fillReport(reporte, objHashMap, conexion);
                            String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;
                            JExcelApiExporter exporterXLS = new JExcelApiExporter();
                            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".xls");
                            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                            exporterXLS.exportReport();
                            File file = new File("D:\\" + nom_reporte + ".xls");
                            Filedownload.save(file, "application/xlsx");
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
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_confafp.detach();
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
