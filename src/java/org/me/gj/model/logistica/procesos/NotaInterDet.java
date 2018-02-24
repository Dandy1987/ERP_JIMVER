package org.me.gj.model.logistica.procesos;

import java.util.Date;

public class NotaInterDet {

    private long ni_key;
    private int emp_id;
    private int suc_id;
    private long nid_item;
    private int nid_tipdoc;
    private String nid_serie;
    private String nid_doc;
    private String nid_docref;
    private int nid_est;
    private String nid_proident;
    private int nid_cantente;
    private int nid_cantentf;
    private int nid_cantenttot;
    private String nid_proidsal;
    private int nid_cantsale;
    private int nid_cantsalf;
    private int nid_cantsaltot;
    private int nid_cantmovent;
    private int nid_cantmovsal;
    private int nid_cantrec;
    private String nid_glosa;
    private String nid_usuadd;
    private Date nid_fecadd;
    private String nid_pcadd;
    private String nid_usumod;
    private Date nid_fecmod;
    private String nid_pcmod;
    private String pro_dessal;
    private String pro_desdessal;
    private String pro_desent;
    private String pro_desdesent;
    private String pro_unimansal;
    private String pro_unimanent;
    private int pro_presminsal;
    private int pro_presminent;
    private String ind_accion = "Q";
    private String pro_id;
    private String pro_des;
    private String pro_desdes;
    private int pro_presmin;
    private String pro_uniman;
    private int nid_cantent;
    private int nid_cantfrac;
    private int nid_canttot;
    private String nid_indicador;
    private String nid_tipdocdes;
    private double nid_precio;
    private int nid_lista;

    public NotaInterDet() {
    }

    public NotaInterDet(long ni_key, int emp_id, int suc_id, long nid_item, String nid_usumod, String nid_pcmod) {
        this.ni_key = ni_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nid_item = nid_item;
        this.nid_usumod = nid_usumod;
        this.nid_pcmod = nid_pcmod;
    }

    public NotaInterDet(long ni_key, int emp_id, int suc_id, long nid_item, int nid_tipdoc, String nid_serie, String nid_doc, String nid_docref, String nid_proident, int nid_cantente, int nid_cantentf, int nid_cantenttot, String nid_proidsal, int nid_cantsale, int nid_cantsalf, int nid_cantsaltot, String nid_glosa, String nid_usuadd, String nid_pcadd, String nid_usumod, String nid_pcmod, String pro_dessal, String pro_desdessal, String pro_desent, String pro_desdesent, String pro_unimansal, String pro_unimanent, int pro_presminsal, int pro_presminent) {
        this.ni_key = ni_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nid_item = nid_item;
        this.nid_tipdoc = nid_tipdoc;
        this.nid_serie = nid_serie;
        this.nid_doc = nid_doc;
        this.nid_docref = nid_docref;
        this.nid_proident = nid_proident;
        this.nid_cantente = nid_cantente;
        this.nid_cantentf = nid_cantentf;
        this.nid_cantenttot = nid_cantenttot;
        this.nid_proidsal = nid_proidsal;
        this.nid_cantsale = nid_cantsale;
        this.nid_cantsalf = nid_cantsalf;
        this.nid_cantsaltot = nid_cantsaltot;
        this.nid_glosa = nid_glosa;
        this.nid_usuadd = nid_usuadd;
        this.nid_pcadd = nid_pcadd;
        this.nid_usumod = nid_usumod;
        this.nid_pcmod = nid_pcmod;
        this.pro_dessal = pro_dessal;
        this.pro_desdessal = pro_desdessal;
        this.pro_desent = pro_desent;
        this.pro_desdesent = pro_desdesent;
        this.pro_unimansal = pro_unimansal;
        this.pro_unimanent = pro_unimanent;
        this.pro_presminsal = pro_presminsal;
        this.pro_presminent = pro_presminent;
    }

