package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Vendedor;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoVendedores {

    //Instancias de Objetos
    ListModelList<Vendedor> objListModelVendedores;
    Vendedor objVendedor;
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
    private static final Logger LOGGER = Logger.getLogger(DaoVendedores.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(Vendedor objVendedor) throws SQLException {
        String ven_nom = objVendedor.getVen_nom().toUpperCase();
        String ven_ape = objVendedor.getVen_ape().toUpperCase();
        String ven_dni = objVendedor.getVen_dni().toUpperCase();
        long ven_cel = objVendedor.getVen_cel();
        long ven_mov = objVendedor.getVen_mov();
        int tab_id = objVendedor.getTab_id();
        int tab_cod = objVendedor.getTab_cod();
        int sup_key = objVendedor.getSup_key();
        String sup_id = objVendedor.getSup_id();
        String ven_pas = objVendedor.getVen_pas().toUpperCase();
        int ven_diamor = objVendedor.getVen_diamor();
        int ven_gps = objVendedor.getVen_gps();
        int suc_id = objVendedor.getSuc_id();
        int emp_id = objVendedor.getEmp_id();
        String ven_nomrep = objVendedor.getVen_nomrep().toUpperCase();
        int ven_ord = objVendedor.getVen_ord();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_SUPERVISOR = "{call pack_tvendedores.p_insertarVendedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_SUPERVISOR);
            cst.clearParameters();
            cst.setString(1, ven_nom); 
            cst.setString(2, ven_ape);
            cst.setString(3, ven_dni);
            cst.setLong(4, ven_cel);
            cst.setLong(5, ven_mov);
            cst.setInt(6, tab_id);
            cst.setInt(7, tab_cod);
            cst.setInt(8, sup_key);
            cst.setString(9, sup_id);
            cst.setString(10, ven_pas);
            cst.setInt(11, ven_diamor);
            cst.setInt(12, ven_gps);
            cst.setInt(13, suc_id);
            cst.setInt(14, emp_id);
            cst.setString(15, ven_nomrep);
            cst.setInt(16, ven_ord);
            cst.setString(17, objUsuCredential.getCuenta());
            cst.registerOutParameter(18, java.sql.Types.NUMERIC);
            cst.registerOutParameter(19, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(19);
            i_flagErrorBD = cst.getInt(18);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción ");
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

    public ParametrosSalida actualizar(Vendedor objVendedor) throws SQLException {
        int i_ven_key = objVendedor.getVen_key();
        String s_ven_id = objVendedor.getVen_id();
        String ven_nom = objVendedor.getVen_nom().toUpperCase();
        String ven_ape = objVendedor.getVen_ape().toUpperCase();
        String ven_dni = objVendedor.getVen_dni().toUpperCase();
        long ven_cel = objVendedor.getVen_cel();
        long ven_mov = objVendedor.getVen_mov();
        int tab_id = objVendedor.getTab_id();
        int tab_cod = objVendedor.getTab_cod();
        int ven_est = objVendedor.getVen_est();
        int sup_key = objVendedor.getSup_key();
        String sup_id = objVendedor.getSup_id();
        String ven_pas = objVendedor.getVen_pas().toUpperCase();
        int ven_diamor = objVendedor.getVen_diamor();
        int ven_gps = objVendedor.getVen_gps();
        int suc_id = objVendedor.getSuc_id();
        int emp_id = objVendedor.getEmp_id();
        String ven_nomrep = objVendedor.getVen_nomrep().toUpperCase();
        int ven_ord = objVendedor.getVen_ord();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_SUPERVISOR = "{call pack_tvendedores.p_actualizarVendedor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_SUPERVISOR);
            cst.clearParameters();
            cst.setInt(1, i_ven_key);
            cst.setString(2, s_ven_id);
            cst.setString(3, ven_nom);
            cst.setString(4, ven_ape);
            cst.setString(5, ven_dni);
            cst.setLong(6, ven_cel);
            cst.setLong(7, ven_mov);
            cst.setInt(8, tab_id);
            cst.setInt(9, tab_cod);
            cst.setInt(10, ven_est);
            cst.setInt(11, sup_key);
            cst.setString(12, sup_id);
            cst.setString(13, ven_pas);
            cst.setInt(14, ven_diamor);
            cst.setInt(15, ven_gps);
            cst.setString(16, objUsuCredential.getCuenta());
            cst.setInt(17, emp_id);
            cst.setInt(18, suc_id);
            cst.setString(19, ven_nomrep);
            cst.setInt(20, ven_ord);
            cst.registerOutParameter(21, java.sql.Types.NUMERIC);
            cst.registerOutParameter(22, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(22);
            i_flagErrorBD = cst.getInt(21);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código ");
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

    public ParametrosSalida eliminar(Vendedor objVendedor) throws SQLException {
        int ven_key = objVendedor.getVen_key();
        String ven_id = objVendedor.getVen_id();
        int suc_id = objVendedor.getSuc_id();
        int emp_id = objVendedor.getEmp_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_SUPERVISOR = "{call pack_tvendedores.p_eliminarVendedor(?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_SUPERVISOR);
            cst.clearParameters();
            cst.setInt(1, ven_key);
            cst.setString(2, ven_id);
            cst.setInt(3, suc_id);
            cst.setInt(4, emp_id);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código ");
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
    public ListModelList<Vendedor> listaVendedores(int i_caso) throws SQLException {
        String SQL_VENDEDORES;
        if (i_caso == 0) {
            SQL_VENDEDORES = " select *"
                    /*+ "t.ven_key, t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , "
                    + "t.tab_subdes , t.ven_mov , t.ven_dni , t.ven_est , t.sup_key, t.sup_id , "
                    + "t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , t.ven_nomrep , "
                    + "t.ven_ord , t.ven_usuadd , t.ven_fecadd , t.ven_usumod , t.ven_fecmod , t.emp_id , t.suc_id"*/
                    + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <> '" + i_caso + "' order by t.ven_ord,t.ven_id";
        } else {
            SQL_VENDEDORES = " select *"
                    /*+ "t.ven_key, t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , "
                    + "t.tab_subdes , t.ven_mov , t.ven_dni ,  t.ven_est, t.sup_key, t.sup_id , "
                    + "t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , t.ven_nomrep , "
                    + "t.ven_ord , t.ven_usuadd , t.ven_fecadd , t.ven_usumod , t.ven_fecmod , t.emp_id , t.suc_id "*/
                    + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_caso + "'  order by t.ven_ord,t.ven_id";
        }
        P_WHERE = SQL_VENDEDORES;
        objListModelVendedores = new ListModelList<Vendedor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VENDEDORES);
            while (rs.next()) {
                objVendedor = new Vendedor();
                objVendedor.setVen_key(rs.getInt("ven_key"));
                objVendedor.setVen_id(rs.getString("ven_id"));
                objVendedor.setVen_ape(rs.getString("ven_ape"));
                objVendedor.setVen_nom(rs.getString("ven_nom"));
                objVendedor.setTab_id(rs.getInt("tab_id"));
                objVendedor.setTab_subdir(rs.getString("tab_subdir"));
                objVendedor.setCanal(rs.getString("tab_subdes"));
                objVendedor.setVen_mov(rs.getLong("ven_mov"));
                objVendedor.setVen_dni(rs.getString("ven_dni"));
                objVendedor.setVen_est(rs.getInt("ven_est"));
                objVendedor.setSup_key(rs.getInt("sup_key"));
                objVendedor.setSup_id(rs.getString("sup_id"));
                objVendedor.setSup_des(rs.getString("sup_apenom"));
                objVendedor.setVen_gps(rs.getInt("ven_gps"));
                objVendedor.setVen_diamor(rs.getInt("ven_diamor"));
                objVendedor.setVen_pas(rs.getString("ven_pas"));
                objVendedor.setVen_cel(rs.getLong("ven_cel"));
                objVendedor.setVen_nomrep(rs.getString("ven_nomrep"));
                objVendedor.setVen_ord(rs.getInt("ven_ord"));
                objVendedor.setEmp_id(rs.getInt("emp_id"));
                objVendedor.setSuc_id(rs.getInt("suc_id"));
                objVendedor.setMesa_id(rs.getString("mes_id"));
                objVendedor.setVen_usuadd(rs.getString("ven_usuadd"));
                objVendedor.setVen_fecadd(rs.getTimestamp("ven_fecadd"));
                objVendedor.setVen_usumod(rs.getString("ven_usumod"));
                objVendedor.setVen_fecmod(rs.getTimestamp("ven_fecmod"));
                objListModelVendedores.add(objVendedor);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListModelVendedores.getSize() + " registro(s)");
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
        return objListModelVendedores;
    }

    public ListModelList<Vendedor> busquedaVendedores(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA = "";
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "  select *"
                        /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir, t.tab_subdes , t.ven_mov , "
                        + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                        + "t.ven_nomrep , t.ven_ord ,ven_usuadd,ven_fecadd,ven_usumod,ven_fecmod , emp_id, suc_id "*/
                        + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc()
                        + "' and t.ven_est <>0  order by t.ven_ord , t.ven_id";
            } else {
                //Busqueda por codigo del vendedor
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir, t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord ,ven_usuadd,ven_fecadd,ven_usumod,ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <>0 and t.ven_id like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                } //Busqueda por apellidos del vendedor
                else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <>0 and t.ven_ape like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                } //Busqueda por nombres del vendedor
                else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord ,  ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <>0 and t.ven_nom like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                } //Busqueda por canal de venta del vendedor
                else if (i_seleccion == 4) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <>0 and t.tab_subdir||'-'||t.tab_subdes like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                } //Busqueda por numero movil del vendedor
                else if (i_seleccion == 5) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod, emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est <>0 and t.ven_mov like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                } //Busqueda por DNI del vendedor
                else if (i_seleccion == 6) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod, emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_dni like '" + s_consulta + "' order by t.ven_ord,t.ven_id";
                } //Busqueda por Supervisor del vendedor
                else if (i_seleccion == 7) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov ,"
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod, emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.mes_id||'-'||t.sup_apenom like '" + s_consulta + "' order by t.ven_ord,t.ven_id";
                } else if (i_seleccion == 8) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod, emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and (t.ven_ape ||', '|| t.ven_nom) like '" + s_consulta + "'  order by t.ven_ord,t.ven_id";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "  select *"
                        /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir, t.tab_subdes , t.ven_mov , "
                        + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                        + "t.ven_nomrep , t.ven_ord ,ven_usuadd,ven_fecadd,ven_usumod,ven_fecmod, emp_id, suc_id  "*/
                        + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' order by t.ven_ord , t.ven_id  ";
            } else {
                //Busqueda por codigo del vendedor
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir, t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord ,ven_usuadd,ven_fecadd,ven_usumod,ven_fecmod, emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_id like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por apellidos del vendedor
                else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id   "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_ape like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por nombres del vendedor
                else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord ,  ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id  "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_nom like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por canal de venta del vendedor
                else if (i_seleccion == 4) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.tab_subdir||'-'||t.tab_subdes like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por numero movil del vendedor
                else if (i_seleccion == 5) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_mov like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por DNI del vendedor
                else if (i_seleccion == 6) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.ven_dni like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } //Busqueda por Supervisor del vendedor
                else if (i_seleccion == 7) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov ,"
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and t.mes_id||'-'||t.sup_apenom like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                } else if (i_seleccion == 8) {
                    SQL_BUSQUEDA = "  select *"
                            /*+ "t.ven_key,t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                            + "t.ven_dni ,  t.ven_est  , t.sup_key,t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , t.ven_cel , "
                            + "t.ven_nomrep , t.ven_ord , ven_usuadd , ven_fecadd , ven_usumod, ven_fecmod , emp_id, suc_id "*/
                            + "from v_listavendedores t where t.emp_id = '" + objUsuCredential.getCodemp() + "' and t.suc_id = '" + objUsuCredential.getCodsuc() + "' and t.ven_est = '" + i_estado + "' and (t.ven_ape ||', '|| t.ven_nom) like '" + s_consulta + "'  order by t.ven_ord , t.ven_id  ";
                }
            }
        }
        objListModelVendedores = new ListModelList<Vendedor>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objVendedor = new Vendedor();
                objVendedor.setVen_key(rs.getInt("ven_key"));
                objVendedor.setVen_id(rs.getString("ven_id"));
                objVendedor.setVen_ape(rs.getString("ven_ape"));
                objVendedor.setVen_nom(rs.getString("ven_nom"));
                objVendedor.setTab_id(rs.getInt("tab_id"));
                objVendedor.setTab_subdir(rs.getString("tab_subdir"));
                objVendedor.setCanal(rs.getString("tab_subdes"));
                objVendedor.setVen_mov(rs.getLong("ven_mov"));
                objVendedor.setVen_dni(rs.getString("ven_dni"));
                objVendedor.setVen_est(rs.getInt("ven_est"));
                objVendedor.setSup_key(rs.getInt("sup_key"));
                objVendedor.setSup_id(rs.getString("sup_id"));
                objVendedor.setSup_des(rs.getString("sup_apenom"));
                objVendedor.setVen_gps(rs.getInt("ven_gps"));
                objVendedor.setVen_diamor(rs.getInt("ven_diamor"));
                objVendedor.setVen_pas(rs.getString("ven_pas"));
                objVendedor.setVen_cel(rs.getLong("ven_cel"));
                objVendedor.setVen_nomrep(rs.getString("ven_nomrep"));
                objVendedor.setVen_ord(rs.getInt("ven_ord"));
                objVendedor.setEmp_id(rs.getInt("emp_id"));
                objVendedor.setSuc_id(rs.getInt("suc_id"));
                objVendedor.setMesa_id(rs.getString("mes_id"));
                objVendedor.setVen_usuadd(rs.getString("ven_usuadd"));
                objVendedor.setVen_fecadd(rs.getTimestamp("ven_fecadd"));
                objVendedor.setVen_usumod(rs.getString("ven_usumod"));
                objVendedor.setVen_fecmod(rs.getTimestamp("ven_fecmod"));
                objListModelVendedores.add(objVendedor);
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
        return objListModelVendedores;
    }

    public Vendedor getNomVendedor(int ven_id) throws SQLException {
        String SQL_BUSQUEDA = "select v.ven_key, v.ven_id , v.ven_ape , v.ven_nom  from tvendedores v where v.ven_est <> 0 and v.ven_id =" + ven_id
                + " and v.emp_id=" + objUsuCredential.getCodemp() + " and v.suc_id=" + objUsuCredential.getCodsuc();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objVendedor = null;
            while (rs.next()) {
                objVendedor = new Vendedor();
                objVendedor.setVen_key(rs.getInt("ven_key"));
                objVendedor.setVen_id(rs.getString("ven_id"));
                objVendedor.setVen_ape(rs.getString("ven_ape"));
                objVendedor.setVen_nom(rs.getString("ven_nom"));
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
        return objVendedor;
    }

    public Vendedor existeDNI(String dni) throws SQLException {
        String SQL_SUPERVISORES;
        SQL_SUPERVISORES = "select v.ven_key, v.ven_id , v.ven_ape , v.ven_nom, v.ven_dni  from tvendedores v where v.emp_id=" + objUsuCredential.getCodemp() + " and "
                + "v.suc_id=" + objUsuCredential.getCodsuc() + " and v.ven_dni=" + dni + " order by v.ven_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_SUPERVISORES);
            objVendedor = null;
            while (rs.next()) {
                objVendedor = new Vendedor();
                objVendedor.setVen_key(rs.getInt("ven_key"));
                objVendedor.setVen_id(rs.getString("ven_id"));
                objVendedor.setVen_ape(rs.getString("ven_ape"));
                objVendedor.setVen_nom(rs.getString("ven_nom"));
                objVendedor.setVen_dni(rs.getString("ven_dni"));

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
        return objVendedor;
    }

    public Vendedor buscarVendedor(int ven_id) throws SQLException {
        String SQL_BUSQUEDA = "select *"
                /*+ "t.ven_key, t.ven_id, t.ven_ape , t.ven_nom , t.tab_id, t.tab_subdir , t.tab_subdes , t.ven_mov , "
                + "t.ven_dni ,  t.ven_est, t.sup_key, t.sup_id , t.sup_apenom , t.ven_gps , t.ven_diamor , t.ven_pas , "
                + "t.ven_cel , t.ven_nomrep , t.ven_ord "*/
                + " from v_listavendedores t where t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.ven_est=1 and t.ven_id=" + ven_id;
        objVendedor = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objVendedor = new Vendedor();
                objVendedor.setVen_key(rs.getInt("ven_key"));
                objVendedor.setVen_id(rs.getString("ven_id"));
                objVendedor.setVen_ape(rs.getString("ven_ape"));
                objVendedor.setVen_nom(rs.getString("ven_nom"));
                objVendedor.setTab_id(rs.getInt("tab_id"));
                objVendedor.setTab_subdir(rs.getString("tab_subdir"));
                objVendedor.setCanal(rs.getString("tab_subdes"));
                objVendedor.setVen_mov(rs.getLong("ven_mov"));
                objVendedor.setVen_dni(rs.getString("ven_dni"));
                objVendedor.setVen_est(rs.getInt("ven_est"));
                objVendedor.setSup_key(rs.getInt("sup_key"));
                objVendedor.setSup_id(rs.getString("sup_id"));
                objVendedor.setSup_des(rs.getString("sup_apenom"));
                objVendedor.setVen_gps(rs.getInt("ven_gps"));
                objVendedor.setVen_diamor(rs.getInt("ven_diamor"));
                objVendedor.setVen_pas(rs.getString("ven_pas"));
                objVendedor.setVen_cel(rs.getLong("ven_cel"));
                objVendedor.setVen_nomrep(rs.getString("ven_nomrep"));
                objVendedor.setVen_ord(rs.getInt("ven_ord"));
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
        return objVendedor;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC = "";
        int valor = 0;
        if (tipo == 1) {
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tsupervisores p , tvendedores t "
                    + "where t.sup_key = p.sup_key and t.suc_id=p.suc_id  and t.emp_id = p.emp_id "
                    + " and t.ven_est<>0 and p.sup_est<>0 "
                    + "and p.suc_id= " + objUsuCredential.getCodsuc() + " and p.emp_id =" + objUsuCredential.getCodemp() + " and t.ven_key=" + dato + " ";
        } else if (tipo == 2) {
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tzonas p , tvendedores t "
                    + "where t.zon_idven = p.ven_key and t.suc_id=p.suc_id  and t.emp_id = p.emp_id "
                    + " and t.ven_est<>0 and p.zon_est<>0 "
                    + "and p.suc_id= " + objUsuCredential.getCodsuc() + " and p.emp_id =" + objUsuCredential.getCodemp() + " and p.zon_idven=" + dato + " ";
        }

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Vendedores debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
