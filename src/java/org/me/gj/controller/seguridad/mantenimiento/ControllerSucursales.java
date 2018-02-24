package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.*;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerSucursales extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listasucursales, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Listbox lst_sucursales;
    @Wire
    Intbox txt_id;
    @Wire
    Textbox txt_suclug, txt_sucdir, txt_sucfax, txt_busqueda;
    @Wire
    Longbox txt_suctel;
    @Wire
    Checkbox chk_sucest, chk_busest;
    @Wire
    Combobox cb_busqueda, cb_estado, cb_empsig;
    @Wire
    Spinner sp_sucord;
    @Wire
    Button btn_buscar;
    //Instancias de Objetos
    ListModelList<Sucursales> objlstSucursales = new ListModelList<Sucursales>();
    ListModelList<Empresas> objlstEmpresas = new ListModelList<Empresas>();
    DaoSucursales objDaoSucursales = new DaoSucursales();
    Sucursales objSucursal = new Sucursales();
    //Variables publicas
    int i_sel;
    String s_estado = "Q";
    String s_mensaje = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerSucursales.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_sucursales")
    public void llenaRegistros() throws SQLException {
        objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
        objlstSucursales = objDaoSucursales.lstSucursales("%%", 1);
        objlstEmpresas = new DaoEmpresas().lstEmpresas(1);
        lst_sucursales.setModel(objlstSucursales);
        cb_empsig.setModel(objlstEmpresas);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
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
        objlstSucursales = new ListModelList<Sucursales>();
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
        objlstSucursales = objDaoSucursales.busquedaSucursales(i_seleccion, s_consulta, i_estado);
        if (objlstSucursales.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstSucursales.getSize() + " registro(s)");
        }
        if (objlstSucursales.getSize() > 0) {
            lst_sucursales.setModel(objlstSucursales);
        } else {
            objlstSucursales = null;
            lst_sucursales.setModel(objlstSucursales);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        //limpiaAuditoria();
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

    @Listen("onSelect=#lst_sucursales")
    public void seleccionaRegistro() throws SQLException {
        objSucursal = (Sucursales) lst_sucursales.getSelectedItem().getValue();
        if (objSucursal == null) {
            objSucursal = objlstSucursales.get(i_sel + 1);
        }
        i_sel = lst_sucursales.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objSucursal.getSuc_id());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objSucursal = new Sucursales();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_sucest.setDisabled(true);
        chk_sucest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_sucursales.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            cb_empsig.setDisabled(true);
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
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
                                objlstSucursales = new ListModelList<Sucursales>();
                                objSucursal = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoSucursales.insertarSucursal(objSucursal);
                                } else {
                                    s_mensaje = objDaoSucursales.actualizarSucursal(objSucursal);
                                }
                                Messagebox.show(s_mensaje);
                                habilitaBotones(false, true);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                objlstSucursales = objDaoSucursales.lstSucursales("%%", 1);
                                lst_sucursales.setModel(objlstSucursales);
                                objSucursal = new Sucursales();
                                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_sucursales.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar este registro de sucursal?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoSucursales.eliminarSucursal(objSucursal);
                                llenaRegistros();
                                lst_sucursales.setModel(objlstSucursales);
                                Messagebox.show(s_mensaje);
                            }
                        }
                    });
        }
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
                            lst_sucursales.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstSucursales = new ListModelList<Sucursales>();
            objlstSucursales = objDaoSucursales.lstSucursales("", 1);
            lst_sucursales.setModel(objlstSucursales);
        }
    }

    //Eventos Otros     
    public void llenarCampos() {
        txt_id.setValue(objSucursal.getSuc_id());
        txt_suclug.setValue(objSucursal.getSuc_des());
        txt_sucdir.setValue(objSucursal.getSuc_dir());
        txt_suctel.setValue(objSucursal.getSuc_telef());
        txt_sucfax.setValue(objSucursal.getSuc_fax());
        txt_sucdir.setValue(objSucursal.getSuc_dir());
        cb_empsig.setValue(objSucursal.getEmp_sig());
        sp_sucord.setValue(objSucursal.getSuc_ord());
        if (objSucursal.getSuc_est() == 1) {
            chk_sucest.setChecked(true);
        } else {
            chk_sucest.setChecked(false);
        }
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasucursales.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasucursales.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_id.setValue(null);
        txt_sucdir.setValue("");
        txt_suclug.setValue(null);
        sp_sucord.setValue(null);
        cb_empsig.setValue("");
        txt_suctel.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstSucursales = null;
        objlstSucursales = new ListModelList<Sucursales>();
        lst_sucursales.setModel(objlstSucursales);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_sucdir.setDisabled(b_valida);
        txt_suclug.setDisabled(b_valida);
        cb_empsig.setDisabled(b_valida);
        chk_sucest.setDisabled(b_valida);
        sp_sucord.setDisabled(b_valida);
        txt_suctel.setDisabled(b_valida);
        txt_sucfax.setDisabled(b_valida);

    }

    public String verificar() {
        String s_valor;
        if (txt_sucdir.getValue().isEmpty()) {
            s_valor = "Direccion";
            txt_sucdir.focus();
        } else if (cb_empsig.getSelectedIndex() == -1) {
            s_valor = "Empresa";
            cb_empsig.focus();
        } else if (txt_suctel.getValue() == null) {
            s_valor = "Telefono";
            txt_suctel.focus();
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public Sucursales generaRegistro() {
        int i_sucid;
        if (txt_id.getValue() == null) {
            i_sucid = 0;
        } else {
            i_sucid = txt_id.getValue();
        }
        int i_sucest;
        if (chk_sucest.isChecked()) {
            i_sucest = 1;
        } else {
            i_sucest = 2;
        }
        String s_sucdir = txt_sucdir.getValue().toUpperCase().trim();
        String s_suclug = txt_suclug.getValue().toUpperCase().trim();
        int i_empid = cb_empsig.getSelectedItem().getValue();
        //int i_empid = Integer.parseInt(cb_empsig.getSelectedItem().toString());
        String s_empsig = cb_empsig.getValue().toUpperCase().trim();
        int i_sucord = sp_sucord.getValue();
        String s_usuadd = objUsuCredential.getCuenta();
        String s_usumod = objUsuCredential.getCuenta();
        long l_telefono = txt_suctel.getValue();
        String s_fax = txt_sucfax.getValue();
        return new Sucursales(i_sucid, s_empsig, s_suclug, i_empid, s_sucdir, l_telefono, s_fax, i_sucest, i_sucord,
                s_usuadd, s_usumod);
    }

    //metodos sin utilizar
    public void escogerOpcion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
