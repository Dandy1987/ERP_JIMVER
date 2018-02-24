package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class Supervisor {

    private int sup_key;
    private String sup_id;
    private String sup_apenom;
    private String sup_dni;
    private String sup_direcc;
    private long sup_telf;
    private long sup_movil;
    private Date sup_fecnac;
    private int sup_est;
    private String sup_usuadd;
    private Date sup_fecadd;
    private String sup_usumod;
    private Date sup_fecmod;
    private String sup_pssw;
    private int sup_flagcie;
    private String sup_apoyo;
    private int suc_id;
    private int emp_id;
    private String sup_nomrep;
    private int sup_ord;
    private boolean valor;
    private String sup_apoyodes;

    public Supervisor() {
    }

    public Supervisor(String sup_id, String sup_apenom) {
        super();
        this.sup_id = sup_id;
        this.sup_apenom = sup_apenom;
    }

    public Supervisor(int sup_key, String sup_id, String sup_apenom, String sup_dni, String sup_direcc, long sup_telf, long sup_movil, Date sup_fecnac, int sup_est, String sup_usuadd, String sup_usumod, String sup_pssw, int sup_flagcie, String sup_apoyo, int suc_id, int emp_id, String sup_nomrep, int sup_ord) {
        this.sup_key = sup_key;
        this.sup_id = sup_id;
        this.sup_apenom = sup_apenom;
        this.sup_dni = sup_dni;
        this.sup_direcc = sup_direcc;
        this.sup_telf = sup_telf;
        this.sup_movil = sup_movil;
        this.sup_fecnac = sup_fecnac;
        this.sup_est = sup_est;
        this.sup_usuadd = sup_usuadd;
        this.sup_usumod = sup_usumod;
        this.sup_pssw = sup_pssw;
        this.sup_flagcie = sup_flagcie;
        this.sup_apoyo = sup_apoyo;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.sup_nomrep = sup_nomrep;
        this.sup_ord = sup_ord;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public String getSup_apenom() {
        return sup_apenom;
    }

    public void setSup_apenom(String sup_apenom) {
        this.sup_apenom = sup_apenom;
    }

    public String getSup_apoyo() {
        return sup_apoyo;
    }

    public void setSup_apoyo(String sup_apoyo) {
        this.sup_apoyo = sup_apoyo;
    }

    public String getSup_direcc() {
        return sup_direcc;
    }

    public void setSup_direcc(String sup_direcc) {
        this.sup_direcc = sup_direcc;
    }

    public String getSup_dni() {
        return sup_dni;
    }

    public void setSup_dni(String sup_dni) {
        this.sup_dni = sup_dni;
    }

    public int getSup_est() {
        return sup_est;
    }

    public void setSup_est(int sup_est) {
        this.sup_est = sup_est;
    }

    public Date getSup_fecadd() {
        return sup_fecadd;
    }

    public void setSup_fecadd(Date sup_fecadd) {
        this.sup_fecadd = sup_fecadd;
    }

    public Date getSup_fecmod() {
        return sup_fecmod;
    }

    public void setSup_fecmod(Date sup_fecmod) {
        this.sup_fecmod = sup_fecmod;
    }

    public Date getSup_fecnac() {
        return sup_fecnac;
    }

    public void setSup_fecnac(Date sup_fecnac) {
        this.sup_fecnac = sup_fecnac;
    }

    public int getSup_flagcie() {
        return sup_flagcie;
    }

    public void setSup_flagcie(int sup_flagcie) {
        this.sup_flagcie = sup_flagcie;
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

    public long getSup_movil() {
        return sup_movil;
    }

    public void setSup_movil(long sup_movil) {
        this.sup_movil = sup_movil;
    }

    public String getSup_nomrep() {
        return sup_nomrep;
    }

    public void setSup_nomrep(String sup_nomrep) {
        this.sup_nomrep = sup_nomrep;
    }

    public int getSup_ord() {
        return sup_ord;
    }

    public void setSup_ord(int sup_ord) {
        this.sup_ord = sup_ord;
    }

    public String getSup_pssw() {
        return sup_pssw;
    }

    public void setSup_pssw(String sup_pssw) {
        this.sup_pssw = sup_pssw;
    }

    public long getSup_telf() {
        return sup_telf;
    }

    public void setSup_telf(long sup_telf) {
        this.sup_telf = sup_telf;
    }

    public String getSup_usuadd() {
        return sup_usuadd;
    }

    public void setSup_usuadd(String sup_usuadd) {
        this.sup_usuadd = sup_usuadd;
    }

    public String getSup_usumod() {
        return sup_usumod;
    }

    public void setSup_usumod(String sup_usumod) {
        this.sup_usumod = sup_usumod;
    }

    public boolean isValor() {
        if (sup_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getSup_apoyodes() {
        return sup_apoyodes;
    }

    public void setSup_apoyodes(String sup_apoyodes) {
        this.sup_apoyodes = sup_apoyodes;
    }
}
