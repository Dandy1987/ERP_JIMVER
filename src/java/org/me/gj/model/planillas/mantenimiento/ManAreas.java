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
public class ManAreas {

    private String area_id;
    private String area_des;
    private int area_est;
    private String area_cod_costo;
    private String area_des_costo;
    private String area_usuadd;
    private Date area_fecadd;
    private String area_usumod;
    private Date area_fecmod;
    private boolean valor;
    private boolean valSelec;
    public ManAreas() {

    }
    
    public ManAreas(String area_id,String area_des,int area_est,String area_cod_costo,String area_usuadd,String area_usumod){
        this.area_id = area_id;
        this.area_des = area_des;
        this.area_est = area_est;
        this.area_cod_costo = area_cod_costo;
        this.area_usuadd = area_usuadd;
        this.area_usumod = area_usumod;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_des() {
        return area_des;
    }

    public void setArea_des(String area_des) {
        this.area_des = area_des;
    }

    public int getArea_est() {
        return area_est;
    }

    public void setArea_est(int area_est) {
        this.area_est = area_est;
    }

    public String getArea_cod_costo() {
        return area_cod_costo;
    }

    public void setArea_cod_costo(String area_cod_costo) {
        this.area_cod_costo = area_cod_costo;
    }

    public String getArea_des_costo() {
        return area_des_costo;
    }

    public void setArea_des_costo(String area_des_costo) {
        this.area_des_costo = area_des_costo;
    }

    public String getArea_usuadd() {
        return area_usuadd;
    }

    public void setArea_usuadd(String area_usuadd) {
        this.area_usuadd = area_usuadd;
    }

    public Date getArea_fecadd() {
        return area_fecadd;
    }

    public void setArea_fecadd(Date area_fecadd) {
        this.area_fecadd = area_fecadd;
    }

    public String getArea_usumod() {
        return area_usumod;
    }

    public void setArea_usumod(String area_usumod) {
        this.area_usumod = area_usumod;
    }

    public Date getArea_fecmod() {
        return area_fecmod;
    }

    public void setArea_fecmod(Date area_fecmod) {
        this.area_fecmod = area_fecmod;
    }

    public boolean isValor() {
        valor = area_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
    
    

}
