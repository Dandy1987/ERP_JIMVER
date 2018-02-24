/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
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
public class ControllerLovAreas extends SelectorComposer<Component> {

    @Wire
    Window w_lov_areas;

    @Wire
    Listbox lst_areas;

    @Wire
    Textbox txt_areaId, txt_areaDes, txt_busqueda_areas,
            txt_ccostoId, txt_ccostoDes;

    ListModelList<ManAreas> objlstAreas;
    ListModelList<ManAreas> objlstAreas2;
    ManAreas objAreas;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    String ccostoId, ccostoDes;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("AreaCenCos")) {
            txt_areaId = (Textbox) parametros.get("tabla_id");
            txt_areaDes = (Textbox) parametros.get("tabla_descri");
            txt_ccostoId = (Textbox) parametros.get("ccostoId");
            txt_ccostoDes = (Textbox) parametros.get("ccostoDes");
        }

    }

    @Listen("onCreate=#w_lov_areas")
    public void cargarAreas() throws SQLException {

        if (parametros.get("validaBusqueda").equals("AreaCenCos")) {
            objlstAreas = objDaoPersonal.busquedaLovAreas();
        }
        lst_areas.setModel(objlstAreas);
        lst_areas.focus();
        txt_busqueda_areas.focus();
    }

    @Listen("onOK=#txt_busqueda_areas")
    public void buscarAreas() throws SQLException {
        String consulta = txt_busqueda_areas.getValue().toUpperCase();
        if (parametros.get("validaBusqueda").equals("AreaCenCos")) {
            objlstAreas = objDaoPersonal.busquedaLovAreas2(consulta);
        }
        lst_areas.setModel(objlstAreas);

    }

    @Listen("onChange=#txt_busqueda_areas")
    public void changefilter() {
        objlstAreas2 = new ListModelList<ManAreas>();
        lst_areas.setModel(getAreas(objlstAreas2));
    }

    public ListModelList<ManAreas> getAreas(ListModelList<ManAreas> u) {
        for (int i = 0; i < objlstAreas.getSize(); i++) {
            ManAreas objAreas;
            objAreas = ((ManAreas) objlstAreas.get(i));
            if ((objAreas.getArea_des()).toString().contains(txt_busqueda_areas.getValue().toUpperCase().trim())
                    || (objAreas.getArea_des().toString().contains(txt_busqueda_areas.getValue().trim()))) {
                objlstAreas2.add(objAreas);
            }
        }
        return objlstAreas2;

    }

    @Listen("onOK=#lst_areas; onClick=#lst_areas")
    public void seleccionaAreas() {
        try {
            objAreas = (ManAreas) lst_areas.getSelectedItem().getValue();

            if (parametros.get("validaBusqueda").equals("AreaCenCos")) {
                txt_areaId.setValue(objAreas.getArea_id());
                txt_areaDes.setValue(objAreas.getArea_des());
                txt_ccostoId.setValue(objAreas.getArea_cod_costo());
                txt_ccostoDes.setValue(objAreas.getArea_des_costo());
            }

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_areas.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_ccostos")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
}
