package org.me.gj.model.logistica.informes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Costos {

    private int emp_id;
    private int suc_id;
    private String cst_periodo;
    private String alm_id;
    private String prov_id;
    private String prod_id;
    private int cst_estado;
    private Double cst_cstini;
    private Double cst_cstfin;
    private Double cst_inilifo;
    private Double cst_finlifo;
    private Double cst_repo;
    private String cst_usuadd;
    private Date cst_fecadd;
    private String cst_usumod;
    private Date cst_fecmod;

    private String prov_des;
    private String prod_des;
    private String s_cst_cstini;
    private String s_cst_cstfin;
    private String s_cst_inilifo;
    private String s_cst_finlifo;
    private String s_cst_repo;

    private boolean valor;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    DecimalFormat df5 = new DecimalFormat("###,##0.00000", dfs);

    public Costos() {
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

    public String getCst_periodo() {
        return cst_periodo;
    }

    public void setCst_periodo(String cst_periodo) {
        this.cst_periodo = cst_periodo;
    }

    public String getAlm_id() {
        return alm_id;
    }

    public void setAlm_id(String alm_id) {
        this.alm_id = alm_id;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public int getCst_estado() {
        return cst_estado;
    }

    public void setCst_estado(int cst_estado) {
        this.cst_estado = cst_estado;
    }

    public Double getCst_cstini() {
        return cst_cstini;
    }

    public void setCst_cstini(Double cst_cstini) {
        this.cst_cstini = cst_cstini;
    }

    public Double getCst_cstfin() {
        return cst_cstfin;
    }

    public void setCst_cstfin(Double cst_cstfin) {
        this.cst_cstfin = cst_cstfin;
    }

    public Double getCst_inilifo() {
        return cst_inilifo;
    }

    public void setCst_inilifo(Double cst_inilifo) {
        this.cst_inilifo = cst_inilifo;
    }

    public Double getCst_finlifo() {
        return cst_finlifo;
    }

    public void setCst_finlifo(Double cst_finlifo) {
        this.cst_finlifo = cst_finlifo;
    }

    public Double getCst_repo() {
        return cst_repo;
    }

    public void setCst_repo(Double cst_repo) {
        this.cst_repo = cst_repo;
    }

    public String getCst_usuadd() {
        return cst_usuadd;
    }

    public void setCst_usuadd(String cst_usuadd) {
        this.cst_usuadd = cst_usuadd;
    }

    public Date getCst_fecadd() {
        return cst_fecadd;
    }

    public void setCst_fecadd(Date cst_fecadd) {
        this.cst_fecadd = cst_fecadd;
    }

    public String getCst_usumod() {
        return cst_usumod;
    }

    public void setCst_usumod(String cst_usumod) {
        this.cst_usumod = cst_usumod;
    }

    public Date getCst_fecmod() {
        return cst_fecmod;
    }

    public void setCst_fecmod(Date cst_fecmod) {
        this.cst_fecmod = cst_fecmod;
    }

    public String getProv_des() {
        return prov_des;
    }

    public void setProv_des(String prov_des) {
        this.prov_des = prov_des;
    }

    public String getProd_des() {
        return prod_des;
    }

    public void setProd_des(String prod_des) {
        this.prod_des = prod_des;
    }

    public String getS_cst_cstini() {
        s_cst_cstini = df5.format(cst_cstini);
        return s_cst_cstini;
    }

    public void setS_cst_cstini(String s_cst_cstini) {
        this.s_cst_cstini = s_cst_cstini;
    }

    public String getS_cst_cstfin() {
        s_cst_cstfin = df5.format(cst_cstfin);
        return s_cst_cstfin;
    }

    public void setS_cst_fin(String s_cst_cstfin) {
        this.s_cst_cstfin = s_cst_cstfin;
    }

    public String getS_cst_inilifo() {
        s_cst_inilifo = df5.format(cst_inilifo);
        return s_cst_inilifo;
    }

    public void setS_cst_inilifo(String s_cst_inilifo) {
        this.s_cst_inilifo = s_cst_inilifo;
    }

    public String getS_cst_finlifo() {
        s_cst_finlifo = df5.format(cst_finlifo);
        return s_cst_finlifo;
    }

    public void setS_cst_finlifo(String s_cst_finlifo) {
        this.s_cst_finlifo = s_cst_finlifo;
    }

    public String getS_cst_repo() {
        s_cst_repo = df5.format(cst_repo);
        return s_cst_repo;
    }

    public void setS_cst_repo(String s_cst_repo) {
        this.s_cst_repo = s_cst_repo;
    }

    public boolean isValor() {
        if (cst_estado == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
