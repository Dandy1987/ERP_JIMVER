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
public class Personal {

    private int pltipdoc;
    private String plnrodoc;
    private int plestado;
    private String plapepat;
    private String plapemat;
    private String plnomemp;
    private String pldiremp;
    private String pldistri;
    private String pltelemp;
    private Date plfecnac;
    private String pltelemovil;
    private String plgruposangui;
    private int plsexo;
    private String plnacion;
    private String plestcivil;
    private String plnivedu;
    private int pldiscap;
    private String plemail;
    private String plidocup;

    //DOMICILIO
    private int plconddom;
    private String pldir_via;
    private String pldir_nomvia;
    private String pldir_num;
    private String pldir_int;
    private String pldir_zona;
    private String pldir_nomzona;
    private String pldir_refer;
    private String pldir_ubigeo;
    private double pldir_corx;
    private double pldir_cory;

    private String plusuadd;
    private Date plfecadd;
    private String plpcadd;
    private String plusumod;
    private Date plfecmod;
    private String plpcmod;
    private boolean valor;

    //DATOS LABORALES
    private int plcorrel;
    private int emp_id;
    private int suc_id;
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
   // private int plhextra;
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

    //DATOS APORTACION
    private String plregpen;
    private Date plfiregpen;
    private String pltippen;
    private String plcodafp;
    private String plcarafp;
    private int plcommix;
    private String plpressal;
    private String plsiteps;
    private Date plfbeps;
    private int plsct_as;
    private int plsct_pp;
    //String aportacion fecha
    private String plfiregpen_s;
    private String plfbeps_s;      

    //DATOS PAGO HABERES
    private int plcorrel_dep_h;
    private int plbanco_h;
    private int pltipcta_h;
    private int plmoneda_h;
    private String plnrocta_h;
    private int pltippago_h;
    private String plglosa_h;

    //DATOS PAGO CTS
    private int plcorrel_dep_c;
    private int plbanco_c;
    private int pltipcta_c;
    private int plmoneda_c;
    private String plnrocta_c;
    private int pltippago_c;
    private String plglosa_c;

    //UTILITARIOS
    private String s_plfecnac;
    private String s_plfecing;
    private String s_plfecces;

    //DESCRIPCIONES
    private String plnacion_des;
    private String plnivedu_des;
    private String plidocup_des;
    private String pldir_ubigeo_des;
    //
    private String plarea_des;
    private String plccosto_des;
    private String pltiptra_des;
    private String plhorari_des;
    private String plidcargo_des;
    private String pltipcont_des;
    //
    private String plregpen_des;
    private String plcodafp_des;
    private String plpressal_des;
    private String plsiteps_des;
    //
    private String plbanco_h_des;
    private String plbanco_c_des;
    private String suc_id_des;

    //PARA EL LOV PERSONAL
    private String plidper;
    private String pldesper;
    private String direccion;
    private String cargo;
    private Date fechaing;
    private String emp_des;
    private String emp_ruc;
    private String emp_repleg;
    private String emp_dnirep;
    
    //lov datos historicos
    private String moneda;
    private String tipo_pago;
    private String tipo_cuenta;
 //para centro de costo
    
    private String costo;
    private String descripcion_costo;
    SimpleDateFormat ffecha = new SimpleDateFormat("dd/MM/yyyy");

    private boolean valSelec;

    public Personal() {
    }

