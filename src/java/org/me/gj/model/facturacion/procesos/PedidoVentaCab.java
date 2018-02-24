package org.me.gj.model.facturacion.procesos;

import java.text.*;
import java.util.Date;
import java.util.Locale;

public class PedidoVentaCab {

    private String pcab_nroped;
    private Date pcab_fecemi;
    private String pcab_periodo;
    private int emp_id;
    private int suc_id;
    private int pcab_estado;
    private int pcab_situacion;
    private Date pcab_fecent;
    private String zon_id;
    private String pcab_motrec;
    private String pcab_motrec_des;
    private int clidir_id;
    private String pcab_dirent;
    private String cli_id;
    private String pcab_canid;
    private String ven_id;
    private String sup_id;
    private int pcab_facbol;
    private String con_id;
    private String pcab_mon;
    private String lp_id;
    private double pcab_tipcam;
    private double pcab_limcre;
    private int pcab_limdoc;
    private double pcab_salcre;
    private String pcab_nrodni;
    private String pcab_nroruc;
    private double pcab_totped;
    private int pcab_diavis;
    private String pcab_horent;
    private int pcab_gpslat;
    private int pcab_gpslon;
    private double pcab_totper;
    private int pcab_aprcon;
    private String pcab_aprglo;
    private String pcab_docref;
    private String pcab_giro;
    private int pcab_ppago;
    private String pcab_tipven;
    private int pcab_modtipcam;
    private String pcab_usuadd;
    private String pcab_pcadd;
    private String pcab_usumod;
    private String pcab_pcmod;
    private Date pcab_fecadd;
    private Date pcab_fecmod;
    private String pcab_situacion_des;
    private String ven_des;
    private String cli_des;
    private String zon_des;
    private String lp_des;
    private String cond_des;
    private String tip_ventades;
    private int con_dpla;
    private String s_totped;
    private String s_fecemi;
    private String pcab_diavisdes;
    private boolean valor;
    private boolean valortipcam;
    private boolean valSelec = false;//estaba true
    private Date hora;
    private int indicador;
    private String notestip;
    private String notesdes;
    private String notesnro;
    private String sup_des;

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df4 = new DecimalFormat("#,###,##0.00", dfs);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public PedidoVentaCab() {
    }

    public PedidoVentaCab(String pcab_nroped, Date pcab_fecemi, int emp_id, int suc_id, int pcab_estado, int pcab_situacion,
            Date pcab_fecent, String zon_id, String pcab_motrec, int clidir_id, String pcab_dirent, String cli_id, String pcab_canid,
            String ven_id, String sup_id, int pcab_facbol, String con_id, String pcab_mon, String lp_id, double pcab_tipcam, double pcab_limcre,
            int pcab_limdoc, double pcab_salcre, String pcab_nrodni, String pcab_nroruc, double pcab_totped, int pcab_diavis, String pcab_horent,
            int pcab_gpslat, int pcab_gpslon, double pcab_totper, int pcab_aprcon, String pcab_aprglo, String pcab_docref, String pcab_giro,
            int pcab_ppago, String pcab_tipven, int pcab_modtipcam, String pcab_usuadd, String pcab_pcadd, String pcab_usumod, String pcab_pcmod) {

        dfs = new DecimalFormatSymbols(Locale.US);
        df4 = new DecimalFormat("#,###,##0.0000", dfs);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        this.pcab_nroped = pcab_nroped;
        this.pcab_fecemi = pcab_fecemi;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.pcab_estado = pcab_estado;
        this.pcab_situacion = pcab_situacion;
        this.pcab_fecent = pcab_fecent;
        this.zon_id = zon_id;
        this.pcab_motrec = pcab_motrec;
        this.clidir_id = clidir_id;
        this.pcab_dirent = pcab_dirent;
        this.cli_id = cli_id;
        this.pcab_canid = pcab_canid;
        this.ven_id = ven_id;
        this.sup_id = sup_id;
        this.pcab_facbol = pcab_facbol;
        this.con_id = con_id;
        this.pcab_mon = pcab_mon;
        this.lp_id = lp_id;
        this.pcab_tipcam = pcab_tipcam;
        this.pcab_limcre = pcab_limcre;
        this.pcab_limdoc = pcab_limdoc;
        this.pcab_salcre = pcab_salcre;
        this.pcab_nrodni = pcab_nrodni;
        this.pcab_nroruc = pcab_nroruc;
        this.pcab_totped = pcab_totped;
        this.pcab_diavis = pcab_diavis;
        this.pcab_horent = pcab_horent;
        this.pcab_gpslat = pcab_gpslat;
        this.pcab_gpslon = pcab_gpslon;
        this.pcab_totper = pcab_totper;
        this.pcab_aprcon = pcab_aprcon;
        this.pcab_aprglo = pcab_aprglo;
        this.pcab_docref = pcab_docref;
        this.pcab_giro = pcab_giro;
        this.pcab_ppago = pcab_ppago;
        this.pcab_tipven = pcab_tipven;
        this.pcab_modtipcam = pcab_modtipcam;
        this.pcab_usuadd = pcab_usuadd;
        this.pcab_pcadd = pcab_pcadd;
        this.pcab_usumod = pcab_usumod;
        this.pcab_pcmod = pcab_pcmod;
    }

