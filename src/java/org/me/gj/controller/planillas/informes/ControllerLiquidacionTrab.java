/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLiquidacionTrab extends SelectorComposer<Component> {

    @Wire
    Textbox txt_codper, txt_desper;
    @Wire
    Button btn_aceptar;
    Session sesion = Sessions.getCurrent();
    DaoCartaBancos objDaoCarBan = new DaoCartaBancos();
    Personal objPersonal;
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLiquidacionTrab.class);

    public static boolean bandera = false;

    //LOV PARA PERSONAL
    @Listen("onOK=#txt_codper") //ENTER DONDE CARGA EL LOV
    public void buscarPersonalPrincipal() {
        if (bandera == false) {
            bandera = true;
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                objMapObjetos.put("controlador", "ControllerLiquidacionTrab");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_codper")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codper.getValue().isEmpty()) {
            String id = txt_codper.getValue();
            objPersonal = objDaoCarBan.getLovPersonal(id);
            if (objPersonal == null) {
                txt_codper.setValue(null);
                txt_codper.setValue("");
                txt_desper.setValue(null);
                txt_desper.setValue("");
                txt_codper.focus();
            } else {
                txt_codper.setValue(objPersonal.getPlidper());
                txt_desper.setValue(objPersonal.getPldesper());
            }
        } else {
            txt_desper.setValue("");
        }
        bandera = false;
    }
}
