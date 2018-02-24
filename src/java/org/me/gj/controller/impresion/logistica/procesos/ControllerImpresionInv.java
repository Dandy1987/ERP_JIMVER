package org.me.gj.controller.impresion.logistica.procesos;

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
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.procesos.DaoInvFisFormato;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.procesos.InvFisico;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ControllerImpresionInv extends SelectorComposer<Component> implements Serializable {

    //Componentes Web
    @Wire
    Window w_lov_impresion_inv;
    @Wire
    Radiogroup rbg_tipo;
    @Wire
    Combobox cb_impresoras;
    //Instancias de Objetos
    ListModelList<InvFisico> objlstInvFisico;
    DaoInvFisFormato objDaoInvFisFormato = new DaoInvFisFormato();

    String tipo, periodo = "";
    String titulo = "";
    String P_WHERE;
    Map parametros;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresionInv.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        objlstInvFisico = (ListModelList<InvFisico>) parametros.get("listainventarios");
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws ScriptException, FileNotFoundException, IOException, JRException, SQLException, PrintException {
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
                Connection conexion = new ConectaBD().conectar();
                InputStream is = null;
                JasperPrint jasperPrint;
                try {
                    AMedia amedia;
                    Map objParam = new HashMap();
                    Map objMapObjetos = new HashMap();
                    objParam.put("P_WHERE", DaoInvFisFormato.P_WHERE);
                    objParam.put("P_EMPRESA", objUsuarioCredential.getEmpresa().toUpperCase());
                    objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                    //si eligio grafico (pdf)
                    int x = 100;
                    int y = 0;
                    int aleatorio = (int) (Math.random() * x) + y;
                    //eligio etiqueta
                    if (rbg_tipo.getItems().get(0).isChecked()) {
                        final Execution exec = Executions.getCurrent();
                        //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("WEB-INF/reportes/logistica/procesos/ProcPedCom.jasper");
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/procesos/InventarioEtiqueta.jasper", false));
                    } else {
                        final Execution exec = Executions.getCurrent();
                        //is = Thread.currentThread().getContextClassLoader().getResourceAsStream("WEB-INF/reportes/logistica/procesos/ProcPedCom.jasper");
                        is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/logistica/procesos/InventarioLista.jasper", false));
                    }
                    jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
                    String nom_reporte = Utilitarios.hoyAsString1() + "INV" + aleatorio;
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
                    exporter.exportReport();
                    File file = new File("D:\\" + nom_reporte + ".pdf");
                    FileInputStream fis = new FileInputStream(file);
                    amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
                    objMapObjetos.put("amedia", amedia);
                    objMapObjetos.put("archivo", file.getAbsolutePath().toString());
                    objMapObjetos.put("tipo", "pdf");
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
                Messagebox.show("Debe Seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            Messagebox.show("No existen impresoras disponibles", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresion_inv.detach();
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
