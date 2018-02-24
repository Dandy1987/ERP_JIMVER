package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.NumeFac;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNumeFac {

    //Instancias de Objetos
    ListModelList<NumeFac> objlstNumeFac;
    NumeFac objNumeFac;
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
    private static final Logger LOGGER = Logger.getLogger(DaoNumeFac.class);

    //Eventos Primarios - Transaccionales
    public String insertarNumeFac(NumeFac objNumeFac) throws SQLException {
        int emp_id = objNumeFac.getEmp_id();
        int suc_id = objNumeFac.getSuc_id();
        int tipven = objNumeFac.getNumefac_tipdoc();
        int serie = objNumeFac.getNumefac_serie();
        String des = objNumeFac.getNumefac_des();
        int notes = objNumeFac.getNumefac_notes();
        int nroitems = objNumeFac.getNumefac_nroitems();
        int estado = objNumeFac.getNumefac_est();
        String nomrep = objNumeFac.getNumefac_nomrep();
        String usuadd = objNumeFac.getNumefac_usuadd();

        String SQL_INSERTAR_NUMDOC = "{call pack_tnumefac.p_insertarNumeFac(?,?,?,?,?,?,?,?,?,?,?,?)}";
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
            cst.setInt(7, nroitems);
            cst.setString(8, nomrep);
            cst.setInt(9, estado);
            cst.setString(10, usuadd);
            cst.registerOutParameter(11, java.sql.Types.NUMERIC);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(12);
            i_flagErrorBD = cst.getInt(11);
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

    public String actualizarNumeFac(NumeFac objNumeFac) throws SQLException {
        int cor = objNumeFac.getNumefac_cor();
        int emp_id = objNumeFac.getEmp_id();
        int suc_id = objNumeFac.getSuc_id();
        int tipven = objNumeFac.getNumefac_tipdoc();
        int serie = objNumeFac.getNumefac_serie();
        int notes = objNumeFac.getNumefac_notes();
        int nroitems = objNumeFac.getNumefac_nroitems();
        String des = objNumeFac.getNumefac_des();
        int estado = objNumeFac.getNumefac_est();
        String nomrep = objNumeFac.getNumefac_nomrep();
        String usumod = objNumeFac.getNumefac_usumod();

        String SQL_ACTUALIZAR_NUMDOC = "{call pack_tnumefac.p_actualizarNumeFac(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_NUMDOC);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, tipven);
            cst.setInt(4, serie);
            cst.setInt(5, cor);
            cst.setString(6, des);
            cst.setInt(7, notes);
            cst.setInt(8, nroitems);
            cst.setString(9, nomrep);
            cst.setInt(10, estado);
            cst.setString(11, usumod);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con correlativo " + cor);
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

    public String eliminarNumeFac(NumeFac objNumeFac) throws SQLException {
        int i_doc = objNumeFac.getNumefac_tipdoc();
        int i_serie = objNumeFac.getNumefac_serie();
        int emp_id = objNumeFac.getEmp_id();
        int suc_id = objNumeFac.getSuc_id();
        String SQL_ELIMINAR_REL_GUIAS = "{call pack_tnumefac.p_eliminarNumeFac(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_REL_GUIAS);
            cst.clearParameters();
            cst.setInt(1, emp_id);
            cst.setInt(2, suc_id);
            cst.setInt(3, i_doc);
            cst.setInt(4, i_serie);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con documento " + i_doc + " y serie " + i_serie);
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
    public ListModelList<NumeFac> listaNumeFac(int i_caso) throws SQLException {
        String SQL_NUMDOC;
        String valida_empsuc = " and v.emp_id= " + objUsuCredential.getCodemp() + " and v.suc_id=" + objUsuCredential.getCodsuc();
        if (i_caso == 0) {
            SQL_NUMDOC = "select v.* from v_listanumefac v "
                    + "where v.numefac_est<>" + i_caso + valida_empsuc + " order by v.numefac_tipdoc,v.numefac_serie";

        } else {
            SQL_NUMDOC = "select  v.* from v_listanumefac v "
                    + "where v.numefac_est=" + i_caso + valida_empsuc + " order by v.numefac_tipdoc,v.numefac_serie";

        }
        P_WHERE = SQL_NUMDOC;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_NUMDOC);
            objlstNumeFac = new ListModelList<NumeFac>();
            while (rs.next()) {
                objNumeFac = new NumeFac();
                objNumeFac.setEmp_id(rs.getInt("emp_id"));
                objNumeFac.setSuc_id(rs.getInt("suc_id"));
                objNumeFac.setNumefac_cor(rs.getInt("numefac_cor"));
                objNumeFac.setNumefac_corid(rs.getString("numefac_cor_id"));
                objNumeFac.setNumefac_tipdoc(rs.getInt("numefac_tipdoc"));
                objNumeFac.setNumefac_tipdoc_id(rs.getString("numefac_tipdoc_id"));
                objNumeFac.setNumefac_tipdoc_des(rs.getString("numefac_tipdoc_des"));
                objNumeFac.setNumefac_serie(rs.getInt("numefac_serie"));
                objNumeFac.setNumefac_serie_id(rs.getString("numefac_serie_id"));
                objNumeFac.setNumefac_des(rs.getString("numefac_des"));
                objNumeFac.setNumefac_notes(rs.getInt("numefac_notes"));
                objNumeFac.setNumefac_notes_des(rs.getString("numefac_notes_des"));
                objNumeFac.setNumefac_est(rs.getInt("numefac_est"));
                objNumeFac.setNumefac_nroitems(rs.getInt("numefac_nroitems"));
                objNumeFac.setNumefac_nomrep(rs.getString("numefac_nomrep"));
                objNumeFac.setNumefac_usuadd(rs.getString("numefac_usuadd"));
                objNumeFac.setNumefac_usumod(rs.getString("numefac_usumod"));
                objNumeFac.setNumefac_fecadd(rs.getTimestamp("numefac_fecadd"));
                objNumeFac.setNumefac_fecmod(rs.getTimestamp("numefac_fecmod"));

                objlstNumeFac.add(objNumeFac);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeFac.getSize() + " registro(s)");
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
        return objlstNumeFac;
    }

    public ListModelList<NumeFac> listaSeriexNumeFac(int tipodoc) throws SQLException {
        String SQL_SERIE;
        SQL_SERIE = "select t.numefac_serie,t.numefac_des from tnumefac t "
                + " where t.numefac_est=1 and t.numefac_tipdoc=" + tipodoc + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.numefac_serie";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SERIE);
            objlstNumeFac = new ListModelList<NumeFac>();
            while (rs.next()) {
                objNumeFac = new NumeFac();
                objNumeFac.setNumefac_serie(rs.getInt("numefac_serie"));
                objNumeFac.setNumefac_des(rs.getString("numefac_des"));
                objlstNumeFac.add(objNumeFac);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNumeFac.getSize() + " registro(s)");
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
        return objlstNumeFac;
    }

    public ListModelList<NumeFac> busquedaNumeFaces(int seleccion, String consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA;
        String valida_empsuc = " and v.emp_id= " + objUsuCredential.getCodemp() + " and v.suc_id=" + objUsuCredential.getCodsuc();
        if (i_estado == 3) {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                        + " where v.numefac_est<>0 " + valida_empsuc + " order by v.numefac_tipdoc,v.numefac_serie";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est<>0 " 
                            + valida_empsuc + "and numefac_cor_id like '"+ consulta +"' order by v.numefac_tipdoc,v.numefac_serie";
                } else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est<>0 " 
                            + valida_empsuc + "and v.numefac_tipdoc_des like '" + consulta +"' order by v.numefac_tipdoc,v.numefac_serie";
                } else {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est<>0 " 
                            + valida_empsuc + "and v.numefac_serie_id like '" + consulta +"' order by v.numefac_tipdoc,v.numefac_serie";
                }
            }
        } else {
            if (seleccion == 0) {
                SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                        + " where v.numefac_est=" + i_estado + valida_empsuc + " order by v.numefac_tipdoc,v.numefac_serie";
            } else {
                if (seleccion == 1) {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est=" + i_estado + ""
                            + valida_empsuc + " and v.numefac_cor_id like '"+ consulta +"' order by v.numefac_tipdoc,v.numefac_serie";
                } else if (seleccion == 2) {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est=" + i_estado + ""
                            + valida_empsuc + " and v.numefac_tipdoc_des like '"+ consulta +"' order by v.numefac_tipdoc,v.numefac_serie";
                } else {
                    SQL_BUSQUEDA = "select v.* from v_listanumefac v "
                            + " where v.numefac_est=" + i_estado + ""
                            + valida_empsuc + " and v.numefac_serie_id like '" + consulta + "' order by v.numefac_tipdoc,v.numefac_serie";
                }
            }
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objlstNumeFac = new ListModelList<NumeFac>();
            while (rs.next()) {
                objNumeFac = new NumeFac();
                objNumeFac.setEmp_id(rs.getInt("emp_id"));
                objNumeFac.setSuc_id(rs.getInt("suc_id"));
                objNumeFac.setNumefac_cor(rs.getInt("numefac_cor"));
                objNumeFac.setNumefac_corid(rs.getString("numefac_cor_id"));
                objNumeFac.setNumefac_tipdoc(rs.getInt("numefac_tipdoc"));
                objNumeFac.setNumefac_tipdoc_id(rs.getString("numefac_tipdoc_id"));
                objNumeFac.setNumefac_tipdoc_des(rs.getString("numefac_tipdoc_des"));
                objNumeFac.setNumefac_serie(rs.getInt("numefac_serie"));
                objNumeFac.setNumefac_serie_id(rs.getString("numefac_serie_id"));
                objNumeFac.setNumefac_des(rs.getString("numefac_des"));
                objNumeFac.setNumefac_notes(rs.getInt("numefac_notes"));
                objNumeFac.setNumefac_notes_des(rs.getString("numefac_notes_des"));
                objNumeFac.setNumefac_est(rs.getInt("numefac_est"));
                objNumeFac.setNumefac_nomrep(rs.getString("numefac_nomrep"));
                objNumeFac.setNumefac_usuadd(rs.getString("numefac_usuadd"));
                objNumeFac.setNumefac_usumod(rs.getString("numefac_usumod"));
                objNumeFac.setNumefac_fecadd(rs.getTimestamp("numefac_fecadd"));
                objNumeFac.setNumefac_fecmod(rs.getTimestamp("numefac_fecmod"));
                objNumeFac.setNumefac_nroitems(rs.getInt("numefac_nroitems"));
                objlstNumeFac.add(objNumeFac);
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
        return objlstNumeFac;
    }
}
