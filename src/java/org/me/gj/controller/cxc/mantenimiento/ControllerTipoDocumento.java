package org.me.gj.controller.cxc.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
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

public class ControllerTipoDocumento extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listatipodoc, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Listbox lst_tipodoc;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<TipoDocumento> objlstTipoDocumento;
    DaoTipoDocumento objDaoTipoDocumento = new DaoTipoDocumento();
    TipoDocumento objTipoDocumento = new TipoDocumento();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "";
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    String foco = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(ControllerTipoDocumento.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_tipodoc")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstTipoDocumento = new ListModelList<TipoDocumento>();
        objlstTipoDocumento = objDaoTipoDocumento.listaTipoDocumento(1);
        lst_tipodoc.setModel(objlstTipoDocumento);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20107000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Tipo de Documento de Identidad con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Tipo de Documento de Identidad con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de Tipo de Documento de Identidad");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de Tipo de Documento de Identidad");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de Tipo de Documento de Identidad");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de Tipo de Documento de Identidad");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de Tipo de Documento de Identidad");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de Tipo de Documento de Identidad");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Tipo de Documento de Identidad");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Tipo de Documento de Identidad");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado = 1;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3; // todos
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;//activos
        } else {
            i_estado = 2;//inactivos
        }
        objlstTipoDocumento = new ListModelList<TipoDocumento>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstTipoDocumento = objDaoTipoDocumento.busquedaTipoDocumento(i_seleccion, s_consulta, i_estado);
        if (objlstTipoDocumento.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstTipoDocumento.getSize() + " registro(s)");
        }
        if (objlstTipoDocumento.getSize() > 0) {
            lst_tipodoc.setModel(objlstTipoDocumento);
        } else {
            objlstTipoDocumento = null;
            lst_tipodoc.setModel(objlstTipoDocumento);
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

    @Listen("onSelect=#lst_tipodoc")
    public void seleccionaRegistro() throws SQLException {
        objTipoDocumento = (TipoDocumento) lst_tipodoc.getSelectedItem().getValue();
        if (objTipoDocumento == null) {
            objTipoDocumento = objlstTipoDocumento.get(i_sel + 1);
        }
        i_sel = lst_tipodoc.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTipoDocumento.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objTipoDocumento = new TipoDocumento();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_tabest.setDisabled(true);
        chk_tabest.setChecked(true);
        txt_tabsubdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_tipodoc.getSelectedIndex() == -1) {
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
                        if (foco.equals("tabsubdes")) {
                            txt_tabsubdes.focus();
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
                                objlstTipoDocumento = new ListModelList<TipoDocumento>();
                                objTipoDocumento = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoTipoDocumento.insertarTipoDocumento(objTipoDocumento);
                                } else {
                                    s_mensaje = objDaoTipoDocumento.actualizarTipoDocumento(objTipoDocumento);
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
                                objlstTipoDocumento = objDaoTipoDocumento.listaTipoDocumento(0);
                                lst_tipodoc.setModel(objlstTipoDocumento);
                                objTipoDocumento = new TipoDocumento();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_tipodoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar el Tipo de Documento de Identidad";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoTipoDocumento.eliminarTipoDocumento(objTipoDocumento);
                                valor = objDaoTipoDocumento.i_flagErrorBD;
                                if (valor == 0) {
                                     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    objlstTipoDocumento = objDaoTipoDocumento.listaTipoDocumento(0);
                                    lst_tipodoc.setModel(objlstTipoDocumento);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                } else {
                                    Messagebox.show(objDaoTipoDocumento.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            limpiaAuditoria();
                            lst_tipodoc.setSelectedIndex(-1);
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
        if (objlstTipoDocumento == null || objlstTipoDocumento.isEmpty()) {
            Messagebox.show("No hay registros de Tipo de Documento para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/cxc/mantenimiento/LovImpresionTipoDocumento.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onOK=#txt_tabsubdes")
    public void lov_txt_tabsubdes() throws SQLException {
        if (!txt_tabsubdes.getValue().isEmpty()) {
            txt_tabnomrep.focus();
        }
    }

    @Listen("onOK=#txt_tabnomrep")
    public void next_tabnomrep() throws SQLException {
        sp_tabord.focus();
    }

    @Listen("onOK=#sp_tabord")
    public void nextGuardar() throws SQLException {
        botonGuardar();
    }

    //Eventos Secundarios - Validaciones  
    /*
     @Listen("onCtrlKey=#tabbox_tipodoc")
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
    @Listen("onCtrlKey=#w_mancat") // tabbox_tipcambio
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
            objlstTipoDocumento = new ListModelList<TipoDocumento>();
            objlstTipoDocumento = objDaoTipoDocumento.listaTipoDocumento(0);
            lst_tipodoc.setModel(objlstTipoDocumento);
        }
    }

    //Eventos Otros 
    public void LimpiarLista() {
        //limpio mi lista
        objlstTipoDocumento = null;
        objlstTipoDocumento = new ListModelList<TipoDocumento>();
        lst_tipodoc.setModel(objlstTipoDocumento);
    }

    public void limpiarCampos() {
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdes.setDisabled(b_valida);
        chk_tabest.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);
    }

    public void llenarCampos() {
        txt_tabid.setValue(String.valueOf(objTipoDocumento.getTab_id()));
        txt_tabsubdes.setValue(objTipoDocumento.getTab_subdes());
        if (objTipoDocumento.getTab_est() == 1) {
            chk_tabest.setChecked(true);
        } else {
            chk_tabest.setChecked(false);
        }
        txt_tabnomrep.setValue(objTipoDocumento.getTab_nomrep());
        sp_tabord.setValue(objTipoDocumento.getTab_ord());
        txt_usuadd.setValue(objTipoDocumento.getTab_usuadd());
        d_fecadd.setValue(objTipoDocumento.getTab_fecadd());
        txt_usumod.setValue(objTipoDocumento.getTab_usumod());
        d_fecmod.setValue(objTipoDocumento.getTab_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipodoc.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipodoc.setSelected(b_valida1);
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

    public TipoDocumento generaRegistro() {
        int i_tabid;
        if (txt_tabid.getValue().isEmpty()) {
            i_tabid = 0;
        } else {
            i_tabid = Integer.parseInt(txt_tabid.getValue());
        }
        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase();
        int i_tabest;
        if (chk_tabest.isChecked()) {
            i_tabest = 1;
        } else {
            i_tabest = 2;
        }
        String s_tabnomrep = txt_tabnomrep.getValue().toUpperCase();
        int i_tabord;
        if (sp_tabord.getValue().toString().isEmpty()) {
            i_tabord = 0;
        } else {
            i_tabord = sp_tabord.getValue();
        }
        return new TipoDocumento(i_tabid, s_tabsubdes, i_tabord, s_tabnomrep, i_tabest);
    }

    public String verificar() {
        String s_valor = "";
        if (txt_tabsubdes.getValue().isEmpty()) {
            s_valor = "Por favor, ingrese el campo Descripción";
            foco = "tabsubdes";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

}
