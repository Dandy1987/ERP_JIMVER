package org.me.gj.controller.componentes;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoCategoria;
import org.me.gj.model.cxc.mantenimiento.Categoria;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class ControllerLovFinancieros extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_financiero;
    @Wire
    Combobox cb_fin_categoria, cb_categoria;
    @Wire
    Longbox txt_fin_limcred, txt_clifin_limcred;
    @Wire
    Intbox txt_fin_limdoc, txt_clifin_limdoc;
    @Wire
    Button btn_aceptar, btn_cancelar;
    //Instancias de Objetos    
    ListModelList<Categoria> objlstCategoria = new ListModelList<Categoria>();
    Categoria objCategoria = new Categoria();
    DaoCategoria objDaoCategoria = new DaoCategoria();
    CliFinanciero objCliFinanciero = new CliFinanciero();
    Utilitarios objUtil = new Utilitarios();
    //Variables publicas
    String transaccion;
    String modoEjecucion;
    Map param;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovFinancieros.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        param = new HashMap(Executions.getCurrent().getArg());
        if (param.get("validaBusqueda").equals("mantFinancierosAdd")) {
            transaccion = "2";
            cb_categoria = (Combobox) param.get("cb_categoria");
            txt_clifin_limcred = (Longbox) param.get("txt_clifin_limcred");
            txt_clifin_limdoc = (Intbox) param.get("txt_clifin_limdoc");
        }
    }

    @Listen("onCreate=#w_lov_financiero")
    public void cargaCombos() throws SQLException, UnknownHostException {
        //carga listas
        objlstCategoria = objDaoCategoria.listaCategorias(1);
        cb_fin_categoria.setModel(objlstCategoria);
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#cb_fin_categoria")
    public void next_cb_fin_categoria() {
        txt_fin_limcred.focus();
    }

    @Listen("onOK=#txt_fin_limcred")
    public void next_txt_clifin_limcred() {
        txt_fin_limdoc.focus();
    }

    @Listen("onOK=#txt_fin_limdoc")
    public void next_txt_fin_limdoc() {
        btn_aceptar.focus();
    }

    @Listen("onClick=#btn_aceptar")
    public void btn_Aceptar() {
        int limdoc, categoria, estado;
        Long limcred;
        if (cb_fin_categoria.getSelectedIndex() == -1) {
            Messagebox.show("Debe seleccionar una categoria de Cliente");
            cb_fin_categoria.focus();
        } else if (txt_fin_limcred.getValue() == null) {
            Messagebox.show("Debe ingresar un Limite de Credito del Cliente");
            txt_fin_limcred.focus();
        } else if (txt_fin_limdoc.getValue() == null) {
            Messagebox.show("Debe ingresar un Limite de Documento del Cliente");
            txt_fin_limdoc.focus();
        } else {
            estado = 1;
            categoria = cb_fin_categoria.getSelectedItem().getValue();
            limcred = txt_fin_limcred.getValue();
            limdoc = txt_fin_limdoc.getValue();
            objCliFinanciero.setClifin_est(estado);
            objCliFinanciero.setClifin_categ(categoria);
            objCliFinanciero.setClifin_limcred(limcred);
            objCliFinanciero.setClifin_limdoc(limdoc);
            objCliFinanciero.setClifin_usuadd(objUsuCredential.getCuenta());
            objCliFinanciero.setEmp_id(objUsuCredential.getCodemp());
            objCliFinanciero.setSuc_id(objUsuCredential.getCodsuc());
            if ("2".equals(transaccion)) {
                txt_clifin_limcred.setValue(limcred);
                txt_clifin_limdoc.setValue(limdoc);
                cb_categoria.setSelectedItem(objUtil.valorPorTexto1(cb_categoria, categoria));
            }
            //creamos sesion de la direccion guardada
            Session sess = Sessions.getCurrent();
            sess.setAttribute("objCliFinanciero", objCliFinanciero);
            /*guardar en un objeto lleno de datos para la insercion*/
            w_lov_financiero.detach();
        }
    }

    @Listen("onClick=#btn_cancelar")
    public void btn_Cancelar() {
        w_lov_financiero.detach();
    }
}
