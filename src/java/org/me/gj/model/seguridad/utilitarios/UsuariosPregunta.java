package org.me.gj.model.seguridad.utilitarios;

import java.io.Serializable;

public class UsuariosPregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pregunta;
    private String user;

    public UsuariosPregunta(String pregunta, String user) {
        this.pregunta = pregunta;
        this.user = user;
    }

    public UsuariosPregunta() {
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
