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
import static org.me.gj.controller.planillas.procesos.ControllerAsistenciaGen.bandera;
import org.me.gj.controller.planillas.procesos.DaoAsistenciaGen;
import org.me.gj.model.planillas.procesos.AsistenciaGen;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.me.gj.util.Utilitarios;
import org.zkoss.xel.fn.StringFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author achocano
 */
public class ControllerLovAsistenciaBloque extends SelectorComposer<Component> {

    @Wire
    Listbox lst_bloque, lst_asistenciagen, lstasistencia;
    @Wire
    Radio rb_0, rb_2, rb_7;
    @Wire
    Window w_lov_asist;
    // rb_1, , rb_3, rb_4, rb_5, rb_6, , rb_8, rb_9, rb_11, rb_12;
    @Wire
    Datebox d_fecini, d_fecfin;
    @Wire
    Radiogroup rbg_asisgen;
    @Wire
    Textbox txt_codigo, txt_apenom, txt_mcodper, txt_mdesper, txt_mes, txt_usuadd, txt_usumod, txt_asistencia, txt_periodo,
            //opara lov
            txt_lovfalta, txt_lovliccgoce, txt_lovlicsgoce, txt_lovdesmedico, txt_lovlicenfermedad,
            txt_lovpaternidad;
    @Wire
    Checkbox chk_selecAll;
    @Wire
    Button btn_guardar;
    String sucursal, periodo;
    Map parametros;
    String controlador;
    // DaoAsistenciaGen objDaoAsis;
    String foco = "";
    String s_estado = "";
    String s_mensaje = "";
    DaoAsistenciaGen objdaoAsistenciaGen;
    AsistenciaGen objAsistenciaGen, objDia;
    SimpleDateFormat convert = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ListModelList<AsistenciaGen> objlstAsistenciaGenDet, objlstAsistenciaGen, objlstPrueba, objAsistenciaLlenar;
    ListModelList<AsistenciaGen> objlstLov = new ListModelList<AsistenciaGen>();
    //ListModelList<AsistenciaGen> objlstLov = new ListModelList<AsistenciaGen>();
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        objdaoAsistenciaGen = new DaoAsistenciaGen();
        objlstPrueba = new ListModelList<AsistenciaGen>();//AsistenciaGen();
        habilitaRadioGroup(false);
        parametros = new HashMap(Executions.getCurrent().getArg());
        sucursal = (String) parametros.get("sucursal");
        periodo = (String) parametros.get("periodo");
        lstasistencia = (Listbox) parametros.get("lista");
        objlstAsistenciaGenDet = objdaoAsistenciaGen.listaBloque(objUsuCredential.getCodemp(), sucursal, periodo);
        lst_bloque.setModel(objlstAsistenciaGenDet);
        objAsistenciaGen = new AsistenciaGen();
        objlstAsistenciaGen = objdaoAsistenciaGen.consultarAsistenciasGen(sucursal, periodo, "", "");
        lst_asistenciagen.setModel(objlstAsistenciaGen);
        chk_selecAll.setChecked(true);
        seleccionaTodo();
        limpiarFechas();

    }

    @Listen("onCheck=#chk_selecAll")
    public void seleccionaTodo() {
        for (int i = 0; i < objlstAsistenciaGen.getSize(); i++) {
            objlstAsistenciaGen.get(i).setValSelec(chk_selecAll.isChecked());
        }
        lst_asistenciagen.setModel(objlstAsistenciaGen);
        // lst_bloque.setModel(objlstAsistenciaGenDet);
    }

    @Listen("onSeleccion=#lst_asistenciagen")
    public void seleccionaRegistro(ForwardEvent evt) {
        objlstAsistenciaGen = (ListModelList) lst_asistenciagen.getModel();
        if (!objlstAsistenciaGen.isEmpty() || objlstAsistenciaGen != null) {
            Checkbox chk = (Checkbox) evt.getOrigin().getTarget();
            Listitem item = (Listitem) chk.getParent().getParent();
            objlstAsistenciaGen.get(item.getIndex()).setValSelec(chk.isChecked());
            lst_asistenciagen.setModel(objlstAsistenciaGen);

        }
    }

    /**
     * Metodo de seleciona falta
     *
     * @autor junior fernandez
     * @version 31/08/2017
     */
    @Listen("onClick=#rb_0")
    public void seleccionaAsistencia() throws SQLException {
        String valida = verificaFecha();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            seleccion();
            devuelveSelect();
            limpiarCheck();
            botonGuardar();
            /*  s_mensaje = "Esta seguro que desea guardar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             limpiarCheck();
             botonGuardar();
             }
             }
             });*/
            objAsistenciaLlenar = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
            lstasistencia.setModel(objAsistenciaLlenar);
            w_lov_asist.detach();

        }
        // rbg_asisgen.setSelectedItem(null);
        //  lst_detasis.focus();
    }

    @Listen("onClick=#rb_2")
    public void seleccionaVacaciones() throws SQLException {

        int tiempo = 0, x = 0;
        if (d_fecini.getValue() != null && d_fecfin.getValue() != null) {
            //int z = objlstAsistenciaGen.getSize();
            for (int i = 0; i < objlstAsistenciaGen.getSize(); i++) {
                if (objlstAsistenciaGen.get(i).isValSelec()) {
                    tiempo = obtenerTiempo(d_fecini.getValue(), objlstAsistenciaGen.get(i).getPer_fecing());
                    if (tiempo < 365) {
                        objlstAsistenciaGen.get(i).setValSelec(!chk_selecAll.isChecked());//(i)).setValSelec(chk.isChecked());

                        //  lst_asistenciagen.setModel(objlstAsistenciaGen);
                        // int posicion = lst_asistenciagen.getSelectedIndex();
                        //int posicion = lst_asistenciagen.getSelectedIndex();
                        // Listitem lista =lst_asistenciagen.getItemAtIndex(i);
                        //lista.setDisabled(true);
                        // objlstPrueba.remove(objlstAsistenciaGen.get(i));
                        // lst_asistenciagen.removeItemAt(i);
                        lst_asistenciagen.setModel(objlstAsistenciaGen);

                        x = x + 1;

                    }

                }

            }

            Messagebox.show("Hay " + x + " trabajador(es) que no cumplen 1 año en la empresa, se desmarco automaticamente", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);

            /*  if (tiempo < 365) {
             Messagebox.show("Trabajador aun no cumple un año en la empresa", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);  
             }*/
        }
        // else{
        String valida = verificaFecha();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } /*else if (tiempo < 365) {
         Messagebox.show("Trabajador aun no cumple un año en la empresa,se desmarco", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
         } */ else {
            seleccion();
            devuelveSelect();
            limpiarCheck();
            botonGuardar();
            /*s_mensaje = "Esta seguro que desea guardar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             limpiarCheck();
             botonGuardar();

             }

             }
             });*/
            objAsistenciaLlenar = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
            lstasistencia.setModel(objAsistenciaLlenar);
            w_lov_asist.detach();

        }
    }

    @Listen("onClick=#rb_7")
    public void seleccionaCompensacion() throws SQLException {
        String valida = verificaFecha();
        if (!valida.isEmpty()) {
            Messagebox.show(valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        devolverFocus();
                    }
                }
            });
        } else {
            seleccion();
            devuelveSelect();
            limpiarCheck();
            botonGuardar();
            /* s_mensaje = "Esta seguro que desea guardar la asistencia?";
             Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
             @Override
             public void onEvent(Event t) throws Exception {
             if (((Integer) t.getData()).intValue() == Messagebox.OK) {
             devuelveSelect();
             limpiarCheck();
             botonGuardar();
             }
             }
             });*/
            objAsistenciaLlenar = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
            lstasistencia.setModel(objAsistenciaLlenar);
            w_lov_asist.detach();
        }
        // rbg_asisgen.setSelectedItem(null);
        //  lst_detasis.focus();
    }

    /**
     * Metodo coloca focus en el campo
     *
     * @version 23/08/2017
     */
    public void devolverFocus() {
        if (foco.equals("fechainicio")) {
            d_fecini.focus();
        } else if (foco.equals("fechafin")) {
            d_fecfin.focus();
        } else if (foco.equals("fecha")) {
            d_fecfin.focus();
        }

    }

    public void devuelveSelect() {
        String asistencia = "";
        String asistencia2 = "";
        asistencia = txt_asistencia.getValue();
        if (txt_asistencia.getValue().equals("ASISTIO")) {
            asistencia2 = "0";
        } else if (txt_asistencia.getValue().equals("LIC. C. GOCE")) {
            asistencia2 = "5";
        } else if (txt_asistencia.getValue().equals("FALTA")) {
            asistencia2 = "1";
        } else if (txt_asistencia.getValue().equals("LIC. S. GOCE")) {
            asistencia2 = "6";
        } else if (txt_asistencia.getValue().equals("VACACIONES")) {
            asistencia2 = "2";
        } else if (txt_asistencia.getValue().equals("COMPENSACION")) {
            asistencia2 = "7";
        } else if (txt_asistencia.getValue().equals("PRE NATAL")) {
            asistencia2 = "3";
        } else if (txt_asistencia.getValue().equals("DESC. MEDICO")) {
            asistencia2 = "8";
        } else if (txt_asistencia.getValue().equals("POST NATAL")) {
            asistencia2 = "4";
        } else if (txt_asistencia.getValue().equals("DESC. M. SUBSIDIO")) {
            asistencia2 = "9";
        } else if (txt_asistencia.getValue().equals("LIC. PATERNIDAD")) {
            asistencia2 = "A";
        } else if (txt_asistencia.getValue().equals("LIC. ENF. TERMINAL")) {
            asistencia2 = "B";
        }

        Date dia_ini = d_fecini.getValue();
        Date dia_fin = d_fecfin.getValue();

        int dia_i = Integer.parseInt(StringFns.substring(sdf.format(dia_ini), 0, 2));
        int dia_f = Integer.parseInt(StringFns.substring(sdf.format(dia_fin), 0, 2));
        for (int i = dia_i; i <= dia_f; i++) {
            String d = objlstAsistenciaGenDet.get(i - 1).getSfec_asisdias();//sfec_asisdias dia
            String o = objlstAsistenciaGenDet.get(i - 1).getSfec_asis();//fecha
            Date c = objlstAsistenciaGenDet.get(i - 1).getFec_asis();
            //String sub = objlstAsistenciaGenDet.get(i - 1).getCod_subsidio();
            //String glosa = objlstAsistenciaGenDet.get(i - 1).getGlosa();
            //String codigo_lov = objlstAsistenciaGenDet.get(i-1).get
            pintarLista("" + i, asistencia2, asistencia, d, o, c);
        }
    }

    public void pintarLista(String a, String asistencia2, String asistencia, String d, String o, Date c) {
        for (int i = 0; i < objlstAsistenciaGen.getSize(); i++) {
            if (objlstAsistenciaGen.get(i).isValSelec()) {

                if (a.equals("1")) {
                    objAsistenciaGen.setPldia01(asistencia2);
                } else if (a.equals("2")) {
                    objAsistenciaGen.setPldia02(asistencia2);
                } else if (a.equals("3")) {
                    objAsistenciaGen.setPldia03(asistencia2);
                } else if (a.equals("4")) {
                    objAsistenciaGen.setPldia04(asistencia2);
                } else if (a.equals("5")) {
                    objAsistenciaGen.setPldia05(asistencia2);
                } else if (a.equals("6")) {
                    objAsistenciaGen.setPldia06(asistencia2);
                } else if (a.equals("7")) {
                    objAsistenciaGen.setPldia07(asistencia2);
                } else if (a.equals("8")) {
                    objAsistenciaGen.setPldia08(asistencia2);
                } else if (a.equals("9")) {
                    objAsistenciaGen.setPldia09(asistencia2);
                } else if (a.equals("10")) {
                    objAsistenciaGen.setPldia10(asistencia2);
                } else if (a.equals("11")) {
                    objAsistenciaGen.setPldia11(asistencia2);
                } else if (a.equals("12")) {
                    objAsistenciaGen.setPldia12(asistencia2);
                } else if (a.equals("13")) {
                    objAsistenciaGen.setPldia13(asistencia2);
                } else if (a.equals("14")) {
                    objAsistenciaGen.setPldia14(asistencia2);
                } else if (a.equals("15")) {
                    objAsistenciaGen.setPldia15(asistencia2);
                } else if (a.equals("16")) {
                    objAsistenciaGen.setPldia16(asistencia2);
                } else if (a.equals("17")) {
                    objAsistenciaGen.setPldia17(asistencia2);
                } else if (a.equals("18")) {
                    objAsistenciaGen.setPldia18(asistencia2);
                } else if (a.equals("19")) {
                    objAsistenciaGen.setPldia19(asistencia2);
                } else if (a.equals("20")) {
                    objAsistenciaGen.setPldia20(asistencia2);
                } else if (a.equals("21")) {
                    objAsistenciaGen.setPldia21(asistencia2);
                } else if (a.equals("22")) {
                    objAsistenciaGen.setPldia22(asistencia2);
                } else if (a.equals("23")) {
                    objAsistenciaGen.setPldia23(asistencia2);
                } else if (a.equals("24")) {
                    objAsistenciaGen.setPldia24(asistencia2);
                } else if (a.equals("25")) {
                    objAsistenciaGen.setPldia25(asistencia2);
                } else if (a.equals("26")) {
                    objAsistenciaGen.setPldia26(asistencia2);
                } else if (a.equals("27")) {
                    objAsistenciaGen.setPldia27(asistencia2);
                } else if (a.equals("28")) {
                    objAsistenciaGen.setPldia28(asistencia2);
                } else if (a.equals("29")) {
                    objAsistenciaGen.setPldia29(asistencia2);
                } else if (a.equals("30")) {
                    objAsistenciaGen.setPldia30(asistencia2);
                } else if (a.equals("31")) {
                    objAsistenciaGen.setPldia31(asistencia2);
                }

                objDia = new AsistenciaGen();
                objDia.setFec_asis(c);
                objDia.setSfec_asisdias(d);///dia
                objDia.setAsistencia(asistencia);//tipo
                objDia.setSfec_asis(o);//fecha

                objlstAsistenciaGenDet.set(Integer.parseInt(a) - 1, objDia);//lst_detasis.getSelectedIndex()
            }
        }
    }

    public void habilitaRadioGroup(boolean b_valida1) {
        rb_0.setDisabled(b_valida1);
        rb_2.setDisabled(b_valida1);
        rb_7.setDisabled(b_valida1);
        d_fecini.setDisabled(b_valida1);
        d_fecfin.setDisabled(b_valida1);
        /*  rb_1.setDisabled(b_valida1);

         rb_3.setDisabled(b_valida1);
         rb_4.setDisabled(b_valida1);
         rb_5.setDisabled(b_valida1);
         rb_6.setDisabled(b_valida1);

         rb_8.setDisabled(b_valida1);
         rb_9.setDisabled(b_valida1);
         rb_11.setDisabled(b_valida1);
         rb_12.setDisabled(b_valida1);*/

    }

    public void seleccion() {
        if (rbg_asisgen.getSelectedIndex() == 0) {
            txt_asistencia.setValue("ASISTIO");
        } else if (rbg_asisgen.getSelectedIndex() == 2) {
            txt_asistencia.setValue("COMPENSACION");
        } else if (rbg_asisgen.getSelectedIndex() == 1) {
            txt_asistencia.setValue("VACACIONES");
        }

        /* else if (rbg_asisgen.getSelectedIndex() == 1) {
         txt_asistencia.setValue("LIC. C. GOCE");
         } else if (rbg_asisgen.getSelectedIndex() == 2) {
         txt_asistencia.setValue("FALTA");
         } else if (rbg_asisgen.getSelectedIndex() == 3) {
         txt_asistencia.setValue("LIC. S. GOCE");
         } else if (rbg_asisgen.getSelectedIndex() == 6) {
         txt_asistencia.setValue("PRE NATAL");
         } else if (rbg_asisgen.getSelectedIndex() == 7) {
         txt_asistencia.setValue("DESC. MEDICO");
         } else if (rbg_asisgen.getSelectedIndex() == 8) {
         txt_asistencia.setValue("POST NATAL");
         } else if (rbg_asisgen.getSelectedIndex() == 9) {
         txt_asistencia.setValue("DESC. M. SUBSIDIO");
         } else if (rbg_asisgen.getSelectedIndex() == 10) {
         txt_asistencia.setValue("LIC. PATERNIDAD");
         } else if (rbg_asisgen.getSelectedIndex() == 11) {
         txt_asistencia.setValue("LIC. ENF. TERMINAL");
         }*/
    }

    public String verificaFecha() {
        String valor = "";
        String fecha_inicio = "", fecha_fin = "";

        if (d_fecini.getValue() != null && d_fecfin.getValue() != null) {
            fecha_inicio = convert.format(d_fecini.getValue());
            fecha_fin = convert.format(d_fecfin.getValue());
        }

        if (d_fecini.getValue() == null) {
            valor = "Por favor ingresar fecha inicio";
            foco = "fechainicio";
            limpiarCheck();

        } else if (d_fecfin.getValue() == null) {
            valor = "Por favor ingresar fecha fin ";
            foco = "fechafin";
            limpiarCheck();
        } else if (d_fecfin.getValue().before(d_fecini.getValue())) {
            valor = "La fecha final debe ser mayor a la de inicio";
            foco = "fecha";
            limpiarCheck();
        }// else if (!objAsistenciaGen.getPeriodo().equals(fecha_inicio)) {
        else if (!periodo.equals(fecha_inicio)) {
            valor = "Por favor fecha inicio debe ser igual al periodo generado";
            foco = "fechainicio";
            limpiarCheck();
        } //else if (!objAsistenciaGen.getPeriodo().equals(fecha_fin)) {
        else if (!periodo.equals(fecha_fin)) {
            valor = "Por favor fecha fin debe ser igual al periodo generado";
            foco = "fechafin"; 
            limpiarCheck();
        } else {
            valor = "";
        }

        return valor;

    }

    public void lov(String tipo, Textbox codigo) {
        if (bandera == false) {
            bandera = true;
            // if (txt_codigo.getValue().equals("")) {
            Map objMapObjetos = new HashMap();
            objMapObjetos.put("codigo", codigo);
            objMapObjetos.put("tipo", tipo);
            objMapObjetos.put("objlstLista", objlstLov);
            objMapObjetos.put("finicio", d_fecini.getValue());
            objMapObjetos.put("ffinal", d_fecfin.getValue());

            objMapObjetos.put("controlador", "ControllerAsistenciaGen");
            Window window = (Window) Executions.createComponents("/org/me/gj/view/componentes/LovAsistenciaSubsidio.zul", null, objMapObjetos);
            window.doModal();
            //}

        }
    }

    ////clase diferencia
    public int obtenerTiempo(Date fecha, Date fechatravajo) {
        long d = fecha.getTime() - fechatravajo.getTime();
        long dias = d / (1000 * 60 * 60 * 24);
        return (int) dias;

    }

    public Object[][] getDetalle(ListModelList<AsistenciaGen> x) {
        ListModelList<AsistenciaGen> objListaAsis;

        objListaAsis = x;
        Object[][] listaDet = new Object[objListaAsis.size()][39];
        for (int i = 0; i < objListaAsis.getSize(); i++) {
            if (objListaAsis.get(i).isValSelec()) {
                listaDet[i][0]  = objUsuCredential.getCodemp();
                listaDet[i][1]  = objListaAsis.get(i).getSuc_id();//objAsistenciaGen.getSuc_id();//objUsuCredential.getCodsuc();
                listaDet[i][2]  = objListaAsis.get(i).getPeriodo();//objAsistenciaGen.getPeriodo();//objListaAsis.get(i).getPeriodo();
                listaDet[i][3]  = objListaAsis.get(i).getPltipdoc();//objAsistenciaGen.getPltipdoc();//objListaAsis.get(i).getPltipdoc();
                listaDet[i][4]  = objListaAsis.get(i).getPlnrodoc();//objAsistenciaGen.getPlnrodoc();///objListaAsis.get(i).getPlnrodoc();
                listaDet[i][5]  = objAsistenciaGen.getPldia01() == null ? "0" : objAsistenciaGen.getPldia01();
                listaDet[i][6]  = objAsistenciaGen.getPldia02() == null ? "0" : objAsistenciaGen.getPldia02();
                listaDet[i][7]  = objAsistenciaGen.getPldia03() == null ? "0" : objAsistenciaGen.getPldia03();
                listaDet[i][8]  = objAsistenciaGen.getPldia04() == null ? "0" : objAsistenciaGen.getPldia04();
                listaDet[i][9]  = objAsistenciaGen.getPldia05() == null ? "0" : objAsistenciaGen.getPldia05();
                listaDet[i][10] = objAsistenciaGen.getPldia06() == null ? "0" : objAsistenciaGen.getPldia06();
                listaDet[i][11] = objAsistenciaGen.getPldia07() == null ? "0" : objAsistenciaGen.getPldia07();
                listaDet[i][12] = objAsistenciaGen.getPldia08() == null ? "0" : objAsistenciaGen.getPldia08();
                listaDet[i][13] = objAsistenciaGen.getPldia09() == null ? "0" : objAsistenciaGen.getPldia09();
                listaDet[i][14] = objAsistenciaGen.getPldia10() == null ? "0" : objAsistenciaGen.getPldia10();
                listaDet[i][15] = objAsistenciaGen.getPldia11() == null ? "0" : objAsistenciaGen.getPldia11();
                listaDet[i][16] = objAsistenciaGen.getPldia12() == null ? "0" : objAsistenciaGen.getPldia12();
                listaDet[i][17] = objAsistenciaGen.getPldia13() == null ? "0" : objAsistenciaGen.getPldia13();
                listaDet[i][18] = objAsistenciaGen.getPldia14() == null ? "0" : objAsistenciaGen.getPldia14();
                listaDet[i][19] = objAsistenciaGen.getPldia15() == null ? "0" : objAsistenciaGen.getPldia15();
                listaDet[i][20] = objAsistenciaGen.getPldia16() == null ? "0" : objAsistenciaGen.getPldia16();
                listaDet[i][21] = objAsistenciaGen.getPldia17() == null ? "0" : objAsistenciaGen.getPldia17();
                listaDet[i][22] = objAsistenciaGen.getPldia18() == null ? "0" : objAsistenciaGen.getPldia18();
                listaDet[i][23] = objAsistenciaGen.getPldia19() == null ? "0" : objAsistenciaGen.getPldia19();
                listaDet[i][24] = objAsistenciaGen.getPldia20() == null ? "0" : objAsistenciaGen.getPldia20();
                listaDet[i][25] = objAsistenciaGen.getPldia21() == null ? "0" : objAsistenciaGen.getPldia21();
                listaDet[i][26] = objAsistenciaGen.getPldia22() == null ? "0" : objAsistenciaGen.getPldia22();
                listaDet[i][27] = objAsistenciaGen.getPldia23() == null ? "0" : objAsistenciaGen.getPldia23();
                listaDet[i][28] = objAsistenciaGen.getPldia24() == null ? "0" : objAsistenciaGen.getPldia24();
                listaDet[i][29] = objAsistenciaGen.getPldia25() == null ? "0" : objAsistenciaGen.getPldia25();
                listaDet[i][30] = objAsistenciaGen.getPldia26() == null ? "0" : objAsistenciaGen.getPldia26();
                listaDet[i][31] = objAsistenciaGen.getPldia27() == null ? "0" : objAsistenciaGen.getPldia27();
                listaDet[i][32] = objAsistenciaGen.getPldia28() == null ? "0" : objAsistenciaGen.getPldia28();
                listaDet[i][33] = objAsistenciaGen.getPldia29() == null ? "0" : objAsistenciaGen.getPldia29();
                listaDet[i][34] = objAsistenciaGen.getPldia30() == null ? "0" : objAsistenciaGen.getPldia30();
                listaDet[i][35] = objAsistenciaGen.getPldia31() == null ? "0" : objAsistenciaGen.getPldia31();
                listaDet[i][36] = objUsuCredential.getCuenta();
                listaDet[i][37] = objListaAsis.get(i).getPlas_fecmod();
                listaDet[i][38] = objListaAsis.get(i).getCoo_accion();

            }
        }

        return listaDet;

    }
    /*
     //@Listen("onClick=#btn_guardar")
     public void botonGuardar() {

     s_mensaje = "Está seguro que desea guardar los cambios?";
     Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
     Messagebox.QUESTION, new EventListener() {
     @Override
     public void onEvent(Event event) throws Exception {
     if (((Integer) event.getData()).intValue() == Messagebox.OK) {

     ParametrosSalida objParamAsis;

     if (s_estado.equals("M")) {
     objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGen), getLov(objlstLov));//
     } else {
     //    objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGenDet));}
     objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGen), getLov(objlstLov));//
     }

     if (objParamAsis.getFlagIndicador() == 0) {
     //                          
     // LimpiarListaDetalle();
     }
     Messagebox.show(objParamAsis.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
     // tbbtn_btn_guardar.setDisabled(true);
     // tbbtn_btn_deshacer.setDisabled(true);
     //
     // habilitaTab(false, false);
     // seleccionaTab(true, false);
     //habilitaBotones(false, true);
     // limpiarCampos();
     // limpiaAuditoria();
     // habilitaRadioGroup(true);
     //  limpiarFechas();
     //  limpiarCamposLoc();
     rbg_asisgen.setSelectedItem(null);
     // objlstAsistenciaGen = objdaoAsistenciaGen.listaAsistencia(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), Utilitarios.periodoActual());
     // lst_asistenciagen.setModel(objlstAsistenciaGen);
     objAsistenciaGen = new AsistenciaGen();
     lst_asistenciagen.focus();
     s_estado = "Q";

     }
     }
     });

     }

     */

    //@Listen("onClick=#btn_guardar")
    public void botonGuardar() throws SQLException {

        ParametrosSalida objParamAsis;

        if (s_estado.equals("M")) {
            objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGen), getLov(objlstLov));//
        } else {

            objParamAsis = objdaoAsistenciaGen.modificarAsistencia(getDetalle(objlstAsistenciaGen), getLov(objlstLov));//
        }

        if (objParamAsis.getFlagIndicador() == 0) {
        }
        Messagebox.show(objParamAsis.getMsgValidacion(), "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        rbg_asisgen.setSelectedItem(null);
        objAsistenciaGen = new AsistenciaGen();
        lst_asistenciagen.focus();
        s_estado = "Q";

    }

    public Object[][] getLov(ListModelList<AsistenciaGen> x) {
        ListModelList<AsistenciaGen> objListaDepurada3;

        objListaDepurada3 = x;

        Object[][] listaLov = new Object[objListaDepurada3.size()][15];
        for (int i = 0; i < objListaDepurada3.size(); i++) {
            listaLov[i][0] = objUsuCredential.getCodemp();
            listaLov[i][1] = objAsistenciaGen.getSuc_id();
            listaLov[i][2] = objAsistenciaGen.getPeriodo();//objListaAsis.get(i).getPeriodo();
            listaLov[i][3] = objAsistenciaGen.getPltipdoc();//objListaAsis.get(i).getPltipdoc();
            listaLov[i][4] = objAsistenciaGen.getPlnrodoc();///objListaAsis.get(i).getPlnrodoc();
            listaLov[i][5] = "N";
            listaLov[i][6] = objListaDepurada3.get(i).getCod_subsidio();
            listaLov[i][7] = "";
            listaLov[i][8] = new java.sql.Date(objListaDepurada3.get(i).getFinicio().getTime());///d_fecini.getValue();
            listaLov[i][9] = new java.sql.Date(objListaDepurada3.get(i).getFfinal().getTime());//d_fecfin.getValue();
            listaLov[i][10] = "";
            listaLov[i][11] = "";
            listaLov[i][12] = objUsuCredential.getCuenta();
            listaLov[i][13] = objUsuCredential.getComputerName();
            listaLov[i][14] = objListaDepurada3.get(i).getGlosa();

        }
        return listaLov;
    }

    public void limpiarFechas() {
        d_fecini.setValue(null);
        d_fecfin.setValue(null);
    }

    public void limpiarCheck() {
        rb_0.setChecked(false);
        rb_2.setChecked(false);
        rb_7.setChecked(false);

    }
}
