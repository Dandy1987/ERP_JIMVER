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
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoMotivoCambio;
import org.me.gj.controller.logistica.mantenimiento.DaoPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.utilitarios.DaoCierreDia;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.logistica.mantenimiento.MotCam;
import org.me.gj.model.logistica.mantenimiento.Precio;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
import org.me.gj.model.logistica.procesos.NotaInterCab;
import org.me.gj.model.logistica.procesos.NotaInterDet;
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

public class ControllerGenNotaIntercambio extends SelectorComposer<Component> {

    // Componentes Web
    @Wire
    Tab tab_listanotinter, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_guardar,
            tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,// BOTONES DE LA NOTA DE INTERCAMBIO CABECERA
            tbbtn_btn_nuevonotinter, tbbtn_btn_editarnotinter,
            tbbtn_btn_eliminarnotinter, tbbtn_btn_guardarnotinter,
            tbbtn_btn_deshacernotinter;// BOTONES DE LA NOTA DE INTERCAMBIO
    // DETALLE
    @Wire
    Doublebox txt_nid_preidsal, txt_nid_preident, txt_nid_pretotalsal,
            txt_nid_pretotalent,
            txt_nid_enteroent, txt_nid_enterosal;
    @Wire
    Textbox txt_venid,
            txt_vennom,// DATOS PARA LA BUSQUEDA
            txt_ni_id, txt_ni_motcamid, txt_ni_motcamdes, txt_cli_id,
            txt_cli_razsoc, txt_clidir_direcc, txt_zon_id, txt_zon_des,
            txt_ni_transid, txt_ni_transdes, txt_ni_venid, txt_sup_id,
            txt_ni_vennom, txt_ni_horid, txt_ni_hordes, txt_ni_registro,
            txt_ni_depurado,
            txt_ni_notaent,
            txt_ni_notasal,// DATOS DE LA NOTA DE INTERCAMBIO CABECERA
            txt_nid_proident, txt_nid_prodesent, txt_nid_proidsal,
            txt_nid_prodessal, txt_nic_provid, txt_nic_provdes, txt_nic_lpcid,
            txt_nic_lpcdes, txt_nid_glosa, txt_nid_serie, txt_nid_doc, // DATOS
            // DE LA
            // NOTA
            // DE
            // INTERCAMBIO
            // DETALLE
            txt_usuadd, txt_usumod;// ------>DATOS DE LA Nota INTERCAMBIO
    // CABECERA
    @Wire
    Longbox txt_clidir_id;
    @Wire
    Combobox cb_situacion;// DATOS DE LA NOTA DE INTERCAMBIO CABECERA
    @Wire
    Combobox cb_nid_tipdoc;// TIPO DOCUMENTO NOTA DE INTERCAMBIO DETALLE
    @Wire
    Intbox //txt_nid_enteroent, txt_nid_enterosal,
            txt_nid_fracent,
            txt_nid_fracsal, txt_nid_upresal, txt_nid_upreent;// DATOS DE LA
    // NOTA DE
    // INTERCAMBIO
    // DETALLE
    @Wire
    Checkbox chk_ordvend;// DATOS PARA LA BUSQUEDA
    @Wire
    Datebox d_fecini, d_fecfin,// DATOS PARA LA BUSQUEDA
            d_ni_fecemi, d_ni_fecent,// DATOS DE LA NOTA DE INTERCAMBIO CABECERA
            d_fecadd, d_fecmod;// ------>DATOS DE LA NOTA INTERCAMBIO CABECERA
    @Wire
    Button btn_buscarinter;
    @Wire
    Listbox lst_notintercab, lst_notinterdet;
    // Instancias de Objetos
    ListModelList<NotaInterCab> objlstNotaInterCab;
    ListModelList<NotaInterDet> objlstNotaInterDet, objlstEliminacion;
    ListModelList<TipDoc> objlstTipDoc;
    Accesos objAccesos;
    DaoAccesos objDaoAccesos;
    DaoCierreDia objDaoCierreDia;
    Productos objProductoEnt, objProductoSal;
    Vendedor objVendedor;
    NotaInterCab objNotaInterCab;
    NotaInterDet objNotaInterDet;
    DaoNotaIntercambio objDaoNotaInter;
    Precio objPrecioCompra;
    DaoNotaCambio objDaoNotaCambio;
    Utilitarios objUtilitario;
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    Cliente objCliente = new Cliente();
    Precio objPrecios = new Precio();
    // Variables publicas
    int emp_id, suc_id;
    int i_selCab, i_selDet;
    int i_camprode = 0, i_camprods = 0, i_camprov = 0;
    String campo = "";
    String fechaActual, s_estado, s_mensaje, s_estadoDetalle;
    String s_prode = "", s_prods = "", s_prov = "";
    String ok = "", sok = "libre";
    public static boolean bandera = false;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    // Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuarioCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenNotaIntercambio.class);

    // Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_notinter")
    public void llenaRegistros() throws SQLException {
        fechaActual = Utilitarios.hoyAsString();
        objDaoCierreDia = new DaoCierreDia();
        objUtilitario = new Utilitarios();
        emp_id = objUsuarioCredential.getCodemp();
        suc_id = objUsuarioCredential.getCodsuc();
        String vend_id = "%%";
        String situacion = "%%";
        objlstNotaInterCab = null;
        objDaoNotaInter = new DaoNotaIntercambio();
        objlstNotaInterCab = objDaoNotaInter.listaNotaInterCab(emp_id, suc_id, vend_id, situacion, fechaActual, fechaActual, false);
        lst_notintercab.setModel(objlstNotaInterCab);
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(2);
        cb_nid_tipdoc.setModel(objlstTipDoc);
        Date fecha = new Date();
        Utilitarios.sumaFecha(d_ni_fecent, fecha, 1);// Fec Entrega

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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10203030,
                usuario, empresa, sucursal);
        if (objUsuarioCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha ingresado al Procesos de Generar de Nota de Intercambio con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha Ingresado al Procesos de Generar de Nota de Intercambio con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a creacion de una nueva Generacion Nota de Intercambio en Procesos");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a creacion de una nueva Generacion Nota de Intercambio en Procesos");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a edicion de un Generar Nota de Intercambio en Procesos");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a edicion de un Generar Nota de Intercambio en Procesos");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a eliminacion de un Generar Nota de Intercambio en Procesos");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a eliminacion de un Generar Nota de Intercambio en Procesos");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | tiene acceso a impresion de la lista de Notas de Intercambio");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Nota de Intercambio");
        }
    }

    @Listen("onClick=#btn_buscarinter")
    public void busquedaRegistros() throws SQLException {
        objlstNotaInterCab = null;
        objlstNotaInterCab = new ListModelList<NotaInterCab>();
        lst_notintercab.setModel(objlstNotaInterCab);
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
            objlstNotaInterCab = objDaoNotaInter.listaNotaInterCab(emp_id, suc_id, vend_id, oc_sit, f_emisioni, f_emisionf, OrdVend);
            // Validar tabla sin registro
            if (objlstNotaInterCab.getSize() > 0) {
                lst_notintercab.setModel(objlstNotaInterCab);
            } else {
                objlstNotaInterCab = null;
                objlstNotaInterCab = new ListModelList<NotaInterCab>();
                lst_notintercab.setModel(objlstNotaInterCab);
                Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
            // validacion de eliminacion
            limpiarCampos();
            // Campos del detalle de la Orden de Compra
            limpiarCamposDetalle();
            limpiaAuditoria();
            objlstNotaInterDet = null;
            objlstNotaInterDet = new ListModelList<NotaInterDet>();
            lst_notinterdet.setModel(objlstNotaInterDet);
        }
    }

    @Listen("onSelect=#lst_notintercab")
    public void seleccionaRegistro() throws SQLException {
        objNotaInterCab = (NotaInterCab) lst_notintercab.getSelectedItem().getValue();
        if (objNotaInterCab == null) {
            objNotaInterCab = objlstNotaInterCab.get(i_selCab + 1);
        }
        i_selCab = lst_notintercab.getSelectedIndex();
        limpiarCampos();
        limpiarCamposDetalle();
        llenarCampos();
        llenarCamposDetalle();
    }

    @Listen("onSelect=#lst_notinterdet")
    public void seleccionaRegistroDetalle() throws SQLException {
        objNotaInterDet = (NotaInterDet) lst_notinterdet.getSelectedItem().getValue();
        if (objNotaInterDet == null) {
            objNotaInterDet = objlstNotaInterDet.get(i_selDet + 1);
        }
        i_selDet = lst_notinterdet.getSelectedIndex();
        limpiarCamposDetalle();
        llenarCamposProducto(objNotaInterDet);
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objNotaInterCab = null;
        objNotaInterCab = new NotaInterCab();
        objNotaInterDet = null;
        objlstNotaInterDet = null;
        objlstNotaInterDet = new ListModelList<NotaInterDet>();
        lst_notinterdet.setModel(objlstNotaInterDet);
        limpiarCampos();
        limpiarCamposDetalle();
        habilitaCampos(false);
        habilitaBotones(true, false);
        habilitabotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        Date fecha = new Date();
        Utilitarios.sumaFecha(d_ni_fecent, fecha, 1);// Fec Entrega
        d_ni_fecemi.focus();
        s_estado = "N";
        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_notintercab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objNotaInterCab.getNi_sit() != 1) {
                Messagebox.show("Esta Nota de Intercambio ya no se Puede Modificar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitabotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                txt_nic_provid.setReadonly(true);
                txt_nic_lpcid.setReadonly(true);
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<NotaInterDet>();
                LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (!objlstNotaInterDet.isEmpty()) {
            String s_valida = verificarCab();
            if (!s_valida.isEmpty()) {
                Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    if (campo.equals("F. Emision")) {
                                        d_ni_fecemi.focus();
                                    } else if (campo.equals("F. Entrega")) {
                                        d_ni_fecent.focus();
                                    } else if (campo.equals("M. Cambio")) {
                                        txt_ni_motcamid.focus();
                                    } else if (campo.equals("Proveedor")) {
                                        txt_nic_provid.focus();
                                    } else if (campo.equals("Lista Precio")) {
                                        txt_nic_lpcid.focus();
                                    } else if (campo.equals("Cliente")) {
                                        txt_cli_id.focus();
                                    } else if (campo.equals("Direccion")) {
                                        txt_clidir_id.focus();
                                    } else if (campo.equals("Zona")) {
                                        txt_zon_id.focus();
                                    } else if (campo.equals("Transporte")) {
                                        txt_ni_transid.focus();
                                    } else if (campo.equals("Vendedor")) {
                                        txt_venid.focus();
                                    } else if (campo.equals("Horario")) {
                                        txt_ni_horid.focus();
                                    }
                                }
                            }
                        });

            } else {
                s_valida = verificarFechas();
                if (!s_valida.equals("")) {
                    Messagebox.show(s_valida);
                } else {
                    String s_ed = s_estadoDetalle == null ? "" : s_estadoDetalle;
                    if (s_ed.equals("N")) {
                        Messagebox.show("Guarde detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else if (s_ed.equals("E")) {
                        Messagebox.show("El detalle esta en edicion, por favor guarde el detalle", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    } else {
                        if (!txt_cli_id.getValue().matches("[0-9]*") || !txt_nic_provid.getValue().matches("[0-9]*") || !txt_clidir_id.getValue().toString().matches("[0-9]*") || !txt_nic_lpcid.getValue().matches("[0-9]*") || !txt_ni_motcamid.getValue().matches("[0-9]*")) {
                            lst_notinterdet.focus();
                        } else {
                            Cliente objCliente = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                            CliDireccion objCliDireccion = new DaoDirecciones().getNomDireccion(txt_cli_id.getValue(), txt_clidir_id.getValue(), emp_id, suc_id);
                            MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(txt_ni_motcamid.getValue());
                            Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(txt_nic_provid.getValue()));
                            ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, Long.valueOf(txt_nic_provid.getValue()), Integer.parseInt(txt_nic_lpcid.getValue()));
                            if (objCliente == null || objProveedor == null || objLpCompra == null || objCliDireccion == null || objMotivoCambio == null) {
                                lst_notinterdet.focus();
                            } else {
                                String fecemi = sdf.format(d_ni_fecemi.getValue());
                                if (new DaoCierreDia().ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                                    Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    Messagebox
                                            .show("Esta Seguro que desea guardar los cambios?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                                                @Override
                                                public void onEvent(Event event) throws Exception {
                                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                        ParametrosSalida objParamCabecera;
                                                        objNotaInterCab = (NotaInterCab) generaNotaIntercambioCab();
                                                        if (s_estado.equals("N")) {
                                                            objParamCabecera = objDaoNotaInter.insertarNotaInterCab(objNotaInterCab);
                                                        } else {
                                                            objParamCabecera = objDaoNotaInter.modificarNotaInterCab(objNotaInterCab);
                                                        }
                                                        if (objParamCabecera.getFlagIndicador() == 0) {
                                                            NotaInterDet objNotInter;
                                                            ParametrosSalida objParamDetalle = new ParametrosSalida();
                                                            boolean verificarDetalle = true;
                                                            int i = 0;
                                                            if (s_estado.equals("N")) {
                                                                while (i < objlstNotaInterDet.getSize() && verificarDetalle) {
                                                                    long nr_key = Long.parseLong(objParamCabecera.getCodigoRegistro());
                                                                    objNotInter = (NotaInterDet) generaNotaIntercambioDet(nr_key, i);
                                                                    objParamDetalle = objDaoNotaInter.insertarNotaInterDet(objNotInter);
                                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                                        Messagebox.show("Ocurrio un Error con el Producto " + objlstNotaInterDet.get(i).getNid_proidsal() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                        verificarDetalle = false;
                                                                    }
                                                                    i = i + 2;
                                                                }
                                                            } else {
                                                                // OPERACION DE ELIMINACION DE PRODUCTOS DE LA NOTA DE RECOJO

                                                                if (!objlstEliminacion.isEmpty()) {
                                                                    while (i < objlstEliminacion.getSize()
                                                                    && verificarDetalle) {
                                                                        objParamDetalle = objDaoNotaInter.eliminarNotaInterDet(objlstEliminacion.get(i));
                                                                        if (objParamDetalle.getFlagIndicador() == 1) {
                                                                            Messagebox.show("Ocurrio un Error con el Item" + objlstEliminacion.get(i).getNid_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                            verificarDetalle = false;
                                                                        }
                                                                        i++;
                                                                    }
                                                                }
                                                                // OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA NOTA DE RECOJO

                                                                i = 0;
                                                                verificarDetalle = true;
                                                                while (i < objlstNotaInterDet.getSize() && verificarDetalle) {
                                                                    String indicador = objlstNotaInterDet.get(i).getInd_accion();
                                                                    long ni_key = objlstNotaInterDet.get(i).getNi_key();
                                                                    objNotInter = (NotaInterDet) generaNotaIntercambioDet(ni_key, i);
                                                                    if (indicador.equals("N") || indicador.equals("NM")) {
                                                                        objParamDetalle = objDaoNotaInter.insertarNotaInterDet(objNotInter);
                                                                    } else if (indicador.equals("M")) {
                                                                        objParamDetalle = objDaoNotaInter.modificarNotaInterDet(objNotInter);
                                                                    }
                                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                                        Messagebox.show("Ocurrio un Error con el Producto " + objlstNotaInterDet.get(i).getNid_proidsal() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                                        verificarDetalle = false;
                                                                    }
                                                                    i = i + 2;
                                                                }
                                                            }
                                                            // VALIDACION DE GUARDAR / NUEVO

                                                            VerificarTransacciones();
                                                            tbbtn_btn_guardar.setDisabled(true);
                                                            tbbtn_btn_deshacer.setDisabled(true);
                                                            //
                                                            habilitaTab(false, false);
                                                            seleccionaTab(true, false);
                                                            habilitaCampos(true);
                                                            habilitaCamposDetalle(true, true, true);
                                                            habilitabotonesDetalle(true, true);
                                                            limpiarCamposDetalle();
                                                            limpiarTabLista(2);
                                                            busquedaRegistros();
                                                            txt_nic_provid.setReadonly(false);
                                                            txt_nic_lpcid.setReadonly(false);
                                                            txt_nic_provid.setReadonly(false);

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
            Messagebox.show("No Puede Guardar una Nota de Intercambio con Detalle Vacio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_notintercab.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar esta Nota de Intercambio?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        ParametrosSalida objParamSalida;
                        objParamSalida = objDaoNotaInter.eliminarNotaInterCab(objNotaInterCab);
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
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
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

                    // Campos del detalle de la Nota de Intercambio
                    habilitabotonesDetalle(true, true);
                    habilitaCamposDetalle(true, true, true);
                    limpiarCamposDetalle();
                    limpiarTabLista(2);
                    txt_nic_provid.setReadonly(false);
                    txt_nic_lpcid.setReadonly(false);
                    busquedaRegistros();
                }
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_nuevonotinter")
    public void nuevoDetalleIntercambio() {
        String verificar = verificarCab();
        if (!verificar.equals("")) {
            Messagebox.show(verificar, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                if (campo.equals("F. Emision")) {
                                    d_ni_fecemi.focus();
                                } else if (campo.equals("F. Entrega")) {
                                    d_ni_fecent.focus();
                                } else if (campo.equals("M. Cambio")) {
                                    txt_ni_motcamid.focus();
                                } else if (campo.equals("Proveedor")) {
                                    txt_nic_provid.focus();
                                } else if (campo.equals("Lista Precio")) {
                                    txt_nic_lpcid.focus();
                                } else if (campo.equals("Cliente")) {
                                    txt_cli_id.focus();
                                } else if (campo.equals("Direccion")) {
                                    txt_clidir_id.focus();
                                } else if (campo.equals("Zona")) {
                                    txt_zon_id.focus();
                                } else if (campo.equals("Transporte")) {
                                    txt_ni_transid.focus();
                                } else if (campo.equals("Vendedor")) {
                                    txt_venid.focus();
                                } else if (campo.equals("Horario")) {
                                    txt_ni_horid.focus();
                                }
                            }
                        }
                    });

        } else {
            habilitaCamposDetalle(false, false, false);
            limpiarCamposDetalle();
            habilitaCamposNI(true);
            tbbtn_btn_guardarnotinter.setDisabled(false);
            habilitabotonesDetalle(true, false);
            s_estadoDetalle = "N";
            Utilitarios.deshabilitarLista(true, lst_notinterdet);
            txt_nid_proidsal.focus();
        }
    }

    @Listen("onClick=#tbbtn_btn_editarnotinter")
    public void modificaDetalleIntercambio() {
        if (objlstNotaInterDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Intercambio a Editar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notinterdet.getSelectedIndex() == -1) {
                Messagebox.show("Por favor Seleccione un Registro del Detalle de Nota de Intercambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                if (lst_notinterdet.getSelectedIndex() % 2 == 0) {
                    habilitaCamposDetalle(false, true, false);
                    txt_nid_proident.setDisabled(true);
                    txt_nid_enterosal.setDisabled(false);
                    txt_nid_fracsal.setDisabled(false);
                    txt_nid_enteroent.focus();
                } else {
                    habilitaCamposDetalle(true, false, false);
                    txt_nid_proidsal.setDisabled(true);
                    txt_nid_enteroent.setDisabled(false);
                    txt_nid_fracent.setDisabled(false);
                    txt_nid_enterosal.focus();
                }
                habilitabotonesDetalle(true, false);
                habilitaCamposNI(true);
                txt_nid_serie.setDisabled(true);
                txt_nid_doc.setDisabled(true);
                tbbtn_btn_guardarnotinter.setDisabled(false);
                Utilitarios.deshabilitarLista(true, lst_notinterdet);
                s_estadoDetalle = "E";
                // txt_nid_proidsal.focus();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardarnotinter")
    public void agregarDetalle() throws SQLException {
        String validaProducto = verificarDetalle();
        if (!validaProducto.isEmpty()) {
            Messagebox.show(validaProducto, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Prod.Ent")) {
                            txt_nid_proident.focus();
                        }
                        if (campo.equals("Prod.Sal")) {
                            txt_nid_proidsal.focus();
                        }
                        if (campo.equals("EC.Ent")) {
                            txt_nid_enteroent.focus();
                        }
                        if (campo.equals("EC.Frac")) {
                            txt_nid_fracent.focus();
                        }
                        if (campo.equals("SC.Ent")) {
                            txt_nid_enterosal.focus();
                        }
                        if (campo.equals("SC.Frac")) {
                            txt_nid_fracsal.focus();
                        }

                        if (campo.equals("Tip. Doc")) {
                            cb_nid_tipdoc.focus();
                        } else if (campo.equals("Serie")) {
                            txt_nid_serie.focus();
                        } else if (campo.equals("Doc")) {
                            txt_nid_doc.focus();
                        }
                    }
                }
            });
        } else {
            if (!txt_nid_proident.getValue().matches("[0-9]*") || !txt_nid_proidsal.getValue().matches("[0-9]*")) {
                lst_notinterdet.focus();
            } else if ((txt_nid_proident.getValue().equals(txt_nid_proidsal.getValue()))) {
                Messagebox.show("El producto saliente no puede ser igual que el producto Entrante", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (!txt_nid_pretotalsal.getValue().equals(txt_nid_pretotalent.getValue())) {
                Messagebox.show("Los precios no coinciden, cambie de producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                String pro_id = txt_nid_proidsal.getValue();
                if (s_estado.equals("N")) {
                    if (s_estadoDetalle.equals("N")) {
                        String valida = validaProducto(pro_id);
                        if (valida.equals("")) {
                            objlstNotaInterDet.add((NotaInterDet) generaNotaInterDetEnt());
                            objlstNotaInterDet.add((NotaInterDet) generaNotaInterDetSal());
                            lst_notinterdet.setModel(objlstNotaInterDet);
                        } else {
                            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    } else {
                        int lst_ord = lst_notinterdet.getSelectedIndex();
                        if (lst_ord % 2 == 0) {
                            int nid_tipdoc = cb_nid_tipdoc.getSelectedItem().getValue();
                            String nid_tipdocdes = cb_nid_tipdoc.getValue();
                            String serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue();
                            objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                            objlstNotaInterDet.set(lst_ord, objNotaInterDet);

                            objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                            objlstNotaInterDet.get(lst_ord + 1).setNid_tipdoc(nid_tipdoc);
                            objlstNotaInterDet.get(lst_ord + 1).setNid_tipdocdes(nid_tipdocdes);
                            objlstNotaInterDet.get(lst_ord + 1).setNid_docref(serie + txt_nid_doc.getValue());
                            objlstNotaInterDet.get(lst_ord + 1).setNid_glosa(txt_nid_glosa.getValue());
                            objlstNotaInterDet.set((lst_ord + 1), objNotaInterDet);
                            // objlstNotaInterDet.set((lst_ord + 1),
                            // objlstNotaInterDet.get(lst_ord + 1));
                        } else {
                            int nid_tipdoc = cb_nid_tipdoc.getSelectedItem().getValue();
                            String nid_tipdocdes = cb_nid_tipdoc.getValue();
                            String serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue();

                            objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                            objlstNotaInterDet.set(lst_ord, objNotaInterDet);

                            objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                            objlstNotaInterDet.get(lst_ord - 1).setNid_tipdoc(nid_tipdoc);
                            objlstNotaInterDet.get(lst_ord - 1).setNid_tipdocdes(nid_tipdocdes);
                            objlstNotaInterDet.get(lst_ord - 1).setNid_docref(serie + txt_nid_doc.getValue());
                            objlstNotaInterDet.get(lst_ord - 1).setNid_glosa(txt_nid_glosa.getValue());
                            objlstNotaInterDet.set((lst_ord - 1), objNotaInterDet);
                            // objlstNotaInterDet.set((lst_ord - 1),
                            // objlstNotaInterDet.get(lst_ord - 1));
                        }
                    }
                } else {
                    if (s_estadoDetalle.equals("N")) {
                        String valida = validaProducto(pro_id);
                        if (valida.equals("")) {
                            if (txt_nid_pretotalent.getValue().equals(txt_nid_pretotalsal.getValue())) {
                                objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                                objNotaInterDet.setInd_accion("N");
                                objlstNotaInterDet.add(objNotaInterDet);
                                objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                                objNotaInterDet.setInd_accion("N");
                                objlstNotaInterDet.add(objNotaInterDet);
                            } else {
                                Messagebox.show("Los precios no coinciden, cambie de producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    } else {
                        String indicador = objNotaInterDet.getInd_accion();
                        int pos = lst_notinterdet.getSelectedIndex();
                        String serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue();

                        if (pos % 2 == 0) {
                            if (indicador.equals("N") || indicador.equals("NM")) {
                                objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                                objNotaInterDet.setInd_accion("NM");
                                objlstNotaInterDet.set(pos, objNotaInterDet);
                                objlstNotaInterDet.get(pos + 1).setNid_tipdoc(Integer.parseInt(cb_nid_tipdoc.getSelectedItem().getValue().toString()));
                                objlstNotaInterDet.get(pos + 1).setNid_docref(serie + txt_nid_doc.getValue());
                                objlstNotaInterDet.get(pos + 1).setNid_glosa(txt_nid_glosa.getValue());
                                // objlstNotaInterDet.get(pos +
                                // 1).setInd_accion("NM");

                                objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                                objlstNotaInterDet.set((pos + 1), objNotaInterDet);
                                // objlstNotaInterDet.set((pos + 1),
                                // objlstNotaInterDet.get(pos + 1));
                            } else {
                                long item = objNotaInterDet.getNid_item();
                                objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                                objNotaInterDet.setInd_accion("M");
                                objNotaInterDet.setNid_item(item);
                                objlstNotaInterDet.set(pos, objNotaInterDet);

                                objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                                objlstNotaInterDet.get(pos + 1).setNid_tipdoc(Integer.parseInt(cb_nid_tipdoc.getSelectedItem().getValue().toString()));
                                objlstNotaInterDet.get(pos + 1).setNid_docref(serie + txt_nid_doc.getValue());
                                objlstNotaInterDet.get(pos + 1).setNid_glosa(txt_nid_glosa.getValue());
                                // objlstNotaInterDet.get(pos +
                                // 1).setInd_accion("M");
                                objlstNotaInterDet.get(pos + 1).setNid_item(item);
                                objlstNotaInterDet.set((pos + 1), objNotaInterDet);
                            }
                        } else {

                            if (indicador.equals("N") || indicador.equals("NM")) {
                                int nid_tipdoc = Integer.parseInt(cb_nid_tipdoc.getSelectedItem().getValue().toString());
                                String nid_tipdocdes = cb_nid_tipdoc.getValue();
                                objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                                objNotaInterDet.setInd_accion("NM");
                                objlstNotaInterDet.set(pos, objNotaInterDet);

                                objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                                objlstNotaInterDet.get(pos - 1).setNid_tipdoc(nid_tipdoc);
                                objlstNotaInterDet.get(pos - 1).setNid_tipdocdes(nid_tipdocdes);
                                objlstNotaInterDet.get(pos - 1).setNid_docref(serie + txt_nid_doc.getValue());
                                objlstNotaInterDet.get(pos - 1).setNid_glosa(txt_nid_glosa.getValue());
                                // objlstNotaInterDet.get(pos -
                                // 1).setInd_accion("NM");
                                objlstNotaInterDet.set((pos - 1), objNotaInterDet);
                                // objlstNotaInterDet.set((pos - 1),
                                // objlstNotaInterDet.get(pos - 1));
                            } else {
                                long item = objNotaInterDet.getNid_item();
                                int nid_tipdoc = Integer.parseInt(cb_nid_tipdoc.getSelectedItem().getValue().toString());
                                String nid_tipdocdes = cb_nid_tipdoc.getValue();

                                objNotaInterDet = (NotaInterDet) generaNotaInterDetSal();
                                objNotaInterDet.setInd_accion("M");
                                objNotaInterDet.setNid_item(item);
                                objlstNotaInterDet.set(pos, objNotaInterDet);

                                objNotaInterDet = (NotaInterDet) generaNotaInterDetEnt();
                                objlstNotaInterDet.get(pos - 1).setNid_tipdoc(nid_tipdoc);
                                objlstNotaInterDet.get(pos - 1).setNid_tipdocdes(nid_tipdocdes);
                                objlstNotaInterDet.get(pos - 1).setNid_docref(serie + txt_nid_doc.getValue());
                                objlstNotaInterDet.get(pos - 1).setNid_glosa(txt_nid_glosa.getValue());
                                // objlstNotaInterDet.get(pos -
                                // 1).setInd_accion("M");
                                objlstNotaInterDet.get(pos - 1).setNid_item(item);
                                objlstNotaInterDet.set((pos - 1), objNotaInterDet);
                                // objlstNotaInterDet.set((pos - 1),
                                // objlstNotaInterDet.get(pos - 1));
                            }
                        }
                    }
                }
                s_estadoDetalle = "";
                limpiarCamposDetalle();
                habilitaCamposDetalle(true, true, true);
                habilitabotonesDetalle(false, true);
                habilitaCamposNI(false);
                txt_nic_provid.setReadonly(true);
                txt_nic_lpcid.setReadonly(true);
                tbbtn_btn_guardarnotinter.setDisabled(true);
                Utilitarios.deshabilitarLista(false, lst_notinterdet);
                lst_notinterdet.clearSelection();
                lst_notinterdet.setFocus(true);
                lst_notinterdet.focus();
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarnotinter")
    public void eliminaDetalleIntercambio() {
        if (objlstNotaInterDet.isEmpty()) {
            Messagebox.show("No existen Elementos en la Nota de Intercambio a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notinterdet.getSelectedIndex() == -1) {
                Messagebox.show("Por Favor Seleccione un Registro a Eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                objNotaInterDet = lst_notinterdet.getSelectedItem().getValue();
                int posicion = lst_notinterdet.getSelectedIndex();
                if (posicion % 2 == 0) {
                    if (s_estado.equals("E") && (!objNotaInterDet.getInd_accion().equals("N") || !objNotaInterDet.getInd_accion().equals("NM"))) {
                        long nrd_item = objNotaInterDet.getNid_item();
                        long nr_key = Long.parseLong(txt_ni_id.getValue());
                        String nrd_usumod = objUsuarioCredential.getCuenta();
                        String nrd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
                        objlstEliminacion.add(new NotaInterDet(nr_key, emp_id, suc_id, nrd_item, nrd_usumod, nrd_pcmod));
                    }
                    objlstNotaInterDet.remove(posicion);
                    objlstNotaInterDet.remove(posicion);
                    limpiarCamposDetalle();
                    tbbtn_btn_guardarnotinter.setDisabled(true);
                } else {
                    if (s_estado.equals("E") && (!objNotaInterDet.getInd_accion().equals("N") || !objNotaInterDet.getInd_accion().equals("NM"))) {
                        long nrd_item = objNotaInterDet.getNid_item();
                        long nr_key = Long.parseLong(txt_ni_id.getValue());
                        String nrd_usumod = objUsuarioCredential.getCuenta();
                        String nrd_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
                        objlstEliminacion.add(new NotaInterDet(nr_key, emp_id, suc_id, nrd_item, nrd_usumod, nrd_pcmod));
                    }
                    objlstNotaInterDet.remove(posicion - 1);
                    objlstNotaInterDet.remove(posicion - 1);
                    limpiarCamposDetalle();
                    tbbtn_btn_guardarnotinter.setDisabled(true);
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacernotinter")
    public void deshacerDetalleIntercambio() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK
                | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            s_estadoDetalle = "";
                            limpiarCamposDetalle();
                            habilitaCamposDetalle(true, true, true);
                            habilitabotonesDetalle(false, true);
                            habilitaCamposNI(false);
                            tbbtn_btn_guardarnotinter.setDisabled(true);
                            Utilitarios.deshabilitarLista(false, lst_notinterdet);
                            lst_notinterdet.clearSelection();
                        }
                        lst_notinterdet.setFocus(true);
                        lst_notinterdet.focus();

                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaInterCab == null || objlstNotaInterCab.isEmpty()) {
            Messagebox.show("No hay Registros de Generar Notas de Intercambio para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notintercab.getSelectedIndex() >= 0) {
                objNotaInterCab = (NotaInterCab) lst_notintercab.getSelectedItem().getValue();
                if (objNotaInterCab == null) {
                    objNotaInterCab = objlstNotaInterCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuarioCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuarioCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuarioCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuarioCredential.getCodsuc());
                objMapObjetos.put("ni_key", objNotaInterCab.getNi_key());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenNotInter.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione Registro de Nota de Intercambio para imprimir");
            }
        }
    }

    // Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_gennotinter")
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
                if (!tbbtn_btn_nuevonotinter.isDisabled()) {
                    nuevoDetalleIntercambio();
                }
                break;
            case 77:
                if (!tbbtn_btn_editarnotinter.isDisabled()) {
                    modificaDetalleIntercambio();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminarnotinter.isDisabled()) {
                    eliminaDetalleIntercambio();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardarnotinter.isDisabled()) {
                    agregarDetalle();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshacernotinter.isDisabled()) {
                    deshacerDetalleIntercambio();
                }
                break;
        }
    }

    @Listen("onCheck=#chk_ordvend")
    public void seleccionarOrden() throws SQLException {
        if (!(objlstNotaInterCab == null || objlstNotaInterCab.isEmpty())) {
            busquedaRegistros();
        }
    }

    @Listen("onOK=#txt_nid_proidsal")
    public void lov_productossalida() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nid_proidsal.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambioS";
                objMapObjetos.put("proveedor", txt_nic_provid.getValue());
                objMapObjetos.put("listaprecio", txt_nic_lpcid.getValue());
                objMapObjetos.put("txt_nid_proidsal", txt_nid_proidsal);
                objMapObjetos.put("txt_nid_prodessal", txt_nid_prodessal);
                objMapObjetos.put("txt_nid_enterosal", txt_nid_enterosal);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaProductoS();
                // txt_nid_enterosal.focus();
            }
        }
    }

    @Listen("onOK=#txt_nid_proident")
    public void lov_productosentrada() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nid_proident.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambioE";
                objMapObjetos.put("proveedor", txt_nic_provid.getValue());
                objMapObjetos.put("listaprecio", txt_nic_lpcid.getValue());
                objMapObjetos.put("txt_nid_proident", txt_nid_proident);
                objMapObjetos.put("txt_nid_prodesent", txt_nid_prodesent);
                objMapObjetos.put("txt_nid_enteroent", txt_nid_enteroent);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, objMapObjetos);
                window.doModal();
            } else {
                validaProductoE();
                // txt_nid_enteroent.focus();
            }
        }
    }

    @Listen("onOK=#txt_cli_id")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_cli_id.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambio";
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_clinom", txt_cli_razsoc);
                objMapObjetos.put("txt_zon_id", txt_zon_id);
                objMapObjetos.put("txt_zon_des", txt_zon_des);
                objMapObjetos.put("txt_nr_venid", txt_ni_venid);
                objMapObjetos.put("txt_sup_id", txt_sup_id);
                objMapObjetos.put("txt_nr_vennom", txt_ni_vennom);
                objMapObjetos.put("txt_nr_transid", txt_ni_transid);
                objMapObjetos.put("txt_nr_transdes", txt_ni_transdes);
                objMapObjetos.put("txt_nr_horid", txt_ni_horid);
                objMapObjetos.put("txt_nr_hordes", txt_ni_hordes);
                objMapObjetos.put("txt_clidir_id", txt_clidir_id);
                objMapObjetos.put("txt_clidir_direcc", txt_clidir_direcc);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_clidir_id.focus();// validaClientes();
            }
        }
    }

    @Listen("onOK=#txt_clidir_id")
    public void lov_direcciones() {
        if (bandera == false) {
            bandera = true;
            if (!txt_cli_id.getValue().isEmpty()
                    && txt_clidir_id.getValue() == null) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambio";
                objMapObjetos.put("txt_dirid", txt_clidir_id);
                objMapObjetos.put("txt_cliid", txt_cli_id);
                objMapObjetos.put("txt_dirdes", txt_clidir_direcc);
                objMapObjetos.put("txt_zonid", txt_zon_id);
                objMapObjetos.put("txt_nid_proidsal", txt_nid_proidsal);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovDirecciones.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_zon_id.focus();
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
                String modoEjecucion = "NotaIntercambio";
                objMapObjetos.put("txt_venid", txt_venid);
                objMapObjetos.put("txt_vennom", txt_vennom);
                objMapObjetos.put("txt_sup_id", txt_supid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_situacion.focus();
            }
        }
    }

    @Listen("onOK=#txt_ni_motcamid")
    public void lov_motCambio() {
        if (bandera == false) {
            bandera = true;
            if (txt_ni_motcamid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "NotaIntercambio";
                objMapObjetos.put("txt_motid", txt_ni_motcamid);
                objMapObjetos.put("txt_motdes", txt_ni_motcamdes);
                objMapObjetos.put("txt_nic_provid", txt_nic_provid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaIntercambio");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoCambio.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_nic_provid.focus();
            }
        }
    }

    @Listen("onOK=#txt_nic_provid")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nic_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                String modoEjecucion = "GenNotInter";
                String tipo = "1";
                objMapObjetos.put("txt_nic_provid", txt_nic_provid);
                objMapObjetos.put("txt_nic_provdes", txt_nic_provdes);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("tipo", tipo);
                objMapObjetos.put("controlador", "ControllerNotInter");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_nic_lpcid.focus();
            }
        }
    }

    @Listen("onOK=#txt_nic_lpcid")
    public void lov_listaPrecio() {
        if (bandera == false) {
            bandera = true;
            if (txt_nic_provid.getValue().isEmpty() || txt_nic_provdes.getValue().isEmpty()) {
                ok = "s";
                txt_nic_provid.focus();
            } else {
                if (txt_nic_lpcid.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    String modoEjecucion = "GenNotInter";
                    objMapObjetos.put("txt_nic_lpcid", txt_nic_lpcid);
                    objMapObjetos.put("txt_nic_lpcdes", txt_nic_lpcdes);
                    objMapObjetos.put("txt_nic_provid", txt_nic_provid);
                    objMapObjetos.put("txt_cli_id", txt_cli_id);
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerNotInter");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovListaPrecioCompra.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_cli_id.focus();
                }
            }
        }
    }

    @Listen("onOK=#txt_nid_enteroent")
    public void validaCantEntE() {
        if (!(txt_nid_proident.getValue().isEmpty() || txt_nid_upreent.getValue() == null || txt_nid_preident.getValue() == null)) {
            if (txt_nid_enteroent.getValue() != null) {
                String s_calculo = String.valueOf(txt_nid_enteroent.getValue());
                String[] cadena = s_calculo.split("[.]");
                int ultimo = Integer.parseInt(cadena[1]);
                if (ultimo > 0) {
                    double cant = cadena[1].length() == 1
                            ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nid_upreent.getValue()
                            : (double) Integer.parseInt(cadena[1]) / 100 * txt_nid_upreent.getValue();
                    int fraccion = (int) cant;
                    txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]));
                    txt_nid_fracent.setValue(fraccion);
                    txt_nid_fracent.focus();
                } else {
                    if (txt_nid_fracent.getValue() != null) {
                        txt_nid_fracent.focus();
                    } else {
                        txt_nid_fracent.setValue(0);
                        txt_nid_fracent.focus();
                    }
                }
                txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
            } else {
                txt_nid_fracent.focus();
            }
        } else {
            if (txt_nid_enteroent.getValue() == null && txt_nid_proident.getValue().isEmpty()) {
                Messagebox.show("Ingrese producto de entrada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleE();
                            txt_nid_proident.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onBlur=#txt_nid_enteroent")
    public void validaOnBlurCantEntE() {
        if (!(txt_nid_proident.getValue().isEmpty() || txt_nid_upreent.getValue() == null || txt_nid_preident.getValue() == null)) {
            if (txt_nid_enteroent.getValue() != null) {
                String s_calculo = String.valueOf(txt_nid_enteroent.getValue());
                String[] cadena = s_calculo.split("[.]");
                int ultimo = Integer.parseInt(cadena[1]);
                if (ultimo > 0) {
                    double cant = cadena[1].length() == 1
                            ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nid_upreent.getValue()
                            : (double) Integer.parseInt(cadena[1]) / 100 * txt_nid_upreent.getValue();
                    int fraccion = (int) cant;
                    txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]));
                    txt_nid_fracent.setValue(fraccion);
                } else {
                    if (txt_nid_fracent.getValue() == null) {
                        txt_nid_fracent.setValue(0);
                    }
                }
                txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
            }
        } else {
            if (txt_nid_enterosal.getValue() == null && txt_nid_proidsal.getValue().isEmpty()) {
                lst_notinterdet.focus();
            }
        }
    }

    @Listen("onOK=#txt_nid_enterosal")
    public void validaOnOKCantEntS() {
        if (!(txt_nid_proidsal.getValue().isEmpty() || txt_nid_upresal.getValue() == null || txt_nid_preidsal.getValue() == null)) {
            if (txt_nid_enterosal.getValue() != null) {
                String s_calculo = String.valueOf(txt_nid_enterosal.getValue());
                String[] cadena = s_calculo.split("[.]");
                int ultimo = Integer.parseInt(cadena[1]);
                if (ultimo > 0) {
                    double cant = cadena[1].length() == 1
                            ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nid_upresal.getValue()
                            : (double) Integer.parseInt(cadena[1]) / 100 * txt_nid_upresal.getValue();
                    int fraccion = (int) cant;
                    txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]));
                    txt_nid_fracsal.setValue(fraccion);
                    txt_nid_fracsal.focus();
                } else {
                    if (txt_nid_fracsal.getValue() != null) {
                        txt_nid_fracsal.focus();
                    } else {
                        txt_nid_fracsal.setValue(0);
                        txt_nid_fracsal.focus();
                    }
                }
                txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
            } else {
                txt_nid_fracsal.focus();
            }
        } else {
            if (txt_nid_enterosal.getValue() == null && txt_nid_proidsal.getValue().isEmpty()) {
                Messagebox.show("Ingrese Producto de Salida", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleS();
                            txt_nid_proidsal.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onBlur=#txt_nid_enterosal")
    public void validaOnBlurCantEntS() {
        if (!(txt_nid_proidsal.getValue().isEmpty() || txt_nid_upresal.getValue() == null || txt_nid_preidsal.getValue() == null)) {
            if (txt_nid_enterosal.getValue() != null) {
                String s_calculo = String.valueOf(txt_nid_enterosal.getValue());
                String[] cadena = s_calculo.split("[.]");
                int ultimo = Integer.parseInt(cadena[1]);
                if (ultimo > 0) {
                    double cant = cadena[1].length() == 1
                            ? (double) Integer.parseInt(cadena[1]) / 10 * txt_nid_upresal.getValue()
                            : (double) Integer.parseInt(cadena[1]) / 100 * txt_nid_upresal.getValue();
                    int fraccion = (int) cant;
                    txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]));
                    txt_nid_fracsal.setValue(fraccion);
                } else {
                    if (txt_nid_fracsal.getValue() == null) {
                        txt_nid_fracsal.setValue(0);
                    }
                }
                txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
            }
        } else {
            if (txt_nid_enterosal.getValue() == null && txt_nid_proidsal.getValue().isEmpty()) {
                lst_notinterdet.focus();
            }

        }
    }

    @Listen("onOK=#txt_nid_fracent")
    public void validaCantFracE() {
        if (!txt_nid_proident.getValue().isEmpty() && txt_nid_proident.getValue().matches("[0-9]*")) {
            if (txt_nid_fracent.getValue() == null && txt_nid_enteroent.getValue() != null) {
                txt_nid_fracent.setValue(0);
                txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                cb_nid_tipdoc.focus();
            } else {
                if (txt_nid_enteroent.getValue() == null) {
                    if (txt_nid_fracent.getValue() != null) {
                        if (txt_nid_upreent.getValue() <= txt_nid_fracent.getValue()) {
                            double d_calculosal = txt_nid_fracent.getValue() / txt_nid_upreent.getValue();
                            String s_calculosal = String.valueOf(d_calculosal);
                            String[] cadena = s_calculosal.split("[.]");
                            int d_calculofrac = txt_nid_fracent.getValue() % txt_nid_upreent.getValue();
                            txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]));
                            txt_nid_fracent.setValue(d_calculofrac);
                            txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                            cb_nid_tipdoc.focus();
                        }
                    } else {
                        Messagebox.show("Ingrese cantidad entera de entrada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nid_enteroent.focus();
                                }
                            }
                        });
                    }
                } else {
                    String s_cal = String.valueOf(txt_nid_enteroent.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_ent = Integer.parseInt(cad[0]);
                    if (txt_nid_upreent.getValue() <= txt_nid_fracent.getValue()) {
                        double d_calculosal = txt_nid_fracent.getValue() / txt_nid_upreent.getValue();
                        String s_calculosal = String.valueOf(d_calculosal);
                        String[] cadena = s_calculosal.split("[.]");
                        int d_calculofrac = txt_nid_fracent.getValue() % txt_nid_upreent.getValue();
                        txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]) + i_ent);
                        txt_nid_fracent.setValue(d_calculofrac);
                        txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                        cb_nid_tipdoc.focus();
                    } else {
                        txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                        cb_nid_tipdoc.focus();
                    }
                }
            }
        } else if (txt_nid_fracent.getValue() != null && txt_nid_proident.getValue().isEmpty()) {
            lst_notinterdet.focus();
        }
    }

    @Listen("onOK=#txt_nid_fracsal")
    public void validaCantFracS() {
        if (!txt_nid_proidsal.getValue().isEmpty() && txt_nid_proidsal.getValue().matches("[0-9]*")) {
            if (txt_nid_fracsal.getValue() == null && txt_nid_enterosal.getValue() != null) {
                txt_nid_fracsal.setValue(0);
                String s_calculo = String.valueOf(txt_nid_enterosal.getValue());
                String[] cadena = s_calculo.split("[.]");
                int entero = Integer.parseInt(cadena[0]);
                txt_nid_pretotalsal.setValue((entero * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                txt_nid_proident.focus();
            } else {
                if (txt_nid_enterosal.getValue() == null) {
                    if (txt_nid_fracsal.getValue() != null) {
                        if (txt_nid_upresal.getValue() <= txt_nid_fracsal.getValue()) {
                            double d_calculosal = txt_nid_fracsal.getValue() / txt_nid_upresal.getValue();
                            String s_calculosal = String.valueOf(d_calculosal);
                            String[] cadena = s_calculosal.split("[.]");
                            int d_calculofrac = txt_nid_fracsal.getValue() % txt_nid_upresal.getValue();
                            txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]));
                            txt_nid_fracsal.setValue(d_calculofrac);
                            txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                            if (txt_nid_proident.isDisabled()) {
                                txt_nid_enteroent.focus();
                            } else {
                                txt_nid_proident.focus();
                            }
                        }
                    } else {
                        Messagebox.show("Ingrese cantidad entera de salida", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nid_enterosal.focus();
                                }
                            }
                        });
                    }
                } else {
                    String s_cal = String.valueOf(txt_nid_enterosal.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_entsal = Integer.parseInt(cad[0]);
                    if (txt_nid_upresal.getValue() <= txt_nid_fracsal.getValue()) {
                        double d_calculosal = txt_nid_fracsal.getValue() / txt_nid_upresal.getValue();
                        String s_calculosal = String.valueOf(d_calculosal);
                        String[] cadena = s_calculosal.split("[.]");
                        int d_calculofrac = txt_nid_fracsal.getValue() % txt_nid_upresal.getValue();
                        txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]) + i_entsal);
                        txt_nid_fracsal.setValue(d_calculofrac);
                        txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                        if (txt_nid_proident.isDisabled()) {
                            txt_nid_enteroent.focus();
                        } else {
                            txt_nid_proident.focus();
                        }
                    } else {
                        txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                        if (txt_nid_proident.isDisabled()) {
                            txt_nid_enteroent.focus();
                        } else {
                            txt_nid_proident.focus();
                        }
                    }
                }
            }
        } else if (txt_nid_fracsal.getValue() != null && txt_nid_proidsal.getValue().isEmpty()) {
            lst_notinterdet.focus();
        }
    }

    @Listen("onBlur=#txt_nid_fracent")
    public void ValidaCantFracE() {
        if (!txt_nid_proident.getValue().isEmpty() && txt_nid_proident.getValue().matches("[0-9]*")) {
            if (txt_nid_fracent.getValue() == null && txt_nid_enteroent.getValue() != null) {
                txt_nid_fracent.setValue(0);
                txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
            } else {
                if (txt_nid_enteroent.getValue() == null) {
                    if (txt_nid_fracent.getValue() != null) {
                        if (txt_nid_upreent.getValue() <= txt_nid_fracent.getValue()) {
                            double d_calculosal = txt_nid_fracent.getValue() / txt_nid_upreent.getValue();
                            String s_calculosal = String.valueOf(d_calculosal);
                            String[] cadena = s_calculosal.split("[.]");
                            int d_calculofrac = txt_nid_fracent.getValue() % txt_nid_upreent.getValue();
                            txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]));
                            txt_nid_fracent.setValue(d_calculofrac);
                            txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                        }
                    }
                } else {
                    String s_cal = String.valueOf(txt_nid_enteroent.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_entsal = Integer.parseInt(cad[0]);
                    if (txt_nid_upreent.getValue() <= txt_nid_fracent.getValue()) {
                        double d_calculosal = txt_nid_fracent.getValue() / txt_nid_upreent.getValue();
                        String s_calculosal = String.valueOf(d_calculosal);
                        String[] cadena = s_calculosal.split("[.]");
                        int d_calculofrac = txt_nid_fracent.getValue() % txt_nid_upreent.getValue();
                        txt_nid_enteroent.setValue(Integer.parseInt(cadena[0]) + i_entsal);
                        txt_nid_fracent.setValue(d_calculofrac);
                        txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                    } else {
                        txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                    }
                }
            }
        } else {
            if (txt_nid_fracent.getValue() != null && txt_nid_proident.getValue().isEmpty()) {
                Messagebox.show("Ingrese producto de entrada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleE();
                            txt_nid_proident.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onBlur=#txt_nid_fracsal")
    public void ValidaCantFracS() {
        if (!txt_nid_proidsal.getValue().isEmpty() && txt_nid_proidsal.getValue().matches("[0-9]*")) {
            if (txt_nid_fracsal.getValue() == null && txt_nid_enterosal.getValue() != null) {
                txt_nid_fracsal.setValue(0);
                txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
            } else {
                if (txt_nid_enterosal.getValue() == null) {
                    if (txt_nid_fracsal.getValue() != null) {
                        if (txt_nid_upresal.getValue() <= txt_nid_fracsal.getValue()) {
                            double d_calculosal = txt_nid_fracsal.getValue() / txt_nid_upresal.getValue();
                            String s_calculosal = String.valueOf(d_calculosal);
                            String[] cadena = s_calculosal.split("[.]");
                            int d_calculofrac = txt_nid_fracsal.getValue() % txt_nid_upresal.getValue();
                            txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]));
                            txt_nid_fracsal.setValue(d_calculofrac);
                            txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                            // txt_nid_proident.focus();
                        }
                    }
                } else {
                    String s_cal = String.valueOf(txt_nid_enterosal.getValue());
                    String[] cad = s_cal.split("[.]");
                    int i_entsal = Integer.parseInt(cad[0]);
                    if (txt_nid_upresal.getValue() <= txt_nid_fracsal.getValue()) {
                        double d_calculosal = txt_nid_fracsal.getValue() / txt_nid_upresal.getValue();
                        String s_calculosal = String.valueOf(d_calculosal);
                        String[] cadena = s_calculosal.split("[.]");
                        int d_calculofrac = txt_nid_fracsal.getValue() % txt_nid_upresal.getValue();
                        txt_nid_enterosal.setValue(Integer.parseInt(cadena[0]) + i_entsal);
                        txt_nid_fracsal.setValue(d_calculofrac);
                        txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                        // txt_nid_proident.focus();
                    } else {
                        txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));
                        // txt_nid_proident.focus();
                    }
                }
            }
        } else {
            if (txt_nid_fracsal.getValue() != null && txt_nid_proidsal.getValue().isEmpty()) {
                Messagebox.show("Ingrese producto de salida", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleS();
                            txt_nid_proidsal.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onOK=#d_ni_fecemi")
    public void validaFechaEmision() throws SQLException {
        if (d_ni_fecemi.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Emision de la Nota de Intercambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String fecemi = sdf.format(d_ni_fecemi.getValue());
            if (objDaoCierreDia.ValidaDia(fecemi, "L") == null || objDaoCierreDia.ValidaDia(fecemi, "L").getCiedia_log() == 0) {
                Messagebox.show("El dia se encuentra cerrado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                d_ni_fecent.focus();
            }
        }
    }

    @Listen("onOK=#d_ni_fecent")
    public void validaFechaEntrega() {
        if (d_ni_fecent.getValue() == null) {
            Messagebox.show("Por favor Ingrese la Fecha de Emision de la Nota de Intercambio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (verificarFechas().isEmpty()) {
                txt_ni_motcamid.focus();
            } else {
                Messagebox.show(verificarFechas(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onOK=#cb_nid_tipdoc")
    public void onSelectTipoDoc() throws SQLException {
        if (cb_nid_tipdoc.getSelectedIndex() == -1) {
            cb_nid_tipdoc.focus();
            cb_nid_tipdoc.select();
        } else {
            txt_nid_serie.focus();
        }
    }

    @Listen("onOK=#txt_nid_doc")
    public void validaDocumento() {
        if (!txt_nid_doc.getValue().matches("^[0-9]*")) {
            Messagebox.show("Ingrese datos numericos en el campo 'Documento'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nid_doc.focus();
                    }
                }
            });
        } else if (txt_nid_doc.getValue().length() != 7 && txt_nid_doc.getValue().length() != 8) {
            Messagebox.show("En el campo 'Documento' debe ingresar 7 o 8 caracteres", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nid_doc.focus();
                    }
                }
            });
        } else if (Integer.parseInt(txt_nid_doc.getValue()) <= 0) {
            Messagebox.show("En el campo 'Documento' debe ingresar valores numericos mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event)
                        throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_nid_doc.focus();
                    }
                }
            });
        } else {
            txt_nid_glosa.focus();
        }
    }

    @Listen("onOK=#txt_nid_serie")
    public void validaSerie() {
        if (!txt_nid_serie.getValue().isEmpty()) {
            if (txt_nid_serie.getValue().matches("^[0-9]*") || txt_nid_serie.getValue().matches("^\\w*")) {
                if (txt_nid_serie.getValue().matches("^[0-9]*") && txt_nid_serie.getValue().length() <= 2) {
                    Messagebox.show("Debe ingresar valores  mayores a 2 en  'Serie'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nid_serie.focus();
                            }
                        }
                    });
                } else if (txt_nid_serie.getValue().matches("^[0-9]*")
                        && Integer.parseInt(txt_nid_serie.getValue()) <= 0) {
                    Messagebox.show("En el campo 'Serie' debe ingresar numero mayor a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nid_serie.focus();
                            }
                        }
                    });
                } else if (txt_nid_serie.getValue().matches("^\\w*") && txt_nid_serie.getValue().length() <= 2) {
                    Messagebox.show("En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nid_serie.focus();
                            }
                        }
                    });
                } else {
                    txt_nid_doc.focus();
                }
            } else {
                Messagebox.show("Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_nid_serie.focus();
                        }
                    }
                });
            }
        } else {
            txt_nid_doc.focus();
        }
    }

    @Listen("onOK=#txt_nid_glosa")
    public void validaGlosa() throws SQLException {
        agregarDetalle();
    }

    @Listen("onBlur=#txt_nic_provid")
    public void onBlur_txtProvid() throws SQLException {
        if (!txt_nic_provid.getValue().isEmpty()) {
            if (!txt_nic_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        limpiarProvLp();
                        txt_nic_provid.focus();
                    }
                });
            } else if (objlstNotaInterDet.getSize() > 0 && txt_nic_provid.isReadonly()) {
                if (sok.equals("libre")) {
                    sok = "en espera";
                    Messagebox.show("Esta seguro que desea cambiar el proveedor?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            // Si la respuesta es SI = OK
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                if (!s_estado.equals("N")) {
                                    objlstEliminacion.addAll(objlstNotaInterDet);
                                }
                                txt_nic_lpcid.setReadonly(false);
                                txt_nic_provid.setReadonly(false);
                                limpiarProvLp();
                                limpiarCamposDetalle();
                                limpiarListaDet();
                                sok = "libre";
                            } else {
                                sok = "libre";
                            }
                        }
                    });
                }
            } else {
                txt_nic_provid.setValue(Utilitarios.lpad(txt_nic_provid.getValue(), 8, "0"));
                Long prov_id = Long.parseLong(txt_nic_provid.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    Messagebox.show("El codigo de proveedor no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            limpiarProvLp();
                            txt_nic_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedor.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_nic_provid.setValue(objProveedor.getProv_id());
                    txt_nic_provdes.setValue(objProveedor.getProv_razsoc());
                    if (!txt_nic_lpcid.getValue().isEmpty() && txt_nic_lpcid.getValue().matches("[0-9]*")) {
                        txt_nic_lpcid.setValue(Utilitarios.lpad(txt_nic_lpcid.getValue(), 4, "0"));
                        int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                        ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_id, lpc_key);
                        if (objLpCompra == null) {
                            txt_nic_lpcid.setValue("");
                            txt_nic_lpcdes.setValue("");
                        }
                    }
                }
            }
        } else {
            txt_nic_provdes.setValue("");
            txt_nic_lpcid.setValue("");
            txt_nic_lpcdes.setValue("");
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nic_lpcid")
    public void onBlur_txtLpcid() throws SQLException {
        if (ok.equals("s") || !txt_nic_lpcid.getValue().isEmpty() && txt_nic_provid.getValue().isEmpty()) {
            Messagebox.show("Debe ingresar Proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
                    txt_nic_lpcid.setValue("");
                    txt_nic_lpcid.setValue("");
                    txt_nic_provid.focus();
                    ok = "";
                }
            });
        } else {
            if (txt_nic_provid.getValue().matches("[0-9]*")) {
                if (!txt_nic_lpcid.getValue().isEmpty()) {
                    if (!txt_nic_lpcid.getValue().matches("[0-9]*")) {
                        Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                            public void onEvent(Event t) throws Exception {
                                txt_nic_lpcid.setValue("");
                                txt_nic_lpcdes.setValue("");
                                txt_nic_lpcid.focus();
                            }
                        });
                    } else if (objlstNotaInterDet.getSize() > 0 && txt_nic_lpcid.isReadonly()) {
                        if (sok.equals("libre")) {
                            sok = "en espera";
                            Messagebox.show("Esta seguro que desea cambiar la lista de precio?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    // Si la respuesta es SI = OK
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        if (!s_estado.equals("N")) {
                                            objlstEliminacion.addAll(objlstNotaInterDet);
                                        }
                                        txt_nic_lpcid.setReadonly(false);
                                        txt_nic_provid.setReadonly(false);
                                        txt_nic_lpcid.setValue("");
                                        txt_nic_lpcdes.setValue("");
                                        limpiarCamposDetalle();
                                        limpiarListaDet();
                                        sok = "libre";
                                    } else {
                                        sok = "libre";
                                    }
                                }
                            });
                        }
                    } else {
                        long prov_key = Long.parseLong(txt_nic_provid.getValue());
                        int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                        ListaPrecio objLpCompra = new DaoListaPrecios().getListaPreCompxProv(emp_id, suc_id, prov_key, lpc_key);
                        if (objLpCompra == null) {
                            s_mensaje = "El codigo de la 'lista de precio' no existe o esta eliminado";
                            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    // Si la respuesta es SI = OK
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_nic_lpcid.setValue("");
                                        txt_nic_lpcdes.setValue("");
                                        txt_nic_lpcid.focus();
                                    }
                                }
                            });
                        } else {
                            LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | ha cargado los datos de la Lista Precio de Compra ").append(objLpCompra.getLp_id()).append(" y ha encontrado 1 registro(s)").toString());
                            txt_nic_lpcid.setValue(objLpCompra.getLp_id());
                            txt_nic_lpcdes.setValue(objLpCompra.getLp_des());
                        }
                    }
                } else {
                    txt_nic_lpcid.setValue("");
                    txt_nic_lpcdes.setValue("");
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nid_proident")
    public void validaProductosEntrada() throws SQLException {
        String prod = txt_nid_proident.getValue() == null ? "" : txt_nid_proident.getValue();
        if (i_camprode == 0) {
            txt_nid_prodesent.setValue("");
            if (!prod.isEmpty()) {
                if (!prod.matches("[0-9]*")) {
                    Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nid_prodesent.setValue("");
                                txt_nid_proident.setValue("");
                                txt_nid_enteroent.setValue(null);
                                txt_nid_fracent.setValue(null);
                                txt_nid_upreent.setValue(null);
                                txt_nid_pretotalent.setValue(null);
                                txt_nid_proident.focus();
                            }
                        }
                    });
                } else {
                    // String pro_id = txt_nid_proident.getValue() == null ? ""
                    // : txt_nid_proident.getValue();
                    long prov_key = Long.parseLong(txt_nic_provid.getValue());
                    int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                    objProductoEnt = new DaoProductos().buscarProducto(prod, txt_nic_provid.getValue());
                    objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, prod, prov_key, lpc_key);
                    if (objProductoEnt == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else {
                        i_camprode = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proident.setValue(objProductoEnt.getPro_id());
                        s_prode = txt_nid_proident.getValue();
                        txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                        txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                        txt_nid_preident.setValue(objPrecios.getPre_valvent());
                        txt_nid_enteroent.setValue(null);
                        txt_nid_fracent.setValue(null);
                        txt_nid_pretotalent.setValue(null);
                    }
                }
            } else {
                i_camprode = 0;
            }
        } else {
            txt_nid_prodesent.setValue("");
            if (prod.equals(s_prode)) {
                cargaDatosProdE(prod, 1);
            } else {
                cargaDatosProdE(prod, 0);
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_nid_proidsal")
    public void validaProductosSalida() throws SQLException {
        String prod = txt_nid_proidsal.getValue() == null ? "" : txt_nid_proidsal.getValue();
        if (i_camprods == 0) {
            txt_nid_prodessal.setValue("");
            if (!prod.isEmpty()) {
                if (!prod.matches("[0-9]*")) {
                    Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarCamposDetalleS();
                                txt_nid_proidsal.focus();
                            }
                        }
                    });
                } else {
                    // String pro_id = txt_nid_proident.getValue() == null ? ""
                    // : txt_nid_proident.getValue();
                    long prov_key = Long.parseLong(txt_nic_provid.getValue());
                    int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                    objProductoSal = new DaoProductos().buscarProducto(prod, txt_nic_provid.getValue());
                    objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, prod, prov_key, lpc_key);
                    if (objProductoSal == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else {
                        i_camprods = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                        s_prods = txt_nid_proidsal.getValue();
                        txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                        txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                        txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                    }
                }
            } else {
                i_camprods = 0;
            }
        } else {
            txt_nid_prodessal.setValue("");
            if (prod.equals(s_prods)) {
                cargaDatosProdS(prod, 1);
            } else {
                cargaDatosProdS(prod, 0);
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_venid")
    public void validaVendedor() throws SQLException {
        txt_vennom.setValue("");
        if (!txt_venid.getValue().isEmpty()) {
            if (!txt_venid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_venid.setValue("");
            } else {
                int ven_id = Integer.parseInt(txt_venid.getValue());
                Vendedor objVendedorCon = new DaoVendedores().getNomVendedor(ven_id);
                if (objVendedorCon == null) {
                    Messagebox.show("El Codigo de Vendedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    txt_venid.setValue("");
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Vendedor " + objVendedorCon.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_venid.setValue(objVendedorCon.getVen_id());
                    txt_vennom.setValue(objVendedorCon.getVen_ape() + " " + objVendedorCon.getVen_nom());
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_ni_motcamid")
    public void validaMotCambio() throws SQLException {
        txt_ni_motcamdes.setValue("");
        if (!txt_ni_motcamid.getValue().isEmpty()) {
            if (!txt_ni_motcamid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor Ingrese Datos Numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_ni_motcamid.setValue(null);
                            txt_ni_motcamdes.setValue(null);
                            txt_ni_motcamid.focus();
                        }
                    }
                });
            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_ni_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    Messagebox.show("El Codigo del Motivo de Cambio no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event)
                                throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ni_motcamid.setValue(null);
                                txt_ni_motcamdes.setValue(null);
                                txt_ni_motcamid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_ni_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_ni_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                    // txt_nic_provid.focus();
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_clidir_id")
    public void validaDirecciones() throws SQLException {
        if (txt_cli_id.getValue().isEmpty() && txt_clidir_id.getValue() != null) {
            Messagebox.show("Por favor Verifique Primero El Codigo del Cliente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
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
                    CliDireccion objCliDireccion = new DaoDirecciones().getNomDireccion(cli_id, clidir_id, emp_id, suc_id);
                    if (objCliDireccion == null) {
                        Messagebox.show("El Codigo de Direccion no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
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
                        txt_ni_transid.setValue(objCliDireccion.getTrans_id());
                        txt_ni_transdes.setValue(objCliDireccion.getTrans_des());
                        txt_ni_venid.setValue(objCliDireccion.getVen_id());
                        txt_ni_vennom.setValue(objCliDireccion.getVen_apenom());
                        txt_ni_horid.setValue(objCliDireccion.getHor_id());
                        txt_ni_hordes.setValue(objCliDireccion.getHor_des());
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_cli_id")
    public void validaClienteNotaIntercambio() throws SQLException {
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
                objCliente = new DaoCliente().getDireccionDefault(txt_cli_id.getValue(), emp_id, suc_id);
                if (objCliente == null) {
                    Messagebox.show("El Codigo del Cliente no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
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
                    txt_ni_venid.setValue(objCliente.getVen_id());
                    txt_sup_id.setValue(objCliente.getSup_id());
                    txt_ni_vennom.setValue(objCliente.getVen_apenom());
                    if (objCliente.getHor_id() == null) {
                        Messagebox.show("El Codigo del Cliente no tiene direccion asignada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCliDirVacio();
                                    txt_cli_id.focus();
                                }
                            }
                        });
                    } else {
                        txt_ni_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                        txt_ni_hordes.setValue(objCliente.getHor_des());
                        txt_ni_transid.setValue(objCliente.getTrans_id());
                        txt_ni_transdes.setValue(objCliente.getTrans_alias());
                        txt_clidir_id.setValue(objCliente.getClidir_id());
                        txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                        // txt_clidir_id.focus();
                    }
                }
            }
        }
        bandera = false;
    }

    // Eventos Otros
    public void cargaDatosProdS(String prods, int elicant) throws SQLException {
        if (!prods.isEmpty()) {
            if (!prods.matches("[0-9]*")) {
                s_mensaje = "Por favor Ingrese Datos Numericos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        // Si la respuesta es SI = OK
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleS();
                            txt_nid_proidsal.focus();
                        }
                    }
                });
            } else {
                long prov_key = Long.parseLong(txt_nic_provid.getValue());
                int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                objProductoSal = new DaoProductos().buscarProducto(prods, txt_nic_provid.getValue());
                objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, prods, prov_key, lpc_key);
                if (elicant == 0) {
                    if (objProductoSal == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                        s_prods = txt_nid_proidsal.getValue();
                        txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                        txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                        txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                        txt_nid_enterosal.setValue(null);
                        txt_nid_fracsal.setValue(null);
                        txt_nid_pretotalsal.setValue(null);
                    }
                } else {
                    if (objProductoSal == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleS();
                                    txt_nid_proidsal.focus();
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                        s_prods = txt_nid_proidsal.getValue();
                        txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                        txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                        txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                    }
                }
            }
        } else {
            limpiarCamposDetalleS();
        }
    }

    public void cargaDatosProdE(String prode, int elicant) throws SQLException {
        if (!prode.isEmpty()) {
            if (!prode.matches("[0-9]*")) {
                s_mensaje = "Por favor Ingrese Datos Numericos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        // Si la respuesta es SI = OK
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCamposDetalleE();
                            txt_nid_proident.focus();
                        }
                    }
                });
            } else {
                long prov_key = Long.parseLong(txt_nic_provid.getValue());
                int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
                objProductoEnt = new DaoProductos().buscarProducto(prode, txt_nic_provid.getValue());
                objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, prode, prov_key, lpc_key);
                if (elicant == 0) {
                    // objProductoEnt = new DaoProductos().buscarProducto(prod,
                    // txt_nic_provid.getValue());
                    if (objProductoEnt == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proident.setValue(objProductoEnt.getPro_id());
                        s_prode = txt_nid_proident.getValue();
                        txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                        txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                        txt_nid_preident.setValue(objPrecios.getPre_valvent());
                        txt_nid_enteroent.setValue(null);
                        txt_nid_fracent.setValue(null);
                        txt_nid_pretotalent.setValue(null);
                    }
                } else {
                    if (objProductoEnt == null) {
                        Messagebox.show("El Codigo de Producto no existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else if (objPrecios == null) {
                        Messagebox.show("El Codigo de Producto no tiene precio", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalleE();
                                    txt_nid_proident.focus();
                                }
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proident.setValue(objProductoEnt.getPro_id());
                        s_prode = txt_nid_proident.getValue();
                        txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                        txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                        txt_nid_preident.setValue(objPrecios.getPre_valvent());
                    }
                }
            }
        } else {
            limpiarCamposDetalleE();
        }
    }

    /*public void cargaDatosProv(String prov, int limlp) throws SQLException {
     if (!prov.isEmpty()) {
     if (limlp == 0) {
     Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.parseLong(prov));
     if (objProveedor == null) {
     Messagebox.show("El Codigo de Proveedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     limpiarProvLp();
     txt_nic_provid.focus();
     }
     }
     });
     } else {
     txt_nic_provid.setValue(objProveedor.getProv_id());
     s_prov = txt_nic_provid.getValue();
     txt_nic_provdes.setValue(objProveedor.getProv_razsoc());
     txt_nic_lpcid.setValue("");
     txt_nic_lpcdes.setValue("");
     LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
     }
     } else {
     Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.parseLong(prov));
     if (objProveedor == null) {
     Messagebox.show("El Codigo de Proveedor no Existe o esta Eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     limpiarProvLp();
     txt_nic_provid.focus();
     }
     }
     });
     } else {
     txt_nic_provid.setValue(objProveedor.getProv_id());
     s_prov = txt_nic_provid.getValue();
     txt_nic_provdes.setValue(objProveedor.getProv_razsoc());

     LOGGER.info((new StringBuilder()).append("[").append(objUsuarioCredential.getComputerName()).append("] | ").append(objUsuarioCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
     }
     }
     } else {
     limpiarProvLp();
     }*/
    /*
     * if (!prov.isEmpty()) { if (prov.matches("[0-9]*")) { long prov_id =
     * Long.parseLong(prov); Proveedores objProveedor = (new
     * DaoProveedores()).BusquedaProveedor(prov_id); if (limlp == 0) { if
     * (objProveedor == null) {
     * Messagebox.show("El Codigo de Proveedor no Existe o esta Eliminado",
     * "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new
     * EventListener() {
     * 
     * @Override public void onEvent(Event event) throws Exception { //Si la
     * respuesta es SI = OK if (((Integer) event.getData()).intValue() ==
     * Messagebox.OK) { limpiarProvLp(); txt_nic_provid.focus(); } } }); }
     * else { txt_nic_provid.setValue(objProveedor.getProv_id()); s_prov =
     * txt_nic_provid.getValue();
     * txt_nic_provdes.setValue(objProveedor.getProv_razsoc());
     * txt_nic_lpcid.setValue(""); txt_nic_lpcdes.setValue("");
     * txt_nic_lpcid.focus(); LOGGER.info((new
     * StringBuilder()).append("[").append
     * (objUsuarioCredential.getComputerName
     * ()).append("] | ").append(objUsuarioCredential
     * .getCuenta()).append(" | ha cargado los datos del Proveedor "
     * ).append(
     * objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)"
     * ).toString()); } } else { if (objProveedor == null) {
     * Messagebox.show("El Codigo de Proveedor no Existe o esta Eliminado",
     * "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new
     * EventListener() {
     * 
     * @Override public void onEvent(Event event) throws Exception { //Si la
     * respuesta es SI = OK if (((Integer) event.getData()).intValue() ==
     * Messagebox.OK) { limpiarProvLp(); txt_nic_provid.focus(); } } }); }
     * else {
     * 
     * txt_nic_provid.setValue(objProveedor.getProv_id()); s_prov =
     * txt_nic_provid.getValue();
     * txt_nic_provdes.setValue(objProveedor.getProv_razsoc());
     * txt_nic_lpcid.focus(); LOGGER.info((new
     * StringBuilder()).append("[").append
     * (objUsuarioCredential.getComputerName
     * ()).append("] | ").append(objUsuarioCredential
     * .getCuenta()).append(" | ha cargado los datos del Proveedor "
     * ).append(
     * objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)"
     * ).toString()); } } } else { //limpiarProvLp();
     * txt_nic_provid.focus(); } } else { limpiarProvLp(); }
     */
    //}
    public String validaProducto(String pro_id) {
        int i = 0;
        String msj_prod = "";
        int cantDet = objlstNotaInterDet.getSize();
        boolean validaIngreso = true;
        String serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue();
        while (i < cantDet && validaIngreso) {
            if (objlstNotaInterDet.get(i).getPro_id().equals(pro_id) && objlstNotaInterDet.get(i).getNid_indicador().equals("S")) {
                msj_prod = "El producto ya fue Ingresado";
                validaIngreso = false;
            } else if (objlstNotaInterDet.get(i).getNid_docref().equals(serie + txt_nid_doc.getValue())) {
                msj_prod = "El Doc. ya fue Ingresado";
                validaIngreso = false;
            } else if (objlstNotaInterDet.get(i).getPro_id().equals(txt_nid_proident.getValue())) {
                msj_prod = "El producto ya fue Ingresado";
                validaIngreso = false;
            }
            i++;
        }
        return msj_prod;
    }

    public void validaMotivoCambio() throws SQLException {
        txt_ni_motcamdes.setValue("");
        if (!txt_ni_motcamid.getValue().isEmpty()) {
            if (!txt_ni_motcamid.getValue().matches("[0-9]*")) {
                d_ni_fecent.focus();
            } else {
                String motcamid = String.valueOf(Integer.parseInt(txt_ni_motcamid.getValue()));
                MotCam objMotivoCambio = new DaoMotivoCambio().getNomMotivoCambio(motcamid);
                if (objMotivoCambio == null) {
                    d_ni_fecent.focus();
                } else {
                    LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Motivo cambio " + objMotivoCambio.getTab_subdir() + "y ha encontrado 1 registro(s)");
                    txt_ni_motcamid.setValue(objMotivoCambio.getTab_subdir());
                    txt_ni_motcamdes.setValue(objMotivoCambio.getTab_subdes());
                }
            }
        }
        bandera = false;
    }

    public void validaProductoE() throws SQLException {

        String prode = txt_nid_proident.getValue() == null ? "" : txt_nid_proident.getValue();
        if (i_camprode == 0) {
            txt_nid_prodesent.setValue("");
            if (!prode.isEmpty()) {
                if (!prode.matches("[0-9]*")) {
                    txt_nid_enteroent.focus();
                } else {
                    // String pro_id = txt_nid_proident.getValue() == null ? ""
                    // : txt_nid_proident.getValue();
                    objProductoEnt = new DaoProductos().buscarProducto(prode, txt_nic_provid.getValue());
                    objPrecios = new DaoPrecios().listaProvPrecioNIC(prode);
                    if (objPrecios == null || objProductoEnt == null) {
                        txt_nid_enteroent.focus();
                    } else {
                        i_camprode = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proident.setValue(objProductoEnt.getPro_id());
                        txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                        txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                        txt_nid_preident.setValue(objPrecios.getPre_valvent());
                        txt_nid_enteroent.focus();

                    }
                }
            } else {
                i_camprode = 0;
            }
        } else {
            txt_nid_prodesent.setValue("");

            if (prode.equals(s_prode)) {
                if (!prode.isEmpty()) {
                    if (!prode.matches("[0-9]*")) {
                        txt_nid_enteroent.focus();
                    } else {
                        objProductoEnt = new DaoProductos().buscarProducto(prode, txt_nic_provid.getValue());
                        objPrecios = new DaoPrecios().listaProvPrecioNIC(prode);
                        if (objProductoEnt == null || objPrecios == null) {
                            txt_nid_enteroent.focus();
                        } else {
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                            txt_nid_proident.setValue(objProductoEnt.getPro_id());
                            s_prode = txt_nid_proident.getValue();
                            txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                            txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                            txt_nid_preident.setValue(objPrecios.getPre_valvent());
                            txt_nid_enteroent.focus();
                        }
                    }
                }
            } else {
                if (!prode.isEmpty()) {
                    if (!prode.matches("[0-9]*")) {
                        txt_nid_enteroent.focus();
                    } else {
                        objProductoEnt = new DaoProductos().buscarProducto(prode, txt_nic_provid.getValue());
                        objPrecios = new DaoPrecios().listaProvPrecioNIC(prode);
                        if (objProductoEnt == null || objPrecios == null) {
                            txt_nid_enteroent.focus();
                        } else {
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Entrante con Codigo " + objProductoEnt.getPro_id() + " y ha encontrado 1 registro(s)");
                            txt_nid_proident.setValue(objProductoEnt.getPro_id());
                            s_prode = txt_nid_proident.getValue();
                            txt_nid_prodesent.setValue(objProductoEnt.getPro_des());
                            txt_nid_upreent.setValue(objProductoEnt.getPro_presminven());
                            txt_nid_preident.setValue(objPrecios.getPre_valvent());
                            txt_nid_enteroent.setValue(null);
                            txt_nid_fracent.setValue(null);
                            txt_nid_pretotalent.setValue(null);
                            txt_nid_enteroent.focus();
                        }
                    }
                }

            }
        }
        bandera = false;
    }

    public void validaProductoS() throws SQLException {
        String prods = txt_nid_proidsal.getValue() == null ? "" : txt_nid_proidsal.getValue();
        if (i_camprods == 0) {
            txt_nid_prodessal.setValue("");
            if (!prods.isEmpty()) {
                if (!prods.matches("[0-9]*")) {
                    txt_nid_enterosal.focus();
                } else {
                    // String pro_id = txt_nid_proidsal.getValue()== null ? "" :
                    // txt_nid_proidsal.getValue();
                    objProductoSal = new DaoProductos().buscarProducto(prods, txt_nic_provid.getValue());
                    objPrecios = new DaoPrecios().listaProvPrecioNIC(prods);
                    if (objProductoSal == null || objPrecios == null) {
                        txt_nid_enterosal.focus();
                    } else {
                        i_camprods = 1;
                        LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                        txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                        s_prods = txt_nid_proidsal.getValue();
                        txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                        txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                        txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                        txt_nid_enterosal.focus();
                    }
                }
            } else {
                i_camprods = 0;
            }
        } else {
            txt_nid_prodessal.setValue("");
            if (prods.equals(s_prods)) {
                if (!prods.isEmpty()) {
                    if (!prods.matches("[0-9]*")) {
                        txt_nid_enterosal.focus();
                    } else {
                        objProductoSal = new DaoProductos().buscarProducto(prods, txt_nic_provid.getValue());
                        objPrecios = new DaoPrecios().listaProvPrecioNIC(prods);
                        if (objProductoSal == null || objPrecios == null) {
                            txt_nid_enterosal.focus();
                        } else {
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                            txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                            s_prods = txt_nid_proidsal.getValue();
                            txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                            txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                            txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                            txt_nid_enterosal.focus();
                        }
                    }
                }
            } else {
                if (!prods.isEmpty()) {
                    if (!prods.matches("[0-9]*")) {
                        txt_nid_enterosal.focus();
                    } else {
                        objProductoSal = new DaoProductos().buscarProducto(prods, txt_nic_provid.getValue());
                        objPrecios = new DaoPrecios().listaProvPrecioNIC(prods);
                        if (objProductoSal == null || objPrecios == null) {
                            txt_nid_enterosal.focus();
                        } else {
                            LOGGER.info("[" + objUsuarioCredential.getComputerName() + "] | " + objUsuarioCredential.getCuenta() + " | ha cargado los datos del Producto Saliente con Codigo " + objProductoSal.getPro_id() + " y ha encontrado 1 registro(s)");
                            txt_nid_proidsal.setValue(objProductoSal.getPro_id());
                            s_prods = txt_nid_proidsal.getValue();
                            txt_nid_prodessal.setValue(objProductoSal.getPro_des());
                            txt_nid_upresal.setValue(objProductoSal.getPro_presminven());
                            txt_nid_preidsal.setValue(objPrecios.getPre_valvent());
                            txt_nid_enterosal.setValue(null);
                            txt_nid_fracsal.setValue(null);
                            txt_nid_pretotalsal.setValue(null);
                            txt_nid_enterosal.focus();
                        }
                    }
                }

            }

        }
        bandera = false;
    }

    public String verificarCab() {
        String validar;
        if (d_ni_fecemi.getValue() == null) {
            validar = "El campo F. Emision es Obligatorio";
            campo = "F. Emision";
        } else if (d_ni_fecent.getValue() == null) {
            validar = "El campo F. Entrega es Obligatorio";
            campo = "F. Entrega";
        } else if (txt_ni_motcamid.getValue().isEmpty()) {
            validar = "El campo Motivo de Cambio es Obligatorio";
            campo = "M. Cambio";
        } else if (txt_ni_motcamdes.getValue().isEmpty()) {
            validar = "El campo Motivo de Cambio es Obligatorio";
            campo = "M. Cambio";
        } else if (txt_cli_id.getValue().isEmpty()) {
            validar = "El campo Cliente es Obligatorio";
            campo = "Cliente";
        } else if (txt_cli_razsoc.getValue().isEmpty()) {
            validar = "El campo Cliente es Obligatorio";
            campo = "Cliente";
        } else if (txt_nic_provid.getValue().isEmpty()) {
            validar = "El campo Proveedor es Obligatorio";
            campo = "Proveedor";
        } else if (txt_nic_provdes.getValue().isEmpty()) {
            validar = "El campo Proveedor es Obligatorio";
            campo = "Proveedor";
        } else if (txt_nic_lpcid.getValue().isEmpty()) {
            validar = "El campo Lista de Precio es Obligatorio";
            campo = "Lista Precio";
        } else if (txt_nic_lpcdes.getValue().isEmpty()) {
            validar = "El campo Lista de Precio es Obligatorio";
            campo = "Lista Precio";
        } else if (txt_clidir_id.getValue() == null) {
            validar = "El campo Direccion del Cliente es Obligatorio";
            campo = "Direccion";
        } else if (txt_zon_id.getValue().isEmpty()) {
            validar = "El campo Zona es Obligatorio";
            campo = "Zona";
        } else if (txt_ni_transid.getValue().isEmpty()) {
            validar = "El campo Transporte es Obligatorio";
            campo = "Transporte";
        } else if (txt_ni_venid.getValue().isEmpty()) {
            validar = "El campo Vendedor es Obligatorio";
            campo = "Vendedor";
        } else if (txt_ni_horid.getValue().isEmpty()) {
            validar = "El campo Horario es Obligatorio";
            campo = "Horario";
        } else {
            validar = "";
        }
        return validar;
    }

    public String verificarDetalle() throws SQLException {
        String validarDetalle;
        if (s_estadoDetalle.equals("N")) {
            if (txt_nid_proident.getValue().isEmpty()
                    || txt_nid_prodesent.getValue().isEmpty()) {
                validarDetalle = "El campo 'Productos Entrante' es obligatorio";
                campo = "Prod.Ent";
            } else if (txt_nid_proidsal.getValue().isEmpty()
                    || txt_nid_prodessal.getValue().isEmpty()) {
                validarDetalle = "El campo 'Productos Saliente' es obligatorio";
                campo = "Prod.Sal";
            } else if (txt_nid_enteroent.getValue() == null) {
                validarDetalle = "El producto entrante, el campo 'C ENT. ' es obligatorio";
                campo = "EC.Ent";
            } else if (txt_nid_fracent.getValue() == null) {
                validarDetalle = "El producto entrante, el campo 'C FRAC. ' es obligatorio";
                campo = "EC.Frac";
            } else if (txt_nid_enterosal.getValue() == null) {
                validarDetalle = "El producto saliente, el campo 'C ENT. ' es obligatorio";
                campo = "SC.Ent";
            } else if (txt_nid_fracsal.getValue() == null) {
                validarDetalle = "El producto saliente, el campo 'C FRAC. ' es obligatorio";
                campo = "SC.Frac";
            } else {
                Productos objProdEnt, objProdSal;
                objProdEnt = new DaoProductos().listaPro(txt_nid_proident.getValue());
                objProdSal = new DaoProductos().listaPro(txt_nid_proidsal.getValue());
                if (objProdEnt != null && objProdEnt.getPro_presminven() < txt_nid_fracent.getValue()) {
                    validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                    campo = "EC.Frac";
                } else if (objProdSal != null && objProdSal.getPro_presminven() < txt_nid_fracsal.getValue()) {
                    validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                    campo = "SC.Frac";
                } else if (txt_nid_enteroent.getValue() == 0 && txt_nid_fracent.getValue() == 0) {
                    validarDetalle = "En el producto entrante, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                } else if (txt_nid_enterosal.getValue() == 0 && txt_nid_fracsal.getValue() == 0) {
                    validarDetalle = "En el producto saliente, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                } else if (cb_nid_tipdoc.getSelectedIndex() == -1) {
                    validarDetalle = "El campo 'Tipo de Documento' es obligatorio";
                    campo = "Tip. Doc";
                } else if (txt_nid_serie.getValue().isEmpty()) {
                    validarDetalle = "El campo 'Serie' es obligatorio";
                    campo = "Serie";
                } else if (!txt_nid_serie.getValue().isEmpty() && !(txt_nid_serie.getValue().matches("^[0-9]*") || txt_nid_serie.getValue().matches("^\\w*"))) {
                    txt_nid_serie.setValue("");
                    validarDetalle = "Formato incorrecto, solo se puede ingresar valores numericos o alfanumerico";
                    campo = "Serie";
                } else if (!txt_nid_serie.getValue().isEmpty()
                        && txt_nid_serie.getValue().matches("^[0-9]*")
                        && txt_nid_serie.getValue().length() <= 2) {
                    validarDetalle = "Debe ingresar valores numericos mayores a 2 en el campo 'Serie'";
                    campo = "Serie";
                } else if (!txt_nid_serie.getValue().isEmpty()
                        && txt_nid_serie.getValue().matches("^[0-9]*")
                        && Integer.parseInt(txt_nid_serie.getValue()) <= 0) {
                    validarDetalle = "En el campo 'Serie' debe ingresar numero mayor a 0";
                    campo = "Serie";
                } else if (!txt_nid_serie.getValue().isEmpty()
                        && txt_nid_serie.getValue().matches("^\\w*")
                        && txt_nid_serie.getValue().length() <= 2) {
                    txt_nid_serie.setValue("");
                    validarDetalle = "En el campo 'Serie' debe ingresar valores alfanumerico mayores a 2";
                    campo = "Serie";
                } else if (txt_nid_doc.getValue().isEmpty()) {
                    validarDetalle = "El campo 'Documento'  es obligatorio";
                    campo = "Doc";
                } else if (!txt_nid_doc.getValue().matches("[0-9]*")) {
                    validarDetalle = "Ingrese datos numericos en el campo 'Documento' ";
                    campo = "Doc";
                } else if (txt_nid_doc.getValue().length() != 7
                        && txt_nid_doc.getValue().length() != 8) {
                    validarDetalle = "En el campo 'Documento' debe ingresar 7 o 8 caracteres";
                } else if (Integer.parseInt(txt_nid_doc.getValue()) <= 0) {
                    validarDetalle = "En el campo 'Documento' debe ingresar valores numericos mayores a 0";
                    campo = "Doc";
                } else if (txt_nid_pretotalsal.getValue() <= 0
                        || txt_nid_pretotalent.getValue() <= 0) {
                    validarDetalle = "El precio no puede ser 0";
                } else {
                    validarDetalle = "";
                }
            }
        } else {
            if (lst_notinterdet.getSelectedIndex() % 2 == 0) {
                if (txt_nid_proident.getValue().isEmpty()
                        || txt_nid_prodesent.getValue().isEmpty()) {
                    validarDetalle = "El campo 'Productos Entrante' es obligatorio";
                    campo = "Prod.Ent";
                    txt_nid_proident.setDisabled(true);
                } else if (cb_nid_tipdoc.getSelectedIndex() == -1) {
                    validarDetalle = "El campo 'Tipo de Documento' es obligatorio";
                    campo = "Tip. Doc";
                } else if (txt_nid_enteroent.getValue() == null) {
                    validarDetalle = "El producto entrante, el campo 'C ENT. ' es obligatorio";
                    campo = "EC.Ent";
                } else if (txt_nid_fracent.getValue() == null) {
                    validarDetalle = "El producto entrante, el campo 'C FRAC. ' es obligatorio";
                    campo = "EC.Frac";
                } else if (txt_nid_enterosal.getValue() == null) {
                    validarDetalle = "El producto saliente, el campo 'C ENT. ' es obligatorio";
                    campo = "SC.Ent";
                } else if (txt_nid_fracsal.getValue() == null) {
                    validarDetalle = "El producto saliente, el campo 'C FRAC. ' es obligatorio";
                    campo = "SC.Frac";
                } else {
                    Productos objProdEnt, objProdSal;
                    objProdEnt = new DaoProductos().listaPro(txt_nid_proident.getValue());
                    objProdSal = new DaoProductos().listaPro(txt_nid_proidsal.getValue());
                    if (objProdEnt != null && objProdEnt.getPro_presminven() < txt_nid_fracent.getValue()) {
                        validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                        campo = "EC.Frac";
                    } else if (objProdSal != null && objProdSal.getPro_presminven() < txt_nid_fracsal.getValue()) {
                        validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                        campo = "SC.Frac";
                    } else if (txt_nid_enteroent.getValue() == 0
                            && txt_nid_fracent.getValue() == 0) {
                        validarDetalle = "En el producto entrante, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                    } else if (txt_nid_enterosal.getValue() == 0
                            && txt_nid_fracsal.getValue() == 0) {
                        validarDetalle = "En el producto saliente, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                    } else if (txt_nid_pretotalsal.getValue() <= 0
                            || txt_nid_pretotalent.getValue() <= 0) {
                        validarDetalle = "El precio no puede ser 0";
                    } else {
                        validarDetalle = "";
                    }
                }
            } else {
                if (txt_nid_proidsal.getValue().isEmpty() || txt_nid_prodessal.getValue().isEmpty()) {
                    validarDetalle = "El campo 'Productos Saliente' es obligatorio";
                    campo = "Prod.Sal";
                    txt_nid_proidsal.setDisabled(true);
                } else if (cb_nid_tipdoc.getSelectedIndex() == -1) {
                    validarDetalle = "El campo 'Tipo de Documento' es obligatorio";
                    campo = "Tip. Doc";
                } else if (txt_nid_enteroent.getValue() == null) {
                    validarDetalle = "El producto entrante, el campo 'C ENT. ' es obligatorio";
                    campo = "EC.Ent";
                } else if (txt_nid_fracent.getValue() == null) {
                    validarDetalle = "El producto entrante, el campo 'C FRAC. ' es obligatorio";
                    campo = "EC.Frac";
                } else if (txt_nid_enterosal.getValue() == null) {
                    validarDetalle = "El producto saliente, el campo 'C ENT. ' es obligatorio";
                    campo = "SC.Ent";
                } else if (txt_nid_fracsal.getValue() == null) {
                    validarDetalle = "El producto saliente, el campo 'C FRAC. ' es obligatorio";
                    campo = "SC.Frac";
                } else {
                    Productos objProdEnt, objProdSal;
                    objProdEnt = new DaoProductos().listaPro(txt_nid_proident.getValue());
                    objProdSal = new DaoProductos().listaPro(txt_nid_proidsal.getValue());
                    if (objProdEnt != null && objProdEnt.getPro_presminven() < txt_nid_fracent.getValue()) {
                        validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                        campo = "EC.Frac";
                    } else if (objProdSal != null && objProdSal.getPro_presminven() < txt_nid_fracsal.getValue()) {
                        validarDetalle = "Cant.Frac supera a la unidad de presentacion";
                        campo = "SC.Frac";
                    } else if (txt_nid_enteroent.getValue() == 0 && txt_nid_fracent.getValue() == 0) {
                        validarDetalle = "En el producto entrante, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                    } else if (txt_nid_enterosal.getValue() == 0 && txt_nid_fracsal.getValue() == 0) {
                        validarDetalle = "En el producto saliente, debe ingresar cantidad mayores a cero en el campo 'Cant. Entero' o 'Cant. Fraccion' ";
                    } else if (txt_nid_pretotalsal.getValue() <= 0
                            || txt_nid_pretotalent.getValue() <= 0) {
                        validarDetalle = "El precio no puede ser 0";
                    } else {
                        validarDetalle = "";
                    }
                }
            }
        }
        return validarDetalle;
    }

    public String verificarFechas() {
        String s_valor = "";
        // se tiene que validar que la fecha de emision sea del mismo periodo
        String fecha_emision = sdf.format(d_ni_fecemi.getValue());

        if (d_ni_fecent.getValue().getTime() < d_ni_fecemi.getValue().getTime()) {
            s_valor = "La fecha de Entrega debe ser Mayor o igual que : " + fecha_emision;
        }
        return s_valor;
    }

    public Object generaNotaIntercambioCab() {
        long ni_key = txt_ni_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_ni_id.getValue());
        String cli_id = txt_cli_id.getValue();

        long nic_provid = Long.parseLong(txt_nic_provid.getValue());
        int nic_lpcid = Integer.parseInt(txt_nic_lpcid.getValue());
        long cli_key = Long.parseLong(txt_cli_id.getValue());
        long clidir_id = txt_clidir_id.getValue();
        String nr_zona = txt_zon_id.getValue();
        Date ni_fecemi = d_ni_fecemi.getValue();
        Date ni_fecent = d_ni_fecent.getValue();
        int ni_motcam = Integer.parseInt(txt_ni_motcamid.getValue());
        int ni_sup = Integer.parseInt(txt_sup_id.getValue());
        int ni_vend = Integer.parseInt(txt_ni_venid.getValue());
        int ni_trans = Integer.parseInt(txt_ni_transid.getValue());
        int ni_hor = Integer.parseInt(txt_ni_horid.getValue());
        String ni_usuadd = objUsuarioCredential.getCuenta();
        String ni_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String ni_usumod = objUsuarioCredential.getCuenta();
        String ni_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotaInterCab(ni_key, ni_fecemi, ni_fecent, emp_id, suc_id, cli_key, cli_id, clidir_id, nr_zona, ni_motcam, ni_sup, ni_vend, ni_trans, ni_hor, ni_usuadd, ni_pcadd, ni_usumod, ni_pcmod, nic_provid, nic_lpcid);
    }

    public Object generaNotaInterDetEnt() throws SQLException {
        long prov_key = Long.parseLong(txt_nic_provid.getValue());
        int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());

        objProductoEnt = new DaoProductos().buscarProducto(txt_nid_proident.getValue(), txt_nic_provid.getValue());
        objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, txt_nid_proident.getValue(), prov_key, lpc_key);

        long ni_key = txt_ni_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_ni_id.getValue());
        int nid_tipdoc = cb_nid_tipdoc.getSelectedItem().getValue();
        String nid_tipdocdes = cb_nid_tipdoc.getValue();
        String nid_serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue().toUpperCase();
        String nid_doc = txt_nid_doc.getValue().isEmpty() ? "" : txt_nid_doc.getValue();
        String nid_docref = nid_serie + nid_doc;
        String pro_id = objProductoEnt.getPro_id();
        String pro_des = objProductoEnt.getPro_des();
        String pro_desdes = objProductoEnt.getPro_desdes();
        String pro_uniman = objProductoEnt.getPro_unimanven();
        int pro_presmin = objProductoEnt.getPro_presminven();
        double nid_precio = objPrecios.getPre_valvent();
        String s_calculo = String.valueOf(txt_nid_enteroent.getValue());
        String[] cadena = s_calculo.split("[.]");
        int enteroent = Integer.parseInt(cadena[0]);

        int nid_cantent = txt_nid_enteroent.getValue() == null ? 0 : enteroent;
        int nid_cantfrac = txt_nid_fracent.getValue() == null ? 0 : txt_nid_fracent.getValue();
        int nid_canttot;
        if (pro_uniman.equals("BOL") || pro_uniman.equals("CJA") || pro_uniman.equals("PAQ")) {
            nid_canttot = nid_cantent * pro_presmin + nid_cantfrac;
        } else {
            nid_canttot = nid_cantent + nid_cantfrac;
        }
        String nid_glosa = txt_nid_glosa.getValue().toUpperCase();
        String nid_usuadd = objUsuarioCredential.getCuenta();
        String nid_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nid_usumod = objUsuarioCredential.getCuenta();
        String nid_pcmod = objUsuarioCredential.getComputerName();
        return new NotaInterDet(ni_key, emp_id, suc_id, nid_tipdoc, nid_tipdocdes, nid_serie, nid_doc, nid_docref, nid_glosa, nid_usuadd, nid_pcadd, nid_usumod, nid_pcmod, pro_id, pro_des, pro_desdes, pro_presmin, pro_uniman, nid_cantent, nid_cantfrac, nid_canttot, "E", nid_precio);
    }

    public Object generaNotaInterDetSal() throws SQLException {
        long prov_key = Long.parseLong(txt_nic_provid.getValue());
        int lpc_key = Integer.parseInt(txt_nic_lpcid.getValue());
        objProductoSal = new DaoProductos().buscarProducto(txt_nid_proidsal.getValue(), txt_nic_provid.getValue());
        objPrecios = new DaoPrecios().getPrecioxLpCompra(emp_id, suc_id, txt_nid_proidsal.getValue(), prov_key, lpc_key);
        long ni_key = txt_ni_id.getValue().isEmpty() ? 0 : Long.parseLong(txt_ni_id.getValue());
        int nid_tipdoc = cb_nid_tipdoc.getSelectedItem().getValue();
        String nid_tipdocdes = cb_nid_tipdoc.getValue();
        String nid_serie = txt_nid_serie.getValue().isEmpty() ? "" : txt_nid_serie.getValue().toUpperCase();
        String nid_doc = txt_nid_doc.getValue().isEmpty() ? "" : txt_nid_doc.getValue();
        String nid_docref = nid_serie + nid_doc;
        String pro_id = objProductoSal.getPro_id();
        String pro_des = objProductoSal.getPro_des();
        String pro_desdes = objProductoSal.getPro_desdes();
        String pro_uniman = objProductoSal.getPro_unimanven();
        int pro_presmin = objProductoSal.getPro_presminven();
        double nid_precio = objPrecios.getPre_valvent();
        String s_calculo = String.valueOf(txt_nid_enterosal.getValue());
        String[] cadena = s_calculo.split("[.]");
        int enterosal = Integer.parseInt(cadena[0]);
        int nid_cantent = txt_nid_enterosal.getValue() == null ? 0 : enterosal;
        int nid_cantfrac = txt_nid_fracsal.getValue() == null ? 0 : txt_nid_fracsal.getValue();
        int nid_canttot;
        if (pro_uniman.equals("BOL") || pro_uniman.equals("CJA") || pro_uniman.equals("PAQ")) {
            nid_canttot = nid_cantent * pro_presmin + nid_cantfrac;
        } else {
            nid_canttot = nid_cantent + nid_cantfrac;
        }
        String nid_glosa = txt_nid_glosa.getValue().toUpperCase();
        String nid_usuadd = objUsuarioCredential.getCuenta();
        String nid_pcadd = objUsuarioCredential.getComputerName().toUpperCase();
        String nid_usumod = objUsuarioCredential.getCuenta();
        String nid_pcmod = objUsuarioCredential.getComputerName().toUpperCase();
        return new NotaInterDet(ni_key, emp_id, suc_id, nid_tipdoc, nid_tipdocdes, nid_serie, nid_doc, nid_docref, nid_glosa, nid_usuadd, nid_pcadd, nid_usumod, nid_pcmod, pro_id, pro_des, pro_desdes, pro_presmin, pro_uniman, nid_cantent, nid_cantfrac, nid_canttot, "S", nid_precio);
    }

    public Object generaNotaIntercambioDet(long ni_key, int pos) {

        int nid_tipdoc = objlstNotaInterDet.get(pos).getNid_tipdoc();
        String nid_serie = objlstNotaInterDet.get(pos).getNid_serie();
        String nid_doc = objlstNotaInterDet.get(pos).getNid_doc();
        String nid_docref = objlstNotaInterDet.get(pos).getNid_serie().concat(objlstNotaInterDet.get(pos).getNid_doc());
        long nid_item = objlstNotaInterDet.get(pos).getNid_item();

        // Datos para el Producto de Entrada
        String nid_proident = objlstNotaInterDet.get(pos).getPro_id();
        String nid_prodesent = objlstNotaInterDet.get(pos).getPro_des();
        String nid_prodesdesent = objlstNotaInterDet.get(pos).getPro_desdes();
        String pro_unimanent = objlstNotaInterDet.get(pos).getPro_uniman();
        int pro_presminent = objlstNotaInterDet.get(pos).getPro_presmin();
        int nid_cantente = objlstNotaInterDet.get(pos).getNid_cantent();
        int nid_cantentf = objlstNotaInterDet.get(pos).getNid_cantfrac();
        int nid_cantenttot = objlstNotaInterDet.get(pos).getNid_canttot();

        // Datos para el Producto de salida
        String nid_proidsal = objlstNotaInterDet.get(pos + 1).getPro_id();
        String nid_prodessal = objlstNotaInterDet.get(pos + 1).getPro_des();
        String nid_prodesdessal = objlstNotaInterDet.get(pos + 1).getPro_desdes();
        String pro_unimansal = objlstNotaInterDet.get(pos + 1).getPro_uniman();
        int pro_presminsal = objlstNotaInterDet.get(pos + 1).getPro_presmin();
        int nid_cantsale = objlstNotaInterDet.get(pos + 1).getNid_cantent();
        int nid_cantsalf = objlstNotaInterDet.get(pos + 1).getNid_cantfrac();
        int nid_cantsaltot = objlstNotaInterDet.get(pos + 1).getNid_canttot();

        String nid_glosa = objlstNotaInterDet.get(pos).getNid_glosa();
        String nid_usuadd = objlstNotaInterDet.get(pos).getNid_usuadd();
        String nid_pcadd = objlstNotaInterDet.get(pos).getNid_pcadd();
        String nid_usumod = objlstNotaInterDet.get(pos).getNid_usumod();
        String nid_pcmod = objlstNotaInterDet.get(pos).getNid_usumod();
        return new NotaInterDet(ni_key, emp_id, suc_id, nid_item, nid_tipdoc, nid_serie, nid_doc, nid_docref, nid_proident, nid_cantente, nid_cantentf, nid_cantenttot, nid_proidsal, nid_cantsale, nid_cantsalf, nid_cantsaltot, nid_glosa, nid_usuadd, nid_pcadd, nid_usumod, nid_pcmod, nid_prodessal, nid_prodesdessal, nid_prodesent, nid_prodesdesent, pro_unimansal, pro_unimanent, pro_presminsal, pro_presminent);
    }

    public void llenarCampos() {
        txt_ni_id.setValue(objNotaInterCab.getNi_id());
        d_ni_fecemi.setValue(objNotaInterCab.getNi_fecemi());
        d_ni_fecent.setValue(objNotaInterCab.getNi_fecent());
        txt_ni_motcamid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_motcam()), 3, "0"));
        txt_ni_motcamdes.setValue(objNotaInterCab.getMcam_des());
        txt_cli_id.setValue(objNotaInterCab.getCli_id());
        txt_cli_razsoc.setValue(objNotaInterCab.getCli_razsoc());
        txt_nic_provid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNic_provid()), 8, "0"));
        txt_nic_provdes.setValue(objNotaInterCab.getNic_provrazsoc());
        txt_nic_lpcid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNic_lpcid()), 4, "0"));
        txt_nic_lpcdes.setValue(objNotaInterCab.getNic_lpcdes());
        txt_ni_notaent.setValue(objNotaInterCab.getNi_notaent());
        txt_ni_notasal.setValue(objNotaInterCab.getNi_notasal());
        txt_zon_id.setValue(objNotaInterCab.getNi_zona());
        txt_zon_des.setValue(objNotaInterCab.getZon_des());
        txt_ni_venid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_vend()), 4, "0"));
        txt_sup_id.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_sup()), 4, "0"));
        txt_ni_vennom.setValue(objNotaInterCab.getVen_apenom());
        txt_ni_transid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_trans()), 4, "0"));
        txt_ni_transdes.setValue(objNotaInterCab.getTrans_alias());
        txt_clidir_id.setValue(objNotaInterCab.getClidir_id());
        txt_clidir_direcc.setValue(objNotaInterCab.getClidir_direcc());
        txt_ni_horid.setValue(Utilitarios.lpad(String.valueOf(objNotaInterCab.getNi_hor()), 3, "0"));
        txt_ni_hordes.setValue(objNotaInterCab.getHor_des());
        txt_ni_registro.setValue(objNotaInterCab.getNi_nroreg());
        txt_ni_depurado.setValue(objNotaInterCab.getNi_nrodep());
        txt_usuadd.setValue(objNotaInterCab.getNi_usuadd());
        d_fecadd.setValue(objNotaInterCab.getNi_fecadd());
        txt_usumod.setValue(objNotaInterCab.getNi_usumod());
        d_fecmod.setValue(objNotaInterCab.getNi_fecmod());
    }

    public void limpiarCampos() {
        txt_ni_id.setValue("");
        d_ni_fecemi.setValue(Utilitarios.hoyAsFecha());
        d_ni_fecent.setValue(Utilitarios.hoyAsFecha());
        txt_nic_provid.setValue("");

        txt_nic_provdes.setValue("");
        txt_nic_lpcdes.setValue("");
        txt_nic_lpcid.setValue("");
        txt_ni_motcamid.setValue("");
        txt_ni_motcamdes.setValue("");
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_ni_notaent.setValue("");
        txt_ni_notasal.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_ni_venid.setValue("");
        txt_sup_id.setValue("");
        txt_ni_vennom.setValue("");
        txt_ni_transid.setValue("");
        txt_ni_transdes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
        txt_ni_horid.setValue("");
        txt_ni_hordes.setValue("");
        txt_ni_registro.setValue("");
        txt_usuadd.setValue("");
        d_fecadd.setValue(null);
        txt_usumod.setValue("");
        d_fecmod.setValue(null);
    }

    public void limpiarProvLp() {
        txt_nic_provid.setValue("");
        txt_nic_provdes.setValue("");
        txt_nic_lpcid.setValue("");
        txt_nic_lpcdes.setValue("");
    }

    public void limpiarCliDirVacio() {
        txt_cli_id.setValue("");
        txt_cli_razsoc.setValue("");
        txt_zon_id.setValue("");
        txt_zon_des.setValue("");
        txt_ni_venid.setValue("");
        txt_sup_id.setValue("");
        txt_ni_vennom.setValue("");
        txt_ni_transid.setValue("");
        txt_ni_transdes.setValue("");
        txt_ni_horid.setValue("");
        txt_ni_hordes.setValue("");
        txt_clidir_id.setValue(null);
        txt_clidir_direcc.setValue("");
    }

    public void limpiarCamposDetalle() {
        txt_nid_doc.setValue("");
        txt_nid_serie.setValue("");
        txt_nid_prodessal.setValue("");
        txt_nid_proidsal.setValue("");
        txt_nid_enterosal.setValue(null);
        txt_nid_fracsal.setValue(null);
        txt_nid_prodesent.setValue("");
        txt_nid_proident.setValue("");
        txt_nid_preident.setValue(null);
        txt_nid_preidsal.setValue(null);
        txt_nid_enteroent.setValue(null);
        txt_nid_fracent.setValue(null);
        txt_nid_glosa.setValue("");
        txt_nid_upresal.setValue(null);
        txt_nid_upreent.setValue(null);
        txt_nid_pretotalent.setValue(null);
        txt_nid_pretotalsal.setValue(null);
        cb_nid_tipdoc.setSelectedIndex(-1);
    }

    public void limpiarCamposDetalleE() {
        txt_nid_proident.setValue("");
        txt_nid_prodesent.setValue("");
        txt_nid_preident.setValue(null);
        txt_nid_enteroent.setValue(null);
        txt_nid_fracent.setValue(null);
        txt_nid_pretotalent.setValue(null);
        txt_nid_upreent.setValue(null);
    }

    public void limpiarCamposDetalleS() {
        txt_nid_prodessal.setValue("");
        txt_nid_proidsal.setValue("");
        txt_nid_preidsal.setValue(null);
        txt_nid_enterosal.setValue(null);
        txt_nid_fracsal.setValue(null);
        txt_nid_upresal.setValue(null);
        txt_nid_pretotalsal.setValue(null);
    }

    public void habilitaCampos(boolean b_valida) {
        d_ni_fecemi.setDisabled(b_valida);
        d_ni_fecent.setDisabled(b_valida);
        txt_ni_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_zon_id.setDisabled(b_valida);
        txt_ni_venid.setDisabled(b_valida);
        txt_ni_transid.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
        txt_nic_provid.setDisabled(b_valida);
        txt_nic_lpcid.setDisabled(b_valida);
        txt_ni_horid.setDisabled(b_valida);
    }

    public void habilitaCamposNI(boolean b_valida) {
        d_ni_fecemi.setDisabled(b_valida);
        d_ni_fecent.setDisabled(b_valida);
        txt_ni_motcamid.setDisabled(b_valida);
        txt_cli_id.setDisabled(b_valida);
        txt_clidir_id.setDisabled(b_valida);
        txt_nic_provid.setDisabled(b_valida);
        txt_nic_lpcid.setDisabled(b_valida);
    }

    public void habilitaCamposDetalle(boolean b_valida1, boolean b_valida2, boolean b_valida3) {
        txt_nid_doc.setDisabled(b_valida3);
        txt_nid_serie.setDisabled(b_valida3);
        txt_nid_proident.setDisabled(b_valida1);
        txt_nid_proidsal.setDisabled(b_valida2);
        cb_nid_tipdoc.setDisabled(b_valida3);
        txt_nid_glosa.setDisabled(b_valida3);
        txt_nid_enteroent.setDisabled(b_valida1);
        txt_nid_fracent.setDisabled(b_valida1);
        txt_nid_enterosal.setDisabled(b_valida2);
        txt_nid_fracsal.setDisabled(b_valida2);
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
        tbbtn_btn_nuevonotinter.setDisabled(b_valida1);
        tbbtn_btn_editarnotinter.setDisabled(b_valida1);
        tbbtn_btn_eliminarnotinter.setDisabled(b_valida1);
        tbbtn_btn_deshacernotinter.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotinter.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanotinter.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void llenarCamposDetalle() throws SQLException {
        objlstNotaInterDet = null;
        long nr_key = objNotaInterCab.getNi_key();
        objlstNotaInterDet = objDaoNotaInter.listaNotaInterDet(emp_id, suc_id, nr_key);
        lst_notinterdet.setModel(objlstNotaInterDet);
    }

    private void llenarCamposProducto(NotaInterDet objNotaInterDet) {
        if (lst_notinterdet.getSelectedIndex() % 2 == 0) {
            txt_nid_proident.setValue(objNotaInterDet.getPro_id());
            txt_nid_prodesent.setValue(objNotaInterDet.getPro_des());
            txt_nid_preident.setValue(objNotaInterDet.getNid_precio());
            txt_nid_upreent.setValue(objNotaInterDet.getPro_presmin());

            txt_nid_enteroent.setValue(objNotaInterDet.getNid_cantent());
            txt_nid_fracent.setValue(objNotaInterDet.getNid_cantfrac());
            txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));

            // Producto Saliente
            for (int i = 0; i < objlstNotaInterDet.getSize(); i++) {
                if (("S").equals(objlstNotaInterDet.get(i).getNid_indicador().toString()) && objlstNotaInterDet.get(i).getNid_docref().equals(objNotaInterDet.getNid_serie() + objNotaInterDet.getNid_doc().toString())) {
                    txt_nid_proidsal.setValue(objlstNotaInterDet.get(i).getPro_id());
                    txt_nid_prodessal.setValue(objlstNotaInterDet.get(i).getPro_des());
                    txt_nid_enterosal.setValue(objlstNotaInterDet.get(i).getNid_cantent());
                    txt_nid_fracsal.setValue(objlstNotaInterDet.get(i).getNid_cantfrac());
                    txt_nid_preidsal.setValue(objlstNotaInterDet.get(i).getNid_precio());
                    txt_nid_upresal.setValue(objlstNotaInterDet.get(i).getPro_presmin());
                    txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));

                }
            }
        } else {
            txt_nid_proidsal.setValue(objNotaInterDet.getPro_id());
            txt_nid_prodessal.setValue(objNotaInterDet.getPro_des());
            txt_nid_preidsal.setValue(objNotaInterDet.getNid_precio());
            txt_nid_upresal.setValue(objNotaInterDet.getPro_presmin());

            txt_nid_enterosal.setValue(objNotaInterDet.getNid_cantent());
            txt_nid_fracsal.setValue(objNotaInterDet.getNid_cantfrac());
            txt_nid_pretotalsal.setValue((txt_nid_enterosal.getValue() * txt_nid_preidsal.getValue()) + ((txt_nid_preidsal.getValue() / txt_nid_upresal.getValue()) * txt_nid_fracsal.getValue()));

            // Producto Entrante
            for (int i = 0; i < objlstNotaInterDet.getSize(); i++) {
                if (("E").equals(objlstNotaInterDet.get(i).getNid_indicador().toString()) && objlstNotaInterDet.get(i).getNid_docref().equals(objNotaInterDet.getNid_serie() + objNotaInterDet.getNid_doc().toString())) {
                    txt_nid_proident.setValue(objlstNotaInterDet.get(i).getPro_id());
                    txt_nid_prodesent.setValue(objlstNotaInterDet.get(i).getPro_des());
                    txt_nid_enteroent.setValue(objlstNotaInterDet.get(i).getNid_cantent());
                    txt_nid_fracent.setValue(objlstNotaInterDet.get(i).getNid_cantfrac());
                    txt_nid_preident.setValue(objlstNotaInterDet.get(i).getNid_precio());
                    txt_nid_upreent.setValue(objlstNotaInterDet.get(i).getPro_presmin());
                    txt_nid_pretotalent.setValue((txt_nid_enteroent.getValue() * txt_nid_preident.getValue()) + ((txt_nid_preident.getValue() / txt_nid_upreent.getValue()) * txt_nid_fracent.getValue()));
                }
            }
        }

        String tipo_doc = String.valueOf(objNotaInterDet.getNid_tipdoc());
        if (tipo_doc != null || !tipo_doc.isEmpty()) {
            cb_nid_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nid_tipdoc, tipo_doc));
        } else {
            cb_nid_tipdoc.setSelectedIndex(-1);
        }

        txt_nid_doc.setValue(objNotaInterDet.getNid_doc().isEmpty() ? "" : objNotaInterDet.getNid_doc());
        txt_nid_serie.setValue(objNotaInterDet.getNid_serie().isEmpty() ? "" : objNotaInterDet.getNid_serie());
        txt_nid_glosa.setValue(objNotaInterDet.getNid_glosa());
    }

    public void limpiarTabLista(int i) {

        // Fechas Inicial y Final vacias
        if (d_fecini.getValue() == null || d_fecfin.getValue() == null) {
            d_fecini.setValue(new Date());
            d_fecfin.setValue(new Date());
        }
        if (i == 1) {
            objlstNotaInterCab = null;
            objlstNotaInterCab = new ListModelList<NotaInterCab>();
            lst_notintercab.setModel(objlstNotaInterCab);
        } else if (i == 2) {
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
        } else {
            txt_venid.setValue("");
            txt_vennom.setValue("");
            cb_situacion.setSelectedIndex(-1);
            objlstNotaInterCab = null;
            objlstNotaInterCab = new ListModelList<NotaInterCab>();
            lst_notintercab.setModel(objlstNotaInterCab);
        }
    }

    public void limpiarListaDet() {
        objlstNotaInterDet = null;
        objlstNotaInterDet = new ListModelList<NotaInterDet>();
        lst_notinterdet.setModel(objlstNotaInterDet);

    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

}
