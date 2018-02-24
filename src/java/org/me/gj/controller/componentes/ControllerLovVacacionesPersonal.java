/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

/**
 *
 * @author cpure
 */
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import org.me.gj.controller.impresion.planillas.procesos.ControllerImpresionProcesosRegAsistencia;
import org.me.gj.controller.planillas.mantenimiento.ControllerManPresPer;

import org.me.gj.controller.planillas.mantenimiento.DaoManPresPer;
import org.me.gj.controller.planillas.procesos.DaoVacaciones;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovVacacionesPersonal extends SelectorComposer<Component> {

    @Wire
    Window w_lov_vacaciones_per;

    @Wire
    Listbox lst_per;

    @Wire
    Textbox txt_idpersonal, txt_despersonal, txt_busqueda_per;

//    ListModelList<ManPresPer> objlstManPresPer;
//    ListModelList<ManPresPer> objlstManPresPer2;
//    ManPresPer objManPresPer;
    ListModelList<Personal> objlstPersonal;
    ListModelList<Personal> objlstPersonal2;
    Personal objPersonal;
    DaoVacaciones objDaoVacaciones;

    DaoManPresPer objDaoManPresPer = new DaoManPresPer();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    String area;
    
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_idpersonal = (Textbox) parametros.get("id_per");
        txt_despersonal = (Textbox) parametros.get("des_per");
        controlador = (String) parametros.get("controlador");
        area = (String) parametros.get("area");
        objlstPersonal = new ListModelList<Personal>();
        objDaoVacaciones = new DaoVacaciones();
    }

    @Listen("onCreate=#w_lov_vacaciones_per")
    public void cargarPersonal() throws SQLException {
        objlstPersonal = objDaoVacaciones.listarPersonal(area);
        lst_per.setModel(objlstPersonal);
        lst_per.focus();
        txt_busqueda_per.focus();

    }

    @Listen("onOK=#txt_busqueda_per")
    public void buscarPersonal() throws SQLException {
        String consulta = txt_busqueda_per.getValue().toUpperCase();
        objlstPersonal.clear();
        objlstPersonal = objDaoVacaciones.listarPersonalFiltro(area,consulta);
        lst_per.setModel(objlstPersonal);

    }

    @Listen("onChange=#txt_busqueda_per")
    public void changefilter() {
        objlstPersonal2 = new ListModelList<Personal>();
        lst_per.setModel(getPersonal(objlstPersonal2));
    }

    public ListModelList<Personal> getPersonal(ListModelList<Personal> u) {
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            Personal objPersonal;
            objPersonal = ((Personal) objlstPersonal.get(i));
            if ((objPersonal.getPlidper()).toString().contains(txt_busqueda_per.getValue().toUpperCase().trim())
                    || (objPersonal.getPldesper().toString().contains(txt_busqueda_per.getValue().trim()))) {
                objlstPersonal2.add(objPersonal);
            }
        }
        return objlstPersonal2;

    }

    @Listen("onOK=#lst_per; onClick=#lst_per")
    public void seleccionaPersonal() {
        try {
            objPersonal = (Personal) lst_per.getSelectedItem().getValue();
            txt_idpersonal.setValue(objPersonal.getPlidper());
            txt_despersonal.setValue(objPersonal.getPldesper());
            lst_per.focus();

            if (controlador.equals("ControllerManPresPer")) {
                ControllerManPresPer.bandera = false;
            } else if (controlador.equals("ControllerImpresionProcesosRegAsistencia")) {
                ControllerImpresionProcesosRegAsistencia.bandera = false;
            }
            w_lov_vacaciones_per.detach();

        } catch (Exception e) {

        }
    }

    @Listen("onClose=#w_lov_per")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerManPresPer")) {
            ControllerManPresPer.bandera = false;
        } else if (controlador.equals("ControllerImpresionProcesosRegAsistencia")) {
            ControllerImpresionProcesosRegAsistencia.bandera = false;

        }
    }
}
