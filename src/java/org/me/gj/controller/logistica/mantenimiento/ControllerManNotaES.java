package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerManNotaES extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaguias, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar,
            tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_guias;
    @Wire
    Combobox cb_tabval1, cb_tabguiingart, cb_busqueda, cb_estado;
    @Wire
    Textbox txt_tabsubdes, txt_tabid,
            txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod,txt_glosa;
    @Wire
    Intbox txt_guisun, txt_tabguiref;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Guias> objlstGuias = new ListModelList<Guias>();
    ListModelList<TipGui> objlstTtipGui = new ListModelList<TipGui>();
    ListModelList<DetGui> objlstTdetGui = new ListModelList<DetGui>();
    DaoManNotaES objDaoGuias = new DaoManNotaES();
    Guias objGuias = new Guias();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje = "";
    int i_sel;
    int i_gref;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManNotaES.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_guia")
    public void llenaRegistros() throws SQLException {
        objlstGuias = objDaoGuias.listaGuias(1);
        objlstTtipGui = objDaoGuias.listaTipGui(1);
        objlstTdetGui = objDaoGuias.lstDetGui(1);
        lst_guias.setModel(objlstGuias);
        cb_tabval1.setModel(objlstTtipGui);
        cb_tabguiingart.setModel(objlstTdetGui);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10110010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Guias con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Guias con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Guia");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Guia");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Guia");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Guia");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Guia");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Guia");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Guias");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Guias");
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

        objlstGuias = new ListModelList<Guias>();
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
        objlstGuias = objDaoGuias.busquedaGuias(i_seleccion, s_consulta, i_estado);

        if (objlstGuias.getSize() > 0) {
            lst_guias.setModel(objlstGuias);
        } else {
            objlstGuias = null;
            lst_guias.setModel(objlstGuias);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#lst_guias")
    public void seleccionaRegistro() throws SQLException {
        objGuias = objlstGuias.get(lst_guias.getSelectedIndex());
        if (objGuias == null) {
            objGuias = objlstGuias.get(i_sel + 1);
        }
        i_sel = lst_guias.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objGuias.getIdGui());
        limpiarCampos();
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objGuias = new Guias();
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
        if (lst_guias.getSelectedIndex() == -1) {
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

        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                i_gref = txt_tabguiref.getValue() == null ? 0 : txt_tabguiref.getValue();
                                valor = objDaoGuias.ValidaGuiaRef(i_gref);
                                if (valor == 0) {
                                    Messagebox.show("La guia de Referencia no Existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    ParametrosSalida objParamSalida;
                                    objGuias = (Guias) generaRegistro();
                                    if (s_estado.equals("N")) {
                                        objParamSalida = objDaoGuias.insertarGuia(objGuias);
                                    } else {
                                        objParamSalida = objDaoGuias.actualizarGuia(objGuias);
                                    }
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        habilitaTab(false, false);
                                        seleccionaTab(true, false);
                                        //validacion de guardar/nuevo
                                        VerificarTransacciones();
                                        tbbtn_btn_guardar.setDisabled(true);
                                        tbbtn_btn_deshacer.setDisabled(true);
                                        //
                                        habilitaCampos(true);
                                        limpiarCampos();
                                        limpiaAuditoria();
                                        objlstGuias.clear();
                                        objlstGuias = objDaoGuias.listaGuias(1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_guias.setModel(objlstGuias);
                                        objGuias = new Guias();
                                    }
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

                                }
                            }
                        }
                    }
            );
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_guias.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoGuias.validaMovimientosg(objGuias);
            valor = objDaoGuias.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoGuias.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Esta Seguro que desea Eliminar esta guia?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoGuias.eliminarGuia(objGuias);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstGuias.clear();
                                        objlstGuias = objDaoGuias.listaGuias(1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_guias.setModel(objlstGuias);
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
                            lst_guias.setSelectedIndex(-1);
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
        if (objlstGuias == null || objlstGuias.isEmpty()) {
            Messagebox.show("No hay registros de NOTA E/S para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionGuia.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#tabbox_guia")
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
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstGuias = new ListModelList<Guias>();
            objlstGuias = objDaoGuias.listaGuias(0);
            lst_guias.setModel(objlstGuias);
        }
    }

    @Listen("onCtrlKey=#w_mangui")
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
                    botonDeshacer();
                }
                break;

      
        }
    }
    //Eventos Otros 
    public void llenarCampos() {
        //Utilitarios objUilitario = new Utilitarios();
        txt_tabid.setValue(objGuias.getCodigo());
        if (objGuias.getEstGui() == 2) {
            chk_tabest.setChecked(false);
        } else {
            chk_tabest.setChecked(true);
        }
        txt_tabsubdes.setValue(objGuias.getDesGui());
        cb_tabval1.setSelectedItem(Utilitarios.valorPorTexto(cb_tabval1, objGuias.getIdMovGui()));
        cb_tabguiingart.setSelectedItem(Utilitarios.valorPorTexto(cb_tabguiingart, objGuias.getIdDetGui()));
        txt_tabguiref.setValue(objGuias.getRefGui());
        txt_guisun.setValue(objGuias.getSunGui());
        txt_tabnomrep.setValue(objGuias.getNomRepGui());
        sp_tabord.setValue(objGuias.getOrdGui());
        txt_usuadd.setValue(objGuias.getUsuaddGui());
        d_fecadd.setValue(objGuias.getFecaddGui());
        txt_usumod.setValue(objGuias.getUsumodGui());
        d_fecmod.setValue(objGuias.getFecmodGui());
        txt_glosa.setValue(objGuias.getGlosa());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaguias.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaguias.setSelected(b_valida1);
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
        String s_valida;
        if (txt_tabsubdes.getValue().isEmpty() || txt_tabsubdes.getText().trim().length() == 0) {
            s_valida = "Descripcion";
            txt_tabsubdes.focus();
        } else if (cb_tabguiingart.getValue().isEmpty()) {
            s_valida = "Detalle de guia";
            cb_tabguiingart.focus();
        } else if (cb_tabval1.getValue().isEmpty()) {
            s_valida = "Tipo de Movimiento";
            cb_tabval1.focus();
        } else {
            s_valida = "";
        }
        return s_valida;
    }

    public void limpiarCampos() {
        txt_tabid.setValue(null);
        txt_tabsubdes.setValue("");
        cb_tabval1.setSelectedIndex(-1);
        cb_tabguiingart.setSelectedIndex(-1);
        txt_tabguiref.setValue(null);
        txt_guisun.setValue(null);
        txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
        txt_glosa.setValue("");
                
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdes.setDisabled(b_valida);
        cb_tabval1.setDisabled(b_valida);
        txt_tabguiref.setDisabled(b_valida);
        cb_tabguiingart.setDisabled(b_valida);
        txt_guisun.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);
        chk_tabest.setDisabled(b_valida);
        txt_glosa.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int i_tabid;
        if (txt_tabid.getValue() == null || txt_tabid.getValue().isEmpty()) {
            i_tabid = 0;
        } else {
            i_tabid = Integer.parseInt(txt_tabid.getValue());
        }
        int tabest;
        if (chk_tabest.isChecked()) {
            tabest = 1;
        } else {
            tabest = 2;
        }
        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase().trim();
        int i_tabvall = Integer.parseInt(cb_tabval1.getSelectedItem().getValue().toString().trim());
        
        int i_tabguiref;
        if (txt_tabguiref.getValue() == null) {
            i_tabguiref = 0;
        } else {
            i_tabguiref = txt_tabguiref.getValue();
        }
        int i_tabguiingart = Integer.parseInt(cb_tabguiingart.getSelectedItem().getValue().toString().trim());
        int i_tabguisun;
        if (txt_guisun.getValue() == null) {
            i_tabguisun = 0;
        } else {
            i_tabguisun = txt_guisun.getValue();
        }
        String s_tabnomrep = txt_tabnomrep.getValue().toUpperCase().trim();
        String s_glosa = txt_glosa.getValue().toUpperCase().trim();

        int i_tabord;
        if (sp_tabord.getValue().toString().isEmpty()) {
            i_tabord = 0;
        } else {
            i_tabord = sp_tabord.getValue();
        }
        String s_tab_usuadd = objUsuCredential.getCuenta();
        String s_tab_usumod = objUsuCredential.getCuenta();
        return new Guias(1, i_tabid, tabest, s_tabsubdes, i_tabvall, i_tabguiref, i_tabguiingart,
                i_tabguisun, i_tabord, s_tabnomrep, s_tab_usuadd, s_tab_usumod,s_glosa);
       
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstGuias = null;
        objlstGuias = new ListModelList<Guias>();
        lst_guias.setModel(objlstGuias);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
