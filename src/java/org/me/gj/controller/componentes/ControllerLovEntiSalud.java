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
import org.me.gj.model.planillas.mantenimiento.EntiSalud;
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
public class ControllerLovEntiSalud extends SelectorComposer<Component> {

    @Wire
    Window w_lov_entisalud;

    @Wire
    Listbox lst_entisalud;

    @Wire
    Textbox txt_presSaludId, txt_presSaludDes, txt_busqueda_entisalud;

    ListModelList<EntiSalud> objlstEntiSalud;
    ListModelList<EntiSalud> objlstEntiSalud2;
    EntiSalud objEntiSalud;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_presSaludId = (Textbox) parametros.get("plps_id");
        txt_presSaludDes = (Textbox) parametros.get("plps_descri");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_entisalud")
    public void cargarEntiSalud() throws SQLException {
        objlstEntiSalud = objDaoPersonal.busquedaLovEntiSalud();
        lst_entisalud.setModel(objlstEntiSalud);
        lst_entisalud.focus();
        txt_busqueda_entisalud.focus();
    }

    @Listen("onOK=#txt_busqueda_entisalud")
    public void buscarEntiSalud() throws SQLException {
        String consulta = txt_busqueda_entisalud.getValue().toUpperCase();
        objlstEntiSalud.clear();
        objlstEntiSalud = objDaoPersonal.busquedaLovEntiSalud2(consulta);
        lst_entisalud.setModel(objlstEntiSalud);
        
    }

    @Listen("onChange=#txt_busqueda_entisalud")
    public void changefilter() {
        objlstEntiSalud2 = new ListModelList<EntiSalud>();
        lst_entisalud.setModel(getEntiSalud(objlstEntiSalud2));
    }

    public ListModelList<EntiSalud> getEntiSalud(ListModelList<EntiSalud> u) {
        for (int i = 0; i < objlstEntiSalud.getSize(); i++) {
             EntiSalud objEntiSalud;
            objEntiSalud = ((EntiSalud) objlstEntiSalud.get(i));
            if ((objEntiSalud.getEntisalud_des()).toString().contains(txt_busqueda_entisalud.getValue().toUpperCase().trim())
                    || (objEntiSalud.getEntisalud_id().toString().contains(txt_busqueda_entisalud.getValue().trim()))) {
                objlstEntiSalud2.add(objEntiSalud);
            }
        }
        return objlstEntiSalud2;

    }

    @Listen("onOK=#lst_entisalud; onClick=#lst_entisalud")
    public void seleccionaSitu() {
        try {
            objEntiSalud = (EntiSalud) lst_entisalud.getSelectedItem().getValue();
            txt_presSaludId.setValue(objEntiSalud.getEntisalud_id());
            txt_presSaludDes.setValue(objEntiSalud.getEntisalud_des());
            lst_entisalud.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_entisalud.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_entisalud")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
    
    
}
