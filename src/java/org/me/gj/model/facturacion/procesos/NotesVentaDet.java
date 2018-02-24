package org.me.gj.model.facturacion.procesos;

public class NotesVentaDet extends PedidoVentaDet {

    private double nesdet_cant;
    private int nesdet_frac;
    private int nesdet_ent;
    private int nesdet_undpre;

    public double getNesdet_cant() {
        return nesdet_cant;
    }

    public void setNesdet_cant(double nesdet_cant) {
        this.nesdet_cant = nesdet_cant;
    }

    public int getNesdet_undpre() {
        return nesdet_undpre;
    }

    public void setNesdet_undpre(int nesdet_undpre) {
        this.nesdet_undpre = nesdet_undpre;
    }

    public int getNesdet_ent() {
        nesdet_ent = (int) (nesdet_cant / nesdet_undpre);
        return nesdet_ent;
    }

    public void setNesdet_ent(int nesdet_ent) {
        this.nesdet_ent = nesdet_ent;
    }

    public int getNesdet_frac() {
        nesdet_frac = (int) nesdet_cant % nesdet_undpre;
        return nesdet_frac;
    }

    public void setNesdet_frac(int nesdet_frac) {
        this.nesdet_frac = nesdet_frac;
    }

}
