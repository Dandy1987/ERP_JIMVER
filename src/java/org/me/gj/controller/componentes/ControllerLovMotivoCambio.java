package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoMotivoCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerProNotasCIR;
import org.me.gj.model.logistica.mantenimiento.MotCam;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovMotivoCambio extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_motcamb;
    @Wire
    Listbox lst_motcamb;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_motdes, txt_cliid;
    Textbox txt_motid;
    Datebox d_fecemi;
    //Instancias de Objetos
    ListModelList<MotCam> objlstMotCam = new ListModelList<MotCam>();
    DaoMotivoCambio objDaoMotivoCambio = new DaoMotivoCambio();
    MotCam objMotCam;
    //Variables publicas 
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovMotivoCambio.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_motid = (Textbox) parametros.get("txt_motid");
            txt_motdes = (Textbox) parametros.get("txt_motdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_motid = (Textbox) parametros.get("txt_motid");
            txt_motdes = (Textbox) parametros.get("txt_motdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_motid = (Textbox) parametros.get("txt_motid");
            txt_motdes = (Textbox) parametros.get("txt_motdes");
            txt_cliid = (Textbox) parametros.get("txt_nic_provid");
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_motid = (Textbox) parametros.get("txt_motid");
            txt_motdes = (Textbox) parametros.get("txt_motdes");
            d_fecemi = (Datebox) parametros.get("d_fecemi");
        }

    }

    @Listen("onCreate=#w_lov_motcamb")
    public void cargarMotivos() throws SQLException {
        objlstMotCam = objDaoMotivoCambio.listaMotivosCambio(1);
        lst_motcamb.setModel(objlstMotCam);
    }

    @Listen("onSelect=#lst_motcamb")
    public void seleccionaMotivo() {
        objMotCam = (MotCam) lst_motcamb.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_motid.setValue(objMotCam.getTab_subdir());
            txt_motdes.setValue(objMotCam.getTab_subdes());
            txt_cliid.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_motid.setValue(objMotCam.getTab_subdir());
            txt_motdes.setValue(objMotCam.getTab_subdes());
            txt_cliid.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_motid.setValue(objMotCam.getTab_subdir());
            txt_motdes.setValue(objMotCam.getTab_subdes());
            txt_cliid.focus();
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_motid.setValue(objMotCam.getTab_subdir());
            txt_motdes.setValue(objMotCam.getTab_subdes());
            d_fecemi.focus();
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
        w_lov_motcamb.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarDireccion() throws SQLException {
        objlstMotCam = objDaoMotivoCambio.busquedaMotivo(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_motcamb.setModel(objlstMotCam);
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstMotCam = objDaoMotivoCambio.listaMotivosCambio(1);
            lst_motcamb.setModel(objlstMotCam);
        }
    }

    @Listen("onClose=#w_lov_motcamb")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerIntercambio")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
    }
}
