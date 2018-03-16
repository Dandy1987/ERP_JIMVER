package org.me.gj.controller.seguridad.mantenimiento;

import java.sql.SQLException;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;

public class ControllerPrivilegios extends SelectorComposer<Component> implements InterfacePrivilegios {

    //General
    @Wire
    Image img_logistica;
    //modulos
    @Wire
    Menubar mod_logistica, mod_cxc, mod_distribucion, mod_facturacion, mod_contabilidad, mod_planillas;
    //menus
    @Wire
    Menu men_mantenimiento, men_procesos, men_informes, men_utilitarios, men_ayuda;
    //------------submenus1 y submenus2 de Logistica----------------------
    @Wire
    Menuitem smen1_articulos, smen1_proveedores, smen1_precios, smen1_lista_precios,
            smen1_condiciones, smen1_almacenes, smen1_ubicaciones, smen1_lineas,
            smen1_sublineas, smen1_guias, smen1_marcas, smen1_umanejo, smen1_motcam, smen1_undmed, smen1_notes,
            smen1_autord, smen1_ordrec, smen1_compras, smen1_pedcom, smen1_admped, smen1_pronot,
            smen1_infkar, smen1_infnotaes, smen1_infcos,
            smen1_ciedialog, smen1_ciemenlog, smen1_costos, smen1_infis, smen1_comprascon, smen1_comvsnotesfac,
            smen1_auditoria, smen1_manayu;
    @Wire
    Menuitem smen2_precompra, smen2_precventa, smen2_lisprecompra, smen2_lisprecventa,
            smen2_compra, smen2_venta, smen2_mantalmacenes, smen2_tipalmacenes,
            smen2_mantguias, smen2_detguias, smen2_relguias, smen2_tipmovguias,
            smen2_genordcompmerc, smen2_gennotcam, smen2_gennotrec, smen2_gennotint,
            smen2_autcam, smen2_autrec, smen2_autint,
            smen2_cosauto, smen2_cosman,
            smen2_ordcompxprov, smen2_ordcompxprod, smen2_ordcompxlin, smen2_ordcompxsublin,
            smen2_factprovxprov, smen2_factprovxprod, smen2_factprovxlin, smen2_factprovxsublin,
            smen2_notesxprov, smen2_notesxprod, smen2_notesxlin, smen2_notesxsublin,
            smen2_pedcompxprov, smen2_pedcompxprod, smen2_pedcompxlin, smen2_pedcompxsublin,
            smen2_regsto, smen2_regcos, smen2_stkbasico,smen2_stkpareto, smen2_notesvsfacxprov, smen2_notesvsfacxprod,
            smen2_notesvsfacxlin, smen2_notesvsfacxsublin,
            smen2_audnotaes, smen2_audnotacam,smen2_ingreainv,smen2_forinvfis;
    //--------------------------------*****-------------------------------
    //------------submenus1 y submenus2 de cxc----------------------
    @Wire
    Menuitem smen1_postal, smen1_ubigeo, smen1_moneda, smen1_giro,
            smen1_tipodoc, smen1_forpag, smen1_clientes,
            smen1_tipcamb, smen1_ciemencxc, smen1_ciediacxc, smen1_cxccli,
            smen1_consultaxcliente, smen1_consultaxdocumento, smen1_carteracliente;
    @Wire
    Menuitem smen2_categoriacliente, smen2_manclientes;
    //--------------------------------*****-------------------------------
    //------------submenus1 y submenus2 de distribucion----------------------
    @Wire
    Menuitem smen1_horario, smen1_empresa, smen1_propietario,smen1_progreparto,smen1_chofer,smen1_repartidor,smen1_progrutas;
    @Wire
    Menuitem smen2_vehiculo, smen2_categoria, smen2_color, smen2_combustible, smen2_marca,
            smen2_modelo, smen2_carroceria, smen2_transmision;

