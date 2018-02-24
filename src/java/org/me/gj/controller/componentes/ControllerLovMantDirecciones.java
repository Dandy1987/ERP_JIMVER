package org.me.gj.controller.componentes;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.ControllerCliente;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovMantDirecciones extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_mantdireccion;
    @Wire
    Textbox txt_clidir_nomvia, txt_clidir_mza, txt_clidir_lote, txt_clidir_nomseg, txt_clidir_referencia,
            txt_direccion, txt_cli_direc, txt_clidir_ref, txt_clidir_zona;

    @Wire
    Longbox txt_clidir_id, txt_clidir_nroint;
    @Wire
    Combobox cb_clidir_idvia, cb_clidir_signo, cb_clidir_idint, cb_clidir_idseg;
    @Wire
    Intbox txt_clidir_numvia;
    @Wire
    Checkbox chk_clidir_estado;
    @Wire
    Button btn_aceptar, btn_cancelar;
    //Instancias de Objetos    
    CliDireccion objCliDireccion;
    //Variables publicas 
    String modoEjecucion;
    String controlador;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovMantDirecciones.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        Map param = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) param.get("controlador");
        if (!param.isEmpty()) {
            if (param.get("validaBusqueda").equals("mantDireccionesAdd")) {
                objCliDireccion = (CliDireccion) param.get("objCliDireccion");
                txt_cli_direc = (Textbox) param.get("txt_clidir_direccion");
                txt_clidir_ref = (Textbox) param.get("txt_clidir_ref");
                txt_clidir_zona = (Textbox) param.get("txt_clidir_zona");
            }
        }
        if (objCliDireccion == null) {
            objCliDireccion = new CliDireccion();
        } else {
            cargarData();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#cb_clidir_idvia")
    public void next_cb_clidir_idvia() {
        txt_clidir_nomvia.focus();
    }

    @Listen("onBlur=#cb_clidir_idvia")
    public void onBlur_cb_clidir_idvia() {
        if (cb_clidir_idvia.getSelectedIndex() == 0) {
            txt_clidir_nomvia.setValue("");
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_nomvia")
    public void next_txt_clidir_nomvia() {
        cb_clidir_signo.focus();
        cb_clidir_signo.select();
    }

    @Listen("onBlur=#txt_clidir_nomvia")
    public void onBlur_txt_clidir_nomvia() {
        if (cb_clidir_idvia.getSelectedIndex() == 0) {
            txt_clidir_nomvia.setValue("");
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#cb_clidir_signo")
    public void next_cb_clidir_signo() {
        txt_clidir_numvia.focus();
    }

    @Listen("onBlur=#cb_clidir_signo")
    public void onBlur_cb_clidir_signo() {
        if (cb_clidir_signo.getSelectedIndex() == 0) {
            txt_clidir_numvia.setValue(null);
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_numvia")
    public void next_txt_clidir_numvia() {
        cb_clidir_idint.focus();
        cb_clidir_idint.select();
    }

    @Listen("onBlur=#txt_clidir_numvia")
    public void onBlur_txt_clidir_numvia() {
        if (cb_clidir_signo.getSelectedIndex() == 0) {
            txt_clidir_numvia.setValue(null);
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#cb_clidir_idint")
    public void next_cb_clidir_idint() {
        txt_clidir_nroint.focus();
    }

    @Listen("onBlur=#cb_clidir_idint")
    public void onBlur_cb_clidir_idint() {
        if (cb_clidir_idint.getSelectedIndex() == 0) {
            txt_clidir_nroint.setValue(null);
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_nroint")
    public void next_txt_clidir_nroint() {
        txt_clidir_mza.focus();
    }

    @Listen("onBlur=#txt_clidir_nroint")
    public void onBlur_txt_clidir_nroint() {
        if (cb_clidir_idint.getSelectedIndex() == 0) {
            txt_clidir_nroint.setValue(null);
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_mza")
    public void next_txt_clidir_mza() {
        txt_clidir_lote.focus();
    }

    @Listen("onBlur=#txt_clidir_mza")
    public void onBlur_txt_clidir_mza() {
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_lote")
    public void next_txt_clidir_lote() {
        cb_clidir_idseg.focus();
        cb_clidir_idseg.select();
    }

    @Listen("onBlur=#txt_clidir_lote")
    public void onBlur_txt_clidir_lote() {
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#cb_clidir_idseg")
    public void next_cb_clidir_idseg() {
        txt_clidir_nomseg.focus();
    }

    @Listen("onBlur=#cb_clidir_idseg")
    public void onBlur_cb_clidir_idseg() {
        if (cb_clidir_idseg.getSelectedIndex() == 0) {
            txt_clidir_nomseg.setValue("");
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_nomseg")
    public void next_txt_clidir_nomseg() {
        txt_clidir_referencia.focus();
    }

    @Listen("onBlur=#txt_clidir_nomseg")
    public void onBlur_txt_clidir_nomseg() {
        if (cb_clidir_idseg.getSelectedIndex() == 0) {
            txt_clidir_nomseg.setValue("");
        }
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onOK=#txt_clidir_referencia")
    public void next_txt_clidir_referencia() {
        btn_aceptar.focus();
    }

    @Listen("onBlur=#txt_clidir_referencia")
    public void onBlur_txt_clidir_referencia() {
        txt_direccion.setValue(generaDireccion());
    }

    @Listen("onClick=#btn_aceptar")
    public void btn_Aceptar() {
        objCliDireccion.setClidir_direc(txt_direccion.getValue());
        objCliDireccion.setClidir_idvia(cb_clidir_idvia.getSelectedIndex());
        objCliDireccion.setClidir_nomvia(txt_clidir_nomvia.getValue().toUpperCase());
        objCliDireccion.setClidir_signo(cb_clidir_signo.getSelectedIndex());
        objCliDireccion.setClidir_numvia(txt_clidir_numvia.getValue() == null ? 0 : txt_clidir_numvia.getValue());
        objCliDireccion.setClidir_idint(cb_clidir_idint.getSelectedIndex());
        objCliDireccion.setClidir_nroint(txt_clidir_nroint.getValue() == null ? 0 : txt_clidir_nroint.getValue());
        objCliDireccion.setClidir_mza(txt_clidir_mza.getValue().toUpperCase());
        objCliDireccion.setClidir_lote(txt_clidir_lote.getValue().toUpperCase());
        objCliDireccion.setClidir_idseg(cb_clidir_idseg.getSelectedIndex());
        objCliDireccion.setClidir_nomseg(txt_clidir_nomseg.getValue().toUpperCase());
        objCliDireccion.setClidir_ref(txt_clidir_referencia.getValue().toUpperCase());
        //creamos sesion de la direccion guardada
        Session sess = Sessions.getCurrent();
        sess.setAttribute("objCliDireccion", objCliDireccion);
        //
        txt_cli_direc.setValue(txt_direccion.getValue().toUpperCase());
        txt_clidir_ref.setValue(txt_clidir_referencia.getValue().toUpperCase());
        txt_clidir_zona.focus();
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        }
        w_lov_mantdireccion.detach();
    }

    @Listen("onClick=#btn_cancelar")
    public void btn_Cancelar() {
        if (controlador.equals("ControllerCliente")) {
            ControllerCliente.bandera = false;
        }
        w_lov_mantdireccion.detach();
    }

    //Eventos Otros 
    public void cargarData() {
        long num = objCliDireccion.getClidir_nroint();
        cb_clidir_idvia.setSelectedIndex(objCliDireccion.getClidir_idvia());
        cb_clidir_idvia.select();
        txt_clidir_nomvia.setValue(objCliDireccion.getClidir_nomvia());
        cb_clidir_signo.setSelectedIndex(objCliDireccion.getClidir_signo());
        if (num == 0) {
            txt_clidir_numvia.setValue(null);
        } else {
            txt_clidir_numvia.setValue(objCliDireccion.getClidir_numvia());
        }
        cb_clidir_idint.setSelectedIndex(objCliDireccion.getClidir_idint());
        if (num == 0) {
            txt_clidir_nroint.setValue(null);
        } else {
            txt_clidir_nroint.setValue(objCliDireccion.getClidir_nroint());
        }
        txt_clidir_mza.setValue(objCliDireccion.getClidir_mza());
        txt_clidir_lote.setValue(objCliDireccion.getClidir_lote());
        cb_clidir_idseg.setSelectedIndex(objCliDireccion.getClidir_idseg());
        txt_clidir_nomseg.setValue(objCliDireccion.getClidir_nomseg());
        txt_clidir_referencia.setValue(objCliDireccion.getClidir_ref());
        txt_direccion.setValue(generaDireccion());
    }

    public String generaDireccion() {
        long interior;
        int signo;
        String s_interior, s_signo, nomvia, manzana, lote, segmento, referencia, direccion;
        nomvia = cb_clidir_idvia.getSelectedIndex() == 0 || cb_clidir_idvia.getSelectedIndex() == -1 ? "" : cb_clidir_idvia.getSelectedItem().getValue() + txt_clidir_nomvia.getValue().trim().toUpperCase();
        signo = cb_clidir_signo.getSelectedIndex() == 0 || cb_clidir_signo.getSelectedIndex() == -1 ? 0 : (txt_clidir_numvia.getValue() == null ? 0 : txt_clidir_numvia.getValue());
        interior = cb_clidir_idint.getSelectedIndex() == 0 || cb_clidir_idint.getSelectedIndex() == -1 ? 0 : (txt_clidir_nroint.getValue() == null ? 0 : txt_clidir_nroint.getValue());
        manzana = txt_clidir_mza.getValue().isEmpty() ? "" : "Mz. " + txt_clidir_mza.getValue().toUpperCase();
        lote = txt_clidir_lote.getValue().isEmpty() ? "" : "Lt. " + txt_clidir_lote.getValue().toUpperCase();
        segmento = cb_clidir_idseg.getSelectedIndex() == 0 || cb_clidir_idseg.getSelectedIndex() == -1 ? "" : cb_clidir_idseg.getSelectedItem().getValue() + txt_clidir_nomseg.getValue().trim().toUpperCase();
        referencia = txt_clidir_referencia.getValue().toUpperCase().isEmpty() ? "" : "Ref. " + txt_clidir_referencia.getValue().toUpperCase();
        s_interior = interior == 0 ? "" : cb_clidir_idint.getSelectedItem().getValue() + String.valueOf(interior);
        s_signo = signo == 0 ? "" : cb_clidir_signo.getSelectedItem().getValue() + String.valueOf(signo);
        direccion = nomvia + " " + s_signo + " " + s_interior + " " + manzana + " " + lote + " " + segmento + " " + referencia;
        return direccion.trim();
    }

}
