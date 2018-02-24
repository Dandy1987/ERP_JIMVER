package org.me.gj.controller.logistica.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.zkoss.zul.ListModelList;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.apache.log4j.Logger;
import org.me.gj.model.logistica.mantenimiento.Almacenes;

public class DaoAlmacenes {

    //Instancias de Objetos
    ListModelList<Almacenes> objlstAlmacenes;
    Almacenes objAlmacen;
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
    private static final Logger LOGGER = Logger.getLogger(DaoAlmacenes.class);

    //Eventos Primarios - Transaccionales
    public String insertar(Almacenes objAlmacen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
        String s_almdes = objAlmacen.getAlm_des();
        String s_almdirecc = objAlmacen.getAlm_direcc();
        double i_almalt = objAlmacen.getAlm_alt();
        double i_almanc = objAlmacen.getAlm_anc();
        double i_almlar = objAlmacen.getAlm_lar();
        String s_almnomrep = objAlmacen.getAlm_nomrep();
        int i_almord = objAlmacen.getAlm_ord();
        String s_almusuadd = objAlmacen.getAlm_usuadd();
        int i_empid = objAlmacen.getEmp_id();
        int i_sucid = objAlmacen.getSuc_id();
        int i_almtip = objAlmacen.getAlm_tip();
        int i_almvta = objAlmacen.getAlm_vta();
        int i_almcom = objAlmacen.getAlm_com();
        int i_almdef = objAlmacen.getAlm_def();
        String SQL_INSERTAR_ALMACEN = "{call pack_talmacenes.p_insertarAlmacen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_ALMACEN);
            cst.clearParameters();
            cst.setString(1, s_almdes);
            cst.setString(2, s_almdirecc);
            cst.setDouble(3, i_almalt);
            cst.setDouble(4, i_almanc);
            cst.setDouble(5, i_almlar);
            cst.setString(6, s_almnomrep);
            cst.setInt(7, i_almord);
            cst.setString(8, s_almusuadd);
            cst.setDouble(9, i_empid);
            cst.setDouble(10, i_sucid);
            cst.setDouble(11, i_almtip);
            cst.setInt(12, i_almvta);
            cst.setInt(13, i_almcom);
            cst.setInt(14, i_almdef);
            cst.registerOutParameter(15, java.sql.Types.NUMERIC);
            cst.registerOutParameter(16, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(16);
            i_flagErrorBD = cst.getInt(15);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_almdes);
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

    public String eliminar(Almacenes objAlmacen) throws SQLException {
        String SQL_ELIMINAR_ALMACEN = "{call pack_talmacenes.p_eliminarAlmacen(?,?,?,?,?)}";
//        int i_almkey = objAlmacen.getAlm_key();
        String i_almkey = objAlmacen.getAlm_key();
        int i_empid = objAlmacen.getEmp_id();
        int i_sucid = objAlmacen.getSuc_id();
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_ALMACEN);
            cst.clearParameters();
            cst.setString(1, i_almkey);
            cst.setInt(2, i_empid);
            cst.setInt(3, i_sucid);
            cst.registerOutParameter(4, java.sql.Types.NUMERIC);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(5);
            i_flagErrorBD = cst.getInt(4);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objAlmacen.getAlm_id());
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

