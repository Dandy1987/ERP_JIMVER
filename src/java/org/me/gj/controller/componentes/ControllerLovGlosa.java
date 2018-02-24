package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.informes.ControllerConsultaxDocumento;
import org.me.gj.controller.cxc.informes.DaoCtaCob;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionReparto;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
import org.me.gj.model.cxc.mantenimiento.CtaCobCab;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovGlosa extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_glosa;
    @Wire
    Textbox txt_glosa, txt_nrodoc;
    @Wire
    Button btn_cambiar, btn_cancelar;
    //Instancias de Objetos
    //Variables publicas
    Map parametros;
    String controlador;
    String modoEjecucion;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovGlosa.class);

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ConsultaxDoc")) {
            txt_nrodoc = (Textbox) parametros.get("txt_nrodoc");
        }
    }
    
    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_cambiar")
    public void btn_Aceptar() throws SQLException, ParseException {
    	if (parametros.get("validaBusqueda").equals("ConsultaxDoc")) {
            Messagebox.show("Desea Ud. actualizar la observacion del documento ? ", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
				        ParametrosSalida parametroSalida ;
				    	String glosa = txt_glosa.getValue().toUpperCase();
				    	String nrodoc = txt_nrodoc.getValue();
				        parametroSalida = new DaoCtaCob().actualizaGlosa(nrodoc, glosa);				        
				        if(parametroSalida.getFlagIndicador() == 0){
				        	txt_glosa.setValue("");
				        }				        
				        Messagebox.show(parametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
			    	 }
                }
          });
    	}
    }

    @Listen("onClick=#btn_cancelar")
    public void btn_Cancelar() {
        w_lov_glosa.detach();
    }
    
    @Listen("onClose=#w_lov_glosa")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerConsultaxDocumento")) {
            ControllerConsultaxDocumento.bandera = false;
        }
    }
}

