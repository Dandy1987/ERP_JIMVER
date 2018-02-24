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
import org.me.gj.model.logistica.procesos.NotaRecojoCab;
import org.me.gj.model.logistica.procesos.NotaRecojoDet;
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

public class ControllerAdmNotaRecojo extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_aprobar, tbbtn_btn_rechazar, tbbtn_btn_imprimir,
            tbbtn_btn_nuevonotrec, tbbtn_btn_editarnotrec, tbbtn_btn_eliminarnotrec, tbbtn_btn_guardarnotrec, tbbtn_btn_deshacernotrec;//Botones Nota de Recojo Detalle;
    @Wire
    Listbox lst_notreccab, lst_notrecdet;
    @Wire
    Datebox d_fecini, d_fecfin,
            d_fecadd, d_fecmod,//------>Datos de la Nota Cambio Cabecera 
            d_nr_fecemi, d_nr_fecent;//Datos de la Nota Recojo Cabecera
    @Wire
    Combobox cb_situacion, cb_nrd_tipdoc;
    @Wire
    Textbox txt_ven_id, txt_ven_apenom, txt_cli_razsoc, txt_cli_id,
            txt_usuadd, txt_usumod,//------>Datos de la Nota Cambio Cabecera
            txt_venid, txt_vennom,//Datos para la busqueda
            txt_nr_id, txt_nr_motcamid, txt_nr_motcamdes, txt_cli_iddet, txt_cli_razsocdet, txt_nr_notaent, txt_zon_id,
            txt_zon_des, txt_clidir_direcc, txt_nr_horid, txt_nr_hordes, txt_nr_registro,
            txt_sup_id, txt_nr_venid, txt_nr_vennom, txt_nr_transid, txt_nr_transdes,//Datos de la Nota Recojo Cabecera
            txt_nrd_prodid, txt_nrd_proddes, txt_nrd_doc, txt_nrd_serie, txt_nrd_glosanotrec;//Datos de la Nota Recojo Detalle
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Intbox //Datos de la Nota Recojo Cabecera
            txt_nrd_entero, txt_nrd_fraccion;//Datos de la Nota Recojo Detalle
    @Wire
    Checkbox chk_selecAll;
    //Instancias de Objetos
    ListModelList<NotaRecojoCab> objlstNotaRecojoCab;
    ListModelList<NotaRecojoDet> objlstNotaRecojoDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    NotaRecojoCab objNotaRecojoCab;
    NotaRecojoDet objNotaRecojoDet;
    DaoNotaRecojo objDaoNotaRecojo;
    DaoAccesos objDaoAccesos;
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
    private static final Logger LOGGER = Logger.getLogger(ControllerAdmNotaRecojo.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objlstNotaRecojoCab = null;
        objDaoNotaRecojo = new DaoNotaRecojo();
        objlstNotaRecojoCab = objDaoNotaRecojo.listaNotRecCabApro(emp_id, suc_id, "%%", "%%", "1", "3", "4", fechaActual, fechaActual);
        lst_notreccab.setModel(objlstNotaRecojoCab);

        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_nrd_tipdoc.setModel(objlstTipDoc);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10208020, usuario, empresa, sucursal);
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

    @Listen("onClick=#btn_buscarrec")
    public void busquedaRegistros() throws SQLException {
        limpiarTabLista(1);
        limpiarTabLista(2);
        limpiarCamposDetalle();
        limpiarCampos();
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
        String situacion = cb_situacion.getSelectedIndex() == -1 ? "134" : cb_situacion.getSelectedItem().getValue().toString();
        String sitIng = situacion.substring(0, 1);
        String sitApro = situacion.substring(1, 2);
        String sitRec = situacion.substring(2, 3);
        if (resultado.equals("F1>")) {
            Messagebox.show("La Fecha Inicial no puede ser mayor a la Final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstNotaRecojoCab = objDaoNotaRecojo.listaNotRecCabApro(emp_id, suc_id, cli_id, ven_id, sitIng, sitApro, sitRec, f_emisioni, f_emisionf);
            //Validar tabla sin registro
            if (objlstNotaRecojoCab.getSize() > 0) {
                lst_notreccab.setModel(objlstNotaRecojoCab);
            } else {
                limpiarTabLista(1);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            limpiarTabLista(2);
            limpiaAuditoria();
            chk_selecAll.setChecked(false);
        }
    }

    @Listen("onSelect=#lst_notreccab")
    public void seleccionaRegistro() throws SQLException {
        objNotaRecojoCab = (NotaRecojoCab) lst_notreccab.getSelectedItem().getValue();
        if (objNotaRecojoCab == null) {
            objNotaRecojoCab = objlstNotaRecojoCab.get(i_selCab + 1);
        }
        i_selCab = lst_notreccab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotaRecojoCab.isEmpty()) {
            Messagebox.show("No hay Registros de Nota de Recojo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaRecojoCab.getSize(); i++) {
                if (objlstNotaRecojoCab.get(i).getNr_sit() != 3 && objlstNotaRecojoCab.get(i).getNr_sit() != 4) {
                    objlstNotaRecojoCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_notreccab.setModel(objlstNotaRecojoCab);
        }
    }

    @Listen("onSeleccion=#lst_notreccab")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstNotaRecojoCab.get(item.getIndex()).getNr_sit() == 3 || objlstNotaRecojoCab.get(item.getIndex()).getNr_sit() == 4) {
            Messagebox.show("La Nota de Recojo ya no puede ser Procesada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstNotaRecojoCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_notreccab.setModel(objlstNotaRecojoCab);

        }
    }

    @Listen("onSelect=#lst_notrecdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaRecojoDet = (NotaRecojoDet) lst_notrecdet.getSelectedItem().getValue();
        if (objNotaRecojoDet == null) {
            objNotaRecojoDet = objlstNotaRecojoDet.get(i_selDet + 1);
        }
        i_selDet = lst_notrecdet.getSelectedIndex();
        llenarCamposProducto();
    }

    @Listen("onClick=#tbbtn_btn_aprobar")
    public void autorizarNotaRecojo() {
        if (objlstNotaRecojoCab == null || objlstNotaRecojoCab.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaRecojoCab.getSize(); j++) {
                if (objlstNotaRecojoCab.get(j).isValSelec()) {
                    i = i + 1;
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                relizarAutorizacion("En Transito", "En Transito", 3);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_rechazar")
    public void RechazarNotaRecojo() {
        if (objlstNotaRecojoCab == null || objlstNotaRecojoCab.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int i = 0;
            for (int j = 0; j < objlstNotaRecojoCab.getSize(); j++) {
                if (objlstNotaRecojoCab.get(j).isValSelec()) {
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
        if (objlstNotaRecojoCab == null || objlstNotaRecojoCab.isEmpty()) {
            Messagebox.show("No hay Registros de Autorizar Notas de Recojo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                String modoEjecucion = "NotaRecojoAuto";
                objMapObjetos.put("txt_venid", txt_ven_id);
                objMapObjetos.put("txt_vennom", txt_ven_apenom);
                objMapObjetos.put("txt_cli_id", txt_cli_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojoAuto");
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
                String modoEjecucion = "NotaRecojoAuto";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojoAuto");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                d_fecini.focus();
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

    //Eventos Otros
    public void relizarAutorizacion(String acc1, final String acc2, final int sit) {
        s_mensaje = "Esta Seguro que desea " + acc1 + " las Notas de Recojo Seleccionadas?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            String lstNotInter = "Informacion de " + acc2 + " de N.R. \n\n ";
                            ParametrosSalida objParamSalida;
                            for (int i = 0; i < objlstNotaRecojoCab.getSize(); i++) {
                                if (objlstNotaRecojoCab.get(i).isValSelec() && objlstNotaRecojoCab.get(i).getNr_sit() == 1) {
                                    objlstNotaRecojoCab.get(i).setNr_autusuadd(objUsuCredential.getCuenta());
                                    objlstNotaRecojoCab.get(i).setNr_autpcadd(objUsuCredential.getComputerName());
                                    objlstNotaRecojoCab.get(i).setNr_sit(sit);
                                    objParamSalida = objDaoNotaRecojo.autorizarNotaRecojo(objlstNotaRecojoCab.get(i));
                                    lstNotInter += "N.I: " + objlstNotaRecojoCab.get(i).getNr_id() + ": " + objParamSalida.getMsgValidacion() + "\n";
                                }
                            }
                            busquedaRegistros();
                            Messagebox.show(lstNotInter, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    }
                });
    }

    public void limpiarCampos() {
        txt_nr_id.setValue("");
        d_nr_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_nr_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_nr_motcamid.setValue("");
        txt_nr_motcamdes.setValue("");
        txt_cli_iddet.setValue("");
        txt_cli_razsocdet.setValue("");
        txt_nr_notaent.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nr_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nr_vennom.setValue("");
        txt_nr_transid.setValue("");
        txt_nr_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_nr_horid.setValue("");
        txt_nr_hordes.setValue("");
        txt_nr_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_nrd_doc.setValue("");
        txt_nrd_proddes.setValue("");
        txt_nrd_prodid.setValue("");
        txt_nrd_serie.setValue("");
        cb_nrd_tipdoc.setSelectedIndex(-1);
        txt_nrd_entero.setValue(null);
        txt_nrd_fraccion.setValue(null);
        txt_nrd_glosanotrec.setValue("");
    }

    public void llenarCampos() {
        txt_nr_id.setValue(objNotaRecojoCab.getNr_id());
        d_nr_fecemi.setValue(objNotaRecojoCab.getNr_fecemi());
        d_nr_fecent.setValue(objNotaRecojoCab.getNr_fecent());
        txt_nr_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_motcam()), 3, "0"));
        txt_nr_motcamdes.setValue(objNotaRecojoCab.getMcam_des());
        txt_cli_iddet.setValue(objNotaRecojoCab.getCli_id());
        txt_cli_razsocdet.setValue(objNotaRecojoCab.getCli_razsoc());
        txt_nr_notaent.setValue(objNotaRecojoCab.getNr_notaent());
        txt_zon_id.setValue(objNotaRecojoCab.getNr_zona());
        txt_zon_des.setValue(objNotaRecojoCab.getZon_des());
        txt_nr_venid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_vend()), 4, "0"));
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_sup()), 4, "0"));
        txt_nr_vennom.setValue(objNotaRecojoCab.getVen_nom());
        txt_nr_transid.setValue(objNotaRecojoCab.getTrans_id());
        txt_nr_transdes.setValue(objNotaRecojoCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaRecojoCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaRecojoCab.getClidir_direcc());
        txt_nr_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_hor()), 3, "0"));
        txt_nr_hordes.setValue(objNotaRecojoCab.getHor_des());
        txt_nr_registro.setValue(objNotaRecojoCab.getNr_nroreg());
        txt_usuadd.setValue(objNotaRecojoCab.getNr_usuadd());
        d_fecadd.setValue(objNotaRecojoCab.getNr_fecadd());
        txt_usumod.setValue(objNotaRecojoCab.getNr_usumod());
        d_fecmod.setValue(objNotaRecojoCab.getNr_fecmod());
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenarCamposProducto() {
        txt_nrd_prodid.setValue(objNotaRecojoDet.getPro_id());
        txt_nrd_proddes.setValue(objNotaRecojoDet.getPro_des());
        txt_nrd_doc.setValue(objNotaRecojoDet.getNrd_doc() == null ? "" : objNotaRecojoDet.getNrd_doc());
        txt_nrd_serie.setValue(objNotaRecojoDet.getNrd_serie() == null ? "" : objNotaRecojoDet.getNrd_serie());
        txt_nrd_entero.setValue(objNotaRecojoDet.getNrd_cantent());
        txt_nrd_fraccion.setValue(objNotaRecojoDet.getNrd_cantfrac());
        txt_nrd_glosanotrec.setValue(objNotaRecojoDet.getNrd_glosa());

        String tipo_doc = String.valueOf(objNotaRecojoDet.getNrd_tipdoc());
        if (!tipo_doc.isEmpty() || tipo_doc != null) {
            cb_nrd_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nrd_tipdoc, tipo_doc));
        } else {
            cb_nrd_tipdoc.setSelectedIndex(-1);
        }
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaRecojoDet = null;
        long nr_key = objNotaRecojoCab.getNr_key();
        objlstNotaRecojoDet = objDaoNotaRecojo.listaNotaRecojoDet(emp_id, suc_id, nr_key);
        lst_notrecdet.setModel(objlstNotaRecojoDet);
    }

    public void limpiarTabLista(int i) {
        if (i == 1) {
            objlstNotaRecojoCab = null;
            objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
            lst_notreccab.setModel(objlstNotaRecojoCab);
        } else {
            objlstNotaRecojoDet = null;
            objlstNotaRecojoDet = new ListModelList<NotaRecojoDet>();
            lst_notrecdet.setModel(objlstNotaRecojoDet);
        }
    }

    public String verificarFechas() {
        String s_valor = "";
        String fecha_ini = sdf.format(d_fecini.getValue());
        if (d_fecfin.getValue().getTime() < d_fecini.getValue().getTime()) {
            s_valor = "La fecha de Final debe ser Mayor o igual que : " + fecha_ini;
        }
        return s_valor;
    }
}
