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
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
//import org.me.gj.controller.logistica.mantenimiento.ControllerUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
import org.me.gj.model.planillas.mantenimiento.ConfAfp2;
import org.me.gj.model.planillas.mantenimiento.ManConfAfps;
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
import org.zkoss.zul.Doublebox;
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
public class ControllerConfAfps extends SelectorComposer<Component> {

    @Wire
    Tab tab_listaConfAfps, tab_mantenimiento;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,tbbtn_excel;

    @Wire
    Textbox txt_afpid, txt_afpdes, txt_apori, txt_origen, txt_apodes, txt_destino, txt_comori, txt_com_origen, txt_comdes, txt_com_destino, txt_priori, txt_pri_origen, txt_prides, txt_pri_destino, txt_mixori, txt_mix_origen, txt_mixdes, txt_mix_destino, txt_idcta, txt_descta, txt_usuadd, txt_usumod, txt_busqueda, txt_busqueda_1;

    @Wire
    Doublebox txt_apobl, txt_comfig, txt_priseg, txt_commix, txt_topseg;

    @Wire
    Spinner sp_cargoord;

    @Wire
    Checkbox chk_confest;

    @Wire
    Listbox lst_confafp;

    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Combobox cb_periodo, cb_busqueda, cb_estado;

//Instancias de objetos
    ListModelList<ManConfAfps> objlstConf = new ListModelList<ManConfAfps>();
    ManConfAfps objConf = new ManConfAfps();
    DaoConfAfps objDaoConf = new DaoConfAfps();
    //LOV 1
    ListModelList<ConfAfp1> objlstConfAfp1;
    ConfAfp1 objConfafp1;

    //LOV 2
    ListModelList<ConfAfp2> objlstConfAfp2;
    ConfAfp2 objConfafp2;

    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios objUtilitarios = new Utilitarios();

    ListModelList<ManPer> lst_ManPeriodos;
    ListModelList<ManPer> objLisManPer;
    ManPer objManPer;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");

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
    private static final Logger LOGGER = Logger.getLogger(ControllerConfAfps.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        lst_ManPeriodos = new ListModelList<ManPer>();
        lst_ManPeriodos = (new DaoManPer()).listaPeriodos(0);
        lst_ManPeriodos = (new DaoManPer()).listaPeriodosActual(1, 5);
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(lst_ManPeriodos);
        cb_periodo.setValue(periodo);

    }

