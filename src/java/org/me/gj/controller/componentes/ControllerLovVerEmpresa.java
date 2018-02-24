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
import org.me.gj.model.planillas.mantenimiento.Personal;
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
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovVerEmpresa extends SelectorComposer<Component> {

    @Wire
    Window w_lov_empresa;

    @Wire
    Listbox lst_empresa;
    ListModelList<Personal> objlstPersonal;
   // Personal objPersonalDni;
    DaoPersonal objDaoPersonal = new DaoPersonal();
    Map parametros;
    String controlador;
    String documento;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        documento = (String) parametros.get("documento");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_empresa")
    public void cargarSituacion() throws SQLException {
        objlstPersonal = objDaoPersonal.verEmpresa(documento);
        lst_empresa.setModel(objlstPersonal);
        lst_empresa.focus();

    }

    @Listen("onClose=w_lov_empresa")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
