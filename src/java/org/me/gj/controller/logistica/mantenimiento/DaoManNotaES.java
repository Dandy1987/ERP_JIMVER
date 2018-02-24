package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.DetGui;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.mantenimiento.TipGui;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoManNotaES {

    //Instancias de Objetos
    ListModelList<Guias> objlstGuias;
    ListModelList<TipGui> objlstTtipGui;
    ListModelList<DetGui> objlstTdetGui;
    TipGui objTipGui;
    DetGui objDetGui;
    Guias objGuias;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoManNotaES.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarGuia(Guias guia) throws SQLException {
        String s_desGui = guia.getDesGui();
        int i_idMovGui = guia.getIdMovGui();
        int i_ordGui = guia.getOrdGui();
        int i_refGui = guia.getRefGui();
        int i_idDetGui = guia.getIdDetGui();
        String s_sunGui = String.valueOf(guia.getSunGui());
        String s_nomRepGui = guia.getNomRepGui();
        String s_glosa = guia.getGlosa();
        String s_usuadd = guia.getUsuaddGui();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERT_PROC_GUIAS = "{call pack_ttabgen.p_001_insertarGuia(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_desGui);
            cst.setInt(2, i_idMovGui);
            cst.setInt(3, i_ordGui);
            cst.setString(4, s_nomRepGui);
            cst.setInt(5, i_refGui);
            cst.setString(6, s_sunGui);
            cst.setInt(7, i_idDetGui);
            cst.setString(8, s_glosa);
            cst.setString(9, s_usuadd);
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripciÃ³n " + s_desGui);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarGuia(Guias guia) throws SQLException {
        int i_idGui = guia.getIdGui();
        String s_desGui = guia.getDesGui();
        int i_idMovGui = guia.getIdMovGui();
        int i_ordGui = guia.getOrdGui();
        int i_estGui = guia.getEstGui();
        String s_nomRepGui = guia.getNomRepGui();
        int i_refGui = guia.getRefGui();
        String s_sunGui = String.valueOf(guia.getSunGui());
        int i_detGui = guia.getIdDetGui();
        String s_glosa = guia.getGlosa();
        String s_usumod = guia.getUsumodGui();
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_UPDATE_PROC_GUIAS = "{call pack_ttabgen.p_001_actualizarGuia(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_idGui);
            cst.setString(2, s_desGui);
            cst.setInt(3, i_idMovGui);
            cst.setInt(4, i_ordGui);
            cst.setInt(5, i_estGui);
            cst.setString(6, s_nomRepGui);
            cst.setInt(7, i_refGui);
            cst.setString(8, s_sunGui);
            cst.setInt(9, i_detGui);
            cst.setString(10, s_glosa);
            cst.setString(11, s_usumod);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con cÃ³digo " + i_idGui);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarGuia(Guias guia) throws SQLException {
        int i_idGui = guia.getIdGui();
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_DELETE_PROC_GUIAS = "{call pack_ttabgen.p_001_eliminarGuia(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_DELETE_PROC_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_idGui);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con cÃ³digo " + i_idGui);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarTipMovGui(TipGui objTipGui) throws SQLException {
        String s_tabsubdes = objTipGui.getTab_subdes();
        String s_tabnomrep = objTipGui.getTab_nomrep();
        int i_tabord = objTipGui.getTab_ord();
        String s_tabusuadd = objTipGui.getTab_usuadd();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_TIPMOV_GUIAS = "{call pack_ttabgen.p_008_insertarTipoGuia(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_TIPMOV_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.setString(4, s_tabusuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripciÃ³n " + s_tabsubdes);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarTipMovGui(TipGui objTipGui) throws SQLException {
        int s_tabid = objTipGui.getTab_id();
        String s_tabsubdes = objTipGui.getTab_subdes();
        String s_tabnomrep = objTipGui.getTab_nomrep();
        int i_tabord = objTipGui.getTab_ord();
        int i_tabest = objTipGui.getTab_est();
        String s_tabusumod = objTipGui.getTab_usuadd();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_TIPMOV_GUIAS = "{call pack_ttabgen.p_008_actualizarTipoGuia(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_TIPMOV_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.setString(6, s_tabusumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con cÃ³digo " + s_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarTipMovGui(TipGui objTipGui) throws SQLException {
        int s_tabid = objTipGui.getTab_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_TIPMOV_GUIAS = "{call pack_ttabgen.p_008_eliminarTipoGuia(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_TIPMOV_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con cÃ³digo " + s_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida insertarDetGui(DetGui objDetGui) throws SQLException {
        String s_tabsubdes = objDetGui.getTab_subdes();
        String s_tabnomrep = objDetGui.getTab_nomrep();
        int i_tabord = objDetGui.getTab_ord();
        String s_tabusuadd = objDetGui.getTab_usuadd();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_DET_GUIAS = "{call pack_ttabgen.p_010_insertarDetalleGuia(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_DET_GUIAS);
            cst.clearParameters();
            cst.setString(1, s_tabsubdes);
            cst.setString(2, s_tabnomrep);
            cst.setInt(3, i_tabord);
            cst.setString(4, s_tabusuadd);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripciÃ³n " + s_tabsubdes);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizarDetGui(DetGui objDetGui) throws SQLException {
        int s_tabid = objDetGui.getTab_id();
        String s_tabsubdes = objDetGui.getTab_subdes();
        String s_tabnomrep = objDetGui.getTab_nomrep();
        int i_tabord = objDetGui.getTab_ord();
        int i_tabest = objDetGui.getTab_est();
        String s_tabusumod = objDetGui.getTab_usumod();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_DET_GUIAS = "{call pack_ttabgen.p_010_actualizarDetalleGuia(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_DET_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.setString(2, s_tabsubdes);
            cst.setString(3, s_tabnomrep);
            cst.setInt(4, i_tabord);
            cst.setInt(5, i_tabest);
            cst.setString(6, s_tabusumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con cÃ³digo " + s_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminarDetGui(DetGui objDetGui) throws SQLException {
        int s_tabid = objDetGui.getTab_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_DET_GUIAS = "{call pack_ttabgen.p_010_eliminarDetalleGuia(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_DET_GUIAS);
            cst.clearParameters();
            cst.setInt(1, s_tabid);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con cÃ³digo " + s_tabid);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminaciÃ³n porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Guias> listaGuias(int i_caso) throws SQLException {
        String SQL_GUIAS;
        if (i_caso == 0) {
            SQL_GUIAS = "select t.tab_id, t.tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod,t.tab_glosa from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est<>" + i_caso + " order by t.tab_id";
        } else if (i_caso == 3) {
            SQL_GUIAS = "select t.tab_id, t.tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod,t.tab_glosa from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_id=15 and t.tab_est=1 order by t.tab_id";
        } else {
            SQL_GUIAS = "select t.tab_id, t.tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod,t.tab_glosa from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est=" + i_caso + " order by t.tab_id";
        }

        P_WHERER = SQL_GUIAS;
        objlstGuias = new ListModelList<Guias>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GUIAS);
            while (rs.next()) {
                objGuias = new Guias();
                objGuias.setIdGui(rs.getInt("tab_id"));
                objGuias.setEstGui(rs.getInt("tab_est"));
                objGuias.setDesGui(rs.getString("tab_subdes"));
                objGuias.setIdMovGui(rs.getInt("tab_tip"));
                objGuias.setRefGui(rs.getInt("tab_guiref"));
                objGuias.setIdDetGui(rs.getInt("tab_guiingart"));
                objGuias.setSunGui(rs.getInt("tab_guisun"));
                objGuias.setOrdGui(rs.getInt("tab_ord"));
                objGuias.setNomRepGui(rs.getString("tab_nomrep"));
                objGuias.setCodigo(rs.getString("tab_subdir"));
                objGuias.setUsuaddGui(rs.getString("tab_usuadd"));
                objGuias.setFecaddGui(rs.getTimestamp("tab_fecadd"));
                objGuias.setUsumodGui(rs.getString("tab_usumod"));
                objGuias.setFecmodGui(rs.getTimestamp("tab_fecmod"));
                objGuias.setGlosa(rs.getString("tab_glosa"));
                objlstGuias.add(objGuias);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstGuias.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstGuias;
    }
	//Eventos Secundarios - Listas y validaciones
    public ListModelList<Guias> listaGuiasCombo(int i_caso) throws SQLException {
        String SQL_GUIAS;
        if (i_caso == 0) {
            SQL_GUIAS = "select t.tab_id, t.tab_subdir ||' - '|| t.tab_subdes tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est<>" + i_caso + " order by t.tab_id";
        } else if (i_caso == 3) {
            SQL_GUIAS = "select t.tab_id, t.tab_subdir ||' - '|| t.tab_subdes tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_id=15 and t.tab_est=1 order by t.tab_id";
        } else {
            SQL_GUIAS = "select t.tab_id, t.tab_subdir ||' - '|| t.tab_subdes tab_subdes , t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                    + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est=" + i_caso + " order by t.tab_id";
        }

        P_WHERER = SQL_GUIAS;
        objlstGuias = new ListModelList<Guias>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GUIAS);
            while (rs.next()) {
                objGuias = new Guias();
                objGuias.setIdGui(rs.getInt("tab_id"));
                objGuias.setEstGui(rs.getInt("tab_est"));
                objGuias.setDesGui(rs.getString("tab_subdes"));
                objGuias.setIdMovGui(rs.getInt("tab_tip"));
                objGuias.setRefGui(rs.getInt("tab_guiref"));
                objGuias.setIdDetGui(rs.getInt("tab_guiingart"));
                objGuias.setSunGui(rs.getInt("tab_guisun"));
                objGuias.setOrdGui(rs.getInt("tab_ord"));
                objGuias.setNomRepGui(rs.getString("tab_nomrep"));
                objGuias.setCodigo(rs.getString("tab_subdir"));
                objGuias.setUsuaddGui(rs.getString("tab_usuadd"));
                objGuias.setFecaddGui(rs.getTimestamp("tab_fecadd"));
                objGuias.setUsumodGui(rs.getString("tab_usumod"));
                objGuias.setFecmodGui(rs.getTimestamp("tab_fecmod"));
                objlstGuias.add(objGuias);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstGuias.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstGuias;
    }
    public ListModelList<Guias> busquedaGuias(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                        + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est<>0 order by t.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                            + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est<>0 and t.tab_subdir like '" + consulta + "' order by t.tab_id";
                } else {
                    sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                            + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est<>0 and t.tab_subdes like '" + consulta + "' order by t.tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                        + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est=" + i_estado + "  order by t.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                            + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est=" + i_estado + " and t.tab_subdir like '" + consulta + "' order by t.tab_id";
                } else {
                    sql = "select t.tab_id,t.tab_subdes,t.tab_est,t.tab_tip,t.tab_guiref,t.tab_guisun,t.tab_guiingart,t.tab_ord,t.tab_nomrep,t.tab_subdir,t.tab_usuadd,t.tab_fecadd,t.tab_usumod,t.tab_fecmod from ttabgen t "
                            + "where t.tab_cod=1 and t.tab_id<>0 and t.tab_est=" + i_estado + " and t.tab_subdes like '" + consulta + "' order by t.tab_id";
                }
            }
        }
        P_WHERER = sql;
        objlstGuias = new ListModelList<Guias>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objGuias = new Guias();
                objGuias.setIdGui(rs.getInt("tab_id"));
                objGuias.setEstGui(rs.getInt("tab_est"));
                objGuias.setDesGui(rs.getString("tab_subdes"));
                objGuias.setIdMovGui(rs.getInt("tab_tip"));
                objGuias.setRefGui(rs.getInt("tab_guiref"));
                objGuias.setIdDetGui(rs.getInt("tab_guiingart"));
                objGuias.setSunGui(rs.getInt("tab_guisun"));
                objGuias.setOrdGui(rs.getInt("tab_ord"));
                objGuias.setNomRepGui(rs.getString("tab_nomrep"));
                objGuias.setCodigo(rs.getString("tab_subdir"));
                objGuias.setUsuaddGui(rs.getString("tab_usuadd"));
                objGuias.setFecaddGui(rs.getTimestamp("tab_fecadd"));
                objGuias.setUsumodGui(rs.getString("tab_usumod"));
                objGuias.setFecmodGui(rs.getTimestamp("tab_fecmod"));
                objlstGuias.add(objGuias);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstGuias;
    }

    public ListModelList<DetGui> busquedaDetGui(int i_tipBus, String i_consulta, int i_estado) throws SQLException {
        String sql;

        if (i_estado == 3) {
            if (i_tipBus == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est<>0 order by p.tab_id";
            } else {
                if (i_tipBus == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est<>0 and p.tab_id like '" + i_consulta + "' order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdes like '" + i_consulta + "' order by tab_id";
                }
            }
        } else {
            if (i_tipBus == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else {
                if (i_tipBus == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + i_consulta + "' order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + i_consulta + "' order by p.tab_id";
                }
            }
        }
        P_WHERE = sql;
        objlstTdetGui = new ListModelList<DetGui>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objDetGui = new DetGui();
                objDetGui.setTab_id(rs.getInt("tab_id"));
                objDetGui.setTab_subdes(rs.getString("tab_subdes"));
                objDetGui.setTab_subdir(rs.getString("tab_subdir"));
                objDetGui.setTab_est(rs.getInt("tab_est"));
                objDetGui.setTab_ord(rs.getInt("tab_ord"));
                objDetGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objDetGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objDetGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objDetGui.setTab_usumod(rs.getString("tab_usumod"));
                objDetGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTdetGui.add(objDetGui);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstTdetGui;
    }

    public ListModelList<DetGui> lstDetGui(int val) throws SQLException {
        String sql;
        if (val == 0) {
            sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                    + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est<>" + val + " order by p.tab_id";
        } else {
            sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                    + "where p.tab_cod=10 and p.tab_id<>0 and p.tab_est=" + val + " order by p.tab_id";
        }
        P_WHERE = sql;
        objlstTdetGui = new ListModelList<DetGui>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objDetGui = new DetGui();
                objDetGui.setTab_id(rs.getInt("tab_id"));
                objDetGui.setTab_subdes(rs.getString("tab_subdes"));
                objDetGui.setTab_subdir(rs.getString("tab_subdir"));
                objDetGui.setTab_est(rs.getInt("tab_est"));
                objDetGui.setTab_ord(rs.getInt("tab_ord"));
                objDetGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objDetGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objDetGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objDetGui.setTab_usumod(rs.getString("tab_usumod"));
                objDetGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));

                objlstTdetGui.add(objDetGui);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTdetGui.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstTdetGui;
    }

    public ListModelList<TipGui> listaTipGui(int val) throws SQLException {
        String sql;
        if (val == 0) {
            sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                    + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est<>" + val + " order by p.tab_id";
        } else {
            sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                    + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est=" + val + " order by p.tab_id";
        }
        P_WHERE = sql;
        objlstTtipGui = new ListModelList<TipGui>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTipGui = new TipGui();
                objTipGui.setTab_id(rs.getInt("tab_id"));
                objTipGui.setTab_subdes(rs.getString("tab_subdes"));
                objTipGui.setTab_subdir(rs.getString("tab_subdir"));
                objTipGui.setTab_est(rs.getInt("tab_est"));
                objTipGui.setTab_ord(rs.getInt("tab_ord"));
                objTipGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipGui.setTab_usumod(rs.getString("tab_usumod"));
                objTipGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTtipGui.add(objTipGui);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstTtipGui.getSize() + " registro(s)");
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objlstTtipGui;
    }

    public ListModelList<TipGui> busquedaTipMovGui(int seleccion, String consulta, int i_estado) throws SQLException {
        String sql;
        if (i_estado == 3) {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est<>0 order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est<>0 and p.tab_id like '" + consulta
                            + "' order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est<>0 and p.tab_subdes like '" + consulta
                            + "' order by p.tab_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                        + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est=" + i_estado + " order by p.tab_id";
            } else {
                if (seleccion == 1) {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_id like '" + consulta
                            + "' order by p.tab_id";
                } else {
                    sql = "select p.tab_id,p.tab_subdes,p.tab_subdir,p.tab_est,p.tab_ord,p.tab_nomrep,p.tab_usuadd,p.tab_fecadd,p.tab_usumod,p.tab_fecmod from ttabgen p "
                            + "where p.tab_cod=8 and p.tab_id<>0 and p.tab_est=" + i_estado + " and p.tab_subdes like '" + consulta
                            + "' order by p.tab_id";
                }
            }
        }
        P_WHERE = sql;
        objlstTtipGui = new ListModelList<TipGui>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objTipGui = new TipGui();
                objTipGui.setTab_id(rs.getInt("tab_id"));
                objTipGui.setTab_subdes(rs.getString("tab_subdes"));
                objTipGui.setTab_subdir(rs.getString("tab_subdir"));
                objTipGui.setTab_est(rs.getInt("tab_est"));
                objTipGui.setTab_ord(rs.getInt("tab_ord"));
                objTipGui.setTab_nomrep(rs.getString("tab_nomrep"));
                objTipGui.setTab_usuadd(rs.getString("tab_usuadd"));
                objTipGui.setTab_fecadd(rs.getTimestamp("tab_fecadd"));
                objTipGui.setTab_usumod(rs.getString("tab_usumod"));
                objTipGui.setTab_fecmod(rs.getTimestamp("tab_fecmod"));
                objlstTtipGui.add(objTipGui);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstTtipGui;
    }

    public int ValidaGuiaRef(int dato) throws SQLException {
        String SQL_GUIAS;
        int valor = 0;
        SQL_GUIAS = "select nvl(to_number(count(*)),0) from ttabgen t where t.tab_cod='1' and t.tab_id='" + dato + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_GUIAS);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de ValidaciÃ³n de Existencia de Abreviatura  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return valor;
    }

    public Guias BusquedaGuia(String codigo) throws SQLException {
        String SQL_PROV = "select t.tab_subdes,t.tab_subdir from ttabgen t where  t.tab_cod='1' and t.tab_est=1 "
                + "and t.tab_id is not null and t.tab_subdir='" + codigo + "' order by t.tab_subdir";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PROV);
            objGuias = new Guias();
            while (rs.next()) {
                objGuias.setDesGui(rs.getString(1));
                objGuias.setCodigo(rs.getString(2));
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexiÃ³n a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objGuias;
    }

    public String validaMovimientos(DetGui objDetGui) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objDetGui.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del almacen " + objDetGui.getTab_subdir());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_msg;
    }

    public String validaMovimientost(TipGui objTipGui) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientostipmov(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setInt(1, objTipGui.getTab_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del tipo de movimiento de guia " + objTipGui.getTab_id());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_msg;
    }

    public String validaMovimientosg(Guias guia) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_ttabgen.p_valida_movimientosguia(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, guia.getCodigo());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de guia " + objDetGui.getTab_subdir());
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_msg;
    }
}
