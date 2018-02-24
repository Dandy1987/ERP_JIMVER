package org.me.gj.model.seguridad.mantenimiento;

public class Menus {

    private int men_id;
    private String men_des;
    private int men_idmod;
    private int men_idmen;
    private int men_idsubmen1;
    private int men_idsubmen2;
    private int men_tip;
    private int cod_menu;
    private String des_modulo;
    private String des_menu;
    private String des_submenu;
    private String des_nombre;

    public Menus() {
    }

    public Menus(int men_id, String men_des, int men_idmod, int men_idmen, int men_idsubmen1, int men_idsubmen2, int men_tip) {
        this.men_id = men_id;
        this.men_des = men_des;
        this.men_idmod = men_idmod;
        this.men_idmen = men_idmen;
        this.men_idsubmen1 = men_idsubmen1;
        this.men_idsubmen2 = men_idsubmen2;
        this.men_tip = men_tip;
    }

    public Menus(String men_des) {
        this.men_des = men_des;
    }

    public Menus(int men_idmod, String men_des) {
        this.men_idmod = men_idmod;
        this.men_des = men_des;
    }

    public Menus(int men_idmod, String men_des, int men_idmen) {
        this.men_des = men_des;
        this.men_idmod = men_idmod;
        this.men_idmen = men_idmen;
    }

    public Menus(int men_idmod, String men_des, int men_idmen, int men_idsubmen1) {
        this.men_des = men_des;
        this.men_idmod = men_idmod;
        this.men_idmen = men_idmen;
        this.men_idsubmen1 = men_idsubmen1;
    }

    public Menus(int cod_menu, String des_modulo, String des_menu, String des_submenu, String des_nombre) {
        this.cod_menu = cod_menu;
        this.des_modulo = des_modulo;
        this.des_menu = des_menu;
        this.des_submenu = des_submenu;
        this.des_nombre = des_nombre;
    }

    public String getMen_des() {
        return men_des;
    }

    public void setMen_des(String men_des) {
        this.men_des = men_des;
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

    public int getMen_tip() {
        return men_tip;
    }

    public void setMen_tip(int men_tip) {
        this.men_tip = men_tip;
    }

    public int getCod_menu() {
        return cod_menu;
    }

    public void setCod_menu(int cod_menu) {
        this.cod_menu = cod_menu;
    }

    public String getDes_menu() {
        return des_menu;
    }

    public void setDes_menu(String des_menu) {
        this.des_menu = des_menu;
    }

    public String getDes_modulo() {
        return des_modulo;
    }

    public void setDes_modulo(String des_modulo) {
        this.des_modulo = des_modulo;
    }

    public String getDes_nombre() {
        return des_nombre;
    }

    public void setDes_nombre(String des_nombre) {
        this.des_nombre = des_nombre;
    }

    public String getDes_submenu() {
        return des_submenu;
    }

    public void setDes_submenu(String des_submenu) {
        this.des_submenu = des_submenu;
    }

}
