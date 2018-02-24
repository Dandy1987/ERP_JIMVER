package org.me.gj.model.logistica.informes;

public class NotaESLin extends MontosTotales {

    private int lin_key;
    private String lin_id;
    private String lin_des;

    public NotaESLin() {
        super();
    }

    public int getLin_key() {
        return lin_key;
    }

    public void setLin_key(int lin_key) {
        this.lin_key = lin_key;
    }

    public String getLin_id() {
        return lin_id;
    }

    public void setLin_id(String lin_id) {
        this.lin_id = lin_id;
    }

    public String getLin_des() {
        return lin_des;
    }

    public void setLin_des(String lin_des) {
        this.lin_des = lin_des;
    }

}
