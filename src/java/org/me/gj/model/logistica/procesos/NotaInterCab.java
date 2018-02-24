package org.me.gj.model.logistica.procesos;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.me.gj.util.Utilitarios;

public class NotaInterCab {

    private long ni_key;
    private int emp_id;
    private int suc_id;
    private String ni_id;
    private Date ni_fecemi;
    private Date ni_fecent;
    private long cli_key;
    private String cli_id;
    private long clidir_id;
    private int ni_periodo;
    private int ni_sit;
    private int ni_motrec;
    private int ni_est;
    private String ni_zona;
    private int ni_motcam;
    private int ni_sup;
    private int ni_vend;
    private int ni_trans;
    private int ni_hor;
    private String ni_tipnotaent;
    private String ni_notaent;
    private String ni_tipnotasal;
    private String ni_notasal;
    private String ni_nroreg;
    private String ni_nrodep;
    private String ni_autusuadd;
    private Date ni_autfecadd;
    private String ni_autpcadd;
    private String ni_usuadd;
    private Date ni_fecadd;
    private String ni_pcadd;
    private String ni_usumod;
    private Date ni_fecmod;
    private String ni_pcmod;
    private long nic_provid;
    private String nic_provrazsoc;
    private int nic_lpcid;
    private String nic_lpcdes;
    private String cli_razsoc;
    private String clidir_direcc;
    private String zon_des;
    private String mcam_des;
    private String sup_apenom;
    private String ven_apenom;
    private String trans_alias;
    private String hor_des;
    private String mrec_des;
    private String dessituacion;
    private String ni_sfecemi;
    private String ni_sfecent;
    private String ni_svend;
    private boolean valSelec;

    public NotaInterCab() {
    }

