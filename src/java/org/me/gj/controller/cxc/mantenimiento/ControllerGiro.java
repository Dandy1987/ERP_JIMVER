package org.me.gj.controller.cxc.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Giro;
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

public class ControllerGiro extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listagiros, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_giros;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Giro> objlstGiros;
    DaoGiro objDaoGiro = new DaoGiro();
    Giro objGiro = new Giro();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "";
    String foco = "";
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(ControllerGiro.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_giros")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstGiros = new ListModelList<Giro>();
        objlstGiros = objDaoGiro.listaGiros(1);
        lst_giros.setModel(objlstGiros);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
        cb_busqueda.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20103000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Mantenimiento de Giro de Negocio con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Giro de Negocio con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Giro de Negocio");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Giro de Negocio");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de Giro de Negocio");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de Giro de Negocio");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de Giro de Negocio");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de Giro de Negocio");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Giros de Negocio");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Giros de Negocio");
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
        objlstGiros = new ListModelList<Giro>();
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
        objlstGiros = objDaoGiro.busquedaGiros(i_seleccion, s_consulta, i_estado);
        if (objlstGiros.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstGiros.getSize() + " registro(s)");
        }
        if (objlstGiros.getSize() > 0) {
            lst_giros.setModel(objlstGiros);
        } else {
            objlstGiros = null;
            lst_giros.setModel(objlstGiros);
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

    @Listen("onSelect=#lst_giros")
    public void seleccionaRegistro() throws SQLException {
        objGiro = (Giro) lst_giros.getSelectedItem().getValue();
        if (objGiro == null) {
            objGiro = objlstGiros.get(i_sel + 1);
        }
        i_sel = lst_giros.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objGiro.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objGiro = new Giro();
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
//Eventos Otros

    @Listen("onCtrlKey=#w_mangiro")
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

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_giros.getSelectedIndex() == -1) {
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
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if ( ((Integer) event.getData()).intValue() == Messagebox.OK  ) {
                    if (foco.equals("descrip")) {
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
                            if (((Integer) event.getData()).intValue() == Messagebox.OK /*Messagebox.show("Datos Guardados Satisfactoriamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION)*/) {
                                objlstGiros = new ListModelList<Giro>();
                                objGiro = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoGiro.insertarGiro(objGiro);
                                } else {
                                    s_mensaje = objDaoGiro.actualizarGiro(objGiro);
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
                                objlstGiros = objDaoGiro.listaGiros(0);
                                lst_giros.setModel(objlstGiros);
                                objGiro = new Giro();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_giros.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar el Giro de Negocio";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK /*Messagebox.show("Datos Eliminados Satisfactoriamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION)*/) {
                                s_mensaje = objDaoGiro.eliminarGiro(objGiro);
                                valor = objDaoGiro.i_flagErrorBD;
                                if (valor == 0) {
                                    Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                    objlstGiros = objDaoGiro.listaGiros(0);
                                    lst_giros.setModel(objlstGiros);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                } else {
                                    Messagebox.show(objDaoGiro.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            lst_giros.setSelectedIndex(-1);
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
        if (objlstGiros == null || objlstGiros.isEmpty()) {
            Messagebox.show("No hay registros de Formas de Pago para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/cxc/mantenimiento/LovImpresionGiro.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones  
    @Listen("onCtrlKey=#tabbox_giros")
    public void GuardarInformacion(Event event) throws SQLException {
        if (!tbbtn_btn_guardar.isDisabled()) {
            int keyCode;
            keyCode = ((KeyEvent) event).getKeyCode();
            switch (keyCode) {
                case 121:
                    if (!tbbtn_btn_guardar.isDisabled()) {
                        botonGuardar();
                    }
            }
        }
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstGiros = new ListModelList<Giro>();
            objlstGiros = objDaoGiro.listaGiros(0);
            lst_giros.setModel(objlstGiros);
        }
    }

    //Eventos Otros 
    public Giro generaRegistro() {
        int i_tabid = txt_tabid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_tabid.getValue());
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
        return new Giro(i_tabid, s_tabsubdes, i_tabord, s_tabnomrep, i_tabest);
    }

    public String verificar() {
        String s_valor = "";
        if (txt_tabsubdes.getValue().isEmpty()) {
            s_valor = "Por Favor Ingrese el Campo Descripción";
            foco = "descrip";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstGiros = null;
        objlstGiros = new ListModelList<Giro>();
        lst_giros.setModel(objlstGiros);
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
        txt_tabid.setValue(objGiro.getTab_subdir());
        txt_tabsubdes.setValue(objGiro.getTab_subdes());
        if (objGiro.getTab_est() == 1) {
            chk_tabest.setChecked(true);
        } else {
            chk_tabest.setChecked(false);
        }
        txt_tabnomrep.setValue(objGiro.getTab_nomrep());
        sp_tabord.setValue(objGiro.getTab_ord());
        txt_usuadd.setValue(objGiro.getTab_usuadd());
        d_fecadd.setValue(objGiro.getTab_fecadd());
        txt_usumod.setValue(objGiro.getTab_usumod());
        d_fecmod.setValue(objGiro.getTab_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listagiros.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listagiros.setSelected(b_valida1);
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
