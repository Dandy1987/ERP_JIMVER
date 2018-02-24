package org.me.gj.model.logistica.informes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MontosTotales {

    private int emp_id;
    private int suc_id;
    private long cant;
    private double vimpto;
    private double vdesc;
    private double vafecto;
    private double vtotal;
    private double vinafecto;
    private double vexonerado;
    private double vbruto;
    private double vdscgen;
    private String fecemi;
    private String fecfin;
    private String periodo;
    private String svimpto;
    private String svdesc;
    private String svafecto;
    private String svtotal;
    private String svinafecto;
    private String svexonerado;
    private String svbruto;
    private String svdscgen;
    private boolean selImp;

    private final DecimalFormatSymbols dfs;
    private final DecimalFormat formato;

    public MontosTotales() {
        dfs = new DecimalFormatSymbols(Locale.US);
        formato = new DecimalFormat("###,##0.00", dfs);
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

    public long getCant() {
        return cant;
    }

    public void setCant(long cant) {
        this.cant = cant;
    }

    public double getVimpto() {
        return vimpto;
    }

    public void setVimpto(double vimpto) {
        this.vimpto = vimpto;
    }

    public double getVdesc() {
        return vdesc;
    }

    public void setVdesc(double vdesc) {
        this.vdesc = vdesc;
    }

    public double getVafecto() {
        return vafecto;
    }

    public void setVafecto(double vafecto) {
        this.vafecto = vafecto;
    }

    public double getVtotal() {
        return vtotal;
    }

    public void setVtotal(double vtotal) {
        this.vtotal = vtotal;
    }

    public double getVinafecto() {
        return vinafecto;
    }

    public void setVinafecto(double vinafecto) {
        this.vinafecto = vinafecto;
    }

    public double getVbruto() {
        return vbruto;
    }

    public void setVbruto(double vbruto) {
        this.vbruto = vbruto;
    }

    public double getVdscgen() {
        return vdscgen;
    }

    public void setVdscgen(double vdscgen) {
        this.vdscgen = vdscgen;
    }

    public String getFecemi() {
        return fecemi;
    }

    public void setFecemi(String fecemi) {
        this.fecemi = fecemi;
    }

    public String getFecfin() {
        return fecfin;
    }

    public void setFecfin(String fecfin) {
        this.fecfin = fecfin;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getSvimpto() {
        svimpto = formato.format(vimpto);
        return svimpto;
    }

    public void setSvimpto(String svimpto) {
        this.svimpto = svimpto;
    }

    public String getSvdesc() {
        svdesc = formato.format(vdesc);
        return svdesc;
    }

    public void setSvdesc(String svdesc) {
        this.svdesc = svdesc;
    }

    public String getSvafecto() {
        svafecto = formato.format(vafecto);
        return svafecto;
    }

    public void setSvafecto(String svafecto) {
        this.svafecto = svafecto;
    }

    public String getSvtotal() {
        svtotal = formato.format(vtotal);
        return svtotal;
    }

    public void setSvtotal(String svtotal) {
        this.svtotal = svtotal;
    }

    public String getSvinafecto() {
        svinafecto = formato.format(vinafecto);
        return svinafecto;
    }

    public void setSvinafecto(String svinafecto) {
        this.svinafecto = svinafecto;
    }
    
    public double getVexonerado() {
        return vexonerado;
    }

    public void setVexonerado(double vexonerado) {
        this.vexonerado = vexonerado;
    }

    public String getSvexonerado() {
        svexonerado = formato.format(vexonerado);
        return svexonerado;
    }

    public void setSvexonerado(String svexonerado) {
        this.svexonerado = svexonerado;
    }

    public String getSvbruto() {
        svbruto = formato.format(vbruto);
        return svbruto;
    }

    public void setSvbruto(String svbruto) {
        this.svbruto = svbruto;
    }

    public String getSvdscgen() {
        svdscgen = formato.format(vdscgen);
        return svdscgen;
    }

    public void setSvdscgen(String svdscgen) {
        this.svdscgen = svdscgen;
    }

    public boolean isSelImp() {
        return selImp;
    }

    public void setSelImp(boolean selImp) {
        this.selImp = selImp;
    }
}
