package org.me.gj.model.logistica.procesos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.me.gj.util.Utilitarios;

public class ComprasCab {

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("###,##0.00", dfs);
    //DecimalFormat df4 = new DecimalFormat("###,##0.0000", dfs);

    private long tc_key;
    private int emp_id;
    private int suc_id;
    private String tc_id;
    private String tc_tipdoc;
    private String tc_serie;
    private String tc_nrodoc;
    private String tc_provid;
    private Date tc_fecemi;
    private Date tc_fecven;
    private long tc_nrpedkey;
    private int tc_moneda;
    private double tc_tcambio;
    private int tc_conid;
    private double tc_dscgral;
    private double tc_dscfin;
    private double tc_ocvtotal;
    private double tc_valventa;
    private double tc_vimpt;
    private double tc_vtotal;
    private String tc_glosa;
    private int tc_est;
    private int tc_efectivo;
    private String tc_usuadd;
    private Date tc_fecadd;
    private String tc_usumod;
    private Date tc_fecmod;
    private String tc_provrazsoc;
    private String tc_condicionpago;
    private String tc_estado;
    private String tc_ocompra;
    private String tc_svtotal;
    private String tc_tipodocumento;
    private boolean valor;
    private boolean valorefec;

    Utilitarios objUtil = new Utilitarios();

    public ComprasCab() {
    }

    public ComprasCab(long tc_key, int emp_id, int suc_id, String tc_id, String tc_tipdoc, String tc_serie, String tc_nrodoc, String tc_provid, Date tc_fecemi, Date tc_fecven, long tc_nrpedkey, int tc_moneda, double tc_tcambio, int tc_conid,
            double tc_dscgral, double tc_dscfin, double tc_ocvtotal, double tc_valventa, double tc_vimpt, double tc_vtotal, String tc_glosa, int tc_est, int tc_efectivo, String tc_usuadd, Date tc_fecadd, String tc_usumod, Date tc_fecmod) {
        this.tc_key = tc_key;
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tc_id = tc_id;
        this.tc_tipdoc = tc_tipdoc;
        this.tc_serie = tc_serie;
        this.tc_nrodoc = tc_nrodoc;
        this.tc_provid = tc_provid;
        this.tc_fecemi = tc_fecemi;
        this.tc_fecven = tc_fecven;
        this.tc_nrpedkey = tc_nrpedkey;
        this.tc_moneda = tc_moneda;
        this.tc_tcambio = tc_tcambio;
        this.tc_conid = tc_conid;
        this.tc_dscgral = tc_dscgral;
        this.tc_dscfin = tc_dscfin;
        this.tc_ocvtotal = tc_ocvtotal;
        this.tc_valventa = tc_valventa;
        this.tc_vimpt = tc_vimpt;
        this.tc_vtotal = tc_vtotal;
        this.tc_glosa = tc_glosa;
        this.tc_est = tc_est;
        this.tc_efectivo = tc_efectivo;
        this.tc_usuadd = tc_usuadd;
        this.tc_fecadd = tc_fecadd;
        this.tc_usumod = tc_usumod;
        this.tc_fecmod = tc_fecmod;
    }

    public ComprasCab(int emp_id, int suc_id, long tc_key, String tc_tipdoc, String tc_serie, String tc_nrodoc, String tc_provid, Date tc_fecemi, Date tc_fecven, long tc_nrpedkey, int tc_moneda, double tc_tcambio, int tc_conid,
            double tc_dscgral, double tc_dscfin, /*double tc_tdsctos,*/ double tc_ocvtotal, double tc_valventa, double tc_vimpt, double tc_vtotal, String tc_glosa, int tc_est, int tc_efectivo, String tc_usuadd, String tc_usumod) {
        this.emp_id = emp_id;
        this.suc_id = suc_id;
        this.tc_key = tc_key;
        this.tc_tipdoc = tc_tipdoc;
        this.tc_serie = tc_serie;
        this.tc_nrodoc = tc_nrodoc;
        this.tc_provid = tc_provid;
        this.tc_fecemi = tc_fecemi;
        this.tc_fecven = tc_fecven;
        this.tc_nrpedkey = tc_nrpedkey;
        this.tc_moneda = tc_moneda;
        this.tc_tcambio = tc_tcambio;
        this.tc_conid = tc_conid;
        this.tc_dscgral = tc_dscgral;
        this.tc_dscfin = tc_dscfin;
        //this.tc_tdsctos = tc_tdsctos;
        this.tc_ocvtotal = tc_ocvtotal;
        this.tc_valventa = tc_valventa;
        this.tc_vimpt = tc_vimpt;
        this.tc_vtotal = tc_vtotal;
        this.tc_glosa = tc_glosa;
        this.tc_est = tc_est;
        this.tc_efectivo = tc_efectivo;
        this.tc_usuadd = tc_usuadd;
        this.tc_usumod = tc_usumod;
    }

    public long getTc_key() {
        return tc_key;
    }

