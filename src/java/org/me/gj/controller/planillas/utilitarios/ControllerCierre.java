package org.me.gj.controller.planillas.utilitarios;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.me.gj.controller.planillas.mantenimiento.DaoPerPago;
import org.me.gj.controller.planillas.procesos.ControllerMovLinea;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author achocano
 */
public class ControllerCierre extends SelectorComposer<Component> {

    @Wire
    Textbox txt_periodo;
    @Wire
    Button btn_calcular;
    @Wire
    Combobox cb_sucursal;
    DaoCierres objDaoCierre;
    private DaoAccesos objDaoAccesos;
    private ListModelList<Sucursales> objListSucursales;
    private DaoPerPago objDaoPerPago;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerMovLinea.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objDaoPerPago = new DaoPerPago();
        objDaoAccesos = new DaoAccesos();
        objDaoCierre = new DaoCierres();

        String periodo = objDaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
        txt_periodo.setValue(periodo.equals("") ? "--------" : periodo);
        objListSucursales = new ListModelList<Sucursales>();
    }

	@Listen("onClick=#btn_calcular")
    public void botonCierre() throws SQLException {
        String periodo = txt_periodo.getValue();
        String percom = periodo.substring(6, 8);
        if (periodo.equals("") || periodo.equals("--------")) {
            Messagebox.show("No hay ningun periodo en proceso", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            if (percom.equals("00") || percom.equals("01") || percom.equals("02") || percom.equals("03")) {

                //comparamos los numero de trabajadores 
                int trab_act = objDaoCierre.trabActivos(periodo);
                int trab_cal = objDaoCierre.trabPlames(periodo);

                if (trab_act == trab_cal) {
                    int objParamSalida = objDaoCierre.cierre(periodo, objUsuCredential.getCodemp());
                    ParametrosSalida objParaInactiva = objDaoCierre.inactivaAutomatico(periodo);
                    if (objParamSalida == 1) {
                        Messagebox.show("Se cerro periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        String perio = objDaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
                        txt_periodo.setValue(perio.equals("") ? "--------" : perio);
                    } else {
                        Messagebox.show("Hubo algun problema en la ejecucion", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                    }
                } else {
                    Messagebox.show("Tiene que calcular todos los trabajadores antes del cierre", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                }

            } else {
                int objParamSalida = objDaoCierre.cierre(periodo, objUsuCredential.getCodemp());
                ParametrosSalida objParaInactiva = objDaoCierre.inactivaAutomatico(periodo);
                if (objParamSalida == 1) {
                    Messagebox.show("Se cerro periodo", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    String perio = objDaoPerPago.getPeriodoCalculado(objUsuCredential.getCodemp());
                    txt_periodo.setValue(perio.equals("") ? "--------" : perio);
                } else {
                    Messagebox.show("Hubo algun problema en la ejecucion", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

}
