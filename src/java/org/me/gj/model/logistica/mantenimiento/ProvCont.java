package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class ProvCont {

    private int procon_id;
    private long prov_key;
    private String procon_nom;
    private String procon_ape;
    private String procon_ema;
    private String procon_usuadd;
    private Date procon_fecadd;
    private String procon_usumod;
    private Date procon_fecmod;
    private long procon_telef1;
    private long procon_telef2;
    private String procon_cargo;
    private String procon_suc;
    private int procon_est;
    private boolean val;
    private String prov_razsoc;
    private String ind_accion = "Q";

    public ProvCont(int procon_id, long prov_key, String procon_nom, String procon_ape, String procon_ema, String procon_cargo, String procon_suc, String procon_usuadd, String procon_usumod, long procon_telef1, long procon_telef2, int procon_est) {
        this.procon_id = procon_id;
        this.prov_key = prov_key;
        this.procon_nom = procon_nom;
        this.procon_ape = procon_ape;
        this.procon_ema = procon_ema;
        this.procon_cargo = procon_cargo;
        this.procon_suc = procon_suc;
        this.procon_usuadd = procon_usuadd;
        this.procon_usumod = procon_usumod;
        this.procon_telef1 = procon_telef1;
        this.procon_telef2 = procon_telef2;
        this.procon_est = procon_est;
    }

    public ProvCont() {
    }

    public String getProcon_ape() {
        return procon_ape;
    }

    public void setProcon_ape(String procon_ape) {
        this.procon_ape = procon_ape;
    }

    public String getProcon_ema() {
        return procon_ema;
    }

    public void setProcon_ema(String procon_ema) {
        this.procon_ema = procon_ema;
    }

    public int getProcon_est() {
        val = procon_est == 1;
        return procon_est;
    }

    public void setProcon_est(int procon_est) {
        this.procon_est = procon_est;
    }

    public Date getProcon_fecadd() {
        return procon_fecadd;
    }

    public void setProcon_fecadd(Date procon_fecadd) {
        this.procon_fecadd = procon_fecadd;
    }

    public Date getProcon_fecmod() {
        return procon_fecmod;
    }

    public void setProcon_fecmod(Date procon_fecmod) {
        this.procon_fecmod = procon_fecmod;
    }

    public int getProcon_id() {
        return procon_id;
    }

    public void setProcon_id(int procon_id) {
        this.procon_id = procon_id;
    }

    public String getProcon_nom() {
        return procon_nom;
    }

    public void setProcon_nom(String procon_nom) {
        this.procon_nom = procon_nom;
    }

    public long getProcon_telef1() {
        return procon_telef1;
    }

    public void setProcon_telef1(long procon_telef1) {
        this.procon_telef1 = procon_telef1;
    }

    public long getProcon_telef2() {
        return procon_telef2;
    }

    public String getProcon_cargo() {
        return procon_cargo;
    }

    public void setProcon_cargo(String procon_cargo) {
        this.procon_cargo = procon_cargo;
    }

    public String getProcon_suc() {
        return procon_suc;
    }

    public void setProcon_suc(String procon_suc) {
        this.procon_suc = procon_suc;
    }

    public void setProcon_telef2(long procon_telef2) {
        this.procon_telef2 = procon_telef2;
    }

    public String getProcon_usuadd() {
        return procon_usuadd;
    }

    public void setProcon_usuadd(String procon_usuadd) {
        this.procon_usuadd = procon_usuadd;
    }

    public String getProcon_usumod() {
        return procon_usumod;
    }

    public void setProcon_usumod(String procon_usumod) {
        this.procon_usumod = procon_usumod;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

}
