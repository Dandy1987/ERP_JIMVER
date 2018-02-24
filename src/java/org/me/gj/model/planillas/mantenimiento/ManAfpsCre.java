/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author greyes
 */
public class ManAfpsCre {

    private String afp_id;
    private String afp_des;
    private int afp_est;
    private String afp_cod_enl;
    private String afp_cod_reg;
    private String afp_des_reg;
    private String afp_usuadd;
    private Date afp_fecadd;
    private String afp_usumod;
    private Date afp_fecmod;
    private boolean valor;

    public ManAfpsCre() {

    }

    public ManAfpsCre(String afp_id, String afp_des, int afp_est, String afp_cod_enl, String afp_cod_reg/*, String afp_des_reg*/, String afp_usuadd, String afp_usumod) {
        this.afp_id = afp_id;
        this.afp_des = afp_des;
        this.afp_est = afp_est;
        this.afp_cod_enl = afp_cod_enl;
        this.afp_cod_reg = afp_cod_reg;
        //this.afp_des_reg = afp_des_reg;
        this.afp_usuadd = afp_usuadd;
        this.afp_usumod = afp_usumod;

    }

    public String getAfp_id() {
        return afp_id;
    }

    public void setAfp_id(String afp_id) {
        this.afp_id = afp_id;
    }

    public String getAfp_des() {
        return afp_des;
    }

    public void setAfp_des(String afp_des) {
        this.afp_des = afp_des;
    }

    public int getAfp_est() {
        return afp_est;
    }

    public void setAfp_est(int afp_est) {
        this.afp_est = afp_est;
    }

    public String getAfp_cod_enl() {
        return afp_cod_enl;
    }

    public void setAfp_cod_enl(String afp_cod_enl) {
        this.afp_cod_enl = afp_cod_enl;
    }

    public String getAfp_cod_reg() {
        return afp_cod_reg;
    }

    public void setAfp_cod_reg(String afp_cod_reg) {
        this.afp_cod_reg = afp_cod_reg;
    }

    public String getAfp_des_reg() {
        return afp_des_reg;
    }

    public void setAfp_des_reg(String afp_des_reg) {
        this.afp_des_reg = afp_des_reg;
    }

    public String getAfp_usuadd() {
        return afp_usuadd;
    }

    public void setAfp_usuadd(String afp_usuadd) {
        this.afp_usuadd = afp_usuadd;
    }

    public Date getAfp_fecadd() {
        return afp_fecadd;
    }

    public void setAfp_fecadd(Date afp_fecadd) {
        this.afp_fecadd = afp_fecadd;
    }

    public String getAfp_usumod() {
        return afp_usumod;
    }

    public void setAfp_usumod(String afp_usumod) {
        this.afp_usumod = afp_usumod;
    }

    public Date getAfp_fecmod() {
        return afp_fecmod;
    }

    public void setAfp_fecmod(Date afp_fecmod) {
        this.afp_fecmod = afp_fecmod;
    }

    public boolean isValor() {
        valor = afp_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

}
