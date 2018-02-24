package org.me.gj.model.facturacion.procesos;

import java.text.*;
import java.util.Date;
import java.util.Locale;

public class PedidoVentaDet {

    private String pcab_nroped;
    private Date pcab_fecemi;
    private String pcab_periodo;
    private int suc_id;
    private int emp_id;
    private int pdet_estado;
    private int pdet_situacion;
    private String pro_id;
    private String pro_cla;
    private String lp_id;
    private int pdet_canped;
    private int pdet_canent;
    private double pdet_punit;
    private double pdet_vventa;
    private double pdet_dscto;
    private double pdet_sdscto;
    private double pdet_impto;
    private double pdet_vimpto;
    private double pdet_pventa;
    private int pdet_unipre;
    private String pdet_desart;
    private int pdet_desman;
    private double pdet_proper;
    private int pdet_item;

    private String pdet_usuadd;
    //private Date pdet_fecadd;
    private String pdet_usumod;
    //private Date pdet_fecmod;

    //private double pro_presminven;

    //VARIABLES AUXILIARES PARA DIFERENCIA PRECIOS
    private String difPrecio_spunit;
    private String difPrecio_spventa;
    private String difPrecio_svimpto;
    private String difPrecio_sdscto;

    //VARIABLES AUXILIARES PARA DETALLE DE PEDIDO
    private String lp_des;
    private int pdet_ent;
    private int pdet_frac;
    private String pdet_prodes;
    private String s_vventa;
    private String s_punit;
    private String s_sdscto;
    private String s_vimpto;
    private String s_pventa;
    private boolean valor;
    private boolean val_descman;

    private String ind_accion = "Q";
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat dfv = new DecimalFormat("#,###,##0.0000", dfs);
    DecimalFormat dfv2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public PedidoVentaDet() {
    }

    public PedidoVentaDet(int pdet_item, String pcab_nroped, Date pcab_fecemi, int suc_id, int emp_id, int pdet_estado, int pdet_situacion,
            String pro_id, String pro_cla, String lp_id, int pdet_canped, int pdet_canent, double pdet_punit, double pdet_vventa, double pdet_dscto, double pdet_sdscto,
            double pdet_impto, double pdet_vimpto, double pdet_pventa, int pdet_unipre, /*String pdet_desart,*/ int pdet_desman,/* double pdet_proper,*/
            String pdet_usuadd, String pdet_usumod) {

        dfs = new DecimalFormatSymbols(Locale.US);
        dfv = new DecimalFormat("#,###,##0.0000", dfs);
        dfv2 = new DecimalFormat("#,###,##0.00", dfs);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        this.pdet_item = pdet_item;
        this.pcab_nroped = pcab_nroped;
        this.pcab_fecemi = pcab_fecemi;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.pdet_estado = pdet_estado;
        this.pdet_situacion = pdet_situacion;
        this.pro_id = pro_id;
        this.pro_cla = pro_cla;
        this.lp_id = lp_id;
        this.pdet_canped = pdet_canped;
        this.pdet_canent = pdet_canent;
        this.pdet_punit = pdet_punit;
        this.pdet_vventa = pdet_vventa;
        this.pdet_dscto = pdet_dscto;
        this.pdet_dscto = pdet_dscto;
        this.pdet_sdscto = pdet_sdscto;
        this.pdet_impto = pdet_impto;
        this.pdet_vimpto = pdet_vimpto;
        this.pdet_pventa = pdet_pventa;
        this.pdet_unipre = pdet_unipre;
        //this.pdet_desart = pdet_desart;
        this.pdet_desman = pdet_desman;
        //this.pdet_proper = pdet_proper;
        this.pdet_usuadd = pdet_usuadd;
        //this.pdet_fecadd = pdet_fecadd;
        this.pdet_usumod = pdet_usumod;
        //this.pdet_fecmod = pdet_fecmod;

    }