    public void setTc_key(long tc_key) {
        this.tc_key = tc_key;
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

    public String getTc_id() {
        return tc_id;
    }

    public void setTc_id(String tc_id) {
        this.tc_id = tc_id;
    }

    public String getTc_tipdoc() {
        return tc_tipdoc;
    }

    public void setTc_tipdoc(String tc_tipdoc) {
        this.tc_tipdoc = tc_tipdoc;
    }

    public String getTc_tipodocumento() {
        return tc_tipodocumento;
    }

    public void setTc_tipodocumento(String tc_tipodocumento) {
        this.tc_tipodocumento = tc_tipodocumento;
    }

    public String getTc_serie() {
        return tc_serie;
    }

    public void setTc_serie(String tc_serie) {
        this.tc_serie = tc_serie;
    }

    public String getTc_nrodoc() {
        return tc_nrodoc;
    }

    public void setTc_nrodoc(String tc_nrodoc) {
        this.tc_nrodoc = tc_nrodoc;
    }

    public String getTc_provid() {
        return tc_provid;
    }

    public void setTc_provid(String tc_provid) {
        this.tc_provid = tc_provid;
    }

    public String getTc_provrazsoc() {
        return tc_provrazsoc;
    }

    public void setTc_provrazsoc(String tc_provrazsoc) {
        this.tc_provrazsoc = tc_provrazsoc;
    }

    public Date getTc_fecemi() {
        return tc_fecemi;
    }

    public void setTc_fecemi(Date tc_fecemi) {
        this.tc_fecemi = tc_fecemi;
    }

    public String getTc_fecemision() {
        String fecha_cadena;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(tc_fecemi);
        return fecha_cadena;
    }

    public Date getTc_fecven() {
        return tc_fecven;
    }

    public void setTc_fecven(Date tc_fecven) {
        this.tc_fecven = tc_fecven;
    }

    public String getTc_fecvencimiento() {
        String fecha_cadena;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha_cadena = sdf.format(tc_fecven);
        return fecha_cadena;
    }

    public long getTc_nrpedkey() {
        return tc_nrpedkey;
    }

    public void setTc_nrpedkey(long tc_nrpedkey) {
        this.tc_nrpedkey = tc_nrpedkey;
    }

    public String getTc_ocompra() {
        tc_ocompra = Utilitarios.lpad(String.valueOf(tc_nrpedkey), 10, "0");
        return tc_ocompra;
    }

    public int getTc_moneda() {
        return tc_moneda;
    }

    public void setTc_moneda(int tc_moneda) {
        this.tc_moneda = tc_moneda;
    }

    public double getTc_tcambio() {
        return tc_tcambio;
    }

    public void setTc_tcambio(double tc_tcambio) {
        this.tc_tcambio = tc_tcambio;
    }

    public int getTc_conid() {
        return tc_conid;
    }

    public void setTc_conid(int tc_conid) {
        this.tc_conid = tc_conid;
    }

    public String getTc_condicionpago() {
        return tc_condicionpago;
    }

    public void setTc_condicionpago(String tc_condicionpago) {
        this.tc_condicionpago = tc_condicionpago;
    }

    public double getTc_dscgral() {
        return tc_dscgral;
    }

    public void setTc_dscgral(double tc_dscgral) {
        this.tc_dscgral = tc_dscgral;
    }

    public double getTc_dscfin() {
        return tc_dscfin;
    }

    public void setTc_dscfin(double tc_dscfin) {
        this.tc_dscfin = tc_dscfin;
    }

    /*public double getTc_tdsctos() {
     return tc_tdsctos;
     }

     public void setTc_tdsctos(double tc_tdsctos) {
     this.tc_tdsctos = tc_tdsctos;
     }*/
    public double getTc_ocvtotal() {
        return tc_ocvtotal;
    }

    public void setTc_ocvtotal(double tc_ocvtotal) {
        this.tc_ocvtotal = tc_ocvtotal;
    }

    public double getTc_valventa() {
        return tc_valventa;
    }

    public void setTc_valventa(double tc_valventa) {
        this.tc_valventa = tc_valventa;
    }

    public double getTc_vimpt() {
        return tc_vimpt;
    }

    public void setTc_vimpt(double tc_vimpt) {
        this.tc_vimpt = tc_vimpt;
    }

    public double getTc_vtotal() {
        return tc_vtotal;
    }

    public void setTc_vtotal(double tc_vtotal) {
        this.tc_vtotal = tc_vtotal;
    }

    public String getTc_svtotal() {
        tc_svtotal = df2.format(tc_vtotal);
        return tc_svtotal;
    }

    public void setTc_svtotal(String tc_svtotal) {
        this.tc_svtotal = tc_svtotal;
    }

    public String getTc_glosa() {
        return tc_glosa;
    }

    public void setTc_glosa(String tc_glosa) {
        this.tc_glosa = tc_glosa;
    }

    public int getTc_est() {
        return tc_est;
    }

    public void setTc_est(int tc_est) {
        this.tc_est = tc_est;
    }

    public String getTc_estado() {
        if (tc_est == 1) {
            tc_estado = "ACTIVO";
        } else {
            tc_estado = "ANULADO";
        }
        return tc_estado;
    }

    public boolean isValor() {
        if (tc_est == 1) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public int getTc_efectivo() {
        return tc_efectivo;
    }

    public void setTc_efectivo(int tc_efectivo) {
        this.tc_efectivo = tc_efectivo;
    }

    public boolean isValorefec() {
        if (tc_efectivo == 1) {
            valorefec = true;
        } else {
            valorefec = false;
        }
        return valorefec;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public void setValorefec(boolean valorefec) {
        this.valorefec = valorefec;
    }

    public String getTc_usuadd() {
        return tc_usuadd;
    }

    public void setTc_usuadd(String tc_usuadd) {
        this.tc_usuadd = tc_usuadd;
    }

    public Date getTc_fecadd() {
        return tc_fecadd;
    }

    public void setTc_fecadd(Date tc_fecadd) {
        this.tc_fecadd = tc_fecadd;
    }

    public String getTc_usumod() {
        return tc_usumod;
    }

    public void setTc_usumod(String tc_usumod) {
        this.tc_usumod = tc_usumod;
    }

    public Date getTc_fecmod() {
        return tc_fecmod;
    }

    public void setTc_fecmod(Date tc_fecmod) {
        this.tc_fecmod = tc_fecmod;
    }
}
