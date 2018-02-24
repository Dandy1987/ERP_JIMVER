package org.me.gj.model.distribucion.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Soat {

    private int soat_id;
    private int tab_id;//empresa
    private String tab_subdes; //solo consulta
    private int tab_cod;//aseguradora
    private int trans_key;//transporte
    private String trans_alias;
    private int suc_id;//sucursal
    private int emp_id;//empresa
    private String soat_nro;
    private Date soat_fecini;
    private Date soat_fecfin;
    private int soat_est;//estado
    private String soat_usuadd;
    private Date soat_fecadd;
    private String soat_usumod;
    private Date soat_fecmod;
    private boolean valor = false;
    private String soat_fechainicial;
    private String soat_fechafinal;
    private String ind_accion = "Q";

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Soat(int soat_id, int tab_id, int tab_cod, int trans_key, int suc_id, int emp_id, String soat_nro, Date soat_fecini,
            Date soat_fecfin, int soat_est, String soat_usuadd, Date soat_fecadd, String soat_usumod, Date soat_fecmod, boolean valor) {
        this.soat_id = soat_id;
        this.tab_id = tab_id;
        this.tab_cod = tab_cod;
        this.trans_key = trans_key;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.soat_nro = soat_nro;
        this.soat_fecini = soat_fecini;
        this.soat_fecfin = soat_fecfin;
        this.soat_est = soat_est;
        this.soat_usuadd = soat_usuadd;
        this.soat_fecadd = soat_fecadd;
        this.soat_usumod = soat_usumod;
        this.soat_fecmod = soat_fecmod;
        this.valor = valor;
    }

    public Soat(int soat_id, int tab_id, String tab_subdes, int tab_cod, int trans_key, String trans_alias, int suc_id, int emp_id,
            String soat_nro, Date soat_fecini, Date soat_fecfin, int soat_est, String soat_usuadd, Date soat_fecadd, String soat_usumod,
            Date soat_fecmod, boolean valor) {
        this.soat_id = soat_id;
        this.tab_id = tab_id;
        this.tab_subdes = tab_subdes;
        this.tab_cod = tab_cod;
        this.trans_key = trans_key;
        this.trans_alias = trans_alias;
        this.suc_id = suc_id;
        this.emp_id = emp_id;
        this.soat_nro = soat_nro;
        this.soat_fecini = soat_fecini;
        this.soat_fecfin = soat_fecfin;
        this.soat_est = soat_est;
        this.soat_usuadd = soat_usuadd;
        this.soat_fecadd = soat_fecadd;
        this.soat_usumod = soat_usumod;
        this.soat_fecmod = soat_fecmod;
        this.valor = valor;
    }

    public Soat() {
    }

    public String getTab_subdes() {
        return tab_subdes;
    }

    public void setTab_subdes(String tab_subdes) {
        this.tab_subdes = tab_subdes;
    }

    public String getTrans_alias() {
        return trans_alias;
    }

    public void setTrans_alias(String trans_alias) {
        this.trans_alias = trans_alias;
    }

    public int getSoat_id() {
        return soat_id;
    }

    public void setSoat_id(int soat_id) {
        this.soat_id = soat_id;
    }

    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public int getTab_cod() {
        return tab_cod;
    }

    public void setTab_cod(int tab_cod) {
        this.tab_cod = tab_cod;
    }

    public int getTrans_key() {
        return trans_key;
    }

    public void setTrans_key(int trans_key) {
        this.trans_key = trans_key;
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

    public String getSoat_nro() {
        return soat_nro;
    }

    public void setSoat_nro(String soat_nro) {
        this.soat_nro = soat_nro;
    }

    /*public Date getSoat_fecini() throws ParseException {
     String fecha_cadena = sdf.format(soat_fecini);
     Date fecha_date = sdf.parse(fecha_cadena);
     return fecha_date;
     }*/
    public Date getSoat_fecini() {
        return soat_fecini;
    }

    public void setSoat_fecini(Date soat_fecini) {
        this.soat_fecini = soat_fecini;
    }

    public Date getSoat_fecfin() {
        return soat_fecfin;
    }

    public void setSoat_fecfin(Date soat_fecfin) {
        this.soat_fecfin = soat_fecfin;
    }

    public int getSoat_est() {
        return soat_est;
    }

    public void setSoat_est(int soat_est) {
        this.soat_est = soat_est;
    }

    public String getSoat_usuadd() {
        return soat_usuadd;
    }

    public void setSoat_usuadd(String soat_usuadd) {
        this.soat_usuadd = soat_usuadd;
    }

    public Date getSoat_fecadd() {
        return soat_fecadd;
    }

    public void setSoat_fecadd(Date soat_fecadd) {
        this.soat_fecadd = soat_fecadd;
    }

    public String getSoat_usumod() {
        return soat_usumod;
    }

    public void setSoat_usumod(String soat_usumod) {
        this.soat_usumod = soat_usumod;
    }

    public Date getSoat_fecmod() {
        return soat_fecmod;
    }

    public void setSoat_fecmod(Date soat_fecmod) {
        this.soat_fecmod = soat_fecmod;
    }

    public boolean isValor() {
        if (soat_est == 1) {
            valor = true;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getSoat_fechainicial() {
        soat_fechainicial = sdf.format(soat_fecini);
        return soat_fechainicial;
    }

    public void setSoat_fechainicial(String soat_fechainicial) {
        this.soat_fechainicial = soat_fechainicial;
    }

    public String getSoat_fechafinal() {
        soat_fechafinal = sdf.format(soat_fecfin);
        return soat_fechafinal;
    }

    public void setSoat_fechafinal(String soat_fechafinal) {
        this.soat_fechafinal = soat_fechafinal;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }
}