    public String modificar(Almacenes objAlmacen) throws SQLException {
        //Recupera en variables los valores de la tabla seteada
//        int i_almkey = objAlmacen.getAlm_key();
        String i_almkey = objAlmacen.getAlm_key();
        int i_almest = objAlmacen.getAlm_est();
        String s_almdes = objAlmacen.getAlm_des();
        String s_almdirecc = objAlmacen.getAlm_direcc();
        double i_almalt = objAlmacen.getAlm_alt();
        double i_almanc = objAlmacen.getAlm_anc();
        double i_almlar = objAlmacen.getAlm_lar();
        String s_almnomrep = objAlmacen.getAlm_nomrep();
        int i_almord = objAlmacen.getAlm_ord();
        String s_almusumod = objAlmacen.getAlm_usumod();
        int i_empid = objAlmacen.getEmp_id();
        int i_sucid = objAlmacen.getSuc_id();
        int i_almtip = objAlmacen.getAlm_tip();
        int i_almvta = objAlmacen.getAlm_vta();
        int i_almcom = objAlmacen.getAlm_com();
        int i_almdef = objAlmacen.getAlm_def();
        String SQL_ACTUALIZAR_ALMACEN = "{call pack_talmacenes.p_actualizarAlmacen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_ALMACEN);
            cst.clearParameters();
            cst.setString(1, i_almkey);
            cst.setInt(2, i_almest);
            cst.setString(3, s_almdes);
            cst.setString(4, s_almdirecc);
            cst.setDouble(5, i_almalt);
            cst.setDouble(6, i_almanc);
            cst.setDouble(7, i_almlar);
            cst.setString(8, s_almnomrep);
            cst.setDouble(9, i_almord);
            cst.setString(10, s_almusumod);
            cst.setDouble(11, i_empid);
            cst.setInt(12, i_sucid);
            cst.setInt(13, i_almtip);
            cst.setInt(14, i_almvta);
            cst.setInt(15, i_almcom);
            cst.setInt(16, i_almdef);
            cst.registerOutParameter(17, java.sql.Types.NUMERIC);
            cst.registerOutParameter(18, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(18);
            i_flagErrorBD = cst.getInt(17);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objAlmacen.getAlm_id());
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
    public ListModelList<Almacenes> lstAlmacenes(int estado) throws SQLException {
        String SQL_ALMACENES;
        if (estado == 0) {
            SQL_ALMACENES = "select * from v_listalmacenes t where t.alm_est<>" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.alm_key";
        } else {
            SQL_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + estado + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' order by t.alm_key";
        }
        P_WHERE = SQL_ALMACENES;
        objlstAlmacenes = new ListModelList<Almacenes>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ALMACENES);
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_id(rs.getString("alm_id"));
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objAlmacen.setAlm_est(rs.getInt("alm_est"));
                objAlmacen.setAlm_direcc(rs.getString("alm_direcc"));
                objAlmacen.setAlm_alt(rs.getDouble("alm_alt"));
                objAlmacen.setAlm_anc(rs.getDouble("alm_anc"));
                objAlmacen.setAlm_lar(rs.getDouble("alm_lar"));
                objAlmacen.setAlm_nomrep(rs.getString("alm_nomrep"));
                objAlmacen.setAlm_ord(rs.getInt("alm_ord"));
                objAlmacen.setAlm_usuadd(rs.getString("alm_usuadd"));
                objAlmacen.setAlm_fecadd(rs.getTimestamp("alm_fecadd"));
                objAlmacen.setAlm_usumod(rs.getString("alm_usumod"));
                objAlmacen.setAlm_fecmod(rs.getTimestamp("alm_fecmod"));
                objAlmacen.setSuc_id(rs.getInt("suc_id"));
                objAlmacen.setEmp_id(rs.getInt("emp_id"));
                objAlmacen.setAlm_tip(rs.getInt("alm_tip"));
                objAlmacen.setAlm_tipdes(rs.getString("tab_subdes"));
                objAlmacen.setAlm_vta(rs.getInt("alm_vta"));
                objAlmacen.setAlm_com(rs.getInt("alm_com"));
                objAlmacen.setAlm_def(rs.getInt("alm_def"));
                objlstAlmacenes.add(objAlmacen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAlmacenes.getSize() + " registro(s)");
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
        return objlstAlmacenes;
    }

    public ListModelList<Almacenes> listaDescAlmacenOri(int emp_id, int suc_id) throws SQLException {
        String sql = "select t.alm_key,t.alm_des from talmacenes t where t.alm_est=1 and "
                + "t.emp_id=" + emp_id + " and t.suc_id=" + suc_id;
        objlstAlmacenes = new ListModelList<Almacenes>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objlstAlmacenes.add(objAlmacen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAlmacenes.getSize() + " registro(s)");
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
        return objlstAlmacenes;
    }

    public ListModelList<Almacenes> almacenDefectoCompra(int emp_id, int suc_id) throws SQLException {
        String sql = "select t.alm_key,t.alm_des ,t.alm_id from talmacenes t where t.alm_est = 1 and "
                + "t.emp_id = " + emp_id + " and t.suc_id = " + suc_id + " and t.alm_com = 1 ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objlstAlmacenes = new ListModelList<Almacenes>();
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objAlmacen.setAlm_id(rs.getString("alm_id"));
                objlstAlmacenes.add(objAlmacen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAlmacenes.getSize() + " registro(s)");
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
        return objlstAlmacenes;
    }

    public ListModelList<Almacenes> almacenDefecto(int emp_id, int suc_id) throws SQLException {
        String sql = "select t.alm_key,t.alm_des , t.alm_direcc , t.alm_id from talmacenes t where t.alm_est = 1 and "
                + "t.emp_id = " + emp_id + " and t.suc_id = " + suc_id + " and t.alm_def = 1 ";
        objlstAlmacenes = new ListModelList<Almacenes>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objAlmacen.setAlm_direcc(rs.getString("alm_direcc"));
                objAlmacen.setAlm_id(rs.getString("alm_id"));
                objlstAlmacenes.add(objAlmacen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAlmacenes.getSize() + " registro(s)");
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
        return objlstAlmacenes;
    }

