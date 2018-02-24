/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerConfPlanilla;
import org.me.gj.controller.planillas.procesos.ControllerMovLinea;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.model.planillas.procesos.Movlinea;
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
public class ControllerLovConstante extends SelectorComposer<Component> {

    @Wire
    Window w_constante;
    @Wire
    Textbox codigo, descripcion, txt_busqueda;
    @Wire
    String tipo;
    @Wire
    Listbox lst_constante;

    Map parametros;
    String controlador;
    ListModelList<Movlinea> objlstMov, objlstMov2;
    Movlinea objMov;
    DaoMovLinea objDaoMov = new DaoMovLinea();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        codigo = (Textbox) parametros.get("codigo");
        descripcion = (Textbox) parametros.get("descripcion");
        tipo = (String) parametros.get("tipo");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_constante")
    public void cargarSituacion() throws SQLException {
        objlstMov = objDaoMov.busquedaConstante(tipo);
        lst_constante.setModel(objlstMov);
        lst_constante.focus();
        txt_busqueda.focus();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        objlstMov.clear();
        objlstMov = objDaoMov.buscarSeleccion(consulta, tipo);
        lst_constante.setModel(objlstMov);
        lst_constante.focus();

    }

    @Listen("onChange=#txt_busqueda")
    public void changeFilter() {
        objlstMov2 = new ListModelList<Movlinea>();
        lst_constante.setModel(getMovimiento(objlstMov2));
    }

    public ListModelList<Movlinea> getMovimiento(ListModelList<Movlinea> u) {
        for (int i = 0; i < objlstMov.getSize(); i++) {
            Movlinea objMovimi;
            objMovimi = ((Movlinea) objlstMov.get(i));
            if ((objMovimi.getId_concepto().contains(txt_busqueda.getValue())
                    || (objMovimi.getDescripcion().toString().contains(txt_busqueda.getValue().toUpperCase().trim())))) {
                objlstMov2.add(objMovimi);
            }

        }
        return objlstMov2;
    }

    @Listen("onOK=#lst_constante;onClick=#lst_constante")
    public void seleccionar() {
        try {
            objMov = (Movlinea) lst_constante.getSelectedItem().getValue();
            codigo.setValue(objMov.getId_concepto());
            descripcion.setValue(objMov.getDescripcion());
            if (controlador.equals("ControllerMovLinea")) {
                ControllerMovLinea.bandera = false;
            }
            if (controlador.equals("ControllerConfPlanilla")) {
                ControllerConfPlanilla.bandera = false;
            }
            
            w_constante.detach();
        } catch (Exception e) {
        }
    }

    @Listen("onClose=#w_constante")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerMovLinea")) {
            ControllerMovLinea.bandera = false;
        }
        if (controlador.equals("ControllerConfPlanilla")) {
            ControllerConfPlanilla.bandera = false;
        }
       
    }
}
