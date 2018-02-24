/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.ControllerUbicaciones;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ConfPlanilla;
import org.me.gj.model.planillas.procesos.Movlinea;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerConfPlanilla extends SelectorComposer<Component> {

    @Wire
    Textbox txt_busqueda, txt_usua, txt_usumo, txt_codigo, txt_constante, txt_descon;
    @Wire
    Button btn_buscar;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Listbox lst_lista;
    @Wire
    Combobox cb_busqueda;
    @Wire
    Radiogroup rbg_indicador_principal;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUbicaciones.class);
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    ConfPlanilla objConf;
    ListModelList<ConfPlanilla> objlstConf = new ListModelList<ConfPlanilla>();
    DaoConfPlanilla objDaoConf = new DaoConfPlanilla();
    String s_estado, campo, s_mensaje, estado = "Q";
    int i_sel;
    public static boolean bandera = false;
    DaoMovLinea objDaoMovLinea;
    Movlinea objMovLinea;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        objDaoMovLinea = new DaoMovLinea();
        habilitaTab(false, true);
        rbg_indicador_principal.setSelectedIndex(0);
        consultaPrincipal();
    }

     @Listen("onCreate=#tabbox_conf")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101140, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Configuracion Planilla con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Configuracion Planilla con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Configuracion Planilla");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Configuracion Planilla");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Configuracion Planilla");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Configuracion Planilla");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Configuracion Planilla");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Configuracion Planilla");
        }
    }
	
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        desabilita(false);
        limpiaAuditoria();
        txt_codigo.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    //evento enter para ver contante a elegir
    @Listen("onOK=#txt_constante")
    public void lovConstantePrincipal() {
        if (bandera == false) {
            bandera = true;
            String tipo = "";
            if (rbg_indicador_principal.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indicador_principal.getSelectedIndex() == 1) {
                tipo = "M";
            } else if (rbg_indicador_principal.getSelectedIndex() == 2) {
                tipo = "F";
            }

            if (txt_constante.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_constante);
                objMapObjetos.put("descripcion", txt_descon);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerConfPlanilla");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstante.zul", null, objMapObjetos);
                window.doModal();

            } /*else {
             cb_area.focus();
             }*/

        }
    }

    /*
     * Evento al seleccionar la lista principal te carga los dal seleccionado
     *
     * @version 11/12/2017
     */
    @Listen("onSelect=#lst_lista")
    public void seleccionaPrincipal() throws SQLException {
        limpiarCampos();
        objConf = (ConfPlanilla) lst_lista.getSelectedItem().getValue();
        if (objConf == null) {
            objConf = objlstConf.get(i_sel + 1);
        }
        i_sel = lst_lista.getSelectedIndex();
        habilitaTab(false, false);
        completDatos(objConf);
        completAuditoria();
        colocarRadio();
    }

    @Listen("onBlur=#txt_constante")
    public void validaConstantePrincipal() throws SQLException {
        if (!estado.equals("Q")) {
            if (!txt_constante.getValue().isEmpty()) {
                String consulta = txt_constante.getValue();
                String tipo = "";
                if (rbg_indicador_principal.getSelectedIndex() == 0) {
                    tipo = "C";
                } else if (rbg_indicador_principal.getSelectedIndex() == 1) {
                    tipo = "M";
                } else if (rbg_indicador_principal.getSelectedIndex() == 2) {
                    tipo = "F";
                }
                objMovLinea = objDaoMovLinea.consultaContante(consulta, tipo);
                if (objMovLinea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_constante.setValue("");
                            txt_constante.focus();
                        }
                    });
                } else {

                    txt_constante.setValue(objMovLinea.getId_concepto());
                    txt_descon.setValue(objMovLinea.getDescripcion());

                }
            } else {
                txt_descon.setValue("");
            }
            bandera = false;
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        int existe = 0;
        String valida = verificar();
        if (!valida.equals("")) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("codigo")) {
                            txt_codigo.focus();
                        } else if (campo.equals("constante")) {
                            txt_constante.focus();
                        }
                    }
                }
            });
        } else {
            if (estado.equals("N")) {
                existe = objDaoConf.validaConstante(txt_constante.getValue(), txt_codigo.getValue());
            }

            if (existe == 0) {

                s_mensaje = " Esta seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event t) throws Exception {
                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                    objConf = (ConfPlanilla) generaRegistro();
                                    if (estado.equals("N")) {
                                        s_mensaje = objDaoConf.insertar(objConf);

                                    } else {
                                        s_mensaje = objDaoConf.actualizar(objConf);
                                    }
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaBotones(false, true);
                                    habilitaCampos(true, true);
                                    limpiaAuditoria();
                                    limpiarCampos();
                                    consultaPrincipal();
                                }
                            }
                        });
            } else {
                Messagebox.show("Constante ya se encuentra registrada", "CODIJISA", 1, "z-msgbox z-msgbox-information");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            estado = "M";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false, true);

            //txt_constante.focus();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar el este registro?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", 3, "z-msgbox z-msgbox-question", new EventListener<Event>() {

                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == 1) {
                        s_mensaje = objDaoConf.eliminar(objConf);

                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        limpiaAuditoria();
                        limpiarCampos();
                        consultaPrincipal();
                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion eliminar  un registro");
                    }
                }
            });
        }

    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            lst_lista.setSelectedIndex(-1);
                            seleccionaTab(true, false);
                            VerificarTransacciones();
                            habilitaBotones(false, true);
                            habilitaTab(false, true);
                            desabilita(true);
                            estado = "Q";
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#btn_buscar")
    public void botonBuscar() throws SQLException {
        String codigo = txt_busqueda.getValue();
        int selec = 0;
        if (cb_busqueda.getSelectedIndex() == 0) {
            selec = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            selec = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + codigo + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            selec = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + codigo + " para su busqueda");
        }

        objlstConf = objDaoConf.consultar(codigo, selec);
        if (objlstConf.isEmpty()) {
            objlstConf = null;
            lst_lista.setModel(objlstConf);;
            Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");

        } else {
            lst_lista.setModel(objlstConf);
        }
    }

    public ConfPlanilla generaRegistro() {
        int nro = 0;
        String cod = txt_codigo.getValue();
        String consta = txt_constante.getValue();
        if (estado.equals("N")) {
            nro = 0;
        } else {
            nro = objConf.getNroope();
        }

        return new ConfPlanilla(cod, consta, nro);
    }

    //metodos sin utilizar
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void desabilita(boolean b_valida1) {
        txt_codigo.setDisabled(b_valida1);
        txt_constante.setDisabled(b_valida1);
    }

    public String verificar() {
        String valor;
        if (txt_codigo.getValue().isEmpty()) {
            valor = "Por favor ingresar un codigo";
            campo = "codigo";
        } else if (txt_constante.getValue().isEmpty()) {
            valor = "Por favor ingresar una constante";
            campo = "constante";
        } else {
            valor = "";
        }

        return valor;
    }

    public void limpiarCampos() {
        txt_codigo.setValue("");
        txt_constante.setValue("");
        txt_descon.setValue("");
    }

    public void completAuditoria() {
        txt_usua.setValue(objConf.getUsu_add());
        txt_usumo.setValue(objConf.getUsu_mod());
        d_fec.setValue(objConf.getFecha_add());
        d_fecmo.setValue(objConf.getFecha_mod());
    }

    public void completDatos(ConfPlanilla c) {
        txt_codigo.setValue(c.getCodigo());
        txt_constante.setValue(c.getConstante());
        txt_descon.setValue(c.getDes_const());
    }

    public void habilitaCampos(boolean t, boolean f) {
        txt_codigo.setDisabled(f);
        txt_constante.setDisabled(t);
    }

    public void consultaPrincipal() throws SQLException {
        objlstConf = objDaoConf.consultar("", 0);
        lst_lista.setModel(objlstConf);
    }

    public void colocarRadio() {
        if (objConf.getTipo().equals("C")) {
            rbg_indicador_principal.setSelectedIndex(0);
        } else if (objConf.getTipo().equals("M")) {
            rbg_indicador_principal.setSelectedIndex(1);
        } else if (objConf.getTipo().equals("F")) {
            rbg_indicador_principal.setSelectedIndex(2);
        }
    }
}
