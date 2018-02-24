package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
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
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.planillas.mantenimiento.Util;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author acaro
 */
public class ControllerUtil extends SelectorComposer<Component> implements InterfaceControllers {

    // Componentes Web
    @Wire
    Button btn_actualizar;
    @Wire
    Combobox cb_mesPago, cb_situ, cb_manEjer;
    @Wire
    Checkbox chk_manAct;
    @Wire
    Datebox dt_fecAdd, dt_fecMod, dt_fecInicial, dt_fecFinal;
    @Wire
    Doublebox db_manPartLegal, db_manTotalRemun, db_manFacRemu, db_manTotalDiasLab, db_manFacTiemLab;
    @Wire
    Listbox lst_util;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Textbox txt_usuAdd, txt_usuMod;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;

    //Instancias de Objetos
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;

    DaoUtil objDaoUtil;
    Util objUtil;
    DaoManPer objDaoManPer;

    ListModelList<Util> objLstUtil;
    ListModelList<ManPer> objlstManPeriodos;
    //Variables Públicas
    String s_estado = "Q";
    int i_sel;
    int i_empId;
    String s_mensaje;
    String foco;
    Boolean b_estadoTotalRem;

    //Variables de Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerPerProceso.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_util")
    public void llenarRegistros() throws SQLException {
        objLstUtil = new DaoUtil().listaUtil();
        lst_util.setModel(objLstUtil);
//        objLstUtil = new ListModelList<Util>();
        objDaoUtil = new DaoUtil();
        cargarEjercicio();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Utilidad con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Utilidad con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Utilidad");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Utilidad");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Utilidad");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Utilidad");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Utilidad");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Utilidad");
        }
    }

    @Listen("onSelect=#lst_util")
    public void seleccionaRegistro() throws SQLException {
        if (lst_util.getSelectedIndex() == -1) {
            Messagebox.show("Por favor, seleccione un periodo de Procesos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objUtil = new Util();
            objUtil = lst_util.getSelectedItem().getValue();
            if (objUtil == null) {
                objUtil = objLstUtil.get(i_sel + 1);
            }
            i_sel = lst_util.getSelectedIndex();
            llenarCampos();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objUtil.getpUtil_id());
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        try {
            s_estado = "N";
            objUtil = new Util();
            limpiarCampos();
            seleccionaTab(false, true);
            habilitaBotones(true, false);
            habilitaCampos(false);
            habilitaTab(true, false);
            chk_manAct.setChecked(true);
            chk_manAct.setDisabled(true);
            cargarEjercicio();
            cb_manEjer.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ControllerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        valFoco();
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
                                objUtil = (Util) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoUtil.insertarUtil(objUtil);
                                } else {
                                    objParamSalida = objDaoUtil.editarUtil(objUtil);
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objLstUtil = new ListModelList<Util>();
                                    //Validación de Guardar/Nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    llenarRegistros();
                                    //
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiaAuditoria();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_util.getSelectedIndex() == -1) {
            Messagebox.show("Por favor, seleccione una participacion de utilidad", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            String situacion = new DaoUtil().validarSituacionUtil(objUtil.getpUtil_id());
            if (!situacion.isEmpty()) {
                if (situacion.equals("1")) {
                    s_estado = "M";
                    habilitaBotones(true, false);
                    habilitaTab(true, false);
                    seleccionaTab(false, true);
                    habilitaCampos(false);
                } else {
                    Messagebox.show("No se puede, el periodo está en Proceso, Calculado o Cerrado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                }
            } else {
                Messagebox.show("No hay registro para actualizar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            }
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
                            limpiarCampos();
                            limpiaAuditoria();
                            lst_util.setSelectedIndex(-1);
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
        if (lst_util == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionUtilidades.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onSelect=#cb_tipPla")
    public void onSelectSerie() throws SQLException {
        db_manPartLegal.setValue(0.0);
        db_manTotalRemun.setValue(0.0);
        dt_fecInicial.setValue(null);
        dt_fecFinal.setValue(null);
        String valorC = cb_situ.getValue();
        if (!valorC.isEmpty()) {
            db_manPartLegal.focus();
        }
    }

    @Listen(Events.ON_BLUR + "=#db_manPartLegal")
    public void onBlur_manPartLegal() {
        if (!db_manPartLegal.getValue().isNaN()) {
            if (db_manPartLegal.getValue() > 0.0) {
                if (db_manTotalRemun.getValue().intValue() != 0 && db_manTotalDiasLab.getValue().intValue() != 0) {
                    db_manFacRemu.setValue((db_manPartLegal.getValue() / 2) / db_manTotalRemun.getValue());
                    db_manFacTiemLab.setValue((db_manPartLegal.getValue() / 2) / db_manTotalDiasLab.getValue());
                }
                if (db_manTotalRemun.getValue().intValue() != 0) {
                    db_manFacRemu.setValue((db_manPartLegal.getValue() / 2) / db_manTotalRemun.getValue());
                    b_estadoTotalRem = true;
                } else {
                    if (db_manTotalDiasLab.getValue().intValue() != 0) {
                        db_manFacTiemLab.setValue((db_manPartLegal.getValue() / 2) / db_manTotalDiasLab.getValue());
                    }
                    b_estadoTotalRem = false;
                }
                if (b_estadoTotalRem == true) {
                    if (db_manTotalDiasLab.getValue().intValue() != 0) {
                        db_manFacTiemLab.setValue((db_manPartLegal.getValue() / 2) / db_manTotalDiasLab.getValue());
                    }
                }
            } else {
                Messagebox.show("Debe ingresar mayor a 0 en el campo Participación Legal", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                db_manPartLegal.setValue(Double.NaN);
                db_manFacRemu.setValue(Double.NaN);
                db_manFacTiemLab.setValue(Double.NaN);
            }
        } else {
            Messagebox.show("Por favor ingrese en el campo Participación Legal", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            db_manFacRemu.setValue(Double.NaN);
            db_manFacTiemLab.setValue(Double.NaN);
        }
    }

    @Listen(Events.ON_BLUR + "=#db_manTotalRemun")
    public void onBlur_manTotalRemun() {
        if (!db_manTotalRemun.getValue().isNaN()) {
            if (db_manTotalRemun.getValue() > 0.0) {
                db_manFacRemu.setValue((db_manPartLegal.getValue() / 2) / db_manTotalRemun.getValue());
            } else {
                Messagebox.show("Debe ingresar mayor a 0 en el campo Total Remuneración", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                db_manTotalRemun.setValue(Double.NaN);
                db_manFacRemu.setValue(Double.NaN);
            }
        } else {
            Messagebox.show("Por favor ingrese en el campo Total Remuneración", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            db_manFacRemu.setValue(Double.NaN);
        }
    }

    @Listen(Events.ON_BLUR + "=#db_manTotalDiasLab")
    public void onBlur_manTotalDiasLab() {
        if (!db_manTotalDiasLab.getValue().isNaN()) {
            if (db_manTotalRemun.getValue() > 0.0) {
                db_manFacTiemLab.setValue((db_manPartLegal.getValue() / 2) / db_manTotalDiasLab.getValue());
            } else {
                Messagebox.show("Debe ingresar mayor a 0 en el campo Total Días Laborados", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                db_manTotalDiasLab.setValue(Double.NaN);
                db_manFacTiemLab.setValue(Double.NaN);
            }
        } else {
            Messagebox.show("Por favor ingrese en el campo Total Días Laborados", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            db_manFacTiemLab.setValue(Double.NaN);
        }
    }

    @Listen(Events.ON_OK + "=#db_manPartLegal")
    public void onEnter_ManPartLegal() {
        if (db_manPartLegal.getValue().isNaN()) {
            db_manPartLegal.focus();
        }
    }

    @Listen(Events.ON_OK + "=#db_manTotalRemun")
    public void onEnter_ManTotalRemun() {
        if (db_manTotalRemun.getValue().isNaN()) {
            db_manTotalRemun.focus();
        }
    }

    @Listen(Events.ON_OK + "=#db_manTotalDiasLab")
    public void onEnter_ManTotalDiasLab(Event event) {
        if (db_manTotalDiasLab.getValue().isNaN()) {
            db_manTotalDiasLab.focus();
        }
    }

    @Listen(Events.ON_OK + "=#cb_manEjer")
    public void onEnter_manEjer(Event event) {
        if (!cb_manEjer.getValue().isEmpty()) {
            db_manPartLegal.focus();
        } else {
            Messagebox.show("Por favor seleccione en el campo Ejercicio", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            cb_manEjer.focus();
        }
    }

    @Listen("onCtrlKey=#db_manPartLegal")
    public void ctrl_manPartLegal(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 46://SUPRIMIR
                db_manPartLegal.setValue(Double.NaN);
                db_manFacRemu.setValue(Double.NaN);
                db_manFacTiemLab.setValue(Double.NaN);
                break;
        }
    }

    @Listen("onCtrlKey=#db_manTotalRemun")
    public void ctrl_manTotalRemun(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 46://SUPRIMIR
                db_manTotalRemun.setValue(Double.NaN);
                db_manFacRemu.setValue(Double.NaN);
                break;
        }
    }

    @Listen("onCtrlKey=#db_manTotalDiasLab")
    public void ctrl_manTotalDiasLab(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 46://SUPRIMIR
                db_manTotalDiasLab.setValue(Double.NaN);
                db_manFacTiemLab.setValue(Double.NaN);
                break;
        }
    }

    //Eventos Otros
    public void limpiarCampos() {
        cb_manEjer.setValue("");
        db_manPartLegal.setValue(Double.NaN);
        db_manTotalRemun.setValue(Double.NaN);
        db_manFacRemu.setValue(Double.NaN);
        db_manTotalDiasLab.setValue(Double.NaN);
        db_manFacTiemLab.setValue(Double.NaN);
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
        chk_manAct.setDisabled(b_valida1);
        if (s_estado.equals("M")) {
            cb_manEjer.setDisabled(!b_valida1);
        } else {
            cb_manEjer.setDisabled(b_valida1);
        }
        db_manPartLegal.setDisabled(b_valida1);
        db_manTotalRemun.setDisabled(b_valida1);
        db_manTotalDiasLab.setDisabled(b_valida1);
    }

    public void habilitaCamposEditar(boolean b_valida1) {
        chk_manAct.setDisabled(b_valida1);
        cb_situ.setDisabled(!b_valida1);
        db_manPartLegal.setDisabled(!b_valida1);
        db_manTotalRemun.setDisabled(b_valida1);
        dt_fecInicial.setDisabled(b_valida1);
        dt_fecFinal.setDisabled(b_valida1);
        db_manFacRemu.setDisabled(!b_valida1);
    }

    public ListModelList<Util> cargarEjercicio() throws SQLException {
        objLstUtil = objDaoUtil.listaPeriodoUtil();
        cb_manEjer.setModel(objLstUtil);
        return objLstUtil;
    }

    public Object generaRegistro() throws ParseException {
        int i_manActivo = chk_manAct.isChecked() ? 1 : 2;
        String s_manEjercicio = cb_manEjer.getValue();
        Double dbl_manPartLegal = db_manPartLegal.getValue();
        Double dbl_manTotalRemun = db_manTotalRemun.getValue();
        Double dbl_manFacTotalRemun = db_manFacRemu.getValue();
        Double dbl_manTotalDiasLab = db_manTotalDiasLab.getValue();
        Double dbl_manFacTotalDiasLab = db_manFacTiemLab.getValue();
        return new Util(i_manActivo, s_manEjercicio, dbl_manPartLegal, dbl_manTotalRemun, dbl_manFacTotalRemun, dbl_manTotalDiasLab, dbl_manFacTotalDiasLab);
    }

    public String verificar() {
        String act = "";
        if (cb_manEjer.getValue().isEmpty()) {
            act = "Por favor seleccione en el campo Ejercicio";
            foco = "cb_manEjer";
        } else if (db_manPartLegal.getValue().isNaN()) {
            act = "Por favor ingrese en el campo Participación Legal";
            foco = "db_manPartLegal";
        } else if (db_manPartLegal.getValue() <= 0.0) {
            act = "Debe ingresar mayor a 0 en el campo Participación Legal";
            foco = "db_manPartLegal";
        } else if (db_manTotalRemun.getValue().isNaN()) {
            act = "Por favor ingrese en el campo Total Remuneración";
            foco = "db_manTotalRemun";
        } else if (db_manTotalRemun.getValue() <= 0.0) {
            act = "Debe ingresar mayor a 0 en el campo Total Remuneración";
            foco = "db_manTotalRemun";
        } else if (db_manTotalDiasLab.getValue().isNaN()) {
            act = "Por favor ingrese en el campo Total Días Laborados";
            foco = "db_manTotalDiasLab";
        } else if (db_manTotalDiasLab.getValue() <= 0.0) {
            act = "Debe ingresar mayor a 0 en el campo Total Días Laborados";
            foco = "db_manTotalDiasLab";
        }
        return act;
    }

    public void valFoco() {
        if (foco.equals("cb_manEjer")) {
            cb_manEjer.focus();
        } else if (foco.equals("db_manPartLegal")) {
            db_manPartLegal.focus();
        } else if (foco.equals("db_manTotalRemun")) {
            db_manTotalRemun.focus();
        } else if (foco.equals("db_manTotalDiasLab")) {
            db_manTotalDiasLab.focus();
        }
    }

    public void llenarCampos() {
        //Lista
        txt_usuAdd.setValue(objUtil.getpUtil_usuAdd());
        dt_fecAdd.setValue(objUtil.getpUtil_fecAdd());
        txt_usuMod.setValue(objUtil.getpUtil_usuMod());
        dt_fecMod.setValue(objUtil.getpUtil_fecMod());
        //Mantenimiento
        cb_manEjer.setSelectedItem(Utilitarios.textoPorTexto(cb_manEjer, String.valueOf(objUtil.getpUtil_id())));
        chk_manAct.setChecked(objUtil.getpUtil_estado() == 1);
        db_manPartLegal.setValue(objUtil.getpUtil_parLeg());
        db_manTotalRemun.setValue(objUtil.getpUtil_totRem());
        db_manFacRemu.setValue(objUtil.getpUtil_facRem());
        db_manTotalDiasLab.setValue(objUtil.getpUtil_totDiasLab());
        db_manFacTiemLab.setValue(objUtil.getpUtil_facTpoLab());
    }

    @Listen("onCtrlKey=#w_util")
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

    public void llenaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
