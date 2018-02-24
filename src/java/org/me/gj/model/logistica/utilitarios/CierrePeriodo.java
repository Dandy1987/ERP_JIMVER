package org.me.gj.model.logistica.utilitarios;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CierrePeriodo {

    private String mes;
    private String per_id;
    private int per_key;
    private int suc_id;
    private int emp_id;
    private int cieper_estado;
    private int cieper_log;
    private int cieper_cxc;
    private int cieper_dis;
    private int cieper_fac;
    private int cieper_cxp;
    private int cieper_ban;
    private int cieper_caj;
    private int cieper_con;
    private int cieper_pla;
    private int cieper_seg;
    private int cieper_fin;
    private int cieper_est;
    private String cieper_usuadd;
    private Date cieper_fecadd;
    private String cieper_sfecadd;
    private String cieper_usumod;
    private Date cieper_fecmod;
    private String cieper_sfecmod;
    private String cieper_sitlog;
    private String cieper_sitfac;
    private String cieper_sitcxc;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CierrePeriodo() {
    }

    public CierrePeriodo(String mes, int suc_id, int emp_id, int cieper_estado, int cieper_log, int cieper_cxc, int cieper_dis, int cieper_fac, int cieper_cxp, int cieper_ban, int cieper_caj, int cieper_con, int cieper_pla, int cieper_seg, int cieper_fin, int cieper_est, String cieper_usuadd, Date cieper_fecadd, String cieper_usumod, Date cieper_fecmod) {
        this.mes = mes;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.cieper_estado = cieper_estado;
        this.cieper_log = cieper_log;
        this.cieper_cxc = cieper_cxc;
        this.cieper_dis = cieper_dis;
        this.cieper_fac = cieper_fac;
        this.cieper_cxp = cieper_cxp;
        this.cieper_ban = cieper_ban;
        this.cieper_caj = cieper_caj;
        this.cieper_con = cieper_con;
        this.cieper_pla = cieper_pla;
        this.cieper_seg = cieper_seg;
        this.cieper_fin = cieper_fin;
        this.cieper_est = cieper_est;
        this.cieper_usuadd = cieper_usuadd;
        this.cieper_fecadd = cieper_fecadd;
        this.cieper_usumod = cieper_usumod;
        this.cieper_fecmod = cieper_fecmod;
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getCieper_estado() {
        return cieper_estado;
    }

    public void setCieper_estado(int cieper_estado) {
        this.cieper_estado = cieper_estado;
    }

    public int getCieper_log() {
        return cieper_log;
    }

    public String getCieper_sitlog() {
        cieper_sitlog = cieper_log == 1 ? "ABIERTO" : "CERRADO";
        return cieper_sitlog;
    }

    public String getCieper_sitfac() {
        cieper_sitfac = cieper_fac == 1 ? "ABIERTO" : "CERRADO";
        return cieper_sitfac;
    }

    public int getCieper_cxc() {
        return cieper_cxc;
    }

    public String getCieper_sitcxc() {
        cieper_sitcxc = cieper_cxc == 1 ? "ABIERTO" : "CERRADO";
        return cieper_sitcxc;
    }

    public void setCieper_sitcxc(String cieper_sitcxc) {
        this.cieper_sitcxc = cieper_sitcxc;
    }

    public void setCieper_log(int cieper_log) {
        this.cieper_log = cieper_log;
    }

    public void setCieper_cxc(int cieper_cxc) {
        this.cieper_cxc = cieper_cxc;
    }

    public int getCieper_dis() {
        return cieper_dis;
    }

    public void setCieper_dis(int cieper_dis) {
        this.cieper_dis = cieper_dis;
    }

    public int getCieper_fac() {
        return cieper_fac;
    }

    public void setCieper_fac(int cieper_fac) {
        this.cieper_fac = cieper_fac;
    }

    public int getCieper_cxp() {
        return cieper_cxp;
    }

    public void setCieper_cxp(int cieper_cxp) {
        this.cieper_cxp = cieper_cxp;
    }

    public int getCieper_ban() {
        return cieper_ban;
    }

    public void setCieper_ban(int cieper_ban) {
        this.cieper_ban = cieper_ban;
    }

    public int getCieper_caj() {
        return cieper_caj;
    }

    public void setCieper_caj(int cieper_caj) {
        this.cieper_caj = cieper_caj;
    }

    public int getCieper_con() {
        return cieper_con;
    }

    public void setCieper_con(int cieper_con) {
        this.cieper_con = cieper_con;
    }

    public int getCieper_pla() {
        return cieper_pla;
    }

    public void setCieper_pla(int cieper_pla) {
        this.cieper_pla = cieper_pla;
    }

    public int getCieper_seg() {
        return cieper_seg;
    }

    public void setCieper_seg(int cieper_seg) {
        this.cieper_seg = cieper_seg;
    }

    public int getCieper_fin() {
        return cieper_fin;
    }

    public void setCieper_fin(int cieper_fin) {
        this.cieper_fin = cieper_fin;
    }

    public int getCieper_est() {
        return cieper_est;
    }

    public void setCieper_est(int cieper_est) {
        this.cieper_est = cieper_est;
    }

    public String getCieper_usuadd() {
        return cieper_usuadd;
    }

    public void setCieper_usuadd(String cieper_usuadd) {
        this.cieper_usuadd = cieper_usuadd;
    }

    public Date getCieper_fecadd() {
        return cieper_fecadd;
    }

    public void setCieper_fecadd(Date cieper_fecadd) {
        this.cieper_fecadd = cieper_fecadd;
    }

    public String getCieper_usumod() {
        return cieper_usumod;
    }

    public void setCieper_usumod(String cieper_usumod) {
        this.cieper_usumod = cieper_usumod;
    }

    public Date getCieper_fecmod() {
        return cieper_fecmod;
    }

    public void setCieper_fecmod(Date cieper_fecmod) {
        this.cieper_fecmod = cieper_fecmod;
    }

    public String getCieper_sfecadd() {
        SimpleDateFormat sdfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        cieper_sfecadd = sdfm.format(cieper_fecadd);
        return cieper_sfecadd;
    }

    public String getCieper_sfecmod() {
        if (cieper_fecmod != null) {
            SimpleDateFormat sdfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            cieper_sfecmod = sdfm.format(cieper_fecmod);
        } else {
            cieper_sfecmod = "";
        }
        return cieper_sfecmod;
    }

}
