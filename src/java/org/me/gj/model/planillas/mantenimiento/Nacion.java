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
public class Nacion {
    
    private String nacion_id;
    private String nacion_des;
    
    public Nacion(){
    
    }
    
    public Nacion(String nacion_id,String nacion_des){
    this.nacion_id = nacion_id;
    this.nacion_des = nacion_des;
    
    }

    public String getNacion_id() {
        return nacion_id;
    }

    public void setNacion_id(String nacion_id) {
        this.nacion_id = nacion_id;
    }

    public String getNacion_des() {
        return nacion_des;
    }

    public void setNacion_des(String nacion_des) {
        this.nacion_des = nacion_des;
    }
    
    
    
}
