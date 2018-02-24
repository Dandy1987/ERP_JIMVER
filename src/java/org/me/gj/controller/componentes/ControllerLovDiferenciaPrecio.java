package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.procesos.DaoPedVen;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.model.facturacion.procesos.PedidoVentaDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

public class ControllerLovDiferenciaPrecio extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_difprecio;
    @Wire
    Checkbox chk_difprecio;
    @Wire
    Listbox lst_difprecio;

    //Instancias de Objetos    
    ListModelList<PedidoVentaDet> objlstpedidovendet;
    DaoPedVen objDaoPedVen;

    //Variables publicas 
    Map parametros;
    String modoEjecucion;
    String controlador;
    String nropedven = "";
    public static boolean bandera = false;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovDiferenciaPrecio.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstpedidovendet = new ListModelList<PedidoVentaDet>();
        objDaoPedVen = new DaoPedVen();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        nropedven = (String) parametros.get("nropedven");
        onCheckDiferenciaPrecio();
    }

    @Listen("onCheck=#chk_difprecio")
    public void onCheckDiferenciaPrecio() throws SQLException {
        if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            limpiarLista();
            if (chk_difprecio.isChecked()) {
                objlstpedidovendet = objDaoPedVen.listaPedidoVentaDet(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), nropedven);
                lst_difprecio.setModel(objlstpedidovendet);
            }
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_difprecio")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenNotaES.bandera = false;
        }
    }

    public void limpiarLista() {
        objlstpedidovendet = null;
        lst_difprecio.setModel(objlstpedidovendet);
    }
}
