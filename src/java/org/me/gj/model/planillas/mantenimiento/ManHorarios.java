/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author achocano
 */
public class ManHorarios {

    private String sucursal;
    private String horario;

    private String tipo_dia;
    private String ingreso;
    private String sal_ref;
    private String ing_ref;
    private String salida;
    //mantenimiento
    private String man_codigo;
    private int man_sucursal;
    private String man_descripcion;
    private String man_codigo_semanal;
    private String tipo;
    private String man_amanecida;
    private int man_rango;
    private int man_hr_trabajadas;
    private int man_hr_refrigerio;
    private int man_hr_tolerancia;
    private String man_hr_semanal;
    private String man_de;
    private String man_a;
    private String man_descanso;
    private String man_iempresa;
    private String man_srefrigerio;
    private String man_irefrigerio;
    private String man_sempresa;
    private String man_lun1;
    private String man_lun2;
    private String man_lun3;
    private String man_lun4;
    private String man_mar1;
    private String man_mar2;
    private String man_mar3;
    private String man_mar4;
    private String man_mier1;
    private String man_mier2;
    private String man_mier3;
    private String man_mier4;
    private String man_juev1;
    private String man_juev2;
    private String man_juev3;
    private String man_juev4;
    private String man_vie1;
    private String man_vie2;
    private String man_vie3;
    private String man_vie4;
    private String man_sab1;
    private String man_sab2;
    private String man_sab3;
    private String man_sab4;
    private String man_dom1;
    private String man_dom2;
    private String man_dom3;
    private String man_dom4;
    private int man_ant1;
    private int man_ant2;
    private int man_ant3;
    private int man_ant4;
    private int man_des1;
    private int man_des2;
    private int man_des3;
    private int man_des4;
    private boolean ama_valor;
    private boolean iempresa_valor;
    private boolean srefrigerio_valor;
    private boolean irefrigerio_valor;
    private boolean sempresa_valor;
    private String usu_add;
    private String usu_mod;
    private Date dia_add;
    private Date dia_mod;
    private int marcado;
    private String dia_inicio;
    private String dia_descanso;
    private String s_sucursal;

    private String hor_tiphora;

    public ManHorarios() {
    }

    public ManHorarios(String man_codigo, int man_sucursal, String man_descripcion, String tipo, String man_amanecida, int man_rango, int man_hr_trabajadas, int man_hr_refrigerio, int man_hr_tolerancia, String man_hr_semanal, String man_de, String man_descanso, String man_iempresa, String man_srefrigerio, String man_irefrigerio, String man_sempresa, String man_lun1, String man_lun2, String man_lun3, String man_lun4, String man_mar1, String man_mar2, String man_mar3, String man_mar4, String man_mier1, String man_mier2, String man_mier3, String man_mier4, String man_juev1, String man_juev2, String man_juev3, String man_juev4, String man_vie1, String man_vie2, String man_vie3, String man_vie4, String man_sab1, String man_sab2, String man_sab3, String man_sab4, String man_dom1, String man_dom2, String man_dom3, String man_dom4, int man_ant1, int man_ant2, int man_ant3, int man_ant4, int man_des1, int man_des2, int man_des3, int man_des4,int marcado) {
        this.man_codigo = man_codigo;
        this.man_sucursal = man_sucursal;
        this.man_descripcion = man_descripcion;
      
        this.tipo = tipo;
        this.man_amanecida = man_amanecida;
        this.man_rango = man_rango;
        this.man_hr_trabajadas = man_hr_trabajadas;
        this.man_hr_refrigerio = man_hr_refrigerio;
        this.man_hr_tolerancia = man_hr_tolerancia;
        this.man_hr_semanal = man_hr_semanal;
        this.man_de = man_de;
        this.man_descanso = man_descanso;
        this.man_iempresa = man_iempresa;
        this.man_srefrigerio = man_srefrigerio;
        this.man_irefrigerio = man_irefrigerio;
        this.man_sempresa = man_sempresa;
        this.man_lun1 = man_lun1;
        this.man_lun2 = man_lun2;
        this.man_lun3 = man_lun3;
        this.man_lun4 = man_lun4;
        this.man_mar1 = man_mar1;
        this.man_mar2 = man_mar2;
        this.man_mar3 = man_mar3;
        this.man_mar4 = man_mar4;
        this.man_mier1 = man_mier1;
        this.man_mier2 = man_mier2;
        this.man_mier3 = man_mier3;
        this.man_mier4 = man_mier4;
        this.man_juev1 = man_juev1;
        this.man_juev2 = man_juev2;
        this.man_juev3 = man_juev3;
        this.man_juev4 = man_juev4;
        this.man_vie1 = man_vie1;
        this.man_vie2 = man_vie2;
        this.man_vie3 = man_vie3;
        this.man_vie4 = man_vie4;
        this.man_sab1 = man_sab1;
        this.man_sab2 = man_sab2;
        this.man_sab3 = man_sab3;
        this.man_sab4 = man_sab4;
        this.man_dom1 = man_dom1;
        this.man_dom2 = man_dom2;
        this.man_dom3 = man_dom3;
        this.man_dom4 = man_dom4;
        this.man_ant1 = man_ant1;
        this.man_ant2 = man_ant2;
        this.man_ant3 = man_ant3;
        this.man_ant4 = man_ant4;
        this.man_des1 = man_des1;
        this.man_des2 = man_des2;
        this.man_des3 = man_des3;
        this.man_des4 = man_des4;
        this.marcado= marcado;
    }
    