    public ListModelList<Almacenes> listaDescAlmacenDes(int emp_id, int suc_id) throws SQLException {
        String sql = "select t.alm_key,t.alm_des from talmacenes t where t.alm_est=1 and "
                + "t.emp_id=" + emp_id + " and t.suc_id=" + suc_id;
        objlstAlmacenes = new ListModelList<Almacenes>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objlstAlmacenes.add(objAlmacen);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstAlmacenes.getSize() + " registro(s)");
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
        return objlstAlmacenes;
    }

    public ListModelList<Almacenes> busquedaAlmacenes(String dato, int indice, int i_estado) throws SQLException {
        String SQL_BUSQUEDA_ALMACENES = ""; 
        if (i_estado == 3) {
            if (indice == 0) {
                SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_est<>0 order by t.alm_key";
            } else {
                if (indice == 1) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_id like '" + dato + "'and t.alm_est<>0  order by t.alm_key";
                } else if (indice == 2) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_des like '" + dato + "'and t.alm_est<>0  order by t.alm_key";
                } else if (indice == 3) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_direcc like '" + dato + "'and t.alm_est<>0  order by t.alm_key";
                } else if (indice == 4) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tab_subdes like '" + dato + "'and t.alm_est<>0  order by t.alm_key";
                } else if (indice == 5) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_alt like '" + dato.replace(".", ",") + "' and t.alm_est<>0 order by t.alm_key";
                } else if (indice == 6) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_anc like '" + dato.replace(".", ",") + "'and t.alm_est<>0  order by t.alm_key";
                } else if (indice == 7) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_lar like '" + dato.replace(".", ",") + "'and t.alm_est<>0  order by t.alm_key";
                }
            }
        } else {
            if (indice == 0) {
                SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                        + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "'and t.alm_est<>0 order by t.alm_key";
            } else {
                if (indice == 1) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_id like '" + dato + "' order by t.alm_key";
                } else if (indice == 2) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_des like '" + dato + "' order by t.alm_key";
                } else if (indice == 3) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_direcc like '" + dato + "' order by t.alm_key";
                } else if (indice == 4) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.tab_subdes like '" + dato + "' order by t.alm_key";
                } else if (indice == 5) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_alt like '" + dato.replace(".", ",") + "' order by t.alm_key";
                } else if (indice == 6) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_anc like '" + dato.replace(".", ",") + "' order by t.alm_key";
                } else if (indice == 7) {
                    SQL_BUSQUEDA_ALMACENES = "select * from v_listalmacenes t where t.alm_est=" + i_estado
                            + " and t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' and t.alm_lar like '" + dato.replace(".", ",") + "' order by t.alm_key";
                }
            }
        }
        P_WHERE = SQL_BUSQUEDA_ALMACENES;
        objlstAlmacenes = new ListModelList<Almacenes>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_ALMACENES);
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key")); // Corregido
                objAlmacen.setAlm_id(rs.getString("alm_id"));
                objAlmacen.setAlm_des(rs.getString("alm_des"));
                objAlmacen.setAlm_est(rs.getInt("alm_est"));
                objAlmacen.setAlm_direcc(rs.getString("alm_direcc"));
                objAlmacen.setAlm_alt(rs.getDouble("alm_alt"));
                objAlmacen.setAlm_anc(rs.getDouble("alm_anc"));
                objAlmacen.setAlm_lar(rs.getDouble("alm_lar"));
                objAlmacen.setAlm_nomrep(rs.getString("alm_nomrep"));
                objAlmacen.setAlm_ord(rs.getInt("alm_ord"));
                objAlmacen.setAlm_usuadd(rs.getString("alm_usuadd"));
                objAlmacen.setAlm_fecadd(rs.getTimestamp("alm_fecadd"));
                objAlmacen.setAlm_usumod(rs.getString("alm_usumod"));
                objAlmacen.setAlm_fecmod(rs.getTimestamp("alm_fecmod"));
                objAlmacen.setSuc_id(rs.getInt("suc_id"));
                objAlmacen.setEmp_id(rs.getInt("emp_id"));
                objAlmacen.setAlm_tip(rs.getInt("alm_tip"));
                objAlmacen.setAlm_tipdes(rs.getString("tab_subdes"));
                objAlmacen.setAlm_vta(rs.getInt("alm_vta"));
                objAlmacen.setAlm_com(rs.getInt("alm_com"));
                objAlmacen.setAlm_def(rs.getInt("alm_def"));
                objlstAlmacenes.add(objAlmacen);
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
        return objlstAlmacenes;
    }

    public Almacenes getNomAlmacenes(int alm_id) throws SQLException {
        String SQL_BUSQUEDA = "select a.alm_key, a.alm_id , a.alm_des from talmacenes a where a.alm_est <> 0 and a.alm_key =" + alm_id;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            objAlmacen = null;
            while (rs.next()) {
                objAlmacen = new Almacenes();
                objAlmacen.setAlm_key(rs.getString("alm_key"));// Corregido
                objAlmacen.setAlm_id(rs.getString("alm_id"));
                objAlmacen.setAlm_des(rs.getString("alm_des"));
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
        return objAlmacen;
    }
    
    public String validaMovimientos(Almacenes objAlmacen) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_talmacenes.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, objAlmacen.getAlm_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del almacen " + objAlmacen.getAlm_id());
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
    
    
    
    public int busquedaExistenciaDefault() throws SQLException {
        String SQL_VERIFICA_DEF;
        int valor = 0;

        SQL_VERIFICA_DEF = "select nvl(to_number(count(*)),0) from talmacenes a "
                + " where a.emp_id=" + objUsuCredential.getCodemp() + " and a.suc_id=" + objUsuCredential.getCodsuc()
                + " and a.alm_def = 1 "
                + " order by a.alm_key";
        
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DEF);

            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {

            Messagebox.show(" Error de Validación de Ubicacion debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
