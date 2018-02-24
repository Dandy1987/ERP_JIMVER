package org.me.gj.controller.logistica.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.informes.StockTmp;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

public class DaoStockTmp {

    //Instancias de Objetos
    ListModelList<StockTmp> objlstStock;
    StockTmp objStockTmp;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoStockTmp.class);

    //Eventos Primarios - Transaccionales
    public ParametrosSalida generarStocks(ListModelList<StockTmp> objlstStock, int emp_id, int suc_id, String periodo, String almacen, String ubicacion, String proveedor, String valorizado, String impuesto, String orden, String inactivos, String stocks, String bonificacion) throws SQLException {
        s_msg = "";
        cst = null;
        con = null;
        //objlstStock = new ListModelList<StockTmp>();
        String SQL_GENERAR_STOCKS = "{call pack_tstocks.p_stocks(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_GENERAR_STOCKS);
            cst.clearParameters();
            cst.setInt("n_emp_id", emp_id);
            cst.setInt("n_suc_id", suc_id);
            cst.setString("c_per_id", periodo);
            cst.setString("c_alm_id", almacen);
            cst.setString("c_ubi_id", ubicacion);
            cst.setString("c_prov_id", proveedor);
            cst.setDate("d_fecha", null);
            cst.setString("cValorizado", valorizado);
            cst.setString("cImpuesto", impuesto);
            cst.setString("cInactivos", inactivos);
            cst.setString("cStocks", stocks);
            cst.setString("cVentas", "");
            cst.setString("cBonificacion", bonificacion);
            cst.setString("cCompra", "");
            cst.registerOutParameter("n_Flag", java.sql.Types.NUMERIC);
            cst.registerOutParameter("c_msg", java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString("c_msg");
            i_flagErrorBD = cst.getInt("n_Flag");
            if (i_flagErrorBD == 0) {
                st = con.createStatement();
                String SQL_STOCKS = "select * from ( select t.emp_id,t.suc_id,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.pro_id,t.pro_des,t.pro_presminven, "
                        + " t.pro_clas,t.ubic_id,t.stock_ent,t.stock_fra,t.stock_min,t.stock_max,nvl(t.stock_dias,0) stock_dias,nvl(t.stock_mes,0) stock_mes, t.costo,t.costotot,t.ventas,"
                        + " t.ventasprom,t.ventashist,t.pendiente,t.desplazamiento,t.tmp_usuadd,t.tmp_fecadd,t.tmp_usumod,t.tmp_fecmod,"
                        + " sum(t.stock_ent+t.stock_fra) stock,p.pro_est estado from ttmp_stocks t ,tproductos p where t.pro_id=p.pro_id  "
                        + " group by t.emp_id,t.suc_id,t.prov_id,t.prov_razsoc,t.lin_id,t.lin_des,t.pro_id,t.pro_des,t.pro_presminven,t.pro_clas,"
                        + " t.ubic_id,t.stock_ent,t.stock_fra,t.stock_min,t.stock_max,t.stock_dias,t.stock_mes, t.costo,t.costotot,t.ventas,t.ventasprom,"
                        + " t.ventashist,t.pendiente,t.desplazamiento,t.tmp_usuadd, t.tmp_fecadd,t.tmp_usumod,t.tmp_fecmod,p.pro_est"
                        + " ) x "
                        + " where x.emp_id = " + emp_id + " and x.suc_id=" + suc_id + " and x.estado in(" + inactivos + ") " + " " + bonificacion + " " + orden;
                P_WHERER = SQL_STOCKS;
                P_TITULO = "REPORTE DE STOCKS";
                rs = st.executeQuery(SQL_STOCKS);
                while (rs.next()) {
                    objStockTmp = new StockTmp();
                    objStockTmp.setEmp_id(rs.getInt("emp_id"));
                    objStockTmp.setSuc_id(rs.getInt("suc_id"));
                    objStockTmp.setProv_id(rs.getString("prov_id"));
                    objStockTmp.setProv_razsoc(rs.getString("prov_razsoc"));
                    objStockTmp.setLin_id(rs.getString("lin_id"));
                    objStockTmp.setLin_des(rs.getString("lin_des"));
                    objStockTmp.setPro_id(rs.getString("pro_id"));
                    objStockTmp.setPro_des(rs.getString("pro_des"));
                    objStockTmp.setPro_presminven(rs.getInt("pro_presminven"));
                    objStockTmp.setPro_clas(rs.getString("pro_clas"));
                    objStockTmp.setUbic_id(rs.getString("ubic_id"));
                    objStockTmp.setStock_ent(rs.getLong("stock_ent"));
                    objStockTmp.setStock_fra(rs.getLong("stock_fra"));
                    objStockTmp.setStock_min(rs.getLong("stock_min"));
                    objStockTmp.setStock_max(rs.getLong("stock_max"));
                    objStockTmp.setStock_dias(rs.getLong("stock_dias"));
                    objStockTmp.setStock_mes(rs.getLong("stock_mes"));
                    objStockTmp.setCosto(rs.getDouble("costo"));
                    objStockTmp.setCostotot(rs.getDouble("costotot"));
                    objStockTmp.setVentas(rs.getDouble("ventas"));
                    objStockTmp.setVentasprom(rs.getDouble("ventasprom"));
                    objStockTmp.setVentashist(rs.getDouble("ventashist"));
                    objStockTmp.setPendiente(rs.getInt("pendiente"));
                    objStockTmp.setDesplazamiento(rs.getString("desplazamiento"));
                    objStockTmp.setTmp_usuadd(rs.getString("tmp_usuadd"));
                    objStockTmp.setTmp_fecadd(rs.getDate("tmp_fecadd"));
                    objStockTmp.setTmp_usumod(rs.getString("tmp_usumod"));
                    objStockTmp.setTmp_fecmod(rs.getDate("tmp_fecmod"));
                    objlstStock.add(objStockTmp);
                }
                LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | genero Stocks ");
            } else {
                objlstStock = null;
            }
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el reporte de Stocks debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo generar el reporte de Stocks porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

}
