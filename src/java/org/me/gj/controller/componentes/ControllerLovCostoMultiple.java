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
import org.me.gj.controller.planillas.informes.ControllerPatronE;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovCostoMultiple extends SelectorComposer<Component> {

    @Wire
    Window w_lov_ccostos;

    @Wire
    Listbox lst_costo;
    @Wire
    Button btn_aceptar;
    @Wire
    Textbox txt_cod_costo, txt_des_costo, txt_busqueda_costos, txt_costo1;
    @Wire
    Checkbox chk_selecAll;
    ListModelList<ManCCostos> objlstManCCostos;
    ListModelList<ManCCostos> objlstManCCostos2;
    ManCCostos objCCostos;

    DaoManAreas objDaoAreas = new DaoManAreas();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_cod_costo = (Textbox) parametros.get("costo");
        txt_des_costo = (Textbox) parametros.get("descripcion");
        txt_costo1 = (Textbox) parametros.get("costo1");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_ccostos")
    public void cargarCosto() throws SQLException {
        objlstManCCostos = objDaoAreas.busquedaLovCCostos();
        lst_costo.setModel(objlstManCCostos);
        lst_costo.focus();
        txt_busqueda_costos.focus();
    }

    @Listen("onOK=#txt_busqueda_costos")
    public void buscarCCostos() throws SQLException {
        String consulta = txt_busqueda_costos.getValue().toUpperCase();
        objlstManCCostos.clear();
        objlstManCCostos = objDaoAreas.busquedaLovCCostos2(consulta);
        lst_costo.setModel(objlstManCCostos);

    }

    @Listen("onChange=#txt_busqueda_costos")
    public void changefilter() {
        objlstManCCostos2 = new ListModelList<ManCCostos>();
        lst_costo.setModel(getCCostos(objlstManCCostos2));
    }

    public ListModelList<ManCCostos> getCCostos(ListModelList<ManCCostos> u) {
        for (int i = 0; i < objlstManCCostos.getSize(); i++) {
            ManCCostos objCCostos;
            objCCostos = ((ManCCostos) objlstManCCostos.get(i));
            if ((objCCostos.getCosto_des()).toString().contains(txt_busqueda_costos.getValue().toUpperCase().trim())
                    || (objCCostos.getCosto_cod().toString().contains(txt_busqueda_costos.getValue().trim()))) {
                objlstManCCostos2.add(objCCostos);
            }
        }
        return objlstManCCostos2;

    }

    @Listen("onClick=#btn_aceptar")
    public void accept() throws SQLException {
        int a = contar();
        if (a == 1 || a == 0) {
            String cadena = obtenerCode();
            String cadena1 = cadena.replace("','", " ");
            String linea = obtenerDescription();
            String linea1 = linea.replace("','", " ");
            txt_cod_costo.setValue(cadena1.trim());
            txt_des_costo.setValue(linea1);
            txt_costo1.setValue(obtenerCode());

        } else {
            txt_cod_costo.setValue("ALL");
            txt_des_costo.setValue("VARIOS");
            txt_costo1.setValue(obtenerCode());
        }
        if (controlador.equals("ControllerBoletaPago")) {
            ControllerBoletaPago.bandera = false;

        }
		if (controlador.equals("ControllerPatronE")) {
            ControllerPatronE.bandera = false;
        }
        w_lov_ccostos.detach();
    }

    @Listen("onClose=w_lov_ccostos")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerBoletaPago")) {
            ControllerBoletaPago.bandera = false;
        }
		if (controlador.equals("ControllerPatronE")) {
            ControllerPatronE.bandera = false;
        }

    }

    @Listen("onCheck=#chk_selecAll")
    public void selection() {
        for (int i = 0; i < objlstManCCostos.getSize(); i++) {
            objlstManCCostos.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_costo.setModel(objlstManCCostos);
    }

    @Listen("onSeleccion=#lst_costo")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstManCCostos = (ListModelList) lst_costo.getModel();
        if (!objlstManCCostos.isEmpty() || objlstManCCostos != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstManCCostos.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_costo.setModel(objlstManCCostos);
        }
    }

    /**
     * metodo para contar numero de registro
     */
    public int contar() {
        int a = 0;
        for (int i = 0; i < objlstManCCostos.getSize(); i++) {
            if (objlstManCCostos.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }
        return a;
    }

    public String obtenerCode() throws SQLException {
        String cadena = " ";
        //int i = 0;
        for (int j = 0; j < objlstManCCostos.getSize(); j++) {
            if (objlstManCCostos.get(j).isValSelec()) {
                cadena = cadena + objlstManCCostos.get(j).getCosto_cod() + "','";
            }
        }
        return cadena;
    }

    public String obtenerDescription() throws SQLException {
        String cadena = "";
        // int i = 0;
        for (int j = 0; j < objlstManCCostos.getSize(); j++) {
            if (objlstManCCostos.get(j).isValSelec()) {
                cadena = cadena + objlstManCCostos.get(j).getCosto_des() + "','";
            }
        }

        return cadena;

    }

}
