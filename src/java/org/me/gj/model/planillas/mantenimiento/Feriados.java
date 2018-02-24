/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author cpure
 */
public class Feriados {
    private String s_dia;
    private Date d_dia;
    private String s_diasemana;
    private boolean valSelec;
    private String s_dianum;
    private String s_mes;
    private String s_anho;

    public Feriados() {
    }

    public String getS_dia() {
        return s_dia;
    }

    public void setS_dia(String s_dia) {
        this.s_dia = s_dia;
    }

    public Date getD_dia() {
        return d_dia;
    }

    public void setD_dia(Date d_dia) {
        this.d_dia = d_dia;
    }

    public String getS_diasemana() {
        return s_diasemana;
    }

    public void setS_diasemana(String s_diasemana) {
        this.s_diasemana = s_diasemana;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getS_dianum() {
        return s_dianum;
    }

    public void setS_dianum(String s_dianum) {
        this.s_dianum = s_dianum;
    }

    public String getS_mes() {
        return s_mes;
    }

    public void setS_mes(String s_mes) {
        this.s_mes = s_mes;
    }

    public String getS_anho() {
        return s_anho;
    }

    public void setS_anho(String s_anho) {
        this.s_anho = s_anho;
    }

    
}
