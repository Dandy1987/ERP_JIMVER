package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Ubicaciones;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.logistica.procesos.NotaESDet;
import org.me.gj.model.logistica.procesos.OrdCompDet;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerCosteoMan extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_costeonotes;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoes, tbbtn_btn_editares, tbbtn_btn_eliminares, tbbtn_btn_guardares, tbbtn_btn_deshaceres;
    @Wire
    Listbox lst_notes, lst_notes_det;
    @Wire
    Textbox txt_nestipnotaes, txt_ntipnotaes, txt_usuadd, txt_usumod,
            txt_nescab_provid, txt_nescab_provrazsoc, txt_nescab_glosa, txt_nescab_tipnotaes, txt_nomtipnotaes,
            txt_nescab_id, txt_nescab_nroserie, txt_nescab_nrodoc, txt_proid, txt_prodes, txt_stock, txt_nescab_ocnropedkey, txt_listapreid, txt_listapredes,
            txt_nescab_cliid, txt_nescab_clinom, txt_nescab_nrodep, txt_nescab_nroped, txt_provid,
            txt_proveedor, txt_prodesdes, txt_ubicod, txt_ubides, txt_usuadd_det, txt_usumod_det, txt_proconv;
    @Wire
    Datebox d_nescab_fecha, d_fecemi, txt_fecmod_det, txt_fecadd_det, d_fecadd, d_fecmod;
    @Wire
    Intbox txt_prokey, txt_undpre, txt_unimas;
    @Wire
    Timebox d_nescab_hora;
    @Wire
    Checkbox chk_despacho, chk_estado;
    @Wire
    Doublebox txt_nescab_tcamb, txt_canent, txt_canfra,
            txt_pimpto, txt_vimpto, txt_valafe, txt_valina, txt_pvta;
    @Wire
    Combobox cb_periodo, cb_situacion, cb_notaes, cb_serie, cb_moneda, cb_nescab_almori, cb_nescab_almdes, cb_nescab_tipdoc;
    @Wire
    Button btn_consultar, btn_nescab_ocnropedkey, btn_despacho;
    //Instancias de Objetos    
    ListModelList<OrdCompDet> objlstOrdCompDet = new ListModelList<OrdCompDet>();
    ListModelList<ManPer> objListModelListManPer;
    ListModelList<NotaESCab> objlstNotaESCab;
    ListModelList<Guias> objlstGuias;
    ListModelList<Almacenes> objlstAlmacenOrigen;
    ListModelList<Almacenes> objlstAlmacenDestino;
    ListModelList<Moneda> objlstMonedas;
    ListModelList<TipDoc> objlstTipDoc;
    ListModelList<Numeracion> objlstNumeracion;
    ListModelList<NotaESDet> objlstNotaESDet;
    ListModelList<Productos> objlstproducto;
    Accesos objAccesos = new Accesos();
    Guias objGuias = new Guias();
    NotaESCab objNotaESCab = new NotaESCab();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoManPer objDaoManPer = new DaoManPer();
    DaoCosteo objDaoCosteo = new DaoCosteo();
    DaoUbicaciones objdaoubicaciones = new DaoUbicaciones();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    DaoAlmacenes objDaoAlmacen = new DaoAlmacenes();
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    DaoMoneda objDaoMonedas = new DaoMoneda();
    DaoNumeracion objDaoNumeracion = new DaoNumeracion();
    DaoNotaES objDaoNotaES;
    DaoOrdCom objDaoOrdComp;
    DaoProductos objDaoProducto;
    NotaESDet objNotaESDet = new NotaESDet();
    ManPer objManPer = new ManPer();
    Ubicaciones objUbicaciones;
    Almacenes objAlmacenes = new Almacenes();
    Moneda objMonedas = new Moneda();
    TipDoc objTipDoc = new TipDoc();
    Numeracion objNumeracion = new Numeracion();
    OrdCompDet objOrdCompDet = new OrdCompDet();
    OrdCompDet objlstNotaES;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    String s_estado = "Q";
    String s_estadoDetalle = "Q";
    String s_mensaje = "";
    String modoEjecucion;
    int i_sel;
    int valor;
    long nrooc;
    int i_selCab;
    int i_empid;
    int i_sucid;
    String foco = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCosteoMan.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoNotaES = new DaoNotaES();
        objDaoOrdComp = new DaoOrdCom();
        objlstNotaES = new OrdCompDet();
    }

    @Listen("onCreate=#tabbox_notes")
    public void llenaRegistros() throws SQLException {
        //carga periodos y selecciona el actual
        objListModelListManPer = new ListModelList<ManPer>();
        //objListModelListManPer = objDaoManPer.listaPeriodos(1);
        objListModelListManPer = objDaoManPer.listaPeriodosActual(1, 11); 
        cb_periodo.setModel(objListModelListManPer);
        objListModelListManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        txt_nestipnotaes.focus();
        cb_periodo.setValue(Utilitarios.periodoActual());
        //carga serie
        objlstNumeracion = new ListModelList<Numeracion>();
        objlstNumeracion = objDaoNumeracion.listaSeriesSinFiltro();
        cb_serie.setModel(objlstNumeracion);
        //Cargamos Mantenimiento
        //carga almacen/
        objlstAlmacenOrigen = new ListModelList<Almacenes>();
        objlstAlmacenOrigen = objDaoAlmacen.lstAlmacenes(1);
        cb_nescab_almori.setModel(objlstAlmacenOrigen);
        objlstAlmacenDestino = new ListModelList<Almacenes>();
        objlstAlmacenDestino = objDaoAlmacen.lstAlmacenes(1);
        cb_nescab_almdes.setModel(objlstAlmacenDestino);
        //carga monedas
        objlstMonedas = new ListModelList<Moneda>();
        objlstMonedas = objDaoMonedas.listaMonedas(1);
        cb_moneda.setModel(objlstMonedas);
        //carga tipo de documento
        objlstTipDoc = new ListModelList<TipDoc>();
        objlstTipDoc = objDaoTipDoc.listaTipDoc(1);
        cb_nescab_tipdoc.setModel(objlstTipDoc);
        cb_nescab_tipdoc.setValue("FACTURA");
        //consulta inicial
        /*objlstNotaESCab = objDaoNotaES.listaNotaESCab();*/
        objlstNotaESCab = objDaoCosteo.BusquedaNotaES(cb_periodo.getValue(), sdf.format(Utilitarios.hoyAsFecha()), "", 1, "");
        lst_notes.setModel(objlstNotaESCab);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10211020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Costeo de Nota E/S Manual con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Costeo de Nota E/S Manual con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de Costeo una Nota de E/S Manual");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de Costeo una Nota de E/S Manual");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_nestipnotaes.getValue().toUpperCase().trim();
        String s_periodo = cb_periodo.getValue();
        String fecha = d_fecemi.getValue() == null ? "" : sdf.format(d_fecemi.getValue());
        String s_situacion;
        s_situacion = cb_situacion.getSelectedIndex() == -1 ? "" : cb_situacion.getSelectedItem().getValue().toString();
        objlstNotaESCab = new ListModelList<NotaESCab>();
        objlstNotaESCab = objDaoCosteo.BusquedaNotaES(s_periodo, fecha, s_consulta, 1, s_situacion);
        if (objlstNotaESCab.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstNotaESCab.getSize() + " registro(s)");
        }
        if (objlstNotaESCab.getSize() > 0) {
            lst_notes.setModel(objlstNotaESCab);
        } else {
            objlstNotaESCab = null;
            lst_notes.setModel(objlstNotaESCab);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        objlstNotaESDet = null;
        lst_notes_det.setModel(objlstNotaESDet);
        limpiarCamposDetalle();
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_notes")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCampos();
        limpiarCamposDetalle();
        objNotaESCab = (NotaESCab) lst_notes.getSelectedItem().getValue();
        if (objNotaESCab == null) {
            objNotaESCab = objlstNotaESCab.get(i_sel + 1);
        }
        i_sel = lst_notes.getSelectedIndex();
        //cargamos las listas con los contactos y telefonos de los proveedores
        objlstNotaESDet = objDaoNotaES.listaNotaESDet(objNotaESCab.getNescab_id(), 1);
        lst_notes_det.setModel(objlstNotaESDet);
        //cargamos los campos
        llenarCampos(objNotaESCab);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objNotaESCab.getNescab_id());
    }

    @Listen("onSelect=#lst_notes_det")
    public void seleccionaRegistroDetalle() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCamposDetalle();
        objNotaESDet = (NotaESDet) lst_notes_det.getSelectedItem().getValue();
        if (objNotaESDet == null) {
            objNotaESDet = objlstNotaESDet.get(i_sel + 1);
        }
        i_sel = lst_notes_det.getSelectedIndex();
        //cargamos los campos
        llenarCamposDetalle(objNotaESDet);
        habilitaCamposDetalle(true);

        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con glosa " + objOrdCompDet.getOcd_glosa());
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_notes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una nota E/S", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if ("ANULADO".equals(objNotaESCab.getEstado()) || objNotaESCab.getNescab_despacho() == 1) {
                Messagebox.show("La nota E/S ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if ("010".equals(objNotaESCab.getNescab_tipnotaes()) || "011".equals(objNotaESCab.getNescab_tipnotaes()) || "012".equals(objNotaESCab.getNescab_tipnotaes()) || "013".equals(objNotaESCab.getNescab_tipnotaes())
                    || "014".equals(objNotaESCab.getNescab_tipnotaes()) || "018".equals(objNotaESCab.getNescab_tipnotaes()) || "019".equals(objNotaESCab.getNescab_tipnotaes()) || "020".equals(objNotaESCab.getNescab_tipnotaes())) {
                Messagebox.show("La nota E/S ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaBotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                lst_notes_det.setSelectedIndex(-1);
                limpiarCamposDetalle();
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_editares")
    public void botonEditarDetalle() throws SQLException {
        if (lst_notes_det.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Utilitarios.deshabilitarLista(true, lst_notes_det);
            habilitaBotonesDetalle(true, false);
            habilitaCamposDetalle(false);
            s_estadoDetalle = "E";
            txt_valafe.focus();
            txt_valina.focus();
        }
        d_nescab_hora.setDisabled(true);
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (s_estadoDetalle.equals("E") && tbbtn_btn_editares.isDisabled()) {
            Messagebox.show("El detalle se encuentra en edición");
        } else {
            if (lst_notes_det.getItemCount() <= 0) {
                Messagebox.show("Debe ingresar algun producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParamDetalle = new ParametrosSalida();
                                    //OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                    if (objlstNotaESDet != null) {
                                        int i = 0;
                                        while (i < objlstNotaESDet.getSize()) {
                                            String indicador = objlstNotaESDet.get(i).getInd_accion();
                                            if (indicador.equals("M")) {
                                                objParamDetalle = objDaoNotaES.modificarDetalle(objlstNotaESDet.get(i));
                                            }
                                            i++;
                                        }
                                    }
                                }
                                tab_lista.setSelected(true);
                                tab_lista.setDisabled(false);
                                tab_mantenimiento.setSelected(false);
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                limpiarCampos();
                                limpiaAuditoria();
                                limpiarCamposDetalle();
                                habilitaCampos(true);
                                habilitaBotonesDetalle(true, true);
                                //limpiamos las listas
                                objlstNotaESCab = new ListModelList<NotaESCab>();
                                objlstNotaESDet = new ListModelList<NotaESDet>();
                                lst_notes.setModel(objlstNotaESCab);
                                lst_notes_det.setModel(objlstNotaESDet);
                                //cargamos la lista de facturas proveedor
                                //objlstNotaESCab = objDaoNotaES.listaNotaESCab();
                                objlstNotaESCab = objDaoCosteo.BusquedaNotaES(cb_periodo.getValue(), sdf.format(Utilitarios.hoyAsFecha()), "", 1, "");
                                lst_notes.setModel(objlstNotaESCab);
                                lst_notes.setSelectedIndex(-1);
                                s_estadoDetalle = "Q";
                                s_estado = "Q";
                            }
                        }
                );
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardares")
    public void botonGuardarDetalle() throws SQLException {
        if (objlstNotaES != null) {
            String validaValores = verificarDetalle();
            if (!validaValores.isEmpty()) {
                Messagebox.show(validaValores, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    if (foco.equals("inafecto")) {
                                        txt_valina.focus();
                                    } else if (foco.equals("afecto")) {
                                        txt_valafe.focus();
                                    }
                                }
                            }
                        });
            } else {//si es edicion de nota E/S
                long item = objNotaESDet.getNesdet_item();
                objNotaESDet = (NotaESDet) generaRegistroDetalle();
                objNotaESDet.setInd_accion("M");
                objNotaESDet.setNesdet_item(item);

                int lst_ord = lst_notes_det.getSelectedIndex();
                objlstNotaESDet.set(lst_ord, objNotaESDet);

            }
            limpiarCamposDetalle();
            habilitaBotonesDetalle(false, true);
            habilitaCamposDetalle(true);
            habilitaCampos(false);
            txt_valafe.setDisabled(true);
            txt_valina.setDisabled(true);
            lst_notes_det.setSelectedIndex(-1);
            Utilitarios.deshabilitarLista(false, lst_notes_det);
        } else {
            Messagebox.show("No hay informacion para editar en la lista");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            limpiarCamposDetalle();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaBotonesDetalle(true, true);
                            s_estado = "Q";
                            //limpiamos la lista
                            objlstOrdCompDet = null;
                            lst_notes_det.setModel(objlstOrdCompDet);
                            llenaRegistros();
                            lst_notes.setSelectedIndex(-1);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_deshaceres")
    public void botonDeshacerDetalle() throws SQLException {
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                    habilitaBotonesDetalle(false, true);
                    limpiarCamposDetalle();
                    habilitaCamposDetalle(true);
                    txt_valafe.setDisabled(true);
                    txt_valina.setDisabled(true);
                    Utilitarios.deshabilitarLista(false, lst_notes_det);
                    lst_notes_det.setSelectedIndex(-1);
                    s_estadoDetalle = "Q";
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                }
                d_nescab_hora.setDisabled(true);
            }
        });
    }

    //Eventos Secundarios - Validaciones 
    @Listen("onOK=#txt_nestipnotaes")
    public void lov_notaes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nestipnotaes.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "CosteoMan";
                mapeo.put("txt_nescab_tipnotaes", txt_nestipnotaes);
                mapeo.put("txt_nomtipnotaes", txt_ntipnotaes);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerCosteoMan");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNotaEs.zul", null, mapeo);
                window.doModal();
            } else {
                btn_consultar.focus();
            }
        }
    }

    @Listen("onBlur=#txt_nestipnotaes")
    public void valida_tipnotaes() throws SQLException {
        if (!txt_nestipnotaes.getValue().isEmpty()) {
            if (!txt_nestipnotaes.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nestipnotaes.setValue("");
                                    txt_ntipnotaes.setValue("");
                                    txt_nestipnotaes.focus();
                                }
                            }
                        });
            } else {
                txt_nestipnotaes.setValue(Utilitarios.lpad(txt_nestipnotaes.getValue(), 3, "0"));
                Guias objGuia = new DaoManNotaES().BusquedaGuia(txt_nestipnotaes.getValue());
                if (objGuia.getCodigo() == null) {
                    Messagebox.show("El código de la nota de E/S no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_nestipnotaes.setValue("");
                                        txt_ntipnotaes.setValue("");
                                        txt_nestipnotaes.focus();
                                    }
                                }
                            });
                } else {
                    txt_nestipnotaes.setValue(objGuia.getCodigo());
                    txt_ntipnotaes.setValue(objGuia.getDesGui());
                }
            }
        } else {
            txt_ntipnotaes.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_valafe")
    public void onOKValorAfecto() throws SQLException {
        botonGuardarDetalle();
    }

    @Listen("onOK=#txt_valina")
    public void onOKValorInafecto() throws SQLException {
        botonGuardarDetalle();
    }

    //Eventos Otros
    public Object generaRegistroDetalle() {
        String nescab_id = txt_nescab_id.getValue();
        String nescab_key = (String) (txt_nescab_id.getValue().isEmpty() ? "" : txt_nescab_id.getValue().substring(3, 10));
        String nescab_tipmov;
        String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
        String gdet_ubiori;
        String gdet_ubides;
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();
        if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
            nescab_tipmov = "E";
            gdet_ubiori = "";
            gdet_ubides = txt_ubicod.getValue();
        } else {
            nescab_tipmov = "S";
            gdet_ubiori = txt_ubicod.getValue();
            gdet_ubides = "";
        }
        String prov_id = txt_provid.getValue();
        String prov_proveedor = txt_proveedor.getValue();
        String pro_id = txt_proid.getValue();
        String pro_des = txt_prodes.getValue();
        String pro_desdes = txt_prodesdes.getValue();
        long pro_key = Long.parseLong(txt_proid.getValue());
        int gdet_undpre = txt_undpre.getValue();
        double gdet_cant = txt_canent.getValue() * gdet_undpre;
        double gdet_frac = txt_canfra.getValue();
        gdet_cant = gdet_cant + gdet_frac;
        double gdetcant_cal = gdet_cant / gdet_undpre;
        String gdet_proconv = txt_proconv.getValue();
        String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
        nrooc = Long.parseLong(ordcompra);
        long gdet_cantconv;
        gdet_cantconv = gdet_proconv.equals("*") ? (long) (gdetcant_cal / txt_unimas.getValue()) : (long) (txt_unimas.getValue() * gdetcant_cal);
        double gdet_valafe, gdet_pimpto, gdet_vimpto, gdet_valina, gdet_pvta;
        gdet_valafe = txt_valafe.getValue() == null ? 0 : txt_valafe.getValue();
        gdet_valina = txt_valina.getValue() == null ? 0 : txt_valina.getValue();
        gdet_pimpto = txt_pimpto.getValue() == null ? 0 : txt_pimpto.getValue();
        if (gdet_pimpto > 0) {
            gdet_vimpto = Utilitarios.formatoDecimal(txt_valafe.getValue() * (txt_pimpto.getValue() / 100), 4);
            gdet_pvta = gdet_valafe + gdet_vimpto;
        } else {
            gdet_vimpto = 0;
            gdet_pvta = gdet_valina + gdet_vimpto;
        }
        int gdet_est = 1;
        String gdet_almori = cb_nescab_almori.getSelectedItem().getValue();
        String gdet_almdes = cb_nescab_almdes.getSelectedItem().getValue();
        double gdet_cantfac = 0;
        double gdet_peso = 0;
        double gdet_cositem = 0;
        String gdet_usuadd = objUsuCredential.getCuenta();
        String gdet_usumod = objUsuCredential.getCuenta();
        return new NotaESDet(nescab_id, nescab_key, nescab_tipnotaes, emp_id, suc_id, gdet_almori, gdet_almdes, nescab_tipmov, prov_id, prov_proveedor,
                pro_id, pro_des, pro_desdes, pro_key, gdet_ubiori, gdet_ubides, gdet_cant, gdet_undpre, gdet_cantconv, gdet_proconv, gdet_pimpto, gdet_vimpto,
                gdet_valafe, gdet_valina, gdet_pvta, gdet_est, gdet_cantfac,
                gdet_peso, gdet_cositem, gdet_usuadd, gdet_usumod);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        /*tbbtn_btn_nuevoes.setDisabled(b_valida1);*/
        tbbtn_btn_editares.setDisabled(b_valida1);
        /*tbbtn_btn_eliminares.setDisabled(b_valida1);*/
        tbbtn_btn_deshaceres.setDisabled(b_valida2);
        tbbtn_btn_guardares.setDisabled(b_valida2);
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        double impto;
        impto = txt_pimpto.getValue() == null ? 0 : txt_pimpto.getValue().doubleValue();
        if (impto > 0) {
            txt_valafe.setDisabled(b_valida1);
        } else {
            txt_valina.setDisabled(b_valida1);
        }
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida1) {
        /*cb_moneda.setDisabled(b_valida1);
         d_nescab_fecha.setDisabled(b_valida1);
         d_nescab_hora.setDisabled(b_valida1);
         cb_serie.setDisabled(b_valida1);
         txt_nescab_tipnotaes.setDisabled(b_valida1);
         btn_nescab_ocnropedkey.setDisabled(b_valida1);
         cb_nescab_tipdoc.setDisabled(b_valida1);
         txt_nescab_nrodoc.setDisabled(b_valida1);
         txt_nescab_provid.setDisabled(b_valida1);
         txt_nescab_cliid.setDisabled(b_valida1);
         txt_nescab_glosa.setDisabled(b_valida1);
         cb_nescab_almdes.setDisabled(b_valida1);*/
    }

    public void limpiarCampos() {
        txt_nescab_id.setValue(null);
        cb_moneda.setSelectedIndex(-1);
        txt_nescab_tcamb.setValue(0);
        d_nescab_fecha.setValue(null);
        d_nescab_hora.setValue(null);
        txt_nescab_tipnotaes.setValue("");
        txt_nomtipnotaes.setValue("");
        cb_serie.setSelectedIndex(-1);
        txt_nescab_ocnropedkey.setValue("");
        cb_nescab_tipdoc.setSelectedIndex(-1);
        txt_nescab_nroserie.setValue("");
        txt_nescab_nrodoc.setValue("");
        txt_nescab_provid.setValue("");
        txt_nescab_provrazsoc.setValue("");
        txt_nescab_cliid.setValue("");
        txt_nescab_clinom.setValue("");
        txt_nescab_glosa.setValue("");
        txt_nescab_nroped.setValue("");
        txt_nescab_nrodep.setValue("");
        cb_nescab_almori.setSelectedIndex(-1);
        cb_nescab_almdes.setSelectedIndex(-1);
        //limpiamos la lista
        objlstOrdCompDet = null;
        lst_notes_det.setModel(objlstOrdCompDet);
    }

    public void limpiarCamposDetalle() {
        txt_proid.setValue("");
        txt_prokey.setValue(null);
        txt_prodes.setValue("");
        txt_prodesdes.setValue("");
        txt_ubides.setValue("");
        txt_canent.setValue(null);
        txt_canfra.setValue(null);
        txt_pimpto.setValue(null);
        txt_vimpto.setValue(null);
        txt_valafe.setValue(null);
        txt_valina.setValue(null);
        txt_pvta.setValue(null);
        txt_proconv.setValue("");
        txt_provid.setValue("");
        txt_proveedor.setValue("");
        txt_ubicod.setValue("");
        txt_ubides.setValue("");
        txt_stock.setValue("");
        txt_fecadd_det.setValue(null);
        txt_fecmod_det.setValue(null);
        txt_usuadd_det.setValue("");
        txt_usumod_det.setValue("");
    }

    public void llenarCampos(NotaESCab objGCab) throws SQLException {
        txt_nescab_id.setValue(objGCab.getNescab_id());
        txt_nescab_tcamb.setValue(objGCab.getNescab_tcamb());
        d_nescab_fecha.setValue(new Timestamp(objGCab.getNescab_fecha().getTime()));
        //d_nescab_hora.setValue(objGCab.getHora());
        d_nescab_hora.setValue(new Timestamp(objGCab.getNescab_fecadd().getTime()));
        int moneda = objGCab.getNescab_moneda();
        if (moneda > 0) {
            cb_moneda.setSelectedItem(Utilitarios.valorPorTexto1(cb_moneda, objGCab.getNescab_moneda()));
        } else {
            cb_moneda.setSelectedIndex(-1);
        }
        txt_nescab_tipnotaes.setValue(objGCab.getNescab_tipnotaes());
        txt_nomtipnotaes.setValue(objGCab.getNotaes());
        cb_serie.setSelectedItem(Utilitarios.textoPorTexto(cb_serie, objGCab.getNescab_serie()));
        if (txt_nescab_tipnotaes.getValue().equals("001")) {
            txt_nescab_ocnropedkey.setValue(Utilitarios.lpad(String.valueOf(objGCab.getNescab_ocnropedkey()), 10, "0"));
        } else {
            txt_nescab_ocnropedkey.setValue("");
        }
        String tipo_doc = objGCab.getNescab_tipdoc();
        if (!tipo_doc.isEmpty()) {
            cb_nescab_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nescab_tipdoc, objGCab.getNescab_tipdoc()));
        } else {
            cb_nescab_tipdoc.setSelectedIndex(-1);
        }
        txt_nescab_nroserie.setValue(objGCab.getNescab_nroserie());
        txt_nescab_nrodoc.setValue(objGCab.getNescab_nrodoc());
        txt_nescab_provid.setValue(objGCab.getNescab_provid());
        txt_nescab_provrazsoc.setValue(objGCab.getNescab_provrazsoc());
        txt_nescab_cliid.setValue(objGCab.getNescab_cliid());
        txt_nescab_clinom.setValue(objGCab.getNescab_clinom());
        txt_nescab_glosa.setValue(objGCab.getNescab_glosa());
        txt_nescab_nrodep.setValue(objGCab.getNescab_nrodep());
        cb_nescab_almori.setSelectedItem(Utilitarios.textoPorTexto(cb_nescab_almori, objGCab.getNescab_almori()));
        cb_nescab_almdes.setSelectedItem(Utilitarios.textoPorTexto(cb_nescab_almdes, objGCab.getNescab_almdes()));
        txt_usuadd.setValue(objGCab.getNescab_usuadd());
        d_fecadd.setValue(objGCab.getNescab_fecadd());
        txt_usumod.setValue(objGCab.getNescab_usumod());
        d_fecmod.setValue(objGCab.getNescab_fecmod());
    }

    public void llenarCamposDetalle(NotaESDet objNotaESDetalle) throws SQLException {
        Productos objProductos;
        txt_proid.setValue(objNotaESDetalle.getPro_id());
        txt_prodes.setValue(objNotaESDetalle.getPro_des());
        txt_prodesdes.setValue(objNotaESDetalle.getPro_desdes());
        txt_undpre.setValue(objNotaESDetalle.getNesdet_undpre());
        //txt_canfra.setValue(Math.round(objNotaESDetalle.getNesdet_cant() * objNotaESDetalle.getNesdet_undpre()) % objNotaESDetalle.getNesdet_undpre());
        txt_canfra.setValue(Math.round(objNotaESDetalle.getNesdet_cant() % objNotaESDetalle.getNesdet_undpre()));
        if (txt_undpre.getValue() / 2.0 <= txt_canfra.getValue()) {
            txt_canent.setValue(Math.round(objNotaESDetalle.getNesdet_cant() / objNotaESDetalle.getNesdet_undpre()) - 1);
        } else {
            //txt_canent.setValue(Math.round(objNotaESDetalle.getNesdet_cant()));
            txt_canent.setValue(Math.round(objNotaESDetalle.getNesdet_cant() / objNotaESDetalle.getNesdet_undpre()));
        }
        objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
        objParametrosSalida = new ParametrosSalida();
        String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
        int almacen;
        String codUbi;
        if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
            almacen = Integer.parseInt(cb_nescab_almdes.getSelectedItem().getValue().toString());
            codUbi = objNotaESDetalle.getNesdet_ubides();
            txt_ubicod.setValue(codUbi);
            txt_ubides.setValue(objdaoubicaciones.descripcionUbicacion(objNotaESDetalle.getNesdet_ubides()));
        } else {
            almacen = Integer.parseInt(cb_nescab_almori.getSelectedItem().getValue().toString());
            codUbi = objNotaESDetalle.getNesdet_ubiori();
            txt_ubicod.setValue(codUbi);
            txt_ubides.setValue(objdaoubicaciones.descripcionUbicacion(objNotaESDetalle.getNesdet_ubiori()));
        }
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nescab_fecha.getValue()), almacen, objProductos.getPro_id(), String.valueOf(Integer.parseInt(codUbi)));
        if (objParametrosSalida.getCantStocks() > objProductos.getPro_scknofact()) {
            txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() - (objProductos.getPro_scknofact())) / objNotaESDetalle.getNesdet_undpre()));
        } else {
            txt_stock.setValue("0");
        }
        txt_proconv.setValue(objProductos.getPro_conv());
        txt_unimas.setValue(objProductos.getPro_unimas());
        txt_pimpto.setValue(objNotaESDetalle.getNesdet_pimpto());
        txt_vimpto.setValue(objNotaESDetalle.getNesdet_vimpto());
        txt_valafe.setValue(objNotaESDetalle.getNesdet_valafe());
        txt_valina.setValue(objNotaESDetalle.getNesdet_valina());
        txt_pvta.setValue(objNotaESDetalle.getNesdet_pvta());
        txt_provid.setValue(objNotaESDetalle.getNesdet_provid());
        txt_proveedor.setValue(objNotaESDetalle.getNesdet_provrazsoc());
        txt_fecadd_det.setValue(objNotaESDetalle.getNesdet_fecadd());
        txt_fecmod_det.setValue(objNotaESDetalle.getNesdet_fecmod());
        txt_usuadd_det.setValue(objNotaESDetalle.getNesdet_usuadd());
        txt_usumod_det.setValue(objNotaESDetalle.getNesdet_usumod());
    }

    public void LlenaCamposDetalle(OrdCompDet objNES) throws SQLException {
        Productos objProductos;
        //aqui solo devolveremos las cantidades restantes de la o/c
        txt_proid.setValue(objNES.getPro_id());
        txt_prokey.setValue(Integer.parseInt(String.valueOf(objNES.getPro_key())));
        txt_prodes.setValue(objNES.getPro_des());
        txt_prodesdes.setValue(objNES.getPro_desdes());
        txt_undpre.setValue(Integer.parseInt(objNES.getPro_presminven()));
        txt_unimas.setValue(Integer.parseInt(objNES.getPro_unimas()));
        txt_provid.setValue(objNES.getPro_provid());
        txt_proveedor.setValue(objNES.getPro_provrazsoc());
        //cantidades
        objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
        if (s_estadoDetalle.equals("N")) {
            txt_canent.setValue((int) (objNES.getOcd_cantped() / Integer.parseInt(objNES.getPro_presminven())));
            //txt_canfra.setValue(0);
            txt_canfra.setValue(Math.round(objNES.getOcd_cantped() % Integer.parseInt(objNES.getPro_presminven())));
            txt_ubicod.setValue(objNES.getPro_ubi());
            txt_ubides.setValue(objdaoubicaciones.descripcionUbicacion(objNES.getPro_ubi()));
        } else {
            txt_canfra.setValue(Math.round(objNotaESDet.getNesdet_cant() % objNotaESDet.getNesdet_undpre()));
            if (txt_undpre.getValue() / 2.0 <= txt_canfra.getValue()) {
                txt_canent.setValue(Math.round(objNotaESDet.getNesdet_cant() / objNotaESDet.getNesdet_undpre()) - 1);
            } else {
                txt_canent.setValue(Math.round(objNotaESDet.getNesdet_cant() / objNotaESDet.getNesdet_undpre()));
            }
            txt_ubicod.setValue(objNotaESDet.getNesdet_ubides());
            txt_ubides.setValue(objdaoubicaciones.descripcionUbicacion(objNotaESDet.getNesdet_ubides()));
        }
        txt_proconv.setValue(objNES.getPro_conv());
        txt_pimpto.setValue(objNES.getOcd_pimpto());//aqui obtenemos el porcentaje de dscto
        txt_vimpto.setValue(objNES.getOcd_vimpto());
        txt_valafe.setValue(objNES.getOcd_vafecto());
        txt_valina.setValue(objNES.getOcd_exonerado());
        txt_pvta.setValue(objNES.getOcd_vtotal());
        //aqui para llenar el campo stock necesitamos hacer una funcion enviando empresa, sucursal, producto y almacen
        objParametrosSalida = new ParametrosSalida();
        int almacen;
        almacen = Integer.parseInt(cb_nescab_almdes.getSelectedItem().getValue().toString());
        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nescab_fecha.getValue()), almacen, objNES.getPro_id(), String.valueOf(Integer.parseInt(txt_ubicod.getValue().toString())));
        if (objParametrosSalida.getCantStocks() > objProductos.getPro_scknofact()) {
            txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() - (objProductos.getPro_scknofact())) / Integer.parseInt(objNES.getPro_presminven())));
        } else {
            txt_stock.setValue("0");
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        lst_notes.setModel(objlstNotaESCab);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void llenarCampos() {
        txt_usuadd.setValue(objNotaESCab.getNescab_usuadd());
        d_fecadd.setValue(objNotaESCab.getNescab_fecadd());
        txt_usumod.setValue(objNotaESCab.getNescab_usumod());
        d_fecmod.setValue(objNotaESCab.getNescab_fecmod());
    }

    public String verificarDetalle() {
        String valida;
        double impto = txt_vimpto.getValue();
        if (impto > 0) {
            if (txt_valafe.getValue() == null || txt_valafe.getValue() <= 0) {
                valida = "Ingrese Valor Afecto";
                foco = "afecto";
            } else {
                valida = "";
            }
        } else {
            if (txt_valina.getValue() == null || txt_valina.getValue() <= 0) {
                valida = "Ingrese Valor Inafecto";
                foco = "inafecto";
            } else {
                valida = "";
            }
        }
        return valida;
    }
}
