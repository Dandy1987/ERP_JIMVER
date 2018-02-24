package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerPreciosCompra;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.procesos.ControllerGenPedCom;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
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

public class ControllerLovListaPrecioCompra extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_listapreciocompra;
    @Wire
    Window w_lov_listapreciocompra;
    @Wire
    Textbox txt_busqueda, txt_lisprecom, txt_lisprecomdes, txt_provid, txt_linid, txt_lindes,
            txt_providman, txt_linidman, txt_lindesman, txt_nic_lpcid, txt_nic_lpcdes, txt_nic_provid, txt_cli_id;
    Combobox cb_moneda;
    //Instancias de Objetos
    ListModelList<ListaPrecio> objListaPrecioCompra = new ListModelList<ListaPrecio>();
    DaoListaPrecios objDaoListaPrecioCompra;
    ListaPrecio objLPrecioCompra;
    //Variables publicas
    int emp_id, suc_id;
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovListaPrecioCompra.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoListaPrecioCompra = new DaoListaPrecios();
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        long prov_key = 0;
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            txt_provid = (Textbox) parametros.get("txt_proidman");
            txt_lisprecom = (Textbox) parametros.get("txt_lisprecom");
            txt_lisprecomdes = (Textbox) parametros.get("txt_lisprecomdes");
            cb_moneda = (Combobox) parametros.get("cb_moneda");
            prov_key = Long.parseLong(txt_provid.getValue());
        } else if (parametros.get("validaBusqueda").equals("mantOrdCompMant")) {
            txt_provid = (Textbox) parametros.get("txt_proidman");
            txt_lisprecom = (Textbox) parametros.get("txt_lisprecom");
            txt_lisprecomdes = (Textbox) parametros.get("txt_lisprecomdes");
            cb_moneda = (Combobox) parametros.get("cb_moneda");
            prov_key = Long.parseLong(txt_provid.getValue());
        } else if (parametros.get("validaBusqueda").equals("PreCom")) {
            txt_provid = (Textbox) parametros.get("txt_provid");
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_lindes = (Textbox) parametros.get("txt_lindes");
            prov_key = Long.parseLong(txt_provid.getValue());
        } else if (parametros.get("validaBusqueda").equals("manPreCom")) {
            txt_providman = (Textbox) parametros.get("txt_providman");
            txt_linidman = (Textbox) parametros.get("txt_linidman");
            txt_lindesman = (Textbox) parametros.get("txt_lindesman");
            prov_key = Long.parseLong(txt_providman.getValue());
        } else if (parametros.get("validaBusqueda").equals("GenNotInter")) {
            txt_nic_provid = (Textbox) parametros.get("txt_nic_provid");
            txt_nic_lpcid = (Textbox) parametros.get("txt_nic_lpcid");
            txt_nic_lpcdes = (Textbox) parametros.get("txt_nic_lpcdes");
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
            prov_key = Long.parseLong(txt_nic_provid.getValue());
        }

        objListaPrecioCompra = objDaoListaPrecioCompra.getListaPreCompxProv(emp_id, suc_id, prov_key);
        lst_listapreciocompra.setModel(objListaPrecioCompra);
    }

    @Listen("onSelect=#lst_listapreciocompra")
    public void seleccionaRegistro() throws SQLException {
        objLPrecioCompra = lst_listapreciocompra.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            txt_lisprecom.setValue(objLPrecioCompra.getLp_id());
            txt_lisprecomdes.setValue(objLPrecioCompra.getLp_des());
        } else if (parametros.get("validaBusqueda").equals("mantOrdCompMant")) {
            txt_lisprecom.setValue(objLPrecioCompra.getLp_id());
            txt_lisprecomdes.setValue(objLPrecioCompra.getLp_des());
        } else if (parametros.get("validaBusqueda").equals("PreCom")) {
            txt_linid.setValue(objLPrecioCompra.getLp_id());
            txt_lindes.setValue(objLPrecioCompra.getLp_des());
        } else if (parametros.get("validaBusqueda").equals("manPreCom")) {
            txt_linidman.setValue(objLPrecioCompra.getLp_id());
            txt_lindesman.setValue(objLPrecioCompra.getLp_des());
        } else if (parametros.get("validaBusqueda").equals("GenNotInter")) {
            txt_nic_lpcid.setValue(objLPrecioCompra.getLp_id());
            txt_nic_lpcdes.setValue(objLPrecioCompra.getLp_des());
            txt_cli_id.focus();
        }
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }

        w_lov_listapreciocompra.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_listapreciocompra")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }

        /* if (controlador.equals("ControllerRepPercepciones")) {
         ControllerRepPercepciones.bandera = false;
         }
         if (controlador.equals("ControllerReciboNcAut")) {
         ControllerReciboNcAut.bandera = false;
         }
         if (controlador.equals("ControllerReciboNcRes")) {
         ControllerReciboNcRes.bandera = false;
         }
         if (controlador.equals("ControllerMonitoreo")) {
         ControllerMonitoreo.bandera = false;
         }
         if (controlador.equals("ControllerResumenVtas")) {
         ControllerResumenVtas.bandera = false;
         }
         if (controlador.equals("ControllerAvanceVtas")) {
         ControllerAvanceVtas.bandera = false;
         }
         if (controlador.equals("ControllerLovFaltPlanilla")) {
         ControllerLovFaltPlanilla.bandera = false;
         }
         if (controlador.equals("ControllerLovFaltVendedor")) {
         ControllerLovFaltVendedor.bandera = false;
         }*/
    }
}
