package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.utilitarios.DaoCierrePeriodo;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.utilitarios.CierrePeriodo;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class ControllerRegeneraCosto extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Combobox cb_periodo;
    @Wire
    Button btn_procesar;
    @Wire
    Checkbox chk_costorep;
    //Instancias de Objetos
    ListModelList<CierrePeriodo> objlstCierrePeriodos = new ListModelList<CierrePeriodo>();
    DaoCierrePeriodo objDaoCierrePeriodo = new DaoCierrePeriodo();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    CierrePeriodo objCierrePer = new CierrePeriodo();
    Accesos objAccesos = new Accesos();
    DaoRegeneraCosto objDaoRegeneraCosto;
    //Variables publicas
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRegeneraCosto.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoRegeneraCosto = new DaoRegeneraCosto();
        cb_periodo.setModel(new DaoManPer().listaPeriodosActual(1,11));
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10209020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Regenerar Stock con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado en Procesos Regenerar Stock con el rol: USUARIO NORMAL");
        }
        if (objAccesos != null) {
            if (objAccesos.getAcc_ins() == 1) {
                btn_procesar.setDisabled(false);
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a Regenerar Stock");
            } else {
                btn_procesar.setDisabled(true);
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a Regenerar Stock");
            }
        }
    }

    @Listen("onClick=#btn_procesar")
    public void regenerarCosto() throws SQLException {
        if (cb_periodo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String per = cb_periodo.getValue().toString().trim();
            objlstCierrePeriodos = objDaoCierrePeriodo.listaCierrePeriodo(StringFns.substring(cb_periodo.getValue(), 1, 5));
            for (int i = 0; i < objlstCierrePeriodos.size(); i++) {
                if (objlstCierrePeriodos.get(i).getPer_id().toString().equals(per)) {
                    objCierrePer = objlstCierrePeriodos.get(i);
                }
            }
            if (objCierrePer.getCieper_log() == 1) {
                Messagebox.show("¿Desea regenerar el costo para este periodo?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamSalida;
                                    String per_id = cb_periodo.getSelectedItem().getValue();
                                    String stk_usuadd = objUsuCredential.getCuenta();
                                    String cos_rep;
                                    if (chk_costorep.isChecked()) {
                                        cos_rep = "S";
                                    } else {
                                        cos_rep = "N";
                                    }
                                    objParamSalida = objDaoRegeneraCosto.regenerarCosto(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), per_id, stk_usuadd, cos_rep);
                                    Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        });
            } else {
                Messagebox.show("El periodo se encuentra cerrado");
            }
        }
    }
}
