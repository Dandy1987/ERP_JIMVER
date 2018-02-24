package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.informes.ControllerCertificadoTrab;
import org.me.gj.controller.planillas.informes.DaoCartaBancos;
import org.me.gj.controller.planillas.informes.DaoCertificadoTrab;
import org.me.gj.controller.planillas.mantenimiento.ControllerManPresPer;
import org.me.gj.controller.planillas.procesos.ControllerAsistenciaGen;
import org.me.gj.controller.planillas.procesos.ControllerMovLinea;
import org.me.gj.controller.planillas.procesos.DaoMovLinea;
import org.me.gj.model.planillas.mantenimiento.Personal;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovBuscarPersonalMovimiento extends SelectorComposer<Component> {

    @Wire
    Window w_lov_per;

    @Wire
    Listbox lst_per;

    @Wire
    Textbox txt_codper_man, txt_desper_man, txt_busqueda_per;
    ListModelList<Personal> objlstPersonal;
    ListModelList<Personal> objlstPersonal2;
    Personal objPersonal;

    DaoMovLinea objDaoManPresPer = new DaoMovLinea();
    DaoCertificadoTrab objDaoInfCerTra = new DaoCertificadoTrab();
    DaoCartaBancos objDaoInfCarBan = new DaoCartaBancos();

    Map parametros;
    String controlador;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        parametros = new HashMap(Executions.getCurrent().getArg());
        txt_codper_man = (Textbox) parametros.get("id_per");
        txt_desper_man = (Textbox) parametros.get("des_per");
        controlador = (String) parametros.get("controlador");
    }

    @Listen("onCreate=#w_lov_per")
    public void cargarPersonal() throws SQLException {
        if (controlador.equals("ControllerCertificadoTrab")) {
            objlstPersonal = objDaoInfCerTra.busquedaLovPersonal(objUsuCredential.getCodemp(),objUsuCredential.getCodsuc(),"TODOS","ACTIVOS","X","");
            lst_per.setModel(objlstPersonal);
            lst_per.focus();
            txt_busqueda_per.focus();
        } else if (controlador.equals("ControllerCartaBancos")) {
            objlstPersonal = objDaoInfCarBan.busquedaLovPersonal(objUsuCredential.getCodemp(),"1",3,"",2);
            lst_per.setModel(objlstPersonal);
            lst_per.focus();
            txt_busqueda_per.focus();
        } else {
            objlstPersonal = objDaoManPresPer.busquedaLovPersonal(objUsuCredential.getCodemp(),objUsuCredential.getCodsuc(),"TODOS","ACTIVOS","X","");
            lst_per.setModel(objlstPersonal);
            lst_per.focus();
            txt_busqueda_per.focus();
        }
    }

    @Listen("onOK=#txt_busqueda_per")
    public void buscarPersonal() throws SQLException {
        String consulta = txt_busqueda_per.getValue().toUpperCase();
        if (controlador.equals("ControllerCertificadoTrab")) {
            if (txt_busqueda_per.getValue().isEmpty()) {
                txt_busqueda_per.setValue("%%");
//                objlstPersonal = objDaoInfCerTra.busquedaLovPersonal();
                objlstPersonal = objDaoInfCerTra.busquedaLovPersonal(objUsuCredential.getCodemp(),objUsuCredential.getCodsuc(),"TODOS","ACTIVOS","X","");
                lst_per.setModel(objlstPersonal);
                lst_per.focus();
                txt_busqueda_per.focus();
            } else {
                objlstPersonal = objDaoInfCerTra.busquedaLovPersonal2(consulta);
                lst_per.setModel(objlstPersonal);
                lst_per.focus();
                txt_busqueda_per.focus();
            }
        } else if (controlador.equals("ControllerCartaBancos")) {
            if (txt_busqueda_per.getValue().isEmpty()) {
                txt_busqueda_per.setValue("%%");
                //objlstPersonal = objDaoInfCarBan.busquedaLovPersonal();
                objlstPersonal = objDaoInfCarBan.busquedaLovPersonal(objUsuCredential.getCodemp(),"1",3,"",2);
                lst_per.setModel(objlstPersonal);
                lst_per.focus();
                txt_busqueda_per.focus();
            } else {
                objlstPersonal = objDaoInfCarBan.busquedaLovPersonal2(consulta);
                lst_per.setModel(objlstPersonal);
                lst_per.focus();
                txt_busqueda_per.focus();
            }
        } else {
            objlstPersonal.clear();
            objlstPersonal = objDaoManPresPer.busquedaLovPersonal2(consulta);
            lst_per.setModel(objlstPersonal);
        }

    }

    @Listen("onChange=#txt_busqueda_per")
    public void changefilter() {
        objlstPersonal2 = new ListModelList<Personal>();
        lst_per.setModel(getPersonal(objlstPersonal2));
    }

    public ListModelList<Personal> getPersonal(ListModelList<Personal> u) {
        for (int i = 0; i < objlstPersonal.getSize(); i++) {
            Personal objPersonal;
            objPersonal = ((Personal) objlstPersonal.get(i));
            if ((objPersonal.getPlidper()).toString().contains(txt_busqueda_per.getValue().toUpperCase().trim())
                    || (objPersonal.getPldesper().toString().contains(txt_busqueda_per.getValue().trim()))) {
                objlstPersonal2.add(objPersonal);
            }
        }
        return objlstPersonal2;

    }

    @Listen("onOK=#lst_per; onClick=#lst_per")
    public void seleccionaPersonal() {
        try {
            if (lst_per.getSelectedIndex() != -1) {
                objPersonal = (Personal) lst_per.getSelectedItem().getValue();
                txt_codper_man.setValue(objPersonal.getPlidper());
                txt_desper_man.setValue(objPersonal.getPldesper());
                lst_per.focus();

                if (controlador.equals("ControllerMovLinea")) {
                    ControllerMovLinea.bandera = false;
                } else if (controlador.equals("ControllerContratos")) {
                    ControllerManPresPer.bandera = false;
                } else if (controlador.equals("ControllerManPresPer")) {
                    ControllerManPresPer.bandera = false;
                }
                w_lov_per.detach();
            }
        } catch (WrongValueException e) {
            Messagebox.show("Error de Selección debido al Error " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Listen("onClose=#w_lov_per")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerMovLinea")) {
            ControllerMovLinea.bandera = false;
        } else if (controlador.equals("ControllerContratos")) {
            ControllerManPresPer.bandera = false;
        } else if (controlador.equals("ControllerManPresPer")) {
            ControllerManPresPer.bandera = false;
        } else if (controlador.equals("ControllerAsistenciaGen")) {
            ControllerAsistenciaGen.bandera = false;
        }
    }

}
