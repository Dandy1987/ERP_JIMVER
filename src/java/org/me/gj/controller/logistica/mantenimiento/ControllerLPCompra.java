package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.Sessions;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.apache.log4j.Logger;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class ControllerLPCompra extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_clonacion;
    @Wire
    Textbox txt_provid, txt_busqueda, txt_lpcdes, txt_lpcnomrep, txt_provdes, txt_usuadd, txt_usumod, txt_lpcid; //
    @Wire
    Spinner sp_lpcord;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Checkbox chk_lpcest;
    @Wire
    Listbox lst_pcompra;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Doublebox db_descgen, db_descfinan;
    //Instancias de Objetos    
    ListModelList<ListaPrecio> objlstpcompra, objlstAux;
    ListaPrecio objLpcompra;
    DaoListaPrecios objDaoLpCompra;
    Accesos objAccesos;
    //Variables publicas    
    String s_estado = "Q";
    String s_mensaje = "";
    String campo = "";
    int valor;
    String modoEjecucion;
    public static boolean bandera = false;
    int i_sel, emp_id, suc_id;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential;
    private static final Logger LOGGER = Logger.getLogger(ControllerLPCompra.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_lstpcompra")
    public void llenaRegistros() throws SQLException {
        objDaoLpCompra = new DaoListaPrecios();
        objlstpcompra = objDaoLpCompra.listaPrecios(emp_id, suc_id, 1, 1);
        lst_pcompra.setModel(objlstpcompra);
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
        objAccesos = new DaoAccesos().Verifica_Permisos_Transacciones(10104010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Lista Precio de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Lista Precio de Compra con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una Lista Precio de Compra");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una Lista Precio de Compra");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Lista Precio de Compra");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Lista Precio de Compra");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Lista Precio de Compra");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Lista Precio de Compra");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la Lista Precio de Compra");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la Lista Precio de Compra");
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
        objlstpcompra = new ListModelList<ListaPrecio>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        }
        if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        }
        if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstpcompra = objDaoLpCompra.busquedaLpPrecio(emp_id, suc_id, 1, i_seleccion, s_consulta, i_estado);

        if (objlstpcompra.getSize() > 0) {
            lst_pcompra.setModel(objlstpcompra);
        } else {
            objlstpcompra = null;
            lst_pcompra.setModel(objlstpcompra);
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

    @Listen("onSelect=#lst_pcompra")
    public void seleccionaRegistro() throws SQLException {
        objLpcompra = (ListaPrecio) lst_pcompra.getSelectedItem().getValue();
        if (objLpcompra == null) {
            objLpcompra = objlstpcompra.get(i_sel + 1);
        }
        i_sel = lst_pcompra.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objLpcompra.getLp_id());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objLpcompra = new ListaPrecio();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_lpcest.setDisabled(true);
        chk_lpcest.setChecked(true);
        txt_provid.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_pcompra.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_provid.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opción modificar para actualizar un registro");
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
                            txt_provid.focus();
                        } else if (campo.equals("Lista")) {
                            txt_lpcdes.focus();
                        } else if (campo.equals("descgeneral")) {
                            db_descgen.focus();
                        } else if (campo.equals("descfinanciero")) {
                            db_descfinan.focus();
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
                                objLpcompra = (ListaPrecio) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoLpCompra.insertarListaPrecio(objLpcompra);
                                } else {
                                    objParamSalida = objDaoLpCompra.modificarListaPrecio(objLpcompra);
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
                                    //objlstpcompra.clear();
                                    objlstpcompra = objDaoLpCompra.listaPrecios(emp_id, suc_id, 1, 1);
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    lst_pcompra.setModel(objlstpcompra);
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_pcompra.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoLpCompra.validaMovimientos(objLpcompra);
            valor = objDaoLpCompra.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoLpCompra.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea eliminar esta lista de precios de compra?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoLpCompra.eliminarListaPrecio(objLpcompra);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstpcompra.clear();
                                        objlstpcompra = objDaoLpCompra.listaPrecios(emp_id, suc_id, 1, 1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_pcompra.setModel(objlstpcompra);
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
                            lst_pcompra.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstpcompra == null || objlstpcompra.isEmpty()) {
            Messagebox.show("No hay registros de lista de precios de compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionLPCompra.zul", null, objMapObjetos);
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
    
    @Listen("onCtrlKey=#w_manlin")
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
            objlstpcompra = new ListModelList<ListaPrecio>();
            objlstpcompra = objDaoLpCompra.listaPrecios(emp_id, suc_id, 0, 1);
            lst_pcompra.setModel(objlstpcompra);
        }
    }

    @Listen("onOK=#txt_provid")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "LPreCom";
                mapeo.put("txt_proidman", txt_provid);
                mapeo.put("txt_prodesman", txt_provdes);
                mapeo.put("tipo", "1");
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerLPCompra");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            } else {
                txt_lpcdes.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedor() throws SQLException {
        txt_provdes.setValue("");
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_provid.setValue("");
            } else {
                String prov_id = txt_provid.getValue();
                txt_provid.setValue(Utilitarios.lpad(prov_id, 8, "0"));
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(txt_provid.getValue(), "1");
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_provid.setValue("");
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_lpcdes")
    public void next_descgeneral() {
        db_descgen.focus();
    }

    @Listen("onOK=#db_descgen")
    public void next_descfinan() {
        db_descfinan.focus();
    }

    @Listen("onBlur=#db_descgen")
    public void valida_descgen() {
        if (db_descgen.getValue() == null) {
            db_descgen.setValue(0.0);
        }
    }

    @Listen("onOK=#db_descfinan")
    public void next_reporte() {
        txt_lpcnomrep.focus();
    }

    @Listen("onBlur=#db_descfinan")
    public void valida_descfinan() {
        if (db_descfinan.getValue() == null) {
            db_descfinan.setValue(0.0);
        }
    }

    @Listen("onOK=#txt_lpcnomrep")
    public void next_orden() {
        sp_lpcord.focus();
    }

    @Listen("onClickListaPrecioCompra=#lst_pcompra")
    public void goManCliente(ForwardEvent evt) {
        tbbtn_clonacion = (Toolbarbutton) evt.getOrigin().getTarget();
        objlstAux = (ListModelList) lst_pcompra.getModel();
        Listitem item = (Listitem) tbbtn_clonacion.getParent().getParent();

        String lp_id = objlstAux.get(item.getIndex()).getLp_id();
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
        window.doModal();
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_lpcid.setValue(objLpcompra.getLp_id());
        txt_lpcdes.setValue(objLpcompra.getLp_des());
        chk_lpcest.setChecked(objLpcompra.isValor());
        txt_lpcnomrep.setValue(objLpcompra.getLp_nomrep());
        txt_provid.setValue(objLpcompra.getProv_id());
        txt_provdes.setValue(objLpcompra.getProv_razsoc());
        db_descgen.setValue(objLpcompra.getLp_descgen());
        db_descfinan.setValue(objLpcompra.getLp_descfinan());
        sp_lpcord.setValue(objLpcompra.getLp_ord());
        txt_usuadd.setValue(objLpcompra.getLp_usuadd());
        d_fecadd.setValue(objLpcompra.getLp_fecadd());
        txt_usumod.setValue(objLpcompra.getLp_usumod());
        d_fecmod.setValue(objLpcompra.getLp_fecmod());
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
        if (txt_provid.getValue().isEmpty()) {
            s_valor = "El campo Proveedor es Obligatorio";
            campo = "Proveedor";
        } else if (txt_lpcdes.getValue().isEmpty() || txt_lpcdes.getText().trim().length() == 0) {
            s_valor = "El campo Lista es Obligatorio";
            campo = "Lista";
        } else if (db_descgen.getValue() > 100) {
            s_valor = "El descuento general debe ser menor a 100";
            campo = "descgeneral";
        } else if (db_descgen.getValue() < 0.0) {
            s_valor = "El descuento general debe ser mayor a 0";
            campo = "descgeneral";
        } else if (db_descfinan.getValue() > 100) {
            s_valor = "El descuento financiero debe ser menor a 100";
            campo = "descfinanciero";
        } else if (db_descfinan.getValue() < 0.0) {
            s_valor = "El descuento financiero debe ser mayor a 0";
            campo = "descfinanciero";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        txt_lpcid.setValue("");
        txt_lpcdes.setValue("");
        txt_provid.setValue("");
        txt_provdes.setValue("");
        db_descgen.setValue(0.0);
        db_descfinan.setValue(0.0);
        txt_lpcnomrep.setValue("");
        sp_lpcord.setValue(0);
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
        txt_provid.setDisabled(b_valida);
        txt_lpcdes.setDisabled(b_valida);
        db_descgen.setDisabled(b_valida);
        db_descfinan.setDisabled(b_valida);
        chk_lpcest.setDisabled(b_valida);
        txt_lpcnomrep.setDisabled(b_valida);
        sp_lpcord.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int i_lp_key = txt_lpcid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_lpcid.getValue().trim());
        String s_lp_des = txt_lpcdes.getValue().toUpperCase().trim();
        long i_prov_key = Long.parseLong(txt_provid.getValue());
        int i_lp_est = chk_lpcest.isChecked() ? 1 : 2;
        String s_lp_nomrep = txt_lpcnomrep.getValue().toUpperCase().trim();
        double d_lp_descgen = db_descgen.getValue();
        double d_lp_descfinan = db_descfinan.getValue();
        int i_lp_ord = sp_lpcord.getValue() == null ? 0 : sp_lpcord.getValue();
        String s_lp_usuadd = objUsuCredential.getCuenta();
        String s_lp_usumod = objUsuCredential.getCuenta();
        return new ListaPrecio(i_lp_key, emp_id, suc_id, i_prov_key, s_lp_des, i_lp_est, d_lp_descgen, d_lp_descfinan, i_lp_ord, s_lp_nomrep, s_lp_usuadd, s_lp_usumod);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstpcompra = null;
        objlstpcompra = new ListModelList<ListaPrecio>();
        lst_pcompra.setModel(objlstpcompra);
    }
}
