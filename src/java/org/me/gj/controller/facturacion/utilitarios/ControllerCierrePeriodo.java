package org.me.gj.controller.facturacion.utilitarios;

import org.me.gj.controller.logistica.utilitarios.*;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.utilitarios.CierrePeriodo;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerCierrePeriodo extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_cieperiodo;
    @Wire
    Tab tab_lista;
    @Wire
    Listbox lst_cierreperiodo;
    @Wire
    Combobox cb_anio;
    @Wire
    Toolbarbutton tbbtn_btn_cerrar, tbbtn_btn_revertir;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPer;
    ListModelList<CierrePeriodo> objlstCierrePeriodos;
    Utilitarios objUtil = new Utilitarios();
    DaoManPer objDaoManPer = new DaoManPer();
    ManPer objManPer = new ManPer();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoCierrePeriodo objDaoCierrePeriodo = new DaoCierrePeriodo();
    CierrePeriodo objCierrePeriodos = new CierrePeriodo();
    //Variables publicas
    String s_mensaje = "";
    int i_sel;
    int valor;
    //Variables de Sesion    
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCierrePeriodo.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_cierreperiodo")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objlstManPer = new ListModelList<ManPer>();
        objlstManPer = objDaoManPer.listaAnios(1);
        cb_anio.setModel(objlstManPer);
        objlstManPer.add(new ManPer("", ""));
        cb_anio.setValue(Utilitarios.anioActual());
        //carga los cierres
        objlstCierrePeriodos = new ListModelList<CierrePeriodo>();
        objlstCierrePeriodos = objDaoCierrePeriodo.listaCierrePeriodo(Utilitarios.anioActual());
        lst_cierreperiodo.setModel(objlstCierrePeriodos);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40302000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a la Apertura y Cierre Mensual con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a la Apertura y Cierre Mensual con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_cerrar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a cerrar de una Apertura y Cierre Mensual");
        } else {
            tbbtn_btn_cerrar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a cerrar de una Apertura y Cierre Mensual");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_revertir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a revertir cierre de una Apertura y Cierre Mensual");
        } else {
            tbbtn_btn_revertir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a revertir cierre de una Apertura y Cierre Mensual");
        }
    }

    @Listen("onSelect=#cb_anio")
    public void seleccionaAnio() throws SQLException {
        String anio = cb_anio.getValue();
        objlstCierrePeriodos.clear();
        //carga los cierres
        objlstCierrePeriodos = new ListModelList<CierrePeriodo>();
        objlstCierrePeriodos = objDaoCierrePeriodo.listaCierrePeriodo(anio);
        lst_cierreperiodo.setModel(objlstCierrePeriodos);
    }

    @Listen("onSelect=#lst_cierreperiodo")
    public void seleccionaRegistro() {
        //limpiamos los datos anteriores
        objCierrePeriodos = new CierrePeriodo();
        objCierrePeriodos = (CierrePeriodo) lst_cierreperiodo.getSelectedItem().getValue();
        if (objCierrePeriodos == null) {
            objCierrePeriodos = objlstCierrePeriodos.get(i_sel + 1);
        }
        i_sel = lst_cierreperiodo.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Apertura:  " + objCierrePeriodos.getPer_id());
    }

    @Listen("onClick=#tbbtn_btn_cerrar")
    public void botonCerrar() throws SQLException {
        if (lst_cierreperiodo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea cerrar este periodo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objCierrePeriodos.setCieper_usumod(objUsuCredential.getCuenta());
                                objParamSalida = objDaoCierrePeriodo.CerrarPeriodoFacturacion(objCierrePeriodos);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstCierrePeriodos.clear();
                                    seleccionaAnio();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_revertir")
    public void botonRevertir() throws SQLException {
        if (lst_cierreperiodo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea revertir el cierre de este periodo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objCierrePeriodos.setCieper_usumod(objUsuCredential.getCuenta());
                                objParamSalida = objDaoCierrePeriodo.AbriPeriodoFacturacion(objCierrePeriodos);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstCierrePeriodos.clear();
                                    seleccionaAnio();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }
}
