package org.me.gj.controller.seguridad.mantenimiento;

import java.net.InetAddress;
import java.sql.SQLException;
import org.me.gj.model.seguridad.mantenimiento.Empresas;
import org.me.gj.model.seguridad.mantenimiento.Sucursales;
import org.me.gj.model.seguridad.utilitarios.UsuariosLogueo;
import org.me.gj.model.seguridad.utilitarios.UsuariosPregunta;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import java.net.UnknownHostException;
import org.zkforge.bwcaptcha.Captcha;

public class ControllerLogin extends SelectorComposer<Component> implements InterfaceLogin {

    //Componentes Web
    @Wire
    Textbox txt_user, txt_pass, txt_adm, txt_passadm, txt_preg, txt_rpta, txt_captcha;
    @Wire
    Combobox cbx_emp, cbx_suc;
    @Wire
    Label lbl_tipologin;
    @Wire
    Button btn_login, btn_ok, btn_cancel;
    @Wire
    Captcha cap1;
    //Instancias de Objetos
    ListModelList<Empresas> lstempresas;
    ListModelList<Sucursales> lstsucursales;
    UsuariosCredential cre;
    UsuariosPregunta usupreg;
    //Variables publicas
    String s_pregunta;
    String s_respuesta;
    String s_user = "";
    String s_adm = "";
    String s_password = "";
    String s_passadm = "";
    String s_nomemp = "";
    String s_nomsuc = "";
    int s_codemp = 0;
    int s_codsuc = 0;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLogin.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#grd_login")
    public void CargaEmpresas() throws SQLException, UnknownHostException {
        //Carga Empresas solo para login de usuarios normales
        lstempresas = new ListModelList<Empresas>();
        lstempresas = new DaoEmpresas().lstEmpresasLogin(1);
        cbx_emp.setModel(lstempresas);
        String s_idemp = "11";
        lstsucursales = new ListModelList<Sucursales>();
        lstsucursales = new DaoSucursales().lstSucursalesLogin(s_idemp, 1);
        cbx_suc.setModel(lstsucursales);
        cbx_suc.setDisabled(false);
        cbx_suc.setValue("LIMA");
    }

    @Listen("onSelect=#cbx_emp")
    public void SeleccionaEmpresa() throws SQLException, UnknownHostException {
        //Limpio el combo 
        cbx_suc.setValue("");
        //Leo el codigo para enviar
        String s_idemp;
        s_idemp = cbx_emp.getSelectedItem().getValue().toString();
        lstsucursales = new ListModelList<Sucursales>();
        lstsucursales = new DaoSucursales().lstSucursalesLogin(s_idemp, 1);
        cbx_suc.setModel(lstsucursales);
        cbx_suc.setDisabled(false);
    }

