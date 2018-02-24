package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.procesos.DaoOrdCom;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.OrdCompProd;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.OrdCompCab;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerOrdComxProd extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Textbox txt_provid, txt_provdes, txt_prodid, txt_proddes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod;
    @Wire
    Button btn_buscarordenes;
    @Wire
    Listbox lst_ordcompcabxprod, lst_ordcompcab;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Doublebox txt_totcant, txt_totdes, txt_totafec, txt_totinafec, txt_totexo, txt_totigv, txt_totventa;
    //Instancias de Objetos
    ListModelList<OrdCompProd> objlstOrdCompCabxProd;
    ListModelList<OrdCompCab> objlstOrdCompCab;
    ListModelList<ManPer> objlstPeriodos;
    DaoOrdCom objDaoOrdCom;
    DaoManPer objDaoPeriodo;
    DaoAccesos objDaoAccesos;
    ManPer objPeriodo;
    OrdCompProd objOrdCompProd;
    OrdCompCab objOrdCompCab;
    Accesos objAccesos;
    //Variables publicas
    int emp_id, suc_id;
    int i_selCab;
    String fechaActual;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerOrdComxProd.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objDaoPeriodo = new DaoManPer();
        objDaoOrdCom = new DaoOrdCom();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objlstPeriodos = objDaoPeriodo.listaPeriodos(1);
        objlstPeriodos.add(new ManPer("", ""));
        cb_periodo.setModel(objlstPeriodos);
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10301020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Orden de Compra por Producto con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Orden de Compra por Producto con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Orden de Compra por Producto");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Orden de Compra por Producto");
        }
    }
    
    @Listen("onClick=#btn_buscarordenes")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String resultado/* = ""*/;
        Date fecha_emisioni = d_fecini.getValue();
        Date fecha_emisionf = d_fecfin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_fecini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_fecfin.getValue());
        }
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_ordcompcabxprod.setSelectedIndex(-1);
            limpiarCamposTotales();
        } else {
            String per = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedItem().getValue() == "" ? "%%" : cb_periodo.getSelectedItem().getValue().toString();
            String prod_id = txt_prodid.getValue().isEmpty() ? "%%" : Utilitarios.lpad(txt_prodid.getValue(), 9, "0");
            String prov_id = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
            objlstOrdCompCabxProd = new ListModelList<OrdCompProd>();
            objlstOrdCompCabxProd = objDaoOrdCom.listaTotalxProducto(emp_id, suc_id, per, "%45%", f_emisioni, f_emisionf, prov_id, prod_id);
            if (objlstOrdCompCabxProd.getSize() > 0) {
                lst_ordcompcabxprod.setModel(objlstOrdCompCabxProd);
                llenarCamposTotales();
            } else {
                objlstOrdCompCabxProd = null;
                lst_ordcompcabxprod.setModel(objlstOrdCompCabxProd);
                limpiarCamposTotales();
                Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        LimpiarCampos();
        objlstOrdCompCab = null;
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }
    
    @Listen("onSelect=#lst_ordcompcabxprod")
    public void seleccionaRegistroTotal() throws SQLException {
        objOrdCompProd = (OrdCompProd) lst_ordcompcabxprod.getSelectedItem().getValue();
        String per = objOrdCompProd.getPeriodo();
        String fecini = objOrdCompProd.getFecemi();
        String fecfinal = objOrdCompProd.getFecfin();
        String pro_id = objOrdCompProd.getPro_id();
        //String pro_key = String.valueOf(Long.parseLong(objOrdCompProd.getPro_id()));
        objlstOrdCompCab = null;
        objlstOrdCompCab = objDaoOrdCom.OrdCompxProducto(emp_id, suc_id, per, "%45%", fecini, fecfinal, pro_id);
        LimpiarCampos();
        lst_ordcompcab.setModel(objlstOrdCompCab);
    }
    
    @Listen("onSelect=#lst_ordcompcab")
    public void seleccionaRegistro() {
        objOrdCompCab = (OrdCompCab) lst_ordcompcab.getSelectedItem().getValue();
        LimpiarCampos();
        txt_usuadd.setValue(objOrdCompCab.getOc_usuadd());
        d_fecadd.setValue(objOrdCompCab.getOc_fecadd());
        txt_usumod.setValue(objOrdCompCab.getOc_usumod());
        d_fecmod.setValue(objOrdCompCab.getOc_fecmod());
    }
    
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstOrdCompCabxProd == null || objlstOrdCompCabxProd.isEmpty()) {
            Messagebox.show("No hay ordenes de compra por producto para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_ordcompcabxprod.getSelectedIndex() >= 0) {
                objOrdCompProd = (OrdCompProd) lst_ordcompcabxprod.getSelectedItem().getValue();
                if (objOrdCompProd == null) {
                    objOrdCompProd = objlstOrdCompCabxProd.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("pro_id", objOrdCompProd.getPro_id());
                objMapObjetos.put("pro_des", objOrdCompProd.getPro_des());
                objMapObjetos.put("svimpto", objOrdCompProd.getSvimpto());
                objMapObjetos.put("svdesc", objOrdCompProd.getSvdesc());
                objMapObjetos.put("svafecto", objOrdCompProd.getSvafecto());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionOrdComxProd.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione un producto para imprimir sus ordenes de compra");
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_provid")
    public void lovProveedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                String tipo = "1";
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "OrdComProd";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompxProd");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarordenes.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_provid")
    public void validaProveedores() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_provid.setValue("");
                        txt_provdes.setValue("");
                        txt_provid.focus();
                    }
                });
            } else {
                Long prov_id = Long.parseLong(txt_provid.getValue());
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(prov_id);
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        } else {
            txt_provdes.setValue("");
        }
        bandera = false;
    }
    
    @Listen("onOK=#txt_prodid")
    public void lovProductos() {
        if (bandera == false) {
            bandera = true;
            if (txt_prodid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "OrdComProd";
                String prov_id = txt_provid.getValue().isEmpty() ? "" : txt_provid.getValue();
                objMapObjetos.put("txt_proid", txt_prodid);
                objMapObjetos.put("txt_prodes", txt_proddes);
                objMapObjetos.put("proveedor", prov_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerOrdCompxProd");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarordenes.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_prodid")
    public void validaProductos() throws SQLException {
        if (!txt_prodid.getValue().isEmpty()) {
            if (!txt_prodid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prodid.setValue("");
                        txt_proddes.setValue("");
                        txt_prodid.focus();
                    }
                });
            } else {
                String pro_id = txt_prodid.getValue();
                Productos objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_prodid.setValue("");
                            txt_proddes.setValue("");
                            txt_prodid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_prodid.setValue(objProducto.getPro_id());
                    txt_proddes.setValue(objProducto.getPro_des());
                }
            }
        }else{
            txt_proddes.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros 
    public void LimpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }
    
    public void LimpiarLista() {
        //limpio mi lista
        objlstOrdCompCabxProd = null;
        objlstOrdCompCabxProd = new ListModelList<OrdCompProd>();
        lst_ordcompcabxprod.setModel(objlstOrdCompCabxProd);
    }
    
    public double[] calculosTotal() {
        int i, cont = 1;
        double data[] = new double[7];
        for (i = 0; i < objlstOrdCompCabxProd.getSize(); i++) {
            //data[0] = cont++;
            data[0] = data[0] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getCant();
            data[1] = data[1] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVdesc();
            data[2] = data[2] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVafecto();
            data[3] = data[3] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVinafecto();
            data[4] = data[4] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVexonerado();
            data[5] = data[5] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVimpto();
            data[6] = data[6] + ((OrdCompProd) objlstOrdCompCabxProd.get(i)).getVtotal();
        }
        return data;
    }
    
    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_totcant.setValue(data[0]);
        txt_totdes.setValue(data[1]);
        txt_totafec.setValue(data[2]);
        txt_totinafec.setValue(data[3]);
        txt_totexo.setValue(data[4]);
        txt_totigv.setValue(data[5]);
        txt_totventa.setValue(data[6]);
        txt_totventa.setStyle("text-align: right; background-color: #BEF781;color: #2E2E2E;");
    }
    
    public void limpiarCamposTotales() {
        txt_totcant.setValue(null);
        txt_totdes.setValue(null);
        txt_totafec.setValue(null);
        txt_totinafec.setValue(null);
        txt_totexo.setValue(null);
        txt_totigv.setValue(null);
        txt_totventa.setValue(null);
        txt_totventa.setStyle(null);
    }
}