    //--------------------------------*****-------------------------------
    //------------submenus1 y submenus2 de facturacion----------------------
    @Wire
    Menuitem smen1_supervisor, smen1_aperdia, smen1_apeper, smen1_cierre, smen1_canal, smen1_mesa, smen1_vendedores, smen1_ruta,
            smen1_zona, smen1_progzon, smen1_motrec, smen1_tipoventa, smen1_pedven, smen1_procpedpend, smen1_notesven, smen1_numdoc,smen1_provpresu,
            smen1_anuladocven, smen1_regrepartidor, smen1_ciediafac, smen1_ciemenfac, smen1_guiasremision, smen1_consomercaderia, smen1_gendocventa;
    @Wire
    Menuitem smen2_anulamanual, smen2_anulaautomatica, smen2_anulaautofuerames, smen2_anularefacacredito, smen2_facturaboleta;

    //--------------------------------*****-------------------------------
    //------------submenus1 y submenus2 de contabilidad----------------------
    @Wire
    Menuitem smen1_apeanio, smen1_placon, smen1_tabsun, smen1_tipvou, smen1_tipdoc, smen1_cencos, smen1_tipcam;
    
    
    //--------------------------------*****-------------------------------
    //------------submenus1 y submenus2 y submenus3 de planillas----------------------
    @Wire
    Menuitem smen1_paramper, smen1_manper , smen1_contrato , smen1_presper;
    @Wire
    Menuitem smen2_perproc, smen2_util, smen2_confper, smen2_confcalc, smen2_bancos, smen2_afps, smen2_asist;
    @Wire
    Menuitem smen3_areas, smen3_cargos, smen3_datfor, smen3_tablas, smen3_conceptos, smen3_funciones, 
            smen3_creacion, smen3_config, smen3_horarios, smen3_feriado,
    
	//para ver utilitarios
	smen3_utienlConta, smen3_utienlSunat, smen3_utienlAfp, smen3_utienlBancos,
	smen3_utienlPlaElec, smen3_utienlCtsSem, smen3_utienlCarArc, smen3_utienlRegPlame;
    @Wire
    Menuitem smen3_uti1, smen3_uti2, smen3_uti3,
            smeni_bolPag, smeni_liqTra, smeni_plan, smeni_resRet, smeni_bolCts, smeni_bolPartic,
            smen_anaAfp, smen_pdrEmp, smen3_confplanilla, smen3_variable,
            smen_movimientos, smen_descuentos, smen_prestamos, smen_descanso,
            smen_cruce, smen_regAsis, smen_cer5ta, smen_cerTra, smen_crtCtsBanco, smen_contratos,
            smen_boleta, smen_legales, smen_uti2, smen_uti1, smen3_utienlComi,
            smeni_liqPla, smen_EliCal, smen_CalPLan, smen_ConVac, smen_AsisGeneral, smen_RegAsis, smen_Desc;
    @Wire
    Menuitem smen_movLin;

    //--------------------------------*****-------------------------------
    DaoAccesos objDaoAccesos = new DaoAccesos();
    UsuariosCredential cre;
    //Armado de Menu
    int usuario, empresa, sucursal = 0;

