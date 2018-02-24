package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoMotivoRechazo;
import org.me.gj.controller.facturacion.procesos.ControllerProcPedPend;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovMotivoRechazo extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_motrec;
    @Wire
    Listbox lst_motrec;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_motdes;
    Textbox txt_motid;
    Datebox d_fecemi;
    //Instancias de Objetos        
    ListModelList<MotivoRechazo> objlstMotRec = new ListModelList<MotivoRechazo>();
    DaoMotivoRechazo objDaoMotivoRechazo = new DaoMotivoRechazo();
    MotivoRechazo objMotRec;
    //Variables publicas  
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovMotivoRechazo.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("ProcPedPend")) {
            txt_motid = (Textbox) parametros.get("txt_idmotivo");
            txt_motdes = (Textbox) parametros.get("txt_desmotivo");
        }
    }

    @Listen("onCreate=#w_lov_motrec")
    public void cargarMotivos() throws SQLException {
        objlstMotRec = objDaoMotivoRechazo.listaMotivoRecxTipo("PV");
        lst_motrec.setModel(objlstMotRec);
    }

    @Listen("onSelect=#lst_motrec")
    public void seleccionaMotivo() {
        objMotRec = (MotivoRechazo) lst_motrec.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("ProcPedPend")) {
            txt_motid.setValue(objMotRec.getMrec_id());
            txt_motdes.setValue(objMotRec.getMrec_des());
        }
        if (controlador.equals("ControllerProcPedPend")) {
            ControllerProcPedPend.bandera = false;
        }
        w_lov_motrec.detach();
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarDireccion() throws SQLException {
        objlstMotRec = objDaoMotivoRechazo.busquedaMotivoRechazo(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_motrec.setModel(objlstMotRec);
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstMotRec = objDaoMotivoRechazo.listaMotivoRecxTipo("PV");
            lst_motrec.setModel(objlstMotRec);
        }
    }

    @Listen("onClose=#w_lov_motrec")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerProcPedPend")) {
            ControllerProcPedPend.bandera = false;
        }
    }
}
