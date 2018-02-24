package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.me.gj.database.ConectaBD;
import org.zkoss.zul.ListModelList;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.logistica.mantenimiento.SubLineas;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zul.Messagebox;

public class DaoSubLineas {

    //Instancias de Objetos
    ListModelList<SubLineas> objlstSublineas;
    SubLineas objSublinea;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    private static final Logger LOGGER = Logger.getLogger(DaoSubLineas.class);
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    public static String P_WHERE = "";
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    //Eventos Primarios - Transaccionales
    public String insertarSubLinea(SubLineas objSubLinea) throws SQLException {
        //Recupera en variables los valores de la tabla seteada        
        String SQL_INSERT_PROC_SUBLINEAS = "{call pack_tsublineas.p_005_insertarSublineas(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_SUBLINEAS);
            cst.clearParameters();
            cst.setString(1, objSubLinea.getSlin_des().toUpperCase());
            cst.setInt(2, objSubLinea.getSlin_codlin());
            cst.setInt(3, 0);
            cst.setInt(4, 1);
            cst.setString(5, "");
            cst.setString(6, objSubLinea.getSlin_usuadd());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + objSubLinea.getSlin_des());
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

    public String eliminarSubLinea(SubLineas objSubLinea) throws SQLException {
        String SQL_INSERT_PROC_SUBLINEAS = "{call pack_tsublineas.p_005_eliminarSublineas(?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PROC_SUBLINEAS);
            cst.clearParameters();
            cst.setString(1, objSubLinea.getSlin_cod());
            cst.setString(2, objSubLinea.getSlin_usumod());
            cst.registerOutParameter(3, java.sql.Types.NUMERIC);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(3);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objSubLinea.getSlin_cod());
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

