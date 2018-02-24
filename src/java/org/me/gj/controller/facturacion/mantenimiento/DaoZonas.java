package org.me.gj.controller.facturacion.mantenimiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.facturacion.mantenimiento.Zona;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoZonas {

    ListModelList<Zona> objlstZonas;
    Zona objZona;
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
    private static final Logger LOGGER = Logger.getLogger(DaoZonas.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida insertarZona(Zona objZona) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_MESA = "{call pack_tzonas.p_insertarZona(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MESA);
            cst.clearParameters();
            cst.setInt(1, objZona.getSuc_id());
            cst.setInt(2, objZona.getEmp_id());
            cst.setString(3, objZona.getZon_des());
            cst.setInt(4, Integer.parseInt(objZona.getZon_idven()));
            cst.setInt(5, objZona.getZon_dvis());
            cst.setString(6, objZona.getZon_rutid());
            cst.setInt(7, objZona.getZon_ord());
            cst.setInt(8, objZona.getZon_canid());
            cst.setInt(9, objZona.getZon_mesid());
            cst.setString(10, objZona.getZon_nomrep());
            cst.setString(11, objZona.getZon_usuadd());
            cst.setString(12, objZona.getZon_ubiid());
            cst.setInt(13, Integer.parseInt(objZona.getZon_paisid()));
            cst.registerOutParameter(14, java.sql.Types.NUMERIC);
            cst.registerOutParameter(15, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(15);
            i_flagErrorBD = cst.getInt(14);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Zona con la descripcion " + objZona.getZon_des());
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

    public ParametrosSalida eliminarZona(Zona objZona) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cst = null;
        String SQL_INSERTAR_MESA = "{call pack_tzonas.p_eliminarZona(?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERTAR_MESA);
            cst.clearParameters();
            cst.setLong(1, objZona.getZon_key());
            cst.setString(2, objZona.getZon_id());
            cst.setString(3, objZona.getZon_usumod());
            cst.setInt(4, objZona.getEmp_id());
            cst.setInt(5, objZona.getSuc_id());
            cst.registerOutParameter(6, java.sql.Types.NUMERIC);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(7);
            i_flagErrorBD = cst.getInt(6);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro de Zona con la descripcion " + objZona.getZon_des());
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

    //Eventos Secundarios - Listas y validaciones
    public ListModelList<Zona> listaZonas(int i_caso) throws SQLException {
        String SQL_ZONAS;
        if (i_caso == 0) {
            SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " and t.zon_est<>" + i_caso + " order by t.zon_id";
        } else {
            SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " and t.zon_est=" + i_caso + " order by t.zon_id";
        }
        P_WHERE = SQL_ZONAS;
        objlstZonas = new ListModelList<Zona>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ZONAS);
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                //objZona.setZon_des(rs.getString("zon_des").length() < 50 ? rs.getString("zon_des") : rs.getString("zon_des").substring(0, 50));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstZonas.getSize() + " registro(s)");
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
        return objlstZonas;
    }

    public ListModelList<Zona> busquedaZonas(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_ZONAS = "";
        /*if (i_seleccion == 1) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_id like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 2) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_des like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 3) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_rutid like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 4) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_idven like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 5) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_apenom like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 6) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_ubiid like '" + s_consulta + "' order by t.zon_id";
         } else if (i_seleccion == 7) {
         SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
         + " and t.zon_est=" + i_estado + " and t.zon_ubides like '" + s_consulta + "' order by t.zon_id";
         }*/
        if (i_estado == 3) {
            if (i_seleccion == 0) {
                SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                        + " and t.zon_est<>0  order by t.zon_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_id like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 2) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_des like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 3) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_rutid like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 4) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_idven like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 5) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_apenom like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 6) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_ubiid like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 7) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est<>0 and t.zon_ubides like '" + s_consulta + "' order by t.zon_id";
                }
            }
        } else {
            if (i_seleccion == 0) {
                SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                        + " and t.zon_est=" + i_estado + " order by t.zon_id";
            } else {
                if (i_seleccion == 1) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_id like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 2) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_des like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 3) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_rutid like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 4) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_idven like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 5) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_apenom like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 6) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_ubiid like '" + s_consulta + "' order by t.zon_id";
                } else if (i_seleccion == 7) {
                    SQL_ZONAS = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                            + " and t.zon_est=" + i_estado + " and t.zon_ubides like '" + s_consulta + "' order by t.zon_id";
                }
            }
        }
        P_WHERE = SQL_ZONAS;
        objlstZonas = new ListModelList<Zona>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_ZONAS);
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                //objZona.setZon_des(rs.getString("zon_des").length() < 35 ? rs.getString("zon_des") : rs.getString("zon_des").substring(0, 35));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getDate("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getDate("zon_fecmod"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstZonas.getSize() + " registro(s)");
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
        return objlstZonas;
    }

    public Zona buscaZonaxCodigo(String s_codigo) throws SQLException {
        objZona = null;
        String SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                + " and t.zon_est=1 and t.zon_id='" + s_codigo + "' order by t.zon_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAZONA);
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
        return objZona;
    }
    
    public Zona buscaZonaxDiaVisita(String diavisita,String zona) throws SQLException {
    	 String SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                 + " and t.zon_est=1 and t.zon_id='"+zona+"' and t.zon_dvis=" + diavisita + " order by t.zon_id";
         try {
             con = new ConectaBD().conectar();
             st = con.createStatement();
             rs = st.executeQuery(SQL_BUSCAZONA);
             objlstZonas = new ListModelList<Zona>();
             while (rs.next()) {
                 objZona = new Zona();
                 objZona.setZon_key(rs.getLong("zon_key"));
                 objZona.setZon_id(rs.getString("zon_id"));
                 objZona.setSuc_id(rs.getInt("suc_id"));
                 objZona.setEmp_id(rs.getInt("emp_id"));
                 objZona.setZon_des(rs.getString("zon_des"));
                 objZona.setZon_est(rs.getInt("zon_est"));
                 objZona.setZon_idven(rs.getString("zon_idven"));
                 objZona.setZon_apenom(rs.getString("zon_apenom"));
                 objZona.setZon_dvis(rs.getInt("zon_dvis"));
                 objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
                 objZona.setZon_rutid(rs.getString("zon_rutid"));
                 objZona.setZon_canid(rs.getInt("zon_canid"));
                 objZona.setZon_candes(rs.getString("zon_candes"));
                 objZona.setZon_mesid(rs.getInt("zon_mesid"));
                 objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                 objZona.setZon_ubides(rs.getString("zon_ubides"));
                 objZona.setZon_paisid(rs.getString("zon_paisid"));
                 objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                 objZona.setZon_ord(rs.getInt("zon_ord"));
                 objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                 objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                 objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                 objZona.setZon_usumod(rs.getString("zon_usumod"));
                 objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                 objZona.setZon_postid(rs.getInt("zon_postid"));
                 objZona.setZon_postdes(rs.getString("zon_postdes"));
                 objlstZonas.add(objZona);
             }
             LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
         return objZona;
    }
    

    public ListModelList<Zona> listaDiaVisita(int i_caso) throws SQLException {
        String SQL_DIAVISTA;
        if (i_caso == 0) {
        	SQL_DIAVISTA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " and t.zon_est<>" + i_caso + " order by t.zon_id";
        } else {
        	SQL_DIAVISTA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                    + " and t.zon_est=" + i_caso + " order by t.zon_id";
        }
        P_WHERE = SQL_DIAVISTA;
     
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_DIAVISTA);
            objlstZonas = new ListModelList<Zona>();
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                objZona.setZon_des(rs.getString("zon_des").length() < 24 ? rs.getString("zon_des") : rs.getString("zon_des").substring(0, 24));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstZonas.getSize() + " registro(s)");
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
        return objlstZonas;
    }

    
    public Zona buscaDiaVisitaxCodigo(String s_codigo) throws SQLException {        
        String SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                + " and t.zon_est=1 and t.zon_dvis=" + s_codigo + " order by t.zon_id";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAZONA);
            objlstZonas = new ListModelList<Zona>();
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
        return objZona;
    }
    
    public ListModelList<Zona> busquedaDiaVisita(int seleccion, String busqueda,  int estado) throws SQLException {
		String SQL_BUSCAZONA ="";
		
		if(seleccion == 2){
			 SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
					 	   + " and t.zon_est="+estado+"  and t.zon_dvisdes like '" + busqueda + "' order by t.zon_id";
		}
	    try {
	        con = new ConectaBD().conectar();
	        st = con.createStatement();
	        rs = st.executeQuery(SQL_BUSCAZONA);
	        objlstZonas = new ListModelList<Zona>();
	        while (rs.next()) {
	            objZona = new Zona();
	            objZona.setZon_key(rs.getLong("zon_key"));
	            objZona.setZon_id(rs.getString("zon_id"));
	            objZona.setSuc_id(rs.getInt("suc_id"));
	            objZona.setEmp_id(rs.getInt("emp_id"));
	            objZona.setZon_des(rs.getString("zon_des"));
	            objZona.setZon_est(rs.getInt("zon_est"));
	            objZona.setZon_idven(rs.getString("zon_idven"));
	            objZona.setZon_apenom(rs.getString("zon_apenom"));
	            objZona.setZon_dvis(rs.getInt("zon_dvis"));
	            objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
	            objZona.setZon_rutid(rs.getString("zon_rutid"));
	            objZona.setZon_canid(rs.getInt("zon_canid"));
	            objZona.setZon_candes(rs.getString("zon_candes"));
	            objZona.setZon_mesid(rs.getInt("zon_mesid"));
	            objZona.setZon_ubiid(rs.getString("zon_ubiid"));
	            objZona.setZon_ubides(rs.getString("zon_ubides"));
	            objZona.setZon_paisid(rs.getString("zon_paisid"));
	            objZona.setZon_paisdes(rs.getString("zon_paisdes"));
	            objZona.setZon_ord(rs.getInt("zon_ord"));
	            objZona.setZon_nomrep(rs.getString("zon_nomrep"));
	            objZona.setZon_usuadd(rs.getString("zon_usuadd"));
	            objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
	            objZona.setZon_usumod(rs.getString("zon_usumod"));
	            objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
	            objZona.setZon_postid(rs.getInt("zon_postid"));
	            objZona.setZon_postdes(rs.getString("zon_postdes"));
	            objlstZonas.add(objZona);
	        }
	        LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
	    return objlstZonas;
    }

    
    public ListModelList<Zona> listaZonaxDiaVisita(String s_codigo, int estado) throws SQLException {
        String SQL_BUSCAZONA ="";
                
        SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
                     + " and t.zon_est="+estado+" and t.zon_dvis=" + s_codigo + " order by t.zon_id";
       
        
       try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAZONA);
            objlstZonas = new ListModelList<Zona>();
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
        return objlstZonas;
    }
    
    public ListModelList<Zona> busquedaZonaxDiaVisita(int seleccion, String diavisid, String busqueda, int estado) throws SQLException {
    	String SQL_BUSCAZONA ="";
    	
    	if(seleccion == 2){
    		 SQL_BUSCAZONA = "select * from v_listazonas t where t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc()
    				 	   + " and t.zon_est="+estado+" and t.zon_des like '"+busqueda+"' and t.zon_dvis=" + diavisid + " order by t.zon_id";
    	}
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSCAZONA);
            objlstZonas = new ListModelList<Zona>();
            while (rs.next()) {
                objZona = new Zona();
                objZona.setZon_key(rs.getLong("zon_key"));
                objZona.setZon_id(rs.getString("zon_id"));
                objZona.setSuc_id(rs.getInt("suc_id"));
                objZona.setEmp_id(rs.getInt("emp_id"));
                objZona.setZon_des(rs.getString("zon_des"));
                objZona.setZon_est(rs.getInt("zon_est"));
                objZona.setZon_idven(rs.getString("zon_idven"));
                objZona.setZon_apenom(rs.getString("zon_apenom"));
                objZona.setZon_dvis(rs.getInt("zon_dvis"));
                objZona.setZon_dvisdes(rs.getString("zon_dvisdes"));
                objZona.setZon_rutid(rs.getString("zon_rutid"));
                objZona.setZon_canid(rs.getInt("zon_canid"));
                objZona.setZon_candes(rs.getString("zon_candes"));
                objZona.setZon_mesid(rs.getInt("zon_mesid"));
                objZona.setZon_ubiid(rs.getString("zon_ubiid"));
                objZona.setZon_ubides(rs.getString("zon_ubides"));
                objZona.setZon_paisid(rs.getString("zon_paisid"));
                objZona.setZon_paisdes(rs.getString("zon_paisdes"));
                objZona.setZon_ord(rs.getInt("zon_ord"));
                objZona.setZon_nomrep(rs.getString("zon_nomrep"));
                objZona.setZon_usuadd(rs.getString("zon_usuadd"));
                objZona.setZon_fecadd(rs.getTimestamp("zon_fecadd"));
                objZona.setZon_usumod(rs.getString("zon_usumod"));
                objZona.setZon_fecmod(rs.getTimestamp("zon_fecmod"));
                objZona.setZon_postid(rs.getInt("zon_postid"));
                objZona.setZon_postdes(rs.getString("zon_postdes"));
                objlstZonas.add(objZona);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado el registro con codigo : " + objZona.getZon_id() + "");
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
        return objlstZonas;
    }
   
    
}