    public ManHorarios(String sucursal, String horario, String tipo, String tipo_dia, String ingreso, String sal_ref, String ing_ref, String salida) {
        this.sucursal = sucursal;
        this.horario = horario;
        this.tipo = tipo;
        this.tipo_dia = tipo_dia;
        this.ingreso = ingreso;
        this.sal_ref = sal_ref;
        this.ing_ref = ing_ref;
        this.salida = salida;
    }

    public int getMan_sucursal() {
        return man_sucursal;
    }

    public void setMan_sucursal(int man_sucursal) {
        this.man_sucursal = man_sucursal;
    }

    public String getMan_codigo() {
        return man_codigo;
    }

    public void setMan_codigo(String man_codigo) {
        this.man_codigo = man_codigo;
    }

    public String getMan_descripcion() {
        return man_descripcion;
    }

    public void setMan_descripcion(String man_descripcion) {
        this.man_descripcion = man_descripcion;
    }

    public String getMan_amanecida() {
        return man_amanecida;
    }

    public void setMan_amanecida(String man_amanecida) {
        this.man_amanecida = man_amanecida;
    }

    public int getMan_rango() {
        return man_rango;
    }

    public void setMan_rango(int man_rango) {
        this.man_rango = man_rango;
    }

    public int getMan_hr_trabajadas() {
        return man_hr_trabajadas;
    }

    public void setMan_hr_trabajadas(int man_hr_trabajadas) {
        this.man_hr_trabajadas = man_hr_trabajadas;
    }

    public int getMan_hr_refrigerio() {
        return man_hr_refrigerio;
    }

    public void setMan_hr_refrigerio(int man_hr_refrigerio) {
        this.man_hr_refrigerio = man_hr_refrigerio;
    }

    public int getMan_hr_tolerancia() {
        return man_hr_tolerancia;
    }

    public void setMan_hr_tolerancia(int man_hr_tolerancia) {
        this.man_hr_tolerancia = man_hr_tolerancia;
    }

    public String getMan_hr_semanal() {
        return man_hr_semanal;
    }

    public void setMan_hr_semanal(String man_hr_semanal) {
        this.man_hr_semanal = man_hr_semanal;
    }

    public String getMan_de() {
        return man_de;
    }

    public void setMan_de(String man_de) {
        this.man_de = man_de;
    }

    public String getMan_a() {
        return man_a;
    }

    public void setMan_a(String man_a) {
        this.man_a = man_a;
    }

    public String getMan_descanso() {
        return man_descanso;
    }

    public void setMan_descanso(String man_descanso) {
        this.man_descanso = man_descanso;
    }

    public String getMan_iempresa() {
        return man_iempresa;
    }

    public void setMan_iempresa(String man_iempresa) {
        this.man_iempresa = man_iempresa;
    }

    public String getMan_srefrigerio() {
        return man_srefrigerio;
    }

    public void setMan_srefrigerio(String man_srefrigerio) {
        this.man_srefrigerio = man_srefrigerio;
    }

    public String getMan_irefrigerio() {
        return man_irefrigerio;
    }

    public void setMan_irefrigerio(String man_irefrigerio) {
        this.man_irefrigerio = man_irefrigerio;
    }

    public String getMan_sempresa() {
        return man_sempresa;
    }

    public void setMan_sempresa(String man_sempresa) {
        this.man_sempresa = man_sempresa;
    }

    public String getMan_lun1() {
        return man_lun1;
    }

    public void setMan_lun1(String man_lun1) {
        this.man_lun1 = man_lun1;
    }

    public String getMan_mar1() {
        return man_mar1;
    }

    public void setMan_mar1(String man_mar1) {
        this.man_mar1 = man_mar1;
    }

