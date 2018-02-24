/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.informes;

import java.util.Date;

/**
 *
 * @author cpure
 */
public class InformesPrestamos {

    private String codigo;
    private String nombres;
    private int nrorestamos;
    private String fecemi;
    private String estado;
    private double monto;
    private String fechapago;
    private int nrocuota;
    private double montocuota;
    private String estadocuota;
    private double faltante;
    private double totalprestamos;
	private double totaldeuda;
    private double totalpagado;
    private String empresa;
    private String sucursal;
    private String nrodocper;
    private double porpagar;
	private String idper;


    public InformesPrestamos() {
    }

    public InformesPrestamos(String codigo, String nombres, int nrorestamos, String fecemi, String estado, double monto) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.nrorestamos = nrorestamos;
        this.fecemi = fecemi;
        this.estado = estado;
        this.monto = monto;
    }

    public InformesPrestamos(String codigo, String fechapago, int nrocuota, double montocuota, String estadocuota, double faltante) {
        this.codigo = codigo;
        this.fechapago = fechapago;
        this.nrocuota = nrocuota;
        this.montocuota = montocuota;
        this.estadocuota = estadocuota;
        this.faltante = faltante;
    }

    public double getPorpagar() {
        return porpagar;
    }

    public void setPorpagar(double porpagar) {
        this.porpagar = porpagar;
    }

    public String getNrodocper() {
        return nrodocper;
    }

    public void setNrodocper(String nrodocper) {
        this.nrodocper = nrodocper;
    }

    public String getIdper() {
        return idper;
    }

    public void setIdper(String idper) {
        this.idper = idper;
    }
	
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
    

    public String getFechapago() {
        return fechapago;
    }

    public void setFechapago(String fechapago) {
        this.fechapago = fechapago;
    }

    public int getNrocuota() {
        return nrocuota;
    }

    public void setNrocuota(int nrocuota) {
        this.nrocuota = nrocuota;
    }

    public double getMontocuota() {
        return montocuota;
    }

    public void setMontocuota(double montocuota) {
        this.montocuota = montocuota;
    }

    public String getEstadocuota() {
        return estadocuota;
    }

    public void setEstadocuota(String estadocuota) {
        this.estadocuota = estadocuota;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
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

    public int getNrorestamos() {
        return nrorestamos;
    }

    public void setNrorestamos(int nrorestamos) {
        this.nrorestamos = nrorestamos;
    }

    public String getFecemi() {
        return fecemi;
    }

    public void setFecemi(String fecemi) {
        this.fecemi = fecemi;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

	public double getTotalprestamos() {
        return totalprestamos;
    }

    public void setTotalprestamos(double totalprestamos) {
        this.totalprestamos = totalprestamos;
    }

    public double getTotaldeuda() {
        return totaldeuda;
    }

    public void setTotaldeuda(double totaldeuda) {
        this.totaldeuda = totaldeuda;
    }

    public double getTotalpagado() {
        return totalpagado;
    }

    public void setTotalpagado(double totalpagado) {
        this.totalpagado = totalpagado;
    }
	
}
