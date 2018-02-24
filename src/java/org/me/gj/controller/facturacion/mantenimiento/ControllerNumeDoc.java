package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.facturacion.mantenimiento.NumeFac;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerNumeDoc extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listanumdoc, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_busqueda, txt_usuadd, txt_usumod, txt_numserie, txt_descripcion, txt_nomreporte;
    @Wire
    Textbox txt_correlativo;
    @Wire
    Combobox cb_busqueda, cb_busest, cb_notes, cb_tipodoc;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_estado;
    @Wire
    Listbox lst_numdoc;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Intbox txt_nroitems;
    //Instancias de Objetos
    ListModelList<TipDoc> objlstTipDoc;
    ListModelList<Guias> objlstNotES;
    ListModelList<NumeFac> objlstNumDoc;
    Accesos objAccesos = new Accesos();
    NumeFac objNumDoc = new NumeFac();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoNumeFac objDaoNumDoc = new DaoNumeFac();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q";
    String s_mensaje = "";
    String campo = "";
    public static boolean bandera;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerNumeDoc.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //Lista Tipo Documento de Venta
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = (new DaoTipDoc()).listaTipDoc(1);
        cb_tipodoc.setModel(objlstTipDoc);
        cb_tipodoc.setValue("");
        //Lista Nota E/S
        objlstNotES = new ListModelList<Guias>();
        objlstNotES = (new DaoManNotaES()).listaGuias(1);
        objlstNotES.add(null);
        cb_notes.setModel(objlstNotES);
        cb_notes.setValue("");
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    }

    @Listen("onCreate=#tabbox_mannumdoc")
    public void llenaRegistros() throws SQLException {
        objlstNumDoc = new ListModelList<NumeFac>();
        objlstNumDoc = objDaoNumDoc.listaNumeFac(1);
        lst_numdoc.setModel(objlstNumDoc);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40110000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Mantenimiento de Numeraci�n de Documento con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Numeraci�n de Documento con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creaci�n de una nueva Numeraci�n de Documento");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creaci�n de una nueva Numeraci�n de Documento");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edici�n de una Numeraci�n de Documento");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edici�n de una Numeraci�n de Documento");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminaci�n de una Numeraci�n de Documento");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminaci�n de una Numeraci�n de Documento");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().isEmpty() ? "%%" : txt_busqueda.getValue().toUpperCase();
        int i_seleccion = 0;
        int i_estado;
        if (cb_busest.getSelectedIndex() == 0) {
            i_estado = 3;
        } else if (cb_busest.getSelectedIndex() == 1) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de numeracion del documento " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripci�n " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la serie  " + s_consulta + " para su busqueda");
        }
        objlstNumDoc = objDaoNumDoc.busquedaNumeFaces(i_seleccion, s_consulta, i_estado);

        if (objlstNumDoc.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstNumDoc.getSize() + " registro(s)");
        }

        if (objlstNumDoc.getSize() > 0) {
            lst_numdoc.setModel(objlstNumDoc);
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

    @Listen("onSelect=#lst_numdoc")
    public void seleccionaRegistro() throws SQLException {
        objNumDoc = (NumeFac) lst_numdoc.getSelectedItem().getValue();
        if (objNumDoc == null) {
            objNumDoc = objlstNumDoc.get(i_sel + 1);
        }
        i_sel = lst_numdoc.getSelectedIndex();
        llenarCampos(objNumDoc);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objNumDoc.getNumefac_cor());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_estado.setChecked(true);
        chk_estado.setDisabled(true);
        cb_tipodoc.focus();
        txt_correlativo.setDisabled(true);
        cb_notes.setSelectedIndex(-1);
        cb_notes.setDisabled(true);
        cb_tipodoc.setSelectedIndex(-1);
        cb_tipodoc.setValue("");
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_numdoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_numserie.setDisabled(true);
            txt_correlativo.setDisabled(true);
            cb_tipodoc.setDisabled(true);
            txt_correlativo.focus();
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
                                objNumDoc = (NumeFac) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoNumDoc.insertarNumeFac(objNumDoc);
                                } else {
                                    s_mensaje = objDaoNumDoc.actualizarNumeFac(objNumDoc);
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                limpiaAuditoria();
                                limpiarLista();
                                VerificarTransacciones();
                                llenaRegistros();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_numdoc.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta Numeraci�n?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String msg = objDaoNumDoc.eliminarNumeFac(objNumDoc);
                                limpiarLista();
                                limpiarCampos();
                                Messagebox.show(msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                llenaRegistros();
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
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            VerificarTransacciones();
                            llenaRegistros();
                            lst_numdoc.focus();
                            //
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_mannumdoc")
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
            objlstNumDoc = new ListModelList<NumeFac>();
            objlstNumDoc = objDaoNumDoc.listaNumeFac(0);
            lst_numdoc.setModel(objlstNumDoc);
        }
    }

    @Listen("onSelect=#cb_tipodoc")
    public void SeleccionaTipoDoc() throws SQLException {
        cb_notes.setValue("");
        cb_notes.setDisabled(false);
    }

    /*@Listen("onOK=#txt_correlativo")
     public void next_correlativo() {
     cb_notes.focus();
     cb_notes.select();
     }*/
    @Listen("onOK=#cb_tipodoc")
    public void next_tipventa() {
        cb_notes.focus();
        cb_notes.select();
    }

    @Listen("onOK=#cb_notes")
    public void next_notes() {
        txt_numserie.focus();

    }

    @Listen("onOK=#txt_numserie")
    public void next_serie() {
        //cb_tipodoc.focus();
        //cb_tipodoc.select();
        txt_descripcion.focus();
    }

    @Listen("onOK=#txt_descripcion")
    public void next_descripcion() {
        txt_nroitems.focus();
    }

    @Listen("onOK=#txt_nroitems")
    public void next_reporte() {
        txt_nomreporte.focus();
    }

    //Eventos Otros
    public String verificar() {
        String s_valor;
        if (txt_numserie.getValue().isEmpty()) {
            s_valor = "Ingrese 'Numero de serie'"; 
            campo = "numero";
        } else if (!txt_numserie.getValue().matches("[0-9]*")) {
            s_valor = "Ingrese valores numericos en 'Numero de serie'";
            campo = "numero";
            txt_numserie.setValue("");
        } else if (txt_numserie.getValue().length() != 3) {
            s_valor = "Debe ingresar 3 digitos en 'Numero de serie'";
            campo = "numero";
        } else if (txt_numserie.getValue().equals("000")) {
            s_valor = "Ingrese valores mayores a cero en 'Numero de serie'";
            campo = "numero";
            txt_numserie.setValue("");
        } else if (txt_correlativo.getValue().isEmpty() && s_estado.equals("M")) {
            s_valor = "Ingrese 'Correlativo'";
            campo = "correlativo";
        } else if (!txt_correlativo.getValue().matches("[0-9]*")) {
            s_valor = "Ingrese valores numericos en 'Correlativo'";
            campo = "correlativo";
        } else if (txt_correlativo.getValue().equals("0") || txt_correlativo.getValue().equals("00") || txt_correlativo.getValue().equals("000") || txt_correlativo.getValue().equals("0000")) {
            s_valor = "Ingrese valores mayores a cero en 'Correlativo'";
            campo = "correlativo";
        } else if (cb_tipodoc.getSelectedIndex() == -1) {
            s_valor = "Seleccione 'Tipo de venta'";
            campo = "tipo";
        } else if (!cb_tipodoc.getValue().equals("GUIAS REMISION") && cb_notes.getValue().equals("")
                || !cb_tipodoc.getValue().equals("GUIAS REMISION") && cb_notes.getSelectedIndex() == -1) {
            s_valor = "Seleccione 'Nota E/S'";
            campo = "notes";
        } else if (cb_notes.getSelectedIndex() == -1) {
            s_valor = "Seleccione 'Nota E/S'";
            campo = "notes";
        } else if (txt_descripcion.getValue().isEmpty()) {
            s_valor = "Seleccione 'Descripcion'";
            campo = "descripcion";
        } /*else if (!txt_descripcion.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "Por Favor Ingresar Solo Letras en el campo 'Descripcion'";
            campo = "descripcion";
            txt_descripcion.setValue("");
        } */else if (txt_nroitems.getValue() == null) {
            s_valor = "Ingrese 'NRO Items'";
            campo = "item";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public void validafocos() {
        if (campo.equals("numero")) {
            txt_numserie.focus();
        } else if (campo.equals("correlativo")) {
            txt_correlativo.focus();
        } else if (campo.equals("tipo")) {
            cb_tipodoc.focus();
        } else if (campo.equals("notes")) {
            cb_notes.focus();
        } else if (campo.equals("descripcion")) {
            txt_descripcion.focus();
        } else if (campo.equals("item")) {
            txt_nroitems.focus();
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
        //limpio mi lista
        objlstNumDoc = null;
        objlstNumDoc = new ListModelList<NumeFac>();
        lst_numdoc.setModel(objlstNumDoc);
    }

    public void llenarCampos(NumeFac objNumDoc) {
        txt_correlativo.setValue(objNumDoc.getNumefac_corid());
        txt_numserie.setValue(String.valueOf(objNumDoc.getNumefac_serie_id()));
        cb_notes.setSelectedItem(Utilitarios.valorPorTexto1(cb_notes, objNumDoc.getNumefac_notes()));
        cb_tipodoc.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipodoc, objNumDoc.getNumefac_tipdoc()));
        chk_estado.setChecked(objNumDoc.isValor());
        chk_estado.setLabel(objNumDoc.isValor() ? "ACTIVO" : "INACTIVO");
        txt_descripcion.setValue(objNumDoc.getNumefac_des());
        txt_nomreporte.setValue(objNumDoc.getNumefac_nomrep());
        txt_nroitems.setValue(objNumDoc.getNumefac_nroitems());
        txt_usuadd.setValue(objNumDoc.getNumefac_usuadd());
        d_fecadd.setValue(objNumDoc.getNumefac_fecadd());
        txt_usumod.setValue(objNumDoc.getNumefac_usumod());
        d_fecmod.setValue(objNumDoc.getNumefac_fecmod());
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanumdoc.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanumdoc.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_estado.setChecked(true);
        chk_estado.setLabel("ACTIVO");
        txt_correlativo.setValue("");
        txt_numserie.setValue("");
        txt_descripcion.setValue("");
        txt_nomreporte.setValue("");
        txt_nroitems.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        txt_correlativo.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
        txt_numserie.setDisabled(b_valida);
        cb_tipodoc.setDisabled(b_valida);
        cb_notes.setDisabled(b_valida);
        txt_descripcion.setDisabled(b_valida);
        txt_nomreporte.setDisabled(b_valida);
        txt_nroitems.setDisabled(b_valida);
    }

    public Object generaRegistro() {
        int i_cor = txt_correlativo.getValue().isEmpty() ? 0 : Integer.parseInt(txt_correlativo.getValue());
        int i_emp_id = objUsuCredential.getCodemp();
        int i_suc_id = objUsuCredential.getCodsuc();
        int i_serie = Integer.parseInt(txt_numserie.getValue());
        int i_tipvent = cb_tipodoc.getSelectedItem().getValue();
        int i_notes = Integer.parseInt(cb_notes.getSelectedItem().getValue().toString());
        int i_nro_items = txt_nroitems.getValue();
        String s_des_serie = txt_descripcion.getValue().toUpperCase().trim();
        String s_nomreport = txt_nomreporte.getValue();
        int i_est = chk_estado.isChecked() ? 1 : 2;
        String s_usuadd = objUsuCredential.getCuenta();
        String s_usumod = objUsuCredential.getCuenta();

        return new NumeFac(i_cor, i_emp_id, i_suc_id, i_tipvent, i_serie, i_notes, i_nro_items, s_des_serie,
                s_nomreport, i_est, s_usuadd, s_usumod);
    }

    //metodos sin utilizar
    public void botonImprimir() {
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
