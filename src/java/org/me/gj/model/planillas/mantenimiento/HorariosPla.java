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
public class HorariosPla {
    
    private String horario_id;
    private String hor_descrip;
    
    public HorariosPla(){
    
    }
    
    public HorariosPla(String horario_id,String hor_descrip){
    
    }

    public String getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(String horario_id) {
        this.horario_id = horario_id;
    }

    public String getHor_descrip() {
        return hor_descrip;
    }

    public void setHor_descrip(String hor_descrip) {
        this.hor_descrip = hor_descrip;
    }
    
    
    
}
