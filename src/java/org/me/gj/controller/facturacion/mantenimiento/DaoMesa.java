package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Mesa;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoMesa {

    //Instancias de Objetos
    ListModelList<Mesa> objlstMesa;
    Mesa objMesa;
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
    public ParametrosSalida insertar(Mesa objMesa) throws SQLException {
        int i_supkey = objMesa.getSup_key();
        String s_supid = objMesa.getSup_id();
        int i_mescanalid = objMesa.getMes_canalid();
        int i_empid = objMesa.getEmp_id();
        int i_sucid = objMesa.getSuc_id();
        int i_mesord = objMesa.getMes_ord();
        String s_mesusuadd = objMesa.getMes_usuadd();
        String s_mesnomrep = objMesa.getMes_nomrep();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_MESA = "{call pack_tmesas.p_insertarMesa(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MESA);
            cst.clearParameters();
            cst.setInt(1, i_supkey);
            cst.setString(2, s_supid);
            cst.setInt(3, i_mescanalid);
            cst.setInt(4, i_empid);
            cst.setInt(5, i_sucid);
            cst.setLong(6, i_mesord);
            cst.setString(7, s_mesusuadd);
            cst.setString(8, s_mesnomrep);
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Mesa para el canal " + i_mescanalid);
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

    public ParametrosSalida actualizar(Mesa objMesa) throws SQLException {
        int i_meskey = objMesa.getMes_key();
        int i_canalid = objMesa.getMes_canalid();
        String s_mesid = objMesa.getMes_id();
        int i_supkey = objMesa.getSup_key();
        String s_supid = objMesa.getSup_id();
        int i_mesest = objMesa.getMes_est();
        int i_mesord = objMesa.getMes_ord();
        String s_mesnomrep = objMesa.getMes_nomrep();
        String s_mesusumod = objMesa.getMes_usumod();
        int i_empid = objMesa.getEmp_id();
        int i_sucid = objMesa.getSuc_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ACTUALIZAR_MESA = "{call pack_tmesas.p_actualizarMesa(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_MESA);
            cst.clearParameters();
            cst.setInt(1, i_meskey);
            cst.setInt(2, i_canalid);
            cst.setString(3, s_mesid);
            cst.setInt(4, i_supkey);
            cst.setString(5, s_supid);
            cst.setInt(6, i_mesest);
            cst.setInt(7, i_mesord);
            cst.setString(8, s_mesnomrep);
            cst.setString(9, s_mesusumod);
            cst.setInt(10, i_empid);
            cst.setInt(11, i_sucid);
            cst.registerOutParameter(12, java.sql.Types.NUMERIC);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(13);
            i_flagErrorBD = cst.getInt(12);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objMesa.getMes_id());
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

    public ParametrosSalida eliminar(Mesa objMesa) throws SQLException {
        int i_canalid = objMesa.getMes_canalid();
        int i_meskey = objMesa.getMes_key();
        String i_mesid = objMesa.getMes_id();
        int i_empid = objMesa.getEmp_id();
        int i_sucid = objMesa.getSuc_id();
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_MESA = "{call pack_tmesas.p_eliminarMesa(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_MESA);
            cst.clearParameters();
            cst.setInt(1, i_canalid);
            cst.setInt(2, i_meskey);
            cst.setString(3, i_mesid);
            cst.setInt(4, i_empid);
            cst.setInt(5, i_sucid);
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objMesa.getMes_key());
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
    public ListModelList<Mesa> listaMesas(int i_caso) throws SQLException {
        String SQL_MESAS;
        if (i_caso == 0) {
            SQL_MESAS = "select * from v_listamesas t where t.mes_est<>" + i_caso + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
        } else {
            SQL_MESAS = "select * from v_listamesas t where t.mes_est=" + i_caso + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
        }
        P_WHERE = SQL_MESAS;
        objlstMesa = new ListModelList<Mesa>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_MESAS);
            while (rs.next()) {
                objMesa = new Mesa();
                objMesa.setMes_key(rs.getInt("mes_key"));
                objMesa.setMes_id(rs.getString("mes_id"));
                objMesa.setMes_des(rs.getString("mes_des"));
                objMesa.setMes_canalid(rs.getInt("mes_canalid"));
                objMesa.setMes_canaldes(rs.getString("mes_canaldes"));
                objMesa.setMes_nomrep(rs.getString("mes_nomrep"));
                objMesa.setMes_ord(rs.getInt("mes_ord"));
                objMesa.setMes_usuadd(rs.getString("mes_usuadd"));
                objMesa.setMes_fecadd(rs.getTimestamp("mes_fecadd"));
                objMesa.setMes_usumod(rs.getString("mes_usumod"));
                objMesa.setMes_fecmod(rs.getTimestamp("mes_fecmod"));
                objMesa.setSuc_id(rs.getInt("suc_id"));
                objMesa.setEmp_id(rs.getInt("emp_id"));
                objMesa.setSup_key(rs.getInt("sup_key"));
                objMesa.setSup_id(rs.getString("sup_id"));
                objMesa.setSup_apenom(rs.getString("sup_apenom"));
                objMesa.setMes_est(rs.getInt("mes_est"));
                objlstMesa.add(objMesa);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstMesa.getSize() + " registro(s)");
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
        return objlstMesa;
    }

    public ListModelList<Mesa> busquedaMesas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA = "";
        /*if (i_seleccion == 1) {
         SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est="+i_estado+" and t.tab_subdes like '"+s_consulta+"' and t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" order by t.mes_key";
         } else if (i_seleccion == 2) {
         SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est="+i_estado+" and t.sup_apenom like '"+s_consulta+"' and t.emp_id="+objUsuCredential.getCodemp()+" and t.suc_id="+objUsuCredential.getCodsuc()+" order by t.mes_key";
         }*/
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est<>0 and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est<>0 and t.mes_des like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est<>0 and t.mes_canaldes like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est<>0 and t.sup_apenom like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est=" + i_estado + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est=" + i_estado + " and t.mes_des like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est=" + i_estado + " and t.mes_canaldes like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listamesas t where t.mes_est=" + i_estado + " and t.sup_apenom like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.mes_key";
                }
            }
        }
        objlstMesa = new ListModelList<Mesa>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objMesa = new Mesa();
                objMesa.setMes_key(rs.getInt("mes_key"));
                objMesa.setMes_id(rs.getString("mes_id"));
                objMesa.setMes_des(rs.getString("mes_des"));
                objMesa.setMes_canalid(rs.getInt("mes_canalid"));
                objMesa.setMes_canaldes(rs.getString("mes_canaldes"));
                objMesa.setMes_nomrep(rs.getString("mes_nomrep"));
                objMesa.setMes_ord(rs.getInt("mes_ord"));
                objMesa.setMes_usuadd(rs.getString("mes_usuadd"));
                objMesa.setMes_fecadd(rs.getTimestamp("mes_fecadd"));
                objMesa.setMes_usumod(rs.getString("mes_usumod"));
                objMesa.setMes_fecmod(rs.getTimestamp("mes_fecmod"));
                objMesa.setSuc_id(rs.getInt("suc_id"));
                objMesa.setEmp_id(rs.getInt("emp_id"));
                objMesa.setSup_key(rs.getInt("sup_key"));
                objMesa.setSup_id(rs.getString("sup_id"));
                objMesa.setSup_apenom(rs.getString("sup_apenom"));
                objMesa.setMes_est(rs.getInt("mes_est"));
                objlstMesa.add(objMesa);
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
        return objlstMesa;
    }
    
    public ListModelList<Mesa> busquedaLovMesas(String buscar) throws SQLException{
     String SQL_LOVMESAS = "select * from v_listamesas t where t.mes_est = '1' and   t.sup_apenom like '" + buscar+ "'";
    objlstMesa = null;
    objlstMesa = new ListModelList<Mesa>();
    try{
       con = new ConectaBD().conectar();
       st = con.createStatement();
       rs = st.executeQuery(SQL_LOVMESAS);
       while (rs.next()){
       objMesa = new Mesa();
                objMesa.setMes_key(rs.getInt("mes_key"));
                objMesa.setMes_id(rs.getString("mes_id"));
                objMesa.setMes_des(rs.getString("mes_des"));
                objMesa.setMes_canalid(rs.getInt("mes_canalid"));
                objMesa.setMes_canaldes(rs.getString("mes_canaldes"));
                objMesa.setMes_nomrep(rs.getString("mes_nomrep"));
                objMesa.setMes_ord(rs.getInt("mes_ord"));
                objMesa.setMes_usuadd(rs.getString("mes_usuadd"));
                objMesa.setMes_fecadd(rs.getTimestamp("mes_fecadd"));
                objMesa.setMes_usumod(rs.getString("mes_usumod"));
                objMesa.setMes_fecmod(rs.getTimestamp("mes_fecmod"));
                objMesa.setSuc_id(rs.getInt("suc_id"));
                objMesa.setEmp_id(rs.getInt("emp_id"));
                objMesa.setSup_key(rs.getInt("sup_key"));
                objMesa.setSup_id(rs.getString("sup_id"));
                objMesa.setSup_apenom(rs.getString("sup_apenom"));
                objMesa.setMes_est(rs.getInt("mes_est"));
                objlstMesa.add(objMesa);
       }
    }
    catch (SQLException e) {
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
        return objlstMesa;
    
    }
    
    
    
    
    
    
    
    
    
    

    public ListModelList<Mesa> lstMesaxCanal(int can_id) throws SQLException {
        String SQL_BUSQUEDA = "select t.mes_key,t.mes_id,p.sup_apenom "
                + "from tmesas t,tsupervisores p where t.sup_id=p.sup_id "
                + "and t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.mes_est=1 "
                + "and t.emp_id=" + objUsuCredential.getCodemp()
                + "and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.mes_canalid=" + can_id
                + "order by t.mes_key";
        objlstMesa = new ListModelList<Mesa>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objMesa = new Mesa();
                objMesa.setMes_key(rs.getInt(1));
                objMesa.setMes_id(rs.getString(2));
                objMesa.setSup_apenom(rs.getString(3));
                objlstMesa.add(objMesa);
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
        return objlstMesa;
    }

    public Mesa busMesaxCanal(int can_id, int mes_id) throws SQLException {
        String SQL_BUSQUEDA = "select t.mes_key,t.mes_id,p.sup_id,p.sup_apenom "
                + "from tmesas t,tsupervisores p where t.sup_id=p.sup_id "
                + "and t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.mes_est=1 "
                + " and t.emp_id=" + objUsuCredential.getCodemp()
                + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and t.mes_canalid=" + can_id
                + " and t.mes_key=" + mes_id;
        objMesa = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objMesa = new Mesa();
                objMesa.setMes_key(rs.getInt(1));
                objMesa.setMes_id(rs.getString(2));
                objMesa.setSup_id(rs.getString(3));
                objMesa.setSup_apenom(rs.getString(4));
                objlstMesa.add(objMesa);
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
        return objMesa;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC;
        int valor = 0;
        if (tipo == 1) {//
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from trutas r, tmesas s where r.emp_id=s.emp_id and r.suc_id=s.suc_id and r.rut_mesaid=s.mes_key and r.rut_est<>0 "
                    + "  and s.mes_est<>0 and r.rut_canalid=s.mes_canalid "
                    + "  and s.mes_id=" + dato + " and r.emp_id=" + objUsuCredential.getCodemp() + " and r.suc_id=" + objUsuCredential.getCodsuc() + " ";
        } else {
            SQL_VERIFICA_DOC = "select nvl(to_number(count(*)),0) from tzonas t,tmesas p where t.zon_mesid=p.mes_key and t.emp_id=p.emp_id and t.suc_id=p.suc_id "
                    + " and t.zon_canid=p.mes_canalid and t.zon_est<>0 and p.mes_est<>0 "
                    + "  and p.mes_id=" + dato + " and p.emp_id=" + objUsuCredential.getCodemp() + " and p.suc_id=" + objUsuCredential.getCodsuc() + " ";

        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Canal debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
