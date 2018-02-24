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
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.planillas.mantenimiento.DaoContrato;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.mantenimiento.PlantillaContrato;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerInfContratos extends SelectorComposer<Component> {

    @Wire
    Combobox cb_fpering, cb_area;
    @Wire
    Textbox txt_codper, txt_desper, txt_codper1;
    @Wire
    Radiogroup rg_periodo;
    @Wire
	Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Listbox lst_lista;
    @Wire
    Button btn_consultar;
    DaoAccesos objDaoAccesos;
	Accesos objAccesos = new Accesos();
    Personal objPersonal;
    DaoContrato objDaoContrato;
    DaoMovLinea objDaoMovLinea;
    DaoPersonal objDaoPersonal;
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<PlantillaContrato> objlstPlaContraton;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovimiento.class);
    public static boolean bandera = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoMovLinea = new DaoMovLinea();
        objDaoAccesos = new DaoAccesos();
        objDaoPersonal = new DaoPersonal();
        objDaoContrato = new DaoContrato();
        objlstPlaContraton  = new ListModelList<PlantillaContrato>();
        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
        //cb_fpering.setValue(Utilitarios.periodoActual());
        cb_fpering.setModel(new DaoManPer().listaPeriodosDinamico(0));
        rg_periodo.setSelectedIndex(0);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90310000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Contratos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Contratos con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Contratos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Contratos");
        }

    }
	
    /**
     * Metodo para consultar
     */
    @Listen("onClick=#btn_consultar")
    public void consultar() throws SQLException {
        limpiarLista();        
        String personal = txt_codper1.getValue();
        //String x = "('" + personal + "')";//txt_codper.getValue()
        int tipoPeriodo = rg_periodo.getSelectedIndex();
        String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
        String periodo = cb_fpering.getSelectedIndex() == -1 || cb_fpering.getSelectedIndex() == 0 ? "" : cb_fpering.getSelectedItem().getValue().toString();
        objlstPlaContraton = objDaoContrato.consultaContratoInformes(area, personal, periodo, tipoPeriodo);
        if (objlstPlaContraton.isEmpty()) {
            Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {

            lst_lista.setModel(objlstPlaContraton);
        }        
    }

    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_codper")
    public void busquedaPersonalMan() {

        if (bandera == false) {
            bandera = true;
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                objMapObjetos.put("cod", txt_codper1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }
    /*
     * Metodo que se realiza al salir del campo codigo de personal
     * @version 09/08/2017
     * @autor Junior Fernandez
     */

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
     * metodo para impresion por pantalla en Informes
     */
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() {
        if ( objlstPlaContraton.isEmpty() || objlstPlaContraton == null) {
            Messagebox.show("No hay datos a imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMap = new HashMap();
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesContratos.zul", null, objMap);
            window.doModal();
        }
    }

    /**
     * Metodo para limpiar lista principal
     */
    public void limpiarLista() {
        objlstPlaContraton = null;
        objlstPlaContraton = new ListModelList<PlantillaContrato>();
        lst_lista.setModel(objlstPlaContraton);
    }

}
