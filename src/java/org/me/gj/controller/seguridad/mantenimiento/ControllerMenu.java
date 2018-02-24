package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;
import org.me.gj.model.seguridad.mantenimiento.Menus;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;

public class ControllerMenu extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Radiogroup rb_grupo;
    @Wire
    Radio rb_modulo, rb_menu, rb_submenu1, rb_submenu2;
    @Wire
    Listbox lst_menus;
    @Wire
    Textbox txt_busqueda, txt_nommod, txt_nommenu, txt_nomsubmenu1, txt_nomsubmenu2;
    @Wire
    Combobox cb_busqueda, cb_menu, cb_modulo, cb_submenu1;
    @Wire
    Tab tab_listamenus, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Button btn_buscar;
    //Instancias de Objetos
    ListModelList<Menus> objlstMenus = new ListModelList<Menus>();
    ListModelList<Menus> objlstModulos = new ListModelList<Menus>();
    ListModelList<Menus> objlstModulosMenus = new ListModelList<Menus>();
    ListModelList<Menus> objlstModulosMenusSubmenus = new ListModelList<Menus>();
    Menus objMenu = new Menus();
    DaoMenu objDaoMenus = new DaoMenu();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje = "";
    String usuario;
    int i_sel = 0;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMenu.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_menus")
    public void llenaRegistros() throws SQLException {
        objlstMenus = objDaoMenus.lstMenus();
        lst_menus.setModel(objlstMenus);
        objlstModulos = objDaoMenus.listaModulos();
        cb_modulo.setModel(objlstModulos);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        objlstMenus = new ListModelList<Menus>();
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
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstMenus = objDaoMenus.busquedaMenus(i_seleccion, s_consulta);
        if (objlstMenus.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstMenus.getSize() + " registro(s)");
        }
        if (objlstMenus.getSize() > 0) {
            lst_menus.setModel(objlstMenus);
        } else {
            objlstMenus = null;
            lst_menus.setModel(objlstMenus);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        //limpiaAuditoria();
    }

    @Listen("onSelect=#cb_modulo")
    public void seleccionaModulo() throws SQLException {
        int i_modulo;
        i_modulo = cb_modulo.getSelectedItem().getValue();
        objlstModulosMenus = objDaoMenus.listaMenus(i_modulo);
        cb_menu.setModel(objlstModulosMenus);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo de modulo " + i_modulo);
    }

    @Listen("onSelect=#cb_menu")
    public void seleccionaMenu() throws SQLException {
        String s_modulo;
        String s_menu;
        s_modulo = cb_modulo.getValue();
        s_menu = cb_menu.getValue();
        objlstModulosMenusSubmenus = objDaoMenus.listaSubMenus(s_modulo, s_menu);
        cb_submenu1.setModel(objlstModulosMenusSubmenus);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo de menu " + s_menu);
    }

    @Listen("onCheck=#rb_grupo")
    public void escogerOpcion() {
        if (rb_modulo.isChecked() == true) {
            habilitaCampos(true);
            limpiarCampos();
            rb_modulo.setChecked(true);
            txt_nommod.setDisabled(false);
            txt_nommod.setFocus(true);

        } else if (rb_menu.isChecked() == true) {
            habilitaCampos(true);
            limpiarCampos();
            rb_menu.setChecked(true);
            txt_nommenu.setDisabled(false);
            txt_nommenu.setFocus(true);
            cb_modulo.setDisabled(false);
        } else if (rb_submenu1.isChecked() == true) {
            habilitaCampos(true);
            limpiarCampos();
            rb_submenu1.setChecked(true);
            txt_nomsubmenu1.setDisabled(false);
            txt_nomsubmenu1.setFocus(true);
            cb_modulo.setDisabled(false);
            cb_menu.setDisabled(false);

        } else {
            habilitaCampos(true);
            limpiarCampos();
            rb_submenu2.setChecked(true);
            txt_nomsubmenu2.setDisabled(false);
            txt_nomsubmenu2.setFocus(true);
            cb_modulo.setDisabled(false);
            cb_menu.setDisabled(false);
            cb_submenu1.setDisabled(false);
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        llenaRegistros();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        rb_modulo.setDisabled(false);
        rb_menu.setDisabled(false);
        rb_submenu1.setDisabled(false);
        rb_submenu2.setDisabled(false);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
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
                            lst_menus.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            limpiarCampos();
                            rb_modulo.setDisabled(true);
                            rb_menu.setDisabled(true);
                            rb_submenu1.setDisabled(true);
                            rb_submenu2.setDisabled(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_menus.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            rb_modulo.setDisabled(true);
            rb_menu.setDisabled(true);
            rb_submenu1.setDisabled(true);
            rb_submenu2.setDisabled(true);
            habilitaCampos(true);
            txt_nomsubmenu1.setDisabled(false);
            txt_nomsubmenu2.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (rb_modulo.isChecked() == false && rb_menu.isChecked() == false && rb_submenu1.isChecked() == false && rb_submenu2.isChecked() == false) {
            Messagebox.show("Por favor seleccione Accesos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (rb_modulo.isChecked() == true) {
                i_sel = 1;//modulo
            }
            if (rb_menu.isChecked() == true) {
                i_sel = 2;//menu
            }
            if (rb_submenu1.isChecked() == true) {
                i_sel = 3;//submenu1
            }
            if (rb_submenu2.isChecked() == true) {
                i_sel = 4;//submenu2
            }
            String s_valida = verificar();
            if (!s_valida.isEmpty()) {
                Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    objlstMenus = new ListModelList<Menus>();
                                    if (s_estado.equals("N")) {
                                        if (rb_modulo.isChecked()) {
                                            objMenu = generaModulo();
                                        } else if (rb_menu.isChecked()) {
                                            objMenu = generaMenu();
                                        } else if (rb_submenu1.isChecked()) {
                                            objMenu = generaSubMenu1();
                                        } else if (rb_submenu2.isChecked()) {
                                            objMenu = generaSubMenu2();
                                        }
                                        s_mensaje = objDaoMenus.insertarMenus(objMenu, i_sel, objUsuCredential.getNombre());
                                    }
                                    Messagebox.show(s_mensaje);
                                    habilitaBotones(false, true);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstMenus = objDaoMenus.lstMenus();
                                    lst_menus.setModel(objlstMenus);
                                    objMenu = new Menus();
                                }
                            }
                        });
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstMenus = new ListModelList<Menus>();
            objlstMenus = objDaoMenus.lstMenus();
            lst_menus.setModel(objlstMenus);
        }
    }

    //Eventos Otros
    public Menus generaModulo() {
        String s_modulo = txt_nommod.getValue();
        return new Menus(s_modulo);
    }

    public Menus generaMenu() {
        int i_modulo = cb_modulo.getSelectedItem().getValue();
        String s_menu = txt_nommenu.getValue();
        return new Menus(i_modulo, s_menu);
    }

    public Menus generaSubMenu1() {
        int i_modulo = cb_modulo.getSelectedItem().getValue();
        int i_menu = cb_menu.getSelectedItem().getValue();
        String s_submen1 = txt_nomsubmenu1.getValue();
        return new Menus(i_modulo, s_submen1, i_menu);
    }

    public Menus generaSubMenu2() {
        int i_modulo = cb_modulo.getSelectedItem().getValue();
        int i_menu = cb_menu.getSelectedItem().getValue();
        int i_submenu1 = cb_submenu1.getSelectedItem().getValue();
        String s_submen2 = txt_nomsubmenu2.getValue();
        return new Menus(i_modulo, s_submen2, i_menu, i_submenu1);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamenus.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamenus.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void llenarCampos() {
        txt_nommod.setValue(String.valueOf(objMenu.getDes_modulo()));
        txt_nommenu.setValue(String.valueOf(objMenu.getDes_menu()));
        txt_nomsubmenu1.setValue(String.valueOf(objMenu.getDes_submenu()));
        txt_nomsubmenu2.setValue(String.valueOf(objMenu.getDes_nombre()));
    }

    public void limpiarCampos() {
        txt_nommenu.setValue("");
        txt_nommod.setValue("");
        txt_nomsubmenu1.setValue("");
        txt_nomsubmenu2.setValue("");
        rb_modulo.setChecked(false);
        rb_menu.setChecked(false);
        rb_submenu1.setChecked(false);
        rb_submenu2.setChecked(false);
        cb_modulo.setValue("");
        cb_menu.setValue("");
        cb_submenu1.setValue("");
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstMenus = null;
        objlstMenus = new ListModelList<Menus>();
        lst_menus.setModel(objlstMenus);
    }

    public void habilitaCampos(boolean b_valida1) {
        cb_modulo.setDisabled(b_valida1);
        cb_menu.setDisabled(b_valida1);
        cb_submenu1.setDisabled(b_valida1);
        txt_nommod.setDisabled(b_valida1);
        txt_nommenu.setDisabled(b_valida1);
        txt_nomsubmenu1.setDisabled(b_valida1);
        txt_nomsubmenu2.setDisabled(b_valida1);
    }

    public String verificar() {
        String s_valida = "";
        //valida campos si es serie
        if (rb_modulo.isChecked()) {
            if (txt_nommod.getValue().isEmpty()) {
                s_valida = "Modulo";
                txt_nommod.focus();
            } else {
                s_valida = "";
            }
        } else if (rb_menu.isChecked()) {
            if (txt_nommenu.getValue().isEmpty() || cb_modulo.getSelectedIndex() == -1) {
                s_valida = "Menu";
                txt_nommenu.focus();
            } else {
                s_valida = "";
            }
        } else if (rb_submenu1.isChecked()) {
            if (txt_nomsubmenu1.getValue().isEmpty() || cb_modulo.getSelectedIndex() == -1 || cb_menu.getSelectedIndex() == -1) {
                s_valida = "Submenu1";
                txt_nomsubmenu1.focus();
            } else {
                s_valida = "";
            }
        } else if (rb_submenu2.isChecked()) {
            if (txt_nomsubmenu2.getValue().isEmpty() || cb_modulo.getSelectedIndex() == -1 || cb_menu.getSelectedIndex() == -1 || cb_submenu1.getSelectedIndex() == -1) {
                s_valida = "Submenu2";
                txt_nommenu.focus();
            } else {
                s_valida = "";
            }
        }
        return s_valida;
    }

    //metodos sin utilizar
    public void seleccionaRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
