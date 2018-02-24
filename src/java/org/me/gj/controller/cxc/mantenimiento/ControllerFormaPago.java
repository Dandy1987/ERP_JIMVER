package org.me.gj.controller.cxc.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.FormaPago;
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

public class ControllerFormaPago extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Listbox lst_forpag;
    @Wire
    Textbox txt_forpagid, txt_forpagdes, txt_forpaglov, txt_nomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_forpagest, chk_forpagcaj, chk_forpagcom, chk_busest;
    @Wire
    Spinner sp_orden;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Tab tab_listaforpag, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    //Instancias de Objetos
    ListModelList<FormaPago> objlstFormaPago;
    DaoFormaPago objDaoFormaPago = new DaoFormaPago();
    FormaPago objFormaPago = new FormaPago();
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
    private static final Logger LOGGER = Logger.getLogger(ControllerFormaPago.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_forpag")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objlstFormaPago = new ListModelList<FormaPago>();
        objlstFormaPago = objDaoFormaPago.listaFormaPago(1);
        lst_forpag.setModel(objlstFormaPago);;
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20102000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Forma de Pago con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Forma de Pago con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Forma de Pago");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Forma de Pago");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Forma de Pago");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Forma de Pago");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Forma de Pago");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Forma de Pago");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Forma de Pago");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Forma de Pago");
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
        objlstFormaPago = new ListModelList<FormaPago>();
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
        objlstFormaPago = objDaoFormaPago.busquedaFormasPago(i_seleccion, s_consulta, i_estado);
        if (objlstFormaPago.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstFormaPago.getSize() + " registro(s)");
        }
        if (objlstFormaPago.getSize() > 0) {
            lst_forpag.setModel(objlstFormaPago);
        } else {
            objlstFormaPago = null;
            lst_forpag.setModel(objlstFormaPago);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
    }

    //Eventos Otros
    @Listen("onCtrlKey=#w_manforpag")
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

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onSelect=#lst_forpag")
    public void seleccionaRegistro() throws SQLException {
        objFormaPago = (FormaPago) lst_forpag.getSelectedItem().getValue();
        if (objFormaPago == null) {
            objFormaPago = objlstFormaPago.get(i_sel + 1);
        }
        i_sel = lst_forpag.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objFormaPago.getForpag_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objFormaPago = new FormaPago();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_forpagest.setDisabled(true);
        txt_forpagdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_forpag.getSelectedIndex() == -1) {
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

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_forpag.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro De Eliminar el Registro";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK /*Messagebox.show("Datos Eliminados Satisfactoriamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION)*/) {

                                s_mensaje = objDaoFormaPago.eliminarFormaPago(objFormaPago);
                                objlstFormaPago = objDaoFormaPago.listaFormaPago(0);
                                lst_forpag.setModel(objlstFormaPago);
                                limpiarCampos();
                                limpiaAuditoria();
                                Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                //
                            }
                        }
                    });
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
                            txt_forpagdes.focus();
                        } 
                    }
                }
            });
            //txt_forpagdes.focus();
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK /*Messagebox.show("Datos Guardados Satisfactoriamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION)*/) {
                                objlstFormaPago = new ListModelList<FormaPago>();
                                objFormaPago = (FormaPago) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoFormaPago.insertarFormaPago(objFormaPago);
                                } else {
                                    s_mensaje = objDaoFormaPago.actualizarFormaPago(objFormaPago);
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
                                objlstFormaPago = objDaoFormaPago.listaFormaPago(0);
                                lst_forpag.setModel(objlstFormaPago);
                                objFormaPago = new FormaPago();
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
                            lst_forpag.setSelectedIndex(-1);
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
        if (objlstFormaPago == null || objlstFormaPago.isEmpty()) {
            Messagebox.show("No hay registros de Formas de Pago para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/cxc/mantenimiento/LovImpresionForPag.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones  
    @Listen("onCtrlKey=#tabbox_forpag")
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
            objlstFormaPago = new ListModelList<FormaPago>();
            objlstFormaPago = objDaoFormaPago.listaFormaPago(0);
            lst_forpag.setModel(objlstFormaPago);
        }
    }

    //Eventos Otros 
    public String verificar() {
        String s_valor = "";
        if (txt_forpagdes.getValue().isEmpty()) {
            s_valor = "Por Favor Ingrese el campo Descripción";
            foco = "descrip";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public Object generaRegistro() {
        int forpag_key = objFormaPago.getForpag_key();
        String forpag_id = objFormaPago.getForpag_id();
        String forpag_des = txt_forpagdes.getValue().toUpperCase();

        int forpag_caj = (chk_forpagcaj.isChecked()) ? 1 : 0;
        int forpag_com = (chk_forpagcom.isChecked()) ? 1 : 0;
        String forpag_lov;
        if (txt_forpagdes.getValue().isEmpty()) {
            forpag_lov = txt_forpaglov.getValue();
        } else {
            forpag_lov = "/org/me/gj/view/componentes/" + txt_forpaglov.getValue() + ".zul";
        }
        int forpag_est = (chk_forpagest.isChecked()) ? 1 : 2;
        int forpag_ord = sp_orden.getValue();
        String forpag_nomrep = txt_nomrep.getValue().toUpperCase();
        String forpag_usuadd = objUsuCredential.getCuenta();
        String forpag_usumod = objUsuCredential.getCuenta();
        return new FormaPago(forpag_key, forpag_id, forpag_des, forpag_caj, forpag_com, forpag_lov, forpag_est,
                forpag_ord, forpag_nomrep, forpag_usuadd, forpag_usumod);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstFormaPago = null;
        objlstFormaPago = new ListModelList<FormaPago>();
        lst_forpag.setModel(objlstFormaPago);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaforpag.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaforpag.setDisabled(b_valida1);
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

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCampos() {
        chk_forpagest.setChecked(true);
        chk_forpagest.setLabel("ACTIVO");
        txt_forpagid.setValue("");
        txt_forpagdes.setValue("");
        txt_forpaglov.setValue("");
        chk_forpagcaj.setChecked(true);
        chk_forpagcaj.setLabel("SI ASUME CAJA");
        chk_forpagcom.setChecked(true);
        chk_forpagcom.setLabel("SI PAGA COMISION");
        txt_nomrep.setValue("");
        sp_orden.setValue(null);
    }

    public void llenarCampos() {
        chk_forpagest.setChecked(objFormaPago.isValorEst());
        chk_forpagest.setLabel((objFormaPago.getForpag_est() == 1) ? "ACTIVO" : "INACTIVO");
        txt_forpagid.setValue(objFormaPago.getForpag_id());
        txt_forpagdes.setValue(objFormaPago.getForpag_des());
        if (objFormaPago.getForpag_lov() == null) {
            txt_forpaglov.setValue(objFormaPago.getForpag_lov());
        } else {
            String lov_pantalla = objFormaPago.getForpag_lov().substring(28);
            int cont_lov = lov_pantalla.lastIndexOf(".");
            txt_forpaglov.setValue(lov_pantalla.substring(0, cont_lov));
        }
        chk_forpagcaj.setChecked(objFormaPago.isValorCaj());
        chk_forpagcaj.setLabel((objFormaPago.getForpag_caj() == 1) ? "SI ASUME CAJA" : "NO ASUME CAJA");
        chk_forpagcom.setChecked(objFormaPago.isValorCom());
        chk_forpagcom.setLabel((objFormaPago.getForpag_com() == 1) ? "SI PAGA COMISION" : "NO PAGA COMISION");
        txt_nomrep.setValue(objFormaPago.getForpag_nomrep());
        sp_orden.setValue(objFormaPago.getForpag_ord());
        txt_usuadd.setValue(objFormaPago.getForpag_usuadd());
        d_fecadd.setValue(objFormaPago.getForpag_fecadd());
        txt_usumod.setValue(objFormaPago.getForpag_usumod());
        d_fecmod.setValue(objFormaPago.getForpag_fecmod());
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_forpagest.setDisabled(b_valida1);
        chk_forpagest.setDisabled(b_valida1);
        txt_forpagdes.setDisabled(b_valida1);
        txt_forpaglov.setDisabled(b_valida1);
        chk_forpagcaj.setDisabled(b_valida1);
        chk_forpagcaj.setDisabled(b_valida1);
        chk_forpagcom.setDisabled(b_valida1);
        chk_forpagcom.setDisabled(b_valida1);
        txt_nomrep.setDisabled(b_valida1);
        sp_orden.setDisabled(b_valida1);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
