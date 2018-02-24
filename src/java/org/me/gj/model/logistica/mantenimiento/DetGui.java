
package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class DetGui {
    private int tab_id;
    private String tab_subdes;
    private String tab_subdir;
    private int tab_est;
    private String tab_nomrep;
    private int tab_ord;
    private boolean valor;
    private String tab_usuadd;
    private Date tab_fecadd;
    private String tab_usumod;
    private Date tab_fecmod;

    public DetGui() {
    }

    public DetGui(int tab_id, String tab_subdes, int tab_est, String tab_nomrep, int tab_ord,String tab_usuadd,String tab_usumod) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_ord = tab_ord;
        this.tab_usuadd=tab_usuadd;
        this.tab_usumod=tab_usumod;
    }
    
    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
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

    public boolean isValor() {
        valor=tab_est==1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
    
    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public String getTab_subdes() {
        return tab_subdes;
    }

    public void setTab_subdes(String tab_subdes) {
        this.tab_subdes = tab_subdes;
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

    public void setTab_fecadd(Date tab_fecadd) {
        this.tab_fecadd = tab_fecadd;
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
