package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class NumDoc {

    private int tnumdoc_cor;
    private int emp_id;
    private int suc_id;
    private int tnumdoc_tipven;
    private int tnumdoc_serie;
    private int tnumdoc_notes;
    private String tnumdoc_des;
    private String tnumdoc_nomrep;
    private int tnumdoc_est;
    private String tnumdoc_usuadd;
    private Date tnumdoc_fecadd;
    private String tnumdoc_usumod;
    private Date tnumdoc_fecmod;
    private boolean valor;
    private String tnumdoc_corid;
    private String tnumdoc_tipvenid;
    private String tnumdoc_tipven_des;
    private String tnumdoc_notes_des;
    private String tnumdoc_serieid;

    public NumDoc() {
    }

    public NumDoc(int tnumdoc_cor, int emp_id, int suc_id, int tnumdoc_tipven, int tnumdoc_serie, int tnumdoc_notes, String tnumdoc_des, String tnumdoc_nomrep, int tnumdoc_est, String tnumdoc_usuadd, String tnumdoc_usumod) {
        this.tnumdoc_cor = tnumdoc_cor;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tnumdoc_tipven = tnumdoc_tipven;
        this.tnumdoc_serie = tnumdoc_serie;
        this.tnumdoc_notes = tnumdoc_notes;
        this.tnumdoc_des = tnumdoc_des;
        this.tnumdoc_nomrep = tnumdoc_nomrep;
        this.tnumdoc_est = tnumdoc_est;
        this.tnumdoc_usuadd = tnumdoc_usuadd;
        this.tnumdoc_usumod = tnumdoc_usumod;
    }

    public int getTnumdoc_cor() {
        return tnumdoc_cor;
    }

    public void setTnumdoc_cor(int tnumdoc_cor) {
        this.tnumdoc_cor = tnumdoc_cor;
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

    public int getTnumdoc_tipven() {
        return tnumdoc_tipven;
    }

    public void setTnumdoc_tipven(int tnumdoc_tipven) {
        this.tnumdoc_tipven = tnumdoc_tipven;
    }

    public int getTnumdoc_serie() {
        return tnumdoc_serie;
    }

    public void setTnumdoc_serie(int tnumdoc_serie) {
        this.tnumdoc_serie = tnumdoc_serie;
    }

    public int getTnumdoc_notes() {
        return tnumdoc_notes;
    }

    public void setTnumdoc_notes(int tnumdoc_notes) {
        this.tnumdoc_notes = tnumdoc_notes;
    }

    public String getTnumdoc_des() {
        return tnumdoc_des;
    }

    public void setTnumdoc_des(String tnumdoc_des) {
        this.tnumdoc_des = tnumdoc_des;
    }

    public String getTnumdoc_nomrep() {
        return tnumdoc_nomrep;
    }

    public void setTnumdoc_nomrep(String tnumdoc_nomrep) {
        this.tnumdoc_nomrep = tnumdoc_nomrep;
    }

    public int getTnumdoc_est() {
        return tnumdoc_est;
    }

    public void setTnumdoc_est(int tnumdoc_est) {
        this.tnumdoc_est = tnumdoc_est;
    }

    public String getTnumdoc_usuadd() {
        return tnumdoc_usuadd;
    }

    public void setTnumdoc_usuadd(String tnumdoc_usuadd) {
        this.tnumdoc_usuadd = tnumdoc_usuadd;
    }

    public Date getTnumdoc_fecadd() {
        return tnumdoc_fecadd;
    }

    public void setTnumdoc_fecadd(Date tnumdoc_fecadd) {
        this.tnumdoc_fecadd = tnumdoc_fecadd;
    }

    public String getTnumdoc_usumod() {
        return tnumdoc_usumod;
    }

    public void setTnumdoc_usumod(String tnumdoc_usumod) {
        this.tnumdoc_usumod = tnumdoc_usumod;
    }

    public Date getTnumdoc_fecmod() {
        return tnumdoc_fecmod;
    }

    public void setTnumdoc_fecmod(Date tnumdoc_fecmod) {
        this.tnumdoc_fecmod = tnumdoc_fecmod;
    }

    public boolean isValor() {
        if (tnumdoc_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getTnumdoc_tipven_des() {
        return tnumdoc_tipven_des;
    }

    public void setTnumdoc_tipven_des(String tnumdoc_tipven_des) {
        this.tnumdoc_tipven_des = tnumdoc_tipven_des;
    }

    public String getTnumdoc_corid() {
        return tnumdoc_corid;
    }

    public void setTnumdoc_corid(String tnumdoc_corid) {
        this.tnumdoc_corid = tnumdoc_corid;
    }

    public String getTnumdoc_notes_des() {
        return tnumdoc_notes_des;
    }

    public void setTnumdoc_notes_des(String tnumdoc_notes_des) {
        this.tnumdoc_notes_des = tnumdoc_notes_des;
    }

    public String getTnumdoc_serieid() {
        return tnumdoc_serieid;
    }

    public void setTnumdoc_serieid(String tnumdoc_serieid) {
        this.tnumdoc_serieid = tnumdoc_serieid;
    }

    public String getTnumdoc_tipvenid() {
        return tnumdoc_tipvenid;
    }

    public void setTnumdoc_tipvenid(String tnumdoc_tipvenid) {
        this.tnumdoc_tipvenid = tnumdoc_tipvenid;
    }

}
