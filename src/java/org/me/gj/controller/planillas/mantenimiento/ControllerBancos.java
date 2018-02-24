/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerBancos extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_estado, cb_busqueda;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Textbox txt_busqueda, txt_id, txt_descripcion, txt_siglas, txt_usua, txt_usumo;
    @Wire
    Listbox lst_lista;
    @Wire
    Button btn_buscar;
    @Wire
    Intbox txt_numeracion;
    @Wire
    Checkbox chk_almest;
    String s_estado, campo, s_mensaje;
    int i_sel, valor;
    ListModelList<Bancos> objlstBancos;
    DaoBancos objDaoBancos;
    //Instancias de Objetos
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    Bancos objBancos = new Bancos();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUbicaciones.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstBancos = new ListModelList<Bancos>();
        objDaoBancos = new DaoBancos();
        objlstBancos = objDaoBancos.lstBancos(1);
        lst_lista.setModel(objlstBancos);
        habilitaTab(false, true);

    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101090, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Bancos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Bancos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Banco");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Banco");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Banco");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Banco");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Banco");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Banco");
        }
    }

    @Listen("onSelect=#lst_lista")
    public void seleccion() {
        objBancos = lst_lista.getSelectedItem().getValue();
        if (objBancos == null) {
            objBancos = objlstBancos.get(i_sel + 1);

        }
        i_sel = lst_lista.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objBancos.getId());
        habilitaTab(false, false);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        habilitaCampos(false);
        chk_almest.setDisabled(true);
        chk_almest.setChecked(true);
        txt_descripcion.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (chk_almest.isChecked() && lst_lista.getSelectedIndex() >= 0) {
            //   Messagebox.show("Es una Ubicación por Defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_descripcion.focus();

        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_descripcion.focus();

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_lista.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            //  tbbtn_btn_guardar.setDisabled(true);
                            //   tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            // lst_ubicaciones.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("descripcion")) {
                            txt_descripcion.focus();
                        } else if (campo.equals("siglas")) {
                            txt_siglas.focus();
                        } else if (campo.equals("numeracion")) {
                            txt_numeracion.focus();
                        }
                    }
                }
            });
        } else {
            if (txt_descripcion.getText().matches("^\\s") || txt_descripcion.getText().matches("^\\s+")) {
                Messagebox.show("El Campo Ubicacion no debe tener espacio en blanco al principio");

            } else {
                s_mensaje = "Esta Seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    //ParametrosSalida objParamSalida;
                                    objlstBancos = new ListModelList<Bancos>();
                                    objBancos = (Bancos) generaRegistro();
                                    if (s_estado.equals("N")) {
                                        s_mensaje = objDaoBancos.insertar(objBancos);
                                    } else {
                                        s_mensaje = objDaoBancos.actualizar(objBancos);
                                    }
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    habilitaBotones(false, true);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    VerificarTransacciones();
                                    objlstBancos = objDaoBancos.lstBancos(1);
                                    lst_lista.setModel(objlstBancos);
                                }
                            }
                        });
            }
        }
    }
    
     @Listen("onClick=#tbbtn_btn_imprimir") 
    public void botonImprimir() throws SQLException {
        if (lst_lista == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionBancos.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        int selec = 0;
        int est;
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }

        if (cb_busqueda.getSelectedIndex() == 0) {
            selec = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            selec = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            selec = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        }
        objlstBancos = objDaoBancos.consultar(selec, consulta, est);
        if (objlstBancos.getSize() > 0) {
            lst_lista.setModel(objlstBancos);
        } else {
            objlstBancos = null;
            lst_lista.setModel(objlstBancos);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiaAuditoria();
        limpiarCampos();

    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este banco?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoBancos.eliminar(objBancos);
                                valor = objDaoBancos.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstBancos.clear();
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    objlstBancos = objDaoBancos.lstBancos(1);
                                    lst_lista.setModel(objlstBancos);
                                    lst_lista.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoBancos.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
        }

    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_bancos")
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
            /*case 119:
             if (!tbbtn_btn_imprimir.isDisabled()) {
             botonImprimir();
             }
             break;*/
        }
    }

    @Listen("onOK=#txt_descripcion")
    public void next() {
        txt_siglas.focus();
    }

    @Listen("onOK=#txt_siglas")
    public void next1() {
        txt_numeracion.focus();
    }

    public Bancos generaRegistro() {

        // objBancos = new Bancos();
        // objBancos.setId(txt_id.getValue());
        //  objBancos.setDescripcion(txt_descripcion.getValue());
        String bancokey = objBancos.getId();
        String descripcion = txt_descripcion.getValue().toUpperCase().trim();
        int i_ubicest;
        if (chk_almest.isChecked()) {
            i_ubicest = 1;
        } else {
            i_ubicest = 2;
        }
        String siglas = txt_siglas.getValue().toUpperCase();
        int numeracion = txt_numeracion.getValue();
        return new Bancos(bancokey, descripcion, i_ubicest, siglas, numeracion);

    }

    public String verificar() {
        String men;
        if (txt_descripcion.getValue().isEmpty()) {
            men = "Ingresa descripción";
            campo = "descripcion";
        } else if (txt_siglas.getValue().isEmpty()) {
            men = "Ingresa siglas";
            campo = "siglas";
        } else if (txt_numeracion.getValue() == null) {
            men = "Ingrese numero de digitos de la cuenta";
            campo = "numeracion";
        } else {
            men = "";
        }
        return men;
    }

    //metodos sin utilizar
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_descripcion.setDisabled(b_valida1);
        txt_siglas.setDisabled(b_valida1);
        txt_numeracion.setDisabled(b_valida1);
        chk_almest.setDisabled(b_valida1);
    }

    public void limpiarCampos() {
        chk_almest.setChecked(false);
        txt_descripcion.setValue("");
        txt_siglas.setValue("");
        txt_id.setValue("");
        txt_numeracion.setValue(null);
        cb_estado.setValue("");
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
        txt_descripcion.setValue(objBancos.getDescripcion());
        txt_id.setValue(objBancos.getId());
        if (objBancos.getEstado() == 1) {
            chk_almest.setChecked(true);
        } else {
            chk_almest.setChecked(false);
        }
        txt_siglas.setValue(objBancos.getSiglas());
        txt_numeracion.setValue(objBancos.getNumeracion());
        txt_usua.setValue(objBancos.getUsu_add());
        txt_usumo.setValue(objBancos.getUsu_mod());
        d_fec.setValue(objBancos.getDia_add());
        d_fecmo.setValue(objBancos.getDia_mod());
    }

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }
}
