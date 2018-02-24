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
import org.me.gj.model.planillas.mantenimiento.Situacion;
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
public class ControllerLovSituacion extends SelectorComposer<Component> {

    @Wire
    Window w_lov_situacion;

    @Wire
    Listbox lst_situacion;

    @Wire
    Textbox txt_sitEpsId, txt_sitEpsDes, txt_busqueda_situ;

    ListModelList<Situacion> objlstSituacion;
    ListModelList<Situacion> objlstSituacion2;
    Situacion objSituacion;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_sitEpsId = (Textbox) parametros.get("TABLA_ID");
        txt_sitEpsDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_situacion")
    public void cargarSituacion() throws SQLException {
        objlstSituacion = objDaoPersonal.busquedaLovSituacion();
        lst_situacion.setModel(objlstSituacion);
        lst_situacion.focus();
        txt_busqueda_situ.focus();
    }

    @Listen("onOK=#txt_busqueda_situ")
    public void buscarSituacion() throws SQLException {
        String consulta = txt_busqueda_situ.getValue().toUpperCase();
        objlstSituacion.clear();
        objlstSituacion = objDaoPersonal.busquedaLovSituacion2(consulta);
        lst_situacion.setModel(objlstSituacion);
        
    }

    @Listen("onChange=#txt_busqueda_situ")
    public void changefilter() {
        objlstSituacion2 = new ListModelList<Situacion>();
        lst_situacion.setModel(getSitu(objlstSituacion2));
    }

    public ListModelList<Situacion> getSitu(ListModelList<Situacion> u) {
        for (int i = 0; i < objlstSituacion.getSize(); i++) {
             Situacion objSituacion;
            objSituacion = ((Situacion) objlstSituacion.get(i));
            if ((objSituacion.getSitu_des()).toString().contains(txt_busqueda_situ.getValue().toUpperCase().trim())
                    || (objSituacion.getSitu_des().toString().contains(txt_busqueda_situ.getValue().trim()))) {
                objlstSituacion2.add(objSituacion);
            }
        }
        return objlstSituacion2;

    }

    @Listen("onOK=#lst_situacion; onClick=#lst_situacion")
    public void seleccionaSitu() {
        try {
            objSituacion = (Situacion) lst_situacion.getSelectedItem().getValue();
            txt_sitEpsId.setValue(objSituacion.getSitu_id());
            txt_sitEpsDes.setValue(objSituacion.getSitu_des());
            lst_situacion.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_situacion.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_situacion")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
