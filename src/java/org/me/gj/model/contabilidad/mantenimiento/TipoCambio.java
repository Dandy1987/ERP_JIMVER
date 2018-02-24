package org.me.gj.model.contabilidad.mantenimiento;

public class TipoCambio {
    private int idTipCam;
    private String desTipCam; 
    private String sigTipCam;
    private int estTipCam;
    private double valorTipCam;
    private int ordTipCam;
    private String nomRepTipCam;
    private boolean valor;

    public TipoCambio(int idTipCam, String desTipCam, String sigTipCam, int estTipCam, double valorTipCam, int ordTipCam, String nomRepTipCam) {
        this.idTipCam = idTipCam;
        this.desTipCam = desTipCam;
        this.sigTipCam = sigTipCam;
        this.estTipCam = estTipCam;
        this.valorTipCam = valorTipCam;
        this.ordTipCam = ordTipCam;
        this.nomRepTipCam = nomRepTipCam;
    }

    public TipoCambio() {
    }

    public String getDesTipCam() {
        return desTipCam;
    }

    public void setDesTipCam(String desTipCam) {
        this.desTipCam = desTipCam;
    }

    public int getEstTipCam() {
        return estTipCam;
    }

    public void setEstTipCam(int estTipCam) {
        this.estTipCam = estTipCam;
    }

    public int getIdTipCam() {
        return idTipCam;
    }

    public void setIdTipCam(int idTipCam) {
        this.idTipCam = idTipCam;
    }

    public String getNomRepTipCam() {
        return nomRepTipCam;
    }

    public void setNomRepTipCam(String nomRepTipCam) {
        this.nomRepTipCam = nomRepTipCam;
    }

    public int getOrdTipCam() {
        return ordTipCam;
    }

    public void setOrdTipCam(int ordTipCam) {
        this.ordTipCam = ordTipCam;
    }

    public String getSigTipCam() {
        return sigTipCam;
    }

    public void setSigTipCam(String sigTipCam) {
        this.sigTipCam = sigTipCam;
    }

    public double getValorTipCam() {
        return valorTipCam;
    }

    public void setValorTipCam(double valorTipCam) {
        this.valorTipCam = valorTipCam;
    }

    public boolean isValor() {
        if(estTipCam==1)
            valor=true;
        else
            valor=false;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
