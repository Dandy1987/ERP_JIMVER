package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Repartidor;
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

public class ControllerRepartidor extends SelectorComposer<Component> implements InterfaceControllers {

    // Componentes Web
    @Wire
    Tab tab_listarepartidores, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_repid, txt_repapepat, txt_repapemat, txt_repnom,
            txt_repdirecc, txt_repdni, txt_reptelef,
            txt_usuadd, txt_usumod, txt_busqueda;
    @Wire
    Combobox cb_busqueda, cb_busest;
    @Wire
    Checkbox chk_repest;
    @Wire
    Listbox lst_repartidor;
    @Wire
    Datebox dt_repfecnac, d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Repartidor> objlstRepartidor;
    DaoAccesos objDaoAccesos;
    DaoRepartidor objDaoRepartidor;
    Accesos objAccesos;
    Repartidor objRepartidor;
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "", campo = "";
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
    private static final Logger LOGGER = Logger.getLogger(ControllerRepartidor.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_repartidores")
    public void llenaRegistros() throws SQLException {

        objlstRepartidor = new ListModelList<Repartidor>();
        objDaoRepartidor = new DaoRepartidor();
        objRepartidor = new Repartidor();

        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();

        //carga las asignacion reparto
        objlstRepartidor = objDaoRepartidor.busquedaRepartidores(1, "%%", 1);
        if (objlstRepartidor != null) {
            lst_repartidor.setModel(objlstRepartidor);
        }

        cb_busqueda.setSelectedIndex(0);
        cb_busest.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30107000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Mantenimiento de Repartidor con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Repartidor con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creacion de un nuevo Repartidor");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creacion de un nuevo Repartidor");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edicion de Repartidor");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edicion de Repartidor");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminacion de Repartidor");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminacion de Repartidor");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Repartidores");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Repartidores");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarlista();

        int seleccion = 0, estado;
        int index_est = cb_busest.getSelectedIndex();
        int index_sel = cb_busqueda.getSelectedIndex();
        String s_consulta = txt_busqueda.getValue().isEmpty() ? "%%" : txt_busqueda.getValue();//.toUpperCase().replace("0", "").trim();

        if (index_est == 0) {
            estado = 1;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha seleccionado el estado 'ACTIVO' para su busqueda");
        } else if (index_est == 1) {
            estado = 2;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha seleccionado el estado 'INACTIVO' para su busqueda");
        } else {
            estado = 3;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha seleccionado el estado 'TODOS' para su busqueda");
        }

        if (index_sel == 0) {
            seleccion = 0;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta());
        } else if (index_sel == 1) {
            seleccion = 1;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (index_sel == 2) {
            seleccion = 2;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado la descripcion " + s_consulta + " para su busqueda");
        } else if (index_sel == 3) {
            seleccion = 3;
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado la descripcion " + s_consulta + " para su busqueda");
        }

        objlstRepartidor = objDaoRepartidor.busquedaRepartidores(seleccion, s_consulta, estado);
        if (objlstRepartidor.getSize() > 0) {
            lst_repartidor.setModel(objlstRepartidor);
        } else {
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no se encontraron registros para la busqueda");
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

    @Listen("onSelect=#lst_repartidor")
    public void seleccionaRegistro() throws SQLException, ParseException {
        objRepartidor = (Repartidor) lst_repartidor.getSelectedItem().getValue();
        if (objRepartidor == null) {
            objRepartidor = objlstRepartidor.get(i_sel + 1);
        }
        i_sel = lst_repartidor.getSelectedIndex();
        limpiarCampos();
        llenarCampos(objRepartidor);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_repest.setDisabled(true);
        chk_repest.setChecked(true);
        txt_repapepat.focus();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_repartidor.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_repartidor.focus();
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            chk_repest.setDisabled(false);
            txt_repdni.setDisabled(true);
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
            String s_validadni = verificardni();
            if (!s_validadni.isEmpty()) {
                Messagebox.show(s_validadni, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("dni")) {
                                txt_repdni.focus();
                            }
                        }
                    }
                });
            } else {
                Messagebox.show("Está seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            guardarCambios();
                        }
                    }
                });

            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_repartidor.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_repartidor.focus();
        } else {
            s_mensaje = "Está seguro que desea eliminar este repartidor?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objParametroSalida = objDaoRepartidor.eliminarRepartidor(objRepartidor);
                        if (objParametroSalida.getFlagIndicador() == 0) {
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarlista();
                            busquedaRegistros();
                        }
                        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        lst_repartidor.focus();
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
                            tab_listarepartidores.setDisabled(false);
                            seleccionaTab(true, false);
                            VerificarTransacciones();
                            habilitaCampos(true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            chk_repest.setDisabled(true);
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha deshecho los cambios");
                            lst_repartidor.setSelectedIndex(-1);
                            lst_repartidor.focus();
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstRepartidor == null || objlstRepartidor.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
            objMapObjetos.put("rep_key", objRepartidor.getRep_key());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
              Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionRepartidor.zul", null, objMapObjetos);
              window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manrepartidores")
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

    @Listen("onOK=#txt_repapepat")
    public void onOK_ApePaterno() {
        if (!txt_repapepat.getValue().isEmpty()) {
            txt_repapemat.focus();
        }
    }

    @Listen("onOK=#txt_repapemat")
    public void onOK_ApeMaterno() {
        if (!txt_repapemat.getValue().isEmpty()) {
            txt_repnom.focus();
        }
    }

    @Listen("onOK=#txt_repnom")
    public void onOK_Nombre() {
        if (!txt_repnom.getValue().isEmpty()) {
            txt_repdirecc.focus();
        }
    }

    @Listen("onOK=#txt_repdirecc")
    public void onOK_Direccion() {
        if (!txt_repdirecc.getValue().isEmpty()) {
            txt_repdni.focus();
        }
    }

    @Listen("onOK=#txt_repdni")
    public void onOK_Dni() {
        if (!txt_repdni.getValue().isEmpty()) {
            dt_repfecnac.focus();
        }
    }

    @Listen("onOK=#dt_repfecnac")
    public void onOK_FecNac() {
        if (dt_repfecnac.getValue() != null) {
            txt_reptelef.focus();
        }
    }
    
    @Listen("onOK=#txt_reptelef")
    public void nextGuardar() throws SQLException {
       botonGuardar();
    }

    //Eventos Otros 
    public String verificar() {
        s_mensaje = "";
        Utilitarios objUtilitarios = new Utilitarios();
        int edad = objUtilitarios.validaEdadMayor(dt_repfecnac.getValue());
        if (txt_repapepat.getValue().isEmpty()) {
            campo = "apepat";
            s_mensaje = "El campo 'APE.PATERNO' es obligatorio";
        } else if (!txt_repapepat.getValue().matches("[a-zA-ZñÑáÁéÉiÍóÓúÚ\\s]+")) {
            campo = "apepat";
            s_mensaje = "Por favor ingresar solo letras en el campo 'APE.PATERNO'";
        } else if (txt_repapemat.getValue().isEmpty()) {
            campo = "apemat";
            s_mensaje = "El campo 'APE.MATERNO' es obligatorio";
        } else if (!txt_repapemat.getValue().matches("[a-zA-ZñÑáÁéÉiÍóÓúÚ\\s]+")) {
            campo = "apemat";
            s_mensaje = "Por favor ingresar solo letras en el campo 'APE.MATERNO'";
        } else if (txt_repnom.getValue().isEmpty()) {
            campo = "nom";
            s_mensaje = "El campo 'NOMBRE' es obligatorio";
        } else if (!txt_repnom.getValue().matches("[a-zA-ZñÑáÁéÉiÍóÓúÚ\\s]+")) {
            campo = "nom";
            s_mensaje = "Por favor ingresar solo letras en el campo 'NOMBRE'";
        } else if (txt_repdirecc.getValue().isEmpty()) {
            campo = "direcc";
            s_mensaje = "El campo 'DIRECCION' es obligatorio";
        } else if (txt_repdni.getValue().isEmpty() && !txt_repdni.isDisabled()) {
            campo = "dni";
            s_mensaje = "El campo 'DNI' es obligatorio";
        } else if (!txt_repdni.getValue().matches("[0-9]*")) {
            campo = "dni";
            s_mensaje = "Ingrese valores numericos en el campo 'DNI'";
        } else if (txt_repdni.getValue().length() != 8 && !txt_repdni.isDisabled()) {
            campo = "dni";
            s_mensaje = "Por favor ingrese 8 valores numéricos en el campo 'DNI'";
        } else if (dt_repfecnac.getValue() == null) {
            campo = "fecnac";
            s_mensaje = "Debe ingresar 'FEC. NAC' ";
        } else if (edad < 18) {
            campo = "fecnac";
            s_mensaje = "El Repartidor es menor de edad";
        } else if (txt_reptelef.getValue().isEmpty() && !txt_reptelef.isDisabled()) {
            campo = "telef";
            s_mensaje = "El campo 'TELEFONO' es obligatorio";
        } else if (!txt_reptelef.getValue().matches("[0-9]*")) {
            campo = "telef";
            s_mensaje = "Ingrese valores numéricos en el campo 'TELEFONO'";
        } else {
            s_mensaje = "";
        }
        return s_mensaje;
    }

    public String verificardni() throws WrongValueException, SQLException {
        s_mensaje = "";
        if (s_estado.equals("N")) {
            if (txt_repdni.getValue().matches("[0-9]*")) {
                Repartidor objRepDni = new DaoRepartidor().existeDNI(txt_repdni.getValue());
                if (objRepDni != null) {
                    if (objRepDni.getRep_dni().equals(txt_repdni.getValue())) {
                        campo = "dni";
                        s_mensaje = "El DNI ya existe, verifique por favor";
                    }

                }
            }
        }
        return s_mensaje;
    }

    public void validafocos() {
        if (campo.equals("apepat")) {
            txt_repapepat.setValue("");
            txt_repapepat.focus();
        } else if (campo.equals("apemat")) {
            txt_repapemat.setValue("");
            txt_repapemat.focus();
        } else if (campo.equals("nom")) {
            txt_repnom.setValue("");
            txt_repnom.focus();
        } else if (campo.equals("direcc")) {
            txt_repdirecc.setValue("");
            txt_repdirecc.focus();
        } else if (campo.equals("dni")) {
            txt_repdni.setValue("");
            txt_repdni.focus();
        } else if (campo.equals("fecnac")) {
            dt_repfecnac.setValue(null);
            dt_repfecnac.focus();
        } else if (campo.equals("telef")) {
            txt_reptelef.setValue("");
            txt_reptelef.focus();
        }
    }

    public void validafocosdni() {
        if (campo.equals("dni")) {
            txt_repdni.focus();
        }
    }

    public void guardarCambios() throws SQLException {
        objRepartidor = generaRegistro();
        objParametroSalida = s_estado.equals("N") ? objDaoRepartidor.insertarRepartidor(objRepartidor) : objDaoRepartidor.actualizarRepartidor(objRepartidor);
        if (objParametroSalida.getFlagIndicador() == 0) {
            VerificarTransacciones();
            tbbtn_btn_guardar.setDisabled(true);
            tbbtn_btn_deshacer.setDisabled(true);
            chk_repest.setDisabled(true);
            habilitaTab(false, false);
            seleccionaTab(true, false);
            habilitaCampos(true);
            limpiarCampos();
            limpiaAuditoria();
            lst_repartidor.setSelectedIndex(-1);
            objlstRepartidor = objDaoRepartidor.listaRepartidor(1);
            lst_repartidor.setModel(objlstRepartidor);
        }

        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
    }

    public Repartidor generaRegistro() {
        int rep_key = txt_repid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_repid.getValue());
        String rep_id = txt_repid.getValue();
        String rep_apepat = txt_repapepat.getValue().toUpperCase().trim();
        String rep_apemat = txt_repapemat.getValue().toUpperCase().trim();
        String rep_nom = txt_repnom.getValue().toUpperCase().trim();
        String rep_dni = txt_repdni.getValue().trim();
        Date rep_fecnac = dt_repfecnac.getValue();
        int rep_telef = Integer.parseInt(txt_reptelef.getValue().trim());
        String rep_direcc = txt_repdirecc.getValue().toUpperCase().trim();
        int rep_est = chk_repest.isChecked() ? 1 : 2;
        String rep_usuadd = objUsuarioCredential.getCuenta();
        String rep_usumod = objUsuarioCredential.getCuenta();

        return new Repartidor(rep_key, rep_id, rep_apepat, rep_apemat, rep_nom, rep_dni,
                rep_fecnac, rep_telef, rep_direcc,
                rep_est, rep_usuadd, rep_usumod);
    }

    public void llenarCampos(Repartidor objRepartidor) throws ParseException {
        txt_repid.setValue(objRepartidor.getRep_id());
        txt_repnom.setValue(objRepartidor.getRep_nom());
        txt_repapepat.setValue(objRepartidor.getRep_apepat());
        txt_repapemat.setValue(objRepartidor.getRep_apemat());
        txt_repdirecc.setValue(objRepartidor.getRep_direcc());
        txt_reptelef.setValue(String.valueOf(objRepartidor.getRep_telef()));
        dt_repfecnac.setValue(objRepartidor.getRep_fecnac());
        txt_repdni.setValue(objRepartidor.getRep_dni());
        chk_repest.setChecked(objRepartidor.getRep_est() == 1);
        chk_repest.setLabel(chk_repest.isChecked() ? "ACTIVO" : "INACTIVO");
        txt_usuadd.setValue(objRepartidor.getRep_usuadd());
        d_fecadd.setValue(objRepartidor.getRep_fecadd());
        txt_usumod.setValue(objRepartidor.getRep_usumod());
        d_fecmod.setValue(objRepartidor.getRep_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarepartidores.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listarepartidores.setDisabled(b_valida1);
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

    public void limpiarlista() {
        objlstRepartidor = null;
        lst_repartidor.setModel(objlstRepartidor);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCampos() {
        txt_repid.setValue("");
        txt_repnom.setValue("");
        txt_repapepat.setValue("");
        txt_repapemat.setValue("");
        txt_repdirecc.setValue("");
        txt_reptelef.setValue("");
        dt_repfecnac.setValue(null);
        txt_repdni.setValue("");
        chk_repest.setChecked(false);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_repnom.setDisabled(b_valida1);
        txt_repapepat.setDisabled(b_valida1);
        txt_repapemat.setDisabled(b_valida1);
        txt_repdirecc.setDisabled(b_valida1);
        txt_reptelef.setDisabled(b_valida1);
        dt_repfecnac.setDisabled(b_valida1);
        txt_repdni.setDisabled(b_valida1);
    }

    //Metodos sin utlizar
    public void llenarCampos() throws ParseException {
    }

    public void OnChange() {
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
    }
}
