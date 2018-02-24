package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import static org.me.gj.controller.logistica.informes.ControllerKardex.bandera;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.procesos.DaoCosteo;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.Costos;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerCostos extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_repcostos;
    @Wire
    Tab tab_lista;
    @Wire
    Listbox lst_costos;
    /*@Wire
     Checkbox chk_imprimir;*/
    @Wire
    Combobox cb_periodo;
    @Wire
    Button btn_consultar;
    @Wire
    Intbox txt_almid;
    @Wire
    Textbox txt_almdes, txt_provid, txt_provdes, txt_proid, txt_prodes;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPer;
    ListModelList<Costos> objlstCostos;
    Utilitarios objUtil = new Utilitarios();
    DaoManPer objDaoManPer = new DaoManPer();
    ManPer objManPer = new ManPer();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Guias objGuias = new Guias();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    DaoCosteo objDaoCosteo = new DaoCosteo();
    Costos objCostos = new Costos();
    //Variables publicas
    String s_estado = "Q";
    String s_estadoDetalle = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCostos.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_costos")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objlstManPer = new ListModelList<ManPer>();
        objlstManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objlstManPer);
        objlstManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10309000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a Informes de Nota E/S con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a Informes de Nota E/S con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a imprimir informes de Nota E/S");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a imprimir informes de Nota E/S");
        }
    }
    
    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        int emp_id, suc_id;
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        String s_periodo, s_almacen, s_proveedor, s_producto;
        s_periodo = cb_periodo.getValue().isEmpty() ? "%%" : cb_periodo.getValue();
        s_almacen = txt_almid.getValue() == null ? "%%" : Utilitarios.lpad(txt_almid.getValue().toString(), 4, "0");
        s_proveedor = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
        s_producto = txt_proid.getValue().isEmpty() ? "%%" : txt_proid.getValue();
        
        objlstCostos = new ListModelList<Costos>();
        objlstCostos = objDaoCosteo.busquedaCostos(emp_id, suc_id, s_periodo, s_almacen, s_proveedor, s_producto);
        if (objlstCostos.getSize() > 0) {
            lst_costos.setModel(objlstCostos);
        } else {
            Messagebox.show("No existen registros");
        }
        
    }
    
    @Listen("onOK=#txt_almid")
    public void lovAlmID() throws SQLException {
        if (txt_almid.getValue() == null) {
            Map objMapObjetos = new HashMap();
            modoEjecucion = "Costos";
            objMapObjetos.put("txt_almid", txt_almid);
            objMapObjetos.put("txt_almdes", txt_almdes);
            objMapObjetos.put("tipo", "1");
            objMapObjetos.put("validaBusqueda", modoEjecucion);
            objMapObjetos.put("controlador", "ControllerCostos");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAlmacenes.zul", null, objMapObjetos);
            window.doModal();
        } else {
            txt_provid.focus();
        }
    }
    
    @Listen("onBlur=#txt_almid")
    public void validaBlurAlmID() throws SQLException {
        if (txt_almid.getValue() != null) {
            int alm_id = txt_almid.getValue();
            Almacenes objAlmacenes = (new DaoAlmacenes()).getNomAlmacenes(alm_id);
            if (objAlmacenes == null) {
                Messagebox.show("El código de almacen no existe o está eliminado", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_almid.setValue(null);
                                    txt_almdes.setValue("");
                                    txt_almid.focus();
                                }
                            }
                        });
            } else {
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objAlmacenes.getAlm_id() + " y ha encontrado 1 registro(s)");
                txt_almid.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
                txt_almdes.setValue(objAlmacenes.getAlm_des());
            }
        } else {
            txt_almdes.setValue("");
        }
    }
    
    @Listen("onOK=#txt_provid")
    public void lov_ocproveedor() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "Costos";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerCostos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_proid.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_provid.setValue("");
                                    txt_provdes.setValue("");
                                    txt_provid.focus();
                                }
                            }
                        });
            } else {
                long prov_id = Long.parseLong(txt_provid.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    s_mensaje = "El código de proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_provid.setValue("");
                                        txt_provdes.setValue("");
                                        txt_provid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        } else {
            txt_provdes.setValue("");
        }
        bandera = false;
    }
    
    @Listen("onOK=#txt_proid")
    public void lovProductosp() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "Costos";
                String prov_id = "";
                objMapObjetos.put("txt_proid", txt_proid);
                objMapObjetos.put("txt_prodes", txt_prodes);
                objMapObjetos.put("proveedor", prov_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerCostos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_proid")
    public void validaProductosp() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (!txt_proid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_proid.setValue("");
                        txt_prodes.setValue("");
                        txt_proid.focus();
                    }
                });
            } else {
                String pro_id = txt_proid.getValue();
                Productos objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_proid.setValue("");
                            txt_prodes.setValue("");
                            txt_proid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_proid.setValue(objProducto.getPro_id());
                    txt_prodes.setValue(objProducto.getPro_des());
                }
            }
        } else {
            txt_prodes.setValue("");
        }
        bandera = false;
    }
    
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstCostos == null || objlstCostos.isEmpty()) {
            Messagebox.show("No hay registro de costos para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionCostos.zul", null, objMapObjetos);
            window.doModal();
        }
    }   
}
