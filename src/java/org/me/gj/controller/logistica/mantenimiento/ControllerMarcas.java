package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.TtabGen;
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

public class ControllerMarcas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Textbox txt_busqueda, txt_tabdes, txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_usuadd, txt_usumod;
    @Wire
    Intbox txt_tabcod;
    @Wire
    Spinner spi_tabord;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Listbox lst_marcas;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<TtabGen> lstmarca;
    TtabGen objTtabgen;
    DaoMarcas objDaoMarcas = new DaoMarcas();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado_mantenimiento = "Q";
    String s_mensaje = "";
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMarcas.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_marcas")
    public void llenaRegistros() throws SQLException {
        lstmarca = new ListModelList<TtabGen>();
        lstmarca = new DaoMarcas().lstMarcas("1");
        lst_marcas.setModel(lstmarca);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10111000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Marcas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Marcas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Marca");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Marca");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Marca");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Marca");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Marca");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Marca");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Marcas");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Marcas");
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
        lstmarca = new ListModelList<TtabGen>();
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
        lstmarca = new DaoMarcas().Search(s_consulta, i_seleccion, i_estado);
        if (lstmarca.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + lstmarca.getSize() + " registro(s)");
        }

        if (lstmarca.getSize() > 0) {
            lst_marcas.setModel(lstmarca);
        } else {
            lstmarca = null;
            lst_marcas.setModel(lstmarca);
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

    @Listen("onSelect=#lst_marcas")
    public void seleccionaRegistro() throws SQLException {
        boolean b_val = false;
        objTtabgen = lst_marcas.getSelectedItem().getValue();
        if (objTtabgen == null) {
            objTtabgen = lstmarca.get(i_sel + 1);
        }
        i_sel = lst_marcas.getSelectedIndex();
        if (objTtabgen.getTab_est() == 1) {
            b_val = true;
        }
        chk_tabest.setChecked(b_val);
        txt_tabcod.setValue(objTtabgen.getTab_cod());
        txt_tabdes.setValue(objTtabgen.getTab_des());
        txt_tabid.setValue(objTtabgen.getTab_subdir());
        txt_tabsubdes.setValue(objTtabgen.getTab_subdes());
        spi_tabord.setValue(objTtabgen.getTab_ord());
        txt_tabnomrep.setValue(objTtabgen.getTab_nomrep());
        txt_usuadd.setValue(objTtabgen.getTab_usuadd());
        d_fecadd.setValue(objTtabgen.getTab_fecadd());
        txt_usumod.setValue(objTtabgen.getTab_usumod());
        d_fecmod.setValue(objTtabgen.getTab_fecmod());
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTtabgen.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {

        OnChange();
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
        //Campos del Mantenimiento Todos Activos Menos Codigo
        txt_tabsubdes.setDisabled(false);
        txt_tabnomrep.setDisabled(false);
        spi_tabord.setDisabled(false);
        chk_tabest.setChecked(true);
        s_estado_mantenimiento = "N";
        txt_tabsubdes.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_marcas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una marca a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            seleccionaRegistro();
            tab_lista.setSelected(false);
            tab_lista.setDisabled(true);
            tab_mantenimiento.setSelected(true);
            //tab_mantenimiento.setDisabled(false);
            txt_tabnomrep.setDisabled(false);
            txt_tabsubdes.setDisabled(false);
            spi_tabord.setDisabled(false);
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
        if (s_estado_mantenimiento.equals("N")) {
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
                if (txt_tabsubdes.getText().matches("^\\s") || txt_tabsubdes.getText().matches("^\\s+")) {
                    Messagebox.show("El Campo Marcas no debe tener espacio en blanco al principio");

                } else {
                    s_mensaje = "Esta Seguro que desea guardar los cambios?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        String s_msg;
                                        int i_tab_est = 1;
                                        objDaoMarcas = new DaoMarcas();
                                        //Recuperar en variables los valores en el formulario
                                        int i_tab_cod = 0;
                                        String s_tab_des = "";
                                        int i_tab_id = 0;
                                        String s_tab_subdes = txt_tabsubdes.getValue();
                                        int i_tab_ord = spi_tabord.getValue();
                                        String s_tab_nomrep = txt_tabnomrep.getValue();
                                        String s_tab_usuadd = objUsuCredential.getCuenta();
                                        String s_tab_usumod = objUsuCredential.getCuenta();
                                        /*  generaNroPedLista(pcab_nroped);*/
                                        objTtabgen = new TtabGen(i_tab_cod, s_tab_des, i_tab_id, s_tab_subdes, i_tab_ord, i_tab_est, s_tab_nomrep, s_tab_usuadd, s_tab_usumod);
                                        s_msg = objDaoMarcas.insertar(objTtabgen);
                                        s_mensaje = s_msg;
                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                        //Actualizamos la Lista
                                        lstmarca = new DaoMarcas().lstMarcas("1");
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_marcas.setModel(lstmarca);
                                        OnChange();
                                        limpiaAuditoria();
                                        tab_lista.setDisabled(false);
                                        tab_lista.setSelected(true);
                                        //validacion de guardar/nuevo
                                        VerificarTransacciones();
                                        tbbtn_btn_guardar.setDisabled(true);
                                        tbbtn_btn_deshacer.setDisabled(true);
                                        lst_marcas.focus();
                                        
                                        //
                                    }
                                }

                            }
                    );
                }
            }
        } else {
            if (s_estado_mantenimiento.equals("M")) {
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
                    if (txt_tabsubdes.getText().matches("^\\s")) {
                        Messagebox.show("El Campo Marcas no debe tener espacio en blanco al principio");

                    } else {
                        s_mensaje = "Esta Seguro que desea guardar los cambios?";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            String s_msg;
                                            int i_tab_est = 2;
                                            objDaoMarcas = new DaoMarcas();
                                            //Recuperar en variables los valores en el formulario
                                            int i_tab_cod = txt_tabcod.getValue();
                                            String s_tab_des = txt_tabdes.getValue();
                                            int i_tab_id = Integer.parseInt(txt_tabid.getValue());
                                            String s_tab_subdes = txt_tabsubdes.getValue();
                                            int i_tab_ord = spi_tabord.getValue();
                                            if (chk_tabest.isChecked()) {
                                                i_tab_est = 1;
                                            }
                                            String s_tab_nomrep = txt_tabnomrep.getValue();
                                            String s_tab_usuadd = objUsuCredential.getCuenta();
                                            String s_tab_usumod = objUsuCredential.getCuenta();
                                            objTtabgen = new TtabGen(i_tab_cod, s_tab_des, i_tab_id, s_tab_subdes, i_tab_ord, i_tab_est, s_tab_nomrep, s_tab_usuadd, s_tab_usumod);
                                            s_msg = objDaoMarcas.modificar(objTtabgen);
                                            s_mensaje = s_msg;
                                            Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                            //Actualizamos la Lista
                                            lstmarca = new DaoMarcas().lstMarcas("1");
                                            cb_estado.setSelectedIndex(0);
                                            cb_busqueda.setSelectedIndex(0);
                                            txt_busqueda.setValue("%%");
                                            txt_busqueda.setDisabled(true);
                                            lst_marcas.setModel(lstmarca);
                                            OnChange();
                                            limpiaAuditoria();
                                            tab_lista.setDisabled(false);
                                            tab_lista.setSelected(true);
                                            //validacion de guardar/nuevo
                                            VerificarTransacciones();
                                            tbbtn_btn_guardar.setDisabled(true);
                                            tbbtn_btn_deshacer.setDisabled(true);
                                            lst_marcas.focus();
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
        if (lst_marcas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una marca a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoMarcas.validaMovimientos(objTtabgen);
            valor = objDaoMarcas.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoMarcas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Esta Seguro que desea Eliminar la Marca";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    int i_tab_id = Integer.parseInt(txt_tabid.getValue());
                                    s_mensaje = objDaoMarcas.eliminar(i_tab_id);
                                    valor = objDaoMarcas.i_flagErrorBD;
                                    if (valor == 0) {
                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                        //Seteamos la Lista
                                        lstmarca = new ListModelList<TtabGen>();
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        lstmarca = new DaoMarcas().lstMarcas("1");
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_marcas.setModel(lstmarca);
                                        OnChange();
                                        limpiaAuditoria();
                                        tab_lista.setSelected(true);
                                        lst_marcas.focus();
                                    } else {
                                        Messagebox.show(objDaoMarcas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {

                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_lista.setSelected(true);
                            tab_lista.setDisabled(false);
                            tab_mantenimiento.setSelected(false);
                            //tab_mantenimiento.setDisabled(true);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_marcas.focus();
                            //
                            OnChange();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lstmarca == null || lstmarca.isEmpty()) {
            Messagebox.show("No hay Registros de Marcas para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionMar.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
   /* @Listen("onCtrlKey=#tabbox_marcas")
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
*/
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            llenaRegistros();
        }
    }

    //Eventos Otros 
    public String verificar() {
        String s_valor;
        if (txt_tabsubdes.getValue().isEmpty()) {
            s_valor = "El Campo Marca es Obligatorio";
        } else {
            s_valor = "";
        }
        return s_valor;
    }
    
    //Eventos Secundarios - Validacion
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

    public void OnChange() {
        txt_tabnomrep.setDisabled(true);
        txt_tabsubdes.setDisabled(true);
        chk_tabest.setChecked(false);
        spi_tabord.setDisabled(true);
        //limpio los datos ingresados
        txt_tabid.setValue(null);
        txt_tabnomrep.setValue("");
        txt_tabsubdes.setValue("");
        chk_tabest.setChecked(false);
        spi_tabord.setValue(0);
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
        lstmarca = null;
        lstmarca = new ListModelList<TtabGen>();
        lst_marcas.setModel(lstmarca);
    }

    //metodos sin utilizar
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
