package org.me.gj.model.seguridad.mantenimiento;

import java.util.Date;

public class Usuarios {

    private int usu_id;
    private String usu_nom;
    private String usu_ape;
    private String usu_nick;
    private String usu_pass;
    private int usu_est;
    private int usu_sit;
    private String usu_email;
    private String usu_dni;
    private String usu_rol;
    private int usu_idRol;
    private String usu_imag;
    private String usu_area;
    private String usu_preg;
    private String usu_rpta;
    private int usu_idPreg;
    private int usu_int;
    private boolean valor;
    private String usu_usuadd;
    private Date usu_fecadd;
    private String usu_usumod;
    private Date usu_fecmod;

    public Usuarios(int usu_id, String usu_nom, String usu_ape, String usu_nick, String usu_pass, int usu_est, int usu_sit,
            String usu_email, String usu_dni, int usu_idRol, String usu_imag, String usu_area, int usu_idPreg,
            String usu_rpta, String usu_usuadd, String usu_usumod) {
        this.usu_id = usu_id;
        this.usu_nom = usu_nom;
        this.usu_ape = usu_ape;
        this.usu_nick = usu_nick;
        this.usu_pass = usu_pass;
        this.usu_est = usu_est;
        this.usu_sit = usu_sit;
        this.usu_email = usu_email;
        this.usu_dni = usu_dni;
        this.usu_idRol = usu_idRol;
        this.usu_imag = usu_imag;
        this.usu_area = usu_area;
        this.usu_idPreg = usu_idPreg;
        this.usu_rpta = usu_rpta;
        this.usu_usuadd = usu_usuadd;
        this.usu_usumod = usu_usumod;
    }

    public Usuarios() {
    }

    public int getUsu_idPreg() {
        return usu_idPreg;
    }

    public void setUsu_idPreg(int usu_idPreg) {
        this.usu_idPreg = usu_idPreg;
    }

    public int getUsu_idRol() {
        return usu_idRol;
    }

    public void setUsu_idRol(int usu_idRol) {
        this.usu_idRol = usu_idRol;
    }

    public String getUsu_ape() {
        return usu_ape;
    }

    public void setUsu_ape(String usu_ape) {
        this.usu_ape = usu_ape;
    }

    public String getUsu_area() {
        return usu_area;
    }

    public void setUsu_area(String usu_area) {
        this.usu_area = usu_area;
    }

    public String getUsu_dni() {
        return usu_dni;
    }

    public void setUsu_dni(String usu_dni) {
        this.usu_dni = usu_dni;
    }

    public String getUsu_email() {
        return usu_email;
    }

    public void setUsu_email(String usu_email) {
        this.usu_email = usu_email;
    }

    public int getUsu_est() {
        return usu_est;
    }

    public void setUsu_est(int usu_est) {
        this.usu_est = usu_est;
    }

    public int getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(int usu_id) {
        this.usu_id = usu_id;
    }

    public String getUsu_imag() {
        return usu_imag;
    }

    public void setUsu_imag(String usu_imag) {
        this.usu_imag = usu_imag;
    }

    public int getUsu_int() {
        return usu_int;
    }

    public void setUsu_int(int usu_int) {
        this.usu_int = usu_int;
    }

    public String getUsu_nick() {
        return usu_nick;
    }

    public void setUsu_nick(String usu_nick) {
        this.usu_nick = usu_nick;
    }

    public String getUsu_nom() {
        return usu_nom;
    }

    public void setUsu_nom(String usu_nom) {
        this.usu_nom = usu_nom;
    }

    public String getUsu_pass() {
        return usu_pass;
    }

    public void setUsu_pass(String usu_pass) {
        this.usu_pass = usu_pass;
    }

    public String getUsu_preg() {
        return usu_preg;
    }

    public void setUsu_preg(String usu_preg) {
        this.usu_preg = usu_preg;
    }

    public String getUsu_rol() {
        return usu_rol;
    }

    public void setUsu_rol(String usu_rol) {
        this.usu_rol = usu_rol;
    }

    public String getUsu_rpta() {
        return usu_rpta;
    }

    public void setUsu_rpta(String usu_rpta) {
        this.usu_rpta = usu_rpta;
    }

    public int getUsu_sit() {
        return usu_sit;
    }

    public void setUsu_sit(int usu_sit) {
        this.usu_sit = usu_sit;
    }

    public boolean isValor() {
        if (usu_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public Date getUsu_fecadd() {
        return usu_fecadd;
    }

    public void setUsu_fecadd(Date usu_fecadd) {
        this.usu_fecadd = usu_fecadd;
    }

    public Date getUsu_fecmod() {
        return usu_fecmod;
    }

    public void setUsu_fecmod(Date usu_fecmod) {
        this.usu_fecmod = usu_fecmod;
    }

    public String getUsu_usuadd() {
        return usu_usuadd;
    }

    public void setUsu_usuadd(String usu_usuadd) {
        this.usu_usuadd = usu_usuadd;
    }

    public String getUsu_usumod() {
        return usu_usumod;
    }

    public void setUsu_usumod(String usu_usumod) {
        this.usu_usumod = usu_usumod;
    }
}