    public String getMan_mier1() {
        return man_mier1;
    }

    public void setMan_mier1(String man_mier1) {
        this.man_mier1 = man_mier1;
    }

    public String getMan_juev1() {
        return man_juev1;
    }

    public void setMan_juev1(String man_juev1) {
        this.man_juev1 = man_juev1;
    }

    public String getMan_vie1() {
        return man_vie1;
    }

    public void setMan_vie1(String man_vie1) {
        this.man_vie1 = man_vie1;
    }

    public String getMan_sab1() {
        return man_sab1;
    }

    public void setMan_sab1(String man_sab1) {
        this.man_sab1 = man_sab1;
    }

    public String getMan_dom1() {
        return man_dom1;
    }

    public void setMan_dom1(String man_dom1) {
        this.man_dom1 = man_dom1;
    }

    public int getMan_ant1() {
        return man_ant1;
    }

    public void setMan_ant1(int man_ant1) {
        this.man_ant1 = man_ant1;
    }

    public int getMan_des1() {
        return man_des1;
    }

    public void setMan_des1(int man_des1) {
        this.man_des1 = man_des1;
    }

    public String getMan_lun2() {
        return man_lun2;
    }

    public void setMan_lun2(String man_lun2) {
        this.man_lun2 = man_lun2;
    }

    public String getMan_mar2() {
        return man_mar2;
    }

    public void setMan_mar2(String man_mar2) {
        this.man_mar2 = man_mar2;
    }

    public String getMan_mier2() {
        return man_mier2;
    }

    public void setMan_mier2(String man_mier2) {
        this.man_mier2 = man_mier2;
    }

    public String getMan_juev2() {
        return man_juev2;
    }

    public void setMan_juev2(String man_juev2) {
        this.man_juev2 = man_juev2;
    }

    public String getMan_vie2() {
        return man_vie2;
    }

    public void setMan_vie2(String man_vie2) {
        this.man_vie2 = man_vie2;
    }

    public String getMan_sab2() {
        return man_sab2;
    }

    public void setMan_sab2(String man_sab2) {
        this.man_sab2 = man_sab2;
    }

    public String getMan_dom2() {
        return man_dom2;
    }

    public void setMan_dom2(String man_dom2) {
        this.man_dom2 = man_dom2;
    }

    public int getMan_ant2() {
        return man_ant2;
    }

    public void setMan_ant2(int man_ant2) {
        this.man_ant2 = man_ant2;
    }

    public int getMan_des2() {
        return man_des2;
    }

    public void setMan_des2(int man_des2) {
        this.man_des2 = man_des2;
    }

    public String getMan_lun3() {
        return man_lun3;
    }

    public void setMan_lun3(String man_lun3) {
        this.man_lun3 = man_lun3;
    }

    public String getMan_mar3() {
        return man_mar3;
    }

    public void setMan_mar3(String man_mar3) {
        this.man_mar3 = man_mar3;
    }

    public String getMan_mier3() {
        return man_mier3;
    }

    public void setMan_mier3(String man_mier3) {
        this.man_mier3 = man_mier3;
    }

    public String getMan_juev3() {
        return man_juev3;
    }

    public void setMan_juev3(String man_juev3) {
        this.man_juev3 = man_juev3;
    }

    public String getMan_vie3() {
        return man_vie3;
    }

    public void setMan_vie3(String man_vie3) {
        this.man_vie3 = man_vie3;
    }

    public String getMan_sab3() {
        return man_sab3;
    }

    public void setMan_sab3(String man_sab3) {
        this.man_sab3 = man_sab3;
    }

    public String getMan_dom3() {
        return man_dom3;
    }

    public void setMan_dom3(String man_dom3) {
        this.man_dom3 = man_dom3;
    }

    public int getMan_ant3() {
        return man_ant3;
    }

    public void setMan_ant3(int man_ant3) {
        this.man_ant3 = man_ant3;
    }

    public int getMan_des3() {
        return man_des3;
    }

    public void setMan_des3(int man_des3) {
        this.man_des3 = man_des3;
    }

    public String getMan_lun4() {
        return man_lun4;
    }

    public void setMan_lun4(String man_lun4) {
        this.man_lun4 = man_lun4;
    }

    public String getMan_mar4() {
        return man_mar4;
    }

    public void setMan_mar4(String man_mar4) {
        this.man_mar4 = man_mar4;
    }

    public String getMan_mier4() {
        return man_mier4;
    }

    public void setMan_mier4(String man_mier4) {
        this.man_mier4 = man_mier4;
    }

    public String getMan_juev4() {
        return man_juev4;
    }

    public void setMan_juev4(String man_juev4) {
        this.man_juev4 = man_juev4;
    }

