package org.me.gj.model.cxc.informes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CliObservacion {

    private int emp_id;
    private int suc_id;
    private String cli_id;
    private int cli_nroope;
    private String cli_observa;
    private String cli_usuadd;
    private Date cli_fecadd;
    private String cli_usumod;
    private Date cli_fecmod;
    private String cli_tipobs;
    private String s_cli_fecadd;

    public CliObservacion() {
    }

    public CliObservacion(int emp_id, int suc_id, String cli_id, int cli_nroope, String cli_observa, String cli_usuadd, String cli_usumod, String cli_tipobs) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.cli_id = cli_id;
        this.cli_nroope = cli_nroope;
        this.cli_observa = cli_observa;
        this.cli_usuadd = cli_usuadd;
        this.cli_usumod = cli_usumod;
        this.cli_tipobs = cli_tipobs;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public String getCli_id() {
        return cli_id;
    }

    public void setCli_id(String cli_id) {
        this.cli_id = cli_id;
    }

    public int getCli_nroope() {
        return cli_nroope;
    }

    public void setCli_nroope(int cli_nroope) {
        this.cli_nroope = cli_nroope;
    }

    public String getCli_observa() {
        return cli_observa;
    }

    public void setCli_observa(String cli_observa) {
        this.cli_observa = cli_observa;
    }

    public String getCli_usuadd() {
        return cli_usuadd;
    }

    public void setCli_usuadd(String cli_usuadd) {
        this.cli_usuadd = cli_usuadd;
    }

    public Date getCli_fecadd() {
        return cli_fecadd;
    }

    public void setCli_fecadd(Date cli_fecadd) {
        this.cli_fecadd = cli_fecadd;
    }

    public String getCli_usumod() {
        return cli_usumod;
    }

    public void setCli_usumod(String cli_usumod) {
        this.cli_usumod = cli_usumod;
    }

    public Date getCli_fecmod() {
        return cli_fecmod;
    }

    public void setCli_fecmod(Date cli_fecmod) {
        this.cli_fecmod = cli_fecmod;
    }

    public String getCli_tipobs() {
        return cli_tipobs;
    }

    public void setCli_tipobs(String cli_tipobs) {
        this.cli_tipobs = cli_tipobs;
    }

    public String getS_cli_fecadd() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        s_cli_fecadd = sdf.format(cli_fecadd);
        return s_cli_fecadd;
    }

    public void setS_cli_fecadd(String s_cli_fecadd) {
        this.s_cli_fecadd = s_cli_fecadd;
    }
}
