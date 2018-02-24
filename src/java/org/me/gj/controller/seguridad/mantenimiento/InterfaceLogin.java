package org.me.gj.controller.seguridad.mantenimiento;

import java.net.UnknownHostException;
import java.sql.SQLException;

public interface InterfaceLogin {
    //----Seccion Login-------//
    //Metodo se inicializa los combos

    public void CargaEmpresas() throws SQLException, UnknownHostException;

    public void CargaSucursales() throws SQLException;
    //Login Controller 

    public void IniciarSesion() throws SQLException,UnknownHostException;

    public void CerrarSesion() throws SQLException;

    public void SeleccionaEmpresa() throws SQLException, UnknownHostException;
    //Pregunta de seguridad

    public void ControlaPregunta() throws SQLException;

    public void ControlaRespuesta() throws SQLException, UnknownHostException;

    public void CerrarPregunta();
}
