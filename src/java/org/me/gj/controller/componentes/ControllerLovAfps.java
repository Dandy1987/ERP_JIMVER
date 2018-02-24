/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import org.me.gj.controller.planillas.mantenimiento.ControllerAfpsCre;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoAfpsCre;
import org.me.gj.model.planillas.mantenimiento.RegPensionario;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerLovAfps extends SelectorComposer<Component> {
    
    @Wire
    Window w_lov_reg;
    
    @Wire
    Listbox lst_reg;
    
    @Wire
    Textbox txt_cod_regpen, txt_des_regpen, txt_busqueda, txt_id, txt_des;
    
    ListModelList<RegPensionario> objlstRegPensionario;
    ListModelList<RegPensionario> objlstRegPensionario2;
    RegPensionario objRegPensionario;
    
    DaoAfpsCre objDaoAfpsCre = new DaoAfpsCre();
    
    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (controlador.equals("ControllerAfpsCre")) {
            txt_cod_regpen = (Textbox) parametros.get("TABLA_ID");
            txt_des_regpen = (Textbox) parametros.get("TABLA_DESCRI");
            controlador = (String) parametros.get("controlador");
        } else {
            txt_cod_regpen = (Textbox) parametros.get("TABLA_ID");
            txt_des_regpen = (Textbox) parametros.get("TABLA_DESCRI");
            txt_id = (Textbox) parametros.get("id");
            txt_des = (Textbox) parametros.get("des");
           
            
        }
        
    }
    
    @Listen("onCreate=#w_lov_reg")
    public void cargarUsuario() throws SQLException {
        objlstRegPensionario = objDaoAfpsCre.busquedaLovRegPensionario();
        lst_reg.setModel(objlstRegPensionario);
        lst_reg.focus();
        txt_busqueda.focus();
    }
    
    @Listen("onOK=#txt_busqueda")
    public void buscarRegimen() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        objlstRegPensionario.clear();
        objlstRegPensionario = objDaoAfpsCre.busquedaLovRegPensionario2(consulta);
        lst_reg.setModel(objlstRegPensionario);
        
    }
    
    @Listen("onChange=#txt_busqueda")
    public void changefilter() {
        objlstRegPensionario2 = new ListModelList<RegPensionario>();
        lst_reg.setModel(getRegimen(objlstRegPensionario2));
    }
    
    public ListModelList<RegPensionario> getRegimen(ListModelList<RegPensionario> u) {
        for (int i = 0; i < objlstRegPensionario.getSize(); i++) {
            RegPensionario objRegPensionario;
            objRegPensionario = ((RegPensionario) objlstRegPensionario.get(i));
            if ((objRegPensionario.getReg_des()).toString().contains(txt_busqueda.getValue().toUpperCase().trim())
                    || (objRegPensionario.getReg_id().toString().contains(txt_busqueda.getValue().trim()))) {
                objlstRegPensionario2.add(objRegPensionario);
            }
        }
        return objlstRegPensionario2;
        
    }
    
    @Listen("onOK=#lst_reg; onClick=#lst_reg")
    public void seleccionaRegimen() {
        try {
            objRegPensionario = (RegPensionario) lst_reg.getSelectedItem().getValue();
            if (controlador.equals("ControllerAfpsCre")) {
                txt_cod_regpen.setValue(objRegPensionario.getReg_id());
                txt_des_regpen.setValue(objRegPensionario.getReg_des());
            } else {
                txt_cod_regpen.setValue(objRegPensionario.getReg_id());
                txt_des_regpen.setValue(objRegPensionario.getReg_des());
                txt_id.setValue(objRegPensionario.getId());
                txt_des.setValue(objRegPensionario.getDescr());
                
            }
            
            lst_reg.focus();
            
            if (controlador.equals("ControllerAfpsCre")) {
                ControllerAfpsCre.bandera = false;
                
            }
            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;
                
            }
            w_lov_reg.detach();
            
        } catch (Exception e) {
            //e.printStackTrace();
        }
        
    }
    
    @Listen("onClose=w_lov_reg")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerAfpsCre")) {
            ControllerAfpsCre.bandera = false;
        }
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }
    
}
