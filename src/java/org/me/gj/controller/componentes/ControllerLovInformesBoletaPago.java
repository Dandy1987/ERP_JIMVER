/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerBoletaPago;
import org.me.gj.controller.planillas.informes.ControllerInfUtil;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.model.planillas.mantenimiento.Personal;
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
 * @author achocano
 */
public class ControllerLovInformesBoletaPago extends SelectorComposer<Component> {

    @Wire
    Window w_lov_movimiento;

    @Wire
    Listbox lst_movimiento;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Button btn_aceptar;
    @Wire
    Textbox txt_codper_man, txt_desper_man, txt_busqueda_per,
            cod, descri;
    ListModelList<Personal> objlstPersonal;
    ListModelList<Personal> objlstPersonal2;
    Personal objPersonal;

    DaoMovLinea objDaoManPresPer = new DaoMovLinea();
    String sucursal, periodo, controlador, area,estadotrab;
    Map parametros;

    int tipo;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_codper_man = (Textbox) parametros.get("id_per");
        txt_desper_man = (Textbox) parametros.get("des_per");
        cod = (Textbox) parametros.get("cod");
        sucursal = (String) parametros.get("sucursal");
        periodo = (String) parametros.get("periodo");
        tipo = (Integer) parametros.get("tipo");
        controlador = (String) parametros.get("controlador");
        area = (String) parametros.get("area");
		estadotrab = (String)parametros.get("estado");
    }

    @Listen("onCreate=#w_lov_movimiento")
    public void cargarPersonal() throws SQLException {
     
        String s_aux =area.replace("'","");
        s_aux = area.replace(")", "");
       int sucu;
        if(sucursal.isEmpty()){
            sucu = 0;
        }else{
            sucu = Integer.parseInt(sucursal);
        }
        //Va condiciones para validar el mismo lov pero con diferentes
        //parametros de acuerdo al controlador que usamos
        if (controlador.equals("ControllerInfUtil")) {
            if (tipo == 1) {
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlames(sucursal,"",periodo); 
            }else{
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlanilla(sucursal,"",periodo); 
            }
           
            
        }else if (controlador.equals("ControllerBoletaPago")) {
             if (tipo == 1) {
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlames(sucursal,"",periodo); 
            }else{
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlanilla(sucursal,"",periodo); 
            }
        }
        else{
             objlstPersonal = objDaoManPresPer.buscarPersonaBoleta(sucu, periodo, tipo, s_aux,estadotrab);
        }
       
        lst_movimiento.setModel(objlstPersonal);
        lst_movimiento.focus();
        txt_busqueda_per.focus();

    }

    @Listen("onOK=#txt_busqueda_per")
    public void buscarPersonal() throws SQLException {
        String consulta = txt_busqueda_per.getValue().toUpperCase();
		String s_aux = area.replace("'", "");
        s_aux = area.replace(")", "");
        int sucu;
        if (sucursal.isEmpty()) {
            sucu = 0;
        } else {
            sucu = Integer.parseInt(sucursal);
        }
        objlstPersonal.clear();
        objlstPersonal = objDaoManPresPer.busquedaLovPersonalBoleta(sucu, periodo, tipo, s_aux,consulta);
        lst_movimiento.setModel(objlstPersonal);

    }

    @Listen("onChange=#txt_busqueda_per")
    public void changefilter() {
        objlstPersonal2 = new ListModelList<Personal>();
        lst_movimiento.setModel(getPersonal(objlstPersonal2));
    }

    public ListModelList<Personal> getPersonal(ListModelList<Personal> u) {
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            Personal objPersonal;
            objPersonal = ((Personal) objlstPersonal.get(i));
            if ((objPersonal.getPlidper()).toString().contains(txt_busqueda_per.getValue().toUpperCase().trim())
                    || (objPersonal.getPldesper().toString().contains(txt_busqueda_per.getValue().trim()))) {
                objlstPersonal2.add(objPersonal);
            }
        }
        return objlstPersonal2;

    }

    /*  @Listen("onOK=#lst_movimiento; onClick=#lst_movimiento")
     public void seleccionaPersonal() {
     try {
     objPersonal = (Personal) lst_movimiento.getSelectedItem().getValue();
     txt_codper_man.setValue(objPersonal.getPlidper());
     txt_desper_man.setValue(objPersonal.getPldesper());
     lst_movimiento.focus();

     if (controlador.equals("ControllerMovimiento")) {
     ControllerMovimiento.bandera = false;
     }

     w_lov_movimiento.detach();

     } catch (Exception e) {

     }
     }*/
    @Listen("onClose=#w_lov_movimiento")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerBoletaPago")) {
            ControllerBoletaPago.bandera = false;

        }if (controlador.equals("ControllerInfUtil")) {
                ControllerInfUtil.bandera = false;
        }
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionatodo() {
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            objlstPersonal.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_movimiento.setModel(objlstPersonal);
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
            txt_codper_man.setValue(cadena1.trim());
            txt_desper_man.setValue(linea1);

            cod.setValue(obtenerCodigo());

        } else {
            txt_codper_man.setValue("ALL");
            txt_desper_man.setValue("VARIOS");
            cod.setValue(obtenerCodigo());
            // descri.setValue(obtenerNombre());
        }
        if (controlador.equals("ControllerBoletaPago")) {
            ControllerBoletaPago.bandera = false;

        }
        if (controlador.equals("ControllerInfUtil")) {
                ControllerInfUtil.bandera = false;
            }
        w_lov_movimiento.detach();

    }

    public String obtenerCodigo() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstPersonal.getSize(); j++) {
            if (objlstPersonal.get(j).isValSelec()) {
                cadena = cadena + objlstPersonal.get(j).getPlidper() + "','";
            }
        }
        return cadena;

    }

    public String obtenerNombre() throws SQLException {
        String cadena = "";
        //int i = 0;
        for (int j = 0; j < objlstPersonal.getSize(); j++) {
            if (objlstPersonal.get(j).isValSelec()) {
                cadena = cadena + objlstPersonal.get(j).getPldesper() + "','";
            }
        }
        return cadena;

    }

    @Listen("onSeleccion=#lst_movimiento")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstPersonal = (ListModelList) lst_movimiento.getModel();
        if (!objlstPersonal.isEmpty() || objlstPersonal != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstPersonal.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_movimiento.setModel(objlstPersonal);
        }
    }

    /**
     * Metodo para contar el numero de registros seleccionados
     *
     * @return
     */
    public int contar() {
        int a = 0;
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            if (objlstPersonal.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }
}
