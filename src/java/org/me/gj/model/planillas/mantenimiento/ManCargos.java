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


public class ManCargos {
    private String cargo_id;
    private String cargo_des;
    private int cargo_est;
    private String cargo_usuadd;
    private Date cargo_fecadd;
    private String cargo_usumod;
    private Date cargo_fecmod;
    private int suc_id;
    private int emp_id;
    private boolean valor;
    
    
    public ManCargos(){
    }
    
    public ManCargos(String cargo_id,String cargo_des,int cargo_est,String cargo_usuadd,String cargo_usumod,int emp_id,int suc_id ){
    this.cargo_id = cargo_id;
    this.cargo_des = cargo_des;
    this.cargo_est = cargo_est;
    this.cargo_usuadd = cargo_usuadd;
    this.cargo_usumod = cargo_usumod;
    this.emp_id = emp_id;
    this.suc_id = suc_id;
    }

   

    public String getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(String cargo_id) {
        this.cargo_id = cargo_id;
    }

    public String getCargo_des() {
        return cargo_des;
    }

    public void setCargo_des(String cargo_des) {
        this.cargo_des = cargo_des;
    }

    public int getCargo_est() {
        return cargo_est;
    }

    public void setCargo_est(int cargo_est) {
        this.cargo_est = cargo_est;
    }

    public String getCargo_usuadd() {
        return cargo_usuadd;
    }

    public void setCargo_usuadd(String cargo_usuadd) {
        this.cargo_usuadd = cargo_usuadd;
    }

    public Date getCargo_fecadd() {
        return cargo_fecadd;
    }

    public void setCargo_fecadd(Date cargo_fecadd) {
        this.cargo_fecadd = cargo_fecadd;
    }

    public String getCargo_usumod() {
        return cargo_usumod;
    }

    public void setCargo_usumod(String cargo_usumod) {
        this.cargo_usumod = cargo_usumod;
    }

    public Date getCargo_fecmod() {
        return cargo_fecmod;
    }

    public void setCargo_fecmod(Date cargo_fecmod) {
        this.cargo_fecmod = cargo_fecmod;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public boolean isValor() {  
        valor = cargo_est == 1;
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
    private boolean valSelec;
}
