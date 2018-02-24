package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.procesos.ControllerGenFacPro;
import org.me.gj.controller.logistica.procesos.DaoOrdCom;
import org.me.gj.model.logistica.procesos.OrdCompDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerLovOrdCompDet extends SelectorComposer<Component> {

    //Componentes Web    
    @Wire
    Window w_lov_ordcompdet;
    @Wire
    Listbox lst_ordcompdet;
    @Wire
    Textbox txt_busqueda;
    Textbox txt_pro_id, txt_pro_des;
    Label lbl_codori;
    Doublebox txt_cantped, txt_vventa, txt_totalped, txt_cantfac, txt_totalfac, txt_preuni, txt_preunifac;
    Toolbarbutton tbbtn_btn_guardarc;
    //Instancias de Objetos 
    ListModelList<OrdCompDet> objlstOrdCompDet;
    OrdCompDet objOrdCompDet;
    DaoOrdCom objDaoOrdComp;
    //Variables publicas 
    String controlador;
    long nropedkey;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovOrdCompDet.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        objDaoOrdComp = new DaoOrdCom();
        if (parametros.get("validaBusqueda").equals("GenFactProv")) {
            nropedkey = Long.parseLong(parametros.get("nropedkey").toString());
            objlstOrdCompDet = objDaoOrdComp.listaOrdCompDet(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), nropedkey, "C");
            lst_ordcompdet.setModel(objlstOrdCompDet);
            txt_pro_id = (Textbox) parametros.get("txt_prodid");
            txt_pro_des = (Textbox) parametros.get("txt_proddes");
            txt_cantped = (Doublebox) parametros.get("txt_cantped");
            txt_cantfac = (Doublebox) parametros.get("txt_cantfac");
            txt_preuni = (Doublebox) parametros.get("txt_preuni");
            txt_preunifac = (Doublebox) parametros.get("txt_preunifac");
            //txt_vventa = (Doublebox) parametros.get("txt_vventa");
            txt_totalped = (Doublebox) parametros.get("txt_totalped");
            txt_totalfac = (Doublebox) parametros.get("txt_totalfac");
            tbbtn_btn_guardarc = (Toolbarbutton) parametros.get("tbbtn_btn_guardarc");
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarProducto() throws SQLException {
        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        objlstOrdCompDet = objDaoOrdComp.listaOrdCompDetxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), nropedkey, "C", txt_busqueda.getValue().toUpperCase());
        lst_ordcompdet.setModel(objlstOrdCompDet);
    }

    @Listen("onSelect=#lst_ordcompdet")
    public void seleccionaDetalle() {
        objOrdCompDet = (OrdCompDet) lst_ordcompdet.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("GenFactProv")) {
            txt_pro_id.setValue(objOrdCompDet.getPro_id());
            txt_pro_des.setValue(objOrdCompDet.getPro_desdes());
            txt_cantped.setValue(objOrdCompDet.getOcd_cantped());
            txt_cantfac.setValue(objOrdCompDet.getOcd_cantped());
            txt_preuni.setValue(objOrdCompDet.getOcd_precio());
            txt_preunifac.setValue(objOrdCompDet.getOcd_precio());
            //txt_vventa.setValue(objOrdCompDet.getOcd_vafecto());
            txt_totalped.setValue(objOrdCompDet.getOcd_vtotal());
            txt_totalfac.setValue(objOrdCompDet.getOcd_vtotal());
            //txt_pro_cant.setFocus(true);
        }
        if (controlador.equals("ControllerGenFactProv")) {
            ControllerGenFacPro.bandera = false;
        }
        w_lov_ordcompdet.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_ordcompdet")
    public void cerrarVentana() {
        if (controlador.equals("ControllerGenFactProv")) {
            ControllerGenFacPro.bandera = false;
        }
    }
}
