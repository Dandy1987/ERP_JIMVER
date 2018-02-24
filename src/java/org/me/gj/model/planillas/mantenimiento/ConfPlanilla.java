/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author achocano
 */
public class ConfPlanilla {

    private String codigo;
    private String constante;
    private String des_const;
    private String usu_add;
    private Date fecha_add;
    private String usu_mod;
    private Date fecha_mod;
    private String pc_add;
    private String pc_mod;
    private String ind_accion = "Q";
    private int nroope;
    private String tipo;

    public ConfPlanilla() {
    }

    public ConfPlanilla(String codigo, String constante, String des_const, Date fecha_add, String usu_mod, Date fecha_mod, String pc_add, String pc_mod) {
        this.codigo = codigo;
        this.constante = constante;
        this.des_const = des_const;
        this.fecha_add = fecha_add;
        this.usu_mod = usu_mod;
        this.fecha_mod = fecha_mod;
        this.pc_add = pc_add;
        this.pc_mod = pc_mod;
    }

    public ConfPlanilla(String codigo, String constante,int nroope) {
        this.codigo = codigo;
        this.constante = constante;
        this.nroope = nroope;
    }
    

    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getConstante() {
        return constante;
    }

    public void setConstante(String constante) {
        this.constante = constante;
    }

    public String getDes_const() {
        return des_const;
    }

    public void setDes_const(String des_const) {
        this.des_const = des_const;
    }

    public String getUsu_add() {
        return usu_add;
    }

    public void setUsu_add(String usu_add) {
        this.usu_add = usu_add;
    }

    public Date getFecha_add() {
        return fecha_add;
    }

    public void setFecha_add(Date fecha_add) {
        this.fecha_add = fecha_add;
    }

    public String getUsu_mod() {
        return usu_mod;
    }

    public void setUsu_mod(String usu_mod) {
        this.usu_mod = usu_mod;
    }

    public Date getFecha_mod() {
        return fecha_mod;
    }

    public void setFecha_mod(Date fecha_mod) {
        this.fecha_mod = fecha_mod;
    }

    public String getPc_add() {
        return pc_add;
    }

    public void setPc_add(String pc_add) {
        this.pc_add = pc_add;
    }

    public String getPc_mod() {
        return pc_mod;
    }

    public void setPc_mod(String pc_mod) {
        this.pc_mod = pc_mod;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public int getNroope() {
        return nroope;
    }

    public void setNroope(int nroope) {
        this.nroope = nroope;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
