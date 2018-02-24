package org.me.gj.model.logistica.procesos;

import java.text.*;
import java.util.Date;
import java.util.Locale;

public class PedidoCompraCab {

    private int emp_id;
    private int suc_id;
    private String per_id;
    private int per_key;
    private String pedcom_id;
    private long pedcom_key;
    private int pedcom_est;
    private String pedcom_estdes;
    private Date pedcom_fecemi;
    private String pedcom_sfecemi;
    private Date pedcom_fecrec;
    private String pedcom_sfecrec;
    private Date pedcom_feccad;
    private String pedcom_sfeccad;
    private int pedcom_mon;
    private String pedcom_mondes;
    private double pedcom_tipcam;
    private String pedcom_glo;
    private String pedcom_lispre;
    private String pedcom_lispredes;
    private String prov_id;
    private long prov_key;
    private long prov_ruc;
    private String prov_razsoc;
    private int con_key;
    private String con_tipo;
    private String con_des;
    private int pedcom_sit;
    private String pedcom_sitdes;
    private String pedcom_almori;
    private String pedcom_almorides;
    private String pedcom_almdes;
    private String pedcom_almdesdes;
    private String pedcom_usuadd;
    private Date pedcom_fecadd;
    private String pedcom_usumod;
    private Date pedcom_fecmod;
    private Double pedcom_total;
    private String pedcom_stotal;
    private String pedcom_lugent;
    private boolean valor;
    private boolean valSelec;
    DecimalFormatSymbols dfs;
    DecimalFormat df2;
    SimpleDateFormat sdf;

    public PedidoCompraCab(int emp_id, int suc_id, String per_id, int per_key, String pedcom_id, long pedcom_key,
            int pedcom_est, Date pedcom_fecemi, Date pedcom_fecrec, Date pedcom_feccad, double pedcom_tipcam, String pedcom_glo,
            long prov_key, long prov_ruc, int con_key, String con_tipo, int pedcom_sit,
            String pedcom_almori, String pedcom_almdes, String prov_id, String pedcom_usuadd, Date pedcom_fecadd, String pedcom_usumod, Date pedcom_fecmod) {
        dfs = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("#,###,##0.00", dfs);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.per_id = per_id;
        this.per_key = per_key;
        this.pedcom_id = pedcom_id;
        this.pedcom_key = pedcom_key;
        this.pedcom_est = pedcom_est;
        this.pedcom_fecemi = pedcom_fecemi;
        this.pedcom_fecrec = pedcom_fecrec;
        this.pedcom_feccad = pedcom_feccad;
        this.pedcom_tipcam = pedcom_tipcam;
        this.pedcom_glo = pedcom_glo;
        this.prov_key = prov_key;
        this.prov_ruc = prov_ruc;
        this.con_key = con_key;
        this.con_tipo = con_tipo;
        this.pedcom_sit = pedcom_sit;
        this.pedcom_almori = pedcom_almori;
        this.pedcom_almdes = pedcom_almdes;
        this.prov_id = prov_id;
        this.pedcom_usuadd = pedcom_usuadd;
        this.pedcom_fecadd = pedcom_fecadd;
        this.pedcom_usumod = pedcom_usumod;
        this.pedcom_fecmod = pedcom_fecmod;
    }

