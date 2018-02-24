package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class Ruta {

    private int rut_key;
    private String rut_id;
    private int suc_id;
    private int emp_id;
    private int rut_est;
    private int rut_canalid;
    private int rut_mesaid;
    private int rut_corid;
    private String rut_usuadd;
    private Date rut_fecadd;
    private String rut_usumod;
    private Date rut_fecmod;
    private int rut_ord;
    private String rut_nomrep;
    private boolean valor;
    private String rut_canaldes;
    private String rut_supapenom;
    private String mes_id;
    private String can_id;

    public Ruta() {
    }

    public Ruta(int rut_key, String rut_id, int suc_id, int emp_id, int rut_est, int rut_canalid, int rut_mesaid, int rut_corid, String rut_usuadd, String rut_usumod, int rut_ord, String rut_nomrep) {
        this.rut_key = rut_key;
        this.rut_id = rut_id;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.rut_est = rut_est;
        this.rut_canalid = rut_canalid;
        this.rut_mesaid = rut_mesaid;
        this.rut_corid = rut_corid;
        this.rut_usuadd = rut_usuadd;
        this.rut_usumod = rut_usumod;
        this.rut_ord = rut_ord;
        this.rut_nomrep = rut_nomrep;
    }

    public String getRut_id() {
        return rut_id;
    }

    public void setRut_id(String rut_id) {
        this.rut_id = rut_id;
    }

    public int getRut_canalid() {
        return rut_canalid;
    }

    public void setRut_canalid(int rut_canalid) {
        this.rut_canalid = rut_canalid;
    }

    public int getRut_mesaid() {
        return rut_mesaid;
    }

    public void setRut_mesaid(int rut_mesaid) {
        this.rut_mesaid = rut_mesaid;
    }

    public int getRut_est() {
        return rut_est;
    }

    public void setRut_est(int rut_est) {
        this.rut_est = rut_est;
    }

    public int getRut_corid() {
        return rut_corid;
    }

    public void setRut_corid(int rut_corid) {
        this.rut_corid = rut_corid;
    }

    public int getRut_ord() {
        return rut_ord;
    }

    public void setRut_ord(int rut_ord) {
        this.rut_ord = rut_ord;
    }

    public String getRut_nomrep() {
        return rut_nomrep;
    }

    public void setRut_nomrep(String rut_nomrep) {
        this.rut_nomrep = rut_nomrep;
    }

    public String getRut_usuadd() {
        return rut_usuadd;
    }

    public void setRut_usuadd(String rut_usuadd) {
        this.rut_usuadd = rut_usuadd;
    }

    public Date getRut_fecadd() {
        return rut_fecadd;
    }

    public void setRut_fecadd(Date rut_fecadd) {
        this.rut_fecadd = rut_fecadd;
    }

    public String getRut_usumod() {
        return rut_usumod;
    }

    public void setRut_usumod(String rut_usumod) {
        this.rut_usumod = rut_usumod;
    }

    public Date getRut_fecmod() {
        return rut_fecmod;
    }

    public void setRut_fecmod(Date rut_fecmod) {
        this.rut_fecmod = rut_fecmod;
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

    public int getRut_key() {
        return rut_key;
    }

    public void setRut_key(int rut_key) {
        this.rut_key = rut_key;
    }

    public boolean isValor() {
        if (rut_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getRut_canaldes() {
        return rut_canaldes;
    }

    public void setRut_canaldes(String rut_canaldes) {
        this.rut_canaldes = rut_canaldes;
    }

    public String getRut_supapenom() {
        return rut_supapenom;
    }

    public void setRut_supapenom(String rut_supapenom) {
        this.rut_supapenom = rut_supapenom;
    }

    public String getMes_id() {
        return mes_id;
    }

    public void setMes_id(String mes_id) {
        this.mes_id = mes_id;
    }

    public String getCan_id() {
        return can_id;
    }

    public void setCan_id(String can_id) {
        this.can_id = can_id;
    }

}
