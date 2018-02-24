package org.me.gj.model.logistica.mantenimiento;

public class TipVen {

    private int idVen;
    private String desVen;
    private int estVen;
    private String nomRepVen;
    private int ordVen;
    private boolean valor;

    public TipVen() {
    }

    public TipVen(int idVen, String desVen, int estVen, String nomRepVen, int ordVen) {
        this.idVen = idVen;
        this.desVen = desVen;
        this.estVen = estVen;
        this.nomRepVen = nomRepVen;
        this.ordVen = ordVen;
    }

    public int getEstVen() {
        return estVen;
    }

    public void setEstVen(int estVen) {
        this.estVen = estVen;
    }

    public String getNomRepVen() {
        return nomRepVen;
    }

    public void setNomRepVen(String nomRepVen) {
        this.nomRepVen = nomRepVen;
    }

    public int getOrdVen() {
        return ordVen;
    }

    public void setOrdVen(int ordVen) {
        this.ordVen = ordVen;
    }

    public boolean isValor() {
        if (estVen == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getDesVen() {
        return desVen;
    }

    public void setDesVen(String desVen) {
        this.desVen = desVen;
    }

    public int getIdVen() {
        return idVen;
    }

    public void setIdVen(int idVen) {
        this.idVen = idVen;
    }

}
