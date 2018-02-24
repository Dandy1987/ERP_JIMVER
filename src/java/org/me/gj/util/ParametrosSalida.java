package org.me.gj.util;

public class ParametrosSalida {

    int flagIndicador;
    String msgValidacion;
    String codigoRegistro;
    String descripcionRegistro;
    //solo para stocks
    int cantStocks;

    public ParametrosSalida() {
    }

    public ParametrosSalida(int flagIndicador, String msgValidacion, int cantStocks) {
        this.flagIndicador = flagIndicador;
        this.msgValidacion = msgValidacion;
        this.cantStocks = cantStocks;
    }

    public ParametrosSalida(int flagIndicador, String msgValidacion) {
        this.flagIndicador = flagIndicador;
        this.msgValidacion = msgValidacion;
    }

    public ParametrosSalida(int flagIndicador, String msgValidacion, String codigoRegistro) {
        this.flagIndicador = flagIndicador;
        this.msgValidacion = msgValidacion;
        this.codigoRegistro = codigoRegistro;
    }

    public ParametrosSalida(int flagIndicador, String msgValidacion, String codigoRegistro, String descripcionRegistro) {
        this.flagIndicador = flagIndicador;
        this.msgValidacion = msgValidacion;
        this.codigoRegistro = codigoRegistro;
        this.descripcionRegistro = descripcionRegistro;
    }

    public int getFlagIndicador() {
        return flagIndicador;
    }

    public void setFlagIndicador(int flagIndicador) {
        this.flagIndicador = flagIndicador;
    }

    public String getMsgValidacion() {
        return msgValidacion;
    }

    public void setMsgValidacion(String msgValidacion) {
        this.msgValidacion = msgValidacion;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    public String getDescripcionRegistro() {
        return descripcionRegistro;
    }

    public void setDescripcionRegistro(String descripcionRegistro) {
        this.descripcionRegistro = descripcionRegistro;
    }

    public int getCantStocks() {
        return cantStocks;
    }

    public void setCantStocks(int cantStocks) {
        this.cantStocks = cantStocks;
    }

}
