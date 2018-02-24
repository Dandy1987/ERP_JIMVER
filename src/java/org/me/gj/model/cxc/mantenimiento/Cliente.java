package org.me.gj.model.cxc.mantenimiento;

import java.util.Date;

public class Cliente {

    private String cli_id;
    private long cli_key;
    private String cli_apepat;
    private String cli_apemat;
    private String cli_nombre;
    private String cli_razsoc;
    private Date cli_fecnac;
    private long cli_ruc;
    private String cli_dirweb;
    private String cli_email1;
    private String cli_email2;
    private int cli_est;
    private String cli_dni;
    private int cli_tipodoc;
    private int cli_perju;
    //private long cli_credcor;
    //private int cli_doccor;
    private double cli_limcredcorp;
    private int cli_limdoccorp;
    
    private double cli_dscto;
    private int cli_factura;
    private int cli_mon;
    private int cli_lista;
    private int cli_con;
    private int cli_canal;
    private int forpag_key;
    private String forpag_id;
    private int cli_emprel;
    private int cli_perc;
    private String cli_usuadd;
    private Date cli_fecadd;
    private String cli_usumod;
    private Date cli_fecmod;
    private String cli_giro;
    private int dia_vis;
    private String cli_deslista;
    private String cli_descond;
    private int diasplazo;
    private String zon_dvis_des;
    private boolean valor;
    private String clidir_direc;
    private int zon_key;
    private String zon_id;
    private String zon_des;
    private String hor_id;
    private String hor_des;
    private boolean val_rel;
    private boolean val_perc;
    private long clidir_id;
    private String ven_id;
    private String ven_apenom;
    private String sup_id;
    private String sup_apenom;
    private String trans_id;
    private String trans_alias;
    private double cli_limcredemp;
    private int cli_limdocemp;
    private String clitel_telef1;
    private String dia_vis_des;
    private String cli_tipodoc_des;
    //private double cli_limcredcorp;
    //private int cli_limdoccorp;
    private String cli_cate_id;
    private String cli_cate_des;
    private String cli_dirref;

    public Cliente() {
    }

