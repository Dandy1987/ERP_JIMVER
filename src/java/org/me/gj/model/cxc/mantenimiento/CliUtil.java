package org.me.gj.model.cxc.mantenimiento;

public class CliUtil {

    private String mensaje;
    private String c_cliente;
    private Long n_cliente;
    private int existe_cliente;

    public CliUtil(String mensaje, String c_cliente, Long n_cliente, int existe_cliente) {
        this.mensaje = mensaje;
        this.c_cliente = c_cliente;
        this.n_cliente = n_cliente;
        this.existe_cliente = existe_cliente;
    }

    public CliUtil() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getC_cliente() {
        return c_cliente;
    }

    public void setC_cliente(String c_cliente) {
        this.c_cliente = c_cliente;
    }

    public Long getN_cliente() {
        return n_cliente;
    }

    public void setN_cliente(Long n_cliente) {
        this.n_cliente = n_cliente;
    }

    public int getExiste_cliente() {
        return existe_cliente;
    }

    public void setExiste_cliente(int existe_cliente) {
        this.existe_cliente = existe_cliente;
    }

}
