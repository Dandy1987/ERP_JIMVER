/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.utilitarios;

/**
 *
 * @author achocano
 */
public class ModelUtiComision {
    
  private int empresa;
  private int sucursal;
  private String cod_vendedor;
  private String dni;
  private int tipo_doc;
  private String personal;
  private double valor;
  private String periodo;
  private String encriptado;
  private boolean valSelec;

    public ModelUtiComision() {
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    public String getCod_vendedor() {
        return cod_vendedor;
    }

    public void setCod_vendedor(String cod_vendedor) {
        this.cod_vendedor = cod_vendedor;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getEncriptado() {
        return encriptado;
    }

    public void setEncriptado(String encriptado) {
        this.encriptado = encriptado;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
  
  
}
