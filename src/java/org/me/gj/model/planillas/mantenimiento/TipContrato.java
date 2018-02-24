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
public class TipContrato {

    private String tipcont_id;
    private String tipcont_des;

    public TipContrato() {

    }

    public TipContrato(String tipcont_id, String tipcont_des) {
        this.tipcont_id = tipcont_id;
        this.tipcont_des = tipcont_des;
    }

    public String getTipcont_id() {
        return tipcont_id;
    }

    public void setTipcont_id(String tipcont_id) {
        this.tipcont_id = tipcont_id;
    }

    public String getTipcont_des() {
        return tipcont_des;
    }

    public void setTipcont_des(String tipcont_des) {
        this.tipcont_des = tipcont_des;
    }

}
