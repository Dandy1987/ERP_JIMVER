package org.me.gj.model.seguridad.mantenimiento;

public class Preguntas {

    private int preg_id;
    private String preg_des;
    private int preg_est;
    private String preg_nomrep;
    private int preg_ord;
    private boolean valor;

    public Preguntas(int preg_id, String preg_des, int preg_est, int preg_ord, String preg_nomrep) {
        this.preg_id = preg_id;
        this.preg_des = preg_des;
        this.preg_est = preg_est;
        this.preg_ord = preg_ord;
        this.preg_nomrep = preg_nomrep;
    }

    public Preguntas() {
    }

    public boolean isValor() {
        if (preg_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPreg_nomrep() {
        return preg_nomrep;
    }

    public void setPreg_nomrep(String preg_nomrep) {
        this.preg_nomrep = preg_nomrep;
    }

    public int getPreg_ord() {
        return preg_ord;
    }

    public void setPreg_ord(int preg_ord) {
        this.preg_ord = preg_ord;
    }

    public int getPreg_est() {
        return preg_est;
    }

    public void setPreg_est(int preg_est) {
        this.preg_est = preg_est;
    }

    public String getPreg_des() {
        return preg_des;
    }

    public void setPreg_des(String preg_des) {
        this.preg_des = preg_des;
    }

    public int getPreg_id() {
        return preg_id;
    }

    public void setPreg_id(int preg_id) {
        this.preg_id = preg_id;
    }
}
