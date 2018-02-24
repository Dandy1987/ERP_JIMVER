package org.me.gj.model.cxc.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TipoCambio {

    private int tcam_key;
    private String tcam_id;
    private Date dia_fec;
    private int tab_cod;
    private int tab_id;
    private String moneda;
    private int tcam_est;
    private double tcam_com;
    private String tcam_scom;
    private double tcam_conv;
    private String tcam_sconv;
    private double tcam_conc;
    private String tcam_sconc;
    private String tcam_usuadd;
    private Date tcam_fecadd;
    private String tcam_usumod;
    private Date tcam_fecmod;
    private boolean valor;

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df = new DecimalFormat("#0.000", dfs);

    public TipoCambio(int tcam_key, String tcam_id, Date dia_fec, int tab_cod, int tab_id, int tcam_est, double tcam_com, double tcam_conv, double tcam_conc, String tcam_usuadd, Date tcam_fecadd, String tcam_usumod, Date tcam_fecmod) {
        this.tcam_key = tcam_key;
        this.tcam_id = tcam_id;
        this.dia_fec = dia_fec;
        this.tab_cod = tab_cod;
        this.tab_id = tab_id;
        this.tcam_est = tcam_est;
        this.tcam_com = tcam_com;
        this.tcam_conv = tcam_conv;
        this.tcam_conc = tcam_conc;
        this.tcam_usuadd = tcam_usuadd;
        this.tcam_fecadd = tcam_fecadd;
        this.tcam_usumod = tcam_usumod;
        this.tcam_fecmod = tcam_fecmod;
    }

    public TipoCambio(String tcam_id, Date dia_fec, int tab_id, int tcam_est, double tcam_com, double tcam_conv, double tcam_conc, String tcam_usuadd) {
        this.tcam_id = tcam_id;
        this.dia_fec = dia_fec;
        this.tab_id = tab_id;
        this.tcam_est = tcam_est;
        this.tcam_com = tcam_com;
        this.tcam_conv = tcam_conv;
        this.tcam_conc = tcam_conc;
        this.tcam_usuadd = tcam_usuadd;
    }

//    public TipoCambio(int tcam_key, String tcam_id, Date dia_fec, int tab_id, int tcam_est, double tcam_com, double tcam_conv, double tcam_conc, String tcam_usumod) {
    public TipoCambio(int tcam_key, String tcam_id, Date dia_fec, int tab_id, int tcam_est, double tcam_com, double tcam_conv, double tcam_conc, String tcam_usuadd) {
        this.tcam_key = tcam_key;
        this.tcam_id = tcam_id;
        this.dia_fec = dia_fec;
        this.tab_id = tab_id;
        this.tcam_est = tcam_est;
        this.tcam_com = tcam_com;
        this.tcam_conv = tcam_conv;
        this.tcam_conc = tcam_conc;
        this.tcam_usuadd = tcam_usuadd;
    }

    public TipoCambio() {
    }

    public int getTcam_key() {
        return tcam_key;
    }

    public void setTcam_key(int tcam_key) {
        this.tcam_key = tcam_key;
    }

    public String getTcam_id() {
        return tcam_id;
    }

    public void setTcam_id(String tcam_id) {
        this.tcam_id = tcam_id;
    }

    public Date getDia_fec() {
        return dia_fec;
    }

    public void setDia_fec(Date dia_fec) {
        this.dia_fec = dia_fec;
    }

    public int getTab_cod() {
        return tab_cod;
    }

    public void setTab_cod(int tab_cod) {
        this.tab_cod = tab_cod;
    }

    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public int getTcam_est() {
        return tcam_est;
    }

    public void setTcam_est(int tcam_est) {
        this.tcam_est = tcam_est;
    }

    public double getTcam_com() {
        return tcam_com;
    }

    public void setTcam_com(double tcam_com) {
        this.tcam_com = tcam_com;
    }

    public String getTcam_scom() { // 01

        tcam_scom = df.format(tcam_com);
        return tcam_scom;
    }

    public void setTcam_scom(String tcam_scom) {
        this.tcam_scom = tcam_scom;
    }

    public double getTcam_conv() {
        return tcam_conv;
    }

    public void setTcam_conv(double tcam_conv) {
        this.tcam_conv = tcam_conv;
    }

    public String getTcam_sconv() { // 02
        tcam_sconv = df.format(tcam_conv);
        return tcam_sconv;
    }

    public void setTcam_sconv(String tcam_sconv) {
        this.tcam_sconv = tcam_sconv;
    }

    public double getTcam_conc() {
        return tcam_conc;
    }

    public void setTcam_conc(double tcam_conc) {
        this.tcam_conc = tcam_conc;
    }

    public String getTcam_sconc() { // 03
        tcam_sconc = df.format(tcam_conc);
        return tcam_sconc;
    }

    public void setTcam_sconc(String tcam_sconc) {
        this.tcam_sconc = tcam_sconc;
    }

    public String getTcam_usuadd() {
        return tcam_usuadd;
    }

    public void setTcam_usuadd(String tcam_usuadd) {
        this.tcam_usuadd = tcam_usuadd;
    }

    public Date getTcam_fecadd() {
        return tcam_fecadd;
    }

    public void setTcam_fecadd(Date tcam_fecadd) {
        this.tcam_fecadd = tcam_fecadd;
    }

    public String getTcam_usumod() {
        return tcam_usumod;
    }

    public void setTcam_usumod(String tcam_usumod) {
        this.tcam_usumod = tcam_usumod;
    }

    public Date getTcam_fecmod() {
        return tcam_fecmod;
    }

    public void setTcam_fecmod(Date tcam_fecmod) {
        this.tcam_fecmod = tcam_fecmod;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public boolean isValor() {
        if (tcam_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getFecha_cambio() {
        String fecha_cadena = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(dia_fec);
        return fecha_cadena;
    }
}
