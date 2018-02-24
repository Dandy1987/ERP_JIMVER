package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Ruta;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoRutas {

    //Instancias de Objetos
    Ruta objRuta;
    ListModelList<Ruta> objlstRutas;
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
    private static final Logger LOGGER = Logger.getLogger(DaoRutas.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertar(Ruta objRuta) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_RUTA = "{call pack_trutas.p_insertarRuta(?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_RUTA);
            cst.clearParameters();
            cst.setInt(1, objRuta.getRut_canalid());
            cst.setInt(2, objRuta.getRut_mesaid());
            cst.setInt(3, objRuta.getRut_corid());
            cst.setInt(4, objRuta.getRut_ord());
            cst.setString(5, objRuta.getRut_nomrep());
            cst.setString(6, objRuta.getRut_usuadd());
            cst.setInt(7, objRuta.getSuc_id());
            cst.setInt(8, objRuta.getEmp_id());
            cst.registerOutParameter(9, java.sql.Types.NUMERIC);
            cst.registerOutParameter(10, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(10);
            i_flagErrorBD = cst.getInt(9);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro para el canal " + objRuta.getRut_canalid() + " Mesa " + objRuta.getRut_mesaid());
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

    public ParametrosSalida modificar(Ruta objRuta) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_MODIFICAR_RUTA = "{call pack_trutas.p_modificarRuta(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_MODIFICAR_RUTA);
            cst.clearParameters();
            cst.setInt(1, objRuta.getRut_key());
            cst.setInt(2, objRuta.getRut_est());
            cst.setInt(3, objRuta.getRut_ord());
            cst.setString(4, objRuta.getRut_nomrep());
            cst.setString(5, objRuta.getRut_usumod());
            cst.setInt(6, objRuta.getSuc_id());
            cst.setInt(7, objRuta.getEmp_id());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objRuta.getRut_id());
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

    public ParametrosSalida eliminar(Ruta objRuta) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_ELIMINAR_RUTA = "{call pack_trutas.p_eliminarRuta(?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_RUTA);
            cst.clearParameters();
            cst.setInt(1, objRuta.getEmp_id());
            cst.setInt(2, objRuta.getSuc_id());
            cst.setInt(3, objRuta.getRut_est());
            cst.setInt(4, objRuta.getRut_key());
            cst.setString(5, objRuta.getRut_id());
            cst.setString(6, objUsuCredential.getCuenta());
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(8);
            i_flagErrorBD = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objRuta.getRut_id());
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
    public ListModelList<Ruta> listaRutas(int i_caso) throws SQLException {
        String SQL_RUTAS;
        objlstRutas = new ListModelList<Ruta>();
        if (i_caso == 0) {
            SQL_RUTAS = "select * from v_listarutas t where t.rut_est<>" + i_caso + " and t.emp_id="
                    + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
        } else {
            SQL_RUTAS = "select * from v_listarutas t where t.rut_est=" + i_caso + " and t.emp_id="
                    + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
        }
        P_WHERE = SQL_RUTAS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_RUTAS);
            while (rs.next()) {
                objRuta = new Ruta();
                objRuta.setRut_key(rs.getInt("rut_key"));
                objRuta.setRut_id(rs.getString("rut_id"));
                objRuta.setSuc_id(rs.getInt("suc_id"));
                objRuta.setEmp_id(rs.getInt("emp_id"));
                objRuta.setRut_est(rs.getInt("rut_est"));
                objRuta.setRut_canalid(rs.getInt("rut_canalid"));
                objRuta.setCan_id(rs.getString("can_id"));
                objRuta.setRut_canaldes(rs.getString("rut_canaldes"));
                objRuta.setRut_mesaid(rs.getInt("rut_mesaid"));
                objRuta.setMes_id(rs.getString("mes_id"));
                objRuta.setRut_supapenom(rs.getString("rut_supapenom"));
                objRuta.setRut_corid(rs.getInt("rut_corid"));
                objRuta.setRut_usuadd(rs.getString("rut_usuadd"));
                objRuta.setRut_fecadd(rs.getTimestamp("rut_fecadd"));
                objRuta.setRut_usumod(rs.getString("rut_usumod"));
                objRuta.setRut_fecmod(rs.getTimestamp("rut_fecmod"));
                objRuta.setRut_ord(rs.getInt("rut_ord"));
                objRuta.setRut_nomrep(rs.getString("rut_nomrep"));
                objRuta.setRut_usuadd(rs.getString("rut_usuadd"));
                objRuta.setRut_fecadd(rs.getTimestamp("rut_fecadd"));
                objRuta.setRut_usumod(rs.getString("rut_usumod"));
                objRuta.setRut_fecmod(rs.getTimestamp("rut_fecmod"));
                objlstRutas.add(objRuta);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRutas.getSize() + " registro(s)");
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
        return objlstRutas;
    }

    public ListModelList<Ruta> busquedaRutas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA = "";

        /*
         if (i_seleccion == 1) {
         SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.rut_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         } else if (i_seleccion == 2) {
         SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.tab_subdir like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         } else if (i_seleccion == 3) {
         SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.tab_subdes like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         } else if (i_seleccion == 4) {
         SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.mes_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         } else if (i_seleccion == 5) {
         SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.sup_apenom like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         //seleccion especial para el lov busqueda de Rutas
         } else if (i_seleccion == 6) {
         SQL_BUSQUEDA = "select * from v_listarutas t "
         + "where  t.rut_id like '" + s_consulta + "' or "
         + "t.tab_subdir like '" + s_consulta + "'or "
         + "t.sup_apenom like '" + s_consulta + "' or "
         + "t.rut_mesaid like '" + s_consulta + "' or "
         + "t.tab_subdes like '" + s_consulta + "' and t.rut_est=" + i_estado + " and "
         + "t.emp_id=" + objUsuCredential.getCodemp() + " and "
         + "t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
         }*/
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est<>0 and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est <>0 and t.rut_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est <>0 and t.can_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est <>0 and t.rut_canaldes like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 4) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est <>0 and t.mes_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 5) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est <>0 and t.rut_supapenom like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                    //seleccion especial para el lov busqueda de Rutas
                } else if (i_seleccion == 6) {
                    SQL_BUSQUEDA = "select * from v_listarutas t "
                            + "where  (t.rut_id like '" + s_consulta + "' or "
                            + "t.can_id like '" + s_consulta + "'or "
                            + "t.rut_supapenom like '" + s_consulta + "' or "
                            + "t.rut_mesaid like '" + s_consulta + "' or "
                            + "t.rut_canaldes like '" + s_consulta + "') and t.rut_est<>0 and "
                            + "t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.rut_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 2) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.can_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 3) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.rut_canaldes like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 4) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.mes_id like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                } else if (i_seleccion == 5) {
                    SQL_BUSQUEDA = "select * from v_listarutas t where t.rut_est=" + i_estado + " and t.rut_supapenom like '" + s_consulta + "' and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                    //seleccion especial para el lov busqueda de Rutas
                } else if (i_seleccion == 6) {
                    SQL_BUSQUEDA = "select * from v_listarutas t "
                            + "where  (t.rut_id like '" + s_consulta + "' or "
                            + "t.can_id like '" + s_consulta + "'or "
                            + "t.rut_supapenom like '" + s_consulta + "' or "
                            + "t.rut_mesaid like '" + s_consulta + "' or "
                            + "t.rut_canaldes like '" + s_consulta + "') and t.rut_est=" + i_estado + " and "
                            + "t.emp_id=" + objUsuCredential.getCodemp() + " and "
                            + "t.suc_id=" + objUsuCredential.getCodsuc() + " order by t.rut_id";
                }
            }
        }
        P_WHERE = SQL_BUSQUEDA;
        objlstRutas = new ListModelList<Ruta>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA);
            while (rs.next()) {
                objRuta = new Ruta();
                objRuta.setRut_key(rs.getInt("rut_key"));
                objRuta.setRut_id(rs.getString("rut_id"));
                objRuta.setSuc_id(rs.getInt("suc_id"));
                objRuta.setEmp_id(rs.getInt("emp_id"));
                objRuta.setRut_est(rs.getInt("rut_est"));
                objRuta.setRut_canalid(rs.getInt("rut_canalid"));
                objRuta.setCan_id(rs.getString("can_id"));
                objRuta.setRut_canaldes(rs.getString("rut_canaldes"));
                objRuta.setRut_mesaid(rs.getInt("rut_mesaid"));
                objRuta.setMes_id(rs.getString("mes_id"));
                objRuta.setRut_supapenom(rs.getString("rut_supapenom"));
                objRuta.setRut_corid(rs.getInt("rut_corid"));
                objRuta.setRut_usuadd(rs.getString("rut_usuadd"));
                objRuta.setRut_fecadd(rs.getTimestamp("rut_fecadd"));
                objRuta.setRut_usumod(rs.getString("rut_usumod"));
                objRuta.setRut_fecmod(rs.getTimestamp("rut_fecmod"));
                objRuta.setRut_ord(rs.getInt("rut_ord"));
                objRuta.setRut_nomrep(rs.getString("rut_nomrep"));
                objRuta.setRut_usuadd(rs.getString("rut_usuadd"));
                objRuta.setRut_fecadd(rs.getTimestamp("rut_fecadd"));
                objRuta.setRut_usumod(rs.getString("rut_usumod"));
                objRuta.setRut_fecmod(rs.getTimestamp("rut_fecmod"));
                objlstRutas.add(objRuta);
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
        return objlstRutas;
    }

    public Ruta buscarRuta(String rut_id) throws SQLException {
        String SQL_RUTA = "select * from v_listarutas p where p.rut_id='" + rut_id + "' and p.emp_id=" + objUsuCredential.getCodemp()
                + " and p.suc_id=" + objUsuCredential.getCodsuc() + " and p.rut_est=1";
        objRuta = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_RUTA);
            while (rs.next()) {
                objRuta = new Ruta();
                objRuta.setRut_key(rs.getInt("rut_key"));
                objRuta.setRut_id(rs.getString("rut_id"));
                objRuta.setSuc_id(rs.getInt("suc_id"));
                objRuta.setEmp_id(rs.getInt("emp_id"));
                objRuta.setRut_est(rs.getInt("rut_est"));
                objRuta.setRut_canalid(rs.getInt("rut_canalid"));
                objRuta.setCan_id(rs.getString("can_id"));
                objRuta.setRut_canaldes(rs.getString("rut_canaldes"));
                objRuta.setRut_mesaid(rs.getInt("rut_mesaid"));
                objRuta.setMes_id(rs.getString("mes_id"));
                objRuta.setRut_supapenom(rs.getString("rut_supapenom"));
                objRuta.setRut_corid(rs.getInt("rut_corid"));
                objRuta.setRut_usuadd(rs.getString("rut_usuadd"));
                objRuta.setRut_fecadd(rs.getTimestamp("rut_fecadd"));
                objRuta.setRut_usumod(rs.getString("rut_usumod"));
                objRuta.setRut_fecmod(rs.getTimestamp("rut_fecmod"));
                objRuta.setRut_ord(rs.getInt("rut_ord"));
                objRuta.setRut_nomrep(rs.getString("rut_nomrep"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstRutas.getSize() + " registro(s)");
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
        return objRuta;
    }

    public int busquedaExistencia(int tipo, String dato) throws SQLException {
        String SQL_VERIFICA_DOC = "";
        int valor = 0;
        if (tipo == 1) {
            SQL_VERIFICA_DOC = "  select nvl(to_number(count(*)),0) from tzonas t, trutas p "
                    + "where t.emp_id=p.emp_id and t.suc_id=p.suc_id and t.zon_est<>0 and p.rut_est<>0 and t.zon_rutid=p.rut_id "
                    + "and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + "and t.zon_rutid=" + dato + " ";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_VERIFICA_DOC);
            while (rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            Messagebox.show(" Error de Validación de Ruta debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
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