    @Listen("onCreate=#tabbox_confafp")
    public void llenaLista() throws SQLException {
        cb_estado.setSelectedIndex(2);
        objlstConf = objDaoConf.listaConf(1);
        lst_confafp.setModel(objlstConf);
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101110, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Configuracion Afp con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Configuracion Afp con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Configuracion Afp");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Configuracion Afp");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Configuracion Afp");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Configuracion Afp");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Configuracion Afp");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Configuracion Afp");
        }
    }
	
    @Listen("onSelect=#lst_confafp")
    public void seleccionaRegistro() throws SQLException {
        objConf = (ManConfAfps) lst_confafp.getSelectedItem().getValue();
        if (objConf == null) {
            objConf = objlstConf.get(i_sel + 1);
        }
        i_sel = lst_confafp.getSelectedIndex();
        cb_periodo.setDisabled(true);
        txt_afpid.setDisabled(true);
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objConf.getAfpid());
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstConf = new ListModelList<ManConfAfps>();
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
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }

        objlstConf = objDaoConf.busquedaConf(i_seleccion, s_consulta, i_estado);

        if (objlstConf.getSize() > 0) {
            lst_confafp.setModel(objlstConf);
        } else {
            objlstConf = null;
            lst_confafp.setModel(objlstConf);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        
      
        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objConf = new ManConfAfps();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        txt_afpid.setDisabled(false);
        cb_periodo.setDisabled(false);
        habilitaCampos(false);
        txt_afpid.focus();
        chk_confest.setDisabled(true);
        chk_confest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_confafp.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            cb_periodo.setDisabled(true);
            habilitaCampos(false);
            txt_afpid.setDisabled(true);
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
                            limpiaAuditoria();
                            lst_confafp.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            lst_confafp.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_confafp.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar esta configuración?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoConf.eliminar(objConf);
                                valor = objDaoConf.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstConf.clear();
                                    //cb_estado.setSelectedIndex(0);
                                    //cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    objlstConf = objDaoConf.listaConf(1);
                                    lst_confafp.setModel(objlstConf);
                                    lst_confafp.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoConf.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                        if (campo.equals("periodo")) {
                            cb_periodo.focus();
                        } else if (campo.equals("afpid")) {
                            txt_afpid.focus();
                        } else if (campo.equals("apobl")) {
                            txt_apobl.focus();
                        } else if (campo.equals("comfig")) {
                            txt_comfig.focus();
                        } else if (campo.equals("priseg")) {
                            txt_priseg.focus();
                        } else if (campo.equals("commix")) {
                            txt_commix.focus();
                        } else if (campo.equals("topseg")) {
                            txt_topseg.focus();
                        } else if (campo.equals("idcta")) {
                            txt_idcta.focus();
                        } else if (campo.equals("apori")) {
                            txt_apori.focus();
                        } else if (campo.equals("comori")) {
                            txt_comori.focus();
                        } else if (campo.equals("priori")) {
                            txt_priori.focus();
                        } else if (campo.equals("mixori")) {
                            txt_mixori.focus();
                        } else if (campo.equals("apodes")) {
                            txt_apodes.focus();
                        } else if (campo.equals("comdes")) {
                            txt_comdes.focus();
                        } else if (campo.equals("prides")) {
                            txt_prides.focus();
                        } else if (campo.equals("mixdes")) {
                            txt_mixdes.focus();
                        }

                    }

                }
            });
        } else {
//            if (!txt_afpdes.getText().matches("^\\s") || !txt_afpdes.getText().matches("^\\s+")) {
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
                                objlstConf = new ListModelList<ManConfAfps>();
                                objConf = generaRegistro();//falta
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoConf.insertar(objConf);
                                } else {
                                    s_mensaje = objDaoConf.modificar(objConf);
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
                                cb_estado.setSelectedIndex(-1);
                                cb_busqueda.setSelectedIndex(0);
                                txt_busqueda.setValue("%%");
                                txt_busqueda.setDisabled(true);
                                objlstConf = objDaoConf.listaConf(1);
                                lst_confafp.setModel(objlstConf);
                                objConf = new ManConfAfps();
                                lst_confafp.focus();

                            }
                        }
                    });

