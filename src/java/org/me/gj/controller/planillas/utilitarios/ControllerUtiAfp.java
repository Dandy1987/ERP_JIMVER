package org.me.gj.controller.planillas.utilitarios;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

/**
 *
 * @author HUALLPA
 */
public class ControllerUtiAfp extends SelectorComposer<Component> {

    @Wire
    Combobox cb_sucursal, cb_periodo;

    @Wire
    Textbox txt_desperiodo;

    @Wire
    Button btn_aceptar;

    @Wire
    Radiogroup rg_periodo;

    @Wire
    Checkbox chk_afpnet;

    //Variables Globales
    PerPago objPerPago = new PerPago();
    DaoAccesos objDaoAccesos;
    DaoEnlaces objDaoEnlaces;
    Process proceso;
    Runtime runtime;
    int i_emp_id;
    private final String s_ruta_base = "D:\\GJ_CARPETAS\\PLANILLAS\\ENLACES\\AFP\\";
    private final String s_ruta_bk = "D:\\GJ_CARPETAS\\PLANILLAS\\ENLACES\\AFP\\Backup\\";
    private String s_nombre_bat = ""; //genlinkafp.bat
    Date tiempo;
    DateFormat d_ffecha = new SimpleDateFormat("_yyyyMMdd_");
    DateFormat d_fhora = new SimpleDateFormat("_HH_mm_ss");

    //Listas
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<PerPago> objlstPerPago = new ListModelList<PerPago>();

    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //Log
    private static final Logger LOGGER = Logger.getLogger(ControllerUtiAfp.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        i_emp_id = objUsuCredential.getCodemp();
        objDaoAccesos = new DaoAccesos();
        objDaoEnlaces = new DaoEnlaces();
        objlstSucursal = objDaoAccesos.lstSucursales_union(i_emp_id);
        objlstPerPago = objDaoEnlaces.periodoCerrado(true);
        cb_sucursal.setModel(objlstSucursal);
        cb_periodo.setModel(objlstPerPago);
    }

    @Listen("onChange=#cb_periodo")
    public void cargaDesc() throws SQLException {
        if (cb_periodo.getSelectedIndex() == 0) {
            txt_desperiodo.setValue("");
            txt_desperiodo.setValue(null);
        } else {
            txt_desperiodo.setValue(objDaoEnlaces.periodoCerradoDesc(cb_periodo.getValue().toString(), false).substring(8));
        }
    }

    @Listen("onClick=#rg_periodo")
    public void radioSeleccion() throws SQLException {
        if (rg_periodo.getSelectedIndex() == 0) {
            String periodo = objDaoEnlaces.getPeriodoCalculado(i_emp_id);
            cb_periodo.setValue(periodo);
            cb_periodo.setDisabled(true);
            if (periodo.equals("--------")) {
                txt_desperiodo.setValue("--------");
            } else {
                txt_desperiodo.setValue(objDaoEnlaces.periodoCerradoDesc(periodo, true));
            }
        } else {
            cb_periodo.setValue("");
            cb_periodo.setValue(null);
            txt_desperiodo.setValue("");
            txt_desperiodo.setValue(null);
            cb_periodo.setDisabled(false);
            objlstPerPago = objDaoEnlaces.periodoCerrado(true);
            cb_periodo.setModel(objlstPerPago);
        }
    }

