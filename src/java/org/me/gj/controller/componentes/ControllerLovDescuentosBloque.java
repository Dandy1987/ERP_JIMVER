/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.Date;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
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
    Combobox cb_fsucursal, cb_area, cb_cargo;
    @Wire
    Textbox txt_periodo, txt_codcon, txt_descon, txt_glosa;
    @Wire
    Button btn_consultar, btn_cancelar, btn_procesar;
    @Wire
    Doublebox txt_monto;
    @Wire
	Datebox d_fecha;
    @Wire
    Listbox lst_bloque;
    @Wire
	Checkbox chk_selecAll;
    @Wire
    Window w_lov_bloque;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<Descuentos> objlstPrincipal = new ListModelList<Descuentos>();
	ListModelList<Descuentos> objlstPrincipal2 = new ListModelList<Descuentos>();
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
        buscarPeriodo();
        /*String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
         txt_periodo.setValue(periodo);*/
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
        if (txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equals("--------")) {
            valor = "No hay periodo en proceso para continuar con la operacion ";

        } else if (txt_codcon.getValue().isEmpty()) {
            valor = "Por favor ingresar constante";
            foco = "constante";
        } else if (txt_glosa.getValue().isEmpty()) {
            valor = "Por favor ingresar glosa";
            foco = "glosa";
        } else if (cb_cargo.getSelectedItem().getValue().toString().equals("1")) {
            valor = "Por favor debe elegir un cargo";
            foco = "cargo";
        } else if (txt_monto.getValue() <= 0) {
            valor = "Debe ingresar un monto mayor a cero";
            foco = "monto";
        }

        return valor;

    }

    @Listen("onClick=#btn_cancelar")
    public void cerrar() {
        w_lov_bloque.detach();

    }

@Listen("onClick =#btn_procesar")
    public void procesar() throws SQLException {
        buscarPeriodo();

        if (objlstPrincipal.isEmpty()) {
            Messagebox.show("No puede procesar, no hay periodo en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_mensaje = "Esta seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP,JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objPara;
                                  int i = 0;
                                for (int j = 0; j < objlstPrincipal.getSize(); j++) {
                                    if (objlstPrincipal.get(j).isValSelec()) {
                                        objlstPrincipal2 = objlstPrincipal;
                                    }

                                }
                       objPara = objDaoDescuentos.insertarBloqueDes(getDetalle(objlstPrincipal2));
                            if (objPara.getFlagIndicador() == 0) {
                                    
                                    limpiarLista();
 
                                    if (objPara.getMsgValidacion() == null) {
                                        Messagebox.show("No se realizó ninguna operación", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                    } else {
                                        Messagebox.show(objPara.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                                 
                                            public void onEvent(Event t) throws Exception {
                                                //if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                                    w_lov_bloque.detach();
                                                //}
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
        }

    }
	
	    public Object[][] getDetalle(ListModelList<Descuentos> x) {

       
        ListModelList<Descuentos> objLista;
      
        objLista = x;

        Object[][] listaDescuentos = new Object[objLista.size()][16];
        for (int j = 0; j < objLista.size(); j++) {
            if (objLista.get(j).isValSelec() == true) {
                listaDescuentos[j][0] = objUsuCredential.getCodemp();
                listaDescuentos[j][1] = objLista.get(j).getSucursal();
                listaDescuentos[j][2] = objLista.get(j).getTipo_doc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaDescuentos[j][3] = objLista.get(j).getDocumento();
                listaDescuentos[j][4] = objLista.get(j).getPeriodo();
                listaDescuentos[j][5] = objLista.get(j).getCod_constante();
                listaDescuentos[j][6] = new java.sql.Date(objLista.get(j).getFecha_mov().getTime());//objLista.get(j).getFecha_mov() == null ? null : new java.sql.Date(objLista.get(j).getFecha_mov().getTime());
                //listaDescuentos[j][7] = "PRESTAMO PERSONAL";
                 listaDescuentos[j][7] = objLista.get(j).getGlosa();
                listaDescuentos[j][8] = objLista.get(j).getCargo();
                listaDescuentos[j][9] = objLista.get(j).getAbono();
                listaDescuentos[j][10] = "";//objLista.get(j).getRecibo_egreso() == null ? "" : objLista.get(j).getRecibo_egreso();
                listaDescuentos[j][11] = "";//objLista.get(j).getRecibo_egreso_referencia() == null ? "" : objLista.get(j).getRecibo_egreso_referencia();
                listaDescuentos[j][12] = objUsuCredential.getCuenta();
                listaDescuentos[j][13] = "N";//objLista.get(j).getInd_accion();
                listaDescuentos[j][14] = 1;//objLista.get(j).getNumero_op();
                listaDescuentos[j][15] = 1;//tipo de ingreso 1 -> automatico
            }
        }

        return listaDescuentos;

    }
	
    @Listen("onClick=#btn_consultar")
    public void buscarRegistros() throws SQLException {
        limpiarLista();
        buscarPeriodo();
        String valida = verificar();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("constante")) {
                            txt_codcon.focus();
                        } else if (foco.equals("glosa")) {
                            txt_glosa.focus();
                        } else if (foco.equals("cargo")) {
                            cb_cargo.focus();
                        } else if (foco.equals("monto")) {
                            txt_monto.focus();
                        }

                    }
                }
            });

        } else {
            double monto = txt_monto.getValue();
            String idconstante = txt_codcon.getValue();
            Date fecha = d_fecha.getValue();
            String periodo = txt_periodo.getValue();
            String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
            String cargo = cb_cargo.getSelectedItem().getValue().toString();
            String glosa = txt_glosa.getValue();
            objlstPrincipal = objDaoDescuentos.buscaBloquedescuentos(sucursal, idconstante, area, periodo, monto, glosa, cargo,fecha);
            if (objlstPrincipal.isEmpty()) {
                Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {

                lst_bloque.setModel(objlstPrincipal);
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
        if (objlstPrincipal.isEmpty()) {
            Messagebox.show("No hay registros a mostrar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstPrincipal.getSize(); i++) {
                objlstPrincipal.get(i).setValSelec(chk_selecAll.isChecked());

            }
            lst_bloque.setModel(objlstPrincipal);
        }
    }
	
    public void limpiarLista() {
        objlstPrincipal = null;
        objlstPrincipal = new ListModelList<Descuentos>();
        lst_bloque.setModel(objlstPrincipal);

    }

    public void buscarPeriodo() throws SQLException {
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
    }
}