package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.procesos.InvFisicoCab;
import org.me.gj.model.logistica.procesos.InvFisicoDet;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import java.util.*;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerInvFisico extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Tab tab_listainvfiscab, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir, tbbtn_btn_buscar,
            tbbtn_btn_nuevodet, tbbtn_btn_editardet, tbbtn_btn_eliminardet, tbbtn_btn_guardardet, tbbtn_btn_deshacerdet;
    @Wire
    Datebox d_fecadd, d_fecmod;
    @Wire
    Listbox lst_invfiscab, lst_invfisico, lst_invfisdet;
    @Wire
    Intbox txt_almid, txt_almidbus, txt_entero, txt_fraccion, txt_estado,
            txt_fisidet, txt_index, txt_unipres;
    @Wire
    Longbox txt_prokey, txt_provkey;
    @Wire
    Textbox txt_grupid, txt_grupidbus, txt_respid, txt_almdesbus,
            txt_almdes, txt_usumod, txt_usuadd,
            txt_seleccion, txt_idubicacion, txt_ubicacion,
            txt_seleccionfocus,
            txt_proid, txt_prodes, txt_provid, txt_provdes;
    ;
    @Wire
    Combobox cb_perid, cb_peridbus;
    @Wire
    Button btn_consultar, btn_buscarinvfis, btn_agregar, btn_habilitar;
    //Instancias de Objetos
    ListModelList<InvFisicoCab> objlstInvFisicoCab, objlstInvFisico, objlstEliminacionCab;
    ListModelList<InvFisicoDet> objlstInvFisicoDet, objlstEliminacion, objlstModificacion;
    InvFisicoCab objInvFisico, objInvFisicoCab;
    InvFisicoDet objInvFisicoDet, objInvFisicoDetN;
    Accesos objAccesos;
    Ubicaciones objUbicaciones;
    DaoAccesos objDaoAccesos;
    DaoInvFisico objDaoInvFisico;
    DaoAlmacenes objDaoAlmacenes;
    DaoUbicaciones objDaoUbicaciones = new DaoUbicaciones();
    //Variables publicas    
    int emp_id, suc_id, i_ent, i_frac;
    int i_selCab;
    int i_sel;
    String s_mensaje;
    String campo = "";
    String modoEjecucion;
    String s_estado = "Q";
    String s_estadodet = "Q";
    String foco = "";
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerInvFisico.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoInvFisico = new DaoInvFisico();
        objlstInvFisico = new ListModelList<InvFisicoCab>();
        objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
        objlstEliminacion = new ListModelList<InvFisicoDet>();
        objInvFisicoDet = new InvFisicoDet();
        objInvFisicoCab = new InvFisicoCab();
        objInvFisico = new InvFisicoCab();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        objlstInvFisicoCab = objDaoInvFisico.listaInvFisico("", emp_id, suc_id, "", 0);
        lst_invfiscab.setModel(objlstInvFisicoCab);
        ListModelList objlstManPer = new DaoManPer().listaPeriodos(1);
        objlstManPer.add(new ManPer("", ""));
        cb_peridbus.setModel(objlstManPer);
        cb_perid.setModel(new DaoManPer().listaPeriodos(1));
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10212010, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Procesos de Inventario Físico con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Procesos de Inventario Físico con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creación de un nuevo Inventario Fisico en Procesos");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Inventario Fisico en Procesos");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edición de un Inventario Fisico en Procesos");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edición de un Inventario Fisico en Procesos");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminación de un Inventario Fisico en Procesos");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminación de un Inventario Fisico en Procesos");
        }
