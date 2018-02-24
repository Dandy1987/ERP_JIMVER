package org.me.gj.model.contabilidad.mantenimiento;

public class TipDoc {

    private int tipdoc_key;	//Correlativo del tipo de documento
    private String tipdoc_des;	//descripcion del documento
    private String tipdoc_id;	//Codigo del Tipo de documento
    private int tipdoc_sun;	//Codigo Sunat
    private int tipdoc_ord;	//Orden del Registro
    private String tipdoc_nomrep;	//Reporte registro
    private int tipdoc_est;	//Estado Registro
    private int tipdoc_com;	//Pago Comision
    private int tipdoc_caj;	//Asume Caja
    private int tipdoc_deb;	//Aplicar Nota Debito
    private String tipdoc_mov; //Mov CxC
    private boolean valor;

    public TipDoc() {
    }

    public TipDoc(String tipdoc_id, String tipdoc_des) {
        super();
        this.tipdoc_id = tipdoc_id;
        this.tipdoc_des = tipdoc_des;

    }

    public TipDoc(int tipdoc_key, String tipdoc_des, int tipdoc_sun, int tipdoc_ord, String tipdoc_nomrep, int tipdoc_est, int tipdoc_com, int tipdoc_caj, int tipdoc_deb, String tipdoc_mov) {
        this.tipdoc_key = tipdoc_key;
        this.tipdoc_des = tipdoc_des;
        this.tipdoc_sun = tipdoc_sun;
        this.tipdoc_ord = tipdoc_ord;
        this.tipdoc_nomrep = tipdoc_nomrep;
        this.tipdoc_est = tipdoc_est;
        this.tipdoc_com = tipdoc_com;
        this.tipdoc_caj = tipdoc_caj;
        this.tipdoc_deb = tipdoc_deb;
        this.tipdoc_mov = tipdoc_mov;
    }

    public int getTipdoc_key() {
        return tipdoc_key;
    }

    public void setTipdoc_key(int tipdoc_key) {
        this.tipdoc_key = tipdoc_key;
    }

    public String getTipdoc_des() {
        return tipdoc_des;
    }

    public void setTipdoc_des(String tipdoc_des) {
        this.tipdoc_des = tipdoc_des;
    }

    public String getTipdoc_id() {
        return tipdoc_id;
    }

    public void setTipdoc_id(String tipdoc_id) {
        this.tipdoc_id = tipdoc_id;
    }

    public int getTipdoc_sun() {
        return tipdoc_sun;
    }

    public void setTipdoc_sun(int tipdoc_sun) {
        this.tipdoc_sun = tipdoc_sun;
    }

    public int getTipdoc_ord() {
        return tipdoc_ord;
    }

    public void setTipdoc_ord(int tipdoc_ord) {
        this.tipdoc_ord = tipdoc_ord;
    }

    public String getTipdoc_nomrep() {
        return tipdoc_nomrep;
    }

    public void setTipdoc_nomrep(String tipdoc_nomrep) {
        this.tipdoc_nomrep = tipdoc_nomrep;
    }

    public int getTipdoc_est() {
        return tipdoc_est;
    }

    public void setTipdoc_est(int tipdoc_est) {
        this.tipdoc_est = tipdoc_est;
    }

    public int getTipdoc_com() {
        return tipdoc_com;
    }

    public void setTipdoc_com(int tipdoc_com) {
        this.tipdoc_com = tipdoc_com;
    }

    public int getTipdoc_caj() {
        return tipdoc_caj;
    }

    public void setTipdoc_caj(int tipdoc_caj) {
        this.tipdoc_caj = tipdoc_caj;
    }

    public int getTipdoc_deb() {
        return tipdoc_deb;
    }

    public void setTipdoc_deb(int tipdoc_deb) {
        this.tipdoc_deb = tipdoc_deb;
    }

    public String getTipdoc_mov() {
        return tipdoc_mov;
    }

    public void setTipdoc_mov(String tipdoc_mov) {
        if (tipdoc_mov == null) {
            tipdoc_mov = "";
        }
        this.tipdoc_mov = tipdoc_mov;
    }

    public boolean isValor() {
        return valor = tipdoc_est == 1;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
