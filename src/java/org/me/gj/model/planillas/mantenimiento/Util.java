package org.me.gj.model.planillas.mantenimiento;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author acaro
 */
public class Util {

    private int pUtil_emp_id;
    private String pUtil_id;
    private int pUtil_estado;
    private Double pUtil_parLeg;
    private Double pUtil_totRem;
    private Double pUtil_totDiasLab;
    private Double pUtil_facRem;
    private Double pUtil_facTpoLab;
    private String pUtil_usuAdd;
    private Date pUtil_fecAdd;
    private String pUtil_usuMod;
    private Date pUtil_fecMod;

    private String s_pUtil_parLeg;
    private String s_pUtil_totRem;
    private String s_pUtil_totDiasLab;
    private String s_pUtil_facRem;
    private String s_pUtil_facTpoLab;

    private boolean pUtil_valor;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    DecimalFormat dfTotales = new DecimalFormat("###,###,##0.00", dfs);
    DecimalFormat dfFactores = new DecimalFormat("0.00000000", dfs);

    public Util(int pUtil_estado,String pUtil_id, Double pUtil_parLeg, Double pUtil_totRem, Double pUtil_totDiasLab, Double pUtil_facRem, Double pUtil_facTpoLab) {
        this.pUtil_estado = pUtil_estado;
        this.pUtil_id = pUtil_id;
        this.pUtil_parLeg = pUtil_parLeg;
        this.pUtil_totRem = pUtil_totRem;
        this.pUtil_totDiasLab = pUtil_totDiasLab;
        this.pUtil_facRem = pUtil_facRem;
        this.pUtil_facTpoLab = pUtil_facTpoLab;
    }

    public Util() {
    }

    public int getpUtil_emp_id() {
        return pUtil_emp_id;
    }

    public void setpUtil_emp_id(int pUtil_emp_id) {
        this.pUtil_emp_id = pUtil_emp_id;
    }

    public String getpUtil_id() {
        return pUtil_id;
    }

    public void setpUtil_id(String pUtil_id) {
        this.pUtil_id = pUtil_id;
    }

    public int getpUtil_estado() {
        return pUtil_estado;
    }

    public void setpUtil_estado(int pUtil_estado) {
        this.pUtil_estado = pUtil_estado;
    }

    public Double getpUtil_parLeg() {
        return pUtil_parLeg;
    }

    public void setpUtil_parLeg(Double pUtil_parLeg) {
        this.pUtil_parLeg = pUtil_parLeg;
    }

    public Double getpUtil_totRem() {
        return pUtil_totRem;
    }

    public void setpUtil_totRem(Double pUtil_totRem) {
        this.pUtil_totRem = pUtil_totRem;
    }

    public Double getpUtil_totDiasLab() {
        return pUtil_totDiasLab;
    }

    public void setpUtil_totDiasLab(Double pUtil_totDiasLab) {
        this.pUtil_totDiasLab = pUtil_totDiasLab;
    }

    public Double getpUtil_facRem() {
        return pUtil_facRem;
    }

    public void setpUtil_facRem(Double pUtil_facRem) {
        this.pUtil_facRem = pUtil_facRem;
    }

    public Double getpUtil_facTpoLab() {
        return pUtil_facTpoLab;
    }

    public void setpUtil_facTpoLab(Double pUtil_facTpoLab) {
        this.pUtil_facTpoLab = pUtil_facTpoLab;
    }

    public String getpUtil_usuAdd() {
        return pUtil_usuAdd;
    }

    public void setpUtil_usuAdd(String pUtil_usuAdd) {
        this.pUtil_usuAdd = pUtil_usuAdd;
    }

    public Date getpUtil_fecAdd() {
        return pUtil_fecAdd;
    }

    public void setpUtil_fecAdd(Date pUtil_fecAdd) {
        this.pUtil_fecAdd = pUtil_fecAdd;
    }

    public String getpUtil_usuMod() {
        return pUtil_usuMod;
    }

    public void setpUtil_usuMod(String pUtil_usuMod) {
        this.pUtil_usuMod = pUtil_usuMod;
    }

    public Date getpUtil_fecMod() {
        return pUtil_fecMod;
    }

    public void setpUtil_fecMod(Date pUtil_fecMod) {
        this.pUtil_fecMod = pUtil_fecMod;
    }

    public boolean ispUtil_valor() {
        pUtil_valor = (pUtil_estado == 1);
        return pUtil_valor;
    }

    public void setpUtil_valor(boolean pUtil_valor) {
        this.pUtil_valor = pUtil_valor;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public String getS_pUtil_parLeg() {
        s_pUtil_parLeg = dfTotales.format(pUtil_parLeg);
        return s_pUtil_parLeg;
    }

    public void setS_pUtil_parLeg(String s_pUtil_parLeg) {
        this.s_pUtil_parLeg = s_pUtil_parLeg;
    }

    public String getS_pUtil_totRem() {
        s_pUtil_totRem = dfTotales.format(pUtil_totRem);
        return s_pUtil_totRem;
    }

    public void setS_pUtil_totRem(String s_pUtil_totRem) {
        this.s_pUtil_totRem = s_pUtil_totRem;
    }

    public String getS_pUtil_totDiasLab() {
        s_pUtil_totDiasLab = dfTotales.format(pUtil_totDiasLab);
        return s_pUtil_totDiasLab;
    }

    public void setS_pUtil_totDiasLab(String s_pUtil_totDiasLab) {
        this.s_pUtil_totDiasLab = s_pUtil_totDiasLab;
    }

    public String getS_pUtil_facRem() {
        s_pUtil_facRem = dfFactores.format(pUtil_facRem);
        return s_pUtil_facRem;
    }

    public void setS_pUtil_facRem(String s_pUtil_facRem) {
        this.s_pUtil_facRem = s_pUtil_facRem;
    }

    public String getS_pUtil_facTpoLab() {
        s_pUtil_facTpoLab = dfFactores.format(pUtil_facTpoLab);
        return s_pUtil_facTpoLab;
    }

    public void setS_pUtil_facTpoLab(String s_pUtil_facTpoLab) {
        this.s_pUtil_facTpoLab = s_pUtil_facTpoLab;
    }

}
