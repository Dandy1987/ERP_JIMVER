package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.utilitarios.ControllerUtiBancos;
import org.me.gj.controller.planillas.utilitarios.DaoEnlaces;
import org.me.gj.model.planillas.utilitarios.UtiNroCuenta;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author CHUALLPA
 */
public class ControllerLovUtiNroCuenta extends SelectorComposer<Component> {

    @Wire
    Window w_lov_utinrocta;

    @Wire
    Listbox lst_nrocta;

    @Wire
    Textbox txt_buscuenta, txt_nrocuenta_lov;

    //Variables Globales
    Map parametros;
    String controlador;
    String ban_key, nro_cta;
    UtiNroCuenta objNrocta;

    //Data Access Objects (DAO)
    DaoEnlaces objDaoEnlaces = new DaoEnlaces();

    //Listas
    ListModelList<UtiNroCuenta> objListNrocta;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        ban_key = (String) parametros.get("ban_key");
        txt_nrocuenta_lov = (Textbox) parametros.get("nro_cuenta");
        controlador = (String) parametros.get("controlador");
    }

    @Listen("onCreate=#w_lov_utinrocta")
    public void cargarPersonal() throws SQLException {
        if (controlador.equals("ControllerUtiBancos")) {
            objListNrocta = objDaoEnlaces.listaNroCuenta(Integer.parseInt(ban_key));
            lst_nrocta.setModel(objListNrocta);
            lst_nrocta.focus();
            txt_buscuenta.focus();
        }
    }

    @Listen("onOK=#txt_buscuenta")
    public void buscarPersonal() throws SQLException {
        String filtro = txt_buscuenta.getValue().toUpperCase();
        if (controlador.equals("ControllerUtiBancos")) {
        }
    }

    @Listen("onOK=#lst_nrocta; onClick=#lst_nrocta")
    public void selectNroCuenta() {
        try {
            if (lst_nrocta.getSelectedIndex() != -1) {
                objNrocta = (UtiNroCuenta) lst_nrocta.getSelectedItem().getValue();
                txt_nrocuenta_lov.setValue(objNrocta.getS_nrocta());
                lst_nrocta.focus();

                if (controlador.equals("ControllerUtiBancos")) {
                    ControllerUtiBancos.bandera = false;
                }
                w_lov_utinrocta.detach();
            }
        } catch (WrongValueException e) {
            Messagebox.show("Error de Selección debido al Error " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Listen("onClose=#w_lov_utinrocta")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerUtiBancos")) {
            ControllerUtiBancos.bandera = false;
        }
    }

}
