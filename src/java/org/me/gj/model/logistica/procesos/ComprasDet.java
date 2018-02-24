package org.me.gj.model.logistica.procesos;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class ComprasDet implements Serializable {

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    //DecimalFormat df5 = new DecimalFormat("###,##0.00000", dfs);

    private long tc_key;
    private int emp_id;
    private int suc_id;
    private long tcd_item;
    private int tcd_est;
    private String tcd_codori;
    private long pro_key;
    private String pro_id;
    private double tcd_cantped;
    private double tcd_cantfac;
    private double tcd_cantrec;
    private double tcd_dscxart;
    private double tcd_vdscxart;
    private String tcd_svdscxart;
    private double tcd_precioped;
    private String tcd_sprecioped;
    private double tcd_preciofac;
    private String tcd_spreciofac;
    private double tcd_vventaped;
    private String tcd_svventaped;
    private double tcd_vventafac;
    private String tcd_svventafac;
    private double tcd_igvped;
    private String tcd_sigvped;
    private double tcd_igvfac;
    private String tcd_sigvfac;
    private double tcd_prevenped;
    private String tcd_sprevenped;
    private double tcd_prevenfac;
    private String tcd_sprevenfac;
    private double tcd_impnet;
    private String tcd_simpnet;
    private double tcd_pvtatoc;
    private String tcd_simpnetoc;
    private double tcd_mdscgral;
    private String tcd_smdscgral;
    private double tcd_imptot;
    private String tcd_simptot;
    private String tcd_usuadd;
    private Date tcd_fecadd;
    private String tcd_usumod;
    private Date tcd_fecmod;
    private String pro_desdes;
    private String pro_des;
    private String ind_accion = "Q";

    public ComprasDet() {
    }

    public ComprasDet(long tc_key, int emp_id, int suc_id, long tcd_item, String tcd_usumod) {
        this.tc_key = tc_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tcd_item = tcd_item;
        this.tcd_usumod = tcd_usumod;
    }

    public ComprasDet(long tc_key, String pro_des, String pro_desdes, int emp_id, int suc_id, String tcd_codori, String pro_id, double tcd_precioped, double tcd_preciofac,
            double tcd_cantped, double tcd_cantfac, double tcd_cantrec,
            double tcd_dscxart, double tcd_vdscxart,
            double tcd_vventaped, double tcd_vventafac, double tcd_igvped, double tcd_igvfac, double tcd_prevenped, double tcd_prevenfac, String tcd_usuadd, String tcd_usumod) {
        this.tc_key = tc_key;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tcd_codori = tcd_codori;
        this.pro_id = pro_id;
        this.tcd_precioped = tcd_precioped;
        this.tcd_preciofac = tcd_preciofac;
        this.tcd_cantped = tcd_cantped;
        this.tcd_cantfac = tcd_cantfac;
        this.tcd_cantrec = tcd_cantrec;
        this.tcd_dscxart = tcd_dscxart;
        this.tcd_vdscxart = tcd_vdscxart;
        this.tcd_vventaped = tcd_vventaped;
        this.tcd_vventafac = tcd_vventafac;
        this.tcd_igvped = tcd_igvped;
        this.tcd_igvfac = tcd_igvfac;
        this.tcd_prevenped = tcd_prevenped;
        this.tcd_prevenfac = tcd_prevenfac;
        this.tcd_usuadd = tcd_usuadd;
        this.tcd_usumod = tcd_usumod;
    }

    public long getTc_key() {
        return tc_key;
    }

    public void setTc_key(long tc_key) {
        this.tc_key = tc_key;
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

    public long getTcd_item() {
        return tcd_item;
    }

    public void setTcd_item(long tcd_item) {
        this.tcd_item = tcd_item;
    }

    public int getTcd_est() {
        return tcd_est;
    }

    public void setTcd_est(int tcd_est) {
        this.tcd_est = tcd_est;
    }

    public String getTcd_codori() {
        return tcd_codori;
    }

    public void setTcd_codori(String tcd_codori) {
        this.tcd_codori = tcd_codori;
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

    public double getTcd_cantped() {
        return tcd_cantped;
    }

    public void setTcd_cantped(double tcd_cantped) {
        this.tcd_cantped = tcd_cantped;
    }

    public double getTcd_cantrec() {
        return tcd_cantrec;
    }

    public void setTcd_cantrec(double tcd_cantrec) {
        this.tcd_cantrec = tcd_cantrec;
    }

    public double getTcd_cantfac() {
        return tcd_cantfac;
    }

    public void setTcd_cantfac(double tcd_cantfac) {
        this.tcd_cantfac = tcd_cantfac;
    }

    public double getTcd_impnet() {
        return tcd_impnet;
    }

    public void setTcd_impnet(double tcd_impnet) {
        this.tcd_impnet = tcd_impnet;
    }

    public double getTcd_impnetoc() {
        return tcd_pvtatoc;
    }

    public void setTcd_impnetoc(double tcd_impnetoc) {
        this.tcd_pvtatoc = tcd_impnetoc;
    }

    public String getTcd_simpnet() {
        tcd_simpnet = df2.format(tcd_impnet);
        return tcd_simpnet;
    }

    public String getTcd_simpnetoc() {
        tcd_simpnetoc = df2.format(tcd_pvtatoc);
        return tcd_simpnetoc;
    }

    public double getTcd_mdscgral() {
        return tcd_mdscgral;
    }

    public String getTcd_smdscgral() {
        tcd_smdscgral = df2.format(tcd_mdscgral);
        return tcd_smdscgral;
    }

    public void setTcd_mdscgral(double tcd_mdscgral) {
        this.tcd_mdscgral = tcd_mdscgral;
    }

    public double getTcd_dscxart() {
        return tcd_dscxart;
    }

    public void setTcd_dscxart(double tcd_dscxart) {
        this.tcd_dscxart = tcd_dscxart;
    }

    public double getTcd_vdscxart() {
        return tcd_vdscxart;
    }

    public void setTcd_vdscxart(double tcd_vdscxart) {
        this.tcd_vdscxart = tcd_vdscxart;
    }

    public String getTcd_svdscxart() {
        tcd_svdscxart = df2.format(tcd_vdscxart);
        return tcd_svdscxart;
    }

    public String getTcd_simptot() {
        tcd_simptot = df2.format(tcd_imptot);
        return tcd_simptot;
    }

    public double getTcd_imptot() {
        return tcd_imptot;
    }

    public void setTcd_imptot(double tcd_imptot) {
        this.tcd_imptot = tcd_imptot;
    }

    public String getTcd_usuadd() {
        return tcd_usuadd;
    }

    public void setTcd_usuadd(String tcd_usuadd) {
        this.tcd_usuadd = tcd_usuadd;
    }

    public Date getTcd_fecadd() {
        return tcd_fecadd;
    }

    public void setTcd_fecadd(Date tcd_fecadd) {
        this.tcd_fecadd = tcd_fecadd;
    }

    public String getTcd_usumod() {
        return tcd_usumod;
    }

    public void setTcd_usumod(String tcd_usumod) {
        this.tcd_usumod = tcd_usumod;
    }

    public Date getTcd_fecmod() {
        return tcd_fecmod;
    }

    public void setTcd_fecmod(Date tcd_fecmod) {
        this.tcd_fecmod = tcd_fecmod;
    }

    public String getPro_desdes() {
        return pro_desdes;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public double getTcd_vventaped() {
        return tcd_vventaped;
    }

    public void setTcd_vventaped(double tcd_vventa) {
        this.tcd_vventaped = tcd_vventa;
    }

    public String getTcd_svventaped() {
        tcd_svventaped = df2.format(tcd_vventaped);
        return tcd_svventaped;
    }

    public double getTcd_igvped() {
        return tcd_igvped;
    }

    public void setTcd_igvped(double tcd_igv) {
        this.tcd_igvped = tcd_igv;
    }

    public String getTcd_sigvped() {
        tcd_sigvped = df2.format(tcd_igvped);
        return tcd_sigvped;
    }

    public double getTcd_prevenfac() {
        return tcd_prevenfac;
    }

    public void setTcd_prevenfac(double tcd_prevenfac) {
        this.tcd_prevenfac = tcd_prevenfac;
    }

    public String getTcd_sprevenfac() {
        tcd_sprevenfac = df2.format(tcd_prevenfac);
        return tcd_sprevenfac;
    }

    public void setTcd_sprevenfac(String tcd_sprevenfac) {
        this.tcd_sprevenfac = tcd_sprevenfac;
    }

    public double getTcd_vventafac() {
        return tcd_vventafac;
    }

    public void setTcd_vventafac(double tcd_vventafac) {
        this.tcd_vventafac = tcd_vventafac;
    }

    public String getTcd_svventafac() {
        tcd_svventafac = df2.format(tcd_vventafac);
        return tcd_svventafac;
    }

    public void setTcd_svventafac(String tcd_svventafac) {
        this.tcd_svventafac = tcd_svventafac;
    }

    public double getTcd_igvfac() {
        return tcd_igvfac;
    }

    public void setTcd_igvfac(double tcd_igvfac) {
        this.tcd_igvfac = tcd_igvfac;
    }

    public String getTcd_sigvfac() {
        tcd_sigvfac = df2.format(tcd_igvfac);
        return tcd_sigvfac;
    }

    public void setTcd_sigvfac(String tcd_sigvfac) {
        this.tcd_sigvfac = tcd_sigvfac;
    }

    public double getTcd_precioped() {
        return tcd_precioped;
    }

    public void setTcd_precioped(double tcd_precioped) {
        this.tcd_precioped = tcd_precioped;
    }

    public String getTcd_sprecioped() {
        tcd_sprecioped = df2.format(tcd_precioped);
        return tcd_sprecioped;
    }

    public void setTcd_sprecioped(String tcd_sprecioped) {
        this.tcd_sprecioped = tcd_sprecioped;
    }

    public double getTcd_preciofac() {
        return tcd_preciofac;
    }

    public void setTcd_preciofac(double tcd_preciofac) {
        this.tcd_preciofac = tcd_preciofac;
    }

    public String getTcd_spreciofac() {
        tcd_spreciofac = df2.format(tcd_preciofac);
        return tcd_spreciofac;
    }

    public void setTcd_spreciofac(String tcd_spreciofac) {
        this.tcd_spreciofac = tcd_spreciofac;
    }

    public double getTcd_prevenped() {
        return tcd_prevenped;
    }

    public void setTcd_prevenped(double tcd_prevenped) {
        this.tcd_prevenped = tcd_prevenped;
    }

    public String getTcd_sprevenped() {
        tcd_sprevenped = df2.format(tcd_prevenped);
        return tcd_sprevenped;
    }

    public void setTcd_sprevenped(String tcd_sprevenped) {
        this.tcd_sprevenped = tcd_sprevenped;
    }

}
