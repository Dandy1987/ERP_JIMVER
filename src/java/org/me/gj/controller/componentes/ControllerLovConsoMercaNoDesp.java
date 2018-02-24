package org.me.gj.controller.componentes;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovConsoMercaNoDesp extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_nodespacho;
    @Wire
    Textbox txt_nroregistro;
    @Wire
    Datebox d_fecini, d_fecfin;
    @Wire
    Combobox cb_listaprecioref;
    @Wire
    Button btn_aceptar, btn_cancelar;
    //Instancias de Objetos
    ListModelList<ListaPrecio> objlstListaPrecios;
    DaoListaPrecios objDaolistaPrecios;
    ListaPrecio objListaPrecio;
    Utilitarios objUtil;
    //Variables publicas
    Map parametros;
    Integer suc_id, emp_id;
    String controlador, empresa, usuario;
    String modoEjecucion;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovConsoMercaNoDesp.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");

        objlstListaPrecios = new ListModelList<ListaPrecio>();
        objDaolistaPrecios = new DaoListaPrecios();
        objListaPrecio = new ListaPrecio();
        objUtil = new Utilitarios();

        objlstListaPrecios = objDaolistaPrecios.listaPrecios(emp_id, suc_id, 1, 2);
        cb_listaprecioref.setModel(objlstListaPrecios);

        d_fecini.setValue(objUtil.hoyAsFecha());
        d_fecfin.setValue(objUtil.hoyAsFecha());
    }

    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_cancelar")
    public void botonSalir() {
        w_lov_nodespacho.detach();
    }

}
