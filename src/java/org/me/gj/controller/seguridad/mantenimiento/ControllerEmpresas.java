package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerEmpresas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Listbox lst_empresas;
    @Wire
    Intbox txt_id;
    @Wire
    Textbox txt_empdes, txt_empsig, txt_empdir, txt_busqueda;
    @Wire
    Longbox txt_empruc;
    @Wire
    Checkbox chk_empest, chk_busest;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Tab tab_listaempresas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Button btn_buscar;
    //Instancias de Objetos
    ListModelList<Empresas> objlstEmpresas = new ListModelList<Empresas>();
    Empresas objEmpresa = new Empresas();
    DaoEmpresas objDaoEmpresas = new DaoEmpresas();
    //Variables publicas
    int i_sel;
    String s_estado = "Q";
    String s_mensaje = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerEmpresas.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_empresas")
    public void llenaRegistros() throws SQLException {
        objlstEmpresas = objDaoEmpresas.lstEmpresas(1);
        lst_empresas.setModel(objlstEmpresas);
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
        objlstEmpresas = new ListModelList<Empresas>();
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
        objlstEmpresas = objDaoEmpresas.busquedaEmpresas(i_seleccion, s_consulta, i_estado);
        if (objlstEmpresas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstEmpresas.getSize() + " registro(s)");
        }
        if (objlstEmpresas.getSize() > 0) {
            lst_empresas.setModel(objlstEmpresas);
        } else {
            objlstEmpresas = null;
            lst_empresas.setModel(objlstEmpresas);
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

    @Listen("onSelect=#lst_empresas")
    public void seleccionaRegistro() throws SQLException {
        objEmpresa = (Empresas) lst_empresas.getSelectedItem().getValue();
        if (objEmpresa == null) {
            objEmpresa = objlstEmpresas.get(i_sel + 1);
        }
        i_sel = lst_empresas.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objEmpresa.getEmp_id());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objEmpresa = new Empresas();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_empest.setDisabled(true);
        chk_empest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_empresas.getSelectedIndex() == -1) {
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
        //String s_validaruc = Verificaruc();
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_validaruc = Verificaruc();
            if (!s_validaruc.isEmpty()) {
                Messagebox.show(s_validaruc, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    objlstEmpresas = new ListModelList<Empresas>();
                                    objEmpresa = generaRegistro();
                                    if (s_estado.equals("N")) {
                                        s_mensaje = objDaoEmpresas.insertarEmpresa(objEmpresa);
                                    } else {
                                        s_mensaje = objDaoEmpresas.actualizarEmpresa(objEmpresa);
                                    }
                                    Messagebox.show(s_mensaje);
                                    habilitaBotones(false, true);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstEmpresas = objDaoEmpresas.lstEmpresas(1);
                                    lst_empresas.setModel(objlstEmpresas);
                                    objEmpresa = new Empresas();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_empresas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar este registro de empresa?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoEmpresas.eliminarEmpresa(objEmpresa);
                                habilitaBotones(false, true);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                objlstEmpresas.clear();
                                objlstEmpresas = objDaoEmpresas.lstEmpresas(0);
                                lst_empresas.setModel(objlstEmpresas);
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
                            lst_empresas.setSelectedIndex(-1);
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
            objlstEmpresas = new ListModelList<Empresas>();
            objlstEmpresas = objDaoEmpresas.lstEmpresas(1);
            lst_empresas.setModel(objlstEmpresas);
        }
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_id.setValue(objEmpresa.getEmp_id());
        txt_empdes.setValue(objEmpresa.getEmp_des());
        txt_empdir.setValue(objEmpresa.getEmp_dir());
        txt_empruc.setValue(objEmpresa.getEmp_ruc());
        txt_empsig.setValue(objEmpresa.getEmp_sig());
        if (objEmpresa.getEmp_est() == 1) {
            chk_empest.setChecked(true);
        } else {
            chk_empest.setChecked(false);
        }
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaempresas.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaempresas.setSelected(b_valida1);
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
        txt_empdes.setValue("");
        txt_empdir.setValue("");
        txt_empruc.setValue(null);
        txt_empsig.setValue("");
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstEmpresas = null;
        objlstEmpresas = new ListModelList<Empresas>();
        lst_empresas.setModel(objlstEmpresas);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_empdes.setDisabled(b_valida);
        txt_empdir.setDisabled(b_valida);
        txt_empruc.setDisabled(b_valida);
        txt_empsig.setDisabled(b_valida);
        chk_empest.setDisabled(b_valida);
    }

    public String verificar() {
        String s_valor;
        if (txt_empdes.getValue().isEmpty()) {
            s_valor = "Razon Social";
            txt_empdes.focus();
        } else if (txt_empdir.getValue().isEmpty()) {
            s_valor = "Dirección";
            txt_empdes.focus();
        } else if (txt_empruc.getValue() == null) {
            s_valor = "Ruc";
            txt_empdes.focus();
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public String Verificaruc() {
        String s_valor_ruc = "";
        // txt_dni.setConstraint("/^[0-9]+$/: Por Favor Solo ingrese Números");
        String s_ruc = txt_empruc.getValue().toString();
        if (s_ruc.length() < 11) {
            s_valor_ruc = "Por favor ingrese un Ruc de 11 numeros";
        }
        return s_valor_ruc;
    }

    public Empresas generaRegistro() {
        int i_empid;
        if (txt_id.getValue() == null) {
            i_empid = 0;
        } else {
            i_empid = txt_id.getValue();
        }
        String s_empdes = txt_empdes.getValue().toUpperCase().trim();
        int i_empest;
        if (chk_empest.isChecked()) {
            i_empest = 1;
        } else {
            i_empest = 2;
        }
        String s_empdir = txt_empdir.getValue().toUpperCase().trim();
        Long s_empruc = txt_empruc.getValue();
        String s_empsig = txt_empsig.getValue().toUpperCase().trim();
        String s_usuadd = objUsuCredential.getCuenta();
        String s_usumod = objUsuCredential.getCuenta();
        return new Empresas(i_empid, s_empsig, s_empdes, s_empdir, s_empruc, i_empest, s_usuadd, s_usumod);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void escogerOpcion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
