package org.me.gj.controller.componentes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerReporte extends SelectorComposer<Component> implements Serializable {

    @Wire
    Window w_reporte;
    @Wire
    Iframe reporte;
    //JasperReport report;
    AMedia amedia;
    String reportearchivo, tipo;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir, tbbtn_btn_salir;

    Map parametros;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        parametros = new HashMap(Executions.getCurrent().getArg());
        amedia = (AMedia) parametros.get("amedia");
        reportearchivo = (String) parametros.get("archivo");
        tipo = (String) parametros.get("tipo");
        reporte.setContent(amedia);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("archivo", reportearchivo);
        objMapObjetos.put("tipo", tipo);
        Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/LovImpresion.zul", null, objMapObjetos);
        window.doModal();
    }

    @Listen("onClick=#tbbtn_btn_salir")
    public void botonSalir() {
        w_reporte.detach();
    }

    @Listen("onCreate=#w_reporte")
    public void ValidacionImpresion() {
        if ("pdf".equals(tipo)) {
            tbbtn_btn_imprimir.setVisible(false);
        } else {
            tbbtn_btn_imprimir.setVisible(true);
        }
    }

}
