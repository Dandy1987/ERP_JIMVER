/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import fr.opensagres.xdocreport.core.XDocReportException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.planillas.mantenimiento.DaoContrato;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.AsistenciaDias;
import org.me.gj.model.planillas.procesos.AsistenciaGen;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarModel;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author wcamacho
 */
public class ControllerAsistenciaGen extends SelectorComposer<Component> {

    @Wire
    Tab tab_listasistenciagen, tab_mantenimiento;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_nuevoasis, tbbtn_btn_editarasis, tbbtn_btn_eliminarasis, tbbtn_btn_guardarasis, tbbtn_btn_deshacerasis;
    @Wire
    Calendars calendars;

    @Wire
    Combobox cb_sucursal, cb_periodo, cb_area;

    @Wire
    Textbox txt_codigo, txt_apenom, txt_mcodper, txt_mdesper, txt_mes, txt_usuadd, txt_usumod, txt_asistencia, txt_periodo,
            //opara lov
            txt_lovfalta, txt_lovliccgoce, txt_lovlicsgoce, txt_lovdesmedico, txt_lovlicenfermedad,
            txt_lovpaternidad;
    CalendarModel calendarioModelo;

    @Wire
    Listbox lst_asistenciagen, lst_detasis;

    @Wire
    Datebox d_fecact, d_fecadd, d_fecmod, d_fecini, d_fecfin;

    @Wire
    Radiogroup rbg_asisgen;

    @Wire
    Radio rb_0, rb_1, rb_2, rb_3, rb_4, rb_5, rb_6, rb_7, rb_8, rb_9, rb_11, rb_12;

    @Wire
    Button btn_consultar;

    @Wire
    Listcell l1;

    //Instancias de objetos
    ListModelList<AsistenciaGen> objlstAsistenciaGen;
    ListModelList<AsistenciaGen> objlstAsistenciaGenDet;
    AsistenciaGen objAsistenciaGen, objDia;
    DaoAsistenciaGen objdaoAsistenciaGen;

    ListModelList<Sucursales> objlstSucursal;
    ListModelList<Sucursales> objlstSucursales;
	
	String s_tipoAsistencia = "";
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ManAreas objAreas = new ManAreas();
    DaoManAreas objDaoAreas = new DaoManAreas();
    DaoMovLinea objDaoMovLinea;
    DaoContrato objDaoContrato;
    DaoCliente objDaoCliente;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;

    DaoManPer objDaoPeriodo;
    ListModelList<ManPer> objlstPeriodos;
    ManPer objManPer;

