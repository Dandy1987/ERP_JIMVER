/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author greyes
 */
public class AsistenciaGen {

    //formato
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEEEEEEE");

    private int emp_id;
    private int suc_id;
    private int suc_des;
    private String periodo;
    private int pltipdoc;
    private String plnrodoc;
    private String id_per;
    private String des_per;
    private String plcodhor;
    private String deshor;
    private String plas_usuadd;
    private Date plas_fecadd;
    private String plas_usumod;
    private Date plas_fecmod;
    private String id_area;
    private String des_area;
    private String per_cargo;
    private Date per_fecing;
    private Date per_fecces;
    private Date fec_asis;
    private String sfec_asis;
    private String sfec_asisdias;
    private String mes;

    //DIAS DE ASISTECIA
    private String pldia01;
    private String pldia02;
    private String pldia03;
    private String pldia04;
    private String pldia05;
    private String pldia06;
    private String pldia07;
    private String pldia08;
    private String pldia09;
    private String pldia10;
    private String pldia11;
    private String pldia12;
    private String pldia13;
    private String pldia14;
    private String pldia15;
    private String pldia16;
    private String pldia17;
    private String pldia18;
    private String pldia19;
    private String pldia20;
    private String pldia21;
    private String pldia22;
    private String pldia23;
    private String pldia24;
    private String pldia25;
    private String pldia26;
    private String pldia27;
    private String pldia28;
    private String pldia29;
    private String pldia30;
    private String pldia31;

    private String asistencia;
    private String coo_accion;
    // para lov
    private String cod_subsidio;
    private String des_subsidio;
    private String sper_fecing;
    private String sper_fecces;
    private String glosa;
    private Date finicio;
    private Date ffinal;
    private boolean valSelec;
	private int tipoasistencia;
	private int tipodescanso;

    public AsistenciaGen() {

    }

    public AsistenciaGen(String pldia01) {
        this.pldia01 = pldia01;
    }

    public AsistenciaGen(String pldia01, String pldia02, String pldia03, String pldia04, String pldia05, String pldia06, String pldia07, String pldia08, String pldia09, String pldia10, String pldia11, String pldia12, String pldia13, String pldia14, String pldia15, String pldia16, String pldia17, String pldia18, String pldia19, String pldia20, String pldia21, String pldia22, String pldia23, String pldia24, String pldia25, String pldia26, String pldia27, String pldia28, String pldia29, String pldia30, String pldia31) {
        this.pldia01 = pldia01;
        this.pldia02 = pldia02;
        this.pldia03 = pldia03;
        this.pldia04 = pldia04;
        this.pldia05 = pldia05;
        this.pldia06 = pldia06;
        this.pldia07 = pldia07;
        this.pldia08 = pldia08;
        this.pldia09 = pldia09;
        this.pldia10 = pldia10;
        this.pldia11 = pldia11;
        this.pldia12 = pldia12;
        this.pldia13 = pldia13;
        this.pldia14 = pldia14;
        this.pldia15 = pldia15;
        this.pldia16 = pldia16;
        this.pldia17 = pldia17;
        this.pldia18 = pldia18;
        this.pldia19 = pldia19;
        this.pldia20 = pldia20;
        this.pldia21 = pldia21;
        this.pldia22 = pldia22;
        this.pldia23 = pldia23;
        this.pldia24 = pldia24;
        this.pldia25 = pldia25;
        this.pldia26 = pldia26;
        this.pldia27 = pldia27;
        this.pldia28 = pldia28;
        this.pldia29 = pldia29;
        this.pldia30 = pldia30;
        this.pldia31 = pldia31;
    }

    public String getCoo_accion() {
        return coo_accion;
    }

    public void setCoo_accion(String coo_accion) {
        this.coo_accion = coo_accion;
    }

