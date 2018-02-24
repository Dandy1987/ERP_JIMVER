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
public class ConfAfp2 {

    private String concep_id;
    private String concep_des;

    public ConfAfp2() {

    }

    public ConfAfp2(String concep_id, String concep_des) {
        this.concep_id = concep_id;
        this.concep_des = concep_des;
    }

    public String getConcep_id() {
        return concep_id;
    }

    public void setConcep_id(String concep_id) {
        this.concep_id = concep_id;
    }

    public String getConcep_des() {
        return concep_des;
    }

    public void setConcep_des(String concep_des) {
        this.concep_des = concep_des;
    }

}
