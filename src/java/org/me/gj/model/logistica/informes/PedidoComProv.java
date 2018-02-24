package org.me.gj.model.logistica.informes;

public class PedidoComProv extends MontosTotales {

    private String prov_id;
    private long prov_key;
    private String prov_razsoc;

    public PedidoComProv() {
        super();
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public long getProv_key() {
        return prov_key;
    }

    public void setProv_key(long prov_key) {
        this.prov_key = prov_key;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

}
