package org.me.gj.model.seguridad.utilitarios;

import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;

public class AutenticacionInicialPregunta implements Initiator {

    public void doInit(Page page, Map<String, Object> args) throws Exception {
        Session sess = Sessions.getCurrent();
        UsuariosPregunta usupreg = (UsuariosPregunta) sess.getAttribute("usuariosPregunta");
        if (usupreg != null) {
            sess.removeAttribute("usuariosPregunta");
            Executions.sendRedirect("/login.zul");
        }
        Executions.sendRedirect("/login.zul");
    }
}