//        if (objAccesos.getAcc_imp() == 1) {
//            tbbtn_btn_imprimir.setDisabled(false);
//            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Inventario Fisico");
//        } else {
//            tbbtn_btn_imprimir.setDisabled(true);
//            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Inventario Fisico");
//        }
    }

    @Listen("onSelect=#lst_invfiscab")
    public void seleccionaGrupo() throws SQLException {
        objInvFisicoCab = (InvFisicoCab) lst_invfiscab.getSelectedItem().getValue();
        limpiarListaInvFisico(4);
        llenarCampos();
        objlstInvFisico = objDaoInvFisico.listaInvFisicoCab(emp_id, suc_id, objInvFisicoCab.getPer_key(), objInvFisicoCab.getAlm_key());
        //consultamos los registros ya guardados en stkfisco_det
        lst_invfisico.setModel(objlstInvFisico);
    }

    @Listen("onSelect=#lst_invfisico")
    public void seleccionaCabecera() throws SQLException {
//        int i = 0;
//        boolean valida = true;
        if (!s_estado.equals("N")) {
            String pro_id = ((InvFisicoCab) lst_invfisico.getSelectedItem().getValue()).getPro_id();
            objlstInvFisicoDet = objDaoInvFisico.listaInvFisicoDetxProd(emp_id, suc_id, objInvFisicoCab.getFisicab_id().toUpperCase(), objInvFisicoCab.getPer_key(), objInvFisicoCab.getAlm_key(), pro_id);
//        objlstModificacion = new ListModelList<InvFisicoDet>();
//        while (i < objlstInvFisicoDet.size() && valida) {
//            if (objlstInvFisicoDet.get(i).getPro_id().equals(pro_id) && objlstInvFisicoDet.get(i).getFisidet_est() == 1) {
//                objlstModificacion.add(objlstInvFisicoDet.get(i));
//                valida = false;
//            }
//            i++;
//       }
//        lst_invfisdet.setModel(objlstModificacion);
            lst_invfisdet.setModel(objlstInvFisicoDet);
            objInvFisico = (InvFisicoCab) lst_invfisico.getSelectedItem().getValue();
            limpiarCamposDetalle();
        }
    }

    @Listen("onSelect=#lst_invfisdet")
    public void seleccionaDetalle() {
        objInvFisicoDet = lst_invfisdet.getSelectedItem().getValue();
        llenarCamposDetalle();
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | selecciono el registro la ubicacion ").append(objInvFisicoDet.getUbic_id()).toString());
    }

    @Listen("onClick=#btn_buscarinvfis")
    public void consultarInventarios() throws SQLException {
        LimpiarLista();
        String grup_id = txt_grupidbus.getValue().toUpperCase();
        int alm_id = txt_almidbus.getValue() == null ? 0 : txt_almidbus.getValue();
        String per_id = (String) (cb_peridbus.getSelectedIndex() == -1 ? "" : cb_peridbus.getSelectedItem().getValue());
        //Limpiar tabla
        limpiarListaInvFisico(5);
        objlstInvFisicoCab = objDaoInvFisico.listaInvFisico(grup_id, emp_id, suc_id, per_id, alm_id);
        //Validar tabla sin registro
        if (objlstInvFisicoCab.getSize() > 0) {
            lst_invfiscab.setModel(objlstInvFisicoCab);
        } else {
            limpiarListaInvFisico(1);
            Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiarCamposDetalle();
    }

    @Listen("onClick=#btn_consultar")
    public void consultarProductos() throws SQLException {
        int per_id = cb_perid.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_perid.getSelectedItem().getValue().toString());
        int alm_key = txt_almid.getValue() == null ? 0 : txt_almid.getValue();
        String grupo = txt_grupid.getValue();
        limpiarListaInvFisico(4);

        objlstInvFisico = objDaoInvFisico.listaInvFisicoCab(emp_id, suc_id, per_id, alm_key);
        if (objlstInvFisico == null || objlstInvFisico.isEmpty()) {
            Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {

            lst_invfisico.setModel(objlstInvFisico);
            //consultamos tambien a la bd las ubicaciones por productos / almacen
            objlstInvFisicoDet = objDaoInvFisico.listaStkFisicoDet(emp_id, suc_id, grupo, per_id, alm_key);
            lst_invfisdet.setModel(objlstInvFisicoDet);
            //habilitaCamposBusqueda(false);
            //Utilitarios.deshabilitarLista(true, lst_invfisico);
        }

    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        habilitaBotones(false, true);
        habilitaBotonesDetalle(true, true);
        habilitaCampos(true);
        btn_consultar.setDisabled(false);
        limpiarListaInvFisico(4);
        habilitaTab(false, true);
        seleccionaTab(false, true);
        limpiarCampos();
        txt_grupid.setValue(objDaoInvFisico.generaGrupoInvFisico(emp_id, suc_id));
        txt_respid.focus();
        s_estado = "N";

    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_invfiscab.getSelectedIndex() == - 1) {
            Messagebox.show("Por favor seleccione un Inventario Fisico a Editar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_invfisico.getSelectedIndex() != -1) {
                //si tenemos seleccionado un registro en lst_invfisico
                //quitamos la seleccion y habilitamos la busqueda del producto
                lst_invfisico.setSelectedIndex(-1);
                lst_invfisdet.setModel(new ListModelList<InvFisicoDet>());
                Utilitarios.deshabilitarLista(true, lst_invfisico);
                txt_seleccion.setDisabled(false);
                btn_consultar.setDisabled(true);
                //habilitamos los botones de detalle y cambiamos el de cabecera
                habilitaBotones(false, true);
                habilitaTab(false, true);
                seleccionaTab(false, true);
                habilitaBotonesDetalle(true, true);
                s_estado = "M";
                txt_seleccion.focus();
            } else {
                //si tenemos seleccionado un registro en lst_invfisico
                //quitamos la seleccion y habilitamos la busqueda del producto
                Utilitarios.deshabilitarLista(true, lst_invfisico);
                txt_seleccion.setDisabled(false);
                btn_consultar.setDisabled(true);
                //habilitamos los botones de detalle y cambiamos el de cabecera
                habilitaBotones(false, true);
                habilitaTab(false, true);
                seleccionaTab(false, true);
                habilitaBotonesDetalle(true, true);
                s_estado = "M";
                txt_seleccion.focus();
            }

//            habilitaBotones(false, true);
//            habilitaTab(false, true);
//            seleccionaTab(false, true);
//            s_estado = "M";
//            objlstEliminacion = null;
//            objlstEliminacion = new ListModelList();
//            //verificamos si tenemos seleccionado algun registro de inventario fisico
//            txt_seleccion.setDisabled(false);
//            lst_invfisico.setFocus(true);
//            if (lst_invfisico.getSelectedIndex() == -1) {
//                habilitaBotonesDetalle(false, true);
//                txt_seleccion.focus();
//            } else {
//                habilitaBotonesDetalle(false, true);
//                //verificamos si hay datos en la lista detalle
//                //si existe datos entonces seleccionamos por defecto el primero y editamos la seleccion
//                if (objlstInvFisicoDet.getSize() > 0) {
//                    int i = 0;
//                    while (i < objlstInvFisicoDet.size()) {
//                        lst_invfisdet.setSelectedIndex(i);
//                        if (lst_invfisdet.getSelectedItem().getValue() != null) {
//                            objInvFisicoDet = (InvFisicoDet) lst_invfisdet.getSelectedItem().getValue();
//                            llenarCamposDetalle();
//                            botonEditarDetalle();
//                        }
//                        break;
//                    }
//                    i++;
//                } else {
//                    botonNuevoDetalle();
//                }
//            }
        }
    }
