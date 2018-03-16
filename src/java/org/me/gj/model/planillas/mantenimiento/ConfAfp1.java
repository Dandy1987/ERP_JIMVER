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
public class ConfAfp1 {

    private String afp_id;
    private String afp_des;
	private boolean valSelec;	
    public ConfAfp1() {

    }

    public ConfAfp1(String afp_id, String afp_des) {
        this.afp_id = afp_id;
        this.afp_des = afp_des;
    }

    public String getAfp_id() {
        return afp_id;
    }

    public void setAfp_id(String afp_id) {
        this.afp_id = afp_id;
    }

    public String getAfp_des() {
        return afp_des;
    }

    public void setAfp_des(String afp_des) {
        this.afp_des = afp_des;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }
	
}