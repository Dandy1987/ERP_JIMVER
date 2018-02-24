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
public class TipTrabajador {

    private String tiptrab_id;
    private String tiptrab_des;

    public TipTrabajador() {

    }

    public TipTrabajador(String tiptrab_id, String tiptrab_des) {
        this.tiptrab_id = tiptrab_id;
        this.tiptrab_des = tiptrab_des;
    }

    public String getTiptrab_id() {
        return tiptrab_id;
    }

    public void setTiptrab_id(String tiptrab_id) {
        this.tiptrab_id = tiptrab_id;
    }

    public String getTiptrab_des() {
        return tiptrab_des;
    }

    public void setTiptrab_des(String tiptrab_des) {
        this.tiptrab_des = tiptrab_des;
    }

}
