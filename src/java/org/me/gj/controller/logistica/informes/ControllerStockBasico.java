package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.StockTmp;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerStockBasico extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_imprimir, tbbtn_despacho;
    @Wire
    Listbox lst_stockbasico, lst_proveedor, lst_ubicacion, lst_almacen;
    @Wire
    Combobox cb_periodo, cb_proveedor, cb_ubicacion, cb_almacen;
    @Wire
    Textbox txt_provid, txt_provdes, txt_linid, txt_lindes, txt_proveedor;
    @Wire
    Intbox txt_almcod;
    @Wire
    Button btn_buscar;
    @Wire
    Radiogroup rbg_orden;
    @Wire
    Checkbox chk_valorizado, chk_impuesto, chk_incinac, chk_stock, chk_stockseg, chk_nobonif;
    //Instancias de Objetos
    ListModelList<StockTmp> objlstStock, objlstAux;
    ListModelList<Proveedores> objlstProveedores;
    ListModelList<Proveedores> objlstAuxp;
    ListModelList<Proveedores> objlstProveedorestmp;
    ListModelList<Ubicaciones> objlstUbicaciones;
    ListModelList<Ubicaciones> objlstAuxu;
    ListModelList<Ubicaciones> objlstUbicacionestmp;
    ListModelList<Almacenes> objlstAlmacenes;
    ListModelList<Almacenes> objlstAuxa;
    ListModelList<Almacenes> objlstAlmacenestmp;
    DaoProveedores objDaoProveedores;
    DaoAlmacenes objDaoAlmacenes;
    DaoUbicaciones objDaoUbicaciones;
    DaoAccesos objDaoAccesos;
    DaoStockTmp objDaoStockTmp;
    Accesos objAccesos;
    StockTmp objStockTmp;
    //Variables publicas
    String filtro = "PROVEEDOR";
    String valorizado = "0";
    String impuesto = "0";
    String orden = " order by x.prov_id ";
    String coninactivos = " ";
    String constocks = " ";
    String constockseg = " ";
    String nobonificacion = " and x.pro_clas = 'N'";
    String periodo = "";
    int emp_id, suc_id;
    int i_sel;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerStockBasico.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoStockTmp = new DaoStockTmp();
        objDaoProveedores = new DaoProveedores();
        objDaoAlmacenes = new DaoAlmacenes();
        objDaoUbicaciones = new DaoUbicaciones();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        ListModelList objlstManPer = new DaoManPer().listaPeriodos(1);
        objlstManPer.add(new ManPer("", ""));
        cb_periodo.setModel(objlstManPer);
        Date fecha = new Date();
        String per = sdfm.format(fecha);
        cb_periodo.setModel(objlstManPer);
        cb_periodo.setValue(per);
        cb_periodo.focus();
        cb_periodo.select();
        //lena los bandbox
        //carga combo de proveedores
        objlstProveedores = new ListModelList<Proveedores>();
        objlstProveedores = objDaoProveedores.listaProveedores("1");
        lst_proveedor.setModel(objlstProveedores);
        //carga combo de almacenes
        objlstAlmacenes = new ListModelList<Almacenes>();
        objlstAlmacenes = objDaoAlmacenes.lstAlmacenes(1);
        lst_almacen.setModel(objlstAlmacenes);
        //carga combo de ubicaciones
        objlstUbicaciones = new ListModelList<Ubicaciones>();
        objlstUbicaciones = objDaoUbicaciones.listaUbicaciones(1);
        lst_ubicacion.setModel(objlstUbicaciones);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10306010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Stock Basico con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Stock Basico con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de almacenes");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de almacenes");
        }
    }

    @Listen("onSelect=#lst_stockbasico")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCampos();
        objStockTmp = new StockTmp();
        objStockTmp = (StockTmp) lst_stockbasico.getSelectedItem().getValue();
        if (objStockTmp == null) {
            objStockTmp = objlstStock.get(i_sel + 1);
        }
        i_sel = lst_stockbasico.getSelectedIndex();
        //cargamos las listas con los contactos y telefonos de los proveedores
        int i_estado;
        //cargamos los campos
        llenarCampos(objStockTmp);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Stock del producto con codigo : " + objStockTmp.getPro_id());
    }

    @Listen("onClick=#btn_buscar")
    public void consultarRegistros() throws SQLException {
        int i = 0;
        for (int j = 0; j < objlstAlmacenes.getSize(); j++) {
            if (objlstAlmacenes.get(j).isValSelec()) {
                i = i + 1;
            }
        }
        if (i <= 0) {
            Messagebox.show("Debe seleccionar un almacen", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            LimpiarLista();
            limpiarCampos();
            if (rbg_orden.getItems().get(0).isChecked()) {//selecciono proveedor
                orden = " order by x.prov_id, x.pro_id, x.ubic_id";
                filtro = "PROVEEDOR";
            } else if (rbg_orden.getItems().get(1).isChecked()) {//selecciono linea
                orden = " order by x.lin_id, x.pro_id, x.ubic_id";
                filtro = "LINEA";
            }
            ParametrosSalida objParam;
            periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedItem().getValue().equals("") ? "%%" : cb_periodo.getSelectedItem().getValue().toString();
            coninactivos = chk_incinac.isChecked() == true ? "1,2" : "1";
            constocks = chk_stockseg.isChecked() == true ? "1" : "0";
            objlstStock = null;
            objlstStock = new ListModelList<StockTmp>();
            objParam = objDaoStockTmp.generarStocks(objlstStock, emp_id, suc_id, periodo, RetornaCadenaAlmacenes(), RetornaCadenaUbicaciones(),
                    RetornaCadenaProveedores(), valorizado, impuesto, orden, coninactivos, constocks, nobonificacion);
            if (objlstStock.getSize() > 0) {
                lst_stockbasico.setModel(objlstStock);
            } else {
                objlstStock = null;
                lst_stockbasico.setModel(objlstStock);
                Messagebox.show("No existe reporte de Stock de productos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onSeleccionp=#lst_proveedor")
    public void seleccionaRegistroProveedores(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstProveedores.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_proveedor.setModel(objlstProveedores);
    }

    @Listen("onSelecciona=#lst_almacen")
    public void seleccionaRegistroAlmacenes(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstAlmacenes.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_almacen.setModel(objlstAlmacenes);
    }

    @Listen("onSeleccionu=#lst_ubicacion")
    public void seleccionaRegistroUbicaciones(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstUbicaciones.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
        lst_ubicacion.setModel(objlstUbicaciones);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstStock == null || objlstStock.isEmpty()) {
            Messagebox.show("No existe stock de productos para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("periodo", periodo);
            objMapObjetos.put("filtro", filtro);
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionStockBasico.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_proveedor")
    public void buscarProveedor() throws SQLException {
        objlstProveedores = new ListModelList<Proveedores>();
        objlstProveedores = objDaoProveedores.busquedaProveedores(2, txt_proveedor.getValue().toUpperCase(), 1, "1");
        lst_proveedor.setModel(objlstProveedores);
    }

    @Listen("onCheck=#chk_valorizado")
    public void onCheckValorizado() {
        if (chk_valorizado.isChecked()) {
            valorizado = "1";
        } else {
            valorizado = "0";
        }
    }

    @Listen("onCheck=#chk_impuesto")
    public void onCheckImpuesto() {
        if (chk_impuesto.isChecked()) {
            impuesto = "1";
        } else {
            impuesto = "0";
        }
    }

    @Listen("onCheck=#chk_incinac")
    public void onCheckIncluyeinactivo() {
        if (chk_incinac.isChecked()) {
            coninactivos = " ";
        } else {
            coninactivos = " and x.estado = 1 ";
        }
    }

    @Listen("onCheck=#chk_stock")
    public void onCheckConStock() {
        if (chk_stock.isChecked()) {
            constocks = " and x.stock > 0 ";
        } else {
            constocks = " ";
        }
    }

    @Listen("onCheck=#chk_nobonif")
    public void onCheckSinBonificacion() {
        if (chk_nobonif.isChecked()) {
            nobonificacion = " and x.pro_clas = 'N' ";
        } else {
            nobonificacion = " ";
        }
    }

    @Listen("onClickListaStockDespacho=#lst_stockbasico")
    public void goManCliente(ForwardEvent evt) {
        tbbtn_despacho = (Toolbarbutton) evt.getOrigin().getTarget();
        objlstAux = (ListModelList) lst_stockbasico.getModel();
        Listitem item = (Listitem) tbbtn_despacho.getParent().getParent();

        /*String lp_id = objlstAux.get(item.getIndex()).getLp_id();
         String lp_provid = objlstAux.get(item.getIndex()).getProv_id();
         String lp_provrazsoc = objlstAux.get(item.getIndex()).getProv_razsoc();
         Double lp_descgen = objlstAux.get(item.getIndex()).getLp_descgen();
         Double lp_descfinan = objlstAux.get(item.getIndex()).getLp_descfinan();
         String lp_reporte = objlstAux.get(item.getIndex()).getLp_nomrep();
         String lp_usuadd = objUsuCredential.getCuenta();

         Map objMapObjetos = new HashMap();
         objMapObjetos.put("lp_id", lp_id);
         objMapObjetos.put("lp_provid", lp_provid);
         objMapObjetos.put("lp_provrazsoc", lp_provrazsoc);
         objMapObjetos.put("lp_descgen", lp_descgen);
         objMapObjetos.put("lp_descfinan", lp_descfinan);
         objMapObjetos.put("lp_reporte", lp_reporte);
         objMapObjetos.put("lp_usuadd", lp_usuadd);
         objMapObjetos.put("lst_pcompra", lst_pcompra);
         objMapObjetos.put("controlador", "ControllerLPCompra");
         Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClonacionLisPreCom.zul", null, objMapObjetos);
         window.doModal();*/
    }

    //Eventos Otros
    public ListModelList<Proveedores> getlistaProveedores(ListModelList<Proveedores> objlstProveedores) {
        objlstAuxp = new ListModelList<Proveedores>();
        for (int j = 0; j < objlstProveedores.getSize(); j++) {
            if (objlstProveedores.get(j).isValSelec()) {
                objlstAuxp.add(objlstProveedores.get(j));
            }
        }
        return objlstAuxp;
    }

    public String RetornaCadenaProveedores() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstProveedores.getSize(); j++) {
            if (objlstProveedores.get(j).isValSelec()) {
                i = i + 1;
                break;
            }
        }
        if (i <= 0) {
            cadena = "";
        } else {
            objlstProveedorestmp = new ListModelList<Proveedores>();
            objlstProveedorestmp = getlistaProveedores(objlstProveedores);
            for (int j = 0; j < objlstProveedorestmp.getSize(); j++) {
                cadena = cadena + objlstProveedorestmp.get(j).getProv_id() + ",";
            }
            int wPosx;
            wPosx = cadena.length();
            cadena = cadena.substring(0, (wPosx - 1)).toString();
        }
        return cadena;
    }

    public ListModelList<Almacenes> getlistaAlmacenes(ListModelList<Almacenes> objlstAlmacenes) {
        objlstAuxa = new ListModelList<Almacenes>();
        for (int j = 0; j < objlstAlmacenes.getSize(); j++) {
            if (objlstAlmacenes.get(j).isValSelec()) {
                objlstAuxa.add(objlstAlmacenes.get(j));
            }
        }
        return objlstAuxa;
    }

    public String RetornaCadenaAlmacenes() throws SQLException {
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
                //cadena = cadena + "'" + objlstAlmacenestmp.get(j).getAlm_id() + "'" + ",";
                cadena = cadena + objlstAlmacenestmp.get(j).getAlm_id() + ",";
            }
            int wPosx;
            wPosx = cadena.length();
            cadena = cadena.substring(0, (wPosx - 1)).toString();
        }
        return cadena;
    }

    public ListModelList<Ubicaciones> getlistaUbicaciones(ListModelList<Ubicaciones> objlstUbicaciones) {
        objlstAuxu = new ListModelList<Ubicaciones>();
        for (int j = 0; j < objlstUbicaciones.getSize(); j++) {
            if (objlstUbicaciones.get(j).isValSelec()) {
                objlstAuxu.add(objlstUbicaciones.get(j));
            }
        }
        return objlstAuxu;
    }

    public String RetornaCadenaUbicaciones() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstUbicaciones.getSize(); j++) {
            if (objlstUbicaciones.get(j).isValSelec()) {
                i = i + 1;
                break;
            }
        }
        if (i <= 0) {
            cadena = "";
        } else {
            objlstUbicacionestmp = new ListModelList<Ubicaciones>();
            objlstUbicacionestmp = getlistaUbicaciones(objlstUbicaciones);
            for (int j = 0; j < objlstUbicacionestmp.getSize(); j++) {
                //cadena = cadena + "'" + objlstUbicacionestmp.get(j).getUbic_id() + "'" + ",";
                cadena = cadena +  objlstUbicacionestmp.get(j).getUbic_id()  + ",";
            }
            int wPosx;
            wPosx = cadena.length();
            cadena = cadena.substring(0, (wPosx - 1)).toString();
        }
        return cadena;
    }

    public void llenarCampos(StockTmp objStockTmp) throws SQLException {
        txt_provid.setValue(objStockTmp.getProv_id());
        txt_provdes.setValue(objStockTmp.getProv_razsoc());
        txt_linid.setValue(objStockTmp.getLin_id());
        txt_lindes.setValue(objStockTmp.getLin_des());
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstStock = null;
        objlstStock = new ListModelList<StockTmp>();
        lst_stockbasico.setModel(objlstStock);
    }

    public void limpiarCampos() {
        txt_provid.setValue("");
        txt_provdes.setValue("");
        txt_linid.setValue("");
        txt_lindes.setValue("");
    }
}
