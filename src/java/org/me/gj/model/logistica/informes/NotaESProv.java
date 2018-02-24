package org.me.gj.model.logistica.informes;

public class NotaESProv extends MontosTotales {

    private String provid;
    private String provrazsoc;

    public NotaESProv() {
        super();
    }

    public String getProvid() {
        return provid;
    }

    public void setProvid(String provid) {
        this.provid = provid;
    }

    public String getProvrazsoc() {
        return provrazsoc;
    }

    public void setProvrazsoc(String provrazsoc) {
        this.provrazsoc = provrazsoc;
    }

}
