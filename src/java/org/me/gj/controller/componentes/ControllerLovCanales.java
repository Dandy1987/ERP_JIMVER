package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoCanal;
import org.me.gj.model.facturacion.mantenimiento.Canal;
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

public class ControllerLovCanales extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_buscan;
    @Wire
    Window w_lov_canales;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_canid;
    Textbox txt_candes;
    Textbox txt_canalid;
    //Instancias de Objetos
    ListModelList<Canal> objlstCanal = new ListModelList<Canal>();
    DaoCanal objDaoCanal = new DaoCanal();
    Canal objCanal = new Canal();
    //Variables publicas
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovCanales.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        if (parametros.get("validaBusqueda").equals("mantRutas")) {
            txt_canalid = (Textbox) parametros.get("txt_canid");
            txt_candes = (Textbox) parametros.get("txt_candes");
        } else if (parametros.get("validaBusqueda").equals("mantVendedores")) {
            txt_canid = (Textbox) parametros.get("txt_tabid");
            txt_candes = (Textbox) parametros.get("txt_tabdes");
        }
    }

    @Listen("onCreate=#w_lov_canales")
    public void cargarCanales() throws SQLException {
        objlstCanal = objDaoCanal.listaCanales(1);
        lst_buscan.setModel(objlstCanal);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarCanales() throws SQLException {
        objlstCanal = objDaoCanal.busquedaCanales(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_buscan.setModel(objlstCanal);
    }

    @Listen("onSelect=#lst_buscan")
    public void seleccionaRegistro() throws SQLException {
        objCanal = lst_buscan.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantRutas")) {
            txt_canalid.setValue(objCanal.getTab_subdir());
            txt_candes.setValue(objCanal.getTab_subdes());
        } else if (parametros.get("validaBusqueda").equals("mantVendedores")) {
            txt_canid.setValue(objCanal.getTab_subdir());
            txt_candes.setValue(objCanal.getTab_subdes());
        }
        w_lov_canales.detach();
    }

}
