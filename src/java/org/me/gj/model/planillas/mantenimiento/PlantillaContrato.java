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
 * @author ROJAS
 */
public class PlantillaContrato {

    private String id_contrato;
    private int emp_id;
    private int ct_estado;
    private String ct_tipo;
    private String par01_empnom;
    private String par02_empruc;
    private String par03_empdom;
    private String par04_empxxx;
    //private String par05_empxxx;
    private int par06_pertipdoc;
    private String par07_pernumdoc;
    private String par08_pernom;
    private String par09_perdir;
    private String par10_percargo;
    //private String par11_perxxx;
    //private String par12_perxxx;
    private int par13_conmeses;
    private Date par14_confecini;
    private Date par15_confecfin;
    private int par16_conremu;
    private String par17_conact;
    //private String par18_conxxx;
    //private String par19_conxxx;
    //private String par20_conxxx;

    //representante legal
    private String replegal;
    private String dnireplegal;
    //descripcion de contrato
    private String desct_tipo;
    private String ct_usuadd;
    private Date ct_fecadd;
    private String ct_usumod;
    private Date ct_fecmod;

    private boolean valor;

    //formato de fecha
    private String spar14_confecini;
    private String spar15_confecfin;
    
    private int dias_faltantes;

    //formato
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public PlantillaContrato() {
    }

    public PlantillaContrato(String id_contrato, int ct_estado, String ct_tipo, String par01_empnom, String par02_empruc, String par03_empdom,
            /*int par06_pertipdoc,*/ String par07_pernumdoc, String par08_pernom, String par09_perdir, String par10_percargo, int par13_conmeses,
            Date par14_confecini, Date par15_confecfin, int par16_conremu, String par17_conact, String par04_empxxx, String ct_usumod, int emp_id) {
        this.id_contrato = id_contrato;
        this.ct_estado = ct_estado;
        this.ct_tipo = ct_tipo;
        this.par01_empnom = par01_empnom;
        this.par02_empruc = par02_empruc;
        this.par03_empdom = par03_empdom;
        //this.par06_pertipdoc = par06_pertipdoc;
        this.par07_pernumdoc = par07_pernumdoc;
        this.par08_pernom = par08_pernom;
        this.par09_perdir = par09_perdir;
        this.par10_percargo = par10_percargo;
        this.par13_conmeses = par13_conmeses;
        this.par14_confecini = par14_confecini;
        this.par15_confecfin = par15_confecfin;
        this.par16_conremu = par16_conremu;
        this.par17_conact = par17_conact;
        this.par04_empxxx = par04_empxxx;
        this.ct_usumod = ct_usumod;
        this.emp_id = emp_id;
    }

    public PlantillaContrato(String id_contrato, int ct_estado, String ct_tipo, String par01_empnom, String par02_empruc, String par03_empdom,
            int par06_pertipdoc, String par07_pernumdoc, String par08_pernom, String par09_perdir, String par10_percargo, int par13_conmeses,
            Date par14_confecini, Date par15_confecfin, int par16_conremu, String par17_conact, String par04_empxxx, String ct_usumod, int emp_id) {
        this.id_contrato = id_contrato;
        this.ct_estado = ct_estado;
        this.ct_tipo = ct_tipo;
        this.par01_empnom = par01_empnom;
        this.par02_empruc = par02_empruc;
        this.par03_empdom = par03_empdom;
        this.par06_pertipdoc = par06_pertipdoc;
        this.par07_pernumdoc = par07_pernumdoc;
        this.par08_pernom = par08_pernom;
        this.par09_perdir = par09_perdir;
        this.par10_percargo = par10_percargo;
        this.par13_conmeses = par13_conmeses;
        this.par14_confecini = par14_confecini;
        this.par15_confecfin = par15_confecfin;
        this.par16_conremu = par16_conremu;
        this.par17_conact = par17_conact;
        this.par04_empxxx = par04_empxxx;
        this.ct_usumod = ct_usumod;
        this.emp_id = emp_id;
    }
    /* public PlantillaContrato(String id_contrato, int ct_estado,String par01_empnom, String par02_empruc, String par03_empdom,
     int par06_pertipdoc, String par07_pernumdoc, String par08_pernom, String par09_perdir, String par10_percargo, int par13_conmeses,
     Date par14_confecini, Date par15_confecfin, int par16_conremu, String par17_conact,String par04_empxxx) {
     this.id_contrato = id_contrato;
     this.ct_estado = ct_estado;
     this.ct_tipo = ct_tipo;
     this.par01_empnom = par01_empnom;
     this.par02_empruc = par02_empruc;
     this.par03_empdom = par03_empdom;
     this.par07_pernumdoc = par07_pernumdoc;
     this.par08_pernom = par08_pernom;
     this.par09_perdir = par09_perdir;
     this.par10_percargo = par10_percargo;
     this.par13_conmeses = par13_conmeses;
     this.par14_confecini = par14_confecini;
     this.par15_confecfin = par15_confecfin;
     this.par16_conremu = par16_conremu;
     this.par17_conact = par17_conact;
     this.par04_empxxx = par04_empxxx;
        
     }*/

