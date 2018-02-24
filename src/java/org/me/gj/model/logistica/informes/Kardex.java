package org.me.gj.model.logistica.informes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kardex {

    private int emp_id;
    private int suc_id;
    private long kar_key;
    private Date gui_fecha;
    private String gui_sfecha;
    private String gui_hora;
    private int kar_item;
    private String prov_id;
    private String prov_razsoc;
    private String pro_id;
    private String pro_des;
    private double pro_undpre;
    private String gui_tip;
    private String gui_des;
    private String gui_nro;
    private String tip_mov;
    private String tip_doc;
    private String nro_doc;
    private double kar_ent;
    private double kar_sal;
    private double kar_cant;
    private double stk_ini;
    private double stk_act;
    private String gui_usuadd;
    private Date gui_fecadd;
    private String gui_usumod;
    private Date gui_fecmod;
    private int movent;
    private int movfra;
    private int stkent;
    private int stkfra;
    
    private String kar_cliid;
    private String kar_clides;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Kardex() {
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

    public long getKar_key() {
        return kar_key;
    }

    public void setKar_key(long kar_key) {
        this.kar_key = kar_key;
    }

    public Date getGui_fecha() {
        return gui_fecha;
    }

    public void setGui_fecha(Date gui_fecha) {
        this.gui_fecha = gui_fecha;
    }

    public String getGui_sfecha() {
        gui_sfecha = sdf.format(gui_fecha);
        return gui_sfecha;
    }

    public String getGui_hora() {
        return gui_hora;
    }

    public void setGui_hora(String gui_hora) {
        this.gui_hora = gui_hora;
    }

    public int getKar_item() {
        return kar_item;
    }

    public void setKar_item(int kar_item) {
        this.kar_item = kar_item;
    }

    public String getProv_id() {
        return prov_id;
    }

    public void setProv_id(String prov_id) {
        this.prov_id = prov_id;
    }

    public String getProv_razsoc() {
        return prov_razsoc;
    }

    public void setProv_razsoc(String prov_razsoc) {
        this.prov_razsoc = prov_razsoc;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public double getPro_undpre() {
        return pro_undpre;
    }

    public void setPro_undpre(double pro_undpre) {
        this.pro_undpre = pro_undpre;
    }

    public String getGui_tip() {
        return gui_tip;
    }

    public void setGui_tip(String gui_tip) {
        this.gui_tip = gui_tip;
    }

    public String getGui_des() {
        return gui_des;
    }

    public void setGui_des(String gui_des) {
        this.gui_des = gui_des;
    }

    public String getGui_nro() {
        return gui_nro;
    }

    public void setGui_nro(String gui_nro) {
        this.gui_nro = gui_nro;
    }

    public String getTip_mov() {
        return tip_mov;
    }

    public void setTip_mov(String tip_mov) {
        this.tip_mov = tip_mov;
    }

    public String getTip_doc() {
        return tip_doc;
    }

    public void setTip_doc(String tip_doc) {
        this.tip_doc = tip_doc;
    }

    public String getNro_doc() {
        return nro_doc;
    }

    public void setNro_doc(String nro_doc) {
        this.nro_doc = nro_doc;
    }

    public double getKar_ent() {
        return kar_ent;
    }

    public void setKar_ent(double kar_ent) {
        this.kar_ent = kar_ent;
    }

    public double getKar_sal() {
        return kar_sal;
    }

    public void setKar_sal(double kar_sal) {
        this.kar_sal = kar_sal;
    }

    public double getKar_cant() {
        return kar_cant;
    }

    public void setKar_cant(double kar_cant) {
        this.kar_cant = kar_cant;
    }

    public double getStk_ini() {
        return stk_ini;
    }

    public void setStk_ini(double stk_ini) {
        this.stk_ini = stk_ini;
    }

    public double getStk_act() {
        return stk_act;
    }

    public void setStk_act(double stk_act) {
        this.stk_act = stk_act;
    }

    public String getGui_usuadd() {
        return gui_usuadd;
    }

    public void setGui_usuadd(String gui_usuadd) {
        this.gui_usuadd = gui_usuadd;
    }

    public Date getGui_fecadd() {
        return gui_fecadd;
    }

    public void setGui_fecadd(Date gui_fecadd) {
        this.gui_fecadd = gui_fecadd;
    }

    public String getGui_usumod() {
        return gui_usumod;
    }

    public void setGui_usumod(String gui_usumod) {
        this.gui_usumod = gui_usumod;
    }

    public Date getGui_fecmod() {
        return gui_fecmod;
    }

    public void setGui_fecmod(Date gui_fecmod) {
        this.gui_fecmod = gui_fecmod;
    }

    public int getMovent() {
        return movent;
    }

    public void setMovent(int movent) {
        this.movent = movent;
    }

    public int getMovfra() {
        return movfra;
    }

    public void setMovfra(int movfra) {
        this.movfra = movfra;
    }

    public int getStkent() {
        return stkent;
    }

    public void setStkent(int stkent) {
        this.stkent = stkent;
    }

    public int getStkfra() {
        return stkfra;
    }

    public void setStkfra(int stkfra) {
        this.stkfra = stkfra;
    }
    
    public String getKar_cliid() {
        return kar_cliid;
    }

    public void setKar_cliid(String kar_cliid) {
        this.kar_cliid = kar_cliid;
    }

    public String getKar_clides() {
        return kar_clides;
    }

    public void setKar_clides(String kar_clides) {
        this.kar_clides = kar_clides;
    }
}
