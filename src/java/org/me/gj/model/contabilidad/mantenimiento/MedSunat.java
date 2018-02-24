/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.contabilidad.mantenimiento;

/**
 *
 * @author jmayuri
 */
public class MedSunat {
    private int tab_id;
    private String tab_subdes;
    private String tab_subdir; 
    private int tab_est;

    public MedSunat() {
    }

    public MedSunat(int tab_id, String tab_subdes, int tab_est, String tab_subdir) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_est=tab_est;
        this.tab_subdir=tab_subdir;
    }

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
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

    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }
    
}
