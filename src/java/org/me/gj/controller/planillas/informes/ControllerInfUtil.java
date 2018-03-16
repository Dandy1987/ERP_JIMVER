/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;

import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.Boleta;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
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
public class ControllerInfUtil extends SelectorComposer<Component> {

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
    Datebox db_fechapu, db_fechapdj;
    @Wire
    Groupbox g_personal, g_costo;
    @Wire
    Label lbl_periododesc;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    public static boolean bandera = false;
    String tipo, foco;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";
    Session sesion = Sessions.getCurrent();
    private static final Logger LOGGER = Logger.getLogger(ControllerInfUtil.class);
    DaoMovimiento objDaoMovimiento;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<Personal> objlstPersonal = new ListModelList<Personal>();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    DaoPerPago objdaoPerPago;
    DaoAccesos objDaoAccesos;
    InformesMovimiento objMovimiento;
    DaoMovLinea objDaoMovLinea;
    Personal objPersonal;
    DaoMovLinea objDaoManPresPer = new DaoMovLinea();
    Accesos objAccesos = new Accesos();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    //SimpleDateFormat sda = new SimpleDateFormat("");
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objdaoPerPago = new DaoPerPago();
        objDaoAccesos = new DaoAccesos();
        objDaoMovLinea = new DaoMovLinea();
        objDaoMovimiento = new DaoMovimiento();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());

        txt_periodo.setValue(periodo);
        txt_periodo.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
        String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);
        db_fechapdj.setValue(Utilitarios.hoyAsFecha());
        db_fechapu.setValue(Utilitarios.hoyAsFecha());

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

    /*   @Listen("onOK=#txt_codper")
     public void busquedaPersonalMan() {

     if (bandera == false) {
     bandera = true;
     if (txt_codper.getValue().equals("")) {
     Map objMapObjetos = new HashMap();
     objMapObjetos.put("id_per", txt_codper);
     objMapObjetos.put("des_per", txt_desper);
     objMapObjetos.put("controlador", "ControllerInfUtil");
     Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
     window.doModal();
     }
     }

     }*/
    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_codper")
    public void buscarPersonalPrincipal() {
        int perio;
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            Messagebox.show("Por favor ingrese un periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (rg_periodo.getSelectedIndex() == 0) {
                perio = 1;//PERIODO ACTUAL

            } else {
                perio = 2;//PERIODO ANTERIOR

            }
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                objMapObjetos.put("cod", txt_codper1);//campo invisible que guarda informacion de personal
                objMapObjetos.put("sucursal", cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString());
                objMapObjetos.put("periodo", txt_periodo.getValue());
                objMapObjetos.put("tipo", perio);
                objMapObjetos.put("area", "TODOS");
                objMapObjetos.put("controlador", "ControllerInfUtil");
                if (rg_periodo.getSelectedIndex() == 1) {
                    objMapObjetos.put("estado", "TODOS");
                } else {
                    objMapObjetos.put("estado", "TODOS");
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

    //lov para periodo
    //Lov en constante principal
    @Listen("onOK=#txt_periodo")
    public void muestraPeriodo() {
        if (bandera == false) {
            bandera = true;
            if (txt_periodo.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("periodo", txt_periodo);
                objMapObjetos.put("controlador", "ControllerInfUtil");
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
                    lbl_periododesc.setValue(objdaoPerPago.getPeriodoDescripcion(txt_periodo.getValue().toString()));
                }
            }
        }

        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() throws SQLException {

        String tabla;
        if (txt_codper1.getValue().isEmpty() && rg_tiporep.getSelectedIndex() == 0) {
            Messagebox.show("Debe ingresar un código de personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
            String per = txt_periodo.getValue();
            int tipo = rg_tiporep.getSelectedIndex();
            String personal = txt_codper1.getValue();
            String x = sdf.format(db_fechapdj.getValue());
            String y = sdf.format(db_fechapu.getValue());
            if (rg_periodo.getSelectedIndex() == 0) {
                tabla = "1";//PERIODO ACTUAL
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlames(sucursal, personal, per);
            } else {
                tabla = "2";//PERIODO ANTERIOR
                objlstPersonal = objDaoManPresPer.buscarPersonalInformesPlanilla(sucursal, personal, per);
            }

            // objlstPersonal = objDaoManPresPer.buscarPersonalInformes(sucursal, personal, per);
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("EMPRESA", objUsuCredential.getCodemp());
            objMapObjetos.put("SUCURSAL", sucursal);
            objMapObjetos.put("PERIODO", per);
            objMapObjetos.put("EMPLEADO", txt_codper1.getValue());// txt_codper1.getValue()
            objMapObjetos.put("FECHAI", x);
            objMapObjetos.put("FECHAF", y);
            objMapObjetos.put("tabla", tabla);
            objMapObjetos.put("lista", objlstPersonal);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionUtilidades.zul", null, objMapObjetos);
            window.doModal();

        }
    }

}
