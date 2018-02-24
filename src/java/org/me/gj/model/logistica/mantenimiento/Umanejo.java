package org.me.gj.model.logistica.mantenimiento;

public class Umanejo {

    private int tab_empid;
    private int tab_sucid;
    private int tab_id;
    private String tab_subdes;
    private int tab_est;
    private String tab_nomrep;
    private int tab_ord;

    public Umanejo(int tab_empid, int tab_sucid, int tab_id, String tab_subdes, int tab_est, String tab_nomrep, int tab_ord) {
        this.tab_empid = tab_empid;
        this.tab_sucid = tab_sucid;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_ord = tab_ord;
    }

    public Umanejo() {
    }

    public int getTab_empid() {
        return tab_empid;
    }

    public void setTab_empid(int tab_empid) {
        this.tab_empid = tab_empid;
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

    public String getTab_subdes() {
        return tab_subdes;
    }

    public void setTab_subdes(String tab_subdes) {
        this.tab_subdes = tab_subdes;
    }

    public int getTab_sucid() {
        return tab_sucid;
    }

    public void setTab_sucid(int tab_sucid) {
        this.tab_sucid = tab_sucid;
    }

}
