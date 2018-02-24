/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import fr.opensagres.xdocreport.document.json.JSONArray;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.me.gj.model.planillas.mantenimiento.Feriados;

import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import org.zkoss.zk.ui.event.ForwardEvent;

import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class ControllerManFeriados extends GenericForwardComposer {

    @Wire
    Listbox lst_Feriados, lst_mantenimiento;
    @Wire
    Combobox cb_mes, cb_anho;

    Feriados objFeriado;
    ListModelList<Feriados> objlstFeriado, objlstFeriadoVista;
    DaoManFeriados objDaoManFeriados;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    boolean b_valida = true;
    boolean b_validainsert = true;

    @Override
    public void doAfterCompose(Component comp) throws Exception {

        super.doAfterCompose(comp);
        objFeriado = new Feriados();
        objlstFeriado = new ListModelList<Feriados>();
        objDaoManFeriados = new DaoManFeriados();
        objlstFeriadoVista = new ListModelList<Feriados>();
        objlstFeriadoVista = objDaoManFeriados.listarFeriados("TODOS", "TODOS");
        lst_mantenimiento.setModel(objlstFeriadoVista);
        cb_anho.setSelectedIndex(0);
        cb_mes.setSelectedIndex(0);
    }

    public void onClick$lst_Feriados() {
        if (lst_Feriados.getSelectedItem() != null) {
            Messagebox.show("¿Desea eliminar la fecha seleccionada?", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        objFeriado = (Feriados) lst_Feriados.getSelectedItem().getValue();
                        objlstFeriado.remove(objFeriado);
                        lst_Feriados.setModel(objlstFeriado);
                    }
                }
            });

        }
    }

    public void onClick$btn_consultar() throws SQLException {

        objlstFeriadoVista = objDaoManFeriados.listarFeriados(cb_anho.getValue(), cb_mes.getValue());
        lst_mantenimiento.setModel(objlstFeriadoVista);
    }

    public void onClick$btn_eliminar() throws SQLException {

        if (lst_mantenimiento.getSelectedItem() == null) {
            Messagebox.show("Tiene que escoger un registro", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            Messagebox.show("¿Desea eliminar la fecha seleccionada?", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        String s_m;
                        s_m = objDaoManFeriados.eliminarFeriado((Feriados) lst_mantenimiento.getSelectedItem().getValue());
                        Messagebox.show(s_m, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                        objlstFeriadoVista = objDaoManFeriados.listarFeriados("TODOS", "TODOS");
                        lst_mantenimiento.setModel(objlstFeriadoVista);
            
                    }
                }
            });
            /*objlstFeriadoVista = objDaoManFeriados.listarFeriados(cb_anho.getValue(), cb_mes.getValue());
             lst_mantenimiento.setModel(objlstFeriadoVista);*/
        }
    }

    public void onClick$btn_guardar() throws SQLException {
        String s_ms = "";
        if (!objlstFeriado.isEmpty()) {
            for (int i = 0; i < objlstFeriado.size(); i++) {
                s_ms = objDaoManFeriados.insertarFeriado(objlstFeriado.get(i));
                if (!s_ms.equals("Se grabó correctamente")) {
                    Messagebox.show(s_ms, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    b_validainsert = false;
                    break;
                }
            }

        } else {
            Messagebox.show("No hay ninguna fecha seleccionada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        if (b_validainsert) {
            Messagebox.show(s_ms, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        objlstFeriadoVista = objDaoManFeriados.listarFeriados("TODOS", "TODOS");
        lst_mantenimiento.setModel(objlstFeriadoVista);
        objlstFeriado = null;
        objlstFeriado = new ListModelList<Feriados>();
        lst_Feriados.setModel(objlstFeriado);
        b_validainsert = true;
    }

    public void onUser$in(Event event) throws ParseException {
        ForwardEvent eventx = (ForwardEvent) event;
        Object o_dia = eventx.getOrigin().getData();
        String s_dia = o_dia.toString();
        String[] a_dia = s_dia.split("/");
        String s_diasemana = "";
        String s_mes = "";

        if (Integer.parseInt(a_dia[0]) < 10) {
            a_dia[0] = "0" + a_dia[0];
        }
        if (Integer.parseInt(a_dia[1]) < 10) {
            a_dia[1] = "0" + a_dia[1];
        }
        s_dia = a_dia[0] + "/" + a_dia[1] + "/" + a_dia[2];
        objFeriado = new Feriados();
        objFeriado.setS_dia(s_dia);

        if (!objlstFeriado.isEmpty()) {
            for (int i = 0; i < objlstFeriado.size(); i++) {
                if (objlstFeriado.get(i).getS_dia().equals(s_dia)) {
                    Messagebox.show("La fecha elegida ya se encuentra agregada", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
                    b_valida = false;
                    break;

                }
            }

        }
        objFeriado.setS_anho(s_dia.substring(6, 10));
        objFeriado.setS_dianum(s_dia.substring(0, 2));
        Date d_dia = formatter.parse(s_dia);
        Calendar c = Calendar.getInstance();
        c.setTime(d_dia);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int i_mes = Integer.parseInt(s_dia.substring(3, 5));
        switch (i_mes) {
            case 1:
                s_mes = "ENERO";
                break;
            case 2:
                s_mes = "FEBRERO";
                break;
            case 3:
                s_mes = "MARZO";
                break;
            case 4:
                s_mes = "ABRIL";
                break;
            case 5:
                s_mes = "MAYO";
                break;
            case 6:
                s_mes = "JUNIO";
                break;
            case 7:
                s_mes = "JULIO";
                break;
            case 8:
                s_mes = "AGOSTO";
                break;
            case 9:
                s_mes = "SEPTIEMBRE";
                break;
            case 10:
                s_mes = "OCTUBRE";
                break;
            case 11:
                s_mes = "NOVIEMBRE";
                break;
            case 12:
                s_mes = "DICIEMBRE";
                break;

        }

        switch (dayOfWeek) {
            case 1:
                s_diasemana = "DOMINGO";
                break;
            case 2:
                s_diasemana = "LUNES";
                break;
            case 3:
                s_diasemana = "MARTES";
                break;
            case 4:
                s_diasemana = "MIERCOLES";
                break;
            case 5:
                s_diasemana = "JUEVES";
                break;
            case 6:
                s_diasemana = "VIERNES";
                break;
            case 7:
                s_diasemana = "SABADO";
                break;

        }
        objFeriado.setD_dia(d_dia);
        objFeriado.setS_diasemana(s_diasemana);
        objFeriado.setS_mes(s_mes);
        if (b_valida) {
            objlstFeriado.add(objFeriado);
            lst_Feriados.setModel(objlstFeriado);
        }
        b_valida = true;
    }
}
