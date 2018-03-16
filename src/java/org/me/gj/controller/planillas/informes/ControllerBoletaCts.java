/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.me.gj.controller.planillas.informes.ControllerBoletaPago.bandera;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.Boleta;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author cpure
 */
public class ControllerBoletaCts extends SelectorComposer<Component> {

    @Wire
    Textbox txt_periodo, txt_codper, txt_desper, txt_codper1,
            txt_codarea, txt_desarea, txt_codarea1;
    @Wire
    Radiogroup rg_periodo, rg_tiporep;
    @Wire
    Combobox cb_sucursal;

    @Wire
    Datebox db_fechadep;
	@Wire
    Label lbl_periododesc;

    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    public static boolean bandera = false;
    ListModelList<Personal> objlstPersonal;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    Personal objPersonal;

    InformesMovimiento objMovimiento;
    DaoMovimiento objDaoMovimiento;
    ManAreas objArea;
    DaoManAreas objDaoAreas = new DaoManAreas();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos;
    DaoPerPago objdaoPerPago;
    DaoMovLinea objDaoMovLinea;
    String tipo, foco;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";
    Session sesion = Sessions.getCurrent();
    private static final Logger LOGGER = Logger.getLogger(ControllerBoletaCts.class);

    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoMovimiento = new DaoMovimiento();
        objlstPersonal = new ListModelList<Personal>();
        objDaoAccesos = new DaoAccesos();
        objDaoMovLinea = new DaoMovLinea();
        objdaoPerPago = new DaoPerPago();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
        txt_periodo.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
		String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);

        rg_tiporep.setSelectedIndex(0);

    }

    @Listen("onClick=#rg_periodo")
    public void radioSeleccion() throws SQLException {
        if (rg_periodo.getSelectedIndex() == 0) {
            String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
            txt_periodo.setValue(periodo);
            txt_periodo.setDisabled(true);
			String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
            lbl_periododesc.setValue(periodo_descrip);

        } else {
            txt_periodo.setValue("");
            txt_periodo.setDisabled(false);

        }
    }

    @Listen("onClick=#rg_tiporep")
    public void radioReporte() {
        if (rg_tiporep.getSelectedIndex() == 0) {
            
            txt_codper.setDisabled(false);
        }else{
            
            txt_codper.setDisabled(true);
        }
    }

    @Listen("onOK=#txt_periodo")
    public void muestraPeriodo() {

            if (txt_periodo.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("periodo", txt_periodo);
                objMapObjetos.put("controlador", "ControllerBoletaPago");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesPeriodoAnterior.zul", null, objMapObjetos);
                window.doModal();

            }
        
    }

    @Listen("onBlur=#txt_periodo")
    public void salirPeriodo() throws SQLException {

        if (!txt_periodo.getValue().isEmpty()) {
            if (!txt_periodo.getValue().equals("ALL")) {
                String consulta = txt_periodo.getValue();
                objMovimiento = objDaoMovimiento.getperiodo(consulta);
                if (objMovimiento == null) {
                    txt_periodo.setValue("");
                    Messagebox.show("El periodo no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            //txt_periodo.setValue("");
                            txt_periodo.focus();
                        }
                    });
                } else {
                    txt_periodo.setValue(objMovimiento.getPeriodo());
                    //txt_periodo1.setValue(objMovimiento.getPeriodo() + "','");
					lbl_periododesc.setValue(objdaoPerPago.getPeriodoDescripcion(txt_periodo.getValue().toString()));
                }
            }
        }
    }

    @Listen("onOK=#txt_codarea")
    public void busquedaArea() {

        if (txt_codarea.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_area", txt_codarea);
            objMapObjetos.put("des_area", txt_desarea);
            objMapObjetos.put("codarea1", txt_codarea1);//campo invisible que guarda informacion de personal
            // objMapObjetos.put("des", txt_desper1);
            objMapObjetos.put("controlador", "ControllerMovimiento");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAreas.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    @Listen("onBlur=#txt_codarea")
    public void validaAreas() throws SQLException {
        if (!txt_codarea.getValue().isEmpty()) {

            if (!txt_codarea.getValue().equals("ALL")) {
                String consulta = txt_codarea.getValue();
                objArea = objDaoMovLinea.consultaArea(consulta);
                if (objArea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codarea.setValue("");
                            txt_desarea.setValue("");
                            txt_codarea.focus();
                        }
                    });
                }
            }
        } else {
            txt_codarea.setValue("");
            txt_desarea.setValue("");

        }

    }

    @Listen("onOK=#txt_codper")
    public void enterPersonal() {
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            Messagebox.show("Por favor ingrese un periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

                if (txt_codper.getValue().equals("")) {
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("id_per", txt_codper);
                    objMapObjetos.put("des_per", txt_desper);
                    objMapObjetos.put("cod", txt_codper1);//campo invisible que guarda informacion de personal
                    objMapObjetos.put("sucursal", cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString());
                    objMapObjetos.put("periodo", txt_periodo.getValue());
                    objMapObjetos.put("tipo", rg_tiporep.getSelectedIndex());
                    objMapObjetos.put("area", txt_codarea1.getValue());
                    objMapObjetos.put("controlador", "ControllerBoletaPago");
                    if (rg_periodo.getSelectedIndex() == 1) {
                        objMapObjetos.put("estado", "TODOS");
                    } else {
                        objMapObjetos.put("estado", "ACTIVOS");
                    }
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesBoletaPago.zul", null, objMapObjetos);
                    window.doModal();
                }
        }

    }

    @Listen("onBlur=#txt_codper")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codper.getValue().isEmpty()) {
            if (!txt_codper.getValue().equals("ALL")) {
                String id = txt_codper.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_codper.setValue(null);
                            txt_codper.focus();
                            txt_desper.setValue("");

                        }
                    });

                } else {
                    txt_codper.setValue(objPersonal.getPlidper());
                    txt_desper.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                    txt_codper1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_desper.setValue("");
            txt_codper1.setValue("");
        }

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        int sucu = cb_sucursal.getSelectedItem().getValue();
        if ((db_fechadep.getValue() != null ))   {
            if (sucu == 0) {
                sucu = 1;
            }
            if (txt_codper1.getValue().isEmpty() && !txt_codper.isDisabled()) {
                Messagebox.show("Debe ingresar un código de personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("nempid", objUsuCredential.getCodemp());
                    objMapObjetos.put("nsucuid", sucu);

                    objMapObjetos.put("cperiodo", txt_periodo.getValue());
                    objMapObjetos.put("ccodemp", txt_codper1.getValue());
                    objMapObjetos.put("cfechadeposito", db_fechadep.getValue());
                    if (rg_periodo.getSelectedIndex() == 0) {
                        objMapObjetos.put("tipo", 1);
                    } else {
                        objMapObjetos.put("tipo", 2);
                    }
                    if (rg_tiporep.getSelectedIndex() == 0) {
                    objMapObjetos.put("formato", "detallado");
                    } else{
                        objMapObjetos.put("formato", "resumen"); 
                    }
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesBoletaCts.zul", null, objMapObjetos);
                    window.doModal();
            }
        } else {
            Messagebox.show("Debe ingresar una fecha de depósito", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

    }
	
    public void limpiarPersonal() {
        txt_codper.setValue("");
        txt_codper1.setValue("");
        txt_desper.setValue("");
    }

}
