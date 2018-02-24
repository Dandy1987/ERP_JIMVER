package org.me.gj.controller.logistica.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControlleNotaESInf extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_repnotaes;
    @Wire
    Tab tab_lista;
    @Wire
    Listbox lst_notaes;
    @Wire
    Checkbox chk_imprimir;
    @Wire
    Combobox cb_periodo, cb_notaes;
    @Wire
    Toolbarbutton tbbtn_btn_imprimir;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPer;
    ListModelList<Guias> objlstGuias;
    ListModelList<NotaESCab> objlstNotaESInf;
    Utilitarios objUtil = new Utilitarios();
    DaoManPer objDaoManPer = new DaoManPer();
    ManPer objManPer = new ManPer();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Guias objGuias = new Guias();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    DaoNotaESInf objDaoNotaESInf = new DaoNotaESInf();
    NotaESCab objNotaESCab = new NotaESCab();
    //Variables publicas
    String s_estado = "Q";
    String s_estadoDetalle = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    int i_sel;
    int valor;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControlleNotaESInf.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_notaes")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objlstManPer = new ListModelList<ManPer>();
        objlstManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objlstManPer);
        objlstManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        //carga notas
        objlstGuias = new ListModelList<Guias>();
        objlstGuias = objDaoManNotaES.listaGuias(1);
        cb_notaes.setModel(objlstGuias);
        objlstGuias.add(new Guias(100, ""));
    }
    
    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10308000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a Informes de Nota E/S con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a Informes de Nota E/S con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a imprimir informes de Nota E/S");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a imprimir informes de Nota E/S");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        String s_periodo, s_nescab_tipnotaes = "";
        s_periodo = cb_periodo.getValue();
        if (cb_notaes.getSelectedIndex() != -1) {
            if ("100".equals(cb_notaes.getSelectedItem().getValue().toString())) {
                s_nescab_tipnotaes = "";
            } else {
                s_nescab_tipnotaes = Utilitarios.lpad(cb_notaes.getSelectedItem().getValue().toString(), 3, "0");
            }
        }
        objlstNotaESInf = new ListModelList<NotaESCab>();
        objlstNotaESInf = objDaoNotaESInf.listaNotasInf(s_periodo, s_nescab_tipnotaes);
        lst_notaes.setModel(objlstNotaESInf);

    }
    
    @Listen("onSelect=#lst_notaes")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        objNotaESCab = new NotaESCab();
        objNotaESCab = (NotaESCab) lst_notaes.getSelectedItem().getValue();
        if (objNotaESCab == null) {
            objNotaESCab = objlstNotaESInf.get(i_sel + 1);
        }
        i_sel = lst_notaes.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objNotaESCab.getNescab_id());
    }

    @Listen("onClick=#btn_resumen")
    public void botonResumen() throws SQLException {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
        objMapObjetos.put("usuario", objUsuCredential.getCuenta());
        objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
        objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
        objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
        Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionNotaESResumen.zul", null, objMapObjetos);
        window.doModal();

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaESInf == null || objlstNotaESInf.isEmpty()) {
            Messagebox.show("No hay registro Nota E/S para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String codigoNotaES = "";
            int i = 0;
            for (int j = 0; j < objlstNotaESInf.getSize(); j++) {
                if (objlstNotaESInf.get(j).isSelImp()) {
                    i = i + 1;
                    codigoNotaES = objlstNotaESInf.get(j).getNescab_id();
                }
            }
            if (i <= 0) {
                Messagebox.show("Debe seleccionar un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
                objMapObjetos.put("codigoNotaES", codigoNotaES);
                objMapObjetos.put("sel", i);
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/informes/LovImpresionInfNotaES.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }
    
    @Listen("onSeleccion=#lst_notaes")
    public void seleccionRegistroImprimir(ForwardEvent evt) {
        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
        Listitem item = (Listitem) chk_Reg.getParent().getParent();
        objlstNotaESInf.get(item.getIndex()).setSelImp(chk_Reg.isChecked());
        lst_notaes.setModel(objlstNotaESInf);
    }

    @Listen("onCheck=#chk_imprimir")
    public void seleccionMultiple() {
        if (objlstNotaESInf == null || objlstNotaESInf.isEmpty()) {
            Messagebox.show("No hay Registros para Consultar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_imprimir.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaESInf.getSize(); i++) {
                objlstNotaESInf.get(i).setSelImp(chk_imprimir.isChecked());
            }
            lst_notaes.setModel(objlstNotaESInf);
        }
    }
}
