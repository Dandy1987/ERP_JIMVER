/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

/**
 *
 * @author achocano
 */
public class ControllerProcesoPeriodo extends SelectorComposer<Component> {

    @Wire
    Combobox cb_periodo;
    @Wire
    Tab tab_lista;
    @Wire
    Button btn_aceptar;

    ListModelList<PerPago> objListPerPago;
    DaoPerPago objDaoPerPago;
    //DaoProcesoPeriodo objDaoPPeriodo;
	DaoCierres objDaoCierres;
     Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProcesoPeriodo.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        objListPerPago = new DaoPerPago().busquedaProceso();
        cb_periodo.setModel(objListPerPago);
        objDaoPerPago = new DaoPerPago();
		objDaoCierres = new DaoCierres();
    }
	
@Listen("onClick=#btn_aceptar")
    public void aceptar() throws SQLException {

        //int  periodo = objDaoPPeriodo.validaPeriodo(objUsuCredential.getCodemp(),cb_periodo.getValue());        
        int i_periodo_proceso = objDaoPerPago.validaPeriodo(objUsuCredential.getCodemp(), cb_periodo.getValue());
        if (i_periodo_proceso == 0) {
            Messagebox.show("Se puso el periodo en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             ParametrosSalida objPara =objDaoCierres.activaAutomatico(cb_periodo.getValue());
        } else {
            Messagebox.show("Existe un periodo en PROCESO o CALCULADO", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }
}
