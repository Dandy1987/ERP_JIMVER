package org.me.gj.model.logistica.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrdCompCab {

    private int emp_id;
    private int suc_id;
    private String oc_ind;
    private long oc_nropedkey;
    private String oc_nropedid;
    private long oc_provid;
    private int oc_moneda;
    private double oc_tcambio;
    private int oc_est;
    private int oc_situacion;
    private String oc_usuapro;
    private Date oc_fecapro;
    private String oc_pcapro;
    private int oc_almori;
    private int oc_almdes;
    private double oc_vafecto;
    private double oc_exonerado;
    private double oc_vimpt;
    private double oc_vtotal;
    private Date oc_fecemi;
    private Date oc_fecrec;
    private Date oc_feccad;
    private String oc_glosa;
    private int oc_periodo;
    private int oc_conid;
    private int oc_lpcid;
    private double oc_vdesc;
    private long pedcom_key;
    private String oc_usuadd;
    private Date oc_fecadd;
    private String oc_pcadd;
    private String oc_usumod;
    private Date oc_fecmod;
    private String oc_pcmod;
    private boolean valor;
    private boolean valSelec;
    private String prov_id;
    private String oc_provrazsoc;
    private String oc_condes;
    private String oc_lpcdes;
    private String oc_mondes;
    private String oc_sitdes;
    private String oc_almorides;
    private String oc_almdesdes;
    private String oc_svtotal;
    private String oc_sfecemi;
    private String oc_sfecrec;
    private String oc_sfeccad;
    private String pedcom_id;
    private double lp_descgen;
    private double lp_descfinan;
    private final DecimalFormatSymbols dfs;
    private final DecimalFormat df2;
    private final SimpleDateFormat formato;

    public OrdCompCab(int emp_id, int suc_id, String oc_ind, long oc_nropedkey, long oc_provid, int oc_moneda, double oc_tcambio, double oc_vafecto, double oc_exonerado, double oc_vimpt, double oc_vtotal, Date oc_fecemi, Date oc_fecrec, Date oc_feccad, String oc_glosa, int oc_conid, int oc_lpcid, double oc_vdesc, String oc_usuadd, String oc_pcadd, String oc_usumod, String oc_pcmod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.oc_ind = oc_ind;
        this.oc_nropedkey = oc_nropedkey;
        this.oc_provid = oc_provid;
        this.oc_moneda = oc_moneda;
        this.oc_tcambio = oc_tcambio;
        this.oc_vafecto = oc_vafecto;
        this.oc_exonerado = oc_exonerado;
        this.oc_vimpt = oc_vimpt;
        this.oc_vtotal = oc_vtotal;
        this.oc_fecemi = oc_fecemi;
        this.oc_fecrec = oc_fecrec;
        this.oc_feccad = oc_feccad;
        this.oc_glosa = oc_glosa;
        this.oc_conid = oc_conid;
        this.oc_lpcid = oc_lpcid;
        this.oc_vdesc = oc_vdesc;
        this.oc_usuadd = oc_usuadd;
        this.oc_pcadd = oc_pcadd;
        this.oc_usumod = oc_usumod;
        this.oc_pcmod = oc_pcmod;
        dfs = new DecimalFormatSymbols(Locale.US);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        df2 = new DecimalFormat("#,###,##0.00", dfs);
    }

    public OrdCompCab() {
        dfs = new DecimalFormatSymbols(Locale.US);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        df2 = new DecimalFormat("#,###,##0.00", dfs);
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

    public String getOc_ind() {
        return oc_ind;
    }

    public void setOc_ind(String oc_ind) {
        this.oc_ind = oc_ind;
    }

    public long getOc_nropedkey() {
        return oc_nropedkey;
    }

    public void setOc_nropedkey(long oc_nropedkey) {
        this.oc_nropedkey = oc_nropedkey;
    }

    public String getOc_nropedid() {
        return oc_nropedid;
    }

    public void setOc_nropedid(String oc_nropedid) {
        this.oc_nropedid = oc_nropedid;
    }

    public long getOc_provid() {
        return oc_provid;
    }

    public void setOc_provid(long oc_provid) {
        this.oc_provid = oc_provid;
    }

    public int getOc_moneda() {
        return oc_moneda;
    }

    public void setOc_moneda(int oc_moneda) {
        this.oc_moneda = oc_moneda;
    }

    public double getOc_tcambio() {
        return oc_tcambio;
    }

    public void setOc_tcambio(double oc_tcambio) {
        this.oc_tcambio = oc_tcambio;
    }

    public int getOc_est() {
        return oc_est;
    }

    public void setOc_est(int oc_est) {
        this.oc_est = oc_est;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public int getOc_situacion() {
        return oc_situacion;
    }

    public void setOc_situacion(int oc_situacion) {
        this.oc_situacion = oc_situacion;
    }

    public String getOc_usuapro() {
        return oc_usuapro;
    }

    public void setOc_usuapro(String oc_usuapro) {
        this.oc_usuapro = oc_usuapro;
    }

    public Date getOc_fecapro() {
        return oc_fecapro;
    }

    public void setOc_fecapro(Date oc_fecapro) {
        this.oc_fecapro = oc_fecapro;
    }

    public String getOc_pcapro() {
        return oc_pcapro;
    }

    public void setOc_pcapro(String oc_pcapro) {
        this.oc_pcapro = oc_pcapro;
    }

    public int getOc_almori() {
        return oc_almori;
    }

    public void setOc_almori(int oc_almori) {
        this.oc_almori = oc_almori;
    }

    public int getOc_almdes() {
        return oc_almdes;
    }

    public void setOc_almdes(int oc_almdes) {
        this.oc_almdes = oc_almdes;
    }

    public double getOc_vafecto() {
        return oc_vafecto;
    }

    public void setOc_vafecto(double oc_vafecto) {
        this.oc_vafecto = oc_vafecto;
    }

    public double getOc_exonerado() {
        return oc_exonerado;
    }

    public void setOc_exonerado(double oc_exonerado) {
        this.oc_exonerado = oc_exonerado;
    }

    public double getOc_vimpt() {
        return oc_vimpt;
    }

    public void setOc_vimpt(double oc_vimpt) {
        this.oc_vimpt = oc_vimpt;
    }

    public double getOc_vtotal() {
        return oc_vtotal;
    }

    public void setOc_vtotal(double oc_vtotal) {
        this.oc_vtotal = oc_vtotal;
    }

    public String getOc_svtotal1(double oc_vtotal) {
        this.oc_svtotal = df2.format(oc_vtotal);
        return oc_svtotal;
    }

    public String getOc_svtotal() {
        this.oc_svtotal = df2.format(oc_vtotal);
        return oc_svtotal;
    }

    public Date getOc_fecemi() throws ParseException {
        return oc_fecemi;
    }

    public void setOc_fecemi(Date oc_fecemi) {
        this.oc_fecemi = oc_fecemi;
    }

    public Date getOc_fecrec() throws ParseException {
        return oc_fecrec;
    }

    public void setOc_fecrec(Date oc_fecrec) {
        this.oc_fecrec = oc_fecrec;
    }

    public Date getOc_feccad() throws ParseException {
        return oc_feccad;
    }

    public void setOc_feccad(Date oc_feccad) {
        this.oc_feccad = oc_feccad;
    }

    public String getOc_sfecemi() {
        oc_sfecemi = formato.format(oc_fecemi);
        return oc_sfecemi;
    }

    public String getOc_sfecrec() {
        oc_sfecrec = formato.format(oc_fecrec);
        return oc_sfecrec;
    }

    public String getOc_sfeccad() {
        oc_sfeccad = formato.format(oc_feccad);
        return oc_sfeccad;
    }

    public String getOc_glosa() {
        return oc_glosa;
    }

    public void setOc_glosa(String oc_glosa) {
        this.oc_glosa = oc_glosa;
    }

    public int getOc_periodo() {
        return oc_periodo;
    }

    public void setOc_periodo(int oc_periodo) {
        this.oc_periodo = oc_periodo;
    }

    public int getOc_conid() {
        return oc_conid;
    }

    public void setOc_conid(int oc_conid) {
        this.oc_conid = oc_conid;
    }

    public int getOc_lpcid() {
        return oc_lpcid;
    }

    public void setOc_lpcid(int oc_lpcid) {
        this.oc_lpcid = oc_lpcid;
    }

    public double getOc_vdesc() {
        return oc_vdesc;
    }

    public void setOc_vdesc(double oc_vdesc) {
        this.oc_vdesc = oc_vdesc;
    }

    public long getPedcom_key() {
        return pedcom_key;
    }

    public void setPedcom_key(long pedcom_key) {
        this.pedcom_key = pedcom_key;
    }

    public String getPedcom_id() {
        return pedcom_id;
    }

    public void setPedcom_id(String pedcom_id) {
        this.pedcom_id = pedcom_id;
    }

    public String getOc_usuadd() {
        return oc_usuadd;
    }

    public void setOc_usuadd(String oc_usuadd) {
        this.oc_usuadd = oc_usuadd;
    }

    public Date getOc_fecadd() {
        return oc_fecadd;
    }

    public void setOc_fecadd(Date oc_fecadd) {
        this.oc_fecadd = oc_fecadd;
    }

    public String getOc_pcadd() {
        return oc_pcadd;
    }

    public void setOc_pcadd(String oc_pcadd) {
        this.oc_pcadd = oc_pcadd;
    }

    public String getOc_usumod() {
        return oc_usumod;
    }

    public void setOc_usumod(String oc_usumod) {
        this.oc_usumod = oc_usumod;
    }

    public Date getOc_fecmod() {
        return oc_fecmod;
    }

    public void setOc_fecmod(Date oc_fecmod) {
        this.oc_fecmod = oc_fecmod;
    }

    public String getOc_pcmod() {
        return oc_pcmod;
    }

    public void setOc_pcmod(String oc_pcmod) {
        this.oc_pcmod = oc_pcmod;
    }

    public boolean isValor() {
        valor = oc_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getOc_provrazsoc() {
        return oc_provrazsoc;
    }

    public void setOc_provrazsoc(String oc_provrazsoc) {
        this.oc_provrazsoc = oc_provrazsoc;
    }

    public String getOc_condes() {
        return oc_condes;
    }

    public void setOc_condes(String oc_condes) {
        this.oc_condes = oc_condes;
    }

    public String getOc_lpcdes() {
        return oc_lpcdes;
    }

    public void setOc_lpcdes(String oc_lpcdes) {
        this.oc_lpcdes = oc_lpcdes;
    }

    public String getOc_mondes() {
        return oc_mondes;
    }

    public void setOc_mondes(String oc_mondes) {
        this.oc_mondes = oc_mondes;
    }

    public String getOc_sitdes() {
        if (oc_situacion == 1) {
            oc_sitdes = "POR APROBAR";
        } else if (oc_situacion == 2) {
            oc_sitdes = "APROBADO";
        } else if (oc_situacion == 3) {
            oc_sitdes = "AT. PENDIENTE";
        } else if (oc_situacion == 4) {
            oc_sitdes = "ATENDIDO";
        } else if (oc_situacion == 5) {
            oc_sitdes = "AT. PARCIAL";
        } else if (oc_situacion == 6) {
            oc_sitdes = "CADUCADO";
        } else if (oc_situacion == 7) {
            oc_sitdes = "RECHAZADO";
        }
        return oc_sitdes;
    }

    public void setOc_sitdes(String oc_sitdes) {
        this.oc_sitdes = oc_sitdes;
    }

    public String getOc_almorides() {
        return oc_almorides;
    }

    public void setOc_almorides(String oc_almorides) {
        this.oc_almorides = oc_almorides;
    }

    public String getOc_almdesdes() {
        return oc_almdesdes;
    }

    public void setOc_almdesdes(String oc_almdesdes) {
        this.oc_almdesdes = oc_almdesdes;
    }

    public double getLp_descgen() {
        return lp_descgen;
    }

    public void setLp_descgen(double lp_descgen) {
        this.lp_descgen = lp_descgen;
    }

    public double getLp_descfinan() {
        return lp_descfinan;
    }

    public void setLp_descfinan(double lp_descfinan) {
        this.lp_descfinan = lp_descfinan;
    }

}
