package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.RelGui;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Executions;

public class ControllerRelNotaES extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listarelgui, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Listbox lst_relgui;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<RelGui> objlstRelGui = new ListModelList<RelGui>();
    RelGui objRelGui = new RelGui();
    DaoRelNotaES objDaoRelGui = new DaoRelNotaES();
    UsuariosCredential cre;
    Accesos obj_Accesos = new Accesos();
    DaoAccesos obj_daoAcc = new DaoAccesos();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje;
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRelNotaES.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_relgui")
    public void llenaRegistros() throws SQLException {
        objlstRelGui = objDaoRelGui.listaRelacionGuia(1);
        lst_relgui.setModel(objlstRelGui);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = cre.getCodigo();
        int empresa = cre.getCodemp();
        int sucursal = cre.getCodsuc();
        obj_Accesos = obj_daoAcc.Verifica_Permisos_Transacciones(10110030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Relación de Guias con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Relación de Guias con el rol: USUARIO NORMAL");
        }
        if (obj_Accesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Relación de Guias");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Relación de Guias");
        }
        if (obj_Accesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Relación de Guias");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Relación de Guias");
        }
        if (obj_Accesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Relación de Guias");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Relación de Guias");
        }
        if (obj_Accesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Relación de Guias");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Relación de Guias");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
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
        objlstRelGui = new ListModelList<RelGui>();
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
        objlstRelGui = objDaoRelGui.busquedaRelGui(i_seleccion, s_consulta, i_estado);
        if (objlstRelGui.getSize() > 0) {
            lst_relgui.setModel(objlstRelGui);
        } else {
            objlstRelGui = null;
            lst_relgui.setModel(objlstRelGui);
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

    @Listen("onSelect=#lst_relgui")
    public void seleccionaRegistro() throws SQLException {
        objRelGui = (RelGui) lst_relgui.getSelectedItem().getValue();
        if (objRelGui == null) {
            objRelGui = objlstRelGui.get(i_sel + 1);
        }
        i_sel = lst_relgui.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objRelGui.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objRelGui = new RelGui();
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
        if (lst_relgui.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
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
                        txt_tabsubdes.focus();
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
                                objRelGui = generaRegistro();
                                ParametrosSalida objParamSalida;
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoRelGui.insertarRelGui(objRelGui);
                                } else {
                                    objParamSalida = objDaoRelGui.actualizarRelGui(objRelGui);
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
                                    objlstRelGui.clear();
                                    objlstRelGui = objDaoRelGui.listaRelacionGuia(1);
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    lst_relgui.setModel(objlstRelGui);
                                    objRelGui = new RelGui();

                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_relgui.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoRelGui.validaMovimientos(objRelGui);
            valor = objDaoRelGui.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoRelGui.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Esta Seguro que desea Eliminar esta relacion de guia?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoRelGui.eliminarRelGui(objRelGui);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstRelGui.clear();
                                        objlstRelGui = objDaoRelGui.listaRelacionGuia(1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_relgui.setModel(objlstRelGui);
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
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_relgui.setSelectedIndex(-1);
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
        if (objlstRelGui == null || objlstRelGui.isEmpty()) {
            Messagebox.show("No hay Registros de Relación de Guias para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionRelGuia.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
   /* @Listen("onCtrlKey=#tabbox_relgui")
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
            objlstRelGui = new ListModelList<RelGui>();
            objlstRelGui = objDaoRelGui.listaRelacionGuia(0);
            lst_relgui.setModel(objlstRelGui);
        }
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_tabid.setValue(String.valueOf(objRelGui.getTab_id()));
        txt_tabsubdes.setValue(objRelGui.getTab_subdes());
        if (objRelGui.getTab_est() == 1) {
            chk_tabest.setChecked(true);
        } else {
            chk_tabest.setChecked(false);
        }
        txt_tabnomrep.setValue(objRelGui.getTab_nomrep());
        sp_tabord.setValue(objRelGui.getTab_ord());
        txt_usuadd.setValue(objRelGui.getTab_usuadd());
        d_fecadd.setValue(objRelGui.getTab_fecadd());
        txt_usumod.setValue(objRelGui.getTab_usumod());
        d_fecmod.setValue(objRelGui.getTab_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarelgui.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarelgui.setDisabled(b_valida1);
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
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
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

    public String verificar() {
        String s_valor;
        if (txt_tabsubdes.getValue().isEmpty() || txt_tabsubdes.getText().trim().length() == 0) {
            s_valor = "El Campo Relacion es Obligatorio";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdes.setDisabled(b_valida);
        chk_tabest.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);
    }

    public RelGui generaRegistro() {
        int i_tabid;
        if (txt_tabid.getValue().isEmpty()) {
            i_tabid = 0;
        } else {
            i_tabid = Integer.parseInt(txt_tabid.getValue().trim());
        }
        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase().trim();
        int i_tabest;
        if (chk_tabest.isChecked()) {
            i_tabest = 1;
        } else {
            i_tabest = 2;
        }
        String s_tabnomrep = txt_tabnomrep.getValue().toUpperCase().trim();
        int i_tabord;
        if (sp_tabord.getValue().toString().isEmpty()) {
            i_tabord = 0;
        } else {
            i_tabord = sp_tabord.getValue();
        }
        String tab_usuadd = objUsuCredential.getCuenta();
        String tab_usumod = objUsuCredential.getCuenta();
        return new RelGui(i_tabid, s_tabsubdes, i_tabest, s_tabnomrep, i_tabord, tab_usuadd, tab_usumod);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstRelGui = null;
        objlstRelGui = new ListModelList<RelGui>();
        lst_relgui.setModel(objlstRelGui);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
