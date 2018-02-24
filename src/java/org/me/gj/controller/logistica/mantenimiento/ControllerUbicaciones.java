package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Executions;

public class ControllerUbicaciones extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaubicaciones, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Textbox txt_ubicid, txt_ubicnomrep, txt_ubicdes, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Doublebox txt_ubicanc, txt_ubiclar, txt_ubicalt;
    @Wire
    Spinner sp_ubicord;
    @Wire
    Checkbox chk_ubicest, chk_ubidef;
    @Wire
    Listbox lst_ubicaciones;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Ubicaciones> objlstUbi = new ListModelList<Ubicaciones>();
    Ubicaciones objUbicacion = new Ubicaciones();
    DaoUbicaciones objDaoUbicaciones = new DaoUbicaciones();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios objUtilitarios = new Utilitarios();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje;
    String campo = "";
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUbicaciones.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_ubicaciones")
    public void llenaRegistros() throws SQLException {
        objlstUbi = objDaoUbicaciones.listaUbicaciones(1);
        lst_ubicaciones.setModel(objlstUbi);
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10107000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Ubicaciones con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Ubicaciones con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Ubicación");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Ubicación");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Ubicación");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Ubicación");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Ubicación");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Ubicación");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Ubicaciones");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Ubicaciones");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstUbi = new ListModelList<Ubicaciones>();
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
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el alto " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el ancho " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el largo " + s_consulta + " para su busqueda");
        }

        objlstUbi = objDaoUbicaciones.busquedaUbicacion(i_seleccion, s_consulta, i_estado);

        if (objlstUbi.getSize() > 0) {
            lst_ubicaciones.setModel(objlstUbi);
        } else {
            objlstUbi = null;
            lst_ubicaciones.setModel(objlstUbi);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#lst_ubicaciones")
    public void seleccionaRegistro() throws SQLException {
        objUbicacion = (Ubicaciones) lst_ubicaciones.getSelectedItem().getValue();
        if (objUbicacion == null) {
            objUbicacion = objlstUbi.get(i_sel + 1);
        }
        i_sel = lst_ubicaciones.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objUbicacion.getUbic_id());
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

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objUbicacion = new Ubicaciones();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_ubicdes.focus();
        chk_ubicest.setDisabled(true);
        chk_ubicest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_ubicaciones.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (chk_ubidef.isChecked() && lst_ubicaciones.getSelectedIndex() >= 0) {
            Messagebox.show("Es una Ubicación por Defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            //chk_ubidef.setDisabled(false);
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            chk_ubidef.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Ubicacion")) {
                            txt_ubicdes.focus();
                        } else if (campo.equals("Alto")) {
                            txt_ubicalt.focus();
                        } else if (campo.equals("Ancho")) {
                            txt_ubicanc.focus();
                        } else if (campo.equals("Largo")) {
                            txt_ubiclar.focus();
                        }
                    }
                }
            });
        } else {
            if (txt_ubicdes.getText().matches("^\\s") || txt_ubicdes.getText().matches("^\\s+")) {
                Messagebox.show("El Campo Ubicacion no debe tener espacio en blanco al principio");

            } else {

                s_mensaje = "Esta Seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida;
                                    objlstUbi = new ListModelList<Ubicaciones>();
                                    objUbicacion = generaRegistro();
                                    if (s_estado.equals("N")) {
                                        s_mensaje = objDaoUbicaciones.insertar(objUbicacion);
                                    } else {
                                        s_mensaje = objDaoUbicaciones.actualizar(objUbicacion);
                                    }
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    objlstUbi = objDaoUbicaciones.listaUbicaciones(1);
                                    lst_ubicaciones.setModel(objlstUbi);
                                    objUbicacion = new Ubicaciones();
                                    lst_ubicaciones.focus();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_ubicaciones.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoUbicaciones.validaMovimientos(objUbicacion);
            valor = objDaoUbicaciones.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoUbicaciones.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (chk_ubidef.isChecked() && lst_ubicaciones.getSelectedIndex() >= 0) {
                Messagebox.show("No se puede eliminar una Ubicación por defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

            } else {
                s_mensaje = "Esta Seguro que desea Eliminar esta ubicacion?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoUbicaciones.eliminar(objUbicacion);
                                    valor = objDaoUbicaciones.i_flagErrorBD;
                                    if (valor == 0) {
                                        objlstUbi.clear();
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        objlstUbi = objDaoUbicaciones.listaUbicaciones(1);
                                        lst_ubicaciones.setModel(objlstUbi);
                                        lst_ubicaciones.focus();
                                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        limpiarCampos();
                                        limpiaAuditoria();
                                    } else {
                                        Messagebox.show(objDaoUbicaciones.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirm", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_ubicaciones.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_ubicaciones.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstUbi == null || objlstUbi.isEmpty()) {
            Messagebox.show("No hay Registros de Ubicaciones para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionUbi.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#tabbox_ubicaciones")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
        }
    }

    @Listen("onCheck=#chk_ubidef")
    public void VerificaEstado() throws SQLException {
        if (objDaoUbicaciones.busquedaExistenciaDefault() > 0) {
            chk_ubidef.setChecked(false);
            Messagebox.show("Ya existe una ubicacion por defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstUbi = new ListModelList<Ubicaciones>();
            objlstUbi = objDaoUbicaciones.listaUbicaciones(0);
            lst_ubicaciones.setModel(objlstUbi);
        }
    }

    @Listen("onOK=#txt_ubicalt;onBlur=#txt_ubicalt")
    public void onOk_txt_ubicalt() {
        if (txt_ubicalt.getValue() != null) {
            double d_numero = txt_ubicalt.getValue();
            double d_resultado = objUtilitarios.FormatoNumero(d_numero, 3);
            txt_ubicalt.setValue(d_resultado);
        }
    }

    @Listen("onOK=#txt_ubicanc;onBlur=#txt_ubicanc")
    public void onOk_txt_ubicanc() {
        if (txt_ubicanc.getValue() != null) {
            double d_numero = txt_ubicanc.getValue();
            double d_resultado = objUtilitarios.FormatoNumero(d_numero, 3);
            txt_ubicanc.setValue(d_resultado);
            txt_ubiclar.focus();
        }
    }

    @Listen("onOK=#txt_ubiclar;onBlur=#txt_ubiclar")
    public void onOk_txt_ubiclar() {
        if (txt_ubiclar.getValue() != null) {
            double d_numero = txt_ubiclar.getValue();
            double d_resultado = objUtilitarios.FormatoNumero(d_numero, 3);
            txt_ubiclar.setValue(d_resultado);
            txt_ubicalt.focus();
        }
    }

    @Listen("onOK=#txt_ubicdes")
    public void nextUbicAnc() {
        if (txt_ubicdes.getValue() != null) {
            txt_ubicanc.focus();
        }
    }

    @Listen("onOK=#txt_ubicalt")
    public void nextOrden() {
        sp_ubicord.focus();
    }

    @Listen("onOK=#sp_ubicord")
    public void onOK_sp_AlmOrd() {
        botonGuardar();
    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_manubi")
    public void ctrl_teclado(Event event) throws SQLException, ParseException {
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

    public void llenarCampos() {
        txt_ubicid.setValue(String.valueOf(objUbicacion.getUbic_id()));
        txt_ubicdes.setValue(objUbicacion.getUbic_des());
        if (objUbicacion.getUbic_est() == 1) {
            chk_ubicest.setChecked(true);
        } else {
            chk_ubicest.setChecked(false);
        }
        //txt_ubicnomrep.setValue(objUbicacion.getUbic_nomrep());
        sp_ubicord.setValue(objUbicacion.getUbic_ord());
        txt_ubicanc.setValue(objUbicacion.getUbic_anc());
        txt_ubicalt.setValue(objUbicacion.getUbic_alt());
        txt_ubiclar.setValue(objUbicacion.getUbic_lar());
        txt_usuadd.setValue(objUbicacion.getUbic_usuadd());
        d_fecadd.setValue(objUbicacion.getUbic_fecadd());
        txt_usumod.setValue(objUbicacion.getUbic_usumod());
        d_fecmod.setValue(objUbicacion.getUbic_fecmod());
        if (objUbicacion.getUbic_default() == 1) {
            chk_ubidef.setChecked(true);
        } else {
            chk_ubidef.setChecked(false);
        }
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaubicaciones.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaubicaciones.setSelected(b_valida1);
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

    public String verificar() {
        String s_valor;
        double alto, ancho, largo;
        alto = (txt_ubicalt.getValue() != null ? txt_ubicalt.getValue() : 0);
        ancho = (txt_ubicanc.getValue() != null ? txt_ubicanc.getValue() : 0);
        largo = (txt_ubiclar.getValue() != null ? txt_ubiclar.getValue() : 0);
        if (txt_ubicdes.getValue().isEmpty()) {
            s_valor = "El Campo Ubicacion es Obligatorio";
            campo = "Ubicacion";
        } else if (!txt_ubicdes.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "El Campo Ubicacion Ingresar solo letras";
            campo = "Ubicacion";
            txt_ubicdes.setValue("");
        } else if (txt_ubicanc.getValue() == null) {
            s_valor = "El Campo Ancho es Obligatorio";
            campo = "Ancho";
        } else if (txt_ubiclar.getValue() == null) {
            s_valor = "El Campo Largo es Obligatorio";
            campo = "Largo";
        } else if (txt_ubicalt.getValue() == null) {
            s_valor = "El Campo Alto es Obligatorio";
            campo = "Alto";
        } else if (ancho <= 0) {
            s_valor = "El Campo Ancho es Inválido";
            campo = "Ancho";
            txt_ubicanc.setValue(null);
        } else if (largo <= 0) {
            s_valor = "El Campo Largo es Inválido";
            campo = "Largo";
            txt_ubiclar.setValue(null);
        } else if (alto <= 0) {
            s_valor = "El Campo Alto es Inválido";
            campo = "Alto";
            txt_ubicalt.setValue(null);
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        chk_ubidef.setChecked(false);
        txt_ubicid.setValue("");
        txt_ubicdes.setValue("");
        chk_ubicest.setValue("");
        //txt_ubicnomrep.setValue("");
        txt_ubicanc.setValue(null);
        txt_ubiclar.setValue(null);
        txt_ubicalt.setValue(null);
        sp_ubicord.setValue(0);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_ubicdes.setDisabled(b_valida);
        chk_ubicest.setDisabled(b_valida);
        //txt_ubicnomrep.setDisabled(b_valida);
        sp_ubicord.setDisabled(b_valida);
        txt_ubicanc.setDisabled(b_valida);
        txt_ubiclar.setDisabled(b_valida);
        txt_ubicalt.setDisabled(b_valida);
        chk_ubidef.setDisabled(b_valida);
    }

    public Ubicaciones generaRegistro() {
        int i_ubickey = objUbicacion.getUbic_key();
        String ubicdes = txt_ubicdes.getValue().toUpperCase().trim();
        int i_ubicest;
        if (chk_ubicest.isChecked()) {
            i_ubicest = 1;
        } else {
            i_ubicest = 2;
        }
        //String s_ubicnomrep = txt_ubicnomrep.getValue().toUpperCase().trim();
        int i_ubicord;
        if (sp_ubicord.getValue().toString().isEmpty()) {
            i_ubicord = 0;
        } else {
            i_ubicord = sp_ubicord.getValue();
        }
        double db_ubicanc = txt_ubicanc.getValue();
        double db_ubicalt = txt_ubicalt.getValue();
        double db_ubiclar = txt_ubiclar.getValue();
        int i_empid = objUsuCredential.getCodemp();
        int i_sucid = objUsuCredential.getCodsuc();
        String s_usuadd = objUsuCredential.getCuenta();
        String s_usumod = objUsuCredential.getCuenta();
        int i_ubicdef;
        if (chk_ubidef.isChecked()) {
            i_ubicdef = 1;
        } else {
            i_ubicdef = 0;
        }
        return new Ubicaciones(i_ubickey, ubicdes, i_ubicest, db_ubicalt, db_ubiclar, db_ubicanc,
                /*s_ubicnomrep,*/ i_ubicord, s_usuadd, s_usumod, i_sucid, i_empid, i_ubicdef);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstUbi = null;
        objlstUbi = new ListModelList<Ubicaciones>();
        lst_ubicaciones.setModel(objlstUbi);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
