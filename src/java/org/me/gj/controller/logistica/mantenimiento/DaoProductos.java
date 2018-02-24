package org.me.gj.controller.logistica.mantenimiento;

import java.sql.*;
import org.me.gj.database.ConectaBD;
import org.me.gj.model.logistica.mantenimiento.Productos;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.apache.log4j.Logger;
import org.me.gj.model.seguridad.utilitarios.UsuariosCredential;
import org.me.gj.util.ParametrosSalida;

public class DaoProductos {

    //Instancias de Objetos
    ListModelList<Productos> objlstProductos;
    Productos objProducto;
    //Variables publicas    
    Connection con;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cst = null;
    int i_flagErrorBD = 0;
    int cant_Stock = 0;
    String s_msg = "";
    public static String P_WHERE = "";
    //Variables de Sesion
    Session sesion = Sessions.getCurrent();
    UsuariosCredential objUsuCredential = (UsuariosCredential) sesion.getAttribute("usuariosCredential");
    private static final Logger LOGGER = Logger.getLogger(DaoProductos.class);

    //Eventos Primarios - Transaccionales
    public String[] insertarProducto(Productos objProducto) throws SQLException {
        String valores[] = new String[2];
        String s_des = objProducto.getPro_des();
        String s_desdes = objProducto.getPro_desdes();
        String s_descor = objProducto.getPro_descor();
        String i_lin = objProducto.getPro_lin();
        String i_sublin = objProducto.getPro_sublin();
        String i_mar = objProducto.getPro_mar();
        String s_clas = objProducto.getPro_clas();
        String s_tip = objProducto.getPro_tip();
        int i_ordkar = objProducto.getPro_ordkar();
        int i_ordlstprec = objProducto.getPro_ordlstprec();
        int i_ordcons = objProducto.getPro_ordcons();
        int i_ord = objProducto.getPro_ord();
        int i_tipexisun = objProducto.getPro_tipexisun();
        int i_indperc = objProducto.getPro_indperc();
        String s_medunisun = objProducto.getPro_medunisun();
        String i_provid = objProducto.getPro_provid();
        String s_codori = objProducto.getPro_codori();
        String s_conv = objProducto.getPro_conv();
        int i_unimas = objProducto.getPro_unimas();
        String s_unimanven = objProducto.getPro_unimanven();
        String s_empindven = objProducto.getPro_empindven();
        int i_presminven = objProducto.getPro_presminven();
        int i_unibodeguero = objProducto.getPro_unibodeguero();
        String s_frac = objProducto.getPro_frac();
        String s_proc = objProducto.getPro_proc();
        int i_impid = objProducto.getImp_id();
        String s_condimp = objProducto.getPro_condimp();
        int i_ubi = objProducto.getPro_ubi();
        double d_alt = objProducto.getPro_alt();
        double d_anc = objProducto.getPro_anc();
        double d_lar = objProducto.getPro_lar();
        int s_rot = objProducto.getPro_rot();
        double s_peso = objProducto.getPro_peso();
        String s_unipeso = objProducto.getPro_unipeso();
        double d_vol = objProducto.getPro_vol();
        String s_unimancom = objProducto.getPro_unimancom();
        String s_empindcom = objProducto.getPro_empindcom();
        int i_presmincom = objProducto.getPro_presmincom();
        int i_scknofact = objProducto.getPro_scknofact();
        String s_codbar = objProducto.getPro_codbar();
        String s_tipcodbar = objProducto.getPro_tipcodbar();
        String s_imcodbar = objProducto.getPro_imgcodbar();
        String s_ingrod = "";
        String s_img = objProducto.getPro_img();
        String s_usuadd = objProducto.getPro_usuadd();
        int i_afecam = objProducto.getPro_afecam();
        int i_proprovpresu = objProducto.getPro_provpresukey();
        String s_codean13prov = objProducto.getPro_codean13prov();
        String s_codean14prov = objProducto.getPro_codean14prov();

        cst = null;
        i_flagErrorBD = 0;
        String SQL_INSERT_PRODUCTOS = "{call pack_tproductos.p_insertarProducto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_INSERT_PRODUCTOS);
            cst.clearParameters();
            cst.setString(1, s_des);
            cst.setString(2, s_desdes);
            cst.setString(3, s_descor);
            cst.setString(4, i_lin);
            cst.setString(5, i_sublin);
            cst.setString(6, i_mar);
            cst.setString(7, s_clas);
            cst.setString(8, s_tip);
            cst.setInt(9, i_ordkar);
            cst.setInt(10, i_ordlstprec);
            cst.setInt(11, i_ordcons);
            cst.setInt(12, i_ord);
            cst.setInt(13, i_tipexisun);
            cst.setInt(14, i_indperc);
            cst.setString(15, s_medunisun);
            cst.setString(16, i_provid);
            cst.setString(17, s_codori);
            cst.setString(18, s_conv);
            cst.setInt(19, i_unimas);
            cst.setString(20, s_unimanven);
            cst.setString(21, s_empindven);
            cst.setInt(22, i_presminven);
            cst.setInt(23, i_unibodeguero);
            cst.setString(24, s_frac);
            cst.setString(25, s_proc);
            cst.setInt(26, i_impid);
            cst.setString(27, s_condimp);
            cst.setInt(28, i_ubi);
            cst.setDouble(29, d_alt);
            cst.setDouble(30, d_anc);
            cst.setDouble(31, d_lar);
            cst.setInt(32, s_rot);
            cst.setDouble(33, s_peso);
            cst.setString(34, s_unipeso);
            cst.setDouble(35, d_vol);
            cst.setString(36, s_unimancom);
            cst.setString(37, s_empindcom);
            cst.setInt(38, i_presmincom);
            cst.setInt(39, i_scknofact);
            cst.setString(40, s_codbar);
            cst.setString(41, s_tipcodbar);
            cst.setString(42, s_imcodbar);
            cst.setString(43, s_ingrod);
            cst.setString(44, s_img);
            cst.setString(45, s_usuadd);
            cst.setInt(46, i_afecam);
            cst.setInt(47, i_proprovpresu);
            cst.setString(48, s_codean13prov);
            cst.setString(49, s_codean14prov);
            cst.registerOutParameter(50, java.sql.Types.NUMERIC);
            cst.registerOutParameter(51, java.sql.Types.VARCHAR);
            cst.registerOutParameter(52, java.sql.Types.VARCHAR);
            cst.execute();
            valores[0] = cst.getString(51);
            valores[1] = cst.getString(52);
            i_flagErrorBD = cst.getInt(50);

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | creo un nuevo registro con descripción " + s_des);
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
        return valores;
    }

