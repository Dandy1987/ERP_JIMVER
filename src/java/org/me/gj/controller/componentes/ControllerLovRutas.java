package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoRutas;
import org.me.gj.model.facturacion.mantenimiento.Ruta;
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

public class ControllerLovRutas extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_rutas;
    @Wire
    Listbox lst_rutas;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_rutid, txt_rutdes, txt_ubiid;
    //Instancias de Objetos 
    ListModelList<Ruta> objlstRutas = new ListModelList<Ruta>();
    DaoRutas objDaoRutas = new DaoRutas();
    Ruta objRuta;
    //Variables publicas 
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovRutas.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        if (parametros.get("validaBusqueda").equals("mantZonas")) {
            txt_rutid = (Textbox) parametros.get("txt_rutid");
            txt_rutdes = (Textbox) parametros.get("txt_rutdes");
            txt_ubiid = (Textbox) parametros.get("txt_ubiid");
        }
    }

    @Listen("onCreate=#w_lov_rutas")
    public void cargarRutas() throws SQLException {
        objlstRutas = objDaoRutas.listaRutas(1);
        lst_rutas.setModel(objlstRutas);
    }

    @Listen("onSelect=#lst_rutas")
    public void seleccionaRuta() {
        objRuta = (Ruta) lst_rutas.getSelectedItem().getValue();
        txt_rutid.setValue(objRuta.getRut_id());
        txt_rutdes.setValue("Canal: " + objRuta.getRut_canaldes() + " - Mesa: " + objRuta.getMes_id() + " - Ruta: " + objRuta.getRut_corid());
        txt_ubiid.setDisabled(false);
        txt_ubiid.focus();
        w_lov_rutas.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarRutas() throws SQLException {
        objlstRutas = objDaoRutas.busquedaRutas(6, txt_busqueda.getValue().toUpperCase(), 1);
        lst_rutas.setModel(objlstRutas);
    }
}
