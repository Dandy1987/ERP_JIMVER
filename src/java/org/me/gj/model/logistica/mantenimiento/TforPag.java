package org.me.gj.model.logistica.mantenimiento;

public class TforPag {

    int fpag_id;
    String fpag_des;
    int fpag_est;
    int fpag_caj;
    int fpag_pan;
    int fpag_com;
    int fpag_jav;
    int fpag_dpla;
    int fpag_ord;
    String fpag_nomrep;
    int fpag_comp;
    int fpag_cont;
    int fpag_ger;
    String fpag_mod;
    String s_chk_comp = "NO";
    String s_chk_cont = "NO";
    String s_chk_ger = "NO";
    boolean val = false;

    public TforPag(int fpag_id, String fpag_des, int fpag_est, int fpag_caj, int fpag_pan, int fpag_com, int fpag_jav, int fpag_dpla, int fpag_ord, String fpag_nomrep, int fpag_comp, int fpag_cont, int fpag_ger, String fpag_mod) {
        this.fpag_id = fpag_id;
        this.fpag_des = fpag_des;
        this.fpag_est = fpag_est;
        this.fpag_caj = fpag_caj;
        this.fpag_pan = fpag_pan;
        this.fpag_com = fpag_com;
        this.fpag_jav = fpag_jav;
        this.fpag_dpla = fpag_dpla;
        this.fpag_ord = fpag_ord;
        this.fpag_nomrep = fpag_nomrep;
        this.fpag_comp = fpag_comp;
        this.fpag_cont = fpag_cont;
        this.fpag_ger = fpag_ger;
        this.fpag_mod = fpag_mod;
    }

    public TforPag(int fpag_id, String fpag_des, int fpag_est, int fpag_dpla, int fpag_ord, String fpag_nomrep, int fpag_comp, int fpag_cont, int fpag_ger) {
        this.fpag_id = fpag_id;
        this.fpag_des = fpag_des;
        this.fpag_est = fpag_est;
        this.fpag_dpla = fpag_dpla;
        this.fpag_ord = fpag_ord;
        this.fpag_nomrep = fpag_nomrep;
        this.fpag_comp = fpag_comp;
        this.fpag_cont = fpag_cont;
        this.fpag_ger = fpag_ger;
    }

    public TforPag() {
    }

    public String getS_chk_comp() {
        return s_chk_comp;
    }

    public void setS_chk_comp(String s_chk_comp) {
        this.s_chk_comp = s_chk_comp;
    }

    public String getS_chk_cont() {
        return s_chk_cont;
    }

    public void setS_chk_cont(String s_chk_cont) {
        this.s_chk_cont = s_chk_cont;
    }

    public String getS_chk_ger() {
        return s_chk_ger;
    }

    public void setS_chk_ger(String s_chk_ger) {
        this.s_chk_ger = s_chk_ger;
    }

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public int getFpag_caj() {
        return fpag_caj;
    }

    public void setFpag_caj(int fpag_caj) {
        this.fpag_caj = fpag_caj;
    }

    public int getFpag_com() {
        return fpag_com;
    }

    public void setFpag_com(int fpag_com) {
        this.fpag_com = fpag_com;
    }

    public String getFpag_des() {
        return fpag_des;
    }

    public void setFpag_des(String fpag_des) {
        this.fpag_des = fpag_des;
    }

    public int getFpag_dpla() {
        return fpag_dpla;
    }

    public void setFpag_dpla(int fpag_dpla) {
        this.fpag_dpla = fpag_dpla;
    }

    public int getFpag_est() {
        if (fpag_est == 1) {
            val = true;
        }
        return fpag_est;
    }

    public void setFpag_est(int fpag_est) {
        this.fpag_est = fpag_est;
    }

    public int getFpag_id() {
        return fpag_id;
    }

    public void setFpag_id(int fpag_id) {
        this.fpag_id = fpag_id;
    }

    public int getFpag_jav() {
        return fpag_jav;
    }

    public void setFpag_jav(int fpag_jav) {
        this.fpag_jav = fpag_jav;
    }

    public String getFpag_mod() {
        return fpag_mod;
    }

    public void setFpag_mod(String fpag_mod) {
        this.fpag_mod = fpag_mod;
    }

    public int getFpag_pan() {
        return fpag_pan;
    }

    public void setFpag_pan(int fpag_pan) {
        this.fpag_pan = fpag_pan;
    }

    public int getFpag_ord() {
        return fpag_ord;
    }

    public void setFpag_ord(int fpag_ord) {
        this.fpag_ord = fpag_ord;
    }

    public String getFpag_nomrep() {
        return fpag_nomrep;
    }

    public void setFpag_nomrep(String fpag_nomrep) {
        this.fpag_nomrep = fpag_nomrep;
    }

    public int getFpag_comp() {
        return fpag_comp;
    }

    public void setFpag_comp(int fpag_comp) {
        this.fpag_comp = fpag_comp;
    }

    public int getFpag_cont() {
        return fpag_cont;
    }

    public void setFpag_cont(int fpag_cont) {
        this.fpag_cont = fpag_cont;
    }

    public int getFpag_ger() {
        return fpag_ger;
    }

    public void setFpag_ger(int fpag_ger) {
        this.fpag_ger = fpag_ger;
    }
}
