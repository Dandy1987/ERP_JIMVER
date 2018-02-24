package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author greyes
 */
public class ControllerLovBancos extends SelectorComposer<Component> {

    @Wire
    Window w_lov_bancos;

    @Wire
    Listbox lst_bancos;

    @Wire
    Textbox txt_banDepHabId, txt_banDepHabDes, txt_busqueda_bancos;

    ListModelList<Bancos> objlstBancos;
    ListModelList<Bancos> objlstBancos2;
    Bancos objBancos;

    DaoPersonal objDaoPersonal = new DaoPersonal();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_banDepHabId = (Textbox) parametros.get("ban_id");
        txt_banDepHabDes = (Textbox) parametros.get("ban_des");
        controlador = (String) parametros.get("controlador");

    }

    @Listen("onCreate=#w_lov_bancos")
    public void cargarSituacion() throws SQLException {
        objlstBancos = objDaoPersonal.busquedaLovBancos();
        lst_bancos.setModel(objlstBancos);
        lst_bancos.focus();
        txt_busqueda_bancos.focus();
    }

    @Listen("onOK=#txt_busqueda_bancos")
    public void buscarBancos() throws SQLException {
        String consulta = txt_busqueda_bancos.getValue().toUpperCase();
        objlstBancos.clear();
        objlstBancos = objDaoPersonal.busquedaLovBancos2(consulta);
        lst_bancos.setModel(objlstBancos);

    }

    @Listen("onChange=#txt_busqueda_bancos")
    public void changefilter() {
        objlstBancos2 = new ListModelList<Bancos>();
        lst_bancos.setModel(getBancos(objlstBancos2));

    }

    public ListModelList<Bancos> getBancos(ListModelList<Bancos> u) {
        for (int i = 0; i < objlstBancos.getSize(); i++) {
            Bancos objBancos;
            objBancos = ((Bancos) objlstBancos.get(i));
            if ((objBancos.getDescripcion()).toString().contains(txt_busqueda_bancos.getValue().toUpperCase().trim())
                    || (objBancos.getId().toString().contains(txt_busqueda_bancos.getValue().trim()))) {
                objlstBancos2.add(objBancos);
            }
        }
        return objlstBancos2;

    }

    @Listen("onOK=#lst_bancos; onClick=#lst_bancos") //onOK=#lst_bancos;
    public void seleccionaSitu() {
        try {
            objBancos = (Bancos) lst_bancos.getSelectedItem().getValue();
            txt_banDepHabId.setValue(String.valueOf(objBancos.getId()));
            txt_banDepHabDes.setValue(objBancos.getDescripcion());
            lst_bancos.focus();

            if (controlador.equals("ControllerPersonal")) {
                ControllerPersonal.bandera = false;
            }

            w_lov_bancos.detach();

        } catch (Exception e) {
			String error = e.toString();
        }
    }

    @Listen("onClose=w_lov_bancos")
    public void cerrarVentana() throws SQLException {

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
