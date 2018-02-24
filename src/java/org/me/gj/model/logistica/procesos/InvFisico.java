
package org.me.gj.model.logistica.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InvFisico {

    private String proveedor;
    private String artid;
    private String artdes;
    private String undmed;
    private String undman;

    private double fisico;
    private String sfisico;
    private String observacion;
    private String grupo;

    private boolean valSelec;
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df = new DecimalFormat("###,##0.00", dfs);

    public InvFisico() {
    }

    public InvFisico(String proveedor, String artid, String artdes, String undmed, String undman) {
        this.proveedor = proveedor;
        this.artid = artid;
        this.artdes = artdes;
        this.undmed = undmed;
        this.undman = undman;
    }

    public InvFisico(String proveedor, String artid, String artdes, String undmed, String undman, double fisico, String sfisico, String observacion, String grupo) {
        this.proveedor = proveedor;
        this.artid = artid;
        this.artdes = artdes;
        this.undmed = undmed;
        this.undman = undman;
        this.fisico = fisico;
        this.sfisico = sfisico;
        this.observacion = observacion;
        this.grupo = grupo;
    }

    public double getFisico() {
        return fisico;
    }

    public void setFisico(double fisico) {
        this.fisico = fisico;
    }

    public String getSfisico() {
        sfisico = df.format(fisico);
        return sfisico;
    }

    public void setSfisico(String sfisico) {
        this.sfisico = sfisico;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getArtid() {
        return artid;
    }

    public void setArtid(String artid) {
        this.artid = artid;
    }

    public String getArtdes() {
        return artdes;
    }

    public void setArtdes(String artdes) {
        this.artdes = artdes;
    }

    public String getUndmed() {
        return undmed;
    }

    public void setUndmed(String undmed) {
        this.undmed = undmed;
    }

    public String getUndman() {
        return undman;
    }

    public void setUndman(String undman) {
        this.undman = undman;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
