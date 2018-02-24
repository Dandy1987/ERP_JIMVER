package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class Proveedores {

    private Long prov_key;
    private String prov_id;
    private Long prov_ruc;
    private int con_key;
    private String con_tipo;
    private String prov_razsoc;
    private String prov_siglas;
    private String prov_dir;
    private int prov_tip;
    private Long prov_rucext;
    private String prov_web;
    private String prov_clave;
    private int prov_est;
    private int prov_normal;
    private int prov_reten;
    private int prov_buenc;
    private int prov_percep;
    private int prov_detrac;
    private int prov_rel;
    private String prov_usuadd;
    private Date prov_fecadd;
    private String prov_usumod;
    private Date prov_fecmod;
    private String prov_nomrep;
    private int prov_ord;
    boolean val = false;
    private boolean valSelec;
    private String con_des;

    public Proveedores(Long prov_key, String prov_id, Long prov_ruc, int con_key, String con_tipo, String prov_razsoc, String prov_siglas, String prov_dir, int prov_tip, Long prov_rucext, String prov_web, String prov_clave, int prov_est, int prov_normal, int prov_reten, int prov_buenc, int prov_percep, int prov_detrac, int prov_rel, String prov_usuadd, Date prov_fecadd, String prov_usumod, Date prov_fecmod, String prov_nomrep, int prov_ord) {
        this.prov_key = prov_key;
        this.prov_id = prov_id;
        this.prov_ruc = prov_ruc;
        this.con_key = con_key;
        this.con_tipo = con_tipo;
        this.prov_razsoc = prov_razsoc;
        this.prov_siglas = prov_siglas;
        this.prov_dir = prov_dir;
        this.prov_tip = prov_tip;
        this.prov_rucext = prov_rucext;
        this.prov_web = prov_web;
        this.prov_clave = prov_clave;
        this.prov_est = prov_est;
        this.prov_normal = prov_normal;
        this.prov_reten = prov_reten;
        this.prov_buenc = prov_buenc;
        this.prov_percep = prov_percep;
        this.prov_detrac = prov_detrac;
        this.prov_rel = prov_rel;
        this.prov_usuadd = prov_usuadd;
        this.prov_fecadd = prov_fecadd;
        this.prov_usumod = prov_usumod;
        this.prov_fecmod = prov_fecmod;
        this.prov_nomrep = prov_nomrep;
        this.prov_ord = prov_ord;
    }

    public Proveedores(long prov_ruc, int con_key, String prov_razsoc, String prov_siglas, String prov_dir, int prov_tip, Long prov_rucext, String prov_web, String prov_clave, int prov_normal, int prov_reten, int prov_buenc, int prov_percep, int prov_rel, String prov_usuadd, String prov_nomrep, int prov_ord) {
        this.prov_ruc = prov_ruc;
        this.con_key = con_key;
        this.prov_razsoc = prov_razsoc;
        this.prov_siglas = prov_siglas;
        this.prov_dir = prov_dir;
        this.prov_tip = prov_tip;
        this.prov_rucext = prov_rucext;
        this.prov_web = prov_web;
        this.prov_clave = prov_clave;
        this.prov_normal = prov_normal;
        this.prov_reten = prov_reten;
        this.prov_buenc = prov_buenc;
        this.prov_percep = prov_percep;
        this.prov_rel = prov_rel;
        this.prov_usuadd = prov_usuadd;
        this.prov_nomrep = prov_nomrep;
        this.prov_ord = prov_ord;
    }

    public Proveedores(long prov_key, String prov_id, long prov_ruc, String prov_razsoc, String prov_dir, int prov_est) {
        this.prov_key = prov_key;
        this.prov_id = prov_id;
        this.prov_ruc = prov_ruc;
        this.prov_razsoc = prov_razsoc;
        this.prov_dir = prov_dir;
        this.prov_est = prov_est;
    }

    public Proveedores(long prov_key, int i_con_key, long prov_ruc, String prov_razsoc, String prov_siglas, String prov_dir, int prov_tip, Long prov_rucext, String prov_web, String prov_clave, int prov_est, int prov_normal, int prov_reten, int prov_buenc, int prov_percep, int prov_detrac, int prov_rel, String prov_usuadd, String prov_usumod, String prov_nomrep, int prov_ord) {
        this.prov_key = prov_key;
        this.con_key = i_con_key;
        this.prov_razsoc = prov_razsoc;
        this.prov_ruc = prov_ruc;
        this.prov_siglas = prov_siglas;
        this.prov_dir = prov_dir;
        this.prov_tip = prov_tip;
        this.prov_rucext = prov_rucext;
        this.prov_web = prov_web;
        this.prov_clave = prov_clave;
        this.prov_est = prov_est;
        this.prov_normal = prov_normal;
        this.prov_reten = prov_reten;
        this.prov_buenc = prov_buenc;
        this.prov_percep = prov_percep;
        this.prov_detrac = prov_detrac;
        this.prov_rel = prov_rel;
        this.prov_usuadd = prov_usuadd;
        this.prov_usumod = prov_usumod;
        this.prov_nomrep = prov_nomrep;
        this.prov_ord = prov_ord;
    }

    public Proveedores(Long prov_key, String prov_razsoc) {
        this.prov_key = prov_key;
        this.prov_razsoc = prov_razsoc;
    }

    public Proveedores() {
    }

    public int getCon_key() {
        return con_key;
    }

    public void setCon_key(int con_key) {
        this.con_key = con_key;
    }

    public String getCon_tipo() {
        return con_tipo;
    }

    public void setCon_tipo(String con_tipo) {
        this.con_tipo = con_tipo;
    }

    public int getProv_buenc() {
        return prov_buenc;
    }

    public void setProv_buenc(int prov_buenc) {
        this.prov_buenc = prov_buenc;
    }

    public String getProv_clave() {
        return prov_clave;
    }

    public void setProv_clave(String prov_clave) {
        this.prov_clave = prov_clave;
    }

    public int getProv_detrac() {
        return prov_detrac;
    }

    public void setProv_detrac(int prov_detrac) {
        this.prov_detrac = prov_detrac;
    }

    public String getProv_dir() {
        return prov_dir;
    }

    public void setProv_dir(String prov_dir) {
        this.prov_dir = prov_dir;
    }

    public int getProv_est() {
        return prov_est;
    }

    public void setProv_est(int prov_est) {
        this.prov_est = prov_est;
    }

    public Date getProv_fecadd() {
        return prov_fecadd;
    }

    public void setProv_fecadd(Date prov_fecadd) {
        this.prov_fecadd = prov_fecadd;
    }

    public Date getProv_fecmod() {
        return prov_fecmod;
    }

    public void setProv_fecmod(Date prov_fecmod) {
        this.prov_fecmod = prov_fecmod;
    }

    public Long getProv_key() {
        return prov_key;
    }

    public void setProv_key(Long prov_key) {
        this.prov_key = prov_key;
    }

    public String getProv_nomrep() {
        return prov_nomrep;
    }

    public void setProv_nomrep(String prov_nomrep) {
        this.prov_nomrep = prov_nomrep;
    }

    public int getProv_normal() {
        return prov_normal;
    }

    public void setProv_normal(int prov_normal) {
        this.prov_normal = prov_normal;
    }

    public int getProv_ord() {
        return prov_ord;
    }

    public void setProv_ord(int prov_ord) {
        this.prov_ord = prov_ord;
    }

    public int getProv_percep() {
        return prov_percep;
    }

    public void setProv_percep(int prov_percep) {
        this.prov_percep = prov_percep;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public int getProv_reten() {
        return prov_reten;
    }

    public void setProv_reten(int prov_reten) {
        this.prov_reten = prov_reten;
    }

    public Long getProv_ruc() {
        return prov_ruc;
    }

    public void setProv_ruc(Long prov_ruc) {
        this.prov_ruc = prov_ruc;
    }

    public Long getProv_rucext() {
        return prov_rucext;
    }

    public void setProv_rucext(Long prov_rucext) {
        this.prov_rucext = prov_rucext;
    }

    public String getProv_siglas() {
        return prov_siglas;
    }

    public void setProv_siglas(String prov_siglas) {
        this.prov_siglas = prov_siglas;
    }

    public int getProv_tip() {
        return prov_tip;
    }

    public void setProv_tip(int prov_tip) {
        this.prov_tip = prov_tip;
    }

    public String getProv_usuadd() {
        return prov_usuadd;
    }

    public void setProv_usuadd(String prov_usuadd) {
        this.prov_usuadd = prov_usuadd;
    }

    public String getProv_usumod() {
        return prov_usumod;
    }

    public void setProv_usumod(String prov_usumod) {
        this.prov_usumod = prov_usumod;
    }

    public String getProv_web() {
        return prov_web;
    }

    public void setProv_web(String prov_web) {
        this.prov_web = prov_web;
    }

    public boolean isVal() {
        val = prov_est == 1;
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getCon_des() {
        return con_des;
    }

    public void setCon_des(String con_des) {
        this.con_des = con_des;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public int getProv_rel() {
        return prov_rel;
    }

    public void setProv_rel(int prov_rel) {
        this.prov_rel = prov_rel;
    }
}
