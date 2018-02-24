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
public class Ocupacion {

    private String ocup_id;
    private String ocup_des;

    public Ocupacion() {

    }

    public Ocupacion(String ocup_id, String ocup_des) {
        this.ocup_id = ocup_id;
        this.ocup_des = ocup_des;

    }

    public String getOcup_id() {
        return ocup_id;
    }

    public void setOcup_id(String ocup_id) {
        this.ocup_id = ocup_id;
    }

    public String getOcup_des() {
        return ocup_des;
    }

    public void setOcup_des(String ocup_des) {
        this.ocup_des = ocup_des;
    }

}
