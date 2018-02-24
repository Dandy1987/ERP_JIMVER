package org.me.gj.controller.cxc.utilitarios;

import org.me.gj.controller.logistica.utilitarios.*;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.utilitarios.CierreDia;
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

public class ControllerCierreDiaCxc extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_ciedia;
    @Wire
    Tab tab_lista;
    @Wire
    Listbox lst_cierredia;
    @Wire
    Combobox cb_periodo;
    @Wire
    Toolbarbutton tbbtn_btn_cerrar, tbbtn_btn_revertir;
    //Instancias de Objetos
    ListModelList<ManPer> objlstManPer;
    ListModelList<CierreDia> objlstCierreDias;
    Utilitarios objUtil = new Utilitarios();
    DaoManPer objDaoManPer = new DaoManPer();
    ManPer objManPer = new ManPer();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoCierreDia objDaoCierreDia = new DaoCierreDia();
    CierreDia objCierreDias = new CierreDia();
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
    private static final Logger LOGGER = Logger.getLogger(ControllerCierreDiaCxc.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_apecidia")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objlstManPer = new ListModelList<ManPer>();
        objlstManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objlstManPer);
        objlstManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        //carga los cierres
        objlstCierreDias = new ListModelList<CierreDia>();
        objlstCierreDias = objDaoCierreDia.listaCierreDias(Utilitarios.periodoActual());
        lst_cierredia.setModel(objlstCierreDias);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20201000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a la Apertura y Cierre Diario con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a la Apertura y Cierre Diario con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_cerrar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a cerrar de una Apertura y Cierre Diario");
        } else {
            tbbtn_btn_cerrar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a cerrar de una Apertura y Cierre Diario");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_revertir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a revertir cierre de una Apertura y Cierre Diario");
        } else {
            tbbtn_btn_revertir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a revertir cierre de una Apertura y Cierre Diario");
        }
    }

    @Listen("onSelect=#cb_periodo")
    public void seleccionaPeriodo() throws SQLException {
        String periodo = cb_periodo.getValue();
        objlstCierreDias.clear();
        //carga los cierres
        objlstCierreDias = new ListModelList<CierreDia>();
        objlstCierreDias = objDaoCierreDia.listaCierreDias(periodo);
        lst_cierredia.setModel(objlstCierreDias);
    }

    @Listen("onSelect=#lst_cierredia")
    public void seleccionaRegistro() {
        //limpiamos los datos anteriores
        objCierreDias = new CierreDia();
        objCierreDias = (CierreDia) lst_cierredia.getSelectedItem().getValue();
        if (objCierreDias == null) {
            objCierreDias = objlstCierreDias.get(i_sel + 1);
        }
        i_sel = lst_cierredia.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro de Apertura:  " + objCierreDias.getDias_fec());
    }

    @Listen("onClick=#tbbtn_btn_cerrar")
    public void botonCerrar() throws SQLException {
        if (lst_cierredia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Cerrar este Dia?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objCierreDias.setCiedia_usumod(objUsuCredential.getCuenta());
                                objParamSalida = objDaoCierreDia.CerrarDiaCxc(objCierreDias);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstCierreDias.clear();
                                    seleccionaPeriodo();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_revertir")
    public void botonRevertir() throws SQLException {
        if (lst_cierredia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Revertir el cierre de este Dia?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objCierreDias.setCiedia_usumod(objUsuCredential.getCuenta());
                                objParamSalida = objDaoCierreDia.AbriDiaCxc(objCierreDias);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    objlstCierreDias.clear();
                                    seleccionaPeriodo();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }
}
