package org.me.gj.controller.componentes;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovGarantias extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_garantia;
    @Wire
    Combobox cb_gar_garantia, cb_garantia;
    @Wire
    Longbox txt_gar_monto, txt_cligar_monto;
    @Wire
    Textbox txt_gar_obs, txt_cligar_obs;
    @Wire
    Button btn_aceptar, btn_cancelar;
    //Instancias de Objetos
    CliGarantia objCliGarantia = new CliGarantia();
    //Variables publicas
    Map param;
    String transaccion;
    String modoEjecucion;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovGarantias.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        param = new HashMap(Executions.getCurrent().getArg());
        if (param.get("validaBusqueda").equals("mantGarantiasAdd")) {
            transaccion = "2";
            cb_garantia = (Combobox) param.get("cb_garantia");
            txt_cligar_monto = (Longbox) param.get("txt_cligar_monto");
            txt_cligar_obs = (Textbox) param.get("txt_cligar_obs");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#cb_gar_garantia")
    public void next_cb_gar_garantia() {
        txt_gar_monto.focus();
    }

    @Listen("onOK=#txt_gar_monto")
    public void next_txt_gar_monto() {
        txt_gar_obs.focus();
    }

    @Listen("onOK=#txt_gar_obs")
    public void next_txt_gar_obs() {
        btn_aceptar.focus();
    }

    @Listen("onClick=#btn_aceptar")
    public void btn_Aceptar() {
        int estado;
        Long monto;
        String obs, garantia;
        if (cb_gar_garantia.getSelectedIndex() == -1) {
            Messagebox.show("Debe seleccionar una garantia de Cliente");
            cb_gar_garantia.focus();
        } else if (txt_gar_monto.getValue() == null) {
            Messagebox.show("Debe ingresar un Monto de Garantia del Cliente");
            txt_gar_monto.focus();
        } else if (txt_gar_obs.getValue().isEmpty()) {
            Messagebox.show("Debe ingresar una Observacion de la Garantia del Cliente");
            txt_gar_obs.focus();
        } else {
            estado = 1;
            garantia = cb_gar_garantia.getValue();
            monto = txt_gar_monto.getValue();
            obs = txt_gar_obs.getValue().toUpperCase();
            objCliGarantia.setCligar_est(estado);
            objCliGarantia.setCligar_garantia(garantia);
            objCliGarantia.setCligar_monto(monto);
            objCliGarantia.setCligar_obs(obs);
            objCliGarantia.setCligar_usuadd(objUsuCredential.getCuenta());
            objCliGarantia.setEmp_id(objUsuCredential.getCodemp());
            objCliGarantia.setSuc_id(objUsuCredential.getCodsuc());
            if ("2".equals(transaccion)) {
                txt_cligar_monto.setValue(monto);
                txt_cligar_obs.setValue(obs);
                cb_garantia.setValue(garantia);
            }
            //creamos sesion de la direccion guardada
            Session sess = Sessions.getCurrent();
            sess.setAttribute("objCliGarantia", objCliGarantia);
            /*guardar en un objeto lleno de datos para la insercion*/
            w_lov_garantia.detach();
        }
    }

    @Listen("onClick=#btn_cancelar")
    public void btn_Cancelar() {
        w_lov_garantia.detach();
    }
}
