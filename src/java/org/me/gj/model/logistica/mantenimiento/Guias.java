package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class Guias {

    private int codTabGui;
    private int idGui;
    private int estGui;
    private String desGui;
    private int idMovGui;
    private int refGui;
    private int idDetGui;
    private int sunGui;
    private int ordGui;
    private String nomRepGui;
    private String desMovGui;
    private String desDetGui;
    private boolean valor;
    private boolean selImp;
    private String codigo;
    private String usuaddGui;
    private Date fecaddGui;
    private String usumodGui;
    private Date fecmodGui;
    private String glosa;

    public Guias() {
    }

    public Guias(int idGui, String desGui) {
        this.idGui = idGui;
        this.desGui = desGui;

    }

    public Guias(int codTabGui, int idGui, int estGui, String desGui, int idMovGui, int refGui, int idDetGui, int sunGui, int ordGui, String nomRepGui, String usuaddGui, String usumodGui,String glosa) {
        this.codTabGui = codTabGui;
        this.idGui = idGui;
        this.estGui = estGui;
        this.desGui = desGui;
        this.idMovGui = idMovGui;
        this.refGui = refGui;
        this.idDetGui = idDetGui;
        this.sunGui = sunGui;
        this.ordGui = ordGui;
        this.nomRepGui = nomRepGui;
        this.usuaddGui = usuaddGui;
        this.usumodGui = usumodGui;
        this.glosa = glosa;
    }

    public Guias(int codTabGui, int idGui, int estGui, String desGui, int idMovGui, int refGui, int idDetGui, int sunGui, int ordGui, String nomRepGui, String codigo, String usuaddGui, String usumodGui,String glosa) {
        this.codTabGui = codTabGui;
        this.idGui = idGui;
        this.estGui = estGui;
        this.desGui = desGui;
        this.idMovGui = idMovGui;
        this.refGui = refGui;
        this.idDetGui = idDetGui;
        this.sunGui = sunGui;
        this.ordGui = ordGui;
        this.nomRepGui = nomRepGui;
        this.codigo = codigo;
        this.usuaddGui = usuaddGui;
        this.usumodGui = usumodGui;
        this.glosa = glosa;
    }

    public String getDesDetGui() {
        return desDetGui;
    }

    public void setDesDetGui(String desDetGui) {
        this.desDetGui = desDetGui;
    }

    public String getDesMovGui() {
        return desMovGui;
    }

    public void setDesMovGui(String desMovGui) {
        this.desMovGui = desMovGui;
    }

    public boolean isValor() {
        valor = estGui == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getCodTabGui() {
        return codTabGui;
    }

    public void setCodTabGui(int codTabGui) {
        this.codTabGui = codTabGui;
    }

    public int getIdDetGui() {
        return idDetGui;
    }

    public void setIdDetGui(int idDetGui) {
        this.idDetGui = idDetGui;
    }

    public int getIdMovGui() {
        return idMovGui;
    }

    public void setIdMovGui(int idMovGui) {
        this.idMovGui = idMovGui;
    }

    public int getEstGui() {
        return estGui;
    }

    public void setEstGui(int estGui) {
        this.estGui = estGui;
    }

    public int getIdGui() {
        return idGui;
    }

    public void setIdGui(int idGui) {
        this.idGui = idGui;
    }

    public int getRefGui() {
        return refGui;
    }

    public void setRefGui(int refGui) {
        this.refGui = refGui;
    }

    public int getSunGui() {
        return sunGui;
    }

    public void setSunGui(int sunGui) {
        this.sunGui = sunGui;
    }

    public String getDesGui() {
        return desGui;
    }

    public void setDesGui(String desGui) {
        this.desGui = desGui;
    }

    public String getNomRepGui() {
        return nomRepGui;
    }

    public void setNomRepGui(String nomRepGui) {
        this.nomRepGui = nomRepGui;
    }

    public int getOrdGui() {
        return ordGui;
    }

    public void setOrdGui(int ordGui) {
        this.ordGui = ordGui;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuaddGui() {
        return usuaddGui;
    }

    public void setUsuaddGui(String usuaddGui) {
        this.usuaddGui = usuaddGui;
    }

    public Date getFecaddGui() {
        return fecaddGui;
    }

    public void setFecaddGui(Date fecaddGui) {
        this.fecaddGui = fecaddGui;
    }

    public String getUsumodGui() {
        return usumodGui;
    }

    public void setUsumodGui(String usumodGui) {
        this.usumodGui = usumodGui;
    }

    public Date getFecmodGui() {
        return fecmodGui;
    }

    public void setFecmodGui(Date fecmodGui) {
        this.fecmodGui = fecmodGui;
    }

    public boolean isSelImp() {
        return selImp;
    }

    public void setSelImp(boolean selImp) {
        this.selImp = selImp;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

}
