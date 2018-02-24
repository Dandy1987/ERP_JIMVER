package org.me.gj.controller.planillas.informes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.me.gj.controller.facturacion.mantenimiento.DaoManPer;
import org.me.gj.controller.planillas.procesos.DaoAsistenciaGen;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.planillas.procesos.AsistenciaGen;
import org.me.gj.model.seguridad.mantenimiento.Accesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class ControllerRegistroAsis extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_periodo;

    @Wire
    Listbox lst_asistencia;

    @Wire
    Radiogroup rg_filtro;

    @Wire
    Textbox txt_busqueda, txt_pidpersonal, txt_pdespersonal, txt_pidpersonal1, txt_codarea1, txt_codarea, txt_desarea;

    @Wire
    Checkbox chk_selecAll, chk_forant;

    @Wire
    Datebox d_fecini, d_fecfin;
	
	@Wire
    Toolbarbutton tbbtn_btn_imprimir;
    //Objetos
    DaoAccesos objDaoAccesos;
    DaoAsistenciaGen objDaoRegAsistencia;
    AsistenciaGen objRegAsistencia;
	Accesos objAccesos = new Accesos();
	Personal objPersonal;
    DaoMovLinea objDaoMovLinea;
    ManAreas objArea;
    //Listas
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<AsistenciaGen> objlstRegAsistencia;
	
    //Variables Globales
    int sucursal;
    String periodo;
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
    int i_sel;
	
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerRegistroAsis.class);
	
    public static int forant = 0; //0 no seleccionado, 1 seleccionado

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
		d_fecini.setValue(new Date());
        d_fecfin.setValue(new Date());
        objDaoAccesos = new DaoAccesos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_sucursal.setModel(objlstSucursal);
        //cb_periodo.setValue(Utilitarios.periodoActual());
        //cb_periodo.setModel(new DaoManPer().listaPeriodosDinamico(0));
        objDaoRegAsistencia = new DaoAsistenciaGen();
        objlstRegAsistencia = objDaoRegAsistencia.listaRegistro(objUsuCredential.getCodemp(), 0, "TODOS", "TODOS", d_fecini.getValue(), d_fecfin.getValue());
        lst_asistencia.setModel(objlstRegAsistencia);
		objDaoMovLinea = new DaoMovLinea();
        objPersonal = new Personal();
        //rg_filtro.setSelectedIndex(0);
        //txt_busqueda.setMaxlength(16);
    }

    @Listen("onCreate=#tb_transacciones")
    public void VerificarTransacciones() throws SQLException {
        int usuario = objUsuCredential.getCodigo();
        int empresa = objUsuCredential.getCodemp();
        int sucursal = objUsuCredential.getCodsuc();
        objDaoAccesos = new DaoAccesos();
        objAccesos = objDaoAccesos.Verifica_Permisos_Transacciones(90306000, usuario, empresa, sucursal);

        if (objUsuCredential.getRol() == 1) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado a informes de Registro de Asistencia con el rol: ADMINISTRADOR");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha Ingresado a informes de Registro de Asistencia con el rol: USUARIO NORMAL");
        }

        if (objAccesos.getAcc_imp() == 1) {
            tbbtn_btn_imprimir.setDisabled(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | tiene acceso a impresion de Registro de Asistencia");
        } else {
            tbbtn_btn_imprimir.setDisabled(true);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no tiene acceso a impresion de Registro de Asistencia");
        }

    }
	
    @Listen("onClick=#btn_consultar")
    public void consultar() throws SQLException {
        String s_area = txt_codarea1.getValue();
        String s_idper = txt_pidpersonal1.getValue();

        int i_suc = cb_sucursal.getSelectedItem().getValue();
        if (s_area.isEmpty()) {
            s_area = "TODOS";
        } else {
            s_area = s_area.replace("'", "");
        }
        if (s_idper.isEmpty()) {
            s_idper = "TODOS";
        } else {
            s_idper = s_idper.replace("'", "");
        }
        objlstRegAsistencia = objDaoRegAsistencia.listaRegistro(objUsuCredential.getCodemp(), i_suc, s_area, s_idper, d_fecini.getValue(), d_fecfin.getValue());
        lst_asistencia.setModel(objlstRegAsistencia);

    }

    @Listen("onOK=#txt_codarea")
    public void busquedaArea() {

        if (txt_codarea.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_area", txt_codarea);
            objMapObjetos.put("des_area", txt_desarea);
            objMapObjetos.put("codarea1", txt_codarea1);//campo invisible que guarda informacion de personal
            // objMapObjetos.put("des", txt_desper1);
            objMapObjetos.put("controlador", "ControllerMovimiento");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesAreas.zul", null, objMapObjetos);
            window.doModal();
        }

    }

    @Listen("onBlur=#txt_codarea")
    public void validaAreas() throws SQLException {
        if (!txt_codarea.getValue().isEmpty()) {
            objArea = new ManAreas();
            if (!txt_codarea.getValue().equals("ALL")) {
                String consulta = txt_codarea.getValue();
                objArea = objDaoMovLinea.consultaArea(consulta);
                if (objArea == null) {
                    Messagebox.show("El código de constante no existe o esta inactivo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                        public void onEvent(Event t) throws Exception {
                            txt_codarea.setValue("");
                            txt_desarea.setValue("");
                            txt_codarea1.setValue("");
                            txt_codarea.focus();
                        }
                    });
                }
            }
        } else {
            txt_codarea.setValue("");
            txt_desarea.setValue("");

            txt_codarea1.setValue("");
        }

    }
	
	@Listen("onOK=#txt_pidpersonal")
    public void busquedaPersonal() {

        if (txt_pidpersonal.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("id_per", txt_pidpersonal);
            objMapObjetos.put("des_per", txt_pdespersonal);
            objMapObjetos.put("cod", txt_pidpersonal1);//campo invisible que guarda informacion de personal
            // objMapObjetos.put("des", txt_desper1);
            objMapObjetos.put("area", txt_codarea1.getValue());
            objMapObjetos.put("tipo", 0);
            objMapObjetos.put("sucursal", cb_sucursal.getValue());

            objMapObjetos.put("periodo", "TODOS");
            objMapObjetos.put("controlador", "ControllerDescuentos");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovInformesBoletaPago.zul", null, objMapObjetos);
            window.doModal();
        }

    }
	
	@Listen("onBlur=#txt_pidpersonal")
    public void valida_Personal() throws SQLException {
        if (!txt_pidpersonal.getValue().isEmpty()) {
            if (!txt_pidpersonal.getValue().equals("ALL")) {
                String id = txt_pidpersonal.getValue();
                objPersonal = objDaoMovLinea.getLovPersonal(id);
                if (objPersonal == null) {
                    Messagebox.show("El codigo debe ser del Personal", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {

                        public void onEvent(Event t) throws Exception {
                            txt_pidpersonal.setValue(null);
                            txt_pidpersonal.focus();
                            txt_pdespersonal.setValue("");

                        }
                    });

                } else {
                    txt_pidpersonal.setValue(objPersonal.getPlidper());
                    txt_pdespersonal.setValue(objPersonal.getPldesper());
                    //  habilitaBotonesDetalle(false);
                    txt_pidpersonal1.setValue(objPersonal.getPlidper() + "','");
                }
            }

        } else {// (txt_codper.getValue().isEmpty()) {
            txt_pdespersonal.setValue("");
            txt_pidpersonal1.setValue("");
        }
    }
	
    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlstRegAsistencia.isEmpty()) {
            Messagebox.show("No hay registros de asistencia", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlstRegAsistencia.getSize(); i++) {
                objlstRegAsistencia.get(i).setValSelec(chk_selecAll.isChecked());
            }
            lst_asistencia.setModel(objlstRegAsistencia);
        }
    }

    @Listen("onSelect=#lst_asistencia")
    public void seleccionaRegistroCab() throws SQLException {
        objRegAsistencia = (AsistenciaGen) lst_asistencia.getSelectedItem().getValue();
        if (objRegAsistencia == null) {
            objRegAsistencia = objlstRegAsistencia.get(i_sel + 1);//aqui captura un objeto
        }
        i_sel = lst_asistencia.getSelectedIndex();
    }
	
    @Listen("onSeleccion=#lst_asistencia")
    public void seleccionaEquipo(ForwardEvent evt) {
        //PARA ELEGIR VARIOS DATOS
        objlstRegAsistencia = (ListModelList) lst_asistencia.getModel();

        if (!objlstRegAsistencia.isEmpty() || objlstRegAsistencia != null) {
            Checkbox chk_Equi = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk_Equi.getParent().getParent();
            objlstRegAsistencia.get(item.getIndex()).setValSelec(chk_Equi.isChecked());
            lst_asistencia.setModel(objlstRegAsistencia);

        }
//        //PARA ELEGIR SOLO UN DATO
//        for (int i = 0; i < objlstRegAsistencia.getSize(); i++) {
//            objlstRegAsistencia.get(i).setValSelec(false);
//        }
//        Checkbox chk_Reg = (Checkbox) evt.getOrigin().getTarget();
//        Listitem item = (Listitem) chk_Reg.getParent().getParent();
//        objlstRegAsistencia.get(item.getIndex()).setValSelec(chk_Reg.isChecked());
//        lst_asistencia.setModel(objlstRegAsistencia);
    }

	@Listen("onClick=#tbbtn_btn_imprimir")
    public void botonImprimir() throws SQLException {

        int cont = 0;
        String s_area = txt_codarea1.getValue();
        if (s_area.isEmpty()) {
            s_area = "TODOS";
        } else {
            s_area = s_area.replace("'", "");
        }
        for (int i = 0; i < objlstRegAsistencia.getSize(); i++) {
            if (objlstRegAsistencia.get(i).isValSelec()) {
                cont++;
            }
        }

        if (cont <= 0 || lst_asistencia.getModel().getSize() < 1) {
            Messagebox.show("Debe seleccionar (√) un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (verificaFecha()) {
                if (chk_forant.isChecked()) {
                    forant = 1;
                    //objDaoRegAsistencia.reporteInfRegistroAsis(obtenerCodigo(), sdf2.format(d_fecini.getValue()), sdf2.format(d_fecfin.getValue()), forant);
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("n_sucid", cb_sucursal.getSelectedItem().getValue());
                    objMapObjetos.put("c_area", s_area);
                    objMapObjetos.put("c_idper", obtenerCodigo());
					objMapObjetos.put("d_fecini", d_fecini.getValue());
                    objMapObjetos.put("d_fecfin", d_fecfin.getValue());
                    objMapObjetos.put("formato", "antiguo");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesRegAsistencia.zul", null, objMapObjetos);
                    window.doModal();
                } else {
                    forant = 0;
                    // objDaoRegAsistencia.reporteInfRegistroAsis(obtenerCodigo(), sdf2.format(d_fecini.getValue()), sdf2.format(d_fecfin.getValue()), forant);
                    Map objMapObjetos = new HashMap();
                    objMapObjetos.put("n_sucid", cb_sucursal.getSelectedItem().getValue());
                    objMapObjetos.put("c_area", s_area);
                    objMapObjetos.put("c_idper", obtenerCodigo());
					objMapObjetos.put("d_fecini", d_fecini.getValue());
                    objMapObjetos.put("d_fecfin", d_fecfin.getValue());
                    objMapObjetos.put("formato", "normal");
                    Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/informes/LovImpresionInformesRegAsistencia.zul", null, objMapObjetos);
                    window.doModal();
                }

            } else {
                Messagebox.show("La fecha inicial y final deben pertenecer al periodo seleccionado y la fecha final tiene que ser mayor o igual a la fecha inicial.", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }
    }
	
    /**
     * Metodo que me permite obtener el codigo del trabajor
	 *
     * @return Devuelve una cadena con los codigos de los trabajadores
     * @throws SQLException 
     */
    public String obtenerCodigo() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstRegAsistencia.getSize(); j++) {
            if (objlstRegAsistencia.get(j).isValSelec()) {
                cadena = cadena + objlstRegAsistencia.get(j).getId_per() + ",";
            }
        }
        return cadena;
    }

    public String obtenerNombre() throws SQLException {
        String cadena = "";
        int i = 0;
        for (int j = 0; j < objlstRegAsistencia.getSize(); j++) {
            if (objlstRegAsistencia.get(j).isValSelec()) {
                cadena = cadena + objlstRegAsistencia.get(j).getId_per() + "','";
            }
        }
        return cadena;
    }

    public boolean verificaFecha() {
        boolean indicator = false;

        String fecIni = "", fecFin = "";

        if (d_fecini.getValue() != null && d_fecfin.getValue() != null) {
            fecIni = sdf1.format(d_fecini.getValue());
            fecFin = sdf1.format(d_fecfin.getValue());
            if (!d_fecfin.getValue().before(d_fecini.getValue())) { //SI NO ES MAYOR

                indicator = true;

            }
        }
        return indicator;
    }

}
