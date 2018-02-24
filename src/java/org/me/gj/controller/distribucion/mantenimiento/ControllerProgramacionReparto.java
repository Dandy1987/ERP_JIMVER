package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Chofer;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.distribucion.mantenimiento.ProgramacionReparto;
import org.me.gj.model.distribucion.mantenimiento.Repartidor;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
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

public class ControllerProgramacionReparto extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaprogreparto, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_progrepar;
    @Wire
    Textbox txt_progid, txt_transid, txt_transdes, txt_chofid, txt_chofdes, txt_repartid, txt_repartdes,
            txt_horid, txt_hordes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo, cb_estado;
    @Wire
    Checkbox chk_estado;
    @Wire
    Datebox d_fechprogreparto, d_fecmod, d_fecadd, d_busq_fecprog, d_fecduplicar, d_fecnueva;

    //Instancias de Objetos
    ListModelList<ManPer> objlstManPeriodos;
    ListModelList<ProgramacionReparto> objlstProgRepar;
    DaoAccesos objDaoAccesos;
    DaoManPer objDaoManPer;
    DaoProgramacionReparto objDaoProgRepar;
    Accesos objAccesos;
    ProgramacionReparto objProgRepar;
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "", campo = "";
    String s_transid = "", s_reparid = "", s_chofid = "", s_horid = "";
    int i_sel;
    int valor;
    int emp_id;
    int suc_id;
    public static boolean bandera = false;
    ParametrosSalida objParametroSalida;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProgramacionReparto.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_progreparto")
    public void llenaRegistros() throws SQLException {
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        objProgRepar = new ProgramacionReparto();
        objDaoProgRepar = new DaoProgramacionReparto();
        objDaoManPer = new DaoManPer();
        objlstProgRepar = new ListModelList<ProgramacionReparto>();
        //carga los periodos
        objlstManPeriodos = objDaoManPer.listaPeriodosActual(1, 11);
        cb_periodo.setModel(objlstManPeriodos);
        cb_periodo.setValue(periodo);
        Utilitarios.sumaFecha(d_fecnueva, new Date(), 1);

        d_fecduplicar.setValue(Utilitarios.RestaDias(d_fecnueva.getValue(), 7));
        //carga las prognacion reparto
        objlstProgRepar = objDaoProgRepar.listaProgramacionReparto(1);
        lst_progrepar.setModel(objlstProgRepar);
        lst_progrepar.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30105000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Mantenimiento de Programacion Reparto con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Programacion Reparto con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creación de un nuevo Programacion Reparto");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Programacion Reparto");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edición de Programacion Reparto");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edición de Programacion Reparto");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminación de Programacion Reparto");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminación de Programacion Reparto");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Programacion Reparto");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Programacion Reparto");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarlista();

        Date d_prog = d_busq_fecprog.getValue();
        String f_prog = d_prog == null ? "" : sdf.format(d_prog);
        String f_periodo = f_prog.equals("") ? "" : sdfm.format(d_prog);
        String periodo = cb_periodo.getValue();

        if (!(periodo.equals(f_periodo) || f_prog.equals(""))) {
            Messagebox.show("La fecha no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado la fecha " + f_prog + " que no pertenece al periodo seleccionado (" + periodo + ")");
        } else {
            objlstProgRepar = objDaoProgRepar.busquedaProgramacionReparto(emp_id, suc_id, periodo, f_prog);
            // Validar tabla sin registro
            if (objlstProgRepar.getSize() > 0) {
                lst_progrepar.setModel(objlstProgRepar);
            } else {
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiarCampos();
        limpiaAuditoria();
        lst_progrepar.focus();
    }

    @Listen("onSelect=#lst_progrepar")
    public void seleccionaRegistro() throws SQLException, ParseException {
        objProgRepar = (ProgramacionReparto) lst_progrepar.getSelectedItem().getValue();
        if (objProgRepar == null) {
            objProgRepar = objlstProgRepar.get(i_sel + 1);
        }
        i_sel = lst_progrepar.getSelectedIndex();
        limpiarCampos();
        llenarCampos(objProgRepar);
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
        txt_transid.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_progrepar.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            //chk_estado.setDisabled(true);
            //datosProgReparto();
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
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
            //guardarCambios();
            Messagebox.show("Está seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objProgRepar = generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParametroSalida = objDaoProgRepar.insertarProgramacionReparto(objProgRepar);
                                } else {
                                    objParametroSalida = objDaoProgRepar.actualizarProgramacionReparto(objProgRepar);
                                }
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
                                    //busquedaRegistros();
                                    objlstProgRepar = new ListModelList<ProgramacionReparto>();
                                    objlstProgRepar = objDaoProgRepar.listaProgramacionReparto(1);
                                    lst_progrepar.setModel(objlstProgRepar);
                                }
                                Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_progrepar.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_progrepar.focus();
        } else {
            Messagebox.show("Está seguro que desea eliminar esta programacion de reparto?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objParametroSalida = objDaoProgRepar.eliminarProgramacionReparto(objProgRepar);
                        if (objParametroSalida.getFlagIndicador() == 0) {
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarlista();
                            busquedaRegistros();
                        }
                        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        Messagebox.show("Está seguro que desea deshacer los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_listaprogreparto.setDisabled(false);
                            seleccionaTab(true, false);
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            chk_estado.setDisabled(true);
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha deshecho los cambios");
                            lst_progrepar.setSelectedIndex(-1);
                            lst_progrepar.focus();
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstProgRepar == null || objlstProgRepar.isEmpty()) {
            Messagebox.show("No hay Registros de Programacion de Reparto para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_progrepar.focus();
        /*} else if (lst_progrepar.getSelectedIndex() == -1) {
            Messagebox.show("Seleccione Registro de Programacion de Reparto para imprimir");*/
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
            objMapObjetos.put("progrep_key", objProgRepar.getProgrep_key());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionProgReparto.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onClick=#btn_duplicar")
    public void botonDuplicar() {
        String validaFechaDuplicar = validaFechaDuplicar();
        if (!validaFechaDuplicar.isEmpty()) {
            Messagebox.show(validaFechaDuplicar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("fecduplicar")) {
                            d_fecduplicar.focus();
                        } else if (campo.equals("fecnueva")) {
                            d_fecnueva.focus();
                        }
                    }
                }
            });
        } else {
            Messagebox.show("Está seguro que desea duplicar con esa fecha ?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        Date fecduplicar = d_fecduplicar.getValue();
                        Date fecnueva = d_fecnueva.getValue();
                        objParametroSalida = objDaoProgRepar.duplicarDia(fecduplicar, fecnueva);
                        if (objParametroSalida.getFlagIndicador() == 0) {
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarlista();
                            busquedaRegistros();
                        }
                        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            });
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manprogreparto")
    public void ctrl_teclado(Event event) throws SQLException {
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

    @Listen("onOK=#txt_transid")
    public void lovTransporte() {
        if (bandera == false) {
            bandera = true;
            if (txt_transid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProgReparto";
                objMapObjetos.put("txt_transid", txt_transid);
                objMapObjetos.put("txt_transdes", txt_transdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProgramacionReparto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTransportes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_chofid.focus();
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

    @Listen("onOK=#txt_chofid")
    public void lovChofer() {
        if (bandera == false) {
            bandera = true;
            if (txt_chofid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProgReparto";
                objMapObjetos.put("txt_chofid", txt_chofid);
                objMapObjetos.put("txt_chofdes", txt_chofdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProgramacionReparto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovChofer.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_repartid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_chofid")
    public void validaChofer() throws WrongValueException, SQLException {
        txt_chofdes.setValue("");
        if (!txt_chofid.getValue().isEmpty()) {
            if (!txt_chofid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarChofer();
                            txt_chofid.focus();
                        }
                    }
                });
            } else {
                int chofid = Integer.parseInt(txt_chofid.getValue());
                Chofer objChofer = new DaoChofer().busquedaChoferxCodigo(chofid);
                if (objChofer == null) {
                    Messagebox.show("El código no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarChofer();
                                txt_chofid.focus();
                            }
                        }
                    });
                } else {
                    txt_chofid.setValue(objChofer.getChof_id());
                    txt_chofdes.setValue(objChofer.getChof_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_repartid")
    public void lovRepartidor() {
        if (bandera == false) {
            bandera = true;
            if (txt_repartid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProgReparto";
                objMapObjetos.put("txt_repartid", txt_repartid);
                objMapObjetos.put("txt_repartdes", txt_repartdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProgramacionReparto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovRepartidor.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_horid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_repartid")
    public void validaRepartidor() throws WrongValueException, SQLException {
        txt_repartdes.setValue("");
        if (!txt_repartid.getValue().isEmpty()) {
            if (!txt_repartid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingresar valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarRepartidor();
                            txt_repartid.focus();
                        }
                    }
                });
            } else {
                int repartid = Integer.parseInt(txt_repartid.getValue());
                Repartidor objRepartidor = new DaoRepartidor().busquedaRepartidorxCodigo(repartid);
                if (objRepartidor == null) {
                    Messagebox.show("El código no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarRepartidor();
                                txt_repartid.focus();
                            }
                        }
                    });
                } else {
                    txt_repartid.setValue(objRepartidor.getRep_id());
                    txt_repartdes.setValue(objRepartidor.getS_nomcompleto());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_horid")
    public void lovHorario() {
        if (bandera == false) {
            bandera = true;
            if (txt_horid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "mantProgReparto";
                objMapObjetos.put("txt_horid", txt_horid);
                objMapObjetos.put("txt_hordes", txt_hordes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProgramacionReparto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorario.zul", null, objMapObjetos);
                window.doModal();
            } else {
                d_fechprogreparto.focus();
            }
        }
    }

    @Listen("onBlur=#txt_horid")
    public void validaHorario() throws WrongValueException, SQLException {
        txt_hordes.setValue("");
        if (!txt_horid.getValue().isEmpty()) {
            if (!txt_horid.getValue().matches("[0-9]*")) {
                Messagebox.show("Debe ingresar valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarHorario();
                            txt_horid.focus();
                        }
                    }
                });
            } else {
                s_horid = txt_horid.getValue().isEmpty() ? "%%" : txt_horid.getValue();
                Horario objHorario = new DaoHorario().buscaHorarioxCodigo(s_horid);
                if (objHorario == null) {
                    Messagebox.show("El codigo no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
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
        bandera = false;
    }

    //Eventos Otros 
    public String verificar() {
        s_mensaje = "";
        if (txt_transid.getValue().isEmpty() || txt_transdes.getValue().isEmpty()) {
            campo = "transid";
            s_mensaje = "El campo 'TRANSPORTE' es obligatorio";
        } else if (!txt_transid.getValue().matches("^[0-9]*")) {
            campo = "transid";
            s_mensaje = "Ingresar valores numéricos en campo 'TRANSPORTE'";
        } else if (txt_chofid.getValue().isEmpty() || txt_chofdes.getValue().isEmpty()) {
            campo = "chofid";
            s_mensaje = "El campo 'CHOFER' es obligatorio";
        } else if (!txt_chofid.getValue().matches("^[0-9]*")) {
            campo = "chofid";
            s_mensaje = "Ingresar valores numéricos en campo 'CHOFER'";
        } else if (txt_repartid.getValue().isEmpty() || txt_repartdes.getValue().isEmpty()) {
            campo = "repartid";
            s_mensaje = "El campo 'REPARTIDOR' es obligatorio";
        } else if (!txt_repartid.getValue().matches("^[0-9]*")) {
            campo = "repartid";
            s_mensaje = "Ingresar valores numéricos en campo 'REPARTIDOR'";
        } else if (txt_horid.getValue().isEmpty() || txt_hordes.getValue().isEmpty()) {
            campo = "horid";
            s_mensaje = "El campo 'HORARIO' es obligatorio";
        } else if (!txt_horid.getValue().matches("^[0-9]*")) {
            campo = "horid";
            s_mensaje = "Ingresar valores numéricos en campo 'HORARIO'";
        } else if (d_fechprogreparto.getValue() == null) {
            campo = "fecprog";
            s_mensaje = "El campo 'FECHA PROG.REPARTO' es obligatorio";
        } else {
            /*Calendar c = Utilitarios.DateCalendar(d_fechprogreparto.getValue());
             int nrodia = c.get(Calendar.DAY_OF_WEEK);
             if (nrodia == 1) {
             campo = "fecprog";
             s_mensaje = "No se puede programar el reparto los domingos ";
             } else {*/
            s_mensaje = "";
            //}
        }
        return s_mensaje;
    }

    public void validafocos() {
        if (campo.equals("transid")) {
            txt_transid.focus();
        } else if (campo.equals("chofid")) {
            txt_chofid.focus();
        } else if (campo.equals("repartid")) {
            txt_repartid.focus();
        } else if (campo.equals("horid")) {
            txt_horid.focus();
        } else if (campo.equals("fecprog")) {
            d_fechprogreparto.focus();
        }
    }

    public String validaFechaDuplicar() {
        s_mensaje = "";
        String compareFechas;
        if (d_fecduplicar.getValue() == null) {
            campo = "fecduplicar";
            s_mensaje = "Ingrese fecha en el campo 'FECHA DUPLICAR' ";
        } else if (d_fecnueva.getValue() == null) {
            campo = "fecnueva";
            s_mensaje = "Ingrese fecha en el campo 'FECHA INGRESADA' ";
        } else {
            Calendar calDiferencia = Calendar.getInstance();
            Calendar calNueva = Calendar.getInstance();
            calDiferencia.setTime(Utilitarios.sumaDias(d_fecduplicar.getValue(), 7));
            calNueva.setTime(d_fecnueva.getValue());
            compareFechas = Utilitarios.compareFechas(calDiferencia.getTime(), calNueva.getTime());
            if (!compareFechas.equals("=")) {
                s_mensaje = "La fecha del campo 'FECHA INGRESADA' debe ser mayor a una semana de la fecha a Duplicar (" + new SimpleDateFormat("dd/MM/yyyy").format(d_fecduplicar.getValue()) + ")";
            } else {
                s_mensaje = "";
            }
        }
        return s_mensaje;
    }

    /*public void datosProgReparto() {
     s_transid = txt_transid.getValue().isEmpty() ? "" : txt_transid.getValue();
     s_chofid = txt_chofid.getValue().isEmpty() ? "" : txt_chofid.getValue();
     s_reparid = txt_repartid.getValue().isEmpty() ? "" : txt_repartid.getValue();
     s_horid = txt_horid.getValue().isEmpty() ? "" : txt_horid.getValue();
     }

     public String validaEditar() {
     String b_indi;
     if (txt_transid.getValue().equals(s_transid) && txt_chofid.getValue().equals(s_chofid)
     && txt_repartid.getValue().equals(s_reparid) && txt_horid.getValue().equals(s_horid)) {
     b_indi = "NC";
     } else {
     b_indi = "C";
     }
     return b_indi;
     }*/
    public void guardarCambios() throws WrongValueException, NumberFormatException, SQLException {
        Repartidor objRepartidor = new DaoRepartidor().busquedaRepartidorxCodigo(Integer.parseInt(txt_repartid.getValue()));
        Vehiculo objVehiculo = new DaoVehiculo().buscarVehiculoxCodigo(txt_transid.getValue());
        Horario objHorario = new DaoHorario().buscaHorarioxCodigo(txt_horid.getValue().replace("0", ""));
        Chofer objChofer = new DaoChofer().busquedaChoferxCodigo(Integer.parseInt(txt_chofid.getValue()));
        if (objVehiculo == null || objChofer == null || objRepartidor == null || objHorario == null) {
            lst_progrepar.focus();
        } else {
            Messagebox.show("Esta Seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objProgRepar = generaRegistro();
                                objParametroSalida = s_estado.equals("N") ? objDaoProgRepar.insertarProgramacionReparto(objProgRepar) : objDaoProgRepar.actualizarProgramacionReparto(objProgRepar);
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
                            }
                        }
                    });
        }
    }

    public ProgramacionReparto generaRegistro() {
        int prog_key = txt_progid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_progid.getValue());
        String prog_id = txt_progid.getValue();
        String prog_transid = txt_transid.getValue();
        String prog_chofid = txt_chofid.getValue();
        String prog_repartid = txt_repartid.getValue();
        Date prog_fecha = d_fechprogreparto.getValue();
        int prog_estado = chk_estado.isChecked() ? 1 : 2;
        //String s_cambios = validaEditar();
        String prog_horaid = txt_horid.getValue();
        String prog_usuadd = objUsuarioCredential.getCuenta();
        String prog_usumod = objUsuarioCredential.getCuenta();

        return new ProgramacionReparto(prog_key, prog_id, prog_transid, prog_chofid, prog_repartid,
                prog_fecha, prog_estado, prog_horaid, prog_usuadd, prog_usumod);
    }

    public void llenarCampos(ProgramacionReparto objProgRep) throws ParseException {
        txt_progid.setValue(objProgRep.getProgrep_id());
        txt_transid.setValue(objProgRep.getProgrep_transid());
        txt_transdes.setValue(objProgRep.getProgrep_transdes());
        txt_chofid.setValue(objProgRep.getProgrep_chofid());
        txt_chofdes.setValue(objProgRep.getProgrep_chofdes());
        txt_repartid.setValue(objProgRep.getProgrep_repartid());
        txt_repartdes.setValue(objProgRep.getProgrep_repartdes());
        d_fechprogreparto.setValue(objProgRep.getProgrep_fecha());
        txt_horid.setValue(objProgRep.getProgrep_horaid());
        txt_hordes.setValue(objProgRep.getProgrep_horades());
        chk_estado.setChecked(objProgRep.getProgrep_estado() == 1);
        chk_estado.setLabel(chk_estado.isChecked() ? "ACTIVO" : "INACTIVO");
        txt_usuadd.setValue(objProgRep.getProgrep_usuadd());
        d_fecadd.setValue(objProgRep.getProgrep_fecadd());
        txt_usumod.setValue(objProgRep.getProgrep_usumod());
        d_fecmod.setValue(objProgRep.getProgrep_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogreparto.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprogreparto.setDisabled(b_valida1);
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

    public void habilitaCampos(boolean b_valida1) {
        chk_estado.setDisabled(b_valida1);
        txt_transid.setDisabled(b_valida1);
        txt_chofid.setDisabled(b_valida1);
        txt_repartid.setDisabled(b_valida1);
        txt_horid.setDisabled(b_valida1);
        d_fechprogreparto.setDisabled(b_valida1);
    }

    public void limpiarCampos() {
        txt_progid.setValue("");
        txt_transid.setValue("");
        txt_transdes.setValue("");
        txt_chofid.setValue("");
        txt_chofdes.setValue("");
        txt_repartid.setValue("");
        txt_repartdes.setValue("");
        d_fechprogreparto.setValue(null);
        txt_horid.setValue("");
        txt_hordes.setValue("");
        chk_estado.setChecked(true);

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

    public void limpiarChofer() {
        txt_chofid.setValue("");
        txt_chofdes.setValue("");
    }

    public void limpiarRepartidor() {
        txt_repartid.setValue("");
        txt_repartdes.setValue("");
    }

    public void limpiarlista() {
        objlstProgRepar = new ListModelList<ProgramacionReparto>();
        lst_progrepar.setModel(objlstProgRepar);
    }

    // Metodos sin utilizar
    public void llenarCampos() throws ParseException {
    }

    public void OnChange() {
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
    }
}
