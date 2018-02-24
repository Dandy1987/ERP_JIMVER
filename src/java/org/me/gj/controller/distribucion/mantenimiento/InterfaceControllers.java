
package org.me.gj.controller.distribucion.mantenimiento;

import java.sql.SQLException;
import java.text.ParseException;
import org.zkoss.zk.ui.event.InputEvent;

public interface InterfaceControllers {
    
        //Metodo se inicializa con el window
    public void llenaRegistros() throws SQLException;
    //Transacciones
    public void botonGuardar() throws SQLException;
    public void botonEliminar() throws SQLException;
    public void seleccionaRegistro() throws SQLException , ParseException;
    public void busquedaRegistros() throws SQLException;
    //Botones
    public void botonNuevo();
    public void botonDeshacer();
    public void botonEditar() throws SQLException, ParseException;
    //Evento
    public void OnChange();
    public void validaBusqueda(InputEvent event) throws SQLException;
    //Validaciones
    public void VerificarTransacciones() throws SQLException;
    public void llenarCampos() throws ParseException;
    public void seleccionaTab(boolean b_valida1, boolean b_valida2);
    public void habilitaTab(boolean b_valida1, boolean b_valida2);
    public void habilitaBotones(boolean b_valida1, boolean b_valida2);
    public void limpiarCampos();
    public void habilitaCampos(boolean b_valida1);
    public String verificar();
 
}