    public String modificarSubLinea(SubLineas objSubLinea) throws SQLException {
        //Recupera en variables los valores de la tabla seteada        
        String SQL_UPDATE_PROC_SUBLINEAS = "{call pack_tsublineas.p_005_actualizarSublineas(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_UPDATE_PROC_SUBLINEAS);
            cst.clearParameters();
            cst.setString(1, objSubLinea.getSlin_cod());
            cst.setString(2, objSubLinea.getSlin_des().toUpperCase());
            cst.setString(3, objSubLinea.getSlin_estado());
            cst.setString(4, objSubLinea.getSlin_usumod());
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(5);
            s_msg = cst.getString(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objSubLinea.getSlin_codslin());
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

    //Eventos Secundarios - Listas y validaciones
    /*public ListModelList<SubLineas> lstSublineas(String estado) throws SQLException {
     String SQL_SUBLINEAS;
     if ("0".equals(estado)) {
     SQL_SUBLINEAS = " select s.slin_cod , s.slin_des , s.slin_codlin , lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, "
     + " slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 order by s.slin_codlin , s.slin_codslin  ";
     } else {
     SQL_SUBLINEAS = " select s.slin_cod , s.slin_des , s.slin_codlin , lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes, s.slin_estado, s.slin_usuadd, slin_fecadd, "
     + " slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado = 1 order by s.slin_codlin , s.slin_codslin ";
     }
     P_WHERE = SQL_SUBLINEAS;
     objlstSublineas = new ListModelList<SubLineas>();
     try {
     con = new ConectaBD().conectar();
     st = con.createStatement();
     rs = st.executeQuery(SQL_SUBLINEAS);
     while (rs.next()) {
     objSublinea = new SubLineas();
     objSublinea.setSlin_cod(rs.getString("slin_cod"));
     objSublinea.setSlin_des(rs.getString("slin_des"));
     objSublinea.setSlin_codlin(rs.getInt("slin_codlin"));
     objSublinea.setSslin_codlin(rs.getString("sslin_codlin"));
     objSublinea.setSlin_deslin(rs.getString("slin_codlindes"));
     objSublinea.setSlin_estado(rs.getString("slin_estado"));
     objSublinea.setSlin_usuadd(rs.getString("slin_usuadd"));
     objSublinea.setSlin_fecadd(rs.getTimestamp("slin_fecadd"));
     objSublinea.setSlin_usumod(rs.getString("slin_usumod"));
     objSublinea.setSlin_fecmod(rs.getTimestamp("slin_fecmod"));
     objSublinea.setVal(rs.getString("slin_estado").equals("1"));
     objlstSublineas.add(objSublinea);
     }
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSublineas.getSize() + " registro(s)");
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
     return objlstSublineas;
     }

     public ListModelList<SubLineas> CargaSublineas(int idLinea) throws SQLException {
     String SQL_SUBLINEAS = " select slin_cod, slin_des, slin_estado, slin_codlin codlin, slin_codslin slin_codlindes, slin_usuadd, slin_fecadd, slin_usumod, "
     + " slin_fecmod from tsublineas  where slin_estado = 1 and slin_codlin = " + idLinea + " order by slin_cod  ";
     objlstSublineas = null;
     objlstSublineas = new ListModelList<SubLineas>();
     try {
     con = new ConectaBD().conectar();
     st = con.createStatement();
     rs = st.executeQuery(SQL_SUBLINEAS);
     while (rs.next()) {
     objSublinea = new SubLineas();
     objSublinea.setSlin_cod(rs.getString("slin_cod"));
     objSublinea.setSlin_des(rs.getString("slin_des"));
     objSublinea.setSlin_codlincad(rs.getString("codlin"));
     objSublinea.setSlin_codlindes(rs.getString("slin_codlindes"));
     objSublinea.setSlin_estado(rs.getString("slin_estado"));
     objSublinea.setSlin_usuadd(rs.getString("slin_usuadd"));
     objSublinea.setSlin_fecadd(rs.getTimestamp("slin_fecadd"));
     objSublinea.setSlin_usumod(rs.getString("slin_usumod"));
     objSublinea.setSlin_fecmod(rs.getTimestamp("slin_fecmod"));
     objSublinea.setVal(rs.getString("slin_estado").equals("1"));
     objlstSublineas.add(objSublinea);
     }
     LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSublineas.getSize() + " registro(s)");
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
     return objlstSublineas;
     }*/
    public ListModelList<SubLineas> busquedaLovSubLineas(int linea, String busqueda) throws SQLException {
        String SQL_LOVSUBLINEAS;
        /*if ("%%".equals(lin)) {
         SQL_LOVSUBLINEAS = " select slin_cod, slin_des, slin_estado, slin_codlin, slin_codslin, slin_usuadd, "
         + "slin_fecadd, slin_usumod, slin_fecmod from tsublineas p where p.slin_estado = 1 and (p.slin_cod like '" + busqueda + "' or p.slin_des like '" + busqueda + "') order by p.slin_cod  ";
         } else {*/
        SQL_LOVSUBLINEAS = " select slin_cod, slin_des "
                //+ "slin_estado, slin_codlin, slin_codslin, slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod "
                + " from tsublineas p where p.slin_estado = 1 and p.slin_codlin = " + linea + " and (p.slin_cod like '" + busqueda + "' or p.slin_des like '" + busqueda + "') order by p.slin_cod  ";
        //}
        objlstSublineas = null;
        objlstSublineas = new ListModelList<SubLineas>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LOVSUBLINEAS);
            while (rs.next()) {
                objSublinea = new SubLineas();
                objSublinea.setSlin_cod(rs.getString("slin_cod"));
                objSublinea.setSlin_des(rs.getString("slin_des"));
                /*objSublinea.setSslin_codlin(rs.getString("sslin_codlin"));
                //objSublinea.setSlin_deslin(rs.getString("slin_codlindes"));
                objSublinea.setSlin_estado(rs.getString("slin_estado"));
                objSublinea.setSlin_usuadd(rs.getString("slin_usuadd"));
                objSublinea.setSlin_fecadd(rs.getTimestamp("slin_fecadd"));
                objSublinea.setSlin_usumod(rs.getString("slin_usumod"));
                objSublinea.setSlin_fecmod(rs.getTimestamp("slin_fecmod"));
                objSublinea.setVal(rs.getString("slin_estado").equals("1"));*/
                objlstSublineas.add(objSublinea);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSublineas.getSize() + " registro(s)");
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
        return objlstSublineas;
    }

