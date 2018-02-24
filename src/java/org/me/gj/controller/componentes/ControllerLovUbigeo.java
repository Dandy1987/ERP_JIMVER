package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoUbigeo;
import org.me.gj.model.cxc.mantenimiento.Ubigeo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovUbigeo extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_ubigeo;
    @Wire
    Listbox lst_ubigeo;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_ubiid, txt_ubides, txt_paisid, txt_clidir_idubigeo, txt_clidir_ubigeo;
    //Instancias de Objetos 
    ListModelList<Ubigeo> objlstUbigeo = new ListModelList<Ubigeo>();
    DaoUbigeo objDaoUbigeo = new DaoUbigeo();
    Ubigeo objUbigeo;
    //Variables publicas
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovUbigeo.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_ubiid = (Textbox) parametros.get("txt_ubiid");
            txt_ubides = (Textbox) parametros.get("txt_ubides");
            txt_paisid = (Textbox) parametros.get("txt_paisid");
            objUbigeo = (Ubigeo) parametros.get("objUbigeo");
        } else if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idubigeo = (Textbox) parametros.get("txt_clidir_idubigeo");
            txt_clidir_ubigeo = (Textbox) parametros.get("txt_clidir_ubigeo");
        }
    }

    @Listen("onCreate=#w_lov_ubigeo")
    public void cargarUbigeo() throws SQLException {
        objlstUbigeo = objDaoUbigeo.listaUbigeo(1);
        lst_ubigeo.setModel(objlstUbigeo);
    }

    @Listen("onSelect=#lst_ubigeo")
    public void seleccionaUbigeo() {
        objUbigeo = (Ubigeo) lst_ubigeo.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_ubiid.setValue(objUbigeo.getUbi_cod());
            txt_ubides.setValue(objUbigeo.getUbi_nomdep() + "-"
                    + objUbigeo.getUbi_nompro() + "-"
                    + objUbigeo.getUbi_nomdis());
            txt_paisid.setDisabled(false);
            txt_paisid.focus();
        } else if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idubigeo.setValue(objUbigeo.getUbi_cod());
            txt_clidir_ubigeo.setValue(objUbigeo.getUbi_nomdep() + "-"
                    + objUbigeo.getUbi_nompro() + "-"
                    + objUbigeo.getUbi_nomdis());
        }
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
        
        w_lov_ubigeo.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarUbigeo() throws SQLException {
        objlstUbigeo = objDaoUbigeo.busquedaUbigeo(3, txt_busqueda.getValue().toUpperCase());
        lst_ubigeo.setModel(objlstUbigeo);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_ubigeo")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
        
    }
}
