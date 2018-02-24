package org.me.gj.model.logistica.procesos;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class NotaESDet implements Serializable {

    private String nescab_id;
    private String nescab_key;
    private String nescab_tipnotaes;
    private int emp_id;
    private int suc_id;
    private long nesdet_item;
    private String nesdet_tipmov;
    private int nesdet_est;
    private String nesdet_provid;
    private String nesdet_provrazsoc;
    private String pro_id;
    private long pro_key;
    private String pro_des;
    private String pro_desdes;
    private String nesdet_glosa;
    private double nesdet_cant;
    private int nesdet_ent;
    private int nesdet_frac;
    private int nesdet_undpre;
    private String nesdet_proconv;
    private long nesdet_cantconv;
    private double nesdet_pimpto;
    private String nesdet_spimpto;
    private double nesdet_vimpto;
    private String nesdet_svimpto;
    private double nesdet_valafe;
    private String nesdet_svalafe;
    private double nesdet_valina;
    private String nesdet_svalina;
    private double nesdet_pvta;
    private String nesdet_spvta;
    private String nesdet_almori;
    private String nesdet_almdes;
    private String nesdet_ubiori;
    private String nesdet_ubides;
    private double nesdet_cantfac;
    private double nesdet_peso;
    private double nesdet_cositem;
    private String nesdet_usuadd;
    private Date nesdet_fecadd;
    private String nesdet_usumod;
    private Date nesdet_fecmod;
    private boolean valor;
    private String ind_accion = "Q";

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);

    public NotaESDet(String nescab_id, String nescab_key, int emp_id, int suc_id, long nesdet_item, String nesdet_usumod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nesdet_item = nesdet_item;
        this.nesdet_usumod = nesdet_usumod;
    }

    public NotaESDet(String nescab_id, String nescab_key, String nescab_tipnotaes, int emp_id, int suc_id, long nesdet_item, String nesdet_tipmov, int nesdet_est,
            String nesdet_provid, String pro_id, long pro_key, String nesdet_glosa, double nesdet_cant, int nesdet_undpre, String nesdet_proconv, long nesdet_cantconv,
            double nesdet_pimpto, double nesdet_vimpto, double nesdet_valafe, double nesdet_valina, double nesdet_pvta, String nesdet_almori, String nesdet_almdes,
            String nesdet_ubiori, String nesdet_ubides, double nesdet_cantfac, double nesdet_peso, double nesdet_cositem, String nesdet_usuadd, Date nesdet_fecadd,
            String nesdet_usumod, Date nesdet_fecmod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.nescab_tipnotaes = nescab_tipnotaes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nesdet_item = nesdet_item;
        this.nesdet_tipmov = nesdet_tipmov;
        this.nesdet_est = nesdet_est;
        this.nesdet_provid = nesdet_provid;
        this.pro_id = pro_id;
        this.pro_key = pro_key;
        this.nesdet_glosa = nesdet_glosa;
        this.nesdet_cant = nesdet_cant;
        this.nesdet_undpre = nesdet_undpre;
        this.nesdet_proconv = nesdet_proconv;
        this.nesdet_cantconv = nesdet_cantconv;
        this.nesdet_pimpto = nesdet_pimpto;
        this.nesdet_vimpto = nesdet_vimpto;
        this.nesdet_valafe = nesdet_valafe;
        this.nesdet_valina = nesdet_valina;
        this.nesdet_pvta = nesdet_pvta;
        this.nesdet_almori = nesdet_almori;
        this.nesdet_almdes = nesdet_almdes;
        this.nesdet_ubiori = nesdet_ubiori;
        this.nesdet_ubides = nesdet_ubides;
        this.nesdet_cantfac = nesdet_cantfac;
        this.nesdet_peso = nesdet_peso;
        this.nesdet_cositem = nesdet_cositem;
        this.nesdet_usuadd = nesdet_usuadd;
        this.nesdet_fecadd = nesdet_fecadd;
        this.nesdet_usumod = nesdet_usumod;
        this.nesdet_fecmod = nesdet_fecmod;
    }

    public NotaESDet(String nescab_id, String nescab_key, String nescab_tipnotaes, int emp_id, int suc_id, long nesdet_item, String nesdet_tipmov, int nesdet_est,
            String nesdet_provid, String pro_id, long pro_key, double nesdet_cant, int nesdet_undpre, long nesdet_cantconv, double nesdet_pimpto, double nesdet_vimpto,
            double nesdet_valafe, double nesdet_valina, double nesdet_pvta, String nesdet_almori, String nesdet_almdes, String nesdet_ubiori, String nesdet_ubides,
            double nesdet_cantfac, double nesdet_peso, double nesdet_cositem, String nesdet_usuadd, String nesdet_usumod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.nescab_tipnotaes = nescab_tipnotaes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nesdet_item = nesdet_item;
        this.nesdet_tipmov = nesdet_tipmov;
        this.nesdet_est = nesdet_est;
        this.nesdet_provid = nesdet_provid;
        this.pro_id = pro_id;
        this.pro_key = pro_key;
        this.nesdet_cant = nesdet_cant;
        this.nesdet_undpre = nesdet_undpre;
        this.nesdet_cantconv = nesdet_cantconv;
        this.nesdet_pimpto = nesdet_pimpto;
        this.nesdet_vimpto = nesdet_vimpto;
        this.nesdet_valafe = nesdet_valafe;
        this.nesdet_valina = nesdet_valina;
        this.nesdet_pvta = nesdet_pvta;
        this.nesdet_almori = nesdet_almori;
        this.nesdet_almdes = nesdet_almdes;
        this.nesdet_ubiori = nesdet_ubiori;
        this.nesdet_ubides = nesdet_ubides;
        this.nesdet_cantfac = nesdet_cantfac;
        this.nesdet_peso = nesdet_peso;
        this.nesdet_cositem = nesdet_cositem;
        this.nesdet_usuadd = nesdet_usuadd;
        this.nesdet_usumod = nesdet_usumod;
    }

    public NotaESDet(String nescab_id, String nescab_key, String nescab_tipnotaes, int emp_id, int suc_id, String nesdet_almori, String nesdet_almdes, String nesdet_tipmov, String nesdet_provid, String nesdet_provrazsoc, String pro_id, String pro_des, String pro_desdes, long pro_key, String nesdet_ubiori, String nesdet_ubides, double nesdet_cant, int nesdet_undpre, long nesdet_cantconv, String nesdet_proconv, double nesdet_pimpto, double nesdet_vimpto, double nesdet_valafe, double nesdet_valina, double nesdet_pvta, int nesdet_est, double nesdet_cantfac, double nesdet_peso, double nesdet_cositem, String nesdet_usuadd, String nesdet_usumod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.nescab_tipnotaes = nescab_tipnotaes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nesdet_tipmov = nesdet_tipmov;
        this.nesdet_provid = nesdet_provid;
        this.nesdet_provrazsoc = nesdet_provrazsoc;
        this.pro_id = pro_id;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.pro_key = pro_key;
        this.nesdet_almori = nesdet_almori;
        this.nesdet_almdes = nesdet_almdes;
        this.nesdet_ubiori = nesdet_ubiori;
        this.nesdet_ubides = nesdet_ubides;
        this.nesdet_cant = nesdet_cant;
        this.nesdet_undpre = nesdet_undpre;
        this.nesdet_cantconv = nesdet_cantconv;
        this.nesdet_proconv = nesdet_proconv;
        this.nesdet_pimpto = nesdet_pimpto;
        this.nesdet_vimpto = nesdet_vimpto;
        this.nesdet_valafe = nesdet_valafe;
        this.nesdet_valina = nesdet_valina;
        this.nesdet_pvta = nesdet_pvta;
        this.nesdet_est = nesdet_est;
        this.nesdet_cantfac = nesdet_cantfac;
        this.nesdet_peso = nesdet_peso;
        this.nesdet_cositem = nesdet_cositem;
        this.nesdet_usuadd = nesdet_usuadd;
        this.nesdet_usumod = nesdet_usumod;
    }

    public NotaESDet() {
    }

    public String getNescab_id() {
        return nescab_id;
    }

    public void setNescab_id(String nescab_id) {
        this.nescab_id = nescab_id;
    }

    public String getNescab_key() {
        return nescab_key;
    }

    public void setNescab_key(String nescab_key) {
        this.nescab_key = nescab_key;
    }

    public String getNescab_tipnotaes() {
        return nescab_tipnotaes;
    }

    public void setNescab_tipnotaes(String nescab_tipnotaes) {
        this.nescab_tipnotaes = nescab_tipnotaes;
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

    public long getNesdet_item() {
        return nesdet_item;
    }

    public void setNesdet_item(long nesdet_item) {
        this.nesdet_item = nesdet_item;
    }

    public String getNesdet_tipmov() {
        return nesdet_tipmov;
    }

    public void setNesdet_tipmov(String nesdet_tipmov) {
        this.nesdet_tipmov = nesdet_tipmov;
    }

    public int getNesdet_est() {
        return nesdet_est;
    }

    public void setNesdet_est(int nesdet_est) {
        this.nesdet_est = nesdet_est;
    }

    public String getNesdet_provid() {
        return nesdet_provid;
    }

    public void setNesdet_provid(String nesdet_provid) {
        this.nesdet_provid = nesdet_provid;
    }

    public String getNesdet_provrazsoc() {
        return nesdet_provrazsoc;
    }

    public void setNesdet_provrazsoc(String nesdet_provrazsoc) {
        this.nesdet_provrazsoc = nesdet_provrazsoc;
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

    public String getPro_desdes() {
        return pro_desdes;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public String getNesdet_glosa() {
        return nesdet_glosa;
    }

    public void setNesdet_glosa(String nesdet_glosa) {
        this.nesdet_glosa = nesdet_glosa;
    }

    public double getNesdet_cant() {
        return nesdet_cant;
    }

    public void setNesdet_cant(double nesdet_cant) {
        this.nesdet_cant = nesdet_cant;
    }

    public int getNesdet_undpre() {
        return nesdet_undpre;
    }

    public void setNesdet_undpre(int nesdet_undpre) {
        this.nesdet_undpre = nesdet_undpre;
    }

    public String getNesdet_proconv() {
        return nesdet_proconv;
    }

    public void setNesdet_proconv(String nesdet_proconv) {
        this.nesdet_proconv = nesdet_proconv;
    }

    public long getNesdet_cantconv() {
        return nesdet_cantconv;
    }

    public void setNesdet_cantconv(long nesdet_cantconv) {
        this.nesdet_cantconv = nesdet_cantconv;
    }

    public double getNesdet_pimpto() {
        return nesdet_pimpto;
    }

    public void setNesdet_pimpto(double nesdet_pimpto) {
        this.nesdet_pimpto = nesdet_pimpto;
    }

    public double getNesdet_vimpto() {
        return nesdet_vimpto;
    }

    public void setNesdet_vimpto(double nesdet_vimpto) {
        this.nesdet_vimpto = nesdet_vimpto;
    }

    public double getNesdet_valafe() {
        return nesdet_valafe;
    }

    public void setNesdet_valafe(double nesdet_valafe) {
        this.nesdet_valafe = nesdet_valafe;
    }

    public String getNesdet_spimpto() {
        this.nesdet_spimpto = df2.format(nesdet_pimpto);
        return nesdet_spimpto;
    }

    public String getNesdet_svimpto() {
        this.nesdet_svimpto = df2.format(nesdet_vimpto);
        return nesdet_svimpto;
    }

    public String getNesdet_svalafe() {
        this.nesdet_svalafe = df2.format(nesdet_valafe);
        return nesdet_svalafe;
    }

    public String getNesdet_svalina() {
        this.nesdet_svalina = df2.format(nesdet_valina);
        return nesdet_svalina;
    }

    public String getNesdet_spvta() {
        this.nesdet_spvta = df2.format(nesdet_pvta);
        return nesdet_spvta;
    }

    public double getNesdet_valina() {
        return nesdet_valina;
    }

    public void setNesdet_valina(double nesdet_valina) {
        this.nesdet_valina = nesdet_valina;
    }

    public double getNesdet_pvta() {
        return nesdet_pvta;
    }

    public void setNesdet_pvta(double nesdet_pvta) {
        this.nesdet_pvta = nesdet_pvta;
    }

    public String getNesdet_almori() {
        return nesdet_almori;
    }

    public void setNesdet_almori(String nesdet_almori) {
        this.nesdet_almori = nesdet_almori;
    }

    public String getNesdet_almdes() {
        return nesdet_almdes;
    }

    public void setNesdet_almdes(String nesdet_almdes) {
        this.nesdet_almdes = nesdet_almdes;
    }

    public String getNesdet_ubiori() {
        return nesdet_ubiori;
    }

    public void setNesdet_ubiori(String nesdet_ubiori) {
        this.nesdet_ubiori = nesdet_ubiori;
    }

    public String getNesdet_ubides() {
        return nesdet_ubides;
    }

    public void setNesdet_ubides(String nesdet_ubides) {
        this.nesdet_ubides = nesdet_ubides;
    }

    public double getNesdet_cantfac() {
        return nesdet_cantfac;
    }

    public void setNesdet_cantfac(double nesdet_cantfac) {
        this.nesdet_cantfac = nesdet_cantfac;
    }

    public double getNesdet_peso() {
        return nesdet_peso;
    }

    public void setNesdet_peso(double nesdet_peso) {
        this.nesdet_peso = nesdet_peso;
    }

    public double getNesdet_cositem() {
        return nesdet_cositem;
    }

    public void setNesdet_cositem(double nesdet_cositem) {
        this.nesdet_cositem = nesdet_cositem;
    }

    public String getNesdet_usuadd() {
        return nesdet_usuadd;
    }

    public void setNesdet_usuadd(String nesdet_usuadd) {
        this.nesdet_usuadd = nesdet_usuadd;
    }

    public Date getNesdet_fecadd() {
        return nesdet_fecadd;
    }

    public void setNesdet_fecadd(Date nesdet_fecadd) {
        this.nesdet_fecadd = nesdet_fecadd;
    }

    public String getNesdet_usumod() {
        return nesdet_usumod;
    }

    public void setNesdet_usumod(String nesdet_usumod) {
        this.nesdet_usumod = nesdet_usumod;
    }

    public Date getNesdet_fecmod() {
        return nesdet_fecmod;
    }

    public void setNesdet_fecmod(Date nesdet_fecmod) {
        this.nesdet_fecmod = nesdet_fecmod;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isValor() {
        valor = nesdet_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getNesdet_ent() {
        //nesdet_ent = Math.round(Math.round(nesdet_cant * nesdet_undpre) / nesdet_undpre);
        //nesdet_ent = Math.round(nesdet_cant / nesdet_undpre);
        nesdet_ent = (int) (nesdet_cant / nesdet_undpre);
        return nesdet_ent;
    }

    public void setNesdet_ent(int nesdet_ent) {
        this.nesdet_ent = nesdet_ent;
    }

    public int getNesdet_frac() {
        //nesdet_frac = Math.round(nesdet_cant * nesdet_undpre) % nesdet_undpre;
        nesdet_frac = (int) Math.round(nesdet_cant % nesdet_undpre);

        return nesdet_frac;
    }

    public void setNesdet_frac(int nesdet_frac) {
        this.nesdet_frac = nesdet_frac;
    }
}
