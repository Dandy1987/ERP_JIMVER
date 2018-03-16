/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.utilitarios.ModelUtiComision;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

/**
 *
 * @author achocano
 */
public class ControllerUtiComision extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal;
    @Wire
    Textbox txt_periodo;
    @Wire
    Button btn_buscar, btn_procesar;
    @Wire
    Radiogroup rg_tipodata;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Listbox lst_lista;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    DaoPerPago objDaoPerPago;
    DaoAccesos objDaoAccesos;
    DaoEnlaces objDaoEnlace;
    ListModelList<ModelUtiComision> objlstComi = new ListModelList<ModelUtiComision>();
    ListModelList<ModelUtiComision> objlstComi2 = new ListModelList<ModelUtiComision>();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    String foco, s_mensaje;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoAccesos = new DaoAccesos();
        objDaoEnlace = new DaoEnlaces();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        objDaoPerPago = new DaoPerPago();
        encontrarPeriodo();

    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        limpiarListPrincipal();
        encontrarPeriodo();
        String valida = verificarCampos();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devuelveFocus();
                    }
                }
            });
        } else {
            String periodo = txt_periodo.getValue();
            String per = periodo.substring(0, 6);
            String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
            objlstComi = objDaoEnlace.listaComision(objUsuCredential.getCodemp(), sucursal, per);
            if (objlstComi.isEmpty()) {
                Messagebox.show("No se encontraron registros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {
                lst_lista.setModel(objlstComi);
                chk_selecAll.setChecked(true);
                seleccion();
            }
        }

    }

    @Listen("onClick=#btn_procesar")
    public void procesar() throws SQLException {

        encontrarPeriodo();
        String valida = verificarLista();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devuelveFocus();
                    }
                }
            });
        } else {

            s_mensaje = "Esta seguro de guardar los cambios";
            Messagebox.show(s_mensaje, "ERO-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objPara;
                                for (int i = 0; i < objlstComi.getSize(); i++) {
                                    if (objlstComi.get(i).isValSelec()) {
                                        objlstComi2 = objlstComi;
                                    }
                                }
                                
                                String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
                                objPara = objDaoEnlace.insertarComisiones(getLista(objlstComi2),objUsuCredential.getCodemp(),sucursal);
                                
                                if (objPara.getFlagIndicador() == 0) {
                                    limpiarListPrincipal();

                                    if (objPara.getMsgValidacion() == null) {
                                        Messagebox.show("No se realizó ninguna operación", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                    } else {
                                        Messagebox.show(objPara.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {

                                            public void onEvent(Event t) throws Exception {
                                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                                    //va el focus que te mostrara despues de guardar
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                        }
                    });
            /* String periodo = txt_periodo.getValue();
             String per = periodo.substring(0, 6);
             String sucursal = cb_sucursal.getSelectedIndex() == -1 || cb_sucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_sucursal.getSelectedItem().getValue().toString();
             int flag = objDaoEnlace.enlaceComision(objUsuCredential.getCodemp(), sucursal, per);
             if (flag == 0) {
             Messagebox.show("Se realizo exitosamente el enlace", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             } else {
             Messagebox.show("Error al enlazar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
             }*/
        }
    }

    public Object[][] getLista(ListModelList<ModelUtiComision> x) {
        ListModelList<ModelUtiComision> objListAux;
        objListAux = x;
        Object[][] listaEnvio = new Object[objListAux.size()][12];
        for (int i = 0; i < objListAux.size(); i++) {
            if (objListAux.get(i).isValSelec() == true) {
                listaEnvio[i][0] = objListAux.get(i).getEmpresa();
                listaEnvio[i][1] = objListAux.get(i).getSucursal();
                listaEnvio[i][2] = objListAux.get(i).getTipo_doc();
                listaEnvio[i][3] = objListAux.get(i).getDni();
                listaEnvio[i][4] = objListAux.get(i).getPeriodo();
                listaEnvio[i][5] = "064";
                listaEnvio[i][6] = objListAux.get(i).getValor();
                listaEnvio[i][7] = objUsuCredential.getCuenta();
                listaEnvio[i][8] = objUsuCredential.getComputerName();
                listaEnvio[i][9] = objListAux.get(i).getCod_vendedor();
                listaEnvio[i][10] = "1";
                listaEnvio[i][11] = "N";
            }

        }
        return listaEnvio;
    }

    /**
     * Metodo seleccion
     */
    @Listen("onSeleccion=#lst_lista")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstComi = (ListModelList) lst_lista.getModel();
        //si viene de registrar nuevos recibos
        if (!objlstComi.isEmpty() || objlstComi != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstComi.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_lista.setModel(objlstComi);
        }
    }

    /**
     * Metodo para seleccionar todo los resgistros mostrados
     */
    @Listen("onCheck=#chk_selecAll")
    public void seleccion() {
        if (objlstComi.isEmpty()) {
            Messagebox.show("No hay registros a mostrar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstComi.getSize(); i++) {
                objlstComi.get(i).setValSelec(chk_selecAll.isChecked());
            }
            lst_lista.setModel(objlstComi);
        }
    }

    public void encontrarPeriodo() throws SQLException {
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
    }

    public String verificarCampos() {
        String valor;
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "No hay periodo en proceso";
            foco = "periodo";
        } else if (cb_sucursal.getSelectedItem().getValue().toString().equals("0") || cb_sucursal.getSelectedIndex() == -1) {
            valor = "Debe elegir una sucursal";
            foco = "sucursal";
        } else {
            valor = "";
        }

        return valor;

    }

    public String verificarLista() {
        String valor;
        if (objlstComi.isEmpty()) {
            valor = "No hay datos por enlazar";
            foco = "lista";
        } else {
            valor = "";
        }

        return valor;

    }

    public void devuelveFocus() {
        if (foco.equals("periodo")) {
            txt_periodo.focus();
        } else if (foco.equals("sucursal")) {
            cb_sucursal.focus();
        }
    }

    public void limpiarListPrincipal() {
        objlstComi = null;
        objlstComi = new ListModelList<ModelUtiComision>();
        lst_lista.setModel(objlstComi);
    }
}
