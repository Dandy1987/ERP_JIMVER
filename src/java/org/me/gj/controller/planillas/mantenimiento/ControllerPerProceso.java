package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.InterfaceControllers;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.Util;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author acaro
 */
public class ControllerPerProceso extends SelectorComposer<Component> implements InterfaceControllers {

    // Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_tipPeriodo, cb_tipPla;
    @Wire
    Button btn_consultar, btn_ajussp;
    @Wire
    Listbox lst_perProc;
    @Wire
    Textbox txt_usuAdd, txt_usuMod, txt_periodo, txt_desc, txt_situ;
    @Wire
    Datebox dt_fecAdd, dt_fecMod, dt_fecInicial, dt_fecFinal;
    @Wire
    Checkbox chk_act;

    //Instancias de Objetos
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    DaoPerPago objDaoPerPago;
    PerPago objPerPago;
    Util objTipPeriodo;

    ListModelList<Util> objListTipPeriodo;
    ListModelList<PerPago> objListPerPago;
    //Variables Públicas
    String s_estado = "Q";
    int i_sel;
    int i_empId;
    int i_sucId;
    String s_mensaje;
    String foco;
    String s_mes = "";
    String codTipPlanilla = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    //Variables de Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerPerProceso.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_perProc")
    public void llenarRegistros(Event e) throws SQLException {
        objDaoPerPago = new DaoPerPago();
        cb_tipPeriodo.setModel(cargarComboTipoPlanilla());
        cb_tipPla.setModel(cargarComboTipoPlanilla());
        ListModelList modelo = (ListModelList) cb_tipPla.getModel();
        modelo.clearSelection();
        modelo.addToSelection(cargarComboTipoPlanilla().get(3));
        objListPerPago = new DaoPerPago().busquedaPeriodoPago("03"); //Planilla NORMAL
        lst_perProc.setModel(objListPerPago);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Periodo Proceso con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Periodo Proceso con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Periodo Proceso");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Periodo Proceso");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Periodo Proceso");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Periodo Proceso");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Periodo Proceso");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Periodo Proceso");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void consultaRegistros() throws SQLException {
        String s_tipPeri = cb_tipPeriodo.getValue().trim();
        int s_indexTipPeri = cb_tipPeriodo.getSelectedItem().getIndex();
        String s_consulta = s_indexTipPeri != 0 ? s_tipPeri.substring(0, 2) : "";
        objListPerPago = new DaoPerPago().busquedaPeriodoPago(s_consulta);
        lst_perProc.setModel(objListPerPago);
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_perProc")
    public void seleccionaRegistro() throws SQLException {
        if (lst_perProc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor, seleccione un periodo de Procesos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objPerPago = new PerPago();
            objPerPago = lst_perProc.getSelectedItem().getValue();
            if (objPerPago == null) {
                objPerPago = objListPerPago.get(i_sel + 1);
            }
            i_sel = lst_perProc.getSelectedIndex();
            llenarCampos();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objPerPago.getPerPag_id());
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        objPerPago = new PerPago();
        limpiarCampos();
        seleccionaTab(false, true);
        habilitaBotones(true, false);
        habilitaCampos(false);
        habilitaTab(true, false);
        chk_act.setChecked(true);
        chk_act.setDisabled(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificarCampos();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        colocarFoco();
                    }
                }
            });
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objPerPago = (PerPago) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoPerPago.insertarPerPago(objPerPago);
                                } else {
                                    objParamSalida = objDaoPerPago.editarPerPago(objPerPago);
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    s_estado = "G";
                                    objListPerPago = new ListModelList<PerPago>();
                                    //Validación de Guardar/Nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    consultaRegistros();
                                    //
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }
    /*
     @Listen("onBlur=#txt_periodo")
     public void onBlur_txtPeriodo() throws SQLException {
     if (txt_periodo.getValue().isEmpty()) {
     txt_desc.setValue("");
     dt_fecInicial.setValue(null);
     dt_fecFinal.setValue(null);
     } else {
     verificarCamposPerProceso();
     }
     }
     */

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_perProc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor, seleccione un periodo de procesos", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(true);
            cb_tipPeriodo.focus();
            LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | pulso la opcion modificar para actualizar un registro").toString());
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
                            s_estado = "D";
                            limpiarCampos();
                            limpiaAuditoria();
                            lst_perProc.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //Validación de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_perProc == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionPeriodos.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onSelect=#cb_tipPla")
    public void onSelectSerie() throws SQLException {
        txt_periodo.setValue("");
        txt_desc.setValue("");
        txt_desc.setValue("");
        dt_fecInicial.setValue(null);
        dt_fecFinal.setValue(null);
        String valorC = cb_tipPla.getValue();
        if (!valorC.isEmpty()) {
            txt_periodo.focus();
        }
    }

    @Listen("onCtrlKey=#w_perProc")
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
                    //botonImprimir();
                }
                break;
        }
    }

    //@Listen("onOK=#txt_periodo")
    @Listen("onOK=#txt_periodo")
    public void onNextDescripcion() throws SQLException {
        txt_desc.focus();
    }

    @Listen("onBlur=#txt_periodo")
    public void onBlurDescripcion() throws SQLException {
        if (!cb_tipPla.getValue().isEmpty()) {
            codTipPlanilla = cb_tipPla.getValue().substring(0, 2).trim();
        } else {
            Messagebox.show("Por favor seleccione en el campo Tipo de Planilla", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            cb_tipPla.focus();
        }
        if (!codTipPlanilla.isEmpty()) {
            if (txt_periodo.getValue().length() == 6) {
                s_mes = txt_periodo.getValue().substring(4, 6);
            }
            if (s_mes != null) {
                if (Utilitarios.isMes(s_mes) == false) {
                    Messagebox.show("El Campo Período, es incorrecto.\n Ejemplo : " + Utilitarios.periodoActual(), "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                    txt_periodo.setValue("");
                    dt_fecInicial.setValue(null);
                    dt_fecFinal.setValue(null);
                } else {
                    verificarCamposPerProceso();
                }
            } else {
                verificarCamposPerProceso();
            }
        }
    }

    @Listen("onClick=#btn_ajussp")
    public void ajustarSSP() {
        if (lst_perProc.getSelectedIndex() < 0) {
            Messagebox.show("Por favor, seleccione un periodo de Procesos.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objPerPago == null) {
                Messagebox.show("Verifique datos del periodo.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (objPerPago.getPerPag_situ().toString().equals("Cerrado") && objPerPago.getTab_id().toString().equals("03")) {
                    String msj = "¿Desea ajustar el S.S.P. del Empleador al Monto Minimo en el\n" + "Periodo: " + objPerPago.getPerPag_id() + " - " + objPerPago.getPerPag_desc() + "?";
                    Messagebox.show(msj, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String mnsj = objDaoPerPago.ajusteSSP(objPerPago.getPerPag_id());
                                Messagebox.show(mnsj, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
                } else {
                    Messagebox.show("El periodo debe ser de tipo Normal y estar Cerrado.", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    public void verificarCamposPerProceso() {
        if (cb_tipPla.getValue().isEmpty()) {
            Messagebox.show("Por favor seleccione en el campo Tipo de Planilla", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            codTipPlanilla = cb_tipPla.getValue().substring(0, 2).trim();
            if (!txt_periodo.getValue().isEmpty()) {
                if (txt_periodo.getValue().length() == 6) {
                    String mes = txt_periodo.getValue().substring(4, 6).trim();
                    String periodo = txt_periodo.getValue().substring(0, 4).trim();
                    if (codTipPlanilla.equals("02")) { // CTS SEMESTRAL
                        if (mes.equals("05")) {
                            fechaInicioMayoCTS(Integer.parseInt(periodo), Integer.parseInt(mes));
                            fechaFinalMayoCTS(Integer.parseInt(periodo), Integer.parseInt(mes));
                            txt_desc.focus();
                        } else if (mes.equals("11")) {
                            fechaInicioNovCTS(Integer.parseInt(periodo), Integer.parseInt(mes));
                            fechaFinalNovCTS(Integer.parseInt(periodo), Integer.parseInt(mes));
                            txt_desc.focus();
                        } else {
                            Messagebox.show("Se permite para los meses de MAYO y NOVIEMBRE", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                            txt_periodo.setValue("");
                            txt_desc.setValue("");
                            dt_fecInicial.setValue(null);
                            dt_fecFinal.setValue(null);
                        }
                    } else if (codTipPlanilla.equals("03")) { // NORMAL
                        fechaInicioNormal(Integer.parseInt(periodo), Integer.parseInt(mes));
                        fechaFinalNormal(Integer.parseInt(periodo), Integer.parseInt(mes));
                        txt_desc.focus();
                    } else if (codTipPlanilla.equals("01")) { // De acuerdo al periodo - UTILIDADES
                        if (mes.equals("03")) {
                            fechaInicioMarzoUtil(Integer.parseInt(periodo), Integer.parseInt(mes));
                            fechaFinalMarzoUtil(Integer.parseInt(periodo), Integer.parseInt(mes));
                            txt_desc.focus();
                        } else {
                            Messagebox.show("Se permite para el mes de MARZO", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                            txt_periodo.setValue("");
                            txt_desc.setValue("");
                            dt_fecInicial.setValue(null);
                            dt_fecFinal.setValue(null);
                        }
                    } else {
                        fechaInicioDemas(Integer.parseInt(periodo), Integer.parseInt(mes));
                        fechaFinalDemas(Integer.parseInt(periodo), Integer.parseInt(mes));
                        txt_desc.focus();
                    }

                } else {
                    Messagebox.show("El periodo es de 6 digitos", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                    txt_periodo.setValue("");
                    dt_fecInicial.setValue(null);
                    dt_fecFinal.setValue(null);
                }
            } else {
                Messagebox.show("Por favor ingrese en el campo Período", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_periodo.setValue("");
                dt_fecInicial.setValue(null);
                dt_fecFinal.setValue(null);
            }
        }
    }

    //Eventos Otros
    public void limpiarCampos() {
        cb_tipPla.setSelectedIndex(-1);
        txt_periodo.setValue("");
        txt_desc.setValue("");
        txt_situ.setValue("");
        dt_fecInicial.setValue(null);
        dt_fecFinal.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuAdd.setValue("");
        txt_usuMod.setValue("");
        dt_fecAdd.setValue(null);
        dt_fecMod.setValue(null);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_guardar.setDisabled(b_valida2);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
    }

    public void habilitaCampos(boolean b_valida1) {
        if (s_estado.equals("M")) {
            chk_act.setDisabled(!b_valida1);
            txt_desc.setDisabled(!b_valida1);
        } else {// s_estado = N [Nuevo], s_estado = G [Guardar] y s_estado = D [Dehacer]
            chk_act.setDisabled(b_valida1);
            cb_tipPla.setDisabled(b_valida1);
            txt_desc.setDisabled(b_valida1);
            txt_periodo.setDisabled(b_valida1);
        }
    }

    public ListModelList<PerPago> cargarComboTipoPlanilla() throws SQLException {
        objListPerPago = new ListModelList<PerPago>();
        objListPerPago = new DaoPerPago().listaTipPeriodo();
        objListPerPago.add(0, new PerPago("", ""));
        return objListPerPago;
    }

    public Object generaRegistro() throws ParseException {
        int i_manActivo = chk_act.isChecked() ? 1 : 2;
        String s_manPeriodo = txt_periodo.getValue().trim() + cb_tipPla.getSelectedItem().getValue().toString().substring(0, 2);
        String s_manDesc = txt_desc.getValue().trim();
        String s_manFecI = sdf.format(dt_fecInicial.getValue());
        String s_manFecF = sdf.format(dt_fecFinal.getValue());
        return new PerPago(i_manActivo, s_manPeriodo, s_manDesc, sdf.parse(s_manFecI), sdf.parse(s_manFecF));
    }

    public String verificarCampos() {
        if (txt_periodo.getValue().length() == 6) {
            s_mes = txt_periodo.getValue().substring(4, 6);
        }
        String s_resultado = "";
        if (cb_tipPla.getValue().isEmpty()) {
            s_resultado = "Por favor seleccione en el campo Tipo de Planilla";
            foco = "cb_tipPla";
        } else if (txt_periodo.getValue().isEmpty()) {
            s_resultado = "Por favor ingrese en el campo Período";
            foco = "txt_periodo";
        } else if (!txt_periodo.getValue().matches("[0-9]*")) {
            s_resultado = "El Campo Período, solo debe ingresar números";
            foco = "txt_periodo";
        } else if (s_mes != null) {
            if (Utilitarios.isMes(s_mes) == false) {
                s_resultado = "El Campo Período, es incorrecto.\n Ejemplo : " + Utilitarios.periodoActual();
                foco = "txt_periodo";
            } else if (txt_periodo.getValue().length() != 6) {
                s_resultado = "Ingrese 6 digitos en el campo Período";
                foco = "txt_periodo";
            } else if (txt_desc.getValue().isEmpty()) {
                s_resultado = "Por favor ingrese en el campo la descripción";
                foco = "txt_desc";
            } else if (Utilitarios.validarLetrasYNumeros(txt_desc.getValue().trim()) == false) {
                s_resultado = "El Campo Descripción permite Letras, Números o ambos";
                foco = "txt_desc";
            } else {
                verificarCamposPerProceso();
            }
        } else if (txt_periodo.getValue().length() != 6) {
            s_resultado = "Ingrese 6 digitos en el campo Período";
            foco = "txt_periodo";
        } else if (txt_desc.getValue().isEmpty()) {
            s_resultado = "Por favor ingrese en el campo la descripción";
            foco = "txt_desc";
        } else if (Utilitarios.validarLetrasYNumeros(txt_desc.getValue().trim()) == false) {
            s_resultado = "El Campo Descripción permite Letras, Números o ambos";
            foco = "txt_desc";
        }

        return s_resultado;
    }

    public void colocarFoco() {
        if (foco.equals("cb_tipPla")) {
            cb_tipPla.focus();
        } else if (foco.equals("txt_periodo")) {
            txt_periodo.focus();
            txt_periodo.setValue("");
            dt_fecInicial.setValue(null);
            dt_fecFinal.setValue(null);
        } else if (foco.equals("txt_desc")) {
            txt_desc.focus();
            txt_desc.setValue("");
        }
    }

    public void llenarCampos() {
        //Lista
        txt_usuAdd.setValue(objPerPago.getPerPag_usuAdd());
        dt_fecAdd.setValue(objPerPago.getPerPag_fecAdd());
        txt_usuMod.setValue(objPerPago.getPerPag_usuMod());
        dt_fecMod.setValue(objPerPago.getPerPag_fecMod());
        //Mantenimiento
        cb_tipPla.setSelectedItem(Utilitarios.textoPorTexto(cb_tipPla, Utilitarios.lpad(String.valueOf(objPerPago.getTab_id()), 2, "0")));
        txt_periodo.setText(String.valueOf(objPerPago.getPerPag_id()).substring(0, 6));
        txt_desc.setText(objPerPago.getPerPag_desc());
        chk_act.setChecked(objPerPago.getPerPag_estado() == 1);
        dt_fecInicial.setValue(objPerPago.getPerPag_fecIni());
        dt_fecFinal.setValue(objPerPago.getPerPag_fecFin());
        txt_situ.setText(objPerPago.getPerPag_situ());
    }

    public Date fechaInicioMayoCTS(int anio, int mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(anio - 1, mes + 5, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dt_fecInicial.setValue(cal.getTime());
        return cal.getTime();
    }
    
   
    

    public Date fechaFinalMayoCTS(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 2, 1);
        cal.set(anio, mes - 2, cal.getActualMaximum(Calendar.DAY_OF_MONTH));//prueba
        dt_fecFinal.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaInicioNovCTS(int anio, int mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(anio, mes - 7, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dt_fecInicial.setValue(cal.getTime());
        return cal.getTime();
    }
//error 28
    public Date fechaFinalNovCTS(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 2, 1);
        cal.set(anio, mes - 2, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dt_fecFinal.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaInicioNormal(int anio, int mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(anio, mes - 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dt_fecInicial.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaFinalNormal(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1);
        cal.set(anio, mes - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dt_fecFinal.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaInicioDemas(int anio, int mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(anio, mes - 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dt_fecInicial.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaFinalDemas(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1);
        cal.set(anio, mes - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dt_fecFinal.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaInicioMarzoUtil(int anio, int mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(anio - 1, mes - 3, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dt_fecInicial.setValue(cal.getTime());
        return cal.getTime();
    }

    public Date fechaFinalMarzoUtil(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio - 1, mes + 8, 1);
        cal.set(anio - 1, mes + 8, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dt_fecFinal.setValue(cal.getTime());
        return cal.getTime();
    }

    public void llenaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
