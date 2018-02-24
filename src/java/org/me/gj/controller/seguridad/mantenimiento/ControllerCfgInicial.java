package org.me.gj.controller.seguridad.mantenimiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JFileChooser;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.CfgInicial;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

public class ControllerCfgInicial extends SelectorComposer<Component> {

    //Componentes Web   
    @Wire
    Tab tab_mantenimiento, tab_listacfginicial;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_enpla, tbbtn_enple;
    @Wire
    Doublebox txt_topefe;
    @Wire
    Textbox txt_tcfgkey, txt_mtomin, txt_enpla, txt_firma, txt_enple,
            txt_usuadd, txt_usumod, txt_busqueda,
            txt_ctaafecto, txt_ctaafectofuerames, txt_ctaexoinaf, txt_ctaimpuesto, txt_ctatotal, txt_reclamoafecto, txt_reclamoexoinaf, txt_reclamodsctodinero;
    @Wire
    Intbox txt_tcfdiacad, txt_voucher;
    @Wire
    Combobox cb_empresas, cb_sucursales, cb_monedas, cb_busqueda, cb_voucher;
    @Wire
    Button btn_busqueda_cfgini, up, btn_upload;
    @Wire
    Checkbox chk_igc, chk_igs, chk_igd, chk_igg, chk_cieped, chk_ciemon;
    @Wire
    Listbox lst_cfgini;
    @Wire
    Datebox d_fecadd, d_fecmod;

    @Wire
    Div img_firma;
//Instancia de Objetos
    ListModelList<Empresas> objlstEmpresas;
    ListModelList<Sucursales> objlstSucursales;
    ListModelList<CfgInicial> objlstCfgInicial;
    ListModelList<Moneda> objlstMoneda;
    Empresas objEmpresas = new Empresas();
    Sucursales objSucursales = new Sucursales();
    CfgInicial objCfgInicial = new CfgInicial();
    Accesos objAccesos;
    DaoEmpresas objDaoEmpresas;
    DaoMoneda objDaoMoneda;
    DaoCfgInicial objDaoCfgInicial;
    DaoAccesos objDaoAccesos;

    //Variables publicas
    String campo = "";
    String s_mensaje = "";
    String s_estado = "Q";
    int i_selCab = 0;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\FIRMAS\\";

    //Variables de session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCfgInicial.class);

