package org.me.gj.controller.logistica.mantenimiento;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.me.gj.controller.contabilidad.mantenimiento.DaoImpuesto;
import org.me.gj.controller.contabilidad.mantenimiento.DaoUnidMedida;
import org.me.gj.controller.contabilidad.mantenimiento.DaoMedSunat;
import org.me.gj.controller.contabilidad.mantenimiento.DaoExistSunat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import org.me.gj.model.contabilidad.mantenimiento.*;
import org.me.gj.model.logistica.mantenimiento.*;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import net.sourceforge.jbarcodebean.*;
import net.sourceforge.jbarcodebean.model.*;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoProvPresupuesto;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.facturacion.mantenimiento.ProvPresupuesto;
import org.me.gj.model.seguridad.mantenimiento.Accesos;

public class ControllerProductos extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_lista, tab_mantenimiento, tab_otros;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar,
            tbbtn_btn_deshacer, tbbtn_btn_guardar, tbbtn_btn_imprimir, tbbtn_btn_imagenpro, tbbtn_btn_elegir;
    @Wire
    Listbox lst_productos;
    @Wire
    Checkbox chk_proest, chk_busest, chk_proafecam, chk_elegir;
    @Wire
    Intbox txt_prostknofact, txt_prounimaster, txt_prounipresven, txt_prouniprescom, txt_prounibodegero;
    @Wire
    Textbox txt_prodescom, txt_provid, txt_proid, txt_prodescor, txt_procodori, txt_procodbar, txt_procodean13prov, txt_procodean14prov,
            txt_proimgcod, txt_proimg, txt_prodes, txt_busqueda, txt_provdes, txt_usuadd, txt_usumod,
            txt_codlinea, txt_deslinea, txt_codsublinea, txt_dessublinea, txt_codmarca, txt_desmarca,
            txt_provpresuid, txt_provpresudes;
    @Wire
    Combobox cb_prolin, cb_prosublin, cb_promar, cb_proclas, cb_protip, cb_proextsun, cb_promedsun,
            cb_prouniven, cb_proemppresven, cb_profrac, cb_proprocedencia, cb_conimp, //cb_proidprov,
            cb_proimp, cb_proubi, cb_prounimed, cb_prounicom, cb_proempprescom, cb_protipcodbar,
            cb_prorot, cb_busqueda, cb_proindperc, cb_proconv;
    @Wire
    Spinner sp_proordkar, sp_prolstpre, sp_procons, sp_ord;
    @Wire
    Doublebox txt_proalt, txt_proanc, txt_prolar, txt_propeso, txt_provol;
    @Wire
    Div img_foto, img_codigo;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    ListModelList<Productos> objlstProductos = new ListModelList<Productos>();
    ListModelList<Lineas> objlstLineas = new ListModelList<Lineas>();
    ListModelList<TtabGen> objlstSublineas = new ListModelList<TtabGen>();
    ListModelList<TtabGen> objlstMarcas = new ListModelList<TtabGen>();
    ListModelList<ExistSunat> objlstExistSunat = new ListModelList<ExistSunat>();
    ListModelList<MedSunat> objlstMedSunat = new ListModelList<MedSunat>();
    ListModelList<Proveedores> objlstProveedores = new ListModelList<Proveedores>();
    ListModelList<TtabGen> objlstUmanejoVen = new ListModelList<TtabGen>();
    ListModelList<TtabGen> objlstUmanejoComp = new ListModelList<TtabGen>();
    ListModelList<TtabGen> objlstEmpIndivVen = new ListModelList<TtabGen>();
    ListModelList<UnidMedida> objlstUnidMedida = new ListModelList<UnidMedida>();
    ListModelList<Ubicaciones> objlstUbicaciones = new ListModelList<Ubicaciones>();
    ListModelList<TtabGen> objlstEmpIndivComp = new ListModelList<TtabGen>();
    ListModelList<Impuestos> objlstImpuestos = new ListModelList<Impuestos>();
    DaoProductos objDaoProductos = new DaoProductos();
    DaoLineas objDaoLineas = new DaoLineas();
    DaoMarcas objDaoMarcas = new DaoMarcas();
    DaoSubLineas objDaoSublineas = new DaoSubLineas();
    DaoExistSunat objDaoExistSunat = new DaoExistSunat();
    DaoMedSunat objDaoMedSunat = new DaoMedSunat();
    DaoProveedores objDaoProveedores = new DaoProveedores();
    DaoUmanejo objDaoUmanejo = new DaoUmanejo();
    DaoUbicaciones objDaoUbicaciones = new DaoUbicaciones();
    DaoUnidMedida objDaoUnidMedida = new DaoUnidMedida();
    DaoImpuesto objDaoImpuesto = new DaoImpuesto();
    DaoProvPresupuesto objDaoProvPresu = new DaoProvPresupuesto();
    ProvPresupuesto objProvPresu;
    Proveedores objProveedores;
    Productos objProducto = new Productos();
    Session objSecion;
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    Utilitarios util = new Utilitarios();
    BufferedImage bimage;
    //Variables publicas
    int i_empresa, i_sucursal;
    int valor;
    int i_sel;
    String ok;
    String s_estado = "Q", s_mensaje;
    String campo = "";
    String modoEjecucion;
    Media media;
    File archivo;
    String separador = System.getProperty("file.separator");
    String fimg_raiz = "IMG_ERP";
    String fimg_producto = "PRODUCTOS";
    String fimg_codbarra = "CODIGOBARRA";
    boolean cargaImagen = false;
    String[] valores = new String[2];
    public static boolean bandera = false;

    public static int MAX_WIDTH = 500;
    //Alto máximo
    public static int MAX_HEIGHT = 480;

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerProductos.class);

    //transacciones
    @Listen("onCreate=#tabbox_productos")
    public void llenaRegistros() throws SQLException {
        i_empresa = objUsuCredential.getCodemp();
        i_sucursal = objUsuCredential.getCodsuc();
        objlstProductos = objDaoProductos.lstProductos(1);
        objlstExistSunat = objDaoExistSunat.lstExistSunat(1);
        objlstMedSunat = objDaoMedSunat.lstMedSunat(1);
        objlstProveedores = objDaoProveedores.listaProveedores("1");
        objlstUmanejoVen = objDaoUmanejo.lstUmanejoVen();
        objlstEmpIndivVen = objDaoUmanejo.lstEmpIndivVen();
        objlstUbicaciones = objDaoUbicaciones.listaUbicaciones(1);
        objlstUnidMedida = objDaoUnidMedida.lstUnidMedida();
        objlstUmanejoComp = objDaoUmanejo.lstUmanejoComp();
        objlstEmpIndivComp = objDaoUmanejo.lstUmanejoComp();
        objlstImpuestos = objDaoImpuesto.lstImpuestos(1);
        lst_productos.setModel(objlstProductos);
        cb_proextsun.setModel(objlstExistSunat);
        cb_promedsun.setModel(objlstMedSunat);
        cb_prouniven.setModel(objlstUmanejoVen);
        cb_proemppresven.setModel(objlstEmpIndivVen);
        cb_prounimed.setModel(objlstUnidMedida);
        cb_prounicom.setModel(objlstEmpIndivComp);
        cb_proempprescom.setModel(objlstUmanejoComp);
        cb_proimp.setModel(objlstImpuestos);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
        habilitaTab(false, true);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(10101000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Productos con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Productos con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Producto");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Producto");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Producto");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Producto");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Producto");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Producto");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Productos");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Productos");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (chk_busest.isChecked()) {
            i_estado = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else {
            i_estado = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstProductos = new ListModelList<Productos>();
        //Busqueda por vacio y no seleccion
        if (cb_busqueda.getSelectedIndex() == -1 || cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
        } //Busqueda por ID
        else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
        } //Busqueda por CODIGO
        else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
        } //Busqueda por DESCRIPCION
        else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
        } //Busqueda por PROVEEDOR
        else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
        } //Busqueda por LINEA
        else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
        } //Busqueda por CODIGO ORIGEN
        else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
        } //Busqueda por PESO
        else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
        } //Busqueda por CLASIFICACION
        else if (cb_busqueda.getSelectedIndex() == 8) {
            i_seleccion = 8;
        }
        objlstProductos = objDaoProductos.busquedaProductos(i_seleccion, s_consulta, i_estado);

        //Validar la tabla sin registro
        if (objlstProductos.getSize() > 0) {
            lst_productos.setModel(objlstProductos);
        } else {
            objlstProductos = null;
            lst_productos.setModel(objlstProductos);
            Messagebox.show("No existe productos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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

    @Listen("onSelect=#lst_productos")
    public void seleccionaRegistro() throws SQLException {
        objProducto = (Productos) lst_productos.getSelectedItem().getValue();
        if (objProducto == null) {
            objProducto = objlstProductos.get(i_sel + 1);
        }
        i_sel = lst_productos.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objProducto.getPro_key());
        habilitaTab(false, false);
        limpiarCampos();
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objProducto = new Productos();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_proest.setDisabled(true);
        chk_proest.setChecked(true);
        s_estado = "N";
        cb_prounimed.setDisabled(false);
        //txt_prounipresven.setDisabled(false);
        txt_propeso.setDisabled(false);
        cb_protip.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_productos.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (new DaoProductos().validaMovimientos(objProducto).equals("Producto, ya tiene movimientos")) {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCamposEdicion(false);
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            } else {
                s_estado = "E";
                habilitaBotones(true, false);
                habilitaTab(true, false);
                seleccionaTab(false, true);
                habilitaCampos(false);
                txt_codmarca.setDisabled(true);
                txt_codlinea.setDisabled(true);
                txt_codsublinea.setDisabled(true);
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida;
        s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event t) throws Exception {
                    if (campo.equals("protip")) {
                        cb_protip.focus();
                    } else if (campo.equals("proclas")) {
                        cb_proclas.focus();
                    } else if (campo.equals("codlinea")) {
                        txt_codlinea.focus();
                    } else if (campo.equals("codsublinea")) {
                        txt_codsublinea.focus();
                    } else if (campo.equals("codmarca")) {
                        txt_codmarca.focus();
                    } else if (campo.equals("prodescor")) {
                        txt_prodescor.focus();
                    } else if (campo.equals("provid")) {
                        txt_provid.focus();
                    } else if (campo.equals("procodori")) {
                        txt_procodori.focus();
                    } else if (campo.equals("prounicom")) {
                        cb_prounicom.focus();
                    } else if (campo.equals("proempprescom")) {
                        cb_proempprescom.focus();
                    } else if (campo.equals("prouniprescom")) {
                        txt_prouniprescom.focus();
                    } else if (campo.equals("proconv")) {
                        cb_proconv.focus();
                    } else if (campo.equals("prounimaster")) {
                        txt_prounimaster.focus();
                    } else if (campo.equals("proalt")) {
                        txt_proalt.focus();
                    } else if (campo.equals("proanc")) {
                        txt_proanc.focus();
                    } else if (campo.equals("prolar")) {
                        txt_prolar.focus();
                    } else if (campo.equals("provpresu")) {
                        txt_provpresuid.focus();
                    } else if (campo.equals("prouniven")) {
                        cb_prouniven.focus();
                    } else if (campo.equals("prounipresven")) {
                        txt_prounipresven.focus();
                    } else if (campo.equals("proemppresven")) {
                        cb_proemppresven.focus();
                    } else if (campo.equals("profrac")) {
                        cb_profrac.focus();
                    } else if (campo.equals("prostknofact")) {
                        txt_prostknofact.focus();
                    } else if (campo.equals("proprocedencia")) {
                        cb_proprocedencia.focus();
                    } else if (campo.equals("prorot")) {
                        cb_prorot.focus();
                    } else if (campo.equals("propeso")) {
                        txt_propeso.focus();
                    } else if (campo.equals("prounimed")) {
                        cb_prounimed.focus();
                    } else if (campo.equals("proextsun")) {
                        cb_proextsun.focus();
                    } else if (campo.equals("promedsun")) {
                        cb_promedsun.focus();
                    } else if (campo.equals("conimp")) {
                        cb_conimp.focus();
                    } else if (campo.equals("proimp")) {
                        cb_proimp.focus();
                    } else if (campo.equals("proindperc")) {
                        cb_proindperc.focus();
                    } else if (campo.equals("proordkar")) {
                        sp_proordkar.focus();
                    } else if (campo.equals("prolstpre")) {
                        sp_prolstpre.focus();
                    } else if (campo.equals("procons")) {
                        sp_procons.focus();
                    } else if (campo.equals("ord")) {
                        sp_ord.focus();
                    } else if (campo.equals("protipcodbar")) {
                        cb_protipcodbar.focus();
                    }
                }
            });
        } else if (new DaoProductos().validaMovimientos(objProducto).equals("Producto, ya tiene movimientos")) {
            Messagebox.show("El Producto no puede ser modificado, ya tiene movimientos' ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstProductos = new ListModelList<Productos>();
                                objProducto = generaRegistro();

                                /*ByteArrayOutputStream os = new ByteArrayOutputStream();
                                 ImageIO.write(bimage, "jpeg", os);
                                 InputStream is = new ByteArrayInputStream(os.toByteArray());*/
                                if (s_estado.equals("N")) {
                                    valores = objDaoProductos.insertarProducto(objProducto);
                                    //---------------
                                    if (System.getProperty("os.name").startsWith("Windows")) {
                                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                                        archivo = new File("C:\\" + fimg_raiz + "\\" + fimg_producto + "\\" + valores[1] + ".JPEG");
                                    } else {
                                        // everything else
                                        archivo = new File(separador + "home" + separador + fimg_raiz + separador + fimg_producto + separador + valores[1] + ".JPEG");
                                    }
                                    //archivo = new File("C:\\PRODUCTOS\\" + valores[1] + ".JPEG");
                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                                    ImageIO.write(bimage, "jpeg", os);
                                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                                    Files.copy(archivo, is);
                                    generarCodigoBarra(objProducto.getPro_tipcodbar(), valores[1]);
                                    //Messagebox.show(valores[0] + ", Cod.Producto : " + valores[1]);
                                    Messagebox.show(valores[0] + ", Cod.Producto : " + valores[1], "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                } else {
                                    s_mensaje = objDaoProductos.actualizarProducto(objProducto);
                                    //--------------------
                                    if (System.getProperty("os.name").startsWith("Windows")) {
                                        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                                        archivo = new File("C:\\" + fimg_raiz + "\\" + fimg_producto + "\\" + objProducto.getPro_id() + ".JPEG");
                                    } else {
                                        // everything else
                                        archivo = new File(separador + "home" + separador + fimg_raiz + separador + fimg_producto + separador + valores[1] + ".JPEG");
                                    }
                                    //archivo = new File("C:\\PRODUCTOS\\" + objProducto.getPro_id() + ".JPEG");

                                    if (cargaImagen == true) {
                                        if (archivo.exists()) {
                                            String s_men = "Esta imagen de Producto ya existe. ¿Desea Reemplazarla?";
                                            Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                                    Messagebox.QUESTION, new EventListener() {
                                                @Override
                                                public void onEvent(Event event) throws Exception {
                                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                                                        ImageIO.write(bimage, "jpeg", os);
                                                        InputStream is = new ByteArrayInputStream(os.toByteArray());
                                                        Files.copy(archivo, is);
                                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                                    } else {
                                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                                    }
                                                }
                                            });
                                        } else {
                                            Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                                            ImageIO.write(bimage, "jpeg", os);
                                            InputStream is = new ByteArrayInputStream(os.toByteArray());
                                            Files.copy(archivo, is);
                                        }
                                    } else {
                                        Messagebox.show(s_mensaje,"ERP-JIMVER",Messagebox.OK,Messagebox.INFORMATION);
                                    }
                                    cargaImagen = false;
                                }
                                //validacion de guardar/nuevo
                                VerificarTransacciones();
                                tbbtn_btn_guardar.setDisabled(true);
                                tbbtn_btn_deshacer.setDisabled(true);
                                //
                                habilitaTab(false, true);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                objlstProductos = objDaoProductos.lstProductos(1);
                                lst_productos.setModel(objlstProductos);
                                objProducto = new Productos();
                            }
                        }
                    }
            );
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_productos.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un producto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = objDaoProductos.validaMovimientos(objProducto);
            valor = objDaoProductos.i_flagErrorBD;
            if (valor > 0) {
                Messagebox.show(objDaoProductos.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                s_mensaje = "Esta Seguro que desea Eliminar este Producto?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_mensaje = objDaoProductos.eliminarProducto(objProducto);
                                    objlstProductos = new ListModelList<Productos>();
                                    objlstProductos = objDaoProductos.lstProductos(1);
                                    lst_productos.setModel(objlstProductos);
                                    Messagebox.show(s_mensaje);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_productos.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstProductos == null || objlstProductos.isEmpty()) {
            Messagebox.show("No existen Productos para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            int indice = 0;
            if (lst_productos.getSelectedIndex() >= 0) {
                objProducto = (Productos) lst_productos.getSelectedItem().getValue();
                /*if (objProducto == null) {
                 objProducto = objlstProductos.get(i_sel + 1);
                 }*/
                indice = 1;
            }
            /*objProducto = (Productos) lst_productos.getSelectedItem().getValue();
             int indice = lst_productos.getSelectedIndex() >= 0 ? 1 : 0;*/

            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("producto", objProducto.getPro_id());
            objMapObjetos.put("indice", indice);
            objMapObjetos.put("lst_productos", lst_productos);
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/logistica/mantenimiento/LovImpresionProd.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    //Eventos Secundarios - Validacion
    /*@Listen("onCtrlKey=#tabbox_productos")
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
    }*/
    
    

@Listen("onCtrlKey=#w_productos")
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
        }
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstProductos = new ListModelList<Productos>();
            objlstProductos = objDaoProductos.lstProductos(1);
            lst_productos.setModel(objlstProductos);
        }
    }

    @Listen("onUpload=#tbbtn_btn_imagenpro")
    public void cargarImagen(UploadEvent event) throws Exception {
        if (txt_prodes.getText().isEmpty()) {
            Messagebox.show("Por favor verifique el campo 'Descripción'", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            media = event.getMedia();
            if (media instanceof org.zkoss.image.Image) {
                //Redimnesionar a 400x400
                img_foto.getChildren().clear();
                //img_foto.setWidth("255px");
                //img_foto.setWidth("970px");
                //img_foto.setHeight("400px");
                //img_foto.setHeight("490px");
                Image foto_usuario = new Image();

                bimage = copyImage(media);
                //bimage = loadImage(media);

                //ImageIO.write(bimage,"jpeg",new File("saved.png"));
                //Image imagen7 =  ImageIO.read(new File("saved.png"));
                //java.awt.Image imagen7 = Toolkit.getDefaultToolkit().createImage(bimage.getSource());
                //Toolkit.getDefaultToolkit().createImage(bimage.getSource());
                foto_usuario.setContent(bimage);

                //foto_usuario.setContent((org.zkoss.image.AImage) media);                
                foto_usuario.setParent(img_foto);
                foto_usuario.setStyle("border: 1px solid;resize:none;overflow:hidden;");
                txt_proimg.setValue(media.getName().toUpperCase());
                cargaImagen = true;
            }
        }
    }

    public BufferedImage copyImage(Media media/*, String copyPath*/) throws IOException {
        //bimage = loadImage(media);
        bimage = ImageIO.read(media.getStreamData());
        /*if (bimage.getHeight() > bimage.getWidth()) {
         int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
         bimage = resize(bimage, MAX_WIDTH, heigt);
         int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
         bimage = resize(bimage, width, MAX_HEIGHT);
            
         bimage = resize(bimage, 550, 400);
         } else {
         int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
         bimage = resize(bimage, width, MAX_HEIGHT);
         int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
         bimage = resize(bimage, MAX_WIDTH, heigt);
            
         bimage = resize(bimage, 400, 550);
         }*/
        bimage = resize(bimage, 480, 480);
        return bimage;
        //saveImag(bimage, copyPath);
    }

    //public static BufferedImage loadImage(Media media) {
    //BufferedImage bimage = null;
    //try {
    //bimage = ImageIO.read(new File(pathName));
    //      bimage = ImageIO.read(media.getStreamData());
        /*} catch (Exception e) {
     e.printStackTrace();
     }*/
    //return bimage;
    //}
    public static BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bufim = new BufferedImage(newW, newH, bufferedImage.getType());
        Graphics2D g = bufim.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return bufim;
    }

    @Listen("onCheck=#chk_elegir")
    public void onChecked_elegir() {
        if (chk_elegir.isChecked()) {
            txt_prodes.setDisabled(true);
            txt_prodes.setValue(txt_prodescom.getValue());
        } else {
            txt_prodes.setDisabled(false);
            txt_prodes.setValue("");
        }
    }

    @Listen("onOK=#txt_codlinea")
    public void onEnter_txtCodLinea() {
        if (bandera == false) {
            bandera = true;
            if (txt_codlinea.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "ManPro";
                objMapObjetos.put("txt_linid", txt_codlinea);
                objMapObjetos.put("txt_lindes", txt_deslinea);
                objMapObjetos.put("tipo", "1");
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProductos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovLineas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_codsublinea.focus();
            }
        }
    }

    @Listen("onBlur=#txt_codlinea")
    public void onBlur_txtCodLinea() throws SQLException {
        if (!txt_codlinea.getValue().isEmpty()) {
            if (!txt_codlinea.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codlinea.setValue("");
                        txt_deslinea.setValue("");
                        txt_codsublinea.setValue("");
                        txt_dessublinea.setValue("");
                        txt_codmarca.setValue("");
                        txt_desmarca.setValue("");
                        txt_codlinea.focus();
                    }
                });
            } else {
                int linea = Integer.parseInt(txt_codlinea.getValue());
                Lineas objLineas = objDaoLineas.busquedaLineaxCodigo(linea);
                if (objLineas == null) {
                    Messagebox.show("El código de línea no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codlinea.setValue("");
                            txt_deslinea.setValue("");
                            txt_codsublinea.setValue("");
                            txt_dessublinea.setValue("");
                            txt_codmarca.setValue("");
                            txt_desmarca.setValue("");
                            txt_codlinea.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea " + objLineas.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_codlinea.setValue(objLineas.getTab_subdir());
                    txt_deslinea.setValue(objLineas.getTab_subdes());
                    generaSugerido();
                }
            }
        } else {
            txt_deslinea.setValue("");
            txt_codsublinea.setValue("");
            txt_dessublinea.setValue("");
        }
        txt_codsublinea.setValue("");
        txt_dessublinea.setValue("");
        bandera = false;
    }

    @Listen("onOK=#txt_codsublinea")
    public void onEnter_txt_Codsublinea() {
        if (bandera == false) {
            bandera = true;
            if (txt_codlinea.getValue().isEmpty()) {
                Messagebox.show("Debe ingresar el código de línea", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codsublinea.setValue("");
                        txt_codlinea.focus();
                    }
                });
            } else {
                if (txt_codsublinea.getValue().isEmpty() && !txt_codlinea.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    modoEjecucion = "ManPro";
                    objMapObjetos.put("txt_sublinid", txt_codsublinea);
                    objMapObjetos.put("txt_sublindes", txt_dessublinea);
                    objMapObjetos.put("txt_linid", txt_codlinea);
                    objMapObjetos.put("tipo", "1");
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerProductos");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovSublineas.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_codmarca.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_codsublinea")
    public void onBlur_txt_Codsublinea() throws SQLException {
        if (!txt_codsublinea.getValue().isEmpty() && !txt_codlinea.getValue().isEmpty()) {
            if (!txt_codsublinea.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codsublinea.setValue("");
                        txt_dessublinea.setValue("");
                        txt_codmarca.setValue("");
                        txt_codsublinea.focus();
                    }
                });
            } else {
                String sublin_id = txt_codsublinea.getValue();
                int lin_id = Integer.parseInt(txt_codlinea.getValue());
                SubLineas objSublinea = new DaoSubLineas().buscarSublineaxCodigo("" + lin_id, sublin_id);
                if (objSublinea == null) {
                    Messagebox.show("El código de sublínea no pertenece a la línea ingresada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codsublinea.setValue("");
                            txt_dessublinea.setValue("");
                            txt_codmarca.setValue("");
                            txt_codsublinea.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la SubLinea " + objSublinea.getSlin_cod() + " y ha encontrado 1 registro(s)");
                    txt_codsublinea.setValue(objSublinea.getSlin_cod());
                    txt_dessublinea.setValue(objSublinea.getSlin_des());
                    generaSugerido();
                }
            }
        } else {
            txt_dessublinea.setValue("");
            txt_codmarca.setValue("");
            txt_desmarca.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_codmarca")
    public void onEnter_txtCodmarca() {
        if (bandera == false) {
            bandera = true;
            if (txt_codsublinea.getValue().isEmpty()) {
                Messagebox.show("Debe ingresar el código de sublínea", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codmarca.setValue("");
                        txt_codsublinea.focus();
                    }
                });
            } else {
                if (txt_codmarca.getValue().isEmpty() && !txt_codsublinea.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    modoEjecucion = "ManPro";
                    objMapObjetos.put("txt_codmarca", txt_codmarca);
                    objMapObjetos.put("txt_desmarca", txt_desmarca);
                    objMapObjetos.put("tipo", "1");
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerProductos");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMarcas.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    txt_prodescor.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_codmarca")
    public void onBlur_txtCodmarca() throws SQLException {
        if (!txt_codmarca.getValue().isEmpty() && !txt_codsublinea.getValue().isEmpty()) {
            if (!txt_codmarca.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_codmarca.setValue("");
                        txt_desmarca.setValue("");
                        txt_codmarca.focus();
                    }
                });
            } else {
                int marca = Integer.parseInt(txt_codmarca.getValue());
                TtabGen objMarca = objDaoMarcas.busquedaMarcaxCodigo(marca);
                if (objMarca == null) {
                    Messagebox.show("El código de marca no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codmarca.setValue("");
                            txt_desmarca.setValue("");
                            txt_codmarca.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Linea " + objMarca.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_codmarca.setValue(objMarca.getTab_subdir());
                    txt_desmarca.setValue(objMarca.getTab_subdes());
                    generaSugerido();
                }
            }
        } else {
            txt_desmarca.setValue("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_provid")
    public void onEnter_txtProvid() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_provid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "ManPro";
                objMapObjetos.put("txt_provid", txt_provid);
                objMapObjetos.put("txt_provdes", txt_provdes);
                objMapObjetos.put("tipo", "1");
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProductos");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProveedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_procodori.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provid")
    public void onBlur_txtProvid() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (!txt_provid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_provid.setValue("");
                        txt_provdes.setValue("");
                        txt_provpresudes.setValue("");
                        txt_provpresuid.setValue("");
                        txt_provid.focus();
                    }
                });
            } else {
                txt_provid.setValue(Utilitarios.lpad(txt_provid.getValue(), 8, "0"));
                Long proveedor = Long.parseLong(txt_provid.getText());
                objProveedores = new DaoProveedores().BusquedaProveedor(proveedor);
                if (objProveedores == null) {
                    Messagebox.show("El codigo de proveedor no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_provid.setValue("");
                            txt_provdes.setValue("");
                            txt_provpresudes.setValue("");
                            txt_provpresuid.setValue("");
                            txt_provid.focus();
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedores.getProv_id() + " y ha encontrado 1 registro(s)");
                    txt_provid.setValue(objProveedores.getProv_id());
                    txt_provdes.setValue(objProveedores.getProv_razsoc());
                    if (!txt_provpresuid.getValue().isEmpty() && txt_provpresuid.getValue().matches("[0-9]*")) {
                        txt_provpresuid.setValue(Utilitarios.lpad(txt_provpresuid.getValue(), 5, "0"));
                        int provpresu = Integer.parseInt(txt_provpresuid.getValue());
                        objProvPresu = objDaoProvPresu.ProvPresxProveedor(txt_provid.getValue(), provpresu);
                        if (objProvPresu == null) {
                            txt_provpresudes.setValue("");
                            txt_provpresuid.setValue("");
                        }
                    }
                }
            }
        } else {
            txt_provdes.setValue("");
            txt_provpresuid.setValue("");
            txt_provpresudes.setValue("");
        }
        bandera = false;
    }

    @Listen("onBlur=#txt_prounipresven")
    public void onBlurProUniPreVen() {
        generaSugerido();
    }

    @Listen("onBlur=#txt_propeso")
    public void onBlurProPeso() {
        generaSugerido();
    }

    @Listen("onBlur=#cb_prounimed")
    public void onBlurProUniMed() {
        generaSugerido();
    }

    @Listen("onOK=#cb_protip")
    public void onEnterProBien() {
        if (cb_protip.getSelectedIndex() == -1) {
            cb_protip.focus();
        } else {
            cb_proclas.focus();
        }
    }

    @Listen("onOK=#cb_proclas")
    public void onEnterProCla() {
        if (cb_proclas.getSelectedIndex() == -1) {
            cb_proclas.focus();
        } else {
            txt_codlinea.focus();
        }
    }

    @Listen("onOK=#txt_prodescor")
    public void onEnterProCam() {
        if (txt_prodescor.getText().equals("")) {
            txt_prodescor.focus();
        } else {
            txt_provid.focus();
        }
    }

    @Listen("onBlur=#txt_prodescor")
    public void onBlurProDesCor() {
        generaSugerido();
    }

    @Listen("onOK=#txt_procodori")
    public void onEnterUniManejoCom() {
        if (txt_procodori.getText().equals("")) {
            txt_procodori.focus();
        } else {
            cb_prounicom.focus();
        }
    }

    @Listen("onOK=#cb_prounicom")
    public void onEnterEmpIndividualCom() {
        if (cb_prounicom.getSelectedIndex() == -1) {
            cb_prounicom.focus();
        } else {
            cb_proempprescom.focus();
        }
    }

    @Listen("onOK=#cb_proempprescom")
    public void onEnterUniPresentacionCom() {
        if (cb_proempprescom.getSelectedIndex() == -1) {
            cb_proempprescom.focus();
        } else {
            txt_prouniprescom.focus();
        }
    }

    @Listen("onOK=#txt_prouniprescom")
    public void onEnterSigno() {
        if (txt_prouniprescom.getText().equals("")) {
            txt_prouniprescom.focus();
        } else {
            cb_proconv.focus();
        }
    }

    @Listen("onBlur=#txt_prouniprescom")
    public void validaUniPreCom() {
        if (txt_prouniprescom.getValue() != null) {
            if (txt_prouniprescom.getValue() <= 0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prouniprescom.setValue(null);
                        txt_prouniprescom.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#cb_proconv")
    public void onEnterConversion() {
        if (cb_proconv.getSelectedIndex() == -1) {
            cb_proconv.focus();
        } else {
            txt_prounimaster.focus();
        }
    }

    @Listen("onOK=#txt_prounimaster")
    public void onEnterAltura() {
        if (txt_prounimaster.getText().equals("")) {
            txt_prounimaster.focus();
        } else {
            cb_prouniven.focus();
        }
    }

    @Listen("onBlur=#txt_prounimaster")
    public void validaConversion() {
        if (txt_prounimaster.getValue() != null) {
            if (txt_prounimaster.getValue() <= 0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prounimaster.setValue(null);
                        txt_prounimaster.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#txt_proalt")
    public void onEnterAlto() {
        txt_proanc.focus();
    }

    @Listen("onOK=#txt_proanc")
    public void onEnterAncho() {
        txt_prolar.focus();
    }

    @Listen("onOK=#txt_prolar")
    public void onEnterLargo() {
        cb_proextsun.focus();
    }

    @Listen("onBlur=#txt_prolar")
    public void onBlurLargo() {
        if (txt_prolar.getValue() != null) {
            if (txt_prolar.doubleValue() <= 0.0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prolar.setValue(null);
                        txt_prolar.focus();
                    }
                });
            } else {
                if (txt_proalt.getValue() == null) {
                    txt_proalt.focus();
                } else if (txt_proanc.getValue() == null) {
                    txt_proanc.focus();
                } else if (txt_prolar.getValue() == null) {
                    txt_prolar.focus();
                } else {
                    txt_provol.setValue(calculoVolumen(txt_proalt.getValue(), txt_proanc.getValue(), txt_prolar.getValue()));
                    cb_proextsun.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_proanc")
    public void onBlurAncho() {
        if (txt_proanc.getValue() != null) {
            if (txt_proanc.doubleValue() <= 0.0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_proanc.setValue(null);
                        txt_proanc.focus();
                    }
                });
            } else {
                if (txt_proalt.getValue() == null) {
                    txt_proalt.focus();
                } else if (txt_proanc.getValue() == null) {
                    txt_proanc.focus();
                } else if (txt_prolar.getValue() == null) {
                    txt_prolar.focus();
                } else {
                    txt_provol.setValue(calculoVolumen(txt_proalt.getValue(), txt_proanc.getValue(), txt_prolar.getValue()));
                    cb_proextsun.focus();
                }
            }
        }
    }

    @Listen("onBlur=#txt_proalt")
    public void onBlurAlto() {
        if (txt_proalt.getValue() != null) {
            if (txt_proalt.doubleValue() <= 0.0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_proalt.setValue(null);
                        txt_proalt.focus();
                    }
                });
            } else {
                if (txt_proalt.getValue() == null) {
                    txt_proalt.focus();
                } else if (txt_proanc.getValue() == null) {
                    txt_proanc.focus();
                } else if (txt_prolar.getValue() == null) {
                    txt_prolar.focus();
                } else {
                    txt_provol.setValue(calculoVolumen(txt_proalt.getValue(), txt_proanc.getValue(), txt_prolar.getValue()));
                    cb_proextsun.focus();
                }
            }
        }
    }

    @Listen("onOK=#txt_provpresuid")
    public void onEnter_txtProvPresu() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (!txt_provid.getValue().isEmpty()) {
                if (txt_provpresuid.getValue().isEmpty()) {
                    Map objMapObjetos = new HashMap();
                    modoEjecucion = "ManPro";
                    objMapObjetos.put("txt_provpresuid", txt_provpresuid);
                    objMapObjetos.put("txt_provpresudes", txt_provpresudes);
                    objMapObjetos.put("txt_provid", txt_provid);
                    objMapObjetos.put("validaBusqueda", modoEjecucion);
                    objMapObjetos.put("controlador", "ControllerProductos");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovProvPresupuesto.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    if ((s_estado.equals("E") || s_estado.equals("N")) && !new DaoProductos().validaMovimientos(objProducto).equals("Producto, ya tiene movimientos")) {
                        cb_proextsun.focus();
                    }
                }
            } else {
                ok = "s";
                txt_provid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_provpresuid")
    public void onBlur_txtProvPresu() throws SQLException {
        if (!txt_provid.getValue().isEmpty()) {
            if (txt_provid.getValue().matches("[0-9]*")) {
                if (!txt_provpresuid.getValue().isEmpty()) {
                    if (!txt_provpresuid.getValue().matches("[0-9]*")) {
                        Messagebox.show("Por favor ingrese valores numericos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                            public void onEvent(Event t) throws Exception {
                                txt_provpresuid.setValue("");
                                txt_provpresudes.setValue("");
                                txt_provpresuid.focus();
                            }
                        });
                    } else {
                        txt_provpresuid.setValue(Utilitarios.lpad(txt_provpresuid.getValue(), 5, "0"));
                        int provpresu = Integer.parseInt(txt_provpresuid.getValue());
                        objProvPresu = objDaoProvPresu.ProvPresxProveedor(txt_provid.getValue(), provpresu);
                        if (objProvPresu == null) {
                            Messagebox.show("El codigo de proveedor con presupuesto no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                                public void onEvent(Event t) throws Exception {
                                    txt_provpresuid.setValue("");
                                    txt_provpresudes.setValue("");
                                    txt_provpresuid.focus();
                                }
                            });
                        } else {
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Proveedor " + objProveedores.getProv_id() + " y ha encontrado 1 registro(s)");
                            txt_provpresuid.setValue(Utilitarios.lpad(objProvPresu.getProvpresu_id(), 5, "0"));
                            txt_provpresudes.setValue(objProvPresu.getProvpresu_desabre());
                        }
                    }
                } else {
                    txt_provpresudes.setValue("");
                }
            }
        } else {
            if (ok.equals("s") || !txt_provpresuid.getValue().isEmpty() && txt_provid.getValue().isEmpty()) {
                Messagebox.show("Debe ingresar Proveedor", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_provpresuid.setValue("");
                        txt_provpresudes.setValue("");
                        txt_provid.focus();
                        ok = "";
                    }
                });
            }
        }
        bandera = false;
    }

    @Listen("onOK=#cb_prouniven")
    public void onEnterUniPreseVen() {
        if (cb_prouniven.getSelectedIndex() == -1) {
            cb_prouniven.focus();
        } else {
            txt_prounipresven.focus();
        }
    }

    @Listen("onOK=#txt_prounipresven")
    public void onEnterEmpIndiVen() {
        if (txt_prounipresven.getText().equals("")) {
            txt_prounipresven.focus();
        } else {
            cb_proemppresven.focus();
        }
    }

    @Listen("onBlur=#txt_prounipresven")
    public void validaUniPreVen() {
        if (txt_prounipresven.getValue() != null) {
            if (txt_prounipresven.getValue() <= 0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prounipresven.setValue(null);
                        txt_prounipresven.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#cb_proemppresven")
    public void onEnterUnidadBodeguero() {
        if (cb_proemppresven.getSelectedIndex() == -1) {
            cb_proemppresven.focus();
        } else {
            txt_prounibodegero.focus();
        }
    }

    @Listen("onOK=#txt_prounibodegero")
    public void onEnterVenxFraccion() {
        cb_profrac.focus();
    }

    @Listen("onOK=#cb_profrac")
    public void onEnterStockNFact() {
        if (cb_profrac.getSelectedIndex() == -1) {
            cb_profrac.focus();
        } else {
            txt_prostknofact.focus();
        }
    }

    @Listen("onOK=#txt_prostknofact")
    public void onEnterProcedencia() {
        if (txt_prostknofact.getText().equals("")) {
            txt_prostknofact.focus();
        } else {
            cb_proprocedencia.focus();
        }
    }

    @Listen("onBlur=#txt_prostknofact")
    public void validaStknofact() {
        if (txt_prostknofact.getValue() != null) {
            if (txt_prostknofact.getValue() <= 0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_prostknofact.setValue(null);
                        txt_prostknofact.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#cb_proprocedencia")
    public void onEnterRotacion() {
        if (cb_proprocedencia.getSelectedIndex() == -1) {
            cb_proprocedencia.focus();
        } else {
            cb_prorot.focus();
        }
    }

    @Listen("onOK=#cb_prorot")
    public void onEnterPeso() {
        if (cb_prorot.getSelectedIndex() == -1) {
            cb_prorot.focus();
        } else {
            txt_propeso.focus();
        }
    }

    @Listen("onOK=#txt_propeso")
    public void onEnterUnidMediVen() {
        if (txt_propeso.getText().equals("")) {
            txt_propeso.focus();
        } else {
            cb_prounimed.focus();
        }
    }

    @Listen("onBlur=#txt_propeso")
    public void validaPeso() {
        if (txt_propeso.getValue() != null) {
            if (txt_propeso.getValue() <= 0) {
                Messagebox.show("Por favor ingrese valores mayores a 0", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event t) throws Exception {
                        txt_propeso.setValue(null);
                        txt_propeso.focus();
                    }
                });
            }
        }
    }

    @Listen("onOK=#cb_prounimed")
    public void onEnterTipoExiSunat() {
        if (cb_prounimed.getSelectedIndex() == -1) {
            cb_prounimed.focus();
        } else {
            txt_proalt.focus();
        }
    }

    @Listen("onOK=#cb_proextsun")
    public void onEnterUniMediSunat() {
        if (cb_proextsun.getSelectedIndex() == -1) {
            cb_proextsun.focus();
        } else {
            cb_promedsun.focus();
        }
    }

    @Listen("onOK=#cb_promedsun")
    public void onEnterImpuestoSuntat() {
        if (cb_promedsun.getSelectedIndex() == -1) {
            cb_promedsun.focus();
        } else {
            cb_conimp.focus();
        }
    }

    @Listen("onOK=#cb_conimp")
    public void onEnterTipoImpuestoSuntat() {
        if (cb_conimp.getSelectedIndex() == -1) {
            cb_conimp.focus();
        } else {
            cb_proimp.focus();
        }
    }

    @Listen("onOK=#cb_proimp")
    public void onEnterAPercepcionSunat() {
        if (cb_proimp.getSelectedIndex() == -1) {
            cb_proimp.focus();
        } else {
            cb_proindperc.focus();
        }
    }

    @Listen("onOK=#cb_proindperc")
    public void onEnterNKardex() {
        if (cb_proindperc.getSelectedIndex() == -1) {
            cb_proindperc.focus();
        } else {
            sp_proordkar.focus();
        }
    }

    @Listen("onOK=#sp_proordkar")
    public void onEnterListaPrecio() {
        if (sp_proordkar.getText().equals("")) {
            sp_proordkar.focus();
        } else {
            sp_prolstpre.focus();
        }
    }

    @Listen("onOK=#sp_prolstpre")
    public void onEnterNConsolidado() {
        if (sp_prolstpre.getText().equals("")) {
            sp_prolstpre.focus();
        } else {
            sp_procons.focus();
        }
    }

    @Listen("onOK=#sp_procons")
    public void onEnterOrden() {
        if (sp_procons.getText().equals("")) {
            sp_procons.focus();
        } else {
            sp_ord.focus();
        }
    }

    @Listen("onOK=#sp_ord")
    public void onEnterTipCodBarras() {
        if (sp_ord.getText().equals("")) {
            sp_ord.focus();
        } else {
            cb_protipcodbar.focus();
        }
    }

    @Listen("onOK=#cb_protipcodbar")
    public void onEnterDescriCompu() {
        if (cb_protipcodbar.getSelectedIndex() == -1) {
            cb_protipcodbar.focus();
        } else {
            tab_otros.setSelected(true);
            tbbtn_btn_imagenpro.setFocus(true);
        }
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_proid.setValue(objProducto.getPro_id());
        txt_prostknofact.setValue(objProducto.getPro_scknofact());
        //txt_prodescom.setValue(objProducto.getPro_des());

        txt_prodescom.setValue(objProducto.getPro_sublinea()
                + " " + objProducto.getPro_desmar()
                + " " + objProducto.getPro_descor()
                + "*" + objProducto.getPro_presminven()
                + "*" + objProducto.getPro_peso()
                + "" + objProducto.getPro_unipeso());

        txt_prodes.setValue(objProducto.getPro_des());
        txt_prodescor.setValue(objProducto.getPro_descor());
        txt_procodori.setValue(objProducto.getPro_codori());
        cb_proconv.setValue(objProducto.getPro_conv());
        txt_prounimaster.setValue(objProducto.getPro_unimas());
        txt_prounipresven.setValue(objProducto.getPro_presminven());
        cb_prorot.setSelectedItem(Utilitarios.valorPorTexto(cb_prorot, objProducto.getPro_rot()));
        txt_prouniprescom.setValue(objProducto.getPro_presmincom());
        txt_proimg.setValue(objProducto.getPro_ingprod());
        txt_codlinea.setValue(objProducto.getPro_lin());
        txt_deslinea.setValue(objProducto.getPro_linea());
        txt_codsublinea.setValue(objProducto.getPro_sublin());
        txt_dessublinea.setValue(objProducto.getPro_sublinea());
        txt_codmarca.setValue(objProducto.getPro_mar());
        txt_desmarca.setValue(objProducto.getPro_desmar());
        cb_proclas.setSelectedItem(Utilitarios.textoPorTexto(cb_proclas, objProducto.getPro_clas()));
        cb_protip.setSelectedItem(Utilitarios.textoPorTexto(cb_protip, objProducto.getPro_tip()));
        cb_proextsun.setSelectedItem(Utilitarios.valorPorTexto(cb_proextsun, objProducto.getPro_tipexisun()));
        cb_promedsun.setSelectedItem(Utilitarios.textoPorTexto(cb_promedsun, objProducto.getPro_medunisun()));
        txt_provid.setValue(objProducto.getPro_provid());
        try {
            objProveedores = new DaoProveedores().BusquedaProveedor(objProducto.getPro_provid(), "1");
            txt_provdes.setValue(objProveedores.getProv_razsoc());
        } catch (SQLException e) {
        }
        cb_prouniven.setSelectedItem(Utilitarios.textoPorTexto(cb_prouniven, objProducto.getPro_unimanven()));
        txt_prounibodegero.setValue(objProducto.getPro_unibodeguero());
        cb_proemppresven.setSelectedItem(Utilitarios.textoPorTexto(cb_proemppresven, objProducto.getPro_empindven()));
        cb_profrac.setSelectedItem(Utilitarios.textoPorTexto(cb_profrac, objProducto.getPro_frac()));
        cb_proprocedencia.setSelectedItem(Utilitarios.textoPorTexto(cb_proprocedencia, objProducto.getPro_proc()));
        cb_conimp.setSelectedItem(Utilitarios.textoPorTexto(cb_conimp, objProducto.getPro_condimp()));
        cb_proimp.setSelectedItem(Utilitarios.valorPorTexto(cb_proimp, objProducto.getImp_id()));
        //cb_proubi.setSelectedItem(util.valorPorTexto(cb_proubi, objProducto.getPro_ubi()));
        cb_prounimed.setSelectedItem(Utilitarios.textoPorTexto(cb_prounimed, objProducto.getPro_unipeso()));
        cb_prounicom.setSelectedItem(Utilitarios.textoPorTexto(cb_prounicom, objProducto.getPro_unimancom()));
        cb_proempprescom.setSelectedItem(Utilitarios.textoPorTexto(cb_proempprescom, objProducto.getPro_empindcom()));
        cb_protipcodbar.setSelectedItem(Utilitarios.textoPorTexto(cb_protipcodbar, objProducto.getPro_tipcodbar()));
        cb_proindperc.setSelectedItem(Utilitarios.textoPorTexto(cb_proindperc, String.valueOf(objProducto.getPro_indperc())));
        sp_proordkar.setValue(objProducto.getPro_ordkar());
        sp_prolstpre.setValue(objProducto.getPro_ordlstprec());
        sp_procons.setValue(objProducto.getPro_ordcons());
        sp_ord.setValue(objProducto.getPro_ord());
        txt_proalt.setValue(objProducto.getPro_alt());
        txt_proanc.setValue(objProducto.getPro_anc());
        txt_prolar.setValue(objProducto.getPro_lar());
        txt_propeso.setValue(objProducto.getPro_peso());
        txt_provol.setValue(objProducto.getPro_vol());
        txt_proimg.setValue(objProducto.getPro_img());
        txt_usuadd.setValue(objProducto.getPro_usuadd());
        d_fecadd.setValue(objProducto.getPro_fecadd());
        txt_usumod.setValue(objProducto.getPro_usumod());
        txt_provpresuid.setValue(objProducto.getPro_provpresuid());
        txt_provpresudes.setValue(objProducto.getPro_provpresudes());
        d_fecmod.setValue(objProducto.getPro_fecmod());
        if (objProducto.getPro_est() == 2) {
            chk_proest.setChecked(false);
        } else {
            chk_proest.setChecked(true);
        }
        if (objProducto.getPro_afecam() == 0) {
            chk_proafecam.setChecked(false);
        } else {
            chk_proafecam.setChecked(true);
        }
        Image foto_producto = new Image();
        Image foto_usuario = new Image();
        try {

            if (System.getProperty("os.name").startsWith("Windows")) {
                // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                //file = new File("C:\\CODIGO BARRA\\" + pro_key + ".JPEG");
                foto_producto.setContent(new AImage("C:\\" + fimg_raiz + "\\" + fimg_producto + "\\" + objProducto.getPro_id() + ".JPEG"));
                foto_usuario.setContent(new AImage("C:\\" + fimg_raiz + "\\" + fimg_codbarra + "\\" + objProducto.getPro_id() + ".PNG"));
            } else {
                // everything else
                //file = new File(separador + "home" + separador + pro_key + ".pdf");
                foto_producto.setContent(new AImage(separador + "home" + separador + fimg_raiz + separador + fimg_producto + separador + objProducto.getPro_id() + ".JPEG"));
                foto_usuario.setContent(new AImage(separador + "home" + separador + fimg_raiz + separador + fimg_codbarra + separador + objProducto.getPro_id() + ".PNG"));
            }

            //foto_producto.setContent(new AImage("C:\\PRODUCTOS\\" + objProducto.getPro_id() + ".JPEG"));
            //foto_usuario.setContent(new AImage("C:\\CODIGO BARRA\\" + objProducto.getPro_id() + ".PNG"));
            /*img_foto.setWidth("294px");
             img_foto.setHeight("287px");*/
            //img_foto.setWidth("255px");
            img_foto.setWidth("970px");
            //img_foto.setHeight("400px");
            img_foto.setHeight("490px");
            /*img_codigo.setWidth("191px");
             img_codigo.setHeight("75px");*/
            img_codigo.setWidth("235px");
            img_codigo.setHeight("80px");
            foto_producto.setParent(img_foto);
            foto_usuario.setParent(img_codigo);
            //border: 2px solid;
            foto_producto.setStyle("text-align: center;resize:none;overflow:hidden;");
            foto_usuario.setStyle("text-align: center;");
        } catch (IOException ex) {
            Messagebox.show("Falta datos en el campo " + ex, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
        tab_otros.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
    }

    public void limpiarCampos() {
        txt_proid.setValue(null);
        txt_prostknofact.setValue(null);
        txt_prodes.setValue(null);
        txt_prodescom.setValue(null);
        txt_prodescor.setValue(null);
        txt_procodori.setValue(null);
        cb_proconv.setSelectedIndex(-1);
        txt_prounimaster.setValue(null);
        txt_prounipresven.setValue(null);
        txt_prounibodegero.setValue(null);
        cb_prorot.setSelectedIndex(-1);
        txt_prouniprescom.setValue(null);
        txt_proimg.setValue(null);
        txt_codlinea.setValue(null);
        txt_deslinea.setValue(null);
        txt_codsublinea.setValue(null);
        txt_dessublinea.setValue(null);
        txt_codmarca.setValue("");
        txt_desmarca.setValue("");
        cb_proclas.setSelectedIndex(-1);
        cb_protip.setSelectedIndex(-1);
        cb_proextsun.setSelectedIndex(-1);
        cb_promedsun.setSelectedIndex(-1);
        txt_provid.setValue(null);
        txt_provdes.setValue(null);
        cb_prouniven.setSelectedIndex(-1);
        cb_proemppresven.setSelectedIndex(-1);
        cb_profrac.setSelectedIndex(-1);
        cb_proprocedencia.setSelectedIndex(-1);
        cb_proindperc.setSelectedIndex(-1);
        cb_conimp.setSelectedIndex(-1);
        cb_proimp.setSelectedIndex(-1);
        cb_prounimed.setSelectedIndex(-1);
        cb_prounicom.setSelectedIndex(-1);
        cb_proempprescom.setSelectedIndex(-1);
        cb_protipcodbar.setSelectedIndex(-1);
        sp_proordkar.setValue(null);
        sp_prolstpre.setValue(null);
        sp_procons.setValue(null);
        sp_ord.setValue(null);
        txt_proalt.setValue(null);
        txt_proanc.setValue(null);
        txt_prolar.setValue(null);
        txt_propeso.setValue(null);
        txt_provol.setValue(null);
        sp_proordkar.setValue(0);
        sp_prolstpre.setValue(0);
        sp_procons.setValue(0);
        sp_ord.setValue(0);
        img_foto.getChildren().clear();
        img_codigo.getChildren().clear();
        txt_provpresuid.setValue("");
        txt_provpresudes.setValue("");
        txt_procodean13prov.setValue("");
        txt_procodean14prov.setValue("");
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaCampos(boolean b_valida1) {
        chk_proest.setDisabled(b_valida1);
        chk_elegir.setDisabled(b_valida1);
        chk_proafecam.setDisabled(b_valida1);
        cb_proindperc.setDisabled(b_valida1);
        txt_prostknofact.setDisabled(b_valida1);
        txt_prodescor.setDisabled(b_valida1);
        txt_procodori.setDisabled(b_valida1);
        cb_proconv.setDisabled(b_valida1);
        txt_prounimaster.setDisabled(b_valida1);
        txt_prounipresven.setDisabled(b_valida1);
        txt_prounibodegero.setDisabled(b_valida1);
        cb_prorot.setDisabled(b_valida1);
        txt_prouniprescom.setDisabled(b_valida1);
        txt_proimg.setDisabled(b_valida1);
        txt_codlinea.setDisabled(b_valida1);
        txt_codsublinea.setDisabled(b_valida1);
        txt_codmarca.setDisabled(b_valida1);
        cb_proclas.setDisabled(b_valida1);
        cb_protip.setDisabled(b_valida1);
        cb_proextsun.setDisabled(b_valida1);
        cb_promedsun.setDisabled(b_valida1);
        txt_provid.setDisabled(b_valida1);
        cb_prouniven.setDisabled(b_valida1);
        cb_proemppresven.setDisabled(b_valida1);
        cb_profrac.setDisabled(b_valida1);
        cb_proprocedencia.setDisabled(b_valida1);
        cb_conimp.setDisabled(b_valida1);
        cb_proimp.setDisabled(b_valida1);
        cb_prounimed.setDisabled(b_valida1);
        txt_propeso.setDisabled(b_valida1);
        cb_prounicom.setDisabled(b_valida1);
        cb_proempprescom.setDisabled(b_valida1);
        cb_protipcodbar.setDisabled(b_valida1);
        sp_proordkar.setDisabled(b_valida1);
        sp_prolstpre.setDisabled(b_valida1);
        sp_procons.setDisabled(b_valida1);
        sp_ord.setDisabled(b_valida1);
        txt_proalt.setDisabled(b_valida1);
        txt_proanc.setDisabled(b_valida1);
        txt_prolar.setDisabled(b_valida1);
        txt_provpresuid.setDisabled(b_valida1);
        tbbtn_btn_imagenpro.setDisabled(b_valida1);
    }

    public void habilitaCamposEdicion(boolean b_valida1) {
        chk_proest.setDisabled(b_valida1);
        chk_elegir.setDisabled(b_valida1);
        chk_proafecam.setDisabled(b_valida1);
        //cb_proindperc.setDisabled(b_valida1);
        //txt_prostknofact.setDisabled(b_valida1);
        txt_prodescor.setDisabled(b_valida1);
        //txt_procodori.setDisabled(b_valida1);
        //cb_proconv.setDisabled(b_valida1);
        //txt_prounimaster.setDisabled(b_valida1);
        //txt_prounipresven.setDisabled(b_valida1);
        //txt_prounibodegero.setDisabled(b_valida1);
        cb_prorot.setDisabled(b_valida1);
        //txt_prouniprescom.setDisabled(b_valida1);
        txt_proimg.setDisabled(b_valida1);
        //cb_proclas.setDisabled(b_valida1);
        //cb_protip.setDisabled(b_valida1);
        //cb_proextsun.setDisabled(b_valida1);
        //cb_promedsun.setDisabled(b_valida1);
        //txt_provid.setDisabled(b_valida1);
        //cb_prouniven.setDisabled(b_valida1);
        //cb_proemppresven.setDisabled(b_valida1);
        cb_profrac.setDisabled(b_valida1);
        //cb_proprocedencia.setDisabled(b_valida1);
        //cb_conimp.setDisabled(b_valida1);
        //cb_proimp.setDisabled(b_valida1);
        //cb_prounimed.setDisabled(b_valida1);
        //txt_propeso.setDisabled(b_valida1);
        //cb_prounicom.setDisabled(b_valida1);
        //cb_proempprescom.setDisabled(b_valida1);
        cb_protipcodbar.setDisabled(b_valida1);
        sp_proordkar.setDisabled(b_valida1);
        sp_prolstpre.setDisabled(b_valida1);
        sp_procons.setDisabled(b_valida1);
        sp_ord.setDisabled(b_valida1);
        txt_proalt.setDisabled(b_valida1);
        txt_proanc.setDisabled(b_valida1);
        txt_prolar.setDisabled(b_valida1);
        tbbtn_btn_imagenpro.setDisabled(b_valida1);
    }

    public Productos generaRegistro() throws IOException {
        int pro_key = objProducto.getPro_key();
        int pro_est = 2;
        int pro_cam = 0;
        if (chk_proest.isChecked()) {
            pro_est = 1;
        }
        if (chk_proafecam.isChecked()) {
            pro_cam = 1;
        }
        String pro_id = txt_proid.getValue();
        String pro_descor = txt_prodescor.getValue().toUpperCase();
        String pro_lin = txt_codlinea.getValue();
        String pro_sublin = txt_codsublinea.getValue();
        String pro_mar = txt_codmarca.getValue();
        String pro_clas = cb_proclas.getSelectedItem().getValue();
        String pro_tip = cb_protip.getSelectedItem().getValue();
        int pro_ordkar = sp_proordkar.getValue();
        int pro_ordlstprec = sp_prolstpre.getValue();
        int pro_ordcons = sp_procons.getValue();
        int pro_ord = sp_ord.getValue();
        int pro_tipexisun = cb_proextsun.getSelectedItem().getValue();
        int pro_indperc = Integer.parseInt(cb_proindperc.getSelectedItem().getValue().toString());
        String pro_medunisun = cb_promedsun.getSelectedItem().getValue();
        String pro_provid = txt_provid.getValue();
        String pro_codori = txt_procodori.getValue().toUpperCase();
        String pro_conv = cb_proconv.getSelectedItem().getValue();
        int pro_unimas = txt_prounimaster.getValue();
        String pro_unimanven = cb_prouniven.getSelectedItem().getValue();
        String pro_empindven = cb_proemppresven.getSelectedItem().getValue();
        int pro_presminven = txt_prounipresven.getValue();
        int pro_unibodeguero = txt_prounibodegero.getValue();
        String pro_frac = cb_profrac.getSelectedItem().getValue();
        String pro_proc = cb_proprocedencia.getSelectedItem().getValue();
        int imp_id = cb_proimp.getSelectedItem().getValue();
        String pro_condimp = cb_conimp.getSelectedItem().getValue();
        int pro_ubi = 0;
        double pro_alt = txt_proalt.getValue();
        double pro_anc = txt_proanc.getValue();
        double pro_lar = txt_prolar.getValue();
        int pro_rot = Integer.parseInt(cb_prorot.getSelectedItem().getValue().toString());
        double pro_peso = txt_propeso.getValue();
        String pro_unipeso = cb_prounimed.getSelectedItem().getValue();
        double pro_vol = calculoVolumen(pro_anc, pro_lar, pro_alt);

        String pro_unimancom = cb_prounicom.getSelectedItem().getValue();
        String pro_empindcom = cb_proempprescom.getSelectedItem().getValue();
        int pro_presmincom = txt_prouniprescom.getValue();
        int pro_scknofact = txt_prostknofact.getValue();
        String pro_codbar = "";
        String pro_tipcodbar = cb_protipcodbar.getSelectedItem().getValue();
        String pro_codean13prov = txt_procodean13prov.getValue().isEmpty() ? "" : txt_procodean13prov.getValue();
        String pro_codean14prov = txt_procodean14prov.getValue().isEmpty() ? "" : txt_procodean14prov.getValue();;
        String pro_imgcodbar = "";
        String pro_img = txt_proimg.getValue().toUpperCase();
        String pro_usuadd = objUsuCredential.getCuenta();
        String pro_usumod = objUsuCredential.getCuenta();
        String pro_desdes = (txt_dessublinea.getValue() + " " + txt_desmarca.getValue() + " " + txt_prodescor.getValue()).toUpperCase();/*txt_prodes.getValue().toUpperCase();*/

        int pro_provpresu = Integer.parseInt(txt_provpresuid.getValue());

        String pro_des = (chk_elegir.isChecked() ? txt_prodescom.getValue().toUpperCase() : txt_prodes.getValue().toUpperCase());
        return new Productos(pro_id, pro_key, pro_est, pro_des, pro_desdes, pro_descor, pro_lin, pro_sublin, pro_mar,
                pro_clas, pro_tip, pro_ordkar, pro_ordlstprec, pro_ordcons, pro_ord, pro_tipexisun,
                pro_indperc, pro_medunisun, pro_provid, pro_codori, pro_conv, pro_unimas, pro_unimanven,
                pro_empindven, pro_presminven, pro_unibodeguero, pro_frac, pro_proc, imp_id, pro_condimp, pro_ubi, pro_alt,
                pro_anc, pro_lar, pro_rot, pro_peso, pro_unipeso, pro_vol, pro_unimancom, pro_empindcom,
                pro_presmincom, pro_scknofact, pro_codbar, pro_tipcodbar, pro_imgcodbar, pro_img, pro_cam,
                pro_usuadd, pro_usumod, pro_provpresu, pro_codean13prov, pro_codean14prov);
    }

    public void generarCodigoBarra(String pro_tipcodbar, String pro_id) throws IOException {
        JBarcodeBean barcode = new JBarcodeBean();
        if (pro_tipcodbar.equalsIgnoreCase("COD39")) {
            barcode.setCodeType(new Code39());
            barcode.setCode(pro_id);
        } else if (pro_tipcodbar.equalsIgnoreCase("COD128")) {
            barcode.setCodeType(new Code128());
            barcode.setCode(pro_id);
        } else if (pro_tipcodbar.equalsIgnoreCase("COD11")) {
            barcode.setCodeType(new Code11());
            barcode.setCode(pro_id);
        } else if (pro_tipcodbar.equalsIgnoreCase("COD93")) {
            barcode.setCodeType(new Code93());
            barcode.setCode(pro_id);
        } else if (pro_tipcodbar.equalsIgnoreCase("EAN8")) {
            barcode.setCodeType(new Ean8());
            barcode.setCode(pro_id);
        } else if (pro_tipcodbar.equalsIgnoreCase("EAN13")) {
            barcode.setCodeType(new Ean13());
            barcode.setCode("0000" + pro_id);
        } /*else if (pro_tipcodbar.equalsIgnoreCase("EAN14")) {
         barcode.setCodeType(new );
         }*/ else {
            barcode.setCodeType(new Interleaved25());
        }
        
        
        barcode.setCheckDigit(true);
        BufferedImage bufferedImage = barcode.draw(new BufferedImage(178, 70, BufferedImage.TYPE_INT_RGB));
        File file;

        if (System.getProperty("os.name").startsWith("Windows")) {
            // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
            file = new File("C:\\" + fimg_raiz + "\\" + fimg_codbarra + "\\" + pro_id + ".png");
        } else {
            // everything else
            file = new File(separador + "home" + separador + fimg_raiz + separador + fimg_codbarra + separador + pro_id + ".png");
        }

        //File file = new File("C:\\CODIGO BARRA\\" + pro_key + ".png");
        ImageIO.write(bufferedImage, "png", file);
    }

    public double calculoVolumen(double ancho, double largo, double alto) {
        NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.US);
        nf_in.setMaximumFractionDigits(5);
        double volumen = Double.parseDouble(nf_in.format(alto * ancho * largo).replace(",", ""));
        return volumen;
    }

    public void generaSugerido() {
        double num, p_dec = 0;
        int p_ent = 0;
        if (txt_propeso.getValue() != null) {
            num = txt_propeso.getValue();
            p_ent = (int) num;
            p_dec = num - p_ent;

        }
        txt_prodescom.setValue(("".equals(txt_dessublinea.getValue()) ? "" : txt_dessublinea.getValue())
                + " " + ("".equals(txt_desmarca.getValue()) ? "" : txt_desmarca.getValue())
                + " " + ("".equals(txt_prodescor.getValue()) ? "" : txt_prodescor.getValue())
                + "*" + (txt_prounipresven.getValue() == null ? "" : txt_prounipresven.getValue().toString())
                + "*" + (txt_propeso.getValue() == null ? "" : String.valueOf(p_dec > 0 ? txt_propeso.getValue() : p_ent))
                + "" + (cb_prounimed.getSelectedIndex() == -1 ? "" : cb_prounimed.getSelectedItem().getValue()));

        txt_prodes.setValue(("".equals(txt_dessublinea.getValue()) ? "" : txt_dessublinea.getValue())
                + " " + ("".equals(txt_desmarca.getValue()) ? "" : txt_desmarca.getValue())
                + " " + ("".equals(txt_prodescor.getValue()) ? "" : txt_prodescor.getValue())
                + "*" + (txt_prounipresven.getValue() == null ? "" : txt_prounipresven.getValue().toString())
                + "*" + (txt_propeso.getValue() == null ? "" : String.valueOf(p_dec > 0 ? txt_propeso.getValue() : p_ent))
                + "" + (cb_prounimed.getSelectedIndex() == -1 ? "" : cb_prounimed.getSelectedItem().getValue()));
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstProductos = null;
        objlstProductos = new ListModelList<Productos>();
        lst_productos.setModel(objlstProductos);
    }

    public String verificar() {
        String s_verifica;
        //Panel Mantenimiento
        if (cb_protip.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Tipo de producto";
            campo = "protip";
        } else if (cb_proclas.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Clasificación";
            campo = "proclas";
        } else if (txt_codlinea.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Línea";
            campo = "codlinea";
        } else if (txt_codsublinea.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Sublínea";
            campo = "codsublinea";
        } else if (txt_codmarca.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Marca";
            campo = "codmarca";
        } else if (txt_prodescor.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Adjetivo";
            campo = "prodescor";
        } else if (txt_prodes.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Descripción";
        } else if (txt_proimg.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Imagen del producto";
        } else if (txt_provid.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Proveedor";
            campo = "provid";
        } else if (txt_procodori.getValue().isEmpty()) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Código de origen";
            campo = "procodori";
        } else if (cb_prounicom.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Unidad manejo - Compra";
            campo = "prounicom";
        } else if (cb_proempprescom.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Empaque individual - Compra";
            campo = "proempprescom";
        } else if (txt_prouniprescom.getValue() == null) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Unidad de presentación - Compra";
            campo = "prouniprescom";
        } else if (cb_proconv.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Signo de conversión";
            campo = "proconv";
        } else if (txt_prounimaster.getValue() == null) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Conversión";
            campo = "prounimaster";
        } else if (txt_proalt.getValue() == null) {
            s_verifica = "Altura";
            campo = "proalt";
        } else if (txt_proanc.getValue() == null) {
            s_verifica = "Ancho";
            campo = "proanc";
        } else if (txt_prolar.getValue() == null) {
            s_verifica = "Largo";
            campo = "prolar";
        } else if (txt_provpresuid.getValue().isEmpty()) {
            s_verifica = "Prov. con Presupuesto";
            campo = "provpresu";
        } else if (txt_provpresuid.getValue().matches("[0-9]*") && Integer.parseInt(txt_provpresuid.getValue()) <= 0) {
            s_verifica = "Prov. con Presupuesto";
            campo = "provpresu";
        } else if (cb_prouniven.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Unidad manejo - Venta";
            campo = "prouniven";
        } else if (txt_prounipresven.getValue() == null) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Unidad de presentación - Venta";
            campo = "prounipresven";
        } else if (cb_proemppresven.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Empaque individual - Venta";
            campo = "proemppresven";
        } else if (cb_profrac.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Venta x fracción";
            campo = "profrac";
        } else if (txt_prostknofact.getValue() == null) {
            s_verifica = "Stock no facturable";
            campo = "prostknofact";
        } else if (cb_proprocedencia.getSelectedIndex() == -1) {
            tab_mantenimiento.setSelected(true);
            s_verifica = "Procedencia";
            campo = "proprocedencia";
        } else if (cb_prorot.getSelectedIndex() == -1) {
            s_verifica = "Rotación";
            campo = "prorot";
        } else if (txt_propeso.getValue() == null) {
            s_verifica = "Peso";
            campo = "propeso";
        } else if (cb_prounimed.getSelectedIndex() == -1) {
            s_verifica = "Unidad de medida - Venta";
            campo = "prounimed";
        } else if (cb_proextsun.getSelectedIndex() == -1) {
            s_verifica = "Tipo de existencia - Sunat";
            campo = "proextsun";
        } else if (cb_promedsun.getSelectedIndex() == -1) {
            s_verifica = "Unidad de medida - Sunat";
            campo = "promedsun";
        } else if (cb_conimp.getSelectedIndex() == -1) {
            s_verifica = "Impuesto";
            campo = "conimp";
        } else if (cb_proimp.getSelectedIndex() == -1) {
            s_verifica = "Tipo de impuesto";
            campo = "proimp";
        } else if (cb_proindperc.getSelectedIndex() == -1) {
            s_verifica = "Afecto a percepción";
            campo = "proindperc";
        } else if (sp_proordkar.getValue() == null) {
            s_verifica = "Orden de kardex";
            campo = "proordkar";
        } else if (sp_prolstpre.getValue() == null) {
            s_verifica = "Orden de lista de precio";
            campo = "prolstpre";
        } else if (sp_procons.getValue() == null) {
            s_verifica = "Orden de consolidado";
            campo = "procons";
        } else if (sp_ord.getValue() == null) {
            s_verifica = "Orden";
            campo = "ord";
        } else if (cb_protipcodbar.getSelectedIndex() == -1) {
            s_verifica = "Tipo de código de barras";
            campo = "protipcodbar";
        } else {
            s_verifica = "";
        }
        return s_verifica;
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
