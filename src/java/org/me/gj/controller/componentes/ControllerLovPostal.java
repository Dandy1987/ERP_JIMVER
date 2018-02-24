package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoPostal;
import org.me.gj.model.cxc.mantenimiento.Postal;
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

public class ControllerLovPostal extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_postal;
    @Wire
    Listbox lst_postal;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_posid, txt_posdes, txt_clidir_idpost, txt_clidir_postal;
    //Instancias de Objetos  
    ListModelList<Postal> objlstPostal = new ListModelList<Postal>();
    DaoPostal objDaoPostal = new DaoPostal();
    Postal objPostal;
    //Variables publicas      
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovPostal.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idpost = (Textbox) parametros.get("txt_clidir_idpost");
            txt_clidir_postal = (Textbox) parametros.get("txt_clidir_postal");
        }
    }

    @Listen("onCreate=#w_lov_postal")
    public void cargarPostal() throws SQLException {
        objlstPostal = objDaoPostal.listaPostales(1);
        lst_postal.setModel(objlstPostal);
    }

    @Listen("onSelect=#lst_postal")
    public void seleccionaPostal() {
        objPostal = (Postal) lst_postal.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idpost.setValue(objPostal.getTab_subdir());
            txt_clidir_postal.setValue(objPostal.getTab_subdes());
        }
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
        w_lov_postal.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarPostal() throws SQLException {
        //String consulta=txt_busqueda.getValue() == null?"%%":txt_busqueda.getValue().toUpperCase();
        objlstPostal = objDaoPostal.busquedaPostales(2, txt_busqueda.getValue() == null ? "%%" : txt_busqueda.getValue().toUpperCase(), 1);
        lst_postal.setModel(objlstPostal);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_postal")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerLovMantDirecciones")) {
            ControllerLovMantDirecciones.bandera = false;
        }
    }
}
