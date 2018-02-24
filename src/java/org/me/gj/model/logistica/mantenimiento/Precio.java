package org.me.gj.model.logistica.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class Precio {

    private long pre_key;
    private int emp_id;
    private int suc_id;
    private long pro_key;
    private String pro_id;
    private int lp_key;
    private int lp_tipo;
    private double pre_valvent;
    private double pre_igv;
    private double pre_venta;
    private int pre_est;
    private String pre_nomrep;
    private int pre_ord;
    private String pre_usuadd;
    private Date pre_fecadd;
    private String pre_usumod;
    private Date pre_fecmod;
    //Variables Auxiliares
    private boolean valor;
    private String pro_des;
    private String pro_desdes;
    private String pro_condimp;
    private double imp_valor;
    private double pro_peso;
    private long prov_key;
    private String prov_id;
    private String prov_razsoc;
    private String lp_id;
    private String lp_des;
    
    //valores con formato cadena
    private String spre_valvent;
    private String spre_igv;
    private String spre_venta;
    
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);

    public Precio() {
    }

    public Precio(long pre_key, int emp_id, int suc_id, String pro_id, int lp_key, int lp_tipo, double pre_valvent, double pre_igv, double pre_venta, int pre_est, String pre_nomrep, int pre_ord, String pre_usuadd, String pre_usumod) {
        this.pre_key = pre_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.pro_id = pro_id;
        this.lp_key = lp_key;
        this.lp_tipo = lp_tipo;
        this.pre_valvent = pre_valvent;
        this.pre_igv = pre_igv;
        this.pre_venta = pre_venta;
        this.pre_est = pre_est;
        this.pre_nomrep = pre_nomrep;
        this.pre_ord = pre_ord;
        this.pre_usuadd = pre_usuadd;
        this.pre_usumod = pre_usumod;
    }

    public long getPre_key() {
        return pre_key;
    }

    public void setPre_key(long pre_key) {
        this.pre_key = pre_key;
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

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public int getLp_key() {
        return lp_key;
    }

    public void setLp_key(int lp_key) {
        this.lp_key = lp_key;
    }

    public int getLp_tipo() {
        return lp_tipo;
    }

    public void setLp_tipo(int lp_tipo) {
        this.lp_tipo = lp_tipo;
    }

    public double getPre_valvent() {
        return pre_valvent;
    }

    public void setPre_valvent(double pre_valvent) {
        this.pre_valvent = pre_valvent;
    }

    public double getPre_igv() {
        return pre_igv;
    }

    public void setPre_igv(double pre_igv) {
        this.pre_igv = pre_igv;
    }

    public double getPre_venta() {
        return pre_venta;
    }

    public void setPre_venta(double pre_venta) {
        this.pre_venta = pre_venta;
    }

    public int getPre_est() {
        return pre_est;
    }

    public void setPre_est(int pre_est) {
        this.pre_est = pre_est;
    }

    public String getPre_nomrep() {
        return pre_nomrep;
    }

    public void setPre_nomrep(String pre_nomrep) {
        this.pre_nomrep = pre_nomrep;
    }

    public int getPre_ord() {
        return pre_ord;
    }

    public void setPre_ord(int pre_ord) {
        this.pre_ord = pre_ord;
    }

    public String getPre_usuadd() {
        return pre_usuadd;
    }

    public void setPre_usuadd(String pre_usuadd) {
        this.pre_usuadd = pre_usuadd;
    }

    public Date getPre_fecadd() {
        return pre_fecadd;
    }

    public void setPre_fecadd(Date pre_fecadd) {
        this.pre_fecadd = pre_fecadd;
    }

    public String getPre_usumod() {
        return pre_usumod;
    }

    public void setPre_usumod(String pre_usumod) {
        this.pre_usumod = pre_usumod;
    }

    public Date getPre_fecmod() {
        return pre_fecmod;
    }

    public void setPre_fecmod(Date pre_fecmod) {
        this.pre_fecmod = pre_fecmod;
    }

    public boolean isValor() {
        valor = pre_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public String getLp_id() {
        return lp_id;
    }

    public void setLp_id(String lp_id) {
        this.lp_id = lp_id;
    }

    public String getLp_des() {
        return lp_des;
    }

    public void setLp_des(String lp_des) {
        this.lp_des = lp_des;
    }

    public String getPro_desdes() {
        return pro_desdes;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public String getPro_condimp() {
        return pro_condimp;
    }

    public void setPro_condimp(String pro_condimp) {
        this.pro_condimp = pro_condimp;
    }

    public double getImp_valor() {
        return imp_valor;
    }

    public void setImp_valor(double imp_valor) {
        this.imp_valor = imp_valor;
    }

    public double getPro_peso() {
        return pro_peso;
    }

    public void setPro_peso(double pro_peso) {
        this.pro_peso = pro_peso;
    }

    public String getSpre_valvent() {
        spre_valvent = df2.format(pre_valvent);
        return spre_valvent;
    }

    public void setSpre_valvent(String spre_valvent) {
        this.spre_valvent = spre_valvent;
    }

    public String getSpre_igv() {
        spre_igv = df2.format(pre_igv);
        return spre_igv;
    }

    public void setSpre_igv(String spre_igv) {
        this.spre_igv = spre_igv;
    }

    public String getSpre_venta() {
        spre_venta = df2.format(pre_venta);
        return spre_venta;
    }

    public void setSpre_venta(String spre_venta) {
        this.spre_venta = spre_venta;
    }
    
    
}