    public PedidoCompraCab() {
        dfs = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("#,###,##0.00", dfs);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
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

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public int getPer_key() {
        return per_key;
    }

    public void setPer_key(int per_key) {
        this.per_key = per_key;
    }

    public String getPedcom_id() {
        return pedcom_id;
    }

    public void setPedcom_id(String pedcom_id) {
        this.pedcom_id = pedcom_id;
    }

    public long getPedcom_key() {
        return pedcom_key;
    }

    public void setPedcom_key(long pedcom_key) {
        this.pedcom_key = pedcom_key;
    }

    public int getPedcom_est() {
        return pedcom_est;
    }

    public void setPedcom_est(int pedcom_est) {
        this.pedcom_est = pedcom_est;
    }

    public Date getPedcom_fecemi() {
        return pedcom_fecemi;
    }

    public void setPedcom_fecemi(Date pedcom_fecemi) {
        this.pedcom_fecemi = pedcom_fecemi;
    }

    public Date getPedcom_fecrec() {
        return pedcom_fecrec;
    }

    public void setPedcom_fecrec(Date pedcom_fecrec) {
        this.pedcom_fecrec = pedcom_fecrec;
    }

    public Date getPedcom_feccad() {
        return pedcom_feccad;
    }

    public void setPedcom_feccad(Date pedcom_feccad) {
        this.pedcom_feccad = pedcom_feccad;
    }

    public int getPedcom_mon() {
        return pedcom_mon;
    }

    public void setPedcom_mon(int pedcom_mon) {
        this.pedcom_mon = pedcom_mon;
    }

    public String getPedcom_mondes() {
        return pedcom_mondes;
    }

    public void setPedcom_mondes(String pedcom_mondes) {
        this.pedcom_mondes = pedcom_mondes;
    }

    public double getPedcom_tipcam() {
        return pedcom_tipcam;
    }

    public void setPedcom_tipcam(double pedcom_tipcam) {
        this.pedcom_tipcam = pedcom_tipcam;
    }

    public String getPedcom_glo() {
        return pedcom_glo;
    }

    public void setPedcom_glo(String pedcom_glo) {
        this.pedcom_glo = pedcom_glo;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public long getProv_ruc() {
        return prov_ruc;
    }

    public void setProv_ruc(long prov_ruc) {
        this.prov_ruc = prov_ruc;
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

    public int getPedcom_sit() {
        return pedcom_sit;
    }

    public void setPedcom_sit(int pedcom_sit) {
        this.pedcom_sit = pedcom_sit;
    }

    public String getPedcom_almori() {
        return pedcom_almori;
    }

    public void setPedcom_almori(String pedcom_almori) {
        this.pedcom_almori = pedcom_almori;
    }

    public String getPedcom_almdes() {
        return pedcom_almdes;
    }

    public void setPedcom_almdes(String pedcom_almdes) {
        this.pedcom_almdes = pedcom_almdes;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getPedcom_usuadd() {
        return pedcom_usuadd;
    }

    public void setPedcom_usuadd(String pedcom_usuadd) {
        this.pedcom_usuadd = pedcom_usuadd;
    }

    public Date getPedcom_fecadd() {
        return pedcom_fecadd;
    }

    public void setPedcom_fecadd(Date pedcom_fecadd) {
        this.pedcom_fecadd = pedcom_fecadd;
    }

    public String getPedcom_usumod() {
        return pedcom_usumod;
    }

    public void setPedcom_usumod(String pedcom_usumod) {
        this.pedcom_usumod = pedcom_usumod;
    }

    public Date getPedcom_fecmod() {
        return pedcom_fecmod;
    }

    public void setPedcom_fecmod(Date pedcom_fecmod) {
        this.pedcom_fecmod = pedcom_fecmod;
    }

    public boolean isValor() {
        if (pedcom_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPedcom_lispre() {
        return pedcom_lispre;
    }

    public void setPedcom_lispre(String pedcom_lispre) {
        this.pedcom_lispre = pedcom_lispre;
    }

    public String getPedcom_lispredes() {
        return pedcom_lispredes;
    }

    public void setPedcom_lispredes(String pedcom_lispredes) {
        this.pedcom_lispredes = pedcom_lispredes;
    }

    public String getPedcom_estdes() {
        return pedcom_estdes;
    }

    public void setPedcom_estdes(String pedcom_estdes) {
        this.pedcom_estdes = pedcom_estdes;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public String getCon_des() {
        return con_des;
    }

    public void setCon_des(String con_des) {
        this.con_des = con_des;
    }

    public String getPedcom_sitdes() {
        return pedcom_sitdes;
    }

    public void setPedcom_sitdes(String pedcom_sitdes) {
        this.pedcom_sitdes = pedcom_sitdes;
    }

    public String getPedcom_almorides() {
        return pedcom_almorides;
    }

    public void setPedcom_almorides(String pedcom_almorides) {
        this.pedcom_almorides = pedcom_almorides;
    }

    public String getPedcom_almdesdes() {
        return pedcom_almdesdes;
    }

    public void setPedcom_almdesdes(String pedcom_almdesdes) {
        this.pedcom_almdesdes = pedcom_almdesdes;
    }

    public Double getPedcom_total() {
        return pedcom_total;
    }

    public void setPedcom_total(Double pedcom_total) {
        this.pedcom_total = pedcom_total;
    }

    public String getPedcom_stotal() {
        //tcd_simpnetoc=df2.format(tcd_impnetoc);
        this.pedcom_stotal = df2.format(pedcom_total);
        return pedcom_stotal;
    }

    public String getPedcom_sfecemi() {
        String fecha_cadena = "";
        fecha_cadena = sdf.format(pedcom_fecemi);
        pedcom_sfecemi = fecha_cadena;
        return pedcom_sfecemi;
    }

    public String getPedcom_sfecrec() {
        String fecha_cadena = "";
        fecha_cadena = sdf.format(pedcom_fecrec);
        pedcom_sfecrec = fecha_cadena;
        return pedcom_sfecrec;
    }

    public String getPedcom_sfeccad() {
        String fecha_cadena = "";
        fecha_cadena = sdf.format(pedcom_feccad);
        pedcom_sfeccad = fecha_cadena;
        return pedcom_sfeccad;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getPedcom_lugent() {
        return pedcom_lugent;
    }

    public void setPedcom_lugent(String pedcom_lugent) {
        this.pedcom_lugent = pedcom_lugent;
    }

}
