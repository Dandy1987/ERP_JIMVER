package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class NumeFac {

    private int numefac_cor;
    private int emp_id;
    private int suc_id;
    private int numefac_tipdoc;
    private int numefac_serie;
    private int numefac_notes;
    private String numefac_des;
    private String numefac_nomrep;
    private int numefac_est;
    private String numefac_usuadd;
    private Date numefac_fecadd;
    private String numefac_usumod;
    private Date numefac_fecmod;
    private boolean valor;
    private String numefac_corid;
    private String numefac_tipdoc_id;
    private String numefac_tipdoc_des;
    private String numefac_notes_des;
    private String numefac_serie_id;
    private int numefac_nroitems;

    public NumeFac() {
    }

    public NumeFac(int numefac_cor, int emp_id, int suc_id, int numefac_tipdoc, int numefac_serie, int numefac_notes, int numefac_nroitems, 
            String numefac_des, String numefac_nomrep, int numefac_est, String numefac_usuadd, String numefac_usumod) {
        this.numefac_cor = numefac_cor;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.numefac_tipdoc = numefac_tipdoc;
        this.numefac_serie = numefac_serie;
        this.numefac_notes = numefac_notes;
        this.numefac_nroitems = numefac_nroitems;
        this.numefac_des = numefac_des;
        this.numefac_nomrep = numefac_nomrep;
        this.numefac_est = numefac_est;
        this.numefac_usuadd = numefac_usuadd;
        this.numefac_usumod = numefac_usumod;
    }

    public int getNumefac_cor() {
        return numefac_cor;
    }

    public void setNumefac_cor(int numefac_cor) {
        this.numefac_cor = numefac_cor;
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

    public int getNumefac_tipdoc() {
        return numefac_tipdoc;
    }

    public void setNumefac_tipdoc(int numefac_tipdoc) {
        this.numefac_tipdoc = numefac_tipdoc;
    }

    public int getNumefac_serie() {
        return numefac_serie;
    }

    public void setNumefac_serie(int numefac_serie) {
        this.numefac_serie = numefac_serie;
    }

    public int getNumefac_notes() {
        return numefac_notes;
    }

    public void setNumefac_notes(int numefac_notes) {
        this.numefac_notes = numefac_notes;
    }

    public String getNumefac_des() {
        return numefac_des;
    }

    public void setNumefac_des(String numefac_des) {
        this.numefac_des = numefac_des;
    }

    public String getNumefac_nomrep() {
        return numefac_nomrep;
    }

    public void setNumefac_nomrep(String numefac_nomrep) {
        this.numefac_nomrep = numefac_nomrep;
    }

    public int getNumefac_est() {
        return numefac_est;
    }

    public void setNumefac_est(int numefac_est) {
        this.numefac_est = numefac_est;
    }

    public String getNumefac_usuadd() {
        return numefac_usuadd;
    }

    public void setNumefac_usuadd(String numefac_usuadd) {
        this.numefac_usuadd = numefac_usuadd;
    }

    public Date getNumefac_fecadd() {
        return numefac_fecadd;
    }

    public void setNumefac_fecadd(Date numefac_fecadd) {
        this.numefac_fecadd = numefac_fecadd;
    }

    public String getNumefac_usumod() {
        return numefac_usumod;
    }

    public void setNumefac_usumod(String numefac_usumod) {
        this.numefac_usumod = numefac_usumod;
    }

    public Date getNumefac_fecmod() {
        return numefac_fecmod;
    }

    public void setNumefac_fecmod(Date numefac_fecmod) {
        this.numefac_fecmod = numefac_fecmod;
    }

    public String getNumefac_corid() {
        return numefac_corid;
    }

    public void setNumefac_corid(String numefac_corid) {
        this.numefac_corid = numefac_corid;
    }

    public String getNumefac_tipdoc_id() {
        return numefac_tipdoc_id;
    }

    public void setNumefac_tipdoc_id(String numefac_tipdoc_id) {
        this.numefac_tipdoc_id = numefac_tipdoc_id;
    }

    public String getNumefac_tipdoc_des() {
        return numefac_tipdoc_des;
    }

    public void setNumefac_tipdoc_des(String numefac_tipdoc_des) {
        this.numefac_tipdoc_des = numefac_tipdoc_des;
    }

    public String getNumefac_notes_des() {
        return numefac_notes_des;
    }

    public void setNumefac_notes_des(String numefac_notes_des) {
        this.numefac_notes_des = numefac_notes_des;
    }

    public String getNumefac_serie_id() {
        return numefac_serie_id;
    }

    public void setNumefac_serie_id(String numefac_serie_id) {
        this.numefac_serie_id = numefac_serie_id;
    }

    public boolean isValor() {
        if (numefac_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getNumefac_nroitems() {
        return numefac_nroitems;
    }

    public void setNumefac_nroitems(int numefac_nroitems) {
        this.numefac_nroitems = numefac_nroitems;
    }
}
