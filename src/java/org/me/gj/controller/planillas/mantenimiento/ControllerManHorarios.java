/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManHorarios;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerManHorarios extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_sucursal_man, cb_tipo;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Listbox lst_lista;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Textbox txt_usua, txt_usumo,
            /*txt_des1, txt_ant1, txt_sab1, txt_vie1, txt_jue1, txt_mie1, txt_mar1, txt_lun1,*/
            txt_descripcion, txt_codigo, /*txt_dom1,*/
            txt_descanso, txt_hasta, txt_desde, txt_hro_sem;
    @Wire
    Timebox txt_lun1, txt_mar1, txt_mie1, txt_jue1, txt_vie1, txt_sab1, txt_dom1,
            txt_sab2, txt_vie2, txt_jue2, txt_mie2, txt_mar2, txt_lun2,
            txt_sab3, txt_vie3, txt_jue3, txt_mie3, txt_mar3, txt_lun3,
            txt_sab4, txt_vie4, txt_jue4, txt_mie4, txt_mar4, txt_lun4,
            txt_dom2, txt_dom3, txt_dom4;

    @Wire
    Intbox n_min_tole, n_hora_refri, n_hora_trab,
            n_rango, txt_ant1, txt_des1, txt_des2, txt_ant2, txt_des3, txt_ant3, txt_des4, txt_ant4;
    @Wire
    Checkbox chk_amanecida, chk_sempresa, chk_irefrigerio,
            chk_srefrigerio, chk_iempresa;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    DaoAccesos objDaoAccesos;
    ListModelList<ManHorarios> objlstHorarios;
    ManHorarios objHorarios = new ManHorarios();
    DaoManHorarios objDaoHorarios;
    Accesos objAccesos;
    ManHorarios objHor_Sem;
    ListModelList<Sucursales> objlstSucursal;
    int i_sel, emp_id, suc_id, valor;
    String s_estado, campo, s_mensaje;
    //para horario automatico
    Calendar calendar = Calendar.getInstance();
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();
    Calendar calendar3 = Calendar.getInstance();
    
    
    
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManHorarios.class);
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        emp_id = objUsuCredential.getCodemp();
        objDaoHorarios = new DaoManHorarios();
        objDaoAccesos = new DaoAccesos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        cb_sucursal.setModel(objlstSucursal);
        cb_sucursal_man.setModel(objlstSucursal);
        objlstHorarios = new ListModelList<ManHorarios>();
        objlstHorarios = objDaoHorarios.listHorario(emp_id);
        lst_lista.setModel(objlstHorarios);
        habilitaTab(false, true);

    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101120, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Horarios con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Horarios con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Horarios");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Horarios");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Horarios");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Horarios");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Horarios");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Horarios");
        }
    }

    @Listen("onSelect=#lst_lista")
    public void seleccion() throws ParseException {
        objHorarios = lst_lista.getSelectedItem().getValue();
        //Messagebox.show(objHorarios.getS_sucursal() + " - "  + objHorarios.getSucursal() + " - " + objHorarios.getMan_codigo() );
        if (objHorarios == null) {
            objHorarios = objlstHorarios.get(i_sel + 1);

        }
        i_sel = lst_lista.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objHorarios.getMan_codigo());
        habilitaTab(false, false);
        seleccionaTab(bandera, bandera);
    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        if (cb_sucursal.getSelectedIndex() == -1) {
            Messagebox.show("Debe elegir una sucursal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int sucursal = cb_sucursal.getSelectedItem().getValue();
            objlstHorarios = objDaoHorarios.listHorarioBuscar(objUsuCredential.getCodemp(), sucursal);
            lst_lista.setModel(objlstHorarios);
            if (objlstHorarios.getSize() > 0) {
                lst_lista.setModel(objlstHorarios);
            } else {
                objlstHorarios = null;
                lst_lista.setModel(objlstHorarios);
                Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        limpiaAuditoria();
        limpiarCampos();
    }

  /*  @Listen("onClick=#tbbtn_reset")
    public void reset() throws SQLException {
        objlstHorarios = objDaoHorarios.listHorario(emp_id);
        lst_lista.setModel(objlstHorarios);
        cb_sucursal.setValue("");
    }
*/
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        habilitaCampos(false);
        objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        cb_sucursal_man.setModel(objlstSucursal);
        //chk_almest.setDisabled(true);
        // chk_almest.setChecked(true);
        cb_sucursal_man.focus();
        txt_codigo.setDisabled(true);
        cb_tipo.setSelectedIndex(1);
        campoSeteado();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_lista.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            //  tbbtn_btn_guardar.setDisabled(true);
                            //   tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            // lst_ubicaciones.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    } 

//    public ManHorarios generaRegistro(){
//        String = objHorarios.get
//    }
    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } /*else if (chk_almest.isChecked() && lst_lista.getSelectedIndex() >= 0) {
         //   Messagebox.show("Es una Ubicación por Defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         s_estado = "E";
         habilitaBotones(true, false);
         habilitaTab(true, false);
         seleccionaTab(false, true);
         habilitaCampos(false);
         txt_descripcion.focus();

         }*/ else {
            s_estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            // cb_sucursal_man.setSelectedIndex(objHorarios.getMan_sucursal());
            //  objlstSucursal = objDaoAccesos.lstSucursales(emp_id);
            // cb_tipo.setSelectedIndex(objHorarios.getTipo().charAt(i_sel));
            txt_descripcion.focus();
            cb_sucursal_man.setDisabled(true);

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onSelect=#cb_sucursal_man")
    public void next() {
        txt_descripcion.focus();
    }

    //***********************LOVS**********************
    @Listen("onOK=#txt_hro_sem")
    public void busquedadCuenta() {
        if (bandera == false) {
            bandera = true;
            if (txt_hro_sem.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_hro_sem", txt_hro_sem);
                objMapObjetos.put("txt_desde", txt_desde);
                objMapObjetos.put("txt_hasta", txt_hasta);
                objMapObjetos.put("txt_descanso", txt_descanso);
                objMapObjetos.put("controlador", "ControllerManHorarios");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorarios.zul", null, objMapObjetos);
                window.doModal();
            } else {
                // cb_tipo.focus();
                //  cb_tipo.select();
                txt_lun1.focus();
            }
        }
    }

    @Listen("onBlur=#txt_hro_sem")
    public void valida_Leasing() throws SQLException {
        if (!txt_hro_sem.getValue().isEmpty()) {
            String consulta = txt_hro_sem.getValue();
            objHor_Sem = objDaoHorarios.getHora(objUsuCredential.getCodemp(), consulta);
            if (objHor_Sem == null) {
                Messagebox.show("El código de horario no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_hro_sem.setValue("");
                        txt_hro_sem.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_hro_sem.setValue(objHor_Sem.getMan_codigo_semanal());
                txt_desde.setValue(objHor_Sem.getMan_de());
                txt_hasta.setValue(objHor_Sem.getMan_a());
                txt_descanso.setValue(objHor_Sem.getMan_descanso());
            }
        } else {
            txt_hro_sem.setValue("");
        }
        bandera = false;
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este registro?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoHorarios.eliminar(objHorarios);
                                valor = objDaoHorarios.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstHorarios.clear();
                                    // cb_estado.setSelectedIndex(0);
                                    // cb_busqueda.setSelectedIndex(0);
                                    // txt_busqueda.setValue("%%");
                                    // txt_busqueda.setDisabled(true);
                                    objlstHorarios = objDaoHorarios.listHorario(1);
                                    lst_lista.setModel(objlstHorarios);
                                    lst_lista.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoHorarios.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
        }

    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("sucursal")) {
                            cb_sucursal_man.focus();
                        } else if (campo.equals("descripcion")) {
                            txt_descripcion.focus();
                        } else if (campo.equals("hrsemanal")) {
                            txt_hro_sem.focus();
                        } else if(campo.equals("hrtrabajo")){
                            n_hora_trab.focus();
                        }else if (campo.equals("hrrefrigerio")) {
                            n_hora_refri.focus();
                        }else if(campo.equals("mayor_refri")){
                            n_hora_refri.focus();
                        }else if (campo.equals("min_tol")){
                            n_min_tole.focus();
                        }else if(campo.equals("min_tol2")){
                            n_min_tole.focus();
                        }
                    }
                }
            });
        } else {
            s_mensaje = "Esta Seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                //ParametrosSalida objParamSalida;
                                objlstHorarios = new ListModelList<ManHorarios>();
                                objHorarios = (ManHorarios) generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoHorarios.insertar(objHorarios);
                                } else {
                                    s_mensaje = objDaoHorarios.actualizar(objHorarios);
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                VerificarTransacciones();
                                cb_sucursal.setValue(null);
                                objlstHorarios = objDaoHorarios.listHorario(objUsuCredential.getCodemp());
                                lst_lista.setModel(objlstHorarios);
                            }
                        }
                    });
        }

    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_horario")
    public void ctrl_teclado(Event event) throws SQLException, ParseException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo();
                }
                break;
            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    botonEliminar();
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
            /*case 119:
             if (!tbbtn_btn_imprimir.isDisabled()) {
             botonImprimir();
             }
             break;*/
        }
    }

    @Listen("onOK=#txt_descripcion")
    public void next1() {
        cb_tipo.focus();
    }

    @Listen("onOK=#cb_tipo")
    public void next2() {
        chk_amanecida.focus();
    }

    @Listen("onOK=#chk_amanecida")
    public void next3() {
        n_rango.focus();
    }

    @Listen("onOK=#n_rango")
    public void next4() {
        n_hora_trab.focus();
    }

    @Listen("onOK=#n_hora_trab")
    public void next5() {
        n_hora_refri.focus();
    }

    @Listen("onOK=#n_hora_refri")
    public void next6() {
        n_min_tole.focus();
    }

    @Listen("onOK=#n_min_tole")
    public void next7() {
        txt_hro_sem.focus();
    }

    @Listen("onOK=#txt_hro_sem")
    public void next8() {
        txt_lun1.focus();
    }

    @Listen("onSelect=#cb_sucursal_man")
    public void next9() {
        txt_descripcion.focus();
    }
    /* @Listen("onClick=#txt_lun1")
     public void click1(){
        
     }*/

    @Listen("onOK=#txt_lun1")
    public void nextHora1() {
        //metodo de carga de horas full 
       /* Calendar calendar = Calendar.getInstance();
         Calendar calendar1 = Calendar.getInstance();
         Calendar calendar2 = Calendar.getInstance();*/
        // date = df.parse(String.valueOf(inputDate));
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int h = n_hora_trab.getValue() / 2; //dividimos horas trabajadas entre 2 para sacar tiempo en salir a refrigerio          
            int h_ref = n_hora_refri.getValue() + h;//sumamos tiempo de refrigerio con la division de salida de refri
            int total = n_hora_trab.getValue() + n_hora_refri.getValue();//sumar hr trabajadas + hr refrigerio para sacar la hr salida de empresa

            calendar.setTime(txt_lun1.getValue());
            calendar.add(Calendar.HOUR, h);
            calendar1.setTime(txt_lun1.getValue());
            calendar1.add(Calendar.HOUR, h_ref);
            calendar2.setTime(txt_lun1.getValue());
            calendar2.add(Calendar.HOUR, total);
            //   calendar3.setTime(txt_lun1.getValue());
            //String s_refri = jr.format(txt_lun1.getValue().getHours()+h);
            //  int i_refri = txt_lun1.getValue().getHours() + n_hora_refri.getValue();
            //  int sal_emp = txt_lun1.getValue().getHours()+n_hora_trab.getValue()+n_hora_refri.getValue();
            //  txt_lun1.setValue(calendar3.getTime());
            txt_lun2.setValue(calendar.getTime());
            txt_lun3.setValue(calendar1.getTime());
            txt_lun4.setValue(calendar2.getTime());
            //setamos los campos
            txt_mar1.setValue(txt_lun1.getValue());
            txt_mie1.setValue(txt_lun1.getValue());
            txt_jue1.setValue(txt_lun1.getValue());
            txt_vie1.setValue(txt_lun1.getValue());
            txt_sab1.setValue(txt_lun1.getValue());
            nextHora2_1();
            nextHora3_1();
            nextHora4_1();

        } else {
            txt_mar1.setValue(txt_lun1.getValue());
            txt_mie1.setValue(txt_lun1.getValue());
            txt_jue1.setValue(txt_lun1.getValue());
            txt_vie1.setValue(txt_lun1.getValue());
            txt_sab1.setValue(txt_lun1.getValue());
            limpiarCampos234();
            txt_lun2.focus();
        }
    }

    @Listen("onOK=#txt_lun2")
    public void nextHora2() {

        if (txt_lun2.getValue().before(txt_lun1.getValue())) {
            Messagebox.show("Campo debe ser mayor ingreso a empresa del dia lunes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_lun2.setValue(null);
        } else {
            if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
                int h_ref = n_hora_refri.getValue();
                int total = n_hora_trab.getValue() + n_hora_refri.getValue();
                calendar.setTime(txt_lun2.getValue());
                calendar.add(Calendar.HOUR, h_ref);
                calendar2.setTime(txt_lun1.getValue());
                calendar2.add(Calendar.HOUR, total);
                txt_lun3.setValue(calendar.getTime());
                txt_lun4.setValue(calendar2.getTime());
                nextHora2_1();
                nextHora3_1();
                nextHora4_1();

            } else {
                txt_mar2.setValue(txt_lun2.getValue());
                txt_mie2.setValue(txt_lun2.getValue());
                txt_jue2.setValue(txt_lun2.getValue());
                txt_vie2.setValue(txt_lun2.getValue());
                txt_sab2.setValue(txt_lun2.getValue());
                limpiarCampos34();
                txt_lun3.focus();
            }
        }

    }

    @Listen("onOK=#txt_lun3")
    public void nextHora3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_lun3.setValue(null);
        } else {

            if (txt_lun3.getValue().before(txt_lun2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia lunes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_lun3.setValue(null);
            } else {
                if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
                    int total = n_hora_trab.getValue() + n_hora_refri.getValue();
                    calendar2.setTime(txt_lun1.getValue());
                    calendar2.add(Calendar.HOUR, total);
                    txt_lun4.setValue(calendar2.getTime());
                    nextHora3_1();
                    nextHora4_1();
                } else {
                    txt_mar3.setValue(txt_lun3.getValue());
                    txt_mie3.setValue(txt_lun3.getValue());
                    txt_jue3.setValue(txt_lun3.getValue());
                    txt_vie3.setValue(txt_lun3.getValue());
                    txt_sab3.setValue(txt_lun3.getValue());
                    limpiarCampos4();
                    txt_lun4.focus();
                }
            }
        }
    }

    @Listen("onOK=#txt_lun4")
    public void nextHora4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_lun4.getValue().getHours() - txt_lun1.getValue().getHours();

            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia lunes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_lun4.setValue(null);
            }

        } else if (txt_lun3.getValue() != null) {

            if (txt_lun4.getValue().before(txt_lun4.getValue())) {
                Messagebox.show("Campo debe ser mayor que horario ingreso de refrigerio del dia lunes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_lun4.setValue(null);
            } else {
                txt_mar4.setValue(txt_lun4.getValue());
                txt_mie4.setValue(txt_lun4.getValue());
                txt_jue4.setValue(txt_lun4.getValue());
                txt_vie4.setValue(txt_lun4.getValue());
                txt_sab4.setValue(txt_lun4.getValue());
                //  txt_lun2.focus();
            }
        }
    }

    @Listen("onOK=#txt_mar1")
    public void okMar1() {
        txt_mar2.focus();
    }

    @Listen("onOK=#txt_mar2")
    public void okMar2() {
        if (txt_mar2.getValue().before(txt_mar1.getValue())) {
            Messagebox.show("Campo debe ser mayor a ingreso del dia martes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_mar2.setValue(null);
        } else {
            txt_mar3.focus();
        }
    }

    @Listen("onOK=#txt_mar3")
    public void okMar3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_mar3.setValue(null);
        } else {

            if (txt_mar3.getValue().before(txt_mar2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia martes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mar3.setValue(null);
            } else {
                txt_mar4.focus();
            }
        }
    }

    @Listen("onOK=#txt_mar4")
    public void okMar4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_mar4.getValue().getHours() - txt_mar1.getValue().getHours();

            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia martes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mar4.setValue(null);
            }

        } else if (txt_mar3.getValue() != null) {

            if (txt_mar4.getValue().before(txt_mar3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia martes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mar4.setValue(null);
            } else {
                txt_mie1.focus();
            }
        }
    }

    //miercoles
    @Listen("onOK=#txt_mie1")
    public void okMie1() {
        txt_mie2.focus();
    }

    @Listen("onOK=#txt_mie2")
    public void okMie2() throws ParseException {
        //calendar.setTime(txt_mie1.getValue());
        // txt_mie1.setValue(calendar.getTime());
        if (txt_mie2.getValue().before(txt_mie1.getValue())) {///txt_mie1.getValue()
            Messagebox.show("Campo debe ser mayor a ingreso del dia miercoles", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_mie2.setValue(null);
        } else {
            txt_mie3.focus();
        }
    }

    @Listen("onOK=#txt_mie3")
    public void okMie3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_mie3.setValue(null);
        } else {
            if (txt_mie3.getValue().before(txt_mie2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia miercoles", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mie3.setValue(null);
            } else {
                txt_mie4.focus();
            }
        }
    }

    @Listen("onOK=#txt_mie4")
    public void okMie4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_mie4.getValue().getHours() - txt_mie1.getValue().getHours();
            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia miercoles", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mie4.setValue(null);
            }

        } else if (txt_mie3.getValue() != null) {

            if (txt_mie4.getValue().before(txt_mie3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia miercoles", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_mie4.setValue(null);
            } else {
                txt_jue1.focus();
            }
        }
    }

    //jueves
    @Listen("onOK=#txt_jue1")
    public void okJu1() {
        txt_jue2.focus();
    }

    @Listen("onOK=#txt_jue2")
    public void okJue2() {
        if (txt_jue2.getValue().before(txt_jue1.getValue())) {
            Messagebox.show("Campo debe ser mayor a ingreso del dia jueves", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_jue2.setValue(null);
        } else {
            txt_jue3.focus();
        }
    }

    @Listen("onOK=#txt_jue3")
    public void okJue3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_jue3.setValue(null);
        } else {
            if (txt_jue3.getValue().before(txt_jue2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia jueves", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_jue3.setValue(null);
            } else {
                txt_jue4.focus();
            }
        }
    }

    @Listen("onOK=#txt_jue4")
    public void okJue4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_jue4.getValue().getHours() - txt_jue1.getValue().getHours();
            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia jueves", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_jue4.setValue(null);
            }

        } else if (txt_jue3.getValue() != null) {

            if (txt_jue4.getValue().before(txt_jue3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia jueves", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_jue4.setValue(null);
            } else {
                txt_vie1.focus();
            }
        }
    }

    //viernes
    @Listen("onOK=#txt_vie1")
    public void okVie1() {
        txt_vie2.focus();
    }

    @Listen("onOK=#txt_vie2")
    public void okVie2() {
        if (txt_vie2.getValue().before(txt_vie1.getValue())) {
            Messagebox.show("Campo debe ser mayor a ingreso del dia viernes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_vie2.setValue(null);
        } else {
            txt_vie3.focus();
        }
    }

    @Listen("onOK=#txt_vie3")
    public void okVie3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_vie3.setValue(null);
        } else {
            if (txt_vie3.getValue().before(txt_vie2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia viernes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_vie3.setValue(null);
            } else {
                txt_vie4.focus();
            }
        }
    }

    @Listen("onOK=#txt_vie4")
    public void okVie4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_vie4.getValue().getHours() - txt_vie1.getValue().getHours();
            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia viernes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_vie4.setValue(null);
            }

        } else if (txt_vie3.getValue() != null) {

            if (txt_vie4.getValue().before(txt_vie3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia viernes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_vie4.setValue(null);
            } else {
                txt_sab1.focus();
            }
        }
    }

    //sabado
    @Listen("onOK=#txt_sab1")
    public void okSab1() {
        txt_sab2.focus();
    }

    @Listen("onOK=#txt_sab2")
    public void okSab2() {
        if (txt_sab2.getValue().before(txt_sab1.getValue())) {
            Messagebox.show("Campo debe ser mayor a ingreso del dia sabado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_sab2.setValue(null);
        } else {
            txt_sab3.focus();
        }
    }

    @Listen("onOK=#txt_sab3")
    public void okSab3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_sab3.setValue(null);
        } else {
            if (txt_sab3.getValue().before(txt_sab2.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia sabado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_sab3.setValue(null);
            } else {
                txt_sab4.focus();
            }
        }
    }

    @Listen("onOK=#txt_sab4")
    public void okSab4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_sab4.getValue().getHours() - txt_sab1.getValue().getHours();

            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia sabado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_sab4.setValue(null);
            }

        } else if (txt_sab3.getValue() != null) {

            if (txt_sab4.getValue().before(txt_sab3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia sabado", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_sab4.setValue(null);
            } else {
                txt_dom1.focus();
            }
        }
    }

    //domingo
    @Listen("onOK=#txt_dom1")
    public void okDom1() {
        txt_dom2.focus();
    }

    @Listen("onOK=#txt_dom2")
    public void okDom2() {
        if (txt_dom2.getValue().before(txt_dom1.getValue())) {
            Messagebox.show("Campo debe ser mayor a ingreso del dia domingo", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_dom2.setValue(null);
        } else {
            txt_dom3.focus();
        }
    }

    @Listen("onOK=#txt_dom3")
    public void okDom3() {
        if (n_hora_refri.getValue() == null) {
            Messagebox.show("Debe ingresar hora de  refrigerio ", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            txt_dom3.setValue(null);
        } else {
            if (txt_dom3.getValue().before(txt_dom1.getValue())) {
                Messagebox.show("Campo debe ser mayor a salida de refrigerio del dia domingo", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_dom3.setValue(null);
            } else {
                txt_dom4.focus();
            }
        }
    }

    @Listen("onOK=#txt_dom4")
    public void okDom4() {
        if (n_hora_trab.getValue() != null && n_hora_refri.getValue() != null) {
            int horas_trabajadas_total = n_hora_refri.getValue() + n_hora_trab.getValue();
            int horas_a_trabajar = txt_dom4.getValue().getHours() - txt_dom1.getValue().getHours();

            if (horas_trabajadas_total <= horas_a_trabajar) {
                Messagebox.show("Se pasa la horas de trabajo del dia domingo", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_dom4.setValue(null);
            }

        } else if (txt_dom3.getValue() != null) {

            if (txt_dom4.getValue().before(txt_dom3.getValue())) {
                Messagebox.show("Campo debe ser mayor a ingreso de refrigerio del dia domingo", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                txt_dom4.setValue(null);
            } else {
                txt_ant1.focus();
            }
        }
    }

    @Listen("onClick=#txt_lun1")
    public void clickLun1() {
        txt_lun1.select();
    }

    @Listen("onClick=#txt_lun2")
    public void clickLun2() {
        txt_lun2.select();
    }

    @Listen("onClick=#txt_lun3")
    public void clickLun3() {
        txt_lun3.select();
    }

    @Listen("onClick=#txt_lun4")
    public void clickLun4() {
        txt_lun4.select();
    }

    @Listen("onClick=#txt_mar1")
    public void clickMar1() {
        txt_mar1.select();
    }

    @Listen("onClick=#txt_mar2")
    public void clickMar2() {
        txt_mar2.select();
    }

    @Listen("onClick=#txt_mar3")
    public void clickMar3() {
        txt_mar3.select();
    }

    @Listen("onClick=#txt_mar4")
    public void clickMar4() {
        txt_mar4.select();
    }

    @Listen("onClick=#txt_mie1")
    public void clickMie1() {
        txt_mie1.select();
    }

    @Listen("onClick=#txt_mie2")
    public void clickMie2() {
        txt_mie2.select();
    }

    @Listen("onClick=#txt_mie3")
    public void clickMie3() {
        txt_mie3.select();
    }

    @Listen("onClick=#txt_mie4")
    public void clickMie4() {
        txt_mie4.select();
    }

    @Listen("onClick=#txt_jue1")
    public void clickJue1() {
        txt_jue1.select();
    }

    @Listen("onClick=#txt_jue2")
    public void clickJue2() {
        txt_jue2.select();
    }

    @Listen("onClick=#txt_jue3")
    public void clickJue3() {
        txt_jue3.select();
    }

    @Listen("onClick=#txt_jue4")
    public void clickJue4() {
        txt_jue4.select();
    }

    @Listen("onClick=#txt_vie1")
    public void clickVie1() {
        txt_vie1.select();
    }

    @Listen("onClick=#txt_vie2")
    public void clickVie2() {
        txt_vie2.select();
    }

    @Listen("onClick=#txt_vie3")
    public void clickVie3() {
        txt_vie3.select();
    }

    @Listen("onClick=#txt_vie4")
    public void clickVie4() {
        txt_vie4.select();
    }

    @Listen("onClick=#txt_sab1")
    public void clickSab1() {
        txt_sab1.select();
    }

    @Listen("onClick=#txt_sab2")
    public void clickSab2() {
        txt_sab2.select();
    }

    @Listen("onClick=#txt_sab3")
    public void clickSab3() {
        txt_sab3.select();
    }

    @Listen("onClick=#txt_sab4")
    public void clickSab4() {
        txt_sab4.select();
    }

    @Listen("onClick=#txt_dom1")
    public void clickDomi1() {
        txt_dom1.select();
    }

    @Listen("onClick=#txt_dom2")
    public void clickDomi2() {
        txt_dom2.select();
    }

    @Listen("onClick=#txt_dom3")
    public void clickDomi3() {
        txt_dom3.select();
    }

    @Listen("onClick=#txt_dom4")
    public void clickDomi4() {
        txt_dom4.select();
    }

    @Listen("onOK=#txt_ant1")
    public void okAn() {
        txt_des1.focus();

    }

    @Listen("onOK=#txt_des1")
    public void okDes1() {
        txt_ant2.focus();
    }

    @Listen("onOK=#txt_ant2")
    public void okAn2() {
        txt_des2.focus();
    }

    @Listen("onOK=#txt_des2")
    public void okDes2() {
        txt_ant3.focus();
    }

    @Listen("onOK=#txt_ant3")
    public void okAn3() {
        txt_des3.focus();
    }

    @Listen("onOK=#txt_des3")
    public void okDes3() {
        txt_ant4.focus();
    }

    @Listen("onOK=#txt_ant4")
    public void okDes4() {
        txt_des4.focus();
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_lista == null) {
            Messagebox.show("No hay registros de Horarios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("EMPRESA", objUsuCredential.getCodemp());
            objMapObjetos.put("SUCURSAL", objUsuCredential.getCodsuc());
            objMapObjetos.put("P_WHERE", DaoManHorarios.P_WHERE);
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionHorarios.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //muestra de reporte
 /*   @Listen("onClick=#tbbtn_pdf")
     public void reporte() throws SQLException, IOException {
     if (objlstHorarios == null || objlstHorarios.isEmpty()) {
     Messagebox.show("No hay registros de Horarios", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     Connection conexion = new ConectaBD().conectar();
     InputStream is = null;
     JasperPrint jasperPrint;
     try {
     AMedia amedia;
     Map objParam = new HashMap();
     Map objMapObjetos = new HashMap();

     objParam.put("EMPRESA", objUsuCredential.getCodemp());
     objParam.put("SUCURSAL", objUsuCredential.getCodsuc());
     // objParam.put("SUCURSAL",cb_sucursal.getSelectedItem().getValue());
     objParam.put("P_WHERE", DaoManHorarios.P_WHERE);
     objParam.put("REPORT_LOCALE", new Locale("en", "US"));
     JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     is = Thread.currentThread().getContextClassLoader().getResourceAsStream("../../reportes/ReportHorarios.jasper");
            
     int x = 100;
     int y = 0;
     int aleatorio = (int) (Math.random() * x) + y;
     jasperPrint = JasperFillManager.fillReport(is, objParam, conexion);
     String nom_reporte = Utilitarios.hoyAsString1() + "REPORTHORARIOS" + aleatorio;
     JRPdfExporter exporter = new JRPdfExporter();
     exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
     exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".pdf");
     exporter.exportReport();

     File file = new File("D:\\" + nom_reporte + ".pdf");
     FileInputStream fis = new FileInputStream(file);
     amedia = new AMedia(file.getAbsolutePath(), "pdf", "application/pdf", fis);
     objMapObjetos.put("amedia", amedia);
     objMapObjetos.put("archivo", file.getAbsolutePath().toString());
     objMapObjetos.put("tipo", "pdf");
     Window window = (Window) Executions.createComponents("/org/me/gj/view/planillas/informes/Reporte.zul", null, objMapObjetos);
     window.doModal();
     } catch (JRException ex) {
     throw new RuntimeException(ex);
     } finally {
     if (is != null) {
     is.close();
     }
     if (conexion != null) {
     conexion.close();
     }
     }
     }
     }*/
    public void nextHora2_1() {

        txt_mar2.setValue(txt_lun2.getValue());
        txt_mie2.setValue(txt_lun2.getValue());
        txt_jue2.setValue(txt_lun2.getValue());
        txt_vie2.setValue(txt_lun2.getValue());
        txt_sab2.setValue(txt_lun2.getValue());
        txt_lun3.focus();
    }

    public void nextHora3_1() {

        txt_mar3.setValue(txt_lun3.getValue());
        txt_mie3.setValue(txt_lun3.getValue());
        txt_jue3.setValue(txt_lun3.getValue());
        txt_vie3.setValue(txt_lun3.getValue());
        txt_sab3.setValue(txt_lun3.getValue());
        // txt_lun4.focus();
    }

    public void nextHora4_1() {

        txt_mar4.setValue(txt_lun4.getValue());
        txt_mie4.setValue(txt_lun4.getValue());
        txt_jue4.setValue(txt_lun4.getValue());
        txt_vie4.setValue(txt_lun4.getValue());
        txt_sab4.setValue(txt_lun4.getValue());
        //  txt_lun2.focus();
    }

    public String verificar() {
        String valor;
        if (cb_sucursal_man.getValue().isEmpty()) {
            valor = "Debe ingresar una sucursal";
            campo = "sucursal";
        } else if (txt_descripcion.getValue().isEmpty()) {
            valor = "Debe ingresar descripción";
            campo = "descripcion";
        } else if (txt_hro_sem.getValue().isEmpty()) {
            valor = "Debe ingresar codigo de horario semanal";
            campo = "hrsemanal";
        }else if (n_hora_trab.getValue()==null || n_hora_trab.getValue()<=0) {
            valor = "Hora de trabajo debe ser mayor a 0";
            campo = "hrtrabajo";
        }else if (n_hora_refri.getValue() == null  || n_hora_refri.getValue()<=0) {
            valor = "Hora de refrigerio debe ser mayor a 0";
            campo = "hrrefrigerio";
        }else if( n_hora_refri.getValue()> n_hora_trab.getValue()){
            valor = "Hora de refrigerio no puede ser mayor a horas trabajadas";
            campo = "mayor_refri";
        }else if((n_min_tole.getValue()!=null && (n_min_tole.getValue()/60)>n_hora_trab.getValue()/*&&  n_min_tole.getValue()/60)>n_hora_trab.getValue()*/)){
            valor = "Hora de tolerancia no puede ser mayor a horas trabajadas";
            campo = "min_tol";
        }else if(n_min_tole.getValue()!=null && (n_min_tole.getValue()/60)>n_hora_refri.getValue()){
            valor = "Hora de tolerancia no puede ser mayor a horas de refrigerio";
            campo = "min_tol2";
        }else {
            valor = "";
        }
        return valor;
    }

    //metodos de validacion 
    public String verificar1() {
        String valor2;
        if (txt_lun2.getValue().before(txt_lun1.getValue())) {
            valor2 = "Salida de no puede ser igual o mayor a ingreso";
            campo = "uno";
        } else {
            valor2 = "";
        }

        return valor2;
    }

    public void paso1() {
        if (campo.equals("uno")) {
            txt_lun2.focus();
        }
    }

    public ManHorarios generaRegistro() {
//Integer.valueOf((String)IdProviderComboBox.getSelectedItem());
        int sucursal;
        int numero_marcado = 0;
        String iempresa, srefrigerio, irefrigerio, sempresa;
        int s_iempresa, s_srefrigerio, s_irefrigerio, s_sempresa;
        String i_amanecida;
        String desde, descanso;
        String codigo = objHorarios.getMan_codigo();
        sucursal = (Integer) cb_sucursal_man.getSelectedItem().getValue();//cb_sucursal_man.getValue();  
        String tipo = (String) cb_tipo.getSelectedItem().getValue();//cb_tipo.getValue();
        String descripcion = txt_descripcion.getValue();
        if (chk_amanecida.isChecked()) {
            i_amanecida = "S";

        } else {
            i_amanecida = "N";
        }
        int rango = n_rango.getValue() == null ? 0 : n_rango.getValue();
        int hr_trabajo =  n_hora_trab.getValue();//n_hora_trab.getValue() == null ? 0 :
        int hr_refrigerio =  n_hora_refri.getValue();//n_hora_refri.getValue() == null ? 0 :
        int hr_tolerancia = n_min_tole.getValue() == null ? 0 : n_min_tole.getValue();
        String hr_semanal = txt_hro_sem.getValue();
        if (objHor_Sem != null) {
            desde = objHor_Sem.getDia_inicio();//txt_desde.getValue();
            descanso = objHor_Sem.getDia_descanso();//txt_descanso.getValue();
        } else {
            desde = objHorarios.getDia_inicio();
            descanso = objHorarios.getDia_descanso();
        }

        if (chk_iempresa.isChecked()) {
            iempresa = "1";
            s_iempresa = 1;
        } else {
            iempresa = "0";
            s_iempresa = 0;
        }
        if (chk_srefrigerio.isChecked()) {
            srefrigerio = "1";
            s_srefrigerio = 1;
        } else {
            srefrigerio = "0";
            s_srefrigerio = 0;
        }
        if (chk_irefrigerio.isChecked()) {
            irefrigerio = "1";
            s_irefrigerio = 1;
        } else {
            irefrigerio = "0";
            s_irefrigerio = 0;
        }
        if (chk_sempresa.isChecked()) {
            sempresa = "1";
            s_sempresa = 1;
        } else {
            sempresa = "0";
            s_sempresa = 0;
        }
        numero_marcado = s_iempresa + s_srefrigerio + s_irefrigerio + s_sempresa;
        String lunes1 = txt_lun1.getValue() == null ? "00:00" : sdf.format(txt_lun1.getValue());//ParseException.format(txt_lun1.getValue());
        String lunes2 = txt_lun2.getValue() == null ? "00:00" : sdf.format(txt_lun2.getValue());
        String lunes3 = txt_lun3.getValue() == null ? "00:00" : sdf.format(txt_lun3.getValue());
        String lunes4 = txt_lun4.getValue() == null ? "00:00" : sdf.format(txt_lun4.getValue());
        String martes1 = txt_mar1.getValue() == null ? "00:00" : sdf.format(txt_mar1.getValue());
        String martes2 = txt_mar2.getValue() == null ? "00:00" : sdf.format(txt_mar2.getValue());
        String martes3 = txt_mar3.getValue() == null ? "00:00" : sdf.format(txt_mar3.getValue());
        String martes4 = txt_mar4.getValue() == null ? "00:00" : sdf.format(txt_mar4.getValue());
        String miercoles1 = txt_mie1.getValue() == null ? "00:00" : sdf.format(txt_mie1.getValue());
        String miercoles2 = txt_mie2.getValue() == null ? "00:00" : sdf.format(txt_mie2.getValue());
        String miercoles3 = txt_mie3.getValue() == null ? "00:00" : sdf.format(txt_mie3.getValue());
        String miercoles4 = txt_mie4.getValue() == null ? "00:00" : sdf.format(txt_mie4.getValue());
        String jueves1 = txt_jue1.getValue() == null ? "00:00" : sdf.format(txt_jue1.getValue());
        String jueves2 = txt_jue2.getValue() == null ? "00:00" : sdf.format(txt_jue2.getValue());
        String jueves3 = txt_jue3.getValue() == null ? "00:00" : sdf.format(txt_jue3.getValue());
        String jueves4 = txt_jue4.getValue() == null ? "00:00" : sdf.format(txt_jue4.getValue());
        String viernes1 = txt_vie1.getValue() == null ? "00:00" : sdf.format(txt_vie1.getValue());
        String viernes2 = txt_vie2.getValue() == null ? "00:00" : sdf.format(txt_vie2.getValue());
        String viernes3 = txt_vie3.getValue() == null ? "00:00" : sdf.format(txt_vie3.getValue());
        String viernes4 = txt_vie4.getValue() == null ? "00:00" : sdf.format(txt_vie4.getValue());
        String sabado1 = txt_sab1.getValue() == null ? "00:00" : sdf.format(txt_sab1.getValue());
        String sabado2 = txt_sab2.getValue() == null ? "00:00" : sdf.format(txt_sab2.getValue());
        String sabado3 = txt_sab3.getValue() == null ? "00:00" : sdf.format(txt_sab3.getValue());
        String sabado4 = txt_sab4.getValue() == null ? "00:00" : sdf.format(txt_sab4.getValue());
        String domingo1 = txt_dom1.getValue() == null ? "00:00" : sdf.format(txt_dom1.getValue());
        String domingo2 = txt_dom2.getValue() == null ? "00:00" : sdf.format(txt_dom2.getValue());
        String domingo3 = txt_dom3.getValue() == null ? "00:00" : sdf.format(txt_dom3.getValue());
        String domingo4 = txt_dom4.getValue() == null ? "00:00" : sdf.format(txt_dom4.getValue());
        int antes1 = txt_ant1.getValue() == null ? 0 : txt_ant1.getValue();
        int antes2 = txt_ant2.getValue() == null ? 0 : txt_ant2.getValue();
        int antes3 = txt_ant3.getValue() == null ? 0 : txt_ant3.getValue();
        int antes4 = txt_ant4.getValue() == null ? 0 : txt_ant4.getValue();
        int despues1 = txt_des1.getValue() == null ? 0 : txt_des1.getValue();
        int despues2 = txt_des2.getValue() == null ? 0 : txt_des2.getValue();
        int despues3 = txt_des3.getValue() == null ? 0 : txt_des3.getValue();
        int despues4 = txt_des4.getValue() == null ? 0 : txt_des4.getValue();
        return new ManHorarios(codigo, sucursal, descripcion, tipo, i_amanecida,
                rango, hr_trabajo, hr_refrigerio, hr_tolerancia, hr_semanal,
                desde, descanso, iempresa, srefrigerio, irefrigerio, sempresa,
                lunes1, lunes2, lunes3, lunes4, martes1, martes2, martes3, martes4,
                miercoles1, miercoles2, miercoles3, miercoles4, jueves1, jueves2, jueves3, jueves4,
                viernes1, viernes2, viernes3, viernes4, sabado1, sabado2, sabado3, sabado4,
                domingo1, domingo2, domingo3, domingo4, antes1, antes2, antes3, antes4,
                despues1, despues2, despues3, despues4, numero_marcado);
    }

    //metodos sin utilizar
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_ant1.setValue(null);
        txt_ant2.setValue(null);
        txt_ant3.setValue(null);
        txt_ant4.setValue(null);
        txt_codigo.setValue(null);
        txt_des1.setValue(null);
        txt_des2.setValue(null);
        txt_des3.setValue(null);
        txt_des4.setValue(null);
        txt_descanso.setValue(null);
        txt_descripcion.setValue(null);
        txt_desde.setValue(null);
        txt_hasta.setValue(null);
        txt_hro_sem.setValue("");
        txt_jue1.setValue(null);
        txt_jue2.setValue(null);
        txt_jue3.setValue(null);
        txt_jue4.setValue(null);
        txt_lun1.setValue(null);
        txt_lun2.setValue(null);
        txt_lun3.setValue(null);
        txt_lun4.setValue(null);
        txt_mar1.setValue(null);
        txt_mar2.setValue(null);
        txt_mar3.setValue(null);
        txt_mar4.setValue(null);
        txt_mie1.setValue(null);
        txt_mie2.setValue(null);
        txt_mie3.setValue(null);
        txt_mie4.setValue(null);
        txt_sab1.setValue(null);
        txt_sab2.setValue(null);
        txt_sab3.setValue(null);
        txt_sab4.setValue(null);
        txt_usua.setValue(null);
        txt_usumo.setValue(null);
        d_fec.setValue(null);
        d_fecmo.setValue(null);
        txt_vie1.setValue(null);
        txt_vie2.setValue(null);
        txt_vie3.setValue(null);
        txt_vie4.setValue(null);
        txt_dom1.setValue(null);
        txt_dom2.setValue(null);
        txt_dom3.setValue(null);
        txt_dom4.setValue(null);
        n_hora_refri.setValue(null);
        n_hora_trab.setValue(null);
        n_min_tole.setValue(null);
        n_rango.setValue(null);
        chk_amanecida.setChecked(false);
        chk_iempresa.setChecked(false);
        chk_irefrigerio.setChecked(false);
        chk_sempresa.setChecked(false);
        chk_srefrigerio.setChecked(false);
        cb_sucursal_man.setValue("");

    }

    public void habilitaCampos(boolean b_valida) {
        txt_ant1.setDisabled(b_valida);
        txt_ant2.setDisabled(b_valida);
        txt_ant3.setDisabled(b_valida);
        txt_ant4.setDisabled(b_valida);
        // txt_codigo.setDisabled(b_valida);
        txt_des1.setDisabled(b_valida);
        txt_des2.setDisabled(b_valida);
        txt_des3.setDisabled(b_valida);
        txt_des4.setDisabled(b_valida);
        // txt_descanso.setDisabled(b_valida);
        txt_descripcion.setDisabled(b_valida);
        //  txt_desde.setDisabled(b_valida);
        //  txt_hasta.setDisabled(b_valida);
        txt_hro_sem.setDisabled(b_valida);
        txt_jue1.setDisabled(b_valida);
        txt_jue2.setDisabled(b_valida);
        txt_jue3.setDisabled(b_valida);
        txt_jue4.setDisabled(b_valida);
        txt_lun1.setDisabled(b_valida);
        txt_lun2.setDisabled(b_valida);
        txt_lun3.setDisabled(b_valida);
        txt_lun4.setDisabled(b_valida);
        txt_mar1.setDisabled(b_valida);
        txt_mar2.setDisabled(b_valida);
        txt_mar3.setDisabled(b_valida);
        txt_mar4.setDisabled(b_valida);
        txt_mie1.setDisabled(b_valida);
        txt_mie2.setDisabled(b_valida);
        txt_mie3.setDisabled(b_valida);
        txt_mie4.setDisabled(b_valida);
        txt_sab1.setDisabled(b_valida);
        txt_sab2.setDisabled(b_valida);
        txt_sab3.setDisabled(b_valida);
        txt_sab4.setDisabled(b_valida);
        txt_dom1.setDisabled(b_valida);
        txt_dom2.setDisabled(b_valida);
        txt_dom3.setDisabled(b_valida);
        txt_dom4.setDisabled(b_valida);
        //txt_usua.setValue("");
        //txt_usumo.setValue("");
        txt_vie1.setDisabled(b_valida);
        txt_vie2.setDisabled(b_valida);
        txt_vie3.setDisabled(b_valida);
        txt_vie4.setDisabled(b_valida);
        n_hora_refri.setDisabled(b_valida);
        n_hora_trab.setDisabled(b_valida);
        n_min_tole.setDisabled(b_valida);
        n_rango.setDisabled(b_valida);
        chk_amanecida.setDisabled(b_valida);
        chk_iempresa.setDisabled(b_valida);
        chk_irefrigerio.setDisabled(b_valida);
        chk_sempresa.setDisabled(b_valida);
        chk_srefrigerio.setDisabled(b_valida);
        cb_sucursal_man.setDisabled(b_valida);
        cb_tipo.setDisabled(b_valida);

    }

    public void llenarCampos() throws ParseException {

        txt_codigo.setValue(objHorarios.getMan_codigo());
        cb_sucursal_man.setValue(objHorarios.getS_sucursal());
        txt_descripcion.setValue(objHorarios.getMan_descripcion());
        int i = Integer.parseInt(objHorarios.getHor_tiphora());
        cb_tipo.setSelectedIndex(i-1);
        //cb_tipo.setValue(objHorarios.getTipo()); //objHorarios.getTipo()
        //  cb_tipo.setSelectedItem(Utilitarios.textoPorTexto(cb_tipo, objHorarios.getTipo()));
        n_rango.setValue(objHorarios.getMan_rango());
        n_hora_trab.setValue(objHorarios.getMan_hr_trabajadas());
        n_hora_refri.setValue(objHorarios.getMan_hr_refrigerio());
        n_min_tole.setValue(objHorarios.getMan_hr_tolerancia());
        txt_hro_sem.setValue(objHorarios.getMan_hr_semanal());
        txt_desde.setValue(objHorarios.getMan_de());
        txt_hasta.setValue(objHorarios.getMan_a());
        txt_descanso.setValue(objHorarios.getMan_descanso());
        // txt_lun1.setValue(objHorarios.getMan_lun1());
        txt_lun1.setValue(sdf.parse(objHorarios.getMan_lun1()));
        txt_lun2.setValue(sdf.parse(objHorarios.getMan_lun2()));
        txt_lun3.setValue(sdf.parse(objHorarios.getMan_lun3()));
        txt_lun4.setValue(sdf.parse(objHorarios.getMan_lun4()));

        txt_mar1.setValue(sdf.parse(objHorarios.getMan_mar1()));
        txt_mar2.setValue(sdf.parse(objHorarios.getMan_mar2()));
        txt_mar3.setValue(sdf.parse(objHorarios.getMan_mar3()));
        txt_mar4.setValue(sdf.parse(objHorarios.getMan_mar4()));

        txt_mie1.setValue(sdf.parse(objHorarios.getMan_mier1()));
        txt_mie2.setValue(sdf.parse(objHorarios.getMan_mier2()));
        txt_mie3.setValue(sdf.parse(objHorarios.getMan_mier3()));
        txt_mie4.setValue(sdf.parse(objHorarios.getMan_mier4()));

        txt_jue1.setValue(sdf.parse(objHorarios.getMan_juev1()));
        txt_jue2.setValue(sdf.parse(objHorarios.getMan_juev2()));
        txt_jue3.setValue(sdf.parse(objHorarios.getMan_juev3()));
        txt_jue4.setValue(sdf.parse(objHorarios.getMan_juev4()));

        txt_vie1.setValue(sdf.parse(objHorarios.getMan_vie1()));
        txt_vie2.setValue(sdf.parse(objHorarios.getMan_vie2()));
        txt_vie3.setValue(sdf.parse(objHorarios.getMan_vie3()));
        txt_vie4.setValue(sdf.parse(objHorarios.getMan_vie4()));

        txt_sab1.setValue(sdf.parse(objHorarios.getMan_sab1()));
        txt_sab2.setValue(sdf.parse(objHorarios.getMan_sab2()));
        txt_sab3.setValue(sdf.parse(objHorarios.getMan_sab3()));
        txt_sab4.setValue(sdf.parse(objHorarios.getMan_sab4()));

        txt_dom1.setValue(sdf.parse(objHorarios.getMan_dom1()));
        txt_dom2.setValue(sdf.parse(objHorarios.getMan_dom2()));
        txt_dom3.setValue(sdf.parse(objHorarios.getMan_dom3()));
        txt_dom4.setValue(sdf.parse(objHorarios.getMan_dom4()));

        txt_ant1.setValue(objHorarios.getMan_ant1());
        txt_ant2.setValue(objHorarios.getMan_ant2());
        txt_ant3.setValue(objHorarios.getMan_ant3());
        txt_ant4.setValue(objHorarios.getMan_ant4());

        txt_des1.setValue(objHorarios.getMan_des1());
        txt_des2.setValue(objHorarios.getMan_des2());
        txt_des3.setValue(objHorarios.getMan_des3());
        txt_des4.setValue(objHorarios.getMan_des4());

        txt_usua.setValue(objHorarios.getUsu_add());
        txt_usumo.setValue(objHorarios.getUsu_mod());
        d_fec.setValue(objHorarios.getDia_add());
        d_fecmo.setValue(objHorarios.getDia_mod());
        //cb_sucursal_man.setSelectedItem();
        //llenado de check
        if (objHorarios.getMan_amanecida().equals("S")) {
            chk_amanecida.setChecked(true);
        } else {
            chk_amanecida.setChecked(false);
        }
        if (objHorarios.getMan_iempresa().equals("1")) {
            chk_iempresa.setChecked(true);
        } else {
            chk_iempresa.setChecked(false);
        }
        if (objHorarios.getMan_srefrigerio().equals("1")) {
            chk_srefrigerio.setChecked(true);
        } else {
            chk_srefrigerio.setChecked(false);
        }
        if (objHorarios.getMan_irefrigerio().equals("1")) {
            chk_irefrigerio.setChecked(true);
        } else {
            chk_irefrigerio.setChecked(false);
        }
        if (objHorarios.getMan_sempresa().equals("1")) {
            chk_sempresa.setChecked(true);
        } else {
            chk_sempresa.setChecked(false);
        }

    }

    public void limpiarCampos234() {
        //  txt_lun1.setValue(null);
        txt_lun2.setValue(null);
        txt_lun3.setValue(null);
        txt_lun4.setValue(null);
        //  txt_mar1.setValue(null);
        txt_mar2.setValue(null);
        txt_mar3.setValue(null);
        txt_mar4.setValue(null);
        //  txt_mie1.setValue(null);
        txt_mie2.setValue(null);
        txt_mie3.setValue(null);
        txt_mie4.setValue(null);
        //    txt_jue1.setValue(null);
        txt_jue2.setValue(null);
        txt_jue3.setValue(null);
        txt_jue4.setValue(null);
        //  txt_vie1.setValue(null);
        txt_vie2.setValue(null);
        txt_vie3.setValue(null);
        txt_vie4.setValue(null);
        //   txt_sab1.setValue(null);
        txt_sab2.setValue(null);
        txt_sab3.setValue(null);
        txt_sab4.setValue(null);
        //   txt_dom1.setValue(null);
        txt_dom2.setValue(null);
        txt_dom3.setValue(null);
        txt_dom4.setValue(null);
        //  txt_ant1.setValue(null);
        txt_ant2.setValue(null);
        txt_ant3.setValue(null);
        txt_ant4.setValue(null);
        //  txt_des1.setValue(null);
        txt_des2.setValue(null);
        txt_des3.setValue(null);
        txt_des4.setValue(null);
        //   txt_descanso.setValue(null);
        //   txt_descripcion.setValue(null);

    }

    public void limpiarCampos34() {
        //  txt_lun1.setValue(null);
        //  txt_lun2.setValue(null);
        txt_lun3.setValue(null);
        txt_lun4.setValue(null);
        //  txt_mar1.setValue(null);
        //  txt_mar2.setValue(null);
        txt_mar3.setValue(null);
        txt_mar4.setValue(null);
        //  txt_mie1.setValue(null);
        //   txt_mie2.setValue(null);
        txt_mie3.setValue(null);
        txt_mie4.setValue(null);
        //    txt_jue1.setValue(null);
        //  txt_jue2.setValue(null);
        txt_jue3.setValue(null);
        txt_jue4.setValue(null);
        //  txt_vie1.setValue(null);
        //   txt_vie2.setValue(null);
        txt_vie3.setValue(null);
        txt_vie4.setValue(null);
        //   txt_sab1.setValue(null);
        //    txt_sab2.setValue(null);
        txt_sab3.setValue(null);
        txt_sab4.setValue(null);
        //   txt_dom1.setValue(null);
        //    txt_dom2.setValue(null);
        txt_dom3.setValue(null);
        txt_dom4.setValue(null);
        //  txt_ant1.setValue(null);
        // txt_ant2.setValue(null);
        txt_ant3.setValue(null);
        txt_ant4.setValue(null);
        //  txt_des1.setValue(null);
        // txt_des2.setValue(null);
        txt_des3.setValue(null);
        txt_des4.setValue(null);
        //   txt_descanso.setValue(null);
        //   txt_descripcion.setValue(null);

    }

    public void limpiarCampos4() {
        //  txt_lun1.setValue(null);
        //  txt_lun2.setValue(null);
        //txt_lun3.setValue(null);
        txt_lun4.setValue(null);
        //  txt_mar1.setValue(null);
        //  txt_mar2.setValue(null);
        // txt_mar3.setValue(null);
        txt_mar4.setValue(null);
        //  txt_mie1.setValue(null);
        //   txt_mie2.setValue(null);
        // txt_mie3.setValue(null);
        txt_mie4.setValue(null);
        //    txt_jue1.setValue(null);
        //  txt_jue2.setValue(null);
        //  txt_jue3.setValue(null);
        txt_jue4.setValue(null);
        //  txt_vie1.setValue(null);
        //   txt_vie2.setValue(null);
        // txt_vie3.setValue(null);
        txt_vie4.setValue(null);
        //   txt_sab1.setValue(null);
        //    txt_sab2.setValue(null);
        //  txt_sab3.setValue(null);
        txt_sab4.setValue(null);
        //   txt_dom1.setValue(null);
        //    txt_dom2.setValue(null);
        //  txt_dom3.setValue(null);
        txt_dom4.setValue(null);
        //  txt_ant1.setValue(null);
        // txt_ant2.setValue(null);
        //  txt_ant3.setValue(null);
        txt_ant4.setValue(null);
        //  txt_des1.setValue(null);
        // txt_des2.setValue(null);
        // txt_des3.setValue(null);
        txt_des4.setValue(null);
        //   txt_descanso.setValue(null);
        //   txt_descripcion.setValue(null);

    }

    public void campoSeteado() {
        txt_lun1.setText("00:00");
        txt_mar1.setText("00:00");
        txt_mie1.setText("00:00");
        txt_jue1.setText("00:00");
        txt_vie1.setText("00:00");
        txt_sab1.setText("00:00");
        txt_dom1.setText("00:00");

        txt_lun2.setText("00:00");
        txt_mar2.setText("00:00");
        txt_mie2.setText("00:00");
        txt_jue2.setText("00:00");
        txt_vie2.setText("00:00");
        txt_sab2.setText("00:00");
        txt_dom2.setText("00:00");

        txt_lun3.setText("00:00");
        txt_mar3.setText("00:00");
        txt_mie3.setText("00:00");
        txt_jue3.setText("00:00");
        txt_vie3.setText("00:00");
        txt_sab3.setText("00:00");
        txt_dom3.setText("00:00");

        txt_lun4.setText("00:00");
        txt_mar4.setText("00:00");
        txt_mie4.setText("00:00");
        txt_jue4.setText("00:00");
        txt_vie4.setText("00:00");
        txt_sab4.setText("00:00");
        txt_dom4.setText("00:00");
    }

}
