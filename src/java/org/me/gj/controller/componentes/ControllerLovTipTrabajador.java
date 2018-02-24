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
import org.me.gj.model.planillas.mantenimiento.TipTrabajador;
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
public class ControllerLovTipTrabajador extends SelectorComposer<Component> {
    
    @Wire
    Window w_lov_tiptrab;

    @Wire
    Listbox lst_tibtrab;

    @Wire
    Textbox txt_tipTrabId, txt_tipTrabDes, txt_busqueda_tiptrab;

    ListModelList<TipTrabajador> objlstTipTrabajador;
    ListModelList<TipTrabajador> objlstTipTrabajador2;
    TipTrabajador objTipTrabajador;
    
    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    
     @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_tipTrabId = (Textbox) parametros.get("TABLA_ID");
        txt_tipTrabDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_tiptrab")
    public void cargarTipTrab() throws SQLException {
        objlstTipTrabajador = objDaoPersonal.busquedaLovTipTrab();
        lst_tibtrab.setModel(objlstTipTrabajador);
        lst_tibtrab.focus();
        txt_busqueda_tiptrab.focus();
    }

    @Listen("onOK=#txt_busqueda_tiptrab")
    public void buscarTipTrab() throws SQLException {
        String consulta = txt_busqueda_tiptrab.getValue().toUpperCase();
        objlstTipTrabajador.clear();
        objlstTipTrabajador = objDaoPersonal.busquedaLovTipTrab2(consulta);
        lst_tibtrab.setModel(objlstTipTrabajador);
        
    }

    @Listen("onChange=#txt_busqueda_tiptrab")
    public void changefilter() {
        objlstTipTrabajador2 = new ListModelList<TipTrabajador>();
        lst_tibtrab.setModel(getTipTrab(objlstTipTrabajador2));
    }

    public ListModelList<TipTrabajador> getTipTrab(ListModelList<TipTrabajador> u) {
        for (int i = 0; i < objlstTipTrabajador.getSize(); i++) {
            TipTrabajador objTipTrabajador;
            objTipTrabajador = ((TipTrabajador) objlstTipTrabajador.get(i));
            if ((objTipTrabajador.getTiptrab_des()).toString().contains(txt_busqueda_tiptrab.getValue().toUpperCase().trim())
                    || (objTipTrabajador.getTiptrab_id().toString().contains(txt_busqueda_tiptrab.getValue().trim()))) {
                objlstTipTrabajador2.add(objTipTrabajador);
            }
        }
        return objlstTipTrabajador2;

    }

    @Listen("onOK=#lst_tibtrab; onClick=#lst_tibtrab")
    public void seleccionaTipTrab() {
        try {
            objTipTrabajador = (TipTrabajador) lst_tibtrab.getSelectedItem().getValue();
            txt_tipTrabId.setValue(objTipTrabajador.getTiptrab_id());
            txt_tipTrabDes.setValue(objTipTrabajador.getTiptrab_des());
            lst_tibtrab.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_tiptrab.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_tiptrab")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
}
