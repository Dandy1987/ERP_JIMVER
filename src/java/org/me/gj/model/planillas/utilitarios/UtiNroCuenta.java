package org.me.gj.model.planillas.utilitarios;

/**
 *
 * @author CHUALLPA
 */
public class UtiNroCuenta {

    //Variables
    private int i_emp_id;
    private int i_ban_key;
    private String s_nrocta;
    private int i_estado;
    private String s_tipmon;
    private String s_ctacon;
    private String s_glosa;
    
    //Sobrecarga de constructores
    public UtiNroCuenta() {
    }

    public UtiNroCuenta(int i_emp_id, int i_ban_key, String s_nrocta, int i_estado, String s_tipmon, String s_ctacon, String s_glosa) {
        this.i_emp_id = i_emp_id;
        this.i_ban_key = i_ban_key;
        this.s_nrocta = s_nrocta;
        this.i_estado = i_estado;
        this.s_tipmon = s_tipmon;
        this.s_ctacon = s_ctacon;
        this.s_glosa = s_glosa;
    }
    
    //Metodos de Encapsulamiento
    public int getI_emp_id() {
        return i_emp_id;
    }

    public void setI_emp_id(int i_emp_id) {
        this.i_emp_id = i_emp_id;
    }

    public int getI_ban_key() {
        return i_ban_key;
    }

    public void setI_ban_key(int i_ban_key) {
        this.i_ban_key = i_ban_key;
    }

    public String getS_nrocta() {
        return s_nrocta;
    }

    public void setS_nrocta(String s_nrocta) {
        this.s_nrocta = s_nrocta;
    }

    public int getI_estado() {
        return i_estado;
    }

    public void setI_estado(int i_estado) {
        this.i_estado = i_estado;
    }

    public String getS_tipmon() {
        return s_tipmon;
    }

    public void setS_tipmon(String s_tipmon) {
        this.s_tipmon = s_tipmon;
    }

    public String getS_ctacon() {
        return s_ctacon;
    }

    public void setS_ctacon(String s_ctacon) {
        this.s_ctacon = s_ctacon;
    }

    public String getS_glosa() {
        return s_glosa;
    }

    public void setS_glosa(String s_glosa) {
        this.s_glosa = s_glosa;
    }
    
}
