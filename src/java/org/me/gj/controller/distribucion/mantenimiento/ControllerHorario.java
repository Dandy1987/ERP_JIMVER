package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.InterfaceControllers;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Horario;
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

public class ControllerHorario extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listahorario, tab_mantenimiento;
    @Wire
    Listbox lst_horario;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Button btn_buscar;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Horario> objlstHorario;
    DaoHorario objDaoHorario = new DaoHorario();
    Horario objHorario = new Horario();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "", campo = "";
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerHorario.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_horario")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstHorario = new ListModelList<Horario>();
        objlstHorario = objDaoHorario.listaHorario(1);
        lst_horario.setModel(objlstHorario);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30101000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Horario de Entrega con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Horario de Entrega con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Horario de Entrega");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Horario de Entrega");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Horario de Entrega");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Horario de Entrega");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Horario de Entrega");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Horario de Entrega");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Horario de Entrega");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Horario de Entrega");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3; // todos
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;//activos
        } else {
            i_estado = 2;//inactivos
        }
        objlstHorario = new ListModelList<Horario>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        }
        objlstHorario = objDaoHorario.busquedaHorario(i_seleccion, s_consulta, i_estado);
        if (objlstHorario.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstHorario.getSize() + " registro(s)");
        }
        if (objlstHorario.getSize() > 0) {
            lst_horario.setModel(objlstHorario);
        } else {
            objlstHorario = null;
            lst_horario.setModel(objlstHorario);
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

    @Listen("onSelect=#lst_horario")
    public void seleccionaRegistro() throws SQLException {
        objHorario = (Horario) lst_horario.getSelectedItem().getValue();
        if (objHorario == null) {
            objHorario = objlstHorario.get(i_sel + 1);
        }
        i_sel = lst_horario.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objHorario.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objHorario = new Horario();
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
        if (lst_horario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_horario.focus();
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
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        validafocos();
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
                                objlstHorario = new ListModelList<Horario>();
                                objHorario = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoHorario.insertarHorario(objHorario);
                                } else {
                                    s_mensaje = objDaoHorario.actualizarHorario(objHorario);
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
                                objlstHorario = objDaoHorario.listaHorario(1);
                                lst_horario.setModel(objlstHorario);
                                objHorario = new Horario();
                                lst_horario.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_horario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_horario.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar el horario de entrega";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoHorario.eliminarHorario(objHorario);
                                //valor = objDaoHorario.i_flagErrorBD;
                                objlstHorario = objDaoHorario.listaHorario(1);
                                lst_horario.setModel(objlstHorario);
                                limpiarCampos();
                                limpiaAuditoria();
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                lst_horario.focus();
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
                            lst_horario.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_horario.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstHorario == null || objlstHorario.isEmpty()) {
            Messagebox.show("No hay registros de horario de entrega para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_horario.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionHoraEntrega.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones
    @Listen("onCtrlKey=#w_manhor")
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

    @Listen("onOK=#txt_tabsubdes")
    public void onOKDescripcion() {
        txt_tabnomrep.focus();
    }

    @Listen("onOK=#txt_tabnomrep")
    public void onOKReport() {
        sp_tabord.focus();
    }

    @Listen("onOK=#txt_busqueda")
    public void onOKBusqueda() {
        btn_buscar.setFocus(true);
    }

    //Eventos Otros 
    public Horario generaRegistro() {
        int i_tabid;
        if (txt_tabid.getValue().isEmpty()) {
            i_tabid = 0;
        } else {
            i_tabid = Integer.parseInt(txt_tabid.getValue());
        }
        String s_tabsubdir = txt_tabid.getValue();
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
        String usu_add = objUsuCredential.getCuenta();
        String usu_mod = objUsuCredential.getCuenta();
        return new Horario(i_tabid, s_tabsubdes, s_tabsubdir, i_tabord, s_tabnomrep, i_tabest, usu_add, usu_mod);
    }

    public String verificar() {
        String mensaje = "";
        if (txt_tabsubdes.getValue().isEmpty()) {
            campo = "Descripcion";
            mensaje = "El campo 'DESCRIPCION' es obligatorio";
        } else if (txt_tabsubdes.getValue().matches("^\\s+")) {
            campo = "Descripcion";
            mensaje = "En el campo 'DESCRIPCION' no debe ingresar espacios en blanco al principio ";
        } else {
            mensaje = "";
        }
        return mensaje;
    }

    public void validafocos() {
        if (campo.equals("Descripcion")) {
            txt_tabsubdes.setValue("");
            txt_tabsubdes.focus();
        }
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstHorario = null;
        objlstHorario = new ListModelList<Horario>();
        lst_horario.setModel(objlstHorario);
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
        txt_tabid.setValue(objHorario.getTab_subdir());
        txt_tabsubdes.setValue(objHorario.getTab_subdes());
        if (objHorario.getTab_est() == 1) {
            chk_tabest.setChecked(true);
        } else {
            chk_tabest.setChecked(false);
        }
        txt_tabnomrep.setValue(objHorario.getTab_nomrep());
        sp_tabord.setValue(objHorario.getTab_ord());
        txt_usuadd.setValue(objHorario.getTab_usuadd());
        d_fecadd.setValue(objHorario.getTab_fecadd());
        txt_usumod.setValue(objHorario.getTab_usumod());
        d_fecmod.setValue(objHorario.getTab_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listahorario.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listahorario.setSelected(b_valida1);
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
