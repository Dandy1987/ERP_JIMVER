/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.gj.model.planillas.mantenimiento;

/**
 *
 * @author ROJAS
 */
public class PersPagoDep {
    
    private int pltipdoc;
    private String plnrodoc;
    private int emp_id;
    private int pltipdep;
    private int plcorrel_dep;
    private int plestado_dep;
    private int plbanco;
    private int pltipcta;
    private int plmoneda;
    private String plnrocta;
    private int pltippago;
    private String plglosa;
    
    //Utilitarios
    private String plbanco_des;
    private String indsql;

    public PersPagoDep() {
    }

    public PersPagoDep(int pltipdep, int plbanco, int pltipcta, int plcorrel_dep, int plmoneda, String plnrocta, int pltippago, String plglosa, String indsql) {
        this.pltipdep = pltipdep;
        this.plbanco = plbanco;
        this.pltipcta = pltipcta;
        this.plcorrel_dep = plcorrel_dep;
        this.plmoneda = plmoneda;
        this.plnrocta = plnrocta;
        this.pltippago = pltippago;
        this.plglosa = plglosa;
        this.indsql = indsql;
    }   
    
    public int getPltipdoc() {
        return pltipdoc;
    }

    public void setPltipdoc(int pltipdoc) {
        this.pltipdoc = pltipdoc;
    }

    public String getPlnrodoc() {
        return plnrodoc;
    }

    public void setPlnrodoc(String plnrodoc) {
        this.plnrodoc = plnrodoc;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getPltipdep() {
        return pltipdep;
    }

    public void setPltipdep(int pltipdep) {
        this.pltipdep = pltipdep;
    }

    public int getPlcorrel_dep() {
        return plcorrel_dep;
    }

    public void setPlcorrel_dep(int plcorrel_dep) {
        this.plcorrel_dep = plcorrel_dep;
    }

    public int getPlestado_dep() {
        return plestado_dep;
    }

    public void setPlestado_dep(int plestado_dep) {
        this.plestado_dep = plestado_dep;
    }

    public int getPlbanco() {
        return plbanco;
    }

    public void setPlbanco(int plbanco) {
        this.plbanco = plbanco;
    }

    public int getPltipcta() {
        return pltipcta;
    }

    public void setPltipcta(int pltipcta) {
        this.pltipcta = pltipcta;
    }

    public int getPlmoneda() {
        return plmoneda;
    }

    public void setPlmoneda(int plmoneda) {
        this.plmoneda = plmoneda;
    }

    public String getPlnrocta() {
        return plnrocta;
    }

    public void setPlnrocta(String plnrocta) {
        this.plnrocta = plnrocta;
    }

    public int getPltippago() {
        return pltippago;
    }

    public void setPltippago(int pltippago) {
        this.pltippago = pltippago;
    }

    public String getPlglosa() {
        return plglosa;
    }

    public void setPlglosa(String plglosa) {
        this.plglosa = plglosa;
    }

    public String getIndsql() {
        return indsql;
    }

    public void setIndsql(String indsql) {
        this.indsql = indsql;
    }

    public String getPlbanco_des() {
        return plbanco_des;
    }

    public void setPlbanco_des(String plbanco_des) {
        this.plbanco_des = plbanco_des;
    }

}
