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
import org.me.gj.model.planillas.mantenimiento.TipContrato;
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
public class ControllerLovTipContrato extends SelectorComposer<Component> {

    @Wire
    Window w_lov_tipcont;

    @Wire
    Listbox lst_tipcont;

    @Wire
    Textbox txt_tipContId, txt_tipContDes, txt_busqueda_tipcont;

    ListModelList<TipContrato> objlstTipContrato;
    ListModelList<TipContrato> objlstTipContrato2;
    TipContrato objTipContrato;
    
    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_tipContId = (Textbox) parametros.get("TABLA_ID");
        txt_tipContDes = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_tipcont")
    public void cargarTipCont() throws SQLException {
        objlstTipContrato = objDaoPersonal.busquedaLovTipCont();
        lst_tipcont.setModel(objlstTipContrato);
        lst_tipcont.focus();
        txt_busqueda_tipcont.focus();
    }

    @Listen("onOK=#txt_busqueda_tipcont")
    public void buscarTipCont() throws SQLException {
        String consulta = txt_busqueda_tipcont.getValue().toUpperCase();
        objlstTipContrato.clear();
        objlstTipContrato = objDaoPersonal.busquedaLovTipCont2(consulta);
        lst_tipcont.setModel(objlstTipContrato);
        
    }

    @Listen("onChange=#txt_busqueda_tipcont")
    public void changefilter() {
        objlstTipContrato2 = new ListModelList<TipContrato>();
        lst_tipcont.setModel(getTipCont(objlstTipContrato2));
    }

    public ListModelList<TipContrato> getTipCont(ListModelList<TipContrato> u) {
        for (int i = 0; i < objlstTipContrato.getSize(); i++) {
            TipContrato objTipContrato;
            objTipContrato = ((TipContrato) objlstTipContrato.get(i));
            if ((objTipContrato.getTipcont_des()).toString().contains(txt_busqueda_tipcont.getValue().toUpperCase().trim())
                    || (objTipContrato.getTipcont_id().toString().contains(txt_busqueda_tipcont.getValue().trim()))) {
                objlstTipContrato2.add(objTipContrato);
            }
        }
        return objlstTipContrato2;

    }

    @Listen("onOK=#lst_tipcont; onClick=#lst_tipcont")
    public void seleccionaTipCont() {
        try {
            objTipContrato = (TipContrato) lst_tipcont.getSelectedItem().getValue();
            txt_tipContId.setValue(objTipContrato.getTipcont_id());
            txt_tipContDes.setValue(objTipContrato.getTipcont_des());
            lst_tipcont.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;

            }
            w_lov_tipcont.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_tipcont")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
