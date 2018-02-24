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
import org.me.gj.model.planillas.mantenimiento.NivEducativo;
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
public class ControllerLovNivEducativo extends SelectorComposer<Component> {

    @Wire
    Window w_lov_niveducativo;

    @Wire
    Listbox lst_niveducativo;

    @Wire
    Textbox txt_nivEduId, txt_nivEduDes, txt_busqueda_niveducativo;

    ListModelList<NivEducativo> objlstNivEdu;
    ListModelList<NivEducativo> objlstNivEdu2;
    NivEducativo objNivEdu;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_nivEduId = (Textbox) parametros.get("TABLA_ID");
        txt_nivEduDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_niveducativo")
    public void cargarNivel() throws SQLException {
        objlstNivEdu = objDaoPersonal.busquedaLovNivEdu();
        lst_niveducativo.setModel(objlstNivEdu);
        lst_niveducativo.focus();
        txt_busqueda_niveducativo.focus();
    }

    @Listen("onOK=#txt_busqueda_niveducativo")
    public void buscarNivel() throws SQLException {
        String consulta = txt_busqueda_niveducativo.getValue().toUpperCase();
        objlstNivEdu.clear();
        objlstNivEdu = objDaoPersonal.busquedaLovNivEdu2(consulta);
        lst_niveducativo.setModel(objlstNivEdu);
        
    }

    @Listen("onChange=#txt_busqueda_nacion")
    public void changefilter() {
        objlstNivEdu2 = new ListModelList<NivEducativo>();
        lst_niveducativo.setModel(getNivel(objlstNivEdu2));
    }

    public ListModelList<NivEducativo> getNivel(ListModelList<NivEducativo> u) {
        for (int i = 0; i < objlstNivEdu.getSize(); i++) {
            NivEducativo objNivEdu;
            objNivEdu = ((NivEducativo) objlstNivEdu.get(i));
            if ((objNivEdu.getNivedu_des()).toString().contains(txt_busqueda_niveducativo.getValue().toUpperCase().trim())
                    || (objNivEdu.getNivedu_id().toString().contains(txt_busqueda_niveducativo.getValue().trim()))) {
                objlstNivEdu2.add(objNivEdu);
            }
        }
        return objlstNivEdu2;

    }

    @Listen("onOK=#lst_niveducativo; onClick=#lst_niveducativo")
    public void seleccionaNivel() {
        try {
            objNivEdu = (NivEducativo) lst_niveducativo.getSelectedItem().getValue();
            txt_nivEduId.setValue(objNivEdu.getNivedu_id());
            txt_nivEduDes.setValue(objNivEdu.getNivedu_des());
            lst_niveducativo.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }

            w_lov_niveducativo.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_niveducativo")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
