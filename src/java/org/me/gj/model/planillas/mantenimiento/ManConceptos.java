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
public class ManConceptos {

    private String codigo;
    private String descripcion;
    private String tipo;
    private int prioridad;
    private String debe;
    private String haber;
    private int boleta;
    private String cod_sunat;
    private String usu_add;
    private String usu_mod;
    private Date dia_add;
    private Date dia_mod;
    private int estado;
    private boolean valor;
    private String s_tipo;
    private String s_boleta;
    private String g_tipo;
    private String v_boleta;
    private String s_prioridad;

    public ManConceptos() {
    }

    public ManConceptos(String codigo, String descripcion, String tipo, int prioridad, String debe, String haber, int boleta, String cod_sunat, int estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.debe = debe;
        this.haber = haber;
        this.boleta = boleta;
        this.cod_sunat = cod_sunat;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    public String getHaber() {
        return haber;
    }

    public void setHaber(String haber) {
        this.haber = haber;
    }

    public int getBoleta() {

        return boleta;
    }

    public void setBoleta(int boleta) {
        this.boleta = boleta;
    }

    public String getCod_sunat() {
        return cod_sunat;
    }

    public void setCod_sunat(String cod_sunat) {
        this.cod_sunat = cod_sunat;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getS_tipo() {
        if (tipo.equals(" ")) {
            s_tipo = " ";
        } else if (tipo.equals("M")) {
            s_tipo = "CONSTANTE MENSUAL";
        } else if (tipo.equals("F")) {
            s_tipo = "FUNCION";
        } else {
            s_tipo = "CONSTANTE";
        }

        return s_tipo;
    }

    public void setS_tipo(String s_tipo) {
        this.s_tipo = s_tipo;
    }

    public boolean isValor() {
        if (estado == 1) {
            valor = true;
        } else {
            valor = false;
        }

        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getS_boleta() {
        if (boleta == 0) {
            s_boleta = "1";
        } else if (boleta == 1) {
            s_boleta = "2";
        } else if (boleta == 2) {
            s_boleta = "3";
        } else if (boleta == 3) {
            s_boleta = "4";
        } else if (boleta == 4) {
            s_boleta = "5";
        } else if (boleta == 5) {
            s_boleta = "6";
        } else if (boleta == 6) {
            s_boleta = "7";
        }

        return s_boleta;
    }

    public void setS_boleta(String s_boleta) {
        this.s_boleta = s_boleta;
    }

    public String getG_tipo() {
        if (tipo.equals("FUNCION")) {
            g_tipo = "F";
        } else if (tipo.equals("CONSTANTE")) {
            g_tipo = "C";
        } else {
            g_tipo = "M";

        }
        return g_tipo;
    }

    public void setG_tipo(String g_tipo) {
        this.g_tipo = g_tipo;
    }

    public String getV_boleta() {
        if (s_boleta.equals("1")) {
            v_boleta = "INGRESO";
        } else if (s_boleta.equals("2")) {
            v_boleta = "DESCUENTO";
        } else if (s_boleta.equals("3")) {
            v_boleta = "APORTE";
        } else if (s_boleta.equals("4")) {
            v_boleta = "TOTAL INGRESO";
        } else if (s_boleta.equals("5")) {
            v_boleta = "TOTAL DESCUENTO";
        } else if (s_boleta.equals("6")) {
            v_boleta = "NETO A PAGAR";
        } else if (s_boleta.equals("7")) {
            v_boleta = "INGRESO/DESCUENTO";
        }
        return v_boleta;
    }

    public void setV_boleta(String v_boleta) {
        this.v_boleta = v_boleta;
    }

    public String getS_prioridad() {
        if (prioridad == 0) {
            s_prioridad = "0";
        } else if (prioridad == 1) {
            s_prioridad = "1";
        } else if (prioridad == 2) {
            s_prioridad = "2";
        } else if (prioridad == 3) {
            s_prioridad = "3";
        }

        return s_prioridad;
    }

    public void setS_prioridad(String s_prioridad) {
        this.s_prioridad = s_prioridad;
    }

}
