package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.logistica.procesos.ControllerGenOrdCom;
import org.me.gj.controller.logistica.procesos.ControllerGenPedCom;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.controller.logistica.procesos.ControllerInvFisico;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovUbicacion extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_ubica;
    @Wire
    Listbox lst_ubica;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_pedubi, txt_peddes, txt_ocd_idubi, txt_ocd_desubi, txt_ubicod, txt_ubides;//clientes
    Button btn_aceptar;
    Intbox txt_entero;
    //Instancias de Objetos 
    ListModelList<Ubicaciones> objlstUbicacion = new ListModelList<Ubicaciones>();
    DaoUbicaciones objDaoUbicacion = new DaoUbicaciones();
    Ubicaciones objUbicacion;
    //Variables publicas 
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovUbicacion.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            txt_pedubi = (Textbox) parametros.get("txt_pedubi");
            txt_peddes = (Textbox) parametros.get("txt_peddes");
        } else if (parametros.get("validaBusqueda").equals("mantOrdComp")) {
            txt_ocd_idubi = (Textbox) parametros.get("txt_ocd_idubi");
            txt_ocd_desubi = (Textbox) parametros.get("txt_ocd_desubi");
        } else if (parametros.get("validaBusqueda").equals("mantNotaES")) {
            txt_ubicod = (Textbox) parametros.get("txt_ubicod");
            txt_ubides = (Textbox) parametros.get("txt_ubides");
        } else if (parametros.get("validaBusqueda").equals("InvFisico")) {
            txt_ubicod = (Textbox) parametros.get("txt_ubicod");
            txt_ubides = (Textbox) parametros.get("txt_ubides");
            txt_entero = (Intbox) parametros.get("txt_entero");
        }

    }

    @Listen("onCreate=#w_lov_ubica")
    public void cargarUbicacion() throws SQLException {
        objlstUbicacion = objDaoUbicacion.listaUbicaciones(1);
        lst_ubica.setModel(objlstUbicacion);
    }

    @Listen("onSelect=#lst_ubica")
    public void seleccionaUbicacion() {
        objUbicacion = (Ubicaciones) lst_ubica.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            txt_pedubi.setValue(objUbicacion.getUbic_id());
            txt_peddes.setValue(objUbicacion.getUbic_des());
        } else if (parametros.get("validaBusqueda").equals("mantOrdComp")) {
            txt_ocd_idubi.setValue(objUbicacion.getUbic_id());
            txt_ocd_desubi.setValue(objUbicacion.getUbic_des());
        } else if (parametros.get("validaBusqueda").equals("mantNotaES")) {
            txt_ubicod.setValue(objUbicacion.getUbic_id());
            txt_ubides.setValue(objUbicacion.getUbic_des());
        } else if (parametros.get("validaBusqueda").equals("InvFisico")) {
            txt_ubicod.setValue(objUbicacion.getUbic_id());
            txt_ubides.setValue(objUbicacion.getUbic_des());
            txt_entero.focus();
        }

        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerGenOrdCom")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerInvFisico")) {
            ControllerGenNotaES.bandera = false;
        }
        w_lov_ubica.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarUbicacion() throws SQLException {
        objlstUbicacion = objDaoUbicacion.busquedaUbicacion(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_ubica.setModel(objlstUbicacion);
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_ubica")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerGenOrdCom")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerInvFisico")) {
            ControllerInvFisico.bandera = false;
        }
    }
}
