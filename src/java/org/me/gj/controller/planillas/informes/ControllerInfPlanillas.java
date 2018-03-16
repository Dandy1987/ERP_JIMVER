/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.me.gj.controller.planillas.informes.ControllerBoletaPago.bandera;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerInfPlanillas extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_sucursaltotal;
    @Wire
    Textbox txt_periodo, txt_codarea, txt_desarea, txt_codarea1, txt_periodototal, txt_codareatotal, txt_codarea1total, txt_desareatotal;
    @Wire
    Radiogroup rg_periodo, rg_tiporep, rg_order, rg_periodototal, rg_tiporeptotal;
    @Wire
    Checkbox chk_cabecera;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
	@Wire
    Tab tab_planillas, tab_planillastotal;
	@Wire
    Label lbl_periododesc, lbl_periododesc1;
	
	Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos;
    DaoPerPago objdaoPerPago;
    DaoMovLinea objDaoMovLinea;
	InformesMovimiento objMovimiento;
    DaoMovimiento objDaoMovimiento;
	
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    String foco;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ManAreas objArea;
	private static final Logger LOGGER = Logger.getLogger(ControllerInfPlanillas.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoAccesos = new DaoAccesos();
        objdaoPerPago = new DaoPerPago();
		objDaoMovimiento = new DaoMovimiento();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
		cb_sucursaltotal.setModel(objlstSucursal);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
		txt_periodototal.setValue(periodo);
        txt_periodo.setDisabled(true);
		txt_periodototal.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
        rg_tiporep.setSelectedIndex(0);
		rg_periodototal.setSelectedIndex(0);
        rg_tiporeptotal.setSelectedIndex(0);
        rg_order.setSelectedIndex(0);
		objDaoMovLinea = new DaoMovLinea();
		String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);
        String periodo_descrip1 = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc1.setValue(periodo_descrip1);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90311030, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes Planilla con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes Planilla con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Planilla");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Planilla");
        }

    }
	
    @Listen("onOK=#txt_codarea")
    public void busquedaArea() {

        if (bandera == false) {
            bandera = true;
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

    }

    @Listen("onBlur=#txt_codarea")
    public void validaAreas() throws SQLException {
        if (!txt_codarea.getValue().isEmpty()) {
            objArea = new ManAreas();
            if (!txt_codarea.getValue().equals("ALL")) {
                String consulta = txt_codarea.getValue();
                objArea = objDaoMovLinea.consultaArea(consulta);
                if (objArea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codarea.setValue("");
                            txt_desarea.setValue("");
							txt_codarea1.setValue("");
                            txt_codarea.focus();
                        }
                    });
                }
            }
        } else {
            txt_codarea.setValue("");
            txt_desarea.setValue("");
			txt_codarea1.setValue("");

        }
        bandera = false;
    }

    @Listen("onOK=#txt_codareatotal")
    public void busquedaAreaTotal() {

        if (bandera == false) {
            bandera = true;
            if (txt_codareatotal.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_area", txt_codareatotal);
                objMapObjetos.put("des_area", txt_desareatotal);
                objMapObjetos.put("codarea1", txt_codarea1total);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAreas.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    @Listen("onBlur=#txt_codareatotal")
    public void validaAreasTotaltotal() throws SQLException {
        if (!txt_codareatotal.getValue().isEmpty()) {
            objArea = new ManAreas();
            if (!txt_codareatotal.getValue().equals("ALL")) {
                String consulta = txt_codareatotal.getValue();
                objArea = objDaoMovLinea.consultaArea(consulta);
                if (objArea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codareatotal.setValue("");
                            txt_desareatotal.setValue("");
                            txt_codareatotal.focus();
							txt_codarea1total.setValue("");
                        }
                    });
                }
            }
        } else {
            txt_codareatotal.setValue("");
            txt_desareatotal.setValue("");
			txt_codarea1total.setValue("");

        }
        bandera = false;
    }
	
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() throws SQLException {
        String confirm = "";
        String confirmtotal = "";
        String tabla;
        int flag = 0;

        confirm = validateImpresionTotal();
        if (tab_planillas.isSelected()) {
            confirm = validateImpresion();
            if (!confirm.isEmpty()) {
                Messagebox.show(confirm, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            comeFocus();
                        }
                    }
                });
            } else {
                String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
                String per = txt_periodo.getValue();
                int tipo = rg_tiporep.getSelectedIndex();
                boolean chk = chk_cabecera.isChecked();
                if (rg_periodo.getSelectedIndex() == 0) {
                    tabla = "1";//PERIODO ACTUAL
                } else {
                    tabla = "2";//PERIODO ANTERIOR
                }

                String anio = StringFns.substring(txt_periodo.getValue(), 0, 4);
                String mes = StringFns.substring(txt_periodo.getValue(), 4, 6);
                int diauno = Integer.parseInt(anio);
                int diafin = Integer.parseInt(mes);

                int diau = obtenerPrimer(diauno, diafin);
                int diados = obtenerUltimo(diauno, diafin);

                String x = "0" + diau + "/" + mes + "/" + anio;
                String y = diados + "/" + mes + "/" + anio;
                String mes_report = verMes(mes);
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("anio", anio);
                objMapObjetos.put("diauno", x);
                objMapObjetos.put("diados", y);
                objMapObjetos.put("mes", mes_report);
                //para consulta por trabahjador
                objMapObjetos.put("sucursal", sucursal);
                objMapObjetos.put("per", per);
                objMapObjetos.put("tabla", tabla);
                objMapObjetos.put("flag", flag);
                objMapObjetos.put("chk", chk);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("tab", "personal");
                objMapObjetos.put("area", txt_codarea1.getValue());
                objMapObjetos.put("norden", rg_order.getSelectedIndex());
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesPlanilla.zul", null, objMapObjetos);
                window.doModal();

            }
        } else {
            confirmtotal = validateImpresionTotal();
            if (!confirmtotal.isEmpty()) {
                Messagebox.show(confirm, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            comeFocustotal();
                        }
                    }
                });
            } else {
                String sucursal = cb_sucursaltotal.getSelectedIndex() == -1 || cb_sucursaltotal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursaltotal.getSelectedItem().getValue().toString();
                String per = txt_periodototal.getValue();
                int tipo = rg_tiporeptotal.getSelectedIndex();
                if (rg_periodototal.getSelectedIndex() == 0) {
                    tabla = "1";//PERIODO ACTUAL
                } else {
                    tabla = "2";//PERIODO ANTERIOR
                }

                String anio = StringFns.substring(txt_periodototal.getValue(), 0, 4);
                String mes = StringFns.substring(txt_periodototal.getValue(), 4, 6);
                int diauno = Integer.parseInt(anio);
                int diafin = Integer.parseInt(mes);

                int diau = obtenerPrimer(diauno, diafin);
                int diados = obtenerUltimo(diauno, diafin);

                String x = "0" + diau + "/" + mes + "/" + anio;
                String y = diados + "/" + mes + "/" + anio;
                String mes_report = verMes(mes);
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("anio", anio);
                objMapObjetos.put("diauno", x);
                objMapObjetos.put("diados", y);
                objMapObjetos.put("mes", mes_report);
                //para consulta por trabahjador
                objMapObjetos.put("sucursal", sucursal);
                objMapObjetos.put("per", per);
                objMapObjetos.put("tabla", tabla);
                objMapObjetos.put("flag", flag);
                objMapObjetos.put("tab", "total");
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("area", txt_codarea1total.getValue());

                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesPlanilla.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    /**
     * Metodo para seleccion de radiogroup en la pantalla principal
     *
     * @throws java.sql.SQLException
     */
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
    
            @Listen("onBlur=#txt_periodototal")
    public void salirPeriodoTotal() throws SQLException {

        if (!txt_periodototal.getValue().isEmpty()) {
            if (!txt_periodototal.getValue().equals("ALL")) {
                String consulta = txt_periodototal.getValue();
                objMovimiento = objDaoMovimiento.getperiodo(consulta);
                if (objMovimiento == null) {
                    txt_periodototal.setValue("");
                    Messagebox.show("El periodo no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            //txt_periodo.setValue("");
                            txt_periodototal.focus();
                        }
                    });
                } else {
                    txt_periodototal.setValue(objMovimiento.getPeriodo());
                    //txt_periodo1.setValue(objMovimiento.getPeriodo() + "','");
                    lbl_periododesc1.setValue(objdaoPerPago.getPeriodoDescripcion(txt_periodototal.getValue().toString()));
                }
            }
        }

        
    }
	
    @Listen("onClick=#rg_periodototal")
    public void radioSeleccionTotal() throws SQLException {
        if (rg_periodototal.getSelectedIndex() == 0) {
            String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
            txt_periodototal.setValue(periodo);
            txt_periodototal.setDisabled(true);
            String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
            lbl_periododesc1.setValue(periodo_descrip);

        } else {
            txt_periodototal.setValue("");
            txt_periodototal.setDisabled(false);

        }
    }
	
    //Lov en constante principal
    @Listen("onOK=#txt_periodo")
    public void muestraPeriodo() {
        if (bandera == false) {
            bandera = true;
            if (txt_periodo.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("periodo", txt_periodo);
                objMapObjetos.put("controlador", "ControllerBoletaPago");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesPeriodoAnterior.zul", null, objMapObjetos);
                window.doModal();

            }
        }
    }

    @Listen("onOK=#txt_periodototal")
    public void muestraPeriodoTotal() {
        if (bandera == false) {
            bandera = true;
            if (txt_periodototal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("periodo", txt_periodototal);
                objMapObjetos.put("controlador", "ControllerBoletaPago");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesPeriodoAnterior.zul", null, objMapObjetos);
                window.doModal();

            }
        }
    }
	
    public String verMes(String mes) {
        String mes_report = "";
        if (mes.equals("01")) {
            mes_report = "ENERO";
        } else if (mes.equals("02")) {
            mes_report = "FEBRERO";
        } else if (mes.equals("03")) {
            mes_report = "MARZO";
        } else if (mes.equals("04")) {
            mes_report = "ABRIL";
        } else if (mes.equals("05")) {
            mes_report = "MAYO";
        } else if (mes.equals("06")) {
            mes_report = "JUNIO";
        } else if (mes.equals("07")) {
            mes_report = "JULO";
        } else if (mes.equals("08")) {
            mes_report = "AGOSTO";
        } else if (mes.equals("09")) {
            mes_report = "SEPTIEMBRE";
        } else if (mes.equals("10")) {
            mes_report = "OCTUBRE";
        } else if (mes.equals("11")) {
            mes_report = "NOVIEMBRE";
        } else if (mes.equals("12")) {
            mes_report = "DICIEMBRE";
        }

        return mes_report;

    }

    public int obtenerUltimo(int anio, int mes) {

        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1);
        return cal.getActualMaximum(cal.DAY_OF_MONTH);

    }

    public int obtenerPrimer(int anio, int mes) {

        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 1);
        return cal.getActualMinimum(cal.DAY_OF_MONTH);

    }

    public String validateImpresion() {
        String valor = "";
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "Por favor ingresar periodo a mostrar";
            foco = "periodo";
        }
        return valor;
    }

    public String validateImpresionTotal() {
        String valor = "";
        if (txt_periodototal.getValue().isEmpty() || txt_periodototal.getValue().equals("--------")) {
            valor = "Por favor ingresar periodo a mostrar";
            foco = "periodo";
        }
        return valor;
    }
	
    public void comeFocus() {
        if (foco.equals("periodo")) {
            txt_periodo.focus();
        }
    }
	
    public void comeFocustotal() {
        if (foco.equals("periodo")) {
            txt_periodototal.focus();
        }
    }
}
