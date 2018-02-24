package org.me.gj.controller.logistica.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.apache.log4j.Logger;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Executions;

public class ControllerAlmacenes extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_buscar;
    @Wire
    Doublebox txt_almanc, txt_almlar, txt_almalt;
    @Wire
    Textbox txt_busqueda, txt_almdes, txt_almdirecc/*, txt_almnomrep*/, txt_almid, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_almtip, cb_busqueda, cb_estado;
    @Wire
    Spinner sp_almord;
    @Wire
    Checkbox chk_almdef, chk_almvta, chk_almest, chk_almcom;
    @Wire
    Listbox lst_almacenes;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Almacenes> objlstAlmacen;
    ListModelList<TipAlm> objlstTipoAlmacen;
    Almacenes objAlmacen; /*= new Almacenes();*/

    DaoAlmacenes objDaoAlmacen;
    Accesos objAccesos;/* = new Accesos();*/

    DaoAccesos objDaoAccesos;/* = new DaoAccesos();*/

    Utilitarios objUtil;
    //Variables publicas
    int i_sel;
    int valor;
    int i_empid;
    int i_sucid;
    String s_estado = "Q";
    String s_mensaje = "";
    String campo = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAlmacenes.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_almacenes")
    public void llenaRegistros() throws SQLException {
        i_empid = objUsuCredential.getCodemp();
        i_sucid = objUsuCredential.getCodsuc();
        objUtil = new Utilitarios();
        objlstAlmacen = new ListModelList<Almacenes>();
        objDaoAlmacen = new DaoAlmacenes();
        objlstAlmacen = objDaoAlmacen.lstAlmacenes(1);
        lst_almacenes.setModel(objlstAlmacen);
        objlstTipoAlmacen = new ListModelList<TipAlm>();
        objlstTipoAlmacen = new DaoTipAlm().listaTipoAlmacen(1);
        cb_almtip.setModel(objlstTipoAlmacen);
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
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10106010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Almacenes con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Almacenes con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo almacen");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo almacen");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un almacen");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un almacen");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un almacen");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un almacen");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de almacenes");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de almacenes");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        //String s_consulta = "";
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstAlmacen = new ListModelList<Almacenes>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la dirección " + s_consulta + " para su busqueda");

        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción del tipo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la altura " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el ancho " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
            //LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el largo " + s_consulta + " para su busqueda");
        }
        objlstAlmacen = new DaoAlmacenes().busquedaAlmacenes(s_consulta, i_seleccion, i_estado);
        if (objlstAlmacen.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstAlmacen.getSize() + " registro(s)");
        }

        if (objlstAlmacen.getSize() > 0) {
            lst_almacenes.setModel(objlstAlmacen);
        } else {
            objlstAlmacen = null;
            lst_almacenes.setModel(objlstAlmacen);
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

    @Listen("onSelect=#lst_almacenes")
    public void seleccionaRegistro() throws SQLException {
        if (lst_almacenes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un almacen", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objAlmacen = new Almacenes();
            objAlmacen = lst_almacenes.getSelectedItem().getValue();
            if (objAlmacen == null) {
                objAlmacen = objlstAlmacen.get(i_sel + 1);
            }
            i_sel = lst_almacenes.getSelectedIndex();

            chk_almest.setChecked(objAlmacen.getAlm_est() == 1);
            txt_almid.setValue(objAlmacen.getAlm_id());
            txt_almdes.setValue(objAlmacen.getAlm_des());
            txt_almdirecc.setValue(objAlmacen.getAlm_direcc());
            cb_almtip.setSelectedItem(Utilitarios.valorPorTexto(cb_almtip, objAlmacen.getAlm_tip()));
            sp_almord.setValue(objAlmacen.getAlm_ord());
            //txt_almnomrep.setValue(objAlmacen.getAlm_nomrep());
            txt_almalt.setValue(objAlmacen.getAlm_alt());
            txt_almanc.setValue(objAlmacen.getAlm_anc());
            txt_almlar.setValue(objAlmacen.getAlm_lar());
            chk_almdef.setChecked(objAlmacen.getAlm_def() == 1);
            chk_almvta.setChecked(objAlmacen.getAlm_vta() == 1);
            chk_almcom.setChecked(objAlmacen.getAlm_com() == 1);
            txt_usuadd.setValue(objAlmacen.getAlm_usuadd());
            d_fecadd.setValue(objAlmacen.getAlm_fecadd());
            txt_usumod.setValue(objAlmacen.getAlm_usumod());
            d_fecmod.setValue(objAlmacen.getAlm_fecmod());

            habilitaTab(false, false);

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objAlmacen.getAlm_id());
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        objAlmacen = new Almacenes();
        limpiarCampos();
        seleccionaTab(false, true);
        tab_lista.setDisabled(true);
        habilitaTab(true, false);
        habilitaBotones(true, false);
        habilitaCampos(false);
        tbbtn_btn_imprimir.setDisabled(true);
        cb_almtip.setSelectedIndex(0);
        chk_almest.setChecked(true);
        chk_almest.setDisabled(true);
        txt_almdes.focus();

        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_almacenes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
//            s_estado = "M";
            s_estado = "E";
//            seleccionaRegistro();
            seleccionaTab(false, true);
//            habilitaTab(false, true);
            habilitaTab(true, false);
            habilitaCampos(false);
            txt_almdes.focus();
            habilitaBotones(true, false);
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Almacen")) {
                            txt_almdes.setValue("");
                            txt_almdes.focus();
                        } else if (campo.equals("Direccion")) {
                            txt_almdirecc.setValue("");
                            txt_almdirecc.focus();
                        } else if (campo.equals("Tipo")) {
                            cb_almtip.focus();
                        } else if (campo.equals("Alto")) {
                            txt_almalt.setValue(null);
                            txt_almalt.focus();
                        } else if (campo.equals("Ancho")) {
                            txt_almanc.setValue(null);
                            txt_almanc.focus();
                        } else if (campo.equals("Largo")) {
                            txt_almlar.setValue(null);
                            txt_almlar.focus();
                        }
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
                                objAlmacen = generaregistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoAlmacen.insertar(objAlmacen);
                                } else {
                                    s_mensaje = objDaoAlmacen.modificar(objAlmacen);
                                }
                                valor = objDaoAlmacen.i_flagErrorBD;
                                if (valor > 0) {
                                    Messagebox.show(objDaoAlmacen.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    cb_busqueda.setSelectedIndex(0);
                                    cb_estado.setSelectedIndex(0);
                                    //txt_busqueda.setValue("%%");
                                    objlstAlmacen = new ListModelList<Almacenes>();
                                    objlstAlmacen = objDaoAlmacen.lstAlmacenes(1);
                                    lst_almacenes.setModel(objlstAlmacen);
                                    limpiarCampos();
                                    //tab_lista.setSelected(true);
                                    //tab_lista.setDisabled(false);

                                    seleccionaTab(true, false);
                                    habilitaTab(false, true);
                                    habilitaBotones(false, true);
                                    habilitaCampos(true);
                                    //validacion de guardar/nuevo
                                    VerificarTransacciones();
                                }
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_almacenes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoAlmacen.validaMovimientos(objAlmacen);
            valor = objDaoAlmacen.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoAlmacen.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String s_str = "Está seguro que desea eliminar el almacen";
                Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoAlmacen.eliminar(objAlmacen);
                                    valor = objDaoAlmacen.i_flagErrorBD;
                                    if (valor == 0) {
                                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        objlstAlmacen = new ListModelList<Almacenes>();
                                        //validacion de eliminacion
                                        //tbbtn_btn_eliminar.setDisabled(false);
                                        seleccionaTab(true, false);
                                        habilitaBotones(false, true);

                                        VerificarTransacciones();
                                        cb_busqueda.setSelectedIndex(0);
                                        cb_estado.setSelectedIndex(0);
                                        //txt_busqueda.setValue("%%");
                                        objlstAlmacen = objDaoAlmacen.lstAlmacenes(1);
                                        lst_almacenes.setModel(objlstAlmacen);
                                        limpiarCampos();
                                        //tab_lista.setSelected(true);

                                        limpiaAuditoria();
                                    } else {
                                        Messagebox.show(objDaoAlmacen.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            /*tab_lista.setSelected(true);
                             tab_lista.setDisabled(false);
                             tab_mantenimiento.setSelected(false);*/
                            lst_almacenes.clearSelection();
                            seleccionaTab(true, false);
                            habilitaTab(false, true);
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                                
                            limpiarCampos();

                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstAlmacen == null || objlstAlmacen.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionAlm.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#tabbox_almacenes")
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

    @Listen("onOK=#txt_almanc;onBlur=#txt_almanc")
    public void onOk_txt_almanc() {
        if (txt_almanc.getValue() != null) {
            double d_numero = txt_almanc.getValue();
            double d_resultado = objUtil.FormatoNumero(d_numero, 4);
            txt_almanc.setValue(d_resultado);
            txt_almlar.focus();
        }
    }

    @Listen("onOK=#txt_almlar;onBlur=#txt_almlar")
    public void onOk_txt_almlar() {
        if (txt_almlar.getValue() != null) {
            //double d_numero = txt_almlar.getValue();
            double d_resultado = objUtil.FormatoNumero(txt_almlar.getValue(), 3);
            txt_almlar.setValue(d_resultado);
            txt_almalt.focus();
        }
    }

    @Listen("onOK=#txt_almalt;onBlur=#txt_almalt")
    public void onOk_txt_almalt() {
        if (txt_almalt.getValue() != null) {
            //double d_numero = txt_almalt.getValue();
            double d_resultado = objUtil.FormatoNumero(txt_almalt.getValue(), 3);
            txt_almalt.setValue(d_resultado);
            sp_almord.focus();
        }
    }

    @Listen("onOK=#sp_almord")
    public void onOK_sp_AlmOrd() {
        if (sp_almord.getValue() != null) {
            chk_almdef.focus();
        }
    }

    @Listen("onOK=#chk_almdef")
    public void onOK_chk_almDef() {
        chk_almvta.focus();
    }

    @Listen("onOK=#chk_almvta")
    public void onOK_chk_almVta() {
        chk_almcom.focus();
    }

    @Listen("onOK=#chk_almcom")
    public void onOK_chk_almCom() throws SQLException {
        botonGuardar();
    }

    @Listen("onCheck=#chk_almvta")
    public void onAlmacenVenta() {
        if (chk_almcom.isChecked()) {
            chk_almcom.setChecked(false);
        }
    }

    @Listen("onCheck=#chk_almcom")
    public void onAlmacenCompra() {
        if (chk_almvta.isChecked()) {
            chk_almvta.setChecked(false);
        }
    }

    @Listen("onOK=#txt_almdes")
    public void nextAlmDirec() throws SQLException {
        if (!txt_almdes.getValue().isEmpty()) {
            txt_almdirecc.focus();
        }
    }

    @Listen("onOK=#txt_almdirecc")
    public void nextAlmTip() throws SQLException {
        if (!txt_almdirecc.getValue().isEmpty()) {
            cb_almtip.focus();
        }
    }

    @Listen("onOK=#cb_almtip")
    public void nextAlmAnc() throws SQLException {
        if (!cb_almtip.getValue().isEmpty()) {
            txt_almanc.focus();
        }
    }
    
    @Listen("onCheck=#chk_almdef")
    public void VerificaEstado() throws SQLException {
        if (objDaoAlmacen.busquedaExistenciaDefault() > 0) {
            chk_almdef.setChecked(false);
            Messagebox.show("Ya existe una almacen por defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_almacenes")
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

    public String verificar() {
        String s_valor;
        //double alto, ancho, largo;
        /*alto = (txt_almalt.getValue() != null ? txt_almalt.getValue() : 0);
         ancho = (txt_almanc.getValue() != null ? txt_almanc.getValue() : 0);
         largo = (txt_almlar.getValue() != null ? txt_almlar.getValue() : 0);*/
        if (txt_almdes.getValue().isEmpty()) {
            s_valor = "El campo 'ALMACEN' es obligatorio";
            campo = "Almacen";
        } else if (!txt_almdes.getValue().matches("[a-zA-ZñÑáÁéÉíÍóÓúÚ\\s]+")) {
            s_valor = "Por favor ingresar solo letras en el campo 'ALMACEN'";
            campo = "Almacen";
        } else if (txt_almdirecc.getValue().isEmpty()) {
            s_valor = "El campo 'DIRECCION' es obligatorio'";
            campo = "Direccion";
        } else if (cb_almtip.getSelectedIndex() == -1) {
            s_valor = "El campo 'TIPO' es obligatorio";
            campo = "Tipo";
        } else if (txt_almanc.getValue() == null) {
            s_valor = "El campo 'Ancho' es obligatorio";
            campo = "Ancho";
        } else if (txt_almlar.getValue() == null) {
            s_valor = "El campo 'Largo' es obligatorio";
            campo = "Largo";
        } else if (txt_almalt.getValue() == null) {
            s_valor = "El campo 'Alto' es obligatorio";
            campo = "Alto";
        } else if (txt_almanc.getValue() <= 0) {
            s_valor = "El ancho debe ser mayor a 0";
            campo = "Ancho";
        } else if (txt_almlar.getValue() <= 0) {
            s_valor = "El largo debe ser mayor a 0";
            campo = "Largo";
        } else if (txt_almalt.getValue() <= 0) {
            s_valor = "El alto debe ser mayor a 0";
            campo = "Alto";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstAlmacen = null;
        objlstAlmacen = new ListModelList<Almacenes>();
        lst_almacenes.setModel(objlstAlmacen);
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

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_guardar.setDisabled(b_valida2);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_almdef.setChecked(false);
        chk_almvta.setChecked(false);
        chk_almcom.setChecked(false);
        chk_almest.setChecked(false);
        txt_almid.setValue(null);
        //txt_almnomrep.setValue("");
        txt_almdes.setValue("");
        txt_almdirecc.setValue("");
        cb_almtip.setSelectedIndex(-1);
        txt_almanc.setValue(null);
        txt_almalt.setValue(null);
        txt_almlar.setValue(null);
        sp_almord.setValue(0);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_almdes.setDisabled(b_valida1);
        txt_almdirecc.setDisabled(b_valida1);
        //txt_almnomrep.setDisabled(false);
        cb_almtip.setDisabled(b_valida1);
        sp_almord.setDisabled(b_valida1);
        txt_almalt.setDisabled(b_valida1);
        txt_almanc.setDisabled(b_valida1);
        txt_almlar.setDisabled(b_valida1);
        chk_almdef.setDisabled(b_valida1);
        chk_almvta.setDisabled(b_valida1);
        chk_almcom.setDisabled(b_valida1);
        //chk_almest.setDisabled(true);
    }

    public void llenarCampos() {

    }

    public Almacenes generaregistro() {
        int i_almest = chk_almest.isChecked() ? 1 : 2;
        int i_almdef = chk_almdef.isChecked() ? 1 : 0;
        int i_almvta = chk_almvta.isChecked() ? 1 : 0;
        int i_almcom = chk_almcom.isChecked() ? 1 : 0;
//       int i_almkey = 0;
        //Recuperar en variables los valores en el formulario
        String s_almkey = txt_almid.getValue().toUpperCase();
        String s_almdes = txt_almdes.getValue().toUpperCase();
        String s_almdirecc = txt_almdirecc.getValue().toUpperCase();
        double db_almanc = txt_almanc.getValue();
        double db_almalt = txt_almalt.getValue();
        double db_almlar = txt_almlar.getValue();
        int i_almtip = cb_almtip.getSelectedItem().getValue();
        int i_almord = sp_almord.getValue();
        String s_usuadd = objUsuCredential.getCuenta().toUpperCase();
        String s_usumod = objUsuCredential.getCuenta().toUpperCase();

        return objAlmacen = new Almacenes(s_almkey/*i_almkey*/, s_almdes, i_almest, s_almdirecc, db_almalt,
                db_almanc, db_almlar, /*s_almnomrep,*/ i_almord, s_usuadd,
                s_usumod, i_sucid, i_empid, i_almtip, i_almvta, i_almcom, i_almdef);
    }

}
