package org.me.gj.model.logistica.procesos;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.me.gj.util.Utilitarios;

public class NotaRecojoCab {

    private long nr_key;
    private String nr_id;
    private int emp_id;
    private int suc_id;
    private String cli_id;
    private long cli_key;
    private long clidir_id;
    private Date nr_fecemi;
    private Date nr_fecent;
    private int nr_periodo;
    private int nr_sit;
    private int nr_motrec;
    private int nr_est;
    private String nr_zona;
    private int nr_motcam;
    private int nr_sup;
    private int nr_vend;
    private int nr_trans;
    private int nr_hor;
    private String nr_tipnotaent;
    private String nr_notaent;
    private String nr_nroreg;
    private String nr_autusuadd;
    private Date nr_autfecadd;
    private String nr_autpcadd;
    private String nr_usuadd;
    private Date nr_fecadd;
    private String nr_usumod;
    private Date nr_fecmod;
    private String nr_pcadd;
    private String nr_pcmod;
    private String nr_svend;
    private String cli_razsoc;
    private String clidir_direcc;
    private String mrec_des;
    private String zon_des;
    private String mcam_des;
    private String ven_nom;
    private String trans_id;
    private String trans_alias;
    private String hor_des;
    private String dessituacion;
    private String nr_sfecemi;
    private String nr_sfecent;
    private boolean valSelec;

    public NotaRecojoCab() {
    }

    public NotaRecojoCab(long nr_key, String nr_id, int emp_id, int suc_id, String cli_id, long cli_key, long clidir_id, String nr_zona, Date nr_fecemi, Date nr_fecent, int nr_motcam, int nr_sup, int nr_vend, int nr_trans, int nr_hor, String nr_usuadd, String nr_pcadd, String nr_usumod, String nr_pcmod) {
        this.nr_key = nr_key;
        this.nr_id = nr_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_id = cli_id;
        this.nr_zona = nr_zona;
        this.cli_key = cli_key;
        this.clidir_id = clidir_id;
        this.nr_fecemi = nr_fecemi;
        this.nr_fecent = nr_fecent;
        this.nr_motcam = nr_motcam;
        this.nr_sup = nr_sup;
        this.nr_vend = nr_vend;
        this.nr_trans = nr_trans;
        this.nr_hor = nr_hor;
        this.nr_usuadd = nr_usuadd;
        this.nr_pcadd = nr_pcadd;
        this.nr_usumod = nr_usumod;
        this.nr_pcmod = nr_pcmod;
    }

    public long getNr_key() {
        return nr_key;
    }

    public void setNr_key(long nr_key) {
        this.nr_key = nr_key;
    }

    public String getNr_id() {
        return nr_id;
    }

