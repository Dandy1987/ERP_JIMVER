package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class Ubicaciones {

    private int ubic_key;
    private String ubic_id;
    private String ubic_des;
    private int ubic_est;
    private double ubic_alt;
    private double ubic_lar;
    private double ubic_anc;
    private String ubic_nomrep;
    private int ubic_ord;
    private String ubic_usuadd;
    private Date ubic_fecadd;
    private String ubic_usumod;
    private Date ubic_fecmod;
    private int suc_id;
    private int emp_id;
    private int ubic_default;
    private boolean valor;

    private boolean valSelec;

    public Ubicaciones() {
    }

    public Ubicaciones(int ubic_key, String ubic_des, int ubic_est, double ubic_alt, double ubic_lar, double ubic_anc/*, String ubic_nomrep*/, int ubic_ord, String ubic_usuadd, String ubic_usumod, int suc_id, int emp_id, int ubic_default) {
        this.ubic_key = ubic_key;
        this.ubic_des = ubic_des;
        this.ubic_est = ubic_est;
        this.ubic_alt = ubic_alt;
        this.ubic_lar = ubic_lar;
        this.ubic_anc = ubic_anc;
        //this.ubic_nomrep = ubic_nomrep;
        this.ubic_ord = ubic_ord;
        this.ubic_usuadd = ubic_usuadd;
        this.ubic_usumod = ubic_usumod;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.ubic_default = ubic_default;
    }

    public int getUbic_key() {
        return ubic_key;
    }

    public void setUbic_key(int ubic_key) {
        this.ubic_key = ubic_key;
    }

    public String getUbic_id() {
        return ubic_id;
    }

    public void setUbic_id(String ubic_id) {
        this.ubic_id = ubic_id;
    }

    public String getUbic_des() {
        return ubic_des;
    }

    public void setUbic_des(String ubic_des) {
        this.ubic_des = ubic_des;
    }

    public int getUbic_est() {
        return ubic_est;
    }

    public void setUbic_est(int ubic_est) {
        this.ubic_est = ubic_est;
    }

    public double getUbic_alt() {
        return ubic_alt;
    }

    public void setUbic_alt(double ubic_alt) {
        this.ubic_alt = ubic_alt;
    }

    public double getUbic_lar() {
        return ubic_lar;
    }

    public void setUbic_lar(double ubic_lar) {
        this.ubic_lar = ubic_lar;
    }

    public double getUbic_anc() {
        return ubic_anc;
    }

    public void setUbic_anc(double ubic_anc) {
        this.ubic_anc = ubic_anc;
    }

    public String getUbic_nomrep() {
        return ubic_nomrep;
    }

    public void setUbic_nomrep(String ubic_nomrep) {
        this.ubic_nomrep = ubic_nomrep;
    }

    public int getUbic_ord() {
        return ubic_ord;
    }

    public void setUbic_ord(int ubic_ord) {
        this.ubic_ord = ubic_ord;
    }

    public String getUbic_usuadd() {
        return ubic_usuadd;
    }

    public void setUbic_usuadd(String ubic_usuadd) {
        this.ubic_usuadd = ubic_usuadd;
    }

    public Date getUbic_fecadd() {
        return ubic_fecadd;
    }

    public void setUbic_fecadd(Date ubic_fecadd) {
        this.ubic_fecadd = ubic_fecadd;
    }

    public String getUbic_usumod() {
        return ubic_usumod;
    }

    public void setUbic_usumod(String ubic_usumod) {
        this.ubic_usumod = ubic_usumod;
    }

    public Date getUbic_fecmod() {
        return ubic_fecmod;
    }

    public void setUbic_fecmod(Date ubic_fecmod) {
        this.ubic_fecmod = ubic_fecmod;
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
        if (ubic_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getUbic_default() {
        return ubic_default;
    }

    public void setUbic_default(int ubic_default) {
        this.ubic_default = ubic_default;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

}