    public NotaInterDet(long ni_key, int emp_id, int suc_id, int nid_tipdoc, String nid_tipdocdes, String nid_serie, String nid_doc, String nid_docref, String nid_glosa, String nid_usuadd, String nid_pcadd, String nid_usumod, String nid_pcmod, String pro_id, String pro_des, String pro_desdes, int pro_presmin, String pro_uniman, int nid_cantent, int nid_cantfrac, int nid_canttot, String nid_indicador, double nid_precio) {
        this.ni_key = ni_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nid_tipdoc = nid_tipdoc;
        this.nid_tipdocdes = nid_tipdocdes;
        this.nid_serie = nid_serie;
        this.nid_doc = nid_doc;
        this.nid_docref = nid_docref;
        this.nid_glosa = nid_glosa;
        this.nid_usuadd = nid_usuadd;
        this.nid_pcadd = nid_pcadd;
        this.nid_usumod = nid_usumod;
        this.nid_pcmod = nid_pcmod;
        this.pro_id = pro_id;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.pro_presmin = pro_presmin;
        this.pro_uniman = pro_uniman;
        this.nid_cantent = nid_cantent;
        this.nid_cantfrac = nid_cantfrac;
        this.nid_canttot = nid_canttot;
        this.nid_indicador = nid_indicador;
        this.nid_precio = nid_precio;
    }

    public double getNid_precio() {
        return nid_precio;
    }

    public void setNid_precio(double nid_precio) {
        this.nid_precio = nid_precio;
    }

    public int getNid_lista() {
        return nid_lista;
    }

    public void setNid_lista(int nid_lista) {
        this.nid_lista = nid_lista;
    }

    public long getNi_key() {
        return ni_key;
    }