    ListModelList<AsistenciaGen> objlstLov = new ListModelList<AsistenciaGen>();
    //LOV PERSONAL
    ListModelList<Personal> objlstPersonal;
    Personal objPersonal;
    DaoPersonal objDaoPersonal = new DaoPersonal();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat convert = new SimpleDateFormat("yyyyMM");
    int i_sel, emp_id, suc_id, i_num_descansos, i_dias_selecc, i_cuenta_lista_sele;
    String foco = "";
	int vacTotal = 0;
    ListModelList<Integer> objlistSelect = new ListModelList<Integer>();
	boolean b_vaolidaseleccion = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerAsistenciaGen.class);
    public static boolean bandera = false;
    private String s_estado = "";
    private String s_mensaje = "";

    boolean b_modificado = false;

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_asistenciagen")
    public void llenaRegistros() throws SQLException {
        objlstAsistenciaGenDet = null;
        objlstAsistenciaGenDet = new ListModelList<AsistenciaGen>();

        objDaoMovLinea = new DaoMovLinea();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        cb_periodo.setValue(Utilitarios.periodoActual());

        objdaoAsistenciaGen = new DaoAsistenciaGen();
        objlstAsistenciaGen = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
        lst_asistenciagen.setModel(objlstAsistenciaGen);//guarda un registro

        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        cb_sucursal.setModel(objlstSucursal);

        cb_periodo.setModel(new DaoManPer().listaPeriodosDinamico(0));

        /* objDaoAreas = new DaoManAreas();
         objlstAreas = objDaoAreas.listaAreas(1);
         cb_area.setModel(objlstAreas);*/
        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
		
		i_cuenta_lista_sele = 0;
        i_dias_selecc = 0;
        rbg_asisgen.setSelectedItem(null);
        limpiarFechas();
		/*
		int i_valida = objdaoAsistenciaGen.validaCambioAsistencia(cb_periodo.getValue().toString());
        if (i_valida == 0) {
            tbbtn_btn_editar.setDisabled(true);
        } else {
            tbbtn_btn_editar.setDisabled(false);
        }
*/
    }

     @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90204000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Asistencia General con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Asistencia General con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Asistencia General");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Asistencia General");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Asistencia General");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Asistencia General");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Asistencia General");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Asistencia General");
        }
    }
	
    @Listen("onSelect=#lst_asistenciagen")
    public void seleccionaRegistroCab() throws SQLException {
        limpiarCampos();
        objAsistenciaGen = (AsistenciaGen) lst_asistenciagen.getSelectedItem().getValue();
        if (objAsistenciaGen == null) {
            objAsistenciaGen = objlstAsistenciaGen.get(i_sel + 1);//aqui captura un objeto
        }
        i_sel = lst_asistenciagen.getSelectedIndex();
        //habilitaCampos(true);
        txt_periodo.setDisabled(true);
        txt_asistencia.setDisabled(true);
        txt_mes.setValue(objAsistenciaGen.getMes());
        LimpiarListaDetalle();
        llenarCabecera(objAsistenciaGen);
        llenarListaDetalle();
        habilitaRadioGroup(true);
        objlistSelect = new ListModelList<Integer>();
        //   l1.setStyle("background-color: yellow");

    }

    @Listen("onSelect=#lst_detasis")
    public void seleccionaRegistroDet() throws SQLException {
        objDia = (AsistenciaGen) lst_detasis.getSelectedItem().getValue();
        if (objDia == null) {
            objDia = objlstAsistenciaGenDet.get(i_sel + 1);
        }
        i_sel = lst_detasis.getSelectedIndex();
        // lst_detasis.getSelectedItem().setStyle("background-color: yellow");
        // d_fecini.setValue(objDia.getFec_asis());
        d_fecact.setValue(objDia.getFec_asis());
        txt_asistencia.setValue(objDia.getAsistencia());
        txt_periodo.setValue(objDia.getPeriodo());

    }

	@Listen("onSeleccion=#lst_detasis")
    public void seleccionaRegistro(ForwardEvent evt) {
		
        b_vaolidaseleccion = false;
        if (tbbtn_btn_editar.isDisabled() && !tbbtn_btn_guardar.isDisabled()) {
            if (d_fecini.getValue() != null || d_fecfin.getValue() != null) {
                Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
                chk_Reg.setChecked(false);
            } else {
                Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
                Listitem item = (Listitem) chk_Reg.getParent().getParent();

                int i_index = 0;
                // evt.getOrigin().
                int i_inder = item.getIndex();
                if (chk_Reg.isChecked()) {
                    objlistSelect.add(i_inder);
                    Object obj = i_inder;
                } else {
                    for (int i = 0; i < objlistSelect.size(); i++) {
                        if (objlistSelect.get(i) == i_inder) {
                            i_index = i;
                            break;
                        }

                    }
                    objlistSelect.remove(i_index);
                }
                if (objlistSelect.size() > 0) {
                    d_fecini.setDisabled(true);
                    d_fecfin.setDisabled(true);
                    b_vaolidaseleccion = true;
                } else {
                    d_fecini.setDisabled(false);
                    d_fecfin.setDisabled(false);
                    b_vaolidaseleccion = false;
                }
            }
            /*lst_detasis.setSelectedIndex(5);
             lst_detasis.getSelectedItem().setStyle("background-color: yellow");*/
        } else {

            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            chk_Reg.setChecked(false);
        }

    }
	
    /**
     * Metodo de seleciona falta
     *
     * @autor junior fernandez
     * @version 31/08/2017
     */
    @Listen("onClick=#rb_1")
    public void seleccionaFalta() {

        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            //seleccion();
            //devuelveSelect();
			s_tipoAsistencia = "1";
            lov("1", txt_lovfalta);//lov para ver detalle

            /* s_mensaje = "Esta seguro que desea cambiar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {

             devuelveSelect();
                        
             lov("1", txt_lovfalta);//lov para ver detalle
                       
             }
             }
             });*/
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("FALTA", 0);
            } else {
                cambiaEstadoAsistencia("FALTA", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
        // rbg_asisgen.setSelectedItem(null);
        //lst_detasis.focus();

    }

    @Listen("onSelect=#cb_periodo")
    public void saltoCombo() throws SQLException {
        busquedaRegistros();
    }

    @Listen("onClick=#rb_0")
    public void seleccionaAsistencia() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            //seleccion();
            //devuelveSelect();
         /*s_mensaje = "Esta seguro que desea cambiar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             //  limpiarFechas();
             }
             }
             });*/
			s_tipoAsistencia = "0";
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("ASISTIO", 0);
            } else {
                cambiaEstadoAsistencia("ASISTIO", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
        // rbg_asisgen.setSelectedItem(null);
        //  lst_detasis.focus();

    }

    @Listen("onClick=#rb_5")
    public void seleccionaCGoce() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            // seleccion();
            //devuelveSelect();
            // limpiarFechas();
            lov("5", txt_lovliccgoce);//lov para ver detalle
           /* s_mensaje = "Esta seguro que desea cambiar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             // limpiarFechas();
             lov("5", txt_lovliccgoce);//lov para ver detalle
             }
             }
             });*/
			s_tipoAsistencia = "5";
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("LIC. C. GOCE", 0);
            } else {
                cambiaEstadoAsistencia("LIC. C. GOCE", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
        //    rbg_asisgen.setSelectedItem(null);
        //    lst_detasis.focus();
    }

    @Listen("onClick=#rb_6")
    public void seleccionaSGoce() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                        devolverFocus();

                    }
                }
            });
        } else {
            lov("6", txt_lovlicsgoce);//lov para ver detalle
			
			s_tipoAsistencia = "6";
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("LIC. S. GOCE", 0);
            } else {
                cambiaEstadoAsistencia("LIC. S. GOCE", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }
        }
    }

    @Listen("onClick=#rb_2")
    public void seleccionaVacaciones() throws SQLException {
        String valida = verificaFecha();
        int tiempo = 0;
		
        if (!valida.isEmpty() && objlistSelect.isEmpty()) {
            Messagebox.show("Seleccione un registro o una fecha inicial", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });

        } else {
            if (d_fecini.getValue() == null) {

                tiempo = obtenerTiempo(objlstAsistenciaGenDet.get(objlistSelect.get(0)).getFec_asis(), objAsistenciaGen.getPer_fecing());

            } else {
                tiempo = obtenerTiempo(d_fecini.getValue(), objAsistenciaGen.getPer_fecing());
            }            
            if (tiempo < 365) {
                Messagebox.show("Trabajador aun no cumple un año en la empresa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
				s_tipoAsistencia = "2";
                if (!valida.isEmpty()) {

                    int i_vac = objdaoAsistenciaGen.getVacxMes(objAsistenciaGen.getPeriodo(), objAsistenciaGen.getPlnrodoc());

                    i_vac = i_vac + objlistSelect.size();
                    vacTotal = vacTotal + i_vac;
                    if (vacTotal > 30) {
                        Messagebox.show("No puede salir de vacaciones más de 30 días", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {

                        cambiaEstadoAsistencia("VACACIONES", 0);
                    }
                } else {
                    int i_vac = objdaoAsistenciaGen.getVacxMes(objAsistenciaGen.getPeriodo(), objAsistenciaGen.getPlnrodoc());
                    i_vac = i_vac + (diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()) + 1);
                    vacTotal = vacTotal + i_vac;
                    if (vacTotal > 30) {
                        Messagebox.show("No puede salir de vacaciones más de 30 días", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {

                        cambiaEstadoAsistencia("VACACIONES", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));

                    }
                    /*
                     
                     */

                }

            }
        }
		
    }
         
    @Listen("onClick=#rb_7")
    public void seleccionaCompensacion() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
			s_tipoAsistencia = "7";
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("COMPENSACION", 0);
            } else {
                cambiaEstadoAsistencia("COMPENSACION", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
    }

    @Listen("onClick=#rb_3")
    public void seleccionaPreNatal() throws SQLException {
        int sexo = objdaoAsistenciaGen.getSexo(objAsistenciaGen.getPltipdoc(), objAsistenciaGen.getPlnrodoc());
        if (sexo == 1) {
            Messagebox.show("El sexo debe ser femenino", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
			
            String valida = verificaFecha();
            if (!valida.isEmpty()) {
                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            devolverFocus();
                        }
                    }
                });
            } else {
				s_tipoAsistencia = "3";
                if (!valida.isEmpty()) {
                    cambiaEstadoAsistencia("PRE NATAL", 0);
                } else {
                    cambiaEstadoAsistencia("PRE NATAL", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
                }

            }
        }
    }

    @Listen("onClick=#rb_8")
    public void seleccionaDesMedico() {

        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            //lov para ver detalle
            if (!valida.isEmpty()) {
                if (i_num_descansos + objlistSelect.size() + i_dias_selecc > 20) {
                    Messagebox.show("No puede tener más de 20 días de descanso médico en el año", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    rb_8.setChecked(false);
                } else {
					s_tipoAsistencia = "8";
                    i_cuenta_lista_sele = objlistSelect.size();
                    lov("8", txt_lovdesmedico);
                    cambiaEstadoAsistencia("DESC. MEDICO", 0);

                }
            } else {
                if ((i_num_descansos + diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()) + 1) + i_cuenta_lista_sele > 20) {
                    Messagebox.show("No puede tener más de 20 días de descanso médico en el año", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    rb_8.setChecked(false);
                } else {
					s_tipoAsistencia = "8";
                    i_dias_selecc = diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()) + 1;
                    lov("8", txt_lovdesmedico);
                    cambiaEstadoAsistencia("DESC. MEDICO", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));

                }
			}
        }
    }

    @Listen("onClick=#rb_4")
    public void seleccionaPostNatal() throws SQLException {
        int sexo = objdaoAsistenciaGen.getSexo(objAsistenciaGen.getPltipdoc(), objAsistenciaGen.getPlnrodoc());
        if (sexo == 1) {
            Messagebox.show("El sexo debe ser femenino", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String valida = verificaFecha();
            if (!valida.isEmpty() && objlistSelect.size() == 0) {
                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            devolverFocus();
                        }
                    }
                });
            } else {
                // seleccion();
                // devuelveSelect();
                /* s_mensaje = "Esta seguro que desea cambiar la asistencia?";
                 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                 @Override
                 public void onEvent(Event t) throws Exception {
                 if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                 devuelveSelect();
                 // limpiarFechas();
                 }
                 }
                 });*/
				s_tipoAsistencia = "4";
                if (!valida.isEmpty()) {
                    cambiaEstadoAsistencia("POST NATAL", 0);
                } else {
                    cambiaEstadoAsistencia("POST NATAL", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
                }

            }
        }
        // rbg_asisgen.setSelectedItem(null);
        //  lst_detasis.focus();
    }

    @Listen("onClick=#rb_9")
    public void seleccionaSubsidio() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            //seleccion();
            //devuelveSelect();
            /*s_mensaje = "Esta seguro que desea cambiar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             // limpiarFechas();
             }
             }
             });*/
			s_tipoAsistencia = "9";
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("DESC. M. SUBSIDIO", 0);
            } else {
                cambiaEstadoAsistencia("DESC. M. SUBSIDIO", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
        // rbg_asisgen.setSelectedItem(null);
        // lst_detasis.focus();
    }

    @Listen("onClick=#rb_11")
    public void seleccionaPaternidad() throws SQLException {
        int sexo = objdaoAsistenciaGen.getSexo(objAsistenciaGen.getPltipdoc(), objAsistenciaGen.getPlnrodoc());
        if (sexo == 2) {
            Messagebox.show("El sexo debe ser masculino", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String valida = verificaFecha();
            if (!valida.isEmpty() && objlistSelect.size() == 0) {
                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            devolverFocus();
                        }
                    }
                });
            } else {
                // seleccion();
                //devuelveSelect();
                // limpiarFechas();
				s_tipoAsistencia = "A";
                lov("A", txt_lovpaternidad);//lov para ver detalle
                /*s_mensaje = "Esta seguro que desea cambiar la asistencia?";
                 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                 @Override
                 public void onEvent(Event t) throws Exception {
                 if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                 devuelveSelect();
                 // limpiarFechas();
                 lov("A", txt_lovpaternidad);//lov para ver detalle
                 }
                 }
                 });*/
                if (!valida.isEmpty()) {
                    cambiaEstadoAsistencia("LIC. PATERNIDAD", 0);
                } else {
                    cambiaEstadoAsistencia("LIC. PATERNIDAD", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
                }

            }
        }
        // rbg_asisgen.setSelectedItem(null);
        //  lst_detasis.focus();
    }

    @Listen("onClick=#rb_12")
    public void seleccionaEnfermedad() {
        String valida = verificaFecha();
        if (!valida.isEmpty() && objlistSelect.size() == 0) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            // seleccion();
            //devuelveSelect();
            //  limpiarFechas();
			s_tipoAsistencia = "B";
            lov("B", txt_lovlicenfermedad);//lov para ver detalle
         /*   s_mensaje = "Esta seguro que desea cambiar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             //  limpiarFechas();
             lov("B", txt_lovlicenfermedad);//lov para ver detalle
             }
             }
             });*/
            if (!valida.isEmpty()) {
                cambiaEstadoAsistencia("LIC. ENF. TERMINAL", 0);
            } else {
                cambiaEstadoAsistencia("LIC. ENF. TERMINAL", diasDiferencia(d_fecini.getValue(), d_fecfin.getValue()));
            }

        }
        // rbg_asisgen.setSelectedItem(null); limpia radiobutton
        //  lst_detasis.focus();
    }
	
        public void devuelveSelect() {
        String asistencia = "";
        String asistencia2 = "";
        asistencia = txt_asistencia.getValue();
        if (txt_asistencia.getValue().equals("ASISTIO")) {
            asistencia2 = "0";
        } else if (txt_asistencia.getValue().equals("LIC. C. GOCE")) {
            asistencia2 = "5";
        } else if (txt_asistencia.getValue().equals("FALTA")) {
            asistencia2 = "1";
        } else if (txt_asistencia.getValue().equals("LIC. S. GOCE")) {
            asistencia2 = "6";
        } else if (txt_asistencia.getValue().equals("VACACIONES")) {
            asistencia2 = "2";
        } else if (txt_asistencia.getValue().equals("COMPENSACION")) {
            asistencia2 = "7";
        } else if (txt_asistencia.getValue().equals("PRE NATAL")) {
            asistencia2 = "3";
        } else if (txt_asistencia.getValue().equals("DESC. MEDICO")) {
            asistencia2 = "8";
        } else if (txt_asistencia.getValue().equals("POST NATAL")) {
            asistencia2 = "4";
        } else if (txt_asistencia.getValue().equals("DESC. M. SUBSIDIO")) {
            asistencia2 = "9";
        } else if (txt_asistencia.getValue().equals("LIC. PATERNIDAD")) {
            asistencia2 = "A";
        } else if (txt_asistencia.getValue().equals("LIC. ENF. TERMINAL")) {
            asistencia2 = "B";
        }

        Date dia_ini = d_fecini.getValue();
        Date dia_fin = d_fecfin.getValue();

        int dia_i = Integer.parseInt(StringFns.substring(sdf.format(dia_ini), 0, 2));
        int dia_f = Integer.parseInt(StringFns.substring(sdf.format(dia_fin), 0, 2));
        for (int i = dia_i; i <= dia_f; i++) {
            String d = objlstAsistenciaGenDet.get(i - 1).getSfec_asisdias();//sfec_asisdias dia
            String o = objlstAsistenciaGenDet.get(i - 1).getSfec_asis();//fecha
            Date c = objlstAsistenciaGenDet.get(i - 1).getFec_asis();
            //String sub = objlstAsistenciaGenDet.get(i - 1).getCod_subsidio();
            //String glosa = objlstAsistenciaGenDet.get(i - 1).getGlosa();
            //String codigo_lov = objlstAsistenciaGenDet.get(i-1).get
            pintarLista("" + i, asistencia2, asistencia, d, o, c);
        }
    }

    public void pintarLista(String a, String asistencia2, String asistencia, String d, String o, Date c) {
        if (a.equals("1")) {
            objAsistenciaGen.setPldia01(asistencia2);
        } else if (a.equals("2")) {
            objAsistenciaGen.setPldia02(asistencia2);
        } else if (a.equals("3")) {
            objAsistenciaGen.setPldia03(asistencia2);
        } else if (a.equals("4")) {
            objAsistenciaGen.setPldia04(asistencia2);
        } else if (a.equals("5")) {
            objAsistenciaGen.setPldia05(asistencia2);
        } else if (a.equals("6")) {
            objAsistenciaGen.setPldia06(asistencia2);
        } else if (a.equals("7")) {
            objAsistenciaGen.setPldia07(asistencia2);
        } else if (a.equals("8")) {
            objAsistenciaGen.setPldia08(asistencia2);
        } else if (a.equals("9")) {
            objAsistenciaGen.setPldia09(asistencia2);
        } else if (a.equals("10")) {
            objAsistenciaGen.setPldia10(asistencia2);
        } else if (a.equals("11")) {
            objAsistenciaGen.setPldia11(asistencia2);
        } else if (a.equals("12")) {
            objAsistenciaGen.setPldia12(asistencia2);
        } else if (a.equals("13")) {
            objAsistenciaGen.setPldia13(asistencia2);
        } else if (a.equals("14")) {
            objAsistenciaGen.setPldia14(asistencia2);
        } else if (a.equals("15")) {
            objAsistenciaGen.setPldia15(asistencia2);
        } else if (a.equals("16")) {
            objAsistenciaGen.setPldia16(asistencia2);
        } else if (a.equals("17")) {
            objAsistenciaGen.setPldia17(asistencia2);
        } else if (a.equals("18")) {
            objAsistenciaGen.setPldia18(asistencia2);
        } else if (a.equals("19")) {
            objAsistenciaGen.setPldia19(asistencia2);
        } else if (a.equals("20")) {
            objAsistenciaGen.setPldia20(asistencia2);
        } else if (a.equals("21")) {
            objAsistenciaGen.setPldia21(asistencia2);
        } else if (a.equals("22")) {
            objAsistenciaGen.setPldia22(asistencia2);
        } else if (a.equals("23")) {
            objAsistenciaGen.setPldia23(asistencia2);
        } else if (a.equals("24")) {
            objAsistenciaGen.setPldia24(asistencia2);
        } else if (a.equals("25")) {
            objAsistenciaGen.setPldia25(asistencia2);
        } else if (a.equals("26")) {
            objAsistenciaGen.setPldia26(asistencia2);
        } else if (a.equals("27")) {
            objAsistenciaGen.setPldia27(asistencia2);
        } else if (a.equals("28")) {
            objAsistenciaGen.setPldia28(asistencia2);
        } else if (a.equals("29")) {
            objAsistenciaGen.setPldia29(asistencia2);
        } else if (a.equals("30")) {
            objAsistenciaGen.setPldia30(asistencia2);
        } else if (a.equals("31")) {
            objAsistenciaGen.setPldia31(asistencia2);
        }

        objDia = new AsistenciaGen();
        objDia.setFec_asis(c);
        objDia.setSfec_asisdias(d);///dia
        objDia.setAsistencia(asistencia);//tipo
        objDia.setSfec_asis(o);//fecha

        objlstAsistenciaGenDet.set(Integer.parseInt(a) - 1, objDia);//lst_detasis.getSelectedIndex()

    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
		
        s_mensaje = "Está seguro que desea guardar los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                            ParametrosSalida objParamAsis;

                            if (s_estado.equals("M")) {
                                objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGenDet), getLov(objlstLov));//
                            } else {
                                //    objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGenDet));}
                                objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGenDet), getLov(objlstLov));//
                            }

                            if (objParamAsis.getFlagIndicador() == 0) {
//                          
                                LimpiarListaDetalle();
                            }
                            Messagebox.show(objParamAsis.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            limpiarCampos();
                            limpiaAuditoria();
                            habilitaRadioGroup(true);
                            limpiarFechas();
                            limpiarCamposLoc();
							d_fecini.setDisabled(true);
                            d_fecfin.setDisabled(true);
                            rbg_asisgen.setSelectedItem(null);
                            // objlstAsistenciaGen = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
                            objlstAsistenciaGen = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), cb_periodo.getValue());
                            lst_asistenciagen.setModel(objlstAsistenciaGen);
                            objAsistenciaGen = new AsistenciaGen();
                            lst_asistenciagen.focus();
                            s_estado = "Q";
							i_cuenta_lista_sele = 0;
                            i_dias_selecc = 0;

                        }
                    }
                });

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_asistenciagen.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objdaoAsistenciaGen.validaPeriodoCalculado(cb_periodo.getValue().toString()) == 1) {
                Messagebox.show("La planilla se encuentra calculando", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {

                int i_valida = objdaoAsistenciaGen.validaPeriodoProceso(cb_periodo.getValue().toString());
                if (i_valida == 1) {
                    objlstLov = null;
                    objlstLov = new ListModelList<AsistenciaGen>();
                    s_estado = "M";
                    i_num_descansos = objdaoAsistenciaGen.getDescansoMedico(objAsistenciaGen.getPeriodo(), objAsistenciaGen.getId_per());
                    habilitaBotones(true, false);
                    habilitaTab(true, false);
                    seleccionaTab(false, true);
                    txt_asistencia.setDisabled(true);
                    habilitaRadioGroup(false);
                    limpiarFechas();
                    rbg_asisgen.setSelectedItem(null);
                    if (b_vaolidaseleccion == false) {
                        d_fecini.setDisabled(false);
                        d_fecfin.setDisabled(false);
                    }
                } else {
                    Messagebox.show("El periodo no se encuentra en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }
            }

        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() throws SQLException {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_asistenciagen.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            lst_asistenciagen.focus();
                            //
                            //habilitaCampos(true);
                            limpiaAuditoria();
                            LimpiarListaDetalle();
                            habilitaRadioGroup(true);
                            limpiarFechas();
                            rbg_asisgen.setSelectedItem(null);
                            s_estado = "Q";
							d_fecini.setDisabled(true);
                            d_fecfin.setDisabled(true);

                        }
                    }
                });

    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        objlstAsistenciaGen = new ListModelList<AsistenciaGen>();
        LimpiarListaCabecera();

        // String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedIndex() == 0 || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
        String sucursal = cb_sucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();
        // String area = cb_area.getSelectedIndex() == -1 || cb_area.getSelectedIndex() == 0 ? "" : cb_area.getSelectedItem().getValue().toString();
        String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
        String codigo = txt_codigo.getValue();

        objlstAsistenciaGen = objdaoAsistenciaGen.consultarAsistenciasGen(sucursal, periodo, area, codigo);

        if (objlstAsistenciaGen.getSize() > 0) {
            lst_asistenciagen.setModel(objlstAsistenciaGen);
        } else {
            /*objlstAsistenciaGen = null;
             lst_asistenciagen.setModel(objlstAsistenciaGen);*/
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            //cb_area.setValue(null);
        }

        limpiarCampos();
        limpiaAuditoria();
		/*
        int i_valida = objdaoAsistenciaGen.validaCambioAsistencia(cb_periodo.getSelectedItem().getValue().toString());
        if (i_valida == 0) {
            tbbtn_btn_editar.setDisabled(true);
        } else {
            tbbtn_btn_editar.setDisabled(false);
        }
*/
    }

    public void llenarCabecera(AsistenciaGen objAsistenciaGen) throws SQLException {
        txt_mcodper.setValue(objAsistenciaGen.getId_per());
        txt_mdesper.setValue(objAsistenciaGen.getDes_per());
        txt_usuadd.setValue(objAsistenciaGen.getPlas_usuadd());
        d_fecadd.setValue(objAsistenciaGen.getPlas_fecadd());
        txt_usumod.setValue(objAsistenciaGen.getPlas_usumod());
        d_fecmod.setValue(objAsistenciaGen.getPlas_fecmod());

    }

    public void llenarListaDetalle() throws SQLException {
        objlstAsistenciaGenDet = null;
        if (!b_modificado) {
            objlstAsistenciaGenDet = objdaoAsistenciaGen.listaDetalle(objUsuCredential.getCodemp(), objAsistenciaGen.getSuc_id(), objAsistenciaGen.getId_per(), objAsistenciaGen.getPeriodo());
        } else {
            objlstAsistenciaGenDet = objlstAsistenciaGenDet;
        }
//lst_detasis.setModel(objlstAsistenciaGenDet);//se llena con todos los datos
        String x = objAsistenciaGen.getPeriodo();
        //String ani = x.substring(0,4);
        // String ms = x.substring(4,6);
        int ani = Integer.parseInt(x.substring(0, 4));
        int ms = Integer.parseInt(x.substring(4, 6));

        int ultimo_dia = obtenerUltimoDia(ani, ms);

        if (ultimo_dia == 30) {
            objlstAsistenciaGenDet.remove(30);

        } else if (ultimo_dia == 29) {
            objlstAsistenciaGenDet.remove(30);
            objlstAsistenciaGenDet.remove(29);

        } else if (ultimo_dia == 28) {
            objlstAsistenciaGenDet.remove(30);
            objlstAsistenciaGenDet.remove(29);
            objlstAsistenciaGenDet.remove(28);

        }
        lst_detasis.setModel(objlstAsistenciaGenDet);

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objAsistenciaGen == null) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String x = objAsistenciaGen.getPeriodo();
            int ani = Integer.parseInt(x.substring(0, 4));
            int ms = Integer.parseInt(x.substring(4, 6));

            int sucursal = objAsistenciaGen.getSuc_id();
            int empresa = objUsuCredential.getCodemp();
            String codigo = objAsistenciaGen.getId_per();
            String periodo = objAsistenciaGen.getPeriodo();
            int ultimo_dia = obtenerUltimoDia(ani, ms);

            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", empresa);
            objMapObjetos.put("sucursal", sucursal);
            objMapObjetos.put("codigo", codigo);
            objMapObjetos.put("periodo", periodo);
            objMapObjetos.put("ultimo_dia", ultimo_dia);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/procesos/LovImpresionProcesosAsistencia.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    //obtener ultimo dia de mes
    public int obtenerUltimoDia(int anio, int mes) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes - 1, 1);
        return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    ////clase diferencia
    public int obtenerTiempo(Date fecha, Date fechatravajo) {
        long d = fecha.getTime() - fechatravajo.getTime();
        long dias = d / (1000 * 60 * 60 * 24);
        return (int) dias;

    }

    public void LimpiarListaDetalle() {
        //limpio mi lista
        objlstAsistenciaGen = null;
        objlstAsistenciaGen = new ListModelList<AsistenciaGen>();
        lst_detasis.setModel(objlstAsistenciaGen);
    }

    public void LimpiarListaCabecera() {
        objlstAsistenciaGen = null;
        objlstAsistenciaGen = new ListModelList<AsistenciaGen>();
        lst_asistenciagen.setModel(objlstAsistenciaGen);

    }

    public void limpiarCampos() {
        txt_mcodper.setValue("");
        txt_mdesper.setValue("");
        txt_mes.setValue("");
        d_fecact.setValue(null);
        txt_asistencia.setValue("");
        txt_periodo.setValue("");

    }

    public void habilitaRadioGroup(boolean b_valida1) {
        rb_0.setDisabled(b_valida1);
        rb_1.setDisabled(b_valida1);
        rb_2.setDisabled(b_valida1);
        rb_3.setDisabled(b_valida1);
        rb_4.setDisabled(b_valida1);
        rb_5.setDisabled(b_valida1);
        rb_6.setDisabled(b_valida1);
        rb_7.setDisabled(b_valida1);
        rb_8.setDisabled(b_valida1);
        rb_9.setDisabled(b_valida1);
        rb_11.setDisabled(b_valida1);
        rb_12.setDisabled(b_valida1);
        //d_fecini.setDisabled(b_valida1);
        //d_fecfin.setDisabled(b_valida1);

    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
		tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasistenciagen.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasistenciagen.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public Object[][] getLov(ListModelList<AsistenciaGen> x) {
        ListModelList<AsistenciaGen> objListaDepurada3;

        objListaDepurada3 = x;

        Object[][] listaLov = new Object[objListaDepurada3.size()][17];
        for (int i = 0; i < objListaDepurada3.size(); i++) {
            listaLov[i][0] = objUsuCredential.getCodemp();
            listaLov[i][1] = objAsistenciaGen.getSuc_id();
            listaLov[i][2] = objAsistenciaGen.getPeriodo();//objListaAsis.get(i).getPeriodo();
            listaLov[i][3] = objAsistenciaGen.getPltipdoc();//objListaAsis.get(i).getPltipdoc();
            listaLov[i][4] = objAsistenciaGen.getPlnrodoc();///objListaAsis.get(i).getPlnrodoc();
            listaLov[i][5] = "N";
            listaLov[i][6] = objListaDepurada3.get(i).getCod_subsidio();
            listaLov[i][7] = "";
            listaLov[i][8] = new java.sql.Date(objListaDepurada3.get(i).getFinicio().getTime());///d_fecini.getValue();
            listaLov[i][9] = new java.sql.Date(objListaDepurada3.get(i).getFfinal().getTime());//d_fecfin.getValue();
            listaLov[i][10] = "";
            listaLov[i][11] = "";
            listaLov[i][12] = objUsuCredential.getCuenta();
            listaLov[i][13] = objUsuCredential.getComputerName();
            listaLov[i][14] = objListaDepurada3.get(i).getGlosa();
			if (s_tipoAsistencia.equals("1")) {
                listaLov[i][15] = objListaDepurada3.get(i).getTipoasistencia();
				
            } else {
                listaLov[i][15] = 0;
            }
            if (s_tipoAsistencia.equals("8")) {
                listaLov[i][16] = objListaDepurada3.get(i).getTipodescanso();
            } else {
                listaLov[i][16] = 0;
            }
        }
        return listaLov;
    }

    public Object[][] getDetalle(ListModelList<AsistenciaGen> x) {
        ListModelList<AsistenciaGen> objListaAsis;

        objListaAsis = x;
        Object[][] listaDet = new Object[objListaAsis.size()][39];

        for (int i = 0; i < 1; i++) {
            listaDet[i][0] = objUsuCredential.getCodemp();
            listaDet[i][1] = objAsistenciaGen.getSuc_id();//objUsuCredential.getCodsuc();
            listaDet[i][2] = objAsistenciaGen.getPeriodo();//objListaAsis.get(i).getPeriodo();
            listaDet[i][3] = objAsistenciaGen.getPltipdoc();//objListaAsis.get(i).getPltipdoc();
            listaDet[i][4] = objAsistenciaGen.getPlnrodoc();///objListaAsis.get(i).getPlnrodoc();
            listaDet[i][5] = objAsistenciaGen.getPldia01() == null ? "0" : objAsistenciaGen.getPldia01();
            listaDet[i][6] = objAsistenciaGen.getPldia02() == null ? "0" : objAsistenciaGen.getPldia02();
            listaDet[i][7] = objAsistenciaGen.getPldia03() == null ? "0" : objAsistenciaGen.getPldia03();
            listaDet[i][8] = objAsistenciaGen.getPldia04() == null ? "0" : objAsistenciaGen.getPldia04();
            listaDet[i][9] = objAsistenciaGen.getPldia05() == null ? "0" : objAsistenciaGen.getPldia05();
            listaDet[i][10] = objAsistenciaGen.getPldia06() == null ? "0" : objAsistenciaGen.getPldia06();
            listaDet[i][11] = objAsistenciaGen.getPldia07() == null ? "0" : objAsistenciaGen.getPldia07();
            listaDet[i][12] = objAsistenciaGen.getPldia08() == null ? "0" : objAsistenciaGen.getPldia08();
            listaDet[i][13] = objAsistenciaGen.getPldia09() == null ? "0" : objAsistenciaGen.getPldia09();
            listaDet[i][14] = objAsistenciaGen.getPldia10() == null ? "0" : objAsistenciaGen.getPldia10();
            listaDet[i][15] = objAsistenciaGen.getPldia11() == null ? "0" : objAsistenciaGen.getPldia11();
            listaDet[i][16] = objAsistenciaGen.getPldia12() == null ? "0" : objAsistenciaGen.getPldia12();
            listaDet[i][17] = objAsistenciaGen.getPldia13() == null ? "0" : objAsistenciaGen.getPldia13();
            listaDet[i][18] = objAsistenciaGen.getPldia14() == null ? "0" : objAsistenciaGen.getPldia14();
            listaDet[i][19] = objAsistenciaGen.getPldia15() == null ? "0" : objAsistenciaGen.getPldia15();
            listaDet[i][20] = objAsistenciaGen.getPldia16() == null ? "0" : objAsistenciaGen.getPldia16();
            listaDet[i][21] = objAsistenciaGen.getPldia17() == null ? "0" : objAsistenciaGen.getPldia17();
            listaDet[i][22] = objAsistenciaGen.getPldia18() == null ? "0" : objAsistenciaGen.getPldia18();
            listaDet[i][23] = objAsistenciaGen.getPldia19() == null ? "0" : objAsistenciaGen.getPldia19();
            listaDet[i][24] = objAsistenciaGen.getPldia20() == null ? "0" : objAsistenciaGen.getPldia20();
            listaDet[i][25] = objAsistenciaGen.getPldia21() == null ? "0" : objAsistenciaGen.getPldia21();
            listaDet[i][26] = objAsistenciaGen.getPldia22() == null ? "0" : objAsistenciaGen.getPldia22();
            listaDet[i][27] = objAsistenciaGen.getPldia23() == null ? "0" : objAsistenciaGen.getPldia23();
            listaDet[i][28] = objAsistenciaGen.getPldia24() == null ? "0" : objAsistenciaGen.getPldia24();
            listaDet[i][29] = objAsistenciaGen.getPldia25() == null ? "0" : objAsistenciaGen.getPldia25();
            listaDet[i][30] = objAsistenciaGen.getPldia26() == null ? "0" : objAsistenciaGen.getPldia26();
            listaDet[i][31] = objAsistenciaGen.getPldia27() == null ? "0" : objAsistenciaGen.getPldia27();
            listaDet[i][32] = objAsistenciaGen.getPldia28() == null ? "0" : objAsistenciaGen.getPldia28();
            listaDet[i][33] = objAsistenciaGen.getPldia29() == null ? "0" : objAsistenciaGen.getPldia29();
            listaDet[i][34] = objAsistenciaGen.getPldia30() == null ? "0" : objAsistenciaGen.getPldia30();
            listaDet[i][35] = objAsistenciaGen.getPldia31() == null ? "0" : objAsistenciaGen.getPldia31();
            listaDet[i][36] = objUsuCredential.getCuenta();
            listaDet[i][37] = objListaAsis.get(i).getPlas_fecmod();
            listaDet[i][38] = objListaAsis.get(i).getCoo_accion();

        }

        return listaDet;

    }

    //Lov para filtro 
    @Listen("onOK=#txt_codigo")
    public void buscarPersonalPrincipal() {
        if (bandera == false) {
            bandera = true;
            if (txt_codigo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codigo);
                objMapObjetos.put("des_per", txt_apenom);
                objMapObjetos.put("controlador", "ControllerAsistenciaGen");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    //Salir de lov para el filtro
    @Listen("onBlur=#txt_codigo")
    public void valida_PersonalPrincipal() throws SQLException {

        if (!txt_codigo.getValue().isEmpty()) {
            String id = txt_codigo.getValue();
            objPersonal = objDaoMovLinea.getLovPersonal(id);
            if (objPersonal == null) {
                Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_codigo.setValue(null);
                        txt_codigo.setValue(null);
                        txt_codigo.focus();
                        txt_apenom.setValue("");

                    }
                });

            } else {
                txt_codigo.setValue(objPersonal.getPlidper());
                txt_apenom.setValue(objPersonal.getPldesper());
                //  habilitaBotonesDetalle(false);
            }

        } else {
            txt_apenom.setValue("");
            //habilitaBotonesDetalle(true);

        }
        bandera = false;
    }
	
    @Listen("onCtrlKey=#tabbox_asistenciagen")
    public void ctrl_teclado(Event event) throws SQLException, ParseException, IOException, XDocReportException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {

            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;

            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;

        }
    }

    public void seleccion() {
        if (rbg_asisgen.getSelectedIndex() == 0) {
            txt_asistencia.setValue("ASISTIO");
        } else if (rbg_asisgen.getSelectedIndex() == 1) {
            txt_asistencia.setValue("LIC. C. GOCE");
        } else if (rbg_asisgen.getSelectedIndex() == 2) {
            txt_asistencia.setValue("FALTA");
        } else if (rbg_asisgen.getSelectedIndex() == 3) {
            txt_asistencia.setValue("LIC. S. GOCE");
        } else if (rbg_asisgen.getSelectedIndex() == 4) {
            txt_asistencia.setValue("VACACIONES");
        } else if (rbg_asisgen.getSelectedIndex() == 5) {
            txt_asistencia.setValue("COMPENSACION");
        } else if (rbg_asisgen.getSelectedIndex() == 6) {
            txt_asistencia.setValue("PRE NATAL");
        } else if (rbg_asisgen.getSelectedIndex() == 7) {
            txt_asistencia.setValue("DESC. MEDICO");
        } else if (rbg_asisgen.getSelectedIndex() == 8) {
            txt_asistencia.setValue("POST NATAL");
        } else if (rbg_asisgen.getSelectedIndex() == 9) {
            txt_asistencia.setValue("DESC. M. SUBSIDIO");
        } else if (rbg_asisgen.getSelectedIndex() == 10) {
            txt_asistencia.setValue("LIC. PATERNIDAD");
        } else if (rbg_asisgen.getSelectedIndex() == 11) {
            txt_asistencia.setValue("LIC. ENF. TERMINAL");
        }
    }

    /**
     * Metodo para verificar campos antes de guardar
     *
     * @version 23/08/2017
     * @return valor
     */
    public String verificaFecha() {
        String valor = "";
        String fecha_inicio = "", fecha_fin = "";

        if (d_fecini.getValue() != null && d_fecfin.getValue() != null) {
            fecha_inicio = convert.format(d_fecini.getValue());
            fecha_fin = convert.format(d_fecfin.getValue());
        }

        if (d_fecini.getValue() == null) {
            valor = "Por favor ingresar fecha inicio";
            foco = "fechainicio";
            rbg_asisgen.setSelectedItem(null);
        } else if (d_fecfin.getValue() == null) {
            valor = "Por favor ingresar fecha fin ";
            foco = "fechafin";
            rbg_asisgen.setSelectedItem(null);
        } else if (d_fecfin.getValue().before(d_fecini.getValue())) {
            valor = "La fecha final debe ser mayor a la de inicio";
            foco = "fecha";
            rbg_asisgen.setSelectedItem(null);
        } else if (!objAsistenciaGen.getPeriodo().equals(fecha_inicio)) {
            valor = "Por favor fecha inicio debe ser igual al periodo generado";
            foco = "fechainicio";
            rbg_asisgen.setSelectedItem(null);
        } else if (!objAsistenciaGen.getPeriodo().equals(fecha_fin)) {
            valor = "Por favor fecha fin debe ser igual al periodo generado";
            foco = "fechafin";
            rbg_asisgen.setSelectedItem(null);
        } else {
            valor = "";
        }

        return valor;

    }

    /**
     * Metodo coloca focus en el campo
     *
     * @version 23/08/2017
     */
    public void devolverFocus() {
        if (foco.equals("fechainicio")) {
            d_fecini.focus();
        } else if (foco.equals("fechafin")) {
            d_fecfin.focus();
        } else if (foco.equals("fecha")) {
            d_fecfin.focus();
        }

    }

    public void limpiarFechas() {
        d_fecini.setValue(null);
        d_fecfin.setValue(null);
    }

    public void lov(String tipo, Textbox codigo) {
        ListModelList<AsistenciaDias> objlstDate = new ListModelList<AsistenciaDias>();
        AsistenciaDias objAsisDias;
        Window window;
        if (bandera == false) {
            bandera = true;
            // if (txt_codigo.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("codigo", codigo);
            objMapObjetos.put("tipo", tipo);
            objMapObjetos.put("objlstLista", objlstLov);

            if (verificaFecha().isEmpty()) {
                objAsisDias = new AsistenciaDias();
                objAsisDias.setFechainicio(d_fecini.getValue());
                objAsisDias.setFechafin(d_fecfin.getValue());
                objlstDate.add(objAsisDias);
            } else {
                for (int i = 0; i < objlistSelect.size(); i++) {
                    objAsisDias = new AsistenciaDias();
                    objAsisDias.setFechainicio(objlstAsistenciaGenDet.get(objlistSelect.get(i)).getFec_asis());
                    objAsisDias.setFechafin(objlstAsistenciaGenDet.get(objlistSelect.get(i)).getFec_asis());
                    objlstDate.add(objAsisDias);
                }
            }

            objMapObjetos.put("finicio", objlstDate);
            objMapObjetos.put("tamanho", objlstDate.size());
            objMapObjetos.put("controlador", "ControllerAsistenciaGen");
            // objMapObjetos.put("tipodoc", objAsistenciaGen.getPltipdoc());
            objMapObjetos.put("objeto", objAsistenciaGen);
            if (tipo.equals("8")) {

                window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAsisDMedicos.zul", null, objMapObjetos);
                // window.doModal();
			} else if (tipo.equals("1")) {
                window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAsistenciaFalta.zul", null, objMapObjetos);
                //window.doModal();
            } else {
                window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAsistenciaSubsidio.zul", null, objMapObjetos);
                //window.doModal();

            }
            window.doModal();
        }
    }

    public void limpiarCamposLoc() {
        txt_lovfalta.setValue("");
        txt_lovdesmedico.setValue("");
        txt_lovliccgoce.setValue("");
        txt_lovlicenfermedad.setValue("");
        txt_lovlicsgoce.setValue("");
        txt_lovpaternidad.setValue("");
    }
	
    @Listen("onClick=#btn_bloque")
    public void procesoEnBloque() {
        String sucursal = cb_sucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
        String periodo = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedIndex() == 0 ? "" : cb_periodo.getSelectedItem().getValue().toString();

        Map objMapObjetos = new HashMap();
        objMapObjetos.put("sucursal", sucursal);
        objMapObjetos.put("periodo", periodo);
		objMapObjetos.put("lista",lst_asistenciagen);
        objMapObjetos.put("controlador", "ControllerAsistenciaGen");
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAsistenciaBloque.zul", null, objMapObjetos);
        window.doModal();

    }

    public void cambiaEstadoAsistencia(String s_tipo, int i_size) {
        AsistenciaGen objAsistenciaDetalle = new AsistenciaGen();

        int j;
        int i_aux;
        if (!verificaFecha().isEmpty()) {

            for (int conta = 0; conta < objlistSelect.size(); conta++) {

                lst_detasis.setSelectedIndex(objlistSelect.get(conta));
                objlstAsistenciaGenDet.get(objlistSelect.get(conta)).setAsistencia(s_tipo);
                int d = objlstAsistenciaGenDet.get(objlistSelect.get(conta)).getFec_asis().getDate();
                String aux = "" + d;
                llenaEstadoFecha(aux, s_tipo);

            }
        } else {
            for (j = 0; j < objlstAsistenciaGenDet.size(); j++) {
                String s_dia1 = objlstAsistenciaGenDet.get(j).getFec_asis().toString();
                String s_dia2 = convertJavaDateToSqlDate(d_fecini.getValue()).toString();
                if (s_dia1.equals(s_dia2)) {
                    //objAsistenciaDetalle = objlstAsistenciaGenDet.get(j);
                    break;
                }
            }
            i_aux = j;
            for (int conta = 0; conta <= i_size; conta++) {

                lst_detasis.setSelectedIndex(i_aux);
                objlstAsistenciaGenDet.get(i_aux).setAsistencia(s_tipo);
                int d = objlstAsistenciaGenDet.get(i_aux).getFec_asis().getDate();
                String aux = "" + d;
                llenaEstadoFecha(aux, s_tipo);
                i_aux++;
            }

        }
        objlistSelect = new ListModelList<Integer>();
        lst_detasis.setModel(objlstAsistenciaGenDet);
        d_fecini.setValue(null);
        d_fecfin.setValue(null);
		d_fecini.setDisabled(false);
        d_fecfin.setDisabled(false);
         rbg_asisgen.setSelectedItem(null);
    }

    public int diasDiferencia(Date fechaini, Date fechafin) {
        // i_dias = Days.daysBetween(new LocalDate(d_dia_ahora), new LocalDate(objPlaContrato.getPar15_confecfin().getTime())).getDays(); 

        return Days.daysBetween(new LocalDate(fechaini), new LocalDate(fechafin.getTime())).getDays();
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public void llenaEstadoFecha(String s_dia, String tipo) {
        if (s_dia.length() == 1) {
            s_dia = "0" + s_dia;

        }

        if (tipo.equals("ASISTIO")) {
            tipo = "0";
        } else if (tipo.equals("LIC. C. GOCE")) {
            tipo = "5";
        } else if (tipo.equals("FALTA")) {
            tipo = "1";
        } else if (tipo.equals("LIC. S. GOCE")) {
            tipo = "6";
        } else if (tipo.equals("VACACIONES")) {
            tipo = "2";
        } else if (tipo.equals("COMPENSACION")) {
            tipo = "7";
        } else if (tipo.equals("PRE NATAL")) {
            tipo = "3";
        } else if (tipo.equals("DESC. MEDICO")) {
            tipo = "8";
        } else if (tipo.equals("POST NATAL")) {
            tipo = "4";
        } else if (tipo.equals("DESC. M. SUBSIDIO")) {
            tipo = "9";
        } else if (tipo.equals("LIC. PATERNIDAD")) {
            tipo = "A";
        } else if (tipo.equals("LIC. ENF. TERMINAL")) {
            tipo = "B";
        }

        if (s_dia.equals("01")) {

            objAsistenciaGen.setPldia01(tipo);

        }
        if (s_dia.equals("02")) {

            objAsistenciaGen.setPldia02(tipo);

        }
        if (s_dia.equals("03")) {

            objAsistenciaGen.setPldia03(tipo);

        }
        if (s_dia.equals("04")) {

            objAsistenciaGen.setPldia04(tipo);

        }
        if (s_dia.equals("05")) {

            objAsistenciaGen.setPldia05(tipo);

        }
        if (s_dia.equals("06")) {

            objAsistenciaGen.setPldia06(tipo);

        }
        if (s_dia.equals("07")) {

            objAsistenciaGen.setPldia07(tipo);

        }
        if (s_dia.equals("08")) {

            objAsistenciaGen.setPldia08(tipo);

        }
        if (s_dia.equals("09")) {

            objAsistenciaGen.setPldia09(tipo);

        }
        if (s_dia.equals("10")) {

            objAsistenciaGen.setPldia10(tipo);

        }
        if (s_dia.equals("11")) {

            objAsistenciaGen.setPldia11(tipo);

        }
        if (s_dia.equals("12")) {

            objAsistenciaGen.setPldia12(tipo);

        }
        if (s_dia.equals("13")) {

            objAsistenciaGen.setPldia13(tipo);

        }
        if (s_dia.equals("14")) {

            objAsistenciaGen.setPldia14(tipo);

        }
        if (s_dia.equals("15")) {

            objAsistenciaGen.setPldia15(tipo);

        }
        if (s_dia.equals("16")) {

            objAsistenciaGen.setPldia16(tipo);

        }
        if (s_dia.equals("17")) {

            objAsistenciaGen.setPldia17(tipo);

        }
        if (s_dia.equals("18")) {

            objAsistenciaGen.setPldia18(tipo);

        }
        if (s_dia.equals("19")) {

            objAsistenciaGen.setPldia19(tipo);

        }
        if (s_dia.equals("20")) {

            objAsistenciaGen.setPldia20(tipo);

        }
        if (s_dia.equals("21")) {

            objAsistenciaGen.setPldia21(tipo);

        }
        if (s_dia.equals("22")) {

            objAsistenciaGen.setPldia22(tipo);

        }
        if (s_dia.equals("23")) {

            objAsistenciaGen.setPldia23(tipo);

        }
        if (s_dia.equals("24")) {

            objAsistenciaGen.setPldia24(tipo);

        }
        if (s_dia.equals("25")) {

            objAsistenciaGen.setPldia25(tipo);

        }
        if (s_dia.equals("26")) {

            objAsistenciaGen.setPldia26(tipo);

        }
        if (s_dia.equals("27")) {

            objAsistenciaGen.setPldia27(tipo);

        }
        if (s_dia.equals("28")) {

            objAsistenciaGen.setPldia28(tipo);

        }
        if (s_dia.equals("29")) {

            objAsistenciaGen.setPldia29(tipo);

        }
        if (s_dia.equals("30")) {

            objAsistenciaGen.setPldia30(tipo);

        }
        if (s_dia.equals("31")) {

            objAsistenciaGen.setPldia31(tipo);

        }

    }
}
