package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoCondicion;
import org.me.gj.model.logistica.mantenimiento.Condicion;
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

public class ControllerLovCondicionesCompra extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_condiciones_compra;
    @Wire
    Window w_lov_condiciones_compra;
    @Wire
    Textbox txt_busqueda;
    //Instancias de Objetos
    ListModelList<Condicion> objLstCondicionCompra = new ListModelList<Condicion>();
    DaoCondicion objDaoCondicionCompra = new DaoCondicion();
    Condicion objCondicionCompra = new Condicion();
    //Variables publicas
    Textbox txt_forid;
    Textbox txt_fordes;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovCondicionesCompra.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_forid = (Textbox) parametros.get("txt_forid");
        txt_fordes = (Textbox) parametros.get("txt_fordes");
    }

    @Listen("onCreate=#w_lov_condiciones_compra")
    public void cargarCondicionesCompra() throws SQLException {
        objLstCondicionCompra = objDaoCondicionCompra.lstCondicion("C", 1);
        lst_condiciones_compra.setModel(objLstCondicionCompra);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarCondicionCompra() throws SQLException {
        objLstCondicionCompra = objDaoCondicionCompra.busqueda(2, txt_busqueda.getValue().toUpperCase(), 1, "C");
        lst_condiciones_compra.setModel(objLstCondicionCompra);
    }

    @Listen("onSelect=#lst_condiciones_compra")
    public void seleccionaRegistro() throws SQLException {
        objCondicionCompra = lst_condiciones_compra.getSelectedItem().getValue();
        txt_forid.setValue(objCondicionCompra.getConId());
        txt_fordes.setValue(objCondicionCompra.getConDes());
        w_lov_condiciones_compra.detach();
    }
}
