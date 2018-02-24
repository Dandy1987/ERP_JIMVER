package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
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

public class ControllerLovCondicionesVenta extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_condiciones_venta;
    @Wire
    Listbox lst_condiciones_venta;    
    @Wire
    Textbox txt_busqueda;
    @Wire
    Textbox txt_id_condven, txt_des_condven, txt_diaplazo;
    //Instancias de Objetos
    ListModelList<Condicion> objLstCondicionVenta = new ListModelList<Condicion>();
    DaoCondicion objDaoCondicionVenta = new DaoCondicion();
    Condicion objCondicionVenta = new Condicion();
    //Variables publicas
    int emp_id, suc_id;
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovCondicionesVenta.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_id_condven = (Textbox) parametros.get("txt_id_condven");
            txt_des_condven = (Textbox) parametros.get("txt_des_condven");
            txt_diaplazo = (Textbox) parametros.get("txt_diaplazo");
        }
    }

    @Listen("onCreate=#w_lov_condiciones_venta")
    public void cargarCondicionesVenta() throws SQLException {
        objLstCondicionVenta = objDaoCondicionVenta.lstCondicion("V", 1);
        lst_condiciones_venta.setModel(objLstCondicionVenta);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarCondicionVenta() throws SQLException {
        objLstCondicionVenta = objDaoCondicionVenta.busqueda(2, txt_busqueda.getValue().toUpperCase(), 1, "V");
        lst_condiciones_venta.setModel(objLstCondicionVenta);
    }

    @Listen("onSelect=#lst_condiciones_venta")
    public void seleccionaRegistro() throws SQLException {
        objCondicionVenta = lst_condiciones_venta.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_id_condven.setValue(objCondicionVenta.getConId());
            txt_des_condven.setValue(objCondicionVenta.getConDes());
            txt_diaplazo.setValue(String.valueOf(objCondicionVenta.getConDias()));
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        w_lov_condiciones_venta.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_condiciones_venta")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
    }
}
