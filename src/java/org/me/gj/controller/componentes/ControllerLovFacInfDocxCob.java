package org.me.gj.controller.componentes;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovFacInfDocxCob extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_docxcob;
    @Wire
    Tab tab_lista;
    @Wire
    Textbox txt_nrodocman;
    @Wire
    Datebox d_feccob, d_feccobman, d_fecadd;
    @Wire
    Combobox cb_tipdoc;
    @Wire
    Checkbox chk_resumen, chk_estado;
    @Wire
    Button btn_actsaldos, btn_guardar, btn_cancelar;
    //Instancias de Objetos
    Utilitarios objUtil;
    //Variables publicas
    String controlador, empresa, usuario;
    Map parametros;
    int suc_id, emp_id;
    public static boolean bandera = false;
    SimpleDateFormat sdffe = new SimpleDateFormat("EEE");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovFacInfDocxCob.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");

        objUtil = new Utilitarios();
        String diaent = sdffe.format(objUtil.hoyAsFecha());
        if (diaent.equals("sáb")) {
            d_feccob.setValue(Utilitarios.sumaDias(objUtil.hoyAsFecha(), 2));
        } else {
            d_feccob.setValue(Utilitarios.sumaDias(objUtil.hoyAsFecha(), 1));
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_cancelar")
    public void botonSalir() {
        //w_lov_docxcob.detach();
        tab_lista.setSelected(true);
    }

}
