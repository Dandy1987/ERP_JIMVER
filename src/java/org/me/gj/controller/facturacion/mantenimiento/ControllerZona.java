package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.mantenimiento.DaoUbigeo;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.cxc.mantenimiento.Ubigeo;
import org.me.gj.model.facturacion.mantenimiento.Pais;
import org.me.gj.model.facturacion.mantenimiento.Ruta;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.facturacion.mantenimiento.Zona;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class ControllerZona extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listazonas, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Textbox txt_zonid, txt_zondes, txt_rutid, txt_rutdes, txt_ubiid, txt_ubides, txt_paisid, txt_paisdes, txt_vendes, txt_zonnomrep, txt_busqueda, txt_venid, txt_usuadd, txt_usumod;
    @Wire
    Combobox cb_busest, cb_dvisita, cb_busqueda;
    @Wire
    Spinner sp_zonord;
    @Wire
    Checkbox chk_zonest, chk_busest;
    @Wire
    Listbox lst_zonas;
    @Wire
    Datebox d_fecadd, d_fecmod;
    //Instancias de Objetos
    DaoZonas objDaoZonas = new DaoZonas();
    ListModelList<Zona> objlstZonas = new ListModelList<Zona>();
    Zona objZona = new Zona();
    Accesos objAccesos = new Accesos();
    DaoAccesos objDaoAccesos = new DaoAccesos();
    //Variables publicas
    int i_sel;
    String modoEjecucion, s_estado = "Q", s_mensaje;
    String campo = "";
    public static boolean bandera = false;
    //Variables de Session
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerZona.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_zonas")
    public void llenaRegistros() throws SQLException {
        objlstZonas = objDaoZonas.listaZonas(1);
        lst_zonas.setModel(objlstZonas);
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();

        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(40108000, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Zona con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Zona con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nuevo Zona");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nuevo Zona");
        }
        /*if (objAccesos.getAcc_mod() == 1) {
         tbbtn_btn_editar.setDisabled(false);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de un canal");
         } else {
         tbbtn_btn_editar.setDisabled(true);
         LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de un canal");
         }*/
        if (objAccesos.getAcc_eli() == 1) {
            tbbtn_btn_eliminar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a eliminación de un Zona");
        } else {
            tbbtn_btn_eliminar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a eliminación de un Zona");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Zona");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Zona");
        }
    }

    @Listen("onClick=#tbbtn_btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_busest.getSelectedIndex() == 2) {
            i_estado = 3;
        } else if (cb_busest.getSelectedIndex() == 0) {
            i_estado = 1;
        } else {
            i_estado = 2;
        }
        objlstZonas = new ListModelList<Zona>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de Zona " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción de Zona " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo de Ruta" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del Vendedor" + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la nombre del vendedor " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 6) {
            i_seleccion = 6;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo del Ubigeo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 7) {
            i_seleccion = 7;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripcion del Ubigeo " + s_consulta + " para su busqueda");
        }
        objlstZonas = objDaoZonas.busquedaZonas(i_seleccion, s_consulta, i_estado);
        if (objlstZonas.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstZonas.getSize() + " registro(s)");
        }
        if (objlstZonas.getSize() > 0) {
            lst_zonas.setModel(objlstZonas);
        } else {
            limpiarLista();
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

    @Listen("onSelect=#lst_zonas")
    public void seleccionaRegistro() throws SQLException {
        objZona = (Zona) lst_zonas.getSelectedItem().getValue();
        if (objZona == null) {
            objZona = objlstZonas.get(i_sel + 1);
        }
        i_sel = lst_zonas.getSelectedIndex();
        limpiarCampos();
        llenarCampos();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objZona.getZon_id());
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objZona = new Zona();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        txt_zondes.focus();
        chk_zonest.setDisabled(true);
        s_estado = "N";
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        if (!s_valida.isEmpty()) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("Zona")) {
                            txt_zondes.focus();
                        } else if (campo.equals("Ruta")) {
                            txt_rutid.focus();
                        } else if (campo.equals("Ubigeo")) {
                            txt_ubiid.focus();
                        } else if (campo.equals("Pais")) {
                            txt_paisid.focus();
                        } else if (campo.equals("Visita")) {
                            cb_dvisita.select();
                        } else if (campo.equals("Vendedor")) {
                            txt_venid.focus();
                        } else if (campo.equals("Orden")) {
                            sp_zonord.focus();
                        }
                    }
                }
            });
        } else {
            if (!txt_paisid.getValue().matches("[0-9]*") || !txt_venid.getValue().matches("[0-9]*")
                    || !txt_ubiid.getValue().matches("[0-9]*") || !txt_rutid.getValue().matches("[0-9]*")) {
                lst_zonas.focus();
            } else {
                int pais_id = Integer.parseInt(txt_paisid.getValue());
                int vend_id = Integer.parseInt(txt_venid.getValue());
                Vendedor objVendedor = new DaoVendedores().buscarVendedor(vend_id);
                Pais objPais = new DaoPaises().busquedaPais(pais_id);
                Ruta objRuta = new DaoRutas().buscarRuta(txt_rutid.getValue());
                Ubigeo objUbigeo = new DaoUbigeo().buscarUbigeo(txt_ubiid.getValue());
                if (objUbigeo == null || objRuta == null || objPais == null || objVendedor == null) {
                    lst_zonas.focus();
                } else {
                    Zona objZon = new DaoZonas().buscaZonaxCodigo(txt_rutid.getValue().toString().concat(Utilitarios.lpad(String.valueOf(cb_dvisita.getSelectedIndex() + 1), 2, "0")));
                    if (objZon == null) {
                        s_mensaje = "Está seguro que desea guardar los cambios?";
                        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {

                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            objlstZonas = new ListModelList<Zona>();
                                            objZona = generaRegistro();
                                            ParametrosSalida objParamSalida;
                                            objParamSalida = objDaoZonas.insertarZona(objZona);
                                            if (objParamSalida.getFlagIndicador() == 0) {
                                                //validacion de guardar/nuevo
                                                VerificarTransacciones();
                                                tbbtn_btn_guardar.setDisabled(true);
                                                tbbtn_btn_deshacer.setDisabled(true);
                                                //
                                                habilitaTab(false, false);
                                                seleccionaTab(true, false);
                                                habilitaCampos(true);
                                                limpiarCampos();
                                                limpiaAuditoria();
                                                limpiarLista();
                                                objlstZonas = objDaoZonas.listaZonas(1);
                                                lst_zonas.setModel(objlstZonas);
                                                objZona = new Zona();
                                            }
                                            Messagebox.show(objParamSalida.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                        }
                                    }
                                });
                    } else {
                        Messagebox.show("Por favor ingrese otro día de visita", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    cb_dvisita.focus();
                                    cb_dvisita.select();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_zonas.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar esta zona?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParamSalida = objDaoZonas.eliminarZona(objZona);
                                if (objParamSalida.getFlagIndicador() == 0) {
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    limpiarLista();
                                    objlstZonas = objDaoZonas.listaZonas(1);
                                    lst_zonas.setModel(objlstZonas);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    lst_zonas.focus();
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
                            limpiaAuditoria();
                            OnChange();
                            limpiarLista();
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            tbbtn_btn_guardar.setDisabled(true);
                            tbbtn_btn_deshacer.setDisabled(true);
                            //
                            habilitaCampos(true);
                            lst_zonas.focus();
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                            busquedaRegistros();
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() {
        if (objlstZonas == null || objlstZonas.isEmpty()) {
            Messagebox.show("No hay registros para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            lst_zonas.focus();
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("empresa", objUsuCredential.getEmpresa());
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/facturacion/mantenimiento/LovImpresionZona.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    //Eventos Secundarios - Validacion
    @Listen("onCtrlKey=#w_manzon")
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
            objlstZonas = new ListModelList<Zona>();
            objlstZonas = objDaoZonas.listaZonas(0);
            lst_zonas.setModel(objlstZonas);
        }
    }

    @Listen("onOK=#txt_rutid")
    public void busquedaRutas() {
        if (bandera == false) {
            bandera = true;
            if (txt_rutid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "mantZonas";
                objMapObjetos.put("txt_rutid", txt_rutid);
                objMapObjetos.put("txt_rutdes", txt_rutdes);
                objMapObjetos.put("txt_ubiid", txt_ubiid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerZona");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovRutas.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_ubiid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_rutid")
    public void validaRutas() throws SQLException {
        if (!txt_rutid.getValue().isEmpty()) {
            txt_rutdes.setValue("");
            if (!txt_rutid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_rutid.setText("");
                            txt_rutdes.setText("");
                            txt_rutid.focus();
                        }
                    }
                });
            } else if (txt_rutid.getValue().length() < 6) {
                Messagebox.show("Por favor ingrese 6 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_rutid.setText("");
                            txt_rutdes.setText("");
                            txt_rutid.focus();
                        }
                    }
                });
            } else {
                String rut_id = txt_rutid.getValue();
                Ruta objRuta = new DaoRutas().buscarRuta(rut_id);
                if (objRuta == null) {
                    Messagebox.show("El código de ruta no existe o está eliminado", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_rutid.setText("");
                                txt_rutdes.setText("");
                                txt_rutid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la Ruta con codigo " + objRuta.getRut_id() + " y ha encontrado 1 registro(s)");
                    txt_rutid.setValue(objRuta.getRut_id());
                    txt_rutdes.setValue("Canal: " + objRuta.getRut_canaldes()
                            + " - Mesa: " + objRuta.getMes_id()
                            + " - Ruta: " + objRuta.getRut_corid());
                }
            }
        } else {
            txt_rutid.setText("");
            txt_rutdes.setText("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_ubiid")
    public void busquedaUbigeo() {
        if (bandera == false) {
            bandera = true;
            if (txt_ubiid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "mantZonas";
                objMapObjetos.put("txt_ubiid", txt_ubiid);
                objMapObjetos.put("txt_ubides", txt_ubides);
                objMapObjetos.put("txt_paisid", txt_paisid);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerZona");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovUbigeo.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_paisid.focus();
            }
        }
    }

    @Listen("onBlur=#txt_ubiid")
    public void validaUbigeo() throws SQLException {
        if (!txt_ubiid.getValue().isEmpty()) {
            txt_ubides.setValue("");
            if (!txt_ubiid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_ubiid.setText("");
                            txt_ubides.setText("");
                            txt_ubiid.focus();
                        }
                    }
                });

            } else if (txt_ubiid.getValue().length() < 6) {
                Messagebox.show("Por favor ingrese 6 dígitos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_ubiid.setText("");
                            txt_ubides.setText("");
                            txt_ubiid.focus();
                        }
                    }
                });
            } else {
                Ubigeo objUbigeo = new DaoUbigeo().buscarUbigeo(txt_ubiid.getValue());
                if (objUbigeo == null) {
                    Messagebox.show("El código de ubigeo no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_ubiid.setText("");
                                txt_ubides.setText("");
                                txt_ubiid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Ubigeo con codigo " + objUbigeo.getUbi_id() + " y ha encontrado 1 registro(s)");
                    txt_ubiid.setValue(objUbigeo.getUbi_cod());
                    txt_ubides.setValue(objUbigeo.getUbi_nomdep() + "-" + objUbigeo.getUbi_nompro() + "-" + objUbigeo.getUbi_nomdis());
                }
            }
        } else {
            txt_ubiid.setText("");
            txt_ubides.setText("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_paisid")
    public void busquedaPais() throws SQLException {
        if (bandera == false) {
            bandera = true;
            if (txt_paisid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "mantZonas";
                objMapObjetos.put("txt_paisid", txt_paisid);
                objMapObjetos.put("txt_paisdes", txt_paisdes);
                objMapObjetos.put("cb_dvisita", cb_dvisita);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerZona");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovPaises.zul", null, objMapObjetos);
                window.doModal();
            } else {
                cb_dvisita.focus();
            }
        }
    }

    @Listen("onBlur=#txt_paisid")
    public void validaPais() throws SQLException {
        if (!txt_paisid.getValue().isEmpty()) {
            txt_paisdes.setValue("");
            if (!txt_paisid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_paisid.setText("");
                            txt_paisdes.setText("");
                            txt_paisid.focus();
                        }
                    }
                });

            } else {
                int pais_id = Integer.parseInt(txt_paisid.getValue());
                Pais objPais = new DaoPaises().busquedaPais(pais_id);
                if (objPais == null) {
                    Messagebox.show("El código del país no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_paisid.setText("");
                                txt_paisdes.setText("");
                                txt_paisid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Pais con codigo " + objPais.getTab_subdir() + " y ha encontrado 1 registro(s)");
                    txt_paisid.setValue(Utilitarios.lpad(objPais.getTab_subdir(), 3, "0"));
                    txt_paisdes.setValue(objPais.getTab_subdes());
                }
            }
        } else {
            txt_paisid.setText("");
            txt_paisdes.setText("");
        }
        bandera = false;
    }

    @Listen("onOK=#txt_venid")
    public void busquedaVendedor() {
        if (bandera == false) {
            bandera = true;
            if (txt_venid.getValue().isEmpty()) {
                Map objMapObjetos = new HashMap();
                modoEjecucion = "mantZonas";
                objMapObjetos.put("txt_venid", txt_venid);
                objMapObjetos.put("txt_vendes", txt_vendes);
                objMapObjetos.put("txt_zonnomrep", txt_zonnomrep);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerZona");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovVendedores.zul", null, objMapObjetos);
                window.doModal();
            } else {
                txt_zonnomrep.focus();
            }
        }
    }

    @Listen("onBlur=#txt_venid")
    public void validaVendedor() throws SQLException {
        if (!txt_venid.getValue().isEmpty()) {
            txt_vendes.setValue("");
            if (!txt_venid.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_venid.setText("");
                            txt_vendes.setText("");
                            txt_venid.focus();
                        }
                    }
                });
            } else {
                int vend_id = Integer.parseInt(txt_venid.getValue());
                Vendedor objVendedor = new DaoVendedores().buscarVendedor(vend_id);
                if (objVendedor == null) {
                    Messagebox.show("El código de vendedor no existe", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_venid.setText("");
                                txt_vendes.setText("");
                                txt_venid.focus();
                            }
                        }
                    });
                } else {
                    LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos del Vendedor con codigo " + objVendedor.getVen_id() + " y ha encontrado 1 registro(s)");
                    txt_venid.setValue(Utilitarios.lpad(objVendedor.getVen_id(), 4, "0"));
                    txt_vendes.setValue(objVendedor.getVen_ape() + ", " + objVendedor.getVen_nom());
                }
            }
        } else {
            txt_venid.setText("");
            txt_vendes.setText("");
        }
        bandera = false;
    }

    @Listen("onOK=#cb_dvisita")
    public void busquedaDia() {
        if (cb_dvisita.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un día de la semana", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            txt_venid.setDisabled(false);
            txt_venid.focus();
        }
    }

    @Listen("onOK=#txt_zonnomrep")
    public void ingresaReporteZona() {
        sp_zonord.focus();
    }

    @Listen("onOK=#txt_zondes")
    public void ingresaRuta() {
        txt_rutid.focus();

    }

    @Listen("onOK=#txt_zondes")
    public void ingresarDescripcionZona() {
        if (txt_zondes.getValue().isEmpty()) {
            Messagebox.show("Por favor ingrese una descripción para la zona", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_zondes.focus();
                    }
                }
            });
        } else {
            txt_rutid.focus();
        }
    }

    //Eventos Otros
    public String verificar() {
        String verifica;
        if (txt_zondes.getValue().isEmpty()) {
            verifica = "El campo Descripcion es obligatorio";
            campo = "Zona";
        } else if (txt_zondes.getValue().matches("(\\s)+")) {
            verifica = "El campo Descripcion no debe tener espacios en blanco al principio";
            campo = "Zona";
            txt_zondes.setText("");
        } else if (txt_zondes.getValue().matches("([^\\w])+")) {
            verifica = "El campo Descripcion , no se permite caracteres especiales";
            campo = "Zona";
            txt_zondes.setText("");
        } else if (txt_rutid.getValue().isEmpty()) {
            verifica = "El campo Ruta es obligatorio";
            campo = "Ruta";
        } else if (txt_ubiid.getValue().isEmpty()) {
            verifica = "El campo Ubigeo es obligatorio";
            campo = "Ubigeo";
        } else if (txt_paisid.getValue().isEmpty()) {
            verifica = "El campo Pais es obligatorio";
            campo = "Pais";
        } else if (cb_dvisita.getSelectedIndex() == -1) {
            verifica = "Debe seleccionar Dia de Visita ";
            campo = "Visita";
        } else if (txt_venid.getValue().isEmpty()) {
            verifica = "El campo Vendedor es obligatorio";
            campo = "Vendedor";
        } else if (sp_zonord.getValue() == null) {
            verifica = "Ingrese Orden, por favor";
            campo = "Orden";
        } else {
            verifica = "";
        }
        return verifica;
    }

    public void validafocos() {
        if (campo.equals("Zona")) {
            txt_zondes.focus();
        }
    }

    public void OnChange() {
        cb_busest.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(-1);
        txt_busqueda.setValue("%%");
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listazonas.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void limpiarCampos() {
        txt_zonid.setValue("");
        txt_zondes.setValue("");
        txt_rutid.setValue("");
        txt_rutdes.setValue("");
        txt_ubiid.setValue("");
        txt_ubides.setValue("");
        txt_paisid.setValue("");
        txt_paisdes.setValue("");
        txt_vendes.setValue("");
        txt_zonnomrep.setValue("");
        txt_venid.setValue(null);
        cb_dvisita.setSelectedIndex(-1);
        sp_zonord.setValue(0);
    }

    public void limpiarLista() {
        //limpio mi lista
        objZona = null;
        objlstZonas = new ListModelList<Zona>();
        lst_zonas.setModel(objlstZonas);
    }

    public void limpiaAuditoria() {
        txt_usuadd.setValue("");
        txt_usumod.setValue("");
        d_fecadd.setValue(null);
        d_fecmod.setValue(null);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listazonas.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void llenarCampos() {
        chk_zonest.setChecked(objZona.isValor());
        txt_zonid.setValue(objZona.getZon_id());
        txt_zondes.setValue(objZona.getZon_des());
        txt_rutid.setValue(objZona.getZon_rutid());
        //txt_rutdes.setValue(objZona.getZon_des());

        txt_rutdes.setValue("Canal: " + objZona.getZon_candes() + " - Mesa: " + StringFns.substring(objZona.getZon_rutid(), 2, 4) + " - Ruta: " + StringFns.substring(objZona.getZon_rutid(), 4, 6));

        txt_ubiid.setValue(objZona.getZon_ubiid());
        txt_ubides.setValue(objZona.getZon_ubides());
        txt_paisid.setValue(String.valueOf(objZona.getZon_paisid()));
        txt_paisdes.setValue(objZona.getZon_paisdes());
        cb_dvisita.setSelectedIndex(objZona.getZon_dvis() - 1);
        txt_venid.setValue(objZona.getZon_idven());
        txt_vendes.setValue(objZona.getZon_apenom());
        txt_zonnomrep.setValue(objZona.getZon_nomrep());
        sp_zonord.setValue(objZona.getZon_ord());

        txt_usuadd.setValue(objZona.getZon_usuadd());
        d_fecadd.setValue(objZona.getZon_fecadd());
        txt_usumod.setValue(objZona.getZon_usumod());
        d_fecmod.setValue(objZona.getZon_fecmod());
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_zondes.setDisabled(b_valida1);
        txt_rutid.setDisabled(b_valida1);
        txt_ubiid.setDisabled(b_valida1);
        txt_paisid.setDisabled(b_valida1);
        txt_zonnomrep.setDisabled(b_valida1);
        txt_venid.setDisabled(b_valida1);
        cb_dvisita.setDisabled(b_valida1);
        chk_zonest.setDisabled(b_valida1);
        sp_zonord.setDisabled(b_valida1);
    }

    public Zona generaRegistro() {
        long zon_key = objZona.getZon_key();
        String zon_id = objZona.getZon_id();
        int suc_id = objUsuCredential.getCodsuc();
        int emp_id = objUsuCredential.getCodemp();
        String zon_des = txt_zondes.getValue().toUpperCase();
        int zon_est;
        if (chk_zonest.isChecked()) {
            zon_est = 1;
        } else {
            zon_est = 2;
        }
        String zon_idven = txt_venid.getValue();
        int zon_dvis = Integer.parseInt(cb_dvisita.getSelectedItem().getValue().toString());
        String zon_rutid = txt_rutid.getValue();
        int zon_ord = sp_zonord.getValue();
        int zon_canid = Integer.parseInt(txt_rutid.getValue().substring(0, 2));
        int zon_mesid = Integer.parseInt(txt_rutid.getValue().substring(2, 4));
        String zon_ubiid = txt_ubiid.getValue();
        String zon_paisid = txt_paisid.getValue();
        String zon_nomrep = txt_zonnomrep.getValue().toUpperCase();
        String zon_usuadd = objUsuCredential.getCuenta();
        String zon_usumod = objUsuCredential.getCuenta();
        return new Zona(zon_key, zon_id, suc_id, emp_id, zon_des, zon_est, zon_idven, zon_dvis,
                zon_rutid, zon_ord, zon_canid, zon_mesid, zon_ubiid, zon_paisid, zon_nomrep,
                zon_usuadd, zon_usumod);
    }

    //metodos sin utilizar        
    public void botonEditar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
