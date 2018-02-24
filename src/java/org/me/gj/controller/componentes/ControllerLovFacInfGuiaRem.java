package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.DaoVehiculo;
import org.me.gj.controller.seguridad.mantenimiento.DaoSucursales;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovFacInfGuiaRem extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_guiaremision;
    @Wire
    Textbox txt_trans_id, txt_trans_des;
    @Wire
    Datebox d_fecemi, d_fecenvio;
    @Wire
    Combobox cb_tipguia, cb_sucursal;
    @Wire
    Checkbox chk_activos;
    @Wire
    Radiogroup rbg_modo;
    @Wire
    Button btn_consultar, btn_generar;
    //Instancias de Objetos
    ListModelList<Sucursales> objlstSucursal;
    ListModelList<Vehiculo> objlstVehiculos;
    DaoSucursales objDaoSucursal;
    DaoVehiculo objDaoVehiculos;
    Sucursales objSucursal;
    Vehiculo objVehiculo;
    Utilitarios objUtil;
    //Variables publicas
    Map parametros;
    String controlador, empresa, usuario;
    String modoEjecucion;
    public static boolean bandera = false;
    SimpleDateFormat sdffe = new SimpleDateFormat("EEE");
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovFacInfGuiaRem.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");

        objSucursal = new Sucursales();
        objDaoSucursal = new DaoSucursales();
        objDaoVehiculos = new DaoVehiculo();
        objlstVehiculos = new ListModelList<Vehiculo>();
        objUtil = new Utilitarios();
        d_fecemi.setValue(objUtil.hoyAsFecha());
        String diaent = sdffe.format(d_fecemi.getValue());
        if (diaent.equals("sáb")) {
            d_fecenvio.setValue(Utilitarios.sumaDias(d_fecemi.getValue(), 2));
        } else {
            d_fecenvio.setValue(Utilitarios.sumaDias(d_fecemi.getValue(), 1));
        }
        //carga sucursal
        objlstSucursal = new ListModelList<Sucursales>();
        objlstSucursal = objDaoSucursal.lstSucursales(String.valueOf(objUsuCredential.getCodemp()), 1);
        cb_sucursal.setModel(objlstSucursal);

    }

    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_cancelar")
    public void botonSalir() {
        w_lov_guiaremision.detach();
    }

    @Listen("onCheck=#rbg_modo")
    public void seleccionarModo() throws SQLException {
        if (rbg_modo.getSelectedIndex() == 0) {
            txt_trans_id.setDisabled(false);
        } else {
            txt_trans_id.setValue("");
            txt_trans_des.setValue("");
            txt_trans_id.setDisabled(true);
        }
    }

    @Listen("onOK=#txt_trans_id")
    public void lovTransporte() {
        if (bandera == false) {
            bandera = true;
            if (txt_trans_id.getValue().isEmpty()) {
                modoEjecucion = "LovFacInfGuiaRem";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_transid", txt_trans_id);
                objMapObjetos.put("txt_transdes", txt_trans_des);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerLovFacInfGuiaRem");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovTransportes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }

    @Listen("onBlur=#txt_trans_id")
    public void validaTransporte() throws SQLException {
        if (!txt_trans_id.getValue().isEmpty()) {
            if (!txt_trans_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_trans_id.setValue("");
                            txt_trans_des.setValue("");
                            txt_trans_id.focus();
                        }
                    }
                });
            } else {
                Vehiculo objVehiculo;
                objVehiculo = objDaoVehiculos.buscarVehiculoxCodigo(txt_trans_id.getValue());
                if (objVehiculo == null) {
                    Messagebox.show("El código de transporte no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_trans_id.setValue("");
                                txt_trans_des.setValue("");
                                txt_trans_id.focus();
                            }
                        }
                    });
                } else {
                    txt_trans_id.setValue(Utilitarios.lpad(String.valueOf(objVehiculo.getTrans_id()), 4, "0"));
                    txt_trans_des.setValue(objVehiculo.getTrans_alias());
                }
            }
        } else {
            txt_trans_des.setValue("");
        }
        bandera = false;
    }
}