    public String getPcab_nroped() {
        return pcab_nroped;
    }

    public void setPcab_nroped(String pcab_nroped) {
        this.pcab_nroped = pcab_nroped;
    }

    public Date getPcab_fecemi() {
        return pcab_fecemi;
    }

    public void setPcab_fecemi(Date pcab_fecemi) {
        this.pcab_fecemi = pcab_fecemi;
    }

    public String getPcab_periodo() {
        return pcab_periodo;
    }

    public void setPcab_periodo(String pcab_periodo) {
        this.pcab_periodo = pcab_periodo;
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

    public int getPcab_estado() {
        return pcab_estado;
    }

    public void setPcab_estado(int pcab_estado) {
        this.pcab_estado = pcab_estado;
    }

    public int getPcab_situacion() {
        return pcab_situacion;
    }

    public void setPcab_situacion(int pcab_situacion) {
        this.pcab_situacion = pcab_situacion;
    }

    public Date getPcab_fecent() {
        return pcab_fecent;
    }

    public void setPcab_fecent(Date pcab_fecent) {
        this.pcab_fecent = pcab_fecent;
    }

    public String getZon_id() {
        return zon_id;
    }

    public void setZon_id(String zon_id) {
        this.zon_id = zon_id;
    }

    public String getPcab_motrec() {
        return pcab_motrec;
    }

    public void setPcab_motrec(String pcab_motrec) {
        this.pcab_motrec = pcab_motrec;
    }

    public int getClidir_id() {
        return clidir_id;
    }

    public void setClidir_id(int clidir_id) {
        this.clidir_id = clidir_id;
    }

    public String getPcab_dirent() {
        return pcab_dirent;
    }

    public void setPcab_dirent(String pcab_dirent) {
        this.pcab_dirent = pcab_dirent;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public String getPcab_canid() {
        return pcab_canid;
    }

    public void setPcab_canid(String pcab_canid) {
        this.pcab_canid = pcab_canid;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
    }

    public String getSup_id() {
        return sup_id;
    }

    public void setSup_id(String sup_id) {
        this.sup_id = sup_id;
    }

    public int getPcab_facbol() {
        return pcab_facbol;
    }

    public void setPcab_facbol(int pcab_facbol) {
        this.pcab_facbol = pcab_facbol;
    }

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
    }

    public String getPcab_mon() {
        return pcab_mon;
    }

    public void setPcab_mon(String pcab_mon) {
        this.pcab_mon = pcab_mon;
    }

    public String getLp_id() {
        return lp_id;
    }

    public void setLp_id(String lp_id) {
        this.lp_id = lp_id;
    }

    public double getPcab_tipcam() {
        return pcab_tipcam;
    }

    public void setPcab_tipcam(double pcab_tipcam) {
        this.pcab_tipcam = pcab_tipcam;
    }

    public double getPcab_limcre() {
        return pcab_limcre;
    }

    public void setPcab_limcre(double pcab_limcre) {
        this.pcab_limcre = pcab_limcre;
    }

    public int getPcab_limdoc() {
        return pcab_limdoc;
    }

    public void setPcab_limdoc(int pcab_limdoc) {
        this.pcab_limdoc = pcab_limdoc;
    }

    public double getPcab_salcre() {
        return pcab_salcre;
    }

    public void setPcab_salcre(double pcab_salcre) {
        this.pcab_salcre = pcab_salcre;
    }

    public String getPcab_nrodni() {
        return pcab_nrodni;
    }

    public void setPcab_nrodni(String pcab_nrodni) {
        this.pcab_nrodni = pcab_nrodni;
    }

    public String getPcab_nroruc() {
        return pcab_nroruc;
    }

    public void setPcab_nroruc(String pcab_nroruc) {
        this.pcab_nroruc = pcab_nroruc;
    }

    public double getPcab_totped() {
        return pcab_totped;
    }

    public void setPcab_totped(double pcab_totped) {
        this.pcab_totped = pcab_totped;
    }

    public int getPcab_diavis() {
        return pcab_diavis;
    }

    public void setPcab_diavis(int pcab_diavis) {
        this.pcab_diavis = pcab_diavis;
    }

    public String getPcab_horent() {
        return pcab_horent;
    }

    public void setPcab_horent(String pcab_horent) {
        this.pcab_horent = pcab_horent;
    }

    public int getPcab_gpslat() {
        return pcab_gpslat;
    }

    public void setPcab_gpslat(int pcab_gpslat) {
        this.pcab_gpslat = pcab_gpslat;
    }

    public int getPcab_gpslon() {
        return pcab_gpslon;
    }

    public void setPcab_gpslon(int pcab_gpslon) {
        this.pcab_gpslon = pcab_gpslon;
    }

    public double getPcab_totper() {
        return pcab_totper;
    }

    public void setPcab_totper(double pcab_totper) {
        this.pcab_totper = pcab_totper;
    }

    public int getPcab_aprcon() {
        return pcab_aprcon;
    }

    public void setPcab_aprcon(int pcab_aprcon) {
        this.pcab_aprcon = pcab_aprcon;
    }

    public String getPcab_aprglo() {
        return pcab_aprglo;
    }

    public void setPcab_aprglo(String pcab_aprglo) {
        this.pcab_aprglo = pcab_aprglo;
    }

    public String getPcab_docref() {
        return pcab_docref;
    }

    public void setPcab_docref(String pcab_docref) {
        this.pcab_docref = pcab_docref;
    }

    public String getPcab_giro() {
        return pcab_giro;
    }

    public void setPcab_giro(String pcab_giro) {
        this.pcab_giro = pcab_giro;
    }

    public int getPcab_ppago() {
        return pcab_ppago;
    }

    public void setPcab_ppago(int pcab_ppago) {
        this.pcab_ppago = pcab_ppago;
    }

    public String getPcab_tipven() {
        return pcab_tipven;
    }

    public void setPcab_tipven(String pcab_tipven) {
        this.pcab_tipven = pcab_tipven;
    }

    public int getPcab_modtipcam() {
        return pcab_modtipcam;
    }

    public void setPcab_modtipcam(int pcab_modtipcam) {
        this.pcab_modtipcam = pcab_modtipcam;
    }

    public String getPcab_usuadd() {
        return pcab_usuadd;
    }

    public void setPcab_usuadd(String pcab_usuadd) {
        this.pcab_usuadd = pcab_usuadd;
    }

    public Date getPcab_fecadd() {
        return pcab_fecadd;
    }

    public void setPcab_fecadd(Date pcab_fecadd) {
        this.pcab_fecadd = pcab_fecadd;
    }

    public String getPcab_usumod() {
        return pcab_usumod;
    }

    public void setPcab_usumod(String pcab_usumod) {
        this.pcab_usumod = pcab_usumod;
    }

    public Date getPcab_fecmod() {
        return pcab_fecmod;
    }

    public void setPcab_fecmod(Date pcab_fecmod) {
        this.pcab_fecmod = pcab_fecmod;
    }

    public String getPcab_pcadd() {
        return pcab_pcadd;
    }

    public void setPcab_pcadd(String pcab_pcadd) {
        this.pcab_pcadd = pcab_pcadd;
    }

    public String getPcab_pcmod() {
        return pcab_pcmod;
    }

    public void setPcab_pcmod(String pcab_pcmod) {
        this.pcab_pcmod = pcab_pcmod;
    }

    public String getPcab_situacion_des() {
        if (pcab_situacion == 1) {
            pcab_situacion_des = "INGRESADO";
        } else if (pcab_situacion == 2) {
            pcab_situacion_des = "PROCESADO";
        } else if (pcab_situacion == 3) {
            pcab_situacion_des = "RECHAZADO";
        } else {
            pcab_situacion_des = "REVISADO";
        }
        return pcab_situacion_des;
    }

    public void setPcab_situacion_des(String pcab_situacion_des) {
        this.pcab_situacion_des = pcab_situacion_des;
    }

    public String getVen_des() {
        return ven_des;
    }

    public void setVen_des(String ven_des) {
        this.ven_des = ven_des;
    }

    public String getCli_des() {
        return cli_des;
    }

    public void setCli_des(String cli_des) {
        this.cli_des = cli_des;
    }

    public String getZon_des() {
        return zon_des;
    }

    public void setZon_des(String zon_des) {
        this.zon_des = zon_des;
    }

    public String getLp_des() {
        return lp_des;
    }

    public void setLp_des(String lp_des) {
        this.lp_des = lp_des;
    }

    public String getCond_des() {
        return cond_des;
    }

    public void setCond_des(String cond_des) {
        this.cond_des = cond_des;
    }

    public int getCon_dpla() {
        return con_dpla;
    }

    public void setCon_dpla(int con_dpla) {
        this.con_dpla = con_dpla;
    }

    public String getTip_ventades() {
        return tip_ventades;
    }

    public void setTip_ventades(String tip_ventades) {
        this.tip_ventades = tip_ventades;
    }

    public String getS_fecemi() {
        s_fecemi = sdf.format(pcab_fecemi);
        return s_fecemi;
    }

    public void setS_fecemi(String s_fecemi) {
        this.s_fecemi = s_fecemi;
    }

    public boolean isValor() {
        if (pcab_estado == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getS_totped() {
        s_totped = df2.format(pcab_totped);
        return s_totped;
    }

    public void setS_totped(String s_totped) {
        this.s_totped = s_totped;
    }

    public boolean isValortipcam() {
        if (pcab_modtipcam == 1) {
            valortipcam = true;
        } else {
            valortipcam = false;
        }
        return valortipcam;
    }

    public void setValortipcam(boolean valortipcam) {
        this.valortipcam = valortipcam;
    }

    public boolean isValSelec() {
        if (indicador == 1) {
            valortipcam = false;
        }
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getPcab_motrec_des() {
        return pcab_motrec_des;
    }

    public void setPcab_motrec_des(String pcab_motrec_des) {
        this.pcab_motrec_des = pcab_motrec_des;
    }

    public String getPcab_diavisdes() {
        return pcab_diavisdes;
    }

    public void setPcab_diavisdes(String pcab_diavisdes) {
        this.pcab_diavisdes = pcab_diavisdes;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public String getNotestip() {
        return notestip;
    }

    public void setNotestip(String notestip) {
        this.notestip = notestip;
    }

    public String getNotesdes() {
        return notesdes;
    }

    public void setNotesdes(String notesdes) {
        this.notesdes = notesdes;
    }

    public String getNotesnro() {
        return notesnro;
    }

    public void setNotesnro(String notesnro) {
        this.notesnro = notesnro;
    }

    public String getSup_des() {
        return sup_des;
    }

    public void setSup_des(String sup_des) {
        this.sup_des = sup_des;
    }

}
