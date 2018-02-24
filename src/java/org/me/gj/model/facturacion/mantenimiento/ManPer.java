package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class ManPer {

    private String per_id;
    private String per_periodo;
    private int per_estado;
    private String anio;
    private String mes;
    private String fecini;
    private String fecfin;
    private String usuadd;
    private Date fecadd;
    private String usumod;
    private Date fecmod;
    private boolean valor;

    public ManPer(String per_id, String per_periodo) {
        this.per_id = per_id;
        this.per_periodo = per_periodo;
    }

    public ManPer(String per_id, int per_estado, String anio, String mes, String fecini, String fecfin, String usuadd, Date fecadd, String usumod, Date fecmod) {
        this.per_id = per_id;
        this.per_estado = per_estado;
        this.anio = anio;
        this.mes = mes;
        this.fecini = fecini;
        this.fecfin = fecfin;
        this.usuadd = usuadd;
        this.fecadd = fecadd;
        this.usumod = usumod;
        this.fecmod = fecmod;
    }

    public ManPer() {
    }

    public String getPer_periodo() {
        return per_periodo;
    }

    public void setPer_periodo(String per_periodo) {
        this.per_periodo = per_periodo;
    }

    public boolean isValor() {
        if (per_estado == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public int getPer_estado() {
        return per_estado;
    }

    public void setPer_estado(int per_estado) {
        this.per_estado = per_estado;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getUsuadd() {
        return usuadd;
    }

    public void setUsuadd(String usuadd) {
        this.usuadd = usuadd;
    }

    public Date getFecadd() {
        return fecadd;
    }

    public void setFecadd(Date fecadd) {
        this.fecadd = fecadd;
    }

    public String getUsumod() {
        return usumod;
    }

    public void setUsumod(String usumod) {
        this.usumod = usumod;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

    public String getFecini() {
        return fecini;
    }

    public void setFecini(String fecini) {
        this.fecini = fecini;
    }

    public String getFecfin() {
        return fecfin;
    }

    public void setFecfin(String fecfin) {
        this.fecfin = fecfin;
    }

}
