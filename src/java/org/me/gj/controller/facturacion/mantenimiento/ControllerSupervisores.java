package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerSupervisores extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listasupervisores, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Longbox txt_suptelef, txt_supmovil;
    @Wire
    Textbox txt_supid, txt_supidapo, txt_supapenom, txt_supdni, txt_suppssw, txt_supdirecc,
            txt_supapenomapo, txt_supnomrep, txt_busqueda, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda;
    @Wire
    Combobox cb_busest;
    @Wire
    Spinner sp_supord;
    @Wire
    Checkbox chk_supest, chk_flagcie, chk_busest;
    @Wire
    Listbox lst_sup;
    @Wire
    Datebox dt_supfecnac;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Supervisor> objlstSupervisores = new ListModelList<Supervisor>();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Supervisor objSupervisor = new Supervisor();
    DaoSupervisores objDaoSupervisores = new DaoSupervisores();
    Accesos objAccesos = new Accesos();
    Utilitarios objUtilitarios = new Utilitarios();
    //Variables publicas
    int i_sel;
    int valor;
    String s_estado = "Q", s_mensaje = "", modoEjecucion;
    String campo = "";
    public static boolean bandera;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerSupervisores.class);

    //Eventos Primarios - Transaccionales
    
    @Listen("onCreate=#tabbox_supervisores")
    public void llenaRegistros() throws SQLException {
        objlstSupervisores = objDaoSupervisores.listaSupervisores(1);
        lst_sup.setModel(objlstSupervisores);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
    	lst_sup.focus();

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40106000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Superviores con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Superviores con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo registro de Supervisor de Compra ");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo registro de Supervisor  ");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un registro de Supervisor ");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un registro de Supervisor");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un registro de Supervisor ");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un registro de Supervisor");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Supervisores ");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Supervisores");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {

        limpiarLista();
        limpiarCampos();
        limpiaAuditoria();
        
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;

        if (cb_busest.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_busest.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de supervisor" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el nombre y apellido de supervisor" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el DNI de supervisor " + s_consulta + " para su busqueda");
        }
        objlstSupervisores = objDaoSupervisores.busquedaSupervisores(i_seleccion, s_consulta, i_estado);
        if (objlstSupervisores.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstSupervisores.getSize() + " registro(s)");
        }        
        
        if (objlstSupervisores.getSize() > 0) {
            lst_sup.setModel(objlstSupervisores);
        } else {          
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }


    }

    @Listen("onSelect=#lst_sup")
    public void seleccionaRegistro() throws SQLException {
        objSupervisor = (Supervisor) lst_sup.getSelectedItem().getValue();
        if (objSupervisor == null) {
            objSupervisor = objlstSupervisores.get(i_sel + 1);
        }
        i_sel = lst_sup.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objSupervisor.getSup_id());
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objSupervisor = new Supervisor();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_supest.setDisabled(true);
        chk_flagcie.setDisabled(true);
        s_estado = "N";
        chk_supest.setChecked(true);
        chk_supest.setLabel("ACTIVO");
        chk_flagcie.setLabel("ACTIVO");
        txt_supapenomapo.setDisabled(true);
        txt_supapenom.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_sup.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        	lst_sup.focus();
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_supdni.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        validafocos();
                    }
                }
            });
        } else {            	
	        String s_existedni = existeDni();
	        if (!s_existedni.isEmpty()) {
	            Messagebox.show(s_existedni, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
	                @Override
	                public void onEvent(Event event) throws Exception {
	                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
	                        if (campo.equals("DNI")) {
	                            txt_supdni.focus();
	                        }
	                    }
	                }
	            });
	        } else {
	        	 String s_valifecnac = validaFechaNacimiento();
	        	if (!s_valifecnac.isEmpty()) {
		            Messagebox.show(s_valifecnac, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
		                @Override
		                public void onEvent(Event event) throws Exception {
		                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
		                        if (campo.equals("FecNac")) {
		                            dt_supfecnac.focus();
		                        }
		                    }
		                }
		            });
	        	} else {
	        		 String s_valisupapoyo = validaSupervisorApoyo();
		            if (!s_valisupapoyo.isEmpty()) {
		                lst_sup.focus();
		            } else {
		                s_mensaje = "Esta Seguro que desea guardar los cambios?";
		                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
		                        Messagebox.QUESTION, new EventListener() {
		
		                            @Override
		                            public void onEvent(Event event) throws Exception {
		                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
		                                    objSupervisor = generaRegistro();
		                                    ParametrosSalida objParamSalida;
		                                    if (s_estado.equals("N")) {
		                                        objParamSalida = objDaoSupervisores.insertar(objSupervisor);
		                                    } else {
		                                        objParamSalida = objDaoSupervisores.actualizar(objSupervisor);
		                                    }
		                                    if (objParamSalida.getFlagIndicador() == 0) {
		                                        habilitaTab(false, false);
		                                        seleccionaTab(true, false);
		                                        //validacion de guardar/nuevo
		                                        VerificarTransacciones();
		                                        tbbtn_btn_guardar.setDisabled(true);
		                                        tbbtn_btn_deshacer.setDisabled(true);
		                                        //
		                                        habilitaCampos(true);
		                                        limpiarCampos();
		                                        limpiaAuditoria();
		                                        objlstSupervisores.clear();
		                                        objlstSupervisores = objDaoSupervisores.listaSupervisores(1);
		                                        lst_sup.setModel(objlstSupervisores);
		                                        objSupervisor = new Supervisor();
		                                    }
		                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
		                                    lst_sup.focus();
		                                }
		                            }
		                        });
		            }
		        }
	        }	

        }

    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_sup.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar al Supervisor?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoSupervisores.eliminar(objSupervisor);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    limpiarLista();
                                    objlstSupervisores = objDaoSupervisores.listaSupervisores(1);
                                    lst_sup.setModel(objlstSupervisores);

                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    VerificarTransacciones();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }

                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {        
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {

                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarLista();
                            OnChange();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            busquedaRegistros();
                            lst_sup.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");

                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstSupervisores == null || objlstSupervisores.isEmpty()) {
            Messagebox.show("No hay registros de Supervisores para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
            objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionSup.zul", null, objMapObjetos);
            window.doModal();
        }
    }
    
  
    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_mansup")
    public void GuardarInformacion(Event event) throws SQLException {       
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
                case 119:
                    if (!tbbtn_btn_imprimir.isDisabled()) {
                        botonImprimir();
                    }
                    break;
            }
        
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstSupervisores = new ListModelList<Supervisor>();
            objlstSupervisores = objDaoSupervisores.listaSupervisores(0);
            lst_sup.setModel(objlstSupervisores);
        }
    }

    @Listen("onOK=#txt_supidapo")
    public void busquedaSupervisorApoyo() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_supidapo.getValue().isEmpty()) {
                //Mostrar la Ventana de busqueda de proveedores
                modoEjecucion = "mantSupervisores";
                Map mapeo = new HashMap();
                mapeo.put("txt_supidapo", txt_supidapo);
                mapeo.put("txt_supapenomapo", txt_supapenomapo);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerSupervisores");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSupervisores.zul", null, mapeo);
                window.doModal();
            } else {
                txt_supnomrep.focus();
            }
        }
    }

    @Listen("onOK=#txt_supapenom")
    public void onOKDni() {
        txt_supdni.focus();
    }

    @Listen("onOK=#txt_supdni")
    public void onOKDireccion() {
        txt_supdirecc.focus();
    }

    @Listen("onOK=#txt_supdirecc")
    public void onOKTelefono() {
        txt_suptelef.focus();
    }

    @Listen("onOK=#txt_suptelef")
    public void onOKMovil() {
        txt_supmovil.focus();
    }

    @Listen("onOK=#txt_supmovil")
    public void onOKFecNac() {
        dt_supfecnac.focus();
    }

    @Listen("onOK=#dt_supfecnac")
    public void valida_clifecnac() {
        if (dt_supfecnac.getValue() != null) {
            int edad = objUtilitarios.validaEdadMayor(dt_supfecnac.getValue());
            int sobreedad = objUtilitarios.validaSobreEdad(dt_supfecnac.getValue());
            if (sobreedad > 65 || edad > 65) {
                Messagebox.show("Fecha de Nacimiento Invalida", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        dt_supfecnac.focus();
                    }
                });
            } else if (edad < 18) {
                Messagebox.show("El supervisor es menor de edad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        dt_supfecnac.focus();
                    }
                });
            }
        } else {
            txt_supidapo.focus();
        }
    }

    @Listen("onOK=#txt_supnomrep")
    public void onOKOrden() {
        sp_supord.focus();
    }

    @Listen("onOK=#sp_supord")
    public void onOKWap() {
        txt_suppssw.focus();
    }

    @Listen("onBlur=#txt_supidapo")
    public void validaSupApoyo() throws SQLException {
        if (txt_supidapo.getValue().isEmpty()) {
        	limpiarSupervisor();
        } else {
            txt_supapenomapo.setValue("");
            if (!txt_supidapo.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                           limpiarSupervisor();
                            txt_supidapo.focus();
                        }
                    }
                });
            } else {
                int sup_id = Integer.parseInt(txt_supidapo.getValue());
                Supervisor objSup = objDaoSupervisores.getNomSupervisor(sup_id);
                if (objSup == null) {
                    Messagebox.show("El Supervisor Apoyo no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            	limpiarSupervisor();
                                txt_supidapo.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Supervisor con codigo " + objSup.getSup_id() + " y ha encontrado 1 registro(s)");
                    txt_supidapo.setValue(Utilitarios.lpad(String.valueOf(objSup.getSup_key()), 4, "0"));
                    txt_supapenomapo.setValue(objSup.getSup_apenom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onCheck=#chk_supest")
    public void VerificaEstado() throws SQLException {
        int i_supest = objSupervisor.getSup_est();
        if (objDaoSupervisores.busquedaExistencia(1, txt_supid.getValue()) > 0 || objDaoSupervisores.busquedaExistencia(2, txt_supid.getValue()) > 0) {
            if (i_supest == 1) {
                chk_supest.setChecked(true);
                chk_supest.setLabel("ACTIVO");
                Messagebox.show("El supervisor esta en movimiento", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                chk_supest.setChecked(false);
                chk_supest.setLabel("INACTIVO");
                Messagebox.show("Ya no se puede activar el supervisor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

            }
        }
    }

    //Eventos Otros
    public String verificar() {
        String s_validacion;
        if (txt_supapenom.getValue().isEmpty()) {
            s_validacion = "El campo 'APELLIDOS NOMBRES' es obligatorio";
            campo = "Nombres";
        } /*else if (!txt_supapenom.getValue().matches("^\\s+")) {
            s_validacion = "El campo 'APELLIDOS NOMBRES', no debe tener espacios en blanco al principio";
            campo = "Nombres";
            txt_supapenom.setValue("");
        }*/ else if (!txt_supapenom.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_validacion = "El campo 'APELLIDOS NOMBRES', no se permite caracteres especiales";
            campo = "Nombres";
            txt_supapenom.setValue("");
        } else if (txt_supdni.getValue().isEmpty()) {
            s_validacion = "El campo 'DNI' es obligatorio";
            campo = "Dni";
        } else if(txt_supdni.getValue().length()< 8){
        	s_validacion = "Por favor ingrese 8 dígitos en el campo 'DNI'";
        	 campo = "Dni";
        } else if (txt_supdirecc.getValue().isEmpty()) {
            s_validacion = "El campo 'DIRECCION' es obligatorio";
            campo = "Direccion";
        }/*else if (!txt_supdirecc.getValue().matches("^\\s+")) {
            s_validacion = "El campo 'DIRECCION' no debe tener espacios en blanco al principio";
            campo = "Direccion";
            txt_supdirecc.setValue("");
        }*/ else if (!txt_supdirecc.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_validacion = "El campo 'DIRECCION', no se permite caracteres especiales";
            campo = "Direccion";
            txt_supdirecc.setValue("");
        }  /*else if (txt_suptelef.getValue() == null) {
            s_validacion = "El campo 'TELEFONO FIJO' es obligatorio";
            campo = "Telefono";
        } else if (txt_supmovil.getValue() == null) {
            s_validacion = "El campo 'CELULAR' es obligatorio";
            campo = "Movil";
        }*/ else if (dt_supfecnac.getValue() == null) {
            s_validacion = "Ingrese fecha de nacimiento";
            campo = "FecNac";
        }  else if (txt_suppssw.getValue().isEmpty()) {
            s_validacion = "El campo 'CODIGO WAP' es obligatorio";
            campo = "WAP";
        } else {
            s_validacion = "";
        }
        return s_validacion;
    }

    public void validafocos(){
    	if (campo.equals("Nombres")) {
            txt_supapenom.focus();
        } else if (campo.equals("Dni")) {
            txt_supdni.focus();
        } else if (campo.equals("Direccion")) {
            txt_supdirecc.focus();
        } else if (campo.equals("Telefono")) {
            txt_suptelef.focus();
        } else if (campo.equals("Movil")) {
            txt_supmovil.focus();
        } else if (campo.equals("FecNac")) {
            dt_supfecnac.focus();
        }  else if (campo.equals("WAP")) {
            txt_suppssw.focus();
        }
    }
    
   /* public String Verificadni() {
        String s_valor_dni = "";
        // txt_dni.setConstraint("/^[0-9]+$/: Por Favor Solo ingrese Números");
        String s_dni = txt_supdni.getValue();
        if (s_dni.length() < 8) {
            s_valor_dni = "Por favor ingrese un Dni de 8 numeros";
        }
        return s_valor_dni;
    }*/

    public String validaSupervisorApoyo() throws SQLException {
        String s_validasupap;
        if (txt_supidapo.getValue().isEmpty()) {
            s_validasupap = "";
        } else {
            if (txt_supidapo.getValue().matches("[0-9]*")) {
                int sup_id = Integer.parseInt(txt_supidapo.getValue());
                Supervisor objSup = objDaoSupervisores.getNomSupervisor(sup_id);
                if (objSup != null) {
                    s_validasupap = "";
                } else {
                    s_validasupap = "El Supervisor no existe o esta eliminado";
                }
            } else {
                s_validasupap = "Ingrese Valores numericos";
            }
        }
        return s_validasupap;
    }

    public String validaFechaNacimiento() {
        String s_valifecnac = "";
        if (dt_supfecnac.getValue() != null) {
            int edad = objUtilitarios.validaEdadMayor(dt_supfecnac.getValue());
            int sobreedad = objUtilitarios.validaSobreEdad(dt_supfecnac.getValue());
            if (sobreedad > 65 || edad > 65) {
                s_valifecnac = "Fecha de Nacimiento Invalida";
                campo = "FecNac";
            } else if (edad < 18) {
                s_valifecnac = "El supervisor es menor de edad";
                campo = "FecNac";

            }
        }

        return s_valifecnac;
    }

    public String existeDni() throws SQLException {
        String s_valida = "";
        Supervisor objSupDni;
        if (s_estado.equals("N")) {
            if (!txt_supdni.getValue().isEmpty()) {
                if (txt_supdni.getValue().matches("[0-9]*")) {
                    objSupDni = objDaoSupervisores.existeDNI(txt_supdni.getValue());
                    if (objSupDni != null) {
                        if (objSupDni.getSup_dni().equals(txt_supdni.getValue())) {
                            s_valida = "El dni ya existe, verifique por favor";
                            campo = "DNI";
                        }
                    }
                } else {
                    txt_supdni.setValue("");
                    s_valida = "Ingrese valores numericos en el campo 'DNI'";
                    campo = "DNI";
                }
            }
        }
        return s_valida;
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setValue("%%");
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstSupervisores = new ListModelList<Supervisor>();
        lst_sup.setModel(objlstSupervisores);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasupervisores.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listasupervisores.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void llenarCampos() {
        txt_supid.setValue(objSupervisor.getSup_id());
        txt_supapenom.setValue(objSupervisor.getSup_apenom());
        txt_supdni.setValue(objSupervisor.getSup_dni());
        txt_supdirecc.setValue(objSupervisor.getSup_direcc());
        txt_suppssw.setValue(objSupervisor.getSup_pssw());
        txt_suptelef.setValue(objSupervisor.getSup_telf());
        txt_supmovil.setValue(objSupervisor.getSup_movil());
        dt_supfecnac.setValue(objSupervisor.getSup_fecnac());
        chk_supest.setChecked(objSupervisor.isValor());
        chk_supest.setLabel(objSupervisor.getSup_est() == 1 ? "ACTIVO" : "INACTIVO");
        chk_flagcie.setChecked(objSupervisor.getSup_flagcie() == 1);
        chk_flagcie.setLabel(objSupervisor.getSup_flagcie() == 1 ? "ACTIVO" : "INACTIVO");
        txt_supidapo.setValue(objSupervisor.getSup_apoyo());
        txt_supapenomapo.setValue(objSupervisor.getSup_apoyodes());
        txt_supnomrep.setValue(objSupervisor.getSup_nomrep());
        sp_supord.setValue(objSupervisor.getSup_ord());
        txt_usuadd.setValue(objSupervisor.getSup_usuadd());
        d_fecadd.setValue(objSupervisor.getSup_fecadd());
        txt_usumod.setValue(objSupervisor.getSup_usumod());
        d_fecmod.setValue(objSupervisor.getSup_fecmod());
    }

    public void limpiarCampos() {
        txt_supid.setValue(null);
        chk_supest.setChecked(true);
        chk_supest.setLabel("ACTIVO");
        txt_supapenom.setValue("");
        txt_supdni.setValue("");
        txt_supdirecc.setValue("");
        txt_suppssw.setValue("");
        txt_suptelef.setValue(null);
        txt_supmovil.setValue(null);
        dt_supfecnac.setValue(null);
        chk_flagcie.setChecked(true);
        chk_flagcie.setLabel("ACTIVO");
        txt_supidapo.setValue(null);
        txt_supapenomapo.setValue("");
        txt_supnomrep.setValue("");
        sp_supord.setValue(0);
    }

    public void limpiarSupervisor(){
        txt_supidapo.setValue("");
        txt_supapenomapo.setValue("");
    	 
    }
    
    public void habilitaCampos(boolean b_valida1) {
        chk_supest.setDisabled(b_valida1);
        txt_supapenom.setDisabled(b_valida1);
        txt_supdni.setDisabled(b_valida1);
        txt_supdirecc.setDisabled(b_valida1);
        txt_suppssw.setDisabled(b_valida1);
        txt_suptelef.setDisabled(b_valida1);
        txt_supmovil.setDisabled(b_valida1);
        dt_supfecnac.setDisabled(b_valida1);
        chk_flagcie.setDisabled(b_valida1);
        txt_supidapo.setDisabled(b_valida1);
        txt_supapenomapo.setDisabled(b_valida1);
        txt_supnomrep.setDisabled(b_valida1);
        sp_supord.setDisabled(b_valida1);
    }

    public Supervisor generaRegistro() {
        int sup_key = objSupervisor.getSup_key();
        String sup_id = objSupervisor.getSup_id();
        String sup_apenom = txt_supapenom.getValue().toUpperCase();
        String sup_dni = txt_supdni.getValue();
        String sup_direcc = txt_supdirecc.getValue().toUpperCase();
        long sup_telf = txt_suptelef.getValue()==null?0:txt_suptelef.getValue();
        long sup_movil = txt_supmovil.getValue()==null?0:txt_supmovil.getValue();
        Date sup_fecnac = dt_supfecnac.getValue();
        int sup_est = chk_supest.isChecked() ? 1 : 2;
        String sup_usuadd = objUsuCredential.getCuenta().toUpperCase();
        String sup_usumod = objUsuCredential.getCuenta().toUpperCase();
        String sup_pssw = txt_suppssw.getValue().toUpperCase();
        int sup_flagcie = chk_flagcie.isChecked() ? 1 : 0;
        String sup_apoyo = txt_supidapo.getValue().isEmpty() ? "" : txt_supidapo.getValue();
        int suc_id = objUsuCredential.getCodsuc();
        int emp_id = objUsuCredential.getCodemp();
        String sup_nomrep = txt_supnomrep.getValue().toUpperCase();
        int sup_ord = sp_supord.getValue();
        return new Supervisor(sup_key, sup_id, sup_apenom, sup_dni, sup_direcc, sup_telf, sup_movil, sup_fecnac,
                sup_est, sup_usuadd, sup_usumod, sup_pssw, sup_flagcie, sup_apoyo, suc_id,
                emp_id, sup_nomrep, sup_ord);
    }

}