    public Personal(int pltipdoc, String plnrodoc, int plestado, String plapepat, String plapemat, String plnomemp, String pldiremp,
            String pltelemp, Date plfecnac, String pltelemovil, String plgruposangui, int plsexo, String plnacion, String plestcivil,
            String plnivedu, int pldiscap, int plconddom, String plemail, String plidocup, String pldir_via, String pldir_nomvia,
            String pldir_num, String pldir_int, String pldir_zona, String pldir_nomzona, String pldir_refer, String pldir_ubigeo,
            double pldir_corx, double pldir_cory) {
        this.pltipdoc = pltipdoc;
        this.plnrodoc = plnrodoc;
        this.plestado = plestado;
        this.plapepat = plapepat;
        this.plapemat = plapemat;
        this.plnomemp = plnomemp;
        this.pldiremp = pldiremp;
        this.pltelemp = pltelemp;
        this.plfecnac = plfecnac;
        this.pltelemovil = pltelemovil;
        this.plgruposangui = plgruposangui;
        this.plsexo = plsexo;
        this.plnacion = plnacion;
        this.plestcivil = plestcivil;
        this.plnivedu = plnivedu;
        this.pldiscap = pldiscap;
        this.plconddom = plconddom;
        this.plemail = plemail;
        this.plidocup = plidocup;
        this.pldir_via = pldir_via;
        this.pldir_nomvia = pldir_nomvia;
        this.pldir_num = pldir_num;
        this.pldir_int = pldir_int;
        this.pldir_zona = pldir_zona;
        this.pldir_nomzona = pldir_nomzona;
        this.pldir_refer = pldir_refer;
        this.pldir_ubigeo = pldir_ubigeo;
        this.pldir_corx = pldir_corx;
        this.pldir_cory = pldir_cory;
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

    public int getPlestado() {
        return plestado;
    }

    public void setPlestado(int plestado) {
        this.plestado = plestado;
    }

    public String getPlapepat() {
        return plapepat;
    }

    public void setPlapepat(String plapepat) {
        this.plapepat = plapepat;
    }

    public String getPlapemat() {
        return plapemat;
    }

    public void setPlapemat(String plapemat) {
        this.plapemat = plapemat;
    }

    public String getPlnomemp() {
        return plnomemp;
    }

    public void setPlnomemp(String plnomemp) {
        this.plnomemp = plnomemp;
    }

    public String getPldiremp() {
        return pldiremp;
    }

    public void setPldiremp(String pldiremp) {
        this.pldiremp = pldiremp;
    }

    public String getPldistri() {
        return pldistri;
    }

    public void setPldistri(String pldistri) {
        this.pldistri = pldistri;
    }

    public String getPltelemp() {
        return pltelemp;
    }

    public void setPltelemp(String pltelemp) {
        this.pltelemp = pltelemp;
    }

    public Date getPlfecnac() {
        return plfecnac;
    }

    public void setPlfecnac(Date plfecnac) {
        this.plfecnac = plfecnac;
    }

    public String getPltelemovil() {
        return pltelemovil;
    }

    public void setPltelemovil(String pltelemovil) {
        this.pltelemovil = pltelemovil;
    }

    public String getPlgruposangui() {
        return plgruposangui;
    }

    public void setPlgruposangui(String plgruposangui) {
        this.plgruposangui = plgruposangui;
    }

    public int getPlsexo() {
        return plsexo;
    }

    public void setPlsexo(int plsexo) {
        this.plsexo = plsexo;
    }

    public String getPlnacion() {
        return plnacion;
    }

    public void setPlnacion(String plnacion) {
        this.plnacion = plnacion;
    }

    public String getPlestcivil() {
        return plestcivil;
    }

    public void setPlestcivil(String plestcivil) {
        this.plestcivil = plestcivil;
    }

    public String getPlnivedu() {
        return plnivedu;
    }

    public void setPlnivedu(String plnivedu) {
        this.plnivedu = plnivedu;
    }

    public int getPldiscap() {
        return pldiscap;
    }

    public void setPldiscap(int pldiscap) {
        this.pldiscap = pldiscap;
    }

    public int getPlconddom() {
        return plconddom;
    }

    public void setPlconddom(int plconddom) {
        this.plconddom = plconddom;
    }

    public String getPlemail() {
        return plemail;
    }

    public void setPlemail(String plemail) {
        this.plemail = plemail;
    }

    public String getPlidocup() {
        return plidocup;
    }

    public void setPlidocup(String plidocup) {
        this.plidocup = plidocup;
    }

    public String getPldir_via() {
        return pldir_via;
    }

    public void setPldir_via(String pldir_via) {
        this.pldir_via = pldir_via;
    }

    public String getPldir_nomvia() {
        return pldir_nomvia;
    }

    public void setPldir_nomvia(String pldir_nomvia) {
        this.pldir_nomvia = pldir_nomvia;
    }

    public String getPldir_num() {
        return pldir_num;
    }

    public void setPldir_num(String pldir_num) {
        this.pldir_num = pldir_num;
    }

    public String getPldir_int() {
        return pldir_int;
    }

    public void setPldir_int(String pldir_int) {
        this.pldir_int = pldir_int;
    }

    public String getPldir_zona() {
        return pldir_zona;
    }

    public void setPldir_zona(String pldir_zona) {
        this.pldir_zona = pldir_zona;
    }

    public String getPldir_nomzona() {
        return pldir_nomzona;
    }

    public void setPldir_nomzona(String pldir_nomzona) {
        this.pldir_nomzona = pldir_nomzona;
    }

    public String getPldir_refer() {
        return pldir_refer;
    }

    public void setPldir_refer(String pldir_refer) {
        this.pldir_refer = pldir_refer;
    }

    public String getPldir_ubigeo() {
        return pldir_ubigeo;
    }

    public void setPldir_ubigeo(String pldir_ubigeo) {
        this.pldir_ubigeo = pldir_ubigeo;
    }

    public double getPldir_corx() {
        return pldir_corx;
    }

    public void setPldir_corx(double pldir_corx) {
        this.pldir_corx = pldir_corx;
    }

    public double getPldir_cory() {
        return pldir_cory;
    }

    public void setPldir_cory(double pldir_cory) {
        this.pldir_cory = pldir_cory;
    }

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

    public String getPlpcadd() {
        return plpcadd;
    }

    public void setPlpcadd(String plpcadd) {
        this.plpcadd = plpcadd;
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

    public String getPlpcmod() {
        return plpcmod;
    }

    public void setPlpcmod(String plpcmod) {
        this.plpcmod = plpcmod;
    }

    public boolean isValor() {
        valor = plestado == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPlfiregpen_s() {
        plfiregpen_s =plfiregpen == null ? "" : ffecha.format(plfiregpen);
        return plfiregpen_s;
    }

    public void setPlfiregpen_s(String plfiregpen_s) {
        this.plfiregpen_s = plfiregpen_s;
    }

    public String getPlfbeps_s() {
       plfbeps_s = plfbeps == null ? "" : ffecha.format(plfbeps);
        return plfbeps_s;
    }

    public void setPlfbeps_s(String plfbeps_s) {
        this.plfbeps_s = plfbeps_s;
    }
    

    public String getS_plfecnac() {
        s_plfecnac = ffecha.format(plfecnac);
        return s_plfecnac;
    }

    public void setS_plfecnac(String s_plfecnac) {
        this.s_plfecnac = s_plfecnac;
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

    /**
     * **************************************************
     ********************DATOS LABORALES******************
     * ***************************************************
     */
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

   /* public int getPlhextra() {
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
    /**
     * **************************************************
     ********************DATOS APORTACIONES******************
     * ***************************************************
     */
    public String getPlregpen() {
        return plregpen;
    }

    public void setPlregpen(String plregpen) {
        this.plregpen = plregpen;
    }

    public Date getPlfiregpen() {
        return plfiregpen;
    }

    public void setPlfiregpen(Date plfiregpen) {
        this.plfiregpen = plfiregpen;
    }

    public String getPltippen() {
        return pltippen;
    }

    public void setPltippen(String pltippen) {
        this.pltippen = pltippen;
    }

    public String getPlcodafp() {
        return plcodafp;
    }

    public void setPlcodafp(String plcodafp) {
        this.plcodafp = plcodafp;
    }

    public String getPlcarafp() {
        return plcarafp;
    }

    public void setPlcarafp(String plcarafp) {
        this.plcarafp = plcarafp;
    }

    public int getPlcommix() {
        return plcommix;
    }

    public void setPlcommix(int plcommix) {
        this.plcommix = plcommix;
    }

    public String getPlpressal() {
        return plpressal;
    }

    public void setPlpressal(String plpressal) {
        this.plpressal = plpressal;
    }

    public String getPlsiteps() {
        return plsiteps;
    }

    public void setPlsiteps(String plsiteps) {
        this.plsiteps = plsiteps;
    }

    public Date getPlfbeps() {
        return plfbeps;
    }

    public void setPlfbeps(Date plfbeps) {
        this.plfbeps = plfbeps;
    }

    public int getPlsct_as() {
        return plsct_as;
    }

    public void setPlsct_as(int plsct_as) {
        this.plsct_as = plsct_as;
    }

    public int getPlsct_pp() {
        return plsct_pp;
    }

    public void setPlsct_pp(int plsct_pp) {
        this.plsct_pp = plsct_pp;
    }

    /**
     * **************************************************
     ********************DATOS PAGO HABERES ******************
     * ***************************************************
     */
    public int getPlcorrel_dep_h() {
        return plcorrel_dep_h;
    }

    public void setPlcorrel_dep_h(int plcorrel_dep_h) {
        this.plcorrel_dep_h = plcorrel_dep_h;
    }

    public int getPlbanco_h() {
        return plbanco_h;
    }

    public void setPlbanco_h(int plbanco_h) {
        this.plbanco_h = plbanco_h;
    }

    public int getPltipcta_h() {
        return pltipcta_h;
    }

    public void setPltipcta_h(int pltipcta_h) {
        this.pltipcta_h = pltipcta_h;
    }

    public int getPlmoneda_h() {
        return plmoneda_h;
    }

    public void setPlmoneda_h(int plmoneda_h) {
        this.plmoneda_h = plmoneda_h;
    }

    public String getPlnrocta_h() {
        return plnrocta_h;
    }

    public void setPlnrocta_h(String plnrocta_h) {
        this.plnrocta_h = plnrocta_h;
    }

    public int getPltippago_h() {
        return pltippago_h;
    }

    public void setPltippago_h(int pltippago_h) {
        this.pltippago_h = pltippago_h;
    }

    public String getPlglosa_h() {
        return plglosa_h;
    }

    public void setPlglosa_h(String plglosa_h) {
        this.plglosa_h = plglosa_h;
    }

    /**
     * **************************************************
     ********************DATOS PAGO CTS ******************
     * ***************************************************
     */
    public int getPlcorrel_dep_c() {
        return plcorrel_dep_c;
    }

    public void setPlcorrel_dep_c(int plcorrel_dep_c) {
        this.plcorrel_dep_c = plcorrel_dep_c;
    }

    public int getPlbanco_c() {
        return plbanco_c;
    }

    public void setPlbanco_c(int plbanco_c) {
        this.plbanco_c = plbanco_c;
    }

    public int getPltipcta_c() {
        return pltipcta_c;
    }

    public void setPltipcta_c(int pltipcta_c) {
        this.pltipcta_c = pltipcta_c;
    }

    public int getPlmoneda_c() {
        return plmoneda_c;
    }

    public void setPlmoneda_c(int plmoneda_c) {
        this.plmoneda_c = plmoneda_c;
    }

    public String getPlnrocta_c() {
        return plnrocta_c;
    }

    public void setPlnrocta_c(String plnrocta_c) {
        this.plnrocta_c = plnrocta_c;
    }

    public int getPltippago_c() {
        return pltippago_c;
    }

    public void setPltippago_c(int pltippago_c) {
        this.pltippago_c = pltippago_c;
    }

    public String getPlglosa_c() {
        return plglosa_c;
    }

    public void setPlglosa_c(String plglosa_c) {
        this.plglosa_c = plglosa_c;
    }

    //DESCRIPCIONES
    public String getPlnacion_des() {
        return plnacion_des;
    }

    public void setPlnacion_des(String plnacion_des) {
        this.plnacion_des = plnacion_des;
    }

    public String getPlnivedu_des() {
        return plnivedu_des;
    }

    public void setPlnivedu_des(String plnivedu_des) {
        this.plnivedu_des = plnivedu_des;
    }

    public String getPlidocup_des() {
        return plidocup_des;
    }

    public void setPlidocup_des(String plidocup_des) {
        this.plidocup_des = plidocup_des;
    }

    public String getPldir_ubigeo_des() {
        return pldir_ubigeo_des;
    }

    public void setPldir_ubigeo_des(String pldir_ubigeo_des) {
        this.pldir_ubigeo_des = pldir_ubigeo_des;
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

    public String getPlregpen_des() {
        return plregpen_des;
    }

    public void setPlregpen_des(String plregpen_des) {
        this.plregpen_des = plregpen_des;
    }

    public String getPlcodafp_des() {
        return plcodafp_des;
    }

    public void setPlcodafp_des(String plcodafp_des) {
        this.plcodafp_des = plcodafp_des;
    }

    public String getPlpressal_des() {
        return plpressal_des;
    }

    public void setPlpressal_des(String plpressal_des) {
        this.plpressal_des = plpressal_des;
    }

    public String getPlsiteps_des() {
        return plsiteps_des;
    }

    public void setPlsiteps_des(String plsiteps_des) {
        this.plsiteps_des = plsiteps_des;
    }

    public String getPlbanco_h_des() {
        return plbanco_h_des;
    }

    public void setPlbanco_h_des(String plbanco_h_des) {
        this.plbanco_h_des = plbanco_h_des;
    }

    public String getPlbanco_c_des() {
        return plbanco_c_des;
    }

    public void setPlbanco_c_des(String plbanco_c_des) {
        this.plbanco_c_des = plbanco_c_des;
    }

    public String getSuc_id_des() {
        return suc_id_des;
    }

    public void setSuc_id_des(String suc_id_des) {
        this.suc_id_des = suc_id_des;
    }
//LOV PERSONAL

    public String getPlidper() {
        return plidper;
    }

    public void setPlidper(String plidper) {
        this.plidper = plidper;
    }

    public String getPldesper() {
        return pldesper;
    }

    public void setPldesper(String pldesper) {
        this.pldesper = pldesper;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFechaing() {
        return fechaing;
    }

    public void setFechaing(Date fechaing) {
        this.fechaing = fechaing;
    }

    public String getEmp_des() {
        return emp_des;
    }

    public void setEmp_des(String emp_des) {
        this.emp_des = emp_des;
    }

    public String getEmp_ruc() {
        return emp_ruc;
    }

    public void setEmp_ruc(String emp_ruc) {
        this.emp_ruc = emp_ruc;
    }

    public String getEmp_repleg() {
        return emp_repleg;
    }

    public void setEmp_repleg(String emp_repleg) {
        this.emp_repleg = emp_repleg;
    }

    public String getEmp_dnirep() {
        return emp_dnirep;
    }

    public void setEmp_dnirep(String emp_dnirep) {
        this.emp_dnirep = emp_dnirep;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public int compareTo(Personal o) {
        String a = String.valueOf(o.getPlapepat());
        String b = String.valueOf(o.getPlapepat());
        return new Integer(a.compareTo(b));
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getTipo_cuenta() {
        return tipo_cuenta;
    }

    public void setTipo_cuenta(String tipo_cuenta) {
        this.tipo_cuenta = tipo_cuenta;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getDescripcion_costo() {
        return descripcion_costo;
    }

    public void setDescripcion_costo(String descripcion_costo) {
        this.descripcion_costo = descripcion_costo;
    }
    

}
