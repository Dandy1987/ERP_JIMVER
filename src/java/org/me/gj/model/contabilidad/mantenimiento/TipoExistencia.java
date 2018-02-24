
package org.me.gj.model.contabilidad.mantenimiento;

public class TipoExistencia {
    private int tab_id;
    private String tab_subdes;
    private int tab_tip;
    private String tab_nomrep;
    private int tab_est;
    private boolean val;
        
    public TipoExistencia(int tab_id, String tab_subdes, int tab_tip, String tab_nomrep, int tab_est) {
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_tip = tab_tip;
        this.tab_nomrep = tab_nomrep;
        this.tab_est = tab_est;
        
    }

    public TipoExistencia() {
    }

    public TipoExistencia(int i_tab_id, String s_tab_subdes, int i_tab_tip, String s_tab_nomrep,int i_tab_est,boolean tab_val) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public int getTab_tip() {
        return tab_tip;
    }

    public void setTab_tip(int tab_tip) {
        this.tab_tip = tab_tip;
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

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

  
   
        
    
    
}