    @Listen("onClick=#btn_aceptar")
    public void generaConta() {
        if (cb_periodo.getSelectedIndex() == 0 || cb_sucursal.getSelectedIndex() == cb_sucursal.getItemCount() - 1) {
            Messagebox.show("¡Revise los campos!", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
        } else {
            if (cb_periodo.getValue().toString().equals("--------") || txt_desperiodo.getValue().toString().equals("--------") || txt_desperiodo.getValue().toString().equals("")) {
                Messagebox.show("¡Periodo actual no válido!", "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            } else {

                try {

                    String u_validacion1 = objDaoEnlaces.getPeriodoCalculado(i_emp_id);
                    String u_validacion2 = objDaoEnlaces.periodoCerradoDesc(cb_periodo.getValue().toString(), false).substring(0, 8);

                    if ((rg_periodo.getSelectedIndex() == 0 && u_validacion1.equals(cb_periodo.getValue().toString())) ||
                        (rg_periodo.getSelectedIndex() == 1 && u_validacion2.equals(cb_periodo.getValue().toString()))) {

                        String periodo = cb_periodo.getValue().toString();
                        String nom_archivo = "";

                        if (chk_afpnet.isChecked()) {
                            nom_archivo = "AFPNET" + periodo;
                            s_nombre_bat = "genlinkafp.bat";
                        } else {
                            nom_archivo = "AFP" + periodo;
                            s_nombre_bat = "genlinkafp2.bat";
                        }

                        String ruta_base_bat = s_ruta_base + s_nombre_bat;
                        String nom_tabla = "";
                        int i_suc_id = Integer.parseInt(cb_sucursal.getSelectedItem().getValue().toString());

                        if (rg_periodo.getSelectedIndex() == 0) {
                            nom_tabla = "codijisa.tplplames";
                        } else {
                            nom_tabla = "codijisa.tplplanilla";
                        }

                        runtime = Runtime.getRuntime();
                        //Despues de 'ruta_base_bat' siguen en secuencia los parametros
                        String comandos_p = "cmd start /c " + ruta_base_bat + " " + s_ruta_base + nom_archivo + ".txt" + " " + periodo + " " + nom_tabla + " " + i_emp_id + " " + i_suc_id;
                        proceso = runtime.exec(comandos_p);

                        int x = proceso.waitFor();

                        if (x == 0) {
                            File archivo = new File(s_ruta_base + nom_archivo + ".txt");
                            Filedownload.save(archivo, "text/plain");
                            Messagebox.show("¡Proceso terminado satisfactoriamente!");
                        }

                        tiempo = new Date();
                        ruta_base_bat = s_ruta_base + s_nombre_bat;
                        String nom_archivo_bk = s_ruta_bk + nom_archivo + "_" + objUsuCredential.getEmpresa() + "_" + cb_sucursal.getValue().toString() + d_ffecha.format(tiempo) + d_fhora.format(tiempo) + ".txt";
                        comandos_p = "cmd start /c " + ruta_base_bat + " " + nom_archivo_bk + " " + periodo + " " + nom_tabla + " " + i_emp_id + " " + i_suc_id;
                        proceso = runtime.exec(comandos_p);

                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | ha generado el enlace de AFP de " + objUsuCredential.getEmpresa() + " - " + cb_sucursal.getValue().toString() + " en el periodo " + periodo);

                        restableceCampos();

                    } else {
                        Messagebox.show("¡El periodo elegido cambio de situacion, vuelva a seleccionar el Tipo de Periodo!", "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
                        restableceCampos();
                    }

                } catch (IOException e) {
                    Messagebox.show("Error al cargar los datos debido al error: " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
                    LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | no pudo generar el enlace de AFP de " + objUsuCredential.getEmpresa() + " - " + cb_sucursal.getValue().toString());
                } catch (InterruptedException ex) {
                    Messagebox.show("Error al cargar los datos debido al error: " + ex.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
                    LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | no pudo generar el enlace de AFP de " + objUsuCredential.getEmpresa() + " - " + cb_sucursal.getValue().toString());
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(ControllerUtiAfp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void restableceCampos() {
        try {
            rg_periodo.setSelectedIndex(1);
            cb_sucursal.setSelectedItem(null);
            cb_sucursal.setValue("");
            cb_periodo.setSelectedItem(null);
            cb_periodo.setValue("");
            txt_desperiodo.setValue("");
            objlstSucursal = objDaoAccesos.lstSucursales_union(i_emp_id);
            objlstPerPago = objDaoEnlaces.periodoCerrado(true);
            cb_sucursal.setModel(objlstSucursal);
            cb_periodo.setModel(objlstPerPago);
            cb_periodo.setDisabled(false);
        } catch (SQLException e) {
            Messagebox.show("Error al cargar los datos debido al error: " + e.getMessage(), "ERP-JIMVER", Messagebox.OK, Messagebox.EXCLAMATION);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | [" + objUsuCredential.getCuenta() + "] | tuvo un error al cargar los datos.");
        }
    }

}
