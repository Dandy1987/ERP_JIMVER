package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.controller.distribucion.mantenimiento.ControllerProgramacionRutas;
import org.me.gj.controller.facturacion.mantenimiento.ControllerProgramacionZonas;
import org.me.gj.controller.facturacion.mantenimiento.DaoZonas;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.model.facturacion.mantenimiento.Zona;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovZonas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_zonas;
    @Wire
    Listbox lst_buszon;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_clidir_zona, txt_clidir_zonadesc, txt_clidir_vendedor, txt_clidir_postal,
            txt_clidir_idubigeo, txt_clidir_ubigeo, txt_clidir_idhorario;
    Textbox txt_zonid, txt_zondes, txt_transid, txt_horentid, txt_dvisid;
    Intbox txt_clidir_idpostal;
    //Instancias de Objetos
    ListModelList<Zona> objlstZonas = new ListModelList<Zona>();
    DaoZonas objDaoZonas = new DaoZonas();
    Zona objZona = new Zona();
    //Variables publicas         
    String controlador;
    String diavisita;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovZonas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_zona = (Textbox) parametros.get("txt_clidir_zona");
            txt_clidir_zonadesc = (Textbox) parametros.get("txt_clidir_zonadesc");
            txt_clidir_vendedor = (Textbox) parametros.get("txt_clidir_vendedor");
            txt_clidir_idubigeo = (Textbox) parametros.get("txt_clidir_idubigeo");
            txt_clidir_ubigeo = (Textbox) parametros.get("txt_clidir_ubigeo");
            txt_clidir_idpostal = (Intbox) parametros.get("txt_clidir_idpostal");
            txt_clidir_postal = (Textbox) parametros.get("txt_clidir_postal");
            txt_clidir_idhorario = (Textbox) parametros.get("txt_clidir_idhorario");
        } else if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_zonid = (Textbox) parametros.get("txt_zonid");
            txt_zondes = (Textbox) parametros.get("txt_zondes");
            txt_horentid = (Textbox) parametros.get("txt_horentid");
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_zonid = (Textbox) parametros.get("txt_zonid");
            txt_zondes = (Textbox) parametros.get("txt_zondes");
            txt_transid = (Textbox) parametros.get("txt_transid");
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_zonid = (Textbox) parametros.get("txt_zonid");
            txt_zondes = (Textbox) parametros.get("txt_zondes");
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            diavisita = (String) parametros.get("txt_dvisid");
            txt_zonid = (Textbox) parametros.get("txt_zonid");
            txt_zondes = (Textbox) parametros.get("txt_zondes");
        }
    }

    @Listen("onCreate=#w_lov_zonas")
    public void cargarZonas() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            objlstZonas = objDaoZonas.listaZonaxDiaVisita(diavisita, 1);
        } else {
            objlstZonas = objDaoZonas.listaZonas(1);
        }
        lst_buszon.setModel(objlstZonas);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarZonas() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            objlstZonas = objDaoZonas.busquedaZonaxDiaVisita(2, diavisita, txt_busqueda.getValue().toUpperCase(), 1);
        } else {
            objlstZonas = objDaoZonas.busquedaZonas(2, txt_busqueda.getValue().toUpperCase(), 1);
        }

        lst_buszon.setModel(objlstZonas);
    }

    @Listen("onSelect=#lst_buszon")
    public void seleccionaRegistro() throws SQLException {
        objZona = lst_buszon.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantDirecciones")) {
            txt_clidir_zona.setValue(objZona.getZon_id());
            txt_clidir_zonadesc.setValue(objZona.getZon_des());
            txt_clidir_vendedor.setValue(objZona.getZon_idven() + " - " + objZona.getZon_apenom());
            txt_clidir_idubigeo.setValue(objZona.getZon_ubiid());
            txt_clidir_ubigeo.setValue(objZona.getZon_ubides());
            txt_clidir_idpostal.setValue(objZona.getZon_postid());
            txt_clidir_postal.setValue(objZona.getZon_postdes());
            txt_clidir_idhorario.focus();
        } else if (parametros.get("validaBusqueda").equals("mantProgZonas")) {
            txt_zonid.setValue(objZona.getZon_id());
            txt_zondes.setValue(objZona.getZon_des());
            txt_horentid.focus();
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_zonid.setValue(objZona.getZon_id());
            txt_zondes.setValue(objZona.getZon_des());
            txt_transid.focus();
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_zonid.setValue(objZona.getZon_id());
            txt_zondes.setValue(objZona.getZon_des());
        } else if (parametros.get("validaBusqueda").equals("mantProgRutas")) {
            txt_zonid.setValue(objZona.getZon_id());
            txt_zondes.setValue(objZona.getZon_des());
        }
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
        w_lov_zonas.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_zonas")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        } else if (controlador.equals("ControllerProgramacionZonas")) {
            ControllerProgramacionZonas.bandera = false;
        } else if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        } else if (controlador.equals("ControllerProgramacionRutas")) {
            ControllerProgramacionRutas.bandera = false;
        }
    }
}
