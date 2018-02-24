/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.procesos.ControllerAsistenciaGen;
import org.me.gj.controller.planillas.procesos.DaoAsistenciaGen;
import org.me.gj.model.planillas.procesos.AsistenciaDias;
import org.me.gj.model.planillas.procesos.AsistenciaGen;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovAsistenciaSubsidio extends SelectorComposer<Component> {

    @Wire
    Window w_subsidio;
    @Wire
    Radiogroup rbg_asisgen, rbg_tipodescanso;

    @Wire
    Radio rb_0, rb_1, rb_seguro, rb_particular;

    @Wire
    Listbox lst_subsidio;
    @Wire
    Textbox txt_busqueda, txt_cod, txt_observacion, txt_adj_cermed;
    Map parametros;
    String controlador, tipo;
	Media mediaCerDom;
    File archivoCerDom;
    String rutaFile = "D:\\GJ_CARPETAS\\PLANILLAS\\";
    String rutaFileCerDom = rutaFile + "CERMED\\";
    boolean cargaCerDom = false;
    Date finicio, ffinal;
    ListModelList<AsistenciaGen> objlstAsistencia = new ListModelList<AsistenciaGen>();
    ListModelList<AsistenciaGen> objlstLov = new ListModelList<AsistenciaGen>();
    AsistenciaGen objAsistencia;
	AsistenciaGen objADMedico;
    DaoAsistenciaGen objDaoAsistencia = new DaoAsistenciaGen();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    int tamanho;
    ListModelList<AsistenciaDias> objlstDate = new ListModelList<AsistenciaDias>();
    AsistenciaDias objAsisDias;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        // objlstLov = new ListModelList<AsistenciaGen>();
        txt_cod = (Textbox) parametros.get("codigo");
        tipo = (String) parametros.get("tipo");
        objlstLov = (ListModelList<AsistenciaGen>) parametros.get("objlstLista");
        objlstDate = (ListModelList<AsistenciaDias>) parametros.get("finicio");
        if (tipo.equals("8")) {
            objADMedico = (AsistenciaGen) parametros.get("objeto");
			rb_seguro.setSelected(true);
        }
        if (tipo.equals("1")) {
            rb_0.setSelected(true);
        }
        controlador = (String) parametros.get("controlador");
        tamanho = (Integer) parametros.get("tamanho");
		
    }

    @Listen("onCreate=#w_subsidio")
    public void cargarCosto() throws SQLException {
        objlstAsistencia = objDaoAsistencia.buscarLov(tipo);
        lst_subsidio.setModel(objlstAsistencia);
        lst_subsidio.focus();
        // txt_busqueda.focus();
    }

    @Listen("onOK=#lst_subsidio;onClick=#lst_subsidio")
    public void seleccion() {
        AsistenciaGen objAux = new AsistenciaGen();
        objAux=lst_subsidio.getSelectedItem().getValue();
        String subsidio = objAux.getCod_subsidio();
        try {
            if (tamanho > 1) {

                for (int i = 0; i < tamanho; i++) {
                    objAsistencia = null;
                    //objAsistencia = (AsistenciaGen) lst_subsidio.getSelectedItem().getValue();
                    objAsistencia = new AsistenciaGen();
                    txt_cod.setValue(objAsistencia.getCod_subsidio());

                    String glosa = txt_observacion.getValue();
                    objAsistencia.setGlosa(glosa.toUpperCase());
                    objAsistencia.setFinicio(objlstDate.get(i).getFechainicio());
                    
                    objAsistencia.setFfinal(objlstDate.get(i).getFechainicio());
                    objAsistencia.setCod_subsidio(subsidio);

                    if (tipo.equals("1")) {
                        if (rb_0.isSelected()) {
                            objAsistencia.setTipoasistencia(Integer.parseInt(rb_0.getValue().toString()));
                        } else {
                            objAsistencia.setTipoasistencia(Integer.parseInt(rb_1.getValue().toString()));
                        }
                    }
					if (tipo.equals("8")) {
                        if (rb_particular.isSelected()) {
                            objAsistencia.setTipodescanso(Integer.parseInt(rb_particular.getValue().toString()));
                        } else {
                            objAsistencia.setTipodescanso(Integer.parseInt(rb_seguro.getValue().toString()));
                        }
                    }
					
                    objlstLov.add(objAsistencia);
                }

            } else {
                objAsistencia = (AsistenciaGen) lst_subsidio.getSelectedItem().getValue();
                txt_cod.setValue(objAsistencia.getCod_subsidio());
                String glosa = txt_observacion.getValue();
                objAsistencia.setGlosa(glosa.toUpperCase());
                objAsistencia.setFinicio(objlstDate.get(0).getFechainicio());
                objAsistencia.setFfinal(objlstDate.get(0).getFechafin());

                if (tipo.equals("1")) {
                    if (rb_0.isSelected()) {
                        objAsistencia.setTipoasistencia(Integer.parseInt(rb_0.getValue().toString()));
                    } else {
                        objAsistencia.setTipoasistencia(Integer.parseInt(rb_1.getValue().toString()));
                    }
                }
				if (tipo.equals("8")) {
                    if (rb_particular.isSelected()) {
                        objAsistencia.setTipodescanso(Integer.parseInt(rb_particular.getValue().toString()));
                    } else {
                        objAsistencia.setTipodescanso(Integer.parseInt(rb_seguro.getValue().toString()));
                    }
                }
                objlstLov.add(objAsistencia);
            }
			if (cargaCerDom == true) {
                Files.copy(archivoCerDom, mediaCerDom.getStreamData());
            }
            lst_subsidio.focus();
            if (controlador.equals("ControllerAsistenciaGen")) {
                ControllerAsistenciaGen.bandera = false;

            }
            w_subsidio.detach();

        } catch (Exception e) {
        }
    }

    @Listen("onClose=#w_subsidio")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerAsistenciaGen")) {
            ControllerAsistenciaGen.bandera = false;
        }

    }

    //para cargar el certificado medico
    @Listen("onUpload=#tbbtn_adjcar_cermed")
    public void onUploadCerDom(UploadEvent event) throws Exception {

        mediaCerDom = event.getMedia();
        if (mediaCerDom instanceof Media) {

            if (mediaCerDom.getName().contains(".pdf") || mediaCerDom.getName().contains(".PDF")) {
                //String documento = objADMedico.getPltipdoc() + objADMedico.getPlnrodoc() + objADMedico.getFinicio();
                String documento = objADMedico.getPltipdoc() + objADMedico.getPlnrodoc() + "-" + mediaCerDom.getName();
                //txt_adj_cermed.setValue(rutaFileCerDom + documento + ".pdf");
                txt_adj_cermed.setValue(rutaFileCerDom + documento);
                archivoCerDom = new File(txt_adj_cermed.getValue());
                cargaCerDom = true;
            } else {
                Messagebox.show("NO SE CARGÓ. Seleccionar documento en pdf", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            }
        }

    }
	
}
