package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class InvFisicoDet {

    private String fisicab_id;
    private int emp_id;
    private int suc_id;
    private int per_key;
    private String per_id;
    private int alm_key;
    private String alm_id;
    private int fisidet_key;
    private long pro_key;
    private String pro_id;
    private String pro_des;
    private int entero;
    private int fraccion;
    private int fisidet_cant;
    private long stk_actual;
    private int pro_presminven;
    private long prov_key;
    private String prov_id;
    private String prov_razsoc;
    private int ubic_key;
    private String ubic_id;
    private String ubic_des;
    private int fisidet_est;
    private String fisidet_usuadd;
    private Date fisidet_fecadd;
    private String fisidet_pcadd;
    private String fisidet_usumod;
    private Date fisidet_fecmod;
    private String fisidet_pcmod;
    private String alm_des;
    private boolean valor;
    private String ind_accion = "Q";
    
    private int index;

    public InvFisicoDet(int index, String fisicab_id, int emp_id, int suc_id, int per_key, String per_id, int alm_key, String alm_id,
            int fisidet_key, long pro_key, String pro_id, String pro_des,int pro_presminven, int entero, int fraccion, int fisidet_cant,
            long prov_key, String prov_id, String prov_razsoc, int ubic_key, String ubic_id, String ubic_des, int fisidet_est, String fisidet_usuadd, String fisidet_pcadd) {
        this.index = index;
        this.fisicab_id = fisicab_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.per_key = per_key;
        this.per_id = per_id;
        this.alm_key = alm_key;
        this.alm_id = alm_id;
        this.fisidet_key = fisidet_key;
        this.pro_key = pro_key;
        this.pro_id = pro_id;
        this.pro_des = pro_des;
        this.pro_presminven = pro_presminven;
        this.entero = entero;
        this.fraccion = fraccion;
        this.fisidet_cant = fisidet_cant;
        this.prov_key = prov_key;
        this.prov_id = prov_id;
        this.prov_razsoc = prov_razsoc;
        this.ubic_key = ubic_key;
        this.ubic_id = ubic_id;
        this.ubic_des = ubic_des;
        this.fisidet_est = fisidet_est;
        this.fisidet_usuadd = fisidet_usuadd;
        this.fisidet_pcadd = fisidet_pcadd;
    }

    public InvFisicoDet() {
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

    public int getPer_key() {
        return per_key;
    }

    public void setPer_key(int per_key) {
        this.per_key = per_key;
    }

    public String getFisicab_id() {
        return fisicab_id;
    }

    public void setFisicab_id(String fisicab_id) {
        this.fisicab_id = fisicab_id;
    }

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
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

    public int getEntero() {
        double decimal = (fisidet_cant / pro_presminven) % 1;
        entero = (int) (Math.round((fisidet_cant / pro_presminven) - decimal));
        return entero;
    }

    public void setEntero(int entero) {
        this.entero = entero;
    }

    public int getFraccion() {
        fraccion = (int) (Math.round(fisidet_cant % pro_presminven));
        return fraccion;
    }

    public void setFraccion(int fraccion) {
        this.fraccion = fraccion;
    }

    public long getStk_actual() {
        return stk_actual;
    }

    public void setStk_actual(long stk_actual) {
        this.stk_actual = stk_actual;
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

    public int getUbic_key() {
        return ubic_key;
    }

    public void setUbic_key(int ubic_key) {
        this.ubic_key = ubic_key;
    }

    public String getUbic_des() {
        return ubic_des;
    }

    public void setUbic_des(String ubic_des) {
        this.ubic_des = ubic_des;
    }

    public int getAlm_key() {
        return alm_key;
    }

    public void setAlm_key(int alm_key) {
        this.alm_key = alm_key;
    }

    public String getAlm_des() {
        return alm_des;
    }

    public void setAlm_des(String alm_des) {
        this.alm_des = alm_des;
    }

    public boolean isValor() {
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

    public int getFisidet_key() {
        return fisidet_key;
    }

    public void setFisidet_key(int fisidet_key) {
        this.fisidet_key = fisidet_key;
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public int getFisidet_cant() {
        return fisidet_cant;
    }

    public void setFisidet_cant(int fisidet_cant) {
        this.fisidet_cant = fisidet_cant;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public int getFisidet_est() {
        return fisidet_est;
    }

    public void setFisidet_est(int fisidet_est) {
        this.fisidet_est = fisidet_est;
    }

    public String getFisidet_usuadd() {
        return fisidet_usuadd;
    }

    public void setFisidet_usuadd(String fisidet_usuadd) {
        this.fisidet_usuadd = fisidet_usuadd;
    }

    public Date getFisidet_fecadd() {
        return fisidet_fecadd;
    }

    public void setFisidet_fecadd(Date fisidet_fecadd) {
        this.fisidet_fecadd = fisidet_fecadd;
    }

    public String getFisidet_pcadd() {
        return fisidet_pcadd;
    }

    public void setFisidet_pcadd(String fisidet_pcadd) {
        this.fisidet_pcadd = fisidet_pcadd;
    }

    public String getFisidet_usumod() {
        return fisidet_usumod;
    }

    public void setFisidet_usumod(String fisidet_usumod) {
        this.fisidet_usumod = fisidet_usumod;
    }

    public Date getFisidet_fecmod() {
        return fisidet_fecmod;
    }

    public void setFisidet_fecmod(Date fisidet_fecmod) {
        this.fisidet_fecmod = fisidet_fecmod;
    }

    public String getFisidet_pcmod() {
        return fisidet_pcmod;
    }

    public void setFisidet_pcmod(String fisidet_pcmod) {
        this.fisidet_pcmod = fisidet_pcmod;
    }

    public String getAlm_id() {
        return alm_id;
    }

    public void setAlm_id(String alm_id) {
        this.alm_id = alm_id;
    }

    public String getUbic_id() {
        return ubic_id;
    }

    public void setUbic_id(String ubic_id) {
        this.ubic_id = ubic_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
}
