package org.me.gj.controller.logistica.informes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Guias;
import org.me.gj.model.logistica.procesos.NotaESCab;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoNotaESInf {

    //Instancias de Objetos
    ListModelList<NotaESCab> objlstNotaESInf = new ListModelList<NotaESCab>();
    ListModelList<Guias> objlstGuias = new ListModelList<Guias>();
    NotaESCab objNotaESCab;
    Guias objGuias;
    ParametrosSalida objParametrosSalida;
    //Variables publicas
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    public static String P_WHERER = "";
    public static String P_TITULO = "";
    public static String P_NOTAES = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoNotaESInf.class);

    //Eventos Primarios - Transaccionales
    //Eventos Secundarios - Listas y validaciones
    public ListModelList<NotaESCab> listaNotasInf(String s_periodo, String s_nescab_tipnotaes) throws SQLException {
        String SQL_LISTANOTAESINF = "";
        //el periodo es un dato obligatorio
        //si eligio fecha + proveedor
        if (s_periodo.isEmpty()) {
            s_periodo = "%%";
        }
        if (!s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTANOTAESINF = "select * from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' and t.nescab_tipnotaes = '" + s_nescab_tipnotaes + "' order by t.nescab_id,t.nescab_fecha";
        }//si eligio proveedor
        else if (s_nescab_tipnotaes.isEmpty()) {
            SQL_LISTANOTAESINF = "select * from codijisa.v_listanotaescab t where t.emp_id='" + objUsuCredential.getCodemp() + "' and t.suc_id='" + objUsuCredential.getCodsuc() + "' "
                    + " and to_char(t.nescab_fecha,'yyyymm') like '" + s_periodo + "' order by t.nescab_id,t.nescab_fecha";
        }
        objlstNotaESInf = new ListModelList<NotaESCab>();
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTANOTAESINF);
            while (rs.next()) {
                objNotaESCab = new NotaESCab();
                objNotaESCab.setNescab_id(rs.getString("nescab_id"));
                objNotaESCab.setNescab_key(rs.getString("nescab_key"));
                objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                objNotaESCab.setNotaes(rs.getString("notaes").length() < 23 ? rs.getString("notaes") : rs.getString("notaes").substring(0, 23));
                objNotaESCab.setEmp_id(rs.getInt("emp_id"));
                objNotaESCab.setSuc_id(rs.getInt("suc_id"));
                objNotaESCab.setNescab_serie(rs.getString("serie"));
                objNotaESCab.setNescab_nroped(rs.getString("nescab_nroped"));
                objNotaESCab.setNescab_fecha(rs.getDate("nescab_fecha"));
                objNotaESCab.setNescab_glosa(rs.getString("nescab_glosa"));
                objNotaESCab.setNescab_situacion(rs.getInt("nescab_situacion"));
                objNotaESCab.setNescab_est(rs.getInt("nescab_est"));
                objNotaESCab.setNescab_ocind(rs.getString("nescab_ocind"));
                objNotaESCab.setNescab_ocnropedkey(rs.getLong("nescab_ocnropedkey"));
                objNotaESCab.setOrd_compra(rs.getString("oc"));
                objNotaESCab.setNescab_tipdoc(rs.getString("nescab_tipdoc") == null ? "" : rs.getString("nescab_tipdoc"));
                objNotaESCab.setNescab_nrodoc(rs.getString("nescab_nrodoc") == null ? "" : rs.getString("nescab_nrodoc"));
                objNotaESCab.setNescab_nroserie(rs.getString("nescab_nroserie") == null ? "" : rs.getString("nescab_nroserie"));
                objNotaESCab.setNescab_provid(rs.getString("nescab_provid") == null ? "" : rs.getString("nescab_provid"));
                objNotaESCab.setNescab_provrazsoc(rs.getString("proveedor").length() < 28 ? rs.getString("proveedor") : rs.getString("proveedor").substring(0, 28));
                objNotaESCab.setNescab_cliid(rs.getString("nescab_cliid"));
                objNotaESCab.setNescab_clinom(rs.getString("cliente") == null ? "" : rs.getString("cliente"));
                objNotaESCab.setNescab_moneda(rs.getInt("nescab_moneda"));
                objNotaESCab.setNescab_tcamb(rs.getDouble("nescab_tcamb"));
                objNotaESCab.setNescab_almori(rs.getString("nescab_almori"));
                objNotaESCab.setNescab_almdes(rs.getString("nescab_almdes"));
                objNotaESCab.setNescab_costeo(rs.getInt("nescab_costeo"));
                objNotaESCab.setNescab_despacho(rs.getInt("nescab_despacho"));
                objNotaESCab.setNescab_sitdes(rs.getString("sitdes"));
                objNotaESCab.setNescab_nrodep(rs.getString("nescab_nrodep"));
                objNotaESCab.setNescab_usuadd(rs.getString("nescab_usuadd"));
                objNotaESCab.setNescab_fecadd(rs.getDate("nescab_fecadd"));
                objNotaESCab.setHora(rs.getTime("nescab_fecha"));
                //objNotaESCab.setGcab_fecadd(new Date().getDate());
                objNotaESCab.setNescab_usumod(rs.getString("nescab_usumod"));
                objNotaESCab.setNescab_fecmod(rs.getDate("nescab_fecmod"));
                objlstNotaESInf.add(objNotaESCab);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstNotaESInf.getSize() + " registro(s)");
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
        return objlstNotaESInf;
    }

    public NotaESCab ListaResumenNotaES(String fecini, String fecfin, String subdir, int tipo) throws SQLException {
        int i_subdir;
        String validaFecha = "", SQL_RESUNOTAES = "", titulo = "", SQL_NOTAES = "", NOTAES = "";
        String notaes = "";
        if (!fecini.isEmpty() && !fecfin.isEmpty()) {
            validaFecha = " and trunc(t.nescab_fecha) between to_date('" + fecini + "','dd/mm/yyyy') and to_date('" + fecfin + "','dd/mm/yyyy') ";
            i_subdir = subdir.length();
            notaes = String.valueOf(subdir.substring(0, i_subdir - 1));
        }

        if (tipo == 1) {
            SQL_NOTAES = "select d.nescab_tipnotaes from tnotaes_cab t, tnotaes_det d , v_listaproductos p "
                    + " where d.pro_id = p.PRO_ID and d.nescab_tipnotaes in(" + notaes + ")" + validaFecha
                    + " and t.emp_id =" + objUsuCredential.getCodemp() + " and  t.suc_id =" + objUsuCredential.getCodsuc()
                    + "group by  d.nescab_tipnotaes";

            SQL_RESUNOTAES = " select  distinct d.PRO_ID,  p.PRO_DES , d.nescab_tipnotaes, "
                    + " round(round(d.nesdet_cant,0)/p.PRO_PRESMINVEN,0) cantidad, mod(round(d.nesdet_cant,0) , p.PRO_PRESMINVEN) fraccion, " 
                    + " sum(d.nesdet_valafe) vafecto, sum(d.nesdet_valina) vinafecto, "
                    + " sum(d.nesdet_vimpto) igv,  sum(d.nesdet_pvta) total "
                    + " from tnotaes_cab t, tnotaes_det d , v_listaproductos p"
                    + " where t.nescab_id = d.nescab_id " 
                    + " and t.emp_id = d.emp_id and t.suc_id = d.suc_id" 
                    + " and d.nescab_tipnotaes in(" + notaes + ") and  d.pro_id = p.PRO_ID  " 
                    + " and d.emp_id =" + objUsuCredential.getCodemp() + " and  d.suc_id =" + objUsuCredential.getCodsuc()
                    +  validaFecha
                    + " group by d.PRO_ID, p.PRO_DES ,d.nescab_tipnotaes,d.nesdet_cant,p.PRO_PRESMINVEN"
                    + " order by d.PRO_ID";

            NOTAES = transformacionNotaES(notaes);
            titulo = "REPORTE POR NOTA E/S POR ARTICULOS\n DESDE " + fecini + " HASTA " + fecfin;

        } else if (tipo == 2) {
            SQL_RESUNOTAES = "select "
                    + "t.nescab_tipnotaes notaes,tt.tab_subdes notaesdes, t.nescab_id nnotaes, lpad(t.nescab_tipdoc,2,0) tipo, (nvl(t.nescab_nroserie,'')||t.nescab_tipdoc||t.nescab_nrodoc) referencia , "
                    + "t.oc oc, t.nescab_glosa glosa , "
                    + "d.pro_id prodid, p.pro_des prodes, " 
                    + "round(round(d.nesdet_cant,0)/p.PRO_PRESMINVEN,0) cantidad, mod(round(d.nesdet_cant,0) , p.PRO_PRESMINVEN) fraccion, " 
                    + "sum(d.nesdet_valafe) vafecto, sum(d.nesdet_valina) vinafecto , sum(d.nesdet_vimpto) igv, sum(d.nesdet_pvta) total "
                    + "from v_listanotaescab t, tnotaes_det d, tproductos p , "
                    + "(select*from ttabgen x where x.tab_cod=1 and x.tab_id<>0 order by x.tab_id )tt "
                    + "where d.pro_id= p.pro_id and  t.nescab_id = d.nescab_id and t.nescab_tipnotaes= tt.tab_subdir and p.pro_est<>0 and t.nescab_est = 1 and t.emp_id = d.emp_id "
                    + " and t.emp_id=" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + "and  t.nescab_tipnotaes  in (" + notaes + ")" + validaFecha
                    + "group by "
                    + "t.nescab_tipnotaes , tt.tab_subdes, t.nescab_id , t.nescab_tipdoc , (t.nescab_tipdoc||t.nescab_nrodoc)  ,"
                    + "t.oc , t.nescab_glosa  , t.nescab_nroserie, t.nescab_nrodoc ,"
                    + "d.pro_id , p.pro_des, d.nesdet_cant,p.PRO_PRESMINVEN "
                    + "order by nnotaes";

            titulo = "DETALLE NOTA E/S - ARTICULOS\n DESDE " + fecini + " HASTA " + fecfin;

        } else if (tipo == 3) {
            SQL_RESUNOTAES = 
            		"select distinct t.nescab_tipnotaes, t.notaes,t.nescab_id, lpad(t.nescab_tipdoc,2,0) nescab_tipdoc, t.nescab_fecha, t.nescab_nrodoc, "
            		+" d.nesdet_undpre, d.cantidad, d.fraccion, d.vafecto , d.vinafecto, d.igv , d.total "
            		+" from v_listanotaescab t, (select x.nescab_id, sum(x.nesdet_undpre) nesdet_undpre,sum(x.cant_ent) cantidad, sum(x.cant_frac) fraccion,  sum(x.nesdet_valafe) vafecto, sum(x.nesdet_valina) vinafecto , sum(x.nesdet_vimpto) igv, sum(x.nesdet_pvta) total from v_listanotaesdet x group by x.nescab_id) d "
            		+" where t.nescab_id = d.nescab_id "
            		+" and t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() 
            		+" and t.nescab_tipnotaes in ("+notaes+") "            		
            		+" order by t.nescab_tipnotaes";
            		
            		/*"select "
                    + " t.nescab_tipnotaes , tt.tab_subdes ,  t.nescab_id, t.nescab_fecha, lpad(t.nescab_tipdoc,2,0) nescab_tipdoc, t.nescab_nrodoc, sum(p.PRO_PRESMINVEN) nesdet_undpre, "
                    + " round(round(d.nesdet_cant,0)/p.PRO_PRESMINVEN,0) cantidad, mod(round(d.nesdet_cant,0) , p.PRO_PRESMINVEN) fraccion,  sum(d.nesdet_valafe) vafecto, sum(d.nesdet_valina) vinafecto , sum(d.nesdet_vimpto) igv, sum(d.nesdet_pvta) total"
                    + " from v_listanotaescab t, tnotaes_det d, tproductos p , "
                    + " (select*from ttabgen x where x.tab_cod=1 and x.tab_id<>0 order by x.tab_id )tt "
                    + " where d.pro_id= p.pro_id and  t.nescab_id = d.nescab_id and t.nescab_tipnotaes= tt.tab_subdir and p.pro_est<>0 and t.nescab_est = 1"
                    + " and t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() + " and  t.nescab_tipnotaes  in (" + notaes + ") and d.nescab_tipnotaes = t.nescab_tipnotaes" + validaFecha
                    + " group by t.nescab_tipnotaes , tt.tab_subdes ,  t.nescab_id, t.nescab_fecha,t.nescab_tipdoc, t.nescab_nrodoc , d.nesdet_cant,p.PRO_PRESMINVEN "
                    + " order by t.nescab_id";*/

            titulo = "LISTADO POR NOTA E/S - DOC.REFERENCIA\n DESDE " + fecini + " HASTA " + fecfin;
        } else if (tipo == 4) {
            SQL_RESUNOTAES = "select distinct t.nescab_tipnotaes, t.notaes, d.cantidad, d.fraccion, d.vafecto , d.vinafecto, d.igv , d.total "
            		+" from v_listanotaescab t, (select x.nescab_tipnotaes,sum(x.cant_ent) cantidad, sum(x.cant_frac) fraccion,  sum(x.nesdet_valafe) vafecto, sum(x.nesdet_valina) vinafecto , sum(x.nesdet_vimpto) igv, sum(x.nesdet_pvta) total from v_listanotaesdet x group by x.nescab_tipnotaes) d "
            		+" where t.nescab_tipnotaes = d.nescab_tipnotaes "
            		+" and t.emp_id =" + objUsuCredential.getCodemp() + " and t.suc_id=" + objUsuCredential.getCodsuc() 
            		+" and t.nescab_tipnotaes in ("+notaes+") "            		
            		+" order by t.nescab_tipnotaes";            		
            titulo = "RESUMEN POR NOTA E/S \n DESDE " + fecini + " HASTA " + fecfin;
        }
        P_NOTAES = NOTAES;
        P_WHERER = SQL_RESUNOTAES;
        P_TITULO = titulo;
        objNotaESCab = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            if (tipo == 1) {
                rs = st.executeQuery(SQL_NOTAES);
                while (rs.next()) {
                    objNotaESCab = new NotaESCab();
                }
            } else if (tipo == 2) {
                rs = st.executeQuery(SQL_RESUNOTAES);
                while (rs.next()) {
                    objNotaESCab = new NotaESCab();
                    objNotaESCab.setNescab_tipnotaes(rs.getString("nnotaes"));
                }
            } else {
                rs = st.executeQuery(SQL_RESUNOTAES);
                while (rs.next()) {
                    objNotaESCab = new NotaESCab();
                    objNotaESCab.setNescab_tipnotaes(rs.getString("nescab_tipnotaes"));
                }
            }
        } catch (SQLException ex) {
            Messagebox.show("Error de Carga de Datos debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + ex.toString());
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return objNotaESCab;
    }

    public String transformacionNotaES(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            result += (c == ',') ? '-' : c;
        }
        return result;
    }
}