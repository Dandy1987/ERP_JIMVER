package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.Umedida;
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

public class ControllerUnidMedida extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_mantenimiento, tab_lista;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Textbox txt_busqueda, txt_tabsubdes, txt_tabsubdir, txt_tabtip, txt_tabnomrep, txt_usuadd, txt_usumod;
    @Wire
    Textbox txt_tabid;
    @Wire
    Spinner spi_tabtip;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Listbox lst_umedida;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Umedida> lstumedida;
    Umedida objUmedida;
    DaoUmedida objDaoUmedida = new DaoUmedida();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado_mantenimiento = "Q";
    String s_mensaje = "";
    String campo = "";
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUnidMedida.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_umedida")
    public void llenaRegistros() throws SQLException {
        lstumedida = new ListModelList<Umedida>();
        lstumedida = new DaoUmedida().listaUmedida(1);
        lst_umedida.setModel(lstumedida);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10114000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Unidad de Manejo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Unidad de Manejo con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Unidad de Manejo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Unidad de Manejo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Unidad de Manejo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Unidad de Manejo");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Unidad de Manejo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Unidad de Manejo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Unidad de Manejo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Unidad de Manejo");
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
        lstumedida = new ListModelList<Umedida>();
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
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la abreviatura " + s_consulta + " para su busqueda");
        }

        if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la Orden Sunat " + s_consulta + " para su busqueda");
        }
        lstumedida = new DaoUmedida().busqueda(s_consulta, i_seleccion, i_estado);

        // Validar Tabla sin registro
        if (lstumedida.getSize() > 0) {
            lst_umedida.setModel(lstumedida);
        } else {
            lstumedida = null;
            lst_umedida.setModel(lstumedida);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        OnChange();
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

    @Listen("onSelect=#lst_umedida")
    public void seleccionaRegistro() throws SQLException {
        objUmedida = lst_umedida.getSelectedItem().getValue();
        if (objUmedida == null) {
            objUmedida = lstumedida.get(i_sel + 1);
        }
        i_sel = lst_umedida.getSelectedIndex();
        chk_tabest.setChecked(objUmedida.isValor());
        chk_tabest.setLabel(objUmedida.isValor() ? "ACTIVO" : "INACTIVO");
        //txt_tabid.setValue(objUmedida.getTab_id());
        txt_tabid.setValue(objUmedida.getStab_id());
        txt_tabsubdes.setValue(objUmedida.getTab_subdes());
        txt_tabsubdir.setValue(objUmedida.getTab_subdir());
        spi_tabtip.setValue(objUmedida.getTab_tip());
        txt_tabnomrep.setValue(objUmedida.getTab_nomrep());
        txt_usuadd.setValue(objUmedida.getTab_usuadd());
        d_fecadd.setValue(objUmedida.getTab_fecadd());
        txt_usumod.setValue(objUmedida.getTab_usumod());
        d_fecmod.setValue(objUmedida.getTab_fecmod());
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objUmedida.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        OnChange();
        tab_lista.setSelected(false);
        tab_mantenimiento.setSelected(true);
        tab_lista.setDisabled(true);
        //Guardar y Deshacer Activos
        tbbtn_btn_guardar.setDisabled(false);
        tbbtn_btn_deshacer.setDisabled(false);
        //Inactivar Nuevo, editar , eliminar
        tbbtn_btn_nuevo.setDisabled(true);
        tbbtn_btn_editar.setDisabled(true);
        tbbtn_btn_eliminar.setDisabled(true);
        //Campos del Mantenimiento Todos Activos Menos Codigo
        txt_tabsubdes.setDisabled(false);
        txt_tabnomrep.setDisabled(false);
        txt_tabsubdir.setDisabled(false);
        spi_tabtip.setDisabled(false);
        chk_tabest.setChecked(true);
        chk_tabest.setDisabled(true);
        s_estado_mantenimiento = "N";
        txt_tabsubdes.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_umedida.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Unidad de Manejo a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            seleccionaRegistro();
            tab_lista.setSelected(false);
            tab_lista.setDisabled(true);
            tab_mantenimiento.setSelected(true);
            //tab_mantenimiento.setDisabled(false);
            txt_tabnomrep.setDisabled(false);
            txt_tabsubdes.setDisabled(false);
            txt_tabsubdir.setDisabled(false);
            spi_tabtip.setDisabled(false);
            chk_tabest.setDisabled(false);
            txt_tabsubdes.focus();

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

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_abrev;
        String s_valida = verificar();
        if (s_estado_mantenimiento.equals("N")) {
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("Descripcion")) {
                                txt_tabsubdes.focus();
                            } else if (campo.equals("Abreviatura")) {
                                txt_tabsubdir.focus();
                            }
                        }
                    }
                });
            } else {
                s_abrev = txt_tabsubdir.getValue().toString().toUpperCase();
                valor = objDaoUmedida.ValidaAbrev(s_abrev, 0);
                if (valor >= 1) {
                    Messagebox.show("La abreviatura ya existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    s_mensaje = "Está seguro que desea guardar los cambios?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        String s_msg;
                                        int i_tab_est = 2;
                                        int i_tab_id = 0;

                                        //Recuperar en variables los valores en el formulario
                                        String s_tab_subdes = txt_tabsubdes.getValue().toUpperCase();
                                        String s_tab_subdir = txt_tabsubdir.getValue().toUpperCase();
                                        int i_tab_tip = spi_tabtip.getValue();
                                        if (chk_tabest.isChecked()) {
                                            i_tab_est = 1;
                                        }
                                        String s_tab_nomrep = txt_tabnomrep.getValue().toUpperCase();
                                        String tab_usuadd = objUsuCredential.getCuenta();
                                        String tab_usumod = objUsuCredential.getCuenta();
                                        /*  generaNroPedLista(pcab_nroped);*/
                                        objUmedida = new Umedida(i_tab_id, s_tab_subdes, i_tab_tip, s_tab_subdir, i_tab_est, s_tab_nomrep, tab_usuadd, tab_usumod);
                                        s_msg = objDaoUmedida.insertar(objUmedida);
                                        s_mensaje = s_msg;
                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                        lstumedida = new DaoUmedida().listaUmedida(1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_umedida.setModel(lstumedida);
                                        OnChange();
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
            }
        } else {
            if (s_estado_mantenimiento.equals("M")) {
                if (!s_valida.equals("")) {
                    Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                if (campo.equals("Descripcion")) {
                                    txt_tabsubdes.focus();
                                } else if (campo.equals("Abreviatura")) {
                                    txt_tabsubdir.focus();
                                }
                            }
                        }
                    });
                } else {
                    valor = objDaoUmedida.ValidaAbrev(txt_tabsubdir.getValue().toUpperCase(), Integer.parseInt(txt_tabid.getValue()));
                    if (valor >= 1) {
                        Messagebox.show("La abreviatura ya existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        s_mensaje = "Está seguro que desea guardar los cambios?";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            int i_tab_est = 2;
                                            //Recuperar en variables los valores en el formulario
                                            int i_tab_id = Integer.parseInt(txt_tabid.getValue());
                                            String s_tab_subdes = txt_tabsubdes.getValue();
                                            String s_tab_subdir = txt_tabsubdir.getValue();
                                            int i_tab_tip = spi_tabtip.getValue();
                                            if (chk_tabest.isChecked()) {
                                                i_tab_est = 1;
                                            }
                                            String s_tab_nomrep = txt_tabnomrep.getValue();
                                            String tab_usuadd = objUsuCredential.getCuenta();
                                            String tab_usumod = objUsuCredential.getCuenta();
                                            /*  generaNroPedLista(pcab_nroped);*/
                                            objUmedida = new Umedida(i_tab_id, s_tab_subdes, i_tab_tip, s_tab_subdir, i_tab_est, s_tab_nomrep, tab_usuadd, tab_usumod);
                                            s_mensaje = objDaoUmedida.modificar(objUmedida);
                                            Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                            lstumedida = new DaoUmedida().listaUmedida(1);
                                            cb_estado.setSelectedIndex(0);
                                            cb_busqueda.setSelectedIndex(0);
                                            txt_busqueda.setValue("%%");
                                            txt_busqueda.setDisabled(true);
                                            lst_umedida.setModel(lstumedida);
                                            OnChange();
                                            limpiaAuditoria();
                                            tab_lista.setSelected(true);
                                            tab_lista.setDisabled(false);
                                            //validacion de guardar/nuevo
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
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_umedida.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Unidad de Medida a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoUmedida.validaMovimientos(objUmedida);
            valor = objDaoUmedida.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoUmedida.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Está seguro que desea eliminar la Unidad de Medida";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoUmedida.eliminar(objUmedida);
                                    valor = objDaoUmedida.i_flagErrorBD;
                                    if (valor == 0) {
                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        //
                                        lstumedida = new DaoUmedida().listaUmedida(1);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_umedida.setModel(lstumedida);
                                        OnChange();
                                        limpiaAuditoria();
                                        tab_lista.setSelected(true);
                                    } else {
                                        Messagebox.show(objDaoUmedida.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        });
            }
        }
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
                            OnChange();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lstumedida == null || lstumedida.isEmpty()) {
            Messagebox.show("No hay Registros de Unidad de Medida para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionUmed.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
   /*@Listen("onCtrlKey=#tabbox_umedida")
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
  
    @Listen("onCtrlKey=#w_marca")
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
            llenaRegistros();
        }
    }

    //Eventos Otros 
    public String verificar() {
        String s_valor;
        if (txt_tabsubdes.getValue().isEmpty() || txt_tabsubdes.getText().trim().length() == 0) {
            s_valor = "El Campo Descripcion es Obligatorio";
            campo = "Descripcion";
        } else if (txt_tabsubdir.getValue().isEmpty() || txt_tabsubdir.getText().trim().length() == 0) {
            s_valor = "El Campo Abreviatura es Obligatorio";
            campo = "Abreviatura";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void OnChange() {

        txt_tabnomrep.setDisabled(true);
        txt_tabsubdes.setDisabled(true);
        txt_tabsubdir.setDisabled(true);
        chk_tabest.setChecked(false);
        chk_tabest.setLabel("ACTIVO");
        spi_tabtip.setDisabled(true);
        //limpio los datos ingresados
        txt_tabid.setValue(null);
        txt_tabnomrep.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabsubdir.setValue("");
        chk_tabest.setChecked(false);
        spi_tabtip.setValue(0);
        chk_tabest.setDisabled(true);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        lstumedida = null;
        lstumedida = new ListModelList<Umedida>();
        lst_umedida.setModel(lstumedida);
    }

    //metodos que no se utilizan
    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
