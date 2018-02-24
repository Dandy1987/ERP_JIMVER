package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoBancos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.planillas.utilitarios.UtiNroCuenta;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author CHUALLPA
 */
public class ControllerUtiBancos extends SelectorComposer<Component> {

    @Wire
    Combobox cb_periodo, cb_sucursal, cb_bancos, cb_formato;

    @Wire
    Textbox txt_desperiodo, txt_nrocuenta, txt_ref;

    @Wire
    Radiogroup rg_periodo, rg_personal, rg_deposito;

    @Wire
    Button btn_aceptar;

    @Wire
    Datebox db_fecha;

    //Variables Globales
    int i_emp_id;
    public static boolean bandera = false;

    //Objetos de Acceso a la Base de Datos
    DaoEnlaces objDaoEnlaces;
    DaoAccesos objDaoAccesos;
    DaoBancos objDaoBancos;

    //Listas
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();
    ListModelList<Bancos> objlstBancos = new ListModelList<Bancos>();
    ListModelList<UtiNroCuenta> objListNrocta;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //Log
    private static final Logger LOGGER = Logger.getLogger(ControllerUtiBancos.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        i_emp_id = objUsuCredential.getCodemp();
        objDaoEnlaces = new DaoEnlaces();
        objDaoAccesos = new DaoAccesos();
        objDaoBancos = new DaoBancos();
        objlstPerPago = objDaoEnlaces.periodoCerrado(true);
        objlstSucursal = objDaoAccesos.lstSucursales_union(i_emp_id);
        objlstBancos = objDaoBancos.lstBancos(1);
        cb_periodo.setModel(objlstPerPago);
        cb_sucursal.setModel(objlstSucursal);
        cb_bancos.setModel(objlstBancos);
    }

    @Listen("onClick=#rg_periodo")
    public void radioSeleccion() throws SQLException {
        if (rg_periodo.getSelectedIndex() == 0) {
            String periodo = objDaoEnlaces.getPeriodoCalculado(i_emp_id);
            cb_periodo.setValue(periodo);
            cb_periodo.setDisabled(true);
            if (periodo.equals("--------")) {
                txt_desperiodo.setValue("--------");
            } else {
                txt_desperiodo.setValue(objDaoEnlaces.periodoCerradoDesc(periodo, true));
            }
        } else {
            cb_periodo.setValue("");
            cb_periodo.setValue(null);
            txt_desperiodo.setValue("");
            txt_desperiodo.setValue(null);
            cb_periodo.setDisabled(false);
            objlstPerPago = objDaoEnlaces.periodoCerrado(true);
            cb_periodo.setModel(objlstPerPago);
        }
    }

    @Listen("onChange=#cb_periodo")
    public void cargaDesc() throws SQLException {
        if (cb_periodo.getSelectedIndex() == 0) {
            txt_desperiodo.setValue("");
            txt_desperiodo.setValue(null);
        } else {
            txt_desperiodo.setValue(objDaoEnlaces.periodoCerradoDesc(cb_periodo.getValue().toString(), false).substring(8));
        }
    }

    @Listen("onChange=#cb_bancos")
    public void cargaNroCuenta() {
        int ban_key = Integer.parseInt(cb_bancos.getSelectedItem().getValue().toString());
        objListNrocta = objDaoEnlaces.listaNroCuenta(ban_key);

        if (objListNrocta.isEmpty()) {
            Messagebox.show("¡El Banco seleccionado no tiene Nro. de Cuenta en esta Empresa!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            txt_nrocuenta.setValue("");
            txt_nrocuenta.setValue(null);
            cb_formato.setValue("");
            cb_formato.setValue(null);
            cb_formato.setModel(null);
        } else {
            if (bandera == false) {
                bandera = true;
                if (!cb_bancos.getValue().equals("")) {
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("ban_key", cb_bancos.getSelectedItem().getValue().toString());
                    objMapObjetos.put("nro_cuenta", txt_nrocuenta);
                    objMapObjetos.put("controlador", "ControllerUtiBancos");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUtiNroCuenta.zul", null, objMapObjetos);
                    window.doModal();
                }
            }
            objlstBancos = objDaoEnlaces.getFormatoBancos(ban_key);
            cb_formato.setModel(objlstBancos);
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void generaBanco() {
        if (cb_periodo.getSelectedIndex() == 0 || cb_sucursal.getSelectedIndex() == cb_sucursal.getItemCount() - 1 || txt_nrocuenta.getText().equals("") ||
            cb_formato.getSelectedIndex() == cb_formato.getItemCount() - 1 || txt_ref.getValue().equals("")) {
            Messagebox.show("¡Revise los campos!", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
        } else {
            if (cb_periodo.getValue().toString().equals("--------") || txt_desperiodo.getValue().toString().equals("--------") || txt_desperiodo.getValue().toString().equals("")) {
                Messagebox.show("¡Periodo actual no válido!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {

                try {

                    String u_validacion1 = objDaoEnlaces.getPeriodoCalculado(i_emp_id);
                    String u_validacion2 = objDaoEnlaces.periodoCerradoDesc(cb_periodo.getValue().toString(), false).substring(0, 8);

                    if ((rg_periodo.getSelectedIndex() == 0 && u_validacion1.equals(cb_periodo.getValue().toString()))
                            || (rg_periodo.getSelectedIndex() == 1 && u_validacion2.equals(cb_periodo.getValue().toString()))) {

                        restableceCampos();

                    } else {
                        Messagebox.show("¡El periodo elegido cambio de situacion, vuelva a seleccionar el Tipo de Periodo!", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
                        restableceCampos();
                    }
                } catch (SQLException ex) {
                    Messagebox.show("Error al cargar los datos debido al error: " + ex.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
                    LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | no pudo generar el enlace de Bancos de " + objUsuCredential.getEmpresa() + " - " + cb_sucursal.getValue().toString());
                }

            }
        }
    }

    private void restableceCampos() {
        try {
            rg_periodo.setSelectedIndex(1);
            rg_personal.setSelectedIndex(0);
            rg_deposito.setSelectedIndex(1);
            cb_sucursal.setSelectedItem(null);
            cb_sucursal.setValue("");
            cb_periodo.setSelectedItem(null);
            cb_periodo.setValue("");
            cb_bancos.setSelectedItem(null);
            cb_bancos.setValue("");
            cb_formato.setSelectedItem(null);
            cb_formato.setValue("");
            txt_desperiodo.setValue("");
            txt_nrocuenta.setValue("");
            txt_ref.setValue("");
            objlstSucursal = objDaoAccesos.lstSucursales_union(i_emp_id);
            objlstPerPago = objDaoEnlaces.periodoCerrado(true);
            objlstBancos = objDaoBancos.lstBancos(1);
            cb_sucursal.setModel(objlstSucursal);
            cb_periodo.setModel(objlstPerPago);
            cb_bancos.setModel(objlstBancos);
            cb_formato.setModel(null);
            cb_periodo.setDisabled(false);
        } catch (SQLException e) {
            Messagebox.show("Error al cargar los datos debido al error: " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | tuvo un error al cargar los datos.");
        }
    }

}
