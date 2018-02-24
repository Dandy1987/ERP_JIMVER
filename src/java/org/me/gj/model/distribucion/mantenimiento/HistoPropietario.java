package org.me.gj.model.distribucion.mantenimiento;

import java.util.Date;

public class HistoPropietario {

    private int hprop_id;
    private int prop_id;
    private String prop_razsoc;//solo consulta
    private int trans_key;
    private String trans_alias;//solo consulta
    private int suc_id;
    private int emp_id;
    private String hprop_usuadd;
    private Date hprop_fecadd;
    private String hprop_usumod;
    private Date hprop_fecmod;
    private int hprop_est;
    private boolean valor;
    private String ind_accion = "Q";

    public HistoPropietario(int hprop_id, int prop_id, int trans_key, int suc_id, int emp_id, String hprop_usuadd, Date hprop_fecadd, String hprop_usumod, Date hprop_fecmod, int hprop_est) {
        this.hprop_id = hprop_id;
        this.prop_id = prop_id;
        this.trans_key = trans_key;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.hprop_usuadd = hprop_usuadd;
        this.hprop_fecadd = hprop_fecadd;
        this.hprop_usumod = hprop_usumod;
        this.hprop_fecmod = hprop_fecmod;
        this.hprop_est = hprop_est;
    }

    public HistoPropietario(int hprop_id, int prop_id, String prop_razsoc, int trans_key, String trans_alias, int suc_id, int emp_id, String hprop_usuadd, Date hprop_fecadd, String hprop_usumod, Date hprop_fecmod, int hprop_est, boolean valor) {
        this.hprop_id = hprop_id;
        this.prop_id = prop_id;
        this.prop_razsoc = prop_razsoc;
        this.trans_key = trans_key;
        this.trans_alias = trans_alias;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.hprop_usuadd = hprop_usuadd;
        this.hprop_fecadd = hprop_fecadd;
        this.hprop_usumod = hprop_usumod;
        this.hprop_fecmod = hprop_fecmod;
        this.hprop_est = hprop_est;
        this.valor = valor;
    }

    public HistoPropietario() {
    }

    public String getProp_razsoc() {
        return prop_razsoc;
    }

    public void setProp_razsoc(String prop_razsoc) {
        this.prop_razsoc = prop_razsoc;
    }

    public String getTrans_alias() {
        return trans_alias;
    }

    public void setTrans_alias(String trans_alias) {
        this.trans_alias = trans_alias;
    }

    public int getHprop_id() {
        return hprop_id;
    }

    public void setHprop_id(int hprop_id) {
        this.hprop_id = hprop_id;
    }

    public int getProp_id() {
        return prop_id;
    }

    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }

    public int getTrans_key() {
        return trans_key;
    }

    public void setTrans_key(int trans_key) {
        this.trans_key = trans_key;
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

    public String getHprop_usuadd() {
        return hprop_usuadd;
    }

    public void setHprop_usuadd(String hprop_usuadd) {
        this.hprop_usuadd = hprop_usuadd;
    }

    public Date getHprop_fecadd() {
        return hprop_fecadd;
    }

    public void setHprop_fecadd(Date hprop_fecadd) {
        this.hprop_fecadd = hprop_fecadd;
    }

    public String getHprop_usumod() {
        return hprop_usumod;
    }

    public void setHprop_usumod(String hprop_usumod) {
        this.hprop_usumod = hprop_usumod;
    }

    public Date getHprop_fecmod() {
        return hprop_fecmod;
    }

    public void setHprop_fecmod(Date hprop_fecmod) {
        this.hprop_fecmod = hprop_fecmod;
    }

    public int getHprop_est() {
        return hprop_est;
    }

    public void setHprop_est(int hprop_est) {
        this.hprop_est = hprop_est;
    }

    public boolean isValor() {
        if (hprop_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }
    
}
