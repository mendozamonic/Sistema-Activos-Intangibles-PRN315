/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author monic
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Connection conexion = null;

    public static Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                throw new SQLException("No se encontró el driver de PostgreSQL", ex);
            }

            String url = "jdbc:postgresql://localhost:5434/proyecto";
            String usuario = "postgres";  
            String password = "hdp"; 

            conexion = DriverManager.getConnection(url, usuario, password);
        }
        return conexion;
    }
}