    @Listen("onCreate=#menu_logistica")
    public void Modulo_Logistica() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
        //mantenimiento en logistica
        //submenu1
        smen1_articulos.setDisabled(objDaoAccesos.Verifica_Acceso(10101000, usuario, empresa, sucursal));
        smen1_proveedores.setDisabled(objDaoAccesos.Verifica_Acceso(10102000, usuario, empresa, sucursal));
        smen1_ubicaciones.setDisabled(objDaoAccesos.Verifica_Acceso(10107000, usuario, empresa, sucursal));
        smen1_lineas.setDisabled(objDaoAccesos.Verifica_Acceso(10108000, usuario, empresa, sucursal));
        smen1_sublineas.setDisabled(objDaoAccesos.Verifica_Acceso(10109000, usuario, empresa, sucursal));
        smen1_marcas.setDisabled(objDaoAccesos.Verifica_Acceso(10111000, usuario, empresa, sucursal));
        smen1_umanejo.setDisabled(objDaoAccesos.Verifica_Acceso(10112000, usuario, empresa, sucursal));
        smen1_motcam.setDisabled(objDaoAccesos.Verifica_Acceso(10113000, usuario, empresa, sucursal));
        smen1_undmed.setDisabled(objDaoAccesos.Verifica_Acceso(10114000, usuario, empresa, sucursal));
        //submenu2*/
        smen2_precompra.setDisabled(objDaoAccesos.Verifica_Acceso(10103010, usuario, empresa, sucursal));
        smen2_precventa.setDisabled(objDaoAccesos.Verifica_Acceso(10103020, usuario, empresa, sucursal));
        smen2_lisprecompra.setDisabled(objDaoAccesos.Verifica_Acceso(10104010, usuario, empresa, sucursal));
        smen2_lisprecventa.setDisabled(objDaoAccesos.Verifica_Acceso(10104020, usuario, empresa, sucursal));
        smen2_compra.setDisabled(objDaoAccesos.Verifica_Acceso(10105010, usuario, empresa, sucursal));
        smen2_venta.setDisabled(objDaoAccesos.Verifica_Acceso(10105020, usuario, empresa, sucursal));
        smen2_mantalmacenes.setDisabled(objDaoAccesos.Verifica_Acceso(10106010, usuario, empresa, sucursal));
        smen2_tipalmacenes.setDisabled(objDaoAccesos.Verifica_Acceso(10106020, usuario, empresa, sucursal));
        smen2_mantguias.setDisabled(objDaoAccesos.Verifica_Acceso(10110010, usuario, empresa, sucursal));
        smen2_detguias.setDisabled(objDaoAccesos.Verifica_Acceso(10110020, usuario, empresa, sucursal));
        smen2_relguias.setDisabled(objDaoAccesos.Verifica_Acceso(10110030, usuario, empresa, sucursal));
        smen2_tipmovguias.setDisabled(objDaoAccesos.Verifica_Acceso(10110040, usuario, empresa, sucursal));

        //procesos en logistica
        smen1_pedcom.setDisabled(objDaoAccesos.Verifica_Acceso(10202000, usuario, empresa, sucursal));
        smen1_admped.setDisabled(objDaoAccesos.Verifica_Acceso(10210000, usuario, empresa, sucursal));
        smen1_notes.setDisabled(objDaoAccesos.Verifica_Acceso(10206000, usuario, empresa, sucursal));
        smen1_autord.setDisabled(objDaoAccesos.Verifica_Acceso(10204000, usuario, empresa, sucursal));
        smen1_ordrec.setDisabled(objDaoAccesos.Verifica_Acceso(10205000, usuario, empresa, sucursal));
        smen1_compras.setDisabled(objDaoAccesos.Verifica_Acceso(10207000, usuario, empresa, sucursal));
        smen1_comprascon.setDisabled(objDaoAccesos.Verifica_Acceso(10214000, usuario, empresa, sucursal));
        //smen1_infis.setDisabled(objDaoAccesos.Verifica_Acceso(10212000, usuario, empresa, sucursal));
        smen1_pronot.setDisabled(objDaoAccesos.Verifica_Acceso(10213000, usuario, empresa, sucursal));
        //submenu2*/
        smen2_cosauto.setDisabled(objDaoAccesos.Verifica_Acceso(10211010, usuario, empresa, sucursal));
        smen2_cosman.setDisabled(objDaoAccesos.Verifica_Acceso(10211010, usuario, empresa, sucursal));
        smen2_genordcompmerc.setDisabled(objDaoAccesos.Verifica_Acceso(10201010, usuario, empresa, sucursal));
        smen2_gennotcam.setDisabled(objDaoAccesos.Verifica_Acceso(10203010, usuario, empresa, sucursal));
        smen2_gennotrec.setDisabled(objDaoAccesos.Verifica_Acceso(10203020, usuario, empresa, sucursal));
        smen2_gennotint.setDisabled(objDaoAccesos.Verifica_Acceso(10203030, usuario, empresa, sucursal));
        smen2_autcam.setDisabled(objDaoAccesos.Verifica_Acceso(10208010, usuario, empresa, sucursal));
        smen2_autrec.setDisabled(objDaoAccesos.Verifica_Acceso(10208020, usuario, empresa, sucursal));
        smen2_autint.setDisabled(objDaoAccesos.Verifica_Acceso(10208030, usuario, empresa, sucursal));
        smen2_regsto.setDisabled(objDaoAccesos.Verifica_Acceso(10209010, usuario, empresa, sucursal));
        smen2_regcos.setDisabled(objDaoAccesos.Verifica_Acceso(10209020, usuario, empresa, sucursal));
        smen2_ingreainv.setDisabled(objDaoAccesos.Verifica_Acceso(10212010, usuario, empresa, sucursal));
        smen2_forinvfis.setDisabled(objDaoAccesos.Verifica_Acceso(10212020, usuario, empresa, sucursal));
        
