/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovDescuentosFaltantes extends SelectorComposer<Component> {

    @Wire
    Combobox cb_fsucursal;
    @Wire
    Checkbox chk_rango;
    @Wire
    Datebox d_fecha, d_inicio, d_fin;
    @Wire
    Textbox txt_periodo;
    @Wire
    Button btn_consultar, btn_cancelar, btn_procesar;
    @Wire
    Window w_lov_faltantes;
    String foco;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    DaoAccesos objDaoAccesos;
    DaoPerPago objDaoPerPago;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoAccesos = new DaoAccesos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        objDaoPerPago = new DaoPerPago();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);

    }

    @Listen("onClick=#chk_rango")
    public void check() {
        boolean check = chk_rango.isChecked();
        if (check == true) {
            habilitarFecha(false);

        } else {
            habilitarFecha(true);
        }

    }

    public void habilitarFecha(boolean t) {
        d_inicio.setDisabled(t);
        d_fin.setDisabled(t);
    }

    public String verificaFecha() {
        String valor = "";
        if (d_fin.getValue().before(d_inicio.getValue())) {
            valor = "La fecha final debe ser mayor a la de inicio";
            foco = "fecha";

        }
        return valor;
    }

    @Listen("onClick=#btn_cancelar")
    public void cerrar() {
        w_lov_faltantes.detach();

    }

}
