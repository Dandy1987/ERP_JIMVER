package org.me.gj.controller.logistica.informes;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.informes.Kardex;
import org.me.gj.model.logistica.informes.KardexCab;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
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
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerKardex extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Listbox lst_kardex, lst_proveedor, lst_almacen;
    @Wire
    Datebox d_fecini, d_fecfin;
    @Wire
    Combobox cb_periodo;
    @Wire
    Textbox txt_prodidp, txt_proddesp, txt_prodidf, txt_proddesf, txt_proveedor, txt_prodid, txt_proddes, txt_provid, txt_provrazsoc, txt_almdes;
    @Wire
    Button btn_buscar;
    @Wire
    Radiogroup rbg_productos, rbg_detalle;
    @Wire
    Button btn_consultar;
    @Wire
    Bandbox cb_proveedor, cb_almacen;
    @Wire
    Doublebox txt_etotent, txt_etotfra, txt_stotent, txt_stotfra, /*txt_stkacte, txt_stkactf,*/ txt_undpre;
    @Wire
    Intbox txt_stkinie, txt_stkinif, txt_stkacte, txt_stkactf, txt_almid;
    //Instancias de Objetos
    ListModelList<Kardex> objlstKardex;
    ListModelList<Proveedores> objlstProveedores;
    ListModelList<Proveedores> objlstAuxp;
    ListModelList<Proveedores> objlstProveedorestmp;
    ListModelList<Almacenes> objlstAlmacenes;
    ListModelList<Almacenes> objlstAuxa;
    ListModelList<Almacenes> objlstAlmacenestmp;
    DaoProveedores objDaoProveedores;
    DaoAlmacenes objDaoAlmacenes;
    DaoAccesos objDaoAccesos;
    DaoKardex objDaoKardex;
    Accesos objAccesos;
    Kardex objKardex;
    KardexCab objKardexCab;
    Proveedores objProveedores;
    Almacenes objAlmacen;
    //Variables Publicas
    String filtro = "";
    String detalle = "D";
    String periodo = "";
    String resultado = "";
    String s_valor = "";
    String s_filtro = "Q";
    String campo = "";
    String tipodet = "D";
    String tipopro = "";
    int emp_id, suc_id;
    int i_sel;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerKardex.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoKardex = new DaoKardex();
        objDaoProveedores = new DaoProveedores();
        objDaoAlmacenes = new DaoAlmacenes();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        d_fecini.focus();
        d_fecini.select();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10307000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Kardex con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Kardex con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion del Kardex");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion del Kardex");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void ConsultarKardex() throws SQLException, UnknownHostException, SocketException {
        limpiarLista();
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Almacen")) {
                            txt_almid.focus();
                        } else if (campo.equals("Producto Inicial")) {
                            txt_prodidp.focus();
                        } else if (campo.equals("Producto Final")) {
                            txt_prodidf.focus();
                        }
                    }
                }
            });
            limpiarLista();
            limpiarCamposDetalle();
        } else {
            if (rbg_productos.getItems().get(2).isChecked()) {//si eligio rango o totalizado
                Messagebox.show("El Kardex totalizado solo se mostrará en Reporte", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                limpiarLista();
                limpiarCamposDetalle();
            } else {//si solo eligio un producto
                Date fecha_emisioni = d_fecini.getValue();
                Date fecha_emisionf = d_fecfin.getValue();
                if (fecha_emisioni == null || fecha_emisionf == null) {
                    resultado = "OK";
                } else {
                    resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
                }
                String pro_idi = txt_prodidp.getValue().isEmpty() ? "%%" : txt_prodidp.getValue();
                String pro_idf;
                if (rbg_productos.getItems().get(1).isChecked()) {
                    pro_idf = txt_prodidf.getValue().isEmpty() ? "%%" : txt_prodidf.getValue();
                } else {
                    pro_idf = "";
                }
                String prov_id;
                prov_id = txt_provid.getValue().isEmpty() == true ? "" : txt_provid.getValue();
                String alm_id;
                alm_id = txt_almid.getValue() == null ? "" : Utilitarios.lpad(txt_almid.getValue().toString(), 4, "0");
                if (resultado.equals("F1>")) {
                    Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if (rbg_productos.getItems().get(1).isChecked()) {
                        tipopro = "RG";
                    }
                    objKardexCab = null;
                    objKardexCab = new KardexCab();
                    if (rbg_detalle.getItems().get(1).isChecked()) {//Si eligio resumido
                        tipodet = "R";
                        objKardexCab = objDaoKardex.generarKardexResumido(emp_id, suc_id, fecha_emisioni, fecha_emisionf, alm_id, prov_id, pro_idi, pro_idf, objUsuCredential.getCuenta() + objUsuCredential.getComputerName());
                    } else if (rbg_detalle.getItems().get(0).isChecked()) {//Si eligio detallado
                        tipodet = "D";
                        objKardexCab = objDaoKardex.generarKardexDetallado(emp_id, suc_id, fecha_emisioni, fecha_emisionf, alm_id, prov_id, pro_idi, pro_idf, objUsuCredential.getCuenta() + objUsuCredential.getComputerName());
                    }
                    if (objKardexCab == null) {
                        Messagebox.show("No existen registros para mostrar");
                        limpiarLista();
                        limpiarCamposDetalle();
                    } else if (rbg_productos.getItems().get(1).isChecked()) {//si eligio rango o totalizado
                        Messagebox.show("El Kardex por rango solo se mostrará en Reporte", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        limpiarLista();
                        limpiarCamposDetalle();
                    } else {
                        llenaCabecera(objKardexCab);
                        llenaDetalle(emp_id, suc_id, fecha_emisioni, fecha_emisionf, objKardexCab.getKar_key(), tipodet);
                        llenarTotales();
                    }
                }
            }
        }
    }

    @Listen("onOK=#d_fecini")
    public void next_fechafin() {
        d_fecfin.focus();
        d_fecfin.select();
    }
    
    @Listen("onOK=#d_fecfin")
    public void next_provid() {
        txt_provid.focus();
    }
    
    @Listen("onOK=#txt_provid")
    public void lov_ocproveedor() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "Kardex";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provrazsoc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerKardex");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_almid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                String s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_provid.setValue("");
                                    txt_provrazsoc.setValue("");
                                    txt_provid.focus();
                                }
                            }
                        });
            } else {
                long prov_id = Long.parseLong(txt_provid.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    String s_mensaje = "El código de proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_provid.setValue("");
                                        txt_provrazsoc.setValue("");
                                        txt_provid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provrazsoc.setValue(objProveedor.getProv_razsoc());
                }
            }
        } else {
            txt_provrazsoc.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_almid")
    public void lovAlmID() throws SQLException {
        if (txt_almid.getValue() == null) {
            Map objMapObjetos = new HashMap();
            String modoEjecucion = "StockBasico";
            objMapObjetos.put("txt_almid", txt_almid);
            objMapObjetos.put("txt_almdes", txt_almdes);
            objMapObjetos.put("tipo", "1");
            objMapObjetos.put("validaBusqueda", modoEjecucion);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAlmacenes.zul", null, objMapObjetos);
            window.doModal();
        } else {
            rbg_detalle.focus();

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

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException, UnknownHostException, SocketException {
        if ("R".equals(tipodet) || "D".equals(tipodet)) {//Resumido o Detallado
            if (objlstKardex == null || objlstKardex.isEmpty()) {
                if ("RG".equals(tipopro)) {
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                    objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                    objMapObjetos.put("tipodet", tipodet);
                    objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionKardex.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    Messagebox.show("No hay movimientos en el Kardex", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            } else {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("tipodet", tipodet);
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionKardex.zul", null, objMapObjetos);
                window.doModal();
            }
        } else if ("T".equals(tipodet)) {
            Date fecha_emisioni = d_fecini.getValue();
            Date fecha_emisionf = d_fecfin.getValue();
            //String s_mac = "";

            if (fecha_emisioni == null || fecha_emisionf == null) {
                resultado = "OK";
            } else {
                resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
            }
            String pro_idi = txt_prodidp.getValue().isEmpty() ? "%%" : txt_prodidp.getValue();
            String pro_idf;
            if (rbg_productos.getItems().get(1).isChecked()) {
                pro_idf = txt_prodidf.getValue().isEmpty() ? "%%" : txt_prodidf.getValue();
            } else {
                pro_idf = "";
            }
            String prov_id;
            prov_id = txt_provid.getValue().isEmpty() == true ? "" : txt_provid.getValue();
            String alm_id;
            alm_id = txt_almid.getValue() == null ? "" : txt_almid.getValue().toString();

            if (resultado.equals("F1>")) {
                Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                /*InetAddress ip;
                 ip = InetAddress.getLocalHost();
                 //System.out.println("Current IP address : " + ip.getHostAddress());
                 NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                 byte[] mac = network.getHardwareAddress();
                 //System.out.print("Current MAC address : ");
                 StringBuilder sb = new StringBuilder();
                 for (int i = 0; i < mac.length; i++) {
                 sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                 }
                 //System.out.println(sb.toString());
                 s_mac = sb.toString();*/

            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            //  objKardexCab = objDaoKardex.generarKardexTotalizado(emp_id, suc_id, fecha_emisioni, fecha_emisionf, alm_id, prov_id, pro_idi, pro_idf, s_mac);

            objMapObjetos.put("femi", fecha_emisioni);
            objMapObjetos.put("ffin", fecha_emisionf);
            objMapObjetos.put("almid", alm_id);
            objMapObjetos.put("provid", prov_id);
            objMapObjetos.put("proidi", pro_idi);
            objMapObjetos.put("proidf", pro_idf);
            //objMapObjetos.put("mac", s_mac);
            objMapObjetos.put("mac", objUsuCredential.getCuenta() + objUsuCredential.getComputerName());
            objMapObjetos.put("tipodet", tipodet);
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionKardex.zul", null, objMapObjetos);
            window.doModal();
            }
        }
    }

    //Eventos Secundarios - Validaciones 
    @Listen("onOK=#txt_proveedor")
    public void buscarProveedor() throws SQLException {
        objlstProveedores = new ListModelList<Proveedores>();
        objlstProveedores = objDaoProveedores.busquedaProveedores(2, txt_proveedor.getValue().toUpperCase(), 1, "1");
        lst_proveedor.setModel(objlstProveedores);
    }

    @Listen("onCheck=#rbg_detalle;")
    public void SeleccionaDetalle() {
        if (rbg_detalle.getItems().get(0).isChecked()) {
            detalle = "D";
        } else if (rbg_detalle.getItems().get(1).isChecked()) {
            detalle = "R";
        }
    }

    @Listen("onOK=#rbg_detalle")
    public void onOKDetalle() {
        rbg_productos.getItems().get(0).setChecked(true);
        SeleccionaProductos();
        /*txt_prodidp.setDisabled(false);
        txt_prodidp.focus();*/
    }

    @Listen("onCheck=#rbg_productos;")
    public void SeleccionaProductos() {
        if (rbg_productos.getItems().get(0).isChecked()) {
            habilitadetalle(false);
            txt_prodidp.setDisabled(false);
            txt_prodidf.setDisabled(true);
            txt_prodidp.focus();
            limpiarCampos();
            s_filtro = "0";
        } else if (rbg_productos.getItems().get(1).isChecked()) {
            habilitadetalle(false);
            txt_prodidp.setDisabled(false);
            txt_prodidf.setDisabled(false);
            txt_prodidp.focus();
            limpiarCampos();
            s_filtro = "1";
        } else if (rbg_productos.getItems().get(2).isChecked()) {
            tipodet = "T";
            habilitadetalle(true);
            txt_prodidp.setDisabled(false);
            txt_prodidf.setDisabled(true);
            txt_prodidp.focus();
            limpiarCampos();
            s_filtro = "0";
        }
    }

    @Listen("onOK=#txt_prodidp")
    public void lovProductosp() throws SQLException {
        if (bandera == false) {
            if (txt_prodidp.getValue().isEmpty()) {
                bandera = true;
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "Kardexp";
                String prov_id = txt_provid.getValue().isEmpty() ? "" : txt_provid.getValue();
                objMapObjetos.put("txt_proid", txt_prodidp);
                objMapObjetos.put("txt_prodes", txt_proddesp);
                objMapObjetos.put("proveedor", prov_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerKardex");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                //validaProductosp();
                if (rbg_productos.getItems().get(1).isChecked()) {
                    txt_prodidf.focus();
                } else {
                    btn_consultar.focus();
                }

            }
        }
    }

    @Listen("onBlur=#txt_prodidp")
    public void validaProductosp() throws SQLException {
        if (!txt_prodidp.getValue().isEmpty()) {
            if (!txt_prodidp.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prodidp.setValue("");
                        txt_proddesp.setValue("");
                        txt_prodidp.focus();
                    }
                });
            } else {
                String pro_id = txt_prodidp.getValue();
                Productos objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_prodidp.setValue("");
                            txt_proddesp.setValue("");
                            txt_prodidp.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_prodidp.setValue(objProducto.getPro_id());
                    txt_proddesp.setValue(objProducto.getPro_des());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_prodidf")
    public void lovProductosf() throws SQLException {
        if (bandera == false) {
            if (txt_prodidf.getValue().isEmpty()) {
                bandera = true;
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "Kardexf";
                String prov_id = txt_provid.getValue().isEmpty() ? "" : txt_provid.getValue();
                objMapObjetos.put("txt_proid", txt_prodidf);
                objMapObjetos.put("txt_prodes", txt_proddesf);
                objMapObjetos.put("proveedor", prov_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerKardex");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }

    @Listen("onBlur=#txt_prodidf")
    public void validaProductosf() throws SQLException {
        if (!txt_prodidf.getValue().isEmpty()) {
            if (!txt_prodidf.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prodidf.setValue("");
                        txt_proddesf.setValue("");
                        txt_prodidf.focus();
                    }
                });
            } else {
                String pro_id = txt_prodidf.getValue();
                Productos objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_prodidf.setValue("");
                            txt_proddesf.setValue("");
                            txt_prodidf.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_prodidf.setValue(objProducto.getPro_id());
                    txt_proddesf.setValue(objProducto.getPro_des());
                }
            }
        }
        bandera = false;
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
                cadena = cadena + objlstAlmacenestmp.get(j).getAlm_id() + ",";
            }
            int wPosx;
            wPosx = cadena.length();
            cadena = cadena.substring(0, (wPosx - 1)).toString();
        }
        return cadena;
    }

    public void llenaDetalle(int emp_id, int suc_id, Date fechae, Date fechaf, long kar_key, String tip) throws SQLException {
        if ("R".equals(tip)) {
            objlstKardex = objDaoKardex.mostrarKardexResumido(emp_id, suc_id, fechae, fechaf, kar_key);
        } else {
            objlstKardex = objDaoKardex.mostrarKardexDetallado(emp_id, suc_id, fechae, fechaf, kar_key);
        }
        lst_kardex.setModel(objlstKardex);
    }

    public void llenaCabecera(KardexCab objKardexCab) {
        txt_prodid.setValue(objKardexCab.getPro_id());
        txt_proddes.setValue(objKardexCab.getPro_des());
        txt_undpre.setValue(objKardexCab.getPro_undpre());
        txt_stkinie.setValue((int) (objKardexCab.getSkt_ini() / objKardexCab.getPro_undpre()));
        txt_stkinif.setValue((int) (objKardexCab.getSkt_ini() % objKardexCab.getPro_undpre()));
        txt_stkacte.setValue((int) (objKardexCab.getSkt_fin() / objKardexCab.getPro_undpre()));
        txt_stkactf.setValue((int) (objKardexCab.getSkt_fin() % objKardexCab.getPro_undpre()));
    }

    public void llenarTotales() {
        double data[];
        data = calcularTotales();
        double totent, totsal;
        totent = data[0] * txt_undpre.getValue() + data[1];
        totsal = data[2] * txt_undpre.getValue() + data[3];

        txt_etotent.setValue((int) (totent / txt_undpre.getValue()));
        txt_etotfra.setValue((int) (totent % txt_undpre.getValue()));
        txt_stotent.setValue((int) (totsal / txt_undpre.getValue()));
        txt_stotfra.setValue((int) (totsal % txt_undpre.getValue()));
    }

    public void limpiarLista() {
        objlstKardex = null;
        objlstKardex = new ListModelList<Kardex>();
        lst_kardex.setModel(objlstKardex);
    }

    public void limpiarCampos() {
        txt_prodidf.setValue("");
        txt_prodidp.setValue("");
        txt_proddesp.setValue("");
        txt_proddesf.setValue("");

    }

    public void limpiarCamposDetalle() {
        txt_prodid.setValue(null);
        txt_proddes.setValue(null);
        txt_undpre.setValue(null);
        txt_stkinie.setValue(null);
        txt_stkinif.setValue(null);
        txt_stkacte.setValue(null);
        txt_stkactf.setValue(null);
        txt_etotent.setValue(null);
        txt_etotfra.setValue(null);
        txt_stotent.setValue(null);
        txt_stotfra.setValue(null);
    }

    public double[] calcularTotales() {
        double totales[] = new double[4];
        for (int i = 0; i < objlstKardex.getSize(); i++) {
            if ("E".equals(objlstKardex.get(i).getTip_mov())) {
                totales[0] = totales[0] + ((Kardex) objlstKardex.get(i)).getMovent();
                totales[1] = totales[1] + ((Kardex) objlstKardex.get(i)).getMovfra();
            } else {
                totales[2] = totales[2] + ((Kardex) objlstKardex.get(i)).getMovent();
                totales[3] = totales[3] + ((Kardex) objlstKardex.get(i)).getMovfra();
            }
        }
        return totales;
    }

    public String verificar() {
        if (txt_almid.getValue() == null) {
            s_valor = "El campo 'Almacen' es obligatorio";
            campo = "Almacen";
        } else if (s_filtro.equals("Q")) {
            s_valor = "Debe elegir algun filtro de 'Producto' es obligatorio";
            campo = "Filtro Productos";
        } else {
            if ("0".equals(s_filtro)) {
                if (txt_prodidp.getValue().isEmpty()) {
                    s_valor = "El campo 'Producto Inicial' es obligatorio";
                    campo = "Producto Inicial";
                } else {
                    s_valor = "";
                }
            } else if ("1".equals(s_filtro)) {
                if (txt_prodidp.getValue().isEmpty()) {
                    s_valor = "El campo 'Producto Inicial' es obligatorio";
                    campo = "Producto Inicial";
                } else if (txt_prodidf.getValue().isEmpty()) {
                    s_valor = "El campo 'Producto Final' es obligatorio";
                    campo = "Producto Final";
                } else {
                    s_valor = "";
                }
            } else {
                s_valor = "";
            }
        }
        return s_valor;
    }

    public void habilitadetalle(boolean activa) {
        rbg_detalle.getItems().get(0).setDisabled(activa);
        rbg_detalle.getItems().get(1).setDisabled(activa);
    }
}
