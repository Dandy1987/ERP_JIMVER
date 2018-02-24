package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class TipoVenta {

    private int tab_id;
    private String tab_subdes;
    private int tab_est;
    private String tab_subdir;
    private int tab_ord;
    private int tab_tip;
    private String tab_nomrep;
    private boolean valorEst;
    private boolean valorBon;
    private String tab_usuadd;
    private Date tab_fecadd;
    private String tab_usumod;
    private Date tab_fecmod;

    public TipoVenta() {
    }

    public TipoVenta(int tab_id, String tab_subdes) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
    }

    public TipoVenta(int tab_id, String tab_subdes, int tab_est, int tab_ord, int tab_tip, String tab_nomrep,
            String tab_usuadd, String tab_usumod) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_est = tab_est;
        this.tab_ord = tab_ord;
        this.tab_tip = tab_tip;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;

    }

    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }

    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public String getTab_nomrep() {
        return tab_nomrep;
    }

    public void setTab_nomrep(String tab_nomrep) {
        this.tab_nomrep = tab_nomrep;
    }

    public int getTab_ord() {
        return tab_ord;
    }

    public void setTab_ord(int tab_ord) {
        this.tab_ord = tab_ord;
    }

    public int getTab_tip() {
        return tab_tip;
    }

    public void setTab_tip(int tab_tip) {
        this.tab_tip = tab_tip;
    }

    public String getTab_subdes() {
        return tab_subdes;
    }

    public void setTab_subdes(String tab_subdes) {
        this.tab_subdes = tab_subdes;
    }

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
    }

    public boolean isValorEst() {
        if (tab_est == 1) {
            valorEst = true;
        } else {
            valorEst = false;
        }
        return valorEst;
    }

    public void setValorEst(boolean valorEst) {
        this.valorEst = valorEst;
    }

    public boolean isValorBon() {
        if (tab_tip == 1) {
            valorBon = true;
        } else {
            valorBon = false;
        }
        return valorBon;
    }

    public void setValorBon(boolean valorBon) {
        this.valorBon = valorBon;
    }

    public String getTab_usuadd() {
        return tab_usuadd;
    }

    public void setTab_usuadd(String tab_usuadd) {
        this.tab_usuadd = tab_usuadd;
    }

    public Date getTab_fecadd() {
        return tab_fecadd;
    }

    public void setTab_fecadd(Date tab_feadd) {
        this.tab_fecadd = tab_feadd;
    }

    public String getTab_usumod() {
        return tab_usumod;
    }

    public void setTab_usumod(String tab_usumod) {
        this.tab_usumod = tab_usumod;
    }

    public Date getTab_fecmod() {
        return tab_fecmod;
    }

    public void setTab_fecmod(Date tab_fecmod) {
        this.tab_fecmod = tab_fecmod;
    }

}
