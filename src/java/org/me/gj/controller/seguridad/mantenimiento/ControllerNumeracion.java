package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoAlmacenes;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoRelNotaES;
import org.me.gj.controller.logistica.mantenimiento.DaoTipVen;
import org.me.gj.model.logistica.mantenimiento.Almacenes;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.RelGui;
import org.me.gj.model.logistica.mantenimiento.TipVen;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerNumeracion extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir;
    @Wire
    Listbox lst_numeracion;
    @Wire
    Intbox txt_correlativo, txt_numserie, txt_numid;
    @Wire
    Combobox cb_empresa, cb_sucursal, cb_tipo, cb_codigo, cb_tipguia, cb_cliprov, cb_salalm, cb_almori, cb_tipven, cb_almdes, cb_busqueda, cb_estado;
    @Wire
    Checkbox chk_estado, chk_comp, chk_cost, chk_busest;
    @Wire
    Textbox txt_descripcion, txt_busqueda, txt_nomrep;
    @Wire
    Tab tab_listanumeracion, tab_mantenimiento;
    @Wire
    Radio rb_numeracion, rb_serie;
    @Wire
    Cell c_ser, c_gui, c_tipgui, c_relgui, c_salalm, c_tipven, c_comp, c_cost, c_almori, c_almdes, c_nomrep;
    @Wire
    Button btn_buscar;
    //Instancias de Objetos
    ListModelList<Numeracion> objlstNumeracion = new ListModelList<Numeracion>();
    ListModelList<TipVen> objlstTipVen = new ListModelList<TipVen>();
    ListModelList<Almacenes> objlstAlmDesOri = new ListModelList<Almacenes>();
    ListModelList<Almacenes> objlstAlmDesDes = new ListModelList<Almacenes>();
    ListModelList<Guias> objlstGuias = new ListModelList<Guias>();
    ListModelList<RelGui> objlstRelgui = new ListModelList<RelGui>();
    ListModelList<Empresas> objlstEmpresas = new ListModelList<Empresas>();
    ListModelList<Sucursales> objlstSucursales = new ListModelList<Sucursales>();
    DaoNumeracion objDaoNumeracion = new DaoNumeracion();
    DaoTipVen objDaoTipVen = new DaoTipVen();
    DaoRelNotaES objDaoRelGui = new DaoRelNotaES();
    DaoEmpresas objDaoEmpresas = new DaoEmpresas();
    DaoSucursales objDaoSucursales = new DaoSucursales();
    DaoAlmacenes objDaoAlmacenes = new DaoAlmacenes();
    DaoManNotaES objDaoGuias = new DaoManNotaES();
    Numeracion objNumeracion = new Numeracion();
    //Variables publicas
    int i_sel;
    String s_estado = "Q", s_estadoNum = "Q", s_numMaster = "Q";
    String s_mensaje;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerNumeracion.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_numeracion")
    public void llenaRegistros() throws SQLException {
        objlstNumeracion = objDaoNumeracion.listaNumeracion();
        objlstEmpresas = objDaoEmpresas.lstEmpresas(1);
        objlstGuias = objDaoGuias.listaGuias(1);
        objlstRelgui = objDaoRelGui.listaRelacionGuia(1);
        objlstTipVen = objDaoTipVen.listaTipoVenta(1);
        lst_numeracion.setModel(objlstNumeracion);
        cb_empresa.setModel(objlstEmpresas);
        cb_codigo.setModel(objlstGuias);
        cb_cliprov.setModel(objlstRelgui);
        cb_tipven.setModel(objlstTipVen);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onClick=#btn_buscar")
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
        objlstNumeracion = new ListModelList<Numeracion>();
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
        objlstNumeracion = objDaoNumeracion.busquedaNumeracion(i_seleccion, s_consulta, i_estado);
        if (objlstNumeracion.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstNumeracion.getSize() + " registro(s)");
        }
        if (objlstNumeracion.getSize() > 0) {
            lst_numeracion.setModel(objlstNumeracion);
        } else {
            objlstNumeracion = null;
            lst_numeracion.setModel(objlstNumeracion);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        //limpiaAuditoria();
    }

    @Listen("onSelect=#lst_numeracion")
    public void seleccionaRegistro() throws SQLException {
        objNumeracion = lst_numeracion.getSelectedItem().getValue();
        if (objNumeracion == null) {
            objNumeracion = objlstNumeracion.get(i_sel + 1);
        }
        i_sel = lst_numeracion.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objNumeracion.getEmp_id());
        limpiarCampos();
        camposVisibles(objNumeracion.getNum_tip().equals("R"));
        s_estadoNum = objNumeracion.getNum_tip().equals("R") ? "S" : "T";
        habilitaCampos(true);
        if (objNumeracion.getNum_tip().equals("R")) {
        }
        llenarCampos();
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

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        objNumeracion = new Numeracion();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_estado.setDisabled(true);
        s_estado = "N";
        s_estadoNum = "T";
        s_mensaje = "Desea Crear una Numeracion Master?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            s_numMaster = "S";
                            cb_empresa.setSelectedIndex(-1);
                            cb_sucursal.setSelectedIndex(-1);
                            cb_empresa.setDisabled(true);
                            cb_sucursal.setDisabled(true);
                            rb_numeracion.setDisabled(true);
                            rb_serie.setDisabled(true);
                        }
                    }
                });
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_numeracion.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            rb_numeracion.setDisabled(true);
            rb_serie.setDisabled(true);
            cb_empresa.setDisabled(true);
            cb_sucursal.setDisabled(true);
            s_numMaster = "S";
            cb_tipo.setDisabled(objNumeracion.getNum_tip().equals("R"));
            if (objNumeracion.getEmp_id() != 0 && objNumeracion.getSuc_id() != 0 && !objNumeracion.getNum_tip().equals("R")) {
                s_mensaje = "Desea Crear otra numeracion por empresa y Sucursal para este registro?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    s_numMaster = "N";
                                    s_estado = "N";
                                    txt_numid.setValue(objNumeracion.getNum_id());
                                    cb_empresa.setDisabled(false);
                                    cb_sucursal.setDisabled(false);
                                    cb_tipo.setDisabled(false);
                                    chk_estado.setDisabled(false);
                                }
                            }
                        });
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                if (s_estadoNum.equals("T")) {
                                    objNumeracion = (Numeracion) generaRegistroNumeracionTabla();
                                } else {
                                    objNumeracion = (Numeracion) generaRegistroNumeracionSerie();
                                }
                                if (s_estado.equals("N")) {
                                    if (s_estadoNum.equals("T")) {
                                        objParamSalida = objDaoNumeracion.insertarNumeracionTabla(objNumeracion);
                                    } else {
                                        objParamSalida = objDaoNumeracion.insertarNumeracionSerie(objNumeracion);
                                    }
                                } else {
                                    if (s_estadoNum.equals("T")) {
                                        objParamSalida = objDaoNumeracion.actualizarNumeracionTabla(objNumeracion);
                                    } else {
                                        objParamSalida = objDaoNumeracion.actualizarNumeracionSerie(objNumeracion);
                                    }
                                }
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    habilitaBotones(false, true);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstNumeracion.clear();
                                    objlstNumeracion = objDaoNumeracion.listaNumeracion();
                                    lst_numeracion.setModel(objlstNumeracion);
                                    objNumeracion = new Numeracion();
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_numeracion.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar este registro de empresa?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida;
                                objParamSalida = objDaoNumeracion.eliminarNumeracion(objNumeracion);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    habilitaBotones(false, true);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    objlstNumeracion.clear();
                                    objlstNumeracion = objDaoNumeracion.listaNumeracion();
                                    lst_numeracion.setModel(objlstNumeracion);
                                }
                                Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
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
                            lst_numeracion.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            limpiarCampos();
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onSelect=#cb_empresa")
    public void onSelectCbEmpresas() throws SQLException {
        String emp_id = String.valueOf(cb_empresa.getSelectedItem().getValue());
        objlstSucursales.clear();
        objlstSucursales = objDaoSucursales.lstSucursales(emp_id, 1);
        cb_sucursal.setModel(objlstSucursales);
    }

    @Listen("onSelect=#cb_sucursal")
    public void onSelectCbSucursal() throws SQLException {
        objlstAlmDesOri.clear();
        objlstAlmDesDes.clear();
        int emp_id = cb_empresa.getSelectedItem().getValue();
        int suc_id = cb_sucursal.getSelectedItem().getValue();
        objlstAlmDesOri = objDaoAlmacenes.listaDescAlmacenOri(emp_id, suc_id);
        objlstAlmDesDes = objDaoAlmacenes.listaDescAlmacenDes(emp_id, suc_id);
        cb_almori.setModel(objlstAlmDesOri);
        cb_almdes.setModel(objlstAlmDesDes);
    }

    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstNumeracion.clear();
            objlstNumeracion = objDaoNumeracion.listaNumeracion();
            lst_numeracion.setModel(objlstNumeracion);
        }
    }

    @Listen("onCheck=#rb_grupo")
    public void escogerOpcion() {
        if (rb_numeracion.isChecked()) {
            s_estadoNum = "T";
            camposVisibles(false);
            habilitaCampos(false);
            cb_tipo.setDisabled(false);
        } else {
            s_estadoNum = "S";
            camposVisibles(true);
            habilitaCampos(false);
            cb_tipo.setSelectedIndex(-1);
            cb_tipo.setDisabled(true);
        }
    }

    //Eventos Otros   
    public void llenarCampos() {
        Utilitarios objUtilitario = new Utilitarios();
        rb_numeracion.setChecked(!objNumeracion.getNum_tip().equals("R"));
        rb_serie.setChecked(objNumeracion.getNum_tip().equals("R"));
        txt_numid.setValue(objNumeracion.getNum_id());
        if (objNumeracion.getEmp_id() == 0) {
            cb_empresa.setSelectedIndex(-1);
        } else {
            cb_empresa.setSelectedItem(Utilitarios.valorPorTexto(cb_empresa, objNumeracion.getEmp_id()));
        }
        objlstSucursales.clear();
        cb_sucursal.setValue("");
        if (objNumeracion.getSuc_id() != 0) {
            try {
                objlstSucursales = objDaoSucursales.lstSucursales(String.valueOf(objNumeracion.getEmp_id()), 1);
            } catch (SQLException ex) {
            }
            cb_sucursal.setModel(objlstSucursales);
            cb_sucursal.setValue(extraeSucursal(objNumeracion.getSuc_id()));
        }
        txt_descripcion.setValue(objNumeracion.getNum_tip().equals("R") ? objNumeracion.getNum_des() : objNumeracion.getNum_des().substring(17));
        chk_estado.setChecked(objNumeracion.isValor());
        chk_estado.setLabel(objNumeracion.isValor() ? "ACTIVO" : "INACTIVO");
        cb_tipo.setSelectedItem(objUtilitario.textoPorTexto(cb_tipo, objNumeracion.getNum_tip()));
        txt_correlativo.setValue(objNumeracion.getNum_cor());
        //SOLO SE CUMPLIRA CUANDO EL REGISTRO SEA UMA SERIE
        if (objNumeracion.getNum_tip().equals("R")) {
            cb_tipo.setSelectedIndex(-1);
            objlstAlmDesOri.clear();
            objlstAlmDesDes.clear();
            int emp_id = objNumeracion.getEmp_id();
            int suc_id = objNumeracion.getSuc_id();
            try {
                objlstAlmDesOri = objDaoAlmacenes.listaDescAlmacenOri(emp_id, suc_id);
                objlstAlmDesDes = objDaoAlmacenes.listaDescAlmacenDes(emp_id, suc_id);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(ControllerNumeracion.class.getName()).log(Level.SEVERE, null, ex);
            }
            cb_almori.setModel(objlstAlmDesOri);
            cb_almdes.setModel(objlstAlmDesOri);
            rb_serie.setChecked(true);
            txt_numserie.setValue(Integer.parseInt(objNumeracion.getNum_ser()));
            cb_codigo.setSelectedItem(objUtilitario.valorPorTexto(cb_codigo, objNumeracion.getNum_gui()));
            cb_tipguia.setSelectedItem(objUtilitario.textoPorTexto(cb_tipguia, objNumeracion.getNum_tip()));
            cb_cliprov.setSelectedItem(objUtilitario.valorPorTexto(cb_cliprov, objNumeracion.getNum_clipro()));
            cb_salalm.setSelectedItem(objUtilitario.textoPorTexto(cb_salalm, objNumeracion.getNum_salalm() == null ? "" : objNumeracion.getNum_salalm()));
            cb_tipven.setSelectedItem(objUtilitario.valorPorTexto(cb_tipven, objNumeracion.getNum_fac()));
            chk_comp.setChecked(objNumeracion.getNum_com().equals("S"));
            chk_cost.setChecked(objNumeracion.getNum_cos().equals("S"));
            cb_almori.setValue(extraeNombreAlmacen(objNumeracion.getNum_almori(), objlstAlmDesOri));
            cb_almdes.setValue(extraeNombreAlmacen(objNumeracion.getNum_almdes(), objlstAlmDesDes));
            txt_nomrep.setValue(objNumeracion.getNum_nomrep());
        }
    }

    public String extraeNombreAlmacen(int alm_id, ListModelList<Almacenes> objlstAlmacen) {
        int i = 0;
        boolean verifica = true;
        String alm = "";
        while (i < objlstAlmacen.getSize() && verifica) {
            if (Integer.parseInt(objlstAlmacen.get(i).getAlm_key()) == alm_id) { // Corregir
                alm = objlstAlmacen.get(i).getAlm_des();
                verifica = false;
            }
            i++;
        }
        return alm;
    }

    public String extraeSucursal(int sucid) {
        int i = 0;
        boolean verifica = true;
        String sucDes = "";
        while (i < objlstSucursales.getSize() && verifica) {
            if (objlstSucursales.get(i).getSuc_id() == sucid) {
                sucDes = objlstSucursales.get(i).getSuc_des();
                verifica = false;
            }
            i++;
        }
        return sucDes;
    }

    public String verificar() {
        String valida;
        if (cb_empresa.getSelectedIndex() == -1 && !s_numMaster.equals("S")) {
            valida = "Empresa";
            cb_empresa.focus();
        } else if (cb_sucursal.getSelectedIndex() == -1 && !s_numMaster.equals("S")) {
            valida = "Sucursal";
            cb_sucursal.focus();
        } else if (txt_descripcion.getValue().isEmpty()) {
            valida = "Descripcion";
            txt_descripcion.focus();
        } else if (cb_tipo.getSelectedIndex() == -1 && !s_estadoNum.equals("S")) {
            valida = "Tipo";
            cb_tipo.focus();
        } else if (txt_correlativo.getValue() == null) {
            valida = "Correlativo";
            txt_correlativo.focus();
        } else if (rb_serie.isChecked()) {
            //VALIDACION DE DATOS PARA LOS REGISTROS DEL TIPO SERIE
            if (txt_numserie.getValue() == null) {
                valida = "Serie";
                txt_numserie.focus();
            } else if (cb_codigo.getSelectedIndex() == -1) {
                valida = "Guia";
                cb_codigo.focus();
            } else if (cb_tipguia.getSelectedIndex() == -1) {
                valida = "Tipo Guia";
                cb_tipguia.focus();
            } else if (cb_cliprov.getSelectedIndex() == -1) {
                valida = "Relacion Guia";
                cb_cliprov.focus();
            } else if (cb_salalm.getSelectedIndex() == -1) {
                valida = "Salida Almacen";
                cb_salalm.focus();
            } else if (cb_tipven.getSelectedIndex() == -1) {
                valida = "Tipo Venta";
                cb_tipven.focus();
            } else if (cb_almori.getSelectedIndex() == -1) {
                valida = "Almacen Origen";
                cb_almori.focus();
            } else if (cb_almdes.getSelectedIndex() == -1) {
                valida = "Almacen Destino";
                cb_almdes.focus();
            } else if (txt_nomrep.getValue().isEmpty()) {
                valida = "Nombre Reporte";
                txt_nomrep.focus();
            } else {
                valida = "";
            }
        } else {
            valida = "";
        }
        return valida;
    }

    public Object generaRegistroNumeracionSerie() {
        int emp_id = cb_empresa.getSelectedItem().getValue();
        int suc_id = cb_sucursal.getSelectedItem().getValue();
        int num_id = txt_numid.getValue() == null ? 0 : txt_numid.getValue();
        String num_tip = cb_tipo.getSelectedIndex() == -1 ? "R" : cb_tipo.getSelectedItem().getValue().toString();
        int num_est = chk_estado.isChecked() ? 1 : 2;
        String num_des = txt_descripcion.getValue().toUpperCase();
        int num_cor = txt_correlativo.getValue();
        String num_ser = txt_numserie.getValue() == null ? "" : String.valueOf(txt_numserie.getValue());
        int num_gui = cb_codigo.getSelectedItem().getValue();
        //VACIO DONDE VA NUM_VEN
        String num_cos = chk_cost.isChecked() ? "S" : "N";
        int num_fac = cb_tipven.getSelectedItem().getValue();
        int num_almori = cb_almori.getSelectedItem().getValue();
        int num_almdes = cb_almdes.getSelectedItem().getValue();
        int num_clipro = cb_cliprov.getSelectedItem().getValue();
        String num_salalm = cb_salalm.getSelectedItem().getValue();
        String num_com = chk_comp.isChecked() ? "S" : "N";
        String num_nomrep = txt_nomrep.getValue().toUpperCase();
        String num_usuadd = objUsuCredential.getCuenta();
        String num_usumod = objUsuCredential.getCuenta();
        return new Numeracion(emp_id, suc_id, num_id, num_tip, num_est, num_des, num_cor, num_ser,
                num_gui, null, num_cos, num_fac, num_almori, num_almdes, num_clipro,
                num_salalm, num_com, num_nomrep, num_usuadd, num_usumod);
    }

    public Object generaRegistroNumeracionTabla() {
        int emp_id = cb_empresa.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_empresa.getSelectedItem().getValue().toString());
        int suc_id = cb_sucursal.getSelectedIndex() == -1 ? 0 : Integer.parseInt(cb_sucursal.getSelectedItem().getValue().toString());
        int num_id = txt_numid.getValue() == null ? 0 : txt_numid.getValue();
        String num_tip = cb_tipo.getSelectedItem().getValue();
        int num_est = chk_estado.isChecked() ? 1 : 2;
        String num_des = txt_descripcion.getValue().toUpperCase();
        int num_cor = txt_correlativo.getValue();
        String num_usuadd = objUsuCredential.getCuenta();
        String num_usumod = objUsuCredential.getCuenta();
        return new Numeracion(emp_id, suc_id, num_id, num_tip, num_est, num_des, num_cor, num_usuadd, num_usumod);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanumeracion.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listanumeracion.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        rb_numeracion.setChecked(true);
        rb_serie.setChecked(false);
        camposVisibles(false);
        txt_numid.setValue(null);
        cb_empresa.setSelectedIndex(-1);
        cb_sucursal.setSelectedIndex(-1);
        cb_sucursal.setModel(null);
        txt_descripcion.setValue("");
        chk_estado.setLabel("ACTIVO");
        chk_estado.setChecked(true);
        cb_tipo.setSelectedIndex(-1);
        txt_correlativo.setValue(null);
        //SOLO SE CUMPLIRA CUANDO EL REGISTRO SEA UMA SERIE
        if (rb_serie.isChecked() || (!rb_numeracion.isChecked() && !rb_serie.isChecked())) {
            txt_numserie.setValue(null);
            cb_codigo.setSelectedIndex(-1);
            cb_tipguia.setSelectedIndex(-1);
            cb_cliprov.setSelectedIndex(-1);
            cb_salalm.setSelectedIndex(-1);
            cb_tipven.setSelectedIndex(-1);
            chk_comp.setChecked(true);
            chk_comp.setLabel("SI");
            chk_cost.setChecked(true);
            chk_cost.setLabel("SI");
            cb_almori.setSelectedIndex(-1);
            cb_almdes.setSelectedIndex(-1);
            txt_nomrep.setValue("");
        }
        s_estado = "Q";
        s_numMaster = "Q";
        s_estadoNum = "Q";
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstNumeracion = null;
        objlstNumeracion = new ListModelList<Numeracion>();
        lst_numeracion.setModel(objlstNumeracion);
    }

    public void habilitaCampos(boolean b_valida) {
        rb_numeracion.setDisabled(b_valida);
        rb_serie.setDisabled(b_valida);
        cb_empresa.setDisabled(b_valida);
        cb_sucursal.setDisabled(b_valida);
        txt_descripcion.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
        chk_estado.setDisabled(b_valida);
        cb_tipo.setDisabled(b_valida);
        txt_correlativo.setDisabled(b_valida);
        //SOLO SE CUMPLIRA CUANDO EL REGISTRO SEA UMA SERIE
        if (rb_serie.isChecked() || (!rb_numeracion.isChecked() && !rb_serie.isChecked())) {
            txt_numserie.setDisabled(b_valida);
            cb_codigo.setDisabled(b_valida);
            cb_tipguia.setDisabled(b_valida);
            cb_cliprov.setDisabled(b_valida);
            cb_salalm.setDisabled(b_valida);
            cb_tipven.setDisabled(b_valida);
            chk_comp.setDisabled(b_valida);
            chk_cost.setDisabled(b_valida);
            cb_almori.setDisabled(b_valida);
            cb_almdes.setDisabled(b_valida);
            txt_nomrep.setDisabled(b_valida);
        }
    }

    public void camposVisibles(boolean b_valida) {
        txt_numserie.setVisible(b_valida);
        cb_codigo.setVisible(b_valida);
        cb_tipguia.setVisible(b_valida);
        cb_cliprov.setVisible(b_valida);
        cb_salalm.setVisible(b_valida);
        cb_tipven.setVisible(b_valida);
        chk_comp.setVisible(b_valida);
        chk_cost.setVisible(b_valida);
        cb_almori.setVisible(b_valida);
        cb_almdes.setVisible(b_valida);
        txt_nomrep.setVisible(b_valida);
        c_ser.setVisible(b_valida);
        c_gui.setVisible(b_valida);
        c_tipgui.setVisible(b_valida);
        c_relgui.setVisible(b_valida);
        c_salalm.setVisible(b_valida);
        c_tipven.setVisible(b_valida);
        c_comp.setVisible(b_valida);
        c_cost.setVisible(b_valida);
        c_almori.setVisible(b_valida);
        c_almdes.setVisible(b_valida);
        c_nomrep.setVisible(b_valida);
    }
}
