/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerBoletaPago;
import org.me.gj.controller.planillas.informes.ControllerInfUtil;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerPeriodoAnterior extends SelectorComposer<Component> {

    @Wire
    Window w_periodo;
    @Wire
    Combobox cb_tipPeriodo;
    @Wire
    Listbox lst_periodo;
    @Wire
    Button btn_aceptar;
    @Wire
    Textbox txt_periodo, txt_periodo1;
    ListModelList<PerPago> objListPerPago = new ListModelList<PerPago>();
    PerPago objPeriodo;
    DaoPerPago objDaoPerPago;
    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoPerPago = new DaoPerPago();

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_periodo = (Textbox) parametros.get("periodo");
        //  txt_periodo1 = (Textbox) parametros.get("per");
        controlador = (String) parametros.get("controlador");
        cb_tipPeriodo.setModel(cargarComboTipoPlanilla());
        objListPerPago = new DaoPerPago().busquedaPeriodoAnterior(""); //Planilla NORMAL 03
        lst_periodo.setModel(objListPerPago);

    }

    public ListModelList<PerPago> cargarComboTipoPlanilla() throws SQLException {
        objListPerPago = new ListModelList<PerPago>();
        objListPerPago = new DaoPerPago().listaTipPeriodo();
        objListPerPago.add(0, new PerPago("", ""));
        return objListPerPago;
    }

    @Listen("onClick=#btn_consultar")
    public void consultaRegistros() throws SQLException {
        String s_tipPeri = cb_tipPeriodo.getValue().trim();
        int s_indexTipPeri = cb_tipPeriodo.getSelectedItem().getIndex();
        String s_consulta = s_indexTipPeri != 0 ? s_tipPeri.substring(0, 2) : "";
        objListPerPago = new DaoPerPago().busquedaPeriodoAnterior(s_consulta);
        lst_periodo.setModel(objListPerPago);

    }

    @Listen("onClose=#w_periodo")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerBoletaPago")) {
            ControllerBoletaPago.bandera = false;
		}if (controlador.equals("ControllerInfUtil")) {
                ControllerInfUtil.bandera = false;
        }
    }

    @Listen("onOK=#lst_periodo;onClick=#lst_periodo")
    public void seleccion() {
        try {
            objPeriodo = (PerPago) lst_periodo.getSelectedItem().getValue();
            txt_periodo.setValue(objPeriodo.getPerPag_id());
            lst_periodo.focus();

            if (controlador.equals("ControllerBoletaPago")) {
                ControllerBoletaPago.bandera = false;
			}if (controlador.equals("ControllerInfUtil")) {
                ControllerInfUtil.bandera = false;
            }
            w_periodo.detach();
        } catch (Exception e) {
        }

    }

}
