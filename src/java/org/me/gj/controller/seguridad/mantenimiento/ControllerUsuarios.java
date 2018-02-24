package org.me.gj.controller.seguridad.mantenimiento;

import java.io.File;

import java.io.IOException;
import java.sql.*;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.mantenimiento.*;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ControllerUsuarios extends SelectorComposer<Component> implements InterfaceControllers {

    //Componentes Web
    @Wire
    Tab tab_listausuarios, tab_mantenimiento;
    @Wire
    Toolbarbutton tbbtn_btn_nuevo, tbbtn_btn_editar, tbbtn_btn_eliminar, tbbtn_btn_guardar, tbbtn_btn_deshacer,
            tbbtn_btn_imprimir, tbbtn_btn_imagen;
    @Wire
    Listbox lst_usuarios;
    @Wire
    Combobox cb_rol, cb_pregunta, cb_area, cb_busqueda, cb_estado;
    @Wire
    Textbox txt_nombres, txt_apellidos, txt_login, txt_password, txt_imagen, txt_respuesta, txt_email, txt_busqueda, txt_dni;
    @Wire
    Intbox txt_id;
    @Wire
    Checkbox chk_estado, chk_situacion, chk_busest;
    @Wire
    Button btn_buscar;
    @Wire
    Div img_foto;
    //Instancias de Objetos
    ListModelList<Usuarios> objlstUsuarios = new ListModelList<Usuarios>();
    ListModelList<Roles> objlstRoles = new ListModelList<Roles>();
    ListModelList<Preguntas> objlstPreguntas = new ListModelList<Preguntas>();
    DaoUsuarios objDaoUsuarios = new DaoUsuarios();
    DaoPreguntas objDaoPreguntas = new DaoPreguntas();
    Usuarios objUsuario = new Usuarios();
    //Variables publicas
    String s_estado = "Q";
    String s_mensaje;
    Media media;
    File archivo;
    int flag = 0;
    int i_sel;
    boolean cargaImagen = false;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerUsuarios.class);

    //Eventos Primarios - Transaccionales
    @Listen("onCreate=#tabbox_usuarios")
    public void llenaRegistros() throws SQLException {
        objlstUsuarios = objDaoUsuarios.lstUsuarios();
        objlstRoles = objDaoUsuarios.lstRoles();
        objlstPreguntas = objDaoPreguntas.lstPreguntas(1);
        lst_usuarios.setModel(objlstUsuarios);
        cb_rol.setModel(objlstRoles);
        cb_pregunta.setModel(objlstPreguntas);
        cb_estado.setSelectedIndex(0);
        cb_busqueda.setSelectedIndex(0);
        txt_busqueda.setValue("%%");
        txt_busqueda.setDisabled(true);
    }

    @Listen("onClick=#btn_buscar")
    public void busquedaRegistros() throws SQLException {
        limpiarLista();
        String s_consulta = txt_busqueda.getValue().toUpperCase().trim();
        int i_seleccion = 0;
        int i_estado;
        if (cb_estado.getSelectedIndex() == 2) {
            i_estado = 3; // todos
        } else if (cb_estado.getSelectedIndex() == 0) {
            i_estado = 1;//activos
        } else {
            i_estado = 2;//inactivos
        }
        objlstUsuarios = new ListModelList<Usuarios>();
        if (cb_busqueda.getSelectedIndex() == 0) {
            i_seleccion = 0;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta());
        } else if (cb_busqueda.getSelectedIndex() == 1) {
            i_seleccion = 1;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado el codigo " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 2) {
            i_seleccion = 2;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 3) {
            i_seleccion = 3;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 4) {
            i_seleccion = 4;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        } else if (cb_busqueda.getSelectedIndex() == 5) {
            i_seleccion = 5;
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha ingresado la descripción " + s_consulta + " para su busqueda");
        }
        objlstUsuarios = objDaoUsuarios.busquedaUsuarios(i_seleccion, s_consulta, i_estado);
        if (objlstUsuarios.isEmpty()) {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno 0 registros");
        } else {
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha realizado una busqueda mediante el dato " + s_consulta + " que retorno " + objlstUsuarios.getSize() + " registro(s)");
        }
        if (objlstUsuarios.getSize() > 0) {
            lst_usuarios.setModel(objlstUsuarios);
        } else {
            objlstUsuarios = null;
            lst_usuarios.setModel(objlstUsuarios);
            Messagebox.show("No existe registros", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        }
        limpiarCampos();
        //limpiaAuditoria();
    }

    @Listen("onSelect=#cb_busqueda")
    public void seleccionabusqueda() throws SQLException {
        if (cb_busqueda.getSelectedIndex() > 0) {
            txt_busqueda.setDisabled(false);
        } else {
            txt_busqueda.setDisabled(true);
            txt_busqueda.setValue("%%");
        }
    }

    @Listen("onSelect=#lst_usuarios")
    public void seleccionaRegistro() throws SQLException {
        objUsuario = (Usuarios) lst_usuarios.getSelectedItem().getValue();
        if (objUsuario == null) {
            objUsuario = objlstUsuarios.get(i_sel + 1);
        }
        i_sel = lst_usuarios.getSelectedIndex();
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | selecciono el registro con el codigo " + objUsuario.getUsu_id());
        limpiarCampos();
        llenarCampos();
    }

    @Listen("onClick=#tbbtn_btn_nuevo")
    public void botonNuevo() {
        objUsuario = new Usuarios();
        limpiarCampos();
        habilitaBotones(true, false);
        habilitaTab(true, false);
        seleccionaTab(false, true);
        habilitaCampos(false);
        chk_estado.setDisabled(true);
        chk_situacion.setDisabled(true);
        s_estado = "N";
        txt_dni.setConstraint("/^[0-9]+$/: Por Favor Solo ingrese Números");
        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion nuevo para crear un registro");
    }

    @Listen("onClick=#tbbtn_btn_editar")
    public void botonEditar() throws SQLException {
        if (lst_usuarios.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_estado = "E";
            habilitaBotones(true, false);
            habilitaTab(true, false);
            seleccionaTab(false, true);
            habilitaCampos(false);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | pulso la opcion modificar para actualizar un registro");
        }
    }

    @Listen("onClick=#tbbtn_btn_guardar")
    public void botonGuardar() throws SQLException {
        String s_valida = verificar();
        String s_validadni = Verificadni();
        if (!s_valida.isEmpty()) {
            Messagebox.show("Falta datos en el campo " + s_valida, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (!s_validadni.isEmpty()) {
            Messagebox.show(s_validadni, "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else if (("N".equals(s_estado) || !txt_login.getValue().equalsIgnoreCase(objUsuario.getUsu_nick()))
                && objDaoUsuarios.validaNick(txt_login.getValue().toUpperCase().trim()) == 2) {
            Messagebox.show("El nick ingresado ya se encuentra en uso", "Error de Nick", Messagebox.OK, Messagebox.ERROR);
            txt_login.focus();
        } else {
            s_mensaje = "Está seguro que desea guardar los cambios?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                objlstUsuarios = new ListModelList<Usuarios>();
                                objUsuario = generaRegistro();
                                archivo = new File("C:\\USUARIOS\\" + objUsuario.getUsu_nick() + ".JPEG");
                                if (s_estado.equals("N")) {
                                    s_mensaje = objDaoUsuarios.insertarUsuario(objUsuario);
                                    Files.copy(archivo, media.getStreamData());
                                    Messagebox.show(s_mensaje);
                                    txt_dni.setConstraint("");
                                } else {
                                    if (cargaImagen == true) {
                                        if (archivo.exists()) {
                                            String s_men = "Esta imagen de usuario ya existe. ¿Desea Reemplazarla?";
                                            Messagebox.show(s_men, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                                                    Messagebox.QUESTION, new EventListener() {

                                                @Override
                                                public void onEvent(Event event) throws Exception {
                                                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                                        Files.copy(archivo, media.getStreamData());
                                                        s_mensaje = objDaoUsuarios.actualizarUsuario(objUsuario);
                                                        Messagebox.show(s_mensaje);
                                                    } else {
                                                        Messagebox.show(s_mensaje);
                                                    }
                                                }
                                            });
                                        } else {
                                            s_mensaje = objDaoUsuarios.actualizarUsuario(objUsuario);
                                            Messagebox.show(s_mensaje);
                                            Files.copy(archivo, media.getStreamData());
                                            txt_dni.setConstraint("");
                                        }
                                    } else {
                                        s_mensaje = objDaoUsuarios.actualizarUsuario(objUsuario);
                                        Messagebox.show(s_mensaje);
                                    }
                                    cargaImagen = false;
                                }
                                habilitaBotones(false, true);
                                habilitaTab(false, false);
                                seleccionaTab(true, false);
                                habilitaCampos(true);
                                limpiarCampos();
                                objlstUsuarios = objDaoUsuarios.lstUsuarios();
                                lst_usuarios.setModel(objlstUsuarios);
                                objUsuario = new Usuarios();
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_eliminar")
    public void botonEliminar() throws SQLException {
        if (lst_usuarios.getSelectedIndex() == -1) {
            Messagebox.show("Por favor seleccione un registro de la tabla", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            s_mensaje = "Está seguro que desea eliminar este usuario?";
            Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                                s_mensaje = objDaoUsuarios.eliminarUsuario(objUsuario);
                                objlstUsuarios.remove(lst_usuarios.getSelectedIndex());
                                lst_usuarios.setModel(objlstUsuarios);
                                Messagebox.show(s_mensaje);
                            }
                        }
                    });
        }
    }

    @Listen("onClick=#tbbtn_btn_deshacer")
    public void botonDeshacer() {
        txt_dni.setConstraint("");
        s_mensaje = "Está seguro que desea deshacer los cambios?";
        Messagebox.show(s_mensaje, "ERP-JIMVER", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                            limpiarCampos();
                            lst_usuarios.setSelectedIndex(-1);
                            habilitaTab(false, false);
                            seleccionaTab(true, false);
                            seleccionaTab(true, false);
                            habilitaBotones(false, true);
                            habilitaCampos(true);
                            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha deshecho los cambios");
                        }
                    }
                });
    }

    //Eventos Secundarios - Validacion
    @Listen("onChanging=#txt_busqueda")
    public void validaBusqueda(InputEvent event) throws SQLException {
        if (event.getValue().length() == 0) {
            objlstUsuarios = new ListModelList<Usuarios>();
            objlstUsuarios = objDaoUsuarios.lstUsuarios();
            lst_usuarios.setModel(objlstUsuarios);
        }
    }

    @Listen("onUpload=#tbbtn_btn_imagen")
    public void cargarImagen(UploadEvent event) throws Exception {
        if (txt_login.getText().isEmpty()) {
            Messagebox.show("Por favor verifique el campo login", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            media = event.getMedia();
            if (media instanceof org.zkoss.image.Image) {
                img_foto.getChildren().clear();
                Image foto_usuario = new Image();
                foto_usuario.setContent((org.zkoss.image.AImage) media);
                foto_usuario.setParent(img_foto);
                foto_usuario.setStyle("border: 2px solid #2E9AFE; width : 353px; height : 405px;  ");
                txt_imagen.setValue("C:\\USUARIOS\\" + txt_login.getValue().toUpperCase() + ".JPEG");
                cargaImagen = true;
            }
        }
    }

    //Eventos Otros 
    public void llenarCampos() {
        txt_nombres.setValue(objUsuario.getUsu_nom());
        txt_apellidos.setValue(objUsuario.getUsu_ape());
        txt_login.setValue(objUsuario.getUsu_nick());
        txt_password.setValue(objUsuario.getUsu_pass());
        txt_imagen.setValue(objUsuario.getUsu_imag());
        txt_respuesta.setValue(objUsuario.getUsu_rpta());
        txt_email.setValue(objUsuario.getUsu_email());
        txt_id.setValue(objUsuario.getUsu_id());
        txt_dni.setValue(objUsuario.getUsu_dni());
        if (objUsuario.getUsu_est() == 1) {
            chk_estado.setChecked(true);
            chk_estado.setLabel("Activo");
        } else {
            chk_estado.setChecked(false);
            chk_estado.setLabel("Inactivo");
        }
        if (objUsuario.getUsu_sit() == 1) {
            chk_situacion.setChecked(false);
            chk_situacion.setLabel("Desbloqueado");
        } else {
            chk_situacion.setChecked(true);
            chk_situacion.setLabel("Bloqueado");
        }
        cb_rol.setValue(objUsuario.getUsu_rol());
        cb_pregunta.setValue(objUsuario.getUsu_preg());
        cb_area.setValue(objUsuario.getUsu_area());
        Image foto_usuario = new Image();
        try {
            foto_usuario.setContent(new AImage("C:\\USUARIOS\\" + objUsuario.getUsu_nick() + ".JPEG"));
            foto_usuario.setParent(img_foto);
            foto_usuario.setStyle("border: 2px solid;");
        } catch (IOException ex) {
        }
    }

    public void seleccionaTab(boolean b_valida1, boolean b_valida2) {
        tab_listausuarios.setSelected(b_valida1);
        tab_mantenimiento.setSelected(b_valida2);
    }

    public void habilitaTab(boolean b_valida1, boolean b_valida2) {
        tab_listausuarios.setDisabled(b_valida1);
        tab_mantenimiento.setDisabled(b_valida2);
    }

    public void habilitaBotones(boolean b_valida1, boolean b_valida2) {
        tbbtn_btn_nuevo.setDisabled(b_valida1);
        tbbtn_btn_editar.setDisabled(b_valida1);
        tbbtn_btn_eliminar.setDisabled(b_valida1);
        //tbbtn_btn_imprimir.setDisabled(b_valida1);
        tbbtn_btn_deshacer.setDisabled(b_valida2);
        tbbtn_btn_guardar.setDisabled(b_valida2);
    }

    public void limpiarCampos() {
        txt_nombres.setValue("");
        txt_apellidos.setValue("");
        txt_login.setValue("");
        txt_password.setValue("");
        txt_imagen.setValue("");
        txt_respuesta.setValue("");
        txt_email.setValue("");
        txt_id.setValue(null);
        txt_dni.setValue("");
        cb_rol.setValue("");
        cb_pregunta.setValue("");
        cb_area.setValue("");
        chk_estado.setChecked(true);
        chk_estado.setLabel("Activo");
        chk_situacion.setChecked(false);
        chk_situacion.setLabel("Desbloqueado");
        img_foto.getChildren().clear();
    }

    public void limpiarLista() {
        //limpio mi lista
        objlstUsuarios = null;
        objlstUsuarios = new ListModelList<Usuarios>();
        lst_usuarios.setModel(objlstUsuarios);
    }

    public void habilitaCampos(boolean b_valida1) {
        txt_nombres.setDisabled(b_valida1);
        txt_apellidos.setDisabled(b_valida1);
        txt_login.setDisabled(b_valida1);
        txt_password.setDisabled(b_valida1);
        txt_imagen.setDisabled(b_valida1);
        txt_respuesta.setDisabled(b_valida1);
        txt_email.setDisabled(b_valida1);
        txt_dni.setDisabled(b_valida1);
        cb_rol.setDisabled(b_valida1);
        cb_pregunta.setDisabled(b_valida1);
        cb_area.setDisabled(b_valida1);
        chk_estado.setDisabled(b_valida1);
        chk_situacion.setDisabled(b_valida1);
        tbbtn_btn_imagen.setDisabled(b_valida1);
    }

    public String verificar() {
        String s_valor;
        if (txt_nombres.getValue().isEmpty()) {
            s_valor = "Nombres";
            txt_nombres.focus();
        } else if (txt_apellidos.getValue().isEmpty()) {
            s_valor = "Apellidos";
            txt_apellidos.focus();
        } else if (txt_login.getValue().isEmpty()) {
            s_valor = "Nick";
            txt_login.focus();
        } else if (txt_password.getValue().isEmpty()) {
            s_valor = "Password";
            txt_password.focus();
        } else if (txt_imagen.getValue().isEmpty()) {
            s_valor = "Imagen";
            txt_imagen.focus();
        } else if (txt_respuesta.getValue().isEmpty()) {
            s_valor = "Respuesta";
            txt_respuesta.focus();
        } else if (txt_email.getValue().isEmpty()) {
            s_valor = "Email";
            txt_email.focus();
        } else if (txt_dni.getValue().isEmpty()) {
            s_valor = "DNI";
            txt_dni.focus();
        } else if (cb_rol.getSelectedIndex() == -1) {
            s_valor = "Rol";
            cb_rol.focus();
        } else if (cb_pregunta.getSelectedIndex() == -1) {
            s_valor = "Pregunta";
            cb_pregunta.focus();
        } else if (cb_area.getSelectedIndex() == -1) {
            s_valor = "Area";
            cb_area.focus();
        } else {
            s_valor = "";
        }
        return s_valor;
    }

    public String Verificadni() {
        String s_valor_dni = "";
        // txt_dni.setConstraint("/^[0-9]+$/: Por Favor Solo ingrese Números");
        String s_dni = txt_dni.getValue();
        if (s_dni.length() < 8) {
            s_valor_dni = "Por favor ingrese un Dni de 8 numeros";
        }
        return s_valor_dni;
    }

    public Usuarios generaRegistro() throws IOException {
        String usu_nom = txt_nombres.getValue().toUpperCase();
        String usu_ape = txt_apellidos.getValue().toUpperCase();
        String usu_nick = txt_login.getValue().toUpperCase();
        String usu_pass = txt_password.getValue();
        String usu_imag = txt_imagen.getValue().toUpperCase();
        String usu_rpta = txt_respuesta.getValue().toUpperCase();
        String usu_email = txt_email.getValue().toUpperCase();
        int usu_id = 0;
        if (txt_id.getValue() != null) {
            usu_id = txt_id.getValue();
        }
        String usu_dni = txt_dni.getValue().toString();
        int usu_est;
        if (chk_estado.isChecked()) {
            usu_est = 1;
        } else {
            usu_est = 2;
        }
        int usu_sit;
        if (chk_situacion.isChecked()) {
            usu_sit = 0;
        } else {
            usu_sit = 1;
        }
        int usu_idRol = cb_rol.getSelectedItem().getValue();
        int usu_idPreg = cb_pregunta.getSelectedItem().getValue();
        String usu_area = cb_area.getValue().toUpperCase();
        String usu_usuadd = objUsuCredential.getCuenta();
        String usu_usumod = objUsuCredential.getCuenta();
        return new Usuarios(usu_id, usu_nom, usu_ape, usu_nick, usu_pass, usu_est, usu_sit, usu_email, usu_dni, usu_idRol,
                usu_imag, usu_area, usu_idPreg, usu_rpta, usu_usuadd, usu_usumod);
    }

    //metodos sin utilizar
    public void escogerOpcion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