    public Cliente(String cli_id, long cli_key, String cli_apepat, String cli_apemat, String cli_nombre, String cli_razsoc, Date cli_fecnac,
            long cli_ruc, String cli_dirweb, String cli_email1, String cli_email2, int cli_est, String cli_dni, int cli_tipodoc,
            int cli_perju, double cli_limcredcorp, int cli_limdoccorp, double cli_dscto, int cli_mon, int cli_con,
            int cli_canal, int forpag_key, String forpag_id, int cli_emprel, int cli_perc, String cli_usuadd, String cli_usumod, double cli_limcredemp, int cli_limdocemp) {
        this.cli_id = cli_id;
        this.cli_key = cli_key;
        this.cli_apepat = cli_apepat;
        this.cli_apemat = cli_apemat;
        this.cli_nombre = cli_nombre;
        this.cli_razsoc = cli_razsoc;
        this.cli_fecnac = cli_fecnac;
        this.cli_ruc = cli_ruc;
        this.cli_dirweb = cli_dirweb;
        this.cli_email1 = cli_email1;
        this.cli_email2 = cli_email2;
        this.cli_est = cli_est;
        this.cli_dni = cli_dni;
        this.cli_tipodoc = cli_tipodoc;
        this.cli_perju = cli_perju;
        this.cli_limcredcorp = cli_limcredcorp;
        this.cli_limdoccorp = cli_limdoccorp;
        this.cli_dscto = cli_dscto;
        this.cli_mon = cli_mon;
        this.cli_con = cli_con;
        this.cli_canal = cli_canal;
        this.forpag_key = forpag_key;
        this.forpag_id = forpag_id;
        this.cli_emprel = cli_emprel;
        this.cli_perc = cli_perc;
        this.cli_usuadd = cli_usuadd;
        this.cli_usumod = cli_usumod;
        this.cli_limcredemp = cli_limcredemp;
        this.cli_limdocemp = cli_limdocemp;
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

    public String getCli_apepat() {
        return cli_apepat;
    }

    public void setCli_apepat(String cli_apepat) {
        this.cli_apepat = cli_apepat;
    }

    public String getCli_apemat() {
        return cli_apemat;
    }

    public void setCli_apemat(String cli_apemat) {
        this.cli_apemat = cli_apemat;
    }

    public String getCli_nombre() {
        return cli_nombre;
    }

    public void setCli_nombre(String cli_nombre) {
        this.cli_nombre = cli_nombre;
    }

    public String getCli_razsoc() {
        return cli_razsoc;
    }

    public void setCli_razsoc(String cli_razsoc) {
        this.cli_razsoc = cli_razsoc;
    }

    public Date getCli_fecnac() {
        return cli_fecnac;
    }

    public void setCli_fecnac(Date cli_fecnac) {
        this.cli_fecnac = cli_fecnac;
    }

    public long getCli_ruc() {
        return cli_ruc;
    }

    public void setCli_ruc(long cli_ruc) {
        this.cli_ruc = cli_ruc;
    }

    public String getCli_dirweb() {
        return cli_dirweb;
    }

    public void setCli_dirweb(String cli_dirweb) {
        this.cli_dirweb = cli_dirweb;
    }

    public String getCli_email1() {
        return cli_email1;
    }

    public void setCli_email1(String cli_email1) {
        this.cli_email1 = cli_email1;
    }

    public String getCli_email2() {
        return cli_email2;
    }

    public void setCli_email2(String cli_email2) {
        this.cli_email2 = cli_email2;
    }

    public int getCli_est() {
        return cli_est;
    }

    public void setCli_est(int cli_est) {
        this.cli_est = cli_est;
    }

    public String getCli_dni() {
        return cli_dni;
    }

    public void setCli_dni(String cli_dni) {
        this.cli_dni = cli_dni;
    }

    public int getCli_tipodoc() {
        return cli_tipodoc;
    }

    public void setCli_tipodoc(int cli_tipodoc) {
        this.cli_tipodoc = cli_tipodoc;
    }

    public int getCli_perju() {
        return cli_perju;
    }

    public void setCli_perju(int cli_perju) {
        this.cli_perju = cli_perju;
    }

    /*public long getCli_credcor() {
        return cli_credcor;
    }

    public void setCli_credcor(long cli_credcor) {
        this.cli_credcor = cli_credcor;
    }

    public int getCli_doccor() {
        return cli_doccor;
    }

    public void setCli_doccor(int cli_doccor) {
        this.cli_doccor = cli_doccor;
    }*/

    public double getCli_dscto() {
        return cli_dscto;
    }

    public void setCli_dscto(double cli_dscto) {
        this.cli_dscto = cli_dscto;
    }

    public int getCli_factura() {
        return cli_factura;
    }

    public void setCli_factura(int cli_factura) {
        this.cli_factura = cli_factura;
    }

    public int getCli_mon() {
        return cli_mon;
    }

    public void setCli_mon(int cli_mon) {
        this.cli_mon = cli_mon;
    }

    public int getCli_lista() {
        return cli_lista;
    }

    public void setCli_lista(int cli_lista) {
        this.cli_lista = cli_lista;
    }

    public int getCli_con() {
        return cli_con;
    }

    public void setCli_con(int cli_con) {
        this.cli_con = cli_con;
    }

    public int getCli_canal() {
        return cli_canal;
    }

    public void setCli_canal(int cli_canal) {
        this.cli_canal = cli_canal;
    }

    public int getForpag_key() {
        return forpag_key;
    }

    public void setForpag_key(int forpag_key) {
        this.forpag_key = forpag_key;
    }

    public String getForpag_id() {
        return forpag_id;
    }

    public void setForpag_id(String forpag_id) {
        this.forpag_id = forpag_id;
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

    public boolean isValor() {
        if (cli_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getClidir_direc() {
        return clidir_direc;
    }

    public void setClidir_direc(String clidir_direc) {
        this.clidir_direc = clidir_direc;
    }

    public int getZon_key() {
        return zon_key;
    }

    public void setZon_key(int zon_key) {
        this.zon_key = zon_key;
    }

    public String getZon_id() {
        return zon_id;
    }

    public void setZon_id(String zon_id) {
        this.zon_id = zon_id;
    }

    public String getZon_des() {
        return zon_des;
    }

    public void setZon_des(String zon_des) {
        this.zon_des = zon_des;
    }

    public String getHor_id() {
        return hor_id;
    }

    public void setHor_id(String hor_id) {
        this.hor_id = hor_id;
    }

    public String getHor_des() {
        return hor_des;
    }

    public void setHor_des(String hor_des) {
        this.hor_des = hor_des;
    }

    public long getClidir_id() {
        return clidir_id;
    }

    public void setClidir_id(long clidir_id) {
        this.clidir_id = clidir_id;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
    }

    public String getVen_apenom() {
        return ven_apenom;
    }

    public void setVen_apenom(String ven_apenom) {
        this.ven_apenom = ven_apenom;
    }

    public String getSup_id() {
        return sup_id;
    }

    public void setSup_id(String sup_id) {
        this.sup_id = sup_id;
    }

    public String getSup_apenom() {
        return sup_apenom;
    }

    public void setSup_apenom(String sup_apenom) {
        this.sup_apenom = sup_apenom;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getTrans_alias() {
        return trans_alias;
    }

    public void setTrans_alias(String trans_alias) {
        this.trans_alias = trans_alias;
    }

    public double getCli_limcredemp() {
        return cli_limcredemp;
    }

    public void setCli_limcredemp(double cli_limcredemp) {
        this.cli_limcredemp = cli_limcredemp;
    }

    public int getCli_limdocemp() {
        return cli_limdocemp;
    }

    public void setCli_limdocemp(int cli_limdocemp) {
        this.cli_limdocemp = cli_limdocemp;
    }

    public String getCli_giro() {
        return cli_giro;
    }

    public void setCli_giro(String cli_giro) {
        this.cli_giro = cli_giro;
    }

    public int getDia_vis() {
        return dia_vis;
    }

    public void setDia_vis(int dia_vis) {
        this.dia_vis = dia_vis;
    }

    public String getCli_deslista() {
        return cli_deslista;
    }

    public void setCli_deslista(String cli_deslista) {
        this.cli_deslista = cli_deslista;
    }

    public String getCli_descond() {
        return cli_descond;
    }

    public void setCli_descond(String cli_descond) {
        this.cli_descond = cli_descond;
    }

    public int getDiasplazo() {
        return diasplazo;
    }

    public void setDiasplazo(int diasplazo) {
        this.diasplazo = diasplazo;
    }

    public String getZon_dvis_des() {
        return zon_dvis_des;
    }

    public void setZon_dvis_des(String zon_dvis_des) {
        this.zon_dvis_des = zon_dvis_des;
    }

    public boolean isVal_rel() {
        if (cli_emprel == 1) {
            val_rel = true;
        } else {
            val_rel = false;
        }
        return val_rel;
    }

    public void setVal_rel(boolean val_rel) {
        this.val_rel = val_rel;
    }

    public int getCli_emprel() {
        return cli_emprel;
    }

    public void setCli_emprel(int cli_emprel) {
        this.cli_emprel = cli_emprel;
    }

    public int getCli_perc() {
        return cli_perc;
    }

    public void setCli_perc(int cli_perc) {
        this.cli_perc = cli_perc;
    }

    public boolean isVal_perc() {
        if (cli_perc == 1) {
            val_perc = true;
        } else {
            val_perc = false;
        }
        return val_perc;
    }

    public void setVal_perc(boolean val_perc) {
        this.val_perc = val_perc;
    }

    public String getDia_vis_des() {
        return dia_vis_des;
    }

    public void setDia_vis_des(String dia_vis_des) {
        this.dia_vis_des = dia_vis_des;
    }

    public String getClitel_telef1() {
        return clitel_telef1;
    }

    public void setClitel_telef1(String clitel_telef1) {
        this.clitel_telef1 = clitel_telef1;
    }

    public void setCli_tipodoc_des(String cli_tipodoc_des) {
        this.cli_tipodoc_des = cli_tipodoc_des;
    }

    public String getCli_tipodoc_des() {
        return cli_tipodoc_des;
    }

    public double getCli_limcredcorp() {
        return cli_limcredcorp;
    }

    public void setCli_limcredcorp(double cli_limcredcorp) {
        this.cli_limcredcorp = cli_limcredcorp;
    }

    public int getCli_limdoccorp() {
        return cli_limdoccorp;
    }

    public void setCli_limdoccorp(int cli_limdoccorp) {
        this.cli_limdoccorp = cli_limdoccorp;
    }

    public String getCli_cate_id() {
        return cli_cate_id;
    }

    public void setCli_cate_id(String cli_cate_id) {
        this.cli_cate_id = cli_cate_id;
    }

    public String getCli_cate_des() {
        return cli_cate_des;
    }

    public void setCli_cate_des(String cli_cate_des) {
        this.cli_cate_des = cli_cate_des;
    }

    public String getCli_dirref() {
        return cli_dirref;
    }

    public void setCli_dirref(String cli_dirref) {
        this.cli_dirref = cli_dirref;
    }
}
