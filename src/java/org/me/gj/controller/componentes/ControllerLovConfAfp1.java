/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerConfAfps;
import org.me.gj.controller.planillas.mantenimiento.DaoConfAfps;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
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
public class ControllerLovConfAfp1 extends SelectorComposer<Component> {

    @Wire
    Window w_lov_Conf1;

    @Wire
    Listbox lst_Conf1;

    @Wire
    Textbox txt_afpid, txt_afpdes, txt_busqueda_1;
    
    ListModelList<ConfAfp1> objlstConfAfp1;
    ListModelList<ConfAfp1> objlstConfAfp1_2;
    ConfAfp1 objConfAfp1;
    
    DaoConfAfps objDaoConfAfps = new DaoConfAfps();
    
    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_afpid = (Textbox) parametros.get("TABLA_ID");
        txt_afpdes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }
    @Listen("onCreate=#w_lov_Conf1")
    public void cargarAfp() throws SQLException {
        objlstConfAfp1 = objDaoConfAfps.busquedaLovConf1();
        lst_Conf1.setModel(objlstConfAfp1);
        lst_Conf1.focus();
        txt_busqueda_1.focus();
    }
    @Listen("onOK=#txt_busqueda_1")
    public void buscarRegimen() throws SQLException {
        String consulta = txt_busqueda_1.getValue().toUpperCase();
        objlstConfAfp1.clear();
        objlstConfAfp1 = objDaoConfAfps.busquedaLovConf1_2(consulta);
        lst_Conf1.setModel(objlstConfAfp1);
        
    }
@Listen("onChange=#txt_busqueda_1")
    public void changefilter() {
        objlstConfAfp1_2 = new ListModelList<ConfAfp1>();
        lst_Conf1.setModel(getConf1(objlstConfAfp1_2));
    }
    
     public ListModelList<ConfAfp1> getConf1(ListModelList<ConfAfp1> u) {
        for (int i = 0; i < objlstConfAfp1.getSize(); i++) {
            ConfAfp1 objConfAfp1;
            objConfAfp1 = ((ConfAfp1) objlstConfAfp1.get(i));
            if ((objConfAfp1.getAfp_des()).toString().contains(txt_busqueda_1.getValue().toUpperCase().trim())
                    || (objConfAfp1.getAfp_id().toString().contains(txt_busqueda_1.getValue().trim()))) {
                objlstConfAfp1_2.add(objConfAfp1);
            }
        }
        return objlstConfAfp1_2;

    }
     
     @Listen("onOK=#lst_Conf1; onClick=#lst_Conf1")
    public void seleccionaRegimen() {
        try {
            objConfAfp1 = (ConfAfp1) lst_Conf1.getSelectedItem().getValue();
            txt_afpid.setValue(objConfAfp1.getAfp_id());
            txt_afpdes.setValue(objConfAfp1.getAfp_des());
            lst_Conf1.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf1.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_Conf1")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerConfAfps")) {
            ControllerConfAfps.bandera = false;
        }
    }
}
