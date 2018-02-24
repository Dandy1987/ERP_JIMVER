package org.me.gj.controller.cxc.informes;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.CtaCobCab;
import org.me.gj.model.cxc.mantenimiento.VtasDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerEliminaArmotizacion extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Listbox lst_detdoc, lst_movdoc;
    @Wire
    Intbox txt_dplazo;
    @Wire
    Doublebox txt_totvvta, txt_igv, txt_totventa, txt_totimper, txt_tipcam;
    @Wire
    Textbox txt_tipid, txt_tipdes, txt_nrodoc, txt_cli_id, txt_cli_des, txt_nroiden,  txt_direcc, txt_nescab_id,
            txt_nroped,  txt_zon_id, txt_zon_des, txt_ven_id, txt_ven_des,  txt_lp_id, txt_lp_des,
            txt_conid, txt_condes,  txt_trans_des, txt_moneda, txt_total, txt_saldo, txt_saldochq,txt_ruc,txt_trans_id,
            txt_docref_id, txt_docref_des, txt_terminal, txt_red, txt_glosa, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_cliest;
    @Wire
    Datebox d_fecemi, d_feccheque, d_fecreg, d_fecanu, d_fecadd, d_fecmod;
    @Wire
    Combobox cb_tipdoc;
    
    //Instancia de Objetos
    ListModelList<TipDoc> objlstTipDoc;
    ListModelList<VtasDet> objlstVtasDet;
    DaoTipDoc objDaoTipDoc;
    DaoCtaCob objDaoCtaCob;
    DaoVtas objDaoVtasDet;
    CtaCobCab objCtaCobCab;
    VtasDet objVtasDet;
    
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    //Variables publicas
    String s_nrodoc;
	int s_tipdoc;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerEliminaArmotizacion.class);

    
    @Override
    public void doAfterCompose(Component comp) throws Exception {    	
    	super.doAfterCompose(comp);
        
    	limpiarCampos();  
        limpiarListaDocumento();
    	
    	objlstTipDoc = new ListModelList<TipDoc>();
        objlstVtasDet = new ListModelList<VtasDet>();
        objDaoTipDoc = new DaoTipDoc();
        objDaoCtaCob = new DaoCtaCob();
        objDaoVtasDet = new DaoVtas();
        objCtaCobCab = new CtaCobCab();
        objVtasDet 	 = new VtasDet();
    	
        //carga tipo documento
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_tipdoc.setModel(objlstTipDoc);     	
    	
    }
    
    // Eventos Primarios - Transaccionales
    @Listen("onCreate=#div_eliarmo")
    public void llenaRegistros() throws SQLException {        
        if (ControllerConsultaxCliente.controlador.equals("ControllerConsultaxCliente")) {
        	 habilitaBotones(true, true);
             tbbtn_btn_imprimir.setDisabled(false);
         	 ControllerConsultaxCliente.controlador = "";
             s_nrodoc = ControllerConsultaxCliente.s_nrodoc;
             s_tipdoc = ControllerConsultaxCliente.s_tipdoc; 
             txt_nrodoc.setValue(s_nrodoc);
             cb_tipdoc.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipdoc, s_tipdoc));             
             llenarCampos(s_nrodoc, s_tipdoc);    
         }
    }

   @Listen("onBlur=#txt_nrodoc;onSelect=#cb_tipdoc")
    public void onBlurNumeroDocumento() throws WrongValueException, SQLException{    	
    	if(txt_nrodoc.getValue().isEmpty()){
    		limpiarCampos();
    		limpiarListaDocumento();
    	}else if(!txt_nrodoc.getValue().matches("[0-9]*")){
    		Messagebox.show("Debe ingresar valores numericos en el campo 'NRO DOC.'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
               	 limpiarCampos();
               	 txt_nrodoc.focus();
                }
            });
    	}else {
    		if(cb_tipdoc.getSelectedIndex() !=-1){
    				String nrodoc = txt_nrodoc.getValue();
    				int tipdoc = cb_tipdoc.getSelectedItem().getValue();
    				llenarCampos(nrodoc, tipdoc);	    			
	    		}	    		
    		}
    }
    
    
    public void llenarCampos(String nrodoc , int tipdoc) throws WrongValueException, SQLException {        
		objCtaCobCab = objDaoCtaCob.listaCtaCabxNroDocumento(nrodoc, tipdoc);
		
		if(objCtaCobCab != null){	
			txt_cli_id.setValue(objCtaCobCab.getCli_id());
    		txt_cli_des.setValue(objCtaCobCab.getCli_des());
    		txt_direcc.setValue(objCtaCobCab.getClidir_des());
    		txt_ruc.setValue(objCtaCobCab.getCli_ruc());
    		txt_ven_id.setValue(objCtaCobCab.getVen_id());
    	    txt_ven_des.setValue(objCtaCobCab.getVen_des());
    	    txt_zon_id.setValue(objCtaCobCab.getZon_id());
            txt_zon_des.setValue(objCtaCobCab.getZon_des());
            txt_trans_id.setValue(Utilitarios.lpad(objCtaCobCab.getTrans_id(),4,"0"));
            txt_trans_des.setValue(objCtaCobCab.getTrans_des());
            txt_condes.setValue(objCtaCobCab.getCon_des());
            txt_conid.setValue(objCtaCobCab.getCon_id());
            txt_tipcam.setValue(objCtaCobCab.getCtacob_tipcam());
            txt_nroped.setValue(objCtaCobCab.getPcab_nroped());
            txt_nroiden.setValue(objCtaCobCab.getNroiden());
            txt_lp_id.setValue(objCtaCobCab.getLp_id());
            txt_lp_des.setValue(objCtaCobCab.getLp_des());
            txt_nescab_id.setValue(objCtaCobCab.getNescab_id());
            txt_moneda.setValue(objCtaCobCab.getCtacob_mondes());
            txt_total.setValue(objCtaCobCab.getCtacob_stotal());
            txt_saldo.setValue(objCtaCobCab.getCtacob_ssaldo());
            txt_dplazo.setValue(objCtaCobCab.getCtacob_diapla());
            //Auditoria
            txt_usuadd.setValue(objCtaCobCab == null ?"":objCtaCobCab.getCtacob_usuadd());
            d_fecadd.setValue(objCtaCobCab == null?null:objCtaCobCab.getCtacob_fecadd());
            txt_usumod.setValue(objCtaCobCab == null?"":objCtaCobCab.getCtacob_usumod());
            d_fecmod.setValue(objCtaCobCab == null?null:objCtaCobCab.getCtacob_fecmod());
            
            //Detalle            
            llenarDetalle(objCtaCobCab.getCtacob_key());
            
		
		}else{
        	Messagebox.show("El codigo no existe o esta eliminado'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                 public void onEvent(Event t) throws Exception {
                	 limpiarCampos();
                	 limpiarListaDocumento();
                	  cb_tipdoc.setSelectedIndex(-1);
                	 txt_nrodoc.focus();
                 }
             });
        }
    }
    
    public void llenarDetalle(String vtas_key) throws SQLException{
    	objlstVtasDet = objDaoVtasDet.listaVentasDetallexCodigo(vtas_key);
    	lst_detdoc.setModel(objlstVtasDet);
    	
        double data[] = calcularTotales();
        txt_totvvta.setValue(data[0]);
        txt_igv.setValue(data[1]);
        txt_totventa.setValue(data[2]);    	       
    }
       
    
    public double[] calcularTotales() {
        double totales[] = new double[4];
        for (int i = 0; i < objlstVtasDet.getSize(); i++) {
            totales[0] = totales[0] + ((VtasDet) objlstVtasDet.get(i)).getVtas_vventa();
            totales[1] = totales[1] + ((VtasDet) objlstVtasDet.get(i)).getVtas_vimpto();
            totales[2] = totales[2] + ((VtasDet) objlstVtasDet.get(i)).getVtas_pventa();            
            
        }
        return totales;
    }
    
    public void limpiarCampos() {
    	txt_nrodoc.setValue("");
    	txt_cli_id.setValue("");
		txt_cli_des.setValue("");
		txt_direcc.setValue("");
		txt_ruc.setValue(null);
		txt_ven_id.setValue("");
	    txt_ven_des.setValue("");
	    txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_trans_des.setValue("");
        txt_condes.setValue("");
        txt_conid.setValue("");
        txt_tipcam.setValue(null);
        txt_nroped.setValue("");
        txt_nroiden.setValue("");
        txt_lp_id.setValue("");
        txt_lp_des.setValue("");
        txt_nescab_id.setValue("");
        txt_moneda.setValue("");
        txt_total.setValue("");
        txt_saldo.setValue("");
        txt_dplazo.setValue(null);    
    }

    public void limpiarListaDocumento(){
    	objlstVtasDet = null;
    	lst_detdoc.setModel(objlstVtasDet);
    }    
    
    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }
    
    public void botonGuardar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEliminar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void seleccionaRegistro() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void busquedaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonDeshacer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void botonEditar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void VerificarTransacciones() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }
    
    public void habilitaCampos(boolean b_valida1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	@Override
	public void llenarCampos() {
		// TODO Auto-generated method stub
		
	}

}
