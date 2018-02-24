package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class ProgramacionZona {

    private int prog_key;
    private String prog_zonid;
    private int suc_id;
    private int emp_id;
    private int prog_trans;
    private String prog_transid;
    private String prog_horentid;
    private String prog_glosa;
    private int prog_est;
    private String prog_usuadd;
    private Date prog_fecadd;
    private String prog_usumod;
    private Date prog_fecmod;
    private int prog_ord;
    private String prog_nomrep;
    private String prog_zondes;
    private String prog_transalias;
    private String prog_horentdes;
    private boolean valor;

    public ProgramacionZona() {
    }

    public ProgramacionZona(int prog_key, String prog_zonid, int suc_id, int emp_id, int prog_trans, String prog_transid, String prog_horentid, String prog_glosa, int prog_est, String prog_usuadd, String prog_usumod, int prog_ord, String prog_nomrep) {
        this.prog_key = prog_key;
        this.prog_zonid = prog_zonid;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.prog_trans = prog_trans;
        this.prog_transid = prog_transid;
        this.prog_horentid = prog_horentid;
        this.prog_glosa = prog_glosa;
        this.prog_est = prog_est;
        this.prog_usuadd = prog_usuadd;
        this.prog_usumod = prog_usumod;
        this.prog_ord = prog_ord;
        this.prog_nomrep = prog_nomrep;
    }

    public int getProg_key() {
        return prog_key;
    }

    public void setProg_key(int prog_key) {
        this.prog_key = prog_key;
    }

    public String getProg_zonid() {
        return prog_zonid;
    }

    public void setProg_zonid(String prog_zonid) {
        this.prog_zonid = prog_zonid;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getProg_transid() {
        return prog_transid;
    }

    public void setProg_transid(String prog_transid) {
        this.prog_transid = prog_transid;
    }

    public String getProg_horentid() {
        return prog_horentid;
    }

    public void setProg_horentid(String prog_horentid) {
        this.prog_horentid = prog_horentid;
    }

    public String getProg_glosa() {
        return prog_glosa;
    }

    public void setProg_glosa(String prog_glosa) {
        this.prog_glosa = prog_glosa;
    }

    public int getProg_est() {
        return prog_est;
    }

    public void setProg_est(int prog_est) {
        this.prog_est = prog_est;
    }

    public String getProg_usuadd() {
        return prog_usuadd;
    }

    public void setProg_usuadd(String prog_usuadd) {
        this.prog_usuadd = prog_usuadd;
    }

    public Date getProg_fecadd() {
        return prog_fecadd;
    }

    public void setProg_fecadd(Date prog_fecadd) {
        this.prog_fecadd = prog_fecadd;
    }

    public String getProg_usumod() {
        return prog_usumod;
    }

    public void setProg_usumod(String prog_usumod) {
        this.prog_usumod = prog_usumod;
    }

    public Date getProg_fecmod() {
        return prog_fecmod;
    }

    public void setProg_fecmod(Date prog_fecmod) {
        this.prog_fecmod = prog_fecmod;
    }

    public int getProg_ord() {
        return prog_ord;
    }

    public void setProg_ord(int prog_ord) {
        this.prog_ord = prog_ord;
    }

    public String getProg_nomrep() {
        return prog_nomrep;
    }

    public void setProg_nomrep(String prog_nomrep) {
        this.prog_nomrep = prog_nomrep;
    }

    public String getProg_zondes() {
        return prog_zondes;
    }

    public void setProg_zondes(String prog_zondes) {
        this.prog_zondes = prog_zondes;
    }

    public String getProg_transalias() {
        return prog_transalias;
    }

    public void setProg_transalias(String prog_transalias) {
        this.prog_transalias = prog_transalias;
    }

    public String getProg_horentdes() {
        return prog_horentdes;
    }

    public void setProg_horentdes(String prog_horentdes) {
        this.prog_horentdes = prog_horentdes;
    }

    public boolean isValor() {
        if (prog_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getProg_trans() {
        return prog_trans;
    }

    public void setProg_trans(int prog_trans) {
        this.prog_trans = prog_trans;
    }

}
