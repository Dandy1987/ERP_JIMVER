package org.me.gj.model.logistica.informes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class StockTmp {

    private int emp_id;
    private int suc_id;
    private String prov_id;
    private String prov_razsoc;
    private String lin_id;
    private String lin_des;
    private String pro_id;
    private String pro_des;
    private int pro_presminven;
    private String pro_clas;
    private String ubic_id;
    private long stock_ent;
    private long stock_fra;
    private long stock_min;
    private long stock_max;
    private long stock_dias;
    private long stock_mes;
    private double costo;
    private double costotot;
    private double ventas;
    private double ventasprom;
    private double ventashist;
    private int pendiente;
    private String desplazamiento;
    private String tmp_usuadd;
    private Date tmp_fecadd;
    private String tmp_usumod;
    private Date tmp_fecmod;
    private String sstock_ent;
    private String sstock_fra;
    private String sstock_min;
    private String sstock_max;
    private String sstock_dias;
    private String sstock_mes;
    private String scosto;
    private String scostotot;
    private String sventas;
    private String sventasprom;
    private String sventashist;
    private String spendiente;

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    DecimalFormat df = new DecimalFormat("###,###", dfs);

    public StockTmp() {
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

    public String getLin_id() {
        return lin_id;
    }

    public void setLin_id(String lin_id) {
        this.lin_id = lin_id;
    }

    public String getLin_des() {
        return lin_des;
    }

    public void setLin_des(String lin_des) {
        this.lin_des = lin_des;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public int getPro_presminven() {
        return pro_presminven;
    }

    public void setPro_presminven(int pro_presminven) {
        this.pro_presminven = pro_presminven;
    }

    public String getPro_clas() {
        return pro_clas;
    }

    public void setPro_clas(String pro_clas) {
        this.pro_clas = pro_clas;
    }

    public String getUbic_id() {
        return ubic_id;
    }

    public void setUbic_id(String ubic_id) {
        this.ubic_id = ubic_id;
    }

    public long getStock_ent() {
        return stock_ent;
    }

    public void setStock_ent(long stock_ent) {
        this.stock_ent = stock_ent;
    }

    public long getStock_fra() {
        return stock_fra;
    }

    public void setStock_fra(long stock_fra) {
        this.stock_fra = stock_fra;
    }

    public long getStock_min() {
        return stock_min;
    }

    public void setStock_min(long stock_min) {
        this.stock_min = stock_min;
    }

    public long getStock_max() {
        return stock_max;
    }

    public void setStock_max(long stock_max) {
        this.stock_max = stock_max;
    }

    public long getStock_dias() {
        return stock_dias;
    }

    public void setStock_dias(long stock_dias) {
        this.stock_dias = stock_dias;
    }

    public long getStock_mes() {
        return stock_mes;
    }

    public void setStock_mes(long stock_mes) {
        this.stock_mes = stock_mes;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getCostotot() {
        return costotot;
    }

    public void setCostotot(double costotot) {
        this.costotot = costotot;
    }

    public double getVentas() {
        return ventas;
    }

    public void setVentas(double ventas) {
        this.ventas = ventas;
    }

    public double getVentasprom() {
        return ventasprom;
    }

    public void setVentasprom(double ventasprom) {
        this.ventasprom = ventasprom;
    }

    public double getVentashist() {
        return ventashist;
    }

    public void setVentashist(double ventashist) {
        this.ventashist = ventashist;
    }

    public int getPendiente() {
        return pendiente;
    }

    public void setPendiente(int pendiente) {
        this.pendiente = pendiente;
    }

    public String getDesplazamiento() {
        return desplazamiento;
    }

    public void setDesplazamiento(String desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    public String getTmp_usuadd() {
        return tmp_usuadd;
    }

    public void setTmp_usuadd(String tmp_usuadd) {
        this.tmp_usuadd = tmp_usuadd;
    }

    public Date getTmp_fecadd() {
        return tmp_fecadd;
    }

    public void setTmp_fecadd(Date tmp_fecadd) {
        this.tmp_fecadd = tmp_fecadd;
    }

    public String getTmp_usumod() {
        return tmp_usumod;
    }

    public void setTmp_usumod(String tmp_usumod) {
        this.tmp_usumod = tmp_usumod;
    }

    public Date getTmp_fecmod() {
        return tmp_fecmod;
    }

    public void setTmp_fecmod(Date tmp_fecmod) {
        this.tmp_fecmod = tmp_fecmod;
    }

    public String getSstock_ent() {
        sstock_ent = df.format(stock_ent);
        return sstock_ent;
    }

    public String getSstock_fra() {
        sstock_fra = df.format(stock_fra);
        return sstock_fra;
    }

    public String getSstock_min() {
        sstock_min = df.format(stock_min);
        return sstock_min;
    }

    public String getSstock_max() {
        sstock_max = df.format(stock_max);
        return sstock_max;
    }

    public String getSstock_dias() {
        sstock_dias = df.format(stock_dias);
        return sstock_dias;
    }

    public String getSstock_mes() {
        sstock_mes = df.format(stock_mes);
        return sstock_mes;
    }

    public String getScosto() {
        scosto = df2.format(costo);
        return scosto;
    }

    public String getScostotot() {
        scostotot = df2.format(costotot);
        return scostotot;
    }

    public String getSventas() {
        sventas = df2.format(ventas);
        return sventas;
    }

    public String getSventasprom() {
        sventasprom = df2.format(ventasprom);
        return sventasprom;
    }

    public String getSventashist() {
        sventashist = df2.format(ventashist);
        return sventashist;
    }

    public String getSpendiente() {
        spendiente = df2.format(pendiente);
        return spendiente;
    }

}
