package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoLineas;
import org.me.gj.controller.logistica.procesos.DaoFacPro;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.FacturaLin;
import org.me.gj.model.logistica.mantenimiento.Lineas;
import org.me.gj.model.logistica.procesos.ComprasCab;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerFacProLin extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Textbox txt_linid, txt_lindes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod;
    @Wire
    Button btn_buscarfacturas;
    @Wire
    Listbox lst_factprovxlin, lst_factprovcab;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Doublebox txt_totcant, txt_totdes, txt_totafec, txt_totigv, txt_totventa;
    //Instancias de Objetos
    ListModelList<FacturaLin> objlstFacturaxLin;
    ListModelList<ComprasCab> objlstFactProv;
    ListModelList<ManPer> objlstPeriodos;
    DaoFacPro objDaoFactProv;
    DaoManPer objDaoPeriodo;
    DaoAccesos objDaoAccesos;
    ManPer objPeriodo;
    FacturaLin obFacturaxLin;
    ComprasCab objFactProv;
    Accesos objAccesos;
    //Variables publicas
    int emp_id, suc_id;
    int i_selCab;
    String fechaActual;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerFacProLin.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        fechaActual = Utilitarios.hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objDaoPeriodo = new DaoManPer();
        objDaoFactProv = new DaoFacPro();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        //objlstPeriodos = objDaoPeriodo.listaPeriodos(1);
        objlstPeriodos = objDaoPeriodo.listaPeriodosActual(1, 11);
        objlstPeriodos.add(new ManPer("", ""));
        cb_periodo.setModel(objlstPeriodos);
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10302030, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Factura Proveedor por Linea con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Factura Proveedor por Linea con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Factura Proveedor por Linea");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Factura Proveedor por Linea");
        }
    }
    
    @Listen("onClick=#btn_buscarfacturas")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String resultado/* = ""*/;
        Date fecha_emisioni = d_fecini.getValue();
        Date fecha_emisionf = d_fecfin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni, fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_fecini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_fecfin.getValue());
        }
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_factprovcab.setSelectedIndex(-1);
            limpiarCamposTotales();
        } else {
            String per = cb_periodo.getSelectedIndex() == -1 || cb_periodo.getSelectedItem().getValue() == "" ? "%%" : cb_periodo.getSelectedItem().getValue().toString();
            String lin_id = txt_linid.getValue().isEmpty() ? "%%" : String.valueOf(Integer.parseInt(txt_linid.getValue()));
            objlstFacturaxLin = null;
            lst_factprovxlin.setModel(objlstFacturaxLin);
            objlstFactProv = null;
            objlstFacturaxLin = objDaoFactProv.listaTotalxLinea(f_emisioni, f_emisionf, per, lin_id);
            lst_factprovcab.setModel(objlstFactProv);
            
            if (objlstFacturaxLin.getSize() > 0) {
                lst_factprovxlin.setModel(objlstFacturaxLin);
                llenarCamposTotales();
            } else {
                objlstFacturaxLin = null;
                lst_factprovxlin.setModel(objlstFacturaxLin);
                limpiarCamposTotales();
                Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
        LimpiarCampos();
    }
    
    @Listen("onSelect=#lst_factprovxlin")
    public void seleccionaRegistroTotal() throws SQLException {
        obFacturaxLin = (FacturaLin) lst_factprovxlin.getSelectedItem().getValue();
        String periodo = obFacturaxLin.getPeriodo();
        String fecini = obFacturaxLin.getFecemi();
        String fecfinal = obFacturaxLin.getFecfin();
        String lin_key = String.valueOf(Integer.parseInt(obFacturaxLin.getLin_id()));
        objlstFactProv = null;
        objlstFactProv = objDaoFactProv.FacProvxLinea(fecini, fecfinal, periodo, lin_key);
        LimpiarCampos();
        lst_factprovcab.setModel(objlstFactProv);
    }
    
    @Listen("onSelect=#lst_factprovcab")
    public void seleccionaRegistro() {
        objFactProv = (ComprasCab) lst_factprovcab.getSelectedItem().getValue();
        LimpiarCampos();
        txt_usuadd.setValue(objFactProv.getTc_usuadd());
        d_fecadd.setValue(objFactProv.getTc_fecadd());
        txt_usumod.setValue(objFactProv.getTc_usumod());
        d_fecmod.setValue(objFactProv.getTc_fecmod());
    }
    
    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstFacturaxLin == null || objlstFacturaxLin.isEmpty()) {
            Messagebox.show("No hay facturas de proveedor por linea para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_factprovxlin.getSelectedIndex() >= 0) {
                obFacturaxLin = (FacturaLin) lst_factprovxlin.getSelectedItem().getValue();
                if (obFacturaxLin == null) {
                    obFacturaxLin = objlstFacturaxLin.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("lin_id", obFacturaxLin.getLin_id());
                objMapObjetos.put("lin_des", obFacturaxLin.getLin_des());
                objMapObjetos.put("vdscgen", obFacturaxLin.getVdscgen());
                objMapObjetos.put("vneto", obFacturaxLin.getVafecto());
                objMapObjetos.put("vimpt", obFacturaxLin.getVimpto());
                objMapObjetos.put("vtotal", obFacturaxLin.getVtotal());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionFacProvxLin.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una linea para imprimir sus facturas de proveedor");
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_linid")
    public void lovLineas() {
        if (bandera == false) {
            bandera = true;
            if (txt_linid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "FacturaLin";
                objMapObjetos.put("txt_linid", txt_linid);
                objMapObjetos.put("txt_lindes", txt_lindes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerFacturaLin");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovLineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarfacturas.focus();
            }
        }
    }
    
    @Listen("onBlur=#txt_linid")
    public void validaLineas() throws SQLException {
        if (!txt_linid.getValue().isEmpty()) {
            if (!txt_linid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_linid.setValue("");
                        txt_lindes.setValue("");
                        txt_linid.focus();
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_linid.getValue());
                Lineas objlinea = new DaoLineas().busquedaLineaxCodigo(ven_id);
                if (objlinea == null) {
                    Messagebox.show("El código de linea no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_linid.setValue("");
                            txt_lindes.setValue("");
                            txt_linid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea " + objlinea.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_linid.setValue(objlinea.getTab_subdir());
                    txt_lindes.setValue(objlinea.getTab_subdes());
                }
            }
        } else {
            txt_lindes.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros 
    public void LimpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }
    
    public void LimpiarLista() {
        //limpio mi lista
        objlstFacturaxLin = null;
        objlstFacturaxLin = new ListModelList<FacturaLin>();
        lst_factprovcab.setModel(objlstFacturaxLin);
    }
    
    public double[] calculosTotal() {
        int i, cont = 1;
        double data[] = new double[5];
        for (i = 0; i < objlstFacturaxLin.getSize(); i++) {
            //data[0] = cont++;
            data[0] = data[0] + ((FacturaLin) objlstFacturaxLin.get(i)).getCant();
            data[1] = data[1] + ((FacturaLin) objlstFacturaxLin.get(i)).getVdesc();
            data[2] = data[2] + ((FacturaLin) objlstFacturaxLin.get(i)).getVafecto();
            //data[3] = data[3] + ((FacturaLin) objlstFacturaxLin.get(i)).getVinafecto();
            //data[4] = data[4] + ((FacturaLin) objlstFacturaxLin.get(i)).getVexonerado();
            data[3] = data[3] + ((FacturaLin) objlstFacturaxLin.get(i)).getVimpto();
            data[4] = data[4] + ((FacturaLin) objlstFacturaxLin.get(i)).getVtotal();
        }
        return data;
    }
    
    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_totcant.setValue(data[0]);
        txt_totdes.setValue(data[1]);
        txt_totafec.setValue(data[2]);
        //txt_totinafec.setValue(data[3]);
        //txt_totexo.setValue(data[4]);
        txt_totigv.setValue(data[3]);
        txt_totventa.setValue(data[4]);
        txt_totventa.setStyle("text-align: right; background-color: #BEF781;color: #2E2E2E;");
    }
    
    public void limpiarCamposTotales() {
        txt_totcant.setValue(null);
        txt_totdes.setValue(null);
        txt_totafec.setValue(null);
        //txt_totinafec.setValue(null);
        //txt_totexo.setValue(null);
        txt_totigv.setValue(null);
        txt_totventa.setValue(null);
        txt_totventa.setStyle(null);
    }
}
