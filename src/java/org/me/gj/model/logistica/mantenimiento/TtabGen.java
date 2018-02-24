package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class TtabGen {

    private int tab_cod;
    private String tab_des;
    private int tab_id;
    private String tab_subdes;
    private String tab_subdir;
    private int tab_tip;
    private String tab_stip;
    private int tab_ord;
    private int tab_est;
    private String tab_nomrep;
    private int tab_guienl;
    private int tab_guiref;
    private String tab_guisun;
    private String tab_guiccea;
    private String tab_guiccee;
    private String tab_guiccei;
    private String tab_guiccet;
    private String tab_guivouenl;
    private String tab_guicceaf;
    private String tab_guiingart;
    private int tab_almdef;
    private int tab_almvta;
    private String val1;
    private int tab_empid;
    private int tab_sucid;
    private String tab_subdes_tipo;
    private double tab_alt;
    private double tab_anc;
    private double tab_lar;
    private String tab_usuadd;
    private Date tab_fecadd;
    private String tab_usumod;
    private Date tab_fecmod;
    private boolean val;
    final int empresa = 1;
    final int sucursal = 1;

    public TtabGen(int tab_cod, String tab_des, int tab_id, String tab_subdes, String tab_subdir, int tab_tip, int tab_ord, int tab_est, String tab_nomrep, int tab_guienl, int tab_guiref, String tab_guisun, String tab_guiccea, String tab_guiccee, String tab_guiccei, String tab_guiccet, String tab_guivouenl, String tab_guicceaf, String tab_guiingart, int tab_almdef, int tab_almvta, String val1, int tab_empid, int tab_sucid) {
        this.tab_cod = tab_cod;
        this.tab_des = tab_des;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_tip = tab_tip;
        this.tab_ord = tab_ord;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_guienl = tab_guienl;
        this.tab_guiref = tab_guiref;
        this.tab_guisun = tab_guisun;
        this.tab_guiccea = tab_guiccea;
        this.tab_guiccee = tab_guiccee;
        this.tab_guiccei = tab_guiccei;
        this.tab_guiccet = tab_guiccet;
        this.tab_guivouenl = tab_guivouenl;
        this.tab_guicceaf = tab_guicceaf;
        this.tab_guiingart = tab_guiingart;
        this.tab_almdef = tab_almdef;
        this.tab_almvta = tab_almvta;
        this.val1 = val1;
        this.tab_empid = tab_empid;
        this.tab_sucid = tab_sucid;
    }

    public TtabGen(int tab_cod, String tab_des, int tab_id, String tab_subdes, String tab_subdir, int tab_tip, int tab_ord, int tab_est, String tab_nomrep, int tab_almdef,
            int tab_almvta, int tab_empid, int tab_sucid, double tab_alt, double tab_anc, double tab_lar) {
        this.tab_cod = tab_cod;
        this.tab_des = tab_des;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_tip = tab_tip;
        this.tab_ord = tab_ord;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_almdef = tab_almdef;
        this.tab_almvta = tab_almvta;
        this.tab_empid = tab_empid;
        this.tab_sucid = tab_sucid;
        this.tab_lar = tab_lar;
        this.tab_alt = tab_alt;
        this.tab_anc = tab_anc;
    }

    //Para Mantenimiento de Marcas
    public TtabGen(int tab_cod, String tab_des, int tab_id, String tab_subdes, int tab_ord, int tab_est, String tab_nomrep, String tab_usuadd, String tab_usumod) {
        this.tab_cod = tab_cod;
        this.tab_des = tab_des;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_ord = tab_ord;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;
    }

    //Para Mantenimiento de SubLineas
    public TtabGen(int tab_cod, String tab_des, int tab_id, String tab_subdes, int tab_tip, int tab_ord, int tab_est, String tab_nomrep, String tab_usuadd, String tab_usumod) {
        this.tab_cod = tab_cod;
        this.tab_des = tab_des;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_tip = tab_tip;
        this.tab_ord = tab_ord;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;
    }

    //Para Mantenimiento de Unidades de Manejo
    public TtabGen(int tab_cod, String tab_des, int tab_id, String tab_subdes, String tab_subdir, int tab_ord, int tab_est, String tab_nomrep, String tab_usuadd, String tab_usumod) {
        this.tab_cod = tab_cod;
        this.tab_des = tab_des;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_subdir = tab_subdir;
        this.tab_ord = tab_ord;
        this.tab_est = tab_est;
        this.tab_nomrep = tab_nomrep;
        this.tab_usuadd = tab_usuadd;
        this.tab_usumod = tab_usumod;
    }

    public TtabGen() {
    }

    public boolean isVal() {
        val = tab_est == 1;
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public String getTab_subdes_tipo() {
        return tab_subdes_tipo;
    }

    public void setTab_subdes_tipo(String tab_subdes_tipo) {
        this.tab_subdes_tipo = tab_subdes_tipo;
    }

    public int getEmpresa() {
        return empresa;
    }

    public int getSucursal() {
        return sucursal;
    }

    public int getTab_almdef() {
        return tab_almdef;
    }

    public void setTab_almdef(int tab_almdef) {
        this.tab_almdef = tab_almdef;
    }

    public int getTab_almvta() {
        return tab_almvta;
    }

    public void setTab_almvta(int tab_almvta) {
        this.tab_almvta = tab_almvta;
    }

    public int getTab_cod() {
        return tab_cod;
    }

    public void setTab_cod(int tab_cod) {
        this.tab_cod = tab_cod;
    }

    public String getTab_des() {
        return tab_des;
    }

    public void setTab_des(String tab_des) {
        this.tab_des = tab_des;
    }

    public int getTab_empid() {
        return tab_empid;
    }

    public void setTab_empid(int tab_empid) {
        this.tab_empid = tab_empid;
    }

    public int getTab_est() {
        return tab_est;
    }

    public void setTab_est(int tab_est) {
        this.tab_est = tab_est;
    }

    public String getTab_guiccea() {
        return tab_guiccea;
    }

    public void setTab_guiccea(String tab_guiccea) {
        this.tab_guiccea = tab_guiccea;
    }

    public String getTab_guicceaf() {
        return tab_guicceaf;
    }

    public void setTab_guicceaf(String tab_guicceaf) {
        this.tab_guicceaf = tab_guicceaf;
    }

    public String getTab_guiccee() {
        return tab_guiccee;
    }

    public void setTab_guiccee(String tab_guiccee) {
        this.tab_guiccee = tab_guiccee;
    }

    public String getTab_guiccei() {
        return tab_guiccei;
    }

    public void setTab_guiccei(String tab_guiccei) {
        this.tab_guiccei = tab_guiccei;
    }

    public String getTab_guiccet() {
        return tab_guiccet;
    }

    public void setTab_guiccet(String tab_guiccet) {
        this.tab_guiccet = tab_guiccet;
    }

    public int getTab_guienl() {
        return tab_guienl;
    }

    public void setTab_guienl(int tab_guienl) {
        this.tab_guienl = tab_guienl;
    }

    public String getTab_guiingart() {
        return tab_guiingart;
    }

    public void setTab_guiingart(String tab_guiingart) {
        this.tab_guiingart = tab_guiingart;
    }

    public int getTab_guiref() {
        return tab_guiref;
    }

    public void setTab_guiref(int tab_guiref) {
        this.tab_guiref = tab_guiref;
    }

    public String getTab_guisun() {
        return tab_guisun;
    }

    public void setTab_guisun(String tab_guisun) {
        this.tab_guisun = tab_guisun;
    }

    public String getTab_guivouenl() {
        return tab_guivouenl;
    }

    public void setTab_guivouenl(String tab_guivouenl) {
        this.tab_guivouenl = tab_guivouenl;
    }

    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public String getTab_nomrep() {
        return tab_nomrep;
    }

    public void setTab_nomrep(String tab_nomrep) {
        this.tab_nomrep = tab_nomrep;
    }

    public int getTab_ord() {
        return tab_ord;
    }

    public void setTab_ord(int tab_ord) {
        this.tab_ord = tab_ord;
    }

    public String getTab_subdes() {
        return tab_subdes;
    }

    public void setTab_subdes(String tab_subdes) {
        this.tab_subdes = tab_subdes;
    }

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
    }

    public int getTab_sucid() {
        return tab_sucid;
    }

    public void setTab_sucid(int tab_sucid) {
        this.tab_sucid = tab_sucid;
    }

    public int getTab_tip() {
        return tab_tip;
    }

    public void setTab_tip(int tab_tip) {
        this.tab_tip = tab_tip;
    }

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public double getTab_alt() {
        return tab_alt;
    }

    public void setTab_alt(double tab_alt) {
        this.tab_alt = tab_alt;
    }

    public double getTab_anc() {
        return tab_anc;
    }

    public void setTab_anc(double tab_anc) {
        this.tab_anc = tab_anc;
    }

    public double getTab_lar() {
        return tab_lar;
    }

    public void setTab_lar(double tab_lar) {
        this.tab_lar = tab_lar;
    }

    public String getTab_stip() {
        return tab_stip;
    }

    public void setTab_stip(String tab_stip) {
        this.tab_stip = tab_stip;
    }

    public String getTab_usuadd() {
        return tab_usuadd;
    }

    public void setTab_usuadd(String tab_usuadd) {
        this.tab_usuadd = tab_usuadd;
    }

    public Date getTab_fecadd() {
        return tab_fecadd;
    }

    public void setTab_fecadd(Date tab_fecadd) {
        this.tab_fecadd = tab_fecadd;
    }

    public String getTab_usumod() {
        return tab_usumod;
    }

    public void setTab_usumod(String tab_usumod) {
        this.tab_usumod = tab_usumod;
    }

    public Date getTab_fecmod() {
        return tab_fecmod;
    }

    public void setTab_fecmod(Date tab_fecmod) {
        this.tab_fecmod = tab_fecmod;
    }

}
