package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoMarcas;
import org.me.gj.model.logistica.mantenimiento.TtabGen;
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

public class ControllerLovMarcas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_marcas;
    @Wire
    Textbox txt_busqueda, txt_codmarca, txt_desmarca;
    @Wire
    Listbox lst_busmar;
    //Instancias de Objetos    
    ListModelList<TtabGen> objlstMarcas;
    TtabGen objMarcas;
    DaoMarcas objDaoMarcas;
    //Variables publicas    
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovMarcas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoMarcas = new DaoMarcas();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_codmarca = (Textbox) parametros.get("txt_codmarca");
            txt_desmarca = (Textbox) parametros.get("txt_desmarca");
        }
    }

    @Listen("onCreate=#w_lov_marcas")
    public void cargarLineas() throws SQLException {
        objlstMarcas = objDaoMarcas.lstMarcas("1");
        lst_busmar.setModel(objlstMarcas);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarMarcas() throws SQLException {
        if (txt_busqueda.getValue().isEmpty()) {
            Messagebox.show("Por favor Ingrese un Dato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String busqueda = txt_busqueda.getValue().replace("0", "").trim().toUpperCase();
            objlstMarcas.clear();
            objlstMarcas = objDaoMarcas.busquedaLovMarcas(busqueda);
            lst_busmar.setModel(objlstMarcas);
        }
    }

    @Listen("onSelect=#lst_busmar")
    public void seleccionaRegistro() {
        objMarcas = (TtabGen) lst_busmar.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("ManPro")) {
            txt_codmarca.setValue(objMarcas.getTab_subdir());
            txt_desmarca.setValue(objMarcas.getTab_subdes());
        }
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
        w_lov_marcas.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_marcas")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProductos")) {
            ControllerProductos.bandera = false;
        }
    }
}
