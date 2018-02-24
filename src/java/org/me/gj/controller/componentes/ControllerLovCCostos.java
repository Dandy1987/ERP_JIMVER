/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerDescuentos;
import org.me.gj.controller.planillas.mantenimiento.ControllerManAreas;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerLovCCostos extends SelectorComposer<Component> {

    @Wire
    Window w_lov_ccostos;

    @Wire
    Listbox lst_costo;

    @Wire
    Textbox txt_cod_costo, txt_des_costo, txt_busqueda_costos;

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
        txt_cod_costo = (Textbox) parametros.get("id");
        txt_des_costo = (Textbox) parametros.get("cc_descri");
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

    @Listen("onOK=#lst_costo; onClick=#lst_costo")
    public void seleccionaCCostos() {
        try {
            objCCostos = (ManCCostos) lst_costo.getSelectedItem().getValue();
            txt_cod_costo.setValue(objCCostos.getCosto_cod());
            txt_des_costo.setValue(objCCostos.getCosto_des());
            lst_costo.focus();

            if (controlador.equals("ControllerManAreas")) {
                ControllerManAreas.bandera = false;

            }

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }if(controlador.equals("ControllerDescuentos")){
                ControllerDescuentos.bandera = false;
            }
            w_lov_ccostos.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_ccostos")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerManAreas")) {
            ControllerManAreas.bandera = false;
        }
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
}
