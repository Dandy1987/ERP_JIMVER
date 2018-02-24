package org.me.gj.controller.cxc.informes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.model.cxc.informes.CliObservacion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerIngObserCxc extends SelectorComposer<Component> {

    //Componentes web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_guardar;
    @Wire
    Textbox txt_obs, txt_usuadd;
    @Wire
    Datebox d_fecadd;
    @Wire
    Listbox lst_inobsercxc;
    @Wire
    Combobox cb_filtro;

    //Instancia de Objetos
    ListModelList<CliObservacion> objlstCliObserva;
    CliObservacion objCliObserva;
    DaoCtaCob objDaoCxcCliente;
    //Variables publicas
    int obs_nro, emp_id, suc_id;
    String campo = "";
    String cli_id, s_emp_id, s_suc_id;
    Map parametros;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerIngObserCxc.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstCliObserva = new ListModelList<CliObservacion>();
        objCliObserva = new CliObservacion();
        objDaoCxcCliente = new DaoCtaCob();
        
        parametros = new HashMap(Executions.getCurrent().getArg());
        emp_id = (Integer) parametros.get("emp_id");
        suc_id = (Integer) parametros.get("suc_id");
        cli_id = (String) parametros.get("cli_id");

        objlstCliObserva = objDaoCxcCliente.listaObservacionCliente(emp_id, suc_id, cli_id);
        lst_inobsercxc.setModel(objlstCliObserva);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void BotonNuevo() {
        habilitaCampos(false);
        limpiar();
        txt_obs.focus();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void BotonGuardar() throws SQLException {
        String valida = valida();
        if (!valida.equals("")) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("observacion")) {
                            txt_obs.focus();
                        }
                    }
                }
            });
        } else {
            objCliObserva = (CliObservacion) generaRegistro();
            String s_mensaje = objDaoCxcCliente.ingresarCliObservacion(objCliObserva);
            Messagebox.show(s_mensaje);
            objlstCliObserva = objDaoCxcCliente.listaObservacionCliente(emp_id, suc_id, cli_id);
            lst_inobsercxc.setModel(objlstCliObserva);
            habilitaCampos(true);
            limpiar();

        }
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion guardar un registro");
    }

    @Listen("onSelect=#lst_inobsercxc")
    public void seleccionObservacion() {
        objCliObserva = (CliObservacion) lst_inobsercxc.getSelectedItem().getValue();
        llenarCampos(objCliObserva);
    }

    public Object generaRegistro() {
        String cli_observa = txt_obs.getValue().toUpperCase();
        String cli_usuadd = objUsuarioCredential.getCuenta();
        return new CliObservacion(emp_id, suc_id, cli_id, 0, cli_observa, cli_usuadd, cli_usuadd, null);
    }

    public void limpiar() {
        txt_obs.setValue("");
        cb_filtro.setSelectedIndex(-1);
    }

    public void llenarCampos(CliObservacion objCliObserva) {
        txt_obs.setValue(objCliObserva.getCli_observa());
        txt_usuadd.setValue(objCliObserva.getCli_usuadd());
        d_fecadd.setValue(objCliObserva.getCli_fecadd());
    }

    public void habilitaCampos(boolean disabled) {
        txt_obs.setDisabled(disabled);
        cb_filtro.setDisabled(!disabled);
    }

    public String valida() {
        String mensaje;
        if (txt_obs.isDisabled()) {
            mensaje = "No se puede grabar porque no esta adicionando o modificando";
        } else if (txt_obs.getValue().isEmpty()) {
            campo = "observacion";
            mensaje = "El campo 'observacion' es obligatorio";
        } else if (txt_obs.getValue().matches("^\\s+")) {
            campo = "observacion";
            mensaje = "En el campo 'observacion' no se permite varios espacios en blanco ";
        } else {
            mensaje = "";
        }
        return mensaje;
    }
}
