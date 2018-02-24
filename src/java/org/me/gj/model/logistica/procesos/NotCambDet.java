package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class NotCambDet {

    private long nc_key;
    private int emp_id;
    private int suc_id;
    private long ncd_item;
    private int ncd_tipdoc;
    private String ncd_serie;
    private String ncd_doc;
    private String ncd_docref;
    private int ncd_cantent;
    private int ncd_cantfrac;
    private int ncd_canttot;
    private int ncd_cantmov;
    private int ncd_cantrec;
    private String pro_id;
    private long pro_key;
    private String ncd_glosa;
    private int ncd_est;
    private String ncd_usuadd;
    private Date ncd_fecadd;
    private String ncd_pcadd;
    private String ncd_usumod;
    private Date ncd_fecmod;
    private String ncd_pcmod;
    private String pro_desdes;
    private String pro_des;
    private String pro_uniman;
    private int pro_presmin;
    private String ind_accion = "Q";
    private String ncd_tipdocdes;

    public NotCambDet() {
    }

    public NotCambDet(long nc_key, int emp_id, int suc_id, long ncd_item, String ncd_usumod, String ncd_pcmod) {
        this.nc_key = nc_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.ncd_item = ncd_item;
        this.ncd_usumod = ncd_usumod;
        this.ncd_pcmod = ncd_pcmod;
    }

    public NotCambDet(long nc_key, int emp_id, int suc_id, int ncd_tipdoc, String ncd_tipdocdes, String ncd_serie, String ncd_doc, String ncd_docref, int ncd_cantent, int ncd_cantfrac, int ncd_canttot, String pro_id, long pro_key, String pro_desdes, String pro_des, String uniman, int presmin, String ncd_glosa, String ncd_usuadd, String ncd_pcadd, String ncd_usumod, String ncd_pcmod) {
        this.nc_key = nc_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.ncd_tipdoc = ncd_tipdoc;
        this.ncd_tipdocdes = ncd_tipdocdes;
        this.ncd_serie = ncd_serie;
        this.ncd_docref = ncd_docref;
        this.ncd_doc = ncd_doc;
        this.ncd_cantent = ncd_cantent;
        this.ncd_cantfrac = ncd_cantfrac;
        this.ncd_canttot = ncd_canttot;
        this.pro_id = pro_id;
        this.pro_key = pro_key;
        this.pro_desdes = pro_desdes;
        this.pro_des = pro_des;
        this.pro_uniman = uniman;
        this.pro_presmin = presmin;
        this.ncd_glosa = ncd_glosa;
        this.ncd_usuadd = ncd_usuadd;
        this.ncd_pcadd = ncd_pcadd;
        this.ncd_usumod = ncd_usumod;
        this.ncd_pcmod = ncd_pcmod;
    }

    public long getNc_key() {
        return nc_key;
    }

    public void setNc_key(long nc_key) {
        this.nc_key = nc_key;
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

    public long getNcd_item() {
        return ncd_item;
    }

    public void setNcd_item(long ncd_item) {
        this.ncd_item = ncd_item;
    }

    public int getNcd_tipdoc() {
        return ncd_tipdoc;
    }

    public void setNcd_tipdoc(int ncd_tipdoc) {
        this.ncd_tipdoc = ncd_tipdoc;
    }

    public String getNcd_doc() {
        return ncd_doc;
    }

    public void setNcd_doc(String ncd_doc) {
        this.ncd_doc = ncd_doc;
    }

    public int getNcd_cantent() {
        return ncd_cantent;
    }

    public void setNcd_cantent(int ncd_cantent) {
        this.ncd_cantent = ncd_cantent;
    }

    public int getNcd_cantfrac() {
        return ncd_cantfrac;
    }

    public void setNcd_cantfrac(int ncd_cantfrac) {
        this.ncd_cantfrac = ncd_cantfrac;
    }

    public int getNcd_canttot() {
        return ncd_canttot;
    }

    public void setNcd_canttot(int ncd_canttot) {
        this.ncd_canttot = ncd_canttot;
    }

    public int getNcd_cantmov() {
        return ncd_cantmov;
    }

    public void setNcd_cantmov(int ncd_cantmov) {
        this.ncd_cantmov = ncd_cantmov;
    }

    public int getNcd_cantrec() {
        return ncd_cantrec;
    }

    public void setNcd_cantrec(int ncd_cantrec) {
        this.ncd_cantrec = ncd_cantrec;
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

    public String getNcd_glosa() {
        return ncd_glosa;
    }

    public void setNcd_glosa(String ncd_glosa) {
        this.ncd_glosa = ncd_glosa;
    }

    public int getNcd_est() {
        return ncd_est;
    }

    public void setNcd_est(int ncd_est) {
        this.ncd_est = ncd_est;
    }

    public String getNcd_usuadd() {
        return ncd_usuadd;
    }

    public void setNcd_usuadd(String ncd_usuadd) {
        this.ncd_usuadd = ncd_usuadd;
    }

    public Date getNcd_fecadd() {
        return ncd_fecadd;
    }

    public void setNcd_fecadd(Date ncd_fecadd) {
        this.ncd_fecadd = ncd_fecadd;
    }

    public String getNcd_pcadd() {
        return ncd_pcadd;
    }

    public void setNcd_pcadd(String ncd_pcadd) {
        this.ncd_pcadd = ncd_pcadd;
    }

    public String getNcd_usumod() {
        return ncd_usumod;
    }

    public void setNcd_usumod(String ncd_usumod) {
        this.ncd_usumod = ncd_usumod;
    }

    public Date getNcd_fecmod() {
        return ncd_fecmod;
    }

    public void setNcd_fecmod(Date ncd_fecmod) {
        this.ncd_fecmod = ncd_fecmod;
    }

    public String getNcd_pcmod() {
        return ncd_pcmod;
    }

    public void setNcd_pcmod(String ncd_pcmod) {
        this.ncd_pcmod = ncd_pcmod;
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

    public String getPro_uniman() {
        return pro_uniman;
    }

    public void setPro_uniman(String pro_uniman) {
        this.pro_uniman = pro_uniman;
    }

    public int getPro_presmin() {
        return pro_presmin;
    }

    public void setPro_presmin(int pro_presmin) {
        this.pro_presmin = pro_presmin;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public String getNcd_tipdocdes() {
        return ncd_tipdocdes;
    }

    public void setNcd_tipdocdes(String ncd_tipdocdes) {
        this.ncd_tipdocdes = ncd_tipdocdes;
    }

    public String getNcd_serie() {
        return ncd_serie;
    }

    public void setNcd_serie(String ncd_serie) {
        this.ncd_serie = ncd_serie;
    }

    public String getNcd_docref() {
        ncd_docref = ncd_serie == null ? "" : ncd_serie.trim() + ncd_doc;
        return ncd_docref;
    }

    public void setNcd_docref(String ncd_docref) {
        this.ncd_docref = ncd_docref;
    }

}
