/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerMovimiento;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerInformesPeriodo extends SelectorComposer<Component> {

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

    @Wire
    Checkbox chk_selecAll;
    ListModelList<PerPago> objListPerPago = new ListModelList<PerPago>();
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
        txt_periodo1 = (Textbox) parametros.get("per");
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

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        for (int i = 0; i < objListPerPago.getSize(); i++) {
            objListPerPago.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_periodo.setModel(objListPerPago);
    }

    @Listen("onSeleccion=#lst_periodo")
    public void seleccionaRegistro(ForwardEvent evt) {
        objListPerPago = (ListModelList) lst_periodo.getModel();
        if (!objListPerPago.isEmpty() || objListPerPago != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objListPerPago.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_periodo.setModel(objListPerPago);
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void acepta() throws SQLException {
        int y = contar();
        if (y == 1 || y == 0) {
            String cadena = obtenerCodigo();//obtenemos la cadena de caracteres
            String cadena1 = cadena.replace("','", " ");//se reemplaza ',' por vacio
            txt_periodo.setValue(cadena1.trim());//seteamos el campo si los caracteres que se envia al zul 
            //txt_periodo.setValue(obtenerCodigo());
            txt_periodo1.setValue(obtenerCodigo());
        } else {
            txt_periodo.setValue("ALL");//TRANPARENTE
            txt_periodo1.setValue(obtenerCodigo());//ESTE CAMPO ES EL QUE SE VA A SETEAR
        }
// txt_desper_man.setValue(obtenerNombre());
        w_periodo.detach();
    }

    public String obtenerCodigo() throws SQLException {
        String cadena = "";
        //int i = 0;
        for (int j = 0; j < objListPerPago.getSize(); j++) {
            if (objListPerPago.get(j).isValSelec()) {
                cadena = cadena + objListPerPago.get(j).getPerPag_id() + "','";
            }
        }
        return cadena;

    }

    @Listen("onClose=#w_periodo")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerMovimiento")) {
            ControllerMovimiento.bandera = false;
        }
    }

    /**
     * Metodo para ver cuantos periodos a seleccionado
     * @return numero elegido
     */
    public int contar() {
        int a = 0;
        for (int i = 0; i < objListPerPago.getSize(); i++) {
            if (objListPerPago.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }

}