    public String getAsistencia() {
        if (asistencia.equals("0")) {
            asistencia = "ASISTIO";
        } else if (asistencia.equals("1")) {
            asistencia = "FALTA";
        } else if (asistencia.equals("2")) {
            asistencia = "VACACIONES";
        } else if (asistencia.equals("3")) {
            asistencia = "PRE NATAL";
        } else if (asistencia.equals("4")) {
            asistencia = "POST NATAL";
        } else if (asistencia.equals("5")) {
            asistencia = "LIC. C. GOCE";
        } else if (asistencia.equals("6")) {
            asistencia = "LIC. S. GOCE";
        } else if (asistencia.equals("7")) {
            asistencia = "COMPENSACION";
        } else if (asistencia.equals("8")) {
            asistencia = "DESC. MEDICO";
        } else if (asistencia.equals("9")) {
            asistencia = "DESC. M. SUBSIDIO";
        } else if (asistencia.equals("A")) {
            asistencia = "LIC. PATERNIDAD";
        } else if (asistencia.equals("B")) {
            asistencia = "LIC. ENF. TERMINAL";
        }
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public int getSuc_des() {
        return suc_des;
    }

    public void setSuc_des(int suc_des) {
        this.suc_des = suc_des;
    }

    public String getId_per() {
        return id_per;
    }

    public void setId_per(String id_per) {
        this.id_per = id_per;
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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public String getDes_per() {
        return des_per;
    }

    public void setDes_per(String des_per) {
        this.des_per = des_per;
    }

    public String getPlas_usuadd() {
        return plas_usuadd;
    }

    public void setPlas_usuadd(String plas_usuadd) {
        this.plas_usuadd = plas_usuadd;
    }

    public Date getPlas_fecadd() {
        return plas_fecadd;
    }

    public void setPlas_fecadd(Date plas_fecadd) {
        this.plas_fecadd = plas_fecadd;
    }

    public String getPlas_usumod() {
        return plas_usumod;
    }

    public void setPlas_usumod(String plas_usumod) {
        this.plas_usumod = plas_usumod;
    }

    public Date getPlas_fecmod() {
        return plas_fecmod;
    }

    public void setPlas_fecmod(Date plas_fecmod) {
        this.plas_fecmod = plas_fecmod;
    }

    public String getPlcodhor() {
        return plcodhor;
    }

    public void setPlcodhor(String plcodhor) {
        this.plcodhor = plcodhor;
    }

    public String getDeshor() {
        return deshor;
    }

    public void setDeshor(String deshor) {
        this.deshor = deshor;
    }

    public String getId_area() {
        return id_area;
    }

    public void setId_area(String id_area) {
        this.id_area = id_area;
    }

    public String getDes_area() {
        return des_area;
    }

    public void setDes_area(String des_area) {
        this.des_area = des_area;
    }

    public String getPer_cargo() {
        return per_cargo;
    }

    public void setPer_cargo(String per_cargo) {
        this.per_cargo = per_cargo;
    }

    public Date getPer_fecing() {
        return per_fecing;
    }

    public void setPer_fecing(Date per_fecing) {
        this.per_fecing = per_fecing;
    }

    public Date getPer_fecces() {
        return per_fecces;
    }

    public void setPer_fecces(Date per_fecces) {
        this.per_fecces = per_fecces;
    }

    public String getPldia01() {
        return pldia01;
    }

    public void setPldia01(String pldia01) {
        this.pldia01 = pldia01;
    }

    public String getPldia02() {
        return pldia02;
    }

    public void setPldia02(String pldia02) {
        this.pldia02 = pldia02;
    }

    public String getPldia03() {
        return pldia03;
    }

    public void setPldia03(String pldia03) {
        this.pldia03 = pldia03;
    }

    public String getPldia04() {
        return pldia04;
    }

    public void setPldia04(String pldia04) {
        this.pldia04 = pldia04;
    }

    public String getPldia05() {
        return pldia05;
    }

    public void setPldia05(String pldia05) {
        this.pldia05 = pldia05;
    }

    public String getPldia06() {
        return pldia06;
    }

    public void setPldia06(String pldia06) {
        this.pldia06 = pldia06;
    }

    public String getPldia07() {
        return pldia07;
    }

    public void setPldia07(String pldia07) {
        this.pldia07 = pldia07;
    }

    public String getPldia08() {
        return pldia08;
    }

    public void setPldia08(String pldia08) {
        this.pldia08 = pldia08;
    }

    public String getPldia09() {
        return pldia09;
    }

    public void setPldia09(String pldia09) {
        this.pldia09 = pldia09;
    }

    public String getPldia10() {
        return pldia10;
    }

    public void setPldia10(String pldia10) {
        this.pldia10 = pldia10;
    }

    public String getPldia11() {
        return pldia11;
    }

    public void setPldia11(String pldia11) {
        this.pldia11 = pldia11;
    }

    public String getPldia12() {
        return pldia12;
    }

    public void setPldia12(String pldia12) {
        this.pldia12 = pldia12;
    }

    public String getPldia13() {
        return pldia13;
    }

    public void setPldia13(String pldia13) {
        this.pldia13 = pldia13;
    }

    public String getPldia14() {
        return pldia14;
    }

    public void setPldia14(String pldia14) {
        this.pldia14 = pldia14;
    }

    public String getPldia15() {
        return pldia15;
    }

    public void setPldia15(String pldia15) {
        this.pldia15 = pldia15;
    }

    public String getPldia16() {
        return pldia16;
    }

    public void setPldia16(String pldia16) {
        this.pldia16 = pldia16;
    }

    public String getPldia17() {
        return pldia17;
    }

    public void setPldia17(String pldia17) {
        this.pldia17 = pldia17;
    }

    public String getPldia18() {
        return pldia18;
    }

    public void setPldia18(String pldia18) {
        this.pldia18 = pldia18;
    }

    public String getPldia19() {
        return pldia19;
    }

    public void setPldia19(String pldia19) {
        this.pldia19 = pldia19;
    }

    public String getPldia20() {
        return pldia20;
    }

    public void setPldia20(String pldia20) {
        this.pldia20 = pldia20;
    }

    public String getPldia21() {
        return pldia21;
    }

    public void setPldia21(String pldia21) {
        this.pldia21 = pldia21;
    }

    public String getPldia22() {
        return pldia22;
    }

    public void setPldia22(String pldia22) {
        this.pldia22 = pldia22;
    }

    public String getPldia23() {
        return pldia23;
    }

    public void setPldia23(String pldia23) {
        this.pldia23 = pldia23;
    }

    public String getPldia24() {
        return pldia24;
    }

    public void setPldia24(String pldia24) {
        this.pldia24 = pldia24;
    }

    public String getPldia25() {
        return pldia25;
    }

    public void setPldia25(String pldia25) {
        this.pldia25 = pldia25;
    }

    public String getPldia26() {
        return pldia26;
    }

    public void setPldia26(String pldia26) {
        this.pldia26 = pldia26;
    }

    public String getPldia27() {
        return pldia27;
    }

    public void setPldia27(String pldia27) {
        this.pldia27 = pldia27;
    }

    public String getPldia28() {
        return pldia28;
    }

    public void setPldia28(String pldia28) {
        this.pldia28 = pldia28;
    }

    public String getPldia29() {
        return pldia29;
    }

    public void setPldia29(String pldia29) {
        this.pldia29 = pldia29;
    }

    public String getPldia30() {
        return pldia30;
    }

    public void setPldia30(String pldia30) {
        this.pldia30 = pldia30;
    }

    public String getPldia31() {
        return pldia31;
    }

    public void setPldia31(String pldia31) {
        this.pldia31 = pldia31;
    }

    public Date getFec_asis() {
        return fec_asis;
    }

    public void setFec_asis(Date fec_asis) {
        this.fec_asis = fec_asis;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getSfec_asis() {
        sfec_asis = sdf.format(fec_asis);
        return sfec_asis;
    }

    public void setSfec_asis(String sfec_asis) {
        this.sfec_asis = sfec_asis;
    }

    public String getSfec_asisdias() {
        sfec_asisdias = sdf2.format(fec_asis);
        return sfec_asisdias;
    }

    public void setSfec_asisdias(String sfec_asisdias) {
        this.sfec_asisdias = sfec_asisdias;
    }

    public String getCod_subsidio() {
        return cod_subsidio;
    }

    public void setCod_subsidio(String cod_subsidio) {
        this.cod_subsidio = cod_subsidio;
    }

    public String getDes_subsidio() {
        return des_subsidio;
    }

    public void setDes_subsidio(String des_subsidio) {
        this.des_subsidio = des_subsidio;
    }

    public String getSper_fecing() {
        sper_fecing = sdf.format(per_fecing);
        return sper_fecing;
    }

    public void setSper_fecing(String sper_fecing) {
        this.sper_fecing = sper_fecing;
    }

    public String getSper_fecces() {
        if (per_fecces != null) {
            sper_fecces = sdf.format(per_fecces);
        } else {
            sper_fecces = "";
        }

        return sper_fecces;
    }

    public void setSper_fecces(String sper_fecces) {
        this.sper_fecces = sper_fecces;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public Date getFinicio() {
        return finicio;
    }

    public void setFinicio(Date finicio) {
        this.finicio = finicio;
    }

    public Date getFfinal() {
        return ffinal;
    }

    public void setFfinal(Date ffinal) {
        this.ffinal = ffinal;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public int getTipoasistencia() {
        return tipoasistencia;
    }

    public void setTipoasistencia(int tipoasistencia) {
        this.tipoasistencia = tipoasistencia;
    }

    public int getTipodescanso() {
        return tipodescanso;
    }

    public void setTipodescanso(int tipodescanso) {
        this.tipodescanso = tipodescanso;
    }
    
}
