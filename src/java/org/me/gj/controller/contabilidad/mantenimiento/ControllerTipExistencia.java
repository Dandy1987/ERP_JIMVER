package org.me.gj.controller.contabilidad.mantenimiento;

import org.me.gj.controller.logistica.mantenimiento.*;
import java.sql.SQLException;
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
import org.me.gj.model.contabilidad.mantenimiento.TipoExistencia;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;

public class ControllerTipExistencia extends SelectorComposer<Component> implements InterfaceControllers {

    ListModelList<TipoExistencia> lsttexistencia;
    TipoExistencia obj_tabgen;
    DaoTipExistencia obj_daoTipExistencia;
    UsuariosCredential cre;
    Accesos obj_Accesos = new Accesos();
    DaoAccesos obj_daoAcc = new DaoAccesos();
    @Wire
    Tab tab_mantenimiento, tab_lista;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Textbox txt_busqueda, txt_tabdes, txt_tabsubdes, txt_tabnomrep;
    @Wire
    Intbox txt_tabcod, txt_tabid;
    @Wire
    Spinner spi_tabtip;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Listbox lst_texistencia;
    @Wire
    Combobox cb_busqueda;
    private String s_estado_mantenimiento = "Q";
    private String s_mensaje = "";
    //variable auxiliar para validar la carga de registros con paginacion
    int i_sel;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static Logger LOGGER = Logger.getLogger(ControllerUnidMedida.class);

    //Metodo se inicializa con el window
    @Listen("onCreate=#tabbox_texistencia")
    public void llenaRegistros() throws SQLException {
        lsttexistencia = new ListModelList<TipoExistencia>();
        lsttexistencia = new DaoTipExistencia().lstTexistencia(0);
        lst_texistencia.setModel(lsttexistencia);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = cre.getCodigo();
        int empresa = cre.getCodemp();
        int sucursal = cre.getCodsuc();
        obj_Accesos = obj_daoAcc.Verifica_Permisos_Transacciones(10112000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Unidad de Manejo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Unidad de Manejo con el rol: USUARIO NORMAL");
        }
        if (obj_Accesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Unidad de Manejo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Unidad de Manejo");
        }
        if (obj_Accesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Unidad de Manejo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Unidad de Manejo");
        }
        if (obj_Accesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Unidad de Manejo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Unidad de Manejo");
        }
        if (obj_Accesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Unidad de Manejo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Unidad de Manejo");
        }

    }

