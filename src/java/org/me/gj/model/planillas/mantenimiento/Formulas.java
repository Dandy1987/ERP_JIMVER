/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author wcamacho
 */
public class Formulas {

    private int emp_id;
    private int suc_id;
    private String form_id;
    private int form_estado;
    private String form_descri;
    private String form_contenido;
    private String form_calculo;
    private String form_sep_contenido;
    private String form_sep_calculo;
    private String form_detalle;
    private String form_usuadd;
    private Date form_fecadd;
    private String form_usumod;
    private Date form_fecmod;
    private boolean valor;

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public int getForm_estado() {
        return form_estado;
    }

    public void setForm_estado(int form_estado) {
        this.form_estado = form_estado;
    }

    

    public String getForm_contenido() {
        return form_contenido;
    }

    public void setForm_contenido(String form_contenido) {
        this.form_contenido = form_contenido;
    }

    public String getForm_calculo() {
        return form_calculo;
    }

    public void setForm_calculo(String form_calculo) {
        this.form_calculo = form_calculo;
    }

    public String getForm_sep_contenido() {
        return form_sep_contenido;
    }

    public void setForm_sep_contenido(String form_sep_contenido) {
        this.form_sep_contenido = form_sep_contenido;
    }

    public String getForm_sep_calculo() {
        return form_sep_calculo;
    }

    public void setForm_sep_calculo(String form_sep_calculo) {
        this.form_sep_calculo = form_sep_calculo;
    }

    public String getForm_detalle() {
        return form_detalle;
    }

    public void setForm_detalle(String form_detalle) {
        this.form_detalle = form_detalle;
    }

    public String getForm_usuadd() {
        return form_usuadd;
    }

    public void setForm_usuadd(String form_usuadd) {
        this.form_usuadd = form_usuadd;
    }

    public Date getForm_fecadd() {
        return form_fecadd;
    }

    public void setForm_fecadd(Date form_fecadd) {
        this.form_fecadd = form_fecadd;
    }

    public String getForm_usumod() {
        return form_usumod;
    }

    public void setForm_usumod(String form_usumod) {
        this.form_usumod = form_usumod;
    }

    public Date getForm_fecmod() {
        return form_fecmod;
    }

    public void setForm_fecmod(Date form_fecmod) {
        this.form_fecmod = form_fecmod;
    }

    public String getForm_descri() {
        return form_descri;
    }

    public void setForm_descri(String form_descri) {
        this.form_descri = form_descri;
    }

    public boolean isValor() {
        if (form_estado == 1) {
           valor = true; 
        }else {
            valor = false;
        }        
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
    
    
    
    
    
    
    
    
}
