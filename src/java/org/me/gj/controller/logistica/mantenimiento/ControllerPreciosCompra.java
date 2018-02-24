package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoImpuesto;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.*;

public class ControllerPreciosCompra extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaprecio, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_provdes, txt_proddes, txt_provdesman, txt_prodesman, txt_procodori,
            txt_prenomrep, txt_proidman, txt_provid, txt_prodid, txt_providman, txt_usuadd, txt_usumod, txt_linid, txt_lindes, txt_linidman, txt_lindesman;
    @Wire
    Checkbox chk_preest;
    @Wire
    Button tbbtn_btn_buscar;
    @Wire
    Doublebox txt_preven, txt_igv, txt_preval;
    @Wire
    Spinner sp_preord;
    @Wire
    Listbox lst_precio;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Precio> objlstPrecios;
    DaoPrecios objDaoPrecios;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Precio objPrecio;
    DaoImpuesto objDaoImpuesto;
    Utilitarios objUtil;
    //Variables publicas    
    String s_estado = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    int emp_id, suc_id;
    double impto;
    int valor;
    String campo = "";
    int i_sel;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential;
    private static final Logger LOGGER = Logger.getLogger(ControllerPreciosCompra.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_precio")
    public void llenaRegistros() throws SQLException {
        objDaoImpuesto = new DaoImpuesto();
        objDaoPrecios = new DaoPrecios();
        objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, "", "", "1", "%%");
        lst_precio.setModel(objlstPrecios);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10103010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Precio de compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Precio de compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Precio de compra");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Precio de compra");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Precio de compra");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Precio de compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Precio de compra");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Precio de compra");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Precio de compra");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Precio de compra");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        objlstPrecios = new ListModelList<Precio>();
        /*if (txt_provid.getValue().isEmpty()) {
         Messagebox.show("Por favor ingrese un código de proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         txt_linid.setValue("");
         txt_lindes.setValue("");
         txt_prodid.setValue("");
         txt_proddes.setValue("");
         } else if (!txt_provid.getValue().isEmpty() && txt_linid.getValue().isEmpty() && txt_prodid.getValue().isEmpty()) {
         String prov_id = txt_provid.getValue();
         objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, prov_id, "%%", "1", "%%");
         } else if (!txt_provid.getValue().isEmpty() && !txt_linid.getValue().isEmpty() && txt_prodid.getValue().isEmpty()) {
         String prov_id = txt_provid.getValue();
         String lp_id = txt_linid.getValue();
         objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, prov_id, lp_id, "1", "%%");
         } else if (!txt_provid.getValue().isEmpty() && !txt_linid.getValue().isEmpty() && !txt_prodid.getValue().isEmpty()) {
         String prov_id = txt_provid.getValue();
         String lp_id = txt_linid.getValue();
         String prod_id = txt_prodid.getValue();
         objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, prov_id, lp_id, "1", prod_id);
         } else {
         String prov_id = txt_provid.getValue();
         objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, prov_id, "%%", "1", "%%");
         }*/

        String prov_id = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
        String lp_id = txt_linid.getValue().isEmpty() ? "%%" : txt_linid.getValue();
        String prod_id = txt_prodid.getValue().isEmpty() ? "%%" : txt_prodid.getValue();
        objlstPrecios = objDaoPrecios.lstPrecios(emp_id, suc_id, prov_id, lp_id, "1", prod_id);

        if (objlstPrecios.getSize() > 0) {
            lst_precio.setModel(objlstPrecios);
        } else {
            objlstPrecios = null;
            lst_precio.setModel(objlstPrecios);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
    }

    @Listen("onSelect=#lst_precio")
    public void seleccionaRegistro() throws SQLException {
        objPrecio = (Precio) lst_precio.getSelectedItem().getValue();
        if (objPrecio == null) {
            objPrecio = objlstPrecios.get(i_sel + 1);
        }
        i_sel = lst_precio.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objPrecio.getPre_ord());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objPrecio = new Precio();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_preest.setDisabled(true);
        chk_preest.setChecked(true);
        txt_providman.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_precio.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoPrecios.validaMovimientos(objPrecio);
            valor = objDaoPrecios.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoPrecios.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                txt_providman.setDisabled(true);
                txt_proidman.setDisabled(true);
                txt_provid.focus();
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Proveedor")) {
                            txt_providman.focus();
                        } else if (campo.equals("Lista de Precios")) {
                            txt_linidman.focus();
                        } else if (campo.equals("Producto")) {
                            txt_proidman.focus();
                        } else if (campo.equals("Valor Venta")) {
                            txt_preval.focus();
                        } else if (campo.equals("Valor IGV")) {
                            txt_igv.focus();
                        } else if (campo.equals("Precio Venta")) {
                            txt_preven.focus();
                        }
                    }
                }
            });
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstPrecios = new ListModelList<Precio>();
                                ParametrosSalida objParamSalida;
                                objPrecio = (Precio) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoPrecios.insertarPrecio(objPrecio);
                                } else {
                                    objParamSalida = objDaoPrecios.modificarPrecio(objPrecio);
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    //validacion de guardar/nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    //
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    /*txt_linid.setValue("");
                                     txt_lindes.setValue("");
                                     txt_provid.setValue("");
                                     txt_provdes.setValue("");*/
                                    limpiarCampos();
                                    /*lst_precio.setModel(objlstPrecios);*/
                                    busquedaRegistros();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_precio.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoPrecios.validaMovimientos(objPrecio);
            valor = objDaoPrecios.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoPrecios.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea eliminar este precio de compra?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoPrecios.eliminarPrecio(objPrecio);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstPrecios.clear();
                                        busquedaRegistros();
                                        lst_precio.setModel(objlstPrecios);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                    }
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {

                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_precio.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstPrecios == null || objlstPrecios.isEmpty()) {
            Messagebox.show("No hay registros de precios de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionPcom.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    /*@Listen("onCtrlKey=#tabbox_lstpcompra")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
        }
    }*/
    
    @Listen("onCtrlKey=#w_manpre")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo(); 
                }
                break;
            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    botonEliminar();
                }
                break;
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;
        }
    }

    @Listen("onBlur=#txt_preval")
    public void validaPrecio() throws SQLException {
        if (txt_preval.getValue() != null) {
            if (!"".equals(txt_prodesman.getValue())) {
                impto = objDaoImpuesto.obtieneValorImpuesto(txt_proidman.getValue());
                double d_valvent = txt_preval.getValue();
                txt_igv.setValue(d_valvent * (impto / 100));
                txt_preven.setValue(d_valvent + (d_valvent * (impto / 100)));
            } else {
                Messagebox.show("Por favor ingrese el producto para ingresar su valor venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_preval.setValue(null);
                            txt_proidman.focus();
                        }
                    }
                });
            }

        }
    }

    @Listen("onOK=#txt_preval")
    public void txt_nomrep() {
        if (txt_preval.getValue() == null) {
            Messagebox.show("Por favor ingrese el valor venta del producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            txt_prenomrep.focus();
        }
    }

    @Listen("onOK=#txt_provid")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "PreCom";
                mapeo.put("txt_proidman", txt_provid);
                mapeo.put("txt_prodesman", txt_provdes);
                mapeo.put("tipo", "1");
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerPreciosCompra");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            } else {
                txt_linid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_linid.setValue("");
                            txt_lindes.setValue("");
                            txt_provid.focus();
                        }
                    }
                });
            } else {
                String prov_id = Utilitarios.lpad(txt_provid.getValue(), 8, "0");
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(prov_id, "1");
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_provid.setValue("");
                                txt_provdes.setValue("");
                                txt_linid.setValue("");
                                txt_lindes.setValue("");
                                txt_provid.focus();
                            }
                        }
                    });
                } else {
                    //ListModelList<ListaPrecio> objAuxiliar;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    //limpiarCampos();
                    //objlstPrecios = null;
                    //lst_precio.setModel(objlstPrecios);
                    //txt_linid.setValue("");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                    //objAuxiliar = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, objProveedor.getProv_key());
                    //objAuxiliar.add(new ListaPrecio(-1, ""));
                    //txt_linid.setValue("");
                    //txt_lindes.setValue("");
                }
            }
        } else {
            txt_provdes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_prodid")
    public void lov_productos() throws SQLException {
        String s_valida = verificarCamposProveedor();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Proveedor")) {
                            txt_provid.focus();
                        } else if (campo.equals("Lista de Precios")) {
                            txt_linid.focus();
                        }
                    }
                }
            });
        } else {
            if (bandera == false) {
                bandera = true;
                if (txt_prodid.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "PreCom";
                    mapeo.put("txt_prodid", txt_prodid);
                    mapeo.put("txt_proddes", txt_proddes);
                    mapeo.put("proveedor", txt_provid.getValue());
                    mapeo.put("txt_linid", txt_linid);
                    mapeo.put("txt_provid", txt_provid);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    mapeo.put("controlador", "ControllerPreciosCompra");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                    window.doModal();
                } else {
                    tbbtn_btn_buscar.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_prodid")
    public void validaProducto() throws SQLException {
        if (!txt_prodid.getValue().isEmpty()) {
            if (!txt_prodid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_prodid.setValue("");
                            txt_proddes.setValue("");
                            txt_prodid.focus();
                        }
                    }
                });
            } else {
                String prod_id = Utilitarios.lpad(("".equals(txt_prodid.getValue()) ? "0" : txt_prodid.getValue()), 9, "0");
                String prov_id = Utilitarios.lpad(("".equals(txt_provid.getValue()) ? "0" : txt_provid.getValue()), 8, "0");
                Productos objProducto = new DaoProductos().buscarProducto(prod_id, prov_id);
                if (objProducto == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_prodid.setValue("");
                                txt_proddes.setValue("");
                                txt_prodid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                    txt_prodid.setValue(objProducto.getPro_id());
                    txt_proddes.setValue(objProducto.getPro_des());
                }
            }
        } else {
            txt_proddes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_linid")
    public void lov_listas() throws SQLException {
        if (txt_provid.getValue().isEmpty()) {
            Messagebox.show("Por favor verifique los datos del proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_provid.focus();
                    }
                }
            });
        } else {
            if (bandera == false) {
                bandera = true;
                if (txt_linid.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "PreCom";
                    mapeo.put("txt_provid", txt_provid);
                    mapeo.put("txt_linid", txt_linid);
                    mapeo.put("txt_lindes", txt_lindes);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    mapeo.put("controlador", "ControllerPreciosCompra");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioCompra.zul", null, mapeo);
                    window.doModal();
                } else {
                    //txt_provid.focus();
                    txt_prodid.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_linid")
    public void validaListas() throws SQLException {
        if (!txt_linid.getValue().isEmpty()) {
            if (!txt_linid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_linid.setValue("");
                            txt_lindes.setValue("");
                            txt_linid.focus();
                        }
                    }
                });
            } else {
                long prov_key = Long.parseLong(("".equals(txt_provid.getValue()) ? "0" : txt_provid.getValue()));
                int lpc_key = Integer.parseInt(("".equals(txt_linid.getValue()) ? "0" : txt_linid.getValue()));
                ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_key, lpc_key);
                if (objLpCompra == null) {
                    Messagebox.show("El código de la lista de precio no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_linid.setValue("");
                                txt_lindes.setValue("");
                                txt_linid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objLpCompra.getLp_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_linid.setValue(objLpCompra.getLp_id());
                    txt_lindes.setValue(objLpCompra.getLp_des());
                }
            }
        } else {
            txt_lindes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_providman")
    public void lov_proveedoresMant() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_providman.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "manPreCom";
                mapeo.put("txt_proidman", txt_providman);
                mapeo.put("txt_prodesman", txt_provdesman);
                mapeo.put("tipo", "1");
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerPreciosCompra");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            } else {
                txt_linidman.focus();
            }
        }
    }

    @Listen("onBlur=#txt_providman")
    public void validaProveedoresMant() throws SQLException {
        if (!txt_providman.getValue().isEmpty()) {
            if (!txt_providman.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_providman.setValue("");
                            txt_provdesman.setValue("");
                            txt_providman.focus();
                        }
                    }
                });
            } else {
                String prov_id = Utilitarios.lpad(txt_providman.getValue(), 8, "0");
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(prov_id, "1");
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_providman.setValue("");
                                txt_provdesman.setValue("");
                                txt_providman.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_providman.setValue(objProveedor.getProv_id());
                    txt_provdesman.setValue(objProveedor.getProv_razsoc());
                }
            }
        } else {
            txt_linidman.setValue("");
            txt_lindesman.setValue("");
            txt_proidman.setValue("");
            txt_prodesman.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_linidman")
    public void lov_listasMant() throws SQLException {
        if (txt_providman.getValue().isEmpty()) {
            Messagebox.show("Por favor verifique los datos del proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_providman.focus();
                        txt_providman.select();
                    }
                }
            });
        } else {
            if (bandera == false) {
                bandera = true;
                if (txt_linidman.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "manPreCom";
                    mapeo.put("txt_providman", txt_providman);
                    mapeo.put("txt_linidman", txt_linidman);
                    mapeo.put("txt_lindesman", txt_lindesman);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    mapeo.put("controlador", "ControllerPreciosCompra");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioCompra.zul", null, mapeo);
                    window.doModal();
                } else {
                    txt_proidman.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_linidman")
    public void validaListasMant() throws SQLException {
        if (!txt_linidman.getValue().isEmpty()) {
            if (!txt_linidman.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_linidman.setValue("");
                            txt_lindesman.setValue("");
                            txt_linidman.focus();
                        }
                    }
                });
            } else {
                long prov_key = Long.parseLong(("".equals(txt_providman.getValue()) ? "0" : txt_providman.getValue()));
                int lpc_key = Integer.parseInt(("".equals(txt_linidman.getValue()) ? "0" : txt_linidman.getValue()));
                ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_key, lpc_key);
                if (objLpCompra == null) {
                    Messagebox.show("El código de la lista de precio no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_linidman.setValue("");
                                txt_lindesman.setValue("");
                                txt_linidman.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objLpCompra.getLp_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_linidman.setValue(objLpCompra.getLp_id());
                    txt_lindesman.setValue(objLpCompra.getLp_des());
                }
            }
        } else {
            txt_lindesman.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_proidman")
    public void lov_productosMant() throws SQLException {
        if (!txt_providman.getValue().isEmpty()) {
            if (!txt_linidman.getValue().isEmpty()) {
                if (bandera == false) {
                    bandera = true;
                    if (txt_proidman.getValue().isEmpty()) {
                        Map mapeo = new HashMap();
                        modoEjecucion = "manPreCom";
                        mapeo.put("txt_proidman", txt_proidman);
                        mapeo.put("txt_prodesman", txt_prodesman);
                        mapeo.put("proveedor", txt_providman.getValue());
                        mapeo.put("validaBusqueda", modoEjecucion);
                        mapeo.put("controlador", "ControllerPreciosCompra");
                        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                        window.doModal();
                    } else {
                        txt_preval.focus();
                    }
                }
            } else {
                Messagebox.show("Por favor ingrese la lista de precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_linidman.focus();
                        }
                    }
                });
            }
        } else {
            Messagebox.show("Por favor ingrese el proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_providman.focus();
                    }
                }
            });
        }
    }

    @Listen("onBlur=#txt_proidman")
    public void validaProductosMant() throws SQLException {
        if (!txt_proidman.getValue().isEmpty()) {
            if (!txt_providman.getValue().isEmpty()) {
                if (!txt_linidman.getValue().isEmpty()) {
                    if (!txt_proidman.getValue().matches("[0-9]*")) {
                        Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_proidman.setValue("");
                                    txt_prodesman.setValue("");
                                    txt_proidman.focus();
                                }
                            }
                        });
                    } else {
                        ParametrosSalida objParam;
                        String c_prod_id = txt_proidman.getValue();
                        String l_prov_key = txt_providman.getValue();
                        int i_lp_key = Integer.parseInt(("".equals(txt_linidman.getValue()) ? "0" : txt_linidman.getValue()));
                        objParam = objDaoPrecios.verificarPrecio(emp_id, suc_id, c_prod_id, l_prov_key, i_lp_key, 1);
                        txt_proidman.setValue(objParam.getCodigoRegistro());
                        txt_prodesman.setValue(objParam.getDescripcionRegistro());
                        if (objParam.getFlagIndicador() == 0) {
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objParam.getCodigoRegistro() + " y ha encontrado 1 registro(s)");
                        } else {
                            Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_proidman.setValue("");
                                        txt_prodesman.setValue("");
                                        txt_proidman.focus();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Messagebox.show("Por favor ingrese una lista de precios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_proidman.setValue("");
                                txt_prodesman.setValue("");
                                txt_linidman.focus();
                            }
                        }
                    });
                }
            } else {
                Messagebox.show("Por favor ingrese un proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_proidman.setValue("");
                            txt_prodesman.setValue("");
                            txt_providman.focus();
                        }
                    }
                });
            }
        } else {
            txt_prodesman.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros 
    public void validaBusqueda(InputEvent event) throws SQLException {

    }

    public void llenarCampos() {
        txt_providman.setValue(objPrecio.getProv_id());
        txt_provdesman.setValue(objPrecio.getProv_razsoc());
        txt_linidman.setValue(objPrecio.getLp_id());
        txt_lindesman.setValue(objPrecio.getLp_des());
        txt_proidman.setValue(objPrecio.getPro_id());
        txt_prodesman.setValue(objPrecio.getPro_des());
        txt_preval.setValue(objPrecio.getPre_valvent());
        txt_igv.setValue(objPrecio.getPre_igv());
        txt_preven.setValue(objPrecio.getPre_venta());
        txt_prenomrep.setValue(objPrecio.getPre_nomrep());
        sp_preord.setValue(objPrecio.getPre_ord());
        chk_preest.setChecked(objPrecio.isValor());
        chk_preest.setLabel(objPrecio.isValor() ? "ACTIVO" : "INACTIVO");
        txt_usuadd.setValue(objPrecio.getPre_usuadd());
        d_fecadd.setValue(objPrecio.getPre_fecadd());
        txt_usumod.setValue(objPrecio.getPre_usumod());
        d_fecmod.setValue(objPrecio.getPre_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprecio.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprecio.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public String verificar() {
        String s_valor;
        double valorven;
        valorven = (txt_preval.getValue() != null ? txt_preval.getValue() : 0);
        if (txt_providman.getValue().isEmpty()) {
            s_valor = "El Campo Proveedor es Obligatorio";
            campo = "Proveedor";
        } else if (txt_linidman.getValue().isEmpty()) {
            s_valor = "El Campo Lista de Precios es Obligatorio";
            campo = "Lista de Precios";
        } else if (txt_proidman.getValue().isEmpty()) {
            s_valor = "El Campo Producto es Obligatorio";
            campo = "Producto";
        } else if (txt_preval.getValue() == null) {
            s_valor = "El Campo Valor Venta es Obligatorio";
            campo = "Valor Venta";
        } else if (txt_igv.getValue() == null) {
            s_valor = "El Campo Valor IGV es Obligatorio";
            campo = "Valor IGV";
        } else if (txt_preven.getValue() == null) {
            s_valor = "El Campo Precio Venta es Obligatorio";
            campo = "Precio Venta";
        } else if (valorven <= 0) {
            s_valor = "El Campo Valor Venta es Invalido";
            campo = "Valor Venta";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        txt_providman.setValue("");
        txt_provdesman.setValue("");
        txt_lindesman.setValue("");
        txt_linidman.setValue("");
        txt_proidman.setValue("");
        txt_prodesman.setValue("");
        txt_preval.setValue(null);
        txt_igv.setValue(null);
        txt_preven.setValue(null);
        txt_prenomrep.setValue("");
        sp_preord.setValue(0);
        chk_preest.setChecked(true);
        chk_preest.setLabel("ACTIVO");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_providman.setDisabled(b_valida);
        txt_linidman.setDisabled(b_valida);
        txt_proidman.setDisabled(b_valida);
        txt_preval.setDisabled(b_valida);
        txt_igv.setDisabled(b_valida);
        txt_preven.setDisabled(b_valida);
        txt_prenomrep.setDisabled(b_valida);
        chk_preest.setDisabled(b_valida);
        sp_preord.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        long l_pre_key = objPrecio.getPre_key();
        int i_lp_key = Integer.parseInt(txt_linidman.getValue());
        String c_pro_id = txt_proidman.getValue();
        int i_pre_est = chk_preest.isChecked() ? 1 : 2;
        double d_pre_val = txt_preval.getValue();
        double d_pre_igv = txt_igv.getValue();
        double d_pre_ven = txt_preven.getValue();
        String s_pre_nomrep = txt_prenomrep.getValue().toUpperCase();
        int i_pre_ord = sp_preord.getValue();
        String s_pre_usuadd = objUsuCredential.getCuenta();
        String s_pre_usumod = objUsuCredential.getCuenta();
        return new Precio(l_pre_key, emp_id, suc_id, c_pro_id, i_lp_key, 1, d_pre_val, d_pre_igv, d_pre_ven, i_pre_est, s_pre_nomrep,
                i_pre_ord, s_pre_usuadd, s_pre_usumod);
    }

    public String verificarCamposProveedor() {
        String s_valor = "";
        if (txt_provid.getValue().isEmpty()) {
            s_valor = "El Campo Proveedor es Obligatorio";
            campo = "Proveedor";
        } else if (txt_linid.getValue().isEmpty()) {
            s_valor = "El Campo Lista de Precios es Obligatorio";
            campo = "Lista de Precios";
        }
        return s_valor;
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstPrecios = null;
        objlstPrecios = new ListModelList<Precio>();
        lst_precio.setModel(objlstPrecios);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
