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
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
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
public class ControllerInfAnalisisAfp extends SelectorComposer<Component> {

    @Wire
    Textbox txt_periodo, txt_codper, txt_desper, txt_codper1, txt_codafp, txt_desafp, txt_codafp1;

    @Wire
    Radiogroup rg_periodo;
    @Wire
    Combobox cb_sucursal;

    @Wire
    Datebox db_fechadep;
    @Wire
    Label lbl_periododesc;

    @Wire
    Toolbarbutton tbbtn_btn_imprimir;

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
    private static final Logger LOGGER = Logger.getLogger(ControllerInfAnalisisAfp.class);

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

    @Listen("onOK=#txt_codafp")
    public void muestraAfp() {

        if (txt_codafp.getValue().isEmpty()) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_fp", txt_codafp);
            objMapObjetos.put("des_afp", txt_desafp);
            objMapObjetos.put("cod", txt_codafp1);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAfp.zul", null, objMapObjetos);
            window.doModal();

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
                objMapObjetos.put("tipo", 0);
                objMapObjetos.put("area", "");
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
        if (txt_codper1.getValue().isEmpty()) {
            Messagebox.show("Debe ingresar un código de personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

        } else {
            if (sucu == 0) {
                sucu = 1;
            }
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("nempid", objUsuCredential.getCodemp());
            objMapObjetos.put("nsucuid", sucu);

            objMapObjetos.put("cperiodo", txt_periodo.getValue());
            objMapObjetos.put("ccodemp", txt_codper1.getValue());
            objMapObjetos.put("cafp", txt_codafp1.getValue());

            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesAnalisisAfp.zul", null, objMapObjetos);
            window.doModal();
        }

    }

}
