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
import org.me.gj.controller.logistica.mantenimiento.InterfaceControllers;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManAfpsCre;
import org.me.gj.model.planillas.mantenimiento.RegPensionario;
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
public class ControllerAfpsCre extends SelectorComposer<Component> implements InterfaceControllers {

    @Wire
    Tab tab_listaAfpsCre, tab_mantenimiento;

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,tbbtn_excel;

    @Wire
    Textbox txt_afpid, txt_afpcoden, txt_afpdes, txt_cod_regpen, txt_des_regpen, txt_busqueda_afp, txt_usuadd, txt_usumod, txt_busqueda;

    @Wire
    Spinner sp_cargoord;

    @Wire
    Checkbox chk_afpest;

    @Wire
    Listbox lst_afpcre;

    @Wire
    Datebox d_fecadd, d_fecmod;

    @Wire
    Combobox cb_busqueda, cb_estado;

    //Instancias de objetos
    ListModelList<ManAfpsCre> objlstAfp = new ListModelList<ManAfpsCre>();
    ManAfpsCre objAfp = new ManAfpsCre();
    DaoAfpsCre objDaoAfp = new DaoAfpsCre();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios objUtilitarios = new Utilitarios();
    //LOV REGIMEN PENSIONARIO
    ListModelList<RegPensionario> objlstRegPensionario;
    RegPensionario objRegPensionario;
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
    private static final Logger LOGGER = Logger.getLogger(ControllerAfpsCre.class);

