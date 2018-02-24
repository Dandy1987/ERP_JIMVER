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
public class RegPensionario {

    private String reg_id;
    private String reg_des;
    
    //para mostrar afp
    private String id;
    private String descr;

    public RegPensionario() {

    }

    public RegPensionario(String reg_id, String reg_des) {
        this.reg_id = reg_id;
        this.reg_des = reg_des;

    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_des() {
        return reg_des;
    }

    public void setReg_des(String reg_des) {
        this.reg_des = reg_des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
    
    

}