    //Transacciones
    @Listen("onSelect=#lst_texistencia")
    public void seleccionaRegistro() throws SQLException {
        boolean b_val = false;
        obj_tabgen = lst_texistencia.getSelectedItem().getValue();
        if (obj_tabgen == null) {
            obj_tabgen = lsttexistencia.get(i_sel + 1);
        }
        i_sel = lst_texistencia.getSelectedIndex();
        if (obj_tabgen.getTab_est() == 1) {
            b_val = true;
        }
        chk_tabest.setChecked(b_val);
        txt_tabid.setValue(obj_tabgen.getTab_id());
        txt_tabsubdes.setValue(obj_tabgen.getTab_subdes());
        spi_tabtip.setValue(obj_tabgen.getTab_tip());
        txt_tabnomrep.setValue(obj_tabgen.getTab_nomrep());
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + obj_tabgen.getTab_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        if (lst_texistencia.getSelectedIndex() > -1) {
            Messagebox.show("Tienes Selecionado una Unidad de Medida . Deseas Crear uno Nuevo", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
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
                                spi_tabtip.setDisabled(false);
                                chk_tabest.setChecked(true);
                                chk_tabest.setDisabled(true);
                                s_estado_mantenimiento = "N";
                                txt_tabsubdes.focus();
                                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
                            } else {
                                seleccionaRegistro();
                            }
                        }
                    });
        } else {

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
            //txt_tabsubdir.setDisabled(false);
            spi_tabtip.setDisabled(false);
            chk_tabest.setChecked(true);
            chk_tabest.setDisabled(true);
            s_estado_mantenimiento = "N";
            txt_tabsubdes.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_texistencia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Unidad de Manejo a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            seleccionaRegistro();
            tab_lista.setSelected(false);
            tab_lista.setDisabled(true);
            tab_mantenimiento.setSelected(true);
            //tab_mantenimiento.setDisabled(false);
            txt_tabnomrep.setDisabled(false);
            txt_tabsubdes.setDisabled(false);
            //txt_tabsubdir.setDisabled(false);
            spi_tabtip.setDisabled(false);
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
        int valor = 0;
        String s_abrev="";
        if (s_estado_mantenimiento.equals("N")) {
            if (("").equals(txt_tabsubdes.getValue()) || txt_tabsubdes.getValue().isEmpty()) {
                if (txt_tabsubdes.getValue().isEmpty()) {
                    Messagebox.show("El Campo Descripcion  es Obligatorio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_tabsubdes.focus();
                }
            } else {
                obj_daoTipExistencia = new DaoTipExistencia();
                valor = obj_daoTipExistencia.ValidaAbrev(s_abrev);
                if (valor >= 1) {
                    Messagebox.show("La Abreviatura ya Existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);    
                } else {
                    String s_msg = "";
                    int i_tab_est = 2;
                    int i_tab_id = 0;
                    
                    //Recuperar en variables los valores en el formulario
                    String s_tab_subdes = txt_tabsubdes.getValue();
                    
                    int i_tab_tip = spi_tabtip.getValue();
                    if (chk_tabest.isChecked()) {
                        i_tab_est = 1;
                    }
                    String s_tab_nomrep = txt_tabnomrep.getValue();
                    /*  generaNroPedLista(pcab_nroped);*/
                    obj_tabgen = new TipoExistencia(i_tab_id, s_tab_subdes, i_tab_tip, s_tab_nomrep, i_tab_est);
                    s_msg = obj_daoTipExistencia.Insert(obj_tabgen);
                    s_mensaje = s_msg;
                    Messagebox.show(s_mensaje);
                    lsttexistencia = new DaoTipExistencia().lstTexistencia(0);
                    lst_texistencia.setModel(lsttexistencia);
                    OnChange();
                    tab_lista.setDisabled(false);
                    tab_lista.setSelected(true);
                    //validacion de guardar/nuevo
                    VerificarTransacciones();
                    tbbtn_btn_guardar.setDisabled(true);
                    tbbtn_btn_deshacer.setDisabled(true);
                    //
                }
            }
        } else {
            if (s_estado_mantenimiento.equals("M")) {
                if (("").equals(txt_tabsubdes.getValue()) || txt_tabsubdes.getValue().isEmpty()) {
                    if (txt_tabsubdes.getValue().isEmpty()) {
                        Messagebox.show("El Campo Descripcion  es Obligatorio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        txt_tabsubdes.focus();
                    } 
                } else {
                    String msg = "";
                    int i_tab_est = 2;
                    obj_daoTipExistencia = new DaoTipExistencia();
                    //Recuperar en variables los valores en el formulario
                    int i_tab_id = txt_tabid.getValue();
                    String s_tab_subdes = txt_tabsubdes.getValue();
                    int i_tab_tip = spi_tabtip.getValue();
                    if (chk_tabest.isChecked()) {
                        i_tab_est = 1;
                    }
                    String s_tab_nomrep = txt_tabnomrep.getValue();
                    /*  generaNroPedLista(pcab_nroped);*/
                    obj_tabgen = new TipoExistencia(i_tab_id, s_tab_subdes, i_tab_tip, s_tab_nomrep, i_tab_est);
                    msg = obj_daoTipExistencia.Update(obj_tabgen);
                    s_mensaje = msg;
                    Messagebox.show(s_mensaje);
                    lsttexistencia = new DaoTipExistencia().lstTexistencia(0);
                    lst_texistencia.setModel(lsttexistencia);
                    OnChange();
                    tab_lista.setSelected(true);
                    tab_lista.setDisabled(false);
                    //validacion de guardar/nuevo
                    VerificarTransacciones();
                    tbbtn_btn_guardar.setDisabled(true);
                    tbbtn_btn_deshacer.setDisabled(true);
                    //
                }
            }

        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_texistencia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una Unidad de Manejo a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Esta Seguro que desea Eliminar la Unidad de Manejo";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String s_msg = "";
                                obj_daoTipExistencia = new DaoTipExistencia();
                                int i_tab_id = txt_tabid.getValue();
                                int valor = 0;
                                if (valor == 0){
                                s_msg = obj_daoTipExistencia.Delete(i_tab_id);
                                s_mensaje = s_msg;
                                Messagebox.show(s_mensaje);
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                //
                                lsttexistencia = new DaoTipExistencia().lstTexistencia(0);
                                lst_texistencia.setModel(lsttexistencia);
                                OnChange();
                                tab_lista.setSelected(true);
                                }
                                else {
                                  Messagebox.show("La unidad de Manejo tiene Existencia en Productos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);  
                                }
                            }
                        }
                    });
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
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            OnChange();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            llenaRegistros();
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
                lsttexistencia = new ListModelList<TipoExistencia>();
                if (cb_busqueda.getSelectedIndex() == 0) {
                    i_seleccion = 1;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 1) {
                    i_seleccion = 2;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 2) {
                    i_seleccion = 3;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la abreviatura " + s_consulta + " para su busqueda");
                }
                                
                lsttexistencia = new DaoTipExistencia().Search(s_consulta, i_seleccion, i_estado);
                lst_texistencia.setModel(lsttexistencia);
            }
        }
    }

    public void OnChange() {

        txt_tabnomrep.setDisabled(true);
        txt_tabsubdes.setDisabled(true);
        chk_tabest.setChecked(false);
        spi_tabtip.setDisabled(true);
        //limpio los datos ingresados
        txt_tabid.setValue(null);
        txt_tabnomrep.setValue("");
        txt_tabsubdes.setValue("");
        //txt_tabsubdir.setValue("");
        chk_tabest.setChecked(false);
        spi_tabtip.setValue(0);
        chk_tabest.setDisabled(true);
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void limpiarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
