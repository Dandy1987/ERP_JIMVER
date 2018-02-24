package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.procesos.DaoNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.informes.NotaESProv;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.NotaESCab;
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

public class ControllerNotaESvsFacProv extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Textbox txt_provid, txt_provdes, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_periodo;
    @Wire
    Datebox d_fecini, d_fecfin, d_fecadd, d_fecmod;
    @Wire
    Button btn_buscarnotavsfac;
    @Wire
    Listbox lst_notaesxprov, lst_notescab;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    @Wire
    Doublebox txt_totcant, txt_totafec, txt_totinafec, txt_totexo, txt_totigv, txt_totventa;
    //Instancias de Objetos
    ListModelList<NotaESProv> objlstNotaESProv;
    ListModelList<NotaESCab> objlstNotaES;
    ListModelList<ManPer> objlstPeriodos;
    DaoNotaES objDaoNotaES;
    DaoManPer objDaoPeriodo;
    DaoAccesos objDaoAccesos;
    ManPer objPeriodo;
    NotaESProv objNotaESProv;
    NotaESCab objNotaESCab;
    Accesos objAccesos;
    DaoProveedores objDaoProveedores;
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
    private static final Logger LOGGER = Logger.getLogger(ControllerNotaESvsFacProv.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoProveedores = new DaoProveedores();
        fechaActual = new Utilitarios().hoyAsString();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        objDaoPeriodo = new DaoManPer();
        objDaoNotaES = new DaoNotaES();
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10305010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al Reporte de Factura Proveedor por Proveedor con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Reporte de Factura Proveedor por Proveedor con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Factura Proveedor por Proveedor");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Factura Proveedor por Proveedor");
        }
    }

    @Listen("onClick=#btn_buscarnotavsfac")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        Date fecini = d_fecini.getValue();
        Date fecfin = d_fecfin.getValue();
        String f_emisioni, f_emisionf, periodoi, periodof;
        if (fecini == null) {
            f_emisioni = "";
            periodoi = "";
        } else {
            f_emisioni = sdf.format(fecini);
            periodoi = sdfm.format(fecini);
        }

        if (fecfin == null) {
            f_emisionf = "";
            periodof = "";
        } else {
            f_emisionf = sdf.format(fecfin);
            periodof = sdfm.format(fecfin);
        }

        if (!cb_periodo.getValue().equals("")) {
            if (f_emisioni.isEmpty() || f_emisionf.isEmpty()) {
                if (!(cb_periodo.getValue().equals(periodoi) || f_emisioni.equals(""))) {
                    Messagebox.show("La fecha emision inicial no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    LimpiarLista();
                } else if (!(cb_periodo.getValue().equals(periodof) || f_emisionf.equals(""))) {
                    Messagebox.show("La fecha emision final no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    LimpiarLista();
                } else {
                    BusquedaNotasFacturas(f_emisioni, f_emisionf, cb_periodo.getValue());
                }
            } else {
                if (!cb_periodo.getValue().equals(periodoi) || !cb_periodo.getValue().equals(periodof)) {
                    Messagebox.show("Las fechas no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    LimpiarLista();
                } else {
                    BusquedaNotasFacturas(f_emisioni, f_emisionf, periodoi);
                }
            }
        } else {
            BusquedaNotasFacturas(f_emisioni, f_emisionf, periodoi);
        }
    }

    @Listen("onSelect=#lst_notaesxprov")
    public void seleccionaRegistroTotal() throws SQLException {
        objNotaESProv = (NotaESProv) lst_notaesxprov.getSelectedItem().getValue();
        String fecini = objNotaESProv.getFecemi();
        String prov_id = objNotaESProv.getProvid();
        objlstNotaES = null;
        objlstNotaES = objDaoNotaES.ListaNotaESvsFacxProveedor(fecini, prov_id);
        LimpiarCampos();
        lst_notescab.setModel(objlstNotaES);
    }

    @Listen("onSelect=#lst_notescab")
    public void seleccionaRegistro() {
        objNotaESCab = (NotaESCab) lst_notescab.getSelectedItem().getValue();
        LimpiarCampos();
        txt_usuadd.setValue(objNotaESCab.getNescab_usuadd());
        d_fecadd.setValue(objNotaESCab.getNescab_fecadd());
        txt_usumod.setValue(objNotaESCab.getNescab_usumod());
        d_fecmod.setValue(objNotaESCab.getNescab_fecmod());
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaESProv == null || objlstNotaESProv.isEmpty()) {
            Messagebox.show("No hay notas de E/S VS. Factura por proveedor para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notaesxprov.getSelectedIndex() >= 0) {
                objNotaESProv = (NotaESProv) lst_notaesxprov.getSelectedItem().getValue();
                if (objNotaESProv == null) {
                    objNotaESProv = objlstNotaESProv.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("provid", objNotaESProv.getProvid());
                objMapObjetos.put("prov_razsoc", objNotaESProv.getProvrazsoc());
                objMapObjetos.put("svimpto", objNotaESProv.getSvimpto());
                objMapObjetos.put("svdesc", objNotaESProv.getSvdesc());
                objMapObjetos.put("svafecto", objNotaESProv.getSvafecto());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionNotaESvsFacxProv.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione un proveedor para imprimir sus notas E/S VS. Factura");
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_provid")
    public void lovProveedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                String tipo = "1";
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaESvsFacProv";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaESvsFacProv");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_buscarnotavsfac.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void validaProveedores() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_provid.setValue("");
                        txt_provdes.setValue("");
                        txt_provid.focus();
                    }
                });
            } else {
                Long prov_id = Long.parseLong(txt_provid.getValue());
                Proveedores objProveedor = objDaoProveedores.BusquedaProveedor(prov_id);
                if (objProveedor == null) {
                    Messagebox.show("El código de proveedor no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedor.getProv_id());
                    txt_provdes.setValue(objProveedor.getProv_razsoc());
                }
            }
        } else {
            txt_provdes.setValue("");
        }
        bandera = false;
    }

    //Eventos Otros 
    public void BusquedaNotasFacturas(String f_emisioni, String f_emisionf, String periodoi) throws SQLException {
        String resultado;
        resultado = d_fecini.getValue() == null || d_fecfin.getValue() == null ? "OK" : Utilitarios.compareFechas(d_fecini.getValue(), d_fecfin.getValue());
        if (resultado.equals("F1>")) {
            Messagebox.show("La fecha inicial no puede ser mayor a la fecha final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            LimpiarLista();
            limpiarCamposTotales();
        } else {
            String prov_id = txt_provid.getValue().isEmpty() ? "%%" : txt_provid.getValue();
            objlstNotaESProv = new ListModelList<NotaESProv>();
            objlstNotaESProv = objDaoNotaES.listaTotalNotaESvsFacxProveedor(f_emisioni, f_emisionf, periodoi, prov_id);
            if (objlstNotaESProv.getSize() > 0) {
                lst_notaesxprov.setModel(objlstNotaESProv);
                llenarCamposTotales();
            } else {
                objlstNotaESProv = null;
                lst_notaesxprov.setModel(objlstNotaESProv);
                limpiarCamposTotales();
                Messagebox.show("No existen registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            LimpiarCampos();
            objlstNotaES = null;
            lst_notescab.setModel(objlstNotaES);
        }
    }

    public void LimpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstNotaESProv = null;
        objlstNotaESProv = new ListModelList<NotaESProv>();
        lst_notaesxprov.setModel(objlstNotaESProv);
    }
    
    public double[] calculosTotal() {
        int i, cont = 1;
        double data[] = new double[6];
        for (i = 0; i < objlstNotaESProv.getSize(); i++) {
            //data[0] = cont++;
            data[0] = data[0] + ((NotaESProv) objlstNotaESProv.get(i)).getCant();
            data[1] = data[1] + ((NotaESProv) objlstNotaESProv.get(i)).getVafecto();
            data[2] = data[2] + ((NotaESProv) objlstNotaESProv.get(i)).getVinafecto();
            data[3] = data[3] + ((NotaESProv) objlstNotaESProv.get(i)).getVexonerado();
            data[4] = data[4] + ((NotaESProv) objlstNotaESProv.get(i)).getVimpto();
            data[5] = data[5] + ((NotaESProv) objlstNotaESProv.get(i)).getVtotal();
        }
        return data;
    }

    public void llenarCamposTotales() {
        double data[] = calculosTotal();
        txt_totcant.setValue(data[0]);
        txt_totafec.setValue(data[1]);
        txt_totinafec.setValue(data[2]);
        txt_totexo.setValue(data[3]);
        txt_totigv.setValue(data[4]);
        txt_totventa.setValue(data[5]);
        txt_totventa.setStyle("text-align: right; background-color: #BEF781;color: #2E2E2E;");
    }

    public void limpiarCamposTotales() {
        txt_totcant.setValue(null);
        txt_totafec.setValue(null);
        txt_totinafec.setValue(null);
        txt_totexo.setValue(null);
        txt_totigv.setValue(null);
        txt_totventa.setValue(null);
        txt_totventa.setStyle(null);
    }
}
