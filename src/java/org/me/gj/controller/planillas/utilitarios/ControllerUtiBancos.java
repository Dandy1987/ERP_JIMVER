package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoBancos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.planillas.utilitarios.UtiNroCuenta;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.me.gj.controller.planillas.informes.DaoMovimiento;
import org.me.gj.controller.planillas.mantenimiento.DaoManAreas;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.mantenimiento.Accesos;

import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

/**
 *
 * @author CHUALLPA
 */
public class ControllerUtiBancos extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_banco, cb_formato;

    @Wire
    Textbox txt_periodo, txt_desperiodo, txt_nrocuenta, txt_ref;

    @Wire
    Radiogroup rg_periodo, rg_personal, rg_deposito;

    @Wire
    Button btn_aceptar;
    @Wire
    Label lbl_periododesc;
    @Wire
    Datebox db_fecha;
    @Wire
    Radio rb_cts;

    //Variables Globales
    int i_emp_id;
    public static boolean bandera = false;

    //Objetos de Acceso a la Base de Datos
    DaoEnlaces objDaoEnlaces;
    DaoAccesos objDaoAccesos;
    DaoBancos objDaoBancos;

    ListModelList<Personal> objlstPersonal;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    Personal objPersonal;

    InformesMovimiento objMovimiento;
    DaoMovimiento objDaoMovimiento;
    ManAreas objArea;
    DaoManAreas objDaoAreas = new DaoManAreas();
    Accesos objAccesos = new Accesos();

    DaoPerPago objdaoPerPago;
    DaoMovLinea objDaoMovLinea;

    //Listas
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();
    ListModelList<Bancos> objlstBancos = new ListModelList<Bancos>();
    ListModelList<UtiNroCuenta> objListNrocta;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //Log
    private static final Logger LOGGER = Logger.getLogger(ControllerUtiBancos.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        i_emp_id = objUsuCredential.getCodemp();
        objDaoEnlaces = new DaoEnlaces();
        objDaoAccesos = new DaoAccesos();
        objDaoBancos = new DaoBancos();
        objlstPerPago = objDaoEnlaces.periodoCerrado(true);

        objlstBancos = objDaoBancos.lstBancos(1);
        objDaoMovimiento = new DaoMovimiento();
        objlstPersonal = new ListModelList<Personal>();

        objDaoMovLinea = new DaoMovLinea();
        objdaoPerPago = new DaoPerPago();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        String periodo = objdaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
        txt_periodo.setDisabled(true);
        rg_periodo.setSelectedIndex(0);
        String periodo_descrip = objdaoPerPago.getPeriodoDescripcion(periodo);
        lbl_periododesc.setValue(periodo_descrip);
        cb_banco.setModel(objlstBancos);
        rg_deposito.setSelectedIndex(0);
        rg_personal.setSelectedIndex(0);
        txt_ref.setValue("PAGO QUINCENAL");
    }

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

    @Listen("onChange=#cb_banco")
    public void cargaNroCuenta() {
        int ban_key = Integer.parseInt(cb_banco.getSelectedItem().getValue().toString());
        objListNrocta = objDaoEnlaces.listaNroCuenta(ban_key);

        if (objListNrocta.isEmpty()) {
            Messagebox.show("¡El Banco seleccionado no tiene Nro. de Cuenta en esta Empresa!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            txt_nrocuenta.setValue("");
            txt_nrocuenta.setValue(null);
            cb_formato.setValue("");
            cb_formato.setValue(null);
            cb_formato.setModel(null);
        } else {

            if (!cb_banco.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("ban_key", cb_banco.getSelectedItem().getValue().toString());
                objMapObjetos.put("nro_cuenta", txt_nrocuenta);
                objMapObjetos.put("controlador", "ControllerUtiBancos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUtiNroCuenta.zul", null, objMapObjetos);
                window.doModal();

            }
            objlstBancos = objDaoEnlaces.getFormatoBancos(ban_key);
            cb_formato.setModel(objlstBancos);
        }
    }

    @Listen("onOK=#txt_periodo")
    public void muestraPeriodo() {

        if (txt_periodo.getValue().isEmpty()) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("periodo", txt_periodo);
            objMapObjetos.put("controlador", "ControllerBoletaPago");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesPeriodoAnterior.zul", null, objMapObjetos);
            window.doModal();

        }

    }

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
                    //txt_periodo1.setValue(objMovimiento.getPeriodo() + "','");
                    lbl_periododesc.setValue(objdaoPerPago.getPeriodoDescripcion(txt_periodo.getValue().toString()));
                }
            }
            if (txt_periodo.getValue().toString().substring(6).equals("02")) {
                rb_cts.setDisabled(false);
            } else {
                rb_cts.setDisabled(true);
            }
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void generaBanco() throws IOException, InterruptedException, SQLException {
       
        //procesos
        String parametro_conex_03 = "";//PARA CONEXION DE EMPRESAS
        SimpleDateFormat formateadorA = new SimpleDateFormat("yyyyMMdd");

        String ruta = "D:\\PAGOS_BANCOS\\";//ruta base
     //ruta del bat que se creara
        //la ruta junta

        // String asiento = inb_numeroAsiento.getValue();
        //Integer asiento = inb_numeroAsiento.getValue();
        String nombre_archivo = "PRUEBA.txt";

        String parametro_01, parametro_02, parametro_03, parametro_04, parametro_05, parametro_06, parametro_07;

        parametro_04 = "";
        //paquete

        parametro_01 = i_emp_id + "";
        if (Integer.parseInt(cb_sucursal.getSelectedItem().getValue().toString()) != 0) {

            parametro_02 = cb_sucursal.getSelectedItem().getValue() + "";
            parametro_03 = txt_periodo.getValue();
            if(rg_deposito.getItems().get(0).isChecked()){
                parametro_04="136";
            }else if(rg_deposito.getItems().get(1).isChecked()){
                parametro_04="124";
            }   else if(rg_deposito.getItems().get(2).isChecked()){
                parametro_04="124";
            } else if(rg_deposito.getItems().get(3).isChecked()){
                parametro_04="138";
            } else if(rg_deposito.getItems().get(4).isChecked()){
                parametro_04="XXX";
            }

            parametro_05 = formateadorA.format(db_fecha.getValue());
            parametro_06 = txt_nrocuenta.getValue();
            parametro_07 = txt_ref.getValue().toString().replace(' ','_');
            String nombre_bat = "bcp_pago.bat";
            String ruta_defecto_bat = ruta + nombre_bat;
        // String parametro_emp01 = objdaoUtiBancos.Cabecera(i_emp_id, 1, "20180203");
            // parametro_emp01= parametro_emp01.replace(',','.' );
            parametro_conex_03 = "CODIJISA/codijisa@JIMVER";

            Runtime runtime = Runtime.getRuntime();
            //   String sentencia_batch = "cmd start /c " + ruta_defecto_bat + " " + ruta + nombre_archivo + " "/* + ruta + formato_leasing + " "*/ + parametro_emp01 + " " + parametro_periodo + " " + parametro_conex_03;
            String sentencia_batch = "cmd start /c " + ruta_defecto_bat + " " + ruta + nombre_archivo + " "
                    + parametro_01 + " " + parametro_02 + " " + parametro_03 + " " + parametro_04 + " " + parametro_05 + " "
                    + parametro_06 + " " + parametro_07 + " " + parametro_conex_03;
            Process process = runtime.exec(sentencia_batch);

            int x = process.waitFor();
        //int z=y.waitFor();
            //limpiar();
            if (x == 0) {
                File archivo = new File(ruta + nombre_archivo);
                Filedownload.save(archivo, "text/plain");
                Messagebox.show("Proceso terminado satisfactoriamente...!!!");
            }
        } else {
            Messagebox.show("Debe seleccionar una sucursal");
        }
    }

    @Listen("onCheck=#rg_deposito")
    public void seleccionPago() {

        if (rg_deposito.getItems().get(0).isChecked()) {
            txt_ref.setValue("PAGO QUINCENAL");
        } else if (rg_deposito.getItems().get(1).isChecked()) {
            txt_ref.setValue("PAGO MENSUAL");
        } else if (rg_deposito.getItems().get(2).isChecked()) {
            txt_ref.setValue("PAGO CTS");
        } else if (rg_deposito.getItems().get(3).isChecked()) {
            txt_ref.setValue("PAGO ADELANTO");
        } else if (rg_deposito.getItems().get(4).isChecked()) {
            txt_ref.setValue("PAGO PRESTAMO");
        }

    }

    private void restableceCampos() {
        try {
            rg_periodo.setSelectedIndex(1);
            rg_personal.setSelectedIndex(0);
            rg_deposito.setSelectedIndex(1);
            cb_sucursal.setSelectedItem(null);
            cb_sucursal.setValue("");

            cb_banco.setSelectedItem(null);
            cb_banco.setValue("");
            cb_formato.setSelectedItem(null);
            cb_formato.setValue("");
            txt_desperiodo.setValue("");
            txt_nrocuenta.setValue("");
            txt_ref.setValue("");
            objlstSucursal = objDaoAccesos.lstSucursales_union(i_emp_id);
            objlstPerPago = objDaoEnlaces.periodoCerrado(true);
            objlstBancos = objDaoBancos.lstBancos(1);
            cb_sucursal.setModel(objlstSucursal);

            cb_banco.setModel(objlstBancos);
            cb_formato.setModel(null);

        } catch (SQLException e) {
            Messagebox.show("Error al cargar los datos debido al error: " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | tuvo un error al cargar los datos.");
        }
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

}
