package org.me.gj.model.seguridad.mantenimiento;

import java.util.Date;

public class Empresas {

    private int emp_id;
    private String emp_sig;
    private String emp_des;
    private String emp_dir;
    private Long emp_ruc;
    private int emp_est;
    private String emp_usuadd;
    private String emp_usumod;
    private Date emp_feccadd;
    private Date emp_fecmod;
    private boolean valor;

    public Empresas(int emp_id, String emp_sig, String emp_des, String emp_dir, Long emp_ruc, int emp_est, String emp_usuadd, String emp_usumod) {
        this.emp_id = emp_id;
        this.emp_sig = emp_sig;
        this.emp_des = emp_des;
        this.emp_dir = emp_dir;
        this.emp_ruc = emp_ruc;
        this.emp_est = emp_est;
        this.emp_usuadd = emp_usuadd;
        this.emp_usumod = emp_usumod;
    }

    public Empresas() {
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_sig() {
        return emp_sig;
    }

    public void setEmp_sig(String emp_sig) {
        this.emp_sig = emp_sig;
    }

    public String getEmp_des() {
        return emp_des;
    }

    public void setEmp_des(String emp_des) {
        this.emp_des = emp_des;
    }

    public String getEmp_dir() {
        return emp_dir;
    }

    public void setEmp_dir(String emp_dir) {
        this.emp_dir = emp_dir;
    }

    public int getEmp_est() {
        return emp_est;
    }

    public void setEmp_est(int emp_est) {
        this.emp_est = emp_est;
    }

    public Long getEmp_ruc() {
        return emp_ruc;
    }

    public void setEmp_ruc(Long emp_ruc) {
        this.emp_ruc = emp_ruc;
    }

    public boolean isValor() {
        if (emp_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getEmp_usuadd() {
        return emp_usuadd;
    }

    public void setEmp_usuadd(String emp_usuadd) {
        this.emp_usuadd = emp_usuadd;
    }

    public String getEmp_usumod() {
        return emp_usumod;
    }

    public void setEmp_usumod(String emp_usumod) {
        this.emp_usumod = emp_usumod;
    }

    public Date getEmp_feccadd() {
        return emp_feccadd;
    }

    public void setEmp_feccadd(Date emp_feccadd) {
        this.emp_feccadd = emp_feccadd;
    }

    public Date getEmp_fecmod() {
        return emp_fecmod;
    }

    public void setEmp_fecmod(Date emp_fecmod) {
        this.emp_fecmod = emp_fecmod;
    }
}
