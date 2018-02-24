package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Menus;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.mantenimiento.Usuarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;

public class ControllerAccesos extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Window w_accesos;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Combobox cb_usuarios, cb_empresas, cb_sucursales, cb_modulos, cb_menus, cb_submenu1, cb_submenu2;
    @Wire
    Radiogroup rb_grupo;
    @Wire
    Radio rb_modulo, rb_menu, rb_submenu1, rb_submenu2;
    @Wire
    Listbox lst_modulos, lst_menus, lst_submenu1, lst_submenu2, lst_accesos;
    @Wire
    Button btn_veracc;
    @Wire
    Tab tab_listaaccesos, tab_listaaccesos_1;
    @Wire
    Checkbox chk_ins, chk_eli, chk_mod, chk_imp;
    //Instancias de Objetos
    ListModelList<Usuarios> objlstUsuarios = new ListModelList<Usuarios>();
    ListModelList<Empresas> objlstEmpresas = new ListModelList<Empresas>();
    ListModelList<Sucursales> objlstSucursales = new ListModelList<Sucursales>();
    ListModelList<Menus> objlstModulos = new ListModelList<Menus>();
    ListModelList<Menus> objlstMenus = new ListModelList<Menus>();
    ListModelList<Menus> objlstSubmenu1 = new ListModelList<Menus>();
    ListModelList<Menus> objlstSubmenu2 = new ListModelList<Menus>();
    ListModelList<Accesos> objlstAccesos = new ListModelList<Accesos>();
    Usuarios objUsuarios = new Usuarios();
    Empresas objEmpresas = new Empresas();
    Sucursales objSucursales = new Sucursales();
    Menus objModulos = new Menus();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_mensaje = "";
    String usuario = "";
    int i_codusu = 0;
    int i_empresa = 0;
    int i_sucursal = 0;
    int i_menu = 0;
    int i_modulo = 0;
    int i_submenu1 = 0;
    int i_submenu2 = 0;
    int i_insertar = 0;
    int i_eliminar = 0;
    int i_modificar = 0;
    int i_imprimir = 0;
    int i_selec = 0;
    int i_sel;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAccesos.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_accesos")
    public void llenaRegistros() throws SQLException {
        //cargamos los usuarios
        objlstUsuarios = objDaoAccesos.lstUsuarios();
        cb_usuarios.setModel(objlstUsuarios);
        //cargamos las empresas
        objlstEmpresas = objDaoAccesos.lstEmpresas();
        cb_empresas.setModel(objlstEmpresas);
        cb_sucursales.setValue("");
        cb_sucursales.setDisabled(true);
        //cargamos los modulos
        objlstModulos = objDaoAccesos.lstModulos();
        cb_modulos.setModel(objlstModulos);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaCampos(false);
        //combos
        cb_modulos.setDisabled(true);
        cb_menus.setDisabled(true);
        cb_submenu1.setDisabled(true);
        cb_submenu2.setDisabled(true);
        //checkbox
        chk_eli.setChecked(true);
        chk_imp.setChecked(true);
        chk_ins.setChecked(true);
        chk_mod.setChecked(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
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
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            tbbtn_btn_eliminar.setDisabled(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        i_selec = ElegirAcceso();
        String s_valida = verificar(i_selec);
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                Session sess = Sessions.getCurrent();
                                objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
                                String usuario = objUsuCredential.getCuenta();
                                i_codusu = cb_usuarios.getSelectedItem().getValue();
                                i_modulo = cb_modulos.getSelectedItem().getValue();
                                i_empresa = cb_empresas.getSelectedItem().getValue();
                                i_sucursal = cb_sucursales.getSelectedItem().getValue();
                                //INSERTAR, MODIFICAR, ELIMINAR, IMPRIMIR
                                if (chk_ins.isChecked()) {
                                    i_insertar = 1;
                                }
                                if (chk_eli.isChecked()) {
                                    i_eliminar = 1;
                                }
                                if (chk_mod.isChecked()) {
                                    i_modificar = 1;
                                }
                                if (chk_imp.isChecked()) {
                                    i_imprimir = 1;
                                }
                                if (i_selec == 1) {
                                    objAccesos = new Accesos(i_codusu, i_modulo, i_empresa, usuario, i_sucursal, 0, 0, 0, 0, i_insertar, i_modificar, i_eliminar, i_imprimir);
                                } else if (i_selec == 2) {
                                    i_menu = cb_menus.getSelectedItem().getValue();
                                    objAccesos = new Accesos(i_codusu, i_menu, i_empresa, usuario, i_sucursal, i_modulo, 0, 0, 0, i_insertar, i_modificar, i_eliminar, i_imprimir);
                                } else if (i_selec == 3) {
                                    i_menu = cb_menus.getSelectedItem().getValue();
                                    i_submenu1 = cb_submenu1.getSelectedItem().getValue();
                                    objAccesos = new Accesos(i_codusu, i_submenu1, i_empresa, usuario, i_sucursal, i_modulo, i_menu, 0, 0, i_insertar, i_modificar, i_eliminar, i_imprimir);
                                } else if (i_selec == 4) {
                                    i_menu = cb_menus.getSelectedItem().getValue();
                                    i_submenu1 = cb_submenu1.getSelectedItem().getValue();
                                    i_submenu2 = cb_submenu2.getSelectedItem().getValue();
                                    objAccesos = new Accesos(i_codusu, i_submenu2, i_empresa, usuario, i_sucursal, i_modulo, i_menu, i_submenu1, 0, i_insertar, i_modificar, i_eliminar, i_imprimir);
                                }
                                s_mensaje = objDaoAccesos.insertarAcceso(i_selec, objAccesos);
                                Messagebox.show(s_mensaje);
                            }
                        }
                    });
        }

    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_accesos.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este acceso?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                i_empresa = cb_empresas.getSelectedItem().getValue();
                                i_sucursal = cb_sucursales.getSelectedItem().getValue();
                                i_codusu = cb_usuarios.getSelectedItem().getValue();
                                s_mensaje = objDaoAccesos.eliminarAcceso(objAccesos.getMen_id(), i_empresa, i_sucursal, i_codusu);
                                objlstAccesos = new ListModelList<Accesos>();
                                lst_accesos.setModel(objlstAccesos);
                                Messagebox.show(s_mensaje);
                                limpiarCampos();
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                seleccionaTab(true, false);
                                habilitaBotones(false, true);
                                habilitaCampos(true);
                                tbbtn_btn_eliminar.setDisabled(true);
                            }
                        }
                    });
        }
    }

    @Listen("onSelect=#cb_empresas")
    public void SeleccionaEmpresa() throws SQLException {
        //cargamos las sucursales
        int i_codemp;
        cb_sucursales.setValue("");
        i_codemp = cb_empresas.getSelectedItem().getValue();
        objlstSucursales = objDaoAccesos.lstSucursales(i_codemp);
        cb_sucursales.setModel(objlstSucursales);
        cb_sucursales.setDisabled(false);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Empresa con el codigo " + i_codemp);
    }

    @Listen("onSelect=#cb_modulos")
    public void SeleccionaModulo() throws SQLException {
        //limpiamos las demas listas
        int i_codmod;
        i_codmod = cb_modulos.getSelectedItem().getValue();
        cb_menus.setValue("");
        cb_submenu1.setValue("");
        cb_submenu2.setValue("");
        //cargamos los menus
        objlstMenus = objDaoAccesos.lstMenus(i_codmod);
        cb_menus.setModel(objlstMenus);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Modulo con el codigo " + i_codmod);
    }

    @Listen("onSelect=#cb_menus")
    public void SeleccionaMenus() throws SQLException {
        cb_submenu1.setValue("");
        cb_submenu2.setValue("");
        //cargamos los submenus
        int i_codmen;
        int i_codmod;
        i_codmod = cb_modulos.getSelectedItem().getValue();
        i_codmen = cb_menus.getSelectedItem().getValue();
        objlstSubmenu1 = objDaoAccesos.lstSubmenu1(i_codmod, i_codmen);
        cb_submenu1.setModel(objlstSubmenu1);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Modulo con el codigo " + i_codmen);
    }

    @Listen("onSelect=#cb_submenu1")
    public void SeleccionaSubmenu() throws SQLException {
        //cargamos los submenus
        cb_submenu2.setValue("");
        int i_codmen;
        int i_codmod;
        int i_codsubmenu;
        i_codmod = cb_modulos.getSelectedItem().getValue();
        i_codmen = cb_menus.getSelectedItem().getValue();
        i_codsubmenu = cb_submenu1.getSelectedItem().getValue();
        objlstSubmenu2 = objDaoAccesos.lstSubmenu2(i_codmod, i_codmen, i_codsubmenu);
        cb_submenu2.setModel(objlstSubmenu2);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Modulo con el codigo " + i_codsubmenu);
    }

    @Listen("onSelect=#lst_accesos")
    public void SeleccionaLista() {
        objAccesos = (Accesos) lst_accesos.getSelectedItem().getValue();
        if (objAccesos == null) {
            objAccesos = objlstAccesos.get(i_sel + 1);
        }
        i_sel = lst_accesos.getSelectedIndex();
        tbbtn_btn_eliminar.setDisabled(false);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo de usuario " + objAccesos.getUsu_id());
    }

    //Eventos Secundarios - Validacion
    @Listen("onCheck=#rb_grupo")
    public int ElegirAcceso() {
        int i_seleccion = 0;
        if (rb_modulo.isChecked()) {
            i_seleccion = 1;
            cb_modulos.setDisabled(false);
            cb_menus.setDisabled(true);
            cb_submenu1.setDisabled(true);
            cb_submenu2.setDisabled(true);
            chk_eli.setDisabled(false);
            chk_imp.setDisabled(false);
            chk_ins.setDisabled(false);
            chk_mod.setDisabled(false);
        } else if (rb_menu.isChecked()) {
            i_seleccion = 2;
            cb_modulos.setDisabled(false);
            cb_menus.setDisabled(false);
            cb_submenu1.setDisabled(true);
            cb_submenu2.setDisabled(true);
            chk_eli.setDisabled(false);
            chk_imp.setDisabled(false);
            chk_ins.setDisabled(false);
            chk_mod.setDisabled(false);
        } else if (rb_submenu1.isChecked()) {
            i_seleccion = 3;
            cb_modulos.setDisabled(false);
            cb_menus.setDisabled(false);
            cb_submenu1.setDisabled(false);
            cb_submenu2.setDisabled(true);
            chk_eli.setDisabled(false);
            chk_imp.setDisabled(false);
            chk_ins.setDisabled(false);
            chk_mod.setDisabled(false);
        } else if (rb_submenu2.isChecked()) {
            i_seleccion = 4;
            cb_modulos.setDisabled(false);
            cb_menus.setDisabled(false);
            cb_submenu1.setDisabled(false);
            cb_submenu2.setDisabled(false);
            chk_eli.setDisabled(false);
            chk_imp.setDisabled(false);
            chk_ins.setDisabled(false);
            chk_mod.setDisabled(false);
        }
        return i_seleccion;
    }

    //Eventos Otros 
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaaccesos.setSelected(b_valida1);
        //tab_listaaccesos_1.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaaccesos.setDisabled(b_valida1);
        //tab_listaaccesos_1.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida1) {
        //radiogroup
        rb_menu.setDisabled(b_valida1);
        rb_modulo.setDisabled(b_valida1);
        rb_submenu1.setDisabled(b_valida1);
        rb_submenu2.setDisabled(b_valida1);
        //combos
        cb_modulos.setDisabled(b_valida1);
        cb_menus.setDisabled(b_valida1);
        cb_submenu1.setDisabled(b_valida1);
        cb_submenu2.setDisabled(b_valida1);
        //transacciones
        chk_eli.setDisabled(b_valida1);
        chk_imp.setDisabled(b_valida1);
        chk_ins.setDisabled(b_valida1);
        chk_mod.setDisabled(b_valida1);

    }

    public void limpiarCampos() {
        cb_empresas.setSelectedIndex(-1);
        cb_menus.setSelectedIndex(-1);
        cb_modulos.setSelectedIndex(-1);
        cb_submenu1.setSelectedIndex(-1);
        cb_submenu2.setSelectedIndex(-1);
        cb_sucursales.setSelectedIndex(-1);
        cb_usuarios.setSelectedIndex(-1);
        rb_menu.setChecked(false);
        rb_modulo.setChecked(false);
        rb_submenu1.setChecked(false);
        rb_submenu2.setChecked(false);
        chk_eli.setChecked(false);
        chk_imp.setChecked(false);
        chk_ins.setChecked(false);
        chk_mod.setChecked(false);
    }

    public String verificar(int i_sel) {
        String s_valida = "";
        //valida campos si es serie
        if (cb_usuarios.getSelectedIndex() == -1) {
            s_valida = "Usuarios";
        } else if (cb_empresas.getSelectedIndex() == -1) {
            s_valida = "Empresas";
        } else if (cb_sucursales.getSelectedIndex() == -1) {
            s_valida = "Sucursales";
        } else if (rb_modulo.isChecked() == false && rb_menu.isChecked() == false && rb_submenu1.isChecked() == false && rb_submenu2.isChecked() == false) {
            s_valida = "Accesos";
        } else {
            if (i_sel == 1) {
                if (cb_modulos.getSelectedIndex() == -1) {
                    s_valida = "Modulos";
                } else {
                    s_valida = "";
                }
            } else if (i_sel == 2) {
                if (cb_modulos.getSelectedIndex() == -1) {
                    s_valida = "Modulos";
                } else if (cb_menus.getSelectedIndex() == -1) {
                    s_valida = "Menus";
                } else {
                    s_valida = "";
                }
            } else if (i_sel == 3) {
                if (cb_modulos.getSelectedIndex() == -1) {
                    s_valida = "Modulos";
                } else if (cb_menus.getSelectedIndex() == -1) {
                    s_valida = "Menus";
                } else if (cb_submenu1.getSelectedIndex() == -1) {
                    s_valida = "Submenu1";
                } else {
                    s_valida = "";
                }
            } else if (i_sel == 4) {
                if (cb_modulos.getSelectedIndex() == -1) {
                    s_valida = "Modulos";
                } else if (cb_menus.getSelectedIndex() == -1) {
                    s_valida = "Menus";
                } else if (cb_submenu1.getSelectedIndex() == -1) {
                    s_valida = "Submenu1";
                } else if (cb_submenu2.getSelectedIndex() == -1) {
                    s_valida = "Submenu2";
                } else {
                    s_valida = "";
                }

            }
        }

        return s_valida;
    }

    //metodos sin utilizar
    public void seleccionaRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void botonEditar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void escogerOpcion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
