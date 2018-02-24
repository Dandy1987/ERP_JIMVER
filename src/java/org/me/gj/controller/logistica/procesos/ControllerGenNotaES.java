package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.cxc.mantenimiento.DaoTipoCambio;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoProveedores;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.me.gj.model.logistica.mantenimiento.Proveedores;
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
import org.zkoss.zk.ui.event.InputEvent;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerGenNotaES extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Window w_gennotes;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoes, tbbtn_btn_editares, tbbtn_btn_eliminares, tbbtn_btn_guardares, tbbtn_btn_deshaceres;
    @Wire
    Listbox lst_notes, lst_notes_det;
    @Wire
    Combobox cb_periodo, cb_notaes, cb_serie, cb_moneda, cb_nescab_almori, cb_nescab_almdes, cb_nescab_tipdoc;
    @Wire
    Textbox txt_nescab_provid, txt_nescab_provrazsoc, txt_nescab_glosa, txt_nescab_tipnotaes, txt_nomtipnotaes,
            txt_nescab_id, txt_nescab_nroserie, txt_nescab_nrodoc, txt_proid, txt_prodes, txt_stock, txt_nescab_ocnropedkey, txt_listapreid, txt_listapredes,
            txt_usuadd, txt_usumod, txt_nescab_cliid, txt_nescab_clinom, txt_nescab_nrodep, txt_nescab_nroped, txt_provid,
            txt_proveedor, txt_prodesdes, txt_ubicod, txt_ubides, txt_usuadd_det, txt_usumod_det, txt_proconv, txt_filtro_nota;
    @Wire
    Datebox d_nescab_fecha, d_fecemi, txt_fecmod_det, txt_fecadd_det, d_fecadd, d_fecmod;
    @Wire
    Intbox txt_prokey, txt_undpre, txt_unimas;
    @Wire
    Timebox d_nescab_hora;
    @Wire
    Checkbox chk_despacho, chk_estado;
    @Wire
    Doublebox txt_nescab_tcamb, txt_canent, txt_canfra, txt_precio,
            txt_pimpto, txt_vimpto, txt_valafe, txt_valina, txt_pvta;
    @Wire
    Button btn_nescab_ocnropedkey, btn_buscar, btn_despacho;
    //Instancias de Objetos
    ListModelList<ManPer> objListModelListManPer;
    ListModelList<Guias> objlstGuias;
    ListModelList<Almacenes> objlstAlmacenOrigen;
    ListModelList<Almacenes> objlstAlmacenDestino;
    ListModelList<Moneda> objlstMonedas;
    ListModelList<TipDoc> objlstTipDoc;
    ListModelList<Numeracion> objlstNumeracion;
    ListModelList<OrdCompDet> objlstOrdCompDet = new ListModelList<OrdCompDet>();
    ListModelList<NotaESDet> objlstEliminacion = new ListModelList<NotaESDet>();
    ListModelList<NotaESCab> objlstNotaESCab;
    ListModelList<NotaESDet> objlstNotaESDet;
    ListModelList<Productos> objlstproducto;
    DaoUbicaciones objdaoubicaciones = new DaoUbicaciones();
    DaoManPer objDaoManPer = new DaoManPer();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    DaoAlmacenes objDaoAlmacen = new DaoAlmacenes();
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    DaoMoneda objDaoMonedas = new DaoMoneda();
    DaoNumeracion objDaoNumeracion = new DaoNumeracion();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    DaoNotaES objDaoNotaES;
    DaoOrdCom objDaoOrdComp;
    DaoProductos objDaoProducto;
    ListaPrecio objListaPrecio = new ListaPrecio();
    Guias objGuias = new Guias();
    NotaESCab objNotaESCab = new NotaESCab();
    NotaESDet objNotaESDet = new NotaESDet();
    ManPer objManPer = new ManPer();
    Ubicaciones objUbicaciones;
    Almacenes objAlmacenes = new Almacenes();
    Moneda objMonedas = new Moneda();
    TipDoc objTipDoc = new TipDoc();
    Numeracion objNumeracion = new Numeracion();
    Accesos objAccesos = new Accesos();
    OrdCompDet objOrdCompDet = new OrdCompDet();
    OrdCompDet objlstNotaES;
    //Utilitarios objUtil = new Utilitarios();
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
    String foco = "";
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerGenNotaES.class);

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
        objListModelListManPer = objDaoManPer.listaPeriodos(1);
        cb_periodo.setModel(objListModelListManPer);
        objListModelListManPer.add(new ManPer("", ""));
        cb_periodo.setValue(Utilitarios.periodoActual());
        d_fecemi.setValue(Utilitarios.hoyAsFecha());
        //cb_notaes.focus();
        //carga guias
        objlstGuias = new ListModelList<Guias>();
        //objlstGuias = objDaoManNotaES.listaGuias(1);
        objlstGuias = objDaoManNotaES.listaGuiasCombo(1);
        cb_notaes.setModel(objlstGuias);
        objlstGuias.add(new Guias(100, ""));
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
        //txt_filtro_nota.focus();
        cb_periodo.focus();
        cb_periodo.select();
        //consulta inicial
        objlstNotaESCab = objDaoNotaES.listaNotaESCab();
        lst_notes.setModel(objlstNotaESCab);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10206000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a la Generación de Notas de E/S con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a la Generación de Notas de E/S con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de una nueva Nota de E/S");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de una nueva Nota de E/S");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Nota de E/S");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Nota de E/S");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de una Nota de E/S");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de una Nota de E/S");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de una Nota de E/S");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de una Nota de E/S");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        int i_estado;
        int i_despacho;
        String s_periodo, f_fecha, s_nescab_tipnotaes = "";
        Date fecha;
        i_estado = chk_estado.isChecked() ? 1 : 3;
        i_despacho = chk_despacho.isChecked() ? 1 : 2;
        s_periodo = cb_periodo.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha = d_fecemi.getValue();
        if (fecha == null) {
            f_fecha = "";
        } else {
            f_fecha = sdf.format(d_fecemi.getValue());
        }
        if (cb_notaes.getSelectedIndex() != -1) {
            if (Integer.parseInt(cb_notaes.getSelectedItem().getValue().toString()) == 100) {
                s_nescab_tipnotaes = "";
            } else {
                s_nescab_tipnotaes = Utilitarios.lpad(cb_notaes.getSelectedItem().getValue().toString(), 3, "0");
            }
        }
        String s_nota = txt_filtro_nota.getValue();
        objlstNotaESCab = new ListModelList<NotaESCab>();
        objlstNotaESCab = objDaoNotaES.BusquedaNotaES(s_periodo, f_fecha, s_nescab_tipnotaes, i_estado, i_despacho, s_nota);
        if (objlstNotaESCab.getSize() > 0) {
            lst_notes.setModel(objlstNotaESCab);
        } else {
            objlstNotaESCab = null;
            lst_notes.setModel(objlstNotaESCab);
            Messagebox.show("No existe registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
        objNotaESCab = new NotaESCab();
        objNotaESCab = (NotaESCab) lst_notes.getSelectedItem().getValue();
        if (objNotaESCab == null) {
            objNotaESCab = objlstNotaESCab.get(i_sel + 1);
        }
        i_sel = lst_notes.getSelectedIndex();
        //cargamos las listas con los contactos y telefonos de los proveedores
        int i_estado;
        i_estado = chk_estado.isChecked() ? 1 : 3;
        objlstNotaESDet = objDaoNotaES.listaNotaESDet(objNotaESCab.getNescab_id(), i_estado);
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
        //llenarCamposDetalle(); 
        String codProd = objNotaESDet.getPro_id();
        String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
        nrooc = Long.parseLong(ordcompra);
        if (nrooc > 0) {
            String notaes_cab;
            if (s_estado.equals("E")) {
                notaes_cab = objNotaESCab.getNescab_id();
            } else {
                notaes_cab = "";
            }
            objlstNotaES = objDaoOrdComp.OrdCompDetNotaESxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Long.parseLong(txt_nescab_ocnropedkey.getValue()), "C", codProd, s_estado, notaes_cab);
            if (!txt_nescab_id.getValue().equals("")) {
                objlstNotaES.setPro_ubi(objNotaESDet.getNesdet_ubiori());
                txt_fecadd_det.setValue(objNotaESDet.getNesdet_fecadd());
                txt_fecmod_det.setValue(objNotaESDet.getNesdet_fecmod());
                txt_usuadd_det.setValue(objNotaESDet.getNesdet_usuadd());
                txt_usumod_det.setValue(objNotaESDet.getNesdet_usumod());
            }
            LlenaCamposDetalle(objlstNotaES);
            habilitaCamposDetalle(true);
        } else {
            llenarCamposDetalle(objNotaESDet);
            habilitaCamposDetalle(true);
        }
        //solo lectura
        if (s_estado.equals("N")) {
            habilitaBotonesDetalle(false, true);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con glosa " + objOrdCompDet.getOcd_glosa());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaBotonesDetalle(false, true);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        limpiarCamposDetalle();
        //habilitar campos 
        d_nescab_fecha.setDisabled(false);
        txt_nescab_tipnotaes.setDisabled(false);
        cb_serie.setDisabled(false);
        cb_nescab_almdes.setDisabled(false);
        LlenaDataES();
        s_estado = "N";
        //cargo por defecto soles y verifica tipo de cambio
        //cb_moneda.setValue("NUEVOS SOLES");
        //cb_moneda.setModel(objlstMonedas);
        cb_moneda.setDisabled(false);
        //obtenerTipoCambio();
        //d_nescab_fecha.focus();
        cb_moneda.focus();
        cb_moneda.setValue("NUEVOS SOLES");
        cb_moneda.select();
        objlstNotaESDet = null;
        objlstNotaESDet = new ListModelList<NotaESDet>();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_nuevoes")
    public void botonNuevoDetalle() throws SQLException {
        String valida;
        valida = VerificarCabecera();
        if (!VerificarCabecera().isEmpty()) {
            Messagebox.show("Completar datos de la nota E/S: '" + valida + "'","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
        } else {
            limpiarCamposDetalle();
            habilitaCampos(true);
            habilitaCamposDetalle(false);
            habilitaBotonesDetalle(true, false);
            s_estadoDetalle = "N";
            txt_proid.focus();
            txt_ubicod.setDisabled(true);
        }
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro Detalle");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_notes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una nota E/S", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if ("ANULADO".equals(objNotaESCab.getEstado()) || objNotaESCab.getNescab_situacion() == 2) {
                Messagebox.show("La nota E/S ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if ("010".equals(objNotaESCab.getNescab_tipnotaes()) || "011".equals(objNotaESCab.getNescab_tipnotaes()) || "012".equals(objNotaESCab.getNescab_tipnotaes()) || "013".equals(objNotaESCab.getNescab_tipnotaes())
                    || "014".equals(objNotaESCab.getNescab_tipnotaes()) || "018".equals(objNotaESCab.getNescab_tipnotaes()) || "019".equals(objNotaESCab.getNescab_tipnotaes()) || "020".equals(objNotaESCab.getNescab_tipnotaes())) {
                Messagebox.show("La nota E/S ya no puede ser modificada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (objNotaESCab.getNescab_despacho() == 1){
                Messagebox.show("La nota E/S ya fue despachada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (objNotaESCab.getNescab_costeo()== 1){
                Messagebox.show("La nota E/S ya fue costeada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else{
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaBotonesDetalle(false, true);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                objlstEliminacion = null;
                objlstEliminacion = new ListModelList<NotaESDet>();
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
            txt_proid.setDisabled(true);
            habilitaCamposCabecera(true);
            s_estadoDetalle = "E";
            //txt_canent.focus();
            habilitaCampos(true);
            txt_ubicod.focus();
        }
        d_nescab_hora.setDisabled(true);
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_notes.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione una nota E/S", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (objNotaESCab.getNescab_est() == 1) {
                if (objNotaESCab.getNescab_despacho() == 1) {
                    Messagebox.show("Esta nota E/S ya no puede ser anulada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    s_mensaje = "Está seguro que desea eliminar esta nota E/S?";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        ParametrosSalida objParamSalida, objParamDetalle;
                                        objNotaESCab.setNescab_usumod(objUsuCredential.getCuenta());
                                        objParamSalida = objDaoNotaES.eliminarCabecera(objNotaESCab);
                                        String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
                                        nrooc = Long.parseLong(ordcompra);
                                        if (nrooc > 0) {
                                            objParamDetalle = objDaoNotaES.actualizaSituacionOC(objNotaESCab.getNescab_ocnropedkey(), objNotaESCab.getNescab_id());
                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                Messagebox.show("Ocurrio un Error al Actualizar la Situacion de la O/C debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                //verificarDetalle = false;
                                            }
                                        }
                                        if (objParamSalida.getFlagIndicador() == 0) {
                                            limpiarLista();
                                            busquedaRegistros();
                                            limpiarCampos();
                                            limpiaAuditoria();
                                        }
                                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            });
                }
            } else {
                Messagebox.show("La nota ya fue anulada","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminares")
    public void botonEliminarDetalle() {
        if (lst_notes_det.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objNotaESDet = (NotaESDet) lst_notes_det.getSelectedItem().getValue();
            int posicion = lst_notes_det.getSelectedIndex();
            if (s_estado.equals("E") && (!objNotaESDet.getInd_accion().equals("N") || !objNotaESDet.getInd_accion().equals("NM"))) {
                long nesdet_item = objNotaESDet.getNesdet_item();
                String nescab_id = txt_nescab_id.getValue();
                String nescab_key = txt_nescab_id.getValue().substring(3, 10);
                String tcd_usumod = objUsuCredential.getCuenta();
                objlstEliminacion.add(new NotaESDet(nescab_id, nescab_key, objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), nesdet_item, tcd_usumod));
            }
            objlstNotaESDet = (ListModelList) lst_notes_det.getModel();
            objlstNotaESDet.remove(posicion);
            limpiarCamposDetalle();
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        if (s_estadoDetalle.equals("E") && tbbtn_btn_editares.isDisabled()) {
            Messagebox.show("El detalle se encuentra en edición","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
        } else {
            String s_valida = VerificarCabecera();
            if (!s_valida.isEmpty()) {
                Messagebox.show("Faltan datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else if (lst_notes_det.getItemCount() <= 0) {
                Messagebox.show("Debe ingresar algun producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    ParametrosSalida objParametrosCabecera;
                                    objNotaESCab = generaRegistro();
                                    if (s_estado.equals("N")) {
                                        objParametrosCabecera = objDaoNotaES.insertarCabecera(objNotaESCab);
                                    } else {
                                        objParametrosCabecera = objDaoNotaES.modificarCabecera(objNotaESCab);
                                    }
                                    if (objParametrosCabecera.getFlagIndicador() == 0) {
                                        ParametrosSalida objParamDetalle = new ParametrosSalida();
                                        boolean verificarDetalle = true;
                                        int i = 0;
                                        if (s_estado.equals("N")) {
                                            if (objlstNotaESDet == null || objlstNotaESDet.isEmpty()) {
                                                Session sess = Sessions.getCurrent();
                                                objlstNotaESDet = (ListModelList) sess.getAttribute("objlstNotaESDet");
                                                while (i < objlstNotaESDet.getSize() && verificarDetalle) {
                                                    String nescab_id = objParametrosCabecera.getCodigoRegistro();
                                                    String nescab_key = nescab_id.substring(3, 10).toString();
                                                    String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
                                                    String nesdet_almori = cb_nescab_almori.getSelectedItem().getValue();
                                                    String nesdet_almdes = cb_nescab_almdes.getSelectedItem().getValue();
                                                    objlstNotaESDet.get(i).setNescab_id(nescab_id);
                                                    objlstNotaESDet.get(i).setNescab_key(nescab_key);
                                                    objlstNotaESDet.get(i).setNescab_tipnotaes(nescab_tipnotaes);
                                                    objlstNotaESDet.get(i).setNesdet_almori(nesdet_almori);
                                                    objlstNotaESDet.get(i).setNesdet_almdes(nesdet_almdes);
                                                    if ("001".equals(nescab_tipnotaes)) {
                                                        objlstNotaESDet.get(i).setNesdet_ubiori("");
                                                    }
                                                    objParamDetalle = objDaoNotaES.insertarDetalle(objlstNotaESDet.get(i));
                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                        Messagebox.show("Ocurrió un error al crear el producto " + objlstNotaESDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                        verificarDetalle = false;
                                                    }
                                                    i++;
                                                }
                                            } else if (objlstNotaESDet != null) {
                                                while (i < objlstNotaESDet.getSize() && verificarDetalle) {
                                                    String nescab_id = objParametrosCabecera.getCodigoRegistro();
                                                    String nescab_key = nescab_id.substring(3, 10).toString();
                                                    String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
                                                    String nesdet_almori = cb_nescab_almori.getSelectedItem().getValue();
                                                    String nesdet_almdes = cb_nescab_almdes.getSelectedItem().getValue();
                                                    objlstNotaESDet.get(i).setNescab_id(nescab_id);
                                                    objlstNotaESDet.get(i).setNescab_key(nescab_key);
                                                    objlstNotaESDet.get(i).setNescab_tipnotaes(nescab_tipnotaes);
                                                    objlstNotaESDet.get(i).setNesdet_almori(nesdet_almori);
                                                    objlstNotaESDet.get(i).setNesdet_almdes(nesdet_almdes);
                                                    if ("001".equals(nescab_tipnotaes)) {
                                                        objlstNotaESDet.get(i).setNesdet_ubiori("");
                                                    }
                                                    objParamDetalle = objDaoNotaES.insertarDetalle(objlstNotaESDet.get(i));
                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                        Messagebox.show("Ocurrió un error al crear el producto " + objlstNotaESDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                        verificarDetalle = false;
                                                    }
                                                    i++;
                                                }
                                            }
                                        } else {
                                            //OPERACION DE ELINACION DE PRODUCTOS DE LA LISTA FACTURA PROVEEDOR
                                            if (!objlstEliminacion.isEmpty()) {
                                                while (i < objlstEliminacion.getSize() && verificarDetalle) {
                                                    objParamDetalle = objDaoNotaES.eliminarDetalle(objlstEliminacion.get(i));
                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                        Messagebox.show("Ocurrió un error al eliminar un producto con el item" + objlstEliminacion.get(i).getNesdet_item() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                        verificarDetalle = false;
                                                    }
                                                    i++;
                                                }
                                            }
                                            //OPERACION DE INSERCION Y MODIFICACION DE PRODUCTOS DE LA LISTA ORDEN DE COMPRA DETALLE
                                            if (objlstNotaESDet != null) {
                                                i = 0;
                                                verificarDetalle = true;
                                                while (i < objlstNotaESDet.getSize() && verificarDetalle) {
                                                    String indicador = objlstNotaESDet.get(i).getInd_accion();
                                                    if (indicador.equals("N") || indicador.equals("NM")) {
                                                        objParamDetalle = objDaoNotaES.insertarDetalle(objlstNotaESDet.get(i));
                                                    } else if (indicador.equals("M")) {
                                                        objParamDetalle = objDaoNotaES.modificarDetalle(objlstNotaESDet.get(i));
                                                    }
                                                    if (objParamDetalle.getFlagIndicador() == 1) {
                                                        Messagebox.show("Ocurrió un error con el producto " + objlstNotaESDet.get(i).getPro_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                        verificarDetalle = false;
                                                    }
                                                    i++;
                                                }
                                            }
                                        }
                                        String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
                                        nrooc = Long.parseLong(ordcompra);
                                        if (nrooc > 0) {
                                            //ACTUALIZACION DE LA SITUACION DE LA O/C
                                            objParamDetalle = objDaoNotaES.actualizaSituacionOC(objNotaESCab.getNescab_ocnropedkey(), objParametrosCabecera.getCodigoRegistro());
                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                Messagebox.show("Ocurrió un error al actualizar la situación de la O/C debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                //verificarDetalle = false;
                                            }
                                        }
                                        // Messagebox.show(s_mensaje);
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
                                        //lst_notes.setSelectedIndex(-1);
                                        //limpiamos las listas
                                        objlstNotaESCab = new ListModelList<NotaESCab>();
                                        objlstNotaESDet = new ListModelList<NotaESDet>();
                                        lst_notes.setModel(objlstNotaESCab);
                                        lst_notes_det.setModel(objlstNotaESDet);
                                        //cargamos la lista de facturas proveedor
                                        objlstNotaESCab = objDaoNotaES.listaNotaESCab();
                                        lst_notes.setModel(objlstNotaESCab);
                                        lst_notes.setSelectedIndex(-1);
                                        s_estadoDetalle = "Q";
                                        s_estado = "Q";
                                        String periodo = Utilitarios.periodoActual();
                                        ParametrosSalida objParametrosSalida = new DaoRegeneraStock().regenerarStock(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), periodo, objUsuCredential.getCuenta());
                                        if (objParametrosSalida.getFlagIndicador() != 0) {
                                            Messagebox.show("No se pudo regenerar el stock","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                        }
                                    } else {
                                        Messagebox.show(objParametrosCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            }
                        }
                );
            }
        }
        d_nescab_hora.setDisabled(true);
    }

    @Listen("onClick=#tbbtn_btn_guardares")
    public void agregarProducto() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (objlstNotaES != null) {
                String validaProducto = verificarDetalle();
                String validaStock = verificaStock();
                String validaCantidad;
                String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
                nrooc = Long.parseLong(ordcompra);
                if (nrooc > 0) {
                    validaCantidad = verificaCantOCvsNES();
                } else {
                    validaCantidad = "";
                }
                if (!validaProducto.isEmpty()) {
                    Messagebox.show(validaProducto, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        if (foco.equals("producto")) {
                                            txt_proid.focus();
                                        } else if (foco.equals("cantent")) {
                                            txt_canent.focus();
                                        } else if (foco.equals("cantfrac")) {
                                            txt_canfra.focus();
                                        } else if (foco.equals("ubicacion")) {
                                            txt_ubicod.focus();
                                        }
                                    }
                                }
                            });
                } else if (!validaStock.isEmpty()) {
                    Messagebox.show(validaStock, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else if (!validaCantidad.isEmpty()) {
                    Messagebox.show(validaCantidad, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                } else {
                    String pro_id = txt_proid.getValue();
                    if ("001".equals(txt_nescab_tipnotaes.getValue())) {
                        objlstNotaESDet = (ListModelList) lst_notes_det.getModel();
                    }
                    if (s_estado.equals("N")) {//si es nueva nota es
                        if (s_estadoDetalle.equals("N")) {
                            if (validaIngresoProducto(pro_id)) {
                                objNotaESDet = (NotaESDet) generaRegistroDetalle();
                                objlstNotaESDet.add(objNotaESDet);
                                lst_notes_det.setModel(objlstNotaESDet);
                            } else {
                                Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                limpiarCamposDetalle();
                            }
                        } else {
                            objNotaESDet = (NotaESDet) generaRegistroDetalle();
                            int lst_ord = lst_notes_det.getSelectedIndex();
                            objlstNotaESDet.set(lst_ord, objNotaESDet);
                        }
                    } else {//si es edicion de nota E/S
                        if (s_estadoDetalle.equals("N")) {
                            if (validaIngresoProducto(pro_id)) {
                                objNotaESDet = (NotaESDet) generaRegistroDetalle();
                                objNotaESDet.setInd_accion("N");
                                objlstNotaESDet.add(objNotaESDet);
                            } else {
                                Messagebox.show("El producto ya fue ingresado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        } else {
                            String indicador = objNotaESDet.getInd_accion();
                            if (indicador.equals("N") || indicador.equals("NM")) {
                                objNotaESDet = (NotaESDet) generaRegistroDetalle();
                                objNotaESDet.setInd_accion("NM");
                            } else {
                                long item = objNotaESDet.getNesdet_item();
                                objNotaESDet = (NotaESDet) generaRegistroDetalle();
                                objNotaESDet.setInd_accion("M");
                                objNotaESDet.setNesdet_item(item);
                            }

                            int lst_ord = lst_notes_det.getSelectedIndex();
                            objlstNotaESDet.set(lst_ord, objNotaESDet);
                        }
                    }
                    limpiarCamposDetalle();
                    habilitaBotonesDetalle(false, true);
                    habilitaCamposDetalle(true);
                    habilitaCampos(false);
                    if (txt_nescab_tipnotaes.getValue().equals("001")) {
                        txt_nescab_cliid.setDisabled(true);
                        txt_nescab_provid.setDisabled(true);
                        txt_nescab_glosa.setDisabled(true);
                        btn_nescab_ocnropedkey.setDisabled(false);
                    } else {
                        txt_nescab_cliid.setDisabled(false);
                        txt_nescab_provid.setDisabled(true);
                        btn_nescab_ocnropedkey.setDisabled(true);
                    }
                    lst_notes_det.setSelectedIndex(-1);
                    Utilitarios.deshabilitarLista(false, lst_notes_det);
                }
                d_nescab_hora.setDisabled(true);
            } else {
                Messagebox.show("Por favor ingrese un producto de la orden de compra", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_proid.focus();
                                }
                            }
                        });
            }
        } else {
            Messagebox.show("Por favor ingrese un producto", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_proid.focus();
                            }
                        }
                    });
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
                    /*habilitaCampos(false);
                     d_nescab_hora.setDisabled(true);
                     if (txt_nescab_tipnotaes.getValue().equals("001")) {
                     txt_nescab_cliid.setDisabled(true);
                     txt_nescab_provid.setDisabled(true);
                     txt_nescab_glosa.setDisabled(true);
                     btn_nescab_ocnropedkey.setDisabled(false);
                     } else {
                     txt_nescab_cliid.setDisabled(false);
                     txt_nescab_provid.setDisabled(true);
                     btn_nescab_ocnropedkey.setDisabled(true);
                     }*/
                    habilitaCamposDetalle(true);
                    Utilitarios.deshabilitarLista(false, lst_notes_det);
                    lst_notes_det.setSelectedIndex(-1);
                    s_estadoDetalle = "Q";
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                }
                d_nescab_hora.setDisabled(true);
            }
        });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstNotaESCab == null || objlstNotaESCab.isEmpty()) {
            Messagebox.show("No hay notas de E/S para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notes.getSelectedIndex() >= 0) {
                objNotaESCab = (NotaESCab) lst_notes.getSelectedItem().getValue();
                if (objNotaESCab == null) {
                    objNotaESCab = objlstNotaESCab.get(i_selCab + 1);
                }
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
                objMapObjetos.put("usuario", objUsuCredential.getCuenta());
                objMapObjetos.put("emp_id", objUsuCredential.getCodemp());
                objMapObjetos.put("suc_id", objUsuCredential.getCodsuc());
                objMapObjetos.put("codigoNotaES", objNotaESCab.getNescab_id());
                objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
                Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/procesos/LovImpresionGenNotaES.zul", null, objMapObjetos);
                window.doModal();
            } else {
                Messagebox.show("Seleccione una nota de E/S para imprimir","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
            }
        }
    }

     //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_gennotes")
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
            case 119:
                if (!tbbtn_btn_imprimir.isDisabled()) {
                    botonImprimir();
                }
                break;

            case 78:
                if (!tbbtn_btn_nuevoes.isDisabled()) {
                    botonNuevoDetalle();
                }
                break;
            case 77:
                if (!tbbtn_btn_editares.isDisabled()) {
                    botonEditarDetalle();
                }
                break;
            case 69:
                if (!tbbtn_btn_eliminares.isDisabled()) {
                    botonEliminarDetalle();
                }
                break;
            case 71:
                if (!tbbtn_btn_guardares.isDisabled()) {
                    agregarProducto();
                }
                break;
            case 68:
                if (!tbbtn_btn_deshaceres.isDisabled()) {
                    botonDeshacerDetalle();
                }
                break;
        }
    }
    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_nescab_tipnotaes")
    public void lov_notaes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nescab_tipnotaes.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenNotES";
                mapeo.put("txt_nescab_tipnotaes", txt_nescab_tipnotaes);
                mapeo.put("txt_nomtipnotaes", txt_nomtipnotaes);
                mapeo.put("cb_serie", cb_serie);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerNotaES");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNotaES.zul", null, mapeo);
                window.doModal();
                cb_serie.setDisabled(false);
            } else {
                limpiarListaSerie();
                cb_serie.setDisabled(false);
                cb_serie.focus();
            }
        }
    }

    @Listen("onBlur=#txt_nescab_tipnotaes")
    public void valida_tipnotaes() throws SQLException {
        if (!txt_nescab_tipnotaes.getValue().isEmpty()) {
            if (!txt_nescab_tipnotaes.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nescab_tipnotaes.setValue("");
                                    txt_nomtipnotaes.setValue("");
                                    limpiarListaSerie();
                                    txt_nescab_tipnotaes.focus();
                                }
                            }
                        });
            } else {
                txt_nescab_tipnotaes.setValue(Utilitarios.lpad(txt_nescab_tipnotaes.getValue(), 3, "0"));
                Guias objGuia = new DaoManNotaES().BusquedaGuia(txt_nescab_tipnotaes.getValue());
                if (objGuia.getCodigo() == null) {
                    Messagebox.show("El código de la nota de E/S no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {

                                        txt_nescab_tipnotaes.setValue("");
                                        txt_nomtipnotaes.setValue("");
                                        limpiarListaSerie();
                                        txt_nescab_tipnotaes.focus();
                                    }
                                }
                            });
                } else {
                    cambiodenotaes();
                    txt_nomtipnotaes.setValue(objGuia.getDesGui());
                    limpiarListaSerie();
                    objlstNumeracion = objDaoNumeracion.listaSeries(Integer.parseInt(txt_nescab_tipnotaes.getValue().toString()));
                    cb_serie.setModel(objlstNumeracion);
                    cb_serie.setDisabled(false);
                    cb_serie.focus();
                }
            }
        }
        if ("001".equals(txt_nescab_tipnotaes.getValue())) {
            txt_nescab_ocnropedkey.setValue("");
            txt_nescab_cliid.setValue("");
            txt_nescab_clinom.setValue("");
            txt_nescab_cliid.setDisabled(true);
            txt_nescab_glosa.setDisabled(true);
        }
        txt_listapreid.setValue("");
        txt_listapredes.setValue("");
        bandera = false;
    }

    @Listen("onOK=#txt_nescab_provid")
    public void lov_proveedores() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nescab_provid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenNotES";
                mapeo.put("txt_nescab_provid", txt_nescab_provid);
                mapeo.put("txt_nescab_provrazsoc", txt_nescab_provrazsoc);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("tipo", "1");
                mapeo.put("controlador", "ControllerNotaES");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, mapeo);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_nescab_provid")
    public void valida_proveedores() throws SQLException {
        txt_nescab_provrazsoc.setValue("");
        if (!txt_nescab_provid.getValue().isEmpty()) {
            if (!txt_nescab_provid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nescab_provid.setValue("");
                                    txt_nescab_provid.focus();
                                }
                            }
                        });
            } else {
                long prov_id = Long.parseLong(txt_nescab_provid.getValue());
                Proveedores objProveedor = (new DaoProveedores()).BusquedaProveedor(Long.valueOf(prov_id));
                if (objProveedor == null) {
                    s_mensaje = "El código del proveedor no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_nescab_provid.setValue("");
                                        txt_nescab_provid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Proveedor ").append(objProveedor.getProv_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_nescab_provid.setValue(objProveedor.getProv_id());
                    txt_nescab_provrazsoc.setValue(objProveedor.getProv_razsoc());
                }
            }
            txt_nescab_glosa.focus();
        }
        bandera = false;
    }

    @Listen("onOK=#txt_nescab_cliid")
    public void lov_clientes() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_nescab_cliid.getValue().isEmpty()) {
                Map mapeo = new HashMap();
                modoEjecucion = "mantGenNotES";
                mapeo.put("txt_cliid", txt_nescab_cliid);
                mapeo.put("txt_clinom", txt_nescab_clinom);
                mapeo.put("validaBusqueda", modoEjecucion);
                mapeo.put("controlador", "ControllerNotaES");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovClientes.zul", null, mapeo);
                window.doModal();
            } else {
                txt_nescab_glosa.setDisabled(false);
                txt_nescab_glosa.focus();
            }
        }
    }

    @Listen("onBlur=#txt_nescab_cliid")
    public void valida_clientes() throws SQLException {
        txt_nescab_clinom.setValue("");
        if (!txt_nescab_cliid.getValue().isEmpty()) {
            if (!txt_nescab_cliid.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_nescab_cliid.setValue("");
                                    txt_nescab_clinom.setValue("");
                                    txt_nescab_cliid.focus();
                                }
                            }
                        });
            } else {
                Cliente objCliente;
                if (Long.parseLong(txt_nescab_cliid.getValue()) < 1000000000) {
                    txt_nescab_cliid.setValue(Utilitarios.lpad(txt_nescab_cliid.getValue(), 10, "0"));
                    objCliente = new DaoCliente().getNomCliente(txt_nescab_cliid.getValue());
                } else {
                    objCliente = null;
                }
                if (objCliente == null) {
                    s_mensaje = "El código del cliente no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_nescab_cliid.setValue("");
                                        txt_nescab_clinom.setValue("");
                                        txt_nescab_cliid.focus();
                                    }
                                }
                            });
                } else {
                    LOGGER.info((new StringBuilder()).append("[").append(objUsuCredential.getComputerName()).append("] | ").append(objUsuCredential.getCuenta()).append(" | ha cargado los datos del Cliente ").append(objCliente.getCli_id()).append(" y ha encontrado 1 registro(s)").toString());
                    txt_nescab_cliid.setValue(objCliente.getCli_id());
                    txt_nescab_clinom.setValue(objCliente.getCli_razsoc());
                }
            }
        } else {
            txt_nescab_clinom.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_proid")
    public void lov_productosSeleccion() throws SQLException {
        if (txt_nescab_ocnropedkey.getValue().equals("") || txt_nescab_ocnropedkey.getValue().isEmpty()) {
            lov_productos();
        } else {
            lov_productosOrden();
        }
    }

    @Listen("onBlur=#txt_proid")
    public void valida_productos() throws SQLException {
        if (!txt_proid.getValue().isEmpty()) {
            if (!txt_proid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    limpiarCamposDetalle();
                                    txt_proid.focus();
                                }
                            }
                        });
            } else {
                Productos objProductos;
                objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
                if (objProductos == null) {
                    Messagebox.show("El código de producto no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION,
                            new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        limpiarCamposDetalle();
                                        txt_proid.focus();
                                    }
                                }
                            });

                } else {
                    String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
                    nrooc = Long.parseLong(ordcompra);
                    txt_proid.setDisabled(true);
                    txt_ubicod.setDisabled(false);
                    txt_ubicod.focus();
                    if (nrooc > 0) {
                        String notaes_cab;
                        if (s_estado.equals("E")) {
                            notaes_cab = objNotaESCab.getNescab_id();
                        } else {
                            notaes_cab = "";
                        }
                        objlstNotaES = objDaoOrdComp.OrdCompDetNotaESxProd(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Long.parseLong(txt_nescab_ocnropedkey.getValue()), "C", txt_proid.getValue(), s_estado, notaes_cab);
                        if (objlstNotaES != null) {
                            LlenaCamposDetalle(objlstNotaES);
                        } else {
                            Messagebox.show("El producto no pertenece a la orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION,
                                    new EventListener() {
                                        @Override
                                        public void onEvent(Event event) throws Exception {
                                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                txt_proid.setValue("");
                                                txt_proid.focus();
                                            }
                                        }
                                    });
                        }
                    } else {
                        txt_proid.setValue(objProductos.getPro_id());
                        txt_proconv.setValue(objProductos.getPro_conv());
                        txt_prokey.setValue(objProductos.getPro_key());
                        txt_prodes.setValue(objProductos.getPro_des());
                        txt_prodesdes.setValue(objProductos.getPro_desdes());
                        txt_undpre.setValue(objProductos.getPro_presminven());
                        txt_unimas.setValue(objProductos.getPro_unimas());
                        txt_provid.setValue(objProductos.getPro_provid());
                        txt_proveedor.setValue(objProductos.getPro_sigprov());
                        String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
                        int almacen;
                        if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                                || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                                || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                                || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
                            almacen = Integer.parseInt(cb_nescab_almdes.getSelectedItem().getValue().toString());
                        } else {
                            almacen = Integer.parseInt(cb_nescab_almori.getSelectedItem().getValue().toString());
                        }
                        objParametrosSalida = new ParametrosSalida();
                        objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nescab_fecha.getValue()), almacen, objProductos.getPro_id(), "%%");
                        int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
                        if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > stockSeguridad) {
                            //txt_stock.setValue(String.valueOf(objParametrosSalida.getCantStocks() - objProductos.getPro_scknofact()));
                            txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - stockSeguridad));
                        } else {
                            txt_stock.setValue("0");
                        }
                    }
                }
            }
        }
        bandera = false;
    }

    @Listen("onChanging=#txt_nescab_tipnotaes")
    public void onChangingNotaEs(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            txt_nomtipnotaes.setValue("");
            cb_nescab_almori.setSelectedIndex(-1);
            limpiarCamposDetalle();
            //QUITAMOS LA DATA A LA SERIE
            limpiarListaSerie();
            txt_nescab_ocnropedkey.setValue("");
            cb_nescab_tipdoc.setSelectedIndex(-1);
            txt_nescab_nroserie.setValue("");
            txt_nescab_nrodoc.setValue("");
            txt_nescab_provid.setValue("");
            txt_nescab_provrazsoc.setValue("");
            txt_nescab_cliid.setValue("");
            txt_nescab_clinom.setValue("");
            txt_nescab_glosa.setValue("");
            cb_nescab_almori.setSelectedIndex(-1);
            cb_nescab_almdes.setSelectedIndex(-1);
            txt_listapreid.setValue("");
            txt_listapredes.setValue("");
            objlstOrdCompDet = null;
            objlstOrdCompDet = new ListModelList<OrdCompDet>();
            lst_notes_det.setModel(objlstOrdCompDet);
            objlstNotaESDet = null;
            objlstNotaESDet = new ListModelList<NotaESDet>();
            lst_notes_det.setModel(objlstNotaESDet);
            habilitaCamposCabecera(true);
        }
    }

    @Listen("onChanging=#txt_nescab_provid")
    public void onChangingProveedores(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            txt_prodes.setValue("");
            objlstOrdCompDet = null;
            lst_notes_det.setModel(objlstOrdCompDet);
        }
    }

    @Listen("onChanging=#txt_nescab_cliid")
    public void onChangingClientes(InputEvent event) {
        if (event.getValue().length() == 0) {
            txt_nescab_clinom.setValue("");
        }
    }

    @Listen("onOK=#cb_moneda")
    public void onSelectMoneda() throws SQLException {
        d_nescab_fecha.focus();
    }

    @Listen("onBlur=#cb_moneda")
    public void ontipocambioMoneda() throws SQLException {
        if (d_nescab_fecha != null) {
            obtenerTipoCambio();
        }
    }

    @Listen("onOK=#d_nescab_fecha")
    public void onBlurFecha() throws SQLException {
        if (d_nescab_fecha != null) {
            obtenerTipoCambio();
            txt_nescab_tipnotaes.focus();
        }
    }

    /*@Listen("onBlur=#d_nescab_fecha")
     public void onOKFecha() throws SQLException {
     if (d_nescab_fecha.getValue() == null) {
     s_mensaje = "Ingresar la fecha de emision de la nota de E/S";
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION,
     new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     d_nescab_fecha.focus();
     }
     }
     });
     }
     }*/
    @Listen("onSelect=#cb_serie")
    public void onSelectSerie() throws SQLException {
        cb_nescab_tipdoc.focus();
        cb_nescab_tipdoc.select();
    }

    @Listen("onOk=#cb_serie")
    public void onOKSerie() throws SQLException {
        cb_nescab_tipdoc.focus();
        cb_nescab_tipdoc.select();
    }

    @Listen("onBlur=#cb_serie")
    public void onBlurSerie() throws SQLException {
        if (cb_serie.getSelectedIndex() != -1) {
            if (!txt_nescab_tipnotaes.getValue().isEmpty()) {
                if (("").equals(cb_serie.getValue()) || cb_serie.getSelectedIndex() == -1) {
                    s_mensaje = "No existe serie para el tipo de nota E/S";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION,
                            new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        cb_nescab_almori.setSelectedIndex(-1);
                                        cb_nescab_almdes.setSelectedIndex(-1);
                                        cb_serie.focus();
                                    }
                                }
                            });
                } else {
                    String alm_ori = objDaoNumeracion.listaAlmacenxGuia(Integer.parseInt(txt_nescab_tipnotaes.getValue().toString()), cb_serie.getValue());
                    cb_nescab_almori.setSelectedItem(Utilitarios.textoPorTexto(cb_nescab_almori, alm_ori));
                    habilitaCajas(txt_nescab_tipnotaes.getValue());
                }
            } else {
                s_mensaje = "Debe ingresar nota E/S";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION,
                        new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    cb_serie.setSelectedIndex(-1);
                                    txt_nescab_tipnotaes.focus();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#btn_nescab_ocnropedkey")
    public void lov_oc() throws SQLException {
        if (bandera == false) {
            bandera = true;
            Map parametros = new HashMap();
            modoEjecucion = "mantNotaES";
            parametros.put("txt_nescab_ocnropedkey", txt_nescab_ocnropedkey);
            parametros.put("txt_nescab_provid", txt_nescab_provid);
            parametros.put("txt_nescab_provrazsoc", txt_nescab_provrazsoc);
            parametros.put("txt_nescab_glosa", txt_nescab_glosa);
            parametros.put("cb_nescab_almori", cb_nescab_almori);
            parametros.put("cb_nescab_almdes", cb_nescab_almdes);
            parametros.put("txt_listapreid", txt_listapreid);
            parametros.put("txt_listapredes", txt_listapredes);
            parametros.put("lst_notes_det", lst_notes_det);
            parametros.put("validaBusqueda", modoEjecucion);
            parametros.put("cb_nescab_tipdoc", cb_nescab_tipdoc);
            parametros.put("controlador", "ControllerNotaES");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProcOrdComRec.zul", null, parametros);
            window.doModal();
        }
    }

    @Listen("onBlur=#btn_nescab_ocnropedkey")
    public void valida_oc() throws SQLException {
        bandera = false;
    }

    @Listen("onOK=#cb_nescab_tipdoc")
    public void onSelectTipoDoc() throws SQLException {
        if (cb_nescab_tipdoc.getSelectedIndex() == -1) {
            cb_nescab_tipdoc.focus();
            cb_nescab_tipdoc.select();
        } else {
            txt_nescab_nroserie.focus();
        }
    }

    @Listen("onOK=#txt_nescab_nroserie")
    public void txt_nescab_nroserieNext() throws SQLException {
        if (!txt_nescab_nroserie.getValue().isEmpty()) {
            txt_nescab_nrodoc.focus();
        }
    }

    @Listen("onOK=#txt_nescab_nrodoc")
    public void onSelectNumeroDoc() throws SQLException {
        if (!txt_nescab_nrodoc.getValue().matches("[0-9]*")) {
            Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nescab_nrodoc.setValue("");
                                txt_nescab_nrodoc.focus();
                            }
                        }
                    });
        } else if (txt_nescab_nrodoc.getValue().equals("")) {
            Messagebox.show("Ingrese número de referencia", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_nescab_nrodoc.setValue("");
                                txt_nescab_nrodoc.focus();
                            }
                        }
                    });
        } else {
            if (txt_nescab_provid.isDisabled() && txt_nescab_cliid.isDisabled()) {
                cb_nescab_almdes.focus();
            } else if (txt_nescab_provid.isDisabled() && !txt_nescab_cliid.isDisabled()) {
                txt_nescab_cliid.focus();
            } else {
                txt_nescab_provid.focus();
            }
        }
    }

    @Listen("onOK=#txt_nescab_glosa")
    public void onOkGlosa() {
        cb_nescab_almdes.focus();
    }

    @Listen("onOK=#cb_nescab_almdes")
    public void onSelectAlmacenDes() throws SQLException {
        botonNuevoDetalle();
    }

    @Listen("onOK=#txt_ubicod")
    public void lov_ubicaciones() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_ubicod.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "mantNotaES";
                objMapObjetos.put("txt_ubicod", txt_ubicod);
                objMapObjetos.put("txt_ubides", txt_ubides);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerNotaES");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbicacion.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_canent.focus();
            }
        }
    }

    @Listen("onBlur=#txt_ubicod")
    public void valida_ubicaciones() throws SQLException {
        if (!txt_ubicod.getValue().isEmpty()) {
            if (!txt_ubicod.getValue().matches("[0-9]*")) {
                s_mensaje = "Por favor ingrese valores numéricos";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_ubicod.setValue("");
                                    txt_ubides.setValue("");
                                    txt_ubicod.focus();
                                }
                            }
                        });
            } else {
                String codigo = Utilitarios.lpad(txt_ubicod.getValue(), 5, "0");
                objUbicaciones = (new DaoUbicaciones()).listauUbi(codigo);
                if (objUbicaciones == null) {
                    s_mensaje = "La ubicación no existe o está inactivo";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_ubicod.setValue("");
                                        txt_ubides.setValue("");
                                        txt_ubicod.focus();
                                    }
                                }
                            });
                } else {
                    Productos objProductos;
                    objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
                    int almacen;
                    String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
                    if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                            || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                            || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                            || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
                        almacen = Integer.parseInt(cb_nescab_almdes.getSelectedItem().getValue().toString());
                    } else {
                        almacen = Integer.parseInt(cb_nescab_almori.getSelectedItem().getValue().toString());
                    }
                    txt_ubicod.setValue(codigo);
                    txt_ubides.setValue(objdaoubicaciones.descripcionUbicacion(txt_ubicod.getValue()));
                    objParametrosSalida = new ParametrosSalida();
                    objParametrosSalida = new DaoProductos().stockProducto(Utilitarios.periodoActualxFecha(d_nescab_fecha.getValue()), almacen, objProductos.getPro_id(), String.valueOf(Integer.parseInt(txt_ubicod.getValue())));
                    int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
                    if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > stockSeguridad) {
                        txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - stockSeguridad));
                    } else {
                        txt_stock.setValue("0");
                    }
                }
            }
        } else {
            txt_ubides.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_canent")
    public void onOKCantidad() {
        txt_canfra.focus();
    }

    @Listen("onBlur=#txt_canent")
    public void onBlurCantidad() {
        if (txt_canent.getValue() == null) {
            txt_canent.setValue(0);
        }
    }

    @Listen("onOK=#txt_canfra")
    public void onOKtxt_canfra() throws SQLException {
        if (txt_canfra.getValue() == null) {
            txt_canfra.setValue(0);
            txt_canfra.focus();
        } else {
            agregarProducto();
        }
    }

    @Listen("onBlur=#txt_canfra")
    public void onBlurtxt_canfra() throws SQLException {
        if (txt_canfra.getValue() == null) {
            txt_canfra.setValue(0);
        }
    }

    @Listen("onClick=#btn_despachado")
    public void onClick_despachado() {
        if (objNotaESCab.getNescab_sitdes().equals("ND")) {
            objNotaESCab.setNescab_sitdes("D");
            objNotaESCab.setNescab_despacho(1);
        } else {
            objNotaESCab.setNescab_sitdes("ND");
            objNotaESCab.setNescab_despacho(2);
        }
    }

    @Listen("onClick=#btn_despacho")
    public void onClick_despacho() {
        if (objlstNotaESCab == null || objlstNotaESCab.isEmpty()) {
            Messagebox.show("No se encontró nota E/S", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (lst_notes.getSelectedIndex() >= 0) {
                objNotaESCab = (NotaESCab) lst_notes.getSelectedItem().getValue();
                if (objNotaESCab == null) {
                    objNotaESCab = objlstNotaESCab.get(i_selCab + 1);
                }
                if (objNotaESCab.getNescab_despacho() != 1) {
                    s_mensaje = "Está seguro que desea despachar la nota de E/S";
                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        objNotaESCab.setNescab_sitdes("D");
                                        objNotaESCab.setNescab_despacho(1);
                                        objNotaESCab.setNescab_usumod(objUsuCredential.getCuenta());
                                        ParametrosSalida objParametrosCabecera;
                                        objParametrosCabecera = objDaoNotaES.modificarCabecera(objNotaESCab);
                                        if (objParametrosCabecera.getFlagIndicador() == 0) {
                                            limpiarLista();
                                            busquedaRegistros();
                                            limpiarCampos();
                                            limpiaAuditoria();
                                        }
                                        Messagebox.show(objParametrosCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    }
                                }
                            });
                } else {
                    Messagebox.show("Esta nota de E/S ya fue despachada","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                }
            } else {
                Messagebox.show("Seleccione una nota de E/S","ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
            }
        }
    }

    @Listen("onOK=#txt_filtro_nota")
    public void onOK_txt_filtro_nota() throws SQLException {
        /*cb_notaes.focus();
        cb_notaes.select();*/
        busquedaRegistros();
    }

    @Listen("onBlur=#txt_filtro_nota")
    public void valida_txt_filtro_nota() throws SQLException {
        if (!txt_filtro_nota.getValue().isEmpty()) {
            if (!txt_filtro_nota.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_filtro_nota.setValue("");
                        txt_filtro_nota.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#cb_notaes")
    public void onOK_cb_notaes() throws SQLException {
        /*cb_periodo.focus();
        cb_periodo.select();*/
        txt_filtro_nota.focus();
    }

    @Listen("onOK=#cb_periodo")
    public void onOK_cb_periodo() throws SQLException {
        d_fecemi.focus();
    }

    @Listen("onOK=#d_fecemi")
    public void onOK_d_fecemi() throws SQLException {
        //btn_buscar.focus();
        cb_notaes.focus();
        cb_notaes.select();
        
    }

    //Eventos Otros     
    public void cambiodenotaes() throws SQLException {
        txt_nomtipnotaes.setValue("");
        cb_nescab_almori.setSelectedIndex(-1);
        limpiarCamposDetalle();
        //QUITAMOS LA DATA A LA SERIE
        limpiarListaSerie();
        txt_nescab_ocnropedkey.setValue("");
        cb_nescab_tipdoc.setSelectedIndex(-1);
        txt_nescab_nroserie.setValue("");
        txt_nescab_nrodoc.setValue("");
        txt_nescab_provid.setValue("");
        txt_nescab_provrazsoc.setValue("");
        txt_nescab_cliid.setValue("");
        txt_nescab_clinom.setValue("");
        txt_nescab_glosa.setValue("");
        cb_nescab_almori.setSelectedIndex(-1);
        cb_nescab_almdes.setSelectedIndex(-1);
        objlstOrdCompDet = null;
        objlstOrdCompDet = new ListModelList<OrdCompDet>();
        lst_notes_det.setModel(objlstOrdCompDet);
        objlstNotaESDet = null;
        objlstNotaESDet = new ListModelList<NotaESDet>();
        lst_notes_det.setModel(objlstNotaESDet);
        habilitaCamposCabecera(true);
    }

    public void lov_productosOrden() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                Map parametros = new HashMap();
                modoEjecucion = "mantNotaESOrden";
                parametros.put("txt_nescab_ocnropedkey", txt_nescab_ocnropedkey);
                parametros.put("txt_proid", txt_proid);
                parametros.put("txt_prodes", txt_prodes);
                parametros.put("txt_listapreid", txt_listapreid);
                parametros.put("validaBusqueda", modoEjecucion);
                parametros.put("controlador", "ControllerNotaESOrden");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, parametros);
                window.doModal();

            } else {
                txt_canent.focus();
            }
        }
    }

    public void lov_productos() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_proid.getValue().isEmpty()) {
                Map parametros = new HashMap();
                modoEjecucion = "mantNotaES";
                parametros.put("txt_proid", txt_proid);
                parametros.put("txt_prokey", txt_prokey);
                parametros.put("txt_prodes", txt_prodes);
                parametros.put("txt_prodesdes", txt_prodesdes);
                parametros.put("txt_undpre", txt_undpre);
                parametros.put("txt_unimas", txt_unimas);
                parametros.put("txt_proconv", txt_proconv);
                parametros.put("txt_provid", txt_provid);
                parametros.put("txt_proveedor", txt_proveedor);
                parametros.put("txt_canent", txt_canent);
                parametros.put("txt_ubides", txt_ubides);
                parametros.put("validaBusqueda", modoEjecucion);
                parametros.put("controlador", "ControllerNotaES");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProductos.zul", null, parametros);
                window.doModal();
            } else {
                txt_canent.focus();
            }
        }
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
        txt_precio.setValue(objNES.getOcd_precio());
        txt_provid.setValue(objNES.getPro_provid());
        txt_proveedor.setValue(objNES.getPro_provrazsoc());
        //cantidades
        objProductos = new DaoProductos().getNomProductos(txt_proid.getValue());
        if (s_estadoDetalle.equals("N")) {
            txt_canent.setValue((int) (objNES.getOcd_cantped() / Integer.parseInt(objNES.getPro_presminven())));
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
        int stockSeguridad = "001".equals(txt_nescab_tipnotaes.getValue()) ? objProductos.getPro_scknofact() : 0;
        if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > stockSeguridad) {
            txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - stockSeguridad));
        } else {
            txt_stock.setValue("0");
        }
    }

    public boolean validaIngresoProducto(String pro_id) {
        int i = 0;
        int cantDet = objlstNotaESDet == null ? 0 : objlstNotaESDet.getSize();
        boolean validaIngreso = true;
        while (i < cantDet && validaIngreso) {
            if (objlstNotaESDet.get(i).getPro_id().equals(pro_id)) {
                validaIngreso = false;
            }
            i++;
        }
        return validaIngreso;
    }

    public NotaESCab generaRegistro() {
        String nescab_id, nescab_key, nescab_tipnotaes, nescab_nroped, nescab_glosa, nescab_ocind,
                nescab_tipdoc, nescab_nroserie, nescab_nrodoc, nescab_provid, nescab_cliid, nescab_almori, nescab_almdes,
                nescab_nrodep, nescab_usuadd, nescab_usumod;
        String serie;
        int emp_id, suc_id, nescab_situacion, nescab_est, nescab_moneda, nescab_costeo, nescab_despacho;
        long nescab_ocnropedkey;
        double nescab_tcamb;
        Date nescab_fecha;
        nescab_id = txt_nescab_id.getValue();
        serie = cb_serie.getSelectedItem().getValue();
        nescab_key = (String) (txt_nescab_id.getValue().isEmpty() ? "" : txt_nescab_id.getValue().substring(3, 10));
        nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
        nescab_nroped = txt_nescab_nroped.getValue();
        nescab_glosa = txt_nescab_glosa.getValue().toUpperCase();
        nescab_ocind = txt_nescab_tipnotaes.getValue().equals("001") ? "C" : "";
        nescab_tipdoc = cb_nescab_tipdoc.getSelectedIndex() != -1 ? cb_nescab_tipdoc.getSelectedItem().getValue().toString() : "";
        nescab_nroserie = txt_nescab_nroserie.getValue().toUpperCase();
        nescab_nrodoc = txt_nescab_nrodoc.getValue();
        nescab_provid = txt_nescab_provid.getValue();
        nescab_cliid = txt_nescab_cliid.getValue();
        nescab_almori = cb_nescab_almori.getSelectedItem().getValue();
        nescab_almdes = cb_nescab_almdes.getSelectedItem().getValue();
        nescab_nrodep = txt_nescab_nrodep.getValue();
        nescab_usuadd = objUsuCredential.getCuenta();
        nescab_usumod = objUsuCredential.getCuenta();
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        nescab_situacion = 1;
        nescab_est = 1;
        nescab_moneda = cb_moneda.getSelectedItem().getValue();
        nescab_costeo = 2;
        nescab_despacho = 2;
        nescab_ocnropedkey = Long.parseLong(txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue());
        nescab_tcamb = (txt_nescab_tcamb.getValue() == null ? 0 : txt_nescab_tcamb.getValue());
        nescab_fecha = d_nescab_fecha.getValue();
        return new NotaESCab(nescab_id, nescab_key, nescab_tipnotaes, serie, emp_id, suc_id, nescab_nroped,
                nescab_fecha, nescab_glosa, nescab_situacion, nescab_est, nescab_ocind,
                nescab_ocnropedkey, nescab_tipdoc, nescab_nroserie, nescab_nrodoc,
                nescab_provid, nescab_cliid, nescab_moneda, nescab_tcamb,
                nescab_almori, nescab_almdes, nescab_costeo, nescab_despacho,
                nescab_nrodep, nescab_usuadd, nescab_usumod);
    }

    public Object generaRegistroDetalle() {
        String nescab_id = txt_nescab_id.getValue();
        String nescab_key = (String) (txt_nescab_id.getValue().isEmpty() ? "" : txt_nescab_id.getValue().substring(3, 10));
        String nescab_tipmov;
        String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
        String nesdet_ubiori;
        String nesdet_ubides;
        int emp_id = objUsuCredential.getCodemp();
        int suc_id = objUsuCredential.getCodsuc();
        if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
            nescab_tipmov = "E";
            nesdet_ubiori = "";
            nesdet_ubides = txt_ubicod.getValue();
        } else {
            nescab_tipmov = "S";
            nesdet_ubiori = txt_ubicod.getValue();
            nesdet_ubides = "";
        }
        String prov_id = txt_provid.getValue();
        String prov_proveedor = txt_proveedor.getValue();
        String pro_id = txt_proid.getValue();
        String pro_des = txt_prodes.getValue();
        String pro_desdes = txt_prodesdes.getValue();
        long pro_key = Long.parseLong(txt_proid.getValue());
        int nesdet_undpre = txt_undpre.getValue();
        double nesdet_precio = "001".equals(nescab_tipnotaes) ? txt_precio.getValue() : 0;
        double nesdet_cant = txt_canent.getValue() * nesdet_undpre;
        double nesdet_frac = txt_canfra.getValue();
        nesdet_cant = nesdet_cant + nesdet_frac;
        double gdetcant_cal = nesdet_cant / nesdet_undpre;
        String nesdet_proconv = txt_proconv.getValue();
        String ordcompra = txt_nescab_ocnropedkey.getValue().isEmpty() ? "0" : txt_nescab_ocnropedkey.getValue();
        nrooc = Long.parseLong(ordcompra);
        long nesdet_cantconv;
        nesdet_cantconv = nesdet_proconv.equals("*") ? (long) (gdetcant_cal / txt_unimas.getValue()) : (long) (txt_unimas.getValue() * gdetcant_cal);
        //calculos
        double nesdet_valafe, nesdet_pimpto, nesdet_vimpto, nesdet_valina, nesdet_pvta;
        /*if (txt_pimpto.getValue() == null) {
         nesdet_valafe = 0;
         nesdet_pimpto = 0;
         nesdet_vimpto = 0;
         nesdet_valina = (txt_valina.getValue() == null ? 0 : txt_valina.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal;
         nesdet_pvta = (txt_valina.getValue() == null ? 0 : txt_valina.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal;
         } else {
         nesdet_valafe = txt_valafe.getValue() == null || txt_valafe.getValue() == 0 ? 0 : (txt_valafe.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal;
         nesdet_pimpto = txt_pimpto.getValue() == null ? 0 : txt_pimpto.getValue();
         nesdet_vimpto = txt_vimpto.getValue() == null || txt_vimpto.getValue() == 0 ? 0 : ((txt_valafe.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal) * (txt_pimpto.getValue() / 100);
         nesdet_valina = 0;
         nesdet_pvta = txt_pvta.getValue() == null || txt_pvta.getValue() == 0 ? 0 : ((txt_valafe.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal) * (1 + (txt_pimpto.getValue() / 100));
         }*/
        if (txt_pimpto.getValue() == null) {
            nesdet_valafe = 0;
            nesdet_pimpto = 0;
            nesdet_vimpto = 0;
            nesdet_valina = nesdet_precio * gdetcant_cal;
            nesdet_pvta = nesdet_precio * gdetcant_cal;
        } else {
            //nesdet_valafe = txt_valafe.getValue() == null || txt_valafe.getValue() == 0 ? 0 : (txt_valafe.getValue() / objlstNotaES.getOcd_cantped()) * gdetcant_cal;
            nesdet_valafe = nesdet_precio * gdetcant_cal;
            nesdet_pimpto = txt_pimpto.getValue() == null ? 0 : txt_pimpto.getValue();
            nesdet_vimpto = nesdet_valafe == 0 ? 0 : (nesdet_valafe * (txt_pimpto.getValue() / 100));
            nesdet_valina = 0;
            nesdet_pvta = nesdet_valafe + nesdet_vimpto;
        }

        int nesdet_est = 1;
        String nesdet_almori = cb_nescab_almori.getSelectedItem().getValue();
        String nesdet_almdes = cb_nescab_almdes.getSelectedItem().getValue();
        double nesdet_cantfac = 0;
        double nesdet_peso = 0;
        double nesdet_cositem = 0;
        String nesdet_usuadd = objUsuCredential.getCuenta();
        String nesdet_usumod = objUsuCredential.getCuenta();
        return new NotaESDet(nescab_id, nescab_key, nescab_tipnotaes, emp_id, suc_id, nesdet_almori, nesdet_almdes, nescab_tipmov, prov_id, prov_proveedor,
                pro_id, pro_des, pro_desdes, pro_key, nesdet_ubiori, nesdet_ubides, nesdet_cant, nesdet_undpre, nesdet_cantconv, nesdet_proconv, nesdet_pimpto, nesdet_vimpto,
                nesdet_valafe, nesdet_valina, nesdet_pvta, nesdet_est, nesdet_cantfac,
                nesdet_peso, nesdet_cositem, nesdet_usuadd, nesdet_usumod);
    }

    public void obtenerTipoCambio() throws SQLException {
        int x;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String s_fecha_emision = sdf.format(d_nescab_fecha.getValue());

        if (cb_moneda.getSelectedIndex() != -1) {
            if ("NUEVOS SOLES".equals(cb_moneda.getValue())) {
                x = 1;
            } else if ("DOLARES AMERICANOS".equals(cb_moneda.getValue())) {
                x = 2;
            } else if ("EUROS".equals(cb_moneda.getValue())) {
                x = 3;
            } else {
                x = cb_moneda.getSelectedItem().getValue();
            }
            double i_tc = new DaoTipoCambio().obtieneTipoCambioComercial(s_fecha_emision, x);
            if (i_tc < 1) {
                Messagebox.show("No existe tipo de cambio para la fecha " + s_fecha_emision,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                txt_nescab_tcamb.setValue(0);
                cb_moneda.focus();
            } else {
                txt_nescab_tcamb.setValue(i_tc);
                txt_nescab_tipnotaes.setDisabled(false);
                //txt_nescab_tipnotaes.focus();
            }
        }
    }

    public void llenarCampos(NotaESCab objGCab) throws SQLException {
        txt_nescab_id.setValue(objGCab.getNescab_id());
        txt_nescab_tcamb.setValue(objGCab.getNescab_tcamb());
        d_nescab_fecha.setValue(new Timestamp(objGCab.getNescab_fecha().getTime()));
        d_nescab_hora.setValue(new Timestamp(objGCab.getNescab_fecadd().getTime()));
        int moneda = objGCab.getNescab_moneda();
        if (moneda > 0) {
            cb_moneda.setSelectedItem(Utilitarios.valorPorTexto1(cb_moneda, objGCab.getNescab_moneda()));
        } else {
            cb_moneda.setSelectedIndex(-1);
        }
        txt_nescab_nroped.setValue(objGCab.getNescab_nroped() == null ? "" : objGCab.getNescab_nroped());
        txt_nescab_tipnotaes.setValue(objGCab.getNescab_tipnotaes());
        txt_nomtipnotaes.setValue(objGCab.getNotaes());
        cb_serie.setSelectedItem(Utilitarios.textoPorTexto(cb_serie, objGCab.getNescab_serie()));
        if (txt_nescab_tipnotaes.getValue().equals("001")) {
            txt_nescab_ocnropedkey.setValue(Utilitarios.lpad(String.valueOf(objGCab.getNescab_ocnropedkey()), 10, "0"));
            txt_listapreid.setValue(Utilitarios.lpad(objGCab.getListaprecio(), 4, "0"));
            objListaPrecio = new DaoListaPrecios().getListaPrecio(Integer.parseInt(objGCab.getListaprecio()), 1);
            txt_listapredes.setValue(objListaPrecio.getLp_des());
        } else {
            txt_nescab_ocnropedkey.setValue("");
        }
        String tipo_doc = objGCab.getNescab_tipdoc();
        if (!tipo_doc.isEmpty()) {
            cb_nescab_tipdoc.setSelectedItem(Utilitarios.textoPorTexto(cb_nescab_tipdoc, objGCab.getNescab_tipdoc()));
        } else {
            cb_nescab_tipdoc.setSelectedIndex(-1);
            cb_nescab_tipdoc.setValue("");
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
        txt_canfra.setValue(Math.round(objNotaESDetalle.getNesdet_cant() % objNotaESDetalle.getNesdet_undpre()));
        if (txt_undpre.getValue() / 2.0 <= txt_canfra.getValue()) {
            txt_canent.setValue(Math.round(objNotaESDetalle.getNesdet_cant() / objNotaESDetalle.getNesdet_undpre()) - 1);
        } else {
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
        int stockSeguridad = "001".equals(nescab_tipnotaes) ? objProductos.getPro_scknofact() : 0;
        if ((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) > stockSeguridad) {
            //txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() - (objProductos.getPro_scknofact())) / objNotaESDetalle.getNesdet_undpre()));
            txt_stock.setValue(String.valueOf((objParametrosSalida.getCantStocks() / objProductos.getPro_presminven()) - stockSeguridad));
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
        txt_listapreid.setValue("");
        txt_listapredes.setValue("");
        cb_nescab_almori.setSelectedIndex(-1);
        cb_nescab_almdes.setSelectedIndex(-1);
        //limpiamos la lista
        objlstOrdCompDet = null;
        lst_notes_det.setModel(objlstOrdCompDet);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
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

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevoes.setDisabled(b_valida1);
        tbbtn_btn_editares.setDisabled(b_valida1);
        tbbtn_btn_eliminares.setDisabled(b_valida1);
        tbbtn_btn_deshaceres.setDisabled(b_valida2);
        tbbtn_btn_guardares.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida1) {
        cb_moneda.setDisabled(b_valida1);
        d_nescab_fecha.setDisabled(b_valida1);
        d_nescab_hora.setDisabled(b_valida1);
        cb_serie.setDisabled(b_valida1);
        txt_nescab_tipnotaes.setDisabled(b_valida1);
        btn_nescab_ocnropedkey.setDisabled(b_valida1);
        cb_nescab_tipdoc.setDisabled(b_valida1);
        txt_nescab_nroserie.setDisabled(b_valida1);
        txt_nescab_nrodoc.setDisabled(b_valida1);
        txt_nescab_provid.setDisabled(b_valida1);
        txt_nescab_cliid.setDisabled(b_valida1);
        txt_nescab_glosa.setDisabled(b_valida1);
        cb_nescab_almdes.setDisabled(b_valida1);
    }

    public void habilitaCamposCabecera(boolean b_valida1) {
        cb_nescab_tipdoc.setDisabled(b_valida1);
        txt_nescab_nroserie.setDisabled(b_valida1);
        txt_nescab_nrodoc.setDisabled(b_valida1);
        txt_nescab_provid.setDisabled(b_valida1);
        txt_nescab_cliid.setDisabled(b_valida1);
        txt_nescab_glosa.setDisabled(b_valida1);
        btn_nescab_ocnropedkey.setDisabled(b_valida1);
    }

    public void habilitaCamposDetalle(boolean b_valida1) {
        txt_proid.setDisabled(b_valida1);
        txt_canent.setDisabled(b_valida1);
        txt_canfra.setDisabled(b_valida1);
        txt_ubicod.setDisabled(b_valida1);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public String VerificarCabecera() {
        String valida;
        if (d_nescab_fecha.getValue() == null) {
            valida = "Fecha de Emisión";
        } else if (cb_moneda.getSelectedIndex() == -1) {
            valida = "Moneda";
        } else if (txt_nescab_tcamb.getValue() <= 0) {
            valida = "Tipo de Cambio";
        } else if (txt_nescab_tipnotaes.getValue().isEmpty()) {
            valida = "Tipo de Nota E/S";
            txt_nescab_tipnotaes.focus();
        } else {
            if (cb_moneda.getSelectedIndex() == -1) {
                valida = "Tipo de Moneda";
            } else if (txt_nescab_tcamb.getValue() == null) {
                valida = "Tipo de Cambio";
            } else if (d_nescab_fecha.getValue() == null) {
                valida = "Fecha de Nota E/S";
            } else if (txt_nomtipnotaes.getValue().isEmpty()) {
                valida = "Descripción del Tipo de Nota E/S";
            } else if (cb_serie.getSelectedIndex() == -1 || cb_serie.getValue().equals("") && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                valida = "Serie";
            } else if (txt_nescab_nrodoc.getValue().isEmpty() && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                valida = "Numero de documento de referencia";
            } else if (cb_nescab_almori.getSelectedIndex() == -1) {
                valida = "Almacen Origen";
            } else if (cb_nescab_almdes.getSelectedIndex() == -1) {
                valida = "Almacen Destino";
            } else if ("001".equals(txt_nescab_tipnotaes.getValue())) {//Facturas
                if (txt_nescab_ocnropedkey.getValue().isEmpty()) {
                    valida = "Numero de O/C";
                } else if (txt_nescab_provid.getValue().isEmpty()) {
                    valida = "Codigo de Proveedor";
                } else if (txt_nescab_provrazsoc.getValue().isEmpty()) {
                    valida = "Razon Social del Proveedor";
                } else if (cb_nescab_tipdoc.getSelectedIndex() == -1 && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Seleccione tipo de Documento de Referencia";
                } else if (txt_nescab_nroserie.getValue().isEmpty() && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Ingrese Numero de Serie de Referencia";
                } else if (txt_nescab_nrodoc.getValue().isEmpty() && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Ingrese Numero de Documento de Referencia";
                } else {
                    valida = "";
                }
            } else if ("002".equals(txt_nescab_tipnotaes.getValue()) || "004".equals(txt_nescab_tipnotaes.getValue())
                    || "005".equals(txt_nescab_tipnotaes.getValue()) || "007".equals(txt_nescab_tipnotaes.getValue())
                    || "008".equals(txt_nescab_tipnotaes.getValue()) || "009".equals(txt_nescab_tipnotaes.getValue())
                    || "010".equals(txt_nescab_tipnotaes.getValue()) || "011".equals(txt_nescab_tipnotaes.getValue())
                    || "012".equals(txt_nescab_tipnotaes.getValue()) || "014".equals(txt_nescab_tipnotaes.getValue())
                    || "015".equals(txt_nescab_tipnotaes.getValue())) {
                if (txt_nescab_cliid.getValue().isEmpty()) {
                    valida = "Codigo de Cliente";
                } else if (txt_nescab_clinom.getValue().isEmpty()) {
                    valida = "Nombres de Cliente";
                } else if (cb_moneda.getSelectedIndex() == -1) {
                    valida = "Tipo de Moneda";
                } else if (txt_nescab_tcamb.getValue() == null) {
                    valida = "Tipo de Cambio";
                } else if (d_nescab_fecha.getValue() == null) {
                    valida = "Fecha de Nota E/S";
                } else if (txt_nomtipnotaes.getValue().isEmpty()) {
                    valida = "Descripción del Tipo de Nota E/S";
                } else if (cb_serie.getSelectedIndex() == -1 && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Serie";
                } else if (txt_nescab_nrodoc.getValue().isEmpty() && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Numero de Referencia";
                } else if (cb_nescab_almori.getSelectedIndex() == -1) {
                    valida = "Almacen Origen";
                } else if (cb_nescab_almdes.getSelectedIndex() == -1) {
                    valida = "Almacen Destino";
                } else {
                    valida = "";
                }
            } else if (!"006".equals(txt_nescab_tipnotaes.getValue())) {
                if (cb_nescab_tipdoc.getSelectedIndex() == -1 && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Tipo de Documento";
                } else {
                    valida = "";
                }
            } else {
                if (cb_moneda.getSelectedIndex() == -1) {
                    valida = "Tipo de Moneda";
                } else if (txt_nescab_tcamb.getValue() == null) {
                    valida = "Tipo de Cambio";
                } else if (d_nescab_fecha.getValue() == null) {
                    valida = "Fecha de Nota E/S";
                } else if (txt_nomtipnotaes.getValue().isEmpty()) {
                    valida = "Descripción del Tipo de Nota E/S";
                } else if (cb_serie.getSelectedIndex() == -1 && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Serie";
                } else if (cb_nescab_tipdoc.getSelectedIndex() == -1 && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Documento de referencia";
                } else if (txt_nescab_nrodoc.getValue().isEmpty() && !"015".equals(txt_nescab_tipnotaes.getValue())) {
                    valida = "Numero de Referencia";
                } else if (cb_nescab_almori.getSelectedIndex() == -1) {
                    valida = "Almacen Origen";
                } else if (cb_nescab_almdes.getSelectedIndex() == -1) {
                    valida = "Almacen Destino";
                } else {
                    valida = "";
                }
            }
        }
        return valida;
    }

    public String verificarDetalle() {
        String valida;
        if (txt_proid.getValue().isEmpty() || txt_prodes.getValue().isEmpty()) {
            valida = "Ingrese Código del Producto";
            foco = "producto";
        } else if (txt_canent.getValue() == null) {
            valida = "Ingrese Cantidad Entera";
            foco = "cantent";
        } else if (txt_canfra.getValue() == null) {
            valida = "Ingrese Cantidad Fracción";
            foco = "cantfrac";
        } else if (txt_canent.getValue() == 0 && txt_canfra.getValue() == 0.0) {
            valida = "Las cantidades entera y fracción no pueden ser 0";
        } else if (txt_canent.getValue() < 0) {
            valida = "Cantidad entera no puede ser negativo";
            foco = "cantent";
        } else if (txt_canfra.getValue() < 0) {
            valida = "Cantidad Fraccion no puede ser negativo";
            foco = "cantfrac";
        } else if (txt_ubicod.getValue().isEmpty()) {
            valida = "Ingrese ubicación";
            foco = "ubicacion";
        } else {
            valida = "";
        }
        return valida;
    }

    public String verificaCantOCvsNES() {
        String valida;
        int nesdet_undpre = txt_undpre.getValue();
        double nesdet_cant = txt_canent.getValue() * nesdet_undpre;
        double nesdet_frac = txt_canfra.getValue();
        nesdet_cant = nesdet_cant + nesdet_frac;
        if (nesdet_cant > objlstNotaES.getOcd_cantped()) {
            valida = "Ingrese Cantidad Menor o Igual al Pedido en O/C";
            txt_canent.focus();
        } else {
            valida = "";
        }
        return valida;
    }

    public String verificaStock() throws SQLException {
        String valida = "";
        double nesdet_cant = txt_canent.getValue();
        double nesdet_frac = txt_canfra.getValue();
        double stock_disp = Double.parseDouble(txt_stock.getValue());
        int nesdet_undpre = txt_undpre.getValue();
        double fraccion = Math.rint((nesdet_frac / nesdet_undpre) * 100) / 100;
        nesdet_cant = nesdet_cant + fraccion;
        String nescab_tipmov;
        String nescab_tipnotaes = txt_nescab_tipnotaes.getValue();
        if ("001".equals(nescab_tipnotaes) || "002".equals(nescab_tipnotaes) || "003".equals(nescab_tipnotaes)
                || "004".equals(nescab_tipnotaes) || "005".equals(nescab_tipnotaes) || "006".equals(nescab_tipnotaes)
                || "008".equals(nescab_tipnotaes) || "009".equals(nescab_tipnotaes) || "011".equals(nescab_tipnotaes)
                || "012".equals(nescab_tipnotaes) || "013".equals(nescab_tipnotaes)) {
            nescab_tipmov = "E";
        } else {
            nescab_tipmov = "S";
        }
        if (nescab_tipmov.equals("S")) {
            if (stock_disp == 0) {
                valida = "No hay productos en almacen";
            } else {
                if (nesdet_cant > stock_disp) {
                    valida = "Solo hay " + stock_disp + " en stock";
                    txt_canent.setValue(stock_disp);
                } else if (nesdet_cant < stock_disp) {
                    valida = "";
                    txt_stock.setValue("" + (stock_disp - nesdet_cant));
                }
            }
        }
        return valida;
    }

    public void LlenaDataES() throws SQLException {
        d_nescab_fecha.setValue(Utilitarios.hoyAsFecha());
        d_nescab_hora.setValue(new Date());
    }

    public void habilitaCajas(String tipo_notaes) {
        txt_nescab_glosa.setDisabled(false);
        cb_nescab_tipdoc.setDisabled(false);
        txt_nescab_nroserie.setDisabled(false);
        txt_nescab_nrodoc.setDisabled(false);
        if ("001".equals(tipo_notaes) && cb_serie.getSelectedItem().getValue().equals("101")) {
            btn_nescab_ocnropedkey.setDisabled(false);
            btn_nescab_ocnropedkey.setFocus(true);
            txt_nescab_glosa.setDisabled(true);
        } else if ("002".equals(tipo_notaes) || "004".equals(tipo_notaes) || "005".equals(tipo_notaes)
                || "007".equals(tipo_notaes) || "008".equals(tipo_notaes) || "009".equals(tipo_notaes)
                || "010".equals(tipo_notaes) || "011".equals(tipo_notaes) || "012".equals(tipo_notaes)
                || "014".equals(tipo_notaes) || "015".equals(tipo_notaes) || "016".equals(tipo_notaes)) {
            txt_nescab_cliid.setDisabled(false);
            btn_nescab_ocnropedkey.setDisabled(true);
        }
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstNotaESCab = null;
        objlstNotaESCab = new ListModelList<NotaESCab>();
        lst_notes.setModel(objlstNotaESCab);
    }

    public void limpiarListaSerie() {
        //limpio mi lista serie
        objlstNumeracion = null;
        objlstNumeracion = new ListModelList<Numeracion>();
        cb_serie.setValue("");
        cb_serie.setModel(objlstNumeracion);
        cb_nescab_tipdoc.setDisabled(false);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validaBusqueda(InputEvent event) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verificar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
