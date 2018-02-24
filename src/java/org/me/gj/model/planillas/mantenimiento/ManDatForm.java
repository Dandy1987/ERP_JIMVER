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
public class ManDatForm {
  
    private String codigo;
    private String view;
    private String record;
    private String usu_add;
    private String usu_mod;
    private Date dia_add;
    private Date dia_mod;
    private int estado;
    private boolean valor;
    private String identificador;

    public ManDatForm() {
    }

    public ManDatForm(String codigo, String view, String record, int estado) {
        this.codigo = codigo;
        this.view = view;
        this.record = record;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
    
}
