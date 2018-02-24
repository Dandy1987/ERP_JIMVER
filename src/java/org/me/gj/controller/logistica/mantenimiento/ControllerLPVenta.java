package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class ControllerLPVenta extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_clonacion;
    @Wire
    Textbox txt_busqueda,
            txt_lpvdes, txt_lpvnomrep, txt_usuadd, txt_usumod, txt_lpvid; //;
    @Wire
    Spinner sp_lpvord;
    @Wire
    Combobox cb_busqueda, cb_estado;
    @Wire
    Checkbox chk_lpvest;
    @Wire
    Listbox lst_pventa;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<ListaPrecio> objlstpventa, objlstAux;
    ListaPrecio objLpventas;
    DaoListaPrecios objDaoLpVenta;
    UsuariosCredential cre;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    //Variables publicas
    String s_nombre, s_abrev;
    int valor;
    String s_estado = "Q", s_mensaje = "";
    int i_sel, i_valor, emp_id, suc_id;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential;
    private static final Logger LOGGER = Logger.getLogger(ControllerLPVenta.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_lstpventa")
    public void llenaRegistros() throws SQLException {
        objDaoLpVenta = new DaoListaPrecios();
        objlstpventa = objDaoLpVenta.listaPrecios(emp_id, suc_id, 1, 2);
        lst_pventa.setModel(objlstpventa);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        objUsuCredential = (UsuariosCredential) Sessions.getCurrent().getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new DaoAccesos().Verifica_Permisos_Transacciones(10104020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Lista Precio de Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Lista Precio de Venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una Lista Precio de Venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una Lista Precio de Venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Lista Precio de Venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Lista Precio de Venta");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Lista Precio de Venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Lista Precio de Venta");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la Lista Precio de Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la Lista Precio de Venta");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
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
        objlstpventa = new ListModelList<ListaPrecio>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstpventa = objDaoLpVenta.busquedaLpPrecio(emp_id, suc_id, 2, i_seleccion, s_consulta, i_estado);

        if (objlstpventa.getSize() > 0) {
            lst_pventa.setModel(objlstpventa);
        } else {
            objlstpventa = null;
            lst_pventa.setModel(objlstpventa);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        lst_pventa.setModel(objlstpventa);
        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#lst_pventa")
    public void seleccionaRegistro() throws SQLException {
        objLpventas = (ListaPrecio) lst_pventa.getSelectedItem().getValue();
        if (objLpventas == null) {
            objLpventas = objlstpventa.get(i_sel + 1);
        }
        i_sel = lst_pventa.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objLpventas.getLp_id());
        llenarCampos();
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
        objLpventas = new ListaPrecio();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_lpvest.setDisabled(true);
        chk_lpvest.setChecked(true);
        txt_lpvdes.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_pventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_lpvdes.focus();
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
                        txt_lpvdes.focus();
                    }
                }
            });
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objLpventas = (ListaPrecio) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoLpVenta.insertarListaPrecio(objLpventas);
                                } else {
                                    objParamSalida = objDaoLpVenta.modificarListaPrecio(objLpventas);
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
                                    limpiaAuditoria();
                                    objlstpventa=new ListModelList<ListaPrecio>();
                                    objlstpventa.clear();
                                    objlstpventa = objDaoLpVenta.listaPrecios(emp_id, suc_id, 1, 2);
                                    cb_estado.setSelectedIndex(0);
                                    cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    lst_pventa.setModel(objlstpventa);
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_pventa.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoLpVenta.validaMovimientos(objLpventas);
            valor = objDaoLpVenta.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoLpVenta.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea eliminar esta lista de precios de venta?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida = objDaoLpVenta.eliminarListaPrecio(objLpventas);
                                    if (objParamSalida.getFlagIndicador() == 0) {
                                        objlstpventa.clear();
                                        objlstpventa = objDaoLpVenta.listaPrecios(emp_id, suc_id, 1, 2);
                                        cb_estado.setSelectedIndex(0);
                                        cb_busqueda.setSelectedIndex(0);
                                        txt_busqueda.setValue("%%");
                                        txt_busqueda.setDisabled(true);
                                        lst_pventa.setModel(objlstpventa);
                                        //validacion de eliminacion
                                        tbbtn_btn_eliminar.setDisabled(false);
                                        VerificarTransacciones();
                                        limpiarCampos();
                                        limpiaAuditoria();
                                    }
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_pventa.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
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
        if (objlstpventa == null || objlstpventa.isEmpty()) {
            Messagebox.show("No hay registros de lista de precios de venta para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionLPVenta.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
   /* @Listen("onCtrlKey=#tabbox_lstpventa")
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
    
    @Listen("onCtrlKey=#w_manlin")
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
            objlstpventa = new ListModelList<ListaPrecio>();
            objlstpventa = objDaoLpVenta.listaPrecios(emp_id, suc_id, 0, 2);
            lst_pventa.setModel(objlstpventa);
        }
    }

    @Listen("onClickListaPrecioVenta=#lst_pventa")
    public void goManCliente(ForwardEvent evt) {
        tbbtn_clonacion = (Toolbarbutton) evt.getOrigin().getTarget();
        objlstAux = (ListModelList) lst_pventa.getModel();
        Listitem item = (Listitem) tbbtn_clonacion.getParent().getParent();

        String lp_id = objlstAux.get(item.getIndex()).getLp_id();
        String lp_reporte = objlstAux.get(item.getIndex()).getLp_nomrep();
        String lp_usuadd = objUsuCredential.getCuenta();

        Map objMapObjetos = new HashMap();
        objMapObjetos.put("lp_id", lp_id);
        objMapObjetos.put("lp_reporte", lp_reporte);
        objMapObjetos.put("lp_usuadd", lp_usuadd);
        objMapObjetos.put("lst_pventa", lst_pventa);
        objMapObjetos.put("controlador", "ControllerLPVenta");
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClonacionLisPreVen.zul", null, objMapObjetos);
        window.doModal();
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_lpvid.setValue(objLpventas.getLp_id());
        txt_lpvdes.setValue(objLpventas.getLp_des());
        chk_lpvest.setChecked(objLpventas.isValor());
        txt_lpvnomrep.setValue(objLpventas.getLp_nomrep());
        sp_lpvord.setValue(objLpventas.getLp_ord());
        txt_usuadd.setValue(objLpventas.getLp_usuadd());
        d_fecadd.setValue(objLpventas.getLp_fecadd());
        txt_usumod.setValue(objLpventas.getLp_usumod());
        d_fecmod.setValue(objLpventas.getLp_fecmod());
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

    public String verificar() {
        String s_valor;
        if (txt_lpvdes.getValue().isEmpty() || txt_lpvdes.getText().trim().length() == 0) {
            s_valor = "El Campo Lista es Obligatorio";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiarCampos() {
        txt_lpvid.setValue("");
        txt_lpvdes.setValue("");
        txt_lpvnomrep.setValue("");
        sp_lpvord.setValue(0);
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_lpvdes.setDisabled(b_valida);
        chk_lpvest.setDisabled(b_valida);
        txt_lpvnomrep.setDisabled(b_valida);
        sp_lpvord.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int i_lp_key = txt_lpvid.getValue().isEmpty() ? 0 : Integer.parseInt(txt_lpvid.getValue().trim());
        String s_lp_des = txt_lpvdes.getValue().toUpperCase().trim();
        int i_lp_est = chk_lpvest.isChecked() ? 1 : 2;
        String s_lp_nomrep = txt_lpvnomrep.getValue().toUpperCase().trim();
        int i_lp_ord = sp_lpvord.getValue() == null ? 0 : sp_lpvord.getValue();
        String s_lp_usuadd = objUsuCredential.getCuenta();
        String s_lp_usumod = objUsuCredential.getCuenta();
        return new ListaPrecio(i_lp_key, emp_id, suc_id, s_lp_des, i_lp_est, i_lp_ord, s_lp_nomrep, s_lp_usuadd, s_lp_usumod);
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstpventa = null;
        objlstpventa = new ListModelList<ListaPrecio>();
        lst_pventa.setModel(objlstpventa);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
