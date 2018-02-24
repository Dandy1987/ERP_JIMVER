package org.me.gj.model.planillas.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author acaro
 */
public class PerPago {

    private int perPag_empId;
    private String perPag_id;
    private String perPag_desc;
    private Date perPag_fecIni;
    private Date perPag_fecFin;
    private String perPag_situ;
    private String perPag_usuAdd;
    private Date perPag_fecAdd;
    private String perPag_usuMod;
    private Date perPag_fecMod;
    private int perPag_estado;

    private boolean perPag_valor;
    private String s_perPag_fecIni;
    private String s_perPag_fecFin;
    private String s_cperPag_tipPan;

    private String tab_id;
    private String tab_descri;
    private String periodoProceso;
    private boolean valSelec;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public PerPago(int perPag_estado, String perPag_id, String perPag_desc, Date perPag_fecIni, Date perPag_fecFin) {
        this.perPag_estado = perPag_estado;
        this.perPag_id = perPag_id;
        this.perPag_desc = perPag_desc;
        this.perPag_fecIni = perPag_fecIni;
        this.perPag_fecFin = perPag_fecFin;
    }

    public PerPago(int perPag_empId, String perPag_id, String perPag_desc, Date perPag_fecIni, Date perPag_fecFin, String perPag_situ, String perPag_usuAdd, Date perPag_fecAdd, String perPag_usuMod, Date perPag_fecMod, int perPag_estado, String tab_id, String tab_descri) {
        this.perPag_empId = perPag_empId;
        this.perPag_id = perPag_id;
        this.perPag_desc = perPag_desc;
        this.perPag_fecIni = perPag_fecIni;
        this.perPag_fecFin = perPag_fecFin;
        this.perPag_situ = perPag_situ;
        this.perPag_usuAdd = perPag_usuAdd;
        this.perPag_fecAdd = perPag_fecAdd;
        this.perPag_usuMod = perPag_usuMod;
        this.perPag_fecMod = perPag_fecMod;
        this.perPag_estado = perPag_estado;
        this.tab_id = tab_id;
        this.tab_descri = tab_descri;
    }

    public PerPago(String tab_id, String tab_descri) {
        this.tab_id = tab_id;
        this.tab_descri = tab_descri;
    }

    public PerPago() {
    }

    public int getPerPag_empId() {
        return perPag_empId;
    }

    public void setPerPag_empId(int perPag_empId) {
        this.perPag_empId = perPag_empId;
    }

    public String getPerPag_id() {
        return perPag_id;
    }

    public void setPerPag_id(String perPag_id) {
        this.perPag_id = perPag_id;
    }

    public String getPerPag_desc() {
        return perPag_desc;
    }

    public void setPerPag_desc(String perPag_desc) {
        this.perPag_desc = perPag_desc;
    }

    public Date getPerPag_fecIni() {
        return perPag_fecIni;
    }

    public void setPerPag_fecIni(Date perPag_fecIni) {
        this.perPag_fecIni = perPag_fecIni;
    }

    public Date getPerPag_fecFin() {
        return perPag_fecFin;
    }

    public void setPerPag_fecFin(Date perPag_fecFin) {
        this.perPag_fecFin = perPag_fecFin;
    }

    public String getPerPag_situ() {
        return perPag_situ;
    }

    public void setPerPag_situ(String perPag_situ) {
        this.perPag_situ = perPag_situ;
    }

    public String getPerPag_usuAdd() {
        return perPag_usuAdd;
    }

    public void setPerPag_usuAdd(String perPag_usuAdd) {
        this.perPag_usuAdd = perPag_usuAdd;
    }

    public Date getPerPag_fecAdd() {
        return perPag_fecAdd;
    }

    public void setPerPag_fecAdd(Date perPag_fecAdd) {
        this.perPag_fecAdd = perPag_fecAdd;
    }

    public String getPerPag_usuMod() {
        return perPag_usuMod;
    }

    public void setPerPag_usuMod(String perPag_usuMod) {
        this.perPag_usuMod = perPag_usuMod;
    }

    public Date getPerPag_fecMod() {
        return perPag_fecMod;
    }

    public void setPerPag_fecMod(Date perPag_fecMod) {
        this.perPag_fecMod = perPag_fecMod;
    }

    public int getPerPag_estado() {
        return perPag_estado;
    }

    public void setPerPag_Estado(int perPag_estado) {
        this.perPag_estado = perPag_estado;
    }

    public boolean isPerPag_valor() {
        perPag_valor = (perPag_estado == 1);
        return perPag_valor;
    }

    public void setPerPag_valor(boolean perPag_valor) {
        this.perPag_valor = perPag_valor;
    }

    public String getS_perPag_fecIni() {
        s_perPag_fecIni = sdf.format(perPag_fecIni);
        return s_perPag_fecIni;
    }

    public void setS_perPag_fecIni(String s_perPag_fecIni) {
        this.s_perPag_fecIni = s_perPag_fecIni;
    }

    public String getS_perPag_fecFin() {
        s_perPag_fecFin = sdf.format(perPag_fecFin);
        return s_perPag_fecFin;
    }

    public void setS_perPag_fecFin(String s_perPag_fecFin) {
        this.s_perPag_fecFin = s_perPag_fecFin;
    }

    public String getS_cperPag_tipPan() {
        return s_cperPag_tipPan;
    }

    public void setS_cperPag_tipPan(String s_cperPag_tipPan) {
        this.s_cperPag_tipPan = s_cperPag_tipPan;
    }

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public String getTab_descri() {
        return tab_descri;
    }

    public void setTab_descri(String tab_descri) {
        this.tab_descri = tab_descri;
    }

    public String getPeriodoProceso() {
        return periodoProceso;
    }

    public void setPeriodoProceso(String periodoProceso) {
        this.periodoProceso = periodoProceso;
    }

    public boolean isValSelec() {
        return valSelec;
    }

    public void setValSelec(boolean valSelec) {
        this.valSelec = valSelec;
    }

}
