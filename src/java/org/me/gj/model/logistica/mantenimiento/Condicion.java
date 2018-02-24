package org.me.gj.model.logistica.mantenimiento;

import java.util.Date;

public class Condicion {

    private int conKey;
    private String conId;
    private String conDes;
    private int conEst;
    private int conDias;
    private int conOrd;
    private String conNomRep;
    private boolean valor;
    private String conTipo;
    private String conUsuadd;
    private Date conFecadd;
    private String conUsumod;
    private Date conFecmod;

    public Condicion(int conkey, String conTipo, String conDes, int conEst, int conDias, int conOrd, String conNomRep, String conUsuadd, String conUsumod) {
        this.conKey = conkey;
        this.conTipo = conTipo;
        this.conDes = conDes;
        this.conEst = conEst;
        this.conDias = conDias;
        this.conOrd = conOrd;
        this.conNomRep = conNomRep;
        this.conUsuadd = conUsuadd;
        this.conUsumod = conUsumod;
    }

    public Condicion(int conId, String conDes) {
        this.conKey = conId;
        this.conDes = conDes;
    }

    public Condicion() {
    }

    public String getConTipo() {
        return conTipo;
    }

    public void setConTipo(String conTipo) {
        this.conTipo = conTipo;
    }

    public String getConDes() {
        return conDes;
    }

    public void setConDes(String conDes) {
        this.conDes = conDes;
    }

    public int getConDias() {
        return conDias;
    }

    public void setConDias(int conDias) {
        this.conDias = conDias;
    }

    public int getConEst() {
        return conEst;
    }

    public void setConEst(int conEst) {
        this.conEst = conEst;
    }

    public int getConKey() {
        return conKey;
    }

    public void setConKey(int conKey) {
        this.conKey = conKey;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getConNomRep() {
        return conNomRep;
    }

    public void setConNomRep(String conNomRep) {
        this.conNomRep = conNomRep;
    }

    public int getConOrd() {
        return conOrd;
    }

    public void setConOrd(int conOrd) {
        this.conOrd = conOrd;
    }

    public boolean isValor() {
        if (conEst == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public Date getConFecadd() {
        return conFecadd;
    }

    public void setConFecadd(Date conFecadd) {
        this.conFecadd = conFecadd;
    }

    public Date getConFecmod() {
        return conFecmod;
    }

    public void setConFecmod(Date conFecmod) {
        this.conFecmod = conFecmod;
    }

    public String getConUsuadd() {
        return conUsuadd;
    }

    public void setConUsuadd(String conUsuadd) {
        this.conUsuadd = conUsuadd;
    }

    public String getConUsumod() {
        return conUsumod;
    }

    public void setConUsumod(String conUsumod) {
        this.conUsumod = conUsumod;
    }
}
