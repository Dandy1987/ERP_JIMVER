package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.informes.ControllerFacProLin;
import org.me.gj.controller.logistica.informes.ControllerNotaESLin;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacLin;
import org.me.gj.controller.logistica.informes.ControllerOrdComxLin;
import org.me.gj.controller.logistica.informes.ControllerPedComLin;
import org.me.gj.controller.logistica.mantenimiento.ControllerProductos;
import org.me.gj.controller.logistica.mantenimiento.ControllerSubLineas;
import org.me.gj.controller.logistica.mantenimiento.DaoLineas;
import org.me.gj.controller.logistica.procesos.ControllerOrdComRec;
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

public class ControllerLovLineas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_lineas;
    @Wire
    Textbox txt_busqueda, txt_linid, txt_lindes, txt_prodid;
    @Wire
    Listbox lst_buslin;
    //Instancias de Objetos    
    ListModelList<Lineas> objlstLineas;
    Lineas objLinea;
    DaoLineas objDaoLineas;
    //Variables publicas    
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovLineas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoLineas = new DaoLineas();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
            txt_prodid = (Textbox) parametros.get("txt_prodid");
        } else if (parametros.get("validaBusqueda").equals("PedidoComLin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("OrdComLin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("NotaESLin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacLin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("FacturaLin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        } else if (parametros.get("validaBusqueda").equals("Sublinea")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
        }
    }

    @Listen("onCreate=#w_lov_lineas")
    public void cargarLineas() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            if (txt_prodid.getValue().isEmpty()) {
                objlstLineas = objDaoLineas.listaLineas(1);
            } else {
                objlstLineas = objDaoLineas.listaLineasXProd(1, txt_prodid.getValue());
            }
            lst_buslin.setModel(objlstLineas);
        } else {
            objlstLineas = objDaoLineas.listaLineas(1);
            lst_buslin.setModel(objlstLineas);
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarLineas() throws SQLException {
        if (txt_busqueda.getValue().isEmpty()) {
            Messagebox.show("Por favor Ingrese un Dato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String busqueda = txt_busqueda.getValue().replace("0", "").trim().toUpperCase();
            objlstLineas.clear();
            objlstLineas = objDaoLineas.busquedaLovLineas(busqueda);
            lst_buslin.setModel(objlstLineas);
        }
    }

    @Listen("onSelect=#lst_buslin")
    public void seleccionaRegistro() {
        objLinea = (Lineas) lst_buslin.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("PedidoComLin")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("OrdComLin")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("NotaESLin")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacLin")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("FacturaLin")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("Sublinea")) {
            txt_linid.setValue(objLinea.getTab_subdir());
            txt_lindes.setValue(objLinea.getTab_subdes());
        }
        
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComLin")) {
            ControllerPedComLin.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxLin")) {
            ControllerOrdComxLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESLin")) {
            ControllerNotaESLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacLin")) {
            ControllerNotaESvsFacLin.bandera = false;
        }
        if (controlador.equals("ControllerFacturaLin")) {
            ControllerFacProLin.bandera = false;
        }
        if (controlador.equals("ControllerSubLineas")) {
            ControllerSubLineas.bandera = false;
        }
        w_lov_lineas.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_lineas")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComLin")) {
            ControllerPedComLin.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxLin")) {
            ControllerOrdComxLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESLin")) {
            ControllerNotaESLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacLin")) {
            ControllerNotaESvsFacLin.bandera = false;
        }
        if (controlador.equals("ControllerFacturaLin")) {
            ControllerFacProLin.bandera = false;
        }
        if (controlador.equals("ControllerSubLineas")) {
            ControllerSubLineas.bandera = false;
        }
    }
}
