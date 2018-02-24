package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoProvPresupuesto;
import org.me.gj.controller.logistica.mantenimiento.ControllerProductos;
import org.me.gj.model.facturacion.mantenimiento.ProvPresupuesto;
import org.me.gj.model.logistica.mantenimiento.Lineas;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovProvPresupuesto extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_provpresu;
    @Wire
    Window w_lov_provpresu;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_provpresuid;
    Textbox txt_provpresudes;
    Textbox txt_provid;
    
    //Instancias de Objetos
    ListModelList<ProvPresupuesto> objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
    DaoProvPresupuesto objDaoProvPresupuesto = new DaoProvPresupuesto();
    ProvPresupuesto objProvPresupuesto = new ProvPresupuesto();
    
    //Variables publicas
    String tipo , controlador;
    Map parametros;
    
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovProvPresupuesto.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ManPro")) {
        	txt_provpresuid = (Textbox) parametros.get("txt_provpresuid");
        	txt_provpresudes = (Textbox) parametros.get("txt_provpresudes");
        	txt_provid = (Textbox) parametros.get("txt_provid");
        }
    }

    @Listen("onCreate=#w_lov_provpresu")
    public void cargarProvPresupuestoes() throws SQLException {
        objlstProvPresupuesto = objDaoProvPresupuesto.listaProvPresuxProv(txt_provid.getValue());
        lst_provpresu.setModel(objlstProvPresupuesto);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarProvPresupuestoes() throws SQLException {
    	if (txt_busqueda.getValue().isEmpty()) {
            Messagebox.show("Por favor Ingrese un Dato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstProvPresupuesto.clear();
            objlstProvPresupuesto = objDaoProvPresupuesto.busquedaProvPresuxProv(txt_provid.getValue(), txt_busqueda.getValue(), 1);
            lst_provpresu.setModel(objlstProvPresupuesto);
        }
    }

    @Listen("onSelect=#lst_provpresu")
    public void seleccionaRegistro() throws SQLException {
        objProvPresupuesto =  (ProvPresupuesto) lst_provpresu.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("ManPro")) {
        	txt_provpresuid.setValue(objProvPresupuesto.getProvpresu_id());
        	txt_provpresudes.setValue(objProvPresupuesto.getProvpresu_desabre());
        }
        
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        
        w_lov_provpresu.detach();
    }
    
    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_provpresu") 
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
    }    
}
