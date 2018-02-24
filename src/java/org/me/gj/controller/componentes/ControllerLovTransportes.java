package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionReparto;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionRutas;
import org.me.gj.controller.distribucion.mantenimiento.DaoVehiculo;
import org.me.gj.controller.facturacion.mantenimiento.ControllerProgramacionZonas;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
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

public class ControllerLovTransportes extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_transportes;
    @Wire
    Listbox lst_transportes;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_transid, txt_transdes, txt_glosa, txt_veniddet;
    //Instancias de Objetos
    ListModelList<Vehiculo> objlstVehiculos = new ListModelList<Vehiculo>();
    DaoVehiculo objDaoVehiculos = new DaoVehiculo();
    Vehiculo objVehiculo;
    //Variables publicas
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovTransportes.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_transid = (Textbox) parametros.get("txt_transid");
            txt_transdes = (Textbox) parametros.get("txt_transdes");
            txt_glosa = (Textbox) parametros.get("txt_glosa");
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_transid = (Textbox) parametros.get("txt_transid");
            txt_transdes = (Textbox) parametros.get("txt_transdes");
            txt_veniddet = (Textbox) parametros.get("txt_veniddet");
        } else if (parametros.get("validaBusqueda").equals("LovFacInfGuiaRem")) {
            txt_transid = (Textbox) parametros.get("txt_transid");
            txt_transdes = (Textbox) parametros.get("txt_transdes");
        } else if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_transid = (Textbox) parametros.get("txt_transid");
            txt_transdes = (Textbox) parametros.get("txt_transdes");
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            txt_transid = (Textbox) parametros.get("txt_transid");
            txt_transdes = (Textbox) parametros.get("txt_transdes");
        }
    }

    @Listen("onCreate=#w_lov_transportes")
    public void cargarVehiculos() throws SQLException {
        objlstVehiculos = objDaoVehiculos.listaVehiculo(1);
        lst_transportes.setModel(objlstVehiculos);
    }

    @Listen("onSelect=#lst_transportes")
    public void seleccionaVehiculo() {
        objVehiculo = (Vehiculo) lst_transportes.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_transid.setValue(objVehiculo.getTrans_id());
            txt_transdes.setValue(objVehiculo.getTrans_alias());
            txt_glosa.focus();
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_transid.setValue(objVehiculo.getTrans_id());
            txt_transdes.setValue(objVehiculo.getTrans_alias());
            txt_veniddet.focus();
        } else if (parametros.get("validaBusqueda").equals("LovFacInfGuiaRem")) {
            txt_transid.setValue(objVehiculo.getTrans_id());
            txt_transdes.setValue(objVehiculo.getTrans_alias());
        } else if (parametros.get("validaBusqueda").equals("mantProgReparto")) {
            txt_transid.setValue(objVehiculo.getTrans_id());
            txt_transdes.setValue(objVehiculo.getTrans_alias());
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            txt_transid.setValue(objVehiculo.getTrans_id());
            txt_transdes.setValue(objVehiculo.getTrans_alias());
        }

        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerLovFacInfGuiaRem")) {
            ControllerLovFacInfGuiaRem.bandera = false;
        } else if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
        w_lov_transportes.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarVehiculo() throws SQLException {
        objlstVehiculos = objDaoVehiculos.busquedaVehiculo(3, txt_busqueda.getValue().toUpperCase(), 1);
        lst_transportes.setModel(objlstVehiculos);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_transportes")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerLovFacInfGuiaRem")) {
            ControllerLovFacInfGuiaRem.bandera = false;
        } else if (controlador.equals("ControllerProgramacionReparto")) {
            ControllerProgramacionReparto.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
    }
}
