/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerConfAfps;
import org.me.gj.controller.planillas.mantenimiento.DaoConfAfps;
import org.me.gj.model.planillas.mantenimiento.ConfAfp2;
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
public class ControllerLovConAfp2 extends SelectorComposer<Component> {

    @Wire
    Window w_lov_Conf2;

    @Wire
    Listbox lst_Conf2;

    @Wire
    Textbox txt_comdes, txt_com_destino, txt_comori, txt_com_origen, txt_apori, txt_apodes, txt_destino, txt_origen, txt_busqueda_2,
            txt_priori, txt_prides, txt_pri_origen, txt_pri_destino, txt_mixori, txt_mixdes, txt_mix_origen, txt_mix_destino;

    ListModelList<ConfAfp2> objlstConfAfp2;
    ListModelList<ConfAfp2> objlstConfAfp2_2;
    ConfAfp2 objConfAfp2;

    DaoConfAfps objDaoConfAfps = new DaoConfAfps();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_apori = (Textbox) parametros.get("TABLA_ID");
        txt_origen = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");
/*
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_apodes = (Textbox) parametros.get("TABLA_ID");
        txt_destino = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_comori = (Textbox) parametros.get("TABLA_ID");
        txt_com_origen = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_comdes = (Textbox) parametros.get("TABLA_ID");
        txt_com_destino = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_priori = (Textbox) parametros.get("TABLA_ID");
        txt_pri_origen = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_prides = (Textbox) parametros.get("TABLA_ID");
        txt_pri_destino = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_mixori = (Textbox) parametros.get("TABLA_ID");
        txt_mix_origen = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");

        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_mixdes = (Textbox) parametros.get("TABLA_ID");
        txt_mix_destino = (Textbox) parametros.get("TABLA_DESCRI");
        controlador = (String) parametros.get("controlador");*/

    }

    @Listen("onCreate=#w_lov_Conf2")
    public void cargarAfp_2() throws SQLException {
        objlstConfAfp2 = objDaoConfAfps.busquedaLovConf2();
        lst_Conf2.setModel(objlstConfAfp2);
        lst_Conf2.focus();
        txt_busqueda_2.focus();

    }

    @Listen("onOK=#txt_busqueda_2")
    public void buscarRegimen_2() throws SQLException {
        String consulta = txt_busqueda_2.getValue().toUpperCase();
        objlstConfAfp2.clear();
        objlstConfAfp2 = objDaoConfAfps.busquedaLovConf2_2(consulta);
        lst_Conf2.setModel(objlstConfAfp2);
        
    }

    @Listen("onChange=#txt_busqueda_2")
    public void changefilter_2() {
        objlstConfAfp2_2 = new ListModelList<ConfAfp2>();
        lst_Conf2.setModel(getConf2(objlstConfAfp2_2));
    }

    public ListModelList<ConfAfp2> getConf2(ListModelList<ConfAfp2> u) {
        for (int i = 0; i < objlstConfAfp2.getSize(); i++) {
            ConfAfp2 objConfAfp2;
            objConfAfp2 = ((ConfAfp2) objlstConfAfp2.get(i));
            if ((objConfAfp2.getConcep_des()).toString().contains(txt_busqueda_2.getValue().toUpperCase().trim())
                    || (objConfAfp2.getConcep_id().toString().contains(txt_busqueda_2.getValue().trim()))) {
                objlstConfAfp2_2.add(objConfAfp2);
            }
        }
        return objlstConfAfp2_2;

    }

    //txt_apori
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen1() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_apori.setValue(objConfAfp2.getConcep_id());
            txt_origen.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    //txt_apodes
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen2() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_apodes.setValue(objConfAfp2.getConcep_id());
            txt_destino.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    // txt_comori
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen3() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_comori.setValue(objConfAfp2.getConcep_id());
            txt_com_origen.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    // txt_comdes
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen4() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_comdes.setValue(objConfAfp2.getConcep_id());
            txt_com_destino.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    
    
    // txt_priori
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen5() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_priori.setValue(objConfAfp2.getConcep_id());
            txt_pri_origen.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    
     // txt_prides
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen6() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_prides.setValue(objConfAfp2.getConcep_id());
            txt_pri_destino.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    
    
    
    // txt_mixori
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen7() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_mixori.setValue(objConfAfp2.getConcep_id());
            txt_mix_origen.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    
     // txt_mixdes
    @Listen("onOK=#lst_Conf2; onClick=#lst_Conf2")
    public void seleccionaRegimen8() {
        try {
            objConfAfp2 = (ConfAfp2) lst_Conf2.getSelectedItem().getValue();
            txt_mixdes.setValue(objConfAfp2.getConcep_id());
            txt_mix_destino.setValue(objConfAfp2.getConcep_des());
            lst_Conf2.focus();

            if (controlador.equals("ControllerConfAfps")) {
                ControllerConfAfps.bandera = false;

            }
            w_lov_Conf2.detach();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Listen("onClose=w_lov_Conf2")
    public void cerrarVentana_2() throws SQLException {
        if (controlador.equals("ControllerConfAfps")) {
            ControllerConfAfps.bandera = false;
        }
    }

}
