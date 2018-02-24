package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class Stock {

    private int emp_id;
    private int suc_id;
    private String per_id;
    private int per_key;
    private String prov_id;
    private String pro_id;
    private long pro_key;
    private int alm_key;
    private String alm_id;
    private int ubic_key;
    private String ubic_id;
    private int stk_estado;
    private long stk_inicial;
    private long stk_actual;
    private Date stk_fecultent;
    private Date stk_fecultsal;
    private String stk_usuadd;
    private Date stk_fecadd;
    private String stk_usumod;
    private Date stk_fecmod;
    private String pro_des;
    private String prov_razsoc;
    private String alm_des;

    public String getAlm_des() {
        return alm_des;
    }

    public void setAlm_des(String alm_des) {
        this.alm_des = alm_des;
    }
    private boolean selImp;

    public Stock() {
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

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public int getPer_key() {
        return per_key;
    }

    public void setPer_key(int per_key) {
        this.per_key = per_key;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public int getAlm_key() {
        return alm_key;
    }

    public void setAlm_key(int alm_key) {
        this.alm_key = alm_key;
    }

    public String getAlm_id() {
        return alm_id;
    }

    public void setAlm_id(String alm_id) {
        this.alm_id = alm_id;
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

    public int getStk_estado() {
        return stk_estado;
    }

    public void setStk_estado(int stk_estado) {
        this.stk_estado = stk_estado;
    }

    public long getStk_inicial() {
        return stk_inicial;
    }

    public void setStk_inicial(long stk_inicial) {
        this.stk_inicial = stk_inicial;
    }

    public long getStk_actual() {
        return stk_actual;
    }

    public void setStk_actual(long stk_actual) {
        this.stk_actual = stk_actual;
    }

    public Date getStk_fecultent() {
        return stk_fecultent;
    }

    public void setStk_fecultent(Date stk_fecultent) {
        this.stk_fecultent = stk_fecultent;
    }

    public Date getStk_fecultsal() {
        return stk_fecultsal;
    }

    public void setStk_fecultsal(Date stk_fecultsal) {
        this.stk_fecultsal = stk_fecultsal;
    }

    public String getStk_usuadd() {
        return stk_usuadd;
    }

    public void setStk_usuadd(String stk_usuadd) {
        this.stk_usuadd = stk_usuadd;
    }

    public Date getStk_fecadd() {
        return stk_fecadd;
    }

    public void setStk_fecadd(Date stk_fecadd) {
        this.stk_fecadd = stk_fecadd;
    }

    public String getStk_usumod() {
        return stk_usumod;
    }

    public void setStk_usumod(String stk_usumod) {
        this.stk_usumod = stk_usumod;
    }

    public Date getStk_fecmod() {
        return stk_fecmod;
    }

    public void setStk_fecmod(Date stk_fecmod) {
        this.stk_fecmod = stk_fecmod;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public boolean isSelImp() {
        return selImp;
    }

    public void setSelImp(boolean selImp) {
        this.selImp = selImp;
    }
}
