/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesDescuentos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerDescuentos extends SelectorComposer<Component> {

    @Wire
    Groupbox g_legal, g_fecha, g_personal, g_costo;
    @Wire
    Tab tab_listadescuentos;
    @Wire
    Listbox lst_descuentos;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar,
            tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Radiogroup rg_descripcion, rg_orden;
    @Wire
    Datebox d_fdesde, d_fhasta, d_cdesde, d_chasta;
    /*@Wire
     Intbox txt_lidsucursal1,txt_lidsucursal;*/
    @Wire
    Textbox txt_lidsucursal1, txt_lidsucursal, txt_ldessucursal, txt_lidpersonal, txt_ldespersonal, txt_lidpersonal1,//legal
            txt_fidsucursal1, txt_fidsucursal, txt_fdessucursal,//fecha
            txt_pidsucursal1, txt_pidpersonal1, txt_pdespersonal, txt_pidpersonal, txt_pidsucursal, txt_pdessucursal,//personal
            txt_cidsucursal1, txt_ccosto, txt_cdessucursal, txt_cidsucursal, txt_cdescosto, txt_codarea, txt_desarea, txt_codarea1;
    @Wire
    Button btn_lconsultar, btn_pconsultar, btn_fconsultar, btn_cconsultar;
    @Wire
    Combobox cb_periodo;
	String periodo;
    DaoPerPago objdaoPerPago;
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();
    ListModelList<InformesDescuentos> objlstDescuentos = new ListModelList<InformesDescuentos>();
    Personal objPersonal;
    DaoMovLinea objDaoMovLinea;
	Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    ManCCostos objCCostos;
    DaoManAreas objDaoAreas = new DaoManAreas();
    InformesDescuentos objDescuentos;
    DaoInformesDescuentos objDaoDescuentos;
    ManAreas objArea;
    public static boolean bandera = false;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static final Logger LOGGER = Logger.getLogger(ControllerDescuentos.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoMovLinea = new DaoMovLinea();
        objdaoPerPago = new DaoPerPago();
        objDaoDescuentos = new DaoInformesDescuentos();
        objlstPerPago = objdaoPerPago.busquedaPeriodoCerrado();
        cb_periodo.setModel(objlstPerPago);
		objPersonal = new Personal();
        rg_descripcion.setSelectedIndex(0);
        g_legal.setVisible(true);
		
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90302000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Descuentos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Descuentos con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Descuentos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Descuentos");
        }

    }
	
    /**
     * Metodos generales
     */
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

    @Listen("onClick=#rg_descripcion")
    public void seleccionRadio() {

        if (rg_descripcion.getSelectedIndex() == 0) {
            g_legal.setVisible(true);
            //los demas se bloquean
            g_fecha.setVisible(false);
            g_personal.setVisible(false);
            g_costo.setVisible(false);
            limpiaCamposFecha();
            limpiaCamposPersona();
            limpiaCamposCosto();
            limpiarLista();
        } else if (rg_descripcion.getSelectedIndex() == 1) {
            g_fecha.setVisible(true);
            //los demas se bloquean
            g_legal.setVisible(false);
            g_personal.setVisible(false);
            g_costo.setVisible(false);
            limpiaCamposLegal();
            limpiaCamposPersona();
            limpiaCamposCosto();
            limpiarLista();
        } else if (rg_descripcion.getSelectedIndex() == 2) {
            g_personal.setVisible(true);
            //los demas se bloquean
            g_fecha.setVisible(false);
            g_legal.setVisible(false);
            g_costo.setVisible(false);
            limpiaCamposLegal();
            limpiaCamposFecha();
            limpiaCamposCosto();
            limpiarLista();
        } else if (rg_descripcion.getSelectedIndex() == 3) {
            g_costo.setVisible(true);
            //los demas se bloquean
            g_fecha.setVisible(false);
            g_legal.setVisible(false);
            g_personal.setVisible(false);
            limpiaCamposLegal();
            limpiaCamposFecha();
            limpiaCamposPersona();
            limpiarLista();
        }

    }

    /**
     * Metodo muestra lov de personal para legal,persona
     *
     * @version 28/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_lidpersonal")
    public void busquedaPersonalLegal() {

        if (bandera == false) {
            bandera = true;
            if (txt_lidpersonal.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_lidpersonal);
                objMapObjetos.put("des_per", txt_ldespersonal);
                objMapObjetos.put("cod", txt_lidpersonal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);

                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //personal
    @Listen("onOK=#txt_pidpersonal")
    public void busquedaPersonal() {

        if (bandera == false) {
            bandera = true;
            if (txt_pidpersonal.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_pidpersonal);
                objMapObjetos.put("des_per", txt_pdespersonal);
                objMapObjetos.put("cod", txt_pidpersonal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("area", txt_codarea1.getValue());
                objMapObjetos.put("tipo", 0);
                objMapObjetos.put("sucursal", txt_pidsucursal1.getValue());
                if (cb_periodo.getValue().toString().isEmpty()) {
                    periodo = "TODOS";
                } else {
                    periodo = cb_periodo.getValue().toString();
                }
                objMapObjetos.put("periodo", periodo);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesBoletaPago.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }
    /*
     * Metodo que se realiza al salir del campo codigo de personal
     * @version 28/08/2017
     * @autor Junior Fernandez
     */

    //Salir de lov para el filtro legal
    @Listen("onBlur=#txt_lidpersonal")
    public void valida_PersonalLegal() throws SQLException {
        if (!txt_lidpersonal.getValue().isEmpty()) {
            if (!txt_lidpersonal.getValue().equals("ALL")) {
                String id = txt_lidpersonal.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_lidpersonal.setValue(null);
                            txt_lidpersonal.focus();
                            txt_ldespersonal.setValue("");
                        }
                    });
                } else {
                    txt_lidpersonal.setValue(objPersonal.getPlidper());
                    txt_ldespersonal.setValue(objPersonal.getPldesper());
                    txt_lidpersonal1.setValue(objPersonal.getPlidper() + "','");
                }
            }
        } else {
            txt_ldespersonal.setValue("");
            txt_lidpersonal1.setValue("");
        }

        bandera = false;
    }

    //personal
    //Salir de lov para el filtro
    @Listen("onBlur=#txt_pidpersonal")
    public void valida_Personal() throws SQLException {
        if (!txt_pidpersonal.getValue().isEmpty()) {
            if (!txt_pidpersonal.getValue().equals("ALL")) {
                String id = txt_pidpersonal.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_pidpersonal.setValue(null);
                            txt_pidpersonal.focus();
                            txt_pdespersonal.setValue("");

                        }
                    });

                } else {
                    txt_pidpersonal.setValue(objPersonal.getPlidper());
                    txt_pdespersonal.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                    txt_pidpersonal1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_pdespersonal.setValue("");
            txt_pidpersonal1.setValue("");
        }

        bandera = false;
    }

    /**
     * Metodo para mostar sucursal
     */
    @Listen("onOK=#txt_lidsucursal")
    public void busquedaSucursalLegal() {

        if (bandera == false) {
            bandera = true;
            if (txt_lidsucursal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("sucursal", txt_lidsucursal);
                objMapObjetos.put("dessucursal", txt_ldessucursal);
                objMapObjetos.put("cod", txt_lidsucursal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesSucursal.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //salir del lov sucursal legal
    @Listen("onBlur=#txt_lidsucursal")
    public void validaSucursalLegal() throws SQLException {

        if (!txt_lidsucursal.getValue().isEmpty()) {
            if (!txt_lidsucursal.getValue().equals("ALL")) {
                String sucursal = txt_lidsucursal.getValue();
                objDescuentos = objDaoDescuentos.getSucursal(sucursal);
                if (objDescuentos == null) {
                    Messagebox.show("El código de sucursal no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_lidsucursal.setValue("");
                            txt_lidsucursal.focus();
                        }
                    });
                } else {
                    txt_lidsucursal.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                    txt_ldessucursal.setValue(objDescuentos.getDes_sucursal());
                    txt_lidsucursal1.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                }
            }
        } else {
            txt_ldessucursal.setValue("");
            txt_lidsucursal1.setValue("");
        }

        bandera = false;
    }
    //para fecha

    @Listen("onOK=#txt_fidsucursal")
    public void busquedaSucursalFecha() {

        if (bandera == false) {
            bandera = true;
            if (txt_fidsucursal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("sucursal", txt_fidsucursal);
                objMapObjetos.put("dessucursal", txt_fdessucursal);
                objMapObjetos.put("cod", txt_fidsucursal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesSucursal.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //salir del lov sucursal legal
    @Listen("onBlur=#txt_fidsucursal")
    public void validaSucursalFecha() throws SQLException {

        if (!txt_fidsucursal.getValue().isEmpty()) {
            if (!txt_fidsucursal.getValue().equals("ALL")) {
                String sucursal = txt_fidsucursal.getValue();
                objDescuentos = objDaoDescuentos.getSucursal(sucursal);
                if (objDescuentos == null) {
                    Messagebox.show("El código de sucursal no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_fidsucursal.setValue("");
                            txt_fidsucursal.focus();
                        }
                    });
                } else {
                    txt_fidsucursal.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                    txt_fdessucursal.setValue(objDescuentos.getDes_sucursal());
                    txt_fidsucursal1.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                }
            }
        } else {
            txt_fdessucursal.setValue("");
            txt_fidsucursal1.setValue("");
        }

        bandera = false;
    }

    //por personal
    @Listen("onOK=#txt_pidsucursal")
    public void busquedaSucursalPersonal() {

        if (bandera == false) {
            bandera = true;
            if (txt_pidsucursal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("sucursal", txt_pidsucursal);
                objMapObjetos.put("dessucursal", txt_pdessucursal);
                objMapObjetos.put("cod", txt_pidsucursal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesSucursal.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //salir del lov sucursal personal
    @Listen("onBlur=#txt_pidsucursal")
    public void validaSucursalPersonal() throws SQLException {

        if (!txt_pidsucursal.getValue().isEmpty()) {
            if (!txt_pidsucursal.getValue().equals("ALL")) {
                String sucursal = txt_pidsucursal.getValue();
                objDescuentos = objDaoDescuentos.getSucursal(sucursal);
                if (objDescuentos == null) {
                    Messagebox.show("El código de sucursal no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_pidsucursal.setValue("");
                            txt_pidsucursal.focus();
                        }
                    });
                } else {
                    txt_pidsucursal.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                    txt_pdessucursal.setValue(objDescuentos.getDes_sucursal());
                    txt_pidsucursal1.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                }
            }
        } else {
            txt_pdessucursal.setValue("");
            txt_pidsucursal1.setValue("");
        }

        bandera = false;
    }

    //por centro de costo
    @Listen("onOK=#txt_cidsucursal")
    public void busquedaSucursalCosto() {

        if (bandera == false) {
            bandera = true;
            if (txt_cidsucursal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("sucursal", txt_cidsucursal);
                objMapObjetos.put("dessucursal", txt_cdessucursal);
                objMapObjetos.put("cod", txt_cidsucursal1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesSucursal.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //salir del lov sucursal personal
    @Listen("onBlur=#txt_cidsucursal")
    public void validaSucursalCosto() throws SQLException {

        if (!txt_cidsucursal.getValue().isEmpty()) {
            if (!txt_cidsucursal.getValue().equals("ALL")) {
                String sucursal = txt_cidsucursal.getValue();
                objDescuentos = objDaoDescuentos.getSucursal(sucursal);
                if (objDescuentos == null) {
                    Messagebox.show("El código de sucursal no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_cidsucursal.setValue("");
                            txt_cidsucursal.focus();
                        }
                    });
                } else {
                    txt_cidsucursal.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                    txt_cdessucursal.setValue(objDescuentos.getDes_sucursal());
                    txt_cidsucursal1.setValue(String.valueOf(objDescuentos.getId_sucursal()));
                }
            }
        } else {
            txt_cdessucursal.setValue("");
            txt_cidsucursal1.setValue("");
        }

        bandera = false;
    }

    @Listen("onClick=#btn_lconsultar")
    public void consultaLegal() throws SQLException {
        limpiarLista();
        String sucursal = txt_lidsucursal1.getValue();
        String personal = txt_lidpersonal1.getValue();
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();

        objlstDescuentos = objDaoDescuentos.consultaLegal(sucursal, personal, periodo);
        lst_descuentos.setModel(objlstDescuentos);

        if (rg_orden.getSelectedIndex() == 0) {
            Collections.sort(objlstDescuentos, new Comparator<InformesDescuentos>() {
                public int compare(InformesDescuentos o1, InformesDescuentos o2) {
                    return o1.getArea().compareTo(o2.getArea());

                }
            });
        } else if (rg_orden.getSelectedIndex() == 1) {
            Collections.sort(objlstDescuentos, new Comparator<InformesDescuentos>() {
                public int compare(InformesDescuentos o1, InformesDescuentos o2) {
                    return o1.getAp_paterno().compareTo(o2.getAp_paterno());

                }
            });
        }

    }

    //para consultar por fechas
    @Listen("onClick=#btn_fconsultar")
    public void consultaFecha() throws SQLException {
        limpiarLista();
        String sucursal = txt_fidsucursal1.getValue();
        String finicio = d_fdesde.getValue() == null ? "" : sdf.format(d_fdesde.getValue());
        String ffinal = d_fhasta.getValue() == null ? "" : sdf.format(d_fhasta.getValue());
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();
        objlstDescuentos = objDaoDescuentos.consultaFecha(sucursal, finicio, ffinal, periodo);
        lst_descuentos.setModel(objlstDescuentos);

    }

    @Listen("onClick=#btn_pconsultar")
    public void consultaPersona() throws SQLException {
        limpiarLista();
        String sucursal = txt_pidsucursal1.getValue();
        String personal = txt_pidpersonal1.getValue();
        String areacod = txt_codarea1.getValue();
        areacod = areacod.replace("'", "");
        
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();
        objlstDescuentos = objDaoDescuentos.consultaPersona(sucursal, personal, periodo,areacod);
        lst_descuentos.setModel(objlstDescuentos);

    }

    @Listen("onClick=#btn_cconsultar")
    public void consultaCosto() throws SQLException {
        limpiarLista();
        String sucursal = txt_cidsucursal1.getValue();
        String costo = txt_ccosto.getValue();
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();
        String finicio = d_cdesde.getValue() == null ? "" : sdf.format(d_cdesde.getValue());
        String ffinal = d_chasta.getValue() == null ? "" : sdf.format(d_chasta.getValue());
        objlstDescuentos = objDaoDescuentos.consultaCosto(sucursal, costo, periodo, finicio, ffinal);
        lst_descuentos.setModel(objlstDescuentos);
    }

    public void limpiarLista() {
        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<InformesDescuentos>();
        lst_descuentos.setModel(objlstDescuentos);
    }

    public void limpiaCamposLegal() {
        txt_lidsucursal.setValue("");
        txt_ldessucursal.setValue("");

        txt_ldespersonal.setValue("");
        txt_lidpersonal.setValue("");

        txt_lidpersonal1.setValue("");
        txt_lidsucursal1.setValue("");
    }

    public void limpiaCamposFecha() {
        txt_fdessucursal.setValue("");
        txt_fidsucursal.setValue("");
        txt_fidsucursal1.setValue("");
        d_fdesde.setValue(null);
        d_fhasta.setValue(null);
    }

    public void limpiaCamposPersona() {
        txt_pdespersonal.setValue("");
        txt_pdessucursal.setValue("");
        txt_pidpersonal.setValue("");
        txt_pidpersonal1.setValue("");
        txt_pidsucursal.setValue("");
        txt_pidsucursal1.setValue("");
    }

    public void limpiaCamposCosto() {
        txt_ccosto.setValue("");
        txt_cdescosto.setValue("");
        txt_cdessucursal.setValue("");
        txt_cidsucursal.setValue("");
        txt_cidsucursal1.setValue("");
        d_cdesde.setValue(null);
        d_chasta.setValue(null);
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() {
        Map objMapObjetos = new HashMap();
        //objMapObjetos.put("movimiento", objlstMovimiento);
        Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesDescuentos.zul", null, objMapObjetos);
        window.doModal();
    }

    //LOV COSTOS
    @Listen("onOK=#txt_ccosto")
    public void busquedaCCostos() {
        if (bandera == false) {
            bandera = true;
            if (txt_ccosto.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id", txt_ccosto);
                objMapObjetos.put("cc_descri", txt_cdescosto);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCCostos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_ccosto")
    public void valida_CCostos() throws SQLException {
        if (!txt_ccosto.getValue().isEmpty()) {
            String costo = txt_ccosto.getValue();
            objCCostos = objDaoAreas.getLovCCostos(costo);
            if (objCCostos == null) {
                Messagebox.show("El codigo debe ser del Centro Costos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_ccosto.focus();
                        txt_ccosto.setValue("");
                    }
                });

            } else {
                txt_ccosto.setValue(objCCostos.getCosto_cod());
                txt_cdescosto.setValue(objCCostos.getCosto_des());

            }

        } else {
            txt_cdescosto.setValue("");

        }
        bandera = false;
    }

}
