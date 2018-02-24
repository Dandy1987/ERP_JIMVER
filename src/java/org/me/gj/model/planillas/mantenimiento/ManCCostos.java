/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

/**
 *
 * @author greyes
 */
public class ManCCostos {

    private String costo_cod;
    private String costo_des;
    private boolean valSelec;

    public ManCCostos() {

    }

    public ManCCostos(String costo_cod, String costo_des) {
        this.costo_cod = costo_cod;
        this.costo_des = costo_des;

    }

    public String getCosto_cod() {
        return costo_cod;
    }

    public void setCosto_cod(String costo_cod) {
        this.costo_cod = costo_cod;
    }

    public String getCosto_des() {
        return costo_des;
    }

    public void setCosto_des(String costo_des) {
        this.costo_des = costo_des;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

}
