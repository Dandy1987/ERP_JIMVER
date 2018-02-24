package org.me.gj.model.logistica.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class Almacenes {

//    private int alm_key;
    private String alm_key;
    private String alm_id;
    private String alm_des;
    private int alm_est;
    private String alm_direcc;
    private double alm_alt;
    private double alm_anc;
    private double alm_lar;
    private String alm_nomrep;
    private int alm_ord;
    private String alm_usuadd;
    private Date alm_fecadd;
    private String alm_usumod;
    private Date alm_fecmod;
    private int suc_id;
    private int emp_id;
    private int alm_tip;
    private int alm_vta;
    private int alm_com;
    private int alm_def;
    private boolean valor;
    //varibles auxiliares
    private String alm_tipdes;
    private boolean valSelec;
    
    private String salm_alt;
    private String salm_anc;
    private String salm_lar;
    
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);

    public Almacenes() {
    }

    public Almacenes(String alm_key, String alm_des, int alm_est, String alm_direcc, double alm_alt, double alm_anc, double alm_lar, 
            /*String alm_nomrep,*/int alm_ord, String alm_usuadd, String alm_usumod, int suc_id, int emp_id, int alm_tip, int alm_vta, 
            int alm_com, int alm_def) {
        this.alm_key = alm_key;
        this.alm_des = alm_des;
        this.alm_est = alm_est;
        this.alm_direcc = alm_direcc;
        this.alm_alt = alm_alt;
        this.alm_anc = alm_anc;
        this.alm_lar = alm_lar;
        //this.alm_nomrep = alm_nomrep;
        this.alm_ord = alm_ord;
        this.alm_usuadd = alm_usuadd;
        this.alm_usumod = alm_usumod;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.alm_tip = alm_tip;
        this.alm_vta = alm_vta;
        this.alm_com = alm_com;
        this.alm_def = alm_def;
    }

    public String getAlm_key() {
        return alm_key;
    }

    public void setAlm_key(String alm_key) {
        this.alm_key = alm_key;
    }

    public String getAlm_id() {
        return alm_id;
    }

    public void setAlm_id(String alm_id) {
        this.alm_id = alm_id;
    }

    public String getAlm_des() {
        return alm_des;
    }

    public void setAlm_des(String alm_des) {
        this.alm_des = alm_des;
    }

    public int getAlm_est() {
        return alm_est;
    }

    public void setAlm_est(int alm_est) {
        this.alm_est = alm_est;
    }

    public String getAlm_direcc() {
        return alm_direcc;
    }

    public void setAlm_direcc(String alm_direcc) {
        this.alm_direcc = alm_direcc;
    }

    public double getAlm_alt() {
        return alm_alt;
    }

    public void setAlm_alt(double alm_alt) {
        this.alm_alt = alm_alt;
    }

    public double getAlm_anc() {
        return alm_anc;
    }

    public void setAlm_anc(double alm_anc) {
        this.alm_anc = alm_anc;
    }

    public double getAlm_lar() {
        return alm_lar;
    }

    public void setAlm_lar(double alm_lar) {
        this.alm_lar = alm_lar;
    }

    public String getAlm_nomrep() {
        return alm_nomrep;
    }

    public void setAlm_nomrep(String alm_nomrep) {
        this.alm_nomrep = alm_nomrep;
    }

    public int getAlm_ord() {
        return alm_ord;
    }

    public void setAlm_ord(int alm_ord) {
        this.alm_ord = alm_ord;
    }

    public String getAlm_usuadd() {
        return alm_usuadd;
    }

    public void setAlm_usuadd(String alm_usuadd) {
        this.alm_usuadd = alm_usuadd;
    }

    public Date getAlm_fecadd() {
        return alm_fecadd;
    }

    public void setAlm_fecadd(Date alm_fecadd) {
        this.alm_fecadd = alm_fecadd;
    }

    public String getAlm_usumod() {
        return alm_usumod;
    }

    public void setAlm_usumod(String alm_usumod) {
        this.alm_usumod = alm_usumod;
    }

    public Date getAlm_fecmod() {
        return alm_fecmod;
    }

    public void setAlm_fecmod(Date alm_fecmod) {
        this.alm_fecmod = alm_fecmod;
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

    public int getAlm_tip() {
        return alm_tip;
    }

    public void setAlm_tip(int alm_tip) {
        this.alm_tip = alm_tip;
    }

    public int getAlm_vta() {
        return alm_vta;
    }

    public void setAlm_vta(int alm_vta) {
        this.alm_vta = alm_vta;
    }

    public int getAlm_def() {
        return alm_def;
    }

    public void setAlm_def(int alm_def) {
        this.alm_def = alm_def;
    }

    public String getAlm_tipdes() {
        return alm_tipdes;
    }

    public void setAlm_tipdes(String alm_tipdes) {
        this.alm_tipdes = alm_tipdes;
    }

    public boolean isValor() {
        /*if (alm_est == 1) {
            valor = true;
        } else {
            valor = false;
        }*/
        valor = (alm_est == 1);
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getAlm_com() {
        return alm_com;
    }

    public void setAlm_com(int alm_com) {
        this.alm_com = alm_com;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getSalm_alt() {
        salm_alt = df2.format(alm_alt);
        return salm_alt;
    }

    public void setSalm_alt(String salm_alt) {
        this.salm_alt = salm_alt;
    }

    public String getSalm_anc() {
        salm_anc = df2.format(alm_anc);
        return salm_anc;
    }

    public void setSalm_anc(String salm_anc) {
        this.salm_anc = salm_anc;
    }

    public String getSalm_lar() {
        salm_lar = df2.format(alm_lar);
        return salm_lar;
    }

    public void setSalm_lar(String salm_lar) {
        this.salm_lar = salm_lar;
    }

}
