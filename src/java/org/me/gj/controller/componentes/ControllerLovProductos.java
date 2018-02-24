package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.controller.logistica.informes.ControllerFacProProd;
import org.me.gj.controller.logistica.informes.ControllerKardex;
import org.me.gj.controller.logistica.informes.ControllerNotaESProd;
import org.me.gj.controller.logistica.informes.ControllerNotaESvsFacProd;
import org.me.gj.controller.logistica.informes.ControllerOrdComxProd;
import org.me.gj.controller.logistica.informes.ControllerPedComProd;
import org.me.gj.controller.logistica.informes.ControllerStockBasico;
import org.me.gj.controller.logistica.mantenimiento.ControllerPreciosCompra;
import org.me.gj.controller.logistica.mantenimiento.ControllerPreciosVenta;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.procesos.ControllerGenPedCom;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerGenOrdCom;
import org.me.gj.controller.logistica.procesos.ControllerInvFisFormato;
import org.me.gj.controller.logistica.procesos.ControllerOrdComRec;
import org.me.gj.controller.logistica.procesos.DaoOrdCom;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovProductos extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_producto;
    @Wire
    Listbox lst_productos;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_proidman, txt_prodesman, txt_linid, txt_sublinid, txt_prodid, txt_proddes, txt_prodesdes, txt_umanejo, txt_provid, txt_prodesmandes, txt_listapreid,
            txt_proveedor, txt_ubides, txt_proid, txt_prodes, txt_nescab_ocnropedkey, txt_proconv,
            txt_nid_prodesent, txt_nid_proident, txt_nid_proidsal, txt_nid_prodessal;
    Intbox txt_upresentacion, txt_prokey, txt_nrd_tipdoc, txt_undpre, txt_unimas;
    Doublebox txt_canent, txt_ncd_entero, txt_nrd_entero, txt_nid_enterosal, txt_nid_enteroent;
    //Instancias de Objetos
    ListModelList<Productos> objlstProductos = new ListModelList<Productos>();
    Productos objProductos = new Productos();
    DaoProductos objDaoProductos = new DaoProductos();
    DaoOrdCom objDaoOrdCom = new DaoOrdCom();
    //Variables publicas 
    String proveedor = "";
    String orden = "";
    String listaprecio;
    String controlador;
    String ordenCompra = "";
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovProductos.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_proidman = (Textbox) parametros.get("txt_proidman");
            txt_prokey = (Intbox) parametros.get("txt_prokey");
            txt_prodesman = (Textbox) parametros.get("txt_prodesman");
            txt_umanejo = (Textbox) parametros.get("txt_umanejo");
            txt_upresentacion = (Intbox) parametros.get("txt_upresentacion");
        } else if (parametros.get("validaBusqueda").equals("manPreCom")) {
            txt_proidman = (Textbox) parametros.get("txt_proidman");
            txt_prodesman = (Textbox) parametros.get("txt_prodesman");
            proveedor = (String) parametros.get("proveedor");
        } else if (parametros.get("validaBusqueda").equals("mantOrdComp")) {
            txt_proidman = (Textbox) parametros.get("txt_proidman");
            txt_prodesman = (Textbox) parametros.get("txt_prodesman");
            proveedor = (String) parametros.get("proveedor");
        } else if (parametros.get("validaBusqueda").equals("PreCom")) {
            txt_prodid = (Textbox) parametros.get("txt_prodid");
            txt_proddes = (Textbox) parametros.get("txt_proddes");
            proveedor = (String) parametros.get("proveedor").toString();
            txt_provid = (Textbox) parametros.get("txt_provid");
            txt_linid = (Textbox) parametros.get("txt_linid");
        } else if (parametros.get("validaBusqueda").equals("PreVen")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = "";
        } else if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            proveedor = (String) parametros.get("proveedor");
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_proidman = (Textbox) parametros.get("txt_proidman");
            txt_prodesman = (Textbox) parametros.get("txt_prodesman");
            txt_provid = (Textbox) parametros.get("txt_provid");
            txt_linid = (Textbox) parametros.get("txt_linid");
            txt_sublinid = (Textbox) parametros.get("txt_sublinid");
        } else if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            txt_ncd_entero = (Doublebox) parametros.get("txt_ncd_entero");
            proveedor = "";
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            txt_nrd_entero = (Doublebox) parametros.get("txt_nrd_entero");
            proveedor = "";
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioS")) {
            txt_nid_proidsal = (Textbox) parametros.get("txt_nid_proidsal");
            txt_nid_prodessal = (Textbox) parametros.get("txt_nid_prodessal");
            txt_nid_enterosal = (Doublebox) parametros.get("txt_nid_enterosal");
            proveedor = (String) parametros.get("proveedor");
            listaprecio = (String) parametros.get("listaprecio");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioE")) {
            txt_nid_proident = (Textbox) parametros.get("txt_nid_proident");
            txt_nid_prodesent = (Textbox) parametros.get("txt_nid_prodesent");
            txt_nid_enteroent = (Doublebox) parametros.get("txt_nid_enteroent");
            proveedor = (String) parametros.get("proveedor");
            listaprecio = (String) parametros.get("listaprecio");
        } else if (parametros.get("validaBusqueda").equals("mantNotaESOrden")) {
            orden = "";
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            txt_listapreid = (Textbox) parametros.get("txt_listapreid");
            txt_nescab_ocnropedkey = (Textbox) parametros.get("txt_nescab_ocnropedkey");
        } else if (parametros.get("validaBusqueda").equals("mantNotaES")) {
            proveedor = "";
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prokey = (Intbox) parametros.get("txt_prokey");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            txt_prodesdes = (Textbox) parametros.get("txt_prodesdes");
            txt_undpre = (Intbox) parametros.get("txt_undpre");
            txt_unimas = (Intbox) parametros.get("txt_unimas");
            txt_proconv = (Textbox) parametros.get("txt_proconv");
            txt_provid = (Textbox) parametros.get("txt_provid");
            txt_proveedor = (Textbox) parametros.get("txt_proveedor");
            txt_canent = (Doublebox) parametros.get("txt_canent");
            txt_ubides = (Textbox) parametros.get("txt_ubides");
        } else if (parametros.get("validaBusqueda").equals("PedidoComProd")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("OrdComProd")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("NotaESProd")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacProd")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("FacturaProd")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("Kardexp")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("Kardexf")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("InvFisFormato")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = (String) parametros.get("proveedor").toString();
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = "";
        } else if (parametros.get("validaBusqueda").equals("Costos")) {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = "";
        } else {
            txt_proid = (Textbox) parametros.get("txt_proid");
            txt_prodes = (Textbox) parametros.get("txt_prodes");
            proveedor = "";
        }
    }

    @Listen("onCreate=#w_lov_producto")
    public void cargarProductos() throws SQLException {
        if (!"".equals(proveedor)) {
            objlstProductos = objDaoProductos.cargarProductosXProveedor(proveedor);
        } else {
            objlstProductos = objDaoProductos.lstProductos(1);
        }
        if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            String producto = txt_proidman.getValue().isEmpty() ? "%%" : txt_proidman.getValue();
            String prov = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
            String linea = txt_linid.getValue().isEmpty() ? "%%" : txt_linid.getValue();
            String sublinea = txt_sublinid.getValue().isEmpty() ? "%%" : txt_sublinid.getValue();
            objlstProductos = objDaoProductos.buscarProductoxTodo(producto, prov, linea, sublinea, "c");
        }
        if (parametros.get("validaBusqueda").equals("PreCom")) {
            objlstProductos = objDaoProductos.cargarProductosXProveedorListaPrecio(txt_provid.getValue(), txt_linid.getValue());
        }
        if (parametros.get("validaBusqueda").equals("mantNotaESOrden")) {
            listaprecio = txt_listapreid.getValue().isEmpty() ? "%%" : txt_listapreid.getValue();
            objlstProductos = objDaoProductos.cargarProductosXOrdenCompra(txt_nescab_ocnropedkey.getValue(), listaprecio);
        }
        if (parametros.get("validaBusqueda").equals("NotaIntercambioE") || parametros.get("validaBusqueda").equals("NotaIntercambioS")) {
            objlstProductos = objDaoProductos.cargarProductosXProveedor(proveedor);
        }
        lst_productos.setModel(objlstProductos);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarProducto() throws SQLException {
        if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            String prov = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
            String linea = txt_linid.getValue().isEmpty() ? "%%" : txt_linid.getValue();
            String sublinea = txt_sublinid.getValue().isEmpty() ? "%%" : txt_sublinid.getValue();
            objlstProductos = objDaoProductos.buscarProductoxTodo(txt_busqueda.getValue().toUpperCase(), prov, linea, sublinea, "b");
            lst_productos.setModel(objlstProductos);
        } else {
            if (!proveedor.isEmpty()) {
                objlstProductos = objDaoProductos.buscarProductoxProveedor(txt_busqueda.getValue().toUpperCase(), proveedor);
                lst_productos.setModel(objlstProductos);
            } else {
                objlstProductos = objDaoProductos.buscarProductoxProveedor(txt_busqueda.getValue().toUpperCase(), proveedor);
                lst_productos.setModel(objlstProductos);
            }
        }
        if (parametros.get("validaBusqueda").equals("mantNotaESOrden")) {
            objlstProductos = objDaoProductos.cargarProductosXOrdenCompra(txt_nescab_ocnropedkey.getValue(), txt_listapreid.getValue());
            lst_productos.setModel(objlstProductos);
        }
    }

    @Listen("onSelect=#lst_productos")
    public void seleccionaRegistro() throws SQLException {
        objProductos = lst_productos.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantNotaCambio")) {
            txt_prokey.setValue(Integer.valueOf(objProductos.getPro_key()));
            txt_umanejo.setValue(objProductos.getPro_unimanven());
            txt_upresentacion.setValue(Integer.valueOf(objProductos.getPro_presminven()));
        } else if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
            txt_ncd_entero.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
            txt_nrd_entero.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioE")) {
            txt_nid_proident.setValue(objProductos.getPro_id());
            txt_nid_prodesent.setValue(objProductos.getPro_des());
            txt_nid_enteroent.focus();
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioS")) {
            txt_nid_proidsal.setValue(objProductos.getPro_id());
            txt_nid_prodessal.setValue(objProductos.getPro_des());
            txt_nid_enterosal.focus();
        } else if (parametros.get("validaBusqueda").equals("mantGenPedCom")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("mantProcOrdCompRec")) {
            txt_proidman.setValue(objProductos.getPro_id());
            txt_prodesman.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("PreCom")) {
            txt_prodid.setValue(objProductos.getPro_id());
            txt_proddes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("manPreCom")) {
            txt_proidman.setValue(objProductos.getPro_id());
            txt_prodesman.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("PreVen")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("mantNotaESOrden")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("mantNotaES")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prokey.setValue(Integer.valueOf(objProductos.getPro_key()));
            txt_prodes.setValue(objProductos.getPro_des());
            txt_prodesdes.setValue(objProductos.getPro_desdes());
            txt_undpre.setValue(Integer.valueOf(objProductos.getPro_presminven()));
            txt_unimas.setValue(objProductos.getPro_unimas());
            txt_proconv.setValue(objProductos.getPro_conv());
            txt_provid.setValue(objProductos.getPro_provid());
            txt_proveedor.setValue(objProductos.getPro_sigprov());
            //txt_ubides.setValue(String.valueOf(objProductos.getPro_ubi()));
            //txt_canent.focus();
        } else if (parametros.get("validaBusqueda").equals("PedidoComProd")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("OrdComProd")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("NotaESProd")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("NotaESvsFacProd")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("FacturaProd")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("StockBasico")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("Kardexp")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("Kardexf")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("InvFisFormato")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else if (parametros.get("validaBusqueda").equals("Costos")) {
            txt_proid.setValue(objProductos.getPro_id());
            txt_prodes.setValue(objProductos.getPro_des());
        } else {
            txt_proidman.setValue(objProductos.getPro_id());
            txt_prodesman.setValue(objProductos.getPro_des());
        }
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerPreciosVenta")) {
            ControllerPreciosVenta.bandera = false;
        }
        if (controlador.equals("ControllerOrdCom")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdComRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProd")) {
            ControllerPedComProd.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProd")) {
            ControllerOrdComxProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProd")) {
            ControllerNotaESProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacProd")) {
            ControllerNotaESvsFacProd.bandera = false;
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
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        if (controlador.equals("ControllerCostos")) {
            ControllerGenPedVen.bandera = false;
        }
        w_lov_producto.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_producto")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPreciosCompra")) {
            ControllerPreciosCompra.bandera = false;
        }
        if (controlador.equals("ControllerPreciosVenta")) {
            ControllerPreciosVenta.bandera = false;
        }
        if (controlador.equals("ControllerOrdComp")) {
            ControllerGenOrdCom.bandera = false;
        }
        if (controlador.equals("ControllerGenPedCom")) {
            ControllerGenPedCom.bandera = false;
        }
        if (controlador.equals("ControllerOrdComRec")) {
            ControllerOrdComRec.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerPedidoComProd")) {
            ControllerPedComProd.bandera = false;
        }
        if (controlador.equals("ControllerOrdCompxProd")) {
            ControllerOrdComxProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESProd")) {
            ControllerNotaESProd.bandera = false;
        }
        if (controlador.equals("ControllerNotaESvsFacProd")) {
            ControllerNotaESvsFacProd.bandera = false;
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
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        if (controlador.equals("ControllerCostos")) {
            ControllerGenPedVen.bandera = false;
        }
    }
}
