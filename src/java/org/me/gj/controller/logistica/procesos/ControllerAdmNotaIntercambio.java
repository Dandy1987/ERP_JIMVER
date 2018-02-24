package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.procesos.NotaInterCab;
import org.me.gj.model.logistica.procesos.NotaInterDet;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerAdmNotaIntercambio extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_aprobar, tbbtn_btn_rechazar, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_notintercab, lst_notinterdet;
    @Wire
    Datebox d_fecini, d_fecfin,
            d_ni_fecemi, d_ni_fecent,//DATOS DE LA NOTA DE INTERCAMBIO CABECERA
            d_fecadd, d_fecmod;//------>Datos de la Nota Cambio Cabecera
    @Wire
    Doublebox txt_nid_preident, txt_nid_preidsal, txt_nid_pretotalsal, txt_nid_pretotalent;
    @Wire
    Combobox cb_situacion;
    @Wire
    Combobox cb_nid_tipdoc;//TIPO DOCUMENTO NOTA DE INTERCAMBIO DETALLE
    @Wire
    Intbox txt_nid_tipdoc, txt_nid_enteroent, txt_nid_fracent, txt_nid_enterosal, txt_nid_fracsal, txt_nid_upreent, txt_nid_upresal;//DATOS DE LA NOTA DE INTERCAMBIO DETALLE
    @Wire
    Textbox txt_ven_id, txt_ven_apenom, txt_cli_razsoc, txt_cli_id,
            txt_usuadd, txt_usumod,//------>Datos de la Nota Cambio Cabecera
            txt_venid, txt_vennom,//DATOS PARA LA BUSQUEDA
            txt_ni_id, txt_ni_motcamid, txt_ni_motcamdes, txt_cli_iddet, txt_cli_razsocdet, txt_clidir_direcc, txt_zon_id, txt_zon_des,
            txt_ni_transid, txt_ni_transdes, txt_ni_venid, txt_sup_id, txt_ni_vennom, txt_ni_horid, txt_ni_hordes, txt_ni_registro, txt_ni_depurado,
            txt_ni_notaent, txt_ni_notasal, txt_ni_provid, txt_ni_provdes, txt_ni_lpcid, txt_ni_lpcdes,//DATOS DE LA NOTA DE INTERCAMBIO CABECERA
            txt_nid_proident, txt_nid_prodesent, txt_nid_proidsal, txt_nid_prodessal,
            txt_nid_glosa, txt_nid_doc, txt_nid_serie;//DATOS DE LA NOTA DE INTERCAMBIO DETALLE
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<NotaInterCab> objlstNotaInterCab;
    ListModelList<NotaInterDet> objlstNotaInterDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    NotaInterDet objNotaInterDet;
    NotaInterCab objNotaInterCab;
    DaoNotaIntercambio objDaoNotaInter;
    DaoAccesos objDaoAccesos;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    Accesos objAccesos;
    //Variables publicas
    String fechaActual, s_estado, s_mensaje, s_estadoDetalle;
    int emp_id, suc_id;
    int i_selCab, i_selDet;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion    
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAdmNotaIntercambio.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objlstNotaInterCab = null;
        objDaoNotaInter = new DaoNotaIntercambio();
        objlstNotaInterCab = objDaoNotaInter.listaNotInterCabApro(emp_id, suc_id, "%%", "%%", "1", "2", "4", fechaActual, fechaActual);
        lst_notintercab.setModel(objlstNotaInterCab);
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(1);
        cb_nid_tipdoc.setModel(objlstTipDoc);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10208030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Lineas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Lineas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_aprobar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a la Autorizacion de Notas de Cambio");
        } else {
            tbbtn_btn_aprobar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a la Autorizacion de Notas de Cambio");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_rechazar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a el Rechazo de Notas de Cambio");
        } else {
            tbbtn_btn_rechazar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a el Rechazo de Notas de Cambio");
        }
        /*if (objAccesos.getAcc_imp() == 1) {
         tbbtn_btn_imprimir.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a la impresion de Notas de Cambio");
         } else {
         tbbtn_btn_imprimir.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a la impresion de Notas de Cambio");
         }*/
    }

    @Listen("onClick=#btn_buscarinter")
    public void busquedaRegistros() throws SQLException {
        limpiarTabLista(1);
        limpiarTabLista(2);
        String resultado;
        Date fecha_emisioni = d_fecini.getValue();
        Date fecha_emisionf = d_fecfin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_fecini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "01/01/3000";
        } else {
            f_emisionf = sdf.format(d_fecfin.getValue());
        }
        String cli_id = txt_cli_id.getValue().isEmpty() ? "%%" : txt_cli_id.getValue().replace("0", "").trim();
        String ven_id = txt_ven_id.getValue().isEmpty() ? "%%" : txt_ven_id.getValue().replace("0", "").trim();
        String situacion = cb_situacion.getSelectedIndex() == -1 ? "124" : cb_situacion.getSelectedItem().getValue().toString();
        String sitIng = situacion.substring(0, 1);
        String sitApro = situacion.substring(1, 2);
        String sitRec = situacion.substring(2, 3);
        limpiarCampos();
        limpiarCamposDetalle();
        if (resultado.equals("F1>")) {
            Messagebox.show("La Fecha Inicial no puede ser mayor a la Final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstNotaInterCab = objDaoNotaInter.listaNotInterCabApro(emp_id, suc_id, cli_id, ven_id, sitIng, sitApro, sitRec, f_emisioni, f_emisionf);
            //Validar tabla vacia
            if (objlstNotaInterCab.getSize() > 0) {
                lst_notintercab.setModel(objlstNotaInterCab);
            } else {
                limpiarTabLista(1);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            limpiarTabLista(2);
            limpiaAuditoria();
            chk_selecAll.setChecked(false);
        }
    }

    @Listen("onSelect=#lst_notintercab")
    public void seleccionaRegistro() throws SQLException {
        objNotaInterCab = (NotaInterCab) lst_notintercab.getSelectedItem().getValue();
        if (objNotaInterCab == null) {
            objNotaInterCab = objlstNotaInterCab.get(i_selCab + 1);
        }
        i_selCab = lst_notintercab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotaInterCab.isEmpty()) {
            Messagebox.show("No hay Registros de Nota de Intercambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaInterCab.getSize(); i++) {
                if (objlstNotaInterCab.get(i).getNi_sit() != 2 && objlstNotaInterCab.get(i).getNi_sit() != 4) {
                    objlstNotaInterCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_notintercab.setModel(objlstNotaInterCab);
        }
    }

    @Listen("onSeleccion=#lst_notintercab")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstNotaInterCab.get(item.getIndex()).getNi_sit() == 2 || objlstNotaInterCab.get(item.getIndex()).getNi_sit() == 4) {
            Messagebox.show("La Nota de Intercambio ya no puede ser Procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstNotaInterCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_notintercab.setModel(objlstNotaInterCab);

        }
    }

    @Listen("onSelect=#lst_notinterdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaInterDet = (NotaInterDet) lst_notinterdet.getSelectedItem().getValue();
        if (objNotaInterDet == null) {
            objNotaInterDet = objlstNotaInterDet.get(i_selDet + 1);
        }
        i_selDet = lst_notinterdet.getSelectedIndex();
        limpiarCamposDetalle();
        llenarCamposProducto(objNotaInterDet);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaInterCab == null || objlstNotaInterCab.isEmpty()) {
            Messagebox.show("No hay Registros de Autorizar Notas de Intercambio para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/LovImpresionAdmPedCom.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_ven_id")
    public void lov_vendedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_ven_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambioAuto";
                objMapObjetos.put("txt_venid", txt_ven_id);
                objMapObjetos.put("txt_vennom", txt_ven_apenom);
                objMapObjetos.put("txt_cli_id", txt_cli_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambioAuto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onBlur=#txt_ven_id")
    public void validaVendedor() throws SQLException {
        txt_ven_apenom.setValue("");
        if (!txt_ven_id.getValue().isEmpty()) {
            if (!txt_ven_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_ven_id.setValue("");
            } else {
                int ven_id = Integer.parseInt(txt_ven_id.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El Codigo de Vendedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_ven_id.setValue("");
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedorCon.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_ven_id.setValue(objVendedorCon.getVen_id());
                    txt_ven_apenom.setValue(objVendedorCon.getVen_ape() + " " + objVendedorCon.getVen_nom());
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
                String modoEjecucion = "NotaIntercambioAuto";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambioAuto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClientes() throws SQLException {
        txt_cli_razsoc.setValue("");
        if (!txt_cli_id.getValue().isEmpty()) {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_cli_id.setValue("");
            } else {
                String cli_id = txt_cli_id.getValue();
                Cliente objCliente = new DaoCliente().getDireccionDefault(cli_id, emp_id, suc_id);
                if (objCliente == null) {
                    Messagebox.show("El Codigo del Cliente no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_cli_id.setValue("");
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + "y ha encontrado 1 registro(s)");
                    txt_cli_id.setValue(objCliente.getCli_id());
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                }
            }
        }
        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_aprobar")
    public void autorizarNotaIntercambio() {
        if (objlstNotaInterCab == null || objlstNotaInterCab.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaInterCab.getSize(); j++) {
                if (objlstNotaInterCab.get(j).isValSelec()) {
                    i = i + 1;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                relizarAutorizacion("Autorizar", "Autorizacion", 2);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_rechazar")
    public void RechazarNotaIntercambio() {
        if (objlstNotaInterCab == null || objlstNotaInterCab.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaInterCab.getSize(); j++) {
                if (objlstNotaInterCab.get(j).isValSelec()) {
                    i = i + 1;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                relizarAutorizacion("Rechazar", "Rechazos", 4);
            }
        }
    }

    //Eventos Otros
    public void relizarAutorizacion(String acc1, final String acc2, final int sit) {
        s_mensaje = "Esta Seguro que desea " + acc1 + " las Notas de Intercambio Seleccionadas?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            String lstNotInter = "Informacion de " + acc2 + " de N.I. \n\n ";
                            ParametrosSalida objParamSalida;
                            for (int i = 0; i < objlstNotaInterCab.getSize(); i++) {
                                if (objlstNotaInterCab.get(i).isValSelec() && objlstNotaInterCab.get(i).getNi_sit() == 1) {
                                    objlstNotaInterCab.get(i).setNi_autusuadd(objUsuCredential.getCuenta());
                                    objlstNotaInterCab.get(i).setNi_autpcadd(objUsuCredential.getComputerName());
                                    objlstNotaInterCab.get(i).setNi_sit(sit);
                                    objParamSalida = objDaoNotaInter.autorizarNotaIntercambio(objlstNotaInterCab.get(i));
                                    lstNotInter += "N.I: " + objlstNotaInterCab.get(i).getNi_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                }
                            }
                            busquedaRegistros();
                            Messagebox.show(lstNotInter, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    }
                });
    }

    public void llenarCampos() {
        txt_ni_id.setValue(objNotaInterCab.getNi_id());
        d_ni_fecemi.setValue(objNotaInterCab.getNi_fecemi());
        d_ni_fecent.setValue(objNotaInterCab.getNi_fecent());
        txt_ni_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_motcam()), 3, "0"));
        txt_ni_motcamdes.setValue(objNotaInterCab.getMcam_des());
        txt_ni_provid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNic_provid()), 8, "0"));
        txt_ni_provdes.setValue(objNotaInterCab.getNic_provrazsoc());
        txt_ni_lpcid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNic_lpcid()), 4, "0"));
        txt_ni_lpcdes.setValue(objNotaInterCab.getNic_lpcdes());
        txt_cli_iddet.setValue(objNotaInterCab.getCli_id());
        txt_cli_razsocdet.setValue(objNotaInterCab.getCli_razsoc());
        txt_ni_notaent.setValue(objNotaInterCab.getNi_notaent());
        txt_ni_notasal.setValue(objNotaInterCab.getNi_notasal());
        txt_zon_id.setValue(objNotaInterCab.getNi_zona());
        txt_zon_des.setValue(objNotaInterCab.getZon_des());
        txt_ni_venid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_vend()), 4, "0"));
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_sup()), 4, "0"));
        txt_ni_vennom.setValue(objNotaInterCab.getVen_apenom());
        txt_ni_transid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_trans()), 4, "0"));
        txt_ni_transdes.setValue(objNotaInterCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaInterCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaInterCab.getClidir_direcc());
        txt_ni_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_hor()), 3, "0"));
        txt_ni_hordes.setValue(objNotaInterCab.getHor_des());
        txt_ni_registro.setValue(objNotaInterCab.getNi_nroreg());
        txt_ni_depurado.setValue(objNotaInterCab.getNi_nrodep());
        txt_usuadd.setValue(objNotaInterCab.getNi_usuadd());
        d_fecadd.setValue(objNotaInterCab.getNi_fecadd());
        txt_usumod.setValue(objNotaInterCab.getNi_usumod());
        d_fecmod.setValue(objNotaInterCab.getNi_fecmod());
    }

    public void limpiarCampos() {
        txt_ni_id.setValue("");
        d_ni_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_ni_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_ni_motcamid.setValue("");
        txt_ni_motcamdes.setValue("");
        txt_ni_provid.setValue("");
        txt_ni_provdes.setValue("");
        txt_ni_lpcdes.setValue("");
        txt_ni_lpcid.setValue("");
        txt_cli_iddet.setValue("");
        txt_cli_razsocdet.setValue("");
        txt_ni_notaent.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_ni_venid.setValue("");
        txt_sup_id.setValue("");
        txt_ni_vennom.setValue("");
        txt_ni_transid.setValue("");
        txt_ni_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_ni_horid.setValue("");
        txt_ni_hordes.setValue("");
        txt_ni_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_nid_doc.setValue("");
        txt_nid_serie.setValue("");
        txt_nid_prodessal.setValue("");
        txt_nid_proidsal.setValue("");
        txt_nid_enterosal.setValue(null);
        txt_nid_fracsal.setValue(null);
        txt_nid_prodesent.setValue("");
        txt_nid_proident.setValue("");
        txt_nid_preident.setValue(null);
        txt_nid_preidsal.setValue(null);
        txt_nid_enteroent.setValue(null);
        txt_nid_fracent.setValue(null);
        txt_nid_glosa.setValue("");
        txt_nid_upresal.setValue(null);
        txt_nid_upreent.setValue(null);
        txt_nid_pretotalent.setValue(null);
        txt_nid_pretotalsal.setValue(null);
        cb_nid_tipdoc.setSelectedIndex(-1);
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaInterDet = null;
        long nr_key = objNotaInterCab.getNi_key();
        objlstNotaInterDet = objDaoNotaInter.listaNotaInterDet(emp_id, suc_id, nr_key);
        lst_notinterdet.setModel(objlstNotaInterDet);
    }

    private void llenarCamposProducto(NotaInterDet objNotaInterDet) {
        if (lst_notinterdet.getSelectedIndex() % 2 == 0) {
            txt_nid_proident.setValue(objNotaInterDet.getPro_id());
            txt_nid_prodesent.setValue(objNotaInterDet.getPro_des());
            txt_nid_preident.setValue(objNotaInterDet.getNid_precio());
            txt_nid_upreent.setValue(objNotaInterDet.getPro_presmin());

            txt_nid_enteroent.setValue(objNotaInterDet.getNid_cantent());
            txt_nid_fracent.setValue(objNotaInterDet.getNid_cantfrac());
            txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));

            //Producto Saliente
            for (int i = 0; i < objlstNotaInterDet.getSize(); i++) {
                if (("S").equals(objlstNotaInterDet.get(i).getNid_indicador().toString()) && objlstNotaInterDet.get(i).getNid_docref().equals(objNotaInterDet.getNid_serie() + objNotaInterDet.getNid_doc().toString())) {
                    txt_nid_proidsal.setValue(objlstNotaInterDet.get(i).getPro_id());
                    txt_nid_prodessal.setValue(objlstNotaInterDet.get(i).getPro_des());
                    txt_nid_enterosal.setValue(objlstNotaInterDet.get(i).getNid_cantent());
                    txt_nid_fracsal.setValue(objlstNotaInterDet.get(i).getNid_cantfrac());
                    txt_nid_preidsal.setValue(objlstNotaInterDet.get(i).getNid_precio());
                    txt_nid_upresal.setValue(objlstNotaInterDet.get(i).getPro_presmin());
                    txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));

                }
            }
        } else {
            txt_nid_proidsal.setValue(objNotaInterDet.getPro_id());
            txt_nid_prodessal.setValue(objNotaInterDet.getPro_des());
            txt_nid_preidsal.setValue(objNotaInterDet.getNid_precio());
            txt_nid_upresal.setValue(objNotaInterDet.getPro_presmin());

            txt_nid_enterosal.setValue(objNotaInterDet.getNid_cantent());
            txt_nid_fracsal.setValue(objNotaInterDet.getNid_cantfrac());
            txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));

            //Producto Entrante
            for (int i = 0; i < objlstNotaInterDet.getSize(); i++) {
                if (("E").equals(objlstNotaInterDet.get(i).getNid_indicador().toString()) && objlstNotaInterDet.get(i).getNid_docref().equals(objNotaInterDet.getNid_serie() + objNotaInterDet.getNid_doc().toString())) {
                    txt_nid_proident.setValue(objlstNotaInterDet.get(i).getPro_id());
                    txt_nid_prodesent.setValue(objlstNotaInterDet.get(i).getPro_des());
                    txt_nid_enteroent.setValue(objlstNotaInterDet.get(i).getNid_cantent());
                    txt_nid_fracent.setValue(objlstNotaInterDet.get(i).getNid_cantfrac());
                    txt_nid_preident.setValue(objlstNotaInterDet.get(i).getNid_precio());
                    txt_nid_upreent.setValue(objlstNotaInterDet.get(i).getPro_presmin());
                    txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                }
            }
        }

        String tipo_doc = String.valueOf(objNotaInterDet.getNid_tipdoc());
        if (tipo_doc != null || !tipo_doc.isEmpty()) {
            cb_nid_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nid_tipdoc, tipo_doc));
        } else {
            cb_nid_tipdoc.setSelectedIndex(-1);
        }

        txt_nid_doc.setValue(objNotaInterDet.getNid_doc().isEmpty() ? "" : objNotaInterDet.getNid_doc());
        txt_nid_serie.setValue(objNotaInterDet.getNid_serie().isEmpty() ? "" : objNotaInterDet.getNid_serie());
        txt_nid_glosa.setValue(objNotaInterDet.getNid_glosa());
    }

    public String verificarFechas() {
        String s_valor = "";
        String fecha_ini = sdf.format(d_fecini.getValue());
        if (d_fecfin.getValue().getTime() < d_fecini.getValue().getTime()) {
            s_valor = "La fecha de Final debe ser Mayor o igual que : " + fecha_ini;
        }
        return s_valor;
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarTabLista(int i) {
        if (i == 1) {
            objlstNotaInterCab = null;
            objlstNotaInterCab = new ListModelList<NotaInterCab>();
            lst_notintercab.setModel(objlstNotaInterCab);
        } else {
            objlstNotaInterDet = null;
            objlstNotaInterDet = new ListModelList<NotaInterDet>();
            lst_notinterdet.setModel(objlstNotaInterDet);
        }
    }
}
