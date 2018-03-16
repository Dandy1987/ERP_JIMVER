package org.me.gj.controller.planillas.utilitarios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.log4j.Logger;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.mantenimiento.Bancos;
import org.me.gj.model.planillas.mantenimiento.PerPago;
import org.me.gj.model.planillas.utilitarios.ModelUtiComision;
import org.me.gj.model.planillas.utilitarios.UtiNroCuenta;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author HUALLPA
 */
public class DaoEnlaces {

    //Instancias de objetos
    ListModelList<PerPago> objListPerPago;
    ListModelList<Bancos> objListBancos;
    ListModelList<UtiNroCuenta> objListNrocta;
    ListModelList<ModelUtiComision> objlstComi;
    PerPago objPerPago;
    Bancos objBancos;
    UtiNroCuenta objNrocta;
    ModelUtiComision objComision;
 ArrayDescriptor arrayC, arrayCM;
    ARRAY arrC, arrCM;
     int i_flagErrorBD = 0;
    String s_msg = "";
    //Variables jdbc
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    //Variables Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoEnlaces.class);

    /**
     * Método para cargar datos a un componente de selección para revertir el
     * cierre de planilla. - chuallpa
     *
     * @param f1
     * @return Retorna una lista con los periodos de pago en situación de
     * cerrado.
     * @throws SQLException
     */
    public ListModelList<PerPago> periodoCerrado(boolean f1) throws SQLException {
        String filtro1 = f1 ? " and substr(t.plppag_id,7,2) = '03' \n" : "";
        String query;
        query = " select r.plppag_id, r.plppag_desc \n"
                + " from ( \n"
                + " select t.plppag_id, t.plppag_desc \n"
                + " from tplperpag t \n"
                + " where t.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + " and t.plppag_estado = 1 \n"
                + " and t.plppag_situ = '3' \n"
                + filtro1
                + " union \n"
                + " select '', '' \n"
                + " from tplperpag t \n"
                + " where t.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + " and t.plppag_estado = 1 \n"
                + " and t.plppag_situ = '3' \n"
                + filtro1
                + " ) r \n"
                + " order by r.plppag_id desc";

        objListPerPago = new ListModelList<PerPago>();

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                objPerPago = new PerPago();
                objPerPago.setPeriodoProceso(rs.getString("plppag_id"));
                objListPerPago.add(objPerPago);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objListPerPago.getSize() + " registros(s)");
        } catch (SQLException e) {
            Messagebox.show(" Error de Búsqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return objListPerPago;
    }

    /**
     * Método para obtener el id y descripción del periodo de pago. - chuallpa
     *
     * @param per_id Parametro id del periodo de pago.
     * @param f1 Parametro para varias el filtro.
     * @return El id y descripción del periodo de pago.
     * @throws SQLException
     */
    public String periodoCerradoDesc(String per_id, boolean f1) throws SQLException {
        String filtro1 = f1 ? "in ('3','4')" : "= '3'";
        String desc = "----------";
        String query;
        query = " select t.plppag_id, t.plppag_desc \n"
                + "from tplperpag t \n"
                + "where t.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + "and t.plppag_estado = 1 \n"
                + "and t.plppag_situ " + filtro1 + " \n"
                + "and t.plppag_id = '" + per_id + "'";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (f1) {
                    desc = rs.getString("plppag_desc");
                } else {
                    desc = rs.getString("plppag_id");
                    desc = desc + rs.getString("plppag_desc");
                }
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y no encontró la descripción del periodo " + per_id);
        } catch (SQLException e) {
            Messagebox.show(" Error de Búsqueda de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } catch (NullPointerException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo pudo visualizar los datos debido al error " + e.toString());
        } finally {
            if (con != null) {
                con.close();
                st.close();
                rs.close();
            }
        }
        return desc;
    }

    public int validaVoucher(int emp, String periodo, String mes, String id_voucher, String nro_vol) {
        String empresa = String.valueOf(emp);
        int consulta = 0;
        String query = "";
        query = "{?=call pack_tvoucher_cab.f_existe(?,?,?,?,?)}";

        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, empresa);
            cst.setString(3, periodo);
            cst.setString(4, mes);
            cst.setString(5, id_voucher);
            cst.setString(6, nro_vol);
            cst.execute();
            consulta = cst.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Intentó validar la existencia de un voucher | " + objUsuCredential.getEmpresa() + " | En el periodo: " + periodo + " - " + e.toString());
            Messagebox.show("Error de Carga de Datos debido al Error: " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            consulta = 0;
        } finally {
            if (con != null) {
                try {
                    con.close();
                    cst.close();
                } catch (SQLException ex) {
                    Messagebox.show("Error de conexión debido al Error: " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
        return consulta;
    }

    public String generaContab(String suc, String periodos, String periodo, String fecha) throws SQLException {
        String query, mnsj = "";
        query = "{call pack_tcalculoplla.p_linkconta(?,?,?,?,?)}";
        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setString(2, suc);
            cst.setString(3, periodos);
            cst.setString(4, periodo);
            cst.setString(5, fecha);
            cst.execute();
            mnsj = "¡Generación exitosa!";
        } catch (SQLException e) {
            mnsj = "Error al generar el enlace de Contabilidad debido al Error " + e.toString();
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return mnsj;
    }

    /**
     * Metodo que devuelve el periodo calculado segun el codigo de la empresa
     * Llama a funcion call pack_tperpag.f_periodo_calculado(?)
     *
     * @param i_cod_empresa Codigo de la empresa
     * @return periodo calculado
     * @throws SQLException
     */
    public String getPeriodoCalculado(int i_cod_empresa) throws SQLException {
        String s_periodo_calculado = "";
        try {
            con = (new ConectaBD()).conectar();
            String sql_query = "{?=call pack_tperpag.f_periodo_calculado(?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, i_cod_empresa);
            cst.execute();
            s_periodo_calculado = cst.getString(1);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } catch (NullPointerException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return s_periodo_calculado;

    }

    /**
     * Método para obtener el formato para realizar el enlace. Actualmente solo
     * se trabaja con el key de banco '11'.
     *
     * @param ban_key Es el key de la tabla tbancos.
     * @return Una lista de Bancos con el formato para para el enlace.
     * @autor Carlos H.
     */
    public ListModelList<Bancos> getFormatoBancos(int ban_key) {

        String query = "select x.* from\n"
                + "(\n"
                + "select t.tabla_id, t.tabla_descri, t.tabla_valor3 from tpltablas1 t\n"
                + "where t.tabla_cod = '00032'\n"
                + "and t.tabla_id <> '000'\n"
                + "and t.tabla_estado = 1\n"
                + "and t.tabla_valor3 = " + ban_key + "\n"
                + "UNION\n"
                + "select null,'',null from dual\n"
                + ") x\n"
                + "order by x.tabla_id asc";

        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);

            objListBancos = null;
            objListBancos = new ListModelList<Bancos>();

            while (rs.next()) {
                objBancos = new Bancos();
                objBancos.setKey(rs.getInt("tabla_valor3"));;
                objBancos.setId_formato(rs.getString("tabla_id"));
                objBancos.setDesc_formato(rs.getString("tabla_descri"));
                objListBancos.add(objBancos);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                try {
                    st.close();
                    rs.close();
                    con.close();
                } catch (SQLException ex) {
                    Messagebox.show("Error al cerrar la conexión debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }

        return objListBancos;
    }

    /**
     * Método para listar los números de cuenta con el key de la tabla tbancos y
     * la empresa para el enlace.
     *
     * @param ban_key
     * @return Lista con los números de cuenta del banco seleccioado por
     * empresa.
     * @autor Carlos H.
     */
    public ListModelList<UtiNroCuenta> listaNroCuenta(int ban_key) {

        String query = "select c.plcb_glosa, g.tab_nomrep, c.plcb_nrocta from tplctasban c, ttabgen g\n"
                + "where c.plcb_tipmon = g.tab_subdir\n"
                + "and c.emp_id = " + objUsuCredential.getCodemp() + "\n"
                + "and c.ban_key = " + ban_key + "\n"
                + "and c.plcb_estado = 1\n"
                + "and g.tab_cod = 24\n"
                + "and g.tab_est = 1\n"
                + "and g.tab_id <> '0'";

        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(query);

            objListNrocta = null;
            objListNrocta = new ListModelList<UtiNroCuenta>();

            while (rs.next()) {
                objNrocta = new UtiNroCuenta();
                objNrocta.setS_glosa(rs.getString("plcb_glosa"));
                objNrocta.setS_tipmon(rs.getString("tab_nomrep"));
                objNrocta.setS_nrocta(rs.getString("plcb_nrocta"));
                objListNrocta.add(objNrocta);
            }

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                try {
                    st.close();
                    rs.close();
                    con.close();
                } catch (SQLException ex) {
                    Messagebox.show("Error al cerrar la conexión debido al Error " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
        return objListNrocta;
    }

    public int enlaceComision(int emp, String sucursal, String periodo) {
        int valor = 0;
        String empresa = String.valueOf(emp);
        String query = "";
        query = "{?= call pack_tcalculoplla.f_linkcomiJimver(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, empresa);
            cst.setString(3, sucursal);
            cst.setString(4, periodo);
            cst.execute();
            valor = cst.getInt(1);

        } catch (Exception e) {
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Intentó revertir el cierre de planilla | " + objUsuCredential.getEmpresa() + " | En el periodo: " + periodo + e.toString());
            Messagebox.show("Error de Carga de Datos debido al Error: " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            valor = 0;
        } finally {
            if (con != null) {
                try {
                    con.close();
                    cst.close();
                } catch (SQLException ex) {
                    Messagebox.show("Error de conexión debido al Error: " + ex.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
        return valor;
    }

    /**
     * Funcion para traer lsita de comision con link
     * @param empresa
     * @param sucursal
     * @param periodo
     * @return 
     * @throws java.sql.SQLException 
     */
    public ListModelList<ModelUtiComision> listaComision(int empresa, String sucursal, String periodo) throws SQLException {
        String query;
        try {
            con = new ConectaBD().conectar();
            query = "{?= call codijisa.pack_tcomision.f_listacomision(?,?,?)}";
            cst = con.prepareCall(query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setInt(2, empresa);
            cst.setString(3, sucursal);
            cst.setString(4, periodo);
            cst.execute();
            String consulta = cst.getString(1);
            objlstComi = new ListModelList<ModelUtiComision>();
            st = con.createStatement();
            rs = st.executeQuery(consulta);
            while (rs.next()) {
                objComision = new ModelUtiComision();
                objComision.setCod_vendedor(rs.getString("cod_ven"));
                objComision.setDni(rs.getString("dni"));
                objComision.setEmpresa(rs.getInt("empresa"));
                objComision.setEncriptado(rs.getString("encriptado"));
                objComision.setPeriodo(rs.getString("periodo"));
                objComision.setPersonal(rs.getString("personal"));
                objComision.setSucursal(rs.getInt("sucursal"));
                objComision.setTipo_doc(rs.getInt("tipo_doc"));
                objComision.setValor(rs.getDouble("valor"));
                objlstComi.add(objComision);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }

        return objlstComi;
    }

    
      public ParametrosSalida insertarComisiones(Object[][] listaConstante,int empresa, String sucursal) throws SQLException {

        String query = "{call pack_tcomision.p_insertar_comisiones(?,?,?,?,?)}";

        try {
            con = (new ConectaBD()).conectar();
            cst = con.prepareCall(query);
            arrayC = ArrayDescriptor.createDescriptor("LISTACOMISIONES", con.getMetaData().getConnection());
            arrC = new ARRAY(arrayC, con.getMetaData().getConnection(), listaConstante);

            cst.clearParameters();
            cst.setArray(1, arrC);
            cst.setInt(2, empresa);
            cst.setString(3, sucursal);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.registerOutParameter(5, java.sql.Types.NUMERIC);
            cst.execute();
            s_msg = cst.getString(4);
            i_flagErrorBD = cst.getInt(5);
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);

    }
}