//            }
        }

    }
    
     @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_confafp == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionConfAfp.zul", null, objMapObjetos);
            window.doModal();
        }
    }
    
     @Listen("onClick=#tbbtn_excel")
    public void exportarExcel() throws SQLException, JRException, FileNotFoundException, IOException {
        if (objlstConf == null || objlstConf.isEmpty()) {
            Messagebox.show("No hay registros", "ERP_JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            JasperPrint jasperPrint;
            
            try {
                Map objParam = new HashMap();
                objParam.put("usuario", objUsuCredential.getCuenta());
                objParam.put("P_WHERE", DaoConfAfps.P_WHERE);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));

                objParam.put(JRParameter.IS_IGNORE_PAGINATION, true);
                URL fileUrl = getClass().getClassLoader().getResource("../reportes/planillas/mantenimiento/ManConfAfp.jasper");
                JasperReport reporte = (JasperReport) JRLoader.loadObject(fileUrl);

                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "CONF" + aleatorio;

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


    public ManConfAfps generaRegistro() {

        int emp_id = objUsuCredential.getCodemp();
        String n_plppag_id = cb_periodo.getValue();
        String n_plafp_id = txt_afpid.getValue();
        double n_plafp_apobl = txt_apobl.doubleValue();
        String n_plafp_apori = txt_apori.getValue();
        String n_plafp_apodes = txt_apodes.getValue();
        double n_plafp_comfij = txt_comfig.doubleValue();
        String n_plafp_comori = txt_comori.getValue();
        String n_plafp_comdes = txt_comdes.getValue();
        double n_plafp_priseg = txt_priseg.doubleValue();
        String n_plafp_priori = txt_priori.getValue();
        String n_plafp_prides = txt_prides.getValue();
        double n_plafp_topseg = txt_topseg.doubleValue();
        String n_plafp_idcta = txt_idcta.getValue();
        int s_estado_afp;
        if (chk_confest.isChecked()) {
            s_estado_afp = 1;
        } else {
            s_estado_afp = 2;
        }
        //int n_plafp_estado = objConf.getEstado();
        double n_plafp_commix = txt_commix.doubleValue();
        String n_plafp_mixori = txt_mixori.getValue();
        String n_plafp_mixdes = txt_mixdes.getValue();
        String n_plafp_usuadd = txt_usuadd.getValue();
        String n_plafp_usumod = txt_usumod.getValue();

        return new ManConfAfps(emp_id, n_plppag_id, n_plafp_id, n_plafp_apobl, n_plafp_apori, n_plafp_apodes, n_plafp_comfij, n_plafp_comori, n_plafp_comdes, n_plafp_priseg, n_plafp_priori, n_plafp_prides, n_plafp_topseg, n_plafp_idcta, s_estado_afp, n_plafp_commix, n_plafp_mixori, n_plafp_mixdes, n_plafp_usuadd, n_plafp_usumod);

    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        //txt_afpdes.setDisabled(b_valida);
        chk_confest.setDisabled(b_valida);
        //txt_afpid.setDisabled(b_valida);
        //cb_periodo.setDisabled(b_valida);
        txt_apori.setDisabled(b_valida);
        txt_apodes.setDisabled(b_valida);
        txt_comori.setDisabled(b_valida);
        txt_comdes.setDisabled(b_valida);
        txt_priori.setDisabled(b_valida);
        txt_prides.setDisabled(b_valida);
        txt_mixori.setDisabled(b_valida);
        txt_mixdes.setDisabled(b_valida);
        //cb_periodo.setDisabled(b_valida);
        txt_comfig.setDisabled(b_valida);
        txt_apobl.setDisabled(b_valida);
        txt_priseg.setDisabled(b_valida);
        txt_commix.setDisabled(b_valida);
        txt_topseg.setDisabled(b_valida);
        txt_idcta.setDisabled(b_valida);
        
        

    }

    public void limpiarCampos() {
        txt_afpid.setValue("");
        txt_afpdes.setValue("");
        cb_periodo.setValue("");
        chk_confest.setValue("");
        cb_periodo.setValue("");
        txt_apori.setValue("");
        txt_apodes.setValue("");
        txt_comori.setValue("");
        txt_comdes.setValue("");
        txt_priori.setValue("");
        txt_prides.setValue("");
        txt_mixori.setValue("");
        txt_mixdes.setValue("");
        txt_comfig.setValue(null);
        txt_apobl.setValue(null);
        txt_priseg.setValue(null);
        txt_commix.setValue(null);
        txt_topseg.setValue(null);
        txt_idcta.setValue("");
        txt_descta.setValue("");
        txt_origen.setValue("");
        txt_destino.setValue("");
        txt_com_origen.setValue("");
        txt_com_destino.setValue("");
        txt_mix_origen.setValue("");
        txt_mix_destino.setValue("");
        txt_pri_origen.setValue("");
        txt_pri_destino.setValue("");
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstConf = null;
        objlstConf = new ListModelList<ManConfAfps>();
        lst_confafp.setModel(objlstConf);
    }

    public void llenarCampos() {
        cb_periodo.setValue(objConf.getPeriodo());
        txt_afpid.setValue(String.valueOf(objConf.getAfpid()));
        txt_afpdes.setValue(objConf.getAfp_des());
        if (objConf.getEstado() == 1) {
            chk_confest.setChecked(true);
        } else {
            chk_confest.setChecked(false);
        }
        txt_apobl.setValue(objConf.getApobl());
        txt_comfig.setValue(objConf.getComfij());
        txt_priseg.setValue(objConf.getPriseg());
        txt_topseg.setValue(objConf.getTopseg());
        txt_commix.setValue(objConf.getCommix());

        txt_apori.setValue(objConf.getApori());
        txt_apodes.setValue(objConf.getApodes());

        txt_comori.setValue(objConf.getComori());
        txt_comdes.setValue(objConf.getComdes());

        txt_priori.setValue(objConf.getPriori());
        txt_prides.setValue(objConf.getPrides());

        txt_mixori.setValue(objConf.getMixori());
        txt_mixdes.setValue(objConf.getMixdes());

        txt_origen.setValue(objConf.getOrigen());
        txt_destino.setValue(objConf.getDestino());
        txt_com_origen.setValue(objConf.getCom_origen());
        txt_com_destino.setValue(objConf.getCom_destino());
        txt_pri_origen.setValue(objConf.getPri_origen());
        txt_pri_destino.setValue(objConf.getPri_destino());
        txt_mix_origen.setValue(objConf.getMix_origen());
        txt_mix_destino.setValue(objConf.getMix_destino());
        txt_idcta.setValue(objConf.getIdcta());

        txt_usuadd.setValue(objConf.getUsuadd());
        d_fecadd.setValue(objConf.getFecadd());
        txt_usumod.setValue(objConf.getUsumod());
        d_fecmod.setValue(objConf.getFecmod());

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
        tab_listaConfAfps.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaConfAfps.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public String verificar() {
        String s_valor;
        if (cb_periodo.getValue().isEmpty()) {
            s_valor = "El campo PERIODO es obligatorio";
            campo = "periodo";
        } else if (txt_afpid.getValue().isEmpty()) {
            s_valor = "El campo ID es obligatorio";
            campo = "afpid";
            txt_afpid.setValue("");

        } else if (txt_apobl.getValue() == null) {
            s_valor = "El campo APO. OBLIG % es obligatorio";
            campo = "apobl";
            txt_apobl.setValue(null);

        } else if (txt_comfig.getValue() == null) {
            s_valor = "El campo COMI. FIJA % es obligatorio";
            campo = "comfig";
            txt_comfig.setValue(null);
        } else if (txt_priseg.getValue() == null) {
            s_valor = "El campo PRIMA SEGU. %  es obligatorio";
            campo = "priseg";
            txt_priseg.setValue(null);
        } else if (txt_commix.getValue() == null) {
            s_valor = "El campo COMI. FIJA %  es obligatorio";
            campo = "commix";
            txt_commix.setValue(null);
        } else if (txt_topseg.getValue() == null) {
            s_valor = "El campo TOPE SEGURO  es obligatorio";
            campo = "topseg";
            txt_topseg.setValue(null);
        } else if (txt_idcta.getValue().isEmpty()) {
            s_valor = "El campo CUENTA CONTABLE  es obligatorio";
            campo = "idcta";
            txt_idcta.setValue("");
        } else if (txt_idcta.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "El campo CUENTA CONTABLE  ingresar solo números";
            campo = "idcta";
            txt_idcta.setValue("");
        } else if (txt_apori.getValue().isEmpty()) {
            s_valor = "El campo ORIGEN es obligatorio";
            campo = "apori";
            txt_apori.setValue("");
        } else if (txt_comori.getValue().isEmpty()) {
            s_valor = "El campo ORIGEN es obligatorio";
            campo = "comori";
            txt_comori.setValue("");
        } else if (txt_priori.getValue().isEmpty()) {
            s_valor = "El campo ORIGEN es obligatorio";
            campo = "priori";
            txt_priori.setValue("");
        } else if (txt_mixori.getValue().isEmpty()) {
            s_valor = "El campo ORIGEN es obligatorio";
            campo = "mixori";
            txt_mixori.setValue("");
        } else if (txt_apodes.getValue().isEmpty()) {
            s_valor = "El campo DESTINO es obligatorio";
            campo = "apodes";
            txt_apodes.setValue("");
        } else if (txt_comdes.getValue().isEmpty()) {
            s_valor = "El campo DESTINO es obligatorio";
            campo = "comdes";
            txt_comdes.setValue("");
        } else if (txt_prides.getValue().isEmpty()) {
            s_valor = "El campo DESTINO es obligatorio";
            campo = "prides";
            txt_prides.setValue("");
        } else if (txt_mixdes.getValue().isEmpty()) {
            s_valor = "El campo DESTINO es obligatorio";
            campo = "mixdes";
            txt_mixdes.setValue("");
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    //LOV 1
    @Listen("onOK=#txt_afpid")
    public void busquedaRegimen() {
        if (bandera == false) {
            bandera = true;
            if (txt_afpid.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_afpid);
                objMapObjetos.put("TABLA_DESCRI", txt_afpdes);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp1.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_afpid")
    public void valida_Regimen() throws SQLException {
        if (!txt_afpid.getValue().equals("")) {
            String afp = txt_afpid.getValue();
            objConfafp1 = objDaoConf.getLovConf1(afp);
            if (objConfafp1 == null) {
                Messagebox.show("El código debe ser de la AFP", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_afpid.setValue(null);
                        txt_afpid.setValue(null);
                        txt_afpid.focus();
                        txt_afpdes.setValue("");
                    }
                });

            } else {
                txt_afpid.setValue(objConfafp1.getAfp_id());
                txt_afpdes.setValue(objConfafp1.getAfp_des());

            }

        } else {
            txt_afpdes.setValue("");

        }
        bandera = false;
    }

    //LOV 2
    @Listen("onOK=#txt_apori")
    public void busquedaRegimen_1() {
        if (bandera == false) {
            bandera = true;
            if (txt_apori.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_apori);
                objMapObjetos.put("TABLA_DESCRI", txt_origen);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_apori")
    public void valida_Regimen_1() throws SQLException {
        if (!txt_apori.getValue().isEmpty()) {
            String afp = txt_apori.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_apori.setValue(null);
                        txt_apori.setValue(null);
                        txt_apori.focus();
                        txt_origen.setValue("");
                    }
                });

            } else {
                txt_apori.setValue(objConfafp2.getConcep_id());
                txt_origen.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_origen.setValue("");

        }
        bandera = false;
    }

    //txt_apodes
    @Listen("onOK=#txt_apodes")
    public void busquedaRegimen_2() {
        if (bandera == false) {
            bandera = true;
            if (txt_apodes.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_apodes);
                objMapObjetos.put("TABLA_DESCRI", txt_destino);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_apodes")
    public void valida_Regimen_2() throws SQLException {
        if (!txt_apodes.getValue().isEmpty()) {
            String afp = txt_apodes.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_apodes.setValue(null);
                        txt_apodes.setValue(null);
                        txt_apodes.focus();
                        txt_destino.setValue("");
                    }
                });

            } else {
                txt_apodes.setValue(objConfafp2.getConcep_id());
                txt_destino.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_destino.setValue("");

        }
        bandera = false;
    }

    //LOV txt_comori
    @Listen("onOK=#txt_comori")
    public void busquedaRegimen_3() {
        if (bandera == false) {
            bandera = true;
            if (txt_comori.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_comori);
                objMapObjetos.put("TABLA_DESCRI", txt_com_origen);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_comori")
    public void valida_Regimen_3() throws SQLException {
        if (!txt_comori.getValue().isEmpty()) {
            String afp = txt_comori.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_comori.setValue(null);
                        txt_comori.setValue(null);
                        txt_comori.focus();
                        txt_com_origen.setValue("");
                    }
                });

            } else {
                txt_comori.setValue(objConfafp2.getConcep_id());
                txt_com_origen.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_com_origen.setValue("");

        }
        bandera = false;
    }

    //LOV txt_comdes
    @Listen("onOK=#txt_comdes")
    public void busquedaRegimen_4() {
        if (bandera == false) {
            bandera = true;
            if (txt_comdes.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_comdes);
                objMapObjetos.put("TABLA_DESCRI", txt_com_destino);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_comdes")
    public void valida_Regimen_4() throws SQLException {
        if (!txt_comdes.getValue().isEmpty()) {
            String afp = txt_comdes.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_comdes.setValue(null);
                        txt_comdes.setValue(null);
                        txt_comdes.focus();
                        txt_com_destino.setValue("");
                    }
                });

            } else {
                txt_comdes.setValue(objConfafp2.getConcep_id());
                txt_com_destino.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_com_destino.setValue("");

        }
        bandera = false;
    }

    //LOV txt_priori
    @Listen("onOK=#txt_priori")
    public void busquedaRegimen_5() {
        if (bandera == false) {
            bandera = true;
            if (txt_priori.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_priori);
                objMapObjetos.put("TABLA_DESCRI", txt_pri_origen);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_priori")
    public void valida_Regimen_5() throws SQLException {
        if (!txt_priori.getValue().isEmpty()) {
            String afp = txt_priori.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_priori.setValue(null);
                        txt_priori.setValue(null);
                        txt_priori.focus();
                        txt_pri_origen.setValue("");
                    }
                });

            } else {
                txt_priori.setValue(objConfafp2.getConcep_id());
                txt_pri_origen.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_pri_origen.setValue("");

        }
        bandera = false;
    }

    //LOV txt_prides
    @Listen("onOK=#txt_prides")
    public void busquedaRegimen_6() {
        if (bandera == false) {
            bandera = true;
            if (txt_prides.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_prides);
                objMapObjetos.put("TABLA_DESCRI", txt_pri_destino);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_prides")
    public void valida_Regimen_6() throws SQLException {
        if (!txt_prides.getValue().isEmpty()) {
            String afp = txt_prides.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_prides.setValue(null);
                        txt_prides.setValue(null);
                        txt_prides.focus();
                        txt_pri_destino.setValue("");
                    }
                });

            } else {
                txt_prides.setValue(objConfafp2.getConcep_id());
                txt_pri_destino.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_pri_destino.setValue("");

        }
        bandera = false;
    }

    //LOV txt_mixori
    @Listen("onOK=#txt_mixori")
    public void busquedaRegimen_7() {
        if (bandera == false) {
            bandera = true;
            if (txt_mixori.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_mixori);
                objMapObjetos.put("TABLA_DESCRI", txt_mix_origen);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_mixori")
    public void valida_Regimen_7() throws SQLException {
        if (!txt_mixori.getValue().isEmpty()) {
            String afp = txt_mixori.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_mixori.setValue(null);
                        txt_mixori.setValue(null);
                        txt_mixori.focus();
                        txt_mix_origen.setValue("");
                    }
                });

            } else {
                txt_mixori.setValue(objConfafp2.getConcep_id());
                txt_mix_origen.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_mix_origen.setValue("");

        }
        bandera = false;
    }

    //LOV txt_mixdes
    @Listen("onOK=#txt_mixdes")
    public void busquedaRegimen_8() {
        if (bandera == false) {
            bandera = true;
            if (txt_mixdes.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_mixdes);
                objMapObjetos.put("TABLA_DESCRI", txt_mix_destino);
                objMapObjetos.put("controlador", "ControllerConfAfps");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConfAfp2.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_mixdes")
    public void valida_Regimen_8() throws SQLException {
        if (!txt_mixdes.getValue().isEmpty()) {
            String afp = txt_mixdes.getValue();
            objConfafp2 = objDaoConf.getLovConf2(afp);
            if (objConfafp2 == null) {
                Messagebox.show("El código debe ser de Conceptos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_mixdes.setValue(null);
                        txt_mixdes.setValue(null);
                        txt_mixdes.focus();
                        txt_mix_destino.setValue("");
                    }
                });

            } else {
                txt_mixdes.setValue(objConfafp2.getConcep_id());
                txt_mix_destino.setValue(objConfafp2.getConcep_des());

            }

        } else {
            txt_mix_destino.setValue("");

        }
        bandera = false;
    }

    @Listen("onCtrlKey=#w_confafp")
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
//

    @Listen("onOK=#txt_afpid")
    public void Next_1() {
        txt_apobl.focus();
    }

    @Listen("onOK=#txt_apobl")
    public void Next_2() {
        txt_apori.focus();
    }

    @Listen("onOK=#txt_apori")
    public void Next_3() {
        txt_apodes.focus();
    }

    @Listen("onOK=#txt_apodes")
    public void Next_4() {
        txt_comfig.focus();
    }

    @Listen("onOK=#txt_comfig")
    public void Next_5() {
        txt_comori.focus();
    }

    @Listen("onOK=#txt_comori")
    public void Next_6() {
        txt_comdes.focus();
    }

    @Listen("onOK=#txt_comdes")
    public void Next_7() {
        txt_priseg.focus();
    }

    @Listen("onOK=#txt_priseg")
    public void Next_8() {
        txt_priori.focus();
    }

    @Listen("onOK=#txt_priori")
    public void Next_9() {
        txt_prides.focus();
    }

    @Listen("onOK=#txt_prides")
    public void Next_10() {
        txt_commix.focus();
    }

    @Listen("onOK=#txt_commix")
    public void Next_11() {
        txt_mixori.focus();
    }

    @Listen("onOK=#txt_mixori")
    public void Next_12() {
        txt_mixdes.focus();
    }

    @Listen("onOK=#txt_mixdes")
    public void Next_13() {
        txt_topseg.focus();
    }

    @Listen("onOK=#txt_topseg")
    public void Next_14() {
        txt_idcta.focus();
    }
}
