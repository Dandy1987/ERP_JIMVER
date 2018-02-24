package org.me.gj.controller.cxc.mantenimiento;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.me.gj.controller.distribucion.mantenimiento.DaoHorario;
import org.me.gj.controller.facturacion.mantenimiento.DaoZonas;
import org.me.gj.controller.facturacion.procesos.ControllerProcPedPend;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Categoria;
import org.me.gj.model.cxc.mantenimiento.CliDireccion;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.CliGarantia;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.cxc.mantenimiento.FormaPago;
import org.me.gj.model.cxc.mantenimiento.Giro;
import org.me.gj.model.cxc.mantenimiento.Moneda;
import org.me.gj.model.cxc.mantenimiento.TipoDocumento;
import org.me.gj.model.distribucion.mantenimiento.Horario;
import org.me.gj.model.facturacion.mantenimiento.Canal;
import org.me.gj.model.facturacion.mantenimiento.Zona;
import org.me.gj.model.logistica.mantenimiento.Condicion;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
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

public class ControllerCliente extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listacliente, tab_mantenimiento, tab_direccion, tab_telefono;//, tab_financiero, tab_garantia;
    @Wire
    Listbox lst_cliente,
            lst_direccion,
            //lst_telefono,
            //lst_financiero,
            lst_garantia;
    @Wire
    Textbox txt_busqueda, txt_cliid, txt_cliapepat, txt_cliapemat, txt_clinombre, txt_clirazsoc, txt_clidni, txt_cliemail1, txt_cliemail2, txt_clidirweb,
            txt_clidir_direccion, txt_clidir_ref, txt_clidir_zona, txt_clidir_zonadesc, txt_clidir_vendedor, txt_clidir_idubigeo, txt_clidir_ubigeo,
            txt_clidir_horario, txt_clidir_usuadd, txt_clidir_usumod,
            //txt_clitel_usuadd, txt_clitel_usumod,
            //txt_clifin_usuadd, txt_clifin_usumod,
            txt_cligar_obs, txt_cligar_usuadd, txt_cligar_usumod,
            txt_clidir_idhorario, txt_clidir_idpost, txt_clidir_postal, txt_clidir_idgiro, txt_clidir_giro,
            txt_usuadd, txt_usumod;
    @Wire
    Longbox txt_cliruc,
            txt_clidir_id,
            txt_clitel_id, txt_clitel_telef1, txt_clitel_telef2, txt_clitel_movil,
            txt_clifin_id, /*txt_clifin_limcred,*/
            txt_cligar_id, txt_cligar_monto;
    @Wire
    Combobox cb_busqueda, cb_tipodoc, cb_categoria, cb_moneda, cb_lprecio, cb_condicion,
            cb_garantia, cb_canal, cb_fpago, cb_estado;
    @Wire
    Intbox txt_clifin_limdoc, txt_clidoccor;
    @Wire
    Doublebox txt_clicredcor, txt_cli_dscto, txt_clifin_limcred, db_saldocredcorp, txt_saldodoccorp;
    @Wire
    Datebox txt_clifecnac,
            txt_clidir_fecadd, txt_clidir_fecmod,
            //txt_clitel_fecadd, txt_clitel_fecmod,
            //txt_clifin_fecadd, txt_clifin_fecmod,
            txt_cligar_fecadd, txt_cligar_fecmod,
            d_fecadd, d_fecmod;
    @Wire
    Checkbox chk_cliestado, chk_busest, chk_cliperju,
            chk_clidir_estado, chk_clidir_default,
            chk_clitel_estado,
            chk_clifin_estado, chk_cli_factura,
            chk_cligar_estado,
            chk_cli_emprel, chk_cli_perc;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir,
            tbbtn_btn_nuevod, tbbtn_btn_editard, tbbtn_btn_eliminard, tbbtn_btn_guardard, tbbtn_btn_deshacerd,
            //tbbtn_btn_nuevot, tbbtn_btn_editart, tbbtn_btn_eliminart, tbbtn_btn_guardart, tbbtn_btn_deshacert,
            //tbbtn_btn_nuevof, tbbtn_btn_editarf, tbbtn_btn_eliminarf, tbbtn_btn_guardarf, tbbtn_btn_deshacerf,
            tbbtn_btn_nuevog, tbbtn_btn_editarg, tbbtn_btn_eliminarg, tbbtn_btn_guardarg, tbbtn_btn_deshacerg;
    @Wire
    Button btn_mant_direccion;
    //Instancias de Objetos
    ListModelList<Canal> objlstCanal;
    ListModelList<Moneda> objlstMoneda;
    ListModelList<Categoria> objlstCategoria;
    ListModelList<Condicion> objlstCondicion;
    ListModelList<TipoDocumento> objlstTipoDocumento;
    ListModelList<FormaPago> objlstFormaPago;
    ListModelList<ListaPrecio> objlstListaPrecio;
    ListModelList<Cliente> objlstCliente;
    ListModelList<CliDireccion> objlstCliDireccion;
    //ListModelList<CliFinanciero> objlstCliFinanciero, objlstCliFinancieroCorp;
    ListModelList<CliGarantia> objlstCliGarantia;
    //ListModelList<CliTelefono> objlstCliTelefono;
    ListModelList<CliDireccion> objlstEliDireccion;
    ListModelList<CliFinanciero> objlstEliFinanciero;
    ListModelList<CliGarantia> objlstEliGarantia;
    ListModelList<CliTelefono> objlstEliTelefono;
    DaoCliente objDaoCliente;
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    Cliente objCliente;
    CliDireccion objCliDireccion;
    CliFinanciero objCliFinanciero;
    CliGarantia objCliGarantia;
    CliTelefono objCliTelefono;
    Zona objZona;
    Utilitarios objUtil = new Utilitarios();
    ParametrosSalida objParamSalida;

    //Variables publicas
    String s_estado = "Q";
    String s_estado_direccion = "Q";
    String s_estado_telefono = "Q";
    String s_estado_garantia = "Q";
    String s_estado_financiero = "Q";
    String s_codigo_telefono = "";
    String s_codigo_garantia = "";
    String s_codigo_financiero = "";
    String s_mensaje = "";
    String modoEjecucion;
    String s_cliid = "";
    String regreso = "";
    int i_sel, emp_id, suc_id;
    int flag = 3;
    public static boolean bandera = false;
    public static boolean banderadir = false;
    public static boolean banderatel = false;
    public static boolean banderafin = false;
    public static boolean banderagar = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerCliente.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_cliente")
    public void llenaRegistros() throws SQLException {
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        //carga listas
        objDaoCliente = new DaoCliente();
        objlstTipoDocumento = objDaoCliente.listaTipoDocumento();
        cb_tipodoc.setModel(objlstTipoDocumento);
        objlstCategoria = objDaoCliente.listaCategoria();
        cb_categoria.setModel(objlstCategoria);
        objlstMoneda = objDaoCliente.listaMoneda();
        cb_moneda.setModel(objlstMoneda);
        objlstListaPrecio = objDaoCliente.listaPrecio();
        cb_lprecio.setModel(objlstListaPrecio);
        objlstCondicion = objDaoCliente.listaCondicion();
        cb_condicion.setModel(objlstCondicion);
        objlstCanal = objDaoCliente.listaCanal();
        cb_canal.setModel(objlstCanal);
        objlstFormaPago = objDaoCliente.listaFPago();
        cb_fpago.setModel(objlstFormaPago);

        objlstCliente = new ListModelList<Cliente>();
        if (ControllerProcPedPend.controlador.equals("ControllerProcPedPend")) {
            ControllerProcPedPend.controlador = "";
            s_cliid = ControllerProcPedPend.s_cliid;
            objlstCliente = objDaoCliente.busquedaClientes(emp_id, suc_id, 1, s_cliid, 1);
        } else {
            objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
        }
        lst_cliente.setModel(objlstCliente);
        //carga clientes
        objlstCliente = new ListModelList<Cliente>();
        objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
        lst_cliente.setModel(objlstCliente);
        txt_clifecnac.setDisabled(true);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(20101020, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Cliente con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Cliente con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Cliente");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Cliente");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Cliente");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Cliente");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Cliente");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Cliente");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Cliente");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Cliente");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3; // todos
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;//activos
        } else {
            i_estado = 2;//inactivos
        }
        objlstCliente = new ListModelList<Cliente>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        //objlstCliente = objDaoCliente.busquedaClientes(emp_id, suc_id, i_seleccion, s_consulta, i_estado);
        objlstCliente = objDaoCliente.busquedaClientes(emp_id, suc_id, i_seleccion, s_consulta, i_estado);
        if (objlstCategoria.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstCategoria.getSize() + " registro(s)");
        }
        if (objlstCliente.getSize() > 0) {
            lst_cliente.setModel(objlstCliente);
        } else {
            objlstCliente = null;
            lst_cliente.setModel(objlstCliente);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        limpiaAuditoria();
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onSelect=#lst_cliente")
    public void seleccionaRegistro() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCampos();
        limpiarCamposDireccion();
        limpiarCamposTelefono();
        limpiarCamposFinanciero();
        limpiarCamposGarantia();
        objCliente = new Cliente();
        objCliente = (Cliente) lst_cliente.getSelectedItem().getValue();
        if (objCliente == null) {
            objCliente = objlstCliente.get(i_sel + 1);
        }
        i_sel = lst_cliente.getSelectedIndex();
        //cargamos las listas con los direccion y telefonos de los clientes
        objlstCliDireccion = objDaoCliente.listaDireccion(objCliente.getCli_id(), emp_id, suc_id);
        lst_direccion.setModel(objlstCliDireccion);

        //objlstCliTelefono = objDaoCliente.listaTelefono(objCliente.getCli_id());
        //lst_telefono.setModel(objlstCliTelefono);
        //objlstCliFinanciero = objDaoCliente.listaFinanciero(objCliente.getCli_id(), emp_id, suc_id);
        //lst_financiero.setModel(objlstCliFinanciero);
        objlstCliGarantia = objDaoCliente.listaGarantia(objCliente.getCli_id(), emp_id, suc_id);
        lst_garantia.setModel(objlstCliGarantia);
        //lista financiero en todas las empresas
        //objlstCliFinancieroCorp = objDaoCliente.listaFinanciero(objCliente.getCli_id(), emp_id, suc_id);
        //cargamos los campos

        objCliTelefono = objDaoCliente.getClienteTel(objCliente.getCli_key());
        objCliFinanciero = objDaoCliente.getClienteFinan(objCliente.getCli_key(), emp_id, suc_id);

        llenarCampos(objCliente);
        if (objCliTelefono != null) {
            llenarCamposTelefono(objCliTelefono);
        }
        if (objCliFinanciero != null) {
            llenarCamposFinanciero(objCliFinanciero);
        }
        //solo lectura
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCliente.getCli_id());
    }

    @Listen("onSelect=#lst_direccion")
    public void seleccionaRegistroDireccion() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCamposDireccion();
        objCliDireccion = (CliDireccion) lst_direccion.getSelectedItem().getValue();
        if (objCliDireccion == null) {
            objCliDireccion = objlstCliDireccion.get(i_sel + 1);
        }
        i_sel = lst_direccion.getSelectedIndex();
        //cargamos los campos
        llenarCamposDireccion(objCliDireccion);
        validaZona();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCliDireccion.getClidir_id());
    }

    /*@Listen("onSelect=#lst_telefono")
     public void seleccionaRegistroTelefono() throws SQLException {
     //limpiamos los datos anteriores
     limpiarCamposTelefono();
     objCliTelefono = new CliTelefono();
     objCliTelefono = (CliTelefono) lst_telefono.getSelectedItem().getValue();
     if (objCliTelefono == null) {
     objCliTelefono = objlstCliTelefono.get(i_sel + 1);
     }
     i_sel = lst_telefono.getSelectedIndex();
     //cargamos los campos
     llenarCamposTelefono(objCliTelefono);
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCliTelefono.getClitel_id());
     }*/

    /*@Listen("onSelect=#lst_financiero")
     public void seleccionaRegistroFinanciero() throws SQLException {
     //limpiamos los datos anteriores
     limpiarCamposFinanciero();
     objCliFinanciero = new CliFinanciero();
     objCliFinanciero = (CliFinanciero) lst_financiero.getSelectedItem().getValue();
     if (objCliFinanciero == null) {
     objCliFinanciero = objlstCliFinanciero.get(i_sel + 1);
     }
     i_sel = lst_financiero.getSelectedIndex();
     //cargamos los campos
     llenarCamposFinanciero(objCliFinanciero);
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCliFinanciero.getClifin_id());
     }*/
    @Listen("onSelect=#lst_garantia")
    public void seleccionaRegistroGarantia() throws SQLException {
        //limpiamos los datos anteriores
        limpiarCamposGarantia();
        objCliGarantia = new CliGarantia();
        objCliGarantia = (CliGarantia) lst_garantia.getSelectedItem().getValue();
        if (objCliGarantia == null) {
            objCliGarantia = objlstCliGarantia.get(i_sel + 1);
        }
        i_sel = lst_garantia.getSelectedIndex();
        //cargamos los campos
        llenarCamposGarantia(objCliGarantia);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objCliGarantia.getCligar_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";
        s_estado_direccion = "N";
        objCliente = new Cliente();
        //inicializando las clases de los Detalles de Cliente
        limpiarCampos();
        limpiarCamposFinanciero();
        limpiarCamposTelefono();
        limpiarCamposGarantia();
        habilitaBotones(true, false);
        botonNuevoDireccion();
        //botonNuevoTelefono();
        //botonNuevoFinanciero();
        botonNuevoGarantia();
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        //modificado x jr
        habilitaCamposTelefono(false);
        habilitaCamposFinanciero(false);
        //inhabilitamos dni, ruc, emite factura y persona juridica
        txt_clidni.setDisabled(true);
        txt_cliruc.setDisabled(true);
        chk_cliperju.setDisabled(true);
        chk_cli_factura.setDisabled(true);
        //limpiamos las listas 
        objlstCliDireccion = new ListModelList<CliDireccion>();
        //objlstCliFinanciero = new ListModelList<CliFinanciero>();
        objlstCliGarantia = new ListModelList<CliGarantia>();
        //objlstCliTelefono = new ListModelList<CliTelefono>();
        lst_direccion.setModel(objlstCliDireccion);
        //lst_financiero.setModel(objlstCliFinanciero);
        lst_garantia.setModel(objlstCliGarantia);
        //lst_telefono.setModel(objlstCliTelefono);

        //prueba
        objlstEliDireccion = null;
        objlstEliFinanciero = null;
        objlstEliGarantia = null;
        objlstEliTelefono = null;
        objlstEliDireccion = new ListModelList<CliDireccion>();
        objlstEliFinanciero = new ListModelList<CliFinanciero>();
        objlstEliGarantia = new ListModelList<CliGarantia>();
        objlstEliTelefono = new ListModelList<CliTelefono>();
        chk_cliestado.setDisabled(true);
        chk_cliestado.setChecked(true);
        chk_clidir_default.setDisabled(false);
        chk_clidir_default.setChecked(true);
        // cb_tipodoc.setFocus(true);
        cb_tipodoc.setSelectedIndex(0);
        cb_moneda.setSelectedIndex(0);
        cb_moneda.select();
        cb_lprecio.setSelectedIndex(0);
        cb_canal.setSelectedIndex(0);
        cb_condicion.setSelectedIndex(0);
        cb_fpago.setSelectedIndex(0);
        txt_cliapepat.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_nuevod")
    public void botonNuevoDireccion() {
        banderadir = false;
        objCliDireccion = new CliDireccion();
        limpiarCamposDireccion();
        habilitaBotonesDireccion(true, false);
        habilitaCamposDireccion(false);
        //estado activo y default en 0
        chk_clidir_estado.setChecked(true);
        chk_clidir_estado.setDisabled(true);
        chk_clidir_default.setChecked(false);
        //Desabilitar la Lista de Direcciones
        Utilitarios.deshabilitarLista(true, lst_direccion);
        btn_mant_direccion.focus();
        //
        s_estado_direccion = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");

    }

    /*@Listen("onClick=#tbbtn_btn_nuevot")
     public void botonNuevoTelefono() {
     banderatel = false;
     objCliTelefono = new CliTelefono();
     limpiarCamposTelefono();
     habilitaBotonesTelefono(true, false);
     habilitaCamposTelefono(false);
     //estado activo y default en 0
     chk_clitel_estado.setChecked(true);
     chk_clitel_estado.setDisabled(true);
     //Desabilitar la Lista de Direcciones
     Utilitarios.deshabilitarLista(true, lst_telefono);
     txt_clitel_telef1.focus();
     s_estado_telefono = "N";
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
     }*/

    /*@Listen("onClick=#tbbtn_btn_nuevof")
     public void botonNuevoFinanciero() {
     banderafin = false;
     objCliFinanciero = new CliFinanciero();
     limpiarCamposFinanciero();
     habilitaBotonesFinanciero(true, false);
     habilitaCamposFinanciero(false);
     //estado activo y default en 0
     chk_clifin_estado.setChecked(true);
     chk_clifin_estado.setDisabled(true);
     //Desabilitar la Lista de Direcciones
     Utilitarios.deshabilitarLista(true, lst_financiero);
     //cb_categoria.setSelectedIndex(0);
     cb_categoria.focus();
     cb_categoria.select();
     s_estado_financiero = "N";
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
     }*/
    @Listen("onClick=#tbbtn_btn_nuevog")
    public void botonNuevoGarantia() {
        banderagar = false;
        objCliGarantia = new CliGarantia();
        limpiarCamposGarantia();
        habilitaBotonesGarantia(true, false);
        habilitaCamposGarantia(false);
        //estado activo y default en 0
        chk_cligar_estado.setChecked(true);
        chk_cligar_estado.setDisabled(true);
        //Desabilitar la Lista de Garantias
        Utilitarios.deshabilitarLista(true, lst_garantia);
        //cb_garantia.setSelectedIndex(0);
        cb_garantia.focus();
        cb_garantia.select();
        s_estado_garantia = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");

    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_listacliente.setSelected(true);
                            tab_mantenimiento.setSelected(false);
                            habilitaTab(false, false);
                            //tab_mantenimiento.setDisabled(false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            limpiarCampos();
                            limpiarCamposDireccion();
                            limpiarCamposFinanciero();
                            limpiarCamposGarantia();
                            limpiarCamposTelefono();
                            limpiaAuditoria();
                            habilitaCampos(true);
                            habilitaBotonesDireccion(true, true);
                            //habilitaBotonesFinanciero(true, true);
                            habilitaBotonesGarantia(true, true);
                            //habilitaBotonesTelefono(true, true);
                            //
                            habilitaCamposDireccion(true);
                            habilitaCamposFinanciero(true);
                            habilitaCamposGarantia(true);
                            habilitaCamposTelefono(true);

                            chk_cliestado.setDisabled(true);
                            //limpiamos las listas
                            // limpiarLista();
                            objlstCliente = null;
                            /*objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
                             lst_cliente.setModel(objlstCliente);
                             lst_cliente.setSelectedIndex(-1);*/
                            cb_estado.setSelectedIndex(0);
                            s_estado_direccion = "Q";
                            s_codigo_financiero = "Q";
                            s_codigo_garantia = "Q";
                            s_codigo_telefono = "Q";
                            s_estado = "Q";
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_deshacerd")
    public void botonDeshacerDireccion() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //
                            habilitaBotonesDireccion(false, true);
                            limpiarCamposDireccion();
                            habilitaCamposDireccion(true);
                            //limpiamos las listas
                            lst_direccion.setSelectedIndex(-1);
                            Utilitarios.deshabilitarLista(false, lst_direccion);
                            objCliDireccion = null;
                            s_estado_direccion = "Q";
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    /*@Listen("onClick=#tbbtn_btn_deshacert")
     public void botonDeshacerTelefono() {
     String s_str = "Está seguro que desea deshacer los cambios";
     Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     //
     habilitaBotonesTelefono(false, true);
     limpiarCamposTelefono();
     habilitaCamposTelefono(true);
     //limpiamos las listas
     lst_telefono.setSelectedIndex(-1);
     Utilitarios.deshabilitarLista(false, lst_telefono);
     objCliTelefono = null;
     s_estado_telefono = "Q";
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
     }
     }
     });
     }*/

    /*@Listen("onClick=#tbbtn_btn_deshacerf")
     public void botonDeshacerFinanciero() {
     String s_str = "Está seguro que desea deshacer los cambios";
     Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     //
     habilitaBotonesFinanciero(false, true);
     limpiarCamposFinanciero();
     habilitaCamposFinanciero(true);
     //limpiamos las listas
     lst_financiero.setSelectedIndex(-1);
     Utilitarios.deshabilitarLista(false, lst_financiero);
     objCliFinanciero = null;
     s_estado_financiero = "Q";
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
     }
     }
     });
     }*/
    @Listen("onClick=#tbbtn_btn_deshacerg")
    public void botonDeshacerGarantia() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            //
                            habilitaBotonesGarantia(false, true);
                            limpiarCamposGarantia();
                            habilitaCamposGarantia(true);
                            //limpiamos las listas
                            lst_garantia.setSelectedIndex(-1);
                            Utilitarios.deshabilitarLista(false, lst_garantia);
                            objCliGarantia = null;
                            s_estado_garantia = "Q";
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_cliente.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaBotonesDireccion(false, true);
            //habilitaBotonesTelefono(false, true);
            //habilitaBotonesFinanciero(false, true);
            habilitaBotonesGarantia(false, true);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);

            habilitaCamposTelefono(false);
            habilitaCamposFinanciero(false);

            txt_clidni.setDisabled(true);
            txt_cliruc.setDisabled(true);
            chk_cliestado.setDisabled(false);
            chk_cliperju.setDisabled(true);
            if (chk_cliperju.isChecked()) {
                txt_clifecnac.setDisabled(true);
            }
            objlstEliDireccion = null;
            objlstEliFinanciero = null;
            objlstEliGarantia = null;
            objlstEliTelefono = null;
            objlstEliDireccion = new ListModelList<CliDireccion>();
            objlstEliFinanciero = new ListModelList<CliFinanciero>();
            objlstEliGarantia = new ListModelList<CliGarantia>();
            objlstEliTelefono = new ListModelList<CliTelefono>();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_editard")
    public void botonEditarDireccion() throws SQLException {
        if (lst_direccion.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            banderadir = false;
            s_estado_direccion = "E";
            habilitaBotonesDireccion(true, false);
            habilitaCamposDireccion(false);
            //Desabilitar la Lista de Garantias
            Utilitarios.deshabilitarLista(true, lst_direccion);
            chk_clidir_default.setDisabled(false);
            btn_mant_direccion.focus();
            btn_mant_direccion.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    /*@Listen("onClick=#tbbtn_btn_editart")
     public void botonEditarTelefono() throws SQLException {
     if (lst_telefono.getSelectedIndex() == -1) {
     Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     banderatel = false;
     s_estado_telefono = "E";
     habilitaBotonesTelefono(true, false);
     habilitaCamposTelefono(false);
     //Desabilitar la Lista de Garantias
     Utilitarios.deshabilitarLista(true, lst_telefono);
     chk_clitel_estado.setDisabled(false);
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
     }
     }*/
    @Listen("onClick=#tbbtn_btn_editarg")
    public void botonEditarGarantia() throws SQLException {
        if (lst_garantia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            banderagar = false;
            s_estado_garantia = "E";
            habilitaBotonesGarantia(true, false);
            habilitaCamposGarantia(false);
            //Desabilitar la Lista de Garantias
            Utilitarios.deshabilitarLista(true, lst_garantia);
            chk_cligar_estado.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    /*@Listen("onClick=#tbbtn_btn_editarf")
     public void botonEditarFinanciero() throws SQLException {
     if (lst_financiero.getSelectedIndex() == -1) {
     Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     banderafin = false;
     s_estado_financiero = "E";
     habilitaBotonesFinanciero(true, false);
     habilitaCamposFinanciero(false);
     //Desabilitar la Lista de Garantias
     Utilitarios.deshabilitarLista(true, lst_financiero);
     chk_clifin_estado.setDisabled(false);
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
     }
     }*/
    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        /*  if(!s_estado.equals("N")){
         verificarCreditoRegistrado();
         Messagebox.show("EL crédito finaciero asignado para este cliente ha excedido el límite","ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION); 
         }
         else{*/
        String verifica = verificar();
        if (!verifica.isEmpty()) {
            Messagebox.show(verifica, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        nextSalto();
                    }
                }
            });
        } else if (!validarDefault()) {
            Messagebox.show("Por favor ingrese una dirección como default", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } /*else if (verificarCreditoRegistrado()) {
         Messagebox.show("EL crédito finaciero asignado para este cliente ha excedido el límite", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } */ else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamCabecera, objParamFinanciero, objParamTelefono;
                                objCliente = (Cliente) generaRegistro();
                                // objCliFinanciero = (CliFinanciero) generaRegistroFinanciero();
                                //  objCliTelefono = (CliTelefono) generaRegiostroTelefono();
                                long cli_key;
                                String cli_id;
                                if (s_estado.equals("N")) {
                                    objParamCabecera = objDaoCliente.insertarCliente(objCliente);

                                } else {
                                    objParamCabecera = objDaoCliente.actualizarCliente(objCliente);

                                    //objParamCabecera = objDaoCliente.actualizarTelefono(objCliTelefono);
                                }
                                objCliFinanciero = (CliFinanciero) generaRegistroFinanciero();
                                objCliTelefono = (CliTelefono) generaRegistroTelefono();
                                // objParamCabecera = objDaoCliente.insertarFinanciero(objCliFinanciero);
                                if (objParamCabecera.getFlagIndicador() == 0) {
                                    ParametrosSalida objParamDetalle;
                                    boolean verificarDetalle = true;
                                    int i = 0;
                                    //CUANDO SE CREA UN NUEVO CLIENTE
                                    if (s_estado.equals("N")) {
                                        objParamFinanciero = objDaoCliente.insertarFinanciero(objCliFinanciero);
                                        objParamTelefono = objDaoCliente.insertarTelefono(objCliTelefono);
                                        if (objParamFinanciero.getFlagIndicador() == 1 || objParamTelefono.getFlagIndicador() == 1) {
                                            Messagebox.show("Ocurrio un Error al Insertar los datos ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            verificarDetalle = false;
                                        }
                                    } else {
                                        // objCliFinanciero = (CliFinanciero) generaRegistroFinanciero();
                                        objParamFinanciero = objDaoCliente.actualizarFinanciero(objCliFinanciero);
                                        objParamTelefono = objDaoCliente.actualizarTelefono(objCliTelefono);
                                        if (objParamFinanciero.getFlagIndicador() == 1 || objParamTelefono.getFlagIndicador() == 1) {
                                            Messagebox.show("Ocurrio un Error al actualizar los datos ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                            verificarDetalle = false;
                                        }
                                    }
                                    if (s_estado.equals("N")) {
                                        cli_key = Long.parseLong(objParamCabecera.getCodigoRegistro());
                                        cli_id = objParamCabecera.getCodigoRegistro();
                                        //INSERTANDO LAS DIRECCIONES DEL CLIENTE
                                        while (i < objlstCliDireccion.getSize() && verificarDetalle) {
                                            objlstCliDireccion.get(i).setCli_key(cli_key);
                                            objlstCliDireccion.get(i).setCli_id(cli_id);
                                            if (objlstCliDireccion.get(i).getClizon_default() == 1) {
                                                objlstCliDireccion.get(i).setCli_lista(Integer.parseInt(cb_lprecio.getSelectedItem().getValue().toString()));
                                                objlstCliDireccion.get(i).setCli_factura(chk_cli_factura.isChecked() ? 1 : 0);
                                                objlstCliDireccion.get(i).setCli_perc(chk_cli_perc.isChecked() ? 1 : 0);
                                            }
                                            objParamDetalle = objDaoCliente.insertarDireccion(objlstCliDireccion.get(i));

                                            if (objParamDetalle.getFlagIndicador() == 1) {
                                                Messagebox.show("Ocurrio un Error al Insertar la Direccion " + objlstCliDireccion.get(i).getClidir_direc() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                verificarDetalle = false;
                                            }
                                            i++;
                                        }
                                        //se 
                                        /*verificarDetalle = true;
                                         i = 0;
                                         //INSERTANDO LOS TELEFONOS DEL CLIENTE
                                         while (i < objlstCliTelefono.getSize() && verificarDetalle) {
                                         objlstCliTelefono.get(i).setCli_key(cli_key);
                                         objlstCliTelefono.get(i).setCli_id(cli_id);
                                         objParamDetalle = objDaoCliente.insertarTelefono(objlstCliTelefono.get(i));
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error al Insertar el Telefono " + objlstCliTelefono.get(i).getClitel_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         verificarDetalle = true;
                                         i = 0;
                                         //INSERTANDO LOS REGISTROS FINANCIEROS DEL CLIENTE
                                         while (i < objlstCliFinanciero.getSize() && verificarDetalle) {
                                         objlstCliFinanciero.get(i).setEmp_id(emp_id);
                                         objlstCliFinanciero.get(i).setSuc_id(suc_id);
                                         objlstCliFinanciero.get(i).setCli_key(cli_key);
                                         objlstCliFinanciero.get(i).setCli_id(cli_id);
                                         objParamDetalle = objDaoCliente.insertarFinanciero(objlstCliFinanciero.get(i));
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error al Insertar el Registro Financiero " + objlstCliFinanciero.get(i).getClifin_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }*/
                                         verificarDetalle = true;
                                         i = 0;
                                         //INSERTANDO LOS REGISTROS GARANTIA DEL CLIENTE
                                         while (i < objlstCliGarantia.getSize() && verificarDetalle) {
                                         objlstCliGarantia.get(i).setEmp_id(emp_id);
                                         objlstCliGarantia.get(i).setSuc_id(suc_id);
                                         objlstCliGarantia.get(i).setCli_key(cli_key);
                                         objlstCliGarantia.get(i).setCli_id(cli_id);
                                         objParamDetalle = objDaoCliente.insertarGarantia(objlstCliGarantia.get(i));
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error al Insertar el Registro de Garantia " + objlstCliGarantia.get(i).getCligar_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         } else {
                                         /**
                                         * **************************PROCESOS
                                         * PARA LA ELIMINACION DE LOS DETALLES
                                         * DEL
                                         * CLIENTE********************************
                                         */
                                        // if (objlstEliDireccion!=null) {
                                        if (!objlstEliDireccion.isEmpty()) {
                                            while (i < objlstEliDireccion.getSize() && verificarDetalle) {
                                                objParamDetalle = objDaoCliente.eliminarDireccion(objlstEliDireccion.get(i));
                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                    Messagebox.show("Ocurrio un Error con la Direccion " + objlstEliDireccion.get(i).getClidir_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                    verificarDetalle = false;
                                                }
                                                i++;
                                            }
                                        }
                                        /*if (!objlstEliFinanciero.isEmpty()) {
                                         i = 0;
                                         verificarDetalle = true;
                                         while (i < objlstEliFinanciero.getSize() && verificarDetalle) {
                                         objParamDetalle = objDaoCliente.eliminarFinanciero(objlstEliFinanciero.get(i));
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error con el Dato Financiero " + objlstEliFinanciero.get(i).getClifin_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         }*/
                                        // if (objlstEliGarantia!=null) {
                                        if (!objlstEliGarantia.isEmpty()) {
                                            i = 0;
                                            verificarDetalle = true;
                                            while (i < objlstEliGarantia.getSize() && verificarDetalle) {
                                                objParamDetalle = objDaoCliente.eliminarGarantia(objlstEliGarantia.get(i));
                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                    Messagebox.show("Ocurrio un Error con el Dato de garantia " + objlstEliGarantia.get(i).getCligar_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                    verificarDetalle = false;
                                                }
                                                i++;
                                            }
                                        }
                                        /*if (!objlstEliTelefono.isEmpty()) {
                                         i = 0;
                                         verificarDetalle = true;
                                         while (i < objlstEliTelefono.getSize() && verificarDetalle) {
                                         objParamDetalle = objDaoCliente.eliminarTelefono(objlstEliTelefono.get(i));
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error con el Dato de garantia " + objlstEliTelefono.get(i).getClitel_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         }*/
                                        /**
                                         * ***********************************************************************************************************
                                         */
                                        //  if (objlstCliDireccion!=null) {
                                        //comentado todo el if ver si asi funciona
                                        if (!objlstCliDireccion.isEmpty()) {
                                         i = 0;
                                         verificarDetalle = true;
                                         objParamDetalle = new ParametrosSalida();
                                         while (i < objlstCliDireccion.getSize() && verificarDetalle) {
                                         if (objlstCliDireccion.get(i).getClizon_default() == 1) {
                                         objlstCliDireccion.get(i).setCli_lista(Integer.parseInt(cb_lprecio.getSelectedItem().getValue().toString()));
                                         objlstCliDireccion.get(i).setCli_factura(chk_cli_factura.isChecked() ? 1 : 0);
                                         objlstCliDireccion.get(i).setCli_perc(chk_cli_perc.isChecked() ? 1 : 0);
                                         }
                                         String indicador = objlstCliDireccion.get(i).getInd_accion();
                                         if (indicador.equals("N") || indicador.equals("NM")) {

                                         objParamDetalle = objDaoCliente.insertarDireccion(objlstCliDireccion.get(i));
                                         } else if (indicador.equals("M") || indicador.equals("Q")) {
                                         if (objlstCliDireccion.get(i).getZon_id() != null) {
                                         objParamDetalle = objDaoCliente.actualizarDireccion(objlstCliDireccion.get(i));
                                         }
                                         }
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error con la Direccion " + objlstCliDireccion.get(i).getClidir_direc() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         }
                                        /*if (!objlstCliFinanciero.isEmpty()) {
                                         i = 0;
                                         verificarDetalle = true;
                                         objParamDetalle = new ParametrosSalida();
                                         while (i < objlstCliFinanciero.getSize() && verificarDetalle) {
                                         String indicador = objlstCliFinanciero.get(i).getInd_accion();
                                         if (indicador.equals("N") || indicador.equals("NM")) {
                                         objParamDetalle = objDaoCliente.insertarFinanciero(objlstCliFinanciero.get(i));
                                         } else if (indicador.equals("M")) {
                                         objParamDetalle = objDaoCliente.actualizarFinanciero(objlstCliFinanciero.get(i));
                                         }
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error con el Monto Financiero " + objlstCliFinanciero.get(i).getClifin_limcred() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         }*/
                                        //  if (objlstCliGarantia!=null) {
                                        if (!objlstCliGarantia.isEmpty()) {
                                            i = 0;
                                            verificarDetalle = true;
                                            objParamDetalle = new ParametrosSalida();
                                            while (i < objlstCliGarantia.getSize() && verificarDetalle) {
                                                String indicador = objlstCliGarantia.get(i).getInd_accion();
                                                if (indicador.equals("N") || indicador.equals("NM")) {
                                                    objParamDetalle = objDaoCliente.insertarGarantia(objlstCliGarantia.get(i));
                                                } else if (indicador.equals("M")) {
                                                    objParamDetalle = objDaoCliente.actualizarGarantia(objlstCliGarantia.get(i));
                                                }
                                                if (objParamDetalle.getFlagIndicador() == 1) {
                                                    Messagebox.show("Ocurrio un Error con el Monto de Garantia " + objlstCliGarantia.get(i).getCligar_garantia() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                                    verificarDetalle = false;
                                                }
                                                i++;
                                            }
                                        }
                                        /*if (!objlstCliTelefono.isEmpty()) {
                                         i = 0;
                                         verificarDetalle = true;
                                         objParamDetalle = new ParametrosSalida();
                                         while (i < objlstCliTelefono.getSize() && verificarDetalle) {
                                         String indicador = objlstCliTelefono.get(i).getInd_accion();
                                         if (indicador.equals("N") || indicador.equals("NM")) {
                                         objParamDetalle = objDaoCliente.insertarTelefono(objlstCliTelefono.get(i));
                                         } else if (indicador.equals("M")) {
                                         objParamDetalle = objDaoCliente.actualizarTelefono(objlstCliTelefono.get(i));
                                         }
                                         if (objParamDetalle.getFlagIndicador() == 1) {
                                         Messagebox.show("Ocurrio un Error con el Telefono " + objlstCliTelefono.get(i).getClitel_id() + " debido a " + objParamDetalle.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                         verificarDetalle = false;
                                         }
                                         i++;
                                         }
                                         }*/
                                    }
                                    tab_listacliente.setSelected(true);
                                    tab_mantenimiento.setSelected(false);
                                    habilitaTab(false, false);
                                    //tab_mantenimiento.setDisabled(false);
                                    //validacion de deshacer
                                    VerificarTransacciones();
                                    tbbtn_btn_guardar.setDisabled(true);
                                    tbbtn_btn_deshacer.setDisabled(true);
                                    //
                                    limpiarCampos();
                                    limpiarCamposDireccion();
                                    limpiarCamposFinanciero();
                                    limpiarCamposGarantia();
                                    limpiarCamposTelefono();
                                    limpiaAuditoria();
                                    habilitaCampos(true);
                                    //modificado x jr
                                    habilitaCamposTelefono(true);
                                    habilitaCamposFinanciero(true);
                                    habilitaBotonesDireccion(true, true);
                                    //habilitaBotonesFinanciero(true, true);
                                    habilitaBotonesGarantia(true, true);
                                    //habilitaBotonesTelefono(true, true);
                                    chk_cliestado.setDisabled(true);
                                    //limpiamos las listas
                                    limpiarLista();
                                    objlstEliDireccion = null;
                                    //objlstEliFinanciero = null;
                                    objlstEliGarantia = null;
                                    //objlstEliTelefono = null;
                                    objlstCliente = null;
                                    /*objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
                                     lst_cliente.setModel(objlstCliente);*/
                                    cb_estado.setSelectedIndex(0);
                                    lst_cliente.setSelectedIndex(-1);
                                    s_estado_direccion = "Q";
                                    //s_codigo_financiero = "Q";
                                    s_codigo_garantia = "Q";
                                    //s_codigo_telefono = "Q";
                                    s_estado = "Q";
                                }
                                Messagebox.show(objParamCabecera.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    }
            );
        }
        //}
    }

    @Listen("onClick=#tbbtn_btn_guardard")
    public void botonGuardarDireccion() throws SQLException {
        String verificar = verificarDirecciones();
        if (!verificar.isEmpty()) {
            Messagebox.show("Verificar los datos en el campo " + verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (banderadir == false) {
                banderadir = true;
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    if (s_estado_direccion.equals("E") && objCliDireccion == null) {
                                        objCliDireccion = (CliDireccion) sesion.getAttribute("objCliDireccion");
                                    }
                                    validaZona();
                                    objCliDireccion.setZon_id(txt_clidir_zona.getValue());
                                    objCliDireccion.setZon_des(txt_clidir_zonadesc.getValue());
                                    objCliDireccion.setHor_id(txt_clidir_idhorario.getValue());
                                    objCliDireccion.setHor_des(txt_clidir_horario.getValue());
                                    objCliDireccion.setVen_id(objZona.getZon_idven());
                                    objCliDireccion.setVen_apenom(txt_clidir_vendedor.getValue());
                                    objCliDireccion.setClizon_default(chk_clidir_default.isChecked() ? 1 : 0);
                                    objCliDireccion.setClizon_est(chk_clidir_estado.isChecked() ? 1 : 2);
                                    objCliDireccion.setCan_des(objZona.getZon_candes());
                                    objCliDireccion.setClidir_ref(objCliDireccion.getClidir_ref());
                                    objCliDireccion.setUbi_id(txt_clidir_idubigeo.getValue());
                                    objCliDireccion.setPos_id(txt_clidir_idpost.getValue());
                                    objCliDireccion.setGiro_id(txt_clidir_idgiro.getValue());
                                    objCliDireccion.setGiro_des(txt_clidir_giro.getValue());

                                    //objCliDireccion.setCli_lista(cb_lprecio.getSelectedIndex());
                                    //objCliDireccion.setCli_factura(chk_cli_factura.isChecked() ? 1 : 0);
                                    if (s_estado.equals("N")) {
                                        if (s_estado_direccion.equals("N")) {
                                            objCliDireccion.setClidir_usuadd(objUsuCredential.getCuenta());
                                            objlstCliDireccion.add(objCliDireccion);
                                        } else {
                                            int index = lst_direccion.getSelectedIndex();
                                            objlstCliDireccion.set(index, objCliDireccion);
                                            objCliDireccion.setClidir_usumod(objUsuCredential.getCuenta());
                                        }
                                    } else {
                                        if (s_estado_direccion.equals("N")) {
                                            objCliDireccion.setClidir_usuadd(objUsuCredential.getCuenta());
                                            objCliDireccion.setInd_accion("N");
                                            objCliDireccion.setCli_id(txt_cliid.getValue());
                                            objCliDireccion.setCli_key(Long.parseLong(txt_cliid.getValue()));
                                            objlstCliDireccion.add(objCliDireccion);
                                        } else {
                                            String indicador = objCliDireccion.getInd_accion();
                                            objCliDireccion.setClidir_usumod(objUsuCredential.getCuenta());
                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                objCliDireccion.setInd_accion("NM");
                                            } else {
                                                objCliDireccion.setInd_accion("M");
                                            }
                                            objlstCliDireccion.set(lst_direccion.getSelectedIndex(), objCliDireccion);
                                        }
                                    }
                                    lst_direccion.setModel(objlstCliDireccion);
                                    objlstCliDireccion.clearSelection();
                                    sesion.removeAttribute("objCliDireccion");
                                    habilitaBotonesDireccion(false, true);
                                    habilitaCamposDireccion(true);
                                    limpiarCamposDireccion();
                                    btn_mant_direccion.setDisabled(true);
                                    tab_telefono.setSelected(true);
                                    txt_clitel_telef1.focus();
                                } else {
                                    banderadir = false;
                                }
                            }
                        });
            }
        }
    }

    /*@Listen("onClick=#tbbtn_btn_guardart")
     public void botonGuardarTelefono() throws SQLException {
     String verificar = verificarTelefonos();
     if (!verificar.isEmpty()) {
     Messagebox.show("Por favor ingrese al menos un número telefónico", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
     public void onEvent(Event t) throws Exception {
     txt_clitel_telef1.focus();
     }
     });
     } else {
     if (banderatel == false) {
     banderatel = true;
     s_mensaje = "Está seguro que desea guardar los cambios?";
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     objCliTelefono.setClitel_telef1(txt_clitel_telef1.getValue() == null ? 0 : txt_clitel_telef1.getValue());
     objCliTelefono.setClitel_telef2(txt_clitel_telef2.getValue() == null ? 0 : txt_clitel_telef2.getValue());
     objCliTelefono.setClitel_movil(txt_clitel_movil.getValue() == null ? 0 : txt_clitel_movil.getValue());
     if (s_estado.equals("N")) {
     if (s_estado_telefono.equals("N")) {
     objCliTelefono.setClitel_usuadd(objUsuCredential.getCuenta());
     objCliTelefono.setClitel_est(1);
     objlstCliTelefono.add(objCliTelefono);
     } else {
     int index = lst_telefono.getSelectedIndex();
     objCliTelefono.setClitel_usumod(objUsuCredential.getCuenta());
     objCliTelefono.setClitel_est(chk_clitel_estado.isChecked() ? 1 : 2);
     objlstCliTelefono.set(index, objCliTelefono);
     }
     } else {
     if (s_estado_telefono.equals("N")) {
     objCliTelefono.setClitel_usuadd(objUsuCredential.getCuenta());
     objCliTelefono.setClitel_est(1);
     objCliTelefono.setInd_accion("N");
     objCliTelefono.setCli_id(txt_cliid.getValue());
     objCliTelefono.setCli_key(Long.parseLong(txt_cliid.getValue()));
     objlstCliTelefono.add(objCliTelefono);
     } else {
     String indicador = objCliTelefono.getInd_accion();
     objCliTelefono.setClitel_usumod(objUsuCredential.getCuenta());
     objCliTelefono.setClitel_est(chk_clitel_estado.isChecked() ? 1 : 2);
     if (indicador.equals("N") || indicador.equals("NM")) {
     objCliTelefono.setInd_accion("NM");
     } else {
     objCliTelefono.setInd_accion("M");
     }
     //objlstCliTelefono.set(lst_telefono.getSelectedIndex(), objCliTelefono);
     }
     }
     //lst_telefono.setModel(objlstCliTelefono);
     //objlstCliTelefono.clearSelection();
     //habilitaBotonesTelefono(false, true);
     habilitaCamposTelefono(true);
     limpiarCamposTelefono();
     } else {
     banderatel = false;
     }
     }
     });
     }
     }
     }*/

    /*@Listen("onClick=#tbbtn_btn_guardarf")
     public void botonGuardarFinanciero() throws SQLException {
     String verificar = verificarFinanciero();
     if (!verificar.isEmpty()) {
     Messagebox.show("Verificar los datos en el campo " + verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     int nroitem = s_estado_financiero.equals("E") ? 0 : objlstCliFinanciero.getSize();
     if (nroitem == 0) {
     if (banderafin == false) {
     banderafin = true;
     s_mensaje = "Está seguro que desea guardar los cambios?";
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     objCliFinanciero.setClifin_categ(Integer.parseInt(cb_categoria.getSelectedItem().getValue().toString()));
     objCliFinanciero.setClifin_limcred(txt_clifin_limcred.getValue());
     objCliFinanciero.setClifin_limdoc(txt_clifin_limdoc.getValue());
     if (s_estado.equals("N")) {
     if (s_estado_financiero.equals("N")) {
     objCliFinanciero.setClifin_est(1);
     objCliFinanciero.setClifin_usuadd(objUsuCredential.getCuenta());
     objlstCliFinanciero.add(objCliFinanciero);
     } else {
     int index = lst_financiero.getSelectedIndex();
     objCliFinanciero.setClifin_usumod(objUsuCredential.getCuenta());
     objCliFinanciero.setClifin_est(chk_clifin_estado.isChecked() ? 1 : 2);
     objlstCliFinanciero.set(index, objCliFinanciero);
     }
     } else {
     if (s_estado_financiero.equals("N")) {
     objCliFinanciero.setClifin_est(1);
     objCliFinanciero.setInd_accion("N");
     objCliFinanciero.setClifin_usuadd(objUsuCredential.getCuenta());
     objCliFinanciero.setEmp_id(emp_id);
     objCliFinanciero.setSuc_id(suc_id);
     objCliFinanciero.setCli_id(txt_cliid.getValue());
     objCliFinanciero.setCli_key(Long.parseLong(txt_cliid.getValue()));
     objlstCliFinanciero.add(objCliFinanciero);
     } else {
     String indicador = objCliFinanciero.getInd_accion();
     objCliFinanciero.setClifin_usumod(objUsuCredential.getCuenta());
     objCliFinanciero.setClifin_est(chk_clifin_estado.isChecked() ? 1 : 2);
     if (indicador.equals("N") || indicador.equals("NM")) {
     objCliFinanciero.setInd_accion("NM");
     } else {
     objCliFinanciero.setInd_accion("M");
     }
     objlstCliFinanciero.set(lst_financiero.getSelectedIndex(), objCliFinanciero);
     }
     }
     lst_financiero.setModel(objlstCliFinanciero);
     objlstCliFinanciero.clearSelection();
     habilitaBotonesFinanciero(false, true);
     habilitaCamposFinanciero(true);
     limpiarCamposFinanciero();
     }
     }
     });
     } else {
     banderafin = false;
     }
     } else {
     Messagebox.show("Ya existe un registro financiero");
     }
     }
     }*/
    @Listen("onClick=#tbbtn_btn_guardarg")
    public void botonGuardarGarantia() throws SQLException {
        String verificar = verificarGarantias();
        if (!verificar.isEmpty()) {
            Messagebox.show("Verificar los datos en el campo " + verificar, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (banderagar == false) {
                banderagar = true;
                s_mensaje = "Está seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    objCliGarantia.setCligar_garantia(cb_garantia.getValue());
                                    objCliGarantia.setCligar_monto(txt_cligar_monto.getValue());
                                    objCliGarantia.setCligar_obs(txt_cligar_obs.getValue());
                                    if (s_estado.equals("N")) {
                                        if (s_estado_garantia.equals("N")) {
                                            objCliGarantia.setCligar_usuadd(objUsuCredential.getCuenta());
                                            objCliGarantia.setCligar_est(1);
                                            objlstCliGarantia.add(objCliGarantia);
                                        } else {
                                            objCliGarantia.setCligar_usumod(objUsuCredential.getCuenta());
                                            int index = lst_garantia.getSelectedIndex();
                                            objCliGarantia.setCligar_est(chk_cligar_estado.isChecked() ? 1 : 2);
                                            objlstCliGarantia.set(index, objCliGarantia);
                                        }
                                    } else {
                                        if (s_estado_garantia.equals("N")) {
                                            objCliGarantia.setCligar_usuadd(objUsuCredential.getCuenta());
                                            objCliGarantia.setCligar_est(1);
                                            objCliGarantia.setInd_accion("N");
                                            objCliGarantia.setEmp_id(emp_id);
                                            objCliGarantia.setSuc_id(suc_id);
                                            objCliGarantia.setCli_id(txt_cliid.getValue());
                                            objCliGarantia.setCli_key(Long.parseLong(txt_cliid.getValue()));
                                            objlstCliGarantia.add(objCliGarantia);
                                        } else {
                                            objCliGarantia.setCligar_usumod(objUsuCredential.getCuenta());
                                            String indicador = objCliGarantia.getInd_accion();
                                            objCliGarantia.setCligar_est(chk_cligar_estado.isChecked() ? 1 : 2);
                                            if (indicador.equals("N") || indicador.equals("NM")) {
                                                objCliGarantia.setInd_accion("NM");
                                            } else {
                                                objCliGarantia.setInd_accion("M");
                                            }
                                            objlstCliGarantia.set(lst_garantia.getSelectedIndex(), objCliGarantia);
                                        }
                                    }
                                    lst_garantia.setModel(objlstCliGarantia);
                                    objlstCliGarantia.clearSelection();
                                    habilitaBotonesGarantia(false, true);
                                    habilitaCamposGarantia(true);
                                    limpiarCamposGarantia();
                                }
                            }
                        });
            } else {
                banderagar = false;
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_cliente.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un cliente a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Está seguro que desea eliminar el cliente";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                String s_cliid = txt_cliid.getValue();
                                Long l_clikey = Long.parseLong(txt_cliid.getValue());
                                s_mensaje = objDaoCliente.eliminarCliente(l_clikey, s_cliid);
                                Messagebox.show(s_mensaje);
                                VerificarTransacciones();
                                limpiarCampos();
                                limpiaAuditoria();
                                limpiarLista();
                                objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
                                lst_cliente.setModel(objlstCliente);
                                lst_cliente.setSelectedIndex(-1);
                                s_estado_direccion = "Q";
                                s_codigo_financiero = "Q";
                                s_codigo_garantia = "Q";
                                s_codigo_telefono = "Q";
                                s_estado = "Q";
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminard")
    public void botonEliminarDireccion() throws SQLException {
        if (lst_direccion.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione el detalle a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Está seguro que desea eliminar la dirección del cliente";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objCliDireccion = lst_direccion.getSelectedItem().getValue();
                                int posicion = lst_direccion.getSelectedIndex();
                                if (s_estado.equals("E") && (!objCliDireccion.getInd_accion().equals("N") || !objCliDireccion.getInd_accion().equals("NM"))) {
                                    String cli_id = objCliDireccion.getCli_id();
                                    long cli_key = objCliDireccion.getCli_key();
                                    long clidir_id = objCliDireccion.getClidir_id();
                                    String ocd_usumod = objUsuCredential.getCuenta();
                                    objlstEliDireccion.add(new CliDireccion(cli_id, cli_key, clidir_id, ocd_usumod));
                                }
                                objlstCliDireccion.remove(posicion);
                                habilitaBotonesDireccion(false, true);
                                limpiarCamposDireccion();
                                habilitaCamposDireccion(true);
                                //limpiamos las listas
                                lst_direccion.setSelectedIndex(-1);
                                s_estado_direccion = "Q";
                            }
                        }
                    });
        }
    }

    /*@Listen("onClick=#tbbtn_btn_eliminart")
     public void botonEliminarTelefono() throws SQLException {
     if (lst_telefono.getSelectedIndex() == -1) {
     Messagebox.show("Por favor seleccione el detalle a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     String s_str = "Está seguro que desea eliminar el teléfono del cliente";
     Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     objCliTelefono = lst_telefono.getSelectedItem().getValue();
     int posicion = lst_telefono.getSelectedIndex();
     if (s_estado.equals("E") && (!objCliTelefono.getInd_accion().equals("N") || !objCliTelefono.getInd_accion().equals("NM"))) {
     String cli_id = objCliTelefono.getCli_id();
     long cli_key = objCliTelefono.getCli_key();
     long clitel_id = objCliTelefono.getClitel_id();
     String ocd_usumod = objUsuCredential.getCuenta();
     objlstEliTelefono.add(new CliTelefono(cli_id, cli_key, clitel_id, ocd_usumod));
     }
     objlstCliTelefono.remove(posicion);
     habilitaBotonesTelefono(false, true);
     limpiarCamposTelefono();
     habilitaCamposTelefono(true);
     //limpiamos las listas
     lst_telefono.setSelectedIndex(-1);
     s_estado_telefono = "Q";
     }
     }
     });
     }
     }*/

    /*@Listen("onClick=#tbbtn_btn_eliminarf")
     public void botonEliminarFinanciero() throws SQLException {
     if (lst_financiero.getSelectedIndex() == -1) {
     Messagebox.show("Por favor seleccione el detalle a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     } else {
     String s_str = "Está seguro que desea eliminar el detalle financiero del cliente";
     Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {
     objCliFinanciero = lst_financiero.getSelectedItem().getValue();
     int posicion = lst_financiero.getSelectedIndex();
     if (s_estado.equals("E") && (!objCliFinanciero.getInd_accion().equals("N") || !objCliFinanciero.getInd_accion().equals("NM"))) {
     String cli_id = objCliFinanciero.getCli_id();
     long cli_key = objCliFinanciero.getCli_key();
     long clitel_id = objCliFinanciero.getClifin_id();
     String ocd_usumod = objUsuCredential.getCuenta();
     objlstEliFinanciero.add(new CliFinanciero(emp_id, suc_id, cli_id, cli_key, clitel_id, ocd_usumod));
     }
     objlstCliFinanciero.remove(posicion);
     habilitaBotonesFinanciero(false, true);
     limpiarCamposFinanciero();
     habilitaCamposFinanciero(true);
     //limpiamos las listas
     lst_financiero.setSelectedIndex(-1);
     s_estado_financiero = "Q";
     }
     }
     });
     }
     }*/
    @Listen("onClick=#tbbtn_btn_eliminarg")
    public void botonEliminarGarantia() throws SQLException {
        if (lst_garantia.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione el detalle a eliminar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Está seguro que desea eliminar el detalle de garantía del cliente";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objCliGarantia = lst_garantia.getSelectedItem().getValue();
                                int posicion = lst_garantia.getSelectedIndex();
                                if (s_estado.equals("E") && (!objCliGarantia.getInd_accion().equals("N") || !objCliGarantia.getInd_accion().equals("NM"))) {
                                    String cli_id = objCliGarantia.getCli_id();
                                    long cli_key = objCliGarantia.getCli_key();
                                    long cligar_id = objCliGarantia.getCligar_id();
                                    String ocd_usumod = objUsuCredential.getCuenta();
                                    objlstEliGarantia.add(new CliGarantia(emp_id, suc_id, cli_id, cli_key, cligar_id, ocd_usumod));
                                }
                                objlstCliGarantia.remove(posicion);
                                habilitaBotonesGarantia(false, true);
                                limpiarCamposGarantia();
                                habilitaCamposGarantia(true);
                                //limpiamos las listas
                                lst_garantia.setSelectedIndex(-1);
                                s_estado_garantia = "Q";
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstCliente == null || objlstCliente.isEmpty()) {
            Messagebox.show("No hay registros de clientes para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/cxc/mantenimiento/LovImpresionCliente.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onOK=#txt_clidir_zona")
    public void lovZonas() {
        if (bandera == false) {
            bandera = true;
            if (txt_clidir_zona.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                modoEjecucion = "mantProgZonas";
                objLovZonas.put("txt_zonid", txt_clidir_zona);
                objLovZonas.put("txt_zondes", txt_clidir_zonadesc);
                objLovZonas.put("txt_horentid", txt_clidir_horario);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerCliente");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovZonas.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_clidir_idhorario.focus();
            }
        }
    }

    @Listen("onBlur=#txt_clidir_zona")
    public void validaZona() throws SQLException {
        if (!txt_clidir_zona.getValue().isEmpty()) {
            if (!txt_clidir_zona.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor solo ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidir_zona.setValue("");
                        txt_clidir_zonadesc.setValue("");
                        txt_clidir_zona.focus();
                    }
                });
            } else if (txt_clidir_zona.getValue().length() < 8) {
                Messagebox.show("El código ingresado debe ser de 8 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidir_zona.setValue("");
                        txt_clidir_zonadesc.setValue("");
                        txt_clidir_zona.focus();
                    }
                });
            } else {
                objZona = new DaoZonas().buscaZonaxCodigo(txt_clidir_zona.getValue());
                if (objZona == null) {
                    Messagebox.show("El código ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_clidir_zona.setValue("");
                            txt_clidir_zonadesc.setValue("");
                            txt_clidir_zona.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Zona con codigo " + objZona.getZon_id() + " y ha encontrado 1 registro(s)");
                    txt_clidir_zona.setValue(objZona.getZon_id());
                    txt_clidir_zonadesc.setValue(objZona.getZon_des());
                    txt_clidir_vendedor.setValue(objZona.getZon_idven() + " - " + objZona.getZon_apenom());
                    txt_clidir_idubigeo.setValue(objZona.getZon_ubiid());
                    txt_clidir_ubigeo.setValue(objZona.getZon_ubides());
                    txt_clidir_idpost.setValue(String.valueOf(objZona.getZon_postid()));
                    txt_clidir_postal.setValue(objZona.getZon_postdes());
                    txt_clidir_idhorario.focus();
                }
            }
        } else {
            txt_clidir_zonadesc.setValue("");
            txt_clidir_vendedor.setValue("");
            txt_clidir_idubigeo.setValue("");
            txt_clidir_ubigeo.setValue("");
            txt_clidir_idpost.setValue("");
            txt_clidir_postal.setValue("");
            txt_clidir_idhorario.setValue("");
            txt_clidir_horario.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_clidir_idhorario")
    public void lovHorario() {
        if (bandera == false) {
            bandera = true;
            if (txt_clidir_idhorario.getValue().isEmpty()) {
                Map objLovZonas = new HashedMap();
                modoEjecucion = "mantDirecciones";
                objLovZonas.put("txt_clidir_zona", txt_clidir_zona);
                objLovZonas.put("txt_clidir_idhorario", txt_clidir_idhorario);
                objLovZonas.put("txt_clidir_horario", txt_clidir_horario);
                objLovZonas.put("validaBusqueda", modoEjecucion);
                objLovZonas.put("controlador", "ControllerCliente");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovHorario.zul", null, objLovZonas);
                window.doModal();
            } else {
                txt_clidir_idgiro.focus();
            }
        }
    }

    @Listen("onBlur=#txt_clidir_idhorario")
    public void validaHorario() throws SQLException {
        if (!txt_clidir_idhorario.getValue().isEmpty()) {
            if (!txt_clidir_zona.getValue().isEmpty()) {
                if (!txt_clidir_idhorario.getValue().matches("[0-9]*")) {
                    Messagebox.show("Por favor solo ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_clidir_horario.setValue("");
                            txt_clidir_horario.setValue("");
                            txt_clidir_idhorario.focus();
                        }
                    });
                } else if (txt_clidir_idhorario.getValue().length() < 3) {
                    Messagebox.show("El código ingresado debe ser de 3 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_clidir_horario.setValue("");
                            txt_clidir_horario.setValue("");
                            txt_clidir_idhorario.focus();
                        }
                    });
                } else {
                    Horario objHorario = new DaoHorario().getHorarioxZona(txt_clidir_zona.getValue(),
                            Integer.parseInt(txt_clidir_idhorario.getValue()));
                    if (objHorario == null) {
                        Messagebox.show("El horario programado para esta zona no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                            public void onEvent(Event t) throws Exception {
                                txt_clidir_horario.setValue("");
                                txt_clidir_horario.setValue("");
                                txt_clidir_idhorario.focus();
                            }
                        });
                    } else {
                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Horario con Codigo " + objHorario.getTab_subdir() + " y ha encontrado 1 registro(s)");
                        txt_clidir_idhorario.setValue(objHorario.getTab_subdir());
                        txt_clidir_horario.setValue(objHorario.getTab_subdes());
                    }
                }
            } else {
                Messagebox.show("Primero tiene que seleccionar el código de la zona", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidir_horario.setValue("");
                        txt_clidir_horario.setValue("");
                        txt_clidir_zona.focus();
                    }
                });
            }
        } else {
            txt_clidir_horario.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_clidir_idgiro")
    public void lov_txt_clidir_idgiro() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_clidir_idgiro.getValue().isEmpty()) {
                Map parametros = new HashMap();
                modoEjecucion = "mantDirecciones";
                parametros.put("txt_clidir_idgiro", txt_clidir_idgiro);
                parametros.put("txt_clidir_giro", txt_clidir_giro);
                //parametros.put("btn_aceptar", btn_aceptar);
                parametros.put("validaBusqueda", modoEjecucion);
                parametros.put("controlador", "ControllerLovMantDirecciones");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovGiro.zul", null, parametros);
                window.doModal();
            } else {
                //tbbtn_btn_guardard.focus();
                //txt_clidir_idhorario.focus();
                botonGuardarDireccion();
            }
        }
    }

    @Listen("onBlur=#txt_clidir_idgiro")
    public void validarGiro() throws SQLException {
        if (!txt_clidir_idgiro.getValue().isEmpty()) {
            if (!txt_clidir_idgiro.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidir_idgiro.setValue("");
                        txt_clidir_giro.setValue("");
                        txt_clidir_idgiro.focus();
                    }
                });
            } else if (txt_clidir_idgiro.getValue().length() < 3) {
                Messagebox.show("Por favor ingrese un código de 3 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidir_idgiro.setValue("");
                        txt_clidir_giro.setValue("");
                        txt_clidir_idgiro.focus();
                    }
                });
            } else {
                Giro objGiro = new DaoGiro().buscaGiroxCodigo(String.valueOf(Integer.parseInt(txt_clidir_idgiro.getValue())));
                if (objGiro == null) {
                    Messagebox.show("El código de giro ingresado no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_clidir_idgiro.setValue("");
                            txt_clidir_giro.setValue("");
                            txt_clidir_idgiro.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Giro con codigo " + objGiro.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_clidir_idgiro.setValue(objGiro.getTab_subdir());
                    txt_clidir_giro.setValue(objGiro.getTab_subdes());
                }
            }
        } else {
            txt_clidir_giro.setValue("");
        }
        bandera = false;
    }

    @Listen("onCheck=#chk_clidir_default")
    public void validarDefaulf() {
        if (chk_clidir_default.isChecked()) {
            if (validarDefault()) {
                Messagebox.show("Este cliente ya tiene una dirección Default asignada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                chk_clidir_default.setChecked(false);
            }
        }
    }

    @Listen("onCheck=#chk_cliperju")
    public void validarperjur() {
        if (chk_cliperju.isChecked()) {
            if ("RUC".equals(cb_tipodoc.getValue())) {
                txt_clifecnac.setValue(null);
                txt_clidni.setValue("");
                txt_clidni.setDisabled(true);
                txt_clifecnac.setDisabled(true);
            }
            txt_cliruc.setDisabled(false);
        } else {
            if ("RUC".equals(cb_tipodoc.getValue())) {
                txt_cliruc.setDisabled(false);
            } else {
                txt_cliruc.setValue(null);
            }
            txt_clifecnac.setDisabled(false);
        }
        txt_cliruc.focus();

    }

    @Listen("onClick=#btn_mant_financiero")
    public void lov_btn_financiero() {
        Map parametros = new HashMap();
        modoEjecucion = "mantFinancierosAdd";
        parametros.put("cb_categoria", cb_categoria);
        parametros.put("txt_clifin_limcred", txt_clifin_limcred);
        parametros.put("txt_clifin_limdoc", txt_clifin_limdoc);
        parametros.put("validaBusqueda", modoEjecucion);
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovFinancieros.zul", null, parametros);
        window.doModal();
    }

    @Listen("onClick=#btn_mant_garantia")
    public void lov_btn_garantia() {
        Map parametros = new HashMap();
        modoEjecucion = "mantGarantiasAdd";
        parametros.put("cb_garantia", cb_garantia);
        parametros.put("txt_cligar_monto", txt_cligar_monto);
        parametros.put("txt_cligar_obs", txt_cligar_obs);
        parametros.put("validaBusqueda", modoEjecucion);
        Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovGarantias.zul", null, parametros);
        window.doModal();
    }

    @Listen("onOK=#cb_moneda")
    public void lov_cb_moneda() {
        cb_lprecio.focus();
        cb_lprecio.select();
    }

    @Listen("onOK=#cb_lprecio")
    public void lov_cb_lprecio() {
        cb_canal.focus();
        cb_canal.select();
    }

    @Listen("onOK=#cb_canal")
    public void lov_cb_canal() {
        cb_condicion.focus();
        cb_condicion.select();
    }

    @Listen("onOK=#cb_condicion")
    public void lov_cb_condicion() {
        cb_fpago.focus();
        cb_fpago.select();
    }

    @Listen("onOK=#cb_fpago")
    public void lov_cb_fpago() {
        txt_cli_dscto.focus();
    }

    @Listen("onCtrlKey=#tabbox_cliente")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 121:
                if (!tbbtn_btn_guardar.isDisabled()) {
                    botonGuardar();
                }
                break;
        }
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstCliente = new ListModelList<Cliente>();
            objlstCliente = objDaoCliente.listaCliente(emp_id, suc_id, 1);
            lst_cliente.setModel(objlstCliente);
        }
    }

    @Listen("onOK=#cb_tipodoc;onBlur=#cb_tipodoc")
    public void select_cb_tipodoc() {
        String sel;
        sel = cb_tipodoc.getValue();
        if ("RUC".equals(sel)) {
            txt_clidni.setDisabled(true);
            txt_cliruc.setDisabled(false);
            if (s_estado.equals("N")) {
                txt_clidni.setValue("");
                txt_clifecnac.setValue(null);
                chk_cliperju.setDisabled(false);
            } else {
                chk_cliperju.setDisabled(true);
            }
            if (chk_cliperju.isChecked()) {
                txt_clifecnac.setDisabled(true);
            } else {
                txt_clifecnac.setDisabled(false);
            }
            chk_cli_factura.setDisabled(false);
            txt_cliruc.focus();
            flag = 2;
        } else if ("DNI".equals(sel)) {
            txt_clidni.setDisabled(false);
            txt_cliruc.setDisabled(true);
            chk_cliperju.setDisabled(true);
            chk_cliperju.setChecked(false);
            chk_cli_factura.setChecked(false);
            chk_cli_factura.setDisabled(true);
            txt_cliruc.setValue(null);
            txt_clifecnac.setDisabled(false);
            txt_clidni.focus();
            flag = 1;
        } else {
            txt_clidni.setDisabled(false);
            txt_cliruc.setDisabled(true);
            chk_cliperju.setDisabled(true);
            chk_cliperju.setChecked(false);
            chk_cli_factura.setChecked(false);
            chk_cli_factura.setDisabled(true);
            txt_cliruc.setValue(null);
            txt_clifecnac.setDisabled(false);
            txt_clidni.focus();
            flag = 4;
        }
    }

    @Listen("onOK=#txt_cliapepat")
    public void next_txt_cliapepat() {
        txt_cliapepat.setValue(txt_cliapepat.getValue().trim());
        txt_cliapemat.focus();
    }

    @Listen("onBlur=#txt_cliapepat")
    public void valida_txt_cliapepat() {
        txt_clirazsoc.setValue(txt_cliapepat.getValue() + " " + txt_cliapemat.getValue() + " " + txt_clinombre.getValue());
    }

    @Listen("onOK=#txt_cliapemat")
    public void next_txt_cliapemat() {
        txt_cliapemat.setValue(txt_cliapemat.getValue().trim());
        txt_clinombre.focus();
    }

    @Listen("onBlur=#txt_cliapemat")
    public void valida_txt_cliapemat() {
        txt_clirazsoc.setValue(txt_cliapepat.getValue() + " " + txt_cliapemat.getValue() + " " + txt_clinombre.getValue());
    }

    @Listen("onOK=#txt_clinombre")
    public void next_txt_clinombre() {
        txt_clinombre.setValue(txt_clinombre.getValue().trim());
        txt_clirazsoc.focus();
    }

    @Listen("onBlur=#txt_clinombre")
    public void valida_txt_clinombre() {
        txt_clirazsoc.setValue(txt_cliapepat.getValue() + " " + txt_cliapemat.getValue() + " " + txt_clinombre.getValue());
    }

    @Listen("onOK=#txt_clirazsoc")
    public void next_txt_clirazsoc() {
        if (chk_cliperju.isChecked()) {
            txt_cliemail1.focus();
        } else {
            //txt_clifecnac.focus();
            cb_tipodoc.focus();
        }
    }

    @Listen("onOK=#txt_clidni")
    public void next_txt_clidni() throws SQLException {
        validaDocumento();
        txt_clifecnac.focus();
        //  txt_cliapepat.focus();
    }

    @Listen("onBlur=#txt_clidni")
    public void valida_clidni() throws SQLException {
        if (!txt_clidni.getValue().isEmpty()) {
            if (!txt_clidni.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidni.setValue("");
                        txt_clidni.focus();
                    }
                });
            } /*else {
             if (txt_clidni.getValue().length() == 8) {
             String nrodoc;
             ParametrosSalida objParamSalida;
             nrodoc = txt_clidni.getValue();
             objParamSalida = objDaoCliente.ExistenciaDocumento(txt_cliid.getValue().equals("") ? "0" : txt_cliid.getValue(), nrodoc, 1);
             if (objParamSalida.getFlagIndicador() == 1) {
             Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
             @Override
             public void onEvent(Event event) throws Exception {
             txt_clidni.focus();
             }
             });
             } else {
             if (txt_cliapepat.getValue().isEmpty()) {
             txt_cliapepat.focus();
             } else {
             txt_clifecnac.focus();
             }
             }
             } else {
             Messagebox.show("Debe ingresar 8 dígitos", "ERP-JIMVER", Messagebox.OK,
             Messagebox.INFORMATION, new EventListener() {
             @Override
             public void onEvent(Event event) throws Exception {
             if (((Integer) event.getData()).intValue() == Messagebox.OK) {
             txt_clidni.focus();
             }
             }
             });
             }
             }*/

        }
    }

    @Listen("onOK=#txt_clifecnac")
    public void next_txt_clifecnac() {
        txt_cliemail1.focus();
    }

    @Listen("onBlur=#txt_clifecnac")
    public void valida_clifecnac() {
        if (txt_clifecnac.getValue() != null) {
            int edad = objUtil.validaEdadMayor(txt_clifecnac.getValue());
            if (edad < 18) {
                Messagebox.show("El cliente es menor de edad", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        //txt_clifecnac.setValue(null);
                        txt_clifecnac.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#txt_cliruc")
    public void next_txt_cliruc() throws SQLException {
        txt_cliapepat.focus();
    }

    @Listen("onBlur=#txt_cliruc")
    public void valida_cliruc() throws SQLException {
        if (txt_cliruc.getValue() != null) {
            if (txt_cliruc.getValue().toString().length() == 11) {
                String pridosdig = StringFns.substring(txt_cliruc.getValue().toString(), 0, 2);
                if ("20".equals(pridosdig) && chk_cliperju.isChecked() || "10".equals(pridosdig) && !chk_cliperju.isChecked() || "15".equals(pridosdig) && !chk_cliperju.isChecked()) {
                    long nroruc;
                    ParametrosSalida objParamSalida;
                    nroruc = txt_cliruc.getValue();
                    objParamSalida = objDaoCliente.ExistenciaDocumento(txt_cliid.getValue().equals("") ? "0" : txt_cliid.getValue(), String.valueOf(nroruc), 2);
                    if (objParamSalida.getFlagIndicador() == 1) {
                        Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                txt_cliruc.focus();
                            }
                        });
                    } else {
                        String gendni = StringFns.substring(txt_cliruc.getValue().toString(), 2, 10);
                        if ("10".equals(pridosdig) /*&& !chk_cliperju.isChecked()*/) {
                            txt_clidni.setValue(gendni);
                            objParamSalida = objDaoCliente.ExistenciaDocumento(txt_cliid.getValue().equals("") ? "0" : txt_cliid.getValue(), txt_clidni.getValue(), 1);
                            if (objParamSalida.getFlagIndicador() == 1) {
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        txt_clidni.setValue("");
                                        txt_cliruc.focus();
                                    }
                                });
                            }
                        } else if ("15".equals(pridosdig) /*&& !chk_cliperju.isChecked()*/) {
                            txt_clidni.setDisabled(false);
                            txt_clidni.setValue("");
                            txt_clidni.focus();
                        } else {
                            txt_cliapepat.focus();
                        }
                    }
                } else {
                    //Messagebox.show("El numero de RUC es incorrecto", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.show("El RUC ingresado es incorrecto, verifique el tipo de persona", "ERP-JIMVER", Messagebox.OK,
                            Messagebox.INFORMATION, new EventListener() {
                                @Override
                                public void onEvent(Event event) throws Exception {
                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                        txt_cliruc.setValue(null);
                                        txt_cliruc.focus();
                                    }
                                }
                            });
                }
            } else {
                Messagebox.show("Debe ingresar 11 dígitos", "ERP-JIMVER", Messagebox.OK,
                        Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    txt_cliruc.focus();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onOK=#txt_cliemail1")
    public void next_txt_cliemail1() {
        txt_cliemail2.focus();
    }

    @Listen("onOK=#txt_cliemail2")
    public void next_txt_cliemail2() {
        txt_clidirweb.focus();
    }

    @Listen("onOK=#txt_clidirweb")
    public void next_txt_clidirweb() {
        tab_direccion.setSelected(true);
        btn_mant_direccion.focus();
    }

    @Listen("onClick=#btn_mant_direccion")
    public void lov_btn_direccionAdd() {
        if (bandera == false) {
            bandera = true;
            Map parametros = new HashMap();
            modoEjecucion = "mantDireccionesAdd";
            parametros.put("txt_clidir_direccion", txt_clidir_direccion);
            parametros.put("txt_clidir_ref", txt_clidir_ref);
            parametros.put("txt_clidir_zona", txt_clidir_zona);
            parametros.put("objCliDireccion", objCliDireccion);
            parametros.put("validaBusqueda", modoEjecucion);
            parametros.put("controlador", "ControllerCliente");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMantDirecciones.zul", null, parametros);
            window.doModal();
        }
    }

    @Listen("onBlur=#btn_mant_direccion")
    public void valida_direccionAdd() {
        bandera = false;
    }

    @Listen("onOK=#txt_cli_dscto")
    public void next_txt_cli_dscto() {
        txt_clicredcor.focus();
    }

    @Listen("onOK=#txt_clicredcor")
    public void next_txt_clicredcor() {
        txt_clidoccor.focus();
    }
    @Listen("onBlur=#txt_clidoccor")
    public void verificadoc(){
     if(txt_clidoccor.getValue() != null){
         if(txt_clidoccor.getValue()<=0){
         Messagebox.show("Por favor ingresar valor mayor a cero", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_clidoccor.setValue(null);
                        txt_clidoccor.focus();
                    }

                });
            }
        }
    }
    @Listen("onOK=#txt_clidoccor")
    public void next_txt_clidoccor() {
        tab_direccion.setSelected(true);
        btn_mant_direccion.focus();
    }

    @Listen("onOK=#txt_clitel_telef1")
    public void next_txt_clitel_telef1() {
        txt_clitel_telef2.focus();
    }

    @Listen("onOK=#txt_clitel_telef2")
    public void next_txt_clitel_telef2() {
        txt_clitel_movil.focus();
    }

    @Listen("onOK=#txt_clitel_movil")
    public void next_txt_clitel_movil() throws SQLException {
        //botonGuardarTelefono();
    }

    @Listen("onOK=#cb_categoria")
    public void next_cb_categoria() {
        txt_clifin_limcred.focus();
    }

    @Listen("onOK=#txt_clifin_limcred")
    public void next_txt_clifin_limcred() throws SQLException {
        txt_clifin_limdoc.focus();
    }

    @Listen("onOK=#txt_clifin_limdoc")
    public void next_txt_clifin_limdoc() throws SQLException {
        //botonGuardarFinanciero();
    }

    @Listen("onOK=#cb_garantia")
    public void next_cb_garantia() {
        txt_cligar_monto.focus();
    }

    @Listen("onOK=#txt_cligar_monto")
    public void next_txt_cligar_monto() {
        txt_cligar_obs.focus();
    }

    @Listen("onOK=#txt_cligar_obs")
    public void next_txt_cligar_obs() throws SQLException {
        botonGuardarGarantia();
    }

    //Eventos Otros 
    public void limpiarLista() {
        objlstCliente = null;
        lst_cliente.setModel(objlstCliente);
        objlstCliDireccion = null;
        lst_direccion.setModel(objlstCliDireccion);
        //objlstCliFinanciero = null;
        //lst_financiero.setModel(objlstCliFinanciero);
        objlstCliGarantia = null;
        lst_garantia.setModel(objlstCliGarantia);
        //objlstCliTelefono = null;
        //lst_telefono.setModel(objlstCliTelefono);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public boolean validarDefault() {
        int i = 0;
        boolean validar = false;
        while (i < objlstCliDireccion.size() && !validar) {
            if (objlstCliDireccion.get(i).getClizon_default() == 1) {
                validar = true;
            }
            i++;
        }
        return validar;
    }

    /*public boolean verificarCreditoRegistrado() {
     long datos[] = sumaCreditos();
     long cli_credcor = txt_clicredcor.getValue() == null ? 0 : txt_clicredcor.getValue();
     long cli_doccor = txt_clidoccor.getValue() == null ? 0 : txt_clidoccor.getValue();
     return datos[0] > cli_credcor || datos[1] > cli_doccor;
     }

     public long[] sumaCreditos() {
     long datos[] = new long[2];
     for (int i = 0; i < objlstCliFinanciero.getSize(); i++) {
     datos[0] = datos[0] + objlstCliFinanciero.get(i).getClifin_limcred();
     datos[1] = datos[1] + objlstCliFinanciero.get(i).getClifin_limdoc();
     }
     return datos;
     }*/
    public boolean verificarCreditoRegistrado() throws SQLException {
        Object datos[] = sumaCreditos();
        double cli_limcredcorp = txt_clicredcor.getValue() == null ? 0 : txt_clicredcor.getValue();
        int cli_limdoccorp = txt_clidoccor.getValue() == null ? 0 : txt_clidoccor.getValue();
        return Double.parseDouble(datos[0].toString()) > cli_limcredcorp || Integer.parseInt(datos[1].toString()) > cli_limdoccorp;
    }

    public Object[] sumaCreditos() throws SQLException {
        //long datos[] = new long[2];
        String emp_suc = Utilitarios.lpad(String.valueOf(emp_id), 3, "0").concat(Utilitarios.lpad(String.valueOf(suc_id), 3, "0"));

        Object datos[] = objDaoCliente.ValidaLimiteCredito(objCliente.getCli_id(), Long.parseLong(objCliente.getCli_id()), emp_suc);
        /*for (int i = 0; i < objlstCliFinanciero.getSize(); i++) {
         datos[0] = Integer.parseInt(datos[0].toString()) + objlstCliFinanciero.get(i).getClifin_limcred();
         datos[1] = Integer.parseInt(datos[1].toString()) + objlstCliFinanciero.get(i).getClifin_limdoc();
         }*/
        return datos;
    }

    public Object generaRegistroTelefono() throws SQLException {
        String cli_id;
        long cli_key;
        if (s_estado.equals("N")) {
            String fecha = Utilitarios.hoyAsString();
            objCliente = objDaoCliente.getCuentaContable(fecha);
            cli_id = objCliente.getCli_id();//txt_cliid.getValue();
            cli_key = Long.parseLong(cli_id);//txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());
        } else {
            cli_id = txt_cliid.getValue();
            cli_key = txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());
        }
       // long clitel_id = 0;
        //String cli_id = txt_cliid.getValue();
        //long cli_key = txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());
        long clitel_telef1 = txt_clitel_telef1.getValue() == null ? 0 : txt_clitel_telef1.getValue();
        long clitel_telef2 = txt_clitel_telef2.getValue() == null ? 0 : txt_clitel_telef2.getValue();
        long clitel_movil = txt_clitel_movil.getValue() == null ? 0 : txt_clitel_movil.getValue();
        int clitel_est = 1;
        String cli_usuadd = objUsuCredential.getCuenta();
        String cli_usumod = objUsuCredential.getCuenta();
        return new CliTelefono(cli_id, cli_key, clitel_telef1, clitel_telef2, clitel_movil, clitel_est, cli_usuadd, cli_usumod);
    }

    public Object generaRegistroFinanciero() throws SQLException {
        String cli_id;
        long cli_key;

        if (s_estado.equals("N")) {
            String fecha = Utilitarios.hoyAsString();
            objCliente = objDaoCliente.getCuentaContable(fecha);
            cli_id = objCliente.getCli_id();//txt_cliid.getValue();
            cli_key = Long.parseLong(cli_id);//txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());
        } else {
            cli_id = txt_cliid.getValue();
            cli_key = txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());

        }

        //long clifin_id = 0;

        int suc_id = objUsuCredential.getCodsuc();
        int emp_id = objUsuCredential.getCodemp();
        double clifin_limcred = txt_clifin_limcred.getValue() == null ? 0.00 : txt_clifin_limcred.getValue();
        int clifin_limdoc = txt_clifin_limdoc.getValue() == null ? 0 : txt_clifin_limdoc.getValue();
        int clifin_est = 1;
        String cli_usuadd = objUsuCredential.getCuenta();
        String cli_usumod = objUsuCredential.getCuenta();
        int clifin_categ =cb_categoria.getSelectedItem()==null ? 1 :Integer.parseInt(cb_categoria.getSelectedItem().getValue().toString());
        return new CliFinanciero( cli_id, cli_key, suc_id, emp_id, clifin_limcred, clifin_limdoc, clifin_est, cli_usuadd, cli_usumod, clifin_categ);
    }

    public Object generaRegistro() throws SQLException {
        //long datos[];
        Object datos[];
        double cli_limcredemp;
        int cli_limdocemp;
        String cli_id = txt_cliid.getValue();
        long cli_key = txt_cliid.getValue().isEmpty() ? 0 : Long.parseLong(txt_cliid.getValue());
        String cli_apepat = txt_cliapepat.getValue().toUpperCase();
        String cli_apemat = txt_cliapemat.getValue().toUpperCase();
        String cli_nombre = txt_clinombre.getValue().toUpperCase();
        String cli_razsoc = txt_clirazsoc.getValue().toUpperCase();
        Date cli_fecnac = txt_clifecnac.getValue();
        long cli_ruc = txt_cliruc.getValue() == null ? 0 : txt_cliruc.getValue();
        String cli_dirweb = txt_clidirweb.getValue().toUpperCase();
        String cli_email1 = txt_cliemail1.getValue().toUpperCase();
        String cli_email2 = txt_cliemail2.getValue().toUpperCase();
        int cli_est = chk_cliestado.isChecked() ? 1 : 2;
        String cli_dni = txt_clidni.getValue();
        int cli_tipodoc = cb_tipodoc.getSelectedItem().getValue();
        int cli_perju = chk_cliperju.isChecked() ? 1 : 0;
        double cli_limcredcorp = txt_clicredcor.getValue() == null ? 0 : txt_clicredcor.getValue();
        int cli_limdoccorp = txt_clidoccor.getValue() == null ? 0 : txt_clidoccor.getValue();
        double cli_dscto = txt_cli_dscto.getValue() == null ? 0 : txt_cli_dscto.getValue();
        //int cli_factura = chk_cli_factura.isChecked() ? 1 : 0;
        int cli_mon = /*cb_moneda.getSelectedIndex() == -1 ? 0 :*/ Integer.parseInt(cb_moneda.getSelectedItem().getValue().toString());
        //int cli_lista = /*cb_lprecio.getSelectedIndex() == -1 ? 0 :*/ Integer.parseInt(cb_lprecio.getSelectedItem().getValue().toString());
        int cli_con = /*cb_condicion.getSelectedIndex() == -1 ? 0 :*/ Integer.parseInt(cb_condicion.getSelectedItem().getValue().toString());
        int cli_canal = /*cb_canal.getSelectedIndex() == -1 ? 0 :*/ Integer.parseInt(cb_canal.getSelectedItem().getValue().toString());
        int forpag_key = /*cb_fpago.getSelectedIndex() == -1 ? 0 :*/ Integer.parseInt(cb_fpago.getSelectedItem().getValue().toString());
        String forpag_id = Utilitarios.lpad(String.valueOf(forpag_key), 4, "0");
        int cli_emprel = chk_cli_emprel.isChecked() ? 1 : 0;
        int cli_perc = chk_cli_perc.isChecked() ? 1 : 0;
        if (!s_estado.equals("N")) {
            datos = sumaCreditos();
            cli_limcredemp = Double.parseDouble(datos[0].toString());
            cli_limdocemp = Integer.parseInt(datos[1].toString());
        } else {
            cli_limcredemp = 0.00;
            cli_limdocemp = 0;
        }

        String cli_usuadd = objUsuCredential.getCuenta();
        String cli_usumod = objUsuCredential.getCuenta();
        //insertar registros de telefono y financiiero
       /* long telf1 = txt_clitel_telef1.getValue() == null ? 0 : txt_clitel_telef1.getValue();
         long telf2 = txt_clitel_telef2.getValue() == null ? 0 : txt_clitel_telef2.getValue();
         long movil = txt_clitel_movil.getValue() == null ? 0 : txt_clitel_movil.getValue();

         double limicred = txt_clifin_limcred.getValue() == null ? 0.00 : txt_clifin_limcred.getValue();
         int limidoc = txt_clifin_limdoc.getValue() == null ? 0 : txt_clifin_limdoc.getValue();
         int cat = cb_categoria.getSelectedItem().getValue();*/
        return new Cliente(cli_id, cli_key, cli_apepat, cli_apemat, cli_nombre, cli_razsoc, cli_fecnac, cli_ruc, cli_dirweb,
                cli_email1, cli_email2, cli_est, cli_dni, cli_tipodoc, cli_perju, cli_limcredcorp, cli_limdoccorp, cli_dscto,
                cli_mon, cli_con, cli_canal, forpag_key, forpag_id, cli_emprel, cli_perc, cli_usuadd, cli_usumod, cli_limcredemp,
                cli_limdocemp);
        /*return new Cliente(cli_id, cli_key, cli_apepat, cli_apemat, cli_nombre, cli_razsoc, cli_fecnac, cli_ruc, cli_dirweb,
         cli_email1, cli_email2, cli_est, cli_dni, cli_tipodoc, cli_perju, cli_limcredcorp, cli_limdoccorp, cli_dscto,
         cli_mon, cli_con, cli_canal, forpag_key, forpag_id, cli_emprel, cli_perc, cli_usuadd, cli_usumod, cli_limcredemp,
         cli_limdocemp);*/
    }

    public String verificar() {
        String s_valor;
        if (txt_cliapepat.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el apellido paterno de cliente";
            regreso = "paterno";
            // txt_cliapepat.focus();
        } else if (txt_cliapemat.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el apellido materno de cliente";
            // txt_cliapemat.focus();
            regreso = "materno";
        } else if (txt_clinombre.getValue().isEmpty()) {
            s_valor = "Por favor ingrese el nombre de cliente";
            //txt_clinombre.focus();
            regreso = "nombre";
        } else if (txt_clirazsoc.getValue().isEmpty()) {
            s_valor = "Por favor verifique los datos de razón social";

            regreso = "razon";
        } else if (txt_clifecnac.getValue() == null && !txt_clidni.getValue().equals("")) {
            s_valor = "Verifique datos en la fecha de nacimiento";

            regreso = "nacimiento";
        } /*else if (txt_clidni.getValue().equals("") && (txt_cliruc.getValue() == null || txt_cliruc.getValue() == 0)) {
         s_valor = "Por favor ingrese un número de documento(DNI/RUC) segun sea el caso";
           
         regreso = "documento";*/ else if ("DNI".equals(cb_tipodoc.getValue()) && txt_clidni.getValue().equals("")) {
            s_valor = "Por favor ingrese un número de documento(DNI/RUC) segun sea el caso";
            regreso = "documento";

        } else if ("RUC".equals(cb_tipodoc.getValue()) && (txt_cliruc.getValue() == null || txt_cliruc.getValue() == 0)) {
            s_valor = "Por favor ingrese un número de documento(DNI/RUC) segun sea el caso";
            regreso = "documento1";

        } else if (("DNI".equals(cb_tipodoc.getValue()) || "CARNET EXTRANJERIA".equals(cb_tipodoc.getValue())) && txt_clidni.getValue().equals("")) {
            s_valor = "Por favor ingrese Documento de Indentidad";
            //txt_clifecnac.focus();
            regreso = "tipodoc";
        } else if ("RUC".equals(cb_tipodoc.getValue()) && (txt_cliruc.getValue() == null || txt_cliruc.getValue() == 0)) {
            s_valor = "Por favor ingrese el número de RUC";
            //txt_clifecnac.focus();
            regreso = "ruc";
        } else if (cb_lprecio.getSelectedIndex() == -1) {
            s_valor = "Lista de Precio";
            cb_lprecio.focus();
        }else if (txt_clicredcor.getValue()==null) {
            s_valor = "Por favor ingrese credito corporativo";
            //txt_clifecnac.focus();
            regreso = "corporativo";
        }else if(txt_clidoccor.getValue()==null){
            s_valor="Por favor ingrese n° doc.corporativo";
            regreso = "doccorporativo";
        
        }else if( txt_clifin_limcred.getValue()!=null && txt_clifin_limcred.getValue()>txt_clicredcor.getValue()){
            s_valor = "Por favor ingrese monto menor a credito corporativo";
            regreso = "limicre";
        }else if(txt_clifin_limdoc.getValue()!=null && txt_clifin_limdoc.getValue()>txt_clidoccor.getValue()){
            s_valor = "por favor ingrese n° documento menor al de corporativo";
            regreso = "doccre";       
        }
        else {
            s_valor = "";
        }
        return s_valor;
    }
    

    public void nextSalto() {
        if (regreso.equals("paterno")) {
            txt_cliapepat.focus();
        } else if (regreso.equals("materno")) {
            txt_cliapemat.focus();
        } else if (regreso.equals("nombre")) {
            txt_clinombre.focus();
        } else if (regreso.equals("razon")) {
            txt_clirazsoc.focus();
        } else if (regreso.equals("nacimiento")) {
            txt_clifecnac.focus();
        } else if (regreso.equals("documento")) {
            txt_clidni.focus();
        } else if (regreso.equals("tipodoc")) {
            cb_tipodoc.focus();
        } else if (regreso.equals("documento1")) {
            txt_cliruc.focus();
        } else if (regreso.equals("corporativo")){
            txt_clicredcor.focus();
        } else if (regreso.equals("doccorporativo")){
            txt_clidoccor.focus();
        } else if (regreso.equals("limicre")){
            txt_clifin_limcred.focus();
        }else if (regreso.equals("doccre")){
           txt_clifin_limdoc.focus();
        }
    }

    public String validaDocumento() throws SQLException {
        String validadoc;
        if (txt_clidni.getValue().length() == 8) {
            objParamSalida = objDaoCliente.ExistenciaDocumento(txt_cliid.getValue().equals("") ? "0" : txt_cliid.getValue(), txt_clidni.getValue(), 1);
            if (objParamSalida.getFlagIndicador() == 1) {
                validadoc = "Ocurrió un error durante la validación del documento";
            } else {
                validadoc = "";
            }
        } else {
            validadoc = "";
        }
        return validadoc;
    }

    public void llenarCampos(Cliente objCliente) throws SQLException {
        //chk_cliestado.setChecked(objCliente.getCli_est() == 1);
        txt_cliid.setValue(objCliente.getCli_id());
        txt_cliapepat.setValue(objCliente.getCli_apepat());
        txt_cliapemat.setValue(objCliente.getCli_apemat());
        txt_clinombre.setValue(objCliente.getCli_nombre());
        txt_clirazsoc.setValue(objCliente.getCli_razsoc());
        cb_tipodoc.setSelectedItem(Utilitarios.valorPorTexto1(cb_tipodoc, objCliente.getCli_tipodoc()));
        txt_clidni.setValue(objCliente.getCli_dni());
        txt_clifecnac.setValue(objCliente.getCli_fecnac());
        chk_cliperju.setChecked(objCliente.getCli_perju() == 1);
        txt_cliruc.setValue(objCliente.getCli_ruc());
        txt_cliemail1.setValue(objCliente.getCli_email1());
        txt_cliemail2.setValue(objCliente.getCli_email2());
        txt_clidirweb.setValue(objCliente.getCli_dirweb());
        //
        cb_moneda.setSelectedItem(Utilitarios.valorPorTexto1(cb_moneda, objCliente.getCli_mon()));
        cb_lprecio.setSelectedItem(Utilitarios.valorPorTexto1(cb_lprecio, objCliente.getCli_lista()));
        //cb_lprecio.setSelectedIndex(objCliente.getCli_lista());
        cb_canal.setSelectedItem(Utilitarios.valorPorTexto1(cb_canal, objCliente.getCli_canal()));
        cb_condicion.setSelectedItem(Utilitarios.valorPorTexto1(cb_condicion, objCliente.getCli_con()));
        cb_fpago.setSelectedItem(Utilitarios.valorPorTexto(cb_fpago, objCliente.getForpag_key()));

        chk_cli_factura.setChecked(objCliente.getCli_factura() == 1);
        chk_cli_perc.setChecked(objCliente.isVal_perc());
        txt_cli_dscto.setValue(objCliente.getCli_dscto());
        txt_clicredcor.setValue(objCliente.getCli_limcredcorp());
        txt_clidoccor.setValue(objCliente.getCli_limdoccorp());
        chk_cliestado.setChecked(objCliente.getCli_est() == 1);
        chk_cli_emprel.setChecked(objCliente.isVal_rel());

       // objCliFinanciero = objDaoCliente.getClienteFin(objCliente.getCli_id().toString(), emp_id, suc_id);
        String emp_suc = Utilitarios.lpad(String.valueOf(emp_id), 3, "0").concat(Utilitarios.lpad(String.valueOf(suc_id), 3, "0"));
        Object datos[] = objDaoCliente.ValidaLimiteCredito(objCliente.getCli_id(), Long.parseLong(objCliente.getCli_id()), emp_suc);
        db_saldocredcorp.setValue(Double.parseDouble(datos[2].toString()) - Double.parseDouble(datos[0].toString()) - (objCliFinanciero == null ? 0.0 : objCliFinanciero.getClifin_limcred()));
        txt_saldodoccorp.setValue(Double.parseDouble(datos[3].toString()) - Double.parseDouble(datos[1].toString()) - (objCliFinanciero == null ? 0 : objCliFinanciero.getClifin_limdoc()));
        txt_usuadd.setValue(objCliente.getCli_usuadd());
        d_fecadd.setValue(objCliente.getCli_fecadd());
        txt_usumod.setValue(objCliente.getCli_usumod());
        d_fecmod.setValue(objCliente.getCli_fecmod());
    }

    public void llenarCamposDireccion(CliDireccion objCliDireccion) {
        chk_clidir_default.setChecked(objCliDireccion.getClizon_default() == 1);
        chk_clidir_estado.setChecked(objCliDireccion.getClizon_est() == 1);
        txt_clidir_id.setValue(objCliDireccion.getClidir_id());
        txt_clidir_direccion.setValue(objCliDireccion.getClidir_direc());
        txt_clidir_ref.setValue(objCliDireccion.getClidir_ref());
        txt_clidir_zona.setValue(objCliDireccion.getZon_id());
        txt_clidir_zonadesc.setValue(objCliDireccion.getZon_des());
        txt_clidir_vendedor.setValue(objCliDireccion.getVen_id());
        txt_clidir_idhorario.setValue(objCliDireccion.getHor_id());
        txt_clidir_horario.setValue(objCliDireccion.getHor_des());
        txt_clidir_idgiro.setValue(objCliDireccion.getGiro_id());
        txt_clidir_giro.setValue(objCliDireccion.getGiro_des());

        //cb_lprecio.setSelectedItem(objUtil.valorPorTexto1(cb_lprecio, objCliDireccion.getCli_lista()));
        //chk_cli_factura.setChecked(objCliDireccion.getCli_factura() == 1);
        txt_clidir_usuadd.setValue(objCliDireccion.getClidir_usuadd());
        txt_clidir_fecadd.setValue(objCliDireccion.getClidir_fecadd());
        txt_clidir_usumod.setValue(objCliDireccion.getClidir_usumod());
        txt_clidir_fecmod.setValue(objCliDireccion.getClidir_fecmod());
    }

    public void llenarCamposTelefono(CliTelefono objCliTelefono) {
        //chk_clitel_estado.setChecked(objCliTelefono.getClitel_est() == 1);
        txt_clitel_id.setValue(objCliTelefono.getClitel_id());
        txt_clitel_telef1.setValue(objCliTelefono.getClitel_telef1());
        txt_clitel_telef2.setValue(objCliTelefono.getClitel_telef2());
        txt_clitel_movil.setValue(objCliTelefono.getClitel_movil());
        //txt_clitel_usuadd.setValue(objCliTelefono.getClitel_usuadd());
        //txt_clitel_fecadd.setValue(objCliTelefono.getClitel_fecadd());
        //txt_clitel_usumod.setValue(objCliTelefono.getClitel_usumod());
        //txt_clitel_fecmod.setValue(objCliTelefono.getClitel_fecmod());
    }

    public void llenarCamposFinanciero(CliFinanciero objCliFinanciero) throws SQLException{
        //chk_clifin_estado.setChecked(objCliFinanciero.getClifin_est() == 1);
        txt_clifin_id.setValue(objCliFinanciero.getClifin_id());
      //  cb_categoria.setSelectedItem(Utilitarios.valorPorTexto1(cb_categoria, objCliFinanciero.getClifin_categ()));
        cb_categoria.setValue(objCliFinanciero.getCat());//setSelectedItem(Utilitarios.valorPorTexto1(cb_categoria, objCliFinanciero.getCat().toString()));
        txt_clifin_limcred.setValue(objCliFinanciero.getClifin_limcred());
        txt_clifin_limdoc.setValue(objCliFinanciero.getClifin_limdoc());
        //txt_clifin_usuadd.setValue(objCliFinanciero.getClifin_usuadd());
        //txt_clifin_fecadd.setValue(objCliFinanciero.getClifin_fecadd());
        //txt_clifin_usumod.setValue(objCliFinanciero.getClifin_usumod());
        //txt_clifin_fecmod.setValue(objCliFinanciero.getClifin_fecmod());
    }

    public void llenarCamposGarantia(CliGarantia objCliGarantia) {
        chk_cligar_estado.setChecked(objCliGarantia.getCligar_est() == 1);
        txt_cligar_id.setValue(objCliGarantia.getCligar_id());
        cb_garantia.setValue(objCliGarantia.getCligar_garantia());
        txt_cligar_monto.setValue(objCliGarantia.getCligar_monto());
        txt_cligar_obs.setValue(objCliGarantia.getCligar_obs());
        txt_cligar_usuadd.setValue(objCliGarantia.getCligar_usuadd());
        txt_cligar_fecadd.setValue(objCliGarantia.getCligar_fecadd());
        txt_cligar_usumod.setValue(objCliGarantia.getCligar_usumod());
        txt_cligar_fecmod.setValue(objCliGarantia.getCligar_fecmod());
    }

    public void limpiarCampos() {
        //mantenimiento
        txt_cliid.setValue("");
        txt_cliapepat.setValue("");
        txt_cliapemat.setValue("");
        txt_clinombre.setValue("");
        txt_clirazsoc.setValue("");
        cb_tipodoc.setSelectedIndex(-1);
        txt_clidni.setValue("");
        txt_clifecnac.setValue(null);
        chk_cliperju.setChecked(false);
        txt_cliruc.setValue(null);
        txt_cliemail1.setValue("");
        txt_cliemail2.setValue("");
        txt_clidirweb.setValue("");
        txt_cli_dscto.setValue(null);
        chk_cli_factura.setDisabled(true);
        chk_cli_factura.setChecked(false);
        chk_cli_emprel.setChecked(false);
        chk_cli_perc.setChecked(false);
        cb_moneda.setSelectedIndex(-1);
        cb_lprecio.setSelectedIndex(-1);
        cb_condicion.setSelectedIndex(-1);
        cb_canal.setSelectedIndex(-1);
        cb_fpago.setSelectedIndex(-1);
        txt_clicredcor.setValue(null);
        txt_clidoccor.setValue(null);
        db_saldocredcorp.setValue(null);
        txt_saldodoccorp.setValue(null);
        
    }

    public void limpiarCamposDireccion() {
        //mantenimiento
        txt_clidir_id.setValue(null);
        chk_clidir_estado.setChecked(false);
        chk_clidir_default.setChecked(false);
        txt_clidir_direccion.setValue("");
        txt_clidir_ref.setValue("");
        txt_clidir_zona.setValue("");
        txt_clidir_zonadesc.setValue("");
        txt_clidir_vendedor.setValue("");
        txt_clidir_idubigeo.setValue("");
        txt_clidir_ubigeo.setValue("");
        txt_clidir_idpost.setValue("");
        txt_clidir_postal.setValue("");
        txt_clidir_idhorario.setValue("");
        txt_clidir_horario.setValue("");
        txt_clidir_idgiro.setValue("");
        txt_clidir_giro.setValue("");
        txt_clidir_usuadd.setValue("");
        txt_clidir_fecadd.setValue(null);
        txt_clidir_usumod.setValue("");
        txt_clidir_fecmod.setValue(null);

        //chk_cli_factura.setDisabled(true);
        //chk_cli_factura.setChecked(false);
        //cb_lprecio.setSelectedIndex(-1);
    }

    public void limpiarCamposTelefono() {
        //mantenimiento
        txt_clitel_id.setValue(null);
        /*chk_clitel_estado.setChecked(false);*/
        txt_clitel_telef1.setValue(null);
        txt_clitel_telef2.setValue(null);
        txt_clitel_movil.setValue(null);
        /*txt_clitel_usuadd.setValue("");
         txt_clitel_fecadd.setValue(null);
         txt_clitel_usumod.setValue("");
         txt_clitel_fecmod.setValue(null);*/
    }

    public void limpiarCamposFinanciero() {
        //mantenimiento
        txt_clifin_id.setValue(null);
        /*chk_clifin_estado.setChecked(false);*/
        cb_categoria.setSelectedIndex(-1);
        txt_clifin_limcred.setValue(null);
        txt_clifin_limdoc.setValue(null);
        /*txt_clifin_usuadd.setValue("");
         txt_clifin_fecadd.setValue(null);
         txt_clifin_usumod.setValue("");
         txt_clifin_fecmod.setValue(null);*/
    }

    public void limpiarCamposGarantia() {
        //mantenimiento
        txt_cligar_id.setValue(null);
        chk_cligar_estado.setChecked(false);
        cb_garantia.setSelectedIndex(-1);
        txt_cligar_monto.setValue(null);
        txt_cligar_obs.setValue("");
        txt_cligar_usuadd.setValue("");
        txt_cligar_fecadd.setValue(null);
        txt_cligar_usumod.setValue("");
        txt_cligar_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida1) {
        //mantenimiento
        txt_cliapepat.setDisabled(b_valida1);
        txt_cliapemat.setDisabled(b_valida1);
        txt_clinombre.setDisabled(b_valida1);
        txt_clirazsoc.setDisabled(b_valida1);
        cb_tipodoc.setDisabled(b_valida1);
        txt_clidni.setDisabled(b_valida1);
        txt_clifecnac.setDisabled(b_valida1);
        chk_cliperju.setDisabled(b_valida1);
        txt_cliruc.setDisabled(b_valida1);
        txt_cliemail1.setDisabled(b_valida1);
        txt_cliemail2.setDisabled(b_valida1);
        txt_clidirweb.setDisabled(b_valida1);
        txt_cli_dscto.setDisabled(b_valida1);
        chk_cli_factura.setDisabled(b_valida1);
        cb_moneda.setDisabled(b_valida1);
        cb_lprecio.setDisabled(b_valida1);
        cb_condicion.setDisabled(b_valida1);
        cb_canal.setDisabled(b_valida1);
        cb_fpago.setDisabled(b_valida1);
        chk_cli_emprel.setDisabled(b_valida1);
        chk_cli_perc.setDisabled(b_valida1);
        txt_clicredcor.setDisabled(b_valida1);
        txt_clidoccor.setDisabled(b_valida1);
    }

    public void habilitaCamposDireccion(boolean b_valida1) {
        //mantenimiento
        chk_clidir_estado.setDisabled(b_valida1);
        chk_clidir_default.setDisabled(b_valida1);
        btn_mant_direccion.setDisabled(b_valida1);
        txt_clidir_zona.setDisabled(b_valida1);
        txt_clidir_idhorario.setDisabled(b_valida1);
        txt_clidir_idgiro.setDisabled(b_valida1);
    }

    public void habilitaCamposTelefono(boolean b_valida1) {
        //mantenimiento
        //chk_clitel_estado.setDisabled(b_valida1);
        txt_clitel_telef1.setDisabled(b_valida1);
        txt_clitel_telef2.setDisabled(b_valida1);
        txt_clitel_movil.setDisabled(b_valida1);
    }

    public void habilitaCamposFinanciero(boolean b_valida1) {
        //mantenimiento
        //chk_clifin_estado.setDisabled(b_valida1);
        cb_categoria.setDisabled(b_valida1);
        txt_clifin_limcred.setDisabled(b_valida1);
        txt_clifin_limdoc.setDisabled(b_valida1);
    }

    public void habilitaCamposGarantia(boolean b_valida1) {
        //mantenimiento
        chk_cligar_estado.setDisabled(b_valida1);
        cb_garantia.setDisabled(b_valida1);
        txt_cligar_monto.setDisabled(b_valida1);
        txt_cligar_obs.setDisabled(b_valida1);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaBotonesDireccion(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevod.setDisabled(b_valida1);
        tbbtn_btn_editard.setDisabled(b_valida1);
        tbbtn_btn_eliminard.setDisabled(b_valida1);
        tbbtn_btn_deshacerd.setDisabled(b_valida2);
        tbbtn_btn_guardard.setDisabled(b_valida2);
    }

    /*public void habilitaBotonesTelefono(boolean b_valida1, boolean b_valida2) {
     tbbtn_btn_nuevot.setDisabled(b_valida1);
     tbbtn_btn_editart.setDisabled(b_valida1);
     tbbtn_btn_eliminart.setDisabled(b_valida1);
     tbbtn_btn_deshacert.setDisabled(b_valida2);
     tbbtn_btn_guardart.setDisabled(b_valida2);
     }*/

    /*public void habilitaBotonesFinanciero(boolean b_valida1, boolean b_valida2) {
     tbbtn_btn_nuevof.setDisabled(b_valida1);
     tbbtn_btn_editarf.setDisabled(b_valida1);
     tbbtn_btn_eliminarf.setDisabled(b_valida1);
     tbbtn_btn_deshacerf.setDisabled(b_valida2);
     tbbtn_btn_guardarf.setDisabled(b_valida2);
     }*/
    public void habilitaBotonesGarantia(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevog.setDisabled(b_valida1);
        tbbtn_btn_editarg.setDisabled(b_valida1);
        tbbtn_btn_eliminarg.setDisabled(b_valida1);
        tbbtn_btn_deshacerg.setDisabled(b_valida2);
        tbbtn_btn_guardarg.setDisabled(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacliente.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
        tab_direccion.setDisabled(b_valida2);
        //tab_financiero.setDisabled(b_valida2);
        //tab_garantia.setDisabled(b_valida2);
        tab_telefono.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listacliente.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public String verificarDirecciones() {
        String verificar;
        if (txt_clidir_direccion.getValue().isEmpty()) {
            verificar = "Direccion";
        } else if (txt_clidir_zona.getValue().isEmpty()) {
            verificar = "Zona";
        } else if (txt_clidir_idhorario.getValue().isEmpty()) {
            verificar = "Horario";
        } else {
            verificar = "";
        }
        return verificar;
    }

    public String verificarTelefonos() {
        String verificar;
        if (txt_clitel_telef1.getValue() == null && txt_clitel_telef2.getValue() == null && txt_clitel_movil.getValue() == null) {
            verificar = "Telefonos";
        } else if (txt_clitel_telef1.getValue() == 0 && txt_clitel_telef2.getValue() == 0 && txt_clitel_movil.getValue() == 0) {
            verificar = "Telefonos";
        } else {
            verificar = "";
        }
        return verificar;
    }

    public String verificarFinanciero() {
        String verificar;
        if (cb_categoria.getSelectedIndex() == -1) {
            verificar = "Categoria del Cliente";
        } else if (txt_clifin_limcred.getValue() == null || txt_clifin_limcred.getValue() <= 0.0) {
            verificar = "Limite Crediticio";
        } else if (txt_clifin_limdoc.getValue() == null || txt_clifin_limdoc.getValue() <= 0) {
            verificar = "Limite de Documentos";
        } else {
            verificar = "";
        }
        return verificar;
    }

    public String verificarGarantias() {
        String verificar;
        if (cb_garantia.getSelectedIndex() == -1) {
            verificar = "Tipo de Garantia";
        } else if (txt_cligar_monto.getValue() == null) {
            verificar = "Tipo de la garantia";
        } else {
            verificar = "";
        }
        return verificar;
    }

    //metodos sin utilizar
    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