    @Listen("onClick=#btn_login")
    public void IniciarSesion() throws SQLException, UnknownHostException {
        String computerName;
        boolean valor;
        computerName = InetAddress.getLocalHost().getHostName();
        UsuariosLogueo usulog/* = new UsuariosLogueo()*/;
        if (!"ADM".equals(lbl_tipologin.getValue())) {
            if (!"".equals(txt_user.getValue()) && !"".equals(txt_pass.getValue()) && cbx_emp.getSelectedIndex() != -1 && cbx_suc.getSelectedIndex() != -1) {
                s_user = txt_user.getValue();
                s_user = s_user.toUpperCase();
                s_password = txt_pass.getValue();
                s_codemp = cbx_emp.getSelectedItem().getValue();
                s_nomemp = cbx_emp.getValue();
                s_codsuc = cbx_suc.getSelectedItem().getValue();
                s_nomsuc = cbx_suc.getValue();
                usulog = new DaoLogin().Login(s_user, s_password);
                LOGGER.info("[" + computerName + "] | " + s_user + " | de la empresa: " + s_nomemp + " y sucursal: " + s_nomsuc + " intento iniciar sesión");
                //Preguntamos si el array tiene valores devueltos, si es asi entonces seguimos validando.
                if (usulog.getValido() == 1) {
                    //preguntamos si el usuario y la contraseña esta ok
                    if (usulog.getExiste() == 1 && usulog.getEstado() == 1 && usulog.getSituacion() == 1) {
                        Session sess = Sessions.getCurrent();
                        objUsuCredential = new UsuariosCredential(usulog.getCodigo(), s_user, usulog.getNombre(), s_nomemp, s_nomsuc, s_codemp, s_codsuc,
                                usulog.getRol(),
                                usulog.getEstado(),
                                usulog.getSituacion());
                        sess.setAttribute("usuariosCredential", objUsuCredential);
                        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | inicio sesión en la empresa: " + objUsuCredential.getEmpresa() + " y sucursal: " + objUsuCredential.getSucursal());
                        Executions.sendRedirect("/org/me/gj/view/planillas/Planillas.zul"); //Executions.sendRedirect("/Main.zul");
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() == 1 && usulog.getSituacion() != 1) {
                        LOGGER.error("[" + computerName + "] | " + s_user + " | se encuentra bloqueado ");
                        String s_mensaje = "Usuario Bloqueado, ¿Quiere desbloquear su Cuenta?";
                        Messagebox.show(s_mensaje, "Confirmación", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            Session sess = Sessions.getCurrent();
                                            s_pregunta = new DaoLogin().PreguntaUser(s_user);
                                            usupreg = new UsuariosPregunta(s_pregunta, s_user);
                                            sess.setAttribute("usuariosPregunta", usupreg);
                                            Executions.sendRedirect("/org/me/gj/view/seguridad/utilitarios/Pregunta.zul");
                                        }
                                    }
                                });
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() != 1 && usulog.getSituacion() == 1) {
                        Messagebox.show("Usuario Inactivo, comuniquese con su administrador de sistemas", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        LOGGER.error("[" + computerName + "] | " + s_user + " | se encuentra inactivo ");
                        txt_user.setFocus(true);
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() != 1 && usulog.getSituacion() != 1) {
                        String s_mensaje = "Usuario Bloqueado, ¿Quiere desbloquear su Cuenta?";
                        LOGGER.error("[" + computerName + "] | " + s_user + " | se encuentra bloqueado e Inactivo ");
                        Messagebox.show(s_mensaje, "Confirmación", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {
                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            Executions.sendRedirect("/org/me/gj/view/seguridad/utilitarios/Pregunta.zul");
                                        }
                                    }
                                });
                    } else if (usulog.getExiste() != 1 && usulog.getEstado() != 1 && usulog.getSituacion() != 1) {
                        LOGGER.error("[" + computerName + "] | " + s_user + " | contraseña incorrecta");
                        Messagebox.show("Contraseña Incorrecta", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        txt_user.setFocus(true);
                    }
                } else {
                    LOGGER.error("[" + computerName + "] | " + s_user + " | , usuario y contraseña incorrecta");
                    Messagebox.show("Usuario o Contraseña Incorrecta", "Información", Messagebox.OK, Messagebox.INFORMATION);
                    txt_user.setFocus(true);
                }
            } else if ("".equals(txt_user.getValue())) {
                showNotify("Falta Ingresar Usuario", txt_user);
                txt_user.focus();
            } else if ("".equals(txt_pass.getValue())) {
                showNotify("Falta Ingresar Password", txt_pass);
                txt_pass.focus();
            } else if (cbx_emp.getSelectedIndex() == -1) {
                showNotify("Falta Seleccionar Empresa", cbx_emp);
                cbx_emp.focus();
            } else if (cbx_suc.getSelectedIndex() == -1) {
                showNotify("Falta Seleccionar Sucursal", cbx_suc);
                cbx_suc.focus();
            }
        } else {
            valor = verificaCaptcha();
            if (!"".equals(txt_adm.getValue()) && !"".equals(txt_passadm.getValue()) && valor == true) {
                s_adm = txt_adm.getValue();
                s_adm = s_adm.toUpperCase();
                s_passadm = txt_passadm.getValue();
                usulog = new DaoLogin().Login(s_adm, s_passadm);
                LOGGER.info("[" + computerName + "] | " + s_adm + " | intento iniciar sesión de Administrador");
                //Preguntamos si el array tiene valores devueltos, si es asi entonces seguimos validando.
                if (usulog.getValido() == 1) {
                    //preguntamos si el usuario y la contraseña esta ok
                    if (usulog.getExiste() == 1 && usulog.getEstado() == 1 && usulog.getSituacion() == 1) {
                        if (usulog.getRol() == 1) {
                            Session sess = Sessions.getCurrent();

                            cre = new UsuariosCredential(usulog.getCodigo(), s_adm, usulog.getNombre(), s_nomemp, s_nomsuc, s_codemp, s_codsuc,
                                    usulog.getRol(),
                                    usulog.getEstado(),
                                    usulog.getSituacion());
                            sess.setAttribute("usuariosCredential", cre);
                            Executions.sendRedirect("/org/me/gj/view/seguridad/Seguridad.zul");
                            LOGGER.info("[" + cre.getComputerName() + "] | " + cre.getCuenta() + " | inicio sesión administrador");
                        } else {
                            Messagebox.show("Usted no es administrador", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        }
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() == 1 && usulog.getSituacion() != 1) {
                        //Messagebox.show("Usuario Bloqueado, comuniquese con su administrador de sistemas", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        String s_mensaje = "Administrador Bloqueado, ¿Quiere desbloquear su Cuenta?";
                        LOGGER.error("[" + computerName + "] | " + s_adm + " | se encuentra bloqueado el usuario administrador ");
                        Messagebox.show(s_mensaje, "Confirmación", Messagebox.OK | Messagebox.CANCEL,
                                Messagebox.QUESTION, new EventListener() {

                                    @Override
                                    public void onEvent(Event event) throws Exception {
                                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                            Session sess = Sessions.getCurrent();
                                            s_pregunta = new DaoLogin().PreguntaUser(s_adm);
                                            usupreg = new UsuariosPregunta(s_pregunta, s_adm);
                                            sess.setAttribute("usuariosPregunta", usupreg);
                                            Executions.sendRedirect("/org/me/gj/view/seguridad/utilitarios/Pregunta.zul");
                                        }
                                    }
                                });
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() != 1 && usulog.getSituacion() == 1) {
                        LOGGER.error("[" + computerName + "] | " + s_adm + " | se encuentra inactivo el usuario administrador ");
                        Messagebox.show("Usuario Inactivo, comuniquese con su administrador de sistemas", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        txt_adm.setFocus(true);
                    } else if (usulog.getExiste() == 1 && usulog.getEstado() != 1 && usulog.getSituacion() != 1) {
                        LOGGER.error("[" + computerName + "] | " + s_adm + " | se encuentra inactivo y bloqueado el usuario administrador ");
                        Messagebox.show("Usuario Bloqueado e Inactivo", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        txt_adm.setFocus(true);
                    } else if (usulog.getExiste() != 1 && usulog.getEstado() != 1 && usulog.getSituacion() != 1) {
                        LOGGER.error("[" + computerName + "] | " + s_adm + " | , usuario y contraseña incorrecta");
                        Messagebox.show("Contraseña Incorrecta", "Información", Messagebox.OK, Messagebox.INFORMATION);
                        txt_adm.setFocus(true);
                    }
                } else {
                    Messagebox.show("Usuario o Contraseña Incorrecta", "Información", Messagebox.OK, Messagebox.INFORMATION);
                    txt_adm.setFocus(true);
                }
            } else if ("".equals(txt_adm.getValue())) {
                showNotify("Falta Ingresar Usuario", txt_adm);
                txt_adm.focus();
            } else if ("".equals(txt_passadm.getValue())) {
                showNotify("Falta Ingresar Password", txt_passadm);
                txt_passadm.focus();
            }
        }
    }

    @Listen("onClick=#btn_logout")
    public void CerrarSesion() throws SQLException {
        Session sess = Sessions.getCurrent();
        objUsuCredential = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        System.out.println("Cerro Sesion:" + objUsuCredential.getNombre());
        sess.removeAttribute("usuariosCredential");
        Executions.sendRedirect("/Login.zul");
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | cerro sesión administrador");
    }

    @Listen("onCreate=#grd_pregunta")
    public void ControlaPregunta() throws SQLException {
        Session sess = Sessions.getCurrent();
        usupreg = (UsuariosPregunta) sess.getAttribute("usuariosPregunta");
        txt_preg.setValue(usupreg.getPregunta());
        System.out.println("Dato:" + usupreg.getPregunta());
    }

    @Listen("onClick=#btn_ok")
    public void ControlaRespuesta() throws SQLException, UnknownHostException {
        Session sess = Sessions.getCurrent();
        usupreg = (UsuariosPregunta) sess.getAttribute("usuariosPregunta");
        s_user = usupreg.getUser();
        s_respuesta = txt_rpta.getValue().trim();
        int i_resultado = new DaoLogin().RespuestaUser(s_user, s_respuesta);
        if (i_resultado == 1) {
            String s_mensaje = "Usuario Desbloqueado, ¿Quiere loguearse?";
            Messagebox.show(s_mensaje, "Confirmación", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                //Elimina la instancia de sesion
                                Session sess = Sessions.getCurrent();
                                sess.removeAttribute("usuariosPregunta");
                                //Redirige a login
                                Executions.sendRedirect("/Login.zul");
                            }
                        }
                    });
        } else {
            Messagebox.show("Respuesta Incorrecta", "Información", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Listen("onClick=#btn_cancel")
    public void CerrarPregunta() {
        Session sess = Sessions.getCurrent();
        sess.removeAttribute("usuariosPregunta");
        Executions.sendRedirect("/Login.zul");
    }

    //Eventos Secundarios - Validacion
    //Eventos Otros 
    private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "end_center", 2000);
    }

    public boolean verificaCaptcha() {
        boolean b_val = false;
        if (!cap1.getValue().equalsIgnoreCase(txt_captcha.getValue())) {
            showNotify("Codigo no valido", txt_captcha);
            //throw new WrongValueException(txt_captcha, "Codigo no valido!");
        } else {
            b_val = true;
        }
        return b_val;
    }

    //metodos sin utilizar
    public void CargaSucursales() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
