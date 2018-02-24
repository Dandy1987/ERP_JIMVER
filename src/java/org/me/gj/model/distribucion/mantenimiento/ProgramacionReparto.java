package org.me.gj.model.distribucion.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgramacionReparto {

    private int progrep_key;
    private String progrep_id;
    private String progrep_transid;
    private String progrep_transdes;
    private String progrep_chofid;
    private String progrep_chofdes;
    private String progrep_repartid;
    private String progrep_repartdes;
    private Date progrep_fecha;
    private int progrep_estado;
    private String progrep_horaid;
    private String progrep_horades;
    private String progrep_usuadd;
    private String progrep_usumod;
    private Date progrep_fecadd;
    private Date progrep_fecmod;
    private int emp_id;
    private int suc_id;
    private boolean valor;
    private String s_progrep_fecha;
    //Auxiliares
    private String pcambios; //valida si realizo un cambio al actualizar:  " Cambio (C) - No Cambio(NC)

    public ProgramacionReparto() {

    }

    public ProgramacionReparto(int progrep_key, String progrep_id,String progrep_transid, String progrep_chofid, 
            String progrep_repartid,Date progrep_fecha, int progrep_estado, String progrep_horaid,
            String progrep_usuadd, String progrep_usumod) {
        super();
        this.progrep_key = progrep_key;
        this.progrep_id = progrep_id;
        this.progrep_transid = progrep_transid;
        this.progrep_chofid = progrep_chofid;
        this.progrep_repartid = progrep_repartid;
        this.progrep_fecha = progrep_fecha;
        this.progrep_estado = progrep_estado;
        this.progrep_horaid = progrep_horaid;
        this.progrep_usuadd = progrep_usuadd;
        this.progrep_usumod = progrep_usumod;
        //this.pcambios = pcambios;
    }

    public int getProgrep_key() {
        return progrep_key;
    }

    public void setProgrep_key(int progrep_key) {
        this.progrep_key = progrep_key;
    }

    public String getProgrep_id() {
        return progrep_id;
    }

    public void setProgrep_id(String progrep_id) {
        this.progrep_id = progrep_id;
    }

    public String getProgrep_transid() {
        return progrep_transid;
    }

    public void setProgrep_transid(String progrep_transid) {
        this.progrep_transid = progrep_transid;
    }

    public String getProgrep_transdes() {
        return progrep_transdes;
    }

    public void setProgrep_transdes(String progrep_transdes) {
        this.progrep_transdes = progrep_transdes;
    }

    public String getProgrep_chofid() {
        return progrep_chofid;
    }

    public void setProgrep_chofid(String progrep_chofid) {
        this.progrep_chofid = progrep_chofid;
    }

    public String getProgrep_chofdes() {
        return progrep_chofdes;
    }

    public void setProgrep_chofdes(String progrep_chofdes) {
        this.progrep_chofdes = progrep_chofdes;
    }

    public String getProgrep_repartid() {
        return progrep_repartid;
    }

    public void setProgrep_repartid(String progrep_repartid) {
        this.progrep_repartid = progrep_repartid;
    }

    public String getProgrep_repartdes() {
        return progrep_repartdes;
    }

    public void setProgrep_repartdes(String progrep_repartdes) {
        this.progrep_repartdes = progrep_repartdes;
    }

    public Date getProgrep_fecha() {
        return progrep_fecha;
    }

    public void setProgrep_fecha(Date progrep_fecha) {
        this.progrep_fecha = progrep_fecha;
    }

    public int getProgrep_estado() {
        return progrep_estado;
    }

    public void setProgrep_estado(int progrep_estado) {
        this.progrep_estado = progrep_estado;
    }

    public String getProgrep_horaid() {
        return progrep_horaid;
    }

    public void setProgrep_horaid(String progrep_horaid) {
        this.progrep_horaid = progrep_horaid;
    }

    public String getProgrep_horades() {
        return progrep_horades;
    }

    public void setProgrep_horades(String progrep_horades) {
        this.progrep_horades = progrep_horades;
    }

    public String getProgrep_usuadd() {
        return progrep_usuadd;
    }

    public void setProgrep_usuadd(String progrep_usuadd) {
        this.progrep_usuadd = progrep_usuadd;
    }

    public String getProgrep_usumod() {
        return progrep_usumod;
    }

    public void setProgrep_usumod(String progrep_usumod) {
        this.progrep_usumod = progrep_usumod;
    }

    public Date getProgrep_fecadd() {
        return progrep_fecadd;
    }

    public void setProgrep_fecadd(Date progrep_fecadd) {
        this.progrep_fecadd = progrep_fecadd;
    }

    public Date getProgrep_fecmod() {
        return progrep_fecmod;
    }

    public void setProgrep_fecmod(Date progrep_fecmod) {
        this.progrep_fecmod = progrep_fecmod;
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

    public boolean isValor() {
        valor = (progrep_estado == 1);
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getS_progrep_fecha() {
        s_progrep_fecha = new SimpleDateFormat("dd/MM/yyyy").format(progrep_fecha);
        return s_progrep_fecha;
    }

    public void setS_progrep_fecha(String s_progrep_fecha) {
        this.s_progrep_fecha = s_progrep_fecha;
    }

    public String getPcambios() {
        return pcambios;
    }

    public void setPcambios(String pcambios) {
        this.pcambios = pcambios;
    }

}
