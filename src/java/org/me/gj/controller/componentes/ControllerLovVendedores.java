package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.informes.ControllerRepDocVenFacBol;
import org.me.gj.controller.facturacion.mantenimiento.ControllerZona;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.facturacion.procesos.ControllerAnuManDocVen;
import org.me.gj.controller.facturacion.procesos.ControllerAnuRefAcredDocVen;
import org.me.gj.controller.facturacion.procesos.ControllerGenDocVenta;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerProNotasCIR;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
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

public class ControllerLovVendedores extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_vendedores;
    @Wire
    Listbox lst_vendedores;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_vendes, txt_zonnomrep, txt_vendesdet, txt_venid, txt_veniddet, txt_supid, txt_supiddet,
            txt_nr_horid, txt_sup_id, txt_ven_apenom, txt_cli_id, txt_motid;
    //Instancias de Objetos 
    ListModelList<Vendedor> objlstVendedores = new ListModelList<Vendedor>();
    DaoVendedores objDaoVendedores = new DaoVendedores();
    Vendedor objVendedor;
    //Variables publicas 
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovVendedores.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("NotaCambioAuto")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojoAuto")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioAuto")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
            txt_motid = (Textbox) parametros.get("txt_motid");
        } else if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vendes");
            txt_zonnomrep = (Textbox) parametros.get("txt_zonnomrep");
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vendes");
        } else if (parametros.get("validaBusqueda").equals("AnuRef")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vendes");
        }   else if (parametros.get("validaBusqueda").equals("GenDocVenta")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vennom");
        } else if (parametros.get("validaBusqueda").equals("AnuRef")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vendes");
        } else if (parametros.get("validaBusqueda").equals("RepFacBol")) {
            txt_venid = (Textbox) parametros.get("txt_venid");
            txt_vendes = (Textbox) parametros.get("txt_vendes");
        }
    }

    @Listen("onCreate=#w_lov_vendedores")
    public void cargarVendedor() throws SQLException {
        objlstVendedores = objDaoVendedores.listaVendedores(1);
        lst_vendedores.setModel(objlstVendedores);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarVendedor() throws SQLException {
        objlstVendedores = objDaoVendedores.busquedaVendedores(8, txt_busqueda.getValue().toUpperCase(), 1);
        lst_vendedores.setModel(objlstVendedores);
    }

    @Listen("onSelect=#lst_vendedores")
    public void seleccionaVendedor() {
        objVendedor = (Vendedor) lst_vendedores.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_sup_id.setValue(objVendedor.getSup_id());
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_sup_id.setValue(objVendedor.getSup_id());
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_sup_id.setValue(objVendedor.getSup_id());
        } else if (parametros.get("validaBusqueda").equals("NotaCambioAuto")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_cli_id.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaRecojoAuto")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_cli_id.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioAuto")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_cli_id.focus();
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_motid.focus();
        } else if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
            txt_zonnomrep.focus();
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
        }else if (parametros.get("validaBusqueda").equals("AnuMan")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
        } else if (parametros.get("validaBusqueda").equals("AnuRef")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
        }else if (parametros.get("validaBusqueda").equals("GenDocVenta")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
        }else if (parametros.get("validaBusqueda").equals("RepFacBol")) {
            txt_venid.setValue(objVendedor.getVen_id());
            txt_vendes.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
        }
        
        
        
        if (controlador.equals("ControllerZona")) {
            ControllerZona.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambioAuto")) {
            ControllerAdmNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojoAuto")) {
            ControllerAdmNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambioAuto")) {
            ControllerAdmNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambioAuto")) {
            ControllerAdmNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        } 
        if (controlador.equals("ControllerAnuManDocVen")) {
        	ControllerAnuManDocVen.bandera = false;
        }
        if (controlador.equals("ControllerAnuRefAcredDocVen")) {
        	ControllerAnuRefAcredDocVen.bandera = false;
        }
        if (controlador.equals("ControllerGenDocVenta")) {
        	ControllerGenDocVenta.bandera = false;
        }if (controlador.equals("ControllerRepDocVenFacBol")) {
        	ControllerRepDocVenFacBol.bandera = false;
        }
        
        w_lov_vendedores.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_vendedores")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerZona")) {
            ControllerZona.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambioAuto")) {
            ControllerAdmNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojoAuto")) {
            ControllerAdmNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambioAuto")) {
            ControllerAdmNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        if (controlador.equals("ControllerAnuManDocVen")) {
        	ControllerAnuManDocVen.bandera = false;
        }
        if (controlador.equals("ControllerAnuRefAcredDocVen")) {
        	ControllerAnuRefAcredDocVen.bandera = false;
        }
        if (controlador.equals("ControllerGenDocVenta")) {
        	ControllerGenDocVenta.bandera = false;
        }if (controlador.equals("ControllerRepDocVenFacBol")) {
        	ControllerRepDocVenFacBol.bandera = false;
        }
    }
}
