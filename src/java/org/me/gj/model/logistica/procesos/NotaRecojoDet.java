package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class NotaRecojoDet {

    private long nr_key;
    private int emp_id;
    private int suc_id;
    private long nrd_item;
    private int nrd_tipdoc;
    private String nrd_doc;
    private String nrd_serie;
    private String nrd_docref;
    private int nrd_cantent;
    private int nrd_cantfrac;
    private int nrd_canttot;
    private int nrd_cantrec;
    private String pro_id;
    private long pro_key;
    private String nrd_glosa;
    private int nrd_est;
    private String nrd_usuadd;
    private Date nrd_fecadd;
    private String nrd_pcadd;
    private String nrd_usumod;
    private Date nrd_fecmod;
    private String nrd_pcmod;
    private String pro_des;
    private String pro_desdes;
    private String pro_unimancom;
    private int pro_presmincom;
    private String ind_accion = "Q";
    private String nrd_tipdocdes;

    public NotaRecojoDet() {
    }

    public NotaRecojoDet(long nr_key, int emp_id, int suc_id, long nrd_item, String nrd_usumod, String nrd_pcmod) {
        this.nr_key = nr_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nrd_item = nrd_item;
        this.nrd_usumod = nrd_usumod;
        this.nrd_pcmod = nrd_pcmod;
    }

    public NotaRecojoDet(long nr_key, int emp_id, int suc_id, long nrd_item, int nrd_tipdoc, String nrd_tipdocdes, String nrd_serie, String nrd_doc, String nrd_docref, int nrd_cantent, int nrd_cantfrac, int nrd_canttot, String pro_id, String pro_desdes, String pro_des, String pro_unimancom, int pro_presmincom, String nrd_glosa, String nrd_usuadd, String nrd_pcadd, String nrd_usumod, String nrd_pcmod) {
        this.nr_key = nr_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nrd_item = nrd_item;
        this.nrd_tipdoc = nrd_tipdoc;
        this.nrd_tipdocdes = nrd_tipdocdes;
        this.nrd_serie = nrd_serie;
        this.nrd_doc = nrd_doc;
        this.nrd_docref = nrd_docref;
        this.nrd_cantent = nrd_cantent;
        this.nrd_cantfrac = nrd_cantfrac;
        this.nrd_canttot = nrd_canttot;
        this.pro_id = pro_id;
        this.pro_desdes = pro_desdes;
        this.pro_des = pro_des;
        this.pro_unimancom = pro_unimancom;
        this.pro_presmincom = pro_presmincom;
        this.nrd_glosa = nrd_glosa;
        this.nrd_usuadd = nrd_usuadd;
        this.nrd_pcadd = nrd_pcadd;
        this.nrd_usumod = nrd_usumod;
        this.nrd_pcmod = nrd_pcmod;
    }

    public long getNr_key() {
        return nr_key;
    }

    public void setNr_key(long nr_key) {
        this.nr_key = nr_key;
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

    public long getNrd_item() {
        return nrd_item;
    }

    public void setNrd_item(long nrd_item) {
        this.nrd_item = nrd_item;
    }

    public int getNrd_tipdoc() {
        return nrd_tipdoc;
    }

    public void setNrd_tipdoc(int nrd_tipdoc) {
        this.nrd_tipdoc = nrd_tipdoc;
    }

    public String getNrd_doc() {
        return nrd_doc;
    }

    public void setNrd_doc(String nrd_doc) {
        this.nrd_doc = nrd_doc;
    }

    public int getNrd_cantent() {
        return nrd_cantent;
    }

    public void setNrd_cantent(int nrd_cantent) {
        this.nrd_cantent = nrd_cantent;
    }

    public int getNrd_cantfrac() {
        return nrd_cantfrac;
    }

    public void setNrd_cantfrac(int nrd_cantfrac) {
        this.nrd_cantfrac = nrd_cantfrac;
    }

    public int getNrd_canttot() {
        return nrd_canttot;
    }

    public void setNrd_canttot(int nrd_canttot) {
        this.nrd_canttot = nrd_canttot;
    }

    public int getNrd_cantrec() {
        return nrd_cantrec;
    }

    public void setNrd_cantrec(int nrd_cantrec) {
        this.nrd_cantrec = nrd_cantrec;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public String getNrd_glosa() {
        return nrd_glosa;
    }

    public void setNrd_glosa(String nrd_glosa) {
        this.nrd_glosa = nrd_glosa;
    }

    public int getNrd_est() {
        return nrd_est;
    }

    public void setNrd_est(int nrd_est) {
        this.nrd_est = nrd_est;
    }

    public String getNrd_usuadd() {
        return nrd_usuadd;
    }

    public void setNrd_usuadd(String nrd_usuadd) {
        this.nrd_usuadd = nrd_usuadd;
    }

    public Date getNrd_fecadd() {
        return nrd_fecadd;
    }

    public void setNrd_fecadd(Date nrd_fecadd) {
        this.nrd_fecadd = nrd_fecadd;
    }

    public String getNrd_pcadd() {
        return nrd_pcadd;
    }

    public void setNrd_pcadd(String nrd_pcadd) {
        this.nrd_pcadd = nrd_pcadd;
    }

    public String getNrd_usumod() {
        return nrd_usumod;
    }

    public void setNrd_usumod(String nrd_usumod) {
        this.nrd_usumod = nrd_usumod;
    }

    public Date getNrd_fecmod() {
        return nrd_fecmod;
    }

    public void setNrd_fecmod(Date nrd_fecmod) {
        this.nrd_fecmod = nrd_fecmod;
    }

    public String getNrd_pcmod() {
        return nrd_pcmod;
    }

    public void setNrd_pcmod(String nrd_pcmod) {
        this.nrd_pcmod = nrd_pcmod;
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

    public String getPro_unimancom() {
        return pro_unimancom;
    }

    public void setPro_unimancom(String pro_unimancom) {
        this.pro_unimancom = pro_unimancom;
    }

    public int getPro_presmincom() {
        return pro_presmincom;
    }

    public void setPro_presmincom(int pro_presmincom) {
        this.pro_presmincom = pro_presmincom;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public String getNrd_tipdocdes() {
        return nrd_tipdocdes;
    }

    public void setNrd_tipdocdes(String nrd_tipdocdes) {
        this.nrd_tipdocdes = nrd_tipdocdes;
    }

    public String getNrd_serie() {
        return nrd_serie;
    }

    public void setNrd_serie(String nrd_serie) {
        this.nrd_serie = nrd_serie;
    }

    public String getNrd_docref() {
        nrd_docref = nrd_serie == null ? "" : nrd_serie.trim() + nrd_doc;
        return nrd_docref;
    }

    public void setNrd_docref(String nrd_docref) {
        this.nrd_docref = nrd_docref;
    }
}
