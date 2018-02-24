package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.cxc.mantenimiento.DaoDirecciones;
import org.me.gj.controller.facturacion.mantenimiento.DaoVendedores;
import org.me.gj.controller.logistica.mantenimiento.DaoMotivoCambio;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.mantenimiento.MotCam;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.procesos.NotCambCab;
import org.me.gj.model.logistica.procesos.NotCambDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerGenNotaCambio extends SelectorComposer<Component> {

    // Componentes Web
    @Wire
    Tab tab_listanotcam, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar,
            tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,// Botones Nota de Cambio
            tbbtn_btn_nuevonotcam, tbbtn_btn_editarnotcam,
            tbbtn_btn_eliminarnotcam, tbbtn_btn_guardarnotcam,
            tbbtn_btn_deshacernotcam;// Botones Nota de Cambio Detalle
    @Wire
    Button btn_buscarcambios;
    @Wire
    Listbox lst_notcambcab, lst_notcambdet;
    @Wire
    Combobox cb_situacion, cb_ncd_tipdoc;

    @Wire
    Textbox txt_venid,
            txt_vennom,// Datos para la busqueda
            txt_nc_id, txt_nc_motcamid, txt_nc_motcamdes, txt_cli_id,
            txt_cli_razsoc, txt_nc_depurado, txt_nc_notaent, txt_nc_notasal,
            txt_zon_id, txt_zon_des, txt_clidir_direcc, txt_nc_horid,
            txt_nc_hordes, txt_nc_registro, txt_sup_id, txt_nc_venid,
            txt_nc_vennom, txt_nc_transid,
            txt_nc_transdes,// Datos de la Nota Cambio Cabecera
            txt_ncd_prodid, txt_ncd_proddes, txt_ncd_doc, txt_ncd_glosanotcam,
            txt_ncd_serie, txt_ncd_stock,// Datos de la Nota Cambio Detalle
            txt_usuadd, txt_usumod;// ------>Datos de la Nota Cambio Cabecera
    @Wire
    Intbox // Datos de la Nota Recojo Cabecera
            // txt_ncd_entero,
            txt_ncd_fraccion, txt_ncd_upre;// Datos de la Nota Cambio Detalle
    @Wire
    Doublebox txt_ncd_entero;

    @Wire
    Longbox txt_clidir_id;
    @Wire
    Checkbox chk_ordvend;// Datos para la Busqueda
    @Wire
    Datebox d_fecini, d_fecfin,// Datos para la Busqueda
            d_nc_fecemi, d_nc_fecent,// Datos de la Nota Cambio Cabecera
            d_fecadd, d_fecmod;// ------>Datos de la Nota Cambio Cabecera
    // Instancias de Objetos
    ListModelList<NotCambCab> objlstNotaCambioCab;
    ListModelList<NotCambDet> objlstNotaCambioDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    DaoCierreDia objDaoCierreDia;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    Productos objProducto;
    Vendedor objVendedor;
    NotCambCab objNotaCambioCab;
    NotCambDet objNotaCambioDet;
    DaoNotaCambio objDaoNotaCambio;
    Utilitarios objUtilitario;
    Cliente objCliente = new Cliente();
    CliDireccion objCliDireccion;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    ParametrosSalida objParametrosSalida;
    // Variables publicas
    int i_selCab, i_selDet;
    int emp_id, suc_id;
    int i = 0;
    String campo = "";
    String fechaActual, s_estado, s_mensaje, s_estadoDetalle;
    String x = "";
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    // Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenNotaCambio.class);

    // Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_notcam")
    public void llenaRegistros() throws SQLException {
        Date fecha = new Date();
        objDaoCierreDia = new DaoCierreDia();
        fechaActual = Utilitarios.hoyAsString();
        objUtilitario = new Utilitarios();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        String vend_id = "%%";
        String situacion = "%%";
        objlstNotaCambioCab = null;
        objDaoNotaCambio = new DaoNotaCambio();
        objlstNotaCambioCab = objDaoNotaCambio.listaNotaCambioCab(emp_id, suc_id, vend_id, situacion, fechaActual, fechaActual, false);
        Utilitarios.sumaFecha(d_nc_fecent, fecha, 1);// Fec Entrega
        lst_notcambcab.setModel(objlstNotaCambioCab);
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_ncd_tipdoc.setModel(objlstTipDoc);
        d_fecini.setConstraint("before " + Utilitarios.hoyAsString2());
        d_fecfin.setConstraint("before " + Utilitarios.hoyAsString2());
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10203010, usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Proceso de Generar Nota de Cambio: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Proceso de Generar Nota de Cambio con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creacion de una nueva Generacion Nota de Cambio");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creacion de una nueva Generacion Nota de Cambio");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edicion de una Generacion Nota de Cambio");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edicion de una Generacion Nota de Cambio");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminacion de una Generacion  Nota de Cambio");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminacion de Generacion una Nota de Cambio");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Nota de Cambio");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Nota de Cambio");
        }
    }

    @Listen("onClick=#btn_buscarcambios")
    public void busquedaRegistros() throws SQLException {
        objlstNotaCambioCab = null;
        objlstNotaCambioCab = new ListModelList<NotCambCab>();
        lst_notcambcab.setModel(objlstNotaCambioCab);
        String resultado;
        Date fecha_emisioni = d_fecini.getValue();
        Date fecha_emisionf = d_fecfin.getValue();
        if (fecha_emisioni == null || fecha_emisionf == null) {
            resultado = "OK";
        } else {
            resultado = Utilitarios.compareFechas(fecha_emisioni,
                    fecha_emisionf);
        }
        String f_emisioni;
        if (fecha_emisioni == null) {
            f_emisioni = "01/01/2000";
        } else {
            f_emisioni = sdf.format(d_fecini.getValue());
        }
        String f_emisionf;
        if (fecha_emisionf == null) {
            f_emisionf = "";
        } else {
            f_emisionf = sdf.format(d_fecfin.getValue());
        }
        boolean OrdVend = chk_ordvend.isChecked();

        String vend_id = txt_venid.getValue().isEmpty() ? "%%" : txt_venid.getValue().replace("0", "").trim();
        String oc_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        if (resultado.equals("F1>")) {
            Messagebox.show("La Fecha Inicial no puede ser mayor a la Final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstNotaCambioCab = objDaoNotaCambio.listaNotaCambioCab(emp_id, suc_id, vend_id, oc_sit, f_emisioni, f_emisionf, OrdVend);

            // Validar tabla sin registro
            if (objlstNotaCambioCab.getSize() > 0) {
                lst_notcambcab.setModel(objlstNotaCambioCab);
            } else {
                objlstNotaCambioCab = null;
                objlstNotaCambioCab = new ListModelList<NotCambCab>();
                lst_notcambcab.setModel(objlstNotaCambioCab);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            // Campos del detalle de la Orden de Compra
            limpiarCamposDetalle();
            objlstNotaCambioDet = null;
            objlstNotaCambioDet = new ListModelList<NotCambDet>();
            lst_notcambdet.setModel(objlstNotaCambioDet);
            limpiarCampos();
            limpiaAuditoria();
        }
    }

    @Listen("onSelect=#lst_notcambcab")
    public void seleccionaRegistro() throws SQLException {
        objNotaCambioCab = (NotCambCab) lst_notcambcab.getSelectedItem().getValue();
        if (objNotaCambioCab == null) {
            objNotaCambioCab = objlstNotaCambioCab.get(i_selCab + 1);
        }
        i_selCab = lst_notcambcab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onSelect=#lst_notcambdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaCambioDet = (NotCambDet) lst_notcambdet.getSelectedItem().getValue();
        if (objNotaCambioDet == null) {
            objNotaCambioDet = objlstNotaCambioDet.get(i_selDet + 1);
        }
        i_selDet = lst_notcambdet.getSelectedIndex();
        llenarCamposProducto();
        // validaProductos();
    }

    @Listen("onCheck=#chk_ordvend")
    public void seleccionarOrden() throws SQLException {
        if (!(objlstNotaCambioCab == null || objlstNotaCambioCab.isEmpty())) {
            busquedaRegistros();
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        objNotaCambioCab = null;
        objNotaCambioCab = new NotCambCab();
        objNotaCambioDet = null;
        objlstNotaCambioDet = null;
        objlstNotaCambioDet = new ListModelList<NotCambDet>();
        Date fecha = new Date();
        lst_notcambdet.setModel(objlstNotaCambioDet);
        limpiarCampos();
        limpiarCamposDetalle();
        limpiarTabLista(3);
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitabotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        Utilitarios.sumaFecha(d_nc_fecent, fecha, 1);// Fec Recepcion
        d_nc_fecemi.focus();
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_notcambcab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objNotaCambioCab.getNc_sit() != 1) {
                Messagebox.show("Esta Nota de Cambio ya no se Puede Modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitabotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<NotCambDet>();
                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (!objlstNotaCambioDet.isEmpty()) {
            String s_valida = verificarCab();
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("F. Emision")) {
                                d_nc_fecemi.focus();
                            } else if (campo.equals("F. Entrega")) {
                                d_nc_fecent.focus();
                            } else if (campo.equals("M. Cambio")) {
                                txt_nc_motcamid.focus();
                            } else if (campo.equals("Cliente")) {
                                txt_cli_id.focus();
                            } else if (campo.equals("Direccion")) {
                                txt_clidir_id.focus();
                            } else if (campo.equals("Zona")) {
                                txt_zon_id.focus();
                            } else if (campo.equals("Transporte")) {
                                txt_nc_transid.focus();
                            } else if (campo.equals("Vendedor")) {
                                txt_nc_venid.focus();
                            } else if (campo.equals("Horario")) {
                                txt_nc_horid.focus();
                            }
                        }
                    }
                });
            } else {
                s_valida = verificarFechas();
                if (!s_valida.equals("")) {
                    Messagebox.show(s_valida);
                } else {
                    if (!txt_cli_id.getValue().matches("[0-9]*")) {
                        lst_notcambdet.focus();
                    } else if (!txt_clidir_id.getValue().toString()
                            .matches("[0-9]*")) {
                        lst_notcambdet.focus();
                    } else if (!txt_nc_motcamid.getValue().matches("[0-9]*")) {
                        lst_notcambdet.focus();
                    } else {
                        objCliente = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                        objCliDireccion = new DaoDirecciones().getNomDireccion(txt_cli_id.getValue(), txt_clidir_id.getValue(), emp_id, suc_id);
                        MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(txt_nc_motcamid.getValue());
                        if (objCliente == null || objCliDireccion == null || objMotivoCambio == null) {
                            lst_notcambdet.focus();
                        } else {
                            String s_ed = s_estadoDetalle == null ? "" : s_estadoDetalle;
                            if (s_ed.equals("N")) {
                                Messagebox.show("Guarde detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            } else if (s_ed.equals("E")) {
                                Messagebox.show("El Detalle esta en edicion ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            } else {
                                String fecemi = sdf.format(d_nc_fecemi.getValue());
                                if (new DaoCierreDia().ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                                    Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    s_mensaje = "Esta Seguro que desea guardar los cambios?";
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParamCabecera;
                                                objNotaCambioCab = (NotCambCab) generaNotaCambioCabecera();
                                                if (s_estado.equals("N")) {
                                                    objParamCabecera = objDaoNotaCambio.insertarNotaCambioCab(objNotaCambioCab);
                                                } else {
                                                    objParamCabecera = objDaoNotaCambio.modificarNotaCambioCab(objNotaCambioCab);
                                                }
                                                if (objParamCabecera.getFlagIndicador() == 0) {
                                                    ParametrosSalida objParamDetalle = new ParametrosSalida();
                                                    boolean verificarDetalle = true;
                                                    int i = 0;
                                                    if (s_estado
                                                            .equals("N")) {
                                                        while (i < objlstNotaCambioDet.getSize() && verificarDetalle) {
                                                            long nr_key = Long.parseLong(objParamCabecera.getCodigoRegistro());
                                                            objlstNotaCambioDet.get(i).setNc_key(nr_key);
                                                            objParamDetalle = objDaoNotaCambio.insertarNotaCambioDet(objlstNotaCambioDet.get(i));
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un Error con el Item " + objlstNotaCambioDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    } else {
                                                        // OPERACION DE ELINACION DE PRODUCTOS DE LA NOTA DE RECOJO
                                                        if (!objlstEliminacion.isEmpty()) {
                                                            while (i < objlstEliminacion.getSize() && verificarDetalle) {
                                                                objParamDetalle = objDaoNotaCambio.eliminarNotaCambioDet(objlstEliminacion.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrio un Error con el Item" + objlstEliminacion.get(i).getNcd_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                        // OPERACION DE INSERCION Y  MODIFICACION  DE PRODUCTOS  DE LA NOTA DE RECOJO
                                                        i = 0;
                                                        verificarDetalle = true;
                                                        while (i < objlstNotaCambioDet.getSize() && verificarDetalle) {
                                                            String indicador = objlstNotaCambioDet.get(i).getInd_accion();
                                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                                objParamDetalle = objDaoNotaCambio.insertarNotaCambioDet(objlstNotaCambioDet.get(i));
                                                            } else if (indicador.equals("M")) {
                                                                objParamDetalle = objDaoNotaCambio.modificarNotaCambioDet(objlstNotaCambioDet.get(i));
                                                            }
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un Error con el Producto " + objlstNotaCambioDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    }
                                                    // validacion de
                                                    // guardar/nuevo
                                                    VerificarTransacciones();
                                                    tbbtn_btn_guardar.setDisabled(true);
                                                    tbbtn_btn_deshacer.setDisabled(true);
                                                    //
                                                    habilitaTab(false, false);
                                                    seleccionaTab(true, false);
                                                    habilitaCampos(true);
                                                    habilitaCamposDetalle(true);
                                                    habilitabotonesDetalle(true, true);
                                                    limpiarCamposDetalle();
                                                    limpiarCampos();
                                                    limpiaAuditoria();
                                                    limpiarTabLista(2);
                                                    busquedaRegistros();
                                                }
                                                Messagebox.show(objParamCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                }
            }
        } else {
            Messagebox.show("No Puede Guardar una Nota de Cambio con Detalle Vacio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_notcambcab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta Nota de Cambio?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        ParametrosSalida objParamSalida;
                        objParamSalida = objDaoNotaCambio.eliminarNotaCambioCab(objNotaCambioCab);
                        if (objParamSalida.getFlagIndicador() == 0) {
                            if (d_fecini.getValue() == null || d_fecfin.getValue() == null) {
                                d_fecini.setValue(new Date());
                                d_fecfin.setValue(new Date());
                            }
                            limpiarCampos();
                            limpiaAuditoria();
                            limpiarTabLista(2);
                            busquedaRegistros();
                        }
                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        Messagebox.show("Esta Seguro que desea deshacer los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    // validacion de guardar/nuevo
                    VerificarTransacciones();
                    tbbtn_btn_guardar.setDisabled(true);
                    tbbtn_btn_deshacer.setDisabled(true);
                    //
                    habilitaTab(false, false);
                    seleccionaTab(true, false);
                    habilitaCampos(true);
                    // limpiar los campos
                    limpiarCampos();
                    limpiarCamposDetalle();
                    limpiarTabLista(3);
                    // Campos del detalle de la Orden de Compra
                    habilitabotonesDetalle(true, true);
                    habilitaCamposDetalle(true);
                    busquedaRegistros();
                }
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_nuevonotcam")
    public void nuevoDetalleCambio() {
        String verificar = verificarCab();
        if (!verificar.equals("")) {
            Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                if (campo.equals("F. Emision")) {
                                    d_nc_fecemi.focus();
                                } else if (campo.equals("F. Entrega")) {
                                    d_nc_fecent.focus();
                                } else if (campo.equals("M. Cambio")) {
                                    txt_nc_motcamid.focus();
                                } else if (campo.equals("Cliente")) {
                                    txt_cli_id.focus();
                                } else if (campo.equals("Direccion")) {
                                    txt_clidir_id.focus();
                                } else if (campo.equals("Zona")) {
                                    txt_zon_id.focus();
                                } else if (campo.equals("Transporte")) {
                                    txt_nc_transid.focus();
                                } else if (campo.equals("Vendedor")) {
                                    txt_venid.focus();
                                } else if (campo.equals("Horario")) {
                                    txt_nc_horid.focus();
                                }
                            }
                        }
                    });
        } else {
            s_estadoDetalle = "N";
            tbbtn_btn_guardarnotcam.setDisabled(false);
            txt_ncd_prodid.focus();
            habilitaCamposDetalle(false);
            habilitaCampos(true);
            habilitaCamposNC(true);
            habilitabotonesDetalle(true, false);
            limpiarCamposDetalle();
            Utilitarios.deshabilitarLista(true, lst_notcambdet);
        }
    }

    @Listen("onClick=#tbbtn_btn_editarnotcam")
    public void modificaDetalleCambio() {
        if (objlstNotaCambioDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Cambio a Editar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notcambdet.getSelectedIndex() == -1) {
                Messagebox.show("Por favor Seleccione un Registro del Detalle de Nota de Cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                habilitaCamposDetalle(false);
                habilitaCamposNC(true);
                txt_ncd_prodid.setDisabled(true);
                txt_ncd_serie.setDisabled(true);
                txt_ncd_doc.setDisabled(true);
                tbbtn_btn_guardarnotcam.setDisabled(false);
                habilitabotonesDetalle(true, false);
                Utilitarios.deshabilitarLista(true, lst_notcambdet);
                s_estadoDetalle = "E";
                txt_ncd_entero.focus();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardarnotcam")
    public void agregarDetalle() throws SQLException {
        String validaProducto = verificarDetalle();
        String validaStock = verificaStock();
        if (!validaProducto.isEmpty()) {
            Messagebox.show(validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Producto")) {
                            txt_ncd_prodid.focus();
                        } else if (campo.equals("Tip. Doc")) {
                            cb_ncd_tipdoc.focus();
                        } else if (campo.equals("Serie")) {
                            txt_ncd_serie.focus();
                        } else if (campo.equals("Doc")) {
                            txt_ncd_doc.focus();
                        } else if (campo.equals("Cant. Entero")) {
                            txt_ncd_entero.focus();
                        } else if (campo.equals("Cant. Fraccion")) {
                            txt_ncd_fraccion.focus();
                        } else if (campo.equals("Fraccion")) {
                            txt_ncd_fraccion.focus();
                        }
                    }
                }
            });
        } else if (!validaStock.isEmpty()) {
            Messagebox.show(validaStock, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String pro_id = txt_ncd_prodid.getValue();
            if (!pro_id.matches("[0-9]*")) {
                lst_notcambdet.focus();
            } else {
                objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    lst_notcambdet.focus();
                } else {
                    if (s_estado.equals("N")) {
                        if (s_estadoDetalle.equals("N")) {
                            String valida = validaIngresoDetalle(pro_id);
                            if (valida.equals("")) {
                                objNotaCambioDet = (NotCambDet) generaNotaCambioDetalle();
                                objlstNotaCambioDet.add(objNotaCambioDet);
                                lst_notcambdet.setModel(objlstNotaCambioDet);
                            } else {
                                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            objNotaCambioDet = (NotCambDet) generaNotaCambioDetalle();
                            int lst_ord = lst_notcambdet.getSelectedIndex();
                            objlstNotaCambioDet.set(lst_ord, objNotaCambioDet);
                        }
                    } else {
                        if (s_estadoDetalle.equals("N")) {
                            String valida = validaIngresoDetalle(pro_id);
                            if (valida.equals("")) {
                                objNotaCambioDet = (NotCambDet) generaNotaCambioDetalle();
                                objNotaCambioDet.setInd_accion("N");
                                objlstNotaCambioDet.add(objNotaCambioDet);
                            } else {
                                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            String indicador = objNotaCambioDet.getInd_accion();
                            if (indicador.equals("N") || indicador.equals("NM")) {
                                objNotaCambioDet = (NotCambDet) generaNotaCambioDetalle();
                                objNotaCambioDet.setInd_accion("NM");
                            } else {
                                long item = objNotaCambioDet.getNcd_item();
                                objNotaCambioDet = (NotCambDet) generaNotaCambioDetalle();
                                objNotaCambioDet.setInd_accion("M");
                                objNotaCambioDet.setNcd_item(item);
                            }
                            objlstNotaCambioDet.set(lst_notcambdet.getSelectedIndex(), objNotaCambioDet);
                        }
                    }
                    s_estadoDetalle = "";
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    habilitabotonesDetalle(false, true);
                    habilitaCamposNC(false);
                    tbbtn_btn_guardarnotcam.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_notcambdet);
                    lst_notcambdet.clearSelection();
                    lst_notcambdet.setFocus(true);
                    lst_notcambdet.focus();
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarnotcam")
    public void eliminaDetalleCambio() {
        if (objlstNotaCambioDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Cambio a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notcambdet.getSelectedIndex() == -1) {
                Messagebox.show("Por Favor Seleccione un Registro a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objNotaCambioDet = lst_notcambdet.getSelectedItem().getValue();
                int posicion = lst_notcambdet.getSelectedIndex();
                if (s_estado.equals("E") && (!objNotaCambioDet.getInd_accion().equals("N") || !objNotaCambioDet.getInd_accion().equals("NM"))) {
                    long nrd_item = objNotaCambioDet.getNcd_item();
                    long nr_key = Long.parseLong(txt_nc_id.getValue());
                    String nrd_usumod = objUsuarioCredential.getCuenta();
                    String nrd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
                    objlstEliminacion.add(new NotCambDet(nr_key, emp_id, suc_id, nrd_item, nrd_usumod, nrd_pcmod));
                }
                objlstNotaCambioDet.remove(posicion);
                limpiarCamposDetalle();
                tbbtn_btn_guardarnotcam.setDisabled(true);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacernotcam")
    public void deshacerProducto() {
        Messagebox.show("Esta Seguro que desea deshacer los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    s_estadoDetalle = "";
                    cb_ncd_tipdoc.setFocus(true);
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    habilitaCamposNC(false);
                    habilitabotonesDetalle(false, true);
                    tbbtn_btn_guardarnotcam.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_notcambdet);
                    lst_notcambdet.clearSelection();
                }
                lst_notcambdet.setFocus(true);
                lst_notcambdet.focus();
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaCambioCab == null || objlstNotaCambioCab.isEmpty()) {
            Messagebox.show("No hay Registros de Generar Notas de Cambio para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notcambcab.getSelectedIndex() >= 0) {
                objNotaCambioCab = (NotCambCab) lst_notcambcab.getSelectedItem().getValue();
                if (objNotaCambioCab == null) {
                    objNotaCambioCab = objlstNotaCambioCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
                objMapObjetos.put("nc_key", objNotaCambioCab.getNc_key());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenNotCam.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione Registro de Nota de Cambio para imprimir");
            }
        }
    }

    // Eventos Secundarios - Validacion
    @Listen("onOK=#txt_venid")
    public void lov_vendedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_venid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaCambio";
                Textbox txt_supid = new Textbox();
                objMapObjetos.put("txt_venid", txt_venid);
                objMapObjetos.put("txt_vennom", txt_vennom);
                objMapObjetos.put("txt_sup_id", txt_supid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onOK=#txt_nc_motcamid")
    public void lov_motCambio() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nc_motcamid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaCambio";
                objMapObjetos.put("txt_motid", txt_nc_motcamid);
                objMapObjetos.put("txt_motdes", txt_nc_motcamdes);
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoCambio.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaMotivoCambio();
            }
        }
    }

    @Listen("onOK=#txt_clidir_id")
    public void lov_direcciones() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (!txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() == null) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaCambio";
                objMapObjetos.put("txt_dirid", txt_clidir_id);
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_dirdes", txt_clidir_direcc);
                objMapObjetos.put("txt_zonid", txt_zon_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDirecciones.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaDireccion();
            }
        }
    }

    @Listen("onOK=#txt_cli_id")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaCambio";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("txt_zon_id", txt_zon_id);
                objMapObjetos.put("txt_zon_des", txt_zon_des);
                objMapObjetos.put("txt_nr_venid", txt_nc_venid);
                objMapObjetos.put("txt_sup_id", txt_sup_id);
                objMapObjetos.put("txt_nr_vennom", txt_nc_vennom);
                objMapObjetos.put("txt_nr_transid", txt_nc_transid);
                objMapObjetos.put("txt_nr_transdes", txt_nc_transdes);
                objMapObjetos.put("txt_nr_horid", txt_nc_horid);
                objMapObjetos.put("txt_nr_hordes", txt_nc_hordes);
                objMapObjetos.put("txt_clidir_id", txt_clidir_id);
                objMapObjetos.put("txt_clidir_direcc", txt_clidir_direcc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaClientes();
            }
        }
    }

    @Listen("onOK=#txt_ncd_prodid")
    public void lov_productos() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_ncd_prodid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaCambio";
                objMapObjetos.put("txt_proid", txt_ncd_prodid);
                objMapObjetos.put("txt_prodes", txt_ncd_proddes);
                objMapObjetos.put("txt_ncd_entero", txt_ncd_entero);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaCambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaProducto();
            }
        }
    }

    @Listen("onOK=#d_nc_fecemi")
    public void validaFechaEmision() throws SQLException {
        if (d_nc_fecemi.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Emision de la Nota de Cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event)
                        throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        d_nc_fecent.focus();
                    }
                }
            });
        } else {
            String fecemi = sdf.format(d_nc_fecemi.getValue());
            if (objDaoCierreDia.ValidaDia(fecemi, "L") == null || objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                d_nc_fecent.focus();
            }
        }
    }

    @Listen("onOK=#d_nc_fecent")
    public void validaFechaEntrega() {
        if (d_nc_fecent.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Entrega de la Nota de Cambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event)
                        throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        d_nc_fecent.focus();
                    }
                }
            });

        } else {
            if (verificarFechas().isEmpty()) {
                txt_nc_motcamid.focus();
            } else {
                Messagebox.show(verificarFechas(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onOK=#cb_ncd_tipdoc")
    public void validaTipoDocumento() {
        if (cb_ncd_tipdoc.getSelectedIndex() == -1) {
            Messagebox.show("Seleccione 'Tipo de documento'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        cb_ncd_tipdoc.focus();
                    }
                }
            });
        } else {
            txt_ncd_serie.focus();
        }
    }

    @Listen("onOK=#txt_ncd_serie")
    public void validaSerie() {
        if (!txt_ncd_serie.getValue().isEmpty()) {
            if (txt_ncd_serie.getValue().matches("^[0-9]*") || txt_ncd_serie.getValue().matches("^\\w*")) {
                if (txt_ncd_serie.getValue().matches("^[0-9]*") && txt_ncd_serie.getValue().length() <= 2) {
                    Messagebox.show("Debe ingresar valores  mayores a 2 en  'Serie'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_serie.focus();
                            }
                        }
                    });
                } else if (txt_ncd_serie.getValue().matches("^[0-9]*")
                        && Integer.parseInt(txt_ncd_serie.getValue()) <= 0) {
                    Messagebox.show("En el campo 'Serie' debe ingresar numero mayor a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_serie.focus();
                            }
                        }
                    });

                } else if (txt_ncd_serie.getValue().matches("^\\w*")
                        && txt_ncd_serie.getValue().length() <= 2) {
                    Messagebox.show("En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_serie.focus();
                            }
                        }
                    });
                } else {
                    txt_ncd_doc.focus();
                }
            } else {
                Messagebox.show("Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_ncd_serie.focus();
                        }
                    }
                });
            }
        } else {
            txt_ncd_doc.focus();
        }
    }

    @Listen("onOK=#txt_ncd_doc")
    public void validaDocumento() {
        if (!txt_ncd_doc.getValue().matches("^[0-9]*")) {
            Messagebox.show("Ingrese datos numericos en el campo 'Documento'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_ncd_doc.focus();
                    }
                }
            });
        } else if (txt_ncd_doc.getValue().length() != 7 && txt_ncd_doc.getValue().length() != 8) {
            Messagebox.show(
                    "En el campo 'Documento' debe ingresar 7 o 8 caracteres", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_doc.focus();
                            }
                        }
                    });
        } else if (Integer.parseInt(txt_ncd_doc.getValue()) <= 0) {
            Messagebox.show("En el campo 'Documento' debe ingresar valores numericos mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_ncd_doc.focus();
                    }
                }
            });

        } else {
            txt_ncd_glosanotcam.focus();
        }
    }

    @Listen("onOK=#txt_ncd_entero")
    public void validaEntero() {
        if (txt_ncd_entero.getValue() != null) {
            if (!txt_ncd_entero.getValue().toString().trim().matches("[0-9]*")) {
                String s_calculo = String.valueOf(txt_ncd_entero.getValue());
                String[] cadena = s_calculo.split("[.]");
                int ultimo = Integer.parseInt(cadena[1]);
                if (ultimo > 0) {
                    double cant = cadena[1].length() == 1
                            ? (double) Integer.parseInt(cadena[1]) / 10 * txt_ncd_upre.getValue()
                            : (double) Integer.parseInt(cadena[1]) / 100 * txt_ncd_upre.getValue();
                    int fraccion = (int) cant;
                    txt_ncd_entero.setValue(Integer.parseInt(cadena[0]));
                    txt_ncd_fraccion.setValue(fraccion);
                    txt_ncd_fraccion.focus();
                } else {
                    if (txt_ncd_fraccion.getValue() != null) {
                        txt_ncd_fraccion.focus();
                    } else {
                        txt_ncd_fraccion.setValue(0);
                        txt_ncd_fraccion.focus();
                    }
                }
            }
        } else {
            txt_ncd_fraccion.focus();
        }
    }

    @Listen("onOK=#txt_ncd_fraccion")
    public void validaFraccion() {
        if (txt_ncd_prodid.getValue().isEmpty()) {
            Messagebox.show("Ingrese Producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        limpiarCamposDetalleProducto();
                        txt_ncd_prodid.focus();
                    }
                }
            });
        } else {
            if (txt_ncd_entero.getValue() == null) {
                if (txt_ncd_fraccion.getValue() != null) {
                    if (txt_ncd_upre.getValue() <= txt_ncd_fraccion.getValue()) {
                        double d_calculo = txt_ncd_fraccion.getValue() / txt_ncd_upre.getValue();
                        String s_calculo = String.valueOf(d_calculo);
                        String[] cadena = s_calculo.split("[.]");
                        int d_calculofrac = txt_ncd_fraccion.getValue() % txt_ncd_upre.getValue();
                        txt_ncd_entero.setValue(Integer.parseInt(cadena[0]));
                        txt_ncd_fraccion.setValue(d_calculofrac);
                        cb_ncd_tipdoc.focus();
                    } else {
                        txt_ncd_entero.setValue(0);
                        cb_ncd_tipdoc.focus();
                    }
                } else {
                    Messagebox.show("Ingrese Cantidad Entero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_entero.focus();
                            }
                        }
                    });
                }
            } else {
                String s_cal = String.valueOf(txt_ncd_entero.getValue());
                String[] cad = s_cal.split("[.]");
                int i_ent = Integer.parseInt(cad[0]);
                if (txt_ncd_upre.getValue() <= txt_ncd_fraccion.getValue()) {
                    double d_calculo = txt_ncd_fraccion.getValue() / txt_ncd_upre.getValue();
                    String s_calculo = String.valueOf(d_calculo);
                    String[] cadena = s_calculo.split("[.]");
                    int d_calculofrac = txt_ncd_fraccion.getValue() % txt_ncd_upre.getValue();
                    txt_ncd_entero.setValue(Integer.parseInt(cadena[0]) + i_ent);
                    txt_ncd_fraccion.setValue(d_calculofrac);
                    cb_ncd_tipdoc.focus();
                } else {
                    cb_ncd_tipdoc.focus();
                }
            }
        }
    }

    @Listen("onOK=#txt_ncd_glosanotcam")
    public void validaGlosa() throws SQLException {
        agregarDetalle();
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClienteNotCam() throws SQLException {
        if (txt_cli_id.getValue().isEmpty()) {
            limpiarCliDirVacio();
        } else {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese datos numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCliDirVacio();
                            txt_cli_id.focus();
                        }
                    }
                });
            } else {
                objCliente = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                if (objCliente == null) {
                    Messagebox.show("El codigo del cliente no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event)
                                throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarCliDirVacio();
                                txt_cli_id.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + "y ha encontrado 1 registro(s)");
                    txt_cli_id.setValue(objCliente.getCli_id());
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                    txt_zon_id.setValue(objCliente.getZon_id());
                    txt_zon_des.setValue(objCliente.getZon_des());
                    txt_nc_venid.setValue(objCliente.getVen_id());
                    txt_sup_id.setValue(objCliente.getSup_id());
                    txt_nc_vennom.setValue(objCliente.getVen_apenom());
                    if (objCliente.getHor_id() == null) {
                        Messagebox.show("El codigo del cliente no tiene direccion asignada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCliDirVacio();
                                    txt_cli_id.focus();
                                }
                            }
                        });
                    } else {
                        txt_nc_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                        txt_nc_hordes.setValue(objCliente.getHor_des());
                        txt_nc_transid.setValue(objCliente.getTrans_id());
                        txt_nc_transdes.setValue(objCliente.getTrans_alias());
                        txt_clidir_id.setValue(objCliente.getClidir_id());
                        txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                        txt_clidir_id.focus();
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_clidir_id")
    public void validaDirecciones() throws SQLException {
        if (txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() != null) {
            Messagebox.show("Por favor verifique primero el codigo del cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        limpiarCliDirVacio();
                        txt_cli_id.focus();
                    }
                }
            });
        } else {
            txt_clidir_direcc.setValue("");
            if (txt_clidir_id.getValue() != null) {
                String cli_id = txt_cli_id.getValue();
                long clidir_id = txt_clidir_id.getValue();
                if (cli_id.matches("[0-9]*")) {
                    objCliDireccion = new DaoDirecciones().getNomDireccion(cli_id, clidir_id, emp_id, suc_id);
                    if (objCliDireccion == null) {
                        Messagebox.show("El codigo de direccion no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_clidir_direcc.setValue("");
                                    txt_clidir_id.setValue(null);
                                    txt_clidir_id.focus();
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la Direccion del Clientes con codigo " + objCliDireccion.getCli_id() + " y ha encontrado 1 registro(s)");
                        txt_clidir_id.setValue(objCliDireccion.getClidir_id());
                        txt_clidir_direcc.setValue(objCliDireccion.getClidir_direc());
                        txt_zon_id.setValue(objCliDireccion.getZon_id());
                        txt_zon_des.setValue(objCliDireccion.getZon_des());
                        txt_nc_transid.setValue(objCliDireccion.getTrans_id());
                        txt_nc_transdes.setValue(objCliDireccion.getTrans_des());
                        txt_nc_venid.setValue(objCliDireccion.getVen_id());
                        txt_nc_vennom.setValue(objCliDireccion.getVen_apenom());
                        txt_nc_horid.setValue(objCliDireccion.getHor_id());
                        txt_nc_hordes.setValue(objCliDireccion.getHor_des());
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_ncd_prodid")
    public void validaProductos() throws SQLException {
        String prod = txt_ncd_prodid.getValue() == null ? "" : txt_ncd_prodid.getValue();
        if (i == 0) {
            txt_ncd_proddes.setValue("");
            if (!txt_ncd_prodid.getValue().isEmpty()) {
                if (!txt_ncd_prodid.getValue().matches("[0-9]*")) {
                    Messagebox.show("Por favor ingrese datos numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_prodid.setValue("");
                                txt_ncd_proddes.setValue("");
                                txt_ncd_entero.setValue(null);
                                txt_ncd_fraccion.setValue(null);
                                txt_ncd_upre.setValue(null);
                                txt_ncd_prodid.focus();
                            }
                        }
                    });
                } else {
                    String pro_id = txt_ncd_prodid.getValue();
                    objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                    if (objProducto == null) {
                        Messagebox.show("El codigo de producto no existe o esta eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_ncd_prodid.setValue("");
                                    txt_ncd_proddes.setValue("");
                                    txt_ncd_entero.setValue(null);
                                    txt_ncd_fraccion.setValue(null);
                                    txt_ncd_upre.setValue(null);
                                    txt_ncd_prodid.focus();
                                }
                            }
                        });
                    } else {
                        i = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_ncd_prodid.setValue(objProducto.getPro_id());
                        txt_ncd_proddes.setValue(objProducto.getPro_des());
                        txt_ncd_upre.setValue(objProducto.getPro_presminven());

                        objParametrosSalida = new ParametrosSalida();
                        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nc_fecemi.getValue()), 101, objProducto.getPro_id(), "%%");
                        //int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
                        if ((objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()) > 0) {
                            //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
                            txt_ncd_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()));
                        } else {
                            txt_ncd_stock.setValue("0");
                        }
                    }
                }
            } else {
                i = 0;
            }
        } else {
            txt_ncd_proddes.setValue("");
            if (prod.equals(x)) {
                cargaDatosProd(prod, 1);
            } else {
                cargaDatosProd(prod, 0);

            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_venid")
    public void validaVendedor() throws SQLException {
        txt_vennom.setValue("");
        if (!txt_venid.getValue().isEmpty()) {
            if (!txt_venid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_venid.setValue(null);
                            txt_vennom.setValue("");
                            txt_venid.focus();
                        }
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_venid.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El Codigo de Vendedor no Existe o esta Eliminado ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_venid.setValue(null);
                                txt_vennom.setValue("");
                                txt_venid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedorCon.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_venid.setValue(objVendedorCon.getVen_id());
                    txt_vennom.setValue(objVendedorCon.getVen_ape() + " " + objVendedorCon.getVen_nom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nc_motcamid")
    public void validaMotCambio() throws SQLException {
        txt_nc_motcamdes.setValue("");
        if (!txt_nc_motcamid.getValue().isEmpty()) {
            if (!txt_nc_motcamid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_nc_motcamid.setValue("");
                            txt_nc_motcamdes.setValue("");
                            txt_nc_motcamid.focus();
                        }
                    }
                });
            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_nc_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    Messagebox.show("El Codigo del Motivo de Cambio no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nc_motcamid.setValue("");
                                txt_nc_motcamdes.setValue("");
                                txt_nc_motcamid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_nc_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_nc_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_ncd_entero")
    public void validaBlurEntero() {
        if (txt_ncd_entero.getValue() != null) {
            String s_calculo = String.valueOf(txt_ncd_entero.getValue());
            String[] cadena = s_calculo.split("[.]");
            int ultimo = Integer.parseInt(cadena[1]);
            if (ultimo > 0) {
                double cant = cadena[1].length() == 1
                        ? (double) Integer.parseInt(cadena[1]) / 10 * txt_ncd_upre.getValue()
                        : (double) Integer.parseInt(cadena[1]) / 100 * txt_ncd_upre.getValue();
                int fraccion = (int) cant;

                txt_ncd_entero.setValue(Integer.parseInt(cadena[0]));
                txt_ncd_fraccion.setValue(fraccion);
            } else {
                if (txt_ncd_fraccion.getValue() == null) {
                    txt_ncd_fraccion.setValue(0);
                }
                //else {  txt_ncd_fraccion.setValue(0);  txt_ncd_fraccion.focus(); }
            }
        }
    }

    @Listen("onBlur=#txt_ncd_fraccion")
    public void validaBlurFraccion() {
        if (!txt_ncd_prodid.getValue().isEmpty() && txt_ncd_prodid.getValue().matches("[0-9]*")) {
            if (txt_ncd_fraccion.getValue() == null && txt_ncd_entero.getValue() != null) {
                txt_ncd_fraccion.setValue(0);
            } else {
                if (txt_ncd_entero.getValue() == null) {
                    if (txt_ncd_fraccion.getValue() != null) {
                        if (txt_ncd_upre.getValue() <= txt_ncd_fraccion.getValue()) {
                            double d_calculo = txt_ncd_fraccion.getValue() / txt_ncd_upre.getValue();
                            String s_calculo = String.valueOf(d_calculo);
                            String[] cadena = s_calculo.split("[.]");
                            int d_calculofrac = txt_ncd_fraccion.getValue() % txt_ncd_upre.getValue();

                            txt_ncd_entero.setValue(Integer.parseInt(cadena[0]));
                            txt_ncd_fraccion.setValue(d_calculofrac);
                        }
                    }
                } else {
                    String s_cal = String.valueOf(txt_ncd_entero.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_ent = Integer.parseInt(cad[0]);
                    if (txt_ncd_upre.getValue() <= txt_ncd_fraccion.getValue()) {

                        double d_calculo = txt_ncd_fraccion.getValue() / txt_ncd_upre.getValue();
                        String s_calculo = String.valueOf(d_calculo);
                        String[] cadena = s_calculo.split("[.]");
                        int d_calculofrac = txt_ncd_fraccion.getValue() % txt_ncd_upre.getValue();

                        txt_ncd_entero.setValue(Integer.parseInt(cadena[0]) + i_ent);
                        txt_ncd_fraccion.setValue(d_calculofrac);
                    }
                }
            }
        }
    }

    @Listen("onCtrlKey=#w_gennotcam")
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
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonDeshacer();
                }
                break;

            case 78:
                if (!tbbtn_btn_nuevonotcam.isDisabled()) {
                    nuevoDetalleCambio();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarnotcam.isDisabled()) {
                    modificaDetalleCambio();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarnotcam.isDisabled()) {
                    eliminaDetalleCambio();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarnotcam.isDisabled()) {
                    agregarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacernotcam.isDisabled()) {
                    deshacerProducto();
                }
                break;
        }
    }

    // Eventos Otros
    public void cargaDatosProd(String prod, int elicant) throws SQLException {
        if (!prod.isEmpty()) {
            objProducto = new DaoProductos().buscarProducto(prod, "%%");
            if (elicant == 0) {
                if (objProducto == null) {
                    Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_prodid.setValue("");
                                txt_ncd_proddes.setValue("");
                                txt_ncd_entero.setValue(null);
                                txt_ncd_fraccion.setValue(null);
                                txt_ncd_upre.setValue(null);
                                txt_ncd_prodid.focus();
                            }
                        }
                    });
                } else {
                    txt_ncd_prodid.setValue(objProducto.getPro_id());
                    x = txt_ncd_prodid.getValue();
                    txt_ncd_proddes.setValue(objProducto.getPro_des());
                    txt_ncd_upre.setValue(objProducto.getPro_presminven());
                    objParametrosSalida = new ParametrosSalida();
                    objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nc_fecemi.getValue()), 101, objProducto.getPro_id(), "%%");
                    //int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
                    if ((objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()) > 0) {
                        //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
                        txt_ncd_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()));
                    } else {
                        txt_ncd_stock.setValue("0");
                    }
                    txt_ncd_entero.setValue(null);
                    txt_ncd_fraccion.setValue(null);
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                }
            } else {
                if (objProducto == null) {
                    Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ncd_prodid.setValue("");
                                txt_ncd_proddes.setValue("");
                                txt_ncd_entero.setValue(null);
                                txt_ncd_fraccion.setValue(null);
                                txt_ncd_upre.setValue(null);
                                txt_ncd_prodid.focus();
                            }
                        }
                    });
                } else {
                    txt_ncd_prodid.setValue(objProducto.getPro_id());
                    x = txt_ncd_prodid.getValue();
                    txt_ncd_proddes.setValue(objProducto.getPro_des());
                    txt_ncd_upre.setValue(objProducto.getPro_presminven());
                    objParametrosSalida = new ParametrosSalida();
                    objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nc_fecemi.getValue()), 101, objProducto.getPro_id(), "%%");
                    //int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
                    if ((objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()) > 0) {
                        //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
                        txt_ncd_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() / objProducto.getPro_presminven()));
                    } else {
                        txt_ncd_stock.setValue("0");
                    }
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                }
            }
        } else {
            txt_ncd_prodid.setValue("");
            txt_ncd_proddes.setValue("");
            txt_ncd_entero.setValue(null);
            txt_ncd_fraccion.setValue(null);
            txt_ncd_upre.setValue(null);
        }
    }

    public String verificarFechas() {
        String s_valor = "";
        // se tiene que validar que la fecha de emision sea del mismo periodo
        String fecha_emision = sdf.format(d_nc_fecemi.getValue());

        if (d_nc_fecent.getValue().getTime() < d_nc_fecemi.getValue().getTime()) {
            s_valor = "La fecha de Entrega debe ser Mayor o igual que : " + fecha_emision;
        }

        return s_valor;
    }

    public String verificarCab() {
        String validar;
        if (d_nc_fecemi.getValue() == null) {
            validar = "El campo F. Emision es Obligatorio";
            campo = "F. Emision";
        } else if (d_nc_fecent.getValue() == null) {
            validar = "El campo F. Entrega es Obligatorio";
            campo = "F. Entrega";
        } else if (txt_nc_motcamid.getValue().isEmpty()) {
            validar = "El campo M. Cambio es Obligatorio";
            campo = "M. Cambio";
        } else if (txt_cli_id.getValue().isEmpty()) {
            validar = "El campo Cliente es Obligatorio";
            campo = "Cliente";
        } else if (txt_clidir_id.getValue() == null) {
            validar = "El campo Direccion es Obligatorio";
            campo = "Direccion";
        } else if (txt_zon_id.getValue().isEmpty()) {
            validar = "El campo Zona es Obligatorio";
            campo = "Zona";
        } else if (txt_nc_transid.getValue().isEmpty()) {
            validar = "El campo Transporte es Obligatorio";
            campo = "Transporte";
        } else if (txt_nc_venid.getValue().isEmpty()) {
            validar = "El campo Vendedor es Obligatorio";
            campo = "Vendedor";
        } else if (txt_nc_horid.getValue().isEmpty()) {
            validar = "El campo Horario es Obligatorio";
            campo = "Horario";
        } else {
            validar = "";
        }
        return validar;
    }

    public String verificarDetalle() throws SQLException {
        String validar;
        if (txt_ncd_prodid.getValue().isEmpty()) {
            validar = "El campo Producto es obligatorio";
            campo = "Producto";
        } else if (txt_ncd_entero.getValue() == null) {
            validar = "El campo 'Cant.Entero' es obligatorio";
            campo = "Cant. Entero";
        } else if (txt_ncd_fraccion.getValue() == null) {
            validar = "El campo 'Cant.Fraccion' es obligatorio";
            campo = "Cant. Fraccion";
        } else {
            objProducto = new DaoProductos().listaPro(txt_ncd_prodid.getValue());
            if (objProducto != null && objProducto.getPro_presminven() < txt_ncd_fraccion.getValue()) {
                validar = "Cant.Frac supera a la unidad de presentacion";
                campo = "Cant. Fraccion";
            } else if (txt_ncd_entero.getValue() == 0 && txt_ncd_fraccion.getValue() == 0) {
                validar = "Debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
            } else if (cb_ncd_tipdoc.getSelectedIndex() == -1) {
                validar = "Seleccione 'Tipo de Documento'";
                campo = "Tip. Doc";
            } else if (txt_ncd_serie.getValue().isEmpty()) {
                validar = "El campo 'Serie' es obligatorio";
                campo = "Serie";
            } else if (!txt_ncd_serie.getValue().isEmpty() && !(txt_ncd_serie.getValue().matches("^[0-9]*") || txt_ncd_serie.getValue().matches("^\\w*"))) {
                txt_ncd_serie.setValue("");
                validar = "Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico";
                campo = "Serie";
            } else if (!txt_ncd_serie.getValue().isEmpty() && txt_ncd_serie.getValue().matches("^[0-9]*") && txt_ncd_serie.getValue().length() <= 2) {
                validar = "Debe ingresar valores numericos mayores a 2 en el campo 'Serie'";
                campo = "Serie";
            } else if (!txt_ncd_serie.getValue().isEmpty() && txt_ncd_serie.getValue().matches("^[0-9]*") && Integer.parseInt(txt_ncd_serie.getValue()) <= 0) {
                validar = "En el campo 'Serie' debe ingresar numero mayor a 0";
                campo = "Serie";
            } else if (!txt_ncd_serie.getValue().isEmpty() && txt_ncd_serie.getValue().matches("^\\w*") && txt_ncd_serie.getValue().length() <= 2) {
                txt_ncd_serie.setValue("");
                validar = "En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2";
                campo = "Serie";
            } else if (txt_ncd_doc.getValue().isEmpty()) {
                validar = "El campo 'Documento' es obligatorio";
                campo = "Doc";
            } else if (!txt_ncd_doc.getValue().matches("[0-9]*")) {
                txt_ncd_doc.setValue("");
                validar = "Ingrese datos numericos en el campo 'Documento'";
                campo = "Doc";
            } else if (txt_ncd_doc.getValue().length() != 7 && txt_ncd_doc.getValue().length() != 8) {
                validar = "En el campo 'Documento' debe ingresar 7 o 8 caracteres";
                campo = "Doc";
            } else if (Integer.parseInt(txt_ncd_doc.getValue()) <= 0) {
                validar = "En el campo 'Documento' debe ingresar valores numericos mayores a 0";
                campo = "Doc";
            } else {
                validar = "";
            }
        }
        return validar;
    }

    public String verificaStock() throws SQLException {
        String valida = "";
        double nesdet_cant = txt_ncd_entero.getValue();
        double nesdet_frac = txt_ncd_fraccion.getValue();
        double stock_disp = Double.parseDouble(txt_ncd_stock.getValue());
        int nesdet_undpre = txt_ncd_upre.getValue();
        double fraccion = Math.rint((nesdet_frac / nesdet_undpre) * 100) / 100;
        nesdet_cant = nesdet_cant + fraccion;

        if (stock_disp == 0) {
            valida = "No hay productos en almacen";
        } else {
            if (nesdet_cant > stock_disp) {
                valida = "Solo hay " + stock_disp + " unidades en stock";
                txt_ncd_entero.setValue(stock_disp);
            } else if (nesdet_cant < stock_disp) {
                valida = "";
                txt_ncd_stock.setValue("" + (stock_disp - nesdet_cant));
            }
        }
        return valida;
    }

    public void validaProducto() throws SQLException {
        String prod = txt_ncd_prodid.getValue() == null ? "" : txt_ncd_prodid.getValue();
        if (i == 0) {
            txt_ncd_proddes.setValue("");
            if (!txt_ncd_prodid.getValue().isEmpty()) {
                if (!txt_ncd_prodid.getValue().matches("[0-9]*")) {
                    txt_ncd_entero.focus();
                } else {
                    String pro_id = txt_ncd_prodid.getValue() == null ? "" : txt_ncd_prodid.getValue();
                    objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                    if (objProducto == null) {
                        txt_ncd_entero.focus();
                    } else {
                        i = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_ncd_prodid.setValue(objProducto.getPro_id());
                        x = txt_ncd_prodid.getValue();
                        txt_ncd_proddes.setValue(objProducto.getPro_des());
                        txt_ncd_upre.setValue(objProducto.getPro_presminven());
                        txt_ncd_entero.focus();
                    }
                }
            } else {
                i = 0;
            }
        } else {
            txt_ncd_proddes.setValue("");
            if (prod.equals(x)) {
                if (!prod.isEmpty()) {
                    objProducto = new DaoProductos().buscarProducto(prod, "%%");
                    if (objProducto == null) {
                        txt_ncd_entero.focus();
                    } else {
                        txt_ncd_prodid.setValue(objProducto.getPro_id());
                        x = txt_ncd_prodid.getValue();
                        txt_ncd_proddes.setValue(objProducto.getPro_des());
                        txt_ncd_upre.setValue(objProducto.getPro_presminven());
                        txt_ncd_entero.focus();
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                    }
                }

            } else {
                if (!prod.isEmpty()) {
                    objProducto = new DaoProductos().buscarProducto(prod, "%%");
                    if (objProducto == null) {
                        txt_ncd_entero.focus();
                    } else {
                        txt_ncd_prodid.setValue(objProducto.getPro_id());
                        x = txt_ncd_prodid.getValue();
                        txt_ncd_proddes.setValue(objProducto.getPro_des());
                        txt_ncd_upre.setValue(objProducto.getPro_presminven());
                        txt_ncd_entero.setValue(null);
                        txt_ncd_fraccion.setValue(null);
                        txt_ncd_entero.focus();
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                    }
                }
            }
        }

        bandera = false;
    }

    public String validaIngresoDetalle(String pro_id) {
        i = 0;
        int cantDet = objlstNotaCambioDet.getSize();
        String msj_prod = "";
        //String serie = txt_ncd_serie.getValue().isEmpty() ? "" : txt_ncd_serie.getValue().toUpperCase();
        boolean validaIngreso = true;
        while (i < cantDet && validaIngreso) {
            if (objlstNotaCambioDet.get(i).getPro_id().equals(pro_id)) {
                msj_prod = "El producto ya fue Ingresado";
                validaIngreso = false;
            } /*else if (objlstNotaCambioDet.get(i).getNcd_docref().equals(serie + txt_ncd_doc.getValue())) {
             msj_prod = "El Doc.Ref ya fue ingresado";
             validaIngreso = false;
             }*/

            i++;
        }
        return msj_prod;
    }

    public void validaClientes() throws SQLException {
        txt_cli_razsoc.setValue("");
        if (!txt_cli_id.getValue().isEmpty()) {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                txt_clidir_id.focus();
            } else {
                String cli_id = txt_cli_id.getValue();
                objCliente = new DaoCliente().getDireccionDefault(cli_id, emp_id, suc_id);
                if (objCliente == null) {
                    txt_clidir_id.focus();
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Cliente " + objCliente.getCli_id() + "y ha encontrado 1 registro(s)");
                    txt_cli_id.setValue(objCliente.getCli_id());
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                    txt_zon_id.setValue(objCliente.getZon_id());
                    txt_zon_des.setValue(objCliente.getZon_des());
                    txt_nc_venid.setValue(objCliente.getVen_id());
                    txt_sup_id.setValue(objCliente.getSup_id());
                    txt_nc_vennom.setValue(objCliente.getVen_apenom());
                    if (objCliente.getHor_id() != null) {
                        txt_nc_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                        txt_nc_hordes.setValue(objCliente.getHor_des());
                        txt_nc_transid.setValue(objCliente.getTrans_id());
                        txt_nc_transdes.setValue(objCliente.getTrans_alias());
                        txt_clidir_id.setValue(objCliente.getClidir_id());
                        txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                    }
                    txt_clidir_id.focus();
                }
            }
        }
        bandera = false;
    }

    public void validaDireccion() throws SQLException {
        if (txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() != null) {
            txt_zon_id.focus();
        } else {
            txt_clidir_direcc.setValue("");
            if (txt_clidir_id.getValue() != null) {
                String cli_id = txt_cli_id.getValue();
                long clidir_id = txt_clidir_id.getValue();
                if (cli_id.matches("[0-9]*")) {
                    objCliDireccion = new DaoDirecciones().getNomDireccion(cli_id, clidir_id, emp_id, suc_id);
                    if (objCliDireccion == null) {
                        txt_zon_id.focus();
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de la Direccion del Clientes con codigo " + objCliDireccion.getCli_id() + " y ha encontrado 1 registro(s)");
                        txt_clidir_id.setValue(objCliDireccion.getClidir_id());
                        txt_clidir_direcc.setValue(objCliDireccion.getClidir_direc());
                        txt_zon_id.setValue(objCliDireccion.getZon_id());
                        txt_zon_des.setValue(objCliDireccion.getZon_des());
                        txt_nc_transid.setValue(objCliDireccion.getTrans_id());
                        txt_nc_transdes.setValue(objCliDireccion.getTrans_des());
                        txt_nc_venid.setValue(objCliDireccion.getVen_id());
                        txt_nc_vennom.setValue(objCliDireccion.getVen_apenom());
                        txt_nc_horid.setValue(objCliDireccion.getHor_id());
                        txt_nc_hordes.setValue(objCliDireccion.getHor_des());
                    }
                }
            }
        }
        bandera = false;
    }

    public void validaMotivoCambio() throws SQLException {
        txt_nc_motcamdes.setValue("");
        if (!txt_nc_motcamid.getValue().isEmpty()) {
            if (!txt_nc_motcamid.getValue().matches("[0-9]*")) {
                d_nc_fecemi.focus();
            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_nc_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    d_nc_fecemi.focus();
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_nc_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_nc_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                    txt_cli_id.focus();
                }
            }
        }
        bandera = false;
    }

    public Object generaNotaCambioCabecera() {
        long nc_key = txt_nc_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_nc_id.getValue());
        String cli_id = txt_cli_id.getValue();
        long cli_key = Long.parseLong(txt_cli_id.getValue());
        long clidir_id = txt_clidir_id.getValue();
        String nc_zona = txt_zon_id.getValue();
        Date nc_fecemi = d_nc_fecemi.getValue();
        Date nc_fecent = d_nc_fecent.getValue();
        int nc_motcam = Integer.parseInt(txt_nc_motcamid.getValue());
        int nc_sup = Integer.parseInt(txt_sup_id.getValue());
        int nc_vend = Integer.parseInt(txt_nc_venid.getValue());
        int nc_trans = Integer.parseInt(txt_nc_transid.getValue());
        int nc_hor = Integer.parseInt(txt_nc_horid.getValue());
        String nc_usuadd = objUsuarioCredential.getCuenta();
        String nc_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nc_usumod = objUsuarioCredential.getCuenta();
        String nc_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotCambCab(nc_key, emp_id, suc_id, cli_id, cli_key, clidir_id, nc_fecemi, nc_fecent, nc_zona, nc_motcam, nc_sup, nc_vend, nc_trans, nc_hor, nc_usuadd, nc_pcadd, nc_usumod, nc_pcmod);
    }

    public Object generaNotaCambioDetalle() throws SQLException {
        objProducto = new DaoProductos().buscarProducto(txt_ncd_prodid.getValue(), "%%");
        long nc_key = txt_nc_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_nc_id.getValue());
        int ncd_tipdoc = cb_ncd_tipdoc.getSelectedItem().getValue();
        String ncd_tipdocdes = cb_ncd_tipdoc.getValue();
        String ncd_serie = txt_ncd_serie.getValue().isEmpty() ? "" : txt_ncd_serie.getValue().toUpperCase();
        String ncd_doc = txt_ncd_doc.getValue().isEmpty() ? "" : txt_ncd_doc.getValue();
        String ncd_docref = ncd_serie + ncd_doc;
        String s_calculo = String.valueOf(txt_ncd_entero.getValue());
        String[] cadena = s_calculo.split("[.]");

        int entero = Integer.parseInt(cadena[0]);
        int ncd_cantent = txt_ncd_entero.getValue() == null ? 0 : entero;
        int ncd_cantfrac = txt_ncd_fraccion.getValue() == null ? 0 : txt_ncd_fraccion.getValue();
        int ncd_canttot;
        if (objProducto.getPro_unimanven().equals("BOL") || objProducto.getPro_unimanven().equals("CJA") || objProducto.getPro_unimanven().equals("PAQ")) {
            ncd_canttot = ncd_cantent * objProducto.getPro_presminven() + ncd_cantfrac;
        } else {
            ncd_canttot = ncd_cantent + ncd_cantfrac;
        }
        String pro_id = txt_ncd_prodid.getValue();
        long pro_key = Long.parseLong(txt_ncd_prodid.getValue());
        String pro_des = objProducto.getPro_des();
        String pro_desdes = objProducto.getPro_desdes();
        String pro_unimanven = objProducto.getPro_unimanven();
        int pro_presminven = objProducto.getPro_presminven();
        String ncd_glosa = txt_ncd_glosanotcam.getValue().toUpperCase();
        String nc_usuadd = objUsuarioCredential.getCuenta();
        String nc_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nc_usumod = objUsuarioCredential.getCuenta();
        String nc_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotCambDet(nc_key, emp_id, suc_id, ncd_tipdoc, ncd_tipdocdes, ncd_serie, ncd_doc, ncd_docref, ncd_cantent, ncd_cantfrac, ncd_canttot, pro_id, pro_key, pro_desdes, pro_des, pro_unimanven, pro_presminven, ncd_glosa, nc_usuadd, nc_pcadd, nc_usumod, nc_pcmod);
    }

    public void limpiarCampos() {
        txt_nc_id.setValue("");
        d_nc_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_nc_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_nc_motcamid.setValue("");
        txt_nc_motcamdes.setValue("");
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_nc_notaent.setValue("");
        txt_nc_notasal.setValue("");
        txt_nc_depurado.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nc_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nc_vennom.setValue("");
        txt_nc_transid.setValue("");
        txt_nc_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_nc_horid.setValue("");
        txt_nc_hordes.setValue("");
        txt_nc_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_ncd_doc.setValue("");
        txt_ncd_serie.setValue("");
        txt_ncd_proddes.setValue("");
        txt_ncd_prodid.setValue("");
        cb_ncd_tipdoc.setSelectedIndex(-1);
        txt_ncd_upre.setValue(null);
        txt_ncd_entero.setValue(null);
        txt_ncd_fraccion.setValue(null);
        txt_ncd_glosanotcam.setValue("");
        txt_ncd_stock.setValue("");
    }

    public void limpiarCamposDetalleProducto() {
        txt_ncd_proddes.setValue("");
        txt_ncd_upre.setValue(null);
        txt_ncd_entero.setValue(null);
        txt_ncd_fraccion.setValue(null);
        txt_ncd_stock.setValue("");
    }

    public void limpiarCliDirVacio() {
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nc_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nc_vennom.setValue("");
        txt_nc_transid.setValue("");
        txt_nc_transdes.setValue("");
        txt_nc_horid.setValue("");
        txt_nc_hordes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
    }

    public void llenarCampos() {
        txt_nc_id.setValue(objNotaCambioCab.getNc_id());
        d_nc_fecemi.setValue(objNotaCambioCab.getNc_fecemi());
        d_nc_fecent.setValue(objNotaCambioCab.getNc_fecent());
        txt_nc_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_motcam()), 3, "0"));
        txt_nc_motcamdes.setValue(objNotaCambioCab.getMcam_des());
        txt_cli_id.setValue(objNotaCambioCab.getCli_id());
        txt_cli_razsoc.setValue(objNotaCambioCab.getCli_razsoc());
        txt_nc_notaent.setValue(objNotaCambioCab.getNc_notaent());
        txt_nc_notasal.setValue(objNotaCambioCab.getNc_notasal());
        txt_nc_depurado.setValue(objNotaCambioCab.getNc_nrodep());
        txt_zon_id.setValue(objNotaCambioCab.getNc_zona());
        txt_zon_des.setValue(objNotaCambioCab.getZon_des());
        txt_nc_venid.setValue(objNotaCambioCab.getVen_id());
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_sup()), 4, "0"));
        txt_nc_vennom.setValue(objNotaCambioCab.getVen_nom());
        txt_nc_transid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_trans()), 4, "0"));
        txt_nc_transdes.setValue(objNotaCambioCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaCambioCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaCambioCab.getClidir_direc());
        txt_nc_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaCambioCab.getNc_hor()), 3, "0"));
        txt_nc_hordes.setValue(objNotaCambioCab.getHor_des());
        txt_nc_registro.setValue(objNotaCambioCab.getNc_nroreg());
        txt_usuadd.setValue(objNotaCambioCab.getNc_usuadd());
        d_fecadd.setValue(objNotaCambioCab.getNc_fecadd());
        txt_usumod.setValue(objNotaCambioCab.getNc_usumod());
        d_fecmod.setValue(objNotaCambioCab.getNc_fecmod());
    }

    public void llenarCamposProducto() throws SQLException {
        txt_ncd_prodid.setValue(objNotaCambioDet.getPro_id());
        txt_ncd_proddes.setValue(objNotaCambioDet.getPro_des());
        txt_ncd_entero.setValue(objNotaCambioDet.getNcd_cantent());
        txt_ncd_upre.setValue(objNotaCambioDet.getPro_presmin());
        txt_ncd_fraccion.setValue(objNotaCambioDet.getNcd_cantfrac());
        txt_ncd_glosanotcam.setValue(objNotaCambioDet.getNcd_glosa());
        txt_ncd_doc.setValue(objNotaCambioDet.getNcd_doc() == null ? "" : objNotaCambioDet.getNcd_doc());
        txt_ncd_serie.setValue(objNotaCambioDet.getNcd_serie() == null ? "" : objNotaCambioDet.getNcd_serie());
        String tipo_doc = String.valueOf(objNotaCambioDet.getNcd_tipdoc());
        if (!tipo_doc.isEmpty() || tipo_doc != null) {
            cb_ncd_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_ncd_tipdoc, tipo_doc));
        } else {
            cb_ncd_tipdoc.setSelectedIndex(-1);
        }
        objParametrosSalida = new ParametrosSalida();
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nc_fecemi.getValue()), 101, objNotaCambioDet.getPro_id(), "%%");
        //int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
        if ((objParametrosSalida.getCantStocks() / objNotaCambioDet.getPro_presmin()) > 0) {
            //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
            txt_ncd_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() / objNotaCambioDet.getPro_presmin()));
        } else {
            txt_ncd_stock.setValue("0");
        }
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaCambioDet = null;
        long nr_key = objNotaCambioCab.getNc_key();
        objlstNotaCambioDet = objDaoNotaCambio.listaNotaCambioDet(emp_id, suc_id, nr_key);
        lst_notcambdet.setModel(objlstNotaCambioDet);
    }

    public void habilitaCampos(boolean b_valida) {
        d_nc_fecemi.setDisabled(b_valida);
        d_nc_fecent.setDisabled(b_valida);
        txt_nc_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_zon_id.setDisabled(b_valida);
        txt_nc_venid.setDisabled(b_valida);
        txt_nc_transid.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
        txt_nc_horid.setDisabled(b_valida);
    }

    public void habilitaCamposNC(boolean b_valida) {
        d_nc_fecemi.setDisabled(b_valida);
        d_nc_fecent.setDisabled(b_valida);
        txt_nc_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
    }

    public void habilitaCamposDetalle(boolean b_valida) {
        txt_ncd_doc.setDisabled(b_valida);
        txt_ncd_prodid.setDisabled(b_valida);
        txt_ncd_serie.setDisabled(b_valida);
        txt_ncd_glosanotcam.setDisabled(b_valida);
        txt_ncd_entero.setDisabled(b_valida);
        txt_ncd_fraccion.setDisabled(b_valida);
        txt_ncd_glosanotcam.setDisabled(b_valida);
        cb_ncd_tipdoc.setDisabled(b_valida);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitabotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevonotcam.setDisabled(b_valida1);
        tbbtn_btn_editarnotcam.setDisabled(b_valida1);
        tbbtn_btn_eliminarnotcam.setDisabled(b_valida1);
        tbbtn_btn_deshacernotcam.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotcam.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotcam.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiarTabLista(int i) {
        // Fechas Inicial y Final vacias
        if (d_fecini.getValue() == null || d_fecfin.getValue() == null) {
            d_fecini.setValue(new Date());
            d_fecfin.setValue(new Date());
        }
        if (i == 1) {
            objlstNotaCambioCab = null;
            objlstNotaCambioCab = new ListModelList<NotCambCab>();
            lst_notcambcab.setModel(objlstNotaCambioCab);
        } else if (i == 2) {
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
        } else {
            objlstNotaCambioCab = null;
            objlstNotaCambioCab = new ListModelList<NotCambCab>();
            lst_notcambcab.setModel(objlstNotaCambioCab);
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
        }
    }
}
