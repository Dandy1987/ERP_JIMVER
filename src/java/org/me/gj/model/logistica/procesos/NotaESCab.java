package org.me.gj.model.logistica.procesos;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotaESCab {

    private String nescab_id;
    private String nescab_key;
    private String nescab_tipnotaes;
    private String notaes;
    private String nescab_sernotaes;
    private int emp_id;
    private int suc_id;
    private String nescab_serie;
    private String nescab_nroped;
    private Date nescab_fecha;
    private String fecha;
    private String nescab_glosa;
    private Time hora;
    private int nescab_situacion;
    private int nescab_est;
    private String estado;
    private String nescab_ocind;
    private long nescab_ocnropedkey;
    private String ord_compra;
    private String nescab_tipdoc;
    private String nescab_nroserie;
    private String nescab_nrodoc;
    private String referencia;
    private String nescab_provid;
    private String nescab_provrazsoc;
    private String nescab_cliid;
    private String nescab_clinom;
    private int nescab_moneda;
    private double nescab_tcamb;
    private String nescab_almori;
    private String nescab_almdes;
    private int nescab_costeo;
    private String costeo;
    private int nescab_despacho;
    private String despacho;
    private String nescab_sitdes;
    private String nescab_nrodep;
    private String nescab_usuadd;
    private Date nescab_fecadd;
    private String nescab_usumod;
    private Date nescab_fecmod;
    private Double vtotal;
    private String svtotal;
    private boolean valor;
    private boolean valSelec;
    private String listaprecio;

    //
    private String notaesinf;
    private String clienteinf;
    private String proveedorinf;
    private boolean selImp;
    private String concepto;
    private String situacion;

    //VALORES TOTALES - CONTABILIZAR FACTURA PROVEEDOR
    private double v_afecto;
    private double v_inafecto;
    private double v_impto;
    private double p_venta;
    private String s_vafecto;
    private String s_vinafecto;
    private String s_vimpto;
    private String s_pventa;
    //VALORES DE DETALLE COSTEO AUTOMATICO
    private String cstauto_idproducto;
    private String cstauto_desproducto;
    private double cstauto_prefac;
    private double cstauto_cantfac;
    private double cstauto_vventafac;
    private double cstauto_prenota;
    private double cstauto_cantnota;
    private double cstauto_vventanota;
    private double cstauto_totalfac;
    private double cstauto_totalnota;
    private double cstauto_difpre;
    private double cstauto_difcant;
    private double cstauto_difvventa;
    private double cstauto_diftotal;
    private String cstauto_situacion;
    
    private String cstauto_svventafac;
    private String cstauto_svventanota;
    private String cstauto_stotalfac;
    private String cstauto_stotalnota;
    private String cstauto_sdifvventa;
    private String cstauto_sdiftotal;

    private final DecimalFormatSymbols dfs;
    private final DecimalFormat df2;
    

    public NotaESCab() {
        dfs = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("###,##0.00", dfs);
    }

    public NotaESCab(String nescab_id, String nescab_key, String nescab_tipnotaes, String nescab_sernotaes, int emp_id, int suc_id, String nescab_nroped, Date nescab_fecha, 
            String nescab_glosa, int nescab_situacion, int nescab_est, String nescab_ocind, long nescab_ocnropedkey, String nescab_tipdoc, String nescab_nroserie,String nescab_nrodoc, 
            String nescab_provid, String nescab_cliid, int nescab_moneda, double nescab_tcamb, String nescab_almori, String nescab_almdes, int nescab_costeo, int nescab_despacho, 
            String nescab_nrodep, String nescab_usuadd, Date gcab_fecadd, String nescab_usumod, Date gcab_fecmod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.nescab_tipnotaes = nescab_tipnotaes;
        this.nescab_sernotaes = nescab_sernotaes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nescab_nroped = nescab_nroped;
        this.nescab_fecha = nescab_fecha;
        this.nescab_glosa = nescab_glosa;
        this.nescab_situacion = nescab_situacion;
        this.nescab_est = nescab_est;
        this.nescab_ocind = nescab_ocind;
        this.nescab_ocnropedkey = nescab_ocnropedkey;
        this.nescab_tipdoc = nescab_tipdoc;
        this.nescab_nroserie = nescab_nroserie;
        this.nescab_nrodoc = nescab_nrodoc;
        this.nescab_provid = nescab_provid;
        this.nescab_cliid = nescab_cliid;
        this.nescab_moneda = nescab_moneda;
        this.nescab_tcamb = nescab_tcamb;
        this.nescab_almori = nescab_almori;
        this.nescab_almdes = nescab_almdes;
        this.nescab_costeo = nescab_costeo;
        this.nescab_despacho = nescab_despacho;
        this.nescab_nrodep = nescab_nrodep;
        this.nescab_usuadd = nescab_usuadd;
        this.nescab_fecadd = gcab_fecadd;
        this.nescab_usumod = nescab_usumod;
        this.nescab_fecmod = gcab_fecmod;

        dfs = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("###,##0.00", dfs);
    }

    public NotaESCab(String nescab_id, String nescab_key, String nescab_tipnotaes, String nescab_sernotaes, int emp_id, int suc_id, String nescab_nroped, Date nescab_fecha, 
            String nescab_glosa, int nescab_situacion, int nescab_est, String nescab_ocind, long nescab_ocnropedkey, String nescab_tipdoc, String nescab_nroserie,String nescab_nrodoc, 
            String nescab_provid, String nescab_cliid, int nescab_moneda, double nescab_tcamb, String nescab_almori, String nescab_almdes, int nescab_costeo, int nescab_despacho, 
            String nescab_nrodep, String nescab_usuadd, String nescab_usumod) {
        this.nescab_id = nescab_id;
        this.nescab_key = nescab_key;
        this.nescab_tipnotaes = nescab_tipnotaes;
        this.nescab_sernotaes = nescab_sernotaes;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.nescab_nroped = nescab_nroped;
        this.nescab_fecha = nescab_fecha;
        this.nescab_glosa = nescab_glosa;
        this.nescab_situacion = nescab_situacion;
        this.nescab_est = nescab_est;
        this.nescab_ocind = nescab_ocind;
        this.nescab_ocnropedkey = nescab_ocnropedkey;
        this.nescab_tipdoc = nescab_tipdoc;
        this.nescab_nroserie = nescab_nroserie;
        this.nescab_nrodoc = nescab_nrodoc;
        this.nescab_provid = nescab_provid;
        this.nescab_cliid = nescab_cliid;
        this.nescab_moneda = nescab_moneda;
        this.nescab_tcamb = nescab_tcamb;
        this.nescab_almori = nescab_almori;
        this.nescab_almdes = nescab_almdes;
        this.nescab_costeo = nescab_costeo;
        this.nescab_despacho = nescab_despacho;
        this.nescab_nrodep = nescab_nrodep;
        this.nescab_usuadd = nescab_usuadd;
        this.nescab_usumod = nescab_usumod;

        dfs = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("###,##0.00", dfs);
    }

    public String getNescab_sernotaes() {
        return nescab_sernotaes;
    }

    public void setNescab_sernotaes(String nescab_sernotaes) {
        this.nescab_sernotaes = nescab_sernotaes;
    }

    public String getNescab_id() {
        return nescab_id;
    }

    public void setNescab_id(String nescab_id) {
        this.nescab_id = nescab_id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getSituacion() {
        if ("N".equals(concepto)) {
            situacion = "NORMAL";
        } else if ("FF".equals(concepto)) {
            situacion = "FALTA FACTURA";
        } else if ("FP".equals(concepto)) {
            situacion = "FALTA PRODUCTO";
        } else if ("FS".equals(concepto)) {
            situacion = "FALTA SOLES";
        } else if ("FP / FS".equals(concepto)) {
            situacion = "FALTA PROD / FALTA SOLES";
        }
        return situacion;
    }

    public String getNescab_key() {
        return nescab_key;
    }

    public void setNescab_key(String nescab_key) {
        this.nescab_key = nescab_key;
    }

    public String getNescab_tipnotaes() {
        return nescab_tipnotaes;
    }

    public void setNescab_tipnotaes(String nescab_tipnotaes) {
        this.nescab_tipnotaes = nescab_tipnotaes;
    }

    public String getNotaes() {
        return notaes;
    }

    public void setNotaes(String guia) {
        this.notaes = guia;
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

    public String getNescab_serie() {
        return nescab_serie;
    }

    public void setNescab_serie(String gcab_serie) {
        this.nescab_serie = gcab_serie;
    }

    public String getNescab_nroped() {
        return nescab_nroped;
    }

    public void setNescab_nroped(String nescab_nroped) {
        this.nescab_nroped = nescab_nroped;
    }

    public Date getNescab_fecha() {
        return nescab_fecha;
    }

    public void setNescab_fecha(Date nescab_fecha) {
        this.nescab_fecha = nescab_fecha;
    }

    public String getFecha() {
        String fecha_cadena;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(nescab_fecha);
        return fecha_cadena;
    }

    public String getNescab_glosa() {
        return nescab_glosa;
    }

    public void setNescab_glosa(String nescab_glosa) {
        this.nescab_glosa = nescab_glosa;
    }

    public int getNescab_situacion() {
        return nescab_situacion;
    }

    public void setNescab_situacion(int nescab_situacion) {
        this.nescab_situacion = nescab_situacion;
    }

    public int getNescab_est() {
        return nescab_est;
    }

    public void setNescab_est(int nescab_est) {
        this.nescab_est = nescab_est;
    }

    public String getEstado() {
        if (nescab_est == 1) {
            estado = "ACT";
        } else {
            estado = "ANU";
        }
        return estado;
    }

    public String getNescab_ocind() {
        return nescab_ocind;
    }

    public void setNescab_ocind(String nescab_ocind) {
        this.nescab_ocind = nescab_ocind;
    }

    public long getNescab_ocnropedkey() {
        return nescab_ocnropedkey;
    }

    public void setNescab_ocnropedkey(long nescab_ocnropedkey) {
        this.nescab_ocnropedkey = nescab_ocnropedkey;
    }

    public String getOrd_compra() {
        return ord_compra;
    }

    public void setOrd_compra(String ord_compra) {
        this.ord_compra = ord_compra;
    }

    public String getNescab_tipdoc() {
        return nescab_tipdoc;
    }

    public void setNescab_tipdoc(String nescab_tipdoc) {
        this.nescab_tipdoc = nescab_tipdoc;
    }

    public String getNescab_nroserie() {
        return nescab_nroserie;
    }

    public void setNescab_nroserie(String nescab_nroserie) {
        this.nescab_nroserie = nescab_nroserie;
    }
    
    public String getNescab_nrodoc() {
        return nescab_nrodoc;
    }

    public void setNescab_nrodoc(String nescab_nrodoc) {
        this.nescab_nrodoc = nescab_nrodoc;
    }

    public String getReferencia() {
        if (nescab_tipdoc == null && nescab_nrodoc != null) {
            referencia = nescab_nroserie + nescab_nrodoc;
        } else {
            referencia = nescab_tipdoc + nescab_nroserie + nescab_nrodoc;
        }
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNescab_provid() {
        return nescab_provid;
    }

    public void setNescab_provid(String nescab_provid) {
        this.nescab_provid = nescab_provid;
    }

    public String getNescab_provrazsoc() {
        return nescab_provrazsoc;
    }

    public void setNescab_provrazsoc(String gcab_provrazsoc) {
        this.nescab_provrazsoc = gcab_provrazsoc;
    }

    public String getNescab_cliid() {
        return nescab_cliid;
    }

    public String getNescab_clinom() {
        return nescab_clinom;
    }

    public void setNescab_clinom(String gcab_clinom) {
        this.nescab_clinom = gcab_clinom;
    }

    public void setNescab_cliid(String nescab_cliid) {
        this.nescab_cliid = nescab_cliid;
    }

    public int getNescab_moneda() {
        return nescab_moneda;
    }

    public void setNescab_moneda(int nescab_moneda) {
        this.nescab_moneda = nescab_moneda;
    }

    public double getNescab_tcamb() {
        return nescab_tcamb;
    }

    public void setNescab_tcamb(double nescab_tcamb) {
        this.nescab_tcamb = nescab_tcamb;
    }

    public String getNescab_almori() {
        return nescab_almori;
    }

    public void setNescab_almori(String nescab_almori) {
        this.nescab_almori = nescab_almori;
    }

    public String getNescab_almdes() {
        return nescab_almdes;
    }

    public void setNescab_almdes(String nescab_almdes) {
        this.nescab_almdes = nescab_almdes;
    }

    public int getNescab_costeo() {
        return nescab_costeo;
    }

    public void setNescab_costeo(int nescab_costeo) {
        this.nescab_costeo = nescab_costeo;
    }

    public String getCosteo() {
        if (nescab_costeo == 1) {
            costeo = "SI";
        } else {
            costeo = "NO";
        }
        return costeo;
    }

    public int getNescab_despacho() {
        return nescab_despacho;
    }

    public void setNescab_despacho(int nescab_despacho) {
        this.nescab_despacho = nescab_despacho;
    }

    public String getDespacho() {
        if (nescab_despacho == 1) {
            despacho = "D";
        } else {
            despacho = "ND";
        }
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    public String getNescab_sitdes() {
        return nescab_sitdes;
    }

    public void setNescab_sitdes(String gcab_sitdes) {
        this.nescab_sitdes = gcab_sitdes;
    }

    public String getNescab_nrodep() {
        return nescab_nrodep;
    }

    public void setNescab_nrodep(String nescab_nrodep) {
        this.nescab_nrodep = nescab_nrodep;
    }

    public String getNescab_usuadd() {
        return nescab_usuadd;
    }

    public void setNescab_usuadd(String nescab_usuadd) {
        this.nescab_usuadd = nescab_usuadd;
    }

    public Date getNescab_fecadd() {
        return nescab_fecadd;
    }

    public void setNescab_fecadd(Date gcab_fecadd) {
        this.nescab_fecadd = gcab_fecadd;
    }

    public String getNescab_usumod() {
        return nescab_usumod;
    }

    public void setNescab_usumod(String nescab_usumod) {
        this.nescab_usumod = nescab_usumod;
    }

    public Date getNescab_fecmod() {
        return nescab_fecmod;
    }

    public void setNescab_fecmod(Date gcab_fecmod) {
        this.nescab_fecmod = gcab_fecmod;
    }

    public boolean isValor() {
        valor = nescab_est == 1;
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public Double getVtotal() {
        return vtotal;
    }

    public void setVtotal(Double vtotal) {
        this.vtotal = vtotal;
    }

    public String getSvtotal() {
        svtotal = df2.format(vtotal);
        return svtotal;
    }

    public void setSvtotal(String svtotal) {
        this.svtotal = svtotal;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    //
    public String getNotaesinf() {
        notaesinf = nescab_tipnotaes + " " + notaes;
        return notaesinf;
    }

    public String getClienteinf() {
        clienteinf = nescab_cliid;
        return clienteinf;
    }

    public String getProveedorinf() {
        proveedorinf = nescab_provid + "-" + nescab_provrazsoc;
        return proveedorinf;
    }

    public boolean isSelImp() {
        return selImp;
    }

    public void setSelImp(boolean selImp) {
        this.selImp = selImp;
    }

    public double getV_afecto() {
        return v_afecto;
    }

    public void setV_afecto(double v_afecto) {
        this.v_afecto = v_afecto;
    }

    public double getV_inafecto() {
        return v_inafecto;
    }

    public void setV_inafecto(double v_inafecto) {
        this.v_inafecto = v_inafecto;
    }

    public double getV_impto() {
        return v_impto;
    }

    public void setV_impto(double v_impto) {
        this.v_impto = v_impto;
    }

    public double getP_venta() {
        return p_venta;
    }

    public void setP_venta(double p_venta) {
        this.p_venta = p_venta;
    }

    public String getS_vafecto() {
        s_vafecto = df2.format(v_afecto);
        return s_vafecto;
    }

    public String getS_vinafecto() {
        s_vinafecto = df2.format(v_inafecto);
        return s_vinafecto;
    }

    public String getS_vimpto() {
        s_vimpto = df2.format(v_impto);
        return s_vimpto;
    }

    public String getS_pventa() {
        s_pventa = df2.format(p_venta);
        return s_pventa;
    }
    
    public String getListaprecio() {
        return listaprecio;
    }

    public void setListaprecio(String listaprecio) {
        this.listaprecio = listaprecio;
    }
    
    //-----------VALORES PARA DETALLE COSTEO AUTOMATICO
    public String getCstauto_idproducto() {
        return cstauto_idproducto;
    }

    public void setCstauto_idproducto(String cstauto_idproducto) {
        this.cstauto_idproducto = cstauto_idproducto;
    }

    public String getCstauto_desproducto() {
        return cstauto_desproducto;
    }

    public void setCstauto_desproducto(String cstauto_desproducto) {
        this.cstauto_desproducto = cstauto_desproducto;
    }

    public double getCstauto_prefac() {
        return cstauto_prefac;
    }

    public void setCstauto_prefac(double cstauto_prefac) {
        this.cstauto_prefac = cstauto_prefac;
    }

    public double getCstauto_cantfac() {
        return cstauto_cantfac;
    }

    public void setCstauto_cantfac(double cstauto_cantfac) {
        this.cstauto_cantfac = cstauto_cantfac;
    }

    public double getCstauto_vventafac() {
        return cstauto_vventafac;
    }

    public void setCstauto_vventafac(double cstauto_vventafac) {
        this.cstauto_vventafac = cstauto_vventafac;
    }

    public double getCstauto_prenota() {
        return cstauto_prenota;
    }

    public void setCstauto_prenota(double cstauto_prenota) {
        this.cstauto_prenota = cstauto_prenota;
    }

    public double getCstauto_cantnota() {
        return cstauto_cantnota;
    }

    public void setCstauto_cantnota(double cstauto_cantnota) {
        this.cstauto_cantnota = cstauto_cantnota;
    }

    public double getCstauto_vventanota() {
        return cstauto_vventanota;
    }

    public void setCstauto_vventanota(double cstauto_vventanota) {
        this.cstauto_vventanota = cstauto_vventanota;
    }

    public double getCstauto_totalfac() {
        return cstauto_totalfac;
    }

    public void setCstauto_totalfac(double cstauto_totalfac) {
        this.cstauto_totalfac = cstauto_totalfac;
    }

    public double getCstauto_totalnota() {
        return cstauto_totalnota;
    }

    public void setCstauto_totalnota(double cstauto_totalnota) {
        this.cstauto_totalnota = cstauto_totalnota;
    }

    public double getCstauto_difpre() {
        return cstauto_difpre;
    }

    public void setCstauto_difpre(double cstauto_difpre) {
        this.cstauto_difpre = cstauto_difpre;
    }

    public double getCstauto_difcant() {
        return cstauto_difcant;
    }

    public void setCstauto_difcant(double cstauto_difcant) {
        this.cstauto_difcant = cstauto_difcant;
    }

    public double getCstauto_difvventa() {
        return cstauto_difvventa;
    }

    public void setCstauto_difvventa(double cstauto_difvventa) {
        this.cstauto_difvventa = cstauto_difvventa;
    }

    public double getCstauto_diftotal() {
        return cstauto_diftotal;
    }

    public void setCstauto_diftotal(double cstauto_diftotal) {
        this.cstauto_diftotal = cstauto_diftotal;
    }

    public String getCstauto_situacion() {
        return cstauto_situacion;
    }

    public void setCstauto_situacion(String cstauto_situacion) {
        this.cstauto_situacion = cstauto_situacion;
    }
    
    //variables String costeoautomatico
    public String getCstauto_svventafac() {
        cstauto_svventafac = df2.format(cstauto_vventafac);
        return cstauto_svventafac;
    }

    public void setCstauto_svventafac(String cstauto_svventafac) {
        this.cstauto_svventafac = cstauto_svventafac;
    }

    public String getCstauto_svventanota() {
        cstauto_svventanota = df2.format(cstauto_vventanota);
        return cstauto_svventanota;
    }

    public void setCstauto_svventanota(String cstauto_svventanota) {
        this.cstauto_svventanota = cstauto_svventanota;
    }

    public String getCstauto_stotalfac() {
        cstauto_stotalfac = df2.format(cstauto_totalfac);
        return cstauto_stotalfac;
    }

    public void setCstauto_stotalfac(String cstauto_stotalfac) {
        this.cstauto_stotalfac = cstauto_stotalfac;
    }

    public String getCstauto_stotalnota() {
        cstauto_stotalnota = df2.format(cstauto_totalnota);
        return cstauto_stotalnota;
    }

    public void setCstauto_stotalnota(String cstauto_stotalnota) {
        this.cstauto_stotalnota = cstauto_stotalnota;
    }

    public String getCstauto_sdifvventa() {
        cstauto_sdifvventa = df2.format(cstauto_difvventa);
        return cstauto_sdifvventa;
    }

    public void setCstauto_sdifvventa(String cstauto_sdifvventa) {
        this.cstauto_sdifvventa = cstauto_sdifvventa;
    }

    public String getCstauto_sdiftotal() {
        cstauto_sdiftotal = df2.format(cstauto_diftotal);
        return cstauto_sdiftotal;
    }

    public void setCstauto_sdiftotal(String cstauto_sdiftotal) {
        this.cstauto_sdiftotal = cstauto_sdiftotal;
    }
    
}
