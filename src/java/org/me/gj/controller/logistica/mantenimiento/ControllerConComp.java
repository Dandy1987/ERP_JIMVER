package org.me.gj.controller.logistica.mantenimiento;

import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.Condicion;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerConComp extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Listbox lst_concomp;
    @Wire
    Textbox txt_conid, txt_condes, txt_connomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Intbox txt_condplz;
    @Wire
    Checkbox chk_conest;
    @Wire
    Spinner sp_conord;
    @Wire
    Tab tab_listaconcomp, tab_mantenimiento;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de objetos
    ListModelList<Condicion> objlstConComp = new ListModelList<Condicion>();
    DaoCondicion objDaoConComp = new DaoCondicion();
    Condicion objConComp = new Condicion();
    Accesos obj_Accesos = new Accesos();
    DaoAccesos obj_daoAcc = new DaoAccesos();
    //Variables Publicas
    String s_estado = "Q", s_mensaje = "", s_tipo = "C";
    private String campo = "";
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerConComp.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_concomp")
    public void llenaRegistros() throws SQLException {
        objlstConComp = objDaoConComp.lstCondicion(s_tipo, 1);
        lst_concomp.setModel(objlstConComp);
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
        obj_Accesos = obj_daoAcc.Verifica_Permisos_Transacciones(10105010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Condición de Compra con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Condición de Compra con el rol: USUARIO NORMAL");
        }
        if (obj_Accesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Condición de Compra ");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Condición de Compra ");
        }
        if (obj_Accesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Condición de Compra ");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Condición de Compra ");
        }
        if (obj_Accesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Condición de Compra ");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Condición de Compra ");
        }
        if (obj_Accesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Condición de Compra ");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Condición de Compra ");
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

        objlstConComp = new ListModelList<Condicion>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        }
        if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado los dias de plazo " + s_consulta + " para su busqueda");
        }
        objlstConComp = objDaoConComp.busqueda(i_seleccion, s_consulta, i_estado, s_tipo);
        if (objlstConComp.getSize() > 0) {
            lst_concomp.setModel(objlstConComp);
        } else {
            objlstConComp = null;
            lst_concomp.setModel(objlstConComp);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        lst_concomp.setModel(objlstConComp);
        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#lst_concomp")
    public void seleccionaRegistro() throws SQLException {
        objConComp = (Condicion) lst_concomp.getSelectedItem().getValue();
        if (objConComp == null) {
            objConComp = objlstConComp.get(i_sel + 1);
        }
        i_sel = lst_concomp.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objConComp.getConKey());
        llenarCampos();
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

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objConComp = new Condicion();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_conest.setDisabled(true);
        s_estado = "N";
        chk_conest.setChecked(true);
        txt_condes.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_concomp.getSelectedIndex() == -1) {
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
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Condicion")) {
                            txt_condes.focus();
                        } else if (campo.equals("Diaz Plazo")) {
                            txt_condplz.focus();
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
                                objConComp = (Condicion) generaRegistro();
                                ParametrosSalida objParamSalida;
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoConComp.insertar(objConComp);
                                } else {
                                    objParamSalida = objDaoConComp.actualizar(objConComp);
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    //validacion de guardar/nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    //
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    objlstConComp.clear();
                                    objlstConComp = objDaoConComp.lstCondicion(s_tipo, 1);
                                    cb_busqueda.setSelectedIndex(0);
                                    cb_estado.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    lst_concomp.setModel(objlstConComp);
                                    objConComp = new Condicion();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_concomp.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoConComp.validaMovimientos(objConComp);
            valor = objDaoConComp.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoConComp.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Esta Seguro que desea Eliminar esta Condición de Compra?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoConComp.eliminar(objConComp);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstConComp.clear();
                                        objlstConComp = objDaoConComp.lstCondicion(s_tipo, 1);
                                        cb_busqueda.setSelectedIndex(0);
                                        cb_estado.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_concomp.setModel(objlstConComp);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        limpiarCampos();
                                        limpiaAuditoria();
                                    }
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        });
            }
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
                            lst_concomp.setSelectedIndex(-1);
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
        if (objlstConComp == null || objlstConComp.isEmpty()) {
            Messagebox.show("No hay registros de Condición de Compra para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionCcompra.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    /*@Listen("onCtrlKey=#tabbox_concomp")
    public void GuardarInformacion(Event event) throws SQLException {
        if (!tbbtn_btn_guardar.isDisabled()) {
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
    }*/
    
    @Listen("onCtrlKey=#w_manlin")
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
            objlstConComp = new ListModelList<Condicion>();
            objlstConComp = objDaoConComp.lstCondicion(s_tipo, 0);
            lst_concomp.setModel(objlstConComp);
        }
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_conid.setValue(objConComp.getConId());
        txt_condes.setValue(objConComp.getConDes());
        if (objConComp.getConEst() == 1) {
            chk_conest.setChecked(true);
        } else {
            chk_conest.setChecked(false);
        }
        txt_connomrep.setValue(objConComp.getConNomRep());
        sp_conord.setValue(objConComp.getConOrd());
        txt_condplz.setValue(objConComp.getConDias());
        txt_usuadd.setValue(objConComp.getConUsuadd());
        d_fecadd.setValue(objConComp.getConFecadd());
        txt_usumod.setValue(objConComp.getConUsumod());
        d_fecmod.setValue(objConComp.getConFecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaconcomp.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaconcomp.setSelected(b_valida1);
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

    public String verificar() {
        String s_valor;
        int dias = (txt_condplz.getValue() != null ? txt_condplz.getValue() : 0);
        if (txt_condes.getValue().isEmpty() || txt_condes.getText().trim().length() == 0) {
            s_valor = "El Campo Condicion es Obligatorio";
            campo = "Condicion";
        } else if (txt_condplz.getValue() == null) {
            s_valor = "El Campo Diaz Plazo es Obligatorio";
            campo = "Diaz Plazo";
        } else if (dias <= 0) {
            s_valor = "El Campo Diaz Plazo es Inválido";
            campo = "Diaz Plazo";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        txt_conid.setValue(null);
        txt_condes.setValue("");
        txt_connomrep.setValue("");
        txt_condplz.setValue(null);
        sp_conord.setValue(0);
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_condes.setDisabled(b_valida);
        chk_conest.setDisabled(b_valida);
        txt_connomrep.setDisabled(b_valida);
        sp_conord.setDisabled(b_valida);
        txt_condplz.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int conKey = objConComp.getConKey();
        String conTipo = "C";
        String conDes = txt_condes.getValue().toUpperCase();
        int conEst;
        if (chk_conest.isChecked()) {
            conEst = 1;
        } else {
            conEst = 2;
        }
        int conDias = 0;
        if (txt_condplz.getValue() != null) {
            conDias = txt_condplz.getValue();
        }
        int conOrd = sp_conord.getValue();
        String conNomRep = txt_connomrep.getValue().toUpperCase();
        String conUsuadd = objUsuCredential.getCuenta();
        String conUsumodd = objUsuCredential.getCuenta();
        return new Condicion(conKey, conTipo, conDes, conEst, conDias, conOrd, conNomRep, conUsuadd, conUsumodd);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstConComp = null;
        objlstConComp = new ListModelList<Condicion>();
        lst_concomp.setModel(objlstConComp);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
