package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovClonacionLPVenta extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_clonacionlpventa;
    @Wire
    Textbox txt_lpvdes, txt_lpvnomrep;
    @Wire
    Spinner sp_lpvord;
    @Wire
    Checkbox chk_lpcest;
    @Wire
    Button btn_clonar, btn_cancelar;
    @Wire
    Listbox lst_pventa;
    //Instancias de Objetos
    ListModelList<Precio> objlstPrecioVenta = new ListModelList<Precio>();
    ListModelList<ListaPrecio> objlstListaPVenta = new ListModelList<ListaPrecio>();
    DaoPrecios objDaoPrecios = new DaoPrecios();
    DaoListaPrecios objDaoLpVenta = new DaoListaPrecios();
    ParametrosSalida objParamSalida1, objParamSalida2;
    Precio objPrecio = new Precio();
    ListaPrecio objLpventa = new ListaPrecio();
    //Variables publicas
    Map parametros;
    Integer suc_id, emp_id;
    String controlador;
    String lp_id, lp_reporte, lp_usuadd;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovClonacionLPVenta.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        lp_id = (String) parametros.get("lp_id");
        lp_reporte = (String) parametros.get("lp_reporte");
        lp_usuadd = (String) parametros.get("lp_usuadd");
        lst_pventa = (Listbox) parametros.get("lst_pventa");

        llenarCampos();
        txt_lpvdes.setDisabled(false);
        sp_lpvord.setDisabled(false);
    }

    @Listen("onClick=#btn_clonar")
    public void botonClonar() throws SQLException {
        if (txt_lpvdes.getValue().isEmpty()) {
            Messagebox.show("Por favor ingresar nombre de la lista de precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_lpvdes.focus();
                    }
                }
            });
        } else {
            Messagebox.show("Está seguro que desea clonar la lista de precios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objLpventa = (ListaPrecio) generaRegistro();
                                objParamSalida1 = objDaoLpVenta.insertarListaPrecio(objLpventa);
                                if (objParamSalida1.getFlagIndicador() == 1) {
                                    Messagebox.show(objParamSalida1.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    objlstPrecioVenta = objDaoPrecios.lstPrecios(emp_id, suc_id, "%%", lp_id, "2", "%%");
                                    if (objlstPrecioVenta.getSize() == 0) {
                                        Messagebox.show("La nueva lista no tiene precios");
                                    } else {
                                        for (int i = 0; i < objlstPrecioVenta.getSize(); i++) {
                                            objlstPrecioVenta.get(i).setLp_key(objParamSalida1.getCantStocks());
                                            objlstPrecioVenta.get(i).setPre_ord(sp_lpvord.getValue());
                                            objlstPrecioVenta.get(i).setPre_usuadd(objUsuCredential.getCuenta());

                                            objParamSalida2 = objDaoPrecios.insertarPrecio(objlstPrecioVenta.get(i));
                                            if (objParamSalida2.getFlagIndicador() == 1) {
                                                Messagebox.show(objParamSalida2.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                        }
                                        if (objParamSalida2.getFlagIndicador() == 0) {
                                            Messagebox.show("Se clonaron los precios correctamente");
                                        }
                                    }
                                    w_lov_clonacionlpventa.detach();
                                    objlstListaPVenta = objDaoLpVenta.listaPrecios(emp_id, suc_id, 1, 2);
                                    lst_pventa.setModel(objlstListaPVenta);
                                }
                            }
                        }
                    }
            );
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onClick=#btn_cancelar")
    public void botonCancelar() {
        w_lov_clonacionlpventa.detach();
    }

    //Eventos Otros 
    public Object generaRegistro() {
        int i_lp_key = 0;
        String s_lp_des = txt_lpvdes.getValue().toUpperCase().trim();
        int i_lp_est = 1;
        String s_lp_nomrep = txt_lpvnomrep.getValue().toUpperCase().trim();
        int i_lp_ord = sp_lpvord.getValue() == null ? 0 : sp_lpvord.getValue();
        String s_lp_usuadd = objUsuCredential.getCuenta();
        String s_lp_usumod = objUsuCredential.getCuenta();
        return new ListaPrecio(i_lp_key, emp_id, suc_id, s_lp_des, i_lp_est, i_lp_ord, s_lp_nomrep, s_lp_usuadd, s_lp_usumod);
    }

    public void habilitacampos(boolean valida1) {
        txt_lpvdes.setDisabled(valida1);
    }

    public void llenarCampos() {
        chk_lpcest.setChecked(true);
        txt_lpvnomrep.setValue(lp_reporte);
    }

}
