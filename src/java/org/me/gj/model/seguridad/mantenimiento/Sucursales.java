package org.me.gj.model.seguridad.mantenimiento;

import java.util.Date;

public class Sucursales {

    private int suc_id;
    private String suc_des;
    private String emp_sig;
    private String suc_dir;
    private int suc_ord;
    private int emp_id;
    private int suc_est;
    private long suc_telef;
    private String suc_fax;
    private String suc_usuadd;
    private Date suc_fecadd;
    private String suc_usumod;
    private Date suc_fecmod;
    private boolean valor;
	private boolean valSelec;

    public Sucursales(int suc_id, String suc_des) {
        this.suc_id = suc_id;
        this.suc_des = suc_des;
    }

    public Sucursales(int suc_id, String emp_sig, String suc_des, int emp_id, String suc_dir, long suc_telef, String suc_fax, int suc_est,
            int suc_ord, String suc_usuadd, String suc_usumod) {
        this.suc_id = suc_id;
        this.emp_sig = emp_sig;
        this.suc_des = suc_des;
        this.emp_id = emp_id;
        this.suc_dir = suc_dir;
        this.suc_telef = suc_telef;
        this.suc_fax = suc_fax;
        this.suc_est = suc_est;
        this.suc_ord = suc_ord;
        this.suc_usuadd = suc_usuadd;
        this.suc_usumod = suc_usumod;
    }

    public Sucursales() {
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getSuc_ord() {
        return suc_ord;
    }

    public void setSuc_ord(int suc_ord) {
        this.suc_ord = suc_ord;
    }

    public boolean isValor() {
        if (suc_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getSuc_des() {
        return suc_des;
    }

    public void setSuc_des(String suc_des) {
        this.suc_des = suc_des;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getSuc_est() {
        return suc_est;
    }

    public void setSuc_est(int suc_est) {
        this.suc_est = suc_est;
    }

    public String getEmp_sig() {
        return emp_sig;
    }

    public void setEmp_sig(String emp_sig) {
        this.emp_sig = emp_sig;
    }

    public String getSuc_dir() {
        return suc_dir;
    }

    public void setSuc_dir(String suc_dir) {
        this.suc_dir = suc_dir;
    }

    public long getSuc_telef() {
        return suc_telef;
    }

    public void setSuc_telef(long suc_telef) {
        this.suc_telef = suc_telef;
    }

    public String getSuc_fax() {
        return suc_fax;
    }

    public void setSuc_fax(String suc_fax) {
        this.suc_fax = suc_fax;
    }

    public Date getSuc_fecadd() {
        return suc_fecadd;
    }

    public void setSuc_fecadd(Date suc_fecadd) {
        this.suc_fecadd = suc_fecadd;
    }

    public Date getSuc_fecmod() {
        return suc_fecmod;
    }

    public void setSuc_fecmod(Date suc_fecmod) {
        this.suc_fecmod = suc_fecmod;
    }

    public String getSuc_usuadd() {
        return suc_usuadd;
    }

    public void setSuc_usuadd(String suc_usuadd) {
        this.suc_usuadd = suc_usuadd;
    }

    public String getSuc_usumod() {
        return suc_usumod;
    }

    public void setSuc_usumod(String suc_usumod) {
        this.suc_usumod = suc_usumod;
    }
	
    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
	
}
