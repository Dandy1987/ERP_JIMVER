package org.me.gj.controller.distribucion.mantenimiento;

import bsh.ParserConstants;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
import org.me.gj.model.distribucion.mantenimiento.Chofer;
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

public class ControllerChofer extends SelectorComposer<Component> implements InterfaceControllers {

    // Componentes Web
    @Wire
    Tab tab_listachoferes, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_chofid, txt_chofapepat, txt_chofapemat, txt_chofnom, txt_chofrazsoc,
            txt_chofdirecc, txt_chofdni, txt_chofruc, txt_choftelef, txt_chofbrevete,
            txt_usuadd, txt_usumod, txt_busqueda;
    @Wire
    Combobox cb_tipodoc, cb_busqueda, cb_busest;
    @Wire
    Checkbox chk_chofest;
    @Wire
    Listbox lst_chofer;
    @Wire
    Datebox dt_choffecnac, d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Chofer> objlstChofer;
    ListModelList<TipoDocumento> objlstTipoDoc;
    DaoCliente objDaoCliente;
    DaoAccesos objDaoAccesos;
    DaoChofer objDaoChofer;
    Accesos objAccesos;
    Chofer objChofer;
    //Variables Publicas
    String s_estado = "Q";
    String s_mensaje = "", campo = "";
    int i_sel;
    int valor;
    int emp_id;
    int suc_id;
    String busqueda;
    int cont = 0;
    public static boolean bandera = false;
    ParametrosSalida objParametroSalida;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerChofer.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_choferes")
    public void llenaRegistros() throws SQLException {
        objlstTipoDoc = new ListModelList<TipoDocumento>();
        objlstChofer = new ListModelList<Chofer>();
        objDaoChofer = new DaoChofer();
        objDaoCliente = new DaoCliente();
        objChofer = new Chofer();

        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();

        //carga los tipos de documentos
        objlstTipoDoc = objDaoCliente.listaTipoDocumento();
        if (objlstTipoDoc != null) {
            cb_tipodoc.setModel(objlstTipoDoc);
        }

        //carga las asignacion reparto
        objlstChofer = objDaoChofer.listaChofer(1);
        if (objlstChofer != null) {
            lst_chofer.setModel(objlstChofer);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30106000, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Mantenimiento de Chofer con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Chofer con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creacion de un nuevo Chofer");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creacion de un nuevo Chofer");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edicion de Chofer");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edicion de Chofer");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminacion de Chofer");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminacion de Chofer");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Choferes");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Choferes");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarlista();

        int seleccion = 0, estado;
        int index_est = cb_busest.getSelectedIndex();
        int index_sel = cb_busqueda.getSelectedIndex();
        while (txt_busqueda.getValue().trim().equals("%")) {
            cont++;
        }

  /*
        if (txt_busqueda.getValue().trim().replace("%", "").length() == 1) {
            busqueda = "000" + String.valueOf(Integer.parseInt(txt_busqueda.getValue().trim()));
        } else if (txt_busqueda.getValue().trim().replace("%", "").length() == 2) {
            busqueda = "00" + String.valueOf(Integer.parseInt(txt_busqueda.getValue().trim()));
        } else if (txt_busqueda.getValue().trim().replace("%", " ").length() == 3) {
            busqueda = "0" + String.valueOf(Integer.parseInt(txt_busqueda.getValue().trim()));
        } else if (txt_busqueda.getValue().trim().replace("%", "").length() == 4) {
            busqueda = String.valueOf(Integer.parseInt(txt_busqueda.getValue().trim()));
        }*/
        //int bus = Integer.parseInt(txt_busqueda.getValue().trim().replace("%", ""));
        String s_consulta = txt_busqueda.getValue().equals("%%") ? "%%" : txt_busqueda.getValue().trim(); //.toUpperCase().replace("0", "").trim();

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

        objlstChofer = objDaoChofer.busquedaChoferes(seleccion, s_consulta, estado);
        if (objlstChofer.getSize() > 0) {
            lst_chofer.setModel(objlstChofer);
        } else {
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no se encontraron registros para la busqueda");
        }
        limpiarCampos();
        limpiaAuditoria();
        lst_chofer.focus();

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

    @Listen("onSelect=#lst_chofer")
    public void seleccionaRegistro() throws SQLException, ParseException {
        objChofer = (Chofer) lst_chofer.getSelectedItem().getValue();
        if (objChofer == null) {
            objChofer = objlstChofer.get(i_sel + 1);
        }
        i_sel = lst_chofer.getSelectedIndex();
        limpiarCampos();
        llenarCampos(objChofer);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_chofest.setDisabled(true);
        chk_chofest.setChecked(true);
        cb_tipodoc.setFocus(true);
        cb_tipodoc.setSelectedIndex(0);
        txt_chofapepat.focus();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_chofer.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_chofapepat.focus();
            chk_chofest.setDisabled(false);
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
            Messagebox.show("Está seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objChofer = generaRegistro();
                        objParametroSalida = s_estado.equals("N") ? objDaoChofer.insertarChofer(objChofer) : objDaoChofer.actualizarChofer(objChofer);
                        if (objParametroSalida.getFlagIndicador() == 0) {
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            chk_chofest.setDisabled(true);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaCampos(true);
                            limpiarCampos();
                            limpiaAuditoria();
                            busquedaRegistros();
                            lst_chofer.setSelectedIndex(-1);
                            lst_chofer.focus();
                        }

                        Messagebox.show(objParametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            });
        }

    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_chofer.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este Chofer?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objParametroSalida = objDaoChofer.eliminarChofer(objChofer);
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
        Messagebox.show("Esta Seguro que desea Deshacer los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_listachoferes.setDisabled(false);
                            seleccionaTab(true, false);
                            VerificarTransacciones();
                            habilitaCampos(true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            chk_chofest.setDisabled(true);
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha deshecho los cambios");
                            lst_chofer.setSelectedIndex(-1);
                            lst_chofer.focus();
                        }
                    }
                });

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstChofer == null || objlstChofer.isEmpty()) {
            Messagebox.show("No hay registros de choferes para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            /*} else if (lst_chofer.getSelectedIndex() == -1) {
             Messagebox.show("Seleccione Chofer para imprimir");*/
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
            objMapObjetos.put("chof_key", objChofer.getChof_key());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionChofer.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manchoferes")
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

    @Listen("onOK=#txt_chofapepat")
    public void onOK_ApePaterno() {
        if (!txt_chofapepat.getValue().isEmpty()) {
            txt_chofapemat.focus();
        }
    }

    @Listen("onOK=#txt_chofapemat")
    public void onOK_ApeMaterno() {
        if (!txt_chofapemat.getValue().isEmpty()) {
            txt_chofnom.focus();
        }
    }

    @Listen("onOK=#txt_chofnom")
    public void onOK_Nombre() {
        if (!txt_chofnom.getValue().isEmpty()) {
            txt_chofrazsoc.focus();
        }
    }

    @Listen("onOK=#txt_chofrazsoc")
    public void onOK_RazonSocial() {
        if (!txt_chofrazsoc.getValue().isEmpty()) {
            txt_chofdirecc.focus();
        }
    }

    @Listen("onOK=#txt_chofdirecc")
    public void onOK_Direccion() {
        if (!txt_chofdirecc.getValue().isEmpty()) {
            cb_tipodoc.focus();
        }
    }

    @Listen("onOK=#cb_tipodoc")
    public void onSelect_TipDoc() {
        if (!cb_tipodoc.getValue().isEmpty()) {
            txt_chofdni.focus();
        }
    }

    @Listen("onSelect=#cb_tipodoc")
    public void seleccionaTipDoc() throws SQLException {
        txt_chofdni.setValue("");
        txt_chofdni.focus();
    }

    @Listen("onOK=#txt_chofdni")
    public void onOK_Dni() {
        if (!txt_chofdni.getValue().isEmpty()) {
            txt_chofruc.focus();
        }
    }

    @Listen("onOK=#txt_chofruc")
    public void onOK_Ruc() {
        dt_choffecnac.focus();
    }

    @Listen("onOK=#dt_choffecnac")
    public void onOK_FecNac() {
        if (dt_choffecnac.getValue() != null) {
            txt_choftelef.focus();
        }
    }

    @Listen("onOK=#txt_choftelef")
    public void onOK_Telefono() {
        txt_chofbrevete.focus();
    }

    @Listen("onOK=#txt_chofbrevete")
    public void next_Guardar() throws SQLException {
        botonGuardar();
    }

    //Eventos Otros
    public String verificar() {
        s_mensaje = "";
        Utilitarios objUtilitarios = new Utilitarios();
        int edad = objUtilitarios.validaEdadMayor(dt_choffecnac.getValue());
        if (txt_chofapepat.getValue().isEmpty()) {
            campo = "apepat";
            s_mensaje = "El campo 'APE.PATERNO' es obligatorio";
        } else if (!txt_chofapepat.getValue().matches("[a-zA-ZñÑáÁéÉíÍóÓúÚ\\s]+")) {
            campo = "apepat";
            s_mensaje = "Por favor ingresar solo letras en el campo 'APELLIDO PATERNO'";
        } else if (txt_chofapemat.getValue().isEmpty()) {
            campo = "apemat";
            s_mensaje = "El campo 'APE.MATERNO' es obligatorio";
        } else if (!txt_chofapemat.getValue().matches("[a-zA-ZñÑáÁéÉíÍóÓúÚ\\s]+")) {
            campo = "apemat";
            s_mensaje = "Por favor ingresar solo letras en el campo 'APELLIDO MATERNO'";
        } else if (txt_chofnom.getValue().isEmpty()) {
            campo = "nom";
            s_mensaje = "El campo 'NOMBRES' es obligatorio";
        } else if (!txt_chofnom.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            campo = "nom";
            s_mensaje = "Por favor ingresar solo letras en el campo 'NOMBRES'";
        } else if (txt_chofrazsoc.getValue().isEmpty()) {
            campo = "razsoc";
            s_mensaje = "El campo 'RAZON SOCIAL' es obligatorio";
        } else if (txt_chofdirecc.getValue().isEmpty()) {
            campo = "direcc";
            s_mensaje = "El campo 'DIRECCION' es obligatorio";
        } else if (cb_tipodoc.getSelectedIndex() == -1) {
            campo = "tipodoc";
            s_mensaje = "Debe seleccionar 'TIPO DOC'";
        } else if (cb_tipodoc.getValue().equals("DNI")) {
            if (txt_chofdni.getValue().isEmpty() && !txt_chofdni.isDisabled()) {
                campo = "dni";
                s_mensaje = "El campo 'DNI' es obligatorio";
            } else if (!txt_chofdni.getValue().matches("[0-9]*")) {
                campo = "dni";
                s_mensaje = "Ingrese valores numéricos en el campo 'DNI'";
            } else if (txt_chofdni.getValue().length() != 8 && !txt_chofdni.isDisabled()) {
                campo = "dni";
                s_mensaje = "Por favor ingrese 8 valores numéricos en el campo 'DNI'";
            } else if (!txt_chofruc.getValue().matches("[0-9]*")) {
                campo = "ruc";
                s_mensaje = "Ingrese valores numéricos en el campo 'RUC'";
            } else if (txt_chofruc.getValue().length() != 11 && txt_chofruc.isDisabled()) {
                campo = "ruc";
                s_mensaje = "Por favor ingrese 11 valores numéricos en el campo 'RUC'";
            } else if (dt_choffecnac.getValue() == null) {
                campo = "fecnac";
                s_mensaje = "Debe ingresar 'FEC. NAC' ";
            } else if (edad < 18) {
                campo = "fecnaci";
                s_mensaje = "El Chofer es menor de edad";
            } else if (txt_choftelef.getValue().isEmpty() && !txt_choftelef.isDisabled()) {
                campo = "telef";
                s_mensaje = "El campo 'TELEFONO' es obligatorio";
            } else if (!txt_choftelef.getValue().matches("[0-9]*")) {
                campo = "telef";
                s_mensaje = "Ingrese valores numéricos en el campo 'TELEFONO'";
            } else if (txt_chofbrevete.getValue().isEmpty()) {
                campo = "brevete";
                s_mensaje = "El campo 'BREVETE' es obligatorio";
            } else {
                s_mensaje = "";
            }
        } else if (cb_tipodoc.getValue().equals("CARNET EXTRANJERIA") || cb_tipodoc.getValue().equals("PASAPORTE")) {
            if (txt_chofdni.getValue().isEmpty() && !txt_chofdni.isDisabled()) {
                campo = "cp";
                s_mensaje = "El campo '" + cb_tipodoc.getValue() + "' es obligatorio";
            } else if (!txt_chofdni.getValue().matches("[0-9]*")) {
                campo = "cp";
                s_mensaje = "Ingrese valores numéricos en el campo '" + cb_tipodoc.getValue() + "'";
            } else if (txt_chofdni.getValue().length() != 12 && !txt_chofdni.isDisabled()) {
                campo = "cp";
                s_mensaje = "Por favor ingrese 12 valores numéricos en el campo '" + cb_tipodoc.getValue() + "'";
            } else if (!txt_chofruc.getValue().matches("[0-9]*")) {
                campo = "ruc";
                s_mensaje = "Ingrese valores numéricos en el campo 'RUC'";
            } else if (txt_chofruc.getValue().length() != 11 && txt_chofruc.isDisabled()) {
                campo = "ruc";
                s_mensaje = "Por favor ingrese 11 valores numéricos en el campo 'RUC'";
            } else if (dt_choffecnac.getValue() == null) {
                campo = "fecnac";
                s_mensaje = "Debe ingresar 'FEC. NAC' ";
            } else if (edad < 18) {
                campo = "fecnaci";
                s_mensaje = "El Chofer es menor de edad";
            } else if (txt_choftelef.getValue().isEmpty() && !txt_choftelef.isDisabled()) {
                campo = "telef";
                s_mensaje = "El campo 'TELEFONO' es obligatorio";
            } else if (!txt_choftelef.getValue().matches("[0-9]*")) {
                campo = "telef";
                s_mensaje = "Ingrese valores numéricos en el campo 'TELEFONO'";
            } else if (txt_chofbrevete.getValue().isEmpty()) {
                campo = "brevete";
                s_mensaje = "El campo 'BREVETE' es obligatorio";
            } else {
                s_mensaje = "";
            }
        } else if (cb_tipodoc.getValue().equals("PART. DE NACIMIENTO") || cb_tipodoc.getValue().equals("OTROS")) {
            if (txt_chofdni.getValue().isEmpty() && !txt_chofdni.isDisabled()) {
                campo = "otros";
                s_mensaje = "El campo '" + cb_tipodoc.getValue() + "' es obligatorio";
            } else if (!txt_chofdni.getValue().matches("[0-9]*")) {
                campo = "otros";
                s_mensaje = "Ingrese valores numéricos en el campo '" + cb_tipodoc.getValue() + "' ";
            } else if (txt_chofdni.getValue().length() != 15 && !txt_chofdni.isDisabled()) {
                campo = "otros";
                s_mensaje = "Por favor ingrese 15 valores numéricos en el campo '" + cb_tipodoc.getValue() + "'";
            } else if (!txt_chofruc.getValue().matches("[0-9]*")) {
                campo = "ruc";
                s_mensaje = "Ingrese valores numéricos en el campo 'RUC'";
            } else if (txt_chofruc.getValue().length() != 11 && txt_chofruc.isDisabled()) {
                campo = "ruc";
                s_mensaje = "Por favor ingrese 11 valores numéricos en el campo 'RUC'";
            } else if (dt_choffecnac.getValue() == null) {
                campo = "fecnac";
                s_mensaje = "Debe ingresar 'FEC. NAC' ";
            } else if (edad < 18) {
                campo = "fecnaci";
                s_mensaje = "El Chofer es menor de edad";
            } else if (txt_choftelef.getValue().isEmpty() && !txt_choftelef.isDisabled()) {
                campo = "telef";
                s_mensaje = "El campo 'TELEFONO' es obligatorio";
            } else if (!txt_choftelef.getValue().matches("[0-9]*")) {
                campo = "telef";
                s_mensaje = "Ingrese valores numéricos en el campo 'TELEFONO'";
            } else if (txt_chofbrevete.getValue().isEmpty()) {
                campo = "brevete";
                s_mensaje = "El campo 'BREVETE' es obligatorio";
            } else {
                s_mensaje = "";
            }
        }
        return s_mensaje;
    }

    public String verificardni() throws SQLException {
        s_mensaje = "";
        if (s_estado.equals("N")) {
            String dni = txt_chofdni.getValue().isEmpty() ? "" : txt_chofdni.getValue();
            if (!dni.isEmpty() && dni.matches("[0-9]*")) {
                Chofer objChofDni = new DaoChofer().existeDNI(dni);
                if (objChofDni != null) {
                    if (objChofDni.getChof_nroiden().equals(dni)) {
                        campo = "dni";
                        s_mensaje = "El DNI ya existe, verifique por favor";
                    }

                }
            }
        }
        return s_mensaje;
    }

    public String verificarruc() throws WrongValueException, SQLException {
        s_mensaje = "";
        if (s_estado.equals("N")) {
            String ruc = txt_chofruc.getValue().isEmpty() ? "" : txt_chofruc.getValue();
            if (!ruc.isEmpty() && txt_chofruc.getValue().matches("[0-9]*")) {
                Chofer objChofRuc = new DaoChofer().existeRuc(ruc);
                if (objChofRuc != null) {
                    if (objChofRuc.getChof_ruc().equals(ruc)) {
                        campo = "ruc";
                        s_mensaje = "El RUC ya existe, verifique por favor";
                    }

                }
            }
        }
        return s_mensaje;
    }

    public void validafocos() {
        if (campo.equals("apepat")) {
            txt_chofapepat.setValue("");
            txt_chofapepat.focus();
        } else if (campo.equals("apemat")) {
            txt_chofapemat.setValue("");
            txt_chofapemat.focus();
        } else if (campo.equals("nom")) {
            txt_chofnom.setValue("");
            txt_chofnom.focus();
        } else if (campo.equals("razsoc")) {
            txt_chofrazsoc.setValue("");
            txt_chofrazsoc.focus();
        } else if (campo.equals("tipodoc")) {
            cb_tipodoc.focus();
        } else if (cb_tipodoc.getValue().equalsIgnoreCase("DNI")) {
            if (campo.equals("dni")) {
                txt_chofdni.setValue("");
                txt_chofdni.focus();
            } else if (campo.equals("ruc")) {
                txt_chofruc.setValue("");
                txt_chofruc.focus();
            } else if (campo.equals("fecnac")) {
                dt_choffecnac.focus();
            } else if (campo.equals("fecnaci")) {
                dt_choffecnac.setValue(null);
                dt_choffecnac.focus();
            } else if (campo.equals("telef")) {
                txt_choftelef.setValue("");
                txt_choftelef.focus();
            } else if (campo.equals("brevete")) {
                txt_chofbrevete.setValue("");
                txt_chofbrevete.focus();
            }
        } else if (cb_tipodoc.getValue().equalsIgnoreCase("CARNET EXTRANJERIA") || cb_tipodoc.getValue().equalsIgnoreCase("PASAPORTE")) {
            if (campo.equals("cp")) {
                txt_chofdni.setValue("");
                txt_chofdni.focus();
            }
        } else if (cb_tipodoc.getValue().equalsIgnoreCase("PART. DE NACIMIENTO") || cb_tipodoc.getValue().equalsIgnoreCase("OTROS")) {
            if (campo.equals("otros")) {
                txt_chofdni.setValue("");
                txt_chofdni.focus();
            }
        }
    }

    public Chofer generaRegistro() {
        int chof_key = txt_chofid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_chofid.getValue());
        String chof_id = txt_chofid.getValue();
        String chof_apepat = txt_chofapepat.getValue().toUpperCase();
        String chof_apemat = txt_chofapemat.getValue().toUpperCase();
        String chof_nom = txt_chofnom.getValue().toUpperCase();
        String chof_razsoc = txt_chofrazsoc.getValue().toUpperCase();
        String chof_ruc = txt_chofruc.getValue().toUpperCase();
        int chof_ididen = cb_tipodoc.getSelectedItem().getValue();
        String chof_nroiden = txt_chofdni.getValue();
        Date chof_fecnac = dt_choffecnac.getValue();
        String chof_brevete = txt_chofbrevete.getValue().toUpperCase();
        int chof_telef = txt_choftelef.getValue().isEmpty() ? 0 : Integer.parseInt(txt_choftelef.getValue());
        String chof_direcc = txt_chofdirecc.getValue().toUpperCase();
        int chof_est = chk_chofest.isChecked() ? 1 : 2;
        String chof_usuadd = objUsuarioCredential.getCuenta();
        String chof_usumod = objUsuarioCredential.getCuenta();

        return new Chofer(chof_key, chof_id, chof_apepat, chof_apemat, chof_nom, chof_razsoc, chof_ruc,
                chof_ididen, chof_nroiden, chof_fecnac, chof_brevete, chof_telef, chof_direcc,
                chof_est, chof_usuadd, chof_usumod);
    }

    public void llenarCampos(Chofer objChofer) throws ParseException {
        txt_chofid.setValue(objChofer.getChof_id());
        txt_chofnom.setValue(objChofer.getChof_nom());
        txt_chofapepat.setValue(objChofer.getChof_apepat());
        txt_chofapemat.setValue(objChofer.getChof_apemat());
        txt_chofdirecc.setValue(objChofer.getChof_direcc());
        txt_chofrazsoc.setValue(objChofer.getChof_razsoc());
        txt_chofruc.setValue(objChofer.getChof_ruc());
        txt_choftelef.setValue(String.valueOf(objChofer.getChof_telef()));
        txt_chofbrevete.setValue(objChofer.getChof_brevete());
        dt_choffecnac.setValue(objChofer.getChof_fecnac());
        txt_chofdni.setValue(objChofer.getChof_nroiden());
        chk_chofest.setChecked(objChofer.getChof_est() == 1);
        chk_chofest.setLabel(chk_chofest.isChecked() ? "ACTIVO" : "INACTIVO");
        txt_usuadd.setValue(objChofer.getChof_usuadd());
        d_fecadd.setValue(objChofer.getChof_fecadd());
        txt_usumod.setValue(objChofer.getChof_usumod());
        d_fecmod.setValue(objChofer.getChof_fecmod());
        cb_tipodoc.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipodoc, objChofer.getChof_ididen()));
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listachoferes.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listachoferes.setDisabled(b_valida1);
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
        objlstChofer = null;
        lst_chofer.setModel(objlstChofer);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCampos() {
        txt_chofid.setValue("");
        txt_chofnom.setValue("");
        txt_chofapepat.setValue("");
        txt_chofapemat.setValue("");
        txt_chofdirecc.setValue("");
        txt_chofruc.setValue("");
        txt_chofbrevete.setValue("");
        txt_chofrazsoc.setValue("");
        txt_choftelef.setValue("");
        cb_tipodoc.setSelectedIndex(-1);
        dt_choffecnac.setValue(null);
        txt_chofdni.setValue("");
        chk_chofest.setChecked(false);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_chofnom.setDisabled(b_valida1);
        txt_chofapepat.setDisabled(b_valida1);
        txt_chofapemat.setDisabled(b_valida1);
        txt_chofdirecc.setDisabled(b_valida1);
        txt_choftelef.setDisabled(b_valida1);
        txt_chofbrevete.setDisabled(b_valida1);
        txt_chofrazsoc.setDisabled(b_valida1);
        dt_choffecnac.setDisabled(b_valida1);
        txt_chofdni.setDisabled(b_valida1);
        txt_chofruc.setDisabled(b_valida1);
        cb_tipodoc.setDisabled(b_valida1);
    }

    //Metodos sin utlizar
    public void llenarCampos() throws ParseException {
    }

    public void OnChange() {
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
    }

}