    public void setNi_key(long ni_key) {
        this.ni_key = ni_key;
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

    public long getNid_item() {
        return nid_item;
    }

    public void setNid_item(long nid_item) {
        this.nid_item = nid_item;
    }

    public int getNid_tipdoc() {
        return nid_tipdoc;
    }

    public void setNid_tipdoc(int nid_tipdoc) {
        this.nid_tipdoc = nid_tipdoc;
    }

    public String getNid_doc() {
        return nid_doc;
    }

    public void setNid_doc(String nid_doc) {
        this.nid_doc = nid_doc;
    }

    public int getNid_est() {
        return nid_est;
    }

    public void setNid_est(int nid_est) {
        this.nid_est = nid_est;
    }

    public String getNid_proident() {
        return nid_proident;
    }

    public void setNid_proident(String nid_proident) {
        this.nid_proident = nid_proident;
    }

    public int getNid_cantente() {
        return nid_cantente;
    }

    public void setNid_cantente(int nid_cantente) {
        this.nid_cantente = nid_cantente;
    }

    public int getNid_cantentf() {
        return nid_cantentf;
    }

    public void setNid_cantentf(int nid_cantentf) {
        this.nid_cantentf = nid_cantentf;
    }

    public int getNid_cantenttot() {
        return nid_cantenttot;
    }

    public void setNid_cantenttot(int nid_cantenttot) {
        this.nid_cantenttot = nid_cantenttot;
    }

    public String getNid_proidsal() {
        return nid_proidsal;
    }

    public void setNid_proidsal(String nid_proidsal) {
        this.nid_proidsal = nid_proidsal;
    }

    public int getNid_cantsale() {
        return nid_cantsale;
    }

    public void setNid_cantsale(int nid_cantsale) {
        this.nid_cantsale = nid_cantsale;
    }

    public int getNid_cantsalf() {
        return nid_cantsalf;
    }

    public void setNid_cantsalf(int nid_cantsalf) {
        this.nid_cantsalf = nid_cantsalf;
    }

    public int getNid_cantmovent() {
        return nid_cantmovent;
    }

    public void setNid_cantmovent(int nid_cantmovent) {
        this.nid_cantmovent = nid_cantmovent;
    }

    public int getNid_cantmovsal() {
        return nid_cantmovsal;
    }

    public void setNid_cantmovsal(int nid_cantmovsal) {
        this.nid_cantmovsal = nid_cantmovsal;
    }

    public int getNid_cantsaltot() {
        return nid_cantsaltot;
    }

    public void setNid_cantsaltot(int nid_cantsaltot) {
        this.nid_cantsaltot = nid_cantsaltot;
    }

    public int getNid_cantrec() {
        return nid_cantrec;
    }

    public void setNid_cantrec(int nid_cantrec) {
        this.nid_cantrec = nid_cantrec;
    }

    public String getNid_glosa() {
        return nid_glosa;
    }

    public void setNid_glosa(String nid_glosa) {
        this.nid_glosa = nid_glosa;
    }

    public String getNid_usuadd() {
        return nid_usuadd;
    }

    public void setNid_usuadd(String nid_usuadd) {
        this.nid_usuadd = nid_usuadd;
    }

    public Date getNid_fecadd() {
        return nid_fecadd;
    }

    public void setNid_fecadd(Date nid_fecadd) {
        this.nid_fecadd = nid_fecadd;
    }

    public String getNid_pcadd() {
        return nid_pcadd;
    }

    public void setNid_pcadd(String nid_pcadd) {
        this.nid_pcadd = nid_pcadd;
    }

    public String getNid_usumod() {
        return nid_usumod;
    }

    public void setNid_usumod(String nid_usumod) {
        this.nid_usumod = nid_usumod;
    }

    public Date getNid_fecmod() {
        return nid_fecmod;
    }

    public void setNid_fecmod(Date nid_fecmod) {
        this.nid_fecmod = nid_fecmod;
    }

    public String getNid_pcmod() {
        return nid_pcmod;
    }

    public void setNid_pcmod(String nid_pcmod) {
        this.nid_pcmod = nid_pcmod;
    }

    public String getPro_dessal() {
        return pro_dessal;
    }

    public void setPro_dessal(String pro_dessal) {
        this.pro_dessal = pro_dessal;
    }

    public String getPro_desdessal() {
        return pro_desdessal;
    }

    public void setPro_desdessal(String pro_desdessal) {
        this.pro_desdessal = pro_desdessal;
    }

    public String getPro_desent() {
        return pro_desent;
    }

    public void setPro_desent(String pro_desent) {
        this.pro_desent = pro_desent;
    }

    public String getPro_desdesent() {
        return pro_desdesent;
    }

    public void setPro_desdesent(String pro_desdesent) {
        this.pro_desdesent = pro_desdesent;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public String getPro_unimansal() {
        return pro_unimansal;
    }

    public void setPro_unimansal(String pro_unimansal) {
        this.pro_unimansal = pro_unimansal;
    }

    public String getPro_unimanent() {
        return pro_unimanent;
    }

    public void setPro_unimanent(String pro_unimanent) {
        this.pro_unimanent = pro_unimanent;
    }

    public int getPro_presminsal() {
        return pro_presminsal;
    }

    public void setPro_presminsal(int pro_presminsal) {
        this.pro_presminsal = pro_presminsal;
    }

    public int getPro_presminent() {
        return pro_presminent;
    }

    public void setPro_presminent(int pro_presminent) {
        this.pro_presminent = pro_presminent;
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

    public int getPro_presmin() {
        return pro_presmin;
    }

    public void setPro_presmin(int pro_presmin) {
        this.pro_presmin = pro_presmin;
    }

    public String getPro_uniman() {
        return pro_uniman;
    }

    public void setPro_uniman(String pro_uniman) {
        this.pro_uniman = pro_uniman;
    }

    public int getNid_cantent() {
        return nid_cantent;
    }

    public void setNid_cantent(int nid_cantent) {
        this.nid_cantent = nid_cantent;
    }

    public int getNid_cantfrac() {
        return nid_cantfrac;
    }

    public void setNid_cantfrac(int nid_cantfrac) {
        this.nid_cantfrac = nid_cantfrac;
    }

    public int getNid_canttot() {
        return nid_canttot;
    }

    public void setNid_canttot(int nid_canttot) {
        this.nid_canttot = nid_canttot;
    }

    public String getNid_indicador() {
        return nid_indicador;
    }

    public void setNid_indicador(String nid_indicador) {
        this.nid_indicador = nid_indicador;
    }

    public String getNid_tipdocdes() {
        return nid_tipdocdes;
    }

    public void setNid_tipdocdes(String nid_tipdocdes) {
        this.nid_tipdocdes = nid_tipdocdes;
    }

    public String getNid_serie() {
        return nid_serie;
    }

    public void setNid_serie(String nid_serie) {
        this.nid_serie = nid_serie;
    }

    public String getNid_docref() {
        nid_docref = nid_serie == null ? "" : nid_serie.trim() + nid_doc;
        return nid_docref;
    }

    public void setNid_docref(String nid_docref) {
        this.nid_docref = nid_docref;
    }
}
