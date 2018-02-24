package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoPaises;
import org.me.gj.model.facturacion.mantenimiento.Pais;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovPaises extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_paises;
    @Wire
    Listbox lst_paises;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_paisid, txt_paisdes;
    Combobox cb_dvisita;
    //Instancias de Objetos 
    ListModelList<Pais> objlstPaises = new ListModelList<Pais>();
    DaoPaises objDaoPaises = new DaoPaises();
    Pais objPais;
    //Variables publicas 
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovPaises.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_paisid = (Textbox) parametros.get("txt_paisid");
            txt_paisdes = (Textbox) parametros.get("txt_paisdes");
            cb_dvisita = (Combobox) parametros.get("cb_dvisita");
        }
    }

    @Listen("onCreate=#w_lov_paises")
    public void cargarPais() throws SQLException {
        objlstPaises = objDaoPaises.listaPaises(1);
        lst_paises.setModel(objlstPaises);
    }

    @Listen("onSelect=#lst_paises")
    public void seleccionaPais() {
        objPais = (Pais) lst_paises.getSelectedItem().getValue();
        txt_paisid.setValue(objPais.getTab_subdir());
        txt_paisdes.setValue(objPais.getTab_subdes());
        cb_dvisita.setDisabled(false);
        cb_dvisita.focus();
        w_lov_paises.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarPais() throws SQLException {
        objlstPaises = objDaoPaises.busquedaPaises(2, txt_busqueda.getValue(), 1);
        lst_paises.setModel(objlstPaises);
    }
}
