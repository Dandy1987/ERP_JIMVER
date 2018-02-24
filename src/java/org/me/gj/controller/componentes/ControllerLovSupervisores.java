package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.informes.ControllerRepDocVenFacBol;
import org.me.gj.controller.facturacion.mantenimiento.ControllerMesa;
import org.me.gj.controller.facturacion.mantenimiento.ControllerSupervisores;
import org.me.gj.controller.facturacion.mantenimiento.ControllerVendedores;
import org.me.gj.controller.facturacion.mantenimiento.DaoSupervisores;
import org.me.gj.controller.facturacion.procesos.ControllerAnuManDocVen;
import org.me.gj.controller.facturacion.procesos.ControllerAnuRefAcredDocVen;
import org.me.gj.controller.facturacion.procesos.ControllerGenDocVenta;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
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

public class ControllerLovSupervisores extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_supervisor;
    @Wire
    Listbox lst_bussup;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_supdes, txt_supid;
    //Instancias de Objetos 
    DaoSupervisores objDaoSupervisores = new DaoSupervisores();
    ListModelList<Supervisor> objlstSupervisor = new ListModelList<Supervisor>();
    Supervisor objSupervisor = new Supervisor();
    //Variables publicas  
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovSupervisores.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantVendedores")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_supdes");
        } else if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_supdes");
        } else if (parametros.get("validaBusqueda").equals("mantSupervisores")) {
            txt_supid = (Textbox) parametros.get("txt_supidapo");
            txt_supdes = (Textbox) parametros.get("txt_supapenomapo");
        } else if (parametros.get("validaBusqueda").equals("mantMesa")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        } else if (parametros.get("validaBusqueda").equals("AnuMan")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        } else if (parametros.get("validaBusqueda").equals("AnuRef")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        }else if (parametros.get("validaBusqueda").equals("GenDocVenta")) {
            txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        }else if (parametros.get("validaBusqueda").equals("RepFacBol")) {
        	txt_supid = (Textbox) parametros.get("txt_supid");
            txt_supdes = (Textbox) parametros.get("txt_apenom");
        }
        
        
        
    }

    @Listen("onCreate=#w_lov_supervisor")
    public void cargarSupervisores() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantVendedores")) {
            objlstSupervisor = objDaoSupervisores.cargarSupervisores(0);
            lst_bussup.setModel(objlstSupervisor);
        } else if (parametros.get("validaBusqueda").equals("mantSupervisores")) {
            objlstSupervisor = objDaoSupervisores.listaSupervisores(1);
            lst_bussup.setModel(objlstSupervisor);
        } else {
            objlstSupervisor = objDaoSupervisores.listaSupervisores(1);
            lst_bussup.setModel(objlstSupervisor);
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarSupervisor() throws SQLException {
        objlstSupervisor = objDaoSupervisores.busquedaSupervisores(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_bussup.setModel(objlstSupervisor);
    }

    @Listen("onSelect=#lst_bussup")
    public void seleccionaRegistro() throws SQLException {
        objSupervisor = lst_bussup.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantMesa")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("mantSupervisores")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("AnuMan")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("AnuRef")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("GenDocVenta")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else if (parametros.get("validaBusqueda").equals("RepFacBol")) {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        } else {
            txt_supid.setValue(objSupervisor.getSup_id());
            txt_supdes.setValue(objSupervisor.getSup_apenom());
        }
        
        if (controlador.equals("ControllerZona")) {
            ControllerMesa.bandera = false;
        } else if (controlador.equals("ControllerSupervisores")) {
            ControllerSupervisores.bandera = false;
        } else if (controlador.equals("ControllerVendedores")) {
            ControllerVendedores.bandera = false;
        } else if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        } else if (controlador.equals("ControllerAnuManDocVen")) {
        	ControllerAnuManDocVen.bandera = false;
        } else if (controlador.equals("ControllerAnuRefAcredDocVen")) {
            ControllerAnuRefAcredDocVen.bandera = false;
        } else if (controlador.equals("ControllerGenDocVenta")) {
        	ControllerGenDocVenta.bandera = false;
        }else if (controlador.equals("ControllerRepDocVenFacBol")) {
        	ControllerRepDocVenFacBol.bandera = false;
        }
        
        w_lov_supervisor.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_supervisor")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerZona")) {
            ControllerMesa.bandera = false;
        } else if (controlador.equals("ControllerSupervisores")) {
            ControllerMesa.bandera = false;
        } else if (controlador.equals("ControllerVendedores")) {
            ControllerVendedores.bandera = false;
        } else if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        } else if (controlador.equals("ControllerAnuRefAcredDocVen")) {
        	ControllerAnuRefAcredDocVen.bandera = false;
        } else if (controlador.equals("ControllerAnuManDocVen")) {
        	ControllerAnuManDocVen.bandera = false;
        }  else if (controlador.equals("ControllerGenDocVenta")) {
        	ControllerGenDocVenta.bandera = false;
        } else if (controlador.equals("ControllerRepDocVenFacBol")) {
        	ControllerRepDocVenFacBol.bandera = false;
        }
        
        
        
    }
    
}
