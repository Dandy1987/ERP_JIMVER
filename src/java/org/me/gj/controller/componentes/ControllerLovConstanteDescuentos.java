/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.procesos.ControllerDescuentos;
import org.me.gj.controller.planillas.procesos.ControllerMovLinea;
import org.me.gj.controller.planillas.procesos.DaoDescuentos;
import org.me.gj.model.planillas.procesos.Descuentos;
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
public class ControllerLovConstanteDescuentos extends SelectorComposer<Component> {

    @Wire
    Window w_constante;
    @Wire
    Textbox codigo, descripcion, txt_busqueda;
    @Wire
    Listbox lst_constante;

    Map parametros;
    String controlador;
    ListModelList<Descuentos> objlstMov, objlstMov2;
    Descuentos objMov;
    DaoDescuentos objDaoMov = new DaoDescuentos();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        codigo = (Textbox) parametros.get("codigo");
        descripcion = (Textbox) parametros.get("descripcion");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_constante")
    public void cargarSituacion() throws SQLException {
        objlstMov = objDaoMov.busquedaConstanteDescuentos();
        lst_constante.setModel(objlstMov);
        lst_constante.focus();
        txt_busqueda.focus();
    }

    @Listen("onOK=#lst_constante;onClick=#lst_constante")
    public void seleccionar() {
        try {
            objMov = (Descuentos) lst_constante.getSelectedItem().getValue();
            codigo.setValue(objMov.getCod_constante());
            descripcion.setValue(objMov.getDes_constante());
            if (controlador.equals("ControllerDescuentos")) {
                ControllerDescuentos.bandera = false;
            }
             if (controlador.equals("ControllerLovDescuentosBloque")) {
            ControllerLovDescuentosBloque.bandera = false;
        }
            w_constante.detach();
        } catch (Exception e) {
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        objlstMov.clear();
        objlstMov = objDaoMov.buscarSeleccionDescuentos(consulta);
        lst_constante.setModel(objlstMov);
        lst_constante.focus();

    }

    /*@Listen("onChange=#txt_busqueda")
    public void changeFilter() {
        objlstMov2 = new ListModelList<Descuentos>();
        lst_constante.setModel(getMovimiento(objlstMov2));
    }

    public ListModelList<Descuentos> getMovimiento(ListModelList<Descuentos> u) {
        for (int i = 0; i < objlstMov.getSize(); i++) {
            Descuentos objMovimi;
            objMovimi = ((Descuentos) objlstMov.get(i));
            if ((objMovimi.getCod_constante().contains(txt_busqueda.getValue())
                    || (objMovimi.getDes_constante().toString().contains(txt_busqueda.getValue().toUpperCase().trim())))) {
                objlstMov2.add(objMovimi);
            }

        }
        return objlstMov2;
    }*/
    
     @Listen("onClose=#w_constante")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerDescuentos")) {
            ControllerMovLinea.bandera = false;
        }
         if (controlador.equals("ControllerLovDescuentosBloque")) {
            ControllerLovDescuentosBloque.bandera = false;
        }
    }

}
