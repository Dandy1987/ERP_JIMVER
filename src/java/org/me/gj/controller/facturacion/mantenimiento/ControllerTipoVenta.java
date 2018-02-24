package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerTipoVenta extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listatipoventa, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_busest;
    @Wire
    Button btn_buscar;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_est, chk_bonif, chk_busest;
    @Wire
    Listbox lst_tipoventa;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<TipoVenta> objlstTipoVentas = new ListModelList<TipoVenta>();
    DaoTipoVenta objDaoTipoVentas = new DaoTipoVenta();
    TipoVenta objTipoVenta = new TipoVenta();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    String s_estado = "Q";
    String s_mensaje = "";
    String campo = "";
    public static boolean bandera;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerTipoVenta.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_tipoventa")
    public void llenaRegistros() throws SQLException {
        objlstTipoVentas = objDaoTipoVentas.listaTipoVentas(1);
        lst_tipoventa.setModel(objlstTipoVentas);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setDisabled(false);
        cb_busqueda.focus();
        cb_busqueda.select();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40109000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de tipo de venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de tipo de venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo tipo de venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo tipo de venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un tipo de venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un tipo de venta");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un tipo de venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un tipo de venta");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de tipo de venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de tipo de venta");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_busest.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_busest.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstTipoVentas = new ListModelList<TipoVenta>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de tipo de venta " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de tipo de venta" + s_consulta + " para su busqueda");
        }
        objlstTipoVentas = objDaoTipoVentas.busquedaTipoVentas(i_seleccion, s_consulta, i_estado);
        if (objlstTipoVentas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstTipoVentas.getSize() + " registro(s)");
        } 
        
        if (objlstTipoVentas.getSize() > 0) {
            lst_tipoventa.setModel(objlstTipoVentas);
        } else {
            limpiarLista();
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

    @Listen("onSelect=#lst_tipoventa")
    public void seleccionaRegistro() throws SQLException {
        objTipoVenta = (TipoVenta) lst_tipoventa.getSelectedItem().getValue();
        if (objTipoVenta == null) {
            objTipoVenta = objlstTipoVentas.get(i_sel + 1);
        }
        i_sel = lst_tipoventa.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTipoVenta.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objTipoVenta = new TipoVenta();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_est.setDisabled(true);
        chk_est.setChecked(true);
        txt_tabsubdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_tipoventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_tabsubdes.focus();
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
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Descripcion")) {
                            txt_tabsubdes.focus();
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
                                objlstTipoVentas = new ListModelList<TipoVenta>();
                                objTipoVenta = (TipoVenta) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoTipoVentas.insertarTipoVenta(objTipoVenta);
                                } else {
                                    s_mensaje = objDaoTipoVentas.actualizarTipoVenta(objTipoVenta);
                                }
                                Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
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
                                llenaRegistros();
                                objlstTipoVentas = objDaoTipoVentas.listaTipoVentas(1);
                                lst_tipoventa.setModel(objlstTipoVentas);
                                objTipoVenta = new TipoVenta();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_tipoventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_tipoventa.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar el tipo de venta?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String msg = objDaoTipoVentas.eliminarTipoVenta(objTipoVenta);
                                objlstTipoVentas = new ListModelList<TipoVenta>();
                                objlstTipoVentas = objDaoTipoVentas.listaTipoVentas(1);
                                lst_tipoventa.setModel(objlstTipoVentas);
                                //limpiarLista();
                                limpiarCampos();
                                Messagebox.show(msg,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                            }
                        }
                    });
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
                            limpiaAuditoria();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            llenaRegistros();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstTipoVentas == null || objlstTipoVentas.isEmpty()) {
            Messagebox.show("No hay registros de tipo de venta para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_tipoventa.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionTipoVenta.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_mantipven")
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
            objlstTipoVentas = new ListModelList<TipoVenta>();
            objlstTipoVentas = objDaoTipoVentas.listaTipoVentas(0);
            lst_tipoventa.setModel(objlstTipoVentas);
        }
    }

    @Listen("onOK=#cb_busqueda")
    public void onOK_cb_busqueda() throws SQLException {
        txt_busqueda.focus();
    }

    @Listen("onOK=#txt_busqueda")
    public void onOK_txt_busqueda() throws SQLException {
        cb_busest.focus();
        cb_busest.select();
    }

    @Listen("onOK=#cb_busest")
    public void onOK_cb_busest() throws SQLException {
        btn_buscar.focus();
    }

    @Listen("onOK=#txt_tabsubdes")
    public void onOKcanal() {
        txt_tabnomrep.focus();
    }

    @Listen("onOK=#txt_tabnomrep")
    public void onOKrepote() {
        sp_tabord.focus();
    }

    @Listen("onOK=#sp_tabord")
    public void onOKorden() {
        botonGuardar();
    }

    //Eventos Otros
    public String verificar() {
        String s_valor;
        if (txt_tabsubdes.getValue().isEmpty()) {
            s_valor = "El campo 'Tipo de venta' es obligatorio";
            campo = "Descripcion";
        }else if (!txt_tabsubdes.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "Por favor ingresar solo letras en el campo 'Tipo de venta'";
            campo = "Descripcion";
            txt_tabsubdes.setValue("");
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setText("%%");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstTipoVentas = null;
        objlstTipoVentas = new ListModelList<TipoVenta>();
        lst_tipoventa.setModel(objlstTipoVentas);
    }

    public void llenarCampos() {
        txt_tabid.setValue(objTipoVenta.getTab_subdir());
        txt_tabsubdes.setValue(objTipoVenta.getTab_subdes());
        chk_est.setChecked(objTipoVenta.isValorEst());
        chk_est.setLabel(objTipoVenta.isValorEst() ? "ACTIVO" : "INACTIVO");
        chk_bonif.setChecked(objTipoVenta.isValorBon());
        chk_bonif.setLabel(objTipoVenta.isValorBon() ? "PERMITIR" : "NO PERMITIR");
        txt_tabnomrep.setValue(objTipoVenta.getTab_nomrep());
        sp_tabord.setValue(objTipoVenta.getTab_ord());
        txt_usuadd.setValue(objTipoVenta.getTab_usuadd());
        d_fecadd.setValue(objTipoVenta.getTab_fecadd());
        txt_usumod.setValue(objTipoVenta.getTab_usumod());
        d_fecmod.setValue(objTipoVenta.getTab_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipoventa.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipoventa.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_est.setChecked(true);
        chk_est.setLabel("ACTIVO");
        chk_bonif.setChecked(false);
        chk_bonif.setLabel("NO PERMITIR");
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdes.setDisabled(b_valida);
        chk_est.setDisabled(b_valida);
        chk_bonif.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);

    }

    public Object generaRegistro() {
        int i_tabid = objTipoVenta.getTab_id();

        String s_tab_usuadd = objUsuCredential.getCuenta();
        String s_tab_usumod = objUsuCredential.getCuenta();

        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase().trim();
        int i_tabest;
        if (chk_est.isChecked()) {
            i_tabest = 1;
        } else {
            i_tabest = 2;
        }
        String s_tabnomrep = txt_tabnomrep.getValue().toUpperCase().trim();
        int i_tabbonif;
        if (chk_bonif.isChecked()) {
            i_tabbonif = 1;
        } else {
            i_tabbonif = 2;
        }
        int i_tabord;
        if (sp_tabord.getValue().toString().isEmpty()) {
            i_tabord = 0;
        } else {
            i_tabord = sp_tabord.getValue();
        }
        return new TipoVenta(i_tabid, s_tabsubdes, i_tabest, i_tabord, i_tabbonif, s_tabnomrep,
                s_tab_usuadd, s_tab_usumod);
    }

}
