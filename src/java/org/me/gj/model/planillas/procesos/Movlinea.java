/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.procesos;

import java.util.Date;
import org.me.gj.util.Utilitarios;

/**
 *
 * @author achocano
 */
public class Movlinea {

    private int empresa;
    private int sucursal;
    private int tipo_doc;
    private String numero_doc;
    private String periodo_proceso;
    private String id_concepto;
    private double valor_concepto;
    private String usu_add;
    private Date fecha_add;
    private String usu_mod;
    private Date fecha_mod;
    private String pc_add;
    private String pc_mod;
    private String descripcion;
    private String ind_accion = "Q";
    //para consultar
    private String paterno;
    private String materno;
    private String nombre;
    private String area;
    private String codigo_vista;
    private String tipo;
    private boolean valSelec;
    private double valor_constante;
    private double valor_constante_mesual;
    private String svalor_concepto;

    private int nro_ope;

    public Movlinea() {
    }

    public Movlinea(String id_concepto, double valor_concepto, String descripcion) {
        this.id_concepto = id_concepto;
        this.valor_concepto = valor_concepto;
        this.descripcion = descripcion;
    }

    public Movlinea(int empresa, int sucursal, int tipo_doc, String numero_doc, String periodo_proceso, String id_concepto, Byte valor_concepto, String usu_add, Date fecha_add, String usu_mod, Date fecha_mod, String pc_add, String pc_mod) {
        this.empresa = empresa;
        this.sucursal = sucursal;
        this.tipo_doc = tipo_doc;
        this.numero_doc = numero_doc;
        this.periodo_proceso = periodo_proceso;
        this.id_concepto = id_concepto;
        this.valor_concepto = valor_concepto;
        this.usu_add = usu_add;
        this.fecha_add = fecha_add;
        this.usu_mod = usu_mod;
        this.fecha_mod = fecha_mod;
        this.pc_add = pc_add;
        this.pc_mod = pc_mod;
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

    public int getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getNumero_doc() {
        return numero_doc;
    }

    public void setNumero_doc(String numero_doc) {
        this.numero_doc = numero_doc;
    }

    public String getPeriodo_proceso() {
        return periodo_proceso;
    }

    public void setPeriodo_proceso(String periodo_proceso) {
        this.periodo_proceso = periodo_proceso;
    }

    public String getId_concepto() {
        return id_concepto;
    }

    public void setId_concepto(String id_concepto) {
        this.id_concepto = id_concepto;
    }

    public double getValor_concepto() {
        return valor_concepto;
    }

    public void setValor_concepto(double valor_concepto) {
        this.valor_concepto = valor_concepto;
    }

    public String getSvalor_concepto() {
        svalor_concepto = Utilitarios.formatoDecimal(valor_concepto, "###0.000");
        return svalor_concepto;
    }

    public void setSvalor_concepto(String svalor_concepto) {
        this.svalor_concepto = svalor_concepto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCodigo_vista() {
        return codigo_vista;
    }

    public void setCodigo_vista(String codigo_vista) {
        this.codigo_vista = codigo_vista;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public double getValor_constante() {
        return valor_constante;
    }

    public void setValor_constante(double valor_constante) {
        this.valor_constante = valor_constante;
    }

    public double getValor_constante_mesual() {
        return valor_constante_mesual;
    }

    public void setValor_constante_mesual(double valor_constante_mesual) {
        this.valor_constante_mesual = valor_constante_mesual;
    }

    public int getNro_ope() {
        return nro_ope;
    }

    public void setNro_ope(int nro_ope) {
        this.nro_ope = nro_ope;
    }

}
