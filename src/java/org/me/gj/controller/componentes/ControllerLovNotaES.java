package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.procesos.ControllerContaFactura;
import org.me.gj.controller.logistica.procesos.ControllerCosteoAuto;
import org.me.gj.controller.logistica.procesos.ControllerCosteoMan;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovNotaES extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_notaes;
    @Wire
    Listbox lst_notaes;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_clidir_horario, txt_clidir_idhorario, txt_clidir_idgiro;
    Textbox txt_nescab_tipnotaes, txt_nomtipnotaes;
    Combobox cb_serie;
    //Instancias de Objetos
    ListModelList<Guias> objlstGuias = new ListModelList<Guias>();
    ListModelList<Numeracion> objlstNumeracion;
    DaoNumeracion objDaoNumeracion = new DaoNumeracion();
    DaoManNotaES objDaoGuias = new DaoManNotaES();
    Guias objGuias = new Guias();
    //Variables publicas
    Map parametros;
    String controlador;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovNotaES.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_nescab_tipnotaes = (Textbox) parametros.get("txt_nescab_tipnotaes");
            txt_nomtipnotaes = (Textbox) parametros.get("txt_nomtipnotaes");
            cb_serie = (Combobox) parametros.get("cb_serie");
        } else if (parametros.get("validaBusqueda").equals("CosteoAuto")) {
            txt_nescab_tipnotaes = (Textbox) parametros.get("txt_nescab_tipnotaes");
            txt_nomtipnotaes = (Textbox) parametros.get("txt_nomtipnotaes");
        } else if (parametros.get("validaBusqueda").equals("CosteoMan")) {
            txt_nescab_tipnotaes = (Textbox) parametros.get("txt_nescab_tipnotaes");
            txt_nomtipnotaes = (Textbox) parametros.get("txt_nomtipnotaes");
        } else if (parametros.get("validaBusqueda").equals("ContaFactura")) {
            txt_nescab_tipnotaes = (Textbox) parametros.get("txt_nescab_tipnotaes");
            txt_nomtipnotaes = (Textbox) parametros.get("txt_nomtipnotaes");
        }
    }

    @Listen("onCreate=#w_lov_notaes")
    public void cargarNotaES() throws SQLException {
        objlstGuias = objDaoGuias.listaGuias(1);
        lst_notaes.setModel(objlstGuias);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarNotaES() throws SQLException {
        objlstGuias = objDaoGuias.busquedaGuias(2, txt_busqueda.getValue().toUpperCase(), 1);
        lst_notaes.setModel(objlstGuias);
    }

    @Listen("onSelect=#lst_notaes")
    public void seleccionaNotaES() throws SQLException {
        objGuias = (Guias) lst_notaes.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_nescab_tipnotaes.setValue(objGuias.getCodigo());
            txt_nomtipnotaes.setValue(objGuias.getDesGui());
            objlstNumeracion = new ListModelList<Numeracion>();
            objlstNumeracion = objDaoNumeracion.listaSeries(Integer.parseInt(txt_nescab_tipnotaes.getValue().toString()));
            cb_serie.setModel(objlstNumeracion);
        } else if (parametros.get("validaBusqueda").equals("CosteoNotaES")) {
            txt_nescab_tipnotaes.setValue(objGuias.getCodigo());
            txt_nomtipnotaes.setValue(objGuias.getDesGui());
        } else if (parametros.get("validaBusqueda").equals("CosteoAuto")) {
            txt_nescab_tipnotaes.setValue(objGuias.getCodigo());
            txt_nomtipnotaes.setValue(objGuias.getDesGui());
        } else if (parametros.get("validaBusqueda").equals("CosteoMan")) {
            txt_nescab_tipnotaes.setValue(objGuias.getCodigo());
            txt_nomtipnotaes.setValue(objGuias.getDesGui());
        } else if (parametros.get("validaBusqueda").equals("ContaFactura")) {
            txt_nescab_tipnotaes.setValue(objGuias.getCodigo());
            txt_nomtipnotaes.setValue(objGuias.getDesGui());
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerCosteoMan")) {
            ControllerCosteoMan.bandera = false;
        }
        if (controlador.equals("ControllerCosteoAuto")) {
            ControllerCosteoAuto.bandera = false;
        }
        if (controlador.equals("ControllerContaFactura")) {
            ControllerContaFactura.bandera = false;
        }
        w_lov_notaes.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_notaes")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerCosteoAuto")) {
            ControllerCosteoAuto.bandera = false;
        }
        if (controlador.equals("ControllerCosteoMan")) {
            ControllerCosteoMan.bandera = false;
        }
        if (controlador.equals("ControllerContaFactura")) {
            ControllerContaFactura.bandera = false;
        }
    }
}
