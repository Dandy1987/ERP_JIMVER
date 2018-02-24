package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class ControllerRevCierre extends SelectorComposer<Component> {

    @Wire
    Combobox cb_periodo;
    @Wire
    Button btn_calcular;

    //Listas
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();

    //Objetos
    PerPago objPerPago;
    DaoPerPago objdaoPerPago;
    DaoCierres objDaoCierres;

    //Variables de Sesión
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRevCierre.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objdaoPerPago = new DaoPerPago();
        objlstPerPago = objdaoPerPago.busquedaPerRevCierre();
        cb_periodo.setModel(objlstPerPago);
    }

    @Listen("onClick=#btn_calcular")
    public void revierteCierre() throws SQLException {
        if (cb_periodo.getSelectedIndex() == 0 || cb_periodo.getSelectedIndex() == -1) {
            Messagebox.show("Seleccione el periodo que desea revertir.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            
            objPerPago = new PerPago();
            objPerPago = objdaoPerPago.descPerPago(cb_periodo.getSelectedItem().getValue().toString());
            if (objPerPago == null || objPerPago.getPeriodoProceso().toString().equals("")) {
                Messagebox.show("¡Tipo de periodo de pago no válido!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {
                String msj = "¿Está seguro(a) que desea revetir el cierre de periodo de pago " + objPerPago.getPeriodoProceso().toString() + " - " + objPerPago.getPerPag_desc().toString() + "?";
                try {
                    Messagebox.show(msj, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener<Event>() {
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objDaoCierres = new DaoCierres();
                                int flag = objDaoCierres.revierteCierre(objUsuCredential.getCodemp(), objPerPago.getPeriodoProceso().toString());
                                ParametrosSalida objPara = objDaoCierres.activaAutomatico( objPerPago.getPeriodoProceso().toString());
                                if (flag == 1) {
                                    String msj2 = "El Revierte Cierre de Planilla Periodo: " + objPerPago.getPeriodoProceso() + " - " + objPerPago.getPerPag_desc() + ".\n" + "¡Se terminó con éxito!";
                                    Messagebox.show(msj2, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    objlstPerPago = objdaoPerPago.busquedaPerRevCierre();
                                    cb_periodo.setModel(objlstPerPago);
                                    cb_periodo.setValue("");
                                } else {
                                    String msj2 = "El Revierte Cierre de Planilla Periodo: " + objPerPago.getPeriodoProceso() + " - " + objPerPago.getPerPag_desc() + ".\n" + "¡No se terminó con exito, revise la información!";
                                    Messagebox.show(msj2, "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                                    objlstPerPago = objdaoPerPago.busquedaPerRevCierre();
                                    cb_periodo.setModel(objlstPerPago);
                                    cb_periodo.setValue("");
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Messagebox.show(e.toString());
                }
            }
        }
    }
}