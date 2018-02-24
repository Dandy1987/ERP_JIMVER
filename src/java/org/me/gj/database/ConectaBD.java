package org.me.gj.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.zkoss.zul.Messagebox;

public class ConectaBD {

    public Connection conectar() throws SQLException {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            //conexion = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.200:1521:datamart", "codijisa", "codijisa");
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.252:1521:jimver", "codijisa", "codijisa");
            //con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:jimver", "codijisa", "codijisa");
        } catch (Exception e) {
            Messagebox.show("Error de Conexion por " + e.toString(), "ERP-JIMVER", Messagebox.OK, Messagebox.ERROR);
            con = null;
        }
        return con;
    }

}
