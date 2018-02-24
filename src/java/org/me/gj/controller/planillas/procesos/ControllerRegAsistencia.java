package org.me.gj.controller.planillas.procesos;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.model.planillas.procesos.RegAsistencia;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerRegAsistencia extends SelectorComposer<Component> {

    @Wire
    Textbox txt_codigo;
    @Wire
    Radiogroup rbg_indicardor;
    @Wire
    Listbox lst_regAsistencia;

    //Objetos globales
    DaoRegAsistencia objDaoReg = new DaoRegAsistencia();
    ListModelList<RegAsistencia> objlstRegAsistencia;
    RegAsistencia objAsistencia, objCargaDatos;
    String mensaje = "";

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objlstRegAsistencia = null;
        objlstRegAsistencia = new ListModelList<RegAsistencia>();
        objlstRegAsistencia = objDaoReg.listaRegistro();
        lst_regAsistencia.setModel(objlstRegAsistencia);
        rbg_indicardor.setSelectedIndex(0);
        txt_codigo.focus();
    }

    @Listen("onChange=#txt_codigo")
    public void saltoRobot1() throws SQLException {
        if (txt_codigo.getValue() != null) {
            if (!txt_codigo.getValue().matches("[0-9]*")) {
                Messagebox.show("¡Por favor ingrese valores numéricos!", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                txt_codigo.setValue(null);
            } else {
                if (txt_codigo.getValue().toString().length() == 8) {
                    ingresarMarcado();
                }
            }
        }

    }

    public void ingresarMarcado() throws SQLException {
        objCargaDatos = objDaoReg.getPersonal(txt_codigo.getValue().toString());

        if (objCargaDatos == null) {
            String valida = "Codigo de Trabajador no existe";
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        txt_codigo.setValue(null);
                        txt_codigo.focus();
                    }
                }
            });
        } else {
            //validar si ya esta marco
            int cant = 0;
            ParametrosSalida objPara;
            String flag = rbg_indicardor.getSelectedItem().getValue();
            String fecha = Utilitarios.hoyAsString();
            cant = objDaoReg.verificaIngreso(objCargaDatos.getTipo_doc(), objCargaDatos.getDni(), flag, fecha);

            if (cant == 0) {
                objAsistencia = new RegAsistencia(objUsuCredential.getCodemp(), objCargaDatos.getSuc_id(), objCargaDatos.getTipo_doc(), objCargaDatos.getDni(), flag, objCargaDatos.getCodhor());
                objPara = objDaoReg.insertar(objAsistencia);

                if (objPara.getFlagIndicador() == 0) {
                    Messagebox.show(objPara.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                objlstRegAsistencia = objDaoReg.listaRegistro();
                                lst_regAsistencia.setModel(objlstRegAsistencia);
                                txt_codigo.setValue(null);
                                txt_codigo.focus();
                            }
                        }
                    });
                } else {
                    Messagebox.show(objPara.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                txt_codigo.focus();
                            }
                        }
                    });
                }
            } else {
                String valida = "Usted ya esta registrado con ese tipo de registro.";
                Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            txt_codigo.setValue(null);
                            txt_codigo.focus();
                        }
                    }
                });
            }
        }
    }

    @Listen("onClick=#tbbtn_btn_imprimir")
    public void imprimirDocumento() {
        Map objMapObjetos = new HashMap();
        Window window = (Window) Executions.createComponents("/org/me/gj/view/impresion/planillas/procesos/LovImpresionRegAsistencia.zul", null, objMapObjetos);
        window.doModal();
    }

}
