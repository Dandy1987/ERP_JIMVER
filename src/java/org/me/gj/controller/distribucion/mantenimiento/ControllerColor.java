package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Color;
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

public class ControllerColor extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listacolor, tab_mantenimiento;
    @Wire
    Listbox lst_color;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabsubdir, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Spinner sp_tabord;
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
    ListModelList<Color> objlstColor;
    DaoColor objDaoColor = new DaoColor();
    Color objColor = new Color();
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
    private static final Logger LOGGER = Logger.getLogger(ControllerColor.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_color")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstColor = new ListModelList<Color>();
        objlstColor = objDaoColor.listaColor(1);
        lst_color.setModel(objlstColor);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30104030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Color con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Color con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Color");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Color");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Color");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Color");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Color");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Color");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Color");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Color");
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
        objlstColor = new ListModelList<Color>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstColor = objDaoColor.busquedaColor(i_seleccion, s_consulta, i_estado);
        lst_color.setModel(objlstColor);
        if (objlstColor.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstColor.getSize() + " registro(s)");
        }
        if (objlstColor.getSize() > 0) {
            lst_color.setModel(objlstColor);
        } else {
            objlstColor = null;
            lst_color.setModel(objlstColor);
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

    @Listen("onSelect=#lst_color")
    public void seleccionaRegistro() throws SQLException {
        objColor = (Color) lst_color.getSelectedItem().getValue();
        if (objColor == null) {
            objColor = objlstColor.get(i_sel + 1);
        }
        i_sel = lst_color.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objColor.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objColor = new Color();
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
        if (lst_color.getSelectedIndex() == -1) {
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
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
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
                                objlstColor = new ListModelList<Color>();
                                objColor = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoColor.insertarColor(objColor);
                                } else {
                                    s_mensaje = objDaoColor.actualizarColor(objColor);
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
                                objlstColor = objDaoColor.listaColor(1);
                                lst_color.setModel(objlstColor);
                                objColor = new Color();
                                lst_color.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_color.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar el color del vehículo";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoColor.eliminarColor(objColor);
                                valor = objDaoColor.i_flagErrorBD;
                                if (valor == 0) {
                                    Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                    objlstColor = objDaoColor.listaColor(1);
                                    lst_color.setModel(objlstColor);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                } else {
                                    Messagebox.show(objDaoColor.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
        }
        lst_color.focus();
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
                            lst_color.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_color.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstColor == null || objlstColor.isEmpty()) {
            Messagebox.show("No hay registros de color de vehículos para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_color.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionColVehiculo.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones
    @Listen("onCtrlKey=#w_mancolor")
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
    public void onOKCategoria() {
        txt_tabsubdir.focus();
    }

    @Listen("onOK=#txt_tabsubdir")
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
    public Color generaRegistro() {
        int i_tabid;
        if (txt_tabid.getValue().isEmpty()) {
            i_tabid = 0;
        } else {
            i_tabid = Integer.parseInt(txt_tabid.getValue());
        }
        String s_tabsubdes = txt_tabsubdes.getValue().toUpperCase().trim();
        String s_tabsubdir = txt_tabsubdir.getValue().toUpperCase().trim();
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
        return new Color(i_tabid, s_tabsubdes, s_tabsubdir, i_tabord, s_tabnomrep, i_tabest, usu_add, usu_mod);
    }

    public String verificar() {
        String mensaje = "";
        if (txt_tabsubdes.getValue().isEmpty()) {
            campo = "color";
            mensaje = "El campo 'COLOR' es obligatorio";
        } else if (txt_tabsubdes.getValue().matches("^\\s+")) {
            campo = "color";
            mensaje = "En el campo 'COLOR' no debe ingresar espacios en blanco al principio ";
        }else if (!txt_tabsubdes.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "color";
            mensaje = "En el campo 'COLOR' solo ingresar letras ";
            txt_tabsubdes.setValue("");
        } else if (txt_tabsubdir.getValue().isEmpty()) {
            campo = "descripcion";
            mensaje = "El campo 'DESCRIPCION' es obligatorio";
        } else if (txt_tabsubdir.getValue().matches("^\\s+")) {
            campo = "descripcion";
            mensaje = "En el campo 'DESCRIPCION' no debe ingresar espacios en blanco al principio ";           
        }else if (!txt_tabsubdir.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "descripcion";
            mensaje = "En el campo 'DESCRIPCION' solo ingresar letras ";
            txt_tabsubdir.setValue("");
        } else {
            mensaje = "";
        }
        return mensaje;
    }

    public void validafocos() {
        if (campo.equals("color")) {
            txt_tabsubdes.focus();
        } else if (campo.equals("descripcion")) {
            txt_tabsubdir.focus();
        }
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstColor = null;
        objlstColor = new ListModelList<Color>();
        lst_color.setModel(objlstColor);
    }

    public void limpiarCampos() {
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabnomrep.setValue("");
        txt_tabsubdir.setValue("");
        sp_tabord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdir.setDisabled(b_valida);
        txt_tabsubdes.setDisabled(b_valida);
        chk_tabest.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);
    }

    public void llenarCampos() {
        txt_tabid.setValue(String.valueOf(objColor.getTab_id()));
        txt_tabsubdes.setValue(objColor.getTab_subdes());
        txt_tabsubdir.setValue(objColor.getTab_subdir());
        if (objColor.getTab_est() == 1) {
            chk_tabest.setChecked(true);
        } else {
            chk_tabest.setChecked(false);
        }
        txt_tabnomrep.setValue(objColor.getTab_nomrep());
        sp_tabord.setValue(objColor.getTab_ord());
        txt_usuadd.setValue(objColor.getTab_usuadd());
        d_fecadd.setValue(objColor.getTab_fecadd());
        txt_usumod.setValue(objColor.getTab_usumod());
        d_fecmod.setValue(objColor.getTab_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacolor.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacolor.setSelected(b_valida1);
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

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
