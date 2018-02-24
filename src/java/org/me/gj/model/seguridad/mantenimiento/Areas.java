package org.me.gj.model.seguridad.mantenimiento;

public class Areas {

    private int are_id;
    private String are_des;

    public Areas(int are_id, String are_des) {
        this.are_id = are_id;
        this.are_des = are_des;
    }

    public Areas() {
    }

    public String getAre_des() {
        return are_des;
    }

    public void setAre_des(String are_des) {
        this.are_des = are_des;
    }

    public int getAre_id() {
        return are_id;
    }

    public void setAre_id(int are_id) {
        this.are_id = are_id;
    }
}