    public String getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(String id_contrato) {
        this.id_contrato = id_contrato;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getCt_estado() {
        return ct_estado;
    }

    public void setCt_estado(int ct_estado) {
        this.ct_estado = ct_estado;
    }

    public String getCt_tipo() {
        return ct_tipo;
    }

    public void setCt_tipo(String ct_tipo) {
        this.ct_tipo = ct_tipo;
    }

    public String getPar01_empnom() {
        return par01_empnom;
    }

    public void setPar01_empnom(String par01_empnom) {
        this.par01_empnom = par01_empnom;
    }

    public String getPar02_empruc() {
        return par02_empruc;
    }

    public void setPar02_empruc(String par02_empruc) {
        this.par02_empruc = par02_empruc;
    }

    public String getPar03_empdom() {
        return par03_empdom;
    }

    public void setPar03_empdom(String par03_empdom) {
        this.par03_empdom = par03_empdom;
    }

    public String getPar04_empxxx() {
        return par04_empxxx;
    }

    public void setPar04_empxxx(String par04_empxxx) {
        this.par04_empxxx = par04_empxxx;
    }

    public int getPar06_pertipdoc() {
        return par06_pertipdoc;
    }

    public void setPar06_pertipdoc(int par06_pertipdoc) {
        this.par06_pertipdoc = par06_pertipdoc;
    }

    public String getPar07_pernumdoc() {
        return par07_pernumdoc;
    }

    public void setPar07_pernumdoc(String par07_pernumdoc) {
        this.par07_pernumdoc = par07_pernumdoc;
    }

    public String getPar08_pernom() {
        return par08_pernom;
    }

    public void setPar08_pernom(String par08_pernom) {
        this.par08_pernom = par08_pernom;
    }

    public String getPar09_perdir() {
        return par09_perdir;
    }

    public void setPar09_perdir(String par09_perdir) {
        this.par09_perdir = par09_perdir;
    }

    public String getPar10_percargo() {
        return par10_percargo;
    }

    public void setPar10_percargo(String par10_percargo) {
        this.par10_percargo = par10_percargo;
    }

    public int getPar13_conmeses() {
        return par13_conmeses;
    }

    public void setPar13_conmeses(int par13_conmeses) {
        this.par13_conmeses = par13_conmeses;
    }

    public Date getPar14_confecini() {
        return par14_confecini;
    }

    public void setPar14_confecini(Date par14_confecini) {
        this.par14_confecini = par14_confecini;
    }

    public Date getPar15_confecfin() {
        return par15_confecfin;
    }

    public void setPar15_confecfin(Date par15_confecfin) {
        this.par15_confecfin = par15_confecfin;
    }

    public int getPar16_conremu() {
        return par16_conremu;
    }

    public void setPar16_conremu(int par16_conremu) {
        this.par16_conremu = par16_conremu;
    }

    public String getPar17_conact() {
        return par17_conact;
    }

    public void setPar17_conact(String par17_conact) {
        this.par17_conact = par17_conact;
    }

    public String getCt_usuadd() {
        return ct_usuadd;
    }

    public void setCt_usuadd(String ct_usuadd) {
        this.ct_usuadd = ct_usuadd;
    }

    public Date getCt_fecadd() {
        return ct_fecadd;
    }

    public void setCt_fecadd(Date ct_fecadd) {
        this.ct_fecadd = ct_fecadd;
    }

    public String getCt_usumod() {
        return ct_usumod;
    }

    public void setCt_usumod(String ct_usumod) {
        this.ct_usumod = ct_usumod;
    }

    public Date getCt_fecmod() {
        return ct_fecmod;
    }

    public void setCt_fecmod(Date ct_fecmod) {
        this.ct_fecmod = ct_fecmod;
    }

    public boolean isValor() {
        valor = ct_estado == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getSpar14_confecini() {
        spar14_confecini = sdf.format(par14_confecini);
        return spar14_confecini;
    }

    public void setSpar14_confecini(String spar14_confecini) {
        this.spar14_confecini = spar14_confecini;
    }

    public String getSpar15_confecfin() {
        spar15_confecfin = sdf.format(par15_confecfin);
        return spar15_confecfin;
    }

    public void setSpar15_confecfin(String spar15_confecfin) {
        this.spar15_confecfin = spar15_confecfin;
    }

    public String getReplegal() {
        return replegal;
    }

    public void setReplegal(String replegal) {
        this.replegal = replegal;
    }

    public String getDnireplegal() {
        return dnireplegal;
    }

    public void setDnireplegal(String dnireplegal) {
        this.dnireplegal = dnireplegal;
    }

    public String getDesct_tipo() {
        return desct_tipo;
    }

    public void setDesct_tipo(String desct_tipo) {
        this.desct_tipo = desct_tipo;
    }

    public int getDias_faltantes() {
        return dias_faltantes;
    }

    public void setDias_faltantes(int dias_faltantes) {
        this.dias_faltantes = dias_faltantes;
    }

}
