/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerMovimiento;
import org.me.gj.controller.planillas.informes.DaoMovimiento;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
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
public class ControllerLovConstanteMovimiento extends SelectorComposer<Component> {

    @Wire
    Window w_constante;
    @Wire
    Textbox codigo, codigo1, descripcion, txt_busqueda;
    @Wire
    Listbox lst_constante;
    @Wire
    Checkbox chk_selecAll;
    Map parametros;
    String controlador;
    ListModelList<InformesMovimiento> objlstMov, objlstMov2;
    InformesMovimiento objMov;
    DaoMovimiento objDaoMov = new DaoMovimiento();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        codigo = (Textbox) parametros.get("codigo");
        descripcion = (Textbox) parametros.get("descripcion");
        codigo1 = (Textbox) parametros.get("cod");//transparente
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_constante")
    public void cargarSituacion() throws SQLException {
        objlstMov = objDaoMov.busquedaConstante();
        lst_constante.setModel(objlstMov);
        //lst_constante.focus();
        txt_busqueda.focus();
    }

    /*@Listen("onOK=#lst_constante;onClick=#lst_constante")
     public void seleccionar() {
     try {
     objMov = (InformesMovimiento) lst_constante.getSelectedItem().getValue();
     codigo.setValue(objMov.getCod_constante());
     descripcion.setValue(objMov.getDes_constante());
     if (controlador.equals("ControllerMovimiento")) {
     ControllerMovimiento.bandera = false;
     }
     w_constante.detach();
     } catch (Exception e) {
     }
     }*/
    @Listen("onOK=#txt_busqueda")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        objlstMov.clear();
        objlstMov = objDaoMov.buscarSeleccion(consulta);
        lst_constante.setModel(objlstMov);
        lst_constante.focus();

    }

    @Listen("onChange=#txt_busqueda")
    public void changeFilter() {
        objlstMov2 = new ListModelList<InformesMovimiento>();
        lst_constante.setModel(getMovimiento(objlstMov2));
    }

    public ListModelList<InformesMovimiento> getMovimiento(ListModelList<InformesMovimiento> u) {
        for (int i = 0; i < objlstMov.getSize(); i++) {
            InformesMovimiento objMovimi;
            objMovimi = ((InformesMovimiento) objlstMov.get(i));
            if ((objMovimi.getCod_constante().contains(txt_busqueda.getValue())
                    || (objMovimi.getDes_constante().toString().contains(txt_busqueda.getValue().toUpperCase().trim())))) {
                objlstMov2.add(objMovimi);
            }

        }
        return objlstMov2;
    }

    @Listen("onClose=#w_constante")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerMovimiento")) {
            ControllerMovimiento.bandera = false;
        }
    }

    //cuando es con check
    @Listen("onCheck=#chk_selecAll")
    public void seleccionarTodo() {
        for (int i = 0; i < objlstMov.getSize(); i++) {
            objlstMov.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_constante.setModel(objlstMov);
    }

    @Listen("onSeleccion=#lst_constante")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstMov = (ListModelList) lst_constante.getModel();
        if (!objlstMov.isEmpty() || objlstMov != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstMov.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_constante.setModel(objlstMov);
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void acepta() throws SQLException {

        int x = contar();
        if (x == 1 || x == 0) {
            String cadena = obtenerCocigo();
            String cadena1 = cadena.replace("','", " ");
            String linea = obtenerDescripcion();
            String linea1 = linea.replace("','", " ");
            codigo.setValue(cadena1.trim());
            descripcion.setValue(linea1);
            codigo1.setValue(obtenerCocigo());
        } else {
            codigo.setValue("ALL");
            descripcion.setValue("VARIOS");
            codigo1.setValue(obtenerCocigo());
        }

        w_constante.detach();
    }

    public String obtenerCocigo() throws SQLException {
        String cod = "";
        for (int i = 0; i < objlstMov.getSize(); i++) {
            if (objlstMov.get(i).isValSelec()) {
                cod = cod + objlstMov.get(i).getCod_constante() + "','";
            }
        }
        return cod;

    }

    public String obtenerDescripcion() throws SQLException {
        String des = "";
        for (int i = 0; i < objlstMov.getSize(); i++) {
            if (objlstMov.get(i).isValSelec()) {
                des = des + objlstMov.get(i).getDes_constante() + "','";
            }
        }
        return des;
    }

    public int contar() {
        int a = 0;
        for (int i = 0; i < objlstMov.getSize(); i++) {
            if (objlstMov.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }
}
