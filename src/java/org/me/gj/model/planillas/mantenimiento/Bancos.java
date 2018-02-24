package org.me.gj.model.planillas.mantenimiento;

import java.util.Date;

/**
 *
 * @author achocano
 */
public class Bancos {

    private int key;
    private String id;
    private String descripcion;
    private int estado;
    private String siglas;
    private int numeracion;
    private String usu_add;
    private String usu_mod;
    private Date dia_add;
    private Date dia_mod;
    private boolean valor;
    private String id_formato;

    private String desc_formato;

    public Bancos() {
    }

    public Bancos(String id, String descripcion, int estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Bancos(String id, String descripcion, int estado, String siglas, int numeracion) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.siglas = siglas;
        this.numeracion = numeracion;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public int getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(int numeracion) {
        this.numeracion = numeracion;
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

    public Date getDia_add() {
        return dia_add;
    }

    public void setDia_add(Date dia_add) {
        this.dia_add = dia_add;
    }

    public Date getDia_mod() {
        return dia_mod;
    }

    public void setDia_mod(Date dia_mod) {
        this.dia_mod = dia_mod;
    }

    public boolean isValor() {
        if (estado == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getId_formato() {
        return id_formato;
    }

    public void setId_formato(String id_formato) {
        this.id_formato = id_formato;
    }

    public String getDesc_formato() {
        return desc_formato;
    }

    public void setDesc_formato(String desc_formato) {
        this.desc_formato = desc_formato;
    }

}
