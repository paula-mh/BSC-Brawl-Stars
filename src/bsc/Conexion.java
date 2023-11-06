
package bsc;

import java.sql.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Conexion {
    private static Conexion instancia = null;
    private Connection conexion = null;
    private final Icon iconoError = new ImageIcon(getClass().getResource("/images/error_bsc.png"));
    
    private Conexion() {
        try {
            
            //Establecer la conexión con la base de datos en Windows
            String url = "jdbc:mysql://localhost/BSC2023";
            String usuario = "usuario";
            String pass = "password";
            /*
            //Establecer la conexión con la base de datos en Mac
            String url = "jdbc:mysql://localhost:8889/BSC2023";
            String usuario = "root";
            String pass = "root";
            */
            
            conexion = DriverManager.getConnection(url, usuario, pass);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Ha habido un error al conectarse a la base de datos: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}