    public String getMan_vie4() {
        return man_vie4;
    }

    public void setMan_vie4(String man_vie4) {
        this.man_vie4 = man_vie4;
    }

    public String getMan_sab4() {
        return man_sab4;
    }

    public void setMan_sab4(String man_sab4) {
        this.man_sab4 = man_sab4;
    }

    public String getMan_dom4() {
        return man_dom4;
    }

    public void setMan_dom4(String man_dom4) {
        this.man_dom4 = man_dom4;
    }

    public int getMan_ant4() {
        return man_ant4;
    }

    public void setMan_ant4(int man_ant4) {
        this.man_ant4 = man_ant4;
    }

    public int getMan_des4() {
        return man_des4;
    }

    public void setMan_des4(int man_des4) {
        this.man_des4 = man_des4;
    }

    public boolean isAma_valor() {
        if (man_amanecida.equals("N")) {
            ama_valor = true;
        } else {
            ama_valor = false;
        }
        return ama_valor;
    }

    public void setAma_valor(boolean ama_valor) {
        this.ama_valor = ama_valor;
    }

    public boolean isIempresa_valor() {
        if (man_iempresa.equals("1")) {
            iempresa_valor = true;
        } else {
            iempresa_valor = false;
        }
        return iempresa_valor;
    }

    public void setIempresa_valor(boolean iempresa_valor) {
        this.iempresa_valor = iempresa_valor;
    }

    public boolean isSrefrigerio_valor() {
        if (man_srefrigerio.equals("1")) {
            srefrigerio_valor = true;
        } else {
            srefrigerio_valor = false;
        }

        return srefrigerio_valor;
    }

    public void setSrefrigerio_valor(boolean srefrigerio_valor) {
        this.srefrigerio_valor = srefrigerio_valor;
    }

    public boolean isIrefrigerio_valor() {

        if (man_irefrigerio.equals("1")) {
            irefrigerio_valor = true;
        } else {
            irefrigerio_valor = false;
        }
        return irefrigerio_valor;
    }

    public void setIrefrigerio_valor(boolean irefrigerio_valor) {
        this.irefrigerio_valor = irefrigerio_valor;
    }

    public boolean isSempresa_valor() {
        if (man_sempresa.equals("1")) {
            sempresa_valor = true;
        } else {
            sempresa_valor = false;
        }
        return sempresa_valor;
    }

    public void setSempresa_valor(boolean sempresa_valor) {
        this.sempresa_valor = sempresa_valor;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo_dia() {
        return tipo_dia;
    }

    public void setTipo_dia(String tipo_dia) {
        this.tipo_dia = tipo_dia;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getSal_ref() {
        return sal_ref;
    }

    public void setSal_ref(String sal_ref) {
        this.sal_ref = sal_ref;
    }

    public String getIng_ref() {
        return ing_ref;
    }

    public void setIng_ref(String ing_ref) {
        this.ing_ref = ing_ref;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getUsu_add() {
        return usu_add;
    }

    public void setUsu_add(String usu_add) {
        this.usu_add = usu_add;
    }

    public String getUsu_mod() {
        return usu_mod;
    }

    public void setUsu_mod(String usu_mod) {
        this.usu_mod = usu_mod;
    }

    public Date getDia_add() {
        return dia_add;
    }

    public void setDia_add(Date dia_add) {
        this.dia_add = dia_add;
    }

    public Date getDia_mod() {
        return dia_mod;
    }

    public void setDia_mod(Date dia_mod) {
        this.dia_mod = dia_mod;
    }

    public String getMan_codigo_semanal() {
        return man_codigo_semanal;
    }

    public void setMan_codigo_semanal(String man_codigo_semanal) {
        this.man_codigo_semanal = man_codigo_semanal;
    }

    public int getMarcado() {
        return marcado;
    }

    public void setMarcado(int marcado) {
        this.marcado = marcado;
    }

    public String getDia_inicio() {
        return dia_inicio;
    }

    public void setDia_inicio(String dia_inicio) {
        this.dia_inicio = dia_inicio;
    }

    public String getDia_descanso() {
        return dia_descanso;
    }

    public void setDia_descanso(String dia_descanso) {
        this.dia_descanso = dia_descanso;
    }

    public String getS_sucursal() {
        return s_sucursal;
    }

    public void setS_sucursal(String s_sucursal) {
        this.s_sucursal = s_sucursal;
    }

    public String getHor_tiphora() {
        return hor_tiphora;
    }

    public void setHor_tiphora(String hor_tiphora) {
        this.hor_tiphora = hor_tiphora;
    }
    
}
