package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoDirecciones;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaRecojo;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovDirecciones extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_direccion;
    @Wire
    Listbox lst_direccion;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_cliid, txt_dirdes, txt_zonid;
    Longbox txt_dirid;
    //Instancias de Objetos
    ListModelList<CliDireccion> objlstCliDireccion = new ListModelList<CliDireccion>();
    DaoDirecciones objDaoDirecciones = new DaoDirecciones();
    CliDireccion objCliDireccion;
    //Variables publicas
    int emp_id, suc_id;
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovDirecciones.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_dirid = (Longbox) parametros.get("txt_dirid");
            txt_dirdes = (Textbox) parametros.get("txt_dirdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_zonid = (Textbox) parametros.get("txt_zonid");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_dirid = (Longbox) parametros.get("txt_dirid");
            txt_dirdes = (Textbox) parametros.get("txt_dirdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_zonid = (Textbox) parametros.get("txt_zonid");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_dirid = (Longbox) parametros.get("txt_dirid");
            txt_dirdes = (Textbox) parametros.get("txt_dirdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_zonid = (Textbox) parametros.get("txt_zonid");
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_dirid = (Longbox) parametros.get("txt_dirid");
            txt_dirdes = (Textbox) parametros.get("txt_dirdes");
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_zonid = (Textbox) parametros.get("txt_zonid");
        }
    }

    @Listen("onCreate=#w_lov_direccion")
    public void cargarDirecciones() throws SQLException {
        objlstCliDireccion = objDaoDirecciones.ListaDireccion(txt_cliid.getValue(), emp_id, suc_id);
        lst_direccion.setModel(objlstCliDireccion);
    }

    @Listen("onSelect=#lst_direccion")
    public void seleccionaDirecciones() {
        objCliDireccion = (CliDireccion) lst_direccion.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_dirid.setValue(objCliDireccion.getClidir_id());
            txt_dirdes.setValue(objCliDireccion.getClidir_direc());
            txt_zonid.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_dirid.setValue(objCliDireccion.getClidir_id());
            txt_dirdes.setValue(objCliDireccion.getClidir_direc());
            txt_zonid.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_dirid.setValue(objCliDireccion.getClidir_id());
            txt_dirdes.setValue(objCliDireccion.getClidir_direc());
            txt_zonid.focus();
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_dirid.setValue(objCliDireccion.getClidir_id());
            txt_dirdes.setValue(objCliDireccion.getClidir_direc());
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
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        w_lov_direccion.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarDireccion() throws SQLException {
        objlstCliDireccion = objDaoDirecciones.busquedaDirecciones(txt_cliid.getValue(), txt_busqueda.getValue().toUpperCase());
        lst_direccion.setModel(objlstCliDireccion);
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstCliDireccion = objDaoDirecciones.ListaDireccion(txt_cliid.getValue(), emp_id, suc_id);
            lst_direccion.setModel(objlstCliDireccion);
        }
    }

    @Listen("onClose=#w_lov_direccion")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
    }
}
