/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.me.gj.controller.planillas.mantenimiento.DaoManPresPer;
import org.me.gj.controller.planillas.mantenimiento.DaoPersonal;
import org.me.gj.controller.planillas.procesos.DaoDescuentos;
import org.me.gj.controller.seguridad.mantenimiento.DaoAccesos;
import org.me.gj.model.planillas.mantenimiento.ManAreas;
import org.me.gj.model.planillas.mantenimiento.ManPresPerDet;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovDescuentosPrestamos extends SelectorComposer<Component> {

    @Wire
    Combobox cb_fsucursal, cb_area;
    @Wire
    Checkbox chk_rango;
    @Wire
    Button btn_consultar, btn_procesar;
    @Wire
    Datebox d_inicio, d_fin;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Listbox lst_lista;
    @Wire
    Window w_lov_prestamos;
    ListModelList<Sucursales> objlstSucursal = new ListModelList<Sucursales>();
    ListModelList<ManAreas> objlstAreas = new ListModelList<ManAreas>();
    ListModelList<ManPresPerDet> objlsPrestamos = new ListModelList<ManPresPerDet>();
    ListModelList<ManPresPerDet> objlsPrestamosClon = new ListModelList<ManPresPerDet>();
    DaoAccesos objDaoAccesos;
    DaoDescuentos objDaoDescuentos;
    ManPresPerDet objPrestamos;
    DaoPersonal objDaoPersonal;
    ManPresPerDet objPrincipal;
    DaoManPresPer objDaoPrestamos;
    String foco, periodo, s_mensaje;
    Map parametros;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static boolean bandera = false;
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        parametros = new HashMap(Executions.getCurrent().getArg());
        periodo = (String) parametros.get("periodo");
        objDaoAccesos = new DaoAccesos();
        objDaoPersonal = new DaoPersonal();
        objDaoPrestamos = new DaoManPresPer();
        objDaoDescuentos = new DaoDescuentos();
        objlstSucursal = objDaoAccesos.lstSucursales_union(objUsuCredential.getCodemp());
        cb_fsucursal.setModel(objlstSucursal);
        //se completa combobox de areas
        objlstAreas = objDaoPersonal.lst_areas();
        cb_area.setModel(objlstAreas);

    }

    @Listen("onClick=#btn_consultar")
    public void procesar() throws SQLException {
        limpiarLista();
        String valida = verificar();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        if (foco.equals("fechai")) {
                            d_inicio.focus();
                        } else if (foco.equals("fechaf")) {
                            d_fin.focus();
                        } else if (foco.equals("fecha")) {
                            d_fin.focus();
                        }

                    }
                }
            });

        } else {
            String finicio = d_inicio.getValue() == null ? "" : sdf.format(d_inicio.getValue());
            String ffinal = d_fin.getValue() == null ? "" : sdf.format(d_fin.getValue());
            String area = cb_area.getSelectedIndex() == -1 || /*cb_area.getSelectedIndex() == 27 ||*/ cb_area.getSelectedItem().getValue().toString().equals("999") ? "" : cb_area.getSelectedItem().getValue().toString();
            String sucursal = cb_fsucursal.getSelectedIndex() == -1 /*|| cb_fsucursal.getSelectedIndex() == 0*/ || cb_fsucursal.getSelectedItem().getValue().toString().equals("0") ? "" : cb_fsucursal.getSelectedItem().getValue().toString();
            objlsPrestamos = objDaoPrestamos.consultaBloquePrestamos(sucursal, area, "", finicio, ffinal, periodo);
            if (objlsPrestamos.isEmpty()) {
                Messagebox.show("No existen registros para estos filtros", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
            } else {
                lst_lista.setModel(objlsPrestamos);
            }

        }

    }

    @Listen("onSeleccion=#lst_lista")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlsPrestamos = (ListModelList) lst_lista.getModel();
        if (!objlsPrestamos.isEmpty() || objlsPrestamos != null) {
            Checkbox chk = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk.getParent().getParent();
            objlsPrestamos.get(item.getIndex()).setValSelec(chk.isChecked());
            lst_lista.setModel(objlsPrestamos);

        }
    }

    /**
     * Selecciona todos los registos
     */
    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        if (objlsPrestamos.isEmpty()) {
            Messagebox.show("No hay registros a mostrar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            chk_selecAll.setChecked(false);
        } else {
            for (int i = 0; i < objlsPrestamos.getSize(); i++) {
                objlsPrestamos.get(i).setValSelec(chk_selecAll.isChecked());
            }
            lst_lista.setModel(objlsPrestamos);
        }
    }

    @Listen("onClick=#btn_cancelar")
    public void cerrar() {
        w_lov_prestamos.detach();

    }

    @Listen("onClick=#chk_rango")
    public void check() {
        boolean check = chk_rango.isChecked();
        if (check == true) {
            habilitarFecha(false);

        } else {
            habilitarFecha(true);
        }

    }

    /**
     * metodo para procesar la informacion en bloque 3este metodo cambia el
     * estado de la cuota a cancelada e inserta un regitro en la tabla
     * tpldescuentos
     */
    @Listen("onClick=#btn_procesar")
    public void generarBloque() {
        if (objlsPrestamos.isEmpty()) {
            Messagebox.show("No hay datos a procesar", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Esta seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP_JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {

                        public void onEvent(Event t) throws Exception {
                            if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                ParametrosSalida objParam, objSalida;
                                for (int i = 0; i < objlsPrestamos.getSize(); i++) {
                                    /*if (objlsPrestamos.get(i).isValSelec()==false) {
                                        objlsPrestamos.remove(i);
                                       objlsPrestamosClon=objlsPrestamos.;*/
                                 
                                    
                                    if (objlsPrestamos.get(i).isValSelec()) {
                                       // objlsPrestamos.(i);
                                            objlsPrestamosClon = objlsPrestamos;
                                       

                                }
                                }
                                objParam = objDaoPrestamos.modificarBloquePrestamos(getPrestamos(objlsPrestamosClon));
                                objSalida = objDaoDescuentos.insertarBloque(getDetalle(objlsPrestamosClon));
                                if (objParam.getFlagIndicador() == 0 && objSalida.getFlagIndicador() == 0) {

                                    if (objParam.getMsgValidacion() == null) {
                                        Messagebox.show("No se realizó ninguna operación", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                    } else {
                                        Messagebox.show(objParam.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {

                                            public void onEvent(Event t) throws Exception {
                                                if (((Integer) t.getData()).intValue() == Messagebox.OK) {
                                                    w_lov_prestamos.detach();
                                                    //va el focus que te mostrara despues de guardar
                                                }
                                            }
                                        });
                                    }
                                }else{
                                     Messagebox.show("Ocurrio un error al grabar datos", "ERP-JIMVER", 1, "z-msgbox z-msgbox-information");
                                    
                                }
                            }

                        }
                    });
        }
    }

    /**
     * Metodo para contruir arreglo a modificar
     *
     * @param x
     * @return
     */
    public Object[][] getPrestamos(ListModelList<ManPresPerDet> x) {
        ListModelList<ManPresPerDet> objLista;
        
        objLista = x;
        Object[][] listaprestamos = new Object[objLista.size()][5];
        for (int i = 0; i < objLista.size(); i++) {
            if (objLista.get(i).isValSelec() == true) {
                listaprestamos[i][0] = objUsuCredential.getCodemp();
                listaprestamos[i][1] = objLista.get(i).getSUC_ID();
                listaprestamos[i][2] = objLista.get(i).getTPLPRESCAB_ID();
                listaprestamos[i][3] = objLista.get(i).getTPLPRESDET_NROCUOTA();
                listaprestamos[i][4] = objUsuCredential.getCuenta();

            }

        }

        return listaprestamos;

    }

    public Object[][] getDetalle(ListModelList<ManPresPerDet> x) {

        Date fecha = new Date();
        ListModelList<ManPresPerDet> objLista;
      
        objLista = x;

        Object[][] listaDescuentos = new Object[objLista.size()][16];
        for (int j = 0; j < objLista.size(); j++) {
            if (objLista.get(j).isValSelec() == true) {
                listaDescuentos[j][0] = objUsuCredential.getCodemp();
                listaDescuentos[j][1] = objLista.get(j).getSucursal();
                listaDescuentos[j][2] = objLista.get(j).getTipo();//String.valueOf(txt_idpersonal.getValue().charAt(0));
                listaDescuentos[j][3] = objLista.get(j).getDoc();
                listaDescuentos[j][4] = periodo;
                listaDescuentos[j][5] = "137";
                listaDescuentos[j][6] = new java.sql.Date(fecha.getTime());//objLista.get(j).getFecha_mov() == null ? null : new java.sql.Date(objLista.get(j).getFecha_mov().getTime());
                //listaDescuentos[j][7] = "PRESTAMO PERSONAL";
                 listaDescuentos[j][7] = "PRESTAMO " + objLista.get(j).getTPLPRESDET_NROCUOTA() + "/" + objLista.get(j).getTPLPRESDET_TOTCUOTAS();
                listaDescuentos[j][8] = objLista.get(j).getTPLPRESDET_MONTCUOTA();
                listaDescuentos[j][9] = 0.00;
                listaDescuentos[j][10] = "";//objLista.get(j).getRecibo_egreso() == null ? "" : objLista.get(j).getRecibo_egreso();
                listaDescuentos[j][11] = "";//objLista.get(j).getRecibo_egreso_referencia() == null ? "" : objLista.get(j).getRecibo_egreso_referencia();
                listaDescuentos[j][12] = objUsuCredential.getCuenta();
                listaDescuentos[j][13] = "N";//objLista.get(j).getInd_accion();
                listaDescuentos[j][14] = 1;//objLista.get(j).getNumero_op();
                listaDescuentos[j][15] = 1;//tipo de ingreso 1 -> automatico
            }
        }

        return listaDescuentos;

    }

    /**
     * Metodo para habilitar fechas conm el checkbox
     *
     * @param t
     */
    public void habilitarFecha(boolean t) {
        d_inicio.setDisabled(t);
        d_fin.setDisabled(t);
    }

    /**
     * Metodo para limpiar lista principal
     */
    public void limpiarLista() {
        objlsPrestamos = null;
        objlsPrestamos = new ListModelList<ManPresPerDet>();
        lst_lista.setModel(objlsPrestamos);
    }

    /**
     * Metodo para validar que fecha fin sea mayo que fecha inicio
     *
     * @return valor
     */
    public String verificar() {
        String valor = "";
        if (d_inicio.getValue() == null) {
            valor = "Por favor ingresar fecha inicio";
            foco = "fechai";
        } else if (d_fin.getValue() == null) {
            valor = "Por favor ingresar fecha fin";
            foco = "fechaf";
        } else if (d_fin.getValue().before(d_inicio.getValue())) {
            valor = "Fecha fin debe ser mayor";
            foco = "fecha";
        }
        return valor;
    }

}
