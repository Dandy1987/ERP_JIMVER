package org.me.gj.model.seguridad.mantenimiento;

import java.util.Date;

public class Numeracion {

    private int emp_id;
    private int suc_id;
    private int num_id;
    private String num_tip;
    private int num_est;
    private String num_des;
    private int num_cor;
    private String num_ser;
    private int num_gui;
    private String num_ven;
    private String num_cos;
    private int num_fac;
    private int num_almori;
    private int num_almdes;
    private int num_clipro;
    private String num_salalm;
    private String num_com;
    private String num_tipdoc;
    private String num_nomrep;
    private String num_usuadd;
    private Date num_fecadd;
    private String num_usumod;
    private Date num_fecmod;
    private boolean valor;
    private String num_guides;
    private String num_cliprodes;
    private String num_almorides;
    private String num_almdesdes;
    private String num_facdes;

    public Numeracion() {
    }

    public Numeracion(String num_ser) {
        this.num_ser = num_ser;
    }

    //Creacion de Numeraciones de Tablas
    public Numeracion(int emp_id, int suc_id, int num_id, String num_tip, int num_est, String num_des, int num_cor, String num_usuadd, String num_usumod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.num_id = num_id;
        this.num_tip = num_tip;
        this.num_est = num_est;
        this.num_des = num_des;
        this.num_cor = num_cor;
        this.num_usuadd = num_usuadd;
        this.num_usumod = num_usumod;
    }

    //Creacion de Numeracion de Series
    public Numeracion(int emp_id, int suc_id, int num_id, String num_tip, int num_est, String num_des, int num_cor, String num_ser, int num_gui, String num_ven, String num_cos, int num_fac, int num_almori, int num_almdes, int num_clipro, String num_salalm, String num_com, String num_nomrep, String num_usuadd, String num_usumod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.num_id = num_id;
        this.num_tip = num_tip;
        this.num_est = num_est;
        this.num_des = num_des;
        this.num_cor = num_cor;
        this.num_ser = num_ser;
        this.num_gui = num_gui;
        this.num_ven = num_ven;
        this.num_cos = num_cos;
        this.num_fac = num_fac;
        this.num_almori = num_almori;
        this.num_almdes = num_almdes;
        this.num_clipro = num_clipro;
        this.num_salalm = num_salalm;
        this.num_com = num_com;
        this.num_nomrep = num_nomrep;
        this.num_usuadd = num_usuadd;
        this.num_usumod = num_usumod;
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

    public int getNum_id() {
        return num_id;
    }

    public void setNum_id(int num_id) {
        this.num_id = num_id;
    }

    public String getNum_tip() {
        return num_tip;
    }

    public void setNum_tip(String num_tip) {
        this.num_tip = num_tip;
    }

    public int getNum_est() {
        return num_est;
    }

    public void setNum_est(int num_est) {
        this.num_est = num_est;
    }

    public String getNum_des() {
        return num_des;
    }

    public void setNum_des(String num_des) {
        this.num_des = num_des;
    }

    public int getNum_cor() {
        return num_cor;
    }

    public void setNum_cor(int num_cor) {
        this.num_cor = num_cor;
    }

    public String getNum_ser() {
        return num_ser;
    }

    public void setNum_ser(String num_ser) {
        this.num_ser = num_ser;
    }

    public int getNum_gui() {
        return num_gui;
    }

    public void setNum_gui(int num_gui) {
        this.num_gui = num_gui;
    }

    public String getNum_ven() {
        return num_ven;
    }

    public void setNum_ven(String num_ven) {
        this.num_ven = num_ven;
    }

    public String getNum_cos() {
        return num_cos;
    }

    public void setNum_cos(String num_cos) {
        this.num_cos = num_cos;
    }

    public int getNum_fac() {
        return num_fac;
    }

    public void setNum_fac(int num_fac) {
        this.num_fac = num_fac;
    }

    public int getNum_almori() {
        return num_almori;
    }

    public void setNum_almori(int num_almori) {
        this.num_almori = num_almori;
    }

    public int getNum_almdes() {
        return num_almdes;
    }

    public void setNum_almdes(int num_almdes) {
        this.num_almdes = num_almdes;
    }

    public int getNum_clipro() {
        return num_clipro;
    }

    public void setNum_clipro(int num_clipro) {
        this.num_clipro = num_clipro;
    }

    public String getNum_salalm() {
        return num_salalm;
    }

    public void setNum_salalm(String num_salalm) {
        this.num_salalm = num_salalm;
    }

    public String getNum_com() {
        return num_com;
    }

    public void setNum_com(String num_com) {
        this.num_com = num_com;
    }

    public String getNum_tipdoc() {
        return num_tipdoc;
    }

    public void setNum_tipdoc(String num_tipdoc) {
        this.num_tipdoc = num_tipdoc;
    }

    public String getNum_nomrep() {
        return num_nomrep;
    }

    public void setNum_nomrep(String num_nomrep) {
        this.num_nomrep = num_nomrep;
    }

    public String getNum_usuadd() {
        return num_usuadd;
    }

    public void setNum_usuadd(String num_usuadd) {
        this.num_usuadd = num_usuadd;
    }

    public Date getNum_fecadd() {
        return num_fecadd;
    }

    public void setNum_fecadd(Date num_fecadd) {
        this.num_fecadd = num_fecadd;
    }

    public String getNum_usumod() {
        return num_usumod;
    }

    public void setNum_usumod(String num_usumod) {
        this.num_usumod = num_usumod;
    }

    public Date getNum_fecmod() {
        return num_fecmod;
    }

    public void setNum_fecmod(Date num_fecmod) {
        this.num_fecmod = num_fecmod;
    }

    public boolean isValor() {
        valor = num_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getNum_guides() {
        return num_guides;
    }

    public void setNum_guides(String num_guides) {
        this.num_guides = num_guides;
    }

    public String getNum_cliprodes() {
        return num_cliprodes;
    }

    public void setNum_cliprodes(String num_cliprodes) {
        this.num_cliprodes = num_cliprodes;
    }

    public String getNum_almorides() {
        return num_almorides;
    }

    public void setNum_almorides(String num_almorides) {
        this.num_almorides = num_almorides;
    }

    public String getNum_almdesdes() {
        return num_almdesdes;
    }

    public void setNum_almdesdes(String num_almdesdes) {
        this.num_almdesdes = num_almdesdes;
    }

    public String getNum_facdes() {
        return num_facdes;
    }

    public void setNum_facdes(String num_facdes) {
        this.num_facdes = num_facdes;
    }

}
