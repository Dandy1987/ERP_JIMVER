/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.model.planillas.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author greyes
 */
public class ManPresPerDet {

    private int TPLPRESDET_NROCUOTA;
    private String EMP_ID;
    private String SUC_ID;
    private String TPLPRESCAB_ID;
    private Double TPLPRESDET_MONTCUOTA;
    private int TPLPRESDET_TOTCUOTAS;
    private Double TPLPRESDET_SALCUOTA;
    private int TPLPRESDET_IND;
    private Date TPLPRESDET_FECPAGO;
    private Double TPLPRESDET_MONTOTAL;
    private String TPLPRESDET_USUADD;
    private Date TPLPRESDET_FECADD;
    private String TPLPRESDET_USUMOD;
    private Date TPLPRESDET_FECMOD;
    private int TPLPRESDET_SITUCUOTA;
    //formato
    private String STPLPRESDET_FECPAGO;
    private String STPLPRESDET_SITUCUOTA;
    private String STPLPRESDET_IND;
    private String TPLINDACCION = "Q";
    //PARA LOV DE PRESTAMOS ENLACE
    private String TPLID_PERSONAL;
    private String f_pago;
    private boolean valSelec;
    private String situacion;
    private String monto;
    private int tipo;
    private String doc;
    private int sucursal;
    //NUEVOS PARAMETROS
    public ManPresPerDet() {

    }
    //LISTA GENERAR ACUERDO
    private int numero;

    public ManPresPerDet(Date TPLPRESDET_FECPAGO) {
        this.TPLPRESDET_FECPAGO = TPLPRESDET_FECPAGO;
    }

    public ManPresPerDet(int numero,/*String*/ Date fecpago, Double moncuota) {
        this.numero = numero;
        this.TPLPRESDET_NROCUOTA = numero;
        this.TPLPRESDET_FECPAGO = fecpago;
        //this.STPLPRESDET_FECPAGO = fecpago;
        this.TPLPRESDET_MONTCUOTA = moncuota;

    }

    public int getNumero() {
        return this.numero;
    }

//    public String getFecpago(){
//        return STPLPRESDET_FECPAGO ;
//    
//    }
    //
    public ManPresPerDet(String fecpago) {
        this.STPLPRESDET_FECPAGO = fecpago;
    }

    private Double moncuota;

    public ManPresPerDet(Double moncuota) {
        this.moncuota = moncuota;
        this.TPLPRESDET_MONTCUOTA = moncuota.doubleValue();
    }

    public String getSTPLPRESDET_SITUCUOTA() {
        if (TPLPRESDET_SITUCUOTA == 1) {
            STPLPRESDET_SITUCUOTA = "POR PAGAR";
        } else if (TPLPRESDET_SITUCUOTA == 0) {//se cambio 0 x 2  jr
            STPLPRESDET_SITUCUOTA = "CANCELADA";
        }
        return STPLPRESDET_SITUCUOTA;
    }

    public void setSTPLPRESDET_SITUCUOTA(String STPLPRESDET_SITUCUOTA) {
        this.STPLPRESDET_SITUCUOTA = STPLPRESDET_SITUCUOTA;
    }

    public String getSTPLPRESDET_IND() {
        if (TPLPRESDET_IND == 1) {
            STPLPRESDET_IND = "PENDIENTE";
        } else if (TPLPRESDET_IND == 0) {//se cambio 0 x 2  jr
            STPLPRESDET_IND = "ENLAZADO";
        }
        return STPLPRESDET_IND;
    }

    public void setSTPLPRESDET_IND(String STPLPRESDET_IND) {
        this.STPLPRESDET_IND = STPLPRESDET_IND;
    }

    public String getSTPLPRESDET_FECPAGO() {
        STPLPRESDET_FECPAGO = sdf.format(TPLPRESDET_FECPAGO);
        return STPLPRESDET_FECPAGO;//.toString()
        //return "Jean";
    }

    public void setSTPLPRESDET_FECPAGO(String STPLPRESDET_FECPAGO) {
        this.STPLPRESDET_FECPAGO = STPLPRESDET_FECPAGO;
    }

    //formato
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df2 = new DecimalFormat("#,###,##0.00", dfs);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public int getTPLPRESDET_NROCUOTA() {
        return TPLPRESDET_NROCUOTA;
    }

    public void setTPLPRESDET_NROCUOTA(int TPLPRESDET_NROCUOTA) {
        this.TPLPRESDET_NROCUOTA = TPLPRESDET_NROCUOTA;
    }

