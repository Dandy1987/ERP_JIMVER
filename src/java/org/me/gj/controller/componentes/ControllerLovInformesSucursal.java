/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.DaoInformesDescuentos;
import org.me.gj.controller.planillas.procesos.ControllerDescuentos;
import org.me.gj.model.planillas.informes.InformesDescuentos;
import org.me.gj.model.planillas.procesos.Descuentos;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovInformesSucursal extends SelectorComposer<Component> {

    @Wire
    Window w_sucursal;
    @Wire
    Textbox descripcion, txt_busqueda,codigo, codigo1;
    @Wire
    Listbox lst_sucursal;
    @Wire
    Checkbox chk_selecAll;
   /* @Wire
    Intbox codigo, codigo1;*/
    Map parametros;
    String controlador;
    ListModelList<InformesDescuentos> objlstDescuentos,objlstDescuentos2;
    Descuentos objDescuentos;
    DaoInformesDescuentos objDaoDescuentos = new DaoInformesDescuentos();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        codigo = (Textbox) parametros.get("sucursal");
        descripcion = (Textbox) parametros.get("dessucursal");
        codigo1 = (Textbox) parametros.get("cod");//transparente
        controlador = (String) parametros.get("controlador");

    }
    
    
    @Listen("onCreate=#w_sucursal")
    public void cargarSucursal() throws SQLException{
        
      objlstDescuentos = objDaoDescuentos.listaSucursal(); 
      lst_sucursal.setModel(objlstDescuentos);
      lst_sucursal.focus();
      txt_busqueda.focus();
        
    }
    
    @Listen("onOK=#txt_busqueda")
    public void buscar() throws SQLException{
        String consulta = txt_busqueda.getValue();
        objlstDescuentos.clear();
        objlstDescuentos = objDaoDescuentos.buscarSeleccion(consulta);
        lst_sucursal.setModel(objlstDescuentos);
        lst_sucursal.focus();
    }
    
    @Listen("onChange=#txt_busqueda")
    public void filtro(){
        objlstDescuentos2 = new ListModelList<InformesDescuentos>();
        lst_sucursal.setModel(getDescuento(objlstDescuentos2));
        
    }
    
    public ListModelList<InformesDescuentos>getDescuento(ListModelList<InformesDescuentos> d){
        for (int i = 0; i < objlstDescuentos.getSize(); i++) {
            InformesDescuentos objDes;
            objDes = ((InformesDescuentos) objlstDescuentos.get(i));
            if (objDes.getDes_sucursal().toString().contains(txt_busqueda.getValue().toUpperCase().trim())) {
                objlstDescuentos2.add(objDes);
            }
            
        }
        return objlstDescuentos2;
        
        
    }
    
     @Listen("onClose=#w_sucursal")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerDescuentos")) {
            ControllerDescuentos.bandera = false;
        }
    }

      //cuando es con check
    @Listen("onCheck=#chk_selecAll")
    public void seleccionarTodo() {
        for (int i = 0; i < objlstDescuentos.getSize(); i++) {
            objlstDescuentos.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_sucursal.setModel(objlstDescuentos);
    }
    
     @Listen("onSeleccion=#lst_sucursal")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstDescuentos = (ListModelList) lst_sucursal.getModel();
        if (!objlstDescuentos.isEmpty() || objlstDescuentos != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstDescuentos.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_sucursal.setModel(objlstDescuentos);
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

        w_sucursal.detach();
    }
      public String obtenerCocigo() throws SQLException {
        String cod = "";
        for (int i = 0; i < objlstDescuentos.getSize(); i++) {
            if (objlstDescuentos.get(i).isValSelec()) {
                cod = cod + objlstDescuentos.get(i).getId_sucursal()+ "','";
            }
        }
        return cod;

    }

    public String obtenerDescripcion() throws SQLException {
        String des = "";
        for (int i = 0; i < objlstDescuentos.getSize(); i++) {
            if (objlstDescuentos.get(i).isValSelec()) {
                des = des + objlstDescuentos.get(i).getDes_sucursal()+ "','";
            }
        }
        return des;
    }

      public int contar() {
        int a = 0;
        for (int i = 0; i < objlstDescuentos.getSize(); i++) {
            if (objlstDescuentos.get(i).isValSelec() == true) {
                a = a + 1;
            }
        }

        return a;

    }
}
