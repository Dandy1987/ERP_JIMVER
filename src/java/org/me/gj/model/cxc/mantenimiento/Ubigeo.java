package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class Ubigeo {

    private String ubi_id;
    private String ubi_nomdis;
    private String ubi_nompro;
    private String ubi_nomdep;
    private int ubi_est;
    private String ubi_usuadd;
    private Date ubi_fecadd;
    private String ubi_usumod;
    private Date ubi_fecmod;
    private boolean valor;
    private String ubi_cod;

    public Ubigeo(String ubi_id, String ubi_nomdis, String ubi_nompro, String ubi_nomdep, int ubi_est) {
        this.ubi_id = ubi_id;
        this.ubi_nomdis = ubi_nomdis;
        this.ubi_nompro = ubi_nompro;
        this.ubi_nomdep = ubi_nomdep;
        this.ubi_est = ubi_est;
    }

    public Ubigeo() {
    }

    public String getUbi_id() {
        return ubi_id;
    }

    public void setUbi_id(String ubi_id) {
        this.ubi_id = ubi_id;
    }

    public String getUbi_nomdis() {
        return ubi_nomdis;
    }

    public void setUbi_nomdis(String ubi_nomdis) {
        this.ubi_nomdis = ubi_nomdis;
    }

    public String getUbi_nompro() {
        return ubi_nompro;
    }

    public void setUbi_nompro(String ubi_nompro) {
        this.ubi_nompro = ubi_nompro;
    }

    public String getUbi_nomdep() {
        return ubi_nomdep;
    }

    public void setUbi_nomdep(String ubi_nomdep) {
        this.ubi_nomdep = ubi_nomdep;
    }

    public int getUbi_est() {
        return ubi_est;
    }

    public void setUbi_est(int ubi_est) {
        this.ubi_est = ubi_est;
    }

    public boolean isValor() {
        if (ubi_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getUbi_cod() {
        return ubi_cod;
    }

    public void setUbi_cod(String ubi_cod) {
        this.ubi_cod = ubi_cod;
    }

    public String getUbi_usuadd() {
        return ubi_usuadd;
    }

    public void setUbi_usuadd(String ubi_usuadd) {
        this.ubi_usuadd = ubi_usuadd;
    }

    public Date getUbi_fecadd() {
        return ubi_fecadd;
    }

    public void setUbi_fecadd(Date ubi_fecadd) {
        this.ubi_fecadd = ubi_fecadd;
    }

    public String getUbi_usumod() {
        return ubi_usumod;
    }

    public void setUbi_usumod(String ubi_usumod) {
        this.ubi_usumod = ubi_usumod;
    }

    public Date getUbi_fecmod() {
        return ubi_fecmod;
    }

    public void setUbi_fecmod(Date ubi_fecmod) {
        this.ubi_fecmod = ubi_fecmod;
    }

}
