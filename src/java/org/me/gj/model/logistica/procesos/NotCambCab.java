package org.me.gj.model.logistica.procesos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotCambCab {

    private long nc_key;
    private int emp_id;
    private int suc_id;
    private String nc_id;
    private String cli_id;
    private long cli_key;
    private long clidir_id;
    private Date nc_fecemi;
    private Date nc_fecent;
    private int nc_periodo;
    private int nc_sit;
    private int nc_motrec;
    private int nc_est;
    private String nc_zona;
    private int nc_motcam;
    private int nc_sup;
    private int nc_vend;
    private int nc_trans;
    private int nc_hor;
    private String nc_tipnotaent;
    private String nc_notaent;
    private String nc_tipnotasal;
    private String nc_notasal;
    private String nc_nroreg;
    private String nc_nrodep;
    private String nc_autusuadd;
    private Date nc_autfecadd;
    private String nc_autpcadd;
    private String nc_usuadd;
    private Date nc_fecadd;
    private String nc_pcadd;
    private String nc_usumod;
    private Date nc_fecmod;
    private String nc_pcmod;
    private String cli_razsoc;
    private String clidir_direc;
    private String nc_sitdes;
    private String mrec_des;
    private String zon_des;
    private String mcam_des;
    private String ven_id;
    private String ven_nom;
    private String trans_alias;
    private String hor_des;
    private String nc_sfecemi;
    private String nc_sfecent;
    private boolean valSelec;

    public NotCambCab() {
    }

    public NotCambCab(long nc_key, int emp_id, int suc_id, String cli_id, long cli_key, long clidir_id, Date nc_fecemi, Date nc_fecent, String nc_zona, int nc_motcam, int nc_sup, int nc_vend, int nc_trans, int nc_hor, String nc_usuadd, String nc_pcadd, String nc_usumod, String nc_pcmod) {
        this.nc_key = nc_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clidir_id = clidir_id;
        this.nc_fecemi = nc_fecemi;
        this.nc_fecent = nc_fecent;
        this.nc_zona = nc_zona;
        this.nc_motcam = nc_motcam;
        this.nc_sup = nc_sup;
        this.nc_vend = nc_vend;
        this.nc_trans = nc_trans;
        this.nc_hor = nc_hor;
        this.nc_usuadd = nc_usuadd;
        this.nc_pcadd = nc_pcadd;
        this.nc_usumod = nc_usumod;
        this.nc_pcmod = nc_pcmod;
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

    public String getNc_id() {
        return nc_id;
    }

    public void setNc_id(String nc_id) {
        this.nc_id = nc_id;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public long getCli_key() {
        return cli_key;
    }

    public void setCli_key(long cli_key) {
        this.cli_key = cli_key;
    }

    public long getClidir_id() {
        return clidir_id;
    }

    public void setClidir_id(long clidir_id) {
        this.clidir_id = clidir_id;
    }

    public Date getNc_fecemi() {
        return nc_fecemi;
    }

    public void setNc_fecemi(Date nc_fecemi) {
        this.nc_fecemi = nc_fecemi;
    }

    public Date getNc_fecent() {
        return nc_fecent;
    }

    public void setNc_fecent(Date nc_fecent) {
        this.nc_fecent = nc_fecent;
    }

    public int getNc_periodo() {
        return nc_periodo;
    }

    public void setNc_periodo(int nc_periodo) {
        this.nc_periodo = nc_periodo;
    }

    public int getNc_sit() {
        return nc_sit;
    }

    public void setNc_sit(int nc_sit) {
        this.nc_sit = nc_sit;
    }

    public int getNc_motrec() {
        return nc_motrec;
    }

    public void setNc_motrec(int nc_motrec) {
        this.nc_motrec = nc_motrec;
    }

    public int getNc_est() {
        return nc_est;
    }

    public void setNc_est(int nc_est) {
        this.nc_est = nc_est;
    }

    public String getNc_zona() {
        return nc_zona;
    }

    public void setNc_zona(String nc_zona) {
        this.nc_zona = nc_zona;
    }

    public int getNc_motcam() {
        return nc_motcam;
    }

    public void setNc_motcam(int nc_motcam) {
        this.nc_motcam = nc_motcam;
    }

    public int getNc_sup() {
        return nc_sup;
    }

    public void setNc_sup(int nc_sup) {
        this.nc_sup = nc_sup;
    }

    public int getNc_vend() {
        return nc_vend;
    }

    public void setNc_vend(int nc_vend) {
        this.nc_vend = nc_vend;
    }

    public int getNc_trans() {
        return nc_trans;
    }

    public void setNc_trans(int nc_trans) {
        this.nc_trans = nc_trans;
    }

    public int getNc_hor() {
        return nc_hor;
    }

    public void setNc_hor(int nc_hor) {
        this.nc_hor = nc_hor;
    }

    public String getNc_tipnotaent() {
        return nc_tipnotaent;
    }

    public void setNc_tipnotaent(String nc_tipnotaent) {
        this.nc_tipnotaent = nc_tipnotaent;
    }

    public String getNc_notaent() {
        return nc_notaent;
    }

    public void setNc_notaent(String nc_notaent) {
        this.nc_notaent = nc_notaent;
    }

    public String getNc_tipnotasal() {
        return nc_tipnotasal;
    }

    public void setNc_tipnotasal(String nc_tipnotasal) {
        this.nc_tipnotasal = nc_tipnotasal;
    }

    public String getNc_notasal() {
        return nc_notasal;
    }

    public void setNc_notasal(String nc_notasal) {
        this.nc_notasal = nc_notasal;
    }

    public String getNc_nroreg() {
        return nc_nroreg;
    }

    public void setNc_nroreg(String nc_nroreg) {
        this.nc_nroreg = nc_nroreg;
    }

    public String getNc_nrodep() {
        return nc_nrodep;
    }

    public void setNc_nrodep(String nc_nrodep) {
        this.nc_nrodep = nc_nrodep;
    }

    public String getNc_autusuadd() {
        return nc_autusuadd;
    }

    public void setNc_autusuadd(String nc_autusuadd) {
        this.nc_autusuadd = nc_autusuadd;
    }

    public Date getNc_autfecadd() {
        return nc_autfecadd;
    }

    public void setNc_autfecadd(Date nc_autfecadd) {
        this.nc_autfecadd = nc_autfecadd;
    }

    public String getNc_autpcadd() {
        return nc_autpcadd;
    }

    public void setNc_autpcadd(String nc_autpcadd) {
        this.nc_autpcadd = nc_autpcadd;
    }

    public String getNc_usuadd() {
        return nc_usuadd;
    }

    public void setNc_usuadd(String nc_usuadd) {
        this.nc_usuadd = nc_usuadd;
    }

    public Date getNc_fecadd() {
        return nc_fecadd;
    }

    public void setNc_fecadd(Date nc_fecadd) {
        this.nc_fecadd = nc_fecadd;
    }

    public String getNc_pcadd() {
        return nc_pcadd;
    }

    public void setNc_pcadd(String nc_pcadd) {
        this.nc_pcadd = nc_pcadd;
    }

    public String getNc_usumod() {
        return nc_usumod;
    }

    public void setNc_usumod(String nc_usumod) {
        this.nc_usumod = nc_usumod;
    }

    public Date getNc_fecmod() {
        return nc_fecmod;
    }

    public void setNc_fecmod(Date nc_fecmod) {
        this.nc_fecmod = nc_fecmod;
    }

    public String getNc_pcmod() {
        return nc_pcmod;
    }

    public void setNc_pcmod(String nc_pcmod) {
        this.nc_pcmod = nc_pcmod;
    }

    public String getCli_razsoc() {
        return cli_razsoc;
    }

    public void setCli_razsoc(String cli_razsoc) {
        this.cli_razsoc = cli_razsoc;
    }

    public String getClidir_direc() {
        return clidir_direc;
    }

    public void setClidir_direc(String clidir_direc) {
        this.clidir_direc = clidir_direc;
    }

    public String getNc_sitdes() {
        return nc_sitdes;
    }

    public void setNc_sitdes(String nc_sitdes) {
        this.nc_sitdes = nc_sitdes;
    }

    public String getMrec_des() {
        return mrec_des;
    }

    public void setMrec_des(String mrec_des) {
        this.mrec_des = mrec_des;
    }

    public String getZon_des() {
        return zon_des;
    }

    public void setZon_des(String zon_des) {
        this.zon_des = zon_des;
    }

    public String getMcam_des() {
        return mcam_des;
    }

    public void setMcam_des(String mcam_des) {
        this.mcam_des = mcam_des;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
    }

    public String getVen_nom() {
        return ven_nom;
    }

    public void setVen_nom(String ven_nom) {
        this.ven_nom = ven_nom;
    }

    public String getTrans_alias() {
        return trans_alias;
    }

    public void setTrans_alias(String trans_alias) {
        this.trans_alias = trans_alias;
    }

    public String getHor_des() {
        return hor_des;
    }

    public void setHor_des(String hor_des) {
        this.hor_des = hor_des;
    }

    public String getNc_sfecemi() {
        nc_sfecemi = new SimpleDateFormat("dd/MM/yyyy").format(nc_fecemi);
        return nc_sfecemi;
    }

    public void setNc_sfecemi(String nc_sfecemi) {
        this.nc_sfecemi = nc_sfecemi;
    }

    public String getNc_sfecent() {
        nc_sfecent = new SimpleDateFormat("dd/MM/yyyy").format(nc_fecent);
        return nc_sfecent;
    }

    public void setNc_sfecent(String nc_sfecent) {
        this.nc_sfecent = nc_sfecent;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
}
