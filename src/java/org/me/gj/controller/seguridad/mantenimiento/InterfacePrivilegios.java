/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;

/**
 *
 * @author achocano
 */
public interface InterfacePrivilegios {
    //Armado de Menu Logistica
    public void Modulo_Logistica() throws SQLException;
    public void Modulo_CXC() throws SQLException;
    public void Modulo_Distribucion() throws SQLException;
    public void Modulo_Facturacion() throws SQLException;
    public void Modulo_CXP() throws SQLException;
    public void Modulo_Bancos() throws SQLException;
    public void Modulo_Caja() throws SQLException;
    public void Modulo_Contabilidad() throws SQLException;
    public void Modulo_Planillas() throws SQLException;
    public void Modulo_Seguridad() throws SQLException;
    public void Modulo_Finanzas() throws SQLException;
    public void Modulo_Estadisticas() throws SQLException;
}
