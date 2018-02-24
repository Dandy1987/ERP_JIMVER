package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovPreciosCompra extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_buspre;
    @Wire
    Window w_lov_precios;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_proid, txt_prodes, txt_provid, txt_lpcid;
    Doublebox db_desesp;
    //Instancias de Objetos     
    ListModelList<Precio> objlstPrecios;
    DaoPrecios objDaoPrecios;
    Precio objPrecioCompra;
    //Variables publicas 
    int emp_id, suc_id;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovPreciosCompra.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoPrecios = new DaoPrecios();
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_proid = (Textbox) parametros.get("txt_proid");
        txt_prodes = (Textbox) parametros.get("txt_prodes");
        txt_provid = (Textbox) parametros.get("txt_provid");
        txt_lpcid = (Textbox) parametros.get("txt_lpcid");
        db_desesp = (Doublebox) parametros.get("db_des");
        long prov_key = Long.parseLong(txt_provid.getValue());
        int lp_key = Integer.parseInt(txt_lpcid.getValue());
        objlstPrecios = objDaoPrecios.getPreCompxProveedor(emp_id, suc_id, prov_key, lp_key);
        lst_buspre.setModel(objlstPrecios);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarProductos() throws SQLException {
        //String prov_id = txt_provid.getValue();
        //int lpc_id = Integer.parseInt(txt_lpcid.getValue());
        //objlstPrecios = objDaoPrecios.extraerPreciosProductosxLpc(txt_busqueda.getValue().toUpperCase(), prov_id, lpc_id);
        lst_buspre.setModel(objlstPrecios);
    }

    @Listen("onSelect=#lst_buspre")
    public void seleccionaRegistro() throws SQLException {
        objPrecioCompra = lst_buspre.getSelectedItem().getValue();
        txt_proid.setValue(objPrecioCompra.getPro_id());
        txt_prodes.setValue(objPrecioCompra.getPro_des());
        db_desesp.focus();
        w_lov_precios.detach();
    }

}
