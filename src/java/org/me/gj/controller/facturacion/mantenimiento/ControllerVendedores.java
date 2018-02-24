package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerVendedores extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listavendedores, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;

    @Wire
    Intbox txt_vendiamor;
    @Wire
    Longbox txt_venmov, txt_vencel;
    @Wire
    Textbox txt_tabid, txt_venid, txt_supid, txt_venape, txt_vennom, txt_venpas, txt_supdes, txt_tabdes, txt_busqueda, txt_vendni, txt_nomrep,
            txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_busest;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_venest, chk_vengps;
    @Wire
    Listbox lst_vendedores;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Vendedor> objListModelVendedores = new ListModelList<Vendedor>();
    DaoVendedores objDaoVendedores = new DaoVendedores();
    Vendedor objVendedor = new Vendedor();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //variables publicas
    int i_sel;
    String s_estado = "Q", s_mensaje = "", modoEjecucion;
    String campo = "";
    public static boolean bandera;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerVendedores.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_vendedores")
    public void llenaRegistros() throws SQLException {
        objListModelVendedores = objDaoVendedores.listaVendedores(1);
        lst_vendedores.setModel(objListModelVendedores);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        lst_vendedores.focus(); 
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40107000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Vendedor con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Vendedor con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo registro de Vendedor ");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo registro de Vendedor  ");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un registro de Vendedor ");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un registro de Vendedor");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un registro de Vendedor ");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un registro de Vendedor");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Vendedor ");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Vendedor");
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
        objListModelVendedores = new ListModelList<Vendedor>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } //Busqueda por codigo
        else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del vendedor" + s_consulta + " para su busqueda");
        } //Busqueda por apellidos del vendedor
        else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el apellido del vendedor" + s_consulta + " para su busqueda");
        } //Busqueda por nombres del vendedor
        else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el nombre del vendedor " + s_consulta + " para su busqueda");
        } //Busqueda por canal de venta del vendedor
        else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el canal " + s_consulta + " para su busqueda");
        } //Busqueda por numero movil del vendedor
        else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el movil del vendedor " + s_consulta + " para su busqueda");
        } //Busqueda por DNI del vendedor
        else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el dni del vendedor " + s_consulta + " para su busqueda");
        } //Busqueda por supervisor del vendedor
        else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el nombre del supervisor " + s_consulta + " para su busqueda");
        }
        objListModelVendedores = objDaoVendedores.busquedaVendedores(i_seleccion, s_consulta, i_estado);
        if (objListModelVendedores.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objListModelVendedores.getSize() + " registro(s)");
        }
        if (objListModelVendedores.getSize() > 0) {
            lst_vendedores.setModel(objListModelVendedores);
        } else {
            limpiarLista();
            Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_vendedores")
    public void seleccionaRegistro() throws SQLException {
        objVendedor = (Vendedor) lst_vendedores.getSelectedItem().getValue();
        if (objVendedor == null) {
            objVendedor = objListModelVendedores.get(i_sel + 1);
        }
        i_sel = lst_vendedores.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objVendedor.getVen_key());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objVendedor = new Vendedor();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_venest.setDisabled(true);
        s_estado = "N";
        chk_venest.setChecked(true);
        chk_venest.setLabel("ACTIVO");
        txt_venid.setDisabled(true);
        txt_venape.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un nuevo registro de vendedores");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_vendedores.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un vendedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_vendedores.focus();
        } else {
            /*if (objDaoVendedores.busquedaExistencia(1, txt_venid.getValue()) > 0) {
             Messagebox.show("No se puede Editar, el vendedor esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             } else {*/
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_venid.setDisabled(true);
            txt_vendni.setDisabled(true);
            txt_venape.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro de vendedor");

        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_vendedores.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un vendedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_vendedores.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar el vendedor?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoVendedores.eliminar(objVendedor);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    limpiarLista();
                                    objListModelVendedores = objDaoVendedores.listaVendedores(1);
                                    lst_vendedores.setModel(objListModelVendedores);
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    VerificarTransacciones();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                lst_vendedores.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("ape")) {
                            txt_venape.focus();
                        } else if (campo.equals("nom")) {
                            txt_vennom.focus();
                        } else if (campo.equals("dni")) {
                            txt_vendni.focus();
                        } else if (campo.equals("pass")) {
                            txt_venpas.focus();
                        } else if (campo.equals("mov")) {
                            txt_venmov.focus();
                        } else if (campo.equals("cel")) {
                            txt_vencel.focus();
                        } else if (campo.equals("sup")) {
                            txt_supid.focus();
                        } else if (campo.equals("can")) {
                            txt_tabid.focus();
                        } else if (campo.equals("diam")) {
                            txt_vendiamor.focus();
                        } else if (campo.equals("nomrep")) {
                            txt_nomrep.focus();
                        } else if (campo.equals("orden")) {
                            sp_tabord.focus();
                        }
                    }
                }
            });
        } /*else {
         String s_validadni = Verificadni();
         if (!s_validadni.isEmpty()) {
         Messagebox.show(s_validadni, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
         @Override
         public void onEvent(Event event) throws Exception {
         if (((Integer) event.getData()).intValue() == Messagebox.OK) {
         if (campo.equals("Dni")) {
         txt_vendni.focus();
         }
         }
         }
         });
         } */ else {
            String s_existedni = existeDni();
            if (!s_existedni.isEmpty()) {
                Messagebox.show(s_existedni, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("DNI")) {
                                txt_vendni.focus();
                            }
                        }
                    }
                });
            } else {
                if (!txt_tabid.getValue().matches("[0-9]*") || !txt_supid.getValue().matches("[0-9]*")) {
                    lst_vendedores.focus();
                } else {

                    Canal objCanal = new DaoCanal().getDesCanal(txt_tabid.getValue());
                    Supervisor objSupervisor = new DaoSupervisores().getNomSupervisor(Integer.parseInt(txt_supid.getValue()));

                    if (objCanal == null || objSupervisor == null) {
                        lst_vendedores.focus();
                    } else {
                        s_mensaje = "Está seguro que desea guardar los cambios?";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            objVendedor = (Vendedor) generaRegistro();
                                            ParametrosSalida objParamSalida;
                                            if (s_estado.equals("N")) {
                                                objParamSalida = objDaoVendedores.insertar(objVendedor);
                                            } else {
                                                objParamSalida = objDaoVendedores.actualizar(objVendedor);
                                            }
                                            if (objParamSalida.getFlagIndicador() == 0) {
                                                habilitaTab(false, false);
                                                seleccionaTab(true, false);
                                                //validacion de guardar/nuevo
                                                VerificarTransacciones();
                                                tbbtn_btn_guardar.setDisabled(true);
                                                tbbtn_btn_deshacer.setDisabled(true);
                                                habilitaCampos(true);
                                                limpiarCampos();
                                                limpiaAuditoria();
                                                limpiarLista();
                                                objListModelVendedores = objDaoVendedores.listaVendedores(1);
                                                lst_vendedores.setModel(objListModelVendedores);
                                                objVendedor = new Vendedor();
                                            }
                                            Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            lst_vendedores.focus();
                                        }
                                    }
                                });
                    }
                }
            }
            // }
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
                            OnChange();
                            limpiaAuditoria();
                            limpiarLista();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            busquedaRegistros();
                            lst_vendedores.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objListModelVendedores == null || objListModelVendedores.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_vendedores.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionVen.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manvendedores")
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
            objListModelVendedores = new ListModelList<Vendedor>();
            objListModelVendedores = objDaoVendedores.listaVendedores(0);
            lst_vendedores.setModel(objListModelVendedores);
        }
    }

    @Listen("onOK=#txt_supid")
    public void lov_supervisores() {
        if (bandera == false) {
            bandera = true;
            if (txt_supid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantVendedores";
                mapeo.put("txt_supid", txt_supid);
                mapeo.put("txt_supdes", txt_supdes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerVendedores");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSupervisores.zul", null, mapeo);
                window.doModal();
            } else {
                txt_tabid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_supid")
    public void validaSupervisor() throws SQLException {
        if (txt_supid.getValue().isEmpty()) {
            txt_supid.setValue("");
            txt_supdes.setValue("");
        } else {
            if (!txt_supid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_supid.setValue("");
                            txt_supdes.setValue("");
                            txt_supid.focus();

                        }
                    }
                });
            } else {
                Supervisor objSupervisor;
                objSupervisor = new DaoSupervisores().getNomSupervisor(Integer.parseInt(txt_supid.getValue()));
                if (objSupervisor == null) {
                    Messagebox.show("El supervisor no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_supid.setValue("");
                                txt_supdes.setValue("");
                                txt_tabid.focus();

                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor con codigo " + objSupervisor.getSup_id() + " y ha encontrado 1 registro(s)");
                    txt_supid.setValue(Utilitarios.lpad((objSupervisor.getSup_id()), 4, "0"));
                    txt_supdes.setValue(objSupervisor.getSup_apenom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_tabid")
    public void lov_canales() {
        if (bandera == false) {
            bandera = true;
            if (txt_tabid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantVendedores";
                mapeo.put("txt_tabid", txt_tabid);
                mapeo.put("txt_tabdes", txt_tabdes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerVendedores");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCanales.zul", null, mapeo);
                window.doModal();
            } else {
                txt_vendiamor.focus();
            }
        }
    }

    @Listen("onBlur=#txt_tabid")
    public void validaCanal() throws SQLException {
        txt_tabdes.setValue("");
        if (txt_tabid.getValue().isEmpty()) {
            txt_tabid.setValue("");
            txt_tabdes.setValue("");
        } else {
            if (!txt_tabid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_tabid.setValue("");
                            txt_tabdes.setValue("");
                            txt_tabid.focus();
                        }
                    }
                });
            } else {
                Canal objCanal;
                objCanal = new DaoCanal().getDesCanal(txt_tabid.getValue());
                if (objCanal == null) {
                    Messagebox.show("El canal no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_tabid.setValue("");
                                txt_tabdes.setValue("");
                                txt_tabid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Canal con codigo " + objCanal.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_tabid.setValue(Utilitarios.lpad(String.valueOf(objCanal.getTab_id()), 2, "0"));
                    txt_tabdes.setValue(objCanal.getTab_subdes());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_venape")
    public void next_txt_venape() {
        txt_vennom.focus();
    }

    @Listen("onOK=#txt_vennom")
    public void next_txt_vennom() {
        txt_vendni.focus();
    }

    @Listen("onOK=#txt_vendni")
    public void next_txt_vendni() {
        txt_venpas.focus();
    }

    @Listen("onOK=#txt_venpas")
    public void next_txt_venpas() {
        txt_venmov.focus();
    }

    @Listen("onOK=#txt_venmov")
    public void next_txt_venmov() {
        txt_vencel.focus();
    }

    @Listen("onOK=#txt_vencel")
    public void next_txt_vencel() {
        txt_supid.focus();
    }

    @Listen("onOK=#txt_vendiamor")
    public void next_txt_vendiamor() {
        chk_vengps.focus();
    }

    @Listen("onOK=#chk_vengps")
    public void next_chk_vengps() {
        txt_nomrep.focus();
    }

    @Listen("onOK=#txt_nomrep")
    public void next_txt_nomrep() {
        sp_tabord.focus();
    }

    @Listen("onCheck=#chk_supest")
    public void VerificaEstado() throws SQLException {
        int i_supest = objVendedor.getVen_est();
        if (objDaoVendedores.busquedaExistencia(1, txt_venid.getValue()) > 0 || objDaoVendedores.busquedaExistencia(2, txt_venid.getValue()) > 0) {
            if (i_supest == 1) {
                chk_venest.setChecked(true);
                chk_venest.setLabel("ACTIVO");
                Messagebox.show("El código de vendedor está en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                chk_venest.setChecked(false);
                chk_venest.setLabel("INACTIVO");
                Messagebox.show("Ya no se puede activar el vendedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

            }
        }
    }

    //Eventos otros
    public String verificar() {
        //String s_mensaje;
        if (txt_venape.getValue().isEmpty()) {
            s_mensaje = "El campo 'APELLIDOS' es obligatorio";
            campo = "ape";
        } else if (txt_venape.getValue().matches("(\\s)+")) {
            s_mensaje = "El campo 'APELLIDOS', no debe tener espacios en blanco al principio";
            txt_venape.setValue("");
            campo = "ape";
        } else if (txt_venape.getValue().matches("([^\\w])+")) {
            s_mensaje = "El campo 'APELLIDOS' , no se permite caracteres especiales";
            txt_venape.setValue("");
            campo = "ape";
        } else if (txt_venape.getValue().matches("[0-9]*")) {
            s_mensaje = "Ingrese solo letras en el Campo 'APELLIDOS'";
            txt_venape.setValue("");
            campo = "ape";
        } else if (txt_vennom.getValue().isEmpty()) {
            s_mensaje = "El campo 'NOMBRES' es obligatorio";
            campo = "nom";
        } else if (txt_vennom.getValue().matches("(\\s)+")) {
            s_mensaje = "El campo 'NOMBRES', no debe tener espacios en blanco al principio";
            campo = "nom";
            txt_vennom.setValue("");
        } else if (txt_vennom.getValue().matches("([^\\w])+")) {
            s_mensaje = "El campo 'NOMBRES' , no se permite caracteres especiales";
            campo = "nom";
            txt_vennom.setValue("");
        }else if (txt_vennom.getValue().matches("[0-9]*")) {
            s_mensaje = "Ingrese solo letras en el Campo 'NOMBRES'";
            txt_vennom.setValue("");
            campo = "nom";
        } else if (txt_vendni.getValue().isEmpty()) {
            s_mensaje = "El campo 'DNI' es obligatorio";
            campo = "dni";
        } else if (txt_vendni.getValue().length() < 8) {
            s_mensaje = "Por favor ingrese un 'DNI' de 8 numeros";
            campo = "dni";
        } else if (!txt_venpas.getValue().matches("[0-9]*")) {
            s_mensaje = "Ingrese valores numericos en el Campo 'PASSWORD'";
            txt_venpas.setValue("");
            campo = "pass";
        } else if (txt_venpas.getValue().isEmpty()) {
            s_mensaje = "El campo 'PASSWORD' es obligatorio";
            campo = "pass";
        } else if (txt_venmov.getValue() == null) {
            s_mensaje = "El campo 'MOVIL' es obligatorio";
            campo = "mov";
        } else if (txt_vencel.getValue() == null) {
            s_mensaje = "El campo 'CELULAR' es obligatorio";
            campo = "cel";
        } else if (txt_supid.getValue().isEmpty()) {
            s_mensaje = "El campo 'SUPERVISOR' es obligatorio";
            campo = "sup";
        } else if (txt_tabid.getValue().isEmpty()) {
            s_mensaje = "El campo 'CANAL' es obligatorio";
            campo = "can";
        } else if (txt_vendiamor.getValue() == null) {
            s_mensaje = "El campo 'DIAS MOROSIDAD' es obligatorio";
            campo = "diam";
        } else if (txt_vendiamor.getValue() <= 0) {
            s_mensaje = "El 'DIAS MOROSIDAD' debe ser mayor que cero";
            txt_vendiamor.setValue(null);
            campo = "diam";
        } else if (txt_nomrep.getValue() == null) {
            s_mensaje = "El campo 'NOMBRE DE REPORTE' es obligatorio";
            campo = "nomrep";
        } else if (sp_tabord.getValue() == null) {
            s_mensaje = "El campo 'ORDEN' es obligatorio";
            campo = "orden";
        } else {
            s_mensaje = "";
        }
        return s_mensaje;
    }

    /* public String Verificadni() {
     String s_valor_dni = "";
     String s_dni = txt_vendni.getValue();
     if (s_dni.length() < 8) {
     s_valor_dni = "Por favor ingrese un Dni de 8 numeros";
     campo = "Dni";
     }
     return s_valor_dni;
     }*/
    public String existeDni() throws SQLException {
        String s_valida = "";
        Vendedor objVenDni;

        if (s_estado.equals("N")) {
            if (!txt_vendni.getValue().isEmpty()) {
                if (txt_vendni.getValue().matches("[0-9]*")) {
                    objVenDni = objDaoVendedores.existeDNI(txt_vendni.getValue());
                    if (objVenDni != null) {
                        if (objVenDni.getVen_dni().equals(txt_vendni.getValue())) {
                            s_valida = "El 'DNI' ya existe, verifique por favor";
                            campo = "DNI";
                        }
                    }
                } else {
                    txt_vendni.setValue("");
                    s_valida = "Ingrese valores numéricos en el campo 'DNI'";
                    campo = "DNI";
                }
            }
        }

        return s_valida;
    }

    public void OnChange() {
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setValue("%%");
        cb_busest.setSelectedIndex(0);

    }

    public void limpiarCampos() {

        txt_venid.setValue(null);
        chk_venest.setChecked(true);
        chk_venest.setLabel("ACTIVO");
        txt_venape.setValue("");
        txt_vennom.setValue("");
        txt_vendni.setValue("");
        txt_vendiamor.setValue(0);
        txt_vencel.setValue(null);
        txt_venmov.setValue(null);
        txt_venpas.setValue(null);
        chk_vengps.setChecked(true);
        chk_vengps.setLabel("ACTIVO");
        txt_supid.setValue(null);
        txt_supdes.setValue("");
        txt_tabid.setValue(null);
        txt_tabdes.setValue("");
        txt_nomrep.setValue("");
        sp_tabord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objVendedor = null;
        objListModelVendedores = new ListModelList<Vendedor>();
        lst_vendedores.setModel(objListModelVendedores);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listavendedores.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listavendedores.setDisabled(b_valida1);
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

    public void llenarCampos() {
        txt_venid.setValue(objVendedor.getVen_id());
        txt_venape.setValue(objVendedor.getVen_ape());
        txt_vennom.setValue(objVendedor.getVen_nom());
        txt_vendni.setValue(objVendedor.getVen_dni());
        txt_venpas.setValue(objVendedor.getVen_pas());
        txt_venmov.setValue(objVendedor.getVen_mov());
        txt_vencel.setValue(objVendedor.getVen_cel());
        txt_supid.setValue(objVendedor.getSup_id());
        txt_supdes.setValue(objVendedor.getSup_des());
        txt_tabid.setValue(objVendedor.getTab_subdir());
        txt_tabdes.setValue(objVendedor.getCanal());
        txt_vendiamor.setValue(objVendedor.getVen_diamor());
        txt_nomrep.setValue(objVendedor.getVen_nomrep());
        sp_tabord.setValue(objVendedor.getVen_ord());
        txt_usuadd.setValue(objVendedor.getVen_usuadd());
        d_fecadd.setValue(objVendedor.getVen_fecadd());
        txt_usumod.setValue(objVendedor.getVen_usumod());
        d_fecmod.setValue(objVendedor.getVen_fecmod());

        if (objVendedor.getVen_est() == 1) {
            chk_venest.setChecked(true);
            chk_venest.setLabel("ACTIVO");
        } else {
            chk_venest.setChecked(false);
            chk_venest.setLabel("INACTIVO");
        }
        if (objVendedor.getVen_gps() == 1) {
            chk_vengps.setChecked(true);
            chk_vengps.setLabel("ACTIVO");
        } else {
            chk_vengps.setChecked(false);
            chk_vengps.setLabel("INACTIVO");
        }
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_venest.setDisabled(b_valida1);
        txt_venape.setDisabled(b_valida1);
        txt_vennom.setDisabled(b_valida1);
        txt_vendni.setDisabled(b_valida1);
        txt_vendiamor.setDisabled(b_valida1);
        txt_vencel.setDisabled(b_valida1);
        txt_venmov.setDisabled(b_valida1);
        txt_venpas.setDisabled(b_valida1);
        chk_vengps.setDisabled(b_valida1);
        txt_supid.setDisabled(b_valida1);
        txt_tabid.setDisabled(b_valida1);
        txt_nomrep.setDisabled(b_valida1);
        sp_tabord.setDisabled(b_valida1);
    }

    public Object generaRegistro() {
        int i_ven_key = 0;
        String s_ven_id;
        String s_ven_nom;
        String s_ven_ape;
        String s_ven_dni;
        long l_ven_cel;
        long l_ven_mov;
        int i_tab_id;
        String s_can_des;
        int i_tab_cod;
        String s_ven_pas;
        int i_ven_diamor;
        int i_ven_gps;
        int i_sup_key;
        String s_sup_id;
        String s_sup_des;
        String s_ven_nomrep;
        int i_ven_ord;
        int i_ven_est;
        int i_emp_id;
        int i_suc_id;
        String s_ven_usuadd;
        String s_ven_usumod;

        if (s_estado.equals("N")) {
            s_ven_id = "";
            s_ven_nom = txt_vennom.getValue();
            s_ven_ape = txt_venape.getValue();
            s_ven_dni = txt_vendni.getValue();
            l_ven_cel = txt_vencel.getValue();
            l_ven_mov = txt_venmov.getValue();
            i_tab_id = Integer.parseInt(txt_tabid.getValue());
            s_can_des = txt_tabdes.getValue();
            i_tab_cod = 28;
            s_ven_pas = txt_venpas.getValue();
            i_ven_diamor = txt_vendiamor.getValue();
            i_ven_gps = (chk_vengps.isChecked() == true) ? 1 : 2;
            i_sup_key = Integer.parseInt(txt_supid.getValue());
            s_sup_id = txt_supid.getValue();
            s_sup_des = txt_supdes.getValue();
            s_ven_nomrep = txt_nomrep.getValue();
            i_ven_ord = sp_tabord.getValue();
            i_ven_est = (chk_venest.isChecked() == true) ? 1 : 2;
            i_emp_id = objUsuCredential.getCodemp();
            i_suc_id = objUsuCredential.getCodsuc();
            s_ven_usuadd = objUsuCredential.getCuenta();
            s_ven_usumod = objUsuCredential.getCuenta();
        } else {
            i_ven_key = Integer.parseInt(txt_venid.getValue());
            s_ven_id = txt_venid.getValue();
            s_ven_nom = txt_vennom.getValue();
            s_ven_ape = txt_venape.getValue();
            s_ven_dni = txt_vendni.getValue();
            l_ven_cel = txt_vencel.getValue();
            l_ven_mov = txt_venmov.getValue();
            i_tab_id = Integer.parseInt(txt_tabid.getValue());
            s_can_des = txt_tabdes.getValue();
            i_tab_cod = 28;
            s_ven_pas = txt_venpas.getValue();
            i_ven_diamor = txt_vendiamor.getValue();
            i_ven_gps = (chk_vengps.isChecked() == true) ? 1 : 2;
            i_sup_key = Integer.parseInt(txt_supid.getValue());
            s_sup_id = txt_supid.getValue();
            s_sup_des = txt_supdes.getValue();
            s_ven_nomrep = txt_nomrep.getValue();
            i_ven_ord = sp_tabord.getValue();
            i_ven_est = (chk_venest.isChecked() == true) ? 1 : 2;
            i_emp_id = objUsuCredential.getCodemp();
            i_suc_id = objUsuCredential.getCodsuc();
            s_ven_usuadd = objUsuCredential.getCuenta();
            s_ven_usumod = objUsuCredential.getCuenta();
        }
        return new Vendedor(i_ven_key, s_ven_id, i_emp_id, i_suc_id, i_sup_key, s_sup_id, i_ven_est, s_ven_nom, s_ven_ape, s_ven_dni, l_ven_cel,
                l_ven_mov, i_tab_id, s_can_des, i_tab_cod, s_ven_pas, i_ven_diamor, i_ven_gps, s_sup_des, true, s_ven_nomrep,
                i_ven_ord, s_ven_usuadd, s_ven_usumod);
    }
}
