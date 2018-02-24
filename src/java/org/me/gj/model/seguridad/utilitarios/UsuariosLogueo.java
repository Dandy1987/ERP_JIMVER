package org.me.gj.model.seguridad.utilitarios;

public class UsuariosLogueo {

    private int codigo;
    private String nombre;
    private String mensaje;
    private int valido;
    private int ingreso;
    private int existe;
    private int rol;
    private int estado;
    private int situacion;
    private int intentos;

    public UsuariosLogueo(int codigo, String mensaje, String nombre, int valido, int ingreso, int existe, int rol, int estado, int situacion, int intentos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.valido = valido;
        this.ingreso = ingreso;
        this.existe = existe;
        this.rol = rol;
        this.estado = estado;
        this.situacion = situacion;
        this.intentos = intentos;
    }

    public UsuariosLogueo() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getValido() {
        return valido;
    }

    public void setValido(int valido) {
        this.valido = valido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getExiste() {
        return existe;
    }

    public void setExiste(int existe) {
        this.existe = existe;
    }

    public int getIngreso() {
        return ingreso;
    }

    public void setIngreso(int ingreso) {
        this.ingreso = ingreso;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getSituacion() {
        return situacion;
    }

    public void setSituacion(int situacion) {
        this.situacion = situacion;
    }

}
