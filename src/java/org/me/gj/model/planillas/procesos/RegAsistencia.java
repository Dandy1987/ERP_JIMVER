/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.procesos;

import java.util.Date;

/**
 *
 * @author achocano
 */
public class RegAsistencia {

    private int emp_id;//empresa
    private int suc_id;//sucursal
    private int tipo_doc;
    private String dni;//codigo personal
    private Date fec_mov;//fecha movimiento
    private String hor_reg;//hora registrada
    private String nombres;//nombres
    private String flag;//tipo de marcado
    private String codhor;//Codigo de horario

    public RegAsistencia() {
    }

    public RegAsistencia(int emp_id, int suc_id, int tipo_doc, String dni, String flag, String codhor) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tipo_doc = tipo_doc;
        this.dni = dni;
        this.flag = flag;
        this.codhor = codhor;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public Date getFec_mov() {
        return fec_mov;
    }

    public void setFec_mov(Date fec_mov) {
        this.fec_mov = fec_mov;
    }

    public String getHor_reg() {
        return hor_reg;
    }

    public void setHor_reg(String hor_reg) {
        this.hor_reg = hor_reg;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCodhor() {
        return codhor;
    }

    public void setCodhor(String codhor) {
        this.codhor = codhor;
    }

}