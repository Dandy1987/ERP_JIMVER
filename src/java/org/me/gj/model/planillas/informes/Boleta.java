/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.informes;

/**
 *
 * @author achocano
 */
public class Boleta {

    String codigo;
    String apenom;
    String empresa;
    String ruc;
    String dire;
    String cargo;
    String plfecing;
    String tipdoc;
    String nrodoc;
    String situacion;
    String plvacini;
    String plvacfin;
    String plcarssp;
    String plcarafp;
    String diastrab;
    String horastrab;
    String faltas;
    String horasext;
    String plfecces;
    String afp;
    double toting;
    double totdscto;
    double neto;
    String sucursal;
    String diasubsidio;
    String diasnosubsidio;

    //PARA DATASET PRIMERO
    private String idco1;
    private String cpto1;
    private String mto1;
    //PARA DATASET SEGUNDO
    private String idco2;
    private String cpto2;
    private String mto2;
    //PARA DATASET TERCERO
    private String idco3;
    private String cpto3;
    private String mto3;
    
    //para costos
    private String costo;

    public Boleta() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getApenom() {
        return apenom;
    }

    public void setApenom(String apenom) {
        this.apenom = apenom;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDire() {
        return dire;
    }

    public void setDire(String dire) {
        this.dire = dire;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPlfecing() {
        return plfecing;
    }

    public void setPlfecing(String plfecing) {
        this.plfecing = plfecing;
    }

    public String getTipdoc() {
        return tipdoc;
    }

    public void setTipdoc(String tipdoc) {
        this.tipdoc = tipdoc;
    }

    public String getNrodoc() {
        return nrodoc;
    }

    public void setNrodoc(String nrodoc) {
        this.nrodoc = nrodoc;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getPlvacini() {
        return plvacini;
    }

    public void setPlvacini(String plvacini) {
        this.plvacini = plvacini;
    }

    public String getPlvacfin() {
        return plvacfin;
    }

    public void setPlvacfin(String plvacfin) {
        this.plvacfin = plvacfin;
    }

    public String getPlcarssp() {
        return plcarssp;
    }

    public void setPlcarssp(String plcarssp) {
        this.plcarssp = plcarssp;
    }

    public String getPlcarafp() {
        return plcarafp;
    }

    public void setPlcarafp(String plcarafp) {
        this.plcarafp = plcarafp;
    }

    public String getDiastrab() {
        return diastrab;
    }

    public void setDiastrab(String diastrab) {
        this.diastrab = diastrab;
    }

    public String getHorastrab() {
        return horastrab;
    }

    public void setHorastrab(String horastrab) {
        this.horastrab = horastrab;
    }

    public String getFaltas() {
        return faltas;
    }

    public void setFaltas(String faltas) {
        this.faltas = faltas;
    }

    public String getHorasext() {
        return horasext;
    }

    public void setHorasext(String horasext) {
        this.horasext = horasext;
    }

    public String getPlfecces() {
        return plfecces;
    }

    public void setPlfecces(String plfecces) {
        this.plfecces = plfecces;
    }

    public String getAfp() {
        return afp;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    public double getToting() {
        return toting;
    }

    public void setToting(double toting) {
        this.toting = toting;
    }

    public double getTotdscto() {
        return totdscto;
    }

    public void setTotdscto(double totdscto) {
        this.totdscto = totdscto;
    }

    public double getNeto() {
        return neto;
    }

    public void setNeto(double neto) {
        this.neto = neto;
    }

    public String getIdco1() {
        return idco1;
    }

    public void setIdco1(String idco1) {
        this.idco1 = idco1;
    }

    public String getCpto1() {
        return cpto1;
    }

    public void setCpto1(String cpto1) {
        this.cpto1 = cpto1;
    }

    public String getMto1() {
        return mto1;
    }

    public void setMto1(String mto1) {
        this.mto1 = mto1;
    }

    public String getIdco2() {
        return idco2;
    }

    public void setIdco2(String idco2) {
        this.idco2 = idco2;
    }

    public String getCpto2() {
        return cpto2;
    }

    public void setCpto2(String cpto2) {
        this.cpto2 = cpto2;
    }

    public String getMto2() {
        return mto2;
    }

    public void setMto2(String mto2) {
        this.mto2 = mto2;
    }

    public String getIdco3() {
        return idco3;
    }

    public void setIdco3(String idco3) {
        this.idco3 = idco3;
    }

    public String getCpto3() {
        return cpto3;
    }

    public void setCpto3(String cpto3) {
        this.cpto3 = cpto3;
    }

    public String getMto3() {
        return mto3;
    }

    public void setMto3(String mto3) {
        this.mto3 = mto3;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getDiasubsidio() {
        return diasubsidio;
    }

    public void setDiasubsidio(String diasubsidio) {
        this.diasubsidio = diasubsidio;
    }

    public String getDiasnosubsidio() {
        return diasnosubsidio;
    }

    public void setDiasnosubsidio(String diasnosubsidio) {
        this.diasnosubsidio = diasnosubsidio;
    }

    
}
