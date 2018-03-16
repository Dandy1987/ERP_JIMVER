/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import org.zkoss.zk.ui.event.Event;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.EditorFormulas;
import org.me.gj.model.planillas.mantenimiento.Formulas;
import org.me.gj.model.planillas.mantenimiento.Tablas;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author ROJAS
 */
public class ControllerFunciones extends SelectorComposer<Component> {

    @Wire
    Tab tab_listaformulas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_editar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, //tbbtn_btn_nuevo,tbbtn_btn_eliminar,
            tbbtn_add_conceptos, tbbtn_add_operadores, tbbtn_add_constantes, tbbtn_clean_crud, tbbtn_deshacer_crud;
    @Wire
    Combobox cb_estado, cb_busqueda, cb_conceptos, cb_operadores;
    @Wire
    Datebox d_fecadd, d_fecmod, db_fecha_constante;
    @Wire
    Textbox txt_busqueda, txt_usuadd, txt_usumod, txt_editor_formula,
            txt_texto_constantes, txt_constantes, txt_idform, txt_desform;
    @Wire
    Listbox lst_formulas, lst_conceptos;
    @Wire
    Button btn_buscar, btn_clonar;
    @Wire
    Intbox txt_numeracion;
    @Wire
    Checkbox chk_estado;
    @Wire
    Radiogroup rg_constantes;
    @Wire
    Radio rad_btn_tex, rad_btn_num, rad_btn_fec;
    //Declaracion de variables
    String s_estado, campo, s_mensaje;
    int i_sel, valor;
    //Declaracion de Listas de Objetos
    ListModelList<Tablas> objListModelTablas;
    ListModelList<EditorFormulas> objListEditorFormulas;
    ListModelList<Tablas> objListOperadores;
    ListModelList<Formulas> objListModelFormulas;
    //Declaracion de Objetos
    EditorFormulas objEditorFormulas;
    Formulas objFormulas;
    DaoFunciones objDaoFunciones;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    ParametrosSalida objParametrosSalida;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUbicaciones.class);
    private String s_concepto_total = "";
    private final String _SQL_OPERADORES = " SELECT P.PLDATFOR_DVIEW CONCEPTO, P.PLDATFOR_DFORM FORMULA , PLDATFOR_DVIEW CONCEPTO_PURO FROM TPLDATFOR P WHERE P.PLDATFOR_IDT = '100' AND P.PLDATFOR_IDT <> '000' AND P.EMP_ID = " + objUsuCredential.getCodemp() + " AND P.SUC_ID = " + objUsuCredential.getCodsuc();

    /**
     * Metodo Constructor
     *
     * @param comp
     * @throws Exception
     */
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objListEditorFormulas = new ListModelList<EditorFormulas>();
        objDaoFunciones = new DaoFunciones();
        // Cargar combo con operadores                
        objListOperadores = new ListModelList<Tablas>();
        objListOperadores = objDaoFunciones.getListBoxTablas(_SQL_OPERADORES);
        cb_operadores.setModel(objListOperadores);
        // Cargar data para la lista principal
        cb_estado.setSelectedIndex(0);
        objListModelFormulas = new ListModelList<Formulas>();
        objListModelFormulas = objDaoFunciones.listaFormulas(1);
        lst_formulas.setModel(objListModelFormulas);
        //Deshabilitar los Tab
        habilitaTab(false, true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101080, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Funciones con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Funciones con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            //tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Funcion");
        } else {
            //tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Funcion");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Funcion");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Funcion");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Funcion");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Funcion");
        }
    }

    /**
     * Metodo que se ejecuta cuando se selecciona un registro del combobox
     */
    @Listen("onSelect=#cb_conceptos")
    public void seleccionConceptos() {
        try {
            //Cuando seleccione un item del combo filtrar data
            String SQL_TABLA_DATA = null;
            //si seleccionaron PERSONAL
            if (cb_conceptos.getSelectedItem().getValue().equals("1")) {
                //Seleccionar la Tabla de donde sacara los datos <!-- pldatfor p1 -->
                SQL_TABLA_DATA = " SELECT P1.PLDATFOR_DVIEW CONCEPTO, P1.PLDATFOR_DFORM FORMULA , P1.PLDATFOR_DVIEW CONCEPTO_PURO FROM TPLDATFOR P1 WHERE P1.PLDATFOR_IDT = '001' AND P1.EMP_ID = " + objUsuCredential.getCodemp() + " AND P1.SUC_ID = " + objUsuCredential.getCodsuc() + "ORDER BY 1 ";
            }//si seleccionaron ASISTENCIA
            else if (cb_conceptos.getSelectedItem().getValue().equals("2")) {
                // Tabla <!-- pldatfor p2 -->
                SQL_TABLA_DATA = " SELECT P2.PLDATFOR_DVIEW CONCEPTO, P2.PLDATFOR_DFORM FORMULA , P2.PLDATFOR_DVIEW CONCEPTO_PURO FROM TPLDATFOR P2 WHERE P2.PLDATFOR_IDT = '002' AND P2.EMP_ID = " + objUsuCredential.getCodemp() + " AND P2.SUC_ID = " + objUsuCredential.getCodsuc() + "ORDER BY 1 ";
            }//si seleccionaron TABLAS
            else if (cb_conceptos.getSelectedItem().getValue().equals("3")) {
                // Tabla <!-- pltablas2 p3 -->
                SQL_TABLA_DATA = " SELECT P3.TABLA_DESCRI CONCEPTO, P3.TABLA_DATFOR FORMULA , P3.TABLA_DESCRI CONCEPTO_PURO FROM TPLTABLAS2 P3 WHERE P3.TABLA_PER = to_char(sysdate,'yyyy') AND P3.TABLA_IDMES = '00' AND P3.TABLA_ESTADO = '1' ORDER BY 1 ";
            }//si seleccionaron CONCEPTOS
            else if (cb_conceptos.getSelectedItem().getValue().equals("4")) {
                // Tabla <!-- pltablas1 p4 -->
                SQL_TABLA_DATA = " SELECT P4.TABLA_TIPO1 || ' - ' || P4.TABLA_DESCRI CONCEPTO, P4.TABLA_DATFOR FORMULA , P4.TABLA_DESCRI CONCEPTO_PURO FROM TPLTABLAS1 P4 WHERE P4.TABLA_COD = '00001' AND P4.TABLA_ID <> '000' AND P4.TABLA_ESTADO = '1' ORDER BY 1 ";
            }//si seleccionaron UTILIDADES
            else if (cb_conceptos.getSelectedItem().getValue().equals("5")) {
                // Tabla <!-- pldatfor p5 -->
                SQL_TABLA_DATA = " SELECT P5.PLDATFOR_DVIEW CONCEPTO, P5.PLDATFOR_DFORM FORMULA , P5.PLDATFOR_DVIEW CONCEPTO_PURO FROM TPLDATFOR P5 WHERE P5.PLDATFOR_IDT = '003' AND P5.EMP_ID = " + objUsuCredential.getCodemp() + " AND P5.SUC_ID = " + objUsuCredential.getCodsuc() + "ORDER BY 1 ";
            }
            //Luego llenar listbox            
            objListModelTablas = new ListModelList<Tablas>();
            objListModelTablas = objDaoFunciones.getListBoxTablas(SQL_TABLA_DATA);
            lst_conceptos.setModel(objListModelTablas);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ControllerFunciones.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objFormulas.getForm_id());
        }
    }

    /**
     * Metodo que se ejecuta cuando se presiona el boton agregar conceptos
     */
    @Listen("onClick=#tbbtn_add_conceptos")
    public void boton_add_conceptos() {
        boolean b_error = false;
        //Si en el combo no se ha seleccionado ningun valor
        if (cb_conceptos.getSelectedIndex() < 0) {
            Messagebox.show("Debe seleccionar una tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            b_error = true;
        }
        //Si no se ha seleccionado ningun valor del listbox
        if (lst_conceptos.getSelectedIndex() == -1 && b_error == false) {
            Messagebox.show("Debe seleccionar un concepto de la lista", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            b_error = true;

        }
        //Agregar a la cuadro de texto de la formula
        // Si han seleccionado ambos campos
        if (b_error == false) {
            String s_concepto_sel_puro = objListModelTablas.get(lst_conceptos.getSelectedIndex()).getConcepto_puro();
            String s_concepto_sel = objListModelTablas.get(lst_conceptos.getSelectedIndex()).getPldatfor_dview();
            String s_formula_sel = objListModelTablas.get(lst_conceptos.getSelectedIndex()).getPldatfor_dform();
            s_concepto_total += s_concepto_sel_puro + " ";
            txt_editor_formula.setValue(s_concepto_total);
            //Agregar a la lista de objetos de la clase BeanFormula
            // para encapsular los datos adicionados.            
            objEditorFormulas = new EditorFormulas();
            objEditorFormulas.setS_cod_concepto(objListModelFormulas.get(lst_formulas.getSelectedIndex()).getForm_id());
            objEditorFormulas.setConcepto_puro(s_concepto_sel_puro);
            objEditorFormulas.setConcepto(s_concepto_sel);
            objEditorFormulas.setFormula(s_formula_sel);
            objListEditorFormulas.add(objEditorFormulas);
        }
    }

    /**
     * Metodo que se ejecuta cuando se presiona el boton agregar operadores
     */
    @Listen("onClick=#tbbtn_add_operadores")
    public void boton_add_operadores() {
        boolean b_error = false;
        //Si en el combo no se ha seleccionado ningun valor
        if (cb_operadores.getSelectedIndex() < 0) {
            Messagebox.show("Debe seleccionar un Operador", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            b_error = true;
        }
        //Agregar a la cuadro de texto de la formula
        // Si han seleccionado ambos campos
        if (b_error == false) {

            String s_operador_sel_con = objListOperadores.get(cb_operadores.getSelectedIndex()).getPldatfor_dview(); // + Suma

            //int x = s_operador_sel_con.indexOf(" ");
            String cadena = s_operador_sel_con.substring(0, s_operador_sel_con.indexOf(" "));

            String s_operador_sel_for = objListOperadores.get(cb_operadores.getSelectedIndex()).getPldatfor_dform(); // +

            //s_concepto_total += s_operador_sel_for + " ";
            s_concepto_total += cadena + " ";
            txt_editor_formula.setValue(s_concepto_total);
            //Agregar a la lista de objetos de la clase BeanFormula
            // para encapsular los datos adicionados.            
            objEditorFormulas = new EditorFormulas();
            objEditorFormulas.setS_cod_concepto(objListModelFormulas.get(lst_formulas.getSelectedIndex()).getForm_id());
            objEditorFormulas.setConcepto_puro(cadena);
            //objEditorFormulas.setConcepto(s_operador_sel_con);
            objEditorFormulas.setConcepto(cadena);
            objEditorFormulas.setFormula(s_operador_sel_for);
            objListEditorFormulas.add(objEditorFormulas);
        }
    }

    /**
     * Metodo que se ejecuta al presionar el boton agregar constantes
     */
    @Listen("onClick=#tbbtn_add_constantes")
    public void boton_add_constantes() {
        boolean b_valida = false;
        String s_operador_sel_con = null;
        String s_operador_sel_for = null;
        if (rg_constantes.getSelectedItem().getValue().equals("T")) {
            //Validar que el textbox no se encuentre vacio
            if (txt_texto_constantes.getValue().isEmpty() && txt_texto_constantes.getValue().length() == 0) {
                Messagebox.show("Debe ingresar un valor en el campo de texto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                b_valida = false;
            } else {
                s_operador_sel_con = "'" + txt_texto_constantes.getValue() + "'";
                s_operador_sel_for = "'" + txt_texto_constantes.getValue() + "'";
                b_valida = true;
            }
        } else if (rg_constantes.getSelectedItem().getValue().equals("N")) {
            //Validar que el textbox no se encuentre vacio
            if (txt_constantes.getValue().isEmpty() && txt_constantes.getValue().length() == 0) {
                Messagebox.show("Debe ingresar un valor en el campo de Numero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                b_valida = false;
            } else {
                txt_constantes.clearErrorMessage(true);
                Utilitarios.validarSoloIngresoNumerosYDecimal(txt_constantes, "Solo debe ingresar números");
                s_operador_sel_con = txt_constantes.getValue();
                s_operador_sel_for = txt_constantes.getValue();
                b_valida = true;
            }
        } else if (rg_constantes.getSelectedItem().getValue().equals("F")) {
            s_operador_sel_con = Utilitarios.parseString(db_fecha_constante.getValue());
            s_operador_sel_for = Utilitarios.parseString(db_fecha_constante.getValue());
            b_valida = true;
        }
        //Si no hubo errores
        if (b_valida) {
            s_concepto_total += s_operador_sel_for + " ";
            txt_editor_formula.setValue(s_concepto_total);
            //Agregar a la lista de objetos de la clase BeanFormula
            // para encapsular los datos adicionados.            
            objEditorFormulas = new EditorFormulas();
            objEditorFormulas.setS_cod_concepto(objListModelFormulas.get(lst_formulas.getSelectedIndex()).getForm_id());
            objEditorFormulas.setConcepto_puro(s_operador_sel_con);
            objEditorFormulas.setConcepto(s_operador_sel_con);
            objEditorFormulas.setFormula(s_operador_sel_for);
            objListEditorFormulas.add(objEditorFormulas);
        }
    }

    /**
     * Metodo que se ejecuta cuando se presiona el boton Deshacer
     */
    @Listen("onClick=#tbbtn_deshacer_crud")
    public void boton_deshacer_crud() {
        //Validar si la lista tiene elementos
        if (objListEditorFormulas.size() > 0) {
            //Si se presiona limpiar el ultimo registro
            objListEditorFormulas.remove(objListEditorFormulas.size() - 1);
            //Limpiar cadena que almacena formula
            s_concepto_total = "";
            //Barrer arreglo para imprimir en pantalla nuevamente
            for (int x = 0; x < objListEditorFormulas.size(); x++) {
                // Almacenar el objeto seteado
                s_concepto_total += objListEditorFormulas.get(x).getConcepto_puro() + " ";
            }
            //Mostrar en pantalla la nueva formula
            txt_editor_formula.setValue(s_concepto_total);
        } else {
            Messagebox.show("No hay nada para deshacer en el Editor de Formulas");
        }
    }

    /**
     * Metodo que se ejecuta al presionar el boton limpiar todo
     */
    @Listen("onClick=#tbbtn_clean_crud")
    public void boton_clean_crud() {
        //Validar que la lista tenga objetos.
        if (objListEditorFormulas.size() > 0) {
            // Limpiamos la cadena
            s_concepto_total = "";
            // Seteamos la cadena en textbox limpio
            txt_editor_formula.setValue(s_concepto_total);
            // Limpiamos la lista de objetos
            objListEditorFormulas.clear();
        } else {
            Messagebox.show("No hay nada para limpiar en el Editor de Formulas");
        }
    }

    /**
     * Metodo que se ejecuta al seleccionar las opciones del radio group
     */
    @Listen("onCheck=#rg_constantes")
    public void seleccionRadioButtonConstantes() {
        if (rg_constantes.getSelectedItem().getValue().equals("T")) {
            txt_texto_constantes.setDisabled(false);
            txt_constantes.setDisabled(true);
            db_fecha_constante.setDisabled(true);
            txt_texto_constantes.setFocus(true);
        } else if (rg_constantes.getSelectedItem().getValue().equals("N")) {
            txt_constantes.setDisabled(false);
            txt_texto_constantes.setDisabled(true);
            db_fecha_constante.setDisabled(true);
            txt_constantes.setFocus(true);
        } else if (rg_constantes.getSelectedItem().getValue().equals("F")) {
            db_fecha_constante.setDisabled(false);
            txt_constantes.setDisabled(true);
            txt_texto_constantes.setDisabled(true);
            db_fecha_constante.setFocus(true);
        }
    }

    /**
     * Metodo que se ejecuta al seleccionar un registro de la lista principal
     */
    @Listen("onSelect=#lst_formulas")
    public void seleccion() {
        objFormulas = lst_formulas.getSelectedItem().getValue();
        if (objFormulas == null) {
            objFormulas = objListModelFormulas.get(i_sel + 1);
        }
        i_sel = lst_formulas.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objFormulas.getForm_id());
        habilitaTab(false, false);
    }

    /**
     * Metodo que se ejecuta al presionar el boton editar de la barra de
     * herramienta
     */
    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_formulas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_constantes.setDisabled(true);
            db_fecha_constante.setDisabled(true);
            cb_conceptos.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    /**
     * Metodo que se ejecuta al presionar el boton deshacer de la barra de
     * herramienta
     */
    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_formulas.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            VerificarTransacciones();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    /**
     * Metodo para clonar la funcion en todas las sedes
     *
     * @throws java.sql.SQLException
     */
    @Listen("onClick=#btn_clonar")
    public void clonacion() throws SQLException {
        if (objUsuCredential.getCodemp() == 1 && objUsuCredential.getCodsuc() == 1) {
            s_mensaje = "Está seguro que desea realizar esta operación?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                objParametrosSalida = objDaoFunciones.clonar();
                                if (objParametrosSalida.getFlagIndicador() == 0) {
                                    Messagebox.show("Se clono las funciones en todas las sedes", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                } else {
                                    Messagebox.show("No se realizo ninguna operacion", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                }
                            }
                        }
                    });

        } else {
            Messagebox.show("Empresa no tiene privilegios para esta operacion", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        }

    }

    /**
     * Metodo que se ejecuta al presionar el boton guardar de la barra de
     * herramienta
     */
    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("formula")) {
                            cb_conceptos.focus();
                        }
                    }
                }
            });
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoFunciones.actualizar(objListEditorFormulas, (chk_estado.isChecked() ? 1 : 2));
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                VerificarTransacciones();
                                int est = cb_estado.getValue().toString().equals("ACTIVO") ? 1 : 2 ;
                                objListModelFormulas = objDaoFunciones.listaFormulas(est);
                                lst_formulas.setModel(objListModelFormulas);
                            }
                        }
                    });
        }
    }

    /**
     * Metodo que elimina una formula del sistema
     *
     * @throws SQLException
     */
    /*
     @Listen("onClick=#tbbtn_btn_eliminar")
     public void botonEliminar() throws SQLException {
     if (lst_formulas.getSelectedIndex() == -1) {
     Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     s_mensaje = "Está seguro que desea eliminar este banco?";
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {

     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     s_mensaje = objDaoFunciones.eliminar(objFormulas);
     valor = objDaoFunciones.i_flagErrorBD;
     if (valor == 0) {
     objListModelFormulas.clear();
     cb_estado.setSelectedIndex(0);
     cb_busqueda.setSelectedIndex(0);
     txt_busqueda.setValue("%%");
     txt_busqueda.setDisabled(true);
     objListModelFormulas = objDaoFunciones.listaFormulas(1);
     lst_formulas.setModel(objListModelFormulas);
     lst_formulas.focus();
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     //validacion de eliminacion
     tbbtn_btn_eliminar.setDisabled(false);
     VerificarTransacciones();
     limpiarCampos();
     limpiaAuditoria();
     } else {
     Messagebox.show(objDaoFunciones.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     }
     }
     }
     });
     }

     }
     */
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_formulas == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionFunciones.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    /**
     * Metodo para habilitar los tab
     *
     * @param b_valida1 parametro que habilita el tab tab_listaformulas
     * @param b_valida2 parametro que habilita el tab tab_mantenimiento
     */
    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaformulas.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    /**
     * Metodo para habilitar los botones de la barra de herramientas
     *
     * @param b_valida1 habilita los botones
     * tbbtn_btn_nuevo,tbbtn_btn_editar,tbbtn_btn_eliminar,tbbtn_btn_imprimir
     * @param b_valida2 habilita los botones
     * tbbtn_btn_deshacer,tbbtn_btn_guardar
     */
    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        //tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        //tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    /**
     * Metodo para cambiar la seleccion del tab
     *
     * @param b_valida1 parametro que selecciona el tab tab_listaformulas
     * @param b_valida2 parametro que selecciona el tab tab_mantenimiento
     */
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaformulas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    /**
     * Metodo que habilita los campos de edicion y las listas desplegables
     *
     * @param b_valida1 parametro que habilita todos los campos.
     */
    public void habilitaCampos(boolean b_valida1) {
        chk_estado.setDisabled(b_valida1);
        cb_conceptos.setDisabled(b_valida1);
        lst_conceptos.setDisabled(b_valida1);
        tbbtn_add_conceptos.setDisabled(b_valida1);
        cb_operadores.setDisabled(b_valida1);
        tbbtn_add_operadores.setDisabled(b_valida1);
        rad_btn_tex.setDisabled(b_valida1);
        rad_btn_num.setDisabled(b_valida1);
        rad_btn_fec.setDisabled(b_valida1);
        txt_texto_constantes.setDisabled(b_valida1);
        txt_constantes.setDisabled(b_valida1);
        db_fecha_constante.setDisabled(b_valida1);
        tbbtn_add_constantes.setDisabled(b_valida1);
        tbbtn_deshacer_crud.setDisabled(b_valida1);
        tbbtn_clean_crud.setDisabled(b_valida1);

    }

    /**
     * Metodo que permite verificar los campos obligatorios
     *
     * @return retorna una cadena con el nombre del campo no valido o vacio
     */
    public String verificar() {
        String men;
        if (txt_editor_formula.getValue().isEmpty()) {
            men = "Ingresa conceptos para la formula";
            campo = "formula";
        } else {
            men = "";
        }
        return men;
    }

    /**
     * Metodo que limpia los campos
     */
    public void limpiarCampos() {
        txt_editor_formula.setValue("");
    }

    /**
     * Metodo que se utiliza para llenar los campos con la data de Base de
     * Datos.
     */
    public void llenarCampos() {
        cb_conceptos.setValue("");
        objListModelTablas = new ListModelList<Tablas>();
        lst_conceptos.setModel(objListModelTablas);
        cb_operadores.setValue("");
        rg_constantes.setSelectedItem(rad_btn_tex.getRadiogroup().getItemAtIndex(0)); //.getSelectedItem().getValue().equals("T")
        //rad_btn_tex.setValue("T");
        txt_texto_constantes.setValue("");
        txt_constantes.setValue("");
        db_fecha_constante.setValue(null);
        // Almacenamos las cadenas separadas por @        
        String s_cadena_contenido = objFormulas.getForm_sep_contenido();
        String s_cadena_calculo = objFormulas.getForm_sep_calculo();
        // Separamos las cadenas y almacenamos en un arreglo.
        String[] array_conceptos = s_cadena_contenido.split("@");
        String[] array_calculo = s_cadena_calculo.split("@");
        //Limpiamos la lista de objetos
        objListEditorFormulas.clear();
        // Barremos los arreglos y los seteamos en la lista
        for (int i = 0; i < array_conceptos.length; i++) {
            objEditorFormulas = new EditorFormulas();
            objEditorFormulas.setS_cod_concepto(objFormulas.getForm_id());
            objEditorFormulas.setConcepto_puro(array_conceptos[i].trim());
            objEditorFormulas.setFormula(array_calculo[i].trim());
            objListEditorFormulas.add(objEditorFormulas);
        }
        //Seteamos el valor del contenido en el textbos del editor
        txt_editor_formula.setValue(objFormulas.getForm_contenido());
        s_concepto_total = objFormulas.getForm_contenido();
        txt_usuadd.setValue(objFormulas.getForm_usuadd());
        d_fecadd.setValue(objFormulas.getForm_fecadd());
        txt_usumod.setValue(objFormulas.getForm_usumod());
        d_fecmod.setValue(objFormulas.getForm_fecmod());

        txt_idform.setValue(objFormulas.getForm_id());
        txt_desform.setValue(objFormulas.getForm_descri());

        if (objFormulas.getForm_estado() == 1) {
            chk_estado.setChecked(true);
        } else {
            chk_estado.setChecked(false);
        }
    }

    /**
     * Metodo para limpiar los campos de auditoria.
     */
    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    /**
     * Metodo que permite buscar formulas por ID en el sistema
     *
     * @throws SQLException
     */
    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        int selec = 0;
        int est;
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }

        if (cb_busqueda.getSelectedIndex() == 0) {
            selec = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            selec = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            selec = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        }else if (cb_busqueda.getSelectedIndex() == 3) {
            selec = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        }
        
       
        ///  objlstBancos = objDaoBancos.consultar(selec, consulta, est);
        // Cargar data para la lista principal
        objListModelFormulas = new ListModelList<Formulas>();
        objListModelFormulas = objDaoFunciones.listaFormulas2(selec, consulta, est);

        if (objListModelFormulas.getSize() > 0) {
            lst_formulas.setModel(objListModelFormulas);
        } else {
            objListModelFormulas = null;
            lst_formulas.setModel(objListModelFormulas);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiaAuditoria();
        limpiarCampos();

    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

}


