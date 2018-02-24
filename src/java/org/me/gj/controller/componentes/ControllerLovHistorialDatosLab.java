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
import org.me.gj.model.planillas.mantenimiento.DatosLaborales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerLovHistorialDatosLab extends SelectorComposer<Component> {

    @Wire
    Window w_lov_historial_dl;

    @Wire
    Listbox lst_historial_dl;
    
    @Wire
    Label lbl_personal;        

    ListModelList<DatosLaborales> objlstDatosLab;
    DatosLaborales objDatosLab;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String tipdoc, personal, controlador;
    String numdoc;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        tipdoc = (String) parametros.get("tipdoc");
        numdoc = (String) parametros.get("numdoc");
        personal = (String) parametros.get("personal");
        objlstDatosLab = objDaoPersonal.listaDatosLab(Integer.parseInt(tipdoc), numdoc);
        lbl_personal.setValue(personal);
        lst_historial_dl.setModel(objlstDatosLab);
    }

    @Listen("onClick=#btn_salir")
    public void botonSalir() throws SQLException {

        //if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        //}
        w_lov_historial_dl.detach();
    }

}
