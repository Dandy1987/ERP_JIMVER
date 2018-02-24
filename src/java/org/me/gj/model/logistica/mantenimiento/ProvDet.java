package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class ProvDet {

    private int prodet_id;
    private long prov_key;
    private String prov_razsoc;
    private long prodet_tel;
    private long prodet_cel;
    private long prodet_fax;
    private String prodet_usuadd;
    private Date prodet_fecadd;
    private String prodet_usumod;
    private Date prodet_fecmod;
    private int prodet_est;
    private boolean val = false;
    private String ind_accion = "Q";

    public ProvDet(int prodet_id, long prov_key, long prodet_tel, long prodet_cel, long prodet_fax, String prodet_usuadd, String prodet_usumod, int prodet_est) {
        this.prodet_id = prodet_id;
        this.prov_key = prov_key;
        this.prodet_tel = prodet_tel;
        this.prodet_cel = prodet_cel;
        this.prodet_fax = prodet_fax;
        this.prodet_usuadd = prodet_usuadd;
        this.prodet_usumod = prodet_usumod;
        this.prodet_est = prodet_est;
    }

    public ProvDet() {
    }

    public long getProdet_cel() {
        return prodet_cel;
    }

    public void setProdet_cel(long prodet_cel) {
        this.prodet_cel = prodet_cel;
    }

    public int getProdet_est() {
        if (prodet_est == 1) {
            val = true;
        }
        return prodet_est;
    }

    public void setProdet_est(int prodet_est) {
        this.prodet_est = prodet_est;
    }

    public long getProdet_fax() {
        return prodet_fax;
    }

    public void setProdet_fax(long prodet_fax) {
        this.prodet_fax = prodet_fax;
    }

    public Date getProdet_fecadd() {
        return prodet_fecadd;
    }

    public void setProdet_fecadd(Date prodet_fecadd) {
        this.prodet_fecadd = prodet_fecadd;
    }

    public Date getProdet_fecmod() {
        return prodet_fecmod;
    }

    public void setProdet_fecmod(Date prodet_fecmod) {
        this.prodet_fecmod = prodet_fecmod;
    }

    public int getProdet_id() {
        return prodet_id;
    }

    public void setProdet_id(int prodet_id) {
        this.prodet_id = prodet_id;
    }

    public long getProdet_tel() {
        return prodet_tel;
    }

    public void setProdet_tel(long prodet_tel) {
        this.prodet_tel = prodet_tel;
    }

    public String getProdet_usuadd() {
        return prodet_usuadd;
    }

    public void setProdet_usuadd(String prodet_usuadd) {
        this.prodet_usuadd = prodet_usuadd;
    }

    public String getProdet_usumod() {
        return prodet_usumod;
    }

    public void setProdet_usumod(String prodet_usumod) {
        this.prodet_usumod = prodet_usumod;
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

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

}
