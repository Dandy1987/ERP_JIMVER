package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoZonas;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.distribucion.mantenimiento.ProgramacionRutas;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.facturacion.mantenimiento.Zona;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerProgramacionRutas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaprogrutas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Textbox txt_progrutaid, txt_transid, txt_transdes, txt_horid, txt_hordes, txt_zonid, txt_zondes,
            txt_glosa, /*txt_dvisid, txt_dvisdes,*/ txt_usuadd, txt_usumod, txt_busqueda;
    @Wire
    Combobox cb_busqueda, cb_busest, cb_dvisita;
    @Wire
    Checkbox chk_estado;
    @Wire
    Listbox lst_progrutas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<ProgramacionRutas> objlstProgramacionRutas;
    DaoProgramacionRutas objDaoProgramacionRutas;
    ProgramacionRutas objProgramacionRutas;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    //Variables Publicas
    String s_estado = "Q", campo = "";
    String s_diavis = "", s_zonid = "", s_transid = "", s_horid = "";
    String s_campo = "";
    int i_sel;
    int valor;
    int emp_id;
    int suc_id;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProgramacionRutas.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_progrutas")
    public void llenaRegistros() throws SQLException {
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objProgramacionRutas = new ProgramacionRutas();
        objlstProgramacionRutas = new ListModelList<ProgramacionRutas>();
        objDaoProgramacionRutas = new DaoProgramacionRutas();

        objlstProgramacionRutas = objDaoProgramacionRutas.listaProgramacionRutas(1);
        if (objlstProgramacionRutas != null) {
            lst_progrutas.setModel(objlstProgramacionRutas);
        }

        cb_busest.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30108000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Programacion Rutas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Programacion Rutas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creacion de una nueva Programacion Rutas");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creacion de una nueva Programacion Rutas");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edicion de una Programacion Rutas");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edicion de una Programacion Rutas");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminacion de una Programacion Rutas");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminacion de una Programacion Rutas");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de una Programacion Rutas");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de una Programacion Rutas");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarlista();

        int seleccion = 0, estado;
        int index_est = cb_busest.getSelectedIndex();
        int index_sel = cb_busqueda.getSelectedIndex();
        String s_consulta = txt_busqueda.getValue().isEmpty() ? "%%" : txt_busqueda.getValue().toUpperCase()/*.replace("0", "")*/.trim();

        if (index_est == 0) {
            estado = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha seleccionado el estado 'ACTIVO' para su busqueda");
        } else if (index_est == 1) {
            estado = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha seleccionado el estado 'INACTIVO' para su busqueda");
        } else {
            estado = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha seleccionado el estado 'TODOS' para su busqueda");
        }

        if (index_sel == 0) {
            seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (index_sel == 1) {
            seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (index_sel == 2) {
            seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion " + s_consulta + " para su busqueda");
        } else if (index_sel == 3) {
            seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion " + s_consulta + " para su busqueda");
        } else if (index_sel == 4) {
            seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion " + s_consulta + " para su busqueda");
        }

        objlstProgramacionRutas = objDaoProgramacionRutas.busquedaProgramacionRutas(emp_id, suc_id, seleccion, s_consulta, estado);
        // Validar tabla sin registro
        if (objlstProgramacionRutas.getSize() > 0) {
            lst_progrutas.setModel(objlstProgramacionRutas);
        } else {
            Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();
        lst_progrutas.focus();
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

    @Listen("onSelect=#lst_progrutas")
    public void seleccionaRegistro() throws SQLException {
        objProgramacionRutas = (ProgramacionRutas) lst_progrutas.getSelectedItem().getValue();
        if (objProgramacionRutas == null) {
            objProgramacionRutas = objlstProgramacionRutas.get(i_sel + 1);
        }
        i_sel = lst_progrutas.getSelectedIndex();
        llenarCampos(objProgramacionRutas);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProgramacionRutas.getProgruta_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_estado.setDisabled(true);
        chk_estado.setChecked(true);
        cb_dvisita.focus();
        cb_dvisita.select();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_progrutas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_progrutas.focus();
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            datosProgRutas();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws WrongValueException, NumberFormatException, SQLException {
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
            //guardarCambios();
            Messagebox.show("Está seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        ParametrosSalida objParametroSalida;
                        objProgramacionRutas = generaRegistro();
                        objParametroSalida = s_estado.equals("N") ? objDaoProgramacionRutas.insertarProgramacionRutas(objProgramacionRutas) : objDaoProgramacionRutas.actualizarProgramacionRutas(objProgramacionRutas);
                        if (objParametroSalida.getFlagIndicador() == 0) {
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            chk_estado.setDisabled(true);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaCampos(true);
                            limpiarCampos();
                            limpiaAuditoria();
                            busquedaRegistros();
                            lst_progrutas.focus();

                        }
                        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        lst_progrutas.focus();
                    }
                }
            });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_progrutas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Está seguro que desea eliminar la programación de ruta", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParametrosSalida;
                                objParametrosSalida = objDaoProgramacionRutas.eliminarProgramacionRutas(objProgramacionRutas);
                                if (objParametrosSalida.getFlagIndicador() == 0) {
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    busquedaRegistros();
                                }
                                Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_campo = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_campo, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            limpiaAuditoria();
                            lst_progrutas.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_progrutas.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstProgramacionRutas == null || objlstProgramacionRutas.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionProgRutas.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validaciones
    @Listen("onCtrlKey=#w_manprogrutas")
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

    @Listen("onSelect=#cb_dvisita")
    public void onSelectDiaVisita() {
        limpiarZona();
        limpiarHorario();
    }
    
    @Listen("onOK=#cb_dvisita")
     public void onOKdiavisita(){
         txt_zonid.focus();
     }
    
    

    /*
     @Listen("onOK=#txt_dvisid")
     public void lovDiaVisita() {
     if (bandera == false) {
     bandera = true;
     if (txt_dvisid.getValue().isEmpty()) {
     Map objMapObjetos = new HashMap();
     String modoEjecucion = "mantProgRutas";
     objMapObjetos.put("txt_dvisid", txt_dvisid);
     objMapObjetos.put("txt_dvisdes", txt_dvisdes);
     objMapObjetos.put("validaBusqueda", modoEjecucion);
     objMapObjetos.put("controlador", "ControllerProgramacionRutas");
     Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDiaVisita.zul", null, objMapObjetos);
     window.doModal();
     } else {
     txt_zonid.focus();
     }
     }
     }

     @Listen("onBlur=#txt_dvisid")
     public void validaDiaVisita() throws WrongValueException, SQLException {
     if (txt_dvisid.getValue().isEmpty()) {
     limpiarZona();
     limpiarHorario();
     limpiarDiaVisita();
     } else {
     if (!txt_dvisid.getValue().matches("[0-9]*")) {
     Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     limpiarDiaVisita();
     limpiarZona();
     limpiarHorario();
     txt_dvisid.focus();
     }
     }
     });
     } else {
     String s_diaid = txt_dvisid.getValue().replace("0", "").trim();
     Zona objDiaVisita = new DaoZonas().buscaDiaVisitaxCodigo(s_diaid);
     if (objDiaVisita == null) {
     Messagebox.show("El código no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     limpiarDiaVisita();
     limpiarZona();
     limpiarHorario();
     txt_dvisid.focus();
     }
     }
     });
     } else {
     txt_dvisid.setValue(String.valueOf(objDiaVisita.getZon_dvis()));
     txt_dvisdes.setValue(objDiaVisita.getZon_dvisdes());
     }
     }
     }
     bandera = false;
     }*/
    @Listen("onOK=#txt_horid")
    public void lovHorario() {
        if (bandera == false) {
            bandera = true;
            if (!txt_zonid.getValue().isEmpty()) {
                if (txt_horid.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    String modoEjecucion = "mantProgRutas";
                    objMapObjetos.put("txt_zonid", txt_zonid);
                    objMapObjetos.put("txt_horid", txt_horid);
                    objMapObjetos.put("txt_hordes", txt_hordes);
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerProgramacionRutas");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorario.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_glosa.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_horid")
    public void validaHorario() throws WrongValueException, SQLException {
        if (!txt_horid.getValue().isEmpty()) {
            /*if (txt_zonid.getValue().isEmpty()) {
                Messagebox.show("Ingrese zona", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarHorario();
                            txt_zonid.focus();
                        }
                    }
                });
            } else {*/
                if (txt_zonid.getValue().matches("[0-9]*")) {
                    /*Horario objHoraxZona = new DaoHorario().getHorarioxZona(txt_zonid.getValue());
                    if (objHoraxZona != null) {*/
                        if (!txt_horid.getValue().isEmpty()) {
                            if (!txt_horid.getValue().matches("[0-9]*")) {
                                Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            limpiarHorario();
                                            txt_horid.focus();
                                        }
                                    }
                                });
                            } else {
                                //Horario objHorario = new DaoHorario().getHorarioxZona(txt_dvisid.getValue(), txt_zonid.getValue(), Integer.parseInt(txt_horid.getValue()));
                                //Horario objHorario = new DaoHorario().getHorarioxZona(cb_dvisita.getSelectedItem().getValue().toString(), txt_zonid.getValue(), Integer.parseInt(txt_horid.getValue()));
                                Horario objHorario = new DaoHorario().getNomHorario(txt_horid.getValue());
                                if (objHorario == null) {
                                    Messagebox.show("El código no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                limpiarHorario();
                                                txt_horid.focus();
                                            }
                                        }
                                    });
                                } else {
                                    txt_horid.setValue(objHorario.getTab_subdir());
                                    txt_hordes.setValue(objHorario.getTab_subdes());
                                }
                            }
                        }
                    //}
                }
            //}
        } else {
            limpiarHorario();
        }
        bandera = false;
    }
    
    /*@Listen("onBlur=#txt_horid")
    public void validaHorario() {
        bandera = false;
    }*/

    @Listen("onOK=#txt_transid")
    public void lovTransporte() {
        if (bandera == false) {
            bandera = true;
            if (txt_transid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProgRutas";
                objMapObjetos.put("txt_transid", txt_transid);
                objMapObjetos.put("txt_transdes", txt_transdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProgramacionRutas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTransportes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_horid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_transid")
    public void validaTransporte() throws WrongValueException, SQLException {
        txt_transdes.setValue("");
        if (!txt_transid.getValue().isEmpty()) {
            if (!txt_transid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarTransporte();
                            txt_transid.focus();
                        }
                    }
                });
            } else {
                Vehiculo objVehiculo = new DaoVehiculo().buscarVehiculoxCodigo(txt_transid.getValue());
                if (objVehiculo == null) {
                    Messagebox.show("El código no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarTransporte();
                                txt_transid.focus();
                            }
                        }
                    });
                } else {
                    txt_transid.setValue(objVehiculo.getTrans_id());
                    txt_transdes.setValue(objVehiculo.getTrans_alias());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_zonid")
    public void lovZona() {
        if (bandera == false) {
            bandera = true;
            //if (!txt_dvisid.getValue().isEmpty()) {
            if (cb_dvisita.getSelectedIndex() != -1) {
                if (txt_zonid.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    String modoEjecucion = "mantProgRutas";
                    //objMapObjetos.put("txt_dvisid", txt_dvisid);
                    objMapObjetos.put("txt_dvisid", cb_dvisita.getSelectedItem().getValue().toString());
                    objMapObjetos.put("txt_zonid", txt_zonid);
                    objMapObjetos.put("txt_zondes", txt_zondes);
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerProgramacionRutas");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovZonas.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_transid.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_zonid")
    public void validaZona() throws WrongValueException, SQLException {
        if (!txt_zonid.getValue().isEmpty()) {
            //if (txt_dvisid.getValue().isEmpty()) {
            if (cb_dvisita.getSelectedIndex() == -1) {
                Messagebox.show("Ingrese día de visita", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarZona();
                            limpiarHorario();
                            //txt_dvisid.focus();
                        }
                    }
                });
            } else {
                //if (txt_dvisid.getValue().matches("[0-9]*")) {
                //Zona objDiaVisita = new DaoZonas().buscaDiaVisitaxCodigo(txt_dvisid.getValue().replace("0", "").trim());
                //if (objDiaVisita != null) {
                txt_zondes.setValue("");
                if (!txt_zonid.getValue().isEmpty()) {
                    if (!txt_zonid.getValue().matches("[0-9]*")) {
                        Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarZona();
                                    limpiarHorario();
                                    txt_zonid.focus();
                                }
                            }
                        });
                    } else {
                        //Zona objZona = new DaoZonas().buscaZonaxDiaVisita(txt_dvisid.getValue().replace("0", "").trim(), txt_zonid.getValue());
                        Zona objZona = new DaoZonas().buscaZonaxDiaVisita(cb_dvisita.getSelectedItem().getValue().toString(), txt_zonid.getValue());
                        if (objZona == null) {
                            Messagebox.show("El código no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        limpiarZona();
                                        limpiarHorario();
                                        txt_zonid.focus();
                                    }
                                }
                            });
                        } else {
                            txt_zonid.setValue(objZona.getZon_id());
                            txt_zondes.setValue(objZona.getZon_des());
                        }
                    }
                } else {
                    limpiarHorario();
                }
                //}
                //}
            }
        } else {
            limpiarZona();
            limpiarHorario();
        }
        bandera = false;
    }

    //Eventos Otros 
    public String verificar() {
        String mensaje;
        //if (txt_dvisid.getValue().isEmpty() || txt_dvisdes.getValue().isEmpty()) {
        if (cb_dvisita.getSelectedIndex() == -1) {
            campo = "diaid";
            mensaje = "El campo 'DIA VISITA' es obligatorio";
        } else if (txt_zonid.getValue().isEmpty() || txt_zondes.getValue().isEmpty()) {
            campo = "zonid";
            mensaje = "El campo 'ZONA' es obligatorio";
        } else if (txt_transid.getValue().isEmpty() || txt_transdes.getValue().isEmpty()) {
            campo = "transid";
            mensaje = "El campo 'TRANSPORTE' es obligatorio";
        } else if (txt_horid.getValue().isEmpty() || txt_hordes.getValue().isEmpty()) {
            campo = "horaid";
            mensaje = "El campo 'HORARIO' es obligatorio";
        } else if (txt_glosa.getValue().isEmpty()) {
            campo = "glosa";
            mensaje = "El campo 'GLOSA' es obligatorio";
        } else if (txt_glosa.getValue().matches("^\\s+")) {
            campo = "glosa";
            mensaje = "En el campo 'GLOSA' no debe ingresar espacios en blanco al principio ";
        } else {
            mensaje = "";
        }

        return mensaje;
    }

    public void validafocos() {
        if (campo.equals("diaid")) {
            //txt_dvisid.focus();
            cb_dvisita.focus();
        } else if (campo.equals("horaid")) {
            txt_horid.focus();
        } else if (campo.equals("transid")) {
            txt_transid.focus();
        } else if (campo.equals("zonid")) {
            txt_zonid.focus();
        } else if (campo.equals("glosa")) {
            txt_glosa.setValue("");
            txt_glosa.focus();
        }
    }

    /*public void guardarCambios() throws WrongValueException, NumberFormatException, SQLException {
     Zona objZona = new DaoZonas().buscaZonaxCodigo(txt_zonid.getValue());
     Vehiculo objVehiculo = new DaoVehiculo().buscarVehiculoxCodigo(txt_transid.getValue());
     Horario objHorario = new DaoHorario().buscaHorarioxCodigo(txt_horid.getValue().replace("0", ""));
     //Zona objDiaVisita = new DaoZonas().buscaDiaVisitaxCodigo(txt_dvisid.getValue().replace("0", "").trim());
     if (objVehiculo == null || objZona == null || objHorario == null) {
     lst_progrutas.focus();
     } else {
     Messagebox.show("Está seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     ParametrosSalida objParametroSalida;
     objProgramacionRutas = generaRegistro();
     objParametroSalida = s_estado.equals("N") ? objDaoProgramacionRutas.insertarProgramacionRutas(objProgramacionRutas) : objDaoProgramacionRutas.actualizarProgramacionRutas(objProgramacionRutas);
     if (objParametroSalida.getFlagIndicador() == 0) {
     VerificarTransacciones();
     tbbtn_btn_guardar.setDisabled(true);
     tbbtn_btn_deshacer.setDisabled(true);
     chk_estado.setDisabled(true);
     habilitaTab(false, false);
     seleccionaTab(true, false);
     habilitaCampos(true);
     limpiarCampos();
     limpiaAuditoria();
     busquedaRegistros();

     }
     Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     lst_progrutas.focus();
     }
     }
     });
     }
     }*/
    public ProgramacionRutas generaRegistro() {
        int progruta_key = txt_progrutaid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_progrutaid.getValue());
        String progruta_id = txt_progrutaid.getValue();
        //String progruta_dvisid = txt_dvisid.getValue();
        String progruta_dvisid = cb_dvisita.getSelectedItem().getValue().toString();
        String progruta_hentid = txt_horid.getValue();
        String progruta_transid = txt_transid.getValue();
        String progruta_zonid = txt_zonid.getValue();
        String progruta_glosa = txt_glosa.getValue().toUpperCase().trim();
        String s_valedit = validaEditar();
        int progruta_estado = chk_estado.isChecked() ? 1 : 2;

        String usu_add = objUsuCredential.getCuenta();
        String usu_mod = objUsuCredential.getCuenta();
        return new ProgramacionRutas(progruta_key, progruta_id, progruta_dvisid, progruta_hentid, progruta_transid,
                progruta_zonid, progruta_glosa, progruta_estado, usu_add, usu_mod, s_valedit);
    }

    private void datosProgRutas() {
        s_transid = txt_transid.getValue().isEmpty() ? "" : txt_transid.getValue();
        s_zonid = txt_zonid.getValue().isEmpty() ? "" : txt_zonid.getValue();
        s_diavis = cb_dvisita.getSelectedItem().getValue();
        s_horid = txt_horid.getValue().isEmpty() ? "" : txt_horid.getValue();
    }

    public String validaEditar() {
        String b_indi = "";
        if (txt_transid.getValue().equals(s_transid) && txt_zonid.getValue().equals(s_zonid)
                && cb_dvisita.getSelectedItem().getValue().equals(s_diavis) && txt_horid.getValue().equals(s_horid)) {
            b_indi = "NC";
        } else {
            b_indi = "C";
        }
        return b_indi;
    }

    public void limpiarCampos() {
        txt_progrutaid.setValue("");
        cb_dvisita.setSelectedIndex(-1);
        txt_horid.setValue("");
        txt_hordes.setValue("");
        txt_transid.setValue("");
        txt_transdes.setValue("");
        txt_zonid.setValue("");
        txt_zondes.setValue("");
        txt_glosa.setValue("");
        s_diavis = "";
        s_transid = "";
        s_zonid = "";
        s_horid = "";
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarHorario() {
        txt_horid.setValue("");
        txt_hordes.setValue("");
    }

    public void limpiarTransporte() {
        txt_transid.setValue("");
        txt_transdes.setValue("");
    }

    public void limpiarZona() {
        txt_zonid.setValue("");
        txt_zondes.setValue("");
    }

    public void limpiarDiaVisita() {
        cb_dvisita.setSelectedIndex(-1);
    }

    public void limpiarlista() {
        objlstProgramacionRutas = new ListModelList<ProgramacionRutas>();
        lst_progrutas.setModel(objlstProgramacionRutas);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogrutas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogrutas.setDisabled(b_valida1);
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

    public void habilitaCampos(boolean b_valida) {
        cb_dvisita.setDisabled(b_valida);
        txt_horid.setDisabled(b_valida);
        txt_transid.setDisabled(b_valida);
        txt_zonid.setDisabled(b_valida);
        txt_glosa.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
    }

    public void llenarCampos(ProgramacionRutas objProgramacionRutas) {
        txt_progrutaid.setValue(objProgramacionRutas.getProgruta_id());
        cb_dvisita.setSelectedIndex(Integer.parseInt(objProgramacionRutas.getProgruta_dvisid()) - 1);
        txt_horid.setValue(objProgramacionRutas.getProgruta_hentid());
        txt_hordes.setValue(objProgramacionRutas.getProgruta_hentdes());
        txt_transid.setValue(objProgramacionRutas.getProgruta_transid());
        txt_transdes.setValue(objProgramacionRutas.getProgruta_transdes());
        txt_zonid.setValue(objProgramacionRutas.getProgruta_zonid());
        txt_zondes.setValue(objProgramacionRutas.getProgruta_zondes());
        txt_glosa.setValue(objProgramacionRutas.getProgruta_glosa());
        txt_usuadd.setValue(objProgramacionRutas.getProgruta_usuadd());
        d_fecadd.setValue(objProgramacionRutas.getProgruta_fecadd());
        txt_usumod.setValue(objProgramacionRutas.getProgruta_usumod());
        d_fecmod.setValue(objProgramacionRutas.getProgruta_fecmod());
        chk_estado.setLabel(objProgramacionRutas.getProgruta_estado() == 1 ? "ACTIVO" : "INACTIVO");
        chk_estado.setChecked(objProgramacionRutas.getProgruta_estado() == 1);

    }

    public void llenarCampos() {

    }

    public void OnChange() {

    }

    public void validaBusqueda(InputEvent event) throws SQLException {

    }

}
