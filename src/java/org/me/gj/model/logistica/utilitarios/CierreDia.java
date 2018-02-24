package org.me.gj.model.logistica.utilitarios;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CierreDia {

    private Date dia_fec;
    private String dias_fec;
    private int suc_id;
    private int emp_id;
    private int ciedia_estado;
    private int ciedia_log;
    private int ciedia_cxc;
    private int ciedia_dis;
    private int ciedia_fac;
    private int ciedia_cxp;
    private int ciedia_ban;
    private int ciedia_caj;
    private int ciedia_con;
    private int ciedia_pla;
    private int ciedia_seg;
    private int ciedia_fin;
    private int ciedia_fer;
    private int ciedia_ped;
    private int ciedia_labpresu;
    private int ciedia_labasis;
    private int ciedia_est;
    private String ciedia_usuadd;
    private Date ciedia_fecadd;
    private String ciedias_fecadd;
    private String ciedia_usumod;
    private Date ciedia_fecmod;
    private String ciedias_fecmod;
    private String ciedia_sitlog;
    private String ciedia_sitcxc;
    private String ciedia_sitfac;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CierreDia() {
    }

    public CierreDia(Date dia_fec, int suc_id, int emp_id, int ciedia_estado, int ciedia_log, int ciedia_cxc, int ciedia_dis, int ciedia_fac, int ciedia_cxp, int ciedia_ban, int ciedia_caj, int ciedia_con, int ciedia_pla, int ciedia_seg, int ciedia_fin, int ciedia_fer, int ciedia_ped, int ciedia_labpresu, int ciedia_labasis, int ciedia_est, String ciedia_usuadd, Date ciedia_fecadd, String ciedia_usumod, Date ciedia_fecmod) {
        this.dia_fec = dia_fec;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.ciedia_estado = ciedia_estado;
        this.ciedia_log = ciedia_log;
        this.ciedia_cxc = ciedia_cxc;
        this.ciedia_dis = ciedia_dis;
        this.ciedia_fac = ciedia_fac;
        this.ciedia_cxp = ciedia_cxp;
        this.ciedia_ban = ciedia_ban;
        this.ciedia_caj = ciedia_caj;
        this.ciedia_con = ciedia_con;
        this.ciedia_pla = ciedia_pla;
        this.ciedia_seg = ciedia_seg;
        this.ciedia_fin = ciedia_fin;
        this.ciedia_fer = ciedia_fer;
        this.ciedia_ped = ciedia_ped;
        this.ciedia_labpresu = ciedia_labpresu;
        this.ciedia_labasis = ciedia_labasis;
        this.ciedia_est = ciedia_est;
        this.ciedia_usuadd = ciedia_usuadd;
        this.ciedia_fecadd = ciedia_fecadd;
        this.ciedia_usumod = ciedia_usumod;
        this.ciedia_fecmod = ciedia_fecmod;
    }

    public Date getDia_fec() {
        return dia_fec;
    }

    public void setDia_fec(Date dia_fec) {
        this.dia_fec = dia_fec;
    }

    public String getDias_fec() {
        dias_fec = sdf.format(dia_fec);
        return dias_fec;
    }

    public void setDias_fec(String dias_fec) {
        this.dias_fec = dias_fec;
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

    public int getCiedia_estado() {
        return ciedia_estado;
    }

    public void setCiedia_estado(int ciedia_estado) {
        this.ciedia_estado = ciedia_estado;
    }

    public int getCiedia_log() {
        return ciedia_log;
    }

    public void setCiedia_log(int ciedia_log) {
        this.ciedia_log = ciedia_log;
    }

    public void setCiedia_sitcxc(String ciedia_sitcxc) {
        this.ciedia_sitcxc = ciedia_sitcxc;
    }

    public String getCiedia_sitlog() {
        ciedia_sitlog = ciedia_log == 1 ? "ABIERTO" : "CERRADO";
        return ciedia_sitlog;
    }

    public String getCiedia_sitfac() {
        ciedia_sitfac = ciedia_fac == 1 ? "ABIERTO" : "CERRADO";
        return ciedia_sitfac;
    }

    public String getCiedia_sitcxc() {
        ciedia_sitcxc = ciedia_cxc == 1 ? "ABIERTO" : "CERRADO";
        return ciedia_sitcxc;
    }

    public int getCiedia_cxc() {
        return ciedia_cxc;
    }

    public void setCiedia_cxc(int ciedia_cxc) {
        this.ciedia_cxc = ciedia_cxc;
    }

    public int getCiedia_dis() {
        return ciedia_dis;
    }

    public void setCiedia_dis(int ciedia_dis) {
        this.ciedia_dis = ciedia_dis;
    }

    public int getCiedia_fac() {
        return ciedia_fac;
    }

    public void setCiedia_fac(int ciedia_fac) {
        this.ciedia_fac = ciedia_fac;
    }

    public int getCiedia_cxp() {
        return ciedia_cxp;
    }

    public void setCiedia_cxp(int ciedia_cxp) {
        this.ciedia_cxp = ciedia_cxp;
    }

    public int getCiedia_ban() {
        return ciedia_ban;
    }

    public void setCiedia_ban(int ciedia_ban) {
        this.ciedia_ban = ciedia_ban;
    }

    public int getCiedia_caj() {
        return ciedia_caj;
    }

    public void setCiedia_caj(int ciedia_caj) {
        this.ciedia_caj = ciedia_caj;
    }

    public int getCiedia_con() {
        return ciedia_con;
    }

    public void setCiedia_con(int ciedia_con) {
        this.ciedia_con = ciedia_con;
    }

    public int getCiedia_pla() {
        return ciedia_pla;
    }

    public void setCiedia_pla(int ciedia_pla) {
        this.ciedia_pla = ciedia_pla;
    }

    public int getCiedia_seg() {
        return ciedia_seg;
    }

    public void setCiedia_seg(int ciedia_seg) {
        this.ciedia_seg = ciedia_seg;
    }

    public int getCiedia_fin() {
        return ciedia_fin;
    }

    public void setCiedia_fin(int ciedia_fin) {
        this.ciedia_fin = ciedia_fin;
    }

    public int getCiedia_fer() {
        return ciedia_fer;
    }

    public void setCiedia_fer(int ciedia_fer) {
        this.ciedia_fer = ciedia_fer;
    }

    public int getCiedia_ped() {
        return ciedia_ped;
    }

    public void setCiedia_ped(int ciedia_ped) {
        this.ciedia_ped = ciedia_ped;
    }

    public int getCiedia_labpresu() {
        return ciedia_labpresu;
    }

    public void setCiedia_labpresu(int ciedia_labpresu) {
        this.ciedia_labpresu = ciedia_labpresu;
    }

    public int getCiedia_labasis() {
        return ciedia_labasis;
    }

    public void setCiedia_labasis(int ciedia_labasis) {
        this.ciedia_labasis = ciedia_labasis;
    }

    public int getCiedia_est() {
        return ciedia_est;
    }

    public void setCiedia_est(int ciedia_est) {
        this.ciedia_est = ciedia_est;
    }

    public String getCiedia_usuadd() {
        return ciedia_usuadd;
    }

    public void setCiedia_usuadd(String ciedia_usuadd) {
        this.ciedia_usuadd = ciedia_usuadd;
    }

    public Date getCiedia_fecadd() {
        return ciedia_fecadd;
    }

    public void setCiedia_fecadd(Date ciedia_fecadd) {
        this.ciedia_fecadd = ciedia_fecadd;
    }

    public String getCiedias_fecadd() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        ciedias_fecadd = sdf.format(ciedia_fecadd);
        return ciedias_fecadd;
    }

    public void setCiedias_fecadd(String ciedias_fecadd) {
        this.ciedias_fecadd = ciedias_fecadd;
    }

    public String getCiedia_usumod() {
        return ciedia_usumod;
    }

    public void setCiedia_usumod(String ciedia_usumod) {
        this.ciedia_usumod = ciedia_usumod;
    }

    public Date getCiedia_fecmod() {
        return ciedia_fecmod;
    }

    public void setCiedia_fecmod(Date ciedia_fecmod) {
        this.ciedia_fecmod = ciedia_fecmod;
    }

    public String getCiedias_fecmod() {
        if (ciedia_fecmod != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            ciedias_fecmod = sdf.format(ciedia_fecmod);
        } else {
            ciedias_fecmod = "";
        }
        return ciedias_fecmod;
    }

    public void setCiedias_fecmod(String ciedias_fecmod) {
        this.ciedias_fecmod = ciedias_fecmod;
    }

}
