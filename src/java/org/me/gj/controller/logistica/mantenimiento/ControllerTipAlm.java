package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.TipAlm;
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
import org.zkoss.zk.ui.Executions;

public class ControllerTipAlm extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listatipalm, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar,
            tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_tipalm;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, /*txt_tabnomrep, txt_busqueda,*/ txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<TipAlm> objlstTipAlm;/* = new ListModelList<TipAlm>()*/


    TipAlm objTipAlm;/* = new TipAlm();*/

    DaoTipAlm objDaoTipAlm;/* = new DaoTipAlm();*/

    //UsuariosCredential cre;
    Accesos objAccesos;/* = new Accesos();*/

    DaoAccesos objDaoAccesos;/* = new DaoAccesos();*/

    //Variables publicas
    String s_estado = "Q";
    String s_mensaje;
    int i_sel;
    int valor;
    String campo = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerTipAlm.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_tipalm")
    public void llenaRegistros() throws SQLException {
        objTipAlm = new TipAlm();
        objDaoTipAlm = new DaoTipAlm();
        objlstTipAlm = objDaoTipAlm.listaTipoAlmacen(1);
        lst_tipalm.setModel(objlstTipAlm);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        /*txt_busqueda.setValue("%%");
         txt_busqueda.setDisabled(true);*/
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10106020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Tipo de Almacenes con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Tipo de Almacenes con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Tipo de Almacen");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Tipo de Almacen");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Tipo de Almacen");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Tipo de Almacen");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Tipo de Almacen");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Tipo de Almacen");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Tipo de Almacenes");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Tipo de Almacenes");
        }
    }

    //@Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        //String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else if (cb_estado.getSelectedIndex() == 1) {
            i_estado = 2;
        } else {
            i_estado = 0;
        }
        //objlstTipAlm = new ListModelList<TipAlm>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else {
            i_seleccion = 2;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstTipAlm = objDaoTipAlm.busquedaTipAlm(i_seleccion, /*s_consulta,*/ i_estado);
        //   objlstTipAlm = null;
        if (objlstTipAlm.getSize() > 0) {
            lst_tipalm.setModel(objlstTipAlm);
        } else {
            objlstTipAlm = null;
            lst_tipalm.setModel(objlstTipAlm);
            //Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda_orden() throws SQLException {
        /*if (cb_busqueda.getSelectedIndex() > 0) {
         txt_busqueda.setDisabled(false);
         } else {
         txt_busqueda.setDisabled(true);
         //txt_busqueda.setValue("%%");
         }*/
        busquedaRegistros();
    }

    @Listen("onSelect=#cb_estado")
    public void seleccionabusqueda_estado() throws SQLException {
        /*if (cb_busqueda.getSelectedIndex() > 0) {
         txt_busqueda.setDisabled(false);
         } else {
         txt_busqueda.setDisabled(true);
         //txt_busqueda.setValue("%%");
         }*/
        busquedaRegistros();
    }

    @Listen("onSelect=#lst_tipalm")
    public void seleccionaRegistro() throws SQLException {
        objTipAlm = (TipAlm) lst_tipalm.getSelectedItem().getValue();
        if (objTipAlm == null) {
            objTipAlm = objlstTipAlm.get(i_sel + 1);
        }
        i_sel = lst_tipalm.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTipAlm.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objTipAlm = new TipAlm();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_tabest.setDisabled(true);
        //chk_tabest.setChecked(true);
        txt_tabsubdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_tipalm.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                        if (campo.equals("TipoAlmacen")) {
                            txt_tabsubdes.setValue("");
                            txt_tabsubdes.focus();
                        }
                    }
                }
            });
        } else {
            /*if (txt_tabsubdes.getText().matches("^\\s") || txt_tabsubdes.getText().matches("^\\s+")) {
             Messagebox.show("El Campo Tipo Almacen no debe tener espacio en blanco al principio");
             } else {*/
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                //objlstTipAlm = new ListModelList<TipAlm>();
                                objTipAlm = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoTipAlm.insertarTipAlm(objTipAlm);
                                } else {
                                    s_mensaje = objDaoTipAlm.actualizarTipAlm(objTipAlm);
                                }
