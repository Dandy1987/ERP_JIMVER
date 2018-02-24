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
public class Situacion {

    private String situ_id;
    private String situ_des;

    public Situacion() {

    }

    public Situacion(String situ_id, String situ_des) {
        this.situ_id = situ_id;
        this.situ_des = situ_des;
    }

    public String getSitu_id() {
        return situ_id;
    }

    public void setSitu_id(String situ_id) {
        this.situ_id = situ_id;
    }

    public String getSitu_des() {
        return situ_des;
    }

    public void setSitu_des(String situ_des) {
        this.situ_des = situ_des;
    }

}
