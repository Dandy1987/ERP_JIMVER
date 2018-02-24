package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.NumDoc;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNumDoc {

    //Instancias de Objetos
    ListModelList<NumDoc> objlstNumDoc;
    NumDoc objNumDoc;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoNumDoc.class);

    //Eventos Primarios - Transaccionales
    public String insertarNumDoc(NumDoc objNumDoc) throws SQLException {

        int emp_id = objNumDoc.getEmp_id();
        int suc_id = objNumDoc.getSuc_id();
        int tipven = objNumDoc.getTnumdoc_tipven();
        int serie = objNumDoc.getTnumdoc_serie();
        String des = objNumDoc.getTnumdoc_des();
        int notes = objNumDoc.getTnumdoc_notes();
        int estado = objNumDoc.getTnumdoc_est();
        String nomrep = objNumDoc.getTnumdoc_nomrep();
        String usuadd = objNumDoc.getTnumdoc_usuadd();

        String SQL_INSERTAR_NUMDOC = "{call pack_tnumdoc.p_insertarNumDoc(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_NUMDOC);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, tipven);
            cst.setInt(4, serie);
            cst.setString(5, des);
            cst.setInt(6, notes);
            cst.setString(7, nomrep);
            cst.setInt(8, estado);
            cst.setString(9, usuadd);
            cst.registerOutParameter(10, java.sql.Types.NUMERIC);
            cst.registerOutParameter(11, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(11);
            i_flagErrorBD = cst.getInt(10);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + des);
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

    public String actualizarNumDoc(NumDoc objNumDoc) throws SQLException {

        int numdoc_cor = objNumDoc.getTnumdoc_cor();
        int emp_id = objNumDoc.getEmp_id();
        int suc_id = objNumDoc.getSuc_id();
        int tipven = objNumDoc.getTnumdoc_tipven();
        int serie = objNumDoc.getTnumdoc_serie();
        int notes = objNumDoc.getTnumdoc_notes();
        String des = objNumDoc.getTnumdoc_des();
        int estado = objNumDoc.getTnumdoc_est();
        String nomrep = objNumDoc.getTnumdoc_nomrep();
        String usumod = objNumDoc.getTnumdoc_usumod();

        String SQL_ACTUALIZAR_NUMDOC = "{call pack_tnumdoc.p_actualizarNumDoc(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_NUMDOC);
            cst.clearParameters();
            cst.setInt(1, numdoc_cor);
            cst.setInt(2, emp_id);
            cst.setInt(3, suc_id);
            cst.setInt(4, tipven);
            cst.setInt(5, serie);
            cst.setString(6, des);
            cst.setInt(7, notes);
            cst.setString(8, nomrep);
            cst.setInt(9, estado);
            cst.setString(10, usumod);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + des);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    public String eliminarNumDoc(NumDoc objNumDoc) throws SQLException {
        int i_cor = objNumDoc.getTnumdoc_cor();
        int emp_id = objNumDoc.getEmp_id();
        int suc_id = objNumDoc.getSuc_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_tnumdoc.p_eliminarNumDoc(?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, i_cor);
            cst.setInt(2, emp_id);
            cst.setInt(3, suc_id);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + i_cor);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NumDoc> listaNumDoc(int i_caso) throws SQLException {
        String SQL_NUMDOC;
        if (i_caso == 0) {
            SQL_NUMDOC = "select v.* from v_listanumdoc v "
                    + "where v.tnumdoc_est<>" + i_caso + " order by v.tnumdoc_cor";

        } else {
            SQL_NUMDOC = "select  v.* from v_listanumdoc v "
                    + "where v.tnumdoc_est=" + i_caso + " order by v.tnumdoc_cor";

        }
        P_WHERE = SQL_NUMDOC;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NUMDOC);
            objlstNumDoc = new ListModelList<NumDoc>();
            while (rs.next()) {
                objNumDoc = new NumDoc();
                objNumDoc.setEmp_id(rs.getInt("emp_id"));
                objNumDoc.setSuc_id(rs.getInt("suc_id"));
                objNumDoc.setTnumdoc_cor(rs.getInt("tnumdoc_cor"));
                objNumDoc.setTnumdoc_corid(rs.getString("tnumdoc_corid"));
                objNumDoc.setTnumdoc_tipven(rs.getInt("tnumdoc_tipven"));
                objNumDoc.setTnumdoc_tipvenid(rs.getString("tnumdoc_tipvenid"));
                objNumDoc.setTnumdoc_tipven_des(rs.getString("tnumdoc_tipven_des"));
                objNumDoc.setTnumdoc_serie(rs.getInt("tnumdoc_serie"));
                objNumDoc.setTnumdoc_serieid(rs.getString("tnumdoc_serieid"));
                objNumDoc.setTnumdoc_des(rs.getString("tnumdoc_des"));
                objNumDoc.setTnumdoc_notes(rs.getInt("tnumdoc_notes"));
                objNumDoc.setTnumdoc_notes_des(rs.getString("tnumdoc_notes_des"));
                objNumDoc.setTnumdoc_est(rs.getInt("tnumdoc_est"));
                objNumDoc.setTnumdoc_nomrep(rs.getString("tnumdoc_nomrep"));
                objNumDoc.setTnumdoc_usuadd(rs.getString("tnumdoc_usuadd"));
                objNumDoc.setTnumdoc_usuadd(rs.getString("tnumdoc_usumod"));
                objNumDoc.setTnumdoc_fecadd(rs.getTimestamp("tnumdoc_fecadd"));
                objNumDoc.setTnumdoc_fecmod(rs.getTimestamp("tnumdoc_fecmod"));

                objlstNumDoc.add(objNumDoc);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumDoc.getSize() + " registro(s)");
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
        return objlstNumDoc;
    }

    public ListModelList<NumDoc> listaSeriexNumDoc(int tipodoc) throws SQLException {
        String SQL_SERIE;
        SQL_SERIE = "select t.tnumdoc_serie,t.tnumdoc_des from tnumdoc t "
                + " where t.tnumdoc_est=1 and t.tnumdoc_tipven=" + tipodoc + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.tnumdoc_serie";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SERIE);
            objlstNumDoc = new ListModelList<NumDoc>();
            while (rs.next()) {
                objNumDoc = new NumDoc();
                objNumDoc.setTnumdoc_serie(rs.getInt("tnumdoc_serie"));
                objNumDoc.setTnumdoc_des(rs.getString("tnumdoc_des"));
                objlstNumDoc.add(objNumDoc);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumDoc.getSize() + " registro(s)");
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
        return objlstNumDoc;
    }

    public ListModelList<NumDoc> busquedaNumDoces(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;
        if (i_estado == 3) {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                        + "where v.tnumdoc_est<>0 order by v.tnumdoc_cor";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est<>0  and tnumdoc_cor_lpad like '" + consulta + "' order by tnumdoc_cor";
                } else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est<>0 and v.tnumdoc_tipven_des like '" + consulta + "' order by tnumdoc_cor";
                } else {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est<>0 and v.tnumdoc_serie like '" + consulta + "' order by tnumdoc_cor";
                }
            }
        } else {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                        + "where v.tnumdoc_est=" + i_estado + " order by v.tnumdoc_cor";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est=" + i_estado + " and v.tnumdoc_cor_lpad like '" + consulta + "' order by v.tnumdoc_cor";
                } else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est=" + i_estado + " and v.tnumdoc_tipven_des like '" + consulta + "' order by v.tnumdoc_cor";
                } else {
                    SQL_BUSQUEDA = "select v.* from v_listanumdoc v "
                            + "where v.tnumdoc_est=" + i_estado + " and v.tnumdoc_serie like '" + consulta + "' order by v.tnumdoc_cor";
                }
            }
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstNumDoc = new ListModelList<NumDoc>();
            while (rs.next()) {
                objNumDoc = new NumDoc();
                objNumDoc.setEmp_id(rs.getInt("emp_id"));
                objNumDoc.setSuc_id(rs.getInt("suc_id"));
                objNumDoc.setTnumdoc_cor(rs.getInt("tnumdoc_cor"));
                objNumDoc.setTnumdoc_corid(rs.getString("tnumdoc_corid"));
                objNumDoc.setTnumdoc_tipven(rs.getInt("tnumdoc_tipven"));
                objNumDoc.setTnumdoc_tipvenid(rs.getString("tnumdoc_tipvenid"));
                objNumDoc.setTnumdoc_tipven_des(rs.getString("tnumdoc_tipven_des"));
                objNumDoc.setTnumdoc_serie(rs.getInt("tnumdoc_serie"));
                objNumDoc.setTnumdoc_serieid(rs.getString("tnumdoc_serieid"));
                objNumDoc.setTnumdoc_des(rs.getString("tnumdoc_des"));
                objNumDoc.setTnumdoc_notes(rs.getInt("tnumdoc_notes"));
                objNumDoc.setTnumdoc_notes_des(rs.getString("tnumdoc_notes_des"));
                objNumDoc.setTnumdoc_est(rs.getInt("tnumdoc_est"));
                objNumDoc.setTnumdoc_nomrep(rs.getString("tnumdoc_nomrep"));
                objNumDoc.setTnumdoc_usuadd(rs.getString("tnumdoc_usuadd"));
                objNumDoc.setTnumdoc_usuadd(rs.getString("tnumdoc_usumod"));
                objNumDoc.setTnumdoc_fecadd(rs.getTimestamp("tnumdoc_fecadd"));
                objNumDoc.setTnumdoc_fecmod(rs.getTimestamp("tnumdoc_fecmod"));
                objlstNumDoc.add(objNumDoc);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Busqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la busqueda del registro(s) porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objlstNumDoc;
    }

}
