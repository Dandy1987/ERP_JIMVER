package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.Canal;
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

public class ControllerCanal extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listacanales, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_tabid, txt_tabsubdes, txt_tabnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_busest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Listbox lst_canales;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Canal> objlstCanales = new ListModelList<Canal>();
    DaoCanal objDaoCanales = new DaoCanal();
    Canal objCanal = new Canal();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q";
    String s_mensaje = "";
    private String campo = "";
    public static boolean bandera;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCanal.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_canales")
    public void llenaRegistros() throws SQLException {
        objlstCanales = objDaoCanales.listaCanales(1);
        lst_canales.setModel(objlstCanales);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40101000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de canales con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de canales con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo canal");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo canal");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un canal");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un canal");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un canal");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un canal");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de canales");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de canales");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {

        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_busest.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_busest.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }

        objlstCanales = new ListModelList<Canal>();

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

        objlstCanales = objDaoCanales.busquedaCanales(i_seleccion, s_consulta, i_estado);

        if (objlstCanales.getSize() > 0) {
            lst_canales.setModel(objlstCanales);
        } else {
            limpiarLista();
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

    @Listen("onSelect=#lst_canales")
    public void seleccionaRegistro() throws SQLException {
        objCanal = (Canal) lst_canales.getSelectedItem().getValue();
        if (objCanal == null) {
            objCanal = objlstCanales.get(i_sel + 1);
        }
        i_sel = lst_canales.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCanal.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objCanal = new Canal();
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
    public void botonEditar() throws SQLException {
        if (lst_canales.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                        if (campo.equals("Descripcion")) {
                            txt_tabsubdes.focus();
                        } else if (campo.equals("Orden")) {
                            sp_tabord.focus();
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
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstCanales = new ListModelList<Canal>();
                                objCanal = (Canal) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoCanales.insertarCanal(objCanal);
                                } else {
                                    s_mensaje = objDaoCanales.actualizarCanal(objCanal);
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
                                objlstCanales = objDaoCanales.listaCanales(1);
                                lst_canales.setModel(objlstCanales);
                                objCanal = new Canal();
                                lst_canales.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_canales.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este canal?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String msg = objDaoCanales.eliminarCanal(objCanal);
                                objlstCanales = new ListModelList<Canal>();
                                objlstCanales = objDaoCanales.listaCanales(1);
                                lst_canales.setModel(objlstCanales);
                                //limpiarLista();
                                limpiarCampos();
                                Messagebox.show(msg,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                //
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
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_canales.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_mancan")
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
            objlstCanales = new ListModelList<Canal>();
            objlstCanales = objDaoCanales.listaCanales(0);
            lst_canales.setModel(objlstCanales);
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstCanales == null || objlstCanales.isEmpty()) {
            Messagebox.show("No hay registros de Canales para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionCanal.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onOK=#txt_tabsubdes")
    public void onOKcanal() {
        txt_tabnomrep.focus();
    }

    @Listen("onOK=#txt_tabnomrep")
    public void onOKrepote() {
        sp_tabord.focus();
    }

    @Listen("onOK=#sp_tabord")
    public void onOKorden() {
        botonGuardar();
    }

    @Listen("onCheck=#chk_tabest")
    public void VerificaEstado() throws SQLException {
        int i_tabest = objCanal.getTab_est();
        if (objDaoCanales.busquedaExistencia(1, txt_tabid.getValue()) > 0 || objDaoCanales.busquedaExistencia(2, txt_tabid.getValue()) > 0 || objDaoCanales.busquedaExistencia(3, txt_tabid.getValue()) > 0) {
            if (i_tabest == 1) {
                chk_tabest.setChecked(true);
                chk_tabest.setLabel("ACTIVO");
                Messagebox.show("El Canal esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                chk_tabest.setChecked(false);
                chk_tabest.setLabel("INACTIVO");
                Messagebox.show("Ya no se puede activar el canal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    //Eventos Otros
    public String verificar() {
        String s_valor;
        if (txt_tabsubdes.getValue().isEmpty()) {
            s_valor = "El campo Descripción es obligatorio";
            campo = "Descripcion";
        } else if (txt_tabsubdes.getValue().matches("(\\s)+")) {
            s_valor = "El campo Canal , no debe tener espacios en blancos al principio";
            campo = "Descripcion";
            txt_tabsubdes.setValue("");
        } else if (!txt_tabsubdes.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "El campo Canal, no se permite caracteres especiales";
            campo = "Descripcion";
            txt_tabsubdes.setText("");
        } else if (sp_tabord.getValue() == null) {
            s_valor = "Ingrese Orden, por favor";
            campo = "Orden";
        } else {
            s_valor = "";
        }
        return s_valor;
    }
//    public void validafocos(){
//     if(campo.equals("Descripcion")){
//     txt_tabsubdes.focus();
//     }else if(campo.equals("Nombres")){
//     txt
//     }
//    
//    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setText("%%");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstCanales = null;
        objlstCanales = new ListModelList<Canal>();
        lst_canales.setModel(objlstCanales);
    }

    public void llenarCampos() {
        txt_tabid.setValue(objCanal.getTab_subdir());
        txt_tabsubdes.setValue(objCanal.getTab_subdes());
        chk_tabest.setChecked(objCanal.isValor());
        chk_tabest.setLabel(objCanal.isValor() ? "ACTIVO" : "INACTIVO");
        txt_tabnomrep.setValue(objCanal.getTab_nomrep());
        sp_tabord.setValue(objCanal.getTab_ord());
        txt_usuadd.setValue(objCanal.getTab_usuadd());
        d_fecadd.setValue(objCanal.getTab_fecadd());
        txt_usumod.setValue(objCanal.getTab_usumod());
        d_fecmod.setValue(objCanal.getTab_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacanales.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacanales.setDisabled(b_valida1);
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
        chk_tabest.setChecked(true);
        chk_tabest.setLabel("ACTIVO");
        txt_tabid.setValue("");
        txt_tabsubdes.setValue("");
        txt_tabnomrep.setValue("");
        sp_tabord.setValue(0);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tabsubdes.setDisabled(b_valida);
        chk_tabest.setDisabled(b_valida);
        txt_tabnomrep.setDisabled(b_valida);
        sp_tabord.setDisabled(b_valida);

    }

    public Object generaRegistro() {
        int i_tabid = objCanal.getTab_id();

        String s_tab_usuadd = objUsuCredential.getCuenta();
        String s_tab_usumod = objUsuCredential.getCuenta();

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
        return new Canal(i_tabid, s_tabsubdes, i_tabest, i_tabord, s_tabnomrep,
                s_tab_usuadd, s_tab_usumod);
    }

}
