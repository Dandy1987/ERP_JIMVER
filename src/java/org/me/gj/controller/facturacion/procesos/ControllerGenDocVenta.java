package org.me.gj.controller.facturacion.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoNumeFac;
import org.me.gj.controller.facturacion.mantenimiento.DaoSupervisores;
import org.me.gj.controller.facturacion.mantenimiento.DaoTipoVenta;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.facturacion.mantenimiento.NumeFac;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.facturacion.procesos.NotesVentaCab;
import org.me.gj.model.facturacion.procesos.NotesVentaDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.DaoUtil;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerGenDocVenta extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_mantenimiento, tab_lista;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Button btn_consultar,btn_generar;
    @Wire
    Listbox lst_procnotesvencab, lst_procnotesvendet;
    @Wire
    Combobox cb_busq_periodo, cb_bus_tipventa, cb_proc_docventa, cb_proc_serie, cb_pronpag, cb_simbmon, cb_tipoventa;
    @Wire
    Textbox txt_nronotes, txt_situacion, txt_dni, txt_ruc,
            txt_proid, txt_cli_id, txt_cli_razsoc, txt_lincred, txt_limdoc, txt_saldo,
            txt_usuadd, txt_usumod, txt_saldocred, txt_saldodoc, txt_diaplazo, txt_id_condven, txt_giro, txt_estado,
            txt_formapago, txt_des_condven, txt_zon_id, txt_nomvendedor, txt_zon_des, txt_idvendedor,
            txt_clidir_direcc, txt_diavis, txt_id_listapre, txt_des_listapre, txt_listaprecio,
            txt_prodes, txt_det_idlispre, txt_det_deslispre, txt_notessal,
            txt_cantentped, txt_cantfracped, txt_cantentnotes, txt_cantfracnotes,
            txt_busq_idvendedor, txt_busq_nomvendedor, txt_busq_pedventa, txt_busq_idsupervisor, txt_busq_nomsupervisor;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Intbox txt_pedvendet_key, txt_cant, txt_frac;
    @Wire
    Doublebox db_tipcam, db_det_preven, db_det_valvta, db_det_pordes, db_det_valdes, txt_total, db_det_porigv, db_det_valigv, db_det_total;
    @Wire
    Datebox d_busq_fecemi, d_fechadespacho, d_fechadocventa, d_fecadd, d_fecmod, d_fecemi;
    @Wire
    Checkbox chk_modtip, chk_desman, chk_selecAll;
    @Wire
    Radiogroup rbg_orden;

    //Instancias de Objetos
    ListModelList<ManPer> objlstManPeriodos;
    ListModelList<TipDoc> objlstTipDoc;
    ListModelList<NumeFac> objlstNumeFac;
    ListModelList<NotesVentaCab> objlstNotesVentaCab, objlstCabAux;
    ListModelList<NotesVentaDet> objlstNotesVentaDet;
    ListModelList<Moneda> objlstMonedas;
	ListModelList<TipoVenta> objlstTipoventa;
    Accesos objAccesos;
    Vendedor objVendedor;
    Supervisor objSupervisor;
    NotesVentaCab objNotesVentaCab;
    NotesVentaDet objNotesVentaDet;

    DaoAccesos objDaoAccesos;
    DaoManPer objDaoManPer;
    DaoSupervisores objDaoSupervisores;
    DaoVendedores objDaoVendedores;
    DaoTipDoc objDaoTipDoc;
    DaoNumeFac objDaoNumeFac;
    DaoPedVen objDaoPedVen;
    DaoNotesVenta objDaoNotesVenta;
    DaoMoneda objDaoMoneda;
    DaoCierreDia objDaoCierreDia;
    DaoUtil objDaoUtil;
    DaoTipoVenta objDaoTipoVenta;

    //Variables publicas
    String campo = "";
    int emp_id, suc_id;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    Date fecha = new Date();
    public static boolean bandera = false;
    int i_tipodoc, i_bolfac;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenDocVenta.class);
  
   @Override
    public void doAfterCompose(Component window) throws Exception{
     super.doAfterCompose(window);
     chk_selecAll.setChecked(false);
    }
    
    // Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_gendocventa")
    public void llenaRegistros() throws SQLException {

        String periodo = sdfm.format(fecha);
        Utilitarios.sumaFecha(d_fechadespacho, fecha, 1);
        cb_proc_serie.setDisabled(true);

        objlstNumeFac = new ListModelList<NumeFac>();
        objlstNotesVentaCab = new ListModelList<NotesVentaCab>();
        objlstManPeriodos = new ListModelList<ManPer>();
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstMonedas = new ListModelList<Moneda>();
        objlstCabAux = new ListModelList<NotesVentaCab>();
	objlstTipoventa = new ListModelList<TipoVenta>();
        objDaoPedVen = new DaoPedVen();
        objDaoNotesVenta = new DaoNotesVenta();
        objDaoNumeFac = new DaoNumeFac();
        objDaoMoneda = new DaoMoneda();
        objDaoSupervisores = new DaoSupervisores();
        objDaoTipDoc = new DaoTipDoc();
        objDaoVendedores = new DaoVendedores();
        objDaoManPer = new DaoManPer();
        objDaoCierreDia = new DaoCierreDia();
        objDaoUtil = new DaoUtil();
	objDaoTipoVenta = new DaoTipoVenta();
        objVendedor = new Vendedor();
        objSupervisor = new Supervisor();

        //carga periodo
        objlstManPeriodos = objDaoManPer.listaPeriodos(0);
        cb_busq_periodo.setModel(objlstManPeriodos);
        cb_busq_periodo.setValue(periodo);
        //carga tipo documento
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_proc_docventa.setModel(objlstTipDoc);
        //carga monedas
        objlstMonedas = objDaoMoneda.listaMonedas(1);
        cb_simbmon.setModel(objlstMonedas);
		//carga tipo de venta
        objlstTipoventa = objDaoTipoVenta.listaTipoVentas(1);
        objlstTipoventa.add(new TipoVenta(100, ""));
        cb_bus_tipventa.setModel(objlstTipoventa);
        txt_busq_idsupervisor.setDisabled(false);
        txt_busq_idvendedor.setDisabled(false);
        txt_busq_pedventa.setDisabled(false);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        cb_proc_docventa.setSelectedIndex(-1);
        
        habilitaGenera(true);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40204000, usuario, empresa, sucursal);
        /*if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Generar Doc.Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Generar Doc.Venta con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creacion de un nuevo Generar Doc.Venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creacion de un nuevo Generar Doc.Venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edicion de un Generar Doc.Venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edicion de un Generar Doc.Venta");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminacion de un Generar Doc.Venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminacion de un Generar Doc.Venta");
        }*/
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Generar Doc.Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Generar Doc.Venta");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
    	cb_proc_docventa.setSelectedIndex(-1);
    	limpiarlistaCombo();
    	busquedaGeneracion(0);    	
    }
    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotesVentaCab.isEmpty()) {
            Messagebox.show("No existe registro de notas salidas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotesVentaCab.getSize(); i++) {
                if (objlstNotesVentaCab.get(i).getNotes_sit() == 1) {
                    objlstNotesVentaCab.get(i).setValSelec(chk_selecAll.isChecked());
                }
            }
            lst_procnotesvencab.setModel(objlstNotesVentaCab);
        }
    }

    @Listen("onSeleccion=#lst_procnotesvencab")
    public void seleccionaRegistro(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        if (objlstNotesVentaCab.get(item.getIndex()).getNotes_sit() == 2) {
            Messagebox.show("La nota de salida ya no puede volver a ser generada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_Reg.setChecked(false);
        } else {
            objlstNotesVentaCab.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_procnotesvencab.setModel(objlstNotesVentaCab);
        }
    }

    @Listen("onSelect=#lst_procnotesvencab")
    public void seleccionaRegistro() throws SQLException {
        objNotesVentaCab = (NotesVentaCab) lst_procnotesvencab.getSelectedItem().getValue();
        limpiarDetalle();
        llenarCampos(objNotesVentaCab);
        llenarCamposDetalle(objNotesVentaCab);
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el Pedido ").append(objNotesVentaCab.getPcab_nroped()).toString());
    }

    @Listen("onSelect=#lst_procnotesvendet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotesVentaDet = lst_procnotesvendet.getSelectedItem().getValue();
        llenarCamposProducto(objNotesVentaDet);
        llenarCamposTotales();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | selecciono el registro con el Producto ").append(objNotesVentaDet.getPro_id()).toString());
    }

    @Listen("onSelect=#cb_proc_docventa")
    public void seleccionaTipoDocumento() throws SQLException {
        limpiarlistaCombo();
        if (cb_proc_docventa.getSelectedItem().getValue() != null) {
            i_tipodoc = cb_proc_docventa.getSelectedItem().getValue();            
            objlstNumeFac = objDaoNumeFac.listaSeriexNumeFac(i_tipodoc);
            if (!objlstNumeFac.isEmpty()) {            	
                cb_proc_serie.setDisabled(false);  
                busquedaGeneracion(i_tipodoc);
                cb_proc_serie.setModel(objlstNumeFac);
            } else {
                cb_proc_serie.setDisabled(true);
            }
        }
    }

    @Listen("onClick=#btn_generar")
    public void botonGenerar() throws SQLException, ParseException {
        String verificar = verificar();
        String verificarFechas = verificarFechas();

        if (objlstNotesVentaCab.getSize() < 1 || objlstNotesVentaCab.isEmpty() || objlstNotesVentaCab == null) {
            Messagebox.show("No hay notas de salidas para generar documento de venta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (!verificar.isEmpty()) {
            Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("docvta")) {
                            cb_proc_docventa.select();
                            cb_proc_docventa.focus();
                        } else if (campo.equals("serie")) {
                            cb_proc_serie.select();
                            cb_proc_serie.focus();
                        } else if (campo.equals("fdocventa")) {
                            d_fechadocventa.focus();
                        } else if (campo.equals("fdespacho")) {
                            d_fechadespacho.focus();
                        }
                    }
                }
            });

        } else if (!verificarFechas.isEmpty()) {
            Messagebox.show(verificarFechas, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String resultado = "";
            
            String fecemi = sdf.format(d_fecemi.getValue());
            if (objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                Messagebox.show("El dia se encuentra cerrado - Modulo Logistica", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (objDaoCierreDia.ValidaDia(fecemi, "F").getCiedia_fac() == 0) {
                Messagebox.show("El dia se encuentra cerrado - Modulo Facturacion", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (objDaoCierreDia.ValidaDia(fecemi, "X").getCiedia_cxc() == 0) {
                Messagebox.show("El dia se encuentra cerrado - Modulo Cxc", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Date fecha = d_fechadocventa.getValue();
                ParametrosSalida objParam;

                objParam = objDaoUtil.ValidaProcesos(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
                if (objParam.getFlagIndicador() == 0) {
                    if (objParam.getMsgValidacion().equals("N")) {
                        int x = 0;                        
                        for (int j = 0; j < objlstNotesVentaCab.getSize(); j++) {
                            if (objlstNotesVentaCab.get(j).isValSelec()) {
                                x = x + 1;
                                resultado = Utilitarios.compareFechas(objlstNotesVentaCab.get(j).getPcab_fecemi(), fecha);
                                break;
                            }
                        }
                        if (x <= 0) {
                            Messagebox.show("Debe seleccionar una nota de salidas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        } else if (resultado.equals("F1>")) {
                            Messagebox.show("La fecha emision no puede ser mayor a la fecha de generacion", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }  else {
                        	String validaClienteFacturaBoleta = validaClienteFacturaBoleta();
	                        if (!validaClienteFacturaBoleta.isEmpty()) {
	                            Messagebox.show(validaClienteFacturaBoleta, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
		                     } else {
	                            Messagebox.show("Esta seguro que desea generar documentos con las notas de salidas seleccionadas?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
	                                    Messagebox.QUESTION, new EventListener() {
	                                        @Override
	                                        public void onEvent(Event event) throws Exception {
	                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
	                                                objlstCabAux = new ListModelList<NotesVentaCab>();
	                                                for (int i = 0; i < objlstNotesVentaCab.size(); i++) {
	                                                    if (objlstNotesVentaCab.get(i).getNotes_sit() == 1 && objlstNotesVentaCab.get(i).isValSelec()) {
	                                                        objlstCabAux.add(objlstNotesVentaCab.get(i));
	                                                    }
	                                                }
	
	                                                int docventa = cb_proc_docventa.getSelectedItem().getValue();
	                                                int serie = cb_proc_serie.getSelectedItem().getValue();
	                                                Date fdocemi = d_fechadocventa.getValue();
	                                                Date fdepach = d_fechadespacho.getValue();
	                                                String usuario = objUsuCredential.getCuenta();	                                                
	                                                String pcred = objUsuCredential.getComputerName();	                                               
	
	                                                ParametrosSalida objParam2 = objDaoNotesVenta.generarDocumentoVenta(emp_id, suc_id, getNotesVentaCab(objlstCabAux), docventa, serie, fdocemi, fdepach, pcred ,usuario);
	                                                if (objParam2.getFlagIndicador() == 0) {
	                                                    limpiarlista();
	                                                   // busquedaRegistros(); se comento para ver error
	                                                }
	
	                                                Messagebox.show(objParam2.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
	
	                                            }
	                                        }
	                                    });
	                            
	                        	}
                        }
                    } else {
                        Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }

        }

    }

    // Eventos Secundarios - Validacion
    @Listen("onOK=#txt_busq_idvendedor")
    public void lov_vendedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_busq_idvendedor.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "GenDocVenta";
                Textbox txt_supid = new Textbox();
                objMapObjetos.put("txt_venid", txt_busq_idvendedor);
                objMapObjetos.put("txt_vennom", txt_busq_nomvendedor);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenDocVenta");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_busq_idvendedor")
    public void validaVendedor() throws SQLException {
        txt_busq_nomvendedor.setValue("");
        if (!txt_busq_idvendedor.getValue().isEmpty()) {
            if (!txt_busq_idvendedor.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarVendedor();
                            txt_busq_idvendedor.focus();
                        }
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_busq_idvendedor.getValue());
                objVendedor = objDaoVendedores.getNomVendedor(ven_id);
                if (objVendedor == null) {
                    Messagebox.show("El codigo de vendedor no existe o esta eliminado ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarVendedor();
                                txt_busq_idvendedor.focus();
                            }
                        }
                    });

                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedor.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_busq_idvendedor.setValue(objVendedor.getVen_id());
                    txt_busq_nomvendedor.setValue(objVendedor.getS_vennomcompleto());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_busq_idsupervisor")
    public void lovSupervisor() {
        if (bandera == false) {
            bandera = true;
            if (txt_busq_idsupervisor.getValue().isEmpty()) {
                String modoEjecucion = "GenDocVenta";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_supid", txt_busq_idsupervisor);
                objMapObjetos.put("txt_apenom", txt_busq_nomsupervisor);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerGenDocVenta");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSupervisores.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_busq_idsupervisor")
    public void validaSupervisor() throws SQLException {
        if (!txt_busq_idsupervisor.getValue().isEmpty()) {
            if (!txt_busq_idsupervisor.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarSupervisor();
                            txt_busq_idsupervisor.focus();
                        }
                    }
                });
            } else {
                Supervisor objSup;
                int supid = Integer.parseInt(txt_busq_idsupervisor.getValue());
                objSup = new DaoSupervisores().getNomSupervisor(supid);
                if (objSup == null) {
                    Messagebox.show("El codigo de supervisor no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarSupervisor();
                                txt_busq_idsupervisor.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Supervisor con codigo " + supid + " y ha encontrado 1 registro(s)");
                    txt_busq_idsupervisor.setValue(Utilitarios.lpad(String.valueOf(objSup.getSup_key()), 4, "0"));
                    txt_busq_nomsupervisor.setValue(objSup.getSup_apenom());
                }
            }
        } else {
            txt_busq_nomsupervisor.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_busq_pedventa")
    public void onOKPedido() throws SQLException {
        btn_consultar.focus();
    }

    @Listen("onBlur=#txt_busq_pedventa")
    public void onBlurPedido() throws SQLException {
        if (!txt_busq_pedventa.getValue().isEmpty()) {
            if (!txt_busq_pedventa.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_busq_pedventa.setValue("");
                        txt_busq_pedventa.focus();
                    }
                });
            } else {
                if (Long.parseLong(txt_busq_pedventa.getValue()) < 1000000000) {
                    String cod = Utilitarios.lpad(txt_busq_pedventa.getValue(), 10, "0");
                    txt_busq_pedventa.setValue(cod);
                } else {
                    Messagebox.show("El codigo ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_busq_pedventa.setValue("");
                            txt_busq_pedventa.focus();
                        }
                    });
                }
            }
        }
    }

    // Eventos Otros
    public void busquedaGeneracion(int tipodoc) throws SQLException{
    	limpiarlista();
        Date fecha_emisioni = d_busq_fecemi.getValue();
        String fechai, fechap, ordenVenta;
        fechai = fecha_emisioni == null ? "" : sdf.format(d_busq_fecemi.getValue());
        fechap = fecha_emisioni == null ? "" : sdfm.format(d_busq_fecemi.getValue());

        ordenVenta = rbg_orden.getItems().get(0).isChecked() ? "notes_nro"
                : rbg_orden.getItems().get(1).isChecked() ? "zon_id" : "ven_id";

        if (cb_busq_periodo.getValue().equals(fechap) || fechai.equals("")) {
            String supervisor = txt_busq_idsupervisor.getValue();
            String vendedor = txt_busq_idvendedor.getValue();
            String situacion = "1";
            int estado = 1;
            String periodo = cb_busq_periodo.getValue();
            String pedido = txt_busq_pedventa.getValue();
            String  tipventa   = "100".equals(cb_bus_tipventa.getSelectedItem().getValue().toString()) ? "%%" :
            					 Utilitarios.lpad(cb_bus_tipventa.getSelectedItem().getValue().toString(), 3, "0");

            objlstNotesVentaCab = objDaoNotesVenta.busquedaNotaSalidaVenta(emp_id, suc_id, fechai, supervisor, vendedor, pedido, situacion, estado, periodo, ordenVenta , tipventa, tipodoc);

            if (!objlstNotesVentaCab.isEmpty()) {
            	habilitaGenera(false);
                lst_procnotesvencab.setModel(objlstNotesVentaCab);               
            } else { 
            	habilitaGenera(true);  
            	cb_proc_docventa.setSelectedIndex(-1);
            	limpiarlistaCombo();
            	Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        } else {
        	cb_proc_docventa.setSelectedIndex(-1);
        	limpiarlistaCombo();
            Messagebox.show("La fecha no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            habilitaGenera(true);  
        }
    }
    
    
    public void llenarCampos(NotesVentaCab objNotesVentaCab) {
        txt_nronotes.setValue(objNotesVentaCab.getNotes_nro());
        cb_tipoventa.setValue(objNotesVentaCab.getTip_ventades());
        txt_situacion.setValue(objNotesVentaCab.getPcab_situacion_des());
        d_fecemi.setValue(objNotesVentaCab.getPcab_fecemi());
        cb_simbmon.setSelectedIndex(objNotesVentaCab.getPcab_mon().equals("001") ? 0 : 1);
        db_tipcam.setValue(objNotesVentaCab.getPcab_tipcam());
        txt_cli_id.setValue(objNotesVentaCab.getCli_id());
        txt_cli_razsoc.setValue(objNotesVentaCab.getCli_des());
        txt_dni.setValue(objNotesVentaCab.getPcab_nrodni());
        txt_ruc.setValue(objNotesVentaCab.getPcab_nroruc());
        txt_lincred.setValue(String.valueOf(objNotesVentaCab.getPcab_limcre()));
        txt_limdoc.setValue(String.valueOf(objNotesVentaCab.getPcab_limdoc()));
        txt_saldo.setValue(String.valueOf(objNotesVentaCab.getPcab_salcre()));
        txt_estado.setValue(objNotesVentaCab.getPcab_estado() == 1 ? "ACTIVO" : "INACTIVO");
        txt_clidir_id.setValue(Long.parseLong(String.valueOf(objNotesVentaCab.getClidir_id())));
        txt_clidir_direcc.setValue(objNotesVentaCab.getPcab_dirent());
        txt_zon_id.setValue(objNotesVentaCab.getZon_id());
        txt_zon_des.setValue(objNotesVentaCab.getZon_des());
        txt_giro.setValue(objNotesVentaCab.getPcab_giro());
        txt_idvendedor.setValue(objNotesVentaCab.getVen_id());
        txt_nomvendedor.setValue(objNotesVentaCab.getVen_des());
        txt_diavis.setValue(objNotesVentaCab.getPcab_diavisdes());
        txt_id_listapre.setValue(Utilitarios.lpad(objNotesVentaCab.getLp_id(), 4, "0"));
        txt_des_listapre.setValue(objNotesVentaCab.getLp_des());
        txt_id_condven.setValue(Utilitarios.lpad(String.valueOf(objNotesVentaCab.getCon_id()), 3, "0"));
        txt_des_condven.setValue(objNotesVentaCab.getCond_des());
        txt_diaplazo.setValue(String.valueOf(objNotesVentaCab.getCon_dpla()));
        cb_pronpag.setValue(objNotesVentaCab.getPcab_ppago() == 1 ? "SI" : "NO");
        chk_modtip.setChecked(objNotesVentaCab.isValortipcam());
        txt_usuadd.setValue(objNotesVentaCab.getPcab_usuadd());
        d_fecadd.setValue(objNotesVentaCab.getPcab_fecadd());
        txt_usumod.setValue(objNotesVentaCab.getPcab_usumod());
        d_fecmod.setValue(objNotesVentaCab.getPcab_fecmod());
    }

    public void llenarCamposDetalle(NotesVentaCab objNotesVentaCab) throws SQLException {
        String nropedkey = objNotesVentaCab.getPcab_nroped();
        objlstNotesVentaDet = objDaoNotesVenta.listaNotesVentaDet(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), nropedkey);
        lst_procnotesvendet.setModel(objlstNotesVentaDet);
    }

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_total.setValue(data[0]);
    }

    public double[] calculosTotal() {
        int i;
        double data[] = new double[1];
        for (i = 0; i < objlstNotesVentaDet.getSize(); i++) {
            data[0] = data[0] + ((NotesVentaDet) objlstNotesVentaDet.get(i)).getPdet_pventa();
        }
        return data;
    }

    private void llenarCamposProducto(NotesVentaDet objNotesVentaDet) {
        txt_pedvendet_key.setValue(objNotesVentaDet.getPdet_item());// ITEM
        txt_proid.setValue(objNotesVentaDet.getPro_id());//COD Productos
        txt_prodes.setValue(objNotesVentaDet.getPdet_prodes());//Descripcion Producto
        txt_det_idlispre.setValue(objNotesVentaDet.getLp_id());//Codigo de lista
        txt_det_deslispre.setValue(objNotesVentaDet.getLp_des());//Descripcion de lista

        txt_cantentped.setValue(String.valueOf(objNotesVentaDet.getPdet_ent()));//Cant.Entera Pedido
        txt_cantfracped.setValue(String.valueOf(objNotesVentaDet.getPdet_frac()));//Cant.Fraccion Pedido        
        txt_cantentnotes.setValue(String.valueOf(objNotesVentaDet.getNesdet_ent()));//Cant.Entera Nota Salida 
        txt_cantfracnotes.setValue(String.valueOf(objNotesVentaDet.getNesdet_frac()));//Cant.Fraccion Nota Salida 

        db_det_preven.setValue(objNotesVentaDet.getPdet_punit());//P.Unit
        db_det_valvta.setValue(objNotesVentaDet.getPdet_vventa());///Valor Venta
        chk_desman.setChecked(objNotesVentaDet.isVal_descman());/// % Descuento
        db_det_pordes.setValue(objNotesVentaDet.getPdet_dscto());/// % Descuento
        db_det_valdes.setValue(objNotesVentaDet.getPdet_sdscto());// Descuento
        db_det_porigv.setValue(objNotesVentaDet.getPdet_impto());//% Impuesto
        db_det_valigv.setValue(objNotesVentaDet.getPdet_vimpto());//Impuesto
        db_det_total.setValue(objNotesVentaDet.getPdet_pventa());// Total       
    }

    public void limpiarVendedor() {
        txt_busq_idvendedor.setValue("");
        txt_busq_idvendedor.setValue("");
    }

    public void limpiarSupervisor() {
        txt_busq_idsupervisor.setValue("");
        txt_busq_nomsupervisor.setValue("");
    }

    public void limpiarlistaCombo() {    	
	    objlstNumeFac = new ListModelList<NumeFac>();
	    cb_proc_serie.setModel(objlstNumeFac);
	    cb_proc_serie.setValue("");
	       	
    }

    public void limpiarlista() {
        objlstNotesVentaCab = null;
        lst_procnotesvencab.setModel(objlstNotesVentaCab);
    }

    public void limpiarDetalle() {
        txt_pedvendet_key.setValue(null);
        txt_proid.setValue("");
        txt_prodes.setValue("");
        txt_det_idlispre.setValue("");
        txt_det_deslispre.setValue("");

        txt_cantentped.setValue("");
        txt_cantfracped.setValue("");
        txt_cantentnotes.setValue("");
        txt_cantfracnotes.setValue("");

        db_det_preven.setValue(null);
        db_det_valvta.setValue(null);
        db_det_pordes.setValue(null);
        db_det_valdes.setValue(null);
        db_det_porigv.setValue(null);
        db_det_valigv.setValue(null);
        db_det_total.setValue(null);
        txt_total.setValue(null);
    }

    public Object[][] getNotesVentaCab(ListModelList<NotesVentaCab> objLista) {
        //Creamos el arreglo con los detalles
        Object[][] listaNotesVentaCab = new Object[objLista.size()][48];
        //Barreros la lista que contiene los datos a grabar
        for (int i = 0; i < objLista.size(); i++) {
            //Llenamos la lista con los datos
            listaNotesVentaCab[i][0] = objLista.get(i).getPcab_nroped();
            listaNotesVentaCab[i][1] = objLista.get(i).getPcab_fecemi();
            listaNotesVentaCab[i][2] = objLista.get(i).getPcab_periodo();
            listaNotesVentaCab[i][3] = objLista.get(i).getEmp_id();
            listaNotesVentaCab[i][4] = objLista.get(i).getSuc_id();
            listaNotesVentaCab[i][5] = objLista.get(i).getPcab_situacion();
            listaNotesVentaCab[i][6] = objLista.get(i).getPcab_fecent();
            listaNotesVentaCab[i][7] = objLista.get(i).getZon_id();
            listaNotesVentaCab[i][8] = objLista.get(i).getPcab_motrec();
            listaNotesVentaCab[i][9] = objLista.get(i).getClidir_id();
            listaNotesVentaCab[i][10] = objLista.get(i).getPcab_dirent();
            listaNotesVentaCab[i][11] = objLista.get(i).getCli_id();
            listaNotesVentaCab[i][12] = objLista.get(i).getPcab_canid();
            listaNotesVentaCab[i][13] = objLista.get(i).getVen_id();
            listaNotesVentaCab[i][14] = objLista.get(i).getSup_id();
            listaNotesVentaCab[i][15] = objLista.get(i).getPcab_facbol();
            listaNotesVentaCab[i][16] = objLista.get(i).getCon_id();
            listaNotesVentaCab[i][17] = objLista.get(i).getPcab_mon();
            listaNotesVentaCab[i][18] = objLista.get(i).getLp_id();
            listaNotesVentaCab[i][19] = objLista.get(i).getPcab_tipcam();
            listaNotesVentaCab[i][20] = objLista.get(i).getPcab_limcre();
            listaNotesVentaCab[i][21] = objLista.get(i).getPcab_limdoc();
            listaNotesVentaCab[i][22] = objLista.get(i).getPcab_salcre();
            listaNotesVentaCab[i][23] = objLista.get(i).getPcab_nrodni();
            listaNotesVentaCab[i][24] = objLista.get(i).getPcab_nroruc();
            listaNotesVentaCab[i][25] = objLista.get(i).getPcab_totped();
            listaNotesVentaCab[i][26] = objLista.get(i).getPcab_diavis();
            listaNotesVentaCab[i][27] = objLista.get(i).getPcab_gpslat();
            listaNotesVentaCab[i][28] = objLista.get(i).getPcab_gpslon();
            listaNotesVentaCab[i][29] = objLista.get(i).getPcab_totper();
            listaNotesVentaCab[i][30] = objLista.get(i).getPcab_aprcon();
            listaNotesVentaCab[i][31] = objLista.get(i).getPcab_usuadd();
            listaNotesVentaCab[i][32] = objLista.get(i).getPcab_fecadd();
            listaNotesVentaCab[i][33] = objLista.get(i).getPcab_giro();
            listaNotesVentaCab[i][34] = objLista.get(i).getPcab_ppago();
            listaNotesVentaCab[i][35] = objLista.get(i).getPcab_tipven();
            listaNotesVentaCab[i][36] = objLista.get(i).getPcab_modtipcam();
            listaNotesVentaCab[i][37] = objLista.get(i).getCli_des();
            listaNotesVentaCab[i][38] = objLista.get(i).getVen_des();
            listaNotesVentaCab[i][39] = objLista.get(i).getPcab_situacion_des();
            listaNotesVentaCab[i][40] = objLista.get(i).getLp_des();
            listaNotesVentaCab[i][41] = objLista.get(i).getCond_des();
            listaNotesVentaCab[i][42] = objLista.get(i).getCon_dpla();
            listaNotesVentaCab[i][43] = objLista.get(i).getZon_des();
            listaNotesVentaCab[i][44] = objLista.get(i).getTip_ventades();
            listaNotesVentaCab[i][45] = objLista.get(i).getIndicador();
            listaNotesVentaCab[i][46] = objLista.get(i).getNotes_tip();
            listaNotesVentaCab[i][47] = objLista.get(i).getNotes_nro();

        }
        return listaNotesVentaCab;
    }

    public String verificar() {
        String s_mensaje = "";
        if (cb_proc_docventa.getSelectedIndex() == -1) {
            campo = "docvta";
            s_mensaje = "Debe seleccionar 'Tip Doc'";
        } else if (cb_proc_serie.getSelectedIndex() == -1) {
            campo = "serie";
            s_mensaje = "Debe seleccionar 'Serie'";
        } else if (d_fechadocventa.getValue() == null) {
            campo = "fdocventa";
            s_mensaje = "Debe ingresar 'Fecha Doc Vta'";
        } else if (d_fechadespacho.getValue() == null) {
            campo = "fdespacho";
            s_mensaje = "Debe ingresar 'Fecha Despacho'";
        } else {
            s_mensaje = "";
        }

        return s_mensaje;

    }   

    public String verificarFechas() throws ParseException {
        String s_valor = "";
        String f_docvta = sdf.format(d_fechadocventa.getValue());
        String fechadespacho = sdf.format(d_fechadespacho.getValue());
        Date fechauno = sdf.parse(f_docvta);
        Date fechados = sdf.parse(fechadespacho);

       //if (d_fechadocventa.getValue().getTime() > d_fechadespacho.getValue().getTime()) {//se modifico>=
      if (fechados.before(fechauno)) {     
            s_valor = "La fecha de despacho debe ser mayor que : " + f_docvta;
        } else {
            s_valor = "";
        }

        return s_valor;
    }
    
    public String validaClienteFacturaBoleta(){
    	String notasalida="", mensaje="";
    	int j = 0, i=0;
    	int tipdoc = cb_proc_docventa.getSelectedItem().getValue(); 
    	String facbol = cb_proc_docventa.getValue();
    	
        while (j < objlstNotesVentaCab.getSize()) {			
			if(objlstNotesVentaCab.get(j).isValSelec() && objlstNotesVentaCab.get(j).getClibolfac()!= tipdoc) {
				notasalida +=  objlstNotesVentaCab.get(j).getNotes_nro()+",";
                 i++;
			}
			j++;
        }
        
        if(i == objlstNotesVentaCab.getSize()){
        	mensaje = "Los clientes seleccionados no emiten '"+facbol+"'";
        }else{
        	notasalida  = notasalida.length()>0?notasalida.substring(0,notasalida.length()-1):"";
        	mensaje = notasalida.isEmpty()?"":"Los clientes seleccionados no emiten '"+facbol+"' en las siguientes notas de salida : \n"+notasalida;
        }
        return mensaje;
    }
    
    public void habilitaGenera(boolean b_valida1){    	
    	cb_proc_docventa.setDisabled(b_valida1);
    	cb_proc_serie.setDisabled(b_valida1);    
    	d_fechadocventa.setDisabled(b_valida1);
    	d_fechadespacho.setDisabled(b_valida1);
    	btn_generar.setDisabled(b_valida1);
    }
    
          
    
    //metodos sin utilizar
    public void botonGuardar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonNuevo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonDeshacer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEditar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void limpiarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
