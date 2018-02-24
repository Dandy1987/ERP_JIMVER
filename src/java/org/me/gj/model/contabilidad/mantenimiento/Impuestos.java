package org.me.gj.model.contabilidad.mantenimiento;

import java.util.Date;

public class Impuestos {
    private int imp_id;
    private String imp_des;
    private double imp_valor;
    private int imp_est;
    private String imp_usuadd;
    private Date imp_fecadd;
    private String imp_usumod;
    private Date imp_fecmod;
    private String imp_nomrep;
    private int imp_ord;
    private boolean valor;
   
    public Impuestos() {
    }

    public Impuestos(int imp_id, String imp_des, double imp_valor, int imp_est, String imp_usuadd, String imp_usumod, String imp_nomrep, int imp_ord) {
        this.imp_id = imp_id;
        this.imp_des = imp_des;
        this.imp_valor = imp_valor;
        this.imp_est = imp_est;
        this.imp_usuadd = imp_usuadd;
        this.imp_usumod = imp_usumod;
        this.imp_nomrep = imp_nomrep;
        this.imp_ord = imp_ord;
    }

    public String getImp_des() {
        return imp_des;
    }

    public void setImp_des(String imp_des) {
        this.imp_des = imp_des;
    }

    public int getImp_est() {
        return imp_est;
    }

    public void setImp_est(int imp_est) {
        this.imp_est = imp_est;
    }

    public Date getImp_fecadd() {
        return imp_fecadd;
    }

    public void setImp_fecadd(Date imp_fecadd) {
        this.imp_fecadd = imp_fecadd;
    }

    public Date getImp_fecmod() {
        return imp_fecmod;
    }

    public void setImp_fecmod(Date imp_fecmod) {
        this.imp_fecmod = imp_fecmod;
    }

    public int getImp_id() {
        return imp_id;
    }

    public void setImp_id(int imp_id) {
        this.imp_id = imp_id;
    }

    public String getImp_usuadd() {
        return imp_usuadd;
    }

    public void setImp_usuadd(String imp_usuadd) {
        this.imp_usuadd = imp_usuadd;
    }

    public String getImp_usumod() {
        return imp_usumod;
    }

    public void setImp_usumod(String imp_usumod) {
        this.imp_usumod = imp_usumod;
    }

    public double getImp_valor() {
        return imp_valor;
    }

    public void setImp_valor(double imp_valor) {
        this.imp_valor = imp_valor;
    }

    public String getImp_nomrep() {
        return imp_nomrep;
    }

    public void setImp_nomrep(String imp_nomrep) {
        this.imp_nomrep = imp_nomrep;
    }

    public int getImp_ord() {
        return imp_ord;
    }

    public void setImp_ord(int imp_ord) {
        this.imp_ord = imp_ord;
    }

    public boolean isValor() {
        if(imp_est==1)
            valor=true;
        else
            valor=false;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
