/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.procesos;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author achocano
 */
public class Descuentos {

    private int sucursal;
    private int tipo_doc;
    private String documento;
    private String periodo;
    //private int nro_operacion;
    private String cod_constante;
    private String des_constante;
    private int numero_op;//numero de operacion
    private Date fecha_mov;//fecha de movimiento
    private String glosa;
    private double cargo;
    private double abono;
    private String recibo_egreso;
    private String recibo_egreso_referencia;
    private String usu_add;
    private String usu_mod;
    private Date fecha_add;
    private Date fecha_mod;
    private String ind_accion = "Q";
    private boolean valSelec;
    private String fecha_movimiento;
    private int tipo_ingreso;
    
      //para consultar
    private String paterno;
    private String materno;
    private String nombre;
    private String area;
    private String codigo_vista;
    private Date fec_ing;
    private Date fec_ces;
    private String sucu;
    private double neto;

  
    
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Descuentos() {
    }

    public Descuentos(int tipo_doc, String documento, String periodo, String cod_constante, Date fecha_mov, String glosa, double cargo, double abono) {
        this.tipo_doc = tipo_doc;
        this.documento = documento;
        this.periodo = periodo;
        this.cod_constante = cod_constante;
        this.fecha_mov = fecha_mov;
        this.glosa = glosa;
        this.cargo = cargo;
        this.abono = abono;
    }

    public Descuentos(String cod_constante, Date fecha_mov, String glosa, double cargo, double abono) {
        this.cod_constante = cod_constante;
        this.fecha_mov = fecha_mov;
        this.glosa = glosa;
        this.cargo = cargo;
        this.abono = abono;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getCod_constante() {
        return cod_constante;
    }

    public void setCod_constante(String cod_constante) {
        this.cod_constante = cod_constante;
    }

    public int getNumero_op() {
        return numero_op;
    }

    public void setNumero_op(int numero_op) {
        this.numero_op = numero_op;
    }

    public Date getFecha_mov() {
        return fecha_mov;
    }

    public void setFecha_mov(Date fecha_mov) {
        this.fecha_mov = fecha_mov;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
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

    public String getRecibo_egreso() {
        return recibo_egreso;
    }

    public void setRecibo_egreso(String recibo_egreso) {
        this.recibo_egreso = recibo_egreso;
    }

    public String getRecibo_egreso_referencia() {
        return recibo_egreso_referencia;
    }

    public void setRecibo_egreso_referencia(String recibo_egreso_referencia) {
        this.recibo_egreso_referencia = recibo_egreso_referencia;
    }

    public String getUsu_add() {
        return usu_add;
    }

    public void setUsu_add(String usu_add) {
        this.usu_add = usu_add;
    }

    public String getUsu_mod() {
        return usu_mod;
    }

    public void setUsu_mod(String usu_mod) {
        this.usu_mod = usu_mod;
    }

    public Date getFecha_add() {
        return fecha_add;
    }

    public void setFecha_add(Date fecha_add) {
        this.fecha_add = fecha_add;
    }

    public Date getFecha_mod() {
        return fecha_mod;
    }

    public void setFecha_mod(Date fecha_mod) {
        this.fecha_mod = fecha_mod;
    }

    public String getDes_constante() {
        return des_constante;
    }

    public void setDes_constante(String des_constante) {
        this.des_constante = des_constante;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getFecha_movimiento() {
        fecha_movimiento = sdf.format(fecha_mov);
        return fecha_movimiento;
    }

    public void setFecha_movimiento(String fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
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

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

   
    public Date getFec_ing() {
        return fec_ing;
    }

    public void setFec_ing(Date fec_ing) {
        this.fec_ing = fec_ing;
    }

    public Date getFec_ces() {
        return fec_ces;
    }

    public void setFec_ces(Date fec_ces) {
        this.fec_ces = fec_ces;
    }

    public String getSucu() {
        return sucu;
    }

    public void setSucu(String sucu) {
        this.sucu = sucu;
    }

    public double getNeto() {
        return neto;
    }

    public void setNeto(double neto) {
        this.neto = neto;
    }

    public int getTipo_ingreso() {
        return tipo_ingreso;
    }

    public void setTipo_ingreso(int tipo_ingreso) {
        this.tipo_ingreso = tipo_ingreso;
    }
    

}
