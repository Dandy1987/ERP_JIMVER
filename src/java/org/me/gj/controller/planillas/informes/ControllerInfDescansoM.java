/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerUbicaciones;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerInfDescansoM extends SelectorComposer<Component> {

    @Wire
	Combobox cb_tipodescanso;
    @Wire
    Textbox txt_codigo, txt_codigo1, txt_apenom, txt_periodo;
	@Wire
    Toolbarbutton tbbtn_btn_imprimir;
    DaoMovLinea objDaoMovLinea;
    Personal objPersonal;
	DaoAccesos objDaoAccesos;
    Accesos objAccesos = new Accesos();
    String campo;
    public static boolean bandera = false;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUbicaciones.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoMovLinea = new DaoMovLinea();
        txt_periodo.focus();
		cb_tipodescanso.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90304000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Descansos Medicos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Descansos Medicos con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Descansos Medicos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Descansos Medicos");
        }

    }
	
    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_codigo")
    public void buscarPersonalPrincipal() {

        if (bandera == false) {
            bandera = true;
            if (txt_codigo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codigo);
                objMapObjetos.put("des_per", txt_apenom);
                objMapObjetos.put("cod", txt_codigo1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerInfDescansoM");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //Salir de lov para el filtro
    @Listen("onBlur=#txt_codigo")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codigo.getValue().isEmpty()) {
            if (!txt_codigo.getValue().equals("ALL")) {
                String id = txt_codigo.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_codigo.setValue(null);
                            txt_codigo.focus();
                            txt_apenom.setValue("");

                        }
                    });

                } else {
                    txt_codigo.setValue(objPersonal.getPlidper());
                    txt_apenom.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                    txt_codigo1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_apenom.setValue("");
            txt_codigo1.setValue("");
        }

        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        String valida;
        valida = verificar();
        if (!valida.equals("")) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {

                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("anio")) {
                            txt_periodo.focus();
                        }
                    }

                }
            });

        } else {
            String periodo = txt_periodo.getValue();
            String personal = txt_codigo1.getValue();
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getCodemp());
            objMapObjetos.put("periodo", periodo);
            objMapObjetos.put("ccodemp", personal);
			objMapObjetos.put("tipodescanso",cb_tipodescanso.getValue());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionDMedico.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    public String verificar() {
        String mensaje;
        if (txt_periodo.getValue().isEmpty()) {
            mensaje = "Por favor ingresar año para imprimir";
            campo = "anio";
        } else {
            mensaje = "";
        }
        return mensaje;
    }

}
