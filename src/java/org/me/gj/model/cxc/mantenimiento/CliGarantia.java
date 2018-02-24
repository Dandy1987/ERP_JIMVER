package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class CliGarantia {

    private long cligar_id;
    private String cli_id;
    private long cli_key;
    private int suc_id;
    private int emp_id;
    private String cligar_garantia;
    private long cligar_monto;
    private String cligar_obs;
    private int cligar_est;
    private String cligar_usuadd;
    private Date cligar_fecadd;
    private String cligar_usumod;
    private Date cligar_fecmod;
    private String ind_accion = "Q";
    private boolean valor;

    public CliGarantia(int emp_id, int suc_id, String cli_id, long cli_key, long cligar_id, String cligar_usumod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.cligar_id = cligar_id;
        this.cligar_usumod = cligar_usumod;
    }

    public CliGarantia(long CLIGAR_ID, String CLI_ID, long CLI_KEY, int SUC_ID, int EMP_ID, String CLIGAR_GARANTIA, long CLIGAR_MONTO, String CLIGAR_OBS, int CLIGAR_EST, String CLIGAR_USUADD, Date CLIGAR_FECADD, String CLIGAR_USUMOD, Date CLIGAR_FECMOD) {
        this.cligar_id = CLIGAR_ID;
        this.cli_id = CLI_ID;
        this.cli_key = CLI_KEY;
        this.suc_id = SUC_ID;
        this.emp_id = EMP_ID;
        this.cligar_garantia = CLIGAR_GARANTIA;
        this.cligar_monto = CLIGAR_MONTO;
        this.cligar_obs = CLIGAR_OBS;
        this.cligar_est = CLIGAR_EST;
        this.cligar_usuadd = CLIGAR_USUADD;
        this.cligar_fecadd = CLIGAR_FECADD;
        this.cligar_usumod = CLIGAR_USUMOD;
        this.cligar_fecmod = CLIGAR_FECMOD;
    }

    public CliGarantia() {
    }

    public long getCligar_id() {
        return cligar_id;
    }

    public void setCligar_id(long cligar_id) {
        this.cligar_id = cligar_id;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public long getCli_key() {
        return cli_key;
    }

    public void setCli_key(long cli_key) {
        this.cli_key = cli_key;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getCligar_garantia() {
        return cligar_garantia;
    }

    public void setCligar_garantia(String cligar_garantia) {
        this.cligar_garantia = cligar_garantia;
    }

    public long getCligar_monto() {
        return cligar_monto;
    }

    public void setCligar_monto(long cligar_monto) {
        this.cligar_monto = cligar_monto;
    }

    public String getCligar_obs() {
        return cligar_obs;
    }

    public void setCligar_obs(String cligar_obs) {
        this.cligar_obs = cligar_obs;
    }

    public int getCligar_est() {
        return cligar_est;
    }

    public void setCligar_est(int cligar_est) {
        this.cligar_est = cligar_est;
    }

    public String getCligar_usuadd() {
        return cligar_usuadd;
    }

    public void setCligar_usuadd(String cligar_usuadd) {
        this.cligar_usuadd = cligar_usuadd;
    }

    public Date getCligar_fecadd() {
        return cligar_fecadd;
    }

    public void setCligar_fecadd(Date cligar_fecadd) {
        this.cligar_fecadd = cligar_fecadd;
    }

    public String getCligar_usumod() {
        return cligar_usumod;
    }

    public void setCligar_usumod(String cligar_usumod) {
        this.cligar_usumod = cligar_usumod;
    }

    public Date getCligar_fecmod() {
        return cligar_fecmod;
    }

    public void setCligar_fecmod(Date cligar_fecmod) {
        this.cligar_fecmod = cligar_fecmod;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public boolean isValor() {
        valor = cligar_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