//                                Messagebox.show(s_mensaje);
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                habilitaBotones(false, true);
                                /*tbbtn_btn_guardar.setDisabled(true);
                                 tbbtn_btn_deshacer.setDisabled(true);*/
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                /*cb_estado.setSelectedIndex(0);
                                 cb_busqueda.setSelectedIndex(0);
                                 txt_busqueda.setValue("%%");
                                 txt_busqueda.setDisabled(true);
                                 objlstTipAlm = objDaoTipAlm.listaTipoAlmacen(1);
                                 lst_tipalm.setModel(objlstTipAlm);
                                 objTipAlm = new TipAlm();*/
                                llenaRegistros();
                            }
                        }
                    });
            //}
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_tipalm.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoTipAlm.validaMovimientos(objTipAlm);
            valor = objDaoTipAlm.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoTipAlm.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea eliminar el tipo de almacen?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoTipAlm.eliminarTipAlm(objTipAlm);
                                    valor = objDaoTipAlm.i_flagErrorBD;
                                    if (valor == 0) {
                                        /*objlstTipAlm.clear();
                                         cb_estado.setSelectedIndex(0);
                                         cb_busqueda.setSelectedIndex(0);
                                         txt_busqueda.setValue("%%");
                                         txt_busqueda.setDisabled(true);
                                         objlstTipAlm = objDaoTipAlm.listaTipoAlmacen(1);
                                         lst_tipalm.setModel(objlstTipAlm);*/
                                        llenaRegistros();
                                        VerificarTransacciones();
                                         Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        limpiarCampos();
                                        limpiaAuditoria();
                                        //
                                    } else {
                                        Messagebox.show(objDaoTipAlm.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        }
                );
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
                            //lst_tipalm.setSelectedIndex(-1);
                            lst_tipalm.clearSelection();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            /*tbbtn_btn_guardar.setDisabled(true);
                             tbbtn_btn_deshacer.setDisabled(true);*/
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstTipAlm == null || objlstTipAlm.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionTipAlm.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#tabbox_tipalm")
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
            objlstTipAlm = new ListModelList<TipAlm>();
            objlstTipAlm = objDaoTipAlm.listaTipoAlmacen(0);
            lst_tipalm.setModel(objlstTipAlm);
        }
    }

    @Listen("onOK=#txt_tabsubdes")
    public void next_descripcion() {
        botonGuardar();
    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_manlin")
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
    }

    public void llenarCampos() {
        //txt_tabid.setValue(String.valueOf(objTipAlm.getTab_id()));
        txt_tabid.setValue(objTipAlm.getCod_id());
        txt_tabsubdes.setValue(objTipAlm.getTab_subdes());
        /*if (objTipAlm.getTab_est() == 1) {
         chk_tabest.setChecked(true);
         } else {
         chk_tabest.setChecked(false);
         }*/
        chk_tabest.setChecked(objTipAlm.getTab_est() == 1);
        //txt_tabnomrep.setValue(objTipAlm.getTab_nomrep());
        sp_tabord.setValue(objTipAlm.getTab_ord());
        txt_usuadd.setValue(objTipAlm.getTab_usuadd());
        d_fecadd.setValue(objTipAlm.getTab_fecadd());
        txt_usumod.setValue(objTipAlm.getTab_usumod());
        d_fecmod.setValue(objTipAlm.getTab_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipalm.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipalm.setDisabled(b_valida1);
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

    public String verificar() {
        String s_valor = "";
        if (txt_tabsubdes.getValue().isEmpty()) {
            campo = "TipoAlmacen";
            s_valor = "El campo 'Tipo Almacen' es obligatorio";
        } else if (!txt_tabsubdes.getValue().matches("[a-zA-ZñÑáÁéÉíÍóÓúÚ\\s]+")) {
            campo = "TipoAlmacen";
            s_valor = "Por favor ingresar solo letras en el campo 'TIPO ALMACEN'";
        } /*else {
         s_valor = "";
         }*/

        return s_valor;
    }

    public void limpiarCampos() {
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        //txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
        //chk_tabest.setDisabled(true);
        chk_tabest.setChecked(true);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_tabsubdes.setDisabled(b_valida1);
        chk_tabest.setDisabled(b_valida1);
        //txt_tabnomrep.setDisabled(b_valida1);
        sp_tabord.setDisabled(b_valida1);
    }

    public TipAlm generaRegistro() {
        int i_tabid = txt_tabid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_tabid.getValue().trim());
        /*if (txt_tabid.getValue().isEmpty()) {
         i_tabid = 0;
         } else {
         i_tabid = Integer.parseInt(txt_tabid.getValue().trim());
         }*/
        int i_tabest = chk_tabest.isChecked() ? 1 : 2;
        /*if (chk_tabest.isChecked()) {
         i_tabest = 1;
         } else {
         i_tabest = 2;
         }*/
        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase().trim();
        //String s_tabnomrep = txt_tabnomrep.getValue().toUpperCase().trim();
        int i_tabord = sp_tabord.getValue().toString().isEmpty() ? 0 : sp_tabord.getValue();
        /*if (sp_tabord.getValue().toString().isEmpty()) {
         i_tabord = 0;
         } else {
         i_tabord = sp_tabord.getValue();
         }*/
        String s_tab_usuadd = objUsuCredential.getCuenta();
        String s_tab_usumod = objUsuCredential.getCuenta();
        return new TipAlm(i_tabid, s_tabsubdes, i_tabest, i_tabord, s_tab_usuadd, s_tab_usumod);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstTipAlm = null;
        objlstTipAlm = new ListModelList<TipAlm>();
        lst_tipalm.setModel(objlstTipAlm);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
