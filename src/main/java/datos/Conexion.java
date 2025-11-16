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
 
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/proyecto";
    private static final String JDBC_USER = "proyecto";  
    private static final String JDBC_PASSWORD = "proyecto";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✓ PostgreSQL JDBC Driver cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERROR: PostgreSQL JDBC Driver no encontrado!");
            System.err.println("Asegúrate de tener el driver en el classpath.");
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        
        try {
            System.out.println("Intentando conectar a: " + JDBC_URL);
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("✓ Conexión establecida exitosamente");
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ ERROR al conectar a la base de datos:");
            System.err.println("  URL: " + JDBC_URL);
            System.err.println("  Usuario: " + JDBC_USER);
            System.err.println("  SQL State: " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Mensaje: " + e.getMessage());
            
            // Provide helpful error messages
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Connection refused")) {
                    System.err.println("\n  → Posible causa: PostgreSQL no está corriendo o el puerto es incorrecto");
                } else if (e.getMessage().contains("authentication failed")) {
                    System.err.println("\n  → Posible causa: Usuario o contraseña incorrectos");
                } else if (e.getMessage().contains("does not exist")) {
                    System.err.println("\n  → Posible causa: La base de datos 'proyecto' no existe");
                }
            }
            
            throw e;
        }
    }
    
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public static void close(java.sql.PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public static void close(java.sql.ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}