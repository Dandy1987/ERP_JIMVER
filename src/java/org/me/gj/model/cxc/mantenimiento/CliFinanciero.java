package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class CliFinanciero {

    private long clifin_id;
    private String cli_id;
    private long cli_key;
    private int suc_id;
    private int emp_id;
    private double clifin_limcred;
    private int clifin_limdoc;
    private int clifin_est;
    private String clifin_usuadd;
    private Date clifin_fecadd;
    private String clifin_usumod;
    private Date clifin_fecmod;
    private int clifin_categ;
    private String ind_accion = "Q";
    private boolean valor;
    private String clifin_categ_des;
    private double clifin_saldo;
    private int clifin_docemi;
    private String cat;

    public CliFinanciero(int emp_id, int suc_id, String cli_id, long cli_key, long clifin_id, String clifin_usumod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clifin_id = clifin_id;
        this.clifin_usumod = clifin_usumod;
    }

    public CliFinanciero(String cli_id, long cli_key, int suc_id, int emp_id, double clifin_limcred, int clifin_limdoc, int clifin_est,String clifin_usuadd, String clifin_usumod, int clifin_categ) {
        //this.clifin_id = clifin_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.clifin_limcred = clifin_limcred;
        this.clifin_limdoc = clifin_limdoc;
        this.clifin_est = clifin_est;
        this.clifin_usuadd = clifin_usuadd;
        this.clifin_usumod = clifin_usumod;
        this.clifin_categ = clifin_categ;
    }

    public CliFinanciero() {
    }

    public long getClifin_id() {
        return clifin_id;
    }

    public void setClifin_id(long clifin_id) {
        this.clifin_id = clifin_id;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public long getCli_key() {
        return cli_key;
    }

    public void setCli_key(long cli_key) {
        this.cli_key = cli_key;
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

    public double getClifin_limcred() {
        return clifin_limcred;
    }

    public void setClifin_limcred(double clifin_limcred) {
        this.clifin_limcred = clifin_limcred;
    }

    public int getClifin_limdoc() {
        return clifin_limdoc;
    }

    public void setClifin_limdoc(int clifin_limdoc) {
        this.clifin_limdoc = clifin_limdoc;
    }

    public int getClifin_est() {
        return clifin_est;
    }

    public void setClifin_est(int clifin_est) {
        this.clifin_est = clifin_est;
    }

    public String getClifin_usuadd() {
        return clifin_usuadd;
    }

    public void setClifin_usuadd(String clifin_usuadd) {
        this.clifin_usuadd = clifin_usuadd;
    }

    public Date getClifin_fecadd() {
        return clifin_fecadd;
    }

    public void setClifin_fecadd(Date clifin_fecadd) {
        this.clifin_fecadd = clifin_fecadd;
    }

    public String getClifin_usumod() {
        return clifin_usumod;
    }

    public void setClifin_usumod(String clifin_usumod) {
        this.clifin_usumod = clifin_usumod;
    }

    public Date getClifin_fecmod() {
        return clifin_fecmod;
    }

    public void setClifin_fecmod(Date clifin_fecmod) {
        this.clifin_fecmod = clifin_fecmod;
    }

    public int getClifin_categ() {
        return clifin_categ;
    }

    public void setClifin_categ(int clifin_categ) {
        this.clifin_categ = clifin_categ;
    }

    public String getCat() {
      //cat=String.valueOf(clifin_categ);
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public boolean isValor() {
        valor = clifin_est == 1;
        return valor;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getClifin_categ_des() {
        return clifin_categ_des;
    }

    public void setClifin_categ_des(String clifin_categ_des) {
        this.clifin_categ_des = clifin_categ_des;
    }

    public double getClifin_saldo() {
        return clifin_saldo;
    }

    public void setClifin_saldo(double clifin_saldo) {
        this.clifin_saldo = clifin_saldo;
    }

    public int getClifin_docemi() {
        return clifin_docemi;
    }

    public void setClifin_docemi(int clifin_docemi) {
        this.clifin_docemi = clifin_docemi;
    }
    
}
