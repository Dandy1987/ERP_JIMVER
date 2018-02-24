package org.me.gj.model.distribucion.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Propietario {

    private int prop_id;
    private String prop_apepat;
    private String prop_apemat;
    private String prop_nom;
    private String prop_razsoc;
    private Long prop_ruc;
    private String prop_dni;
    private Date prop_fecnac;
    private Long prop_telef;
    private String prop_direcc;
    private int prop_est;
    private String prop_usuadd;
    private Date prop_fecadd;
    private String prop_usumod;
    private Date prop_fecmod;
    private boolean valor;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Propietario(int prop_id, String prop_apepat, String prop_apemat, String prop_nom, String prop_razsoc, Long prop_ruc, String prop_dni, Date prop_fecnac, Long prop_telef, String prop_direcc, int prop_est, String prop_usuadd, String prop_usumod) {
        this.prop_id = prop_id;
        this.prop_apepat = prop_apepat;
        this.prop_apemat = prop_apemat;
        this.prop_nom = prop_nom;
        this.prop_razsoc = prop_razsoc;
        this.prop_ruc = prop_ruc;
        this.prop_dni = prop_dni;
        this.prop_fecnac = prop_fecnac;
        this.prop_telef = prop_telef;
        this.prop_direcc = prop_direcc;
        this.prop_est = prop_est;
        this.prop_usuadd = prop_usuadd;
        this.prop_usumod = prop_usumod;
    }

    public Propietario() {
    }

    public int getProp_id() {
        return prop_id;
    }

    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }

    public String getProp_apepat() {
        return prop_apepat;
    }

    public void setProp_apepat(String prop_apepat) {
        this.prop_apepat = prop_apepat;
    }

    public String getProp_apemat() {
        return prop_apemat;
    }

    public void setProp_apemat(String prop_apemat) {
        this.prop_apemat = prop_apemat;
    }

    public String getProp_nom() {
        return prop_nom;
    }

    public void setProp_nom(String prop_nom) {
        this.prop_nom = prop_nom;
    }

    public String getProp_razsoc() {
        return prop_razsoc;
    }

    public void setProp_razsoc(String prop_razsoc) {
        this.prop_razsoc = prop_razsoc;
    }

    public Long getProp_ruc() {
        return prop_ruc;
    }

    public void setProp_ruc(Long prop_ruc) {
        this.prop_ruc = prop_ruc;
    }

    public String getProp_dni() {
        return prop_dni;
    }

    public void setProp_dni(String prop_dni) {
        this.prop_dni = prop_dni;
    }

    public Date getProp_fecnac() {
        return prop_fecnac;
    }

    public void setProp_fecnac(Date prop_fecnac) {
        this.prop_fecnac = prop_fecnac;
    }

    public Long getProp_telef() {
        return prop_telef;
    }

    public void setProp_telef(Long prop_telef) {
        this.prop_telef = prop_telef;
    }

    public String getProp_direcc() {
        return prop_direcc;
    }

    public void setProp_direcc(String prop_direcc) {
        this.prop_direcc = prop_direcc;
    }

    public int getProp_est() {
        return prop_est;
    }

    public void setProp_est(int prop_est) {
        this.prop_est = prop_est;
    }

    public boolean isValor() {
        if (prop_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getProp_usuadd() {
        return prop_usuadd;
    }

    public Date getProp_fecadd() {
        return prop_fecadd;
    }

    public String getProp_usumod() {
        return prop_usumod;
    }

    public Date getProp_fecmod() {
        return prop_fecmod;
    }

    public void setProp_usuadd(String prop_usuadd) {
        this.prop_usuadd = prop_usuadd;
    }

    public void setProp_fecadd(Date prop_fecadd) {
        this.prop_fecadd = prop_fecadd;
    }

    public void setProp_usumod(String prop_usumod) {
        this.prop_usumod = prop_usumod;
    }

    public void setProp_fecmod(Date prop_fecmod) {
        this.prop_fecmod = prop_fecmod;
    }
}
