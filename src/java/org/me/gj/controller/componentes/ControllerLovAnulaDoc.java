package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoMotivoRechazo;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
import org.me.gj.model.facturacion.mantenimiento.MotivoRechazo;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.seguridad.mantenimiento.Numeracion;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovAnulaDoc extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_anulaciondoc;
    @Wire
    Textbox txt_busqueda, txt_idmot, txt_desmot;
    @Wire
    Datebox d_fecguia, d_hora, d_fecguia2;
    @Wire
    Combobox cb_notaes, cb_serie;
    @Wire
    Checkbox chk_fecanu;
    @Wire
    Radiogroup rbg_tipo;
    @Wire
    Button btn_procesar, btn_cancelar;
    //Instancias de Objetos
    ListModelList<Guias> objlstGuias;
    ListModelList<Numeracion> objlstNumeracion;
    DaoManNotaES objDaoGuias;
    DaoNumeracion objDaoNumeracion;
    Guias objGuias;
    Numeracion objNumeracion;
    //Variables publicas
    Map parametros;
    String controlador, empresa, usuario;
    String modoEjecucion;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovAnulaDoc.class);
    public static boolean bandera = false;

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        parametros = new HashMap(Executions.getCurrent().getArg());
        empresa = (String) parametros.get("empresa");
        usuario = (String) parametros.get("usuario");

        objGuias = new Guias();
        objNumeracion = new Numeracion();
        objDaoGuias = new DaoManNotaES();
        objDaoNumeracion = new DaoNumeracion();
        habilitacampos(true, false);
    }

    //Eventos Secundarios - Validacion
    @Listen("onSelect=#cb_notaes")
    public void valida_tipguia() throws SQLException {
        Guias objGuia = new DaoManNotaES().BusquedaGuia(Utilitarios.lpad(cb_notaes.getSelectedItem().getValue().toString(), 3, "0"));
        if (objGuia.getCodigo() == null) {
            Messagebox.show("El código de la nota de E/S no existe o está inactivo", "ERP-JIMVER", Messagebox.OK,
                    Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                limpiarListaSerie();
                            }
                        }
                    });
        } else {
            limpiarListaSerie();
            objlstNumeracion = objDaoNumeracion.listaSeries(Integer.parseInt(cb_notaes.getSelectedItem().getValue().toString()));
            cb_serie.setModel(objlstNumeracion);
            cb_serie.setDisabled(false);
            cb_serie.focus();
        }
    }

    @Listen("onCheck=#rbg_tipo")
    public void seleccionarTipo() throws SQLException {
        if (rbg_tipo.getSelectedIndex() == 0) {
            habilitacampos(true, false);
            limpiarcampos();
        } else {
            habilitacampos(false, true);
            cargaData();
        }
    }

    @Listen("onCheck=#chk_fecanu")
    public void onCheckCambioFecha() throws SQLException {
        if (chk_fecanu.isChecked()) {
            d_fecguia2.setDisabled(false);
        } else {
            d_fecguia2.setDisabled(true);
        }
    }

    @Listen("onOK=#txt_idmot")
    public void lovMotivoRechazo() {
        if (bandera == false) {
            bandera = true;
            if (txt_idmot.getValue().isEmpty()) {
                modoEjecucion = "ProcPedPend";
                Map objMapObjetos = new HashMap();
                objMapObjetos.put("txt_idmotivo", txt_idmot);
                objMapObjetos.put("txt_desmotivo", txt_desmot);
                objMapObjetos.put("validaBusqueda", modoEjecucion);
                objMapObjetos.put("controlador", "ControllerProcPedPend");
                Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovMotivoRechazo.zul", null, objMapObjetos);
                window.doModal();
            }
        }
    }

    @Listen("onBlur=#txt_idmot")
    public void validaMotivoRechazo() throws SQLException {
        if (!txt_idmot.getValue().isEmpty()) {
            if (!txt_idmot.getValue().matches("[0-9]*")) {
                Messagebox.show("Por favor ingrese valores numéricos", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_idmot.setValue("");
                            txt_desmot.setValue("");
                            txt_idmot.focus();
                        }
                    }
                });
            } else {
                MotivoRechazo objMotRec;
                //int motid = Integer.parseInt(txt_idmot.getValue());
                String motid = txt_idmot.getValue();
                objMotRec = new DaoMotivoRechazo().buscaMotRecxCodigo(motid);
                if (objMotRec == null) {
                    Messagebox.show("El código de motivo no existe o está inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                txt_idmot.setValue("");
                                txt_desmot.setValue("");
                                txt_idmot.focus();
                            }
                        }
                    });
                } else {
                    txt_idmot.setValue(Utilitarios.lpad(String.valueOf(objMotRec.getMrec_id()), 3, "0"));
                    txt_desmot.setValue(objMotRec.getMrec_des());
                }
            }
        } else {
            txt_desmot.setValue("");
        }
        bandera = false;
    }

    @Listen("onClick=#btn_cancelar")
    public void botonSalir() {
        w_lov_anulaciondoc.detach();
    }

    //Eventos Otros 
    public void cargaData() throws SQLException {
        //carga guias
        objlstGuias = new ListModelList<Guias>();
        objlstGuias = objDaoGuias.listaGuias(1);
        cb_notaes.setModel(objlstGuias);
        //objlstGuias.add(new Guias(100, ""));
        //carga serie
        objlstNumeracion = new ListModelList<Numeracion>();
        objlstNumeracion = objDaoNumeracion.listaSeriesSinFiltro();
        cb_serie.setModel(objlstNumeracion);
    }

    public void habilitacampos(boolean valida1, boolean valida2) {
        d_fecguia.setDisabled(valida1);
        d_hora.setDisabled(valida1);
        cb_notaes.setDisabled(valida1);
        cb_serie.setDisabled(valida1);
        chk_fecanu.setDisabled(valida2);
        //if(chk_fecanu.isChecked()){
        d_fecguia2.setDisabled(valida1);
        /*}else{
         d_fecguia2.setDisabled(true);
         }*/
        txt_idmot.setDisabled(valida1);
    }

    public void limpiarcampos() {
        cb_notaes.setSelectedIndex(-1);
        cb_serie.setSelectedIndex(-1);
        chk_fecanu.setChecked(false);
        txt_idmot.setValue("");
        txt_desmot.setValue("");
    }

    public void limpiarListaSerie() {
        //limpio mi lista serie
        objlstNumeracion = null;
        objlstNumeracion = new ListModelList<Numeracion>();
        cb_serie.setValue("");
        cb_serie.setModel(objlstNumeracion);
    }
}
