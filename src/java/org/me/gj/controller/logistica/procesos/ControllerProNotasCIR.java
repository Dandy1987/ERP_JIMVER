package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.logistica.mantenimiento.DaoMotivoCambio;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.mantenimiento.MotCam;
import org.me.gj.model.logistica.procesos.NotaCIR;
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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerProNotasCIR extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Listbox lst_notascir;
    @Wire
    Textbox txt_ven_id, txt_ven_apenom, txt_mcam_id, txt_mcam_des, txt_cli_id, txt_cli_razsoc;
    @Wire
    Datebox d_fecemi, d_fecgen;
    @Wire
    Combobox cb_situacion, cb_tipnota, cb_periodo, cb_procesar;
    @Wire
    Button btn_procesar;
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<NotaCIR> objlstNotasCIR;
    ListModelList<ManPer> objlstManPers;
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoNotasCIR objDaoNotasCIR;
    NotaCIR objNotaCIR;
    //Variables publicas
    int emp_id, suc_id, i_sel;
    String fechaActual, periodoActual;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProNotasCIR.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        sesion = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        fechaActual = Utilitarios.hoyAsString();
        periodoActual = Utilitarios.periodoActual();
        objDaoNotasCIR = new DaoNotasCIR();
        objlstNotasCIR = objDaoNotasCIR.listaNotasCIR(emp_id, suc_id, "%%", "%%", "%%", periodoActual, "235", "%%", fechaActual);
        lst_notascir.setModel(objlstNotasCIR);
        objlstManPers = new DaoManPer().listaPeriodos(1);
        objlstManPers.add(new ManPer("", ""));
        cb_periodo.setModel(objlstManPers);
        cb_tipnota.focus();
        cb_tipnota.select();
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10213000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a Procesar C-I-R con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a Procesar C-I-R con el rol: USUARIO NORMAL");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void buscarNotasCIR() throws SQLException {
        String cli_key = txt_cli_id.getValue().isEmpty() ? "%%" : String.valueOf(Long.parseLong(txt_cli_id.getValue()));
        String ven_key = txt_ven_id.getValue().isEmpty() ? "%%" : String.valueOf(Integer.parseInt(txt_ven_id.getValue()));
        String mcam_id = txt_mcam_id.getValue().isEmpty() ? "%%" : String.valueOf(Integer.parseInt(txt_mcam_id.getValue()));
        String nottip_key = cb_tipnota.getSelectedIndex() == -1 ? "%%" : cb_tipnota.getSelectedItem().getValue().toString();
        String fec_emi = d_fecemi.getValue() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(d_fecemi.getValue());
        String not_periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedItem().getValue() == "" ? "%%" : cb_periodo.getSelectedItem().getValue().toString();
        String not_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        chk_selecAll.setChecked(false);
        objlstNotasCIR = null;
        lst_notascir.setModel(objlstNotasCIR);

        objlstNotasCIR = objDaoNotasCIR.listaNotasCIR(emp_id, suc_id, nottip_key, ven_key, cli_key, not_periodo, not_sit, mcam_id, fec_emi);
        if (objlstNotasCIR.getSize() > 0) {
            lst_notascir.setModel(objlstNotasCIR);
        } else {
            objlstNotasCIR = null;
            objlstNotasCIR = new ListModelList<NotaCIR>();
            lst_notascir.setModel(objlstNotasCIR);
            Messagebox.show("No existe registro en Procesar C-I-R", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onSelect=#lst_notascir")
    public void seleccionaRegistro() throws SQLException {
        objNotaCIR = (NotaCIR) lst_notascir.getSelectedItem().getValue();
        if (objNotaCIR == null) {
            objNotaCIR = objlstNotasCIR.get(i_sel + 1);
        }
        i_sel = lst_notascir.getSelectedIndex();
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotasCIR.isEmpty()) {
            Messagebox.show("No hay Registros de Nota de CIR", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotasCIR.getSize(); i++) {
                if (objlstNotasCIR.get(i).getNot_tipkey() != 2) {//si no es recojo
                    if (objlstNotasCIR.get(i).getNot_sit() == 2 || objlstNotasCIR.get(i).getNot_sit() == 5 && (objlstNotasCIR.get(i).getNot_notaent() == null) || objlstNotasCIR.get(i).getNot_sit() == 6 && (objlstNotasCIR.get(i).getNot_notaent() == null) || objlstNotasCIR.get(i).getNot_sit() == 7 && (objlstNotasCIR.get(i).getNot_notaent() == null)) {
                        objlstNotasCIR.get(i).setValSelec(chk_selecAll.isChecked());
                    }
                } else {//si es recojo
                    if (objlstNotasCIR.get(i).getNot_sit() == 2 || objlstNotasCIR.get(i).getNot_sit() == 5 && (objlstNotasCIR.get(i).getNot_notaent() == null) || objlstNotasCIR.get(i).getNot_sit() == 6 && (objlstNotasCIR.get(i).getNot_notaent() == null) || objlstNotasCIR.get(i).getNot_sit() == 7 && (objlstNotasCIR.get(i).getNot_notaent() == null)) {
                        objlstNotasCIR.get(i).setValSelec(chk_selecAll.isChecked());
                    }
                }
            }
            lst_notascir.setModel(objlstNotasCIR);
        }
    }

    @Listen("onSeleccion=#lst_notascir")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstNotasCIR.get(item.getIndex()).getNot_tipkey() != 2) {//si no es recojo
            if (objlstNotasCIR.get(item.getIndex()).getNot_sit() == 1 || objlstNotasCIR.get(item.getIndex()).getNot_sit() == 4 || objlstNotasCIR.get(item.getIndex()).getNot_sit() == 3 || (objlstNotasCIR.get(item.getIndex()).getNot_notaent() != null && objlstNotasCIR.get(item.getIndex()).getNot_notasal() != null)) {
                Messagebox.show("La Nota ya no puede ser Procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                chk_Reg.setChecked(false);
            } else {
                objlstNotasCIR.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
                lst_notascir.setModel(objlstNotasCIR);
            }
        } else {//si es recojo
            if (objlstNotasCIR.get(item.getIndex()).getNot_sit() == 1 || objlstNotasCIR.get(item.getIndex()).getNot_sit() == 4 || objlstNotasCIR.get(item.getIndex()).getNot_sit() == 3 || (objlstNotasCIR.get(item.getIndex()).getNot_notaent() != null)) {
                Messagebox.show("La Nota ya no puede ser Procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                chk_Reg.setChecked(false);
            } else {
                objlstNotasCIR.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
                lst_notascir.setModel(objlstNotasCIR);
            }
        }
    }

    @Listen("onClick=#btn_procesar")
    public void procesarNotaEntradaSalida() throws SQLException {
        if (d_fecgen.getValue() == null) {
            Messagebox.show("Por Favor Ingrese una Fecha Valida", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        d_fecgen.focus();
                    }
                }
            });
        } else if (objlstNotasCIR == null || objlstNotasCIR.isEmpty()) {
            Messagebox.show("No hay Registros para Procesar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (cb_procesar.getSelectedIndex() == -1) {
            Messagebox.show("Por favor, selecciona el Tipo de Procesado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        cb_procesar.focus();
                    }
                }
            });
        } else {
            final Date fecha = d_fecgen.getValue();
            String resultado = "";
            int i = 0;
            for (int j = 0; j < objlstNotasCIR.getSize(); j++) {
                if (objlstNotasCIR.get(j).isValSelec()) {
                    resultado = Utilitarios.compareFechas(objlstNotasCIR.get(j).getNot_fecemi(), fecha);
                    i = i + 1;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (!ValidaNIC().isEmpty()) {
                Messagebox.show(ValidaNIC(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (resultado.equals("F1>")) {
                    Messagebox.show("La Fecha Emision no puede ser mayor a la Fecha de Generacion", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    Messagebox.show("¿Esta Seguro que desea Procesar los Registros Seleccionados?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        NotaCIR auxNotaCIR;
                                        ParametrosSalida objParamSalida = new ParametrosSalida();
                                        String s_tnotaa = cb_procesar.getSelectedItem().getValue();
                                        String s_mensaje = "Informe de Notas Procesadas \n\n";
                                        int i = 0;
                                        while (i < objlstNotasCIR.size() && objParamSalida.getFlagIndicador() == 0) {
                                            if (objlstNotasCIR.get(i).isValSelec()) {
                                                auxNotaCIR = objlstNotasCIR.get(i);
                                                if (auxNotaCIR.getNot_tipkey() == 1) {
                                                    objParamSalida = objDaoNotasCIR.procesarNotaCambio(auxNotaCIR, s_tnotaa, auxNotaCIR.getNot_sit(), fecha);
                                                    if (objParamSalida.getFlagIndicador() == 0) {
                                                        s_mensaje = s_mensaje + "Nota Cambio N° " + auxNotaCIR.getNot_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                                    } else {
                                                        s_mensaje = objParamSalida.getMsgValidacion();
                                                    }
                                                } else if (auxNotaCIR.getNot_tipkey() == 2) {
                                                    if (s_tnotaa.equals("E")) {
                                                        objParamSalida = objDaoNotasCIR.procesarNotaRecojo(auxNotaCIR, s_tnotaa, auxNotaCIR.getNot_sit(), fecha);
                                                        s_mensaje = s_mensaje + "Nota Recojo N° " + auxNotaCIR.getNot_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                                    }
                                                } else {
                                                    objParamSalida = objDaoNotasCIR.procesarNotaIntercambio(auxNotaCIR, s_tnotaa, auxNotaCIR.getNot_sit(), fecha);
                                                    s_mensaje = s_mensaje + "Nota Intercambio N° " + auxNotaCIR.getNot_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                                }
                                            }
                                            i++;
                                        }
                                        buscarNotasCIR();
                                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        String periodo = Utilitarios.periodoActual();
                                        ParametrosSalida objParametrosSalida = new DaoRegeneraStock().regenerarStock(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), periodo, objUsuCredential.getCuenta());
                                        if (objParametrosSalida.getFlagIndicador() != 0) {
                                            Messagebox.show("No se pudo regenerar el stock");
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#cb_periodo")
    public void periodo() {
        txt_ven_id.focus();
    }

    @Listen("onOK=#txt_ven_id")
    public void lov_vendedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_ven_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "CIR";
                objMapObjetos.put("txt_venid", txt_ven_id);
                objMapObjetos.put("txt_vennom", txt_ven_apenom);
                objMapObjetos.put("txt_motid", txt_mcam_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotasCIR");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_mcam_id.focus();
            }
        }
    }

    @Listen("onBlur=#txt_ven_id")
    public void validaVendedor() throws SQLException {
        txt_ven_apenom.setValue("");
        if (!txt_ven_id.getValue().isEmpty()) {
            if (!txt_ven_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_ven_id.setValue("");
                            txt_ven_apenom.setValue("");
                            txt_ven_id.focus();
                        }
                    }
                });

            } else {
                int ven_id = Integer.parseInt(txt_ven_id.getValue());
                Vendedor objVendedor = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedor == null) {
                    Messagebox.show("El Codigo de Vendedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ven_id.setValue("");
                                txt_ven_apenom.setValue("");
                                txt_ven_id.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedor.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_ven_id.setValue(objVendedor.getVen_id());
                    txt_ven_apenom.setValue(objVendedor.getVen_ape() + " " + objVendedor.getVen_nom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_mcam_id")
    public void lov_motivoCambio() {
        if (bandera == false) {
            bandera = true;
            if (txt_mcam_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "CIR";
                objMapObjetos.put("txt_motid", txt_mcam_id);
                objMapObjetos.put("txt_motdes", txt_mcam_des);
                objMapObjetos.put("d_fecemi", d_fecemi);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotasCIR");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoCambio.zul", null, objMapObjetos);
                window.doModal();
            } else {
                d_fecemi.focus();
            }
        }
    }

    @Listen("onBlur=#txt_mcam_id")
    public void validaMotivoCambio() throws SQLException {
        txt_mcam_des.setValue("");
        if (!txt_mcam_id.getValue().isEmpty()) {
            if (!txt_mcam_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_mcam_id.setValue("");
                            txt_mcam_des.setValue("");
                            txt_mcam_id.focus();
                        }
                    }
                });
            } else {
                String ven_id = String.valueOf(Integer.parseInt(txt_mcam_id.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(ven_id);
                if (objMotivoCambio == null) {
                    Messagebox.show("El Codigo de Motivo de Cambio no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_mcam_id.setValue("");
                                txt_mcam_des.setValue("");
                                txt_mcam_id.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Motivo de Cambio " + objMotivoCambio.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_mcam_id.setValue(objMotivoCambio.getTab_subdir());
                    txt_mcam_des.setValue(objMotivoCambio.getTab_subdes());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_cli_id")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "CIR";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("cb_situacion", cb_situacion);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotasCIR");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClientes() throws SQLException {
        txt_cli_razsoc.setValue("");
        if (!txt_cli_id.getValue().isEmpty()) {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_cli_id.setValue("");
                            txt_cli_razsoc.setValue("");
                            txt_cli_id.focus();
                        }
                    }
                });
            } else {
                String cli_id = Utilitarios.lpad(String.valueOf(Integer.parseInt(txt_cli_id.getValue())), 10, "0");
                Cliente objCliente = new DaoCliente().getNomCliente(cli_id);
                if (objCliente == null) {
                    Messagebox.show("El Codigo del Cliente no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_cli_id.setValue("");
                                txt_cli_razsoc.setValue("");
                                txt_cli_id.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + "y ha encontrado 1 registro(s)");
                    txt_cli_id.setValue(objCliente.getCli_id());
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#d_fecemi")
    public void validaFechaEmision() {
        txt_cli_id.focus();
    }

    @Listen("onOK=#cb_tipnota")
    public void validaTipoNota() {
        cb_periodo.focus();
        cb_periodo.select();
    }

    @Listen("onOK=#cb_situacion")
    public void situacion() throws SQLException {
        buscarNotasCIR();
    }

    //Eventos Otros 
    public String ValidaNIC() {
        int i = 0;
        NotaCIR auxNotaCIR;
        boolean valida = true;
        String s_mensaje = "", tipomov = cb_procesar.getSelectedItem().getValue();
        while (i < objlstNotasCIR.getSize() && valida) {
            if (objlstNotasCIR.get(i).isValSelec()) {
                auxNotaCIR = objlstNotasCIR.get(i);
                if (auxNotaCIR.getNot_sit() == 2) {
                    if (auxNotaCIR.getNot_tipkey() == 1) {
                        if (tipomov.equals("E")) {
                            s_mensaje = "Por favor seleccionar 'SALIDA DE MERCADERIA' para Cambio";
                            valida = false;
                        }
                    } else if (auxNotaCIR.getNot_tipkey() == 2) {
                        if (tipomov.equals("E")) {
                            s_mensaje = "Por favor seleccionar 'SALIDA DE MERCADERIA' para Recojo ";
                            valida = false;
                        }
                    } else {
                        if (tipomov.equals("E")) {
                            s_mensaje = "Por favor seleccionar 'SALIDA DE MERCADERIA' para Intercambio";
                            valida = false;
                        }
                    }
                } else {
                    if (auxNotaCIR.getNot_tipkey() == 1) {
                        if (tipomov.equals("S")) {
                            s_mensaje = "Por favor seleccionar 'ENTRADA DE MERCADERIA' para Cambio";
                            valida = false;
                        }
                    } else if (auxNotaCIR.getNot_tipkey() == 2) {
                        if (tipomov.equals("S")) {
                            s_mensaje = "Por favor seleccionar 'ENTRADA DE MERCADERIA' para Recojo ";
                            valida = false;
                        }
                    } else {
                        if (tipomov.equals("S")) {
                            s_mensaje = "Por favor seleccionar 'ENTRADA DE MERCADERIA' para Intercambio";
                            valida = false;
                        }
                    }
                }
            }
            i++;
        }
        return s_mensaje;
    }
}