    public NotaInterCab(long ni_key, Date ni_fecemi, Date ni_fecent, int emp_id, int suc_id, long cli_key, String cli_id, long clidir_id, String ni_zona, int ni_motcam, int ni_sup, int ni_vend, int ni_trans, int ni_hor, String ni_usuadd, String ni_pcadd, String ni_usumod, String ni_pcmod, long nic_provid, int nic_lpcid) {
        this.ni_key = ni_key;
        this.ni_fecemi = ni_fecemi;
        this.ni_fecent = ni_fecent;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_key = cli_key;
        this.cli_id = cli_id;
        this.clidir_id = clidir_id;
        this.ni_zona = ni_zona;
        this.ni_motcam = ni_motcam;
        this.ni_sup = ni_sup;
        this.ni_vend = ni_vend;
        this.ni_trans = ni_trans;
        this.ni_hor = ni_hor;
        this.ni_usuadd = ni_usuadd;
        this.ni_pcadd = ni_pcadd;
        this.ni_usumod = ni_usumod;
        this.ni_pcmod = ni_pcmod;
        this.nic_provid = nic_provid;
        this.nic_lpcid = nic_lpcid;
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

    public String getNi_id() {
        return ni_id;
    }

    public void setNi_id(String ni_id) {
        this.ni_id = ni_id;
    }

    public Date getNi_fecemi() {
        return ni_fecemi;
    }

    public void setNi_fecemi(Date ni_fecemi) {
        this.ni_fecemi = ni_fecemi;
    }

    public Date getNi_fecent() {
        return ni_fecent;
    }

    public void setNi_fecent(Date ni_fecent) {
        this.ni_fecent = ni_fecent;
    }

    public long getCli_key() {
        return cli_key;
    }

    public void setCli_key(long cli_key) {
        this.cli_key = cli_key;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public long getClidir_id() {
        return clidir_id;
    }

    public void setClidir_id(long clidir_id) {
        this.clidir_id = clidir_id;
    }

    public int getNi_periodo() {
        return ni_periodo;
    }

    public void setNi_periodo(int ni_periodo) {
        this.ni_periodo = ni_periodo;
    }

    public int getNi_sit() {
        return ni_sit;
    }

    public void setNi_sit(int ni_sit) {
        this.ni_sit = ni_sit;
    }

    public int getNi_motrec() {
        return ni_motrec;
    }

    public void setNi_motrec(int ni_motrec) {
        this.ni_motrec = ni_motrec;
    }

    public int getNi_est() {
        return ni_est;
    }

    public void setNi_est(int ni_est) {
        this.ni_est = ni_est;
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

    public String getNi_zona() {
        return ni_zona;
    }

    public void setNi_zona(String ni_zona) {
        this.ni_zona = ni_zona;
    }

    public int getNi_motcam() {
        return ni_motcam;
    }

    public void setNi_motcam(int ni_motcam) {
        this.ni_motcam = ni_motcam;
    }

    public int getNi_sup() {
        return ni_sup;
    }

    public void setNi_sup(int ni_sup) {
        this.ni_sup = ni_sup;
    }

    public int getNi_vend() {
        return ni_vend;
    }

    public void setNi_vend(int ni_vend) {
        this.ni_vend = ni_vend;
    }

    public int getNi_trans() {
        return ni_trans;
    }

    public void setNi_trans(int ni_trans) {
        this.ni_trans = ni_trans;
    }

    public int getNi_hor() {
        return ni_hor;
    }

    public void setNi_hor(int ni_hor) {
        this.ni_hor = ni_hor;
    }

    public String getNi_tipnotaent() {
        return ni_tipnotaent;
    }

    public void setNi_tipnotaent(String ni_tipnotaent) {
        this.ni_tipnotaent = ni_tipnotaent;
    }

    public String getNi_notaent() {
        return ni_notaent;
    }

    public void setNi_notaent(String ni_notaent) {
        this.ni_notaent = ni_notaent;
    }

    public String getNi_tipnotasal() {
        return ni_tipnotasal;
    }

    public void setNi_tipnotasal(String ni_tipnotasal) {
        this.ni_tipnotasal = ni_tipnotasal;
    }

    public String getNi_notasal() {
        return ni_notasal;
    }

    public void setNi_notasal(String ni_notasal) {
        this.ni_notasal = ni_notasal;
    }

    public String getNi_nroreg() {
        return ni_nroreg;
    }

    public void setNi_nroreg(String ni_nroreg) {
        this.ni_nroreg = ni_nroreg;
    }

    public String getNi_nrodep() {
        return ni_nrodep;
    }

    public void setNi_nrodep(String ni_nrodep) {
        this.ni_nrodep = ni_nrodep;
    }

    public String getNi_autusuadd() {
        return ni_autusuadd;
    }

    public void setNi_autusuadd(String ni_autusuadd) {
        this.ni_autusuadd = ni_autusuadd;
    }

    public Date getNi_autfecadd() {
        return ni_autfecadd;
    }

    public void setNi_autfecadd(Date ni_autfecadd) {
        this.ni_autfecadd = ni_autfecadd;
    }

    public String getNi_autpcadd() {
        return ni_autpcadd;
    }

    public void setNi_autpcadd(String ni_autpcadd) {
        this.ni_autpcadd = ni_autpcadd;
    }

    public String getNi_usuadd() {
        return ni_usuadd;
    }

    public void setNi_usuadd(String ni_usuadd) {
        this.ni_usuadd = ni_usuadd;
    }

    public Date getNi_fecadd() {
        return ni_fecadd;
    }

    public void setNi_fecadd(Date ni_fecadd) {
        this.ni_fecadd = ni_fecadd;
    }

    public String getNi_pcadd() {
        return ni_pcadd;
    }

    public void setNi_pcadd(String ni_pcadd) {
        this.ni_pcadd = ni_pcadd;
    }

    public String getNi_usumod() {
        return ni_usumod;
    }

    public void setNi_usumod(String ni_usumod) {
        this.ni_usumod = ni_usumod;
    }

    public Date getNi_fecmod() {
        return ni_fecmod;
    }

    public void setNi_fecmod(Date ni_fecmod) {
        this.ni_fecmod = ni_fecmod;
    }

    public String getNi_pcmod() {
        return ni_pcmod;
    }

    public void setNi_pcmod(String ni_pcmod) {
        this.ni_pcmod = ni_pcmod;
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

    public String getSup_apenom() {
        return sup_apenom;
    }

    public void setSup_apenom(String sup_apenom) {
        this.sup_apenom = sup_apenom;
    }

    public String getVen_apenom() {
        return ven_apenom;
    }

    public void setVen_apenom(String ven_apenom) {
        this.ven_apenom = ven_apenom;
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

    public String getMrec_des() {
        return mrec_des;
    }

    public void setMrec_des(String mrec_des) {
        this.mrec_des = mrec_des;
    }

    public String getDessituacion() {
        if (ni_sit == 1) {
            dessituacion = "INGRESADO";
        } else if (ni_sit == 2) {
            dessituacion = "AUTORIZADO";
        } else if (ni_sit == 3) {
            dessituacion = "EN TRANSITO";
        } else if (ni_sit == 4) {
            dessituacion = "RECHAZADO";
        } else {
            dessituacion = "PROCESADO";
        }
        return dessituacion;
    }

    public void setDessituacion(String dessituacion) {
        this.dessituacion = dessituacion;
    }

    public String getNi_sfecemi() {
        ni_sfecemi = new SimpleDateFormat("dd/MM/YYYY").format(ni_fecemi);
        return ni_sfecemi;
    }

    public void setNi_sfecemi(String ni_sfecemi) {
        this.ni_sfecemi = ni_sfecemi;
    }

    public String getNi_sfecent() {
        ni_sfecent = new SimpleDateFormat("dd/MM/YYYY").format(ni_fecent);
        return ni_sfecent;
    }

    public void setNi_sfecent(String ni_sfecent) {
        this.ni_sfecent = ni_sfecent;
    }

    public String getNi_svend() {
        ni_svend = Utilitarios.lpad(String.valueOf(ni_vend), 4, "0");
        return ni_svend;
    }

    public void setNi_svend(String ni_svend) {
        this.ni_svend = ni_svend;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public long getNic_provid() {
        return nic_provid;
    }

    public void setNic_provid(long nic_provid) {
        this.nic_provid = nic_provid;
    }

    public String getNic_provrazsoc() {
        return nic_provrazsoc;
    }

    public void setNic_provrazsoc(String nic_provrazsoc) {
        this.nic_provrazsoc = nic_provrazsoc;
    }

    public int getNic_lpcid() {
        return nic_lpcid;
    }

    public void setNic_lpcid(int nic_lpcid) {
        this.nic_lpcid = nic_lpcid;
    }

    public String getNic_lpcdes() {
        return nic_lpcdes;
    }

    public void setNic_lpcdes(String nic_lpcdes) {
        this.nic_lpcdes = nic_lpcdes;
    }

}
