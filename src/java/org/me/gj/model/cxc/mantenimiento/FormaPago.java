package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class FormaPago {

    private int forpag_key;
    private String forpag_id;
    private String forpag_des;
    private int forpag_caj;
    private int forpag_com;
    private String forpag_lov;
    private int forpag_est;
    private int forpag_ord;
    private String forpag_nomrep;
    private String forpag_usuadd;
    private Date forpag_fecadd;
    private String forpag_usumod;
    private Date forpag_fecmod;
    private boolean valorEst;
    private boolean valorCaj;
    private boolean valorCom;

    public FormaPago() {
    }

    public FormaPago(int forpag_key, String forpag_id, String forpag_des, int forpag_caj, int forpag_com, String forpag_lov, int forpag_est, int forpag_ord, String forpag_nomrep, String forpag_usuadd, String forpag_usumod) {
        this.forpag_key = forpag_key;
        this.forpag_id = forpag_id;
        this.forpag_des = forpag_des;
        this.forpag_caj = forpag_caj;
        this.forpag_com = forpag_com;
        this.forpag_lov = forpag_lov;
        this.forpag_est = forpag_est;
        this.forpag_ord = forpag_ord;
        this.forpag_nomrep = forpag_nomrep;
        this.forpag_usuadd = forpag_usuadd;
        this.forpag_usumod = forpag_usumod;
    }

    public int getForpag_key() {
        return forpag_key;
    }

    public void setForpag_key(int forpag_key) {
        this.forpag_key = forpag_key;
    }

    public String getForpag_id() {
        return forpag_id;
    }

    public void setForpag_id(String forpag_id) {
        this.forpag_id = forpag_id;
    }

    public String getForpag_des() {
        return forpag_des;
    }

    public void setForpag_des(String forpag_des) {
        this.forpag_des = forpag_des;
    }

    public int getForpag_caj() {
        return forpag_caj;
    }

    public void setForpag_caj(int forpag_caj) {
        this.forpag_caj = forpag_caj;
    }

    public int getForpag_com() {
        return forpag_com;
    }

    public void setForpag_com(int forpag_com) {
        this.forpag_com = forpag_com;
    }

    public String getForpag_lov() {
        return forpag_lov;
    }

    public void setForpag_lov(String forpag_lov) {
        this.forpag_lov = forpag_lov;
    }

    public int getForpag_est() {
        return forpag_est;
    }

    public void setForpag_est(int forpag_est) {
        this.forpag_est = forpag_est;
    }

    public int getForpag_ord() {
        return forpag_ord;
    }

    public void setForpag_ord(int forpag_ord) {
        this.forpag_ord = forpag_ord;
    }

    public String getForpag_nomrep() {
        return forpag_nomrep;
    }

    public void setForpag_nomrep(String forpag_nomrep) {
        this.forpag_nomrep = forpag_nomrep;
    }

    public String getForpag_usuadd() {
        return forpag_usuadd;
    }

    public void setForpag_usuadd(String forpag_usuadd) {
        this.forpag_usuadd = forpag_usuadd;
    }

    public Date getForpag_fecadd() {
        return forpag_fecadd;
    }

    public void setForpag_fecadd(Date forpag_fecadd) {
        this.forpag_fecadd = forpag_fecadd;
    }

    public String getForpag_usumod() {
        return forpag_usumod;
    }

    public void setForpag_usumod(String forpag_usumod) {
        this.forpag_usumod = forpag_usumod;
    }

    public Date getForpag_fecmod() {
        return forpag_fecmod;
    }

    public void setForpag_fecmod(Date forpag_fecmod) {
        this.forpag_fecmod = forpag_fecmod;
    }

    public boolean isValorEst() {
        if (forpag_est == 1) {
            valorEst = true;
        } else {
            valorEst = false;
        }
        return valorEst;
    }

    public void setValorEst(boolean valorEst) {
        this.valorEst = valorEst;
    }

    public boolean isValorCaj() {
        if (forpag_caj == 1) {
            valorCaj = true;
        } else {
            valorCaj = false;
        }
        return valorCaj;
    }

    public void setValorCaj(boolean valorCaj) {

    }

    public boolean isValorCom() {
        if (forpag_com == 1) {
            valorCom = true;
        } else {
            valorCom = false;
        }
        return valorCom;
    }

    public void setValorCom(boolean valorCom) {
        this.valorCom = valorCom;
    }

}
