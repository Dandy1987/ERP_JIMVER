package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.informes.ControllerFacProSubLin;
import org.me.gj.controller.logistica.informes.ControllerNotaESSubLin;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacSubLin;
import org.me.gj.controller.logistica.informes.ControllerOrdComxSublin;
import org.me.gj.controller.logistica.informes.ControllerPedComSubLin;
import org.me.gj.controller.logistica.mantenimiento.ControllerProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoSubLineas;
import org.me.gj.controller.logistica.procesos.ControllerOrdComRec;
import org.me.gj.model.logistica.mantenimiento.SubLineas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
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

public class ControllerLovSublineas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_sublineas;
    @Wire
    Textbox txt_busqueda, txt_linid, txt_sublinid, txt_sublindes;
    @Wire
    Listbox lst_bussublin;
    //Instancias de Objetos
    ListModelList<SubLineas> objlstSubLineas;
    DaoSubLineas objDaoSubLineas;
    SubLineas objSubLinea;
    //Variables publicas  
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovSublineas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoSubLineas = new DaoSubLineas();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("PedidoComSlin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("OrdComSlin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("NotaESSlin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacSublin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        } else if (parametros.get("validaBusqueda").equals("FacturaSlin")) {
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
            txt_sublindes = (Textbox) parametros.get("txt_sublindes");
        }
    }

    @Listen("onCreate=#w_lov_sublineas")
    public void cargarSubLineas() throws SQLException {
        if (txt_linid.getValue().isEmpty()) {
            objlstSubLineas = objDaoSubLineas.listaSublineas("", 0, 1);
        } else {
            //int lin = Integer.parseInt(txt_linid.getValue());
            String lin = txt_linid.getValue();
            objlstSubLineas = objDaoSubLineas.listaSublineas(lin, 3, 1);
        }
        lst_bussublin.setModel(objlstSubLineas);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarSubLineas() throws SQLException {
        if (txt_busqueda.getValue().isEmpty()) {
            Messagebox.show("Por favor ingrese un dato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int lin = Integer.parseInt(txt_linid.getValue());
            String busqueda = txt_busqueda.getValue().trim().toUpperCase();
            objlstSubLineas = objDaoSubLineas.busquedaLovSubLineas(lin, busqueda);
            lst_bussublin.setModel(objlstSubLineas);
        }
    }

    @Listen("onSelect=#lst_bussublin")
    public void seleccionaRegistro() {
        objSubLinea = (SubLineas) lst_bussublin.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("PedidoComSlin")) {
            //txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("OrdComSlin")) {
            //txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("NotaESSlin")) {
            //txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacSublin")) {
            //txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        } else if (parametros.get("validaBusqueda").equals("FacturaSlin")) {
            //txt_linid.setValue(Utilitarios.lpad(objSubLinea.getSlin_codlincad(), 3, "0"));
            txt_sublinid.setValue(objSubLinea.getSlin_cod());
            txt_sublindes.setValue(objSubLinea.getSlin_des());
        }
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComSubLin")) {
            ControllerPedComSubLin.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxSublin")) {
            ControllerOrdComxSublin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESSubLin")) {
            ControllerNotaESSubLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacSubLin")) {
            ControllerNotaESvsFacSubLin.bandera = false;
        }
        if (controlador.equals("ControllerFacturaSubLin")) {
            ControllerFacProSubLin.bandera = false;
        }
        w_lov_sublineas.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_sublineas")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComSubLin")) {
            ControllerPedComSubLin.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxSublin")) {
            ControllerOrdComxSublin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESSubLin")) {
            ControllerNotaESSubLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacSubLin")) {
            ControllerNotaESvsFacSubLin.bandera = false;
        }
        if (controlador.equals("ControllerFacturaSubLin")) {
            ControllerFacProSubLin.bandera = false;
        }
    }
}
