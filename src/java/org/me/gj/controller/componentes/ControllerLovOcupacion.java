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
import org.me.gj.model.planillas.mantenimiento.Ocupacion;
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
public class ControllerLovOcupacion extends SelectorComposer<Component> {

    @Wire
    Window w_lov_ocupacion;

    @Wire
    Listbox lst_ocupacion;

    @Wire
    Textbox txt_ocupaId, txt_ocupaDes, txt_busqueda_ocupacion;

    ListModelList<Ocupacion> objlstOcupacion;
    ListModelList<Ocupacion> objlstOcupacion2;
    Ocupacion objOcupacion;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_ocupaId = (Textbox) parametros.get("PLOCU_ID");
        txt_ocupaDes = (Textbox) parametros.get("PLOCU_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_ocupacion")
    public void cargarOcupacion() throws SQLException {
        objlstOcupacion = objDaoPersonal.busquedaLovOcupacion();
        lst_ocupacion.setModel(objlstOcupacion);
        lst_ocupacion.focus();
        txt_busqueda_ocupacion.focus();
    }

    @Listen("onOK=#txt_busqueda_ocupacion")
    public void buscarOcupacion() throws SQLException {
        String consulta = txt_busqueda_ocupacion.getValue().toUpperCase();
        objlstOcupacion.clear();
        objlstOcupacion = objDaoPersonal.busquedaLovOcupacion2(consulta);
        lst_ocupacion.setModel(objlstOcupacion);
        
    }

    @Listen("onChange=#txt_busqueda_ocupacion")
    public void changefilter() {
        objlstOcupacion2 = new ListModelList<Ocupacion>();
        lst_ocupacion.setModel(getOcup(objlstOcupacion2));
    }

    public ListModelList<Ocupacion> getOcup(ListModelList<Ocupacion> u) {
        for (int i = 0; i < objlstOcupacion.getSize(); i++) {
            Ocupacion objOcupacion;
            objOcupacion = ((Ocupacion) objlstOcupacion.get(i));
            if ((objOcupacion.getOcup_des()).toString().contains(txt_busqueda_ocupacion.getValue().toUpperCase().trim())
                    || (objOcupacion.getOcup_id().toString().contains(txt_busqueda_ocupacion.getValue().trim()))) {
                objlstOcupacion2.add(objOcupacion);
            }
        }
        return objlstOcupacion2;

    }

    @Listen("onOK=#lst_ocupacion; onClick=#lst_ocupacion")
    public void seleccionaOcupacion() {
        try {
            objOcupacion = (Ocupacion) lst_ocupacion.getSelectedItem().getValue();
            txt_ocupaId.setValue(objOcupacion.getOcup_id());
            txt_ocupaDes.setValue(objOcupacion.getOcup_des());
            lst_ocupacion.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }

            w_lov_ocupacion.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_ocupacion")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
