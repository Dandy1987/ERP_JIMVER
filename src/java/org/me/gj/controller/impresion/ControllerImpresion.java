package org.me.gj.controller.impresion;

import com.lowagie.text.pdf.PdfException;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerImpresion extends SelectorComposer<Component> implements Serializable {

    //Componentes Web
    @Wire
    Window w_lov_impresionx;
    Toolbarbutton tbbtn_btn_imprimir, tbbtn_btn_salir;
    @Wire
    Combobox cb_impresoras;
    //Instancias de Objetos
    //Variables publicas
    Map parametros;
    String archivo, tipo;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerImpresion.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        archivo = (String) parametros.get("archivo");
        tipo = (String) parametros.get("tipo");
        //agregamos al combo todas las impresoras agregadas localmente
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < ps.length; i++) {
            cb_impresoras.getItems().add(new Comboitem(ps[i].getName()));
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void Imprimir() throws PrintException, FileNotFoundException, IOException, PdfException, PrinterException {
        PrintService designatedService = null;
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        if (printServices.length > 0)//si existen impresoras
        {
            String impresora = cb_impresoras.getValue();
            if (!impresora.equals("") || cb_impresoras.getSelectedIndex() != -1) //Si se selecciono una impresora
            {
                for (int i = 0; i < printServices.length; i++) {
                    if (printServices[i].getName().equalsIgnoreCase(impresora)) {
                        designatedService = printServices[i];
                    }
                }
                File file = new File(archivo);
                getPrinterTXT(file, designatedService);
            } else {
                Messagebox.show("Debe Seleccionar una impresora", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }

    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_lov_impresionx.detach();
    }

    //Eventos Secundarios - Validacion
    //Eventos Otros
    public void getPrinterTXT(File file, PrintService sp) throws PrintException, FileNotFoundException {
        javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;
        javax.print.attribute.PrintRequestAttributeSet pras = new javax.print.attribute.HashPrintRequestAttributeSet();
        javax.print.DocPrintJob job = sp.createPrintJob();
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();
        javax.print.Doc doc = new javax.print.SimpleDoc(fis, flavor, das);
        job.print(doc, pras);
    }

    public void getPrinterPDF(File file, PrintService sp) throws FileNotFoundException, IOException, PrinterException, PdfException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        PDFFile pdffile = new PDFFile(bb);
        PDFPrintPage pages = new PDFPrintPage((pdffile));
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setPrintService(sp);
        //Formato da página
        Paper paper = new Paper();
        paper.setSize(595, 842);
        PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
        pf.setPaper(paper);
        paper.setSize(595, 842);
        paper.setImageableArea(0, 0, 595, 842);
        Book book = new Book();
        book.append(pages, pf, pdffile.getNumPages());
        pjob.setPageable(book);
        pjob.print();
    }

}
