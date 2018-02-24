package org.me.gj.controller.cxc.informes;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.procesos.ControllerProcPedPend;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.CtaCobCab;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerConsultaxCliente extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Longbox txt_ruc;
    @Wire
    Doublebox txt_pdscto, db_totlimcred, db_saltot, db_limcred;
    @Wire
    Intbox txt_totlimdoc, txt_limdoc;
    @Wire
    Textbox txt_cli_id, txt_cli_razsoc, txt_doc_id, txt_doc_des, txt_direcc, txt_horid, txt_hordes,
            txt_zon_id, txt_zon_des, txt_ven_des, txt_ven_id, txt_giro, txt_diavis_id, txt_diavis_des,
            txt_movil, txt_ref, txt_doc_des_des, txt_datcli,
            txt_docemi, txt_cate_id, txt_cate_des, txt_ncsa, txt_pccred, txt_proatra, txt_nrecla,
            txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_canal;
    @Wire
    Label lbl_datcli;
    @Wire
    Checkbox chk_cliest, chk_ref, chk_ppago, chk_finest;
    @Wire
    Listbox lst_ctacob;
    @Wire
    Datebox d_fecnac, d_fecadd, d_fecmod;
    @Wire
    Radiogroup rbg_datcli;
    //Instancia de Objetos
    ListModelList<Canal> objlstCanal;
    ListModelList<CtaCobCab> objlstCtaCobCab, objlstAux;
    CtaCobCab objCtaCobCab;
    DaoCtaCob objDaoCtaCob;
    DaoCliente objDaoCliente;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    Cliente objCliente;
    CliFinanciero objCliFinanciero;
    CliGarantia objCliGarantia;
    CliTelefono objCliTelefono;
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat dfc = new DecimalFormat("#,###,##0.00", dfs);
    //Variables publicas
    String ok = "", campo = "", s_estado = "";
    public static String s_cliid, s_nrodoc;
    public static String controlador = "";
    public static int s_tipdoc;
    int i_flag = 0;
    public static boolean bandera = false;
    String modoEjecucion;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerConsultaxCliente.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstCtaCobCab = new ListModelList<CtaCobCab>();
        objlstCanal = new ListModelList<Canal>();
        objDaoCtaCob = new DaoCtaCob();
        objDaoCliente = new DaoCliente();
        objCliente = new Cliente();
        objCtaCobCab = new CtaCobCab();
        objlstCanal = objDaoCliente.listaCanal();
        cb_canal.setModel(objlstCanal);
        txt_cli_id.focus();

    }

    // Eventos Primarios - Transaccionales
    //@Listen("onCreate=#tabbox_cxccli")
    @Listen("onCreate=#div_consultaxcliente")
    public void llenaRegistros() throws SQLException {
        if (ControllerProcPedPend.controlador.equals("ControllerProcPedPend")) {
            ControllerProcPedPend.controlador = "";
            s_cliid = Utilitarios.lpad(ControllerProcPedPend.s_cliid, 10, "0");
            objCliente = objDaoCtaCob.listaClienteCtaCob(s_cliid);
            llenarCamposCliente(objCliente);
        }
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20301000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Consulta por Cliente con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Consulta por Cliente con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edici�n de una Consulta por Cliente");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edici�n de una Consulta por Cliente");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de una Consulta por Cliente");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de una Consulta por Cliente");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_cxccli")
    public void ctrl_teclado(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
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
        }
    }

    @Listen("onCheck=#rbg_datcli")
    public void SeleccionaOpcionCliente() {
        if (rbg_datcli.getItems().get(0).isChecked()) {
            lbl_datcli.setValue("DNI: ");
            txt_datcli.setMaxlength(8);
        } else if (rbg_datcli.getItems().get(1).isChecked()) {
            lbl_datcli.setValue("RUC: ");
            txt_datcli.setMaxlength(11);
        } else if (rbg_datcli.getItems().get(2).isChecked()) {
            lbl_datcli.setValue("TELF: ");
            txt_datcli.setMaxlength(7);
        }
        txt_datcli.setValue("");
        txt_datcli.focus();
    }

    @Listen("onCheck=#chk_ref")
    public void SeleccionaVerficacionReferencia() throws SQLException {
        if (!txt_cli_id.getValue().isEmpty() && txt_cli_id.getValue().matches("[0-9]*")) {
            if (chk_ref.isChecked()) {
                String cli_id = txt_cli_id.getValue();
                objCliente = objDaoCtaCob.listaClienteCtaCob(cli_id);
                if (objCliente != null) {
                    String fecha = new SimpleDateFormat("dd/MM/yyyy").format(Utilitarios.RestaDias(Utilitarios.hoyAsFecha(), 184));
                    llenarCamposDetalle(objCliente.getCli_id(), fecha);
                } else {
                    if (i_flag == 0) {
                        seleccionaCheck(true);
                        Messagebox.show("No existe el codigo del cliente o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            } else {
                llenarCamposDetalle(txt_cli_id.getValue(), "");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
         if (txt_cli_id.getValue().isEmpty()) {
            Messagebox.show("Por favor ingresar codigo de cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_cli_id.focus();
                    }
                }
            });

        }
        
        
        /*if (db_limcred.getValue() == null && txt_limdoc.getValue() == null) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            //lst_ctacob.focus();
        } */else {
            s_estado = "E";
            habilitaBotones(true, false);
            tbbtn_btn_deshacer.setDisabled(true);
            db_limcred.setDisabled(false);
            txt_limdoc.setDisabled(false);
            db_limcred.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (!txt_limdoc.isDisabled() && !db_limcred.isDisabled()) {
            String verificar = verificar();
            if (!verificar.isEmpty()) {
                Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("limcred")) {
                                db_limcred.focus();
                            } else if (campo.equals("limdoc")) {
                                txt_limdoc.focus();
                            }
                        }
                    }
                });
            } else {
                ParametrosSalida parametroSalida;
                parametroSalida = objDaoCtaCob.actualizaLimCredDoc(txt_cli_id.getValue(), db_limcred.getValue(), txt_limdoc.getValue());
                if (parametroSalida.getFlagIndicador() == 0) {
                    habilitaBotones(true, true);
                    tbbtn_btn_editar.setDisabled(false);
                    tbbtn_btn_imprimir.setDisabled(false);
                    db_limcred.setDisabled(true);
                    txt_limdoc.setDisabled(true);
                }
                Messagebox.show(parametroSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                lst_ctacob.focus();
            }
        }
    }

    @Listen("onClick=#btn_versaldo")
    public void BotonVerSaldo() throws SQLException {
        if (!txt_cli_id.getValue().isEmpty() && txt_cli_id.getValue().matches("[0-9]*")) {
            objCliente = objDaoCtaCob.listaClienteCtaCob(txt_cli_id.getValue());
            if (objCliente != null) {
                objCliFinanciero = objDaoCtaCob.listaFinancieroCtaCob(objCliente.getCli_key());
                String saldocred = objCliFinanciero == null ? "0.0" : dfc.format(objCliFinanciero.getClifin_saldo());
                int saldodoc = objCliFinanciero == null ? 0 : objCliFinanciero.getClifin_docemi();
                Messagebox.show("Saldo Credito : " + saldocred + "\n Saldo Doc : " + saldodoc, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            if (i_flag == 0) {
                Messagebox.show("No ha ingresado cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClick=#btn_especial")
    public void BotonEspecial() {
        if (i_flag == 0) {
            Messagebox.show("Confirme la autorizacion para no validar 'L/CRED' y 'L/DOC'", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {

                }
            });
        }
    }

    @Listen("onClick=#btn_garantias")
    public void BotonGarantias() throws SQLException {
        if (!txt_cli_id.getValue().isEmpty() && txt_cli_id.getValue().matches("[0-9]*")) {
            objCliente = objDaoCtaCob.listaClienteCtaCob(txt_cli_id.getValue());
            if (objCliente != null) {
                objCliGarantia = objDaoCtaCob.BusquedaGarantiaxCliente(objCliente.getCli_key(), objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                String mensaje = objCliGarantia == null ? "No tiene garantia" : "";
                Messagebox.show(mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
            if (i_flag == 0) {
                Messagebox.show("No ha ingresado cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClick=#btn_glosa")
    public void BotonGlosa() throws WrongValueException, SQLException {
        if (!txt_cli_id.getValue().isEmpty() && txt_cli_id.getValue().matches("[0-9]*")) {
            objCliente = objDaoCtaCob.listaClienteCtaCob(txt_cli_id.getValue());
            if (objCliente != null) {
                Map parametros = new HashMap();
                modoEjecucion = "mantCliObservacion";
                parametros.put("usuario", objUsuCredential.getCuenta());
                parametros.put("emp_id", objUsuCredential.getCodemp());
                parametros.put("suc_id", objUsuCredential.getCodsuc());
                parametros.put("cli_id", txt_cli_id.getValue());
                parametros.put("controlador", "ControllerConsultaxCliente");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/cxc/informes/IngObserCxc.zul", null, parametros);
                window.doModal();
            }
        } else {
            if (i_flag == 0) {
                Messagebox.show("No ha ingresado cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClick=#btn_movcred")
    public void BotonMovCred() {
        if (!txt_cli_id.getValue().isEmpty() && !txt_cli_razsoc.getValue().isEmpty()) {
            Map parametros = new HashMap();
            Window window = (Window) Executions.createComponents("/org/me/gj/view/cxc/informes/ListMovCredCli.zul", null, parametros);
            window.doModal();
        } else {
            if (i_flag == 0) {
                seleccionaCheck(false);
                Messagebox.show("No ha ingresado cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClickConsuxDoc=#lst_ctacob")
    public void goConsultaxDocumento(ForwardEvent evt) {
        obtieneClienteLista(evt, "/informes/ConsultaxDocumento.zul");
    }

    @Listen("onClickAmortiza=#lst_ctacob")
    public void goAmortizacion(ForwardEvent evt) {
        obtieneClienteLista(evt, "/informes/EliminaArmotizacion.zul");
    }

    public void obtieneClienteLista(ForwardEvent evt, String zul) {
        Toolbarbutton tbbtn_cliente = (Toolbarbutton) evt.getOrigin().getTarget();
        objlstAux = (ListModelList) lst_ctacob.getModel();
        Listitem item = (Listitem) tbbtn_cliente.getParent().getParent();

        controlador = "ControllerConsultaxCliente";
        s_nrodoc = objlstAux.get(item.getIndex()).getCtacob_nrodoc();
        s_tipdoc = objlstAux.get(item.getIndex()).getCtacob_tipdoc() == null ? 0 : Integer.parseInt(objlstAux.get(item.getIndex()).getCtacob_tipdoc());
        Executions.sendRedirect("/org/me/gj/view/cxc" + zul);
    }

    @Listen("onOK=#txt_cli_id")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "CxCCli";
                objMapObjetos.put("txt_cli_id", txt_cli_id);
                objMapObjetos.put("txt_cli_razsoc", txt_cli_razsoc);
                objMapObjetos.put("txt_doc_id", txt_doc_id);
                objMapObjetos.put("txt_doc_des", txt_doc_des);
                objMapObjetos.put("txt_doc_des_des", txt_doc_des_des);
                objMapObjetos.put("txt_direcc", txt_direcc);
                objMapObjetos.put("txt_ruc", txt_ruc);
                objMapObjetos.put("txt_ven_id", txt_ven_id);
                objMapObjetos.put("txt_ven_des", txt_ven_des);
                objMapObjetos.put("txt_zon_id", txt_zon_id);
                objMapObjetos.put("txt_zon_des", txt_zon_des);
                objMapObjetos.put("txt_horid", txt_horid);
                objMapObjetos.put("txt_hordes", txt_hordes);
                objMapObjetos.put("txt_diavis_id", txt_diavis_id);
                objMapObjetos.put("txt_diavis_des", txt_diavis_des);
                objMapObjetos.put("txt_movil", txt_movil);
                objMapObjetos.put("txt_giro", txt_giro);
                objMapObjetos.put("d_fecnac", d_fecnac);
                objMapObjetos.put("cb_canal", cb_canal);
                objMapObjetos.put("txt_ref", txt_ref);

                //Detalle
                objMapObjetos.put("db_limcred", db_limcred);
                objMapObjetos.put("db_totlimcred", db_totlimcred);
                objMapObjetos.put("txt_limdoc", txt_limdoc);
                objMapObjetos.put("txt_totlimdoc", txt_totlimdoc);
                objMapObjetos.put("txt_cate_id", txt_cate_id);
                objMapObjetos.put("txt_cate_des", txt_cate_des);
                objMapObjetos.put("txt_pdscto", txt_pdscto);

                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerConsultaxCliente");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                lst_ctacob.focus();
                ok = "s";
            }
        }
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClientes() throws SQLException {
        if (txt_cli_id.getValue().isEmpty()) {
            limpiarCampos();
            limpiarLista();
            seleccionaCheck(true);
        } else {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                i_flag = 1;
                Messagebox.show("Por favor ingrese datos numericos en el campo 'CLIENTE'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            limpiarLista();
                            seleccionaCheck(true);
                            txt_cli_id.focus();
                            i_flag = 0;
                        }
                    }
                });
            } else {
                if ("s".equals(ok)) {
                    String cli_id = txt_cli_id.getValue();
                    objCliente = objDaoCtaCob.listaClienteCtaCob(cli_id);
                    if (objCliente == null) {
                        i_flag = 1;
                        Messagebox.show("El codigo del cliente no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCampos();
                                    limpiarLista();
                                    seleccionaCheck(true);
                                    txt_cli_id.focus();
                                    i_flag = 0;
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + " y ha encontrado 1 registro(s)");

                        llenarCamposCliente(objCliente);

                        if (objCliente.getCli_est() != 1) {
                            Messagebox.show("El cliente se encuentra 'inactivo' ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            ok = "";
                        }
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#db_limcred")
    public void enter1(){
        txt_limdoc.focus();
        
    }
    @Listen("onOK=#txt_datcli")
    public void onload_datcli() throws SQLException {
        if (!txt_datcli.getValue().isEmpty()) {
            String validaSeleccionCliente = validaSeleccionCliente();
            if (!validaSeleccionCliente.isEmpty()) {
                Messagebox.show(validaSeleccionCliente, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        limpiarCampos();
                        limpiarLista();
                        seleccionaCheck(true);
                        txt_datcli.focus();
                    }
                });
            } else {
                objCliente = objDaoCtaCob.listaClienteCtaCob(txt_datcli.getValue());
                if (objCliente == null) {
                    Messagebox.show("El cliente no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            limpiarCampos();
                            limpiarLista();
                            seleccionaCheck(true);
                            txt_datcli.focus();
                        }
                    });
                } else {
                    llenarCamposCliente(objCliente);
                    if (objCliente.getCli_est() != 1) {
                        Messagebox.show("El cliente se encuentra 'inactivo' ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        }
    }

    public String verificar() {
        String mensaje;
        if (db_limcred.getValue() == null) {
            mensaje = "En el campo 'LIM.CRED' debe ingresar valores numericos";
            campo = "limcred";
        } else if (txt_limdoc.getValue() == null) {
            mensaje = "En el campo 'LIM.DOC' debe ingresar valores numericos";
            campo = "limdoc";
        } else {
            mensaje = "";
        }
        return mensaje;
    }

    public String validaSeleccionCliente() {
        String s_mensaje;
        if (!txt_datcli.getValue().matches("[0-9]*")) {
            s_mensaje = "Debe ingresar valores numericos en '" + lbl_datcli.getValue() + "'";
        } else if (rbg_datcli.getItems().get(0).isChecked() && txt_datcli.getValue().length() < 8) {
            s_mensaje = "Debe ingresar maximo 8 digitos en '" + lbl_datcli.getValue() + "'";
        } else if (rbg_datcli.getItems().get(1).isChecked() && txt_datcli.getValue().length() < 11) {
            s_mensaje = "Debe ingresar maximo 11 digitos en '" + lbl_datcli.getValue() + "'";
        } else if (rbg_datcli.getItems().get(2).isChecked() && txt_datcli.getValue().length() < 7) {
            s_mensaje = "Debe ingresar maximo 7 digitos en '" + lbl_datcli.getValue() + "'";
        } else {
            s_mensaje = "";
        }
        return s_mensaje;
    }

    //Eventos Otros 
    public void llenarCamposCliente(Cliente objCliente) throws SQLException {
        objCliTelefono = objDaoCtaCob.listaTelefonoCtaCob(objCliente.getCli_key());
        objCliFinanciero = objDaoCtaCob.listaFinancieroCtaCob(objCliente.getCli_key());
        objCtaCobCab = objDaoCtaCob.obtenerDatosCtaCabxClientes(objCliente.getCli_id());

        //Cabecera  
        txt_cli_id.setValue(objCliente.getCli_id());
        txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
        txt_doc_id.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_tipodoc()), 3, "0"));
        txt_doc_des.setValue(objCliente.getCli_tipodoc_des());
        txt_doc_des_des.setValue(objCliente.getCli_dni());
        txt_direcc.setValue(String.valueOf(objCliente.getClidir_direc()));
        txt_ruc.setValue(objCliente.getCli_ruc());
        txt_ven_id.setValue(objCliente.getVen_id());
        txt_ven_des.setValue(objCliente.getVen_apenom());
        txt_zon_id.setValue(objCliente.getZon_id());
        txt_zon_des.setValue(objCliente.getZon_des());
        txt_horid.setValue(objCliente.getHor_id());
        txt_hordes.setValue(objCliente.getHor_des());
        txt_diavis_id.setValue(String.valueOf(objCliente.getDia_vis()));
        txt_diavis_des.setValue(objCliente.getDia_vis_des());
        txt_giro.setValue(objCliente.getCli_giro());
        d_fecnac.setValue(objCliente.getCli_fecnac());
        cb_canal.setSelectedItem(Utilitarios.valorPorTexto1(cb_canal, objCliente.getCli_canal()));
        chk_cliest.setChecked(objCliente.getCli_est() == 1);
        chk_cliest.setLabel(chk_cliest.isChecked() ? "ACTIVO" : "INACTIVO");
        txt_pdscto.setValue(objCliente.getCli_dscto());
        txt_ref.setValue(objCliente.getCli_dirref());

        txt_movil.setValue(objCliTelefono == null ? "0"
                : objCliTelefono.getClitel_telef1() > 0 ? String.valueOf(objCliTelefono.getClitel_telef1())
                : objCliTelefono.getClitel_telef2() > 0 ? String.valueOf(objCliTelefono.getClitel_telef2())
                : String.valueOf(objCliTelefono.getClitel_movil()));

        txt_cate_id.setValue(objCliFinanciero == null ? "" : Utilitarios.lpad(String.valueOf(objCliFinanciero.getClifin_categ()), 3, "0"));
        txt_cate_des.setValue(objCliFinanciero == null ? "" : objCliFinanciero.getClifin_categ_des());

        //Detalle 
        seleccionaCheck(false);
        SeleccionaVerficacionReferencia();

        chk_finest.setChecked(objlstCtaCobCab.getSize() <= 0 ? false
                : objCliFinanciero == null ? false
                : objCliFinanciero.getClifin_est() == 1 ? true : false);

        db_limcred.setValue(objCliFinanciero == null ? 0.0 : objCliFinanciero.getClifin_limcred());
        db_totlimcred.setValue(objCliente.getCli_limcredcorp());
        txt_limdoc.setValue(objCliFinanciero == null ? 0 : objCliFinanciero.getClifin_limdoc());
        txt_totlimdoc.setValue(objCliente.getCli_limdoccorp());

        txt_docemi.setValue(objCtaCobCab == null ? "0" : objCtaCobCab.getDoc_emi());

        txt_pccred.setValue("0");
        txt_proatra.setValue("0");
        txt_nrecla.setValue("0");
        txt_ncsa.setValue("0");
        //Auditoria
        txt_usuadd.setValue(objCtaCobCab == null ? "" : objCtaCobCab.getCtacob_usuadd());
        d_fecadd.setValue(objCtaCobCab == null ? null : objCtaCobCab.getCtacob_fecadd());
        txt_usumod.setValue(objCtaCobCab == null ? "" : objCtaCobCab.getCtacob_usumod());
        d_fecmod.setValue(objCtaCobCab == null ? null : objCtaCobCab.getCtacob_fecmod());
    }

    public void llenarCamposDetalle(String cli_id, String fecha) throws SQLException {
        objlstCtaCobCab = objDaoCtaCob.listaCtaCobSeisMeses(cli_id, fecha);
        lst_ctacob.setModel(objlstCtaCobCab);

        double data[] = calcularTotales();
        db_saltot.setValue(data[0]);
    }

    public double[] calcularTotales() {
        double totales[] = new double[1];
        for (int i = 0; i < objlstCtaCobCab.getSize(); i++) {
            totales[0] = totales[0] + ((CtaCobCab) objlstCtaCobCab.get(i)).getCtacob_saldo();
        }
        return totales;
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_doc_id.setValue("");
        txt_doc_des.setValue("");
        txt_doc_des_des.setValue("");
        txt_direcc.setValue("");
        txt_ruc.setValue(null);
        txt_ven_id.setValue("");
        txt_ven_des.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_horid.setValue("");
        txt_hordes.setValue("");
        txt_diavis_id.setValue("");
        txt_diavis_des.setValue("");
        txt_movil.setValue("");
        txt_giro.setValue("");
        d_fecnac.setValue(null);
        cb_canal.setValue("");
        cb_canal.setSelectedIndex(-1);
        chk_cliest.setChecked(true);
        chk_cliest.setLabel("ACTIVO");
        txt_ref.setValue("");

        //Detalle
        db_limcred.setValue(null);
        db_totlimcred.setValue(null);
        txt_limdoc.setValue(null);
        txt_totlimdoc.setValue(null);
        txt_cate_id.setValue("");
        txt_cate_des.setValue("");
        txt_pdscto.setValue(null);
        txt_docemi.setValue("");
        db_saltot.setValue(null);
        txt_pccred.setValue("");
        txt_proatra.setValue("");
        txt_nrecla.setValue("");
        txt_ncsa.setValue("");
        chk_finest.setChecked(false);
    }

    public void limpiarLista() {
        objlstCtaCobCab = new ListModelList<CtaCobCab>();
        lst_ctacob.setModel(objlstCtaCobCab);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void seleccionaCheck(boolean disable) {
        chk_ref.setDisabled(disable);
        chk_ref.setChecked(!disable);
    }

    //metodos sin utilizar
    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonDeshacer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
