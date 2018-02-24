/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManTablas;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerManTabla extends SelectorComposer<Component> {

    @Wire
    Tab tab_lista, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer, tbbtn_btn_imprimir;
    @Wire
    Combobox cb_estado, cb_busqueda, cb_periodo, cb_periodo2, cb_tipo;
    @Wire
    Datebox d_fec, d_fecmo;
    @Wire
    Checkbox chk_almest;
    @Wire
    Textbox txt_descripcion, txt_usua, txt_usumo, txt_busqueda, txt_codigo;
    @Wire
    Listbox lst_lista;
    @Wire
    Doublebox d_enero, d_febrero, d_marzo, d_abril, d_mayo, d_junio, d_julio, d_agosto,
            d_septiembre, d_octubre, d_noviembre, d_diciembre, d_default;
    ListModelList lst_ManPeriodos;
    SimpleDateFormat sdfm = new SimpleDateFormat("yyyy");
    Date fecha = new Date();
    ListModelList<ManTablas> objlstTablas = new ListModelList<ManTablas>();
    ListModelList<ManTablas> objlstTablas2 = new ListModelList<ManTablas>();
    DaoManTablas objDaoTablas;
    ListModelList<ManTablas> objlstEliminraTablas;
    ManTablas objManTablas = new ManTablas();
    DaoAccesos objDaoAccesos;
    Accesos objAccesos;
    

    String s_estado, campo, s_mensaje;
    int i_sel, valor;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerManTabla.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        lst_ManPeriodos = new ListModelList();
        lst_ManPeriodos = (new DaoManTablas()).listaPeriodos();
        String periodo = sdfm.format(fecha);
        cb_periodo.setModel(lst_ManPeriodos);
        cb_periodo.setValue(periodo);
        objDaoAccesos = new DaoAccesos();
        objDaoTablas = new DaoManTablas();
        objlstTablas = objDaoTablas.lstTablas(cb_periodo.getValue());
        lst_lista.setModel(objlstTablas);
        objlstEliminraTablas = new ListModelList<ManTablas>();

    }

    @Listen("onClick=#btn_buscar")
    public void buscar() throws SQLException {
        String consulta = txt_busqueda.getValue().toUpperCase();
        int selec = 0;
        int est;
        if (cb_estado.getSelectedIndex() == 2 || cb_estado.getSelectedIndex() == -1) {
            est = 3;
        } else if (cb_estado.getSelectedIndex() == 0) {
            est = 1;
        } else {
            est = 2;
        }

        if (cb_busqueda.getSelectedIndex() == 0) {
            selec = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            selec = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            selec = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + consulta + " para su busqueda");
        }
        objlstTablas = objDaoTablas.consultar(selec, consulta, est, cb_periodo.getValue());
        if (objlstTablas.getSize() > 0) {
            lst_lista.setModel(objlstTablas);
        } else {
            objlstTablas = null;
            lst_lista.setModel(objlstTablas);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiaAuditoria();
        limpiarCampos();

    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90101060, usuario, empresa, sucursal);
        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado al mantenimiento de Tablas con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado al Mantenimiento de Tablas con el rol: USUARIO NORMAL");
        }
        if (objAccesos.getAcc_ins() == 1) {
            tbbtn_btn_nuevo.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a creación de un nueva Tabla");
        } else {
            tbbtn_btn_nuevo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a creación de un nueva Tabla");
        }
        if (objAccesos.getAcc_mod() == 1) {
            tbbtn_btn_editar.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a edición de una Tabla");
        } else {
            tbbtn_btn_editar.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a edición de una Tabla");
        }
        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de la lista de Tabla");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de la lista de Tabla");
        }
    }
    
    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
            txt_busqueda.setValue("%%");
            txt_busqueda.focus();

        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() throws SQLException {
        s_estado = "N";
        limpiarCampos();
        habilitaBotones(true, false);
        seleccionaTab(false, true);
        habilitaTab(true, false);
        habilitaCampos(false);

        chk_almest.setChecked(true);

        lst_ManPeriodos = (new DaoManTablas()).listaPeriodos();
        cb_periodo2.setModel(lst_ManPeriodos);
        //habilitaCampos(false);
        txt_codigo.setDisabled(true);
        cb_periodo2.focus();
        chk_almest.setDisabled(true);
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (chk_almest.isChecked() && lst_lista.getSelectedIndex() >= 0) {
            //   Messagebox.show("Es una Ubicación por Defecto", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            s_estado = "E";
            lst_ManPeriodos = (new DaoManTablas()).listaPeriodos();
            cb_periodo2.setModel(lst_ManPeriodos);
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_descripcion.focus();
            txt_codigo.setDisabled(true);

        } else {
            s_estado = "E";
            lst_ManPeriodos = (new DaoManTablas()).listaPeriodos();
            cb_periodo2.setModel(lst_ManPeriodos);
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            txt_descripcion.focus();
            txt_codigo.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        s_mensaje = "Esta Seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "Confirmar", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_lista.setSelectedIndex(-1);
                            habilitaTab(false, true);
                            seleccionaTab(true, false);
                            //validacion de deshacer
                            VerificarTransacciones();
                            //  tbbtn_btn_guardar.setDisabled(true);
                            //   tbbtn_btn_deshacer.setDisabled(true);
                            habilitaBotones(false, true);
                            // lst_ubicaciones.focus();
                            //
                            // habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() {
        String s_valida = verificar();
        if (!s_valida.equals("")) {
            Messagebox.show(s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (campo.equals("periodo")) {
                            cb_periodo2.focus();
                        } /**/ else if (campo.equals("descripcion")) {
                            txt_descripcion.focus();
                        } else if (campo.equals("tipo")) {
                            cb_tipo.focus();
                        }
                    }
                }
            });
        } else {
            if (txt_descripcion.getText().matches("^\\s") || txt_descripcion.getText().matches("^\\s+")) {
                Messagebox.show("El Campo Ubicacion no debe tener espacio en blanco al principio");

            } else {
                s_mensaje = "Esta Seguro que desea guardar los cambios?";
                Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION, new EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                    // ParametrosSalida objParamSalida;
                                    // objlstTablas.addAll(objlstEliminraTablas);
                                    objManTablas = generaRegistro();
                                    // String[] nombres={ txt_descripcion.getValue(), "ENERO", "FEBRERO", "MARZO"};
                                    String[] nombres = {txt_descripcion.getValue(), "ENERO", "FEBRERO", "MARZO",
                                        "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};//,
                                    String[] mes = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
                                    double[] valor = {
                                        d_default.getValue() == null ? 0.00 : d_default.getValue(), d_enero.getValue() == null ? 0.00 : d_enero.getValue(), d_febrero.getValue() == null ? 0.00 : d_febrero.getValue(), d_marzo.getValue() == null ? 0.00 : d_marzo.getValue(),
                                        d_abril.getValue() == null ? 0.00 : d_abril.getValue(), d_mayo.getValue() == null ? 0.00 : d_mayo.getValue(), d_junio.getValue() == null ? 0.00 : d_junio.getValue(), d_julio.getValue() == null ? 0.00 : d_julio.getValue(), d_agosto.getValue() == null ? 0.00 : d_agosto.getValue(),
                                        d_septiembre.getValue() == null ? 0.00 : d_septiembre.getValue(), d_octubre.getValue() == null ? 0.00 : d_octubre.getValue(), d_noviembre.getValue() == null ? 0.00 : d_noviembre.getValue(), d_diciembre.getValue() == null ? 0.00 : d_diciembre.getValue()};
                                    if (s_estado.equals("N")) {
                                        for (int i = 0; i < 13; i++) {
                                            //  for (int j = 0; j <=12; j++) {
                                            //    for (int j = 0; j <= nombres[i].length; j++) {   
                                            String x = nombres[i];
                                            String y = mes[i];
                                            double z = valor[i];

                                            // String k = nombres[j][i];
                                            s_mensaje = objDaoTablas.insertar(objManTablas, x, y, z);
                                            // }
                                        }

                                    } else {
                                        for (int i = 0; i < 13; i++) {
                                            //  for (int j = 0; j <=12; j++) {
                                            //    for (int j = 0; j <= nombres[i].length; j++) {   
                                            String x = nombres[i];
                                            String y = mes[i];
                                            double z = valor[i];

                                            s_mensaje = objDaoTablas.actualizar(objManTablas, x, y, z);
                                        }
                                    }
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    habilitaTab(false, false);
                                    seleccionaTab(true, false);
                                    habilitaBotones(false, true);
                                    habilitaCampos(true);
                                    limpiarCampos();
                                    limpiaAuditoria();
                                    VerificarTransacciones();
                                    //  cb_periodo2.setSelectedIndex(0);
                                    //  cb_tipo.setSelectedIndex(0);
                                    objlstTablas = objDaoTablas.lstTablas(cb_periodo.getValue());
                                    lst_lista.setModel(objlstTablas);
                                }
                            }
                        });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {
        if (lst_lista == null) {
            Messagebox.show("No hay registros de unidad de manejo para imprimir", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("usuario", objUsuCredential.getCuenta());
            objMapObjetos.put("REPORT_LOCALE", new Locale("en", "US"));
            Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/mantenimiento/LovImpresionTablas.zul", null, objMapObjetos);
            window.doModal();
        }
    }

    @Listen("onSelect=#lst_lista")
    public void seleccionaRegistro() throws SQLException {
        limpiarCampos();
        objManTablas = (ManTablas) lst_lista.getSelectedItem().getValue();
        if (objManTablas == null) {
            objManTablas = objlstTablas.get(i_sel + 1);
        }
        i_sel = lst_lista.getSelectedIndex();
        llenarCampos();
        habilitaCampos(true);
        habilitaTab(false, false);

    }

    @Listen("onSeleccion=#lst_lista")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstTablas = (ListModelList) lst_lista.getModel();
        //si viene de registrar nuevos recibos
        if (!objlstTablas.isEmpty() || objlstTablas != null) {
            Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Reg.getParent().getParent();
            objlstTablas.get(item.getIndex()).setValor(chk_Reg.isChecked());
            lst_lista.setModel(objlstTablas);
        }
    }

    public ManTablas generaRegistro() throws SQLException {
        int i_ubicdef;
        if (s_estado.equals("N")) {
            correlativo();
        }
        String codigo = txt_codigo.getValue();//objManTablas.getCodigo();
        String descripcion = txt_descripcion.getValue();
        String tipo = cb_tipo.getValue();
        String periodo = cb_periodo2.getValue();
        //double defaul = d_default.getValue();
        if (chk_almest.isChecked()) {
            i_ubicdef = 1;
        } else {
            i_ubicdef = 2;
        }
        /*double enero = d_enero.getValue();//objManTablas.getEnero();
         double febrero = d_febrero.getValue();
         double marzo = d_marzo.getValue();
         double abril = d_abril.getValue();
         double mayo = d_mayo.getValue();
         double junio = d_junio.getValue();
         double julio = d_julio.getValue();
         double agosto = d_agosto.getValue();
         double septiembre = d_septiembre.getValue();
         double octubre = d_octubre.getValue();
         double noviembre = d_noviembre.getValue();
         double diciembre = d_diciembre.getValue();*/

        return new ManTablas(codigo, descripcion, tipo, periodo, i_ubicdef);

    }

    public void correlativo() throws SQLException {
        objlstTablas = objDaoTablas.buscaCorrelativo();
        txt_codigo.setValue(DaoManTablas.P_WHERER);
    }

    public Object[][] getTablas(ListModelList<ManTablas> x) {
        ListModelList<ManTablas> objListaDepurada;
        if (s_estado.equals("M")) {
            objListaDepurada = new ListModelList<ManTablas>();
            for (int i = 0; i < x.size(); i++) {
                if (!x.get(i).getInd_accion().equals("Q")) {
                    objListaDepurada.add(x.get(i));
                }
            }
        } else {
            objListaDepurada = x;
        }
        Object[][] listaTabla = new Object[objListaDepurada.getSize()][19];
        for (int i = 0; i < objListaDepurada.size(); i++) {
            listaTabla[i][0] = objListaDepurada.get(i).getPeriodo();
            listaTabla[i][1] = objListaDepurada.get(i).getCodigo();
            listaTabla[i][2] = objListaDepurada.get(i).getDescripcion();
            listaTabla[i][3] = objListaDepurada.get(i).getTipo();
            listaTabla[i][4] = objListaDepurada.get(i).getDefaul();
            listaTabla[i][5] = objListaDepurada.get(i).getEnero();
            listaTabla[i][6] = objListaDepurada.get(i).getFebrero();
            listaTabla[i][7] = objListaDepurada.get(i).getMarzo();
            listaTabla[i][8] = objListaDepurada.get(i).getAbril();
            listaTabla[i][9] = objListaDepurada.get(i).getMayo();
            listaTabla[i][10] = objListaDepurada.get(i).getJunio();
            listaTabla[i][11] = objListaDepurada.get(i).getJulio();
            listaTabla[i][12] = objListaDepurada.get(i).getAgosto();
            listaTabla[i][13] = objListaDepurada.get(i).getSeptiembre();
            listaTabla[i][14] = objListaDepurada.get(i).getOctubre();
            listaTabla[i][15] = objListaDepurada.get(i).getNoviembre();
            listaTabla[i][16] = objListaDepurada.get(i).getDiciembre();
            listaTabla[i][17] = objListaDepurada.get(i).getUsu_add();
            listaTabla[i][18] = objListaDepurada.get(i).getInd_accion();

        }
        return listaTabla;

    }

    public String verificar() {
        String valor;
        if (cb_periodo2.getValue().isEmpty()) {
            valor = "Ingresa periodo";
            campo = "periodo";
        } else if (txt_descripcion.getValue().isEmpty()) {
            valor = "Ingrese descripcion";
            campo = "descripcion";
        } else if (cb_tipo.getValue().isEmpty()) {
            valor = "Ingresa tipo";
            campo = "tipo";
        } else {
            valor = "";
        }
        return valor;
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_lista.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta Seguro que desea Eliminar este registro?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoTablas.eliminar(objManTablas);
                                valor = objDaoTablas.i_flagErrorBD;
                                if (valor == 0) {
                                    objlstTablas.clear();
                                    txt_busqueda.setValue("%%");
                                    txt_busqueda.setDisabled(true);
                                    objlstTablas = objDaoTablas.lstTablas(cb_periodo.getValue());
                                    lst_lista.setModel(objlstTablas);
                                    lst_lista.focus();
                                    Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                    //validacion de eliminacion
                                    tbbtn_btn_eliminar.setDisabled(false);
                                    VerificarTransacciones();
                                    limpiarCampos();
                                    limpiaAuditoria();
                                } else {
                                    Messagebox.show(objDaoTablas.s_msg, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                                }
                            }
                        }
                    });
        }

    }

    //Eventos Otros 
    @Listen("onCtrlKey=#w_tablas")
    public void ctrl_teclado(Event event) throws SQLException, ParseException {
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
            /*case 119:
             if (!tbbtn_btn_imprimir.isDisabled()) {
             botonImprimir();
             }
             break;*/
        }
    }

    //eventos
    @Listen("onOK=#d_default")
    public void next() {
        d_enero.focus();
        d_enero.select();
    }

    @Listen("onOK=#d_enero")
    public void next1() {
        d_febrero.focus();
        d_febrero.select();
    }

    @Listen("onOK=#d_febrero")
    public void next2() {
        d_marzo.focus();
        d_marzo.select();
    }

    @Listen("onOK=#d_marzo")
    public void next3() {
        d_abril.focus();
        d_abril.select();
    }

    @Listen("onOK=#d_abril")
    public void next4() {
        d_mayo.focus();
        d_mayo.select();
    }

    @Listen("onOK=#d_mayo")
    public void next5() {
        d_junio.focus();
        d_junio.select();
    }

    @Listen("onOK=#d_junio")
    public void next6() {
        d_julio.focus();
        d_julio.select();
    }

    @Listen("onOK=#d_julio")
    public void next7() {
        d_agosto.focus();
        d_agosto.select();
    }

    @Listen("onOK=#d_agosto")
    public void next8() {
        d_septiembre.focus();
        d_septiembre.select();
    }

    @Listen("onOK=#d_septiembre")
    public void next9() {
        d_octubre.focus();
        d_octubre.select();
    }

    @Listen("onOK=#d_octubre")
    public void next10() {
        d_noviembre.focus();
        d_noviembre.select();
        
    }

    @Listen("onOK=#d_noviembre")
    public void next11() {
        d_diciembre.focus();
        d_diciembre.select();
    }

    @Listen("onOK=#txt_descripcion")
    public void next12() {
        cb_tipo.focus();
    }

    @Listen("onOK=#cb_periodo2")
    public void next13() {
        txt_descripcion.focus();
    }

    @Listen("onOK=#cb_tipo")
    public void next14() {
        d_default.focus();
    }

    public void limpiaAuditoria() {
        txt_usua.setValue("");
        txt_usumo.setValue("");
        d_fec.setValue(null);
        d_fecmo.setValue(null);
    }

    @Listen("onOK=#d_default")
    public void enter(){
        if (d_default.getValue()!=null) {
            Double x = d_default.getValue();
            d_enero.setValue(x);
            d_febrero.setValue(x);
            d_marzo.setValue(x);
            d_abril.setValue(x);
            d_mayo.setValue(x);
            d_junio.setValue(x);
            d_julio.setValue(x);
            d_agosto.setValue(x);
            d_septiembre.setValue(x);
            d_octubre.setValue(x);
            d_noviembre.setValue(x);
            d_diciembre.setValue(x);
        }else{
              // Double x = d_default.getValue();
            d_enero.setValue(null);
            d_febrero.setValue(null);
            d_marzo.setValue(null);
            d_abril.setValue(null);
            d_mayo.setValue(null);
            d_junio.setValue(null);
            d_julio.setValue(null);
            d_agosto.setValue(null);
            d_septiembre.setValue(null);
            d_octubre.setValue(null);
            d_noviembre.setValue(null);
            d_diciembre.setValue(null);
        }
    
    
    }
    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        chk_almest.setChecked(false);
        txt_descripcion.setValue("");
        txt_codigo.setValue("");
        cb_periodo2.setValue("");
        cb_tipo.setValue("");
        d_default.setValue(null);
        d_enero.setValue(null);
        d_febrero.setValue(null);
        d_marzo.setValue(null);
        d_abril.setValue(null);
        d_mayo.setValue(null);
        d_junio.setValue(null);
        d_julio.setValue(null);
        d_agosto.setValue(null);
        d_septiembre.setValue(null);
        d_octubre.setValue(null);
        d_noviembre.setValue(null);
        d_diciembre.setValue(null);

    }

    //metodos sin utilizar
    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_lista.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaCampos(boolean b_valida) {
        cb_busqueda.setDisabled(b_valida);
        txt_codigo.setDisabled(b_valida);
        txt_descripcion.setDisabled(b_valida);
        cb_tipo.setDisabled(b_valida);
        d_default.setDisabled(b_valida);
        d_enero.setDisabled(b_valida);
        d_febrero.setDisabled(b_valida);
        d_marzo.setDisabled(b_valida);
        d_abril.setDisabled(b_valida);
        d_mayo.setDisabled(b_valida);
        d_junio.setDisabled(b_valida);
        d_julio.setDisabled(b_valida);
        d_agosto.setDisabled(b_valida);
        d_septiembre.setDisabled(b_valida);
        d_octubre.setDisabled(b_valida);
        d_noviembre.setDisabled(b_valida);
        d_diciembre.setDisabled(b_valida);
        chk_almest.setDisabled(b_valida);

    }

    public void llenarCampos() throws SQLException {
        objlstTablas = null;
        objlstTablas = objDaoTablas.lista_det(objManTablas.getCodigo(), cb_periodo.getValue());
        txt_codigo.setValue(objlstTablas.get(0).getCodigo());
        txt_descripcion.setValue(objlstTablas.get(0).getDescripcion());
        cb_periodo2.setValue(objlstTablas.get(0).getPeriodo());

        cb_tipo.setValue(objlstTablas.get(0).getTipo_valor());
        txt_usua.setValue(objlstTablas.get(0).getUsu_add());
        txt_usumo.setValue(objlstTablas.get(0).getUsu_mod());
        d_fec.setValue(objlstTablas.get(0).getDia_add());
        d_fecmo.setValue(objlstTablas.get(0).getDia_mod());

        if (objlstTablas.get(0).getEstado() == 1) {
            chk_almest.setChecked(true);
        } else {
            chk_almest.setChecked(false);
        }
        for (int i = 0; i < 13; i++) {

            if (objlstTablas.get(i).getMes().equals("00")) {
                d_default.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("01")) {
                d_enero.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("02")) {
                d_febrero.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("03")) {
                d_marzo.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("04")) {
                d_abril.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("05")) {
                d_mayo.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("06")) {
                d_junio.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("07")) {
                d_julio.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("08")) {
                d_agosto.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("09")) {
                d_septiembre.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("10")) {
                d_octubre.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("11")) {
                d_noviembre.setValue(objlstTablas.get(i).getDefaul());
            } else if (objlstTablas.get(i).getMes().equals("12")) {
                d_diciembre.setValue(objlstTablas.get(i).getDefaul());
            }

        }

    }
}