    @Listen("onCreate=#tabbox_afpcre")
    public void llenaLista() throws SQLException {
        cb_estado.setSelectedIndex(2);
        objlstAfp = objDaoAfp.listaAfps(1);
        lst_afpcre.setModel(objlstAfp);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101100, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Cliente con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Cliente con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una afp");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una afp");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una afp");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una afp");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una afp");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una afp");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de una afp");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de una afp");
        }

    }
    
    @Listen("onSelect=#lst_afpcre")
    public void seleccionaRegistro() throws SQLException {
        objAfp = (ManAfpsCre) lst_afpcre.getSelectedItem().getValue();
        if (objAfp == null) {
            objAfp = objlstAfp.get(i_sel + 1);
        }
        i_sel = lst_afpcre.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objAfp.getAfp_id());
    }

    public ManAfpsCre generaRegistro() {
        String s_afp_id = objAfp.getAfp_id();
        String s_afp_des = txt_afpdes.getValue().toUpperCase().trim();
        int s_afp_est;
        if (chk_afpest.isChecked()) {
            s_afp_est = 1;
        } else {
            s_afp_est = 2;
        }
        String s_afp_3 = txt_afpcoden.getValue();
        String s_afp_4 = txt_cod_regpen.getValue();
        String s_afp_usuadd = objUsuCredential.getCuenta();
        String s_afp_usumod = objUsuCredential.getCuenta();

        return new ManAfpsCre(s_afp_id, s_afp_des, s_afp_est, s_afp_3, s_afp_4, s_afp_usuadd, s_afp_usumod);

    }

    public void llenarCampos() {
        txt_afpid.setValue(String.valueOf(objAfp.getAfp_id()));
        txt_afpdes.setValue(objAfp.getAfp_des());
        if (objAfp.getAfp_est() == 1) {
            chk_afpest.setChecked(true);
        } else {
            chk_afpest.setChecked(false);
        }
        txt_afpcoden.setValue(objAfp.getAfp_cod_enl());
        txt_cod_regpen.setValue(objAfp.getAfp_cod_reg());
        txt_des_regpen.setValue(objAfp.getAfp_des_reg());
        txt_usuadd.setValue(objAfp.getAfp_usuadd());
        d_fecadd.setValue(objAfp.getAfp_fecadd());
        txt_usumod.setValue(objAfp.getAfp_usumod());
        d_fecmod.setValue(objAfp.getAfp_fecmod());

    }

    public void habilitaCampos(boolean b_valida) {
        txt_afpdes.setDisabled(b_valida);
        chk_afpest.setDisabled(b_valida);
        txt_cod_regpen.setDisabled(b_valida);
        txt_afpcoden.setDisabled(b_valida);
        

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
        tab_listaAfpsCre.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listaAfpsCre.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public String verificar() {
        String s_valor;
        if (txt_afpdes.getValue().isEmpty()) {
            s_valor = "El campo DESCRIPCIÓN es Obligatorio";
            campo = "descri";
        } else if (!txt_afpdes.getValue().matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")) {
            s_valor = "El campo DESCRIPCIÓN ingresar solo letras";
            campo = "descri";
            txt_afpdes.setValue("");
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda_afp.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstAfp = new ListModelList<ManAfpsCre>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }

        objlstAfp = objDaoAfp.busquedaAfp(i_seleccion, s_consulta, i_estado);

        if (objlstAfp.getSize() > 0) {
            lst_afpcre.setModel(objlstAfp);
        } else {
            objlstAfp = null;
            lst_afpcre.setModel(objlstAfp);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        limpiaAuditoria();

    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda_afp.setDisabled(false);
        } else {
            txt_busqueda_afp.setDisabled(true);
            txt_busqueda_afp.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objAfp = new ManAfpsCre();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_afpcoden.focus();
        chk_afpest.setDisabled(true);
        chk_afpest.setChecked(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_afpcre.getSelectedIndex() == -1) {
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
                            lst_afpcre.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            lst_afpcre.focus();
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_afpcre.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar esta AFP - CREACIÓN?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoAfp.eliminar(objAfp);
                                valor = objDaoAfp.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstAfp.clear();
                                    //cb_estado.setSelectedIndex(0);
                                    //cb_busqueda.setSelectedIndex(0);
                                    txt_busqueda_afp.setValue("%%");
                                    txt_busqueda_afp.setDisabled(true);
                                    objlstAfp = objDaoAfp.listaAfps(1);
                                    lst_afpcre.setModel(objlstAfp);
                                    lst_afpcre.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoAfp.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            txt_afpdes.focus();
                        }

                    }

                }
            });
        } else {
            if (txt_afpdes.getText().matches("^\\s") || txt_afpdes.getText().matches("^\\s+")) {
                Messagebox.show("El campo descripción no debe tener espacios en blanco");

            } else {

                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParametrosSalida;
                                    objlstAfp = new ListModelList<ManAfpsCre>();
                                    objAfp = generaRegistro();//falta
                                    if (s_estado.equals("N")) {
                                        s_mensaje = objDaoAfp.insertar(objAfp);
                                    } else {
                                        s_mensaje = objDaoAfp.modificar(objAfp);
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
                                    txt_busqueda_afp.setValue("%%");
                                    txt_busqueda_afp.setDisabled(true);
                                    objlstAfp = objDaoAfp.listaAfps(1);
                                    lst_afpcre.setModel(objlstAfp);
                                    objAfp = new ManAfpsCre();
                                    lst_afpcre.focus();

                                }
                            }
                        });

            }
        }

    }
    
     @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstAfp == null) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionAfp.zul", null, objMapObjetos);
            window.doModal();
        }
    }
    
     @Listen("onClick=#tbbtn_excel")
    public void exportarExcel() throws SQLException, JRException, FileNotFoundException, IOException {
        if (objlstAfp == null || objlstAfp.isEmpty()) {
            Messagebox.show("No hay registros", "ERP_JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            JasperPrint jasperPrint;
            
            try {
                Map objParam = new HashMap();
                objParam.put("usuario", objUsuCredential.getCuenta());
                objParam.put("P_WHERE", DaoAfpsCre.P_WHERE);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));

                objParam.put(JRParameter.IS_IGNORE_PAGINATION, true);
                URL fileUrl = getClass().getClassLoader().getResource("../reportes/planillas/mantenimiento/ManAfp.jasper");
                JasperReport reporte = (JasperReport) JRLoader.loadObject(fileUrl);

                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuteFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "AFP" + aleatorio;

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

    @Listen("onCtrlKey=#w_afpcre")
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

    @Listen("onOK=#txt_afpcoden")
    public void Netx_1() {
        txt_afpdes.focus();

    }

    @Listen("onOK=#txt_afpdes")
    public void Netx_2() {
        txt_cod_regpen.focus();

    }

    /*@Listen("onOK=#txt_cod_regpen")
     public void Netx_3() {
     tbbtn_btn_guardar.focus();

     }*/
    //LOV REGIMEN PENSIONARIO
    @Listen("onOK=#txt_cod_regpen")
    public void busquedaRegimen() {
        if (bandera == false) {
            bandera = true;
            if (txt_cod_regpen.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("TABLA_ID", txt_cod_regpen);
                objMapObjetos.put("TABLA_DESCRI", txt_des_regpen);
                objMapObjetos.put("controlador", "ControllerAfpsCre");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAfps.zul", null, objMapObjetos);
                window.doModal();
            }

        }
    }

    @Listen("onBlur=#txt_cod_regpen")
    public void valida_Regimen() throws SQLException {
        if (!txt_cod_regpen.getValue().isEmpty()) {
            String regimen = txt_cod_regpen.getValue();
            objRegPensionario = objDaoAfp.getLovRegPensionario(regimen);
            if (objRegPensionario == null) {
                Messagebox.show("El código debe ser del régimen pensionario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_cod_regpen.setValue(null);
                        txt_cod_regpen.setValue(null);
                        txt_cod_regpen.focus();
                        txt_des_regpen.setValue("");
                    }
                });

            } else {
                txt_cod_regpen.setValue(objRegPensionario.getReg_id());
                txt_des_regpen.setValue(objRegPensionario.getReg_des());

            }

        } else {
            txt_des_regpen.setValue("");

        }
        bandera = false;
    }

    public void limpiarCampos() {
        txt_afpid.setValue("");
        txt_afpdes.setValue("");
        txt_afpcoden.setValue("");
        txt_cod_regpen.setValue("");
        txt_des_regpen.setValue("");
        chk_afpest.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstAfp = null;
        objlstAfp = new ListModelList<ManAfpsCre>();
        lst_afpcre.setModel(objlstAfp);
    }

    public void llenaRegistros() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
