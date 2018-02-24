package org.me.gj.controller.impresion.logistica.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.me.gj.controller.logistica.informes.DaoNotaESInf;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ControllerImpresionNotaESResumen extends SelectorComposer<Component> implements Serializable {

    //Componentes Web
    @Wire
    Window w_lov_impresion_notaesresu;
    @Wire
    Listbox lst_resnotaes;
    @Wire
    Checkbox chk_all, chkresuar, chkdeargui, chklisdoc, chkresugui, chkresuguiref, ckresugui;
    @Wire
    Radiogroup rbg_tipo, rbg_impresion, rbg_lista;
    @Wire
    Combobox cb_impresoras;
    @Wire
    Datebox d_fecini, d_fecfin;
    //Instancias de Objetos
    ListModelList<Guias> objlstGuias;
    Guias objGuias = new Guias();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    //Variables publicas
    String tipo, fecha = "";
    String titulo, empresa, usuario;
    String separador = System.getProperty("file.separator");
    String ruta = "";
    Integer suc_id, emp_id;
    Map parametros;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionNotaESResumen.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");
        emp_id = (Integer) parametros.get("emp_id");
        suc_id = (Integer) parametros.get("suc_id");

        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
    }

    @Listen("onCreate=#w_lov_impresion_notaesresu")
    public void llenaRegistros() throws SQLException {
        //carga nota E/S
        objlstGuias = new ListModelList<Guias>();
        objlstGuias = objDaoManNotaES.listaGuias(1);
        if (objlstGuias != null) {
            lst_resnotaes.setModel(objlstGuias);
        } else {
            Messagebox.show("No se pudo cargar los datos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onSeleccion=#lst_resnotaes")
    public void seleccionRegistroImprimir(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstGuias.get(item.getIndex()).setSelImp(chk_Reg.isChecked());
        lst_resnotaes.setModel(objlstGuias);
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

    @Listen("onCheck=#chk_all")
    public void seleccionMultiple() {
        if (objlstGuias == null || objlstGuias.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_all.setChecked(false);
        } else {
            for (int i = 0; i < objlstGuias.getSize(); i++) {
                objlstGuias.get(i).setSelImp(chk_all.isChecked());
            }
            lst_resnotaes.setModel(objlstGuias);
        }
    }

    @Listen("onClick=#btn_procesar")
    public void Procesar() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {
        int i = 0;
        String nronotaes = "";

        for (int j = 0; j < objlstGuias.getSize(); j++) {
            if (objlstGuias.get(j).isSelImp()) {
                i = i + 1;
                nronotaes += "'" + objlstGuias.get(j).getCodigo() + "',";
            }
        }
        if (i <= 0) {
            Messagebox.show("Debe seleccionar 'Nota E/S'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (d_fecini.getValue() == null || d_fecfin.getValue() == null) {
            Messagebox.show("Debe seleccionar 'Fechas'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (!verificarFechas().equals("")) {
            Messagebox.show(verificarFechas());
        } else {
            String fecfin = sdf.format(d_fecfin.getValue());
            String fecini = sdf.format(d_fecini.getValue());
            ////SI ELIGIO PANTALLA
            if (rbg_impresion.getItems().get(0).isChecked()) {
                //Si Eligio Resumen de Articulo
                if (rbg_lista.getItems().get(0).isChecked()) {
                    NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 1);
                    if (objNotaES != null) {
                        Connection conexion = new ConectaBD().conectar();

                        InputStream is = null;
                        JasperPrint jasperPrint;
                        try {
                            Map objParam = new HashMap();
                            Map objMapObjetos = new HashMap();
                            AMedia amedia;
                            objParam.put("empresa", objUsuCredential.getEmpresa());
                            objParam.put("usuario", objUsuCredential.getCuenta());
                            objParam.put("emp_id", objUsuCredential.getCodemp());
                            objParam.put("suc_id", objUsuCredential.getCodsuc());
                            objParam.put("P_NOTAES", DaoNotaESInf.P_NOTAES);
                            objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                            objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            //si eligio grafico (pdf)
                            if (rbg_tipo.getItems().get(0).isChecked()) {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuProdNotaES.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuProdNotaES.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUPRODNOTAES" + aleatorio;
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
                            } else {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuProdNotaES_M.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuProdNotaES_M.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUPRODNOTAES" + aleatorio;
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
                            Window window = (Window) Executions.createComponents("/org/me/gj/view/logistica/informes/Reporte.zul", null, objMapObjetos);
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
                    } else {
                        Messagebox.show("No existe informe para esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                } //Si eligio Detalle de Articulo Nota E/S
                else if (rbg_lista.getItems().get(1).isChecked()) {
                    NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 2);
                    if (objNotaES != null) {
                        Connection conexion = new ConectaBD().conectar();

                        InputStream is = null;
                        JasperPrint jasperPrint;
                        try {
                            Map objParam = new HashMap();
                            Map objMapObjetos = new HashMap();
                            AMedia amedia;
                            objParam.put("empresa", objUsuCredential.getEmpresa());
                            objParam.put("usuario", objUsuCredential.getCuenta());
                            objParam.put("emp_id", objUsuCredential.getCodemp());
                            objParam.put("suc_id", objUsuCredential.getCodsuc());
                            objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                            objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            //si eligio grafico (pdf)
                            if (rbg_tipo.getItems().get(0).isChecked()) {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfDetaProdNotaES.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfDetaProdNotaES.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "DETAPRODNOTAES" + aleatorio;
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
                            } else {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfDetaProdNotaES_M.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfDetaProdNotaES_M.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "DETAPRODNOTAES" + aleatorio;
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
                            Window window = (Window) Executions.createComponents("/org/me/gj/view/logistica/informes/Reporte.zul", null, objMapObjetos);
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

                    } else {
                        Messagebox.show("No existe informe para esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                } //Si eligio Lista de Documento / Referencia
                else if (rbg_lista.getItems().get(2).isChecked()) {
                    NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 3);
                    if (objNotaES != null) {
                        Connection conexion = new ConectaBD().conectar();

                        InputStream is = null;
                        JasperPrint jasperPrint;
                        try {
                            Map objParam = new HashMap();
                            Map objMapObjetos = new HashMap();
                            AMedia amedia;
                            objParam.put("empresa", objUsuCredential.getEmpresa());
                            objParam.put("usuario", objUsuCredential.getCuenta());
                            objParam.put("emp_id", objUsuCredential.getCodemp());
                            objParam.put("suc_id", objUsuCredential.getCodsuc());
                            objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                            objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            //si eligio grafico (pdf)
                            if (rbg_tipo.getItems().get(0).isChecked()) {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfListDocNotaES.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfListDocNotaES.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "LISDOCNOTAES" + aleatorio;
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
                            } else {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfListDocNotaES_M.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfListDocNotaES_M.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "LISDOCNOTAES" + aleatorio;
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
                            Window window = (Window) Executions.createComponents("/org/me/gj/view/logistica/informes/Reporte.zul", null, objMapObjetos);
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
                    } else {
                        Messagebox.show("No existe informe para esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }//Si eligio el Resumen de Nota E/S
                else if (rbg_lista.getItems().get(3).isChecked()) {
                    NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 4);
                    if (objNotaES != null) {
                        Connection conexion = new ConectaBD().conectar();

                        InputStream is = null;
                        JasperPrint jasperPrint;
                        try {
                            Map objParam = new HashMap();
                            Map objMapObjetos = new HashMap();
                            AMedia amedia;
                            objParam.put("empresa", objUsuCredential.getEmpresa());
                            objParam.put("usuario", objUsuCredential.getCuenta());
                            objParam.put("emp_id", objUsuCredential.getCodemp());
                            objParam.put("suc_id", objUsuCredential.getCodsuc());
                            objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                            objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                            objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                            //si eligio grafico (pdf)
                            if (rbg_tipo.getItems().get(0).isChecked()) {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuNotaES.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuNotaES.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUNOTAES" + aleatorio;
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
                            }//Si elegio matricial
                            else {
                                final Execution exec = Executions.getCurrent();
                                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuNotaES_M.jasper", false));
                                //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuNotaES_M.jasper");
                                int x = 100;
                                int y = 0;
                                int aleatorio = (int) (Math.random() * x) + y;
                                jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUNOTAES" + aleatorio;
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
                            Window window = (Window) Executions.createComponents("/org/me/gj/view/logistica/informes/Reporte.zul", null, objMapObjetos);
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
                    } else {
                        Messagebox.show("No existe informe para esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            } //SI ELIGIO IMPRESORA
            else if (rbg_impresion.getItems().get(1).isChecked()) {
                PrintService designatedService = null;
                PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                if (printServices.length > 0)//si existen impresoras
                {
                    String impresora = cb_impresoras.getValue();
                    for (PrintService printService : printServices) {
                        if (printService.getName().equalsIgnoreCase(impresora)) {
                            designatedService = printService;
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
                            //Si elegio el Resumen Articulo Nota E/S
                            if (rbg_lista.getItems().get(0).isChecked()) {
                                NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 1);
                                if (objNotaES != null) {
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuProdNotaES_M.jasper", false));
                                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuProdNotaES_M.jasper");
                                    Map objParam = new HashMap();
                                    objParam.put("empresa", objUsuCredential.getEmpresa());
                                    objParam.put("usuario", objUsuCredential.getCuenta());
                                    objParam.put("emp_id", objUsuCredential.getCodemp());
                                    objParam.put("suc_id", objUsuCredential.getCodsuc());
                                    objParam.put("P_NOTAES", DaoNotaESInf.P_NOTAES);
                                    objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                                    objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUPRODNOTAES" + aleatorio;
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
                                } else {
                                    Messagebox.show("No existe registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            } //Si eligio Detalle Articulo Guia
                            else if (rbg_lista.getItems().get(1).isChecked()) {
                                NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 2);
                                if (objNotaES != null) {
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfDetaProdNotaES_M.jasper", false));
                                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfDetaProdNotaES_M.jasper");
                                    Map objParam = new HashMap();
                                    objParam.put("empresa", objUsuCredential.getEmpresa());
                                    objParam.put("usuario", objUsuCredential.getCuenta());
                                    objParam.put("emp_id", objUsuCredential.getCodemp());
                                    objParam.put("suc_id", objUsuCredential.getCodsuc());
                                    objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                                    objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUPRODNOTAES" + aleatorio;
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
                                } else {
                                    Messagebox.show("No existe registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            } //Si eligio Lista de Documento / Referencia
                            else if (rbg_lista.getItems().get(2).isChecked()) {
                                NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 3);
                                if (objNotaES != null) {
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfListDocNotaES_M.jasper", false));
                                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfListDocNotaES_M.jasper");
                                    Map objParam = new HashMap();
                                    objParam.put("empresa", objUsuCredential.getEmpresa());
                                    objParam.put("usuario", objUsuCredential.getCuenta());
                                    objParam.put("emp_id", objUsuCredential.getCodemp());
                                    objParam.put("suc_id", objUsuCredential.getCodsuc());
                                    objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                                    objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "LISDOCNOTAES" + aleatorio;
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
                                } else {
                                    Messagebox.show("No existe registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            } //Si eligio el Resumen de Nota E/S
                            else if (rbg_lista.getItems().get(3).isChecked()) {
                                NotaESCab objNotaES = new DaoNotaESInf().ListaResumenNotaES(fecini, fecfin, nronotaes, 4);
                                if (objNotaES != null) {
                                    final Execution exec = Executions.getCurrent();
                                    is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/informes/InfResuNotaES_M.jasper", false));
                                    //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/logistica/informes/InfResuNotaES_M.jasper");
                                    Map objParam = new HashMap();
                                    objParam.put("empresa", objUsuCredential.getEmpresa());
                                    objParam.put("usuario", objUsuCredential.getCuenta());
                                    objParam.put("emp_id", objUsuCredential.getCodemp());
                                    objParam.put("suc_id", objUsuCredential.getCodsuc());
                                    objParam.put("P_WHERER", DaoNotaESInf.P_WHERER);
                                    objParam.put("P_TITULO", DaoNotaESInf.P_TITULO);
                                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                                    String nom_reporte = Utilitarios.hoyAsString1() + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "RESUNOTAES" + aleatorio;
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
                                } else {
                                    Messagebox.show("No existe registros para imprmir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                } else {
                    Messagebox.show("Debe Seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            }
        }
    }

    @Listen("onClick=#btn_salir")
    public void botonSalir() {
        w_lov_impresion_notaesresu.detach();
    }

    //Eventos Secundarios - Validacion
    //Eventos Otros 
    public String verificarFechas() {
        String s_valor = "";
        //se tiene que validar que la fecha de emision sea del mismo periodo
        String fecha_ini = sdf.format(d_fecini.getValue());

        if (d_fecfin.getValue().getTime() < d_fecini.getValue().getTime()) {
            s_valor = "La fecha de Final debe ser Mayor o igual que : " + fecha_ini;
        }
        return s_valor;
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
