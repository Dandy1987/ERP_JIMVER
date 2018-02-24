package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class LisPre {

    private String desProvLp;
    private int idLp;
    private String desLp;
    private String tipLp;
    private int estLp;
    private double dct1Lp;
    private double dct2Lp;
    private double dct3Lp;
    private double dct4Lp;
    private double totalLp;
    private int idProvLp;
    private Date fecIniLp;
    private Date fecTerLp;
    private int idEmpLp;
    private int idSucLp;
    private int ordLp;
    private String nomRepLp;
    private boolean valor;

    public LisPre(String desProvLp, int idLp, String desLp, String tipLp, int estLp, double dct1Lp, double dct2Lp, double dct3Lp, double dct4Lp, double totalLp, int idProvLp, Date fecIniLp, Date fecTerLp, int idEmpLp, int idSucLp, int ordLp, String nomRepLp) {
        this.desProvLp = desProvLp;
        this.idLp = idLp;
        this.desLp = desLp;
        this.tipLp = tipLp;
        this.estLp = estLp;
        this.dct1Lp = dct1Lp;
        this.dct2Lp = dct2Lp;
        this.dct3Lp = dct3Lp;
        this.dct4Lp = dct4Lp;
        this.totalLp = totalLp;
        this.idProvLp = idProvLp;
        this.fecIniLp = fecIniLp;
        this.fecTerLp = fecTerLp;
        this.idEmpLp = idEmpLp;
        this.idSucLp = idSucLp;
        this.ordLp = ordLp;
        this.nomRepLp = nomRepLp;
    }

    public LisPre() {
    }

    public String getDesProvLp() {
        return desProvLp;
    }

    public void setDesProvLp(String desProvLp) {
        this.desProvLp = desProvLp;
    }

    public String getNomRepLp() {
        return nomRepLp;
    }

    public void setNomRepLp(String nomRepLp) {
        this.nomRepLp = nomRepLp;
    }

    public int getOrdLp() {
        return ordLp;
    }

    public void setOrdLp(int ordLp) {
        this.ordLp = ordLp;
    }

    public double getDct1Lp() {
        return dct1Lp;
    }

    public void setDct1Lp(double dct1Lp) {
        this.dct1Lp = dct1Lp;
    }

    public double getDct2Lp() {
        return dct2Lp;
    }

    public void setDct2Lp(double dct2Lp) {
        this.dct2Lp = dct2Lp;
    }

    public double getDct3Lp() {
        return dct3Lp;
    }

    public void setDct3Lp(double dct3Lp) {
        this.dct3Lp = dct3Lp;
    }

    public double getDct4Lp() {
        return dct4Lp;
    }

    public void setDct4Lp(double dct4Lp) {
        this.dct4Lp = dct4Lp;
    }

    public String getDesLp() {
        return desLp;
    }

    public void setDesLp(String desLp) {
        this.desLp = desLp;
    }

    public int getEstLp() {
        return estLp;
    }

    public void setEstLp(int estLp) {
        this.estLp = estLp;
    }

    public Date getFecIniLp() {
        return fecIniLp;
    }

    public void setFecIniLp(Date fecIniLp) {
        this.fecIniLp = fecIniLp;
    }

    public Date getFecTerLp() {
        return fecTerLp;
    }

    public void setFecTerLp(Date fecTerLp) {
        this.fecTerLp = fecTerLp;
    }

    public int getIdEmpLp() {
        return idEmpLp;
    }

    public void setIdEmpLp(int idEmpLp) {
        this.idEmpLp = idEmpLp;
    }

    public int getIdLp() {
        return idLp;
    }

    public void setIdLp(int idLp) {
        this.idLp = idLp;
    }

    public int getIdProvLp() {
        return idProvLp;
    }

    public void setIdProvLp(int idProvLp) {
        this.idProvLp = idProvLp;
    }

    public int getIdSucLp() {
        return idSucLp;
    }

    public void setIdSucLp(int idSucLp) {
        this.idSucLp = idSucLp;
    }

    public String getTipLp() {
        return tipLp;
    }

    public void setTipLp(String tipLp) {
        this.tipLp = tipLp;
    }

    public double getTotalLp() {
        return totalLp;
    }

    public void setTotalLp(double totalLp) {
        this.totalLp = totalLp;
    }

    public boolean isValor() {
        if (this.estLp == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

}