    public ListModelList<SubLineas> listaSublineas(String dato, int indice, int i_estado) throws SQLException {
        String SQL_SUBLINEAS_SEARCH = "";
        if (i_estado == 3) {
            if (indice == 0) {
                SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 order by s.slin_codlin , s.slin_codslin ";
            } else {
                if (indice == 1) {
                    SQL_SUBLINEAS_SEARCH = "  select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 and s.slin_cod like '" + dato + "' order by s.slin_codlin , s.slin_codslin  ";
                } else if (indice == 2) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 and s.slin_des like '" + dato + "' order by s.slin_codlin , s.slin_codslin ";
                } else if (indice == 3) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 and lpad(s.slin_codlin,3,'0') like '" + dato + "' order by s.slin_codlin , s.slin_codslin ";
                } else if (indice == 4) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado <> 0 and t.tab_subdes like '" + dato + "' order by s.slin_codlin , s.slin_codslin  ";
                }
            }
        } else {
            if (indice == 0) {
                SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0  and s.slin_estado =" + i_estado + " order by s.slin_codlin , s.slin_codslin  ";
            } else {
                if (indice == 1) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado =" + i_estado + " and s.slin_cod like '" + dato + "' order by s.slin_codlin , s.slin_codslin  ";
                } else if (indice == 2) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado =" + i_estado + " and s.slin_des like '" + dato + "' order by s.slin_codlin , s.slin_codslin   ";
                } else if (indice == 3) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado =" + i_estado + " and lpad(s.slin_codlin,3,'0') like '" + dato + "' order by s.slin_codlin , s.slin_codslin   ";
                } else if (indice == 4) {
                    SQL_SUBLINEAS_SEARCH = " select s.slin_cod , s.slin_des , s.slin_codlin ,lpad(s.slin_codlin,3,'0') sslin_codlin ,t.tab_subdes slin_codlindes , s.slin_estado, s.slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from codijisa.tsublineas s, ttabgen t  where s.slin_codlin = t.tab_id and t.tab_cod = 4 and t.tab_id <> 0 and t.tab_est <> 0 and s.slin_estado =" + i_estado + " and t.tab_subdes like '" + dato + "' order by s.slin_codlin , s.slin_codslin   ";
                }
            }
        }
        P_WHERE = SQL_SUBLINEAS_SEARCH;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUBLINEAS_SEARCH);
            objlstSublineas = new ListModelList<SubLineas>();
            while (rs.next()) {
                objSublinea = new SubLineas();
                objSublinea.setSlin_cod(rs.getString("slin_cod"));
                objSublinea.setSlin_des(rs.getString("slin_des"));
                objSublinea.setSslin_codlin(rs.getString("sslin_codlin"));
                objSublinea.setSlin_deslin(rs.getString("slin_codlindes"));
                objSublinea.setSlin_estado(rs.getString("slin_estado"));
                objSublinea.setSlin_usuadd(rs.getString("slin_usuadd"));
                objSublinea.setSlin_fecadd(rs.getTimestamp("slin_fecadd"));
                objSublinea.setSlin_usumod(rs.getString("slin_usumod"));
                objSublinea.setSlin_fecmod(rs.getTimestamp("slin_fecmod"));
                objSublinea.setVal(rs.getString("slin_estado").equals("1"));
                objlstSublineas.add(objSublinea);
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
        return objlstSublineas;
    }

    public SubLineas buscarSublineaxCodigo(String lin_id, String sublin_id) throws SQLException {
        String sql_query;
        if ("%%".equals(lin_id)) {
            sql_query = "select slin_cod, slin_des, slin_estado, slin_codlin , slin_codslin slin_codlindes, "
                    + " slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from tsublineas p where p.slin_estado = 1 "
                    + " and slin_cod='" + sublin_id + "' order by slin_cod ";
        } else {
            int linea = Integer.parseInt(lin_id);
            sql_query = "select slin_cod, slin_des, slin_estado, slin_codlin , slin_codslin slin_codlindes, "
                    + " slin_usuadd, slin_fecadd, slin_usumod, slin_fecmod from tsublineas p where p.slin_estado = 1 "
                    + " and p.slin_codlin = " + linea + " and slin_cod='" + sublin_id + "' order by slin_cod ";
        }

        objSublinea = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            while (rs.next()) {
                objSublinea = new SubLineas();
                objSublinea.setSlin_cod(rs.getString("slin_cod"));//codigo de sublinea cadena
                objSublinea.setSlin_des(rs.getString("slin_des"));
                objSublinea.setSlin_codlin(rs.getInt("slin_codlin"));
                //objSublinea.setSlin_deslin(rs.getString("slin_codlindes"));
                objSublinea.setSlin_estado(rs.getString("slin_estado"));
                /*objSublinea.setSlin_usuadd(rs.getString("slin_usuadd"));
                objSublinea.setSlin_fecadd(rs.getTimestamp("slin_fecadd"));
                objSublinea.setSlin_usumod(rs.getString("slin_usumod"));
                objSublinea.setSlin_fecmod(rs.getTimestamp("slin_fecmod"));
                objSublinea.setVal(rs.getString("slin_estado").equals("1"));*/
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos de la sublinea con Codigo" + sublin_id);
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
        return objSublinea;
    }

    public int ValidaRelacionProducto_Sublinea(int dato) throws SQLException {
        int valor = 0;
        String SQL_SUBLINEAS = "select nvl(to_number(count(*)),0) from codijisa.tproductos t where t.pro_sublin='" + dato + "' order by t.pro_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUBLINEAS);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Relación de Sublinea con Productos  debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
        return valor;
    }

    public String validaMovimientos(SubLineas objSubLinea) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tsublineas.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, objSubLinea.getSlin_cod());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos de la sublinea " + objSubLinea.getSlin_cod());
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
