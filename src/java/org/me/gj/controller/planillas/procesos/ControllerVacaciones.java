/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.me.gj.controller.planillas.informes.DaoInformesPrestamos;
import static org.me.gj.controller.planillas.procesos.ControllerMovLinea.bandera;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Vacaciones;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
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
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author cpure
 */
public class ControllerVacaciones extends SelectorComposer<Component> {

    @Wire
    Listbox lst_personal, lst_vacacionesdetalle;

    @Wire
    Textbox txt_idpersonal, txt_despersonal,/* txt_descriparea, txt_idarea,*/txt_desarea,
            txt_periodo, txt_glosa, txt_periodogozado, txt_codarea, txt_codarea1, txt_usuadd, txt_usumod;

    @Wire
    Combobox cb_fsucursal, cb_estado, cb_fsucursal2;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod, d_periodogozado;
    @Wire
    Button btn_consultar;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoc, tbbtn_btn_eliminarc, tbbtn_btn_guardarc, tbbtn_btn_deshacerc, tbbtn_btn_editarc;
			
    @Wire
    Tab tab_listapersonal, tab_vacacion;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
	ListModelList<Integer> objlstEliminarCorrelativo;
	ListModelList<Integer> objlstModificarCorrelativo;
    ListModelList<Integer> objlstModificarSeleccionado;
    private static final Logger LOGGER = Logger.getLogger(ControllerAsistenciaGen.class);
    public static boolean bandera = false;
    ListModelList<Vacaciones> objlstVacaciones;
    ListModelList<Vacaciones> objlstVacacionesDet;
    DaoVacaciones objDaoVacaciones;
    Vacaciones objVacaciones, objVacacionesDet;
    Personal objPersonal;
    DaoInformesPrestamos objDaoPrestamos;
    DaoMovLinea objDaoMovLinea;
    DaoAccesos objDaoAccesos;
    ManAreas objArea;
    String s_estado = "Q";
	Accesos objAccesos;
	String s_estadodetalle = "Q";
    int i_sel = 0;
    int i_selecDetalle = 0;
	int ntipodoc = 1;
    String s_mensaje = "";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	boolean b_valida = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoMovLinea = new DaoMovLinea();
        objDaoAccesos = new DaoAccesos();
        objArea = new ManAreas();
        objlstVacaciones = new ListModelList<Vacaciones>();
        objDaoVacaciones = new DaoVacaciones();
        objVacaciones = new Vacaciones();
        objDaoPrestamos = new DaoInformesPrestamos();
        objlstVacacionesDet = new ListModelList<Vacaciones>();
        objVacacionesDet = new Vacaciones();
        objlstVacaciones = objDaoVacaciones.listarVacaciones(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());
        lst_personal.setModel(objlstVacaciones);
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        cb_estado.setSelectedIndex(0);
		objlstEliminarCorrelativo = new ListModelList<Integer>();
        objlstModificarCorrelativo  = new ListModelList<Integer>();
		objlstModificarSeleccionado = new ListModelList<Integer>();
    }
	
     @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {

        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90205000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Vacaciones con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Vacaciones con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Vacaciones");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Vacaciones");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Vacaciones");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Vacaciones");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Vacaciones");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Vacaciones");
        }
    }

    @Listen("onSelect=#lst_personal")
    public void seleccionaRegistroCab() throws SQLException {
        limpiarCampos();
		limpiaAuditoria();
        i_sel = 0;
        objVacaciones = (Vacaciones) lst_personal.getSelectedItem().getValue();
        if (objVacaciones == null) {
            objVacaciones = objlstVacaciones.get(i_sel + 1);
        }
        i_sel = lst_personal.getSelectedIndex();
        habilitaCampos(true);
        llenarCabecera(objVacaciones);
        llenaDetalle(objVacaciones);
		objlstEliminarCorrelativo = new ListModelList<Integer>();
        objlstModificarCorrelativo  = new ListModelList<Integer>();
		objlstModificarSeleccionado= new ListModelList<Integer>();

    }
	
    @Listen("onSelect=#lst_vacacionesdetalle")
    public void seleccionaRegistroDet() throws SQLException, ParseException {

        i_selecDetalle = 0;
        objVacacionesDet = (Vacaciones) lst_vacacionesdetalle.getSelectedItem().getValue();
        if (objVacacionesDet == null) {
            objVacacionesDet = objlstVacacionesDet.get(i_sel + 1);
        }
        i_selecDetalle = lst_vacacionesdetalle.getSelectedIndex();
        llenaCamposDetalle(objVacacionesDet);
		llenaAuditoria(objVacacionesDet);
		
    }

    @Listen("onClick=#tbbtn_btn_guardarc")
    public void editarListaDetalle() {
		
        objVacacionesDet = new Vacaciones();

        if (d_fecini.getValue() == null) {
            Messagebox.show("Debe de ingresar fecha de inicio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (d_fecfin.getValue() == null) {
            Messagebox.show("Debe de ingresar fecha fin", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (d_periodogozado.getValue() == null) {
            Messagebox.show("Debe de ingresar Periodo Gozado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (d_fecini.getValue().after(d_fecfin.getValue())) {

            Messagebox.show("Fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (txt_periodo.getValue().isEmpty()) {
            Messagebox.show("Debe de ingresar Periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (txt_glosa.getValue().isEmpty()) {
            Messagebox.show("Debe de ingresar Glosa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (d_fecini.getValue().after(d_fecfin.getValue())) {

            Messagebox.show("Fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if ((s_estado.equals("N") || s_estado.equals("M")) && s_estadodetalle.equals("N")) {
            objVacacionesDet.setGlosa(txt_glosa.getValue());
            objVacacionesDet.setPeriodo(txt_periodo.getValue());
            objVacacionesDet.setPeriodogozado(formatter.format(d_periodogozado.getValue()));
            objVacacionesDet.setFechainicio(d_fecini.getValue());
            objVacacionesDet.setFechafin(d_fecfin.getValue());
            objVacacionesDet.setDias(Days.daysBetween(new LocalDate(objVacacionesDet.getFechainicio()), new LocalDate(objVacacionesDet.getFechafin().getTime())).getDays() + 1);
            objVacacionesDet.setS_fechainicio(sdf.format(objVacacionesDet.getFechainicio()));
            objVacacionesDet.setS_fechafin(sdf.format(objVacacionesDet.getFechafin()));

            if (validaFechaVacaciones(objlstVacacionesDet) || objlstVacacionesDet.isEmpty()) {
                if (s_estado.equals("M")) {
                    int i_sumadias = 0;

                    for (int i = 0; i < objlstVacacionesDet.size(); i++) {
                        i_sumadias = i_sumadias + objlstVacacionesDet.get(i).getDias();

                    }
                    i_sumadias = i_sumadias + objVacacionesDet.getDias();

                    if (i_sumadias > objVacaciones.getTotal()) {
                        Messagebox.show("Ya excedió los días pendientes de vacaciones", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else if (diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()) > 30) {
                        Messagebox.show("No puede salir de vacaciones más de 30 días", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        objlstVacacionesDet.add(objVacacionesDet);
                    }
                }else{
                    objlstVacacionesDet.add(objVacacionesDet);
                }
				lst_vacacionesdetalle.setModel(objlstVacacionesDet);
                limpiarCamposDetalle();
                habilitaBotonesDetalle(false, true);
                habilitaCamposDetalle(true);
				
            } else {
                Messagebox.show("Ya existen registros con esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            /*lst_vacacionesdetalle.setModel(objlstVacacionesDet);
            limpiarCamposDetalle();
            habilitaBotonesDetalle(false, true);
            habilitaCamposDetalle(true);*/
        } else if ((s_estado.equals("N") || s_estado.equals("M")) && s_estadodetalle.equals("M")) {
            if (validaFechaVacacionesModificar(objlstVacacionesDet)) {
                objlstVacacionesDet.get(i_selecDetalle).setFechainicio(d_fecini.getValue());
                objlstVacacionesDet.get(i_selecDetalle).setS_fechainicio(sdf.format(d_fecini.getValue()));
                objlstVacacionesDet.get(i_selecDetalle).setFechafin(d_fecfin.getValue());
                objlstVacacionesDet.get(i_selecDetalle).setS_fechafin(sdf.format(d_fecfin.getValue()));
                objlstVacacionesDet.get(i_selecDetalle).setPeriodo(txt_periodo.getValue());
                objlstVacacionesDet.get(i_selecDetalle).setPeriodogozado(formatter.format(d_periodogozado.getValue()));
                objlstVacacionesDet.get(i_selecDetalle).setGlosa(txt_glosa.getValue());
				objlstVacacionesDet.get(i_selecDetalle).setDias(Days.daysBetween(new LocalDate(objlstVacacionesDet.get(i_selecDetalle).getFechainicio()), new LocalDate(objlstVacacionesDet.get(i_selecDetalle).getFechafin().getTime())).getDays() + 1 );
                objlstModificarCorrelativo.add(objlstVacacionesDet.get(i_selecDetalle).getCorrelativo());
                objlstModificarSeleccionado.add(i_selecDetalle);
				
				lst_vacacionesdetalle.setModel(objlstVacacionesDet);
                limpiarCamposDetalle();
                habilitaBotonesDetalle(false, true);
                habilitaCamposDetalle(true);
            } else {
                Messagebox.show("Ya existen registros con esas fechas", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            /*lst_vacacionesdetalle.setModel(objlstVacacionesDet);
            limpiarCamposDetalle();
            habilitaBotonesDetalle(false, true);
            habilitaCamposDetalle(true);*/
        }
    }

    @Listen("onBlur=#txt_periodo")
    public void verificaCampos() {
        String s_aux = "0123456789-";
        int i_aux1;
        int i_aux2;
        boolean b_verificar = true;
        char[] chars = txt_periodo.getValue().toCharArray();
        if (!txt_periodo.getValue().isEmpty()) {
            for (char c : chars) {
                String s = "" + c;
                if (!s_aux.contains(s)) {
                    Messagebox.show("Formato inválido. Ej: 15-16", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    b_verificar = false;
                    txt_periodo.setValue("");
                    txt_periodo.setFocus(true);
                    break;
                }

            }
            if (b_verificar == true) {
                if (txt_periodo.getValue().length() != 5) {
                    Messagebox.show("Formato inválido. Ej: 15-16", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    b_verificar = false;
                    txt_periodo.setValue("");
                    txt_periodo.setFocus(true);
                }
            }

            if (b_verificar == true) {
                if (chars[0] == '-' || chars[1] == '-' || chars[3] == '-' || chars[4] == '-') {
                    Messagebox.show("Formato inválido. Ej: 15-16", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    b_verificar = false;
                    txt_periodo.setValue("");
                    txt_periodo.setFocus(true);

                }
            }
            if (b_verificar == true) {
                s_aux = txt_periodo.getValue().substring(2, 3);
                i_aux1 = Integer.parseInt(txt_periodo.getValue().substring(0, 2));
                i_aux2 = Integer.parseInt(txt_periodo.getValue().substring(3, 5));
                if (!s_aux.equals("-")) {
                    Messagebox.show("Formato inválido. Ej: 15-16", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_periodo.setValue("");
                    txt_periodo.setFocus(true);
                } else if (i_aux1 > i_aux2) {
                    Messagebox.show("Formato inválido. Ej: 15-16", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_periodo.setValue("");
                    txt_periodo.setFocus(true);

                }
            }

        } else {
            txt_periodo.setValue("");

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
							txt_codarea1.setValue("");
                            txt_desarea.setValue("");
                            txt_codarea.focus();
                        }
                    });
                }
            }
        } else {
            txt_codarea.setValue("");
			txt_codarea1.setValue("");
            txt_desarea.setValue("");

        }
        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_personal.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            //habilitaCamposDetalle(false);
            tbbtn_btn_nuevoc.setDisabled(false);

            tbbtn_btn_eliminarc.setDisabled(false);

            tbbtn_btn_editarc.setDisabled(false);
            s_estado = "M";
        }
    }
	
    @Listen("onClick=#tbbtn_btn_editarc")
    public void botonEditarDetalle() {

        if (lst_vacacionesdetalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            tbbtn_btn_nuevoc.setDisabled(true);
            tbbtn_btn_deshacerc.setDisabled(false);
            tbbtn_btn_eliminarc.setDisabled(true);
            tbbtn_btn_guardarc.setDisabled(false);
            tbbtn_btn_editarc.setDisabled(true);
            txt_glosa.setDisabled(false);
            d_periodogozado.setDisabled(false);
            txt_periodo.setDisabled(false);
            d_fecini.setDisabled(false);
            d_fecfin.setDisabled(false);
            s_estadodetalle = "M";

        }
    }
	
    @Listen("onClick=#tbbtn_btn_deshacerc")
    public void deshacerDetalle() {

        String s_men = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesDetalle(false, true);
                            limpiarCamposDetalle();
							limpiaAuditoria();
                            habilitaCamposDetalle(true);
                            Utilitarios.deshabilitarLista(false, lst_vacacionesdetalle);
                            lst_vacacionesdetalle.clearSelection();
                        }
                    }
                });
    }
	
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {

        objVacaciones = new Vacaciones();
        objVacacionesDet = new Vacaciones();
		objlstEliminarCorrelativo = new ListModelList<Integer>();
        objlstModificarCorrelativo  = new ListModelList<Integer>();
        objlstModificarSeleccionado =new ListModelList<Integer>();
        limpiarCampos();
		limpiaAuditoria();
        objlstVacacionesDet = new ListModelList<Vacaciones>();
        objlstVacaciones = new ListModelList<Vacaciones>();
        lst_vacacionesdetalle.setModel(objlstVacacionesDet);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        //habilitaCamposDetalle(false);
        habilitaCampos(false);
        habilitaBotones(true, false);
        cb_fsucursal2.setModel(objlstSucursal);
        s_estado = "N";
        tbbtn_btn_guardar.setDisabled(false);
        tbbtn_btn_deshacer.setDisabled(false);
        habilitaBotonesDetalle(false, true);
		txt_idpersonal.focus();
        //txt_idarea.setFocus(true);

    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void guardar() {

        if (objlstVacacionesDet.size() < 1) {
            Messagebox.show("Debe dingresar al menos un registro de vacaciones", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Esta seguro que desea guardar?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {

                                Object d = cb_fsucursal2.getSelectedItem().getValue();
                                //  objVacaciones.setIdsucursal(Integer.parseInt();
                                objVacaciones.setIdsucursal(Integer.parseInt(d.toString()));
//                                if (s_estado.equals("N")) {
//                                    objVacaciones.setNrodocpersona(txt_idpersonal.getValue().substring(1));
//                                } else {
//                                    
//                                }
                                objVacaciones.setNrodocpersona(txt_idpersonal.getValue().substring(1));
                                objVacaciones.setTipodoc(Integer.parseInt(txt_idpersonal.getValue().substring(0, 1)));
                                try {
                                    //b_valida = objDaoVacaciones.validaVacaciones(objVacaciones.getNrodocpersona(), ntipodoc,objlstVacacionesDet.get(3).getFechainicio() , objlstVacacionesDet.get(3).getFechafin());
                                    if ((s_estado.equals("N") || s_estado.equals("M")) && (s_estadodetalle.equals("N"))) {
                                        s_mensaje = objDaoVacaciones.insertarVacaciones(objVacaciones, objlstVacacionesDet);
                                    } else if (s_estado.equals("M") && s_estadodetalle.equals("M")) {
                                        s_mensaje = objDaoVacaciones.modificarVacaciones(objVacaciones, objlstVacacionesDet, objlstModificarCorrelativo, objlstModificarSeleccionado);
                                    } else if (s_estado.equals("M") && s_estadodetalle.equals("E")) {
                                        s_mensaje = objDaoVacaciones.eliminarVacacionesDetalle(objlstEliminarCorrelativo);
                                    }
                                    habilitaBotones(false, true);
                                    seleccionaTab(true, false);
                                    limpiaAuditoria();
                                    limpiarCamposDetalle();
                                    habilitaTab(false, false);

                                    habilitaBotonesDetalle(true, true);
                                    objlstVacaciones = objDaoVacaciones.listarVacaciones(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());

                                    lst_personal.setModel(objlstVacaciones);
                                } catch (SQLException ex) {
                                    s_mensaje = ex.toString();
                                }
                                if (s_mensaje.isEmpty()) {
                                    s_mensaje = "Se guardo con éxito";
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                s_estadodetalle = "Q";
                                s_estado = "Q";
                            }
                        }
                    });

        }

    }
    
	@Listen("onClick=#tbbtn_btn_eliminar")
    public void eliminarVacaciones() {

        if (lst_personal.getSelectedIndex() == -1) {
            Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Esta seguro que desea eliminar?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {

                                try {
                                    s_mensaje = objDaoVacaciones.eliminarVacaciones(objVacaciones.getNrodocpersona(), objVacaciones.getTipodoc());
                                    habilitaBotones(false, true);
                                    seleccionaTab(true, false);
                                    limpiaAuditoria();
                                    limpiarCamposDetalle();
                                    /// habilitaTab(false, true);
                                    seleccionaTab(true, false);

                                    habilitaBotonesDetalle(true, true);
                                    objlstVacaciones = objDaoVacaciones.listarVacaciones(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc());

                                    lst_personal.setModel(objlstVacaciones);

                                } catch (SQLException ex) {
                                    s_mensaje = ex.toString();
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });

        }

    }

    @Listen("onClick=#tbbtn_btn_eliminarc")
    public void bonotonEliminarDetalle() {

        if (s_estadodetalle.equals("Q") || s_estadodetalle.equals("E")) {
            objlstEliminarCorrelativo.add(objVacacionesDet.getCorrelativo());
            objlstVacacionesDet.remove(objVacacionesDet);

            s_estadodetalle = "E";
        } else {
            objlstVacacionesDet.remove(objVacacionesDet);
        }

    }
	
	@Listen("onClick=#tbbtn_btn_nuevoc")
    public void botonNuevoDetalle() {
		
        if (txt_idpersonal.getValue().isEmpty()) {
            Messagebox.show("Debe de ingresar el personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (cb_fsucursal2.getValue().isEmpty()) {
            Messagebox.show("Debe de ingresar la sucursal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
			limpiarCamposDetalle();
			limpiaAuditoria();
            habilitaCamposDetalle(false);
            habilitaBotonesDetalle(true, false);
			Utilitarios.deshabilitarLista(true, lst_vacacionesdetalle);
            d_fecini.setFocus(true);
            s_estadodetalle = "N";
        }
        /*
         objVacacionesDet = new Vacaciones();

         if (d_fecini.getValue() == null) {
         Messagebox.show("Debe de ingresar fecha de inicio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else if (d_fecfin.getValue() == null) {
         Messagebox.show("Debe de ingresar fecha fin", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else if (txt_periodogozado.getValue().isEmpty()) {
         Messagebox.show("Debe de ingresar Periodo Gozado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else if (txt_periodo.getValue().isEmpty()) {
         Messagebox.show("Debe de ingresar Periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else if (txt_glosa.getValue().isEmpty()) {
         Messagebox.show("Debe de ingresar Glosa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else if (d_fecini.getValue().after(d_fecfin.getValue())) {

         Messagebox.show("Fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } else {
         objVacacionesDet.setGlosa(txt_glosa.getValue());
         objVacacionesDet.setPeriodo(txt_periodo.getValue());
         objVacacionesDet.setPeriodogozado(txt_periodogozado.getValue());
         objVacacionesDet.setFechainicio(d_fecini.getValue());
         objVacacionesDet.setFechafin(d_fecfin.getValue());
         objVacacionesDet.setDias(Days.daysBetween(new LocalDate(objVacacionesDet.getFechainicio()), new LocalDate(objVacacionesDet.getFechafin().getTime())).getDays() + 1);
         objVacacionesDet.setS_fechainicio(sdf.format(objVacacionesDet.getFechainicio()));
         objVacacionesDet.setS_fechafin(sdf.format(objVacacionesDet.getFechafin()));
         objlstVacacionesDet.add(objVacacionesDet);
         lst_vacacionesdetalle.setModel(objlstVacacionesDet);
         txt_glosa.setValue("");
         txt_periodo.setValue("");
         txt_periodogozado.setValue("");
         d_fecini.setValue(null);
         d_fecfin.setValue(null);
         }
         */
    }

    @Listen("onOK=#d_fecini")
    public void next() {
        d_fecfin.focus();
    }

    @Listen("onOK=#d_fecfin")
    public void next1() {
        d_periodogozado.focus();
    }

    @Listen("onOK=#d_periodogozado")
    public void next2() {
        txt_periodo.focus();
    }

    @Listen("onOK=#txt_periodo")
    public void next3() {
        txt_glosa.focus();
    }

    @Listen("onOK=#txt_glosa")
    public void next4() {
        editarListaDetalle();
    }
	
    @Listen("onOK=#txt_idpersonal")
    public void busquedaPersonalMan() {
        String s_area;

        if (txt_idpersonal.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_per", txt_idpersonal);
            objMapObjetos.put("des_per", txt_despersonal);
            objMapObjetos.put("controlador", "ControllerMovimiento");
            s_area = "TODOS";
            objMapObjetos.put("area", s_area);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVacacionesPersonal.zul", null, objMapObjetos);
            window.doModal();
        }

    }

@Listen("onBlur=#txt_idpersonal")
    public void valida_PersonalPrincipal() throws SQLException {

        if (!txt_idpersonal.getValue().isEmpty()) {
            String id = txt_idpersonal.getValue();
            int iddocumento = objDaoMovLinea.verificarDniVacaciones(id);
            if (iddocumento == 0) {

                objPersonal = objDaoMovLinea.getLovPersonal(id);

                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_idpersonal.setValue(null);
                            txt_idpersonal.setValue(null);
                            txt_idpersonal.focus();
                            txt_despersonal.setValue("");

                        }
                    });

                } else {
                    txt_idpersonal.setValue(objPersonal.getPlidper());
                    txt_despersonal.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                }

            } else {
                Messagebox.show("Este código ya se encuentra registrado,intenta con otro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_idpersonal.setValue(null);
                        txt_idpersonal.setValue(null);
                        txt_idpersonal.focus();
                        txt_despersonal.setValue("");

                    }
                });

            }
        } else {
            txt_despersonal.setValue("");
            //   habilitaBotonesDetalle(true);

        }
        bandera = false;

    }
	
    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() throws SQLException {
        String s_men = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_personal.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            lst_personal.focus();
                            //
                            habilitaCampos(true);
                            tbbtn_btn_editarc.setDisabled(true);
                            tbbtn_btn_nuevoc.setDisabled(true);
                            tbbtn_btn_deshacerc.setDisabled(true);
                            tbbtn_btn_eliminarc.setDisabled(true);
                            tbbtn_btn_guardarc.setDisabled(true);

                            s_estado = "Q";
                            s_estadodetalle = "Q";

                        }
                    }
                });

    }
	
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        String s_area;
        if (objlstVacaciones.size() < 1) {
            Messagebox.show("No hay ningun registro en consulta", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

        } else {
            if (tab_listapersonal.isSelected()) {
                Map objMapObjetos = new HashMap();

                if (txt_codarea1.getValue().isEmpty()) {
                    s_area = "TODOS";
                } else {
                    s_area = txt_codarea1.getValue();
                }
                objMapObjetos.put("AREA", s_area);
                objMapObjetos.put("ESTADO", cb_estado.getValue());
                objMapObjetos.put("FORMATO", "PERSONAL");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/procesos/LovImpresionVacaciones.zul", null, objMapObjetos);
                window.doModal();
            } else {

                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TIPODOC", objVacaciones.getTipodoc());
                objMapObjetos.put("NRODOC", objVacaciones.getNrodocpersona().toString());
                objMapObjetos.put("FORMATO", "DETALLE");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/procesos/LovImpresionVacaciones.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onClick=#btn_consultar")
    public void consultaFiltro() throws SQLException {
        String s_aux = txt_codarea1.getValue().replace("'", "");
        s_aux = s_aux.replace(")", "");
        int sucu;
        if (cb_fsucursal.getValue().toString().isEmpty()) {
            sucu = 0;
        } else {
            sucu = Integer.parseInt(cb_fsucursal.getSelectedItem().getValue().toString());
        }
        objlstVacaciones = objDaoVacaciones.listarVacacionesFiltro(objUsuCredential.getCodemp(), sucu, s_aux, cb_estado.getValue());
        lst_personal.setModel(objlstVacaciones);
    }

public void limpiarCampos() {
       // txt_idarea.setValue("");
        txt_despersonal.setValue("");
        txt_glosa.setValue("");
       // txt_descriparea.setValue("");
        txt_idpersonal.setValue("");
        txt_periodo.setValue("");
        d_periodogozado.setValue(null);
        d_fecini.setValue(null);
        d_fecfin.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
       // txt_idarea.setDisabled(b_valida);
        txt_despersonal.setDisabled(b_valida);
       // txt_descriparea.setDisabled(b_valida);
        txt_idpersonal.setDisabled(b_valida);
        //txt_periodo.setDisabled(b_valida);
        cb_fsucursal2.setDisabled(b_valida);
        /* d_periodogozado.setDisabled(b_valida);
         d_fecini.setDisabled(b_valida);
         d_fecfin.setDisabled(b_valida);
         txt_glosa.setDisabled(b_valida);*/
    }

    public void habilitaCamposDetalle(boolean b_valida) {
        txt_periodo.setDisabled(b_valida);
        d_periodogozado.setDisabled(b_valida);
        d_fecini.setDisabled(b_valida);
        d_fecfin.setDisabled(b_valida);
        txt_glosa.setDisabled(b_valida);
        /* tbbtn_btn_deshacerc.setDisabled(b_valida);
         tbbtn_btn_eliminarc.setDisabled(b_valida);
         tbbtn_btn_guardarc.setDisabled(b_valida);
         tbbtn_btn_nuevoc.setDisabled(b_valida);*/
    }

    public void llenarCabecera(Vacaciones objVaca) throws SQLException {
        int j = -1;
        //    cb_fsucursal2.setModel(objlstSucursal);
      //  txt_descriparea.setValue(objVaca.getArea());
        txt_despersonal.setValue(objVaca.getNombres());

        //txt_glosa.setValue(objVaca.getGlosa());
       // txt_idarea.setValue(objVaca.getIdarea());
        txt_idpersonal.setValue(objVaca.getTipodoc() + objVaca.getNrodocpersona());
        for (int i = 0; i < objlstSucursal.size(); i++) {
            if (objlstSucursal.get(i).getSuc_des().equals(objVaca.getSucursal())) {
                j = i;
                break;
            }
        }
        Comboitem item = new Comboitem();
        item.setValue(objlstSucursal.get(j).getSuc_id());
        item.setLabel(objlstSucursal.get(j).getSuc_des());
        cb_fsucursal2.appendChild(item);
        cb_fsucursal2.setSelectedItem(item);
        // txt_periodo.setValue(objVaca.getPeriodo());
        // txt_periodogozado.setValue(objVaca.getPeriodogozado());
        // d_fecini.setValue(objVaca.getFechainicio());
        // d_fecfin.setValue(objVaca.getFechafin());

    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapersonal.setDisabled(b_valida1);
        tab_vacacion.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listapersonal.setSelected(b_valida1);
        tab_vacacion.setSelected(b_valida2);
    }

    public void llenaDetalle(Vacaciones objVaca) throws SQLException {
        objlstVacacionesDet = objDaoVacaciones.listarVacacionesporPersonal(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), objVaca.getTipodoc(), objVaca.getNrodocpersona());
        lst_vacacionesdetalle.setModel(objlstVacacionesDet);

    }

    public void llenaCamposDetalle(Vacaciones objVaca) throws SQLException, ParseException {

		String dateInString = objVaca.getPeriodogozado();
        Date date = formatter.parse(dateInString);
        txt_periodo.setValue(objVaca.getPeriodo());
        d_periodogozado.setValue(date);
        d_fecini.setValue(objVaca.getFechainicio());
        d_fecfin.setValue(objVaca.getFechafin());
        txt_glosa.setValue(objVaca.getGlosa());

    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

	public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoc.setDisabled(b_valida1);
        tbbtn_btn_editarc.setDisabled(b_valida1);
        tbbtn_btn_guardarc.setDisabled(b_valida2);
        tbbtn_btn_deshacerc.setDisabled(b_valida2);
        tbbtn_btn_eliminarc.setDisabled(b_valida1);
        /* tbbtn_btn_nuevoc.setDisabled(b_valida1);
         tbbtn_btn_editarc.setDisabled(b_valida1);
         tbbtn_btn_guardarc.setDisabled(b_valida1);
         tbbtn_btn_deshacerc.setDisabled(b_valida1);
         tbbtn_btn_eliminarc.setDisabled(b_valida1);*/
    }

    public void limpiarCamposDetalle() {
        txt_periodo.setValue("");
        d_periodogozado.setValue(null);
        d_fecini.setValue(null);
        d_fecfin.setValue(null);
        txt_glosa.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenaAuditoria(Vacaciones objVac) {
        txt_usuadd.setValue(objVac.getUsu_add());
        txt_usumod.setValue(objVac.getUsu_mod());
        d_fecadd.setValue(objVac.getFecha_add());
        d_fecmod.setValue(objVac.getFecha_mod());
    }
	
public boolean validaFechaVacaciones(ListModelList<Vacaciones> objListVacaValidar) {
        boolean b_validar = false;
        for (int i = 0; i < objListVacaValidar.size(); i++) {
            if (validarRangoFecha(d_fecini.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else if (validarRangoFecha(d_fecfin.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else {
                b_validar = true;
            }

        }
        return b_validar;
    }

    public boolean validaFechaVacacionesModificar(ListModelList<Vacaciones> objListVacaValidar) {
        boolean b_validar = true;
        for (int i = 0; i < i_selecDetalle; i++) {
            if (validarRangoFecha(d_fecini.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else if (validarRangoFecha(d_fecfin.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else {
                b_validar = true;
            }

        }

        for (int i = i_selecDetalle + 1; i < objListVacaValidar.size(); i++) {
            if (validarRangoFecha(d_fecini.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else if (validarRangoFecha(d_fecfin.getValue(), objListVacaValidar.get(i).getFechainicio(), objListVacaValidar.get(i).getFechafin())) {
                b_validar = false;
                break;
            } else {
                b_validar = true;
            }

        }

        return b_validar;
    }

    boolean validarRangoFecha(Date d_validar, Date d_fecha1, Date fecha2) {
        return !(d_validar.before(d_fecha1) || d_validar.after(fecha2));
    }
	
    public int diasDiferencia(Date fechaini, Date fechafin) {
        // i_dias = Days.daysBetween(new LocalDate(d_dia_ahora), new LocalDate(objPlaContrato.getPar15_confecfin().getTime())).getDays(); 

        return Days.daysBetween(new LocalDate(fechaini), new LocalDate(fechafin.getTime())).getDays();
    }
}
