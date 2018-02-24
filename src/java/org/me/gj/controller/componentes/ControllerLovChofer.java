package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionReparto;
import org.me.gj.controller.distribucion.mantenimiento.DaoChofer;
import org.me.gj.model.distribucion.mantenimiento.Chofer;
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

public class ControllerLovChofer extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_chofer;
    @Wire
    Listbox lst_buschof;
    @Wire
    Textbox txt_chofid, txt_chofdes, txt_busqueda;

    //Instancias de Objetos
    ListModelList<Chofer> objlstChofer = new ListModelList<Chofer>();
    DaoChofer objDaoChofer = new DaoChofer();
    Chofer objChofer = new Chofer();
    //Variables publicas
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovChofer.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_chofid = (Textbox) parametros.get("txt_chofid");
            txt_chofdes = (Textbox) parametros.get("txt_chofdes");
        }
    }

    @Listen("onCreate=#w_lov_chofer")
    public void cargarChofer() throws SQLException {
        objlstChofer = objDaoChofer.listaChofer(1);
        lst_buschof.setModel(objlstChofer);
    }

    @Listen("onSelect=#lst_buschof")
    public void seleccionaChofer() {
        objChofer = (Chofer) lst_buschof.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_chofid.setValue(objChofer.getChof_id());
            txt_chofdes.setValue(objChofer.getChof_razsoc());
        }

        if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        }

        w_lov_chofer.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarChofer() throws SQLException {
        objlstChofer = objDaoChofer.busquedaChoferes(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_buschof.setModel(objlstChofer);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_chofer")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        }
    }

}
