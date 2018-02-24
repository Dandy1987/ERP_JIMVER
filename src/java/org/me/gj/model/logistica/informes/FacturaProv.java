package org.me.gj.model.logistica.informes;

public class FacturaProv extends MontosTotales {

    String tc_provid;
    String tc_provrazsoc;

    public FacturaProv() {
        super();
    }

    public String getTc_provid() {
        return tc_provid;
    }

    public void setTc_provid(String tc_provid) {
        this.tc_provid = tc_provid;
    }

    public String getTc_provrazsoc() {
        return tc_provrazsoc;
    }

    public void setTc_provrazsoc(String tc_provrazsoc) {
        this.tc_provrazsoc = tc_provrazsoc;
    }
}
