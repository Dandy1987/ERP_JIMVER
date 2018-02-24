package org.me.gj.controller.componentes;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
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
import org.zkoss.zul.Window;

public class ControllerLovTelefonos extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_telefono;
    @Wire
    Longbox txt_telef1, txt_telef2, txt_movil, txt_clitel_telef1, txt_clitel_telef2, txt_clitel_movil;
    @Wire
    Button btn_aceptar, btn_cancelar;
    Longbox txt_cli_telef1, txt_cli_telef2, txt_cli_movil;
    Combobox cb_moneda;
    //Instancias de Objetos   
    CliTelefono objCliTelefono = new CliTelefono();
    //Variables publicas
    String transaccion;
    String modoEjecucion;
    Map param;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovTelefonos.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        param = new HashMap(Executions.getCurrent().getArg());
        if (param.get("validaBusqueda").equals("mantTelefonos")) {
            transaccion = "1";
            txt_cli_telef1 = (Longbox) param.get("txt_cli_telef1");
            txt_cli_telef2 = (Longbox) param.get("txt_cli_telef2");
            txt_cli_movil = (Longbox) param.get("txt_cli_movil");
            cb_moneda = (Combobox) param.get("cb_moneda");
        } else if (param.get("validaBusqueda").equals("mantTelefonosAdd")) {
            transaccion = "2";
            txt_clitel_telef1 = (Longbox) param.get("txt_clitel_telef1");
            txt_clitel_telef2 = (Longbox) param.get("txt_clitel_telef2");
            txt_clitel_movil = (Longbox) param.get("txt_clitel_movil");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_telef1")
    public void next_txt_telef1() {
        txt_telef2.focus();
    }

    @Listen("onOK=#txt_telef2")
    public void next_txt_telef2() {
        txt_movil.focus();
    }

    @Listen("onOK=#txt_movil")
    public void next_txt_movil() {
        btn_aceptar.focus();
    }

    @Listen("onClick=#btn_aceptar")
    public void btn_Aceptar() {
        int idtelefono, estado;
        Long telefono1, telefono2, movil;
        if (txt_telef1.getValue() == null) {
            Messagebox.show("Falta ingresar Telefono1");
        } else {
            telefono1 = txt_telef1.getValue();
            if (txt_telef2.getValue() == null) {
                telefono2 = Long.parseLong("0");
            } else {
                telefono2 = txt_telef2.getValue();
            }
            if (txt_movil.getValue() == null) {
                movil = Long.parseLong("0");
            } else {
                movil = txt_movil.getValue();
            }
            estado = 1;
            idtelefono = 1;
            objCliTelefono.setClitel_id(idtelefono);
            objCliTelefono.setClitel_telef1(telefono1);
            objCliTelefono.setClitel_telef2(telefono2);
            objCliTelefono.setClitel_movil(movil);
            objCliTelefono.setClitel_est(estado);
            objCliTelefono.setClitel_usuadd(objUsuCredential.getCuenta());
            if ("1".equals(transaccion)) {
                txt_cli_telef1.setValue(txt_telef1.getValue());
                txt_cli_telef2.setValue(txt_telef2.getValue());
                txt_cli_movil.setValue(txt_movil.getValue());
                cb_moneda.focus();
            } else if ("2".equals(transaccion)) {
                txt_clitel_telef1.setValue(txt_telef1.getValue());
                txt_clitel_telef2.setValue(txt_telef2.getValue());
                txt_clitel_movil.setValue(txt_movil.getValue());
            }
            //creamos sesion de la direccion guardada
            Session sess = Sessions.getCurrent();
            sess.setAttribute("objCliTelefono", objCliTelefono);
            //
            w_lov_telefono.detach();
        }
    }

    @Listen("onClick=#btn_cancelar")
    public void btn_Cancelar() {
        w_lov_telefono.detach();
    }
}
