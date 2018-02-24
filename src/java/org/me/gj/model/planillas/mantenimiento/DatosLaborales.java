/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ROJAS
 */
public class DatosLaborales {
    
    private int pltipdoc;
    private String plnrodoc;
    private int plcorrel;
    private int emp_id;
    private int suc_id;
    private int plestado;
    private String plcodemp;
    private String plarea;
    private String plccosto;
    private String plcatper;
    private String pltiptra;
    private String plhorari;
    private String plidcargo;
    private int pl_cc;
    private String pltipcont;
    private int plsujconinm;
    private int pltipsue;
    private int plperrem;
  //  private int plhextra;
    private int plputil;
    private int plquinta;
    private int plotr5ta;
    private int plippsvi;
    private String plcarssp;
    private int plsindic;
    private int plespens;
    private Date plfecing; 
    private Date plfecces;
    //private String plmotces;
    private String plcesemot;
    private String plcesedet;
    private String plceseobs;
    
    private String plusuadd;
    private Date plfecadd;
    private String plusumod;
    private Date plfecmod;
    
    //
    private String emp_id_des;
    private String plarea_des;
    private String plccosto_des;
    private String pltiptra_des;
    private String plhorari_des;
    private String plidcargo_des;
    private String pltipcont_des;
    private String plcesemot_des;
    //
    
    //Utilitarios
    private String indsql;
    private String s_plfecing;
    private String s_plfecces;
    
    SimpleDateFormat ffecha = new SimpleDateFormat("dd/MM/yyyy");

    public DatosLaborales() {
    }

    public DatosLaborales(/*int pltipdoc, String plnrodoc, int emp_id, */String indsql, int suc_id, String plcodemp, String plarea, 
            String plccosto, String plcatper, String pltiptra, String plhorari, String plidcargo, int pl_cc, 
            String pltipcont, int plsujconinm, int pltipsue, int plperrem, int plputil, int plquinta, 
            int plotr5ta, int plippsvi, String plcarssp, int plsindic, int plespens, Date plfecing, Date plfecces, 
            String plcesemot, String plcesedet, String plceseobs) {
        /*this.pltipdoc = pltipdoc;
        this.plnrodoc = plnrodoc;
        this.emp_id = emp_id;*/
        this.indsql = indsql;
        this.suc_id = suc_id;
        this.plcodemp = plcodemp;
        this.plarea = plarea;
        this.plccosto = plccosto;
        this.plcatper = plcatper;
        this.pltiptra = pltiptra;
        this.plhorari = plhorari;
        this.plidcargo = plidcargo;
        this.pl_cc = pl_cc;
        this.pltipcont = pltipcont;
        this.plsujconinm = plsujconinm;
        this.pltipsue = pltipsue;
        this.plperrem = plperrem;
        //this.plhextra = plhextra;
        this.plputil = plputil;
        this.plquinta = plquinta;
        this.plotr5ta = plotr5ta;
        this.plippsvi = plippsvi;
        this.plcarssp = plcarssp;
        this.plsindic = plsindic;
        this.plespens = plespens;
        this.plfecing = plfecing;
        this.plfecces = plfecces;
        this.plcesemot = plcesemot;
        this.plcesedet = plcesedet;
        this.plceseobs = plceseobs;
    }
    
    
    
    public int getPltipdoc() {
        return pltipdoc;
    }

    public void setPltipdoc(int pltipdoc) {
        this.pltipdoc = pltipdoc;
    }

    public String getPlnrodoc() {
        return plnrodoc;
    }

    public void setPlnrodoc(String plnrodoc) {
        this.plnrodoc = plnrodoc;
    }

    public int getPlcorrel() {
        return plcorrel;
    }

