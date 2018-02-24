package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionReparto;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionRutas;
import org.me.gj.controller.distribucion.mantenimiento.DaoHorario;
import org.me.gj.controller.facturacion.mantenimiento.ControllerProgramacionZonas;
import org.me.gj.model.distribucion.mantenimiento.Horario;
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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovHorario extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_horario;
    @Wire
    Listbox lst_horario;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_clidir_horario, txt_clidir_idhorario, txt_clidir_zona, txt_clidir_zonadesc;//clientes
    Textbox txt_horentdes, txt_horentid, txt_horid, txt_hordes, txt_transid, txt_zonid;
    Longbox txt_dirid;
    //Instancias de Objetos
    ListModelList<Horario> objlstHorario = new ListModelList<Horario>();
    DaoHorario objDaoHorario = new DaoHorario();
    Horario objHorario;
    //Variables publicas
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovHorario.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_zona = (Textbox) parametros.get("txt_clidir_zona");
            txt_clidir_idhorario = (Textbox) parametros.get("txt_clidir_idhorario");
            txt_clidir_horario = (Textbox) parametros.get("txt_clidir_horario");
        } else if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_horentid = (Textbox) parametros.get("txt_horentid");
            txt_horentdes = (Textbox) parametros.get("txt_horentdes");
            txt_transid = (Textbox) parametros.get("txt_transid");
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_horid = (Textbox) parametros.get("txt_horid");
            txt_hordes = (Textbox) parametros.get("txt_hordes");
            txt_dirid = (Longbox) parametros.get("txt_dirid");
        } else if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_horid = (Textbox) parametros.get("txt_horid");
            txt_hordes = (Textbox) parametros.get("txt_hordes");
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            txt_zonid = (Textbox) parametros.get("txt_zonid");
            txt_horid = (Textbox) parametros.get("txt_horid");
            txt_hordes = (Textbox) parametros.get("txt_hordes");
        }
    }

    @Listen("onCreate=#w_lov_horario")
    public void cargarHorario() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            objlstHorario = objDaoHorario.BusqueHorarioxZona(txt_clidir_zona.getText());
        } /*else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            objlstHorario = objDaoHorario.BusqueHorarioxZona(txt_zonid.getValue());
        }*/ else {
            objlstHorario = objDaoHorario.listaHorario(1);
        }
        lst_horario.setModel(objlstHorario);
    }

    @Listen("onSelect=#lst_horario")
    public void seleccionaHorario() {
        objHorario = (Horario) lst_horario.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_idhorario.setValue(objHorario.getTab_subdir());
            txt_clidir_horario.setValue(objHorario.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_horentid.setValue(objHorario.getTab_subdir());
            txt_horentdes.setValue(objHorario.getTab_subdes());
            txt_transid.focus();
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_horid.setValue(objHorario.getTab_subdir());
            txt_hordes.setValue(objHorario.getTab_subdes());
            txt_dirid.focus();
        } else if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_horid.setValue(objHorario.getTab_subdir());
            txt_hordes.setValue(objHorario.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            txt_horid.setValue(objHorario.getTab_subdir());
            txt_hordes.setValue(objHorario.getTab_subdes());
        }

        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
        w_lov_horario.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarHorario() throws SQLException {
        /*if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            objlstHorario = objDaoHorario.BusquedaHorarioxZona(txt_zonid.getValue(), txt_busqueda.getValue(), 1);
        } else*/ if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            objlstHorario = objDaoHorario.BusquedaHorarioxZona(txt_clidir_zona.getValue(), txt_busqueda.getValue(), 1);
        } else {
            objlstHorario = objDaoHorario.busquedaHorario(2, txt_busqueda.getValue().toUpperCase(), 1);
        }
        lst_horario.setModel(objlstHorario);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_horario")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
    }
}
