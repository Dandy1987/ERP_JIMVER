package org.me.gj.model.seguridad.utilitarios;

import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;

public class AutenticacionInicial implements Initiator {

    public void doInit(Page page, Map<String, Object> args) throws Exception {
        Session sess = Sessions.getCurrent();
        UsuariosCredential cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        if (cre == null) {
            Executions.sendRedirect("/login.zul");
            sess.setAttribute("usuariosCredential", cre);
        }

    }
}