    public void setPlcorrel(int plcorrel) {
        this.plcorrel = plcorrel;
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

    public int getPlestado() {
        return plestado;
    }

    public void setPlestado(int plestado) {
        this.plestado = plestado;
    }

    public String getPlcodemp() {
        return plcodemp;
    }

    public void setPlcodemp(String plcodemp) {
        this.plcodemp = plcodemp;
    }

    public String getPlarea() {
        return plarea;
    }

    public void setPlarea(String plarea) {
        this.plarea = plarea;
    }

    public String getPlccosto() {
        return plccosto;
    }

    public void setPlccosto(String plccosto) {
        this.plccosto = plccosto;
    }

    public String getPlcatper() {
        return plcatper;
    }

    public void setPlcatper(String plcatper) {
        this.plcatper = plcatper;
    }

    public String getPltiptra() {
        return pltiptra;
    }

    public void setPltiptra(String pltiptra) {
        this.pltiptra = pltiptra;
    }

    public String getPlhorari() {
        return plhorari;
    }

    public void setPlhorari(String plhorari) {
        this.plhorari = plhorari;
    }

    public String getPlidcargo() {
        return plidcargo;
    }

    public void setPlidcargo(String plidcargo) {
        this.plidcargo = plidcargo;
    }

    public int getPl_cc() {
        return pl_cc;
    }

    public void setPl_cc(int pl_cc) {
        this.pl_cc = pl_cc;
    }

    public String getPltipcont() {
        return pltipcont;
    }

    public void setPltipcont(String pltipcont) {
        this.pltipcont = pltipcont;
    }

    public int getPlsujconinm() {
        return plsujconinm;
    }

    public void setPlsujconinm(int plsujconinm) {
        this.plsujconinm = plsujconinm;
    }

    public int getPltipsue() {
        return pltipsue;
    }

    public void setPltipsue(int pltipsue) {
        this.pltipsue = pltipsue;
    }

    public int getPlperrem() {
        return plperrem;
    }

    public void setPlperrem(int plperrem) {
        this.plperrem = plperrem;
    }

    /*public int getPlhextra() {
        return plhextra;
    }

    public void setPlhextra(int plhextra) {
        this.plhextra = plhextra;
    }*/

    public int getPlputil() {
        return plputil;
    }

    public void setPlputil(int plputil) {
        this.plputil = plputil;
    }

    public int getPlquinta() {
        return plquinta;
    }

    public void setPlquinta(int plquinta) {
        this.plquinta = plquinta;
    }

    public int getPlotr5ta() {
        return plotr5ta;
    }

    public void setPlotr5ta(int plotr5ta) {
        this.plotr5ta = plotr5ta;
    }

    public int getPlippsvi() {
        return plippsvi;
    }

    public void setPlippsvi(int plippsvi) {
        this.plippsvi = plippsvi;
    }

    public String getPlcarssp() {
        return plcarssp;
    }

    public void setPlcarssp(String plcarssp) {
        this.plcarssp = plcarssp;
    }

    public int getPlsindic() {
        return plsindic;
    }

    public void setPlsindic(int plsindic) {
        this.plsindic = plsindic;
    }

    public int getPlespens() {
        return plespens;
    }

    public void setPlespens(int plespens) {
        this.plespens = plespens;
    }

    public Date getPlfecing() {
        return plfecing;
    }

    public void setPlfecing(Date plfecing) {
        this.plfecing = plfecing;
    }

    public Date getPlfecces() {
        return plfecces;
    }

    public void setPlfecces(Date plfecces) {
        this.plfecces = plfecces;
    }

    public String getPlcesemot() {
        return plcesemot;
    }

    public void setPlcesemot(String plcesemot) {
        this.plcesemot = plcesemot;
    }

    public String getPlcesedet() {
        return plcesedet;
    }

    public void setPlcesedet(String plcesedet) {
        this.plcesedet = plcesedet;
    }

    public String getPlceseobs() {
        return plceseobs;
    }

    public void setPlceseobs(String plceseobs) {
        this.plceseobs = plceseobs;
    }

    /*public String getPlmotces() {
        return plmotces;
    }

    public void setPlmotces(String plmotces) {
        this.plmotces = plmotces;
    }*/

    public String getPlusuadd() {
        return plusuadd;
    }

    public void setPlusuadd(String plusuadd) {
        this.plusuadd = plusuadd;
    }

    public Date getPlfecadd() {
        return plfecadd;
    }

    public void setPlfecadd(Date plfecadd) {
        this.plfecadd = plfecadd;
    }

    public String getPlusumod() {
        return plusumod;
    }

    public void setPlusumod(String plusumod) {
        this.plusumod = plusumod;
    }

    public Date getPlfecmod() {
        return plfecmod;
    }

    public void setPlfecmod(Date plfecmod) {
        this.plfecmod = plfecmod;
    }

    public String getIndsql() {
        return indsql;
    }

    public void setIndsql(String indsql) {
        this.indsql = indsql;
    }

    public String getEmp_id_des() {
        return emp_id_des;
    }

    public void setEmp_id_des(String emp_id_des) {
        this.emp_id_des = emp_id_des;
    }
    
    public String getPlarea_des() {
        return plarea_des;
    }

    public void setPlarea_des(String plarea_des) {
        this.plarea_des = plarea_des;
    }

    public String getPlccosto_des() {
        return plccosto_des;
    }

    public void setPlccosto_des(String plccosto_des) {
        this.plccosto_des = plccosto_des;
    }

    public String getPltiptra_des() {
        return pltiptra_des;
    }

    public void setPltiptra_des(String pltiptra_des) {
        this.pltiptra_des = pltiptra_des;
    }

    public String getPlhorari_des() {
        return plhorari_des;
    }

    public void setPlhorari_des(String plhorari_des) {
        this.plhorari_des = plhorari_des;
    }

    public String getPlidcargo_des() {
        return plidcargo_des;
    }

    public void setPlidcargo_des(String plidcargo_des) {
        this.plidcargo_des = plidcargo_des;
    }

    public String getPltipcont_des() {
        return pltipcont_des;
    }

    public void setPltipcont_des(String pltipcont_des) {
        this.pltipcont_des = pltipcont_des;
    }

    public String getPlcesemot_des() {
        return plcesemot_des;
    }

    public void setPlcesemot_des(String plcesemot_des) {
        this.plcesemot_des = plcesemot_des;
    }    

    public String getS_plfecing() {
        s_plfecing = plfecing == null ? "" : ffecha.format(plfecing);
        return s_plfecing;
    }

    public void setS_plfecing(String s_plfecing) {
        this.s_plfecing = s_plfecing;
    }

    public String getS_plfecces() {
        s_plfecces = plfecces == null ? "" : ffecha.format(plfecces);
        return s_plfecces;
    }

    public void setS_plfecces(String s_plfecces) {
        this.s_plfecces = s_plfecces;
    }
    
    
}
