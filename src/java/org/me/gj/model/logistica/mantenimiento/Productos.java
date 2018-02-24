package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class Productos {

    private String pro_id;
    private int pro_key;
    private int pro_est;
    private String pro_des; //Longitud 60
    private String pro_desdes; //Longitud 42
    private String pro_descor; // Longitud 25
    private String pro_lin;
    private String pro_sublin;
    private String pro_mar;
    private String pro_clas;
    private String pro_tip;
    private int pro_ordkar;
    private int pro_ordlstprec;
    private int pro_ordcons;
    private int pro_ord;
    private int pro_tipexisun;
    private int pro_indperc;
    private String pro_medunisun;
    private String pro_provid;
    private String pro_codori;
    private String pro_conv;
    private int pro_unimas;
    private String pro_unimanven;
    private String pro_empindven;
    private int pro_presminven;
    private int pro_unibodeguero;
    private String pro_frac;
    private String pro_proc;
    private int imp_id;
    private String pro_condimp;
    private int pro_ubi;
    private double pro_alt;
    private double pro_anc;
    private double pro_lar;
    private int pro_rot;
    private double pro_peso;
    private String pro_unipeso;
    private double pro_vol;
    private String pro_unimancom;
    private String pro_empindcom;
    private int pro_presmincom;
    private int pro_scknofact;
    private String pro_codbar;
    private String pro_tipcodbar;
    private String pro_imgcodbar;
    private String pro_ingprod;
    private String pro_img;
    private String pro_usuadd;
    private Date pro_fecadd;
    private String pro_usumod;
    private Date pro_fecmod;
    private boolean valor;
    private String pro_sigprov;
    private String pro_desmar;
    private String pro_linea;
    private String pro_sublinea;
    private int pro_afecam;
    public double volubi;
    public double voltotprod;
    public double voldisp;

    private int pro_provpresukey;
    private String pro_provpresuid;
    private String pro_provpresudes;
    private String pro_codean13prov;
    private String pro_codean14prov;

    public Productos(String pro_id, int pro_key, int pro_est, String pro_des, String pro_desdes, String pro_descor, String pro_lin, String pro_sublin,
            String pro_mar, String pro_clas, String pro_tip, int pro_ordkar, int pro_ordlstprec, int pro_ordcons, int pro_ord, int pro_tipexisun,
            int pro_indperc, String pro_medunisun, String pro_provid, String pro_codori, String pro_conv, int pro_unimas, String pro_unimanven,
            String pro_empindven, int pro_presminven, int pro_unibodeguero, String pro_frac, String pro_proc, int imp_id, String pro_condimp,
            int pro_ubi, double pro_alt, double pro_anc, double pro_lar, int pro_rot, double pro_peso, String pro_unipeso, double pro_vol,
            String pro_unimancom, String pro_empindcom, int pro_presmincom, int pro_scknofact, String pro_codbar, String pro_tipcodbar,
            String pro_imgcodbar, String pro_img, int pro_afecam, String pro_usuadd, String pro_usumod, int pro_provpresukey, String pro_codean13prov, String pro_codean14prov) {
        this.pro_id = pro_id;
        this.pro_key = pro_key;
        this.pro_est = pro_est;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.pro_descor = pro_descor;
        this.pro_lin = pro_lin;
        this.pro_sublin = pro_sublin;
        this.pro_mar = pro_mar;
        this.pro_clas = pro_clas;
        this.pro_tip = pro_tip;
        this.pro_ordkar = pro_ordkar;
        this.pro_ordlstprec = pro_ordlstprec;
        this.pro_ordcons = pro_ordcons;
        this.pro_ord = pro_ord;
        this.pro_tipexisun = pro_tipexisun;
        this.pro_indperc = pro_indperc;
        this.pro_medunisun = pro_medunisun;
        this.pro_provid = pro_provid;
        this.pro_codori = pro_codori;
        this.pro_conv = pro_conv;
        this.pro_unimas = pro_unimas;
        this.pro_unimanven = pro_unimanven;
        this.pro_empindven = pro_empindven;
        this.pro_presminven = pro_presminven;
        this.pro_unibodeguero = pro_unibodeguero;
        this.pro_frac = pro_frac;
        this.pro_proc = pro_proc;
        this.imp_id = imp_id;
        this.pro_condimp = pro_condimp;
        this.pro_ubi = pro_ubi;
        this.pro_alt = pro_alt;
        this.pro_anc = pro_anc;
        this.pro_lar = pro_lar;
        this.pro_rot = pro_rot;
        this.pro_peso = pro_peso;
        this.pro_unipeso = pro_unipeso;
        this.pro_vol = pro_vol;
        this.pro_unimancom = pro_unimancom;
        this.pro_empindcom = pro_empindcom;
        this.pro_presmincom = pro_presmincom;
        this.pro_scknofact = pro_scknofact;
        this.pro_codbar = pro_codbar;
        this.pro_tipcodbar = pro_tipcodbar;
        this.pro_imgcodbar = pro_imgcodbar;
        this.pro_img = pro_img;
        this.pro_usuadd = pro_usuadd;
        this.pro_usumod = pro_usumod;
        this.pro_afecam = pro_afecam;
        this.pro_provpresukey = pro_provpresukey;
        this.pro_codean13prov = pro_codean13prov;
        this.pro_codean14prov = pro_codean14prov;
    }

    public Productos(int pro_key, String pro_des, String pro_codori) {
        this.pro_key = pro_key;
        this.pro_des = pro_des;
        this.pro_codori = pro_codori;
    }

    public Productos() {
    }

    public String getPro_desmar() {
        return pro_desmar;
    }

    public void setPro_desmar(String pro_desmar) {
        this.pro_desmar = pro_desmar;
    }

    public String getPro_sigprov() {
        return pro_sigprov;
    }

    public void setPro_sigprov(String pro_sigprov) {
        this.pro_sigprov = pro_sigprov;
    }

    public boolean isValor() {
        if (this.pro_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getImp_id() {
        return imp_id;
    }

    public void setImp_id(int imp_id) {
        this.imp_id = imp_id;
    }

    public double getPro_alt() {
        return pro_alt;
    }

    public void setPro_alt(double pro_alt) {
        this.pro_alt = pro_alt;
    }

    public double getPro_anc() {
        return pro_anc;
    }

    public void setPro_anc(double pro_anc) {
        this.pro_anc = pro_anc;
    }

    public String getPro_clas() {
        return pro_clas;
    }

    public void setPro_clas(String pro_clas) {
        this.pro_clas = pro_clas;
    }

    public String getPro_codbar() {
        return pro_codbar;
    }

    public void setPro_codbar(String pro_codbar) {
        this.pro_codbar = pro_codbar;
    }

    public String getPro_codori() {
        return pro_codori;
    }

    public void setPro_codori(String pro_codori) {
        this.pro_codori = pro_codori;
    }

    public String getPro_condimp() {
        return pro_condimp;
    }

    public void setPro_condimp(String pro_condimp) {
        this.pro_condimp = pro_condimp;
    }

    public String getPro_conv() {
        return pro_conv;
    }

    public void setPro_conv(String pro_conv) {
        this.pro_conv = pro_conv;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public String getPro_descor() {
        return pro_descor;
    }

    public void setPro_descor(String pro_descor) {
        this.pro_descor = pro_descor;
    }

    public String getPro_empindcom() {
        return pro_empindcom;
    }

    public void setPro_empindcom(String pro_empindcom) {
        this.pro_empindcom = pro_empindcom;
    }

    public String getPro_empindven() {
        return pro_empindven;
    }

    public void setPro_empindven(String pro_empindven) {
        this.pro_empindven = pro_empindven;
    }

    public int getPro_est() {
        return pro_est;
    }

    public void setPro_est(int pro_est) {
        this.pro_est = pro_est;
    }

    public String getPro_frac() {
        return pro_frac;
    }

    public void setPro_frac(String pro_frac) {
        this.pro_frac = pro_frac;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getPro_imgcodbar() {
        return pro_imgcodbar;
    }

    public void setPro_imgcodbar(String pro_imgcodbar) {
        this.pro_imgcodbar = pro_imgcodbar;
    }

    public int getPro_indperc() {
        return pro_indperc;
    }

    public void setPro_indperc(int pro_indperc) {
        this.pro_indperc = pro_indperc;
    }

    public String getPro_ingprod() {
        return pro_ingprod;
    }

    public void setPro_ingprod(String pro_ingprod) {
        this.pro_ingprod = pro_ingprod;
    }

    public int getPro_key() {
        return pro_key;
    }

    public void setPro_key(int pro_key) {
        this.pro_key = pro_key;
    }

    public double getPro_lar() {
        return pro_lar;
    }

    public void setPro_lar(double pro_lar) {
        this.pro_lar = pro_lar;
    }

    public String getPro_lin() {
        return pro_lin;
    }

    public void setPro_lin(String pro_lin) {
        this.pro_lin = pro_lin;
    }

    public String getPro_mar() {
        return pro_mar;
    }

    public void setPro_mar(String pro_mar) {
        this.pro_mar = pro_mar;
    }

    public String getPro_medunisun() {
        return pro_medunisun;
    }

    public void setPro_medunisun(String pro_medunisun) {
        this.pro_medunisun = pro_medunisun;
    }

    public int getPro_ord() {
        return pro_ord;
    }

    public void setPro_ord(int pro_ord) {
        this.pro_ord = pro_ord;
    }

    public int getPro_ordcons() {
        return pro_ordcons;
    }

    public void setPro_ordcons(int pro_ordcons) {
        this.pro_ordcons = pro_ordcons;
    }

    public int getPro_ordkar() {
        return pro_ordkar;
    }

    public void setPro_ordkar(int pro_ordkar) {
        this.pro_ordkar = pro_ordkar;
    }

    public int getPro_ordlstprec() {
        return pro_ordlstprec;
    }

    public void setPro_ordlstprec(int pro_ordlstprec) {
        this.pro_ordlstprec = pro_ordlstprec;
    }

    public double getPro_peso() {
        return pro_peso;
    }

    public void setPro_peso(double pro_peso) {
        this.pro_peso = pro_peso;
    }

    public int getPro_presmincom() {
        return pro_presmincom;
    }

    public void setPro_presmincom(int pro_presmincom) {
        this.pro_presmincom = pro_presmincom;
    }

    public int getPro_presminven() {
        return pro_presminven;
    }

    public void setPro_presminven(int pro_presminven) {
        this.pro_presminven = pro_presminven;
    }

    public String getPro_proc() {
        return pro_proc;
    }

    public void setPro_proc(String pro_proc) {
        this.pro_proc = pro_proc;
    }

    public String getPro_provid() {
        return pro_provid;
    }

    public void setPro_provid(String pro_provid) {
        this.pro_provid = pro_provid;
    }

    public int getPro_rot() {
        return pro_rot;
    }

    public void setPro_rot(int pro_rot) {
        this.pro_rot = pro_rot;
    }

    public int getPro_scknofact() {
        return pro_scknofact;
    }

    public void setPro_scknofact(int pro_scknofact) {
        this.pro_scknofact = pro_scknofact;
    }

    public String getPro_sublin() {
        return pro_sublin;
    }

    public void setPro_sublin(String pro_sublin) {
        this.pro_sublin = pro_sublin;
    }

    public String getPro_tip() {
        return pro_tip;
    }

    public void setPro_tip(String pro_tip) {
        this.pro_tip = pro_tip;
    }

    public String getPro_tipcodbar() {
        return pro_tipcodbar;
    }

    public void setPro_tipcodbar(String pro_tipcodbar) {
        this.pro_tipcodbar = pro_tipcodbar;
    }

    public int getPro_tipexisun() {
        return pro_tipexisun;
    }

    public void setPro_tipexisun(int pro_tipexisun) {
        this.pro_tipexisun = pro_tipexisun;
    }

    public int getPro_ubi() {
        return pro_ubi;
    }

    public void setPro_ubi(int pro_ubi) {
        this.pro_ubi = pro_ubi;
    }

    public String getPro_unimancom() {
        return pro_unimancom;
    }

    public void setPro_unimancom(String pro_unimancom) {
        this.pro_unimancom = pro_unimancom;
    }

    public String getPro_unimanven() {
        return pro_unimanven;
    }

    public void setPro_unimanven(String pro_unimanven) {
        this.pro_unimanven = pro_unimanven;
    }

    public int getPro_unimas() {
        return pro_unimas;
    }

    public void setPro_unimas(int pro_unimas) {
        this.pro_unimas = pro_unimas;
    }

    public String getPro_unipeso() {
        return pro_unipeso;
    }

    public void setPro_unipeso(String pro_unipeso) {
        this.pro_unipeso = pro_unipeso;
    }

    public String getPro_usuadd() {
        return pro_usuadd;
    }

    public void setPro_usuadd(String pro_usuadd) {
        this.pro_usuadd = pro_usuadd;
    }

    public String getPro_usumod() {
        return pro_usumod;
    }

    public void setPro_usumod(String pro_usumod) {
        this.pro_usumod = pro_usumod;
    }

    public double getPro_vol() {
        return pro_vol;
    }

    public void setPro_vol(double pro_vol) {
        this.pro_vol = pro_vol;
    }

    public Date getPro_fecmod() {
        return pro_fecmod;
    }

    public void setPro_fecmod(Date pro_fecmod) {
        this.pro_fecmod = pro_fecmod;
    }

    public Date getPro_fecadd() {
        return pro_fecadd;
    }

    public void setPro_fecadd(Date pro_fecadd) {
        this.pro_fecadd = pro_fecadd;
    }

    public String getPro_linea() {
        return pro_linea;
    }

    public void setPro_linea(String pro_linea) {
        this.pro_linea = pro_linea;
    }

    public int getPro_afecam() {
        return pro_afecam;
    }

    public void setPro_afecam(int pro_afecam) {
        this.pro_afecam = pro_afecam;
    }

    public String getPro_desdes() {
        return pro_desdes;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public String getPro_sublinea() {
        return pro_sublinea;
    }

    public void setPro_sublinea(String pro_sublinea) {
        this.pro_sublinea = pro_sublinea;
    }

    public double getVolubi() {
        return volubi;
    }

    public void setVolubi(double volubi) {
        this.volubi = volubi;
    }

    public double getVoltotprod() {
        return voltotprod;
    }

    public void setVoltotprod(double voltotprod) {
        this.voltotprod = voltotprod;
    }

    public double getVoldisp() {
        return voldisp;
    }

    public void setVoldisp(double voldisp) {
        this.voldisp = voldisp;
    }

    public int getPro_unibodeguero() {
        return pro_unibodeguero;
    }

    public void setPro_unibodeguero(int pro_unibodeguero) {
        this.pro_unibodeguero = pro_unibodeguero;
    }

    public int getPro_provpresukey() {
        return pro_provpresukey;
    }

    public void setPro_provpresukey(int pro_provpresukey) {
        this.pro_provpresukey = pro_provpresukey;
    }

    public String getPro_provpresuid() {
        return pro_provpresuid;
    }

    public void setPro_provpresuid(String pro_provpresuid) {
        this.pro_provpresuid = pro_provpresuid;
    }

    public String getPro_provpresudes() {
        return pro_provpresudes;
    }

    public void setPro_provpresudes(String pro_provpresudes) {
        this.pro_provpresudes = pro_provpresudes;
    }

    public String getPro_codean13prov() {
        return pro_codean13prov;
    }

    public void setPro_codean13prov(String pro_codean13prov) {
        this.pro_codean13prov = pro_codean13prov;
    }

    public String getPro_codean14prov() {
        return pro_codean14prov;
    }

    public void setPro_codean14prov(String pro_codean14prov) {
        this.pro_codean14prov = pro_codean14prov;
    }
    
}

