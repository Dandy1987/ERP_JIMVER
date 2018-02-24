/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.me.gj.controller.planillas.mantenimiento.ControllerPersonal;
import org.zkoss.gmaps.Ginfo;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.MapModel;
import org.zkoss.gmaps.MapModelList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

//import org.zkoss.gmaps.Geocoder;
public class ControllerLovVerDomicilio extends SelectorComposer<Component> {

    @Wire
    Window w_mapa;
    @Wire
    Gmaps mapa;
    @Wire
    Gmarker marca;
    MapModel mapaModelo;
    @Wire
    Doublebox d_latitud, d_longitud;
    List<Ginfo> objlstInfo;
    List<Gmarker> objlstGmarker;
    Map parametros;
    String controlador, s_estado;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        mapa.setZoom(9);

        parametros = new HashMap(Executions.getCurrent().getArg());
        d_latitud = (Doublebox) parametros.get("latitud");
        d_longitud = (Doublebox) parametros.get("longitud");
        controlador = (String) parametros.get("controlador");
        s_estado = (String) parametros.get("s_estado");
       // objlstInfo = new ListModelList<Ginfo>();
        // mapa.setZoom(9);

        //if (s_estado.equals("M")) {
        Gmarker g = new Gmarker();
        g.setLat(d_latitud.getValue() == null ? 0 : d_latitud.getValue());
        g.setLng(d_longitud.getValue() == null ? 0 : d_longitud.getValue());
        objlstGmarker = new ListModelList<Gmarker>();
        objlstGmarker.add(g);
        ListModelList<Object> objlstmaestra = new ListModelList<Object>();
        objlstmaestra.addAll(0, objlstGmarker);
        mapaModelo = new MapModelList(objlstmaestra);
        mapa.setModel(mapaModelo);
        /*} else {
         d_latitud.setValue(mapa.getLat());
         d_longitud.setValue(mapa.getLng());
         }*/

        //Geocoder x = new Geocoder(controlador);
        // x.setAnchor(mapa.getLat(), mapa.getLng());
        //  x.setOpen(true);
    }

    @Listen("onMapClick=#mapa")
    public void mapa() {
        objlstGmarker = new ListModelList<Gmarker>();
        Gmarker g = new Gmarker();
        g.setLat(mapa.getLat());
        g.setLng(mapa.getLng());
        g.setContent("Latitud es : " + String.valueOf(mapa.getLat()) + "<br>"
                + "Longitud es : " + String.valueOf(mapa.getLng()));

        // g.setDraggingEnabled(true);
        g.setOpen(true);
        objlstGmarker.add(g);

        ListModelList<Object> objlstmaestra = new ListModelList<Object>();
        objlstmaestra.addAll(0, objlstGmarker);
        mapaModelo = new MapModelList(objlstmaestra);
        mapa.setModel(mapaModelo);
        // ver(objlstGmarker);
        d_latitud.setValue(mapa.getLat());
        d_longitud.setValue(mapa.getLng());

    }

    @Listen("onClose=#w_mapa")
    public void cerrarVentana() throws SQLException {
        // d_latitud.setValue(mapa.getLat());
        //  d_longitud.setValue(mapa.getLng());

        if (controlador.equals("ControllerPersonal")) {
            ControllerPersonal.bandera = false;
        }
    }

}
