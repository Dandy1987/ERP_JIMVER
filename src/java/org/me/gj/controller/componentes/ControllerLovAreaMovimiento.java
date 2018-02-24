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
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
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
public class ControllerLovAreaMovimiento extends SelectorComposer<Component> {

    @Wire
    Window w_lov_informe_areas;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Listbox lst_areas;

    @Wire
    Button btn_aceptar;

    @Wire
    Textbox txt_areaId, txt_areaDes, txt_busqueda_areas, txt_codarea1;

    ListModelList<ManAreas> objlstAreas;
    ListModelList<ManAreas> objlstAreas2;
    ManAreas objAreas;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    String ccostoId, ccostoDes;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_areaId = (Textbox) parametros.get("id_area");
        txt_areaDes = (Textbox) parametros.get("des_area");
        txt_codarea1 = (Textbox) parametros.get("codarea1");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_informe_areas")
    public void cargarAreas() throws SQLException {

        objlstAreas = objDaoPersonal.busquedaLovAreas();

        lst_areas.setModel(objlstAreas);
        lst_areas.focus();
        txt_busqueda_areas.focus();
    }

    @Listen("onOK=#txt_busqueda_areas")
    public void buscarAreas() throws SQLException {
        String consulta = txt_busqueda_areas.getValue().toUpperCase();

        objlstAreas = objDaoPersonal.busquedaLovAreas2(consulta);

        lst_areas.setModel(objlstAreas);

    }

    @Listen("onChange=#txt_busqueda_areas")
    public void changefilter() {
        objlstAreas2 = new ListModelList<ManAreas>();
        lst_areas.setModel(getAreas(objlstAreas2));
    }

    public ListModelList<ManAreas> getAreas(ListModelList<ManAreas> u) {
        for (int i = 0; i < objlstAreas.getSize(); i++) {
            ManAreas objAreas;
            objAreas = ((ManAreas) objlstAreas.get(i));
            if ((objAreas.getArea_des()).toString().contains(txt_busqueda_areas.getValue().toUpperCase().trim())
                    || (objAreas.getArea_des().toString().contains(txt_busqueda_areas.getValue().trim()))) {
                objlstAreas2.add(objAreas);
            }
        }
        return objlstAreas2;

    }

    /* @Listen("onOK=#lst_areas; onClick=#lst_areas")
     public void seleccionaAreas() {
     try {
     objAreas = (ManAreas) lst_areas.getSelectedItem().getValue();

     txt_areaId.setValue(objAreas.getArea_id());
     txt_areaDes.setValue(objAreas.getArea_des());

     ControllerPersonal.bandera = false;

     w_lov_informe_areas.detach();

     } catch (Exception e) {
     //e.printStackTrace();
     }

     }*/
    @Listen("onClose=w_lov_informe_areas")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPatronE")) {
            ControllerPatronE.bandera = false;
        } else if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionatodo() {
        for (int i = 0; i < objlstAreas.getSize(); i++) {
            objlstAreas.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_areas.setModel(objlstAreas);
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
            txt_areaId.setValue(cadena1.trim());
            txt_areaDes.setValue(linea1);
            txt_codarea1.setValue(obtenerCodigo());

        } else {
            txt_areaId.setValue("ALL");
            txt_areaDes.setValue("VARIOS");
            txt_codarea1.setValue(obtenerCodigo());
            // descri.setValue(obtenerNombre());
        }
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;

        }
        if (controlador.equals("ControllerPatronE")) {
            ControllerPatronE.bandera = false;
        }
        w_lov_informe_areas.detach();

    }

    @Listen("onSeleccion=#lst_areas")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstAreas = (ListModelList) lst_areas.getModel();
        if (!objlstAreas.isEmpty() || objlstAreas != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstAreas.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_areas.setModel(objlstAreas);
        }
    }

    public String obtenerCodigo() throws SQLException {
        String cadena = "";
        // int i = 0;
        for (int j = 0; j < objlstAreas.getSize(); j++) {
            if (objlstAreas.get(j).isValSelec()) {
                cadena = cadena + objlstAreas.get(j).getArea_id() + "','";
            }
        }
        return cadena;

    }

    public String obtenerNombre() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstAreas.getSize(); j++) {
            if (objlstAreas.get(j).isValSelec()) {
                cadena = cadena + objlstAreas.get(j).getArea_des() + "','";
            }
        }
        return cadena;

    }

    public int contar() {
        int a = 0;
        for (int i = 0; i < objlstAreas.getSize(); i++) {
            if (objlstAreas.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }

}
