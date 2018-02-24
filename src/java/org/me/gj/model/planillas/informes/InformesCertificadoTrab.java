package org.me.gj.model.planillas.informes;/*C*/

public class InformesCertificadoTrab {
    
    private String empnom;
    private String empruc;
    private String empsuc;
    private String feccert;
    private String empleado;
    private String tipdoc;
    private String nrodoc;
    private String fecini;
    private String fecfin;
    private String percargo;

    public InformesCertificadoTrab(String empnom, String empruc, String empsuc, String feccert, String empleado, String tipdoc, String nrodoc, String fecini, String fecfin, String percargo) {
        this.empnom = empnom;
        this.empruc = empruc;
        this.empsuc = empsuc;
        this.feccert = feccert;
        this.empleado = empleado;
        this.tipdoc = tipdoc;
        this.nrodoc = nrodoc;
        this.fecini = fecini;
        this.fecfin = fecfin;
        this.percargo = percargo;
    }

    public InformesCertificadoTrab() {
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

    public String getFeccert() {
        return feccert;
    }

    public void setFeccert(String feccert) {
        this.feccert = feccert;
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

    public String getFecini() {
        return fecini;
    }

    public void setFecini(String fecini) {
        this.fecini = fecini;
    }

    public String getFecfin() {
        return fecfin;
    }

    public void setFecfin(String fecfin) {
        this.fecfin = fecfin;
    }

    public String getPercargo() {
        return percargo;
    }

    public void setPercargo(String percargo) {
        this.percargo = percargo;
    }
    
    
  
}
