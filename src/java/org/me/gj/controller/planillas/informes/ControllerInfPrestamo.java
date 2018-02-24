/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.me.gj.controller.planillas.informes.ControllerMovimiento.bandera;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesPrestamos;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.util.media.AMedia;
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
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author cpure
 */
public class ControllerInfPrestamo extends SelectorComposer<Component> {

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar,
            tbbtn_btn_deshacer, tbbtn_btn_imprimir;

    @Wire
    Radiogroup rg_prestamo;

    @Wire
    Label lb_estadocuota, lb_estadopersonal, lb_personal, lb_estadoprestamo;

    @Wire
    Combobox cb_estadocuota, cb_estadopersonal, cb_estadoprestamo, cb_fsucursal;

    @Wire
    Listbox lst_personal, lst_prestamo;

    @Wire
    Textbox txt_codper, txt_desper;

    @Wire
    Doublebox txt_total, txt_totaldeuda;

    @Wire
    Tab tab_listaprestamo;

    @Wire
    Space s_space1, s_space2, s_space3;

    @Wire
    Cell c_cell1;

    @Wire
    Button btn_consultar;

    @Wire
    Datebox d_fechainicio, d_fechafin;

    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<InformesPrestamos> objlstPrestamos;
    DaoAccesos objDaoAccesos;
    Utilitarios objUtil;
    Personal objPersonal;
	Accesos objAccesos = new Accesos();
    DaoInformesPrestamos objDaoPrestamos;
    Session sesion = Sessions.getCurrent();
	private static final Logger LOGGER = Logger.getLogger(ControllerInfPrestamo.class);
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    SimpleDateFormat sdfcont = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoAccesos = new DaoAccesos();
        objUtil = new Utilitarios();
        objDaoPrestamos = new DaoInformesPrestamos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        objlstPrestamos = new ListModelList<InformesPrestamos>();
        cb_fsucursal.setModel(objlstSucursal);
        rg_prestamo.setSelectedIndex(1);
        cb_estadopersonal.setSelectedIndex(0);
        cb_estadoprestamo.setSelectedIndex(0);
        cb_estadocuota.setSelectedIndex(0);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90303000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Prestamo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Prestamo con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Prestamo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Prestamo");
        }

    }
	
    @Listen("onOK=#txt_codper")
    public void busquedaPersonalMan() {

        if (bandera == false) {
            bandera = true;
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                //campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovPersonalPrestamo.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    @Listen("onBlur=#txt_codper")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codper.getValue().isEmpty()) {
            if (!txt_codper.getValue().equals("ALL")) {
                String id = txt_codper.getValue();
                objPersonal = objDaoPrestamos.consultaPersonalPorId(id.substring(1));
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

                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_desper.setValue("");

        }

        bandera = false;
    }

    @Listen("onClick=#rg_prestamo")
    public void radioSeleccion() throws SQLException {
        objlstPrestamos = null;
        lst_personal.setModel(objlstPrestamos);
        lst_prestamo.setModel(objlstPrestamos);
        if (rg_prestamo.getSelectedIndex() == 0) {
            lst_personal.setVisible(false);
            lst_prestamo.setVisible(true);

            cb_estadocuota.setVisible(true);
            lb_estadocuota.setVisible(true);

            cb_estadopersonal.setVisible(false);
            lb_estadopersonal.setVisible(false);

            txt_codper.setVisible(true);
            txt_desper.setVisible(true);
            lb_personal.setVisible(true);

            cb_estadoprestamo.setVisible(false);
            lb_estadoprestamo.setVisible(false);

            s_space1.setVisible(true);
            s_space2.setVisible(true);
            // s_space3.setVisible(true);

            c_cell1.setVisible(true);

        } else {

            lst_personal.setVisible(true);
            lst_prestamo.setVisible(false);

            cb_estadocuota.setVisible(false);
            lb_estadocuota.setVisible(false);

            cb_estadopersonal.setVisible(true);
            lb_estadopersonal.setVisible(true);

            txt_codper.setVisible(false);
            txt_desper.setVisible(false);
            lb_personal.setVisible(false);

            cb_estadoprestamo.setVisible(true);
            lb_estadoprestamo.setVisible(true);

            s_space1.setVisible(false);
            s_space2.setVisible(false);
            // s_space3.setVisible(false);

            c_cell1.setVisible(false);
        }
    }

    @Listen("onClick=#btn_consultar")
    public void consultarPrestamo() throws SQLException {
        int i_rg_prestamo = rg_prestamo.getSelectedIndex();
        String s_cb_fsucursal = cb_fsucursal.getValue();
        int i_cb_fsucursal = 0;
        if (!s_cb_fsucursal.equals("")) {
            i_cb_fsucursal = cb_fsucursal.getSelectedItem().getValue();
        }

        String s_cb_estadopersonal = cb_estadopersonal.getValue().toString();
        String s_cb_estadoprestamo = cb_estadoprestamo.getValue().toString();
        String s_cb_estadocuota = cb_estadocuota.getValue().toString();
        if (i_rg_prestamo == 1) {

            if (d_fechainicio.getValue() == null && d_fechafin.getValue() == null) {
                objlstPrestamos = objDaoPrestamos.consultaPrestamosSinFecha(objUsuCredential.getCodemp(), i_cb_fsucursal, s_cb_estadopersonal, s_cb_estadoprestamo);
            } else if (d_fechainicio.getValue() == null || d_fechafin.getValue() == null) {
                Messagebox.show("Ingrese el otro campo de fecha", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (objUtil.compareFechas(d_fechainicio.getValue(), d_fechafin.getValue()) == "F1>") {
                    Messagebox.show("Fecha final no puede ser menor que la fecha inicial", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    objlstPrestamos = objDaoPrestamos.consultaPrestamosConFecha(objUsuCredential.getCodemp(), i_cb_fsucursal,
                            s_cb_estadopersonal, s_cb_estadoprestamo, convertJavaDateToSqlDate(d_fechainicio.getValue()), convertJavaDateToSqlDate(d_fechafin.getValue()));
                }

            }
            lst_personal.setModel(objlstPrestamos);
        } else {
            if (txt_codper.getText() == "") {
                Messagebox.show("Ingrese un código de un Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (d_fechainicio.getValue() == null && d_fechafin.getValue() == null) {
                    objlstPrestamos = objDaoPrestamos.consultaPrestamosDetalleSinFecha(objUsuCredential.getCodemp(), i_cb_fsucursal, objPersonal.getPlidper(), s_cb_estadocuota);
                } else if (d_fechainicio.getValue() == null || d_fechafin.getValue() == null) {
                    Messagebox.show("Ingrese el otro campo de fecha", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    if (objUtil.compareFechas(d_fechainicio.getValue(), d_fechafin.getValue()) == "F1>") {
                        Messagebox.show("Fecha final no puede ser menor que la fecha inicial", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        objlstPrestamos = objDaoPrestamos.consultaPrestamosDetalleConFecha(objUsuCredential.getCodemp(), i_cb_fsucursal,
                                objPersonal.getPlidper(), s_cb_estadocuota, convertJavaDateToSqlDate(d_fechainicio.getValue()), convertJavaDateToSqlDate(d_fechafin.getValue()));
                    }

                }
				if (!objlstPrestamos.isEmpty()) {
                    lst_prestamo.setModel(objlstPrestamos);

                //montoTotal();
                    //montoPendiente();
                    txt_total.setValue(objlstPrestamos.get(0).getTotalprestamos());
                    txt_totaldeuda.setValue(objlstPrestamos.get(0).getTotaldeuda());
                }else{
                    Messagebox.show("El trabajador no tiene ningún préstamo activo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            }
        }

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {

        if (objlstPrestamos == null) {
            Messagebox.show("No hay ningun prestamo en consulta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (objlstPrestamos.getSize() == 0) {
            Messagebox.show("No hay registros para el personal en consulta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

        } else {
            if (rg_prestamo.getSelectedIndex() == 0) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("NRODOC", objlstPrestamos.get(0).getNrodocper());
                objMapObjetos.put("ESTADOCUOTA", cb_estadocuota.getValue().toString());
                objMapObjetos.put("fechainicio", d_fechainicio.getValue());
                objMapObjetos.put("fechafin", d_fechafin.getValue());
                objMapObjetos.put("formato", "detallado");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesPrestamo.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ESTADOPRESTAMO", cb_estadoprestamo.getValue().toString());
                objMapObjetos.put("ESTADOTRABAJADOR", cb_estadopersonal.getValue().toString());
                objMapObjetos.put("fechainicio", d_fechainicio.getValue());
                objMapObjetos.put("fechafin", d_fechafin.getValue());
                objMapObjetos.put("formato", "total");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesPrestamo.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public void montoTotal() {
        double a = 0;
        for (int i = 0; i < objlstPrestamos.size(); i++) {

            a = objlstPrestamos.get(i).getMontocuota() + a;

        }
        txt_total.setValue(a);
    }

    public void montoPendiente() {
        double a = 0;
        for (int i = 0; i < objlstPrestamos.size(); i++) {
            if (objlstPrestamos.get(i).getEstadocuota().equals("PENDIENTE")) {
                a = objlstPrestamos.get(i).getMontocuota() + a;
            }
        }
        txt_totaldeuda.setValue(a);
    }

}
