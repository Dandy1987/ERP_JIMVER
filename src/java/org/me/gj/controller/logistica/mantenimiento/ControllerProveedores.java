package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.logistica.mantenimiento.ProvCont;
import org.me.gj.model.logistica.mantenimiento.ProvDet;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

public class ControllerProveedores extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento, tab_otros;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Toolbarbutton tbbtn_btn_nuevoc, tbbtn_btn_editarc, tbbtn_btn_eliminarc,
            tbbtn_btn_guardarc, tbbtn_btn_deshacerc, tbbtn_btn_imprimirc, tbbtn_btn_buscarc;
    @Wire
    Toolbarbutton tbbtn_btn_nuevot, tbbtn_btn_editart, tbbtn_btn_eliminart,
            tbbtn_btn_guardart, tbbtn_btn_deshacert, tbbtn_btn_imprimirt, tbbtn_btn_buscart;
    @Wire
    Textbox txt_busqueda,
            txt_provsiglas, txt_provrazsoc, txt_provdir, txt_provcla, txt_provweb,
            txt_procon_nom, txt_procon_ape, txt_procon_ema, txt_provnom_rep, txt_procon_cargo, txt_procon_suc, txt_usuadd, txt_usumod,
            txtid, txtrazsoc, txtdireccion, txtruc;
    @Wire
    Intbox txt_procon_id,
            txt_prodet_id;
    @Wire
    Longbox txt_provkey, txt_provruc, txt_provrucext, txt_procon_telf1, txt_procon_telf2, txt_prodet_fax, txt_prodet_tel, txt_prodet_cel;
    @Wire
    Spinner spi_provord;
    @Wire
    Combobox cb_busqueda,
            cbx_provtip, cbx_provcond,
            cbx_prov_cont,
            cbx_prov_det,
            cb_estado;
    @Wire
    Checkbox chk_prov_est, chk_retencion, chk_normal, chk_bcliente, chk_percepcion, chk_detraccion, chk_relacionado,
            chk_prov_con,
            chk_prov_det;
    @Wire
    Listbox lst_proveedores, lst_prov_cont, lst_prov_det;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Proveedores> objlstprov;
    ListModelList<ProvCont> objlstprovcont, objlstEliCont;
    ListModelList<ProvDet> objlstprovdet, objlstEliDet;
    ListModelList<Proveedores> objListaProv;
    ProvCont objProvCont;
    ProvDet objProvDet;
    DaoProveedores objDaoProv;
    Proveedores objProveedores;
    Utilitarios objUtil;
    Accesos obj_Accesos;
    //Variables publicas   
    private String s_estado = "Q", s_estado_contacto = "Q", s_estado_telefono = "Q", s_mensaje = "";
    private String campo = "";
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProveedores.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        objDaoProv = new DaoProveedores();
        //carga de lista de proveedores
        objlstprov = objDaoProv.listaProveedores("1");
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
        lst_proveedores.setModel(objlstprov);
        //carga de combos condiciones de pago y tipo de proveedor
        cbx_provcond.setModel(new DaoCondicion().lstCondicionProv());
        objlstprovcont = new ListModelList<ProvCont>();
        objlstprovdet = new ListModelList<ProvDet>();
        objlstEliCont = new ListModelList<ProvCont>();
        objlstEliDet = new ListModelList<ProvDet>();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        obj_Accesos = new DaoAccesos().Verifica_Permisos_Transacciones(10102000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Proveedores con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Proveedores con el rol: USUARIO NORMAL");
        }
        if (obj_Accesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Proveedor");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Proveedor");
        }
        if (obj_Accesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Proveedor");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Proveedor");
        }
        if (obj_Accesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Proveedor");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Proveedor");
        }
        if (obj_Accesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Proveedores");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Proveedores");
        }

    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
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
        objlstprov = new ListModelList<Proveedores>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        }
        if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la razon social " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la dirección " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el ruc " + s_consulta + " para su busqueda");
        }
        objlstprov = objDaoProv.busquedaProveedores(i_seleccion, s_consulta, i_estado, "%%");
        if (objlstprov.getSize() > 0) {
            lst_proveedores.setModel(objlstprov);
        } else {
            objlstprov = null;
            lst_proveedores.setModel(objlstprov);
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

    @Listen("onSelect=#lst_proveedores")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        objProveedores = (Proveedores) lst_proveedores.getSelectedItem().getValue();
        if (objProveedores == null) {
            objProveedores = objlstprov.get(i_sel + 1);
        }
        i_sel = lst_proveedores.getSelectedIndex();
        //cargamos las listas con los contactos y telefonos de los proveedores
        objlstprovcont.clear();
        objlstprovdet.clear();
        objlstprovcont = objDaoProv.listaContactos(objProveedores.getProv_key());
        objlstprovdet = objDaoProv.listaTelefonos(objProveedores.getProv_key());
        lst_prov_cont.setModel(objlstprovcont);
        lst_prov_det.setModel(objlstprovdet);
        //cargamos los campos
        limpiarCampos();
        limpiarCamposContacto();
        limpiarCamposTelefono();
        llenarCampos(objProveedores);
        //solo lectura
        //activamos contactos y telefonos
        tab_otros.setDisabled(false);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProveedores.getProv_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        habilitaBotones(false, true);
        habilitaBotonesContacto(true, false);
        habilitaBotonesTelefono(true, false);
        //Limpias los Campos de Todos los Submantenimientos
        limpiarCampos();
        limpiarCamposContacto();
        limpiarCamposTelefono();
        //Habilita los Campos 
        habilitaCampos(true);
        //limpiamos las listas de Contactos y Telefonos
        objlstprovcont.clear();
        objlstprovdet.clear();
        lst_prov_cont.setModel(objlstprovcont);
        lst_prov_det.setModel(objlstprovdet);
        habilitaTab(false, true);
        seleccionaTab(false, true);
        chk_detraccion.setDisabled(true);
        chk_prov_est.setDisabled(true);
        s_estado = "N";
        txt_provruc.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_proveedores.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un Proveedor a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitaBotones(false, true);
            habilitaBotonesContacto(true, false);
            habilitaBotonesTelefono(true, false);
            habilitaCampos(true);
            habilitaTab(false, true);
            seleccionaTab(false, true);
            s_estado = "M";
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {

                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotones(true, false);
                            habilitaBotonesContacto(false, false);
                            habilitaBotonesTelefono(false, false);
                            limpiarCampos();
                            limpiarCamposContacto();
                            limpiarCamposTelefono();
                            habilitaCampos(false);
                            habilitaCamposContacto(false);
                            habilitaCamposTelefono(false);
                            seleccionaTab(true, false);
                            habilitaTab(true, true);
                            //validacion de deshacer
                            VerificarTransacciones();
                            objlstprovcont.clear();
                            objlstprovdet.clear();
                            objlstEliCont.clear();
                            objlstEliDet.clear();
                            lst_prov_cont.setModel(objlstprovcont);
                            lst_prov_det.setModel(objlstprovdet);
                            lst_proveedores.setSelectedIndex(-1);
                            s_estado_telefono = "Q";
                            s_estado_contacto = "Q";
                            s_estado = "Q";
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Ruc")) {
                            txt_provruc.focus();
                        } else if (campo.equals("Siglas")) {
                            txt_provsiglas.focus();
                        } else if (campo.equals("Razon Social")) {
                            txt_provrazsoc.focus();
                        } else if (campo.equals("Tipo")) {
                            cbx_provtip.focus();
                        } else if (campo.equals("Direccion")) {
                            txt_provdir.focus();
                        } else if (campo.equals("Cond. de Pago")) {
                            cbx_provcond.focus();
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
                                objlstprovcont.addAll(objlstEliCont);
                                objlstprovdet.addAll(objlstEliDet);
                                objProveedores = (Proveedores) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoProv.insertarProveedor(objProveedores, getListaContactos(objlstprovcont), getListaTelefonos(objlstprovdet));
                                } else {
                                    objParamSalida = objDaoProv.actualizarProveedor(objProveedores, getListaContactos(objlstprovcont), getListaTelefonos(objlstprovdet));
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    habilitaBotones(true, false);
                                    habilitaBotonesContacto(false, false);
                                    habilitaBotonesTelefono(false, false);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    limpiarCamposContacto();
                                    limpiarCamposTelefono();
                                    habilitaCampos(false);
                                    habilitaCamposContacto(false);
                                    habilitaCamposTelefono(false);
                                    seleccionaTab(true, false);
                                    habilitaTab(true, true);
                                    VerificarTransacciones();
                                    objlstprov.clear();
                                    objlstprovcont.clear();
                                    objlstprovdet.clear();
                                    objlstEliCont.clear();
                                    objlstEliDet.clear();
                                    objlstprov = objDaoProv.listaProveedores("1");
                                    lst_proveedores.setModel(objlstprov);
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    s_estado_telefono = "Q";
                                    s_estado_contacto = "Q";
                                    s_estado = "Q";

                                }
                                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | " + objParamSalida.getMsgValidacion());
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_proveedores.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un Proveedor a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoProv.validaMovimientos(objProveedores);
            valor = objDaoProv.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoProv.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Esta Seguro que desea Eliminar el Proveedor";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida;
                                    objProveedores.setProv_usumod(objUsuCredential.getCuenta());
                                    objParamSalida = objDaoProv.eliminar(objProveedores);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        limpiarCampos();
                                        limpiaAuditoria();
                                        limpiarCamposContacto();
                                        limpiarCamposTelefono();
                                        VerificarTransacciones();
                                        objlstprov.clear();
                                        objlstprovcont.clear();
                                        objlstprovdet.clear();
                                        lst_prov_cont.setModel(objlstprovcont);
                                        lst_prov_det.setModel(objlstprovdet);
                                        objlstprov = objDaoProv.listaProveedores("1");
                                        lst_proveedores.setModel(objlstprov);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        s_estado_telefono = "Q";
                                        s_estado_contacto = "Q";
                                        s_estado = "Q";
                                    }
                                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Eliminado un Registro");
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        });
            }
        }
    }

    @Listen("onSelect=#lst_prov_cont")
    public void seleccionaContacto() throws SQLException {
        objProvCont = (ProvCont) lst_prov_cont.getSelectedItem().getValue();
        //cargamos los datos del contacto
        llenarCamposContacto(objProvCont);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProvCont.getProcon_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevoc")
    public void botonNuevoContacto() {
        limpiarCamposContacto();
        habilitaCamposContacto(true);
        habilitaBotonesContacto(false, true);
        s_estado_contacto = "N";
        chk_prov_con.setDisabled(true);
        Utilitarios.deshabilitarLista(true, lst_prov_cont);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro de contacto");
    }

    @Listen("onClick=#tbbtn_btn_editarc")
    public void botonModificaContacto() {
        if (lst_prov_cont.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un Contacto a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitaCamposContacto(true);
            habilitaBotonesContacto(false, true);
            s_estado_contacto = "M";
            Utilitarios.deshabilitarLista(true, lst_prov_cont);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro de contacto");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardarc")
    public void AgregaContactos() {
        String cadena = verificarContacto();
        if (!cadena.isEmpty()) {
            Messagebox.show("por favor verfique los datos en el campo " + cadena);
        } /*   else if (!"".equals(verificardatos())) {
         Messagebox.show("El Campo" + verificardatos() + " es inválido", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } */ else {
            if (s_estado_contacto.equals("N")) {
                objProvCont = (ProvCont) generaProveedorContacto();
                objProvCont.setInd_accion("N");
                objlstprovcont.add(objProvCont);
            } else {
                if (objProvCont.getInd_accion().equals("N")) {
                    objProvCont = (ProvCont) generaProveedorContacto();
                    objProvCont.setInd_accion("N");
                } else {
                    objProvCont = (ProvCont) generaProveedorContacto();
                    objProvCont.setInd_accion("M");
                }
                objlstprovcont.set(lst_prov_cont.getSelectedIndex(), objProvCont);
            }
            objlstprovcont.clearSelection();
            lst_prov_cont.setModel(objlstprovcont);
            Utilitarios.deshabilitarLista(false, lst_prov_cont);
            habilitaBotonesContacto(true, false);
            limpiarCamposContacto();
            habilitaCamposContacto(false);
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacerc")
    public void botonDeshacerContacto() {
        String s_str = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesContacto(true, false);
                            limpiarCamposContacto();
                            habilitaCamposContacto(false);
                            objlstprovcont.clearSelection();
                            Utilitarios.deshabilitarLista(false, lst_prov_cont);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios en contacto");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_eliminarc")
    public void botonEliminaContacto() {
        if (s_estado.equals("M") && !objProvCont.getInd_accion().equals("N")) {
            objProvCont.setInd_accion("E");
            objlstEliCont.add(objProvCont);
        }
        objlstprovcont.remove(lst_prov_cont.getSelectedIndex());
        limpiarCamposContacto();
        lst_prov_cont.setModel(objlstprovcont);
    }

    @Listen("onSelect=#lst_prov_det")
    public void seleccionaTelefono() throws SQLException {
        objProvDet = (ProvDet) lst_prov_det.getSelectedItem().getValue();
        //cargamos los datos del contacto
        llenarCamposTelefono(objProvDet);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProvDet.getProdet_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevot")
    public void botonNuevoTelefono() {
        limpiarCamposTelefono();
        habilitaCamposTelefono(true);
        habilitaBotonesTelefono(false, true);
        s_estado_telefono = "N";
        chk_prov_det.setDisabled(true);
        Utilitarios.deshabilitarLista(true, lst_prov_det);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro de contacto");
    }

    @Listen("onClick=#tbbtn_btn_editart")
    public void botonModificaTelefono() {
        if (lst_prov_det.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un Registro de Telefonos a modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            habilitaCamposTelefono(true);
            habilitaBotonesTelefono(false, true);
            s_estado_telefono = "M";
            Utilitarios.deshabilitarLista(true, lst_prov_det);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro de contacto");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardart")
    public void AgregaTelefonos() throws SQLException {
        String cadena = verificarTelefono();
        if (!cadena.isEmpty()) {
            Messagebox.show("por favor verfique los datos en el campo " + cadena);
        } else {

            if (s_estado_telefono.equals("N")) {
                objProvDet = (ProvDet) generaProveedorDetalle();
                objProvDet.setInd_accion("N");
                objlstprovdet.add(objProvDet);
            } else {
                if (objProvDet.getInd_accion().equals("N")) {
                    objProvDet = (ProvDet) generaProveedorDetalle();
                    objProvDet.setInd_accion("N");
                } else {
                    objProvDet = (ProvDet) generaProveedorDetalle();
                    objProvDet.setInd_accion("M");
                }
                objlstprovdet.set(lst_prov_det.getSelectedIndex(), objProvDet);
            }
            objlstprovdet.clearSelection();
            lst_prov_det.setModel(objlstprovdet);
            Utilitarios.deshabilitarLista(false, lst_prov_det);
            habilitaBotonesTelefono(true, false);
            limpiarCamposTelefono();
            habilitaCamposTelefono(false);
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacert")
    public void botonDeshacerTelefono() {
        String s_str = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {

                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesTelefono(true, false);
                            limpiarCamposTelefono();
                            habilitaCamposTelefono(false);
                            objlstprovdet.clearSelection();
                            Utilitarios.deshabilitarLista(false, lst_prov_det);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios de telefono");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_eliminart")
    public void botonEliminaTelefono() {
        if (s_estado.equals("M") && !objProvDet.getInd_accion().equals("N")) {
            objProvDet.setInd_accion("E");
            objlstEliDet.add(objProvDet);
        }
        objlstprovdet.remove(lst_prov_det.getSelectedIndex());
        limpiarCamposTelefono();
        lst_prov_det.setModel(objlstprovdet);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstprov == null || objlstprov.isEmpty()) {
            Messagebox.show("No hay Registros de Proveedores para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionProv.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    /*@Listen("onCtrlKey=#tabbox_proveedores")
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
    }*/
    
    @Listen("onCtrlKey=#w_proveedores")
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
            objlstprov = new ListModelList<Proveedores>();
            objlstprov = new DaoProveedores().listaProveedores("0");
            lst_proveedores.setModel(objlstprov);
        }
    }

    @Listen("onChange=#txtid;onChange=#txtrazsoc;onChange=#txtdireccion;onChange=#txtruc")
    public void changeFilter() {
        objListaProv = new ListModelList<Proveedores>();
        lst_proveedores.setModel(getFilterPersons(objListaProv));
    }

    //Eventos Otros 
    public String verificardatosc() {
        String mensaje;
        double telefono, celular, fax, telefono2, telefono1;
        telefono = (txt_prodet_tel.getValue() != null ? txt_prodet_tel.getValue() : 0);
        celular = (txt_prodet_cel.getValue() != null ? txt_prodet_cel.getValue() : 0);
        fax = (txt_prodet_fax.getValue().toString() != null ? txt_prodet_fax.getValue() : 0);
        telefono1 = (txt_procon_telf1.getValue().toString() != null ? txt_procon_telf1.getValue() : 0);
        telefono2 = (txt_procon_telf2.getValue().toString() != null ? txt_procon_telf2.getValue() : 0);
        if (telefono >= 10000000 || telefono <= 1000000) {
            mensaje = " telefono";
        } else if (celular >= 1000000000 || celular <= 100000000) {
            mensaje = " celular";
        } else if (fax <= 10) {
            mensaje = " fax";
        } else {
            mensaje = "";
        }
        return mensaje;
    }

    public ListModelList<Proveedores> getFilterPersons(ListModelList<Proveedores> objListaProv) {
        for (int i = 0; i < objlstprov.getSize(); i++) {
            Proveedores prov;
            prov = ((Proveedores) objlstprov.get(i));
            if (prov.getProv_id().toString().contains(txtid.getValue())) {
                if (prov.getProv_razsoc().toString().contains(txtrazsoc.getValue().toUpperCase())) {
                    if (prov.getProv_dir().toString().contains(txtdireccion.getValue().toUpperCase())) {
                        if (prov.getProv_ruc().toString().contains(txtruc.getValue())) {
                            objListaProv.add(prov);
                        }
                    }
                }
            }
        }
        if (objListaProv.size() > 0) {
            return objListaProv;
        } else {
            return objlstprov;
        }
    }

    public Object[][] getListaContactos(ListModelList<ProvCont> objlstAux) {
        ListModelList<ProvCont> objlstTrans;
        if (s_estado.equals("M")) {
            objlstTrans = new ListModelList<ProvCont>();
            for (int i = 0; i < objlstAux.size(); i++) {
                if (!objlstAux.get(i).getInd_accion().equals("Q")) {
                    objlstTrans.add(objlstAux.get(i));
                }
            }
        } else {
            objlstTrans = objlstAux;
        }
        Object[][] listaContactos = new Object[objlstTrans.size()][13];
        for (int i = 0; i < objlstTrans.size(); i++) {
            listaContactos[i][0] = objlstTrans.get(i).getProv_key();
            listaContactos[i][1] = objlstTrans.get(i).getProcon_id();
            listaContactos[i][2] = objlstTrans.get(i).getProcon_nom();
            listaContactos[i][3] = objlstTrans.get(i).getProcon_ape();
            listaContactos[i][4] = objlstTrans.get(i).getProcon_ema();
            listaContactos[i][5] = objlstTrans.get(i).getProcon_telef1();
            listaContactos[i][6] = objlstTrans.get(i).getProcon_telef2() == 0 ? null : objlstTrans.get(i).getProcon_telef2();
            listaContactos[i][7] = objlstTrans.get(i).getProcon_cargo();
            listaContactos[i][8] = objlstTrans.get(i).getProcon_suc();
            listaContactos[i][9] = objlstTrans.get(i).getProcon_est();
            listaContactos[i][10] = objlstTrans.get(i).getProcon_usuadd();
            listaContactos[i][11] = objlstTrans.get(i).getProcon_usumod();
            listaContactos[i][12] = objlstTrans.get(i).getInd_accion();
        }
        return listaContactos;
    }

    public Object[][] getListaTelefonos(ListModelList<ProvDet> objlstAux) {
        ListModelList<ProvDet> objlstTelf;
        if (s_estado.equals("M")) {
            objlstTelf = new ListModelList<ProvDet>();
            for (int i = 0; i < objlstAux.size(); i++) {
                if (!objlstAux.get(i).getInd_accion().equals("Q")) {
                    objlstTelf.add(objlstAux.get(i));
                }
            }
        } else {
            objlstTelf = objlstAux;
        }
        Object[][] listaTelefonos = new Object[objlstTelf.size()][9];
        for (int i = 0; i < objlstTelf.size(); i++) {
            listaTelefonos[i][0] = objlstTelf.get(i).getProv_key();
            listaTelefonos[i][1] = objlstTelf.get(i).getProdet_id();
            listaTelefonos[i][2] = objlstTelf.get(i).getProdet_tel();
            listaTelefonos[i][3] = objlstTelf.get(i).getProdet_cel();
            listaTelefonos[i][4] = objlstTelf.get(i).getProdet_fax();
            listaTelefonos[i][5] = objlstTelf.get(i).getProdet_est();
            listaTelefonos[i][6] = objlstTelf.get(i).getProdet_usuadd();
            listaTelefonos[i][7] = objlstTelf.get(i).getProdet_usumod();
            listaTelefonos[i][8] = objlstTelf.get(i).getInd_accion();
        }
        return listaTelefonos;
    }

    public Object generaRegistro() {
        long l_prov_ruc, l_prov_rucext, i_prov_key;
        String s_prov_razsoc, s_prov_siglas, s_prov_dir, s_prov_web, s_prov_clave, s_prov_usuadd, s_prov_usumod, s_prov_nomrep;
        int i_con_id, i_prov_tip, i_prov_normal, i_prov_reten, i_prov_buenc, i_prov_percep, i_prov_detrac, i_prov_rel, i_prov_ord, i_prov_est;
        l_prov_ruc = txt_provruc.getValue();
        i_prov_key = txt_provkey.getValue() == null ? 0 : objProveedores.getProv_key();
        l_prov_rucext = txt_provrucext.getValue() == null ? 0 : txt_provrucext.getValue();
        i_prov_est = chk_prov_est.isChecked() ? 1 : 2;
        s_prov_razsoc = txt_provrazsoc.getValue().toUpperCase();
        s_prov_siglas = txt_provsiglas.getValue().toUpperCase();
        s_prov_dir = txt_provdir.getValue().toUpperCase();
        s_prov_web = txt_provweb.getValue().toUpperCase();
        s_prov_usuadd = objUsuCredential.getCuenta();
        s_prov_usumod = objUsuCredential.getCuenta();
        s_prov_nomrep = txt_provnom_rep.getValue().toUpperCase();
        s_prov_clave = txt_provcla.getValue();
        i_con_id = cbx_provcond.getSelectedItem().getValue();
        i_prov_tip = Integer.parseInt(cbx_provtip.getSelectedItem().getValue().toString());
        i_prov_normal = chk_normal.isChecked() ? 1 : 0;
        i_prov_reten = chk_retencion.isChecked() ? 1 : 0;
        i_prov_buenc = chk_bcliente.isChecked() ? 1 : 0;
        i_prov_percep = chk_percepcion.isChecked() ? 1 : 0;
        i_prov_detrac = chk_detraccion.isChecked() ? 1 : 0;
        i_prov_rel = chk_relacionado.isChecked() ? 1 : 0;
        i_prov_ord = spi_provord.getValue();
        return new Proveedores(i_prov_key, i_con_id, l_prov_ruc, s_prov_razsoc, s_prov_siglas, s_prov_dir, i_prov_tip, l_prov_rucext, s_prov_web, s_prov_clave, i_prov_est, i_prov_normal, i_prov_reten, i_prov_buenc, i_prov_percep, i_prov_detrac, i_prov_rel, s_prov_usuadd, s_prov_usumod, s_prov_nomrep, i_prov_ord);
    }

    public String verificar() {
        String s_valor;
        if (txt_provruc.getValue() == null) {
            s_valor = "El Campo Ruc es Obligatorio";
            campo = "Ruc";
        } else if (txt_provruc.getText().length() != 11) {
            s_valor = "El Campo Ruc debe tener 11 caracteres";
            campo = "Ruc";
        } else if (txt_provsiglas.getValue().isEmpty() || txt_provsiglas.getText().trim().length() == 0) {
            s_valor = "El Campo Siglas es Obligatorio";
            campo = "Siglas";
        } else if (txt_provrazsoc.getValue().isEmpty() || txt_provrazsoc.getText().trim().length() == 0) {
            s_valor = "El Campo Razon Social es Obligatorio";
            campo = "Razon Social";
        } else if (cbx_provtip.getSelectedIndex() == -1) {
            s_valor = "El Campo Tipo es Obligatorio";
            campo = "Tipo";
        } else if (txt_provdir.getValue().isEmpty() || txt_provdir.getText().trim().length() == 0) {
            s_valor = "El Campo Direccion es Obligatorio";
            campo = "Direccion";
        } else if (cbx_provcond.getSelectedIndex() == -1) {
            s_valor = "El Campo Cond. de Pago es Obligatorio";
            campo = "Cond. de Pago";
        } else if (txt_provruc.getValue().toString().length() != 11) {
            s_valor = "El Campo Ruc es Invalido";
            campo = "Ruc";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public Object generaProveedorContacto() {
        long prov_key = txt_provkey.getValue() == null ? 0 : txt_provkey.getValue();
        int procon_id = txt_procon_id.getValue() == null ? 0 : txt_procon_id.getValue();
        String procon_nom = txt_procon_nom.getValue().toUpperCase();
        String procon_ape = txt_procon_ape.getValue().toUpperCase();
        String procon_ema = txt_procon_ema.getValue().toUpperCase();
        long procon_telef1 = txt_procon_telf1.getValue();
        long procon_telef2 = txt_procon_telf2.getValue() == null ? 0 : txt_procon_telf2.getValue();
        String procon_cargo = txt_procon_cargo.getValue().toUpperCase();
        String procon_suc = txt_procon_suc.getValue().toUpperCase();
        int procon_est = chk_prov_con.isChecked() ? 1 : 2;
        String procon_usuadd = objUsuCredential.getCuenta();
        String procon_usumod = objUsuCredential.getCuenta();
        return new ProvCont(procon_id, prov_key, procon_nom, procon_ape, procon_ema, procon_cargo, procon_suc, procon_usuadd, procon_usumod, procon_telef1, procon_telef2, procon_est);

    }

    public Object generaProveedorDetalle() {
        long prov_key = txt_provkey.getValue() == null ? 0 : txt_provkey.getValue();
        int prodet_id = txt_prodet_id.getValue() == null ? 0 : txt_prodet_id.getValue();
        int prodet_est = chk_prov_det.isChecked() ? 1 : 2;
        long prodet_tel = txt_prodet_tel.getValue();
        long prodet_cel = txt_prodet_cel.getValue();
        long prodet_fax = txt_prodet_fax.getValue();
        String prodet_usuadd = objUsuCredential.getCuenta();
        String prodet_usumod = objUsuCredential.getCuenta();
        return new ProvDet(prodet_id, prov_key, prodet_tel, prodet_cel, prodet_fax, prodet_usuadd, prodet_usumod, prodet_est);
    }

    public String verificarContacto() {
        String s_valor;
        if (txt_procon_nom.getValue().isEmpty()) {
            s_valor = "Nombres";
            txt_procon_nom.focus();
        } else if (txt_procon_ape.getValue().isEmpty()) {
            s_valor = "Apellidos";
            txt_procon_ape.focus();
        } else if (txt_procon_ema.getValue().isEmpty()) {
            s_valor = "Email";
            txt_procon_ema.focus();
        } else if (txt_procon_telf1.getValue() == null) {
            s_valor = "1er Telefono";
            txt_procon_telf1.focus();
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public String verificarTelefono() {
        String s_valor;
        if (txt_prodet_tel.getValue() == null) {
            s_valor = "Telefono";
        } else if (txt_prodet_cel.getValue() == null) {
            s_valor = "Celular";
        } else if (txt_prodet_fax.getValue() == null) {
            s_valor = "Fax";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void llenarCampos(Proveedores objProve) {
        txt_provkey.setValue(objProve.getProv_key());
        txt_provruc.setValue(objProve.getProv_ruc());
        txt_provrucext.setValue(objProve.getProv_rucext() == 0 ? null : objProve.getProv_rucext());
        txt_provsiglas.setValue(objProve.getProv_siglas());
        txt_provdir.setValue(objProve.getProv_dir());
        txt_provrazsoc.setValue(objProve.getProv_razsoc());
        txt_provcla.setValue(objProve.getProv_clave());
        txt_provweb.setValue(objProve.getProv_web());
        txt_provnom_rep.setValue(objProve.getProv_nomrep());
        cbx_provtip.setSelectedItem(Utilitarios.valorPorTexto(cbx_provtip, objProve.getProv_tip()));
        cbx_provcond.setSelectedItem(Utilitarios.valorPorTexto(cbx_provcond, objProve.getCon_key()));
        spi_provord.setValue(objProve.getProv_ord());
        chk_percepcion.setChecked(objProve.getProv_percep() == 1);
        chk_detraccion.setChecked(objProve.getProv_detrac() == 1);
        chk_retencion.setChecked(objProve.getProv_reten() == 1);
        chk_relacionado.setChecked(objProve.getProv_rel() == 1);
        chk_bcliente.setChecked(objProve.getProv_buenc() == 1);
        chk_normal.setChecked(objProve.getProv_normal() == 1);
        chk_prov_est.setChecked(objProve.getProv_est() == 1);
        chk_prov_est.setLabel(objProve.getProv_est() == 1 ? "ACTIVO" : "INACTIVO");
        txt_usuadd.setValue(objProve.getProv_usuadd());
        d_fecadd.setValue(objProve.getProv_fecadd());
        txt_usumod.setValue(objProve.getProv_usumod());
        d_fecmod.setValue(objProve.getProv_fecmod());
    }

    public void llenarCamposContacto(ProvCont objProvCont) {
        chk_prov_con.setChecked(objProvCont.getProcon_est() == 1);
        chk_prov_con.setLabel(objProvCont.getProcon_est() == 1 ? "ACTIVO" : "INACTIVO");
        txt_procon_id.setValue(objProvCont.getProcon_id());
        txt_procon_nom.setValue(objProvCont.getProcon_nom());
        txt_procon_ape.setValue(objProvCont.getProcon_ape());
        txt_procon_ema.setValue(objProvCont.getProcon_ema());
        txt_procon_cargo.setValue(objProvCont.getProcon_cargo());
        txt_procon_suc.setValue(objProvCont.getProcon_suc());
        txt_procon_telf1.setValue(objProvCont.getProcon_telef1());
        txt_procon_telf2.setValue(objProvCont.getProcon_telef2() == 0 ? null : objProvCont.getProcon_telef2());
    }

    public void llenarCamposTelefono(ProvDet objProvDet) {
        chk_prov_det.setChecked(objProvDet.getProdet_est() == 1);
        chk_prov_det.setLabel(objProvDet.getProdet_est() == 1 ? "ACTIVO" : "INACTIVO");
        txt_prodet_id.setValue(objProvDet.getProdet_id());
        txt_prodet_tel.setValue(objProvDet.getProdet_tel());
        txt_prodet_cel.setValue(objProvDet.getProdet_cel());
        txt_prodet_fax.setValue(objProvDet.getProdet_fax());
    }

    public void limpiarCampos() {
        txt_provkey.setValue(null);
        txt_provruc.setValue(null);
        txt_provrucext.setValue(null);
        txt_provsiglas.setValue("");
        txt_provdir.setValue("");
        txt_provrazsoc.setValue("");
        txt_provcla.setValue("");
        txt_provweb.setValue("");
        txt_provnom_rep.setValue("");
        spi_provord.setValue(0);
        cbx_provtip.setSelectedIndex(-1);
        cbx_provcond.setSelectedIndex(-1);
        cbx_provtip.setDisabled(true);
        cbx_provcond.setDisabled(true);
        chk_percepcion.setDisabled(true);
        chk_percepcion.setChecked(false);
        chk_retencion.setDisabled(true);
        chk_retencion.setChecked(false);
        chk_relacionado.setDisabled(true);
        chk_relacionado.setChecked(false);
        chk_bcliente.setDisabled(true);
        chk_bcliente.setChecked(false);
        chk_normal.setDisabled(true);
        chk_normal.setChecked(false);
        chk_prov_est.setChecked(true);
        chk_prov_est.setLabel("ACTIVO");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCamposContacto() {
        txt_procon_id.setValue(null);
        txt_procon_nom.setValue("");
        txt_procon_ape.setValue("");
        txt_procon_ema.setValue("");
        txt_procon_cargo.setValue("");
        txt_procon_suc.setValue("");
        txt_procon_telf1.setValue(null);
        txt_procon_telf2.setValue(null);
        chk_prov_con.setChecked(true);
        chk_prov_con.setLabel("ACTIVO");
    }

    public void limpiarCamposTelefono() {
        txt_prodet_id.setValue(null);
        txt_prodet_tel.setValue(null);
        txt_prodet_cel.setValue(null);
        txt_prodet_fax.setValue(null);
        chk_prov_det.setChecked(true);
        chk_prov_det.setLabel("ACTIVO");
    }

    public void habilitaCampos(boolean b_valida) {
        txt_provruc.setDisabled(!b_valida);
        txt_provrucext.setDisabled(!b_valida);
        txt_provsiglas.setDisabled(!b_valida);
        txt_provdir.setDisabled(!b_valida);
        txt_provrazsoc.setDisabled(!b_valida);
        txt_provcla.setDisabled(!b_valida);
        txt_provweb.setDisabled(!b_valida);
        txt_provnom_rep.setDisabled(!b_valida);
        spi_provord.setDisabled(!b_valida);
        cbx_provtip.setDisabled(!b_valida);
        cbx_provcond.setDisabled(!b_valida);
        chk_percepcion.setDisabled(!b_valida);
        chk_retencion.setDisabled(!b_valida);
        chk_relacionado.setDisabled(!b_valida);
        chk_bcliente.setDisabled(!b_valida);
        chk_normal.setDisabled(!b_valida);
        chk_detraccion.setDisabled(!b_valida);
        chk_prov_est.setDisabled(!b_valida);
    }

    public void habilitaCamposContacto(boolean b_valida) {
        chk_prov_con.setDisabled(!b_valida);
        txt_procon_nom.setDisabled(!b_valida);
        txt_procon_ape.setDisabled(!b_valida);
        txt_procon_ema.setDisabled(!b_valida);
        txt_procon_cargo.setDisabled(!b_valida);
        txt_procon_suc.setDisabled(!b_valida);
        txt_procon_telf1.setDisabled(!b_valida);
        txt_procon_telf2.setDisabled(!b_valida);
    }

    public void habilitaCamposTelefono(boolean b_valida) {
        chk_prov_det.setDisabled(!b_valida);
        txt_prodet_tel.setDisabled(!b_valida);
        txt_prodet_cel.setDisabled(!b_valida);
        txt_prodet_fax.setDisabled(!b_valida);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(!b_valida1);
        tbbtn_btn_editar.setDisabled(!b_valida1);
        tbbtn_btn_eliminar.setDisabled(!b_valida1);
        tbbtn_btn_guardar.setDisabled(!b_valida2);
        tbbtn_btn_deshacer.setDisabled(!b_valida2);
    }

    public void habilitaBotonesContacto(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoc.setDisabled(!b_valida1);
        tbbtn_btn_editarc.setDisabled(!b_valida1);
        tbbtn_btn_eliminarc.setDisabled(!b_valida1);
        tbbtn_btn_guardarc.setDisabled(!b_valida2);
        tbbtn_btn_deshacerc.setDisabled(!b_valida2);
    }

    public void habilitaBotonesTelefono(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevot.setDisabled(!b_valida1);
        tbbtn_btn_editart.setDisabled(!b_valida1);
        tbbtn_btn_eliminart.setDisabled(!b_valida1);
        tbbtn_btn_guardart.setDisabled(!b_valida2);
        tbbtn_btn_deshacert.setDisabled(!b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_mantenimiento.setSelected(!b_valida1);
        tab_lista.setSelected(!b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(!b_valida1);
        tab_mantenimiento.setDisabled(!b_valida2);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstprov = null;
        objlstprov = new ListModelList<Proveedores>();
        lst_proveedores.setModel(objlstprov);
    }

    //metodos sin utilizar
    public void llenaRegistros() throws SQLException {
    }

    public void OnChange() {
    }

    public void llenarCampos() {
    }
}
