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
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.me.gj.controller.logistica.mantenimiento.DaoUbicaciones;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.ManAfpsCre;
import org.me.gj.model.planillas.mantenimiento.RegPensionario;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author greyes
 */
public class DaoAfpsCre {

    //Instancias de Objetos
    ListModelList<ManAfpsCre> objlstAfp;
    ManAfpsCre objAfp;
    // LOV REGIMEN PENSIONARIO
    ListModelList<RegPensionario> objlstRegPensionario;
    RegPensionario objRegPensionario;
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
    public String insertar(ManAfpsCre objAfp) throws SQLException {

        String s_afp_des = objAfp.getAfp_des();
        String s_afp_usuadd = objAfp.getAfp_usuadd();
        String s_afp_3 = objAfp.getAfp_cod_enl();
        String s_afp_4 = objAfp.getAfp_cod_reg();
        String SQL_INSERTAR_AFP = "{call codijisa.pack_tpltablas1.p_insertarAfp(?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_AFP);
            cst.clearParameters();
            cst.setString(1, s_afp_des);
            cst.setString(2, s_afp_usuadd);
            cst.setString(3, s_afp_3);
            cst.setString(4, s_afp_4);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(6);
            i_flagErrorBD = cst.getInt(5);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_afp_des);
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

    public String modificar(ManAfpsCre objAfp) throws SQLException {
        String s_afp_id = objAfp.getAfp_id();
        String s_afp_des = objAfp.getAfp_des();
        int s_afp_est = objAfp.getAfp_est();
        String s_afp_3 = objAfp.getAfp_cod_enl();
        String s_afp_4 = objAfp.getAfp_cod_reg();
        String s_afp_usumod = objAfp.getAfp_usumod();
        String SQL_MODIFICAR_AFP = "{call codijisa.pack_tpltablas1.p_modificarAfp(?,?,?,?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_AFP);
            cst.clearParameters();
            cst.setString(1, s_afp_id);
            cst.setString(2, s_afp_des);
            cst.setInt(3, s_afp_est);
            cst.setString(4, s_afp_3);
            cst.setString(5, s_afp_4);
            cst.setString(6, s_afp_usumod);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con código " + objAfp.getAfp_id());

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

    public String eliminar(ManAfpsCre objAfp) throws SQLException {

        String s_afp_id = objAfp.getAfp_id();

        String SQL_ELIMINAR_CARGO = "{call codijisa.pack_tpltablas1.p_eliminarAfp(?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_CARGO);
            cst.clearParameters();
            cst.setString(1, s_afp_id);
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objAfp.getAfp_id());
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
    public ListModelList<ManAfpsCre> listaAfps(int s_afp_est) throws SQLException {
        String sql_lista_afp;

        sql_lista_afp = "{call codijisa.pack_tpltablas1.p_listafps(?,?,?)}";

        //P_WHERE = sql_lista_afp;
        try {
            con = new ConectaBD().conectar();
            CallableStatement cst = con.prepareCall(sql_lista_afp);
            cst.setInt(1, s_afp_est);
            cst.registerOutParameter(2, OracleTypes.CURSOR);//REF CURSOR
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();

            P_WHERE = cst.getString(3);

            rs = ((OracleCallableStatement) cst).getCursor(2);
            st = con.createStatement();

            objAfp = null;
            objlstAfp = new ListModelList<ManAfpsCre>();

            while (rs.next()) {
                objAfp = new ManAfpsCre();
                objAfp.setAfp_id(rs.getString("TABLA_ID"));
                objAfp.setAfp_des(rs.getString("TABLA_DESCRI"));
                objAfp.setAfp_est(rs.getInt("TABLA_ESTADO"));
                objAfp.setAfp_cod_enl(rs.getString("TABLA_VALOR3"));
                objAfp.setAfp_cod_reg(rs.getString("TABLA_VALOR4"));
                objAfp.setAfp_des_reg(rs.getString("TABLA_DESCRI2"));
                objAfp.setAfp_usuadd(rs.getString("TABLA_USUADD"));
                objAfp.setAfp_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objAfp.setAfp_usumod(rs.getString("TABLA_USUMOD"));
                objAfp.setAfp_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstAfp.add(objAfp);
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

        return objlstAfp;

    }

    public ListModelList<ManAfpsCre> busquedaAfp(int seleccion, String consulta, int s_afp_est) throws SQLException {
        String sql = "";

        if (s_afp_est == 3) {
            if (seleccion == 0) {
                sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r "
                        + "   where t.tabla_valor4 = r.tabla_id(+)"
                        + "   and  t.tabla_cod = '00005' "
                        + "   and  t.tabla_id <> '000' "
                        + "   and   r.tabla_cod(+) = '00015' "
                        + "   order by t.tabla_id";
            } else {
                if (seleccion == 1) {
                    sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r "
                            + "   where t.tabla_valor4 = r.tabla_id(+)"
                            + "   and   t.tabla_cod = '00005' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_id like '" + consulta + "' "
                            + "   and   r.tabla_cod(+) = '00015' "
                            + "   order by t.tabla_id";
                } else if (seleccion == 2) {
                    sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r "
                            + "   where t.tabla_valor4 = r.tabla_id(+)"
                            + "   and   t.tabla_cod = '00005'"
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_descri like '" + consulta + "' "
                            + "   and   r.tabla_cod(+) = '00015' "
                            + "   order by t.tabla_id";
                }
            }
        } else {
            if (seleccion == 0) {
                sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r  "
                        + "   where t.tabla_valor4 = r.tabla_id(+) "
                        + "   and  t.tabla_cod = '00005' "
                        + "   and  t.tabla_id <> '000' "
                        + "   and  t.tabla_estado ='" + s_afp_est + "' "
                        + "   and   r.tabla_cod(+) = '00015' "
                        + "   order by t.tabla_id ";
            } else {
                if (seleccion == 1) {
                    sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r "
                            + "   where t.tabla_valor4 = r.tabla_id(+)"
                            + "   and   t.tabla_cod = '00005' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_estado ='" + s_afp_est + "' "
                            + "   and   t.tabla_id like '" + consulta + "' "
                            + "   and   r.tabla_cod(+) = '00015' "
                            + "   order by t.tabla_id";
                } else if (seleccion == 2) {
                    sql = "select t.*,r.tabla_descri tabla_descri2 from CODIJISA.Tpltablas1 t,tpltablas1 r  "
                            + "   where t.tabla_valor4 = r.tabla_id(+)"
                            + "   and   t.tabla_cod = '00005' "
                            + "   and   t.tabla_id <> '000' "
                            + "   and   t.tabla_estado ='" + s_afp_est + "' "
                            + "   and   t.tabla_descri like '" + consulta + "' "
                            + "   and   r.tabla_cod(+) = '00015' "
                            + "   order by t.tabla_id";
                }
            }
        }
        P_WHERE = sql;
        objlstAfp = new ListModelList<ManAfpsCre>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objAfp = new ManAfpsCre();
                objAfp.setAfp_id(rs.getString("TABLA_ID"));
                objAfp.setAfp_des(rs.getString("TABLA_DESCRI"));
                objAfp.setAfp_est(rs.getInt("TABLA_ESTADO"));
                objAfp.setAfp_cod_enl(rs.getString("TABLA_VALOR3"));
                objAfp.setAfp_cod_reg(rs.getString("TABLA_VALOR4"));
                objAfp.setAfp_des_reg(rs.getString("TABLA_DESCRI2"));
                objAfp.setAfp_usuadd(rs.getString("TABLA_USUADD"));
                objAfp.setAfp_fecadd(rs.getTimestamp("TABLA_FECADD"));
                objAfp.setAfp_usumod(rs.getString("TABLA_USUMOD"));
                objAfp.setAfp_fecmod(rs.getTimestamp("TABLA_FECMOD"));
                objlstAfp.add(objAfp);
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
        return objlstAfp;

    }

    //LOV REGIMEN PENSIONARIO
    public ListModelList<RegPensionario> busquedaLovRegPensionario() throws SQLException {

        String sql_reg
                = /*"select t.tabla_id,t.tabla_descri from tpltablas1 t "
                 + "where t.tabla_cod = '00015' "
                 + "and t.tabla_id <> '000' "
                 + "order by t.tabla_id ";*/ " select r.tabla_id,r.tabla_descri,t.tabla_id id , t.tabla_descri des\n"
                + "  from tpltablas1 r,tpltablas1 t\n"
                + "     where \n"
                + "        r.tabla_id=t.tabla_valor4(+)\n"
                + "        and r.tabla_cod = '00015'\n"
                + "       and t.tabla_cod(+)='00005'\n"
                + "     and   r.tabla_id <> '000'\n"
                + "     order by r.tabla_id";

        objlstRegPensionario = new ListModelList<RegPensionario>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_reg);
            while (rs.next()) {
                objRegPensionario = new RegPensionario();
                objRegPensionario.setReg_id(rs.getString("TABLA_ID"));
                objRegPensionario.setReg_des(rs.getString("TABLA_DESCRI"));
                objRegPensionario.setId(rs.getString("id"));
                objRegPensionario.setDescr(rs.getString("des"));
                objlstRegPensionario.add(objRegPensionario);

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

        return objlstRegPensionario;

    }

    public ListModelList<RegPensionario> busquedaLovRegPensionario2(String consulta) throws SQLException {

        String sql_reg = " select r.tabla_id,r.tabla_descri,t.tabla_id id , t.tabla_descri des\n"
                + "  from tpltablas1 r,tpltablas1 t\n"
                + "     where \n"
                + "        r.tabla_id=t.tabla_valor4(+)\n"
                + "        and r.tabla_cod = '00015'\n"
                //+ "       and t.tabla_cod(+)='00005'\n"
                + "and r.tabla_descri like '%" + consulta + "%'"
                + "     and   r.tabla_id <> '000'\n"
                + "     order by r.tabla_id";

        /*"select t.tabla_id,t.tabla_descri from tpltablas1 t "
         + "where t.tabla_cod = '00015' "
         + "and t.tabla_id <> '000' "
         + "and t.tabla_descri like '%" + consulta + "%'"
         + "order by t.tabla_id ";*/
        objlstRegPensionario = new ListModelList<RegPensionario>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_reg);
            while (rs.next()) {
                objRegPensionario = new RegPensionario();
                objRegPensionario.setReg_id(rs.getString("TABLA_ID"));
                objRegPensionario.setReg_des(rs.getString("TABLA_DESCRI"));
                objRegPensionario.setId(rs.getString("id"));
                objRegPensionario.setDescr(rs.getString("des"));
                objlstRegPensionario.add(objRegPensionario);

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

        return objlstRegPensionario;

    }

    public RegPensionario getLovRegPensionario(String regimen) throws SQLException {

        String sql_reg = "select t.tabla_id,t.tabla_descri from tpltablas1 t "
                + "where t.tabla_cod = '00015' "
                + "and t.tabla_id <> '000' "
                + "and t.tabla_id like '%" + regimen + "%'"
                + "order by t.tabla_id ";

        //objlstRegPensionario = new ListModelList<RegPensionario>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_reg);
            objRegPensionario = null;
            while (rs.next()) {
                objRegPensionario = new RegPensionario();
                objRegPensionario.setReg_id(rs.getString("TABLA_ID"));
                objRegPensionario.setReg_des(rs.getString("TABLA_DESCRI"));

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

        return objRegPensionario;

    }

}
