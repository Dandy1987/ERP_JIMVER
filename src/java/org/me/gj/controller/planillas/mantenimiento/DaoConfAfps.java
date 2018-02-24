/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.ManPer;
import org.me.gj.model.planillas.mantenimiento.ConfAfp1;
import org.me.gj.model.planillas.mantenimiento.ConfAfp2;
import org.me.gj.model.planillas.mantenimiento.ManConfAfps;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoConfAfps {

    //Instancias de Objetos
    ListModelList<ManConfAfps> objlstConf;
    ManConfAfps objConf;
    // LOV 1
    ListModelList<ConfAfp1> objlstConfAfp1;
    ConfAfp1 objConfafp1;
    // LOV 2
    ListModelList<ConfAfp2> objlstConfAfp2;
    ConfAfp2 objConfafp2;

    ListModelList<ManPer> objLisManPer;
    ManPer objManPer;
    //Variables Publicas
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
    private static final Logger LOGGER = Logger.getLogger(DaoUbicaciones.class);

    //Eventos Primarios - Transaccionales
    public String insertar(ManConfAfps objConf) throws SQLException {

        String n_plppag_id = objConf.getPeriodo();
        String n_plafp_id = objConf.getAfpid();
        double n_plafp_apobl = objConf.getApobl();
        String n_plafp_apori = objConf.getApori();
        String n_plafp_apodes = objConf.getApodes();
        double n_plafp_comfij = objConf.getComfij();
        String n_plafp_comori = objConf.getComori();
        String n_plafp_comdes = objConf.getComdes();
        double n_plafp_priseg = objConf.getPriseg();
        String n_plafp_priori = objConf.getPriori();
        String n_plafp_prides = objConf.getPrides();
        double n_plafp_topseg = objConf.getTopseg();
        String n_plafp_idcta = objConf.getIdcta();
        //int n_plafp_estado = objConf.getEstado();
        double n_plafp_commix = objConf.getCommix();
        String n_plafp_mixori = objConf.getMixori();
        String n_plafp_mixdes = objConf.getMixdes();
        //String n_plafp_usuadd = objConf.getUsuadd();

        String SQL_INSERTAR_CONF = "{call codijisa.pack_tplcfgafp.p_insertarConf(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_CONF);
            cst.clearParameters();
            cst.setString(1, n_plppag_id);
            cst.setString(2, n_plafp_id);
            cst.setDouble(3, n_plafp_apobl);
            cst.setString(4, n_plafp_apori);
            cst.setString(5, n_plafp_apodes);
            cst.setDouble(6, n_plafp_comfij);
            cst.setString(7, n_plafp_comori);
            cst.setString(8, n_plafp_comdes);
            cst.setDouble(9, n_plafp_priseg);
            cst.setString(10, n_plafp_priori);
            cst.setString(11, n_plafp_prides);
            cst.setDouble(12, n_plafp_topseg);
            cst.setString(13, n_plafp_idcta);
            cst.setDouble(14, n_plafp_commix);
            cst.setString(15, n_plafp_mixori);
            cst.setString(16, n_plafp_mixdes);
            cst.setString(17, objUsuCredential.getCuenta());
            cst.registerOutParameter(18, java.sql.Types.NUMERIC);
            cst.registerOutParameter(19, java.sql.Types.VARCHAR);            
            cst.execute();
            s_msg = cst.getString(19);
            i_flagErrorBD = cst.getInt(18);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + n_plafp_id);
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

    public String modificar(ManConfAfps objConf) throws SQLException {

        String n_plppag_id = objConf.getPeriodo();
        String n_plafp_id = objConf.getAfpid();
        double n_plafp_apobl = objConf.getApobl();
        String n_plafp_apori = objConf.getApori();
        String n_plafp_apodes = objConf.getApodes();
        double n_plafp_comfij = objConf.getComfij();
        String n_plafp_comori = objConf.getComori();
        String n_plafp_comdes = objConf.getComdes();
        double n_plafp_priseg = objConf.getPriseg();
        String n_plafp_priori = objConf.getPriori();
        String n_plafp_prides = objConf.getPrides();
        double n_plafp_topseg = objConf.getTopseg();
        String n_plafp_idcta = objConf.getIdcta();
        int s_estado = objConf.getEstado();
        double n_plafp_commix = objConf.getCommix();
        String n_plafp_mixori = objConf.getMixori();
        String n_plafp_mixdes = objConf.getMixdes();
        //String n_plafp_usumod = objConf.getUsumod();

        String SQL_MODIFICAR_CONF = "{call codijisa.pack_tplcfgafp.p_modificarConf(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_CONF);
            cst.clearParameters();
            cst.setString(1, n_plppag_id);
            cst.setString(2, n_plafp_id);
            cst.setDouble(3, n_plafp_apobl);
            cst.setString(4, n_plafp_apori);
            cst.setString(5, n_plafp_apodes);
            cst.setDouble(6, n_plafp_comfij);
            cst.setString(7, n_plafp_comori);
            cst.setString(8, n_plafp_comdes);
            cst.setDouble(9, n_plafp_priseg);
            cst.setString(10, n_plafp_priori);
            cst.setString(11, n_plafp_prides);
            cst.setDouble(12, n_plafp_topseg);
            cst.setString(13, n_plafp_idcta);
            cst.setInt(14, s_estado);
            cst.setDouble(15, n_plafp_commix);
            cst.setString(16, n_plafp_mixori);
            cst.setString(17, n_plafp_mixdes);
            cst.setString(18, objUsuCredential.getCuenta());
            cst.registerOutParameter(19, java.sql.Types.NUMERIC);
            cst.registerOutParameter(20, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(20);
            i_flagErrorBD = cst.getInt(19);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + n_plafp_id);

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

    public String eliminar(ManConfAfps objConf) throws SQLException {

        String n_plafp_id = objConf.getAfpid();
        String n_plppag_id = objConf.getPeriodo();

        String SQL_ELIMINAR_CONF = "{call codijisa.pack_tplcfgafp.p_eliminarConf(?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CONF);
            cst.clearParameters();
            cst.setString(1, n_plafp_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.setString(4, n_plppag_id);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objConf.getAfpid());

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

    //Eventos Secundarios - Listas y Validaciones
    public ListModelList<ManConfAfps> listaConf(int s_estado) throws SQLException {
        String sql_conf;

        sql_conf = "{call codijisa.pack_tplcfgafp.p_listConf(?,?,?)}";

        //P_WHERE = sql_conf;

        try {

            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_conf);
            cst.setInt(1, s_estado);
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            
            P_WHERE = cst.getString(3);
            
            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objConf = null;
            objlstConf = new ListModelList<ManConfAfps>();

            while (rs.next()) {
                objConf = new ManConfAfps();
                //objConf.setEmp_id(rs.getInt("EMP_ID"));
                objConf.setPeriodo(rs.getString("PLPPAG_ID"));
                objConf.setAfpid(rs.getString("PLAFP_ID"));
                objConf.setAfp_des(rs.getString("TABLA_DESCRI"));
                objConf.setApobl(rs.getDouble("PLAFP_APOBL"));
                objConf.setApori(rs.getString("PLAFP_APORI"));
                objConf.setOrigen(rs.getString("apori"));
                objConf.setApodes(rs.getString("PLAFP_APODES"));
                objConf.setDestino(rs.getString("apodes"));
                objConf.setComfij(rs.getDouble("PLAFP_COMFIJ"));
                objConf.setComori(rs.getString("PLAFP_COMORI"));
                objConf.setCom_origen(rs.getString("comori"));
                objConf.setComdes(rs.getString("PLAFP_COMDES"));
                objConf.setCom_destino(rs.getString("comdes"));
                objConf.setPriseg(rs.getDouble("PLAFP_PRISEG"));
                objConf.setPriori(rs.getString("PLAFP_PRIORI"));
                objConf.setPri_origen(rs.getString("priori"));
                objConf.setPrides(rs.getString("PLAFP_PRIDES"));
                objConf.setPri_destino(rs.getString("prides"));
                objConf.setTopseg(rs.getDouble("PLAFP_TOPSEG"));
                objConf.setIdcta(rs.getString("PLAFP_IDCTA"));
                objConf.setEstado(rs.getInt("PLAFP_ESTADO"));
                objConf.setCommix(rs.getDouble("PLAFP_COMMIX"));
                objConf.setMixori(rs.getString("PLAFP_MIXORI"));
                objConf.setMix_origen(rs.getString("mixori"));
                objConf.setMixdes(rs.getString("PLAFP_MIXDES"));
                objConf.setMix_destino(rs.getString("mixdes"));
                objConf.setUsuadd(rs.getString("PLAFP_USUADD"));
                objConf.setFecadd(rs.getTimestamp("PLAFP_FECADD"));
                objConf.setUsumod(rs.getString("PLAFP_USUMOD"));
                objConf.setFecmod(rs.getTimestamp("PLAFP_FECMOD"));
                objlstConf.add(objConf);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }

        return objlstConf;

    }

    public ListModelList<ManConfAfps> busquedaConf(int seleccion, String consulta, int s_estado) throws SQLException {

        String sql = "";

        if (s_estado == 3) {
            if (seleccion == 0) {
                sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                        + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                        + "    where  t.plafp_id = r.tabla_id "
                        + "    and    r.tabla_cod = '00005' "
                        + "    order by t.plppag_id desc,t.plafp_id";

            } else {
                if (seleccion == 1) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plppag_id like '" + consulta + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 2) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plafp_id like '" + consulta + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 3) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    r.tabla_descri like '" + consulta + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 4) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plafp_idcta like '" + consulta + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";

                }

            }

        } else {

            if (seleccion == 0) {
                sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                        + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                        + "    where  t.plafp_id = r.tabla_id "
                        + "    and    r.tabla_cod = '00005' "
                        + "    and    t.plafp_estado ='" + s_estado + "' "
                        + "    order by t.plppag_id desc,t.plafp_id";

            } else {
                if (seleccion == 1) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plppag_id like '" + consulta + "' "
                            + "    and    t.plafp_estado ='" + s_estado + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 2) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plafp_id like '" + consulta + "' "
                            + "    and    t.plafp_estado ='" + s_estado + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 3) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    r.tabla_descri like '" + consulta + "' "
                            + "    and    t.plafp_estado ='" + s_estado + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";
                } else if (seleccion == 4) {
                    sql = "select t.*,r.tabla_descri,pack_tplcfgafp.DESCRIPCION(t.plafp_apori) apori,pack_tplcfgafp.DESCRIPCION(t.plafp_apodes) apodes,pack_tplcfgafp.DESCRIPCION(t.plafp_comori) comori,pack_tplcfgafp.DESCRIPCION(t.plafp_comdes) comdes, \n"
                            + " pack_tplcfgafp.DESCRIPCION(t.plafp_priori) priori,pack_tplcfgafp.DESCRIPCION(t.plafp_prides) prides,pack_tplcfgafp.DESCRIPCION(t.plafp_mixori) mixori,pack_tplcfgafp.DESCRIPCION(t.plafp_mixdes) mixdes from tplcfgafp t,tpltablas1 r "
                            + "    where  t.plafp_id = r.tabla_id "
                            + "    and    r.tabla_cod = '00005' "
                            + "    and    t.plafp_idcta like '" + consulta + "' "
                            + "    and    t.plafp_estado ='" + s_estado + "' "
                            + "    order by t.plppag_id desc,t.plafp_id";

                }

            }
        }
        P_WHERE = sql;
        objlstConf = new ListModelList<ManConfAfps>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConf = new ManConfAfps();
                objConf.setEmp_id(rs.getInt("EMP_ID"));
                objConf.setPeriodo(rs.getString("PLPPAG_ID"));
                objConf.setAfpid(rs.getString("PLAFP_ID"));
                objConf.setAfp_des(rs.getString("TABLA_DESCRI"));
                objConf.setApobl(rs.getDouble("PLAFP_APOBL"));
                objConf.setApori(rs.getString("PLAFP_APORI"));
                objConf.setOrigen(rs.getString("apori"));
                objConf.setApodes(rs.getString("PLAFP_APODES"));
                objConf.setDestino(rs.getString("apodes"));
                objConf.setComfij(rs.getDouble("PLAFP_COMFIJ"));
                objConf.setComori(rs.getString("PLAFP_COMORI"));
                objConf.setCom_origen(rs.getString("comori"));
                objConf.setComdes(rs.getString("PLAFP_COMDES"));
                objConf.setCom_destino(rs.getString("comdes"));
                objConf.setPriseg(rs.getDouble("PLAFP_PRISEG"));
                objConf.setPriori(rs.getString("PLAFP_PRIORI"));
                objConf.setPri_origen(rs.getString("priori"));
                objConf.setPrides(rs.getString("PLAFP_PRIDES"));
                objConf.setPri_destino(rs.getString("prides"));
                objConf.setTopseg(rs.getDouble("PLAFP_TOPSEG"));
                objConf.setIdcta(rs.getString("PLAFP_IDCTA"));
                objConf.setEstado(rs.getInt("PLAFP_ESTADO"));
                objConf.setCommix(rs.getDouble("PLAFP_COMMIX"));
                objConf.setMixori(rs.getString("PLAFP_MIXORI"));
                objConf.setMix_origen(rs.getString("mixori"));
                objConf.setMixdes(rs.getString("PLAFP_MIXDES"));
                objConf.setMix_destino(rs.getString("mixdes"));
                objConf.setUsuadd(rs.getString("PLAFP_USUADD"));
                objConf.setFecadd(rs.getTimestamp("PLAFP_FECADD"));
                objConf.setUsumod(rs.getString("PLAFP_USUMOD"));
                objConf.setFecmod(rs.getTimestamp("PLAFP_FECMOD"));
                objlstConf.add(objConf);

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
        return objlstConf;

    }

    //LOV 1
    public ListModelList<ConfAfp1> busquedaLovConf1() throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00005' "
                + "and t.tabla_id <> '000' "
                + "order by t.tabla_id ";

        objlstConfAfp1 = new ListModelList<ConfAfp1>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConfafp1 = new ConfAfp1();
                objConfafp1.setAfp_id(rs.getString("TABLA_ID"));
                objConfafp1.setAfp_des(rs.getString("TABLA_DESCRI"));
                objlstConfAfp1.add(objConfafp1);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstConfAfp1;

    }

    public ListModelList<ConfAfp1> busquedaLovConf1_2(String consulta) throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00005' "
                + "and t.tabla_id <> '000' "
                + "and t.tabla_descri like '%" + consulta + "%'"
                + "order by t.tabla_id ";

        objlstConfAfp1 = new ListModelList<ConfAfp1>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConfafp1 = new ConfAfp1();
                objConfafp1.setAfp_id(rs.getString("TABLA_ID"));
                objConfafp1.setAfp_des(rs.getString("TABLA_DESCRI"));
                objlstConfAfp1.add(objConfafp1);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstConfAfp1;

    }

    public ConfAfp1 getLovConf1(String afp) throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00005' "
                + "and t.tabla_id <> '000' "
                + "and t.tabla_id like '%" + afp + "%'"
                + "order by t.tabla_id ";

        //objlstRegPensionario = new ListModelList<RegPensionario>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objConfafp1 = null;
            while (rs.next()) {
                objConfafp1 = new ConfAfp1();
                objConfafp1.setAfp_id(rs.getString("TABLA_ID"));
                objConfafp1.setAfp_des(rs.getString("TABLA_DESCRI"));

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objConfafp1;

    }

    //LOV 2
    public ListModelList<ConfAfp2> busquedaLovConf2() throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00001' "
                + "and t.tabla_id <> '000' "
                + "order by t.tabla_id ";

        objlstConfAfp2 = new ListModelList<ConfAfp2>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConfafp2 = new ConfAfp2();
                objConfafp2.setConcep_id(rs.getString("TABLA_ID"));
                objConfafp2.setConcep_des(rs.getString("TABLA_DESCRI"));
                objlstConfAfp2.add(objConfafp2);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstConfAfp2;

    }

    public ListModelList<ConfAfp2> busquedaLovConf2_2(String consulta) throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00001' "
                + "and t.tabla_id <> '000' "
                + "and t.tabla_descri like '%" + consulta + "%'"
                + "order by t.tabla_id ";

        objlstConfAfp2 = new ListModelList<ConfAfp2>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objConfafp2 = new ConfAfp2();
                objConfafp2.setConcep_id(rs.getString("TABLA_ID"));
                objConfafp2.setConcep_des(rs.getString("TABLA_DESCRI"));
                objlstConfAfp2.add(objConfafp2);

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objlstConfAfp2;

    }

    public ConfAfp2 getLovConf2(String afp) throws SQLException {

        String sql = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00001' "
                + "and t.tabla_id <> '000' "
                + "and t.tabla_id like '%" + afp + "%'"
                + "order by t.tabla_id ";

        //objlstRegPensionario = new ListModelList<RegPensionario>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            objConfafp2 = null;
            while (rs.next()) {
                objConfafp2 = new ConfAfp2();
                objConfafp2.setConcep_id(rs.getString("TABLA_ID"));
                objConfafp2.setConcep_des(rs.getString("TABLA_DESCRI"));

            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }

        }

        return objConfafp2;

    }
}
