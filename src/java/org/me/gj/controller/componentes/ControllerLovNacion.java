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
import org.me.gj.model.planillas.mantenimiento.Nacion;
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
public class ControllerLovNacion extends SelectorComposer<Component> {

    @Wire
    Window w_lov_nacion;

    @Wire
    Listbox lst_nacion;

    @Wire
    Textbox txt_nacionId, txt_nacionDes, txt_busqueda_nacion;

    ListModelList<Nacion> objlstNacion;
    ListModelList<Nacion> objlstNacion2;

    Nacion objNacion;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_nacionId = (Textbox) parametros.get("TABLA_ID");
        txt_nacionDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_nacion")
    public void cargarNacion() throws SQLException {
        objlstNacion = objDaoPersonal.busquedaLovNacion();
        lst_nacion.setModel(objlstNacion);
        lst_nacion.focus();
        txt_busqueda_nacion.focus();
    }

    @Listen("onOK=#txt_busqueda_nacion")
    public void buscarNacion() throws SQLException {
        String consulta = txt_busqueda_nacion.getValue().toUpperCase();
        objlstNacion.clear();
        objlstNacion = objDaoPersonal.busquedaLovNacion2(consulta);
        lst_nacion.setModel(objlstNacion);
        
    }

    @Listen("onChange=#txt_busqueda_nacion")
    public void changefilter() {
        objlstNacion2 = new ListModelList<Nacion>();
        lst_nacion.setModel(getNacion(objlstNacion2));
    }

    public ListModelList<Nacion> getNacion(ListModelList<Nacion> u) {
        for (int i = 0; i < objlstNacion.getSize(); i++) {
            Nacion objNacion;
            objNacion = ((Nacion) objlstNacion.get(i));
            if ((objNacion.getNacion_des()).toString().contains(txt_busqueda_nacion.getValue().toUpperCase().trim())
                    || (objNacion.getNacion_id().toString().contains(txt_busqueda_nacion.getValue().trim()))) {
                objlstNacion2.add(objNacion);
            }
        }
        return objlstNacion2;

    }

    @Listen("onOK=#lst_nacion; onClick=#lst_nacion")
    public void seleccionaNacion() {
        try {
            objNacion = (Nacion) lst_nacion.getSelectedItem().getValue();
            txt_nacionId.setValue(objNacion.getNacion_id());
            txt_nacionDes.setValue(objNacion.getNacion_des());
            lst_nacion.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }

            w_lov_nacion.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_nacion")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
