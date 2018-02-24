package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.*;
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
import org.me.gj.util.Utilitarios;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Executions;

public class ControllerSubLineas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Textbox txt_busqueda, /*txt_tabdes, txt_tabsubdes,*/ txt_usuadd, txt_usumod, /*txt_tabcod, txt_tabid, */
            txt_codlinea, txt_deslinea, txt_codsublinea, txt_dessublinea;
    @Wire
    Combobox /*cbx_tabtip,*/ cb_busqueda, cb_estado;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Listbox lst_sublineas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<SubLineas> lstsublineas;
    ListModelList<Lineas> lsttipolineas;
    SubLineas objSubLinea;
    DaoSubLineas objDaoSubLineas = new DaoSubLineas();
    DaoLineas objDaoLineas = new DaoLineas();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado_mantenimiento = "Q";
    String s_mensaje = "";
    String campo = "";
    int i_sel;
    int valor;
    public static boolean bandera = false;
    String modoEjecucion;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerSubLineas.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_sublineas")
    public void llenaRegistros() throws SQLException {
        lstsublineas = new ListModelList<SubLineas>();
        lstsublineas = new DaoSubLineas().listaSublineas("",0,1);
        lst_sublineas.setModel(lstsublineas);
        /*lsttipolineas = new ListModelList<Lineas>();
         lsttipolineas = new DaoLineas().lstTipoLineas();
         cbx_tabtip.setModel(lsttipolineas);*/
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10109000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Sublineas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Sublineas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Sublinea");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Sublinea");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Sublinea");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Sublinea");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Sublinea");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Sublineas");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Sublineas");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Sublineas");
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
        lstsublineas = new ListModelList<SubLineas>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de sublinea " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de sublinea " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de linea " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de linea " + s_consulta + " para su busqueda");
        }
        lstsublineas = new DaoSubLineas().listaSublineas(s_consulta, i_seleccion, i_estado);
        if (lstsublineas.getSize() > 0) {
            lst_sublineas.setModel(lstsublineas);
        } else {
            lstsublineas = null;
            lst_sublineas.setModel(lstsublineas);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        //OnChange();
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

    @Listen("onSelect=#lst_sublineas")
    public void seleccionaRegistro() throws SQLException {
        boolean b_val = false;
        objSubLinea = lst_sublineas.getSelectedItem().getValue();
        if (objSubLinea == null) {
            objSubLinea = lstsublineas.get(i_sel + 1);
        }
        i_sel = lst_sublineas.getSelectedIndex();
        if (objSubLinea.getSlin_estado().equals("1")) {
            b_val = true;
        }
        chk_tabest.setChecked(b_val);
        txt_codlinea.setValue(objSubLinea.getSslin_codlin());
        txt_deslinea.setValue(objSubLinea.getSlin_deslin());
        txt_codsublinea.setValue(objSubLinea.getSlin_cod());
        txt_dessublinea.setValue(objSubLinea.getSlin_des());
        txt_usuadd.setValue(objSubLinea.getSlin_usuadd());
        d_fecadd.setValue(objSubLinea.getSlin_fecadd());
        txt_usumod.setValue(objSubLinea.getSlin_usumod());
        d_fecmod.setValue(objSubLinea.getSlin_fecmod());
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objSubLinea.getSlin_cod());
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        //if (s_estado_mantenimiento.equals("N")) {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Linea")) {
                            txt_codlinea.focus();
                        } else if (campo.equals("Sub Linea")) {
                            txt_dessublinea.focus();
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
                                String s_msg;
                                /*int i_tab_est = 2;
                                 int i_tab_cod = 0;
                                 String s_tab_des = "";
                                 int i_tab_id = 0;*/
                                //Recuperar en variables los valores en el formulario
                                //String s_tab_subdes = txt_tabsubdes.getValue();

                                String cod_sublinea = txt_codsublinea.getValue().isEmpty() ? "" : txt_codsublinea.getValue();
                                String des_sublinea = txt_dessublinea.getValue();
                                int cod_linea = Integer.parseInt(txt_codlinea.getValue());
                                int cod_subinea = txt_codsublinea.getValue().isEmpty() ? 0 : Integer.parseInt(StringFns.substring(txt_codsublinea.getValue(), 3, 6));
                                String estado = chk_tabest.isChecked() ? "1" : "2";

                                String usuarioadd = objUsuCredential.getCuenta();
                                String usuariomod = objUsuCredential.getCuenta();

                                objSubLinea = new SubLineas(cod_sublinea, des_sublinea, estado, cod_linea, cod_subinea, usuarioadd, usuariomod);
                                //(cod_sublinea, des_sublinea, estado, cod_linea, cod_subinea, usuario);
                                if (s_estado_mantenimiento.equals("N")) {
                                    s_msg = objDaoSubLineas.insertarSubLinea(objSubLinea);
                                } else {
                                    s_msg = objDaoSubLineas.modificarSubLinea(objSubLinea);
                                }
                                Messagebox.show(s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                
                                busquedaRegistros();
                                //lstsublineas = new DaoSubLineas().listaSublineas("1");
                                cb_estado.setSelectedIndex(0);
                                cb_busqueda.setSelectedIndex(0);
                                txt_busqueda.setValue("%%");
                                txt_busqueda.setDisabled(true);
                                //lst_sublineas.setModel(lstsublineas);
                                //OnChange();
                                limpiarCampos();
                                limpiaAuditoria();
                                tab_lista.setDisabled(false);
                                tab_lista.setSelected(true);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                            }
                        }
                    });
        }
        /*} else {
         if (s_estado_mantenimiento.equals("M")) {
         String s_valida = verificar();
         if (!s_valida.equals("")) {
         Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
         @Override
         public void onEvent(Event event) throws Exception {
         if (((Integer) event.getData()).intValue() == Messagebox.OK) {
         if (campo.equals("Linea")) {
         cbx_tabtip.focus();
         } else if (campo.equals("Sub Linea")) {
         txt_tabsubdes.focus();
         }
         }
         }
         });
         } else {
         if (txt_tabsubdes.getText().matches("^\\s")) {
         Messagebox.show("El Campo SubLineas no debe tener espacio en blanco al principio");
         } else {
         s_mensaje = "Esta Seguro que desea guardar los cambios?";
         Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
         Messagebox.QUESTION, new EventListener() {
         @Override
         public void onEvent(Event event) throws Exception {
         if (((Integer) event.getData()).intValue() == Messagebox.OK) {
         String msg;
         int i_tab_est = 2;
         //Recuperar en variables los valores en el formulario
         String i_tab_id = txt_tabid.getValue();
         String s_tab_subdes = txt_tabsubdes.getValue();
         int i_tab_tip = cbx_tabtip.getSelectedItem().getValue();
         if (chk_tabest.isChecked()) {
         i_tab_est = 1;
         }
         String s_tab_usuadd = objUsuCredential.getCuenta();
         String s_tab_usumod = objUsuCredential.getCuenta();
         objSubLinea = new SubLineas(i_tab_id, s_tab_subdes, "" + i_tab_est, 0, s_tab_subdes, "", 0, s_tab_usuadd, s_tab_usumod);
         msg = objDaoSubLineas.modificarSubLinea(objSubLinea);
         s_mensaje = msg;
         Messagebox.show(s_mensaje);
         lstsublineas = new DaoSubLineas().lstSublineas("1");
         cb_estado.setSelectedIndex(0);
         cb_busqueda.setSelectedIndex(0);
         txt_busqueda.setValue("%%");
         txt_busqueda.setDisabled(true);
         lst_sublineas.setModel(lstsublineas);
         OnChange();
         limpiaAuditoria();
         tab_lista.setSelected(true);
         tab_lista.setDisabled(false);
         VerificarTransacciones();
         tbbtn_btn_guardar.setDisabled(true);
         tbbtn_btn_deshacer.setDisabled(true);
         //
         }
         }
         });
         }
         }
         }
         }*/
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_sublineas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Sub-Linea a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoSubLineas.validaMovimientos(objSubLinea);
            valor = objDaoSubLineas.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoSubLineas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Está seguro que desea eliminar la sub-linea";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoSubLineas.eliminarSubLinea(objSubLinea);
                                    valor = objDaoSubLineas.i_flagErrorBD;
                                    if (valor == 0) {
                                        Messagebox.show(s_mensaje);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        //
                                        //lstsublineas = new DaoSubLineas().lstSublineas("1");
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        //lst_sublineas.setModel(lstsublineas);
                                        //OnChange();
                                        busquedaRegistros();
                                        limpiarCampos();
                                        limpiaAuditoria();
                                        tab_lista.setSelected(true);
                                    } else {
                                        Messagebox.show(objDaoSubLineas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        //OnChange();
        limpiarCampos();
        tab_lista.setSelected(false);
        tab_mantenimiento.setSelected(true);
        tab_lista.setDisabled(true);
        //tab_mantenimiento.setDisabled(false);
        //Guardar y Deshacer Activos
        tbbtn_btn_guardar.setDisabled(false);
        tbbtn_btn_deshacer.setDisabled(false);
        //Inactivar Nuevo, editar , eliminar
        tbbtn_btn_nuevo.setDisabled(true);
        tbbtn_btn_editar.setDisabled(true);
        tbbtn_btn_eliminar.setDisabled(true);

        txt_codlinea.setDisabled(false);
        txt_dessublinea.setDisabled(false);

        chk_tabest.setChecked(true);
        chk_tabest.setDisabled(true);
        txt_codlinea.focus();
        s_estado_mantenimiento = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_lista.setSelected(true);
                            tab_lista.setDisabled(false);
                            tab_mantenimiento.setSelected(false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            //OnChange();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                            lst_sublineas.setSelectedIndex(-1);
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lstsublineas == null || lstsublineas.isEmpty()) {
            Messagebox.show("No hay Registros de Sublineas para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionSLin.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_sublineas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una sub-linea a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            //seleccionaRegistro();
            tab_lista.setSelected(false);
            tab_lista.setDisabled(true);
            tab_mantenimiento.setSelected(true);
            txt_codlinea.setDisabled(true);
            txt_dessublinea.setDisabled(false);
            chk_tabest.setDisabled(false);
            txt_dessublinea.focus();

            //Guardar y Deshacer Activos
            tbbtn_btn_guardar.setDisabled(false);
            tbbtn_btn_deshacer.setDisabled(false);
            //Inactivar Nuevo, editar , eliminar
            tbbtn_btn_nuevo.setDisabled(true);
            tbbtn_btn_editar.setDisabled(true);
            tbbtn_btn_eliminar.setDisabled(true);
            s_estado_mantenimiento = "M";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_codlinea")
    public void onEnter_txtCodLinea() {
        if (bandera == false) {
            bandera = true;
            if (txt_codlinea.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "Sublinea";
                objMapObjetos.put("txt_linid", txt_codlinea);
                objMapObjetos.put("txt_lindes", txt_deslinea);
                objMapObjetos.put("tipo", "1");
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerSubLineas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovLineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_dessublinea.focus();
            }
        }
    }

    @Listen("onBlur=#txt_codlinea")
    public void onBlur_txtCodLinea() throws SQLException {
        if (!txt_codlinea.getValue().isEmpty()) {
            if (!txt_codlinea.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codlinea.setValue("");
                        txt_deslinea.setValue("");
                        txt_codsublinea.setValue("");
                        txt_dessublinea.setValue("");
                        txt_codlinea.focus();
                    }
                });
            } else {
                int linea = Integer.parseInt(txt_codlinea.getValue());
                Lineas objLineas = objDaoLineas.busquedaLineaxCodigo(linea);
                if (objLineas == null) {
                    Messagebox.show("El código de línea no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codlinea.setValue("");
                            txt_deslinea.setValue("");
                            txt_codsublinea.setValue("");
                            txt_dessublinea.setValue("");
                            txt_codlinea.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea " + objLineas.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_codlinea.setValue(objLineas.getTab_subdir());
                    txt_deslinea.setValue(objLineas.getTab_subdes());
                }
            }
        } else {
            txt_deslinea.setValue("");
        }
        bandera = false;
    }

    @Listen("onCtrlKey=#tabbox_sublineas")
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
            llenaRegistros();
        }
    }

    //Eventos Otros    
    @Listen("onCtrlKey=#w_sublineas")
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

    public String verificar() {
        String s_valor;
        if (txt_codlinea.getValue().isEmpty()) {
            s_valor = "El Campo Linea es Obligatorio";
            campo = "Linea";
        } else if (txt_dessublinea.getValue().isEmpty()) {
            s_valor = "El Campo Sub Linea es Obligatorio";
            campo = "Sub Linea";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    /*public void OnChange() {
     //txt_tabnomrep.setDisabled(true);
     txt_tabsubdes.setDisabled(true);
     cbx_tabtip.setDisabled(true);
     chk_tabest.setChecked(false);
     //spi_tabord.setDisabled(true);
     //limpio los datos ingresados
     txt_tabid.setValue(null);
     //txt_tabnomrep.setValue("");
     txt_tabsubdes.setValue("");
     cbx_tabtip.setValue("");
     chk_tabest.setChecked(false);
     //spi_tabord.setValue(0);
     chk_tabest.setDisabled(true);
     }*/
    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        lstsublineas = null;
        lstsublineas = new ListModelList<SubLineas>();
        lst_sublineas.setModel(lstsublineas);
    }

    public ListModelList<SubLineas> getLstsublineas() {
        return lstsublineas;
    }

    public void setLstsublineas(ListModelList<SubLineas> lstsublineas) {
        this.lstsublineas = lstsublineas;
    }

    public ListModelList<Lineas> getLsttipolineas() {
        return lsttipolineas;
    }

    public void setLsttipolineas(ListModelList<Lineas> lsttipolineas) {
        this.lsttipolineas = lsttipolineas;
    }

    //metodos sin utilizar
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void limpiarCampos() {
     chk_tabest.setChecked(true);
     txt_codlinea.setValue("");
     txt_deslinea.setValue("");
     txt_codsublinea.setValue("");
     txt_dessublinea.setValue("");
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
