package org.me.gj.model.logistica.informes;

public class FacturaProd extends MontosTotales {

    long pro_key;
    String pro_id;
    String pro_des;

    public FacturaProd() {
        super();
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }
}
