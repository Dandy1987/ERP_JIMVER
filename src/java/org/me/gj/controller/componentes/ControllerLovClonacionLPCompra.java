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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovClonacionLPCompra extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_clonacionlpcompra;
    @Wire
    Textbox txt_provid, txt_provdes, txt_lpcdes, txt_lpcnomrep;
    @Wire
    Spinner sp_lpcord;
    @Wire
    Checkbox chk_lpcest;
    @Wire
    Button btn_clonar, btn_cancelar;
    @Wire
    Listbox lst_pcompra;
    @Wire
    Doublebox db_descgen, db_descfinan;
    //Instancias de Objetos
    ListModelList<Precio> objlstPrecioCompra = new ListModelList<Precio>();
    ListModelList<ListaPrecio> objlstListaPCompra = new ListModelList<ListaPrecio>();
    DaoPrecios objDaoPrecios = new DaoPrecios();
    DaoListaPrecios objDaoLpCompra = new DaoListaPrecios();
    ParametrosSalida objParamSalida1, objParamSalida2;
    Precio objPrecio = new Precio();
    ListaPrecio objLpcompra = new ListaPrecio();
    //Variables publicas
    Map parametros;
    Integer emp_id, suc_id;
    String controlador;
    String campo = "";
    String lp_id, lp_provid, lp_provrazsoc, lp_reporte, lp_usuadd;
    Double lp_descgen, lp_descfinan;
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovClonacionLPCompra.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        lp_id = (String) parametros.get("lp_id");
        lp_provid = (String) parametros.get("lp_provid");
        lp_provrazsoc = (String) parametros.get("lp_provrazsoc");
        lp_descgen = (Double) parametros.get("lp_descgen");
        lp_descfinan = (Double) parametros.get("lp_descfinan");
        lp_reporte = (String) parametros.get("lp_reporte");
        lp_usuadd = (String) parametros.get("lp_usuadd");
        lst_pcompra = (Listbox) parametros.get("lst_pcompra");

        llenarCampos();
        habilitacampos(false);
        txt_lpcdes.focus();
    }

    @Listen("onClick=#btn_clonar")
    public void botonClonar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Lista")) {
                            txt_lpcdes.focus();
                        } else if (campo.equals("descgeneral")) {
                            db_descgen.focus();
                        } else if (campo.equals("descfinanciero")) {
                            db_descfinan.focus();
                        }
                    }
                }
            });
        } else {
            Messagebox.show("Está seguro que desea clonar la lista de precios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objLpcompra = (ListaPrecio) generaRegistro();
                                objParamSalida1 = objDaoLpCompra.insertarListaPrecio(objLpcompra);
                                if (objParamSalida1.getFlagIndicador() == 1) {
                                    Messagebox.show(objParamSalida1.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    objlstPrecioCompra = objDaoPrecios.lstPrecios(emp_id, suc_id, lp_provid, lp_id, "1", "%%");
                                    if (objlstPrecioCompra.getSize() == 0) {
                                        Messagebox.show("La nueva lista no tiene precios");
                                    } else {
                                        for (int i = 0; i < objlstPrecioCompra.getSize(); i++) {
                                            objlstPrecioCompra.get(i).setLp_key(objParamSalida1.getCantStocks());
                                            objlstPrecioCompra.get(i).setPre_ord(sp_lpcord.getValue());
                                            objlstPrecioCompra.get(i).setPre_usuadd(objUsuCredential.getCuenta());

                                            objParamSalida2 = objDaoPrecios.insertarPrecio(objlstPrecioCompra.get(i));
                                            if (objParamSalida2.getFlagIndicador() == 1) {
                                                Messagebox.show(objParamSalida2.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                        }
                                        if (objParamSalida2.getFlagIndicador() == 0) {
                                            Messagebox.show("Se clonaron los precios correctamente");
                                        }
                                    }
                                    w_lov_clonacionlpcompra.detach();
                                    objlstListaPCompra = objDaoLpCompra.listaPrecios(emp_id, suc_id, 1, 1);
                                    lst_pcompra.setModel(objlstListaPCompra);
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
        w_lov_clonacionlpcompra.detach();
    }

    @Listen("onOK=#txt_lpcdes")
    public void next_descgeneral() {
        db_descgen.focus();
    }

    @Listen("onOK=#db_descgen")
    public void next_descfinan() {
        db_descfinan.focus();
    }

    @Listen("onBlur=#db_descgen")
    public void valida_descgen() {
        if (db_descgen.getValue() == null) {
            db_descgen.setValue(0.0);
        }
    }

    @Listen("onBlur=#db_descfinan")
    public void valida_descfinan() {
        if (db_descfinan.getValue() == null) {
            db_descfinan.setValue(0.0);
        }
    }

    //Eventos Otros 
    public Object generaRegistro() {
        int i_lp_key = 0;
        String s_lp_des = txt_lpcdes.getValue().toUpperCase().trim();
        long i_prov_key = Long.parseLong(txt_provid.getValue());
        int i_lp_est = 1;
        String s_lp_nomrep = txt_lpcnomrep.getValue().toUpperCase().trim();
        double d_lp_descgen = db_descgen.getValue();
        double d_lp_descfinan = db_descfinan.getValue();
        int i_lp_ord = sp_lpcord.getValue() == null ? 0 : sp_lpcord.getValue();
        String s_lp_usuadd = objUsuCredential.getCuenta();
        String s_lp_usumod = objUsuCredential.getCuenta();
        return new ListaPrecio(i_lp_key, emp_id, suc_id, i_prov_key, s_lp_des, i_lp_est, d_lp_descgen, d_lp_descfinan, i_lp_ord, s_lp_nomrep, s_lp_usuadd, s_lp_usumod);
    }

    public void habilitacampos(boolean valida1) {
        txt_lpcdes.setDisabled(valida1);
        db_descgen.setDisabled(valida1);
        db_descfinan.setDisabled(valida1);
        sp_lpcord.setDisabled(valida1);

    }

    public void llenarCampos() {
        chk_lpcest.setChecked(true);
        txt_provid.setValue(lp_provid);
        txt_provdes.setValue(lp_provrazsoc);
        db_descgen.setValue(lp_descgen);
        db_descfinan.setValue(lp_descfinan);
        txt_lpcnomrep.setValue(lp_reporte);
    }

    public String verificar() {
        String s_valor;
        if (txt_lpcdes.getValue().isEmpty() || txt_lpcdes.getText().trim().length() == 0) {
            s_valor = "El campo Lista es Obligatorio";
            campo = "Lista";
        } else if (db_descgen.getValue() > 100) {
            s_valor = "El descuento general debe ser menor a 100";
            campo = "descgeneral";
        } else if (db_descgen.getValue() < 0.0) {
            s_valor = "El descuento general debe ser mayor a 0";
            campo = "descgeneral";
        } else if (db_descfinan.getValue() > 100) {
            s_valor = "El descuento financiero debe ser menor a 100";
            campo = "descfinanciero";
        } else if (db_descfinan.getValue() < 0.0) {
            s_valor = "El descuento financiero debe ser mayor a 0";
            campo = "descfinanciero";
        } else {
            s_valor = "";
        }
        return s_valor;
    }
}