        //informes en logistica
        smen1_infkar.setDisabled(objDaoAccesos.Verifica_Acceso(10307000, usuario, empresa, sucursal));
        smen1_infnotaes.setDisabled(objDaoAccesos.Verifica_Acceso(10308000, usuario, empresa, sucursal));
        smen1_infcos.setDisabled(objDaoAccesos.Verifica_Acceso(10309000, usuario, empresa, sucursal));
        //submenu2*/
        smen2_pedcompxprov.setDisabled(objDaoAccesos.Verifica_Acceso(10304010, usuario, empresa, sucursal));
        smen2_pedcompxprod.setDisabled(objDaoAccesos.Verifica_Acceso(10304020, usuario, empresa, sucursal));
        smen2_pedcompxlin.setDisabled(objDaoAccesos.Verifica_Acceso(10304030, usuario, empresa, sucursal));
        smen2_pedcompxsublin.setDisabled(objDaoAccesos.Verifica_Acceso(10304040, usuario, empresa, sucursal));
        smen2_ordcompxprov.setDisabled(objDaoAccesos.Verifica_Acceso(10301010, usuario, empresa, sucursal));
        smen2_ordcompxprod.setDisabled(objDaoAccesos.Verifica_Acceso(10301020, usuario, empresa, sucursal));
        smen2_ordcompxlin.setDisabled(objDaoAccesos.Verifica_Acceso(10301030, usuario, empresa, sucursal));
        smen2_ordcompxsublin.setDisabled(objDaoAccesos.Verifica_Acceso(10301040, usuario, empresa, sucursal));
        smen2_factprovxprov.setDisabled(objDaoAccesos.Verifica_Acceso(10302010, usuario, empresa, sucursal));
        smen2_factprovxprod.setDisabled(objDaoAccesos.Verifica_Acceso(10302020, usuario, empresa, sucursal));
        smen2_factprovxlin.setDisabled(objDaoAccesos.Verifica_Acceso(10302030, usuario, empresa, sucursal));
        smen2_factprovxsublin.setDisabled(objDaoAccesos.Verifica_Acceso(10302040, usuario, empresa, sucursal));
        smen2_notesxprov.setDisabled(objDaoAccesos.Verifica_Acceso(10303010, usuario, empresa, sucursal));
        smen2_notesxprod.setDisabled(objDaoAccesos.Verifica_Acceso(10303020, usuario, empresa, sucursal));
        smen2_notesxlin.setDisabled(objDaoAccesos.Verifica_Acceso(10303030, usuario, empresa, sucursal));
        smen2_notesxsublin.setDisabled(objDaoAccesos.Verifica_Acceso(10303040, usuario, empresa, sucursal));
        smen2_stkbasico.setDisabled(objDaoAccesos.Verifica_Acceso(10306010, usuario, empresa, sucursal));
        smen2_stkpareto.setDisabled(objDaoAccesos.Verifica_Acceso(10306020, usuario, empresa, sucursal));       
        smen2_notesvsfacxprov.setDisabled(objDaoAccesos.Verifica_Acceso(10305010, usuario, empresa, sucursal));
        smen2_notesvsfacxprod.setDisabled(objDaoAccesos.Verifica_Acceso(10305020, usuario, empresa, sucursal));
        smen2_notesvsfacxlin.setDisabled(objDaoAccesos.Verifica_Acceso(10305030, usuario, empresa, sucursal));
        smen2_notesvsfacxsublin.setDisabled(objDaoAccesos.Verifica_Acceso(10305040, usuario, empresa, sucursal));

        //utilitarios en logistica
        smen1_ciedialog.setDisabled(objDaoAccesos.Verifica_Acceso(10401000, usuario, empresa, sucursal));
        smen1_ciemenlog.setDisabled(objDaoAccesos.Verifica_Acceso(10402000, usuario, empresa, sucursal));
        
