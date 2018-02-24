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
import org.me.gj.model.logistica.procesos.NotaRecojoCab;
import org.me.gj.model.logistica.procesos.NotaRecojoDet;
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

public class ControllerGenNotaRecojo extends SelectorComposer<Component> {

    // Componentes Web
    @Wire
    Tab tab_listanotrec, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar,
            tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,// Botones Nota de Recojo
            tbbtn_btn_nuevonotrec, tbbtn_btn_editarnotrec,
            tbbtn_btn_eliminarnotrec, tbbtn_btn_guardarnotrec,
            tbbtn_btn_deshacernotrec;// Botones Nota de Recojo Detalle
    @Wire
    Button btn_buscarrecojos;
    @Wire
    Listbox lst_notreccab, lst_notrecdet;
    @Wire
    Combobox cb_situacion, cb_nrd_tipdoc;
    @Wire
    Textbox txt_venid,
            txt_vennom,// Datos para la busqueda
            txt_nr_id, txt_nr_motcamid, txt_nr_motcamdes, txt_cli_id,
            txt_cli_razsoc, txt_nr_notaent, txt_zon_id, txt_zon_des,
            txt_clidir_direcc, txt_nr_horid, txt_nr_hordes, txt_nr_registro,
            txt_sup_id, txt_nr_venid, txt_nr_vennom,
            txt_nr_transid,
            txt_nr_transdes,// Datos de la Nota Recojo Cabecera
            txt_nrd_prodid, txt_nrd_proddes, txt_nrd_doc, txt_nrd_glosanotrec,
            txt_nrd_serie,// Datos de la Nota Recojo Detalle
            txt_usuadd, txt_usumod;// ------>Datos de la Nota Recojo Cabecera
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Intbox // Datos de la Nota Recojo Cabecera
            // txt_nrd_entero,
            txt_nrd_fraccion, txt_nrd_upre;// Datos de la Nota Recojo Detalle
    @Wire
    Doublebox txt_nrd_entero;

    @Wire
    Checkbox chk_ordvend;// Datos para la Busqueda
    @Wire
    Datebox d_fecini, d_fecfin,// Datos para la Busqueda
            d_nr_fecemi, d_nr_fecent,// Datos de la Nota Recojo Cabecera
            d_fecadd, d_fecmod;// ------>Datos de la Nota Cambio Cabecera
    // Instancias de Objetos
    ListModelList<NotaRecojoCab> objlstNotaRecojoCab;
    ListModelList<NotaRecojoDet> objlstNotaRecojoDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    DaoNotaCambio objDaoNotaCambio;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    Productos objProducto;
    DaoCierreDia objDaoCierreDia;
    Vendedor objVendedor;
    NotaRecojoCab objNotaRecojoCab;
    NotaRecojoDet objNotaRecojoDet;
    DaoNotaRecojo objDaoNotaRecojo;
    Utilitarios objUtilitario;
    Cliente objCliente = new Cliente();
    // Variables publicas
    int i_selCab, i_selDet;
    int i = 0;
    int emp_id, suc_id;
    String campo = "";
    String x = "";
    String fechaActual, s_estado, s_mensaje, s_estadoDetalle;
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    // Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenNotaRecojo.class);

    // Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_notrec")
    public void llenaRegistros() throws SQLException {
        Date fecha = new Date();
        objDaoCierreDia = new DaoCierreDia();
        fechaActual = Utilitarios.hoyAsString();
        objUtilitario = new Utilitarios();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        String vend_id = "%%";
        String situacion = "%%";
        objlstNotaRecojoCab = null;
        objDaoNotaRecojo = new DaoNotaRecojo();
        objlstNotaRecojoCab = objDaoNotaRecojo.listaNotaRecojoCab(emp_id, suc_id, vend_id, situacion, fechaActual, fechaActual, false);
        lst_notreccab.setModel(objlstNotaRecojoCab);
        Utilitarios.sumaFecha(d_nr_fecent, fecha, 1);// Fec Entrega

        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_nrd_tipdoc.setModel(objlstTipDoc);

        //d_fecini.setConstraint("before " + Utilitarios.hoyAsString2());
        //d_fecfin.setConstraint("before " + Utilitarios.hoyAsString2());
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuarioCredential.getCodigo();
        int empresa = objUsuarioCredential.getCodemp();
        int sucursal = objUsuarioCredential.getCodsuc();
        objAccesos = new Accesos();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10203020,
                usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al mantenimiento de Nota de Recojo: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Nota de Recojo con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creacion de una nueva Nota de Recojo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creacion de una nueva Nota de Recojo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edicion de una Nota de Recojo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edicion de una Nota de Recojo");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminacion de una Nota de Recojo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminacion de una Nota de Recojo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Nota de Recojo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Nota de Recojo");
        }
    }

    @Listen("onClick=#btn_buscarrecojos")
    public void busquedaRegistros() throws SQLException {

        objlstNotaRecojoCab = null;
        objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
        lst_notreccab.setModel(objlstNotaRecojoCab);
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

        boolean ordVend = chk_ordvend.isChecked();
        String vend_id = txt_venid.getValue().isEmpty() ? "%%" : txt_venid.getValue().replace("0", "").trim();
        String oc_sit = cb_situacion.getSelectedIndex() == -1 ? "%%" : cb_situacion.getSelectedItem().getValue().toString();
        if (resultado.equals("F1>")) {
            Messagebox.show("La Fecha Inicial no puede ser mayor a la Final", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objlstNotaRecojoCab = objDaoNotaRecojo.listaNotaRecojoCab(emp_id, suc_id, vend_id, oc_sit, f_emisioni, f_emisionf, ordVend);

            // Validar tabla sin registro
            if (objlstNotaRecojoCab.getSize() > 0) {
                lst_notreccab.setModel(objlstNotaRecojoCab);
            } else {
                objlstNotaRecojoCab = null;
                objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
                lst_notreccab.setModel(objlstNotaRecojoCab);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }

            // validacion de eliminacion
            limpiarCampos();
            // Campos del detalle de la Orden de Compra
            objlstNotaRecojoDet = null;
            objlstNotaRecojoDet = new ListModelList<NotaRecojoDet>();
            lst_notrecdet.setModel(objlstNotaRecojoDet);
            limpiaAuditoria();
            limpiarCamposDetalle();

        }
    }

    @Listen("onSelect=#lst_notreccab")
    public void seleccionaRegistro() throws SQLException {
        objNotaRecojoCab = (NotaRecojoCab) lst_notreccab.getSelectedItem().getValue();
        if (objNotaRecojoCab == null) {
            objNotaRecojoCab = objlstNotaRecojoCab.get(i_selCab + 1);
        }
        i_selCab = lst_notreccab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onSelect=#lst_notrecdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaRecojoDet = (NotaRecojoDet) lst_notrecdet.getSelectedItem().getValue();
        if (objNotaRecojoDet == null) {
            objNotaRecojoDet = objlstNotaRecojoDet.get(i_selDet + 1);
        }
        i_selDet = lst_notrecdet.getSelectedIndex();
        limpiarCamposDetalle();
        llenarCamposProducto();
        // validaProductos();
    }

    @Listen("onCheck=#chk_ordvend;")
    public void seleccionarOrden() throws SQLException {
        if (!(objlstNotaRecojoCab == null || objlstNotaRecojoCab.isEmpty())) {
            busquedaRegistros();
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objNotaRecojoCab = null;
        objNotaRecojoCab = new NotaRecojoCab();
        objNotaRecojoDet = null;
        objlstNotaRecojoDet = null;
        objlstNotaRecojoDet = new ListModelList<NotaRecojoDet>();
        lst_notrecdet.setModel(objlstNotaRecojoDet);
        limpiarCampos();
        limpiarCamposDetalle();
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitabotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        Date fecha = new Date();
        Utilitarios.sumaFecha(d_nr_fecent, fecha, 1);// Fec Entrega
        d_nr_fecemi.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_notreccab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objNotaRecojoCab.getNr_sit() != 1) {
                Messagebox.show("Esta Nota de Recojo ya no se Puede Modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitabotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<NotaRecojoDet>();
                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (!objlstNotaRecojoDet.isEmpty()) {
            String s_valida = verificarCab();
            if (!s_valida.equals("")) {
                Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            if (campo.equals("F. Emision")) {
                                d_nr_fecemi.focus();
                            } else if (campo.equals("F. Entrega")) {
                                d_nr_fecent.focus();
                            } else if (campo.equals("M. Cambio")) {
                                txt_nr_motcamid.focus();
                            } else if (campo.equals("Cliente")) {
                                txt_cli_id.focus();
                            } else if (campo.equals("Direccion")) {
                                txt_clidir_id.focus();
                            } else if (campo.equals("Zona")) {
                                txt_zon_id.focus();
                            } else if (campo.equals("Transporte")) {
                                txt_nr_transid.focus();
                            } else if (campo.equals("Vendedor")) {
                                txt_nr_venid.focus();
                            } else if (campo.equals("Horario")) {
                                txt_nr_horid.focus();
                            }
                        }
                    }
                });
            } else {
                if (!txt_cli_id.getValue().matches("[0-9]*")) {
                    lst_notrecdet.focus();
                } else if (!txt_clidir_id.getValue().toString().matches("[0-9]*")) {
                    lst_notrecdet.focus();
                } else if (!txt_nr_motcamid.getValue().matches("[0-9]*")) {
                    lst_notrecdet.focus();
                } else {
                    objCliente = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                    CliDireccion objCliDireccion = new DaoDirecciones().getNomDireccion(txt_cli_id.getValue(), txt_clidir_id.getValue(), emp_id, suc_id);
                    MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(txt_nr_motcamid.getValue());
                    if (objCliente == null || objCliDireccion == null || objMotivoCambio == null) {
                        lst_notrecdet.focus();
                    } else {
                        s_valida = verificarFechas();
                        if (!s_valida.equals("")) {
                            Messagebox.show(s_valida);
                        } else {
                            String s_ed = s_estadoDetalle == null ? "" : s_estadoDetalle;
                            if (s_ed.equals("N")) {
                                Messagebox.show("Guarde detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            } else if (s_ed.equals("E")) {
                                Messagebox.show("Guarde detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            } else {
                                String fecemi = sdf.format(d_nr_fecemi.getValue());
                                if (new DaoCierreDia().ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                                    Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    s_mensaje = "Esta Seguro que desea guardar los cambios?";
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                ParametrosSalida objParamCabecera;
                                                objNotaRecojoCab = (NotaRecojoCab) generaNotaRecojoCabecera();
                                                if (s_estado.equals("N")) {
                                                    objParamCabecera = objDaoNotaRecojo.insertarNotaRecojoCab(objNotaRecojoCab);
                                                } else {
                                                    objParamCabecera = objDaoNotaRecojo.modificarNotaRecojoCab(objNotaRecojoCab);
                                                }
                                                if (objParamCabecera.getFlagIndicador() == 0) {
                                                    ParametrosSalida objParamDetalle = new ParametrosSalida();
                                                    boolean verificarDetalle = true;
                                                    int i = 0;
                                                    if (s_estado.equals("N")) {
                                                        while (i < objlstNotaRecojoDet.getSize() && verificarDetalle) {
                                                            long nr_key = Long.parseLong(objParamCabecera.getCodigoRegistro());
                                                            objlstNotaRecojoDet.get(i).setNr_key(nr_key);
                                                            objParamDetalle = objDaoNotaRecojo.insertarNotaRecojoDet(objlstNotaRecojoDet.get(i));
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un Error con el Producto " + objlstNotaRecojoDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                verificarDetalle = false;
                                                            }
                                                            i++;
                                                        }
                                                    } else {
                                                        // OPERACION DE ELINACION DE PRODUCTOS DE LA NOTA DE RECOJO                                  
                                                        if (!objlstEliminacion.isEmpty()) {
                                                            while (i < objlstEliminacion.getSize() && verificarDetalle) {
                                                                objParamDetalle = objDaoNotaRecojo.eliminarNotaRecojoDet(objlstEliminacion.get(i));
                                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                                    Messagebox.show("Ocurrio un Error con el Item" + objlstEliminacion.get(i).getNrd_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                    verificarDetalle = false;
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                        // OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA NOTA DE RECOJO
                                                        i = 0;
                                                        verificarDetalle = true;
                                                        while (i < objlstNotaRecojoDet.getSize() && verificarDetalle) {
                                                            String indicador = objlstNotaRecojoDet.get(i).getInd_accion();
                                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                                objParamDetalle = objDaoNotaRecojo.insertarNotaRecojoDet(objlstNotaRecojoDet.get(i));
                                                            } else if (indicador.equals("M")) {
                                                                objParamDetalle = objDaoNotaRecojo.modificarNotaRecojoDet(objlstNotaRecojoDet.get(i));
                                                            }
                                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                                Messagebox.show("Ocurrio un Error con el Producto " + objlstNotaRecojoDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
            Messagebox.show("No Puede Guardar una Nota de Recojo con Detalle Vacio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_notreccab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta Nota de Recojo?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        ParametrosSalida objParamSalida;
                        objParamSalida = objDaoNotaRecojo.eliminarNotaRecojoCab(objNotaRecojoCab);
                        if (objParamSalida.getFlagIndicador() == 0) {
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
                    limpiarCampos();
                    // Campos del detalle de la Orden de Compra
                    habilitabotonesDetalle(true, true);
                    habilitaCamposDetalle(true);
                    limpiarCamposDetalle();
                    limpiarTabLista(2);
                    busquedaRegistros();

                }
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_nuevonotrec")
    public void nuevoDetalleRecojo() {
        String verificar = verificarCab();
        if (!verificar.equals("")) {
            Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("F. Emision")) {
                            d_nr_fecemi.focus();
                        } else if (campo.equals("F. Entrega")) {
                            d_nr_fecent.focus();
                        } else if (campo.equals("M. Cambio")) {
                            txt_nr_motcamid.focus();
                        } else if (campo.equals("Cliente")) {
                            txt_cli_id.focus();
                        } else if (campo.equals("Direccion")) {
                            txt_clidir_id.focus();
                        } else if (campo.equals("Zona")) {
                            txt_zon_id.focus();
                        } else if (campo.equals("Transporte")) {
                            txt_nr_transid.focus();
                        } else if (campo.equals("Vendedor")) {
                            txt_venid.focus();
                        } else if (campo.equals("Horario")) {
                            txt_nr_horid.focus();
                        }
                    }
                }
            });

        } else {
            s_estadoDetalle = "N";
            habilitaCamposDetalle(false);
            tbbtn_btn_guardarnotrec.setDisabled(false);
            limpiarCamposDetalle();
            habilitaCamposNR(true);
            habilitabotonesDetalle(true, false);
            Utilitarios.deshabilitarLista(true, lst_notrecdet);
            txt_nrd_prodid.focus();
        }
    }

    @Listen("onClick=#tbbtn_btn_editarnotrec")
    public void modificaDetalleRecojo() {
        if (objlstNotaRecojoDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Recojo a Editar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notrecdet.getSelectedIndex() == -1) {
                Messagebox.show("Por favor Seleccione un Registro del Detalle de Nota de Recojo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {

                habilitaCamposDetalle(false);
                habilitaCamposNR(true);
                txt_nrd_prodid.setDisabled(true);
                txt_nrd_doc.setDisabled(true);
                txt_nrd_doc.setDisabled(true);
                tbbtn_btn_guardarnotrec.setDisabled(false);
                habilitabotonesDetalle(true, false);
                Utilitarios.deshabilitarLista(true, lst_notrecdet);
                s_estadoDetalle = "E";
                txt_nrd_entero.focus();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardarnotrec")
    public void agregarDetalle() throws SQLException {
        String validaProducto = verificarDetalle();
        if (!validaProducto.equals("")) {
            Messagebox.show(validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Producto")) {
                            txt_nrd_prodid.focus();
                        } else if (campo.equals("Tip. Doc")) {
                            cb_nrd_tipdoc.focus();
                        } else if (campo.equals("Serie")) {
                            txt_nrd_serie.focus();
                        } else if (campo.equals("Doc. Ref")) {
                            txt_nrd_doc.focus();
                        } else if (campo.equals("Cant. Entero")) {
                            txt_nrd_entero.focus();
                        } else if (campo.equals("Cant. Fraccion")) {
                            txt_nrd_fraccion.focus();
                        } else if (campo.equals("Fraccion")) {
                            txt_nrd_fraccion.focus();
                        }
                    }
                }
            });
        } else {
            String pro_id = txt_nrd_prodid.getValue();
            if (!pro_id.matches("[0-9]*")) {
                lst_notrecdet.focus();
            } else {
                objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                if (objProducto == null) {
                    lst_notrecdet.focus();
                } else {
                    if (s_estado.equals("N")) {
                        if (s_estadoDetalle.equals("N")) {
                            String valida = validaIngresoDetalle(pro_id);
                            if (valida.equals("")) {
                                objNotaRecojoDet = (NotaRecojoDet) generaNotaRecojoDetalle();
                                objlstNotaRecojoDet.add(objNotaRecojoDet);
                                lst_notrecdet.setModel(objlstNotaRecojoDet);
                            } else {
                                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            objNotaRecojoDet = (NotaRecojoDet) generaNotaRecojoDetalle();
                            int lst_ord = lst_notrecdet.getSelectedIndex();
                            objlstNotaRecojoDet.set(lst_ord, objNotaRecojoDet);
                        }
                    } else {
                        if (s_estadoDetalle.equals("N")) {
                            String valida = validaIngresoDetalle(pro_id);
                            if (valida.equals("")) {
                                objNotaRecojoDet = (NotaRecojoDet) generaNotaRecojoDetalle();
                                objNotaRecojoDet.setInd_accion("N");
                                objlstNotaRecojoDet.add(objNotaRecojoDet);
                            } else {
                                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            String indicador = objNotaRecojoDet.getInd_accion();
                            if (indicador.equals("N") || indicador.equals("NM")) {
                                objNotaRecojoDet = (NotaRecojoDet) generaNotaRecojoDetalle();
                                objNotaRecojoDet.setInd_accion("NM");
                            } else {
                                long item = objNotaRecojoDet.getNrd_item();
                                objNotaRecojoDet = (NotaRecojoDet) generaNotaRecojoDetalle();
                                objNotaRecojoDet.setInd_accion("M");
                                objNotaRecojoDet.setNrd_item(item);
                            }
                            objlstNotaRecojoDet.set(lst_notrecdet.getSelectedIndex(), objNotaRecojoDet);
                        }
                    }
                    s_estadoDetalle = "";
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    habilitaCamposNR(false);
                    habilitabotonesDetalle(false, true);
                    tbbtn_btn_guardarnotrec.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_notrecdet);
                    lst_notrecdet.clearSelection();
                    lst_notrecdet.setFocus(true);
                    lst_notrecdet.focus();
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarnotrec")
    public void eliminaDetalleRecojo() {
        if (objlstNotaRecojoDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Recojo a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notrecdet.getSelectedIndex() == -1) {
                Messagebox.show("Por Favor Seleccione un Registro a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objNotaRecojoDet = lst_notrecdet.getSelectedItem().getValue();
                int posicion = lst_notrecdet.getSelectedIndex();
                if (s_estado.equals("E") && (!objNotaRecojoDet.getInd_accion().equals("N") || !objNotaRecojoDet.getInd_accion().equals("NM"))) {
                    long nrd_item = objNotaRecojoDet.getNrd_item();
                    long nr_key = Long.parseLong(txt_nr_id.getValue());
                    String nrd_usumod = objUsuarioCredential.getCuenta();
                    String nrd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
                    objlstEliminacion.add(new NotaRecojoDet(nr_key, emp_id, suc_id, nrd_item, nrd_usumod, nrd_pcmod));
                }
                objlstNotaRecojoDet.remove(posicion);
                limpiarCamposDetalle();
                tbbtn_btn_guardarnotrec.setDisabled(true);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacernotrec")
    public void deshacerProducto() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    s_estadoDetalle = "";
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    habilitabotonesDetalle(false, true);
                    habilitaCamposNR(false);
                    tbbtn_btn_guardarnotrec.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_notrecdet);
                    lst_notrecdet.clearSelection();
                }
                lst_notrecdet.setFocus(true);
                lst_notrecdet.focus();

            }
        });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaRecojoCab == null || objlstNotaRecojoCab.isEmpty()) {
            Messagebox.show("No hay Registros de Generar Notas de Recojo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notreccab.getSelectedIndex() >= 0) {
                objNotaRecojoCab = (NotaRecojoCab) lst_notreccab.getSelectedItem().getValue();
                if (objNotaRecojoCab == null) {
                    objNotaRecojoCab = objlstNotaRecojoCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
                objMapObjetos.put("codigoNotaRecojo", objNotaRecojoCab.getNr_id());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionNotaRecojo.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione Registro de Nota de Recojo para imprimir");
            }
        }
    }

    // Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_gennotrec")
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
                if (!tbbtn_btn_nuevonotrec.isDisabled()) {
                    nuevoDetalleRecojo();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarnotrec.isDisabled()) {
                    modificaDetalleRecojo();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarnotrec.isDisabled()) {
                    eliminaDetalleRecojo();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarnotrec.isDisabled()) {
                    agregarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacernotrec.isDisabled()) {
                    deshacerProducto();
                }
                break;
        }
    }

    @Listen("onOK=#d_nr_fecemi")
    public void validaFechaEmision() throws SQLException {
        if (d_nr_fecemi.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Emision de la Nota de Recojo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String fecemi = sdf.format(d_nr_fecemi.getValue());
            if (objDaoCierreDia.ValidaDia(fecemi, "L") == null || objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                d_nr_fecent.focus();
            }
        }
    }

    @Listen("onOK=#d_nr_fecent")
    public void validaFechaEntrega() {
        if (d_nr_fecent.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Emision de la Nota de Recojo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (verificarFechas().isEmpty()) {
                txt_nr_motcamid.focus();
            } else {
                Messagebox.show(verificarFechas(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onOK=#txt_cli_id")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaRecojo";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("txt_zon_id", txt_zon_id);
                objMapObjetos.put("txt_zon_des", txt_zon_des);
                objMapObjetos.put("txt_nr_venid", txt_nr_venid);
                objMapObjetos.put("txt_sup_id", txt_sup_id);
                objMapObjetos.put("txt_nr_vennom", txt_nr_vennom);
                objMapObjetos.put("txt_nr_transid", txt_nr_transid);
                objMapObjetos.put("txt_nr_transdes", txt_nr_transdes);
                objMapObjetos.put("txt_nr_horid", txt_nr_horid);
                objMapObjetos.put("txt_nr_hordes", txt_nr_hordes);
                objMapObjetos.put("txt_clidir_id", txt_clidir_id);
                objMapObjetos.put("txt_clidir_direcc", txt_clidir_direcc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojo");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaClientes();
            }
        }
    }

    @Listen("onOK=#txt_clidir_id")
    public void lov_direcciones() {
        if (bandera == false) {
            bandera = true;
            if (!txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() == null) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaRecojo";
                objMapObjetos.put("txt_dirid", txt_clidir_id);
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_dirdes", txt_clidir_direcc);
                objMapObjetos.put("txt_zonid", txt_zon_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojo");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDirecciones.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_zon_id.focus();
            }
        }
    }

    @Listen("onOK=#txt_nr_motcamid")
    public void lov_motCambio() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nr_motcamid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaRecojo";
                objMapObjetos.put("txt_motid", txt_nr_motcamid);
                objMapObjetos.put("txt_motdes", txt_nr_motcamdes);
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojo");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoCambio.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaMotivoCambio();
            }
        }
    }

    @Listen("onOK=#txt_nrd_prodid")
    public void lov_productos() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nrd_prodid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaRecojo";
                objMapObjetos.put("txt_proid", txt_nrd_prodid);
                objMapObjetos.put("txt_prodes", txt_nrd_proddes);
                objMapObjetos.put("txt_nrd_entero", txt_nrd_entero);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojo");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaProducto();
            }
        }
    }

    @Listen("onOK=#txt_venid")
    public void lov_vendedores() {
        if (bandera == false) {
            bandera = true;
            if (txt_venid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                Textbox txt_supid = new Textbox();
                String modoEjecucion = "NotaRecojo";
                objMapObjetos.put("txt_venid", txt_venid);
                objMapObjetos.put("txt_vennom", txt_vennom);
                objMapObjetos.put("txt_sup_id", txt_supid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaRecojo");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onOK=#cb_nrd_tipdoc")
    public void validaTipoDocumento() {
        if (cb_nrd_tipdoc.getSelectedIndex() == -1) {
            Messagebox.show("Seleccione 'Tipo de Documento'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        cb_nrd_tipdoc.focus();
                    }
                }
            });
        } else {
            txt_nrd_serie.focus();
        }
    }

    @Listen("onOK=#txt_nrd_doc")
    public void validaDocumento() {
        if (!txt_nrd_doc.getValue().matches("^[0-9]*")) {
            Messagebox.show("En el campo 'Documento' debe ingresar valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nrd_doc.focus();
                    }
                }
            });

        } else if (txt_nrd_doc.getValue().length() != 7 && txt_nrd_doc.getValue().length() != 8) {
            Messagebox.show("En el campo 'Documento' debe ingresar 7 o 8 caracteres", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nrd_doc.focus();
                    }
                }
            });
        } else if (Integer.parseInt(txt_nrd_doc.getValue()) <= 0) {
            Messagebox.show("En el campo 'Documento' debe ingresar valores numericos mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nrd_doc.focus();
                    }
                }
            });

        } else {
            txt_nrd_glosanotrec.focus();
        }
    }

    @Listen("onOK=#txt_nrd_entero")
    public void validaEntero() {
        if (txt_nrd_entero.getValue() != null) {

            //if(!txt_nrd_entero.getValue().toString().trim().matches("[0-9]*")){
            String s_calculo = String.valueOf(txt_nrd_entero.getValue());
            String[] cadena = s_calculo.split("[.]");

            int ultimo = Integer.parseInt(cadena[1]);
            if (ultimo > 0) {
                double cant = cadena[1].length() == 1
                        ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nrd_upre.getValue()
                        : (double) Integer.parseInt(cadena[1]) / 100 * txt_nrd_upre.getValue();
                int fraccion = (int) cant;

                txt_nrd_entero.setValue(Integer.parseInt(cadena[0]));
                txt_nrd_fraccion.setValue(fraccion);
                txt_nrd_fraccion.focus();
            } else {
                /*txt_ncd_fraccion.focus();*/
                if (txt_nrd_fraccion.getValue() != null) {
                    txt_nrd_fraccion.focus();
                } else {
                    txt_nrd_fraccion.setValue(0);
                    txt_nrd_fraccion.focus();
                }

            }
            //}

        } else {
            txt_nrd_fraccion.focus();
        }
    }

    @Listen("onOK=#txt_nrd_fraccion")
    public void validaFraccion() {
        if (txt_nrd_prodid.getValue().isEmpty()) {
            Messagebox.show("Ingrese Producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        limpiarCamposDetalleProducto();
                        txt_nrd_prodid.focus();
                    }
                }
            });
        } else {
            if (txt_nrd_entero.getValue() == null) {
                if (txt_nrd_fraccion.getValue() != null) {
                    if (txt_nrd_upre.getValue() <= txt_nrd_fraccion.getValue()) {
                        double d_calculo = txt_nrd_fraccion.getValue() / txt_nrd_upre.getValue();
                        String s_calculo = String.valueOf(d_calculo);
                        String[] cadena = s_calculo.split("[.]");
                        int d_calculofrac = txt_nrd_fraccion.getValue() % txt_nrd_upre.getValue();
                        txt_nrd_entero.setValue(Integer.parseInt(cadena[0]));
                        txt_nrd_fraccion.setValue(d_calculofrac);
                        cb_nrd_tipdoc.focus();

                    } else {
                        txt_nrd_entero.setValue(0);
                        cb_nrd_tipdoc.focus();
                    }
                } else {
                    Messagebox.show("Ingrese Cantidad Entero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_entero.focus();

                            }
                        }
                    });
                }
            } else {
                String s_cal = String.valueOf(txt_nrd_entero.getValue());
                String[] cad = s_cal.split("[.]");
                int i_ent = Integer.parseInt(cad[0]);
                if (txt_nrd_upre.getValue() <= txt_nrd_fraccion.getValue()) {

                    double d_calculo = txt_nrd_fraccion.getValue() / txt_nrd_upre.getValue();
                    String s_calculo = String.valueOf(d_calculo);
                    String[] cadena = s_calculo.split("[.]");
                    int d_calculofrac = txt_nrd_fraccion.getValue() % txt_nrd_upre.getValue();

                    txt_nrd_entero.setValue(Integer.parseInt(cadena[0]) + i_ent);
                    txt_nrd_fraccion.setValue(d_calculofrac);
                    cb_nrd_tipdoc.focus();
                } else {
                    cb_nrd_tipdoc.focus();
                }
            }
        }

    }

    @Listen("onOK=#txt_nrd_glosanotrec")
    public void validaGlosa() throws SQLException {
        agregarDetalle();
    }

    @Listen("onBlur=#txt_clidir_id")
    public void validaDirecciones() throws SQLException {
        if (txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() != null) {
            Messagebox.show("Por favor Verifique Primero El Codigo del Cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_cli_id.setValue("");
                        txt_cli_razsoc.setValue("");
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
                    CliDireccion objCliDireccion = new DaoDirecciones().getNomDireccion(cli_id, clidir_id, emp_id, suc_id);
                    if (objCliDireccion == null) {
                        Messagebox.show("El Codigo de Direccion no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_clidir_id.setValue(null);
                                    txt_clidir_direcc.setValue("");
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
                        txt_nr_transid.setValue(objCliDireccion.getTrans_id());
                        txt_nr_transdes.setValue(objCliDireccion.getTrans_des());
                        txt_nr_venid.setValue(objCliDireccion.getVen_id());
                        txt_nr_vennom.setValue(objCliDireccion.getVen_apenom());
                        txt_nr_horid.setValue(objCliDireccion.getHor_id());
                        txt_nr_hordes.setValue(objCliDireccion.getHor_des());

                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nrd_prodid")
    public void validaProductos() throws SQLException {
        String prod = txt_nrd_prodid.getValue() == null ? "" : txt_nrd_prodid.getValue();
        if (i == 0) {
            txt_nrd_proddes.setValue("");
            if (!txt_nrd_prodid.getValue().isEmpty()) {
                if (!txt_nrd_prodid.getValue().matches("[0-9]*")) {
                    Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event)
                                throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_prodid.setValue("");
                                txt_nrd_proddes.setValue("");
                                txt_nrd_entero.setValue(null);
                                txt_nrd_fraccion.setValue(null);
                                txt_nrd_upre.setValue(null);
                                txt_nrd_prodid.focus();
                            }
                        }
                    });
                } else {
                    String pro_id = txt_nrd_prodid.getValue();
                    objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                    if (objProducto == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nrd_prodid.setValue("");
                                    txt_nrd_proddes.setValue("");
                                    txt_nrd_entero.setValue(null);
                                    txt_nrd_fraccion.setValue(null);
                                    txt_nrd_upre.setValue(null);
                                    txt_nrd_prodid.focus();
                                }
                            }
                        });
                    } else {
                        i = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nrd_prodid.setValue(objProducto.getPro_id());
                        txt_nrd_proddes.setValue(objProducto.getPro_des());
                        txt_nrd_upre.setValue(objProducto.getPro_presminven());
                    }
                }
            } else {
                i = 0;
            }
        } else {
            txt_nrd_proddes.setValue("");
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
                            txt_venid.setValue("");
                            txt_vennom.setValue("");
                            txt_venid.focus();
                        }
                    }
                });
            } else {
                int ven_id = Integer.parseInt(txt_venid.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El Codigo de Vendedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_venid.setValue("");
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

    @Listen("onBlur=#txt_nr_motcamid")
    public void validaMotCambio() throws SQLException {
        txt_nr_motcamdes.setValue("");
        if (!txt_nr_motcamid.getValue().isEmpty()) {
            if (!txt_nr_motcamid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_nr_motcamid.setValue("");
                            txt_nr_motcamdes.setValue("");
                            txt_nr_motcamid.focus();
                        }
                    }
                });

            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_nr_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    Messagebox.show("El Codigo del Motivo de Cambio no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event)
                                throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nr_motcamid.setValue("");
                                txt_nr_motcamdes.setValue("");
                                txt_nr_motcamid.focus();
                            }
                        }
                    });

                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_nr_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_nr_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                }
            }
        }
        bandera = false;
    }

    @Listen("onOK=#txt_nrd_serie")
    public void validaSerie() {
        if (!txt_nrd_serie.getValue().isEmpty()) {
            if (txt_nrd_serie.getValue().matches("^[0-9]*") || txt_nrd_serie.getValue().matches("^\\w*")) {
                if (txt_nrd_serie.getValue().matches("^[0-9]*") && txt_nrd_serie.getValue().length() <= 2) {
                    Messagebox.show("Debe ingresar valores  mayores a 2 en  'Serie'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_serie.focus();
                            }
                        }
                    });
                } else if (txt_nrd_serie.getValue().matches("^[0-9]*") && Integer.parseInt(txt_nrd_serie.getValue()) <= 0) {
                    Messagebox.show("En el campo 'Serie' debe ingresar numero mayor a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_serie.focus();
                            }
                        }
                    });

                } else if (txt_nrd_serie.getValue().matches("^\\w*") && txt_nrd_serie.getValue().length() <= 2) {
                    Messagebox.show("En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_serie.focus();
                            }
                        }
                    });
                } else {
                    txt_nrd_doc.focus();
                }
            } else {
                Messagebox.show("Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_nrd_serie.focus();
                        }
                    }
                });
            }
        } else {
            txt_nrd_doc.focus();
        }
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClienteNotRec() throws SQLException {
        if (txt_cli_id.getValue().isEmpty()) {
            limpiarCliDirVacio();
        } else {
            if (!txt_cli_id.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCliDirVacio();
                            txt_cli_id.focus();
                        }
                    }
                });

            } else {
                String s_idcliente = txt_cli_id.getValue();
                objCliente = new DaoCliente().getDireccionDefault(s_idcliente, objUsuarioCredential.getCodemp(), objUsuarioCredential.getCodsuc());
                if (objCliente == null) {
                    Messagebox.show("El Codigo del Cliente no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarCliDirVacio();
                                txt_cli_id.focus();
                            }
                        }
                    });
                } else {
                    txt_cli_id.setValue(objCliente.getCli_id());
                    txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
                    txt_zon_id.setValue(objCliente.getZon_id());
                    txt_zon_des.setValue(objCliente.getZon_des());
                    txt_nr_venid.setValue(objCliente.getVen_id());
                    txt_sup_id.setValue(objCliente.getSup_id());
                    txt_nr_vennom.setValue(objCliente.getVen_apenom());
                    if (objCliente.getHor_id() == null) {
                        Messagebox.show("El Codigo del Cliente no tiene Direccion asignada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCliDirVacio();
                                    txt_cli_id.focus();
                                }
                            }
                        });
                    } else {
                        txt_nr_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                        txt_nr_hordes.setValue(objCliente.getHor_des());
                        txt_nr_transid.setValue(objCliente.getTrans_id());
                        txt_nr_transdes.setValue(objCliente.getTrans_alias());
                        txt_clidir_id.setValue(objCliente.getClidir_id());
                        txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                        txt_clidir_id.focus();
                    }

                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nrd_entero")
    public void validaBlurEntero() {
        if (txt_nrd_entero.getValue() != null) {
            String s_calculo = String.valueOf(txt_nrd_entero.getValue());
            String[] cadena = s_calculo.split("[.]");

            int ultimo = Integer.parseInt(cadena[1]);
            if (ultimo > 0) {
                double cant = cadena[1].length() == 1
                        ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nrd_upre.getValue()
                        : (double) Integer.parseInt(cadena[1]) / 100 * txt_nrd_upre.getValue();
                int fraccion = (int) cant;

                txt_nrd_entero.setValue(Integer.parseInt(cadena[0]));
                txt_nrd_fraccion.setValue(fraccion);

            } else {
                /*txt_ncd_fraccion.focus();*/
                if (txt_nrd_fraccion.getValue() == null) {
                    txt_nrd_fraccion.setValue(0);
                }

                //else {  txt_nrd_fraccion.setValue(0);  txt_nrd_fraccion.focus(); }
            }

            /*if (txt_nrd_fraccion.getValue() == null) {
             txt_nrd_fraccion.setValue(0);
             }*/
        } else {
            txt_nrd_fraccion.focus();
        }

    }

    @Listen("onBlur=#txt_nrd_fraccion")
    public void validaBlurFraccion() {
        if (!txt_nrd_prodid.getValue().isEmpty() && txt_nrd_prodid.getValue().matches("[0-9]*")) {
            if (txt_nrd_fraccion.getValue() == null && txt_nrd_entero.getValue() != null) {
                txt_nrd_fraccion.setValue(0);
            } else {
                if (txt_nrd_entero.getValue() == null) {
                    if (txt_nrd_fraccion.getValue() != null) {
                        if (txt_nrd_upre.getValue() <= txt_nrd_fraccion.getValue()) {
                            double d_calculosal = txt_nrd_fraccion.getValue() / txt_nrd_upre.getValue();
                            String s_calculosal = String.valueOf(d_calculosal);
                            String[] cadena = s_calculosal.split("[.]");
                            int d_calculofrac = txt_nrd_fraccion.getValue() % txt_nrd_upre.getValue();

                            txt_nrd_entero.setValue(Integer.parseInt(cadena[0]));
                            txt_nrd_fraccion.setValue(d_calculofrac);
                        }
                    }
                } else {
                    String s_cal = String.valueOf(txt_nrd_entero.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_ent = Integer.parseInt(cad[0]);
                    if (txt_nrd_upre.getValue() <= txt_nrd_fraccion.getValue()) {

                        double d_calculo = txt_nrd_fraccion.getValue() / txt_nrd_upre.getValue();
                        String s_calculo = String.valueOf(d_calculo);
                        String[] cadena = s_calculo.split("[.]");
                        int d_calculofrac = txt_nrd_fraccion.getValue() % txt_nrd_upre.getValue();

                        txt_nrd_entero.setValue(Integer.parseInt(cadena[0]) + i_ent);
                        txt_nrd_fraccion.setValue(d_calculofrac);
                    }
                }
            }
        }
    }

    // Eventos Otros
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
                    txt_nr_venid.setValue(objCliente.getVen_id());
                    txt_sup_id.setValue(objCliente.getSup_id());
                    txt_nr_vennom.setValue(objCliente.getVen_apenom());
                    if (objCliente.getHor_id() != null) {
                        txt_nr_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                        txt_nr_hordes.setValue(objCliente.getHor_des());
                        txt_nr_transid.setValue(objCliente.getTrans_id());
                        txt_nr_transdes.setValue(objCliente.getTrans_alias());
                        txt_clidir_id.setValue(objCliente.getClidir_id());
                        txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                    }
                    txt_clidir_id.focus();
                }
            }
        }
        bandera = false;
    }

    public void validaProducto() throws SQLException {
        String prod = txt_nrd_prodid.getValue() == null ? "" : txt_nrd_prodid.getValue();
        if (i == 0) {
            txt_nrd_proddes.setValue("");
            if (!txt_nrd_prodid.getValue().isEmpty()) {
                if (!txt_nrd_prodid.getValue().matches("[0-9]*")) {
                    txt_nrd_entero.focus();
                } else {
                    String pro_id = txt_nrd_prodid.getValue() == null ? "" : txt_nrd_prodid.getValue();
                    objProducto = new DaoProductos().buscarProducto(pro_id, "%%");
                    if (objProducto == null) {
                        txt_nrd_entero.focus();
                    } else {
                        i = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nrd_prodid.setValue(objProducto.getPro_id());
                        x = txt_nrd_prodid.getValue();
                        txt_nrd_proddes.setValue(objProducto.getPro_des());
                        txt_nrd_upre.setValue(objProducto.getPro_presminven());
                        txt_nrd_entero.focus();
                    }
                }
            } else {
                i = 0;
            }
        } else {
            txt_nrd_proddes.setValue("");
            if (prod.equals(x)) {
                if (!prod.isEmpty()) {
                    objProducto = new DaoProductos().buscarProducto(prod, "%%");
                    if (objProducto == null) {
                        txt_nrd_entero.focus();
                    } else {
                        txt_nrd_prodid.setValue(objProducto.getPro_id());
                        x = txt_nrd_prodid.getValue();
                        txt_nrd_proddes.setValue(objProducto.getPro_des());
                        txt_nrd_upre.setValue(objProducto.getPro_presminven());
                        txt_nrd_entero.focus();
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                    }
                }

            } else {
                if (!prod.isEmpty()) {
                    objProducto = new DaoProductos().buscarProducto(prod, "%%");
                    if (objProducto == null) {
                        txt_nrd_entero.focus();
                    } else {
                        txt_nrd_prodid.setValue(objProducto.getPro_id());
                        x = txt_nrd_prodid.getValue();
                        txt_nrd_proddes.setValue(objProducto.getPro_des());
                        txt_nrd_upre.setValue(objProducto.getPro_presminven());
                        txt_nrd_entero.setValue(null);
                        txt_nrd_fraccion.setValue(null);
                        txt_nrd_entero.focus();
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                    }
                }
            }
        }

        bandera = false;
    }

    public void cargaDatosProd(String prod, int elicant) throws SQLException {
        if (!prod.isEmpty()) {
            objProducto = new DaoProductos().buscarProducto(prod, "%%");
            if (elicant == 0) {
                if (objProducto == null) {
                    Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_prodid.setValue("");
                                txt_nrd_proddes.setValue("");
                                txt_nrd_entero.setValue(null);
                                txt_nrd_fraccion.setValue(null);
                                txt_nrd_upre.setValue(null);
                                txt_nrd_prodid.focus();
                            }
                        }
                    });
                } else {
                    txt_nrd_prodid.setValue(objProducto.getPro_id());
                    x = txt_nrd_prodid.getValue();
                    txt_nrd_proddes.setValue(objProducto.getPro_des());
                    txt_nrd_upre.setValue(objProducto.getPro_presminven());
                    txt_nrd_entero.setValue(null);
                    txt_nrd_fraccion.setValue(null);

                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                }
            } else {
                if (objProducto == null) {
                    Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event)
                                throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nrd_prodid.setValue("");
                                txt_nrd_proddes.setValue("");
                                txt_nrd_entero.setValue(null);
                                txt_nrd_fraccion.setValue(null);
                                txt_nrd_upre.setValue(null);
                                txt_nrd_prodid.focus();
                            }
                        }
                    });
                } else {
                    txt_nrd_prodid.setValue(objProducto.getPro_id());
                    x = txt_nrd_prodid.getValue();
                    txt_nrd_proddes.setValue(objProducto.getPro_des());
                    txt_nrd_upre.setValue(objProducto.getPro_presminven());

                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos de el Producto con Codigo " + objProducto.getPro_id() + " y ha encontrado 1 registro(s)");

                }

            }
        } else {
            txt_nrd_prodid.setValue("");
            txt_nrd_proddes.setValue("");
            txt_nrd_entero.setValue(null);
            txt_nrd_fraccion.setValue(null);
            txt_nrd_upre.setValue(null);
        }
    }

    public String verificarCab() {
        String validar;
        if (d_nr_fecemi.getValue() == null) {
            validar = "El campo F. Emision es Obligatorio";
            campo = "F. Emision";
        } else if (d_nr_fecent.getValue() == null) {
            validar = "El campo F. Entrega es Obligatorio";
            campo = "F. Entrega";
        } else if (txt_nr_motcamid.getValue().isEmpty()) {
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
        } else if (txt_nr_transid.getValue().isEmpty()) {
            validar = "El campo Transporte es Obligatorio";
            campo = "Transporte";
        } else if (txt_nr_venid.getValue().isEmpty()) {
            validar = "El campo Vendedor es Obligatorio";
            campo = "Vendedor";
        } else if (txt_nr_horid.getValue().isEmpty()) {
            validar = "El campo Horario es Obligatorio";
            campo = "Horario";
        } else {
            validar = "";
        }
        return validar;
    }

    public String verificarDetalle() throws SQLException {
        String validar;
        if (txt_nrd_prodid.getValue().isEmpty()) {
            validar = "El campo Producto es obligatorio";
            campo = "Producto";
        } else if (txt_nrd_entero.getValue() == null) {
            validar = "El campo 'Cant.Entero' es obligatorio";
            campo = "Cant. Entero";
        } else if (txt_nrd_fraccion.getValue() == null) {
            validar = "El campo 'Cant.Fraccion' es obligatorio";
            campo = "Cant. Fraccion";
        } else {
            objProducto = new DaoProductos().listaPro(txt_nrd_prodid.getValue());
            if (objProducto != null && objProducto.getPro_presminven() < txt_nrd_fraccion.getValue()) {
                validar = "Cant.Frac supera a la unidad de presentacion";
                campo = "Cant. Fraccion";
            } else if (txt_nrd_entero.getValue() == 0
                    && txt_nrd_fraccion.getValue() == 0) {
                validar = "Debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
            } else if (cb_nrd_tipdoc.getSelectedIndex() == -1) {
                validar = "Seleccione 'Tipo de Documento'";
                campo = "Tip. Doc";
            } else if (txt_nrd_serie.getValue().isEmpty()) {
                validar = "El campo 'Serie' es obligatorio";
                campo = "Serie";
            } else if (!txt_nrd_serie.getValue().isEmpty() && !(txt_nrd_serie.getValue().matches("^[0-9]*") || txt_nrd_serie.getValue().matches("^\\w*"))) {
                txt_nrd_serie.setValue("");
                validar = "Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico";
                campo = "Serie";
            } else if (!txt_nrd_serie.getValue().isEmpty() && txt_nrd_serie.getValue().matches("^[0-9]*") && txt_nrd_serie.getValue().length() <= 2) {
                validar = "Debe ingresar valores numericos mayores a 2 en el campo 'Serie'";
                campo = "Serie";
            } else if (!txt_nrd_serie.getValue().isEmpty() && txt_nrd_serie.getValue().matches("^[0-9]*") && Integer.parseInt(txt_nrd_serie.getValue()) <= 0) {
                validar = "En el campo 'Serie' debe ingresar numero mayor a 0";
                campo = "Serie";
            } else if (!txt_nrd_serie.getValue().isEmpty() && txt_nrd_serie.getValue().matches("^\\w*") && txt_nrd_serie.getValue().length() <= 2) {
                txt_nrd_serie.setValue("");
                validar = "En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2";
                campo = "Serie";
            } else if (txt_nrd_doc.getValue().isEmpty()) {
                validar = "El campo 'Documento' es obligatorio";
                campo = "Doc";
            } else if (!txt_nrd_doc.getValue().matches("[0-9]*")) {
                txt_nrd_doc.setValue("");
                validar = "Ingrese datos numericos en el campo 'Documento'";
                campo = "Doc";
            } else if (txt_nrd_doc.getValue().length() != 7 && txt_nrd_doc.getValue().length() != 8) {
                validar = "En el campo 'Documento' debe ingresar 7 o 8 caracteres";
                campo = "Doc";
            } else if (Integer.parseInt(txt_nrd_doc.getValue()) <= 0) {
                validar = "En el campo 'Documento' debe ingresar valores numericos mayores a 0";
                campo = "Doc";
            } else {
                validar = "";
            }

        }
        return validar;
    }

    public String validaIngresoDetalle(String pro_id) {
        i = 0;
        String msj_prod = "";
        int cantDet = objlstNotaRecojoDet.getSize();
        boolean validaIngreso = true;
        String serie = txt_nrd_serie.getValue().isEmpty() ? "" : txt_nrd_serie.getValue().toUpperCase();
        while (i < cantDet && validaIngreso) {
            if (objlstNotaRecojoDet.get(i).getPro_id().equals(pro_id)) {
                msj_prod = "El producto ya fue Ingresado";
                validaIngreso = false;
            } else if (objlstNotaRecojoDet.get(i).getNrd_docref().equals(serie + txt_nrd_doc.getValue())) {
                msj_prod = "El Doc .Ref ya fue ingresado";
                validaIngreso = false;
            }
            i++;
        }
        return msj_prod;
    }

    public void validaMotivoCambio() throws SQLException {
        txt_nr_motcamdes.setValue("");
        if (!txt_nr_motcamid.getValue().isEmpty()) {
            if (!txt_nr_motcamid.getValue().matches("[0-9]*")) {
                d_nr_fecent.focus();
            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_nr_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    d_nr_fecent.focus();

                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_nr_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_nr_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                    txt_cli_id.focus();
                }
            }
        }
        bandera = false;
    }

    public Object generaNotaRecojoCabecera() {
        long nr_key = txt_nr_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_nr_id.getValue());
        String nr_id = txt_nr_id.getValue();
        String cli_id = txt_cli_id.getValue();
        long cli_key = Long.parseLong(txt_cli_id.getValue());
        long clidir_id = txt_clidir_id.getValue();
        String nr_zona = txt_zon_id.getValue();
        Date nr_fecemi = d_nr_fecemi.getValue();
        Date nr_fecent = d_nr_fecent.getValue();
        int nr_motcam = Integer.parseInt(txt_nr_motcamid.getValue());
        int nr_sup = Integer.parseInt(txt_sup_id.getValue());
        int nr_vend = Integer.parseInt(txt_nr_venid.getValue());
        int nr_trans = Integer.parseInt(txt_nr_transid.getValue());
        int nr_hor = Integer.parseInt(txt_nr_horid.getValue());
        String nr_usuadd = objUsuarioCredential.getCuenta();
        String nr_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nr_usumod = objUsuarioCredential.getCuenta();
        String nr_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotaRecojoCab(nr_key, nr_id, emp_id, suc_id, cli_id, cli_key, clidir_id, nr_zona, nr_fecemi, nr_fecent, nr_motcam, nr_sup, nr_vend, nr_trans, nr_hor, nr_usuadd, nr_pcadd, nr_usumod, nr_pcmod);
    }

    public Object generaNotaRecojoDetalle() throws SQLException {
        objProducto = new DaoProductos().buscarProducto(txt_nrd_prodid.getValue(), "%%");
        long nr_key = txt_nr_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_nr_id.getValue());
        int nrd_tipdoc = cb_nrd_tipdoc.getSelectedItem().getValue();
        String nrd_tipdocdes = cb_nrd_tipdoc.getValue();
        String nrd_serie = txt_nrd_serie.getValue().isEmpty() ? "" : txt_nrd_serie.getValue().toUpperCase();
        String nrd_doc = txt_nrd_doc.getValue().isEmpty() ? "" : txt_nrd_doc.getValue();
        String nrd_docref = nrd_serie + nrd_doc;
        String s_calculo = String.valueOf(txt_nrd_entero.getValue());
        String[] cadena = s_calculo.split("[.]");
        int entero = Integer.parseInt(cadena[0]);
        int nrd_cantent = txt_nrd_entero.getValue() == null ? 0 : entero;
        int nrd_cantfrac = txt_nrd_fraccion.getValue() == null ? 0 : txt_nrd_fraccion.getValue();
        int nrd_canttot;
        if (objProducto.getPro_unimanven().equals("BOL") || objProducto.getPro_unimanven().equals("CJA") || objProducto.getPro_unimanven().equals("PAQ")) {
            nrd_canttot = nrd_cantent * objProducto.getPro_presminven() + nrd_cantfrac;
        } else {
            nrd_canttot = nrd_cantent + nrd_cantfrac;
        }
        String pro_id = txt_nrd_prodid.getValue();
        String pro_des = objProducto.getPro_des();
        String pro_desdes = objProducto.getPro_desdes();
        String pro_unimancom = objProducto.getPro_unimanven();
        int pro_presmincom = objProducto.getPro_presminven();
        String nrd_glosa = txt_nrd_glosanotrec.getValue().toUpperCase();
        String nr_usuadd = objUsuarioCredential.getCuenta();
        String nr_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nr_usumod = objUsuarioCredential.getCuenta();
        String nr_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotaRecojoDet(nr_key, emp_id, suc_id, nr_key, nrd_tipdoc, nrd_tipdocdes, nrd_serie, nrd_doc, nrd_docref, nrd_cantent, nrd_cantfrac, nrd_canttot, pro_id, pro_desdes, pro_des, pro_unimancom, pro_presmincom, nrd_glosa, nr_usuadd, nr_pcadd, nr_usumod, nr_pcmod);
    }

    public void limpiarCampos() {
        txt_nr_id.setValue("");
        d_nr_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_nr_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_nr_motcamid.setValue("");
        txt_nr_motcamdes.setValue("");
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_nr_notaent.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nr_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nr_vennom.setValue("");
        txt_nr_transid.setValue("");
        txt_nr_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_nr_horid.setValue("");
        txt_nr_hordes.setValue("");
        txt_nr_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
        txt_nrd_doc.setValue("");
        txt_nrd_serie.setValue("");
        cb_nrd_tipdoc.setSelectedIndex(-1);
    }

    public void limpiarCliDirVacio() {
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_nr_venid.setValue("");
        txt_sup_id.setValue("");
        txt_nr_vennom.setValue("");
        txt_nr_transid.setValue("");
        txt_nr_transdes.setValue("");
        txt_nr_horid.setValue("");
        txt_nr_hordes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCamposDetalleProducto() {
        txt_nrd_proddes.setValue("");
        txt_nrd_upre.setValue(null);
        txt_nrd_entero.setValue(null);
        txt_nrd_fraccion.setValue(null);
    }

    public void limpiarCamposDetalle() {
        txt_nrd_doc.setValue("");
        txt_nrd_proddes.setValue("");
        txt_nrd_prodid.setValue("");
        txt_nrd_serie.setValue("");
        cb_nrd_tipdoc.setSelectedIndex(-1);
        txt_nrd_entero.setValue(null);
        txt_nrd_fraccion.setValue(null);
        txt_nrd_glosanotrec.setValue("");
    }

    public void llenarCampos() {
        txt_nr_id.setValue(objNotaRecojoCab.getNr_id());
        d_nr_fecemi.setValue(objNotaRecojoCab.getNr_fecemi());
        d_nr_fecent.setValue(objNotaRecojoCab.getNr_fecent());
        txt_nr_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_motcam()), 3, "0"));
        txt_nr_motcamdes.setValue(objNotaRecojoCab.getMcam_des());
        txt_cli_id.setValue(objNotaRecojoCab.getCli_id());
        txt_cli_razsoc.setValue(objNotaRecojoCab.getCli_razsoc());
        txt_nr_notaent.setValue(objNotaRecojoCab.getNr_notaent());
        txt_zon_id.setValue(objNotaRecojoCab.getNr_zona());
        txt_zon_des.setValue(objNotaRecojoCab.getZon_des());
        txt_nr_venid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_vend()), 4, "0"));
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_sup()), 4, "0"));
        txt_nr_vennom.setValue(objNotaRecojoCab.getVen_nom());
        txt_nr_transid.setValue(objNotaRecojoCab.getTrans_id());
        txt_nr_transdes.setValue(objNotaRecojoCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaRecojoCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaRecojoCab.getClidir_direcc());
        txt_nr_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaRecojoCab.getNr_hor()), 3, "0"));
        txt_nr_hordes.setValue(objNotaRecojoCab.getHor_des());
        txt_nr_registro.setValue(objNotaRecojoCab.getNr_nroreg());
        txt_usuadd.setValue(objNotaRecojoCab.getNr_usuadd());
        d_fecadd.setValue(objNotaRecojoCab.getNr_fecadd());
        txt_usumod.setValue(objNotaRecojoCab.getNr_usumod());
        d_fecmod.setValue(objNotaRecojoCab.getNr_fecmod());
    }

    public void llenarCamposProducto() {
        txt_nrd_prodid.setValue(objNotaRecojoDet.getPro_id());
        txt_nrd_proddes.setValue(objNotaRecojoDet.getPro_des());
        txt_nrd_doc.setValue(objNotaRecojoDet.getNrd_doc() == null ? "" : objNotaRecojoDet.getNrd_doc());
        txt_nrd_serie.setValue(objNotaRecojoDet.getNrd_serie() == null ? "" : objNotaRecojoDet.getNrd_serie());
        txt_nrd_entero.setValue(objNotaRecojoDet.getNrd_cantent());
        txt_nrd_fraccion.setValue(objNotaRecojoDet.getNrd_cantfrac());
        txt_nrd_glosanotrec.setValue(objNotaRecojoDet.getNrd_glosa());

        String tipo_doc = String.valueOf(objNotaRecojoDet.getNrd_tipdoc());
        if (!tipo_doc.isEmpty() || tipo_doc != null) {
            cb_nrd_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nrd_tipdoc, tipo_doc));
        } else {
            cb_nrd_tipdoc.setSelectedIndex(-1);
        }
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaRecojoDet = null;
        long nr_key = objNotaRecojoCab.getNr_key();
        objlstNotaRecojoDet = objDaoNotaRecojo.listaNotaRecojoDet(emp_id, suc_id, nr_key);
        lst_notrecdet.setModel(objlstNotaRecojoDet);
    }

    public void habilitaCampos(boolean b_valida) {
        d_nr_fecemi.setDisabled(b_valida);
        d_nr_fecent.setDisabled(b_valida);
        txt_nr_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_zon_id.setDisabled(b_valida);
        txt_nr_venid.setDisabled(b_valida);
        txt_nr_transid.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
        txt_nr_horid.setDisabled(b_valida);
    }

    public void habilitaCamposNR(boolean b_valida) {
        d_nr_fecemi.setDisabled(b_valida);
        d_nr_fecent.setDisabled(b_valida);
        txt_nr_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
    }

    public void habilitaCamposDetalle(boolean b_valida) {
        txt_nrd_doc.setDisabled(b_valida);
        txt_nrd_serie.setDisabled(b_valida);
        txt_nrd_prodid.setDisabled(b_valida);
        txt_nrd_glosanotrec.setDisabled(b_valida);
        txt_nrd_entero.setDisabled(b_valida);
        txt_nrd_fraccion.setDisabled(b_valida);
        txt_nrd_glosanotrec.setDisabled(b_valida);
        cb_nrd_tipdoc.setDisabled(b_valida);

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
        tbbtn_btn_nuevonotrec.setDisabled(b_valida1);
        tbbtn_btn_editarnotrec.setDisabled(b_valida1);
        tbbtn_btn_eliminarnotrec.setDisabled(b_valida1);
        tbbtn_btn_deshacernotrec.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotrec.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotrec.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiarTabLista(int i) {
        // Fechas Inicial y Final vacias
        if (d_fecini.getValue() == null || d_fecfin.getValue() == null) {
            d_fecini.setValue(new Date());
            d_fecfin.setValue(new Date());
        }
        if (i == 1) {
            objlstNotaRecojoCab = null;
            objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
            lst_notreccab.setModel(objlstNotaRecojoCab);
        } else if (i == 2) {
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
        } else {
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
            objlstNotaRecojoCab = null;
            objlstNotaRecojoCab = new ListModelList<NotaRecojoCab>();
            lst_notreccab.setModel(objlstNotaRecojoCab);
        }
    }

    public String verificarFechas() {
        String s_valor = "";
        // se tiene que validar que la fecha de emision sea del mismo periodo
        String fecha_emision = sdf.format(d_nr_fecemi.getValue());
        if (d_nr_fecent.getValue().getTime() < d_nr_fecemi.getValue().getTime()) {
            s_valor = "La fecha de Entrega debe ser Mayor o igual que : "
                    + fecha_emision;
        }
        return s_valor;
    }

}
