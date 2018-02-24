package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
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

public class ControllerMesa extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listamesas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_mesid, txt_mesdes, txt_apenom, txt_mesnomrep, txt_busqueda, txt_supid, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_canaldes, cb_busqueda, cb_busest;
    @Wire
    Spinner sp_mesord;
    @Wire
    Checkbox chk_mesest, chk_busest;
    @Wire
    Listbox lst_mesas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    ListModelList<Mesa> objlstMesas = new ListModelList<Mesa>();
    ListModelList<Canal> objlstCanales = new ListModelList<Canal>();
    DaoMesa objDaoMesas = new DaoMesa();
    DaoCanal objDaoCanales = new DaoCanal();
    DaoSupervisores objDaoSupervisores = new DaoSupervisores();
    Mesa objMesa = new Mesa();
    Utilitarios objUtil = new Utilitarios();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    String campo = "";
    public static boolean bandera;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMesa.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_mesas")
    public void llenaRegistros() throws SQLException {
        limpiarLista();
        objlstMesas = objDaoMesas.listaMesas(1);
        objlstCanales = objDaoCanales.listaCanales(1);
        lst_mesas.setModel(objlstMesas);
        cb_canaldes.setModel(objlstCanales);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        lst_mesas.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40102000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Mesas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Mesas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Mesa");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Mesa");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Mesa");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Mesa");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Mesa");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Mesa");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Mesas");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Mesas");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        limpiarCampos();
        limpiaAuditoria();
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
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstMesas = objDaoMesas.busquedaMesas(i_seleccion, s_consulta, i_estado);

        if (objlstMesas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstMesas.getSize() + " registro(s)");
        }
        if (objlstMesas.getSize() > 0) {
            lst_mesas.setModel(objlstMesas);
        } else {            
            Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
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

    @Listen("onSelect=#lst_mesas")
    public void seleccionaRegistro() throws SQLException {
        objMesa = (Mesa) lst_mesas.getSelectedItem().getValue();
        if (objMesa == null) {
            objMesa = objlstMesas.get(i_sel + 1);
        }
        i_sel = lst_mesas.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objMesa.getMes_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objMesa = new Mesa();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_mesest.setDisabled(true);
        chk_mesest.setChecked(true);
        txt_apenom.setDisabled(true);
        chk_mesest.setLabel("ACTIVO");
        //cb_canaldes.focus();
        txt_mesdes.focus();//setDisabled(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_mesas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            /*if (objDaoMesas.busquedaExistencia(1, txt_mesid.getValue()) > 0 || objDaoMesas.busquedaExistencia(2, txt_mesid.getValue()) > 0) {
             Messagebox.show("No se puede Editar, el canal esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             } else {*/
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
          //  txt_mesdes.setDisabled(true);
            txt_apenom.setDisabled(true);
            cb_canaldes.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            //}
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
                        if (campo.equals("Canal")) {
                            cb_canaldes.select();
                            cb_canaldes.focus();
                        } else if (campo.equals("Supervisor")) {
                            txt_supid.focus();
                        }
                    }
                }
            });
        } else {
            if (!txt_supid.getValue().matches("[0-9]*")) {
                lst_mesas.focus();
            } else {
                int supid = Integer.parseInt(txt_supid.getValue());
                Supervisor objSup = objDaoSupervisores.getNomSupervisor(supid);
                if (objSup == null) {
                    lst_mesas.focus();
                } else {
                    s_mensaje = "Esta Seguro que desea guardar los cambios?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        objMesa = (Mesa) generaRegistro();
                                        ParametrosSalida objParamSalida;
                                        if (s_estado.equals("N")) {
                                            objParamSalida = objDaoMesas.insertar(objMesa);
                                        } else {
                                            objParamSalida = objDaoMesas.actualizar(objMesa);
                                        }

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
                                            objlstMesas.clear();
                                            objlstMesas = objDaoMesas.listaMesas(1);
                                            lst_mesas.setModel(objlstMesas);
                                            objMesa = new Mesa();
                                        }
                                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        lst_mesas.focus();
                                    }
                                }
                            });
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_mesas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_mesas.focus();
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta mesa?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoMesas.eliminar(objMesa);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    limpiarLista();
                                    limpiaAuditoria();
                                    limpiarCampos();
                                    OnChange();
                                    objlstMesas = objDaoMesas.listaMesas(1);
                                    lst_mesas.setModel(objlstMesas);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
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
                            limpiaAuditoria();

                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            busquedaRegistros();
                            lst_mesas.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstMesas == null || objlstMesas.isEmpty()) {
            Messagebox.show("No hay registros de Mesas para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_mesas.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no encontro registro para imprimir");
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionMesa.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manmen")
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
            objlstMesas = new ListModelList<Mesa>();
            objlstMesas = objDaoMesas.listaMesas(0);
            lst_mesas.setModel(objlstMesas);
        }
    }

    @Listen("onOK=#txt_supid")
    public void busquedaSupervisor() {
        if (bandera == false) {
            bandera = true;
            if (txt_supid.getValue().isEmpty()) {
                //Mostrar la Ventana de busqueda de proveedores
                modoEjecucion = "mantMesa";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_supid", txt_supid);
                objMapObjetos.put("txt_apenom", txt_apenom);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerZona");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSupervisores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_mesnomrep.focus();
            }
        }
    }

    @Listen("onBlur=#txt_supid")
    public void validaSupervisor() throws SQLException {
        if (txt_supid.getValue().isEmpty()) {
        	limpiarSupervisor();
        } else {
            txt_apenom.setValue("");
            if (!txt_supid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        	limpiarSupervisor();
                            txt_supid.focus();
                        }
                    }
                });
            } else {
                Supervisor objSup;
                int supid = Integer.parseInt(txt_supid.getValue());
                objSup = objDaoSupervisores.getNomSupervisor(supid);
                if (objSup == null) {
                    Messagebox.show("Supervisor no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            	limpiarSupervisor();
                                txt_supid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Supervisor con codigo " + supid + " y ha encontrado 1 registro(s)");
                    txt_supid.setValue(Utilitarios.lpad(String.valueOf(objSup.getSup_key()), 4, "0"));
                    txt_apenom.setValue(objSup.getSup_apenom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#cb_canaldes")
    public void validaCanal() {
        txt_supid.focus();
    }

    @Listen("onOK=#txt_mesnomrep")
    public void validaReporte() {
        sp_mesord.focus();
    }

    @Listen("onCheck=#chk_mesest")
    public void VerificaEstado() throws SQLException {
        int i_tabest = objMesa.getMes_est();
        if (objDaoMesas.busquedaExistencia(1, txt_mesid.getValue()) > 0 || objDaoCanales.busquedaExistencia(2, txt_mesid.getValue()) > 0) {
            if (i_tabest == 1) {
                chk_mesest.setChecked(true);
                chk_mesest.setLabel("ACTIVO");
                Messagebox.show("La mesa esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                chk_mesest.setChecked(false);
                chk_mesest.setLabel("INACTIVO");
                Messagebox.show("Ya no se puede activar la mesa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    //Eventos Otros
    public String verificar() {
        String s_validacion;
        if (cb_canaldes.getSelectedIndex() == -1) {
            s_validacion = "El campo 'CANAL' es obligatorio";
            campo = "Canal";

        } else if (txt_supid.getValue().isEmpty()) {
            s_validacion = "El campo 'SUPERVISOR' es obligatorio";
            campo = "Supervisor";
        } else {
            s_validacion = "";
        }
        return s_validacion;
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
    }

    public void limpiarCampos() {
        txt_mesid.setValue("");
        txt_mesdes.setValue("");
        chk_mesest.setChecked(true);
        chk_mesest.setLabel("ACTIVO");
        cb_canaldes.setSelectedIndex(-1);
        txt_supid.setValue(null);
        txt_apenom.setValue("");
        txt_mesnomrep.setValue("");
        sp_mesord.setValue(0);
    }
    
    public void limpiarSupervisor(){
        txt_supid.setValue("");
        txt_apenom.setValue("");
    }

    public void limpiarLista() {
        //limpio mi lista
        objMesa = null;
        objlstMesas = new ListModelList<Mesa>();
        lst_mesas.setModel(objlstMesas);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenarCampos() {
        txt_mesid.setValue(objMesa.getMes_id());
        txt_mesdes.setValue(objMesa.getMes_des());
        chk_mesest.setChecked(objMesa.isValor());
        chk_mesest.setLabel(objMesa.isValor() ? "ACTIVO" : "INACTIVO");
        cb_canaldes.setSelectedItem(Utilitarios.valorPorTexto(cb_canaldes, objMesa.getMes_canalid()));
        txt_supid.setValue(objMesa.getSup_id());
        txt_apenom.setValue(objMesa.getSup_apenom());
        txt_mesnomrep.setValue(objMesa.getMes_nomrep());
        sp_mesord.setValue(objMesa.getMes_ord());
        txt_usuadd.setValue(objMesa.getMes_usuadd());
        d_fecadd.setValue(objMesa.getMes_fecadd());
        txt_usumod.setValue(objMesa.getMes_usumod());
        d_fecmod.setValue(objMesa.getMes_fecmod());

    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamesas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamesas.setDisabled(b_valida1);
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
        chk_mesest.setDisabled(b_valida1);
        txt_mesdes.setDisabled(b_valida1);
        cb_canaldes.setDisabled(b_valida1);
        txt_supid.setDisabled(b_valida1);
        txt_apenom.setDisabled(b_valida1);
        sp_mesord.setDisabled(b_valida1);
        txt_mesnomrep.setDisabled(b_valida1);
        
    }

    public Object generaRegistro() {
        int i_meskey = objMesa.getMes_key();
        String i_mesid = objMesa.getMes_id();
        String s_mesdes = txt_mesdes.getValue().toUpperCase();
        int i_canalid = cb_canaldes.getSelectedItem().getValue();
        int i_supkey = Integer.parseInt(txt_supid.getValue());
        String s_supid = txt_supid.getValue();
        int i_mesest;
        if (chk_mesest.isChecked()) {
            i_mesest = 1;
        } else {
            i_mesest = 2;
        }
        int i_empid = objUsuCredential.getCodemp();
        int i_sucid = objUsuCredential.getCodsuc();
        String s_mesnomrep = txt_mesnomrep.getValue().toUpperCase();
        int i_mesord = sp_mesord.getValue();
        String s_mesusuadd = objUsuCredential.getCuenta();
        String s_mesusumod = objUsuCredential.getCuenta();
        return new Mesa(i_meskey, i_mesid, s_mesdes, i_canalid, s_mesnomrep, i_mesord, s_mesusuadd, s_mesusumod, i_sucid, i_empid,
                i_supkey, s_supid, i_mesest);
    }

}
