package org.me.gj.model.facturacion.mantenimiento;

public class Pais {

    private int tab_id;
    private String tab_subdes;
    private String tab_subdir;
    private int tab_ord;
    private String tab_nomrep;
    private int tab_est;
    private boolean valor;

    public Pais() {
    }

    public Pais(int tab_id, String tab_subdes, String tab_subdir, int tab_ord, String tab_nomrep, int tab_est) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_ord = tab_ord;
        this.tab_nomrep = tab_nomrep;
        this.tab_est = tab_est;
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

    public int getTab_ord() {
        return tab_ord;
    }

    public void setTab_ord(int tab_ord) {
        this.tab_ord = tab_ord;
    }

    public String getTab_nomrep() {
        return tab_nomrep;
    }

    public void setTab_nomrep(String tab_nomrep) {
        this.tab_nomrep = tab_nomrep;
    }

    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }

    public boolean isValor() {
        valor = tab_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

}