    public String getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(String EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getSUC_ID() {
        return SUC_ID;
    }

    public void setSUC_ID(String SUC_ID) {
        this.SUC_ID = SUC_ID;
    }

    public String getTPLPRESCAB_ID() {
        return TPLPRESCAB_ID;
    }

    public void setTPLPRESCAB_ID(String TPLPRESCAB_ID) {
        this.TPLPRESCAB_ID = TPLPRESCAB_ID;
    }

    public Double getTPLPRESDET_MONTCUOTA() {
        return TPLPRESDET_MONTCUOTA;
    }

    public void setTPLPRESDET_MONTCUOTA(Double TPLPRESDET_MONTCUOTA) {
        this.TPLPRESDET_MONTCUOTA = TPLPRESDET_MONTCUOTA;
    }

    public int getTPLPRESDET_TOTCUOTAS() {
        return TPLPRESDET_TOTCUOTAS;
    }

    public void setTPLPRESDET_TOTCUOTAS(int TPLPRESDET_TOTCUOTAS) {
        this.TPLPRESDET_TOTCUOTAS = TPLPRESDET_TOTCUOTAS;
    }

    public Double getTPLPRESDET_SALCUOTA() {
        return TPLPRESDET_SALCUOTA;
    }

    public void setTPLPRESDET_SALCUOTA(Double TPLPRESDET_SALCUOTA) {
        this.TPLPRESDET_SALCUOTA = TPLPRESDET_SALCUOTA;
    }

    public int getTPLPRESDET_IND() {
        return TPLPRESDET_IND;
    }

    public void setTPLPRESDET_IND(int TPLPRESDET_IND) {
        this.TPLPRESDET_IND = TPLPRESDET_IND;
    }

    public Date getTPLPRESDET_FECPAGO() {
        return TPLPRESDET_FECPAGO;
    }

    public void setTPLPRESDET_FECPAGO(Date TPLPRESDET_FECPAGO) {
        this.TPLPRESDET_FECPAGO = TPLPRESDET_FECPAGO;
    }

    public Double getTPLPRESDET_MONTOTAL() {
        return TPLPRESDET_MONTOTAL;
    }

    public void setTPLPRESDET_MONTOTAL(Double TPLPRESDET_MONTOTAL) {
        this.TPLPRESDET_MONTOTAL = TPLPRESDET_MONTOTAL;
    }

    public String getTPLPRESDET_USUADD() {
        return TPLPRESDET_USUADD;
    }

    public void setTPLPRESDET_USUADD(String TPLPRESDET_USUADD) {
        this.TPLPRESDET_USUADD = TPLPRESDET_USUADD;
    }

    public Date getTPLPRESDET_FECADD() {
        return TPLPRESDET_FECADD;
    }

    public void setTPLPRESDET_FECADD(Date TPLPRESDET_FECADD) {
        this.TPLPRESDET_FECADD = TPLPRESDET_FECADD;
    }

    public String getTPLPRESDET_USUMOD() {
        return TPLPRESDET_USUMOD;
    }

    public void setTPLPRESDET_USUMOD(String TPLPRESDET_USUMOD) {
        this.TPLPRESDET_USUMOD = TPLPRESDET_USUMOD;
    }

    public Date getTPLPRESDET_FECMOD() {
        return TPLPRESDET_FECMOD;
    }

    public void setTPLPRESDET_FECMOD(Date TPLPRESDET_FECMOD) {
        this.TPLPRESDET_FECMOD = TPLPRESDET_FECMOD;
    }

    public int getTPLPRESDET_SITUCUOTA() {
        return TPLPRESDET_SITUCUOTA;
    }

    public void setTPLPRESDET_SITUCUOTA(int TPLPRESDET_SITUCUOTA) {
        this.TPLPRESDET_SITUCUOTA = TPLPRESDET_SITUCUOTA;
    }

    public String getTPLINDACCION() {
        return TPLINDACCION;
    }

    public void setTPLINDACCION(String TPLINDACCION) {
        this.TPLINDACCION = TPLINDACCION;
    }

    public String getTPLID_PERSONAL() {
        return TPLID_PERSONAL;
    }

    public void setTPLID_PERSONAL(String TPLID_PERSONAL) {
        this.TPLID_PERSONAL = TPLID_PERSONAL;
    }

    public String getF_pago() {
        // f_pago = sdf.format(TPLPRESDET_FECADD);
        return f_pago;
    }

    public void setF_pago(String f_pago) {
        this.f_pago = f_pago;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getMonto() {
        monto = df2.format(TPLPRESDET_MONTCUOTA);
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }
    

}
