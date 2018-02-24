package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;
import org.me.gj.util.Utilitarios;

public class Umedida {

    private int tab_id;
    private String tab_subdes;
    private String tab_subdir;
    private int tab_tip;
    private int tab_est;
    private String tab_nomrep;
    private String tab_usuadd;
    private Date tab_fecadd;
    private String tab_usumod;
    private Date tab_fecmod;
    private boolean valor;
    private String stab_id;

    public Umedida() {
    }

    public Umedida(int tab_id, String tab_subdes, String tab_subdir, int tab_tip, int tab_est, String tab_nomrep, String tab_usuadd, String tab_usumod) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_tip = tab_tip;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;
    }

    public Umedida(int tab_id, String tab_subdes, int tab_tip, String tab_subdir, int tab_est, String tab_nomrep, String tab_usuadd, String tab_usumod) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_tip = tab_tip;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;
    }

    public boolean isValor() {
        valor = tab_est == 1;
        return valor;
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

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
    }

    public int getTab_tip() {
        return tab_tip;
    }

    public void setTab_tip(int tab_tip) {
        this.tab_tip = tab_tip;
    }

    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }

    public String getTab_nomrep() {
        return tab_nomrep;
    }

    public void setTab_nomrep(String tab_nomrep) {
        this.tab_nomrep = tab_nomrep;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
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

    public String getStab_id() {
        stab_id = Utilitarios.lpad(String.valueOf(tab_id), 4, "0"); 
        return stab_id;
    }

    public void setStab_id(String stab_id) {
        this.stab_id = stab_id;
    }
    
}
