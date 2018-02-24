package org.me.gj.model.logistica.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class PedidoCompraDet {

    private int pedcomdet_key;
    private String pedcom_id;
    private long pedcom_key;
    private int emp_id;
    private int suc_id;
    private String per_id;
    private int per_key;
    private String pro_id;
    private long pro_key;
    private int pedcom_est;
    private double pedcom_canped;
    private String pedcom_scanped;
    private double pedcom_canrec;
    private double pedcom_preuni;
    private double pedcom_valafe;
    private String pedcom_svalafe;
    private double pedcom_valexo;
    private String pedcom_svalexo;
    private double pedcom_pordes;
    private double pedcom_valdes;
    private String pedcom_svaldes;
    private double pedcom_porimp;
    private double pedcom_valimp;
    private String pedcom_svalimp;
    private double pedcom_valtot;
    private String pedcom_svaltot;
    private String pedcom_glo;
    private int pedcom_genord;
    private double pedcom_canbon;
    private String pedcom_usuadd;
    private Date pedcom_fecadd;
    private String pedcom_usumod;
    private Date pedcom_fecmod;
    private boolean valor;
    private String pro_des;
    private String pro_desdes;
    private String pro_unimanven;
    private double pro_pesouni;
    private String pro_spesouni;
    private double pro_pesotot;
    private String pro_spesotot;
    private String pro_peso_unidaduni;
    private String pro_peso_unidadtot;
    private double pro_voluni;
    private String pro_svoluni;
    private double pro_voltot;
    private String pro_svoltot;
    private String pro_vol_unidaduni;
    private String pro_vol_unidadtot;
    private String pedcom_ubi;
    private String pedcom_ubides;
    private String ind_accion = "Q";
    DecimalFormatSymbols dfs;
    //DecimalFormat dfv;
    DecimalFormat df2;

    public PedidoCompraDet(long pedcom_key, int emp_id, int suc_id, String ped_ind, int pedcomdet_key, String pedcom_usumod) {
        ind_accion = "Q";
        dfs = new DecimalFormatSymbols(Locale.US);
        //dfv = new DecimalFormat("#,###,##0.00000", dfs);
        df2 = new DecimalFormat("#,###,##0.00", dfs);
        this.pedcom_key = pedcom_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        //this.ped_ind = ped_ind;
        this.pedcomdet_key = pedcomdet_key;
        this.pedcom_usumod = pedcom_usumod;
    }

    public PedidoCompraDet(String pedcom_id, long pedcom_key, int emp_id, int suc_id, String per_id, int per_key,
            String pro_id, long pro_key, int pedcom_est, double pedcom_canped, double pedcom_canrec, double pedcom_preuni, double pedcom_valafe, double pedcom_valexo,
            double pedcom_pordes, double pedcom_valdes, double pedcom_valimp, double pedcom_valtot, String pedcom_glo, int pedcom_genord, double pedcom_canbon, String pedcom_usuadd, Date pedcom_fecadd,
            String pedcom_usumod, Date pedcom_fecmod, String pro_des, String pro_desdes, String pedcom_ubi) {
        ind_accion = "Q";
        dfs = new DecimalFormatSymbols(Locale.US);
        //dfv = new DecimalFormat("#,###,##0.00000", dfs);
        df2 = new DecimalFormat("#,###,##0.00", dfs);
        this.pedcom_id = pedcom_id;
        this.pedcom_key = pedcom_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.per_id = per_id;
        this.per_key = per_key;
        this.pro_id = pro_id;
        this.pro_key = pro_key;
        this.pedcom_est = pedcom_est;
        this.pedcom_canped = pedcom_canped;
        this.pedcom_canrec = pedcom_canrec;
        this.pedcom_preuni = pedcom_preuni;
        this.pedcom_valafe = pedcom_valafe;
        this.pedcom_valexo = pedcom_valexo;
        this.pedcom_pordes = pedcom_pordes;
        this.pedcom_valdes = pedcom_valdes;
        this.pedcom_valimp = pedcom_valimp;
        this.pedcom_valtot = pedcom_valtot;
        this.pedcom_glo = pedcom_glo;
        this.pedcom_genord = pedcom_genord;
        this.pedcom_canbon = pedcom_canbon;
        this.pedcom_usuadd = pedcom_usuadd;
        this.pedcom_fecadd = pedcom_fecadd;
        this.pedcom_usumod = pedcom_usumod;
        this.pedcom_fecmod = pedcom_fecmod;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.pedcom_ubi = pedcom_ubi;
    }

    public PedidoCompraDet() {
        ind_accion = "Q";
        dfs = new DecimalFormatSymbols(Locale.US);
        //dfv = new DecimalFormat("#,###,##0.00000", dfs);
        df2 = new DecimalFormat("#,###,##0.00", dfs);
    }

    public int getPedcomdet_key() {
        return pedcomdet_key;
    }

    public void setPedcomdet_key(int pedcomdet_key) {
        this.pedcomdet_key = pedcomdet_key;
    }

    public String getPedcom_id() {
        return pedcom_id;
    }

    public void setPedcom_id(String pedcom_id) {
        this.pedcom_id = pedcom_id;
    }

    public long getPedcom_key() {
        return pedcom_key;
    }

    public void setPedcom_key(long pedcom_key) {
        this.pedcom_key = pedcom_key;
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

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public int getPer_key() {
        return per_key;
    }

    public void setPer_key(int per_key) {
        this.per_key = per_key;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public long getPro_key() {
        return pro_key;
    }

    public void setPro_key(long pro_key) {
        this.pro_key = pro_key;
    }

    public int getPedcom_est() {
        return pedcom_est;
    }

    public void setPedcom_est(int pedcom_est) {
        this.pedcom_est = pedcom_est;
    }

    public double getPedcom_canped() {
        return pedcom_canped;
    }

    public void setPedcom_canped(double pedcom_canped) {
        this.pedcom_canped = pedcom_canped;
    }

    public double getPedcom_canrec() {
        return pedcom_canrec;
    }

    public void setPedcom_canrec(double pedcom_canrec) {
        this.pedcom_canrec = pedcom_canrec;
    }

    public double getPedcom_preuni() {
        return pedcom_preuni;
    }

    public void setPedcom_preuni(double pedcom_preuni) {
        this.pedcom_preuni = pedcom_preuni;
    }

    public double getPedcom_valafe() {
        return pedcom_valafe;
    }

    public void setPedcom_valafe(double pedcom_valafe) {
        this.pedcom_valafe = pedcom_valafe;
    }

    public double getPedcom_valexo() {
        return pedcom_valexo;
    }

    public void setPedcom_valexo(double pedcom_valexo) {
        this.pedcom_valexo = pedcom_valexo;
    }

    public double getPedcom_pordes() {
        return pedcom_pordes;
    }

    public void setPedcom_pordes(double pedcom_pordes) {
        this.pedcom_pordes = pedcom_pordes;
    }

    public double getPedcom_valdes() {
        return pedcom_valdes;
    }

    public void setPedcom_valdes(double pedcom_valdes) {
        this.pedcom_valdes = pedcom_valdes;
    }

    public double getPedcom_valimp() {
        return pedcom_valimp;
    }

    public void setPedcom_valimp(double pedcom_valimp) {
        this.pedcom_valimp = pedcom_valimp;
    }

    public double getPedcom_valtot() {
        return pedcom_valtot;
    }

    public void setPedcom_valtot(double pedcom_valtot) {
        this.pedcom_valtot = pedcom_valtot;
    }

    public String getPedcom_glo() {
        return pedcom_glo;
    }

    public void setPedcom_glo(String pedcom_glo) {
        this.pedcom_glo = pedcom_glo;
    }

    public int getPedcom_genord() {
        return pedcom_genord;
    }

    public void setPedcom_genord(int pedcom_genord) {
        this.pedcom_genord = pedcom_genord;
    }

    public double getPedcom_canbon() {
        return pedcom_canbon;
    }

    public void setPedcom_canbon(double pedcom_canbon) {
        this.pedcom_canbon = pedcom_canbon;
    }

    public String getPedcom_usuadd() {
        return pedcom_usuadd;
    }

    public void setPedcom_usuadd(String pedcom_usuadd) {
        this.pedcom_usuadd = pedcom_usuadd;
    }

    public Date getPedcom_fecadd() {
        return pedcom_fecadd;
    }

    public void setPedcom_fecadd(Date pedcom_fecadd) {
        this.pedcom_fecadd = pedcom_fecadd;
    }

    public String getPedcom_usumod() {
        return pedcom_usumod;
    }

    public void setPedcom_usumod(String pedcom_usumod) {
        this.pedcom_usumod = pedcom_usumod;
    }

    public Date getPedcom_fecmod() {
        return pedcom_fecmod;
    }

    public void setPedcom_fecmod(Date pedcom_fecmod) {
        this.pedcom_fecmod = pedcom_fecmod;
    }

    public boolean isValor() {
        if (pedcom_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPedcom_svalimp() {
        return pedcom_svalimp;
    }

    public void setPedcom_svalimp(double pedcom_svalimp) {
        this.pedcom_svalimp = df2.format(pedcom_svalimp);
    }

    public String getPedcom_svaltot() {
        return pedcom_svaltot;
    }

    public void setPedcom_svaltot(double pedcom_svaltot) {
        this.pedcom_svaltot = df2.format(pedcom_svaltot);
    }

    public String getPedcom_scanped() {
        return pedcom_scanped;
    }

    public void setPedcom_scanped(double pedcom_scanped) {
        this.pedcom_scanped = df2.format(pedcom_scanped);
    }

    public String getPedcom_svalafe() {
        return pedcom_svalafe;
    }

    public void setPedcom_svalafe(double pedcom_svalafe) {
        this.pedcom_svalafe = df2.format(pedcom_svalafe);
    }

    public String getPedcom_svalexo() {
        return pedcom_svalexo;
    }

    public void setPedcom_svalexo(double pedcom_svalexo) {
        this.pedcom_svalexo = df2.format(pedcom_svalexo);
    }

    public String getPedcom_svaldes() {
        return pedcom_svaldes;
    }

    public void setPedcom_svaldes(double pedcom_svaldes) {
        this.pedcom_svaldes = df2.format(pedcom_svaldes);
    }

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public String getPro_desdes() {
        return pro_desdes;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public double getPedcom_porimp() {
        return pedcom_porimp;
    }

    public void setPedcom_porimp(double pedcom_porimp) {
        this.pedcom_porimp = pedcom_porimp;
    }

    public double getPro_pesouni() {
        return pro_pesouni;
    }

    public void setPro_pesouni(double pro_pesouni) {
        this.pro_pesouni = pro_pesouni;
    }

    public String getPro_spesouni() {
        return pro_spesouni;
    }

    public void setPro_spesouni(double pro_spesouni) {
        this.pro_spesouni = df2.format(pro_spesouni);
    }

    public double getPro_pesotot() {
        return pro_pesotot;
    }

    public void setPro_pesotot(double pro_pesotot) {
        this.pro_pesotot = pro_pesotot;
    }

    public String getPro_spesotot() {
        return pro_spesotot;
    }

    public void setPro_spesotot(double pro_spesotot) {
        this.pro_spesotot = df2.format(pro_spesotot);
    }

    public String getPro_peso_unidaduni() {
        return pro_peso_unidaduni;
    }

    public void setPro_peso_unidaduni(String pro_peso_unidaduni) {
        this.pro_peso_unidaduni = pro_peso_unidaduni;
    }

    public String getPro_peso_unidadtot() {
        return pro_peso_unidadtot;
    }

    public void setPro_peso_unidadtot(String pro_peso_unidadtot) {
        this.pro_peso_unidadtot = pro_peso_unidadtot;
    }

    public double getPro_voluni() {
        return pro_voluni;
    }

    public void setPro_voluni(double pro_voluni) {
        this.pro_voluni = pro_voluni;
    }

    public String getPro_svoluni() {
        return pro_svoluni;
    }

    public void setPro_svoluni(double pro_svoluni) {
        this.pro_svoluni = df2.format(pro_svoluni);
    }

    public double getPro_voltot() {
        return pro_voltot;
    }

    public void setPro_voltot(double pro_voltot) {
        this.pro_voltot = pro_voltot;
    }

    public String getPro_svoltot() {
        return pro_svoltot;
    }

    public void setPro_svoltot(double pro_svoltot) {
        this.pro_svoltot = df2.format(pro_svoltot);
    }

    public String getPro_vol_unidaduni() {
        return pro_vol_unidaduni;
    }

    public void setPro_vol_unidaduni(String pro_vol_unidaduni) {
        this.pro_vol_unidaduni = pro_vol_unidaduni;
    }

    public String getPro_vol_unidadtot() {
        return pro_vol_unidadtot;
    }

    public void setPro_vol_unidadtot(String pro_vol_unidadtot) {
        this.pro_vol_unidadtot = pro_vol_unidadtot;
    }

    public String getPro_unimanven() {
        return pro_unimanven;
    }

    public void setPro_unimanven(String pro_unimanven) {
        this.pro_unimanven = pro_unimanven;
    }

    public String getPedcom_ubi() {
        return pedcom_ubi;
    }

    public void setPedcom_ubi(String pedcom_ubi) {
        this.pedcom_ubi = pedcom_ubi;
    }

    public String getPedcom_ubides() {
        return pedcom_ubides;
    }

    public void setPedcom_ubides(String pedcom_ubides) {
        this.pedcom_ubides = pedcom_ubides;
    }

}
