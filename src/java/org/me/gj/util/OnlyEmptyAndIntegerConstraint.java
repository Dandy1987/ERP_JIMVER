/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

/**
 *
 * @author wcamacho
 */
public class OnlyEmptyAndIntegerConstraint implements Constraint, java.io.Serializable {

    private static final long serialVersionUID = 4052163775381888061L;
    private final String mensaje;

    public OnlyEmptyAndIntegerConstraint(String mensaje) {
        super();
        this.mensaje = mensaje;
    }

    public void validate(Component componente, Object o) {
        if (componente instanceof Textbox) {
            // No necesita para verificar ?
            if (((Textbox) componente).isDisabled()) {
                return;
            }
            // Obetenr el valor a cadena y quitar espacios vacios.
            final String enteredValue = ((String) o).trim();
            // Verificar si no esta vacia la cadena
            if (!enteredValue.isEmpty()) {
                // verificar si permite signos
                if (!enteredValue.matches("[0-9]+")) {
                    //throw new WrongValueException(componente, Labels.getLabel("message.error.OnlyNumbersOrEmptyAllowed"));
                    throw new WrongValueException(componente, mensaje);
                }
            }
        }        
    }

}
