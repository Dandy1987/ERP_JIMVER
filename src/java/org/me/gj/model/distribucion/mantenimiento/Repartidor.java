package org.me.gj.model.distribucion.mantenimiento;

import java.util.Date;

public class Repartidor {

    private int emp_id;
    private int suc_id;
    private int rep_key;
    private String rep_id;
    private String rep_apepat;
    private String rep_apemat;
    private String rep_nom;
    private String rep_dni;
    private Date rep_fecnac;
    private int rep_telef;
    private String rep_direcc;
    private int rep_est;
    private String rep_usuadd;
    private Date rep_fecadd;
    private String rep_usumod;
    private Date rep_fecmod;

    //Auxiliares
    private String s_nomcompleto;
    private boolean valor;

    public Repartidor() {
    }

    public Repartidor(int rep_key, String rep_id,
            String rep_apepat, String rep_apemat, String rep_nom,
            String rep_dni, Date rep_fecnac, int rep_telef, String rep_direcc,
            int rep_est, String rep_usuadd, String rep_usumod) {
        super();
        this.rep_key = rep_key;
        this.rep_id = rep_id;
        this.rep_apepat = rep_apepat;
        this.rep_apemat = rep_apemat;
        this.rep_nom = rep_nom;
        this.rep_dni = rep_dni;
        this.rep_fecnac = rep_fecnac;
        this.rep_telef = rep_telef;
        this.rep_direcc = rep_direcc;
        this.rep_est = rep_est;
        this.rep_usuadd = rep_usuadd;
        this.rep_usumod = rep_usumod;
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

    public int getRep_key() {
        return rep_key;
    }

    public void setRep_key(int rep_key) {
        this.rep_key = rep_key;
    }

    public String getRep_id() {
        return rep_id;
    }

    public void setRep_id(String rep_id) {
        this.rep_id = rep_id;
    }

    public String getRep_apepat() {
        return rep_apepat;
    }

    public void setRep_apepat(String rep_apepat) {
        this.rep_apepat = rep_apepat;
    }

    public String getRep_apemat() {
        return rep_apemat;
    }

    public void setRep_apemat(String rep_apemat) {
        this.rep_apemat = rep_apemat;
    }

    public String getRep_nom() {
        return rep_nom;
    }

    public void setRep_nom(String rep_nom) {
        this.rep_nom = rep_nom;
    }

    public String getRep_dni() {
        return rep_dni;
    }

    public void setRep_dni(String rep_dni) {
        this.rep_dni = rep_dni;
    }

    public Date getRep_fecnac() {
        return rep_fecnac;
    }

    public void setRep_fecnac(Date rep_fecnac) {
        this.rep_fecnac = rep_fecnac;
    }

    public int getRep_telef() {
        return rep_telef;
    }

    public void setRep_telef(int rep_telef) {
        this.rep_telef = rep_telef;
    }

    public String getRep_direcc() {
        return rep_direcc;
    }

    public void setRep_direcc(String rep_direcc) {
        this.rep_direcc = rep_direcc;
    }

    public int getRep_est() {
        return rep_est;
    }

    public void setRep_est(int rep_est) {
        this.rep_est = rep_est;
    }

    public String getRep_usuadd() {
        return rep_usuadd;
    }

    public void setRep_usuadd(String rep_usuadd) {
        this.rep_usuadd = rep_usuadd;
    }

    public Date getRep_fecadd() {
        return rep_fecadd;
    }

    public void setRep_fecadd(Date rep_fecadd) {
        this.rep_fecadd = rep_fecadd;
    }

    public String getRep_usumod() {
        return rep_usumod;
    }

    public void setRep_usumod(String rep_usumod) {
        this.rep_usumod = rep_usumod;
    }

    public Date getRep_fecmod() {
        return rep_fecmod;
    }

    public void setRep_fecmod(Date rep_fecmod) {
        this.rep_fecmod = rep_fecmod;
    }

    public String getS_nomcompleto() {
        return s_nomcompleto;
    }

    public void setS_nomcompleto(String s_nomcompleto) {
        this.s_nomcompleto = s_nomcompleto;
    }

    public boolean isValor() {
        valor = rep_est == 1 ? true : false;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

}
