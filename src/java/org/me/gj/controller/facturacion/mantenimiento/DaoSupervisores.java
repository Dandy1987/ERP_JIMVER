package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Supervisor;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoSupervisores {

    //Instancias de Objetos
    ListModelList<Supervisor> objlstSupervisores;
    Supervisor objSupervisor;
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
    private static final Logger LOGGER = Logger.getLogger(DaoSupervisores.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(Supervisor objSupervisor) throws SQLException {
        String sup_apenom = objSupervisor.getSup_apenom();
        String sup_dni = objSupervisor.getSup_dni();
        String sup_direcc = objSupervisor.getSup_direcc();
        long sup_telf = objSupervisor.getSup_telf();
        long sup_movil = objSupervisor.getSup_movil();
        Date sup_fecnac = objSupervisor.getSup_fecnac();
        String sup_usuadd = objSupervisor.getSup_usuadd();
        String sup_apoyo = objSupervisor.getSup_apoyo();
        int suc_id = objSupervisor.getSuc_id();
        int emp_id = objSupervisor.getEmp_id();
        String sup_nomrep = objSupervisor.getSup_nomrep();
        int sup_ord = objSupervisor.getSup_ord();
        String sup_pssw = objSupervisor.getSup_pssw();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_SUPERVISOR = "{call pack_tsupervisores.p_insertarSupervisor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_SUPERVISOR);
            cst.clearParameters();
            cst.setString(1, sup_apenom);
            cst.setString(2, sup_dni);
            cst.setString(3, sup_direcc);
            cst.setLong(4, sup_telf);
            cst.setLong(5, sup_movil);
            cst.setDate(6, new java.sql.Date(sup_fecnac.getTime()));
            cst.setString(7, sup_usuadd);
            cst.setString(8, sup_apoyo);
            cst.setInt(9, suc_id);
            cst.setLong(10, emp_id);
            cst.setString(11, sup_nomrep);
            cst.setInt(12, sup_ord);
            cst.setString(13, sup_pssw);
            cst.registerOutParameter(14, java.sql.Types.NUMERIC);
            cst.registerOutParameter(15, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(15);
            i_flagErrorBD = cst.getInt(14);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + sup_apenom);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida actualizar(Supervisor objSupervisor) throws SQLException {
        int sup_key = objSupervisor.getSup_key();
        String sup_id = objSupervisor.getSup_id();
        String sup_apenom = objSupervisor.getSup_apenom();
        String sup_dni = objSupervisor.getSup_dni();
        String sup_direcc = objSupervisor.getSup_direcc();
        long sup_telf = objSupervisor.getSup_telf();
        long sup_movil = objSupervisor.getSup_movil();
        Date sup_fecnac = objSupervisor.getSup_fecnac();
        int sup_est = objSupervisor.getSup_est();
        String sup_usumod = objSupervisor.getSup_usumod();
        int sup_flagcie = objSupervisor.getSup_flagcie();
        String sup_apoyo = objSupervisor.getSup_apoyo();
        int suc_id = objSupervisor.getSuc_id();
        int emp_id = objSupervisor.getEmp_id();
        String sup_nomrep = objSupervisor.getSup_nomrep();
        int sup_ord = objSupervisor.getSup_ord();
        String sup_pssw = objSupervisor.getSup_pssw();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_SUPERVISOR = "{call pack_tsupervisores.p_actualizarSupervisor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_SUPERVISOR);
            cst.clearParameters();
            cst.setInt(1, sup_key);
            cst.setString(2, sup_id);
            cst.setString(3, sup_apenom);
            cst.setString(4, sup_dni);
            cst.setString(5, sup_direcc);
            cst.setLong(6, sup_telf);
            cst.setLong(7, sup_movil);
            cst.setDate(8, new java.sql.Date(sup_fecnac.getTime()));
            cst.setInt(9, sup_est);
            cst.setString(10, sup_usumod);
            cst.setInt(11, sup_flagcie);
            cst.setString(12, sup_apoyo);
            cst.setInt(13, suc_id);
            cst.setLong(14, emp_id);
            cst.setString(15, sup_nomrep);
            cst.setInt(16, sup_ord);
            cst.setString(17, sup_pssw);
            cst.registerOutParameter(18, java.sql.Types.NUMERIC);
            cst.registerOutParameter(19, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(19);
            i_flagErrorBD = cst.getInt(18);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + sup_id);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    public ParametrosSalida eliminar(Supervisor objSupervisor) throws SQLException {
        int sup_key = objSupervisor.getSup_key();
        String sup_id = objSupervisor.getSup_id();
        int suc_id = objSupervisor.getSuc_id();
        int emp_id = objSupervisor.getEmp_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_SUPERVISOR = "{call pack_tsupervisores.p_eliminarSupervisor(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_SUPERVISOR);
            cst.clearParameters();
            cst.setInt(1, sup_key);
            cst.setString(2, sup_id);
            cst.setInt(3, suc_id);
            cst.setInt(4, emp_id);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + sup_id);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            i_flagErrorBD = 1;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la eliminación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Supervisor> listaSupervisores(int i_caso) throws SQLException {
        String SQL_SUPERVISORES;
        if (i_caso == 0) {
            SQL_SUPERVISORES = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                    + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_est<>" + i_caso + " order by t.sup_ord,t.sup_id";
        } else {
            SQL_SUPERVISORES = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                    + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_est=" + i_caso + " order by t.sup_ord,t.sup_id";
        }

        P_WHERE = SQL_SUPERVISORES;
        objlstSupervisores = new ListModelList<Supervisor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUPERVISORES);
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt("sup_key"));
                objSupervisor.setSup_id(rs.getString("sup_id"));
                objSupervisor.setSup_apenom(rs.getString("sup_apenom"));
                objSupervisor.setSup_dni(rs.getString("sup_dni"));
                objSupervisor.setSup_direcc(rs.getString("sup_direcc"));
                objSupervisor.setSup_telf(rs.getLong("sup_telf"));
                objSupervisor.setSup_movil(rs.getLong("sup_movil"));
                objSupervisor.setSup_fecnac(rs.getDate("sup_fecnac"));
                objSupervisor.setSup_est(rs.getInt("sup_est"));
                objSupervisor.setSup_usuadd(rs.getString("sup_usuadd"));
                objSupervisor.setSup_fecadd(rs.getTimestamp("sup_fecadd"));
                objSupervisor.setSup_usumod(rs.getString("sup_usumod"));
                objSupervisor.setSup_fecmod(rs.getTimestamp("sup_fecmod"));
                objSupervisor.setSup_pssw(rs.getString("sup_pssw"));
                objSupervisor.setSup_flagcie(rs.getInt("sup_flagcie"));
                objSupervisor.setSup_apoyo(rs.getString("sup_apoyo"));
                objSupervisor.setSup_apoyodes(rs.getString("sup_apoyodes"));
                objSupervisor.setSuc_id(rs.getInt("suc_id"));
                objSupervisor.setEmp_id(rs.getInt("emp_id"));
                objSupervisor.setSup_nomrep(rs.getString("sup_nomrep"));
                objSupervisor.setSup_ord(rs.getInt("sup_ord"));
                objlstSupervisores.add(objSupervisor);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstSupervisores.getSize() + " registro(s)");
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
        return objlstSupervisores;
    }

    public ListModelList<Supervisor> busquedaSupervisores(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA = "";
        /*
         if (i_seleccion == 1) {
         SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id="+objUsuCredential.getCodemp()+" and "
         + "t.suc_id="+objUsuCredential.getCodsuc()+" and t.sup_id like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_id";
         } else if (i_seleccion == 2) {
         SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id="+objUsuCredential.getCodemp()+" and "
         + "t.suc_id="+objUsuCredential.getCodsuc()+" and t.sup_apenom like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_id";
         } else if (i_seleccion == 3) {
         SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id="+objUsuCredential.getCodemp()+" and "
         + "t.suc_id="+objUsuCredential.getCodsuc()+" and t.sup_dni like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_id";
         }*/
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                        + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_est<>0 order by t.sup_ord,t.sup_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_id like '" + s_consulta + "' and t.sup_est<>0 order by t.sup_ord,t.sup_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_apenom like '" + s_consulta + "' and t.sup_est<>0 order by t.sup_ord,t.sup_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_dni like '" + s_consulta + "' and t.sup_est<>0 order by t.sup_ord,t.sup_id";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                        + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_est=" + i_estado + " order by t.sup_ord,t.sup_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_id like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_ord,t.sup_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_apenom like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_ord,t.sup_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listasupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_dni like '" + s_consulta + "' and t.sup_est=" + i_estado + " order by t.sup_ord,t.sup_id";
                }
            }
        }
        objlstSupervisores = new ListModelList<Supervisor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt("sup_key"));
                objSupervisor.setSup_id(rs.getString("sup_id"));
                objSupervisor.setSup_apenom(rs.getString("sup_apenom"));
                objSupervisor.setSup_dni(rs.getString("sup_dni"));
                objSupervisor.setSup_direcc(rs.getString("sup_direcc"));
                objSupervisor.setSup_telf(rs.getLong("sup_telf"));
                objSupervisor.setSup_movil(rs.getLong("sup_movil"));
                objSupervisor.setSup_fecnac(rs.getDate("sup_fecnac"));
                objSupervisor.setSup_est(rs.getInt("sup_est"));
                objSupervisor.setSup_usuadd(rs.getString("sup_usuadd"));
                objSupervisor.setSup_fecadd(rs.getTimestamp("sup_fecadd"));
                objSupervisor.setSup_usumod(rs.getString("sup_usumod"));
                objSupervisor.setSup_fecmod(rs.getTimestamp("sup_fecmod"));
                objSupervisor.setSup_pssw(rs.getString("sup_pssw"));
                objSupervisor.setSup_flagcie(rs.getInt("sup_flagcie"));
                objSupervisor.setSup_apoyo(rs.getString("sup_apoyo"));
                objSupervisor.setSup_apoyodes(rs.getString("sup_apoyodes"));
                objSupervisor.setSuc_id(rs.getInt("suc_id"));
                objSupervisor.setEmp_id(rs.getInt("emp_id"));
                objSupervisor.setSup_nomrep(rs.getString("sup_nomrep"));
                objSupervisor.setSup_ord(rs.getInt("sup_ord"));
                objlstSupervisores.add(objSupervisor);
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
        return objlstSupervisores;
    }

    public ListModelList<Supervisor> cargarSupervisores(int sup_id) throws SQLException {
        String SQL_BUSQUEDA = "select p.sup_key,p.sup_id,p.sup_apenom from tsupervisores p where p.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "p.suc_id=" + objUsuCredential.getCodsuc() + " and p.sup_est=1 and p.sup_id<>" + sup_id + " order by p.sup_key";
        objlstSupervisores = new ListModelList<Supervisor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt(1));
                objSupervisor.setSup_id(rs.getString(2));
                objSupervisor.setSup_apenom(rs.getString(3));
                objlstSupervisores.add(objSupervisor);
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
        return objlstSupervisores;
    }

    public ListModelList<Supervisor> busquedaSupervisoresApeNom(int sup_id, String sup_des) throws SQLException {
        String SQL_BUSQUEDA = "select p.sup_key,p.sup_id,p.sup_apenom from tsupervisores p where p.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "p.suc_id=" + objUsuCredential.getCodsuc() + " and p.sup_est=1 "
                + "and p.sup_id<>" + sup_id + " and p.sup_apenom like '" + sup_des + "'";
        objlstSupervisores = new ListModelList<Supervisor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt(1));
                objSupervisor.setSup_id(rs.getString(2));
                objSupervisor.setSup_apenom(rs.getString(3));
                objlstSupervisores.add(objSupervisor);
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
        return objlstSupervisores;
    }

    public Supervisor existeDNI(String dni) throws SQLException {
        String SQL_SUPERVISORES;

        SQL_SUPERVISORES = "select t.sup_key,t.sup_id,t.sup_apenom ,t.sup_dni from tsupervisores t where t.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_dni=" + dni + " order by t.sup_key";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUPERVISORES);
            objSupervisor = null;
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt("sup_key"));
                objSupervisor.setSup_id(rs.getString("sup_id"));
                objSupervisor.setSup_apenom(rs.getString("sup_apenom"));
                objSupervisor.setSup_dni(rs.getString("sup_dni"));
            }
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
        return objSupervisor;
    }

    public Supervisor getNomSupervisor(int sup_id) throws SQLException {
        String SQL_BUSQUEDA = "select p.sup_key,p.sup_id,p.sup_apenom from tsupervisores p where p.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "p.suc_id=" + objUsuCredential.getCodsuc() + " and p.sup_est=1 and p.sup_id=" + sup_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objSupervisor = null;
            while (rs.next()) {
                objSupervisor = new Supervisor();
                objSupervisor.setSup_key(rs.getInt(1));
                objSupervisor.setSup_id(rs.getString(2));
                objSupervisor.setSup_apenom(rs.getString(3));
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
        return objSupervisor;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC = "";
        int valor = 0;
        if (tipo == 1) {
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tvendedores t, tsupervisores p where t.sup_key = p.sup_key and t.emp_id = p.emp_id "
                    + " and t.suc_id = p.suc_id and t.ven_est<>0 and p.sup_est<>0 and p.suc_id= " + objUsuCredential.getCodsuc()
                    + " and t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_key=" + dato;
        } else {
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tmesas t, tsupervisores p where t.sup_key = p.sup_key and t.emp_id = p.emp_id "
                    + " and t.suc_id = p.suc_id and t.mes_est<>0 and p.sup_est<>0 and p.suc_id= " + objUsuCredential.getCodsuc()
                    + " and t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.sup_key=" + dato;
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Supervisor debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
}
