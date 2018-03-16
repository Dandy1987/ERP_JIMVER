/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.Date;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.DaoDescuentos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.procesos.Descuentos;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovDescuentosFaltantes extends SelectorComposer<Component> {

    @Wire
    Combobox cb_fsucursal, cb_personal;
    @Wire
    Checkbox chk_rango;
    @Wire
    Datebox d_fecha, d_inicio, d_fin;
    @Wire
    Textbox txt_periodo;
    @Wire
	Listbox lst_lista;
    @Wire
    Button btn_consultar, btn_cancelar, btn_procesar;
    @Wire
    Window w_lov_faltantes;
	@Wire
    Checkbox chk_selecAll;
    String foco;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
	ListModelList<Descuentos> objlstDescuentos, objLstDescuentosSelec;
    DaoAccesos objDaoAccesos;
    DaoPerPago objDaoPerPago;
	DaoDescuentos objdaoDescuentos;
    Boolean b_validar = true;
    Session sesion = Sessions.getCurrent();
	
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoAccesos = new DaoAccesos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        objDaoPerPago = new DaoPerPago();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
		objdaoDescuentos = new DaoDescuentos();
        objlstDescuentos = new ListModelList<Descuentos>();
        objLstDescuentosSelec = new ListModelList<Descuentos>();
        objlstDescuentos = objdaoDescuentos.listaDescuentosFaltante(0, periodo, "TODOS", null, null);
        lst_lista.setModel(objlstDescuentos);
        cb_personal.setSelectedIndex(0);

    }

    @Listen("onClick=#chk_rango")
    public void check() {
        boolean check = chk_rango.isChecked();
        if (check == true) {
            habilitarFecha(false);

        } else {
            habilitarFecha(true);
        }

    }

    public void habilitarFecha(boolean t) {
        d_inicio.setDisabled(t);
        d_fin.setDisabled(t);
    }

    public String verificaFecha() {
        String valor = "";
        if (d_fin.getValue().before(d_inicio.getValue())) {
            valor = "La fecha final debe ser mayor a la de inicio";
            foco = "fecha";

        }
        return valor;
    }

    @Listen("onClick=#btn_cancelar")
    public void cerrar() {
        w_lov_faltantes.detach();
		
    }

	
    @Listen("onClick=#btn_procesar")
    public void insertarFaltante() throws SQLException {
        if (objlstDescuentos.isEmpty()) {
            Messagebox.show("No hay datos a procesar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

            Messagebox.show("Esta seguro que desea guardar los cambios?", "ERP_JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                if (objdaoDescuentos.validaPeriodoCalculando(txt_periodo.getValue().toString()) == 1) {
                                    Messagebox.show("La planilla se encuentra calculando", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    int i_valida = objdaoDescuentos.validaPeriodoProceso(txt_periodo.getValue().toString());
                                    if (i_valida == 1) {
                                        ParametrosSalida objParam;
                                        for (int i = 0; i < objlstDescuentos.getSize(); i++) {

                                            if (objlstDescuentos.get(i).isValSelec()) {

                                                objLstDescuentosSelec.add(objlstDescuentos.get(i));

                                            }
                                        }
                                        int i_sucu = cb_fsucursal.getSelectedItem().getValue();
                                        if (i_sucu == 0) {
                                            i_sucu = 1;
                                        }
                                        for (int i = 0; i < objLstDescuentosSelec.getSize(); i++) {
                                            objParam = objdaoDescuentos.insertarFaltante(i_sucu, objLstDescuentosSelec.get(i), txt_periodo.getValue(), d_fecha.getValue());
                                            if (objParam.getFlagIndicador() == 0) {

                                                if (objParam.getMsgValidacion() == null) {
                                                    b_validar = false;
                                                    break;
                                                } else {
                                                    b_validar = true;
                                                }
                                            } else {
                                                b_validar = false;
                                                break;

                                            }
                                        }
                                        if (b_validar == true) {
                                            Messagebox.show("Se realizó con exito", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            w_lov_faltantes.detach();
                                        } else {
                                            Messagebox.show("Hubo un problema al guardar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        }
                                    } else {
                                        Messagebox.show("El periodo no se encuentra en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }

                        }
                    }
            );
        }
    }
	
    @Listen("onClick=#btn_consultar")
    public void cargarConsultar() throws SQLException {
        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<Descuentos>();
        int sucu;
        Date dini;
        Date dfin;
        String tipoPersonal;
        if (cb_personal.getSelectedIndex() == 0) {
            tipoPersonal = "TODOS";
        } else {
            tipoPersonal = cb_personal.getValue();
        }

        if (cb_fsucursal.getSelectedItem() == null) {
            sucu = 0;
        } else {
            sucu = cb_fsucursal.getSelectedItem().getValue();
        }
        if (chk_rango.isChecked()) {
            dini = d_inicio.getValue();
            dfin = d_fin.getValue();
        } else {
            dini = null;
            dfin = null;
        }
        objlstDescuentos = objdaoDescuentos.listaDescuentosFaltante(sucu, txt_periodo.getValue(), tipoPersonal, dini, dfin);
        lst_lista.setModel(objlstDescuentos);

    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstDescuentos.isEmpty()) {
            Messagebox.show("No hay registros a mostrar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstDescuentos.getSize(); i++) {
                objlstDescuentos.get(i).setValSelec(chk_selecAll.isChecked());
            }
            lst_lista.setModel(objlstDescuentos);
        }
    }

    @Listen("onSeleccion=#lst_lista")
    public void seleccionaRegistro(ForwardEvent evt
    ) {
        objlstDescuentos = (ListModelList) lst_lista.getModel();
        if (!objlstDescuentos.isEmpty() || objlstDescuentos != null) {
            Checkbox chk = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk.getParent().getParent();
            objlstDescuentos.get(item.getIndex()).setValSelec(chk.isChecked());
            lst_lista.setModel(objlstDescuentos);

        }
    }
	
}