/*
    
 @Listen("onSelect=#lst_lista")
 public void seleccion() {
 objBancos = lst_lista.getSelectedItem().getValue();
 if (objBancos == null) {
 objBancos = objlstBancos.get(i_sel + 1);

 }
 i_sel = lst_lista.getSelectedIndex();
 llenarCampos();
 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objBancos.getId());
 habilitaTab(false, false);
 }

 @Listen("onClick=#tbbtn_btn_nuevo")
 public void botonNuevo() {
 s_estado = "N";
 limpiarCampos();
 habilitaBotones(true, false);
 seleccionaTab(false, true);
 habilitaTab(true, false);
 habilitaCampos(false);
 chk_banest.setDisabled(true);
 chk_banest.setChecked(true);
 txt_descripcion.focus();
 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
 }

 @Listen("onClick=#tbbtn_btn_editar")
 public void botonEditar() {
 if (lst_lista.getSelectedIndex() == -1) {
 Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 } else if (chk_banest.isChecked() && lst_lista.getSelectedIndex() >= 0) {
 s_estado = "E";
 habilitaBotones(true, false);
 habilitaTab(true, false);
 seleccionaTab(false, true);
 habilitaCampos(false);
 txt_descripcion.focus();

 } else {
 s_estado = "E";
 habilitaBotones(true, false);
 habilitaTab(true, false);
 seleccionaTab(false, true);
 habilitaCampos(false);
 txt_descripcion.focus();

 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
 }
 }

 @Listen("onClick=#tbbtn_btn_deshacer")
 public void botonDeshacer() {
 s_mensaje = "Está seguro que desea deshacer los cambios?";
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
 // VerificarTransacciones();
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

 @Listen("onClick=#tbbtn_btn_guardar")
 public void botonGuardar() {
 String s_valida = verificar();
 if (!s_valida.equals("")) {
 Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
 @Override
 public void onEvent(Event event) throws Exception {
 if (((Integer) event.getData()).intValue() == Messagebox.OK) {
 if (campo.equals("descripcion")) {
 txt_descripcion.focus();
 } else if (campo.equals("siglas")) {
 txt_siglas.focus();
 } else if (campo.equals("numeracion")) {
 txt_numeracion.focus();
 }
 }
 }
 });
 } else {
 s_mensaje = "Está seguro que desea guardar los cambios?";
 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
 Messagebox.QUESTION, new EventListener() {
 @Override
 public void onEvent(Event event) throws Exception {
 if (((Integer) event.getData()).intValue() == Messagebox.OK) {
 //ParametrosSalida objParamSalida;
 objlstBancos = new ListModelList<Bancos>();
 objBancos = (Bancos) generaRegistro();
 if (s_estado.equals("N")) {
 s_mensaje = objDaoBancos.insertar(objBancos);
 } else {
 s_mensaje = objDaoBancos.actualizar(objBancos);
 }
 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 habilitaTab(false, false);
 seleccionaTab(true, false);
 habilitaCampos(true);
 habilitaBotones(false, true);
 limpiarCampos();
 limpiaAuditoria();
 objlstBancos = objDaoBancos.lstBancos(1);
 lst_lista.setModel(objlstBancos);
 }
 }
 });
 }
 }

 @Listen("onSelect=#cb_busqueda")
 public void seleccionabusqueda() throws SQLException {
 if (cb_busqueda.getSelectedIndex() > 0) {
 txt_busqueda.setDisabled(false);
 txt_busqueda.setValue("%%");
 txt_busqueda.focus();

 } else {
 txt_busqueda.setDisabled(true);
 txt_busqueda.setValue("%%");
 }
 }

 @Listen("onClick=#btn_buscar")
 public void buscar() throws SQLException {
 String consulta = txt_busqueda.getValue().toUpperCase();
 int selec = 0;
 int est;
 if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
 est = 3;
 } else if (cb_estado.getSelectedIndex() == 0) {
 est = 1;
 } else {
 est = 2;
 }

 if (cb_busqueda.getSelectedIndex() == 0) {
 selec = 0;
 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
 } else if (cb_busqueda.getSelectedIndex() == 1) {
 selec = 1;
 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + consulta + " para su busqueda");
 } else if (cb_busqueda.getSelectedIndex() == 2) {
 selec = 2;
 LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
 }
 objlstBancos = objDaoBancos.consultar(selec, consulta, est);
 if (objlstBancos.getSize() > 0) {
 lst_lista.setModel(objlstBancos);
 } else {
 objlstBancos = null;
 lst_lista.setModel(objlstBancos);
 Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 }
 limpiaAuditoria();
 limpiarCampos();

 }

 @Listen("onClick=#tbbtn_btn_eliminar")
 public void botonEliminar() throws SQLException {
 if (lst_lista.getSelectedIndex() == -1) {
 Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 } else {
 s_mensaje = "Está seguro que desea eliminar este banco?";
 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
 Messagebox.QUESTION, new EventListener() {

 @Override
 public void onEvent(Event event) throws Exception {
 if (((Integer) event.getData()).intValue() == Messagebox.OK) {
 s_mensaje = objDaoBancos.eliminar(objBancos);
 valor = objDaoBancos.i_flagErrorBD;
 if (valor == 0) {
 objlstBancos.clear();
 cb_estado.setSelectedIndex(0);
 cb_busqueda.setSelectedIndex(0);
 txt_busqueda.setValue("%%");
 txt_busqueda.setDisabled(true);
 objlstBancos = objDaoBancos.lstBancos(1);
 lst_lista.setModel(objlstBancos);
 lst_lista.focus();
 Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 //validacion de eliminacion
 tbbtn_btn_eliminar.setDisabled(false);
 //VerificarTransacciones();
 limpiarCampos();
 limpiaAuditoria();
 } else {
 Messagebox.show(objDaoBancos.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
 }
 }
 }
 });
 }

 }

 //Eventos Otros 
 @Listen("onCtrlKey=#w_bancos")
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
            
 }
 }

 @Listen("onOK=#txt_descripcion")
 public void next() {
 txt_siglas.focus();
 }

 @Listen("onOK=#txt_siglas")
 public void next1() {
 txt_numeracion.focus();
 }

 public Object generaRegistro() {

 String bancokey = objBancos.getId();
 String descripcion = txt_descripcion.getValue().toUpperCase().trim();
 int i_ubicest;
 if (chk_banest.isChecked()) {
 i_ubicest = 1;
 } else {
 i_ubicest = 2;
 }
 String siglas = txt_siglas.getValue().toUpperCase();
 int numeracion = txt_numeracion.getValue();
 return new Bancos(bancokey, descripcion, i_ubicest, siglas, numeracion);

 }

 public String verificar() {
 String valor;
 if (txt_descripcion.getValue().isEmpty()) {
 valor = "Ingresa descripción";
 campo = "descripcion";
 } else if (txt_siglas.getValue().isEmpty()) {
 valor = "Ingresa siglas";
 campo = "siglas";
 } else if (txt_numeracion.getValue() == null) {
 valor = "Ingrese número de dígitos de la cuenta";
 campo = "numeracion";
 } else {
 valor = "";
 }
 return valor;
 }

 //metodos sin utilizar
 public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
 tab_lista.setSelected(b_valida1);
 tab_mantenimiento.setSelected(b_valida2);
 }

 public void habilitaTab(boolean b_valida1, boolean b_valida2) {
 tab_lista.setDisabled(b_valida1);
 tab_mantenimiento.setDisabled(b_valida2);
 }

 public void habilitaCampos(boolean b_valida1) {
 txt_descripcion.setDisabled(b_valida1);
 txt_siglas.setDisabled(b_valida1);
 txt_numeracion.setDisabled(b_valida1);
 chk_banest.setDisabled(b_valida1);
 }

 public void limpiarCampos() {
 chk_banest.setChecked(false);
 txt_descripcion.setValue("");
 txt_siglas.setValue("");
 txt_id.setValue("");
 txt_numeracion.setValue(null);
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
 txt_descripcion.setValue(objBancos.getDescripcion());
 txt_id.setValue(objBancos.getId());
 if (objBancos.getEstado() == 1) {
 chk_banest.setChecked(true);
 } else {
 chk_banest.setChecked(false);
 }
 txt_siglas.setValue(objBancos.getSiglas());
 txt_numeracion.setValue(objBancos.getNumeracion());
 txt_usuadd.setValue(objBancos.getUsu_add());
 txt_usumod.setValue(objBancos.getUsu_mod());
 d_fecadd.setValue(objBancos.getDia_add());
 d_fecmod.setValue(objBancos.getDia_mod());
 }

 public void limpiaAuditoria() {
 txt_usuadd.setValue("");
 txt_usumod.setValue("");
 d_fecadd.setValue(null);
 d_fecmod.setValue(null);
 }
    
    
 */
