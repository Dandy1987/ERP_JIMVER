package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author HUALLPA
 */
public class ControllerUtiContabilidad extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_periodo;

    @Wire
    Textbox txt_desperiodo;

    @Wire
    Button btn_aceptar;

    @Wire
    Datebox db_fecha;

    //Variables Globales
    PerPago objPerPago = new PerPago();
    DaoAccesos objDaoAccesos;
    DaoEnlaces objDaoEnlaces;
    int emp_id;

    //Listas
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        emp_id = objUsuCredential.getCodemp();
        objDaoAccesos = new DaoAccesos();
        objDaoEnlaces = new DaoEnlaces();
        objlstSucursal = objDaoAccesos.lstSucursales_union(emp_id);
        objlstPerPago = objDaoEnlaces.periodoCerrado(false);
        cb_sucursal.setModel(objlstSucursal);
        cb_periodo.setModel(objlstPerPago);
    }

    @Listen("onChange=#cb_periodo")
    public void cargaDesc() throws SQLException {
        if (cb_periodo.getSelectedIndex() == 0) {
            txt_desperiodo.setValue("");
            txt_desperiodo.setValue(null);
        } else {
            txt_desperiodo.setValue(objDaoEnlaces.periodoCerradoDesc(cb_periodo.getSelectedItem().getValue().toString(), false));
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void generaConta() {
        if (cb_periodo.getSelectedIndex() == 0 || cb_sucursal.getSelectedIndex() == cb_sucursal.getItemCount() - 1 || db_fecha.getValue() == null) {
            Messagebox.show("¡Revise los campos!", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
        } else {
            int flag = 0;//objDaoEnlaces.validaVoucher(emp_id, null, null, null, null);
            if (flag == 1) {
                Messagebox.show("¡Enlace existente!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {
                String msj = "Estamos Trabajando...";//objDaoEnlaces.generaContab(null, null, null, null);
                Messagebox.show(msj, "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        }
    }

}
