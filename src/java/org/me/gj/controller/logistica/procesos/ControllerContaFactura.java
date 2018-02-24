package org.me.gj.controller.logistica.procesos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.controller.contabilidad.mantenimiento.DaoTipDoc;
import org.me.gj.controller.cxc.mantenimiento.DaoMoneda;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.mantenimiento.DaoProductos;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.contabilidad.mantenimiento.TipDoc;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
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

public class ControllerContaFactura extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_costafactura;
    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevoes, tbbtn_btn_editares, tbbtn_btn_eliminares, tbbtn_btn_guardares, tbbtn_btn_deshaceres, tbbtn_btn_contabilizar;
    @Wire
    Listbox lst_notes, lst_notes_det;
    @Wire
    Textbox /*txt_nestipnotaes, txt_ntipnotaes,*/ txt_usuadd, txt_usumod,
            txt_nescab_provid, txt_nescab_provrazsoc, txt_nescab_glosa, txt_nescab_tipnotaes, txt_nomtipnotaes,
            txt_nescab_id, txt_nescab_nroserie, txt_nescab_nrodoc, txt_proid, txt_prodes, txt_stock, txt_nescab_ocnropedkey, txt_listapreid, txt_listapredes,
            txt_nescab_cliid, txt_nescab_clinom, txt_nescab_nrodep, txt_nescab_nroped, txt_provid,
            txt_proveedor, txt_prodesdes, txt_ubicod, txt_ubides, txt_usuadd_det, txt_usumod_det, txt_proconv,
            txt_totafecto, txt_totinafecto, txt_totigv, txt_totventa;
    @Wire
    Datebox d_nescab_fecha, d_filtro_fecha, txt_fecmod_det, txt_fecadd_det, d_fecadd, d_fecmod;
    @Wire
    Intbox txt_prokey, txt_undpre, txt_unimas;
    @Wire
    Timebox d_nescab_hora;
    @Wire
    Checkbox chk_despacho, chk_estado, chk_selecAll;
    @Wire
    Doublebox txt_nescab_tcamb, txt_canent, txt_canfra,
            txt_pimpto, txt_vimpto, txt_valafe, txt_valina, txt_pvta;
    @Wire
    Combobox cb_periodo, cb_situacion, cb_notaes, cb_serie, cb_moneda, cb_nescab_almori, cb_nescab_almdes, cb_nescab_tipdoc;
    @Wire
    Button btn_consultar, btn_nescab_ocnropedkey, btn_despacho;
    //Instancias de Objetos    
    ListModelList<OrdCompDet> objlstOrdCompDet = new ListModelList<OrdCompDet>();
    ListModelList<ManPer> objlstManPeriodos;
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
    //DaoCosteo objDaoCosteo = new DaoCosteo();
    DaoUbicaciones objdaoubicaciones = new DaoUbicaciones();
    DaoManNotaES objDaoManNotaES = new DaoManNotaES();
    DaoAlmacenes objDaoAlmacen = new DaoAlmacenes();
    DaoTipDoc objDaoTipDoc = new DaoTipDoc();
    DaoMoneda objDaoMonedas = new DaoMoneda();
    DaoNumeracion objDaoNumeracion = new DaoNumeracion();
    DaoNotaES objDaoNotaES;
    DaoOrdCom objDaoOrdComp;
    DaoProductos objDaoProducto;
    ListaPrecio objListaPrecio = new ListaPrecio();
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
    String fechaActual;
    int i_sel;
    int valor;
    long nrooc;
    int i_selCab;
    int i_empid;
    int i_sucid;
    String foco = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMM");
    public static boolean bandera = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerContaFactura.class);

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
        /*objlstManPeriodos = new ListModelList<ManPer>();
         objlstManPeriodos = objDaoManPer.listaPeriodos(1);
         cb_periodo.setModel(objlstManPeriodos);
         objlstManPeriodos.add(new ManPer("", ""));
         cb_periodo.setValue(Utilitarios.periodoActual());
         d_filtro_fecha.setValue(Utilitarios.hoyAsFecha());
         //txt_nestipnotaes.focus();
         cb_periodo.setValue(Utilitarios.periodoActual());
         d_filtro_fecha.setValue(Utilitarios.hoyAsFecha());*/

        objlstManPeriodos = new ListModelList();
        objlstManPeriodos = objDaoManPer.listaPeriodos(0);
        fechaActual = Utilitarios.hoyAsString();
        Date fecha = new Date();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(objlstManPeriodos);
        cb_periodo.setValue(periodo);

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
        cb_periodo.focus();
        cb_periodo.select();
        //consulta inicial
        /*objlstNotaESCab = objDaoNotaES.listaNotaESCab();*/
        //objlstNotaESCab = objDaoNotaES.listaTotalesNotaES();
        lst_notes.setModel(objlstNotaESCab);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10214000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Contabilizar Nota E/S  con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado en Procesos Contabilizar Nota E/S con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_contabilizar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de Costeo una Nota de E/S Manual");
        } else {
            tbbtn_btn_contabilizar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de Contabilizar Nota E/S");
        }
    }

    @Listen("onClick=#btn_consultar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        Date fecha_emisioni = d_filtro_fecha.getValue();
        String f_emisioni, f_periodo;
        if (fecha_emisioni == null) {
            f_emisioni = "";
            f_periodo = "";
        } else {
            f_emisioni = sdf.format(d_filtro_fecha.getValue());
            f_periodo = sdfm.format(d_filtro_fecha.getValue());
        }
        if (cb_periodo.getValue().equals(f_periodo) || f_emisioni.equals("")) {

            //String s_consulta = txt_nestipnotaes.getValue().toUpperCase().trim();
            //String s_periodo = cb_periodo.getValue();
            //String s_situacion;
            //s_situacion = cb_situacion.getSelectedIndex() == -1 ? "" : cb_situacion.getSelectedItem().getValue().toString();
            objlstNotaESCab = new ListModelList<NotaESCab>();
            //objlstNotaESCab = objDaoCosteo.BusquedaNotaES(s_periodo, fecha, s_consulta, 1, s_situacion);
            objlstNotaESCab = objDaoNotaES.BusquedaContaFactura(f_emisioni);

            if (objlstNotaESCab.isEmpty()) {
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + f_emisioni + " que retorno 0 registros");
            } else {
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + f_emisioni + " que retorno " + objlstNotaESCab.getSize() + " registro(s)");
            }
            if (objlstNotaESCab.getSize() > 0) {
                lst_notes.setModel(objlstNotaESCab);
            } else {
                objlstNotaESCab = null;
                lst_notes.setModel(objlstNotaESCab);
                Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }

        } else {
            Messagebox.show("La fecha no pertenece al periodo seleccionado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }

        limpiarCampos();
        objlstNotaESDet = null;
        lst_notes_det.setModel(objlstNotaESDet);
        //limpiarCamposDetalle();
        limpiaAuditoria();
    }

    @Listen("onSelect=#lst_notes")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCampos();
        //limpiarCamposDetalle();
        objNotaESCab = (NotaESCab) lst_notes.getSelectedItem().getValue();
        if (objNotaESCab == null) {
            objNotaESCab = objlstNotaESCab.get(i_sel + 1);
        }
        i_sel = lst_notes.getSelectedIndex();
        objlstNotaESDet = objDaoNotaES.listaNotaESDet(objNotaESCab.getNescab_id(), 1);
        lst_notes_det.setModel(objlstNotaESDet);
        //cargamos los campos
        llenarCampos(objNotaESCab);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objNotaESCab.getNescab_id());
    }

    @Listen("onSelect=#lst_notes_det")
    public void seleccionaRegistroDetalle() throws SQLException {
        //limpiamos los datos anteriores
        //limpiarCamposDetalle();
        objNotaESDet = (NotaESDet) lst_notes_det.getSelectedItem().getValue();
        if (objNotaESDet == null) {
            objNotaESDet = objlstNotaESDet.get(i_sel + 1);
        }
        i_sel = lst_notes_det.getSelectedIndex();
        //cargamos los campos
        //llenarCamposDetalle(objNotaESDet);
        //habilitaCamposDetalle(true);

        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con glosa " + objOrdCompDet.getOcd_glosa());
    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstNotaESCab == null || objlstNotaESCab.isEmpty()) {
            Messagebox.show("No hay orden de compra", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstNotaESCab.getSize(); i++) {
                //if (objlstNotaESCab.get(i).getOc_situacion() == 1) {
                objlstNotaESCab.get(i).setValSelec(chk_selecAll.isChecked());
                //}
            }
            lst_notes.setModel(objlstNotaESCab);
        }
    }

    //Eventos Secundarios - Validaciones 
    /*@Listen("onOK=#txt_nestipnotaes")
     public void lov_notaes() throws SQLException {
     if (bandera == false) {
     bandera = true;
     if (txt_nestipnotaes.getValue().isEmpty()) {
     Map mapeo = new HashMap();
     modoEjecucion = "ContaFactura";
     mapeo.put("txt_nescab_tipnotaes", txt_nestipnotaes);
     mapeo.put("txt_nomtipnotaes", txt_ntipnotaes);
     mapeo.put("validaBusqueda", modoEjecucion);
     mapeo.put("tipo", "1");
     mapeo.put("controlador", "ControllerContaFactura");
     Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovNotaES.zul", null, mapeo);
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
     }*/
    //Eventos Otros
    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    /*public void habilitaBotonesDetalle(boolean b_valida1, boolean b_valida2) {
     tbbtn_btn_nuevoes.setDisabled(b_valida1);
     tbbtn_btn_editares.setDisabled(b_valida1);
     tbbtn_btn_eliminares.setDisabled(b_valida1);
     tbbtn_btn_deshaceres.setDisabled(b_valida2);
     tbbtn_btn_guardares.setDisabled(b_valida2);
     }*/
    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
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
        txt_listapreid.setValue("");
        txt_listapredes.setValue("");
        //limpiamos la lista
        objlstOrdCompDet = null;
        lst_notes_det.setModel(objlstOrdCompDet);
    }

    /*public void limpiarCamposDetalle() {
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
     }*/
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
        //totales
        txt_totafecto.setValue(objGCab.getS_vafecto());
        txt_totinafecto.setValue(objGCab.getS_vinafecto());
        txt_totigv.setValue(objGCab.getS_vimpto());
        txt_totventa.setValue(objGCab.getS_pventa());
    }

    /*public void llenarCamposDetalle(NotaESDet objNotaESDetalle) throws SQLException {
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
     }*/

    /*public void LlenaCamposDetalle(OrdCompDet objNES) throws SQLException {
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
     }*/
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
}
