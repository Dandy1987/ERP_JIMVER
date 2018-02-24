package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoManNotaES;
import org.me.gj.controller.seguridad.mantenimiento.DaoNumeracion;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovAnulaAuto extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_anulaciondoc;
    @Wire
    Textbox txt_busqueda, txt_idmot, txt_desmot;
    @Wire
    Datebox d_fecguia, d_hora, d_fecanu;
    @Wire
    Combobox cb_notaes, cb_serie, cb_serienctotal;
    @Wire
    Checkbox chk_fecanu;
    @Wire
    Radio rd_tipo;
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
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovAnulaAuto.class);

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
        habilitacampos(false);
        cargaData();
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

    public void habilitacampos(boolean valida1) {
        d_fecguia.setDisabled(valida1);
        cb_notaes.setDisabled(valida1);
        cb_serie.setDisabled(valida1);
        d_fecanu.setDisabled(valida1);
        d_hora.setDisabled(valida1);
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
