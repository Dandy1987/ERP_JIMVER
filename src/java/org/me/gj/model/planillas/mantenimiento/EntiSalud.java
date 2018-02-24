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
public class EntiSalud {

    private String entisalud_id;
    private String entisalud_des;

    public EntiSalud() {
    }

    public EntiSalud(String entisalud_id, String entisalud_des) {
        this.entisalud_id = entisalud_id;
        this.entisalud_des = entisalud_des;
    }

    public String getEntisalud_id() {
        return entisalud_id;
    }

    public void setEntisalud_id(String entisalud_id) {
        this.entisalud_id = entisalud_id;
    }

    public String getEntisalud_des() {
        return entisalud_des;
    }

    public void setEntisalud_des(String entisalud_des) {
        this.entisalud_des = entisalud_des;
    }

}
