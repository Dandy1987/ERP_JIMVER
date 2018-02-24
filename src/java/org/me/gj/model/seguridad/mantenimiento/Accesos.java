package org.me.gj.model.seguridad.mantenimiento;

import java.util.Date;

public class Accesos {

    private int usu_id;
    private int men_id;
    private int emp_id;
    private String acc_usuadd;
    private Date acc_fecadd;
    private int suc_id;
    private int men_idmod;
    private int men_idmen;
    private int men_idsubmen1;
    private int men_idsubmen2;
    private Date acc_fecmod;
    private String acc_usumod;
    private int acc_ins;
    private int acc_mod;
    private int acc_eli;
    private int acc_imp;
    private String acc_des;
    private String modulo;
    private String menu;
    private String submenu1;
    private String submenu2;

    public Accesos(int usu_id, int men_id, int emp_id, String acc_usuadd, Date acc_fecadd, int suc_id, int men_idmod, int men_idmen, int men_idsubmen1, int men_idsubmen2, Date acc_fecmod, String acc_usumod, int acc_ins, int acc_mod, int acc_eli, int acc_imp) {
        this.usu_id = usu_id;
        this.men_id = men_id;
        this.emp_id = emp_id;
        this.acc_usuadd = acc_usuadd;
        this.acc_fecadd = acc_fecadd;
        this.suc_id = suc_id;
        this.men_idmod = men_idmod;
        this.men_idmen = men_idmen;
        this.men_idsubmen1 = men_idsubmen1;
        this.men_idsubmen2 = men_idsubmen2;
        this.acc_fecmod = acc_fecmod;
        this.acc_usumod = acc_usumod;
        this.acc_ins = acc_ins;
        this.acc_mod = acc_mod;
        this.acc_eli = acc_eli;
        this.acc_imp = acc_imp;
    }

    public Accesos(int usu_id, int men_id, int emp_id, String acc_usuadd, int suc_id, int men_idmod, int men_idmen, int men_idsubmen1, int men_idsubmen2, int acc_ins, int acc_mod, int acc_eli, int acc_imp) {
        this.usu_id = usu_id;
        this.men_id = men_id;
        this.emp_id = emp_id;
        this.acc_usuadd = acc_usuadd;
        this.suc_id = suc_id;
        this.men_idmod = men_idmod;
        this.men_idmen = men_idmen;
        this.men_idsubmen1 = men_idsubmen1;
        this.men_idsubmen2 = men_idsubmen2;
        this.acc_ins = acc_ins;
        this.acc_mod = acc_mod;
        this.acc_eli = acc_eli;
        this.acc_imp = acc_imp;
    }

    public Accesos(String modulo, String menu, String submenu1, String submenu2) {
        this.modulo = modulo;
        this.menu = menu;
        this.submenu1 = submenu1;
        this.submenu2 = submenu2;
    }

    public Accesos(String acc_des) {
        this.acc_des = acc_des;
    }

    public Accesos(int usu_id, int men_id, int emp_id, int suc_id, int acc_ins, int acc_mod, int acc_eli, int acc_imp) {
        this.usu_id = usu_id;
        this.men_id = men_id;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.acc_ins = acc_ins;
        this.acc_mod = acc_mod;
        this.acc_eli = acc_eli;
        this.acc_imp = acc_imp;
    }

    public Accesos() {
    }

    public String getAcc_des() {
        return acc_des;
    }

    public void setAcc_des(String acc_des) {
        this.acc_des = acc_des;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getSubmenu1() {
        return submenu1;
    }

    public void setSubmenu1(String submenu1) {
        this.submenu1 = submenu1;
    }

    public String getSubmenu2() {
        return submenu2;
    }

    public void setSubmenu2(String submenu2) {
        this.submenu2 = submenu2;
    }

    public Date getAcc_fecadd() {
        return acc_fecadd;
    }

    public void setAcc_fecadd(Date acc_fecadd) {
        this.acc_fecadd = acc_fecadd;
    }

    public Date getAcc_fecmod() {
        return acc_fecmod;
    }

    public void setAcc_fecmod(Date acc_fecmod) {
        this.acc_fecmod = acc_fecmod;
    }

    public String getAcc_usuadd() {
        return acc_usuadd;
    }

    public void setAcc_usuadd(String acc_usuadd) {
        this.acc_usuadd = acc_usuadd;
    }

    public String getAcc_usumod() {
        return acc_usumod;
    }

    public void setAcc_usumod(String acc_usumod) {
        this.acc_usumod = acc_usumod;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getMen_id() {
        return men_id;
    }

    public void setMen_id(int men_id) {
        this.men_id = men_id;
    }

    public int getMen_idmen() {
        return men_idmen;
    }

    public void setMen_idmen(int men_idmen) {
        this.men_idmen = men_idmen;
    }

    public int getMen_idmod() {
        return men_idmod;
    }

    public void setMen_idmod(int men_idmod) {
        this.men_idmod = men_idmod;
    }

    public int getMen_idsubmen1() {
        return men_idsubmen1;
    }

    public void setMen_idsubmen1(int men_idsubmen1) {
        this.men_idsubmen1 = men_idsubmen1;
    }

    public int getMen_idsubmen2() {
        return men_idsubmen2;
    }

    public void setMen_idsubmen2(int men_idsubmen2) {
        this.men_idsubmen2 = men_idsubmen2;
    }

    public int getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(int suc_id) {
        this.suc_id = suc_id;
    }

    public int getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(int usu_id) {
        this.usu_id = usu_id;
    }

    public int getAcc_ins() {
        return acc_ins;
    }

    public void setAcc_ins(int acc_ins) {
        this.acc_ins = acc_ins;
    }

    public int getAcc_mod() {
        return acc_mod;
    }

    public void setAcc_mod(int acc_mod) {
        this.acc_mod = acc_mod;
    }

    public int getAcc_eli() {
        return acc_eli;
    }

    public void setAcc_eli(int acc_eli) {
        this.acc_eli = acc_eli;
    }

    public int getAcc_imp() {
        return acc_imp;
    }

    public void setAcc_imp(int acc_imp) {
        this.acc_imp = acc_imp;
    }

}
