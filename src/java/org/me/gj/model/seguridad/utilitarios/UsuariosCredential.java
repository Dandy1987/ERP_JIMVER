package org.me.gj.model.seguridad.utilitarios;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import org.zkoss.zk.ui.Executions;

public class UsuariosCredential implements Serializable {

    private static final long serialVersionUID = 1L;
    private int codigo;
    private int codadm;
    private String cuenta;
    private String nombre;
    private String empresa;
    private String sucursal;
    private int codemp;
    private int codsuc;
    private int estado;
    private int situacion;
    private int rol;    
    private String computerName;
    Set<String> roles = new HashSet<String>();

    public UsuariosCredential(int codigo, String cuenta, String nombre, String empresa, String sucursal, int codemp, int codsuc, int rol, int estado, int situacion) {
        this.codigo = codigo;
        this.cuenta = cuenta;
        this.nombre = nombre;
        this.empresa = empresa;
        this.sucursal = sucursal;
        this.codemp = codemp;
        this.codsuc = codsuc;
        this.situacion = situacion;
        this.estado = estado;
        this.rol = rol;

    }

    public UsuariosCredential(int codadm, String cuenta, String nombre, int estado, int situacion, int rol) {
        this.codadm = codadm;
        this.cuenta = cuenta;
        this.nombre = nombre;
        this.situacion = situacion;
        this.estado = estado;
        this.rol = rol;
    }

    public UsuariosCredential() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodadm() {
        return codadm;
    }

    public void setCodadm(int codadm) {
        this.codadm = codadm;
    }

    public String getComputerName() {
        String ipName;
        //computerName = InetAddress.getLocalHost().getHostAddress();
        ipName = Executions.getCurrent().getRemoteHost();
        InetAddress cpu = null;
        try {
            cpu = InetAddress.getByName(ipName);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(UsuariosCredential.class.getName()).log(Level.SEVERE, null, ex);
        }
        computerName = cpu.getHostName();
        return computerName;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public int getCodemp() {
        return codemp;
    }

    public void setCodemp(int codemp) {
        this.codemp = codemp;
    }

    public int getCodsuc() {
        return codsuc;
    }

    public void setCodsuc(int codsuc) {
        this.codsuc = codsuc;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getSituacion() {
        return situacion;
    }

    public void setSituacion(int situacion) {
        this.situacion = situacion;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public void addRole(String role) {
        roles.add(role);
    }
}
