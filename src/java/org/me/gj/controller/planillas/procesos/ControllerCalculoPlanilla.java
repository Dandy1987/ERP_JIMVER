package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.informes.DaoMovimiento;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.informes.InformesMovimiento;
import org.me.gj.model.planillas.mantenimiento.Personal;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author wcamacho
 */
public class ControllerCalculoPlanilla extends SelectorComposer<Component> {

    @Wire
    private Textbox txt_periodo, txt_codigo, txt_codigo1, txt_apenom;
    @Wire
    Checkbox chk_marca;
    @Wire
    private Combobox cb_sucursal;
    @Wire
    private Button btn_calcular;

    DaoMovLinea objDaoMovLinea;
    Personal objPersonal;
    private DaoAccesos objDaoAccesos;
    private DaoPlanilla objDaoCalcularPlanilla;
    private DaoPerPago objDaoPerPago;
    private DaoPlanilla objDaoPlanilla;
    ParametrosSalida objParametrosSalida;
    private ListModelList<Sucursales> objListSucursales;
    ListModelList<InformesMovimiento> objlstmovimiento;
    DaoMovimiento objDaoMovimiento;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovLinea.class);
    public static boolean bandera = false;
    ParametrosSalida objPaSalida;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoMovLinea = new DaoMovLinea();
        objDaoCalcularPlanilla = new DaoPlanilla();
        objDaoPerPago = new DaoPerPago();
        objDaoMovimiento = new DaoMovimiento();
        objlstmovimiento = null;
        objlstmovimiento = new ListModelList<InformesMovimiento>();
        //String periodo = objDaoMovLinea.setearPeriodo();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo.equals("") ? "--------" : periodo);
        objDaoAccesos = new DaoAccesos();
        objListSucursales = new ListModelList<Sucursales>();
        objListSucursales = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        // objlstSucursal = objDaoPersonal.lstSucursales(emp_id); //se comento
        cb_sucursal.setModel(objListSucursales);
    }

    @Listen("onClick=#btn_calcular")
    public void botonCalcularPlanilla() {
        try {
            //Capturar datos ingresados
            final String periodo_proceso = txt_periodo.getValue();
            String sucursal = cb_sucursal.getSelectedItem().getValue().toString();
            final String cod_sucursal = sucursal.equals("0") ? "" : sucursal;
            objlstmovimiento = objDaoMovimiento.verDataTplplames();
            if (periodo_proceso.equals("") /*|| periodo_proceso == null*/ || periodo_proceso.equals("--------")) {
                Messagebox.show("No hay ningun periodo en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {
                if (!objlstmovimiento.isEmpty() && !txt_periodo.getValue().equals("--------")) {
                    Messagebox.show("Ya existe calculo,desea eliminar y volver a generar", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                            new EventListener() {
                                @Override
                                public void onEvent(Event t) throws Exception {
                                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                        //elimina
                                        objDaoPlanilla = new DaoPlanilla();
                                        objParametrosSalida = objDaoPlanilla.eliminar(objUsuCredential.getCodemp(), periodo_proceso);
                                        //calcula
                                        objDaoCalcularPlanilla = new DaoPlanilla();
                                        ParametrosSalida objParametrosSalida;
                                        String persona = txt_codigo1.getValue();
                                        String s_aux = persona.replace("'", "");
                                        s_aux = persona.replace(")", "");
                                        //si va a reliazar por personal o todo
                                        if (chk_marca.isChecked()) {
                                            String valida = verificar();
                                            if (!valida.isEmpty()) {
                                                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                                    @Override
                                                    public void onEvent(Event event) throws Exception {
                                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                            txt_codigo.focus();
                                                        }
                                                    }
                                                });
                                            } else {
                                                objPaSalida = objDaoCalcularPlanilla.calcularPorPersonal(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso, s_aux);
                                                Messagebox.show(objPaSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);                                              
                                            }
                                        } else {
											String codigo = "";
                                            codigo =objDaoCalcularPlanilla.robotBuscarSueldo(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso);
                                            if (codigo == null) {
                                                objParametrosSalida = objDaoCalcularPlanilla.calcular(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso);
                                                Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            } else {
                                                Messagebox.show("El codigo : " + codigo + " no tiene sueldo basico", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                            //limpiar();
                                        }

                                    }
                                }
                            });

                } else {

                    if (chk_marca.isChecked()) {
                        String valida = verificar();
                        if (!valida.isEmpty()) {
                            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_codigo.focus();
                                    }
                                }
                            });
                        } else {
                            Messagebox.show("Esta seguro de calcular la planilla", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new EventListener<Event>() {

                                        public void onEvent(Event t) throws Exception {
                                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                                String persona = txt_codigo1.getValue();
                                                String s_aux = persona.replace("'", "");
                                                s_aux = persona.replace(")", "");
                                                objPaSalida = objDaoCalcularPlanilla.calcularPorPersonal(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso, s_aux);
                                                Messagebox.show(objPaSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                // limpiar();
                                            }
                                        }
                                    });

                        }
                    } else {
                        // objPaSalida = objDaoCalcularPlanilla.calcular(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso);
                        //Messagebox.show(objPaSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        String codigo = objDaoCalcularPlanilla.robotBuscarSueldo(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso);
                        if (codigo != null /*|| codigo.equals("") || codigo.isEmpty() ||*/) {
                            Messagebox.show("El codigo : " + codigo + " no tiene sueldo basico", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

                        } else {
                            objParametrosSalida = objDaoCalcularPlanilla.calcular(objUsuCredential.getCodemp(), cod_sucursal, periodo_proceso);
                            Messagebox.show(objParametrosSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                        // limpiar();
                    }

                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ControllerCalculoPlanilla.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo muestra lov de personal
     *
     * @version 03/01/2018
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_codigo")
    public void buscarPersonalPrincipal() {

        if (bandera == false) {
            bandera = true;
            if (txt_codigo.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_codigo);
                objMapObjetos.put("des_per", txt_apenom);
                objMapObjetos.put("cod", txt_codigo1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovLinea");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    //Salir de lov para el filtro
    @Listen("onBlur=#txt_codigo")
    public void valida_PersonalPrincipal() throws SQLException {
        if (!txt_codigo.getValue().isEmpty()) {
            if (!txt_codigo.getValue().equals("ALL")) {
                String id = txt_codigo.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_codigo.setValue(null);
                            txt_codigo.focus();
                            txt_apenom.setValue("");

                        }
                    });

                } else {
                    txt_codigo.setValue(objPersonal.getPlidper());
                    txt_apenom.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                    txt_codigo1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_apenom.setValue("");
            txt_codigo1.setValue("");
        }

        bandera = false;
    }

    @Listen("onClick=#chk_marca")
    public void check_valida() {

        if (chk_marca.isChecked()) {
            txt_codigo.setDisabled(false);
        } else {
            txt_codigo.setDisabled(true);
        }

    }

    public void limpiar() throws SQLException {
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo.equals("") ? "--------" : periodo);
        txt_codigo.setValue("");
        txt_apenom.setValue("");
        txt_codigo1.setValue("");
        txt_codigo.setDisabled(false);
        chk_marca.setChecked(false);
    }

    public String verificar() {
        String valor = "";
        if (txt_codigo.getValue().isEmpty()) {
            valor = "Debe ingresar codigo de personal";

        }
        return valor;
    }
}