    //Eventos Primarios - Transaccionales;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstEmpresas = new ListModelList<Empresas>();
        objlstSucursales = new ListModelList<Sucursales>();
        objlstMoneda = new ListModelList<Moneda>();
        objlstCfgInicial = new ListModelList<CfgInicial>();
        objDaoEmpresas = new DaoEmpresas();
        objDaoMoneda = new DaoMoneda();
        objDaoCfgInicial = new DaoCfgInicial();
        objlstEmpresas = objDaoEmpresas.lstEmpresas(1);
        cb_empresas.setModel(objlstEmpresas);
        objlstMoneda = objDaoMoneda.listaMonedas(1);
        cb_monedas.setModel(objlstMoneda);
        limpiarCampos();
        s_estado = "Q";

    }

    @Listen("onCreate=#tabbox_cfginicial")
    public void llenoRegistros() throws SQLException {
        objlstCfgInicial = new ListModelList<CfgInicial>();
        objlstCfgInicial = objDaoCfgInicial.lstCfgInicial();
        lst_cfgini.setModel(objlstCfgInicial);
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        int seleccion = cb_busqueda.getSelectedIndex() == -1 ? 0 : cb_busqueda.getSelectedIndex();
        String consulta = txt_busqueda.getValue().isEmpty() ? "%%" : txt_busqueda.getValue().replace("0", "").trim().toUpperCase();
        limpiarLista();
        objlstCfgInicial = objDaoCfgInicial.BusquedaCfgInicial(seleccion, consulta);
        if (objlstCfgInicial.getSize() > 0) {
            lst_cfgini.setModel(objlstCfgInicial);
        } else {
            limpiarLista();
            Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
    }

    @Listen("onSelect=#lst_cfgini")
    public void seleccionaRegistro() throws SQLException {
        objCfgInicial = (CfgInicial) lst_cfgini.getSelectedItem().getValue();
        if (objCfgInicial == null) {
            objCfgInicial = objlstCfgInicial.get(i_selCab + 1);
        }
        i_selCab = lst_cfgini.getSelectedIndex();
        limpiarCampos();
        llenarCampos(objCfgInicial);

    }

    @Listen("onSelect=#cb_empresas")
    public void SeleccionaEmpresa() throws SQLException {
        //cargamos las sucursales
        String i_codemp;
        cb_sucursales.setValue("");
        i_codemp = cb_empresas.getSelectedItem().getValue().toString();
        objlstSucursales = objDaoCfgInicial.lstSucursales(i_codemp, 1);
        if (objlstSucursales.isEmpty()) {
            Messagebox.show("No hay sucursal para esta empresa ", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                cb_empresas.focus();
                                cb_sucursales.setDisabled(true);
                            }
                        }
                    });
        } else {
            cb_sucursales.setModel(objlstSucursales);
            cb_sucursales.setDisabled(false);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Empresa con el codigo " + i_codemp);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        cb_sucursales.setValue("");
        cb_monedas.focus();
        cb_monedas.select();
        s_estado = "N";
        chk_ciemon.setDisabled(true);
        chk_cieped.setDisabled(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String verificar = verificar();
        if (!verificar.equals("")) {
            Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("moneda")) {
                            cb_monedas.focus();
                        } else if (campo.equals("empresa")) {
                            cb_empresas.focus();
                        } else if (campo.equals("sucursal")) {
                            cb_sucursales.focus();
                        } else if (campo.equals("mventa")) {
                            txt_mtomin.focus();
                        } else if (campo.equals("tpedido")) {
                            txt_topefe.focus();
                        } else if (campo.equals("enlpla")) {
                            txt_enpla.focus();
                        } else if (campo.equals("enplple")) {
                            txt_enple.focus();
                        } else if (campo.equals("cadu")) {
                            txt_tcfdiacad.focus();
                        } else if (campo.equals("ctaafecto")) {
                            txt_ctaafecto.focus();
                        } else if (campo.equals("ctaafectofuerames")) {
                            txt_ctaafectofuerames.focus();
                        } else if (campo.equals("ctaexoinaf")) {
                            txt_ctaexoinaf.focus();
                        } else if (campo.equals("ctaimpuesto")) {
                            txt_ctaimpuesto.focus();
                        } else if (campo.equals("ctatotal")) {
                            txt_ctatotal.focus();
                        } else if (campo.equals("reclamoafecto")) {
                            txt_reclamoafecto.focus();
                        } else if (campo.equals("reclamoexoinaf")) {
                            txt_reclamoexoinaf.focus();
                        } else if (campo.equals("reclamodsctodinero")) {
                            txt_reclamodsctodinero.focus();
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
                                ParametrosSalida objSalidaCab;
                                objCfgInicial = (CfgInicial) generaCabecera();
                                if (s_estado.equals("N")) {
                                    objSalidaCab = objDaoCfgInicial.insertarConfInicial(objCfgInicial);
                                } else {
                                    objSalidaCab = objDaoCfgInicial.actualizarConfInicial(objCfgInicial);
                                }
                                if (objSalidaCab.getFlagIndicador() == 0) {
                                    //  validacion de guardar/nuevo
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    habilitaBotones(false, true);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    llenoRegistros();
                                    s_estado = "Q";
                                    cb_busqueda.setSelectedIndex(-1);
                                    txt_busqueda.setValue("%%");
                                }
                                Messagebox.show(objSalidaCab.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_cfgini.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            cb_empresas.setDisabled(true);
            cb_sucursales.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
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
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            lst_cfgini.setSelectedIndex(-1);
                            cb_busqueda.setSelectedIndex(-1);
                            txt_busqueda.setValue("%%");
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_cfgini")
    public void ctrl_teclado(Event event) throws SQLException {
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

    @Listen("onCheck=#chk_igg")
    public void verificaIndiGenGuias() throws SQLException {
        if (chk_igd.isChecked() || chk_igc.isChecked() || chk_igs.isChecked()) {
            Messagebox.show("Tan solo selecciona un indicador");
            chk_igg.setChecked(false);
            chk_igg.setLabel("NO");
        }
    }

    @Listen("onCheck=#chk_igd")
    public void verificaIndiGenDocs() throws SQLException {
        if (chk_igg.isChecked() || chk_igc.isChecked() || chk_igs.isChecked()) {
            Messagebox.show("Tan solo selecciona un indicador");
            chk_igd.setChecked(false);
            chk_igd.setLabel("NO");
        }
    }

    @Listen("onCheck=#chk_igc")
    public void verificaIndiGenCostos() throws SQLException {
        if (chk_igd.isChecked() || chk_igg.isChecked() || chk_igs.isChecked()) {
            Messagebox.show("Tan solo selecciona un indicador");
            chk_igc.setChecked(false);
            chk_igc.setLabel("NO");
        }
    }

    @Listen("onCheck=#chk_igs")
    public void verificaIndiGenStock() throws SQLException {
        if (chk_igd.isChecked() || chk_igg.isChecked() || chk_igc.isChecked()) {
            Messagebox.show("Tan solo selecciona un indicador");
            chk_igs.setChecked(false);
            chk_igs.setLabel("NO");
        }
    }

    @Listen("onOK=#cb_monedas")
    public void next_cbmoneda() {
        cb_empresas.focus();
    }

    @Listen("onOK=#cb_empresas")
    public void next_cbempresas() {
        cb_sucursales.focus();
    }

    @Listen("onOK=#cb_sucursales")
    public void next_cbsucursales() {
        if (chk_ciemon.isDisabled() && chk_cieped.isDisabled()) {
            txt_mtomin.focus();
        } else {
            chk_ciemon.focus();
        }
    }

    @Listen("onOK=#chk_ciemon")
    public void next_chkciemon() {
        chk_cieped.focus();
    }

    @Listen("onOK=#chk_cieped")
    public void next_chkcieped() {
        txt_mtomin.focus();
    }

    @Listen("onOK=#txt_mtomin")
    public void next_txtmtomin() {
        txt_topefe.focus();
    }

    @Listen("onOK=#txt_topefe")
    public void next_txttopefe() {
        txt_enpla.focus();
    }

    @Listen("onOK=#txt_enpla")
    public void next_txtenpla() {
        txt_enple.focus();
    }

    @Listen("onClick=#tbbtn_enpla")
    public void enlace_planilla() {
        JFileChooser chooser;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Escoger enlace de Planillas");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            txt_enpla.setValue(chooser.getSelectedFile().toString());
        }
    }

    @Listen("onClick=#tbbtn_enple")
    public void enlace_librose() {
        JFileChooser chooser;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Escoger enlace de Libros Electronicos");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            txt_enple.setValue(chooser.getSelectedFile().toString());
        }
    }

    //Eventos Otros 
    public String verificar() {
        String msj;
        if (cb_monedas.getSelectedIndex() == -1) {
            campo = "moneda";
            msj = "Seleccione 'Moneda'";
        } else if (cb_empresas.getSelectedIndex() == -1) {
            campo = "empresas";
            msj = "Seleccione 'Empresas'";
        } else if (s_estado.equals("N") && cb_sucursales.getSelectedIndex() == -1) {
            campo = "sucursal";
            msj = "Seleccione 'Sucursal'";
        } else if (s_estado.equals("M") && cb_sucursales.getValue().isEmpty()) {
            campo = "sucursal";
            msj = "Seleccione 'Sucursal'";
        } else if (txt_mtomin.getValue().isEmpty()) {
            campo = "mventa";
            msj = "Ingrese 'Monto minimo de venta' ";
        } else if (!txt_mtomin.getValue().matches("[0-9]*")) {
            campo = "mventa";
            txt_mtomin.setValue("");
            msj = "Ingrese valores numericos en el campo 'Monto minimo de venta'";
        } else if (txt_mtomin.getValue().equals("0") || Integer.parseInt(txt_mtomin.getValue()) == 0) {
            campo = "mventa";
            txt_mtomin.setValue("");
            msj = "Debe ingresar valores mayores a cero en el campo 'Monto minimo de venta'";
        } else if (txt_topefe.getValue() == null) {
            campo = "tpedido";
            msj = "Ingrese 'Tope de pedidos en efectivos'";
        } else if (txt_topefe.getValue().doubleValue() <= 0.0) {
            campo = "tpedido";
            txt_topefe.setValue(null);
            msj = "Debe ingresar valores mayores a cero en el campo 'Tope de pedidos en efectivos'";
        } else if (txt_enpla.getValue().isEmpty()) {
            campo = "enlpla";
            msj = "Ingrese 'ENLACE PLANILLA'";
        } else if (txt_enple.getValue().isEmpty()) {
            campo = "enlple";
            msj = "Ingrese 'ENLACE DE LIBROS ELECTRONICOS PLE'";
        } else if (txt_tcfdiacad.getValue() == null) {
            campo = "cadu";
            msj = "Ingrese 'DIA CADUCIDAD'";
        } else if (txt_tcfdiacad.getValue() == 0) {
            campo = "cadu";
            msj = "Dia de caducidad tiene que ser mayor a cero";
        } else if (cb_empresas.getValue().equals("CODIJISA") && cb_sucursales.getValue().equals("LIMA") && txt_tcfdiacad.getValue() != 7) {
            campo = "cadu";
            msj = "Dia de caducidad tiene que ser 7 dias";
        } else if (!cb_empresas.getValue().equals("CODIJISA") && !cb_sucursales.getValue().equals("LIMA") && txt_tcfdiacad.getValue() != 10) {
            campo = "cadu";
            msj = "Dia de caducidad tiene que ser 10 dias";
        } else if (!txt_ctaafecto.getValue().matches("[0-9]*")) {
            campo = "ctaafecto";
            msj = "Ingrese valores numéricos en el campo 'Cuenta Afecto'";
        } else if (!txt_ctaafectofuerames.getValue().matches("[0-9]*")) {
            campo = "ctaafectofuerames";
            msj = "Ingrese valores numéricos en el campo 'Cuenta Afecto fuera de mes'";
        } else if (!txt_ctaexoinaf.getValue().matches("[0-9]*")) {
            campo = "ctaexoinaf";
            msj = "Ingrese valores numéricos en el campo 'Cuenta Exonerado/Inafecto'";
        } else if (!txt_ctaimpuesto.getValue().matches("[0-9]*")) {
            campo = "ctaimpuesto";
            msj = "Ingrese valores numéricos en el campo 'Cuenta Impuesto'";
        } else if (!txt_ctatotal.getValue().matches("[0-9]*")) {
            campo = "ctatotal";
            msj = "Ingrese valores numéricos en el campo 'Cuenta Total'";
        } else if (!txt_reclamoafecto.getValue().matches("[0-9]*")) {
            campo = "reclamoafecto";
            msj = "Ingrese valores numéricos en el campo 'Reclamo Afecto'";
        } else if (!txt_reclamoexoinaf.getValue().matches("[0-9]*")) {
            campo = "reclamoexoinaf";
            msj = "Ingrese valores numéricos en el campo 'Reclamo Exonerado/Inafecto'";
        } else if (!txt_reclamodsctodinero.getValue().matches("[0-9]*")) {
            campo = "reclamodsctodinero";
            msj = "Ingrese valores numéricos en el campo 'Reclamo Descuento dinero'";
        } else {
            msj = "";
        }
        return msj;
    }

    public void llenarCampos(CfgInicial objCfgInicial) throws SQLException {
        txt_tcfgkey.setValue(objCfgInicial.getTcfg_id().isEmpty() ? "" : objCfgInicial.getTcfg_id());
        cb_empresas.setSelectedItem(Utilitarios.valorPorTexto1(cb_empresas, objCfgInicial.getEmp_id()));
        cb_sucursales.setValue(objCfgInicial.getSuc_des());
        cb_monedas.setSelectedItem(Utilitarios.textoPorTexto(cb_monedas, objCfgInicial.getTcfg_mon()));
        txt_mtomin.setValue(String.valueOf(objCfgInicial.getTcfg_mtomin()));
        chk_ciemon.setChecked((objCfgInicial.getTcfg_ciemon() == 0));
        chk_ciemon.setLabel(objCfgInicial.getTcfg_ciemon_des());
        chk_cieped.setChecked((objCfgInicial.getTcfg_cieped() == 0));
        chk_cieped.setLabel(objCfgInicial.getTcfg_cieped_des());
        txt_topefe.setValue(objCfgInicial.getTcfg_topefe());
        chk_igg.setChecked(objCfgInicial.getTcfg_gengui().equals("S"));
        chk_igg.setLabel(objCfgInicial.getTcfg_gengui_des());
        chk_igd.setChecked(objCfgInicial.getTcfg_gendoc().equals("S"));
        chk_igd.setLabel(objCfgInicial.getTcfg_gendoc_des());
        chk_igs.setChecked(objCfgInicial.getTcfg_regstk().equals("S"));
        chk_igs.setLabel(objCfgInicial.getTcfg_regstk_des());
        chk_igc.setChecked(objCfgInicial.getTcfg_regcst().equals("S"));
        chk_igc.setLabel(objCfgInicial.getTcfg_regcst_des());
        txt_enpla.setValue(objCfgInicial.getTcfg_enlpla());
        txt_enple.setValue(objCfgInicial.getTcfg_enlple());
        txt_tcfdiacad.setValue(objCfgInicial.getTcfg_diacad());
        txt_voucher.setValue(objCfgInicial.getTcfg_voucher());
        txt_ctaafecto.setValue(objCfgInicial.getTcfg_ctaafecto());
        txt_ctaafectofuerames.setValue(objCfgInicial.getTcfg_ctaafectofmes());
        txt_ctaexoinaf.setValue(objCfgInicial.getTcfg_ctaexoinaf());
        txt_ctaimpuesto.setValue(objCfgInicial.getTcfg_ctaimpuesto());
        txt_ctatotal.setValue(objCfgInicial.getTcfg_ctatotal());
        txt_reclamoafecto.setValue(objCfgInicial.getTcfg_reclamoafecto());
        txt_reclamoexoinaf.setValue(objCfgInicial.getTcfg_reclamoexoinaf());
        txt_reclamodsctodinero.setValue(objCfgInicial.getTcfg_reclamodsctodinero());
        txt_firma.setValue(objCfgInicial.getTcfg_firma());
        try {
            cargarImagen(objCfgInicial.getTcfg_firma(), img_firma);
        } catch (Exception ex) {

        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_mantenimiento.setSelected(b_valida2);
        tab_listacfginicial.setSelected(b_valida1);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_mantenimiento.setDisabled(b_valida2);
        tab_listacfginicial.setDisabled(b_valida1);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida1) {
        cb_empresas.setDisabled(b_valida1);
        cb_sucursales.setDisabled(b_valida1);
        cb_monedas.setDisabled(b_valida1);
        chk_ciemon.setDisabled(b_valida1);
        chk_cieped.setDisabled(b_valida1);
        chk_igc.setDisabled(b_valida1);
        chk_igd.setDisabled(b_valida1);
        chk_igg.setDisabled(b_valida1);
        chk_igs.setDisabled(b_valida1);
        txt_mtomin.setDisabled(b_valida1);
        txt_topefe.setDisabled(b_valida1);
        /*txt_enpla.setDisabled(b_valida1);
         txt_enple.setDisabled(b_valida1);*/
        txt_tcfdiacad.setDisabled(b_valida1);
        tbbtn_enpla.setDisabled(b_valida1);
        tbbtn_enple.setDisabled(b_valida1);
        //cb_voucher.setDisabled(b_valida1);
        txt_voucher.setDisabled(b_valida1);
        txt_ctaafecto.setDisabled(b_valida1);
        txt_ctaafectofuerames.setDisabled(b_valida1);
        txt_ctaexoinaf.setDisabled(b_valida1);
        txt_ctaimpuesto.setDisabled(b_valida1);
        txt_ctatotal.setDisabled(b_valida1);
        txt_reclamoafecto.setDisabled(b_valida1);
        txt_reclamoexoinaf.setDisabled(b_valida1);
        txt_reclamodsctodinero.setDisabled(b_valida1);
        txt_firma.setDisabled(b_valida1);
        btn_upload.setDisabled(b_valida1);
    }

    public Object generaCabecera() {
        int tcfg_key = txt_tcfgkey.getValue().isEmpty() ? 0 : Integer.parseInt(txt_tcfgkey.getValue().toString());
        int emp_id = cb_empresas.getSelectedItem().getValue();
        int suc_id;
        if (s_estado.equals("N")) {
            suc_id = cb_sucursales.getSelectedItem().getValue();
        } else {
            suc_id = objCfgInicial.getSuc_id();
        }
        String tcfg_mon = cb_monedas.getSelectedItem().getValue().toString();
        int tcfg_mtomin = txt_mtomin.getValue().isEmpty() ? 0 : Integer.parseInt(txt_mtomin.getValue());
        int tcfg_ciemon = chk_ciemon.isChecked() ? 0 : 1;
        int tcfg_cieped = chk_cieped.isChecked() ? 0 : 1;
        double tcfg_topefe = txt_topefe.getValue() == null ? 0.0 : txt_topefe.getValue().doubleValue();
        String tcfg_enpla = txt_enpla.getValue();
        String tcfg_enple = txt_enple.getValue();
        String tcfg_gengui = chk_igg.isChecked() ? "S" : "N";
        String tcfg_gendoc = chk_igd.isChecked() ? "S" : "N";
        String tcfg_regstk = chk_igs.isChecked() ? "S" : "N";
        String tcfg_regcst = chk_igc.isChecked() ? "S" : "N";
        int tcfg_diacad = txt_tcfdiacad.getValue();
        //int tcfg_voucher = cb_voucher.getSelectedItem().getValue();
        int tcfg_voucher = txt_voucher.getValue() == null ? 0 : txt_voucher.getValue();
        String tcfg_ctaafecto = txt_ctaafecto.getValue().toString();
        String tcfg_ctaafectofmes = txt_ctaafectofuerames.getValue();
        String tcfg_ctaexoinaf = txt_ctaexoinaf.getValue();
        String tcfg_ctaimpuesto = txt_ctaimpuesto.getValue();
        String tcfg_ctatotal = txt_ctatotal.getValue();
        String tcfg_reclamoafecto = txt_reclamoafecto.getValue();
        String tcfg_reclamoexoinaf = txt_reclamoexoinaf.getValue();
        String tcfg_reclamodsctodinero = txt_reclamodsctodinero.getValue();
        String tcfg_firma = txt_firma.getValue();
        return new CfgInicial(tcfg_key, emp_id, suc_id, tcfg_mon, tcfg_mtomin, tcfg_ciemon,
                tcfg_cieped, tcfg_topefe, tcfg_enpla, tcfg_enple, tcfg_gengui, tcfg_gendoc, tcfg_regstk, tcfg_regcst, tcfg_diacad,
                tcfg_voucher, tcfg_ctaafecto, tcfg_ctaafectofmes, tcfg_ctaexoinaf, tcfg_ctaimpuesto, tcfg_ctatotal, tcfg_reclamoafecto, tcfg_reclamoexoinaf, tcfg_reclamodsctodinero, tcfg_firma);
    }

    public void limpiarCampos() {
        txt_tcfgkey.setValue("");
        cb_empresas.setSelectedIndex(-1);
        cb_monedas.setSelectedIndex(-1);
        cb_sucursales.setSelectedIndex(-1);
        chk_igc.setChecked(false);
        chk_igd.setChecked(false);
        chk_igg.setChecked(false);
        chk_igs.setChecked(false);
        chk_ciemon.setChecked(true);
        chk_cieped.setChecked(true);
        txt_mtomin.setValue("");
        txt_topefe.setValue(null);
        txt_enpla.setValue("");
        txt_enple.setValue("");
        chk_igg.setLabel("NO");
        chk_igs.setLabel("NO");
        chk_igd.setLabel("NO");
        chk_igc.setLabel("NO");
        chk_ciemon.setLabel("ABIERTO");
        chk_cieped.setLabel("ABIERTO");
        txt_tcfdiacad.setValue(null);
        //cb_voucher.setSelectedIndex(-1);
        txt_voucher.setValue(null);
        txt_ctaafecto.setValue("");
        txt_ctaafectofuerames.setValue("");
        txt_ctaexoinaf.setValue("");
        txt_ctaimpuesto.setValue("");
        txt_ctatotal.setValue("");
        txt_reclamoafecto.setValue("");
        txt_reclamoexoinaf.setValue("");
        txt_reclamodsctodinero.setValue("");
        txt_firma.setValue("");
        img_firma.getChildren().clear();

    }

    public void limpiarLista() {
        objlstCfgInicial = null;
        objlstCfgInicial = new ListModelList<CfgInicial>();
        lst_cfgini.setModel(objlstCfgInicial);
    }

    //metodos sin utilizar
    public void escogerOpcion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*public String upload() {

     File uploadDirectory = new File(request.getSession().getServletContext().getRealPath("/uploads"));

     if (!uploadDirectory.exists()) {
     uploadDirectory.mkdirs();
     }

     File reportFile = new File(reportDirectory.getAbsolutePath(), fileName);

     String requestUrl = request.getRequestURL().toString();
     requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/") + 1) + "uploads/" + fileName;
     return requestUrl;
     }*/
    @Listen("onUpload=#btn_upload")
    public void onUpload(UploadEvent event) {
        try {
            img_firma.getChildren().clear();
            File theDir = new File(rutaFile);
            File file;
            AMedia amedia;
            if (!theDir.exists()) {

                theDir.mkdir();

            }
            Media media = event.getMedia();
            /* Clients.showNotification("upload details: "
             + " name " + media.getName()
             + " size " + (media.isBinary() ? media.getByteData().length : media.getStringData().length())
             + " type " + media.getContentType());*/

            FileOutputStream out = new FileOutputStream(rutaFile + media.getName());
            out.write(media.getByteData());
            out.close();

            //img_firma.setSrc("D:\\GJ_CARPETAS\\PLANILLAS\\FIRMAS\\112345677.JPEG");
            cargarImagen(rutaFile + media.getName(), img_firma);
            txt_firma.setValue((rutaFile + media.getName().toString()));
            //   img_firma.setSrc(file);

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show(e.toString());
        }

    }

    public void cargarImagen(String ruta, Div img) throws IOException {
        File existe = new File(ruta);
        if (existe.exists()) {
            //if (ruta != null) {
            Image imagen = new Image();
            imagen.setContent(new AImage(ruta));
            imagen.setParent(img);
            imagen.setStyle("border: 2px solid #2E9AFE; width : 194px; height : 250px; ");
            //FileInputStream fis = new FileInputStream(ruta);
            //}
        }

    }

}
