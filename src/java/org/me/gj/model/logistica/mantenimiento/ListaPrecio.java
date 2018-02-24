package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class ListaPrecio {

    private int lp_key;
    private int emp_id;
    private int suc_id;
    private String lp_id;
    private long prov_key;
    private String prov_id;
    private String lp_des;
    private int lp_tipo;
    private int lp_est;
    private int lp_ord;
    private String lp_nomrep;
    private String lp_usuadd;
    private Date lp_fecadd;
    private String lp_usumod;
    private Date lp_fecmod;
    private boolean valor;
    private String prov_razsoc;
    private double lp_descgen;
    private double lp_descfinan;

    public ListaPrecio() {
    }

    public ListaPrecio(int lp_key, String lp_des) {
        this.lp_key = lp_key;
    }

    public ListaPrecio(int lp_key, int emp_id, int suc_id, long prov_key, String lp_des, int lp_est, double lp_descgen, double lp_descfinan, int lp_ord, String lp_nomrep, String lp_usuadd, String lp_usumod) {
        this.lp_key = lp_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.prov_key = prov_key;
        this.lp_des = lp_des;
        this.lp_tipo = 1;
        this.lp_descgen = lp_descgen;
        this.lp_descfinan = lp_descfinan;
        this.lp_est = lp_est;
        this.lp_ord = lp_ord;
        this.lp_nomrep = lp_nomrep;
        this.lp_usuadd = lp_usuadd;
        this.lp_usumod = lp_usumod;
    }

    public ListaPrecio(int lp_key, int emp_id, int suc_id, String lp_des, int lp_est, int lp_ord, String lp_nomrep, String lp_usuadd, String lp_usumod) {
        this.lp_key = lp_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.lp_des = lp_des;
        this.lp_tipo = 2;
        this.lp_est = lp_est;
        this.lp_ord = lp_ord;
        this.lp_nomrep = lp_nomrep;
        this.lp_usuadd = lp_usuadd;
        this.lp_usumod = lp_usumod;
    }

    public int getLp_key() {
        return lp_key;
    }

    public void setLp_key(int lp_key) {
        this.lp_key = lp_key;
    }

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

    public String getLp_id() {
        return lp_id;
    }

    public void setLp_id(String lp_id) {
        this.lp_id = lp_id;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getLp_des() {
        return lp_des;
    }

    public void setLp_des(String lp_des) {
        this.lp_des = lp_des;
    }

    public int getLp_tipo() {
        return lp_tipo;
    }

    public void setLp_tipo(int lp_tipo) {
        this.lp_tipo = lp_tipo;
    }

    public int getLp_est() {
        return lp_est;
    }

    public void setLp_est(int lp_est) {
        this.lp_est = lp_est;
    }

    public int getLp_ord() {
        return lp_ord;
    }

    public void setLp_ord(int lp_ord) {
        this.lp_ord = lp_ord;
    }

    public String getLp_nomrep() {
        return lp_nomrep;
    }

    public void setLp_nomrep(String lp_nomrep) {
        this.lp_nomrep = lp_nomrep;
    }

    public String getLp_usuadd() {
        return lp_usuadd;
    }

    public void setLp_usuadd(String lp_usuadd) {
        this.lp_usuadd = lp_usuadd;
    }

    public Date getLp_fecadd() {
        return lp_fecadd;
    }

    public void setLp_fecadd(Date lp_fecadd) {
        this.lp_fecadd = lp_fecadd;
    }

    public String getLp_usumod() {
        return lp_usumod;
    }

    public void setLp_usumod(String lp_usumod) {
        this.lp_usumod = lp_usumod;
    }

    public Date getLp_fecmod() {
        return lp_fecmod;
    }

    public void setLp_fecmod(Date lp_fecmod) {
        this.lp_fecmod = lp_fecmod;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public double getLp_descgen() {
        return lp_descgen;
    }

    public void setLp_descgen(double lp_descgen) {
        this.lp_descgen = lp_descgen;
    }

    public double getLp_descfinan() {
        return lp_descfinan;
    }

    public void setLp_descfinan(double lp_descfinan) {
        this.lp_descfinan = lp_descfinan;
    }

    public boolean isValor() {
        valor = lp_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
