package org.me.gj.controller.distribucion.mantenimiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.distribucion.mantenimiento.Carroceria;
import org.me.gj.model.distribucion.mantenimiento.Categoria;
import org.me.gj.model.distribucion.mantenimiento.Color;
import org.me.gj.model.distribucion.mantenimiento.Combustible;
import org.me.gj.model.distribucion.mantenimiento.EmpSeguro;
import org.me.gj.model.distribucion.mantenimiento.HistoPropietario;
import org.me.gj.model.distribucion.mantenimiento.Marca;
import org.me.gj.model.distribucion.mantenimiento.Modelo;
import org.me.gj.model.distribucion.mantenimiento.Propietario;
import org.me.gj.model.distribucion.mantenimiento.Soat;
import org.me.gj.model.distribucion.mantenimiento.Transmision;
import org.me.gj.model.distribucion.mantenimiento.Vehiculo;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerVehiculo extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Listbox lst_vehiculo,
            lst_propietario,
            lst_soat;
    @Wire
    Textbox txt_transid, txt_busqueda, txt_transalias, txt_transplaca, txt_transvin, txt_transchasis, txt_transmotor, txt_transversion,
            txt_transpotencia, txt_transtraccion,
            txt_transimg, txt_transnomrep,
            txt_soat_nro, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busqueda, cb_transmarca, cb_transcombustible, cb_transcategoria, cb_transmodelo, cb_transcolor,
            cb_transtransmision, cb_transcarroceria,
            cb_prop,
            cb_seguro, cb_estado;
    @Wire
    Button btn_buscar;
    @Wire
    Intbox txt_transkey, txt_aniofab, txt_aniomod, txt_transejes, txt_transasientos, txt_transpasajeros, txt_transruedas,
            txt_transcilindros,
            txt_prop_id,
            txt_soat_id;
    @Wire
    Doublebox txt_transcilindradas,
            txt_transpesob, txt_transpeson, txt_transcarga, txt_translargo, txt_transaltura,
            txt_transancho;
    @Wire
    Datebox txt_transfecing, txt_transfecsal,
            txt_soat_fecini, txt_soat_fecfin,
            d_fecadd, d_fecmod;
    @Wire
    Checkbox chk_transestado, chk_busest,
            chk_prop_est,
            chk_soat_est;
    @Wire
    Spinner sp_transord;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir,
            tbbtn_btn_imagenpro,
            tbbtn_btn_nuevop, tbbtn_btn_editarp, tbbtn_btn_eliminarp, tbbtn_btn_guardarp, tbbtn_btn_deshacerp,
            tbbtn_btn_nuevos, tbbtn_btn_editars, tbbtn_btn_eliminars, tbbtn_btn_guardars, tbbtn_btn_deshacers;
    @Wire
    Tab tab_listavehiculo, tab_mantenimiento, tab_medidas, tab_propietariosoat/*, tab_soat*/;
    @Wire
    Div img_vehiculo;

    //Instancias de Objetos
    ListModelList<Vehiculo> objlstVehiculo;
    ListModelList<Marca> objlstMarca;
    ListModelList<Categoria> objlstCategoria;
    ListModelList<Color> objlstColor;
    ListModelList<Carroceria> objlstCarroceria;
    ListModelList<Combustible> objlstCombustible;
    ListModelList<Modelo> objlstModelo;
    ListModelList<Transmision> objlstTransmision;
    ListModelList<EmpSeguro> objlstEmpresa;
    ListModelList<Propietario> objlstPropietario;

    ListModelList<HistoPropietario> objlstHistorico, objlstHistoricoEli;
    ListModelList<Soat> objlstSoat, objlstSoatEli;

    DaoVehiculo objDaoVehiculo;
    DaoMarca objDaoMarca;
    DaoCategoria objDaoCategoria;
    DaoColor objDaoColor;
    DaoCarroceria objDaoCarroceria;
    DaoCombustible objDaoCombustible;
    DaoModelo objDaoModelo;
    DaoTransmision objDaoTransmision;
    DaoPropietario objDaoPropietario;
    DaoEmpresa objDaoEmpresa;
    DaoAccesos objDaoAccesos;

    //Vehiculo objVehiculo = new Vehiculo();
    Vehiculo objVehiculo;

    HistoPropietario objHistoPropietario = new HistoPropietario();
    Soat objSoat = new Soat();

    Accesos objAccesos;
    ParametrosSalida objparam;
    //Utilitarios objUtil = new Utilitarios();

    //Variables publicas
    String s_estado = "Q";
    String s_estado_propietario = "Q";
    String s_estado_soat = "Q";
    String s_codigo_propietario = "";
    String s_codigo_soat = "";
    String s_mensaje = "";
    String campo = "";
    Media media;
    File archivo;
    boolean cargaImagen = false;
    int i_sel;
    int valor;
    int i_codigo_vehiculo;
    String fimg_raiz = "IMG_ERP";
    String fimg_vehiculo = "VEHICULOS";

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerVehiculo.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_vehiculo")
    public void llenaRegistros() throws SQLException {
        //carga lista de vehiculos
        objDaoVehiculo = new DaoVehiculo();
        objlstVehiculo = objDaoVehiculo.listaVehiculo(1);
        lst_vehiculo.setModel(objlstVehiculo);
        //combos
        objDaoMarca = new DaoMarca();
        objlstMarca = objDaoMarca.listaMarca(1);
        cb_transmarca.setModel(objlstMarca);
        objDaoCombustible = new DaoCombustible();
        objlstCombustible = objDaoCombustible.listaCombustible(1);
        cb_transcombustible.setModel(objlstCombustible);
        objDaoCategoria = new DaoCategoria();
        objlstCategoria = objDaoCategoria.listaCategoria(1);
        cb_transcategoria.setModel(objlstCategoria);
        objDaoModelo = new DaoModelo();
        objlstModelo = objDaoModelo.listaModelo(1);
        cb_transmodelo.setModel(objlstModelo);
        objDaoColor = new DaoColor();
        objlstColor = objDaoColor.listaColor(1);
        cb_transcolor.setModel(objlstColor);
        objDaoTransmision = new DaoTransmision();
        objlstTransmision = objDaoTransmision.listaTransmision(1);
        cb_transtransmision.setModel(objlstTransmision);
        objDaoCarroceria = new DaoCarroceria();
        objlstCarroceria = objDaoCarroceria.listaCarroceria(1);
        cb_transcarroceria.setModel(objlstCarroceria);
        objDaoPropietario = new DaoPropietario();
        objlstPropietario = objDaoPropietario.listaPropietario(1);
        cb_prop.setModel(objlstPropietario);
        objDaoEmpresa = new DaoEmpresa();
        objlstEmpresa = objDaoEmpresa.listaEmpresa(1);
        cb_seguro.setModel(objlstEmpresa);
        //
        objlstHistoricoEli = new ListModelList<HistoPropietario>();
        objlstSoatEli = new ListModelList<Soat>();
        //
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
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(30104010, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Vehiculo con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Vehiculo con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Vehiculo");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Vehiculo");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un Vehiculo");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un Vehiculo");
        }
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Vehiculo");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Vehiculo");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de un Vehiculo");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de un Vehiculo");
        }
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        LimpiarLista();
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
        objlstVehiculo = new ListModelList<Vehiculo>();
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
        }
        objlstVehiculo = objDaoVehiculo.busquedaVehiculo(i_seleccion, s_consulta, i_estado);
        if (objlstVehiculo.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstVehiculo.getSize() + " registro(s)");
        }
        if (objlstVehiculo.getSize() > 0) {
            lst_vehiculo.setModel(objlstVehiculo);
        } else {
            objlstVehiculo = null;
            lst_vehiculo.setModel(objlstVehiculo);
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

    @Listen("onSelect=#lst_vehiculo")
    public void seleccionaRegistro() throws SQLException, ParseException {
        //limpiamos los datos anteriores
        //OnChange();
        objVehiculo = new Vehiculo();
        objVehiculo = (Vehiculo) lst_vehiculo.getSelectedItem().getValue();
        if (objVehiculo == null) {
            objVehiculo = objlstVehiculo.get(i_sel + 1);
        }
        i_sel = lst_vehiculo.getSelectedIndex();

        //cargamos las listas con los soat y el propietario de los vehiculos
        objlstSoat = objDaoVehiculo.listaSoat(objVehiculo.getTrans_key());
        lst_soat.setModel(objlstSoat);
        objlstHistorico = objDaoVehiculo.listaPropietario(objVehiculo.getTrans_key());
        lst_propietario.setModel(objlstHistorico);

        //cargamos los campos
        limpiarCampos();
        llenarCampos(objVehiculo);
        //solo lectura
        habilitaSubmantenimiento(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objVehiculo.getTrans_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        s_estado = "N";

        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);

        //limpiamos las listas de soat y propietario
        objlstHistorico = new ListModelList<HistoPropietario>();
        lst_propietario.setModel(objlstHistorico);
        objlstSoat = new ListModelList<Soat>();
        lst_soat.setModel(objlstSoat);

        //activamos los tabs de propietario y soat
        habilitaSubmantenimiento(false);

        chk_transestado.setDisabled(true);
        chk_transestado.setChecked(true);
        txt_transplaca.focus();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() {
        if (lst_vehiculo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un vehículo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "M";
            if (tab_listavehiculo.isSelected()) {
                tab_mantenimiento.setSelected(true);
            }
            habilitaTab(true, false);
            habilitaBotones(true, false);
            habilitaCampos(false);
            //txt_transplaca.setDisabled(true);
            //campos de edicion
            /*chk_transestado.setDisabled(false);
             txt_transalias.setDisabled(false);
             tbbtn_btn_imagenpro.setDisabled(false);
             txt_transfecsal.setDisabled(false);
             sp_transord.setDisabled(false);
             txt_transnomrep.setDisabled(false);*/
            //
            habilitaSubmantenimiento(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                validafocos();
                            }
                        }
                    });
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstVehiculo = new ListModelList<Vehiculo>();
                                objlstHistorico.addAll(objlstHistoricoEli);
                                objlstSoat.addAll(objlstSoatEli);
                                archivo = new File("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + txt_transplaca.getValue().toUpperCase() + ".JPEG");
                                if (s_estado.equals("N")) {
                                    objVehiculo = generaRegistro(1);
                                    objparam = objDaoVehiculo.insertarVehiculo(objVehiculo, getLisHisPropietario(objlstHistorico), getLisSoat(objlstSoat));
                                    Files.copy(archivo, media.getStreamData());
                                } else {
                                    String placa_ant = objVehiculo.getTrans_placa();
                                    objVehiculo = generaRegistro(1);
                                    objparam = objDaoVehiculo.actualizarVehiculo(objVehiculo, getLisHisPropietario(objlstHistorico), getLisSoat(objlstSoat));
                                    if (objparam.getFlagIndicador() == 0) {
                                        if (cargaImagen == true) {
                                            if (archivo.exists()) {
                                                String s_men = "Esta imagen de vehículo ya existe. ¿Desea Reemplazarla?";
                                                Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                                        Messagebox.QUESTION, new EventListener() {
                                                    @Override
                                                    public void onEvent(Event event) throws Exception {
                                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                            Files.copy(archivo, media.getStreamData());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Files.copy(archivo, media.getStreamData());
                                            }
                                        }else{
                                            FileInputStream fis = new FileInputStream("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + placa_ant + ".JPEG");
                                            archivo = new File("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + txt_transplaca.getValue().toUpperCase() + ".JPEG");
                                            Files.copy(archivo, fis);
                                            fis.close();
                                        }
                                    }
                                }
                                Messagebox.show(objparam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                cargaImagen = false;
                                tab_listavehiculo.setSelected(true);
                                tab_listavehiculo.setDisabled(false);
                                //tab_propietariosoat.setDisabled(true);
                                habilitaBotones(false, true);
                                VerificarTransacciones();
                                //
                                habilitaCampos(true);
                                //
                                limpiarCampos();
                                limpiarCamposHistorico();
                                limpiarCamposSoat();
                                limpiaAuditoria();
                                //cargamos la lista de vehiculos
                                objlstVehiculo = objDaoVehiculo.listaVehiculo(1);
                                lst_vehiculo.setModel(objlstVehiculo);
                                lst_vehiculo.setSelectedIndex(-1);

                                //limpiamos las listas de soat y propietario
                                objlstHistorico = new ListModelList<HistoPropietario>();
                                lst_propietario.setModel(objlstHistorico);
                                objlstSoat = new ListModelList<Soat>();
                                lst_soat.setModel(objlstSoat);

                                s_estado_propietario = "Q";
                                s_estado_soat = "Q";
                                s_estado = "Q";
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_vehiculo.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un vehículo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            String s_str = "Está seguro que desea eliminar el vehículo";
            Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                int trans_key = txt_transkey.getValue();
                                objparam = objDaoVehiculo.eliminarVehiculo(trans_key);
                                Messagebox.show(objparam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

                                //validacion de eliminacion
                                tbbtn_btn_eliminar.setDisabled(false);
                                VerificarTransacciones();
                                //
                                //limpiamos los datos si es que selecciono algun valor de la lista
                                limpiarCampos();
                                limpiaAuditoria();
                                //limpiamos las listas de contactos y telefonos

                                objlstHistorico = new ListModelList<HistoPropietario>();
                                lst_propietario.setModel(objlstHistorico);
                                objlstSoat = new ListModelList<Soat>();
                                lst_soat.setModel(objlstSoat);

                                //cargamos la lista
                                objlstVehiculo = new ListModelList<Vehiculo>();
                                objlstVehiculo = objDaoVehiculo.listaVehiculo(1);
                                lst_vehiculo.setModel(objlstVehiculo);
                                //volvemos el foco a la vista inicial
                                tab_listavehiculo.setSelected(true);
                                tab_listavehiculo.setDisabled(false);
                                s_estado_propietario = "Q";
                                s_estado_soat = "Q";
                                s_estado = "Q";
                            }
                        }
                    });
        }

    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            tab_listavehiculo.setSelected(true);
                            tab_listavehiculo.setDisabled(false);
                            //habilitaTab(false, true);
                            habilitaBotones(false, true);
                            //tab_propietariosoat.setDisabled(true);
                            //validacion de deshacer
                            VerificarTransacciones();
                            //
                            limpiarCampos();
                            limpiarCamposHistorico();
                            limpiarCamposSoat();
                            limpiaAuditoria();
                            habilitaCampos(true);

                            //limpiamos las listas
                            //objlstHistorico = new ListModelList<HistoPropietario>();
                            objlstHistorico = null;
                            lst_propietario.setModel(objlstHistorico);
                            //objlstSoat = new ListModelList<Soat>();
                            objlstSoat = null;
                            lst_soat.setModel(objlstSoat);

                            lst_vehiculo.setSelectedIndex(-1);
                            s_estado_propietario = "Q";
                            s_estado_soat = "Q";
                            s_estado = "Q";
                            //lst_vehiculo.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (objlstVehiculo == null || objlstVehiculo.isEmpty()) {
            Messagebox.show("No hay registros de vehículos para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_vehiculo.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/distribucion/mantenimiento/LovImpresionVehiculo.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevop")
    public void botonNuevoPropietario() {
        s_estado_propietario = "N";
        limpiarCamposHistorico();
        habilitaBotonesHistprop(true, false);
        //PROPIETARIO
        chk_prop_est.setChecked(true);
        chk_prop_est.setDisabled(true);
        cb_prop.setDisabled(false);
        Utilitarios.deshabilitarLista(true, lst_propietario);
        lst_propietario.clearSelection();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro de Propietario");
    }

    @Listen("onClick=#tbbtn_btn_deshacerp")
    public void botonDeshacerPropietario() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesHistprop(false, true);
                            Utilitarios.deshabilitarLista(false, lst_propietario);
                            limpiarCamposHistorico();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios en Propietario");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardarp")
    public void AgregaPropietario() {
        if (cb_prop.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un propietario");
        } else {
            int cod = cb_prop.getSelectedItem().getValue();
            if ("N".equals(s_estado_propietario)) {
                if (existePropietario(cod)) {
                    Messagebox.show("Ya se encuentra registrado el propietario, verifique su estado");
                } else {
                    objHistoPropietario = null;
                    objHistoPropietario = new HistoPropietario();
                    objHistoPropietario.setInd_accion("N");
                    objHistoPropietario.setHprop_usuadd(objUsuCredential.getCuenta());
                    objHistoPropietario.setProp_id(cod);
                    objHistoPropietario.setProp_razsoc(cb_prop.getValue());
                    objHistoPropietario.setHprop_est(chk_prop_est.isChecked() ? 1 : 2);
                    objlstHistorico.add(objHistoPropietario);
                }
            } else if ("M".equals(s_estado_propietario)) {
                objHistoPropietario.setInd_accion("M");
                objHistoPropietario.setHprop_usumod(objUsuCredential.getCuenta());
                objHistoPropietario.setHprop_est(chk_prop_est.isChecked() ? 1 : 2);
            } else if ("NM".equals(s_estado_propietario)) {
                objHistoPropietario.setInd_accion("N");
                objHistoPropietario.setHprop_usuadd(objUsuCredential.getCuenta());
                objHistoPropietario.setHprop_est(chk_prop_est.isChecked() ? 1 : 2);
            }
            habilitaBotonesHistprop(false, true);
            limpiarCamposHistorico();
            objlstHistorico.clearSelection();
            lst_propietario.setModel(objlstHistorico);
            Utilitarios.deshabilitarLista(false, lst_propietario);
        }
    }

    @Listen("onClick=#tbbtn_btn_editarp")
    public void ModificaPropietario() throws SQLException {
        if (lst_propietario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un propietario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objHistoPropietario = (HistoPropietario) lst_propietario.getSelectedItem().getValue();
            if (objHistoPropietario.getInd_accion().equals("N")) {
                s_estado_propietario = "NM";
            } else {
                s_estado_propietario = "M";
            }
            chk_prop_est.setDisabled(false);
            habilitaBotonesHistprop(true, false);
            Utilitarios.deshabilitarLista(true, lst_propietario);
            objlstHistoricoEli = null;
            objlstHistoricoEli = new ListModelList<HistoPropietario>();
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro de Propietario");
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminarp")
    public void EliminarPropietario() throws SQLException {
        if (objlstHistorico.isEmpty()) {
            Messagebox.show("Por favor seleccione un propietario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (lst_propietario.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un propietario", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Está seguro que desea eliminar al propietario?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objHistoPropietario = (HistoPropietario) lst_propietario.getSelectedItem().getValue();
                                int posicion = lst_propietario.getSelectedIndex();
                                if (s_estado.equals("M") && !objHistoPropietario.getInd_accion().equals("N")) {
                                    objHistoPropietario.setInd_accion("E");
                                    objHistoPropietario.setHprop_usumod(objUsuCredential.getCuenta());
                                    objlstHistoricoEli.add(objHistoPropietario);
                                }
                                objlstHistorico.remove(posicion);
                                lst_propietario.setModel(objlstHistorico);
                                limpiarCamposHistorico();
                            }
                        }
                    });
        }
    }

    @Listen("onSelect=#lst_propietario")
    public void seleccionaPropietario() throws SQLException {
        objHistoPropietario = (HistoPropietario) lst_propietario.getSelectedItem().getValue();
        //cargamos los datos
        llenarCamposPropietario();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objHistoPropietario.getHprop_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevos")
    public void botonNuevoSoat() {
        s_estado_soat = "N";
        limpiarCamposSoat();
        habilitaBotonesSoat(true, false);
        //TELEFONOS 
        chk_soat_est.setChecked(true);
        chk_soat_est.setDisabled(true);
        cb_seguro.setDisabled(false);
        txt_soat_nro.setDisabled(false);
        txt_soat_fecini.setDisabled(false);
        txt_soat_fecfin.setDisabled(false);

        Utilitarios.deshabilitarLista(true, lst_soat);
        lst_soat.clearSelection();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro de Soat");
        /*if ("M".equals(s_estado)) {
         objlstSoat = new ListModelList<Soat>();
         lst_soat.setModel(objlstSoat);
         }*/
    }

    @Listen("onClick=#tbbtn_btn_deshacers")
    public void botonDeshacerSoat() {
        String s_str = "Está seguro que desea deshacer los cambios";
        Messagebox.show(s_str, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            habilitaBotonesSoat(false, true);
                            Utilitarios.deshabilitarLista(false, lst_soat);
                            limpiarCamposSoat();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios de Soat");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardars")
    public void AgregaSoat() throws SQLException {
        String verifica = verificarSoat();
        if (!verifica.isEmpty()) {
            Messagebox.show("Por favor ingresar datos en " + verifica);
        } else {
            int cod = cb_seguro.getSelectedItem().getValue();
            if ("N".equals(s_estado_soat)) {
                /*if (existeSoat(cod)) {
                 Messagebox.show("Ya se encuentra registrado el propietario, verifique su estado");
                 } else {*/
                objSoat = null;
                objSoat = new Soat();
                objSoat.setInd_accion("N");
                objSoat.setTab_id(cod);
                objSoat.setSoat_fecini(txt_soat_fecini.getValue());
                objSoat.setSoat_fecfin(txt_soat_fecfin.getValue());
                objSoat.setSoat_nro(txt_soat_nro.getValue().toUpperCase());
                //objSoat.setSoat_id(cod);
                objSoat.setSoat_est(chk_soat_est.isChecked() ? 1 : 2);
                objSoat.setTab_subdes(cb_seguro.getValue().toUpperCase());
                objSoat.setSoat_usuadd(objUsuCredential.getCuenta());
                objlstSoat.add(objSoat);
                //}
            } else if ("M".equals(s_estado_soat)) {
                objSoat.setInd_accion("M");
                objSoat.setTab_id(cod);
                objSoat.setSoat_fecini(txt_soat_fecini.getValue());
                objSoat.setSoat_fecfin(txt_soat_fecfin.getValue());
                objSoat.setSoat_nro(txt_soat_nro.getValue().toUpperCase());
                //objSoat.setSoat_id(cod);
                objSoat.setSoat_est(chk_soat_est.isChecked() ? 1 : 2);
                objSoat.setTab_subdes(cb_seguro.getValue().toUpperCase());
                objSoat.setSoat_usumod(objUsuCredential.getCuenta());
                //objSoat.setSoat_usumod(objUsuCredential.getCuenta());
                //objSoat.setSoat_est(chk_soat_est.isChecked() ? 1 : 2);
            } else if ("NM".equals(s_estado_soat)) {
                objSoat.setInd_accion("N");
                objSoat.setTab_id(cod);
                objSoat.setSoat_fecini(txt_soat_fecini.getValue());
                objSoat.setSoat_fecfin(txt_soat_fecfin.getValue());
                objSoat.setSoat_nro(txt_soat_nro.getValue().toUpperCase());
                //objSoat.setSoat_id(cod);
                objSoat.setSoat_est(chk_soat_est.isChecked() ? 1 : 2);
                objSoat.setTab_subdes(cb_seguro.getValue().toUpperCase());
                objSoat.setSoat_usuadd(objUsuCredential.getCuenta());
                //objSoat.setSoat_usuadd(objUsuCredential.getCuenta());
                //objSoat.setSoat_est(chk_soat_est.isChecked() ? 1 : 2);
            }

            habilitaBotonesSoat(false, true);
            limpiarCamposSoat();
            objlstSoat.clearSelection();
            lst_soat.setModel(objlstSoat);
            Utilitarios.deshabilitarLista(false, lst_soat);
        }
    }

    @Listen("onClick=#tbbtn_btn_editars")
    public void ModificaSoat() throws SQLException, ParseException {
        if (lst_soat.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un soat", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            objSoat = (Soat) lst_soat.getSelectedItem().getValue();
            if (objSoat.getInd_accion().equals("N")) {
                s_estado_soat = "NM";
            } else {
                s_estado_soat = "M";
            }
            /*chk_soat_est.setDisabled(false);
             txt_soat_fecini.setDisabled(false);
             txt_soat_fecfin.setDisabled(false);*/
            habilitaBotonesSoat(true, false);
            habilitaCamposSoat(false);
            Utilitarios.deshabilitarLista(true, lst_soat);
            objlstSoatEli = null;
            objlstSoatEli = new ListModelList<Soat>();

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro de Soat");
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminars")
    public void EliminarSoat() throws SQLException {
        if (objlstSoat.isEmpty()) {
            Messagebox.show("Por favor seleccione un soat", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (lst_soat.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un soat", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("Está seguro que desea eliminar el soat?", "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objSoat = (Soat) lst_soat.getSelectedItem().getValue();
                                int posicion = lst_soat.getSelectedIndex();
                                if (s_estado.equals("M") && !objSoat.getInd_accion().equals("N")) {
                                    objSoat.setInd_accion("E");
                                    objSoat.setSoat_usumod(objUsuCredential.getCuenta());
                                    objlstSoatEli.add(objSoat);
                                }
                                objlstSoat.remove(posicion);
                                lst_soat.setModel(objlstSoat);
                                limpiarCamposSoat();
                            }
                        }
                    });
        }
    }

    @Listen("onSelect=#lst_soat")
    public void seleccionaSoat() throws SQLException, ParseException {
        objSoat = (Soat) lst_soat.getSelectedItem().getValue();
        //cargamos los datos del contacto
        llenarCamposSoat();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objSoat.getSoat_id());
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manvehiculo")
    public void GuardarInformacion(Event event) throws SQLException {
        int keyCode;
        keyCode = ((KeyEvent) event).getKeyCode();
        switch (keyCode) {
            case 113:
                if (!tbbtn_btn_nuevo.isDisabled()) {
                    botonNuevo();
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
            llenaRegistros();
        }
    }

    @Listen("onOK=#txt_busqueda")
    public void onOKBusqueda() {
        btn_buscar.setFocus(true);
    }

    @Listen("onOK=#txt_transplaca")
    public void onOK_txt_transplaca() {
        txt_transalias.focus();
    }

    @Listen("onOK=#txt_transalias")
    public void onOK_txt_transalias() {
        cb_transmarca.focus();
        cb_transmarca.select();
    }

    @Listen("onOK=#cb_transmarca")
    public void onOK_cb_transmarca() {
        cb_transcombustible.focus();
        cb_transcombustible.select();
    }

    @Listen("onOK=#cb_transcombustible")
    public void onOK_cb_transcombustible() {
        cb_transcategoria.focus();
        cb_transcategoria.select();
    }

    @Listen("onOK=#cb_transcategoria")
    public void onOK_cb_transcategoria() {
        cb_transmodelo.focus();
        cb_transmodelo.select();
    }

    @Listen("onOK=#cb_transmodelo")
    public void onOK_cb_transmodelo() {
        cb_transcolor.focus();
        cb_transcolor.select();
    }

    @Listen("onOK=#cb_transcolor")
    public void onOK_cb_transcolor() {
        cb_transtransmision.focus();
        cb_transtransmision.select();
    }

    @Listen("onOK=#cb_transtransmision")
    public void onOK_cb_transtransmision() {
        cb_transcarroceria.focus();
        cb_transcarroceria.select();
    }

    @Listen("onOK=#cb_transcarroceria")
    public void onOK_cb_transcarroceria() {
        txt_transvin.focus();
    }

    @Listen("onOK=#txt_transvin")
    public void onOK_txt_transvin() {
        txt_transchasis.focus();
    }

    @Listen("onOK=#txt_transchasis")
    public void onOK_txt_transchasis() {
        txt_transmotor.focus();
    }

    @Listen("onOK=#txt_transmotor")
    public void onOK_txt_transmotor() {
        txt_aniofab.focus();
    }

    @Listen("onOK=#txt_aniofab")
    public void onOK_txt_aniofab() {
        txt_aniomod.focus();
    }

    @Listen("onOK=#txt_aniomod")
    public void onOK_txt_aniomod() {
        txt_transversion.focus();
    }

    @Listen("onOK=#txt_transversion")
    public void onOK_txt_transversion() {
        txt_transejes.focus();
    }

    @Listen("onOK=#txt_transejes")
    public void onOK_txt_transejes() {
        txt_transasientos.focus();
    }

    @Listen("onOK=#txt_transasientos")
    public void onOK_txt_transasientos() {
        txt_transpasajeros.focus();
    }

    @Listen("onOK=#txt_transpasajeros")
    public void onOK_txt_transpasajeros() {
        txt_transruedas.focus();
    }

    @Listen("onOK=#txt_transruedas")
    public void onOK_txt_transruedas() {
        txt_transpotencia.focus();
    }

    @Listen("onOK=#txt_transpotencia")
    public void onOK_txt_transpotencia() {
        txt_transcilindros.focus();
    }

    @Listen("onOK=#txt_transcilindros")
    public void onOK_txt_transcilindros() {
        txt_transcilindradas.focus();
    }

    @Listen("onOK=#txt_transcilindradas")
    public void onOK_txt_transcilindradas() {
        txt_transtraccion.focus();
    }

    @Listen("onOK=#txt_transtraccion")
    public void onOK_txt_transtraccion() {
        tab_medidas.setSelected(true);
        txt_transpesob.focus();
    }

    @Listen("onOK=#txt_transpesob")
    public void onOK_txt_transpesob() {
        txt_transpeson.focus();
    }

    @Listen("onOK=#txt_transpeson")
    public void onOK_txt_transpeson() {
        txt_transcarga.focus();
    }

    @Listen("onOK=#txt_transcarga")
    public void onOK_txt_transcarga() {
        txt_translargo.focus();
    }

    @Listen("onOK=#txt_translargo")
    public void onOK_txt_translargo() {
        txt_transaltura.focus();
    }

    @Listen("onOK=#txt_transaltura")
    public void onOK_txt_transaltura() {
        txt_transancho.focus();
    }

    @Listen("onOK=#txt_transancho")
    public void onOK_txt_transancho() {
        txt_transfecing.focus();
    }

    @Listen("onOK=#txt_transfecing")
    public void onOK_txt_transfecing() {
        txt_transfecsal.focus();
    }

    @Listen("onOK=#txt_transfecsal")
    public void onOK_txt_transfecsal() {
        sp_transord.focus();
    }

    @Listen("onOK=#sp_transord")
    public void onOK_sp_transord() {
        txt_transnomrep.focus();
    }

    @Listen("onUpload=#tbbtn_btn_imagenpro")
    public void cargarImagen(UploadEvent event) throws Exception {
        if (txt_transplaca.getText().isEmpty()) {
            Messagebox.show("Por favor verifique el campo Placa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            media = event.getMedia();
            if (media instanceof AImage) {
                img_vehiculo.getChildren().clear();
                Image foto_vehiculo = new Image();
                foto_vehiculo.setContent((AImage) media);
                foto_vehiculo.setParent(img_vehiculo);
                foto_vehiculo.setStyle("border: 2px solid #2E9AFE; width : 350px; height : 190px;  ");
                txt_transimg.setValue("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + txt_transplaca.getValue().toUpperCase().trim() + ".JPEG");
                cargaImagen = true;
            }
        }
    }

    @Listen("onBlur=#txt_soat_fecini")
    public void generafecvencsoat() {
        if (txt_soat_fecini.getValue() != null) {
            txt_soat_fecfin.setValue(Utilitarios.sumaDias(txt_soat_fecini.getValue(), 366));
        }
    }

    //Eventos Otros 
    public void llenarCamposPropietario() {
        chk_prop_est.setChecked(objHistoPropietario.getHprop_est() == 1);
        txt_prop_id.setValue(objHistoPropietario.getProp_id());
        cb_prop.setSelectedItem(Utilitarios.valorPorTexto1(cb_prop, objHistoPropietario.getProp_id()));
    }

    public void llenarCamposSoat() {
        chk_soat_est.setChecked(objSoat.getSoat_est() == 1);
        txt_soat_id.setValue(objSoat.getSoat_id());
        txt_soat_nro.setValue(objSoat.getSoat_nro());
        txt_soat_fecini.setValue(objSoat.getSoat_fecini());
        txt_soat_fecfin.setValue(objSoat.getSoat_fecfin());
        cb_seguro.setSelectedItem(Utilitarios.valorPorTexto1(cb_seguro, objSoat.getTab_id()));
    }

    public String verificarSoat() {
        if (cb_seguro.getSelectedIndex() == -1) {
            campo = "Empresa Aseguradora";
            cb_seguro.focus();
        } else if (txt_soat_nro.getValue().isEmpty()) {
            campo = "Numero de Soat";
            txt_soat_nro.focus();
        } else if (txt_soat_fecini.getValue() == null) {
            campo = "Fecha de Inicio";
            txt_soat_fecini.focus();
        } else if (txt_soat_fecfin.getValue() == null) {
            campo = "Fecha de Vencimiento";
            txt_soat_fecfin.focus();
        } else {
            campo = "";
        }
        return campo;
    }

    public Vehiculo generaRegistro(int transaccion) {
        int codigo, sucursal, empresa, marca, combustible, categoria, modelo, color, transmision,
                carroceria, aniofab, aniomodelo, ejes, asientos, pasajeros, ruedas, cilindros, orden, estado;
        String alias, placa, vin, chasis, motor, version, potencia, traccion, imagen, reporte, codigoid;
        double cilindrada, pesobruto, pesoneto, cargautil, largo, alto, ancho;
        Date fechaingreso, fechasalida;

        //codigoid = objVehiculo.getTrans_id();
        codigo = txt_transkey.getValue() == null ? 0 : txt_transkey.getValue();
        sucursal = objUsuCredential.getCodsuc();
        empresa = objUsuCredential.getCodemp();
        placa = txt_transplaca.getValue().toUpperCase().trim();
        alias = txt_transalias.getValue().toUpperCase().trim();
        marca = cb_transmarca.getSelectedItem().getValue();
        combustible = cb_transcombustible.getSelectedItem().getValue();
        categoria = cb_transcategoria.getSelectedItem().getValue();
        modelo = cb_transmodelo.getSelectedItem().getValue();
        color = cb_transcolor.getSelectedItem().getValue();
        transmision = cb_transtransmision.getSelectedItem().getValue();
        carroceria = cb_transcarroceria.getSelectedItem().getValue();
        vin = txt_transvin.getValue().toUpperCase().trim();
        //auxiliares
        chasis = txt_transchasis.getValue().toUpperCase().trim();
        motor = txt_transmotor.getValue().toUpperCase().trim();
        aniofab = txt_aniofab.getValue() == null ? 0 : txt_aniofab.getValue();
        aniomodelo = txt_aniomod.getValue() == null ? 0 : txt_aniomod.getValue();
        version = txt_transversion.getValue().toUpperCase().trim();
        ejes = txt_transejes.getValue() == null ? 0 : txt_transejes.getValue();
        asientos = txt_transasientos.getValue() == null ? 0 : txt_transasientos.getValue();
        pasajeros = txt_transpasajeros.getValue() == null ? 0 : txt_transpasajeros.getValue();
        ruedas = txt_transruedas.getValue() == null ? 0 : txt_transruedas.getValue();
        potencia = txt_transpotencia.getValue().toUpperCase().trim();
        cilindros = txt_transcilindros.getValue() == null ? 0 : txt_transcilindros.getValue();
        cilindrada = txt_transcilindradas.getValue() == null ? 0 : txt_transcilindradas.getValue();
        traccion = txt_transtraccion.getValue().toUpperCase().trim();
        //otros
        //imagen = txt_transimg.getValue();
        imagen = "C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + txt_transplaca.getValue().toUpperCase() + ".JPEG";
        pesobruto = txt_transpesob.getValue();
        pesoneto = txt_transpeson.getValue();
        cargautil = txt_transcarga.getValue();
        largo = txt_translargo.getValue();
        alto = txt_transaltura.getValue();
        ancho = txt_transancho.getValue();
        fechaingreso = txt_transfecing.getValue();
        reporte = txt_transnomrep.getValue().toUpperCase().trim();
        fechasalida = txt_transfecsal.getValue() == null ? txt_transfecing.getValue() : txt_transfecsal.getValue();
        estado = chk_transestado.isChecked() ? 1 : 2;
        orden = sp_transord.getValue();
        String usu_add = objUsuCredential.getCuenta();
        String usu_mod = objUsuCredential.getCuenta();
        if (transaccion == 1) {//cuando es nuevo
            return new Vehiculo(codigo, empresa, sucursal, estado, alias, placa, marca, combustible, categoria, modelo, color, transmision, carroceria,
                    vin, chasis, motor, aniofab, aniomodelo, version, ejes, asientos, pasajeros, ruedas, potencia, cilindros, cilindrada,
                    traccion, pesobruto, pesoneto, cargautil, largo, alto, ancho, imagen, fechaingreso, fechasalida, orden, reporte, usu_add, usu_mod);
        } else {//cuando es modificar
            return new Vehiculo(codigo, alias, imagen, fechasalida, orden, reporte, estado, sucursal, empresa, usu_add, usu_mod);
        }
    }

    public void limpiarCampos() {
        //MANTENIMIENTO
        txt_transid.setValue("");
        txt_transkey.setValue(null);
        txt_transalias.setValue("");
        txt_transplaca.setValue("");
        cb_transmarca.setSelectedIndex(-1);
        cb_transcarroceria.setSelectedIndex(-1);
        cb_transcategoria.setSelectedIndex(-1);
        cb_transcolor.setSelectedIndex(-1);
        cb_transcombustible.setSelectedIndex(-1);
        cb_transmodelo.setSelectedIndex(-1);
        cb_transtransmision.setSelectedIndex(-1);
        txt_transvin.setValue("");
        txt_transchasis.setValue("");
        txt_transmotor.setValue("");
        txt_aniofab.setValue(null);
        txt_aniomod.setValue(null);
        txt_transversion.setValue("");
        txt_transejes.setValue(null);
        txt_transasientos.setValue(null);
        txt_transpasajeros.setValue(null);
        txt_transruedas.setValue(null);
        txt_transpotencia.setValue(null);
        txt_transcilindros.setValue(null);
        txt_transcilindradas.setValue(null);
        txt_transtraccion.setValue("");
        //OTROS
        txt_transpesob.setValue(null);
        txt_transpeson.setValue(null);
        txt_transcarga.setValue(null);
        txt_translargo.setValue(null);
        txt_transaltura.setValue(null);
        txt_transancho.setValue(null);
        txt_transfecing.setValue(null);
        txt_transfecsal.setValue(null);
        txt_transimg.setValue(null);
        img_vehiculo.getChildren().clear();
        sp_transord.setValue(0);
        txt_transnomrep.setValue("");
        //PROPIETARIO
        txt_prop_id.setValue(null);
        cb_prop.setSelectedIndex(-1);
        //SOAT
        txt_soat_id.setValue(null);
        txt_soat_fecini.setValue(null);
        txt_soat_fecfin.setValue(null);
        txt_soat_nro.setValue(null);
        cb_seguro.setSelectedIndex(-1);
    }

    public void llenarCampos(Vehiculo objVehiculo) throws ParseException {
        txt_transid.setValue(objVehiculo.getTrans_id());
        txt_transkey.setValue(objVehiculo.getTrans_key());
        txt_transalias.setValue(objVehiculo.getTrans_alias());
        txt_transplaca.setValue(objVehiculo.getTrans_placa());
        cb_transmarca.setSelectedItem(Utilitarios.valorPorTexto(cb_transmarca, objVehiculo.getTrans_marca()));
        cb_transcombustible.setSelectedItem(Utilitarios.valorPorTexto(cb_transcombustible, objVehiculo.getTrans_combustible()));
        cb_transcategoria.setSelectedItem(Utilitarios.valorPorTexto(cb_transcategoria, objVehiculo.getTrans_categoria()));
        cb_transmodelo.setSelectedItem(Utilitarios.valorPorTexto(cb_transmodelo, objVehiculo.getTrans_modelo()));
        cb_transcolor.setSelectedItem(Utilitarios.valorPorTexto(cb_transcolor, objVehiculo.getTrans_color()));
        cb_transtransmision.setSelectedItem(Utilitarios.valorPorTexto(cb_transtransmision, objVehiculo.getTrans_transmision()));
        cb_transcarroceria.setSelectedItem(Utilitarios.valorPorTexto(cb_transcarroceria, objVehiculo.getTrans_carroceria()));
        txt_transvin.setValue(objVehiculo.getTrans_vin());
        txt_transimg.setValue(objVehiculo.getTrans_img());
        Image foto_vehiculo = new Image();
        try {
            foto_vehiculo.setContent(new AImage("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + objVehiculo.getTrans_placa() + ".JPEG"));
            foto_vehiculo.setParent(img_vehiculo);
            foto_vehiculo.setStyle("border: 2px solid #2E9AFE; width : 350px; height : 190px; ");
            
            FileInputStream fis = new FileInputStream("C:\\" + fimg_raiz + "\\" + fimg_vehiculo + "\\" + objVehiculo.getTrans_placa() + ".JPEG");
            
        } catch (IOException ex) {

        }
        
        txt_transchasis.setValue(objVehiculo.getTrans_chasis());
        txt_transmotor.setValue(objVehiculo.getTrans_motor());
        txt_aniofab.setValue(objVehiculo.getTrans_aniofab());
        txt_aniomod.setValue(objVehiculo.getTrans_aniomodelo());
        txt_transversion.setValue(objVehiculo.getTrans_version());
        txt_transejes.setValue(objVehiculo.getTrans_ejes());
        txt_transasientos.setValue(objVehiculo.getTrans_asientos());
        txt_transpasajeros.setValue(objVehiculo.getTrans_pasajeros());
        txt_transruedas.setValue(objVehiculo.getTrans_ruedas());
        txt_transpotencia.setValue(objVehiculo.getTrans_potencia());
        txt_transcilindros.setValue(objVehiculo.getTrans_cilindros());
        txt_transcilindradas.setValue(objVehiculo.getTrans_cilindrada());
        txt_transtraccion.setValue(objVehiculo.getTrans_traccion());
        txt_transpesob.setValue(objVehiculo.getTrans_pesobruto());
        txt_transpeson.setValue(objVehiculo.getTrans_pesoneto());
        txt_transcarga.setValue(objVehiculo.getTrans_cargautil());
        txt_translargo.setValue(objVehiculo.getTrans_largo());
        txt_transaltura.setValue(objVehiculo.getTrans_altura());
        txt_transancho.setValue(objVehiculo.getTrans_ancho());
        txt_transfecing.setValue(objVehiculo.getTrans_fecing());
        txt_transfecsal.setValue(objVehiculo.getTrans_fecsal());
        sp_transord.setValue(objVehiculo.getTrans_ord());
        txt_transnomrep.setValue(objVehiculo.getTrans_nomrep());
        chk_transestado.setChecked(objVehiculo.getTrans_estado() == 1);
        //
        txt_usuadd.setValue(objVehiculo.getTrans_usuadd());
        d_fecadd.setValue(objVehiculo.getTrans_fecadd());
        txt_usumod.setValue(objVehiculo.getTrans_usumod());
        d_fecmod.setValue(objVehiculo.getTrans_fecmod());
    }

    public String verificar() {
        String mensaje;
        if (txt_transalias.getValue().isEmpty()) {
            campo = "Alias";
            mensaje = "El campo 'ALIAS' es obligatorio";
            txt_transalias.focus();
        } else if (txt_transplaca.getValue().isEmpty()) {
            campo = "Placa";
            mensaje = "El campo 'PLACA' es obligatorio";
        } else if (cb_transmarca.getSelectedIndex() == -1) {
            campo = "Marca";
            mensaje = "El campo 'MARCA' es obligatorio";
        } else if (cb_transcombustible.getSelectedIndex() == -1) {
            campo = "Combustible";
            mensaje = "El campo 'COMBUSTIBLE' es obligatorio";
        } else if (cb_transcategoria.getSelectedIndex() == -1) {
            campo = "Categoria";
            mensaje = "El campo 'CATEGORIA' es obligatorio";
        } else if (cb_transmodelo.getSelectedIndex() == -1) {
            campo = "Modelo";
            mensaje = "El campo 'MODELO' es obligatorio";
        } else if (cb_transcolor.getSelectedIndex() == -1) {
            campo = "Color";
            mensaje = "El campo 'COLOR' es obligatorio";
        } else if (cb_transtransmision.getSelectedIndex() == -1) {
            campo = "Transmision";
            mensaje = "El campo 'TRANSMISION' es obligatorio";
        } else if (cb_transcarroceria.getSelectedIndex() == -1) {
            campo = "Carroceria";
            mensaje = "El campo 'CARROCERIA' es obligatorio";
        } else if (txt_transvin.getValue().isEmpty()) {
            campo = "Vin";
            mensaje = "El campo 'VIN' es obligatorio";
        } /*else if (txt_transchasis.getValue().isEmpty()) {
         campo = "Chasis";
         mensaje = "El campo 'CHASIS' es obligatorio";
         } else if (txt_transmotor.getValue().isEmpty()) {
         campo = "Motor";
         mensaje = "El campo 'MOTOR' es obligatorio";
         } else if (txt_aniofab.getValue() == null) {
         campo = "Anio de Fabricación";
         mensaje = "El campo 'ANIO FABRICA' es obligatorio";
         } else if (txt_aniomod.getValue() == null) {
         campo = "Anio de Modelo";
         mensaje = "El campo 'ANIO DE MODELO' es obligatorio";
         } else if (txt_transversion.getValue().isEmpty()) {
         campo = "Version";
         mensaje = "El campo 'VERSION' es obligatorio";
         } else if (txt_transejes.getValue() == null) {
         campo = "Ejes";
         mensaje = "El campo 'EJES' es obligatorio";
         } else if (txt_transasientos.getValue() == null) {
         campo = "Asientos";
         mensaje = "El campo 'ASIENTOS' es obligatorio";
         } else if (txt_transpasajeros.getValue() == null) {
         campo = "Pasajeros";
         mensaje = "El campo 'PASAJEROS' es obligatorio";
         } else if (txt_transruedas.getValue() == null) {
         campo = "Ruedas";
         mensaje = "El campo 'RUEDAS' es obligatorio";
         } else if (txt_transpotencia.getValue().isEmpty()) {
         campo = "Potencia";
         mensaje = "El campo 'POTENCIA' es obligatorio";
         } else if (txt_transcilindros.getValue() == null) {
         campo = "Cilindros";
         mensaje = "El campo 'CILINDROS' es obligatorio";
         } else if (txt_transcilindradas.getValue() == null) {
         campo = "Cilindradas";
         mensaje = "El campo 'CILINDRADAS' es obligatorio";
         } else if (txt_transtraccion.getValue().isEmpty()) {
         campo = "Traccion";
         mensaje = "El campo 'TRACCION' es obligatorio";
         }*/ else {
            if (txt_transpesob.getValue() == null) {
                campo = "Peso Bruto";
                mensaje = "El campo 'PESO BRUTO' es obligatorio";
            } else if (txt_transpeson.getValue() == null) {
                campo = "Peso Neto";
                mensaje = "El campo 'PESO NETO' es obligatorio";
            } else if (txt_transcarga.getValue() == null) {
                campo = "Carga Util";
                mensaje = "El campo 'CARGA UTIL' es obligatorio";
            } else if (txt_translargo.getValue() == null) {
                campo = "Largo";
                mensaje = "El campo 'LARGO' es obligatorio";
            } else if (txt_transaltura.getValue() == null) {
                campo = "Altura";
                mensaje = "El campo 'ALTURA' es obligatorio";
            } else if (txt_transancho.getValue() == null) {
                campo = "Ancho";
                mensaje = "El campo 'ANCHO' es obligatorio";
            } else if (txt_transimg.getValue().isEmpty()) {
                campo = "Imagen";
                mensaje = "El campo 'IMAGEN' es obligatorio";
            } else if (txt_transfecing.getValue() == null) {
                campo = "Fecha Ingreso";
                mensaje = "El campo 'FECHA INGRESO' es obligatorio";
            } else {
                mensaje = "";
            }
        }
        return mensaje;
    }

    public void validafocos() {
        if (!tab_mantenimiento.isSelected()) {
            tab_mantenimiento.setSelected(true);
        }
        if (campo.equals("Alias")) {
            txt_transalias.focus();
        } else if (campo.equals("Placa")) {
            txt_transplaca.focus();
        } else if (campo.equals("Marca")) {
            cb_transmarca.focus();
        } else if (campo.equals("Combustible")) {
            cb_transcombustible.focus();
        } else if (campo.equals("Categoria")) {
            cb_transcategoria.focus();
        } else if (campo.equals("Modelo")) {
            cb_transmodelo.focus();
        } else if (campo.equals("Color")) {
            cb_transcolor.focus();
        } else if (campo.equals("Transmision")) {
            cb_transtransmision.focus();
        } else if (campo.equals("Carroceria")) {
            cb_transcarroceria.focus();
        } else if (campo.equals("Vin")) {
            txt_transvin.focus();
        } /*else if (campo.equals("Chasis")) {
         txt_transchasis.focus();
         } else if (campo.equals("Motor")) {
         txt_transmotor.focus();
         } else if (campo.equals("Anio de Fabricación")) {
         txt_aniofab.focus();
         } else if (campo.equals("Anio de Modelo")) {
         txt_aniomod.focus();
         } else if (campo.equals("Version")) {
         txt_transversion.focus();
         } else if (campo.equals("Ejes")) {
         txt_transejes.focus();
         } else if (campo.equals("Asientos")) {
         txt_transasientos.focus();
         } else if (campo.equals("Pasajeros")) {
         txt_transpasajeros.focus();
         } else if (campo.equals("Ruedas")) {
         txt_transruedas.focus();
         } else if (campo.equals("Potencia")) {
         txt_transpotencia.focus();
         } else if (campo.equals("Cilindros")) {
         txt_transcilindros.focus();
         } else if (campo.equals("Cilindradas")) {
         txt_transcilindradas.focus();
         } else if (campo.equals("Traccion")) {
         txt_transtraccion.focus();
         }*/ else {
            if (!tab_medidas.isSelected()) {
                tab_medidas.setSelected(true);
            }
            if (campo.equals("Peso Bruto")) {
                txt_transpesob.focus();
            } else if (campo.equals("Peso Neto")) {
                txt_transpeson.focus();
            } else if (campo.equals("Carga Util")) {
                txt_transcarga.focus();
            } else if (campo.equals("Largo")) {
                txt_translargo.focus();
            } else if (campo.equals("Altura")) {
                txt_transaltura.focus();
            } else if (campo.equals("Ancho")) {
                txt_transancho.focus();
            } else if (campo.equals("Imagen")) {
                txt_transimg.focus();
            } else if (campo.equals("Fecha Ingreso")) {
                txt_transfecing.focus();
            }
        }
    }

    public void LimpiarLista() {
        //limpio mi lista
        objlstVehiculo = null;
        objlstVehiculo = new ListModelList<Vehiculo>();
        lst_vehiculo.setModel(objlstVehiculo);
    }

    public void limpiarCamposHistorico() {
        //limpiar
        txt_prop_id.setValue(null);
        cb_prop.setSelectedIndex(-1);
        //inhabilitar
        txt_prop_id.setDisabled(true);
        cb_prop.setDisabled(true);
        chk_prop_est.setDisabled(true);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void limpiarCamposSoat() {
        //limpiar
        txt_soat_id.setValue(null);
        txt_soat_fecini.setValue(null);
        txt_soat_fecfin.setValue(null);
        txt_soat_nro.setValue(null);
        cb_seguro.setSelectedIndex(-1);
        //inhabilitar
        txt_soat_id.setDisabled(true);
        txt_soat_fecini.setDisabled(true);
        txt_soat_fecfin.setDisabled(true);
        txt_soat_nro.setDisabled(true);
        cb_seguro.setDisabled(true);
        chk_soat_est.setDisabled(true);
    }

    public void habilitaCampos(boolean b_valida) {
        //mantenimiento
        chk_transestado.setDisabled(b_valida);
        txt_transplaca.setDisabled(b_valida);
        txt_transalias.setDisabled(b_valida);
        cb_transmarca.setDisabled(b_valida);
        cb_transcarroceria.setDisabled(b_valida);
        cb_transcategoria.setDisabled(b_valida);
        cb_transcolor.setDisabled(b_valida);
        cb_transcombustible.setDisabled(b_valida);
        cb_transmodelo.setDisabled(b_valida);
        cb_transtransmision.setDisabled(b_valida);
        txt_transvin.setDisabled(b_valida);
        txt_transchasis.setDisabled(b_valida);
        txt_transmotor.setDisabled(b_valida);
        txt_aniofab.setDisabled(b_valida);
        txt_aniomod.setDisabled(b_valida);
        txt_transversion.setDisabled(b_valida);
        txt_transejes.setDisabled(b_valida);
        txt_transasientos.setDisabled(b_valida);
        txt_transpasajeros.setDisabled(b_valida);
        txt_transruedas.setDisabled(b_valida);
        txt_transpotencia.setDisabled(b_valida);
        txt_transcilindros.setDisabled(b_valida);
        txt_transcilindradas.setDisabled(b_valida);
        txt_transtraccion.setDisabled(b_valida);
        tbbtn_btn_imagenpro.setDisabled(b_valida);
        //otros
        txt_transpesob.setDisabled(b_valida);
        txt_transpeson.setDisabled(b_valida);
        txt_transcarga.setDisabled(b_valida);
        txt_translargo.setDisabled(b_valida);
        txt_transaltura.setDisabled(b_valida);
        txt_transancho.setDisabled(b_valida);
        txt_transfecing.setDisabled(b_valida);
        txt_transfecsal.setDisabled(b_valida);
        sp_transord.setDisabled(b_valida);
        txt_transnomrep.setDisabled(b_valida);
    }

    public void habilitaCamposSoat(boolean b_valida) {
        //mantenimiento
        chk_soat_est.setDisabled(b_valida);
        cb_seguro.setDisabled(b_valida);
        txt_soat_nro.setDisabled(b_valida);
        txt_soat_fecini.setDisabled(b_valida);
        txt_soat_fecfin.setDisabled(b_valida);
    }

    public void habilitaSubmantenimiento(boolean b_valida1) {
        //propietarios
        //Inactivar Nuevo, editar , eliminar
        tbbtn_btn_nuevop.setDisabled(b_valida1);
        tbbtn_btn_editarp.setDisabled(b_valida1);
        tbbtn_btn_eliminarp.setDisabled(b_valida1);
        //soat
        //Inactivar Nuevo, editar , eliminar
        tbbtn_btn_nuevos.setDisabled(b_valida1);
        tbbtn_btn_editars.setDisabled(b_valida1);
        tbbtn_btn_eliminars.setDisabled(b_valida1);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listavehiculo.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
        tab_medidas.setDisabled(b_valida2);
        tab_propietariosoat.setDisabled(b_valida2);

    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listavehiculo.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void habilitaBotonesHistprop(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevop.setDisabled(b_valida1);
        tbbtn_btn_editarp.setDisabled(b_valida1);
        tbbtn_btn_eliminarp.setDisabled(b_valida1);
        tbbtn_btn_deshacerp.setDisabled(b_valida2);
        tbbtn_btn_guardarp.setDisabled(b_valida2);
    }

    public void habilitaBotonesSoat(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevos.setDisabled(b_valida1);
        tbbtn_btn_editars.setDisabled(b_valida1);
        tbbtn_btn_eliminars.setDisabled(b_valida1);
        tbbtn_btn_deshacers.setDisabled(b_valida2);
        tbbtn_btn_guardars.setDisabled(b_valida2);
    }

    //metodos sin utilizar
    public void OnChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void llenarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean existePropietario(int cod_propietario) {
        boolean existe = false;
        for (int i = 0; i < objlstHistorico.size(); i++) {
            if (objlstHistorico.get(i).getProp_id() == cod_propietario) {
                existe = true;
            }
        }
        return existe;
    }

    public boolean existeSoat(int cod_soat) {
        boolean existe = false;
        for (int i = 0; i < objlstSoat.size(); i++) {
            if (objlstSoat.get(i).getSoat_id() == cod_soat) {
                existe = true;
            }
        }
        return existe;
    }

    public Object[][] getLisHisPropietario(ListModelList<HistoPropietario> lsthstpropietario) {

        ListModelList<HistoPropietario> objlstdepurado;
        if (s_estado.equals("M")) {
            objlstdepurado = new ListModelList<HistoPropietario>();
            for (int i = 0; i < lsthstpropietario.getSize(); i++) {
                if (!lsthstpropietario.get(i).getInd_accion().equals("Q")) {
                    objlstdepurado.add(lsthstpropietario.get(i));
                }
            }
        } else {
            objlstdepurado = lsthstpropietario;
        }

        Object[][] obj = new Object[objlstdepurado.getSize()][9];
        for (int i = 0; i < objlstdepurado.getSize(); i++) {
            obj[i][0] = objlstdepurado.get(i).getTrans_key();
            obj[i][1] = objlstdepurado.get(i).getSuc_id();
            obj[i][2] = objlstdepurado.get(i).getEmp_id();
            obj[i][3] = objlstdepurado.get(i).getProp_id();
            obj[i][4] = objlstdepurado.get(i).getHprop_id();
            obj[i][5] = objlstdepurado.get(i).getHprop_est();
            obj[i][6] = objlstdepurado.get(i).getHprop_usuadd();
            obj[i][7] = objlstdepurado.get(i).getHprop_usumod();
            obj[i][8] = objlstdepurado.get(i).getInd_accion();
        }
        return obj;
    }

    public Object[][] getLisSoat(ListModelList<Soat> lstsoat) throws ParseException {

        ListModelList<Soat> objlstdepurado;
        if (s_estado.equals("M")) {
            objlstdepurado = new ListModelList<Soat>();
            for (int i = 0; i < lstsoat.getSize(); i++) {
                if (!lstsoat.get(i).getInd_accion().equals("Q")) {
                    objlstdepurado.add(lstsoat.get(i));
                }
            }
        } else {
            objlstdepurado = lstsoat;
        }

        Object[][] obj = new Object[objlstdepurado.getSize()][13];
        for (int i = 0; i < objlstdepurado.getSize(); i++) {
            obj[i][0] = objlstdepurado.get(i).getTab_id();
            obj[i][1] = objlstdepurado.get(i).getTab_cod();
            obj[i][2] = objlstdepurado.get(i).getEmp_id();
            obj[i][3] = objlstdepurado.get(i).getSuc_id();
            obj[i][4] = objlstdepurado.get(i).getTrans_key();
            obj[i][5] = objlstdepurado.get(i).getSoat_id();
            obj[i][6] = objlstdepurado.get(i).getSoat_nro();
            obj[i][7] = new java.sql.Date(objlstdepurado.get(i).getSoat_fecini().getTime());
            obj[i][8] = new java.sql.Date(objlstdepurado.get(i).getSoat_fecfin().getTime());
            obj[i][9] = objlstdepurado.get(i).getSoat_est();
            obj[i][10] = objlstdepurado.get(i).getSoat_usuadd();
            obj[i][11] = objlstdepurado.get(i).getSoat_usumod();
            obj[i][12] = objlstdepurado.get(i).getInd_accion();
        }
        return obj;
    }
}
