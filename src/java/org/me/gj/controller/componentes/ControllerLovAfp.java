/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerPatronE;
import org.me.gj.controller.planillas.informes.DaoAnalisisAfp;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author cpure
 */
public class ControllerLovAfp extends SelectorComposer<Component> {
    
     @Wire
    Window w_lov_informe_afp;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Listbox lst_afp;

    @Wire
    Button btn_aceptar;

    @Wire
    Textbox txt_codafp, txt_codafp1, txt_desafp, txt_busqueda_afp;

    ListModelList<ConfAfp1> objlstAfp;
    ListModelList<ConfAfp1> objlstAfp2;
    // objAfp;
            

    DaoAnalisisAfp objDaoAfp = new DaoAnalisisAfp();

    Map parametros;
    String controlador;
    String ccostoId, ccostoDes;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
        @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_codafp = (Textbox) parametros.get("id_fp");
        txt_desafp = (Textbox) parametros.get("des_afp");
        txt_codafp1 = (Textbox) parametros.get("cod");
   

    }
    
    
    @Listen("onCreate=#w_lov_informe_afp")
    public void cargarAfp() throws SQLException {
        objlstAfp = new ListModelList<ConfAfp1>();
        objlstAfp = objDaoAfp.lovAfp();

        lst_afp.setModel(objlstAfp);
        lst_afp.focus();
       //(/) txt_busqueda_areas.focus();
    }
    
    @Listen("onOK=#txt_busqueda_afp")
    public void buscarAreas() throws SQLException {
        String consulta = txt_busqueda_afp.getValue().toUpperCase();

        objlstAfp =  objDaoAfp.lovAfpConsulta(consulta);

        lst_afp.setModel(objlstAfp);

    }

    @Listen("onChange=#txt_busqueda_afp")
    public void changefilter() {
        objlstAfp2 = new ListModelList<ConfAfp1>();
        lst_afp.setModel(getAfp(objlstAfp2));
    }

    public ListModelList<ConfAfp1> getAfp(ListModelList<ConfAfp1> u) {
        for (int i = 0; i < objlstAfp.getSize(); i++) {
            ConfAfp1 objAfp;
             objAfp= ((ConfAfp1) objlstAfp.get(i));
            if ((objAfp.getAfp_des()).toString().contains(txt_busqueda_afp.getValue().toUpperCase().trim())
                    || (objAfp.getAfp_des().toString().contains(txt_busqueda_afp.getValue().trim()))) {
                objlstAfp2.add(objAfp);
            }
        }
        return objlstAfp;

    }
    
    
    
     @Listen("onCheck=#chk_selecAll")
    public void seleccionatodo() {
        for (int i = 0; i < objlstAfp.getSize(); i++) {
            objlstAfp.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_afp.setModel(objlstAfp);
    }

    @Listen("onClick=#btn_aceptar")
    public void acepta() throws SQLException {
        int a = contar();
        if (a == 1 || a == 0) {
            //para quitar los caracteres    
            String cadena = obtenerCodigo();
            String cadena1 = cadena.replace("','", " ");
            String linea = obtenerNombre();
            String linea1 = linea.replace("','", " ");
            //  txt_codper_man.setValue(replace(cadena,);
            txt_codafp.setValue(cadena1.trim());
            txt_desafp.setValue(linea1);
            txt_codafp1.setValue(obtenerCodigo());

        } else {
            txt_codafp.setValue("ALL");
            txt_desafp.setValue("VARIOS");
            txt_codafp1.setValue(obtenerCodigo());
            // descri.setValue(obtenerNombre());
        }
       
        w_lov_informe_afp.detach();

    }

    
    
     @Listen("onSeleccion=#lst_afp")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstAfp = (ListModelList) lst_afp.getModel();
        if (!objlstAfp.isEmpty() || objlstAfp != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstAfp.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_afp.setModel(objlstAfp);
        }
    }

    public String obtenerCodigo() throws SQLException {
        String cadena = "";
        // int i = 0;
        for (int j = 0; j < objlstAfp.getSize(); j++) {
            if (objlstAfp.get(j).isValSelec()) {
                cadena = cadena + objlstAfp.get(j).getAfp_id()+ "','";
            }
        }
        return cadena;

    }

    public String obtenerNombre() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstAfp.getSize(); j++) {
            if (objlstAfp.get(j).isValSelec()) {
                cadena = cadena + objlstAfp.get(j).getAfp_des()+ "','";
            }
        }
        return cadena;

    }

    public int contar() {
        int a = 0;
        for (int i = 0; i < objlstAfp.getSize(); i++) {
            if (objlstAfp.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }
}
