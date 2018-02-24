package org.me.gj.model.logistica.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class OrdCompDet {

    private long oc_nropedkey;
    private int emp_id;
    private int suc_id;
    private String oc_ind;
    private int ocd_item;
    private int ocd_est;
    private long pro_key;
    private String pro_id;
    private double ocd_precio;
    private double ocd_cantped;
    private double ocd_cantate;
    private double ocd_vafecto;
    private double ocd_exonerado;
    private double ocd_vimpto;
    private double ocd_pimpto;
    private double ocd_vtotal;
    private String ocd_glosa;
    private double ocd_peso;
    private int ocd_periodo;
    private double ocd_vdesc;
    private double ocd_pdesc;
    private String ocd_usuadd;
    private Date ocd_fecadd;
    private String ocd_pcadd;
    private String ocd_usumod;
    private Date ocd_fecmod;
    private String ocd_pcmod;
    private String ocd_speso;
    private String ocd_sprecio;
    private String ocd_scantped;
    private String ocd_scantate;
    private String ocd_svafecto;
    private String ocd_sexonerado;
    private String ocd_svimpto;
    private String ocd_spimpto;
    private String ocd_svdesc;
    private String ocd_spdesc;
    private String ocd_svtotal;
    private boolean valor;
    private String pro_des;
    private String pro_desdes;
    private String pro_unipeso;
    private double pro_svol;
    private String pro_sunivol;
    private double pro_voltotal;
    private String pro_svoltotal;
    private String pro_sunivoltotal;
    private double pro_pesototal;
    private String pro_spesototal;
    private String pro_unipesototal;
    private String pro_unimanven;
    private String pro_ubi;
    private String pro_ubides;
    private String pro_presmincom;
    private String pro_presminven;
    private double cant_ped;
    private double cant_fac;
    private double pv_oc;
    private double p_vta;
    private double imp_neto;
    private double val_vta;
    private double igv;
    private double subtotal;
    private String pro_unimas;
    private String pro_conv;
    private String pro_provid;
    private String pro_provrazsoc;
    private String pro_almdes;
    private String pro_codori;
    private long ocd_canprov;

    private String ind_accion = "Q";
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df = new DecimalFormat("###,##0", dfs);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    //DecimalFormat df5 = new DecimalFormat("###,##0.00000", dfs);

    public OrdCompDet() {
    }

    public OrdCompDet(long oc_nropedkey, int emp_id, int suc_id, String oc_ind, int ocd_item, String ocd_usumod, String ocd_pcmod) {
        this.oc_nropedkey = oc_nropedkey;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.oc_ind = oc_ind;
        this.ocd_item = ocd_item;
        this.ocd_usumod = ocd_usumod;
        this.ocd_pcmod = ocd_pcmod;
    }

    public OrdCompDet(long oc_nropedkey, int emp_id, int suc_id, String oc_ind, int ocd_item, String pro_id, String pro_des, String pro_desdes, double ocd_precio,
            double ocd_cantped, double ocd_vafecto, double ocd_exonerado, double ocd_pimpto, double ocd_vimpto, String ocd_glosa, double ocd_peso,
            double ocd_vdesc, double ocd_pdesc, double ocd_vtotal, String ocd_usuadd, String ocd_pcadd, String ocd_usumod,
            String ocd_pcmod, double pro_svol, String pro_sunivol, String pro_unipeso, String pro_unimanven,
            double d_voltotal, String s_volundtotal, double d_pesototal, String s_unipesototal, String s_ocd_idubi) {
        this.oc_nropedkey = oc_nropedkey;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.oc_ind = oc_ind;
        this.pro_id = pro_id;
        this.ocd_item = ocd_item;
        this.pro_des = pro_des;
        this.pro_desdes = pro_desdes;
        this.ocd_precio = ocd_precio;
        this.ocd_cantped = ocd_cantped;
        this.ocd_vafecto = ocd_vafecto;
        this.ocd_exonerado = ocd_exonerado;
        this.ocd_vimpto = ocd_vimpto;
        this.ocd_pimpto = ocd_pimpto;
        this.ocd_vtotal = ocd_vtotal;
        this.ocd_glosa = ocd_glosa;
        this.ocd_peso = ocd_peso;
        this.ocd_vdesc = ocd_vdesc;
        this.ocd_pdesc = ocd_pdesc;
        this.ocd_usuadd = ocd_usuadd;
        this.ocd_pcadd = ocd_pcadd;
        this.ocd_usumod = ocd_usumod;
        this.ocd_pcmod = ocd_pcmod;
        this.pro_svol = pro_svol;
        this.pro_sunivol = pro_sunivol;
        this.pro_unipeso = pro_unipeso;
        this.pro_unimanven = pro_unimanven;
        this.pro_voltotal = d_voltotal;
        this.pro_sunivoltotal = s_volundtotal;
        this.pro_pesototal = d_pesototal;
        this.pro_unipesototal = s_unipesototal;
        this.pro_ubi = s_ocd_idubi;

    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public void setCant_ped(double cant_ped) {
        this.cant_ped = cant_ped;
    }

    public void setCant_fac(double cant_fac) {
        this.cant_fac = cant_fac;
    }

    public void setPv_oc(double pv_oc) {
        this.pv_oc = pv_oc;
    }

    public void setP_vta(double p_vta) {
        this.p_vta = p_vta;
    }

    public void setImp_neto(double imp_neto) {
        this.imp_neto = imp_neto;
    }

    public void setVal_vta(double val_vta) {
        this.val_vta = val_vta;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getCant_ped() {
        return cant_ped;
    }

    public double getCant_fac() {
        return cant_fac;
    }

    public double getPv_oc() {
        return pv_oc;
    }

    public double getP_vta() {
        return p_vta;
    }

    public double getImp_neto() {
        return imp_neto;
    }

    public double getVal_vta() {
        return val_vta;
    }

    public double getIgv() {
        return igv;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getPro_presminven() {
        return pro_presminven;
    }

    public void setPro_presminven(String pro_presminven) {
        this.pro_presminven = pro_presminven;
    }

    public String getPro_unimas() {
        return pro_unimas;
    }

    public void setPro_unimas(String pro_unimas) {
        this.pro_unimas = pro_unimas;
    }

    public long getOc_nropedkey() {
        return oc_nropedkey;
    }

    public double getPro_svol() {
        return pro_svol;
    }

    public void setPro_svol(double pro_svol) {
        this.pro_svol = pro_svol;
    }

    public String getPro_unimanven() {
        return pro_unimanven;
    }

    public void setPro_unimanven(String pro_unimanven) {
        this.pro_unimanven = pro_unimanven;
    }

    public String getPro_sunivol() {
        return pro_sunivol;
    }

    public void setOc_nropedkey(long oc_nropedkey) {
        this.oc_nropedkey = oc_nropedkey;
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

    public String getPro_des() {
        return pro_des;
    }

    public void setPro_des(String pro_des) {
        this.pro_des = pro_des;
    }

    public String getPro_desdes() {
        return pro_desdes;
    }

    public String getPro_unipeso() {
        return pro_unipeso;
    }

    public long getOcd_canprov() {
        return ocd_canprov;
    }

    public void setOcd_canprov(long ocd_canprov) {
        this.ocd_canprov = ocd_canprov;
    }

    public void setPro_unipeso(String pro_unipeso) {
        this.pro_unipeso = pro_unipeso;
    }

    public void setPro_desdes(String pro_desdes) {
        this.pro_desdes = pro_desdes;
    }

    public int getOcd_periodo() {
        return ocd_periodo;
    }

    public void setOcd_periodo(int ocd_periodo) {
        this.ocd_periodo = ocd_periodo;
    }

    public String getInd_accion() {
        return ind_accion;
    }

    public void setInd_accion(String ind_accion) {
        this.ind_accion = ind_accion;
    }

    public String getOc_ind() {
        return oc_ind;
    }

    public void setOc_ind(String oc_ind) {
        this.oc_ind = oc_ind;
    }

    public int getOcd_item() {
        return ocd_item;
    }

    public void setOcd_item(int ocd_item) {
        this.ocd_item = ocd_item;
    }

    public int getOcd_est() {
        return ocd_est;
    }

    public void setOcd_est(int ocd_est) {
        this.ocd_est = ocd_est;
    }

    public double getOcd_precio() {
        return ocd_precio;
    }

    public void setOcd_precio(double ocd_precio) {
        this.ocd_precio = ocd_precio;
    }

    public double getOcd_cantped() {
        return ocd_cantped;
    }

    public void setOcd_cantped(double ocd_cantped) {
        this.ocd_cantped = ocd_cantped;
    }

    public double getOcd_cantate() {
        return ocd_cantate;
    }

    public void setOcd_cantate(double ocd_cantate) {
        this.ocd_cantate = ocd_cantate;
    }

    public double getOcd_vafecto() {
        return ocd_vafecto;
    }

    public void setOcd_vafecto(double ocd_vafecto) {
        this.ocd_vafecto = ocd_vafecto;
    }

    public double getOcd_exonerado() {
        return ocd_exonerado;
    }

    public void setOcd_exonerado(double ocd_exonerado) {
        this.ocd_exonerado = ocd_exonerado;
    }

    public double getOcd_vimpto() {
        return ocd_vimpto;
    }

    public void setOcd_vimpto(double ocd_vimpto) {
        this.ocd_vimpto = ocd_vimpto;
    }

    public double getOcd_pimpto() {
        return ocd_pimpto;
    }

    public void setOcd_pimpto(double ocd_pimpto) {
        this.ocd_pimpto = ocd_pimpto;
    }

    public double getOcd_vtotal() {
        return ocd_vtotal;
    }

    public void setOcd_vtotal(double ocd_vtotal) {
        this.ocd_vtotal = ocd_vtotal;
    }

    public String getOcd_glosa() {
        return ocd_glosa;
    }

    public double getOcd_peso() {
        return ocd_peso;
    }

    public void setOcd_peso(double ocd_peso) {
        this.ocd_peso = ocd_peso;
    }

    public void setOcd_glosa(String ocd_glosa) {
        this.ocd_glosa = ocd_glosa;
    }

    public double getOcd_vdesc() {
        return ocd_vdesc;
    }

    public void setOcd_vdesc(double ocd_vdesc) {
        this.ocd_vdesc = ocd_vdesc;
    }

    public double getOcd_pdesc() {
        return ocd_pdesc;
    }

    public void setOcd_pdesc(double ocd_pdesc) {
        this.ocd_pdesc = ocd_pdesc;
    }

    public String getOcd_usuadd() {
        return ocd_usuadd;
    }

    public void setOcd_usuadd(String ocd_usuadd) {
        this.ocd_usuadd = ocd_usuadd;
    }

    public Date getOcd_fecadd() {
        return ocd_fecadd;
    }

    public void setOcd_fecadd(Date ocd_fecadd) {
        this.ocd_fecadd = ocd_fecadd;
    }

    public String getOcd_pcadd() {
        return ocd_pcadd;
    }

    public void setOcd_pcadd(String ocd_pcadd) {
        this.ocd_pcadd = ocd_pcadd;
    }

    public String getOcd_usumod() {
        return ocd_usumod;
    }

    public void setOcd_usumod(String ocd_usumod) {
        this.ocd_usumod = ocd_usumod;
    }

    public Date getOcd_fecmod() {
        return ocd_fecmod;
    }

    public void setOcd_fecmod(Date ocd_fecmod) {
        this.ocd_fecmod = ocd_fecmod;
    }

    public String getOcd_pcmod() {
        return ocd_pcmod;
    }

    public void setOcd_pcmod(String ocd_pcmod) {
        this.ocd_pcmod = ocd_pcmod;
    }

    public boolean isValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String getPro_ubi() {
        return pro_ubi;
    }

    public void setPro_ubi(String pro_ubi) {
        this.pro_ubi = pro_ubi;
    }

    public String getPro_presmincom() {
        return pro_presmincom;
    }

    public void setPro_presmincom(String pro_presmincom) {
        this.pro_presmincom = pro_presmincom;
    }

    public String getPro_conv() {
        return pro_conv;
    }

    public void setPro_conv(String pro_conv) {
        this.pro_conv = pro_conv;
    }

    public String getPro_provid() {
        return pro_provid;
    }

    public void setPro_provid(String pro_provid) {
        this.pro_provid = pro_provid;
    }

    public String getPro_provrazsoc() {
        return pro_provrazsoc;
    }

    public void setPro_provrazsoc(String pro_provrazsoc) {
        this.pro_provrazsoc = pro_provrazsoc;
    }

    public String getPro_almdes() {
        return pro_almdes;
    }

    public void setPro_almdes(String pro_almdes) {
        this.pro_almdes = pro_almdes;
    }

    public String getPro_codori() {
        return pro_codori;
    }

    public void setPro_codori(String pro_codori) {
        this.pro_codori = pro_codori;
    }

    public double getPro_pesototal() {
        return pro_pesototal;
    }

    public void setPro_pesototal(double pro_pesototal) {
        this.pro_pesototal = pro_pesototal;
    }

    public String getPro_unipesototal() {
        return pro_unipesototal;
    }

    public void setPro_unipesototal(String pro_unipesototal) {
        this.pro_unipesototal = pro_unipesototal;
    }

    public double getPro_voltotal() {
        return pro_voltotal;
    }

    public void setPro_svoltotal(double pro_voltotal) {
        this.pro_voltotal = pro_voltotal;
    }
    
    public String getPro_spesototal() {
        pro_spesototal = df2.format(pro_pesototal);
        return pro_spesototal;
    }

    public String getPro_svoltotal() {
        pro_svoltotal = df2.format(pro_voltotal);
        return pro_svoltotal;
    }

    public String getPro_sunivoltotal() {
        return pro_sunivoltotal;
    }

    public void setPro_sunivoltotal(String pro_sunivoltotal) {
        this.pro_sunivoltotal = pro_sunivoltotal;
    }

    public void setPro_sunivol(String pro_sunivol) {
        this.pro_sunivol = pro_sunivol;
    }

    public String getOcd_speso() {
        ocd_speso = df2.format(ocd_peso);
        return ocd_speso;
    }

    public void setOcd_speso(String ocd_speso) {
        this.ocd_speso = ocd_speso;
    }

    public String getOcd_sprecio() {
        ocd_sprecio = df2.format(ocd_precio);
        return ocd_sprecio;
    }

    public String getOcd_scantped() {
        ocd_scantped = df.format(ocd_cantped);
        return ocd_scantped;
    }

    public String getOcd_scantate() {
        ocd_scantate = df.format(ocd_cantate);
        return ocd_scantate;
    }

    public String getOcd_svafecto() {
        ocd_svafecto = df2.format(ocd_vafecto);
        return ocd_svafecto;
    }

    public String getOcd_sexonerado() {
        ocd_sexonerado = df2.format(ocd_exonerado);
        return ocd_sexonerado;
    }

    public String getOcd_svimpto() {
        ocd_svimpto = df2.format(ocd_vimpto);
        return ocd_svimpto;
    }

    public String getOcd_spimpto() {
        ocd_spimpto = df2.format(ocd_pimpto);
        return ocd_spimpto;
    }

    public String getOcd_svdesc() {
        ocd_svdesc = df2.format(ocd_vdesc);
        return ocd_svdesc;
    }

    public String getOcd_spdesc() {
        ocd_spdesc = df2.format(ocd_pdesc);
        return ocd_spdesc;
    }

    public String getOcd_svtotal() {
        ocd_svtotal = df2.format(ocd_vtotal);
        return ocd_svtotal;
    }

    public String getPro_ubides() {
        return pro_ubides;
    }

    public void setPro_ubides(String pro_ubides) {
        this.pro_ubides = pro_ubides;
    }

}
