/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.ControllerMovLinea;
import static org.me.gj.controller.planillas.procesos.ControllerMovLinea.bandera;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.procesos.Movlinea;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovConstanteBloque extends SelectorComposer<Component> {

    @Wire
    Doublebox d_valor;
    @Wire
    Textbox txt_codcon, txt_descon, txt_periodo;
    @Wire
    Radiogroup rbg_indica;
    @Wire
    Button btn_consultar, btn_procesar;
    @Wire
    Combobox cb_fsucursal, cb_area;
    @Wire
    Listbox lst_bloque;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovLinea.class);
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<Movlinea> objlsPrincipal = new ListModelList<Movlinea>();
    ListModelList<Movlinea> objlsPrincipal2 = new ListModelList<Movlinea>();
    DaoPersonal objDaoPersonal = new DaoPersonal();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Movlinea objMovLinea;
    DaoMovLinea objDaoMovLinea;
    //variable globales
    String foco, s_mensaje;
    @Wire
    Checkbox chk_selecAll;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objDaoMovLinea = new DaoMovLinea();
        //periodo
        String periodo = objDaoMovLinea.setearPeriodo();
        txt_periodo.setValue(periodo);
        //se completa combobox de sucursales
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
        rbg_indica.setSelectedIndex(0);
    }
    /*
     *Eventos Click para mostrar trabajadores y colocar en bloque constante
     *@version 12/08/2017
     *@autor Junior Fernandez Ortiz
     */

    @Listen("onClick=#btn_consultar")
    public void buscarRegistros() throws SQLException {
        limpiarLista();
        String valida = verificar();
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
            double valor = d_valor.getValue();
            String idconstante = txt_codcon.getValue();
            String periodo = txt_periodo.getValue();
            String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
            objlsPrincipal = objDaoMovLinea.buscarBloque(sucursal, "", idconstante, area, periodo, valor);
            //objMovLinea.setValor_concepto(d_valor.getValue());
            if (objlsPrincipal.isEmpty()) {
                Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {

                lst_bloque.setModel(objlsPrincipal);
                chk_selecAll.setChecked(true);
                seleccionatodo();
            }
        }

    }

    /**
     * Metodo de check
     */
    @Listen("onCheck=#chk_selecAll")
    public void seleccionatodo() {
        if (objlsPrincipal.isEmpty()) {
            Messagebox.show("No hay registros a mostrar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlsPrincipal.getSize(); i++) {
                objlsPrincipal.get(i).setValSelec(chk_selecAll.isChecked());

            }
            lst_bloque.setModel(objlsPrincipal);
        }
    }

    /**
     * Seleciona y guarda
     *
     * @param evt
     */
    @Listen("onSeleccion=#lst_bloque")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlsPrincipal = (ListModelList) lst_bloque.getModel();
        //si viene de registrar nuevos recibos
        if (!objlsPrincipal.isEmpty() || objlsPrincipal != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlsPrincipal.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
            lst_bloque.setModel(objlsPrincipal);
        }
    }
    /*
     *Metodo guardar bloque en bd
     */

    @Listen("onClick=#btn_procesar")
    public void guardarBloque() {
        if (txt_periodo.getValue().isEmpty()) {
            Messagebox.show("No puede procesar, no hay periodo en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (objlsPrincipal.isEmpty()) {
            Messagebox.show("No hay datos a procesar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP,JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objPara;
                                //probando para check
                                int i = 0;
                                for (int j = 0; j < objlsPrincipal.getSize(); j++) {
                                    if (objlsPrincipal.get(j).isValSelec()) {
                                        objlsPrincipal2 = objlsPrincipal;
                                        // i = i +1; 
                                        // objPara = objDaoMovLinea.insertarBloque(getConstante(objlsPrincipal));
                                    }

                                }

                                objPara = objDaoMovLinea.insertarBloque(getConstante(objlsPrincipal2));
                                limpiarCampos();
                                limpiarLista();
                                if (objPara.getFlagIndicador() == 0) {
                                    limpiarCampos();
                                    limpiarLista();
                                   // ControllerMovLinea objmov = new ControllerMovLinea();
                                   // objmov.buscarRegistros();
                                    rbg_indica.setSelectedIndex(0);

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

        }

    }

    public Object[][] getConstante(ListModelList<Movlinea> x) {

        ListModelList<Movlinea> objListaDepurada3;
        objListaDepurada3 = x;

        Object[][] listaConstante = new Object[objListaDepurada3.size()][11];
        for (int i = 0; i < objListaDepurada3.size(); i++) {
            if (objListaDepurada3.get(i).isValSelec() == true) {
                listaConstante[i][0] = objUsuCredential.getCodemp();
                listaConstante[i][1] = objListaDepurada3.get(i).getSucursal();//objUsuCredential.getCodsuc(); viene la sucursal del trabajador
                listaConstante[i][2] = objListaDepurada3.get(i).getTipo_doc();//objMovLinea.getTipo_doc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaConstante[i][3] = objListaDepurada3.get(i).getNumero_doc();//objMovLinea.getNumero_doc();//txt_idpersonal.getValue().substring(1);   
                listaConstante[i][4] = txt_periodo.getValue();
                listaConstante[i][5] = txt_codcon.getValue();//objListaDepurada3.get(i).getId_concepto();
                listaConstante[i][6] = objListaDepurada3.get(i).getValor_concepto();
                listaConstante[i][7] = objUsuCredential.getCuenta();
                listaConstante[i][8] = objUsuCredential.getComputerName();
                listaConstante[i][9] = "N";//objListaDepurada3.get(i).getInd_accion(); 
                listaConstante[i][10] = objListaDepurada3.get(i).getNro_ope();
            }

        }
        return listaConstante;
    }

    /*
     *Eventos OK para mostrar Lov
     *@version 12/08/2017
     *@autor Junior Fernandez Ortiz
     */
    //Lov en txt de constante para buscar
    @Listen("onOK=#txt_codcon")
    public void lovConstantePrincipal() {
        if (bandera == false) {
            bandera = true;
            String tipo = "";
            if (rbg_indica.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indica.getSelectedIndex() == 1) {
                tipo = "M";
            }

            if (txt_codcon.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codcon);
                objMapObjetos.put("descripcion", txt_descon);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerMovLinea");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstante.zul", null, objMapObjetos);
                window.doModal();

            } else {
                d_valor.focus();

            }

        }
    }
    /*
     * Salida del campo que identifica si exite el codigo de constante
     */

    @Listen("onBlur=#txt_codcon")
    public void validaConstantePrincipal() throws SQLException {
        if (!txt_codcon.getValue().isEmpty()) {
            String consulta = txt_codcon.getValue();
            String tipo = "";
            if (rbg_indica.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indica.getSelectedIndex() == 1) {
                tipo = "M";
            }
            objMovLinea = objDaoMovLinea.consultaContante(consulta, tipo);
            if (objMovLinea == null) {
                Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codcon.setValue("");
                        txt_codcon.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_codcon.setValue(objMovLinea.getId_concepto());
                txt_descon.setValue(objMovLinea.getDescripcion());
                // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());

            }
        } else {
            txt_descon.setValue("");
        }
        bandera = false;
    }
    /*
     *Metodo para verificar antes de guardar en bloque
     */

    public String verificar() {
        String valor;
        /*if (txt_periodo.getValue().isEmpty()) {
         valor="No hay periodo en proceso";
         foco="periodo";
         }
         else*/ if (txt_codcon.getValue().isEmpty()) {
            valor = "Ingrese Codigo de  Constante";
            foco = "constante";
        } else if (d_valor.getValue() == null) {
            valor = "Ingrese valor";
            foco = "valor";
        } else {
            valor = "";
        }
        return valor;
    }

    //devuelve cursor en campo vacio
    public void devuelveFocus() {
        if (foco.equals("constante")) {
            txt_codcon.focus();
        } else if (foco.equals("valor")) {
            d_valor.focus();
        }
    }

    public void limpiarLista() {
        objlsPrincipal = null;
        objlsPrincipal = new ListModelList<Movlinea>();
        lst_bloque.setModel(objlsPrincipal);
    }

    public void limpiarCampos() {
        txt_codcon.setValue("");
        txt_descon.setValue("");
        d_valor.setValue(null);
        cb_area.setSelectedIndex(-1);
        cb_fsucursal.setSelectedIndex(-1);
    }
}
