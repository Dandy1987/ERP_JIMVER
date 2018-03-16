/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author wcamacho
 */
public class ControllerEliminarCalculo extends SelectorComposer<Component> {
    
    @Wire
    private Textbox txt_periodo;
    @Wire
    private Button btn_eliminar;
	@Wire
    Label lbl_periododesc;
    
    private DaoPerPago objDaoPerPago;
    private DaoPlanilla objDaoPlanilla;
    ParametrosSalida objParametrosSalida;
	DaoDescuentos objDaoDescuentos;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerEliminarCalculo.class);
    
     @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //objDaoMovLinea = new DaoMovLinea();
        objDaoPerPago = new DaoPerPago();
		objDaoDescuentos = new DaoDescuentos();
        //String periodo = objDaoMovLinea.setearPeriodo();
        String periodo = objDaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo.equals("")? "--------" : periodo);        
        // objlstSucursal = objDaoPersonal.lstSucursales(emp_id); //se comento
		String periodo_descrip = objDaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);
        
    }
	
    @Listen("onClick=#btn_eliminar")
    public void botonEliminarCalculo() {
        try {
            //Capturar datos ingresados
            String periodo_proceso = txt_periodo.getValue();
            if (periodo_proceso.equals("") /*|| periodo_proceso == null*/ || periodo_proceso.equals("--------")) {
                Messagebox.show("No hay ningun periodo calculado", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {
                if (objDaoDescuentos.validaPeriodoCalculando(txt_periodo.getValue().toString()) == 1) {
                Messagebox.show("La planilla se encuentra calculando", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objDaoPlanilla = new DaoPlanilla();
                objParametrosSalida = objDaoPlanilla.eliminar(objUsuCredential.getCodemp(), periodo_proceso);
                Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            }
        } catch (SQLException ex) {
            Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
}
