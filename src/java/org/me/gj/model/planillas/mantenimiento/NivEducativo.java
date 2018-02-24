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
public class NivEducativo {

    private String nivedu_id;
    private String nivedu_des;

    public NivEducativo() {

    }

    public NivEducativo(String nivedu_id, String nivedu_des) {
        this.nivedu_id = nivedu_id;
        this.nivedu_des = nivedu_des;
    }

    public String getNivedu_id() {
        return nivedu_id;
    }

    public void setNivedu_id(String nivedu_id) {
        this.nivedu_id = nivedu_id;
    }

    public String getNivedu_des() {
        return nivedu_des;
    }

    public void setNivedu_des(String nivedu_des) {
        this.nivedu_des = nivedu_des;
    }

}
