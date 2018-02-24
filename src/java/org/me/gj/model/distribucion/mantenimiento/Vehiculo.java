package org.me.gj.model.distribucion.mantenimiento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vehiculo {

    private String trans_id;
    private int trans_key;
    private int trans_sucid;
    private int trans_empid;
    private String trans_alias;
    private String trans_placa;
    private int trans_marca;
    private int trans_combustible;
    private int trans_categoria;
    private int trans_modelo;
    private int trans_color;
    private int trans_transmision;
    private int trans_carroceria;
    private String trans_vin;
    private String trans_chasis;
    private String trans_motor;
    private int trans_aniofab;
    private int trans_aniomodelo;
    private String trans_version;
    private int trans_ejes;
    private int trans_asientos;
    private int trans_pasajeros;
    private int trans_ruedas;
    private String trans_potencia;
    private int trans_cilindros;
    private double trans_cilindrada;
    private String trans_traccion;
    private double trans_pesobruto;
    private double trans_pesoneto;
    private double trans_cargautil;
    private double trans_largo;
    private double trans_altura;
    private double trans_ancho;
    private String trans_img;
    private Date trans_fecing;
    private Date trans_fecsal;
    private int trans_ord;
    private String trans_nomrep;
    private int trans_estado;
    private String trans_usuadd;
    private Date trans_fecadd;
    private String trans_usumod;
    private Date trans_fecmod;
    private String desmarca;
    private String descombustible;
    private String descolor;
    private String strans_fecing;
    private boolean valor = false;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /*public Vehiculo(int trans_key, int trans_sucid, int trans_empid, String trans_alias, String trans_placa, int trans_marca,
     int trans_combustible, int trans_categoria, int trans_modelo, int trans_color, int trans_transmision,
     int trans_carroceria, String trans_vin, String trans_chasis, String trans_motor, int trans_aniofab,
     int trans_aniomodelo, String trans_version, int trans_ejes, int trans_asientos, int trans_pasajeros,
     int trans_ruedas, String trans_potencia, int trans_cilindros, double trans_cilindrada, String trans_traccion,
     double trans_pesobruto, double trans_pesoneto, double trans_cargautil, double trans_largo, double trans_altura,
     double trans_ancho, String trans_img, Date trans_fecing, Date trans_fecsal, int trans_ord, String trans_nomrep,
     int trans_estado, boolean valor, String trans_usuadd, String trans_usumod) {

     this.trans_key = trans_key;
     this.trans_sucid = trans_sucid;
     this.trans_empid = trans_empid;
     this.trans_alias = trans_alias;
     this.trans_placa = trans_placa;
     this.trans_marca = trans_marca;
     this.trans_combustible = trans_combustible;
     this.trans_categoria = trans_categoria;
     this.trans_modelo = trans_modelo;
     this.trans_color = trans_color;
     this.trans_transmision = trans_transmision;
     this.trans_carroceria = trans_carroceria;
     this.trans_vin = trans_vin;
     this.trans_chasis = trans_chasis;
     this.trans_motor = trans_motor;
     this.trans_aniofab = trans_aniofab;
     this.trans_aniomodelo = trans_aniomodelo;
     this.trans_version = trans_version;
     this.trans_ejes = trans_ejes;
     this.trans_asientos = trans_asientos;
     this.trans_pasajeros = trans_pasajeros;
     this.trans_ruedas = trans_ruedas;
     this.trans_potencia = trans_potencia;
     this.trans_cilindros = trans_cilindros;
     this.trans_cilindrada = trans_cilindrada;
     this.trans_traccion = trans_traccion;
     this.trans_pesobruto = trans_pesobruto;
     this.trans_pesoneto = trans_pesoneto;
     this.trans_cargautil = trans_cargautil;
     this.trans_largo = trans_largo;
     this.trans_altura = trans_altura;
     this.trans_ancho = trans_ancho;
     this.trans_img = trans_img;
     this.trans_fecing = trans_fecing;
     this.trans_fecsal = trans_fecsal;
     this.trans_ord = trans_ord;
     this.trans_nomrep = trans_nomrep;
     this.trans_estado = trans_estado;
     this.trans_usuadd = trans_usuadd;
     this.trans_usumod = trans_usumod;
     this.valor = valor;
     }*/
    public Vehiculo(int trans_key, int trans_empid, int trans_sucid, int trans_estado, String trans_alias, String trans_placa, int trans_marca, int trans_combustible,
            int trans_categoria, int trans_modelo, int trans_color, int trans_transmision, int trans_carroceria, String trans_vin,
            String trans_chasis, String trans_motor, int trans_aniofab, int trans_aniomodelo, String trans_version, int trans_ejes,
            int trans_asientos, int trans_pasajeros, int trans_ruedas, String trans_potencia, int trans_cilindros, double trans_cilindrada,
            String trans_traccion, double trans_pesobruto, double trans_pesoneto, double trans_cargautil, double trans_largo,
            double trans_altura, double trans_ancho, String trans_img, Date trans_fecing, Date trans_fecsal, int trans_ord,
            String trans_nomrep, String trans_usuadd, String trans_usumod) {

        this.trans_key = trans_key;
        this.trans_empid = trans_empid;
        this.trans_sucid = trans_sucid;
        this.trans_estado = trans_estado;
        this.trans_alias = trans_alias;
        this.trans_placa = trans_placa;
        this.trans_marca = trans_marca;
        this.trans_combustible = trans_combustible;
        this.trans_categoria = trans_categoria;
        this.trans_modelo = trans_modelo;
        this.trans_color = trans_color;
        this.trans_transmision = trans_transmision;
        this.trans_carroceria = trans_carroceria;
        this.trans_vin = trans_vin;
        this.trans_chasis = trans_chasis;
        this.trans_motor = trans_motor;
        this.trans_aniofab = trans_aniofab;
        this.trans_aniomodelo = trans_aniomodelo;
        this.trans_version = trans_version;
        this.trans_ejes = trans_ejes;
        this.trans_asientos = trans_asientos;
        this.trans_pasajeros = trans_pasajeros;
        this.trans_ruedas = trans_ruedas;
        this.trans_potencia = trans_potencia;
        this.trans_cilindros = trans_cilindros;
        this.trans_cilindrada = trans_cilindrada;
        this.trans_traccion = trans_traccion;
        this.trans_pesobruto = trans_pesobruto;
        this.trans_pesoneto = trans_pesoneto;
        this.trans_cargautil = trans_cargautil;
        this.trans_largo = trans_largo;
        this.trans_altura = trans_altura;
        this.trans_ancho = trans_ancho;
        this.trans_img = trans_img;
        this.trans_fecing = trans_fecing;
        this.trans_fecsal = trans_fecsal;
        this.trans_ord = trans_ord;
        this.trans_nomrep = trans_nomrep;
        this.trans_usuadd = trans_usuadd;
        this.trans_usumod = trans_usumod;
    }

    public Vehiculo(int trans_key, String trans_alias, String trans_img, Date trans_fecsal, int trans_ord, String trans_nomrep,
            int trans_estado, int trans_sucid, int trans_empid, String trans_usuadd, String trans_usumod) {
        this.trans_sucid = trans_sucid;
        this.trans_empid = trans_empid;
        this.trans_key = trans_key;
        this.trans_alias = trans_alias;
        this.trans_img = trans_img;
        this.trans_fecsal = trans_fecsal;
        this.trans_ord = trans_ord;
        this.trans_nomrep = trans_nomrep;
        this.trans_estado = trans_estado;
        this.trans_usuadd = trans_usuadd;
        this.trans_usumod = trans_usumod;
    }

    public Vehiculo() {
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public int getTrans_key() {
        return trans_key;
    }

    public void setTrans_key(int trans_key) {
        this.trans_key = trans_key;
    }

    public int getTrans_sucid() {
        return trans_sucid;
    }

    public void setTrans_sucid(int trans_sucid) {
        this.trans_sucid = trans_sucid;
    }

    public int getTrans_empid() {
        return trans_empid;
    }

    public void setTrans_empid(int trans_empid) {
        this.trans_empid = trans_empid;
    }

    public String getTrans_alias() {
        return trans_alias;
    }

    public void setTrans_alias(String trans_alias) {
        this.trans_alias = trans_alias;
    }

    public String getTrans_placa() {
        return trans_placa;
    }

    public void setTrans_placa(String trans_placa) {
        this.trans_placa = trans_placa;
    }

    public int getTrans_marca() {
        return trans_marca;
    }

    public void setTrans_marca(int trans_marca) {
        this.trans_marca = trans_marca;
    }

    public int getTrans_combustible() {
        return trans_combustible;
    }

    public void setTrans_combustible(int trans_combustible) {
        this.trans_combustible = trans_combustible;
    }

    public int getTrans_categoria() {
        return trans_categoria;
    }

    public void setTrans_categoria(int trans_categoria) {
        this.trans_categoria = trans_categoria;
    }

    public int getTrans_modelo() {
        return trans_modelo;
    }

    public void setTrans_modelo(int trans_modelo) {
        this.trans_modelo = trans_modelo;
    }

    public int getTrans_color() {
        return trans_color;
    }

    public void setTrans_color(int trans_color) {
        this.trans_color = trans_color;
    }

    public int getTrans_transmision() {
        return trans_transmision;
    }

    public void setTrans_transmision(int trans_transmision) {
        this.trans_transmision = trans_transmision;
    }

    public int getTrans_carroceria() {
        return trans_carroceria;
    }

    public void setTrans_carroceria(int trans_carroceria) {
        this.trans_carroceria = trans_carroceria;
    }

    public String getTrans_vin() {
        return trans_vin;
    }

    public void setTrans_vin(String trans_vin) {
        this.trans_vin = trans_vin;
    }

    public String getTrans_chasis() {
        return trans_chasis;
    }

    public void setTrans_chasis(String trans_chasis) {
        this.trans_chasis = trans_chasis;
    }

    public String getTrans_motor() {
        return trans_motor;
    }

    public void setTrans_motor(String trans_motor) {
        this.trans_motor = trans_motor;
    }

    public int getTrans_aniofab() {
        return trans_aniofab;
    }

    public void setTrans_aniofab(int trans_aniofab) {
        this.trans_aniofab = trans_aniofab;
    }

    public int getTrans_aniomodelo() {
        return trans_aniomodelo;
    }

    public void setTrans_aniomodelo(int trans_aniomodelo) {
        this.trans_aniomodelo = trans_aniomodelo;
    }

    public String getTrans_version() {
        return trans_version;
    }

    public void setTrans_version(String trans_version) {
        this.trans_version = trans_version;
    }

    public int getTrans_ejes() {
        return trans_ejes;
    }

    public void setTrans_ejes(int trans_ejes) {
        this.trans_ejes = trans_ejes;
    }

    public int getTrans_asientos() {
        return trans_asientos;
    }

    public void setTrans_asientos(int trans_asientos) {
        this.trans_asientos = trans_asientos;
    }

    public int getTrans_pasajeros() {
        return trans_pasajeros;
    }

    public void setTrans_pasajeros(int trans_pasajeros) {
        this.trans_pasajeros = trans_pasajeros;
    }

    public int getTrans_ruedas() {
        return trans_ruedas;
    }

    public void setTrans_ruedas(int trans_ruedas) {
        this.trans_ruedas = trans_ruedas;
    }

    public String getTrans_potencia() {
        return trans_potencia;
    }

    public void setTrans_potencia(String trans_potencia) {
        this.trans_potencia = trans_potencia;
    }

    public int getTrans_cilindros() {
        return trans_cilindros;
    }

    public void setTrans_cilindros(int trans_cilindros) {
        this.trans_cilindros = trans_cilindros;
    }

    public double getTrans_cilindrada() {
        return trans_cilindrada;
    }

    public void setTrans_cilindrada(double trans_cilindrada) {
        this.trans_cilindrada = trans_cilindrada;
    }

    public String getTrans_traccion() {
        return trans_traccion;
    }

    public void setTrans_traccion(String trans_traccion) {
        this.trans_traccion = trans_traccion;
    }

    public double getTrans_pesobruto() {
        return trans_pesobruto;
    }

    public void setTrans_pesobruto(double trans_pesobruto) {
        this.trans_pesobruto = trans_pesobruto;
    }

    public double getTrans_pesoneto() {
        return trans_pesoneto;
    }

    public void setTrans_pesoneto(double trans_pesoneto) {
        this.trans_pesoneto = trans_pesoneto;
    }

    public double getTrans_cargautil() {
        return trans_cargautil;
    }

    public void setTrans_cargautil(double trans_cargautil) {
        this.trans_cargautil = trans_cargautil;
    }

    public double getTrans_largo() {
        return trans_largo;
    }

    public void setTrans_largo(double trans_largo) {
        this.trans_largo = trans_largo;
    }

    public double getTrans_altura() {
        return trans_altura;
    }

    public void setTrans_altura(double trans_altura) {
        this.trans_altura = trans_altura;
    }

    public double getTrans_ancho() {
        return trans_ancho;
    }

    public void setTrans_ancho(double trans_ancho) {
        this.trans_ancho = trans_ancho;
    }

    public String getTrans_img() {
        return trans_img;
    }

    public void setTrans_img(String trans_img) {
        this.trans_img = trans_img;
    }

    public Date getTrans_fecing() throws ParseException {
        Date fecha_date;
        String fecha_cadena;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(trans_fecing);
        fecha_date = sdf.parse(fecha_cadena);
        return fecha_date;
    }

    public void setTrans_fecing(Date trans_fecing) {
        this.trans_fecing = trans_fecing;
    }

    public Date getTrans_fecsal() throws ParseException {
        Date fecha_date;
        String fecha_cadena;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(trans_fecsal);
        fecha_date = sdf.parse(fecha_cadena);
        return fecha_date;
    }

    public void setTrans_fecsal(Date trans_fecsal) {
        this.trans_fecsal = trans_fecsal;
    }

    public int getTrans_ord() {
        return trans_ord;
    }

    public void setTrans_ord(int trans_ord) {
        this.trans_ord = trans_ord;
    }

    public String getTrans_nomrep() {
        return trans_nomrep;
    }

    public void setTrans_nomrep(String trans_nomrep) {
        this.trans_nomrep = trans_nomrep;
    }

    public int getTrans_estado() {
        return trans_estado;
    }

    public void setTrans_estado(int trans_estado) {
        this.trans_estado = trans_estado;
    }

    public boolean isValor() {
        if (trans_estado == 1) {
            valor = true;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getTrans_usuadd() {
        return trans_usuadd;
    }

    public Date getTrans_fecadd() {
        return trans_fecadd;
    }

    public String getTrans_usumod() {
        return trans_usumod;
    }

    public Date getTrans_fecmod() {
        return trans_fecmod;
    }

    public void setTrans_usuadd(String trans_usuadd) {
        this.trans_usuadd = trans_usuadd;
    }

    public void setTrans_fecadd(Date trans_fecadd) {
        this.trans_fecadd = trans_fecadd;
    }

    public void setTrans_usumod(String trans_usumod) {
        this.trans_usumod = trans_usumod;
    }

    public void setTrans_fecmod(Date trans_fecmod) {
        this.trans_fecmod = trans_fecmod;
    }

    public String getDesmarca() {
        return desmarca;
    }

    public void setDesmarca(String desmarca) {
        this.desmarca = desmarca;
    }

    public String getDescombustible() {
        return descombustible;
    }

    public void setDescombustible(String descombustible) {
        this.descombustible = descombustible;
    }

    public String getDescolor() {
        return descolor;
    }

    public void setDescolor(String descolor) {
        this.descolor = descolor;
    }

    public String getStrans_fecing() {
        strans_fecing = sdf.format(trans_fecing);
        return strans_fecing;
    }

    public void setStrans_fecing(String strans_fecing) {
        this.strans_fecing = strans_fecing;
    }
}
