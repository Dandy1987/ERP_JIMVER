/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author ROJAS
 */
public class PersAportacion {
    
    private int pltipdoc;
    private String plnrodoc;
    private int correl_ap;
    private int plestado_ap;
    private String plregpen;
    private Date plfiregpen;
    private String pltippen;
    private String plcodafp;
    private String plcarafp;
    private int plcommix;
    private String plpressal;
    private String plsiteps;
    private Date plfbeps;
    private int plsct_as;
    private int plsct_pp;
    private String plusuadd;
    private Date plfecadd;
    private String plusumod;
    private Date plfecmod;
    
    //Utilitarios
    private String plregpen_des;
    private String plcodafp_des;
    private String plpressal_des;
    private String plsiteps_des;
    private String indsql;

    public PersAportacion() {
    }

    public PersAportacion(String indsql, String plregpen, Date plfiregpen, String pltippen, String plcodafp, String plcarafp, 
            int plcommix, String plpressal, String plsiteps, Date plfbeps, int plsct_as, int plsct_pp) {
        this.indsql = indsql;
        this.plregpen = plregpen;
        this.plfiregpen = plfiregpen;
        this.pltippen = pltippen;
        this.plcodafp = plcodafp;
        this.plcarafp = plcarafp;
        this.plcommix = plcommix;
        this.plpressal = plpressal;
        this.plsiteps = plsiteps;
        this.plfbeps = plfbeps;
        this.plsct_as = plsct_as;
        this.plsct_pp = plsct_pp;
    }
    
    

    public int getPltipdoc() {
        return pltipdoc;
    }

    public void setPltipdoc(int pltipdoc) {
        this.pltipdoc = pltipdoc;
    }

    public String getPlnrodoc() {
        return plnrodoc;
    }

    public void setPlnrodoc(String plnrodoc) {
        this.plnrodoc = plnrodoc;
    }

    public int getCorrel_ap() {
        return correl_ap;
    }

    public void setCorrel_ap(int correl_ap) {
        this.correl_ap = correl_ap;
    }

    public int getPlestado_ap() {
        return plestado_ap;
    }

    public void setPlestado_ap(int plestado_ap) {
        this.plestado_ap = plestado_ap;
    }

    public String getPlregpen() {
        return plregpen;
    }

    public void setPlregpen(String plregpen) {
        this.plregpen = plregpen;
    }

    public Date getPlfiregpen() {
        return plfiregpen;
    }

    public void setPlfiregpen(Date plfiregpen) {
        this.plfiregpen = plfiregpen;
    }

    public String getPltippen() {
        return pltippen;
    }

    public void setPltippen(String pltippen) {
        this.pltippen = pltippen;
    }

    public String getPlcodafp() {
        return plcodafp;
    }

    public void setPlcodafp(String plcodafp) {
        this.plcodafp = plcodafp;
    }

    public String getPlcarafp() {
        return plcarafp;
    }

    public void setPlcarafp(String plcarafp) {
        this.plcarafp = plcarafp;
    }

    public int getPlcommix() {
        return plcommix;
    }

    public void setPlcommix(int plcommix) {
        this.plcommix = plcommix;
    }

    public String getPlpressal() {
        return plpressal;
    }

    public void setPlpressal(String plpressal) {
        this.plpressal = plpressal;
    }

    public String getPlsiteps() {
        return plsiteps;
    }

    public void setPlsiteps(String plsiteps) {
        this.plsiteps = plsiteps;
    }

    public Date getPlfbeps() {
        return plfbeps;
    }

    public void setPlfbeps(Date plfbeps) {
        this.plfbeps = plfbeps;
    }

    public int getPlsct_as() {
        return plsct_as;
    }

    public void setPlsct_as(int plsct_as) {
        this.plsct_as = plsct_as;
    }

    public int getPlsct_pp() {
        return plsct_pp;
    }

    public void setPlsct_pp(int plsct_pp) {
        this.plsct_pp = plsct_pp;
    }

    public String getPlusuadd() {
        return plusuadd;
    }

    public void setPlusuadd(String plusuadd) {
        this.plusuadd = plusuadd;
    }

    public Date getPlfecadd() {
        return plfecadd;
    }

    public void setPlfecadd(Date plfecadd) {
        this.plfecadd = plfecadd;
    }

    public String getPlusumod() {
        return plusumod;
    }

    public void setPlusumod(String plusumod) {
        this.plusumod = plusumod;
    }

    public Date getPlfecmod() {
        return plfecmod;
    }

    public void setPlfecmod(Date plfecmod) {
        this.plfecmod = plfecmod;
    }

    public String getIndsql() {
        return indsql;
    }

    public void setIndsql(String indsql) {
        this.indsql = indsql;
    }

    public String getPlregpen_des() {
        return plregpen_des;
    }

    public void setPlregpen_des(String plregpen_des) {
        this.plregpen_des = plregpen_des;
    }

    public String getPlcodafp_des() {
        return plcodafp_des;
    }

    public void setPlcodafp_des(String plcodafp_des) {
        this.plcodafp_des = plcodafp_des;
    }

    public String getPlpressal_des() {
        return plpressal_des;
    }

    public void setPlpressal_des(String plpressal_des) {
        this.plpressal_des = plpressal_des;
    }

    public String getPlsiteps_des() {
        return plsiteps_des;
    }

    public void setPlsiteps_des(String plsiteps_des) {
        this.plsiteps_des = plsiteps_des;
    }
    
}
