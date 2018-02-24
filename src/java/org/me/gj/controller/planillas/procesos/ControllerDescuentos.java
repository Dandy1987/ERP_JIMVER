
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Descuentos;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerDescuentos extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoc, tbbtn_btn_deshacerc, tbbtn_btn_eliminarc,
            tbbtn_btn_guardarc, tbbtn_btn_editarc;
    @Wire
    Listbox lst_detalle, lst_principal;
    @Wire
    Combobox cb_fsucursal, cb_area;
    @Wire
    Textbox txt_idpersonal, txt_despersonal, txt_periodo, txt_glosa,
            txt_mcodconstante, txt_mdesconstante, txt_periodo2,
            txt_codcon, txt_descon, txt_codigo, txt_codigo1, txt_apenom, txt_usuadd, txt_usumod;
    @Wire
    Checkbox chk_negativo;
    @Wire
    Button btn_consultar, btn_bloque, btn_faltantes, btn_prestamos, btn_reint;
    @Wire
    Datebox d_fecing, d_fecces, d_feccons, d_fecmod, d_fecadd;
    @Wire
    Doublebox d_cargo, d_abono, txt_total, txt_total_lista;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();

    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();

    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoPersonal objDaoPersonal = new DaoPersonal();
    Personal objPersonal;
	Accesos objAccesos;
    Descuentos objDescuentos, objPrincipal;
    ListModelList<Descuentos> objlstDescuentos;
    ListModelList<Descuentos> objlstPrincipal;
    ListModelList<Descuentos> objlstEliminar;
    DaoPerPago objDaoPerPago;
    DaoMovLinea objDaoMovLinea;
    DaoDescuentos objDaoDescuentos;
    public static boolean bandera = false;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerDescuentos.class);
    String foco, s_estado = "Q", campo, s_mensaje, estado = "Q";//s_estado = detalle && estado = general
    int i_sel;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<Descuentos>();
        objlstEliminar = null;
        objlstEliminar = new ListModelList<Descuentos>();
        objlstPrincipal = null;
        objlstPrincipal = new ListModelList<Descuentos>();

        objDaoMovLinea = new DaoMovLinea();
        objDaoDescuentos = new DaoDescuentos();
        //se completa combobox de sucursales
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        //periodo
        objDaoPerPago = new DaoPerPago();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo2.setValue(periodo);
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
        habilitaTab(false, true);
        objlstPrincipal = objDaoDescuentos.consultar(periodo);
        lst_principal.setModel(objlstPrincipal);
        sumaLista();

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90202000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Descuentos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Descuentos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Descuentos");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Descuentos");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Descuentos");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Descuentos");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Descuentos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Descuentos");
        }
		if (ControllerPersonal.val == 1) {
            botonNuevo();
        }
    }
	
    //Eventos Otros 
    @Listen("onCtrlKey=#w_descuentos")
    public void ctrl_teclado(Event event) throws SQLException, ParseException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo();
                }
                break;
            case 115:
                if (!tbbtn_btn_editar.isDisabled()) {
                    botonEditar();
                }
                break;
            case 118:
                if (!tbbtn_btn_eliminar.isDisabled()) {
                    //botonEliminar();
                }
                break;
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    guardar();
                }
                break;
            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    //  botonImprimir();
                }
                break;
        }
    }

    /**
     * Metodo consultar personal x filtros
     *
     * @throws java.sql.SQLException
     */
    @Listen("onClick=#btn_consultar")
    public void buscarRegistro() throws SQLException {
        limpiarListaPrincipal();
        boolean check = chk_negativo.isChecked();
        String periodo = txt_periodo.getValue();
        String idconstante = txt_codcon.getValue();
        //String idcodigo = txt_codigo.getValue();
        String idcodigo = txt_codigo1.getValue();
        String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
        String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
        objlstPrincipal = objDaoDescuentos.buscarRegistro(sucursal, idcodigo, idconstante, area, periodo, check);

        if (objlstPrincipal.isEmpty()) {
            Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            lst_principal.setModel(objlstPrincipal);
        }
        sumaLista();
        chk_negativo.setChecked(false);

    }

    /**
     * Metodo al seleccionar lsita principal
     *
     * @throws java.sql.SQLException
     */
    @Listen("onSelect=#lst_principal")
    public void seleccionaListaP() throws SQLException {
        limpiarCamposDetalle();
        objPrincipal = (Descuentos) lst_principal.getSelectedItem().getValue();
        if (objPrincipal == null) {
            objPrincipal = objlstPrincipal.get(i_sel + 1);
        }
        i_sel = lst_principal.getSelectedIndex();
        habilitaTab(false, false);
        llenaListaDetalle();
        sumaTotal();
        habilitaBotonesDetalle(true, true);
        txt_idpersonal.setDisabled(true);

    }

    @Listen("onSelect=#lst_detalle")
    public void seleccionaDetalle() throws SQLException {
        objDescuentos = (Descuentos) lst_detalle.getSelectedItem().getValue();
        if (objDescuentos == null) {
            objDescuentos = objlstDescuentos.get(i_sel + 1);
        }
        i_sel = lst_detalle.getSelectedIndex();
        llenarCamposDetalle(objDescuentos);
        llenaAuditoria();

    }

    /**
     * Metodo para crear un nuevo registro
     *
     * @version 16/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        estado = "N";
        //if (txt_periodo.getValue()!=null || !txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equalsIgnoreCase("")) {
        if (!txt_periodo2.getValue().isEmpty() && !txt_periodo2.getValue().equals("--------")) {// if (!txt_periodo.getValue().isEmpty()) {
            String periodo = txt_periodo2.getValue();
            txt_periodo.setValue(periodo);
            habilitaBotones(true, false);
            habilitaBotonesDetalle(false, true);
            seleccionaTab(false, true);
            habilitaTab(true, false);
            txt_idpersonal.focus();
            txt_idpersonal.setDisabled(false);
            limpiarListaDetalle();
            limpiaAuditoria();
            limpiaCabeceradetalle();
            limpiarCamposDetalle();
            /* limpiaAuditoria();
             limpiaConstante();
             limpiaConstanteMensual();
             limpiarCamposGuardar();
             limpiarListas();*/
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
        } else {
            Messagebox.show("No hay periodo en proceso no puede continuar con la operación", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                            }
                        }
                    });
        }

    }
    /*
     *Metodo para editar la tabla tpldatosfv
     */

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (!txt_periodo2.getValue().isEmpty() && !txt_periodo2.getValue().equals("--------")) {
            txt_idpersonal.setDisabled(true);
            if (lst_principal.getSelectedIndex() == -1) {
                Messagebox.show("Por favor seleccionar un registro", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                }
                            }
                        });

            } else {
                estado = "M";
                habilitaBotones(true, false);
                seleccionaTab(false, true);
                habilitaTab(true, false);
                habilitaBotonesDetalle(false, true);
                objlstEliminar = null;
                objlstEliminar = new ListModelList<Descuentos>();
            }
        } else {
            Messagebox.show("No hay periodo en proceso no puede continuar con la operación", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override

                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                            }
                        }
                    });
        }

    }

    /**
     * Metodo deshacer para no guardar cambios
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            // VerificarTransacciones();
                            habilitaBotones(false, true);
                            limpiaAuditoria();
                            habilitaBotonesDetalle(false, true);
                            limpiaCabeceradetalle();
                            txt_periodo.setValue("");
                            habilitarCamposDetalle(true);
                            txt_idpersonal.setDisabled(true);
                            lst_principal.clearSelection();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                            estado = "Q";
                        }
                    }
                });
    }

    /**
     * Metodo para guardar a base de datos
     *
     * @version 16/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_guardar")
    public void guardar() {
        String valida = verifica();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                    }
                }
            });
        } else {
            if (!s_estado.equals("M")) {
                s_mensaje = "Esta seguro que desea guardar?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener<Event>() {

                            public void onEvent(Event t) throws Exception {
                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objSalida;
                                    objlstDescuentos.addAll(objlstEliminar);
                                    if (estado.equals("N")) {
                                        objSalida = objDaoDescuentos.insertar(getDetalle(objlstDescuentos));

                                    } else {
                                        objSalida = objDaoDescuentos.modificar(getDetalle(objlstDescuentos));
                                    }
                                    if (objSalida.getFlagIndicador() == 0) {
                                        limpiaAuditoria();
                                        limpiarCamposDetalle();
                                        habilitaTab(false, true);
                                        seleccionaTab(true, false);
                                        habilitaBotones(false, true);
                                        habilitaBotonesDetalle(true, true);
                                        objlstPrincipal = objDaoDescuentos.consultar(txt_periodo2.getValue());
                                        lst_principal.setModel(objlstPrincipal);
                                        objlstEliminar.clear();
                                    }
                                    if (objSalida.getMsgValidacion() == null) {
                                        Messagebox.show("No se realizo ninguna operacion", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                    } else {
                                        Messagebox.show(objSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                            @Override
                                            public void onEvent(Event event) throws Exception {
                                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                    //txt_zid_codigo.focus();
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        });
            }

        }
    }

    public Object[][] getDetalle(ListModelList<Descuentos> x) {
        ListModelList<Descuentos> objLista;
        if (s_estado.equals("M")) {
            objLista = new ListModelList<Descuentos>();
            for (int h = 0; h < x.size(); h++) {
                if (!x.get(h).getInd_accion().equals("Q")) {
                    objLista.add(x.get(h));
                }
            }
        } else {
            objLista = x;
        }
        Object[][] listaDescuentos = new Object[objLista.size()][16];
        for (int j = 0; j < objLista.size(); j++) {
            listaDescuentos[j][0] = objUsuCredential.getCodemp();
            if (objPersonal != null) {
                listaDescuentos[j][1] = objPersonal.getSuc_id();
                listaDescuentos[j][2] = objPersonal.getPltipdoc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaDescuentos[j][3] = objPersonal.getPlnrodoc();
            } else {
                listaDescuentos[j][1] = objPrincipal.getSucursal();
                listaDescuentos[j][2] = objPrincipal.getTipo_doc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaDescuentos[j][3] = objPrincipal.getDocumento();
            }
            listaDescuentos[j][4] = txt_periodo.getValue();
            listaDescuentos[j][5] = objLista.get(j).getCod_constante();
            listaDescuentos[j][6] = objLista.get(j).getFecha_mov() == null ? null : new java.sql.Date(objLista.get(j).getFecha_mov().getTime());
            listaDescuentos[j][7] = objLista.get(j).getGlosa();
            listaDescuentos[j][8] = objLista.get(j).getCargo();
            listaDescuentos[j][9] = objLista.get(j).getAbono();
            listaDescuentos[j][10] = objLista.get(j).getRecibo_egreso() == null ? "" : objLista.get(j).getRecibo_egreso();
            listaDescuentos[j][11] = objLista.get(j).getRecibo_egreso_referencia() == null ? "" : objLista.get(j).getRecibo_egreso_referencia();
            listaDescuentos[j][12] = objUsuCredential.getCuenta();
            listaDescuentos[j][13] = objLista.get(j).getInd_accion();
            listaDescuentos[j][14] = objLista.get(j).getNumero_op();
            listaDescuentos[j][15] = 0;//tipo de ingreso 0 -> manual

        }

        return listaDescuentos;

    }

    //para los lov de bloques
    //Lov para filtro 
    @Listen("onClick=#btn_bloque")
    public void procesoEnBloque() {
        /// if (bandera == false) {
        //  bandera = true;
        // if (txt_codigo.getValue().equals("")) {
        boolean x = validaPeriodo();
        if (x == true) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_per", txt_codigo);
            objMapObjetos.put("des_per", txt_apenom);
            objMapObjetos.put("controlador", "ControllerDescuentos");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDescuentosBloque.zul", null, objMapObjetos);
            window.doModal();
        } else {
            Messagebox.show("No hay periodo en proceso no puede acceder", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        }
        // }
        //  }
    }
     //para los lov de bloques

    //Lov para filtro 
    @Listen("onClick=#btn_faltantes")
    public void procesoFaltantes() {
        /// if (bandera == false) {
        //  bandera = true;
        // if (txt_codigo.getValue().equals("")) {
        boolean x = validaPeriodo();
        if (x == true) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_per", txt_codigo);
            objMapObjetos.put("des_per", txt_apenom);
            objMapObjetos.put("controlador", "ControllerDescuentos");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDescuentosFaltantes.zul", null, objMapObjetos);
            window.doModal();
        } else {
            Messagebox.show("No hay periodo en proceso no puede acceder", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        }
        // }
        //  }
    }

      //Lov para filtro 
    @Listen("onClick=#btn_reint")
    public void procesoReintegros() {
        /// if (bandera == false) {
        //  bandera = true;
        // if (txt_codigo.getValue().equals("")) {
        boolean x = validaPeriodo();
        if (x == true) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_per", txt_codigo);
            objMapObjetos.put("des_per", txt_apenom);
            objMapObjetos.put("controlador", "ControllerDescuentos");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDescuentosReintegros.zul", null, objMapObjetos);
            window.doModal();
        } else {
            Messagebox.show("No hay periodo en proceso no puede acceder", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        }
        // }
        // }
    }
	
    @Listen("onClick=#btn_prestamos")
    public void procesoPrestamos() {
        boolean x = validaPeriodo();
        if (x == true) {
            Map objMaoObjetos = new HashMap();
            objMaoObjetos.put("periodo", txt_periodo2.getValue());
            //objMaoObjetos.put("", campo);
            objMaoObjetos.put("controlador", campo);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDescuentosPrestamos.zul", null, objMaoObjetos);
            window.doModal();
        } else {
            Messagebox.show("No hay periodo en proceso no puede acceder", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        }

    }

    /**
     * Metodo para validar periodo
	 *
	 * @return valor 
     */
    public boolean validaPeriodo() {
        boolean valor = false;
        if (txt_periodo2.getValue().equals("--------")) {
            valor = false;

        } else {
            valor = true;
        }

        return valor;
    }

    /**
     * Metodo para hacer una nueva constante
     *
     * @version 12/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_nuevoc")
    public void nuevoDetalle() {

        if (txt_despersonal.getValue().isEmpty()) {
            Messagebox.show("Por favor ingresar codigo de personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                //@Override
                public void onEvent(Event t) throws Exception {
                    txt_idpersonal.focus();
                }
            });
        } else {
            s_estado = "N";
            txt_mcodconstante.focus();
            limpiaAuditoria();
            // habilitaBotonesDetalle(true, false);
            habilitarCamposDetalle(false);
            limpiarCamposDetalle();
            txt_idpersonal.setDisabled(true);
            habilitaBotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_detalle);
            lst_detalle.clearSelection();
        }
    }

    /**
     * Metodo editar la lista Constante mensual y normal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_editarc")
    public void editarDetalle() {

        if (lst_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_detalle.focus();
        } else {
            if (objDescuentos.getTipo_ingreso() == 1) {
                Messagebox.show("No se puede editar fue enlazado automatico", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "M";
                habilitaBotonesDetalle(true, false);
                habilitarCamposDetalle(false);
                txt_mcodconstante.setDisabled(true);
                Utilitarios.deshabilitarLista(true, lst_detalle);
                d_feccons.focus();
            }

        }

    }

    /**
     * Metodo retroceder no se guardan los cambios
     *
     * @version 17/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_deshacerc")
    public void deshacerDetalle() {
        s_estado = "Q";
        String s_men = "Esta Seguro que desea Deshacer los cambios";
        Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesDetalle(false, true);
                            limpiarCamposDetalle();
                            habilitarCamposDetalle(true);
                            Utilitarios.deshabilitarLista(false, lst_detalle);
                            lst_detalle.clearSelection();
                        }
                    }
                });
    }

  /**
     * Metodo retroceder no se guardan los cambios
     *
     * @version 17/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_eliminarc")
    public void eliminaDetalle() {
        s_estado = "E";
        if (objlstDescuentos.isEmpty()) {
            Messagebox.show("No existen registros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (lst_detalle.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una fila a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            //se agrego para ver si esta enlazado automatico
            if (objDescuentos.getTipo_ingreso() == 1) {
                Messagebox.show("No se puede eliminar, fue enlazado automaticamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Esta seguro que desea eliminar los datos?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            objDescuentos = (Descuentos) lst_detalle.getSelectedItem().getValue();
                            int posicion = lst_detalle.getSelectedIndex();
                            if (estado.equals("M") && !objDescuentos.getInd_accion().equals("M")) {
                                objDescuentos.setInd_accion("E");
                                objlstEliminar.add(objDescuentos);
                            }
                            objlstDescuentos.remove(posicion);
                            lst_detalle.setModel(objlstDescuentos);
                            limpiarCamposDetalle();
                            sumaTotal();
                            limpiaAuditoria();
                        }
                    }
                });
            }
        }
    }

    /**
     * Metodo para guardar detalle
     *
     * @version 16/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_guardarc")
    public void guardarDetalle() {
        String valida = verificarDetalle();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {

                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                        devuelveValor();
                    }
                }
            });

        } else {
            s_mensaje = "Esta seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                        String cod_constante = txt_mcodconstante.getValue();
                        Date fecha_mov = d_feccons.getValue();
                        String glosa = txt_glosa.getValue();
                        double cargo = d_cargo.getValue() == null ? 0.00 : d_cargo.getValue();
                        double abono = d_abono.getValue() == null ? 0.00 : d_abono.getValue();
                        if (s_estado.equals("N")) {
                            objDescuentos = new Descuentos(cod_constante, fecha_mov, glosa, cargo, abono);
                            objlstDescuentos.add(objDescuentos);
                            lst_detalle.setModel(objlstDescuentos);
                            objDescuentos.setInd_accion("N");

                        } else {
                            if (objDescuentos.getInd_accion().equals("N")) {
                                objDescuentos.setCod_constante(cod_constante);
                                objDescuentos.setFecha_mov(fecha_mov);
                                objDescuentos.setGlosa(glosa);
                                objDescuentos.setCargo(cargo);
                                objDescuentos.setAbono(abono);
                                objDescuentos.setInd_accion("N");

                            } else {
                                objDescuentos.setCod_constante(cod_constante);
                                objDescuentos.setFecha_mov(fecha_mov);
                                objDescuentos.setGlosa(glosa);
                                objDescuentos.setCargo(cargo);
                                objDescuentos.setAbono(abono);
                                objDescuentos.setInd_accion("M");
                            }
                            objlstDescuentos.set(lst_detalle.getSelectedIndex(), objDescuentos);
                            s_estado = "Q";
                        }
                        limpiarCamposDetalle();
                        limpiaAuditoria();
                        habilitarCamposDetalle(true);
                        Utilitarios.deshabilitarLista(false, lst_detalle);
                        lst_detalle.clearSelection();
                        // lst_detalle.setSelectedIndex(-1);
                        sumaTotal();

                    }
                }
            });
        }
    }

    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onOK=#txt_idpersonal")
    public void busquedaPersonalMan() {

        if (bandera == false) {
            bandera = true;
            if (txt_idpersonal.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_per", txt_idpersonal);
                objMapObjetos.put("des_per", txt_despersonal);
                objMapObjetos.put("controlador", "ControllerMovLinea");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

        //tbbtn_btn_nuevoc.focus();
    }
    /*
     * Metodo que se realiza al salir del campo codigo de personal
     * @version 15/08/2017
     * @autor Junior Fernandez
     */

    @Listen("onBlur=#txt_idpersonal")
    public void valida_PersonalMan() throws SQLException {
        if (!txt_idpersonal.getValue().isEmpty()) {
            String id = txt_idpersonal.getValue();
            String periodo = txt_periodo.getValue();
            int iddocumento = objDaoMovLinea.verificarDniDescuentos(id, periodo);
            if (iddocumento == 0) {
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        // @Override
                        public void onEvent(Event t) throws Exception {
                            txt_idpersonal.setValue(null);
                            txt_idpersonal.focus();
                            txt_despersonal.setValue("");

                        }
                    });
                } else {
                    txt_idpersonal.setValue(objPersonal.getPlidper());
                    txt_despersonal.setValue(objPersonal.getPldesper());
                    d_fecing.setValue(objPersonal.getFechaing());
                    d_fecces.setValue(objPersonal.getPlfecces());

                    //  habilitaBotonesDetalle(false);
                }
            } else {

                txt_despersonal.setValue("");
                Messagebox.show("Este código ya se encuentra registrado,intenta con otro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_idpersonal.setValue(null);
                        txt_idpersonal.focus();
                        d_fecing.setValue(null);
                        d_fecces.setValue(null);

                    }
                });

            }
        } else {
            txt_despersonal.setValue("");

        }
        bandera = false;

    }

    //Lov en constante 
    @Listen("onOK=#txt_mcodconstante")
    public void muestraConstanteDescuentos() {
        if (bandera == false) {
            bandera = true;
            if (txt_mcodconstante.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_mcodconstante);
                objMapObjetos.put("descripcion", txt_mdesconstante);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstanteDescuentos.zul", null, objMapObjetos);
                window.doModal();

            } else {
                d_feccons.focus();
            }
        }
    }

    //salida de lov
    @Listen("onBlur=#txt_mcodconstante")
    public void validaConstanteMensual() throws SQLException {
        if (!txt_mcodconstante.getValue().isEmpty()) {
            String consulta = txt_mcodconstante.getValue();
            objDescuentos = objDaoDescuentos.consultaContanteDescuento(consulta);
            if (objDescuentos == null) {
                Messagebox.show("El código de constante mensual no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_mcodconstante.setValue("");
                        txt_mcodconstante.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_mcodconstante.setValue(objDescuentos.getCod_constante());
                txt_mdesconstante.setValue(objDescuentos.getDes_constante());
                // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());

            }
        } else {
            txt_mdesconstante.setValue("");
        }
        bandera = false;
    }

    //Lov en constante principal
    @Listen("onOK=#txt_codcon")
    public void muestraConstanteDescuentosPrincipal() {
        if (bandera == false) {
            bandera = true;
            if (txt_codcon.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codcon);
                objMapObjetos.put("descripcion", txt_descon);
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstanteDescuentos.zul", null, objMapObjetos);
                window.doModal();

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

            objDescuentos = objDaoDescuentos.consultaContanteDescuento(consulta);
            if (objDescuentos == null) {
                Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
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

    /**
     * Eventos OK para el detalle
     */
    @Listen("onOK=#d_feccons")
    public void nextDetalle1() {
        d_cargo.focus();

    }

    @Listen("onOK=#d_cargo")
    public void nextDetalle2() {
        d_abono.focus();

    }

    @Listen("onOK=#d_abono")
    public void nextDetalle3() {
        txt_glosa.focus();

    }

    @Listen("onOK=#txt_glosa")
    public void nextDetalle4() {
        guardarDetalle();
    }

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
                objMapObjetos.put("controlador", "ControllerDescuentos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesMovimiento.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }
    //Lov para filtro 
   /* @Listen("onOK=#txt_codigo")
     public void buscarPersonalPrincipal() {
     if (bandera == false) {
     bandera = true;
     if (txt_codigo.getValue().equals("")) {
     Map objMapObjetos = new HashMap();
     objMapObjetos.put("id_per", txt_codigo);
     objMapObjetos.put("des_per", txt_apenom);
     objMapObjetos.put("controlador", "ControllerMovLinea");
     Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovBuscarPersonalMovimiento.zul", null, objMapObjetos);
     window.doModal();
     }
     }
     }*/

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
    /* if (!txt_codigo.getValue().isEmpty()) {
     String id = txt_codigo.getValue();
     objPersonal = objDaoMovLinea.getLovPersonal(id);
     if (objPersonal == null) {
     Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

     public void onEvent(Event t) throws Exception {
     txt_codigo.setValue(null);
     txt_codigo.setValue(null);
     txt_codigo.focus();
     txt_apenom.setValue("");

     }
     });

     } else {
     txt_codigo.setValue(objPersonal.getPlidper());
     txt_apenom.setValue(objPersonal.getPldesper());
     //  habilitaBotonesDetalle(false);
     }

     } else {
     txt_apenom.setValue("");
     //habilitaBotonesDetalle(true);

     }
     bandera = false;
     }*/

    /*
     * Metodo para verificar antes de guardar en el detalle
     */
    public String verificarDetalle() {
        String m_muestra;
        if (txt_mcodconstante.getValue().isEmpty()) {
            m_muestra = "Por favor ingrese codigo de constante";
            foco = "constante";
        } else if (d_feccons.getValue() == null) {
            m_muestra = "Por favor ingrese fecha de movimiento";
            foco = "fecha";
        } /*else if (d_cargo.getValue() == null) {
         m_muestra = "por favor ingrese monto de cargo";
         foco = "cargo";

         } else if (d_abono.getValue() == null) {
         m_muestra = "Por favor ingrese monto de abono";
         foco = "abono";
         } */ else if (txt_glosa.getValue().isEmpty()) {
            m_muestra = "Por favor ingresar glosa";
            foco = "glosa";
        } else if (d_feccons.getValue().before(d_fecing.getValue())) {
            m_muestra = "Fecha de movimiento debe ser mayor a la de ingreso";
            foco = "fecham";
        } else {
            m_muestra = "";
        }
        return m_muestra;
    }
    /*
     * Metodo que devuelve el focus en el detalle
     */

    public void devuelveValor() {
        if (foco.equals("constante")) {
            txt_mcodconstante.focus();
        } else if (foco.equals("fecha")) {
            d_feccons.focus();
        } /*else if (foco.equals("cargo")) {
         d_cargo.focus();
         } else if (foco.equals("abono")) {
         d_abono.focus();
         } */ else if (foco.equals("glosa")) {
            txt_glosa.focus();
        } else if (foco.equals("fecham")) {
            d_feccons.focus();
        }
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoc.setDisabled(b_valida1);
        tbbtn_btn_editarc.setDisabled(b_valida1);
        tbbtn_btn_guardarc.setDisabled(b_valida2);
        tbbtn_btn_deshacerc.setDisabled(b_valida2);
        tbbtn_btn_eliminarc.setDisabled(b_valida1);
        /* tbbtn_btn_nuevoc.setDisabled(b_valida1);
         tbbtn_btn_editarc.setDisabled(b_valida1);
         tbbtn_btn_guardarc.setDisabled(b_valida1);
         tbbtn_btn_deshacerc.setDisabled(b_valida1);
         tbbtn_btn_eliminarc.setDisabled(b_valida1);*/
    }

    /**
     *
     * @param b_valida1 verdadero
     * @param b_valida2 falso metodos basicos
     */
    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiaCabeceradetalle() {
        txt_idpersonal.setValue("");
        txt_despersonal.setValue("");
        d_fecing.setValue(null);
        d_fecces.setValue(null);
        // txt_periodo.setValue("");
        txt_total.setValue(null);
    }
    /*
     * Metodo para habilitar campos del detalle
     */

    public void habilitarCamposDetalle(boolean b_valida) {
        txt_mcodconstante.setDisabled(b_valida);
        d_feccons.setDisabled(b_valida);
        d_abono.setDisabled(b_valida);
        d_cargo.setDisabled(b_valida);
        txt_glosa.setDisabled(b_valida);
    }
    /*
     * Metodo para limpiar campos del detalle
     */

    public void limpiarCamposDetalle() {
        txt_mcodconstante.setValue("");
        txt_mdesconstante.setValue("");
        d_feccons.setValue(null);
        d_abono.setValue(null);
        d_cargo.setValue(null);
        txt_glosa.setValue("");
        habilitaBotonesDetalle(false, true);
    }
    /*
     * Metodo para verificar al guardar principal
     */

    public String verifica() {
        String valor = "";
        if (lst_detalle.getModel().getSize() == 0) {
            valor = "Personal debe tener minimo un detalle";
        }
        return valor;
    }

    public void llenaListaDetalle() throws SQLException {
        objlstDescuentos = null;
        objlstDescuentos = objDaoDescuentos.buscarDetalle(objPrincipal.getTipo_doc() + objPrincipal.getDocumento(), objPrincipal.getSucursal());
        lst_detalle.setModel(objlstDescuentos);
        d_fecing.setValue(objPrincipal.getFec_ing());
        d_fecces.setValue(objPrincipal.getFec_ces());
        txt_periodo.setValue(objPrincipal.getPeriodo());
        txt_idpersonal.setValue(objPrincipal.getCodigo_vista());
        txt_despersonal.setValue(objPrincipal.getPaterno() + " " + objPrincipal.getMaterno() + " " + objPrincipal.getNombre());
    }

    public void llenarCamposDetalle(Descuentos d) {
        txt_mcodconstante.setValue(d.getCod_constante());
        txt_mdesconstante.setValue(d.getDes_constante());
        d_feccons.setValue(d.getFecha_mov());
        d_cargo.setValue(d.getCargo());
        d_abono.setValue(d.getAbono());
        txt_glosa.setValue(d.getGlosa());

    }

    /**
     * Metodo limpiar listas principal y detalle
     */
    public void limpiarListaPrincipal() {
        objlstPrincipal = null;
        objlstPrincipal = new ListModelList<Descuentos>();
        lst_principal.setModel(objlstPrincipal);
    }

    public void limpiarListaDetalle() {
        objlstDescuentos = null;
        objlstDescuentos = new ListModelList<Descuentos>();
        lst_detalle.setModel(objlstDescuentos);
    }

    //suma de el doublebox total
    public void sumaTotal() {
        double a = 0;
        double b = 0;
        double c = 0;
        for (int i = 0; i < objlstDescuentos.size(); i++) {
            a = objlstDescuentos.get(i).getAbono() + a;
            b = objlstDescuentos.get(i).getCargo() + b;
            c = a - b;
        }
        txt_total.setValue(c);
    }

    /**
     * metodo para la suma neta en la lista principal
     */
    public void sumaLista() {
        double a = 0;
        for (int i = 0; i < objlstPrincipal.size(); i++) {
            a = objlstPrincipal.get(i).getNeto() + a;
        }
        txt_total_lista.setValue(a);
    }

    /*
     *Metodo para completar datos de auditoria
     */
    public void llenaAuditoria() {
        txt_usuadd.setValue(objDescuentos.getUsu_add());
        txt_usumod.setValue(objDescuentos.getUsu_mod());
        d_fecadd.setValue(objDescuentos.getFecha_add());
        d_fecmod.setValue(objDescuentos.getFecha_mod());

    }
}
