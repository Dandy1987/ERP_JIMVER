package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class Mesa {

    private int mes_key;
    private String mes_id;
    private String mes_des;
    private int mes_canalid;
    private String mes_nomrep;
    private int mes_ord;
    private String mes_usuadd;
    private Date mes_fecadd;
    private String mes_usumod;
    private Date mes_fecmod;
    private int suc_id;
    private int emp_id;
    private int sup_key;
    private String sup_id;
    private int mes_est;
    private boolean valor;
    private String mes_canaldes;
    private String sup_apenom;

    public Mesa() {
    }

    public Mesa(int mes_key, String mes_id, String mes_des, int mes_canalid, String mes_nomrep, int mes_ord, String mes_usuadd, String mes_usumod, int suc_id, int emp_id, int sup_key, String sup_id, int mes_est) {
        this.mes_key = mes_key;
        this.mes_id = mes_id;
        this.mes_des = mes_des;
        this.mes_canalid = mes_canalid;
        this.mes_nomrep = mes_nomrep;
        this.mes_ord = mes_ord;
        this.mes_usuadd = mes_usuadd;
        this.mes_usumod = mes_usumod;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.sup_key = sup_key;
        this.sup_id = sup_id;
        this.mes_est = mes_est;
    }

    public int getMes_key() {
        return mes_key;
    }

    public void setMes_key(int mes_key) {
        this.mes_key = mes_key;
    }

    public String getMes_id() {
        return mes_id;
    }

    public void setMes_id(String mes_id) {
        this.mes_id = mes_id;
    }

    public String getMes_des() {
        return mes_des;
    }

    public void setMes_des(String mes_des) {
        this.mes_des = mes_des;
    }

    public int getMes_canalid() {
        return mes_canalid;
    }

    public void setMes_canalid(int mes_canalid) {
        this.mes_canalid = mes_canalid;
    }

    public String getMes_nomrep() {
        return mes_nomrep;
    }

    public void setMes_nomrep(String mes_nomrep) {
        this.mes_nomrep = mes_nomrep;
    }

    public int getMes_ord() {
        return mes_ord;
    }

    public void setMes_ord(int mes_ord) {
        this.mes_ord = mes_ord;
    }

    public String getMes_usuadd() {
        return mes_usuadd;
    }

    public void setMes_usuadd(String mes_usuadd) {
        this.mes_usuadd = mes_usuadd;
    }

    public Date getMes_fecadd() {
        return mes_fecadd;
    }

    public void setMes_fecadd(Date mes_fecadd) {
        this.mes_fecadd = mes_fecadd;
    }

    public String getMes_usumod() {
        return mes_usumod;
    }

    public void setMes_usumod(String mes_usumod) {
        this.mes_usumod = mes_usumod;
    }

    public Date getMes_fecmod() {
        return mes_fecmod;
    }

    public void setMes_fecmod(Date mes_fecmod) {
        this.mes_fecmod = mes_fecmod;
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

    public int getSup_key() {
        return sup_key;
    }

    public void setSup_key(int sup_key) {
        this.sup_key = sup_key;
    }

    public String getSup_id() {
        return sup_id;
    }

    public void setSup_id(String sup_id) {
        this.sup_id = sup_id;
    }

    public int getMes_est() {
        return mes_est;
    }

    public void setMes_est(int mes_est) {
        this.mes_est = mes_est;
    }

    public boolean isValor() {
        if (mes_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getMes_canaldes() {
        return mes_canaldes;
    }

    public void setMes_canaldes(String mes_canaldes) {
        this.mes_canaldes = mes_canaldes;
    }

    public String getSup_apenom() {
        return sup_apenom;
    }

    public void setSup_apenom(String sup_apenom) {
        this.sup_apenom = sup_apenom;
    }
}
