package org.me.gj.model.planillas.informes;/*C*/

public class InformesCartaBancos {
    
    private String empnom;
    private String empruc;
    private String empsuc;
    private String feccar;
    private String perbanco;
    private String empleado;
    private String tipdoc;
    private String nrodoc;
    private String fecces;
    private String nrocuenta;

    public InformesCartaBancos(String empnom, String empruc, String empsuc, String feccar, String perbanco, String empleado, String tipdoc, String nrodoc, String fecces, String nrocuenta) {
        this.empnom = empnom;
        this.empruc = empruc;
        this.empsuc = empsuc;
        this.feccar = feccar;
        this.perbanco = perbanco;
        this.empleado = empleado;
        this.tipdoc = tipdoc;
        this.nrodoc = nrodoc;
        this.fecces = fecces;
        this.nrocuenta = nrocuenta;
    }

    public InformesCartaBancos() {
    }

    public String getEmpnom() {
        return empnom;
    }

    public void setEmpnom(String empnom) {
        this.empnom = empnom;
    }

    public String getEmpruc() {
        return empruc;
    }

    public void setEmpruc(String empruc) {
        this.empruc = empruc;
    }

    public String getEmpsuc() {
        return empsuc;
    }

    public void setEmpsuc(String empsuc) {
        this.empsuc = empsuc;
    }

    public String getFeccar() {
        return feccar;
    }

    public void setFeccar(String feccar) {
        this.feccar = feccar;
    }

    public String getPerbanco() {
        return perbanco;
    }

    public void setPerbanco(String perbanco) {
        this.perbanco = perbanco;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getTipdoc() {
        return tipdoc;
    }

    public void setTipdoc(String tipdoc) {
        this.tipdoc = tipdoc;
    }

    public String getNrodoc() {
        return nrodoc;
    }

    public void setNrodoc(String nrodoc) {
        this.nrodoc = nrodoc;
    }

    public String getFecces() {
        return fecces;
    }

    public void setFecces(String fecces) {
        this.fecces = fecces;
    }

    public String getNrocuenta() {
        return nrocuenta;
    }

    public void setNrocuenta(String nrocuenta) {
        this.nrocuenta = nrocuenta;
    }
    
    
    
}