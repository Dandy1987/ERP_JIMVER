package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoMesa;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
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

public class ControllerLovMesas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_mesas;
    @Wire
    Listbox lst_mesas;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_mesaid;
    Textbox txt_mesades;
    Textbox txt_canid;
    //Instancias de Objetos
    ListModelList<Mesa> objlstMesas = new ListModelList<Mesa>();
    DaoMesa objDaoMesas = new DaoMesa();
    Mesa objMesa = new Mesa();
    //Variables publicas 
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovMesas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        if (parametros.get("validaBusqueda").equals("mantRutas")) {
            txt_mesaid = (Textbox) parametros.get("txt_mesaid");
            txt_mesades = (Textbox) parametros.get("txt_mesades");
            txt_canid = (Textbox) parametros.get("txt_canid");
        }
    }

    @Listen("onCreate=#w_lov_mesas")
    public void cargarMesas() throws SQLException {
        objlstMesas = objDaoMesas.lstMesaxCanal(Integer.parseInt(txt_canid.getValue()));
        lst_mesas.setModel(objlstMesas);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarMesas() throws SQLException {
        if (txt_busqueda.getValue().isEmpty()) {
            Messagebox.show("Por favor Ingrese un Dato", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String busqueda = txt_busqueda.getValue().trim().toUpperCase();
            objlstMesas.clear();
            objlstMesas = objDaoMesas.busquedaLovMesas(busqueda);
            lst_mesas.setModel(objlstMesas);
        }
    }

    @Listen("onSelect=#lst_mesas")
    public void seleccionaRegistro() throws SQLException {
        objMesa = lst_mesas.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantRutas")) {
            txt_mesaid.setValue(objMesa.getMes_id());
            txt_mesades.setValue(objMesa.getSup_apenom());
        }
        w_lov_mesas.detach();
    }
}
