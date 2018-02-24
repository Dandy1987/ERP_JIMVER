package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Propietario;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.xel.fn.StringFns;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerPropietario extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listapropietario, tab_mantenimiento;
    @Wire
    Listbox lst_propietario;
    @Wire
    Checkbox chk_propest, chk_busest;
    @Wire
    Textbox txt_propapepat, txt_propapemat, txt_propnom, txt_proprazsoc, txt_propdni,
            txt_propdirec, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Intbox txt_propid;
    @Wire
    Longbox txt_propruc, txt_proptelef;
    @Wire
    Datebox txt_propfecnac;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Button btn_buscar;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Propietario> objListModelPropietario;
    DaoPropietario objDaoPropietario = new DaoPropietario();
    //DaoHistoPropietario objDaoHistPropietario = new DaoHistoPropietario();
    Propietario objPropietario = new Propietario();
    UsuariosCredential cre;
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos;
    Utilitarios objUtil = new Utilitarios();
    ParametrosSalida objParam;

    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "", campo = "";
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerPropietario.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_propietario")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objListModelPropietario = new ListModelList<Propietario>();
        objListModelPropietario = objDaoPropietario.listaPropietario(1);
        lst_propietario.setModel(objListModelPropietario);
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
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30103000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Propietario con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Propietario con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Propietario");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Propietario");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Propietario");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Propietario");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Propietario");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Propietario");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Propietarios");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Propietarios");
        }
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
        objListModelPropietario = new ListModelList<Propietario>();
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
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objListModelPropietario = objDaoPropietario.busquedaPropietario(i_seleccion, s_consulta, i_estado);
        if (objListModelPropietario.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objListModelPropietario.getSize() + " registro(s)");
        }
        if (objListModelPropietario.getSize() > 0) {
            lst_propietario.setModel(objListModelPropietario);
        } else {
            objListModelPropietario = null;
            lst_propietario.setModel(objListModelPropietario);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
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

    @Listen("onSelect=#lst_propietario")
    public void seleccionaRegistro() throws SQLException, ParseException {
        objPropietario = (Propietario) lst_propietario.getSelectedItem().getValue();
        if (objPropietario == null) {
            objPropietario = objListModelPropietario.get(i_sel + 1);
        }
        i_sel = lst_propietario.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objPropietario.getProp_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objPropietario = new Propietario();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_propest.setDisabled(true);
        chk_propest.setChecked(true);
        txt_propapepat.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_propietario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un propietario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            //txt_propdni.setDisabled(true);
            //txt_propruc.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
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
                        validafocos();
                    }
                }
            });
        } else {
            /*if ((s_estado.equals("N") && objDaoPropietario.busquedaExistencia(1, txt_propdni.getValue()) > 0)) {
             Messagebox.show("El DNI ingresado ya está siendo utilizado ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
             @Override
             public void onEvent(Event event) throws Exception {
             if (((Integer) event.getData()).intValue() == Messagebox.OK) {
             txt_propdni.focus();
             }
             }
             });
             } else if ((s_estado.equals("N") && objDaoPropietario.busquedaExistencia(2, txt_propruc.getValue().toString()) > 0)) {
             Messagebox.show("El RUC ingresado ya está siendo utilizado ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
             @Override
             public void onEvent(Event event) throws Exception {
             if (((Integer) event.getData()).intValue() == Messagebox.OK) {
             txt_propruc.focus();
             }
             }
             });
             } else {*/
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objListModelPropietario = new ListModelList<Propietario>();
                                objPropietario = generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParam = objDaoPropietario.insertarPropietario(objPropietario);

                                } else {
                                    objParam = objDaoPropietario.actualizarPropietario(objPropietario);
                                }
                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                                objListModelPropietario = objDaoPropietario.listaPropietario(1);
                                lst_propietario.setModel(objListModelPropietario);
                                objPropietario = new Propietario();
                                lst_propietario.focus();
                            }
                        }
                    });
        }
        //}
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_propietario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un propietario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_propietario.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar este propietario?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objParam = objDaoPropietario.eliminarPropietario(objPropietario);
                                Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                objListModelPropietario = new ListModelList<Propietario>();
                                objListModelPropietario = objDaoPropietario.listaPropietario(1);
                                lst_propietario.setModel(objListModelPropietario);
                                limpiarCampos();
                                limpiaAuditoria();
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                lst_propietario.focus();
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
                            limpiaAuditoria();
                            lst_propietario.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_propietario.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objListModelPropietario == null || objListModelPropietario.isEmpty()) {
            Messagebox.show("No hay registros de propietarios para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_propietario.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionPropietario.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones
    @Listen("onCtrlKey=#w_manpropietario")
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
            llenaRegistros();
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void onOKBusqueda() {
        btn_buscar.setFocus(true);
    }

    @Listen("onOK=#txt_propapepat")
    public void onOKApepat() {
        txt_propapemat.setValue(txt_propapemat.getValue().trim());
        txt_propapemat.focus();
    }

    @Listen("onBlur=#txt_propapepat")
    public void onBlurApepat() {
        txt_proprazsoc.setValue(txt_propapepat.getValue() + " " + txt_propapemat.getValue() + " " + txt_propnom.getValue());
    }

    @Listen("onOK=#txt_propapemat")
    public void onOKApemat() {
        txt_propnom.setValue(txt_propnom.getValue().trim());
        txt_propnom.focus();
    }

    @Listen("onBlur=#txt_propapemat")
    public void onBlurApemat() {
        txt_proprazsoc.setValue(txt_propapepat.getValue() + " " + txt_propapemat.getValue() + " " + txt_propnom.getValue());
    }

    @Listen("onOK=#txt_propnom")
    public void onOKNombres() {
        txt_proprazsoc.setValue(txt_proprazsoc.getValue().trim());
        txt_propruc.focus();
    }

    @Listen("onBlur=#txt_propnom")
    public void onBlurNombres() {
        txt_proprazsoc.setValue(txt_propapepat.getValue() + " " + txt_propapemat.getValue() + " " + txt_propnom.getValue());
    }

    /*@Listen("onOK=#txt_proprazsoc")
     public void onOKRazsocial() {
     txt_propruc.focus();
     }*/
    @Listen("onOK=#txt_propruc")
    public void onOKRuc() {
        txt_propdni.setDisabled(false);
        txt_propdni.focus();
    }

    @Listen("onBlur=#txt_propruc")
    public void onBlurRuc() {
        if (txt_propruc.getValue() != null) {
            if (txt_propruc.getValue().toString().length() == 11) {
                String pridosdig = StringFns.substring(txt_propruc.getValue().toString(), 0, 2);
                if ("10".equals(pridosdig)) {
                    String dni = StringFns.substring(txt_propruc.getValue().toString(), 2, 10);
                    txt_propdni.setDisabled(true);
                    txt_propdni.setValue(dni);
                    txt_propfecnac.focus();
                } else if ("15".equals(pridosdig)) {
                    txt_propdni.setDisabled(false);
                    txt_propdni.setValue("");
                } else if ("20".equals(pridosdig)) {
                    txt_propdni.setValue("0");
                    txt_propdni.setDisabled(true);
                    txt_propfecnac.focus();
                } else {
                    Messagebox.show("El RUC ingresado es incorrecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_propruc.focus();

                            }
                        }
                    });
                }
            } else {
                Messagebox.show("Por favor ingrese 11 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_propruc.focus();

                        }
                    }
                });
            }
        }
    }

    @Listen("onOK=#txt_propdni")
    public void onOKDni() {
        txt_propfecnac.focus();
    }

    @Listen("onOK=#txt_propfecnac")
    public void onOKFecnac() {
        txt_proptelef.focus();
    }

    @Listen("onBlur=#txt_propfecnac")
    public void onBlurFecnac() {
        if (txt_propfecnac.getValue() != null) {
            objUtil = new Utilitarios();
            int edad = objUtil.validaEdadMayor(txt_propfecnac.getValue());
            if (edad < 18) {
                Messagebox.show("El propietario es menor de edad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_propfecnac.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onOK=#txt_proptelef")
    public void onOKTelefono() {
        txt_propdirec.focus();
    }

    //Eventos Otros 
    public Propietario generaRegistro() {
        int i_prop_id = txt_propid.getValue() == null ? 0 : txt_propid.getValue();
        int i_prop_est = chk_propest.isChecked() ? 1 : 2;
        String s_prop_apepat = txt_propapepat.getValue().toUpperCase();
        String s_prop_apemat = txt_propapemat.getValue().toUpperCase();
        String s_prop_nom = txt_propnom.getValue().toUpperCase();
        String s_prop_razsoc = txt_proprazsoc.getValue().toUpperCase();
        Long s_prop_ruc = txt_propruc.getValue();
        String s_prop_dni = txt_propdni.getValue();
        Date s_prop_fecnac = txt_propfecnac.getValue() == null ? null : txt_propfecnac.getValue();
        Long s_prop_telef = txt_proptelef.getValue() == null ? 0 : txt_proptelef.getValue();
        String s_prop_direcc = txt_propdirec.getValue().toUpperCase();
        String usu_add = objUsuCredential.getCuenta();
        String usu_mod = objUsuCredential.getCuenta();

        return new Propietario(i_prop_id, s_prop_apepat, s_prop_apemat, s_prop_nom, s_prop_razsoc, s_prop_ruc,
                s_prop_dni, s_prop_fecnac, s_prop_telef, s_prop_direcc, i_prop_est, usu_add, usu_mod);
    }

    public String verificar() {
        String mensaje = "";
        if (txt_propapepat.getValue().isEmpty()) {
            campo = "apepat";
            mensaje = "El campo 'APELLIDO PATERNO' es obligatorio";
        } else if (!txt_propapepat.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "apepat";
            mensaje = "Por favor ingresar solo letras en el campo 'APELLIDO PATERNO'";
        } else if (txt_propapemat.getValue().isEmpty()) {
            campo = "apemat";
            mensaje = "El campo 'APELLIDO MATERNO' es obligatorio";
        } else if (!txt_propapemat.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "apemat";
            mensaje = "Por favor ingresar solo letras en el campo 'APELLIDO MATERNO'";
        } else if (txt_propnom.getValue().isEmpty()) {
            campo = "nom";
            mensaje = "El campo 'NOMBRES' es obligatorio";
        } else if (!txt_propnom.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "nom";
            mensaje = "Por favor ingresar solo letras en el campo 'NOMBRES'";
        } else if (txt_proprazsoc.getValue().isEmpty()) {
            campo = "razsoc";
            mensaje = "El campo 'RAZ. SOCIAL' es obligatorio";
        } else if (txt_propruc.getValue() == null || txt_propruc.getValue().toString().length() != 11) {
            campo = "ruc";
            mensaje = "El campo 'RUC' es obligatorio";
        } else if (txt_propdni.getValue().isEmpty() && !txt_propdni.isDisabled()) {
            campo = "dni";
            mensaje = "El campo 'DNI' es obligatorio";
        } /*else if (txt_proptelef.getValue() == null) {
         campo = "tel";
         mensaje = "El campo 'TELEFONO' es obligatorio";
         }*/ else if (txt_propdirec.getValue().isEmpty()) {
            campo = "dir";
            mensaje = "El campo 'DIRECCION' es obligatorio";
        } else {
            campo = "";
        }
        /*else if (txt_propdni.getValue().isEmpty() || txt_propdni.getValue().length() < 8) {
         s_valor = "DNI";
         txt_propdni.focus();
         }*/

        return mensaje;
    }

    public void validafocos() {
        if (campo.equals("apepat")) {
            txt_propapepat.focus();
        } else if (campo.equals("apemat")) {
            txt_propapemat.focus();
        } else if (campo.equals("nom")) {
            txt_propnom.focus();
        } else if (campo.equals("razsoc")) {
            txt_proprazsoc.focus();
        } else if (campo.equals("ruc")) {
            txt_propruc.focus();
        } else if (campo.equals("dni")) {
            txt_propdni.focus();
        } else if (campo.equals("fecnac")) {
            txt_propfecnac.focus();
        } else if (campo.equals("dir")) {
            txt_propdirec.focus();
        }

    }

    public void limpiarLista() {
        //limpio mi lista
        objListModelPropietario = null;
        objListModelPropietario = new ListModelList<Propietario>();
        lst_propietario.setModel(objListModelPropietario);
    }

    public void limpiarCampos() {
        txt_propid.setValue(null);
        txt_propapepat.setValue("");
        txt_propapemat.setValue("");
        txt_propnom.setValue("");
        txt_proprazsoc.setValue("");
        txt_propruc.setValue(null);
        txt_propdni.setValue("");
        txt_propfecnac.setValue(null);
        txt_proptelef.setValue(null);
        txt_propdirec.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        chk_propest.setDisabled(b_valida);
        txt_propapepat.setDisabled(b_valida);
        txt_propapemat.setDisabled(b_valida);
        txt_propnom.setDisabled(b_valida);
        //txt_proprazsoc.setDisabled(b_valida);
        txt_propruc.setDisabled(b_valida);
        txt_propdni.setDisabled(b_valida);
        txt_propfecnac.setDisabled(b_valida);
        txt_proptelef.setDisabled(b_valida);
        txt_propdirec.setDisabled(b_valida);
    }

    public void llenarCampos() throws ParseException {
        txt_propid.setValue(objPropietario.getProp_id());
        txt_propapepat.setValue(objPropietario.getProp_apepat());
        txt_propapemat.setValue(objPropietario.getProp_apemat());
        txt_propnom.setValue(objPropietario.getProp_nom());
        txt_proprazsoc.setValue(objPropietario.getProp_razsoc());
        txt_propruc.setValue(objPropietario.getProp_ruc());
        txt_propdni.setValue(objPropietario.getProp_dni());
        txt_propfecnac.setValue(objPropietario.getProp_fecnac());
        txt_propdirec.setValue(objPropietario.getProp_direcc());
        txt_proptelef.setValue(objPropietario.getProp_telef());
        chk_propest.setChecked(objPropietario.getProp_est() == 1);
        txt_usuadd.setValue(objPropietario.getProp_usuadd());
        d_fecadd.setValue(objPropietario.getProp_fecadd());
        txt_usumod.setValue(objPropietario.getProp_usumod());
        d_fecmod.setValue(objPropietario.getProp_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapropietario.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapropietario.setSelected(b_valida1);
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

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