    public String actualizarProducto(Productos objProducto) throws SQLException {
        String i_id = objProducto.getPro_id();
        int s_key = objProducto.getPro_key();
        int i_est = objProducto.getPro_est();
        String s_des = objProducto.getPro_des();
        String s_desdes = objProducto.getPro_desdes();
        String s_descor = objProducto.getPro_descor();
        String i_lin = objProducto.getPro_lin();
        String i_sublin = objProducto.getPro_sublin();
        String i_mar = objProducto.getPro_mar();
        String s_clas = objProducto.getPro_clas();
        String s_tip = objProducto.getPro_tip();
        int i_ordkar = objProducto.getPro_ordkar();
        int i_ordlstprec = objProducto.getPro_ordlstprec();
        int i_ordcons = objProducto.getPro_ordcons();
        int i_ord = objProducto.getPro_ord();
        int i_tipexisun = objProducto.getPro_tipexisun();
        int i_indperc = objProducto.getPro_indperc();
        String s_medunisun = objProducto.getPro_medunisun();
        String i_provid = objProducto.getPro_provid();
        String s_codori = objProducto.getPro_codori();
        String s_conv = objProducto.getPro_conv();
        int i_unimas = objProducto.getPro_unimas();
        String s_unimanven = objProducto.getPro_unimanven();
        String s_empindven = objProducto.getPro_empindven();
        int i_presminven = objProducto.getPro_presminven();
        int i_unibodeguero = objProducto.getPro_unibodeguero();
        String s_frac = objProducto.getPro_frac();
        String s_proc = objProducto.getPro_proc();
        int i_impid = objProducto.getImp_id();
        String s_condimp = objProducto.getPro_condimp();
        int i_ubi = objProducto.getPro_ubi();
        double d_alt = objProducto.getPro_alt();
        double d_anc = objProducto.getPro_anc();
        double d_lar = objProducto.getPro_lar();
        int s_rot = objProducto.getPro_rot();
        double s_peso = objProducto.getPro_peso();
        String s_unipeso = objProducto.getPro_unipeso();
        double d_vol = objProducto.getPro_vol();
        String s_unimancom = objProducto.getPro_unimancom();
        String s_empindcom = objProducto.getPro_empindcom();
        int i_presmincom = objProducto.getPro_presmincom();
        int i_scknofact = objProducto.getPro_scknofact();
        String s_codbar = objProducto.getPro_codbar();
        String s_tipcodbar = objProducto.getPro_tipcodbar();
        String s_imcodbar = objProducto.getPro_imgcodbar();
        String s_inprod = "";
        String s_img = objProducto.getPro_img();
        String s_usumod = objProducto.getPro_usumod();
        int i_afecam = objProducto.getPro_afecam();
        int i_proprovpresu = objProducto.getPro_provpresukey();
        String s_codean13prov = objProducto.getPro_codean13prov();
        String s_codean14prov = objProducto.getPro_codean14prov();

        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_ACTUALIZAR_PRODUCTOS = "{call pack_tproductos.p_actualizarProducto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ACTUALIZAR_PRODUCTOS);
            cst.clearParameters();
            cst.setString(1, i_id);
            cst.setInt(2, s_key);
            cst.setInt(3, i_est);
            cst.setString(4, s_des);
            cst.setString(5, s_desdes);
            cst.setString(6, s_descor);
            cst.setString(7, i_lin);
            cst.setString(8, i_sublin);
            cst.setString(9, i_mar);
            cst.setString(10, s_clas);
            cst.setString(11, s_tip);
            cst.setInt(12, i_ordkar);
            cst.setInt(13, i_ordlstprec);
            cst.setInt(14, i_ordcons);
            cst.setInt(15, i_ord);
            cst.setInt(16, i_tipexisun);
            cst.setInt(17, i_indperc);
            cst.setString(18, s_medunisun);
            cst.setString(19, i_provid);
            cst.setString(20, s_codori);
            cst.setString(21, s_conv);
            cst.setInt(22, i_unimas);
            cst.setString(23, s_unimanven);
            cst.setString(24, s_empindven);
            cst.setInt(25, i_presminven);
            cst.setInt(26, i_unibodeguero);
            cst.setString(27, s_frac);
            cst.setString(28, s_proc);
            cst.setInt(29, i_impid);
            cst.setString(30, s_condimp);
            cst.setInt(31, i_ubi);
            cst.setDouble(32, d_alt);
            cst.setDouble(33, d_anc);
            cst.setDouble(34, d_lar);
            cst.setInt(35, s_rot);
            cst.setDouble(36, s_peso);
            cst.setString(37, s_unipeso);
            cst.setDouble(38, d_vol);
            cst.setString(39, s_unimancom);
            cst.setString(40, s_empindcom);
            cst.setInt(41, i_presmincom);
            cst.setInt(42, i_scknofact);
            cst.setString(43, s_codbar);
            cst.setString(44, s_tipcodbar);
            cst.setString(45, s_imcodbar);
            cst.setString(46, s_img);
            cst.setString(47, s_usumod);
            cst.setInt(48, i_afecam);
            cst.setInt(49, i_proprovpresu);
            cst.setString(50, s_codean13prov);
            cst.setString(51, s_codean14prov);
            cst.registerOutParameter(52, java.sql.Types.NUMERIC);
            cst.registerOutParameter(53, java.sql.Types.VARCHAR);
            cst.execute();
            i_flagErrorBD = cst.getInt(52);
            s_msg = cst.getString(53);

            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | modifico un registro con codigo " + i_id);
        } catch (SQLException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la modificacion porque no existe conexion a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                con.close();
                cst.close();
            }
        }
        return s_msg;
    }

    public String eliminarProducto(Productos objProducto) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_ELIMINAR_PRODUCTO = "{call pack_tproductos.p_eliminarProducto(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_ELIMINAR_PRODUCTO);
            cst.clearParameters();
            cst.setString(1, objProducto.getPro_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | elimino un registro con código " + objProducto.getPro_id());
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

    //Eventos Secundarios - Listas y validaciones
    public Productos buscarProducto(String prod_id, String prov_id) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos t where t.pro_provid like '" + prov_id + "' and t.pro_id = '" + prod_id + "' ";
        objProducto = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString("pro_id"));
                objProducto.setPro_key(rs.getInt("pro_key"));
                objProducto.setPro_est(rs.getInt("pro_est"));
                objProducto.setPro_des(rs.getString("pro_des"));
                objProducto.setPro_desdes(rs.getString("pro_desdes"));
                objProducto.setPro_descor(rs.getString("pro_descor"));
                objProducto.setPro_lin(rs.getString("pro_lin"));
                objProducto.setPro_sublin(rs.getString("pro_sublin"));
                objProducto.setPro_mar(rs.getString("pro_mar"));
                objProducto.setPro_clas(rs.getString("pro_clas"));
                objProducto.setPro_tip(rs.getString("pro_tip"));
                objProducto.setPro_ordkar(rs.getInt("pro_ordkar"));
                objProducto.setPro_ordlstprec(rs.getInt("pro_ordlstprec"));
                objProducto.setPro_ordcons(rs.getInt("pro_ordcons"));
                objProducto.setPro_ord(rs.getInt("pro_ord"));
                objProducto.setPro_tipexisun(rs.getInt("pro_tipexisun"));
                objProducto.setPro_indperc(rs.getInt("pro_indperc"));
                objProducto.setPro_medunisun(rs.getString("pro_medunisun"));
                objProducto.setPro_provid(rs.getString("pro_provid"));
                objProducto.setPro_codori(rs.getString("pro_codori"));
                objProducto.setPro_conv(rs.getString("pro_conv"));
                objProducto.setPro_unimas(rs.getInt("pro_unimas"));
                objProducto.setPro_unimanven(rs.getString("pro_unimanven"));
                objProducto.setPro_empindven(rs.getString("pro_empindven"));
                objProducto.setPro_presminven(rs.getInt(25));
                objProducto.setPro_unibodeguero(rs.getInt(26));
                objProducto.setPro_frac(rs.getString(27));
                objProducto.setPro_proc(rs.getString(28));
                objProducto.setImp_id(rs.getInt(29));
                objProducto.setPro_condimp(rs.getString(30));
                objProducto.setPro_ubi(rs.getInt(31));
                objProducto.setPro_alt(rs.getDouble(32));
                objProducto.setPro_anc(rs.getDouble(33));
                objProducto.setPro_lar(rs.getDouble(34));
                objProducto.setPro_rot(rs.getInt(35));
                objProducto.setPro_peso(rs.getDouble(36));
                objProducto.setPro_unipeso(rs.getString(37));
                objProducto.setPro_vol(rs.getDouble(38));
                objProducto.setPro_unimancom(rs.getString(39));
                objProducto.setPro_empindcom(rs.getString(40));
                objProducto.setPro_presmincom(rs.getInt(41));
                objProducto.setPro_scknofact(rs.getInt(42));
                objProducto.setPro_codbar(rs.getString(43));
                objProducto.setPro_tipcodbar(rs.getString(44));
                objProducto.setPro_codean13prov(rs.getString(45));
                objProducto.setPro_codean14prov(rs.getString(46));
                objProducto.setPro_imgcodbar(rs.getString(47));
                objProducto.setPro_img(rs.getString(48));
                objProducto.setPro_usuadd(rs.getString(49));
                objProducto.setPro_fecadd(rs.getTimestamp(50));
                objProducto.setPro_usumod(rs.getString(51));
                objProducto.setPro_fecmod(rs.getTimestamp(52));
                objProducto.setPro_sigprov(rs.getString(53));
                objProducto.setPro_desmar(rs.getString(54));
                objProducto.setPro_sublin(rs.getString(55));
                objProducto.setPro_linea(rs.getString(56));
                objProducto.setPro_afecam(rs.getInt(57));
                objProducto.setPro_provpresukey(rs.getInt(58));
                objProducto.setPro_provpresuid(rs.getString(59));
                objProducto.setPro_provpresudes(rs.getString(60));

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
        return objProducto;
    }

    public Productos buscarProdxTodo(String producto, String prov_id, String linea, String sublinea) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_provid like '" + prov_id + "' "
                + "and p.pro_lin like '" + linea + "' and p.pro_sublin like '" + sublinea + "' and p.pro_id like '" + producto + "'";
        objProducto = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
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
        return objProducto;
    }

    public Productos listaPro(String codkey) throws SQLException {
        String SQL_LISTAPRODUCTOS = " select t.pro_id , t.pro_key , t.pro_des , t.pro_desdes , t.pro_presminven , t.pro_unimanven , t.pro_peso , t.pro_unipeso , (t.pro_alt * t.pro_anc * t.pro_lar) vol_uni from codijisa.tproductos t "
                + " where t.pro_id = '" + codkey + "' and t.pro_est = 1";
        objProducto = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPRODUCTOS);

            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString("pro_id"));
                objProducto.setPro_key(rs.getInt("pro_key"));
                objProducto.setPro_des(rs.getString("pro_des"));
                objProducto.setPro_desdes(rs.getString("pro_desdes"));
                objProducto.setPro_presminven(rs.getInt("pro_presminven"));
                objProducto.setPro_unimanven(rs.getString("pro_unimanven"));
                objProducto.setPro_peso(rs.getDouble("pro_peso"));
                objProducto.setPro_unipeso(rs.getString("pro_unipeso"));
                objProducto.setPro_vol(rs.getDouble("vol_uni"));
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos ");
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
        return objProducto;
    }

    public Productos getNomProductos(String pro_id) throws SQLException {
        String SQL_LISTAPRODUCTOS = "select * from codijisa.v_listaproductos t "
                + "where t.pro_est = 1 and t.pro_id = '" + pro_id + "' ";
        objProducto = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPRODUCTOS);
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString("pro_id"));
                objProducto.setPro_key(rs.getInt("pro_key"));
                objProducto.setPro_est(rs.getInt("pro_est"));
                objProducto.setPro_des(rs.getString("pro_des"));
                objProducto.setPro_desdes(rs.getString("pro_desdes"));
                objProducto.setPro_descor(rs.getString("pro_descor"));
                objProducto.setPro_lin(rs.getString("pro_lin"));
                objProducto.setPro_sublin(rs.getString("pro_sublin"));
                objProducto.setPro_mar(rs.getString("pro_mar"));
                objProducto.setPro_clas(rs.getString("pro_clas"));
                objProducto.setPro_tip(rs.getString("pro_tip"));
                objProducto.setPro_ordkar(rs.getInt("pro_ordkar"));
                objProducto.setPro_ordlstprec(rs.getInt("pro_ordlstprec"));
                objProducto.setPro_ordcons(rs.getInt("pro_ordcons"));
                objProducto.setPro_ord(rs.getInt("pro_ord"));
                objProducto.setPro_tipexisun(rs.getInt("pro_tipexisun"));
                objProducto.setPro_indperc(rs.getInt("pro_indperc"));
                objProducto.setPro_medunisun(rs.getString("pro_medunisun"));
                objProducto.setPro_provid(rs.getString("pro_provid"));
                objProducto.setPro_codori(rs.getString("pro_codori"));
                objProducto.setPro_conv(rs.getString("pro_conv"));
                objProducto.setPro_unimas(rs.getInt("pro_unimas"));
                objProducto.setPro_unimanven(rs.getString("pro_unimanven"));
                objProducto.setPro_empindven(rs.getString("pro_empindven"));
                objProducto.setPro_presminven(rs.getInt("pro_presminven"));
                objProducto.setPro_frac(rs.getString("pro_frac"));
                objProducto.setPro_proc(rs.getString("pro_proc"));
                objProducto.setImp_id(rs.getInt("imp_id"));
                objProducto.setPro_condimp(rs.getString("pro_condimp"));
                objProducto.setPro_ubi(rs.getInt("pro_ubi"));
                objProducto.setPro_alt(rs.getDouble("pro_alt"));
                objProducto.setPro_anc(rs.getDouble("pro_anc"));
                objProducto.setPro_lar(rs.getDouble("pro_lar"));
                objProducto.setPro_rot(rs.getInt("pro_rot"));
                objProducto.setPro_peso(rs.getDouble("pro_peso"));
                objProducto.setPro_unipeso(rs.getString("pro_unipeso"));
                objProducto.setPro_vol(rs.getDouble("pro_vol"));
                objProducto.setPro_unimancom(rs.getString("pro_unimancom"));
                objProducto.setPro_empindcom(rs.getString("pro_empindcom"));
                objProducto.setPro_presmincom(rs.getInt("pro_presmincom"));
                objProducto.setPro_scknofact(rs.getInt("pro_scknofact"));
                objProducto.setPro_codbar(rs.getString("pro_codbar"));
                objProducto.setPro_tipcodbar(rs.getString("pro_tipcodbar"));
                objProducto.setPro_codean13prov(rs.getString("pro_codean13prov"));
                objProducto.setPro_codean14prov(rs.getString("pro_codean14prov"));
                objProducto.setPro_imgcodbar(rs.getString("pro_imgcodbar"));
                objProducto.setPro_img(rs.getString("pro_img"));
                objProducto.setPro_usuadd(rs.getString("pro_usuadd"));
                objProducto.setPro_fecadd(rs.getTimestamp("pro_fecadd"));
                objProducto.setPro_usumod(rs.getString("pro_usumod"));
                objProducto.setPro_fecmod(rs.getTimestamp("pro_fecmod"));
                objProducto.setPro_sigprov(rs.getString("pro_sigprov"));
                objProducto.setPro_desmar(rs.getString("pro_desmar"));
                objProducto.setPro_linea(rs.getString("tab_subdes"));
                objProducto.setPro_afecam(rs.getInt("pro_afecam"));
                objProducto.setPro_sublinea(rs.getString("dessublin"));
                objProducto.setPro_provpresukey(rs.getInt("provpresu_key"));
                objProducto.setPro_provpresuid(rs.getString("provpresu_id"));
                objProducto.setPro_provpresudes(rs.getString("provpresu_des"));

            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos ");
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
        return objProducto;
    }

    public ListModelList<Productos> lstProductos(int caso) throws SQLException {
        String SQL_PRODUCTOS;
        if (caso == 0) {
            SQL_PRODUCTOS = "  select * from v_listaproductos p where p.pro_est <> 0 order by p.pro_id  ";
        } else {
            SQL_PRODUCTOS = "  select * from v_listaproductos p where p.pro_est  = 1 order by p.pro_id  ";
        }
        P_WHERE = SQL_PRODUCTOS;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_PRODUCTOS);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString("pro_id"));
                objProducto.setPro_key(rs.getInt("pro_key"));
                objProducto.setPro_est(rs.getInt("pro_est"));
                objProducto.setPro_des(rs.getString("pro_des"));
                objProducto.setPro_desdes(rs.getString("pro_desdes"));
                objProducto.setPro_descor(rs.getString("pro_descor"));
                objProducto.setPro_lin(rs.getString("pro_lin"));
                objProducto.setPro_sublin(rs.getString("pro_sublin"));
                objProducto.setPro_mar(rs.getString("pro_mar"));
                objProducto.setPro_clas(rs.getString("pro_clas"));
                objProducto.setPro_tip(rs.getString("pro_tip"));
                objProducto.setPro_ordkar(rs.getInt("pro_ordkar"));
                objProducto.setPro_ordlstprec(rs.getInt("pro_ordlstprec"));
                objProducto.setPro_ordcons(rs.getInt("pro_ordcons"));
                objProducto.setPro_ord(rs.getInt("pro_ord"));
                objProducto.setPro_tipexisun(rs.getInt("pro_tipexisun"));
                objProducto.setPro_indperc(rs.getInt("pro_indperc"));
                objProducto.setPro_medunisun(rs.getString("pro_medunisun"));
                objProducto.setPro_provid(rs.getString("pro_provid"));
                objProducto.setPro_codori(rs.getString("pro_codori"));
                objProducto.setPro_conv(rs.getString("pro_conv"));
                objProducto.setPro_unimas(rs.getInt("pro_unimas"));
                objProducto.setPro_unimanven(rs.getString("pro_unimanven"));
                objProducto.setPro_empindven(rs.getString("pro_empindven"));
                objProducto.setPro_presminven(rs.getInt("pro_presminven"));
                objProducto.setPro_unibodeguero(rs.getInt("pro_unibodeguero"));
                objProducto.setPro_frac(rs.getString("pro_frac"));
                objProducto.setPro_proc(rs.getString("pro_proc"));
                objProducto.setImp_id(rs.getInt("imp_id"));
                objProducto.setPro_condimp(rs.getString("pro_condimp"));
                objProducto.setPro_ubi(rs.getInt("pro_ubi"));
                objProducto.setPro_alt(rs.getDouble("pro_alt"));
                objProducto.setPro_anc(rs.getDouble("pro_anc"));
                objProducto.setPro_lar(rs.getDouble("pro_lar"));
                objProducto.setPro_rot(rs.getInt("pro_rot"));
                objProducto.setPro_peso(rs.getDouble("pro_peso"));
                objProducto.setPro_unipeso(rs.getString("pro_unipeso"));
                objProducto.setPro_vol(rs.getDouble("pro_vol"));
                objProducto.setPro_unimancom(rs.getString("pro_unimancom"));
                objProducto.setPro_empindcom(rs.getString("pro_empindcom"));
                objProducto.setPro_presmincom(rs.getInt("pro_presmincom"));
                objProducto.setPro_scknofact(rs.getInt("pro_scknofact"));
                objProducto.setPro_codbar(rs.getString("pro_codbar"));
                objProducto.setPro_tipcodbar(rs.getString("pro_tipcodbar"));
                objProducto.setPro_codean13prov(rs.getString("pro_codean13prov"));
                objProducto.setPro_codean14prov(rs.getString("pro_codean14prov"));
                objProducto.setPro_imgcodbar(rs.getString("pro_imgcodbar"));
                objProducto.setPro_img(rs.getString("pro_img"));
                objProducto.setPro_usuadd(rs.getString("pro_usuadd"));
                objProducto.setPro_fecadd(rs.getTimestamp("pro_fecadd"));
                objProducto.setPro_usumod(rs.getString("pro_usumod"));
                objProducto.setPro_fecmod(rs.getTimestamp("pro_fecmod"));
                objProducto.setPro_sigprov(rs.getString("pro_sigprov"));
                objProducto.setPro_desmar(rs.getString("pro_desmar"));
                objProducto.setPro_linea(rs.getString("tab_subdes"));
                objProducto.setPro_afecam(rs.getInt("pro_afecam"));
                objProducto.setPro_sublinea(rs.getString("dessublin"));
                objProducto.setPro_provpresukey(rs.getInt("provpresu_key"));
                objProducto.setPro_provpresuid(rs.getString("provpresu_id"));
                objProducto.setPro_provpresudes(rs.getString("provpresu_des"));

                objlstProductos.add(objProducto);
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos y ha encontrado " + objlstProductos.getSize() + " registro(s)");
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
        return objlstProductos;
    }

    public ListModelList<Productos> busquedaProductos(int i_seleccion, String s_consulta, int i_estado) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "";
        //Busqueda por ID
        if (i_seleccion == 1) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_id like '" + s_consulta + "' and p.pro_est = " + i_estado + "  order by p.pro_id";
        } //Busqueda por CODIGO
        else if (i_seleccion == 2) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_key like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por DESCRIPCION
        else if (i_seleccion == 3) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_des like '" + s_consulta + "'  and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por PROVEEDOR
        else if (i_seleccion == 4) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_sigprov like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por LINEA
        else if (i_seleccion == 5) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.tab_subdes like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por CODIGO DE ORIGEN
        else if (i_seleccion == 6) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_codori like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por PESO
        else if (i_seleccion == 7) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_peso like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } //Busqueda por CLASIFICACION
        else if (i_seleccion == 8) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_clas like '" + s_consulta + "' and p.pro_est = " + i_estado + " order by p.pro_id";
        } else if (i_seleccion == 0) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_est = " + i_estado + " order by p.pro_id";
        }
        P_WHERE = SQL_BUSQUEDA_PRODUCTO;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
                objProducto.setPro_lin(rs.getString(7));
                objProducto.setPro_sublin(rs.getString(8));
                objProducto.setPro_mar(rs.getString(9));
                objProducto.setPro_clas(rs.getString(10));
                objProducto.setPro_tip(rs.getString(11));
                objProducto.setPro_ordkar(rs.getInt(12));
                objProducto.setPro_ordlstprec(rs.getInt(13));
                objProducto.setPro_ordcons(rs.getInt(14));
                objProducto.setPro_ord(rs.getInt(15));
                objProducto.setPro_tipexisun(rs.getInt(16));
                objProducto.setPro_indperc(rs.getInt(17));
                objProducto.setPro_medunisun(rs.getString(18));
                objProducto.setPro_provid(rs.getString(19));
                objProducto.setPro_codori(rs.getString(20));
                objProducto.setPro_conv(rs.getString(21));
                objProducto.setPro_unimas(rs.getInt(22));
                objProducto.setPro_unimanven(rs.getString(23));
                objProducto.setPro_empindven(rs.getString(24));
                objProducto.setPro_presminven(rs.getInt(25));
                objProducto.setPro_presminven(rs.getInt(26));
                objProducto.setPro_frac(rs.getString(27));
                objProducto.setPro_proc(rs.getString(28));
                objProducto.setImp_id(rs.getInt(29));
                objProducto.setPro_condimp(rs.getString(30));
                objProducto.setPro_ubi(rs.getInt(31));
                objProducto.setPro_alt(rs.getDouble(32));
                objProducto.setPro_anc(rs.getDouble(33));
                objProducto.setPro_lar(rs.getDouble(34));
                objProducto.setPro_rot(rs.getInt(35));
                objProducto.setPro_peso(rs.getDouble(36));
                objProducto.setPro_unipeso(rs.getString(37));
                objProducto.setPro_vol(rs.getDouble(38));
                objProducto.setPro_unimancom(rs.getString(39));
                objProducto.setPro_empindcom(rs.getString(40));
                objProducto.setPro_presmincom(rs.getInt(41));
                objProducto.setPro_scknofact(rs.getInt(42));
                objProducto.setPro_codbar(rs.getString(43));
                objProducto.setPro_tipcodbar(rs.getString(44));
                objProducto.setPro_codean13prov(rs.getString(45));
                objProducto.setPro_codean14prov(rs.getString(46));
                objProducto.setPro_imgcodbar(rs.getString(47));
                objProducto.setPro_img(rs.getString(48));
                objProducto.setPro_usuadd(rs.getString(49));
                objProducto.setPro_fecadd(rs.getTimestamp(50));
                objProducto.setPro_usumod(rs.getString(51));
                objProducto.setPro_fecmod(rs.getTimestamp(52));
                objProducto.setPro_sigprov(rs.getString(53));
                objProducto.setPro_desmar(rs.getString(54));
                objProducto.setPro_sublinea(rs.getString(55));
                objProducto.setPro_linea(rs.getString(56));
                objProducto.setPro_afecam(rs.getInt(57));
                objProducto.setPro_provpresukey(rs.getInt("provpresu_key"));
                objProducto.setPro_provpresuid(rs.getString("provpresu_id"));
                objProducto.setPro_provpresudes(rs.getString("provpresu_des"));

                objlstProductos.add(objProducto);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            con.close();
            System.out.print("error de conexion por " + e.getMessage());
        } finally {
            con.close();
        }
        return objlstProductos;
    }

    public ListModelList<Productos> cargarProductosXProveedor(String prov_id) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos t where t.pro_provid = " + prov_id + " ";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public ListModelList<Productos> buscarProductoxTodo(String producto, String prov_id, String linea, String sublinea, String op) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO;
        if (op.equals("c")) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_provid like '" + prov_id + "' "
                    + "and p.pro_lin like '" + linea + "' and p.pro_sublin like '" + sublinea + "' and p.pro_id like '" + producto + "'";
        } else {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos p where p.pro_provid like '" + prov_id + "' "
                    + "and p.pro_lin like '" + linea + "' and p.pro_sublin like '" + sublinea + "' and p.pro_des like '" + producto + "'";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public ListModelList<Productos> buscarProductoxProveedor(String producto, String prov_id) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO;
        if (prov_id.isEmpty()) {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos t where t.pro_des like '" + producto + "' ";
        } else {
            SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos t where t.pro_provid = " + prov_id + " and t.pro_des like '" + producto + "' ";
        }
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public ListModelList<Productos> cargarProductosXProveedorListaPrecio(String prov_id, String lp_id) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "select * from v_listaprecios t where t.prov_id = '" + prov_id + "' and t.lp_id = '" + lp_id + "' and t.lp_tipo=1";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString("pro_id"));
                objProducto.setPro_des(rs.getString("pro_des"));
                objProducto.setPro_desdes(rs.getString("pro_desdes"));
                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public ListModelList<Productos> busquedaProdxProv(long prov_id, String pro_des) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO = "select * from v_listaproductos t where t.pro_provid=" + prov_id + " and t.pro_des like '" + pro_des + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_key(rs.getInt(2));
                objProducto.setPro_est(rs.getInt(3));
                objProducto.setPro_des(rs.getString(4));
                objProducto.setPro_desdes(rs.getString(5));
                objProducto.setPro_descor(rs.getString(6));
                objProducto.setPro_lin(rs.getString(7));
                objProducto.setPro_sublin(rs.getString(8));
                objProducto.setPro_mar(rs.getString(9));
                objProducto.setPro_clas(rs.getString(10));
                objProducto.setPro_tip(rs.getString(11));
                objProducto.setPro_ordkar(rs.getInt(12));
                objProducto.setPro_ordlstprec(rs.getInt(13));
                objProducto.setPro_ordcons(rs.getInt(14));
                objProducto.setPro_ord(rs.getInt(15));
                objProducto.setPro_tipexisun(rs.getInt(16));
                objProducto.setPro_indperc(rs.getInt(17));
                objProducto.setPro_medunisun(rs.getString(18));
                objProducto.setPro_provid(rs.getString(19));
                objProducto.setPro_codori(rs.getString(20));
                objProducto.setPro_conv(rs.getString(21));
                objProducto.setPro_unimas(rs.getInt(22));
                objProducto.setPro_unimanven(rs.getString(23));
                objProducto.setPro_empindven(rs.getString(24));
                objProducto.setPro_presminven(rs.getInt(25));
                objProducto.setPro_frac(rs.getString(26));
                objProducto.setPro_proc(rs.getString(27));
                objProducto.setImp_id(rs.getInt(28));
                objProducto.setPro_condimp(rs.getString(29));
                objProducto.setPro_ubi(rs.getInt(30));
                objProducto.setPro_alt(rs.getDouble(31));
                objProducto.setPro_anc(rs.getDouble(32));
                objProducto.setPro_lar(rs.getDouble(33));
                objProducto.setPro_rot(rs.getInt(34));
                objProducto.setPro_peso(rs.getDouble(35));
                objProducto.setPro_unipeso(rs.getString(36));
                objProducto.setPro_vol(rs.getDouble(37));
                objProducto.setPro_unimancom(rs.getString(38));
                objProducto.setPro_empindcom(rs.getString(39));
                objProducto.setPro_presmincom(rs.getInt(40));
                objProducto.setPro_scknofact(rs.getInt(41));
                objProducto.setPro_codbar(rs.getString(42));
                objProducto.setPro_tipcodbar(rs.getString(43));
                objProducto.setPro_codean13prov(rs.getString(44));
                objProducto.setPro_codean14prov(rs.getString(45));
                objProducto.setPro_imgcodbar(rs.getString(46));
                objProducto.setPro_img(rs.getString(47));
                objProducto.setPro_usuadd(rs.getString(48));
                objProducto.setPro_fecadd(rs.getTimestamp(49));
                objProducto.setPro_usumod(rs.getString(50));
                objProducto.setPro_fecmod(rs.getTimestamp(51));
                objProducto.setPro_sigprov(rs.getString(52));
                objProducto.setPro_desmar(rs.getString(53));
                objProducto.setPro_sublinea(rs.getString(54));
                objProducto.setPro_linea(rs.getString(55));
                objProducto.setPro_afecam(rs.getInt(56));
                objProducto.setPro_provpresukey(rs.getInt("provpresu_key"));
                objProducto.setPro_provpresuid(rs.getString("provpresu_id"));
                objProducto.setPro_provpresudes(rs.getString("provpresu_des"));

                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public String validaMovimientos(Productos objProducto) throws SQLException {
        cst = null;
        i_flagErrorBD = 0;
        s_msg = "";
        String SQL_VALIDA_MOVIMIENTOS = "{call pack_tproductos.p_valida_movimientos(?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_VALIDA_MOVIMIENTOS);
            cst.clearParameters();
            cst.setString(1, objProducto.getPro_id());
            cst.registerOutParameter(2, java.sql.Types.NUMERIC);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(3);
            i_flagErrorBD = cst.getInt(2);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | se valido movimientos del producto " + objProducto.getPro_id());
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

    //Carga productos por orden de compra
    public ListModelList<Productos> cargarProductosXOrdenCompra(String codOrden, String listapre) throws SQLException {
        int listaid = Integer.parseInt(listapre);
        String SQL_BUSQUEDA_PRODUCTO = "select t3.pro_id,t3.pro_des,t1.oc_lpcid from tordcompra_cab t1, tordcompra_det t2,tproductos t3 where t2.pro_id=t3.pro_id"
                + " and t2.ocd_est=1 and t1.emp_id=t2.emp_id and t1.suc_id=t2.suc_id and t1.emp_id=" + objUsuCredential.getCodemp() + " and t1.suc_id=" + objUsuCredential.getCodsuc()
                + " and t1.oc_nropedkey=t2.oc_nropedkey and t1.oc_nropedkey=" + codOrden + " and t1.oc_lpcid like '" + listaid + "'";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);
            objlstProductos = new ListModelList<Productos>();
            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setPro_id(rs.getString(1));
                objProducto.setPro_des(rs.getString(2));
                objlstProductos.add(objProducto);
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
        return objlstProductos;
    }

    public String getDescripcionCortaProducto(String pro_id) throws SQLException {
        String SQL_LISTAPRODUCTOS = "select t.pro_desdes from codijisa.v_listaproductos t "
                + "where t.pro_est = 1 and t.pro_id = '" + pro_id + "' ";
        String desc = "";
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_LISTAPRODUCTOS);
            while (rs.next()) {
                desc = rs.getString("pro_desdes");
            }
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | ha cargado los datos ");
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
        return desc;
    }

    public ParametrosSalida stockProducto(String periodo, int almacen, String producto, String ubicacion) throws SQLException {
        i_flagErrorBD = 0;
        s_msg = "";
        cant_Stock = 0;
        cst = null;
        String SQL_STOCKPRODUCTO = "{call pack_tstocks.p_stockProducto(?,?,?,?,?,?,?,?,?)}";
        try {
            con = new ConectaBD().conectar();
            cst = con.prepareCall(SQL_STOCKPRODUCTO);
            cst.clearParameters();
            cst.setInt(1, objUsuCredential.getCodemp());
            cst.setInt(2, objUsuCredential.getCodsuc());
            cst.setString(3, periodo);
            cst.setInt(4, almacen);
            cst.setString(5, producto);
            cst.setString(6, ubicacion);
            cst.registerOutParameter(7, java.sql.Types.NUMERIC);
            cst.registerOutParameter(8, java.sql.Types.NUMERIC);
            cst.registerOutParameter(9, java.sql.Types.VARCHAR);
            cst.execute();
            s_msg = cst.getString(9);
            i_flagErrorBD = cst.getInt(8);
            cant_Stock = cst.getInt(7);
            LOGGER.info("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | Se cargo stock de un producto : " + producto);
        } catch (SQLException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            cant_Stock = 0;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no pudo realizar la creación de un registro debido al error " + e.toString());
        } catch (NullPointerException e) {
            i_flagErrorBD = 1;
            s_msg = "Ocurrio una Excepcion debido al Error " + e.toString();
            cant_Stock = 0;
            LOGGER.error("[" + objUsuCredential.getComputerName() + "] | " + objUsuCredential.getCuenta() + " | no puedo realizar la creación porque no existe conexión a BD debido al error" + e.toString());
        } finally {
            if (con != null) {
                cst.close();
                con.close();
            }
        }
        return new ParametrosSalida(i_flagErrorBD, s_msg, cant_Stock);
    }

    public Productos infoVolTotProUbi(String codUbi, int almacen) throws SQLException {
        String SQL_BUSQUEDA_PRODUCTO;
        SQL_BUSQUEDA_PRODUCTO = "select  s.alm_key,s.ubic_key, "
                + " (u.ubic_alt*u.ubic_lar*u.ubic_anc) volubi, nvl(sum(p.pro_vol*s.stk_actual),0) voltotprod , (u.ubic_alt*u.ubic_lar*u.ubic_anc) - (nvl(sum(p.pro_vol*s.stk_actual),0))voldisp "
                + " from  tproductos p , tstocks s , tubicaciones u"
                + " where s.ubic_key = " + codUbi + " and s.alm_key=" + almacen + " and s.pro_id = p.pro_id and p.pro_est<>0 and u.ubic_key = s.ubic_key "
                + " and s.emp_id=" + objUsuCredential.getCodemp() + " and s.suc_id=" + objUsuCredential.getCodsuc() + ""
                + " group by s.alm_key, s.ubic_key, u.ubic_alt, u.ubic_lar, u.ubic_anc ";
        objProducto = null;
        try {
            con = new ConectaBD().conectar();
            st = con.createStatement();
            rs = st.executeQuery(SQL_BUSQUEDA_PRODUCTO);

            while (rs.next()) {
                objProducto = new Productos();
                objProducto.setVoldisp(rs.getDouble("voldisp"));
                objProducto.setVoltotprod(rs.getDouble("voltotprod"));
                objProducto.setVolubi(rs.getDouble("volubi"));
            }
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
        return objProducto;
    }
}
