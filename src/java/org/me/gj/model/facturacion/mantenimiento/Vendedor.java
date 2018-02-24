package org.me.gj.model.facturacion.mantenimiento;

import java.util.Date;

public class Vendedor {

    private int ven_key;
    private String ven_id;
    private int emp_id;
    private int suc_id;
    private int sup_key;
    private String sup_id;
    private int ven_est;
    private String ven_nom;
    private String ven_ape;
    private String ven_dni;
    private long ven_cel;
    private long ven_mov;
    private int tab_id;
    private String tab_subdir;
    private String canal;
    private int tab_cod;
    private String ven_pas;
    private int ven_diamor;
    private int ven_gps;
    private String sup_des;
    private boolean valor;
    private String ven_nomrep;
    private int ven_ord;
    private String ven_usuadd;
    private Date ven_fecadd;
    private String ven_usumod;
    private Date ven_fecmod;
    private String s_vennomcompleto;
    private String mesa_id;

    public String getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(String mesa_id) {
        this.mesa_id = mesa_id;
    }
    
    

    public Vendedor() {
    }

    public Vendedor(String ven_id, String ven_nom, String ven_ape) {
        super();
        this.ven_id = ven_id;
        this.ven_nom = ven_nom;
        this.ven_ape = ven_ape;
    }

    public Vendedor(int ven_key, String ven_id, int emp_id, int suc_id, int sup_key,
            String sup_id, int ven_est, String ven_nom, String ven_ape, String ven_dni,
            long ven_cel, long ven_mov, int tab_id, String canal, int tab_cod,
            String ven_pas, int ven_diamor, int ven_gps, String sup_des,
            boolean valor, String ven_nomrep, int ven_ord, String ven_usuadd, String ven_usumod) {
        this.ven_key = ven_key;
        this.ven_id = ven_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.sup_key = sup_key;
        this.sup_id = sup_id;
        this.ven_est = ven_est;
        this.ven_nom = ven_nom;
        this.ven_ape = ven_ape;
        this.ven_dni = ven_dni;
        this.ven_cel = ven_cel;
        this.ven_mov = ven_mov;
        this.tab_id = tab_id;
        this.canal = canal;
        this.tab_cod = tab_cod;
        this.ven_pas = ven_pas;
        this.ven_diamor = ven_diamor;
        this.ven_gps = ven_gps;
        this.sup_des = sup_des;
        this.valor = valor;
        this.ven_nomrep = ven_nomrep;
        this.ven_ord = ven_ord;

        this.ven_usuadd = ven_usuadd;
        this.ven_usumod = ven_usumod;

    }

    public int getVen_key() {
        return ven_key;
    }

    public void setVen_key(int ven_key) {
        this.ven_key = ven_key;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
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

    public int getSup_key() {
        return sup_key;
    }

    public void setSup_key(int sup_key) {
        this.sup_key = sup_key;
    }

    public String getSup_id() {
        return sup_id;
    }

    public void setSup_id(String sup_id) {
        this.sup_id = sup_id;
    }

    public int getVen_est() {
        return ven_est;
    }

    public void setVen_est(int ven_est) {
        this.ven_est = ven_est;
    }

    public String getVen_nom() {
        return ven_nom;
    }

    public void setVen_nom(String ven_nom) {
        this.ven_nom = ven_nom;
    }

    public String getVen_ape() {
        return ven_ape;
    }

    public void setVen_ape(String ven_ape) {
        this.ven_ape = ven_ape;
    }

    public String getVen_dni() {
        return ven_dni;
    }

    public void setVen_dni(String ven_dni) {
        this.ven_dni = ven_dni;
    }

    public long getVen_cel() {
        return ven_cel;
    }

    public void setVen_cel(long ven_cel) {
        this.ven_cel = ven_cel;
    }

    public long getVen_mov() {
        return ven_mov;
    }

    public void setVen_mov(long ven_mov) {
        this.ven_mov = ven_mov;
    }

    public int getTab_id() {
        return tab_id;
    }

    public void setTab_id(int tab_id) {
        this.tab_id = tab_id;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public int getTab_cod() {
        return tab_cod;
    }

    public void setTab_cod(int tab_cod) {
        this.tab_cod = tab_cod;
    }

    public String getVen_pas() {
        return ven_pas;
    }

    public void setVen_pas(String ven_pas) {
        this.ven_pas = ven_pas;
    }

    public int getVen_diamor() {
        return ven_diamor;
    }

    public void setVen_diamor(int ven_diamor) {
        this.ven_diamor = ven_diamor;
    }

    public int getVen_gps() {
        return ven_gps;
    }

    public void setVen_gps(int ven_gps) {
        this.ven_gps = ven_gps;
    }

    public String getSup_des() {
        return sup_des;
    }

    public void setSup_des(String sup_des) {
        this.sup_des = sup_des;
    }

    public String getVen_nomrep() {
        return ven_nomrep;
    }

    public void setVen_nomrep(String ven_nomrep) {
        this.ven_nomrep = ven_nomrep;
    }

    public int getVen_ord() {
        return ven_ord;
    }

    public void setVen_ord(int ven_ord) {
        this.ven_ord = ven_ord;
    }

    public String getTab_subdir() {
        return tab_subdir;
    }

    public void setTab_subdir(String tab_subdir) {
        this.tab_subdir = tab_subdir;
    }

    public boolean isValor() {
        if (ven_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getVen_usuadd() {
        return ven_usuadd;
    }

    public void setVen_usuadd(String ven_usuadd) {
        this.ven_usuadd = ven_usuadd;
    }

    public Date getVen_fecadd() {
        return ven_fecadd;
    }

    public void setVen_fecadd(Date ven_fecadd) {
        this.ven_fecadd = ven_fecadd;
    }

    public String getVen_usumod() {
        return ven_usumod;
    }

    public void setVen_usumod(String ven_usumod) {
        this.ven_usumod = ven_usumod;
    }

    public Date getVen_fecmod() {
        return ven_fecmod;
    }

    public void setVen_fecmod(Date ven_fecmod) {
        this.ven_fecmod = ven_fecmod;
    }

    public String getS_vennomcompleto() {
        s_vennomcompleto = ven_ape + " " + ven_nom;
        return s_vennomcompleto;
    }

    public void setS_vennomcompleto(String s_vennomcompleto) {
        this.s_vennomcompleto = s_vennomcompleto;
    }

}