//Eventos Secundarios - Validacion

    @Listen("onCtrlKey=#w_manprocordcomp")
    public void GuardarInformacion(Event event) throws SQLException {
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
           /* case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;*/

            case 78:
                if (!tbbtn_btn_nuevodet.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
            case 77:
                if (!tbbtn_btn_editardet.isDisabled()) {
                    botonEditarDetalle();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminardet.isDisabled()) {
                    botonEliminarDetalle();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardardet.isDisabled()) {
                    botonGuardarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacerdet.isDisabled()) {
                    botonDeshacerDetalle();
                }
                break;
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_invfiscab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un Inventario Fisico a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Esta Seguro que desea Eliminar el Inventario Fisico";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objParamSalida = objDaoInvFisico.eliminarInventario(objInvFisicoCab);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    limpiarCampos();
                                    habilitaBotones(true, false);
                                    habilitaTab(true, true);
                                    seleccionaTab(true, false);
                                    btn_buscarinvfis.setDisabled(false);
                                    consultarInventarios();
                                }
                                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Eliminado un Registro");
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        Messagebox.show("Esta Seguro que desea deshacer los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            limpiarCampos();
                            limpiarCamposDetalle();
                            habilitaBotones(true, false);
                            habilitaBotonesDetalle(true, true);
                            habilitaTab(true, true);
                            seleccionaTab(true, false);
                            habilitaCampos(false);
                            habilitaCamposDetalle(true);
                            btn_buscarinvfis.setDisabled(false);
                            btn_habilitar.setDisabled(true);
                            btn_agregar.setDisabled(true);
                            consultarInventarios();
                            s_estado = "Q";
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        lst_invfisico.setFocus(true);
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Esta Seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objlstInvFisicoDet.addAll(objlstEliminacion);
                                objInvFisico = (InvFisicoCab) generaRegistro();
                                if (s_estado.equals("N")) {
                                    objParamSalida = objDaoInvFisico.insertarInventario(objInvFisico, generaDetalle(objlstInvFisicoDet));
                                } else {
                                    objParamSalida = objDaoInvFisico.modificarInventario(objInvFisico, generaDetalle(objlstInvFisicoDet));
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    //validacion de guardar/nuevo
                                    limpiarListaInvFisico(5);
                                    objlstEliminacion.clear();
                                    habilitaBotones(true, false);
                                    habilitaBotonesDetalle(true, true);
                                    habilitaTab(true, true);
                                    seleccionaTab(true, false);
                                    habilitaCampos(false);
                                    habilitaCamposDetalle(true);
                                    consultarInventarios();
                                    s_estado = "Q";
                                    s_estadodet = "Q";
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevodet")
    public void botonNuevoDetalle() throws SQLException {
        String verifica = verificaSeleccion();
        if (!verifica.isEmpty()) {
            Messagebox.show(verifica, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("seleccion")) {
                            txt_seleccion.focus();
                        }
                    }
                }
            });
        } else {
            lst_invfisdet.setSelectedIndex(-1);
            habilitaCamposDetalle(false);
            habilitaBotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_invfisdet);
            limpiarCamposDetalle();
            txt_seleccion.setDisabled(true);
            btn_agregar.setDisabled(true);
            txt_idubicacion.focus();
            s_estadodet = "N";
            llenarCamposDetalleN();
        }
    }

    @Listen("onClick=#tbbtn_btn_editardet")
    public void botonEditarDetalle() throws SQLException {
        if (lst_invfisdet.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto del detalle de Inventario fisico", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            s_estadodet = "M";
            habilitaCamposDetalle(false);
            txt_idubicacion.setDisabled(true);
            habilitaBotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_invfisico);
            Utilitarios.deshabilitarLista(true, lst_invfisdet);
            habilitaCamposBusqueda(true);
            txt_entero.focus();
            txt_entero.select();
            btn_agregar.setDisabled(true);
            btn_habilitar.setDisabled(true);
        }
        LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | pulso la opcion editar para modificar un registro en el detalle del inventario fisico").toString());
    }

    @Listen("onClick=#tbbtn_btn_guardardet")
    public void botonGuardarDetalle() throws SQLException {
        //Verificar ingreso del detalle del inventario
        String validaProducto = verificarDetalle();
        //Si algun campo del detalle no se ha ingresado
        if (!validaProducto.isEmpty()) {
            Messagebox.show("Por favor ingrese valores en el campo " + validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("codigo")) {
                            txt_idubicacion.focus();
                        } else if (foco.equals("entero")) {
                            txt_entero.focus();
                        } else if (foco.equals("fraccion")) {
                            txt_fraccion.focus();
                        }
                    }
                }
            });
        } else {
            //Si se agrega un item al detalle
            //Validar si el producto ya esta ingresado 
            if ("N".equals(s_estadodet)) {
                objInvFisicoDet = (InvFisicoDet) generaDetalle();
                //objlstInvFisicoDet.add(objInvFisicoDet);
            } else {
                objInvFisicoDet = (InvFisicoDet) generaDetalle();
                objlstInvFisicoDet.set(objInvFisicoDet.getIndex(), objInvFisicoDet);
            }
            Messagebox.show("Esta Seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                if (s_estadodet.equals("N")) {
                                    //objParamSalida = objDaoInvFisico.insertarInventarioDet(objInvFisicoCab, generaDetalle(objlstInvFisicoDet));
                                    objParamSalida = objDaoInvFisico.insertarInventarioDet(objInvFisicoDet);
                                } else {
                                    objParamSalida = objDaoInvFisico.modificarInventarioDet(objInvFisicoCab, objInvFisicoDet.getPro_id(), generaDetalle(objlstInvFisicoDet));
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    //lst_invfisdet.setModel(objlstInvFisicoDet);
                                    lst_invfisdet.setModel(new ListModelList<InvFisicoDet>());
                                    lst_invfisdet.clearSelection();
                                    //lst_invfisdet.setModel(objlstInvFisicoDet);
                                    busquedaProducto();
                                    btn_agregar.setDisabled(true);
                                    btn_habilitar.setDisabled(false);
                                    habilitaBotonesDetalle(false, true);
                                    habilitaCamposDetalle(true);
                                    limpiarCamposDetalle();
                                    s_estadodet = "Q";
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            } else {
                                txt_entero.select();
                                txt_entero.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacerdet")
    public void botonDeshacerDetalle() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    //VerificarTransacciones();                    
                    habilitaBotonesDetalle(false, true);
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    Utilitarios.deshabilitarLista(false, lst_invfisdet);
                    //Utilitarios.deshabilitarLista(false, lst_invfisico);
                    lst_invfisdet.clearSelection();
                    btn_agregar.setDisabled(true);
                    btn_habilitar.setDisabled(false);
                    s_estadodet = "Q";
                }
                lst_invfisdet.focus();
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_eliminardet")
    public void botonEliminarDetalle() throws SQLException {
        if (objlstInvFisicoDet.isEmpty()) {
            Messagebox.show("No hay productos en ubicaciones disponibles", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else if (lst_invfisdet.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto en ubicacion a eliminar", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
        } else {
            Messagebox.show("Esta Seguro que desea eliminar este registro?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objParamSalida = objDaoInvFisico.eliminarInventarioDet(objInvFisicoDet);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    //lst_invfisdet.setModel(objlstInvFisicoDet);
                                    lst_invfisdet.setModel(new ListModelList<InvFisicoDet>());
                                    lst_invfisdet.clearSelection();
                                    busquedaProducto();
                                    //lst_invfisdet.setModel(objlstInvFisicoDet);
                                    btn_agregar.setDisabled(true);
                                    habilitaBotonesDetalle(false, true);
                                    habilitaCamposDetalle(true);
                                    limpiarCamposDetalle();
                                    s_estadodet = "Q";
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_almidbus")
    public void lovAlmacenes() throws SQLException {
        if (txt_almidbus.getValue() == null) {
            Map objMapObjetos = new HashMap();
            modoEjecucion = "mantInvFis";
            objMapObjetos.put("txt_almid", txt_almidbus);
            objMapObjetos.put("txt_almdes", txt_almdesbus);
            objMapObjetos.put("tipo", "1");
            objMapObjetos.put("validaBusqueda", modoEjecucion);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAlmacenes.zul", null, objMapObjetos);
            window.doModal();
        } else {
            cb_peridbus.focus();
        }
    }

    @Listen("onBlur=#txt_almidbus")
    public void validaAlmacenes() throws SQLException {
        if (txt_almidbus.getValue() != null) {
            if (!txt_almidbus.getValue().toString().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_almidbus.setValue(null);
                                    txt_almdesbus.setValue("");
                                    txt_almidbus.focus();
                                }
                            }
                        });
            } else {
                int alm_id = txt_almidbus.getValue();
                Almacenes objAlmacenes = (new DaoAlmacenes()).getNomAlmacenes(alm_id);
                if (objAlmacenes == null) {
                    Messagebox.show("El código de almacen no existe o está eliminado", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_almidbus.setValue(null);
                                        txt_almdesbus.setValue("");
                                        txt_almidbus.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objAlmacenes.getAlm_id() + " y ha encontrado 1 registro(s)");
                    txt_almidbus.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
                    txt_almdesbus.setValue(objAlmacenes.getAlm_des());
                }
            }
        }
    }

    @Listen("onOK=#txt_almid")
    public void lovAlmID() throws SQLException {
        if (txt_almid.getValue() == null) {
            Map objMapObjetos = new HashMap();
            modoEjecucion = "mantInvFis";
            objMapObjetos.put("txt_almid", txt_almid);
            objMapObjetos.put("txt_almdes", txt_almdes);
            objMapObjetos.put("tipo", "1");
            objMapObjetos.put("validaBusqueda", modoEjecucion);
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAlmacenes.zul", null, objMapObjetos);
            window.doModal();
        } else {
            cb_peridbus.focus();
        }
    }

    @Listen("onOK=#txt_almid")
    public void validaOkAlmID() throws SQLException {
        if (txt_almid.getValue() != null) {
            if (!txt_almid.getValue().toString().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_almid.setValue(null);
                                    txt_almdes.setValue("");
                                    txt_almid.focus();
                                }
                            }
                        });
            } else {
                int alm_id = txt_almid.getValue();
                Almacenes objAlmacenes = (new DaoAlmacenes()).getNomAlmacenes(alm_id);
                if (objAlmacenes == null) {
                    cb_perid.setFocus(true);
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objAlmacenes.getAlm_id() + " y ha encontrado 1 registro(s)");
                    txt_almid.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
                    txt_almdes.setValue(objAlmacenes.getAlm_des());
                    consultarProductos();
                    txt_seleccion.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_almid")
    public void validaBlurAlmID() throws SQLException {
        if (txt_almid.getValue() != null) {
            int alm_id = txt_almid.getValue();
            Almacenes objAlmacenes = (new DaoAlmacenes()).getNomAlmacenes(alm_id);
            if (objAlmacenes == null) {
                Messagebox.show("El código de almacen no existe o está eliminado", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_almid.setValue(null);
                                    txt_almdes.setValue("");
                                    txt_almid.focus();
                                }
                            }
                        });
            } else {
                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objAlmacenes.getAlm_id() + " y ha encontrado 1 registro(s)");
                txt_almid.setValue(Integer.parseInt(objAlmacenes.getAlm_id()));
                txt_almdes.setValue(objAlmacenes.getAlm_des());
            }
        }
    }

    @Listen("onOK=#txt_idubicacion")
    public void lov_ubicaciones() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_idubicacion.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "InvFisico";
                objMapObjetos.put("txt_ubicod", txt_idubicacion);
                objMapObjetos.put("txt_ubides", txt_ubicacion);
                objMapObjetos.put("txt_entero", txt_entero);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerInvFisico");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbicacion.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_entero.focus();
            }
        }
    }

    @Listen("onBlur=#txt_idubicacion")
    public void valida_ubicaciones() throws SQLException {
        if (!txt_idubicacion.getValue().isEmpty()) {
            if (!txt_idubicacion.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_idubicacion.setValue("");
                                    txt_ubicacion.setValue("");
                                    txt_idubicacion.focus();
                                }
                            }
                        });
            } else {
                String codigo = Utilitarios.lpad(txt_idubicacion.getValue(), 4, "0");
                objUbicaciones = (new DaoUbicaciones()).listauUbi(codigo);
                if (objUbicaciones == null) {
                    s_mensaje = "La ubicación no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_idubicacion.setValue("");
                                        txt_ubicacion.setValue("");
                                        txt_idubicacion.focus();
                                    }
                                }
                            });
                } else {
                    txt_idubicacion.setValue(codigo);
                    txt_ubicacion.setValue(objDaoUbicaciones.descripcionUbicacion(txt_idubicacion.getValue()));
                }
            }
        } else {
            txt_ubicacion.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_grupid")
    public void validaGrupo() {
        if (!txt_grupid.getValue().isEmpty()) {
            txt_respid.focus();
        } else {
            Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_grupid.setValue("");
                                txt_grupid.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onOK=#txt_respid")
    public void validaResponsable() {
        if (!txt_respid.getValue().isEmpty()) {
            cb_perid.focus();
        } else {
            Messagebox.show("Por favor Ingrese Datos", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_respid.setValue("");
                                txt_respid.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onOK=#cb_perid")
    public void validaPeriodo() {
        if (cb_perid.getSelectedIndex() != -1) {
            txt_almid.focus();
        } else {
            Messagebox.show("Por Favor seleccione Periodo ", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                cb_perid.focus();
                            }
                        }
                    });
        }
    }

    @Listen("onCtrlKey=#w_procinvfis")
    public void ctrl_teclado(Event event) throws SQLException {
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
        }
    }

    @Listen("onOK=#txt_seleccion;onBlur=#txt_seleccion")
    public void busquedaProducto() throws SQLException {
        String verifica = verificaSeleccion();
        if (!verifica.isEmpty()) {
            Messagebox.show(verifica, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("seleccion")) {
                            txt_seleccion.focus();
                        }
                    }
                }
            });
        } else {

            objlstInvFisicoDet = objDaoInvFisico.listaInvFisicoDetxProd(emp_id, suc_id, objInvFisicoCab.getFisicab_id().toUpperCase(), objInvFisicoCab.getPer_key(), objInvFisicoCab.getAlm_key(), txt_seleccion.getValue());
            if (objlstInvFisicoDet.getSize() > 0) {
                lst_invfisdet.setModel(objlstInvFisicoDet);
                btn_agregar.setDisabled(false);
                btn_agregar.focus();
            } else {
                Messagebox.show("No se encontro ningun producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                btn_agregar.setDisabled(true);
                limpiarCamposDetalle();
                lst_invfisdet.setModel(new ListModelList<InvFisicoDet>());
                lst_invfisdet.clearSelection();
                txt_seleccion.focus();
            }
        }
    }

    @Listen("onClick=#btn_agregar")
    public void busquedaUbicacion() throws SQLException {
        habilitaBotonesDetalle(false, true);
        txt_seleccion.setDisabled(true);
        btn_agregar.setDisabled(true);
        btn_habilitar.setDisabled(false);
    }

    @Listen("onClick=#btn_habilitar")
    public void habilitarDetalle() throws SQLException {
        txt_seleccion.setValue("");
        limpiarCamposDetalle();
        lst_invfisdet.setModel(new ListModelList<InvFisicoDet>());
        lst_invfisdet.clearSelection();
        habilitaBotonesDetalle(true, true);
        txt_seleccion.setDisabled(false);
        btn_habilitar.setDisabled(true);
        btn_agregar.setDisabled(false);
        txt_seleccion.focus();
    }

    @Listen("onOK=#txt_entero")
    public void validaEntero() {
        txt_fraccion.focus();
    }

    @Listen("onOK=#txt_fraccion")
    public void validaFraccion() throws SQLException {
        botonGuardarDetalle();
    }

    @Listen("onBlur=#txt_entero")
    public void validacalculoEnt() throws SQLException {
        if (txt_entero.getValue() != null) {
            if (txt_fraccion.getValue() == null) {
                txt_fraccion.setValue(0);
            }
            if (txt_entero.getValue() < 0) {
                txt_entero.setValue(txt_entero.getValue() * -1);
            }
            if (txt_entero.getValue() != 0) {
                int unipresven = txt_unipres.getValue();
                double decimal = Double.parseDouble(df2.format(txt_entero.getValue() % 1));
                if (decimal != 0.0) {
                    int sindec = (int) Math.round(txt_entero.getValue() - decimal);
                    int frac = (int) (decimal * unipresven);
                    txt_fraccion.setValue(frac);
                    txt_entero.setValue(sindec);
                }
            }

        } else {
            txt_entero.setValue(0);
        }
    }

    @Listen("onBlur=#txt_fraccion")
    public void validacalculoFrac() throws SQLException {
        if (txt_fraccion.getValue() != null) {
            if (txt_entero.getValue() == null) {
                txt_entero.setValue(0);
            }
            if (txt_fraccion.getValue() != 0) {
                int unipresven = txt_unipres.getValue();
                int canfrac = txt_fraccion.getValue();
                int canent = (int) Math.round(txt_entero.getValue());
                txt_fraccion.setValue(canfrac >= unipresven ? canfrac % unipresven : canfrac);
                txt_entero.setValue(canent + Math.round(canfrac / unipresven));
            }

        } else {
            txt_fraccion.setValue(0);
        }
    }

    //Eventos Otros 
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_mantenimiento.setSelected(!b_valida1);
        tab_listainvfiscab.setSelected(!b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listainvfiscab.setDisabled(!b_valida1);
        tab_mantenimiento.setDisabled(!b_valida2);
    }

    public Object generaRegistro() {
        String s_fisicab_id = txt_grupid.getText().toUpperCase();
        int i_alm_key = txt_almid.getValue();
        int i_per_key = Integer.parseInt(cb_perid.getSelectedItem().getValue().toString());
        String s_per_id = cb_perid.getValue();
        String s_fisicab_respon = txt_respid.getValue().toUpperCase();
        String s_fisicab_usuadd = objUsuarioCredential.getCuenta();
        String s_fisicab_usumod = objUsuarioCredential.getCuenta();
        String s_fisicab_pcadd = objUsuarioCredential.getComputerName();
        String s_fisicab_pcmod = objUsuarioCredential.getComputerName();
        return new InvFisicoCab(s_fisicab_id, emp_id, suc_id, s_per_id, i_per_key, i_alm_key, s_fisicab_respon, 1, s_fisicab_usuadd, s_fisicab_pcadd, s_fisicab_usumod, s_fisicab_pcmod);
    }

    public Object generaDetalle() throws SQLException {
        String s_fisicab_id = "", s_alm_id = "", s_per_id, s_ubi_des, s_fisicab_usuadd, s_fisicab_pcadd, s_pro_id, s_pro_des, s_prov_id, s_prov_des, s_ubi_id;
        int i_alm_key, i_per_key, fisidet_key, i_ubi_key, i_u_pres, l_entero, l_fraccion, l_cantidad, i_est_inv, index = 0;
        long l_pro_key, l_prov_key;
        if ("N".equals(s_estadodet)) {
            //si es nuevo registro
            index = txt_index.getValue() == null ? 0 : txt_index.getValue();
            objInvFisicoDetN = new InvFisicoDet();
            objInvFisicoDetN = objDaoInvFisico.agregaDetalleStkFisicoDet(txt_seleccion.getValue());
            s_fisicab_id = txt_grupid.getText().toUpperCase();
            i_alm_key = txt_almid.getValue();
            s_alm_id = Utilitarios.lpad(String.valueOf(txt_almid.getValue()), 4, "0");
            s_per_id = cb_perid.getValue();
            i_per_key = Integer.parseInt(cb_perid.getSelectedItem().getValue().toString());
            fisidet_key = txt_fisidet.getValue() == null ? 1 : txt_fisidet.getValue();
            s_ubi_id = txt_idubicacion.getValue();
            i_ubi_key = Integer.parseInt(s_ubi_id);
            s_ubi_des = txt_ubicacion.getValue();
            s_fisicab_usuadd = objUsuarioCredential.getCuenta();
            s_fisicab_pcadd = objUsuarioCredential.getComputerName();
            i_u_pres = txt_unipres.getValue();
            //i_u_pres = objInvFisicoDetN.getPro_presminven();
            l_entero = txt_entero.getValue();
            l_fraccion = txt_fraccion.getValue();
            l_cantidad = (l_entero * i_u_pres) + l_fraccion;
//            l_pro_key = objInvFisicoDetN.getPro_key();
//            s_pro_id = objInvFisicoDetN.getPro_id();
//            s_pro_des = objInvFisicoDetN.getPro_des();
//            s_prov_id = objInvFisicoDetN.getProv_id();
//            l_prov_key = objInvFisicoDetN.getProv_key();
//              s_prov_des = objInvFisicoDetN.getProv_razsoc();  
            l_pro_key = txt_prokey.getValue();
            s_pro_id = txt_proid.getValue();
            s_pro_des = txt_prodes.getValue();
            s_prov_id = txt_provid.getValue();
            l_prov_key = txt_provkey.getValue();
            s_prov_des = txt_provdes.getValue();

            i_est_inv = 1;
        } else {
            //si es moficacion
            index = txt_index.getValue() == null ? 0 : txt_index.getValue();
            s_fisicab_id = txt_grupid.getText().toUpperCase();
            i_alm_key = txt_almid.getValue();
            s_alm_id = Utilitarios.lpad(String.valueOf(txt_almid.getValue()), 4, "0");
            s_per_id = cb_perid.getValue();
            i_per_key = Integer.parseInt(cb_perid.getSelectedItem().getValue().toString());
            fisidet_key = txt_fisidet.getValue() == null ? 1 : txt_fisidet.getValue();
            s_ubi_id = txt_idubicacion.getValue();
            i_ubi_key = Integer.parseInt(s_ubi_id);
            s_ubi_des = txt_ubicacion.getValue();
            s_fisicab_usuadd = objUsuarioCredential.getCuenta();
            s_fisicab_pcadd = objUsuarioCredential.getComputerName();
            i_u_pres = txt_unipres.getValue();
            l_entero = txt_entero.getValue();
            l_fraccion = txt_fraccion.getValue();
            l_cantidad = (l_entero * i_u_pres) + l_fraccion;
            l_pro_key = txt_prokey.getValue();
            s_pro_id = txt_proid.getValue();
            s_pro_des = txt_prodes.getValue();
            s_prov_id = txt_provid.getValue();
            l_prov_key = txt_provkey.getValue();
            s_prov_des = txt_provdes.getValue();
            i_est_inv = txt_estado.getValue() == null ? 0 : txt_estado.getValue();
        }
        return new InvFisicoDet(index, s_fisicab_id, emp_id, suc_id, i_per_key, s_per_id, i_alm_key, s_alm_id, fisidet_key,
                l_pro_key, s_pro_id, s_pro_des, i_u_pres, l_entero, l_fraccion, l_cantidad, l_prov_key, s_prov_id, s_prov_des,
                i_ubi_key, s_ubi_id, s_ubi_des, i_est_inv, s_fisicab_usuadd, s_fisicab_pcadd);
    }

    public ListModelList<InvFisicoDet> detalleAuxiliar() {
        int i = 0;
        boolean valida = true;
        String pro_id = ((InvFisicoCab) lst_invfisico.getSelectedItem().getValue()).getPro_id();
        objlstModificacion = null;
        objlstModificacion = new ListModelList<InvFisicoDet>();
        while (i < objlstInvFisicoDet.size() && valida) {
            if (objlstInvFisicoDet.get(i).getPro_id().equals(pro_id) && objlstInvFisicoDet.get(i).getFisidet_est() == 1) {
                objlstModificacion.add(objlstInvFisicoDet.get(i));
                valida = false;
            }
            i++;
        }
        return objlstModificacion;
    }

    public Object[][] generaDetalle(ListModelList<InvFisicoDet> objlistaDetalles) {
//        if (s_estado.equals("M")) {
//            objlstTrans = new ListModelList<InvFisicoDet>();
//            for (int i = 0; i < objlistaDetalles.size(); i++) {
//                if (!objlistaDetalles.get(i).getInd_accion().equals("Q")) {
//                    objlstTrans.add(objlistaDetalles.get(i));
//                }
//            }
//            if (!objlstEliminacion.isEmpty()) {
//                for (int i = 0; i < objlstEliminacion.size(); i++) {
//                    if (objlstEliminacion.get(i).getInd_accion().equals("E")) {
//                        objlstTrans.add(objlstEliminacion.get(i));
//                    }
//                }
//            }
//        } else {
//            if (!objlstEliminacion.isEmpty()) {
//                for (int i = 0; i < objlstEliminacion.size(); i++) {
//                    if (objlstEliminacion.get(i).getInd_accion().equals("E")) {
//                        objlistaDetalles.add(objlstEliminacion.get(i));
//                    }
//                }
//            }
//            objlstTrans = objlistaDetalles;
//        }
        Object[][] listaContactos = new Object[objlstInvFisicoDet.size()][20];
        for (int i = 0; i < objlstInvFisicoDet.size(); i++) {
            listaContactos[i][0] = objlstInvFisicoDet.get(i).getFisicab_id();
            listaContactos[i][1] = objlstInvFisicoDet.get(i).getEmp_id();
            listaContactos[i][2] = objlstInvFisicoDet.get(i).getSuc_id();
            listaContactos[i][3] = objlstInvFisicoDet.get(i).getAlm_key();
            listaContactos[i][4] = objlstInvFisicoDet.get(i).getPer_id();
            listaContactos[i][5] = objlstInvFisicoDet.get(i).getPer_key();
            listaContactos[i][6] = objlstInvFisicoDet.get(i).getFisidet_key();
            listaContactos[i][7] = objlstInvFisicoDet.get(i).getUbic_key();
            listaContactos[i][8] = objlstInvFisicoDet.get(i).getPro_id();
            listaContactos[i][9] = objlstInvFisicoDet.get(i).getPro_key();
            listaContactos[i][10] = objlstInvFisicoDet.get(i).getProv_key();
            listaContactos[i][11] = objlstInvFisicoDet.get(i).getFisidet_cant();
            listaContactos[i][12] = objlstInvFisicoDet.get(i).getFisidet_est();
            listaContactos[i][13] = objlstInvFisicoDet.get(i).getFisidet_usuadd();
            listaContactos[i][14] = objlstInvFisicoDet.get(i).getFisidet_fecadd();
            listaContactos[i][15] = objlstInvFisicoDet.get(i).getFisidet_pcadd();
            listaContactos[i][16] = objlstInvFisicoDet.get(i).getFisidet_usumod();
            listaContactos[i][17] = objlstInvFisicoDet.get(i).getFisidet_fecmod();
            listaContactos[i][18] = objlstInvFisicoDet.get(i).getFisidet_pcmod();
            listaContactos[i][19] = objlstInvFisicoDet.get(i).getInd_accion();
        }
        return listaContactos;
    }

    public String verificaSeleccion() {
        String mensaje;
        if (txt_seleccion.getValue().isEmpty()) {
            mensaje = "Debe ingresar valores para buscar producto";
            campo = "seleccion";
        } else if (!txt_seleccion.getValue().matches("[0-9]*")) {
            mensaje = "Debe ingresar valores para buscar producto";
            campo = "seleccion";
        } else if (txt_seleccion.getValue().length() != 9) {
            mensaje = "Tiene que ingresar 9 digitos";
            campo = "seleccion";
        } else {
            mensaje = "";
        }
        return mensaje;
    }

    public String verificar() {
        String s_valor;
        if (txt_grupid.getValue().isEmpty()) {
            s_valor = "El campo Grupo es obligatorio";
        } else if (txt_grupid.getText().matches("^\\s||^\\s+")) {
            s_valor = "El Campo Grupo no debe tener espacio en blanco al principio";
        } else if (txt_grupid.getValue().length() < 3) {
            s_valor = "El Campo Grupo debe tener 3 caracteres";
        } else if (txt_respid.getValue().isEmpty()) {
            s_valor = "El campo Resp es obligatorio";
        } else if (txt_respid.getText().matches("^\\s||^\\s+")) {
            s_valor = "El Campo Resp no debe tener espacio en blanco al principio";
        } else if (cb_perid.getSelectedIndex() == -1) {
            s_valor = "El campo Periodo es obligatorio";
        } else if (txt_almid.getValue() == null) {
            s_valor = "El campo Almacen es obligatorio";
        } else if (objlstInvFisico.isEmpty()) {
            s_valor = "No puede guardar 'Inventario Fisico' con Detalle Vacio";
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public String verificarDetalle() {
        String s_valor = "";
        if (txt_idubicacion.getValue().isEmpty()) {
            s_valor = "'Código de Ubicacion'";
            foco = "codigo";
        } else if (txt_ubicacion.getValue().isEmpty()) {
            s_valor = "'Ubicacion'";
            foco = "codigo";
        } else if (txt_entero.getValue() == null || txt_entero.getValue() < 0) {
            s_valor = "'Cantidad Entera' - Debe ser mayor a 0";
            foco = "entero";
        } else if (txt_fraccion.getValue() == null || txt_fraccion.getValue() < 0) {
            s_valor = "'Cantidad Fraccion' - Debe ser mayor o igual 0";
            foco = "fraccion";
        }
        return s_valor;
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstInvFisicoCab = null;
        objlstInvFisicoCab = new ListModelList<InvFisicoCab>();
        lst_invfiscab.setModel(objlstInvFisicoCab);
    }

    public void limpiarCampos() {
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
        //Lista
        txt_grupidbus.setValue("");
        txt_almidbus.setValue(null);
        txt_almdesbus.setValue("");
        cb_peridbus.setSelectedIndex(-1);
        //Mantenimiento
        txt_almdes.setValue("");
        txt_almid.setText(null);
        txt_grupid.setValue("");
        txt_respid.setText("");
        cb_perid.setSelectedIndex(-1);
        //busqueda
        txt_seleccion.setValue("");
    }

    public void limpiarCamposDetalle() {
        txt_index.setValue(null);
        txt_fisidet.setValue(null);
        txt_proid.setValue("");
        txt_prokey.setValue(null);
        txt_prodes.setValue("");
        txt_unipres.setValue(null);
        txt_provid.setValue("");
        txt_provkey.setValue(null);
        txt_provdes.setValue("");
        txt_idubicacion.setValue("");
        txt_ubicacion.setValue("");
        txt_entero.setValue(null);
        txt_fraccion.setValue(null);
        txt_estado.setValue(null);

    }

    public void limpiarListaInvFisico(int inv) {
        if (inv == 1) {
            objlstInvFisico = null;
            objlstInvFisico = new ListModelList<InvFisicoCab>();
            lst_invfisico.setModel(objlstInvFisico);
        } else if (inv == 2) {
            objlstInvFisicoCab = null;
            objlstInvFisicoCab = new ListModelList<InvFisicoCab>();
            lst_invfiscab.setModel(objlstInvFisicoCab);
        } else if (inv == 3) {
            objlstInvFisicoDet = null;
            objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
            lst_invfisdet.setModel(objlstInvFisicoDet);
        } else if (inv == 4) {
            objlstInvFisico = null;
            objlstInvFisico = new ListModelList<InvFisicoCab>();
            lst_invfisico.setModel(objlstInvFisico);
            objlstInvFisicoDet = null;
            objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
            lst_invfisdet.setModel(objlstInvFisicoDet);
        } else {
            objlstInvFisico = null;
            objlstInvFisico = new ListModelList<InvFisicoCab>();
            lst_invfisico.setModel(objlstInvFisico);
            objlstInvFisicoDet = null;
            objlstInvFisicoDet = new ListModelList<InvFisicoDet>();
            lst_invfisdet.setModel(objlstInvFisicoDet);
            objlstInvFisicoCab = null;
            objlstInvFisicoCab = new ListModelList<InvFisicoCab>();
            lst_invfiscab.setModel(objlstInvFisicoCab);
        }
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenarCampos() {
        txt_grupid.setValue(objInvFisicoCab.getFisicab_id());
        txt_almid.setValue(objInvFisicoCab.getAlm_key());
        txt_almdes.setValue(objInvFisicoCab.getAlm_des());
        txt_respid.setValue(objInvFisicoCab.getFisicab_respon());
        cb_perid.setSelectedItem(Utilitarios.valorPorTexto1(cb_perid, objInvFisicoCab.getPer_key()));
        txt_usuadd.setValue(objInvFisicoCab.getFisicab_usuadd());
        d_fecadd.setValue(objInvFisicoCab.getFisicab_fecadd());
        txt_usumod.setValue(objInvFisicoCab.getFisicab_usumod());
        d_fecmod.setValue(objInvFisicoCab.getFisicab_fecmod());
    }

    public void llenarCamposDetalleN() throws SQLException {
        objInvFisicoDetN = new InvFisicoDet();
        objInvFisicoDetN = objDaoInvFisico.agregaDetalleStkFisicoDet(txt_seleccion.getValue());
        txt_proid.setValue(objInvFisicoDetN.getPro_id());
        txt_prokey.setValue(objInvFisicoDetN.getPro_key());
        txt_prodes.setValue(objInvFisicoDetN.getPro_des());
        txt_unipres.setValue(objInvFisicoDetN.getPro_presminven());
        txt_provid.setValue(objInvFisicoDetN.getProv_id());
        txt_provkey.setValue(objInvFisicoDetN.getProv_key());
        txt_provdes.setValue(objInvFisicoDetN.getProv_razsoc());
    }

    public void llenarCamposDetalle() {
        txt_index.setValue(lst_invfisdet.getSelectedIndex());
        txt_fisidet.setValue(objInvFisicoDet.getFisidet_key());
        txt_proid.setValue(objInvFisicoDet.getPro_id());
        txt_prokey.setValue(objInvFisicoDet.getPro_key());
        txt_prodes.setValue(objInvFisicoDet.getPro_des());
        txt_unipres.setValue(objInvFisicoDet.getPro_presminven());
        txt_provid.setValue(objInvFisicoDet.getProv_id());
        txt_provkey.setValue(objInvFisicoDet.getProv_key());
        txt_provdes.setValue(objInvFisicoDet.getProv_razsoc());
        txt_idubicacion.setValue(objInvFisicoDet.getUbic_id());
        txt_ubicacion.setValue(objInvFisicoDet.getUbic_des());
        txt_entero.setValue(objInvFisicoDet.getEntero());
        txt_fraccion.setValue(objInvFisicoDet.getFraccion());
        txt_estado.setValue(objInvFisicoDet.getFisidet_est());
    }

    public void habilitaCampos(boolean b_valida) {
        txt_grupid.setDisabled(!b_valida);
        txt_almid.setDisabled(!b_valida);
        txt_respid.setDisabled(!b_valida);
        cb_perid.setDisabled(!b_valida);
    }

    public void habilitaCamposDetalle(boolean b_valida) {
        txt_seleccion.setDisabled(b_valida);
        txt_idubicacion.setDisabled(b_valida);
        txt_entero.setDisabled(b_valida);
        txt_fraccion.setDisabled(b_valida);
        btn_agregar.setDisabled(b_valida);
    }

    public void habilitaCamposBusqueda(boolean b_valida) {
        txt_seleccion.setDisabled(b_valida);
        btn_agregar.setDisabled(b_valida);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(!b_valida1);
        tbbtn_btn_editar.setDisabled(!b_valida1);
        tbbtn_btn_eliminar.setDisabled(!b_valida1);
        tbbtn_btn_guardar.setDisabled(!b_valida2);
        tbbtn_btn_deshacer.setDisabled(!b_valida2);
    }

    public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevodet.setDisabled(b_valida1);
        tbbtn_btn_editardet.setDisabled(b_valida1);
        tbbtn_btn_eliminardet.setDisabled(b_valida1);
        tbbtn_btn_deshacerdet.setDisabled(b_valida2);
        tbbtn_btn_guardardet.setDisabled(b_valida2);
    }
}
