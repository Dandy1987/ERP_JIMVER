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
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Executions;

public class ControllerUnidManejo extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_mantenimiento, tab_lista;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Textbox txt_busqueda, txt_tabdes, txt_tabsubdes, txt_tabsubdir, /*txt_tabnomrep,*/ txt_usuadd, txt_usumod;
    @Wire
    Intbox txt_tabcod, txt_tabid;
    @Wire
    Spinner spi_tabord;
    @Wire
    Checkbox chk_tabest;
    @Wire
    Listbox lst_umanejo;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<TtabGen> lstumanejo;
    TtabGen objTtabGen = new TtabGen();
    DaoUmanejo objDaoUmanejo = new DaoUmanejo();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado_mantenimiento = "Q";
    String s_mensaje = "";
    String campo = "";
    int i_sel;
    int i_valor;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUnidManejo.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_umanejo")
    public void llenaRegistros() throws SQLException {
        lstumanejo = new ListModelList<TtabGen>();
        lstumanejo = new DaoUmanejo().lstUmanejo("1");
        lst_umanejo.setModel(lstumanejo);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
        lst_umanejo.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10112000, usuario, empresa, sucursal);
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
        int i_seleccion;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        
        lstumanejo = new ListModelList<TtabGen>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la abreviatura " + s_consulta + " para su busqueda");
        } else {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        lstumanejo = new DaoUmanejo().Search(s_consulta, i_seleccion, i_estado);

        if (lstumanejo.getSize() > 0) {
            lst_umanejo.setModel(lstumanejo);
        } else {
            lstumanejo = null;
            lst_umanejo.setModel(lstumanejo);
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

    @Listen("onSelect=#lst_umanejo")
    public void seleccionaRegistro() throws SQLException {
        boolean b_val = false;
        objTtabGen = lst_umanejo.getSelectedItem().getValue();
        if (objTtabGen == null) {
            objTtabGen = lstumanejo.get(i_sel + 1);
        }
        i_sel = lst_umanejo.getSelectedIndex();
        if (objTtabGen.getTab_est() == 1) {
            b_val = true;
        }
        chk_tabest.setChecked(b_val);
        txt_tabcod.setValue(objTtabGen.getTab_cod());
        txt_tabdes.setValue(objTtabGen.getTab_des());
        txt_tabid.setValue(objTtabGen.getTab_id());
        txt_tabsubdes.setValue(objTtabGen.getTab_subdes());
        txt_tabsubdir.setValue(objTtabGen.getTab_subdir());
        spi_tabord.setValue(objTtabGen.getTab_ord());
        //txt_tabnomrep.setValue(objTtabGen.getTab_nomrep());
        txt_usuadd.setValue(objTtabGen.getTab_usuadd());
        d_fecadd.setValue(objTtabGen.getTab_fecadd());
        txt_usumod.setValue(objTtabGen.getTab_usumod());
        d_fecmod.setValue(objTtabGen.getTab_fecmod());
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTtabGen.getTab_id());
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
        //txt_tabnomrep.setDisabled(false);
        txt_tabsubdir.setDisabled(false);
        spi_tabord.setDisabled(false);
        chk_tabest.setChecked(true);
        chk_tabest.setDisabled(true);
        s_estado_mantenimiento = "N";
        txt_tabsubdir.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_umanejo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione la unidad de manejo a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            seleccionaRegistro();
            tab_lista.setSelected(false);
            tab_lista.setDisabled(true);
            tab_mantenimiento.setSelected(true);
            //tab_mantenimiento.setDisabled(false);
            //txt_tabnomrep.setDisabled(false);
            txt_tabsubdes.setDisabled(false);
            txt_tabsubdir.setDisabled(false);
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
        String s_abrev;
        int tab_id;
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Abreviatura")) {
                            txt_tabsubdir.focus();
                        } else if (campo.equals("Descripcion")) {
                            txt_tabsubdes.focus();
                        }
                    }
                }
            });
        } else {
            s_abrev = txt_tabsubdir.getValue().toUpperCase();
            tab_id = txt_tabid.getValue() == null ? 0 : txt_tabid.getValue();
            valor = objDaoUmanejo.ValidaAbrev(s_abrev, tab_id);
            if (valor >= 1) {
                Messagebox.show("La abreviatura ya existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_tabsubdir.focus();
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
                                    ParametrosSalida param;

                                    int i_tab_cod = txt_tabcod.getValue() == null ? 0 : txt_tabcod.getValue();
                                    int i_tab_id = txt_tabid.getValue() == null ? 0 : txt_tabid.getValue();
                                    int i_tab_est = chk_tabest.isChecked() ? 1 : 2;
                                    String s_tab_des = txt_tabdes.getValue().isEmpty() ? "" : txt_tabdes.getValue();
                                    String s_tab_subdes = txt_tabsubdes.getValue();
                                    String s_tab_subdir = txt_tabsubdir.getValue();
                                    int i_tab_ord = spi_tabord.getValue();
                                    String s_tab_nomrep = "";//txt_tabnomrep.getValue();
                                    String s_tab_usuadd = objUsuCredential.getCuenta();
                                    String s_tab_usumod = objUsuCredential.getCuenta();
                                    objTtabGen = new TtabGen(i_tab_cod, s_tab_des, i_tab_id, s_tab_subdes, s_tab_subdir, i_tab_ord, i_tab_est, s_tab_nomrep, s_tab_usuadd, s_tab_usumod);

                                    if (s_estado_mantenimiento.equals("N")) {
                                        param = objDaoUmanejo.insertar(objTtabGen);
                                    } else {
                                        param = objDaoUmanejo.modificar(objTtabGen);
                                    }

                                    if (param.getFlagIndicador() == 0) {
                                        lstumanejo = new DaoUmanejo().lstUmanejo("1");
                                        cb_busqueda.setSelectedIndex(0);
                                        cb_estado.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_umanejo.setModel(lstumanejo);
                                        OnChange();
                                        limpiaAuditoria();
                                        tab_lista.setDisabled(false);
                                        tab_lista.setSelected(true);
                                        VerificarTransacciones();
                                        tbbtn_btn_guardar.setDisabled(true);
                                        tbbtn_btn_deshacer.setDisabled(true);

                                    }

                                    //Messagebox.show(param.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    Messagebox.show(param.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                lst_umanejo.focus();
                                            }
                                        }
                                    });

                                    //
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_umanejo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione la unidad de manejo a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoUmanejo.validaMovimientos(objTtabGen);
            valor = objDaoUmanejo.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoUmanejo.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Está seguro que desea eliminar la unidad de manejo";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                    ParametrosSalida param;
                                    param = objDaoUmanejo.eliminar(objTtabGen);
                                    if (param.getFlagIndicador() == 0) {
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        //
                                        lstumanejo = new DaoUmanejo().lstUmanejo("1");
                                        cb_busqueda.setSelectedIndex(0);
                                        cb_estado.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        lst_umanejo.setModel(lstumanejo);
                                        OnChange();
                                        limpiaAuditoria();
                                        tab_lista.setSelected(true);
                                        lst_umanejo.focus();
                                    }
                                    
                                    Messagebox.show(param.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                lst_umanejo.focus();
                                            }
                                        }
                                    });

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
                            lst_umanejo.focus();
                            //
                            OnChange();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lstumanejo == null || lstumanejo.isEmpty()) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionUman.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manunimanejo")
    public void ctrl_teclado(Event event) throws SQLException {
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

    @Listen("onOK=#txt_tabsubdir")
    public void nextUnidadManejo() throws SQLException {
        if (!txt_tabsubdir.getValue().isEmpty()) {
            txt_tabsubdes.focus();
        }
    }

    @Listen("onOK=#txt_tabsubdes")
    public void nextReporte() throws SQLException {
        if (!txt_tabsubdes.getValue().isEmpty()) {
            spi_tabord.focus();
        }
    }

    /*@Listen("onOK=#txt_tabnomrep")
     public void nextOrden() throws SQLException {
     spi_tabord.focus();
     }*/
    @Listen("onOK=#spi_tabord")
    public void nextGuardar() throws SQLException {
        botonGuardar();
    }

    //Eventos Otros 
    public void OnChange() {
        //txt_tabnomrep.setDisabled(true);
        txt_tabsubdes.setDisabled(true);
        txt_tabsubdir.setDisabled(true);
        chk_tabest.setChecked(false);
        spi_tabord.setDisabled(true);
        //limpio los datos ingresados
        txt_tabid.setValue(null);
        //txt_tabnomrep.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabsubdir.setValue("");
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

    public String verificar() {
        String s_valor;
        if (txt_tabsubdir.getValue().trim().length() == 0) {
            s_valor = "El campo ABREVIATURA es obligatorio";
            campo = "Abreviatura";
        } /*else if (txt_tabsubdir.getValue().trim().length() != 3) {
         s_valor = "El campo ABREVIATURA debe tener 3 caracteres";
         campo = "Abreviatura";
         }*/ else if (txt_tabsubdes.getValue().trim().length() == 0) {
            s_valor = "El campo DESCRIPCION es obligatorio";
            campo = "Descripcion";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void LimpiarLista() {
        //limpio mi lista
        lstumanejo = null;
        lstumanejo = new ListModelList<TtabGen>();
        lst_umanejo.setModel(lstumanejo);
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
