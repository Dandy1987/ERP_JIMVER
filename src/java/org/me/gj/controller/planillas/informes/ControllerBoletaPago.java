/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerBoletaPago extends SelectorComposer<Component> {

    @Wire
    Textbox txt_periodo, txt_codper, txt_desper, txt_codper1,
            txt_costo, txt_descripcioncosto, txt_costo1, txt_codarea, txt_desarea, txt_codarea1;

    @Wire
    Radiogroup rg_periodo, gb, rg_tiporep, rg_order;
    @Wire
    Combobox cb_sucursal;
    @Wire
    Button btn_aceptar;
    @Wire
    Radiogroup chk_tipo;
    @Wire
    Groupbox g_personal, g_costo;
	@Wire
    Toolbarbutton tbbtn_btn_imprimir;
    public static boolean bandera = false;
    ListModelList<Personal> objlstPersonal;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    Personal objPersonal;
    ListModelList<Boleta> objlstBoleta, objlstLista;
    ManCCostos objCCostos;
    InformesMovimiento objMovimiento;
    DaoMovimiento objDaoMovimiento;
    ManAreas objArea;
    DaoManAreas objDaoAreas = new DaoManAreas();
    DaoBoletaPago objDaoBoleta;
    DaoPersonal objDaoPersonal = new DaoPersonal();
	Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos;
    DaoPerPago objdaoPerPago;
    DaoMovLinea objDaoMovLinea;
    String tipo, foco;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";
    Session sesion = Sessions.getCurrent();
	private static final Logger LOGGER = Logger.getLogger(ControllerBoletaPago.class);
	
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    //SimpleDateFormat sda = new SimpleDateFormat("");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstPersonal = new ListModelList<Personal>();
        objDaoAccesos = new DaoAccesos();
        objDaoMovLinea = new DaoMovLinea();
        objdaoPerPago = new DaoPerPago();
        objDaoBoleta = new DaoBoletaPago();
        objDaoMovimiento = new DaoMovimiento();
        objlstBoleta = new ListModelList<Boleta>();
        objlstLista = new ListModelList<Boleta>();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
        txt_periodo.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
        rg_order.setSelectedIndex(0);
        chk_tipo.setSelectedIndex(0);
        rg_tiporep.setSelectedIndex(0);
        g_personal.setVisible(true);
		
    }

     @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90311010, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Boleta de pago con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Boleta de pago con el rol: USUARIO NORMAL");
        }
       
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Boleta de pago");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Boleta de pago");
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
        bandera = false;
    }

    public void limpiarCosto() {
        txt_costo.setValue("");
        txt_costo1.setValue("");
        txt_descripcioncosto.setValue("");
    }

    public void limpiarPersonal() {
        txt_codper.setValue("");
        txt_codper1.setValue("");
        txt_desper.setValue("");
    }

    @Listen("onClick=#chk_tipo")
    public void seleccionTipo() {
        if (chk_tipo.getSelectedItem().getValue().equals("1")) {
            g_personal.setVisible(true);
            g_costo.setVisible(false);
            limpiarCosto();
        } else {
            g_costo.setVisible(true);
            g_personal.setVisible(false);
            limpiarPersonal();
        }

    }

    //metodo lov para centro de costos
    @Listen("onOK=#txt_costo")
    public void lovCosto() {
        if (bandera == false) {
            bandera = true;
            if (txt_costo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("costo", txt_costo);
                objMapObjetos.put("descripcion", txt_descripcioncosto);
                objMapObjetos.put("costo1", txt_costo1);//campo invisible que guarda informacion de personal
                objMapObjetos.put("controlador", "ControllerBoletaPago");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCostoMultiple.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onOK=#txt_codper")
    public void enterPersonal() {
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            Messagebox.show("Por favor ingrese un periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

            if (bandera == false) {
                bandera = true;
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
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesBoletaPago.zul", null, objMapObjetos);
                    window.doModal();
                }
            }
        }

    }

    //blur para costos
    //Salir de lov para el filtro
    @Listen("onBlur=#txt_costo")
    public void valida_CostoPrincipal() throws SQLException {
        if (!txt_costo.getValue().isEmpty()) {
            if (!txt_costo.getValue().toUpperCase().equals("JIM")) {

                if (!txt_costo.getValue().equals("ALL")) {
                    String id = txt_costo.getValue();
                    objCCostos = objDaoAreas.getLovCCostos(id);
                    //  objPersonal = objDaoMovLinea.getLovPersonal(id);
                    if (objCCostos == null) {
                        Messagebox.show("El codigo de centro de costo no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                            public void onEvent(Event t) throws Exception {
                                txt_costo.setValue(null);
                                txt_costo.focus();
                                txt_descripcioncosto.setValue("");

                            }
                        });

                    } else {
                        txt_costo.setValue(objCCostos.getCosto_cod());
                        txt_descripcioncosto.setValue(objCCostos.getCosto_des());

                        txt_costo1.setValue(objCCostos.getCosto_cod() + "','");
                    }
                }
            }
        } else {
            txt_descripcioncosto.setValue("");
            txt_costo1.setValue("");
        }

        bandera = false;
    }

    //Salir de lov para el filtro
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

        bandera = false;
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

        } else {
            txt_periodo.setValue("");
            txt_periodo.setDisabled(false);

        }
    }

    public void registro(int flag) throws SQLException {
        String tabla = "";
        String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
        String personal = txt_codper1.getValue();
        String periodo = txt_periodo.getValue();
        String costo = txt_costo1.getValue().trim();

        if (rg_periodo.getSelectedIndex() == 0) {
            tabla = "tplplames";
        } else {
            tabla = "tplplanilla";
        }

        if (flag == 1) {

            objlstBoleta = objDaoBoleta.consultaBoleta(sucursal, personal, tabla, periodo, costo);
        } else if (flag == 2) {
            objlstBoleta = objDaoBoleta.consultaCostos(sucursal, tabla, periodo, costo);
        } else {
            objlstBoleta = objDaoBoleta.consultaJim(sucursal, tabla, periodo);
        }
        //para orden
        if (rg_order.getSelectedIndex() == 0) {
            Collections.sort(objlstBoleta, new Comparator<Boleta>() {

                public int compare(Boleta o1, Boleta o2) {
                    return o1.getCargo().compareTo(o2.getCargo());

                }
            });
        } else if (rg_order.getSelectedIndex() == 1) {
            Collections.sort(objlstBoleta, new Comparator<Boleta>() {

                public int compare(Boleta o1, Boleta o2) {
                    return o1.getApenom().compareTo(o2.getApenom());
                }

            });
        } else if (rg_order.getSelectedIndex() == 2) {
            Collections.sort(objlstBoleta, new Comparator<Boleta>() {

                public int compare(Boleta o1, Boleta o2) {
                    return o1.getCodigo().compareTo(o2.getCodigo());

                }

            });
        }
        rg_order.setSelectedIndex(-1);

    }

    //lov para periodo
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
                }
            }
        }

        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() throws SQLException {
        String confirm = "";
        int flag = 0;
        if (chk_tipo.getSelectedItem().getValue().equals("1") && !txt_costo.getValue().toUpperCase().equals("JIM")) {
            confirm = validateImpression();
            flag = 1;
        } else if (chk_tipo.getSelectedItem().getValue().equals("2") && !txt_costo.getValue().toUpperCase().equals("JIM")) {
            confirm = validateCostos();
            flag = 2;
        } else {
            confirm = "";
            flag = 3;
        }

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
            registro(flag);
            if (objlstBoleta == null || objlstBoleta.isEmpty()) {
                Messagebox.show("No hay registros en el periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String tabla = "";
                String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
                String personal = txt_codper1.getValue();
                String per = txt_periodo.getValue();
                String costo = txt_costo1.getValue();

                if (rg_periodo.getSelectedIndex() == 0) {
                    tabla = "tplplames";
                } else {
                    tabla = "tplplanilla";
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
                objMapObjetos.put("lista", objlstBoleta);
                //para consulta por trabahjador
                objMapObjetos.put("sucursal", sucursal);
                objMapObjetos.put("personal", personal);
                objMapObjetos.put("per", per);
                objMapObjetos.put("tabla", tabla);
                objMapObjetos.put("costo", costo);
                objMapObjetos.put("flag", flag);
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesBoleta.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    public String validateImpression() {
        String valor = "";
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "Por favor ingresar periodo a mostrar";
            foco = "periodo";
        } else if (txt_codper.getValue().isEmpty()) {
            valor = "Por favor ingresar personal a mostrar";
            foco = "personal";
        }
        return valor;
    }

    public String validateCostos() {
        String valor = "";
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "Por favor ingresar periodo a mostrar";
            foco = "periodo";
        } else if (txt_costo.getValue().isEmpty()) {
            valor = "Por favor ingresar centro de costo";
            foco = "costo";
        }
        return valor;
    }

    public void comeFocus() {
        if (foco.equals("periodo")) {
            txt_periodo.focus();
        } else if (foco.equals("personal")) {
            txt_codper.focus();
        } else if (foco.equals("costo")) {
            txt_costo.focus();
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

}
