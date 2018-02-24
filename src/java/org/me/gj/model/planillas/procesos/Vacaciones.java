/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.procesos;

import java.util.Date;

/**
 *
 * @author cpure
 */
public class Vacaciones {

    private String codigo;
    private String nombres;
    private String area;
    private String sucursal;
    private Date fechaingreso;
    private Date fechacese;
    private Date fechainicio;
    private Date fechafin;
    private String s_fechaingreso;
    private String s_fechacese;
    private String s_fechainicio;
    private String s_fechafin;
    private int total;
    private int gozado;
    private int pendiente;
    private int tipodoc;
    private String usu_add;
    private Date fecha_add;
    private String usu_mod;
    private Date fecha_mod;
    private String pc_add;
    private String pc_mod;
    private String glosa;
    private String idarea;
    private String nrodocpersona;
    private int idsucursal;
    private String periodogozado;
    private String periodo;
    private int dias;
	private int correlativo;
    private boolean flageliminardetalle;
	
    public Vacaciones() {
    }

    public Vacaciones(String codigo, String nombres, String area, String sucursal, Date fechaingreso, Date fechacese, int total, int gozado, int pendiente) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.area = area;
        this.sucursal = sucursal;
        this.fechaingreso = fechaingreso;
        this.fechacese = fechacese;
        this.total = total;
        this.gozado = gozado;
        this.pendiente = pendiente;
    }

	    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public boolean isFlageliminardetalle() {
        return flageliminardetalle;
    }

    public void setFlageliminardetalle(boolean flageliminardetalle) {
        this.flageliminardetalle = flageliminardetalle;
    }
	
    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(int tipodoc) {
        this.tipodoc = tipodoc;
    }
    

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public String getIdarea() {
        return idarea;
    }

    public void setIdarea(String idarea) {
        this.idarea = idarea;
    }

    public String getNrodocpersona() {
        return nrodocpersona;
    }

    public void setNrodocpersona(String nrodocpersona) {
        this.nrodocpersona = nrodocpersona;
    }

    public int getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(int idsucursal) {
        this.idsucursal = idsucursal;
    }

    public String getPeriodogozado() {
        return periodogozado;
    }

    public void setPeriodogozado(String periodogozado) {
        this.periodogozado = periodogozado;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    
    
    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getS_fechainicio() {
        return s_fechainicio;
    }

    public void setS_fechainicio(String s_fechainicio) {
        this.s_fechainicio = s_fechainicio;
    }

    public String getS_fechafin() {
        return s_fechafin;
    }

    public void setS_fechafin(String s_fechafin) {
        this.s_fechafin = s_fechafin;
    }
    
    
    public String getS_fechaingreso() {
        return s_fechaingreso;
    }

    public void setS_fechaingreso(String s_fechaingreso) {
        this.s_fechaingreso = s_fechaingreso;
    }

    public String getS_fechacese() {
        return s_fechacese;
    }

    public void setS_fechacese(String s_fechacese) {
        this.s_fechacese = s_fechacese;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechacese() {
        return fechacese;
    }

    public void setFechacese(Date fechacese) {
        this.fechacese = fechacese;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getGozado() {
        return gozado;
    }

    public void setGozado(int gozado) {
        this.gozado = gozado;
    }

    public int getPendiente() {
        return pendiente;
    }

    public void setPendiente(int pendiente) {
        this.pendiente = pendiente;
    }

}
