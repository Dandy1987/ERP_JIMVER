/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.informes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author achocano
 */
public class InformesDescuentos {

    private String nombres;
    private String codigo;
    private String id_concepto;
    private String des_concepto;
    private String glosa;
    private String regreso;
    private String referencia;
    private double cargo;
    private double abono;
    private Date fecha_mov;
    private String sfecha_mov;
    private String ap_paterno;
    private String area;
    private String empresa;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

    private int id_sucursal;
    private String des_sucursal;
    private boolean valSelec;

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getDes_sucursal() {
        return des_sucursal;
    }

    public void setDes_sucursal(String des_sucursal) {
        this.des_sucursal = des_sucursal;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getId_concepto() {
        return id_concepto;
    }

    public void setId_concepto(String id_concepto) {
        this.id_concepto = id_concepto;
    }

    public String getDes_concepto() {
        return des_concepto;
    }

    public void setDes_concepto(String des_concepto) {
        this.des_concepto = des_concepto;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public String getRegreso() {
        return regreso;
    }

    public void setRegreso(String regreso) {
        this.regreso = regreso;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public double getCargo() {
        return cargo;
    }

    public void setCargo(double cargo) {
        this.cargo = cargo;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public Date getFecha_mov() {
        return fecha_mov;
    }

    public void setFecha_mov(Date fecha_mov) {
        this.fecha_mov = fecha_mov;
    }

    public String getSfecha_mov() {
        sfecha_mov = sdf.format(fecha_mov);
        return sfecha_mov;
    }

    public void setSfecha_mov(String sfecha_mov) {
        this.sfecha_mov = sfecha_mov;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    

}
