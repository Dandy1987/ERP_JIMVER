package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoGiro;
import org.me.gj.model.cxc.mantenimiento.Giro;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovGiro extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_giro;
    @Wire
    Listbox lst_giro;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_clidir_idgiro, txt_ubiid, txt_ubides, txt_paisid, txt_clidir_giro;
    Button btn_aceptar;
    //Instancias de Objetos
    ListModelList<Giro> objlstGiro = new ListModelList<Giro>();
    DaoGiro objDaoGiro = new DaoGiro();
    Giro objGiro;
    Map parametros;
    //Variables publicas
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovGiro.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idgiro = (Textbox) parametros.get("txt_clidir_idgiro");
            txt_clidir_giro = (Textbox) parametros.get("txt_clidir_giro");
        }
    }

    @Listen("onCreate=#w_lov_giro")
    public void cargarGiro() throws SQLException {
        objlstGiro = objDaoGiro.listaGiros(1);
        lst_giro.setModel(objlstGiro);
    }

    @Listen("onSelect=#lst_giro")
    public void seleccionaGiro() {
        objGiro = (Giro) lst_giro.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idgiro.setValue(objGiro.getTab_subdir());
            txt_clidir_giro.setValue(objGiro.getTab_subdes());
        }
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
        w_lov_giro.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarGiro() throws SQLException {
        objlstGiro = objDaoGiro.busquedaGiros(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_giro.setModel(objlstGiro);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_giro")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
    }
}
