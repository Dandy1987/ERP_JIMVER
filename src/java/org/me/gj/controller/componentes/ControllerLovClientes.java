package org.me.gj.controller.componentes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.me.gj.controller.cxc.informes.ControllerConsultaxCliente;
import org.me.gj.controller.cxc.informes.DaoCtaCob;
import org.me.gj.controller.cxc.mantenimiento.DaoCliente;
import org.me.gj.controller.facturacion.procesos.ControllerGenPedVen;
import org.me.gj.controller.logistica.mantenimiento.DaoListaPrecios;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerAdmNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaCambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaES;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaIntercambio;
import org.me.gj.controller.logistica.procesos.ControllerGenNotaRecojo;
import org.me.gj.controller.logistica.procesos.ControllerProNotasCIR;
import org.me.gj.model.cxc.mantenimiento.CliFinanciero;
import org.me.gj.model.cxc.mantenimiento.CliTelefono;
import org.me.gj.model.cxc.mantenimiento.Cliente;
import org.me.gj.model.logistica.mantenimiento.ListaPrecio;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.Utilitarios;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ControllerLovClientes extends SelectorComposer<Component> {

    //Componentes Web
    @Wire
    Window w_lov_cliente;
    @Wire
    Listbox lst_buscli;
    @Wire
    Textbox txt_cli_id, txt_cli_razsoc, txt_dni, txt_ruc/*, txt_lincred, txt_limdoc, txt_saldo*/, txt_estado, txt_giro, txt_desgiro, txt_idvendedor, txt_nomvendedor,
            txt_diavis, txt_id_listapre, txt_des_listapre, txt_id_condven, txt_des_condven, txt_diaplazo;
    @Wire
    Textbox txt_busqueda, txt_cliid, txt_clinom, txt_horid, txt_nr_hordes, txt_zon_id, txt_zon_des, txt_nr_venid, txt_nr_vennom, txt_nr_transid,
            txt_nr_transdes, txt_clidir_direcc, txt_sup_id;
    @Wire
    Textbox txt_doc_id, txt_doc_des, txt_doc_des_des, txt_direcc, txt_ven_id, txt_ven_des, txt_hordes, txt_diavis_id, txt_diavis_des, txt_movil,
            txt_cate_id, txt_cate_des, /*txt_totlimdoc,*/ txt_ref;
    @Wire
    Longbox txt_clidir_id, txt_clidir_id2, /*txt_limcred,*/ txt_ruccxc;
    @Wire
    Combobox cb_situacion, cb_pronpag, cb_canal;
    @Wire
    Doublebox db_limcred, db_saldo, db_totlimcred, db_por_dsccli, txt_pdscto;
    @Wire
    Intbox txt_limdoc, txt_totlimdoc;
    @Wire
    Datebox d_fecnac;

    //Instancias de Objetos
    ListModelList<Cliente> objlstCliente = new ListModelList<Cliente>();
    DaoCliente objDaoClientes = new DaoCliente();
    DaoCtaCob objDaoCtaCob = new DaoCtaCob();
    Cliente objCliente = new Cliente();
    CliFinanciero objCliFinanciero = new CliFinanciero();
    CliTelefono objCliTelefono = new CliTelefono();

    //Variables publicas
    int emp_id, suc_id;
    String controlador;
    Map parametros;
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(ControllerLovClientes.class);

    //Eventos Primarios - Transaccionales
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        emp_id = objUsuCredential.getCodemp();
        suc_id = objUsuCredential.getCodsuc();
        parametros = new HashMap(Executions.getCurrent().getArg());
        controlador = (String) parametros.get("controlador");
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
            txt_zon_id = (Textbox) parametros.get("txt_zon_id");
            txt_zon_des = (Textbox) parametros.get("txt_zon_des");
            txt_nr_venid = (Textbox) parametros.get("txt_nr_venid");
            txt_nr_vennom = (Textbox) parametros.get("txt_nr_vennom");
            txt_horid = (Textbox) parametros.get("txt_nr_horid");
            txt_nr_hordes = (Textbox) parametros.get("txt_nr_hordes");
            txt_nr_transid = (Textbox) parametros.get("txt_nr_transid");
            txt_nr_transdes = (Textbox) parametros.get("txt_nr_transdes");
            txt_clidir_id = (Longbox) parametros.get("txt_clidir_id");
            txt_clidir_direcc = (Textbox) parametros.get("txt_clidir_direcc");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
            txt_zon_id = (Textbox) parametros.get("txt_zon_id");
            txt_zon_des = (Textbox) parametros.get("txt_zon_des");
            txt_nr_venid = (Textbox) parametros.get("txt_nr_venid");
            txt_nr_vennom = (Textbox) parametros.get("txt_nr_vennom");
            txt_horid = (Textbox) parametros.get("txt_nr_horid");
            txt_nr_hordes = (Textbox) parametros.get("txt_nr_hordes");
            txt_nr_transid = (Textbox) parametros.get("txt_nr_transid");
            txt_nr_transdes = (Textbox) parametros.get("txt_nr_transdes");
            txt_clidir_id = (Longbox) parametros.get("txt_clidir_id");
            txt_clidir_direcc = (Textbox) parametros.get("txt_clidir_direcc");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
            txt_zon_id = (Textbox) parametros.get("txt_zon_id");
            txt_zon_des = (Textbox) parametros.get("txt_zon_des");
            txt_nr_venid = (Textbox) parametros.get("txt_nr_venid");
            txt_nr_vennom = (Textbox) parametros.get("txt_nr_vennom");
            txt_horid = (Textbox) parametros.get("txt_nr_horid");
            txt_nr_hordes = (Textbox) parametros.get("txt_nr_hordes");
            txt_nr_transid = (Textbox) parametros.get("txt_nr_transid");
            txt_nr_transdes = (Textbox) parametros.get("txt_nr_transdes");
            txt_clidir_id = (Longbox) parametros.get("txt_clidir_id");
            txt_clidir_direcc = (Textbox) parametros.get("txt_clidir_direcc");
            txt_sup_id = (Textbox) parametros.get("txt_sup_id");
        } else if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
        } else if (parametros.get("validaBusqueda").equals("NotaCambioAuto")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
        } else if (parametros.get("validaBusqueda").equals("NotaRecojoAuto")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioAuto")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_cliid = (Textbox) parametros.get("txt_cliid");
            txt_clinom = (Textbox) parametros.get("txt_clinom");
            cb_situacion = (Combobox) parametros.get("cb_situacion");
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
            txt_cli_razsoc = (Textbox) parametros.get("txt_cli_razsoc");
            txt_dni = (Textbox) parametros.get("txt_dni");
            txt_ruc = (Textbox) parametros.get("txt_ruc");
            db_limcred = (Doublebox) parametros.get("db_limcred");
            txt_limdoc = (Intbox) parametros.get("txt_limdoc");
            db_saldo = (Doublebox) parametros.get("db_saldo");
            txt_estado = (Textbox) parametros.get("txt_estado");
            txt_clidir_id2 = (Longbox) parametros.get("txt_clidir_id");
            txt_clidir_direcc = (Textbox) parametros.get("txt_clidir_direcc");
            txt_zon_id = (Textbox) parametros.get("txt_zon_id");
            txt_zon_des = (Textbox) parametros.get("txt_zon_des");
            txt_giro = (Textbox) parametros.get("txt_giro");
            txt_desgiro = (Textbox) parametros.get("txt_desgiro");
            txt_idvendedor = (Textbox) parametros.get("txt_idvendedor");
            txt_nomvendedor = (Textbox) parametros.get("txt_nomvendedor");
            txt_diavis = (Textbox) parametros.get("txt_diavis");
            txt_id_listapre = (Textbox) parametros.get("txt_id_listapre");
            txt_des_listapre = (Textbox) parametros.get("txt_des_listapre");
            txt_id_condven = (Textbox) parametros.get("txt_id_condven");
            txt_des_condven = (Textbox) parametros.get("txt_des_condven");
            txt_diaplazo = (Textbox) parametros.get("txt_diaplazo");
            //cb_pronpag = (Combobox) parametros.get("cb_pronpag");
            //db_por_dsccli = (Doublebox) parametros.get("db_por_dsccli");
        } else if (parametros.get("validaBusqueda").equals("CxCCli")) {
            txt_cli_id = (Textbox) parametros.get("txt_cli_id");
            txt_cli_razsoc = (Textbox) parametros.get("txt_cli_razsoc");
            txt_doc_id = (Textbox) parametros.get("txt_doc_id");
            txt_doc_des = (Textbox) parametros.get("txt_doc_des");
            txt_doc_des_des = (Textbox) parametros.get("txt_doc_des_des");
            txt_direcc = (Textbox) parametros.get("txt_direcc");
            txt_ruccxc = (Longbox) parametros.get("txt_ruc");
            txt_ven_id = (Textbox) parametros.get("txt_ven_id");
            txt_ven_des = (Textbox) parametros.get("txt_ven_des");
            txt_zon_id = (Textbox) parametros.get("txt_zon_id");
            txt_zon_des = (Textbox) parametros.get("txt_zon_des");
            txt_horid = (Textbox) parametros.get("txt_horid");
            txt_hordes = (Textbox) parametros.get("txt_hordes");
            txt_diavis_id = (Textbox) parametros.get("txt_diavis_id");
            txt_diavis_des = (Textbox) parametros.get("txt_diavis_des");
            txt_movil = (Textbox) parametros.get("txt_movil");
            txt_giro = (Textbox) parametros.get("txt_giro");
            d_fecnac = (Datebox) parametros.get("d_fecnac");
            cb_canal = (Combobox) parametros.get("cb_canal");
            txt_ref = (Textbox) parametros.get("txt_ref");
            //Detalle
            db_limcred = (Doublebox) parametros.get("db_limcred");
            db_totlimcred = (Doublebox) parametros.get("db_totlimcred");
            txt_limdoc = (Intbox) parametros.get("txt_limdoc");
            txt_totlimdoc = (Intbox) parametros.get("txt_totlimdoc");
            txt_cate_id = (Textbox) parametros.get("txt_cate_id");
            txt_cate_des = (Textbox) parametros.get("txt_cate_des");
            txt_pdscto = (Doublebox) parametros.get("txt_pdscto");
        }
    }

    @Listen("onCreate=#w_lov_cliente")
    public void cargarClientes() throws SQLException {
        if (parametros.get("validaBusqueda").equals("CxCCli")) {
            objlstCliente = objDaoCtaCob.listasClientesCxC(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), 0);
        } else {
            objlstCliente = objDaoClientes.listaCliente(objUsuCredential.getCodemp(), objUsuCredential.getCodsuc(), 1);
        }
        lst_buscli.setModel(objlstCliente);
    }

    @Listen("onOK=#txt_busqueda")
    public void buscarCliente() throws SQLException {
        if (parametros.get("validaBusqueda").equals("CxCCli")) {
            objlstCliente = objDaoCtaCob.busquedaClientes(2, "%" + txt_busqueda.getValue().toUpperCase() + "%", 3);
        } else {
            objlstCliente = objDaoClientes.busquedaClientes(emp_id, suc_id, 2, "%" + txt_busqueda.getValue().toUpperCase() + "%", 1);
        }
        lst_buscli.setModel(objlstCliente);
    }

    @Listen("onSelect=#lst_buscli")
    public void seleccionaRegistro() throws SQLException {
        objCliente = lst_buscli.getSelectedItem().getValue();
        if (parametros.get("validaBusqueda").equals("NotaCambio")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
            txt_zon_id.setValue(objCliente.getZon_id());
            txt_zon_des.setValue(objCliente.getZon_des());
            txt_nr_venid.setValue(objCliente.getVen_id());
            txt_sup_id.setValue(objCliente.getSup_id());
            txt_nr_vennom.setValue(objCliente.getVen_apenom());
            if (objCliente.getHor_id() != null) {
                txt_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                txt_nr_hordes.setValue(objCliente.getHor_des());
                txt_nr_transid.setValue(objCliente.getTrans_id());
                txt_nr_transdes.setValue(objCliente.getTrans_alias());
                txt_clidir_id.setValue(objCliente.getClidir_id());
                txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                txt_clidir_id.focus();
            }
        } else if (parametros.get("validaBusqueda").equals("NotaRecojo")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
            txt_zon_id.setValue(objCliente.getZon_id());
            txt_zon_des.setValue(objCliente.getZon_des());
            txt_nr_venid.setValue(objCliente.getVen_id());
            txt_sup_id.setValue(objCliente.getSup_id());
            txt_nr_vennom.setValue(objCliente.getVen_apenom());
            if (objCliente.getHor_id() != null) {
                txt_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                txt_nr_hordes.setValue(objCliente.getHor_des());
                txt_nr_transid.setValue(objCliente.getTrans_id());
                txt_nr_transdes.setValue(objCliente.getTrans_alias());
                txt_clidir_id.setValue(objCliente.getClidir_id());
                txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                txt_clidir_id.focus();
            }
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambio")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
            txt_zon_id.setValue(objCliente.getZon_id());
            txt_zon_des.setValue(objCliente.getZon_des());
            txt_nr_venid.setValue(objCliente.getVen_id());
            txt_sup_id.setValue(objCliente.getSup_id());
            txt_nr_vennom.setValue(objCliente.getVen_apenom());
            if (objCliente.getHor_id() != null) {
                txt_horid.setValue(Utilitarios.lpad(String.valueOf(objCliente.getHor_id()), 3, "0"));
                txt_nr_hordes.setValue(objCliente.getHor_des());
                txt_nr_transid.setValue(objCliente.getTrans_id());
                txt_nr_transdes.setValue(objCliente.getTrans_alias());
                txt_clidir_id.setValue(objCliente.getClidir_id());
                txt_clidir_direcc.setValue(objCliente.getClidir_direc());
                txt_clidir_id.focus();
            }
        } else if (parametros.get("validaBusqueda").equals("mantGenNotES")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaCambioAuto")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaRecojoAuto")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
        } else if (parametros.get("validaBusqueda").equals("NotaIntercambioAuto")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
        } else if (parametros.get("validaBusqueda").equals("CIR")) {
            txt_cliid.setValue(objCliente.getCli_id());
            txt_clinom.setValue(objCliente.getCli_razsoc());
        } else if (parametros.get("validaBusqueda").equals("GenPedVen")) {
            objCliFinanciero = objDaoClientes.getClienteFin(objCliente.getCli_id(), emp_id, suc_id);
            txt_cli_id.setValue(objCliente.getCli_id());
            txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
            txt_dni.setValue(objCliente.getCli_dni());
            txt_ruc.setValue(String.valueOf(objCliente.getCli_ruc()));
            db_limcred.setValue(objCliFinanciero == null ? 0.0 : objCliFinanciero.getClifin_limcred());
            txt_limdoc.setValue(objCliFinanciero == null ? 0 : objCliFinanciero.getClifin_limdoc());
            /*Object obj[] = objDaoClientes.ValidaLimiteCredito(txt_cli_id.getValue(), Long.parseLong(txt_cli_id.getValue()),"001001");
             int saldo = Integer.parseInt(obj[0].toString());
             int credito = Integer.parseInt(String.valueOf(objCliente.getCli_credcor()));
             txt_saldo.setValue(String.valueOf(credito - saldo));*/
            db_saldo.setValue(0.0);
            txt_estado.setValue(objCliente.getCli_est() == 1 ? "ACTIVO" : "INACTIVO");
            txt_clidir_id2.setValue(Long.parseLong(String.valueOf(objCliente.getClidir_id())));
            txt_clidir_direcc.setValue(objCliente.getClidir_direc());
            txt_zon_id.setValue(objCliente.getZon_id());
            txt_zon_des.setValue(objCliente.getZon_des());
            txt_giro.setValue(objCliente.getCli_giro());
            txt_idvendedor.setValue(objCliente.getVen_id());
            txt_nomvendedor.setValue(objCliente.getVen_apenom());
            txt_diavis.setValue(objCliente.getZon_dvis_des());
            txt_id_listapre.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_lista()), 4, "0"));
            ListaPrecio objlista = new DaoListaPrecios().getListaPrecio(objCliente.getCli_lista(), 2);
            if (objlista != null) {
                txt_des_listapre.setValue(objlista.getLp_des());
            } else {
                txt_des_listapre.setValue("");
            }
            txt_id_condven.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_con()), 3, "0"));
            txt_des_condven.setValue(objCliente.getCli_descond());
            txt_diaplazo.setValue(String.valueOf(objCliente.getDiasplazo()));
            //cb_pronpag.setValue(objCliente.getCli_razsoc());
            //db_por_dsccli.setValue((objCliente.getCli_dscto()));

        } else if (parametros.get("validaBusqueda").equals("CxCCli")) {
            objCliTelefono = objDaoCtaCob.listaTelefonoCtaCob(objCliente.getCli_key());
            objCliFinanciero = objDaoCtaCob.listaFinancieroCtaCob(objCliente.getCli_key());
            //Cabecera  
            txt_cli_id.setValue(Utilitarios.lpad(objCliente.getCli_id(), 10, "0"));
            txt_cli_razsoc.setValue(objCliente.getCli_razsoc());
            txt_doc_id.setValue(Utilitarios.lpad(String.valueOf(objCliente.getCli_tipodoc()), 3, "0"));
            txt_doc_des.setValue(objCliente.getCli_tipodoc_des());
            txt_doc_des_des.setValue(objCliente.getCli_dni());
            txt_direcc.setValue(String.valueOf(objCliente.getClidir_direc()));
            txt_ruccxc.setValue(objCliente.getCli_ruc());
            txt_ven_id.setValue(objCliente.getVen_id());
            txt_ven_des.setValue(objCliente.getVen_apenom());
            txt_zon_id.setValue(objCliente.getZon_id());
            txt_zon_des.setValue(objCliente.getZon_des());
            txt_horid.setValue(objCliente.getHor_id());
            txt_hordes.setValue(objCliente.getHor_des());
            txt_diavis_id.setValue(String.valueOf(objCliente.getDia_vis()));
            txt_diavis_des.setValue(objCliente.getDia_vis_des());
            txt_giro.setValue(objCliente.getCli_giro());
            d_fecnac.setValue(objCliente.getCli_fecnac());
            cb_canal.setSelectedItem(Utilitarios.valorPorTexto1(cb_canal, objCliente.getCli_canal()));
            txt_pdscto.setValue(objCliente.getCli_dscto());
            txt_ref.setValue(objCliente.getCli_dirref());
            txt_movil.setValue(objCliTelefono == null ? "0"
                    : objCliTelefono.getClitel_telef1() > 0 ? String.valueOf(objCliTelefono.getClitel_telef1())
                    : objCliTelefono.getClitel_telef2() > 0 ? String.valueOf(objCliTelefono.getClitel_telef2())
                    : String.valueOf(objCliTelefono.getClitel_movil()));
            txt_cate_id.setValue(objCliFinanciero == null ? "" : Utilitarios.lpad(String.valueOf(objCliFinanciero.getClifin_categ()), 3, "0"));
            txt_cate_des.setValue(objCliFinanciero == null ? "" : objCliFinanciero.getClifin_categ_des());
            //Detalle
            db_limcred.setValue(objCliFinanciero == null ? 0.0 : objCliFinanciero.getClifin_limcred());
            db_totlimcred.setValue(objCliente.getCli_limcredcorp());
            txt_limdoc.setValue(objCliFinanciero == null ? 0 : objCliFinanciero.getClifin_limdoc());
            txt_totlimdoc.setValue(objCliente.getCli_limdoccorp());
            if (objCliente.getCli_est() != 1) {
                Messagebox.show("El cliente se encuentra 'inactivo' ", "ERP-JIMVER", Messagebox.OK, Messagebox.INFORMATION);
            }
        }

        if (controlador.equals("ControllerConsultaxCliente")) {
            ControllerConsultaxCliente.bandera = false;
        }
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambioAuto")) {
            ControllerAdmNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojoAuto")) {
            ControllerAdmNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambioAuto")) {
            ControllerAdmNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        w_lov_cliente.detach();
    }

    //Eventos Secundarios - Validacion
    @Listen("onClose=#w_lov_cliente")
    public void cerrarVentana() throws SQLException {
        if (controlador.equals("ControllerNotaES")) {
            ControllerGenNotaES.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambio")) {
            ControllerGenNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojo")) {
            ControllerGenNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambio")) {
            ControllerGenNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaCambioAuto")) {
            ControllerAdmNotaCambio.bandera = false;
        }
        if (controlador.equals("ControllerNotaRecojoAuto")) {
            ControllerAdmNotaRecojo.bandera = false;
        }
        if (controlador.equals("ControllerNotaIntercambioAuto")) {
            ControllerAdmNotaIntercambio.bandera = false;
        }
        if (controlador.equals("ControllerNotasCIR")) {
            ControllerProNotasCIR.bandera = false;
        }
        if (controlador.equals("ControllerGenPedVen")) {
            ControllerGenPedVen.bandera = false;
        }
        if (controlador.equals("ControllerConsultaxCliente")) {
            ControllerConsultaxCliente.bandera = false;
        }
    }
}
