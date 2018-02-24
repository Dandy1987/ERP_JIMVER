package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
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

public class ControllerLovListaPrecioVenta extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_listaprecioventa;
    @Wire
    Listbox lst_listaprecioventa;
    @Wire
    Textbox txt_id_listapre, txt_des_listapre, txt_id_condven;
    //Instancias de Objetos
    ListModelList<ListaPrecio> objListaPrecioVenta = new ListModelList<ListaPrecio>();
    DaoListaPrecios objDaoListaPrecioVenta;
    ListaPrecio objLPrecioVenta;
    //Variables publicas 
    int emp_id, suc_id;
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovListaPrecioVenta.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoListaPrecioVenta = new DaoListaPrecios();
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_id_listapre = (Textbox) parametros.get("txt_id_listapre");
            txt_des_listapre = (Textbox) parametros.get("txt_des_listapre");
            txt_id_condven = (Textbox) parametros.get("txt_id_condven");
        }
        objListaPrecioVenta = objDaoListaPrecioVenta.listaPrecios(emp_id, suc_id, 1, 2);
        lst_listaprecioventa.setModel(objListaPrecioVenta);
    }

    @Listen("onSelect=#lst_listaprecioventa")
    public void seleccionaRegistro() throws SQLException {
        objLPrecioVenta = lst_listaprecioventa.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_id_listapre.setValue(objLPrecioVenta.getLp_id());
            txt_des_listapre.setValue(objLPrecioVenta.getLp_des());
            txt_id_condven.focus();
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        w_lov_listaprecioventa.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_listapreciocompra")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
    }
}
