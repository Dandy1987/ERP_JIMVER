package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovAlmacenes extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_almacenes;
    @Wire
    Listbox lst_busalm;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_proid, txt_prodid, txt_proidman, txt_providcab, txt_provdescab;
    Textbox txt_prodes, txt_proddes, txt_prodesman;
    Textbox txt_gcab_provid, txt_gcab_provrazsoc;
    Intbox txt_almidbus;
    Textbox txt_almdesbus;
    Textbox txt_oc_conid, txt_oc_condes;
    Datebox d_oc_fecemi;
    //Instancias de Objetos
    ListModelList<Almacenes> objlstAlmacenes = new ListModelList<Almacenes>();
    DaoAlmacenes objDaoAlmacenes = new DaoAlmacenes();
    Almacenes objAlmacenes = new Almacenes();
    //Variables publicas
    Map parametros;
    String tipo;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovAlmacenes.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        if (parametros.get("validaBusqueda").equals("mantInvFis")) {
            txt_almidbus = (Intbox) parametros.get("txt_almid");
            txt_almdesbus = (Textbox) parametros.get("txt_almdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_almidbus = (Intbox) parametros.get("txt_almid");
            txt_almdesbus = (Textbox) parametros.get("txt_almdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("Costos")) {
            txt_almidbus = (Intbox) parametros.get("txt_almid");
            txt_almdesbus = (Textbox) parametros.get("txt_almdes");
            tipo = (String) parametros.get("tipo");
        } else {
            txt_almidbus = (Intbox) parametros.get("txt_almid");
            txt_almdesbus = (Textbox) parametros.get("txt_almdes");
            tipo = (String) parametros.get("tipo");
        }
    }

    @Listen("onCreate=#w_lov_almacenes")
    public void cargarAlmacenes() throws SQLException {
        objlstAlmacenes = objDaoAlmacenes.lstAlmacenes(1);
        lst_busalm.setModel(objlstAlmacenes);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarAlmacenes() throws SQLException {
        objlstAlmacenes = objDaoAlmacenes.busquedaAlmacenes(txt_busqueda.getValue().toUpperCase(), 2, Integer.parseInt(tipo));
        lst_busalm.setModel(objlstAlmacenes);
    }

    @Listen("onSelect=#lst_busalm")
    public void seleccionaRegistro() throws SQLException {
        objAlmacenes = lst_busalm.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantInvFis")) {
            txt_almidbus.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
            txt_almdesbus.setValue(objAlmacenes.getAlm_des());
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_almidbus.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
            txt_almdesbus.setValue(objAlmacenes.getAlm_des());
        } else if (parametros.get("validaBusqueda").equals("Costos")) {
            txt_almidbus.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
            txt_almdesbus.setValue(objAlmacenes.getAlm_des());
        }
        w_lov_almacenes.detach();
    }
}
