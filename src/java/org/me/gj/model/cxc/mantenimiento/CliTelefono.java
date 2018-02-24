package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class CliTelefono {

    private long clitel_id;
    private String cli_id;
    private long cli_key;
    private long clitel_telef1;
    private long clitel_telef2;
    private long clitel_movil;
    private int clitel_est;
    private String clitel_usuadd;
    private Date clitel_fecadd;
    private String clitel_usumod;
    private Date clitel_fecmod;
    private String ind_accion = "Q";
    private boolean valor;

    public CliTelefono(String cli_id, long cli_key, long clitel_id, String clitel_usumod) {
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clitel_id = clitel_id;
        this.clitel_usumod = clitel_usumod;
    }

    public CliTelefono(long clitel_id, String cli_id, long cli_key, long clitel_telef1, long clitel_telef2, long clitel_movil, int clitel_est, String clitel_usuadd, Date clitel_fecadd, String clitel_usumod, Date clitel_fecmod) {
        this.clitel_id = clitel_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clitel_telef1 = clitel_telef1;
        this.clitel_telef2 = clitel_telef2;
        this.clitel_movil = clitel_movil;
        this.clitel_est = clitel_est;
        this.clitel_usuadd = clitel_usuadd;
        this.clitel_fecadd = clitel_fecadd;
        this.clitel_usumod = clitel_usumod;
        this.clitel_fecmod = clitel_fecmod;
    }

    public CliTelefono(/*long clitel_id,*/ String cli_id, long cli_key, long clitel_telef1, long clitel_telef2, long clitel_movil, int clitel_est, String clitel_usuadd, String clitel_usumod) {
       // this.clitel_id = clitel_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.clitel_telef1 = clitel_telef1;
        this.clitel_telef2 = clitel_telef2;
        this.clitel_movil = clitel_movil;
        this.clitel_est = clitel_est;
        this.clitel_usuadd = clitel_usuadd;
        this.clitel_usumod = clitel_usumod;
    }

    public CliTelefono() {
    }

    public long getClitel_id() {
        return clitel_id;
    }

    public void setClitel_id(long clitel_id) {
        this.clitel_id = clitel_id;
    }

    public String getCli_id() {
        return cli_id;
    }
    public void setCli_id(String CLI_ID) {
        this.cli_id = CLI_ID;
    }

    public long getCli_key() {
        return cli_key;
    }

    public void setCli_key(long cli_key) {
        this.cli_key = cli_key;
    }

    public long getClitel_telef1() {
        return clitel_telef1;
    }

    public void setClitel_telef1(long clitel_telef1) {
        this.clitel_telef1 = clitel_telef1;
    }

    public long getClitel_telef2() {
        return clitel_telef2;
    }

    public void setClitel_telef2(long clitel_telef2) {
        this.clitel_telef2 = clitel_telef2;
    }

    public long getClitel_movil() {
        return clitel_movil;
    }

    public void setClitel_movil(long clitel_movil) {
        this.clitel_movil = clitel_movil;
    }

    public int getClitel_est() {
        return clitel_est;
    }

    public void setClitel_est(int clitel_est) {
        this.clitel_est = clitel_est;
    }

    public String getClitel_usuadd() {
        return clitel_usuadd;
    }

    public void setClitel_usuadd(String clitel_usuadd) {
        this.clitel_usuadd = clitel_usuadd;
    }

    public Date getClitel_fecadd() {
        return clitel_fecadd;
    }

    public void setClitel_fecadd(Date clitel_fecadd) {
        this.clitel_fecadd = clitel_fecadd;
    }

    public String getClitel_usumod() {
        return clitel_usumod;
    }

    public void setClitel_usumod(String clitel_usumod) {
        this.clitel_usumod = clitel_usumod;
    }

    public Date getClitel_fecmod() {
        return clitel_fecmod;
    }

    public void setClitel_fecmod(Date clitel_fecmod) {
        this.clitel_fecmod = clitel_fecmod;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isValor() {
        valor = clitel_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

}
