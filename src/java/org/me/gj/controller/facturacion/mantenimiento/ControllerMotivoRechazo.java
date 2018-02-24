package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
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

public class ControllerMotivoRechazo extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listamotrec, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_mrecid, txt_mrecdes, txt_mrecnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_mrectipdoc, cb_mrectipo, cb_busqueda, cb_busest;
    @Wire
    Spinner sp_mrecord;
    @Wire
    Checkbox chk_mrecest, chk_busest;
    @Wire
    Listbox lst_motrec;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<MotivoRechazo> objlstMotRec = new ListModelList<MotivoRechazo>();
    MotivoRechazo objMotivoRechazo = new MotivoRechazo();
    DaoMotivoRechazo objDaoMotivoRechazo = new DaoMotivoRechazo();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q", s_mensaje = "";
    String campo = "";
    public static boolean bandera = false;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMotivoRechazo.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_motrec")
    public void llenaRegistros() throws SQLException {
        objlstMotRec = objDaoMotivoRechazo.listaMotivoRechazo(1);
        lst_motrec.setModel(objlstMotRec);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40103000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Motivo de Rechazo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Motivo de Rechazo con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Motivo de Rechazo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Motivo de Rechazo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Motivo de Rechazo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Motivo de Rechazo");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Motivo de Rechazo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Motivo de Rechazo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Motivo de Rechazo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Motivo de Rechazo");
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
        objlstMotRec = new ListModelList<MotivoRechazo>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del motivo de rechazo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de motivo de rechazo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la codigo del tipo de documento " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la codigo del tipo motivo " + s_consulta + " para su busqueda");
        }
        objlstMotRec = objDaoMotivoRechazo.busquedaMotivoRechazo(i_seleccion, s_consulta, i_estado);
        if (objlstMotRec.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstMotRec.getSize() + " registro(s)");
        }
        
        if (objlstMotRec.getSize() > 0) {
            lst_motrec.setModel(objlstMotRec);
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

    @Listen("onSelect=#cb_mrectipdoc")
    public void onSelectcbmrectipdoc() {
        cb_mrectipo.setValue("");
        String tipo = cb_mrectipdoc.getSelectedItem().getValue();
        cb_mrectipo.setModel(objMotivoRechazo.listaTipoMotRec(tipo));
    }

    @Listen("onSelect=#lst_motrec")
    public void seleccionaRegistro() throws SQLException {
        objMotivoRechazo = (MotivoRechazo) lst_motrec.getSelectedItem().getValue();
        if (objMotivoRechazo == null) {
            objMotivoRechazo = (MotivoRechazo) objlstMotRec.get(i_sel + 1);
        }
        i_sel = lst_motrec.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objMotivoRechazo.getMrec_id());
        limpiarCampos();
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objMotivoRechazo = new MotivoRechazo();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_mrecest.setDisabled(true);
        txt_mrecdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_motrec.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_motrec.focus();
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_mrecdes.setDisabled(true);
            cb_mrectipdoc.setDisabled(true);
            cb_mrectipo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
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
                        if (campo.equals("Descripcion")) {
                            txt_mrecdes.focus();
                        } else if (campo.equals("TipDoc")) {
                            cb_mrectipdoc.focus();
                        } else if (campo.equals("Motivo")) {
                            cb_mrectipo.focus();
                        } else if (campo.equals("Orden")) {
                            sp_mrecord.focus();
                        }
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
                                objMotivoRechazo = (MotivoRechazo) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoMotivoRechazo.insertar(objMotivoRechazo);
                                } else {
                                    objParamSalida = objDaoMotivoRechazo.modificar(objMotivoRechazo);
                                }

                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstMotRec.clear();
                                    //validacion de guardar/nuevo
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    //
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstMotRec = objDaoMotivoRechazo.listaMotivoRechazo(1);
                                    lst_motrec.setModel(objlstMotRec);
                                    objMotivoRechazo = new MotivoRechazo();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                lst_motrec.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_motrec.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_motrec.focus();
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este motivo de Rechazo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoMotivoRechazo.eliminar(objMotivoRechazo);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstMotRec = new ListModelList<MotivoRechazo>();
                                    objlstMotRec = objDaoMotivoRechazo.listaMotivoRechazo(1);
                                    lst_motrec.setModel(objlstMotRec);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                lst_motrec.focus();
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
                            lst_motrec.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_motrec.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstMotRec == null || objlstMotRec.isEmpty()) {
            Messagebox.show("No hay registros de motivos de rechazo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionMotRechazo.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manmotrec")
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
            objlstMotRec = new ListModelList<MotivoRechazo>();
            objlstMotRec = objDaoMotivoRechazo.listaMotivoRechazo(0);
            lst_motrec.setModel(objlstMotRec);
        }
    }

    @Listen("onOK =#txt_mrecdes")
    public void validaDescripcion() {
        cb_mrectipdoc.select();
        cb_mrectipdoc.focus();
    }

    @Listen("onOK =#cb_mrectipdoc")
    public void validaTipoDocumento() {
        cb_mrectipo.select();
        cb_mrectipo.focus();

    }

    @Listen("onOK =#cb_mrectipo")
    public void validaMotivoRechazo() {
        txt_mrecnomrep.focus();
    }

    @Listen("onOK =#txt_mrecnomrep")
    public void validReporte() {
        sp_mrecord.focus();
    }

    @Listen("onOK=#sp_mrecord")
    public void validaOrden() {
        botonGuardar();
    }

    //Eventos Otros
    public String verificar() {
        String verificar;
        if (txt_mrecdes.getValue().isEmpty()) {
            verificar = "El campo Descripcion es obligatorio";
            campo = "Descripcion";
        } else if (txt_mrecdes.getValue().matches("^\\s+")) {
            verificar = "El campo Descripcion, no debe tener espacios en blanco al principio";
            campo = "Descripcion";
            txt_mrecdes.setValue("");
        } else if (txt_mrecdes.getValue().matches("([^\\w])+")) {
            verificar = "El campo Descripcion , no se permite caracteres especiales";
            campo = "Nombres";
            txt_mrecdes.setValue("");
        } else if (cb_mrectipdoc.getSelectedIndex() == -1) {
            verificar = "Debe seleccionar Tipo de Documento";
            campo = "TipDoc";
        } else if (cb_mrectipo.getSelectedIndex() == -1) {
            verificar = "Debe seleccionar Motivo de Rechazo";
            campo = "Motivo";
        } else if (sp_mrecord.getValue() == null) {
            verificar = "Ingrese Orden";
            campo = "Orden";
        } else {
            verificar = "";
        }
        return verificar;
    }

    public void limpiarCampos() {
        chk_mrecest.setChecked(true);
        chk_mrecest.setLabel("ACTIVO");
        txt_mrecid.setValue("");
        txt_mrecdes.setValue("");
        txt_mrecnomrep.setValue("");
        cb_mrectipdoc.setSelectedIndex(-1);
        cb_mrectipo.setValue("");
        cb_mrectipo.setModel(null);
        sp_mrecord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        //limpio mi lista
        objMotivoRechazo = null;
        objlstMotRec = new ListModelList<MotivoRechazo>();
        lst_motrec.setModel(objlstMotRec);
    }

    public void llenarCampos() {
        Utilitarios objUtil = new Utilitarios();
        txt_mrecid.setValue(objMotivoRechazo.getMrec_id());
        txt_mrecdes.setValue(objMotivoRechazo.getMrec_des());
        txt_mrecnomrep.setValue(objMotivoRechazo.getMrec_nomrep());
        cb_mrectipdoc.setSelectedItem(objUtil.textoPorTexto(cb_mrectipdoc, objMotivoRechazo.getMrec_tipdoc()));
        onSelectcbmrectipdoc();
        cb_mrectipo.setValue(objMotivoRechazo.getMrec_destipo());
        chk_mrecest.setChecked(objMotivoRechazo.isValor());
        chk_mrecest.setLabel(objMotivoRechazo.getMrec_est() == 1 ? "ACTIVO" : "INACTIVO");
        sp_mrecord.setValue(objMotivoRechazo.getMrec_ord());
        txt_usuadd.setValue(objMotivoRechazo.getMrec_usuadd());
        d_fecadd.setValue(objMotivoRechazo.getMrec_fecadd());
        txt_usumod.setValue(objMotivoRechazo.getMrec_usumod());
        d_fecmod.setValue(objMotivoRechazo.getMrec_fecmod());
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamotrec.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamotrec.setSelected(b_valida1);
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

    public void habilitaCampos(boolean b_valida) {
        chk_mrecest.setDisabled(b_valida);
        txt_mrecdes.setDisabled(b_valida);
        txt_mrecnomrep.setDisabled(b_valida);
        cb_mrectipdoc.setDisabled(b_valida);
        cb_mrectipo.setDisabled(b_valida);
        sp_mrecord.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int mrec_key = txt_mrecid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_mrecid.getValue());
        String mrec_id = txt_mrecid.getValue().isEmpty() ? "" : txt_mrecid.getValue();
        String mrec_des = txt_mrecdes.getValue().toUpperCase().trim();
        String mrec_tipdoc = cb_mrectipdoc.getSelectedItem().getValue();
        String mrec_tipo = cb_mrectipo.getSelectedItem().getValue();
        int mrec_est = chk_mrecest.isChecked() ? 1 : 2;
        int mrec_ord = sp_mrecord.getValue();
        String mrec_nomrep = txt_mrecnomrep.getValue().toUpperCase().trim();
        String mrec_usuadd = objUsuCredential.getCuenta();
        String mrec_usumod = objUsuCredential.getCuenta();
        return new MotivoRechazo(mrec_key, mrec_id, mrec_des, mrec_tipdoc, mrec_tipo, mrec_est, mrec_ord,
                mrec_nomrep, mrec_usuadd, mrec_usumod);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
