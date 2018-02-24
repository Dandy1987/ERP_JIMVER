package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.facturacion.mantenimiento.Ruta;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerRutas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Textbox txt_candes, txt_mesades, txt_rutnomrep, txt_busqueda, txt_canid, txt_mesaid,
            txt_usuadd, txt_usumod;
    @Wire
    Spinner sp_corid, sp_rutord;
    @Wire
    Combobox cb_busest;
    @Wire
    Combobox cb_busqueda;
    @Wire
    Checkbox chk_busest, chk_rutest;
    @Wire
    Listbox lst_rutas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Tab tab_listarutas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    //Instancias de Objetos
    ListModelList<Ruta> objlstRutas = new ListModelList<Ruta>();
    Ruta objRuta = new Ruta();
    DaoRutas objDaoRutas = new DaoRutas();
    DaoCanal objDaoCanales = new DaoCanal();
    DaoMesa objDaoMesas = new DaoMesa();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado, s_mensaje, modoEjecucion;
    String campo = "";
    public static boolean bandera = false;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRutas.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_rutas")
    public void llenaRegistros() throws SQLException {
        objlstRutas = objDaoRutas.listaRutas(1);
        lst_rutas.setModel(objlstRutas);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40105000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Superviores con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Superviores con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo registro de Supervisor de Compra ");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo registro de Supervisor  ");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un registro de Supervisor ");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un registro de Supervisor");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un registro de Supervisor ");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un registro de Supervisor");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Supervisores ");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Supervisores");
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
        objlstRutas = new ListModelList<Ruta>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el Codigo de Ruta" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el Codigo del Canal " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la Descripcion del Canal" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el Numero de Mesa " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el Nombre del Supervisor " + s_consulta + " para su busqueda");
        }
        objlstRutas = objDaoRutas.busquedaRutas(i_seleccion, s_consulta, i_estado);
        
        if (objlstRutas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstRutas.getSize() + " registro(s)");
        }
        
        if (objlstRutas.getSize() > 0) {
            lst_rutas.setModel(objlstRutas);
        } else {
            limpiarLista();
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

    @Listen("onSelect=#lst_rutas")
    public void seleccionaRegistro() throws SQLException {
        objRuta = (Ruta) lst_rutas.getSelectedItem().getValue();
        if (objRuta == null) {
            objRuta = objlstRutas.get(i_sel + 1);
        }
        i_sel = lst_rutas.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objRuta.getRut_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objRuta = new Ruta();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_rutest.setDisabled(true);
        chk_rutest.setChecked(true);
        txt_canid.focus();
        txt_candes.setDisabled(true);
        txt_mesades.setDisabled(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_rutas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_rutas.focus();
        } else {
            /*if (objDaoRutas.busquedaExistencia(1, (txt_canid.getValue() + txt_mesaid.getValue() + Utilitarios.lpad(String.valueOf(sp_corid.getValue()), 2, "0"))) > 0) {
             Messagebox.show("No se puede Editar, la ruta esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             } else {*/
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            //txt_canid.focus();
            txt_canid.setDisabled(true);
            txt_candes.setDisabled(true);
            txt_mesaid.setDisabled(true);
            txt_mesades.setDisabled(true);
            sp_corid.setDisabled(true);
            s_estado = "E";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            // }
        }
    } 

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_rutas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_rutas.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar esta ruta?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoRutas.eliminar(objRuta);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstRutas.clear();
                                    objlstRutas = objDaoRutas.listaRutas(1);
                                    lst_rutas.setModel(objlstRutas);
                                    limpiaAuditoria();
                                    limpiarCampos();
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                lst_rutas.focus();
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
                        validafocos();
                    }
                }
            });
        } else {
            if (!txt_canid.getValue().matches("[0-9]*") || !txt_mesaid.getValue().matches("[0-9]*")) {
                lst_rutas.focus();
            } else {
                int can_id = Integer.parseInt(txt_canid.getValue());
                int mes_id = Integer.parseInt(txt_mesaid.getValue());
                Canal objCanal = objDaoCanales.getDesCanal(can_id);
                Mesa objMesa = objDaoMesas.busMesaxCanal(can_id, mes_id);
                Ruta objRut = objDaoRutas.buscarRuta((txt_canid.getValue().concat(txt_mesaid.getValue()).concat(Utilitarios.lpad(sp_corid.getValue().toString(), 2, "0"))));
                if (objCanal == null || objMesa == null) {
                    lst_rutas.focus();
                } else if (objRut != null && chk_rutest.isChecked()) {
                    Messagebox.show("La ruta ya existe","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                } else {
                    s_mensaje = "Está seguro que desea guardar los cambios?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        objRuta = (Ruta) generaRegistro();
                                        ParametrosSalida objParamSalida;
                                        if (s_estado.equals("N")) {
                                            objParamSalida = objDaoRutas.insertar(objRuta);
                                        } else {
                                            objParamSalida = objDaoRutas.modificar(objRuta);
                                        }
                                        if (objParamSalida.getFlagIndicador() == 0) {
                                            //validacion de guardar/nuevo
                                            VerificarTransacciones();
                                            tbbtn_btn_guardar.setDisabled(true);
                                            tbbtn_btn_deshacer.setDisabled(true);
                                            //
                                            habilitaTab(false, false);
                                            seleccionaTab(true, false);
                                            habilitaCampos(true);
                                            limpiaAuditoria();
                                            limpiarCampos();
                                            objlstRutas.clear();
                                            objlstRutas = objDaoRutas.listaRutas(1);
                                            lst_rutas.setModel(objlstRutas);
                                            objRuta = new Ruta();
                                        }
                                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            });
                }
            }
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
                            OnChange();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            busquedaRegistros();
                            lst_rutas.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstRutas == null || objlstRutas.isEmpty()) {
            Messagebox.show("No hay registros de rutas para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionRuta.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manrut")
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
            objlstRutas = new ListModelList<Ruta>();
            objlstRutas = objDaoRutas.listaRutas(0);
            lst_rutas.setModel(objlstRutas);
        }
    }

    @Listen("onOK=#txt_canid")
    public void busquedaCanales() {
        if (bandera == false) {
            bandera = true;
            if (txt_canid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantRutas";
                mapeo.put("txt_canid", txt_canid);
                mapeo.put("txt_candes", txt_candes);
                mapeo.put("txt_mesaid", txt_mesaid);
                mapeo.put("validaBusqueda", modoEjecucion);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCanales.zul", null, mapeo);
                window.doModal();
            } else {
                txt_mesaid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_canid")
    public void validaCanal() throws SQLException {
        if (!txt_canid.getValue().isEmpty()) {
            if (!txt_canid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_canid.setValue("");
                            txt_candes.setValue("");
                            txt_mesaid.setValue("");
                            txt_mesades.setValue("");
                            txt_canid.focus();
                        }
                    }
                });
            } else {
                Canal objCanal;
                int can_id = Integer.parseInt(txt_canid.getValue());
                objCanal = objDaoCanales.getDesCanal(can_id);
                if (objCanal == null) {
                    Messagebox.show("Código de canal no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_canid.setValue("");
                                txt_candes.setValue("");
                                txt_mesaid.setValue("");
                                txt_mesades.setValue("");
                                txt_canid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Canal con codigo " + txt_canid.getValue() + " y ha encontrado 1 registro(s)");
                    txt_canid.setValue(Utilitarios.lpad(String.valueOf(objCanal.getTab_id()), 2, "0"));
                    txt_candes.setValue(objCanal.getTab_subdes());
                }
            }
        } else {
            txt_canid.setValue("");
            txt_candes.setValue("");
            txt_mesaid.setValue("");
            txt_mesades.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_mesaid")
    public void busquedaMesas() {
        if (bandera == false) {
            bandera = true;
            if (!txt_canid.getValue().isEmpty()) {
                if (txt_mesaid.getValue().isEmpty()) {
                    Map mapeo = new HashMap();
                    modoEjecucion = "mantRutas";
                    mapeo.put("txt_mesaid", txt_mesaid);
                    mapeo.put("txt_mesades", txt_mesades);
                    mapeo.put("txt_canid", txt_canid);
                    mapeo.put("sp_corid", sp_corid);
                    mapeo.put("validaBusqueda", modoEjecucion);
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMesas.zul", null, mapeo);
                    window.doModal();
                } else {
                    sp_corid.focus();
                }
            } else {
                lst_rutas.focus();
            }
        }
    }

    @Listen("onBlur=#txt_mesaid")
    public void validaMesa() throws SQLException {
        if (txt_canid.getValue().isEmpty()) {
            Messagebox.show("Por favor primero ingrese el canal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_mesaid.setValue("");
                        txt_mesades.setValue("");
                        txt_canid.focus();
                    }
                }
            });
        } else {
            if (txt_canid.getValue().matches("[0-9]*")) {
                if (!txt_mesaid.getValue().isEmpty()) {
                    if (!txt_mesaid.getValue().matches("[0-9]*")) {
                        Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_mesaid.setValue("");
                                    txt_mesades.setValue("");
                                    txt_mesaid.focus();
                                }
                            }
                        });
                    } else {
                        Mesa objMesa;
                        int can_id = Integer.parseInt(txt_canid.getValue());
                        int mes_id = Integer.parseInt(txt_mesaid.getValue());
                        objMesa = objDaoMesas.busMesaxCanal(can_id, mes_id);
                        if (objMesa == null) {
                            Messagebox.show("Número de mesa no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_mesaid.setValue("");
                                        txt_mesades.setValue("");
                                        txt_mesaid.focus();
                                    }
                                }
                            });
                        } else {
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Mesa con codigo " + txt_mesaid.getValue() + " y ha encontrado 1 registro(s)");
                            txt_mesaid.setValue(Utilitarios.lpad(String.valueOf(objMesa.getMes_key()), 2, "0"));
                            txt_mesades.setValue(objMesa.getSup_apenom());
                        }
                    }
                } else {
                    txt_mesaid.setValue("");
                    txt_mesades.setValue("");
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#sp_corid")
    public void validaCorrelativo() {
        txt_rutnomrep.focus();
    }

    @Listen("onOK=#txt_rutnomrep")
    public void validaOrden() throws SQLException {
        sp_rutord.focus();
    }

    @Listen("onCheck=#chk_rutest")
    public void VerificaEstado() throws SQLException {
        int i_rutest = objRuta.getRut_est();
        if (objDaoRutas.busquedaExistencia(1, (txt_canid.getValue() + txt_mesaid.getValue() + Utilitarios.lpad(String.valueOf(sp_corid.getValue()), 2, "0"))) > 0) {
            if (i_rutest == 1) {
                chk_rutest.setChecked(true);
                chk_rutest.setLabel("ACTIVO");
                Messagebox.show("La ruta está en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                chk_rutest.setChecked(false);
                chk_rutest.setLabel("INACTIVO");
                Messagebox.show("Ya no se puede activar la ruta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    //Eventos Otros
    public String verificar() {
        String verifica;
        if (txt_canid.getValue().isEmpty()) {
            verifica = "El campo Codigo de Canal es obligatorio";
            campo = "Codigo de Canal";
        } else if (txt_mesaid.getValue().isEmpty()) {
            verifica = "El campo Nº de Mesa es obligatorio";
            campo = "codigo de Mesa";
        } else if (sp_corid.getValue() == null) {
            verifica = "El campo Correlativo de la Ruta es obligatorio";
            campo = "Correlativo de la Ruta";
        } else if (sp_corid.getValue() == 0) {
            verifica = "El campo Correlativo de la Ruta es obligatorio";
            campo = "Correlativo de la Ruta";
        } else if (sp_rutord.getValue() == null) {
            verifica = "Orden de la Ruta";
            campo = "El campo Orden de la Ruta ";
        } else {
            verifica = "";
        }
        return verifica;
    }
    
    public void validafocos(){
    	if (campo.equals("Codigo de Canal")) {
            txt_canid.focus();
        } else if (campo.equals("codigo de Mesa")) {
            txt_mesaid.focus();
        } else if (campo.equals("Correlativo de la Ruta")) {
            sp_corid.focus();
        } else if (campo.equals("Orden de la Ruta")) {
            sp_rutord.focus();
        }
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setText("%%");
    }

    public void limpiarLista() {
        //limpio mi lista
        objRuta = null;
        objlstRutas = new ListModelList<Ruta>();
        lst_rutas.setModel(objlstRutas);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarutas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarutas.setDisabled(b_valida1);
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

    public void limpiarCampos() {
        txt_canid.setValue(null);
        txt_mesades.setValue("");
        txt_mesaid.setValue("");
        txt_candes.setValue("");
        txt_mesades.setValue("");
        txt_rutnomrep.setValue("");
        sp_corid.setValue(null);
        sp_rutord.setValue(null);
        txt_rutnomrep.setValue("");
        sp_rutord.setValue(0);
    }

    public Object generaRegistro() {
        int i_rutkey = objRuta.getRut_key();
        String s_rutid = objRuta.getRut_id();
        int i_rutest;
        if (chk_rutest.isChecked()) {
            i_rutest = 1;
        } else {
            i_rutest = 2;
        }
        int i_canid = Integer.parseInt(txt_canid.getValue());
        int i_mesid = Integer.parseInt(txt_mesaid.getValue());
        int i_rutcorid = sp_corid.getValue();
        int i_rutord = sp_rutord.getValue();
        String c_nomrep = txt_rutnomrep.getValue().toUpperCase();
        String c_usuadd = objUsuCredential.getCuenta();
        String c_usumod = objUsuCredential.getCuenta();
        int i_empid = objUsuCredential.getCodemp();
        int i_sucid = objUsuCredential.getCodsuc();
        return new Ruta(i_rutkey, s_rutid, i_sucid, i_empid, i_rutest, i_canid, i_mesid, i_rutcorid, c_usuadd,
                c_usumod, i_rutord, c_nomrep);
    }

    public void llenarCampos() {
        txt_canid.setValue(objRuta.getCan_id());
        txt_candes.setValue(objRuta.getRut_canaldes());
        txt_mesaid.setValue(objRuta.getMes_id());
        txt_mesades.setValue(objRuta.getRut_supapenom());
        chk_rutest.setChecked(objRuta.isValor());
        chk_rutest.setLabel(objRuta.isValor() ? "ACTIVO" : "INACTIVO");
        sp_corid.setValue(objRuta.getRut_corid());
        sp_rutord.setValue(objRuta.getRut_ord());
        txt_rutnomrep.setValue(objRuta.getRut_nomrep());
        txt_usuadd.setValue(objRuta.getRut_usuadd());
        d_fecadd.setValue(objRuta.getRut_fecadd());
        txt_usumod.setValue(objRuta.getRut_usumod());
        d_fecmod.setValue(objRuta.getRut_fecmod());
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_rutest.setDisabled(b_valida1);
        txt_canid.setDisabled(b_valida1);
        txt_candes.setDisabled(b_valida1);
        txt_mesaid.setDisabled(b_valida1);
        txt_mesades.setDisabled(b_valida1);
        sp_rutord.setDisabled(b_valida1);
        sp_corid.setDisabled(b_valida1);
        txt_rutnomrep.setDisabled(b_valida1);
    }
}
