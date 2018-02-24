/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.DaoDescuentos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.procesos.Descuentos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovDescuentosBloque extends SelectorComposer<Component> {

    @Wire
    Combobox cb_fsucursal, cb_area;
    @Wire
    Textbox txt_periodo, txt_codcon, txt_descon;
    @Wire
    Button btn_consultar, btn_cancelar, btn_procesar;
    @Wire
    Doublebox txt_monto;
    @Wire
    Listbox lst_bloque;
    @Wire
    Window w_lov_bloque;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<Descuentos> objlstPrincipal = new ListModelList<Descuentos>();
    DaoAccesos objDaoAccesos;
    Descuentos objDescuentos;
    DaoPersonal objDaoPersonal;
    Descuentos objPrincipal;
    DaoDescuentos objDaoDescuentos;
    DaoPerPago objDaoPerPago;
    String foco;
    public static boolean bandera = false;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoAccesos = new DaoAccesos();
        objDaoPersonal = new DaoPersonal();
        objDaoDescuentos = new DaoDescuentos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);

        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
        objDaoPerPago = new DaoPerPago();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
        txt_monto.setValue(0.00);

    }

    @Listen("onOK=#txt_codcon")
    public void muestraConstanteDescuentos() {
        if (bandera == false) {
            bandera = true;
            if (txt_codcon.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codcon);
                objMapObjetos.put("descripcion", txt_descon);
                objMapObjetos.put("controlador", "ControllerLovDescuentosBloque");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstanteDescuentos.zul", null, objMapObjetos);
                window.doModal();

            }
        }
    }

    @Listen("onBlur=#txt_codcon")
    public void validaConstanteMensual() throws SQLException {
        if (!txt_codcon.getValue().isEmpty()) {
            String consulta = txt_codcon.getValue();
            objDescuentos = objDaoDescuentos.consultaContanteDescuento(consulta);
            if (objDescuentos == null) {
                Messagebox.show("El código de constante mensual no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codcon.setValue("");
                        txt_codcon.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_codcon.setValue(objDescuentos.getCod_constante());
                txt_descon.setValue(objDescuentos.getDes_constante());
                // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());

            }
        } else {
            txt_descon.setValue("");
        }
        bandera = false;
    }

    public String verificar() {
        String valor = "";
        if (txt_codcon.getValue().isEmpty()) {
            valor = "Por favor ingresar constante";
            foco = "constante";

        } else if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "Por favor ingresar periodo";

        } else if (txt_monto.getValue() == null) {
            valor = "Por favor ingrese monto";
            foco = "monto";
        }

        return valor;

    }

    @Listen("onClick=#btn_cancelar")
    public void cerrar() {
        w_lov_bloque.detach();

    }

    @Listen("onClick=#btn_consultar")
    public void buscarRegistros() throws SQLException {
        limpiarLista();
        String valida = verificar();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("constante")) {
                            txt_codcon.focus();
                        } else if (foco.equals("monto")) {
                            txt_monto.focus();
                        }
                    }
                }
            });

        } else {
            double monto = txt_monto.getValue();
            String idconstante = txt_codcon.getValue();
            String periodo = txt_periodo.getValue();
            String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
              //objlstPrincipal = objDaoDescuentos.buscarBloque();
        
        }
        
        
        
    }

    public void limpiarLista() {
        objlstPrincipal = null;
        objlstPrincipal = new ListModelList<Descuentos>();
        lst_bloque.setModel(objlstPrincipal);

    }

}
