package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;

import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.Movlinea;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
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
public class ControllerMovLinea extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Textbox txt_usua, txt_usumo, txt_idpersonal,
            txt_despersonal, txt_codcons, txt_descons,
            txt_codconsm, txt_desconsm, txt_periodo,
            txt_codcon, txt_descon, txt_codigo, txt_codigo1, txt_apenom, txt_codarea, txt_desarea, txt_codarea1;
    @Wire
    Doublebox txt_valorcons, txt_valorconsm;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Radiogroup rbg_indicador, rbg_indicador_principal;
    @Wire
    Listbox lst_principal, lst_constante, lst_constantemensual;
    @Wire
    Combobox cb_fsucursal, cb_area;
    @Wire
    Button btn_consultar, btn_genblo;
    @Wire
    Toolbarbutton tbbtn_btn_nuevoc, tbbtn_btn_editarc, tbbtn_btn_deshacerc, tbbtn_btn_eliminarc, tbbtn_btn_guardarc,
            tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    ListModelList<Movlinea> objlstEliminar, objlstMovLinea, objlstConstanteMensual, objlsPrincipal;
    Movlinea objMovLinea, objConstanteMensual, objPrincipal;
    DaoMovLinea objDaoMovLinea;
    ManAreas objArea;
    Personal objPersonal;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    //  DaoManPresPer objDaoManPresPer = new DaoManPresPer();
    //Instancias de Objetos
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    DaoPersonal objDaoPersonal;
    DaoPerPago objDaoPerPago;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovLinea.class);
    //variables a usar
    String s_estado = "Q", campo, s_mensaje, estado = "Q";//s_estado = detalle && estado = general
    String foco = "";
    int i_sel;
	
    public static boolean bandera = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
		
        estado = "Q";
        objDaoMovLinea = new DaoMovLinea();
        objDaoAccesos = new DaoAccesos();
        objDaoPersonal = new DaoPersonal();
        objlstMovLinea = null;
        objlstMovLinea = new ListModelList<Movlinea>();//lista de constante
        objlstConstanteMensual = null;
        objlstConstanteMensual = new ListModelList<Movlinea>();
        objlstEliminar = null;
        objlsPrincipal = new ListModelList<Movlinea>();
        objlstEliminar = new ListModelList<Movlinea>();
        habilitaTab(false, true);
        //se completa combobox de sucursales
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);
        //periodo
        //String periodo = objDaoMovLinea.setearPeriodo();
        objDaoPerPago = new DaoPerPago();
        String periodo = objDaoPerPago.getPeriodoProceso(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo);
        //se completa lista al ingresar al formulario Movimiento Linea

        objlsPrincipal = objDaoMovLinea.ingresoMovimiento(periodo);
        lst_principal.setModel(objlsPrincipal);

        /*if (!periodo.isEmpty()) {
         txt_periodo.setValue(periodo);
         } else {
         tbbtn_btn_nuevo.setDisabled(true);
         tbbtn_btn_editar.setDisabled(true);                       
         }*/
        rbg_indicador.setSelectedIndex(0);
        rbg_indicador_principal.setSelectedIndex(0);
		
    }

    @Listen("onOK=#txt_codarea")
    public void busquedaArea() {

        if (bandera == false) {
            bandera = true;
            if (txt_codarea.getValue().equals("")) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("id_area", txt_codarea);
                objMapObjetos.put("des_area", txt_desarea);
                objMapObjetos.put("codarea1", txt_codarea1);//campo invisible que guarda informacion de personal
                // objMapObjetos.put("des", txt_desper1);
                objMapObjetos.put("controlador", "ControllerMovimiento");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAreas.zul", null, objMapObjetos);
                window.doModal();
            }
        }

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90201000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Movimientos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Movimientos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Movimientos");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Movimientos");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Movimientos");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Movimientos");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Movimientos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Movimientos");
        }
		if (ControllerPersonal.val == 1) {
            botonNuevo();
        }
    }
	
    @Listen("onBlur=#txt_codarea")
    public void validaAreas() throws SQLException {
        if (!txt_codarea.getValue().isEmpty()) {

            if (!txt_codarea.getValue().equals("ALL")) {
                String consulta = txt_codarea.getValue();
                objArea = objDaoMovLinea.consultaArea(consulta);
                if (objArea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codarea.setValue("");
                            txt_desarea.setValue("");
                            txt_codarea.focus();
                        }
                    });
                }else{
                    txt_codarea.setValue(objArea.getArea_id());
                    txt_desarea.setValue(objArea.getArea_des());
                    txt_codarea1.setValue(objArea.getArea_id() + "','");
                }
            }
        } else {
             //txt_codarea.setValue("");
            txt_codarea1.setValue("");
            txt_desarea.setValue("");

        }
        bandera = false;
    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_movlinea")
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
                    botonEliminar();
                }
                break;
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
            case 120:
                if (!tbbtn_btn_deshacer.isDisabled()) {
                    botonDeshacer();
                }
                break;
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;
        }
    }
    /*
     * Metodo para buscar por filtros 
     */

    @Listen("onClick=#btn_consultar")
    public void buscarRegistros() throws SQLException {
        limiparListaPrincipal();
        String periodo = txt_periodo.getValue();
        String idconstante = txt_codcon.getValue();
        // String idcodigo = txt_codigo.getValue();
        String idcodigo = txt_codigo1.getValue();
        String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
        String area = txt_codarea1.getValue();
        objlsPrincipal = objDaoMovLinea.buscarRegistro(sucursal, idcodigo, idconstante, area, periodo);

        if (objlsPrincipal.isEmpty()) {
            Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            lst_principal.setModel(objlsPrincipal);
        }

    }

    /*
     * Evento al seleccionar la lista principal te carga los dal seleccionado
     *
     * @version 10/08/2017
     */
    @Listen("onSelect=#lst_principal")
    public void seleccionaPrincipal() throws SQLException {
        limpiaConstante();
        limpiaConstanteMensual();
        limpiarCamposGuardar();
        objPrincipal = (Movlinea) lst_principal.getSelectedItem().getValue();
        if (objPrincipal == null) {
            objPrincipal = objlsPrincipal.get(i_sel + 1);
        }
        i_sel = lst_principal.getSelectedIndex();
        habilitaTab(false, false);
        completaListaConstante();
        // llenaAuditoria();
        txt_idpersonal.setDisabled(true);
        habilitaBotonesDetalle(true, true);
    }

    /**
     * Metodo para crear un nuevo registro
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        estado = "N";
        //if (txt_periodo.getValue()!=null || !txt_periodo.getValue().isEmpty() || txt_periodo.getValue().equalsIgnoreCase("")) {
        if (!txt_periodo.getValue().isEmpty() && !txt_periodo.getValue().equals("--------")) {
            habilitaBotones(true, false);
            habilitaBotonesDetalle(false, true);
            seleccionaTab(false, true);
            habilitaTab(true, false);
            txt_idpersonal.focus();
            txt_idpersonal.setDisabled(false);
            rbg_indicador.setSelectedIndex(0);
            limpiaAuditoria();
            limpiaConstante();
            limpiaConstanteMensual();
            limpiarCamposGuardar();
            limpiarListas();

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
        if (!txt_periodo.getValue().isEmpty() && !txt_periodo.getValue().equals("--------")) {
            txt_idpersonal.setDisabled(true);//pendiente
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
                // habilitaBotonesDetalle(false);
                seleccionaTab(false, true);
                habilitaTab(true, false);
                habilitaBotonesDetalle(false, true);
                objlstEliminar = null;
                objlstEliminar = new ListModelList<Movlinea>();
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

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() {

    }

    /**
     * Metodo para guardar todos los cambios generales
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
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

                s_mensaje = "Esta seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP,JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {

                            public void onEvent(Event t) throws Exception {
                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objPara;
                                    objlstConstanteMensual.addAll(objlstEliminar);
                                    objlstMovLinea.addAll(objlstEliminar);
                                    if (estado.equals("N")) {
                                        objPara = objDaoMovLinea.insertarConstante(getConstante(objlstMovLinea), getConstanteMensual(objlstConstanteMensual));
                                    } else {
                                        objPara = objDaoMovLinea.modificaConstante(getConstante(objlstMovLinea), getConstanteMensual(objlstConstanteMensual));
                                    }
                                    if (objPara.getFlagIndicador() == 0) {
                                        limpiaAuditoria();
                                        habilitaTab(false, true);
                                        seleccionaTab(true, false);
                                        habilitaBotones(false, true);
                                        //se modifico
                                        //habilitaBotonesDetalle(true,true);
                                        limpiaConstante();
                                        limpiaConstanteMensual();
                                        limpiarCamposGuardar();
                                        limpiarListas();
                                        habilitaCamposConstantes(true, true);
                                        rbg_indicador.setSelectedIndex(0);
                                        rbg_indicador_principal.setSelectedIndex(0);
                                        objlstEliminar.clear();
                                        objlstMovLinea = objDaoMovLinea.ingresoMovimiento(txt_periodo.getValue());
                                        lst_principal.setModel(objlstMovLinea);
                                        estado = "Q";
                                        if (objPara.getMsgValidacion() == null) {
                                            Messagebox.show("No se realizo ninguna operacion", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
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

    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {

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
                            limpiaConstante();
                            limpiaConstanteMensual();
                            limpiarCamposGuardar();
                            limpiarListas();
                            habilitaCamposConstantes(true, true);
                            habilitaBotonesDetalle(false, true);
                            lst_principal.clearSelection();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                            estado = "Q";
                        }
                    }
                });
    }

    /**
     * Metodo guarda datos en la lista constante mensual y normal
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_guardarc")
    public void guardarDetalle() {
        String valida = verificaDetalle();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("codigoc")) {
                            txt_codcons.focus();
                        } else if (foco.equals("valorc")) {
                            txt_valorcons.focus();
                        } else if (foco.equals("codigom")) {
                            txt_codconsm.focus();
                        } else if (foco.equals("valorm")) {
                            txt_valorconsm.focus();
                        } else if (foco.equals("constanteigual")) {
                            txt_codcons.focus();
                        } else if (foco.equals("constantemensualigual")) {
                            txt_codconsm.focus();
                        }

                    }
                }
            });
        } else {
            s_mensaje = "Esta seguro que desea guardar";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

                public void onEvent(Event t) throws Exception {
                    if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                        String codigo, descripcion;
                        double valor;
                        if (rbg_indicador.getSelectedIndex() == 0) {
                            codigo = txt_codcons.getValue();
                            valor = txt_valorcons.getValue();
                            descripcion = txt_descons.getValue();
                        } else {
                            codigo = txt_codconsm.getValue();
                            valor = txt_valorconsm.getValue();
                            descripcion = txt_desconsm.getValue();

                        }
                        // char tipo_doc = txt_idpersonal.getValue().charAt(0);
                        //  String documento = txt_idpersonal.getValue().substring(1);
                        //  int empresa = objUsuCredential.getCodemp();
                        //  int sucursal = objUsuCredential.getCodsuc();
                        //   String usurio = objUsuCredential.getCuenta();
                        //   String pc = objUsuCredential.getComputerName();
                        if (s_estado.equals("N")) {

                            if (rbg_indicador.getSelectedIndex() == 0) {

                                /* String codigo = txt_codcons.getValue();
                                 double valor = txt_valorcons.getValue();
                                 String descripcion = txt_descons.getValue();*/
                                objMovLinea = new Movlinea(codigo, valor, descripcion);
                                objlstMovLinea.add(objMovLinea);
                                lst_constante.setModel(objlstMovLinea);
                                objMovLinea.setInd_accion("N");//se agrego recien 
                                limpiaConstante();
                                txt_codcons.focus();
                            } else {

                                /*String codigo = txt_codconsm.getValue();
                                 double valor = txt_valorconsm.getValue();
                                 String descripcion = txt_desconsm.getValue();*/
                                objMovLinea = new Movlinea(codigo, valor, descripcion);
                                objlstConstanteMensual.add(objMovLinea);
                                lst_constantemensual.setModel(objlstConstanteMensual);
                                objMovLinea.setInd_accion("N");//se agrego recien 
                                limpiaConstanteMensual();
                                txt_codconsm.focus();
                            }
                            objMovLinea.setInd_accion("N");
                            //lst_constante.setModel(objlstMovLinea);
                            //para mañana
                        } else {
                            if (objMovLinea.getInd_accion().equals("N")) {
                                objMovLinea.setId_concepto(codigo);
                                objMovLinea.setDescripcion(descripcion);
                                objMovLinea.setValor_concepto(valor);
                                objMovLinea.setInd_accion("N");

                            } else {
                                objMovLinea.setId_concepto(codigo);
                                objMovLinea.setDescripcion(descripcion);
                                objMovLinea.setValor_concepto(valor);
                                objMovLinea.setInd_accion("M");
                            }
                            if (rbg_indicador.getSelectedIndex() == 0) {
                                objlstMovLinea.set(lst_constante.getSelectedIndex(), objMovLinea);
                                s_estado = "Q";
                            } else {
                                objlstConstanteMensual.set(lst_constantemensual.getSelectedIndex(), objMovLinea);
                                s_estado = "Q";

                            }

                        }
                        limpiaConstante();
                        limpiaConstanteMensual();
                        limpiaAuditoria();
                        habilitaBotonesDetalle(false, true);

                    }

                }
            });

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
        if (rbg_indicador.getSelectedIndex() == 0) {
            if (lst_constante.getSelectedIndex() == -1) {
                Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                lst_constante.focus();
            } else {
                s_estado = "M";
                habilitaCamposConstantes(true, true);
                txt_valorcons.setDisabled(false);
                txt_valorcons.focus();
                limpiaConstanteMensual();
                limpiaAuditoria();
                habilitaBotonesDetalle(true, false);
                //habilitaCamposConstantes(true, false);
            }
        } else {
            if (lst_constantemensual.getSelectedIndex() == -1) {
                Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                lst_constantemensual.focus();
            } else {
                s_estado = "M";
                habilitaCamposConstantes(true, true);
                txt_valorconsm.setDisabled(false);
                txt_valorconsm.focus();
                limpiaConstante();
                limpiaAuditoria();
                habilitaBotonesDetalle(true, false);
            }

        }
    }

    /**
     * Metodo retroceder no se guardan los cambios
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_deshacerc")
    public void deshacer() {
        s_estado = "A";
        if (rbg_indicador.getSelectedIndex() == 0) {
            String mensaje = "Esta Seguro que desea Deshacer los cambios";
            Messagebox.show(mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiaConstante();
                                limpiaConstanteMensual();
                                limpiaAuditoria();
                                habilitaCamposConstantes(true, true);
                                habilitaBotonesDetalle(false, true);
                                Utilitarios.deshabilitarLista(false, lst_constante);
                                lst_constante.clearSelection();

                            }
                        }
                    ;
        }  
            );
        }else{
            String mensaje = "Esta Seguro que desea Deshacer los cambios";
            Messagebox.show(mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                Utilitarios.deshabilitarLista(false, lst_constantemensual);
                                limpiaConstante();
                                limpiaConstanteMensual();
                                limpiaAuditoria();
                                habilitaCamposConstantes(true, true);
                                habilitaBotonesDetalle(false, true);
                                Utilitarios.deshabilitarLista(false, lst_constantemensual);
                                lst_constantemensual.clearSelection();
                            }
                        }
                    ;
        }

    

    );
            
        }
       

    }
   /*
    * Metodo eliminar lista detalle constante y mensual
    */
    @Listen("onClick=#tbbtn_btn_eliminarc")
    public void eliminarDetalle() {
        s_estado = "E";
        if (rbg_indicador.getSelectedIndex() == 0) {
            if (objlstMovLinea.isEmpty()) {
                Messagebox.show("No existen registros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else if (lst_constante.getSelectedIndex() == -1) {
                Messagebox.show("Por favor seleccione una fila a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {
                s_mensaje = "Esta seguro que desea eliminar este registro?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            objMovLinea = (Movlinea) lst_constante.getSelectedItem().getValue();
                            int posicion = lst_constante.getSelectedIndex();
                            if (estado.equals("M") && !objMovLinea.getInd_accion().equals("N")) {
                                objMovLinea.setInd_accion("E");
                                objlstEliminar.add(objMovLinea);
                            }
                            objlstMovLinea.remove(posicion);
                            lst_constante.setModel(objlstMovLinea);
                            limpiaConstante();
                            limpiaAuditoria();
                        }
                    }
                });
            }
        } else {
            if (objlstConstanteMensual.isEmpty()) {
                Messagebox.show("No existen registros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else if (lst_constantemensual.getSelectedIndex() == -1) {
                Messagebox.show("Por favor seleccione una fila a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {
                s_mensaje = "Esta seguro que desea eliminar este registro?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                            objMovLinea = (Movlinea) lst_constantemensual.getSelectedItem().getValue();
                            int posicion = lst_constantemensual.getSelectedIndex();
                            if (estado.equals("M") && !objMovLinea.getInd_accion().equals("N")) {
                                objMovLinea.setInd_accion("E");
                                objlstEliminar.add(objMovLinea);
                            }
                            objlstConstanteMensual.remove(posicion);
                            lst_constantemensual.setModel(objlstConstanteMensual);
                            limpiaConstanteMensual();
                            limpiaAuditoria();
                        }
                    }
                });
            }

        }
    }

    /**
     * Metodo inserta datos en los campos Constante para editarlos
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onSelect=#lst_constante")
    public void seleccionaListaConstante() {
        rbg_indicador.setSelectedIndex(0);
        objMovLinea = (Movlinea) lst_constante.getSelectedItem().getValue();
        if (objMovLinea == null) {
            objMovLinea = objlstMovLinea.get(i_sel + 1);
        }
        i_sel = lst_constante.getSelectedIndex();
        llenacamposConstante(objMovLinea);
        lst_constantemensual.clearSelection();
        limpiaConstanteMensual();
        llenaAuditoriaConstante();
        habilitaCamposConstantes(true, true);
        //habilitaBotonesDetalle(false, true);
        if (estado.equals("N") || estado.equals("M")) {
            habilitaBotonesDetalle(false, true);
        }

    }

    /**
     * Metodo inserta datos en los campos Constante mensual para editarlos
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onSelect=#lst_constantemensual")
    public void seleccionaListaConstanteMensual() {
        rbg_indicador.setSelectedIndex(1);
        objMovLinea = (Movlinea) lst_constantemensual.getSelectedItem().getValue();
        if (objMovLinea == null) {
            objMovLinea = objlstMovLinea.get(i_sel + 1);
        }
        i_sel = lst_constantemensual.getSelectedIndex();
        llenaCampodConstanteMensual(objMovLinea);
        lst_constante.clearSelection();
        limpiaConstante();
        // limpiaAuditoria();
        llenaAuditoriaConstante();
        //habilitaBotonesDetalle(false, true);
        //habilitaBotonesDetalle(true, true);
        habilitaCamposConstantes(true, true);
        if (estado.equals("N") || estado.equals("M")) {
            habilitaBotonesDetalle(false, true);
        }
    }

    /*
     *Eventos OK para mostrar Lov
     */
    //Lov en txt de constante para buscar
    @Listen("onOK=#txt_codcon")
    public void lovConstantePrincipal() {
        if (bandera == false) {
            bandera = true;
            String tipo = "";
            if (rbg_indicador_principal.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indicador_principal.getSelectedIndex() == 1) {
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
                cb_area.focus();
            }

        }
    }

    //Lov en constante 
    @Listen("onOK=#txt_codcons")
    public void muestraConstante() {
        if (bandera == false) {
            bandera = true;
            String tipo = "";
            if (rbg_indicador.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indicador.getSelectedIndex() == 1) {
                tipo = "M";
            }

            if (txt_codcons.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codcons);
                objMapObjetos.put("descripcion", txt_descons);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerMovLinea");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstante.zul", null, objMapObjetos);
                window.doModal();

            } else {
                txt_valorcons.focus();
            }

        }
    }

    //Lov en constante mensual
    @Listen("onOK=#txt_codconsm")
    public void muestraConstanteMensual() {
        if (bandera == false) {
            bandera = true;
            String tipo = "";
            if (rbg_indicador.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indicador.getSelectedIndex() == 1) {
                tipo = "M";
            }

            if (txt_codconsm.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("codigo", txt_codconsm);
                objMapObjetos.put("descripcion", txt_desconsm);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerMovLinea");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstante.zul", null, objMapObjetos);
                window.doModal();

            } else {
                txt_valorconsm.focus();
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
            if (rbg_indicador_principal.getSelectedIndex() == 0) {
                tipo = "C";
            } else if (rbg_indicador_principal.getSelectedIndex() == 1) {
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

    //salida de lov
    @Listen("onBlur=#txt_codcons")
    public void validaConstante() throws SQLException {
        if (!txt_codcons.getValue().isEmpty()) {
            String consulta = txt_codcons.getValue();
            objMovLinea = objDaoMovLinea.consultaContante(consulta, "C");
            if (objMovLinea == null) {
                Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codcons.setValue("");
                        txt_codcons.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_codcons.setValue(objMovLinea.getId_concepto());
                txt_descons.setValue(objMovLinea.getDescripcion());
                // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());

            }
        } else {
            txt_descons.setValue("");
        }
        bandera = false;
    }

    //salida de lov
    @Listen("onBlur=#txt_codconsm")
    public void validaConstanteMensual() throws SQLException {
        if (!txt_codconsm.getValue().isEmpty()) {
            String consulta = txt_codconsm.getValue();
            objMovLinea = objDaoMovLinea.consultaContante(consulta, "M");
            if (objMovLinea == null) {
                Messagebox.show("El código de constante mensual no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codconsm.setValue("");
                        txt_codconsm.focus();
                    }
                });
            } else {
                //LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objleas.getLea_nrolea()).append(" y ha encontrado 1 registro(s)").toString());
                txt_codconsm.setValue(objMovLinea.getId_concepto());
                txt_desconsm.setValue(objMovLinea.getDescripcion());
                // txt_valorcons.setValue(objMovLinea.getValor_concepto().toString());

            }
        } else {
            txt_desconsm.setValue("");
        }
        bandera = false;
    }

    /**
     * Metodo para hacer una nueva constante
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#tbbtn_btn_nuevoc")
    public void nuevoConstante() {
        s_estado = "N";
        if (txt_despersonal.getValue().isEmpty()) {
            Messagebox.show("Por favor ingresar codigo de personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {

                    txt_idpersonal.focus();
                }
            });
        } else {
            if (rbg_indicador.getSelectedIndex() == 0) {
                habilitaCamposConstantes(false, true);
                txt_codcons.focus();
                limpiaConstante();
                limpiaConstanteMensual();
                limpiaAuditoria();
                txt_idpersonal.setDisabled(true);
                habilitaBotonesDetalle(true, false);
            } else if (rbg_indicador.getSelectedIndex() == 1) {
                habilitaCamposConstantes(true, false);
                txt_codconsm.focus();
                limpiaConstante();
                limpiaConstanteMensual();
                limpiaAuditoria();
                txt_idpersonal.setDisabled(true);
                habilitaBotonesDetalle(true, false);
            }
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
    /**
     * Metodo muestra lov de personal
     *
     * @version 09/08/2017
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

    //Lov para filtro 
    @Listen("onClick=#btn_genblo")
    public void procesoEnBloque() {
        /// if (bandera == false) {
        //  bandera = true;
        // if (txt_codigo.getValue().equals("")) {
        Map objMapObjetos = new HashMap();
        objMapObjetos.put("id_per", txt_codigo);
        objMapObjetos.put("des_per", txt_apenom);
        objMapObjetos.put("controlador", "ControllerMovLinea");
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovConstanteBloque.zul", null, objMapObjetos);
        window.doModal();
        // }
        //  }
    }
    /*
     * Metodo que se realiza al salir del campo codigo de personal
     * @version 09/08/2017
     * @autor Junior Fernandez
     */

    @Listen("onBlur=#txt_idpersonal")
    public void valida_PersonalMan() throws SQLException {

        if (!txt_idpersonal.getValue().isEmpty()) {
            String id = txt_idpersonal.getValue();
            String periodo = txt_periodo.getValue();
            int iddocumento = objDaoMovLinea.verificarDni(id, periodo);
            if (iddocumento == 0) {

                objPersonal = objDaoMovLinea.getLovPersonal(id);

                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_idpersonal.setValue(null);
                            txt_idpersonal.setValue(null);
                            txt_idpersonal.focus();
                            txt_despersonal.setValue("");

                        }
                    });

                } else {
                    txt_idpersonal.setValue(objPersonal.getPlidper());
                    txt_despersonal.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                }

            } else {
                Messagebox.show("Este código ya se encuentra registrado,intenta con otro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        txt_idpersonal.setValue(null);
                        txt_idpersonal.setValue(null);
                        txt_idpersonal.focus();
                        txt_despersonal.setValue("");

                    }
                });

            }
        } else {
            txt_despersonal.setValue("");
            //   habilitaBotonesDetalle(true);

        }
        bandera = false;

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

    /**
     * Evento en el radio buton
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    @Listen("onClick=#rbg_indicador")
    public void cambioSelect() {
        if (rbg_indicador.getSelectedIndex() == 0) {
            habilitaCamposConstantes(true, true);
            // habilitaBotonesDetalle(false, true);
            limpiaConstanteMensual();
            lst_constantemensual.clearSelection();
            if (estado.equals("N") || estado.equals("M")) {
                habilitaBotonesDetalle(false, true);
            }

        } else {

            habilitaCamposConstantes(true, true);
            // habilitaBotonesDetalle(false, true);
            limpiaConstante();
            lst_constante.clearSelection();
            if (estado.equals("N") || estado.equals("M")) {
                habilitaBotonesDetalle(false, true);
            }
        }
    }

    /**
     * Metodo de eventos enter
     *
     * @autor junior fernandez
     *
     */
    @Listen("onOK=#txt_valorconsm")
    public void enterCM() {
        guardarDetalle();
    }

    @Listen("onOK=#txt_valorcons")
    public void enterC() {
        guardarDetalle();
    }

    /*
     * Metodo para guardarv el detalle en base de datos de la lista constante
     * @param x
     * @return lista de datos a guardar
     * @autor Junior Fernandez
     */
    public Object[][] getConstante(ListModelList<Movlinea> x) {
        ListModelList<Movlinea> objListaDepurada3;

        if (s_estado.equals("M")) {
            objListaDepurada3 = new ListModelList<Movlinea>();
            for (int i = 0; i < x.size(); i++) {
                if (!x.get(i).getInd_accion().equals("Q")) {
                    objListaDepurada3.add(x.get(i));
                }
            }
        } else {
            objListaDepurada3 = x;
        }
        Object[][] listaConstante = new Object[objListaDepurada3.size()][11];
        for (int i = 0; i < objListaDepurada3.size(); i++) {
            listaConstante[i][0] = objUsuCredential.getCodemp();
            //  listaConstante[i][1] = objUsuCredential.getCodsuc();
            if (objPersonal != null) {
                listaConstante[i][1] = objPersonal.getSuc_id();
                listaConstante[i][2] = objPersonal.getPltipdoc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaConstante[i][3] = objPersonal.getPlnrodoc();//txt_idpersonal.getValue().substring(1);   
            } else {
                listaConstante[i][1] = objPrincipal.getSucursal();
                listaConstante[i][2] = objPrincipal.getTipo_doc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaConstante[i][3] = objPrincipal.getNumero_doc();//txt_idpersonal.getValue().substring(1);    
            }

            listaConstante[i][4] = txt_periodo.getValue();
            listaConstante[i][5] = objListaDepurada3.get(i).getId_concepto();
            listaConstante[i][6] = objListaDepurada3.get(i).getValor_concepto();
            listaConstante[i][7] = objUsuCredential.getCuenta();
            listaConstante[i][8] = objUsuCredential.getComputerName();
            listaConstante[i][9] = objListaDepurada3.get(i).getInd_accion();
            listaConstante[i][10] = objListaDepurada3.get(i).getNro_ope();

        }
        return listaConstante;
    }

    /**
     * Metodo para guardarv el detalle en base de datos de la lista constante
     * mensual
     *
     * @param x
     * @return lista de datos a guardar
     * @autor Junior Fernandez
     */
    public Object[][] getConstanteMensual(ListModelList<Movlinea> x) {
        ListModelList<Movlinea> objListaDepurada3;

        if (s_estado.equals("M")) {
            objListaDepurada3 = new ListModelList<Movlinea>();
            for (int i = 0; i < x.size(); i++) {
                if (!x.get(i).getInd_accion().equals("Q")) {
                    objListaDepurada3.add(x.get(i));
                }
            }
        } else {
            objListaDepurada3 = x;
        }
        Object[][] listaConstanteMensual = new Object[objListaDepurada3.size()][11];
        for (int i = 0; i < objListaDepurada3.size(); i++) {
            listaConstanteMensual[i][0] = objUsuCredential.getCodemp();
            //    listaConstanteMensual[i][1] = objUsuCredential.getCodsuc();
            //listaConstanteMensual[i][2] = objPersonal.getPltipdoc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
            // listaConstanteMensual[i][3] = objPersonal.getPlnrodoc();//txt_idpersonal.getValue().substring(1);
            if (objPersonal != null) {
                listaConstanteMensual[i][1] = objPersonal.getSuc_id();
                listaConstanteMensual[i][2] = objPersonal.getPltipdoc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaConstanteMensual[i][3] = objPersonal.getPlnrodoc();//txt_idpersonal.getValue().substring(1);   
            } else {
                listaConstanteMensual[i][1] = objPrincipal.getSucursal();
                listaConstanteMensual[i][2] = objPrincipal.getTipo_doc();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaConstanteMensual[i][3] = objPrincipal.getNumero_doc();//txt_idpersonal.getValue().substring(1);    
            }
            listaConstanteMensual[i][4] = txt_periodo.getValue();
            listaConstanteMensual[i][5] = objListaDepurada3.get(i).getId_concepto();
            listaConstanteMensual[i][6] = objListaDepurada3.get(i).getValor_concepto();
            listaConstanteMensual[i][7] = objUsuCredential.getCuenta();
            listaConstanteMensual[i][8] = objUsuCredential.getComputerName();
            listaConstanteMensual[i][9] = objListaDepurada3.get(i).getInd_accion();
            listaConstanteMensual[i][10] = objListaDepurada3.get(i).getNro_ope();

        }
        return listaConstanteMensual;
    }

    /**
     * valida botones si campo personal esta vacio
     *
     * @param codigo
     */
    /*  public void verCodigo(String codigo) {

     if (txt_idpersonal.getValue() == null || txt_idpersonal.getValue().isEmpty()) {
     // habilitaBotonesDetalle(true);
     habilitaBotonesDetalle(true, false);
     } else {
     habilitaBotonesDetalle(false, true);
     // habilitaBotonesDetalle(false);
     }

     }*/
    /**
     * Metodo que llena los campos cuando selecciona lista constante
     *
     * @param m del modelo movimiento
     */
    public void llenacamposConstante(Movlinea m) {
        txt_codcons.setValue(m.getId_concepto());
        txt_descons.setValue(m.getDescripcion());
        txt_valorcons.setValue(m.getValor_concepto());
        txt_codcons.setDisabled(true);
        txt_valorcons.setDisabled(true);
    }

    /**
     * Metodo que llena los campos cuando selecciona lista constante Mensual
     *
     * @param p del modelo movimiento
     */
    public void llenaCampodConstanteMensual(Movlinea p) {
        txt_codconsm.setValue(p.getId_concepto());
        txt_desconsm.setValue(p.getDescripcion());
        txt_valorconsm.setValue(p.getValor_concepto());
        txt_codconsm.setDisabled(true);
        txt_valorconsm.setDisabled(true);
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
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    /**
     * Metodo limpia campos Constante mensual
     *
     * @version 09/08/2017
     * @autor Junior Fernandez
     */
    public void limpiaConstante() {
        txt_codcons.setValue("");
        txt_descons.setValue("");
        txt_valorcons.setValue(null);

    }

    public void limpiaConstanteMensual() {
        txt_codconsm.setValue("");
        txt_desconsm.setValue("");
        txt_valorconsm.setValue(null);

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

    public void habilitaCamposConstantes(boolean b_valida1, boolean b_valida2) {
        txt_codcons.setDisabled(b_valida1);
        //txt_descons.setDisabled(b_valida1);
        txt_valorcons.setDisabled(b_valida1);

        txt_codconsm.setDisabled(b_valida2);
        //txt_desconsm.setDisabled(b_valida2);
        txt_valorconsm.setDisabled(b_valida2);
    }
//verifica al guardar general

    public String verifica() {
        String valor = "";
        if (lst_constante.getModel().getSize() == 0 && lst_constantemensual.getModel().getSize() == 0) {
            valor = "Personal debe tener minimo una constante";
        }
        return valor;
    }

    //verificar detalle de lista
    public String verificaDetalle() {
        String valor = "";
        if (rbg_indicador.getSelectedIndex() == 0) {
            if (txt_codcons.getValue().isEmpty()) {
                valor = "Ingrese codigo de constante";
                foco = "codigoc";
            } else if (txt_valorcons.getValue() == null) {
                valor = "Ingrese valor";
                foco = "valorc";
            } else if (s_estado.equals("N") || !s_estado.equals("M")) {
                for (int i = 0; i < objlstMovLinea.getSize(); i++) {
                    if (txt_codcons.getValue().equals(objlstMovLinea.get(i).getId_concepto())) {
                        valor = "Id de constante ya se encuentra registrada";
                        foco = "constanteigual";
                    }

                }
            }
        } else if (rbg_indicador.getSelectedIndex() == 1) {
            if (txt_codconsm.getValue().isEmpty()) {
                valor = "Ingrese codigo de constante";
                foco = "codigom";
            } else if (txt_valorconsm.getValue() == null) {
                valor = "Ingrese valor";
                foco = "valorm";
            } else if (s_estado.equals("N") || !s_estado.equals("M")) {
                for (int i = 0; i < objlstConstanteMensual.getSize(); i++) {
                    if (txt_codconsm.getValue().equals(objlstConstanteMensual.get(i).getId_concepto())) {
                        valor = "Id de constante ya se encuentra registrada";
                        foco = "constantemensualigual";
                    }

                }
            }
        }
        return valor;

    }

    /**
     * Metodo limpia campos despues de guardar general
     *
     * @version 10/08/2017
     * @autor Junior Fernandez
     */
    public void limpiarCamposGuardar() {
        txt_idpersonal.setValue("");
        txt_despersonal.setValue("");
    }

    /*
     * Metodo limpia lista Constante y constante mensual
     * @version 10/08/2017
     * @autor Junior Fernandez
     */
    public void limpiarListas() {
        objlstMovLinea = null;
        objlstMovLinea = new ListModelList<Movlinea>();
        lst_constante.setModel(objlstMovLinea);
        objlstConstanteMensual = null;
        objlstConstanteMensual = new ListModelList<Movlinea>();
        lst_constantemensual.setModel(objlstConstanteMensual);
    }
    /*
     * Metodo para limpiar lista principal
     */

    public void limiparListaPrincipal() {
        objlsPrincipal = null;
        objlsPrincipal = new ListModelList<Movlinea>();
        lst_principal.setModel(objlsPrincipal);
    }
    /*
     * Metodo de completar datos al momento de seleccionar lista principal
     * Se completa las dos lista de acuerdo a tipo de constante
     */

    public void completaListaConstante() throws SQLException {
        txt_idpersonal.setValue(objPrincipal.getCodigo_vista());
        txt_despersonal.setValue(objPrincipal.getPaterno() + " " + objPrincipal.getMaterno() + " " + objPrincipal.getNombre());
        objlstMovLinea = null;
        objlstConstanteMensual = null;
        objlstMovLinea = objDaoMovLinea.buscarDetalle(objPrincipal.getCodigo_vista(), "C", objPrincipal.getSucursal());
        objlstConstanteMensual = objDaoMovLinea.buscarDetalle(objPrincipal.getCodigo_vista(), "M", objPrincipal.getSucursal());
        lst_constantemensual.setModel(objlstConstanteMensual);
        lst_constante.setModel(objlstMovLinea);

        // llenaAuditoria(objls);
        /*  for (int j = 0; j < objlstMovLinea.getSize(); j++) {
         if (objlstMovLinea.get(j).getTipo().equals("C")) {
         lst_constante.setModel(objlstMovLinea);

         } else {
         lst_constantemensual.setModel(objlstMovLinea);
         }
         }*/
    }
    /*
     *Metodo para completar datos de auditoria
     */

    public void llenaAuditoriaConstante() {
        txt_usua.setValue(objMovLinea.getUsu_add());
        txt_usumo.setValue(objMovLinea.getUsu_mod());
        d_fec.setValue(objMovLinea.getFecha_add());
        d_fecmo.setValue(objMovLinea.getFecha_mod());

    }
    /*
     *Metodo para completar datos de auditoria
     */
    /*  public void llenaAuditoriaConstanteMensual(){
     txt_usua.setValue(objMovLinea.getUsu_add());
     txt_usumo.setValue(objMovLinea.getUsu_mod());
     d_fec.setValue(objMovLinea.getFecha_add());
     d_fecmo.setValue(objMovLinea.getFecha_mod());
             
     }*/
}
