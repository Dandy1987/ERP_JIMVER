package org.me.gj.controller.contabilidad.mantenimiento;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.InterfaceControllers;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerTipDoc extends SelectorComposer<Component> implements InterfaceControllers {

    TipDoc objTipDoc = new TipDoc();
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    ListModelList objlstTipDoc = new ListModelList<TipDoc>();
    String s_estado = "Q", s_mensaje;
    @Wire
    Textbox txt_tipdocid, txt_tipdocdes, txt_tipdocnomrep, txt_busqueda;
    @Wire
    Checkbox chk_tipdocest, chk_pagcom, chk_asucaj, chk_aplideb, chk_busest;
    @Wire
    Intbox txt_codsun;
    @Wire
    Combobox cb_movcxc, cb_busqueda;
    @Wire
    Spinner sp_tipdocord;
    @Wire
    Listbox lst_tipdoc;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Tab tab_listatipdoc, tab_mantenimiento;

    int i_sel;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    private static final Logger LOGGER = Logger.getLogger(ControllerTipDoc.class);

    @Listen("onCreate=#tabbox_tipdoc")
    public void llenaRegistros() throws SQLException {
        objlstTipDoc = objDaoTipDoc.listaTipDoc(0);
        lst_tipdoc.setModel(objlstTipDoc);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(80104000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Detalle de Guia con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Detalle de Guia con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Detalle de Guia");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Detalle de Guia");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Detalle de Guia");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Detalle de Guia");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Detalle de Guia");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Detalle de Guia");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Detalle de Guias");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Detalle de Guias");
        }
    }

    @Listen("onSelect=#lst_tipdoc")
    public void seleccionaRegistro() throws SQLException {
        objTipDoc = (TipDoc) lst_tipdoc.getSelectedItem().getValue();
        if (objTipDoc == null) {
            objTipDoc = (TipDoc) objlstTipDoc.get(i_sel + 1);
        }
        i_sel = lst_tipdoc.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objTipDoc.getTipdoc_id());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objTipDoc = new TipDoc();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_tipdocest.setDisabled(true);
        txt_tipdocdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_tipdoc.getSelectedIndex() == -1) {
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
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objTipDoc = (TipDoc) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoTipDoc.insertar(objTipDoc);
                                } else {
                                    objParamSalida = objDaoTipDoc.modificar(objTipDoc);
                                }

                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstTipDoc = new ListModelList<TipDoc>();
                                    //validacion de guardar/nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    //
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstTipDoc = objDaoTipDoc.listaTipDoc(0);
                                    lst_tipdoc.setModel(objlstTipDoc);
                                    objTipDoc = new TipDoc();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_tipdoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta relacion de guia?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        ParametrosSalida objParamSalida  = objDaoTipDoc.eliminar(objTipDoc);
                        if(objParamSalida.getFlagIndicador()==0){
                            objlstTipDoc= new ListModelList<TipDoc>();
                            objlstTipDoc= objDaoTipDoc.listaTipDoc(0);
                            lst_tipdoc.setModel(objlstTipDoc);
                            //validacion de eliminacion
                            tbbtn_btn_eliminar.setDisabled(false);
                            VerificarTransacciones();
                            //
                        }
                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            lst_tipdoc.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
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

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstTipDoc = new ListModelList<TipDoc>();
            objlstTipDoc = objDaoTipDoc.listaTipDoc(0);
            lst_tipdoc.setModel(objlstTipDoc);
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        if (cb_busqueda.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una opcion de busqueda", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
            int i_seleccion = 0;
            int i_estado;
            if (chk_busest.isChecked()) {
                i_estado = 1;

            } else {
                i_estado = 2;
            }
            if (s_consulta.isEmpty()) {
                Messagebox.show("Por favor ingrese una opcion a buscar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objlstTipDoc = new ListModelList<TipDoc>();
                if (cb_busqueda.getSelectedIndex() == 0) {
                    i_seleccion = 1;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 1) {
                    i_seleccion = 2;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
                }
                objlstTipDoc = objDaoTipDoc.busquedaTipDoc(i_seleccion, s_consulta, i_estado);
                lst_tipdoc.setModel(objlstTipDoc);
            }
        }
    }

    public void llenarCampos() {
        txt_tipdocid.setValue(objTipDoc.getTipdoc_id());
        txt_tipdocdes.setValue(objTipDoc.getTipdoc_des());
        txt_codsun.setValue(objTipDoc.getTipdoc_sun()==0?null:objTipDoc.getTipdoc_sun());
        cb_movcxc.setSelectedItem(new Utilitarios().textoPorTexto(cb_movcxc, objTipDoc.getTipdoc_mov()));
        chk_tipdocest.setChecked(objTipDoc.isValor());
        chk_tipdocest.setLabel(objTipDoc.isValor()?"ACTIVO":"INACTIVO");
        chk_aplideb.setChecked(objTipDoc.getTipdoc_deb()==1);
        chk_aplideb.setLabel(objTipDoc.getTipdoc_deb()==1?"SI":"NO");
        chk_asucaj.setChecked(objTipDoc.getTipdoc_caj()==1);
        chk_asucaj.setLabel(objTipDoc.getTipdoc_caj()==1?"SI":"NO");
        chk_pagcom.setChecked(objTipDoc.getTipdoc_com()==1);
        chk_pagcom.setLabel(objTipDoc.getTipdoc_com()==1?"SI":"NO");
        txt_tipdocnomrep.setValue(objTipDoc.getTipdoc_nomrep());
        sp_tipdocord.setValue(objTipDoc.getTipdoc_ord());
                
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipdoc.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listatipdoc.setSelected(b_valida1);
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

    public void limpiarCampos() {
        txt_tipdocid.setValue("");
        txt_tipdocdes.setValue("");
        txt_codsun.setValue(null);
        cb_movcxc.setSelectedIndex(0);
        chk_tipdocest.setChecked(true);
        chk_tipdocest.setLabel("ACTIVO");
        chk_aplideb.setChecked(true);
        chk_aplideb.setLabel("SI");
        chk_asucaj.setChecked(true);
        chk_asucaj.setLabel("SI");
        chk_pagcom.setChecked(true);
        chk_pagcom.setLabel("SI");
        txt_tipdocnomrep.setValue("");
        sp_tipdocord.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_tipdocdes.setDisabled(b_valida);
        txt_codsun.setDisabled(b_valida);
        cb_movcxc.setDisabled(b_valida);
        chk_tipdocest.setDisabled(b_valida);
        chk_aplideb.setDisabled(b_valida);
        chk_asucaj.setDisabled(b_valida);
        chk_pagcom.setDisabled(b_valida);
        txt_tipdocnomrep.setDisabled(b_valida);
        sp_tipdocord.setDisabled(b_valida);
    }

    public String verificar() {
        String s_valor;
        if (txt_tipdocdes.getValue().isEmpty()) {
            s_valor = "Descripcion";
            txt_tipdocdes.focus();
        } else if(cb_movcxc.getSelectedIndex()==-1){
            s_valor="Mov CxC";
            cb_movcxc.focus();
        } else{
            s_valor="";
        }
        return s_valor;
    }

    public Object generaRegistro() {
        int i_tipdoc_key=objTipDoc.getTipdoc_key();
        String s_tipdoc_des=txt_tipdocdes.getValue().toUpperCase();
        int i_tipdoc_sun=(txt_codsun.getValue()==null)?-1:txt_codsun.getValue();
        int i_tipdoc_ord=sp_tipdocord.getValue();
        String s_tipdoc_nomrep=txt_tipdocnomrep.getValue().toUpperCase();
        int i_tipdoc_est=chk_tipdocest.isChecked()?1:2;
        int tipdoc_com=chk_pagcom.isChecked()?1:0;
        int tipdoc_caj=chk_asucaj.isChecked()?1:0;
        int tipdoc_deb=chk_aplideb.isChecked()?1:0;
        String tipdoc_mov=cb_movcxc.getSelectedItem().getValue();
        return new TipDoc(i_tipdoc_key, s_tipdoc_des, i_tipdoc_sun, i_tipdoc_ord, s_tipdoc_nomrep, 
                          i_tipdoc_est, tipdoc_com, tipdoc_caj, tipdoc_deb, tipdoc_mov);
    }

    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
