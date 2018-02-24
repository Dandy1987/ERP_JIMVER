package org.me.gj.model.planillas.informes;

import org.me.gj.util.Utilitarios;

/**
 *
 * @author achocano
 */
public class InformesMovimiento {

    private String codigo;
    private String nombres;
    private String periodo;
    private String desperiodo;
    private String id_concepto;
    private String des_concepto;
    private double monto;
    private String cod_constante;
    private String des_constante;
    private boolean valSelec;
    private String empresa;
    private String s_monto;

    public InformesMovimiento() {
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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDesperiodo() {
        return desperiodo;
    }

    public void setDesperiodo(String desperiodo) {
        this.desperiodo = desperiodo;
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getCod_constante() {
        return cod_constante;
    }

    public void setCod_constante(String cod_constante) {
        this.cod_constante = cod_constante;
    }

    public String getDes_constante() {
        return des_constante;
    }

    public void setDes_constante(String des_constante) {
        this.des_constante = des_constante;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getS_monto() {
        s_monto = Utilitarios.formatoDecimal(monto, "###,##0.00");
        return s_monto;
    }

    public void setS_monto(String s_monto) {
        this.s_monto = s_monto;
    }

}
