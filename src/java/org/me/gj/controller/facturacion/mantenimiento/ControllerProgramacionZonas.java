package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.DaoHorario;
import org.me.gj.controller.distribucion.mantenimiento.DaoVehiculo;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.facturacion.mantenimiento.ProgramacionZona;
import org.me.gj.model.facturacion.mantenimiento.Zona;
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

public class ControllerProgramacionZonas extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaprogzonas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_zonid, txt_zondes, txt_horentdes, txt_transdes, txt_glosa, txt_progzonnomrep, txt_busqueda, txt_horentid, txt_transid, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_busest;
    @Wire
    Spinner sp_progzonord;
    @Wire
    Checkbox chk_progzonest, chk_busest;
    @Wire
    Listbox lst_progzonas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancia de Objetos
    ProgramacionZona objProgramacionZona = new ProgramacionZona();
    ListModelList<ProgramacionZona> objlstProgramacionZonas = new ListModelList<ProgramacionZona>();
    DaoProgramacionZonas objDaoProgramacionZonas = new DaoProgramacionZonas();
    DaoHorario objDaoHorarios = new DaoHorario();
    DaoZonas objDaoZonas = new DaoZonas();
    DaoVehiculo objDaoVehiculo = new DaoVehiculo();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    String s_estado = "Q", s_mensaje, modoEjecucion = "mantProgZonas";
    String campo = "";
    public static boolean bandera = false;
    //Variables de Session    
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProgramacionZonas.class);

    //Evebtos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_progzonas")
    public void llenaRegistros() throws SQLException {
        objlstProgramacionZonas = objDaoProgramacionZonas.listaProgramacionZona(1);
        lst_progzonas.setModel(objlstProgramacionZonas);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40104000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Programacion de Zonas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Programacion de Zonas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Programacion de Zonas");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Programacion de Zonas");
        }
        /*if (objAccesos.getAcc_mod() == 1) {
         tbbtn_btn_editar.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un canal");
         } else {
         tbbtn_btn_editar.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un canal");
         }*/
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Programacion de Zonas");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Programacion de Zonas");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Programacion de Zonas");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Programacion de Zonas");
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

        objlstProgramacionZonas = new ListModelList<ProgramacionZona>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de Zona " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de Zona" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de transporte" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el alias del transporte" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del horario de entrega " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion del horario de entrega " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la glosa de la programacion de zona " + s_consulta + " para su busqueda");
        }
        objlstProgramacionZonas = objDaoProgramacionZonas.busquedaProgramacionZonas(i_seleccion, s_consulta, i_estado);
        
        if (objlstProgramacionZonas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstProgramacionZonas.getSize() + " registro(s)");
        }
        
        if (objlstProgramacionZonas.getSize() > 0) {
            lst_progzonas.setModel(objlstProgramacionZonas);
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

    @Listen("onSelect=#lst_progzonas")
    public void seleccionaRegistro() throws SQLException {
        objProgramacionZona = (ProgramacionZona) lst_progzonas.getSelectedItem().getValue();
        if (objProgramacionZona == null) {
            objProgramacionZona = objlstProgramacionZonas.get(i_sel + 1);
        }
        i_sel = lst_progzonas.getSelectedIndex();
        limpiarCampos();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProgramacionZona.getProg_zonid());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objProgramacionZona = new ProgramacionZona();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_zondes.setDisabled(true);
        txt_horentdes.setDisabled(true);
        txt_transdes.setDisabled(true);
        chk_progzonest.setDisabled(true);
        txt_zonid.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
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
            if (!txt_zonid.getValue().matches("[0-9]*") || !txt_horentid.getValue().matches("[0-9]*")
                    || !txt_transid.getValue().matches("[0-9]*")) {
                lst_progzonas.focus();
            } else {
                Horario objHorario = objDaoHorarios.buscaHorarioxCodigo(String.valueOf(Integer.parseInt(txt_horentid.getValue())));
                Vehiculo objVehiculo = objDaoVehiculo.buscarVehiculoxCodigo(txt_transid.getValue());
                Zona objZona = objDaoZonas.buscaZonaxCodigo(txt_zonid.getValue());
                if (objZona == null || objHorario == null || objVehiculo == null) {
                    lst_progzonas.focus();
                } else {
                    s_mensaje = "Esta Seguro que desea guardar los cambios?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {

                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        objlstProgramacionZonas = new ListModelList<ProgramacionZona>();
                                        objProgramacionZona = generaRegistro();
                                        ParametrosSalida objParamSalida;
                                        objParamSalida = objDaoProgramacionZonas.insertarProgramacionZona(objProgramacionZona);
                                        if (objParamSalida.getFlagIndicador() == 0) {
                                            //validacion de guardar/nuevo
                                            VerificarTransacciones();
                                            tbbtn_btn_guardar.setDisabled(true);
                                            tbbtn_btn_deshacer.setDisabled(true);
                                            //
                                            habilitaTab(false, false);
                                            seleccionaTab(true, false);
                                            habilitaCampos(true);
                                            limpiarCampos();
                                            objlstProgramacionZonas = objDaoProgramacionZonas.listaProgramacionZona(1);
                                            lst_progzonas.setModel(objlstProgramacionZonas);
                                            objProgramacionZona = new ProgramacionZona();
                                        }
                                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            });
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_progzonas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_progzonas.focus();
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta programacion de zona?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoProgramacionZonas.eliminarProgramacionZona(objProgramacionZona);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstProgramacionZonas.clear();
                                    objlstProgramacionZonas = objDaoProgramacionZonas.listaProgramacionZona(1);
                                    lst_progzonas.setModel(objlstProgramacionZonas);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
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
                            OnChange();
                            limpiaAuditoria();
                            lst_progzonas.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //

                            habilitaCampos(true);
                            lst_progzonas.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstProgramacionZonas == null || objlstProgramacionZonas.isEmpty()) {
            Messagebox.show("No hay registros de programación de zonas para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_progzonas.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionProgZona.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manprogzon")
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
            objlstProgramacionZonas = new ListModelList<ProgramacionZona>();
            objlstProgramacionZonas = objDaoProgramacionZonas.listaProgramacionZona(0);
            lst_progzonas.setModel(objlstProgramacionZonas);
        }
    }

    @Listen("onOK=#txt_zonid")
    public void lovZonas() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_zonid.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                objLovZonas.put("txt_zonid", txt_zonid);
                objLovZonas.put("txt_zondes", txt_zondes);
                objLovZonas.put("txt_horentid", txt_horentid);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerProgramacionZonas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovZonas.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_horentid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_zonid")
    public void validaZona() throws SQLException {
        if (!txt_zonid.getValue().isEmpty()) {
            txt_zondes.setValue("");
            if (!txt_zonid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_zonid.setValue("");
                            txt_zondes.setValue("");
                            txt_zonid.focus();

                        }
                    }
                });
            } else if (txt_zonid.getValue().length() < 8) {
                Messagebox.show("el codigo ingresado debe ser de 8 digitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_zonid.setValue("");
                            txt_zondes.setValue("");
                            txt_zonid.focus();

                        }
                    }
                });
            } else {
                Zona objZona = objDaoZonas.buscaZonaxCodigo(txt_zonid.getValue());
                if (objZona == null) {
                    Messagebox.show("Zona no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_zonid.setValue("");
                                txt_zondes.setValue("");
                                txt_zonid.focus();

                            }
                        }
                    });

                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Zona con codigo " + objZona.getZon_id() + " y ha encontrado 1 registro(s)");
                    txt_zonid.setValue(objZona.getZon_id());
                    txt_zondes.setValue(objZona.getZon_des());
                }
            }
        } else {
            txt_zonid.setValue("");
            txt_zondes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_horentid")
    public void lovHorEnt() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_horentid.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                objLovZonas.put("txt_horentid", txt_horentid);
                objLovZonas.put("txt_horentdes", txt_horentdes);
                objLovZonas.put("txt_transid", txt_transid);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerProgramacionZonas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorario.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_transid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_horentid")
    public void validaHorario() throws SQLException {
        if (!txt_horentid.getValue().isEmpty()) {
            txt_horentdes.setValue("");
            if (!txt_horentid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws
                            Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_horentid.setValue("");
                                    txt_horentdes.setValue("");
                                    txt_horentid.focus();
                                }
                            }
                        });
            } else {
                Horario objHorario = objDaoHorarios.buscaHorarioxCodigo(String.valueOf(Integer.parseInt(txt_horentid.getValue())));
                if (objHorario == null) {
                    Messagebox.show("El Horario no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws
                                Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_horentid.setValue("");
                                        txt_horentdes.setValue("");
                                        txt_horentid.focus();
                                    }
                                }
                            });

                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del horario con codigo " + objHorario.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_horentid.setValue(Utilitarios.lpad(objHorario.getTab_subdir(), 3, "0"));
                    txt_horentdes.setValue(objHorario.getTab_subdes());

                }
            }
        } else {
            txt_horentid.setValue("");
            txt_horentdes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_transid")
    public void lovTransid() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_transid.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                objLovZonas.put("txt_transid", txt_transid);
                objLovZonas.put("txt_transdes", txt_transdes);
                objLovZonas.put("txt_glosa", txt_glosa);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerProgramacionZonas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTransportes.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_glosa.focus();
            }
        }
    }

    @Listen("onBlur=#txt_transid")
    public void validaTransporte() throws SQLException {
        if (!txt_transid.getValue().isEmpty()) {
            txt_transdes.setValue("");
            if (!txt_transid.getValue().matches("[0-9]*")) {

                Messagebox.show("Por Favor Ingrese Valores Numericos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws
                            Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_transid.setValue("");
                                    txt_transdes.setValue("");
                                    txt_transid.focus();
                                }
                            }
                        });

            } else {
                Vehiculo objVehiculo = objDaoVehiculo.buscarVehiculoxCodigo(txt_transid.getValue());
                if (objVehiculo == null) {

                    Messagebox.show("El Vehiculo no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws
                                Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_transid.setValue("");
                                        txt_transdes.setValue("");
                                        txt_transid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vehiculo con Codigo " + objVehiculo.getTrans_id() + " y ha encontrado 1 registro(s)");
                    txt_transid.setValue(Utilitarios.lpad(objVehiculo.getTrans_id(), 4, "0"));
                    txt_transdes.setValue(objVehiculo.getTrans_alias());
                }
            }
        } else {
            txt_transid.setValue("");
            txt_transdes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_glosa")
    public void onOKGlosa() {
        txt_progzonnomrep.focus();
    }

    @Listen("onOK=#txt_progzonnomrep")
    public void onOKReporte() {
        sp_progzonord.focus();
    }

    //Eventos Otros
    public String verificar() {
        String verifica;
        if (txt_zonid.getValue().isEmpty()) {
            verifica = "El campo Zona es obligatorio";
            campo = "Zona";
        } else if (txt_horentid.getValue().isEmpty()) {
            verifica = "El campo Horario de entrega es obligatorio";
            campo = "Hora";
        } else if (txt_transid.getValue().isEmpty()) {
            verifica = "El campo Transporte es obligatorio";
            campo = "Tran";
        } else if (sp_progzonord.getValue() == null) {
            verifica = "El campo Orden es obligatorio";
            campo = "Orden";
        } else {
            verifica = "";
        }
        return verifica;
    }
    
    public void validafocos(){
    	if (campo.equals("Zona")) {
            txt_zonid.focus();
        } else if (campo.equals("Hora")) {
            txt_horentid.focus();
        } else if (campo.equals("Tran")) {
            txt_transid.focus();
        } else if (campo.equals("Orden")) {
            sp_progzonord.focus();
        }
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setText("%%");
    }

    public void limpiarCampos() {
        chk_progzonest.setLabel("ACTIVO");
        chk_progzonest.setChecked(true);
        txt_zonid.setValue("");
        txt_zondes.setValue("");
        txt_horentid.setValue("");
        txt_horentdes.setValue("");
        txt_transid.setValue(null);
        txt_transdes.setValue("");
        txt_glosa.setValue("");
        txt_progzonnomrep.setValue("");
        sp_progzonord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objProgramacionZona = null;
        objlstProgramacionZonas = new ListModelList<ProgramacionZona>();
        lst_progzonas.setModel(objlstProgramacionZonas);
    }

    public ProgramacionZona generaRegistro() {
        int prog_key = objProgramacionZona.getProg_key();
        String prog_zonid = txt_zonid.getValue();
        int suc_id = objUsuCredential.getCodsuc();
        int emp_id = objUsuCredential.getCodemp();
        String prog_transid = txt_transid.getValue();
        int prog_trans = Integer.parseInt(txt_transid.getValue());
        String prog_horentid = txt_horentid.getValue();
        String prog_glosa = txt_glosa.getValue().toUpperCase();
        int prog_est = (chk_progzonest.isChecked()) ? 1 : 2;
        String prog_usuadd = objUsuCredential.getCuenta();
        String prog_usumod = objUsuCredential.getCuenta();
        int prog_ord = sp_progzonord.getValue();
        String prog_nomrep = txt_progzonnomrep.getValue().toUpperCase();

        return new ProgramacionZona(prog_key, prog_zonid, suc_id, emp_id, prog_trans, prog_transid, prog_horentid,
                prog_glosa, prog_est, prog_usuadd, prog_usumod, prog_ord, prog_nomrep);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogzonas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogzonas.setDisabled(b_valida1);
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
        chk_progzonest.setChecked(objProgramacionZona.isValor());
        chk_progzonest.setLabel((objProgramacionZona.getProg_est() == 1) ? "ACTIVO" : "INACTIVO");
        txt_zonid.setValue(objProgramacionZona.getProg_zonid());
        txt_zondes.setValue(objProgramacionZona.getProg_zondes());
        txt_horentid.setValue(objProgramacionZona.getProg_horentid());
        txt_horentdes.setValue(objProgramacionZona.getProg_horentdes());
        txt_transid.setValue(objProgramacionZona.getProg_transid());
        txt_transdes.setValue(objProgramacionZona.getProg_transalias());
        txt_glosa.setValue(objProgramacionZona.getProg_glosa());
        txt_progzonnomrep.setValue(objProgramacionZona.getProg_nomrep());
        sp_progzonord.setValue(objProgramacionZona.getProg_ord());
        txt_usuadd.setValue(objProgramacionZona.getProg_usuadd());
        d_fecadd.setValue(objProgramacionZona.getProg_fecadd());
        txt_usumod.setValue(objProgramacionZona.getProg_usumod());
        d_fecmod.setValue(objProgramacionZona.getProg_fecmod());
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_progzonest.setDisabled(b_valida1);
        txt_zonid.setDisabled(b_valida1);
        txt_zondes.setDisabled(b_valida1);
        txt_horentid.setDisabled(b_valida1);
        txt_horentdes.setDisabled(b_valida1);
        txt_transid.setDisabled(b_valida1);
        txt_transdes.setDisabled(b_valida1);
        txt_glosa.setDisabled(b_valida1);
        txt_progzonnomrep.setDisabled(b_valida1);
        sp_progzonord.setDisabled(b_valida1);
    }

    //metodos sin utilizar
    public void botonEditar() throws SQLException {

    }

}
