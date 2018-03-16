/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Movlinea;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
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
 * @version 23/08/2017
 */
public class ControllerMovimiento extends SelectorComposer<Component> {

    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar,
            tbbtn_btn_deshacer, tbbtn_btn_imprimir;

    @Wire
    Tab tab_listamovi;
    @Wire
    Textbox txt_usuadd, txt_usumod, txt_codper, txt_desper, txt_codper1, txt_desper1,
            txt_codcon, txt_descon, txt_periodo, txt_codcon1, txt_periodo1;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Listbox lst_movimiento;
    @Wire
    Button btn_consultar;
    @Wire
    Combobox cb_fsucursal;
    @Wire
    Doublebox txt_total;
    @Wire
    Radiogroup rg_periodo;
    @Wire
    Checkbox chk_positivos, chk_negativos;
	
	@Wire
    Label lbl_periododesc;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();
    ListModelList<InformesMovimiento> objlstMovimiento;
    InformesMovimiento objMovimiento;
    DaoAccesos objDaoAccesos;
    DaoPerPago objdaoPerPago;
    DaoMovimiento objDaoMovimiento;
    DaoMovLinea objDaoMovLinea;
    Movlinea objMovLinea;
    Personal objPersonal;
	Accesos objAccesos = new Accesos();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovimiento.class);
    public static boolean bandera = false;
    //RUTAS
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoMovLinea = new DaoMovLinea();
        objDaoAccesos = new DaoAccesos();
        objdaoPerPago = new DaoPerPago();
        objDaoMovimiento = new DaoMovimiento();
        objlstMovimiento = new ListModelList<InformesMovimiento>();
        //se completa combobox de sucursales
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        //objlstPerPago = objdaoPerPago.busquedaPeriodoCerrado();
        // cb_periodo.setModel(objlstPerPago);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
		String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);
        txt_periodo.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
		
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90301000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Movimiento con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Movimiento con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Movimiento");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Movimiento");
        }

    }
	
    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_codper")
    public void busquedaPersonalMan() {

        if (bandera == false) {
            bandera = true;
            if (txt_codper.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codper);
                objMapObjetos.put("des_per", txt_desper);
                objMapObjetos.put("cod", txt_codper1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }
    /*
     * Metodo que se realiza al salir del campo codigo de personal
     * @version 09/08/2017
     * @autor Junior Fernandez
     */

    //Salir de lov para el filtro
    @Listen("onBlur=#txt_codper")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codper.getValue().isEmpty()) {
            if (!txt_codper.getValue().equals("ALL")) {
                String id = txt_codper.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
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
                    txt_codper1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_desper.setValue("");
            txt_codper1.setValue("");
        }

        bandera = false;
    }

    //Lov en constante principal
    @Listen("onOK=#txt_codcon")
    public void muestraConstanteDescuentosPrincipal() {
        if (bandera == false) {
            bandera = true;
            if (txt_codcon.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codcon);
                objMapObjetos.put("descripcion", txt_descon);
                objMapObjetos.put("cod", txt_codcon1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesConstanteMovimiento.zul", null, objMapObjetos);
                window.doModal();

            }
        }
    }

    //lov para periodo
    //Lov en constante principal
    @Listen("onOK=#txt_periodo")
    public void muestraPeriodo() {
        if (bandera == false) {
            bandera = true;
            if (txt_periodo.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("periodo", txt_periodo);
                objMapObjetos.put("per", txt_periodo1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesPeriodo.zul", null, objMapObjetos);
                window.doModal();

            }
        }
    }

    //@Listen("onChange=#txt_periodo")//optimizar salto de text
    @Listen("onBlur=#txt_periodo")
    public void salirPeriodo() throws SQLException {

        if (!txt_periodo.getValue().isEmpty()) {
            if (!txt_periodo.getValue().equals("ALL")) {
                String consulta = txt_periodo.getValue();
                objMovimiento = objDaoMovimiento.getperiodo(consulta);
                if (objMovimiento == null) {
                    txt_periodo.setValue("");
                    Messagebox.show("El periodo no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            //txt_periodo.setValue("");
                            txt_periodo.focus();
                        }
                    });
                } else {
                    txt_periodo.setValue(objMovimiento.getPeriodo());
                    txt_periodo1.setValue(objMovimiento.getPeriodo() + "','");

                    lbl_periododesc.setValue(objdaoPerPago.getPeriodoDescripcion(txt_periodo.getValue().toString()));

                }
            } else {
                lbl_periododesc.setValue("VARIOS");
            }
        } else {
            txt_periodo1.setValue("");
        }

        bandera = false;
    }

    /* @Listen("onChange=#txt_periodo")
     public void puntero() throws SQLException {
     if (txt_periodo.getValue().length() == 8) {
     salirPeriodo();
     }
     }*/

    /*
     * Salida del campo que identifica si exite el codigo de constante
     */
    @Listen("onBlur=#txt_codcon")
    public void validaConstantePrincipal() throws SQLException {
        if (!txt_codcon.getValue().isEmpty()) {

            if (!txt_codcon.getValue().equals("ALL")) {
                String consulta = txt_codcon.getValue();
                objMovimiento = objDaoMovimiento.consultaConstante(consulta);
                if (objMovimiento == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codcon.setValue("");
                            txt_codcon.focus();
                        }
                    });
                } else {
                    //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_codcon.setValue(objMovimiento.getCod_constante());
                    txt_descon.setValue(objMovimiento.getDes_constante());
                    // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());
                    ///   String constante = txt_codcon.getValue();
                    txt_codcon1.setValue(objMovimiento.getCod_constante() + "','");
                }
            }
        } else { //(txt_codcon.getValue().isEmpty()) {
            txt_descon.setValue("");
            txt_codcon1.setValue("");
        }
        bandera = false;
    }

    /**
     * Metodo para busqueda general o filtros
     *
     * @throws java.sql.SQLException
     */
    @Listen("onClick=#btn_consultar")
    public void consultaPrincipal() throws SQLException {
        salirPeriodo();
        if (!txt_periodo.getValue().isEmpty()) {
            String sucursal = cb_fsucursal.getSelectedIndex() == -1 || cb_fsucursal.getSelectedItem().getValue().toString().trim().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            String personal = txt_codper1.getValue();//txt_codper.getValue()
            String periodo = txt_periodo1.getValue().equals("") ? txt_periodo.getValue() : txt_periodo.getValue();
            String constante = txt_codcon1.getValue();//txt_codcon.getValue();
            int val_m = 0;
            if (chk_positivos.isChecked()) {
                val_m = 1;
            } else if (chk_negativos.isChecked()) {
                val_m = 2;
            } else {
                val_m = 0;
            }
            if (rg_periodo.getSelectedIndex() == 0) {
                objlstMovimiento = objDaoMovimiento.muestraListaTplplames(sucursal, periodo, personal, constante, val_m);
            } else {
                objlstMovimiento = objDaoMovimiento.muestraListaTplplanilla(sucursal, periodo, personal, constante, val_m);
            }

            if (objlstMovimiento.isEmpty()) {
                objlstMovimiento = null;
                lst_movimiento.setModel(objlstMovimiento);;
                Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");

            } else {
                lst_movimiento.setModel(objlstMovimiento);
                sumarLista();
            }
        }
        /*else{
         Messagebox.show("Debe ingresar un periodo", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");   
         }*/

    }

    /**
     * Metodo para seleccion de radiogroup en la pantalla principal
     *
     * @throws java.sql.SQLException
     */
    @Listen("onClick=#rg_periodo")
    public void radioSeleccion() throws SQLException {
        if (rg_periodo.getSelectedIndex() == 0) {
            String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
            txt_periodo.setValue(periodo);
            txt_periodo.setDisabled(true);
			String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
            lbl_periododesc.setValue(periodo_descrip);

        } else {
            txt_periodo.setValue("");
            txt_periodo.setDisabled(false);

        }
    }

    /* @Listen("onClick=#tbbtn_pdf")
     public void exportarPdf() throws SQLException, FileNotFoundException, IOException {
     if (objlstMovimiento == null || objlstMovimiento.isEmpty()) {
     Messagebox.show("No hay Registros para exportacion", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     Connection conexion = new ConectaBD().conectar();
     InputStream is = null;
     JasperPrint jasperPrint;
     int x = 100;
     int y = 0;
     int aleatorio = (int) (Math.random() * x) + y;
     String nom_reporte;
     File file;
     Map<String, Object> objHashMap = new HashMap<String, Object>();
     try {
     //  Map objMapObjetos = new HashMap();
     objHashMap.put("P_WHERE", DaoMovimiento.P_WHERE);
     objHashMap.put("usuario", objUsuCredential.getCuenta());
     objHashMap.put("REPORT_LOCALE", new Locale("en", "US"));
     final Execution exec = Executions.getCurrent();
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportInformesMovimiento.jasper", false));
     jasperPrint = JasperFillManager.fillReport(is, objHashMap, conexion);
     nom_reporte = Utilitarios.hoyAsString1() + "-" + objUsuCredential.getCodemp() + objUsuCredential.getCodsuc() + "_" + "InformesMovimiento-" + aleatorio;
     JRPdfExporter exporter = new JRPdfExporter();
     exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
     exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".pdf");
     exporter.exportReport();
     file = new File(rutaFileCumple + nom_reporte + ".pdf");
     Filedownload.save(file, "application/pdf");
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

     @Listen("onClick=#tbbtn_excel")
     public void exportarExcel() throws SQLException, FileNotFoundException, IOException {
     if (objlstMovimiento == null || objlstMovimiento.isEmpty()) {
     Messagebox.show("No hay Registros para exportacion", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     Connection conexion = new ConectaBD().conectar();
     InputStream is = null;
     JasperPrint jasperPrint;
     try {
     Map objParam = new HashMap();

     objParam.put("P_WHERE", DaoMovimiento.P_WHERE);
     objParam.put("usuario", objUsuCredential.getCuenta());
     final Execution exec = Executions.getCurrent();
     is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportInformesMovimiento.jasper", false));
     JasperReport reporte = (JasperReport) JRLoader.loadObject(is);//fileUrl
     reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     int x = 100;
     int y = 0;
     int aleatorio = (int) (Math.random() * x) + y;
     jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
     String nom_reporte = Utilitarios.hoyAsString1() + "InformesMovimiento" + aleatorio;
     JExcelApiExporter exporterXLS = new JExcelApiExporter();
     exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
     exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, rutaFileCumple + nom_reporte + ".xls");
     exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
     exporterXLS.exportReport();
     File file = new File(rutaFileCumple + nom_reporte + ".xls");
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
     }*/
    //suma los montos de la lista principal
    public void sumarLista() {
        double a = 0;
        for (int i = 0; i < objlstMovimiento.size(); i++) {
            a = objlstMovimiento.get(i).getMonto() + a;
        }
        txt_total.setValue(a);
    }

    /**
     * metodo para impresion por pantalla
     */
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimir() {

        if (objlstMovimiento == null) {
            Messagebox.show("No hay datos a imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            //objMapObjetos.put("movimiento", objlstMovimiento);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesMovimiento.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onClick=#chk_positivos")
    public void desmarcar1() {
        chk_negativos.setChecked(false);
    }

    @Listen("onClick=#chk_negativos")
    public void desmarcar2() {
        chk_positivos.setChecked(false);
    }

}
