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
public class ManTablas {

    private String codigo;
    private String descripcion;
    private String tipo;
    private String periodo;
    private double defaul;
    private String usu_add;
    private String usu_mod;
    private Date dia_add;
    private Date dia_mod;
    private int estado;
    private double enero;
    private double febrero;
    private double marzo;
    private double abril;
    private double mayo;
    private double junio;
    private double julio;
    private double agosto;
    private double septiembre;
    private double octubre;
    private double noviembre;
    private double diciembre;
    private String anio;
    private boolean valor;
    private String tipo_valor;
    private String mes;
    private String ind_accion = "Q";

    public ManTablas() {
    }

    public ManTablas(String codigo, String descripcion, String tipo, String periodo, int estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.periodo = periodo;
        this.estado = estado;
    }

    public ManTablas(String codigo, String descripcion, String tipo) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public ManTablas(String codigo, String descripcion, String tipo, String periodo, double defaul, int estado, double enero, double febrero, double marzo, double abril, double mayo, double junio, double julio, double agosto, double septiembre, double octubre, double noviembre, double diciembre) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.periodo = periodo;
        this.defaul = defaul;
        this.estado = estado;
        this.enero = enero;
        this.febrero = febrero;
        this.marzo = marzo;
        this.abril = abril;
        this.mayo = mayo;
        this.junio = junio;
        this.julio = julio;
        this.agosto = agosto;
        this.septiembre = septiembre;
        this.octubre = octubre;
        this.noviembre = noviembre;
        this.diciembre = diciembre;
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
        /* if (tipo.equals("M")) {
         tipo="MONTO";
         }else{
         tipo="TASA";
         }*/
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public double getEnero() {
        return enero;
    }

    public void setEnero(double enero) {
        this.enero = enero;
    }

    public double getFebrero() {
        return febrero;
    }

    public void setFebrero(double febrero) {
        this.febrero = febrero;
    }

    public double getMarzo() {
        return marzo;
    }

    public void setMarzo(double marzo) {
        this.marzo = marzo;
    }

    public double getAbril() {
        return abril;
    }

    public void setAbril(double abril) {
        this.abril = abril;
    }

    public double getMayo() {
        return mayo;
    }

    public void setMayo(double mayo) {
        this.mayo = mayo;
    }

    public double getJunio() {
        return junio;
    }

    public void setJunio(double junio) {
        this.junio = junio;
    }

    public double getJulio() {
        return julio;
    }

    public void setJulio(double julio) {
        this.julio = julio;
    }

    public double getAgosto() {
        return agosto;
    }

    public void setAgosto(double agosto) {
        this.agosto = agosto;
    }

    public double getSeptiembre() {
        return septiembre;
    }

    public void setSeptiembre(double septiembre) {
        this.septiembre = septiembre;
    }

    public double getOctubre() {
        return octubre;
    }

    public void setOctubre(double octubre) {
        this.octubre = octubre;
    }

    public double getNoviembre() {
        return noviembre;
    }

    public void setNoviembre(double noviembre) {
        this.noviembre = noviembre;
    }

    public double getDiciembre() {
        return diciembre;
    }

    public void setDiciembre(double diciembre) {
        this.diciembre = diciembre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getDefaul() {
        return defaul;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setDefaul(double defaul) {
        this.defaul = defaul;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    /* public String getTipo_valor() {
     if (tipo.equals("MONTO") ) {
     tipo_valor="M";
     }else {
     tipo_valor="T";
     }
 
     return tipo_valor;
     }*/
    public String getTipo_valor() {
        if (tipo.equals("MONTO")) {
            tipo_valor = "M";
        } else if (tipo.equals("TASA")) {
            tipo_valor = "T";
        } else if (tipo.equals("T")) {
            tipo_valor = "TASA";
        } else if (tipo.equals("M")) {
            tipo_valor = "MONTO";
        } else{
            tipo_valor="";
        }

        return tipo_valor;
    }

    public void setTipo_valor(String tipo_valor) {
        this.tipo_valor = tipo_valor;
    }

}
