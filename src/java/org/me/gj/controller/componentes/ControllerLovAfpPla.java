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
import org.me.gj.model.planillas.mantenimiento.ManAfpsCre;
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
public class ControllerLovAfpPla extends SelectorComposer<Component> {

    @Wire
    Window w_lov_afp;

    @Wire
    Listbox lst_afp;

    @Wire
    Textbox txt_afpId, txt_afpDes, txt_busqueda_afp;

    ListModelList<ManAfpsCre> objlstAfp;
    ListModelList<ManAfpsCre> objlstAfp2;
    ManAfpsCre objAfp;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_afpId = (Textbox) parametros.get("TABLA_ID");
        txt_afpDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_afp")
    public void cargarAfp() throws SQLException {
        objlstAfp = objDaoPersonal.busquedaLovAfp();
        lst_afp.setModel(objlstAfp);
        lst_afp.focus();
        txt_busqueda_afp.focus();
    }

    @Listen("onOK=#txt_busqueda_afp")
    public void buscarAfp() throws SQLException {
        String consulta = txt_busqueda_afp.getValue().toUpperCase();
        objlstAfp.clear();
        objlstAfp = objDaoPersonal.busquedaLovAfp2(consulta);
        lst_afp.setModel(objlstAfp);
        
    }

    @Listen("onChange=#txt_busqueda_afp")
    public void changefilter() {
        objlstAfp2 = new ListModelList<ManAfpsCre>();
        lst_afp.setModel(getAfp(objlstAfp2));
    }

    public ListModelList<ManAfpsCre> getAfp(ListModelList<ManAfpsCre> u) {
        for (int i = 0; i < objlstAfp.getSize(); i++) {
            ManAfpsCre objAfp;
            objAfp = ((ManAfpsCre) objlstAfp.get(i));
            if ((objAfp.getAfp_des()).toString().contains(txt_busqueda_afp.getValue().toUpperCase().trim())
                    || (objAfp.getAfp_id().toString().contains(txt_busqueda_afp.getValue().trim()))) {
                objlstAfp2.add(objAfp);
            }
        }
        return objlstAfp2;

    }
    
    @Listen("onOK=#lst_afp; onClick=#lst_afp")
    public void seleccionaAreas() {
        try {
            objAfp = (ManAfpsCre) lst_afp.getSelectedItem().getValue();
            txt_afpId.setValue(objAfp.getAfp_id());
            txt_afpDes.setValue(objAfp.getAfp_des());
            lst_afp.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_afp.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    
     @Listen("onClose=w_lov_afp")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
