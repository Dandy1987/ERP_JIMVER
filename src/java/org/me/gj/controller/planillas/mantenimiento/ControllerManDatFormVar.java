/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManDatForm;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

/**
 *
 * @author achocano
 */
public class ControllerManDatFormVar extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Checkbox chk_estado;
    @Wire
    Listbox lst_lista;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Button btn_buscar;
    @Wire
    Radiogroup rdg_tipo, rdg_tipoP;
    @Wire
    Textbox txt_codigo, txt_busqueda, txt_grabar, txt_mostrar, txt_usua, txt_usumo;
    String s_estado, campo, s_mensaje;
    ListModelList<ManDatForm> objlstDatForm;//= new ListModelList<ManDatForm>();
    ManDatForm objDatForm = new ManDatForm();
    DaoManDatForm objDaoManForm;

    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    int valor, i_sel;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManTabla.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.

        objlstDatForm = new ListModelList<ManDatForm>();
        objDaoManForm = new DaoManDatForm();
        objlstDatForm = objDaoManForm.consultarVariable("", 1, 0);
        lst_lista.setModel(objlstDatForm);
        txt_busqueda.focus();
        cb_estado.setSelectedIndex(0);
        rdg_tipo.setSelectedIndex(0);
        rdg_tipoP.setSelectedIndex(0);
        habilitaTab(false, true);
    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        //parametro para la busqueda
        String consulta = txt_busqueda.getValue().toUpperCase();
        int tipo = rdg_tipoP.getSelectedIndex();
        int est;
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }
        objlstDatForm = objDaoManForm.consultarVariable(consulta, est, tipo);
        if (objlstDatForm.getSize() > 0) {
            lst_lista.setModel(objlstDatForm);
        } else {
            objlstDatForm = null;
            lst_lista.setModel(objlstDatForm);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                public void onEvent(Event t) throws Exception {
                    if ((((Integer) t.getData()).intValue() == Messagebox.OK)) {
                        txt_busqueda.setValue("");//limpia buscar
                        txt_busqueda.focus();//cursor se ubica en campo para ingreso de codigo   
                    }

                }
            });

        }

        limpiaAuditoria();
        limpiarCampos();
        txt_busqueda.setValue("");//limpia buscar
        txt_busqueda.focus();//cursor se ubica en campo para ingreso de codigo

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101050, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de DatFormVar con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de DatFormVar con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo DatFormVar");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo DatFormVar");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un DatFormVar");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un DatFormVar");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de DatFormVar");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de DatFormVar");
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        habilitaCampos(false);
        chk_estado.setChecked(true);
        txt_codigo.setDisabled(true);
        chk_estado.setDisabled(true);
        txt_grabar.focus();
        rdg_tipo.setSelectedIndex(0);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_lista.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            VerificarTransacciones();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onOK=#txt_grabar")
    public void next() {
        txt_mostrar.focus();
    }

    @Listen("onOK=#txt_mostrar")
    public void next1() {
        botonGuardar();
    }

    @Listen("onSelect=#lst_lista")
    public void seleccion() {
        objDatForm = lst_lista.getSelectedItem().getValue();
        if (objDatForm == null) {
            objDatForm = objlstDatForm.get(i_sel + 1);

        }
        i_sel = lst_lista.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objDatForm.getCodigo());
        habilitaTab(false, false);
    }

    public String verificar() {
        String view;
        if (txt_grabar.getValue().isEmpty()) {
            view = "Ingresar campo grabar";
            campo = "grabar";
        } else if (txt_mostrar.getValue().isEmpty()) {
            view = "Ingresar campo mostrar";
            campo = "mostrar";
        } else {
            view = "";
        }
        return view;
    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_datform")
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
            /*case 119:
             if (!tbbtn_btn_imprimir.isDisabled()) {
             botonImprimir();
             }
             break;*/
        }
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }

    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este banco?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                if (objDatForm.getIdentificador().equals("001")) {
                                    s_mensaje = objDaoManForm.eliminar001(objDatForm);
                                } else {
                                    s_mensaje = objDaoManForm.eliminar002(objDatForm);
                                }

                                valor = objDaoManForm.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstDatForm.clear();

                                    //  lst_lista.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    objlstDatForm = objDaoManForm.lstDatForm();
                                    lst_lista.setModel(objlstDatForm);
                                } else {
                                    Messagebox.show(objDaoManForm.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
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
                        if (campo.equals("grabar")) {
                            txt_grabar.focus();
                        } else if (campo.equals("mostrar")) {
                            txt_mostrar.focus();
                        }

                    }
                }
            });
        } else {
            s_mensaje = "Esta seguro de guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstDatForm = new ListModelList<ManDatForm>();
                                objDatForm = (ManDatForm) generaRegistro();
                                if (s_estado.equals("N")) {
                                    if (rdg_tipo.getSelectedIndex() == 0) {
                                        s_mensaje = objDaoManForm.insertar001(objDatForm);
                                    } else if (rdg_tipo.getSelectedIndex() == 1) {
                                        s_mensaje = objDaoManForm.insertar002(objDatForm);
                                    }
                                } else {
                                    if (rdg_tipo.getSelectedIndex() == 0) {
                                        s_mensaje = objDaoManForm.actualizar001(objDatForm);
                                    } else if (rdg_tipo.getSelectedIndex() == 1) {
                                        s_mensaje = objDaoManForm.actualizar002(objDatForm);
                                    }

                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                VerificarTransacciones();
                                cb_estado.setSelectedIndex(0);
                                objlstDatForm = objDaoManForm.consultarVariable("", 1, 0);
                                lst_lista.setModel(objlstDatForm);

                            }

                        }

                    });

        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (chk_estado.isChecked() && lst_lista.getSelectedIndex() >= 0) {
            //   Messagebox.show("Es una Ubicación por Defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            editable();

        } else {
            editable();
        }
    }

    public void editable() {

        s_estado = "E";
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_grabar.focus();
        // rdg_tipo.setVisible(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
    }

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida) {
        //txt_codigo.setDisabled(true);
        txt_grabar.setDisabled(b_valida);
        txt_mostrar.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
    }

    public void limpiarCampos() {
        txt_codigo.setValue("");
        txt_grabar.setValue("");
        txt_mostrar.setValue("");
        chk_estado.setChecked(false);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void llenarCampos() {
        txt_codigo.setValue(objDatForm.getCodigo());
        txt_grabar.setValue(objDatForm.getRecord());
        txt_mostrar.setValue(objDatForm.getView());
        if (objDatForm.getIdentificador().equals("001")) {
            rdg_tipo.setSelectedIndex(0);
        } else {
            rdg_tipo.setSelectedIndex(1);
        }
        if (objDatForm.getEstado() == 1) {
            chk_estado.setChecked(true);

        } else {
            chk_estado.setChecked(false);
        }
        txt_usua.setValue(objDatForm.getUsu_add());
        txt_usumo.setValue(objDatForm.getUsu_mod());
        d_fec.setValue(objDatForm.getDia_add());
        d_fecmo.setValue(objDatForm.getDia_mod());
    }

    public ManDatForm generaRegistro() throws SQLException {
        int i_ubicdef;
        // String codigo = objDatForm.getCodigo();//objManTablas.getCodigo();
        String codigo;
        if (s_estado.equals("N")) {
            codigo = "";
        } else {
            codigo = objDatForm.getCodigo();//objManTablas.getCodigo();
        }
        String view = txt_mostrar.getValue();
        String record = txt_grabar.getValue();

        //double defaul = d_default.getValue();
        if (chk_estado.isChecked()) {
            i_ubicdef = 1;
        } else {
            i_ubicdef = 2;
        }

        return new ManDatForm(codigo, view, record, i_ubicdef);

    }
}
