package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManConceptos;
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
public class ControllerManConceptos extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_estado, cb_tipo_buscar, cb_busqueda, cb_tipo, cb_prioridad, cb_boleta;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Textbox txt_debe_codigo, txt_debe_descrip, txt_haber_codigo, txt_haber_descrip,
            txt_codigo_sunat, txt_busqueda, txt_codigo, txt_descripcion, txt_usua, txt_usumo;
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
    ListModelList<ManConceptos> objlstConceptos;
    DaoManConceptos objDaoConceptos;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;

    ManConceptos objConceptos = new ManConceptos();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManConceptos.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstConceptos = new ListModelList<ManConceptos>();
        objDaoConceptos = new DaoManConceptos();
        cb_estado.setSelectedIndex(0);
        objlstConceptos = objDaoConceptos.lstTablas();
        lst_lista.setModel(objlstConceptos);
        habilitaTab(false, true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101070, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Conceptos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Conceptos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Conceptos");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Conceptos");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Conceptos");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Conceptos");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Conceptos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Conceptos");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        int selec = 0, tipo = 0;
        int est;
        //if para bucar por filtro estado
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }
        //i if para buscar por filtro tipo
        if (cb_tipo_buscar.getSelectedIndex() == 0) {
            tipo = 0;//" ";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_tipo_buscar.getSelectedIndex() == 1) {
            tipo = 1;//" and t.tabla_tipo1 = 'F'";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + consulta + " para su busqueda");
        } else if (cb_tipo_buscar.getSelectedIndex() == 2) {
            tipo = 2;//" and t.tabla_tipo1 = 'C'";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        } else if (cb_tipo_buscar.getSelectedIndex() == 3) {
            tipo = 3;//" and t.tabla_tipo1 = 'M'";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        }
        //if para buscar por id o descripcion
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

        objlstConceptos = objDaoConceptos.consultar(selec, consulta, est, tipo);
        if (objlstConceptos.getSize() > 0) {
            lst_lista.setModel(objlstConceptos);
        } else {
            objlstConceptos = null;
            lst_lista.setModel(objlstConceptos);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiaAuditoria();
        limpiarCampos();

    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        habilitaCampos(false);

        chk_almest.setChecked(true);
        txt_codigo.setDisabled(true);
        chk_almest.setDisabled(true);
        cb_tipo.setSelectedIndex(0);
        cb_prioridad.setSelectedIndex(1);
        cb_boleta.setSelectedIndex(0);
        txt_descripcion.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
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
                            //  tbbtn_btn_guardar.setDisabled(true);
                            //   tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            VerificarTransacciones();
                            // lst_ubicaciones.focus();
                            //
                            // habilitaCampos(true);
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
                        }
                    }
                }
            });
        } else {
            if (txt_descripcion.getText().matches("^\\s") || txt_descripcion.getText().matches("^\\s+")) {
                Messagebox.show("El Campo Ubicacion no debe tener espacio en blanco al principio");
            }
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                //ParametrosSalida objParamSalida;
                                objlstConceptos = new ListModelList<ManConceptos>();
                                objConceptos = (ManConceptos) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoConceptos.insertar(objConceptos);
                                } else {
                                    s_mensaje = objDaoConceptos.modificar(objConceptos);
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                VerificarTransacciones();
                                //actualiza la lista 
                                objlstConceptos = objDaoConceptos.lstTablas();
                                lst_lista.setModel(objlstConceptos);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
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
            txt_codigo.setDisabled(true);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_descripcion.focus();
            txt_codigo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onSelect=#lst_lista")
    public void seleccion() {
        objConceptos = lst_lista.getSelectedItem().getValue();
        if (objConceptos == null) {
            objConceptos = objlstConceptos.get(i_sel + 1);

        }
        i_sel = lst_lista.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objConceptos.getCodigo());
        habilitaTab(false, false);
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
                                s_mensaje = objDaoConceptos.eliminar(objConceptos);
                                valor = objDaoConceptos.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstConceptos.clear();

                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    objlstConceptos = objDaoConceptos.lstTablas();
                                    lst_lista.setModel(objlstConceptos);
                                    lst_lista.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoConceptos.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
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
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionConceptos.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //boton enter
    @Listen("onOK=#txt_descripcion")
    public void next() {
        cb_tipo.focus();
    }

    @Listen("onOK=#cb_tipo")
    public void next1() {
        cb_prioridad.focus();
    }

    @Listen("onOK=#cb_prioridad")
    public void next2() {
        txt_debe_codigo.focus();
    }

    @Listen("onOK=#txt_debe_codigo")
    public void next3() {
        txt_haber_codigo.focus();
    }

    @Listen("onOK=#txt_haber_codigo")
    public void next4() {
        cb_boleta.focus();
    }

    @Listen("onOK=#cb_boleta")
    public void next5() {
        txt_codigo_sunat.focus();
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

    //Eventos Otros 
    @Listen("onCtrlKey=#w_conceptos")
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
    
    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
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

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    public void habilitaCampos(boolean b_valida1) {

        txt_descripcion.setDisabled(b_valida1);
        cb_tipo.setDisabled(b_valida1);
        cb_prioridad.setDisabled(b_valida1);
        txt_debe_codigo.setDisabled(b_valida1);
        txt_haber_codigo.setDisabled(b_valida1);
        cb_boleta.setDisabled(b_valida1);
        txt_codigo_sunat.setDisabled(b_valida1);
        chk_almest.setDisabled(b_valida1);

    }

    public void limpiarCampos() {
        txt_codigo.setValue("");
        txt_descripcion.setValue("");
        // cb_tipo.setValue("");
        //cb_prioridad.setValue("");
        txt_debe_codigo.setValue("");
        txt_debe_descrip.setValue("");
        txt_haber_codigo.setValue("");
        txt_haber_descrip.setValue("");
        // cb_boleta.setValue("");
        txt_codigo_sunat.setValue("");

    }

    public String verificar() {
        String valor1;
        if (txt_descripcion.getValue().isEmpty()) {
            valor1 = "Ingresa descripcion";
            campo = "descripcion";
        } else {
            valor1 = "";
        }

        return valor1;

    }

    //genere registro a guardar o modificar
    public ManConceptos generaRegistro() {
        int estado;
        String codigo = objConceptos.getCodigo();
        String descripcion = txt_descripcion.getValue();
        String tipo = cb_tipo.getValue();
        int prioridad = cb_prioridad.getSelectedIndex();//cb_prioridad.getValue();
        String debe = txt_debe_codigo.getValue();
        String haber = txt_haber_codigo.getValue();
        //   String boleta = cb_boleta.getValue();
        int boleta = cb_boleta.getSelectedIndex();
        String sunat = txt_codigo_sunat.getValue();
        if (chk_almest.isChecked()) {
            estado = 1;
        } else {
            estado = 2;
        }

        return new ManConceptos(codigo, descripcion, tipo, prioridad, debe, haber, boleta, sunat, estado);

    }

    //compleat los campos al seleccionar la lista princiipal
    public void llenarCampos() {
        txt_codigo.setValue(objConceptos.getCodigo());
        txt_descripcion.setValue(objConceptos.getDescripcion());
        cb_tipo.setValue(objConceptos.getS_tipo());
        cb_prioridad.setValue(objConceptos.getS_prioridad());
        txt_debe_codigo.setValue(objConceptos.getDebe());
        txt_haber_codigo.setValue(objConceptos.getHaber());
        cb_boleta.setValue(objConceptos.getV_boleta());
        txt_codigo_sunat.setValue(objConceptos.getCod_sunat());
        txt_usua.setValue(objConceptos.getUsu_add());
        txt_usumo.setValue(objConceptos.getUsu_mod());
        d_fec.setValue(objConceptos.getDia_add());
        d_fecmo.setValue(objConceptos.getDia_mod());
        if (objConceptos.getEstado() == 1) {
            chk_almest.setChecked(true);
        } else {
            chk_almest.setChecked(false);
        }
    }
}
