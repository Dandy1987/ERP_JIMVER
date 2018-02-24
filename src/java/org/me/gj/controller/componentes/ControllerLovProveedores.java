package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.ControllerProvPresupuesto;
import org.me.gj.controller.logistica.informes.ControllerFacProProd;
import org.me.gj.controller.logistica.informes.ControllerFacProProv;
import org.me.gj.controller.logistica.informes.ControllerKardex;
import org.me.gj.controller.logistica.informes.ControllerNotaESProd;
import org.me.gj.controller.logistica.informes.ControllerNotaESProv;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacLin;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacProd;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacProv;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacSubLin;
import org.me.gj.controller.logistica.informes.ControllerOrdComxProd;
import org.me.gj.controller.logistica.informes.ControllerOrdComxProv;
import org.me.gj.controller.logistica.informes.ControllerPedComProd;
import org.me.gj.controller.logistica.informes.ControllerPedComProv;
import org.me.gj.controller.logistica.informes.ControllerStockBasico;
import org.me.gj.controller.logistica.mantenimiento.ControllerLPCompra;
import org.me.gj.controller.logistica.mantenimiento.ControllerPreciosCompra;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.procesos.ControllerAdmPedCom;
import org.me.gj.controller.logistica.procesos.ControllerAdmOrdCom;
import org.me.gj.controller.logistica.procesos.ControllerGenFacPro;
import org.me.gj.controller.logistica.procesos.ControllerGenPedCom;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.controller.logistica.procesos.ControllerGenOrdCom;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerInvFisFormato;
import org.me.gj.controller.logistica.procesos.ControllerOrdComRec;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovProveedores extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_proveedor;
    @Wire
    Listbox lst_buspro;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_proid, txt_prodid, txt_proidman, txt_providcab, txt_provdescab;
    Textbox txt_prodes, txt_proddes, txt_prodesman;
    Textbox txt_gcab_provid, txt_gcab_provrazsoc;
    Textbox txt_nic_provid, txt_nic_provdes;
    Textbox txt_oc_conid, txt_oc_condes;
    Textbox txt_provid, txt_provdes;
    Datebox d_oc_fecemi;
    //Instancias de Objetos 
    ListModelList<Proveedores> objlstProveedor = new ListModelList<Proveedores>();
    DaoProveedores objDaoProveedores = new DaoProveedores();
    Proveedores objProveedores = new Proveedores();
    //Variables publicas 
    String controlador;
    String tipo;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovProveedores.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantGenPedCom") || parametros.get("validaBusqueda").equals("LPreCom") || parametros.get("validaBusqueda").equals("manPreCom") || parametros.get("validaBusqueda").equals("PreCom")) {
            txt_proidman = (Textbox) parametros.get("txt_proidman");
            txt_prodesman = (Textbox) parametros.get("txt_prodesman");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_gcab_provid = (Textbox) parametros.get("txt_gcab_provid");
            txt_gcab_provrazsoc = (Textbox) parametros.get("txt_gcab_provrazsoc");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("mantOrdCompMant")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            txt_oc_conid = (Textbox) parametros.get("txt_oc_conid");
            txt_oc_condes = (Textbox) parametros.get("txt_oc_condes");
            d_oc_fecemi = (Datebox) parametros.get("d_oc_fecemi");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("mantOrdComp")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("AdmPedCom")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("GenFactProv")) {
            txt_providcab = (Textbox) parametros.get("txt_providcab");
            txt_provdescab = (Textbox) parametros.get("txt_provdescab");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("PedidoComProv")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("PedidoComProd")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("OrdComProv")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("OrdComProd")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("NotaESProv")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("NotaESProd")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacProd")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("FacturaProv")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("FacturaProd")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("Kardex")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("InvFisFormato")) {
            txt_provid = (Textbox) parametros.get("txt_provid");
            txt_proid = (Textbox) parametros.get("txt_proid");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("GenNotInter")) {
            txt_nic_provid = (Textbox) parametros.get("txt_nic_provid");
            txt_nic_provdes = (Textbox) parametros.get("txt_nic_provdes");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("MantProvPresu")) {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        } else {
            txt_proid = (Textbox) parametros.get("txt_provid");
            txt_prodes = (Textbox) parametros.get("txt_provdes");
            tipo = (String) parametros.get("tipo");
        }
    }

    @Listen("onCreate=#w_lov_proveedor")
    public void cargarProveedores() throws SQLException {
        objlstProveedor = objDaoProveedores.getListaProveedores(tipo);
        lst_buspro.setModel(objlstProveedor);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarProveedor() throws SQLException {
        objlstProveedor = objDaoProveedores.busquedaProveedores(2, txt_busqueda.getValue().toUpperCase(), 1, tipo);
        lst_buspro.setModel(objlstProveedor);
    }

    @Listen("onSelect=#lst_buspro")
    public void seleccionaRegistro() throws SQLException {
        objProveedores = lst_buspro.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantGenPedCom") || parametros.get("validaBusqueda").equals("LPreCom") || parametros.get("validaBusqueda").equals("manPreCom") || parametros.get("validaBusqueda").equals("PreCom")) {
            txt_proidman.setValue(objProveedores.getProv_id());
            txt_prodesman.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_gcab_provid.setValue(objProveedores.getProv_id());
            txt_gcab_provrazsoc.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("mantOrdCompMant")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
            txt_oc_conid.setValue(Utilitarios.lpad(String.valueOf(objProveedores.getCon_key()), 3, "0"));
            txt_oc_condes.setValue(objProveedores.getCon_des());
        } else if (parametros.get("validaBusqueda").equals("mantOrdComp")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("AdmPedCom")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("PedidoComProv")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("Prod")) {
            txt_prodid.setValue(objProveedores.getProv_id());
            txt_proddes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("GenFactProv")) {
            txt_providcab.setValue(objProveedores.getProv_id());
            txt_provdescab.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("PedidoComProd")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("OrdComProv")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("OrdComProd")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaESProv")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaESProd")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacProd")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacProv")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("FacturaProv")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("FacturaProd")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        } else if (parametros.get("validaBusqueda").equals("Kardex")) {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.focus();
        } else if (parametros.get("validaBusqueda").equals("InvFisFormato")) {
            txt_provid.setValue(objProveedores.getProv_id());
            txt_proid = (Textbox) parametros.get("txt_proid");
            tipo = (String) parametros.get("tipo");
        } else if (parametros.get("validaBusqueda").equals("GenNotInter")) {
            txt_nic_provid.setValue(objProveedores.getProv_id());
            txt_nic_provdes.setValue(objProveedores.getProv_razsoc());
        } else {
            txt_proid.setValue(objProveedores.getProv_id());
            txt_prodes.setValue(objProveedores.getProv_razsoc());
        }
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerLPCompra")) {
            ControllerLPCompra.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdComp")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerAproOrdComp")) {
            ControllerAdmOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerAdmPedCom")) {
            ControllerAdmPedCom.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerGenFactProv")) {
            ControllerGenFacPro.bandera = false;
        }
        if (controlador.equals("ControllerNotInter")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProv")) {
            ControllerPedComProv.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProd")) {
            ControllerPedComProd.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProv")) {
            ControllerOrdComxProv.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProd")) {
            ControllerOrdComxProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProv")) {
            ControllerNotaESProv.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProd")) {
            ControllerNotaESProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacProd")) {
            ControllerNotaESvsFacProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacProv")) {
            ControllerNotaESvsFacProv.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacSubLin")) {
            ControllerNotaESvsFacSubLin.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacLin")) {
            ControllerNotaESvsFacLin.bandera = false;
        }
        if (controlador.equals("ControllerFacturaProv")) {
            ControllerFacProProv.bandera = false;
        }
        if (controlador.equals("ControllerFacturaProd")) {
            ControllerFacProProd.bandera = false;
        }
        if (controlador.equals("ControllerStockBasico")) {
            ControllerStockBasico.bandera = false;
        }
        if (controlador.equals("ControllerKardex")) {
            ControllerKardex.bandera = false;
        }
        if (controlador.equals("ControllerInvFisFormato")) {
            ControllerInvFisFormato.bandera = false;
        }
        if (controlador.equals("ControllerProvPresupuesto")) {
            ControllerProvPresupuesto.bandera = false;
        }
        w_lov_proveedor.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_proveedor")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerLPCompra")) {
            ControllerLPCompra.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdComp")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerAproOrdComp")) {
            ControllerAdmOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerAdmPedCom")) {
            ControllerAdmPedCom.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerGenFactProv")) {
            ControllerGenFacPro.bandera = false;
        }
        if (controlador.equals("ControllerNotInter")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProv")) {
            ControllerPedComProv.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProd")) {
            ControllerPedComProd.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProv")) {
            ControllerOrdComxProv.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProd")) {
            ControllerOrdComxProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProv")) {
            ControllerNotaESProv.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProd")) {
            ControllerNotaESProd.bandera = false;
        }
        if (controlador.equals("NotaESvsFacProd")) {
            ControllerNotaESvsFacProd.bandera = false;
        }
        if (controlador.equals("NotaESvsFacProv")) {
            ControllerNotaESvsFacProv.bandera = false;
        }
        if (controlador.equals("NotaESvsFacSubLin")) {
            ControllerNotaESvsFacSubLin.bandera = false;
        }
        if (controlador.equals("NotaESvsFacLin")) {
            ControllerNotaESvsFacLin.bandera = false;
        }

        if (controlador.equals("ControllerFacturaProv")) {
            ControllerFacProProv.bandera = false;
        }
        if (controlador.equals("ControllerFacturaProd")) {
            ControllerFacProProd.bandera = false;
        }
        if (controlador.equals("ControllerStockBasico")) {
            ControllerStockBasico.bandera = false;
        }
        if (controlador.equals("ControllerKardex")) {
            ControllerKardex.bandera = false;
        }
        if (controlador.equals("ControllerInvFisFormato")) {
            ControllerInvFisFormato.bandera = false;
        }
        if (controlador.equals("ControllerProvPresupuesto")) {
            ControllerProvPresupuesto.bandera = false;
        }
    }
}
