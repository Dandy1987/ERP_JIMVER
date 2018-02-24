package org.me.gj.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

/**
 *
 * @author acaro
 */
public class OnlyEmptyAndStringConstraint implements Constraint, java.io.Serializable {

    private static final long serialVersionUID = 4052163775381888061L;
    private final String mensaje;

    public OnlyEmptyAndStringConstraint(String mensaje) {
        super();
        this.mensaje = mensaje;
    }

    public void validate(Component componente, Object o) {
        if (componente instanceof Textbox) {
            // No necesita para verificar ?
            if (((Textbox) componente).isDisabled()) {
                return;
            }
            // Obtener el valor a cadena.
            final String s_entered_deciaml_Value = ((String) o).trim();
            // Verificar si no esta vacia la cadena
            if (!s_entered_deciaml_Value.isEmpty()) {
                // verificar si permite letras
                if (s_entered_deciaml_Value.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\\\s]+")) {
                    throw new WrongValueException(componente, mensaje);
                }
            }
        }
    }
}
