/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author greyes
 */
public class ManConfAfps {

    private int emp_id;
    private String periodo;
    private String afpid;
    private String afp_des;
    private double apobl;
    private String apori;
    private String apodes;
    private double comfij;
    private String comori;
    private String comdes;
    private double priseg;
    private String priori;
    private String prides;
    private double topseg;
    private String idcta;
    private int estado;
    private double commix;
    private String mixori;
    private String mixdes;
    private String usuadd;
    private Date fecadd;
    private String usumod;
    private Date fecmod;
    private String origen;
    private String destino;
    private String com_origen;
    private String com_destino;
    private String pri_origen;
    private String pri_destino;
    private String mix_origen;
    private String mix_destino;
    private boolean valor;

    public ManConfAfps() {

    }

    public ManConfAfps(int emp_id, String periodo, String afpid, String afp_des, double apobl, String apori, String apodes,
            double comfij, String comori, String comdes, double priseg, String priori, String prides, double topseg, String idcta,
            int estado, double commix, String mixori, String mixdes, String usuadd, String usumod,
            String origen, String destino, String com_origen, String com_destino, String pri_origen, String pri_destino, String mix_origen, String mix_destino) {

        this.emp_id = emp_id;
        this.periodo = periodo;
        this.afpid = afpid;
        this.afp_des = afp_des;
        this.apobl = apobl;
        this.apori = apori;
        this.apodes = apodes;
        this.comfij = comfij;
        this.comori = comori;
        this.comdes = comdes;
        this.priseg = priseg;
        this.priori = priori;
        this.prides = prides;
        this.topseg = topseg;
        this.idcta = idcta;
        this.estado = estado;
        this.commix = commix;
        this.mixori = mixori;
        this.mixdes = mixdes;
        this.usuadd = usuadd;
        this.usumod = usumod;
        this.origen = origen;
        this.destino = destino;
        this.com_origen = com_origen;
        this.com_destino = com_destino;
        this.pri_origen = pri_origen;
        this.pri_destino = pri_destino;
        this.mix_origen = mix_origen;
        this.mix_destino = mix_destino;

    }
    
     public ManConfAfps(int emp_id,String periodo, String afpid, double apobl, String apori, String apodes,
            double comfij, String comori, String comdes, double priseg, String priori, String prides, double topseg, String idcta,
            int estado, double commix, String mixori, String mixdes, String usuadd, String usumod) {

        this.emp_id = emp_id;
        this.periodo = periodo;
        this.afpid = afpid;
        this.apobl = apobl;
        this.apori = apori;
        this.apodes = apodes;
        this.comfij = comfij;
        this.comori = comori;
        this.comdes = comdes;
        this.priseg = priseg;
        this.priori = priori;
        this.prides = prides;
        this.topseg = topseg;
        this.idcta = idcta;
        this.estado = estado;
        this.commix = commix;
        this.mixori = mixori;
        this.mixdes = mixdes;
        this.usuadd = usuadd;
        this.usumod = usumod;

    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCom_origen() {
        return com_origen;
    }

    public void setCom_origen(String com_origen) {
        this.com_origen = com_origen;
    }

    public String getCom_destino() {
        return com_destino;
    }

    public void setCom_destino(String com_destino) {
        this.com_destino = com_destino;
    }

    public String getPri_origen() {
        return pri_origen;
    }

    public void setPri_origen(String pri_origen) {
        this.pri_origen = pri_origen;
    }

    public String getPri_destino() {
        return pri_destino;
    }

    public void setPri_destino(String pri_destino) {
        this.pri_destino = pri_destino;
    }

    public String getMix_origen() {
        return mix_origen;
    }

    public void setMix_origen(String mix_origen) {
        this.mix_origen = mix_origen;
    }

    public String getMix_destino() {
        return mix_destino;
    }

    public void setMix_destino(String mix_destino) {
        this.mix_destino = mix_destino;
    }

    public boolean isValor() {
        valor = estado == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getAfpid() {
        return afpid;
    }

    public void setAfpid(String afpid) {
        this.afpid = afpid;
    }

    public String getAfp_des() {
        return afp_des;
    }

    public void setAfp_des(String afp_des) {
        this.afp_des = afp_des;
    }

    public double getApobl() {
        return apobl;
    }

    public void setApobl(double apobl) {
        this.apobl = apobl;
    }

    public String getApori() {
        return apori;
    }

    public void setApori(String apori) {
        this.apori = apori;
    }

    public String getApodes() {
        return apodes;
    }

    public void setApodes(String apodes) {
        this.apodes = apodes;
    }

    public double getComfij() {
        return comfij;
    }

    public void setComfij(double comfij) {
        this.comfij = comfij;
    }

    public String getComori() {
        return comori;
    }

    public void setComori(String comori) {
        this.comori = comori;
    }

    public String getComdes() {
        return comdes;
    }

    public void setComdes(String comdes) {
        this.comdes = comdes;
    }

    public double getPriseg() {
        return priseg;
    }

    public void setPriseg(double priseg) {
        this.priseg = priseg;
    }

    public String getPriori() {
        return priori;
    }

    public void setPriori(String priori) {
        this.priori = priori;
    }

    public String getPrides() {
        return prides;
    }

    public void setPrides(String prides) {
        this.prides = prides;
    }

    public double getTopseg() {
        return topseg;
    }

    public void setTopseg(double topseg) {
        this.topseg = topseg;
    }

    public String getIdcta() {
        return idcta;
    }

    public void setIdcta(String idcta) {
        this.idcta = idcta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getCommix() {
        return commix;
    }

    public void setCommix(double commix) {
        this.commix = commix;
    }

    public String getMixori() {
        return mixori;
    }

    public void setMixori(String mixori) {
        this.mixori = mixori;
    }

    public String getMixdes() {
        return mixdes;
    }

    public void setMixdes(String mixdes) {
        this.mixdes = mixdes;
    }

    public String getUsuadd() {
        return usuadd;
    }

    public void setUsuadd(String usuadd) {
        this.usuadd = usuadd;
    }

    public Date getFecadd() {
        return fecadd;
    }

    public void setFecadd(Date fecadd) {
        this.fecadd = fecadd;
    }

    public String getUsumod() {
        return usumod;
    }

    public void setUsumod(String usumod) {
        this.usumod = usumod;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

}
