package org.me.gj.controller.cxc.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.cxc.mantenimiento.TipoCambio;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerTipoCambio extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_moneda, cb_estado, cb_periodo;
    @Wire
    Textbox txt_tcam_id, txt_usuadd, txt_usumod;
    @Wire
    Doublebox txt_tcam_com, txt_tcam_conv, txt_tcam_conc;
    @Wire
    Datebox txt_dia_fec, d_fecadd, d_fecmod;
    @Wire
    Listbox lst_tipcamb;
    @Wire
    Checkbox chk_busest, chk_tcambest;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPer;
    ListModelList<Moneda> objlstMoneda;
    ListModelList<TipoCambio> objlstTipoCambio;
    TipoCambio objTipoCambio = new TipoCambio();
    Moneda objMoneda = new Moneda();
    Accesos objAccesos = new Accesos();
    DaoTipoCambio objDaoTipoCambio = new DaoTipoCambio();
    DaoMoneda objDaoMoneda = new DaoMoneda();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios objUtil = new Utilitarios();
    DaoManPer objDaoManPer = new DaoManPer();
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    String foco="";
    //Variables de Sesion    
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(ControllerTipoCambio.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_tipcambio")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objlstManPer = new ListModelList<ManPer>();
        objlstManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objlstManPer);
        objlstManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        //
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstMoneda = objDaoMoneda.listaMonedas(1);
        cb_moneda.setModel(objlstMoneda);
        objlstTipoCambio = new ListModelList<TipoCambio>();
        objlstTipoCambio = objDaoTipoCambio.listaTipoCambio(1, Utilitarios.periodoActual());
        lst_tipcamb.setModel(objlstTipoCambio);
        cb_estado.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20106000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Tipo de Cambio con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Tipo de Cambio con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Tipo de Cambio");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Tipo de Cambio");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Tipo de Cambio");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Tipo de Cambio");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Tipo de Cambio");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Tipo de Cambio");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Tipo de Cambio");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Tipo de Cambio");
        }

    }

    @Listen("onSelect=#lst_tipcamb")
    public void seleccionaRegistro() throws SQLException {
        objTipoCambio = (TipoCambio) lst_tipcamb.getSelectedItem().getValue();
        if (objTipoCambio == null) {
            objTipoCambio = objlstTipoCambio.get(i_sel + 1);
        }
        i_sel = lst_tipcamb.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTipoCambio.getTcam_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objTipoCambio = new TipoCambio();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_tcambest.setDisabled(true);
        chk_tcambest.setChecked(true);
        cb_moneda.focus();
        txt_dia_fec.setValue(new Date());
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_tipcamb.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            cb_moneda.focus();
            SeleccionaSoles();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if ( ((Integer) event.getData()).intValue() == Messagebox.OK  ) {
                        if (foco.equals("moneda")) {
                            cb_moneda.focus();
                        } else if (foco.equals("tcam_com")) {
                            txt_tcam_com.focus();
                        } else if (foco.equals("tcam_conv")) {
                            txt_tcam_conv.focus();
                        } else if (foco.equals("tcam_conc")) {
                            txt_tcam_conc.focus();
                        }
                    }
                }
            });
            
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    objlstTipoCambio = new ListModelList<TipoCambio>();
                                    objTipoCambio = generaRegistro();
                                    if (s_estado.equals("N")) {
                                        s_mensaje = objDaoTipoCambio.insertarTipoCambio(objTipoCambio);
                                    } else {
                                        s_mensaje = objDaoTipoCambio.actualizarTipoCambio(objTipoCambio);
                                    }
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                                    objlstTipoCambio = objDaoTipoCambio.listaTipoCambio(1, Utilitarios.periodoActual());
                                    lst_tipcamb.setModel(objlstTipoCambio);
                                    objTipoCambio = new TipoCambio();
                                }
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_tipcamb.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar el Tipo de Cambio";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoTipoCambio.eliminarTipoCambio(objTipoCambio);
                                valor = objDaoTipoCambio.i_flagErrorBD;
                                if (valor == 0) {
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    objlstTipoCambio = objDaoTipoCambio.listaTipoCambio(1, Utilitarios.periodoActual());
                                    lst_tipcamb.setModel(objlstTipoCambio);
                                    limpiarCampos();
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                } else {
                                    Messagebox.show(objDaoTipoCambio.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
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
                            lst_tipcamb.setSelectedIndex(-1);
                            habilitaTab(false, false);
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
        if (objlstTipoCambio == null || objlstTipoCambio.isEmpty()) {
            Messagebox.show("No hay registros de Tipos de Cambio para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/cxc/mantenimiento/LovImpresionTipoCambio.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones  
    @Listen("onCtrlKey=#w_mantipcam") // tabbox_tipcambio
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

    /*
     @Listen("onCtrlKey=#w_manpost")
     public void ctrl_teclado(Event event) throws SQLException, ParseException {
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
     }*/
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstTipoCambio = new ListModelList<TipoCambio>();
            objlstTipoCambio = objDaoTipoCambio.listaTipoCambio(0, cb_moneda.getValue());
            lst_tipcamb.setModel(objlstTipoCambio);
        }
    }

    @Listen("onOK=#cb_moneda")
    public void lov_cb_moneda() throws SQLException {
        if (cb_moneda.getSelectedIndex() == -1) {
            cb_moneda.focus();
        } else {
            txt_tcam_com.focus();
        }
    }

    @Listen("onOK=#txt_tcam_com")
    public void lov_txt_tcam_com() throws SQLException {
        if (txt_tcam_com.getValue() == null) {
            txt_tcam_com.focus();

        } else {
            txt_tcam_conv.focus();
        }
    }

    @Listen("onOK=#txt_tcam_conv")
    public void lov_txt_tcam_conv() throws SQLException {
        if (txt_tcam_conv.getValue() == null) {
            txt_tcam_conv.focus();
        } else {
            txt_tcam_conc.focus();
        }
    }

    @Listen("onOK=#txt_tcam_conc")
    public void lov_txt_ncddocref() throws SQLException {
        if (txt_tcam_conc.getValue() == null) {
            txt_tcam_conc.focus();
        }
    }
    
    @Listen("onOK=#txt_tcam_conc")
    public void nextGuardar() throws SQLException {
        botonGuardar();
    }

    @Listen("onSelect=#cb_moneda")
    public void SeleccionaSoles() {
        if (Integer.parseInt(cb_moneda.getSelectedItem().getValue().toString()) == 1) {
            txt_tcam_com.setValue(1.00);
            txt_tcam_conc.setValue(1.00);
            txt_tcam_conv.setValue(1.00);
            txt_tcam_com.setDisabled(true);
            txt_tcam_conc.setDisabled(true);
            txt_tcam_conv.setDisabled(true);
        } else {
            txt_tcam_com.setValue(null);
            txt_tcam_conc.setValue(null);
            txt_tcam_conv.setValue(null);
            txt_tcam_com.setDisabled(false);
            txt_tcam_conc.setDisabled(false);
            txt_tcam_conv.setDisabled(false);
        }
    }

    @Listen("onSelect=#cb_periodo")
    public void SeleccionaPeriodo() throws SQLException {
        busquedaRegistros();
        limpiaAuditoria();
        limpiarCampos();
    }

    @Listen("onSelect=#cb_estado")
    public void SeleccionaEstado() throws SQLException {
        busquedaRegistros();
        limpiaAuditoria();
        limpiarCampos();
    }

    //Eventos Otros
    public void busquedaRegistros() throws SQLException {
        int i_estado = 1;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3; // todos
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;//activos
        } else {
            i_estado = 2;//inactivos
        }
        objlstTipoCambio = new ListModelList<TipoCambio>();
        objlstTipoCambio = objDaoTipoCambio.busquedaTipoCambio(i_estado, cb_periodo.getValue());
        lst_tipcamb.setModel(objlstTipoCambio);
    }

    public TipoCambio generaRegistro() {
        String cambio_id, user;
        Date fecha;
        int estado, moneda, cambio_key;
        double tcambio_comercial, tcambio_venta, tcambio_compra;
        cambio_id = txt_tcam_id.getValue();
        if (txt_tcam_id.getValue().isEmpty()) {
            cambio_key = 0;
        } else {
            cambio_key = Integer.parseInt(txt_tcam_id.getValue());
        }
        fecha = txt_dia_fec.getValue();
        moneda = cb_moneda.getSelectedItem().getValue();
        tcambio_comercial = txt_tcam_com.getValue();
        tcambio_venta = txt_tcam_conv.getValue();
        tcambio_compra = txt_tcam_conc.getValue();
        user = objUsuCredential.getCuenta();
        if (chk_tcambest.isChecked()) {
            estado = 1;
        } else {
            estado = 2;
        }
        /*if (tipo == 1) {
         return new TipoCambio(cambio_id, fecha, moneda, estado, tcambio_comercial, tcambio_venta, tcambio_compra, user);
         } else {*/
        return new TipoCambio(cambio_key, cambio_id, fecha, moneda, estado, tcambio_comercial, tcambio_venta, tcambio_compra, user);
        /*}*/
    }

    public String verificar() {
        String s_valor = "";
        if (cb_moneda.getSelectedIndex() == -1) {
            s_valor = "Por favor, ingrese el campo Moneda";
            foco = "moneda";
        } else if (txt_tcam_com.getValue() == null) {
            s_valor = "Por favor, ingrese el campo Tipo de Cambio Comercial";
            foco = "tcam_com";
        } else if (txt_tcam_conv.getValue() == null) {
            s_valor = "Por favor, ingrese el campo Tipo de Cambio Contabilidad Ventas";
            foco = "tcam_conv";
        } else if (txt_dia_fec.getValue() == null) {
            s_valor = "Por favor, ingrese el campo Fecha de Tipo de Cambio";
            foco = "dia_fec";
        } else if (txt_tcam_conc.getValue() == null) {
            s_valor = "Por favor, ingrese el campo Tipo de Cambio Contabilidad Compras";
            foco = "tcam_conc";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstTipoCambio = null;
        objlstTipoCambio = new ListModelList<TipoCambio>();
        lst_tipcamb.setModel(objlstTipoCambio);
    }

    public void limpiarCampos() {
        txt_tcam_id.setValue("");
        txt_tcam_com.setValue(null);
        txt_tcam_conc.setValue(null);
        txt_tcam_conv.setValue(null);
        cb_moneda.setSelectedIndex(-1);
        txt_dia_fec.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        chk_tcambest.setDisabled(b_valida);
        cb_moneda.setDisabled(b_valida);
        txt_tcam_com.setDisabled(b_valida);
        txt_tcam_conc.setDisabled(b_valida);
        txt_tcam_conv.setDisabled(b_valida);
        txt_dia_fec.setDisabled(b_valida);
    }

    public void llenarCampos() {
        txt_tcam_id.setValue(objTipoCambio.getTcam_id());
        txt_dia_fec.setValue(objTipoCambio.getDia_fec());
        txt_tcam_com.setValue(objTipoCambio.getTcam_com());
        txt_tcam_conc.setValue(objTipoCambio.getTcam_conc());
        txt_tcam_conv.setValue(objTipoCambio.getTcam_conv());
        cb_moneda.setSelectedItem(objUtil.valorPorTexto1(cb_moneda, objTipoCambio.getTab_id()));
        if (objTipoCambio.getTcam_est() == 1) {
            chk_tcambest.setChecked(true);
        } else {
            chk_tcambest.setChecked(false);
        }
        txt_usuadd.setValue(objTipoCambio.getTcam_usuadd());
        d_fecadd.setValue(objTipoCambio.getTcam_fecadd());
        txt_usumod.setValue(objTipoCambio.getTcam_usumod());
        d_fecmod.setValue(objTipoCambio.getTcam_fecmod());
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

}
