package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ControllerManPer extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listamanper, tab_mantenimiento;
    @Wire
    Listbox lst_manper;
    @Wire
    Combobox cb_busqueda;
    @Wire
    Spinner sp_tabord;
    @Wire
    Checkbox chk_tabest, chk_busest;
    @Wire
    Textbox txt_codigo, txt_periodo, txt_anio, txt_mes, txt_busqueda;
    @Wire
    Datebox txt_ultdia, txt_pridia;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    //Instancias de Objetos
    ListModelList<ManPer> objListModelListManPer = new ListModelList<ManPer>();
    DaoManPer objDaoManPer = new DaoManPer();
    ManPer objManPer = new ManPer();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje = "";
    int i_sel;
    int valor;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManPer.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_manper")
    public void llenaRegistros() throws SQLException {
        objListModelListManPer = objDaoManPer.listaPeriodos(0);
        lst_manper.setModel(objListModelListManPer);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40102000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Periodos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Periodos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a objUsuCredentialación de un nuevo Periodo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a objUsuCredentialación de un nuevo Periodo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Periodo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Periodo");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Periodo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Periodo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Periodos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Periodos");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        if (cb_busqueda.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una opcion de busqueda", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
            int i_seleccion = 0;
            int i_estado;
            if (chk_busest.isChecked()) {
                i_estado = 1;

            } else {
                i_estado = 2;
            }
            if (s_consulta.isEmpty()) {
                Messagebox.show("Por favor ingrese una opcion a buscar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objListModelListManPer = new ListModelList<ManPer>();
                if (cb_busqueda.getSelectedIndex() == 0) {
                    i_seleccion = 1;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 1) {
                    i_seleccion = 2;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el periodo " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 2) {
                    i_seleccion = 3;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el periodo " + s_consulta + " para su busqueda");
                }
                if (cb_busqueda.getSelectedIndex() == 3) {
                    i_seleccion = 4;
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el periodo " + s_consulta + " para su busqueda");
                }
                objListModelListManPer = objDaoManPer.busquedaPeriodo(i_seleccion, s_consulta, i_estado);
                lst_manper.setModel(objListModelListManPer);
            }
        }
    }

    @Listen("onSelect=#lst_manper")
    public void seleccionaRegistro() throws SQLException {
        objManPer = (ManPer) lst_manper.getSelectedItem().getValue();
        if (objManPer == null) {
            objManPer = objListModelListManPer.get(i_sel + 1);
        }
        i_sel = lst_manper.getSelectedIndex();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objManPer.getPer_id());
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objListModelListManPer = new ListModelList<ManPer>();
                                objManPer = generaRegistro();
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoManPer.insertar(objManPer);
                                } else {
                                    s_mensaje = objDaoManPer.actualizar(objManPer);
                                }
                                Messagebox.show(s_mensaje);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                objListModelListManPer = objDaoManPer.listaPeriodos(0);
                                lst_manper.setModel(objListModelListManPer);
                                objManPer = new ManPer();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_manper.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar este periodo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoManPer.eliminar(objManPer);
                                objListModelListManPer.remove(lst_manper.getSelectedIndex());
                                lst_manper.setModel(objListModelListManPer);
                                Messagebox.show(s_mensaje);
                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                //                                
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objManPer = new ManPer();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_tabest.setDisabled(true);
        chk_tabest.setChecked(true);
        txt_periodo.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para objUsuCredentialar un registro");
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seeguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_manper.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_manper.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objListModelListManPer = new ListModelList<ManPer>();
            objListModelListManPer = objDaoManPer.listaPeriodos(0);
            lst_manper.setModel(objListModelListManPer);
        }
    }

    //Eventos Otros
    public void llenarCampos() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txt_codigo.setValue(String.valueOf(objManPer.getPer_id()));
            txt_periodo.setValue(objManPer.getPer_periodo());
            txt_anio.setValue(objManPer.getAnio());
            txt_mes.setValue(objManPer.getMes());
            txt_pridia.setValue(sdf.parse(objManPer.getFecini()));
            txt_ultdia.setValue(sdf.parse(objManPer.getFecfin()));
            if (objManPer.getPer_estado() == 1) {
                chk_tabest.setChecked(true);
            } else {
                chk_tabest.setChecked(false);
            }
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ControllerManPer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamanper.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listamanper.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_codigo.setValue("");
        txt_periodo.setValue("");
        txt_anio.setValue("");
        txt_mes.setValue("");
        txt_pridia.setValue(null);
        txt_ultdia.setValue(null);
        sp_tabord.setValue(0);
    }

    public void habilitaCampos(boolean b_valida) {
        chk_tabest.setDisabled(b_valida);
        txt_pridia.setDisabled(b_valida);
        txt_ultdia.setDisabled(b_valida);
    }

    public String verificar() {
        String s_valor;
        if (txt_pridia.getValue() == null) {
            s_valor = "Fecha Inicial";
            txt_pridia.focus();
        } else if (txt_ultdia.getValue() == null) {
            s_valor = "Fecha Final";
            txt_ultdia.focus();
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    @SuppressWarnings("deprecation")
    public ManPer generaRegistro() {
        String c_per_id;
        int n_anio;
        int n_mes;
        String c_periodo;
        int i_per_estado;
        String c_anio;
        String c_mes;
        Date d_fecini;
        Date d_fecfin;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (s_estado.equals("N")) {
            c_per_id = "";
            n_anio = txt_pridia.getValue().getYear();
            n_mes = txt_pridia.getValue().getMonth();
            c_periodo = n_anio + "" + n_mes;
            i_per_estado = (chk_tabest.isChecked() == true) ? 1 : 2;
            c_anio = "" + n_anio;
            c_mes = "" + n_mes;
            d_fecini = txt_pridia.getValue();
            d_fecfin = txt_ultdia.getValue();
        } else {
            c_per_id = txt_codigo.getValue();
            c_anio = txt_anio.getValue();
            c_mes = txt_mes.getValue();
            c_periodo = c_anio + "" + c_mes;
            i_per_estado = (chk_tabest.isChecked() == true) ? 1 : 2;
            d_fecini = txt_pridia.getValue();
            d_fecfin = txt_ultdia.getValue();
        }
        return new ManPer(c_per_id, i_per_estado, c_anio, c_mes, sdf.format(d_fecini), sdf.format(d_fecfin), "", null, "", null);
    }

    //metodos sin utilizar
    public void botonImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
