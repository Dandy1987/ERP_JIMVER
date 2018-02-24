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
import org.me.gj.model.planillas.mantenimiento.ManCargos;
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
public class ControllerLovCargos extends SelectorComposer<Component> {

    @Wire
    Window w_lov_cargos;

    @Wire
    Listbox lst_cargos;

    @Wire
    Textbox txt_cargoId, txt_cargoDes, txt_busqueda_cargos;

    ListModelList<ManCargos> objlstCargos;
    ListModelList<ManCargos> objlstCargos2;
    ManCargos objCargos;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_cargoId = (Textbox) parametros.get("TABLA_ID");
        txt_cargoDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_cargos")
    public void cargarCargos() throws SQLException {
        objlstCargos = objDaoPersonal.busquedaLovCargos();
        lst_cargos.setModel(objlstCargos);
        lst_cargos.focus();
        txt_busqueda_cargos.focus();
    }

    @Listen("onOK=#txt_busqueda_cargos")
    public void buscarAreas() throws SQLException {
        String consulta = txt_busqueda_cargos.getValue().toUpperCase();
        objlstCargos.clear();
        objlstCargos = objDaoPersonal.busquedaLovCargos2(consulta);
        lst_cargos.setModel(objlstCargos);
        
    }

    @Listen("onChange=#txt_busqueda_cargos")
    public void changefilter() {
        objlstCargos2 = new ListModelList<ManCargos>();
        lst_cargos.setModel(getCargos(objlstCargos2));
    }

    public ListModelList<ManCargos> getCargos(ListModelList<ManCargos> u) {
        for (int i = 0; i < objlstCargos.getSize(); i++) {
            ManCargos objCargos;
            objCargos = ((ManCargos) objlstCargos.get(i));
            if ((objCargos.getCargo_des()).toString().contains(txt_busqueda_cargos.getValue().toUpperCase().trim())
                    || (objCargos.getCargo_id().toString().contains(txt_busqueda_cargos.getValue().trim()))) {
                objlstCargos2.add(objCargos);
            }
        }
        return objlstCargos2;

    }

    @Listen("onOK=#lst_cargos; onClick=#lst_cargos")
    public void seleccionaAreas() {
        try {
            objCargos = (ManCargos) lst_cargos.getSelectedItem().getValue();
            txt_cargoId.setValue(objCargos.getCargo_id());
            txt_cargoDes.setValue(objCargos.getCargo_des());
            lst_cargos.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_cargos.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_cargos")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
