/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManCCostos;
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
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerManAreas extends SelectorComposer<Component> {

    @Wire
    Tab tab_listaManAreas, tab_mantenimiento;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_excel;

    @Wire
    Textbox txt_areaid, txt_areades, txt_cod_costo, txt_des_costo, txt_busqueda_area, txt_usuadd, txt_usumod/*, txt_busqueda*/;

    @Wire
    Spinner sp_cargoord;

    @Wire
    Checkbox chk_areaest;

    @Wire
    Listbox lst_manareas;

    @Wire
    Datebox d_fecadd, d_fecmod;

    @Wire
    Combobox cb_busqueda, cb_estado;

    String usuario;
    Map parametros;

    //INSTANCIAS DE OBJETOS
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ManAreas objAreas = new ManAreas();
    DaoManAreas objDaoAreas = new DaoManAreas();
    // LOV CENTRO COSTOS
    ListModelList<ManCCostos> objlstCCostos;
    ManCCostos objCCostos;

    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios objUtilitarios = new Utilitarios();

    //Variables publicas
    String s_estado = "Q";
    String s_mensaje;
    String campo = "";
    int i_sel;
    int valor;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManAreas.class);

    @Listen("onCreate=#tabbox_manareas")
    public void llenaLista() throws SQLException {
        cb_estado.setSelectedIndex(2);
        objlstAreas = objDaoAreas.listaAreas(1);
        lst_manareas.setModel(objlstAreas);

    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Area con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Area con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Area");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Area");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Area");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Area");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Area");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Area");
        }
    }

    @Listen("onSelect=#lst_manareas")
    public void seleccionaRegistro() throws SQLException {
        objAreas = (ManAreas) lst_manareas.getSelectedItem().getValue();
        if (objAreas == null) {
            objAreas = objlstAreas.get(i_sel + 1);
        }
        i_sel = lst_manareas.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objAreas.getArea_id());
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda_area.setDisabled(false);
        } else {
            txt_busqueda_area.setDisabled(true);
            txt_busqueda_area.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda_area.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstAreas = new ListModelList<ManAreas>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }

        objlstAreas = objDaoAreas.busquedaAreas(i_seleccion, s_consulta, i_estado);

        if (objlstAreas.getSize() > 0) {
            lst_manareas.setModel(objlstAreas);
        } else {
            objlstAreas = null;
            lst_manareas.setModel(objlstAreas);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onClick=#tbbtn_excel")
    public void exportarExcel() throws SQLException, JRException, FileNotFoundException, IOException {
        if (objlstAreas == null || objlstAreas.isEmpty()) {
            Messagebox.show("No hay registros", "ERP_JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            JasperPrint jasperPrint;
            
            try {
                Map objParam = new HashMap();
                objParam.put("usuario", objUsuCredential.getCuenta());
                objParam.put("P_WHERE", DaoManAreas.P_WHERE);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));

                objParam.put(JRParameter.IS_IGNORE_PAGINATION, true);
                URL fileUrl = getClass().getClassLoader().getResource("../reportes/planillas/mantenimiento/ManAreaPersonal.jasper");
                JasperReport reporte = (JasperReport) JRLoader.loadObject(fileUrl);

                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "AREAS" + aleatorio;

                JExcelApiExporter exporterXLS = new JExcelApiExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "D://" + nom_reporte + ".xls");
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporterXLS.exportReport();

                File file = new File("D:\\" + nom_reporte + ".xls");
                Filedownload.save(file, "application/xlsx");

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

    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objAreas = new ManAreas();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_areades.focus();
        chk_areaest.setDisabled(true);
        chk_areaest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_manareas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_manareas.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_manareas.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_manareas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea Eliminar esta Área?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoAreas.eliminar(objAreas);
                                valor = objDaoAreas.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstAreas.clear();
                                    //cb_estado.setSelectedIndex(0);
                                    //cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda_area.setValue("%%");
                                    txt_busqueda_area.setDisabled(true);
                                    objlstAreas = objDaoAreas.listaAreas(1);
                                    lst_manareas.setModel(objlstAreas);
                                    lst_manareas.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoAreas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                        if (campo.equals("descri")) {
                            txt_areades.focus();
                        } else if (campo.equals("costo")) {
                            txt_cod_costo.focus();
                        }

                    }

                }
            });
        } else {
//            if (txt_areades.getText().matches("^\\s") || txt_areades.getText().matches("^\\s+")) {
//                Messagebox.show("El campo Descripción no debe tener espacios en blanco");
//
//            } else {

            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParametrosSalida;
                                objlstAreas = new ListModelList<ManAreas>();
                                objAreas = generaRegistro();//falta
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoAreas.insertar(objAreas);
                                } else {
                                    s_mensaje = objDaoAreas.modificar(objAreas);
                                }
                                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                habilitaBotones(false, true);
                                limpiarCampos();
                                limpiaAuditoria();
                                cb_estado.setSelectedIndex(0);
                                cb_busqueda.setSelectedIndex(0);
                                txt_busqueda_area.setValue("%%");
                                txt_busqueda_area.setDisabled(true);
                                objlstAreas = objDaoAreas.listaAreas(1);
                                lst_manareas.setModel(objlstAreas);
                                objAreas = new ManAreas();
                                lst_manareas.focus();

                            }
                        }
                    });

            // }
        }

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_manareas == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionAreas.zul", null, objMapObjetos);
            window.doModal();
        }
    }


    //METODOS 
    public void llenarCampos() {
        txt_areaid.setValue(String.valueOf(objAreas.getArea_id()));
        txt_areades.setValue(objAreas.getArea_des());
        if (objAreas.getArea_est() == 1) {
            chk_areaest.setChecked(true);
        } else {
            chk_areaest.setChecked(false);
        }
        txt_cod_costo.setValue(objAreas.getArea_cod_costo());
        txt_des_costo.setValue(objAreas.getArea_des_costo());
        txt_usuadd.setValue(objAreas.getArea_usuadd());
        d_fecadd.setValue(objAreas.getArea_fecadd());
        txt_usumod.setValue(objAreas.getArea_usumod());
        d_fecmod.setValue(objAreas.getArea_fecmod());

    }

    public void habilitaCampos(boolean b_valida) {
        txt_areades.setDisabled(b_valida);
        chk_areaest.setDisabled(b_valida);
        txt_cod_costo.setDisabled(b_valida);

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
        tab_listaManAreas.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaManAreas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiarCampos() {
        txt_areaid.setValue("");
        txt_areades.setValue("");
        txt_cod_costo.setValue("");
        txt_des_costo.setValue("");
        chk_areaest.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstAreas = null;
        objlstAreas = new ListModelList<ManAreas>();
        lst_manareas.setModel(objlstAreas);
    }

    //REGISTRO
    public ManAreas generaRegistro() {
        String s_area_id = objAreas.getArea_id();
        String s_area_des = txt_areades.getValue().toUpperCase();
        int s_area_est;
        if (chk_areaest.isChecked()) {
            s_area_est = 1;
        } else {
            s_area_est = 2;
        }
        String s_area_1 = txt_cod_costo.getValue();
        String s_area_usuadd = objUsuCredential.getCuenta();
        String s_area_usumod = objUsuCredential.getCuenta();

        return new ManAreas(s_area_id, s_area_des, s_area_est, s_area_1, s_area_usuadd, s_area_usumod);

    }

    public String verificar() {
        String s_valor;
        if (txt_areades.getValue().isEmpty()) {
            s_valor = "El campo DESCRIPCIÓN es obligatorio";
            campo = "descri";
        } else if (!txt_areades.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "El Campo DESCRIPCIÓN ingresar solo letras";
            campo = "descri";
            txt_areades.setValue("");
        } else if (txt_cod_costo.getValue().isEmpty()) {
            s_valor = "El campo CENTRO COSTOS es Obligatorio";
            campo = "costo";
            txt_cod_costo.setValue("");
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    @Listen("onOK=#txt_areades")
    public void Netx_1() {
        txt_cod_costo.focus();

    }

    @Listen("onOK=#txt_cod_costo")
    public void Netx_2() {
        txt_areades.focus();

    }

    @Listen("onCtrlKey=#w_manareas")
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
    //LOV COSTOS

    @Listen("onOK=#txt_cod_costo")
    public void busquedaCCostos() {
        if (bandera == false) {
            bandera = true;
            if (txt_cod_costo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id", txt_cod_costo);
                objMapObjetos.put("cc_descri", txt_des_costo);
                objMapObjetos.put("controlador", "ControllerManAreas");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovCCostos.zul", null, objMapObjetos);
                window.doModal();
            }

        }

    }

    @Listen("onBlur=#txt_cod_costo")
    public void valida_CCostos() throws SQLException {
        if (!txt_cod_costo.getValue().isEmpty()) {
            String costo = txt_cod_costo.getValue();
            objCCostos = objDaoAreas.getLovCCostos(costo);
            if (objCCostos == null) {
                Messagebox.show("El codigo debe ser del Centro Costos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_cod_costo.setValue(null);
                        txt_cod_costo.setValue(null);
                        txt_cod_costo.focus();
                        txt_des_costo.setValue("");
                    }
                });

            } else {
                txt_cod_costo.setValue(objCCostos.getCosto_cod());
                txt_des_costo.setValue(objCCostos.getCosto_des());

            }

        } else {
            txt_des_costo.setValue("");

        }
        bandera = false;
    }

}
