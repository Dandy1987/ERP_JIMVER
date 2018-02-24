/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.informes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author achocano
 */
public class ControllerInfPagosCruce extends SelectorComposer<Component> {

    @Wire
    private Textbox txt_periodo;

    private DaoPerPago objDaoPerPago;
	DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerInfPagosCruce.class);
    public static boolean bandera = false;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCumple = rutaFile + "REPORTES\\";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoPerPago = new DaoPerPago();
        //String periodo = objDaoMovLinea.setearPeriodo();
        String periodo = objDaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo.equals("") ? "--------" : periodo);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90305000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes cruce de Personal con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes cruce de Personal con el rol: USUARIO NORMAL");
        }
     

    }
	
    @Listen("onClick=#tbbtn_excel")
    public void exportarExcel() throws ParseException, SQLException, IOException {
        if (txt_periodo.getValue().equals("")  || txt_periodo.getValue().equals("--------")) {
            Messagebox.show("No hay ningun periodo calculado", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } else {
            String periodo_anterior, periodo_tanterior;
            int mes1, mes2;

            int periodoCalcular = Integer.parseInt(txt_periodo.getValue().substring(4, 6));
            //un mes antes del actual
            if (periodoCalcular == 1) {
                mes1 = periodoCalcular + 11;
            } else {
                mes1 = periodoCalcular - 1;

            }
            if (mes1 == 1 || mes1 == 2 || mes1 == 3 || mes1 == 4 || mes1 == 5 || mes1 == 6 || mes1 == 7 || mes1 == 8 || mes1 == 9) {
                periodo_anterior = txt_periodo.getValue().substring(0, 4) + "0" + mes1 + txt_periodo.getValue().substring(6, 8);
            } else {
                periodo_anterior = txt_periodo.getValue().substring(0, 4) + mes1 + txt_periodo.getValue().substring(6, 8);
            }
            //2 meses antes del actual
            if (periodoCalcular == 1) {
                mes2 = periodoCalcular + 10;
            } else if (periodoCalcular == 2) {
                mes2 = periodoCalcular + 10;

            } else {
                mes2 = periodoCalcular - 2;
            }
            if (mes2 == 1 || mes2 == 2 || mes2 == 3 || mes2 == 4 || mes2 == 5 || mes2 == 6 || mes2 == 7 || mes2 == 8 || mes2 == 9) {
                periodo_tanterior = txt_periodo.getValue().substring(0, 4) + "0" + mes2 + txt_periodo.getValue().substring(6, 8);
            } else {
                periodo_tanterior = txt_periodo.getValue().substring(0, 4) + mes2 + txt_periodo.getValue().substring(6, 8);
            }

            Connection conexion = new ConectaBD().conectar();
            InputStream is = null;
            JasperPrint jasperPrint;
            try {
                Map objParam = new HashMap();
                objParam.put("empresa", objUsuCredential.getCodemp());
                objParam.put("actual", txt_periodo.getValue());
                objParam.put("anterio1", periodo_anterior);
                objParam.put("anterior2", periodo_tanterior);
                objParam.put("REPORT_LOCALE", new Locale("en", "US"));
                final Execution exec = Executions.getCurrent();
                is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI("/WEB-INF/reportes/planillas/informes/ReportCrucePlanilla.jasper", false));
                JasperReport reporte = (JasperReport) JRLoader.loadObject(is);
                reporte.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
                int x = 100;
                int y = 0;
                int aleatorio = (int) (Math.random() * x) + y;
                jasperPrint = JasperFillManager.fillReport(reporte, objParam, conexion);
                String nom_reporte = Utilitarios.hoyAsString1() + "PERSONAL" + aleatorio;
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
    }

}
