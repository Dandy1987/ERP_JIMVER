package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class SubLineas {

    private String slin_cod;
    private String slin_des;
    private String slin_estado;
    private int slin_codlin;
    private String slin_deslin;
    private String slin_codlincad;
    private String slin_codlindes;
    private int slin_codslin;
    private String slin_usuadd;
    private Date slin_fecadd;
    private String slin_usumod;
    private Date slin_fecmod;
    private boolean val;
    
    private String sslin_codlin;

    public SubLineas(String slin_cod, String slin_des, String slin_estado, int slin_codlin,  
                     int slin_codslin, String slin_usuadd) {
        
        this.slin_cod = slin_cod;
        this.slin_des = slin_des;
        this.slin_estado = slin_estado;
        this.slin_codlin = slin_codlin;
        /*this.slin_codlincad = slin_codlincad;
        this.slin_codlindes = slin_codlindes;*/
        this.slin_codslin = slin_codslin;
        this.slin_usuadd = slin_usuadd;
    }

    //jr
      public SubLineas(String slin_cod, String slin_des, String slin_estado, int slin_codlin,  
                     int slin_codslin, String slin_usuadd,String slin_usumod) {
        
        this.slin_cod = slin_cod;
        this.slin_des = slin_des;
        this.slin_estado = slin_estado;
        this.slin_codlin = slin_codlin;
        /*this.slin_codlincad = slin_codlincad;
        this.slin_codlindes = slin_codlindes;*/
        this.slin_codslin = slin_codslin;
        this.slin_usuadd = slin_usuadd;
        this.slin_usumod = slin_usumod;
    }
    public SubLineas() {
    }

    public String getSlin_cod() {
        return slin_cod;
    }

    public void setSlin_cod(String slin_cod) {
        this.slin_cod = slin_cod;
    }

    public String getSlin_des() {
        return slin_des;
    }

    public void setSlin_des(String slin_des) {
        this.slin_des = slin_des;
    }

    public String getSlin_estado() {
        return slin_estado;
    }

    public void setSlin_estado(String slin_estado) {
        this.slin_estado = slin_estado;
    }

    public int getSlin_codlin() {
        return slin_codlin;
    }

    public void setSlin_codlin(int slin_codlin) {
        this.slin_codlin = slin_codlin;
    }

    public int getSlin_codslin() {
        return slin_codslin;
    }

    public void setSlin_codslin(int slin_codslin) {
        this.slin_codslin = slin_codslin;
    }

    public String getSlin_usuadd() {
        return slin_usuadd;
    }

    public void setSlin_usuadd(String slin_usuadd) {
        this.slin_usuadd = slin_usuadd;
    }

    public Date getSlin_fecadd() {
        return slin_fecadd;
    }

    public void setSlin_fecadd(Date slin_fecadd) {
        this.slin_fecadd = slin_fecadd;
    }

    public String getSlin_usumod() {
        return slin_usumod;
    }

    public void setSlin_usumod(String slin_usumod) {
        this.slin_usumod = slin_usumod;
    }

    public Date getSlin_fecmod() {
        return slin_fecmod;
    }

    public void setSlin_fecmod(Date slin_fecmod) {
        this.slin_fecmod = slin_fecmod;
    }

    public String getSlin_codlindes() {
        return slin_codlindes;
    }

    public void setSlin_codlindes(String slin_codlindes) {
        this.slin_codlindes = slin_codlindes;
    }

    public String getSlin_codlincad() {
        return slin_codlincad;
    }

    public void setSlin_codlincad(String slin_codlincad) {
        this.slin_codlincad = slin_codlincad;
    }

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public String getSslin_codlin() {
        return sslin_codlin;
    }

    public void setSslin_codlin(String sslin_codlin) {
        this.sslin_codlin = sslin_codlin;
    }

    public String getSlin_deslin() {
        return slin_deslin;
    }

    public void setSlin_deslin(String slin_deslin) {
        this.slin_deslin = slin_deslin;
    }
    
}
