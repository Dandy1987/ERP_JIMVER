package org.me.gj.controller.planillas.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.planillas.procesos.RegAsistencia;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class DaoRegAsistencia {

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    String s_msg = "";
    RegAsistencia objAsistencia;
    ListModelList<RegAsistencia> objlstAsistencia;
    Session sesion = Sessions.getCurrent();
    private final UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");

    public ListModelList<RegAsistencia> listaRegistro() throws SQLException {
        String query = "{call codijisa.pack_planilla_regAsistencia.p_ver_registro(?,?)}";
        try {
            con = (new ConectaBD().conectar());
            cst = con.prepareCall(query);
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            cst.executeQuery();
            rs = ((OracleCallableStatement) cst).getCursor(2);
            objlstAsistencia = null;
            objlstAsistencia = new ListModelList<RegAsistencia>();
            while (rs.next()) {
                objAsistencia = new RegAsistencia();
                objAsistencia.setDni(rs.getString("dni"));
                objAsistencia.setNombres(rs.getString("nombres"));
                objAsistencia.setHor_reg(rs.getString("hora"));

                objlstAsistencia.add(objAsistencia);
            }
        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "JIPESA", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                rs.close();
                con.close();
            }
        }

        return objlstAsistencia;

    }

    public RegAsistencia getPersonal(String dni) throws SQLException {
        String sql_query = " select to_number(COUNT(*))cant,p.plapepat||' '||p.plapemat||' '||p.plnomemp nombres,"
                + " p.pltipdoc tipo,p.plnrodoc dni, d.suc_id, d.plhorari"
                + " from tpersonal p, tpldatoslab d"
                + " where p.pltipdoc=d.pltipdoc"
                + " and p.plnrodoc=d.plnrodoc"
                + " and p.plestado = 1"
                + " and d.plestado_dl = 1"
                + " and d.emp_id=" + objUsuCredential.getCodemp()
                + " and p.plnrodoc = '" + dni + "'"
                + " group by p.plapepat,p.plapemat,p.plnomemp,p.pltipdoc,p.plnrodoc,d.suc_id,d.plhorari";
        try {

            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql_query);
            objAsistencia = null;
            while (rs.next()) {
                objAsistencia = new RegAsistencia();
                objAsistencia.setTipo_doc(rs.getInt("tipo"));
                objAsistencia.setDni(rs.getString("dni"));
                objAsistencia.setNombres(rs.getString("nombres"));
                objAsistencia.setSuc_id(rs.getInt("suc_id"));
                objAsistencia.setCodhor(rs.getString("plhorari"));
                // objAsistencia.setFlag(rs.getString("flag"));
                //  objAsistencia.setFec_mov(rs.getDate("rg_fechmov"));

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

        return objAsistencia;

    }

    public String verifica_personal(String dni) throws SQLException {
        int existe = 0;
        String nombre = "";
        String SQL_PERSONAL = " select to_number(COUNT(*))cant,p.plapepat||' '||p.plapemat||' '||p.plnomemp nombres"
                + " from tpersonal p, tpldatoslab d"
                + " where p.pltipdoc=d.pltipdoc"
                + " and p.plnrodoc=d.plnrodoc"
                + " and p.plestado = 1"
                + " and d.plestado_dl = 1"
                + " and d.emp_id=" + objUsuCredential.getCodemp()
                + " and d.suc_id =" + objUsuCredential.getCodsuc()
                + " and p.plnrodoc=" + dni
                + " group by p.plapepat,p.plapemat,p.plnomemp";
        try {
            con = new ConectaBD().conectar();
            if (con != null) {
                st = con.createStatement();
                rs = st.executeQuery(SQL_PERSONAL);
                while (rs.next()) {
                    existe = rs.getInt("cant");
                    nombre = rs.getString("nombres");
                }
            }
            if (existe != 1) {
                nombre = "no";
            }

        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
        } finally {
            if (con != null) {
                st.close();
                rs.close();
                con.close();
            }
        }
        return nombre;
    }

    public ParametrosSalida insertar(RegAsistencia objAsistencia) throws SQLException {

        String insertar_query = "{call codijisa.pack_planilla_regAsistencia.p_ingresa_marcado(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(insertar_query);
            cst.clearParameters();
            cst.setInt(1, objAsistencia.getEmp_id());
            cst.setInt(2, objAsistencia.getSuc_id());
            cst.setInt(3, objAsistencia.getTipo_doc());
            cst.setString(4, objAsistencia.getDni());
            cst.setString(5, objUsuCredential.getCuenta());
            cst.setString(6, objAsistencia.getFlag());
            cst.setString(7, objAsistencia.getCodhor());
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);

        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();

        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg);
    }

    //metodo para verfificar si ya esta registrado o ya marco
    public int verificaIngreso(int tipo, String dni, String flag, String fecha) throws SQLException {
        int valor = 0;
        String sql = " select count(x.reg_nrodoc) as cant from tas_regasis x"
                + " where x.reg_tipdoc =" + tipo
                + " and x.reg_nrodoc =" + dni
                + " and x.rg_flag =" + flag
                + " and x.reg_emp_id =" + objUsuCredential.getCodemp()
                + " and to_char(x.rg_fechmov,'dd/mm/yyyy') ='" + fecha + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getInt("cant");
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

        return valor;

    }

    public String getNombre(String nombre) throws SQLException {
        String trabajador = "";

        try {
            con = new ConectaBD().conectar();
            String sql_query = "{?=call codijisa.pack_planilla_regAsistencia.f_get_nombre(?)}";
            cst = con.prepareCall(sql_query);
            cst.clearParameters();
            cst.registerOutParameter(1, java.sql.Types.VARCHAR);
            cst.setString(2, nombre);
            cst.execute();
            trabajador = cst.getString(1);

        } catch (SQLException e) {
            Messagebox.show("Error de Carga de Datos debido al Error " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
        } finally {
            if (con != null) {
                cst.close();
                con.close();
                rs.close();
            }
        }
        return trabajador;
    }

}
