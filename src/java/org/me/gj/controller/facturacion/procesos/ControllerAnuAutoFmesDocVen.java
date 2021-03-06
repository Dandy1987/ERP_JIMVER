package org.me.gj.controller.facturacion.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.informes.DaoCtaCob;
import org.me.gj.controller.cxc.informes.DaoVtas;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.facturacion.mantenimiento.DaoTipoVenta;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.CtaCobCab;
import org.me.gj.model.cxc.mantenimiento.VtasDet;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.facturacion.mantenimiento.TipoVenta;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;



public class ControllerAnuAutoFmesDocVen extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listaAnuFmes, tab_mantenimientoAnuFmes;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_buscar, tbbtn_btn_nuevopro, tbbtn_btn_editarpro, tbbtn_btn_eliminarpro,
            tbbtn_btn_guardarpro, tbbtn_btn_deshacerpro, tbbtn_busq_buscar;
    @Wire
    Combobox cb_tipoventa ;
    @Wire
    Datebox d_busq_fecha, d_fecemi, d_fecadd, d_fecmod;
    
    @Wire
    Doublebox db_limcred , txt_cant,txt_total,db_punit,db_valvta,db_pordes,db_valdes,db_porigv,db_valigv,db_total;
    
    @Wire
    Textbox  txt_busq_supid, txt_busq_supdes,txt_busq_venid,txt_busq_vendes,txt_lstvenid,txt_lstvendes,txt_lstsupid,txt_lstsupdes,txt_usuadd, txt_usumod,
             txt_docdes,txt_nrodoc,txt_estado,txt_cli_id,txt_cli_razsoc,txt_ven_id,txt_ven_des,txt_nroped,
             txt_zon_id,txt_zon_des,txt_lp_id,txt_lp_des,txt_conid,txt_condes,txt_trans_id,txt_trans_des,
             txt_dni,txt_ruc,txt_limdoc,txt_clidir_des,txt_diavisdes,txt_chofid,txt_chofdes, txt_trans_placa,
             txt_proid,txt_prodes,txt_lpid,txt_lpdes;
    @Wire
    Listbox lst_anudocven, lst_docven;
    @Wire
    Button btn_consulta, btn_recped;
    @Wire
    Intbox  txt_frac , txt_diaplazo;
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_modtip, chk_desman;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPeriodos;
    ListModelList<CtaCobCab> objlstctacobcab;
    ListModelList<TipDoc> objlstTipDoc;  
    ListModelList<TipoVenta> objlstTipoventa;    
    ListModelList<VtasDet> objlstVtasDet;
    DaoAccesos objDaoAccesos;    
    DaoManPer objDaoManPer;
    DaoTipDoc objDaoTipDoc;
    DaoCtaCob objDaoCtaCob;
    DaoTipoVenta objDaoTipoVenta;
    DaoVtas objDaoVtasDet;
    Accesos objAccesos;
    CtaCobCab objCtaCobCab;
    VtasDet objVtasDet;
    //Variables publicas
    int i_sel;
    int i_selCab, i_selDet;
    int emp_id;
    int suc_id;
    int monid;
    String s_estado = "Q";
    String s_estado_detalle = "Q";
    String s_mensaje;    
    String modoEjecucion;
    String fechaActual;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdffe = new SimpleDateFormat("EEE");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAnuAutoFmesDocVen.class);

    //Eventos Primarios - Transaccionales	
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	    	
    	objlstTipDoc = new  ListModelList<TipDoc>();
    	objlstManPeriodos = new ListModelList<ManPer> ();
		objlstTipoventa = new ListModelList<TipoVenta>();
    	objDaoCtaCob = new DaoCtaCob();
    	objDaoTipDoc = new DaoTipDoc();
    	objDaoTipoVenta = new DaoTipoVenta();
    	objDaoManPer = new DaoManPer();
    	objCtaCobCab = new CtaCobCab();
    	objDaoVtasDet = new DaoVtas();
    	
    	objlstVtasDet = new ListModelList<VtasDet>();
    	objVtasDet = new VtasDet();
        
    }
    
    @Listen("onCreate=#tabbox_anufmes")
    public void llenaRegistros() throws SQLException {
    	//carga tipo de venta
        objlstTipoventa = objDaoTipoVenta.listaTipoVentas(1);
        objlstTipoventa.add(new TipoVenta(100, ""));
        cb_tipoventa.setModel(objlstTipoventa);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40203010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Generacion de Pedido de Venta con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado Procesos Generacion de Pedido de Venta con el rol: USUARIO NORMAL");
        }
        /*if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creacion de un Pedido de Venta");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creacion de un Pedido de Venta");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edicion de un Pedido de Venta");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edicion de un Pedido de Venta");
        }*/
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminacion de un Pedido de Venta");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminacion de un Pedido de Venta");
        }
        /*if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Pedidos de Venta");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Pedidos de Venta");
        }*/
    }

    @Listen("onClick=#btn_consulta")
    public void busquedaRegistros() throws SQLException {        
    	limpiarBusqueda();
    	
        Date fecha_emisioni = d_busq_fecha.getValue();
        String f_emisioni = fecha_emisioni == null ? "":sdf.format(fecha_emisioni);      
        
        objlstctacobcab = objDaoCtaCob.BusquedaFacturaBoletaxFecha(f_emisioni);
            
        //Validar la tabla sin registro
        if (objlstctacobcab.getSize() > 0) {
              lst_anudocven.setModel(objlstctacobcab);
        } else {            	
             Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }     
    }
    

    @Listen("onCtrlKey=#w_anumandocven")
    public void ctrl_teclado(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {            
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    botonEliminar();
                }
                break;         
        }
    }
    
    
    @Listen("onSelect=#lst_anudocven")
    public void seleccionaRegistro() throws SQLException {
        objCtaCobCab = (CtaCobCab) lst_anudocven.getSelectedItem().getValue();
        if (objCtaCobCab == null) {
        	objCtaCobCab = objlstctacobcab.get(i_selCab + 1);
        }
        i_selCab = lst_anudocven.getSelectedIndex();
        limpiarCampos();
        limpiarCampoProductos(); 
        limpiarAuditoria();
        llenarCampos(objCtaCobCab);        
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCtaCobCab.getNescab_id());
    }

    @Listen("onSelect=#lst_docven")
    public void seleccionaRegistroDetalle() throws SQLException { 
        objVtasDet = (VtasDet) lst_docven.getSelectedItem().getValue();
        if (objVtasDet == null) {
        	objVtasDet = objlstVtasDet.get(i_sel + 1);
        }
        i_sel = lst_docven.getSelectedIndex();
       
        limpiarCampoProductos();        
        llenarCamposProducto(objVtasDet);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el producto con el  " + objVtasDet.getPro_id());
    }


   
    //Eventos Secundarios - Validacion    
    @Listen("onOK=#d_busq_fecha")
    public void onOKBusqFechaEmision(){
    	btn_consulta.focus();
    }
    

    // Eventos Otros
	public void llenarCampos(CtaCobCab objCtaCobCab) throws SQLException {
		//Datos busqueda
		txt_lstsupid.setValue(objCtaCobCab.getSup_id());
		txt_lstsupdes.setValue(objCtaCobCab.getSup_des());
		txt_lstvenid.setValue(objCtaCobCab.getVen_id());
		txt_lstvendes.setValue(objCtaCobCab.getVen_des());		
		txt_usuadd.setValue(objCtaCobCab.getCtacob_usuadd());
		d_fecadd.setValue(objCtaCobCab.getCtacob_fecadd());
		txt_usumod.setValue(objCtaCobCab.getCtacob_usumod());
		d_fecmod.setValue(objCtaCobCab.getCtacob_fecmod());		
		
		// Datos Documentos de Venta
		txt_docdes.setValue(objCtaCobCab.getCtacob_tipdocdesdes());
		txt_nrodoc.setValue(objCtaCobCab.getCtacob_nrodoc());
		cb_tipoventa.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipoventa, Integer.parseInt(objCtaCobCab.getTipvenid())));
		txt_estado.setValue(objCtaCobCab.getCtacob_estadodes());
		txt_cli_id.setValue(objCtaCobCab.getCli_id());
		txt_cli_razsoc.setValue(objCtaCobCab.getCli_des());
		txt_dni.setValue(objCtaCobCab.getNroiden());
		txt_ruc.setValue(objCtaCobCab.getCli_ruc());
		
		txt_ven_id.setValue(objCtaCobCab.getVen_id());
	    txt_ven_des.setValue(objCtaCobCab.getVen_des());
        txt_zon_id.setValue(objCtaCobCab.getZon_id());
        txt_zon_des.setValue(objCtaCobCab.getZon_des());
        txt_lp_id.setValue(objCtaCobCab.getLp_id());
        txt_lp_des.setValue(objCtaCobCab.getLp_des());
        txt_conid.setValue(objCtaCobCab.getCon_id());
        txt_condes.setValue(objCtaCobCab.getCon_des());
        txt_trans_id.setValue(Utilitarios.lpad(objCtaCobCab.getTrans_id(),4,"0"));
        txt_trans_des.setValue(objCtaCobCab.getTrans_des());
        txt_clidir_des.setValue(objCtaCobCab.getClidir_des());
        txt_clidir_id.setValue((long) objCtaCobCab.getClidir_id());
        txt_diaplazo.setValue(objCtaCobCab.getCtacob_diapla());
        txt_diavisdes.setValue(objCtaCobCab.getDiavisdes());
        txt_chofid.setValue(objCtaCobCab.getChof_id());
        txt_chofdes.setValue(objCtaCobCab.getChof_des());
        txt_trans_placa.setValue(objCtaCobCab.getTrans_placa());
        db_limcred.setValue(objCtaCobCab.getLimcred());
        txt_limdoc.setValue(objCtaCobCab.getLimdoc());
        txt_nroped.setValue(objCtaCobCab.getPcab_nroped());
        
        llenarListaDetalle(objCtaCobCab.getCtacob_key());
        llenarCamposTotales();	
        
	}

	public void llenarListaDetalle(String vtas_key ) throws SQLException{
		objlstVtasDet = objDaoVtasDet.listaVentasDetallexCodigo(vtas_key);
    	lst_docven.setModel(objlstVtasDet);     	
	}
	
	 public void llenarCamposTotales() {
	    double data[] = calculosTotal();
	    txt_total.setValue(data[0]);
	 }

	 public double[] calculosTotal() {
	     int i;
	     double data[] = new double[1];
	     for (i = 0; i < objlstVtasDet.getSize(); i++) {
	            data[0] = data[0] + ((VtasDet) objlstVtasDet.get(i)).getVtas_pventa();
	        }
	        return data;
	 }

	 private void llenarCamposProducto(VtasDet objVtasDet) {		 
		 txt_proid.setValue(objVtasDet.getPro_id());
		 txt_prodes.setValue(objVtasDet.getPro_des());
		 txt_lpid.setValue(objVtasDet.getLp_id());
		 txt_lpdes.setValue(objVtasDet.getLp_des());
		 txt_cant.setValue(objVtasDet.getCantent());
		 txt_frac.setValue(objVtasDet.getCantfrac());
		 db_punit.setValue(objVtasDet.getVtas_punit());
		 db_valvta.setValue(objVtasDet.getVtas_vventa());
		 db_pordes.setValue(objVtasDet.getVtas_dsctot());
		 db_valdes.setValue(objVtasDet.getVtas_sdsctot());		 
		 db_porigv.setValue(objVtasDet.getVtas_impto());
		 db_valigv.setValue(objVtasDet.getVtas_vimpto());
		 db_total.setValue(objVtasDet.getVtas_pventa());
	 }  
        
    public void limpiarBusquedaSupervisor(){
        txt_busq_venid.setValue("");
        txt_busq_vendes.setValue("");
    }
    
    public void limpiarBusquedaVendedor(){
    	txt_busq_venid.setValue("");
    	txt_busq_vendes.setValue("");
    }
    
    public void limpiarlista(){
    	objlstctacobcab = new ListModelList<CtaCobCab>();
    	lst_anudocven.setModel(objlstctacobcab);
    }
	
	 public void limpiarListaDetalle(){
		 objlstVtasDet = new ListModelList<VtasDet>();
		 lst_docven.setModel(objlstVtasDet);
	 }
	 
	 public void limpiarCampos() {
			txt_docdes.setValue("");
			txt_nrodoc.setValue("");
			cb_tipoventa.setSelectedIndex(-1);
			txt_estado.setValue("");
			txt_cli_id.setValue("");
			txt_cli_razsoc.setValue("");
			txt_dni.setValue("");
			txt_ruc.setValue("");
			
			txt_ven_id.setValue("");
		    txt_ven_des.setValue("");
	        txt_zon_id.setValue("");
	        txt_zon_des.setValue("");
	        txt_lp_id.setValue("");
	        txt_lp_des.setValue("");
	        txt_conid.setValue("");
	        txt_condes.setValue("");
	        txt_trans_id.setValue("");
	        txt_trans_des.setValue("");
	        txt_clidir_des.setValue("");
	        txt_clidir_id.setValue(null);
	        txt_diaplazo.setValue(null);
	        txt_diavisdes.setValue("");
	        txt_chofid.setValue("");
	        txt_chofdes.setValue("");
	        txt_trans_placa.setValue("");
	        db_limcred.setValue(null);
	        txt_limdoc.setValue("");
	        txt_nroped.setValue("");
			
	}
	 
	 public void limpiarCampoProductos(){
		 txt_proid.setValue("");
		 txt_prodes.setValue("");
		 txt_lpid.setValue("");
		 txt_lpdes.setValue("");
		 txt_cant.setValue(null);
		 txt_frac.setValue(null);
		 db_punit.setValue(null);
		 db_valvta.setValue(null);
		 db_pordes.setValue(null);
		 db_valdes.setValue(null); 
		 db_porigv.setValue(null);
		 db_valigv.setValue(null);
		 db_total.setValue(null);
	 }
	 
	 public void limpiarBusqueda(){
		limpiarlista();
		limpiarListaDetalle();
		limpiarCampos();
		limpiarCampoProductos();
		limpiarAuditoria(); 
		txt_lstsupid.setValue("");
		txt_lstsupdes.setValue("");
		txt_lstvenid.setValue("");
		txt_lstvendes.setValue("");
	}
	   
	 public void limpiarAuditoria(){
		 txt_usuadd.setValue("");
		 d_fecadd.setValue(null);
		 txt_usumod.setValue("");
		 d_fecmod.setValue(null);
	 }
	    
	 
	@Override
	public void botonGuardar() throws SQLException {
		// TODO Auto-generated method stub		
	}
	@Override
	public void botonNuevo() throws SQLException {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void botonDeshacer() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void botonEditar() throws SQLException {
	}

	@Override
	public void OnChange() {
	}
	
	@Override
	public void validaBusqueda(InputEvent event) throws SQLException {
	}
		
		
	@Override
	public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
	}

	@Override
	public void habilitaTab(boolean b_valida1, boolean b_valida2) {
	}

	@Override
	public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
	}


	@Override
	public void habilitaCampos(boolean b_valida1) {	
	}

	@Override
	public String verificar() {
		return null;
	}

	@Override
	public void botonEliminar() throws SQLException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void llenarCampos() {
		// TODO Auto-generated method stub
		
	}

	
}
