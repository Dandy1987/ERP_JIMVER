package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class CliDireccion {

    private String cli_id;
    private long cli_key;
    private long clidir_id;
    private String clidir_direc;
    private int clidir_idvia;
    private String clidir_nomvia;
    private int clidir_signo;
    private int clidir_numvia;
    private int clidir_idint;
    private long clidir_nroint;
    private String clidir_mza;
    private String clidir_lote;
    private int clidir_idseg;
    private String clidir_nomseg;
    private String clidir_ref;
    private int clidir_giro;
    private int clidir_postal;
    private String ubi_id;
    private String clidir_usuadd;
    private Date clidir_fecadd;
    private String clidir_usumod;
    private Date clidir_fecmod;
    private String ind_accion = "Q";
    private int clizon_default;
    private int clizon_est;
    private long zon_key;
    private String zon_id;
    private String zon_des;
    private int zon_diavis;
    private String zon_diavis_des;
    private String giro_id;
    private String giro_des;
    private String pos_id;
    private String pos_des;
    private String hor_id;
    private String hor_des;
    private String ubi_des;
    private String can_id;
    private String can_des;
    private String ven_id;
    private String ven_apenom;
    private String trans_id;
    private String trans_des;
    private int cli_lista;
    private int cli_factura;
    private int cli_perc;
    private boolean valor;

    public int getCli_lista() {
        return cli_lista;
    }

    public void setCli_lista(int cli_lista) {
        this.cli_lista = cli_lista;
    }

    public int getCli_factura() {
        return cli_factura;
    }

    public void setCli_factura(int cli_factura) {
        this.cli_factura = cli_factura;
    }

    public int getCli_perc() {
        return cli_perc;
    }

    public void setCli_perc(int cli_perc) {
        this.cli_perc = cli_perc;
    }

    public CliDireccion(String cli_id, long cli_key, long clidir_id, String clidir_usumod) {
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clidir_id = clidir_id;
        this.clidir_usumod = clidir_usumod;
    }

    public CliDireccion(String cli_id, long cli_key, long clidir_id, String clidir_direc, int clidir_idvia, String clidir_nomvia, int clidir_signo, int clidir_numvia, int clidir_idint, long clidir_nroint, String clidir_mza, String clidir_lote, int clidir_idseg, String clidir_nomseg, String clidir_ref, int clidir_giro, int clidir_postal, String ubi_id, String clidir_usuadd, Date clidir_fecadd, String clidir_usumod, Date clidir_fecmod) {
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clidir_id = clidir_id;
        this.clidir_direc = clidir_direc;
        this.clidir_idvia = clidir_idvia;
        this.clidir_nomvia = clidir_nomvia;
        this.clidir_signo = clidir_signo;
        this.clidir_numvia = clidir_numvia;
        this.clidir_idint = clidir_idint;
        this.clidir_nroint = clidir_nroint;
        this.clidir_mza = clidir_mza;
        this.clidir_lote = clidir_lote;
        this.clidir_idseg = clidir_idseg;
        this.clidir_nomseg = clidir_nomseg;
        this.clidir_ref = clidir_ref;
        this.clidir_giro = clidir_giro;
        this.clidir_postal = clidir_postal;
        this.ubi_id = ubi_id;
        this.clidir_usuadd = clidir_usuadd;
        this.clidir_fecadd = clidir_fecadd;
        this.clidir_usumod = clidir_usumod;
        this.clidir_fecmod = clidir_fecmod;
    }

    public CliDireccion() {
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

    public String getClidir_direc() {
        return clidir_direc;
    }

    public void setClidir_direc(String clidir_direc) {
        this.clidir_direc = clidir_direc;
    }

    public int getClidir_idvia() {
        return clidir_idvia;
    }

    public void setClidir_idvia(int clidir_idvia) {
        this.clidir_idvia = clidir_idvia;
    }

    public String getClidir_nomvia() {
        return clidir_nomvia;
    }

    public void setClidir_nomvia(String clidir_nomvia) {
        this.clidir_nomvia = clidir_nomvia;
    }

    public int getClidir_signo() {
        return clidir_signo;
    }

    public void setClidir_signo(int clidir_signo) {
        this.clidir_signo = clidir_signo;
    }

    public int getClidir_numvia() {
        return clidir_numvia;
    }

    public void setClidir_numvia(int clidir_numvia) {
        this.clidir_numvia = clidir_numvia;
    }

    public int getClidir_idint() {
        return clidir_idint;
    }

    public void setClidir_idint(int clidir_idint) {
        this.clidir_idint = clidir_idint;
    }

    public long getClidir_nroint() {
        return clidir_nroint;
    }

    public void setClidir_nroint(long clidir_nroint) {
        this.clidir_nroint = clidir_nroint;
    }

    public String getClidir_mza() {
        return clidir_mza;
    }

    public void setClidir_mza(String clidir_mza) {
        this.clidir_mza = clidir_mza;
    }

    public String getClidir_lote() {
        return clidir_lote;
    }

    public void setClidir_lote(String clidir_lote) {
        this.clidir_lote = clidir_lote;
    }

    public int getClidir_idseg() {
        return clidir_idseg;
    }

    public void setClidir_idseg(int clidir_idseg) {
        this.clidir_idseg = clidir_idseg;
    }

    public String getClidir_nomseg() {
        return clidir_nomseg;
    }

    public void setClidir_nomseg(String clidir_nomseg) {
        this.clidir_nomseg = clidir_nomseg;
    }

    public String getClidir_ref() {
        return clidir_ref;
    }

    public void setClidir_ref(String clidir_ref) {
        this.clidir_ref = clidir_ref;
    }

    public int getClidir_giro() {
        return clidir_giro;
    }

    public void setClidir_giro(int clidir_giro) {
        this.clidir_giro = clidir_giro;
    }

    public int getClidir_postal() {
        return clidir_postal;
    }

    public void setClidir_postal(int clidir_postal) {
        this.clidir_postal = clidir_postal;
    }

    public String getUbi_id() {
        return ubi_id;
    }

    public void setUbi_id(String ubi_id) {
        this.ubi_id = ubi_id;
    }

    public String getClidir_usuadd() {
        return clidir_usuadd;
    }

    public void setClidir_usuadd(String clidir_usuadd) {
        this.clidir_usuadd = clidir_usuadd;
    }

    public Date getClidir_fecadd() {
        return clidir_fecadd;
    }

    public void setClidir_fecadd(Date clidir_fecadd) {
        this.clidir_fecadd = clidir_fecadd;
    }

    public String getClidir_usumod() {
        return clidir_usumod;
    }

    public void setClidir_usumod(String clidir_usumod) {
        this.clidir_usumod = clidir_usumod;
    }

    public Date getClidir_fecmod() {
        return clidir_fecmod;
    }

    public void setClidir_fecmod(Date clidir_fecmod) {
        this.clidir_fecmod = clidir_fecmod;
    }

    public int getClizon_default() {
        return clizon_default;
    }

    public void setClizon_default(int clizon_default) {
        this.clizon_default = clizon_default;
    }

    public int getClizon_est() {
        return clizon_est;
    }

    public void setClizon_est(int clizon_est) {
        this.clizon_est = clizon_est;
    }

    public long getZon_key() {
        return zon_key;
    }

    public void setZon_key(long zon_key) {
        this.zon_key = zon_key;
    }

    public String getZon_id() {
        return zon_id;
    }

    public void setZon_id(String zon_id) {
        this.zon_id = zon_id;
    }

    public String getZon_des() {
        return zon_des;
    }

    public void setZon_des(String zon_des) {
        this.zon_des = zon_des;
    }

    public int getZon_diavis() {
        return zon_diavis;
    }

    public void setZon_diavis(int zon_diavis) {
        this.zon_diavis = zon_diavis;
    }

    public String getZon_diavis_des() {
        return zon_diavis_des;
    }

    public void setZon_diavis_des(String zon_diavis_des) {
        this.zon_diavis_des = zon_diavis_des;
    }

    public String getGiro_id() {
        return giro_id;
    }

    public void setGiro_id(String giro_id) {
        this.giro_id = giro_id;
    }

    public String getGiro_des() {
        return giro_des;
    }

    public void setGiro_des(String giro_des) {
        this.giro_des = giro_des;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getPos_des() {
        return pos_des;
    }

    public void setPos_des(String pos_des) {
        this.pos_des = pos_des;
    }

    public String getHor_id() {
        return hor_id;
    }

    public void setHor_id(String hor_id) {
        this.hor_id = hor_id;
    }

    public String getHor_des() {
        return hor_des;
    }

    public void setHor_des(String hor_des) {
        this.hor_des = hor_des;
    }

    public String getUbi_des() {
        return ubi_des;
    }

    public void setUbi_des(String ubi_des) {
        this.ubi_des = ubi_des;
    }

    public String getCan_id() {
        return can_id;
    }

    public void setCan_id(String can_id) {
        this.can_id = can_id;
    }

    public String getCan_des() {
        return can_des;
    }

    public void setCan_des(String can_des) {
        this.can_des = can_des;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
    }

    public String getVen_apenom() {
        return ven_apenom;
    }

    public void setVen_apenom(String ven_apenom) {
        this.ven_apenom = ven_apenom;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getTrans_des() {
        return trans_des;
    }

    public void setTrans_des(String trans_des) {
        this.trans_des = trans_des;
    }

    public boolean isValor() {
        valor = clizon_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }
}