    public String getPcab_nroped() {
        return pcab_nroped;
    }

    public void setPcab_nroped(String pcab_nroped) {
        this.pcab_nroped = pcab_nroped;
    }

    public Date getPcab_fecemi() {
        return pcab_fecemi;
    }

    public void setPcab_fecemi(Date pcab_fecemi) {
        this.pcab_fecemi = pcab_fecemi;
    }

    public String getPcab_periodo() {
        return pcab_periodo;
    }

    public void setPcab_periodo(String pcab_periodo) {
        this.pcab_periodo = pcab_periodo;
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

    public int getPdet_estado() {
        return pdet_estado;
    }

    public void setPdet_estado(int pdet_estado) {
        this.pdet_estado = pdet_estado;
    }

    public int getPdet_situacion() {
        return pdet_situacion;
    }

    public void setPdet_situacion(int pdet_situacion) {
        this.pdet_situacion = pdet_situacion;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_cla() {
        return pro_cla;
    }

    public void setPro_cla(String pro_cla) {
        this.pro_cla = pro_cla;
    }

    public String getLp_id() {
        return lp_id;
    }

    public void setLp_id(String lp_id) {
        this.lp_id = lp_id;
    }

    public int getPdet_canped() {
        return pdet_canped;
    }

    public void setPdet_canped(int pdet_canped) {
        this.pdet_canped = pdet_canped;
    }

    public int getPdet_canent() {
        return pdet_canent;
    }

    public void setPdet_canent(int pdet_canent) {
        this.pdet_canent = pdet_canent;
    }

    public double getPdet_punit() {
        return pdet_punit;
    }

    public void setPdet_punit(double pdet_punit) {
        this.pdet_punit = pdet_punit;
    }

    public double getPdet_vventa() {
        return pdet_vventa;
    }

    public void setPdet_vventa(double pdet_vventa) {
        this.pdet_vventa = pdet_vventa;
    }

    public double getPdet_dscto() {
        return pdet_dscto;
    }

    public void setPdet_dscto(double pdet_dscto) {
        this.pdet_dscto = pdet_dscto;
    }

    public double getPdet_sdscto() {
        return pdet_sdscto;
    }

    public void setPdet_sdscto(double pdet_sdscto) {
        this.pdet_sdscto = pdet_sdscto;
    }

    public double getPdet_impto() {
        return pdet_impto;
    }

    public void setPdet_impto(double pdet_impto) {
        this.pdet_impto = pdet_impto;
    }

    public double getPdet_vimpto() {
        return pdet_vimpto;
    }

    public void setPdet_vimpto(double pdet_vimpto) {
        this.pdet_vimpto = pdet_vimpto;
    }

    public double getPdet_pventa() {
        return pdet_pventa;
    }

    public void setPdet_pventa(double pdet_pventa) {
        this.pdet_pventa = pdet_pventa;
    }

    public int getPdet_unipre() {
        return pdet_unipre;
    }

    public void setPdet_unipre(int pdet_unipre) {
        this.pdet_unipre = pdet_unipre;
    }

    public String getPdet_desart() {
        return pdet_desart;
    }

    public void setPdet_desart(String pdet_desart) {
        this.pdet_desart = pdet_desart;
    }

    public int getPdet_desman() {
        return pdet_desman;
    }

    public void setPdet_desman(int pdet_desman) {
        this.pdet_desman = pdet_desman;
    }

    public double getPdet_proper() {
        return pdet_proper;
    }

    public void setPdet_proper(double pdet_proper) {
        this.pdet_proper = pdet_proper;
    }

    public int getPdet_item() {
        return pdet_item;
    }

    public void setPdet_item(int pdet_item) {
        this.pdet_item = pdet_item;
    }

    public String getPdet_usuadd() {
        return pdet_usuadd;
    }

    public void setPdet_usuadd(String pdet_usuadd) {
        this.pdet_usuadd = pdet_usuadd;
    }

    public String getPdet_usumod() {
        return pdet_usumod;
    }

    public void setPdet_usumod(String pdet_usumod) {
        this.pdet_usumod = pdet_usumod;
    }

    //VARIABLES AUXILIARES
    public String getLp_des() {
        return lp_des;
    }

    public void setLp_des(String lp_des) {
        this.lp_des = lp_des;
    }

    public int getPdet_ent() {
        double decimal = (pdet_canped / pdet_unipre) % 1;
        pdet_ent = (int) (Math.round((pdet_canped / pdet_unipre) - decimal));
        return pdet_ent;
    }

    public void setPdet_ent(int pdet_ent) {
        this.pdet_ent = pdet_ent;
    }

    public int getPdet_frac() {
        pdet_frac = (int) (Math.round(pdet_canped % pdet_unipre));
        return pdet_frac;
    }

    public void setPdet_frac(int pdet_frac) {
        this.pdet_frac = pdet_frac;
    }

    public String getPdet_prodes() {
        return pdet_prodes;
    }

    public void setPdet_prodes(String pdet_prodes) {
        this.pdet_prodes = pdet_prodes;
    }

    public String getS_vventa() {
        s_vventa = dfv.format(pdet_vventa);
        return s_vventa;
    }

    public void setS_vventa(String s_vventa) {
        this.s_vventa = s_vventa;
    }

    public String getS_punit() {
        s_punit = dfv.format(pdet_punit);
        return s_punit;
    }

    public void setS_punit(String s_punit) {
        this.s_punit = s_punit;
    }

    public String getS_sdscto() {
        return s_sdscto;
    }

    public void setS_sdscto(String s_sdscto) {
        this.s_sdscto = s_sdscto;
    }

    public String getS_vimpto() {
        s_vimpto = dfv.format(pdet_vimpto);
        return s_vimpto;
    }

    public void setS_vimpto(String s_vimpto) {
        this.s_vimpto = s_vimpto;
    }

    public String getS_pventa() {
        s_pventa = dfv.format(pdet_pventa);
        return s_pventa;
    }

    public void setS_pventa(String s_pventa) {
        this.s_pventa = s_pventa;
    }

    public boolean isValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isVal_descman() {
        if (pdet_desman == 1) {
            val_descman = true;
        } else {
            val_descman = false;
        }
        return val_descman;
    }

    public void setVal_descman(boolean val_descman) {
        this.val_descman = val_descman;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    /*public double getPro_presminven() {
        return pro_presminven;
    }

    public void setPro_presminven(double pro_presminven) {
        this.pro_presminven = pro_presminven;
    }*/

    //Para Diferencia de Precios
    public String getDifPrecio_spunit() {
        difPrecio_spunit = dfv2.format(pdet_punit);
        return difPrecio_spunit;
    }

    public void setDifPrecio_spunit(String difPrecio_spunit) {
        this.difPrecio_spunit = difPrecio_spunit;
    }

    public String getDifPrecio_spventa() {
        difPrecio_spventa = dfv2.format(pdet_pventa);
        return difPrecio_spventa;
    }

    public void setDifPrecio_spventa(String difPrecio_spventa) {
        this.difPrecio_spventa = difPrecio_spventa;
    }

    public String getDifPrecio_svimpto() {
        difPrecio_svimpto = dfv2.format(pdet_vimpto);
        return difPrecio_svimpto;
    }

    public void setDifPrecio_svimpto(String difPrecio_svimpto) {
        this.difPrecio_svimpto = difPrecio_svimpto;
    }

    public String getDifPrecio_sdscto() {
        difPrecio_sdscto = dfv2.format(pdet_sdscto);
        return difPrecio_sdscto;
    }

    public void setDifPrecio_sdscto(String difPrecio_sdscto) {
        this.difPrecio_sdscto = difPrecio_sdscto;
    }

}
