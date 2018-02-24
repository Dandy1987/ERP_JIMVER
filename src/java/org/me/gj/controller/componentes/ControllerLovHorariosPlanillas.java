/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerManHorarios;
import org.me.gj.controller.planillas.mantenimiento.DaoManHorarios;
import org.me.gj.model.planillas.mantenimiento.ManHorarios;
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
 * @author achocano
 */
public class ControllerLovHorariosPlanillas extends SelectorComposer<Component> {

    @Wire
    Window w_lov_horarios;
    @Wire
    Listbox lst_buscaHorario;
    @Wire
    Textbox txt_busqueda, txt_hro_sem, txt_desde, txt_hasta, txt_descanso;
    ListModelList<ManHorarios> objlstHorarios = new ListModelList<ManHorarios>();
    ListModelList<ManHorarios> objlstHoarios2;
    ManHorarios objHorarios = new ManHorarios();
    DaoManHorarios objDaoHorarios = new DaoManHorarios();
    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    int empresa = objUsuCredential.getCodemp();

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_hro_sem = (Textbox) parametros.get("txt_hro_sem");
        txt_desde = (Textbox) parametros.get("txt_desde");
        txt_hasta = (Textbox) parametros.get("txt_hasta");
        txt_descanso = (Textbox) parametros.get("txt_descanso");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_horarios")
    public void cargarBanco() throws SQLException {
        objlstHorarios = objDaoHorarios.listaLovHorario(empresa);
        lst_buscaHorario.setModel(objlstHorarios);
        lst_buscaHorario.focus();
        txt_busqueda.focus();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscaBanco() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        objlstHorarios.clear();
        objlstHorarios = objDaoHorarios.buscaInicio(empresa,consulta);
        lst_buscaHorario.setModel(objlstHorarios);
        lst_buscaHorario.focus();
    }

    @Listen("onOK=#lst_buscaHorario;onClick=#lst_buscaHorario")
    public void seleccionaRegistro() {
        try {
            objHorarios = (ManHorarios) lst_buscaHorario.getSelectedItem().getValue();
            txt_hro_sem.setValue(objHorarios.getMan_codigo_semanal());
            txt_desde.setValue(objHorarios.getMan_de());
            txt_hasta.setValue(objHorarios.getMan_a());
            txt_descanso.setValue(objHorarios.getMan_descanso());
            if (controlador.equals("ControllerManHorarios")) {
                ControllerManHorarios.bandera = false;
            }
            w_lov_horarios.detach();
            

        } catch (Exception e) {
        }
    }

    @Listen("onClose=#w_lov_horarios")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerManHorarios")) {
            ControllerManHorarios.bandera = false;
        }
    }
}
