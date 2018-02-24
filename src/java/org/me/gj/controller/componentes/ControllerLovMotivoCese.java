package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.model.planillas.mantenimiento.Tablas1;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovMotivoCese extends SelectorComposer<Component> {

    @Wire
    Window w_lov_motivoCese;
    @Wire
    Combobox cb_motivoCese, cb_motivoCeseLov;
    @Wire
    Textbox txt_ceseDet, txt_ceseObs,
            txt_ceseDetLov, txt_ceseObsLov;

    DaoPersonal objDaoPersonal;
    ListModelList<Tablas1> objlstMotivos;

    Map parametros;

    String controlador;
    String s_estado_dl, motivoCese, ceseDet, ceseObs;

    String banco, moneda, cuenta;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoPersonal = new DaoPersonal();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        s_estado_dl = (String) parametros.get("s_estado_dl");
        cb_motivoCese = (Combobox) parametros.get("cb_motivoCese");
        txt_ceseDet = (Textbox) parametros.get("txt_ceseDet");
        txt_ceseObs = (Textbox) parametros.get("txt_ceseObs");
        
        objlstMotivos = objDaoPersonal.listaTablas1("00021");
        cb_motivoCeseLov.setModel(objlstMotivos);

    }

    @Listen("onCreate=#w_lov_motivoCese")
    public void cargarLov() throws SQLException {

        habilitaCampos(!s_estado_dl.equals("M"));
        cb_motivoCeseLov.setSelectedIndex(cb_motivoCese.getSelectedIndex());
        txt_ceseDetLov.setValue(txt_ceseDet.getValue());
        txt_ceseObsLov.setValue(txt_ceseObs.getValue());

    }

    @Listen("onClick=#btn_salir")
    public void botonSalir() throws SQLException {

        //motivoCese = cb_motivoCese.getSelectedIndex() == -1 ? "" : cb_motivoCese.getSelectedItem().toString();
        cb_motivoCese.setSelectedIndex(cb_motivoCeseLov.getSelectedIndex());
        txt_ceseDet.setValue(txt_ceseDetLov.getValue());
        txt_ceseObs.setValue(txt_ceseObsLov.getValue());
        
        

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
        w_lov_motivoCese.detach();
    }

    @Listen("onClose=w_lov_motivoCese")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

    public void habilitaCampos(boolean b_valida1) {
        cb_motivoCeseLov.setDisabled(b_valida1);
        txt_ceseDetLov.setDisabled(b_valida1);
        txt_ceseObsLov.setDisabled(b_valida1);
    }

}
