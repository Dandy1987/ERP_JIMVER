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
public class UbigeoPla {

    String ubi_id;
    String ubi_des;

    public UbigeoPla() {

    }

    public UbigeoPla(String ubi_id, String ubi_des) {
        this.ubi_id = ubi_id;
        this.ubi_des = ubi_des;
    }

    public String getUbi_id() {
        return ubi_id;
    }

    public void setUbi_id(String ubi_id) {
        this.ubi_id = ubi_id;
    }

    public String getUbi_des() {
        return ubi_des;
    }

    public void setUbi_des(String ubi_des) {
        this.ubi_des = ubi_des;
    }

}
