package org.me.gj.model.seguridad.mantenimiento;

public class Roles {

    private int rol_id;
    private String rol_des;

    public Roles() {
    }

    public Roles(int rol_id, String rol_des) {
        this.rol_id = rol_id;
        this.rol_des = rol_des;
    }

    public String getRol_des() {
        return rol_des;
    }

    public void setRol_des(String rol_des) {
        this.rol_des = rol_des;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

}
