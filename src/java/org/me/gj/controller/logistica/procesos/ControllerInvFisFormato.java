package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.InvFisico;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerInvFisFormato extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_deshacer, tbbtn_btn_guardar, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_periodo;
    @Wire
    Button btn_consultar, btn_provid, btn_artid;
    @Wire
    Listbox lst_formatoinvfis, lst_almacen;
    @Wire
    Textbox txt_provid, txt_provdes, txt_proid, txt_prodes;
    @Wire
    Bandbox cb_almacen;
    @Wire
    Checkbox chk_stock;
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<Almacenes> objlstAlmacenes;
    ListModelList<Almacenes> objlstAlmacenesAux;
    ListModelList<Almacenes> objlstAlmacenestmp;
    ListModelList<InvFisico> objlstInvFisico;
    ListModelList<ManPer> objListModelListManPer;
    DaoInvFisFormato objDaoInvFisFormato = new DaoInvFisFormato();
    DaoProveedores objDaoProveedores = new DaoProveedores();
    DaoAlmacenes objDaoAlmacenes = new DaoAlmacenes();
    DaoManPer objDaoManPer = new DaoManPer();
    DaoProductos objDaoProductos = new DaoProductos();            
    //Variables publicas 
    int i_selCab;
    String tipo = "DFM1";
    String s_codempp = "";
    String s_codsucp = "";
    String modoEjecucion;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerInvFisFormato.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //lista ventas x canal lo inicializamos y creamos nueva instancia
        objlstInvFisico = null;
        objlstInvFisico = new ListModelList<InvFisico>();
        //llenamos los canales
        objlstAlmacenes = new ListModelList<Almacenes>();
        //cargo almacenes
        objlstAlmacenes = objDaoAlmacenes.lstAlmacenes(1);
        lst_almacen.setModel(objlstAlmacenes);
        //periodo
        //carga periodos y selecciona el actual
        objListModelListManPer = new ListModelList<ManPer>();
        objListModelListManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objListModelListManPer);
        objListModelListManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
    }

    @Listen("onSeleccion=#lst_almacen")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstAlmacenes.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_almacen.setModel(objlstAlmacenes);

    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstInvFisico.isEmpty()) {
            Messagebox.show("No hay Registros de Inventarios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstInvFisico.getSize(); i++) {
                objlstInvFisico.get(i).setValSelec(chk_selecAll.isChecked());
            }
            lst_formatoinvfis.setModel(objlstInvFisico);
        }
    }

    public ListModelList<Almacenes> getlistaAlmacenes(ListModelList<Almacenes> objlstVtasCostosProveedores) {
        objlstAlmacenesAux = new ListModelList<Almacenes>();
        for (int j = 0; j < objlstVtasCostosProveedores.getSize(); j++) {
            if (objlstVtasCostosProveedores.get(j).isValSelec()) {
                objlstAlmacenesAux.add(objlstVtasCostosProveedores.get(j));
            }
        }
        return objlstAlmacenesAux;
    }

    public String RetornaCadenaAlmacen() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstAlmacenes.getSize(); j++) {
            if (objlstAlmacenes.get(j).isValSelec()) {
                i = i + 1;
                break;
            }
        }
        if (i <= 0) {
            cadena = "";
        } else {
            objlstAlmacenestmp = new ListModelList<Almacenes>();
            objlstAlmacenestmp = getlistaAlmacenes(objlstAlmacenes);
            for (int j = 0; j < objlstAlmacenestmp.getSize(); j++) {
                cadena = cadena + objlstAlmacenestmp.get(j).getAlm_key()+ "','";
            }
        }
        return cadena;
    }

    @Listen("onOK=#txt_provid")
    public void txtProvid() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                modoEjecucion = "InvFisFormato";
                String tipo = "1";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_proid", txt_proid);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerInvFisFormato");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_proid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        txt_provdes.setValue("");
        if (!txt_provid.getValue().isEmpty()) {
            String prov_id = txt_provid.getValue().toUpperCase();
            Proveedores objProveedores = objDaoProveedores.BusquedaProveedor(Long.parseLong(prov_id));
            if (objProveedores == null) {
                Messagebox.show("El codigo de Proveedor no Existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_provid.setValue("");
            } else {
                txt_provid.setValue(objProveedores.getProv_id());
                txt_provdes.setValue(objProveedores.getProv_razsoc());
            }

        }
        bandera = false;
    }

    @Listen("onOK=#txt_proid")
    public void txtArtid() {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                modoEjecucion = "InvFisFormato";
                String prov_id = txt_provid.getValue().isEmpty() ? "" : txt_provid.getValue();
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_proid", txt_proid);
                objMapObjetos.put("txt_prodes", txt_prodes);
                objMapObjetos.put("proveedor", prov_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerInvFisFormato");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }

    @Listen("onBlur=#txt_proid")
    public void validaArticulo() throws SQLException {
        txt_prodes.setValue("");
        if (!txt_proid.getValue().isEmpty()) {
            if (!txt_proid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_proid.setValue("");
            } else {
                String prov_id = txt_provid.getValue().toUpperCase();
                String pro_id = txt_proid.getValue().toUpperCase();
                Productos objProductos = objDaoProductos.buscarProducto(pro_id, prov_id);
                if (objProductos == null) {
                    Messagebox.show("El codigo de Articulo no Existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_proid.setValue("");
                } else {
                    txt_provid.setValue(objProductos.getPro_provid());
                    txt_provdes.setValue(objProductos.getPro_sigprov());
                    txt_proid.setValue(objProductos.getPro_id());
                    txt_prodes.setValue(objProductos.getPro_des());
                }
            }
        }
        bandera = false;
    }

    @Listen("onClick=#btn_consultar")
    public void botonConsultar() throws SQLException {
        if (objlstAlmacenes == null || objlstAlmacenes.isEmpty()) {
            Messagebox.show("No hay Registros de Inventarios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String periodo = cb_periodo.getSelectedItem().getValue();
            String proveedor = txt_provid.getValue();
            String articulo = txt_proid.getValue();
            String stock = (chk_stock.isChecked() ? ">" : "<=");
            objlstInvFisico = objDaoInvFisFormato.listaInventario(periodo, RetornaCadenaAlmacen(), proveedor, articulo, stock);
            lst_formatoinvfis.setModel(objlstInvFisico);
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstInvFisico.isEmpty()) {
            Messagebox.show("No hay Registros de Inventarios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("listainventarios", objlstInvFisico);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionInventario.zul", null, objMapObjetos);
            window.doModal();
        }
    }

}
