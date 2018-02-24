package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ProvPresupuesto;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerProvPresupuesto extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaprovpresu, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_provpresu, txt_descrip, txt_provid, txt_provdes, txt_desabre, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_busest;

    @Wire
    Checkbox chk_estado, chk_busest;
    @Wire
    Listbox lst_provpresu;
    @Wire
    Datebox d_fecadd, d_fecmod;

    //Instancias de Objetos
    ListModelList<ProvPresupuesto> objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
    DaoProvPresupuesto objDaoProvPresupuesto = new DaoProvPresupuesto();
    ProvPresupuesto objProvPresupuesto = new ProvPresupuesto();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();

    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q";
    String s_mensaje = "";
    private String campo = "";
    public static boolean bandera;

    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProvPresupuesto.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_provpresu")
    public void llenaRegistros() throws SQLException {
        objlstProvPresupuesto = objDaoProvPresupuesto.listaProvPresupuestoes(1);
        lst_provpresu.setModel(objlstProvPresupuesto);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        cb_busqueda.focus();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
         Session sess = Sessions.getCurrent();
         objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
         int usuario = objUsuCredential.getCodigo();
         int empresa = objUsuCredential.getCodemp();
         int sucursal = objUsuCredential.getCodsuc();
         objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40111000, usuario, empresa, sucursal);
         if (objUsuCredential.getRol() == 1) {
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de provpresu con el rol: ADMINISTRADOR");
         } else {
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de provpresu con el rol: USUARIO NORMAL");
         }
         if (objAccesos.getAcc_ins() == 1) {
         tbbtn_btn_nuevo.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creacion de un nuevo proveedor con presupuesto");
         } else {
         tbbtn_btn_nuevo.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creacion de un nuevo proveedor con presupuesto");
         }
         if (objAccesos.getAcc_mod() == 1) {
         tbbtn_btn_editar.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edicion de un proveedor con presupuesto");
         } else {
         tbbtn_btn_editar.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edicion de un proveedor con presupuesto");
         }
         if (objAccesos.getAcc_eli() == 1) {
         tbbtn_btn_eliminar.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminacion de un proveedor con presupuesto");
         } else {
         tbbtn_btn_eliminar.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminacion de un proveedor con presupuesto");
         }
        /* if (objAccesos.getAcc_imp() == 1) {
         tbbtn_btn_imprimir.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de proveedor con presupuesto");
         } else {
         tbbtn_btn_imprimir.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de proveedor con presupuesto");
         }*/
    }


    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {

        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().replace("0", "").trim();;
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
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del proveedor con presupuesto " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el nombre del proveedor con presupuesto " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion abreviada " + s_consulta + " para su busqueda");
        }

        objlstProvPresupuesto = objDaoProvPresupuesto.busquedaProvPresupuestoes(i_seleccion, s_consulta, i_estado);

        if (objlstProvPresupuesto.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstProvPresupuesto.getSize() + " registro(s)");
        }
        
        if (objlstProvPresupuesto.getSize() > 0) {
            lst_provpresu.setModel(objlstProvPresupuesto);
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

    @Listen("onSelect=#lst_provpresu")
    public void seleccionaRegistro() throws SQLException {
        objProvPresupuesto = (ProvPresupuesto) lst_provpresu.getSelectedItem().getValue();
        if (objProvPresupuesto == null) {
            objProvPresupuesto = objlstProvPresupuesto.get(i_sel + 1);
        }
        i_sel = lst_provpresu.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProvPresupuesto.getProvpresu_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objProvPresupuesto = new ProvPresupuesto();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_estado.setDisabled(true);
        chk_estado.setChecked(true);
        txt_provid.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_provpresu.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de proveedor con presupuesto para editar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_provid.focus();
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
                       validafocos();
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
                                objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
                                objProvPresupuesto = (ProvPresupuesto) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoProvPresupuesto.insertarProvPresupuesto(objProvPresupuesto);
                                } else {
                                    s_mensaje = objDaoProvPresupuesto.actualizarProvPresupuesto(objProvPresupuesto);
                                }
                                Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                habilitaBotones(false, true);
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //

                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                limpiaAuditoria();
                                objlstProvPresupuesto = objDaoProvPresupuesto.listaProvPresupuestoes(1);
                                lst_provpresu.setModel(objlstProvPresupuesto);
                                objProvPresupuesto = new ProvPresupuesto();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_provpresu.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro para eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            ParametrosSalida objParametrosSalida = new DaoProvPresupuesto().validamovimiento(objProvPresupuesto.getProvpresu_key());
            if (objParametrosSalida.getFlagIndicador() > 0) {
                Messagebox.show("No se puede eliminar, " + objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {
                s_mensaje = "Esta seguro que desea eliminar este proveedor con presupuesto?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    String msg = objDaoProvPresupuesto.eliminarProvPresupuesto(objProvPresupuesto);
                                    limpiarLista();
                                    objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
                                    objlstProvPresupuesto = objDaoProvPresupuesto.listaProvPresupuestoes(1);
                                    lst_provpresu.setModel(objlstProvPresupuesto);
                                    limpiarCampos();
                                    Messagebox.show(msg,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    //
                                }
                            }
                        });
            }
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
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_provpresu.setSelectedIndex(-1);
                            //
                            llenaRegistros();
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manprovpresu")
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
        }
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
            objlstProvPresupuesto = objDaoProvPresupuesto.listaProvPresupuestoes(0);
            lst_provpresu.setModel(objlstProvPresupuesto);
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstProvPresupuesto == null || objlstProvPresupuesto.isEmpty()) {
            Messagebox.show("No hay registros de proveedor con presupuesto para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionProvPresupuesto.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onOK=#txt_descrip")
    public void onOKcanal() {
        txt_desabre.focus();
    }

    @Listen("onOK=#txt_provid")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "MantProvPresu";
                String tipo = "1";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerProvPresupuesto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_descrip.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedorBusq() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numericos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_provid.setValue("");
                                    txt_provdes.setValue("");
                                    txt_provid.focus();
                                }
                            }
                        });
            } else {
                String prov_id = txt_provid.getValue();
                txt_provid.setValue(Utilitarios.lpad(prov_id, 8, "0"));
                Proveedores objProveedor = new DaoProveedores().BusquedaProveedor(txt_provid.getValue(), "1");
                if (objProveedor == null) {
                    s_mensaje = "El codigo de proveedor no existe o esta inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_provid.setValue("");
                                        txt_provdes.setValue("");
                                        txt_provid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        }
        bandera = false;
    }

    //Eventos Otros
    public String verificar() {
        String mensaje;
        if (txt_provid.getValue().isEmpty() || txt_provdes.getValue().isEmpty()) {
            mensaje = "El campo 'Proveedor' es obligatorio";
            campo = "prov";
        } else if (!txt_provid.getValue().matches("[0-9]*")) {
            mensaje = "En el campo 'Proveedor' debe ingresar valores numericos";
            campo = "prov";
        } else if (txt_descrip.getValue().isEmpty()) {
            mensaje = "El campo 'Descripcion' es obligatorio";
            campo = "desc";
        } else if (txt_descrip.getValue().matches("(\\s)+")) {
            mensaje = "El campo 'Descripcion' , no debe tener espacios en blancos al principio";
            campo = "desc";
            txt_descrip.setValue("");
        } else if (txt_descrip.getValue().matches("([^\\w])+")) {
            mensaje = "En el campo 'Descripcion', no se permite caracteres especiales";
            campo = "desc";
            txt_descrip.setValue("");
        } else if (txt_desabre.getValue().isEmpty()) {
            mensaje = "El campo 'Desc.Abreviada' es obligatorio";
            campo = "desabre";
        } else if (txt_desabre.getValue().matches("(\\s)+")) {
            mensaje = "El campo 'Desc.Abreviada' , no debe tener espacios en blancos al principio";
            campo = "desabre";
            txt_descrip.setValue("");
        } else if (txt_desabre.getValue().matches("([^\\w])+")) {
            mensaje = "En el campo 'Desc.Abreviada', no se permite caracteres especiales";
            campo = "desabre";
            txt_desabre.setValue("");
        } else {
            mensaje = "";
        }
        return mensaje;
    }
    
    public void validafocos(){
    	 if (campo.equals("prov")) {
             txt_provid.focus();
         } else if (campo.equals("desc")) {
             txt_descrip.focus();
         } else if (campo.equals("desabre")) {
             txt_desabre.focus();
         }
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setText("%%");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarLista() {
        objlstProvPresupuesto = null;
        objlstProvPresupuesto = new ListModelList<ProvPresupuesto>();
        lst_provpresu.setModel(objlstProvPresupuesto);
    }

    public void llenarCampos() {
        txt_provpresu.setValue(objProvPresupuesto.getProvpresu_id());

        txt_descrip.setValue(objProvPresupuesto.getProvpresu_des());
        chk_estado.setChecked(objProvPresupuesto.isValor());
        chk_estado.setLabel(objProvPresupuesto.isValor() ? "ACTIVO" : "INACTIVO");
        txt_desabre.setValue(objProvPresupuesto.getProvpresu_desabre());
        txt_provid.setValue(objProvPresupuesto.getProvpresu_provid());
        txt_provdes.setValue(objProvPresupuesto.getProvpresu_provdes());
        txt_usuadd.setValue(objProvPresupuesto.getProvpresu_usuadd());
        d_fecadd.setValue(objProvPresupuesto.getProvpresu_fecadd());
        txt_usumod.setValue(objProvPresupuesto.getProvpresu_usumod());
        d_fecmod.setValue(objProvPresupuesto.getProvpresu_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprovpresu.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaprovpresu.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_estado.setChecked(true);
        chk_estado.setLabel("ACTIVO");
        txt_provpresu.setValue("");
        txt_descrip.setValue("");
        txt_desabre.setValue("");
        txt_provid.setValue("");
        txt_provdes.setValue("");
    }

    public void habilitaCampos(boolean b_valida) {
        txt_descrip.setDisabled(b_valida);
        txt_provid.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
        txt_desabre.setDisabled(b_valida);
    }

    public Object generaRegistro() {

        int i_provpresukey = txt_provpresu.getValue().isEmpty() ? 0 : Integer.parseInt(txt_provpresu.getValue());
        String s_propresu_des = txt_descrip.getValue().toUpperCase().trim();
        String s_propresu_desabre = txt_desabre.getValue().toUpperCase().trim();
        int i_provkey = Integer.parseInt(txt_provid.getValue().toString());
        String s_provpresu_usuadd = objUsuCredential.getCuenta();
        String s_provpresu_usumod = objUsuCredential.getCuenta();
        int i_provpresu_est = chk_estado.isChecked() ? 1 : 2;

        return new ProvPresupuesto(i_provpresukey, s_propresu_des, s_propresu_desabre, i_provkey, i_provpresu_est, s_provpresu_usuadd, s_provpresu_usumod);
    }

}
