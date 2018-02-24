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
import org.me.gj.model.planillas.mantenimiento.HorariosPla;
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
public class ControllerLovHorariosPla extends SelectorComposer<Component> {

    @Wire
    Window w_lov_horarios;

    @Wire
    Listbox lst_horarios;

    @Wire
    Textbox txt_horarioId, txt_horarioDes, txt_busqueda_horarios;

    ListModelList<HorariosPla> objlstHorarios;
    ListModelList<HorariosPla> objlstHorarios2;
    HorariosPla objHorarios;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_horarioId = (Textbox) parametros.get("horario_id");
        txt_horarioDes = (Textbox) parametros.get("hor_descrip");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_horarios")
    public void cargarHorarios() throws SQLException {
        objlstHorarios = objDaoPersonal.busquedaLovHorarios();
        lst_horarios.setModel(objlstHorarios);
        lst_horarios.focus();
        txt_busqueda_horarios.focus();
    }

    @Listen("onOK=#txt_busqueda_horarios")
    public void buscarHorarios() throws SQLException {
        String consulta = txt_busqueda_horarios.getValue().toUpperCase();
        objlstHorarios.clear();
        objlstHorarios = objDaoPersonal.busquedaLovHorarios2(consulta);
        lst_horarios.setModel(objlstHorarios);

    }

    public ListModelList<HorariosPla> getHorarios(ListModelList<HorariosPla> u) {
        for (int i = 0; i < objlstHorarios.getSize(); i++) {
            HorariosPla objHorarios;
            objHorarios = ((HorariosPla) objlstHorarios.get(i));
            if ((objHorarios.getHor_descrip()).toString().contains(txt_busqueda_horarios.getValue().toUpperCase().trim())
                    || (objHorarios.getHorario_id().toString().contains(txt_busqueda_horarios.getValue().trim()))) {
                objlstHorarios2.add(objHorarios);
            }
        }
        return objlstHorarios2;

    }

    @Listen("onChange=#txt_busqueda_horarios")
    public void changefilter() {
        objlstHorarios2 = new ListModelList<HorariosPla>();
        lst_horarios.setModel(getHorarios(objlstHorarios2));
    }

    @Listen("onOK=#lst_horarios; onClick=#lst_horarios")
    public void seleccionaHorarios() {
        try {
            objHorarios = (HorariosPla) lst_horarios.getSelectedItem().getValue();
            txt_horarioId.setValue(objHorarios.getHorario_id());
            txt_horarioDes.setValue(objHorarios.getHor_descrip());
            lst_horarios.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_horarios.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_horarios")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
}