        smen2_audnotaes.setDisabled(objDaoAccesos.Verifica_Acceso(10403010, usuario, empresa, sucursal));
        smen2_audnotacam.setDisabled(objDaoAccesos.Verifica_Acceso(10403020, usuario, empresa, sucursal));
        
        //ayuda en logistica
        smen1_manayu.setDisabled(objDaoAccesos.Verifica_Acceso(10501000, usuario, empresa, sucursal)); 
        
        /**
         * * proceso de planilla
         */
    }

    @Listen("onCreate=#menu_cxc")
    public void Modulo_CXC() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
        //mantenimiento
        //submenu1
        smen1_forpag.setDisabled(objDaoAccesos.Verifica_Acceso(20102000, usuario, empresa, sucursal));
        smen1_giro.setDisabled(objDaoAccesos.Verifica_Acceso(20103000, usuario, empresa, sucursal));
        smen1_moneda.setDisabled(objDaoAccesos.Verifica_Acceso(20104000, usuario, empresa, sucursal));
        smen1_postal.setDisabled(objDaoAccesos.Verifica_Acceso(20105000, usuario, empresa, sucursal));
        smen1_tipcamb.setDisabled(objDaoAccesos.Verifica_Acceso(20106000, usuario, empresa, sucursal));
        smen1_tipodoc.setDisabled(objDaoAccesos.Verifica_Acceso(20107000, usuario, empresa, sucursal));
        smen1_ubigeo.setDisabled(objDaoAccesos.Verifica_Acceso(20108000, usuario, empresa, sucursal));
        smen1_ciediacxc.setDisabled(objDaoAccesos.Verifica_Acceso(20201000, usuario, empresa, sucursal));
        smen1_ciemencxc.setDisabled(objDaoAccesos.Verifica_Acceso(20202000, usuario, empresa, sucursal));
        //submenu2*/
        smen2_categoriacliente.setDisabled(objDaoAccesos.Verifica_Acceso(20101010, usuario, empresa, sucursal));
        smen2_manclientes.setDisabled(objDaoAccesos.Verifica_Acceso(20101020, usuario, empresa, sucursal));
        //informes
        //submenu1
        smen1_consultaxcliente.setDisabled(objDaoAccesos.Verifica_Acceso(20301000, usuario, empresa, sucursal));
        //smen1_consultaxdocumento.setDisabled(objDaoAccesos.Verifica_Acceso(20302000, usuario, empresa, sucursal));
        //smen1_carteracliente.setDisabled(objDaoAccesos.Verifica_Acceso(20303000, usuario, empresa, sucursal));
    }

    @Listen("onCreate=#menu_distribucion")
    public void Modulo_Distribucion() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
        //submenu1
        smen1_horario.setDisabled(objDaoAccesos.Verifica_Acceso(30101000, usuario, empresa, sucursal));
        smen1_empresa.setDisabled(objDaoAccesos.Verifica_Acceso(30102000, usuario, empresa, sucursal));
        smen1_propietario.setDisabled(objDaoAccesos.Verifica_Acceso(30103000, usuario, empresa, sucursal));
        smen1_chofer.setDisabled(objDaoAccesos.Verifica_Acceso(30106000, usuario, empresa, sucursal));
		smen1_repartidor.setDisabled(objDaoAccesos.Verifica_Acceso(30107000, usuario, empresa, sucursal));  
        smen1_progreparto.setDisabled(objDaoAccesos.Verifica_Acceso(30105000, usuario, empresa, sucursal));
        smen1_progrutas.setDisabled(objDaoAccesos.Verifica_Acceso(30108000, usuario, empresa, sucursal));
        //submenu2
        smen2_vehiculo.setDisabled(objDaoAccesos.Verifica_Acceso(30104010, usuario, empresa, sucursal));
        smen2_categoria.setDisabled(objDaoAccesos.Verifica_Acceso(30104020, usuario, empresa, sucursal));
        smen2_color.setDisabled(objDaoAccesos.Verifica_Acceso(30104030, usuario, empresa, sucursal));
        smen2_combustible.setDisabled(objDaoAccesos.Verifica_Acceso(30104040, usuario, empresa, sucursal));
        smen2_marca.setDisabled(objDaoAccesos.Verifica_Acceso(30104050, usuario, empresa, sucursal));
        smen2_modelo.setDisabled(objDaoAccesos.Verifica_Acceso(30104060, usuario, empresa, sucursal));
        smen2_carroceria.setDisabled(objDaoAccesos.Verifica_Acceso(30104070, usuario, empresa, sucursal));
        smen2_transmision.setDisabled(objDaoAccesos.Verifica_Acceso(30104080, usuario, empresa, sucursal));
    }
	
    @Listen("onCreate=#menu_facturacion")
    public void Modulo_Facturacion() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
        //submenu1
        smen1_canal.setDisabled(objDaoAccesos.Verifica_Acceso(40101000, usuario, empresa, sucursal));
        smen1_mesa.setDisabled(objDaoAccesos.Verifica_Acceso(40102000, usuario, empresa, sucursal));
        smen1_motrec.setDisabled(objDaoAccesos.Verifica_Acceso(40103000, usuario, empresa, sucursal));
        //smen1_progzon.setDisabled(objDaoAccesos.Verifica_Acceso(40104000, usuario, empresa, sucursal));
        smen1_ruta.setDisabled(objDaoAccesos.Verifica_Acceso(40105000, usuario, empresa, sucursal));
        smen1_supervisor.setDisabled(objDaoAccesos.Verifica_Acceso(40106000, usuario, empresa, sucursal));
        smen1_vendedores.setDisabled(objDaoAccesos.Verifica_Acceso(40107000, usuario, empresa, sucursal));
        smen1_zona.setDisabled(objDaoAccesos.Verifica_Acceso(40108000, usuario, empresa, sucursal));
        smen1_tipoventa.setDisabled(objDaoAccesos.Verifica_Acceso(40109000, usuario, empresa, sucursal));
        smen1_numdoc.setDisabled(objDaoAccesos.Verifica_Acceso(40110000, usuario, empresa, sucursal));
        smen1_provpresu.setDisabled(objDaoAccesos.Verifica_Acceso(40111000, usuario, empresa, sucursal));

        //submenu2
        //procesos en facturacion
        //submenu1
        smen1_pedven.setDisabled(objDaoAccesos.Verifica_Acceso(40201000, usuario, empresa, sucursal));
        smen1_notesven.setDisabled(objDaoAccesos.Verifica_Acceso(40202000, usuario, empresa, sucursal));
        smen1_procpedpend.setDisabled(objDaoAccesos.Verifica_Acceso(40205000, usuario, empresa, sucursal));
        smen1_gendocventa.setDisabled(objDaoAccesos.Verifica_Acceso(40204000, usuario, empresa, sucursal));

        smen2_anulamanual.setDisabled(objDaoAccesos.Verifica_Acceso(40203010, usuario, empresa, sucursal));
        smen2_anulaautomatica.setDisabled(objDaoAccesos.Verifica_Acceso(40203020, usuario, empresa, sucursal));
        smen2_anulaautofuerames.setDisabled(objDaoAccesos.Verifica_Acceso(40203030, usuario, empresa, sucursal));
        smen2_anularefacacredito.setDisabled(objDaoAccesos.Verifica_Acceso(40203040, usuario, empresa, sucursal));

        //informes en facturacion
        smen1_regrepartidor.setDisabled(objDaoAccesos.Verifica_Acceso(40402000, usuario, empresa, sucursal));
        smen1_guiasremision.setDisabled(objDaoAccesos.Verifica_Acceso(40403000, usuario, empresa, sucursal));
        smen1_consomercaderia.setDisabled(objDaoAccesos.Verifica_Acceso(40404000, usuario, empresa, sucursal));

        smen2_facturaboleta.setDisabled(objDaoAccesos.Verifica_Acceso(40401010, usuario, empresa, sucursal));

        //utilitarios en facturacion
        smen1_ciediafac.setDisabled(objDaoAccesos.Verifica_Acceso(40301000, usuario, empresa, sucursal));
        smen1_ciemenfac.setDisabled(objDaoAccesos.Verifica_Acceso(40302000, usuario, empresa, sucursal));
    }

    @Listen("onCreate=#menu_contabilidad")
    public void Modulo_Contabilidad() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
        //submenu1
        smen1_apeanio.setDisabled(objDaoAccesos.Verifica_Acceso(80101000, usuario, empresa, sucursal));
        smen1_tipdoc.setDisabled(objDaoAccesos.Verifica_Acceso(80104000, usuario, empresa, sucursal));
    }

    public void Modulo_CXP() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Modulo_Bancos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Modulo_Caja() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Listen("onCreate=#menu_planillas")
    public void Modulo_Planillas() throws SQLException {
        Session sess = Sessions.getCurrent();
        cre = (UsuariosCredential) sess.getAttribute("usuariosCredential");
        usuario = cre.getCodigo();
        empresa = cre.getCodemp();
        sucursal = cre.getCodsuc();
		
        smen1_manper.setDisabled(objDaoAccesos.Verifica_Acceso(90102000, usuario, empresa, sucursal));
        
        smen2_perproc.setDisabled(objDaoAccesos.Verifica_Acceso(90101010, usuario, empresa, sucursal));
        smen2_util.setDisabled(objDaoAccesos.Verifica_Acceso(90101020, usuario, empresa, sucursal));
        
        smen2_bancos.setDisabled(objDaoAccesos.Verifica_Acceso(90101090, usuario, empresa, sucursal));
		
        smen3_areas.setDisabled(objDaoAccesos.Verifica_Acceso(90101030, usuario, empresa, sucursal));
        smen3_cargos.setDisabled(objDaoAccesos.Verifica_Acceso(90101040, usuario, empresa, sucursal));
        smen3_datfor.setDisabled(objDaoAccesos.Verifica_Acceso(90101050, usuario, empresa, sucursal));
        smen3_tablas.setDisabled(objDaoAccesos.Verifica_Acceso(90101060, usuario, empresa, sucursal));
        smen3_conceptos.setDisabled(objDaoAccesos.Verifica_Acceso(90101070, usuario, empresa, sucursal));
        smen3_funciones.setDisabled(objDaoAccesos.Verifica_Acceso(90101080, usuario, empresa, sucursal));
        smen3_creacion.setDisabled(objDaoAccesos.Verifica_Acceso(90101100, usuario, empresa, sucursal));
        smen3_config.setDisabled(objDaoAccesos.Verifica_Acceso(90101110, usuario, empresa, sucursal));
        smen3_horarios.setDisabled(objDaoAccesos.Verifica_Acceso(90101120, usuario, empresa, sucursal));
		smen3_feriado.setDisabled(objDaoAccesos.Verifica_Acceso(90101150, usuario, empresa, sucursal));
        
        smen1_contrato.setDisabled(objDaoAccesos.Verifica_Acceso(90103000, usuario, empresa, sucursal));
        smen1_presper.setDisabled(objDaoAccesos.Verifica_Acceso(90104000, usuario, empresa, sucursal));
        
        smen_movLin.setDisabled(objDaoAccesos.Verifica_Acceso(90201000, usuario, empresa, sucursal));
		//03/02/2018
        smen3_confplanilla.setDisabled(objDaoAccesos.Verifica_Acceso(90101140, usuario, empresa, sucursal));
        smen3_variable.setDisabled(objDaoAccesos.Verifica_Acceso(90101130, usuario, empresa, sucursal));
        smen_movimientos.setDisabled(objDaoAccesos.Verifica_Acceso(90301000, usuario, empresa, sucursal));
        smen_descuentos.setDisabled(objDaoAccesos.Verifica_Acceso(90302000, usuario, empresa, sucursal));
        smen_prestamos.setDisabled(objDaoAccesos.Verifica_Acceso(90303000, usuario, empresa, sucursal));
        smen_descanso.setDisabled(objDaoAccesos.Verifica_Acceso(90304000, usuario, empresa, sucursal));
        smen_cruce.setDisabled(objDaoAccesos.Verifica_Acceso(90206000, usuario, empresa, sucursal));
        smen_regAsis.setDisabled(objDaoAccesos.Verifica_Acceso(90306000, usuario, empresa, sucursal));//
        smen_cer5ta.setDisabled(objDaoAccesos.Verifica_Acceso(90307000, usuario, empresa, sucursal));
        smen_cerTra.setDisabled(objDaoAccesos.Verifica_Acceso(90308000, usuario, empresa, sucursal));
        smen_crtCtsBanco.setDisabled(objDaoAccesos.Verifica_Acceso(90309000, usuario, empresa, sucursal));
        smen_contratos.setDisabled(objDaoAccesos.Verifica_Acceso(90310000, usuario, empresa, sucursal));
       // smen_legales.setDisabled(objDaoAccesos.Verifica_Acceso(90311000, usuario, empresa, sucursal));
        smen_boleta.setDisabled(objDaoAccesos.Verifica_Acceso(90311010, usuario, empresa, sucursal));
        smeni_liqTra.setDisabled(objDaoAccesos.Verifica_Acceso(90311020, usuario, empresa, sucursal));
        smen_anaAfp.setDisabled(objDaoAccesos.Verifica_Acceso(90312000, usuario, empresa, sucursal));
        smen_pdrEmp.setDisabled(objDaoAccesos.Verifica_Acceso(90313000, usuario, empresa, sucursal));
        //utilitarios
        //smen_uti1.setDisabled(objDaoAccesos.Verifica_Acceso(90401000, usuario, empresa, sucursal));
        smen3_utienlConta.setDisabled(objDaoAccesos.Verifica_Acceso(90401010, usuario, empresa, sucursal));
        smen3_utienlComi.setDisabled(objDaoAccesos.Verifica_Acceso(90401020, usuario, empresa, sucursal));
        smen3_utienlSunat.setDisabled(objDaoAccesos.Verifica_Acceso(90401030, usuario, empresa, sucursal));
        smen3_utienlAfp.setDisabled(objDaoAccesos.Verifica_Acceso(90401040, usuario, empresa, sucursal));
        smen3_utienlBancos.setDisabled(objDaoAccesos.Verifica_Acceso(90401050, usuario, empresa, sucursal));
        smen3_utienlPlaElec.setDisabled(objDaoAccesos.Verifica_Acceso(90401060, usuario, empresa, sucursal));
        smen3_utienlCtsSem.setDisabled(objDaoAccesos.Verifica_Acceso(90401070, usuario, empresa, sucursal));
        smen3_utienlCarArc.setDisabled(objDaoAccesos.Verifica_Acceso(90401080, usuario, empresa, sucursal));
        smen3_utienlRegPlame.setDisabled(objDaoAccesos.Verifica_Acceso(90401090, usuario, empresa, sucursal));
      //  smen_uti2.setDisabled(objDaoAccesos.Verifica_Acceso(90402000, usuario, empresa, sucursal));
        smen3_uti1.setDisabled(objDaoAccesos.Verifica_Acceso(90402010, usuario, empresa, sucursal));
        smen3_uti2.setDisabled(objDaoAccesos.Verifica_Acceso(90402020, usuario, empresa, sucursal));
        smen3_uti3.setDisabled(objDaoAccesos.Verifica_Acceso(90313000, usuario, empresa, sucursal));

        smeni_bolPartic.setDisabled(objDaoAccesos.Verifica_Acceso(90311060, usuario, empresa, sucursal));
        smeni_bolCts.setDisabled(objDaoAccesos.Verifica_Acceso(90311050, usuario, empresa, sucursal));
        smeni_resRet.setDisabled(objDaoAccesos.Verifica_Acceso(90311040, usuario, empresa, sucursal));
        smeni_liqPla.setDisabled(objDaoAccesos.Verifica_Acceso(90311030, usuario, empresa, sucursal));
        smen_EliCal.setDisabled(objDaoAccesos.Verifica_Acceso(90207000, usuario, empresa, sucursal));
        smen_CalPLan.setDisabled(objDaoAccesos.Verifica_Acceso(90206000, usuario, empresa, sucursal));
        smen_ConVac.setDisabled(objDaoAccesos.Verifica_Acceso(90205000, usuario, empresa, sucursal));
        smen_AsisGeneral.setDisabled(objDaoAccesos.Verifica_Acceso(90204000, usuario, empresa, sucursal));
        smen_RegAsis.setDisabled(objDaoAccesos.Verifica_Acceso(90203000, usuario, empresa, sucursal));
        smen_Desc.setDisabled(objDaoAccesos.Verifica_Acceso(90202000, usuario, empresa, sucursal));
		
    }

    public void Modulo_Seguridad() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Modulo_Finanzas() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Modulo_Estadisticas() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
