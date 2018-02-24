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
import org.me.gj.model.planillas.mantenimiento.UbigeoPla;
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
public class ControllerLovUbigeoPla extends SelectorComposer<Component> {

    @Wire
    Window w_lov_ubigeopla;

    @Wire
    Listbox lst_ubigeopla;

    @Wire
    Textbox txt_ubigeoId, txt_ubigeoDes, txt_busqueda_ubigeo;

    ListModelList<UbigeoPla> objlstUbigeoPla;
    ListModelList<UbigeoPla> objlstUbigeoPla2;
    UbigeoPla objUbigeoPla;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_ubigeoId = (Textbox) parametros.get("ubi_id");
        txt_ubigeoDes = (Textbox) parametros.get("ubi_des");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_ubigeopla")
    public void cargarUbigeoPla() throws SQLException {
        objlstUbigeoPla = objDaoPersonal.busquedaLovUbigeoPla();
        lst_ubigeopla.setModel(objlstUbigeoPla);
        lst_ubigeopla.focus();
        txt_busqueda_ubigeo.focus();
    }

    @Listen("onOK=#txt_busqueda_ubigeo")
    public void buscarUbigeoPla() throws SQLException {
        String consulta = txt_busqueda_ubigeo.getValue().toUpperCase();
        objlstUbigeoPla.clear();
        objlstUbigeoPla = objDaoPersonal.busquedaLovUbigeoPla2(consulta);
        lst_ubigeopla.setModel(objlstUbigeoPla);
        
    }

    @Listen("onChange=#txt_busqueda_ubigeo")
    public void changefilter() {
        objlstUbigeoPla2 = new ListModelList<UbigeoPla>();
        lst_ubigeopla.setModel(getUbigeoPla(objlstUbigeoPla2));
    }

    public ListModelList<UbigeoPla> getUbigeoPla(ListModelList<UbigeoPla> u) {
        for (int i = 0; i < objlstUbigeoPla.getSize(); i++) {
            UbigeoPla objUbigeoPla;
            objUbigeoPla = ((UbigeoPla) objlstUbigeoPla.get(i));
            if ((objUbigeoPla.getUbi_des()).toString().contains(txt_busqueda_ubigeo.getValue().toUpperCase().trim())
                    || (objUbigeoPla.getUbi_id().toString().contains(txt_busqueda_ubigeo.getValue().trim()))) {
                objlstUbigeoPla2.add(objUbigeoPla);
            }
        }
        return objlstUbigeoPla2;

    }

    @Listen("onOK=#lst_ubigeopla; onClick=#lst_ubigeopla")
    public void seleccionaUbigeoPla() {
        try {
            objUbigeoPla = (UbigeoPla) lst_ubigeopla.getSelectedItem().getValue();
            txt_ubigeoId.setValue(objUbigeoPla.getUbi_id());
            txt_ubigeoDes.setValue(objUbigeoPla.getUbi_des());
            lst_ubigeopla.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_ubigeopla.detach();

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