    public void setNr_id(String nr_id) {
        this.nr_id = nr_id;
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

    public Date getNr_fecemi() {
        return nr_fecemi;
    }

    public void setNr_fecemi(Date nr_fecemi) {
        this.nr_fecemi = nr_fecemi;
    }

    public String getNr_sfecemi() {
        nr_sfecemi = new SimpleDateFormat("dd/MM/YYYY").format(nr_fecemi);
        return nr_sfecemi;
    }

    public void setNr_sfecemi(String nr_sfecemi) {
        this.nr_sfecemi = nr_sfecemi;
    }

    public Date getNr_fecent() {
        return nr_fecent;
    }

    public String getNr_sfecent() {
        nr_sfecent = new SimpleDateFormat("dd/MM/YYYY").format(nr_fecent);
        return nr_sfecent;
    }

    public void setNr_sfecent(String nr_sfecent) {
        this.nr_sfecent = nr_sfecent;
    }

    public void setNr_fecent(Date nr_fecent) {
        this.nr_fecent = nr_fecent;
    }

    public int getNr_periodo() {
        return nr_periodo;
    }

    public void setNr_periodo(int nr_periodo) {
        this.nr_periodo = nr_periodo;
    }

    public int getNr_sit() {
        return nr_sit;
    }

    public void setNr_sit(int nr_sit) {
        this.nr_sit = nr_sit;
    }

    public String getDessituacion() {
        if (nr_sit == 1) {
            dessituacion = "INGRESADO";
        } else if (nr_sit == 2) {
            dessituacion = "AUTORIZADO";
        } else if (nr_sit == 3) {
            dessituacion = "EN TRANSITO";
        } else if (nr_sit == 4) {
            dessituacion = "RECHAZADO";
        } else {
            dessituacion = "PROCESADO";
        }
        return dessituacion;
    }

    public void setDessituacion(String dessituacion) {
        this.dessituacion = dessituacion;
    }

    public int getNr_motrec() {
        return nr_motrec;
    }

    public void setNr_motrec(int nr_motrec) {
        this.nr_motrec = nr_motrec;
    }

    public int getNr_est() {
        return nr_est;
    }

    public void setNr_est(int nr_est) {
        this.nr_est = nr_est;
    }

    public String getNr_zona() {
        return nr_zona;
    }

    public void setNr_zona(String nr_zona) {
        this.nr_zona = nr_zona;
    }

    public int getNr_motcam() {
        return nr_motcam;
    }

    public void setNr_motcam(int nr_motcam) {
        this.nr_motcam = nr_motcam;
    }

    public int getNr_sup() {
        return nr_sup;
    }

    public void setNr_sup(int nr_sup) {
        this.nr_sup = nr_sup;
    }

    public int getNr_vend() {
        return nr_vend;
    }

    public void setNr_vend(int nr_vend) {
        this.nr_vend = nr_vend;
    }

    public String getNr_svend() {
        nr_svend = Utilitarios.lpad(String.valueOf(nr_vend), 4, "0");
        return nr_svend;
    }

    public void setNr_svend(String nr_svend) {
        this.nr_svend = nr_svend;
    }

    public int getNr_trans() {
        return nr_trans;
    }

    public void setNr_trans(int nr_trans) {
        this.nr_trans = nr_trans;
    }

    public int getNr_hor() {
        return nr_hor;
    }

    public void setNr_hor(int nr_hor) {
        this.nr_hor = nr_hor;
    }

    public String getNr_tipnotaent() {
        return nr_tipnotaent;
    }

    public void setNr_tipnotaent(String nr_tipnotaent) {
        this.nr_tipnotaent = nr_tipnotaent;
    }

    public String getNr_notaent() {
        return nr_notaent;
    }

    public void setNr_notaent(String nr_notaent) {
        this.nr_notaent = nr_notaent;
    }

    public String getNr_nroreg() {
        return nr_nroreg;
    }

    public void setNr_nroreg(String nr_nroreg) {
        this.nr_nroreg = nr_nroreg;
    }

    public String getNr_autusuadd() {
        return nr_autusuadd;
    }

    public void setNr_autusuadd(String nr_autusuadd) {
        this.nr_autusuadd = nr_autusuadd;
    }

    public Date getNr_autfecadd() {
        return nr_autfecadd;
    }

    public void setNr_autfecadd(Date nr_autfecadd) {
        this.nr_autfecadd = nr_autfecadd;
    }

    public String getNr_autpcadd() {
        return nr_autpcadd;
    }

    public void setNr_autpcadd(String nr_autpcadd) {
        this.nr_autpcadd = nr_autpcadd;
    }

    public String getNr_usuadd() {
        return nr_usuadd;
    }

    public void setNr_usuadd(String nr_usuadd) {
        this.nr_usuadd = nr_usuadd;
    }

    public Date getNr_fecadd() {
        return nr_fecadd;
    }

    public void setNr_fecadd(Date nr_fecadd) {
        this.nr_fecadd = nr_fecadd;
    }

    public String getNr_usumod() {
        return nr_usumod;
    }

    public void setNr_usumod(String nr_usumod) {
        this.nr_usumod = nr_usumod;
    }

    public Date getNr_fecmod() {
        return nr_fecmod;
    }

    public void setNr_fecmod(Date nr_fecmod) {
        this.nr_fecmod = nr_fecmod;
    }

    public String getNr_pcadd() {
        return nr_pcadd;
    }

    public void setNr_pcadd(String nr_pcadd) {
        this.nr_pcadd = nr_pcadd;
    }

    public String getNr_pcmod() {
        return nr_pcmod;
    }

    public void setNr_pcmod(String nr_pcmod) {
        this.nr_pcmod = nr_pcmod;
    }

    public String getCli_razsoc() {
        return cli_razsoc;
    }

    public void setCli_razsoc(String cli_razsoc) {
        this.cli_razsoc = cli_razsoc;
    }

    public String getClidir_direcc() {
        return clidir_direcc;
    }

    public void setClidir_direcc(String clidir_direcc) {
        this.clidir_direcc = clidir_direcc;
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

    public String getVen_nom() {
        return ven_nom;
    }

    public void setVen_nom(String ven_nom) {
        this.ven_nom = ven_nom;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
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

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
}
