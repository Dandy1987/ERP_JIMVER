
package org.me.gj.controller.seguridad.mantenimiento;
import java.sql.SQLException;
import org.zkoss.zk.ui.event.InputEvent;

public interface InterfaceControllers {
    //Validaciones
    public void llenarCampos();
    public void seleccionaTab(boolean b_valida1, boolean b_valida2);
    public void habilitaTab(boolean b_valida1, boolean b_valida2);
    public void habilitaBotones(boolean b_valida1, boolean b_valida2);
    public void limpiarCampos();
    public void habilitaCampos(boolean b_valida1);
    public String verificar();
    //Botones
    public void botonNuevo() throws SQLException;
    public void botonDeshacer();
    public void botonEditar() throws SQLException;
    //Transacciones
    public void botonGuardar() throws SQLException;
    public void botonEliminar() throws SQLException;
    public void seleccionaRegistro() throws SQLException;
    public void busquedaRegistros() throws SQLException;
    public void llenaRegistros()throws SQLException;
    public void escogerOpcion(); 
    //Evento
    public void validaBusqueda(InputEvent event) throws SQLException;
}
