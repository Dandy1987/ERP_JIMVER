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
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.procesos.NotCambCab;
import org.me.gj.model.logistica.procesos.NotCambDet;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerAdmNotaCambio extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_aprobar, tbbtn_btn_rechazar, tbbtn_btn_imprimir,
            tbbtn_btn_nuevonotcam, tbbtn_btn_editarnotcam, tbbtn_btn_eliminarnotcam, tbbtn_btn_guardarnotcam, tbbtn_btn_deshacernotcam;//Botones Nota de Cambio Detalle
    @Wire
    Listbox lst_notcambcab, lst_notcambdet;
    @Wire
    Datebox d_fecini, d_fecfin,
            d_fecadd, d_fecmod,//------>Datos de la Nota Cambio Cabecera 
            d_nc_fecemi, d_nc_fecent;//Datos de la Nota Cambio Cabecera 
    @Wire
    Combobox cb_situacion, cb_ncd_tipdoc;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Textbox txt_ven_id, txt_ven_apenom, txt_cli_razsoc, txt_cli_id,
            txt_usuadd, txt_usumod,//------>Datos de la Nota Cambio Cabecera 
            txt_venid, txt_vennom,//Datos para la busqueda
            txt_nc_id, txt_nc_motcamid, txt_nc_motcamdes, txt_cli_iddet, txt_cli_razsocdet, txt_nc_depurado, txt_nc_notaent, txt_nc_notasal, txt_zon_id,
            txt_zon_des, txt_clidir_direcc, txt_nc_horid, txt_nc_hordes, txt_nc_registro,
            txt_sup_id, txt_nc_venid, txt_nc_vennom, txt_nc_transid, txt_nc_transdes, txt_ncd_stock,//Datos de la Nota Cambio Cabecera
            txt_ncd_prodid, txt_ncd_proddes, txt_ncd_doc, txt_ncd_serie, txt_ncd_glosanotcam;//Datos de la Nota Cambio Detalle
    @Wire
    Intbox //Datos de la Nota Cambio Cabecera
            txt_ncd_entero, txt_ncd_fraccion, txt_ncd_upre;//Datos de la Nota Cambio Detalle
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<NotCambCab> objlstNotaCambioCab;
    ListModelList<NotCambDet> objlstNotaCambioDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    NotCambCab objNotaCambioCab;
    NotCambDet objNotaCambioDet;
    DaoNotaCambio objDaoNotaCambio;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    String fechaActual, s_estado, s_mensaje, s_estadoDetalle;
    int emp_id, suc_id;
    int i_selCab, i_selDet;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAdmNotaCambio.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objlstNotaCambioCab = null;
        objDaoNotaCambio = new DaoNotaCambio();
        objlstNotaCambioCab = objDaoNotaCambio.listaNotCambioCabApro(emp_id, suc_id, "%%", "%%", "1", "2", "4", fechaActual, fechaActual);
        lst_notcambcab.setModel(objlstNotaCambioCab);
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_ncd_tipdoc.setModel(objlstTipDoc);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10208010, usuario, empresa, sucursal);
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

    @Listen("onClick=#btn_buscarcamb")
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
            objlstNotaCambioCab = objDaoNotaCambio.listaNotCambioCabApro(emp_id, suc_id, cli_id, ven_id, sitIng, sitApro, sitRec, f_emisioni, f_emisionf);
            //Validar tabla sin registro
            if (objlstNotaCambioCab.getSize() > 0) {
                lst_notcambcab.setModel(objlstNotaCambioCab);
            } else {
                limpiarTabLista(1);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            //limpiarTabLista(2);
            limpiaAuditoria();
            chk_selecAll.setChecked(false);
        }
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotaCambioCab.isEmpty()) {
            Messagebox.show("No hay registros de Nota de Cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaCambioCab.getSize(); i++) {
                if (objlstNotaCambioCab.get(i).getNc_sit() != 2 && objlstNotaCambioCab.get(i).getNc_sit() != 4) {
                    objlstNotaCambioCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_notcambcab.setModel(objlstNotaCambioCab);
        }
    }

    @Listen("onSelect=#lst_notcambcab")
    public void seleccionaRegistro() throws SQLException {
        objNotaCambioCab = (NotCambCab) lst_notcambcab.getSelectedItem().getValue();
        if (objNotaCambioCab == null) {
            objNotaCambioCab = objlstNotaCambioCab.get(i_selCab + 1);
        }
        i_selCab = lst_notcambcab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        /*txt_usuadd.setValue(objNotaCambioCab.getNc_usuadd());
         d_fecadd.setValue(objNotaCambioCab.getNc_fecadd());
         txt_usumod.setValue(objNotaCambioCab.getNc_usumod());
         d_fecmod.setValue(objNotaCambioCab.getNc_fecmod());*/
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onSeleccion=#lst_notcambcab")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstNotaCambioCab.get(item.getIndex()).getNc_sit() == 2 || objlstNotaCambioCab.get(item.getIndex()).getNc_sit() == 4) {
            Messagebox.show("La nota de cambio ya no puede ser procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstNotaCambioCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_notcambcab.setModel(objlstNotaCambioCab);

        }
    }

    @Listen("onSelect=#lst_notcambdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaCambioDet = (NotCambDet) lst_notcambdet.getSelectedItem().getValue();
        if (objNotaCambioDet == null) {
            objNotaCambioDet = objlstNotaCambioDet.get(i_selDet + 1);
        }
        i_selDet = lst_notcambdet.getSelectedIndex();
        llenarCamposProducto();
    }

    @Listen("onClick=#tbbtn_btn_aprobar")
    public void autorizarNotacambio() {
        if (objlstNotaCambioCab == null || objlstNotaCambioCab.isEmpty()) {
            Messagebox.show("No hay registros para consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaCambioCab.getSize(); j++) {
                if (objlstNotaCambioCab.get(j).isValSelec()) {
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
    public void RechazarNotacambio() {
        if (objlstNotaCambioCab == null || objlstNotaCambioCab.isEmpty()) {
            Messagebox.show("No hay registros para consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaCambioCab.getSize(); j++) {
                if (objlstNotaCambioCab.get(j).isValSelec()) {
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

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaCambioCab == null || objlstNotaCambioCab.isEmpty()) {
            Messagebox.show("No hay Registros de Autorizar Notas de Cambio para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                String modoEjecucion = "NotaCambioAuto";
                objMapObjetos.put("txt_venid", txt_ven_id);
                objMapObjetos.put("txt_vennom", txt_ven_apenom);
                objMapObjetos.put("txt_cli_id", txt_cli_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambioAuto");
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
                Messagebox.show("Por favor ingrese datos numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_ven_id.setValue("");
            } else {
                int ven_id = Integer.parseInt(txt_ven_id.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El codigo de vendedor no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                String modoEjecucion = "NotaCambioAuto";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambioAuto");
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
                Messagebox.show("Por favor ingrese datos numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_cli_id.setValue("");
            } else {
                String cli_id = txt_cli_id.getValue();
                Cliente objCliente = new DaoCliente().getDireccionDefault(cli_id, emp_id, suc_id);
                if (objCliente == null) {
                    Messagebox.show("El codigo del cliente no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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

    @Listen("onOK=#cb_ncd_tipdoc")
    public void validaTipoDocumento() {
        if (cb_ncd_tipdoc.getSelectedIndex() == -1) {
            Messagebox.show("Seleccione 'Tipo de documento'", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                cb_ncd_tipdoc.focus();
                            }
                        }
                    });
        } else {
            txt_ncd_serie.focus();
        }
    }

    //Eventos Otros 
    public void relizarAutorizacion(String acc1, final String acc2, final int sit) {
        s_mensaje = "Esta seguro que desea " + acc1 + " las notas de cambio seleccionadas?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            String lstNotInter = "Informacion de " + acc2 + " de N.C. \n\n ";
                            ParametrosSalida objParamSalida;
                            for (int i = 0; i < objlstNotaCambioCab.getSize(); i++) {
                                if (objlstNotaCambioCab.get(i).isValSelec() && objlstNotaCambioCab.get(i).getNc_sit() == 1) {
                                    objlstNotaCambioCab.get(i).setNc_autusuadd(objUsuCredential.getCuenta());
                                    objlstNotaCambioCab.get(i).setNc_autpcadd(objUsuCredential.getComputerName());
                                    objlstNotaCambioCab.get(i).setNc_sit(sit);
                                    objParamSalida = objDaoNotaCambio.autorizarNotaCambio(objlstNotaCambioCab.get(i));
                                    lstNotInter += "N.I: " + objlstNotaCambioCab.get(i).getNc_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                }
                            }
                            busquedaRegistros();
                            Messagebox.show(lstNotInter, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    }
                });
    }

    public void llenarCampos() {
        txt_nc_id.setValue(objNotaCambioCab.getNc_id());
        d_nc_fecemi.setValue(objNotaCambioCab.getNc_fecemi());
        d_nc_fecent.setValue(objNotaCambioCab.getNc_fecent());
        txt_nc_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_motcam()), 3, "0"));
        txt_nc_motcamdes.setValue(objNotaCambioCab.getMcam_des());
        txt_cli_iddet.setValue(objNotaCambioCab.getCli_id());
        txt_cli_razsocdet.setValue(objNotaCambioCab.getCli_razsoc());
        txt_nc_notaent.setValue(objNotaCambioCab.getNc_notaent());
        txt_nc_notasal.setValue(objNotaCambioCab.getNc_notasal());
        txt_nc_depurado.setValue(objNotaCambioCab.getNc_nrodep());
        txt_zon_id.setValue(objNotaCambioCab.getNc_zona());
        txt_zon_des.setValue(objNotaCambioCab.getZon_des());
        txt_nc_venid.setValue(objNotaCambioCab.getVen_id());
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_sup()), 4, "0"));
        txt_nc_vennom.setValue(objNotaCambioCab.getVen_nom());
        txt_nc_transid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_trans()), 4, "0"));
        txt_nc_transdes.setValue(objNotaCambioCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaCambioCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaCambioCab.getClidir_direc());
        txt_nc_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_hor()), 3, "0"));
        txt_nc_hordes.setValue(objNotaCambioCab.getHor_des());
        txt_nc_registro.setValue(objNotaCambioCab.getNc_nroreg());
        txt_usuadd.setValue(objNotaCambioCab.getNc_usuadd());
        d_fecadd.setValue(objNotaCambioCab.getNc_fecadd());
        txt_usumod.setValue(objNotaCambioCab.getNc_usumod());
        d_fecmod.setValue(objNotaCambioCab.getNc_fecmod());
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaCambioDet = null;
        long nc_key = objNotaCambioCab.getNc_key();
        objlstNotaCambioDet = objDaoNotaCambio.listaNotaCambioDet(emp_id, suc_id, nc_key);
        lst_notcambdet.setModel(objlstNotaCambioDet);
    }

    public void llenarCamposProducto() throws SQLException {
        txt_ncd_prodid.setValue(objNotaCambioDet.getPro_id());
        txt_ncd_proddes.setValue(objNotaCambioDet.getPro_des());

        txt_ncd_entero.setValue(objNotaCambioDet.getNcd_cantent());
        txt_ncd_upre.setValue(objNotaCambioDet.getPro_presmin());
        txt_ncd_fraccion.setValue(objNotaCambioDet.getNcd_cantfrac());
        txt_ncd_glosanotcam.setValue(objNotaCambioDet.getNcd_glosa());

        txt_ncd_doc.setValue(objNotaCambioDet.getNcd_doc() == null ? "" : objNotaCambioDet.getNcd_doc());
        txt_ncd_serie.setValue(objNotaCambioDet.getNcd_serie() == null ? "" : objNotaCambioDet.getNcd_serie());

        String tipo_doc = String.valueOf(objNotaCambioDet.getNcd_tipdoc());
        if (!tipo_doc.isEmpty() || tipo_doc != null) {
            cb_ncd_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_ncd_tipdoc, tipo_doc));
        } else {
            cb_ncd_tipdoc.setSelectedIndex(-1);
        }

        objParametrosSalida = new ParametrosSalida();
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nc_fecemi.getValue()), 101, objNotaCambioDet.getPro_id(), "%%");
        //int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
        if ((objParametrosSalida.getCantStocks() / objNotaCambioDet.getPro_presmin()) > 0) {
            //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
            txt_ncd_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() / objNotaCambioDet.getPro_presmin()));
        } else {
            txt_ncd_stock.setValue("0");
        }
    }

    public void limpiarCampos() {
        txt_nc_id.setValue("");
        d_nc_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_nc_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_nc_motcamid.setValue("");
        txt_nc_motcamdes.setValue("");
        txt_cli_iddet.setValue("");
        txt_cli_razsocdet.setValue("");
        txt_nc_notaent.setValue("");
        txt_nc_notasal.setValue("");
        txt_nc_depurado.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nc_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nc_vennom.setValue("");
        txt_nc_transid.setValue("");
        txt_nc_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_nc_horid.setValue("");
        txt_nc_hordes.setValue("");
        txt_nc_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_ncd_doc.setValue("");
        txt_ncd_serie.setValue("");
        txt_ncd_proddes.setValue("");
        txt_ncd_prodid.setValue("");
        cb_ncd_tipdoc.setSelectedIndex(-1);
        txt_ncd_upre.setValue(null);
        txt_ncd_entero.setValue(null);
        txt_ncd_fraccion.setValue(null);
        txt_ncd_glosanotcam.setValue("");
        txt_ncd_stock.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarTabLista(int i) {
        if (i == 1) {
            objlstNotaCambioCab = null;
            objlstNotaCambioCab = new ListModelList<NotCambCab>();
            lst_notcambcab.setModel(objlstNotaCambioCab);
        } else {
            objlstNotaCambioDet = null;
            objlstNotaCambioDet = new ListModelList<NotCambDet>();
            lst_notcambdet.setModel(objlstNotaCambioDet);
        }
    }
}
