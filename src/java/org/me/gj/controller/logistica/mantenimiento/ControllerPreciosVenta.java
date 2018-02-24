package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.Precio;
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
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerPreciosVenta extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_busqueda, cb_estado, cb_lpcprevman;
    @Wire
    Textbox txt_busqueda, txt_prodes, txt_prenomrep, txt_proid, txt_usuadd, txt_usumod;
    @Wire
    Doublebox txt_preprecio, txt_predsctoefe, txt_predsctocre;
    @Wire
    Spinner sp_preord;
    @Wire
    Listbox lst_precioven;
    @Wire
    Checkbox chk_preest;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Precio> objlstPreciosVenta;
    DaoPrecios objDaoPrecios;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    Precio objPrecio;
    //Variables publicas    
    String s_estado = "Q", s_mensaje = "", modoEjecucion;
    int i_sel, i_valor, emp_id, suc_id;
    String campo = "";
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential;
    private static final Logger LOGGER = Logger.getLogger(ControllerPreciosVenta.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_precioven")
    public void llenaRegistros() throws SQLException {
        //int lp_key = 0;
        objDaoPrecios = new DaoPrecios();
        objlstPreciosVenta = objDaoPrecios.lstPrecios(emp_id, suc_id, "%%", "%%", "2", "%%");
        lst_precioven.setModel(objlstPreciosVenta);
        cb_lpcprevman.setModel(new DaoListaPrecios().listaPrecios(emp_id, suc_id, 1, 2));
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new DaoAccesos().Verifica_Permisos_Transacciones(10103020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Precio de Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Precio de Venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Precio de Venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Precio de Venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Precio de Venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Precio de Venta");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Precio de Venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Precio de Venta");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Precio de Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Precio de Venta");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstPreciosVenta = null;
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el código producto " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción producto " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la código proveedor " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción proveedor " + s_consulta + " para su busqueda");
        }
        
        objlstPreciosVenta = objDaoPrecios.busquedaPreciosVenta(emp_id, suc_id, i_seleccion, s_consulta, i_estado);

        if (objlstPreciosVenta.getSize() > 0) {
            lst_precioven.setModel(objlstPreciosVenta);
        } else {
            objlstPreciosVenta = null;
            lst_precioven.setModel(objlstPreciosVenta);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onSelect=#lst_precioven")
    public void seleccionaRegistro() throws SQLException {
        objPrecio = (Precio) lst_precioven.getSelectedItem().getValue();
        if (objPrecio == null) {
            objPrecio = objlstPreciosVenta.get(i_sel + 1);
        }
        i_sel = lst_precioven.getSelectedIndex();
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
        s_estado = "N";
        cb_lpcprevman.focus();
        cb_lpcprevman.select();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_precioven.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoPrecios.validaMovimientos(objPrecio);
            i_valor = objDaoPrecios.i_flagErrorBD;
            if (i_valor > 0) {
                Messagebox.show(objDaoPrecios.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                txt_proid.setDisabled(true);
                cb_lpcprevman.focus();
                cb_lpcprevman.select();
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
                        if (campo.equals("Lista de Precios")) {
                            cb_lpcprevman.focus();
                        } else if (campo.equals("Producto")) {
                            txt_proid.focus();
                        } else if (campo.equals("Precio")) {
                            txt_preprecio.focus();
                        } else if (campo.equals("Descuento Efectivo")) {
                            txt_predsctoefe.focus();
                        } else if (campo.equals("Descuento Credito")) {
                            txt_predsctocre.focus();
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
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    objlstPreciosVenta =  new ListModelList<Precio>();
                                    objlstPreciosVenta.clear();
                                    //objlstPreciosVenta = null;
                                    //objlstPreciosVenta = objDaoPrecios.lstPrecios(emp_id, suc_id, "%%", "%%", "2", "%%");
                                    busquedaRegistros();
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    lst_precioven.setModel(objlstPreciosVenta);
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_precioven.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoPrecios.validaMovimientos(objPrecio);
            i_valor = objDaoPrecios.i_flagErrorBD;
            if (i_valor > 0) {
                Messagebox.show(objDaoPrecios.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea eliminar este precio de venta?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoPrecios.eliminarPrecio(objPrecio);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstPreciosVenta.clear();
                                        objlstPreciosVenta = objDaoPrecios.lstPrecios(emp_id, suc_id, "%%", "%%", "2", "%%");
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_precioven.setModel(objlstPreciosVenta);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        limpiarCampos();
                                        limpiaAuditoria();
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
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_precioven.setSelectedIndex(-1);
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
        if (objlstPreciosVenta == null || objlstPreciosVenta.isEmpty()) {
            Messagebox.show("No hay registros de precios de venta para imprimir ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionPven.zul", null, objMapObjetos);
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
    
    @Listen("onCtrlKey=#w_manpreven")
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

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstPreciosVenta = new ListModelList<Precio>();
            objlstPreciosVenta = objDaoPrecios.lstPrecios(emp_id, suc_id, "%%", "%%", "2", "%%");
            lst_precioven.setModel(objlstPreciosVenta);
        }
    }

    @Listen("onOK=#txt_proid")
    public void lov_productos() throws SQLException {
        if (cb_lpcprevman.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Lista de Precio");
        } else {
            if (bandera == false) {
                bandera = true;
                if (txt_proid.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "PreVen";
                    mapeo.put("txt_proid", txt_proid);
                    mapeo.put("txt_prodes", txt_prodes);
                    mapeo.put("proveedor", "");
                    mapeo.put("validaBusqueda", modoEjecucion);
                    mapeo.put("controlador", "ControllerPreciosVenta");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, mapeo);
                    window.doModal();
                } else {
                    txt_preprecio.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_proid")
    public void validaProducto() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (!txt_proid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_proid.setValue("");
                            txt_prodes.setValue("");
                            txt_proid.focus();
                        }
                    }
                });
            } else {
                ParametrosSalida objParam;
                String c_pro_id = txt_proid.getValue();
                int i_lp_key = cb_lpcprevman.getSelectedItem().getValue();
                objParam = objDaoPrecios.verificarPrecio(emp_id, suc_id, c_pro_id, "%%", i_lp_key, 2);
                txt_proid.setValue(objParam.getCodigoRegistro());
                txt_prodes.setValue(objParam.getDescripcionRegistro());
                if (objParam.getFlagIndicador() == 0) {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Producto " + objParam.getCodigoRegistro() + " y ha encontrado 1 registro(s)");
                } else {
                    Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_proid.setValue("");
                                txt_prodes.setValue("");
                                txt_proid.focus();
                            }
                        }
                    });
                }
            }
        } else {
            txt_prodes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#cb_lpcprevman")
    public void next_producto() {
        txt_proid.focus();
    }

    /*@Listen("onBlur=#cb_lpcprevman")
    public void valida_producto() {
        txt_proid.setValue("");
        txt_prodes.setValue("");
    }*/

    @Listen("onOK=#txt_preprecio")
    public void next_descEfectivo() {
        txt_predsctoefe.focus();
    }

    @Listen("onOK=#txt_predsctoefe")
    public void next_descCredito() {
        txt_predsctocre.focus();
    }

    @Listen("onOK=#txt_predsctocre")
    public void next_reporte() {
        txt_prenomrep.focus();
    }

    @Listen("onOK=#txt_prenomrep")
    public void next_orden() {
        sp_preord.focus();
    }

    @Listen("onOK=#sp_preord")
    public void next_guardar() {
        botonGuardar();
    }

    //Eventos Otros 
    public void llenarCampos() {
        cb_lpcprevman.setSelectedItem(Utilitarios.valorPorTexto1(cb_lpcprevman, objPrecio.getLp_key()));
        txt_proid.setValue(objPrecio.getPro_id());
        txt_prodes.setValue(objPrecio.getPro_des());
        txt_predsctocre.setValue(objPrecio.getPre_igv());
        txt_predsctoefe.setValue(objPrecio.getPre_valvent());
        txt_preprecio.setValue(objPrecio.getPre_venta());
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
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
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
        double precio, dsctoe, dsctoc;
        precio = (txt_preprecio.getValue() != null ? txt_preprecio.getValue() : 0);
        dsctoe = (txt_predsctoefe.getValue() != null ? txt_predsctoefe.getValue() : 0);
        dsctoc = (txt_predsctocre.getValue() != null ? txt_predsctocre.getValue() : 0);
        if (cb_lpcprevman.getSelectedIndex() == -1) {
            s_valor = "El Campo Lista de Precios es Obligatorio";
            campo = "Lista de Precios";
        } else if (txt_proid.getValue().isEmpty()) {
            s_valor = "El Campo Producto es Obligatorio";
            campo = "Producto";
        } else if (txt_preprecio.getValue() == null) {
            s_valor = "El Campo Precio es Obligatorio";
            campo = "Precio";
        } else if (txt_predsctoefe.getValue() == null) {
            s_valor = "El Campo Descuento Efectivo es Obligatorio";
            campo = "Descuento Efectivo";
        } else if (txt_predsctocre.getValue() == null) {
            s_valor = "El Campo Descuento Credito es Obligatorio";
            campo = "Descuento Credito";
        } else if (precio <= 0) {
            s_valor = "El Campo Precio es Inválido";
            campo = "Precio";
        } else if (dsctoe < 0) {
            s_valor = "El Campo Descuento Efectivo es Inválido";
            campo = "Descuento Efectivo";
        } else if (dsctoc < 0) {
            s_valor = "El Campo Descuento Credito es Inválido";
            campo = "Descuento Credito";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        cb_lpcprevman.setSelectedIndex(-1);
        txt_proid.setValue("");
        txt_prodes.setValue("");
        txt_predsctoefe.setValue(null);
        txt_predsctocre.setValue(null);
        txt_preprecio.setValue(null);
        txt_prenomrep.setValue("");
        sp_preord.setValue(0);
        chk_preest.setChecked(true);
        chk_preest.setLabel("ACTIVO");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        cb_lpcprevman.setDisabled(b_valida);
        txt_proid.setDisabled(b_valida);
        txt_predsctoefe.setDisabled(b_valida);
        txt_predsctocre.setDisabled(b_valida);
        txt_preprecio.setDisabled(b_valida);
        txt_prenomrep.setDisabled(b_valida);
        chk_preest.setDisabled(b_valida);
        sp_preord.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        long l_pre_key = objPrecio.getPre_key();
        int i_lp_key = cb_lpcprevman.getSelectedItem().getValue();
        String c_pro_id = txt_proid.getValue();
        int i_pre_est = chk_preest.isChecked() ? 1 : 2;
        double d_pre_val = txt_predsctoefe.getValue();
        double d_pre_igv = txt_predsctocre.getValue();
        double d_pre_ven = txt_preprecio.getValue();
        String s_pre_nomrep = txt_prenomrep.getValue().toUpperCase();
        int i_pre_ord = sp_preord.getValue();
        String s_pre_usuadd = objUsuCredential.getCuenta();
        String s_pre_usumod = objUsuCredential.getCuenta();
        return new Precio(l_pre_key, emp_id, suc_id, c_pro_id, i_lp_key, 2, d_pre_val, d_pre_igv, d_pre_ven, i_pre_est, s_pre_nomrep,
                i_pre_ord, s_pre_usuadd, s_pre_usumod);
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstPreciosVenta = null;
        objlstPreciosVenta = new ListModelList<Precio>();
        lst_precioven.setModel(objlstPreciosVenta);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
