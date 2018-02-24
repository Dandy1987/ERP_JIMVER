/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author greyes
 */
public class ManPresPer {

    private int EMP_ID;
    private int SUC_ID;
    private String TPLPRESCAB_ID;
    private String TPLPRESCAB_IDAREA;
    private String TPLPRESCAB_DESAREA;
    private String TPLPRESCAB_IDPER;
    private String TPLPRESCAB_DESPER;
    private Date TPLPRESCAB_FECEMI;
    private String TPLPRESCAB_USUADD;
    private Date TPLPRESCAB_FECADD;
    private String TPLPRESCAB_USUMOD;
    private Date TPLPRESCAB_FECMOD;
    private String TPLPRESCAB_NRODOC;
    private int TPLPRESCAB_TIPDOC;
    private String tplPersonal;
    private String tplArea_des;
    private int TPLPRESCAB_ESTADO;
    private double tlplprescab_monto;
    private int tlplprescab_nrocuo;
    
    private boolean valor;
    private boolean valSelec;
    
    //formato
    private String STPLPRESCAB_FECEMI;

    public String getSTPLPRESCAB_FECEMI() {
        STPLPRESCAB_FECEMI = sdf.format(TPLPRESCAB_FECEMI);
        return STPLPRESCAB_FECEMI;
    }

    public void setSTPLPRESCAB_FECEMI(String STPLPRESCAB_FECEMI) {
        this.STPLPRESCAB_FECEMI = STPLPRESCAB_FECEMI;
    }

    //formato
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public String getTPLPRESCAB_DESAREA() {
        return TPLPRESCAB_DESAREA;
    }

    public void setTPLPRESCAB_DESAREA(String TPLPRESCAB_DESAREA) {
        this.TPLPRESCAB_DESAREA = TPLPRESCAB_DESAREA;
    }

    public String getTPLPRESCAB_IDPER() {
        return TPLPRESCAB_IDPER;
    }

    public void setTPLPRESCAB_IDPER(String TPLPRESCAB_IDPER) {
        this.TPLPRESCAB_IDPER = TPLPRESCAB_IDPER;
    }

    public String getTPLPRESCAB_DESPER() {
        return TPLPRESCAB_DESPER;
    }

    public void setTPLPRESCAB_DESPER(String TPLPRESCAB_DESPER) {
        this.TPLPRESCAB_DESPER = TPLPRESCAB_DESPER;
    }

    public String getTplArea_des() {
        return tplArea_des;
    }

    public void setTplArea_des(String tplArea_des) {
        this.tplArea_des = tplArea_des;
    }

    public String getTplPersonal() {
        return tplPersonal;
    }

    public void setTplPersonal(String tplPersonal) {
        this.tplPersonal = tplPersonal;
    }

    public int getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(int EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public int getSUC_ID() {
        return SUC_ID;
    }

    public void setSUC_ID(int SUC_ID) {
        this.SUC_ID = SUC_ID;
    }

    public String getTPLPRESCAB_ID() {
        return TPLPRESCAB_ID;
    }

    public void setTPLPRESCAB_ID(String TPLPRESCAB_ID) {
        this.TPLPRESCAB_ID = TPLPRESCAB_ID;
    }

    public String getTPLPRESCAB_IDAREA() {
        return TPLPRESCAB_IDAREA;
    }

    public void setTPLPRESCAB_IDAREA(String TPLPRESCAB_IDAREA) {
        this.TPLPRESCAB_IDAREA = TPLPRESCAB_IDAREA;
    }

    public Date getTPLPRESCAB_FECEMI() {
        return TPLPRESCAB_FECEMI;
    }

    public void setTPLPRESCAB_FECEMI(Date TPLPRESCAB_FECEMI) {
        this.TPLPRESCAB_FECEMI = TPLPRESCAB_FECEMI;
    }

    public String getTPLPRESCAB_USUADD() {
        return TPLPRESCAB_USUADD;
    }

    public void setTPLPRESCAB_USUADD(String TPLPRESCAB_USUADD) {
        this.TPLPRESCAB_USUADD = TPLPRESCAB_USUADD;
    }

    public Date getTPLPRESCAB_FECADD() {
        return TPLPRESCAB_FECADD;
    }

    public void setTPLPRESCAB_FECADD(Date TPLPRESCAB_FECADD) {
        this.TPLPRESCAB_FECADD = TPLPRESCAB_FECADD;
    }

    public String getTPLPRESCAB_USUMOD() {
        return TPLPRESCAB_USUMOD;
    }

    public void setTPLPRESCAB_USUMOD(String TPLPRESCAB_USUMOD) {
        this.TPLPRESCAB_USUMOD = TPLPRESCAB_USUMOD;
    }

    public Date getTPLPRESCAB_FECMOD() {
        return TPLPRESCAB_FECMOD;
    }

    public void setTPLPRESCAB_FECMOD(Date TPLPRESCAB_FECMOD) {
        this.TPLPRESCAB_FECMOD = TPLPRESCAB_FECMOD;
    }

    public String getTPLPRESCAB_NRODOC() {
        return TPLPRESCAB_NRODOC;
    }

    public void setTPLPRESCAB_NRODOC(String TPLPRESCAB_NRODOC) {
        this.TPLPRESCAB_NRODOC = TPLPRESCAB_NRODOC;
    }

    public int getTPLPRESCAB_TIPDOC() {
        return TPLPRESCAB_TIPDOC;
    }

    public void setTPLPRESCAB_TIPDOC(int TPLPRESCAB_TIPDOC) {
        this.TPLPRESCAB_TIPDOC = TPLPRESCAB_TIPDOC;
    }

    public int getTPLPRESCAB_ESTADO() {
        return TPLPRESCAB_ESTADO;
    }

    public void setTPLPRESCAB_ESTADO(int TPLPRESCAB_ESTADO) {
        this.TPLPRESCAB_ESTADO = TPLPRESCAB_ESTADO;
    }

    public boolean isValor() {
        valor = TPLPRESCAB_ESTADO == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public double getTlplprescab_monto() {
        return tlplprescab_monto;
    }

    public void setTlplprescab_monto(double tlplprescab_monto) {
        this.tlplprescab_monto = tlplprescab_monto;
    }

    public int getTlplprescab_nrocuo() {
        return tlplprescab_nrocuo;
    }

    public void setTlplprescab_nrocuo(int tlplprescab_nrocuo) {
        this.tlplprescab_nrocuo = tlplprescab_nrocuo;
    }
    
    

}
