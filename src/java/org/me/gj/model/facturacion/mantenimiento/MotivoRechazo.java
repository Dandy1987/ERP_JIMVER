package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;
import org.zkoss.zul.ListModelList;

public class MotivoRechazo {

    private int mrec_key;
    private String mrec_id;
    private String mrec_des;
    private String mrec_tipdoc;
    private String mrec_tipo;
    private int mrec_est;
    private int mrec_ord;
    private String mrec_nomrep;
    private String mrec_usuadd;
    private Date mrec_fecadd;
    private String mrec_usumod;
    private Date mrec_fecmod;
    private String mrec_destipdoc;
    private String mrec_destipo;
    private boolean valor;

    public MotivoRechazo() {
    }

    public MotivoRechazo(String mrec_tipo, String mrec_destipo) {
        this.mrec_tipo = mrec_tipo;
        this.mrec_destipo = mrec_destipo;
    }

    public MotivoRechazo(int mrec_key, String mrec_id, String mrec_des, String mrec_tipdoc, String mrec_tipo, int mrec_est, int mrec_ord, String mrec_nomrep, String mrec_usuadd, String mrec_usumod) {
        this.mrec_key = mrec_key;
        this.mrec_id = mrec_id;
        this.mrec_des = mrec_des;
        this.mrec_tipdoc = mrec_tipdoc;
        this.mrec_tipo = mrec_tipo;
        this.mrec_est = mrec_est;
        this.mrec_ord = mrec_ord;
        this.mrec_nomrep = mrec_nomrep;
        this.mrec_usuadd = mrec_usuadd;
        this.mrec_usumod = mrec_usumod;
    }

    public ListModelList<MotivoRechazo> listaTipoMotRec(String tipo) {
        ListModelList<MotivoRechazo> objlstMotRec = new ListModelList<MotivoRechazo>();
        objlstMotRec.add(new MotivoRechazo("RT", "RECHAZO TOTAL"));
        objlstMotRec.add(new MotivoRechazo("RP", "RECHAZO PARCIAL"));
        if (tipo.equals("DV") || tipo.equals("PV")) {
            objlstMotRec.add(new MotivoRechazo("VT", "VENTA TOTAL"));
            objlstMotRec.add(new MotivoRechazo("VP", "VENTA PARCIAL"));
        }
        return objlstMotRec;
    }

    public int getMrec_key() {
        return mrec_key;
    }

    public void setMrec_key(int mrec_key) {
        this.mrec_key = mrec_key;
    }

    public String getMrec_id() {
        return mrec_id;
    }

    public void setMrec_id(String mrec_id) {
        this.mrec_id = mrec_id;
    }

    public String getMrec_des() {
        return mrec_des;
    }

    public void setMrec_des(String mrec_des) {
        this.mrec_des = mrec_des;
    }

    public String getMrec_tipdoc() {
        return mrec_tipdoc;
    }

    public void setMrec_tipdoc(String mrec_tipdoc) {
        this.mrec_tipdoc = mrec_tipdoc;
    }

    public String getMrec_tipo() {
        return mrec_tipo;
    }

    public void setMrec_tipo(String mrec_tipo) {
        this.mrec_tipo = mrec_tipo;
    }

    public int getMrec_est() {
        return mrec_est;
    }

    public void setMrec_est(int mrec_est) {
        this.mrec_est = mrec_est;
    }

    public int getMrec_ord() {
        return mrec_ord;
    }

    public void setMrec_ord(int mrec_ord) {
        this.mrec_ord = mrec_ord;
    }

    public String getMrec_nomrep() {
        return mrec_nomrep;
    }

    public void setMrec_nomrep(String mrec_nomrep) {
        this.mrec_nomrep = mrec_nomrep;
    }

    public String getMrec_usuadd() {
        return mrec_usuadd;
    }

    public void setMrec_usuadd(String mrec_usuadd) {
        this.mrec_usuadd = mrec_usuadd;
    }

    public Date getMrec_fecadd() {
        return mrec_fecadd;
    }

    public void setMrec_fecadd(Date mrec_fecadd) {
        this.mrec_fecadd = mrec_fecadd;
    }

    public String getMrec_usumod() {
        return mrec_usumod;
    }

    public void setMrec_usumod(String mrec_usumod) {
        this.mrec_usumod = mrec_usumod;
    }

    public Date getMrec_fecmod() {
        return mrec_fecmod;
    }

    public void setMrec_fecmod(Date mrec_fecmod) {
        this.mrec_fecmod = mrec_fecmod;
    }

    public boolean isValor() {
        valor = mrec_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getMrec_destipdoc() {
        mrec_destipdoc = mrec_tipdoc.equals("DV") ? "DOCUMENTO DE VENTA" : mrec_tipdoc.equals("PV") ? "PEDIDO DE VENTA" : "CAMBIO";
        return mrec_destipdoc;
    }

    public void setMrec_destipdoc(String mrec_destipdoc) {
        this.mrec_destipdoc = mrec_destipdoc;
    }

    public String getMrec_destipo() {
        if (mrec_tipo.equals("RT")) {
            mrec_destipo = "RECHAZO TOTAL";
        } else if (mrec_tipo.equals("RP")) {
            mrec_destipo = "RECHAZO PARCIAL";
        } else if (mrec_tipo.equals("VT")) {
            mrec_destipo = "VENTA TOTAL";
        } else {
            mrec_destipo = "VENTA PARCIAL";
        }

        return mrec_destipo;
    }

    public void setMrec_destipo(String mrec_destipo) {
        this.mrec_destipo = mrec_destipo;
    }
}
