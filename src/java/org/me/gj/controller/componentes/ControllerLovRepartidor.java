package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionReparto;
import org.me.gj.controller.distribucion.mantenimiento.DaoRepartidor;
import org.me.gj.model.distribucion.mantenimiento.Repartidor;
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

public class ControllerLovRepartidor extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_repartidor;
    @Wire
    Listbox lst_busrep;
    @Wire
    Textbox txt_repartid, txt_repartdes, txt_busqueda;

    //Instancias de Objetos
    ListModelList<Repartidor> objlstRepartidor = new ListModelList<Repartidor>();
    DaoRepartidor objDaoRepartidor = new DaoRepartidor();
    Repartidor objRepartidor = new Repartidor();
    //Variables publicas
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovRepartidor.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_repartid = (Textbox) parametros.get("txt_repartid");
            txt_repartdes = (Textbox) parametros.get("txt_repartdes");
        }
    }

    @Listen("onCreate=#w_lov_repartidor")
    public void cargarRepartidor() throws SQLException {
        objlstRepartidor = objDaoRepartidor.listaRepartidor(1);
        lst_busrep.setModel(objlstRepartidor);
    }

    @Listen("onSelect=#lst_busrep")
    public void seleccionaRepartidor() {
        objRepartidor = (Repartidor) lst_busrep.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_repartid.setValue(objRepartidor.getRep_id());
            txt_repartdes.setValue(objRepartidor.getS_nomcompleto());
        }

        if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        }

        w_lov_repartidor.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarRepartidor() throws SQLException {
        objlstRepartidor = objDaoRepartidor.busquedaRepartidores(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_busrep.setModel(objlstRepartidor);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_repartidor")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        }
    }

}
