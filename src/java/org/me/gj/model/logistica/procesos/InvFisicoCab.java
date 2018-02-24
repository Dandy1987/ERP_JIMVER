package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class InvFisicoCab {

    private String fisicab_id;
    private int emp_id;
    private int suc_id;
    private String per_id;
    private int per_key;
    private int alm_key;
    private String pro_id;
    private String pro_des;
    private int pro_presminven;
    private String prov_id;
    private String prov_razsoc;
    private String fisicab_respon;
    private int fisicab_est;
    private String fisicab_usuadd;
    private Date fisicab_fecadd;
    private String fisicab_pcadd;
    private String fisicab_usumod;
    private Date fisicab_fecmod;
    private String fisicab_pcmod;
    private boolean valor;
    private String alm_id;
    private String alm_des;

    public InvFisicoCab() {
    }

    public InvFisicoCab(String fisicab_id, int emp_id, int suc_id, String per_id, int per_key, int alm_key, String fisicab_respon, int fisicab_est, String fisicab_usuadd, String fisicab_pcadd, String fisicab_usumod, String fisicab_pcmod) {
        this.fisicab_id = fisicab_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.per_id = per_id;
        this.per_key = per_key;
        this.alm_key = alm_key;
        this.fisicab_respon = fisicab_respon;
        this.fisicab_est = fisicab_est;
        this.fisicab_usuadd = fisicab_usuadd;
        this.fisicab_pcadd = fisicab_pcadd;
        this.fisicab_usumod = fisicab_usumod;
        this.fisicab_pcmod = fisicab_pcmod;
    }

    public InvFisicoCab(String pro_id, int emp_id, int suc_id, String fisicab_usumod, String fisicab_pcmod) {
        this.pro_id = pro_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.fisicab_usumod = fisicab_usumod;
        this.fisicab_pcmod = fisicab_pcmod;
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

    public int getAlm_key() {
        return alm_key;
    }

    public void setAlm_key(int alm_key) {
        this.alm_key = alm_key;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public int getPro_presminven() {
        return pro_presminven;
    }

    public void setPro_presminven(int pro_presminven) {
        this.pro_presminven = pro_presminven;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public String getFisicab_id() {
        return fisicab_id;
    }

    public void setFisicab_id(String fisicab_id) {
        this.fisicab_id = fisicab_id;
    }

    public int getPer_key() {
        return per_key;
    }

    public void setPer_key(int per_key) {
        this.per_key = per_key;
    }

    public String getFisicab_respon() {
        return fisicab_respon;
    }

    public void setFisicab_respon(String fisicab_respon) {
        this.fisicab_respon = fisicab_respon;
    }

    public int getFisicab_est() {
        return fisicab_est;
    }

    public void setFisicab_est(int fisicab_est) {
        this.fisicab_est = fisicab_est;
    }

    public String getFisicab_usuadd() {
        return fisicab_usuadd;
    }

    public void setFisicab_usuadd(String fisicab_usuadd) {
        this.fisicab_usuadd = fisicab_usuadd;
    }

    public Date getFisicab_fecadd() {
        return fisicab_fecadd;
    }

    public void setFisicab_fecadd(Date fisicab_fecadd) {
        this.fisicab_fecadd = fisicab_fecadd;
    }

    public String getFisicab_pcadd() {
        return fisicab_pcadd;
    }

    public void setFisicab_pcadd(String fisicab_pcadd) {
        this.fisicab_pcadd = fisicab_pcadd;
    }

    public String getFisicab_usumod() {
        return fisicab_usumod;
    }

    public void setFisicab_usumod(String fisicab_usumod) {
        this.fisicab_usumod = fisicab_usumod;
    }

    public Date getFisicab_fecmod() {
        return fisicab_fecmod;
    }

    public void setFisicab_fecmod(Date fisicab_fecmod) {
        this.fisicab_fecmod = fisicab_fecmod;
    }

    public String getFisicab_pcmod() {
        return fisicab_pcmod;
    }

    public void setFisicab_pcmod(String fisicab_pcmod) {
        this.fisicab_pcmod = fisicab_pcmod;
    }

    public boolean isValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getAlm_id() {
        return alm_id;
    }

    public void setAlm_id(String alm_id) {
        this.alm_id = alm_id;
    }

    public String getAlm_des() {
        return alm_des;
    }

    public void setAlm_des(String alm_des) {
        this.alm_des = alm_des;
    }
}
